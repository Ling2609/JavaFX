package com.salesmanager.UI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.HashMap;
import java.util.Map;

import com.salesmanager.source.*;

public class smDailySCtrl {

	@FXML
    private TableColumn<SalesM_DailyS, String> DSID;

    @FXML
    private TableColumn<SalesM_DailyS, String> itemID;

    @FXML
    private TableColumn<SalesM_DailyS, String> date;

    @FXML
    private TableColumn<SalesM_DailyS, Integer> totalSales;

    @FXML
    private TableColumn<SalesM_DailyS, String> author;

    @FXML
    private TableView<SalesM_DailyS> viewSalesTable;
    
    @FXML
    private ChoiceBox<String> sortChoice;
    
    @FXML
    private TextField txtDSID;
    
    @FXML
    private TextField txtitemID;
    
    @FXML
    private TextField txtDate;
    
    @FXML
    private TextField txttotalSales;
    
    @FXML
    private TextField txtAuthor;
    
    @FXML
    private LineChart<String,Integer> viewSalesChart;
    
    ObservableList<SalesM_DailyS> cacheList = FXCollections.observableArrayList(); 
    	
    private LocalDate today = LocalDate.now();
    
    private int oriSales;
//    private String[] week = {
//    	"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"	
//    };
//    
    public void initialize() throws IOException 
    {
    	DSID.setCellValueFactory(new PropertyValueFactory<>("id")); 
    	itemID.setCellValueFactory(new PropertyValueFactory<>("itemId"));//
    	date.setCellValueFactory(new PropertyValueFactory<>("date"));
        totalSales.setCellValueFactory(new PropertyValueFactory<>("totalSales"));
        author.setCellValueFactory(new PropertyValueFactory<>("author"));
       
        load();
    }
    
    public void load() throws IOException 
    {
//    	SalesM_DailyS listed= new SalesM_DailyS();
//    	ObservableList<SalesM_DailyS> itemList= FXCollections.observableArrayList(); 
//    	String[] row= listed.ReadTextFile().toString().split("\n");
//    	DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//    	LocalDate date = null;
//    	
//    	try {
//	    	for(String rows: row) 
//	    	{
//	    		String[] spl= rows.split(",");
//	    		if(spl.length==5) 
//	    		{
//	    			itemList.add(new SalesM_DailyS(
//	    					spl[0],
//	    					spl[1],
//	    					spl[2],
//	    					Integer.parseInt(spl[3]),
//	    					spl[4]
//	    					));
//	    			
//	    			date = LocalDate.parse(spl[2], format);
//	    		}
//	    	}
//	    	
//	    	resetWeek(date, format);
//	    	cacheList = itemList;
//	    	viewSalesTable.setItems(cacheList);
//	    	clearTextField();
    	
    	SalesM_DailyS listed= new SalesM_DailyS();
    	ObservableList<SalesM_DailyS> itemList= FXCollections.observableArrayList(); 
    	String[] row= listed.ReadTextFile().toString().split("\n");
    	DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    	
    	try {
	    	for(String rows: row) 
	    	{
	    		String[] spl= rows.split(",");
	    		if(spl.length==5) 
	    		{
	    			itemList.add(new SalesM_DailyS(
	    					spl[0],
	    					spl[1],
	    					spl[2],
	    					Integer.parseInt(spl[3]),
	    					spl[4]
	    					));
	    			
	    		}
	    	}
	    	
	    	resetWeek(today, format);
	    	cacheList = itemList;
	    	viewSalesTable.setItems(cacheList);
	    	clearTextField();
	    	
    	} catch (Exception e) {
    		
    		System.out.println(e);
    	}
    }
    
    public void resetWeek(LocalDate dateEnd, DateTimeFormatter format) {
    	
    	try (BufferedReader reader = new BufferedReader(new FileReader("Data/weekRecord.txt"))) {
    		
    		String fileDateStr = reader.readLine();
    		LocalDate lastReset = LocalDate.parse(fileDateStr, format);
    		
    		if(dateEnd.get(WeekFields.ISO.weekOfYear()) != lastReset.get(WeekFields.ISO.weekOfYear())
                || dateEnd.getYear() != lastReset.getYear()) {
    			
    			try (FileWriter writer = new FileWriter("Data/weekRecord.txt", false)) {
    				
    				writer.write(dateEnd.toString());
    				
    				try (FileWriter writerData = new FileWriter("Data/dailySales.txt", false)){
    					
    					load();
    				} catch (IOException e) {
    					e.printStackTrace();
    				}
    	        } catch (IOException e) {
    	            e.printStackTrace();
    	        }
 
    		} 
    		
    	} catch (Exception e) {
    		
    		System.out.println(e);
    	}
    	
//    	if (now.get(WeekFields.ISO.weekOfYear()) != dateEnd.get(WeekFields.ISO.weekOfYear())
//                || now.getYear() != dateEnd.getYear()) {
//
//            Files.write(Paths.get("Data/dailySales.txt"), new byte[0]);
//            Files.write(Paths.get("Data/weekRecord.txt"), now.format(formatter).getBytes());
//            System.out.println("Weekly file has been reset.");
//        } else {
//            System.out.println("Same week, no reset needed.");
//        }
    }
    
    
    public void chartload(String itemId) {
    	
        viewSalesChart.getData().clear();
        XYChart.Series<String, Integer> series = new XYChart.Series<>();
        series.setName("Items Sales");
        
        for (SalesM_DailyS item : cacheList) {
        	if(item.getItemId().equals(itemId)) {
        		series.getData().add(new XYChart.Data<>(item.getDate(), item.getTotalSales()));
        	}
        }

       viewSalesChart.getData().addAll(series);
    }
    
