package com.salesmanager.UI;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.inventorymanager.source.InventoryM_Stocks;
import com.salesmanager.source.*;

public class smItemsCtrl {
	@FXML
    private TableColumn<SalesM_Items, String> ItemsID;

    @FXML
    private TableColumn<SalesM_Items, String> ItemsName;

    @FXML
    private TableColumn<SalesM_Items, Integer> itemsStock;

    @FXML
    private TableColumn<SalesM_Items, Double> itemsUP;

    @FXML
    private TableView<SalesM_Items> viewItemTable;
    
    @FXML
    private TextField txtItemsID;
    
    @FXML
    private TextField txtItemsName;
    
    @FXML
    private TextField txtItemsStock;
    
    @FXML
    private TextField txtItemsUP;
    
    @FXML
    private ListView<String> supplierBox;
    
    @FXML
    private BarChart<?,?> viewStockChart;
    
    private ObservableList<SalesM_Items> cacheList = FXCollections.observableArrayList(); 
    
    private HashMap<String, Integer> chartStore = new HashMap<>();
    
    private ArrayList<String> itemSuppList = new ArrayList<String>();
    
    public void initialize() throws IOException 
    {
    	ItemsID.setCellValueFactory(new PropertyValueFactory<>("id")); // Use the ViewItemList.getID method
    	ItemsName.setCellValueFactory(new PropertyValueFactory<>("name"));// same as above but the method different
        itemsStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        itemsUP.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        
        Platform.runLater(() -> {
    		checkStockLevels();
    	});
        
        load();
    }
    
    public void load() throws IOException 
    {	
    	ItemSuppEntity objItemSupp = new ItemSuppEntity();
    	itemSuppList = objItemSupp.ReadTextFile();
    	
    	SalesM_Items listed= new SalesM_Items();
    	ObservableList<SalesM_Items> itemList= FXCollections.observableArrayList(); 
    	String[] row= listed.ReadTextFile().toString().split("\n");
    	
    	for(String rows: row) 
    	{
    		String[] spl= rows.split(",");
    		if(spl.length==4) 
    		{
    			itemList.add(new SalesM_Items(
    					spl[0],
    					spl[1],
    					Integer.parseInt(spl[2]),
    					Double.parseDouble(spl[3])
    					));
    			
    			chartStore.put(spl[1], Integer.parseInt(spl[2]));
    			
    		}
    	}
    	
    	for (String itemSupp : itemSuppList) {
    	    String[] itemSuppRow = itemSupp.split("-");
    	    String suppItemId = itemSuppRow[1];

    	    boolean exists = false;

    	    for (SalesM_Items item : itemList) {
    	        if (item.getId().equals(suppItemId)) {
    	            exists = true;
    	            break;
    	        }
    	    }

    	    if (!exists) {
    	    	
    	    	itemList.add(new SalesM_Items(
    					suppItemId,
    					"",
    					0,
    					0.0
    					));
    	    	
    	    	String idCopy = suppItemId;
    	    	// Use asynchronous to avoid the stuck of the UI thread
    	    	Platform.runLater(() -> {
    	    		Alert alert = new Alert(AlertType.INFORMATION);
    	    		alert.setContentText("Please insert the information for the new item " + idCopy);
    	    		alert.showAndWait();
    	    	});
    	    }
    	}
    	
    	cacheList = itemList;
    	viewItemTable.setItems(cacheList);
    	
    	chartload();
    	clearTextField();
    }
    
    public void chartload() {
    	
    	viewStockChart.getData().clear();
    	XYChart.Series series = new XYChart.Series();
    	series.setName("Items Stock");
    	for (Map.Entry<String, Integer> entry : chartStore.entrySet()) {
            series.getData().add(new XYChart.Data(entry.getKey(), entry.getValue()));
        }
    	
    	viewStockChart.getData().add(series);
    	
    }
    
