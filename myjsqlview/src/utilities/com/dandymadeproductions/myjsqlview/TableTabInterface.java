//=================================================================
//                 MyJSQLView TableTabInterface
//=================================================================
//    This class defines the methods that are required by all
// TableTabPanels in order to properly function within the 
// MyJSQLView application with other classes.
//
//             << TableTabInterface.java >>
//
//=================================================================
// Copyright (C) 2007-2010 Dana M. Proctor
// Version 3.1 06/24/2010
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
// Version 1.0 Original MyJSQLView TableTabInterface Class.
//         1.1 Class Method getAutoIncrement() Changed to HashMap Return Type
//             and Name to getAutoIncrementHashMap().
//         1.2 Removed Some of the Commented Out Imports. Minor Comments Changes.
//         1.3 Header Update.
//         1.4 setTableFields() Changed to setTableHeadings().
//         1.5 Changed Abstract Method getTableHeadings() to
//             getCurrentTableHeadings(). Added Abstract Method
//             getAllTableHeadings().
//         1.6 MyJSQLView Project Common Source Code Formatting.
//         1.7 Added Abstract Methods getValidDataRows(), setSearchTextField(),
//             & getTableSQLStatement().
//         1.8 Header Format Changes/Update.
//         1.9 Added Interface Methods get/setSaveFileName().
//         2.0 Added Interface Methods get/setState().
//         2.1 Added Interface Method createAdvancedSortSearchFrame().
//         2.2 Added Interface Method createUpdateFrame().
//         2.3 Added The Commented Out Interface Method pasteClipboard() Contents.
//         2.4 Integration of TableTabPanels. Updated Interfaces to Include
//             getColumnNames(), loadTable(), viewSelectedItem(), editSelectedItem(),
//             deleteSelectedItem(), deleteAllItems(), pastClipboardContents().
//         2.5 Changed Package to Reflect Dandy Made Productions Code.
//         2.6 Minor Comment Changes.
//         2.7 Configured Interface Methods With Vector and HashMap Types to be
//             Parameterized So That the Code Will be Compliant to Java 5.0 API.
//         2.8 Aded Interface Method setActionButtonVisible().
//         2.9 Removed Interface Method setActionButtonVisible().
//         3.0 Added Interface Method getTableSQLSearchString().
//         3.1 Undid 3.0 Revision, Short Sighted.
//
//-----------------------------------------------------------------
//                danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview;

import java.awt.Graphics;
import java.awt.print.PageFormat;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.JTable;

/**
 *    The TableTabInterface class defines the methods that are required by
 * all TableTabPanels in order to properly function within the MyJSQLView
 * application with other classes.
 * 
 * @author Dana M. Proctor
 * @version 3.1 06/24/2010
 */

interface TableTabInterface
{
   //==============================================================
   // Class method to collect the column names from the table.
   //==============================================================

   boolean getColumnNames(Connection dbConnection);

   //==============================================================
   // Class method to load the table data into the summary view.
   //==============================================================

   boolean loadTable(Connection dbConnection);

   //==============================================================
   // Class method to view the current selected item in the table.
   //==============================================================

   void viewSelectedItem(Connection dbConnection, int rowToView);

   //==============================================================
   // Class method to add a table entry.
   //==============================================================

   void addItem(Connection dbConnection);

   //==============================================================
   // Class method to edit the current selected item.
   //==============================================================

   void editSelectedItem(Connection dbConnection, int rowToEdit, Object columnName, Object id);

   //==============================================================
   // Class method to delete table entry item(s) from the database.
   // Either single or multiple entries can be removed.
   //==============================================================

   void deleteSelectedItems(Connection dbConnection);

   //==============================================================
   // Class method to delete all table entries from the database.
   //==============================================================

   void deleteAllItems(Connection dbConnection);

   //==============================================================
   // Class method to paste the clipboard contents into the selected
   // table.
   //==============================================================

   void pasteClipboardContents();

   //==============================================================
   // Class method to create the advanced sort/search frame.
   //==============================================================

   void createAdvancedSortSearchFrame();

   //==============================================================
   // Class method to create the update frame.
   //==============================================================

   void createUpdateFrame();

   //==============================================================
   // Class Method to print the panel's current information.
   //==============================================================

   int print(Graphics g, PageFormat pageFormat, int pageIndex);

   //==============================================================
   // Class method to allow classes to obtain the list of allowed
   // column names that can be viewed in the panel.
   //==============================================================

   Vector<String> getTableFields();

   //==============================================================
   // Class method to allow classes to obtain the list of allowed
   // column names that are presently in the summary table.
   //==============================================================

   Vector<String> getCurrentTableHeadings();

   //==============================================================
   // Class method to allow classes to obtain the list of all
   // column names that are possible in the summary table.
   //==============================================================

   Vector<String> getAllTableHeadings();

   //==============================================================
   // Class method to allow classes to obtain name of the table.
   //==============================================================

   String getTableName();

   //==============================================================
   // Class method to allow classes to obtain the name of the last
   // saved file name for the table.
   //==============================================================

   String getSaveFileName();

   //==============================================================
   // Class method to allow classes to obtain current summary
   // table SQL statement.
   //==============================================================

   public String getTableSQLStatement();

   //==============================================================
   // Class method to allow classes to obtain the table row start.
   //==============================================================

   int getTableRowStart();

   //==============================================================
   // Class method to allow classes to obtain the table row limit.
   //==============================================================

   int getTableRowLimit();

   //==============================================================
   // Class method to allow classes to get the summary table row
   // size.
   //==============================================================

   int getTableRowSize();

   //==============================================================
   // Class method to allow classes to obtain the summary list
   // table presently displayed in the tab.
   //==============================================================

   JTable getListTable();

   //==============================================================
   // Class method to allow classes to obtain the number of valid
   // rows of table data.
   //==============================================================

   int getValidDataRowCount();

   //==============================================================
   // Class method to allow classes to obtain the primary key(s)/
   // index(s) used by this list table.
   //==============================================================

   Vector<String> getPrimaryKeys();

   //==============================================================
   // Class method to allow classes to obtain the auto-increment
   // field name.
   //==============================================================

   HashMap<String, String> getAutoIncrementHashMap();

   //==============================================================
   // Class method to allow classes to obtain the columnNamesHashMap.
   //==============================================================

   HashMap<String, String> getColumnNamesHashMap();

   //==============================================================
   // Class method to allow classes to obtain the columnClassHashMap.
   //==============================================================

   HashMap<String, String> getColumnClassHashMap();

   //==============================================================
   // Class method to allow classes to obtain the columnTypeHashMap.
   //==============================================================

   HashMap<String, String> getColumnTypeHashMap();

   //==============================================================
   // Class method to allow classes to obtain the columnSizeHashMap.
   //==============================================================

   HashMap<String, Integer> getColumnSizeHashMap();

   //==============================================================
   // Class method to allow classes to obtain the table's state.
   //==============================================================

   String getState();
   
   //==============================================================
   // Class method to allow classes to set the table heading fields.
   //==============================================================

   void setTableHeadings(Vector<String> newHeadingFields);

   //==============================================================
   // Class method to allow classes to set the summary table row
   // size.
   //==============================================================

   void setTableRowSize(int numberOfRows);

   //==============================================================
   // Class method to allow classes to set the name of the last
   // saved file name for the table.
   //==============================================================

   void setSaveFileName(String fileName);

   //==============================================================
   // Class method to allow classes to set the summary table
   // search string and reload the table.
   //==============================================================

   void setSearchTextField(String searchString);

   //==============================================================
   // Class method to allow classes to set the table's state.
   //==============================================================

   void setState(String stateString);
}