//=================================================================
//                 MyJSQLView Plugin Frame.
//=================================================================
//   This class provides a frame that is used to view, remove, and
// install new plugins to the MyJSQLView application.
//
//                   << PluginFrame.java >>
//
//=================================================================
// Copyright (C) 2005-2013 Dana M. Proctor
// Version 3.4 08/11/2013
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
// Version 1.0 Initial MyJSQLView PluginFrame Class.
//         1.1 Basic GUI Completion, Functionality Still Needed.
//         1.2 Completed Functionality of Removing/Installing Plugins via
//             MyJSQLView_Frame and Configuration File. Added Mouse and Action
//             Event Processing and Methods removePluginConfigurationsModule(),
//             & installPlugin().
//         1.3 Copyright Update.
//         1.4 Added Loading Plugins Capability. Added Class Instances
//             loadingPluginsList, loadingPluginViewTableData, tableModelLoadingPlugins,
//             & loadingPluginViewTable. New Class Methods createLoadingPluginsViewPanel,
//             & displayLoadingPluginsData().
//         1.5 Copyright Update.
//         1.6 Changed Class Instance loadingPluginsList from Vector to ArrayList.
//         1.7 Changes in Way MyJSQLView_ResourceBundle Handles the Collection
//             of Resource Strings. Change to resource.getResourceString(key,
//             default).
//         1.8 Change in installPlugin() to Use New Updated Call to PluginLoader
//             to Use an URL.
//         1.9 MyJSQLView Class Method Change of getLocaleResourceBundle()
//             to getResourceBundle(). Correction in installPlugin() to Properly
//             Specify a File URL for fileName.
//         2.0 Collection of All Image Resources Through resourceBundle.
//         2.1 Changed Package Name to com.dandymadeproductions.myjsqlview.gui.
//             Beginning of Complete Rebuild to Allow Use of External,
//             Non-Local Repositories.
//         2.2 PluginRepositoryPanel Referenced From panels Package. Class Method
//             displayLoadedPluginsData() Referenced Plugin Parameters via getControlled
//             Methods.
//         2.3 Rolling Update. Continuing the Rebuild from 2.1, & 2.2. To Many Changes
//             to Note. Mainly Structure Changes to Class and Some GUI Additions to Achieve
//             Repository/Plugin Loading & Selection. This Syncs the Code So That Things
//             Should Not be Broken.
//         2.4 Rolling Update. Continuing the Rebuild from 2.1, 2.2, & 2.3. Many Changes.
//             Organized Clarified Class Instance Names. Method installPlugin() Changed to
//             installFilePlugin() & displayLoadedPluginsData() to generateLoadedPluginsList().
//             Changed Behavior of stateChanged() Repository Tabs in Displaying Information.
//             Added ListSelectionListener & Its Associated Method. Added Class Method
//             displayPluginInfo().
//         2.5 Rolling Update Continuing the Rebuild from 2.1, 2.2, 2.3, & 2.4. Many
//             Changes. Removed Class Instance repositoryType. General Cleanup. Class
//             Method installFilePlugin() Only Add Loading Plugin to List if PluginLoading
//             Does Not Throw an Inital MalFormedURLException. Class Method addRepository()
//             Still Testing & Finalizing.
//         2.6 Rolling Update Continuing the Rebuild from 2.1-2.5. Removed Class Instance
//             createDefaultMyJSQLViewRepository(), Re-organized Order of Tab Adding, Added
//             in Method removeRepository() Cache Clearing Through runnable, Added Class
//             Methods deleteRepositoryCache() & createRepository(). Formatted.
//         2.7 Added Class Instance MYJSQLVIEW_PLUGIN_CONFIGURATION_FILE. Implemented Install
//             From HTTP_Repository in actionPerformed(). Added Class Method addPlugin().
//             Made removePluginConfigurationModule() proteced & static.
//         2.8 Commented Out the Creation of Default MyJSQLView Repository. Implemented
//             the Loading Existing Repositoies Through loadCachedRepositories(). Moved
//             Some Instances From Inner Blocks to Method Scope in actionPerformed().
//             Removed Class Method refreshRepository() & Implemented Directly in
//             actionPerformed() at refreshButton Detection. Class Method addRepository()
//             Referenced in Setting Dialog Information With MYJSQLVIEW_REPOSITORY_NAME
//             & MYJSQLVIEW_REPOSITORY.
//         2.9 Moved Constructor Instances tabType, & currentTabIndex Default Setting Until
//             After Tabs Setup.
//         3.0 Added Class Instance addRepositoryIcon. Class Method addRepository() Added
//             Instance resourceTitle for Dialog & Changed Borders for JTextFields. Added
//             in Same addRepositoryIcon to Dialog.
//         3.1 Synchronized Method removePluginConfiguationModule() & Threaded Call to in
//             Method mouseClicked() for Removal of Repository Tab.
//         3.2 Added protected Method startAnimation() to Begin the Animation with the
//             northFillerPanel.
//         3.3 Correction in addRepository() for Default Resource Strings, Repository Name
//             & Repository URL.
//         3.4 Added static Class Instance LOAD_DEFAULT_REPOSITORY to More Closely Control
//             the Loading of the MyJSQLView, Default, Repository. Methods Effected
//             removeRepository(), & loadCachedRepository().
//             
//-----------------------------------------------------------------
//                 danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;
import javax.swing.text.html.HTMLEditorKit;

import com.dandymadeproductions.myjsqlview.MyJSQLView;
import com.dandymadeproductions.myjsqlview.gui.panels.PluginFrameFillerPanel;
import com.dandymadeproductions.myjsqlview.gui.panels.PluginRepositoryPanel;
import com.dandymadeproductions.myjsqlview.io.ReadDataFile;
import com.dandymadeproductions.myjsqlview.io.WriteDataFile;
import com.dandymadeproductions.myjsqlview.plugin.FILE_PluginRepository;
import com.dandymadeproductions.myjsqlview.plugin.HTTP_PluginRepository;
import com.dandymadeproductions.myjsqlview.plugin.MyJSQLView_PluginModule;
import com.dandymadeproductions.myjsqlview.plugin.PluginLoader;
import com.dandymadeproductions.myjsqlview.plugin.PluginRepository;
import com.dandymadeproductions.myjsqlview.utilities.InputDialog;
import com.dandymadeproductions.myjsqlview.utilities.JarFileFilter;
import com.dandymadeproductions.myjsqlview.utilities.MyJSQLView_ResourceBundle;
import com.dandymadeproductions.myjsqlview.utilities.MyJSQLView_TableModel;
import com.dandymadeproductions.myjsqlview.utilities.MyJSQLView_Utils;

