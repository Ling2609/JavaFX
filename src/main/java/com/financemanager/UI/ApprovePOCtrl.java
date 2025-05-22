package com.financemanager.UI;

import java.io.IOException;

import com.financemanager.source.FMAppPO;
import com.financemanager.source.FMPayment;
import com.groupfx.JavaFXApp.Purchase_Order;
import com.groupfx.JavaFXApp.Restriction_Text;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

public class ApprovePOCtrl {

    @FXML
    private Button AppEdit;

    @FXML
    private TableColumn<FMAppPO, String> AppID;

    @FXML
    private TextField AppIdBx;

    @FXML
    private TableColumn<FMAppPO, String> AppItem;

    @FXML
    private TableColumn<FMAppPO, String> AppPM;

    @FXML
    private TableColumn<FMAppPO, Double> AppPrice;

    @FXML
    private TableColumn<FMAppPO, Integer> AppQty;

    @FXML
    private TextField AppQtyBx;

    @FXML
    private Button AppRef;

    @FXML
    private Button AppSave;

    @FXML
    private TableColumn<FMAppPO, String> AppStat;


    @FXML
    private TableView<FMAppPO> ViewPO;
    
    @FXML
    private ComboBox<String> SupplierCbx;
    
    @FXML
    private ComboBox<String> StatusSelection;
    
    
    public void initialize() throws IOException 
    {
    	AppID.setCellValueFactory(new PropertyValueFactory<>("poId"));
    	AppItem.setCellValueFactory(new PropertyValueFactory<>("itemsId"));
    	AppQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
    	AppPrice.setCellValueFactory(new PropertyValueFactory<>("cost"));
    	AppPM.setCellValueFactory(new PropertyValueFactory<>("pm"));
    	AppStat.setCellValueFactory(new PropertyValueFactory<>("status"));
    	AppIdBx.setEditable(false);
    	ViewPO.setSortPolicy(null);
    	load();
    	
    	Restriction_Text filter= new Restriction_Text("\\d*");
    	TextFormatter<TextFormatter.Change> format= new TextFormatter<>(filter);
    	AppQtyBx.setTextFormatter(format);
    }


    @FXML
    public void RowClick(MouseEvent event) throws IOException {
    	FMAppPO selectedItems=ViewPO.getSelectionModel().getSelectedItem();
    	FMAppPO data= new FMAppPO();
    	if(selectedItems!=null) 
    	{
    		AppIdBx.setText(selectedItems.getId());
    		AppQtyBx.setText(Integer.toString(selectedItems.getQty()));
    		String[] CbData= data.GetSupplier(selectedItems.getItemsId()).toArray(new String[0]);
    		SupplierCbx.getItems().clear();
        	SupplierCbx.getItems().addAll(CbData);
    		
    	}
    }
    
    
    public void load() throws IOException 
    {	FMAppPO data= new FMAppPO();
    	ObservableList<FMAppPO> obList= FXCollections.observableArrayList();
    	String[] Sdata= data.ReadTextFile().toString().split("\n");
    	
    	for(String row: Sdata) 
    	{
    		String[] RowData= row.split(",");
    		obList.add(new FMAppPO(
    					RowData[0],
    					RowData[1],
    					Integer.parseInt(RowData[2]),
    					Double.parseDouble(RowData[3]),
    					RowData[4],
    					RowData[5]
    				));
    	}
    	ViewPO.setItems(obList);
    	StatusSelection.getItems().addAll("Approve","Rejected");
    	
    }
    
    
    public boolean CheckComboBox(ComboBox<?>...boxs) 
    {
    	for(ComboBox<?> boxes: boxs) 
    	{
    		if(boxes.getSelectionModel().getSelectedIndex()==-1) 
    		{
    			return true;
    		}
    	}
    	return false;
    }
    
    
    public boolean CheckTextField(TextField...fields) 
    {
    	for(TextField field :fields) 
    	{
    		String content= field.getText();
    		if(content.trim().isEmpty()) 
    		{
    			return true;
    		}
    	}
    	return false;
    }
    
    
    @FXML
    public void EditClick(MouseEvent event) {
    	
    	String SelectedSupplier= SupplierCbx.getSelectionModel().getSelectedItem();
    	String SelectedStatus=StatusSelection.getSelectionModel().getSelectedItem();
    	int SelectedIndex=ViewPO.getSelectionModel().getSelectedIndex();
    	
    	if(!CheckTextField(AppQtyBx) && !CheckComboBox(SupplierCbx,StatusSelection)) 
    	{
		    	FMAppPO data= new FMAppPO(SelectedIndex,AppQtyBx.getText(),SelectedSupplier,SelectedStatus);
		    	data.EditFunc();
		    	System.out.println(data.checkingFunc());
		    	if(data.checkingFunc()) 
		    	{
		
		    		Alert alert= new Alert(AlertType.INFORMATION);
		    		alert.setTitle("Adding Sucess");
		    		alert.setHeaderText(null);
		    		alert.setContentText("Edit Sucessfull, Please Save Before Leaving !");
		    		alert.showAndWait();
		    		ViewPO.getSelectionModel().clearSelection(); //clear selection
		    		
		    	}
		    	else 
		    	{
		    		Alert alert= new Alert(AlertType.ERROR);
		    		alert.setTitle("Adding Unsucess");
		    		alert.setHeaderText(null);
		    		alert.setContentText("Edit Unsucessfull, Please Check Your Details");
		    		alert.showAndWait();
		    		ViewPO.getSelectionModel().clearSelection(); //clear selection
		    	}
	    }
    	else 
    	{
    		Alert alert= new Alert(AlertType.ERROR);
    		alert.setTitle("Adding Sucess");
    		alert.setHeaderText(null);
    		alert.setContentText("Edit Unsucessfull, Please Check Your Details");
    		alert.showAndWait();
    		ViewPO.getSelectionModel().clearSelection(); //clear selection
    	}
    }

    @FXML
   public void RefreshClick(MouseEvent event) throws IOException {
    	StatusSelection.getSelectionModel().select(-1);
    	SupplierCbx.getSelectionModel().select(-1);
    	StatusSelection.getItems().clear();
    	SupplierCbx.getItems().clear();
    	load();
    
    }

    @FXML
   public void SaveClick(MouseEvent event) {
    	FMAppPO approve= new FMAppPO();
    	approve.SaveFunc();
    	if(approve.checkingFunc()) 
    	{
    		Alert alert= new Alert(AlertType.INFORMATION);
    		alert.setTitle("Save Sucess");
    		alert.setHeaderText(null);
    		alert.setContentText("Save Sucessfull");
    		alert.showAndWait();
    		ViewPO.getSelectionModel().clearSelection(); //clear selection
    	}else 
    	{
    		Alert alert= new Alert(AlertType.INFORMATION);
    		alert.setTitle("Save Unsucess");
    		alert.setHeaderText(null);
    		alert.setContentText("Save Unsucessfull");
    		alert.showAndWait();
    		ViewPO.getSelectionModel().clearSelection(); //clear selection
    	}
    }

}
