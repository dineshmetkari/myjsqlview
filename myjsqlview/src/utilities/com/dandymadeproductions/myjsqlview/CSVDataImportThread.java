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
// Copyright (C) 2007-2010 Dana M. Proctor
// Version 4.9 05/17/2010
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
//         4.3 Class Method importCSVFile() Set lineContent to "default" When No
//             Data Provided.
//         4.4 Class Method importCSVFile() Check for update Type sqlValuesString
//             Properly Parsed, sqlValuesString.length() > 2.
//         4.5 Check for SecurityException in run() for file.delete().
//         4.6 Method importCSVFile() Changed Method Instances sqlFieldNamesString &
//             sqlValuesString From Strings to StringBuffers.
//         4.7 Changed Package to Reflect Dandy Made Productions Code.
//         4.8 Parameterized Instances primaryKeys, tableFields, & fields in Class Method
//             importCSVFile() to Bring Code Into Compliance With Java 5.0 API.
//         4.9 Parameterized Instance tableHeadings in Class Method refreshTableTabPanel().
//             Also columnTypeHashMap, & columnClassHashMap in Class Method
//             importCSVFile().
//                    
//-----------------------------------------------------------------
//                   danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import java.util.HashMap;
import java.util.StringTokenizer;
import javax.swing.*;

/**
 *    The CSVDataImportThread class provide the means to import a
 * standard CSV file into the current selected MyJSQLView database
 * table via a safe thread method. A progress bar is offered to
 * address the ability to cancel the import.
 * 
 * @author Dana M. Proctor
 * @version 4.9 05/17/2010
 */

class CSVDataImportThread implements Runnable
{
   // Class Instance Fields.
   Thread importThread;
   String fileName, csvOption;
   boolean validImport, temporaryDataFile;

   //==============================================================
   // CSVDataImportThread Constructor.
   //==============================================================

   CSVDataImportThread(String fileName, String csvOption, boolean temporaryDataFile)
   {
      // Constructor Instances
      this.fileName = fileName;
      this.csvOption = csvOption;
      this.temporaryDataFile = temporaryDataFile;

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
         // removing the file if needed.
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
      String sqlStatementString;

      FileReader fileReader;
      BufferedReader bufferedReader;

      String importTable, schemaTableName;
      Vector<String> primaryKeys, tableFields, fields;
      HashMap<String, String> columnTypeHashMap;
      HashMap<String, String> columnClassHashMap;
      String identifierQuoteString;

      String currentLine, columnClass, columnType;
      int fileLineLength, fieldNumber, line;
      String[] lineContent;

      MyJSQLView_ProgressBar csvImportProgressBar;

      // Obtain database connection & setting up.

      dbConnection = MyJSQLView_Access.getConnection("CSVDataImportThread importCSVFile()");
      
      if (dbConnection == null)
      {
         validImport = false;
         return;
      }
      
      importTable = DBTablesPanel.getSelectedTableTabPanel().getTableName();
      
      csvImportProgressBar = new MyJSQLView_ProgressBar("CSV Import To: " + importTable);

      identifierQuoteString = MyJSQLView_Access.getIdentifierQuoteString();
      if (importTable.indexOf(".") != -1)
      {
         schemaTableName = identifierQuoteString + importTable.substring(0, importTable.indexOf("."))
                           + identifierQuoteString + "." + identifierQuoteString
                           + importTable.substring(importTable.indexOf(".") + 1) + identifierQuoteString;
      }
      else
         schemaTableName = identifierQuoteString + importTable + identifierQuoteString;

      primaryKeys = DBTablesPanel.getSelectedTableTabPanel().getPrimaryKeys();
      tableFields = new Vector <String>();
      fields = new Vector <String>();
      columnTypeHashMap = DBTablesPanel.getSelectedTableTabPanel().getColumnTypeHashMap();
      columnClassHashMap = DBTablesPanel.getSelectedTableTabPanel().getColumnClassHashMap();

      fileLineLength = 0;
      line = 0;

      // Begin the processing of the input CSV file by reading
      // each line and separating field data. Expectation
      // being that the first line will hold the field names
      // thereafter data.

      try
      {
         // Disable autocommit and begin the start
         // of transactions.
         dbConnection.setAutoCommit(false);
         sqlStatement = dbConnection.createStatement();

         // HSQL & Oracle does not support.
         if (MyJSQLView_Access.getSubProtocol().indexOf("hsql") == -1
             && MyJSQLView_Access.getSubProtocol().indexOf("oracle") == -1)
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
                     sqlFieldNamesString.delete((sqlFieldNamesString.length() - 2), sqlFieldNamesString.length());
                     sqlFieldNamesString.append(") VALUES (");
                  }
               }
               
