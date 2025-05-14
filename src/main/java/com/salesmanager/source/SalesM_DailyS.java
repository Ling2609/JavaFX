package com.salesmanager.source;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.groupfx.JavaFXApp.modifyData;
import com.groupfx.JavaFXApp.viewData;

import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class SalesM_DailyS extends SalesM implements viewData, modifyData {
	
	private String Id;
	private String itemId;
	private String date;
	private int totalSales;
	private final String author = this.getUserId();
	private String tempAuthor;
	
	//Variable for the modification
	private int selectedIndex;
	private String resultString;
	private ObservableList<SalesM_DailyS> cachelist;
	private int oriSales;
	
	public SalesM_DailyS() {
		
	}
	
	public SalesM_DailyS(String resultString) {
		
		this.resultString = resultString;
	}
	
	public SalesM_DailyS(String itemId, int totalSales) {
		
		this.itemId = itemId;
        this.totalSales = totalSales;
	}

	public SalesM_DailyS(String itemId, int totalSales, int oriSales) {
		
		this.itemId = itemId;
        this.totalSales = totalSales;
        this.oriSales = oriSales;
	}

	public SalesM_DailyS(int selectedIndex, ObservableList<SalesM_DailyS> cacheList) {
		
		this.selectedIndex = selectedIndex;
		this.cachelist = cacheList;
	}
	
	public SalesM_DailyS(String Id, String itemId, String date, int totalSales, String author) {
        this.Id = Id;
        this.itemId = itemId;
        this.date = date;
        this.totalSales = totalSales;
        this.tempAuthor = author;
    }
	
	public SalesM_DailyS(String Id, String itemId, String date, int totalSales, String author, ObservableList<SalesM_DailyS> cacheList, int Index) {
		this.Id = Id;
        this.itemId = itemId;
        this.date = date;
        this.totalSales = totalSales;
        this.tempAuthor = author;
        this.cachelist = cacheList;
        this.selectedIndex = Index;
    }
	
	public SalesM_DailyS(String Id, String itemId, String date, int totalSales, String author, ObservableList<SalesM_DailyS> cacheList, int Index, int oriSales) {
		this.Id = Id;
        this.itemId = itemId;
        this.date = date;
        this.totalSales = totalSales;
        this.tempAuthor = author;
        this.cachelist = cacheList;
        this.selectedIndex = Index;
        this.oriSales = oriSales;
    }
	
	public String getId() { return Id; }
    public String getItemId() { return itemId; }
    public String getDate() { return date; }
    public int getTotalSales() { return totalSales; }
    public String getAuthor() { return tempAuthor; }
	
    @Override
	public StringBuilder ReadTextFile() throws IOException
	{	
		//InputStream stream= getClass().getClassLoader().getResourceAsStream("Data/ItemsList.txt");
		BufferedReader reader= new BufferedReader(new FileReader("Data/dailySales.txt"));
		builder= new StringBuilder();
		String line;
		
		while ((line=reader.readLine())!=null) 
		{
			String[] data=line.split(",");
			builder.append(data[0]).append(","); 
			builder.append(data[1]).append(","); 
			builder.append(data[2]).append(","); 
			builder.append(data[3]).append(","); 
			builder.append(data[4]).append("\n"); 
			
		}
		return builder;
		
	}
	
	private boolean containsID(ObservableList<SalesM_DailyS> List, String id, String itemId, String date) {
		
	    for (SalesM_DailyS item : List) {
	        if (item.getId().equals(id)) {
	        	
	            return true;
	        } else if (item.getDate().equals(date) && item.getItemId().equals(itemId)) {
	        	
	        	return true;
	        }
	    }
	    return false;
	}
	
	public boolean insertCheck(SalesM_DailyS selectedDS) {
    	
    	try {
	    	if(containsID(cachelist, Id, itemId, date) && selectedDS != null) {
	
		    	EditFunc();
		    	return true;
		    	
	    	} else if (!(containsID(cachelist,  Id, itemId, date)) && selectedDS == null){	
	    		
			    AddFunc();
			    return true;
			    	
	    	} else {
	    		
	    		return false;
	    	}
    	} catch (Exception e) {
    		
    		return false;
    	}
    }

	@Override
	public void AddFunc() {
		
		cachelist.add(new SalesM_DailyS(		
				
				Id,
				itemId,
				date,
				totalSales,
				author
				
				));
		
		ModifyDS(new SalesM_DailyS(		
				
				itemId,
				totalSales
				
				), "add");
	}
	
	@Override
	public void EditFunc() {
		
		cachelist.set(selectedIndex, new SalesM_DailyS(		
				
				Id,
				itemId,
				date,
				totalSales,
				author
				
				));
		
		ModifyDS(new SalesM_DailyS(		
				
				itemId,
				totalSales

				), "edit");
	}
	
	@Override
	public void DeleteFunc() {
		
		cachelist.remove(selectedIndex);
	}
	
	@Override
	public void SaveFunc() {
		
		String[] parts = resultString.split("\n");
		try (BufferedWriter writer = new BufferedWriter(new FileWriter("Data/dailySales.txt", false))) {
			for (String part : parts) {
				
            writer.write(part);
            writer.newLine(); 
            
			}
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public ObservableList<SalesM_DailyS>  getCacheList() {
		
		return cachelist;
	}
	
	public void ModifyDS(SalesM_DailyS obj, String type) {
		
		List<String> updatedLines = new ArrayList<>();
		
		switch(type) {
		
			case "add":
				try (BufferedReader reader = new BufferedReader(new FileReader("Data/ItemsList.txt"))) {
					
		            String line;

		            while ((line = reader.readLine()) != null) {
		                String[] spl = line.split(",");
		                if (spl.length == 5) {

		                    if (spl[0].equals(obj.getItemId())) {

		                        int totalSales = Integer.parseInt(spl[3]);
		                        totalSales -= obj.getTotalSales();
		                        spl[3] = String.valueOf(totalSales);
		                    }

		                    updatedLines.add(String.join(",", spl));
		                }
		            }

		            try (BufferedWriter writer = new BufferedWriter(new FileWriter("Data/ItemsList.txt"))) {
		                for (String updatedLine : updatedLines) {
		                    writer.write(updatedLine);
		                    writer.newLine();
		                }
		            }

		        } catch (IOException e) {
		            e.printStackTrace();
		        }
		        break;
		    
			case "edit":
				try (BufferedReader reader = new BufferedReader(new FileReader("Data/ItemsList.txt"))) {
					
		            String line;

		            while ((line = reader.readLine()) != null) {
		                String[] spl = line.split(",");
		                if (spl.length == 5) {

		                    if (spl[0].equals(obj.getItemId())) {

		                        int totalSales = Integer.parseInt(spl[3]);
		                        if(oriSales <= obj.getTotalSales()) {
		                        	int change = obj.getTotalSales() - oriSales;
		                        	totalSales -= change;
		                        	spl[3] = String.valueOf(totalSales);
		                        } else {
		                        	int change = oriSales - obj.getTotalSales();
		                        	totalSales -= change;
		                        	spl[3] = String.valueOf(totalSales);
		                        }   
		                    }

		                    updatedLines.add(String.join(",", spl));
		                }
		            }

		            try (BufferedWriter writer = new BufferedWriter(new FileWriter("Data/ItemsList.txt"))) {
		                for (String updatedLine : updatedLines) {
		                    writer.write(updatedLine);
		                    writer.newLine();
		                }
		            }
		            System.out.println("Updated");
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
		        break;
		}
	}
}
