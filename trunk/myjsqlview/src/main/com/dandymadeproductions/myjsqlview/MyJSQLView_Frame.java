//=================================================================
//                   MyJSQLView_Frame Class
//=================================================================
// 
//    This class is used to setup the main user interface frame
// for the MyJSQLView application. The class provides the basis
// for the overall application's general look and feel in addition
// to plugin creation and inclusion.
//
//                   << MyJSQLView_Frame.java >>
//
//=================================================================
// Copyright (C) 2005-2010 Dana M. Proctor
// Version 3.1 02/27/2010
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
// Version 1.0 10/30/2008 Initial MyJSQLView_Frame Class.
//         1.1 11/01/2008 Added Class Method reloadDBTables(). Also Added
//                        ImageIcons databaseTablesIcon, & pluginsIcons.
//         1.2 11/03/2008 Class Method reloadDBTables() Added Instance 
//                        currentSelectedTable.
//         1.3 11/08/2008 Removed Class Instances dataImport/ExportProperties
//                        & Getter/Setter Methods. Moved Down to the
//                        DBTablesPanel.
//         1,4 11/13/2008 Added Conditional Check for NULL dbConnection in Class
//                        Method reloadDBTables().
//         1.5 11/17/2008 Class Method reloadDBTables() Cleared the DBTablesPanel
//                        .tableSelectionComboBox() Before Creating a new
//                        DBTablesPanel.
//         1.6 11/17/2008 Check for a NULL TableTabPanel Being Returned for the
//                        Instance currentSelectedTable Setting in reloadDBTable().
//         1.7 11/24/2008 Made Class Instance dbTablesPanel static and Only Instantiated
//                        Once. Changed the Access in reloadDBTables() to Just
//                        reloadPanel().
//         1.8 11/26/2008 Class Method reloadDBTables() Eliminated the Removal of
//                        the DBTablesPanel From Tab Index 0 and Just validated
//                        TabPane After.
//         1.9 12/08/2008 Added Instance mainPanel to House the MyJSQLView_JToolBar.
//         2.0 12/24/2008 Added Constructor Instances pluginPanel, and tempPluginLabel.
//         2.1 05/27/2009 Header Format Changes/Update.
//         2.2 08/31/2009 Repainted the dbTablesPanel on reloadDBTables().
//         2.3 09/30/2009 Threaded Tabs, in Class Method createGUI. Moved fileSeparator
//                        to Class Instance.
//         2.4 10/07/2009 Added mainTabThread in Class Method createGUI(). Implemented
//                        ChangeListener, Through stateChanged() Class Method.
//         2.5 10/14/2009 Removed mainTabThread in Class Method createGUI() Added Instances
//                        mainTabImageFileName, calendar, & timeOfDay to Same Method.
//                        Set mainTabLabel Image by Time of Day.
//         2.6 10/24/2009 Class Instance fileSeparator & Constructor Instance iconsDirectory
//                        Obtained From MyJSQLView_Utils Class.
//         2.7 10/25/2009 Added fileSeparator to iconsDirectory.
//         2.8 11/08/2009 Added Comments and Commented Out In Constructor, For Now,
//                        mainTabsPane.addChangeListener().
//         2.9 11/15/2009 Replaced Graphic Filler for the Plugin Tab, With TableFieldChartsPanel
//                        in Constructor. Also Reloaded Same in Class Method reloadDBTables().
//         3.0 02/18/2010 Changed Package to Reflect Dandy Made Productions Code.
//         3.1 02/27/2010 Class Method createGUI(), DatabaseTableThread Addition of resourceBundle
//                        and resource Instances to Handle Internationalization.
//
//-----------------------------------------------------------------
//                 danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview;

import java.sql.*;
import java.util.Calendar;
import java.util.Vector;
import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.event.*;
import javax.swing.event.*;

/**
 *    The MyJSQLView_Frame class is used to setup the main user interface
 * frame for the MyJSQLView application. The class provides the basis for
 * the overall application's general look and feel in addition to plugin
 * creation and inclusion.
 * 
 * @author Dana M. Proctor
 * @version 3.1 02/27/2010
 */

class MyJSQLView_Frame extends JFrame implements ActionListener, ChangeListener
{
   // Class Instances.
   private static final long serialVersionUID = -5105256432038108191L;

