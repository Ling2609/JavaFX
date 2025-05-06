package com.inventorymanager.UI;

import java.io.IOException;

import com.inventorymanager.source.*;
import com.salesmanager.source.SalesM_Items;
import com.salesmanager.source.SalesM_Suppliers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.application.Platform;


public class imStockCtrl {
	
	//For Purchase Order Table
	@FXML
	private TableView<InventoryM_Stocks> POtable;
	
	@FXML
    private TableColumn<InventoryM_Stocks, String> posID;
	
	@FXML
    private TableColumn<InventoryM_Stocks, String> itemsID;

    @FXML
    private TableColumn<InventoryM_Stocks, String> posStatus;

    @FXML
    private TableColumn<InventoryM_Stocks, Integer> posQuantity;

    @FXML
    private TableColumn<InventoryM_Stocks, Double> posPrice;
    
    
    //For Purchase Order Table
    
  	@FXML
  	private TableView<InventoryM_Stocks> stocktable;
  	
  	@FXML
    private TableColumn<InventoryM_Stocks, String> itemID;

    @FXML
    private TableColumn<InventoryM_Stocks, String> itemName;

    @FXML
    private TableColumn<InventoryM_Stocks, Integer> itemStock;
    
    //For TextField
    @FXML
    private TextField txtPOID;
    
    @FXML
    private TextField txtUpdateStock;
    
    @FXML
    private TextField txtItemsID;
    
    @FXML
    private TextField txtItemsName;
    
    @FXML
    private TextField txtItemsStock;
    
    @FXML
    private ObservableList<InventoryM_Stocks> cachePOList = FXCollections.observableArrayList();
    
    @FXML
    private ObservableList<InventoryM_Stocks> cacheStockList = FXCollections.observableArrayList();
    
    private String[] stockRow;
    
    private String[] poRow;
    
    public void initialize() throws IOException 
    {
    	posID.setCellValueFactory(new PropertyValueFactory<>("posID")); // Use the ViewItemList.getID method
    	itemsID.setCellValueFactory(new PropertyValueFactory<>("itemsID"));// same as above but the method different
    	posStatus.setCellValueFactory(new PropertyValueFactory<>("posStatus"));
        posQuantity.setCellValueFactory(new PropertyValueFactory<>("posQuantity"));
        posPrice.setCellValueFactory(new PropertyValueFactory<>("posPrice"));
        
        itemID.setCellValueFactory(new PropertyValueFactory<>("itemStockID"));
        itemName.setCellValueFactory(new PropertyValueFactory<>("itemStockName"));
        itemStock.setCellValueFactory(new PropertyValueFactory<>("itemStock"));
        
        load();
    }
   
    public void load() throws IOException 
    {
    	InventoryM_Stocks listed= new InventoryM_Stocks();
    	ObservableList<InventoryM_Stocks> POList = FXCollections.observableArrayList();
    	
    	//Use String Builder to form a string that contain data we need
    	poRow = listed.ReadTextFile().toString().split("\n");
    	
    	for(String rows: poRow) {
    		
    		String[] spl= rows.split(",");
    		if(spl.length==8) 
    		{	
    			if(spl[5].equals("Approve") && spl[7].equals("Pending")) {
    				
	    			POList.add(new InventoryM_Stocks(
	    					
	    				spl[0],
	    				spl[1],
	    				Integer.parseInt(spl[2]),
	    				Double.parseDouble(spl[3]),
	    				spl[7]
	    				));
    			}
    		}	
    	}
    	
    	// Sort Purchase Order List by posID
    	POList.sort((a, b) -> a.getPosID().compareToIgnoreCase(b.getPosID()));
    	cachePOList = POList;
    	POtable.setItems(cachePOList);

    	
    	
    	ObservableList<InventoryM_Stocks> stockList = FXCollections.observableArrayList();
    	
    	//For Stock Table, becus we need to use this stockRow as a parameter to save data so just declare a private stockRow
    	stockRow = listed.ReadStockTextFile().toString().split("\n");
    	
    	for(String rows: stockRow) {
    		
    		String[] spl= rows.split(",");
    		if(spl.length==5) 
    		{
    			stockList.add(new InventoryM_Stocks(
    					
    					spl[0],
    					spl[1],
    					Integer.parseInt(spl[3])
    					));
    		}	
    	}
    	
    	// Sort Stock List by itemID
    	stockList.sort((a, b) -> a.getItemStockID().compareToIgnoreCase(b.getItemStockID()));
    	cacheStockList = stockList;
    	stocktable.setItems(cacheStockList);
        
        // Wrap checkLowStock() inside Platform.runLater() to avoid illegal access during layout processing
    	Platform.runLater(() -> {
    	    checkStockLevels();  // This will check both low and high stock levels
    	});
    }
    
