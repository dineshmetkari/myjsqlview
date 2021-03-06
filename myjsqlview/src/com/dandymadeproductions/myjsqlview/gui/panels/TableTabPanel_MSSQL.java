//=============================================================
//          MyJSQLView Table TabPanel_MSSQL
//=============================================================
//
//    This class provides the means to create a default table
// summary view of data in an MSSQL database that in MyJSQLView
// is listed according to a specified sort and search. Entries
// from the database table may be viewed, added, edited, or
// deleted by means of this panel. The panel also provides the
// mechanism to page through the database table's data.
//
//           << TableTabPanel_MSSQL.java >>
//
//================================================================
// Copyright (C) 2005-2015 Dana M. Proctor
// Version 1.3 02/14/2014
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
// Version 1.0 Original TableTabPanel_MSSQL Class.
//         1.1 Finalized TableTabPanel for MSSQL With Functionality Verified for
//             mssqlTypes, Data Types, Table.
//         1.2 Excluded Binary, Image, & XML Fields From Generic Not Field Specified
//             Search With LIKE Construction in loadTable().
//         1.3 Modification of Query for Advanced Sort/Search to Use the Selected
//             Summary Table Fields for Proper Aggregation Functionality in Method
//             loadTable().
//             
//-----------------------------------------------------------------
//                  danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview.gui.panels;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Iterator;

import com.dandymadeproductions.myjsqlview.MyJSQLView;
import com.dandymadeproductions.myjsqlview.datasource.ConnectionManager;
import com.dandymadeproductions.myjsqlview.datasource.ConnectionProperties;
import com.dandymadeproductions.myjsqlview.gui.forms.TableEntryForm;
import com.dandymadeproductions.myjsqlview.utilities.BlobTextKey;
import com.dandymadeproductions.myjsqlview.utilities.MyJSQLView_Utils;

/**
 *    The TablePanel_MSSQL class provides the means to create a default table
 * summary view of data in an MSSQL database that in MyJSQLView is listed
 * according to a specified sort and search. Entries from the database table
 * may be viewed, added, edited, or deleted by means of this panel. The panel
 * also provides the mechanism to page through the database table's data.
 * 
 * @author Dana M. Proctor
 * @version 1.3 02/14/2014
 */

public class TableTabPanel_MSSQL extends TableTabPanel
{
   // Class Instances.
   private static final long serialVersionUID = -5209105616133901884L;

   //===========================================================
   // TableTabPanel Constructor
   //===========================================================

   public TableTabPanel_MSSQL(String table, Connection setup_dbConnection, boolean viewOnlyTable)
   {
      super(table, setup_dbConnection, viewOnlyTable);
   }
   
   //==============================================================
   // Class method to obtain the column names from the table. The
   // names are modified for display and placed into a map for
   // later use. Additional information about the column, size,
   // type, etc., are also stored away for future use.
   //==============================================================

