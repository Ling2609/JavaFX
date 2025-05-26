package com.PM.Sources;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import com.groupfx.JavaFXApp.Purchase_Order;
import com.groupfx.JavaFXApp.ReportService;
import com.groupfx.JavaFXApp.modifyData;

public class Standard_PO extends Purchase_Order implements modifyData{
	
	private StringBuilder builder= new StringBuilder();
	public Standard_PO() {}
	
	public Standard_PO(String Id, String name, int Quantity, double Price, String Pm,String Status) 
	{
		super(Id,name,Quantity,Price,Pm,Status);
	}
	
	
	public Standard_PO(String Id, String name, int Quantity, double Price, String Pm,int LineNum ) 
	{
		super(Id,name,Quantity,Price,Pm,LineNum);
	}
	
	public Standard_PO(int LineNum) 
	{
		super(LineNum);
	}
	
	public Standard_PO(int LineNum,String newData) 
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
	
	
	@Override
	public void AddFunc() 
	{
		try(BufferedWriter writer= new BufferedWriter(new FileWriter("Data/Cache.txt",true)))
		{
			//String Data= MessageFormat.format("{0},{1}.{2},{3},{4}",Id,name,Quantity,Price,Pm);
			//writer.write(Data);
			String[] Status= RetriveItemsID(LineNum).toString().split(",");
			if( Status[4].equals("Pending")) //!CacheChecking() &&
			{
				builder.append(Id).append(",");
				builder.append(name).append(",");
				builder.append(Quantity).append(",");
				builder.append(Price).append(",");
				builder.append(Pm).append(",");
				builder.append("Pending").append(",");
				builder.append("Supplier").append(",");
				builder.append(PaymentStatus).append("\n");
				
				writer.write(builder.toString());
				//writer.newLine();
				Checking=true;
				ClickCount++;
			}
			else {Checking=false;}
			
		}
		catch(IOException e) 
		{	Checking=false;
			e.printStackTrace();
		}
	}
	

	
	
	
	@Override
	public void DeleteFunc() 
	{
	
			try(BufferedReader ReadSec= new BufferedReader(new FileReader("Data/Cache.txt")))
			{ 	String line;
				try 
				{
					while((line=ReadSec.readLine())!=null) 
					{
						String[] dataNew=line.split(",");
						builder.append(dataNew[0]).append(",");
						builder.append(dataNew[1]).append(",");
						builder.append(dataNew[2]).append(",");
						builder.append(dataNew[3]).append(",");
						builder.append(dataNew[4]).append(",");
						builder.append(dataNew[5]).append(",");
						builder.append(dataNew[6]).append(",");
						builder.append(dataNew[7]).append("\n");
						
					}
				} 
				catch (IOException e) 
				{
							
					e.printStackTrace();
				}
				
			 
			} 
			catch (FileNotFoundException e2) 
			{
			
				e2.printStackTrace();
			} 
			catch (IOException e2) 
			{
				
				e2.printStackTrace();
			}
			
			
			
			try (BufferedWriter writer = new BufferedWriter(new FileWriter("Data/Cache.txt"))) 
			{
				writer.write(builder.toString());
				
			} catch (IOException e1) {
			
				e1.printStackTrace();
			}
			
			try {
				String[] Status= POStatus(LineNum).toString().split(",");
				
				if(!ReportService.CacheChecking() && Status[5].equals("Pending")) 
				{
					List<String> lineR= new ArrayList<>(Files.readAllLines(Paths.get("Data/Cache.txt"))); //get file into Array
					if(LineNum>=0 &&LineNum<lineR.size()) //size one based start from 1 
					{
						lineR.remove(LineNum); //remove it
					}
					
					//Write into file WRITE is to write in TRUNCATEEXISTING is to clear all the data in file and write new 
					Files.write(Paths.get("Data/Cache.txt"), lineR, StandardOpenOption.WRITE,StandardOpenOption.TRUNCATE_EXISTING);
					Checking=true;
					ClickCount++;
				}else 
				{
					Checking=false;
				}
			} catch (IOException e) {
				Checking=false;
				e.printStackTrace();
			}
			
			
	}
		
	
	
	/**
	 * Perform Saving While User Click *
	 */
	
	@Override
	public void SaveFunc() 
	{
		try(BufferedReader reader= new BufferedReader(new FileReader("Data/Cache.txt")))
		{
			
				StringBuilder NewData= new StringBuilder();
				reader.mark(1000); //set the reader length
				boolean isEmptyOrNot=reader.readLine()==null;
				reader.reset(); //reset the reader to first row
				if(!isEmptyOrNot) 
				{
					try(BufferedWriter writer= new BufferedWriter(new FileWriter(Filepath)))
					{
							//String Data= MessageFormat.format("{0},{1}.{2},{3},{4}",Id,name,Quantity,Price,Pm);
							//writer.write(Data);
							String line;
							ReportService service= new ReportService();
							List<String> AutoId=Files.readAllLines(Paths.get(Filepath));
							int IncrementId= service.getLastIdNum(AutoId,"PO");
							
							while ((line=reader.readLine())!=null) 
							{	
								if(line.trim().isBlank()) {continue;}
								String[] dataSet= line.split(",");
								++IncrementId;
								String NewId=String.format("PO%03d", IncrementId);
								
								NewData.append(NewId).append(",");
								NewData.append(dataSet[1]).append(",");
								NewData.append(dataSet[2]).append(",");
								NewData.append(dataSet[3]).append(",");
								NewData.append(dataSet[4]).append(",");
								NewData.append(dataSet[5]).append(",");
								NewData.append(dataSet[6]).append(",");
								NewData.append(dataSet[7]).append("\n");
								
								lineCount++;
							}
							writer.write(NewData.toString());
							
							//writer.newLine();
							String[] PRID= RetriveItemsID(LineNum).toString().split(","); //get the target PRID
							if(LineNum!=-1 && PRID[4].equals(PStatus)) //for Change Status on PR
							{	
								
								
								try 
								{	
										List<String> AllPr= new ArrayList<>(Files.readAllLines(Paths.get("Data/prList.txt"))); //read an save file into List
										StringBuffer buffer= new StringBuffer();
										
										for(String lines:AllPr) 
										{
											String[] Part=lines.split(","); //split the data with ,//s* means space,empty and commas
											//System.out.println(Part.length);
											if(Part[0].equals(PRID[0])) 	//check the target id is correct or not
											{
												Part[5]="Approved";
												String newl= String.join(",", Part); //build the data with commas
												buffer.append(newl).append("\n");
											}
											else 
											{
												buffer.append(lines).append("\n"); //build the data with normal data(no changes)
											}
										}
										
										Files.write(Paths.get("Data/prList.txt"), buffer.toString().getBytes(), StandardOpenOption.WRITE,StandardOpenOption.TRUNCATE_EXISTING);
										//write in the file ,clear the existing data in the file and rewrite it, use NIO method need transfer to byte
									
								}
								
								
								
								catch(IOException e) 
								{	
									Checking=false;
									e.printStackTrace();
								}
									
									
							}
								
								
							
							}
							Checking=true;
							BufferedWriter Delete= new BufferedWriter(new FileWriter("Data/Cache.txt"));
							Delete.close();
							
					}else {Checking=false;}
						
				
				
			}
			catch(IOException e) 
			{
				e.printStackTrace();
			}
		}
	
	
			
		
		
	
	
	
	@Override
	public void EditFunc() 
	{	
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
				String[] Status= POStatus(LineNum).toString().split(",");
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
	
	
}