	public void rowClick() {
    	
    	try {
	    	SalesM_DailyS selectedItem = viewSalesTable.getSelectionModel().getSelectedItem();
	        
	        if (selectedItem != null) {
	            String id = selectedItem.getId();
	            String itemId = selectedItem.getItemId();
	            String date = selectedItem.getDate();
	            int totalSales = selectedItem.getTotalSales();
	            String author = selectedItem.getAuthor();
	            
	            //for edit Sales usage
	            oriSales = totalSales;
	            
//				<<For Testing>>
//	            System.out.println("Selected Item:");
//	            System.out.println("ID: " + id);
//	            System.out.println("Name: " + name);
//	            System.out.println("Supplier: " + supplier);
//	            System.out.println("Stock: " + stock);
//	            System.out.println("Unit Price: " + unitPrice);
	            
	            txtDSID.setText(id);
	            txtitemID.setText(itemId);
	            txtDate.setText(date);
	            txttotalSales.setText(String.valueOf(totalSales));
	            txtAuthor.setText(String.valueOf(author));
	            
	            txtDSID.setEditable(false);
	            txtitemID.setEditable(false);

	            chartload(itemId);
	        }
	    } catch (Exception e) {
	    	
	    	Alert alert = new Alert(AlertType.INFORMATION);
	        alert.setTitle("Error");
	        alert.setHeaderText("Something went wrong");
	        alert.setContentText("Error: " + e.getMessage());
	        alert.showAndWait();
	    }
    	
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
    
	@FXML
    public void addeditClick() {
    	
		SalesM_DailyS selectedDS = viewSalesTable.getSelectionModel().getSelectedItem();	
		int selectedSuppIndex = viewSalesTable.getSelectionModel().getSelectedIndex();
	
    	try {
    		
			SalesM_DailyS dataEntry = new SalesM_DailyS(
					
				txtDSID.getText().trim(),
				txtitemID.getText().trim(),
				txtDate.getText().trim(),
				Integer.parseInt(txttotalSales.getText().trim()),
				"temp", //Use the UserID in the superclass (author), so  the system will record who edit this record
				cacheList, 
				selectedSuppIndex,
				oriSales
				);
		
			boolean result = dataEntry.insertCheck(selectedDS);
			
			if (result) {
				
				ObservableList<SalesM_DailyS>  tempList = dataEntry.getCacheList();
			    cacheList = tempList;
			    viewSalesTable.setItems(cacheList);
			    clearTextField();
			} else {
				
				Alert alert = new Alert(AlertType.ERROR);
	    		alert.setTitle("Error");
	    		alert.setHeaderText(null);
	    		alert.setContentText("Please do something correct");
	    		alert.showAndWait();
			}
		
    	} catch (Exception e) {
    		
    		Alert alert = new Alert(AlertType.ERROR);
    		alert.setTitle("Error");
    		alert.setHeaderText(null);
    		alert.setContentText(String.format("Error: %s", e.toString()));
    		alert.showAndWait();
    	}	
    }
    
    @FXML
    public void deleteClick() {
    	
    	int selectedSuppIndex = viewSalesTable.getSelectionModel().getSelectedIndex();
    	
    	SalesM_DailyS delIndex = new SalesM_DailyS(selectedSuppIndex, cacheList);
    	
    	if (selectedSuppIndex >= 0) {
	    	try {
	    		
	    		delIndex.DeleteFunc();
	    		ObservableList<SalesM_DailyS>  tempList = delIndex.getCacheList();
	    		cacheList = tempList;
	    		viewSalesTable.setItems(cacheList);
	    		clearTextField();
	    		
	    	} catch (Exception e) {
	    		
	    		Alert alert = new Alert(AlertType.INFORMATION);
	    	    alert.setTitle("Error");
	    	    alert.setHeaderText("Something went wrong");
	    	    alert.setContentText("Error: " + e.getMessage());
	    	    alert.showAndWait();
	    	}
    	} else {
    		
    		Alert alert = new Alert(AlertType.INFORMATION);
    	    alert.setTitle("Error");
    	    alert.setHeaderText("Something went wrong");
    	    alert.setContentText("Bro please select a row first lah, why you try to delete empty (^_^') ");
    	    alert.showAndWait();
    	}
    }
    
    @FXML
    public void saveClick() throws IOException{
    	
    	StringBuilder result = new StringBuilder();
    	try {
	    	for (SalesM_DailyS dailyS : cacheList) {
	            
	            result.append(dailyS.getId()).append(",")
	                  .append(dailyS.getItemId()).append(",")
	                  .append(dailyS.getDate()).append(",")
	                  .append(dailyS.getTotalSales()).append(",")
	                  .append(dailyS.getAuthor()).append("\n");  
	        }
	    	
	    	String netString = result.toString();
	    	SalesM_DailyS note = new SalesM_DailyS(netString);
	    	note.SaveFunc();
	    	
	    	clearTextField();
	    	reloadClick();
    	} catch (Exception e) {
    		
    		Alert alert = new Alert(AlertType.INFORMATION);
    	    alert.setTitle("Error");
    	    alert.setHeaderText("Something went wrong");
    	    alert.setContentText("Error: " + e.getMessage());
    	    alert.showAndWait();
    	}
    }
    
    @FXML
    public void reloadClick() throws IOException {
    	
    	
    	cacheList.clear();
    	load();
    }
    
    public void clearTextField() {
    	
    	TextField[] textFields = {txtDSID, txtitemID, txtDate, txttotalSales, txtAuthor};
    	for (TextField field : textFields) {
    	    field.clear();      	
    	}
    	
    	txtDSID.setEditable(true);
        txtitemID.setEditable(true);
    }
}
