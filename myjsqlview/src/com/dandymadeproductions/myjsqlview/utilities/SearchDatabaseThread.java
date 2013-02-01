//=================================================================
//                  SearchDatabaseThread Class
//=================================================================
//
//    This class provides a thread to search through all the
// database tables for a given input string..
//
//                << SearchDatabaseThread.java >>
//
//=================================================================
// Copyright (C) 2005-2013 Dana M. Proctor
// Version 3.9 09/20/2012
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
// Version 1.0 Original SearchDatabase Class.
//         1.1 Changed Name to SearchDatabaseThread. Implemented Runnable.
//             Replaced Class Method queryDatabase() With run(). Added
//             Class Methods cancel() & getResultData(). Cleaned Up,
//             Created Robustness, and Finalized Code.
//         1.2 Scrubbed, Debugger Was Giving an Error for Thread if Class
//             Was Extended.
//         1.3 Conditional Check for Null dbConnection in run() Method.
//         1.4 Corrected Oversite of Not Incrementing do while Loop Counter
//             i in run() Method When columnsSQLQuery Returned As Empty
//             String.
//         1.5 Header Format Changes/Update.
//         1.6 Class Method createColumnsSQLQuery() Class Instance columnsSQLQuery
//             Changed From String to StringBuffer.
//         1.7 Changed Package to Reflect Dandy Made Productions Code.
//         1.8 Class Method createColumnsSQLQuery() Excluded NOT RAW, But LONG Types
//             Which Are Both Oracle Data Types. Modified in Same the Ability to
//             Properly Search DATE Data Types for Oracle. Organized imports.
//         1.9 Parameterized Class Instance databaseTables and Same in Argument for
//             Constructor to Bring Code Into Compliance With Java 5.0 API.
//         2.0 Method run() Changed resultData Assignment to Integer.valueOf() 
//             Instead of new Integer().
//         2.1 Collected Class Istance schemaTableName in run() From MyJSQLView_Utils
//             Method getSchemaTableName().
//         2.2 Added boolean[] selectedTables to Constructor. Implemented an Additional
//             Condition in do Loop in Method run() for Excluding Search of Tables As
//             Defined by New Class Instance selectedTables.
//         2.3 Class Method run() Assignment of Boolean Object to resultData via
//             Boolean.valueOf() Instead of Creating a New Boolean Object.
//         2.4 Class Method run() Cast Object Returned by MyJSQLView_Access.
//             getConnection() to Connection.
//         2.5 Changes to Class Methods run() & createColumnSQLQuery() to Use Newly
//             Redefined ConnectionManager to Derive Connections & Display SQL Errors.
//             Also identifierQuoteString Collected From ConnectionManager. Changed
//             dbType to subProtocol in Class Method createColumnSQLQuery().
//         2.6 Class Method createColumnsSQLQuery() Corrections to Handle Date, Datetime,
//             & Timestamp for searchQueryString.
//         2.7 Format Change in TO_DATE() Function for Oracle to YYYY-MM-dd in Class
//             Method createColumnSQLQuery().
//         2.8 Class Method createColumnsSQLQuery() Replaced Instance subProtocol With
//             dataSourceType.
//         2.9 Optimized by Not Processing Non-Selected Tables for Search in run().
//             Correction in Class Method createColumnsSQLQuery() for Oracle columnsSQLQuery
//             String Creation.
//         3.0 Copyright Update.
//         3.1 Removed the Casting of (Connection) for the Returned Instance for the
//             ConnectionManager.getConnection() in run().
//         3.2 Class Method run() Added a try catch for createColumnsSQLQuery() Which
//             Now Throws SQLExeception for finally to Close sqlStatement. Additional
//             Try to Close Also sqlStatement in Inner Class tableSearchThreads.
//         3.3 Changes in Method createColumnsSQLQuery() to Address Possible Incorrect
//             Results for Search of Date Fields. Additional Conditional Checks.
//         3.4 Changed Class Instance databaseTables & Corresponding Argument in Constructor
//             from Vector to ArrayList.
//         3.5 Closure for ResultSet, rs, in run() & createColumnsSQLQuery() Moved to
//             finally.
//         3.6 Class Method run() Removal of Redundant Closing of rs & sqlStatement
//             Before finally.
//         3.7 Changed Package Name to com.dandymadeproductions.myjsqlview.utilities.
//             Made Class, Constructor, & Class Methods cancel() & getResultData()
//             Public.
//         3.8 Class Method getResultData() Return a Clone of resultData.
//         3.9 Constructor Argument selectedTables Cloned. Removed Starting of Thread
//             in Constructor.
//         
//-----------------------------------------------------------------
//                  danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview.utilities;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JProgressBar;

