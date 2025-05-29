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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
    	
    	Platform.runLater(() -> {
    		checkStockLevels();
    	});
    	
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
    	
    	try {
    		
    	SalesM_Items dataModify = new SalesM_Items(
    			txtItemsID.getText().trim(),
				txtItemsName.getText().trim(),
				Integer.parseInt(txtItemsStock.getText().trim()),
				Double.parseDouble(txtItemsUP.getText().trim()),
				itemSuppList,
				cacheList, 
				selectedSuppIndex
				);
    	
    	dataModify.insertCheck(
    		
				selectedSupp
				
				);
    	
    	ObservableList<SalesM_Items>  tempList = dataModify.getCacheList();
    	cacheList = tempList;
    	
    	ArrayList<String> tempISList = dataModify.getISList();
    	itemSuppList = tempISList;
    	
    	viewItemTable.setItems(cacheList);
    	clearTextField();
    	
    	} catch (Exception e) {
    		
    		Alert alert = new Alert(AlertType.INFORMATION);
    		alert.setContentText("Okay this guy tried to remove something that doesnt exist");
    		alert.showAndWait();
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
    		ObservableList<SalesM_Items>  tempList = dataModify.getCacheList();
    		cacheList = tempList;
    		
    		ArrayList<String> tempISList = dataModify.getISList();
        	itemSuppList = tempISList;
        	
    		viewItemTable.setItems(cacheList);
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
    	for (SalesM_Items supplier : cacheList) {
            
            result.append(supplier.getId()).append(",")
                  .append(supplier.getName()).append(",")
                  .append(supplier.getStock()).append(",")
                  .append(supplier.getUnitPrice()).append("\n");  
        }
    	
    	String netString = result.toString();
    	
    	SalesM_Items note = new SalesM_Items(netString, itemSuppList);
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
    	
    	TextField[] textFields = {txtItemsID, txtItemsName, txtItemsStock, txtItemsUP};
    	for (TextField field : textFields) {
    	    field.clear();      	
    	}
    	
    	txtItemsID.setEditable(true);
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
