package com.purchasemanager.UI;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.function.UnaryOperator;

import org.apache.pdfbox.pdmodel.PDDocument;

import com.PM.Sources.*;
import com.financemanager.source.FMGenReport;
import com.groupfx.JavaFXApp.Authentication;
import com.groupfx.JavaFXApp.PdfGenerator;
import com.groupfx.JavaFXApp.Purchase_Order;
import com.groupfx.JavaFXApp.Restriction_Text;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.css.*;

public class GenPOCtrl {

    @FXML
    private Button Addbtn;

    @FXML
    private Button PrintBtn;
    
    @FXML
    private Button DelBtn;

    @FXML
    private TextField IdTxtbx;

    @FXML
    private TextField ItemsNameTxt;

    @FXML
    private TextField PMtxt;

    @FXML
    private TableColumn<Standard_PO, String> POIName;

    @FXML
    private TableColumn<Standard_PO, String> POid;

    @FXML
    private TableColumn<Standard_PO, Double> POprice;

    @FXML
    private TableColumn<Standard_PO, Integer> POqty;

    @FXML
    private TableColumn<Standard_PO, String> POso;

    @FXML
    private TextField Pricetxt;

    @FXML
    private TextField QtyTxt;

    @FXML
    private Button RefBtn;

    @FXML
    private ComboBox<String> PRidCb;
    
    @FXML
    private TableView<Standard_PO> ViewPO;

    
    
    @FXML
    private Button EditBtn;
    
    @FXML
    private Button SaveBtn;
    
    public void initialize() throws IOException
    {	
    	
    	POid.setCellValueFactory(new PropertyValueFactory<>("id"));
    	POIName.setCellValueFactory(new PropertyValueFactory<>("name"));
    	POqty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
    	POprice.setCellValueFactory(new PropertyValueFactory<>("price"));
    	POso.setCellValueFactory(new PropertyValueFactory<>("pm"));
    	ViewPO.setSortPolicy(null); //disable sort
    	
    	SetText();
    	load();
    	
    	//Change Color on TableView 
    	ViewPO.setRowFactory(tableView -> new TableRow<>() {
    	    @Override
    	    protected void updateItem(Standard_PO item, boolean empty) {
    	        super.updateItem(item, empty);
    	        if (item == null || empty) {
    	            setStyle("");
    	        } else {
    	            switch (item.getStatus()) {
    	                case "Approve":
    	                    setStyle("-fx-background-color: #008000;");
    	                    break;
    	                case "Rejected":
    	                    setStyle("-fx-background-color: #FF0000;");
    	                    break;
    	                default:
    	                    setStyle("");
    	                    break;
    	            }
    	        }
    	    }
    	});
    	
    	
    	Restriction_Text filter= new Restriction_Text("\\d*(\\.\\d*)?");
    	TextFormatter<String> format= new TextFormatter<>(filter);
    	Pricetxt.setTextFormatter(format);
    	
    	Restriction_Text filQty= new Restriction_Text("\\d*");
    	TextFormatter<String> formatQty= new TextFormatter<>(filQty);
    	QtyTxt.setTextFormatter(formatQty);
    }
    
    
    public int CbSelection() 
    {
    	int Cbselection=PRidCb.getSelectionModel().getSelectedIndex();
    	return Cbselection;
    }
    
    public void CleanTextbox(TextField...fields) 
    {
    	for(TextField field: fields) 
    	{	
    		field.clear();
    	}
    	
    }
    
    
    public boolean CheckTxtbox(TextField...fields) 
    {
    	for(TextField field:fields ) 
    	{	String content= field.getText();
    		if(content.equals(null)||content.trim().isEmpty())
    		{
    			return true;
    		}
    	}
    	return false;
    }
    
    
    
    public String SetText() throws IOException 
    {
    	BufferedReader reader= new BufferedReader(new FileReader("Data/Log.txt"));
    	PMtxt.setEditable(false);
    	String l,Name=null;
    	while((l=reader.readLine())!=null) 
    	{	
    		String[] data= l.split(",");
    		Name=data[0];
	    	PMtxt.setText(Name);
	    	if(!PMtxt.getText().equals(Name)) 
	    	{
	    		PMtxt.setText(Name);
	    		
	    	}
    		
    	}
    	
    	
	    	
    	reader.close();
    	return Name;
    	
    }
    
