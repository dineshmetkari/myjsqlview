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
// Version 4.1 05/04/2010
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
//         3.7 04/28/2010 Added Class Instance defaultMenuBar, and Its Instantiation in
//                        Method createGUI() Along With loadedMenuBars. Control of MyJSQLView_Frame's
//                        MenuBar via the stateChanged() Method. Additional Aspect Collection
//                        of Each Plugin & Initialization in Class Method createGUI().
//         3.8 04/29/2010 Implemented Module ToolBars and Selection Of Tab Icon for Modules.
//                        Added Class Instances toolBarPanel, toolBarCardLayout, & defaultToolBar.
//                        Class Methods Effected createGUI() and stateChanged().
//         3.9 04/29/2010 Reorganized Slightly, Class Method createGUI().
//         4.0 05/03/2010 Made the Class Public So That Plugins Gain Access to the ActionListener.
//         4.1 05/04/2010 Backed Out 4.0.
//
//-----------------------------------------------------------------
//                 danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Image;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Vector;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Map;
import java.security.*;
import java.net.MalformedURLException;
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
 * @version 4.1 05/04/2010
 */

class MyJSQLView_Frame extends JFrame implements ActionListener, ChangeListener
{
   // Class Instances.
   private static final long serialVersionUID = -5105256432038108191L;

   private String[] myJSQLView_Version;
   private String webSiteString;
   private static ImageIcon databaseTablesIcon, pluginIcon;
   private String fileSeparator;
   
   private Default_JMenuBar defaultMenuBar;
   private MyJSQLView_JMenuBar myJSQLViewMenuBar;
   private JPanel toolBarPanel;
   private CardLayout toolBarCardLayout;
   private Default_JToolBar defaultToolBar;
   private MyJSQLView_JToolBar myJSQLViewToolBar;
   private TopTabPanel mainTabPanel;
   private static JTabbedPane mainTabsPane;
   private static DBTablesPanel dbTablesPanel;
   
   private static Vector loadedPluginModules = new Vector();
   private static Vector loadedMenuBars = new Vector();
   
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
      
