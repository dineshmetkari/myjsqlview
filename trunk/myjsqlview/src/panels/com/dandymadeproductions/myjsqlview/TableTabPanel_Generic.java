//=============================================================
//          MyJSQLView Table TabPanel_Generic
//=============================================================
//
//    This class provides the means to create a default table
// summary view of data in an unknown database that in MyJSQLView
// is listed according to a specified sort and search. Entries
// from the database table may be viewed, added, edited, or
// deleted by means of this panel. The panel also provides the
// mechanism to page through the database table's data.
//
//           << TableTabPanel_Generic.java >>
//
//================================================================
// Copyright (C) 2005-2012 Dana M. Proctor
// Version 11.0 05/28/2012
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
// Version 1.0 Original TableTabPanel_Generic Class.
//         1.1 Moved Class Instances and All Non-Essential Class Methods to
//             Parent, TableTabPanel.
//         1.2 Removed doneButton Replaced With closeViewButton. Revamped
//             Actions Buttons Panel. Changed record to row.
//         1.3 Added Binary Detection in Class Method viewSelectedItem(),
//             editSelectedItem(), and deleteSelecteditem().
//         1.4 Class Method setTableFields() Renamed to setTableHeadings().
//             Stored Actual Database Column Nmaes in fields Vector.
//         1.5 Class Instance tableHeadings Changed to currentTableHeadings.
//             Added Class Instance allTableHeadings.
//         1.6 Reduced Text Data Type Field Size Storage in Summary Table
//             from 65535 to 512 Characters in Class Method loadTable().
//         1.7 Removed the trim() From the Default Content Placed in the
//             listTable in Class Method loadTable(). This causes Problems
//             With Keys that Have Leading/Trailing Spaces.
//         1.8 Escaped Key Strings in Class Methods viewSelectedItem(), 
//             editSelectedItem(), & deleteSelectedItem().
//         1.9 Correction for Induced Problem With Prior Fix for Escape
//             Key Strings.
//         2.0 Class Method loadTable() Instance preferredColumnSize Determined
//             by A trim() of the Contents of Table. Compensation for Character
//             Fields That Pad With Spaces.
//         2.1 Class Method getColumnNames() and loadTable() Changed to Return
//             Type Boolean. Check at Calling of These Methods So That TableTabPanel
//             Can Allow the MyJSQLView Application to Exit Instead of Crash If
//             Things Go Astray.
//         2.2 Implemented a Fully Qualified Table Name That Includes the Schema
//             and Table Name. Class Instance schemaTableName.
//         2.3 Added Class Instance serialVersionUID. Moved schemaTableName
//             to Parent.
//         2.4 Established the Control of the Summary Table Fields List Through
//             sqlTableFieldsString. Class Methods Effected getColumnNames(),
//             loadTable(), & setTableHeadings().
//         2.5 Added sqlTableStatement Instance in Class Method
//             loadTable().
//         2.6 Removed else if for Geometric Data Types in addItem() Class Method.
//         2.7 Class Method getColumnNames() Closed Method Instance rs.
//         2.8 Fault Tolerance Test in Class Method getColumnNames() for Class
//             & Type Columns Definitions That May be NULL. Thanks Oracle Sweet.
//         2.9 Class Instance searchTextField Cleared on No Field Selection in
//             searchComboBox. Constructor and actionPerformed() Method Changes.
//         3.0 Comment Removal.
//         3.1 On searchTextField Clearing Fired searchButton in Class Method
//             actionPerformed().
//         3.2 Removed sortButton and Its Action and Replaced With the
//             sortCombobBox.
//         3.3 Search Panel Redesign. Search Button Text Replaced With an Icon.
//         3.4 Fixed Bug of During Delete of Selecting a Empty Row Throws a
//             NULL Pointer. Class Method deleteSelectedItem().
//         3.5 Added clearSearchTextFieldButton and Its Actions.
//         3.6 Implemented Search Text Field Saving via Class Instance
//             columnsSearchString.
//         3.7 Removed Loading of ImageIcon for the Various Buttons to Parent
//             TableTabPanel. Class Instance previous/nextButton Changed
//             to previous/nextTableRowsButton.
//         3.8 Implemeted Basic ASC/DESC Sorts Through Instances ascSortRadioButton,
//             descSortRadioButton, and ascDescString. Class Instances Effected
//             Constructor, actionPerformed(), & loadTable().
//         3.9 Class Method loadTable() Added Instance searchQueryString, & Used
//             to Defined the Way the WHERE Columns LIKE Aspect of the Query is
//             Defined. Essentially All Columns Search for Key Phase. Removed
//             Clearing Action of searchTextField When searchComboBox Set None.
//         4.0 Removed Class Instance columnsSearchStrings and Its Actions.
//         4.1 Popup MenuItem for listTable. Actions in actionPerformed() & Reseting
//             in setTableHeadings().
//         4.2 Applied MyJSQLView Standard Code Reformatting. Changed the Way the
//             Popup Menu is Created for the listTable. Added Class Methods menuItem()
//             & createListTablePopupMenu().
//         4.3 Added Conditional Check for NULL Connection in Class Methods 
//             actionPerformed() & setTableHeadings().
//         4.4 Class Method createListTablePopupMenu() Only Allowed View, Add, Edit,
//             & Delete Popup Item for Tables That Can be Modified via a Key.
//         4.5 Class Method setTableHeadings() Check for No sqlTableFields.
//         4.6 Class Methods viewSelectedItem(), editSelectedItem(), &
//             deletedSelectedItems() Modified So a Date Key is Properly Converted
//             Back to the Database Format for Processing. Cleaned Up Code for Key
//             Processing in Same Methods.
//         4.7 Class Method actionPerformed() Changed Component Type Conditional
//             From if to if/else if.
//         4.8 Added Class Method executeActions(). Also actionThread in Class
//             Method actionPerformed() to Handle Events.
//         4.9 Class Method setTableHeadings() Corrections to Connection Information
//             String.
//         5.0 Moved Processing of dummyButton From Thread in Class Method
//             executeActions() to Method actionPerformed().
//         5.1 Class Method actionPerformed() Provided a String Name for the
//             Thread actionThread.
//         5.2 Provided a Conformalization of DATE Field Types By Obtaining Data
//             From Database via the getDate() Method. Class Methods Effected
//             loadTable(), viewSelectedItem(), & editSelectedItem().
//         5.3 Cleared the TableViewForm's Blob Byte HashMash in the Class Method
//             executeActions() via clearBlobBytesHashMap() on next/previousViewButton.
//         5.4 Moved the advancedSortSearchFrame Creation Over to the New Interface
//             Method createAdvancedSortSearchFrame() So That it Could Be Accessed
//             During a setState().
//         5.5 Class Instance tableScrollPane Set the VerticalScrollPolicy to Always.
//             Class Contructor & Method setTableHeadings().
//         5.6 Removed Reference to Class Instance settingState.
//         5.7 Class Instance settingState Back.
//         5.8 Exclusion of Blob Fields From Initially Loaded Through currentTableFields
//             in Class Method getColumnNames().
//         5.9 Included Class Instance sqlTableFieldsString in Exclusion of Blob Fields
//             in Class Method getColumnNames(). TEMPORARY PERFORMANCE ISSUE SOLUTION.
//         6.0 Class Method actionPerformed() Changed the Handling of closeViewButton
//             & searchComboBox Actions Since They Do Not Need Connection Processing.
//             Removed Them From executeActions().
//         6.1 Corrected Connection String Identification for Debug in Class Method
//             executeActions().
//         6.2 TableEntryForm Constructor Change to Pass Arguments selectedRow, and this
//             TableTabPanel. Added Argument selectedRow int to Class Method editSelectedItem().
//         6.3 Implemented a Saving/Restoring of the Current selectedRow, & tableScrollPane
//             ScrollBars. Class Methods Effected actionPerformed(), executeActions(),
//             viewSelectedItem(), and editSelectedItem(). Changed selectedRow Argument
//             in editSelectedItem() to rowToEdit, & Added Argument rowToView to
//             viewSelectedItem().
//         6.4 Moved activateAdvancedSortSearchButton Action Processing From executeActions()
//             Method Back to actionPerformed() Because it Takes No Initial Connection Creation.
//         6.5 Implemented Update Feature. Activation via LOWER_RIGHT_CORNER of the
//             tableScrollPane. Added Class Method createUpdateFrame(). Class Methods
//             Effected Constructor(), actionPerformed(), executeActions(), setTableHeadings().
//             Added sqlTable to TableEntryForm Title.
//         6.6 Implemented Delete All Items Feature. Class Instance deletAllButton Added Only
//             As a Place Holder. Selection via summaryTablePopupMenu. Class Methods Effected
//             Constructor(), createListTablePopupMenu(), actionPerformed(), & executeActions().
//             Added Class Method deleteAllItems().
//         6.7 Added deleteDataIcon to Dialog in Class Methods deleteSelectedItems() and
//             deleteAllItems().
//         6.8 Implemented a Copy/Paste Popup Menu Addition to the listTable. Implemented the
//             Copy Feature, But Just Provided the Outline for the Paste. Class Methods Effected
//             Constructor(), actionPerformed(), createListTablePopupMenu(), and setTableheadings().
//         6.9 Implemented the Basic Paste Feature for the listTable, in actionPerformed().
//             Added Class Method pasteClipboardContents().
//         7.0 Class Method pasteClipboardContents() Modified tableHeadingsString to be Derived
//             Directly From the listTable, Columns Therefore Moveable.
//         7.1 Added confirmCheckbox and Font Changes to message in Classs Method deleteAllItems().
//         7.2 Class Method pasteClipboardContents() Changed the Method Used to Obtain the Clipboard
//             Contents, Checked for NULL, and Catch for IllegalStateException.
//         7.3 Class Method createUpdateFrame() Arguments columnClass/Type/SizeHashMap.
//         7.4 Class Method createListTablePopupMenu() Paste Excluded if No Keys Found.
//         7.5 Integration of TableTabPanels. Moved All Common Methods to the TableTabPanel.
//         7.6 Class Method loadTable() Changed searchQueryString From String to StringBuffer.
//         7.7 Changed Package to Reflect Dandy Made Productions Code.
//         7.8 Class Methods viewSelectedItem() and editSelectedItem() Changed Method Instance
//             sqlStatementString to a StringBuffer.
//         7.9 Removed the Exclusion of Blob, Bytea, & Binary Types in Initial Loading, Class
//             Method getColumnNames().
//         8.0 Implemented Fix For the Performance Issue That was Temporarily Corrected in
//             5.8 & 5.9. Permanently Excluded Any Loading of LOBs in the Summary Table,
//             Unless They Are Identified As Keys. Class Methods Effected getColumnNames()
//             & loadTable(). Added Instance lobLessSQLStatementString to Class Method
//             loadTable().
//         8.1 Changed | to || in Class Method loadTable() LOB Conditional Check.
//         8.2 Class Method getColumnNames() Changed Instance columnSize From Type Object
//             to Integer. Class Method loadTable() Changed Instance keyLength From String
//             to Integer. Removed Unecessary Casts for the HashMaps.
//         8.3 Class Methods getColumnNames() and loadTable() Assigned Integer via valueOf()
//             Instead of new Integer for Instances columnSize & preferredColumnSizeHashMap.
//         8.4 Parameterized headings in Method loadTable(), keyIterator & textFieldIterator
//             in viewSelectedItem() & editSelectedItem(), and textFieldNamesIterator in
//             addItem().
//         8.5 Made the Class and Constructor Public.
//         8.6 Implemented a View Only Table, via Constructor Argument viewOnlyTable.
//         8.7 Assigned searchQueryString to sqlTableSearchString in loadTable().
//         8.8 Undid Last Revision. Short Sighted.
//         8.9 Check for All Fields Possibly LOBs. Class Method loadTable().
//         9.0 Class Method getColumnNames() Moved the Final Check for primaryKeys, Foreign
//             Keys, Back To End of Script Because Depends on columnNamesHashMap.
//         9.1 Class Methods loadTable(), viewSelectedItem(), addItem() & editSelectedItem()
//             Changed Default Entry for Date/DateTime/TimeStamp Type Entry to
//             GeneralProperties.getDateViewFormat(). Class Methods view/editSelectedItem()
//             Change for Date Key Conversion to MyJSQLView_utils.convertViewDateString_To_
//             DBDateString().
//         9.2 Class Method loadTable() Changes to Give the Ability to Properly Search
//             Given Input for Date/DateTime/Timestamp Fields.
//         9.3 Changes to Class Methods getColumnNames(), loadTable(), viewSelectedItem(),
//             & deleteSelectedItem() to Used Newly Redefined ConnectionManager to Display
//             SQL Errors.
//         9.4 Corrections in Method loadTable to Properly Build searchTextString For Date,
//             DateTime, & Timestamp When No Specific Column is Selected.
//         9.5 Called saveHistory() in Class Method loadTable().
//         9.6 Changed the Conditional Check for saveAction by Removing the NOT Logic.
//         9.7 Correction in editSelectedItem() for Assignment of currentContentData for
//             DATE Types. Correction in loadTable() for Not Modifiying searchTextString
//             During Composition When No Field Specified.
//         9.8 Class Methods viewSelectedItem(), addItem(), & editSelectedItem() Added
//             the Processing of Data Types Clob to be Treated Like Text, LongVarCharacters.
//         9.9 Set sqlTableStatement to an Empty String Prior to Each New
//             Assignment in Class Method loadTable().
//        10.0 Set sqlTableStatement to NULL and Then Created a new String() for it of
//             sqlStatementString to Insure it is Flushed in Class Method loadTable().
//        10.1 Set sqlTableStatement to Not be a new String() of sqlStatementString in
//             Method loadTable().
//        10.2 Removed Method Instance sqlStatementString & Replaced With Parent
//             Class Instance sqlTableStatement.
//        10.3 Class Method addItem() Added a try catch for setSpecialFields(). Methods
//             viewSelectedItem(), editSelectedItem() & getColumnNames() Throws for
//             SQLException Through finally Clause for Closing sqlStatment.
//        10.4 Changes in loadTable to Add Back Instance sqlStatementString and Then
//             Have sqlTableStatement New StringBuffer Designation Loaded From it.
//        10.5 Method loadTable() Conversion of Date From searchString Failed, Due
//             to Possible Generic Search of All Fields for Given Characters. So
//             Just Use Original Characters.
//        10.6 Removed sqlStatementString in loadTable(), Replaced With sqlTableStatement.
//        10.7 Class Method loadTable() Insured That sqlTableStatement is Given a
//             New Instance Before Reasignment. Replaced delete() Which Was Not
//             Propagating Changes to Object Outside the Method.
//        10.8 Change in viewSelectedItem() to Use Brute Force WHERE Creation for
//             Selected listTable Entry if primaryKeys().isEmpty().
//        10.9 Change in Class Method getColumnNames() of Adding Items to New ArrayList
//             Instances by Way of add() Instead of addElement().
//        11.0 Change in getColumnNames() to Always Check for Foreign Keys.
//             
//-----------------------------------------------------------------
//                  danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Iterator;

