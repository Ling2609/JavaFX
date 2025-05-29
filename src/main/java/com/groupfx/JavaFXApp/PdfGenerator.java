package com.groupfx.JavaFXApp;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import com.PM.Sources.Standard_PO;
import com.financemanager.source.FMGenReport;
import com.inventorymanager.source.*;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.imageio.ImageIO;

public class PdfGenerator {
	
	
	public PdfGenerator() {}
	
	public PDDocument PrepareReport(List<?> reportData, String year, String ReportName) {
	    PDDocument doc = new PDDocument();
	    PDPage page = new PDPage(PDRectangle.A4);
	    doc.addPage(page);

	    try (PDPageContentStream cs = new PDPageContentStream(doc, page)) {
	        float margin = 50;
	        float y = 750;
	        float lineHeight = 20;

	        // Title
	        cs.beginText();
	        cs.setFont(PDType1Font.HELVETICA_BOLD, 16);
	        cs.newLineAtOffset(200, y);
	        cs.showText("OMEGA WHOLESALE SDN BHD");
	        cs.endText();

	        y -= lineHeight;

	        cs.beginText();
	        cs.setFont(PDType1Font.HELVETICA_BOLD, 14);
	        cs.newLineAtOffset(225, y);
	        cs.showText(ReportName);
	        cs.endText();

	        y -= lineHeight;

	        cs.beginText();
	        cs.setFont(PDType1Font.HELVETICA, 12);
	        cs.newLineAtOffset(230, y);
	        cs.showText("For Year " + year);
	        cs.endText();

	        cs.beginText();
	        cs.setFont(PDType1Font.HELVETICA, 10);
	        cs.newLineAtOffset(450, y);
	        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	        LocalDate date = LocalDate.now();
	        cs.showText("Date Created: " + date.format(format));
	        cs.endText();

	        y -= (lineHeight * 3);

	        boolean headerFMDrawn = false;
	        double totalIncome = 0.0;
	        
	        
	        for (int i = 0; i < reportData.size(); i++) {
	        	
	            Object item = reportData.get(i);
	            
	        	if(item instanceof FMGenReport) {
	        		
	        		if (!headerFMDrawn) {
	        			
	        			//Header
	                    drawLine(cs, margin, y, 500);
	                    drawText(cs, "Item ID", margin, y - 15, true);
	                    drawText(cs, "Item", margin + 150, y - 15, true);
	                    drawText(cs, "Qty", margin + 300, y - 15, true);
	                    drawText(cs, "Total (RM)", margin + 400, y - 15, true);
	                    y -= lineHeight;
	                    drawLine(cs, margin, y, 500);
	                    
	                    headerFMDrawn = true;  // Set flag to true after drawing header
	                }
	        		
	        		// Content
	        		FMGenReport report = (FMGenReport) item;
	        		
	    	        drawText(cs, report.getItemId(), margin, y - 15, false);
	    	        drawText(cs, report.getItemName(), margin + 150, y - 15, false);
	    	        drawText(cs, String.valueOf(report.getQty()), margin + 300, y - 15, false);
	    	        drawText(cs, String.format("%.2f", report.getTotal()), margin + 400, y - 15, false);
	    	        totalIncome += report.getTotal();
	    	        y -= lineHeight;
	    	        
	    	        if(i == reportData.size() - 1) {
	    	        	
	    	        	drawLine(cs, margin, y, 500);
	    		        y -= lineHeight;

	    		        drawText(cs, "Total Paid/Expenses:", margin + 250, y - 15, true);
	    		        drawText(cs, "RM " + String.format("%.2f", totalIncome), margin + 400, y - 15, true);
	    		        
	    		        y-=lineHeight;
	    		        drawText(cs,"All Prices Are Show in Discounted Price", margin,y-100,true);
	    		        drawText(cs, "THIS IS A COMPUTER GENERATED REPORT", margin,y-200,true);

	    	        }
	        	}
	        	
	        	if(item instanceof InventoryM_Stocks) {
	        		
	        		if (!headerFMDrawn) {
	        			
	        			//Header
	                    drawLine(cs, margin, y, 500);
	                    drawText(cs, "Item ID", margin, y - 15, true);
	                    drawText(cs, "Item", margin + 150, y - 15, true);
	                    drawText(cs, "Stock", margin + 300, y - 15, true);
	                    y -= lineHeight;
	                    drawLine(cs, margin, y, 500);
	                    
	                    headerFMDrawn = true;  // Set flag to true after drawing header
	                }
	        		
	        		InventoryM_Stocks report = (InventoryM_Stocks) item;
	        		
	        		drawText(cs, report.getItemStockID(), margin, y - 15, false);
	    	        drawText(cs, report.getItemStockName(), margin + 150, y - 15, false);
	    	        drawText(cs, String.valueOf(report.getItemStock()), margin + 300, y - 15, false);
	    	        y -= lineHeight;
	    	        
	    	        if(i == reportData.size() - 1) {
	    	        	
	    	        	drawLine(cs, margin, y, 500);
	    		        y -= lineHeight;

	    		        drawText(cs, "Report Writter:", margin + 250, y - 15, true);
	    		        drawText(cs, report.getInventoryM_ID(), margin + 400, y - 15, true);

	    	        }
	        	}
	        	
	        }
	        

	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    
	    return doc;
	    // Save
	}
	
	
	public PDDocument GeneratePurchaseOrder(List<Standard_PO> reportData, String supplier, String PMName) {
	    PDDocument doc = new PDDocument();
	    PDPage page = new PDPage(PDRectangle.A4);
	    doc.addPage(page);

	    float margin = 50;
	    float tableWidth = 500;
	    float yStart = 750;
	    float y = yStart;
	    float lineHeight = 20;
	    String itemName="Undefined";
	    String PId="Undefined";
	    String SuppId= "Undefined";
	    Standard_PO source= new Standard_PO();
        String[] SuppData;
        boolean Procced=true;
		try 
		{

	        for(Standard_PO items: reportData) 
	        { 	
	        	
	     	    itemName=items.getName(); // Items Id
	     	  
	     	 
	     	    PId= items.getId(); // PO id
	        }
			
			
			SuppData = source.ReadSupplierAdd(itemName,PId);
			if(SuppData.length<=0) 
		    {
			  
		      Procced=false;
		      throw new Exception("Supplier not found ! Please");
		      
				  
		    }
			
			    
		}
		catch (Exception e) {
			
	    	if(!Procced) {
	    		
			Platform.runLater(()->
	    	{
	    		try 
	    		{	
	    			showAlert("ERROR","Supplier Not Found or the Purchase Order haven't been Approved !");
	    			doc.close();
	    			
	    			
	    		}catch(Exception later){
	    			later.printStackTrace();
	    		}
	    		
	    	});
			}
	    	
	    	
	    }
		
		if(!Procced) 
		{	
			return null;
		}
		
	    
	    try(PDPageContentStream cs = new PDPageContentStream(doc, page)) {

	    	InputStream img= getClass().getResourceAsStream("/img/OMEGA.png");
	    	if(img==null) 
	    	{
	    		throw new IOException("Image Not Found !");
	    	}
	    	BufferedImage image= ImageIO.read(img);
	    	PDImageXObject CreateImg= LosslessFactory.createFromImage(doc, image);
	    	cs.drawImage(CreateImg, 10,y,100,100);
	    	
	    	
	    	// Title 
	        cs.beginText();
	        cs.setFont(PDType1Font.HELVETICA_BOLD, 18);
	        cs.newLineAtOffset(220, y);
	        cs.showText("PURCHASE ORDER");
	        cs.endText();

	        // Date 
	        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	        LocalDate date = LocalDate.now();
	        drawText(cs, "Date Created: " + date.format(format), 400, y-55, false);

	        y -= 40;

	        //  Company Info 
	        drawText(cs, "From:", margin, y, true);
	        drawText(cs, "OMEGA WHOLESALES SDN BHD", margin, y - 15, true);
	        drawText(cs, "15, Jalan Bored Duck Rd, 81000 SKU, JHR", margin, y - 30, false);

	        y -= 60;

	        for(Standard_PO items: reportData) 
	        { 	
	        	
	     	    itemName=items.getName(); // Items Id
	     	  
	     	 
	     	    PId= items.getId(); // PO id
	        }
	        //  Supplier Info 
	      
	        	SuppData = source.ReadSupplierAdd(itemName,PId);
	        	drawText(cs, "To:", margin, y, true);
	         	drawText(cs, SuppData[0], margin, y - 15, true);
	         	drawText(cs, SuppData[1], margin, y - 30, false);
        	
	       
        	
        	drawText(cs, "Purchase Order ID: " + PId, 400, y-15, true);
	        

	        y -= 60;

	        // === Table Header ===
	        drawLine(cs, margin, y, tableWidth);
	        drawTableHeader(cs, margin, y - 15);
	        y -= lineHeight * 2;

	        // === Table Content ===
	        double Subtotal = 0.0;
	        double Total=0.0;
	        for (Standard_PO item : reportData) {
	            drawTableRow(cs, item, margin, y);
	            y -= lineHeight;
	            int Quantity= item.getQuantity();
	            double UnitP=item.UnitPriceR(item.getName());
	            
	            Subtotal += Quantity*UnitP;
	            
	            Total+=item.getPrice();
	        }

	        drawLine(cs, margin, y, tableWidth);
	        y -= lineHeight;

	        // === Total ===
	        drawText(cs, "Subtotal:", margin + 300, y, true);
	        drawText(cs, "RM " + String.format("%.2f", Subtotal), margin + 400, y, false);
	        
	        //discount label
	        y -= lineHeight;
	        drawText(cs,"Discount (RM)", margin+300,y,true);
	        double disN= Subtotal-Total;
	        drawText(cs,"("+disN+")",margin+400,y,false);
	        
	        y -= lineHeight;
	        
	        drawText(cs, "Total:", margin + 300, y, true);
	        drawText(cs, "RM " + String.format("%.2f", Total), margin + 400, y, false);
	        
	        // FOOTER
	        y-=lineHeight*2;
	        drawText(cs,"This Report Is Written and Approved By: ", margin,y,true);
	        
	        drawText(cs, PMName, margin ,y-20,true);
	        
	       y -= lineHeight *2;
	       drawText(cs, "This is a Computer Generated Report", margin, y, true);

	     
	       y-=lineHeight;
	     drawText(cs, "E & O E", margin, y, false);
	     

	    } catch (IOException e) {
	        System.out.println(e.getMessage());
	    } 
	    
			
		

	    return doc;
	}
	private void drawTableHeader(PDPageContentStream cs, float x, float y) throws IOException {
	    drawText(cs, "Item ID", x, y, true);
	    drawText(cs, "Item Name", x + 120, y, true);
	    drawText(cs, "Unit Price (RM)", x+200,y,true);
	    drawText(cs, "Quantity", x + 300, y, true);
	    drawText(cs, "Price (RM)", x + 400, y, true);
	}

	private void drawTableRow(PDPageContentStream cs, Standard_PO item, float x, float y) throws IOException {
	    Standard_PO source= new Standard_PO();
		String[] data= source.RetriveItems(item.getId(), item.getName()).toString().split(",");
		
		String upData= data[1].replace("\n", " ");
        double UnitP=item.UnitPriceR(item.getName());
		
		drawText(cs, item.getName(), x, y, false);
	    drawText(cs, upData, x + 120, y, false);
	    drawText(cs, String.format("%.3f", item.UnitPriceR(item.getName())), x+200, y, false);
	    drawText(cs, String.valueOf(item.getQuantity()), x + 300, y, false);
	    drawText(cs, String.format("%.2f", item.getQuantity()*UnitP), x + 400, y, false);
	}


	public void savePdfWithChooser(PDDocument doc, Stage stage, String filename, String title) throws IOException {
	    FileChooser chooser = new FileChooser();
	    chooser.setTitle(title);
	    chooser.setInitialFileName(filename);
	    chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF File (*.pdf)", "*.pdf"));
	    File file = chooser.showSaveDialog(stage);

	    if (file != null) {
	        doc.save(file);
	        showAlert("Saved!", "File saved at:\n" + file.getAbsolutePath());
	        if (Desktop.isDesktopSupported()) {
	        	new Thread(() -> {
	        		try {
	        			Desktop.getDesktop().open(file);
	        		} catch (Exception e) {
	        			System.out.println(e);
	        		}
	        	}).start();
	        }
	    } else {
	        showAlert("Cancelled", "User cancelled the save operation.");
	    }

	    doc.close();
	}

	private void showAlert(String header, String content) {
	    Alert alert = new Alert(Alert.AlertType.INFORMATION);
	    alert.setHeaderText(header);
	    alert.setContentText(content);
	    alert.show();
	}

	
	
	
	private void drawLine(PDPageContentStream cs, float x, float y, float width) throws IOException {
	    cs.moveTo(x, y);
	    cs.lineTo(x + width, y);
	    cs.stroke();
	}

	private void drawText(PDPageContentStream cs, String text, float x, float y, boolean bold) throws IOException {
	    cs.beginText();
	    cs.setFont(bold ? PDType1Font.HELVETICA_BOLD : PDType1Font.HELVETICA, 11);
	    cs.newLineAtOffset(x, y);
	    cs.showText(text);
	    cs.endText();
	}


	


	}

