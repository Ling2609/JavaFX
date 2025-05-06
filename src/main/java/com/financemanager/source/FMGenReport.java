package com.financemanager.source;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.groupfx.JavaFXApp.viewData;

public class FMGenReport  implements viewData{
//7
	
	private String ID,ItemId,ItemName,Supplier,date,PayId;
	private int Qty;
	private double Total;
	
	public FMGenReport() {}
	//PY001,PO001,I0003,1501,111.0,Approve,S003,166611.00,Paid,13-04-2025
	public FMGenReport(String PayId,String ID,String ItemId,String ItemName,String Supplier,int Qty,double Total,String date) 
	{	
		this.PayId=PayId;
		this.ID=ID;
		this.ItemId=ItemId;
		this.ItemName=ItemName;
		this.Supplier=Supplier;
		this.date=date;
		this.Qty=Qty;
		this.Total=Total;
	
	}
	
	public String getPayId() {
	    return PayId;
	}

	public String getID() {
	    return ID;
	}

	public String getItemId() {
	    return ItemId;
	}

	public String getItemName() {
	    return ItemName;
	}

	public String getSupplier() {
	    return Supplier;
	}

	public String getDate() {
	    return date;
	}

	public int getQty() {
	    return Qty;
	}

	public double getTotal() {
	    return Total;
	}


	
	@Override
	public StringBuilder ReadTextFile() throws IOException
	{	
		String line;
		StringBuilder builder= new StringBuilder();
		BufferedReader reader= new BufferedReader(new FileReader("Data/Payment.txt"));
		while((line=reader.readLine())!=null) 
		{	String [] data= line.split(",");
			//PY001,PO001,I0003,1501,111.0,Approve,S003,166611.00,Paid,13-04-2025
			builder.append(data[0]).append(",");
			builder.append(data[1]).append(",");
			builder.append(data[2]).append(",");//Items ID
			builder.append(data[3]).append(",");
			builder.append(data[4]).append(",");
			builder.append(data[5]).append(",");
			builder.append(data[6]).append(",");
			builder.append(data[7]).append(",");
			builder.append(data[8]).append(",");
			builder.append(data[9]).append("\n"); //date
		}
		reader.close();
		return builder;
		
	}
	
	/**
	 * Get Item Name and UnitPrice By Using ArrayList
	 * Read from ItemsList and ReadTextFile()
	 * 
	 * */
	
	
	public List<String> RetriveItemName() throws IOException
	{	
		List<String> ItemName= new ArrayList<>();
		String line;
		Map<String,String> ItemsList=new HashMap<>(); //ID, Name
		
		try(BufferedReader reader= new BufferedReader(new FileReader("Data/ItemsList.txt")))
		{	
			// Store the Items Details (Name and Id) into hash map
			
			while((line=reader.readLine())!=null) 
			{
				String[] data= line.split(",");
				ItemsList.put(data[0], data[1]);
				
			}
		}
		
		//Verify Items Id match with Payment text file
		String[] paymentData=ReadTextFile().toString().split(",");
		for(String parts:paymentData) 
		{
			if(parts.startsWith("I") && ItemsList.containsKey(parts)) 
			{
				ItemName.add(ItemsList.get(parts));
				
			}
		}
		
		return ItemName;
	}
	
	public List<String> RetriveYear() throws IOException
	{
		String line;
		List<String> YearList= new ArrayList<>();
		DateTimeFormatter date= DateTimeFormatter.ofPattern("dd-MM-yyyy");
		BufferedReader reader= new BufferedReader(new FileReader("Data/Payment.txt"));
		while((line=reader.readLine())!=null) 
		{
			String[] data= line.split(",");
			try 
			{
				LocalDate D= LocalDate.parse(data[9],date);
				int Year= D.getYear();
				String ListYear=Integer.toString(Year);
				if(YearList.contains(ListYear)) continue;
				YearList.add(ListYear);
				
			} catch(Exception e) 
			{
				e.printStackTrace();
			}
		}
		reader.close();
		return YearList;
		
	}
	
	public StringBuffer YearBasedData(String year) throws IOException
	{	
		StringBuffer buffer= new StringBuffer();
		String[] data= ReadTextFile().toString().split("\n");
		for(String line:data) 
		{
			String[] splitData= line.split(",");
			String[] yeardate=splitData[9].split("-");
			if(year.equals(yeardate[2])) 
			{
				buffer.append(splitData[0]).append(",");
				buffer.append(splitData[1]).append(",");
				buffer.append(splitData[2]).append(",");//Items ID
				buffer.append(splitData[3]).append(",");
				buffer.append(splitData[4]).append(",");
				buffer.append(splitData[5]).append(",");
				buffer.append(splitData[6]).append(",");
				buffer.append(splitData[7]).append(",");
				buffer.append(splitData[8]).append(",");
				buffer.append(splitData[9]).append("\n"); //date
			}
		}
		return buffer;
	}
	
}
