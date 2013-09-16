//=================================================================
//                     MyJSQLView SQLQuery
//=================================================================
//
//    This class provides the means to collect in a generic manner
// the characteristics of a SQL query.
//
//                     << SQLQuery.java >>
//
//=================================================================
// Copyright (C) 2005-2013 Dana M. Proctor
// Version 1.0 09/16/2013
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
// Version 1.0 Original Initial SQLQuery Class.
//        
//-----------------------------------------------------------------
//                 danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview.utilities;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import com.dandymadeproductions.myjsqlview.datasource.ConnectionManager;

/**
 *    The SQLQuery class provides the means to collect in a generic manner
 * the characteristics of a SQL query.   
 * 
 * @author Dana M. Proctor
 * @version 1.0 09/16/2013
 */

public class SQLQuery
{
   // Class Instances.
   private String sqlString;
   private int validQuery;

   private int tableRowLimit;
   private String dataSourceType;
   
   private ArrayList<String> tableHeadings;
   private HashMap<String, String> columnNamesHashMap;
   private HashMap<String, String> columnClassHashMap;
   private HashMap<String, String> columnTypeHashMap;
   private HashMap<String, Integer> columnSizeHashMap;
   
   //==============================================================
   // SQLQuery Constructor
   //==============================================================

   public SQLQuery(String sqlString, int queryRowLimit)
   {
      this.sqlString = sqlString;
      tableRowLimit = queryRowLimit;
      
      // Setting up a data source name qualifier and other
      // instances.
      
      dataSourceType = ConnectionManager.getDataSourceType();
      validQuery = -1;
      
      tableHeadings = new ArrayList <String>();
      columnNamesHashMap = new HashMap <String, String>();
      columnClassHashMap = new HashMap <String, String>();
      columnTypeHashMap = new HashMap <String, String>();
      columnSizeHashMap = new HashMap <String, Integer>();
   }
         
   //==============================================================
   // Class method to execute the given user's input SQL statement.
   //==============================================================

   public int executeSQL() throws SQLException
   {
      // Method Instances
      Connection dbConnection;
      String sqlStatementString;
      Statement sqlStatement;
      int updateCount;
      ResultSet db_resultSet;
      ResultSetMetaData tableMetaData;

      String colNameString;
      String columnClass, columnType;
      int columnSize;
      
      // Checking to see if anything in the input to
      // execute.
      
      if (sqlString.length() < 1)
         return validQuery;

      // Setting up a connection.
      dbConnection = ConnectionManager.getConnection("SQLQuery executeSQL()");
      
      if (dbConnection == null)
         return validQuery;
      
      // Connecting to the data base, to obtain
      // meta data, and column names.
      
      sqlStatement = null;
      
      try
      {
         sqlStatement = dbConnection.createStatement();
         sqlStatement.setMaxRows(tableRowLimit);

         sqlStatementString = sqlString;
         // System.out.println(sqlStatementString);
         
         sqlStatement.execute(sqlStatementString);
         updateCount = sqlStatement.getUpdateCount();
         
         // Collect results.
         if (updateCount == -1)
         {
            db_resultSet = sqlStatement.getResultSet();
            
            // Check to see if there are any results.
            
            if (db_resultSet == null)
            {
               // Fill information instances.
               colNameString = "Result";
               columnClass = "java.lang.String";
               columnType = "VARCHAR";
               columnSize = 30;
               
               tableHeadings.add(colNameString);
               columnNamesHashMap.put(colNameString, colNameString);
               columnClassHashMap.put(colNameString, columnClass);
               columnTypeHashMap.put(colNameString, columnType.toUpperCase());
               columnSizeHashMap.put(colNameString, Integer.valueOf(columnSize));
               
               validQuery = 0;
               return validQuery;
            }
            
            // Have results so setting Up the column names, and collecting
            // information about columns.
            
            tableMetaData = db_resultSet.getMetaData();

            for (int i = 1; i < tableMetaData.getColumnCount() + 1; i++)
            {
               colNameString = tableMetaData.getColumnLabel(i);
               columnClass = tableMetaData.getColumnClassName(i);
               columnType = tableMetaData.getColumnTypeName(i);
               columnSize = tableMetaData.getColumnDisplaySize(i);

               // System.out.println(i + " " + colNameString + " " +
               //                      columnClass + " " + columnType + " " +
               //                      columnSize);

               // This going to be a problem so skip these columns.
               // NOT TESTED. This is still problably not going to
               // help. Bound to crash later.

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

               tableHeadings.add(colNameString);
               columnNamesHashMap.put(colNameString, colNameString);
               columnClassHashMap.put(colNameString, columnClass);
               columnTypeHashMap.put(colNameString, columnType.toUpperCase());
               columnSizeHashMap.put(colNameString, Integer.valueOf(columnSize));
            }
            db_resultSet.close();
            
            // Looks good so validate.
            validQuery = 1; 
         }
         // No results, data, but was update, maybe.
         else
         {
            validQuery = 0;
         }
         return validQuery;
      }
      catch (SQLException e)
      {
         ConnectionManager.displaySQLErrors(e, "SQLQuery executeSQL()");
         validQuery = -1;
         return validQuery;
      }
      finally
      {
         if (sqlStatement != null)
            sqlStatement.close();
         
         ConnectionManager.closeConnection(dbConnection, "SQLQuery executeSQL()");    
      }
   }
   
   //==============================================================
   // Class method to allow classes to obtain the list of allowed
   // column names that is presently in the summary table.
   //==============================================================

   public ArrayList<String> getTableHeadings()
   {
      return tableHeadings;
   }

   //==============================================================
   // Class method to allow classes to obtain the columnNamesHashMap.
   //==============================================================

   public HashMap<String, String> getColumnNamesHashMap()
   {
      return columnNamesHashMap;
   }

   //==============================================================
   // Class method to allow classes to obtain the columnClassHashMap.
   //==============================================================

   public HashMap<String, String> getColumnClassHashMap()
   {
      return columnClassHashMap;
   }

   //==============================================================
   // Class method to allow classes to obtain the columnTypeHashMap.
   //==============================================================

   public HashMap<String, String> getColumnTypeHashMap()
   {
      return columnTypeHashMap;
   }

   //==============================================================
   // Class method to allow classes to obtain the columnSizeHashMap.
   //==============================================================

   public HashMap<String, Integer> getColumnSizeHashMap()
   {
      return columnSizeHashMap;
   }
}