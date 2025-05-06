package com.PM.Sources;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.groupfx.JavaFXApp.Purchase_Order;

public class PMGenPO extends Purchase_Order{
	
	public PMGenPO() {}
	
	
	public PMGenPO(String Id, String name, int Quantity, double Price, String Pm,String Status) 
	{
		super(Id,name,Quantity,Price,Pm,Status);
	}
	
	public PMGenPO(String Id, String name, int Quantity, double Price, String Pm,String Status,String Supplier) 
	{
		super(Id,name,Quantity,Price,Pm,Status,Supplier);
	}
	
	public PMGenPO(String Id, String name, int Quantity, double Price, String Pm,int LineNum ) 
	{
		super(Id,name,Quantity,Price,Pm,LineNum);
	}
	
	public PMGenPO(int LineNum) 
	{
		super(LineNum);
	}
	
	public PMGenPO(int LineNum,String newData) 
	{
		super(LineNum,newData);
	}
	
	
	@Override
	public StringBuilder ReadTextFile() throws IOException
	{
		StringBuilder builder= new StringBuilder();
		
		BufferedReader reader= new BufferedReader(new FileReader(Filepath));
		
		String line;
		while ((line=reader.readLine())!=null) 
		{
			if(line.trim().isEmpty()) continue; //Skip Empty Space/Data
			
			String[] data=line.split(",");
			builder.append(data[0]).append(",");
			builder.append(data[1]).append(",");
			builder.append(data[2]).append(",");
			builder.append(data[3]).append(",");
			builder.append(data[4]).append(",");
			builder.append(data[5]).append(",");
			builder.append(data[6]).append(",");
			builder.append(data[7]).append("\n");
		}
		
		
		try (BufferedWriter ReadCache = new BufferedWriter(new FileWriter("Data/Cache.txt"))) {
			ReadCache.write(builder.toString());
		}
		reader.close();
		return builder;
		
	}
	
	public String[] ReadSupplierAdd(String ItemsId) throws IOException
	{
		BufferedReader reader= new BufferedReader(new FileReader("Data/Suppliers.txt"));
		String line;
		String[] d= {};
		while((line=reader.readLine())!=null) 
		{
			String[] data= line.split(",");
			String[] ItemsData= data[4].split("-");
			for(String id:ItemsData) 
			{
				if(ItemsId.equals(id)) 
				{
					String Address= data[3].replace("-", ",");
					String[] retData= {data[1],Address};
					reader.close();
					return retData;
				}
			}
				
			
		}
		reader.close();
		return d;
		
	}
	
	public double UnitPriceR(String ItemsId) throws IOException
	{
		BufferedReader reader= new BufferedReader(new FileReader("Data/ItemsList.txt"));
		String line;
		double price=0.00;
		while((line=reader.readLine())!=null) 
		{
			String[] data= line.split(",");
			for(String id:data) 
			{
				if(ItemsId.equals(id)) 
				{
					price=Double.parseDouble(data[4]);
					reader.close();
					return price;
				}
			}
				
			
		}
		reader.close();
		return price;
		
	}
	
	
}
