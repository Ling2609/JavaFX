package com.purchasemanager.UI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.animation.TranslateTransition;

public class PMInterface extends Application{
	 @Override
	    public void start(Stage primaryStage) throws Exception {
	    	Parent root = FXMLLoader.load(getClass().getResource("/fxml/Sample.fxml"));
	        Scene scene = new Scene(root, 600, 700);
	        
	        primaryStage.setTitle("O M E G A");
	        primaryStage.setResizable(false);
	        primaryStage.setScene(scene);
	        primaryStage.show();
	    }

	    public static void main(String[] args) {
	        launch(args);
	    }
}
