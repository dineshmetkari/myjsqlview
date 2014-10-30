//=================================================================
//                  MyJSQLView SQL Tab Panel
//=================================================================
//
//    This class provides the view of resultant data/results from
// the direct input of SQL commands executed on the database.
//
//                    << SQLTabPanel.java >>
//
//=================================================================
// Copyright (C) 2005-2014 Dana M. Proctor
// Version 2.8 10/30/2014
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
// Version 1.0 Original Initial SQLTabPanel Class.
//         1.1 Minor Cleanup, Commenting, for Version Release 3.30.
//         1.2 Added Class Instance columnNamesHashMap() & Class Methods
//             actionPerformed(), print(), getTableHeadings(), getListTable(),
//             createListTablePopup(), getColumnNamesHashMap(),
//             getColumnClassHashMap(), & getColumnSizeHashMap(). Removed
//             Class Instance sqlTable, and queryNumber From Constructor
//             Argument. Cleaned Up.
//         1.3 Corrected to Short-Circuit Logical Operator && in Conditional
//             Check for Text in executeSQL().
//         1.4 Made Inner Class SQLTableModel static & Removed All Elements
//             in tableHeadings for setTableRowSize().
//         1.5 Removed Class Method setTableRowSize(). Minor Format Changes
//             in SQLTableModel Inner Class.
//         1.6 Modification to Method executeSQL() to Accomodate MSAccess
//             by Limiting db_resultSet Collection to Only Once.
//         1.7 Copyright Update.
//         1.8 Removed the Casting of (Connection) for the Returned Instance for
//             the ConnectionManager.getConnection() executeSQL().
//         1.9 Changed Class Instance tableHeadings from Vector to ArrayList.
//             Also Same for Return Type for getTableHeadings() & rows Instance
//             in SQLTableModel Class.
//         2.0 Class Method executeSQL Change in Collection of Column Names by
//             Using tableMetaData.getColumnLabel(). Change in Same to Throw a
//             SQL Exception With Finally So That Statements and Connection can
//             be Properly Closed.
//         2.1 Changes in Way MyJSQLView_ResourceBundle Handles the Collection
//             of Resource Strings. Change to resource.getResourceString(key,
//             default).
//         2.2 Class Method Instance columnSize Change from Integer to int.
//         2.3 Change Package Name to com.dandymadeproductions.myjsqlview.gui.panels.
//             Made Class, Constructor, & Getter Methods Public.
//         2.4 Change in executeSQL() to Use DBTablePanel.getGeneralDBProperties().
//         2.5 Added Class Instances ACTION_SELECT_ALL & ACTION_DESELECT_ALL. Changed
//             Static Instance maxPreferredColumnSize to All Capitals. Changed
//             Resource to Reference SQLTablePanel & Added 'Empty'.
//         2.6 Excluded the TIMESTAMP Type of MSSQL From Being Processed in
//             Method executeSQL(). In Same Method Processing for DATETIMEOFFSET
//             & IMAGE Types & Catching All DATETIME Types With indexOf Conditional.
//         2.7 Method executeSQL() Inclusion of Processing for MariaDB for Fields
//             Timestamp, Bit, & Text.
//         2.8 Class Method executeSQL() Increment of j Only After try/catch Clause
//             for BIT Types of MySQL & MariaDB. Same Method Conditional Check for
//             YEAR in Same Databases Size of 4 Before Sub-Stringing.
//        
//-----------------------------------------------------------------
//                 danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview.gui.panels;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseListener;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.RepaintManager;
import javax.swing.ScrollPaneConstants;
import javax.swing.TransferHandler;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;

import com.dandymadeproductions.myjsqlview.MyJSQLView;
import com.dandymadeproductions.myjsqlview.datasource.ConnectionManager;
import com.dandymadeproductions.myjsqlview.gui.MyJSQLView_MouseAdapter;
import com.dandymadeproductions.myjsqlview.gui.QueryFrame;
import com.dandymadeproductions.myjsqlview.utilities.MyJSQLView_ResourceBundle;
import com.dandymadeproductions.myjsqlview.utilities.MyJSQLView_Utils;
import com.dandymadeproductions.myjsqlview.utilities.TableSorter;

