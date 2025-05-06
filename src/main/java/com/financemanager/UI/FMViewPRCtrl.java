package com.financemanager.UI;

import java.io.IOException;

import com.financemanager.source.FMViewPR;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class FMViewPRCtrl {

    @FXML
    private TableColumn<FMViewPR, String> PrDate;

    @FXML
    private TableColumn<FMViewPR, String> PrId;

    @FXML
    private TableColumn<FMViewPR, String> PrItem;

    @FXML
    private TableColumn<FMViewPR, Integer> PrQty;

    @FXML
    private TableColumn<FMViewPR, String> PrRp;

    @FXML
    private TableColumn<FMViewPR, String> PrStat;

    @FXML
    private TableView<FMViewPR> ViewPR;
    
    
    public void initialize() throws IOException 
    {
    	PrId.setCellValueFactory(new PropertyValueFactory<>("prId"));
    	PrItem.setCellValueFactory(new PropertyValueFactory<>("itemsid"));
    	PrQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
    	PrDate.setCellValueFactory(new PropertyValueFactory<>("date"));
    	PrRp.setCellValueFactory(new PropertyValueFactory<>("salesM"));
    	PrStat.setCellValueFactory(new PropertyValueFactory<>("status"));
    	
    	load();
    }
    
    public void load() throws IOException 
    {
    	FMViewPR Source= new FMViewPR();
    	ObservableList<FMViewPR> obList= FXCollections.observableArrayList();
    	String[] data= Source.ReadTextFile().toString().split("\n");
    	for(String D:data) 
    	{	
    		String[] newData=D.split(",");
    			
    		obList.add(new FMViewPR(
    				newData[0],
    				newData[1],
    				Integer.parseInt(newData[2]),
    				newData[3],
    				newData[4],
    				newData[5]
    				));
    	}
    	
    	ViewPR.setItems(obList);
    	
    }

}