    public void load() throws IOException 
    {
    	Standard_PO data= new Standard_PO();
    	ObservableList<Standard_PO> obList= FXCollections.observableArrayList();
    	String[] rows= data.ReadTextFile().toString().split("\n");
    	
    	for(String row:rows) 
    	{	String[] split=row.split(",");
    		if(split.length==8) 
    		{
    			obList.add(new Standard_PO(
    					split[0],
    					split[1],
    					Integer.parseInt(split[2]),
    					Double.parseDouble(split[3]),
    					split[4],
    					split[5]
    					));
    		}
    	}
    	ViewPO.setItems(obList);
    	    	
    	String[] Prid= data.RetrivePR().toString().split("\n");
    	
    	List<String> ListData= Arrays.asList(Prid);
    	PRidCb.getItems().addAll(ListData);
    	
    	
    	
    	
    }
    
    
    @FXML
    public void AddClick(MouseEvent event) {
    	//String Id, String name, String Quantity, String Price, String Pm
    	
    	if(!CheckTxtbox(IdTxtbx,ItemsNameTxt,PMtxt,Pricetxt,QtyTxt)&& CbSelection()!=-1) 
    	{		int SelectedIndex= PRidCb.getSelectionModel().getSelectedIndex();
		    	Standard_PO data= new Standard_PO(IdTxtbx.getText(),ItemsNameTxt.getText(),Integer.parseInt(QtyTxt.getText()),Double.parseDouble(Pricetxt.getText()),PMtxt.getText(),SelectedIndex);
		    	data.AddFunc();
		    	if(data.checkingFunc()) 
		    	{
		    		Alert alert= new Alert(AlertType.INFORMATION);
		    		alert.setTitle("Adding Sucess");
		    		alert.setHeaderText(null);
		    		alert.setContentText("Adding Sucessfull, Please Save Before Leaving !");
		    		alert.showAndWait();
		    		ViewPO.getSelectionModel().clearSelection(); //clear selection
		    		CleanTextbox(ItemsNameTxt,Pricetxt,QtyTxt);
		    	
		    	}
		    	else 
		    	{
		    		Alert alert= new Alert(AlertType.INFORMATION);
		    		alert.setTitle("Adding Failed");
		    		alert.setHeaderText(null);
		    		alert.setContentText("Adding Failed or Please Select an PRID for Adding And Make Sure PO Status is NOT Approve!");
		    		alert.showAndWait();
		    	}
	    }
    	else 
	    {
    		Alert alert= new Alert(AlertType.ERROR);
    		alert.setTitle("Adding Failed");
    		alert.setHeaderText(null);
    		alert.setContentText("Please Ensure All data is Filled ! And Perform Refresh!");
    		alert.showAndWait();
	    }
    }

 
    
    @FXML
    public void RowSelection(MouseEvent e) throws IOException 
    {
    	Purchase_Order selectedItems= ViewPO.getSelectionModel().getSelectedItem();
    	if(selectedItems!=null) 
    	{
    		IdTxtbx.setText(selectedItems.getId());
    		ItemsNameTxt.setText(selectedItems.getName());
    		Pricetxt.setText(Double.toString(selectedItems.getPrice()));
    		QtyTxt.setText(Integer.toString(selectedItems.getQuantity()));
    		PMtxt.setText(SetText());
    		
    	}
    }
    
    
    @FXML
    public void DelClick(MouseEvent event) {
    	int selectedIndex=ViewPO.getSelectionModel().getSelectedIndex();
    	
    	if(selectedIndex>=0 && CbSelection()==-1) 
    	{
    		
    		Standard_PO del= new Standard_PO(selectedIndex);
    		del.DeleteFunc();
    		if(del.checkingFunc()) 
    		{	ViewPO.getItems().remove(selectedIndex);
    			Alert alert= new Alert(AlertType.INFORMATION);
    			alert.setTitle("Delete");
    			alert.setContentText("Delete Sucessfull");
    			alert.showAndWait();
    			ViewPO.getSelectionModel().clearSelection(); //clear the selection
    			CleanTextbox(IdTxtbx,ItemsNameTxt,Pricetxt,QtyTxt);
    			PRidCb.getSelectionModel().select(-1);
    		}
    		else 
    		{
    			Alert alert= new Alert(AlertType.ERROR);
    			alert.setTitle("Delete");
    			alert.setContentText("Delete Failed, Please Do not choose the PRID selection.Then Make Sure PO status is NOT Approve");
    			alert.showAndWait();
    		}
    	}
    	else 
    	{
    		Alert alert= new Alert(AlertType.ERROR);
			alert.setTitle("Delete");
			alert.setContentText(" Please Do not choose the PRID selection");
			alert.showAndWait();
    	}
    }

    

