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
// Version 1.4 09/29/2013
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
//         1.1 Changed Class Name tableHeadings to columnNames and columnTypeHashMap
//             to columnTypeNameHashMap. Removed Class Instance columnNamesHashMap
//             & Added Class Instances columnSQLType/Precision/ScaleHashMap. Added
//             Instances in executeSQL() to Handle Collections for Additional HashMaps.
//             Introduced Using SQL Types in Same Method. Added Getter Methods for
//             New HashMaps.
//         1.2 Added Two Additional Constructors & Removed Gathering dataSourceType
//             Directly From the ConnectionManager, Unless Default Cases.
//         1.3 Added Class Instances columnIsNullableHashMap & columnIsAutoIncrementHashMap.
//             Assignment in executeSQL() Method for New Instances & Getter/Setter
//             Methods.
//         1.4 Corrected in Methods getColumnScale/PrecistionHashMap the Appropriate
//             Instances. Commented System.out in executeSQL().
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
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;

import com.dandymadeproductions.myjsqlview.datasource.ConnectionManager;

/**
 *    The SQLQuery class provides the means to collect in a generic manner
 * the characteristics of a SQL query.   
 * 
 * @author Dana M. Proctor
 * @version 1.4 09/29/2013
 */

public class SQLQuery
{
   // Class Instances.
   private String sqlString;
   private int validQuery;

   private int tableRowLimit;
   private String dataSourceType;
   
   private ArrayList<String> columnNames;
   private HashMap<String, String> columnClassHashMap;
   private HashMap<String, Integer> columnSQLTypeHashMap;
   private HashMap<String, String> columnTypeNameHashMap;
   private HashMap<String, Integer> columnPrecisionHashMap;
   private HashMap<String, Integer> columnScaleHashMap;
   private HashMap<String, Integer> columnSizeHashMap;
   private HashMap<String, Integer> columnIsNullableHashMap;
   private HashMap<String, Boolean> columnIsAutoIncrementHashMap;
   
   //==============================================================
   // SQLQuery Constructors
   //==============================================================

   public SQLQuery(String sqlString)
   {
      this(sqlString, ConnectionManager.getDataSourceType(), 1);
   }
   
   public SQLQuery(String sqlString, String dataSourceType)
   {
      this(sqlString, dataSourceType, 1);
   }
   
   public SQLQuery(String sqlString, String dataSourceType, int queryRowLimit)
   {
      this.sqlString = sqlString;
      tableRowLimit = queryRowLimit;
      
      // Setting up a data source name qualifier and other
      // instances.
      
      this.dataSourceType = dataSourceType;
      validQuery = -1;
      
      columnNames = new ArrayList <String>();
      columnClassHashMap = new HashMap <String, String>();
      columnSQLTypeHashMap = new HashMap <String, Integer>();
      columnTypeNameHashMap = new HashMap <String, String>();
      columnScaleHashMap = new HashMap <String, Integer>();
      columnPrecisionHashMap = new HashMap <String, Integer>();
      columnSizeHashMap = new HashMap <String, Integer>();
      columnIsNullableHashMap = new HashMap <String, Integer>();
      columnIsAutoIncrementHashMap = new HashMap <String, Boolean>();
   }
         
   //==============================================================
   // Class method to execute the given user's input SQL statement.
   // Output:
   //
   // Invalid Query - (-1)
   // Update No Results - (0)
   // Results - (1)
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
      String columnClass, columnTypeName;
      int columnType, columnScale, columnPrecision, columnSize, columnIsNullable;
      boolean columnIsAutoIncrement;
      
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
               columnType = Types.VARCHAR;
               columnTypeName = "VARCHAR";
               columnScale = 0;
               columnPrecision = 0;
               columnSize = 30;
               columnIsNullable = ResultSetMetaData.columnNoNulls;
               columnIsAutoIncrement = false;
               
               columnNames.add(colNameString);
               columnClassHashMap.put(colNameString, columnClass);
               columnSQLTypeHashMap.put(colNameString, new Integer(Types.VARCHAR));
               columnTypeNameHashMap.put(colNameString, columnTypeName.toUpperCase());
               columnScaleHashMap.put(colNameString, Integer.valueOf(columnScale));
               columnPrecisionHashMap.put(colNameString, Integer.valueOf(columnPrecision));
               columnSizeHashMap.put(colNameString, Integer.valueOf(columnSize));
               columnIsNullableHashMap.put(colNameString, Integer.valueOf(columnIsNullable));
               columnIsAutoIncrementHashMap.put(colNameString, Boolean.valueOf(columnIsAutoIncrement));
               
