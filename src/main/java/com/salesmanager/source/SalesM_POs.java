package com.salesmanager.source;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import com.groupfx.JavaFXApp.modifyData;
import com.groupfx.JavaFXApp.viewData;

import javafx.collections.ObservableList;

public class SalesM_POs extends SalesM implements viewData{
	
	private String Id;
	private String itemId;
	private int quantity;
	private double cost;
	private String status;
	
	public SalesM_POs() {
		
	}
	
	
	public SalesM_POs(String id, String itemId, int quantity, double cost, String status) {
        this.Id = id;
        this.itemId = itemId;
        this.quantity = quantity;
        this.cost = cost;
        this.status = status;
    }
	
	public String getId() { return Id; }
    public String getName() { return itemId; }
    public int getContactNum() { return quantity; }
    public double getAddress() { return cost; }
    public String getItem() { return status; }
	
    @Override
	public StringBuilder ReadTextFile() throws IOException
	{	

		//InputStream stream= getClass().getClassLoader().getResourceAsStream("Data/ItemsList.txt");
		BufferedReader reader= new BufferedReader(new FileReader("Data/PurchaseOrder.txt"));
		builder= new StringBuilder();
		String line;
			
		while ((line=reader.readLine())!=null) 
		{
			if(line.trim().isBlank())continue;
			String[] data=line.split(",");
			builder.append(data[0]).append(","); 
			builder.append(data[1]).append(","); 
			builder.append(data[2]).append(","); 
			builder.append(data[3]).append(",");
			builder.append(data[4]).append(","); 
			builder.append(data[5]).append("\n"); 
			
		}

		return builder;
		
	}

}