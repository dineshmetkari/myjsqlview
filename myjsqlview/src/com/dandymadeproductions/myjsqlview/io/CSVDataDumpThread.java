//=================================================================
//                   MyJSQLView CSVDataDumpThread
//=================================================================
//    This class provides a thread to safely dump database table
// data to a local file. A status dialog with cancel is provided to
// allow the ability to prematurely terminate the dump.
//
//                   << CSVDataDumpThread.java >>
//
//=================================================================
// Copyright (C) 2005-2013 Dana M. Proctor
// Version 6.18 07/04/2013
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
//         2.2 Comment Changes & Disposing of dumpProgressBar on SQLExceptions.
//         2.3 Class Method run() Instance dataDelimiter.
//         2.4 Added TEXT, MEDIUMTEXT, & LONGTEXT Option Output As Defined
//             in Preferences Data Export in run(). Added Class Instance
//             tableColumnSizeHashMap, Argument.
//         2.5 Insured NULL Placed for Fields As Needed & Cleanup of Text
//             Fields Some, "\n" & "\r" Removed.
//         2.6 Class Instance dumpProgressBar Set pack() and setVisible(true).
//         2.7 Code Cleanup.
//         2.8 Cleaned Up Javadoc Comments.
//         2.9 Header Update.
//         3.0 Added/Implemented identiferQuoteString.
//         3.1 MyJSQLView.getDataExportProperties().
//         3.2 Output Non-Modified Field Names. Same As Table Name. Modified
//             rowCount Method to SELECT COUNT(*). Inserted identifierQuoteString.
//             Trimmed Default Content.
//         3.3 Deliminator to Delimiter.
//         3.4 ColumnType Binary Exclusion, mysqlStatement Changed to sqlStatement,
//             and rs Changed to dbResultSet, All in Class Method run().
//         3.5 Converted MySQL Exported Data Bit Fields to Base 2.
//         3.6 Chopped MySQL Year Fields to Only Four Digits on Export.
//         3.7 Implemented Fully Qualified Table Name. Added Instance
//             schemaTableName to Class Method run().
//         3.8 Modified to Accomodate Oracle Data Field Types.
//         3.9 MyJSQLView Project Common Source Code Formatting.
//         4.0 Class Method run() getDataExportProperites Changed Over to
//             the MyJSQLView_Frame Class.
//         4.1 Changed MyJSQLView_Frame.getDatabaseExportProperties() Method
//             Moved Over to the DBTablesPanel.
//         4.2 Conditional Check for NULL db_Connection in run().
//         4.3 Formatted Oracle TimestampTZ Data Types For Consistency
//             Throughout the Application With RFC 822 Time Zone in
//             Class Method run().
//         4.4 Formatted All Oracle Date & Timestamp Fields to be Able to
//             Properly Allow Data Imports, Changed to MM-dd-yyyy.
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
//         5.2 Minor Comment Changes.
//         5.3 Parameterized Class Instances tableColumnNamesHashMap, columnNamesFields,
//             tableColumnTypeHashMap, & tableColumnSizeHashmap in Order to
//             Bring Code Into Compliance With Java 5.0 API.
//         5.4 Parameterized columnNamesIterator in Constructor.
//         5.5 Collected Class Istance schemaTableName in run() From MyJSQLView_Utils
//             Method getSchemaTableName().
//         5.6 Class Method run() Change in MyJSQLView_Utils.formatCSVExportDateString()
//             to formatExportDateString().
//         5.7 Class Method run() Changed the Call to Convert Date, DateTime,
//             and Timestamp Content to the Selected CSV Export Format. Also for
//             Oracle Insured Returning the Standard SQL Database Format of
//             YYYY-MM-DD.
//         5.8 Class Method run() Cast Object Returned by MyJSQLView_Access.
//             getConnection() to Connection.
//         5.9 Changes to Class Method run() Used Newly Redefined ConnectionManager
//             to Collect Connections & Display SQL Errors. Also identifierQuoteString
//             Collected From ConnectionManager. Added Method Instance subProtocol
//             in run().
//         6.0 Change in dataDump Clipping by the dataDelimiter.length in run().
//         6.1 Class Method Addition of Proper Processing for Oracle timestampLTZ.
//         6.2 Replaced Method Instance subProtocol in run() With dataSourceType.
//         6.3 Replacement of Multiple dbResultSet Retrieval for a Single Column
//             to Only One for Each Row in Data Collection in run(). Limitation
//             for Some Databases, MSAccess, to Limiting Only One Call to ResultSet.
//         6.4 Copyright Update.
//         6.5 Removed the Casting of (Connection) for the Returned Instance for the
//             ConnectionManager.getConnection() in run().
//         6.6 Introduced binaryContent in run() to Handle Binary Types to getBytes()
//             Instead of getString() & Conversion For columnClass.toUpperCase() for
//             Blob Checks Because HSQL 2.0 is Uppercase for That Data Type.
//         6.7 Exclusion of Array Types From Date, DateTime Conversions in run().
//         6.8 Rebuilt to Limit Result Set Selection of Data to Control Memory Usage.
//             Added Class Method dumpChunkOfData(), Class Instances limitIncrement
//             & fileBuff. Added Instances in run() oracleColumnNamesString, field,
//             firstField, & currentTableIncrement. Major Changes to run() to Achieve
//             New Way of Handling Data.
//         6.9 Obtained limitIncrement From GeneralProperties in Constructor.
//        6.10 Moved Class Instance limitIncrement to Method run().
//        6.11 Class Instance columnNameFields & Same in Constructor Argument Data
//             Type Change from Vector to ArrayList.
//        6.12 Class Method run() Closing of dbResultSet & sqlStatement in finally.
//        6.13 Changed Package Name to com.dandymadeproductions.myjsqlview.io.
//             Made Class, & Constructor Public.
//        6.14 Removal of Starting the Class's Runnable Thread in the Constructor.
//        6.15 Changes in run() to Handle Derby LIMIT Implementation & Exclusion for
//             Same BIT DATA Types.
//        6.16 Renamed From DataDumpThread to CSVDataDumpThread. Updated Comments.
//        6.17 Change in run() to Use DBTablePanel.getGeneralDBProperties().
//        6.18 Added Class Instance limits and Argument of Such in Constructor.
//             Implemented With the Use of limits in run() the Control of SQL
//             Statement of Either Exporting Complete Table or Summary Table. 
//             
//-----------------------------------------------------------------
//                   danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview.io;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JOptionPane;

