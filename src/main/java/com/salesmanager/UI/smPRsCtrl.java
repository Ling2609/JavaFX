package com.salesmanager.UI;

import java.io.IOException;

import com.groupfx.JavaFXApp.ViewItemList;
import com.salesmanager.source.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;

public class smPRsCtrl {
	
	@FXML
    private TableColumn<SalesM_PRs, String> PRsID;

    @FXML
    private TableColumn<SalesM_PRs, String> Item_ID;

    @FXML
    private TableColumn<SalesM_PRs, Integer> Quantity;

    @FXML
    private TableColumn<SalesM_PRs, String> Date;

    @FXML
    private TableColumn<SalesM_PRs, String> SalesM;
    
    @FXML
    private TableColumn<SalesM_PRs, String> Status;

    @FXML
    private TableView<SalesM_PRs> viewPRsTable;
    
    @FXML
    private TextField txtPRsID;
    
    @FXML
    private TextField txtItem_ID;
    
    @FXML
    private TextField txtQuantity;
    
    @FXML
    private TextField txtDate;
    
    @FXML
    private TextField txtSalesM;
    
    @FXML
    private TextField txtStatus;
    
    private ObservableList<SalesM_PRs> cacheList;
    
    public void initialize() throws IOException 
    {
    	PRsID.setCellValueFactory(new PropertyValueFactory<>("id")); // Use the SalesM_PRs.getID method
    	Item_ID.setCellValueFactory(new PropertyValueFactory<>("item_ID"));
        Quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        Date.setCellValueFactory(new PropertyValueFactory<>("date"));
        SalesM.setCellValueFactory(new PropertyValueFactory<>("salesM"));
        Status.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        load();
    }
    
    public void load() throws IOException 
    {
    	SalesM_PRs listed= new SalesM_PRs();
    	ObservableList<SalesM_PRs> itemList= FXCollections.observableArrayList(); 
    	String[] row= listed.ReadTextFile().toString().split("\n");
    	
    	for(String rows: row) 
    	{
    		String[] spl= rows.split(",");
    		if(spl.length==6) 
    		{
    			itemList.add(new SalesM_PRs(
    					
    					spl[0],
    					spl[1],
    					Integer.parseInt(spl[2]),
    					spl[3],
    					spl[4],
    					spl[5]
    					));
    			
    		}
    	}
    	
    	cacheList = itemList;
    	viewPRsTable.setItems(cacheList);
    }
    
    public void rowClick() {

        SalesM_PRs selectedItem = viewPRsTable.getSelectionModel().getSelectedItem();
        
        if (selectedItem.getStatus().equals("Approved")) {
        	
        	viewPRsTable.getSelectionModel().clearSelection();
        	Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("PR Approved");
            alert.setHeaderText(null);
            alert.setContentText("PR have been approved, you cannot do any edition");
            alert.showAndWait();
                
            } else {
            	if (selectedItem != null) {
                    String id = selectedItem.getId();
                    String itemId = selectedItem.getItem_ID();
                    int quantity = selectedItem.getQuantity();
                    String date = selectedItem.getDate();
                    String salesM = selectedItem.getSalesM();
                    String status = selectedItem.getStatus();
                    
                    txtPRsID.setText(id);
                    txtItem_ID.setText(itemId);
                    txtQuantity.setText(String.valueOf(quantity));
                    txtDate.setText(String.valueOf(date));
                    txtSalesM.setText(salesM);
                    txtStatus.setText(status);
                }
            }
    }
    
    
//    private boolean containsID(ObservableList<SalesM_PRs> List, String id) {
//        for (SalesM_PRs pr : List) {
//            if (pr.getId().equals(id)) {
//            	
//                return true;
//            }
//        }
//        return false;
//    }
    
