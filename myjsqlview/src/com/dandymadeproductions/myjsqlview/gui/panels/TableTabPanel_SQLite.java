//=============================================================
//            MyJSQLView TableTabPanel_SQLite
//=============================================================
//
//    This class provides the means to create a default table
// summary view of data in the SQLite database that in MyJSQLView
// is listed according to a specified sort and search. Entries
// from the database table may be viewed, added, edited, or
// deleted by means of this panel. The panel also provides the
// mechanism to page through the database table's data.
//
//           << TableTabPanel_SQLite.java >>
//
//================================================================
// Copyright (C) 2005-2015 Dana M. Proctor
// Version 3.7 05/08/2015
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
// Version 1.0 Original TableTabPanel_SQLite Class.
//         1.1 Commented Out System.out.println() Output in Methods loadTable()
//             & getColumnNames().
//         1.2 Class Methods loadTable(), viewSelectedItem(), addItem() & editSelectedItem()
//             Changed Default Entry for Date/DateTime/TimeStamp Type Entry to
//             GeneralProperties.getDateViewFormat(). Class Methods view/editSelectedItem()
//             Change for Date Key Conversion to MyJSQLView_utils.convertViewDateString_To_
//             DBDateString().
//         1.3 Class Method loadTable() Changes to Give the Ability to Properly Search
//             Given Input for Date/DateTime/Timestamp Fields.
//         1.4 Changes to Class Methods getColumnNames(), loadTable(), viewSelectedItem(),
//             & deleteSelectedItem() to Used Newly Redefined ConnectionManager to Display
//             SQL Errors.
//         1.5 Corrections in Method loadTable to Properly Build searchTextString For Date,
//             DateTime, & Timestamp When No Specific Column is Selected.
//         1.6 Called saveHistory() in Class Method loadTable().
//         1.7 Changed the Conditional Check for saveAction by Removing the NOT Logic.
//         1.8 Correction in loadTable() for Not Modifiying searchTextString
//             During Composition When No Field Specified.
//         1.9 Set sqlTableStatement to an Empty String Prior to Each New
//             Assignment in Class Method loadTable().
//         2.0 Set sqlTableStatement to NULL and Then Created a new String() for it of
//             sqlStatementString to Insure it is Flushed in Class Method loadTable().
//         2.1 Set sqlTableStatement to Not be a new String() of sqlStatementString in
//             Method loadTable().
//         2.2 Removed Method Instance sqlStatementString & Replaced With Parent
//             Class Instance sqlTableStatement.
//         2.3 Class Method addItem() Added a try catch for setSpecialFields(). Methods
//             viewSelectedItem(), editSelectedItem() & getColumnNames() Throws for
//             SQLException Through finally Clause for Closing sqlStatment.
//         2.4 Changes in loadTable to Add Back Instance sqlStatementString and Then
//             Have sqlTableStatement New StringBuffer Designation Loaded From it.
//         2.5 Method loadTable() Conversion of Date From searchString Failed, Due
//             to Possible Generic Search of All Fields for Given Characters. So
//             Just Use Original Characters.
//         2.6 Removed sqlStatementString in loadTable(), Replaced With sqlTableStatement.
//         2.7 Class Method loadTable() Insured That sqlTableStatement is Given a
//             New Instance Before Reasignment. Replaced delete() Which Was Not
//             Propagating Changes to Object Outside the Method.
//         2.8 Change in viewSelectedItem() to Use Brute Force WHERE Creation for
//             Selected listTable Entry if primaryKeys().isEmpty().
//         2.9 Change in Class Method getColumnNames() of Adding Items to New ArrayList
//             Instances by Way of add() Instead of addElement().
//         3.0 Change in getColumnNames() to Always Check for Foreign Keys.
//         3.1 Class Method loadTable() Changed lobLessSQLStatementString to StringBuffer
//             & Chopped String Off Name.
//         3.2 Closure for db_resultSet in editSelectedItem(), viewSelectedItem() &
//             getColumnNames() Moved to finally.
//         3.3 Closure for rs & sqlStatement in loadTable() Moved to finally.
//         3.4 Changed Package Name to com.dandymadeproductions.myjsqlview.gui.panels.
//         3.5 Class Method addItem() & deleteItem() TableEntryForm's disposeButton
//             Collected via getDisposeButton().
//         3.6 Change in loadTable(), viewSelectedItem(), addItem(), &
//             editSelectedItem() to Use DBTablePanel.getGeneralDBProperties().
//         3.7 Change in Column Class Storage for SQLite Temporal Types as Declared
//             by DB Column Type in getColumnNames(). Methods loadTables(), vew/edit
//             SelectedItem() Change in Time Setting & Timestamp Formatting Too
//             (ViewDate, HH:mm:ss.SSS).
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
import com.dandymadeproductions.myjsqlview.gui.forms.TableEntryForm;
import com.dandymadeproductions.myjsqlview.utilities.BlobTextKey;
import com.dandymadeproductions.myjsqlview.utilities.MyJSQLView_Utils;

