//=================================================================
//                    MyJSQLView DataDumpThread
//=================================================================
//    This class provides a thread to safely dump database table
// data to a local file. A status dialog with cancel is provided to
// allow the ability to prematurely terminate the dump.
//
//                   << DataDumpThread.java >>
//
//=================================================================
// Copyright (C) 2006-2010 Dana M. Proctor
// Version 5.1 05/15/2010
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
// Version 1.0 Original DataDumpThread Class.
//         1.1 Status Bar with Cancel Implementation.
//         1.2 Implemented Runnable Instead of Extending Thread.
//         1.3 Data Delimiter Obtained from MyJSQLView_Main JMenuBar.
//         1.4 Class Instance rowNumber Implementation.
//         1.5 Updated for MyJSQLView.
//         1.6 Main Class Name Change to MyJSQLView.
//         1.7 Class Instance dumpData, in Place of Object.
//         1.8 Class WriteDataFile Boolean Argument Addition, true.
//         1.9 Class Constructor Arguments, tableColumnTypeHashMap &
//             tableColumnClassHashMap. Filtering of Blob From Text
//             Output.
//         2.0 Class Instance dumpData Back to Object.
//         2.1 Instance Reorganizing.
//         2.2 Comment Changes & Disposing of dumpProgressBar on
//             SQLExceptions.
//         2.3 Class Method run() Instance dataDelimiter.
//         2.4 Added TEXT, MEDIUMTEXT, & LONGTEXT Option Output
//             As Defined in Preferences Data Export in run().
//             Added Class Instance tableColumnSizeHashMap, Argument.
//         2.5 Insured NULL Placed for Fields As Needed & Cleanup
//             of Text Fields Some, "\n" & "\r" Removed.
//         2.6 Class Instance dumpProgressBar Set pack() and 
//             setVisible(true).
//         2.7 Code Cleanup.
//         2.8 Cleaned Up Javadoc Comments.
//         2.9 Header Update.
//         3.0 Added/Implemented identiferQuoteString.
//         3.1 MyJSQLView.getDataExportProperties().
//         3.2 Output Non-Modified Field Names. Same As Table Name.
//             Modified rowCount Method to SELECT COUNT(*). Inserted
//             identifierQuoteString. Trimmed Default Content.
//         3.3 Deliminator to Delimiter.
//         3.4 ColumnType Binary Exclusion, mysqlStatement Changed to
//             sqlStatement, and rs Changed to dbResultSet, All in
//             Class Method run().
//         3.5 Converted MySQL Exported Data Bit Fields to Base 2.
//         3.6 Chopped MySQL Year Fields to Only Four Digits on Export.
//         3.7 Implemented Fully Qualified Table Name. Added Instance
//             schemaTableName to Class Method run().
//         3.8 Modified to Accomodate Oracle Data Field Types.
//         3.9 MyJSQLView Project Common Source Code Formatting.
//         4.0 Class Method run() getDataExportProperites Changed
//             Over to the MyJSQLView_Frame Class.
//         4.1 Changed MyJSQLView_Frame.getDatabaseExportProperties()
//             Method Moved Over to the DBTablesPanel.
//         4.2 Conditional Check for NULL db_Connection in run().
//         4.3 Formatted Oracle TimestampTZ Data Types For Consistency
//             Throughout the Application With RFC 822 Time Zone in
//             Class Method run().
//         4.4 Formatted All Oracle Date & Timestamp Fields to be Able
//             to Properly Allow Data Imports, Changed to MM-dd-yyyy.
//         4.5 Implemented A Formatting of the Date, DateTime, and Timestamp
//             Fields as Determined by the CSVDataExportPreferencesPanel.
//             Class Method run().
//         4.6 Removed a null Check on the columnType Instance in Class 
//             Method run().
//         4.7 Header Format Changes/Update.
//         4.8 Added Class Method Instance columnNames_String, StringBuffer,
//             to run().
//         4.9 Changed Package to Reflect Dandy Made Productions Code.
//         5.0 Minor Comment Changes and Conditional Check in run() Between
//             dbResultSet.next() and dumpProgressBar.isCanceled() to Short-
//             Circuit &&. Organized imports.
//         5.1 Removed Class Method displayMyDateString(). Part of MyJSQLView_Utils.
//             
//-----------------------------------------------------------------
//                   danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

