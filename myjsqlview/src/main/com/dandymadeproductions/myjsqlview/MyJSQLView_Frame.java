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
// Copyright (C) 2005-2012 Dana M. Proctor
// Version 6.9 01/11/2012
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
//         5.4 06/12/2010 Still Getting "AWT-EventQueue-0" ArrayIndexOutOfBoundsException:
//                        On Occasion With the Tabbed Pane. Conditional Check in stateChange()
//                        of selectedIndex Less Than Total Tab Count. Changed the Way the
//                        DBTablesPanel is Added to the mainTabPane in run().
//         5.5 06/16/2010 Class Method addTab() Added Argument MyJSQLView_Frame and Then Removed
//                        mainTabsPane ChangeListener During the Adding of a Tab. Still Trying
//                        to Fix the ArrayIndexOutOfBoundsException. It Appears That a MouseOver
//                        Event for the Tab is Trying to Change Aspects of the Tabs.
//         5.6 08/05/2010 Class Method createGUI() Added Instance topMenuMenu to Replace the
//                        defaultMenuBar for Plugin Management.
//         5.7 08/05/2010 Removed defaultMenuBar and Replaced All Aspects of Its Use to the
//                        Promoted Class Instance topMenuBar.
//         5.8 08/06/2010 Added Class Methods removeTab(), and getPlugins(). Changes to
//                        toolBarPanel of Assigning the Cards to the Plugin Name Instead of
//                        the Tab Index.
//         5.9 08/20/2010 Added Class Instance pluginFrameListenButton. Program Clicked When
//                        a Plugin Module Tab is Added, addTab().
//         6.0 09/15/2010 Class Method createGUI() Removed the dummyPanel Removal From the
//                        Main Tab.
//         6.1 01/15/2011 Class Methods createGUI() & reloadDBTables() Cast Object Returned by
//                        MyJSQLView_Access.getConnection() to Connection.
//         6.2 01/26/2011 Change in Obtaining host and db For Frame Title. Changes to Access
//                        Connections/Errors to the New Redefined Class ConnectionManager.
//         6.3 03/17/2011 Added Class Instance sqlQueryBucketFrame, and Passed as Argument in
//                        MyJSQLView_JMenuBarActions.actionsSelection() Called in actionPerformed().
//                        Added Class Method getSQLBucket().
//         6.4 04/07/2011 Class Instance sqlQueryBucketFrame Size Change and setResizable(false).
//         6.5 10/05/2011 Added Inner Class myjsqlviewFrameListener to Handle the Closing
//                        Event to Insure the SQLQueryBucket List is Saved.
//         6.6 10/05/2011 Threaded the Setup of the Query Bucket in the Constructor.
//         6.7 10/29/2011 Argument Change to Pass Database Name to openLastUsedList() for the
//                        SQLQueryBucketFrame in Constructor.
//         6.8 01/01/2012 Moved the Loading of the Query Bucket List From Constructor to its
//                        Own Method.
//         6.9 01/11/2012 Removed the Casting of (Connection) for the Returned Instance for the
//                        ConnectionManager.getConnection() in createGUI() & reloadDBTables().
//                        
//
//-----------------------------------------------------------------
//                 danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
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
 * @version 6.9 01/11/2012
 */

public class MyJSQLView_Frame extends JFrame implements ActionListener, ChangeListener
{
   // Class Instances.
   private static final long serialVersionUID = -5105256432038108191L;

   private String[] myJSQLView_Version;
   private String webSiteString;
   private static ImageIcon databaseTablesIcon;
   private static SQLQueryBucketFrame sqlQueryBucketFrame = new SQLQueryBucketFrame();
   private MyJSQLView_JMenuBar myJSQLViewMenuBar;
   private MyJSQLView_Top_JMenuBar topMenuBar;
   private static JPanel toolBarPanel;
   private CardLayout toolBarCardLayout;
   
   private TopTabPanel mainTabPanel;
   private static JTabbedPane mainTabsPane;
   private static DBTablesPanel dbTablesPanel;
   
   private static Vector<MyJSQLView_PluginModule> loadedPluginModules = 
                                                  new Vector <MyJSQLView_PluginModule>();
   protected static final JButton pluginFrameListenButton = new JButton();
   
   //==============================================================
   // MyJSQLView_Frame Constructor
   //==============================================================

