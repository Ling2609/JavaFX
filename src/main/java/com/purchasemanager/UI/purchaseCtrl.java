package com.purchasemanager.UI;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import javafx.util.Duration;

import java.util.Optional;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;



import javafx.fxml.Initializable;

public class purchaseCtrl implements Initializable{
	@FXML
    private Button POBtn;

    @FXML
    private Button ReqBtn;

    @FXML
    private Button SuppBtn;

    @FXML
    private AnchorPane contentPane;

    @FXML
    private AnchorPane drawerPane;

    @FXML
    private Button menuBtn;

    @FXML
    private Button exitIco;

    @FXML
    private Button infoBtn;

    @FXML
    private Button itemsBtn;

    @FXML
    private AnchorPane nvgPane;

    private Scene scene;
    private Stage stage;
    @FXML
    private ImageView HomePng;
    
    @FXML
    private Button returnBtn;
    
	@Override
    public void initialize(URL location, ResourceBundle resources) {
        loadNewContent("/fxml/PMmenu.fxml");
        try 
        {
			EnableButton();
		} 
        catch (IOException e) 
        {
			e.printStackTrace();
		}
    }
	
	
	public void EnableButton() throws IOException
	{
		String line;
		String name=null;
		BufferedReader reader= new BufferedReader(new FileReader("Data/Log.txt"));
		
		while((line=reader.readLine())!=null) 
		{
			String[] data= line.split(",");
			name=data[1];
			
		}
		reader.close();
		
		if(name.equals("admin")) 
		{
			returnBtn.setVisible(true);
		}
		else 
		{
			returnBtn.setVisible(false);
		}
		
	}
	
	
	
	public void loadNewContent(String fxmlFile) {
		
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(1), contentPane);
        fadeOut.setFromValue(1.0);
       	fadeOut.setToValue(0.0); 
       	fadeOut.setCycleCount(1);

       	fadeOut.setOnFinished(event -> {
    	 try {
        	     AnchorPane newContent = FXMLLoader.load(getClass().getResource(fxmlFile));
        	     contentPane.getChildren().setAll(newContent);

        	      AnchorPane.setTopAnchor(newContent, 0.0);
        	      AnchorPane.setBottomAnchor(newContent, 0.0);
        	      AnchorPane.setLeftAnchor(newContent, 0.0);
        	      AnchorPane.setRightAnchor(newContent, 0.0);

        	      FadeTransition fadeIn = new FadeTransition(Duration.seconds(1), contentPane);
        	      fadeIn.setFromValue(0.0);
        	      fadeIn.setToValue(1.0);
        	      fadeIn.setCycleCount(1);
        	      fadeIn.play();
        	      
        	} catch (IOException e) {
        	        e.printStackTrace();
        	}
        });
        fadeOut.play();
	}
	
	
	public boolean SwitchAlert() throws IOException
	{
		Alert alert= new Alert(AlertType.CONFIRMATION);
    	alert.setTitle("Switch Pages?");
    	alert.setContentText("All Unsaved Data will be lost !");
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
	
	


	
    @FXML
    public void handleChangeItems(ActionEvent event) throws IOException {
    	if(SwitchAlert()) {
    	loadNewContent("/fxml/pmViewItems.fxml");
    	}
    }

    @FXML
    private void handleChangePO(ActionEvent event) throws IOException {
    	if(SwitchAlert()) {
    	loadNewContent("/fxml/PMGenPO.fxml");
    	}
    }

    @FXML
    private void handleChangePR(ActionEvent event) throws IOException {
    	if(SwitchAlert()) {
    	loadNewContent("/fxml/PMreq.fxml");}
    }

    @FXML
    private void handleChangeSuppliers(ActionEvent event) throws IOException {
    	if(SwitchAlert()) {
    	loadNewContent("/fxml/pmViewSuppliers.fxml");
    	}
    }
    
    private boolean drawerOpen = false;
    @FXML
    private void toggleDrawer(ActionEvent event) 
    {
    	TranslateTransition transition = new TranslateTransition(Duration.millis(300), drawerPane);

        if (drawerOpen) {
            transition.setToY(+300);
        } else {
            transition.setToY(0);
        }

        transition.play();
        drawerOpen = !drawerOpen;
    }
    

    
    
    @FXML
    public void LogoutB(MouseEvent event) throws IOException {
    	Alert alert= new Alert(AlertType.CONFIRMATION);
    	alert.setTitle("LogOut?");
    	alert.setContentText("All data haven't save will lost");
    	alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO); //add button yes and no
    	Optional<ButtonType> result= alert.showAndWait(); // wait until user select
    	alert.setHeaderText("Do You Want To Log Out ?");
    	
    	if(result.isPresent() && result.get()== ButtonType.YES) {
    		
    		FileWriter writer= new FileWriter("Data/Cache.txt");
    		writer.close();
    		FileWriter writerLog = new FileWriter("Data/Log.txt");
    		writerLog.close();
			Parent root= FXMLLoader.load(getClass().getResource("/fxml/Sample.fxml"));
			stage=(Stage)((Node)event.getSource()).getScene().getWindow();
			scene= new Scene(root);
			stage.setScene(scene);
			stage.show();
    	}
    	
    }
    
    
    @FXML
    public void MenuClick(MouseEvent event) throws IOException {
    	if(SwitchAlert()) 
    	{
    		loadNewContent("/fxml/PMmenu.fxml");
    	}
    	
    }
    
    @FXML
    public void ReturnClick(MouseEvent event) throws IOException
    {
    	if(SwitchAlert()) 
    	{
	    	Parent root= FXMLLoader.load(getClass().getResource("/fxml/adInterface.fxml"));
			stage=(Stage)((Node)event.getSource()).getScene().getWindow();
			scene= new Scene(root);
			stage.setScene(scene);
			stage.show();
		}
    }
  }


