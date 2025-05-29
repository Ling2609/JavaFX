package com.PM.Sources;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.groupfx.JavaFXApp.ViewItemList;



public class PMViewItems extends ViewItemList
{
//	private String Name;
//	private String ID;
//	private double UnitPrice;
//	private int Stock;
	private StringBuilder builder;
	
	public PMViewItems() 
	{
		
	}
	
	public PMViewItems(String ID, String Name,int Stock,double UnitPrice ) 
	{	
		super(ID,Name,Stock,UnitPrice);	    
	}
	
	
	
    @Override
    public StringBuilder ReadTextFile() throws IOException
	{	
		//InputStream stream= getClass().getClassLoader().getResourceAsStream("Data/ItemsList.txt");
		BufferedReader reader= new BufferedReader(new FileReader("Data/ItemsList.txt"));
		builder= new StringBuilder();
		String line;
		
		while ((line=reader.readLine())!=null) 
		{	
			if(line.trim().isBlank())continue;
			String[] data=line.split(",");
			builder.append(data[0]).append(","); //ID
			builder.append(data[1]).append(","); //Name
			builder.append(data[2]).append(","); //Stock
			builder.append(data[3]).append("\n"); //UnitPrice
			
		}
		reader.close();
		return builder;
		
	}
}
