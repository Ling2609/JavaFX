package com.financemanager.UI;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import com.financemanager.source.FMPayment;
import com.groupfx.JavaFXApp.Restriction_Text;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

public class FMPaymentCtrl {

    @FXML
    private Button ApproveBtn;

    @FXML
    private TableColumn<FMPayment, String> POId;

    @FXML
    private TextField PayId;

    @FXML
    private TextField PayItem;

    @FXML
    private TextField PayQty;

    @FXML
    private TextField PaySupp;

    @FXML
    private TextField PayTot;

    @FXML
    private TextField PayUp;

    @FXML
    private TableColumn<FMPayment, String> PoItem;

    @FXML
    private TableColumn<FMPayment, Integer> PoQty;

    @FXML
    private TableColumn<FMPayment, String> PoStat;

    @FXML
    private TableColumn<FMPayment, String> PoSupp;

    @FXML
    private TableColumn<FMPayment, Double> PoUp;

    @FXML
    private Button RefBtn;

    @FXML
    private Button SaveBtn;

    @FXML
    private TableView<FMPayment> ViewPO;
    
    private List<String> Combo=new ArrayList<>();
    
    private String TotalPrice;
    private boolean ApproveClick=false;
    
    
    public void initialize() throws IOException
    {
    	POId.setCellValueFactory(new PropertyValueFactory<>("id"));
    	PoItem.setCellValueFactory(new PropertyValueFactory<>("name"));
    	PoQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
    	PoUp.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
    	PoSupp.setCellValueFactory(new PropertyValueFactory<>("supplier"));
    	PoStat.setCellValueFactory(new PropertyValueFactory<>("status"));
    	ViewPO.setSortPolicy(t->false);
    	load();
    	
    	Restriction_Text filter= new Restriction_Text("\\d*(\\.\\d*)?");
    	TextFormatter<TextFormatter.Change> format= new TextFormatter<>(filter);
    	PayTot.setTextFormatter(format);
    	
    }
    
    public void load() throws IOException 
    {
    	FMPayment Source= new FMPayment();
    	ObservableList<FMPayment> obList= FXCollections.observableArrayList();
    	String[] firstData= Source.LoadData().toString().split("\n");
    
    	
    	if(!Source.isArrayBlank(firstData)) 
    	{	int index=0;
    		String[] UnitPrice= Source.RetriveItemUnitPrice().toArray(new String[0]);
	    	for(String rows: firstData) 
	    	{
	    		String[] realData= rows.split(",");
	    		
	    		obList.add(new FMPayment(
	    					realData[0],
	    					realData[1],
	    					Integer.parseInt(realData[2]),
	    					Double.parseDouble(UnitPrice[index]),
	    					realData[5],
	    					realData[6]
	    				));
	    		index++;
	    	}
	    
	    	ViewPO.setItems(obList);
	    }else 
	    {
	    	ViewPO.setPlaceholder(new Label("All Payments have been made !"));
	    }
    }
    
    public void ClearTextBox(TextField...fields) 
    {
    	for(TextField field:fields) 
    	{
    		field.clear();
    	}
    }

    public boolean CheckTextFieldisEmpty(TextField...fields) 
    {
    	for(TextField field: fields) 
    	{
    		String Content= field.getText();
    		if(Content.trim().isEmpty()) 
    		{
    			return true;
    		}
    	}
    	return false;
    }
    