import com.dandymadeproductions.myjsqlview.datasource.ConnectionManager;

/**
 *    The SearchDatabaseThread class provides a thread to search through
 * all the database tables for a given input string.
 * 
 * @author Dana Proctor
 * @version 3.9 09/20/2012
 */

public class SearchDatabaseThread implements Runnable
{
   // Class Instances
   private ArrayList<String> databaseTables;
   private boolean[] selectedTables;
   private String searchQueryString;
   private JProgressBar searchProgressBar;
   private JButton searchCompleteButton;
   
   private int[] tableSearchResultCounts;
   private int resultsCount;
   private Object[][] resultData;
   private boolean cancelSearch;

   //==============================================================
   // SearchDatabaseThread Constructor
   //==============================================================

   public SearchDatabaseThread(ArrayList<String> databaseTables, boolean[] selectedTables, String searchString,
                               JProgressBar progressBar, JButton searchCompleteButton)
   {
      this.databaseTables = databaseTables;
      this.selectedTables = selectedTables.clone();
      this.searchQueryString = searchString;
      this.searchProgressBar = progressBar;
      this.searchCompleteButton = searchCompleteButton;

      cancelSearch = false;
   }
   
   //==============================================================
   // Class method for normal start of the thread
   //==============================================================

   public void run()
   {
      // Method Instances
      final Connection dbConnection;

      Thread[] tableSearchThreads;
      String columnsSQLQuery, sqlTable;
      String identifierQuoteString, schemaTableName;

      dbConnection = ConnectionManager.getConnection("SearchDatabaseThread queryDatabase()");
      
      if (dbConnection == null)
         return;

      // Setting up various instances needed.
      
      identifierQuoteString = ConnectionManager.getIdentifierQuoteString();
      tableSearchThreads = new Thread[databaseTables.size()];
      tableSearchResultCounts = new int[databaseTables.size()];

      // Fill search count results array with -1 so we know when
      // there is problem with the query for the table.
      
      for (int i = 0; i < tableSearchResultCounts.length; i++)
         tableSearchResultCounts[i] = -1;

      // =====================================================
      // Begin cycling through the tables, creating the search
      // query and executing each as a separate thread.

      resultsCount = 0;
      int i = 0;
      
      do
      {
         final int index = i;
         final String searchQuery;
         
         // Optimize by not bothering with excluded
         // tables.
         
         if (selectedTables[i] == false)
         {
            resultsCount++;
            i++;
            continue;
         }
         
         // Properly format the string used in the query
         // for the table.

         sqlTable = databaseTables.get(index);
         schemaTableName = MyJSQLView_Utils.getSchemaTableName(sqlTable);

         // Create the search query.
         columnsSQLQuery = "";
         
         try
         {
            columnsSQLQuery = createColumnsSQLQuery(dbConnection, schemaTableName,
                                                    searchQueryString);
         }
         catch (SQLException e)
         {
            ConnectionManager.displaySQLErrors(e, "SearchDatabaseThread run()");
         }

         // Problems creating the search, columns, query will be 
         // return as a empty string or table to be not searched
         // so go to next table, but still allow the table result
         // to be displayed, Will be invalid -1.
         
         if (columnsSQLQuery.equals(""))
         {
            resultsCount++;
            i++;
            continue;
         }

         // Actual complete search query.
         searchQuery = "SELECT COUNT(*) AS " + identifierQuoteString + "Count"
                       + identifierQuoteString + " FROM " + schemaTableName
                       + " WHERE " + columnsSQLQuery;
         // System.out.println(searchQuery);
         searchProgressBar.setValue(index + 1);

         // Inner class to execute the query as a new thread.
         tableSearchThreads[i] = new Thread(new Runnable()
         {
            public void run()
            {
               Statement sqlStatement = null;
               ResultSet rs = null;
               
               try
               {
                  sqlStatement = dbConnection.createStatement();
                  rs = sqlStatement.executeQuery(searchQuery);
                  while (rs.next())
                  {
                     int resultCount = rs.getInt(1);
                     tableSearchResultCounts[index] = resultCount;
                     if (resultCount > 0)
                        resultsCount++;
                  }
               }
               catch (SQLException e)
               {
                  // System.out.println(e);
                  
                  resultsCount++;
               }
               finally
               {
                  try
                  {
                     if (rs != null)
                        rs.close();
                  }
                  catch (SQLException sqle)
                  {
                     ConnectionManager.displaySQLErrors(sqle, "SearchDatabaseThread run()");
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
                        ConnectionManager.displaySQLErrors(sqle, "SearchDatabaseThread run()");
                     }
                  }
               }
            }
         });
         tableSearchThreads[i].start();
         i++;
      }
      while (i < databaseTables.size() && !cancelSearch);