    @FXML
    public void reloadClick() throws IOException {
    	
    	cachePOList.clear();
    	cacheStockList.clear();
    	clearTextField();
    	
    	load();
    }
    
    
    public void clearTextField() {
    	
    	TextField[] textFields = {txtItemsID, txtItemsName, txtItemsStock, txtUpdateStock, txtPOID};
    	for (TextField field : textFields) {
    	    field.clear();      	
    	}
    }

    public void poRowClick() {
    	
    	try {
    		
	    	InventoryM_Stocks selectedItem = POtable.getSelectionModel().getSelectedItem();
	        
	        if (selectedItem != null) {
	        	
	            String id = selectedItem.getPosID();
	            int uptQuantity = selectedItem.getPosQuantity();
	            
	            txtPOID.setText(id);
	            txtUpdateStock.setText(String.valueOf(uptQuantity));
	            
	        }
	    } catch (Exception e) {
	    	
	    	System.out.println("Error: " + e);
	    }
    }
    
    public void stockRowClick() {
    	
    	try {
	    	InventoryM_Stocks selectedItem = stocktable.getSelectionModel().getSelectedItem();
	        
	        if (selectedItem != null) {
	        	
	            String id = selectedItem.getItemStockID();
	            String name = selectedItem.getItemStockName();
	            int stock = selectedItem.getItemStock();
	            
	            txtItemsID.setText(id);
	            txtItemsName.setText(name);
	            txtItemsStock.setText(String.valueOf(stock));
	            
	        }
	    } catch (Exception e) {
	    	
	    	System.out.println("Error: " + e);
	    }
    }

    public void saveClick() {
    	
    	InventoryM_Stocks selectedItem = stocktable.getSelectionModel().getSelectedItem();	
    	
    	try {
    		
    		if(selectedItem != null) {

	    		InventoryM_Stocks dataModify = new InventoryM_Stocks(
	    				stockRow,
	    				txtItemsID.getText().trim(),
	    				Integer.parseInt(txtItemsStock.getText().trim())
	    				);
	    		
	    		boolean result = dataModify.manageStockIdentifier();
	    		if (result == true) {
	    			
	    			reloadClick();
	    			
	    		} else {
	    			
	    			Alert alert = new Alert(AlertType.INFORMATION);
	        		alert.setContentText("Some error occurs.");
	        		alert.showAndWait();
	    		}
    		} else {
    			
    			Alert alert = new Alert(AlertType.INFORMATION);
        		alert.setContentText("Please select an Item ID.");
        		alert.showAndWait();
    		}
    	} catch (Exception e) {
    		
    		
    	}
    }
    
    public void updateStockClick() {
    	
    	InventoryM_Stocks selectedPO = POtable.getSelectionModel().getSelectedItem();	
    	
    	try {
    		
    		if(selectedPO != null) {

	    		InventoryM_Stocks dataModify = new InventoryM_Stocks(
	    				stockRow,
	    				poRow,
	    				selectedPO.getItemsID(),
	    				Integer.parseInt(txtUpdateStock.getText().trim()),
	    				selectedPO.getPosID()
	    				);
	    		
	    		boolean result = dataModify.updateStockIdentifier();
	    		if (result == true) {
	    			
	    			reloadClick();
	    			
	    		} else {
	    			
	    			Alert alert = new Alert(AlertType.INFORMATION);
	        		alert.setContentText("Some error occurs.");
	        		alert.showAndWait();
	    		}
    		} else {
    			
    			Alert alert = new Alert(AlertType.INFORMATION);
        		alert.setContentText("Please select a Purchase Order.");
        		alert.showAndWait();
    		}
    	} catch (Exception e) {
    		
    		
    	}
    }
    private void checkStockLevels() {
        final int LOW_STOCK_THRESHOLD = 300;
        final int HIGH_STOCK_THRESHOLD = 1000; 
        StringBuilder lowStockMsg = new StringBuilder();
        StringBuilder highStockMsg = new StringBuilder();
        boolean lowStockFound = false;
        boolean highStockFound = false;

        for (InventoryM_Stocks item : cacheStockList) {
            if (item.getItemStock() < LOW_STOCK_THRESHOLD) {
                lowStockFound = true;
                lowStockMsg.append("- ").append(item.getItemStockName())
                            .append(" (Stock: ").append(item.getItemStock()).append(")\n");
            }
            if (item.getItemStock() > HIGH_STOCK_THRESHOLD) {
                highStockFound = true;
                highStockMsg.append("- ").append(item.getItemStockName())
                             .append(" (Stock: ").append(item.getItemStock()).append(")\n");
            }
        }

        if (lowStockFound) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Low Stock Alert");
            alert.setHeaderText("Some items are low in stock!");
            alert.setContentText(lowStockMsg.toString());
            alert.showAndWait();
        }

        if (highStockFound) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("High Stock Alert");
            alert.setHeaderText("Some items have high stock levels!");
            alert.setContentText(highStockMsg.toString());
            alert.showAndWait();
        }
    }
}
