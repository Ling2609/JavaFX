package com.groupfx.JavaFXApp;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;


public class Controller {
	
    @FXML
    private ImageView flipImage;

    @FXML
    private Button LoginBtn;
    
    @FXML
    private TextField Pwbx;

    @FXML
    private TextField UsernameBx;
    
    @FXML
    private PasswordField passwordbox;
    
    @FXML
    private Button Github;
    

    @FXML
    private AnchorPane rootPane;
    
    @FXML
    private ImageView pwChangePic;

    private boolean isFirstImg=true;

    private boolean Box=false;

   
    @FXML
    public void initialize() {
    	Rotate rotateY = new Rotate(0, 100, 100, 0, Rotate.Y_AXIS);
        flipImage.getTransforms().add(rotateY);

        Timeline timeline = new Timeline(
            new KeyFrame(Duration.seconds(0), new KeyValue(rotateY.angleProperty(), 0)),
            new KeyFrame(Duration.seconds(5), new KeyValue(rotateY.angleProperty(), 360)) 
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
        
        Platform.runLater(()->
        {
        	rootPane.getScene().setOnKeyPressed(event->
        	{
        		if(event.getCode()==KeyCode.ENTER) 
        		{
        			
        		}
        	});
        });
      
    }
    

    @FXML
    public void forgotClick(MouseEvent event) {
    	
    }

    @FXML
    public void loginAct(ActionEvent event) throws IOException {
    	Authentication auth= new Authentication();
		auth.LoadData();
		String Password;
		if(Box) 
		{
			Password=Pwbx.getText();
		}else
		{
			Password=passwordbox.getText();
		}
		
		
		if(auth.UserAuth(UsernameBx.getText(),Password)) 
		{	
			auth.SwitchScene(event);
		
		}
		else 
		{
			Alert alert= new Alert(AlertType.WARNING);
			alert.setTitle("Warning");
			alert.setContentText("Username or Password Incorrect! ");
			alert.setHeaderText(null);
			alert.showAndWait();
		}
    }
    
    

    
    
    
// Click show Password or Hide Password 
    @FXML
    public void pwClick(MouseEvent event) {
    	pwChangePic.setImage(new Image(getClass().getResource("/img/hide.png").toExternalForm()));
    	
    	if(isFirstImg)
    	{
    		pwChangePic.setImage(new Image(getClass().getResource("/img/hide.png").toExternalForm()));
    		Pwbx.setText(passwordbox.getText());
    		passwordbox.setVisible(false);
    		Pwbx.setVisible(true);
    		Box=true;
    	}
    	else
    	{
    		pwChangePic.setImage(new Image(getClass().getResource("/img/view.png").toExternalForm()));
    		passwordbox.setText(Pwbx.getText());
    		passwordbox.setVisible(true);
    		Pwbx.setVisible(false);
    		Box=false;
    	}
    	isFirstImg=!isFirstImg;  //set the current status
    	
    }
    
    @FXML
    public void githubClick() {
    	new Thread(() -> {
            try {
                Desktop.getDesktop().browse(new URI("https://github.com/Ling2609/JavaFX"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

}