   private String[] myJSQLView_Version;
   private String webSiteString;
   private MyJSQLView_JToolBar myJSQLViewToolBar;
   
   private static ImageIcon mainTabIcon, databaseTablesIcon, pluginsIcons;
   private static JTabbedPane mainTabsPane;
   private static DBTablesPanel dbTablesPanel;
   private static TableFieldChartsPanel tableFieldChartsPanel;
   private String fileSeparator;
   
   //==============================================================
   // MyJSQLView_Frame Constructor
   //==============================================================

   protected MyJSQLView_Frame(String[] myJSQLView_Version, String webSiteString)
   {
      // Displaying title and assigning instance associations.

      super("MyJSQLView   " + MyJSQLView_Access.getHostName() + ":"
             + MyJSQLView_Access.getDBName());

      this.myJSQLView_Version = myJSQLView_Version;
      this.webSiteString = webSiteString;
   }

   //==============================================================
   // Class method to setup the MyJSQlView user interface, tabbed
   // pane.
   //==============================================================

   protected void createGUI()
   {
      // Class Instances
      JPanel mainPanel;
      String iconsDirectory;
      String mainTabImageFileName;
      Calendar calendar;
      int timeOfDay;

      // Setting up Various Instances.
      
      fileSeparator = MyJSQLView_Utils.getFileSeparator();
      iconsDirectory = MyJSQLView_Utils.getIconsDirectory() + fileSeparator;;
      
      // Obtain & create Image Icons.
      
      mainTabIcon = new ImageIcon(iconsDirectory + "mainTabIcon.png");
      databaseTablesIcon = new ImageIcon(iconsDirectory + "databasetablesIcon.png");
      pluginsIcons = new ImageIcon(iconsDirectory + "newsiteLeafIcon.png");

      // ===============================================
      // Setting up the tabbed pane with the various
      // panels that provide the functionality of the
      // MyJSQLView application.
      // ===============================================
      
      mainPanel = new JPanel(new BorderLayout());
      
      myJSQLViewToolBar = new MyJSQLView_JToolBar(this, "MyJSQLView ToolBar");
      mainPanel.add(myJSQLViewToolBar, BorderLayout.PAGE_START);

      mainTabsPane = new JTabbedPane();
      mainTabsPane.setTabPlacement(JTabbedPane.RIGHT);
      mainTabsPane.setBorder(BorderFactory.createLoweredBevelBorder());
      mainTabsPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

      // ===============================================
      // Fill the tabs with the default MyJSQlView
      // database tables tab and any other plugins.
      // ===============================================
      
      //=========================================
      // Standard dmp Main Tab
      
      JPanel mainTabPanel = new JPanel();
      
      calendar = Calendar.getInstance();
      timeOfDay = calendar.get(Calendar.HOUR_OF_DAY);
      
      // {8:00pm - 4:00am} Night
      if (timeOfDay >= 20 || timeOfDay <=4)
         mainTabImageFileName = "mainTab_night.jpg";  
      // {5:00am - 9:00am} Morning
      else if (timeOfDay >= 5  && timeOfDay <= 9)
         mainTabImageFileName = "mainTab_morning.jpg";
      // {10:00am - 4:00pm} Afternoon
      else if (timeOfDay >= 10  && timeOfDay <= 16)
         mainTabImageFileName = "mainTab_day.jpg";
      // {5:00pm - 7:00pm} Evening
      else
         mainTabImageFileName = "mainTab_evening.jpg";
      
      JLabel mainTabLabel = new JLabel(new ImageIcon("images" + fileSeparator
            + mainTabImageFileName));
      mainTabPanel.add(mainTabLabel);
      mainTabsPane.addTab(null, mainTabIcon, mainTabPanel, "Dandy Made Productions");     
      
      //=========================================
      // Database Tables Tab
      
      Thread databaseTablesThread = new Thread(new Runnable()
      {
         public void run()
         {
            // Thread Instances.
            Connection dbConnection;
            MyJSQLView_ResourceBundle resourceBundle;
            String resource;
            
            // Obtain a database connection & resources.
            
            dbConnection = MyJSQLView_Access.getConnection("MyJSQLView_Frame createGUI()");
            resourceBundle = MyJSQLView.getLocaleResourceBundle();
            
            dbTablesPanel = new DBTablesPanel(dbConnection, MyJSQLView_Access.getTableNames());
            
            resource = resourceBundle.getResource("MyJSQLView_Frame.tab.DatabaseTables");
            if (resource.equals(""))
               mainTabsPane.addTab(null, databaseTablesIcon, dbTablesPanel, "Database Tables");
            else
               mainTabsPane.addTab(null, databaseTablesIcon, dbTablesPanel, resource);
            
            // Closing the database connection that is used
            // during the inital setup of the application.
            
            MyJSQLView_Access.closeConnection(dbConnection, "MyJSQLView_Frame createGUI()");
         }
      }, "MyJSQLView_Frame.createGUI(), databaseTablesThread");
      databaseTablesThread.start();
      
      //=========================================
      // Plugins Tabs (Table Field Charts)
      
      Thread tempPluginThread = new Thread(new Runnable()
      {
         public void run()
         {
            tableFieldChartsPanel = new TableFieldChartsPanel(MyJSQLView_Access.getTableNames());
            mainTabsPane.addTab(null, pluginsIcons, tableFieldChartsPanel, "Field Charts");
         }
      }, "MyJSQLView_Frame.createGUI(), tempPluginThread");
      tempPluginThread.start();  
      
      //mainTabsPane.addChangeListener(this);
      mainPanel.add(mainTabsPane, BorderLayout.CENTER);
      getContentPane().add(mainPanel);
   }
   
