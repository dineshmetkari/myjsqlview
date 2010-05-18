//=================================================================
//               MyJSQLView DBTablesPanel
//=================================================================
//
//    This class provides the panel that holds all the database
// tables panels, aka. TableTabPanels. In addition the class
// also provides a common mechanism for getting those panels &
// other information about the database tables.
//
//                 << DBTablesPanel.java >>
//
//=================================================================
// Copyright (C) 2005-2010 Dana M. Proctor
// Version 3.7 05/18/2010
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
// Version 1.0 Initial DBTablesPanel Class.
//         1.1 MyJSQLView Project Common Source Code Formatting.
//         1.2 Fault Tolerance for Databases Being Loaded That Has No
//             Defined Tables. Modifications to Constructor, getTableCount(),
//             & getSelectedTableTabPanel().
//         1.3 Added Class Method showTableTabPanel(). Made showTable() Class
//             Method Private.
//         1.4 Added Class Instances dataImport/ExportProperties and Getter
//             and Setter Methods for Same.
//         1.5 Corrected Improper Instantiation of Static Fields tableHashMap,
//             justShowingTable, and tableSelectionComboBox.
//         1,6 Added Conditional Check for NULL work_dbConnection in Class
//             Method actionPerformed().
//         1.7 Class Method getTableTabPanel() & showTableTabPanel(), Conditional
//             Check for NULL Object Returned From Instance tableTabHashMap. Insures
//             Valid Table Data is Available.
//         1.8 Class Instance tableSelectionCombobox Instantiated in Constructor.
//         1.9 Class Instance tableSelectionComboBox Again Instantiated in Declaration.
//             Added Class Method clearTableSelectionComboBoxItems(). Used an
//             Iterator to Fill tableSelectionComboBox in Constructor.
//         2.0 Back to Instantiating tableSelectionComboBox in Constructor.
//         2.1 Renamed Class Method showTableTabPanel() to setSelectedTableTabPanel().
//             Removed Class Instance & Constructor Argument myJSQLViewPopupListener.
//             Changed Class Method loadTable() to static. Added Class Instance
//             statusLabel. Also Added Constructor Instances statusIdle/WorkingIcon.
//         2.2 Added Class Method reloadPanel().
//         2.3 Code Cleanup and Some Minor Comment Changes.
//         2.4 Added Class Instance statusIndicator & statusTimer. Added Class
//             Methods startStatusTimer() & StopStatusTimer().
//         2.5 Changed statusLabel to JTextField. Added a Background Thread for
//             Loading Tables in actionPerformed(). Added a Threaded Delay for
//             Setting statusLabel Back to Idle in stopStatusTimer().
//         2.6 Class Methods actionPerformed() & stopStatusTimer() Provided String
//             Names for the Threads loadTableThread & statusDelayThread, respectively.
//         2.7 Added Class Instance stopStatusDelayThread. Also Modified Class
//             Method stopStatusTimer() to Be Interrupted by Same Instance. Added
//             Several Other Method Instance Also to stopStatusTimer() to TimeSlice.
//         2.8 Implemented a TableTabPanel Limiting Feature to Perserve the Integrity
//             of the Application. Added Class Instance tableTabPanelCardLimit.
//             Changed Instance tableCards to LinkedList. Class Method Modified,
//             loadTable(), getTableTabPanel(), and setTableTabPanel().
//         2.9 Header Format Changes/Update.
//         3.0 Fixed a Bug in Which if All Tables Are Dropped Deactivates the 
//             selectionComboBox, Class Method reloadPanel(). Also Corrected the Lack
//             of Assigning an ActionListener to Same, If on Load There are no Tables
//             Defined.
//         3.1 Removed Constructor Instance fileSeparator. Obtained iconsDirectory From
//             MyJSQLView_Utils Class.
//         3.2 Added fileSeparator to iconsDirectory.
//         3.3 Changed Package to Reflect Dandy Made Productions Code.
//         3.4 Check for NULL tableTabPaneltoRemove in Class Method loadTable().
//             Organized imports.
//         3.5 Reviewed All Methods and Reassigned as Needed to private or public to
//             Properly Protect and Allow Access for Plugins. Made Classs Itself public.
//         3.6 Parameterized Class Instances tableCards & tableTabHashMap to Bring Code
//             Into Compliance With Java 5.0 API. Cleaned Up Code for tableTabHashMap
//             in Class Methods getTableTabPanel(), getSelectedTableTabPanel() &
//             setSelectedTableTabPanel().
//         3.7 Parameterized Argument Instances tableNames in Constructor and Class
//             Method reloadPanel().
//                           
//-----------------------------------------------------------------
//                 danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *    The DbTablesPanel class provides the panel that holds all the
 * database tables panels, aka. TableTabPanels. In addition the class
 * also provides a common mechanism for getting those panels & other
 * information about the database tables.
 * 
 * @author Dana M. Proctor
 * @version 3.7 05/18/2010
 */