    @FXML
    public void addeditClick() {
    	
    	SalesM_PRs selectedSupp = viewPRsTable.getSelectionModel().getSelectedItem();	
    	int selectedSuppIndex = viewPRsTable.getSelectionModel().getSelectedIndex();
    	
    	try {
    		
    	SalesM_PRs dataEntry = new SalesM_PRs(
    			
    			txtPRsID.getText().trim(),
    			txtItem_ID.getText().trim(),
    			Integer.parseInt(txtQuantity.getText().trim()),
    			txtDate.getText().trim(),
    			txtSalesM.getText().trim(),
    			txtStatus.getText().trim(),
    			cacheList, selectedSuppIndex
    			
    			);
    	
    	dataEntry.insertCheck(selectedSupp);
    	
    	ObservableList<SalesM_PRs>  tempList = dataEntry.getCacheList();
    	cacheList = tempList;
    	viewPRsTable.setItems(cacheList);
    	clearTextField();
    	
    	} catch (Exception e) {
    		
    		Alert alert = new Alert(AlertType.ERROR);
    		alert.setTitle("Error");
    		alert.setHeaderText(null);
    		alert.setContentText(String.format("Error: %s", e.toString()));
    		alert.showAndWait();
    	}
//    	try {
//	    	if(containsID(cacheList, txtPRsID.getText()) && selectedSupp != null) {
//	
//	    		SalesM_PRs dataEntry = new SalesM_PRs(txtPRsID.getText(), txtItem_ID.getText(),Integer.parseInt(txtQuantity.getText()),txtDate.getText(),txtSalesM.getText(),txtStatus.getText(), cacheList, selectedSuppIndex);
//		    	dataEntry.EditFunc();
//		    	ObservableList<SalesM_PRs>  tempList = dataEntry.getCacheList();
//		    	cacheList = tempList;
//		    	viewPRsTable.setItems(cacheList);
//		    	clearTextField();
//		    	
//	    	} else if (!(containsID(cacheList, txtPRsID.getText())) && selectedSupp == null){	
//	    		
//	    		SalesM_PRs dataEntry = new SalesM_PRs(txtPRsID.getText(), txtItem_ID.getText(),Integer.parseInt(txtQuantity.getText()),txtDate.getText(),txtSalesM.getText(),txtStatus.getText(), cacheList, selectedSuppIndex);
//			    dataEntry.AddFunc();
//			    ObservableList<SalesM_PRs>  tempList = dataEntry.getCacheList();
//			    cacheList = tempList;
//			    viewPRsTable.setItems(cacheList);
//			    clearTextField();
//	    	} else {
//	    		
//	    		Alert alert = new Alert(AlertType.INFORMATION);
//	    		alert.setTitle("Information");
//	    		alert.setHeaderText(null);
//	    		alert.setContentText("Please select the supplier if you want to edit\n OR \n If you want to add a supplier please dont repeat the ID");
//	    		alert.showAndWait();
//	    	}
//    	} catch (Exception e) {
//    		Alert alert = new Alert(AlertType.ERROR);
//            alert.setTitle("Error");
//            alert.setHeaderText(null);
//            alert.setContentText(String.format("Error: %s", e.toString()));
//            alert.showAndWait();
//    	}
    }
    
    public void deleteClick() {
    	int selectedSuppIndex = viewPRsTable.getSelectionModel().getSelectedIndex();
    	
    	SalesM_PRs delIndex = new SalesM_PRs(selectedSuppIndex, cacheList);
    	try {
    		
    		delIndex.DeleteFunc();
    		ObservableList<SalesM_PRs>  tempList = delIndex.getCacheList();
    		cacheList = tempList;
    		viewPRsTable.setItems(cacheList);
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
    	for (SalesM_PRs supplier : cacheList) {
            
            result.append(supplier.getId()).append(",")
                  .append(supplier.getItem_ID()).append(",")
                  .append(supplier.getQuantity()).append(",")
                  .append(supplier.getDate()).append(",")
                  .append(supplier.getSalesM()).append(",")
                  .append(supplier.getStatus()).append("\n");
        }
    	
    	String netString = result.toString();
    	SalesM_PRs note = new SalesM_PRs(netString);
    	note.SaveFunc();
    	
    	clearTextField();
    	reloadClick();
    }
    
    @FXML
    public void reloadClick() throws IOException {
    	cacheList.clear();
    	load();
    }
    
    public void clearTextField() {
    	
    	TextField[] textFields = {txtPRsID, txtItem_ID, txtQuantity, txtDate, txtSalesM, txtStatus};
    	for (TextField field : textFields) {
    	    field.clear();      	
    	}
    }
}
