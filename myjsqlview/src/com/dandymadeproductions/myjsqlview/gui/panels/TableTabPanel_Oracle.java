//=============================================================
//          MyJSQLView Table TabPanel_Oracle
//=============================================================
//
//    This class provides the means to create a default table
// summary view of data in an Oracle database that in MyJSQLView
// is listed according to a specified sort and search. Entries
// from the database table may be viewed, added, edited, or
// deleted by means of this panel. The panel also provides the
// mechanism to page through the database table's data.
//
//              << TableTabPanel_Oracle.java >>
//
//================================================================
// Copyright (C) 2005-2015 Dana M. Proctor
// Version 12.4 07/01/2013
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
// Version 1.0 Original TableTabPanel_Oracle Class.
//         1.1 Basic Functionlality & Correction in Class Method
//             loadTable() to Properly Handle SQL for Advanced Search.
//         1.2 Added Class Instance serialVersionUID. Moved
//             schemaTableName to Parent.
//         1.3 Moved Class Instance sqlTableFieldsString to Parent.
//             Reset sqlTableFieldsString in setTableHeadings().
//         1.4 Added sqlTableStatement Instance in Class Method
//             loadTable().
//         1.5 Proper Detection of BLOB and TIMESTAMPLTZ in Class 
//             Methods viewSelectedItem(), addSelectedItem(), and
//             editSelectedItem().
//         1.6 Proper Display of DATE Fields in View and Edit Form
//             in Class Methods viewSelectedItem() and editSelectedItem().
//             Began Cleaning Out Some.
//         1.7 Implemtation of LONG String Type Fields. Class Methods
//             Effected loadTable(), viewSelectedItem(), addItem(), &
//             editSelectedItem().
//         1.8 Blob Field Type Conditional Check for Contents Changed
//             to ResultSet.getObject() in Class Methods viewSelectedItem()
//             & editSelectedItem().
//         1.9 Implementation of RAW & LONG RAW Data Types Field Processing
//             for Adding, Editing, and Viewing. Class Methods Effected
//             loadTable(), viewSelectedItem(), addItem(), & editSelectedItem().
//         2.0 Initial Implementation of CLOB Data Types Field Processing
//             for Adding, Editing, and Viewing. Class Methods Effected
//             loadTable(), viewSelectedItem(), addItem(), & editSelectedItem().
//             Correction in addItem() for RAW Data Types to Show Binary
//             Button.
//         2.1 Regrouped RAW and BLOB Types Together in Class Methods
//             viewSelectedItem() & editSelectedItem().
//         2.2 Moved CLOB Data Types Into Same Context As BLOB & RAW Types.
//             Kept Intact the Processing Though Still As String Data.
//         2.3 CLOB Data Types On Edit Filled the editForm.setFormFieldText()
//             as String Instead of byte[]. Class Method editSelectedItem().
//         2.4 Excluded Obtaining Index Information for View Table Types in
//             Class Method getColumnNames().
//         2.5 Quoted schemaName & tableName in Obtaining Index Information
//             in Class Method getColumnNames(). None of the Other Methods
//             Required This, Think it is Broken, getIndexInfo().
//         2.6 Class Method getColumnNames() Created Procedure to Collect
//             AutoIncrement Fields. Oracle Does Not Support Directly So
//             Needed, Inefficiently, To Find Associated Column Names 
//             Associated With Sequences.
//         2.7 Fault Tolerance Test in Class Method getColumnNames() for Class
//             & Type Columns Definitions That May be NULL. Oracle Types
//             BINARY_FLOAT/DOUBLE Modified Type & Class.
//         2.8 Implemented a Basic Support for Listing in Table & Adding Oracle
//             BFILE Data Types. Editing and Viewing Not Supported. Class
//             Methods Effected loadTable(), addItem(), & editSelectedItem().
//         2.9 Added Class Instance sqlTableFieldStringLTZ. Implemented Oracle
//             TIMESTAMPLTZ Data Type Field Processing. Class Methods Effected
//             getColumnNames(), loadTable(), viewSelectedItem(), editSelected
//             Item(), & setTableHeadings().
//         3.0 Class Methods view/editSelectedItem() Modified So a Date Key
//             is Properly Converted Back to the Database Format for Processing.
//         3.1 Reviewed. Added Some Comments.
//         3.2 Class Methods loadTable(), viewSelectedItem() & editSelectedItem()
//             Changed currentContentData Extraction from the Database to
//             getDate() Instead of getObject. Accommodates ojdbc5.jar.
//         3.3 Class Instance searchTextField Cleared on No Field Selection in
//             searchComboBox. Constructor and actionPerformed() Method Changes.
//         3.4 On searchTextField Clearing Fired searchButton in Class Method
//             actionPerformed().
//         3.5 Removed sortButton and Its Action and Replaced With the
//             sortCombobBox.
//         3.6 Search Panel Redesign. Search Button Text Replaced With an Icon.
//         3.7 Fixed Bug of During Delete of Selecting a Empty Row Throws a
//             NULL Pointer. Class Method deleteSelectedItem().
//         3.8 Added clearSearchTextFieldButton and Its Actions.
//         3.9 Implemented Search Text Field Saving via Class Instance
//             columnsSearchString.
//         4.0 Removed Loading of ImageIcon for the Various Buttons to Parent
//             TableTabPanel. Class Instance previous/nextButton Changed
//             to previous/nextTableRowsButton.
//         4.1 Implemeted Basic ASC/DESC Sorts Through Instances ascSortRadioButton,
//             descSortRadioButton, and ascDescString. Class Instances Effected
//             Constructor, actionPerformed(), & loadTable.
//         4.2 Class Method loadTable() Added Instance searchQueryString, & Used
//             to Defined the Way the WHERE Columns LIKE Aspect of the Query is
//             Defined. Essentially All Columns Search for Key Phase. Removed
//             Clearing Action of searchTextField When searchComboBox Set None.
//         4.3 Removed Class Instance columnsSearchStrings and Its Actions.
//         4.4 Applied MyJSQLView Standard Code Reformatting. Created Popup Menu for
//             the listTable. Added Class Methods menuItem() & createListTablePopupMenu().
//         4.5 Added Conditional Check for NULL Connection in Class Methods 
//             actionPerformed() & setTableHeadings().
//         4.6 Class Method createListTablePopupMenu() Only Allowed View, Add,
//             Edit, & Delete Popup Item for Tables That Can be Modified via a Key.
//         4.7 Class Method setTableHeadings() Check for No sqlTableFields.
//         4.8 Class Method deleteSelectedItems() Modified So a Date Key is
//             Properly Converted Back to the Database Format for Processing.
//             Cleaned Up Code for Key Processing in Methods viewSelectedItem(),
//             and editSelectedItem().
//         4.9 Class Method actionPerformed() Changed Component Type Conditional
//             From if to if/else if.
//         5.0 Added Class Method executeActions(). Also actionThread in Class
//             Method actionPerformed() to Handle Events.
//         5.1 Class Method setTableHeadings() Corrections to Connection Information
//             String.
//         5.2 Moved Processing of dummyButton From Thread in Class Method
//             executeActions() to Method actionPerformed().
//         5.3 Class Method actionPerformed() Provided a String Name for the
//             Thread actionThread.
//         5.4 Oracle TIMESTAMPTZ Data Type Formatting From General Time Zone, z,
//             to RFC 822, Z. Class Methods loadTable(), viewSelectedItem(), &
//             editSelectedItem().
//         5.5 Cleared the TableViewForm's Blob Byte HashMash in the Class Method
//             executeActions() via clearBlobBytesHashMap() on next/previousViewButton.
//         5.6 Moved the advancedSortSearchFrame Creation Over to the New Interface
//             Method createAdvancedSortSearchFrame() So That it Could Be Accessed
//             During a setState().
//         5.7 Class Instance tableScrollPane Set the VerticalScrollPolicy to Always.
//             Class Contructor & Method setTableHeadings().
//         5.8 Removed Reference to Class Instance settingState.
//         5.9 Class Instance settingState Back.
//         6.0 Exclusion of Blob, Clob, & Raw Fields From Initially Loaded Through
//             currentTableFields in Class Method getColumnNames().
//         6.1 Included Class Instance sqlTableFieldsString in Exclusion of Blob Fields
//             in Class Method getColumnNames(). TEMPORARY PERFORMANCE ISSUE SOLUTION.
//         6.2 Class Method actionPerformed() Changed the Handling of closeViewButton
//             & searchComboBox Actions Since They Do Not Need Connection Processing.
//             Removed Them From executeActions().
//         6.3 Corrected Connection String Identification for Debug in Class Method
//             executeActions().
//         6.4 TableEntryForm Constructor Change to Pass Arguments selectedRow, and this
//             TableTabPanel. Added Argument selectedRow int to Class Method editSelectedItem().
//         6.5 Implemented a Saving/Restoring of the Current selectedRow, & tableScrollPane
//             ScrollBars. Class Methods Effected actionPerformed(), executeActions(),
//             viewSelectedItem(), and editSelectedItem(). Changed selectedRow Argument
//             in editSelectedItem() to rowToEdit, & Added Argument rowToView to
//             viewSelectedItem().
//         6.6 Moved activateAdvancedSortSearchButton Action Processing From executeActions()
//             Method Back to actionPerformed() Because it Takes No Initial Connection
//             Creation.
//         6.7 Implemented Update Feature. Activation via LOWER_RIGHT_CORNER of the
//             tableScrollPane. Added Class Method createUpdateFrame(). Class Methods
//             Effected Constructor(), actionPerformed(), executeActions(), setTableHeadings().
//             Added sqlTable to TableEntryForm Title.
//         6.8 Implemented Delete All Items Feature. Class Instance deletAllButton Added
//             Only As a Place Holder. Selection via summaryTablePopupMenu. Class Methods
//             Effected Constructor(), createListTablePopupMenu(), actionPerformed(), &
//             executeActions(). Added Class Method deleteAllItems().
//         6.9 Removed the SQL executeUpdate(BEGIN) From Class Method deletAllItems().
//         7.0 Added deleteDataIcon to Dialog in Class Methods deleteSelectedItems() and
//             deleteAllItems().
//         7.1 Implemented a Copy/Paste Popup Menu Addition to the listTable. Implemented
//             the Copy Feature, But Just Provided the Outline for the Paste. Class Methods
//             Effected Constructor(), actionPerformed(), createListTablePopupMenu(), and
//             setTableheadings().
//         7.2 Implemented the Basic Paste Feature for the listTable, in actionPerformed().
//             Added Class Method pasteClipboardContents().
//         7.3 Class Method pasteClipboardContents() Modified tableHeadingsString to be
//             Derived Directly From the listTable, Columns Therefore Moveable.
//         7.4 Added confirmCheckbox and Font Changes to message in Classs Method
//             deleteAllItems().
//         7.5 Class Method pasteClipboardContents() Changed the Method Used to Obtain
//             the Clipboard Contents, Checked for NULL, and Catch for IllegalStateException.
//         7,6 Class Method createUpdateFrame() Arguments columnClass/Type/SizeHashMap.
//         7.7 Class Method createListTablePopupMenu() Paste Excluded if No Keys Found.
//         7.8 Integration of TableTabPanels. Moved All Common Methods to the TableTabPanel.
//         7.9 Class Method loadTable() Changed searchQueryString From String to StringBuffer.
//         8.0 Changed Package to Reflect Dandy Made Productions Code.
//         8.1 Correction in Class Method getColumnNames() To Handle Possible Event of No
//             Fields in the Table. Detected Because 6.2 Exclused Blob and key_tables Has
//             table key_table3 With One Field and It is Blob.
//         8.2 Class Methods viewSelectedItem() and editSelectedItem() Changed Method Instance
//             sqlStatementString to a StringBuffer.
//         8.3 Class Method setTableHeadings() Removed the Reloading of the Class Instance
//             sqlTableFieldsStringLTZ. Should NOT Be Changed. Only Used in this Panel for
//             Views and Edits.
//         8.4 Class Mehod loadTable(), Bug Fix to Correct Inablilty to Search For Date Type
//             Values. Still Broken For TimeStamp and Interval?
//         8.5 Removed the Exclusion of Blob, Raw, & Clob Types in Initial Loading, Class
//             Method getColumnNames().
//         8.6 Implemented Fix For the Performance Issue That was Temporarily Corrected in
//             6.1 & 6.2. Permanently Excluded Any Loading of LOBs in the Summary Table,
//             Unless They Are Identified As Keys. Class Methods Effected getColumnNames()
//             & loadTable(). Added Instance lobLessSQLStatementString to Class Method
//             loadTable().
//         8.7 Class Method getColumnNames() Instance columnType in Some Cases Converted to
//             UpperCase for Comparisons.
//         8.8 Class Method getColumnNames() Changed Instance columnSize From Type Object
//             to Integer. Class Method loadTable() Changed Instance keyLength From String
//             to Integer. Removed Unecessary Casts for the HashMaps. Parameterized Argument
//             newHeadingsFields for Class Method setTableHeadings().
//         8.9 Class Methods getColumnNames() and loadTable() Assigned Integer via
//             valueOf() Instead of new Integer for Instances columnSize &
//             preferredColumnSizeHashMap.
//         9.0 Parameterized headings in Method loadTable(), keyIterator & textFieldIterator
//             in viewSelectedItem() & editSelectedItem(), and textFieldNamesIterator in
//             addItem().
//         9.1 Made the Class and Constructor Public.
//         9.2 Implemented a View Only Table, via Constructor Argument viewOnlyTable.
//         9.3 Assigned searchQueryString to sqlTableSearchString in loadTable().
//         9.4 Undid Last Revision. Short Sighted.
//         9.5 Check for All Fields Possibly LOBs. Class Method loadTable().
//         9.6 Class Method getColumnNames() Moved the Final Check for primaryKeys, Foreign
//             Keys, Back To End of Script Because Depends on columnNamesHashMap.
//         9.7 Class Methods loadTable(), viewSelectedItem(), addItem() & editSelectedItem()
//             Changed Default Entry for Date/DateTime/TimeStamp Type Entry to
//             GeneralProperties.getDateViewFormat(). Class Methods view/editSelectedItem()
//             Change for Date Key Conversion to MyJSQLView_utils.convertViewDateString_To_
//             DBDateString().
//         9.8 Class Method loadTable() Changes to Give the Ability to Properly Search
//             Given Input for Date/DateTime/Timestamp Fields.
//         9.9 Class Method setTableHeadings() Cast Object Returned by MyJSQLView_Access.
//             getConnection() to Connection.
//        10.0 Changes to Class Methods getColumnNames(), loadTable(), viewSelectedItem(),
//             & deleteSelectedItem() to Used Newly Redefined ConnectionManager to Display
//             SQL Errors. Added Method Instance databaseName to getColumnNames().
//        10.1 Corrections in Method loadTable to Properly Build searchTextString For Date,
//             & Timestamp. Class Method viewSelectedItem() & editSelectedItem() to Display
//             Date & Timestamp According to General Preferences Date Setting.
//        10.2 Called saveHistory() in Class Method loadTable().
//        10.3 Changed the Conditional Check for saveAction by Removing the NOT Logic.
//        10.4 Correction in loadTable() for Not Modifiying searchTextString
//             During Composition When No Field Specified.
//        10.5 Class Methods viewSelectedItems(), & addItem() Changed the Processing of Clob
//             Types to be Treated Like Text, Long, Types.
//        10.6 Check For a Single Field Entry in sqlTableFieldsString & lobLessFieldsString
//             in loadTable() for Advanced Sort Search.
//        10.7 Set sqlTableStatement to an Empty String Prior to Each New
//             Assignment in Class Method loadTable().
//        10.8 Set sqlTableStatement to NULL and Then Created a new String() for it of
//             sqlStatementString to Insure it is Flushed in Class Method loadTable().
//        10.9 Set sqlTableStatement to Not be a new String() of sqlStatementString in
//             Method loadTable().
//        11.0 Removed the Casting of (Connection) for the Returned Instance for the
//             ConnectionManager.getConnection() in setTableHeadings().
//        11.1 Removed Method Instance sqlStatementString & Replaced With Parent
//             Class Instance sqlTableStatement.
//        11.2 Methods viewSelectedItem(), editSelectedItem() & getColumnNames() Throws
//             for SQLException Through finally Clause for Closing sqlStatment.
//        11.3 Changes in loadTable to Add Back Instance sqlStatementString and Then
//             Have sqlTableStatement New StringBuffer Designation Loaded From it.
//        11.4 Method loadTable() Conversion of Date From searchString Failed, Due
//             to Possible Generic Search of All Fields for Given Characters. So
//             Just Use Original Characters.
//        11.5 Class Method loadTable() Insured That sqlTableStatement is Given a
//             New Instance Before Reasignment. Replaced delete() Which Was Not
//             Propagating Changes to Object Outside the Method.
//        11.6 Change in viewSelectedItem() to Use Brute Force WHERE Creation for
//             Selected listTable Entry if primaryKeys().isEmpty().
//        11.7 Change in Class Method getColumnNames() of Adding Items to New ArrayList
//             Instances by Way of add() Instead of addElement(). Change in Method
//             setTableHeading() Argument to ArrayList.
//        11.8 Change in getColumnNames() to Always Check for Foreign Keys.
//        11.9 Class Method loadTable() Change in Return Type for advancedSortSearchFrame.
//             getAdvancedSortSearchSQL().
//        12.0 Closure for db_resultSet in editSelectedItem(), viewSelectedItem() &
//             getColumnNames() Moved to finally.
//        12.1 Closure for rs & sqlStatement in loadTable() Moved to finally.
//        12.2 Changed Package Name to com.dandymadeproductions.myjsqlview.gui.panels.
//        12.3 Class Method addItem() & deleteItem() TableEntryForm's disposeButton
//             Collected via getDisposeButton().
//        12.4 Change in loadTable(), viewSelectedItem(), addItem(), &
//             editSelectedItem() to Use DBTablePanel.getGeneralDBProperties().
//
//-----------------------------------------------------------------
//                   danap@dandymadeproductions.com
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
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.Action;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.TransferHandler;
import javax.swing.table.TableColumn;