               // Create SQL statement data field content.
               else
               {
                  // Reset strings for each line of data.
                  sqlValuesString = new StringBuffer();
                  sqlKeyString = "";

                  lineContent = separateTokens(currentLine, fieldNumber);
                  
                  for (int i = 0; i < lineContent.length; i++)
                  {
                     columnClass = columnClassHashMap.get(tableFields.get(i));
                     columnType = columnTypeHashMap.get(tableFields.get(i));
                     //  System.out.println("ColumnClass: " + columnClass + " " + "ColumnType: " + columnType + " " +
                     //                   lineContent[i]);

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
                     
                     else if ((columnClass != null && columnType != null) && ((columnClass.indexOf("String") == -1 && columnType.indexOf("BLOB") != -1) ||
                         (columnClass.indexOf("BLOB") != -1 && columnType.indexOf("BLOB") != -1) ||
                         (columnType.indexOf("BYTEA") != -1) || (columnType.indexOf("BINARY") != -1) ||
                         (columnType.indexOf("RAW") != -1)))
                     {
                        lineContent[i] = "null";
                     }
                     
                     // MySQL Bit Fields
                     
                     else if (MyJSQLView_Access.getSubProtocol().equals("mysql") &&
                              columnType != null && columnType.indexOf("BIT") != -1)
                     {
                        lineContent[i] = "B'" + lineContent[i] + "'";
                     }
                     
                     // PostgreSQL Geometric Fields
                     
                     else if (MyJSQLView_Access.getSubProtocol().equals("postgresql") &&
                              columnClass != null && columnClass.indexOf("geometric") != -1)
                     {
                        lineContent[i] = "'" + lineContent[i] + "'::" + columnType;
                     }
                     
                     // Date, DateTime, & Timestamp Fields
                     
                     else if ((columnType != null) && (columnType.equals("DATE") ||
                                                       columnType.equals("DATETIME") ||
                                                       columnType.indexOf("TIMESTAMP") != -1))
                     {
                        if (columnType.equals("DATE"))
                        {
                           if (MyJSQLView_Access.getSubProtocol().indexOf("oracle") != -1)
                              lineContent[i] = "TO_DATE('" + formatDateString(lineContent[i]) + "', 'YYYY-MM-DD')";
                           else
                              lineContent[i] = "'" + formatDateString(lineContent[i]) + "'";
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
                           if (MyJSQLView_Access.getSubProtocol().indexOf("oracle") != -1)
                           {
                              if (columnType.equals("TIMESTAMP"))
                                 lineContent[i] = "TO_TIMESTAMP('" + formatDateString(lineContent[i]) + time + "', 'YYYY-MM-DD HH24:MI:SS:FF')";
                              
                              else if (columnType.equals("TIMESTAMPTZ"))
                                 lineContent[i] = "TO_TIMESTAMP_TZ('" + formatDateString(lineContent[i]) + time + "', 'YYYY-MM-DD HH24:MI:SS TZHTZM')";
                              // TIMESTAMPLTZ
                              else
                                 lineContent[i] = "TO_TIMESTAMP_TZ('" + formatDateString(lineContent[i]) + time + "', 'YYYY-MM-DD HH24:MI:SS TZH:TZM')";
                           }
                           // All others
                           else
                              lineContent[i] = "'" + formatDateString(lineContent[i]) + time + "'";
                        }
                     }
                     
                     // Normal Fields
                     
                     else
                     {
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
                                             + identifierQuoteString + "=" + lineContent[i] 
                                             + " AND ";
                           }
                           else
                           {
                              sqlKeyString += identifierQuoteString + fields.get(i)
                                              + identifierQuoteString + "=" + lineContent[i]
                                              + " AND ";
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

                  sqlStatementString = sqlFieldNamesString.toString() + sqlValuesString.toString();
                  //System.out.println(sqlStatementString);

                  // Insert/Update current line's data.
                  sqlStatement.executeUpdate(sqlStatementString);
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
            MyJSQLView_Access.closeConnection(dbConnection, "CSVDataImportThread importCSVFile()");
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
               MyJSQLView_Access
                     .closeConnection(dbConnection, "CSVDataImportThread importCSVFile() rollback");
            }
            catch (SQLException error)
            {
               MyJSQLView_Access.displaySQLErrors(error,
                  "SQLDataImportThread importCSVFile() rollback failed");
            }
         }
      }
      catch (SQLException e)
      {
         csvImportProgressBar.dispose();
         MyJSQLView_Access.displaySQLErrors(e, "line# " + line + " CSVDataImportThread importCSVLFile()");
         try
         {
            dbConnection.rollback();
            dbConnection.setAutoCommit(true);
            MyJSQLView_Access.closeConnection(dbConnection, "CSVDataImportThread importCSVFile() rollback");
         }
         catch (SQLException error)
         {
            MyJSQLView_Access.displaySQLErrors(e, "CSVDataImportThread importCSVFile() rollback failed");
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

      firstLetter = capitalizeString.substring(0, 1);
      firstLetter = firstLetter.toUpperCase();
      return firstLetter + capitalizeString.substring(1);
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
      inputLine = inputLine.replaceAll("\"", "");
      inputLine = inputLine.replaceAll("'", "''");
      inputLine = inputLine.replaceAll("`", "''");

      String[] tokens;
      tokens = inputLine.split(delimiter, limit);
      return tokens;
   }
   
   //==============================================================
   // Class method for converting the input date string into the
   // required database format. The CVS import panel allows the
   // hint on how to convert the date field properly. Standard 
   // Date format for databases is YYYY-MM-dd.
   //==============================================================
   
   private String formatDateString(String inputDateString)
   {
      DataImportProperties dataImportProperties;
      String dateFormat, year, month, day;
      int firstDashIndex, lastDashIndex;
      
      // Get the current Date import option.
      dataImportProperties = DBTablesPanel.getDataImportProperties();
      dateFormat = dataImportProperties.getDateFormat();
      
      // Generally just replace all forward slashes to required dash
      // and check to make sure there is some kind of valid format.
      // Routine does not allow dates with spaces, ex. Jan. 01, 2009.
      
      if (inputDateString.indexOf("/") != -1)
         inputDateString = inputDateString.replaceAll("/", "-");
      
      firstDashIndex = inputDateString.indexOf("-");
      lastDashIndex = inputDateString.lastIndexOf("-");
      if (firstDashIndex == -1 || lastDashIndex == -1)
         return "";
      
      // Convert the input date string to the appropriate format.
      
      // YYYY-MM-dd
      if (dateFormat.equals("YYYY-MM-dd") || dateFormat.equals("YYYY/MM/dd"))
      {
         year = inputDateString.substring(0, firstDashIndex);
         month = inputDateString.substring(firstDashIndex + 1, lastDashIndex);
         if (month.length() > 2)
            month = MyJSQLView_Utils.convertCharMonthToDecimal(month) + "";
         day = inputDateString.substring(lastDashIndex + 1);
      }
      
      // dd-MM-YYYY
      else if (dateFormat.equals("dd-MM-YYYY") || dateFormat.equals("dd/MM/YYYY"))
      {
         year = inputDateString.substring(lastDashIndex + 1);
         month = inputDateString.substring(firstDashIndex + 1, lastDashIndex);
         if (month.length() > 2)
            month = MyJSQLView_Utils.convertCharMonthToDecimal(month) + "";
         day = inputDateString.substring(0, firstDashIndex);
      }
      
      // MM-dd-YYYY
      else
      {  
         year = inputDateString.substring(lastDashIndex + 1);
         month = inputDateString.substring(0, firstDashIndex);
         if (month.length() > 2)
            month = MyJSQLView_Utils.convertCharMonthToDecimal(month) + "";
         day = inputDateString.substring(firstDashIndex + 1, lastDashIndex); 
      }
      
      // System.out.println("Year:" + year + " Month:" + month + " Day:" + day);
      return year + "-" + month + "-" + day;
   }

   //==============================================================
   // Class method to refresh table tab panel.
   //==============================================================

   private void refreshTableTabPanel()
   {
      TableTabPanel currentTableTabPanel = DBTablesPanel.getSelectedTableTabPanel();
      if (currentTableTabPanel != null)
      {
         Vector<String> tableHeadings = currentTableTabPanel.getCurrentTableHeadings();
         currentTableTabPanel.setTableHeadings(tableHeadings);
      }
   }
}