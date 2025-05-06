package com.inventorymanager.source;

import java.io.BufferedReader;
import java.io.FileReader;

public class InventoryM {
	
	private String userID;
	
	protected StringBuilder builder;
	
	public InventoryM() {
		
		
	}
	
	public String getUserID() {
		
		userID = ReadUserIDTxt();
		
		return userID;
	}
	
	private String ReadUserIDTxt() {
		
		String ID = null;
		
		try (BufferedReader reader = new BufferedReader(new FileReader("Data/Log.txt"))) {
			
			String content = reader.readLine();
		    String[] arr = content.split(",");
		    
		    ID = arr[0];
			
		} catch (Exception e) {
			
			System.out.println(e);
		}
		
		return ID;
	}
}
