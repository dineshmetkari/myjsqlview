//=================================================================
//           TableRecordCount TableRecordCountPanel
//=================================================================
//
//    This class provides the panel that holds components
// associated with MyJSQLView basic tutorial for a plugin module.
//
//               << TableRecordCountPanel.java >>
//
//=================================================================
// Copyright (C) 2005-2010 Dana M. Proctor
// Version 1.7 09/02/2010
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
//         1.2 Formatted and Moved the Delay in The Constructor Needed to Fix the
//             Problem in v3.17 Code to PluginModule initPlugin().
//         1.3 Added Thread to actionPerformed() Method and Moved All Processing
//             From That Method to New Method executreRecordCount().
//         1.4 Comment Changes for executeRecordCount() Method and disableActions
//             During Processing of a Record Count in Same Method.
//         1.5 Parameterized Argument tableNames in Constructor and Class Method
//             reloadPanel() Along With Iterator Instance tableNamesIterator.
//         1.6 Replaced Code to Properly Format the schemaTableName Instance
//             by the MyJSQLView_Utils.getSchemaTableName() Instead of Manually
//             in Method executeRecordCount(). Removed Method Instance 
//             identifierQuoteString.
//         1.7 Added Class Instance version and Method getVersion().
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

import com.dandymadeproductions.myjsqlview.MyJSQLView_Access;
import com.dandymadeproductions.myjsqlview.DBTablesPanel;
import com.dandymadeproductions.myjsqlview.TableTabPanel;
import com.dandymadeproductions.myjsqlview.MyJSQLView_Utils;

/**
 * The TableRecordCountPanel class provides the panel that holds components
 * associated with the MyJSQLView basic tutorial for a plugin module.
 * 
 * @author Dana M. Proctor
 * @version 1.7 09/02/2010
 */

class TableRecordCountPanel extends JPanel implements ActionListener
{
   // Class Instances.
   private static final long serialVersionUID = 2500935698652883672L;
   private final static String version = "Version 1.7";
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
         Thread actionThread = new Thread(new Runnable()
         {
            public void run()
            {
               executeRecordCount();
            }
         }, "TableRecordCountPanel.actionThread");
         actionThread.start();
      }
   }
   
   //==============================================================
   // Class Method to execute the required processing the selected
   // database table to determine the record count.
   //==============================================================

   private void executeRecordCount()
   {
      // Setup Instances.
      Connection dbConnection;
      String tableName;
      String schemaTableName;

      String sqlStatementString;
      Statement sqlStatement;
      ResultSet rs;

      int rowCount = 0;
      tableSelectionComboBox.setEnabled(false);
      disableActions = true;

      // Get Connection to Database.

      dbConnection = MyJSQLView_Access.getConnection("TableRecordCountPanel executeRecordCount()");

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

         // Collect the properly formatted schema table name with identifier
         // quotes for execution in a SQL statement. 
         
         schemaTableName = MyJSQLView_Utils.getSchemaTableName(tableName);
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
         MyJSQLView_Access.displaySQLErrors(e, "TableRecordCountPanel executeRecordCount()");
         recordCountLabel.setText("0");
         tableSelectionComboBox.setEnabled(true);
         disableActions = false;
      }
      
      // Close connection to database.
      
      MyJSQLView_Access.closeConnection(dbConnection, "TableRecordCountPanel executeRecordCount()");
      tableSelectionComboBox.setEnabled(true);
      disableActions = false;
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
   
   //==============================================================
   // Class method to get the plugin's version.
   //==============================================================

   protected static String getVersion()
   {
      return version;
   }
}