/**
 *    The TableTabPanel_SQLite class provides the means to create a default table
 * summary view of data in an unknown database that in MyJSQLView is listed
 * according to a specified sort and search. Entries from the database table may
 * be viewed, added, edited, or deleted by means of this panel. The panel also
 * provides the mechanism to page through the database table's data.
 * 
 * @author Dana M. Proctor
 * @version 3.7 05/08/2015
 */

public class TableTabPanel_SQLite extends TableTabPanel
{
   // Class Instances.
   private static final long serialVersionUID = 7742168062289517814L;

   //===========================================================
   // TableTabPanel Constructor
   //===========================================================

   public TableTabPanel_SQLite(String table, Connection setup_dbConnection, boolean viewOnlyTable)
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

      String tableName;
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
            tableName = sqlTable.substring(sqlTable.indexOf(".") + 1);
         else
            tableName = sqlTable;
         
         // ====================================================
         // Setting Up the Column Names, Form Fields, ComboBox
         // Text, Hashmaps, Special Fields, & Primary Key(s).

         sqlStatementString = "SELECT * FROM " + schemaTableName + " LIMIT 1";
         // System.out.println(sqlStatementString);

         db_resultSet = sqlStatement.executeQuery(sqlStatementString);

         // Primary Key(s)
         
         dbMetaData = dbConnection.getMetaData();
         tableMetaData = db_resultSet.getMetaData();

         rs = dbMetaData.getPrimaryKeys(tableMetaData.getCatalogName(1),
                                        tableMetaData.getSchemaName(1),
                                        tableMetaData.getTableName(1));
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

         rs = dbMetaData.getIndexInfo(tableMetaData.getCatalogName(1),
                                      tableMetaData.getSchemaName(1),
                                      tableMetaData.getTableName(1), false, false);
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
            //                     comboBoxNameString + " " +
            //                     columnClass + " " + columnType + " " +
            //                     columnSize);

            // This going to be a problem so skip this column.

            if (columnClass == null && columnType == null)
               continue;

            if (columnClass == null)
               columnClass = columnType;

            // Process & Store.

            columnNamesHashMap.put(comboBoxNameString, colNameString);
            
            if (columnType.toUpperCase().equals("DATE"))
               columnClassHashMap.put(comboBoxNameString, "java.sql.Date");
            else if (columnType.toUpperCase().equals("TIME"))
               columnClassHashMap.put(comboBoxNameString, "java.sql.Time");
            else if (columnType.toUpperCase().indexOf("TIMESTAMP") != -1)
               columnClassHashMap.put(comboBoxNameString, "java.sql.Timestamp");
            else
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
            if (columnType.toUpperCase().indexOf("BLOB") != -1
                && !primaryKeys.contains(colNameString))
            {
               lobDataTypesHashMap.put(comboBoxNameString, colNameString);
               lob_sqlTableFieldsString += identifierQuoteString + colNameString + identifierQuoteString + " ";
            }
            