      // ===============================================
      // Queries now execute via a new thread for each,
      // so wait for all to complete.
      
      for (int j = 0; j < databaseTables.size(); j++)
      {
         try
         {
            if (tableSearchThreads[j] != null)
               tableSearchThreads[j].join();
         }
         catch (Exception e)
         {

         }
      }

      // =================================
      // Create data object with results.
      
      resultData = new Object[resultsCount][3];
      
      int j = 0;
      int k = 0;
      
      while (j < databaseTables.size() && k < resultsCount)
      {
         if (tableSearchResultCounts[j] != 0)
         {
            resultData[k][0] = Boolean.valueOf(selectedTables[j]);
            resultData[k][1] = databaseTables.get(j);
            resultData[k++][2] = Integer.valueOf(tableSearchResultCounts[j]);
         }
         j++;
      }
      
      searchCompleteButton.doClick();
      ConnectionManager.closeConnection(dbConnection, "SearchDatabaseThread queryDatabase()");
   }
   
   //==============================================================
   // Class Method to create the given input table columns search
   // SQL query LIKE clause.
   //==============================================================

   private String createColumnsSQLQuery(Connection dbConnection, String tableName,
                                        String searchQueryString) throws SQLException
   {
      // Method Instances
      Statement sqlStatement;
      ResultSet resultSet;
      ResultSetMetaData tableMetaData;

      StringBuffer columnsSQLQuery;
      String dataSourceType, sqlColumnSelectString;
      String identifierQuoteString;

      // Setting up.
      columnsSQLQuery = new StringBuffer();
      identifierQuoteString = ConnectionManager.getIdentifierQuoteString();

      // Beginning creating the table columns
      // search string query.
      
      sqlStatement = null;
      resultSet = null;
      
      try
      {
         sqlStatement = dbConnection.createStatement();

         // Create query to obtain the tables Table Meta Data.
         dataSourceType = ConnectionManager.getDataSourceType();
         
         // HSQL
         if (dataSourceType.equals(ConnectionManager.HSQL))
            sqlColumnSelectString = "SELECT LIMIT 0 1 * FROM " + tableName;
         // Oracle
         else if (dataSourceType.equals(ConnectionManager.ORACLE))
            sqlColumnSelectString = "SELECT * FROM " + tableName + " WHERE ROWNUM=1";
         // MS Access
         else if (dataSourceType.equals(ConnectionManager.MSACCESS))
            sqlColumnSelectString = "SELECT * FROM " + tableName;
         // MySQL, PostgreSQL, & Others
         else
            sqlColumnSelectString = "SELECT * FROM " + tableName + " LIMIT 1";

         // System.out.println(sqlColumnSelectString);
         resultSet = sqlStatement.executeQuery(sqlColumnSelectString);
         tableMetaData = resultSet.getMetaData();

         // Cycling through the table's columns and adding
         // to the SQL search query string. Exclude any coulumn
         // that is not binary in nature.

         for (int k = 1; k < tableMetaData.getColumnCount() + 1; k++)
         {
            // Collect Information on Column.
            String columnType = tableMetaData.getColumnTypeName(k).toUpperCase();
            String columnName = tableMetaData.getColumnName(k);
            // System.out.println(columnName + " " + columnType);
            
            String searchString = searchQueryString;

            // Exclude binary & file column types.
            if (columnType.indexOf("BLOB") == -1 && columnType.indexOf("BYTEA") == -1
                && columnType.indexOf("BYTEA") == -1 && columnType.indexOf("BINARY") == -1
                && columnType.indexOf("LONG") == -1 && columnType.indexOf("FILE") == -1)
            {
               // Convert date, datetime, timestamp search string
               // to proper format.
               if (columnType.equals("DATE"))
               {
                  if (dataSourceType.equals(ConnectionManager.ORACLE))
                  {
                     searchString = MyJSQLView_Utils.processDateFormatSearch(searchString);
                     
                     // Something not right in conversion.
                     if (searchString.equals("0") || searchString.equals(searchQueryString))
                     {
                        searchString = searchQueryString;
                        // Do not process as DATE.
                        columnType = "";
                     }
                  }
                  else
                  {
                     searchString = MyJSQLView_Utils.processDateFormatSearch(searchString);
                     
                     // Something not right in conversion.
                     if (searchString.equals("0"))
                        searchString = searchQueryString;
                  }
               }
               else if (columnType.equals("DATETIME") || columnType.equals("TIMESTAMP") ||
                        columnType.equals("TIMESTAMPTZ"))
               {
                  if (searchString.indexOf(" ") != -1)
                     searchString = MyJSQLView_Utils.processDateFormatSearch(
                        searchString.substring(0, searchString.indexOf(" ")))
                        + searchString.substring(searchString.indexOf(" "));
                  else if (searchString.indexOf("-") != -1 || searchString.indexOf("/") != -1)
                     searchString = MyJSQLView_Utils.processDateFormatSearch(searchString);
               }
               
               // Create search query.
               if (dataSourceType.equals(ConnectionManager.POSTGRESQL))
                  columnsSQLQuery.append(identifierQuoteString + columnName + identifierQuoteString
                                     + "::TEXT LIKE \'%" + searchString + "%\' OR ");
               else if (dataSourceType.equals(ConnectionManager.ORACLE))
               {
                  if (columnType.equals("DATE"))
                     columnsSQLQuery.append(identifierQuoteString + columnName + identifierQuoteString
                                            + " LIKE TO_DATE(\'" + searchString + "\', "
                                            + "\'YYYY-MM-dd\') OR ");
                  else
                     columnsSQLQuery.append(identifierQuoteString + columnName + identifierQuoteString
                        + " LIKE \'%" + searchString + "%\' OR ");     
               }
               else
                  columnsSQLQuery.append(identifierQuoteString + columnName + identifierQuoteString
                     + " LIKE \'%" + searchString + "%\' OR ");   
            }
         }
         
         if (columnsSQLQuery.length() > 4)
            return (columnsSQLQuery.delete((columnsSQLQuery.length() - 4),
                                          columnsSQLQuery.length())).toString();
         else
            return "";
      }
      catch (SQLException e)
      {
         ConnectionManager.displaySQLErrors(e, "SearchDatabaseThread createColumnSQLQuery()");
         return "";
      }
      finally
      {
         try
         {
            if (resultSet != null)
               resultSet.close();
         }
         catch (SQLException sqle)
         {
            ConnectionManager.displaySQLErrors(sqle, "SearchDatabaseThread createColumnSQLQuery()");
         }
         finally
         {
            if (sqlStatement != null)
               sqlStatement.close();
         }
      }
   }
   
   //==============================================================
   // Class Method to allow the cancelation of the search.
   //==============================================================
   
   public void cancel()
   {
      cancelSearch = true;;
   }
   
   //==============================================================
   // Class Method for package classes to obtain the resultant data
   // created by the search of the database. Could be NULL so make
   // a check.
   //==============================================================

   public Object[][] getResultData()
   {
      return resultData.clone();
   }
}
