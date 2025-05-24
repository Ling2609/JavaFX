package com.financemanager.source;

import java.util.List;
import java.util.Map;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.groupfx.JavaFXApp.Purchase_Order;
import com.groupfx.JavaFXApp.ReportService;
import com.groupfx.JavaFXApp.viewData;





public class FMPayment implements viewData{
	
	private String Status;
	private String Total;
	private String Id,name,Supplier;
	private double unitPrice;
	private int Qty;
	private int ClickCount=0; 
	private List<String> oldCache=new ArrayList<>();
	private StringBuilder builder= new StringBuilder();
	private boolean Checking=false;
	
	public FMPayment() {}
	//PO006,I0005,1111111,10.9,Ming,Approve,S001

	public FMPayment(String Total,List<String> PayDetails) 
	{
		this.Total=Total;
		this.oldCache=PayDetails;
	}
	
	public FMPayment(String Id, String name,int Qty, double unitprice,String Supplier,String Status) 
	{
		this.Id = Id;
	    this.name = name;
	    this.Qty = Qty;
	    this.unitPrice=unitprice;
	    this.Supplier = Supplier;
		this.Status=Status;
	}
	
    public String getId() {
        return Id;
    }

    public String getName() {
        return name;
    }

    public int getQty() {
        return Qty;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public String getSupplier() {
        return Supplier;
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
			builder.append(data[2]).append(",");//qty
			builder.append(data[3]).append(",");
			builder.append(data[5]).append(",");// Status
			builder.append(data[6]).append(","); //Supplier
			builder.append(data[7]).append("\n"); //Payment Status
			
		
		}
	
		
		
		try (BufferedWriter ReadCache = new BufferedWriter(new FileWriter("Data/Cache.txt"))) {
			ReadCache.write(builder.toString());
		}
		reader.close();
		return builder;
		
	}
	
	/**
	 * Check the Array is Blank Or Not
	 *@return isBlank-true otherwise false
	 * */
	
	public boolean isArrayBlank(String[] arr) 
	{
		for(String a:arr) 
		{
			if(a!=null && !a.trim().isEmpty()) 
			{
				return false;
			}
		}
		return true;
	}
	
	
	public String getStatus() 
	{
		return Status;
	}
	
	public boolean checkingFunc() 
	{
		return Checking;
	}
	
	
	public List<String> SetData() throws IOException 
	{	
		List<String> Data=new ArrayList<>();
	
		String[] CacheData= LoadData().toString().split(",");
		for(String parts:CacheData) 
		{
			Data.add(parts);
		}
		return Data;
	}
	
	
	
	public StringBuffer LoadData() throws IOException
	{	
	
		StringBuffer buffer= new StringBuffer();
		String[] ReadData= ReadTextFile().toString().split("\n");
		for(String line:ReadData)
		{	String[] data= line.split(",");
	
			
			if(data[4].equals("Approve") && data[6].equals("Unpaid")) 
			{
				buffer.append(data[0]).append(",");
				buffer.append(data[1]).append(",");
				buffer.append(data[2]).append(",");
				buffer.append(data[3]).append(",");
				buffer.append(data[4]).append(",");// Status
				buffer.append(data[5]).append(","); //Supplier
				buffer.append(data[6]).append("\n"); //Payment Status
				
				
			}
		}
		
		return buffer;
	}
	
	public int LineCount(String Id) throws IOException
	{	String line;
		int LineCount=0;
		BufferedReader reader= new BufferedReader(new FileReader("Data/PurchaseOrder.txt"));
		while((line=reader.readLine())!=null) 
		{
			String[] data= line.split(",");
			if(data[0].equals(Id)) 
			{	
				reader.close();
				return LineCount;
			}
			LineCount++;
		}
		reader.close();
		return LineCount;
	}
	
