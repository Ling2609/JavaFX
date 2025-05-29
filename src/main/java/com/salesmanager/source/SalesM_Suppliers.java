package com.salesmanager.source;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.io.BufferedWriter;
import java.io.FileWriter;

import com.groupfx.JavaFXApp.modifyData;
import com.groupfx.JavaFXApp.viewData;

import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextInputDialog;

public class SalesM_Suppliers implements viewData, modifyData{
	
	private String Id;
	private String Name;
	private String ContactNum;
	private String Address;
	
	//Variable for the modification
	private int selectedIndex;
	private String resultString;
	private ArrayList<String> resultItemSuppList;
	private ObservableList<SalesM_Suppliers> cacheList;
	
	private ArrayList<String> itemSuppList;
	
	public SalesM_Suppliers() {
		
	}
	
	public SalesM_Suppliers(String resultString, ArrayList<String> resultItemSuppList) {
		
		this.resultString = resultString;
		this.resultItemSuppList = resultItemSuppList;
	}
	
	public SalesM_Suppliers(int selectedIndex, ArrayList<String> itemSuppList, String id, ObservableList<SalesM_Suppliers> cacheList) {
		
		this.selectedIndex = selectedIndex;
		this.itemSuppList = itemSuppList;
		this.Id = id;
		this.cacheList = cacheList;
	}
	
	public SalesM_Suppliers(String id, String name, String contactNum, String address) {
        this.Id = id;
        this.Name = name;
        this.ContactNum = contactNum;
        this.Address = address;
    }

	
	public SalesM_Suppliers(String id, String name, String contactNum, String address, ArrayList<String> itemSuppList, ObservableList<SalesM_Suppliers> cacheList, int Index) {
        this.Id = id;
        this.Name = name;
        this.ContactNum = contactNum;
        this.Address = address;
        this.itemSuppList = itemSuppList;
        this.cacheList = cacheList;
        this.selectedIndex = Index;
    }
	
//	public SalesM_Suppliers(String id, String name, String contactNum, String address, String item, ObservableList<SalesM_Suppliers> cacheList, int Index) {
//        this.Id = id;
//        this.Name = name;
//        this.ContactNum = contactNum;
//        this.Address = address;
//        this.Item = item;
//        this.cacheList = cacheList;
//        this.selectedIndex = Index;
//    }
	
	public String getId() { return Id; }
    public String getName() { return Name; }
    public String getContactNum() { return ContactNum; }
    public String getAddress() { return Address; }
    
    @Override
	public StringBuilder ReadTextFile() throws IOException{
    	
        StringBuilder builder = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader("Data/Suppliers.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
            	
                if (line.trim().isBlank()) continue;

                String[] data = line.split(",");
                if (data.length < 4) continue; 
                
                builder.append(data[0]).append(",")  // Supplier ID
                       .append(data[1]).append(",")  // Supplier Name
                       .append(data[2]).append(",")  // Contact
                       .append(data[3]).append("\n");  // Address
            }
        }
        
        return builder;
	}
    
    private boolean containsID(ObservableList<SalesM_Suppliers> List, String id) {
        for (SalesM_Suppliers supplier : List) {
            if (supplier.getId().equals(id)) {
            	
                return true;
            }
        }
        return false;
    }
    
    public void insertCheck(SalesM_Suppliers selectedSupp) {
    	
	    	if(containsID(cacheList, Id) && selectedSupp != null) {
	
		    	EditFunc();
		    	
	    	} else if (!(containsID(cacheList, Id)) && selectedSupp == null){	
	    	
			    AddFunc();
			    
	    	} else {
	    		
	    		Alert alert = new Alert(AlertType.INFORMATION);
	    		alert.setTitle("Information");
	    		alert.setHeaderText(null);
	    		alert.setContentText("Please select the supplier if you want to edit\n OR \n If you want to add a supplier please dont repeat the ID");
	    		alert.showAndWait();
	    	}
    }
	
	@Override
	public void AddFunc() {
		
		cacheList.add(new SalesM_Suppliers(		
				
				Id,
				Name,
				ContactNum,
				Address
				
				));
		
		TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Input Required");
        dialog.setHeaderText("Enter the Item ID supplied by New Supplier");
        dialog.setContentText("Item ID:");

        // Show the message box let user key in the ItemID
        Optional<String> result = dialog.showAndWait();

        result.ifPresent(itemId -> {

            if (!itemId.trim().isEmpty()) {
            	itemSuppList.add(String.format("%s-%s", Id,itemId));

            } else {
            	Alert alert = new Alert(AlertType.INFORMATION);
	    		alert.setTitle("Information");
	    		alert.setHeaderText(null);
	    		alert.setContentText("Please Key In The Item ID");
	    		alert.showAndWait();
            }
        });
		
	}
	
	@Override
	public void EditFunc() {
		
		cacheList.set(selectedIndex, new SalesM_Suppliers(		
				
				Id,
				Name,
				ContactNum,
				Address
				
				));
		
	}
	
	@Override
	public void DeleteFunc() {
		
		cacheList.remove(selectedIndex);
		
		itemSuppList.removeIf(entry -> entry.split("-")[0].equals(Id));

	}
	
	@Override
	public void SaveFunc() {

	    // Save Suppliers
	    String[] parts = resultString.split("\n");
	    try (BufferedWriter writer = new BufferedWriter(new FileWriter("Data/Suppliers.txt", false))) {
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
	
	public ObservableList<SalesM_Suppliers>  getCacheList() {
		
		return cacheList;
	}
	
	public ArrayList<String>  getISList() {
		
		return itemSuppList;
	}
}