/**
 *    The DataDumpThread class provides a thread to safely dump
 * database table data to a local file. A status dialog with cancel
 * is provided to allow the ability to prematurely terminate the dump.
 * 
 * @author Dana M. Proctor
 * @version 5.1 05/15/2010
 */

class DataDumpThread implements Runnable
{
   // Class Instances
   Thread t;
   private Vector columnNameFields;
   private HashMap tableColumnNamesHashMap;
   private HashMap tableColumnClassHashMap;
   private HashMap tableColumnTypeHashMap;
   private HashMap tableColumnSizeHashMap;
   private String exportedTable, fileName;

   //==============================================================
   // DataDumpThread Constructor.
   //==============================================================

   DataDumpThread(Vector columnNameFields, HashMap tableColumnNamesHashMap,
                  HashMap tableColumnClassHashMap, HashMap tableColumnTypeHashMap,
                  HashMap tableColumnSizeHashMap, String exportedTable,
                  String fileName)
   {
      this.columnNameFields = columnNameFields;
      this.tableColumnNamesHashMap = tableColumnNamesHashMap;
      this.tableColumnClassHashMap = tableColumnClassHashMap;
      this.tableColumnTypeHashMap = tableColumnTypeHashMap;
      this.tableColumnSizeHashMap = tableColumnSizeHashMap;
      this.exportedTable = exportedTable;
      this.fileName = fileName;

      // Create and start the class thread.
      t = new Thread(this, "DataDumpThread");
      // System.out.println("Data Dumb Thread");

      t.start();
   }

