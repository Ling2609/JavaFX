package com.Admin.UI;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;

import com.Admin.Source.AdminSources;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class adDashboardCtrl {

    @FXML
    private Button FMBtn;

    @FXML
    private Button IMBtn;

    @FXML
    private Button PMBtn;

    @FXML
    private Button SMBtn;

    @FXML
    private Label WelcomeLbl;
    
    private Stage stage;
    private Scene scene;
    
    
    public void initialize() throws IOException 
    {
    	AdminSources source= new AdminSources();
    	String name= source.RetName();
    	WelcomeLbl.setText("Welcome ! "+name);
    }
    
	public boolean SwitchAlert(String pages) throws IOException
	{
		Alert alert= new Alert(AlertType.CONFIRMATION);
    	alert.setTitle("Switch Pages?");
    	alert.setContentText("Comfirm Switch to "+pages );
    	alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO); //add button yes and no
    	Optional<ButtonType> result= alert.showAndWait(); // wait until user select
    	alert.setHeaderText("Do You Want To Switch Pages?");
    	
    	if(result.isPresent() && result.get()==ButtonType.YES) 
    	{
    		 FileWriter writer = new FileWriter("Data/Cache.txt");
    		 writer.close();
    		 return true;
    	}
    	return false;
	}
	
	public void SwitchPages(String fxml, MouseEvent event) throws IOException 
	{
		Parent root= FXMLLoader.load(getClass().getResource(fxml));
		stage=(Stage)((Node)event.getSource()).getScene().getWindow();
		scene= new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

    @FXML
    public void FMClick(MouseEvent event) throws IOException {
    	if(SwitchAlert("Financial Managemer")) 
    	{
    		SwitchPages("/fxml/fmInterface.fxml",event);
    	}
    }

    @FXML
    public void IMClick(MouseEvent event) throws IOException {
    	if(SwitchAlert("Inventory Managemer")) 
    	{
    		SwitchPages("/fxml/imInterface.fxml",event);
    	}
    }

    @FXML
    public void PMClick(MouseEvent event) throws IOException {
    	
    	if(SwitchAlert("Purchase Managemer")) 
    	{
    		SwitchPages("/fxml/pmInterface.fxml",event);
    	}
    }

    @FXML
    public void SMClick(MouseEvent event) throws IOException {

    	if(SwitchAlert("Sales Manager")) 
    	{
    		SwitchPages("/fxml/smInterface.fxml",event);
    	}
    }

}
