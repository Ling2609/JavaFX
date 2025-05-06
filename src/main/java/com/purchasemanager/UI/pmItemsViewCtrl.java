package com.purchasemanager.UI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;

import com.PM.Sources.PMViewItems;
import com.groupfx.JavaFXApp.*;

public class pmItemsViewCtrl {

    @FXML
    private TableColumn<PMViewItems, String> ItemsID;

    @FXML
    private TableColumn<PMViewItems, String> ItemsName;

    @FXML
    private TableColumn<PMViewItems, Integer> itemsStock;

    @FXML
    private TableColumn<PMViewItems, Double> itemsUP;

    @FXML
    private TableView<PMViewItems> viewItemTable;
    
    
    public void initialize() throws IOException 
    {
    	ItemsID.setCellValueFactory(new PropertyValueFactory<>("id")); // Use the ViewList.getID method
    	ItemsName.setCellValueFactory(new PropertyValueFactory<>("name"));// same as above but the method different
        itemsStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        itemsUP.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        
        load();
    }
    
    public void load() throws IOException 
    {
    	PMViewItems listed= new PMViewItems();
    	ObservableList<PMViewItems> itemList= FXCollections.observableArrayList(); 
    	String[] row= listed.ReadTextFile().toString().split("\n");
    	
    	for(String rows: row) 
    	{
    		String[] spl= rows.split(",");
    		if(spl.length==4) 
    		{	
    			itemList.add(new PMViewItems(
    					spl[0],
    					spl[1],
    					Integer.parseInt(spl[2]),
    					Double.parseDouble(spl[3])
    					));
    		}
    	}
    	
    	viewItemTable.setItems(itemList);
    }
    

}