/**
 *    The TableTabPanel_Generic class provides the means to create a default table
 * summary view of data in an unknown database that in MyJSQLView is listed
 * according to a specified sort and search. Entries from the database table may
 * be viewed, added, edited, or deleted by means of this panel. The panel also
 * provides the mechanism to page through the database table's data.
 * 
 * @author Dana M. Proctor
 * @version 11.0 05/28/2012
 */

public class TableTabPanel_Generic extends TableTabPanel
{
   // Class Instances.
   private static final long serialVersionUID = 7742168062289517814L;

   //===========================================================
   // TableTabPanel Constructor
   //===========================================================

   public TableTabPanel_Generic(String table, Connection setup_dbConnection, boolean viewOnlyTable)
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
            // comboBoxNameString + " " +
            // columnClass + " " + columnType + " " +
            // columnSize);

            // This going to be a problem so skip this column.

            if (columnClass == null && columnType == null)
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
            if (((columnClass.indexOf("String") == -1 && columnType.toUpperCase().indexOf("BLOB") != -1)
                  || ((columnClass.indexOf("String") != -1 && !columnType.toUpperCase().equals("CHAR")
                      && columnSize.intValue() > 65535))
                  || (columnType.toUpperCase().indexOf("BINARY") != -1)
                  || (columnType.toUpperCase().indexOf("BLOB") != -1)
                  || (columnType.toUpperCase().indexOf("RAW") != -1)
                  || (columnType.toUpperCase().indexOf("LONG") != -1)
                  || (columnType.toUpperCase().indexOf("CLOB") != -1)
                  || (columnType.toUpperCase().indexOf("BYTEA") != -1)
                  || (columnClass.indexOf("String") != -1 && columnType.toUpperCase().equals("TEXT")))
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

         rs.close();
         db_resultSet.close();
         return true;
      }
      catch (SQLException e)
      {
         ConnectionManager.displaySQLErrors(e, "TableTabPanel_Generic getColumnNames()");
         return false;
      }
      finally
      {
         if (sqlStatement != null)
            sqlStatement.close();
      }
   }

   //==============================================================
   // Class method to load the current table's data. The routine
   // will apply the sort and search parameters.
   //==============================================================
   
   public boolean loadTable(Connection dbConnection)
   {
      // Method Instances
      String lobLessSQLStatementString;
      Statement sqlStatement;
      ResultSet rs;

      StringBuffer searchQueryString;
      String columnSearchString, searchTextString;
      String lobLessFieldsString;
      String columnName, columnClass, columnType;
      int columnSize, preferredColumnSize;
      Integer keyLength;
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
               else if (columnType.equals("DATETIME") || columnType.equals("TIMESTAMP"))
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
            else if (columnType.equals("DATETIME") || columnType.equals("TIMESTAMP") ||
                     columnType.equals("TIMESTAMPTZ"))
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

         if (advancedSortSearch)
         {
            // Complete With All Fields.
            sqlTableStatement.append(advancedSortSearchFrame.getAdvancedSortSearchSQL(sqlTableFieldsString,
                                             tableRowStart, tableRowLimit));
            // Summary Table Without LOBs
            lobLessSQLStatementString = advancedSortSearchFrame.getAdvancedSortSearchSQL(lobLessFieldsString,
                                                    tableRowStart, tableRowLimit);
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
            lobLessSQLStatementString = "SELECT " + lobLessFieldsString + " FROM " + schemaTableName + " "
                                        + "WHERE " + searchQueryString.toString() + " " + "ORDER BY "
                                        + identifierQuoteString
                                        + columnNamesHashMap.get(sortComboBox.getSelectedItem())
                                        + identifierQuoteString + " " + ascDescString + " " + "LIMIT "
                                        + tableRowLimit + " " + "OFFSET " + tableRowStart;  
         }
         // System.out.println(sqlTableStatement);
         // System.out.println(lobLessSQLStatementString);
         rs = sqlStatement.executeQuery(lobLessSQLStatementString);

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
                                           DBTablesPanel.getGeneralProperties().getViewDateFormat()
                                           + " HH:mm:ss").format(currentContentData));
                  }

                  else if (columnType.equals("TIMESTAMPTZ"))
                  {
                     currentContentData = rs.getTimestamp(columnName);
                     tableData[i][j++] = (new SimpleDateFormat(
                                           DBTablesPanel.getGeneralProperties().getViewDateFormat()
                                           + " HH:mm:ss z").format(currentContentData));
                  }
                  
                  // =============================================
                  // BLOB, BINARY, BYTEA, RAW, LONG, RAW, CLOB,
                  // LONG VARCHARS, & TEXT
                  else if ((columnType.equals("BLOB")) || (columnType.equals("BINARY"))
                        || (columnType.equals("RAW")) || (columnType.equals("LONG"))
                        || (columnType.equals("CLOB")) || (columnType.equals("BYTEA"))
                        || ((columnClass.indexOf("String") != -1 && !columnType.equals("CHAR")
                             && columnSize > 65535))
                        || (columnClass.indexOf("String") != -1 && columnType.toUpperCase().equals("TEXT")))
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
                     ;
                  }

                  // =============================================
                  // Text
                  else if (columnClass.indexOf("String") != -1 && !columnType.equals("CHAR")
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
         rs.close();
         sqlStatement.close();
         return true;
      }
      catch (SQLException e)
      {
         ConnectionManager.displaySQLErrors(e, "TableTabPanel_Generic loadTable()");
         return false;
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
      int columnSize;
      int keyColumn = 0;

      // Connecting to the data base, to obtain
      // the selected entry.
      
      sqlStatement = null;
      
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
                                                  + MyJSQLView_Utils.convertViewDateString_To_DBDateString(currentContentData + "",
                                                     DBTablesPanel.getGeneralProperties().getViewDateFormat())
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
               columnSize = columnSizeHashMap.get(listTable.getColumnName(i)).intValue();
               
               // System.out.println("field:" + currentDB_ColumnName + " class:" + currentColumnClass
               //                    + " type:" + currentColumnType + " value:" + currentContentData);
               
               // Skip Blob, Text, & Float Unless NULL.
               if (((currentColumnClass.indexOf("String") == -1 && currentColumnType.indexOf("BLOB") != -1) ||
                     (currentColumnType.indexOf("BYTEA") != -1) ||
                     (currentColumnType.indexOf("BINARY") != -1))
                     || ((currentColumnClass.indexOf("String") != -1 && !currentColumnType.equals("CHAR")
                          && columnSize > 255)
                         || currentColumnType.indexOf("CLOB") != -1)
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
            // currentDB_ColumnName + " " +
            // currentColumnType + " " + columnSize + " " + currentContentData);

            if (currentContentData != null)
            {
               // DATE Type Field
               if (currentColumnType.equals("DATE"))
               {
                  db_resultSet.getDate(currentDB_ColumnName);
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
                  tableViewForm.setFormField(currentColumnName,
                     (new SimpleDateFormat(DBTablesPanel.getGeneralProperties().getViewDateFormat()
                        + " HH:mm:ss").format(currentContentData)));
               }

               else if (currentColumnType.equals("TIMESTAMPTZ"))
               {
                  currentContentData = db_resultSet.getTimestamp(currentDB_ColumnName);
                  tableViewForm.setFormField(currentColumnName,
                     (new SimpleDateFormat(DBTablesPanel.getGeneralProperties().getViewDateFormat()
                        + " HH:mm:ss z").format(currentContentData)));
               }

               // Blob/Bytea/Binary Type Field
               else if ((currentColumnClass.indexOf("String") == -1 &&
                         currentColumnType.indexOf("BLOB") != -1) ||
                        (currentColumnType.indexOf("BYTEA") != -1) ||
                        (currentColumnType.indexOf("BINARY") != -1))
               {
                  if (((String) currentContentData).getBytes().length != 0)
                  {
                     currentContentData = db_resultSet.getBytes(currentDB_ColumnName);

                     int size = ((byte[]) currentContentData).length;
                     if (currentColumnType.equals("BLOB"))
                     {
                        tableViewForm.setFormField(currentColumnName, (Object) ("BLOB " + size + " Bytes"));
                        tableViewForm.setFormFieldBlob(currentColumnName, (byte[]) currentContentData);
                     }
                     else if (currentColumnType.equals("BYTEA"))
                     {
                        tableViewForm.setFormField(currentColumnName, (Object) ("BYTEA " + size + " Bytes"));
                        tableViewForm.setFormFieldBlob(currentColumnName, (byte[]) currentContentData);
                     }
                     else
                     {
                        tableViewForm.setFormField(currentColumnName, (Object) ("BINARY " + size + " Bytes"));
                        tableViewForm.setFormFieldBlob(currentColumnName, (byte[]) currentContentData);
                     }
                  }
                  else
                  {
                     if (currentColumnType.equals("BLOB"))
                        tableViewForm.setFormField(currentColumnName, (Object) "BLOB 0 Bytes");
                     else if (currentColumnType.equals("BYTEA"))
                        tableViewForm.setFormField(currentColumnName, (Object) "BYTEA 0 Bytes");
                     else
                        tableViewForm.setFormField(currentColumnName, (Object) "BINARY 0 Bytes");

                  }
               }

               // Text, & Clob Fields
               else if ((currentColumnClass.indexOf("String") != -1 && !currentColumnType.equals("CHAR")
                         && (columnSizeHashMap.get(currentColumnName)).intValue() > 255)
                        || currentColumnType.indexOf("CLOB") != -1)
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
         db_resultSet.close();
      }
      catch (SQLException e)
      {
         ConnectionManager.displaySQLErrors(e, "TableTabPanel_Generic viewSelectedItem()");
      }
      finally
      {
         if (sqlStatement != null)
            sqlStatement.close();
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

      addForm.disposeButton.addActionListener(this);
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
               ConnectionManager.displaySQLErrors(e, "TableTabPanel_Generic addItem()");
            }  
         }

         // DATE Type Field
         if (currentColumnType.equals("DATE"))
         {
            currentContentData = DBTablesPanel.getGeneralProperties().getViewDateFormat();
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

         // BLOB/BYTEA/BINARY Type Field
         if ((currentColumnClass.indexOf("String") == -1 && currentColumnType.indexOf("BLOB") != -1)
             || currentColumnType.indexOf("BYTEA") != -1 || currentColumnType.indexOf("BINARY") != -1)
         {
            if (currentColumnType.equals("BLOB"))
               addForm.setFormField(currentColumnName, (Object) ("BLOB Browse"));
            else if (currentColumnType.equals("BYTEA"))
               addForm.setFormField(currentColumnName, (Object) ("BYTEA Browse"));
            else
               addForm.setFormField(currentColumnName, (Object) ("BINARY Browse"));
         }

         // Geometric Type Fields
         if (currentColumnType.equals("POINT") || currentColumnType.equals("LSEG")
             || currentColumnType.equals("BOX") || currentColumnType.equals("PATH")
             || currentColumnType.equals("POLYGON") || currentColumnType.equals("CIRCLE"))
         {
            if (currentColumnType.equals("POINT"))
               addForm.setFormField(currentColumnName, (Object) ("(x,y)"));
            else if (currentColumnType.equals("LSEG"))
               addForm.setFormField(currentColumnName, (Object) ("[(x1,y1),(x2,y2)]"));
            else if (currentColumnType.equals("BOX"))
               addForm.setFormField(currentColumnName, (Object) ("(x1,y1),(x2,y2)"));
            else if (currentColumnType.equals("PATH"))
               addForm.setFormField(currentColumnName, (Object) ("[(x1,y1), ... (xn,yn)]"));
            else if (currentColumnType.equals("POLYGON"))
               addForm.setFormField(currentColumnName, (Object) ("((x1,y1), ... (xn,yn))"));
            else
               addForm.setFormField(currentColumnName, (Object) ("<(x,y),r>"));
         }

         // All TEXT, MEDIUMTEXT & LONGTEXT Type Field
         if ((currentColumnClass.indexOf("String") != -1 && !currentColumnType.equals("CHAR")
              && (columnSizeHashMap.get(currentColumnName)).intValue() > 255)
             || (currentColumnType.indexOf("CLOB") != -1))
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
      editForm.disposeButton.addActionListener(this);
      editForm.center();
      editForm.setVisible(true);

      // Connecting to the data base, to obtain
      // the selected entries field data.
      
      sqlStatement = null;

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
                                               + MyJSQLView_Utils.convertViewDateString_To_DBDateString(currentContentData + "",
                                                  DBTablesPanel.getGeneralProperties().getViewDateFormat())
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
                                        (Object) DBTablesPanel.getGeneralProperties().getViewDateFormat());
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
                     (Object) (new SimpleDateFormat(DBTablesPanel.getGeneralProperties().getViewDateFormat()
                                                    + " HH:mm:ss").format(currentContentData)));
               }
               else
                  editForm.setFormField(currentColumnName,
                                        (Object) DBTablesPanel.getGeneralProperties().getViewDateFormat()
                                        + " HH:MM:SS");
            }

            else if (currentColumnType.equals("TIMESTAMPTZ"))
            {
               if (currentContentData != null)
               {
                  currentContentData = db_resultSet.getTimestamp(currentDB_ColumnName);
                  // System.out.println(currentContentData);
                  editForm.setFormField(currentColumnName,
                     (Object) (new SimpleDateFormat(DBTablesPanel.getGeneralProperties().getViewDateFormat()
                               + " HH:mm:ss z").format(currentContentData)));
               }
               else
                  editForm.setFormField(currentColumnName,
                     (Object) DBTablesPanel.getGeneralProperties().getViewDateFormat() + " HH:MM:SS");
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

            // All Text But TinyText & Clob Type Fields
            else if ((currentColumnClass.indexOf("String") != -1 && !currentColumnType.equals("CHAR")
                      && currentColumnSize > 255)
                     || currentColumnType.indexOf("CLOB") != -1)
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
         db_resultSet.close();
      }
      catch (SQLException e)
      {
         ConnectionManager.displaySQLErrors(e, "TableTabPanel_Generic editSelectedItem()");
      }
      finally
      {
         if (sqlStatement != null)
            sqlStatement.close();
      }
   }
}