/**
 *    The SQLTabPanel class provides the view of resultant data/results
 * from the direct input of SQL commands executed on the database.  
 * 
 * @author Dana M. Proctor
 * @version 2.8 10/30/2014
 */

public class SQLTabPanel extends JPanel implements ActionListener, Printable
{
   // Class Instances.
   private static final long serialVersionUID = -2175726421156127449L;

   private String sqlString;
   private boolean validQuery;

   private int tableRowLimit;
   private String dataSourceType;
   
   private ArrayList<String> tableHeadings;
   private HashMap<String, String> columnNamesHashMap;
   private HashMap<String, String> columnClassHashMap;
   private HashMap<String, String> columnTypeHashMap;
   private HashMap<String, Integer> columnSizeHashMap;
   private HashMap<String, Integer> preferredColumnSizeHashMap;
   private MyJSQLView_ResourceBundle resourceBundle;
   
   private JPanel centerPanel;
   private transient MouseListener summaryTablePopupListener;

   private SQLTableModel tableModel;
   private JTable listTable;
   private JScrollPane tableScrollPane;
   
   private static final String ACTION_SELECT_ALL = "Select All";
   private static final String ACTION_DESELECT_ALL = "DeSelect All";
   private static final int MAX_PREFFERED_COLUMN_SIZE = 350;
   
   //==============================================================
   // SQLTabPanel Constructor
   //==============================================================

   public SQLTabPanel(String sqlString, int queryRowLimit,
                         MyJSQLView_ResourceBundle resourceBundle)
   {
      this.sqlString = sqlString;
      tableRowLimit = queryRowLimit;
      this.resourceBundle = resourceBundle;
      
      // Setting up a data source name qualifier and other
      // instances.
      
      dataSourceType = ConnectionManager.getDataSourceType();
      validQuery = false;
      
      tableModel = new SQLTableModel();
      tableHeadings = new ArrayList <String>();
      columnNamesHashMap = new HashMap <String, String>();
      columnClassHashMap = new HashMap <String, String>();
      columnTypeHashMap = new HashMap <String, String>();
      columnSizeHashMap = new HashMap <String, Integer>();
      preferredColumnSizeHashMap = new HashMap <String, Integer>();
      
      // General Panel Configurations
      setLayout(new BorderLayout());
      setBorder(BorderFactory.createRaisedBevelBorder());
      centerPanel = new JPanel(new BorderLayout());
      
      // Connecting to the database to execute the input
      // sql to see if a valid table can be loaded.
      
      try
      {
         executeSQL();
      }
      catch (SQLException e)
      {
         String errorString = "SQLException: " + e.getMessage() + " " + "SQLState: " 
                              + e.getSQLState() + " " + "VendorError: " + e.getErrorCode();
         QueryFrame.setQueryResultTextArea(errorString);
      }
      
      // ==================================================
      // Setting up the Summary Table View.
      // ==================================================

      if (validQuery)
      {
         TableSorter tableSorter = new TableSorter(tableModel);
         listTable = new JTable(tableSorter);
         tableSorter.setTableHeader(listTable.getTableHeader());
         listTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
         listTable.getActionMap().put(TransferHandler.getCopyAction().getValue(Action.NAME),
                                         TransferHandler.getCopyAction());
         createListTablePopupMenu();
         listTable.addMouseListener(summaryTablePopupListener);

         // Sizing columns
         Iterator<String> headings = tableHeadings.iterator();
         TableColumn column = null;
         int i = 0;

         while (headings.hasNext())
         {
            column = listTable.getColumnModel().getColumn(i++);
            column.setPreferredWidth((preferredColumnSizeHashMap.get(headings.next())).intValue());
         }

         // Create a scrollpane for the table.
         
         tableScrollPane = new JScrollPane(listTable);
         tableScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
         centerPanel.add(tableScrollPane, BorderLayout.CENTER);
      }

      add(centerPanel, BorderLayout.CENTER);
      addMouseListener(MyJSQLView.getPopupMenuListener()); 
   }
   
