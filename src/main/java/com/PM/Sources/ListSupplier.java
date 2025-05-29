package com.PM.Sources;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.groupfx.JavaFXApp.viewData;

public class ListSupplier implements viewData  {
	private String Name;
	private String ID;
	private String PhNo;
	private String Address;
	
	public ListSupplier() {}
	
	public ListSupplier(String ID, String Name, String PhNo, String Address) 
	{
		this.Name=Name;
		this.ID=ID;
		this.PhNo=PhNo;
		this.Address=Address;
	}
	
	public String getName() 
	{
		return Name;
	}
	
	public String getID() 
	{
		return ID;
	}
	
	public String getPhNo()
	{
		return PhNo;
	}
	
	public String getAddress() 
	{
		return Address;
	}
	
	
	
	@Override
	public StringBuilder ReadTextFile() throws IOException
	{
		StringBuilder builder= new StringBuilder();

		BufferedReader reader= new BufferedReader(new FileReader("Data/Suppliers.txt"));
		String line;
		while ((line=reader.readLine())!=null) 
		{
			String[] data= line.split(",");
			builder.append(data[0]).append(",");
			builder.append(data[1]).append(",");
			builder.append(data[2]).append(",");
			builder.append(data[3]).append("\n");
			
		}
		

		
		
		
		reader.close();
		return builder;
		
	}
	
}
