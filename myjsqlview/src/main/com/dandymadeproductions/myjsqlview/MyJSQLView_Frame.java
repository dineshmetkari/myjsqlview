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
// Version 5.3 06/11/2010
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
//         1.1 11/01/2008 Added Class Method reloadDBTables(). Also Added ImageIcons
//                        databaseTablesIcon, & pluginsIcons.
//         1.2 11/03/2008 Class Method reloadDBTables() Added Instance 
//                        currentSelectedTable.
//         1.3 11/08/2008 Removed Class Instances dataImport/ExportProperties &
//                        Getter/Setter Methods. Moved Down to the DBTablesPanel.
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
//         3.1 02/27/2010 Class Method createGUI(), DatabaseTableThread Addition of
//                        resourceBundle and resource Instances to Handle Internationalization.
//         3.2 03/22/2010 Moved the Creation of the Main Tab, DMP, to a New Class TopTabPanel.
//                        Created Class Instance mainTabPanel, removed Setting up That
//                        Panel TopTabPanel. Added ChangeListener to mainTabsPane &
//                        Suspended mainTabPanel as Needed to Stop Thread. Removed Method
//                        Instances mainTabImageFileName, calendar, and timeOfDay from
//                        createGUI().
//         3.3 03/25/2010 Class Method stateChanged() mainTabPanel.resetPanel(). Hook for
//                        Future Possible Password Protection for Idle Application.
//         3.4 04/20/2010 Commented Plugin Loading, tableFieldChartsPanel & pluginsIcon to
//                        Work on Plugin Framework.
//         3.5 04/23/2010 Initial Basic Working Framework for Plugins, Class Method
//                        createGUI().
//         3.6 04/25/2010 Added Class Instance loadedPluginModules. Loaded in createGUI.
//                        Added to Class Method reloadDBTables() To Allow Plugins to Be
//                        Updated. Completed Initial Plugin Framework Loading Process in
//                        Class Method createGUI().
//         3.7 04/28/2010 Added Class Instance defaultMenuBar, and Its Instantiation
//                        in Method createGUI() Along With loadedMenuBars. Control of
//                        MyJSQLView_Frame's MenuBar via the stateChanged() Method.
//                        Additional Aspect Collection of Each Plugin & Initialization
//                        in Class Method createGUI().
//         3.8 04/29/2010 Implemented Module ToolBars and Selection Of Tab Icon for
//                        Modules. Added Class Instances toolBarPanel, toolBarCardLayout,
//                        & defaultToolBar. Class Methods Effected createGUI() and
//                        stateChanged().
//         3.9 04/29/2010 Reorganized Slightly, Class Method createGUI().
//         4.0 05/03/2010 Made the Class Public So That Plugins Gain Access to the
//                        ActionListener.
//         4.1 05/04/2010 Backed Out 4.0.
//         4.2 05/06/2010 What Else Put 4.0 Back In, Made Class Public and Passed this
//                        to the Plugins via pluginModule.setParentFrame() in Class
//                        Method createGUI().
//         4.3 05/07/2010 Changed pluginModule.run() to pluginModule.initPlugin() in
//                        Class Method createGUI().
//         4.4 05/08/2010 Changed pluginModule.initPlugin() To Have Argument this in
//                        Class Method createGUI(). Removed setParentFrame() is Same.
//         4.5 05/19/2010 Parameterized Class Instances loadedPluginModules, &
//                        loadedMenuBars in Order to Bring Code Into Compliance With
//                        Java 5.0 API. Also Same for tableNames in Method reloadDBTables()
//                        & pluginsHashMap in createGUI().
//         4.6 05/20/2010 Parameterized pluginModulesIterator in reloadDBTable(). Also
//                        keySet, pluginIterator, & pluginEntry in Method createGUI().
//         4.7 05/23/2010 Insured in Class Method createGUI That dbTablesPanel is
//                        Loaded Into mainTabsPane at Index 1 by Inserting A Tempoary
//                        Dummy JLabel Component. Then Loaded Aspect of That Tab in
//                        the databaseTablesThread.
//         4.8 06/06/2010 Removed Class Method loadedMenuBars & pluginIcon. Also Removed
//                        Method Instances in createGUI() currentModule/Name/Panel/Icon.
//                        ReOrganized Instances and Moved Plugin Loading Completely to
//                        PluginLoader Class. Changes in actionPerformed() to Handle the
//                        Storage of All Plugin Characteristics in PluginModule Classes.
//         4.9 06/07/2010 Class Method createGUI() Changed the Way Plugin Module Aspects
//                        Are Stored, by Instance Instead of Getter Method.
//         5.0 06/07/2010 Added Class Method addTab(). Made Class Instance toolBarPanel
//                        static. Removed Instance pluginModulesIterator From Class
//                        Method createGUI() Thereby Moving Plugin Additions to addTab().
//         5.1 06/09/2010 Removed Instance tableNames in Class Method reloadDBTables().
//         5.2 06/11/2010 Check in stateChanged() to Insure the selectedIndex Derived is Not
//                        Larger Than the Existing Tab Count.
//         5.3 06/11/2010 Changed the Derivation of the selectedIndex From mainTabsPane to
//                        changeSource to Again Try to Correct the ArrayIndex Out of Range
//                        Errors. 
//
//-----------------------------------------------------------------
//                 danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Vector;
import java.util.Iterator;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *    The MyJSQLView_Frame class is used to setup the main user interface
 * frame for the MyJSQLView application. The class provides the basis for
 * the overall application's general look and feel in addition to plugin
 * creation and inclusion.
 * 
 * @author Dana M. Proctor
 * @version 5.3 06/11/2010
 */

