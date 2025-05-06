package com.salesmanager.UI;

import java.io.IOException;

import com.salesmanager.source.SalesM_POs;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;

public class smPOsCtrl {
	@FXML
    private TableColumn<SalesM_POs, String> Id;

    @FXML
    private TableColumn<SalesM_POs, String> itemId;

    @FXML
    private TableColumn<SalesM_POs, Integer> quantity;

    @FXML
    private TableColumn<SalesM_POs, Double> cost;

    @FXML
    private TableColumn<SalesM_POs, String> status;
    
    @FXML
    private TableView<SalesM_POs> viewPOsTable;
    
    private ObservableList<SalesM_POs> cacheList = FXCollections.observableArrayList();
    
    public void initialize() throws IOException 
    {
    	Id.setCellValueFactory(new PropertyValueFactory<>("id")); // Use the SalesM_PRs.getID method
    	itemId.setCellValueFactory(new PropertyValueFactory<>("name"));
        quantity.setCellValueFactory(new PropertyValueFactory<>("contactNum"));
        cost.setCellValueFactory(new PropertyValueFactory<>("address"));
        status.setCellValueFactory(new PropertyValueFactory<>("item"));
        
        load();
    }
    
    public void load() throws IOException 
    {
    	SalesM_POs listed= new SalesM_POs();
    	ObservableList<SalesM_POs> itemList= FXCollections.observableArrayList();
    	String[] row= listed.ReadTextFile().toString().split("\n");
    	
    	for(String rows: row) 
    	{
    		String[] spl= rows.split(",");
    		if(spl.length==6) 
    		{
    			itemList.add(new SalesM_POs(
    					
    					spl[0],
    					spl[1],
    					Integer.parseInt(spl[2]),
    					Double.parseDouble(spl[3]),
    					spl[5]
    					));
    		}
    	}
    	
    	cacheList = itemList;
    	viewPOsTable.setItems(cacheList);
    }    
    
}
