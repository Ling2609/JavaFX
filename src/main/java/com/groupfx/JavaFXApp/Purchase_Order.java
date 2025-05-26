package com.groupfx.JavaFXApp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public abstract class Purchase_Order implements viewData {
	protected String Id,name,Pm;
	protected int Quantity;
	protected double Price;
	protected String Filepath="Data/PurchaseOrder.txt";
	protected boolean Checking=false; 
	private StringBuilder builder= new StringBuilder();
	protected int lineCount; 
	protected int ClickCount; //--//
	private String Status;
	
	public Purchase_Order() {}
	
	
	public Purchase_Order(String Id, String name, int Quantity, double Price, String Pm ) 
	{
		this.Id=Id;
		this.name=name;
		this.Quantity=Quantity;
		this.Price=Price;
		this.Pm=Pm;
	}
	public Purchase_Order(String Id, String name, int Quantity, double Price, String Pm,String status ) 
	{
		this.Id=Id;
		this.name=name;
		this.Quantity=Quantity;
		this.Price=Price;
		this.Pm=Pm;
		this.Status=status;
	}
	
	
	public String getName() 
	{
		return name;
	}
	
	public String getId() 
	{
		return Id;
	}
	
	public int getQuantity() 
	{
		return Quantity;
	}
	
	public double getPrice() 
	{
		return Price;
	}
	
	public String getPm() 
	{
		return Pm;
	}
	
	public int LineCount() 
	{
		return lineCount;
	}
	
	public String getStatus() 
	{
		return Status;
	}
	
	
			
	public StringBuilder PieCData() throws IOException
	{	String line;
		try (BufferedReader reader= new BufferedReader(new FileReader("Data/prList.txt")))
		{
			while((line=reader.readLine())!=null) 
			{
				String[] Prdata= line.split(",");
				builder.append(Prdata[5]).append("\n"); //PRStatus
				
				}
				
			}
			
		return builder;
	}
	
	public StringBuffer POStatus() throws IOException
	{
		String line;
		StringBuffer buffer= new StringBuffer();
		try (BufferedReader reader= new BufferedReader(new FileReader("Data/PurchaseOrder.txt")))
		{
			while((line=reader.readLine())!=null) 
			{
				String[] PoStatus= line.split(",");
				buffer.append(PoStatus[5]).append("\n"); //POStatus
			}
			
		}
		return buffer ;
		
	}
	
	/**
	 * Read All Data From PurchaseOrder.txt based on the Selected Numbers/Line Numbers
	 * @return StringBuffer
	 * <ul>
	 * <li>0 ID</li>
	 * <li>1 Items</li>
	 * <li>2 Qty</li>
	 * <li>3 Price</li>
	 * <li>4 Purchase Manager</li>
	 * <li>5 Status</li>
	 * <li>6 Supplier</li>
	 * <li>7 Payment Status</li>
	 * </ul>
	 * Overloading from POStatus() 
	 * */
	
	
	//Overloading method
	public static StringBuffer POStatus(int SelectedNum ) throws IOException 
	{
		String line;
		int LineCount=0;
		StringBuffer buffer= new StringBuffer();
		try (BufferedReader reader= new BufferedReader(new FileReader("Data/PurchaseOrder.txt")))
		{
			while((line=reader.readLine())!=null) 
			{
				String[] PoStatus= line.split(",");
				if(LineCount==SelectedNum) 
				{	
					buffer.append(PoStatus[0]).append(",");
					buffer.append(PoStatus[1]).append(",");
					buffer.append(PoStatus[2]).append(",");
					buffer.append(PoStatus[3]).append(",");
					buffer.append(PoStatus[4]).append(",");
					buffer.append(PoStatus[5]).append(","); //Status
					buffer.append(PoStatus[6]).append(",");
					buffer.append(PoStatus[7]).append("\n");
					break;
				}
				LineCount++;
			}
			
		}
		return buffer ;
	}
	
	/**
	 * Get Purchase Requisition ID from prList.txt
	 * @return StringBuilder--PRID
	 * */
	public StringBuilder RetrivePR() throws IOException
	{	String line;
	
		try (BufferedReader reader= new BufferedReader(new FileReader("Data/prList.txt")))
		{
			while((line=reader.readLine())!=null) 
			{
				String[] Prdata= line.split(",");
				builder.append(Prdata[0]).append("\n"); //PRID
			}
			
		}
		return builder;
	}
	
	
	/**
	 * Get Items Code/PrID/Qty/Status/Date from PRList TXT 
	 * 
	 * @return 4 data in String Builder---0 PRID  1 ItemsCode 2 Quantity 3 Sales Manager 4 Status(Approve/Pending)
	 * 
	 */
	public StringBuffer RetriveItemsID(int SelectionNum) throws IOException
	{	String line;
	
		int lineNum=0;
		StringBuffer builders= new StringBuffer();
		try (BufferedReader reader= new BufferedReader(new FileReader("Data/prList.txt")))
		{
			while((line=reader.readLine())!=null) 
			{
				String[] Prdata= line.split(",");
				if(lineNum==SelectionNum) 
				{
					builders.append(Prdata[0]).append(","); 
					builders.append(Prdata[1]).append(","); //items code
					builders.append(Prdata[2]).append(","); //qty
					builders.append(Prdata[3]).append(",");
					builders.append(Prdata[5]); //Status
					break;
				}
				lineNum++;
			}
			
		}
		return builders;
	}
	
	/**
	 * Get Items ID from TXT <br>
	 * <strong>Return Items ID AND Items Name Only</strong>
	 */
	public StringBuilder RetriveItems(String PRCode, String ItemsCode) throws IOException
	{	String line;
		StringBuilder Newbuild= new StringBuilder();
		try(BufferedReader reader= new BufferedReader(new FileReader("Data/ItemsList.txt")))
		{
			while((line=reader.readLine())!=null) 
			{
				String[] ItemsName= line.split(",");
				if(ItemsCode.equals(ItemsName[0])) 
				{
//					
					Newbuild.append(ItemsName[0]).append(","); //name
					Newbuild.append(ItemsName[1]).append("\n");
					break;
				}
				else 
				{
					continue;
				}
			}
		}
		return Newbuild;
	}
	
	
	
	
	public boolean checkingFunc() 
	{
		return Checking;
	}
	

	
	/**
	 * Read all Text File While Needs, abstract methods
	 * @return StringBuilder 
	 * @exception IOException
	 * */
	
	@Override
	public abstract StringBuilder ReadTextFile() throws IOException;
	
}
