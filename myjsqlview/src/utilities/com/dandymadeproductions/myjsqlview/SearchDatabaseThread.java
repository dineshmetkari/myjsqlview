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
// Copyright (C) 2005-2010 Dana M. Proctor.
// Version 1.7 02/18/2010
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
//         
//-----------------------------------------------------------------
//                  danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview;

import javax.swing.*;
import java.sql.*;
import java.util.Vector;

/**
 *    The SearchDatabaseThread class provides a thread to search through
 * all the database tables for a given input string.
 * 
 * @author Dana Proctor
 * @version 1.7 02/18/2010
 */

class SearchDatabaseThread implements Runnable
{
   // Class Instances
   Thread searchThread;
   private Vector databaseTables;
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

   SearchDatabaseThread(Vector databaseTables, String searchString,
                        JProgressBar progressBar, JButton searchCompleteButton)
   {
      this.databaseTables = databaseTables;
      this.searchQueryString = searchString;
      this.searchProgressBar = progressBar;
      this.searchCompleteButton = searchCompleteButton;

      cancelSearch = false;

      // Create and start the class thread.
      searchThread = new Thread(this, "SearchDatabaseThread");
      //System.out.println("SearchDatabaseThread Thread");

      searchThread.start();
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

      dbConnection = MyJSQLView_Access.getConnection("SearchDatabaseThread queryDatabase()");
      
      if (dbConnection == null)
         return;

      // Setting up various instances needed.
      
      identifierQuoteString = MyJSQLView_Access.getIdentifierQuoteString();
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
         
         // Properly format the string used in the query
         // for the table.

         sqlTable = databaseTables.get(index).toString();

         if (sqlTable.indexOf(".") != -1)
         {
            schemaTableName = identifierQuoteString
                              + sqlTable.substring(0, sqlTable.indexOf("."))
                              + identifierQuoteString + "." + identifierQuoteString
                              + sqlTable.substring(sqlTable.indexOf(".") + 1)
                              + identifierQuoteString;
         }
         else
            schemaTableName = identifierQuoteString + sqlTable + identifierQuoteString;
         //System.out.println(schemaTableName);

         // Create the search query.
         columnsSQLQuery = createColumnsSQLQuery(dbConnection, schemaTableName,
                                                 searchQueryString);

         // Problems creating the search, columns, query will be 
         // return as a empty string so go to next table, but still
         // allow the table result to be displayed, will be invalid
         // -1.
         
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
         //System.out.println(searchQuery);
         searchProgressBar.setValue(index + 1);

         // Inner class to execute the query as a new thread.
         tableSearchThreads[i] = new Thread(new Runnable()
         {
            public void run()
            {
               try
               {
                  Statement sqlStatement = dbConnection.createStatement();
                  ResultSet rs = sqlStatement.executeQuery(searchQuery);
                  while (rs.next())
                  {
                     int resultCount = rs.getInt(1);
                     tableSearchResultCounts[index] = resultCount;
                     if (resultCount > 0)
                        resultsCount++;
                  }
                  rs.close();
                  sqlStatement.close();
               }
               catch (SQLException e)
               {
                  resultsCount++;
                  //System.out.println(e);
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
      
      resultData = new Object[resultsCount][2];
      
      int j = 0;
      int k = 0;
      
      while (j < databaseTables.size() && k < resultsCount)
      {
         if (tableSearchResultCounts[j] != 0)
         {
            resultData[k][0] = databaseTables.get(j);
            resultData[k++][1] = new Integer(tableSearchResultCounts[j]);
         }
         j++;
      }
      
      searchCompleteButton.doClick();
      MyJSQLView_Access.closeConnection(dbConnection, "SearchDatabaseThread queryDatabase()");
   }
   
   //==============================================================
   // Class Method to create the given input table columns search
   // SQL query LIKE clause.
   //==============================================================

   private String createColumnsSQLQuery(Connection dbConnection, String tableName,
                                        String searchQueryString)
   {
      // Method Instances
      Statement sqlStatement;
      ResultSet resultSet;
      ResultSetMetaData tableMetaData;

      StringBuffer columnsSQLQuery;
      String dbType, sqlColumnSelectString;
      String identifierQuoteString;

      // Setting up.
      columnsSQLQuery = new StringBuffer();
      identifierQuoteString = MyJSQLView_Access.getIdentifierQuoteString();

      // Beginning creating the table columns
      // search string query.
      try
      {
         sqlStatement = dbConnection.createStatement();

         // Create query to obtain the tables Table Meta Data.
         dbType = MyJSQLView_Access.getSubProtocol();

         // HSQL
         if (dbType.indexOf("hsql") != -1)
            sqlColumnSelectString = "SELECT LIMIT 0 1 * FROM " + tableName;
         // Oracle
         else if (dbType.indexOf("oracle") != -1)
            sqlColumnSelectString = "SELECT * FROM " + tableName + " WHERE ROWNUM=1";
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

            // Exclude binary & file column types.
            if (columnType.indexOf("BLOB") == -1 && columnType.indexOf("BYTEA") == -1
                && columnType.indexOf("BYTEA") == -1 && columnType.indexOf("BINARY") == -1
                && columnType.indexOf("RAW") == -1 && columnType.indexOf("FILE") == -1)
            {
               if (dbType.equals("postgresql"))
               {
                  columnsSQLQuery.append(identifierQuoteString + columnName + identifierQuoteString
                                     + "::TEXT LIKE \'%" + searchQueryString + "%\' OR ");
               }
               else
               {
                  columnsSQLQuery.append(identifierQuoteString + columnName + identifierQuoteString
                                     + " LIKE \'%" + searchQueryString + "%\' OR ");
               }
            }
         }
         sqlStatement.close();
         if (columnsSQLQuery.length() > 4)
            return (columnsSQLQuery.delete((columnsSQLQuery.length() - 4),
                                          columnsSQLQuery.length())).toString();
         else
            return "";

      }
      catch (SQLException e)
      {
         MyJSQLView_Access.displaySQLErrors(e, "SearchDatabaseThread createColumnSQLQuery()");
         return "";
      }
   }
   
   //==============================================================
   // Class Method to allow the cancelation of the search.
   //==============================================================
   
   protected void cancel()
   {
      cancelSearch = true;;
   }
   
   //==============================================================
   // Class Method for package classes to obtain the resultant data
   // created by the search of the database. Could be NULL so make
   // a check.
   //==============================================================

   protected Object[][] getResultData()
   {
      return resultData;
   }
}