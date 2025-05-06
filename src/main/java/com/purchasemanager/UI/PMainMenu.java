package com.purchasemanager.UI;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import com.PM.Sources.PMGenPO;
import com.groupfx.JavaFXApp.Purchase_Order;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class PMainMenu {

    @FXML
    private Label DateLbl;

    @FXML
    private Label TimeLbl;
    
    @FXML
    private PieChart ReqChart;

    @FXML
    private Label ReqNum;

    @FXML
    private Label RoleLbl;

    @FXML
    private Label WelcomeLbl;
   
    @FXML
    private Label POApplbl;

    @FXML
    private Label POPendinglbl;

    @FXML
    private Label PORejlbl;


    private String[] data;
   
    @FXML
    public void initialize() throws IOException 
    {
    	String SetName="Welcome, "+SetText();
    	WelcomeLbl.setText(SetName);
    	RoleLbl.setText(data[1].toUpperCase());
    	
    	DateTimeFormatter format= DateTimeFormatter.ofPattern("dd-MM-yyyy");
    	DateTimeFormatter TimeFormat= DateTimeFormatter.ofPattern("HH:mm:ss");
    	Timeline clock= new Timeline(new KeyFrame(Duration.seconds(1), event->
    	{
    		LocalDate Date= LocalDate.now();
    		LocalTime Time= LocalTime.now();
    		DateLbl.setText(Date.format(format));
    		TimeLbl.setText(Time.format(TimeFormat));
    	
    		
    		
    	}));
   
    	SettingPieChart();
    	clock.setCycleCount(Timeline.INDEFINITE);
    	clock.play();
    			
    	
    }
    
    
    
    
    public void SettingPieChart() throws IOException 
    {
    	PMGenPO Data= new PMGenPO();
    	String[]PrData= Data.PieCData().toString().split("\n");
    	int PendingNum=0,ApproveNum=0;
    	for(String Loop: PrData) 
    	{
    		if(Loop.equals("Pending")) 
    		{
    			PendingNum++;
    		}
    		else 
    		{
    			ApproveNum++;
    		}
    	}
    	
    	ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList(
    		    new PieChart.Data("Approved", ApproveNum),
    		    new PieChart.Data("Pending", PendingNum)
    		);
    	ReqChart.setData(pieData);
    	ReqNum.setText(Integer.toString(PendingNum));

    }
    
    
    
    public String SetText() throws IOException 
    {
    	PMGenPO Data= new PMGenPO();
    	BufferedReader reader= new BufferedReader(new FileReader("Data/Log.txt"));
    	String line;
    	int Approve=0,Rejected=0,Pending=0;
    	while((line=reader.readLine())!=null) 
    	{
    		data=line.split(",");
    	}
    	
    	String[]PoS= Data.POStatus().toString().split("\n");
    	for(String Stat :PoS) 
    	{
    		if(Stat.contains("Rejected")) 
    		{
    			Rejected++;
    		}
    		else if(Stat.contains("Approve"))
    		{Approve++;}
    		else {Pending++;}
    		POApplbl.setText(Integer.toString(Approve));
    		POPendinglbl.setText(Integer.toString(Pending));
    		PORejlbl.setText(Integer.toString(Rejected));
    		
    	}
    	
    	
    	String Name=data[0];
    	reader.close();
    	return Name;
    	
    }
    
    
    
}
