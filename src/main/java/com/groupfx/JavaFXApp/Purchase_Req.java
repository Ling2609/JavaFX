package com.groupfx.JavaFXApp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public abstract class Purchase_Req  implements viewData{
	private String Prid;
	private String ItemsId;
	private int Quantity;
	private String date;
	private String SalesM;
	private String status;
	private String suppId;
	
	public Purchase_Req() {}
	
	public Purchase_Req(String Prid, String ItemsId, int Qty, String date, String SalesM ,String status) 
	{
		this.Prid=Prid;
		this.ItemsId=ItemsId;
		this.Quantity=Qty;
		this.date=date;
		this.SalesM=SalesM;
		this.status=status;
	}
	
	
	public String getPrId() 
	{
		return Prid;
	}
	
	public String getItemsid() 
	{
		return ItemsId;
	}
	
	public int getQty() 
	{
		return Quantity;
	}
	
	public String getDate() 
	{
		return date;
	}
	
	public String getSalesM() 
	{
		return SalesM;
	}
	
	
	public String getStatus() 
	{
		return status;
	}
	
	@Override
	public abstract StringBuilder ReadTextFile() throws IOException;
	
	
	
}
