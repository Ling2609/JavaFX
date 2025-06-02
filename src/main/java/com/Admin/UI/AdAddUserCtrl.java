package com.Admin.UI;

import java.io.IOException;

import com.Admin.Source.AdminSources;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

public class AdAddUserCtrl {

    @FXML
    private Button DelBtn;

    @FXML
    private TableColumn<AdminSources, String> PasswordTab;

    @FXML
    private Button RefBtn;

    @FXML
    private TableColumn<AdminSources, String> RoleTab;

    @FXML
    private Button SaveBtn;

    @FXML
    private ToggleGroup SelectedRoles;

    @FXML
    private TableColumn<AdminSources, String> UserNameTab;

    @FXML
    private TableView<AdminSources> UserView;

    @FXML
    private RadioButton fmSelected;

    @FXML
    private RadioButton imSelected;

    @FXML
    private TextField password;

    @FXML
    private RadioButton pmSelected;

    @FXML
    private RadioButton smSelected;

    @FXML
    private TextField username;

    
    public void initialize() throws IOException 
    {
    	UserNameTab.setCellValueFactory(new PropertyValueFactory<>("user"));
    	PasswordTab.setCellValueFactory(new PropertyValueFactory<>("password"));
    	RoleTab.setCellValueFactory(new PropertyValueFactory<>("role"));
    	UserView.setSortPolicy(t->false);
    	loadTable();
    	
    }
    
    public void loadTable() throws IOException 
    {
    	AdminSources source= new AdminSources();
    	ObservableList<AdminSources> obList= FXCollections.observableArrayList();
    	String[] data= source.ReadTextFile().toString().split("\n");
    	for(String split:data) 
    	{
    		String[] spl= split.split(",");
    		obList.add(new AdminSources(
    					spl[0],
    					spl[1],
    					spl[2]
    				));
    		
    	}
    	
    	UserView.setItems(obList);
    	
    }
    
    
    @FXML
    public void DelClick(MouseEvent event) throws IOException {
    	int SelectedRow= UserView.getSelectionModel().getSelectedIndex();
    	AdminSources SelectedItem= UserView.getSelectionModel().getSelectedItem();
     
	    if(SelectedItem!=null) 
	    {
	    	String name= SelectedItem.getUser();
	    	AdminSources source= new AdminSources(SelectedRow,name);
	    	if(SelectedRow>=0 && !source.ReadLog()) 
	    	{
	    	  	
	    		source.DeleteFunc();
	    		if(source.Checking()) 
	    		{
	    			AlertInfo("Delete Successful","Delete User",AlertType.INFORMATION);
	    			UserView.getItems().remove(SelectedRow);
	    		}
	    		else 
	    		{
	    			AlertInfo("Delete Unsucess","Delete User",AlertType.ERROR);
	    		}
	    	}else 
	    	{
	    		AlertInfo("Please Select an Row to Delete/n Or Cannot Delete Current User !","Delete User",AlertType.WARNING);
	    	}
	    }else 
	    {
	    	AlertInfo("Please Select an Row to Delete !","Delete User",AlertType.WARNING);
	    }
    }

    @FXML
    public void RefClick(MouseEvent event) throws IOException {
    	ClearTextAndRb(username,password);
    	loadTable();
    }

    @FXML
    public void SelectedRows(MouseEvent event) {

    }
    
 

    @FXML
    public void saveClick(MouseEvent event) throws IOException {
    	RadioButton selectedRadio= (RadioButton) SelectedRoles.getSelectedToggle();
    	if(SelectedRoles.getSelectedToggle()!=null && !CheckEmptyOrNull(username,password)) 
    	{	
    		String role= selectedRadio.getText();
    		AdminSources source= new AdminSources(username.getText(),password.getText(),role);
    		
    		if(!source.CheckSameUsername(username.getText()))	
    		{
	    		source.AddFunc();
		    	if(source.Checking()) 
		    	{
		    		AlertInfo("Add User Successful","Add User",AlertType.INFORMATION);
		    		ClearTextAndRb(username,password);
		    	
		    	}
		    	else 
		    	{
		    		AlertInfo("Add User Unsuccessful","Add User",AlertType.ERROR);
		    		ClearTextAndRb(username,password);
		    	}
		    }else 
		    {
		    	AlertInfo("Username Repeated, Please try again !","Add User",AlertType.WARNING);
	    		ClearTextAndRb(username,password);
		    }
    	}else 
    	{
    		AlertInfo("Please Select an Role and Fill All the Blanks !","Add User",AlertType.WARNING);
    		ClearTextAndRb(username,password);
    	}
    }
    
    public void AlertInfo(String content, String header, AlertType type) 
    {
    	Alert alert= new Alert(type);
		alert.setTitle(header);
		alert.setContentText(content);
		alert.showAndWait();
    }
    
    public boolean CheckEmptyOrNull(TextField...fields) 
    {
    	
    	for(TextField f:fields) 
    	{
    		String content=f.getText();
    		if(content.trim().isEmpty()) {return true;}
    	}
    	return false;
    }
    
    public void ClearTextAndRb(TextField...fields) 
    {
    	for(TextField f: fields) 
    	{
    		f.clear();
    		SelectedRoles.selectToggle(null);
    	}
    }

}
