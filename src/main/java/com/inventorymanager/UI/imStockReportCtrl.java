package com.inventorymanager.UI;

import java.io.IOException;
import java.time.Year;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.pdfbox.pdmodel.PDDocument;

import com.groupfx.JavaFXApp.PdfGenerator;
import com.inventorymanager.source.*;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.paint.Color;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class imStockReportCtrl {
		
	@FXML
	private BarChart<String, Number> viewStockChart;
	
	@FXML
    private Button generateBtn;
	
	private List<InventoryM_Stocks> reportData = new ArrayList<>();;
	
	private HashMap<String, Integer> chartStore = new HashMap<>();
    
	private static final int LOW_STOCK_THRESHOLD = 300;  // Example threshold for low stock
   
    private static final int HIGH_STOCK_THRESHOLD = 1000;  // Example threshold for high stock

	public void initialize() throws IOException 
    {
        
        load();
    }
	
	public void load() throws IOException 
    {
    	InventoryM_Stocks listed= new InventoryM_Stocks();
    	String[] row= listed.ReadStockTextFile().toString().split("\n");
    	
    	for(String rows: row) 
    	{
    		String[] spl= rows.split(",");
    		if(spl.length==5) 
    		{	
    			reportData.add(new InventoryM_Stocks(
    					
    					spl[0],
    					spl[1],
    					Integer.parseInt(spl[3])
    						
    					));
    					
    			chartStore.put(spl[1], Integer.parseInt(spl[3]));
    		}
    	}
    	chartload();
    }
	
	public void chartload() {
	    viewStockChart.getData().clear();
	    XYChart.Series<String, Number> series = new XYChart.Series<>();
	    viewStockChart.setLegendVisible(false);
	    
	    int maxStock = Integer.MIN_VALUE;
	    int minStock = Integer.MAX_VALUE;

	    for (Map.Entry<String, Integer> entry : chartStore.entrySet()) {
	        int stock = entry.getValue();
	        String item = entry.getKey();

	        if (stock > maxStock) maxStock = stock;
	        if (stock < minStock) minStock = stock;

	        XYChart.Data<String, Number> dataPoint = new XYChart.Data<>(item, stock);
	        series.getData().add(dataPoint);
	    }

	    viewStockChart.getData().add(series);

	    // Dynamic Y-axis scaling with padding
	    NumberAxis yAxis = (NumberAxis) viewStockChart.getYAxis();
	    yAxis.setAutoRanging(false);
	    yAxis.setLowerBound(Math.max(0, minStock - 10));
	    yAxis.setUpperBound(maxStock + 10);
	    
	    // Dynamic tick unit based on range
	    double range = maxStock - minStock;
	    double tickUnit = Math.max(1, range / 10);  // Adjust this division if needed
	    yAxis.setTickUnit(tickUnit);

	    // Style bars after chart nodes are rendered
	    Platform.runLater(() -> {
	        for (XYChart.Data<String, Number> data : series.getData()) {
	            Node node = data.getNode();
	            if (node != null) {
	                int value = data.getYValue().intValue();

	                if (value < LOW_STOCK_THRESHOLD) {
	                    node.setStyle("-fx-bar-fill: red;");
	                } else if (value > HIGH_STOCK_THRESHOLD) {
	                    node.setStyle("-fx-bar-fill: green;");
	                } else {
	                    node.setStyle("-fx-bar-fill: yellow;");
	                }
	            }
	        }
	    });
	}

    
    public void generateReport() {
    	
    	Stage stage = (Stage) generateBtn.getScene().getWindow();
    	String year = String.valueOf(Year.now().getValue());
    	
    	Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                try {
                    PdfGenerator generator = new PdfGenerator();

                    // Generate Report PDF can put on background, not involve in UI Threads
                    PDDocument doc = generator.PrepareReport(reportData, year, "STOCK REPORT");

                    // Need Run on the UI Thread FileChooser Save + Alert + open
                    Platform.runLater(() -> {
                        try {
                            generator.savePdfWithChooser(doc, stage,"Report.pdf","Save Financial Report");  //  FileChooser and Alert
                        } catch (IOException e) {
                            e.printStackTrace();
                            showError("Error while saving PDF: " + e.getMessage());
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                    Platform.runLater(() -> showError("Failed to generate PDF: " + e.getMessage()));
                }
                return null;
            }
        };

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }
    
    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("PDF Generation Failed");
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
