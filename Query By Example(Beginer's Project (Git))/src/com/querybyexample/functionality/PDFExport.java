package com.querybyexample.functionality;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Vector;

import javax.swing.JFileChooser;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class PDFExport {
	PdfWriter writer = null;
/*	
	public PDFExport() throws SecurityException, IOException {
		// TODO Auto-generated constructor stub
		Logger log =  UtilitiesQBE.getLogger(PDFExport.class);
	}*/
	
	protected void defaultExportToPDF(QueryData queryData){
		if(queryData.data==null || queryData.QBECols==null){
			throw new NullPointerException("Nothing to export");
		}else{
			JFileChooser fc = new JFileChooser(); 
			fc.setName("FileChooser");
			 Calendar calendar = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("MM-DD-YYYY HH-mm-ss"); 
				String date = sdf.format(calendar.getTime());
				 fc.setSelectedFile(new java.io.File(date+".pdf"));
				 fc.getCurrentDirectory().setExecutable(true);
				 fc.getCurrentDirectory().setWritable(true);
				 Document document = new Document();
					document.setPageSize(PageSize.A4.rotate());
					 int op = fc.showSaveDialog(null);
					if(op ==JFileChooser.APPROVE_OPTION){
						try{
							String path = fc.getCurrentDirectory().getAbsolutePath();				
		                     String name= fc.getSelectedFile().getName();
		                    writer = PdfWriter.getInstance(document, new FileOutputStream(path+"\\"+name));
		                    
		             		document.open();
		             		
		             		Paragraph p1 = new Paragraph("Query by Example Table Export\n\n");
		             		//document.addTitle("test");
		             		p1.setAlignment(Element.ALIGN_CENTER);
		             		
		             		p1.setSpacingBefore(50.2f);
		             		//document.add(Info);
		             		document.add(p1);
		             		int count=queryData.length;
		             		PdfPTable tab=new PdfPTable(count);
		             		
		             		for(int i=0;i<count;i++){
		             			
		             			tab.addCell(queryData.getQBECols().get(i).toString());
		             		}
		             		int data = queryData.getData().size();
		             		Vector<Object> row =  new Vector<>();
		             		for(int i=0;i<data;i++){
		             			
		             			row =  queryData.getData();
		             			for(int y =0;y<count;y++){
		             			Object obj1 = row.get(y);
		             			if(obj1!=null){
		             			String value1=obj1.toString();
		             
		             			tab.addCell(value1);
		             			}
		             			}
		             		}
		             		document.add(tab);
		             		document.close();
		             		
		             		
						}catch(IOException | DocumentException e){
							e.printStackTrace();
						}
					}
				
		}
		
	}
	
	
	

}
