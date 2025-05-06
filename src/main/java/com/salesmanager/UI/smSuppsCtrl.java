package com.salesmanager.UI;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

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
    
    private HashMap<String, String[]> suppItemList = new HashMap<>();
    
    public ObservableList<SalesM_Suppliers> getSuppItemList() {return cacheList;}
  
    public void initialize() throws IOException 
    {
    	SuppsID.setCellValueFactory(new PropertyValueFactory<>("id")); // Use the SalesM_PRs.getID method
    	Name.setCellValueFactory(new PropertyValueFactory<>("name"));
        ContactNum.setCellValueFactory(new PropertyValueFactory<>("contactNum"));
        Address.setCellValueFactory(new PropertyValueFactory<>("address"));
//        Item_ID.setCellValueFactory(new PropertyValueFactory<>("item"));
        
        load();
    	viewSuppsTable.setItems(cacheList);
    }
    
    public smSuppsCtrl() {
    	
    }
    
    public void load() throws IOException 
    {
    	SalesM_Suppliers listed= new SalesM_Suppliers();
    	ObservableList<SalesM_Suppliers> itemList= FXCollections.observableArrayList();
    	
    	//Use String Builder to form a string that contain data we need
    	String[] row= listed.ReadTextFile().toString().split("\n");
    	
    	for(String rows: row) {
    		
    		String[] spl= rows.split(",");
    		if(spl.length==5) 
    		{
    			itemList.add(new SalesM_Suppliers(
    					
    					spl[0],
    					spl[1],
    					spl[2],
    					spl[3],
    					spl[4]
    					));
    		}
    		
    		String[] itemArr = spl[4].split("-");
    		
    		suppItemList.put(spl[0], itemArr);
    		
    	}
    	
    	cacheList = itemList;
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
            
            for(Map.Entry<String, String[]> entry : suppItemList.entrySet()) {
            	if(id.equals(entry.getKey())) {
            		itemBox.getItems().addAll(entry.getValue());
            	}
            }
            
            txtID.setEditable(false);
        }
    }
    
    private boolean containsID(ObservableList<SalesM_Suppliers> List, String id, String itemId) {
        for (SalesM_Suppliers supplier : List) {
            if (supplier.getId().equals(id) || supplier.getItem().equals(itemId)) {
            	
                return true;
            }
        }
        return false;
    }
    
    @FXML
    public void addeditClick() {
    	
    	SalesM_Suppliers selectedSupp = viewSuppsTable.getSelectionModel().getSelectedItem();	
    	int selectedSuppIndex = viewSuppsTable.getSelectionModel().getSelectedIndex();
    	
    	SalesM_Suppliers dataModify = new SalesM_Suppliers(
    			
    			txtID.getText().trim(),
    			txtName.getText().trim(),
    			txtContactN.getText().trim(),
    			txtAddress.getText().trim(),
    			cacheList, selectedSuppIndex
    			);
    	
    	dataModify.insertCheck(selectedSupp);
    	
    	ObservableList<SalesM_Suppliers>  tempList = dataModify.getCacheList();
    	cacheList = tempList;
    	viewSuppsTable.setItems(cacheList);
    	clearTextField();
    	
//    	try {
//	    	if(containsID(cacheList, txtID.getText(), txtItemID.getText()) && selectedSupp != null) {
//	
//	    		SalesM_Suppliers dataEntry = new SalesM_Suppliers(txtID.getText(), txtName.getText(),txtContactN.getText(),txtAddress.getText(),txtItemID.getText(), cacheList, selectedSuppIndex);
//		    	dataEntry.EditFunc();
//		    	ObservableList<SalesM_Suppliers>  tempList = dataEntry.getCacheList();
//		    	cacheList = tempList;
//		    	viewSuppsTable.setItems(cacheList);
//		    	clearTextField();
//		    	
//	    	} else if (!(containsID(cacheList, txtID.getText(), txtItemID.getText())) && selectedSupp == null){	
//	    		
//	    		SalesM_Suppliers dataEntry = new SalesM_Suppliers(txtID.getText(), txtName.getText(),txtContactN.getText(),txtAddress.getText(),txtItemID.getText(), cacheList, selectedSuppIndex);
//			    dataEntry.AddFunc();
//			    ObservableList<SalesM_Suppliers>  tempList = dataEntry.getCacheList();
//			    cacheList = tempList;
//			    viewSuppsTable.setItems(cacheList);
//			    clearTextField();
//	    	} else {
//	    		
//	    		clearTextField();
//	    		Alert alert = new Alert(AlertType.INFORMATION);
//	    		alert.setTitle("Information");
//	    		alert.setHeaderText(null);
//	    		alert.setContentText("Please select the supplier if you want to edit\n OR \n If you want to add a supplier please dont repeat the ID");
//	    		alert.showAndWait();
//	    	}
//    	} catch (Exception e) {
//    		
//    		clearTextField();
//    		Alert alert = new Alert(AlertType.ERROR);
//            alert.setTitle("Error");
//            alert.setHeaderText(null);
//            alert.setContentText(String.format("Error: %s", e.toString()));
//            alert.showAndWait();
//    	}
    }
    
    @FXML
    public void deleteClick() {
    	
    	int selectedSuppIndex = viewSuppsTable.getSelectionModel().getSelectedIndex();
    	SalesM_Suppliers delIndex = new SalesM_Suppliers(selectedSuppIndex, cacheList);
    	
    	try {
    		
    		delIndex.DeleteFunc();
    		ObservableList<SalesM_Suppliers>  tempList = delIndex.getCacheList();
    		cacheList = tempList;
    		viewSuppsTable.setItems(cacheList);
    		clearTextField();
    		
    	} catch (Exception e) {
    		
    		Alert alert = new Alert(AlertType.INFORMATION);
    		alert.setContentText("Okay this guy tried to remove something that doesnt exist");
    		alert.showAndWait();
    	}
    }
    
    @FXML
    public void saveClick() throws IOException{
    	
    	StringBuilder result = new StringBuilder();
    	for (SalesM_Suppliers supplier : cacheList) {
            
            result.append(supplier.getId()).append(",")
                  .append(supplier.getName()).append(",")
                  .append(supplier.getContactNum()).append(",")
                  .append(supplier.getAddress()).append(",")
                  .append(supplier.getItem()).append("\n");  
        }
    	
    	String netString = result.toString();
    	SalesM_Suppliers note = new SalesM_Suppliers(netString);
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
    	viewSuppsTable.setItems(cacheList);
    	
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
