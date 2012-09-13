//=================================================================
//                   CSVDataImportThread
//=================================================================
//
//    This class provide the means to import a standard CSV file
// into the current selected MyJSQLView database table via a safe
// thread method. A progress bar is offered to address the ability
// to cancel the import.
//
//                 << CSVDataImportThread.java >>
//
//=================================================================
// Copyright (C) 2007-2012 Dana M. Proctor
// Version 6.9 09/11/2012
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
// Version 1.0 Initial Outline of CSVDataImportThread Class.
//         1.1 Rudimentary CSV Import Functionality.
//         1.2 Removed Single & Double Quotes From Field Values in
//             Class Method separateTokens().
//         1.3 Moved Instances importTable & delimiter to
//             Appropriate Class Methods. Instance delimiter
//             Obtained From DataImportProperties Object.
//         1.4 Header Update.
//         1.5 Added/Implemented identiferQuoteString.
//         1.6 MyJSQLView.getDataExportProperties().
//         1.7 ReplaceAll in Class Method separateTokens() for
//             Normal Quotes and Apostrophe.
//         1.8 Deliminator to Delimiter.
//         1.9 Fields Defined By Binary Content on CSV Exports
//             Set as NULL in Import.
//         2.0 Closed FileReader At End of Process in Class Method
//             importCSVFile().
//         2.1 Class Method refreshTableTabPanel() get/setTableFields()
//             Changed to get/setTableHeadings().
//         2.2 Added Class Methods parseColumnNameField() &
//             firstLetterToUpperCase(). Added Class Instances primaryKeys,
//             tableFields, and columnTypeHashMap. Implemented Fix
//             for MySQL Bit Imports, Task# 145594, in Class Method
//             importCSVFile().
//         2.3 Added Class Instance csvOption and Same in Argument for
//             Constructor.
//         2.4 Rebuilt to Implement 2.81++ Update Feature.
//         2.5 Added the Class Method Instance line to SQLIOException
//             Information in Class Method importCSVFile().
//         2.6 TableTabPanel getTableHeadings() to getCurrentTableHeadings()
//             in Class Method refreshTableTabPanel().
//         2.7 Normal Field Content for "Binary" Fields Set to NULL. Class
//             Method separateTokens Modified So Single Quotes Properly
//             Implemented.
//         2.8 Added Conditional and Code for PostgreSQL Geometric Data Types.
//         2.9 Class Method importCSVFile() Added Instance schemaTableName
//             to Properly Qualifier Table Name.
//         3.0 Properly Initialized identifierQuoteString Before Using.
//         3.1 Check on Class Method Instance sqlKeyString to Insure Its
//             Length Before Parsing in Update Finishing Up Class Method
//             importCSVFile().
//         3.2 Removed SQL BEGIN Statement for CSV Oracle Table Exports
//             in Class Method importCSVFile().
//         3.3 Replaced MyJSQLView.getSelectedTab() With DBTablesPanel.
//             getSelectedTableTabPanel().
//         3.4 MyJSQLView Project Common Source Code Formatting.
//         3.5 Class Method separateTokens() getDataExportProperites Changed
//             Over to the MyJSQLView_Frame Class.
//         3.6 Changed MyJSQLView_Frame.getDatabaseImportProperties() Method
//             Moved Over to the DBTablesPanel.
//         3.7 Conditional Check for NULL dbConnection in Method importCSVFile().
//         3.8 Implementation of Formatting for the Date Fields via the CSV
//             Data Import Properties Panel. Detection in importCSVFile() Method
//             & Addition of Class Method formatDateString().
//         3.9 Broke Down the Formatting of Oracle Timestamp,TZ,LTZ Data Formats
//             Further to be Able to Properly Process.
//         4.0 Method Instances columnClass & columnType in Class Method importCSVFile()
//             Check for Possible null If Table Field Name Not Properly Located in
//             Binary Usage. Already Done Previously for Other Uses.
//         4.1 Class Method importCSVFile() Added importTable Name to 
//             csvImportProgressBar, MyJSQLView_ProgressBar Construction.
//         4.2 Added Class Instance temporaryDataFile and Same to Constructor() as
//             an Argument. Handled the Deletion of file in run() as Needed.
//         4.3 Class Method importCSVFile() Set lineContent to "default" When
//             No Data Provided.
//         4.4 Class Method importCSVFile() Check for update Type sqlValuesString
//             Properly Parsed, sqlValuesString.length() > 2.
//         4.5 Check for SecurityException in run() for file.delete().
//         4.6 Method importCSVFile() Changed Method Instances sqlFieldNamesString &
//             sqlValuesString From Strings to StringBuffers.
//         4.7 Changed Package to Reflect Dandy Made Productions Code.
//         4.8 Parameterized Instances primaryKeys, tableFields, & fields in Class
//             Method importCSVFile() to Bring Code Into Compliance With Java 5.0
//             API.
//         4.9 Parameterized Instance tableHeadings in Class Method refreshTableTabPanel().
//             Also columnTypeHashMap, & columnClassHashMap in Class Method
//             importCSVFile().
//         5.0 Collected Class Istance schemaTableName in importCSVFile() From
//             MyJSQLView_Utils Method getSchemaTableName().
//         5.1 Updated Method importCSVFile() Removed BEGIN Statement SQL Query
//             Execution for SQLite Database.
//         5.2 Class Method importCSVFile() Instance schemaTableName Code for Deriving
//             Removed Since Collected From MyJSQLView_Utils, 5.0.
//         5.3 Class Method importCSVFile() Cast Object Returned by MyJSQLView_Access.
//             getConnection() to Connection.
//         5.4 Added Instances connectionProperties, & subProtocol. Changes to Access
//             Connections/Errors to the New Redefined Class ConnectionManager.
//         5.5 Class Method separateTokens() Insured That Removing of Quote Characters
//             Are NOT Removed if Present in delimiter.
//         5.6 Correction in formatDateString Class Method for Condition Use of
//             dateFormat.
//         5.7 Removed Instance connectionProperties in Consructor. Replaced Class
//             Instance subProtocol With dataSourceType.
//         5.8 Chang to MSAcces Date/DateTime Fields to Quote With the # Character
//             in Class Method importCSVFile(). In Same Method and Database Removal
//             of Quotes for Field Values if Non-String.
//         5.9 Replaced the Method formatDateString() Conversion Processing by the
//             Standardized Process Already Supplied by the MyJSQLView_Utils Class.
//         6.0 Copyright Update.
//         6.1 Removed the Casting of (Connection) for the Returned Instance for the
//             ConnectionManager.getConnection() in importCSVFile().
//         6.2 Added a finally in importCSVFile() to Close the fileReader & bufferedReader
//             in Case Exception is Thrown so File is Not Left Open on File System.
//             Class Method firstLetterToUpperCase() Check for Input String is Not
//             Empty.
//         6.3 Method importCSVFile() sqlStatementString Change to StringBuffer, Addition
//             of dateFormat & dataImportProperties. In Same sqlValueString Change to
//             StringBuffer. Same Method Removal of Quotes for Numeric Types All Databases.
//             Removal of Class Method formatDateString and Conversion Done Directly
//             With MyJSQLView_Utils.convertViewDateString_To_DBDateString().
//         6.4 Exclusion of the Processing of Array Timestamp Types. Instead Leave
//             Them As Is, importCSVFile(). Commented Out the Removal of Double Quotes
//             in Method separateTokens(), Screws Up the Processing of Array, Point,
//             etc. Types.
//         6.5 Added Batching to the Process of Inserting Data Into the Database.
//             Introduced Instances currentBatchRows, batchSize, & batchSizeEnabled
//             in importCSVFile() to Accomplish.
//         6.6 Class Method importCSVFile() Changed Instances primaryKeys, tableFields,
//             & fields from Vector Data Type to ArrayList. Also the Same Data Type
//             Changed for refreshTableTabPanel() Instance tableHeadings.
//         6.7 Class Method importCSVFile() finally for Closing sqlStatement.
//         6.8 Organized Imports.
//         6.9 Changed Package Name to com.dandymadeproductions.myjsqlview.io.
//             Made Class, & Constructor, Public.
//                    
//-----------------------------------------------------------------
//                   danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import javax.swing.JOptionPane;