      // Creating MyJSQLView GUI.
      createGUI();
   }

   //==============================================================
   // Class method to setup the MyJSQlView user interface, tabbed
   // pane.
   //==============================================================

   private void createGUI()
   {
      // Class Instances
      JPanel mainPanel;
      
      String iconsDirectory;
      ImageIcon mainTabIcon;
      PluginLoader pluginLoader;
      HashMap pluginsHashMap;
      String currentModuleName;
      JPanel currentModuleMainPanel;
      ImageIcon currentModuleIcon;
      
      // Setting up Various Instances.
      
      fileSeparator = MyJSQLView_Utils.getFileSeparator();
      iconsDirectory = MyJSQLView_Utils.getIconsDirectory() + fileSeparator;;
      
      // Obtain & create Image Icons.
      
      mainTabIcon = new ImageIcon(iconsDirectory + "mainTabIcon.png");
      databaseTablesIcon = new ImageIcon(iconsDirectory + "databasetablesIcon.png");
      pluginIcon = new ImageIcon(iconsDirectory + "newsiteLeafIcon.png");
      
      // Setup the menu bar for the frame.
      
      defaultMenuBar = new Default_JMenuBar(this);
      loadedMenuBars.add(defaultMenuBar);
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
      loadedMenuBars.add(myJSQLViewMenuBar);
      
      myJSQLViewToolBar = new MyJSQLView_JToolBar(this, "MyJSQLView ToolBar");
      toolBarPanel.add("1", myJSQLViewToolBar);
      
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
      // Plugins' Tabs.
      
      pluginLoader = new PluginLoader();
      pluginsHashMap = pluginLoader.getPluginsHashMap();
      
      if (pluginsHashMap != null && !pluginsHashMap.isEmpty())
      {
         Set keySet = pluginsHashMap.entrySet();
         Iterator pluginIterator = keySet.iterator();
         
         while (pluginIterator.hasNext())
         {
            Map.Entry pluginEntry = (Map.Entry) pluginIterator.next();
            try
            {
               final File jarFile = new File((String) pluginEntry.getKey());
               ClassLoader classLoader = (ClassLoader) AccessController.doPrivileged(new PrivilegedAction()
               {
                  public Object run()
                  {
                     try
                     {
                        return new URLClassLoader(new URL[] {jarFile.toURI().toURL()},
                                                  ClassLoader.getSystemClassLoader());
                     }
                     catch (MalformedURLException me)
                     {
                        if (MyJSQLView.getDebug())
                           System.out.println("MyJSQLView_Frame createGUI() URLClassLoader: \n"
                                               + me.toString());
                        return null;
                     }
                  }
               });
               
               // If looks like a good plugin load it.
               
               if (classLoader != null)
               {
                  // Create the instance.
                  Class module = Class.forName((String) pluginEntry.getValue(), true, classLoader);
                  MyJSQLView_PluginModule pluginModule = (MyJSQLView_PluginModule) module.newInstance();
                  pluginModule.run();
                 
                  // Collect the required information needed
                  // by MyJSQLView and add to tabbed pane.
                  
                  // Name
                  currentModuleName = pluginModule.getName();
                  if (currentModuleName == null)
                     currentModuleName = "";
                  else if (currentModuleName.length() > 50)
                     currentModuleName = currentModuleName.substring(0, 49);
                  
                  // Main Panel
                  currentModuleMainPanel = pluginModule.getPanel();
                  if (currentModuleMainPanel == null)
                     currentModuleMainPanel = new JPanel();
                  
                  // Tab Icon
                  currentModuleIcon = pluginModule.getTabIcon();
                  if (currentModuleIcon == null)
                     currentModuleIcon = pluginIcon;
                  else
                     currentModuleIcon = new ImageIcon(currentModuleIcon.getImage().getScaledInstance(12,
                                                                         12, Image.SCALE_FAST));
                  
                  mainTabsPane.addTab(null, currentModuleIcon, currentModuleMainPanel, currentModuleName);
                  
                  // Store plugin's aspects.
                  loadedPluginModules.add(pluginModule);
                  loadedMenuBars.add(pluginModule.getMenuBar());
                  
                  if (pluginModule.getToolBar() == null)
                     toolBarPanel.add((Integer.toString(mainTabsPane.getTabCount() - 1)),
                                       new Default_JToolBar(""));
                  else
                     toolBarPanel.add((Integer.toString(mainTabsPane.getTabCount() - 1)),
                                       pluginModule.getToolBar());  
               }
            }
            catch (Exception e)
            {
               if (MyJSQLView.getDebug())
                  System.out.println("MyJSQLView_Frame createGUI() Exception: \n" + e.toString());
            }
         }  
      }
      
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
      JMenuBar selectedMenuBar;
      
      Object changeSource = evt.getSource();
      
      if (changeSource != null && (JTabbedPane) changeSource == mainTabsPane)
      {
         // Collect some parameters to be used.
         
         selectedIndex = mainTabsPane.getSelectedIndex();
         
         // The top mainTabPanel is a runnable thread so
         // control the animation.
         
         if (selectedIndex == 0)
         {
            mainTabPanel.resetPanel();
            mainTabPanel.setThreadAction(false); 
         }
         else
            mainTabPanel.setThreadAction(true);
         
         // Set the menubar & toolbar required by the tab.
         
         if (selectedIndex > (loadedMenuBars.size() - 1))
            selectedMenuBar = null;
         else
            selectedMenuBar = (JMenuBar) loadedMenuBars.get(selectedIndex);
         
         if (selectedMenuBar != null)
            setJMenuBar(selectedMenuBar);
         else
            setJMenuBar(defaultMenuBar);
         
         toolBarCardLayout.show(toolBarPanel, Integer.toString(selectedIndex));
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
      Iterator pluginModulesIterator;
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
         
         // Reload Plugins' Tables.
         pluginModulesIterator = loadedPluginModules.iterator();
         while (pluginModulesIterator.hasNext())
         {
            MyJSQLView_PluginModule currentPlugin = (MyJSQLView_PluginModule) pluginModulesIterator.next();
            currentPlugin.setDBTables(tableNames);
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