    public void rowClick() {
    	
    	try {
	    	SalesM_Items selectedItem = viewItemTable.getSelectionModel().getSelectedItem();
	        
	    	clearTextField();
	    	
	        if (selectedItem != null) {
	            String id = selectedItem.getId();
	            String name = selectedItem.getName();
	            int stock = selectedItem.getStock();
	            double unitPrice = selectedItem.getUnitPrice();
	            
//				<<For Testing>>
//	            System.out.println("Selected Item:");
//	            System.out.println("ID: " + id);
//	            System.out.println("Name: " + name);
//	            System.out.println("Supplier: " + supplier);
//	            System.out.println("Stock: " + stock);
//	            System.out.println("Unit Price: " + unitPrice);
	            
	            txtItemsID.setText(id);
	            txtItemsName.setText(name);
	            txtItemsStock.setText(String.valueOf(stock));
	            txtItemsUP.setText(String.valueOf(unitPrice));
	            
	            
	            for(String itemSupp : itemSuppList) {
	            	String[] row= itemSupp.split("-");
	            	if(id.equals(row[1])) {
	            		supplierBox.getItems().addAll(row[0]);
	            	}
	            }
	            
	            txtItemsID.setEditable(false);
	        }
	    } catch (Exception e) {
	    	
	    }
    	
    }
    
//    private boolean containsID(ObservableList<SalesM_Items> List, String id, String Name, String suppId) {
//        for (SalesM_Items item : List) {
//            if (item.getId().equals(id)) {
//            	
//                return true;
//            } else if (item.getName().equals(Name) && item.getSupplier().equals(suppId)) {
//            	
//            	return true;
//            }
//        }
//        return false;
//    }
    
    @FXML
    public void addeditClick() {
    	
    	SalesM_Items selectedSupp = viewItemTable.getSelectionModel().getSelectedItem();	
    	int selectedSuppIndex = viewItemTable.getSelectionModel().getSelectedIndex();
    	String ID = txtItemsID.getText().trim();
		String ItemName = txtItemsName.getText().trim().toLowerCase();
		String Stock = txtItemsStock.getText().trim();
		String UnitPrice = txtItemsUP.getText().trim();

		try {
		    // Check for emptiness first for all fields
		    if (ItemName.isEmpty() || Stock.isEmpty() || UnitPrice.isEmpty()) {
		        Alert alert = new Alert(AlertType.INFORMATION);
		        alert.setContentText("Please fill in all the required fields."); // More specific message
		        alert.showAndWait();
		        return; // Important: Stop execution here if fields are empty
		    }

		    // Validate ItemName (alphabets only)
		    // The regex `[a-zA-Z\\s]*` allows empty string and spaces.
		    // Since we already checked for `isEmpty()` above,
		    // this check now only focuses on characters being alphabets or spaces.
		    if (!ItemName.matches("^[a-zA-Z\\s]+$")) { // Added ^ and $ to match entire string
		        Alert alert = new Alert(AlertType.INFORMATION);
		        alert.setContentText("Item Name can only contain alphabets and spaces.");
		        alert.showAndWait();
		        return; // Stop execution
		    }

		    // Parse numerical values (now we know strings are not empty)
		    int stockValue;
		    double unitPriceValue;
		    try {
		        stockValue = Integer.parseInt(Stock);
		        unitPriceValue = Double.parseDouble(UnitPrice);
		    } catch (NumberFormatException e) {
		    	
		    	showAlert("Please enter valid numbers for Stock and Unit Price.");
		    	
		        return; // Stop execution
		    }

		    // Validate numerical ranges (now we know they are valid numbers)
		    if (stockValue < 0 || unitPriceValue < 0.00) {
		    	
		    	showAlert("Stock and Unit Price cannot be negative.");
		        
		        return; // Stop execution
		    }

		    // If all validations pass, proceed with your logic
		    SalesM_Items dataModify = new SalesM_Items(
		        ID,
		        ItemName,
		        stockValue,
		        unitPriceValue,
		        itemSuppList,
		        cacheList,
		        selectedSuppIndex
		    );

		    dataModify.insertCheck(selectedSupp);
		    
		    String alertText = dataModify.getAlertText();
    		showAlert(alertText);
    		
		    ObservableList<SalesM_Items> tempList = dataModify.getCacheList();
		    cacheList = tempList;

		    ArrayList<String> tempISList = dataModify.getISList();
		    itemSuppList = tempISList;

		    viewItemTable.setItems(cacheList);

		} catch (Exception e) {
		    // This catch block should ideally only catch unexpected errors now,
		    // as most common validation issues are handled by specific `return` statements.
		    // Log the exception for debugging.
		    e.printStackTrace();
		    
		    showAlert("An unexpected error occurred: " + e.getMessage());
		    
		} finally { // The finally block ensures these always run
			
		    clearTextField();
		    viewItemTable.getSelectionModel().clearSelection();
		}
    }
    