public class MyJSQLView_Frame extends JFrame implements ActionListener, ChangeListener
{
   // Class Instances.
   private static final long serialVersionUID = -5105256432038108191L;

   private String[] myJSQLView_Version;
   private String webSiteString;
   private static ImageIcon databaseTablesIcon;
   private Default_JMenuBar defaultMenuBar;
   private MyJSQLView_JMenuBar myJSQLViewMenuBar;
   private static JPanel toolBarPanel;
   private CardLayout toolBarCardLayout;
   
   private TopTabPanel mainTabPanel;
   private static JTabbedPane mainTabsPane;
   private static DBTablesPanel dbTablesPanel;
   
   private static Vector<MyJSQLView_PluginModule> loadedPluginModules = 
                                                  new Vector <MyJSQLView_PluginModule>();
   
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
      String fileSeparator, iconsDirectory;
      ImageIcon mainTabIcon;
      Default_JToolBar defaultToolBar;
      MyJSQLView_JToolBar myJSQLViewToolBar;
      
      // Setting up Various Instances.
      
      fileSeparator = MyJSQLView_Utils.getFileSeparator();
      iconsDirectory = MyJSQLView_Utils.getIconsDirectory() + fileSeparator;;
      
      // Obtain & create Image Icons.
      
      mainTabIcon = new ImageIcon(iconsDirectory + "mainTabIcon.png");
      databaseTablesIcon = new ImageIcon(iconsDirectory + "databasetablesIcon.png");
      
      // Setup the menu bar for the frame.
      
      defaultMenuBar = new Default_JMenuBar(this);
      setJMenuBar(defaultMenuBar);
      
      // ===============================================
      // Setting up the tabbed pane with the various
      // panels that provide the functionality of the
      // MyJSQLView application and its plugins.
      // ===============================================
      
      mainPanel = new JPanel(new BorderLayout());
      
      // Toolbar
      toolBarCardLayout = new CardLayout();
      toolBarPanel = new JPanel(toolBarCardLayout);
      defaultToolBar = new Default_JToolBar("Default ToolBar");
      toolBarPanel.add("0", defaultToolBar);
      mainPanel.add(toolBarPanel, BorderLayout.PAGE_START);

      // Central Area
      mainTabsPane = new JTabbedPane();
      mainTabsPane.setTabPlacement(JTabbedPane.RIGHT);
      mainTabsPane.setBorder(BorderFactory.createLoweredBevelBorder());
      mainTabsPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

