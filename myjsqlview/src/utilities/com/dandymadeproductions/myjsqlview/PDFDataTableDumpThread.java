//=================================================================
//               MyJSQLView PDFDataTableDumpThread
//=================================================================
//
//    This class provides a thread to safely dump a TableTabPanel
// summary table data to a local pdf file.
//
//                << PDFDataTableDumpThread.java >>
//
//=================================================================
// Copyright (C) 2007-2010 Dana M. Proctor
// Version 1.0 06/10/2010
//
// This program is free software; you can redistribute it and/or
// modify it under the terms of the GNU General Public License
// as published by the Free Software Foundation; either version
// 2 of the License, or (at your option) any later version. This
// program is distributed in the hope that it will be useful, 
// but WITHOUT ANY WARRANTY; without even the implied warranty
// of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
// the GNU General Public License for more details. You should
// have received a copy of the GNU General Public License along
// with this program; if not, write to the Free Software Foundation,
// Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
// (http://opensource.org)
//
//=================================================================
// Revision History
// Changes to the code should be documented here and reflected
// in the present version number. Author information should
// also be included with the original copyright author.
//=================================================================
// Version 1.0 Original PDFDataTableDumpThread Class.
//             
//-----------------------------------------------------------------
//                    danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import javax.swing.JTable;

import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.PdfPageEvent;

/**
 *    The DataTableDumpThread class provides a thread to safely
 * dump a TableTabPanel summary table data to a local pdf file.
 * 
 * @author Dana M. Proctor
 * @version 1.0 06/10/2010
 */

class PDFDataTableDumpThread implements PdfPageEvent, Runnable
{
   // Class Instances
   Thread t;
   private JTable summaryListTable;
   private HashMap<String, String> tableColumnTypeHashMap;
   private String exportedTable, fileName;
   private PdfTemplate pdfTemplate;

   private static final Font ROW_HEADER_FONT = new Font(Font.FontFamily.UNDEFINED, (float) 12, Font.BOLD);
   private static final Font FONT = new Font();
   private static final BaseFont ROW_HEADER_BASE_FONT = ROW_HEADER_FONT.getCalculatedBaseFont(false);
   private static final BaseFont BASE_FONT = FONT.getCalculatedBaseFont(false);

   //==============================================================
   // PDFDataDumpThread Constructor.
   //==============================================================

   PDFDataTableDumpThread(JTable summaryListTable, HashMap<String, String> tableColumnTypeHashMap,
                          String exportedTable, String fileName)
   {
      this.summaryListTable = summaryListTable;
      this.tableColumnTypeHashMap = tableColumnTypeHashMap;
      this.exportedTable = exportedTable;
      this.fileName = fileName;

      // Create and start the class thread.
      t = new Thread(this, "PDFDataTableDumpThread");
      // System.out.println("Data Dumb Thread");

      t.start();
   }

   //==============================================================
   // Class Method for Normal Start of the Thread.
   //==============================================================