    @FXML
    public void deleteClick() {
    	
    	int selectedSuppIndex = viewItemTable.getSelectionModel().getSelectedIndex();
    	
    	SalesM_Items dataModify = new SalesM_Items(
    			txtItemsID.getText().trim(),
    			itemSuppList,
				cacheList,
				selectedSuppIndex
				);
    	
    	try {
    		
    		dataModify.DeleteFunc();
    		
    		String alertText = dataModify.getAlertText();
    		showAlert(alertText);
    		
    		ObservableList<SalesM_Items>  tempList = dataModify.getCacheList();
    		cacheList = tempList;
    		
    		ArrayList<String> tempISList = dataModify.getISList();
        	itemSuppList = tempISList;
        	
    		viewItemTable.setItems(cacheList);
    		clearTextField();
    		
    	} catch (Exception e) {
    		
    		showAlert("Please select a row for deletion");
    		
    	}
    	
    	viewItemTable.getSelectionModel().clearSelection();
    }
    
    @FXML
    public void saveClick() throws IOException{
    	
    	StringBuilder result = new StringBuilder();
    	for (SalesM_Items supplier : cacheList) {
            
            result.append(supplier.getId()).append(",")
                  .append(supplier.getName()).append(",")
                  .append(supplier.getStock()).append(",")
                  .append(supplier.getUnitPrice()).append("\n");  
        }
    	
    	String netString = result.toString();
    	
    	SalesM_Items note = new SalesM_Items(netString, itemSuppList);
    	note.SaveFunc();
    	
    	String alertText = note.getAlertText();
		showAlert(alertText);
		
    	clearTextField();
    	reloadClick();
    }
    
    @FXML
    public void reloadClick() throws IOException {
    	
    	clearTextField();
    	cacheList.clear();
    	load();
    }
    
    public void clearTextField() {
    	
    	TextField[] textFields = {txtItemsID, txtItemsName, txtItemsStock, txtItemsUP};
    	for (TextField field : textFields) {
    	    field.clear();      	
    	}
    	
    	supplierBox.getItems().clear();
    }
    
    private void checkStockLevels() {
    	
        final int LOW_STOCK_THRESHOLD = 300;
        final int HIGH_STOCK_THRESHOLD = 1000; 
        StringBuilder lowStockMsg = new StringBuilder();
        StringBuilder highStockMsg = new StringBuilder();
        boolean lowStockFound = false;
        boolean highStockFound = false;

        for (SalesM_Items item : cacheList) {
            if (item.getStock() < LOW_STOCK_THRESHOLD) {
                lowStockFound = true;
                lowStockMsg.append("- ").append(item.getName())
                            .append(" (Stock: ").append(item.getStock()).append(")\n");
            }
            if (item.getStock() > HIGH_STOCK_THRESHOLD) {
                highStockFound = true;
                highStockMsg.append("- ").append(item.getName())
                             .append(" (Stock: ").append(item.getStock()).append(")\n");
            }
        }

        if (lowStockFound) {
        	
        	showAlert("Some items are low in stock!");
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
    
    private void showAlert(String msg) {
    	
    	if (msg != null) {
		    Alert alert = new Alert(Alert.AlertType.INFORMATION);
		    alert.setTitle("Information");
		    alert.setHeaderText(null);
		    alert.setContentText(msg);
		    alert.showAndWait();
    	} else {
    		
    		return;
    	}
	}
    
    public Optional<String> getSuppId(){

    	TextInputDialog dialog = new TextInputDialog();
    	dialog.setTitle("Input Required");
    	dialog.setHeaderText("Enter the Supplier ID supplied by New Supplier");
    	dialog.setContentText("Supplier ID:");

    	Optional<String> result = dialog.showAndWait();
    	    
    	return result;    
    }
    
}
