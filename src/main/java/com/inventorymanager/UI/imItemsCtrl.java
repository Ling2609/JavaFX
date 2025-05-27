	package com.inventorymanager.UI;

import java.io.IOException;

import com.PM.Sources.PMViewItems;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class imItemsCtrl {
	
	@FXML
    private TableColumn<PMViewItems, String> ItemsID;

    @FXML
    private TableColumn<PMViewItems, String> ItemsName;

    @FXML
    private TableColumn<PMViewItems, Integer> itemsStock;

    @FXML
    private TableColumn<PMViewItems, Double> itemsUP;

    @FXML
    private TableView<PMViewItems> viewItemTable;
    
    
    public void initialize() throws IOException 
    {
    	ItemsID.setCellValueFactory(new PropertyValueFactory<>("id")); // Use the ViewList.getID method
    	ItemsName.setCellValueFactory(new PropertyValueFactory<>("name"));// same as above but the method different
        itemsStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        itemsUP.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        
        load();
    }
    
    public void load() throws IOException 
    {
        PMViewItems listed = new PMViewItems();
        ObservableList<PMViewItems> itemList = FXCollections.observableArrayList(); 
        String[] row = listed.ReadTextFile().toString().split("\n");
        
        for (String rows : row) 
        {
            String[] spl = rows.trim().split(",");
            if (spl.length == 4) 
            {	
                String id = spl[0].trim(); // Remove extra whitespace
                String name = spl[1].trim();
                int stock = Integer.parseInt(spl[2].trim());
                double price = Double.parseDouble(spl[3].trim());

                itemList.add(new PMViewItems(id, name, stock, price));
            }
        }

        viewItemTable.setItems(itemList);
    }

    

}