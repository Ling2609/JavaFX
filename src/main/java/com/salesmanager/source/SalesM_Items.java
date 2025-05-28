package com.salesmanager.source;	

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.groupfx.JavaFXApp.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;

public class SalesM_Items implements viewData, modifyData{
	
	private String Name;
	private String ID;
	private double UnitPrice;

	private int Stock;
	protected StringBuilder builder;
	private ObservableList<SalesM_Items> cacheList = FXCollections.observableArrayList();
	private int index;
	private String resultString;
	private String suppItemString;
	
	private ArrayList<String> itemSuppList = new ArrayList<String>();
	
	private ArrayList<String> resultItemSuppList;
	
	public SalesM_Items() 
	{
		
	}
	
	public SalesM_Items(String resultString, ArrayList<String> resultItemSuppList) 
	{
		
		this.resultString = resultString;
		this.resultItemSuppList = resultItemSuppList;
	}
	
	public SalesM_Items(int index, ObservableList<SalesM_Items> cacheList) 
	{
		
		this.index = index;
		this.cacheList = cacheList;
	}
	
	public SalesM_Items(String ID, String Name,int Stock,double UnitPrice) 
	{
		
	    this.ID = ID;
        this.Name = Name;
        this.Stock = Stock;
        this.UnitPrice = UnitPrice;
	}
	
	public SalesM_Items(String ID, ArrayList<String> itemSuppList, ObservableList<SalesM_Items> cacheList, int index) 
	{
		
	    this.ID = ID;
        this.itemSuppList = itemSuppList;
        this.cacheList = cacheList;
        this.index = index;
	}
	
	public SalesM_Items(String ID, String Name,int Stock, double UnitPrice, ArrayList<String> itemSuppList, ObservableList<SalesM_Items> cacheList, int index) 
	{
		
	    this.ID = ID;
        this.Name = Name;
        this.Stock = Stock;
        this.UnitPrice = UnitPrice;
        this.itemSuppList = itemSuppList;
        this.cacheList = cacheList;
        this.index = index;
	}
	
	
    public String getId() { return ID; }
    
    public String getName() { return Name; }
    
    public int getStock() { return Stock; }
    
    public double getUnitPrice() { return UnitPrice; }
    
	@Override
	public StringBuilder ReadTextFile() throws IOException {
		
		//InputStream stream= getClass().getClassLoader().getResourceAsStream("Data/ItemsList.txt");
		BufferedReader reader= new BufferedReader(new FileReader("Data/ItemsList.txt"));
		builder= new StringBuilder();
		String line;
		
		while ((line=reader.readLine())!=null) {
			if (line.trim().isBlank()) continue;
			
			String[] data = line.split(",");

	        if (data.length < 4) {
	            System.out.println("Pass the wrong format line: " + line);
	            continue;
	        }

	        builder.append(data[0]).append(","); // ID
	        builder.append(data[1]).append(","); // Name
	        builder.append(data[2]).append(","); // Stock
	        builder.append(data[3]).append("\n"); // UnitPrice
		}
		
		reader.close();
		return builder;
	}
	
	@Override
	public void AddFunc() {
	    
	    cacheList.add(new SalesM_Items(ID, Name, Stock, UnitPrice));
	    
	    TextInputDialog dialog = new TextInputDialog();
	    dialog.setTitle("Input Required");
	    dialog.setHeaderText("Enter the Supplier ID supplied by New Supplier");
	    dialog.setContentText("Supplier ID:");

	    Optional<String> result = dialog.showAndWait();

	    if (result.isPresent()) {
	        String suppId = result.get().trim();

	        if (suppId.isEmpty()) {
	            showAlert("Please key in the Supplier ID.");
	            return;
	        }

	        boolean supplierExists = false;
	        for (String itemSupp : itemSuppList) {
	            String[] row = itemSupp.split("-");
	            if (row.length > 0 && row[0].equals(suppId)) {
	                supplierExists = true;
	                break;
	            }
	        }

	        if (!supplierExists) {
	        	showAlert("Supplier does not exist. Please add the supplier first.");
	        } else {
	        	itemSuppList.add(String.format("%s-%s", suppId, ID));
	        }
	    }
	    
// 		Lambda Expression Readability is not that good!!!
//	    result.ifPresent(suppId -> {
//	        if (suppId.trim().isEmpty()) {
//	            showAlert("Please key in the Supplier ID.");
//	            return;
//	        }
//
//	        boolean supplierExists = false;
//	        for (String itemSupp : itemSuppList) {
//	            String[] row = itemSupp.split("-");
//	            if (row.length > 0 && row[0].equals(suppId)) {
//	                supplierExists = true;
//	                break;
//	            }
//	        }
//
//	        if (supplierExists) {
//	            itemSuppList.add(String.format("%s-%s", suppId, ID));
//	        } else {
//	            showAlert("Supplier does not exist. Please add the supplier first.");
//	        }
//	    });
	    
	}

	private void showAlert(String msg) {
	    Alert alert = new Alert(Alert.AlertType.INFORMATION);
	    alert.setTitle("Information");
	    alert.setHeaderText(null);
	    alert.setContentText(msg);
	    alert.showAndWait();
	}
	
	@Override
	public void EditFunc() {
		
		cacheList.set(index, new SalesM_Items(		
				
				ID,
				Name,
				Stock,
				UnitPrice
				
				));
	}
	
	@Override
	public void DeleteFunc() {
		
		cacheList.remove(index);
		
		itemSuppList.removeIf(entry -> entry.split("-")[1].equals(ID));
	}
	
	@Override
	public void SaveFunc() {
		
		String[] parts = resultString.split("\n");
		try (BufferedWriter writer = new BufferedWriter(new FileWriter("Data/ItemsList.txt", false))) {
			for (String part : parts) {
				
            writer.write(part);
            writer.newLine(); 
            
			}
        } catch (IOException e) {
            e.printStackTrace();
        }
		
		// save Item suppliers soft entity
	    try (BufferedWriter writer = new BufferedWriter(new FileWriter("Data/itemSupp.txt", false))) {
	        for (String itemSupp : resultItemSuppList) {
	            writer.write(itemSupp);
	            writer.newLine();
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	public void insertCheck(SalesM_Items selectedSupp) {
		
		if(containsID(cacheList, ID, Name) && selectedSupp != null) {
    		
	    	EditFunc();
	    	
    	} else if (!(containsID(cacheList, ID, Name)) && selectedSupp == null){	
    		
		    AddFunc();
		    
    	} else {
    		
    		showAlert("The item name has been added");
    	}
		
	} 
	
	
	private boolean containsID(ObservableList<SalesM_Items> List, String id, String Name) {
        
		for (SalesM_Items item : List) {
            if (item.getId().equals(id)) {
            	
                return true;
            } else if (item.getName().equals(Name)) {
            	
            	return true;
            } 
        }
        return false;
    }
	
	public ObservableList<SalesM_Items>  getCacheList() {
			
		return cacheList;
	}
	
	public ArrayList<String>  getISList() {
		
		return itemSuppList;
	}
	
}
