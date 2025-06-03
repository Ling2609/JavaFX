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
import com.salesmanager.UI.smItemsCtrl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TextInputDialog;

public class SalesM_Items implements viewData, modifyData{
	
	private String Name;
	private String ID;
	private double UnitPrice;

	private int Stock;
	protected StringBuilder builder;
	private ObservableList<SalesM_Items> cacheList = FXCollections.observableArrayList();
	private int index;
	private String resultString;
	
	Optional<String> suppIdResult;
	
	private String alertText = null;
	
	private ArrayList<String> itemSuppList = new ArrayList<String>();
	
	private ArrayList<String> resultItemSuppList;
	
	public SalesM_Items() 
	{
		
	}
	
	public SalesM_Items(Optional<String> suppIdResult) {
		
		this.suppIdResult = suppIdResult;
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
    
    public String getAlertText() { return alertText; }
    
	@Override
	public StringBuilder ReadTextFile() throws IOException {
		
		
		StringBuilder builder = new StringBuilder();
		
		try (BufferedReader reader = new BufferedReader(new FileReader("Data/ItemsList.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isBlank()) continue;

                String[] data = line.split(",");
                if (data.length < 4) continue; 

                builder.append(data[0]).append(","); // ID
    	        builder.append(data[1]).append(","); // Name
    	        builder.append(data[2]).append(","); // Stock
    	        builder.append(data[3]).append("\n"); // UnitPrice
            }
		
		return builder;
		}
	}
	
	@Override
	public void AddFunc() {
	    
		int newestNum = 0;
		
		for (SalesM_Items item : cacheList) { 
			
			String[] spl = item.getId().toString().split("I");
			int itemNum = Integer.parseInt(spl[1]);
			if(itemNum > newestNum ) {
				
				newestNum = itemNum;
			}
		}
		
		int currentNum = newestNum + 1;
		
		String currentNumStr = String.valueOf("I00" + currentNum);
	    
		smItemsCtrl suppIdR = new smItemsCtrl();
		Optional<String> result = suppIdR.getSuppId();

	    if (result.isPresent()) {
	        String suppId = result.get().trim();

	        if (suppId.isEmpty()) {
	        	
	        	alertText =  "Please key in the Supplier ID.";
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
	        	
	        	alertText = "Supplier does not exist. Please add the supplier first.";
	        	return;
	        	
	        } else {
	        	
	        	cacheList.add(new SalesM_Items(currentNumStr, Name, Stock, UnitPrice));
	        	itemSuppList.add(String.format("%s-%s", suppId, currentNumStr));
	        }
	    }
	    
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
		
		StringBuilder prBuilder = new StringBuilder();
		try (BufferedReader reader = new BufferedReader(new FileReader("Data/prList.txt"))) {
		    	
		        String line;
		        while ((line = reader.readLine()) != null) {
		            if (line.trim().isBlank()) continue;

		            String[] data = line.split(",");
		            if (data.length < 6) continue;

		            prBuilder.append(data[0]).append(",")  // ID
		                   .append(data[1]).append(",")  // ItemId
		                   .append(data[2]).append(",")  // Quantity
		                   .append(data[3]).append(",")  // Date
		                   .append(data[4]).append(",")  // Author
		                   .append(data[5]).append("\n");// Status
		        }
		} catch (Exception e) {
		    	
		    	alertText = String.format("Error: %s", e.toString());
		}
		
		String[] row= prBuilder.toString().split("\n");
    	
    	for(String rows: row) 
    	{
    		String[] spl= rows.split(",");
    		if(spl.length==6) 
    		{
    			if(spl[1].equals(ID)) {
    				
    				alertText = "The Item exist in a Purchase Requisition \n Please remove the PR first";
    				return;
    				
    			}
    		}
    	}
    	
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
        	
        	alertText = String.format("%s", e);
        }
		
		// save Item suppliers soft entity
	    try (BufferedWriter writer = new BufferedWriter(new FileWriter("Data/itemSupp.txt", false))) {
	        for (String itemSupp : resultItemSuppList) {
	            writer.write(itemSupp);
	            writer.newLine();
	        }
	    } catch (IOException e) {
	    	
	    	alertText = String.format("%s", e);
	    }
	}
	
	public void insertCheck(SalesM_Items selectedSupp) {
		
		if(containsID(cacheList, ID, Name) && selectedSupp != null) {
    		
	    	EditFunc();
	    	
    	} else if (!(containsID(cacheList, ID, Name)) && selectedSupp == null){	
    		
		    AddFunc();
		    
    	} else {
    		
    		alertText = "The item name has been added";

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