   //==============================================================
   // ActionEvent Listener method for detecting the user's selection
   // of a Menu Bar item. Upon detection an action is then completed
   // by calling the MyJSQLView_JMenuBarActions class. Created to
   // reduce clutter in this top level class & consolidate actions.
   //==============================================================

   public void actionPerformed(ActionEvent evt)
   {
      MyJSQLView_JMenuBarActions.actionsSelection(this, evt, webSiteString, myJSQLView_Version);
   }
   
   //==============================================================
   // ChangeEvent Listener method for detecting the user's selection
   // of the various tabs that are loaded in the application.
   //==============================================================

   public void stateChanged(ChangeEvent evt)
   {
      Object changeSource = evt.getSource();
      
      if (changeSource != null && (JTabbedPane) changeSource == mainTabsPane)
      {
         // System.out.println(mainTabsPane.getSelectedIndex());
      }
   }
   
   //==============================================================
   // Class Method to reload the DBTablesPanel. Essentially the
   // panel is left intact, static, just components cleared/reset
   // then redisplayed.
   //==============================================================
   
   protected static void reloadDBTables()
   {
      // Method Instances
      Connection dbConnection;
      String currentSelectedTable;
      Vector tableNames;
      
      // Create a connection, load the database tables again
      // then resetup the DBTablesPanel.
      
      dbConnection = MyJSQLView_Access.getConnection("TableTabPanel_Frame reloadDBTables()");
      
      if (dbConnection == null)
         return;
      
      try
      {
         // Save the current selected table so that it may
         // be shown again, if possible, after the reload.
         
         if (DBTablesPanel.getSelectedTableTabPanel() != null)
            currentSelectedTable = DBTablesPanel.getSelectedTableTabPanel().getTableName();
         else
            currentSelectedTable = "";
         
         // Reload Database Tables.
         MyJSQLView_Access.loadDBTables(dbConnection);
         tableNames = MyJSQLView_Access.getTableNames();
         
         DBTablesPanel.reloadPanel(dbConnection, MyJSQLView_Access.getTableNames());
         dbTablesPanel.repaint();
         
         // Reload Table Field Charts.
         tableFieldChartsPanel.reloadPanel(dbConnection, tableNames);
         tableFieldChartsPanel.repaint();
         
         // Try set the table showing before the reload.
         
         if (!currentSelectedTable.equals(""))
            DBTablesPanel.setSelectedTableTabPanel(currentSelectedTable);
         
         mainTabsPane.validate();
      }
      catch (SQLException e)
      {
         MyJSQLView_Access.displaySQLErrors(e, "MyJSQLView_Frame reloadDBTables()");
      }
      
      MyJSQLView_Access.closeConnection(dbConnection, "TableTabPanel_Frame reloadDBTables()");
   }  
}