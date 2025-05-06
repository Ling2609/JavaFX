package com.salesmanager.source;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;
import java.io.BufferedWriter;
import java.io.FileWriter;

import com.groupfx.JavaFXApp.modifyData;
import com.groupfx.JavaFXApp.viewData;

import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class SalesM_Suppliers implements viewData, modifyData{
	
	private String Id;
	private String Name;
	private String ContactNum;
	private String Address;
	private String Item;
	
	//Variable for the modification
	private int selectedIndex;
	private String resultString;
	private ObservableList<SalesM_Suppliers> cacheList;
	
	public SalesM_Suppliers() {
		
	}
	
	public SalesM_Suppliers(String resultString) {
		
		this.resultString = resultString;
	}
	
	public SalesM_Suppliers(int selectedIndex, ObservableList<SalesM_Suppliers> cacheList) {
		
		this.selectedIndex = selectedIndex;
		this.cacheList = cacheList;
	}
	
	public SalesM_Suppliers(String id, String name, String contactNum, String address) {
        this.Id = id;
        this.Name = name;
        this.ContactNum = contactNum;
        this.Address = address;
    }
	
	public SalesM_Suppliers(String id, String name, String contactNum, String address, String Item) {
        this.Id = id;
        this.Name = name;
        this.ContactNum = contactNum;
        this.Address = address;
        this.Item = Item;
    }
	
	public SalesM_Suppliers(String id, String name, String contactNum, String address, ObservableList<SalesM_Suppliers> cacheList, int Index) {
        this.Id = id;
        this.Name = name;
        this.ContactNum = contactNum;
        this.Address = address;
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
    public String getItem() { return Item; }
	
    public void setItem(String Item) { this.Item = Item; }
    @Override
	public StringBuilder ReadTextFile() throws IOException
	{	
		//InputStream stream= getClass().getClassLoader().getResourceAsStream("Data/ItemsList.txt");
		BufferedReader reader= new BufferedReader(new FileReader("Data/Suppliers.txt"));
		StringBuilder builder = new StringBuilder();
		String line;
		
		while ((line=reader.readLine())!=null) 
		{
			String[] data=line.split(",");
			builder.append(data[0]).append(","); 
			builder.append(data[1]).append(","); 
			builder.append(data[2]).append(","); 
			builder.append(data[3]).append(","); 
			builder.append(data[4]).append("\n"); 
			
		}
		return builder;
		
	}
    
    private boolean containsID(ObservableList<SalesM_Suppliers> List, String id, String itemId) {
        for (SalesM_Suppliers supplier : List) {
            if (supplier.getId().equals(id) || supplier.getItem().equals(itemId)) {
            	
                return true;
            }
        }
        return false;
    }
    
    public void insertCheck(SalesM_Suppliers selectedSupp) {
    	
	    	if(containsID(cacheList, Id, Item) && selectedSupp != null) {
	
		    	EditFunc();
		    	
	    	} else if (!(containsID(cacheList, Id, Item)) && selectedSupp == null){	
	    	
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
	}
	
	@Override
	public void SaveFunc() {
		
		String[] parts = resultString.split("\n");
		try (BufferedWriter writer = new BufferedWriter(new FileWriter("Data/Suppliers.txt", false))) {
			for (String part : parts) {
				
            writer.write(part);
            writer.newLine(); 
            
			}
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public ObservableList<SalesM_Suppliers>  getCacheList() {
		
		return cacheList;
	}
	
	
}