   //==============================================================
   // ActionEvent Listener method for detecting the inputs from
   // the panel and directing to the appropriate routine.
   //=============================================================

   public void actionPerformed(ActionEvent evt)
   {
      Object panelSource = evt.getSource();

      if ((panelSource instanceof JButton || panelSource instanceof JMenuItem) && validQuery)
      {
         // listTable Popup Menu Actions
         if (panelSource instanceof JMenuItem)
         {
            String actionCommand = ((JMenuItem) panelSource).getActionCommand();
            // System.out.println(actionCommand);

            if (actionCommand.equals(ACTION_SELECT_ALL))
               listTable.selectAll();
            else if (actionCommand.equals(ACTION_DESELECT_ALL))
               listTable.clearSelection();
            // Copy
            else if (actionCommand.equals((String)TransferHandler.getCopyAction().getValue(Action.NAME)))
            {
               Action a = listTable.getActionMap().get(actionCommand);
               if (a != null)
                  a.actionPerformed(new ActionEvent(listTable, ActionEvent.ACTION_PERFORMED, null));
            }
         }
      }
   }
         
   //==============================================================
   // Class method to execute the given user's input SQL statement.
   //==============================================================

   private void executeSQL() throws SQLException
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
      int preferredColumnSize;
      Object currentContentData;
      Object[] rowData;
      
      // Checking to see if anything in the input to
      // execute.
      
      if (sqlString.length() < 1)
      {
         validQuery = false;
         return;
      }

      // Setting up a connection.
      dbConnection = ConnectionManager.getConnection("SQLTabPanel executeSQL()");
      
      if (dbConnection == null)
      {
         validQuery = false;
         return;
      }
      
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
               preferredColumnSizeHashMap.put(colNameString,
                                              Integer.valueOf(colNameString.length() * 9));
               
               // Load table model.
               tableModel.setHeader(tableHeadings.toArray());
               
               // Set data.
               rowData = new Object[1];
               rowData[0] = "(" + resourceBundle.getResourceString("SQLTabPanel.label.Empty", "Empty") + ")";
               tableModel.addRow(rowData);  
               
               validQuery = true;
               return;
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
               //                     columnClass + " " + columnType + " " +
               //                     columnSize);

               // This going to be a problem so skip these columns.
               // NOT TESTED. This is still problably not going to
               // help. Bound to crash later.

