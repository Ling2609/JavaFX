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
	public StringBuilder ReadTextFile() throws IOException{
    	
        StringBuilder builder = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader("Data/PurchaseOrder.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isBlank()) continue;

                String[] data = line.split(",");
                if (data.length < 6) continue; 

                builder.append(data[0]).append(",")  // Order ID
                       .append(data[1]).append(",")  // Supplier Name
                       .append(data[2]).append(",")  // Product ID
                       .append(data[3]).append(",")  // Quantity
                       .append(data[4]).append(",")  // Unit Price
                       .append(data[5]).append("\n");// Purchase Date or Manager
            }
        }

        return builder;
		
	}

}