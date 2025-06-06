package com.inventorymanager.source;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import com.groupfx.JavaFXApp.*;

import javafx.scene.control.cell.PropertyValueFactory;

public class InventoryM_Stocks extends InventoryM implements viewData{
	
	private String InventoryM_ID;
	
	private String posID;
	private String itemsID;
	private String posStatus;
	private int posQuantity;
	private double posPrice;
	
	private String itemStockID;
	private String itemStockName;
	private int itemStock;
	
	private String[] updateStockList;
	private String[] poStatusList;
	
	private String supplierId;
	
	public InventoryM_Stocks() {
		
	}
	
	public InventoryM_Stocks(String[] updateStockList, String[] poStatusList, String itemsID, int posQuantity, String posID) {
		
		this.itemsID = itemsID;
		this.posQuantity = posQuantity;
		this.poStatusList= poStatusList;
		this.updateStockList = updateStockList;
		this.posID = posID;
	}
	
	public InventoryM_Stocks(String[] updateStockList, String itemsID, int itemStock) {
		
		this.itemsID = itemsID;
		this.updateStockList = updateStockList;
		this.itemStock = itemStock;
	}

	public InventoryM_Stocks(String posID, String itemsID, int posQuantity, double posPrice,String posStatus) {
		
		this.posID = posID;
		this.itemsID = itemsID;
		this.posStatus = posStatus;
		this.posQuantity = posQuantity;
		this.posPrice = posPrice;
	}
	
	public InventoryM_Stocks(String itemStockID, String supplierId, String itemStockName, int itemStock) {
		
		InventoryM_ID = super.getUserID();
		this.itemStockID = itemStockID;
		this.supplierId = supplierId;
		this.itemStockName	= itemStockName;
		this.itemStock = itemStock;
		
	}
	
	public InventoryM_Stocks(String itemStockID, String itemStockName, int itemStock) {
		
		InventoryM_ID = super.getUserID();
		this.itemStockID = itemStockID;
		this.itemStockName	= itemStockName;
		this.itemStock = itemStock;
		
	}
	
	//For UserID
	public String getInventoryM_ID() {return InventoryM_ID; }
	
    //For Purchsae Order
	public String getPosID() { return posID; }
	
	public String getItemsID() { return itemsID; }
	
	public String getPosStatus() { return posStatus; }
	
	public String getSupplier() { return supplierId; }
	
	public int getPosQuantity() { return posQuantity; }
	
	public double getPosPrice() { return posPrice; }
	
	
	// For Stock
	public String getItemStockID() { return itemStockID; }
	
	public String getItemStockName() { return itemStockName; }
	
	public int getItemStock() { return itemStock; }
	
	@Override
	public StringBuilder ReadTextFile() throws IOException
	{	
		StringBuilder builder = new StringBuilder();
		
		try (BufferedReader reader = new BufferedReader(new FileReader("Data/PurchaseOrder.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isBlank()) continue;

                String[] data = line.split(",");
                if (data.length < 8) continue; 

    			builder.append(data[0]).append(","); 
    			builder.append(data[1]).append(","); 
    			builder.append(data[2]).append(","); 
    			builder.append(data[3]).append(",");
    			builder.append(data[4]).append(","); 
    			builder.append(data[5]).append(",");
    			builder.append(data[6]).append(",");
    			builder.append(data[7]).append("\n");
    			
            }
        }

        return builder;
		
	}
	
	public StringBuilder ReadStockTextFile() throws IOException
	{	
		StringBuilder builder = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader("Data/ItemsList.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isBlank()) continue;

                String[] data = line.split(",");
                if (data.length < 4) continue; 

    			builder.append(data[0]).append(","); 
    			builder.append(data[1]).append(","); 
    			builder.append(data[2]).append(","); 
    			builder.append(data[3]).append("\n");
    			
            }
        }

        return builder;
		
	}
	
	public ArrayList<String> ReadSuppTextFile() throws IOException
	{	
		ArrayList<String> itemSupp = new ArrayList<String>();

        try (BufferedReader reader = new BufferedReader(new FileReader("Data/itemSupp.txt"))) {
        	
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isBlank()) continue;

                itemSupp.add(line);
    		
            }
        }

        return itemSupp;
	}
	
	
	public boolean updateStockIdentifier() {
		
	    boolean updated = false;
	    
	    for (int i = 0; i < updateStockList.length; i++) {
	    	
	        String row = updateStockList[i];
	        String[] spl = row.split(",");
	        
	        if (spl.length == 4) {
	        	
	            if (spl[0].equals(itemsID)) {
	            	
	                spl[2] = String.valueOf(Integer.parseInt(spl[2]) + posQuantity);
	                
	                updateStockList[i] = String.join(",", spl);
	                updated = true;
	            }
	        }
	    }

	    for (int i = 0; i < poStatusList.length; i++) {
	    	
	        String row = poStatusList[i];
	        String[] spl = row.split(",");
	        
	        if (spl.length == 8 && updated == true) {
	        	
	            if (spl[0].equals(posID)) {
	            	
	                spl[7] = "Unpaid";
	                
	                poStatusList[i] = String.join(",", spl);
	            }
	        }
	    }
	    
	    POTxtFile(poStatusList);
	    updateStockTxtFile(updateStockList);
	    return updated;
	}
	
	public boolean manageStockIdentifier() {
		
		boolean updated = false;
	    
	    for (int i = 0; i < updateStockList.length; i++) {
	    	
	        String row = updateStockList[i];
	        String[] spl = row.split(",");
	        
	        if (spl.length == 4) {
	        	
	            if (spl[0].equals(itemsID)) {
	            	
	                spl[2] = String.valueOf(itemStock);
	                
	                updateStockList[i] = String.join(",", spl);
	                updated = true;
	            }
	        }
	    }
	    
	    updateStockTxtFile(updateStockList);
	    return updated;
	}
	public String[] getLowStockItems(int threshold) {
	    return Arrays.stream(updateStockList)
	            .map(row -> row.split(","))
	            .filter(data -> data.length == 4 && Integer.parseInt(data[2]) < threshold)
	            .map(data -> String.join(",", data))
	            .toArray(String[]::new);
	}
	private void updateStockTxtFile(String[] updateStockList) {
		
		try (BufferedWriter writer = new BufferedWriter(new FileWriter("Data/ItemsList.txt", false))) {
			
			for (String row : updateStockList) {
				
	        writer.write(row);
	        writer.newLine(); 
        
		}
	    } catch (IOException e) {
	    	
	        e.printStackTrace();
	    }
	}
	
	private void POTxtFile(String[] poStatusList) {
		
		try (BufferedWriter writer = new BufferedWriter(new FileWriter("Data/PurchaseOrder.txt", false))) {
			
			for (String row : poStatusList) {
				
	        writer.write(row);
	        writer.newLine(); 
        
		}
	    } catch (IOException e) {
	    	
	        e.printStackTrace();
	    }
	}
}