    @FXML
    public void EditClick(MouseEvent event) {
    	int selectedIndex=ViewPO.getSelectionModel().getSelectedIndex();
    	if(selectedIndex>=0 && CbSelection()==-1) 
    	{
    		String format= MessageFormat.format("{0},{1},{2},{3},{4},Pending,Supplier,Checking\n",IdTxtbx.getText(),ItemsNameTxt.getText(),QtyTxt.getText(),Pricetxt.getText(),PMtxt.getText());
    		
    		
    		Standard_PO edt= new Standard_PO(selectedIndex,format);
    		edt.EditFunc();
    		
    		if(edt.checkingFunc()) 
    		{
    			Alert alert= new Alert(AlertType.INFORMATION);
    			alert.setTitle("Edit");
    			alert.setContentText("Edit Sucessfull, Please Refresh Your Data");
    			alert.showAndWait();
    			ViewPO.getSelectionModel().clearSelection();
    			CleanTextbox(IdTxtbx,ItemsNameTxt,Pricetxt,QtyTxt);
    			PRidCb.getSelectionModel().select(-1);
    		}
    		else 
    		{
    			Alert alert= new Alert(AlertType.ERROR);
    			alert.setTitle("Edit");
    			alert.setContentText("Edit Failed or Please select a row and Do not Choose PRID selection, Make Sure PO status is NOT Approve");
    			alert.showAndWait();
    		}
    	}
    }
    
    @FXML
    public void RefClick(MouseEvent event) throws IOException {
    	PRidCb.getSelectionModel().select(-1);
    	PRidCb.getItems().clear();
    	CleanTextbox(IdTxtbx,ItemsNameTxt,Pricetxt,QtyTxt);
    	load();
    	
    }


    
    @FXML
    public void saveClick(MouseEvent event) {
    	int SelectionIndex= PRidCb.getSelectionModel().getSelectedIndex();
    	Standard_PO order= new Standard_PO(SelectionIndex);    	
    	order.SaveFunc();
    	if(order.checkingFunc()) 
    	{
    		
    		
    		Alert alert= new Alert(AlertType.INFORMATION);
    		alert.setTitle("Saving");
    		alert.setContentText(order.LineCount()+"row(s) has been saved !");
    		alert.showAndWait();
    		ViewPO.getSelectionModel().clearSelection();
    		PRidCb.getSelectionModel().select(-1);
    		
    	}
    	else 
    	{
    		Alert alert= new Alert(AlertType.WARNING);
    		alert.setTitle("Saving");
    		alert.setContentText("Error Occur or No Changes Made! Please Make Sure the PO Status is not Approve");
    		alert.showAndWait();
    	}
    }
    

    @FXML
    public void CBoxAction(ActionEvent event) throws IOException {
    	Standard_PO Act= new Standard_PO();
    	
    	String Poid= PRidCb.getValue();
    	int SelectionIndex= PRidCb.getSelectionModel().getSelectedIndex();
    	
    	if(SelectionIndex!=-1) 
    	{
    		String[] ItemsId=Act.RetriveItemsID(SelectionIndex).toString().split(",");
    		String[] itemsChecking= Act.RetriveItems(Poid, ItemsId[1]).toString().split(",");
    		
    		
    			QtyTxt.setText(ItemsId[2]);
    			ItemsNameTxt.setText(itemsChecking[0]);
    			ItemsNameTxt.setEditable(false);
    			IdTxtbx.setText("AUTO");
    	    	IdTxtbx.setEditable(false);
    	}
    	else 
    	{
    		ItemsNameTxt.setEditable(true);
    	}
    }
    
    public void PrintClick(MouseEvent event) {
        Stage stage = (Stage) PrintBtn.getScene().getWindow();
        Standard_PO data= ViewPO.getSelectionModel().getSelectedItem();

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                try {
                    PdfGenerator generator = new PdfGenerator();

                    // Generate Report PDF can put on background, not involve in UI Threads
                    PDDocument doc = generator.GeneratePurchaseOrder(List.of(data),"S001",data.getPm());

                    // Need Run on the UI Thread FileChooser Save + Alert + open
                    Platform.runLater(() -> {
                        try {
                            generator.savePdfWithChooser(doc, stage,"Purchase_Order.pdf","Save Purchase Order");  //  FileChooser and Alert
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
