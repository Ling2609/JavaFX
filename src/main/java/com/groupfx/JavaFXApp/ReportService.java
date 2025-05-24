package com.groupfx.JavaFXApp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class ReportService {

	public ReportService() {}
	
	
	
	/**
	 * Get the last id from the Txt File and Return back
	 * @exception NumberFormatException
	 * */
	public int getLastIdNum(List<String> lines, String Prefix) 
	{
		if(lines.isEmpty()) return 0;
		
		String lastLine=lines.get(lines.size()-1);//get last line (Total-1) cuz start in 0
		String[] parts=lastLine.split(",");
		try 
		{
			if(parts[0].startsWith(Prefix)) 
			{
				return Integer.parseInt(parts[0].substring(2));
			}
		} catch (NumberFormatException e) 
		{
			e.printStackTrace();
			return 0;
		}
		
		return 0;
	}
	
	/**
	 * *
	 * Checking Cache is Empty or Not
	 * (NULL Return True, else return false)
	 * @return Boolean
	 * @throws IOException
	 */
	public static boolean CacheChecking() throws IOException
	{
		BufferedReader reader= new BufferedReader(new FileReader("Data/Cache.txt"));
		boolean check;
		if(check=reader.readLine()==null) 
		{	
			reader.close();
			return true;
		}
		reader.close();
		return false;
		
	}
}