   public boolean getColumnNames(Connection dbConnection) throws SQLException
   {
      // Method Instances
      String sqlStatementString;
      Statement sqlStatement;
      ResultSet rs, db_resultSet;
      DatabaseMetaData dbMetaData;
      ResultSetMetaData tableMetaData;

      String databaseName, schemaName, tableName;
      String colNameString, comboBoxNameString;
      String columnClass, columnType;
      Integer columnSize;

      // Connecting to the data base, to obtain
      // meta data, and column names.
      
      sqlStatement = null;
      db_resultSet = null;
      rs = null;
      
      try
      {
         sqlStatement = dbConnection.createStatement();
         
         if (sqlTable.indexOf(".") != -1)
         {
            schemaName = sqlTable.substring(0, sqlTable.indexOf("."));
            tableName = sqlTable.substring(sqlTable.indexOf(".") + 1);
         }
         else
         {
            schemaName = "";
            tableName = sqlTable;
         }
         
         databaseName = ConnectionManager.getConnectionProperties().getProperty(ConnectionProperties.DB);
         if (databaseName.indexOf(";") != -1)
            databaseName = databaseName.substring(0, databaseName.indexOf(";"));

         // ====================================================
         // Setting Up the Column Names, Form Fields, ComboBox
         // Text, Hashmaps, Special Fields, & Primary Key(s).

         sqlStatementString = "SELECT TOP 1 * FROM " + schemaTableName;
         // System.out.println(sqlStatementString);

         db_resultSet = sqlStatement.executeQuery(sqlStatementString);

         // Primary Key(s)
         
         dbMetaData = dbConnection.getMetaData();
         tableMetaData = db_resultSet.getMetaData();

         rs = dbMetaData.getPrimaryKeys(databaseName, schemaName, tableName);
         
         while (rs.next())
         {
            if (rs.getString("COLUMN_NAME").indexOf("chunk") == -1
                && rs.getString("TABLE_NAME").equals(tableName))
            {
               primaryKeys.add(rs.getString("COLUMN_NAME"));
               // System.out.println(rs.getString("COLUMN_NAME"));
            }
         }

         // Additional Indexes

         rs = dbMetaData.getIndexInfo(databaseName, schemaName, tableName, false, false);
         while (rs.next())
         {
            if (rs.getString("COLUMN_NAME") != null && rs.getString("TABLE_NAME").equals(tableName))
            {
               if (!primaryKeys.contains(rs.getString("COLUMN_NAME")))
               {
                  primaryKeys.add(rs.getString("COLUMN_NAME"));
                  // System.out.println(rs.getString("COLUMN_NAME"));
               }
            }
         }
         
         // Column Names, Form Fields, ComboBox Text, Special Fields,
         // and HashMaps.

         sqlTableFieldsString = "";
         lob_sqlTableFieldsString = "";

         for (int i = 1; i < tableMetaData.getColumnCount() + 1; i++)
         {
            // Collect Information on Column.

            colNameString = tableMetaData.getColumnName(i);
            comboBoxNameString = parseColumnNameField(colNameString);
            columnClass = tableMetaData.getColumnClassName(i);
            columnType = tableMetaData.getColumnTypeName(i);
            columnSize = Integer.valueOf(tableMetaData.getColumnDisplaySize(i));

            // System.out.println(i + " " + colNameString + " " +
            //                      comboBoxNameString + " " +
            //                      columnClass + " " + columnType + " " +
            //                      columnSize);

            // These going to be a problem so skip these columns.

            if ((columnClass == null && columnType == null)
                || columnType.toUpperCase().equals("TIMESTAMP"))
               continue;

            if (columnClass == null)
               columnClass = columnType;

            // Process & Store.

            columnNamesHashMap.put(comboBoxNameString, colNameString);
            columnClassHashMap.put(comboBoxNameString, columnClass);
            columnTypeHashMap.put(comboBoxNameString, columnType.toUpperCase());
            columnSizeHashMap.put(comboBoxNameString, columnSize);
            preferredColumnSizeHashMap.put(comboBoxNameString,
                                           Integer.valueOf(comboBoxNameString.length() * 9));

            fields.add(colNameString);
            viewFormFields.add(comboBoxNameString);
            formFields.add(comboBoxNameString);
            comboBoxFields.add(comboBoxNameString);
            currentTableHeadings.add(comboBoxNameString);
            allTableHeadings.add(comboBoxNameString);
            sqlTableFieldsString += identifierQuoteString + colNameString + identifierQuoteString + ", ";   

            // Collect LOBs.
            if (((columnType.toUpperCase().indexOf("BINARY") != -1)
                  || (columnType.toUpperCase().indexOf("IMAGE") != -1)
                  || (columnType.toUpperCase().indexOf("XML") != -1)
                  || (columnClass.indexOf("String") != -1 && columnType.toUpperCase().indexOf("TEXT") != -1))
                  && !primaryKeys.contains(colNameString))
            {
               lobDataTypesHashMap.put(comboBoxNameString, colNameString);
               lob_sqlTableFieldsString += identifierQuoteString + colNameString + identifierQuoteString + " ";
            }
            
            // Special Column Fields.

            if (columnClass.indexOf("Boolean") != -1 && columnSize.intValue() == 1)
               columnEnumHashMap.put(parseColumnNameField(colNameString), columnType);

            if (primaryKeys.contains(colNameString))
            {
               if (columnSize == null || columnSize.intValue() > 255)
                  columnSize = new Integer("255");
               keyLengthHashMap.put(colNameString, columnSize);
            }

            if (tableMetaData.isAutoIncrement(i))
               autoIncrementHashMap.put(comboBoxNameString, colNameString);
         }
         // Clean up the SQL field string for later use.
         if (sqlTableFieldsString.length() > 2)
            sqlTableFieldsString = sqlTableFieldsString.substring(0, sqlTableFieldsString.length() - 2);
         
         // Make a final check for possible foreign keys.

         rs = dbMetaData.getImportedKeys(databaseName, schemaName, tableName);
         while (rs.next())
         {
            if (rs.getString("FKCOLUMN_NAME") != null
                  && columnNamesHashMap.containsValue(rs.getString("FKCOLUMN_NAME"))
                  && !primaryKeys.contains(rs.getString("FKCOLUMN_NAME")))
            {
               primaryKeys.add(rs.getString("FKCOLUMN_NAME"));
               columnSize = columnSizeHashMap.get(parseColumnNameField(rs.getString("FKCOLUMN_NAME")));
               
               if (columnSize == null || columnSize.intValue() > 255)
                   columnSize = new Integer("255");
               
               keyLengthHashMap.put(rs.getString("FKCOLUMN_NAME"), columnSize);
            }
         }

         // Debug for key resolution varification.
         /*
         System.out.print(sqlTable + ": ");
         Iterator<String> temp = primaryKeys.iterator();
         while (temp.hasNext())
         {
            String currentKey = temp.next();
            System.out.print(currentKey + " " + keyLengthHashMap.get(currentKey) + ", ");
         }
         System.out.println();
         */
         
         return true;
      }
      catch (SQLException e)
      {
         ConnectionManager.displaySQLErrors(e, "TableTapPanel_MSSQL getColumnNames()");
         return false;
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
            ConnectionManager.displaySQLErrors(sqle, "TableTapPanel_MSSQL getColumnNames()");
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
               ConnectionManager.displaySQLErrors(sqle, "TableTapPanel_MSSQL getColumnNames()");
            }
            finally
            {
               if (sqlStatement != null)
                  sqlStatement.close();
            }
         }
      }
   }

   //==============================================================
   // Class method to load the current table's data. The routine
   // will apply the sort and search parameters.
   //==============================================================
   
   public boolean loadTable(Connection dbConnection)
   {
      // Method Instances
      String sqlStatementString;
      String lobLessSQLStatementString;
      StringBuffer lobLessSQLStatement;
      Statement sqlStatement;
      ResultSet rs;

      StringBuffer searchQueryString;
      String columnSearchString, searchTextString;
      String lobLessFieldsString;
      String columnName, columnClass, columnType;
      Integer keyLength;
      int columnSize, preferredColumnSize;
      Object currentContentData;

      // Obtain search parameters column names as needed and
      // saving state for history.
      
      columnSearchString = columnNamesHashMap.get(searchComboBox.getSelectedItem());
      searchTextString = searchTextField.getText();
      
      if (historyAction)
         saveHistory();

      searchQueryString = new StringBuffer();
      if (searchTextString.equals(""))
         searchQueryString.append("'1' LIKE '%'");
      else
      {
         // No field specified so build search for all.
         if (columnSearchString == null)
         {
            String[] tableColumns;
            tableColumns = sqlTableFieldsString.split(",");

            for (int i = 0; i < tableColumns.length; i++)
            {
               columnName = tableColumns[i].replaceAll(identifierQuoteString, "");
               columnType = columnTypeHashMap.get(parseColumnNameField(columnName.trim()));
               
               if (columnType.indexOf("BINARY") != -1 || columnType.indexOf("IMAGE") != -1
                   || columnType.indexOf("XML") != -1)
                  continue;
               
               String searchString = searchTextString;
               
               if (columnType.equals("DATE"))
               {
                  searchString = MyJSQLView_Utils.processDateFormatSearch(searchString);
                  
                  // Something not right in conversion.
                  if (searchString.equals("0"))
                     searchString = searchTextString;
               }
               else if (columnType.indexOf("DATETIME") != -1 || columnType.equals("TIMESTAMP"))
               {
                  if (searchString.indexOf(" ") != -1)
                     searchString = MyJSQLView_Utils.processDateFormatSearch(
                        searchString.substring(0, searchString.indexOf(" ")))
                        + searchString.substring(searchString.indexOf(" "));
                  else if (searchString.indexOf("-") != -1 || searchString.indexOf("/") != -1)
                     searchString = MyJSQLView_Utils.processDateFormatSearch(searchString);
               }
               
               if (i < tableColumns.length - 1)
                  searchQueryString.append(tableColumns[i] + " LIKE '%" + searchString + "%' OR");
               else
                  searchQueryString.append(tableColumns[i] + " LIKE '%" + searchString + "%'");
            }
         }
         // Field specified.
         else
         {
            columnType = columnTypeHashMap.get(searchComboBox.getSelectedItem());
            
            if (columnType.equals("DATE"))
               searchTextString = MyJSQLView_Utils.processDateFormatSearch(searchTextString);
            else if (columnType.indexOf("DATETIME") != -1 || columnType.equals("TIMESTAMP"))
            {
               if (searchTextString.indexOf(" ") != -1)
                  searchTextString = MyJSQLView_Utils.processDateFormatSearch(
                     searchTextString.substring(0, searchTextString.indexOf(" ")))
                     + searchTextString.substring(searchTextString.indexOf(" "));
               else if (searchTextString.indexOf("-") != -1 || searchTextString.indexOf("/") != -1)
                  searchTextString = MyJSQLView_Utils.processDateFormatSearch(searchTextString);
            }
            
            searchQueryString.append(identifierQuoteString + columnSearchString + identifierQuoteString
                                     + " LIKE '%" + searchTextString + "%'");
         }
      }
      // System.out.println(searchTextString);

      // Connect to database to obtain the initial/new items set
      // and then sorting that set.
      
      sqlStatement = null;
      rs = null;
      
      try
      {
         sqlStatement = dbConnection.createStatement();
         
         lobLessFieldsString = sqlTableFieldsString;
         
         if (!lob_sqlTableFieldsString.equals(""))
         {
            String[]  lobColumns = lob_sqlTableFieldsString.split(" ");

            for (int i = 0; i < lobColumns.length; i++)
               lobLessFieldsString = lobLessFieldsString.replace(lobColumns[i], "");
            
            // All fields maybe lobs, so just include all. Network
            // performance hit.
            if (lobLessFieldsString.indexOf(identifierQuoteString) != -1)
               lobLessFieldsString = lobLessFieldsString.substring(lobLessFieldsString.indexOf(
                                                                          identifierQuoteString));
            else
               lobLessFieldsString = sqlTableFieldsString;
            
            lobLessFieldsString = lobLessFieldsString.replaceAll(" ,", "");
            if (lobLessFieldsString.endsWith(", "))
               lobLessFieldsString = lobLessFieldsString.substring(0, lobLessFieldsString.length() - 2);
         }
         
         sqlTableStatement = new StringBuffer();
         lobLessSQLStatement = new StringBuffer();

         if (advancedSortSearch)
         {
            String sqlWhereString = "", lobLess_sqlWhereString = "";
            String sqlOrderString = "", lobLess_sqlOrderString = "";

            // Complete With All Fields.
            sqlStatementString = advancedSortSearchFrame.getAdvancedSortSearchSQL(sqlTableFieldsString,
                                             tableRowStart, tableRowLimit).toString();
            // Summary Table Without LOBs
            lobLessSQLStatementString = advancedSortSearchFrame.getAdvancedSortSearchSQL(lobLessFieldsString,
                                                tableRowStart, tableRowLimit).toString();

            // Clean up the standard sql to meet Oracle's lack of support
            // for the key word LIMIT.

            // Collect WHERE & ORDER structure.
            if (sqlStatementString.indexOf("WHERE") != -1)
            {
               if (sqlStatementString.indexOf("ORDER") != -1)
               {
                  sqlWhereString = sqlStatementString.substring(sqlStatementString.indexOf("WHERE"),
                                                                sqlStatementString.indexOf("ORDER") - 1);
                  lobLess_sqlWhereString = lobLessSQLStatementString.substring(
                                                       lobLessSQLStatementString.indexOf("WHERE"),
                                                       lobLessSQLStatementString.indexOf("ORDER") - 1);
               }
               else
               {
                  sqlWhereString = sqlStatementString.substring(sqlStatementString.indexOf("WHERE"),
                                                                sqlStatementString.indexOf("LIMIT") - 1);
                  lobLess_sqlWhereString = lobLessSQLStatementString.substring(
                                                     lobLessSQLStatementString.indexOf("WHERE"),
                                                     lobLessSQLStatementString.indexOf("LIMIT") - 1);
               }
            }
            if (sqlStatementString.indexOf("ORDER") != -1)
            {
               sqlOrderString = sqlStatementString.substring(sqlStatementString.indexOf("ORDER"),
                                                             sqlStatementString.indexOf("LIMIT") - 1);
               lobLess_sqlOrderString = lobLessSQLStatementString.substring(
                                                     lobLessSQLStatementString.indexOf("ORDER"),
                                                     lobLessSQLStatementString.indexOf("LIMIT") - 1);
            }
            
            // Finish creating modifed SQL.
            sqlTableStatement.append(sqlStatementString.substring(0, sqlStatementString.indexOf("FROM") + 5));
            lobLessSQLStatement.append(lobLessSQLStatementString.substring(0, lobLessSQLStatementString.indexOf("FROM") + 5));

            sqlTableStatement.append("(SELECT " + sqlTableFieldsString + ", ROW_NUMBER() "
                                  + ((sqlOrderString.equals("")) ? ("OVER (ORDER BY "
                                  + (sqlTableFieldsString.indexOf(",") != -1 ?
                                                        sqlTableFieldsString.substring(0, sqlTableFieldsString.indexOf(','))
                                                                             :
                                                        sqlTableFieldsString)
                                  + ") ")
                                                                 : ("OVER (" + sqlOrderString + ") "))
                                  + "AS dmprownumber "
                                  + "FROM " + schemaTableName + " AS t " + sqlWhereString + ") AS t1 "
                                  + "WHERE t1.dmprownumber BETWEEN " + (tableRowStart + 1) + " AND "
                                  + (tableRowStart + tableRowLimit));
            
            lobLessSQLStatement.append("(SELECT " + lobLessFieldsString + ", ROW_NUMBER() "
                                         + ((lobLess_sqlOrderString.equals("")) ? ("OVER (ORDER BY "
                                         + (lobLessFieldsString.indexOf(",") != -1 ?
                                                        lobLessFieldsString.substring(0, lobLessFieldsString.indexOf(','))
                                                                                  :
                                                        lobLessFieldsString)
                                         + ") ")
                                              : ("OVER (" + lobLess_sqlOrderString + ") "))
                                         + "AS dmprownumber "
                                         + "FROM " + schemaTableName + " AS t " + lobLess_sqlWhereString + ") AS t1 "
                                         + "WHERE t1.dmprownumber BETWEEN " + (tableRowStart + 1) + " AND "
                                         + (tableRowStart + tableRowLimit));
            
         }
         else
         {
            sqlTableStatement.append("SELECT " + sqlTableFieldsString + " FROM "
                  + "(SELECT " + sqlTableFieldsString + ", ROW_NUMBER() OVER "
                  + "(ORDER BY t." + identifierQuoteString
                  + columnNamesHashMap.get(sortComboBox.getSelectedItem())
                  + identifierQuoteString + " " + ascDescString + ") " + "AS dmprownumber "
                  + "FROM " + schemaTableName + " AS t " + "WHERE "
                  + searchQueryString.toString() + ") AS t1 " + "WHERE t1.dmprownumber BETWEEN "
                  + (tableRowStart + 1) + " AND " + (tableRowStart + tableRowLimit));

            lobLessSQLStatement.append("SELECT " + lobLessFieldsString + " FROM "
                         + "(SELECT " + lobLessFieldsString + ", ROW_NUMBER() OVER "
                         + "(ORDER BY t." + identifierQuoteString
                         + columnNamesHashMap.get(sortComboBox.getSelectedItem())
                         + identifierQuoteString + " " + ascDescString + ") " + "AS dmprownumber "
                         + "FROM " + schemaTableName + " AS t " + "WHERE "
                         + searchQueryString.toString() + ") AS t1 " + "WHERE t1.dmprownumber BETWEEN "
                         + (tableRowStart + 1) + " AND " + (tableRowStart + tableRowLimit));
         }
         // System.out.println(sqlTableStatement);
         // System.out.println(lobLessSQLStatement.toString());
         rs = sqlStatement.executeQuery(lobLessSQLStatement.toString());

         // Placing the results columns desired into the table that
         // will be display to the user.

         int i = 0;
         int j = 0;

         tableData = new Object[tableRowLimit][currentTableHeadings.size()];

         while (rs.next())
         {
            Iterator<String> headings = currentTableHeadings.iterator();
            while (headings.hasNext())
            {
               String currentHeading = headings.next();
               columnName = columnNamesHashMap.get(currentHeading);
               columnClass = columnClassHashMap.get(currentHeading);
               columnType = columnTypeHashMap.get(currentHeading);
               columnSize = (columnSizeHashMap.get(currentHeading)).intValue();
               keyLength = keyLengthHashMap.get(columnName);
               preferredColumnSize = (preferredColumnSizeHashMap.get(currentHeading)).intValue();

               // System.out.println(i + " " + j + " " + currentHeading + " " +
               // columnName + " " + columnClass + " " +
               // columnType + " " + columnSize + " " +
               // preferredColumnSize + " " + keyLength);

               // Storing data appropriately. If you have some date
               // or other formating, for a field here is where you
               // can take care of it.

               if (lobDataTypesHashMap.containsKey(currentHeading))
                  currentContentData = "lob";
               else
                  currentContentData = rs.getObject(columnName);
               // System.out.println(currentContentData);

               if (currentContentData != null)
               {
                  // =============================================
                  // BigDecimal
                  if (columnClass.indexOf("BigDecimal") != -1)
                     tableData[i][j++] = new BigDecimal(rs.getString(columnName));

                  // =============================================
                  // Date
                  else if (columnType.equals("DATE"))
                  {
                     currentContentData = rs.getDate(columnName);
                     String displayDate = displayMyDateString(currentContentData + "");
                     tableData[i][j++] = displayDate;
                  }
                  
                  // =============================================
                  // Datetime Offset
                  else if (columnType.equals("DATETIMEOFFSET"))
                  {
                     String dateString, timeString;
                     
                     dateString = currentContentData + "";
                     dateString = dateString.substring(0, (dateString.indexOf(" ")));
                     dateString = displayMyDateString(dateString);

                     timeString = currentContentData + "";
                     timeString = timeString.substring(timeString.indexOf(" "));
                     
                     tableData[i][j++] = dateString + timeString;
                  }
                  
                  // =============================================
                  // Datetime
                  else if (columnType.indexOf("DATETIME") != -1)
                  {
                     currentContentData = rs.getTimestamp(columnName);
                     // System.out.println(currentContentData);
                     
                     tableData[i][j++] = (new SimpleDateFormat(
                        DBTablesPanel.getGeneralDBProperties().getViewDateFormat()
                        + " HH:mm:ss").format(currentContentData));
                  }

                  // =============================================
                  // Timestamps
                  else if (columnType.equals("TIMESTAMP"))
                  {
                     currentContentData = rs.getTimestamp(columnName);
                     tableData[i][j++] = (new SimpleDateFormat(
                                           DBTablesPanel.getGeneralDBProperties().getViewDateFormat()
                                           + " HH:mm:ss").format(currentContentData));
                  }
                  
                  // =============================================
                  // BINARY, & IMAGE
                  else if ((columnType.toUpperCase().indexOf("BINARY") != -1)
                            || (columnType.toUpperCase().indexOf("IMAGE") != -1))
                  {
                     // Handles a key Blob
                     if (keyLength != null)
                     {
                        BlobTextKey currentBlobElement = new BlobTextKey();
                        currentBlobElement.setName("LOB");

                        String content = rs.getString(columnName);

                        if (content.length() > keyLength.intValue())
                           content = content.substring(0, keyLength.intValue());

                        currentBlobElement.setContent(content);
                        tableData[i][j++] = currentBlobElement;
                     }
                     else
                     {
                        tableData[i][j++] = "LOB";
                     }
                  }

                  // =============================================
                  // Boolean
                  else if (columnClass.indexOf("Boolean") != -1)
                  {
                     tableData[i][j++] = rs.getString(columnName);
                  }

                  // =============================================
                  // Text & XML
                  else if (columnClass.indexOf("String") != -1 && columnType.indexOf("CHAR") == -1
                           && columnSize > 255)
                  {
                     String stringName;
                     stringName = (String) currentContentData;

                     // Handles a key String
                     if (keyLength != null && columnSize != 255)
                     {
                        BlobTextKey currentBlobElement = new BlobTextKey();
                        currentBlobElement.setName(stringName);

                        String content = rs.getString(columnName);

                        if (content.length() > keyLength.intValue())
                           content = content.substring(0, keyLength.intValue());

                        currentBlobElement.setContent(content);
                        tableData[i][j++] = currentBlobElement;
                     }
                     else
                     {
                        // Limit Table Cell Memory Usage.
                        if (stringName.length() > 512)
                           tableData[i][j++] = stringName.substring(0, 512);
                        else
                           tableData[i][j++] = stringName;
                     }
                  }

                  // =============================================
                  // Any Other
                  else
                  {
                     tableData[i][j++] = rs.getString(columnName);
                     // tableData[i][j++] = currentContentData;
                  }
               }
               // Null Data
               else
               {
                  tableData[i][j++] = "NULL";
               }

               // Setup some sizing for the column in the summary
               // table.
               if ((tableData[i][j - 1] + "").trim().length() * 9 > preferredColumnSize)
               {
                  preferredColumnSize = (tableData[i][j - 1] + "").trim().length() * 9;
                  if (preferredColumnSize > maxPreferredColumnSize)
                     preferredColumnSize = maxPreferredColumnSize;
               }
               preferredColumnSizeHashMap.put(currentHeading, Integer.valueOf(preferredColumnSize));
            }
            j = 0;
            i++;
         }
         return true;
      }
      catch (SQLException e)
      {
         ConnectionManager.displaySQLErrors(e, "TableTapPanel_MSSQL loadTable()");
         return false;
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
            ConnectionManager.displaySQLErrors(sqle, "TableTapPanel_MSSQL loadTable()");
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
               ConnectionManager.displaySQLErrors(sqle, "TableTapPanel_MSSQL loadTable()");
            }
         }
      }
   }
   
   //==============================================================
   // Class method to view the current selected item in the table.
   //==============================================================

   public void viewSelectedItem(Connection dbConnection, int rowToView) throws SQLException
   {
      // Method Instances
      StringBuffer sqlStatementString;
      Statement sqlStatement;
      ResultSet db_resultSet;

      Iterator<String> keyIterator, textFieldNamesIterator;
      Object currentColumnName, currentContentData;
      String currentDB_ColumnName, currentColumnClass, currentColumnType;
      // int columnSize = 0;
      int keyColumn = 0;

      // Connecting to the data base, to obtain
      // the selected entry.
      
      sqlStatement = null;
      db_resultSet = null;
      
      try
      {
         // Begin the SQL statement creation.
         sqlStatement = dbConnection.createStatement();
         sqlStatementString = new StringBuffer();
         sqlStatementString.append("SELECT * FROM " + schemaTableName + " WHERE ");
         
         if (!primaryKeys.isEmpty())
         {
            // Find the key column, in case it has been moved
            // in the summary table, then obtain entry content.

            keyIterator = primaryKeys.iterator();

            while (keyIterator.hasNext())
            {
               currentDB_ColumnName = keyIterator.next();

               for (int i = 0; i < listTable.getColumnCount(); i++)
                  if (listTable.getColumnName(i).equals(parseColumnNameField(currentDB_ColumnName)))
                     keyColumn = i;

               // Found now get key info.
               currentContentData = listTable.getValueAt(rowToView, keyColumn);

               // Special case of blob/text key.
               if (currentContentData instanceof BlobTextKey)
               {
                  String keyString = ((BlobTextKey) currentContentData).getContent();
                  keyString = keyString.replaceAll("'", "''");

                  // select * from t1 where a like "hello%";
                  sqlStatementString.append(identifierQuoteString + currentDB_ColumnName
                                            + identifierQuoteString + " LIKE '"
                                            + keyString + "%' AND ");
               }
               // Normal keys
               else
               {
                  // Handle null content properly.
                  if ((currentContentData + "").toLowerCase().equals("null"))
                  {
                     currentContentData = "IS NULL";
                     sqlStatementString.append(identifierQuoteString + currentDB_ColumnName
                                               + identifierQuoteString + " "
                                               + currentContentData + " AND ");
                  }
                  else
                  {
                     // Escape single quotes.
                     currentColumnClass = columnClassHashMap.get(parseColumnNameField(currentDB_ColumnName));
                     
                     if (currentColumnClass.indexOf("String") != -1)
                        currentContentData = ((String) currentContentData).replaceAll("'", "''");
                     
                     // Reformat date keys.
                     currentColumnType = columnTypeHashMap.get(parseColumnNameField(currentDB_ColumnName));
                     if (currentColumnType.equals("DATE"))
                     {
                        sqlStatementString.append(identifierQuoteString + currentDB_ColumnName
                                                  + identifierQuoteString + "='"
                                                  + MyJSQLView_Utils.convertViewDateString_To_DBDateString(currentContentData + "",
                                                     DBTablesPanel.getGeneralDBProperties().getViewDateFormat())
                                                  + "' AND ");
                     }
                     else
                     {
                        // Character data gets single quotes, not numbers though.
                        if (currentColumnClass.toLowerCase().indexOf("string") != -1)
                           sqlStatementString.append(identifierQuoteString + currentDB_ColumnName
                                                  + identifierQuoteString + "='"
                                                  + currentContentData + "' AND ");
                        else
                           sqlStatementString.append(identifierQuoteString + currentDB_ColumnName
                                                  + identifierQuoteString + "="
                                                  + currentContentData + " AND ");
                     }
                  }
               }
            }
            sqlStatementString.delete((sqlStatementString.length() - 5), sqlStatementString.length());
         }
         // See if we can brute force an all fields
         // SELECT WHERE query.
         else
         {
            // Cycle through each field and set value.
            for (int i = 0; i < listTable.getColumnCount(); i++)
            {
               currentContentData = listTable.getValueAt(rowToView, i);
               currentDB_ColumnName = (String) columnNamesHashMap.get(listTable.getColumnName(i));
               currentColumnClass = columnClassHashMap.get(listTable.getColumnName(i));
               currentColumnType = columnTypeHashMap.get(listTable.getColumnName(i));
               // columnSize = columnSizeHashMap.get(listTable.getColumnName(i)).intValue();
               
               // System.out.println("field:" + currentDB_ColumnName + " class:" + currentColumnClass
               //                    + " type:" + currentColumnType + " value:" + currentContentData);
               
               // Skip Blob, Text, & Float Unless NULL.
               if ((currentColumnType.toUpperCase().indexOf("BINARY") != -1)
                     || (currentColumnType.toUpperCase().indexOf("IMAGE") != -1)
                     || (currentColumnType.toUpperCase().indexOf("XML") != -1)
                     || (currentColumnClass.indexOf("String") != -1
                         && currentColumnType.toUpperCase().indexOf("TEXT") != -1)
                     || (currentColumnType.indexOf("FLOAT") != -1))
               {
                  if (currentContentData.toString().toUpperCase().equals("NULL"))
                     sqlStatementString.append(identifierQuoteString + currentDB_ColumnName
                        + identifierQuoteString + " IS NULL AND ");
                  continue;     
               }
               
               // NULL
               if (currentContentData.toString().toUpperCase().equals("NULL"))
               {
                  sqlStatementString.append(identifierQuoteString + currentDB_ColumnName
                     + identifierQuoteString + " IS NULL ");
               }
               // Try the Rest
               else
               {
                  sqlStatementString.append(identifierQuoteString + currentDB_ColumnName
                     + identifierQuoteString);
                  
                  // Process Date
                  if (currentColumnType.equals("DATE"))
                  {
                     String dateString = MyJSQLView_Utils.processDateFormatSearch(
                        (String) currentContentData);
                     
                     sqlStatementString.append("='" + dateString + "' ");
                  }
                  
                  // Process DateTime
                  else if (currentColumnType.indexOf("DATETIME") != -1 || currentColumnType.equals("TIMESTAMP"))
                  {
                     String content, dateTimeString;
                     content = (String) currentContentData;
                     
                     dateTimeString = MyJSQLView_Utils.processDateFormatSearch(content.substring(0,
                        content.indexOf(" ")))
                           + content.substring(content.indexOf(" "));
                     
                     sqlStatementString.append("='" + dateTimeString + "' ");
                  }
                  // All Others
                  else
                  {
                     if (currentColumnClass.indexOf("Integer") != -1
                           || currentColumnClass.indexOf("Long") != -1
                           || currentColumnClass.indexOf("Float") != -1
                           || currentColumnClass.indexOf("Double") != -1
                           || currentColumnClass.indexOf("Byte") != -1
                           || currentColumnClass.indexOf("BigDecimal") != -1
                           || currentColumnClass.indexOf("Short") != -1)
                        sqlStatementString.append("=" + currentContentData + " ");
                     else
                        sqlStatementString.append("='" + currentContentData + "' ");
                  }
               }
               sqlStatementString.append("AND ");
            }
            sqlStatementString.delete(sqlStatementString.length() - 4, sqlStatementString.length());
         }

         // System.out.println(sqlStatementString);
         db_resultSet = sqlStatement.executeQuery(sqlStatementString.toString());
         
         if (!db_resultSet.next())
            return;

         // Cycling through the item fields and setting
         // in the tableViewForm.

         textFieldNamesIterator = viewFormFields.iterator();
         int i = 0;

         while (textFieldNamesIterator.hasNext())
         {
            currentColumnName = textFieldNamesIterator.next();
            currentDB_ColumnName = columnNamesHashMap.get(currentColumnName);
            currentColumnClass = columnClassHashMap.get(currentColumnName);
            currentColumnType = columnTypeHashMap.get(currentColumnName);

            currentContentData = db_resultSet.getString(currentDB_ColumnName);
            // System.out.println(i + " " + currentColumnName + " " +
            //                    currentDB_ColumnName + " " +
            //                    currentColumnType + " " + columnSize + " " + currentContentData);

            if (currentContentData != null)
            {
               // DATE Type Field
               if (currentColumnType.equals("DATE"))
               {
                  currentContentData = db_resultSet.getDate(currentDB_ColumnName);
                  tableViewForm.setFormField(currentColumnName,
                     (Object) displayMyDateString(currentContentData + ""));
               }
               
               // DATETIME Type Field
               else if (currentColumnType.indexOf("DATETIME") != -1)
               {
                  String dateString = currentContentData + "";
                  dateString = dateString.substring(0, (dateString.indexOf(" ")));
                  dateString = displayMyDateString(dateString);

                  String timeString = currentContentData + "";
                  timeString = timeString.substring(timeString.indexOf(" "));
                  currentContentData = dateString + timeString;
                  tableViewForm.setFormField(currentColumnName, currentContentData);
               }

               // Timestamps Type Field
               else if (currentColumnType.equals("TIMESTAMP"))
               {
                  currentContentData = db_resultSet.getTimestamp(currentDB_ColumnName);
                  tableViewForm.setFormField(currentColumnName,
                     (new SimpleDateFormat(DBTablesPanel.getGeneralDBProperties().getViewDateFormat()
                        + " HH:mm:ss").format(currentContentData)));
               }

               // Binary, Image Type Field
               else if ((currentColumnType.indexOf("BINARY") != -1)
                        || (currentColumnType.equals("IMAGE")))
               {
                  String typeName;
                  
                  if (currentColumnType.indexOf("BINARY") != -1)
                     typeName = "BINARY";
                  else
                     typeName = "IMAGE";
                  
                  if (((String) currentContentData).getBytes().length != 0)
                  {
                     currentContentData = db_resultSet.getBytes(currentDB_ColumnName);
                     int size = ((byte[]) currentContentData).length;
                     
                     tableViewForm.setFormField(currentColumnName, (Object) (typeName + " " + size + " Bytes"));
                     tableViewForm.setFormFieldBlob(currentColumnName, (byte[]) currentContentData);
                  }
                  else
                     tableViewForm.setFormField(currentColumnName, (Object) (typeName + " 0 Bytes"));
               }

               // Text, & Clob Fields
               else if ((currentColumnClass.indexOf("String") != -1 && !currentColumnType.equals("CHAR")
                         && (columnSizeHashMap.get(currentColumnName)).intValue() > 255)
                        || currentColumnType.indexOf("TEXT") != -1
                        || currentColumnType.equals("XML"))
               {
                  if (((String) currentContentData).getBytes().length != 0)
                  {
                     int size = ((String) currentContentData).getBytes().length;
                     tableViewForm.setFormField(currentColumnName, (Object) ("TEXT " + size + " Bytes"));
                     tableViewForm.setFormFieldText(currentColumnName, (String) currentContentData);
                  }
                  else
                     tableViewForm.setFormField(currentColumnName, (Object) "TEXT 0 Bytes");
               }

               // Default Content. A normal table entry should
               // fall through here, to set content.
               else
                  tableViewForm.setFormField(currentColumnName, currentContentData);
            }
            // Null fields fall through here.
            else
            {
               tableViewForm.setFormField(currentColumnName, "NULL");
            }
            i++;
         }
      }
      catch (SQLException e)
      {
         ConnectionManager.displaySQLErrors(e, "TableTapPanel_MSSQL viewSelectedItem()");
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
            ConnectionManager.displaySQLErrors(sqle, "TableTabPanel_Genric viewSelectedItem()");
         }
         finally
         {
            if (sqlStatement != null)
               sqlStatement.close();
         }
      }
   }

   //==============================================================
   // Class method to add a table entry.
   //==============================================================

   public void addItem(Connection dbConnection)
   {
      Iterator<String> textFieldNamesIterator;
      Object currentColumnName, currentContentData;
      String currentColumnClass, currentColumnType;

      // Showing the Table Entry Form
      TableEntryForm addForm = new TableEntryForm("Add Table Entry: ", true, schemaTableName,
                                                  -1, null, primaryKeys, autoIncrementHashMap, null,
                                                  formFields, tableViewForm, columnNamesHashMap,
                                                  columnClassHashMap, columnTypeHashMap,
                                                  columnSizeHashMap, columnEnumHashMap,
                                                  columnSetHashMap);

      // Doing some sizing of the height based on the number
      // of fields in the table. The entry form will though
      // provided scrollbars.

      if ((((formFields.size() / 2) + 1) * 35) > 400)
      {
         if (((formFields.size() / 2) + 1) * 35 < 600)
            addForm.setSize(800, (((formFields.size() / 2) + 1) * 35));
         else
            addForm.setSize(800, 600);
      }
      else
         addForm.setSize(800, 400);

      addForm.getDisposeButton().addActionListener(this);
      addForm.addMouseListener(MyJSQLView.getPopupMenuListener());
      addForm.center();
      addForm.setVisible(true);

      // Fields in the add form will be empty except the
      // ones where special data is specified to the
      // user. aka enum, set, date format.

      textFieldNamesIterator = formFields.iterator();

      while (textFieldNamesIterator.hasNext())
      {
         currentColumnName = textFieldNamesIterator.next();
         currentColumnClass = columnClassHashMap.get(currentColumnName);
         currentColumnType = columnTypeHashMap.get(currentColumnName);

         // Auto-Increment Type Field
         if (autoIncrementHashMap.containsKey(currentColumnName))
         {
            currentContentData = "AUTO";
            addForm.setFormField(currentColumnName, currentContentData);
         }

         // Bit Type Fields
         if (columnEnumHashMap.containsKey(currentColumnName)
             || columnSetHashMap.containsKey(currentColumnName))
         {
            try
            {
               setSpecialFieldData(addForm, dbConnection, currentColumnName, null);
            }
            catch (SQLException e)
            {
               ConnectionManager.displaySQLErrors(e, "TableTapPanel_MSSQL addItem()");
            }  
         }

         // DATE Type Field
         if (currentColumnType.equals("DATE"))
         {
            currentContentData = DBTablesPanel.getGeneralDBProperties().getViewDateFormat();
            addForm.setFormField(currentColumnName, currentContentData);
         }

         // TIME Type Field
         if (currentColumnType.equals("TIME") || currentColumnType.equals("TIMETZ"))
         {
            currentContentData = "hh:mm:ss";
            addForm.setFormField(currentColumnName, currentContentData);
         }
         
         // DATETIME Type Field
         if (currentColumnType.equals("DATETIME") || currentColumnType.equals("DATETIME2")
             || currentColumnType.equals("SMALLDATETIME"))
         {
            currentContentData = DBTablesPanel.getGeneralDBProperties().getViewDateFormat();
            currentContentData = currentContentData + " HH:mm:ss";
            addForm.setFormField(currentColumnName, currentContentData);
         }
         
         // DATETIME OFFSET Type Field
         if (currentColumnType.equals("DATETIMEOFFSET"))
         {
            currentContentData = DBTablesPanel.getGeneralDBProperties().getViewDateFormat();
            currentContentData = currentContentData + " HH:mm:ss.nnnnnnn +|-hh:mm";
            addForm.setFormField(currentColumnName, currentContentData);
         }

         // TIMESTAMP Type Field
         if (currentColumnType.equals("TIMESTAMP"))
         {
            currentContentData = "";
            addForm.setFormField(currentColumnName, currentContentData);
         }

         // BLOB/BYTEA/BINARY Type Field
         if (currentColumnType.indexOf("BINARY") != -1
             || currentColumnType.equals("IMAGE"))
         {
            if (currentColumnType.indexOf("BINARY") != -1)
               addForm.setFormField(currentColumnName, (Object) ("BINARY Browse"));
            else
               addForm.setFormField(currentColumnName, (Object) ("IMAGE Browse"));
         }

         // All TEXT, & XML
         if (currentColumnClass.indexOf("TEXT") != -1
             || currentColumnType.equals("XML"))
         {
            addForm.setFormField(currentColumnName, (Object) ("TEXT Browse"));
         }
      }
   }

   //==============================================================
   // Class method to edit the current selected item.
   //==============================================================

   public void editSelectedItem(Connection dbConnection, int rowToEdit, Object columnName, Object id)
                               throws SQLException
   {
      // Method Instances
      StringBuffer sqlStatementString;
      Statement sqlStatement;
      ResultSet db_resultSet;

      Iterator<String> keyIterator, textFieldNamesIterator;
      Object currentColumnName, currentContentData;
      String currentDB_ColumnName, currentColumnClass, currentColumnType;
      int currentColumnSize;
      int keyColumn = 0;

      // Showing the edit form and trying to size appropriately.
      TableEntryForm editForm = new TableEntryForm("Edit Table Entry: ", false, schemaTableName,
                                                   rowToEdit, this, primaryKeys,
                                                   autoIncrementHashMap, id,
                                                   formFields, tableViewForm, columnNamesHashMap,
                                                   columnClassHashMap, columnTypeHashMap,
                                                   columnSizeHashMap, columnEnumHashMap,
                                                   columnSetHashMap);

      if ((((formFields.size() / 2) + 1) * 35) > 400)
      {
         if (((formFields.size() / 2) + 1) * 35 < 600)
            editForm.setSize(800, (((formFields.size() / 2) + 1) * 35));
         else
            editForm.setSize(800, 600);
      }
      else
         editForm.setSize(800, 400);
      editForm.getDisposeButton().addActionListener(this);
      editForm.center();
      editForm.setVisible(true);

      // Connecting to the data base, to obtain
      // the selected entries field data.
      
      sqlStatement = null;
      db_resultSet = null;

      try
      {
         sqlStatement = dbConnection.createStatement();

         // Begin the SQL statement(s) creation.
         sqlStatementString = new StringBuffer();
         sqlStatementString.append("SELECT * FROM " + schemaTableName + " WHERE ");

         keyIterator = primaryKeys.iterator();

         // Find the key column, in case it has been moved
         // in the summary table, then obtain entry content.

         while (keyIterator.hasNext())
         {
            currentDB_ColumnName = keyIterator.next();

            for (int i = 0; i < listTable.getColumnCount(); i++)
               if (listTable.getColumnName(i).equals(parseColumnNameField(currentDB_ColumnName)))
                  keyColumn = i;

            // Found the key so get info.
            currentContentData = listTable.getValueAt(rowToEdit, keyColumn);

            // Special case of blob/text key.
            if (currentContentData instanceof BlobTextKey)
            {
               String keyString = ((BlobTextKey) currentContentData).getContent();
               keyString = keyString.replaceAll("'", "''");

               sqlStatementString.append(identifierQuoteString + currentDB_ColumnName
                                         + identifierQuoteString
                                         + " LIKE '" + keyString + "%' AND ");
            }
            // Normal key.
            else
            {
               // Handle null content properly.
               if ((currentContentData + "").toLowerCase().equals("null"))
               {
                  currentContentData = "IS NULL";
                  sqlStatementString.append(identifierQuoteString + currentDB_ColumnName
                                            + identifierQuoteString + " " + currentContentData
                                            + " AND ");
               }
               else
               {
                  // Escape single quotes.
                  currentColumnClass = (String) columnClassHashMap
                        .get(parseColumnNameField(currentDB_ColumnName));
                  if (currentColumnClass.indexOf("String") != -1)
                     currentContentData = ((String) currentContentData).replaceAll("'", "''");

                  // Reformat date keys.
                  currentColumnType = columnTypeHashMap.get(parseColumnNameField(currentDB_ColumnName));
                  if (currentColumnType.equals("DATE"))
                  {
                     sqlStatementString.append(identifierQuoteString + currentDB_ColumnName
                                               + identifierQuoteString + "='"
                                               + MyJSQLView_Utils.convertViewDateString_To_DBDateString(
                                                  currentContentData + "",
                                                  DBTablesPanel.getGeneralDBProperties().getViewDateFormat())
                                               + "' AND ");
                  }
                  else
                  {
                     // Character data gets single quotes, not numbers though.
                     if (currentColumnClass.toLowerCase().indexOf("string") != -1)
                        sqlStatementString.append(identifierQuoteString + currentDB_ColumnName
                                               + identifierQuoteString + "='"
                                               + currentContentData + "' AND ");
                     else
                        sqlStatementString.append(identifierQuoteString + currentDB_ColumnName
                                               + identifierQuoteString + "="
                                               + currentContentData + " AND ");
                  }
               }
            }
         }
         sqlStatementString.delete((sqlStatementString.length() - 5), sqlStatementString.length());
         // System.out.println(sqlStatementString);
         db_resultSet = sqlStatement.executeQuery(sqlStatementString.toString());
         db_resultSet.next();

         // Now that we have the data for the selected field entry in
         // the table fill in the edit form.

         textFieldNamesIterator = formFields.iterator();

         while (textFieldNamesIterator.hasNext())
         {
            currentColumnName = textFieldNamesIterator.next();
            currentDB_ColumnName = columnNamesHashMap.get(currentColumnName);
            currentColumnClass = columnClassHashMap.get(currentColumnName);
            currentColumnType = columnTypeHashMap.get(currentColumnName);
            currentColumnSize = (columnSizeHashMap.get(currentColumnName)).intValue();

            currentContentData = db_resultSet.getString(currentDB_ColumnName);
            // System.out.println(currentColumnName + " " + currentContentData);

            // Special content from other tables, ComboBoxes, maybe.
            // Also time, date or your special field formatting.

            if (((String) currentColumnName).equals("Your Special Field Name"))
               setSpecialFieldData(editForm, dbConnection, currentColumnName, currentContentData);

            // BOOLEAN Single Bit Type Fields
            else if (columnEnumHashMap.containsKey(currentColumnName)
                     || columnSetHashMap.containsKey(currentColumnName))
            {
               if (currentColumnClass.indexOf("Boolean") != -1 && currentColumnSize == 1)
               {
                  if (currentContentData.equals("f"))
                     currentContentData = "FALSE";
                  else
                     currentContentData = "TRUE";
               }
               setSpecialFieldData(editForm, dbConnection, currentColumnName, currentContentData);
            }

            // DATE Type Field
            else if (currentColumnType.equals("DATE"))
            {
               if (currentContentData != null)
               {
                  currentContentData = db_resultSet.getDate(currentDB_ColumnName);
                  editForm.setFormField(currentColumnName,
                                        (Object) displayMyDateString(currentContentData + ""));
               }
               else
                  editForm.setFormField(currentColumnName, 
                                        (Object) DBTablesPanel.getGeneralDBProperties().getViewDateFormat());
            }
            
            // DATETIME OFFSET Type Field
            else if (currentColumnType.equals("DATETIMEOFFSET"))
            {
               String dateString, timeString;
               
               if (currentContentData != null)
               {
                  dateString = currentContentData + "";
                  dateString = dateString.substring(0, (dateString.indexOf(" ")));
                  dateString = displayMyDateString(dateString);

                  timeString = currentContentData + "";
                  timeString = timeString.substring(timeString.indexOf(" "));
                  currentContentData = dateString + timeString;
                  
                  editForm.setFormField(currentColumnName, (Object) currentContentData); 
               }
               else
                  editForm.setFormField(currentColumnName,
                     (Object) (DBTablesPanel.getGeneralDBProperties().getViewDateFormat()
                               + " HH:mm:ss.nnnnnnn +|-hh:mm"));
            }
            
            // DATETIME Type Field
            else if (currentColumnType.indexOf("DATETIME") != -1)
            {
               if (currentContentData != null)
               {
                  currentContentData = db_resultSet.getTimestamp(currentDB_ColumnName);
                  // System.out.println(currentContentData);
                  
                  currentContentData = new SimpleDateFormat(
                     DBTablesPanel.getGeneralDBProperties().getViewDateFormat()
                     + " HH:mm:ss").format(currentContentData);
                  editForm.setFormField(currentColumnName, currentContentData);
               }
               else
                  editForm.setFormField(currentColumnName,
                     (Object) (DBTablesPanel.getGeneralDBProperties().getViewDateFormat() + " HH:mm:ss"));
            }

            // Timestamps Type Field
            else if (currentColumnType.equals("TIMESTAMP"))
            {
               if (currentContentData != null)
               {
                  currentContentData = db_resultSet.getTimestamp(currentDB_ColumnName);
                  // System.out.println(currentContentData);
                  editForm.setFormField(currentColumnName,
                     (Object) (new SimpleDateFormat(DBTablesPanel.getGeneralDBProperties().getViewDateFormat()
                                                    + " HH:mm:ss").format(currentContentData)));
               }
               else
                  editForm.setFormField(currentColumnName,
                                        (Object) (DBTablesPanel.getGeneralDBProperties().getViewDateFormat()
                                        + " HH:mm:ss"));
            }

            // Binary/Image Type Field
            else if (currentColumnType.indexOf("BINARY") != -1
                     || currentColumnType.equals("IMAGE"))
            {
               String binaryType;
               if (currentColumnType.indexOf("BINARY") != -1)
                  binaryType = "BINARY";
               else
                  binaryType = "IMAGE";

               if (currentContentData != null)
               {
                  currentContentData = db_resultSet.getBytes(currentDB_ColumnName);

                  if ((((byte[]) currentContentData)).length != 0)
                  {
                     int size = ((byte[]) currentContentData).length;
                     editForm.setFormField(currentColumnName, (Object) (binaryType + " " + size + " Bytes"));
                  }
                  else
                  {
                     editForm.setFormField(currentColumnName, (Object) (binaryType + " 0 Bytes"));
                  }
                  editForm.setFormFieldBlob(currentColumnName, (byte[]) currentContentData);

               }
               else
                  editForm.setFormField(currentColumnName, (Object) (binaryType + " NULL"));
            }

            // All Text But TinyText & Clob Type Fields
            else if ((currentColumnClass.indexOf("String") != -1 && !currentColumnType.equals("CHAR")
                      && currentColumnSize > 255)
                     || currentColumnType.equals("XML"))
            {
               if (currentContentData != null)
               {
                  if (((String) currentContentData).getBytes().length != 0)
                  {
                     int size = ((String) currentContentData).getBytes().length;
                     editForm.setFormField(currentColumnName, (Object) ("TEXT " + size + " Bytes"));
                  }
                  else
                  {
                     editForm.setFormField(currentColumnName, (Object) "TEXT 0 Bytes");
                  }
                  editForm.setFormFieldText(currentColumnName, (String) currentContentData);
               }
               else
                  editForm.setFormField(currentColumnName, (Object) "TEXT NULL");
            }

            // Default Content. A normal table entry should
            // fall through here, to set content.
            else
            {
               if (currentContentData != null)
                  editForm.setFormField(currentColumnName, currentContentData);
               else
                  editForm.setFormField(currentColumnName, (Object) "NULL");
            }
         }
      }
      catch (SQLException e)
      {
         ConnectionManager.displaySQLErrors(e, "TableTapPanel_MSSQL editSelectedItem()");
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
            ConnectionManager.displaySQLErrors(sqle, "TableTapPanel_MSSQL editSelectedItem()");
         }
         finally
         {
            if (sqlStatement != null)
               sqlStatement.close();
         }
      }
   }
}
