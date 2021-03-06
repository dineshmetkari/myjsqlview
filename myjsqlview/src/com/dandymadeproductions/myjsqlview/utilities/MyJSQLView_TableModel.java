//=================================================================
//             MyJSQLView General Table Model
//=================================================================
//
//    This class provides the model that is used with the
// TableTabPanels to display a summary table of database data.
// The class provides the basic defaults and a way to update 
// the table data when sort, search, or the next/previous
// actions are generated.
//
//              << MyJSQLView_TableModel.java >>
//
//=================================================================
// Copyright (C) 2005-2015 Dana M. Proctor
// Version 2.6 09/20/2012
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
// Version 1.0 12/27/2005 Original MyJSQLView StockItemsTableModel
//                        Class.
//         1.1 04/20/2007 Added Class Methods getSelectedRow() &
//                        getSelectedColumn().
//         1.2 09/08/2007 Removed Class Methods getSelectedRow() &
//                        getSelectedColumn().
//         1.3 09/12/2007 Class Method getColumnClass() Performed
//						  Conditional getValueAt(0,col) != null
//                        & Return String Class if Is.
//         1.4 Cleaned Up Javadoc Comments.
//         1.5 12/12/2007 Header Update.
//         1.6 05/14/2008 Added Class Instance serialVersionUID.
//         1.7 10/23/2008 MyJSQLView Project Common Source Code Formatting.
//         1.8 11/08/2008 Removed Comments From Methods getRowCount() &
//                        getColumnCount() For Returning Zero When data null.
//         1.9 05/27/2009 Header Format Changes/Update.
//         2.0 02/18/2010 Changed Package to Reflect Dandy Made Productions Code.
//         2.1 05/20/2010 Parmeterized Class Instance headings and Same in 
//                        Constructor Argument.
//         2.2 01/27/2011 Copyright Update.
//         2.3 01/01/2012 Copyright Update.
//         2.4 05/07/2012 Changed Class Instance and All Associated References
//                        from Vector Data Type to ArrayList.
//         2.5 09/11/2012 Changed Package Name to com.dandymadeproductions.myjsqlview.utilities.
//                        Made Class & Constructor Public.
//         2.6 09/20/2012 Constructor & Method setValues() Cloned Argument tableData.
//                        
//                        
//-----------------------------------------------------------------
//                 danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview.utilities;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

/**
 *    The MyJSQLView_TableModel class provides the model that is
 * used with the TableTabPanels to display a summary table of
 * database data. The class provides the basic defaults and a way
 * to update the table data when sort, search, or the next/previous
 * actions are generated.
 * 
 * @author Dana M. Proctor
 * @version 2.6 09/20/2012
 */

public class MyJSQLView_TableModel extends AbstractTableModel
{
   // Class Instances.

   private static final long serialVersionUID = 3515206651333774517L;

   private ArrayList<String> headings;
   private Object[][] data;
   private boolean[] editableColumns;

   //==============================================================
   // MyJSQLView_TableModel Constructor.
   //==============================================================

   public MyJSQLView_TableModel(ArrayList<String> headings, Object[][] tableData)
   {
      this.headings = headings;
      data = tableData.clone();
      editableColumns = new boolean[headings.size()];
   }
   
   //==============================================================
   // Required default method implementations.
   //==============================================================

   public int getRowCount()
   {
      if (data == null)
         return 0;
      else
         return data.length;
   }
   
   public ArrayList<String> getColumns()
   {
      return headings;
   }

   public int getColumnCount()
   {
      if (headings == null)
         return 0;
      else
         return headings.size();
   }

   public Object getValueAt(int row, int column)
   {
      return data[row][column];
   }

   //==============================================================
   // Class method to return the column names.
   //==============================================================

   public String getColumnName(int column)
   {
      return headings.get(column);
   }

   //==============================================================
   // Class method to insure default renderer/editor for each cell.
   //==============================================================

   public Class<?> getColumnClass(int col)
   {
      if (getValueAt(0, col) != null)
         return getValueAt(0, col).getClass();
      else
         return "".getClass();
   }

   //==============================================================
   // Class method to insure no cells in the table are editable.
   //==============================================================

   public boolean isCellEditable(int row, int col)
   {
      return editableColumns[col];
   }

   //==============================================================
   // Class method to set the whether the table is editable.
   //==============================================================

   public void setEditable()
   {
      for (int i = 0; i < editableColumns.length; i++)
         editableColumns[i] = true;
   }

   //==============================================================
   // Class method to set the editable columns in the table.
   //==============================================================

   public void setColumnEditable(int col, boolean value)
   {
      editableColumns[col] = value;
   }

   //==============================================================
   // Class method to change the data in the table and update
   // display.
   //==============================================================

   public void setValueAt(Object value, int row, int column)
   {
      data[row][column] = value;
      fireTableCellUpdated(row, column);
   }

   //==============================================================
   // Class method to update the entire table.
   //==============================================================

   public void setValues(Object[][] tableData)
   {
      data = tableData.clone();
      fireTableDataChanged();
   }
}
