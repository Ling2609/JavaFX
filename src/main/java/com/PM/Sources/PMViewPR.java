package com.PM.Sources;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import com.groupfx.JavaFXApp.Purchase_Req_View;

public class PMViewPR extends Purchase_Req_View{
	
	public PMViewPR() {}
	
	public PMViewPR(String Prid, String ItemsId, int Qty, String date, String SalesM ,String status) 
	{
		super(Prid,ItemsId,Qty,date,SalesM,status);
	}
	
	
	@Override
	public StringBuilder ReadTextFile() throws IOException
	{
			StringBuilder builder= new StringBuilder();
			String line;
			try(BufferedReader reader= new BufferedReader(new FileReader("Data/prList.txt")))
			{
				while((line=reader.readLine())!=null) 
				{
					String[] data= line.split(",");
					builder.append(data[0]).append(",");
					builder.append(data[1]).append(",");
					builder.append(data[2]).append(",");
					builder.append(data[3]).append(",");
					builder.append(data[4]).append(",");
					builder.append(data[5]).append("\n");
				}
			}
			return builder;
		
	}
	
}