               if ((columnClass == null && columnType == null)
                   ||(dataSourceType.equals(ConnectionManager.MSSQL)
                      && columnType.toUpperCase().equals("TIMESTAMP")))
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
               preferredColumnSizeHashMap.put(colNameString,
                                              Integer.valueOf(colNameString.length() * 9));   
            }
            tableModel.setHeader(tableHeadings.toArray());
            
            // Try and Load the Data From the SQL Execution.
            
            int i = 0;
            int j = 0;
            rowData = new Object[tableHeadings.size()];

            while (db_resultSet.next())
            {
               Iterator<String> headings = tableHeadings.iterator();
               while (headings.hasNext())
               {
                  colNameString = headings.next();
                  columnClass = columnClassHashMap.get(colNameString);
                  columnType = columnTypeHashMap.get(colNameString);
                  columnSize = (columnSizeHashMap.get(colNameString)).intValue();
                  preferredColumnSize = (preferredColumnSizeHashMap.get(colNameString)).intValue();

                  // System.out.println(i + " " + j + " " + colNameString + " " +
                  //                     columnClass + " " + columnType + " " +
                  //                     columnSize + " " + preferredColumnSize);

                  // Storing data appropriately. If you have some
                  // date or other formating, here is where you can
                  // take care of it.
                  
                  // =============================================
                  // BigDecimal
                  if (columnClass.indexOf("BigDecimal") != -1)
                  {
                     currentContentData = db_resultSet.getString(colNameString);
                     if (currentContentData == null)
                        rowData[j++] = "NULL";
                     else
                        rowData[j++] = new BigDecimal(currentContentData.toString());
                  }

                  // =============================================
                  // Date
                  else if (columnType.equals("DATE"))
                  {
                     currentContentData = db_resultSet.getObject(colNameString);
                     if (currentContentData == null)
                        rowData[j++] = "NULL";
                     else
                        rowData[j++] = new SimpleDateFormat(DBTablesPanel.getGeneralDBProperties()
                           .getViewDateFormat()).format(currentContentData);
                  }
                  
                  // =============================================
                  // Datetime Offset
                  else if (columnType.equals("DATETIMEOFFSET"))
                  {
                     String dateString, timeString;
                     
                     currentContentData = db_resultSet.getString(colNameString);
                     
                     if (currentContentData == null)
                        rowData[j++] = "NULL";
                     else
                     {
                        dateString = currentContentData + "";
                        dateString = dateString.substring(0, (dateString.indexOf(" ")));
                        dateString = MyJSQLView_Utils.convertDBDateString_To_ViewDateString(dateString,
                           DBTablesPanel.getGeneralDBProperties().getViewDateFormat());
                        
                        timeString = currentContentData + "";
                        timeString = timeString.substring(timeString.indexOf(" "));
                        
                        rowData[j++] = dateString + timeString;
                     }
                  }

                  // =============================================
                  // Datetime
                  else if (columnType.indexOf("DATETIME") != -1)
                  {
                     currentContentData = db_resultSet.getObject(colNameString);
                     if (currentContentData == null)
                        rowData[j++] = "NULL";
                     else
                        rowData[j++] = new SimpleDateFormat(DBTablesPanel.getGeneralDBProperties()
                           .getViewDateFormat() + " HH:mm:ss").format(currentContentData);
                  }

                  // =============================================
                  // Time With Time Zone
                  else if (columnType.equals("TIMETZ"))
                  {
                     currentContentData = db_resultSet.getTime(colNameString);
                     if (currentContentData == null)
                        rowData[j++] = "NULL";
                     else
                        rowData[j++] = (new SimpleDateFormat("HH:mm:ss z").format(currentContentData));
                  }

                  // =============================================
                  // Timestamps
                  else if (columnType.equals("TIMESTAMP"))
                  {
                     currentContentData = db_resultSet.getTimestamp(colNameString);
                     // System.out.println(currentContentData);
                     
                     if (currentContentData == null)
                        rowData[j++] = "NULL";
                     else
                     {
                        // Old MySQL Database Requirement, 4.x.
                        if (dataSourceType.equals(ConnectionManager.MYSQL)
                            || dataSourceType.equals(ConnectionManager.MARIADB))
                        {
                           if (columnSize == 2)
                              rowData[j++] = (new SimpleDateFormat("yy").format(currentContentData));
                           else if (columnSize == 4)
                              rowData[j++] = (new SimpleDateFormat("MM-yy").format(currentContentData));
                           else if (columnSize == 6)
                              rowData[j++] = (new SimpleDateFormat("MM-dd-yy").format(currentContentData));
                           else if (columnSize == 8)
                              rowData[j++] = (new SimpleDateFormat("MM-dd-yyyy").format(currentContentData));
                           else if (columnSize == 10)
                              rowData[j++] = (new SimpleDateFormat("MM-dd-yy HH:mm")
                                    .format(currentContentData));
                           else if (columnSize == 12)
                              rowData[j++] = (new SimpleDateFormat("MM-dd-yyyy HH:mm")
                                    .format(currentContentData));
                           // All current coloumnSizes for MariaDB, MySQL > 5.0 Should be 19.
                           else
                              rowData[j++] = (new SimpleDateFormat(
                                 DBTablesPanel.getGeneralDBProperties().getViewDateFormat() + " HH:mm:ss")
                                    .format(currentContentData));
                        }
                        else
                           rowData[j++] = (new SimpleDateFormat(
                              DBTablesPanel.getGeneralDBProperties().getViewDateFormat() + " HH:mm:ss")
                                 .format(currentContentData));  
                     }
                  }

                  else if (columnType.equals("TIMESTAMPTZ") || columnType.equals("TIMESTAMP WITH TIME ZONE"))
                  {
                     currentContentData = db_resultSet.getTimestamp(colNameString);
                     if (currentContentData == null)
                        rowData[j++] = "NULL";
                     else
                        rowData[j++] = (new SimpleDateFormat(DBTablesPanel.getGeneralDBProperties()
                           .getViewDateFormat() + " HH:mm:ss z").format(currentContentData));
                  }

                  // =============================================
                  // Year
                  else if (columnType.equals("YEAR"))
                  {
                     currentContentData = db_resultSet.getObject(colNameString);
                     if (currentContentData == null)
                        rowData[j++] = "NULL";
                     else
                     {
                        String displayYear = currentContentData + "";
                        displayYear = displayYear.trim();

                        if (columnSize == 2)
                        {
                           if (columnSize == 2)
                           {
                              if (displayYear.length() == 4)
                                 displayYear = displayYear.substring(2, 4);
                           }
                        }
                        else
                           displayYear = displayYear.substring(0, 4);
                        
                        rowData[j++] = displayYear;
                     }
                  }

                  // =============================================
                  // Blob
                  else if (columnClass.indexOf("String") == -1 && columnType.indexOf("BLOB") != -1)
                  {
                     if (columnSize == 255)
                        rowData[j++] = "Tiny Blob";
                     else if (columnSize == 65535)
                        rowData[j++] = "Blob";
                     else if (columnSize == 16777215)
                        rowData[j++] = "Medium Blob";
                     else if (columnSize > 16777215)
                        rowData[j++] = "Long Blob";
                     else
                        rowData[j++] = "Blob";
                  }
                  
                  //=============================================
                  // CLOB
                  else if (columnType.indexOf("CLOB") != -1)
                  {
                     rowData[j++] = "Clob";
                  }

                  // =============================================
                  // BYTEA
                  else if (columnType.equals("BYTEA"))
                  {
                     rowData[j++] = "Bytea";
                  }

                  // =============================================
                  // BINARY
                  else if (columnType.indexOf("BINARY") != -1 || columnType.indexOf("IMAGE") != -1)
                  {
                     rowData[j++] = "Binary";
                  }
                  
                  //=============================================
                  // RAW
                  else if (columnType.indexOf("RAW") != -1)
                  {
                     rowData[j++] = "Raw";
                  }
                  
                  //=============================================
                  // IMAGE
                  else if (columnType.indexOf("IMAGE") != -1)
                  {
                     rowData[j++] = "IMAGE";
                  }

                  // =============================================
                  // Boolean
                  else if (columnClass.indexOf("Boolean") != -1)
                  {
                     currentContentData = db_resultSet.getString(colNameString);
                     if (currentContentData == null)
                        rowData[j++] = "NULL";
                     else
                        rowData[j++] = currentContentData.toString();
                  }

                  // =============================================
                  // Bit
                  else if (columnType.indexOf("BIT") != -1
                           && (dataSourceType.equals(ConnectionManager.MYSQL)
                               || dataSourceType.equals(ConnectionManager.MARIADB)))
                  {
                     currentContentData = db_resultSet.getString(colNameString);
                     
                     if (currentContentData == null)
                        rowData[j++] = "NULL";
                     else
                     {
                        try
                        {
                           // int bitValue = rs.getInt(columnName);
                           // tableData[i][j] = Integer.toBinaryString(bitValue);
                           rowData[j] = Integer.toBinaryString(Integer.parseInt(
                              currentContentData.toString()));
                        }
                        catch (NumberFormatException e)
                        {
                           rowData[j] = "0";
                        }
                        j++;   
                     }
                  }

                  // =============================================
                  // Text
                  else if (columnClass.indexOf("String") != -1 && !columnType.equals("CHAR")
                           && columnSize > 255)
                  {
                     currentContentData = db_resultSet.getObject(colNameString);
                     if (currentContentData == null)
                        rowData[j++] = "NULL";
                     else
                     {
                        if (columnSize <= 65535)
                           rowData[j++] = (String) currentContentData;
                        else if (columnSize == 16777215)
                           rowData[j++] = ("Medium Text");
                        else
                        // (columnSize > 16777215)
                        {
                           if (dataSourceType.equals(ConnectionManager.MYSQL)
                               || dataSourceType.equals(ConnectionManager.MARIADB))
                              rowData[j++] = ("Long Text");
                           else
                           {
                              // Limit Table Cell Memory Usage.
                              if (((String) currentContentData).length() > 512)
                                 rowData[j++] = ((String) currentContentData).substring(0, 512);
                              else
                                 rowData[j++] = (String) currentContentData;
                           }
                        }   
                     }
                  }
                  
                  // =============================================
                  // LONG
                  else if (columnClass.indexOf("String") != -1 && columnType.equals("LONG"))
                  {
                     currentContentData = db_resultSet.getObject(colNameString);
                     if (currentContentData == null)
                        rowData[j++] = "NULL";
                     else
                     {
                        // Limit Table Cell Memory Usage.
                        if (((String) currentContentData).length() > 512)
                           rowData[j++] = ((String) currentContentData).substring(0, 512);
                        else
                           rowData[j++] = (String) currentContentData;
                     }
                  }

                  // =============================================
                  // Array
                  else if ((columnClass.indexOf("Object") != -1 || columnClass.indexOf("Array") != -1)
                           && (columnType.indexOf("_") != -1))
                  {
                     currentContentData = db_resultSet.getString(colNameString);
                     if (currentContentData == null)
                        rowData[j++] = "NULL";
                     else
                     {
                        String stringName = (String) currentContentData;

                        // Limit Table Cell Memory Usage.
                        if (stringName.length() > 512)
                           rowData[j++] = stringName.substring(0, 512);
                        else
                           rowData[j++] = stringName;
                     }
                  }

                  // =============================================
                  // Any Other
                  else
                  {
                     currentContentData = db_resultSet.getString(colNameString);
                     if (currentContentData == null)
                        rowData[j++] = "NULL";
                     else
                        rowData[j++] = currentContentData.toString().trim();
                  }

                  // Setup some sizing for the column in the summary table.
                  
                  if ((rowData[j - 1] + "").length() * 9 > preferredColumnSize)
                  {
                     preferredColumnSize = (rowData[j - 1] + "").length() * 9;
                     if (preferredColumnSize > MAX_PREFFERED_COLUMN_SIZE)
                        preferredColumnSize = MAX_PREFFERED_COLUMN_SIZE;
                  }
                  preferredColumnSizeHashMap.put(colNameString, Integer.valueOf(preferredColumnSize));
               }
               tableModel.addRow(rowData);
               j = 0;
               i++;
            }
            db_resultSet.close();
            sqlStatement.close();
         }
         // No results, data, but was update.
         else
         {
            // Fill information instances.
            colNameString = "Update Count";
            columnClass = "java.lang.String";
            columnType = "VARCHAR";
            columnSize = 30;
            
            tableHeadings.add(colNameString);
            columnNamesHashMap.put(colNameString, colNameString);
            columnClassHashMap.put(colNameString, columnClass);
            columnTypeHashMap.put(colNameString, columnType.toUpperCase());
            columnSizeHashMap.put(colNameString, Integer.valueOf(columnSize));
            preferredColumnSizeHashMap.put(colNameString,
                                           Integer.valueOf(colNameString.length() * 9));
            
            // Load table model.
            tableModel.setHeader(tableHeadings.toArray());
            
            rowData = new Object[1];
            rowData[0] = updateCount;
            tableModel.addRow(rowData);
         }
         
         // Looks good so validate.
         validQuery = true;
      }
      catch (SQLException e)
      {
         String errorString = "SQLException: " + e.getMessage() + " " + "SQLState: " 
                              + e.getSQLState() + " " + "VendorError: " + e.getErrorCode();
         QueryFrame.setQueryResultTextArea(errorString);
         validQuery = false;
         return;
      }
      finally
      {
         if (sqlStatement != null)
            sqlStatement.close();
         
         ConnectionManager.closeConnection(dbConnection, "SQLTabPanel executeSQL()");    
      }
   }
   
   //==============================================================
   // Class method to create the summary table view popup menu.
   //==============================================================

   private void createListTablePopupMenu()
   {
      // Method Instances.
      JPopupMenu summaryTablePopupMenu = new JPopupMenu();
      JMenuItem menuItem;
      String resource;
      
      // Summary Table select actions.
      
      resource = resourceBundle.getResourceString("SQLTabPanel.menu.SelectAll", "Select All");
      menuItem = new JMenuItem(resource);
      menuItem.setActionCommand(ACTION_SELECT_ALL);
      menuItem.addActionListener(this);
      summaryTablePopupMenu.add(menuItem);

      resource = resourceBundle.getResourceString("SQLTabPanel.menu.DeSelectAll", "DeSelect All");
      menuItem = new JMenuItem(resource);
      menuItem.setActionCommand(ACTION_DESELECT_ALL);
      menuItem.addActionListener(this);
      summaryTablePopupMenu.add(menuItem);
      
      // Summary Table copy/paste? actions
      
      summaryTablePopupMenu.addSeparator();
      
      resource = resourceBundle.getResourceString("SQLTabPanel.menu.Copy", "Copy");
      menuItem = new JMenuItem(resource);
      menuItem.setActionCommand((String)TransferHandler.getCopyAction().getValue(Action.NAME));
      menuItem.setMnemonic(KeyEvent.VK_C);
      menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
      menuItem.addActionListener(this);
      summaryTablePopupMenu.add(menuItem);
      
      summaryTablePopupListener = new MyJSQLView_MouseAdapter(summaryTablePopupMenu);
   }
    
   //==============================================================
   // Class Method to print the Panel's current information.
   //==============================================================

   public int print(Graphics g, PageFormat pageFormat, int pageIndex)
   {
      if (pageIndex > 0)
         return NO_SUCH_PAGE;
      Graphics2D g2 = (Graphics2D) g;
      g2.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

      RepaintManager currentManager = RepaintManager.currentManager(this);
      currentManager.setDoubleBufferingEnabled(false);
      final Rectangle rect = g2.getClipBounds();

      double scaleFactor = rect.getWidth() / this.getWidth();
      g2.scale(scaleFactor, scaleFactor);
      // pageFormat.setOrientation(PageFormat.LANDSCAPE);
      paintAll(g2);
      currentManager.setDoubleBufferingEnabled(true);
      return PAGE_EXISTS;
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
   // Class method to allow classes to obtain the summary list
   // table presently displayed in the tab.
   //==============================================================

   public JTable getListTable()
   {
      return listTable;
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
   
   //==============================================================
   // Class helper for the JTable, listTable, Table Model.
   //==============================================================
   
   static class SQLTableModel extends AbstractTableModel
   {
      private static final long serialVersionUID = 1229214973355124583L;
      private Object[] headers;
      private ArrayList<Object[]> rows;
      
      protected SQLTableModel()
      {
         // Just Intialize Class Instances.
         headers = new Object[0];
         rows = new ArrayList <Object[]>();
      }
      
      public void addRow(Object[] rowData)
      {
         Object[] currentRow = new Object[rowData.length];
         for (int i = 0; i < rowData.length; i++)
         {
            currentRow[i] = rowData[i];
         }
         //System.arraycopy(rowData, 0, row, 0, rowData.length);
         rows.add(currentRow);
      }
      
      public void clear(){rows.clear();}
      
      public String getColumnName(int i){return headers[i].toString();}
      public int getColumnCount(){return headers.length;}
      public ArrayList<Object[]> getData(){return rows;}
      public int getRowCount(){return rows.size();}
      public Object getValueAt(int row, int col)
      {
         if (row >= rows.size())
            return null;

         Object[] colArray = rows.get(row);

         if (col >= colArray.length)
            return null;
         
         return colArray[col];
      }
      public void setHeader(Object[] colNames)
      {
         headers = new Object[colNames.length];
         System.arraycopy(colNames, 0, headers, 0, colNames.length);
      }
   } 
}
