//=================================================================
//                MyJSQLView CSVQueryDataDumpThread
//=================================================================
//    This class provides a thread to safely dump a given SQL query
// data to a local file in the CSV format. A status dialog with
// cancel is provided to allow the ability to prematurely terminate
// the dump.
//
//                 << CSVQueryDataDumpThread.java >>
//
//=================================================================
// Copyright (C) 2005-2013 Dana M. Proctor
// Version 1.2 07/07/2013
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
// Version 1.0 Original CSVQueryDataDumpThread Class.
//         1.1 Change in run() to Use DBTablePanel.getGeneralDBProperties().
//         1.2 Basic Functional Class That Exports With Simple SELECTS.
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
import java.sql.ResultSetMetaData;
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
 *    The CSVQueryDumpThread class provides a thread to safely dump a
 * given SQL query data to a local file in the CSV format. A status
 * dialog with cancel is provided to allow the ability to prematurely
 * terminate the dump.
 * 
 * @author Dana M. Proctor
 * @version 1.2 07/07/2013
 */

public class CSVQueryDataDumpThread implements Runnable
{
   // Class Instances
   private Connection db_Connection;
   
   private ArrayList<String> columnNameFields;
   private HashMap<String, String> columnClassHashMap;
   private HashMap<String, String> columnTypeHashMap;
   private HashMap<String, Integer> columnSizeHashMap;
   
   private String queryString, fileName;
   private String dataSourceType;
   private BufferedOutputStream filebuff;
   
   //==============================================================
   // CSVQueryDataDumpThread Constructor.
   //==============================================================