      //=========================================
      // Standard dmp Main Tab
      
      mainTabPanel = new TopTabPanel();
      mainTabsPane.addTab(null, mainTabIcon, mainTabPanel, "Dandy Made Productions");   
      
      //=========================================
      // Standard Database Tables Tab
      
      myJSQLViewMenuBar = new MyJSQLView_JMenuBar(this);
      
      myJSQLViewToolBar = new MyJSQLView_JToolBar(this, "MyJSQLView ToolBar");
      toolBarPanel.add("1", myJSQLViewToolBar);
      
      // Insure DBTablesPanel to be at index 1.
      mainTabsPane.add(new JLabel(""), 1);
      
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
            {
               mainTabsPane.setIconAt(1, databaseTablesIcon);
               mainTabsPane.setComponentAt(1, dbTablesPanel);
               mainTabsPane.setToolTipTextAt(1, "Database Tables");
            }
            else
            {
               mainTabsPane.setIconAt(1, databaseTablesIcon);
               mainTabsPane.setComponentAt(1, dbTablesPanel);
               mainTabsPane.setToolTipTextAt(1, resource);
            }
            
            // Closing the database connection that is used
            // during the inital setup of the application.
            
            MyJSQLView_Access.closeConnection(dbConnection, "MyJSQLView_Frame createGUI()");
         }
      }, "MyJSQLView_Frame.createGUI(), databaseTablesThread");
      databaseTablesThread.start();
      
      //=========================================
      // Plugins' Tabs.
      
      new PluginLoader(this);
      
      //=========================================
      // Finishing up.
      
      mainTabsPane.addChangeListener(this);
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
      // Method Instances.
      int selectedIndex;
      
      Object changeSource = evt.getSource();
      
      if (changeSource != null && (JTabbedPane) changeSource == mainTabsPane)
      {
         // Collect some parameters to be used.
         
         selectedIndex = ((JTabbedPane) changeSource).getSelectedIndex();
         
         // The top mainTabPanel is a runnable thread so
         // control the animation.
         
         if (selectedIndex == 0)
         {
            mainTabPanel.resetPanel();
            mainTabPanel.setThreadAction(false); 
         }
         else
            mainTabPanel.setThreadAction(true);
         
         // Set the MenuBar required by the tab.
         
         // Top Panel
         if (selectedIndex == 0)
            setJMenuBar(defaultMenuBar);
         // DBTables Panel
         else if (selectedIndex == 1)
            setJMenuBar(myJSQLViewMenuBar);
         // Plugin Panel
         else
         {
            if ((selectedIndex - 2) <= loadedPluginModules.size())
               setJMenuBar((loadedPluginModules.get(selectedIndex - 2)).menuBar);
         }
         
         // Set the ToolBar required by the tab.
         
         toolBarCardLayout.show(toolBarPanel, Integer.toString(selectedIndex));
      }
   }
   
   //==============================================================
   // Class Method to add a new plugin tab to the frame interface.
   //==============================================================
   
   protected static synchronized void addTab(MyJSQLView_PluginModule plugin)
   {
      if (plugin != null)
      {
         loadedPluginModules.add(plugin);
         mainTabsPane.addTab(null, plugin.tabIcon, plugin.panel, plugin.name);
         toolBarPanel.add((Integer.toString(mainTabsPane.getTabCount() - 1)), plugin.toolBar);
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
      Iterator<MyJSQLView_PluginModule> pluginModulesIterator;
      
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
         
         DBTablesPanel.reloadPanel(dbConnection, MyJSQLView_Access.getTableNames());
         dbTablesPanel.repaint();
         
         // Reload Plugins' Tables.
         pluginModulesIterator = loadedPluginModules.iterator();
         while (pluginModulesIterator.hasNext())
         {
            MyJSQLView_PluginModule currentPlugin = pluginModulesIterator.next();
            currentPlugin.setDBTables(MyJSQLView_Access.getTableNames());
         }
         
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