import com.dandymadeproductions.myjsqlview.datasource.ConnectionManager;
import com.dandymadeproductions.myjsqlview.gui.panels.DBTablesPanel;
import com.dandymadeproductions.myjsqlview.gui.panels.TableTabPanel;
import com.dandymadeproductions.myjsqlview.utilities.MyJSQLView_ProgressBar;
import com.dandymadeproductions.myjsqlview.utilities.MyJSQLView_Utils;

/**
 *    The CSVDataImportThread class provide the means to import a
 * standard CSV file into the current selected MyJSQLView database
 * table via a safe thread method. A progress bar is offered to
 * address the ability to cancel the import.
 * 
 * @author Dana M. Proctor
 * @version 6.9 09/11/2012
 */

public class CSVDataImportThread implements Runnable
{
   // Class Instance Fields.
   Thread importThread;
   String dataSourceType, fileName, csvOption;
   boolean validImport, temporaryDataFile;

   //==============================================================
   // CSVDataImportThread Constructor.
   //==============================================================

   public CSVDataImportThread(String fileName, String csvOption, boolean temporaryDataFile)
   {
      // Constructor Instances

      this.fileName = fileName;
      this.csvOption = csvOption;
      this.temporaryDataFile = temporaryDataFile;

      // Setup some needed instances.
      dataSourceType = ConnectionManager.getDataSourceType();

      // Proceed with the process.
      importThread = new Thread(this, "CSVDataImportThread");
      importThread.start();
   }