    @FXML
   public void ApproveClick(MouseEvent event) throws IOException {
    	FMPayment Source= new FMPayment();
    	
    	
    	if (ViewPO.getSelectionModel().getSelectedIndex()>=0 && !CheckTextFieldisEmpty(PayId,PayItem,PayQty,PaySupp,PayTot,PayUp) && ApproveClick==false)
    	{	this.Combo=Source.SetData();
    		Source.Approve(PayId.getText());
	    	if(Source.checkingFunc()) 
	    	{
	    		
	    		
	    		Alert alert= new Alert(AlertType.INFORMATION);
	    		alert.setTitle("Edit");
	    		alert.setContentText("Edit Sucess, Please Save Before Leaving!");
	    		alert.showAndWait();
	    		ViewPO.getItems().remove(ViewPO.getSelectionModel().getSelectedIndex());
	    		ViewPO.getSelectionModel().clearSelection();
	    		TotalPrice=PayTot.getText();
	    		ClearTextBox(PayId,PayItem,PayQty,PaySupp,PayTot,PayUp);
	    		ApproveClick=true;
	    	
	    		
	    	}
	    	else 
	    	{	ApproveClick=false;
	    		Alert alert= new Alert(AlertType.WARNING);
	    		alert.setTitle("Approve");
	    		alert.setContentText("Error Occur or No Changes Made! Please Make Sure the PO Payment Status is not Paid");
	    		alert.showAndWait();
	    		
	    	}
    	}
    	else 
    	{	ApproveClick=false;
    		Alert alert= new Alert(AlertType.WARNING);
    		alert.setTitle("Approve");
    		alert.setContentText("Please Fill All The Blanks \n PLEASE CLICK SAVE BEFORE PAY ANOTHER BILL !");
    		alert.showAndWait();
    		
    	}
    }

    @FXML
    public void RefreshClick(MouseEvent event) throws IOException {
    	ViewPO.getSelectionModel().clearSelection();
    	ApproveClick=false;
    	load();
    }

    @FXML
    public void SaveClick(MouseEvent event) throws IOException {
//    	FMPayment DataSource= new FMPayment();
//    	List<String> Combo=new ArrayList<>();
//    	Combo=DataSource.SetData();
    	if(ApproveClick==true) 
    	{
	    	FMPayment Source= new FMPayment(TotalPrice,Combo);
	    	Source.SaveFunc();
	    	if(Source.checkingFunc()) 
	    	{
	    		
	    		
	    		Alert alert= new Alert(AlertType.INFORMATION);
	    		alert.setTitle("Saving");
	    		alert.setContentText("Save!");
	    		alert.showAndWait();
	    		ViewPO.getSelectionModel().clearSelection();
	    		ApproveClick=false;
	    		
	    	}
	    	else 
	    	{	
	    		ApproveClick=false;
	    		Alert alert= new Alert(AlertType.WARNING);
	    		alert.setTitle("Saving");
	    		alert.setContentText("Error Occur or No Changes Made! Please Make Sure the PO Status is not Approve");
	    		alert.showAndWait();
	    	}
    	}
    	else 
    	{	
    		ApproveClick=false;
    		Alert alert= new Alert(AlertType.WARNING);
    		alert.setTitle("Saving");
    		alert.setContentText("Please Click Approve Before Saving or No Changes Made !");
    		alert.showAndWait();
    	}
    }
    
    @FXML
   public void RowClick(MouseEvent event) 
    {
    	FMPayment RowSelection= ViewPO.getSelectionModel().getSelectedItem();
    	if(RowSelection!=null) 
    	{
    		PayId.setText(RowSelection.getId());
    		PayItem.setText(RowSelection.getName());
    		PayQty.setText(Integer.toString(RowSelection.getQty()));
    		PaySupp.setText(RowSelection.getSupplier());
    		PayUp.setText(Double.toString(RowSelection.getUnitPrice()));
    		
    		double total = RowSelection.getUnitPrice() * RowSelection.getQty();
    		BigDecimal bd = new BigDecimal(total).setScale(2, RoundingMode.HALF_UP); //round to two decimal point
    		PayTot.setText(bd.toPlainString()); //avoided scientific calculations
    		PayId.setEditable(false);
    		PayItem.setEditable(false);
    		PayQty.setEditable(false);
    		PaySupp.setEditable(false);
    		PayUp.setEditable(false);
    		PayTot.setFocusTraversable(false);
    	}
    }

}