import com.dandymadeproductions.myjsqlview.MyJSQLView;
import com.dandymadeproductions.myjsqlview.datasource.ConnectionManager;
import com.dandymadeproductions.myjsqlview.datasource.ConnectionProperties;
import com.dandymadeproductions.myjsqlview.gui.forms.TableEntryForm;
import com.dandymadeproductions.myjsqlview.utilities.BlobTextKey;
import com.dandymadeproductions.myjsqlview.utilities.MyJSQLView_TableModel;
import com.dandymadeproductions.myjsqlview.utilities.MyJSQLView_Utils;

/**
 *    The TableTabPanel_Oracle class provides the means to create a default table
 * summary view of data in an Oracle database that in MyJSQLView is listed
 * according to a specified sort and search. Entries from the database table may
 * be viewed, added, edited, or deleted by means of this panel. The panel also
 * provides the mechanism to page through the database table's data.
 * 
 * @author Dana M. Proctor
 * @version 12.4 07/01/2013
 */

public class TableTabPanel_Oracle extends TableTabPanel
{
   // Class Instances Defined in Parent.
   private static final long serialVersionUID = 5747390393178685731L;
   private String sqlTableFieldsStringLTZ;

   //===========================================================
   // TableTabPanel Constructor
   //===========================================================

   public TableTabPanel_Oracle(String table, Connection setup_dbConnection, boolean viewOnlyTable)
   {
      super(table, setup_dbConnection, viewOnlyTable);
   }

