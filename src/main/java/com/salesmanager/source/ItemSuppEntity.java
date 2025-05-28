package com.salesmanager.source;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ItemSuppEntity {
	
	
	
	public ItemSuppEntity() {
		
		
	}
	
	public ArrayList<String> ReadTextFile() throws IOException {
		
		ArrayList<String> itemSuppList = new ArrayList<>();
		
		try (BufferedReader reader= new BufferedReader(new FileReader("Data/itemSupp.txt"))) {
		//InputStream stream= getClass().getClassLoader().getResourceAsStream("Data/ItemsList.txt");

			String line;
			
			while ((line=reader.readLine())!=null) {
				
				if (line.trim().isBlank()) continue;
				
		        itemSuppList.add(line); // SupplierID and ItemID
			}
			return itemSuppList;
		}
	}
	
	

}