   // Class Method for Normal Start of the Thread
   public void run()
   {
      // Class Method Instances
      Object dumpData;
      MyJSQLView_ProgressBar dumpProgressBar;
      Iterator columnNamesIterator;
      StringBuffer columnNames_String;
      String schemaTableName;
      String columnClass, columnType, dataDelimiter;
      String identifierQuoteString;
      String fieldContent;
      int columnSize, rowNumber, currentRow;

      String sqlStatementString;
      Statement sqlStatement;
      ResultSet dbResultSet;

      // Setting up
      rowNumber = 0;
      dataDelimiter = DBTablesPanel.getDataExportProperties().getDataDelimiter();
      identifierQuoteString = MyJSQLView_Access.getIdentifierQuoteString();

      if (exportedTable.indexOf(".") != -1)
      {
         schemaTableName = identifierQuoteString 
                           + exportedTable.substring(0, exportedTable.indexOf("."))
                           + identifierQuoteString + "." + identifierQuoteString
                           + exportedTable.substring(exportedTable.indexOf(".") + 1)
                           + identifierQuoteString;
      }
      else
         schemaTableName = identifierQuoteString + exportedTable + identifierQuoteString;

      dumpProgressBar = new MyJSQLView_ProgressBar(exportedTable + " Dump");

      // Get Connection to Database.
      Connection db_Connection = MyJSQLView_Access.getConnection("DataDumpThread run()");
      
      if (db_Connection == null)
         return;

      try
      {
         sqlStatement = db_Connection.createStatement();
         sqlStatementString = "SELECT COUNT(*) FROM " + schemaTableName;
         // System.out.println(sqlStatementString);

         dbResultSet = sqlStatement.executeQuery(sqlStatementString);

         // Setting up
         if (dbResultSet.next())
            rowNumber = dbResultSet.getInt(1);

         // Create the table SELECT statement.

         sqlStatementString = "SELECT ";

         columnNames_String = new StringBuffer();
         columnNamesIterator = columnNameFields.iterator();
         
         while (columnNamesIterator.hasNext())
         {
            Object columnNameString = columnNamesIterator.next();

            // Oracle TIMESTAMPLTZ handled differently to remove the
            // need to SET SESSION.

            if (MyJSQLView_Access.getSubProtocol().indexOf("oracle") != -1
                && ((String) tableColumnTypeHashMap.get(columnNameString)).equals("TIMESTAMPLTZ"))
            {
               columnNames_String.append("TO_CHAR(" + identifierQuoteString
                                     + tableColumnNamesHashMap.get(columnNameString) 
                                     + identifierQuoteString
                                     + ", 'MM-DD-YYYY HH24:MM:SS TZR') AS " 
                                     + identifierQuoteString
                                     + tableColumnNamesHashMap.get(columnNameString) 
                                     + identifierQuoteString + ", ");
            }
            else
               columnNames_String.append(identifierQuoteString 
                                     + tableColumnNamesHashMap.get(columnNameString)
                                     + identifierQuoteString + ", ");
         }
         columnNames_String.delete((columnNames_String.length() - 2), columnNames_String.length());
         sqlStatementString += columnNames_String.toString() + " FROM " + schemaTableName;
         //System.out.println(sqlStatementString);

         dbResultSet = sqlStatement.executeQuery(sqlStatementString);

         dumpData = "";
         currentRow = 0;

         // Constructing the column names line.
         columnNamesIterator = columnNameFields.iterator();

         while (columnNamesIterator.hasNext())
            dumpData = (String) dumpData + tableColumnNamesHashMap.get(columnNamesIterator.next())
                       + dataDelimiter;
         dumpData = ((String) dumpData).substring(0, ((String) dumpData).length() - 1) + "\n";

         // Constructing lines of data & progress bar.
         dumpProgressBar.setTaskLength(rowNumber);
         dumpProgressBar.pack();
         dumpProgressBar.center();
         dumpProgressBar.setVisible(true);

         // Collect contents of table field rows and to dump.

         while (dbResultSet.next() && !dumpProgressBar.isCanceled())
         {
            int i = 1;
            dumpProgressBar.setCurrentValue(currentRow);

            columnNamesIterator = columnNameFields.iterator();
            while (columnNamesIterator.hasNext())
            {
               // Filtering out blob & text data as needed.
               Object currentHeading = columnNamesIterator.next();
               columnClass = (String) tableColumnClassHashMap.get(currentHeading);
               columnType = (String) tableColumnTypeHashMap.get(currentHeading);
               columnSize = ((Integer) tableColumnSizeHashMap.get(currentHeading)).intValue();

               fieldContent = dbResultSet.getString(i);

               if (fieldContent != null)
               {
                  // Blob/Bytea data.
 
                  if ((columnClass.indexOf("String") == -1 && columnType.indexOf("BLOB") != -1) ||
                      (columnClass.indexOf("BLOB") != -1 && columnType.indexOf("BLOB") != -1) ||
                      (columnType.indexOf("BYTEA") != -1) || (columnType.indexOf("BINARY") != -1) ||
                      (columnType.indexOf("RAW") != -1))
                  {
                     dumpData = dumpData + "Binary" + dataDelimiter;
                  }

                  // Text, MediumText, LongText, & CLOB.
                  else if ((columnClass.indexOf("String") != -1 && !columnType.equals("CHAR") &&
                            columnSize > 255) ||
                           (columnClass.indexOf("String") != -1 && columnType.equals("LONG")) ||
                           (columnType.indexOf("CLOB") != -1))
                  {
                     // Check to see if a portion of the TEXT data should be
                     // included as defined in the Preferences | Export Data |
                     // CVS.

                     if (DBTablesPanel.getDataExportProperties().getTextInclusion())
                     {
                        int textLength = DBTablesPanel.getDataExportProperties().getTextCharsNumber();

                        // Obtain text and cleanup some
                        fieldContent = fieldContent.replaceAll("\n", "");
                        fieldContent = fieldContent.replaceAll("\r", "");

                        if (fieldContent.length() > textLength)
                           dumpData = dumpData + fieldContent.substring(0, textLength) + dataDelimiter;
                        else
                           dumpData = dumpData + fieldContent + dataDelimiter;
                     }
                     else
                        dumpData = dumpData + "Text" + dataDelimiter;
                  }

                  // Convert MySQL Bit Fields to Such, Since they will
                  // be returned in base 10.
                  else if (MyJSQLView_Access.getSubProtocol().equals("mysql")
                           && columnType.indexOf("BIT") != -1)
                  {
                     try
                     {
                        dumpData = dumpData + Integer.toBinaryString(Integer.parseInt(fieldContent))
                                   + dataDelimiter;
                     }
                     catch (NumberFormatException e)
                     {
                        // Should never happen.
                     }
                  }

                  // Insure MySQL Date/Year fields are chopped to only 4 digits.
                  else if (MyJSQLView_Access.getSubProtocol().equals("mysql")
                           && columnType.indexOf("YEAR") != -1)
                  {
                     String yearString = fieldContent.trim();

                     if (yearString.length() > 4)
                        yearString = yearString.substring(0, 4);

                     dumpData = dumpData + yearString + dataDelimiter;
                  }
                  
                  // Format Date & Timestamp Fields as Needed.
                  else if (columnType.equals("DATE") || columnType.equals("DATETIME") ||
                           columnType.indexOf("TIMESTAMP") != -1)
                  {
                     if (columnType.equals("DATE"))
                     {
                        fieldContent = MyJSQLView_Utils.displayMyDateString(dbResultSet.getDate(i) + "");
                        fieldContent = MyJSQLView_Utils.formatCSVExportDateString(fieldContent);
                     }
                     else
                     {  
                        int firstSpace;
                        String time;
                        
                        if (columnType.equals("DATETIME") || columnType.equals("TIMESTAMP"))
                        {
                           Object dateTime = dbResultSet.getTimestamp(i);
                           fieldContent = (new SimpleDateFormat("MM-dd-yyyy HH:mm:ss")).format(dateTime) + "";  
                        }
                        else if (columnType.equals("TIMESTAMPTZ"))
                        {
                           Object dateTime = dbResultSet.getTimestamp(i);
                           fieldContent = (new SimpleDateFormat("MM-dd-yyyy HH:mm:ss Z")).format(dateTime) + "";  
                        }
                        // TIMESTAMPLTZ, Oracle
                        else
                        {
                           // Do Nothing. Who knows, just comes out in MM-dd-YY with the getString().
                        }
                        // System.out.println(fieldContent);
                          
                        // Try to get the time separated before formatting
                        // the date.
                           
                        if (fieldContent.indexOf(" ") != -1)
                        {
                           firstSpace = fieldContent.indexOf(" ");
                           time = fieldContent.substring(firstSpace);
                           fieldContent = fieldContent.substring(0, firstSpace);
                        }
                        else
                           time = "";
                        
                        fieldContent = MyJSQLView_Utils.formatCSVExportDateString(fieldContent) + time;
                     }
                     dumpData = dumpData + fieldContent + dataDelimiter;  
                  }
                  
                  // All other fields.
                  else
                     dumpData = dumpData + fieldContent.trim() + dataDelimiter;
               }
               else
                  dumpData = dumpData + "NULL" + dataDelimiter;
               
               i++;
            }
            dumpData = ((String) dumpData).substring(0, ((String) dumpData).length() - 1) + "\n";
            currentRow++;
            // System.out.print(currentRow + " " + dumpData);
         }
         WriteDataFile.mainWriteDataString(fileName, dumpData.toString().getBytes(), true);

         dbResultSet.close();
         sqlStatement.close();
         dumpProgressBar.dispose();
         MyJSQLView_Access.closeConnection(db_Connection, "DataDumpThread run()");
      }
      catch (SQLException e)
      {
         dumpProgressBar.dispose();
         MyJSQLView_Access.displaySQLErrors(e, "DataDumpThread run()");
         MyJSQLView_Access.closeConnection(db_Connection, "DataDumpThread run()");
      }
   }
}