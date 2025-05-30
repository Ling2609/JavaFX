package com.financemanager.source;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.groupfx.JavaFXApp.Purchase_Order;
import com.groupfx.JavaFXApp.ReportService;
import com.groupfx.JavaFXApp.viewData;

public class FMAppPO implements viewData{
	
	private String PoId;
	private String ItemsId;
	private int Qty;
	private double cost;
	private String PM;
	private String Status;
	private int ClickCount;
	private int SelectedLine;
	private String newdata;
	private String supplier;
	private boolean check=false;
	private String newStatus;
	
	
	public FMAppPO() {}
	
	public FMAppPO(int SelectedLine, String data,String supplier,String Status) 
	{	
		this.newdata=data;
		this.SelectedLine=SelectedLine;
		this.supplier=supplier;
		this.newStatus=Status;
	}
	
	public FMAppPO(String PoId, String ItemsId, int Qty, double cost, String PM,String Status) 
	{	
		this.PoId=PoId;
		this.ItemsId=ItemsId;
		this.Qty=Qty;
		this.cost=cost;
		this.PM=PM;
		this.Status=Status;
		
		
	}
	
	public String getPoId() 
	{
		return PoId;
	}
	
	public String getItemsId() 
	{
		return ItemsId;
	}
	
	public int getQty() 
	{
		return Qty;
	}
	
	public double getCost() 
	{
		return cost;
	}
	
	public String getPm() 
	{
		return PM;
	}
	
	public String getStatus() 
	{
		return Status;
	}
	
	
	
	public boolean checkingFunc() 
	{
		return check;
	}
	
	@Override
	public StringBuilder ReadTextFile() throws IOException
	{
		StringBuilder builder= new StringBuilder();
		
		BufferedReader reader= new BufferedReader(new FileReader("Data/PurchaseOrder.txt"));
		
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
	
	
	/**
	 * Get Supplier Code from Supplier Text File Based on Items Id 
	 * @param ItemsId
	 * @return ArrayList(String)-Return Only Supplier ID
	 * */
	
	
	public ArrayList<String> GetSupplier(String ItemsId) throws IOException
	{
		String line;
		ArrayList<String> RSupplier=new ArrayList<>();
		BufferedReader reader= new BufferedReader(new FileReader("Data/itemSupp.txt"));
		while((line=reader.readLine())!=null) 
		{
			String[] data= line.split("-");
			if(ItemsId.equals(data[1])) 
			{
				RSupplier.add(data[0]);
			}
			
		}
		reader.close();
		return RSupplier;
	}
	
	/**
	 * *
	 * GET PO ID and Status and Supplier from PurchaseOrder.txt (FM Function)
	 * @param selectedIndex
	 * @return StringBuffer
	 * 
	 */
	
	public StringBuffer GetPoId(int selectedIndex) throws IOException
	{
		String line;
		int LineCount=0;
		StringBuffer buffer= new StringBuffer();
		BufferedReader reader= new BufferedReader(new FileReader("Data/PurchaseOrder.txt"));
		while((line=reader.readLine())!=null) 
		{	
			String[] data=line.split(",");
			if(LineCount==selectedIndex) 
			{	buffer.append(data[0]).append(",");
				buffer.append(data[5]).append(","); //status
				buffer.append(data[6]).append("\n"); //Supplier
				reader.close();
				break;
			}
			LineCount++;
		}
		return buffer;
	}
	
	
	
	/**
	 * Change and Edit on PO(FM Function) *
	 */
	public void EditFunc() 
	{
		String line;
		StringBuffer buffer= new StringBuffer();
		
		if(ClickCount>0) {
		
		
			try(BufferedReader reader= new BufferedReader(new FileReader("Data/PurchaseOrder.txt")))
			{
				while((line=reader.readLine())!=null) 
				{
					String[] data=line.split(",");
					buffer.append(data[0]).append(",");
					buffer.append(data[1]).append(",");
					buffer.append(data[2]).append(",");
					buffer.append(data[3]).append(",");
					buffer.append(data[4]).append(",");
					buffer.append(data[5]).append(",");
					buffer.append(data[6]).append(",");
					buffer.append(data[7]).append("\n");
					
				}
				
				FileWriter writer= new FileWriter("Data/Cache.txt");
				writer.write(buffer.toString());
				writer.close();
				
			}
			catch(IOException e) 
			{
				e.printStackTrace();
			}	
		}
		
		try 
		{
			List<String> CacheData= new ArrayList<>(Files.readAllLines(Paths.get("Data/Cache.txt")));
			String[] index= GetPoId(SelectedLine).toString().split(",");
			StringBuffer EditBuff= new StringBuffer();
				
				if(SelectedLine>=0 || SelectedLine<=CacheData.size()) 
				{  	
					
					for(String P: CacheData) 
					{	String[] parts= P.toString().split(",");
					
						if(index[1].equals(parts[5])&& parts[5].equals("Pending") && index[0].equals(parts[0])) 
						{	
							parts[2]=newdata;
							parts[5]=newStatus;
							parts[6]=supplier;
							String NewData= String.join(",", parts);
							EditBuff.append(NewData).append("\n");
							check=true;
						}else 
						{
							EditBuff.append(P).append("\n");
							
						}
						
					}
					
					Files.write(Paths.get("Data/Cache.txt"), EditBuff.toString().getBytes(), StandardOpenOption.WRITE,StandardOpenOption.TRUNCATE_EXISTING);
					
					
				}else 
				{check=false;
				
				}
				
				
			}
			
		catch(IOException e) 
		{
			e.printStackTrace();
		}
		
		
	}
	
	
	public void SaveFunc() 
	{	String line;
		StringBuffer buffer= new StringBuffer();
			
		
			try(BufferedReader reader= new BufferedReader(new FileReader("Data/Cache.txt")))
				{	
					if(!ReportService.CacheChecking()) 
					{
						while((line=reader.readLine())!=null) 
						{
							String[] dataParts= line.split(",");
							buffer.append(dataParts[0]).append(",");
							buffer.append(dataParts[1]).append(",");
							buffer.append(dataParts[2]).append(",");
							buffer.append(dataParts[3]).append(",");
							buffer.append(dataParts[4]).append(",");
							buffer.append(dataParts[5]).append(",");
							buffer.append(dataParts[6]).append(",");
							buffer.append(dataParts[7]).append("\n");
						}
						
						BufferedWriter writer = new BufferedWriter(new FileWriter("Data/PurchaseOrder.txt"));
						writer.write(buffer.toString());
						writer.close();
						check=true;
						
						FileWriter ClearCache= new FileWriter("Data/Cache.txt");
						ClearCache.close();
					
					}else {check=false;}
				}catch(IOException e) 
				{
					check=false;
					e.printStackTrace();
				}
		
		
	}
}
