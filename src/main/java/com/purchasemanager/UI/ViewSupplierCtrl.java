package com.purchasemanager.UI;

import java.io.IOException;

import com.PM.Sources.ListSupplier;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class ViewSupplierCtrl {

    @FXML
    private Label SupTllbl;

    @FXML
    private TableColumn<ListSupplier, String> SuppAdd;

    @FXML
    private TableColumn<ListSupplier, String> SuppID;

    @FXML
    private TableColumn<ListSupplier, String> SuppName;

    @FXML
    private TableColumn<ListSupplier, String> SuppPhNo;

    @FXML
    private TableView<ListSupplier> SupplierView;
    
    @FXML
    private TableColumn<ListSupplier, String> ItemID;

    public void initialize() throws IOException
    {
    	SuppID.setCellValueFactory(new PropertyValueFactory<>("ID"));
    	SuppName.setCellValueFactory(new PropertyValueFactory<>("name")); //variable take from getter getxxx and the xxx must be lowercase
    	SuppPhNo.setCellValueFactory(new PropertyValueFactory<>("phNo"));
    	SuppAdd.setCellValueFactory(new PropertyValueFactory<>("address"));
    	ItemID.setCellValueFactory(new PropertyValueFactory<>("itemsId"));
    	
    	load();
    	
    }
    
    public void load() throws IOException 
    {
    	ListSupplier supplier= new ListSupplier();
    	ObservableList<ListSupplier> itemList= FXCollections.observableArrayList();
    	String[] Items= supplier.ReadTextFile().toString().split("\n");
    	for(String rows: Items) 
    	{	String[] split= rows.split(",");
    		if(split.length==5) 
    		{
    			itemList.add(new ListSupplier(
    						split[0],
    						split[1],
    						split[2],
    						split[3],
    						split[4]
    					
    					));
    		}
    	}
    	
    	SupplierView.setItems(itemList);
    	
    }
    
    
}