   //==============================================================
   // Class method to obtain the column names from the table.
   // The names are modified for display and placed into a map
   // for later use. Additional information about the column,
   // size, type, etc., are also stored away for future use.
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

         // ====================================================
         // Setting Up the Column Names, Form Fields, ComboBox
         // Text, Hashmaps, Special Fields, & Primary Key(s).

         sqlStatementString = "SELECT * FROM " + schemaTableName + " WHERE ROWNUM=1";
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

         // Additional Indexes, Exclude VIEWS.
         
         rs = dbMetaData.getTables(databaseName, schemaName, tableName, null);
         
         if (rs.next() && !rs.getString("TABLE_TYPE").equals("VIEW"))
         {
            // Clueless why needs quotes?
            rs = dbMetaData.getIndexInfo(databaseName,
               (identifierQuoteString + schemaName + identifierQuoteString),
               (identifierQuoteString + tableName + identifierQuoteString), false, false);
            
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
         }
         
         // Column Names, Form Fields, ComboBox Text, Special Fields,
         // and HashMaps.

         sqlTableFieldsString = "";
         lob_sqlTableFieldsString = "";
         sqlTableFieldsStringLTZ = "";

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
            {
               if (columnType.toUpperCase().equals("BINARY_FLOAT"))
               {
                  columnClass = "java.lang.Float";
                  columnType = "FLOAT";
               }
               else if (columnType.toUpperCase().equals("BINARY_DOUBLE"))
               {
                  columnClass = "java.lang.Double";
                  columnType = "DOUBLE";
               }
               else
                  columnClass = columnType;
            }

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
            if (((columnType.toUpperCase().indexOf("BLOB") != -1)
                  || (columnType.toUpperCase().indexOf("RAW") != -1)
                  || (columnType.toUpperCase().indexOf("LONG") != -1)
                  || (columnType.toUpperCase().indexOf("CLOB") != -1))
                 && !primaryKeys.contains(colNameString))
            {
               lobDataTypesHashMap.put(comboBoxNameString, colNameString);
               lob_sqlTableFieldsString += identifierQuoteString + colNameString + identifierQuoteString + " ";
            }