/**
 *    The PluginFrame class provides a frame that is used to view,
 * remove, and install new plugins to the MyJSQLView application.
 * 
 * @author Dana M. Proctor
 * @version 3.4 08/11/2013
 */

//=================================================================
// MyJSQLView PluginFrame
//=================================================================

public class PluginFrame extends JFrame implements ActionListener, ChangeListener, ListSelectionListener,
                                            MouseListener
{
   // Creation of the necessary class instance
   private static final long serialVersionUID = 6203223580678904034L;

   private MyJSQLView_Frame parentFrame;
   private JPanel mainPanel;
   private PluginFrameFillerPanel northFillerPanel;
   private JSplitPane splitPane;
   private JTabbedPane centralTabsPane;
   private CardLayout infoViewCardLayout;
   private JPanel infoViewPanel;

   private JTable loadedPluginsTable, loadingPluginTable;
   private MyJSQLView_TableModel loadedPluginsTableModel, loadingPluginsTableModel;
   private Object[][] loadedPluginTableData, loadingPluginTableData;
   private ArrayList<String> loadingPluginsList;
   private Hashtable<String, String> repositoryHashtable;
   private JTextPane pluginInformationTextPane;
   private JButton installButton, closeButton, refreshButton;

   private MyJSQLView_ResourceBundle resourceBundle;
   private String fileSeparator, iconsDirectory, lastPluginDirectory;
   private String resourceAlert;

   private ImageIcon statusWorkingIcon;
   private ImageIcon addRepositoryIcon, deleteRepositoryIcon;
   private ImageIcon defaultModuleIcon, removeIcon;

   private String tabType;
   private int currentTabIndex, removeTabIndex, addTabIndex;

   private static final String MANAGE = "manage";
   private static final String REPOSITORY = "repository";
   private static final String INFO_VIEW_LOADING_STATUS = "Loading Status";
   private static final String INFO_VIEW_PLUGIN_INFORMATION = "Plugin Information";

   private static final int TABICON_COLUMN = 0;
   private static final int NAME_COLUMN = 1;
   private static final int VERSION_COLUMN = 2;
   private static final int REMOVE_COLUMN = 3;
   private static final int AUTHOR_COLUMN = 4;
   private static final int PATH_COLUMN = 5;
   private static final int DESCRIPTION_COLUMN = 6;

   private static boolean LOAD_DEFAULT_REPOSITORY = false;
   private static final String MYJSQLVIEW_REPOSITORY_NAME = "MyJSQLView";
   private static final String MYJSQLVIEW_REPOSITORY = "http://plugins.myjsqlview.org/";
   private static final String MYJSQLVIEW_PLUGIN_CONFIGURATION_FILE = "myjsqlview_plugin.conf";

   //==============================================================
   // PluginFrame Constructor
   //==============================================================

   protected PluginFrame(MyJSQLView_Frame parent)
   {
      parentFrame = parent;

      // Constructor Instances.
      JPanel southButtonPanel, buttonPanel;
      JPanel pluginViewPanel, loadingViewPanel;
      JScrollPane infoScrollPane;

      ImageIcon plusIcon, minusIcon, refreshIcon;
      String resource;

      // Setting up resources & instances.

      resourceBundle = MyJSQLView.getResourceBundle();

      fileSeparator = MyJSQLView_Utils.getFileSeparator();
      iconsDirectory = MyJSQLView_Utils.getIconsDirectory() + fileSeparator;
      lastPluginDirectory = "";

      resourceAlert = resourceBundle.getResourceString("PluginFrame.dialogtitle.Alert", "Alert");

      statusWorkingIcon = resourceBundle.getResourceImage(iconsDirectory + "statusWorkingIcon.png");
      removeIcon = resourceBundle.getResourceImage(iconsDirectory + "removeIcon.png");
      addRepositoryIcon = resourceBundle.getResourceImage(iconsDirectory + "addRepositoryIcon.gif");
      deleteRepositoryIcon = resourceBundle.getResourceImage(iconsDirectory + "deleteDataIcon.gif");
      defaultModuleIcon = resourceBundle.getResourceImage(iconsDirectory + "newsiteLeafIcon.png");

      repositoryHashtable = new Hashtable <String, String>();
      loadingPluginsList = new ArrayList <String>();

      // Setting the frame's title, main panel, & window listener.

      resource = resourceBundle.getResourceString("PluginFrame.message.Title", "Plugin Management");
      setTitle(resource);

      mainPanel = new JPanel(new BorderLayout());
      mainPanel.setBorder(BorderFactory.createRaisedBevelBorder());

      GridBagLayout gridbag = new GridBagLayout();
      GridBagConstraints constraints = new GridBagConstraints();

      this.addWindowListener(pluginFrameListener);

      // ======================================================
      // Animated Filler Panel.

      northFillerPanel = new PluginFrameFillerPanel();
      mainPanel.add(northFillerPanel, BorderLayout.NORTH);

      // ======================================================
      // Central Area for Manager, Repositories. Setup as a
      // split pane with tabbed area above and info view below.

      centralTabsPane = new JTabbedPane();
      centralTabsPane.setTabPlacement(JTabbedPane.TOP);
      centralTabsPane.setBorder(BorderFactory.createLoweredBevelBorder());
      centralTabsPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

      splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, false);
      splitPane.setOneTouchExpandable(true);
      splitPane.setResizeWeight(0.65);

      EventQueue.invokeLater(new Runnable()
      {
         public void run()
         {
            splitPane.setDividerLocation(0.65);
         }
      });

      // ======================================================
      // Placement of Components in Tabbed Pane

      // Manage plugins tab, show existing and removal option.

      pluginViewPanel = createInstalledPluginsViewPanel(MyJSQLView_Frame.getPlugins());
      resource = resourceBundle.getResourceString("PluginFrame.tab.Manage", "Manage");
      centralTabsPane.addTab(resource, null, pluginViewPanel, resource);

      // Repository removal/addition control mechanism tabs.

      minusIcon = resourceBundle.getResourceImage(iconsDirectory + "minusIcon.png");
      resource = resourceBundle.getResourceString("PluginFrame.tab.RemoveRepository", "Remove Repository");
      centralTabsPane.addTab(null, minusIcon, new JPanel(), resource);
      removeTabIndex = centralTabsPane.getTabCount() - 1;

      plusIcon = resourceBundle.getResourceImage(iconsDirectory + "plusIcon.png");
      resource = resourceBundle.getResourceString("PluginFrame.tab.AddRepository", "Add Repository");
      centralTabsPane.addTab(null, plusIcon, new JPanel(), resource);
      addTabIndex = centralTabsPane.getTabCount() - 1;

      // Default MyJSQLView repository.

      if (LOAD_DEFAULT_REPOSITORY)
         createRepository(MYJSQLVIEW_REPOSITORY_NAME, MYJSQLVIEW_REPOSITORY);

      // Additional repositories as defined by configuration file?
      
      loadCachedRepositories();

      tabType = MANAGE;
      currentTabIndex = 0;
      centralTabsPane.setSelectedIndex(0);
      splitPane.setTopComponent(centralTabsPane);

      // ======================================================
      // Loading Status/Plugin Infomation View.

      infoViewCardLayout = new CardLayout();
      infoViewPanel = new JPanel(infoViewCardLayout);
      infoViewPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4),
         BorderFactory.createLoweredBevelBorder()));

      loadingViewPanel = createLoadingPluginsViewPanel();
      infoViewPanel.add(INFO_VIEW_LOADING_STATUS, loadingViewPanel);

      pluginInformationTextPane = new JTextPane();
      pluginInformationTextPane.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, true);
      pluginInformationTextPane.setEditorKit(new HTMLEditorKit());
      pluginInformationTextPane.setEditable(false);
      pluginInformationTextPane.addMouseListener(MyJSQLView.getPopupMenuListener());

      infoScrollPane = new JScrollPane(pluginInformationTextPane);
      infoViewPanel.add(INFO_VIEW_PLUGIN_INFORMATION, infoScrollPane);

      splitPane.setBottomComponent(infoViewPanel);

      mainPanel.add(splitPane, BorderLayout.CENTER);

      // ======================================================
      // Buttons to install plugins, close down the frame &
      // refresh repository.

      southButtonPanel = new JPanel(gridbag);
      southButtonPanel.setBorder(BorderFactory.createEtchedBorder());

      buttonPanel = new JPanel();
      buttonPanel.setBorder(BorderFactory.createEmptyBorder());

      resource = resourceBundle.getResourceString("PluginFrame.button.Install", "Install");
      installButton = new JButton(resource);
      installButton.setFocusPainted(false);
      installButton.addActionListener(this);
      buttonPanel.add(installButton);

      resource = resourceBundle.getResourceString("PluginFrame.button.Close", "Close");
      closeButton = new JButton(resource);
      closeButton.setFocusPainted(false);
      closeButton.addActionListener(this);
      buttonPanel.add(closeButton);

      buildConstraints(constraints, 0, 0, 1, 1, 95, 100);
      constraints.fill = GridBagConstraints.BOTH;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(buttonPanel, constraints);
      southButtonPanel.add(buttonPanel);

      resource = resourceBundle.getResourceString("PluginFrame.button.RefreshRepository",
                                                  "Refresh Repository");
      refreshIcon = resourceBundle.getResourceImage(iconsDirectory + "refreshIcon.png");
      refreshButton = new JButton(refreshIcon);
      refreshButton.setToolTipText(resource);
      refreshButton.setHorizontalAlignment(JButton.RIGHT);
      refreshButton.setMargin(new Insets(0, 0, 0, 0));
      refreshButton.addActionListener(this);

      buildConstraints(constraints, 1, 0, 1, 1, 5, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(refreshButton, constraints);
      southButtonPanel.add(refreshButton);

      mainPanel.add(southButtonPanel, BorderLayout.SOUTH);

      // ======================================================
      // Finish up.

      centralTabsPane.addChangeListener(this);
      getContentPane().add(mainPanel);
   }

   //==============================================================
   // WindowListener for insuring that when the frame is closed,
   // (x), MyJSQLView main frame is notified.
   //==============================================================

   private transient WindowListener pluginFrameListener = new WindowAdapter()
   {
      // Standard frame close event, make sure calling frame knows.
      public void windowClosing(WindowEvent e)
      {
         MyJSQLView_JMenuBarActions.setPluginFrameNotVisisble();
         dispose();
      }
   };

   //==============================================================
   // ActionEvent Listener method for determining when selections
   // have been made so an appropriate action can be taken.
   //==============================================================

   public void actionPerformed(ActionEvent evt)
   {
      // Method Instances
      Object frameSource;
      Object selectedTabComponent;
      PluginRepositoryPanel selectedRepositoryPanel;
      String repositoryType;
      
      frameSource = evt.getSource();

      // Overall action buttons.
      if (frameSource instanceof JButton)
      {
         // Install button action.
         if (frameSource == installButton)
         {
            infoViewCardLayout.show(infoViewPanel, INFO_VIEW_LOADING_STATUS);

            // Local file.
            if (tabType.equals(MANAGE))
               installFilePlugin(this);
            // Repository
            else
            {
               selectedTabComponent = centralTabsPane.getSelectedComponent();

               if (selectedTabComponent != null && selectedTabComponent instanceof PluginRepositoryPanel)
               {
                  selectedRepositoryPanel = (PluginRepositoryPanel) selectedTabComponent;
                  repositoryType = selectedRepositoryPanel.getRepositoryType();
                  
                  if (repositoryType.equals(PluginRepository.FILE))
                  {
                     System.out.println("File Repository Install");
                  }
                  else if (repositoryType.equals(PluginRepository.FTP))
                  {
                     System.out.println("FTP Repository Install");
                  }
                  else if (repositoryType.equals(PluginRepository.HTTP))
                  {
                     String selectedPluginPath = selectedRepositoryPanel.getSelectedPluginPath();
                     
                     if (!selectedPluginPath.isEmpty())
                        loadPlugin(selectedPluginPath);
                  }
                  else
                  {
                     System.out.println("Unknown Repository Install");
                  }
               }
            }
         }
         // MyJSQLView_Frame Tab Addition Notification
         // for New Plugin Install.
         else if (frameSource == MyJSQLView_Frame.pluginFrameListenButton)
         {
            // Update plugins list.
            MyJSQLView_Frame.pluginFrameListenButton.removeActionListener(this);

            generateLoadedPluginsList(MyJSQLView_Frame.getPlugins());
            loadedPluginsTableModel.setValues(loadedPluginTableData);

            // Update loading plugins list.
            displayLoadingPluginsData();
         }
         // Refresh repository plugin list.
         else if (frameSource == refreshButton)
         {
            if (!tabType.equals(MANAGE))
            {
               selectedTabComponent = centralTabsPane.getSelectedComponent();

               if (selectedTabComponent != null && selectedTabComponent instanceof PluginRepositoryPanel)
               {
                  selectedRepositoryPanel = (PluginRepositoryPanel) selectedTabComponent;
                  selectedRepositoryPanel.refreshRepository();
                  pluginInformationTextPane.setText("");
               }
            }
         }
         // Must be action of Close buttton.
         else
         {
            MyJSQLView_JMenuBarActions.setPluginFrameNotVisisble();
            this.dispose();
         }
      }
      else
         return;
   }

   //==============================================================
   // ChangeEvent Listener method for detecting the user's selection
   // of the frame's manage/repository tab space.
   //==============================================================

   public void stateChanged(ChangeEvent evt)
   {
      // Method Instances.
      Object changeSource;
      int selectedIndex;

      // Collect source of event and take appropriate action.

      changeSource = evt.getSource();

      if (changeSource != null && (JTabbedPane) changeSource == centralTabsPane)
      {
         // Obtain some parameters to be used & disable
         // tabbed pane activity.

         selectedIndex = ((JTabbedPane) changeSource).getSelectedIndex();
         centralTabsPane.removeChangeListener(this);

         if (selectedIndex > centralTabsPane.getTabCount())
            return;

         // Manage Tab Selected
         if (selectedIndex == 0)
         {
            tabType = MANAGE;
            currentTabIndex = 0;
         }
         // Repository Tab Selected
         else if (selectedIndex > 0 && selectedIndex < removeTabIndex)
         {
            tabType = REPOSITORY;
            currentTabIndex = selectedIndex;
         }
         // Remove Repository
         else if (selectedIndex == removeTabIndex)
         {
            if (centralTabsPane.getTabCount() > 3 && currentTabIndex != 0)
               removeRepository();

            tabType = MANAGE;
            centralTabsPane.setSelectedIndex(0);
            currentTabIndex = 0;
         }
         // Add Repository
         else if (selectedIndex == addTabIndex)
         {
            addRepository();
         }
         // No Way?
         else
         {
            if (MyJSQLView.getDebug())
               System.out.println("PluginFrame stateChanged() Tab Tracking Error.");
            return;
         }

         // Set the appropriate information view.

         if (tabType.equals(MANAGE))
         {
            loadedPluginsTable.clearSelection();
            pluginInformationTextPane.setText("");
            infoViewCardLayout.show(infoViewPanel, INFO_VIEW_LOADING_STATUS);
         }
         else
         {
            if (tabType.equals(REPOSITORY))
            {
               pluginInformationTextPane.setText("");

               Object selectedComponent = centralTabsPane.getSelectedComponent();

               if (selectedComponent != null && selectedComponent instanceof PluginRepositoryPanel)
               {
                  PluginRepositoryPanel selectedRepositoryPanel = (PluginRepositoryPanel) selectedComponent;
                  displayPluginInfo(selectedRepositoryPanel.getSelectedPluginInfo());
               }
               infoViewCardLayout.show(infoViewPanel, INFO_VIEW_PLUGIN_INFORMATION);
            }
         }

         centralTabsPane.addChangeListener(this);
      }
   }

   //==============================================================
   // ListSelectionEvent Listener method for detecting the user's
   // selection of various rows in the plugin Manage view table &
   // repositories plugin list tables.
   //==============================================================

   public void valueChanged(ListSelectionEvent e)
   {
      if (tabType.equals(MANAGE))
      {
         int tableRow;

         tableRow = loadedPluginsTable.getSelectedRow();

         if (tableRow >= loadedPluginsTable.getRowCount() || tableRow < 0)
            return;
         else
         {
            Object[] pluginInfo = new Object[5];

            pluginInfo[0] = loadedPluginTableData[tableRow][NAME_COLUMN];
            pluginInfo[1] = loadedPluginTableData[tableRow][AUTHOR_COLUMN];
            pluginInfo[2] = loadedPluginTableData[tableRow][VERSION_COLUMN];
            pluginInfo[3] = loadedPluginTableData[tableRow][PATH_COLUMN];
            pluginInfo[4] = loadedPluginTableData[tableRow][DESCRIPTION_COLUMN];

            displayPluginInfo(pluginInfo);
         }
      }
      // Repository
      else
      {
         Object selectedComponent = centralTabsPane.getSelectedComponent();

         if (selectedComponent != null && selectedComponent instanceof PluginRepositoryPanel)
         {
            PluginRepositoryPanel selectedRepositoryPanel = (PluginRepositoryPanel) selectedComponent;
            displayPluginInfo(selectedRepositoryPanel.getSelectedPluginInfo());
         }
      }
   }

   //==============================================================
   // MouseEvent Listener methods for detecting mouse events.
   // MounseListner Interface requirements.
   //==============================================================

   public void mouseEntered(MouseEvent evt)
   {
      // Do Nothing.
   }

   public void mouseExited(MouseEvent evt)
   {
      // Do Nothing.
   }

   public void mousePressed(MouseEvent evt)
   {
      // Do Nothing.
   }

   public void mouseReleased(MouseEvent evt)
   {
      // Do Nothing.
   }

   //==============================================================
   // MouseEvent Listener method for detecting mouse clicked events.
   // Collects the row & column selected from the mange tab so that
   // removal can take place of selected Plugin Module.
   //==============================================================

   public void mouseClicked(MouseEvent e)
   {
      Point coordinatePoint;
      int tableRow, tableColumn;

      // Collect coordinate to determine cell selected.
      coordinatePoint = e.getPoint();

      tableRow = loadedPluginsTable.rowAtPoint(coordinatePoint);
      tableColumn = loadedPluginsTable.columnAtPoint(coordinatePoint);

      if (tableRow >= loadedPluginsTable.getRowCount() || tableRow < 0)
         return;
      else
      {
         // Remove Plugin Action
         if (tableColumn == REMOVE_COLUMN)
         {
            final String pathFileName = MyJSQLView_Frame.getPlugins().get(tableRow).getPath_FileName();
            
            Thread removePluginConfigurationModuleThread = new Thread(new Runnable()
            {
               public void run()
               {
                  removePluginConfigurationModule(pathFileName);
               }
            }, "PluginFrame.removePluginConfigurationModuleThread");
            removePluginConfigurationModuleThread.start();
            
            MyJSQLView_Frame.removeTab(tableRow);
            generateLoadedPluginsList(MyJSQLView_Frame.getPlugins());
            loadedPluginsTableModel.setValues(loadedPluginTableData);
            pluginInformationTextPane.setText("");
         }
      }
   }

   //==============================================================
   // Classs Method to aquire a local file system plugin file to
   // be installed.
   //==============================================================

   private void installFilePlugin(JFrame parent)
   {
      // Method Instances.
      JFileChooser pluginFileChooser;
      String fileName;

      // Collect/Set the default directory to be used.
      if (lastPluginDirectory.equals(""))
         pluginFileChooser = new JFileChooser();
      else
         pluginFileChooser = new JFileChooser(new File(lastPluginDirectory));

      // Add a FileFilter for *.jar and open dialog.
      pluginFileChooser.setFileFilter(new JarFileFilter());

      int result = pluginFileChooser.showOpenDialog(parent);

      // Looks like might be good so lets check and read data.
      if (result == JFileChooser.APPROVE_OPTION)
      {
         // Save the selected directory so can be used again.
         lastPluginDirectory = pluginFileChooser.getCurrentDirectory().toString();

         // Collect file name.
         fileName = pluginFileChooser.getSelectedFile().getName();
         fileName = "file:" + pluginFileChooser.getCurrentDirectory() + fileSeparator + fileName;

         // Try Loading the module and if appears ok add
         // the module to the loading list.
         if (!fileName.equals(""))
         {
            loadPlugin(fileName);
         }
         else
         {
            String optionPaneStringErrors;

            optionPaneStringErrors = resourceBundle.getResourceString(
               "PluginFrame.dialogmessage.FileNOTFound", "File NOT Found");

            JOptionPane.showMessageDialog(null, optionPaneStringErrors, resourceAlert,
               JOptionPane.ERROR_MESSAGE);
         }
      }
   }
   
   //==============================================================
   // Class Method for starting the process of actually loading the
   // plugin into the MyJSQLView_Frame.
   //==============================================================
   
   private void loadPlugin(String URLString)
   {
      // Method Instances
      String resource;
      
      MyJSQLView_Frame.pluginFrameListenButton.addActionListener(this);

      try
      {
         new PluginLoader(parentFrame, new URL(URLString));

         loadingPluginsList.add(URLString);
         displayLoadingPluginsData();
      }
      catch (MalformedURLException mfe)
      {
         resource = resourceBundle.getResourceString("PluginFrame.dialogmessage.FailedToCreateURL",
                                                     "Failed to Create URL");

         JOptionPane.showMessageDialog(null, resource + "\n" + mfe.toString(), resourceAlert,
                                       JOptionPane.ERROR_MESSAGE);
      }
   }

   //==============================================================
   // Class Method for loading the plugin modules data, tab icon,
   // name, etc. in the manage tab view table.
   //==============================================================

   private void generateLoadedPluginsList(ArrayList<MyJSQLView_PluginModule> loadedPlugins)
   {
      // Method Instances.
      String path;

      loadedPluginTableData = new Object[loadedPlugins.size()][7];

      for (int i = 0; i < loadedPlugins.size(); i++)
      {
         // Plugin tab icon, name, author, version, path,
         // description and remove element.

         if (loadedPlugins.get(i).getControlledTabIcon() == null)
            loadedPluginTableData[i][TABICON_COLUMN] = defaultModuleIcon;
         else
            loadedPluginTableData[i][TABICON_COLUMN] = loadedPlugins.get(i).getControlledTabIcon();

         loadedPluginTableData[i][NAME_COLUMN] = loadedPlugins.get(i).getControlledName();
         loadedPluginTableData[i][AUTHOR_COLUMN] = loadedPlugins.get(i).getControlledAuthor();
         loadedPluginTableData[i][VERSION_COLUMN] = loadedPlugins.get(i).getControlledVersion();
         loadedPluginTableData[i][PATH_COLUMN] = loadedPlugins.get(i).getPath_FileName();
         loadedPluginTableData[i][DESCRIPTION_COLUMN] = loadedPlugins.get(i).getDescription();
         loadedPluginTableData[i][REMOVE_COLUMN] = removeIcon;

         // Remove plugins from loading list.
         path = loadedPlugins.get(i).getPath_FileName().substring(0,
            loadedPlugins.get(i).getPath_FileName().indexOf("<$$$>"));

         if (loadingPluginsList.contains(path))
            loadingPluginsList.remove(path);
      }
   }

   //==============================================================
   // Class Method for displaying the plugin information for a
   // given selection either in the manage tab, already loaded
   // plugin, or a repository.
   //==============================================================

   private void displayPluginInfo(Object[] params)
   {
      // Method Instances
      String infoText, path;

      // Name, Author, Version, Path, & Desciption
      if (params == null || params.length != 5)
         return;

      infoText = "";
      if (params[0] != null)
         infoText += "<b>Name: </b>" + params[0] + "<br>";

      if (params[1] != null)
         infoText += "<b>Author: </b>" + params[1] + "<br>";

      if (params[2] != null)
         infoText += "<b>Version: </b>" + params[2] + "<br><br>";

      if (params[3] != null)
      {
         path = params[3].toString();
         if (path.indexOf(PluginLoader.pathClassSeparator) != -1)
            path = path.substring(0, path.indexOf(PluginLoader.pathClassSeparator));
         infoText += "<b>Path: </b>" + path + "<br><br>";
      }

      infoText += "<b>Description: </b><br>";

      if (params[4] != null)
         infoText += params[4];

      infoText = infoText.replace("\n", "<br>");
      infoText = "<html>" + infoText + "</html>";

      pluginInformationTextPane.setText(infoText);
      pluginInformationTextPane.setCaretPosition(0);
      infoViewCardLayout.show(infoViewPanel, INFO_VIEW_PLUGIN_INFORMATION);
   }

   //==============================================================
   // Class Method for updating the status data for current loading
   // plugin modules. Includes status and name.
   //==============================================================

   private void displayLoadingPluginsData()
   {
      loadingPluginTableData = new Object[loadingPluginsList.size()][2];

      for (int i = 0; i < loadingPluginsList.size(); i++)
      {
         // Loading plugin status indicator, & name.
         loadingPluginTableData[i][TABICON_COLUMN] = statusWorkingIcon;
         loadingPluginTableData[i][NAME_COLUMN] = loadingPluginsList.get(i);
      }

      if (loadingPluginsTableModel != null)
         loadingPluginsTableModel.setValues(loadingPluginTableData);
   }

   //==============================================================
   // Class Method for removing a plugin repository.
   //==============================================================

   private void removeRepository()
   {
      // Method Instances
      JLabel message;
      InputDialog deleteDialog;
      String resourceMessage, resourceCancel, resourceOK;

      // Confirming really want to delete dialog.

      resourceMessage = resourceBundle.getResourceString("PluginFrame.message.DeleteRepository",
                                                         "Delete Repository?");
      message = new JLabel(resourceMessage, JLabel.CENTER);
      message.setFont(new Font("DIALOG", Font.BOLD, 14));
      message.setForeground(Color.RED);
      Object[] content = {message};

      resourceOK = resourceBundle.getResourceString("PluginFrame.dialogbutton.OK", "OK");
      resourceCancel = resourceBundle.getResourceString("PluginFrame.dialogbutton.Cancel", "Cancel");

      deleteDialog = new InputDialog(null, resourceAlert, resourceOK, resourceCancel, content,
                                     deleteRepositoryIcon);
      deleteDialog.pack();
      deleteDialog.center();
      deleteDialog.setResizable(false);
      deleteDialog.setVisible(true);

      // Deleting

      if (deleteDialog.isActionResult())
      {
         final String repositoryName = centralTabsPane.getTitleAt(currentTabIndex);

         // Remove from cache unless default MyJSQLView.
         if (!(repositoryName.equals(MYJSQLVIEW_REPOSITORY_NAME) && LOAD_DEFAULT_REPOSITORY))
         {
            Thread deleteRepositoryCacheThread = new Thread(new Runnable()
            {
               public void run()
               {
                  deleteRepositoryCache(MyJSQLView_Utils.getCacheDirectory() + repositoryName);
               }
            }, "PluginFrame.deleteRepositoryCacheThread");
            deleteRepositoryCacheThread.start();
         }

         // Remove from interface.
         repositoryHashtable.remove(repositoryName);
         centralTabsPane.removeTabAt(currentTabIndex);
         removeTabIndex--;
         addTabIndex--;
      }
   }

   //==============================================================
   // Class Method for removing the cache of a plugin repository.
   //==============================================================

   private void deleteRepositoryCache(String repositoryDirectoryString)
   {
      // Method Instances

      File cacheDirectory;
      File[] cacheContents;
      boolean fileDeleted, cacheClearFailure;

      // Setup
      cacheDirectory = new File(repositoryDirectoryString);
      cacheClearFailure = false;

      // See if cache exists
      if (cacheDirectory.exists() && cacheDirectory.isDirectory())
      {
         // Collect contents a delete.
         try
         {
            cacheContents = cacheDirectory.listFiles();

            int i = 0;
            while (i < cacheContents.length)
            {
               // File
               if (cacheContents[i].isFile())
               {
                  fileDeleted = cacheContents[i].delete();

                  if (!fileDeleted)
                     cacheClearFailure = true;
               }
               // Directory, Recursively Remove?
               // This is a dangerous piece of code & should
               // not be needed so not using.
               //else
               //   deleteRepositoryCache(cacheContents[i].getAbsolutePath());

               i++;
            }
            cacheClearFailure = !cacheDirectory.delete();
         }
         catch (SecurityException se)
         {
            if (MyJSQLView.getDebug())
               System.out.println("PluginFrame deleteRepositoryCache() "
                                  + "Failed to Clear Repository Cache: " + se.toString());
         }

         if (cacheClearFailure)
            if (MyJSQLView.getDebug())
               System.out.println("PluginFrame deleteRepositoryCache() "
                                  + "Failed to Clear Repository Cache.");
      }

   }

   //==============================================================
   // Class Method for adding a plugin repository. Provides a
   // dialog to collect information before actually activating
   // the routine to create the repository.
   //==============================================================

   private void addRepository()
   {
      // Method Instances
      JLabel repositoryNameLabel, repositoryURLLabel;
      JTextField repositoryNameTextField, repositoryURLTextField;

      String resource, resourceOK, resourceCancel;
      String resourceTitle, repositoryName;

      // Setup and display a option pane to collect the
      // repository name and location, url. Give it some
      // default input for help.
      resourceTitle = resourceBundle.getResourceString("PluginFrame.message.AddRepository", "Add Repository");
      
      resource = resourceBundle.getResourceString("PluginFrame.label.RepositoryName", "Repository Name");
      repositoryNameLabel = new JLabel(resource, JLabel.CENTER);

      repositoryNameTextField = new JTextField();
      repositoryNameTextField.setBorder(BorderFactory.createCompoundBorder(
         BorderFactory.createEtchedBorder(EtchedBorder.RAISED),
         BorderFactory.createLoweredBevelBorder()));
      repositoryNameTextField.setText(MYJSQLVIEW_REPOSITORY_NAME);

      resource = resourceBundle.getResourceString("PluginFrame.label.RepositoryURL", "Repository URL");
      repositoryURLLabel = new JLabel(resource, JLabel.CENTER);

      repositoryURLTextField = new JTextField();
      repositoryURLTextField.setBorder(BorderFactory.createCompoundBorder(
         BorderFactory.createEtchedBorder(EtchedBorder.RAISED), BorderFactory.createLoweredBevelBorder()));
      repositoryURLTextField.setText(MYJSQLVIEW_REPOSITORY);

      Object content[] = {repositoryNameLabel, repositoryNameTextField, repositoryURLLabel,
                          repositoryURLTextField};

      resourceOK = resourceBundle.getResourceString("PluginFrame.dialogbutton.OK", "OK");
      resourceCancel = resourceBundle.getResourceString("PluginFrame.dialogbutton.Cancel", "Cancel");

      InputDialog repositoryDialog = new InputDialog(null, resourceTitle, resourceOK, resourceCancel, content,
                                                     addRepositoryIcon);
      repositoryDialog.pack();
      repositoryDialog.setResizable(false);
      repositoryDialog.center();
      repositoryDialog.setVisible(true);

      // Collect the new repository information and try
      // adding to the plugin manager.

      if (repositoryDialog.isActionResult())
      {
         repositoryName = repositoryNameTextField.getText().trim();
         
         // Cleanup
         if (repositoryName.indexOf(" ") != -1)
            repositoryName = repositoryName.replaceAll("", " ");
         
         if (repositoryName.indexOf("//") != -1)
            repositoryName = repositoryName.replaceAll("//", "");
         
         if (repositoryName.indexOf("/") != -1)
            repositoryName = repositoryName.replaceAll("/", "");
         
         // Check if exists
         if (!repositoryHashtable.containsKey(repositoryName) && !repositoryName.isEmpty())
            createRepository(repositoryName, repositoryURLTextField.getText());
         else
         {
            centralTabsPane.setSelectedIndex(0);
            
            resource = resourceBundle.getResourceString("PluginFrame.dialogmessage.RepositoryExists",
                                                        "Repository Exists");
            JOptionPane.showMessageDialog(null, resource, resourceAlert, JOptionPane.ERROR_MESSAGE);
         }
      }
   }

   //==============================================================
   // Class Method for acutally creating the repositories tabs in
   // the frame.
   //==============================================================

   private void createRepository(String repositoryNameString, String repositoryURLString)
   {
      // Method Instances
      PluginRepository pluginRepository;
      PluginRepositoryPanel pluginRepositoryPanel;

      int addedTabIndex;

      if (repositoryNameString.length() != 0 || repositoryURLString.length() != 0)
      {
         // Limit Tab Name Length

         if (repositoryNameString.length() > 25)
            repositoryNameString = repositoryNameString.substring(0, 24);

         // Collect manage/repository tab index, by
         // selecting an insertion point.

         if (centralTabsPane.getTabCount() == 3)
            addedTabIndex = 1;
         else
            addedTabIndex = removeTabIndex;

         // Create an appropriate repository.

         if (repositoryURLString.toLowerCase().startsWith(PluginRepository.HTTP))
         {
            pluginRepository = new HTTP_PluginRepository();
         }
         else
         {
            pluginRepository = new FILE_PluginRepository();
         }

         // Setup the repository.
         pluginRepository.setName(repositoryNameString);

         if (pluginRepository.setRepository(repositoryURLString) == true)
         {  
            // Load up the tab with a predefined repository panel.

            pluginRepositoryPanel = new PluginRepositoryPanel(pluginRepository, this);
            pluginRepositoryPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory
                  .createEmptyBorder(4, 4, 4, 4), BorderFactory.createLoweredBevelBorder()));

            centralTabsPane.insertTab(pluginRepository.getName(), null, pluginRepositoryPanel,
               pluginRepository.getName(), addedTabIndex);

            // Manage tracking and indexing on tabs.

            repositoryHashtable.put(repositoryNameString, repositoryURLString);
            tabType = REPOSITORY;
            currentTabIndex = addedTabIndex;
            centralTabsPane.setSelectedIndex(addedTabIndex);

            removeTabIndex++;
            addTabIndex++;
         }
      }
   }
   
   //==============================================================
   // Class Method for removing a manually installed plugin from
   // the myjsqlview_plugin.conf file.
   //==============================================================

   public static synchronized void removePluginConfigurationModule(String pluginPathFileName)
   {
      // Method Instances
      String pluginConfigFileString;
      String currentLine;
      File configurationFile;
      FileReader fileReader;
      BufferedReader bufferedReader;
      StringBuffer newPluginConfigurationFileContents;
      
      MyJSQLView_ResourceBundle resourceBundle;
      String resource, resourceAlert;
      
      // Setup Resources
      resourceBundle = MyJSQLView.getResourceBundle();
      resourceAlert = resourceBundle.getResourceString("PluginFrame.dialogtitle.Alert", "Alert");
      
      // Create configuration file name for retrieval.
      pluginConfigFileString = MyJSQLView_Utils.getMyJSQLViewDirectory()
                               + MyJSQLView_Utils.getFileSeparator()
                               + MYJSQLVIEW_PLUGIN_CONFIGURATION_FILE;

      try
      {
         // Check to see if file exists.
         configurationFile = new File(pluginConfigFileString);

         try
         {
            // Nothing to do, no plugins installed manually.
            if (!configurationFile.exists())
               return;
         }
         catch (SecurityException e)
         {
            resource = resourceBundle.getResourceString("PluginFrame.dialogmessage.SecurityException",
                                                        "Security Exception");

            String optionPaneStringErrors = resource + " " + e;
            JOptionPane.showMessageDialog(null, optionPaneStringErrors, resourceAlert,
               JOptionPane.ERROR_MESSAGE);
            return;
         }

         // Looks like a plugin configuration exists so
         // remove the selected entry.

         fileReader = new FileReader(pluginConfigFileString);
         bufferedReader = new BufferedReader(fileReader);
         newPluginConfigurationFileContents = new StringBuffer();

         while ((currentLine = bufferedReader.readLine()) != null)
         {
            currentLine = currentLine.trim();
            if (currentLine.indexOf(pluginPathFileName) == -1)
               newPluginConfigurationFileContents.append(currentLine + "\n");
         }
         bufferedReader.close();
         fileReader.close();

         if (newPluginConfigurationFileContents.length() == 0)
         {
            boolean result = configurationFile.delete();
            if (!result)
               throw (new IOException("Failed to remove plugin configuration file."));
         }
         else
            WriteDataFile.mainWriteDataString(pluginConfigFileString,
                                              newPluginConfigurationFileContents.toString().getBytes(),
                                              false);
      }
      catch (IOException ioe)
      {
         resource = resourceBundle.getResourceString("PluginFrame.dialogmessage.FileI/OProblem",
                                                     "File I/O Problem");

         String optionPaneStringErrors = resource + " " + ioe;
         JOptionPane
               .showMessageDialog(null, optionPaneStringErrors, resourceAlert, JOptionPane.ERROR_MESSAGE);
      }
   }

   //==============================================================
   // Class Method for setting up the view of the current installed
   // plugins that includes the tabIcon, name, version, and a
   // remove icon. This is the manage tab.
   //==============================================================

   private JPanel createInstalledPluginsViewPanel(ArrayList<MyJSQLView_PluginModule> loadedPlugins)
   {
      // Class Method Instances
      JPanel pluginViewPanel;
      ArrayList<String> tableColumns;
      Font systemBoldFont;
      TableColumn tableColumn;
      JScrollPane tableScrollPane;
      String resource, resourceTabIcon, resourceRemove;

      // Setup the plugin items to be listed and columns
      // for the plugin table view.

      tableColumns = new ArrayList<String>();

      resourceTabIcon = resourceBundle.getResourceString("PluginFrame.label.TabIcon", "Tab Icon");
      tableColumns.add(resourceTabIcon);

      resource = resourceBundle.getResourceString("PluginFrame.label.Name", "Name");
      tableColumns.add(resource);

      resource = resourceBundle.getResourceString("PluginFrame.label.Version", "Version");
      tableColumns.add(resource);

      resourceRemove = resourceBundle.getResourceString("PluginFrame.label.Remove", "Remove");
      tableColumns.add(resourceRemove);

      // Collect the plugin data.

      generateLoadedPluginsList(loadedPlugins);

      // Create the plugin table view and scrollpane.

      loadedPluginsTableModel = new MyJSQLView_TableModel(tableColumns, loadedPluginTableData);
      loadedPluginsTable = new JTable(loadedPluginsTableModel);

      systemBoldFont = new Font(mainPanel.getFont().getName(), Font.BOLD, mainPanel.getFont().getSize());
      loadedPluginsTable.getTableHeader().setFont(systemBoldFont);
      loadedPluginsTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
      tableColumn = loadedPluginsTable.getColumnModel().getColumn(TABICON_COLUMN);
      tableColumn.setPreferredWidth(resourceTabIcon.length() - 10);
      tableColumn = loadedPluginsTable.getColumnModel().getColumn(REMOVE_COLUMN);
      tableColumn.setPreferredWidth(resourceTabIcon.length() - 10);
      loadedPluginsTable.getSelectionModel().addListSelectionListener(this);
      loadedPluginsTable.addMouseListener(this);

      tableScrollPane = new JScrollPane(loadedPluginsTable);

      pluginViewPanel = new JPanel(new GridLayout(1, 1, 0, 0));
      pluginViewPanel.setBorder(BorderFactory.createCompoundBorder(
                                                    BorderFactory.createEmptyBorder(4, 4, 4, 4),
                                                    BorderFactory.createLoweredBevelBorder()));
      pluginViewPanel.add(tableScrollPane);

      return pluginViewPanel;
   }
   
   //==============================================================
   // Class Method for inspecting the cache to determine if existing
   // established repostiories should be created.
   //==============================================================

   private void loadCachedRepositories()
   {
      // Method Instances
      File cacheDirectory, pathFile;
      File[] cacheContents;
      String path;
      
      // Setup
      cacheDirectory = new File(MyJSQLView_Utils.getCacheDirectory());
      
      // See if cache exists
      if (cacheDirectory.exists() && cacheDirectory.isDirectory())
      {
         try
         {
            cacheContents = cacheDirectory.listFiles();
            
            int i = 0;
            while (i < cacheContents.length)
            {
               if (cacheContents[i].isDirectory())
               {
                  // Don't load the default twice.
                  if (cacheContents[i].getName().equals(MYJSQLVIEW_REPOSITORY_NAME) &&
                      LOAD_DEFAULT_REPOSITORY)
                  {
                     i++;
                     continue;
                  }
                  
                  pathFile = new File(cacheContents[i].getAbsoluteFile() + fileSeparator
                                      + PluginRepository.REPOSITORY_PATH_FILE);
                  
                  if (pathFile.exists())
                  {
                     path = new String((ReadDataFile.mainReadDataString(pathFile.getAbsolutePath(), false)));
                     createRepository(cacheContents[i].getName(), path);  
                  }
               }
               i++;
            }
         }
         catch (SecurityException se)
         {
           if (MyJSQLView.getDebug()) 
              System.out.println("PluginFrame loadCachedRepositories() Failed to Load Repositories: "
                                 + se.toString());
         }
      }
   }

   //==============================================================
   // Class Method for setting up the view of the plugins that are
   // loading.
   //==============================================================

   private JPanel createLoadingPluginsViewPanel()
   {
      // Class Method Instances
      JPanel loadingViewPanel;
      ArrayList<String> tableColumns;
      Font systemBoldFont;
      TableColumn tableColumn;
      JScrollPane tableScrollPane;
      String resource, resourceTabIcon;

      // Setup the loading plugin items columns for the
      // loading plugin table view.

      tableColumns = new ArrayList<String>();

      resourceTabIcon = resourceBundle.getResourceString("PluginFrame.label.Status", "Status");
      tableColumns.add(resourceTabIcon);

      resource = resourceBundle.getResourceString("PluginFrame.label.Name", "Name");
      tableColumns.add(resource);

      // Collect the loading plugin data, should be
      // none when this method called from constructor.

      displayLoadingPluginsData();

      // Create the plugin table view and scrollpane.

      loadingPluginsTableModel = new MyJSQLView_TableModel(tableColumns, loadingPluginTableData);
      loadingPluginTable = new JTable(loadingPluginsTableModel);

      systemBoldFont = new Font(mainPanel.getFont().getName(), Font.BOLD, mainPanel.getFont().getSize());
      loadingPluginTable.getTableHeader().setFont(systemBoldFont);
      tableColumn = loadingPluginTable.getColumnModel().getColumn(TABICON_COLUMN);
      tableColumn.setMaxWidth(200);

      tableScrollPane = new JScrollPane(loadingPluginTable);

      loadingViewPanel = new JPanel(new GridLayout(1, 1, 0, 0));
      loadingViewPanel.add(tableScrollPane);

      return loadingViewPanel;
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
   // Class method to start the north animation panel.
   //==============================================================

   protected void startAnimation()
   {
      Thread northFillerPanelThread = new Thread(northFillerPanel, "PluginFrame Filler Panel");
      northFillerPanelThread.start();
   }
   
   //==============================================================
   // Class method to center the frame.
   //==============================================================

   protected void center()
   {
      Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
      Dimension us = getSize();
      int x = (screen.width - us.width) / 2;
      int y = (screen.height - us.height) / 2;
      setLocation(x, y);
   }
}
