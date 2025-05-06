package com.inventorymanager.UI;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class imUICtrl implements Initializable {
	
	@FXML
	private AnchorPane contentPane;
	
    @FXML
    private Button returnBtn;
    
    private Scene scene;
    private Stage stage;
    
	
	@Override
    public void initialize(URL location, ResourceBundle resources) {
        loadNewContent("/fxml/imItems.fxml");
        try {
			EnableButton();
		}
        catch (IOException e) 
        {
			
			e.printStackTrace();
		}
    }
	
	
	@FXML
	public void ReturnClick(MouseEvent event) throws IOException{
		
		Parent root= FXMLLoader.load(getClass().getResource("/fxml/adInterface.fxml"));
		stage=(Stage)((Node)event.getSource()).getScene().getWindow();
		scene= new Scene(root);
		stage.setScene(scene);
		stage.show();
		
	}
	
	@FXML
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
	
	@FXML
    private void handleChangeItems() {
        loadNewContent("/fxml/imItems.fxml");
    }
	
	@FXML
	private void handleChangeStocks() {
		loadNewContent("/fxml/imStocks.fxml");
	}
	
	@FXML
	private void handleChangeSR() {
		loadNewContent("/fxml/imStockReport.fxml");
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
	
//  << Lambda Explanation>>
//	fadeOut.setOnFinished(new EventHandler<ActionEvent>() {
//	    @Override
//	    public void handle(ActionEvent event) {
//	        try {
//
//	            AnchorPane newContent = FXMLLoader.load(getClass().getResource(fxmlFile));
//	            contentPane.getChildren().setAll(newContent);
//
//	            AnchorPane.setTopAnchor(newContent, 0.0);
//	            AnchorPane.setBottomAnchor(newContent, 0.0);
//	            AnchorPane.setLeftAnchor(newContent, 0.0);
//	            AnchorPane.setRightAnchor(newContent, 0.0);
//
//	            FadeTransition fadeIn = new FadeTransition(Duration.seconds(1.5), contentPane);
//	            fadeIn.setFromValue(0.0);
//	            fadeIn.setToValue(1.0);
//	            fadeIn.setCycleCount(1);
//	            fadeIn.play();
//	        } catch (IOException e) {
//	            e.printStackTrace();
//	        }
//	    }
//	});

	@FXML
    private AnchorPane drawerPane;

    private boolean drawerOpen = false;

    @FXML
    private void toggleDrawer() {
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
    		
    		FileWriter writerLog = new FileWriter("Data/Log.txt");
    		
			Parent root= FXMLLoader.load(getClass().getResource("/fxml/Sample.fxml"));
			Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
    	}
    }
}