	public void Approve(String Id) throws IOException 
	{	
		String PStatus="Approve";
		int LineNum=LineCount(Id);
		String[] NewDatas;
		try {
			NewDatas = Purchase_Order.POStatus(LineNum).toString().split(",");
			NewDatas[7]="Paid";
			String newData=String.join(",", NewDatas);
			BufferedWriter ClearCache= new BufferedWriter(new FileWriter("Data/Cache.txt"));
			ClickCount=1;
			ClearCache.close();
			builder.setLength(0);
			String line;
			if(ClickCount>0) 
			{
			
				try(BufferedReader readOri= new BufferedReader(new FileReader("Data/PurchaseOrder.txt"))) //initialize first
				{
					
					while((line=readOri.readLine())!=null) 
					{
						String[] oldData= line.split(",");
						builder.append(oldData[0]).append(",");
						builder.append(oldData[1]).append(",");
						builder.append(oldData[2]).append(",");
						builder.append(oldData[3]).append(",");
						builder.append(oldData[4]).append(",");
						builder.append(oldData[5]).append(",");
						builder.append(oldData[6]).append(",");
						builder.append(oldData[7]).append("\n");
					}
				 }
				catch(IOException e) 
				{
					e.printStackTrace();
					Checking=false;
				}
			 }
			
				
				try(BufferedReader reader= new BufferedReader(new FileReader("Data/Cache.txt")))
				{
					
						while((line=reader.readLine())!=null) 
						{	
							if(line.trim().isEmpty())continue;
							
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
						
						try(FileWriter writer= new FileWriter("Data/Cache.txt"))
						{
							writer.write(builder.toString());
						}
				
				}
				catch(IOException e) 
				{
					e.printStackTrace();
				}
				
				try
				{ 	
					String[] Status= Purchase_Order.POStatus(LineNum).toString().split(",");
					if(!ReportService.CacheChecking() && Status[5].equals(PStatus)) 
					{
						List<String> EditList= new ArrayList<>(Files.readAllLines(Paths.get("Data/Cache.txt")));
						if(LineNum>=0 && LineNum<=EditList.size()) 
						{
							EditList.set(LineNum,newData);
							
						}
						Files.write(Paths.get("Data/Cache.txt"),EditList,StandardOpenOption.WRITE,StandardOpenOption.TRUNCATE_EXISTING);
						Checking=true;
						
					}
					else 
					{
						Checking=false;
						
					}
				}
				catch(IOException e) 
				{
					e.printStackTrace();
					Checking=false;
					
				}
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
	}

	
	
	public String ReturnDiscountedPrice(String Source) throws IOException
	{
		BufferedReader reader= new BufferedReader(new FileReader("Data/PurchaseOrder.txt"));
		String line;
		
		String price=null;
		while((line=reader.readLine())!=null) 
		{
			String[] data= line.split(",");
			if(data[0].equals(Source)) 
			{
				price= data[3];
				reader.close();
				return price;
			}
			else continue;
			
		}
		reader.close();
		return price;
	}
	

	public void SaveFunc() 
	{
		String CacheLine;
		
		
		StringBuffer buffer= new StringBuffer();
		try {
			if(!ReportService.CacheChecking()) 
			{
				
				BufferedReader readCache= new BufferedReader(new FileReader("Data/Cache.txt"));
				while((CacheLine=readCache.readLine())!=null) 
				{	String[] Data=CacheLine.split(",");
					if(Data.length==8) 
					{
				
						buffer.append(Data[0]).append(",");
						buffer.append(Data[1]).append(",");
						buffer.append(Data[2]).append(",");
						buffer.append(Data[3]).append(",");
						buffer.append(Data[4]).append(",");
						buffer.append(Data[5]).append(",");
						buffer.append(Data[6]).append(",");
						buffer.append(Data[7]).append("\n");
						
					}else continue;
				}
				Files.write(Paths.get("Data/PurchaseOrder.txt"), buffer.toString().getBytes(), StandardOpenOption.WRITE,StandardOpenOption.TRUNCATE_EXISTING);
				readCache.close();
				BufferedWriter ClearCache= new BufferedWriter(new FileWriter("Data/Cache.txt"));
				ClearCache.close();
				buffer.setLength(0);
				
				
				BufferedWriter writePayment= new BufferedWriter(new FileWriter("Data/Payment.txt",true));
						
						String[] CacheData= oldCache.toArray(new String[0]);
					    List<String> getId= Files.readAllLines(Paths.get("Data/Payment.txt"));
					    ReportService service= new ReportService();
					    int LastId=service.getLastIdNum(getId,"PY");
					    
						LastId++;
						String NewId= String.format("PY%03d", LastId); //Start with %, Use 0 to fill length is 3 and decimal based
					    DateTimeFormatter Dformat= DateTimeFormatter.ofPattern("dd-MM-yyyy");
						LocalDate Date= LocalDate.now(); 
						
						String Price=ReturnDiscountedPrice(CacheData[0]);
						
						
							buffer.append(NewId).append(",");
							buffer.append(CacheData[0]).append(",");
							buffer.append(CacheData[1]).append(",");
							buffer.append(CacheData[2]).append(",");
							buffer.append(CacheData[3]).append(",");
							buffer.append(CacheData[4]).append(",");// Status
							buffer.append(CacheData[5]).append(",");
							buffer.append(Price).append(",");
							buffer.append("Paid").append(",");
							buffer.append(Date.format(Dformat)).append("\n");
						
				
				writePayment.write(buffer.toString());
				
				oldCache.clear();
				
				BufferedWriter ClearCache2= new BufferedWriter(new FileWriter("Data/Cache.txt"));
				ClearCache2.close();
				writePayment.close();
				Checking=true;
	
			}else Checking=false;
		}
		
		catch(IOException e) 
		{
			e.printStackTrace();
			Checking=false;
		}
	}
	
	
	public List<String> RetriveItemUnitPrice() throws IOException
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
				ItemsList.put(data[0], data[4]);
				
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
	
//	@Deprecated
//	public int getLastIdNum(List<String> lines) 
//	{
//		if(lines.isEmpty()) return 0;
//		
//		String lastLine=lines.get(lines.size()-1);//get last line (Total-1) cuz start in 0
//		String[] parts=lastLine.split(",");
//		try 
//		{
//			if(parts[0].startsWith("PY")) 
//			{
//				return Integer.parseInt(parts[0].substring(2));
//			}
//		} catch (NumberFormatException e) 
//		{
//			e.printStackTrace();
//			return 0;
//		}
//		
//		return 0;
//	}
//	
}