   protected MyJSQLView_Frame(String[] myJSQLView_Version, String webSiteString)
   {
      // Displaying title and assigning instance associations.

      super("MyJSQLView   "
            + ConnectionManager.getConnectionProperties().getProperty(ConnectionProperties.HOST) + ":"
            + ConnectionManager.getConnectionProperties().getProperty(ConnectionProperties.DB));

      this.myJSQLView_Version = myJSQLView_Version;
      this.webSiteString = webSiteString;
      
      //==================================================
      // Frame Window closing listener to detect the frame
      // window closing event so the SQL Query Bucket can
      // be saved.
      //==================================================

      WindowListener myjsqlviewFrameListener = new WindowAdapter()
      {
         public void windowClosing(WindowEvent e)
         {
            sqlQueryBucketFrame.saveLastUsedList();
            System.exit(0);
         }

         public void windowActivated(WindowEvent e){}
      };
      this.addWindowListener(myjsqlviewFrameListener);
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
      
      topMenuBar = new MyJSQLView_Top_JMenuBar(this);
      setJMenuBar(topMenuBar);
      
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
      mainTabsPane.add(new JPanel(), databaseTablesIcon, 1);
      
      Thread databaseTablesThread = new Thread(new Runnable()
      {
         public void run()
         {
            // Thread Instances.
            Connection dbConnection;
            MyJSQLView_ResourceBundle resourceBundle;
            String resource;
            
            // Obtain a database connection & resources.
            
            dbConnection = ConnectionManager.getConnection("MyJSQLView_Frame createGUI()");
            resourceBundle = MyJSQLView.getLocaleResourceBundle();
            
            dbTablesPanel = new DBTablesPanel(dbConnection, ConnectionManager.getTableNames());
            mainTabsPane.setComponentAt(1, dbTablesPanel);
            
            resource = resourceBundle.getResource("MyJSQLView_Frame.tab.DatabaseTables");
            if (resource.equals(""))
               mainTabsPane.setToolTipTextAt(1, "Database Tables");
            else
               mainTabsPane.setToolTipTextAt(1, resource);
            
            // Closing the database connection that is used
            // during the inital setup of the application.
            
            ConnectionManager.closeConnection(dbConnection, "MyJSQLView_Frame createGUI()");
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
      MyJSQLView_JMenuBarActions.actionsSelection(this, evt, sqlQueryBucketFrame, myJSQLView_Version, webSiteString);
   }
   
   //==============================================================
   // ChangeEvent Listener method for detecting the user's selection
   // of the various tabs that are loaded in the application.
   //==============================================================

   public void stateChanged(ChangeEvent evt)
   {
      // Method Instances.
      Object changeSource;
      int selectedIndex;
      
      // Collect source of event and take appropriate action.
      
      changeSource = evt.getSource();
      
      if (changeSource != null && (JTabbedPane) changeSource == mainTabsPane)
      {
         // Obtain some parameters to be used.
         
         selectedIndex = ((JTabbedPane) changeSource).getSelectedIndex();
         
         if (selectedIndex > mainTabsPane.getTabCount())
            return;
         
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
            setJMenuBar(topMenuBar);
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
         if (selectedIndex == 0)
            toolBarCardLayout.show(toolBarPanel, "0");
         else if (selectedIndex == 1)
            toolBarCardLayout.show(toolBarPanel, "1");
         else
            toolBarCardLayout.show(toolBarPanel, (loadedPluginModules.get(selectedIndex - 2)).name);
      }
   }
   
   //==============================================================
   // Class Method to add a new plugin tab to the frame interface.
   //==============================================================
   
   protected static synchronized void addTab(MyJSQLView_PluginModule plugin, MyJSQLView_Frame parent)
   {
      if (plugin != null)
      {  
         mainTabsPane.removeChangeListener(parent);
         
         loadedPluginModules.add(plugin);
         mainTabsPane.addTab(null, plugin.tabIcon, plugin.panel, plugin.name);
         
         if (plugin.name.equals(""))
            plugin.name = Integer.toString(loadedPluginModules.size() + 1);
         
         toolBarPanel.add(plugin.name, plugin.toolBar);
         
         // Lets the PluginFrame know that a new
         // plugin module was loaded.
         pluginFrameListenButton.doClick();
         
         mainTabsPane.addChangeListener(parent);
      } 
   }
   
   //==============================================================
   // Class Method to remove a plugin tab from the frame interface.
   //==============================================================
   
   protected static void removeTab(int index)
   { 
      mainTabsPane.removeTabAt(index + 2);
      toolBarPanel.remove((loadedPluginModules.get(index)).toolBar);
      loadedPluginModules.remove(index);
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
      
      dbConnection = ConnectionManager.getConnection("TableTabPanel_Frame reloadDBTables()");
      
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
         ConnectionManager.loadDBTables(dbConnection);
         
         DBTablesPanel.reloadPanel(dbConnection, ConnectionManager.getTableNames());
         dbTablesPanel.repaint();
         
         // Reload Plugins' Tables.
         pluginModulesIterator = loadedPluginModules.iterator();
         while (pluginModulesIterator.hasNext())
         {
            MyJSQLView_PluginModule currentPlugin = pluginModulesIterator.next();
            currentPlugin.setDBTables(ConnectionManager.getTableNames());
         }
         
         // Try set the table showing before the reload.
         
         if (!currentSelectedTable.equals(""))
            DBTablesPanel.setSelectedTableTabPanel(currentSelectedTable);
         
         mainTabsPane.validate();
      }
      catch (SQLException e)
      {
         ConnectionManager.displaySQLErrors(e, "MyJSQLView_Frame reloadDBTables()");
      }
      
      ConnectionManager.closeConnection(dbConnection, "TableTabPanel_Frame reloadDBTables()");
   }
   
   //==============================================================
   // Class Method to load the Query Buckets default last saved
   // list on closing. Based on database connection.
   //==============================================================
   
   protected void loadQueryBucketList()
   {
      // Thread the setup of the Query Bucket.
      Thread loadQueryBucketList = new Thread(new Runnable()
      {
         public void run()
         {
            sqlQueryBucketFrame.setSize(500, 450);
            sqlQueryBucketFrame.setResizable(false);
            sqlQueryBucketFrame.center();
            sqlQueryBucketFrame.openLastUsedList(ConnectionManager.getConnectionProperties()
               .getProperty(ConnectionProperties.DB));
         }
      }, "MyJSQLView_Frame.loadQueryBucketList");
      loadQueryBucketList.start();
   }
   
   //==============================================================
   // Class Method to return the current loaded plugins.
   //==============================================================
   
   protected static Vector<MyJSQLView_PluginModule> getPlugins()
   {
      return loadedPluginModules;
   }
   
   //==============================================================
   // Class Method to return the SQL Bucket Frame.
   //==============================================================
   
   public static SQLQueryBucketFrame getSQLBucket()
   {
      return sqlQueryBucketFrame;
   }
}