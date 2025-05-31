package com.salesmanager.UI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.salesmanager.source.ItemSuppEntity;
import com.salesmanager.source.SalesM_Items;
import com.salesmanager.source.SalesM_Suppliers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;

public class smSuppsCtrl {
	@FXML
    private TableColumn<SalesM_Suppliers, String> SuppsID;

    @FXML
    private TableColumn<SalesM_Suppliers, String> Name;

    @FXML
    private TableColumn<SalesM_Suppliers, String> ContactNum;

    @FXML
    private TableColumn<SalesM_Suppliers, String> Address;

//    @FXML
//    private TableColumn<SalesM_Suppliers, String> Item_ID;
    
    @FXML
    private TableView<SalesM_Suppliers> viewSuppsTable;
    
    @FXML
    private TextField txtID;
    
    @FXML
    private TextField txtName;
    
    @FXML
    private TextField txtContactN;
    
    @FXML
    private TextArea txtAddress;
    
    @FXML
    private ListView<String> itemBox;
    
    private ObservableList<SalesM_Suppliers> cacheList = FXCollections.observableArrayList();
    
    private ArrayList<String> itemSuppList = new ArrayList<String>();
    
    public ObservableList<SalesM_Suppliers> getSuppItemList() {return cacheList;}
  
    public void initialize() throws IOException 
    {
    	SuppsID.setCellValueFactory(new PropertyValueFactory<>("id")); // Use the SalesM_PRs.getID method
    	Name.setCellValueFactory(new PropertyValueFactory<>("name"));
        ContactNum.setCellValueFactory(new PropertyValueFactory<>("contactNum"));
        Address.setCellValueFactory(new PropertyValueFactory<>("address"));
//        Item_ID.setCellValueFactory(new PropertyValueFactory<>("item"));
        
        load();
    }
    
    public smSuppsCtrl() {
    	
    }
    
    public void load() throws IOException {
    	
    	SalesM_Suppliers listed= new SalesM_Suppliers();
    	ObservableList<SalesM_Suppliers> itemList= FXCollections.observableArrayList();
    	
    	//Use String Builder to form a string that contain data we need
    	String[] rows= listed.ReadTextFile().toString().split("\n");
    	
    	for(String row: rows) {
    		String[] spl= row.split(",");
    		if(spl.length==4) {
    			itemList.add(new SalesM_Suppliers(
    					spl[0],
    					spl[1],
    					spl[2],
    					spl[3]
    					));
    		}
    	}
    	
    	cacheList = itemList;	
    	viewSuppsTable.setItems(cacheList);
    	
//    		String[] itemArr = spl[4].split("-");
//    		
//    		suppItemList.put(spl[0], itemArr);
    	
    	ItemSuppEntity objItemSupp = new ItemSuppEntity();
    	itemSuppList = objItemSupp.ReadTextFile();

    }
    
    @FXML
    public void rowClick() {

        SalesM_Suppliers selectedItem = viewSuppsTable.getSelectionModel().getSelectedItem();
        
        clearTextField();
        
        if (selectedItem != null) {
        	
            String id = selectedItem.getId();
            String Name = selectedItem.getName();
            String ContactN = selectedItem.getContactNum();
            String Address = selectedItem.getAddress();
            
            txtID.setText(id);
            txtName.setText(Name);
            txtContactN.setText(ContactN);
            txtAddress.setText(Address);
            
            
            for(String itemSupp : itemSuppList) {
            	String[] row= itemSupp.split("-");
            	if(id.equals(row[0])) {
            		itemBox.getItems().addAll(row[1]);
            	}
            }
            
            txtID.setEditable(false);
        }
    }
    
    @FXML
    public void addeditClick() {
    	
    	SalesM_Suppliers selectedSupp = viewSuppsTable.getSelectionModel().getSelectedItem();	
    	int selectedSuppIndex = viewSuppsTable.getSelectionModel().getSelectedIndex();
    	String ID = txtID.getText().trim();
    	String SuppName = txtName.getText().trim();
		String SuppContactNum = txtContactN.getText().trim();
		String SuppAddress = txtAddress.getText().trim();
		
    	try {
    	
    	if(SuppName.isEmpty() || SuppContactNum.isEmpty() || SuppAddress.isEmpty()) {
    		
    		Alert alert = new Alert(AlertType.INFORMATION);
    		alert.setContentText("Please Fill in all the textField.");
    		alert.showAndWait();
    	} else {
    		
    		SalesM_Suppliers dataModify = new SalesM_Suppliers(
        			
        			ID,
        			SuppName,
        			SuppContactNum,
        			SuppAddress,
        			itemSuppList,
        			cacheList,
        			selectedSuppIndex
        			);
        	
        	dataModify.insertCheck(selectedSupp);
        	
        	ObservableList<SalesM_Suppliers>  tempList = dataModify.getCacheList();
        	cacheList = tempList;
        	ArrayList<String> tempISList = dataModify.getISList();
        	itemSuppList = tempISList;
        	viewSuppsTable.setItems(cacheList);
    	}
    	} catch (Exception e) {
    		
    		Alert alert = new Alert(AlertType.INFORMATION);
    		alert.setContentText("Please Make Sure You Key In the Data in a Proper Way");
    		alert.showAndWait();
    	}
    	
    	clearTextField();
    	
    }
    
    @FXML
    public void deleteClick() {
    	
    	int selectedSuppIndex = viewSuppsTable.getSelectionModel().getSelectedIndex();
    	SalesM_Suppliers delIndex = new SalesM_Suppliers(selectedSuppIndex, itemSuppList, txtID.getText().trim(), cacheList);
    	
    	try {
    		
    		delIndex.DeleteFunc();
    		ObservableList<SalesM_Suppliers>  tempList = delIndex.getCacheList();
    		cacheList = tempList;
    		ArrayList<String> tempISList = delIndex.getISList();
        	itemSuppList = tempISList;
    		viewSuppsTable.setItems(cacheList);
    		clearTextField();
    		
    	} catch (Exception e) {
    		
    		Alert alert = new Alert(AlertType.INFORMATION);
    		alert.setContentText("Okay this guy tried to remove something that doesnt exist");
    		alert.showAndWait();
    		
    		System.out.println(e);
    	}
    	viewSuppsTable.getSelectionModel().clearSelection();
    }
    
    @FXML
    public void saveClick() throws IOException{
    	
    	StringBuilder result = new StringBuilder();
    	for (SalesM_Suppliers supplier : cacheList) {
            
            result.append(supplier.getId()).append(",")
                  .append(supplier.getName()).append(",")
                  .append(supplier.getContactNum()).append(",")
                  .append(supplier.getAddress()).append("\n");
        }
    	
    	String netString = result.toString();
    	SalesM_Suppliers note = new SalesM_Suppliers(netString, itemSuppList);
    	note.SaveFunc();
    	
    	clearTextField();
    	reloadClick();
    }
    
    @FXML
    public void reloadClick() throws IOException {
    	
    	txtID.setEditable(true);
    	cacheList.clear();
    	clearTextField();
    	load();
    	
    }
    
    public void clearTextField() {
    	
    	TextField[] textFields = {txtID, txtName, txtContactN};
    	for (TextField field : textFields) {
    	    field.clear();      	
    	}
    	
    	txtAddress.clear();
    	itemBox.getItems().clear();
    }
}