            // Create a second table field string that allows the collection
            // ot Timestamp Fields with Local Time Zone. Oracle JDBC doozie.
            // SESSION TIMEZONE NOT SET. Were not going to do this at the
            // connection or ALTER SESSION.

            if (columnType.toUpperCase().equals("TIMESTAMPLTZ"))
            {
               sqlTableFieldsStringLTZ += "TO_CHAR(" + identifierQuoteString + colNameString
                                          + identifierQuoteString + ", 'YYYY-MM-DD HH24:MM:SS TZR') AS "
                                          + identifierQuoteString + colNameString + identifierQuoteString
                                          + ", ";
            }
            else
               sqlTableFieldsStringLTZ += identifierQuoteString + colNameString + identifierQuoteString
                                          + ", ";

            // Special Column Fields.

            if (columnClass.indexOf("Boolean") != -1 && columnSize.intValue() == 1)
               columnEnumHashMap.put(parseColumnNameField(colNameString), columnType);

            if (primaryKeys.contains(colNameString))
            {
               if (columnSize == null || Integer.parseInt(columnSize.toString()) > 255)
                  columnSize = new Integer("255");
               keyLengthHashMap.put(colNameString, columnSize);
            }
         }
         // Clean up the SQL field string for later use.
         if (sqlTableFieldsString.length() > 2)
        	 sqlTableFieldsString = sqlTableFieldsString.substring(0, sqlTableFieldsString.length() - 2);
         if (sqlTableFieldsStringLTZ.length() > 2)
            sqlTableFieldsStringLTZ = sqlTableFieldsStringLTZ.substring(0, sqlTableFieldsStringLTZ.length() - 2);

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

         // Finally aaagh!! get any, sequence, autoIncrement Fields.
         
         for (int i = 1; i < tableMetaData.getColumnCount() + 1; i++)
         {
            colNameString = tableMetaData.getColumnName(i);
            comboBoxNameString = parseColumnNameField(colNameString);

            sqlStatementString = "SELECT USER_IND_COLUMNS.INDEX_NAME FROM USER_IND_COLUMNS, "
                                 + "ALL_SEQUENCES WHERE USER_IND_COLUMNS.INDEX_NAME="
                                 + "ALL_SEQUENCES.SEQUENCE_NAME AND USER_IND_COLUMNS.TABLE_NAME='"
                                 + tableName + "' AND USER_IND_COLUMNS.COLUMN_NAME='" + colNameString + "'";
            // System.out.println(sqlStatementString);

            rs = sqlStatement.executeQuery(sqlStatementString);

            if (rs.next())
            {
               // System.out.println(rs.getString("INDEX_NAME"));
               autoIncrementHashMap.put(comboBoxNameString, rs.getString("INDEX_NAME"));
            }
         }

         return true;
      }
      catch (SQLException e)
      {
         ConnectionManager.displaySQLErrors(e, "TableTabPanel_Oracle getColumnNames()");
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
            ConnectionManager.displaySQLErrors(sqle, "TableTabPanel_Oracle getColumnNames()");
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
               ConnectionManager.displaySQLErrors(sqle, "TableTabPanel_Oracle getColumnNames()");
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
      Statement sqlStatement;
      ResultSet rs;

      StringBuffer searchQueryString;
      String columnSearchString, searchTextString;
      String lobLessFieldsString;
      String columnName, columnClass, columnType;
      Integer keyLength;
      int columnSize, preferredColumnSize;
      Object currentContentData;

      // Obtain search parameters, column names as needed and
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
         // No column specified so create WHERE for all except
         // BFILE, LONG, and BLOB. Special case with Dates.
         
         if (columnSearchString == null)
         {
            String[] tableColumns;
            tableColumns = sqlTableFieldsString.split(",");
            
            for (int i = 0; i < tableColumns.length; i++)
            {
               columnName = tableColumns[i].replaceAll(identifierQuoteString, "");
               columnType = columnTypeHashMap.get(parseColumnNameField(columnName.trim()));
               
               String searchString = searchTextString;
               
               if (columnType.equals("BFILE") || columnType.equals("LONG")
                   || columnType.equals("BLOB"))
                  continue;
               
               if (columnType.equals("DATE"))
               {
                  searchString = MyJSQLView_Utils.processDateFormatSearch(searchString);
                  
                  // Something not right in conversion.
                  if (searchString.equals("0") || searchString.equals(searchTextString))
                     searchQueryString.append(tableColumns[i] + " LIKE '%" + searchTextString + "%'");
                  else
                     searchQueryString.append(tableColumns[i] + " LIKE TO_DATE('" + searchString
                                              + "', 'YYYY-MM-dd')");
                  
                  if (i < tableColumns.length - 1)
                     searchQueryString.append(" OR");
               }
               else
               {
                  if (i < tableColumns.length - 1)
                     searchQueryString.append(tableColumns[i] + " LIKE '%" + searchString + "%' OR");
                  else
                     searchQueryString.append(tableColumns[i] + " LIKE '%" + searchString + "%'");  
               }
            }
         }
         // Field specified.
         else
         {
            columnType = columnTypeHashMap.get(searchComboBox.getSelectedItem());
            
            if (columnType.equals("DATE"))
            {
               searchTextString = MyJSQLView_Utils.processDateFormatSearch(searchTextString);
               searchQueryString.append(identifierQuoteString + columnSearchString + identifierQuoteString
                                        + " LIKE TO_DATE('" + searchTextString + "', 'YYYY-MM-dd')");  
            }
            else if (columnType.equals("TIMESTAMP"))
            {
               if (searchTextString.indexOf(" ") != -1)
                  searchTextString = MyJSQLView_Utils.processDateFormatSearch(
                     searchTextString.substring(0, searchTextString.indexOf(" ")))
                     + searchTextString.substring(searchTextString.indexOf(" "));
               else if (searchTextString.indexOf("-") != -1 || searchTextString.indexOf("/") != -1)
                  searchTextString = MyJSQLView_Utils.processDateFormatSearch(searchTextString);
               
               searchQueryString.append(identifierQuoteString + columnSearchString + identifierQuoteString
                                        + " LIKE TO_TIMESTAMP('" + searchTextString
                                        + "', 'YYYY-MM-dd HH24:MI:SS') ");     
            }
            else
               searchQueryString.append(identifierQuoteString + columnSearchString + identifierQuoteString
                                        + " LIKE '%" + searchTextString + "%'");
         }
         // System.out.println(searchQueryString);
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
            sqlStatementString = sqlStatementString.substring(0, sqlStatementString.indexOf("FROM") + 5);
            lobLessSQLStatementString = lobLessSQLStatementString.substring(0,
                                                          lobLessSQLStatementString.indexOf("FROM") + 5);

            sqlStatementString += "(SELECT ROW_NUMBER() "
                                  + ((sqlOrderString.equals("")) ? ("OVER (ORDER BY "
                                  + (sqlTableFieldsString.indexOf(",") != -1 ?
                                                        sqlTableFieldsString.substring(0, sqlTableFieldsString.indexOf(','))
                                                                             :
                                                        sqlTableFieldsString)
                                  + ") ")
                                                                 : ("OVER (" + sqlOrderString + ") "))
                                  + "AS dmprownumber, " + sqlTableFieldsStringLTZ + " "
                                  + "FROM " + schemaTableName + " " + sqlWhereString + ") "
                                  + "WHERE dmprownumber BETWEEN " + (tableRowStart + 1) + " AND "
                                  + (tableRowStart + tableRowLimit);
            
            lobLessSQLStatementString += "(SELECT ROW_NUMBER() "
                                         + ((lobLess_sqlOrderString.equals("")) ? ("OVER (ORDER BY "
                                         + (lobLessFieldsString.indexOf(",") != -1 ?
                                                        lobLessFieldsString.substring(0, lobLessFieldsString.indexOf(','))
                                                                                  :
                                                        lobLessFieldsString)
                                         + ") ")
                                              : ("OVER (" + lobLess_sqlOrderString + ") "))
                                         + "AS dmprownumber, " + sqlTableFieldsStringLTZ + " "
                                         + "FROM " + schemaTableName + " " + lobLess_sqlWhereString + ") "
                                         + "WHERE dmprownumber BETWEEN " + (tableRowStart + 1) + " AND "
                                         + (tableRowStart + tableRowLimit);
         }
         else
         {
            sqlStatementString = "SELECT " + sqlTableFieldsString + " FROM " + "(SELECT ROW_NUMBER() OVER "
                                 + "(ORDER BY " + identifierQuoteString
                                 + columnNamesHashMap.get(sortComboBox.getSelectedItem())
                                 + identifierQuoteString + " " + ascDescString + ") " + "AS dmprownumber, "
                                 + sqlTableFieldsStringLTZ + " " + "FROM " + schemaTableName + " " + "WHERE "
                                 + searchQueryString.toString() + ") " + "WHERE dmprownumber BETWEEN "
                                 + (tableRowStart + 1) + " AND " + (tableRowStart + tableRowLimit);
            
            lobLessSQLStatementString = "SELECT " + lobLessFieldsString + " FROM "
                                        + "(SELECT ROW_NUMBER() OVER "
                                        + "(ORDER BY " + identifierQuoteString
                                        + columnNamesHashMap.get(sortComboBox.getSelectedItem())
                                        + identifierQuoteString + " " + ascDescString + ") "
                                        + "AS dmprownumber, " + sqlTableFieldsStringLTZ + " "
                                        + "FROM " + schemaTableName + " " + "WHERE "
                                        + searchQueryString.toString() + ") " + "WHERE dmprownumber BETWEEN "
                                        + (tableRowStart + 1) + " AND " + (tableRowStart + tableRowLimit);
         }
         sqlTableStatement.append(sqlStatementString);
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
               preferredColumnSize = ((Integer) preferredColumnSizeHashMap.get(currentHeading)).intValue();

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
                  // Timestamps
                  else if (columnType.equals("TIMESTAMP"))
                  {
                     currentContentData = rs.getTimestamp(columnName);
                     tableData[i][j++] = (new SimpleDateFormat(
                        DBTablesPanel.getGeneralDBProperties().getViewDateFormat()
                        + " HH:mm:ss").format(currentContentData));
                  }

                  // =============================================
                  // Timestamps With Time Zone
                  
                  else if (columnType.equals("TIMESTAMPTZ"))
                  {
                     currentContentData = rs.getTimestamp(columnName);
                     
                     tableData[i][j++] = (new SimpleDateFormat(
                        DBTablesPanel.getGeneralDBProperties().getViewDateFormat()
                        + " HH:mm:ss Z").format(currentContentData));
                  }
                  
                  else if (columnType.equals("TIMESTAMPLTZ"))
                  {
                     currentContentData = rs.getString(columnName);
                     String timestampString = (String) currentContentData;
                     
                     if (timestampString.indexOf(" ") != -1)
                        tableData[i][j++] = displayMyDateString(
                                            timestampString.substring(0, timestampString.indexOf(" ")))
                                            + timestampString.substring(timestampString.indexOf(" "));
                     else
                        tableData[i][j++] = timestampString;
                  }

                  // =============================================
                  // BLOB, RAW, LONG, & CLOB
                  else if (columnType.equals("BLOB") || columnType.indexOf("RAW") != -1
                           || columnType.indexOf("CLOB") != -1
                           || (columnClass.indexOf("String") != -1 && columnType.equals("LONG")))
                  {
                     String blobName;
                     
                     if (columnType.equals("BLOB"))
                        blobName = "Blob";
                     else if (columnType.indexOf("RAW") != -1)
                        blobName = "Raw";
                     else if (columnType.equals("LONG"))
                        blobName = "Long";
                     else
                        blobName = "Clob";
                     
                     // Handles a key BLOB ?
                     if (keyLength != null)
                     { 
                        BlobTextKey currentBlobElement = new BlobTextKey();
                        currentBlobElement.setName(blobName);
                        
                        String content = rs.getString(columnName);
                        
                        if (content.length() > keyLength.intValue())
                           content = content.substring(0, keyLength.intValue());
                        
                        currentBlobElement.setContent(content);
                        tableData[i][j++] = currentBlobElement;
                     }
                     else
                     {
                        tableData[i][j++] = blobName;
                     }  
                  }

                  // =============================================
                  // BFILE
                  else if (columnType.equals("BFILE"))
                  {
                     tableData[i][j++] = "BFILE";
                  }

                  // =============================================
                  // VARCHAR2
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
               preferredColumnSizeHashMap.put(currentHeading,
                                              Integer.valueOf(preferredColumnSize));
            }
            j = 0;
            i++;
         }
         return true;
      }
      catch (SQLException e)
      {
         ConnectionManager.displaySQLErrors(e, "TableTabPanel_Oracle loadTable()");
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
            ConnectionManager.displaySQLErrors(sqle, "TableTabPanel_Oracle loadTable()");
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
               ConnectionManager.displaySQLErrors(sqle, "TableTabPanel_Oracle loadTable()");
            }
         }
      }
   }

   //=============================================================
   // Class method to view the current selected item in the table.
   //=============================================================

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
      db_resultSet = null;
      
      try
      {
         // Begin the SQL statement creation.
         sqlStatement = dbConnection.createStatement();
         sqlStatementString = new StringBuffer();
         sqlStatementString.append("SELECT " + sqlTableFieldsStringLTZ + " FROM "
                                   + schemaTableName + " WHERE ");
         
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
                     currentColumnType = columnTypeHashMap.get(parseColumnNameField(currentDB_ColumnName));
                     if (currentColumnType.equals("DATE"))
                     {
                        sqlStatementString.append(identifierQuoteString + currentDB_ColumnName
                                                  + identifierQuoteString + "=TO_DATE('"
                                                  + MyJSQLView_Utils.convertViewDateString_To_DBDateString(
                                                     currentContentData + "", DBTablesPanel.getGeneralDBProperties().getViewDateFormat())
                                                  + "', 'YYYY-MM-dd') AND ");
                     }
                     else
                        sqlStatementString.append(identifierQuoteString + currentDB_ColumnName
                                                  + identifierQuoteString + "='" + currentContentData
                                                  + "' AND ");
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
               //                     + " type:" + currentColumnType + " value:" + currentContentData);
               
               // Skip Blob, Text, Clob, Float, BFile & Timestamps Unless NULL.
               if ((currentColumnType.equals("BLOB") || currentColumnType.indexOf("RAW") != -1)
                     || ((currentColumnClass.indexOf("String") != -1 && !currentColumnType.equals("CHAR")
                          && columnSize > 255)
                         || (currentColumnClass.indexOf("String") != -1 && currentColumnType.equals("LONG")))
                     || (currentColumnType.indexOf("CLOB") != -1)
                     || (currentColumnType.indexOf("FLOAT") != -1)
                     || (currentColumnType.equals("BFILE"))
                     || (currentColumnType.indexOf("TIMESTAMP") != -1))
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
                     
                     sqlStatementString.append(" LIKE TO_DATE('" + dateString + "', 'YYYY-MM-dd') ");
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

            // Oracle only provides a one time chance to obtain the result
            // set for LONG RAW fields so just collect BLOB & RAW content once.

            if (currentColumnType.equals("BLOB") || currentColumnType.indexOf("RAW") != -1)
               currentContentData = db_resultSet.getBytes(currentDB_ColumnName);
            else
               currentContentData = db_resultSet.getObject(currentDB_ColumnName);

            // System.out.println(i + " " + currentColumnName + " " +
            // currentDB_ColumnName + " " +
            // currentColumnType + " " +
            // columnSizeHashMap.get(currentColumnName) +
            // " " + currentContentData);

            if (currentContentData != null)
            {
               // DATE Type Field
               if (currentColumnType.equals("DATE"))
               {
                  currentContentData = db_resultSet.getDate(currentDB_ColumnName);
                  tableViewForm.setFormField(currentColumnName, displayMyDateString(currentContentData + ""));
               }

               // Timestamps Type Field
               else if (currentColumnType.equals("TIMESTAMP"))
               {
                  currentContentData = db_resultSet.getTimestamp(currentDB_ColumnName);
                  tableViewForm.setFormField(currentColumnName,
                                             (new SimpleDateFormat(
                                                DBTablesPanel.getGeneralDBProperties().getViewDateFormat()
                                                + " HH:mm:ss").format(currentContentData)));
               }

               // Timestamps With Time Zone Type Field
               else if (currentColumnType.equals("TIMESTAMPTZ"))
               {
                  currentContentData = db_resultSet.getTimestamp(currentDB_ColumnName);
                  tableViewForm.setFormField(currentColumnName,
                     (new SimpleDateFormat(DBTablesPanel.getGeneralDBProperties().getViewDateFormat()
                        + " HH:mm:ss Z").format(currentContentData)));
               }

               // Timestamps With Local Time Zone Type Field
               else if (currentColumnType.equals("TIMESTAMPLTZ"))
               {
                  currentContentData = db_resultSet.getString(currentDB_ColumnName);
                  String timestampString = (String) currentContentData;
                  
                  if (timestampString.indexOf(" ") != -1)
                     timestampString = displayMyDateString(
                                       timestampString.substring(0, timestampString.indexOf(" ")))
                                       + timestampString.substring(timestampString.indexOf(" "));
                  
                  tableViewForm.setFormField(currentColumnName, timestampString);
               }

               // Blob/Raw Type Field
               else if (currentColumnType.equals("BLOB") || currentColumnType.indexOf("RAW") != -1)
               {
                  String binaryType;
                  if (currentColumnType.equals("BLOB"))
                     binaryType = "BLOB";
                  else
                     binaryType = "RAW";

                  if (((byte[]) currentContentData).length != 0)
                  {
                     int size = ((byte[]) currentContentData).length;

                     tableViewForm.setFormField(currentColumnName,
                        (Object) (binaryType + " " + size + " Bytes"));
                     tableViewForm.setFormFieldBlob(currentColumnName, (byte[]) currentContentData);
                  }
                  else
                     tableViewForm.setFormField(currentColumnName, (Object) (binaryType + " 0 Bytes"));
               }

               // BFILE Type Field
               else if (currentColumnType.equals("BFILE"))
               {
                  tableViewForm.setFormField(currentColumnName, (Object) "BFILE Views Not Supported.");
               }

               // CLOB Type Field
               else if (currentColumnType.indexOf("CLOB") != -1)
               {
                  currentContentData = db_resultSet.getString(currentDB_ColumnName);

                  if (((String) currentContentData).getBytes().length != 0)
                  {
                     int size = ((String) currentContentData).getBytes().length;

                     tableViewForm.setFormField(currentColumnName, (Object) ("CLOB " + size + " Bytes"));
                     tableViewForm.setFormFieldText(currentColumnName,
                                                    (String) currentContentData);
                  }
                  else
                     tableViewForm.setFormField(currentColumnName, (Object) "CLOB 0 Bytes");
               }

               // VARCHAR2 & LONG
               else if ((currentColumnClass.indexOf("String") != -1 &&
                         !currentColumnType.equals("CHAR") &&
                        (columnSizeHashMap.get(currentColumnName)).intValue() > 255) ||
                        (currentColumnClass.indexOf("String") != -1 && currentColumnType.equals("LONG")))
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
               {
                  currentContentData = db_resultSet.getString(currentDB_ColumnName);
                  tableViewForm.setFormField(currentColumnName, currentContentData);
               }
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
         ConnectionManager.displaySQLErrors(e, "TableTabPanel_Oracle viewSelectedItem()");
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
            ConnectionManager.displaySQLErrors(sqle, "TableTabPanel_Oracle viewSelectedItem()");
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
         currentColumnClass = columnClassHashMap.get(currentColumnName);
         currentColumnType = columnTypeHashMap.get(currentColumnName);

         // Auto-Increment Type Field
         if (autoIncrementHashMap.containsKey(currentColumnName))
         {
            currentContentData = "AUTO";
            addForm.setFormField(currentColumnName, currentContentData);
         }

         // DATE Type Field
         if (currentColumnType.equals("DATE"))
         {
            currentContentData = DBTablesPanel.getGeneralDBProperties().getViewDateFormat();
            addForm.setFormField(currentColumnName, currentContentData);
         }

         // TIMESTAMP Type Field
         if (currentColumnType.equals("TIMESTAMP") || currentColumnType.equals("TIMESTAMPTZ")
             || currentColumnType.equals("TIMESTAMPLTZ"))
         {
            currentContentData = "NOW()";
            addForm.setFormField(currentColumnName, currentContentData);
         }

         // BLOB, & RAW Type Field
         if (currentColumnType.equals("BLOB") || currentColumnType.indexOf("RAW") != -1)
         {
            if (currentColumnType.equals("BLOB"))
               addForm.setFormField(currentColumnName, (Object) ("BLOB Browse"));
            else
               addForm.setFormField(currentColumnName, (Object) ("RAW Browse"));
         }

         // BFILE Field
         if (currentColumnType.equals("BFILE"))
         {
            addForm.setFormField(currentColumnName, (Object) ("DIRECTORY OBJECT, FILENAME"));
         }

         // VARCHAR, LONG, & CLOB Type Field
         if ((currentColumnClass.indexOf("String") != -1 && !currentColumnType.equals("CHAR")
              && (columnSizeHashMap.get(currentColumnName)).intValue() > 255)
             || (currentColumnClass.indexOf("String") != -1 && currentColumnType.equals("LONG"))
             || currentColumnType.indexOf("CLOB") != -1)
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
                                                   formFields, tableViewForm,
                                                   columnNamesHashMap, columnClassHashMap, columnTypeHashMap,
                                                   columnSizeHashMap, columnEnumHashMap, columnSetHashMap);

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
         sqlStatementString.append("SELECT " + sqlTableFieldsStringLTZ + " FROM "
                                   + schemaTableName + " WHERE ");

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
                                               + identifierQuoteString + "=TO_DATE('"
                                               + MyJSQLView_Utils.convertViewDateString_To_DBDateString(
                                                  currentContentData + "", DBTablesPanel.getGeneralDBProperties().getViewDateFormat())
                                               + "', 'YYYY-MM-dd') AND ");
                  }
                  else
                     sqlStatementString.append(identifierQuoteString + currentDB_ColumnName
                                               + identifierQuoteString + "='"
                                               + currentContentData + "' AND ");
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

            // Oracle only provides a one time chance to obtain the result
            // set for LONG RAW fields so just collect all BLOB and RAW content.

            if (currentColumnType.equals("BLOB") || currentColumnType.indexOf("RAW") != -1)
               currentContentData = db_resultSet.getBytes(currentDB_ColumnName);
            else if (currentColumnType.equals("BFILE"))
            {
               // BFILE edits not supported.
               continue;
            }
            else
               currentContentData = db_resultSet.getString(currentDB_ColumnName);
            // System.out.println(currentColumnName + " " + currentContentData);

            // Special content from other tables, ComboBoxes, maybe.
            // Also time, date or your special field formatting.

            if (((String) currentColumnName).equals("Your Special Field Name"))
               setSpecialFieldData(editForm, dbConnection, currentColumnName, currentContentData);

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
                     (Object) (DBTablesPanel.getGeneralDBProperties().getViewDateFormat() + " HH:MM:SS"));
            }

            // Timestamps With Time Zone Type Fields
            else if (currentColumnType.equals("TIMESTAMPTZ") ||
                     currentColumnType.equals("TIMESTAMPLTZ"))
            {
               if (currentContentData != null)
               {
                  if (currentColumnType.equals("TIMESTAMPTZ"))
                  {
                     currentContentData = db_resultSet.getTimestamp(currentDB_ColumnName);
                     // System.out.println(currentContentData);
                     editForm.setFormField(currentColumnName,
                        (Object) (new SimpleDateFormat(DBTablesPanel.getGeneralDBProperties().getViewDateFormat()
                           + " HH:mm:ss Z").format(currentContentData)));
                  }
                  else
                  {
                     String timestampString = (String) currentContentData;
                     
                     if (timestampString.indexOf(" ") != -1)
                        timestampString = displayMyDateString(
                                          timestampString.substring(0, timestampString.indexOf(" ")))
                                          + timestampString.substring(timestampString.indexOf(" "));
                     
                     editForm.setFormField(currentColumnName, (Object) timestampString);
                  }
               }
               else
                  editForm.setFormField(currentColumnName,
                     (Object) (DBTablesPanel.getGeneralDBProperties().getViewDateFormat() + " HH:MM:SS"));
            }

            // Blob & Raw Type Field
            else if (currentColumnType.equals("BLOB") || currentColumnType.indexOf("RAW") != -1)
            {
               String binaryType;
               if (currentColumnType.indexOf("BLOB") != -1)
                  binaryType = "BLOB";
               else
                  binaryType = "RAW";

               if (currentContentData != null)
               {
                  int size = ((byte[]) currentContentData).length;

                  if (size != 0)
                     editForm.setFormField(currentColumnName, (Object) (binaryType
                                                               + " " + size + " Bytes"));
                  else
                     editForm.setFormField(currentColumnName, (Object) (binaryType
                                                               + " 0 Bytes"));

                  editForm.setFormFieldBlob(currentColumnName, (byte[]) currentContentData);
               }
               else
                  editForm.setFormField(currentColumnName, (Object) (binaryType + " NULL"));
            }

            // CLOB
            else if (currentColumnType.indexOf("CLOB") != -1)
            {
               if (currentContentData != null)
               {
                  currentContentData = db_resultSet.getString(currentDB_ColumnName);
                  if (((String) currentContentData).getBytes().length != 0)
                  {
                     int size = ((String) currentContentData).getBytes().length;
                     editForm.setFormField(currentColumnName, (Object) ("CLOB " + size + " Bytes"));
                  }
                  else
                  {
                     editForm.setFormField(currentColumnName, (Object) "CLOB 0 Bytes");
                  }
                  editForm.setFormFieldText(currentColumnName, ((String) currentContentData));
               }
               else
                  editForm.setFormField(currentColumnName, (Object) "CLOB NULL");
            }

            // VARCHAR & LONG
            else if ((currentColumnClass.indexOf("String") != -1 &&
                      !currentColumnType.equals("CHAR") && currentColumnSize > 255) ||
                     (currentColumnClass.indexOf("String") != -1 && currentColumnType.equals("LONG")))
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
         ConnectionManager.displaySQLErrors(e, "TableTabPanel_Oracle editSelectedItem()");
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
            ConnectionManager.displaySQLErrors(sqle, "TableTabPanel_Oracle editSelectedItem()");
         }
         finally
         {
            if (sqlStatement != null)
               sqlStatement.close();
         }
      }
   }
   
   //===============================================================
   // Class method to allow classes to set the table heading fields.
   //===============================================================

   public void setTableHeadings(ArrayList<String> newHeadingFields)
   {
      // Create connection, remove old summary table and
      // reload the center panel.

      Connection work_dbConnection = ConnectionManager.getConnection(
         "TableTabPanel_Oracle setTableHeadings()");
      
      if (work_dbConnection == null)
         return;

      centerPanel.remove(tableScrollPane);
      currentTableHeadings = newHeadingFields;
      sqlTableFieldsString = "";

      // Reconstitute the table field names.

      Iterator<String> headings = currentTableHeadings.iterator();

      while (headings.hasNext())
      {
         sqlTableFieldsString += identifierQuoteString + columnNamesHashMap.get(headings.next())
                                 + identifierQuoteString + ", ";
      }
      // No fields, just load empty table else
      // clean up and load.
      if (sqlTableFieldsString.equals(""))
         tableData = new Object[0][0];
      else
      {
         sqlTableFieldsString = sqlTableFieldsString.substring(0, sqlTableFieldsString.length() - 2);
         //sqlTableFieldsStringLTZ = sqlTableFieldsStringLTZ.substring(0, sqlTableFieldsStringLTZ.length() - 2);
         loadTable(work_dbConnection);
      }
      
      tableModel = new MyJSQLView_TableModel(currentTableHeadings, tableData);
      tableModel.setValues(tableData);

      listTable = new JTable(tableModel);
      listTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
      listTable.getActionMap().put(TransferHandler.getCopyAction().getValue(Action.NAME),
                                   TransferHandler.getCopyAction());
      listTable.getActionMap().put(TransferHandler.getPasteAction().getValue(Action.NAME),
                                   TransferHandler.getPasteAction());
      listTable.addMouseListener(summaryTablePopupListener);

      // Sizing columns
      headings = currentTableHeadings.iterator();
      TableColumn column = null;

      int i = 0;
      while (headings.hasNext())
      {
         Object currentHeading = headings.next();
         column = listTable.getColumnModel().getColumn(i++);
         column.setPreferredWidth((preferredColumnSizeHashMap.get(currentHeading)).intValue());
      }

      tableScrollPane = new JScrollPane(listTable);
      
      tableScrollPane.setCorner(ScrollPaneConstants.UPPER_RIGHT_CORNER, activateAdvancedSortSearchButton);
      tableScrollPane.setCorner(ScrollPaneConstants.LOWER_RIGHT_CORNER, activateUpdateButton);
      
      tableScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
      tableScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
      
      centerPanel.add(sqlTable, tableScrollPane);
      centerCardLayout.show(centerPanel, sqlTable);

      ConnectionManager.closeConnection(work_dbConnection, "TableTabPanel_Oracle setTableHeadings()");
   }
}
