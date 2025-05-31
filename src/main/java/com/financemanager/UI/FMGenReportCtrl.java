package com.financemanager.UI;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;

import com.financemanager.source.FMGenReport;
import com.groupfx.JavaFXApp.PdfGenerator;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class FMGenReportCtrl {

    @FXML
    private TableColumn<FMGenReport, String> PayDate;

    @FXML
    private TableColumn<FMGenReport, String> PayId;
    @FXML
    private TableColumn<FMGenReport, String> PoId;

    @FXML
    private TableColumn<FMGenReport, String> PayItemName;

    @FXML
    private TableColumn<FMGenReport, String> PayItemsd;

    @FXML
    private TableColumn<FMGenReport, Integer> PayQty;

    @FXML
    private TableColumn<FMGenReport, String> PaySupp;

    @FXML
    private TableColumn<FMGenReport, Double> PayTot;

    @FXML
    private Button PrintBtn;

    @FXML
    private LineChart<String, Double> ReportChart;





    @FXML
    private TableView<FMGenReport> ViewPayment;

    @FXML
    private ComboBox<String> Yearbox;

    
    public void initialize() throws IOException 
    {
    	PayDate.setCellValueFactory(new PropertyValueFactory<>("date"));
    	PayId.setCellValueFactory(new PropertyValueFactory<>("payId"));
    	PoId.setCellValueFactory(new PropertyValueFactory<>("ID")); // PO ID
    	PayItemName.setCellValueFactory(new PropertyValueFactory<>("itemName"));
    	PayItemsd.setCellValueFactory(new PropertyValueFactory<>("itemId"));
    	PayQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
    	PaySupp.setCellValueFactory(new PropertyValueFactory<>("supplier"));
    	PayTot.setCellValueFactory(new PropertyValueFactory<>("total"));
    	load();
    }
    
    public void load() throws IOException
    {	int index=0;
    	FMGenReport source=new FMGenReport();
    	ObservableList<FMGenReport> obList= FXCollections.observableArrayList();
    	String[] data=source.ReadTextFile().toString().split("\n");
    	
    	for(String Line:data) 
    	{	//String PayId,String ID,String ItemId,String ItemName,String Supplier,int Qty,double Total,String date)
    		String[] parts=Line.split(",");
    		 if(parts.length==10) 
    		 {
    		
    		String[] ItemName=source.RetriveItemName().toArray(new String[0]);
    		//PY001,PO001,I0003,1501,111.0,Approve,S003,166611.00,Paid,13-04-2025
    		
    		
    		obList.add(new FMGenReport(
    				
    					parts[0],
    					parts[1],
    					parts[2],
    					ItemName[index],
    					parts[6],
    					Integer.parseInt(parts[3]),
    					Double.parseDouble(parts[7]),
    					parts[9]
    			
    				));
    		index++;
    		 }else 
    	    	{
    	    		ViewPayment.setPlaceholder(new Label("No Contents Here"));
    	    	}
    	}
    	ViewPayment.setItems(obList);
    	Yearbox.getItems().clear();
    	Yearbox.getItems().addAll(source.RetriveYear());
    	}
    
    
    public void LineChart(ObservableList<FMGenReport> List) 
    {
    	ReportChart.getData().clear();
    	XYChart.Series<String, Double> series= new XYChart.Series<>();
    	series.setName("Month");
    	double[] month= new double[12];
    	DateTimeFormatter date= DateTimeFormatter.ofPattern("dd-MM-yyyy");
    	for(FMGenReport Report: List) 
    	{
    		LocalDate d= LocalDate.parse(Report.getDate(),date);
    		int mth= d.getMonthValue();
    		month[mth-1]+= Report.getTotal();
    		
    		
    	}
    	
    	for(int i=0; i<12;i++) 
    	{
    		String monthLabel= String.format("%02d",i+1);
    		series.getData().add(new XYChart.Data<String, Double>(monthLabel,month[i]));
    	}
    	
    	ReportChart.getData().add(series);
    }
    
    
    public void PrintClick(MouseEvent event) {
        Stage stage = (Stage) PrintBtn.getScene().getWindow();
        List<FMGenReport> data = ViewPayment.getItems();
        String year = Yearbox.getSelectionModel().getSelectedItem();

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                try {
                    PdfGenerator generator = new PdfGenerator();

                    // Generate Report PDF can put on background, not involve in UI Threads
                    PDDocument doc = generator.PrepareReport(data, year, "FINANCIAL STATEMENT");

                    // Need Run on the UI Thread FileChooser Save + Alert + open
                    Platform.runLater(() -> {
                        try {
                            generator.savePdfWithChooser(doc, stage,"Financial_Report.pdf","Save Financial Report");  //  FileChooser and Alert
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

    @FXML
    public void YearCb(ActionEvent event) throws IOException 
    {
    	
    }
    

    @FXML
   public void refclick(MouseEvent event) throws IOException {
    	
    	String dim= Yearbox.getSelectionModel().getSelectedItem();
    	if(dim!=null) {
	    	int index=0;
	    	FMGenReport source= new FMGenReport();
	    	ObservableList<FMGenReport> obList= FXCollections.observableArrayList();
	    	String year= Yearbox.getSelectionModel().getSelectedItem();
	    	String[] data=source.YearBasedData(year).toString().split("\n");
	    	for(String Line:data) 
	    	{	//String PayId,String ID,String ItemId,String ItemName,String Supplier,int Qty,double Total,String date)
	    		String[] parts=Line.split(",");
	    		 if(parts.length==10) 
	    		 {
	    		
	    		String[] ItemName=source.RetriveItemName().toArray(new String[0]);
	    		//PY001,PO001,I0003,1501,111.0,Approve,S003,166611.00,Paid,13-04-2025
	    		
	    		
	    		obList.add(new FMGenReport(
	    				
	    					parts[0],
	    					parts[1],
	    					parts[2],
	    					ItemName[index],
	    					parts[6],
	    					Integer.parseInt(parts[3]),
	    					Double.parseDouble(parts[7]),
	    					parts[9]
	    			
	    				));
	    		index++;
	    		 }else 
	    	    	{
	    	    		ViewPayment.setPlaceholder(new Label("No Contents Here"));
	    	    	}
	    	}
	    	ViewPayment.setItems(obList);
	    	LineChart(obList);
	    }
    	else 
    	{
    		Alert alert= new Alert(AlertType.ERROR);
    		alert.setContentText("Please Select a Year from combo box");
    		alert.showAndWait();
    	}
    }
    

}