   public CSVQueryDataDumpThread(Connection db_Connection, String queryString, String fileName)
   {
      this.db_Connection = db_Connection;
      this.queryString = queryString;
      this.fileName = fileName;
      
      columnNameFields = new ArrayList <String>();
      columnClassHashMap = new HashMap <String, String>();
      columnTypeHashMap = new HashMap <String, String>();
      columnSizeHashMap = new HashMap <String, Integer>();
      
      dataSourceType = ConnectionManager.getDataSourceType();
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
      String field, columnClass, columnType, dataDelimiter;
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
      dumpProgressBar = new MyJSQLView_ProgressBar("SQL Query Dump");

      // Get Connection to Database.
      if (db_Connection == null)
         db_Connection = ConnectionManager.getConnection("CSVQueryDataDumpThread run()");
      
      if (db_Connection == null || queryString.isEmpty())
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
      try
      {
         getColumnNames(db_Connection, queryString);
      }
      catch (SQLException sqle)
      {
         return;
      }
      
      // Modifiy column names?
      columnNamesString = new StringBuffer();
      oracleColumnNamesString = new StringBuffer();
      columnNamesIterator = columnNameFields.iterator();
      
      while (columnNamesIterator.hasNext())
      {
         field = columnNamesIterator.next();

         // Oracle TIMESTAMPLTZ handled differently to remove the
         // need to SET SESSION.

         if (dataSourceType.equals(ConnectionManager.ORACLE)
             && (columnTypeHashMap.get(field)).equals("TIMESTAMPLTZ"))
         {
            oracleColumnNamesString.append("TO_CHAR(" + identifierQuoteString
                                           + field + identifierQuoteString
                                           + ", 'YYYY-MM-DD HH24:MM:SS TZR') AS " 
                                           + identifierQuoteString
                                           + field + identifierQuoteString + ", ");
         }
         else
            oracleColumnNamesString.append(identifierQuoteString 
                                           + field + identifierQuoteString + ", ");
         
         // Unmodified Names.
         columnNamesString.append(identifierQuoteString + field + identifierQuoteString + ", ");
      }
      oracleColumnNamesString.delete((oracleColumnNamesString.length() - 2),
         oracleColumnNamesString.length());
      columnNamesString.delete((columnNamesString.length() - 2), columnNamesString.length());
      
      // Have a connection, columns, query, & file to write to so begin
      // dumping data.
      
      sqlStatement = null;
      dbResultSet = null;
      
      try
      {
         sqlStatement = db_Connection.createStatement();
         
         // Collect the row count of the table and setting
         // up a progress bar for tracking/canceling.
         
         sqlStatementString = "SELECT COUNT(*) AS " + identifierQuoteString + "rowCount"
                              + identifierQuoteString + " FROM (" + queryString + ") AS "
                              + identifierQuoteString + "myRowTable" + identifierQuoteString;
         System.out.println(sqlStatementString);

         dbResultSet = sqlStatement.executeQuery(sqlStatementString);
      
         if (dbResultSet.next())
            rowsCount = dbResultSet.getInt(1);

         dumpProgressBar.setTaskLength(rowsCount);
         dumpProgressBar.pack();
         dumpProgressBar.center();
         dumpProgressBar.setVisible(true);
         
         // Begin Dumping Data.
         
         dumpData = "";
      
         // Constructing the column names line & dumping.
         columnNamesIterator = columnNameFields.iterator();

         while (columnNamesIterator.hasNext())
            dumpData = (String) dumpData + columnNamesIterator.next()
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
            // Creating the Select statement to retrieve data. Oracle
            // needs special handling for Timestamps with Time Zone.
            
            /*
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
            */
            
            System.out.println(queryString);

            dbResultSet = sqlStatement.executeQuery(queryString);
            
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
                  columnClass = columnClassHashMap.get(currentHeading);
                  columnType = columnTypeHashMap.get(currentHeading);
                  columnSize = columnSizeHashMap.get(currentHeading).intValue();

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
         while (currentTableIncrement < rowsCount && !dumpProgressBar.isCanceled());
         
         dumpProgressBar.dispose();
      }
      catch (SQLException e)
      {
         dumpProgressBar.dispose();
         ConnectionManager.displaySQLErrors(e, "CSVQueryDataDumpThread run()");
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
               "CSVQueryDataDumpThread run() failed closing result set");
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
                  "CSVQueryDataDumpThread run() failed closing sql statement");
            }
         }
      }
      ConnectionManager.closeConnection(db_Connection, "CSVQueryDataDumpThread run()");
   }
   
   //==============================================================
   // Class method to obtain the column names from the table.
   // Additional information about the column, size & type, are
   // also stored away for future use.
   //==============================================================

   private void getColumnNames(Connection dbConnection, String query) throws SQLException
   {
      // Method Instances
      String sqlStatementString;
      Statement sqlStatement;
      ResultSet db_resultSet;
      ResultSetMetaData tableMetaData;

      String colNameString, columnClass, columnType;
      Integer columnSize;

      // Connecting to the data base, to obtain
      // meta data, and column names.
      sqlStatement = null;
      db_resultSet = null;
      
      try
      {
         sqlStatement = dbConnection.createStatement();

         // Setting Up the Column Names, Form Fields, ComboBox
         // Text, Hashmaps, Special Fields, & Primary Key(s).

         // *************** HSQL NEEDS FIXED ***********************
         // This is a lame work around for HSQLDB which does not
         // properly parse a standard short query with trailing
         // LIMIT. I hate crap like this.
         // ********************************************************
         if (dataSourceType.equals(ConnectionManager.HSQL))
         {
            if (query.toUpperCase().indexOf("WHERE") != -1 ||
                query.toUpperCase().indexOf("GROUP") != -1 ||
                query.toUpperCase().indexOf("HAVING") != -1 ||
                query.toUpperCase().indexOf("ORDER") != -1)
               sqlStatementString = query + " LIMIT 1";
            else
               sqlStatementString = query + " AS lame LIMIT 1";
         }
         else if (dataSourceType.equals(ConnectionManager.ORACLE))
         {
            if (query.toUpperCase().indexOf("WHERE") != -1)
            {
               if (query.toUpperCase().indexOf("GROUP BY") != -1)
               {
                  sqlStatementString = query.substring(0, query.indexOf("GROUP BY") - 1)
                                       + " AND ROWNUM=1 " + query.substring(query.indexOf("GROUP BY"));
               }
               else
               {
                  if (query.toUpperCase().indexOf("ORDER BY") != -1)
                     sqlStatementString = query.substring(0, query.indexOf("ORDER BY") - 1)
                                          + " AND ROWNUM=1 " + query.substring(query.indexOf("ORDER BY"));
                  else
                     sqlStatementString = query + " AND ROWNUM=1";
               }
            }
            else
            {
               sqlStatementString = query + " WHERE ROWNUM=1";
            }
         }
         else
            sqlStatementString = query + " LIMIT 1";
         System.out.println(sqlStatementString);

         // ********************************************************

         db_resultSet = sqlStatement.executeQuery(sqlStatementString);
         tableMetaData = db_resultSet.getMetaData();

         // Column Names, class, type, and size collection.

         for (int i = 1; i < tableMetaData.getColumnCount() + 1; i++)
         {
            colNameString = tableMetaData.getColumnLabel(i);

            // Additional Information about each column.
            columnClass = tableMetaData.getColumnClassName(i);
            columnType = tableMetaData.getColumnTypeName(i);
            columnSize = Integer.valueOf(tableMetaData.getColumnDisplaySize(i));

            System.out.println(i + " " + colNameString + " " +
                               columnClass + " " + columnType + " " +
                               columnSize);

            // This going to be a problem so skip this column.
            // NOT TESTED. This is still problably not going to
            // help. Bound to crash in loadTable().

            if (columnClass == null && columnType == null)
               continue;

            // Handle some Oracle data types that have a null
            // class type and possibly others.

            if (columnClass == null)
            {
               if (columnType.equals("BINARY_FLOAT")
                   && dataSourceType.equals(ConnectionManager.ORACLE))
               {
                  columnClass = "java.lang.Float";
                  columnType = "FLOAT";
               }
               else if (columnType.equals("BINARY_DOUBLE")
                        && dataSourceType.equals(ConnectionManager.ORACLE))
               {
                  columnClass = "java.lang.Double";
                  columnType = "DOUBLE";
               }
               else
                  columnClass = columnType;
            }

            columnNameFields.add(colNameString);
            columnClassHashMap.put(colNameString, columnClass);
            columnTypeHashMap.put(colNameString, columnType.toUpperCase());
            columnSizeHashMap.put(colNameString, columnSize);
         }
      }
      catch (SQLException e)
      {
         String errorString = "CSVQueryDataDump.getColumnNames() SQLException: " + e.getMessage()
                              + " " + "SQLState: " + e.getSQLState() + " " + "VendorError: "
                              + e.getErrorCode();
         JOptionPane.showMessageDialog(null, errorString, "Query Failure", JOptionPane.ERROR_MESSAGE);
         return;
      }
      finally
      {
         try
         {
            if (db_resultSet != null)
               db_resultSet.close();
         }
         catch (SQLException sqle)
         {
            String errorString = "CSVQueryDataDump.getColumnNames() SQLException: " + sqle.getMessage();
            JOptionPane.showMessageDialog(null, errorString, "Failed to Close ResultSet",
                                          JOptionPane.ERROR_MESSAGE);
         }
         finally
         {
            if (sqlStatement != null)
               sqlStatement.close();
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