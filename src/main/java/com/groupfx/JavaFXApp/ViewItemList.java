package com.groupfx.JavaFXApp;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;



public abstract class ViewItemList implements viewData
{
	private String Name;
	private String ID;
	private double UnitPrice;
	private String Supplier;
	private int Stock;
	protected StringBuilder builder;
	
	public ViewItemList() 
	{
		
	}
	
	public ViewItemList(String ID, String Name,int Stock,double UnitPrice ) 
	{
	    this.ID = ID;
        this.Name = Name;
        this.Stock = Stock;
        this.UnitPrice = UnitPrice;
	}
	
	
	public ViewItemList(String ID, String Name, String Supplier,int Stock,double UnitPrice ) 
	{
	    this.ID = ID;
        this.Name = Name;
        this.Supplier = Supplier;
        this.Stock = Stock;
        this.UnitPrice = UnitPrice;
	}
	
	
    public String getId() 
    { 
    	return ID; 
    }
    
    public String getName() 
    { 
    	return Name; 
    }
    
    public String getSupplier() 
    { 
    	return Supplier; 
    }
    
    public int getStock() 
    { 
    	return Stock; 
    }
    
    public double getUnitPrice() 
    { 
    	return UnitPrice; 
    }
	
    @Override
    public abstract StringBuilder ReadTextFile() throws IOException;
}
