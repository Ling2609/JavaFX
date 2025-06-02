package com.Admin.Source;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import com.groupfx.JavaFXApp.AD_Data;
import com.groupfx.JavaFXApp.modifyData;
import com.groupfx.JavaFXApp.viewData;

public class AdminSources implements viewData,AD_Data {

	private String User,password,role;
	private boolean checking=false;
	private int SelectedIndex;
	
	public AdminSources() {}
	
	
	public AdminSources(String User, String password, String role) 
	{
		this.User=User;
		this.password=password;
		this.role=role;
	}
	
	public AdminSources(int SelectedIndex,String User) 
	{
		this.SelectedIndex=SelectedIndex;
		this.User=User;
	}
	
	public String getUser() { return User;}
	public String getPassword() {return password;}
	public String getRole() {return role;}
	
	
	public String RetName() throws IOException
	{	
		
		String line;
		String name=null;
		BufferedReader reader= new BufferedReader(new FileReader("Data/Log.txt"));
		
		while((line=reader.readLine())!=null) 
		{	
			if(line.trim().isBlank()) continue;
			String[] data= line.split(",");
			name=data[0];
			
		}
		reader.close();
		return name;
	}
	
	@Override
	public StringBuilder ReadTextFile() throws IOException
	{
		String line;
		StringBuilder builder= new StringBuilder();
		BufferedReader reader= new BufferedReader(new FileReader("Data/UserData.txt"));
		
		while ((line=reader.readLine())!=null) 
		{
			if (line.trim().isBlank()) continue;

			String[] data= line.split(",");
			builder.append(data[0]).append(",");
			builder.append(data[1]).append(",");
			builder.append(data[2]).append("\n");
		}
		reader.close();
		
		return builder;
	}
	
	
	public boolean Checking() 
	{
		return checking;
	}
	
	
	@Override
	public void AddFunc() 
	{
		try(BufferedWriter writer= new BufferedWriter(new FileWriter("Data/UserData.txt",true)))
		{
			writer.newLine();
			writer.write(User+","+password+","+role);
			checking=true;
			
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
			checking=false;
		}
	}
		
	@Override
	public void DeleteFunc() 
	{
		
		try {
			
			List<String> allData= new ArrayList<>(Files.readAllLines(Paths.get("Data/UserData.txt")));
			
			for(int i=0;i<=allData.size()-1;i++) 
			{	
				
				if(i==SelectedIndex) 
				{
					allData.remove(SelectedIndex);
					break;
				}
				
				
			}
			
			Files.write(Paths.get("Data/UserData.txt"),allData,StandardOpenOption.WRITE,StandardOpenOption.TRUNCATE_EXISTING );		
			checking=true;
		} 
		
		catch (IOException e) {
			checking=false;
			e.printStackTrace();
		}
	}
	
	/**
	 * Return true while username repeated
	 * */
	public boolean CheckSameUsername(String username) throws IOException
	{
		String line;
		BufferedReader reader = new BufferedReader(new FileReader("Data/UserData.txt"));
		while((line=reader.readLine())!=null) 
		{
			String[] data= line.split(",");
			if(data[0].equals(username)) 
			{
				reader.close();			
				return true;
			}
		}
		reader.close();
		return false;
	}
	
	
	
	/**
	 * Return false while username same as current user
	 * */
	public boolean ReadLog() throws IOException
	{
		String line;
		BufferedReader reader = new BufferedReader(new FileReader("Data/Log.txt"));
		while((line=reader.readLine())!=null) 
		{
			String[] data= line.split(",");
			if(data[0].equals(User)) 
			{	
				reader.close();
				return true;
			}
		}
		reader.close();
		return false;
	}
	
	
}