import com.dandymadeproductions.myjsqlview.datasource.ConnectionManager;
import com.dandymadeproductions.myjsqlview.gui.panels.DBTablesPanel;
import com.dandymadeproductions.myjsqlview.utilities.MyJSQLView_ProgressBar;
import com.dandymadeproductions.myjsqlview.utilities.MyJSQLView_Utils;

/**
 *    The CSVDataDumpThread class provides a thread to safely dump
 * database table data to a local file. A status dialog with cancel
 * is provided to allow the ability to prematurely terminate the dump.
 * 
 * @author Dana M. Proctor
 * @version 6.18 07/04/2013
 */

public class CSVDataDumpThread implements Runnable
{
   // Class Instances
   private String exportedTable, fileName;
   private boolean limits;
   
   private ArrayList<String> columnNameFields;
   private HashMap<String, String> tableColumnNamesHashMap;
   private HashMap<String, String> tableColumnClassHashMap;
   private HashMap<String, String> tableColumnTypeHashMap;
   private HashMap<String, Integer> tableColumnSizeHashMap;
   
   private BufferedOutputStream filebuff;

   //==============================================================
   // CSVDataDumpThread Constructor.
   //==============================================================

   public CSVDataDumpThread(ArrayList<String> columnNameFields, HashMap<String,
                            String> tableColumnNamesHashMap, boolean limits,
                            HashMap<String, String> tableColumnClassHashMap,
                            HashMap<String, String> tableColumnTypeHashMap,
                            HashMap<String, Integer> tableColumnSizeHashMap,
                            String exportedTable, String fileName)
   {
      this.columnNameFields = columnNameFields;
      this.tableColumnNamesHashMap = tableColumnNamesHashMap;
      this.limits = limits;
      this.tableColumnClassHashMap = tableColumnClassHashMap;
      this.tableColumnTypeHashMap = tableColumnTypeHashMap;
      this.tableColumnSizeHashMap = tableColumnSizeHashMap;
      this.exportedTable = exportedTable;
      this.fileName = fileName;
   }