   public void run()
   {
      // Class Method Instances
      PdfPTable pdfTable;
      Document pdfDocument;
      PdfWriter pdfWriter;
      ByteArrayOutputStream byteArrayOutputStream;
      
      int columnCount, rowNumber;
      int[] columnWidths;
      int totalWidth;
      Rectangle pageSize;

      MyJSQLView_ProgressBar dumpProgressBar;
      HashMap<String, String> summaryListTableNameTypes;
      String currentTableFieldName;
      String currentType, currentString;

      // Setup
      columnCount = summaryListTable.getColumnCount();
      rowNumber = summaryListTable.getRowCount();
      columnWidths = new int[columnCount];

      pdfTable = new PdfPTable(columnCount);
      pdfTable.setWidthPercentage(100);
      pdfTable.getDefaultCell().setPaddingBottom(4);

      summaryListTableNameTypes = new HashMap<String, String>();

      // Constructing progress bar.
      rowNumber = summaryListTable.getRowCount();
      dumpProgressBar = new MyJSQLView_ProgressBar(exportedTable + " Dump");
      dumpProgressBar.setTaskLength(rowNumber);
      dumpProgressBar.pack();
      dumpProgressBar.center();
      dumpProgressBar.setVisible(true);

      // Create Row Header
      pdfTable.getDefaultCell().setBorderWidth(2);

      for (int i = 0; i < columnCount; i++)
      {
         currentTableFieldName = summaryListTable.getColumnName(i);
         pdfTable.addCell(new Phrase(currentTableFieldName, ROW_HEADER_FONT));
         columnWidths[i] = Math.min(50000, Math.max(columnWidths[i],
                                    ROW_HEADER_BASE_FONT.getWidth(currentTableFieldName + " ")));
         summaryListTableNameTypes.put(Integer.toString(i),
                                       tableColumnTypeHashMap.get(currentTableFieldName));
      }

      pdfTable.getDefaultCell().setBorderWidth(1);
      pdfTable.setHeaderRows(1);

      // Create the Body of Data.
      int i = 0;
      while ((i < rowNumber) && !dumpProgressBar.isCanceled())
      {
         dumpProgressBar.setCurrentValue(i);

         // Collecting rows of data & formatting date & timestamps
         // as needed according to the Export Properties.

         if (summaryListTable.getValueAt(i, 0) != null)
         {
            for (int j = 0; j < summaryListTable.getColumnCount(); j++)
            {
               currentString = summaryListTable.getValueAt(i, j) + "";
               currentString = currentString.replaceAll("\n", "");
               currentString = currentString.replaceAll("\r", "");

               // Format Date & Timestamp Fields as Needed.
               currentType = summaryListTableNameTypes.get(Integer.toString(j));

               if ((currentType != null)
                   && (currentType.equals("DATE") || currentType.equals("DATETIME") || currentType
                         .indexOf("TIMESTAMP") != -1))
               {
                  if (!currentString.toLowerCase().equals("null"))
                  {
                     if (currentType.equals("Date"))
                        currentString = MyJSQLView_Utils.formatCSVExportDateString(currentString);
                     else
                     {
                        int firstSpace;
                        String time;

                        // Try to get the time separated before formatting
                        // the date.

                        if (currentString.indexOf(" ") != -1)
                        {
                           firstSpace = currentString.indexOf(" ");
                           time = currentString.substring(firstSpace);
                           currentString = currentString.substring(0, firstSpace);
                        }
                        else
                           time = "";

                        currentString = MyJSQLView_Utils.formatCSVExportDateString(currentString) + time;
                     }
                  }
               }
               PdfPCell cell = new PdfPCell(new Phrase(currentString));
               cell.setPaddingBottom(4);
               // if (o instanceof Number)
               // {
               // cell.setHorizontalAlignment(Cell.ALIGN_RIGHT);
               // }
               pdfTable.addCell(cell);
               columnWidths[j] = Math.min(50000, Math.max(columnWidths[j],
                                          BASE_FONT.getWidth(currentString)));
            }
         }
         i++;
      }
      dumpProgressBar.dispose();
      
      // Check to see if any data was in the summary
      // table to even be saved.
      
      if (pdfTable.size() <= 1)
         return;
      
      // Create a document of the PDF formatted data
      // to be saved to the given input file.
      
      try
      {
         // Sizing
         totalWidth = 0;
         for (int width : columnWidths)
            totalWidth += width;
         pageSize = PageSize.A4.rotate();
         pageSize.setRight(pageSize.getRight() * Math.max(1f, totalWidth / 53000f));
         pageSize.setTop(pageSize.getTop() * Math.max(1f, totalWidth / 53000f));
         
         pdfTable.setWidths(columnWidths);

         // Document
         pdfDocument = new Document(pageSize);
         byteArrayOutputStream = new ByteArrayOutputStream();
         pdfWriter = PdfWriter.getInstance(pdfDocument, byteArrayOutputStream);
         pdfDocument.open();
         pdfTemplate = pdfWriter.getDirectContent().createTemplate(100, 100);
         pdfTemplate.setBoundingBox(new com.itextpdf.text.Rectangle(-20, -20, 100, 100));
         pdfWriter.setPageEvent(this);
         pdfDocument.add(pdfTable);
         pdfDocument.close();

         // Outputting
         WriteDataFile.mainWriteDataString(fileName, byteArrayOutputStream.toByteArray(), false);

      }
      catch (DocumentException de)
      {

      }
   }
   
   //==============================================================
   // class Methods to meet the requirements for the PdfPageEvent
   // requirements.
   //==============================================================
   
   public void onOpenDocument(PdfWriter pdfWriter, Document document){}
   
   public void onCloseDocument(PdfWriter writer, Document document)
   {
      pdfTemplate.beginText();
      pdfTemplate.setFontAndSize(BASE_FONT, 12);
      pdfTemplate.showText("" + (writer.getPageNumber() - 1));
      pdfTemplate.endText();
   }
   
   public void onStartPage(PdfWriter pdfWriter, Document document){}

   public void onEndPage(PdfWriter writer, Document document)
   {
      PdfContentByte cb = writer.getDirectContent();
      String text = "Page " + writer.getPageNumber() + " of ";
      float textSize = BASE_FONT.getWidthPoint(text, 12);
      float textBase = document.bottom() - 20;
      cb.beginText();
      cb.setFontAndSize(BASE_FONT, 12);
      float adjust = BASE_FONT.getWidthPoint("000", 12);
      cb.setTextMatrix(document.right() - textSize - adjust, textBase);
      cb.showText(text);
      cb.endText();
      cb.addTemplate(pdfTemplate, document.right() - adjust, textBase);
   }
   
   public void onParagraph(PdfWriter pdfWriter, Document document, float v){}

   public void onParagraphEnd(PdfWriter pdfWriter, Document document, float v){}

   public void onChapter(PdfWriter pdfWriter, Document document, float v, Paragraph paragraph){}

   public void onChapterEnd(PdfWriter pdfWriter, Document document, float v){}

   public void onSection(PdfWriter pdfWriter, Document document, float v, int i, Paragraph paragraph){}

   public void onSectionEnd(PdfWriter pdfWriter, Document document, float v){}

   public void onGenericTag(PdfWriter pdfWriter, Document document, com.itextpdf.text.Rectangle rectangle,
                            String string){}
}