   //==============================================================
   // Class Method for Normal Start of the Thread
   //==============================================================

   public void run()
   {
      // Class Method Instances
      File file;

      // Begin Execution of getting the
      // file and trying an import.

      file = new File(fileName);

      if (file.exists())
      {
         // Importing data from CSV file
         importCSVFile();

         // Refreshing table panel to see new inserted data and
         // removing the  temporary file, clipboard pastes, if
         // needed.
         
         if (validImport)
         {
            refreshTableTabPanel();

            if (temporaryDataFile)
            {
               try
               {
                  file.delete();
               }
               catch (SecurityException se){}
            }
         }
      }
      else
      {
         String msg = "The file '" + fileName + "' does not exists.";
         JOptionPane.showMessageDialog(null, msg, fileName, JOptionPane.ERROR_MESSAGE);
      }
   }

   //==============================================================
   // Class method for the major processing of the import of a
   // CSV data file.
   //==============================================================

   private void importCSVFile()
   {
      // Class Method Instances.
      Connection dbConnection;
      Statement sqlStatement;
      StringBuffer sqlFieldNamesString, sqlValuesString;
      String sqlKeyString;
      StringBuffer sqlStatementString;

      FileReader fileReader;
      BufferedReader bufferedReader;

      String importTable, schemaTableName;
      ArrayList<String> primaryKeys, tableFields, fields;
      HashMap<String, String> columnTypeHashMap;
      HashMap<String, String> columnClassHashMap;
      String identifierQuoteString;

      String currentLine, columnClass, columnType;
      int fileLineLength, fieldNumber, line;
      String[] lineContent;
      int currentBatchRows, batchSize;
      boolean batchSizeEnabled;
      
      MyJSQLView_ProgressBar csvImportProgressBar;
      String dateFormat;

      // Obtain database connection & setting up.

      dbConnection = ConnectionManager.getConnection("CSVDataImportThread importCSVFile()");

      if (dbConnection == null)
      {
         validImport = false;
         return;
      }

      importTable = DBTablesPanel.getSelectedTableTabPanel().getTableName();

      csvImportProgressBar = new MyJSQLView_ProgressBar("CSV Import To: " + importTable);

      identifierQuoteString = ConnectionManager.getIdentifierQuoteString();
      schemaTableName = MyJSQLView_Utils.getSchemaTableName(importTable);

      primaryKeys = DBTablesPanel.getSelectedTableTabPanel().getPrimaryKeys();
      tableFields = new ArrayList<String>();
      fields = new ArrayList<String>();
      columnTypeHashMap = DBTablesPanel.getSelectedTableTabPanel().getColumnTypeHashMap();
      columnClassHashMap = DBTablesPanel.getSelectedTableTabPanel().getColumnClassHashMap();

      dateFormat = DBTablesPanel.getDataImportProperties().getDateFormat();
      batchSize = DBTablesPanel.getGeneralProperties().getBatchSize();
      batchSizeEnabled = DBTablesPanel.getGeneralProperties().getBatchSizeEnabled();

      fileReader = null;
      bufferedReader = null;
      fileLineLength = 0;
      line = 0;

      // Begin the processing of the input CSV file by reading
      // each line and separating field data. Expectation
      // being that the first line will hold the field names
      // thereafter data.
      
      sqlStatement = null;

      try
      {
         // Disable autocommit and begin the start
         // of transactions.
         dbConnection.setAutoCommit(false);
         sqlStatement = dbConnection.createStatement();

         // Only MySQL & PostgreSQL supports.
         if (dataSourceType.equals(ConnectionManager.MYSQL)
             || dataSourceType.equals(ConnectionManager.POSTGRESQL))
            sqlStatement.executeUpdate("BEGIN");

         try
         {
            // Setting file reader & progress bar.
            fileReader = new FileReader(fileName);
            bufferedReader = new BufferedReader(fileReader);

            while ((currentLine = bufferedReader.readLine()) != null)
               fileLineLength++;

            csvImportProgressBar.setTaskLength(fileLineLength);
            csvImportProgressBar.pack();
            csvImportProgressBar.center();
            csvImportProgressBar.setVisible(true);
            validImport = true;

            // Beginning processing the input file for insertions
            // into the database table.

            bufferedReader.close();
            fileReader = new FileReader(fileName);
            bufferedReader = new BufferedReader(fileReader);

            sqlFieldNamesString = new StringBuffer();
            sqlValuesString = new StringBuffer();
            sqlStatementString = new StringBuffer();
            currentBatchRows = 0;
            fieldNumber = 0;
            line = 1;

            while ((currentLine = bufferedReader.readLine()) != null)
            {
               // System.out.println(currentLine);

               // Check to see if user wishes to stop.
               if (csvImportProgressBar.isCanceled())
               {
                  validImport = false;
                  break;
               }

               // Create the required SQL insertation statement
               // with field names or the update statement.
               if (line == 1)
               {
                  // Create Intial Insert Statement
                  if (csvOption.equals("Insert"))
                     sqlFieldNamesString.append("INSERT INTO " + schemaTableName + " (");
                  // Create Intial Update Statement
                  else
                     sqlFieldNamesString.append("UPDATE " + schemaTableName + " SET ");

                  lineContent = separateTokens(currentLine, 0);
                  fieldNumber = lineContent.length;

                  // Inserting fields names or storing them for later
                  // use with an update statement.
                  for (int i = 0; i < fieldNumber; i++)
                  {
                     if (csvOption.equals("Insert"))
                        sqlFieldNamesString.append(identifierQuoteString + lineContent[i]
                                                   + identifierQuoteString + ", ");
                     tableFields.add(parseColumnNameField(lineContent[i]));
                     fields.add(lineContent[i]);
                  }
                  if (csvOption.equals("Insert"))
                  {
                     sqlFieldNamesString.delete((sqlFieldNamesString.length() - 2),
                                                sqlFieldNamesString.length());
                     sqlFieldNamesString.append(") VALUES (");
                  }
               }

               // Create SQL statement data field content.
               else
               {
                  // Reset strings for each line of data.
                  sqlValuesString.delete(0, sqlValuesString.length());
                  sqlStatementString.delete(0, sqlStatementString.length());
                  sqlKeyString = "";

                  lineContent = separateTokens(currentLine, fieldNumber);

                  for (int i = 0; i < lineContent.length; i++)
                  {
                     columnClass = columnClassHashMap.get(tableFields.get(i));
                     columnType = columnTypeHashMap.get(tableFields.get(i));
                     // System.out.println("ColumnClass: " + columnClass + " " + "ColumnType: "
                     // + columnType + " " + lineContent[i]);

                     // Make sure and catch all null default entries first.

                     if (lineContent[i].toLowerCase().equals("null")
                         || lineContent[i].toLowerCase().equals("default"))
                     {
                        // Do Nothing.
                     }

                     // Just set lineContent with no data to default.

                     else if (lineContent[i].equals(""))
                        lineContent[i] = "default";

                     // All Blob/Bytea, Binary Data Exported as Text
                     // 'Binary' in DataDumpThread for CSV.

                     else if ((columnClass != null && columnType != null)
                              && ((columnClass.indexOf("String") == -1 && columnType.indexOf("BLOB") != -1)
                                  || (columnClass.indexOf("BLOB") != -1 && columnType.indexOf("BLOB") != -1)
                                  || (columnType.indexOf("BYTEA") != -1)
                                  || (columnType.indexOf("BINARY") != -1) || (columnType.indexOf("RAW") != -1)))
                     {
                        lineContent[i] = "null";
                     }

                     // MySQL Bit Fields

                     else if (dataSourceType.equals(ConnectionManager.MYSQL) && columnType != null
                              && columnType.indexOf("BIT") != -1)
                     {
                        lineContent[i] = "B'" + lineContent[i] + "'";
                     }

                     // PostgreSQL Geometric Fields

                     else if (dataSourceType.equals(ConnectionManager.POSTGRESQL) && columnClass != null
                              && columnClass.indexOf("geometric") != -1)
                     {
                        lineContent[i] = "'" + lineContent[i] + "'::" + columnType;
                     }

                     // Date, DateTime, & Timestamp Fields

                     else if ((columnType != null)
                              && (columnType.equals("DATE") || columnType.equals("DATETIME")
                                    || (columnType.indexOf("TIMESTAMP") != -1)
                                        && columnClass.indexOf("Array") == -1))
                     {
                        if (columnType.equals("DATE"))
                        {
                           if (dataSourceType.equals(ConnectionManager.ORACLE))
                              lineContent[i] = "TO_DATE('"
                                               + MyJSQLView_Utils.convertViewDateString_To_DBDateString(
                                                  lineContent[i], dateFormat) + "', 'YYYY-MM-DD')";
                           else if (dataSourceType.equals(ConnectionManager.MSACCESS))
                              lineContent[i] = "#"
                                               + MyJSQLView_Utils.convertViewDateString_To_DBDateString(
                                                  lineContent[i], dateFormat) + "#";
                           else
                              lineContent[i] = "'"
                                               + MyJSQLView_Utils.convertViewDateString_To_DBDateString(
                                                  lineContent[i], dateFormat) + "'";
                        }
                        // DateTime & Timestamps.
                        else
                        {
                           int firstSpace;
                           String time;

                           // Try to get the time separated before formatting
                           // the date.

                           if (lineContent[i].indexOf(" ") != -1)
                           {
                              firstSpace = lineContent[i].indexOf(" ");
                              time = lineContent[i].substring(firstSpace);
                              lineContent[i] = lineContent[i].substring(0, firstSpace);
                           }
                           else
                              time = "";

                           // Process accordingly.

                           // Oracle Timestamps
                           if (dataSourceType.equals(ConnectionManager.ORACLE))
                           {
                              if (columnType.equals("TIMESTAMP"))
                                 lineContent[i] = "TO_TIMESTAMP('"
                                                  + MyJSQLView_Utils.convertViewDateString_To_DBDateString(
                                                     lineContent[i], dateFormat) + time
                                                  + "', 'YYYY-MM-DD HH24:MI:SS:FF')";

                              else if (columnType.equals("TIMESTAMPTZ"))
                                 lineContent[i] = "TO_TIMESTAMP_TZ('"
                                                  + MyJSQLView_Utils.convertViewDateString_To_DBDateString(
                                                     lineContent[i], dateFormat) + time
                                                  + "', 'YYYY-MM-DD HH24:MI:SS TZHTZM')";
                              // TIMESTAMPLTZ
                              else
                                 lineContent[i] = "TO_TIMESTAMP_TZ('"
                                                  + MyJSQLView_Utils.convertViewDateString_To_DBDateString(
                                                     lineContent[i], dateFormat) + time
                                                  + "', 'YYYY-MM-DD HH24:MI:SS TZH:TZM')";
                           }
                           // MSAccess
                           else if (dataSourceType.equals(ConnectionManager.MSACCESS))
                              lineContent[i] = "#"
                                               + MyJSQLView_Utils.convertViewDateString_To_DBDateString(
                                                  lineContent[i], dateFormat) + time + "#";
                           // All others
                           else
                              lineContent[i] = "'"
                                               + MyJSQLView_Utils.convertViewDateString_To_DBDateString(
                                                  lineContent[i], dateFormat) + time + "'";
                        }
                     }

                     // Normal Fields

                     else
                     {
                        // Don't Quote Numeric Values.
                        if (columnClass != null && columnClass.indexOf("Integer") == -1
                              && columnClass.indexOf("Long") == -1 && columnClass.indexOf("Float") == -1
                              && columnClass.indexOf("Double") == -1 && columnClass.indexOf("Byte") == -1
                              && columnClass.indexOf("BigDecimal") == -1 && columnClass.indexOf("Short") == -1)
                              
                           lineContent[i] = "'" + lineContent[i] + "'";
                     }

                     // Now that the content data as been derived create the
                     // appropriate Insert or Update SQL statement.

                     // Insert SQL.
                     if (csvOption.equals("Insert"))
                        sqlValuesString.append(lineContent[i] + ", ");
                     // Update SQL
                     else
                     {
                        // Capture key data for SQL.
                        if (primaryKeys.contains(fields.get(i)))
                        {
                           if (sqlKeyString.equals(""))
                           {
                              sqlKeyString = "WHERE " + identifierQuoteString + fields.get(i)
                                             + identifierQuoteString + "=" + lineContent[i] + " AND ";
                           }
                           else
                           {
                              sqlKeyString += identifierQuoteString + fields.get(i) + identifierQuoteString
                                              + "=" + lineContent[i] + " AND ";
                           }
                        }
                        // Normal content.
                        else
                           sqlValuesString.append(identifierQuoteString + fields.get(i)
                                                  + identifierQuoteString + "=" + lineContent[i] + ", ");
                     }
                  }
                  // System.out.println(sqlValuesString);

                  // Finishing the Insert or Update SQL statement.
                  if (csvOption.equals("Insert"))
                  {
                     sqlValuesString.delete((sqlValuesString.length() - 2), sqlValuesString.length());
                     sqlValuesString.append(")");
                  }
                  else
                  {
                     if (sqlValuesString.length() >= 2)
                        sqlValuesString.delete((sqlValuesString.length() - 2), sqlValuesString.length());
                     if (sqlKeyString.length() > 5)
                        sqlValuesString.append(" " + sqlKeyString.substring(0, sqlKeyString.length() - 5));
                     else
                        sqlValuesString.append(" " + sqlKeyString);
                  }

                  sqlStatementString.append(sqlFieldNamesString.toString() + sqlValuesString.toString());
                  // System.out.println(sqlStatementString);

                  // Insert/Update current line's data.
                  sqlStatement.executeUpdate(sqlStatementString.toString());
                  
                  // Commit on Batch Size if Desired.
                  if (batchSizeEnabled)
                  {
                     if (currentBatchRows > batchSize)
                     {
                        dbConnection.commit();
                        currentBatchRows = 0;
                     }
                     else
                        currentBatchRows++;
                  }   
               }
               csvImportProgressBar.setCurrentValue(line++);
            }
            csvImportProgressBar.dispose();

            // Commiting the transactions as necessary
            // and cleaning up.

            if (validImport)
               dbConnection.commit();
            else
               dbConnection.rollback();

            fileReader.close();
            bufferedReader.close();
            sqlStatement.close();
            dbConnection.setAutoCommit(true);
            ConnectionManager.closeConnection(dbConnection, "CSVDataImportThread importCSVFile()");
         }
         catch (IOException e)
         {
            csvImportProgressBar.dispose();
            JOptionPane.showMessageDialog(null, "Unable to Read Input File!", "Alert",
               JOptionPane.ERROR_MESSAGE);
            try
            {
               sqlStatement.close();
               dbConnection.rollback();
               dbConnection.setAutoCommit(true);
               ConnectionManager
                     .closeConnection(dbConnection, "CSVDataImportThread importCSVFile() rollback");
            }
            catch (SQLException error)
            {
               ConnectionManager.displaySQLErrors(error,
                  "SQLDataImportThread importCSVFile() rollback failed");
            }
         }
      }
      catch (SQLException e)
      {
         csvImportProgressBar.dispose();
         ConnectionManager.displaySQLErrors(e, "line# " + line + " CSVDataImportThread importCSVLFile()");
         try
         {
            dbConnection.rollback();
            dbConnection.setAutoCommit(true);
            ConnectionManager.closeConnection(dbConnection, "CSVDataImportThread importCSVFile() rollback");
         }
         catch (SQLException error)
         {
            ConnectionManager.displaySQLErrors(e, "CSVDataImportThread importCSVFile() rollback failed");
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
               ConnectionManager.displaySQLErrors(e, "CSVDataImportThread importCSVFile() failed close");
            }
         }
      }
      finally
      {
         try
         {
            if (fileReader != null)
            {
               fileReader.close();
               bufferedReader.close();
            }
         }
         catch (IOException ioe)
         {
            // Tried
         }
      }
   }

   //==============================================================
   // Class method to parse the MyJSQLView table's column name
   // fields. The parsed strings creates a more user friendly
   // format that will be displayed in the sort/search ComboBoxes,
   // summary table, and TableEntryForm.
   //==============================================================

   private String parseColumnNameField(String columnString)
   {
      // Class Methods.
      StringTokenizer field;
      StringBuffer columnName;

      // Initialize.
      columnName = new StringBuffer();

      // Delimiter '_' should seperate words in a name.

      // Multiple word name.
      if (columnString.indexOf('_') != -1)
      {
         field = new StringTokenizer(columnString, "_");

         while (field.hasMoreTokens())
         {
            if (field.countTokens() > 1)
            {
               columnName.append(firstLetterToUpperCase(field.nextToken()));
               columnName.append(" ");
            }
            else
               columnName.append(firstLetterToUpperCase(field.nextToken()));
         }
         columnString = columnName.toString();
      }

      // Single word name.
      else
      {
         columnString = firstLetterToUpperCase(columnString);
      }
      return columnString;
   }

   //==============================================================
   // Class method to convert the first letter of the input string
   // to uppercase.
   //==============================================================

   private String firstLetterToUpperCase(String capitalizeString)
   {
      String firstLetter;

      if (capitalizeString.length() >= 1)
      {
         firstLetter = capitalizeString.substring(0, 1);
         firstLetter = firstLetter.toUpperCase();
         return firstLetter + capitalizeString.substring(1);
      }
      else
         return capitalizeString;
   }

   //==============================================================
   // Class method for separating tokens in the imput
   // string.
   //==============================================================

   private String[] separateTokens(String inputLine, int limit)
   {
      // Class Instances
      String delimiter = DBTablesPanel.getDataImportProperties().getDataDelimiter();

      // Check characters?
      //if (delimiter.indexOf("\"") == -1)
      //   inputLine = inputLine.replaceAll("\"", "");

      if (delimiter.indexOf("'") == -1)
         inputLine = inputLine.replaceAll("'", "''");

      if (delimiter.indexOf("`") == -1)
         inputLine = inputLine.replaceAll("`", "''");

      String[] tokens;
      tokens = inputLine.split(delimiter, limit);
      return tokens;
   }

   //==============================================================
   // Class method to refresh table tab panel.
   //==============================================================

   private void refreshTableTabPanel()
   {
      TableTabPanel currentTableTabPanel = DBTablesPanel.getSelectedTableTabPanel();
      if (currentTableTabPanel != null)
      {
         ArrayList<String> tableHeadings = currentTableTabPanel.getCurrentTableHeadings();
         currentTableTabPanel.setTableHeadings(tableHeadings);
      }
   }
}