   //==============================================================
   // Class method for normal start of the thread
   //==============================================================
   
   public void run()
   {
      // Class Method Instances
      Object dumpData;
      FileOutputStream fileStream;
      MyJSQLView_ProgressBar dumpProgressBar;
      Iterator<String> columnNamesIterator;
      StringBuffer columnNamesString;
      StringBuffer oracleColumnNamesString;
      String dataSourceType, schemaTableName;
      String field, firstField, columnClass, columnType, dataDelimiter;
      String identifierQuoteString;
      String fieldContent;
      int columnSize, rowsCount, currentTableIncrement, currentRow;
      int limitIncrement;

      String sqlStatementString;
      Statement sqlStatement;
      ResultSet dbResultSet;

      // Setting up
      rowsCount = 0;
      dataDelimiter = DBTablesPanel.getDataExportProperties().getDataDelimiter();
      limitIncrement = DBTablesPanel.getGeneralDBProperties().getLimitIncrement();
      identifierQuoteString = ConnectionManager.getIdentifierQuoteString();
      dataSourceType = ConnectionManager.getDataSourceType();
      schemaTableName = MyJSQLView_Utils.getSchemaTableName(exportedTable);
      dumpProgressBar = new MyJSQLView_ProgressBar(exportedTable + " Dump");

      // Get Connection to Database.
      Connection db_Connection = ConnectionManager.getConnection("CSVDataDumpThread run()");
      
      if (db_Connection == null)
         return;
      
      // Setting up OutputStream
      try
      {
         File makeNewFile = new File(fileName);
         if (makeNewFile.exists())
         {
            try
            {
               makeNewFile.delete();
            }
            catch (SecurityException se){}
         }
         fileStream = new FileOutputStream(fileName, true);
         filebuff = new BufferedOutputStream(fileStream);
      }
      catch (IOException e)
      {
         String msg = "Unable to Create filestream for: '" + fileName + "'.";
         JOptionPane.showMessageDialog(null, msg, fileName, JOptionPane.ERROR_MESSAGE);
         return;
      }
      
      // Collect the column names.
      columnNamesString = new StringBuffer();
      oracleColumnNamesString = new StringBuffer();
      columnNamesIterator = columnNameFields.iterator();
      
      while (columnNamesIterator.hasNext())
      {
         field = columnNamesIterator.next();

         // Oracle TIMESTAMPLTZ handled differently to remove the
         // need to SET SESSION.

         if (dataSourceType.equals(ConnectionManager.ORACLE)
             && (tableColumnTypeHashMap.get(field)).equals("TIMESTAMPLTZ"))
         {
            oracleColumnNamesString.append("TO_CHAR(" + identifierQuoteString
                                           + tableColumnNamesHashMap.get(field) 
                                           + identifierQuoteString
                                           + ", 'YYYY-MM-DD HH24:MM:SS TZR') AS " 
                                           + identifierQuoteString
                                           + tableColumnNamesHashMap.get(field) 
                                           + identifierQuoteString + ", ");
         }
         else
            oracleColumnNamesString.append(identifierQuoteString 
                                           + tableColumnNamesHashMap.get(field)
                                           + identifierQuoteString + ", ");
         
         // Unmodified Names.
         columnNamesString.append(identifierQuoteString + tableColumnNamesHashMap.get(field)
                                  + identifierQuoteString + ", ");
      }
      oracleColumnNamesString.delete((oracleColumnNamesString.length() - 2),
         oracleColumnNamesString.length());
      columnNamesString.delete((columnNamesString.length() - 2), columnNamesString.length());
      firstField = columnNamesString.substring(0, columnNamesString.indexOf(","));
      
      // Have a connection, file to write to and columns so begin
      // dumping data.
      
      sqlStatement = null;
      dbResultSet = null;
      
      try
      {
         sqlStatement = db_Connection.createStatement();
         
         // Collect the row count of the table and setting
         // up a progress bar for tracking/canceling.
         
         if (limits)
            rowsCount = DBTablesPanel.getSelectedTableTabPanel().getValidDataRowCount();
         {
            sqlStatementString = "SELECT COUNT(*) FROM " + schemaTableName;
            // System.out.println(sqlStatementString);

            dbResultSet = sqlStatement.executeQuery(sqlStatementString);

            if (dbResultSet.next())
               rowsCount = dbResultSet.getInt(1);
         }

         dumpProgressBar.setTaskLength(rowsCount);
         dumpProgressBar.pack();
         dumpProgressBar.center();
         dumpProgressBar.setVisible(true);
         
         // Begin Dumping Data.
         
         dumpData = "";
         
         // Constructing the column names line & dumping.
         columnNamesIterator = columnNameFields.iterator();

         while (columnNamesIterator.hasNext())
            dumpData = (String) dumpData + tableColumnNamesHashMap.get(columnNamesIterator.next())
                       + dataDelimiter;
         dumpData = ((String) dumpData).substring(0,
                           ((String) dumpData).length() - dataDelimiter.length()) + "\n";
         
         dumpChunkOfData(dumpData);
         dumpData = "";
         
         // Setting up to begin actual field value dump.
         currentTableIncrement = 0;
         currentRow = 0;
         
         do
         {
            // Creating the Select statement to retrieve data. If not using
            // limit then Oracle needs special handling for Timestamps with
            // Time Zone.
            
            if (limits)
               sqlStatementString = DBTablesPanel.getSelectedTableTabPanel().getTableSQLStatement();
            else
            {
               if (dataSourceType.equals(ConnectionManager.ORACLE))
                  sqlStatementString = "SELECT " + columnNamesString.toString() + " FROM "
                                       + "(SELECT ROW_NUMBER() OVER (ORDER BY " + firstField + " ASC) " 
                                       + "AS dmprownumber, " + oracleColumnNamesString.toString() + " "
                                       + "FROM " + schemaTableName + ") " + "WHERE dmprownumber BETWEEN "
                                       + (currentTableIncrement + 1) + " AND " + (currentTableIncrement
                                       + limitIncrement);
               // MSAccess
               else if (dataSourceType.equals(ConnectionManager.MSACCESS))
                  sqlStatementString = "SELECT " + columnNamesString.toString() + " FROM "
                                        + schemaTableName;
               // Derby
               else if (dataSourceType.equals(ConnectionManager.DERBY))
                  sqlStatementString = "SELECT " + columnNamesString.toString() + " FROM "
                                       + schemaTableName + " OFFSET " + currentTableIncrement + " ROWS "
                                       + "FETCH NEXT " + limitIncrement + " ROWS ONLY";
               else
                  sqlStatementString = "SELECT " + columnNamesString.toString() + " FROM "
                                       + schemaTableName + " LIMIT " + limitIncrement + " OFFSET "
                                       + currentTableIncrement;
            }
            // System.out.println(sqlStatementString);

            dbResultSet = sqlStatement.executeQuery(sqlStatementString);
            
            // Actual data dump.
            while (dbResultSet.next() && !dumpProgressBar.isCanceled())
            {
               int i = 1;
               dumpProgressBar.setCurrentValue(currentRow++);

               columnNamesIterator = columnNameFields.iterator();
               
               while (columnNamesIterator.hasNext())
               {
                  // Filtering out blob & text data as needed.
                  String currentHeading = columnNamesIterator.next();
                  columnClass = tableColumnClassHashMap.get(currentHeading);
                  columnType = tableColumnTypeHashMap.get(currentHeading);
                  columnSize = (tableColumnSizeHashMap.get(currentHeading)).intValue();

                  // Blob/Bytea/Binary/Bit Data/Raw data.
                  
                  if ((columnClass.indexOf("String") == -1 && columnType.indexOf("BLOB") != -1) ||
                      (columnClass.toUpperCase().indexOf("BLOB") != -1 && columnType.indexOf("BLOB") != -1) ||
                      (columnType.indexOf("BYTEA") != -1) || (columnType.indexOf("BINARY") != -1) ||
                      (columnType.indexOf("BIT DATA") != -1) || (columnType.indexOf("RAW") != -1))
                  {
                     Object binaryContent = dbResultSet.getBytes(i);
                     
                     if (binaryContent != null)
                        dumpData = dumpData + "Binary" + dataDelimiter;
                     else
                        dumpData = dumpData + "NULL" + dataDelimiter;
                  }

                  // Text, MediumText, LongText, & CLOB.
                  else if ((columnClass.indexOf("String") != -1 && !columnType.equals("CHAR") &&
                            columnSize > 255) ||
                           (columnClass.indexOf("String") != -1 && columnType.equals("LONG")) ||
                           (columnType.indexOf("CLOB") != -1))
                  {
                     fieldContent = dbResultSet.getString(i);
                     
                     // Check to see if a portion of the TEXT data should be
                     // included as defined in the Preferences | Export Data |
                     // CVS.
                     
                     if (fieldContent != null)
                     {
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
                     else
                        dumpData = dumpData + "NULL" + dataDelimiter;
                  }

                  // Convert MySQL Bit Fields to Such, Since they will
                  // be returned in base 10.
                  else if (dataSourceType.equals(ConnectionManager.MYSQL)
                           && columnType.indexOf("BIT") != -1)
                  {
                     fieldContent = dbResultSet.getString(i);
                     
                     if (fieldContent != null)
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
                     else
                        dumpData = dumpData + "NULL" + dataDelimiter;
                  }

                  // Insure MySQL Date/Year fields are chopped to only 4 digits.
                  else if (dataSourceType.equals(ConnectionManager.MYSQL)
                           && columnType.indexOf("YEAR") != -1)
                  {
                     fieldContent = dbResultSet.getString(i);
                     
                     if (fieldContent != null)
                     {
                        String yearString = fieldContent.trim();

                        if (yearString.length() > 4)
                           yearString = yearString.substring(0, 4);

                        dumpData = dumpData + yearString + dataDelimiter;
                     }
                     else
                        dumpData = dumpData + "NULL" + dataDelimiter;
                  }
                  
                  // Format Date & Timestamp Fields as Needed.
                  else if (columnType.equals("DATE") || columnType.equals("DATETIME")
                           || (columnType.indexOf("TIMESTAMP") != -1 && columnClass.indexOf("Array") == -1))
                  {
                     if (columnType.equals("DATE"))
                     {
                        Object date = dbResultSet.getDate(i);
                        if (date != null)
                           fieldContent = MyJSQLView_Utils.convertDBDateString_To_ViewDateString(
                              date + "", DBTablesPanel.getDataExportProperties().getCSVDateFormat());
                        else
                           fieldContent = "NULL";
                     }
                     else
                     {  
                        if (columnType.equals("DATETIME") || columnType.equals("TIMESTAMP"))
                        {
                           Object dateTime = dbResultSet.getTimestamp(i);
                           if (dateTime != null)
                              fieldContent = (new SimpleDateFormat(
                                 DBTablesPanel.getDataExportProperties().getCSVDateFormat()
                                 + " HH:mm:ss")).format(dateTime) + "";
                           else
                              fieldContent = "NULL";
                        }
                        else if (columnType.equals("TIMESTAMPTZ"))
                        {
                           Object dateTime = dbResultSet.getTimestamp(i);
                           if (dateTime != null)
                              fieldContent = (new SimpleDateFormat(
                                 DBTablesPanel.getDataExportProperties().getCSVDateFormat()
                                 + " HH:mm:ss Z")).format(dateTime) + "";
                           else
                              fieldContent = "NULL";
                        }
                        // TIMESTAMPLTZ, Oracle
                        else
                        {
                           String timestamp = dbResultSet.getString(i);
                           
                           if (timestamp != null)
                           {
                              if (timestamp.indexOf(" ") != -1)
                                 fieldContent = MyJSQLView_Utils.convertDBDateString_To_ViewDateString(
                                                     timestamp.substring(0, timestamp.indexOf(" ")),
                                                     DBTablesPanel.getDataExportProperties().getCSVDateFormat())
                                                     + timestamp.substring(timestamp.indexOf(" "));
                              else
                                 fieldContent = timestamp;
                           }
                           else
                              fieldContent = "NULL";
                        }
                     }
                     dumpData = dumpData + fieldContent + dataDelimiter;  
                  }
                  
                  // All other fields.
                  else
                  {
                     fieldContent = dbResultSet.getString(i);
                     
                     if (fieldContent != null)
                        dumpData = dumpData + fieldContent.trim() + dataDelimiter;
                     else
                        dumpData = dumpData + "NULL" + dataDelimiter;     
                  }
                  i++;
               }
               dumpData = ((String) dumpData).substring(0,
                                 ((String) dumpData).length() - dataDelimiter.length()) + "\n";
               // System.out.print(currentRow + " " + dumpData);
               
               dumpChunkOfData(dumpData);
               dumpData = "";
            }
            currentTableIncrement += limitIncrement;
         }
         while (!limits && currentTableIncrement < rowsCount && !dumpProgressBar.isCanceled());
         
         dumpProgressBar.dispose();
         ConnectionManager.closeConnection(db_Connection, "CSVDataDumpThread run()");
      }
      catch (SQLException e)
      {
         dumpProgressBar.dispose();
         ConnectionManager.displaySQLErrors(e, "CSVDataDumpThread run()");
         ConnectionManager.closeConnection(db_Connection, "CSVDataDumpThread run()");
      }
      finally
      {
         try
         {
            if (dbResultSet != null)
               dbResultSet.close();
         }
         catch (SQLException sqle)
         {
            ConnectionManager.displaySQLErrors(sqle,
               "CSVDataDumpThread run() failed closing result set");
         }
         finally
         {
            try
            {
               if (sqlStatement != null)
                  sqlStatement.close();
            }
            catch (SQLException sqle)
            {
               ConnectionManager.displaySQLErrors(sqle,
                  "CSVDataDumpThread run() failed closing sql statement");
            }
         }
      }
   }
   
   //==============================================================
   // Class Method to dump a chunk of data to the output file.
   //==============================================================

   private void dumpChunkOfData(Object dumpData)
   {
      // Class Method Instances
      byte[] currentBytes;

      // Dump the Chunk.
      try
      {
         currentBytes = dumpData.toString().getBytes();
         filebuff.write(currentBytes);
         filebuff.flush();
      }
      catch (IOException e)
      {
         String msg = "Error outputing data to: '" + fileName + "'.";
         JOptionPane.showMessageDialog(null, msg, fileName, JOptionPane.ERROR_MESSAGE);
      }
   }
}
