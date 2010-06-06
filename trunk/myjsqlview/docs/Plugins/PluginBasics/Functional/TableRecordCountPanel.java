//=================================================================
//           TableRecordCount TableRecordCountPanel
//=================================================================
//
//    This class provides the panel that holds holds components
// associated with MyJSQLView basic tutorial for a plugin module.
//
//               << TableRecordCountPanel.java >>
//
//=================================================================
// Copyright (C) 2005-2010 Dana M. Proctor
// Version 1.3 06/05/2010
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
// Version 1.0 Initial TableFieldChartsPanel Class.
//         1.1 Cleaned Up Some & Implemented reloadPanel().
//         1.2 Formatted and Moved the Delay in The Constructor Needed to
//             Fix the Problem in v3.17 Code to PluginModule initPlugin().
//         1.3 Parameterized Argument tableNames in Constructor and Class
//             Method reloadPanel() Along With Iterator Instance 
//             tableNamesIterator.
//                           
//-----------------------------------------------------------------
//                 danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.tablerecordcount;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import java.util.Iterator;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.dandymadeproductions.myjsqlview.TableTabPanel;
import com.dandymadeproductions.myjsqlview.DBTablesPanel;
import com.dandymadeproductions.myjsqlview.MyJSQLView_Access;

/**
 * The TableRecordCountPanel class provides the panel that holds components
 * associated with the MyJSQLView basic tutorial for a plugin module.
 * 
 * @author Dana M. Proctor
 * @version 1.3 06/05/2010
 */

class TableRecordCountPanel extends JPanel implements ActionListener
{
   // Class Instances.
   private static final long serialVersionUID = 2500935698652883672L;
   private JComboBox tableSelectionComboBox;
   private JLabel recordCountLabel;
   private boolean disableActions;

   //==============================================================
   // TableRecordCountPanel Constructor
   //==============================================================

   TableRecordCountPanel(Vector<String> tableNames)
   {
      // Method Instances.
      JLabel tableNameLabel;

      // Create the components to select the database table,
      // identify the record count and the actual processed
      // row number.

      // Table Selector.
      tableSelectionComboBox = new JComboBox();
      tableSelectionComboBox.setEnabled(false);

      if (!tableNames.isEmpty())
      {
         Iterator<String> tableNamesIterator = tableNames.iterator();
         while (tableNamesIterator.hasNext())
            tableSelectionComboBox.addItem(tableNamesIterator.next());

         tableSelectionComboBox.setEnabled(true);
      }
      tableSelectionComboBox.addActionListener(this);
      add(tableSelectionComboBox);

      // Identify Count.
      tableNameLabel = new JLabel("Record Count: ");
      add(tableNameLabel);

      // Record Count Value.
      recordCountLabel = new JLabel("0");
      add(recordCountLabel);
   }

   //==============================================================
   // ActionEvent Listener method for detecting the selection of
   // a table from the tableSelectionComboBox so that the row,
   // record count can be processed.
   //==============================================================

   public void actionPerformed(ActionEvent evt)
   {
      Object panelSource = evt.getSource();

      if (panelSource instanceof JComboBox && !disableActions)
      {
         // Setup Instances.
         Connection dbConnection;
         String tableName;
         String identifierQuoteString;
         String schemaTableName;

         String sqlStatementString;
         Statement sqlStatement;
         ResultSet rs;

         int rowCount = 0;

         // Get Connection to Database.

         dbConnection = MyJSQLView_Access.getConnection("TableRecordCountPanel actionPerformed()");

         try
         {
            // Create a statement object to be used with the connection.

            sqlStatement = dbConnection.createStatement();

            // Collect the selected table from the combobox.

            tableName = (String) tableSelectionComboBox.getSelectedItem();
            // System.out.println(tableName);

            // Collect the table name as referenced in the database.
            TableTabPanel selectedTablePanel = DBTablesPanel.getTableTabPanel(tableName);

            if (selectedTablePanel == null)
               return;

            tableName = selectedTablePanel.getTableName();
            // System.out.println(tableName);

            // Collect the schema table name if required. Future versions of
            // MyJSQLView will handle this or can be obtained from
            // MyJSQLView_Utils.

            identifierQuoteString = MyJSQLView_Access.getIdentifierQuoteString();

            if (tableName.indexOf(".") != -1)
            {
               schemaTableName = identifierQuoteString + tableName.substring(0, tableName.indexOf("."))
                                 + identifierQuoteString + "." + identifierQuoteString
                                 + tableName.substring(tableName.indexOf(".") + 1) + identifierQuoteString;
            }
            else
               schemaTableName = identifierQuoteString + tableName + identifierQuoteString;
            // System.out.println(schemaTableName);

            // Setup the statement string and execute the database query.

            sqlStatementString = "SELECT COUNT(*) FROM " + schemaTableName;
            rs = sqlStatement.executeQuery(sqlStatementString);
            rs.next();
            rowCount = rs.getInt(1);

            // Set the record count label to reflect the results
            // and close out.

            recordCountLabel.setText(Integer.toString(rowCount));

            rs.close();
            sqlStatement.close();
         }
         catch (SQLException e)
         {
            MyJSQLView_Access.displaySQLErrors(e, "TableRecordCountPanel actionPerformed()");
            recordCountLabel.setText("0");
         }
      }
   }

   //==============================================================
   // Class Method to reset the panel's table selector comboBox
   // if MyJSQLView is called to reload the database tables.
   //==============================================================

   protected void reloadPanel(Vector<String> tableNames)
   {
      // Insure no actions are taken during reload.

      tableSelectionComboBox.setEnabled(false);
      disableActions = true;

      // Clear the components of old data.

      tableSelectionComboBox.removeAllItems();
      recordCountLabel.setText("0");

      // Try reloading tables.
      if (!tableNames.isEmpty())
      {
         Iterator<String> tableNamesIterator = tableNames.iterator();
         while (tableNamesIterator.hasNext())
         {
            tableSelectionComboBox.addItem(tableNamesIterator.next());
         }
         tableSelectionComboBox.setEnabled(true);
      }
      disableActions = false;
   }
}