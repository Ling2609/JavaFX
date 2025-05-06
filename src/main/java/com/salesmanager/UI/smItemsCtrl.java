package com.salesmanager.UI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.salesmanager.source.*;

public class smItemsCtrl {
	@FXML
    private TableColumn<SalesM_Items, String> ItemsID;

    @FXML
    private TableColumn<SalesM_Items, String> ItemsName;

    @FXML
    private TableColumn<SalesM_Items, String> ItemsSupp;

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
    private TextField txtItemSupp;
    
    @FXML
    private BarChart<?,?> viewStockChart;
    
    private ObservableList<SalesM_Items> cacheList = FXCollections.observableArrayList(); 
    
    private ObservableList<SalesM_Suppliers> suppItemList = FXCollections.observableArrayList();
    
    private HashMap<String, Integer> chartStore = new HashMap<>();
    
    public void initialize() throws IOException 
    {
    	ItemsID.setCellValueFactory(new PropertyValueFactory<>("id")); // Use the ViewItemList.getID method
    	ItemsName.setCellValueFactory(new PropertyValueFactory<>("name"));// same as above but the method different
        ItemsSupp.setCellValueFactory(new PropertyValueFactory<>("supplier"));
        itemsStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        itemsUP.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        
        load();
    }
    
    public void load() throws IOException 
    {
    	SalesM_Items listed= new SalesM_Items();
    	ObservableList<SalesM_Items> itemList= FXCollections.observableArrayList(); 
    	String[] row= listed.ReadTextFile().toString().split("\n");
    	
    	for(String rows: row) 
    	{
    		String[] spl= rows.split(",");
    		if(spl.length==5) 
    		{
    			itemList.add(new SalesM_Items(
    					spl[0],
    					spl[1],
    					spl[2],
    					Integer.parseInt(spl[3]),
    					Double.parseDouble(spl[4])*1.1 // 10% profit for every item
    					));
    			
    			chartStore.put(spl[1], Integer.parseInt(spl[3]));
    		}
    	}
    	
    	smSuppsCtrl obj = new smSuppsCtrl();
    	obj.load();
    	suppItemList = obj.getSuppItemList();
    	
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
	        
	        if (selectedItem != null) {
	            String id = selectedItem.getId();
	            String name = selectedItem.getName();
	            String supplier = selectedItem.getSupplier();
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
	            txtItemSupp.setText(supplier);
	            txtItemsStock.setText(String.valueOf(stock));
	            txtItemsUP.setText(String.valueOf(unitPrice));
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
    		
    	SalesM_Items dataModify = new SalesM_Items(txtItemsID.getText().trim(),
				txtItemsName.getText().trim(),
				txtItemSupp.getText().trim(),
				Integer.parseInt(txtItemsStock.getText().trim()),
				Double.parseDouble(txtItemsUP.getText().trim()),
				suppItemList,
				cacheList, selectedSuppIndex
				);
    	
    	dataModify.insertCheck(
    		
				selectedSupp
				
				);
    	
    	ObservableList<SalesM_Items>  tempList = dataModify.getCacheList();
    	suppItemList = dataModify.getSuppItemList();
    	
    	cacheList = tempList;
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
				txtItemSupp.getText().trim(),
				suppItemList,
				cacheList, 
				selectedSuppIndex
				);
    	
    	try {
    		
    		dataModify.DeleteFunc();
    		ObservableList<SalesM_Items>  tempList = dataModify.getCacheList();
    		suppItemList = dataModify.getSuppItemList();
    		
    		cacheList = tempList;
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
                  .append(supplier.getSupplier()).append(",")
                  .append(supplier.getStock()).append(",")
                  .append(supplier.getUnitPrice()).append("\n");  
        }
    	
    	StringBuilder suppItemResult = new StringBuilder();
    	for (SalesM_Suppliers supplier : suppItemList) {
            
            suppItemResult.append(supplier.getId()).append(",")
                  		  .append(supplier.getName()).append(",")
                  		  .append(supplier.getContactNum()).append(",")
                          .append(supplier.getAddress()).append(",")
                          .append(supplier.getItem()).append("\n");  
        }
    	
    	String netString = result.toString();
    	String netSuppItem = suppItemResult.toString();
    	
    	SalesM_Items note = new SalesM_Items(netString, netSuppItem);
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
    	
    	TextField[] textFields = {txtItemsID, txtItemsName, txtItemsStock, txtItemsUP, txtItemSupp};
    	for (TextField field : textFields) {
    	    field.clear();      	
    	}
    }
}