               validQuery = 0;
               return validQuery;
            }
            
            // Have results so setting Up the column names, and collecting
            // information about columns.
            
            tableMetaData = db_resultSet.getMetaData();
            
            // System.out.println("index" + "\t" + "Name" + "\t" + "Class" + "\t"
            //                     + "Type" + "\t" + "Type Name" + "\t" + "Scale"
            //                     + "\t" + "Precision" + "\t" + "Size");

            for (int i = 1; i < tableMetaData.getColumnCount() + 1; i++)
            {
               colNameString = tableMetaData.getColumnLabel(i);
               columnClass = tableMetaData.getColumnClassName(i);
               columnType = tableMetaData.getColumnType(i);
               columnTypeName = tableMetaData.getColumnTypeName(i);
               columnScale = tableMetaData.getScale(i);
               columnPrecision = tableMetaData.getPrecision(i);
               columnSize = tableMetaData.getColumnDisplaySize(i);
               columnIsNullable = tableMetaData.isNullable(i);
               columnIsAutoIncrement = tableMetaData.isAutoIncrement(i);
               
               // System.out.println(i + "\t" + colNameString + "\t" +
               //                        columnClass + "\t" + columnType + "\t" +
               //                        columnTypeName + "\t" + columnScale + "\t" +
               //                        columnPrecision + "\t" + columnSize);

               // This going to be a problem so skip these columns.
               // NOT TESTED. This is still problably not going to
               // help. Bound to crash later.

               if (columnClass == null && columnTypeName == null)
                  continue;

               // Handle some Oracle data types that have a null
               // class type and possibly others.

               if (columnClass == null)
               {
                  if (columnTypeName.toUpperCase().equals("BINARY_FLOAT")
                      && dataSourceType.equals(ConnectionManager.ORACLE))
                  {
                     columnClass = "java.lang.Float";
                     columnType = Types.FLOAT;
                     columnTypeName = "FLOAT";
                  }
                  else if (columnTypeName.toUpperCase().equals("BINARY_DOUBLE")
                           && dataSourceType.equals(ConnectionManager.ORACLE))
                  {
                     columnClass = "java.lang.Double";
                     columnType = Types.DOUBLE;
                     columnTypeName = "DOUBLE";
                  }
                  else
                     columnClass = "java.lang.Object";
               }

               columnNames.add(colNameString);
               columnClassHashMap.put(colNameString, columnClass);
               columnSQLTypeHashMap.put(colNameString, Integer.valueOf(columnType));
               columnTypeNameHashMap.put(colNameString, columnTypeName.toUpperCase());
               columnScaleHashMap.put(colNameString, Integer.valueOf(columnScale));
               columnPrecisionHashMap.put(colNameString, Integer.valueOf(columnPrecision));
               columnSizeHashMap.put(colNameString, Integer.valueOf(columnSize));
               columnIsNullableHashMap.put(colNameString, Integer.valueOf(columnIsNullable));
               columnIsAutoIncrementHashMap.put(colNameString, Boolean.valueOf(columnIsAutoIncrement));
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
   // Class method to allow classes to obtain the list of column
   // names that is the result of the query.
   //==============================================================

   public ArrayList<String> getColumnNames()
   {
      return columnNames;
   }

   //==============================================================
   // Class method to allow classes to obtain the column, Java
   // classes, HashMap.
   //==============================================================

   public HashMap<String, String> getColumnClassHashMap()
   {
      return columnClassHashMap;
   }
   
   //==============================================================
   // Class method to allow classes to obtain the column, SQL
   // types, HashMap.
   //==============================================================

   public HashMap<String, Integer> getColumnSQLTypeHashMap()
   {
      return columnSQLTypeHashMap;
   }

   //==============================================================
   // Class method to allow classes to obtain the column, database
   // names, HashMap.
   //==============================================================

   public HashMap<String, String> getColumnTypeNameHashMap()
   {
      return columnTypeNameHashMap;
   }
   
   //==============================================================
   // Class method to allow classes to obtain the column scales
   // HashMap.
   //==============================================================

   public HashMap<String, Integer> getColumnScaleHashMap()
   {
      return columnScaleHashMap;
   }
   
   //==============================================================
   // Class method to allow classes to obtain the column precisions
   // HashMap.
   //==============================================================

   public HashMap<String, Integer> getColumnPrecisionHashMap()
   {
      return columnPrecisionHashMap;
   }

   //==============================================================
   // Class method to allow classes to obtain the column sizes
   // HashMap.
   //==============================================================

   public HashMap<String, Integer> getColumnSizeHashMap()
   {
      return columnSizeHashMap;
   }
   
   //==============================================================
   // Class method to allow classes to obtain the column nullable
   // HashMap.
   //==============================================================

   public HashMap<String, Integer> getColumnIsNullableHashMap()
   {
      return columnIsNullableHashMap;
   }
   
   //==============================================================
   // Class method to allow classes to obtain the column auto
   // increment HashMap.
   //==============================================================

   public HashMap<String, Boolean> getColumnIsAutoIncrementHashMap()
   {
      return columnIsAutoIncrementHashMap;
   }
}