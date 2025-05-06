package com.groupfx.JavaFXApp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.Optional;

import javafx.animation.TranslateTransition;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
    	Parent root = FXMLLoader.load(getClass().getResource("/fxml/Sample.fxml"));
        Scene scene = new Scene(root, 600, 700);
        
        primaryStage.setTitle("O M E G A");
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
        
        primaryStage.setOnCloseRequest(event->{
        	Alert alert= new Alert(AlertType.CONFIRMATION);
        	alert.setTitle("Close App ?");
        	alert.setHeaderText("Do You Want to Exit app?");
        	alert.setContentText("All haven't save changes will not be saved ");
        	
        	Optional<ButtonType> result= alert.showAndWait();
        	if(result.isPresent() && result.get()== ButtonType.CANCEL) 
        	{	
					event.consume();	
        	}
        	try (FileWriter writer = new FileWriter("Data/Cache.txt")) 
        	{
        		try (FileWriter writerLog = new FileWriter("Data/Log.txt"))
        		{
        			
        		}
			} 
        	catch (IOException e) 
        	{
				e.printStackTrace();
			}
        	
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