public class DBTablesPanel extends JPanel implements ActionListener
{
   // Class Instances
   private static final long serialVersionUID = 5906033083831415803L;
   
   private static final int tableTabPanelCardLimit = 10;
   private static LinkedList<String> tableCards = new LinkedList <String>();
   private static CardLayout tablesCardLayout = new CardLayout();
   private static JPanel tablesPanel = new JPanel(tablesCardLayout);
   
   private static JLabel statusIndicator = new JLabel("", JLabel.LEFT);
   private static JTextField statusLabel = new JTextField("Idle", 8);
   private static JComboBox tableSelectionComboBox = new JComboBox();;
   private static HashMap<String, TableTabPanel> tableTabHashMap = new HashMap <String, TableTabPanel>();
   private static boolean disableActions = false;
   private static long statusTimer;
   private volatile static boolean stopStatusDelayThread;
   
   private static DataImportProperties dataImportProperties = new DataImportProperties();
   private static DataExportProperties dataExportProperties = new DataExportProperties();

   //==============================================================
   // DBTablesPanel Constructor
   //==============================================================

   DBTablesPanel(Connection dbConnection, Vector<String> tableNames)
   {  
      // Constructor Instances
      ImageIcon statusIdleIcon, statusWorkingIcon;
      JPanel statusControlPanel, statusPanel;
      String iconsDirectory, tableName;

      // Initializing & setting up the panel.
      
      iconsDirectory = MyJSQLView_Utils.getIconsDirectory() + MyJSQLView_Utils.getFileSeparator();
      
      statusIdleIcon = new ImageIcon(iconsDirectory + "statusIdleIcon.png");
      statusWorkingIcon = new ImageIcon(iconsDirectory + "statusWorkingIcon.png");
      
      setLayout(new BorderLayout());
      
      // ===============================================
      // Create the status indicator and table selection
      // comboBox components. While at it load the first
      // table that is available.
      
      GridBagLayout gridbag = new GridBagLayout();
      GridBagConstraints constraints = new GridBagConstraints();
      
      statusControlPanel = new JPanel(gridbag);
      
      statusPanel = new JPanel(gridbag);
      statusPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLoweredBevelBorder(),
                                                               BorderFactory.createEmptyBorder(0, 2, 0, 1)));
      
      statusIndicator.setIcon(statusIdleIcon);
      statusIndicator.setDisabledIcon(statusWorkingIcon);
      statusIndicator.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 2));
      
      buildConstraints(constraints, 0, 0, 1, 1, 20, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.WEST;
      gridbag.setConstraints(statusIndicator, constraints);
      statusPanel.add(statusIndicator);
      
      statusLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
      statusLabel.setEditable(false);
      
      buildConstraints(constraints, 1, 0, 1, 1, 80, 100);
      constraints.fill = GridBagConstraints.HORIZONTAL;
      constraints.anchor = GridBagConstraints.WEST;
      gridbag.setConstraints(statusLabel, constraints);
      statusPanel.add(statusLabel);
      
      buildConstraints(constraints, 0, 0, 1, 1, 4, 100);
      constraints.fill = GridBagConstraints.BOTH;
      constraints.anchor = GridBagConstraints.WEST;
      gridbag.setConstraints(statusPanel, constraints);
      statusControlPanel.add(statusPanel);
      
      tableSelectionComboBox.setBorder(BorderFactory.createRaisedBevelBorder());
      tableSelectionComboBox.addActionListener(this);
      tableSelectionComboBox.setEnabled(false);
      
      if (!tableNames.isEmpty())
      {
         //tableSelectionComboBox.setEnabled(false);
         
         Iterator tableNamesIterator = tableNames.iterator();
         while (tableNamesIterator.hasNext())
            tableSelectionComboBox.addItem(tableNamesIterator.next());
         
         //tableSelectionComboBox.addActionListener(this);
         
         // Create the summary table of the first table in the
         // tables list of the database. All others are loaded
         // on the fly as needed.

         tableName = (String) tableSelectionComboBox.getItemAt(0);
         loadTable(tableName, dbConnection);
         
         tableSelectionComboBox.setEnabled(true);
      }
      
      buildConstraints(constraints, 1, 0, 1, 1, 96, 100);
      constraints.fill = GridBagConstraints.HORIZONTAL;
      constraints.anchor = GridBagConstraints.WEST;
      gridbag.setConstraints(tableSelectionComboBox, constraints);
      statusControlPanel.add(tableSelectionComboBox);
      
      add(statusControlPanel, BorderLayout.NORTH);
      
      // ===================================================
      // Add center panel that holds the TableTabPanel(s).
      
      add(tablesPanel, BorderLayout.CENTER);
   }

   //==============================================================
   // ActionEvent Listener method for detecting the inputs from the
   // panel and directing to the appropriate routine.
   //==============================================================

   public void actionPerformed(ActionEvent evt)
   {
      Object panelSource = evt.getSource();
     
      // Setting current Panel card to the selected table.

      if (panelSource instanceof JComboBox && !disableActions)
      {
         String tableName;
          
         if (panelSource == tableSelectionComboBox)
         {
            tableName = (String) tableSelectionComboBox.getSelectedItem();
            
            // If table already loaded just show else
            // have to load it via a background thread.
            // Provide the status indicator update also.
            
            if (tableCards.contains(tableName))
               tablesCardLayout.show(tablesPanel, tableName);
            else
            {
               Thread loadTableThread = new Thread(new Runnable()
               {
                  public void run()
                  {
                     Connection work_dbConnection = MyJSQLView_Access.getConnection("DBTablesPanel actionPerformed()");
                     String tableName = (String) tableSelectionComboBox.getSelectedItem();
                     
                     if (work_dbConnection == null)
                        return;
                     
                     startStatusTimer();
                     
                     loadTable(tableName, work_dbConnection);
                     tablesCardLayout.show(tablesPanel, tableName);
                     
                     MyJSQLView_Access.closeConnection(work_dbConnection, "DBTablesPanel actionPerformed()");
                     stopStatusTimer();
                  }
               }, "DBTablesPanel.loadTableThread");
               loadTableThread.start();  
            } 
         }
      }
   }
   
   //==============================================================
   // Class Method for helping the parameters in gridbag.
   //==============================================================

   private void buildConstraints(GridBagConstraints gbc, int gx, int gy, int gw, int gh, double wx, double wy)
   {
      gbc.gridx = gx;
      gbc.gridy = gy;
      gbc.gridwidth = gw;
      gbc.gridheight = gh;
      gbc.weightx = wx;
      gbc.weighty = wy;
   }

   //==============================================================
   // Class Method to load a new table into the panel's cardlayout.
   //==============================================================

   private static void loadTable(String tableName, Connection dbConnection)
   {
      // Method Instances
      TableTabPanel tableTabPanel;

      // MySQL
      if (MyJSQLView_Access.getSubProtocol().equals("mysql"))
         tableTabPanel = new TableTabPanel_MySQL(tableName, dbConnection);
      // PostgreSQL
      else if (MyJSQLView_Access.getSubProtocol().equals("postgresql"))
         tableTabPanel = new TableTabPanel_PostgreSQL(tableName, dbConnection);
      // HSQL
      else if (MyJSQLView_Access.getSubProtocol().indexOf("hsql") != -1)
         tableTabPanel = new TableTabPanel_HSQL(tableName, dbConnection);
      // Oracle
      else if (MyJSQLView_Access.getSubProtocol().indexOf("oracle") != -1)
         tableTabPanel = new TableTabPanel_Oracle(tableName, dbConnection);
      // Generic
      else
         tableTabPanel = new TableTabPanel_Generic(tableName, dbConnection);

      if (tableTabPanel.getTableFields() != null)
      {
         tableTabPanel.addMouseListener(MyJSQLView.getPopupMenuListener());
         tablesPanel.add(tableName, tableTabPanel);
         tableTabHashMap.put(tableName, tableTabPanel);
         tableCards.addFirst(tableName);
         
         // Control the number of cards that are added to the panel.
         if (tableCards.size() > tableTabPanelCardLimit)
         {
            TableTabPanel tableTabPanelToRemove;
            String tableNametoRemove;
            
            tableNametoRemove = tableCards.removeLast();
            tableTabPanelToRemove = tableTabHashMap.get(tableNametoRemove);
            
            if (tableTabPanelToRemove != null)
            {
               tablesPanel.remove(tableTabPanelToRemove);
               tablesPanel.validate();
            }
            tableTabHashMap.remove(tableNametoRemove);
         }
      }
   }
   
   //==============================================================
   // Class Method to reset the panel's table selector comboBox
   // and table cards then reload tables.
   //==============================================================
   
   protected static void reloadPanel(Connection dbConnection, Vector<String> tableNames)
   {
      // Method Instances
      String tableName;
      
      // Insure no actions are taken during reload.
      tableSelectionComboBox.setEnabled(false);
      disableActions = true;
      
      // Clear the components of old data.
      statusLabel.setEnabled(true);
      tableSelectionComboBox.removeAllItems();
      tableTabHashMap.clear();
      tableCards.clear();
      tablesPanel.removeAll();
      tablesPanel.setLayout(tablesCardLayout);
       
      // Try reloading tables.
      if (!tableNames.isEmpty())
      {
         Iterator tableNamesIterator = tableNames.iterator();
         while (tableNamesIterator.hasNext())
            tableSelectionComboBox.addItem(tableNamesIterator.next());
         
         tableSelectionComboBox.setEnabled(true);
          
         // Create the summary table of the first table in the
         // tables list of the database. All others are loaded
         // on the fly as needed.

         tableName = (String) tableSelectionComboBox.getItemAt(0);
         loadTable(tableName, dbConnection);
      } 
      disableActions = false;
   }
   
   //==============================================================
   // Class Method to start the status label timer, statusLabel set
   // to working.
   //==============================================================

   protected static void startStatusTimer()
   {
      // Method Instances
      java.util.Date startDate;
      
      // Collect and create start timer data.
      startDate = new java.util.Date();
      statusTimer = startDate.getTime();
      
      // Set status.
      stopStatusDelayThread = true;
      statusIndicator.setEnabled(false);
      statusLabel.setText("Working");
   }
   
   //==============================================================
   // Class Method to stop the status label timer, statusLabel set
   // to time difference between start and stop, then to Idle after
   // 3 seconds.
   //==============================================================

   protected static void stopStatusTimer()
   {
      // Method Instances
      java.util.Date stopDate;
      String statusLabelString;
      
      // Collect and create timer data.
      stopDate = new java.util.Date();
      statusTimer = (stopDate.getTime() - statusTimer);
      
      statusLabelString = MyJSQLView_Utils.nDigitChop((statusTimer * 0.001), 2) + " Seconds";
      
      if (statusLabelString.length() > 13)
         statusLabelString = statusLabelString.substring(0, 12);
         
      // Set status.
      statusIndicator.setEnabled(true);
      statusLabel.setText(statusLabelString);
         
      // Display the time for 3 seconds then set
      // label to Idle.
      Thread statusDelayThread = new Thread(new Runnable()
      {
         long timeSlice, totalElaspedTime, totalDelay; //milliseconds
         
         public void run()
         {
            timeSlice = 500;
            totalDelay = 2500;
            totalElaspedTime = 0;
            stopStatusDelayThread = false;
            
            try
            {
               while (totalElaspedTime <= totalDelay)
               {
                  if (!stopStatusDelayThread)
                  {
                     Thread.sleep(timeSlice);
                     totalElaspedTime += timeSlice;
                  }
                  else
                     return;
               }
               statusLabel.setText("Idle");
            }
            catch (InterruptedException e) {}
         }
      }, "DBTablesPanel.statusDelayThread");
      statusDelayThread.start();
   }
   
   //==============================================================
   // Class Method to return the current number of tables in the
   // database that the user has access to.
   //==============================================================

   public static int getTableCount()
   {
      if (tableSelectionComboBox.getItemCount() == 0)
         return 0;
      else
         return tableSelectionComboBox.getItemCount();
   }

   //==============================================================
   // Class Method to return the current selected visible
   // TableTabPanel in the panel.
   //==============================================================

   public static TableTabPanel getSelectedTableTabPanel()
   {  
      TableTabPanel selectedTableTabPanel;
      
	   // Insure the DB Panel is not empty or null selection.
      
      selectedTableTabPanel = tableTabHashMap.get(tableSelectionComboBox.getSelectedItem());
      
      if (tableSelectionComboBox.getItemCount() != 0  && selectedTableTabPanel != null)
         return selectedTableTabPanel;
      else
         return null;
   }
   
   //==============================================================
   // Class Method to set a TableTabPanel given the table tab name,
   // aka table name. Load it if necessary via selecting by the
   // tableSelectionComboBox.
   //==============================================================

   public static void setSelectedTableTabPanel(String tableName)
   {
      // Method Instances.
      boolean validTable;
      
      // Check to insure the table exists in the loaded tables.
      validTable = false;
      
      for (int i=0; i<tableSelectionComboBox.getItemCount(); i++)
         if (tableSelectionComboBox.getItemAt(i).equals(tableName))
            validTable = true;
      
      // Show that TableTabPanel 
      if (validTable)
      {
         // Table not loaded so load it.
         if (tableTabHashMap.get(tableName) == null)
         {
            String connectionString = "DBTablesPanel setSelectedTableTabPanel()";
            Connection work_dbConnection = MyJSQLView_Access.getConnection(connectionString);
            
            loadTable(tableName, work_dbConnection);
            
            MyJSQLView_Access.closeConnection(work_dbConnection, connectionString);
         }
         
         if (tableTabHashMap.get(tableName) == null)
            return;
         else
            tableSelectionComboBox.setSelectedItem(tableName);
      }
   }

   //==============================================================
   // Class Method to get a TableTabPanel given the table
   // tab name, aka table name. Load it if necessary via selecting
   // by the tableSelectionComboBox.
   //==============================================================
   
   public static TableTabPanel getTableTabPanel(String tableName)
   {
      // Table not loaded so load it.
      if (tableTabHashMap.get(tableName) == null)
      {
         Connection work_dbConnection = MyJSQLView_Access.getConnection("DBTablesPanel getTableTabPanel()");
         
         loadTable(tableName, work_dbConnection);
         
         MyJSQLView_Access.closeConnection(work_dbConnection, "DBTablesPanel getTableTabPanel()");
      }

      if (tableTabHashMap.get(tableName) == null)
         return null;
      else
         return tableTabHashMap.get(tableName);
   }
   
   //==============================================================
   // Class Method to return the current DataImportProperties.
   //==============================================================

   protected static DataImportProperties getDataImportProperties()
   {
      return dataImportProperties;
   }

   //==============================================================
   // Class Method to return the current DataExportProperties.
   //==============================================================

   protected static DataExportProperties getDataExportProperties()
   {
      return dataExportProperties;
   }
   
   //==============================================================
   // Class Method to set the DataImportProperties.
   //==============================================================

   protected static void setDataImportProperties(DataImportProperties newDataProperties)
   {
      dataImportProperties = newDataProperties;
   }

   //==============================================================
   // Class Method to set the DataExportProperties.
   //==============================================================

   protected static void setDataExportProperties(DataExportProperties newDataProperties)
   {
      dataExportProperties = newDataProperties;
   }
}