            // Special Column Fields.

            if (columnClass.indexOf("Boolean") != -1 && columnSize.intValue() == 1)
               columnEnumHashMap.put(parseColumnNameField(colNameString), columnType);

            if (columnType.indexOf("enum") != -1)
               columnEnumHashMap.put(parseColumnNameField(colNameString), columnType);

            if (columnType.indexOf("set") != -1)
               columnSetHashMap.put(parseColumnNameField(colNameString), columnType);

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

         rs = dbMetaData.getImportedKeys(tableMetaData.getCatalogName(1),
                                         tableMetaData.getSchemaName(1),
                                         tableMetaData.getTableName(1));
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
         ConnectionManager.displaySQLErrors(e, "TableTabPanel_SQLite getColumnNames()");
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
            ConnectionManager.displaySQLErrors(sqle, "TableTabPanel_SQLite getColumnNames()");
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
               ConnectionManager.displaySQLErrors(sqle, "TableTabPanel_SQLite getColumnNames()");
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
               
               String searchString = searchTextString;
               
               if (columnType.equals("DATE"))
               {
                  searchString = MyJSQLView_Utils.processDateFormatSearch(searchString);
                  
                  // Something not right in conversion.
                  if (searchString.equals("0"))
                     searchString = searchTextString;
               }
               else if (columnType.equals("DATETIME") || columnType.equals("TIMESTAMP")
                        || columnType.equals("TIMESTAMPTZ"))
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
            else if (columnType.equals("DATETIME") || columnType.equals("TIMESTAMP")
                     || columnType.equals("TIMESTAMPTZ"))
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
         // System.out.println(searchTextString);
      }

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
            // Complete With All Fields.
            sqlTableStatement.append(advancedSortSearchFrame.getAdvancedSortSearchSQL(sqlTableFieldsString,
                                             tableRowStart, tableRowLimit));
            // Summary Table Without LOBs
            lobLessSQLStatement.append(advancedSortSearchFrame.getAdvancedSortSearchSQL(lobLessFieldsString,
                                                    tableRowStart, tableRowLimit));
         }
         else
         {
            // Complete With All Fields.
            sqlTableStatement.append("SELECT " + sqlTableFieldsString + " FROM " + schemaTableName + " "
                                 + "WHERE " + searchQueryString.toString() + " " + "ORDER BY "
                                 + identifierQuoteString
                                 + columnNamesHashMap.get(sortComboBox.getSelectedItem())
                                 + identifierQuoteString + " " + ascDescString + " " + "LIMIT "
                                 + tableRowLimit + " " + "OFFSET " + tableRowStart);
            
            // Summary Table Without LOBs.
            lobLessSQLStatement.append("SELECT " + lobLessFieldsString + " FROM " + schemaTableName + " "
                                        + "WHERE " + searchQueryString.toString() + " " + "ORDER BY "
                                        + identifierQuoteString
                                        + columnNamesHashMap.get(sortComboBox.getSelectedItem())
                                        + identifierQuoteString + " " + ascDescString + " " + "LIMIT "
                                        + tableRowLimit + " " + "OFFSET " + tableRowStart);  
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
               //                    columnName + " " + columnClass + " " +
               //                    columnType + " " + columnSize + " " +
               //                    preferredColumnSize + " " + keyLength);

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
                  // Time
                  else if (columnType.equals("TIME"))
                  {
                     currentContentData = rs.getTime(columnName);
                     tableData[i][j++] = (new SimpleDateFormat("HH:mm:ss").format(currentContentData));
                  }

                  // =============================================
                  // Time With Time Zone
                  else if (columnType.equals("TIMETZ"))
                  {
                     currentContentData = rs.getTime(columnName);
                     tableData[i][j++] = (new SimpleDateFormat("HH:mm:ss z").format(currentContentData));
                  }

                  // =============================================
                  // Timestamps
                  else if (columnType.equals("TIMESTAMP"))
                  {
                     currentContentData = rs.getTimestamp(columnName);
                     tableData[i][j++] = (new SimpleDateFormat(
                        DBTablesPanel.getGeneralDBProperties().getViewDateFormat()
                        + " HH:mm:ss.SSS").format(currentContentData));
                  }

                  else if (columnType.equals("TIMESTAMPTZ"))
                  {
                     currentContentData = rs.getTimestamp(columnName);
                     tableData[i][j++] = (new SimpleDateFormat(
                        DBTablesPanel.getGeneralDBProperties().getViewDateFormat()
                        + " HH:mm:ss z").format(currentContentData));
                  }
                  
                  // =============================================
                  // BLOB
                  else if (columnType.equals("BLOB"))
                  {
                     // Handles a key Blob
                     if (keyLength != null)
                     {
                        BlobTextKey currentBlobElement = new BlobTextKey();
                        currentBlobElement.setName("BLOB");

                        String content = rs.getString(columnName);

                        if (content.length() > keyLength.intValue())
                           content = content.substring(0, keyLength.intValue());

                        currentBlobElement.setContent(content);
                        tableData[i][j++] = currentBlobElement;
                     }
                     else
                     {
                        tableData[i][j++] = "BLOB";
                     }
                  }

                  // =============================================
                  // Boolean
                  else if (columnClass.indexOf("Boolean") != -1)
                  {
                     tableData[i][j++] = rs.getString(columnName);
                     ;
                  }

                  // =============================================
                  // Text
                  else if (columnClass.indexOf("Object") != -1 && columnType.equals("TEXT")
                           && columnSize > 255)
                  {
                     String stringName;
                     //stringName = (String) currentContentData;
                     stringName = rs.getString(columnName);

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
         ConnectionManager.displaySQLErrors(e, "TableTabPanel_SQLite loadTable()");
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
            ConnectionManager.displaySQLErrors(sqle, "TableTabPanel_SQLite loadTable()");
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
               ConnectionManager.displaySQLErrors(sqle, "TableTabPanel_SQLite loadTable()");
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
                                            + identifierQuoteString
                                            + " LIKE '" + keyString + "%' AND ");
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
                     currentColumnType = (String) columnTypeHashMap.get(parseColumnNameField(currentDB_ColumnName));
                     if (currentColumnType.equals("DATE"))
                     {
                        sqlStatementString.append(identifierQuoteString + currentDB_ColumnName
                                                  + identifierQuoteString + "='"
                                                  + MyJSQLView_Utils.convertViewDateString_To_DBDateString(
                                                     currentContentData + "", DBTablesPanel.getGeneralDBProperties().getViewDateFormat())
                                                  + "' AND ");
                     }
                     else
                        sqlStatementString.append(identifierQuoteString + currentDB_ColumnName
                                                  + identifierQuoteString + "='"
                                                  + currentContentData + "' AND ");
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
               
               // System.out.println("field:" + currentDB_ColumnName + " class:" + currentColumnClass
               //                    + " type:" + currentColumnType + " value:" + currentContentData);
               
               // Skip Blob, Text, & Float Unless NULL.
               if ((currentColumnClass.indexOf("Object") != -1 && currentColumnType.equals("TEXT"))
                    || (currentColumnType.indexOf("BLOB") != -1) 
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
                  else if (currentColumnType.equals("DATETIME") || currentColumnType.equals("TIMESTAMP"))
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
            //                    currentColumnType + " " + /*columnSize +*/ " " + currentContentData);

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
               else if (currentColumnType.equals("DATETIME"))
               {
                  String dateString = currentContentData + "";
                  dateString = dateString.substring(0, (dateString.indexOf(" ")));
                  dateString = displayMyDateString(dateString);

                  String timeString = currentContentData + "";
                  timeString = timeString.substring(timeString.indexOf(" "));
                  currentContentData = dateString + timeString;
                  tableViewForm.setFormField(currentColumnName, currentContentData);
               }
               
               // Time
               else if (currentColumnType.equals("TIME"))
               {
                  currentContentData = db_resultSet.getTime(currentDB_ColumnName);
                  tableViewForm.setFormField(currentColumnName, (new SimpleDateFormat("HH:mm:ss")
                        .format(currentContentData)));
               }

               // Time With Time Zone
               else if (currentColumnType.equals("TIMETZ"))
               {
                  currentContentData = db_resultSet.getTime(currentDB_ColumnName);
                  tableViewForm.setFormField(currentColumnName, (new SimpleDateFormat("HH:mm:ss z")
                        .format(currentContentData)));
               }

               // Timestamps Type Field
               else if (currentColumnType.equals("TIMESTAMP"))
               {
                  currentContentData = db_resultSet.getTimestamp(currentDB_ColumnName);
                  tableViewForm.setFormField(currentColumnName, (new SimpleDateFormat(
                     DBTablesPanel.getGeneralDBProperties().getViewDateFormat()
                     + " HH:mm:ss.SSS").format(currentContentData)));
               }

               else if (currentColumnType.equals("TIMESTAMPTZ"))
               {
                  currentContentData = db_resultSet.getTimestamp(currentDB_ColumnName);
                  tableViewForm.setFormField(currentColumnName,
                     (new SimpleDateFormat(DBTablesPanel.getGeneralDBProperties().getViewDateFormat()
                        + " HH:mm:ss z").format(currentContentData)));
               }

               // Blob Type Field
               else if (currentColumnType.indexOf("BLOB") != -1)
               {
                  if (((String) currentContentData).getBytes().length != 0)
                  {
                     currentContentData = db_resultSet.getBytes(currentDB_ColumnName);

                     int size = ((byte[]) currentContentData).length;
                     tableViewForm.setFormField(currentColumnName, (Object) ("BLOB " + size + " Bytes"));
                     tableViewForm.setFormFieldBlob(currentColumnName, (byte[]) currentContentData);
                  }
                  else
                     tableViewForm.setFormField(currentColumnName, (Object) "BLOB 0 Bytes");
               }

               // Text, Fields
               else if (currentColumnClass.indexOf("Object") != -1 && currentColumnType.equals("TEXT"))
               {
                  if (((String) currentContentData).getBytes().length != 0)
                  {
                     int size = ((String) currentContentData).getBytes().length;
                     
                     if (size > 255)
                     {
                        tableViewForm.setFormField(currentColumnName, (Object) ("TEXT " + size + " Bytes"));
                        tableViewForm.setFormFieldText(currentColumnName, (String) currentContentData);
                     }
                     else
                        tableViewForm.setFormField(currentColumnName, currentContentData);
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
         ConnectionManager.displaySQLErrors(e, "TableTabPanel_SQLite viewSelectedItem()");
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
            ConnectionManager.displaySQLErrors(sqle, "TableTabPanel_SQLite viewSelectedItem()");
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
      String currentColumnType;

      // Showing the Table Entry Form
      TableEntryForm addForm = new TableEntryForm("Add Table Entry: ", true, schemaTableName,
                                                  -1, null, primaryKeys,
                                                  autoIncrementHashMap, null,
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
         currentColumnType = columnTypeHashMap.get(currentColumnName);

         // Auto-Increment Type Field
         if (autoIncrementHashMap.containsKey(currentColumnName))
         {
            currentContentData = "AUTO";
            addForm.setFormField(currentColumnName, currentContentData);
         }

         // ENUM, BOOLEAN Single Bit, & SET Type Fields
         if (columnEnumHashMap.containsKey(currentColumnName)
             || columnSetHashMap.containsKey(currentColumnName))
         {
            try
            {
               setSpecialFieldData(addForm, dbConnection, currentColumnName, null);
            }
            catch (SQLException e)
            {
               ConnectionManager.displaySQLErrors(e, "TableTabPanel_SQLite addItem()");
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

         // TIMESTAMP Type Field
         if (currentColumnType.equals("TIMESTAMP") || currentColumnType.equals("TIMESTAMPTZ"))
         {
            currentContentData = "NOW()";
            addForm.setFormField(currentColumnName, currentContentData);
         }

         // BLOB Type Field
         if (currentColumnType.indexOf("BLOB") != -1)
         {
            addForm.setFormField(currentColumnName, (Object) ("BLOB Browse"));
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
                                                  currentContentData + "", DBTablesPanel.getGeneralDBProperties().getViewDateFormat())
                                               + "' AND ");
                  }
                  else
                     sqlStatementString.append(identifierQuoteString + currentDB_ColumnName
                                               + identifierQuoteString + "='" + currentContentData
                                               + "' AND ");
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

            // ENUM, BOOLEAN Single Bit, & SET Type Fields
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
            
            // TIME
            else if (currentColumnType.equals("TIME"))
            {
               if (currentContentData != null)
               {
                  currentContentData = db_resultSet.getTime(currentDB_ColumnName);
                  editForm.setFormField(currentColumnName, ((Object) new SimpleDateFormat("HH:mm:ss")
                        .format(currentContentData)));
               }
               else
                  editForm.setFormField(currentColumnName, (Object) "HH:MM:SS");

            }

            // TIME With Time Zone
            else if (currentColumnType.equals("TIMETZ"))
            {
               if (currentContentData != null)
               {
                  currentContentData = db_resultSet.getTime(currentDB_ColumnName);
                  editForm.setFormField(currentColumnName, ((Object) new SimpleDateFormat("HH:mm:ss z")
                        .format(currentContentData)));
               }
               else
                  editForm.setFormField(currentColumnName, (Object) "HH:MM:SS");

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
                        + " HH:mm:ss.SSS").format(currentContentData)));
               }
               else
                  editForm.setFormField(currentColumnName,
                     (Object) DBTablesPanel.getGeneralDBProperties().getViewDateFormat() + " HH:MM:SS");
            }

            else if (currentColumnType.equals("TIMESTAMPTZ"))
            {
               if (currentContentData != null)
               {
                  currentContentData = db_resultSet.getTimestamp(currentDB_ColumnName);
                  // System.out.println(currentContentData);
                  editForm.setFormField(currentColumnName,
                     (Object) (new SimpleDateFormat(DBTablesPanel.getGeneralDBProperties().getViewDateFormat()
                        + " HH:mm:ss z").format(currentContentData)));
               }
               else
                  editForm.setFormField(currentColumnName,
                     (Object) DBTablesPanel.getGeneralDBProperties().getViewDateFormat() + " HH:MM:SS");
            }

            // Blob/Bytea Type Field
            else if ((currentColumnClass.indexOf("String") == -1 &&
                      currentColumnType.indexOf("BLOB") != -1) ||
                      currentColumnType.indexOf("BYTEA") != -1 ||
                      currentColumnType.indexOf("BINARY") != -1)
            {
               String binaryType;
               if (currentColumnType.indexOf("BLOB") != -1)
                  binaryType = "BLOB";
               else if (currentColumnType.indexOf("BYTEA") != -1)
                  binaryType = "BYTEA";
               else
                  binaryType = "BINARY";

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

            // All Text But TinyText Type Field
            else if (currentColumnClass.indexOf("String") != -1 && !currentColumnType.equals("CHAR")
                     && currentColumnSize > 255)
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
         ConnectionManager.displaySQLErrors(e, "TableTabPanel_SQLite editSelectedItem()");
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
            ConnectionManager.displaySQLErrors(sqle, "TableTabPanel_SQLite editSelectedItem()");
         }
         finally
         {
            if (sqlStatement != null)
               sqlStatement.close();
         }
         if (sqlStatement != null)
            sqlStatement.close();
      }
   }
}
