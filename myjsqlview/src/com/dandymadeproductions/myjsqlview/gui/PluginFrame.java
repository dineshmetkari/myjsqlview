//=================================================================
//                 MyJSQLView Plugin Frame.
//=================================================================
//   This class provides a frame that is used to view, remove, and
// install new plugins to the MyJSQLView application.
//
//                   << PluginFrame.java >>
//
//=================================================================
// Copyright (C) 2005-2012 Dana M. Proctor
// Version 2.1 09/11/2012
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
//             
//-----------------------------------------------------------------
//                 danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
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
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.TableColumn;

import com.dandymadeproductions.myjsqlview.MyJSQLView;
import com.dandymadeproductions.myjsqlview.gui.panels.PluginFrameFillerPanel;
import com.dandymadeproductions.myjsqlview.io.WriteDataFile;
import com.dandymadeproductions.myjsqlview.plugin.FILE_PluginRepository;
import com.dandymadeproductions.myjsqlview.plugin.HTTP_PluginRepository;
import com.dandymadeproductions.myjsqlview.plugin.MyJSQLView_PluginModule;
import com.dandymadeproductions.myjsqlview.plugin.PluginLoader;
import com.dandymadeproductions.myjsqlview.plugin.PluginRepository;
import com.dandymadeproductions.myjsqlview.plugin.PluginRepositoryPanel;
import com.dandymadeproductions.myjsqlview.utilities.InputDialog;
import com.dandymadeproductions.myjsqlview.utilities.JarFileFilter;
import com.dandymadeproductions.myjsqlview.utilities.MyJSQLView_ResourceBundle;
import com.dandymadeproductions.myjsqlview.utilities.MyJSQLView_TableModel;
import com.dandymadeproductions.myjsqlview.utilities.MyJSQLView_Utils;

/**
 *    The PluginFrame class provides a frame that is used to view, remove,
 * and install new plugins to the MyJSQLView application.
 * 
 * @author Dana M. Proctor
 * @version 2.1 09/11/2012
 */

//=================================================================
//                   MyJSQLView PluginFrame
//=================================================================

class PluginFrame extends JFrame implements ActionListener, ChangeListener, MouseListener
{
   // Creation of the necessary class instance
   
   private static final long serialVersionUID = 6203223580678904034L;

   private MyJSQLView_Frame parentFrame;
   private JPanel mainPanel;
   private JTabbedPane manageTabsPane;
   private MyJSQLView_ResourceBundle resourceBundle;
   private ImageIcon statusWorkingIcon, deleteRepositoryIcon, defaultModuleIcon, removeIcon;
   private JButton installButton, closeButton;
   
   private Hashtable<String, String> repositoryHashtable;
   private ArrayList<String> loadingPluginsList;
   private Object[][] pluginViewTableData;
   private Object[][] loadingPluginViewTableData;
   private MyJSQLView_TableModel tableModelPlugins;
   private MyJSQLView_TableModel tableModelLoadingPlugins;
   private JTable pluginViewTable, loadingPluginViewTable;
   private String fileSeparator, iconsDirectory, lastPluginDirectory;
   private String resourceAlert;
   
   private String tabType, repositoryType;
   private int currentTabIndex, removeTabIndex, addTabIndex;
   
   private static final String MANAGE = "manage";
   private static final String REPOSITORY = "repository";
   
   private static final int TABICON_COLUMN = 0;
   private static final int NAME_COLUMN = 1;
   private static final int VERSION_COLUMN = 2;
   private static final int REMOVE_COLUMN = 3;
   
   private static final String MYJSQLVIEW_REPOSITORY_NAME = "MyJSQLView.org";
   private static final String MYJSQLVIEW_REPOSITORY = "http://myjsqlview.org/temp/";
   
   //==============================================================
   // PluginFrame Constructor
   //==============================================================

   protected PluginFrame(MyJSQLView_Frame parent)
   {
      parentFrame = parent;
      
      // Constructor Instances.
      JPanel northFillerPanel, southButtonPanel;
      JPanel pluginViewPanel, loadingViewPanel;
      
      ImageIcon plusIcon, minusIcon;
      String resource;

      // Setting up resources & instances.
      
      resourceBundle = MyJSQLView.getResourceBundle();
      
      fileSeparator = MyJSQLView_Utils.getFileSeparator();
      iconsDirectory = MyJSQLView_Utils.getIconsDirectory() + fileSeparator;
      lastPluginDirectory = "";
      
      resourceAlert = resourceBundle.getResourceString("PluginFrame.dialogtitle.Alert", "Alert");
      statusWorkingIcon = resourceBundle.getResourceImage(iconsDirectory + "statusWorkingIcon.png");
      removeIcon = resourceBundle.getResourceImage(iconsDirectory + "removeIcon.png");
      deleteRepositoryIcon = resourceBundle.getResourceImage(iconsDirectory + "deleteDataIcon.gif");
      defaultModuleIcon = resourceBundle.getResourceImage(iconsDirectory + "newsiteLeafIcon.png");
      
      repositoryHashtable = new Hashtable <String, String>();
      loadingPluginsList = new ArrayList <String>();
      currentTabIndex = 0;
      
      // Setting the frame's title, main panel, & window listener.
      
      resource = resourceBundle.getResourceString("PluginFrame.message.Title", "Plugin Management");
      setTitle(resource);
      
      mainPanel = new JPanel(new BorderLayout());
      mainPanel.setBorder(BorderFactory.createRaisedBevelBorder());
      
      this.addWindowListener(pluginFrameListener);
      
      // ======================================================
      // Animated Filler Panel.
      
      northFillerPanel = new PluginFrameFillerPanel();
      mainPanel.add(northFillerPanel, BorderLayout.NORTH);
      
      // ======================================================
      // Central Area for Manager, Repositories.
      
      manageTabsPane = new JTabbedPane();
      manageTabsPane.setTabPlacement(JTabbedPane.TOP);
      manageTabsPane.setBorder(BorderFactory.createLoweredBevelBorder());
      manageTabsPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
      
      // ======================================================
      // Placement of Components in Tabbed Pane
      
      // Manage plugins tab, show exiting and removal option.
      
      pluginViewPanel = createInstalledPluginsViewPanel(MyJSQLView_Frame.getPlugins());
      resource = resourceBundle.getResourceString("PluginFrame.tab.Manage", "Manage");
      manageTabsPane.addTab(resource, null, pluginViewPanel, resource);   
      
      // Default MyJSQLView repository.
      
      createDefaultMyJSQLViewRepository();
      
      // Additional repositories as defined by configuration file.
      
      
      
      // Repository removal/addition control mechanism.
      
      minusIcon = resourceBundle.getResourceImage(iconsDirectory + "minusIcon.png");
      resource = resourceBundle.getResourceString("PluginFrame.tab.RemoveRepository", "Remove Repository");
      manageTabsPane.addTab(null, minusIcon, new JPanel(), resource);
      removeTabIndex = manageTabsPane.getTabCount() - 1;
      
      plusIcon = resourceBundle.getResourceImage(iconsDirectory + "plusIcon.png");
      resource = resourceBundle.getResourceString("PluginFrame.tab.AddRepository", "Add Repository");
      manageTabsPane.addTab(null, plusIcon, new JPanel(), resource);
      addTabIndex = manageTabsPane.getTabCount() - 1;
      
      // ======================================================
      // Loading/Status view panel.
      
      loadingViewPanel = createLoadingPluginsViewPanel();
      
      // ======================================================
      // Split pane for the tabbed pane, manage/repositories.
      // and status view panel. 
      
      JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, manageTabsPane, loadingViewPanel);
      splitPane.setOneTouchExpandable(true);
      splitPane.setDividerLocation(200);
      
      mainPanel.add(splitPane, BorderLayout.CENTER);
      
      // ======================================================
      // Buttons to install plugins &  close down the frame.
      
      southButtonPanel = new JPanel();
      southButtonPanel.setBorder(BorderFactory.createEtchedBorder());
      
      resource = resourceBundle.getResourceString("PluginFrame.button.Install", "Install");
      installButton = new JButton(resource);
      installButton.setFocusPainted(false);
      installButton.addActionListener(this);
      southButtonPanel.add(installButton);

      resource = resourceBundle.getResourceString("PluginFrame.button.Close", "Close");
      closeButton = new JButton(resource);
      closeButton.setFocusPainted(false);
      closeButton.addActionListener(this);
      southButtonPanel.add(closeButton);
      
      mainPanel.add(southButtonPanel, BorderLayout.SOUTH);
      
      // ======================================================
      // Finish up.
      
      manageTabsPane.addChangeListener(this);
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
      Object frameSource = evt.getSource();

      // Overall action buttons.
      if (frameSource instanceof JButton)
      {
         // Install button action.
         if (frameSource == installButton)
         {
            installPlugin(this);
         }
         // MyJSQLView_Frame Tab Addition Notification
         // for New Plugin Install.
         else if (frameSource == MyJSQLView_Frame.pluginFrameListenButton)
         {
            // Update plugins list.
            MyJSQLView_Frame.pluginFrameListenButton.removeActionListener(this);
            displayLoadedPluginsData(MyJSQLView_Frame.getPlugins());
            tableModelPlugins.setValues(pluginViewTableData);
            
            // Update loading plugins list.
            displayLoadingPluginsData();
            tableModelLoadingPlugins.setValues(loadingPluginViewTableData);
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
      
      if (changeSource != null && (JTabbedPane) changeSource == manageTabsPane)
      {
         // Obtain some parameters to be used & disable
         // tabbed pane activity.
         
         selectedIndex = ((JTabbedPane) changeSource).getSelectedIndex();
         manageTabsPane.removeChangeListener(this);
         
         if (selectedIndex > manageTabsPane.getTabCount())
            return;
         
         // Record Manage Tab Selected 
         if (selectedIndex == 0)
         {
            tabType = MANAGE;
            currentTabIndex = 0;
         }
         // Record Repository Tab Selected
         else if (selectedIndex > 0 && selectedIndex < removeTabIndex)
         {
            tabType = REPOSITORY;
            currentTabIndex = selectedIndex;
         }
         // Remove Repository
         else if (selectedIndex == removeTabIndex)
         {  
            if (manageTabsPane.getTabCount() > 3 && currentTabIndex != 0)
               removeRepository();
            
            tabType = MANAGE;
            manageTabsPane.setSelectedIndex(0);
            currentTabIndex = 0;         
         }
         // Add Repository
         else
         {
            addRepository();
            
            
            
            // currentTabIndex = ?
         }
         manageTabsPane.addChangeListener(this);
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
      tableRow = pluginViewTable.rowAtPoint(coordinatePoint);
      tableColumn = pluginViewTable.columnAtPoint(coordinatePoint);

      if (tableRow >= pluginViewTable.getRowCount() || tableRow < 0)
         return;
      else
      {
         // Remove Plugin Action
         if (tableColumn == REMOVE_COLUMN)
         {
            removePluginConfigurationModule(tableRow);
            MyJSQLView_Frame.removeTab(tableRow);
            displayLoadedPluginsData(MyJSQLView_Frame.getPlugins());
            tableModelPlugins.setValues(pluginViewTableData);
         }
      }
   }
   
   //==============================================================
   // Classs Method to aquire a local file system plugin file to
   // be installed.
   //==============================================================
   
   private void installPlugin(JFrame parent)
   {
      // Method Instances.
      JFileChooser pluginFileChooser;
      String fileName, resource;

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

         // Add the module to the loading list & try Loading
         // the module.
         if (!fileName.equals(""))
         {
            loadingPluginsList.add(fileName);
            displayLoadingPluginsData();
            tableModelLoadingPlugins.setValues(loadingPluginViewTableData);
            
            MyJSQLView_Frame.pluginFrameListenButton.addActionListener(this);
            
            try
            {
               new PluginLoader(parentFrame, new URL(fileName));
            }
            catch (MalformedURLException mfe)
            {
               resource = resourceBundle.getResourceString(
                  "PluginFrame.dialogmessage.FailedToCreateURL", "Failed to Create URL");
               
               JOptionPane.showMessageDialog(null, resource + "\n" + mfe.toString(),
                                             resourceAlert, JOptionPane.ERROR_MESSAGE);
            }
         }
         else
         {
            String optionPaneStringErrors;
            
            optionPaneStringErrors = resourceBundle.getResourceString(
               "PluginFrame.dialogmessage.FileNOTFile", "File NOT Found");
            
            JOptionPane.showMessageDialog(null, optionPaneStringErrors,
                                          resourceAlert, JOptionPane.ERROR_MESSAGE);
         }
      }
   }
      
   //==============================================================
   // Class Method for loading the plugin modules data, tab icon,
   // name, etc. in the manage tab view table.
   //==============================================================

   private void displayLoadedPluginsData(ArrayList<MyJSQLView_PluginModule> loadedPlugins)
   {
      // Method Instances.
      String path;
      
      pluginViewTableData = new Object[loadedPlugins.size()][4];
      
      for (int i = 0; i < loadedPlugins.size(); i++)
      {
         // Plugin tab icon, name, version and remove element.
         
         if (loadedPlugins.get(i).getTabIcon() == null)
            pluginViewTableData[i][TABICON_COLUMN] = defaultModuleIcon;
         else
            pluginViewTableData[i][TABICON_COLUMN] = loadedPlugins.get(i).getTabIcon();
         
         pluginViewTableData[i][NAME_COLUMN] = loadedPlugins.get(i).getName();
         pluginViewTableData[i][VERSION_COLUMN] = loadedPlugins.get(i).getVersion();
         pluginViewTableData[i][REMOVE_COLUMN] = removeIcon;
         
         // Remove plugins from loading list.
         path = loadedPlugins.get(i).getPath_FileName().substring(0,
                loadedPlugins.get(i).getPath_FileName().indexOf("<$$$>"));
         
         if (loadingPluginsList.contains(path))
            loadingPluginsList.remove(path);
      }
   }
   
   //==============================================================
   // Class Method for updating the status data for current loading
   // plugin modules. Includes status and name. 
   //==============================================================

   private void displayLoadingPluginsData()
   {
      loadingPluginViewTableData = new Object[loadingPluginsList.size()][2];
      
      for (int i = 0; i < loadingPluginsList.size(); i++)
      {
         // Loading plugin status indicator, & name.   
         loadingPluginViewTableData[i][TABICON_COLUMN] = statusWorkingIcon;
         loadingPluginViewTableData[i][NAME_COLUMN] = loadingPluginsList.get(i);
      }
   }
   
   //==============================================================
   // Class Method for removing a plugin repository.
   //==============================================================

   private void removeRepository()
   {
      // Method Instances
      JLabel message;
      InputDialog deleteDialog;
      String  resourceMessage, resourceCancel, resourceOK;

      // Confirming really want to delete dialog.
      
      resourceMessage = resourceBundle.getResourceString("PluginFrame.message.DeleteRepository",
                                                         "Delete Repository?");
      message = new JLabel(resourceMessage, JLabel.CENTER);
      message.setFont(new Font("DIALOG", Font.BOLD, 14));
      message.setForeground(Color.RED);
      Object[] content = {message};
      
      resourceCancel = resourceBundle.getResourceString("PluginFrame.dialogbutton.Cancel", "Cancel");
      resourceOK = resourceBundle.getResourceString("PluginFrame.dialogbutton.OK", "OK");

      deleteDialog = new InputDialog(null, resourceAlert, resourceOK, resourceCancel, content,
                                     deleteRepositoryIcon);
      deleteDialog.pack();
      deleteDialog.center();
      deleteDialog.setResizable(false);
      deleteDialog.setVisible(true);

      // Deleting
      
      if (deleteDialog.isActionResult())
      {
         repositoryHashtable.remove(manageTabsPane.getTitleAt(currentTabIndex));
         manageTabsPane.removeTabAt(currentTabIndex);
         removeTabIndex--;
         addTabIndex--;
      }
   }
   
   //==============================================================
   // Class Method for adding a plugin repository, these are not
   // local file systems resources.
   //==============================================================

   private void addRepository()
   {
      // Method Instances
      JLabel repositoryNameLabel, repositoryURLLabel;
      JTextField repositoryNameTextField, repositoryURLTextField;
      
      PluginRepository pluginRepository;
      PluginRepositoryPanel pluginRepositoryPanel;
      String repositoryNameString, repositoryURLString;
      String resource, resourceOK, resourceCancel;
      
      int addedTabIndex;
      
      // Collect manage/repository tab index, by
      // selecting an insertion point.
      
      if (manageTabsPane.getTabCount() == 3)
         addedTabIndex = 1;
      else
         addedTabIndex = removeTabIndex;
      
      // Setup and display a option pane to collect the
      // repository name and location, url.
      
      resource = resourceBundle.getResourceString("PluginFrame.label.RepositoryName", "RepositoryName");
      repositoryNameLabel = new JLabel(resource, JLabel.CENTER);
      
      repositoryNameTextField = new JTextField();
      repositoryNameTextField.setText("MyJSQLView");
      
      resource = resourceBundle.getResourceString("PluginFrame.label.RepositoryURL", "RepositoryURL");
      repositoryURLLabel = new JLabel(resource, JLabel.CENTER);
      
      repositoryURLTextField = new JTextField();
      repositoryURLTextField.setText("http://myjsqlview.org");
      
      Object content[] = {repositoryNameLabel, repositoryNameTextField, repositoryURLLabel,
                          repositoryURLTextField};
      
      resourceOK = resourceBundle.getResourceString("PluginFrame.button.OK", "OK");
      resourceCancel = resourceBundle.getResourceString("PluginFrame.button.Cancel", "Cancel");
      
      InputDialog repositoryDialog = new InputDialog(null, resource, resourceOK, resourceCancel,
                                                  content, null);
      repositoryDialog.pack();
      repositoryDialog.setResizable(false);
      repositoryDialog.center();
      repositoryDialog.setVisible(true);
      
      // Collect the new repository information and try
      // adding to the plugin manager.

      if (repositoryDialog.isActionResult())
      {
         // Collect and see if a valid input given for
         // a name and URL/Location.
         
         repositoryNameString = repositoryNameTextField.getText();
         repositoryURLString = repositoryURLTextField.getText();
         
         if (repositoryNameString.length() != 0 || repositoryURLString.length() != 0)
         {
            if (repositoryNameString.length() > 25)
               repositoryNameString = repositoryNameString.substring(0, 24);
            
            // Create an appropriate repository.
            
            if (repositoryURLString.indexOf(PluginRepository.HTTP) != -1)
            {
               pluginRepository = new HTTP_PluginRepository();  
            }
            else
            {
               pluginRepository = new FILE_PluginRepository();
            }
               
            pluginRepository.setRepository(repositoryURLString);
            pluginRepository.setName(repositoryNameString);
            
            // Load up the tab with a predefined repository panel.
            
            pluginRepositoryPanel = new PluginRepositoryPanel(pluginRepository.getPluginItems());
            manageTabsPane.insertTab(pluginRepository.getName(), null, pluginRepositoryPanel,
                                     pluginRepository.getName(), addedTabIndex);
               
            // Manage tracking and indexing on tabs.
            
            repositoryHashtable.put(repositoryNameString, repositoryURLString);
            tabType = REPOSITORY;
            currentTabIndex = addedTabIndex;
            manageTabsPane.setSelectedIndex(addedTabIndex);
               
            removeTabIndex++;
            addTabIndex++;
         }  
      }
   }
   
   //==============================================================
   // Class Method for removing a manually installed plugin from
   // the myjsqlview_plugin.conf file.
   //==============================================================

   private void removePluginConfigurationModule(int index)
   {
      // Method Instances
      String pluginConfigurationFileName = "myjsqlview_plugin.conf";
      String pluginConfigFileString;
      String currentLine, pluginPathFileName;
      File configurationFile;
      FileReader fileReader;
      BufferedReader bufferedReader;
      StringBuffer newPluginConfigurationFileContents;
      String resource;
      
      // Create configuration file name for retrieval.
      pluginConfigFileString = MyJSQLView_Utils.getMyJSQLViewDirectory()
                               + MyJSQLView_Utils.getFileSeparator() + pluginConfigurationFileName;
      
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
            JOptionPane.showMessageDialog(null, optionPaneStringErrors,
                                          resourceAlert, JOptionPane.ERROR_MESSAGE);
            return;
         }
         
         // Looks like a plugin configuration exists so
         // remove the selected entry.
         
         fileReader = new FileReader(pluginConfigFileString);
         bufferedReader = new BufferedReader(fileReader);
         newPluginConfigurationFileContents = new StringBuffer();
         pluginPathFileName = MyJSQLView_Frame.getPlugins().get(index).getPath_FileName();
            
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
               throw(new IOException("Failed to remove plugin configuration file."));
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
         JOptionPane.showMessageDialog(null, optionPaneStringErrors,
                                       resourceAlert, JOptionPane.ERROR_MESSAGE);
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
      
      displayLoadedPluginsData(loadedPlugins);
          
      // Create the plugin table view and scrollpane.
      
      tableModelPlugins = new MyJSQLView_TableModel(tableColumns, pluginViewTableData);
      pluginViewTable = new JTable(tableModelPlugins);
      
      systemBoldFont = new Font(mainPanel.getFont().getName(), Font.BOLD, mainPanel.getFont().getSize());
      pluginViewTable.getTableHeader().setFont(systemBoldFont);
      pluginViewTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
      tableColumn = pluginViewTable.getColumnModel().getColumn(TABICON_COLUMN);
      tableColumn.setPreferredWidth(resourceTabIcon.length() - 10);
      tableColumn = pluginViewTable.getColumnModel().getColumn(REMOVE_COLUMN);
      tableColumn.setPreferredWidth(resourceTabIcon.length() - 10);
      pluginViewTable.addMouseListener(this);
      
      tableScrollPane = new JScrollPane(pluginViewTable);
      
      pluginViewPanel = new JPanel(new GridLayout(1, 1, 0, 0));
      pluginViewPanel.setBorder(BorderFactory.createLoweredBevelBorder());
      pluginViewPanel.add(tableScrollPane);
      
      return pluginViewPanel;
   }
   
   //==============================================================
   // Class Method for setting up the view of the current installed
   // plugins that includes the tabIcon, name, version, and a 
   // remove icon. This is the manage tab.
   //==============================================================

   private void createDefaultMyJSQLViewRepository()
   {
      // Method Instances
      PluginRepository pluginRepository;
      PluginRepositoryPanel pluginRepositoryPanel;
      
      // Set repository & load.
      pluginRepository = new HTTP_PluginRepository();     
      pluginRepository.setRepository(MYJSQLVIEW_REPOSITORY);
      pluginRepository.setName(MYJSQLVIEW_REPOSITORY_NAME);
      
      pluginRepositoryPanel = new PluginRepositoryPanel(pluginRepository.getPluginItems());
      manageTabsPane.addTab(pluginRepository.getName(), null, pluginRepositoryPanel,
                            pluginRepository.getName());
                               
         
      
      /*
      ArrayList<String> myjsqlviewRepositoryList = new ArrayList <String>();
      myjsqlviewRepositoryList.add("TableFieldProfiler.jar");
      myjsqlviewRepositoryList.add("QueryBuilder.jar");
      PluginRepositoryPanel myjsqlviewRepositoryPanel = new PluginRepositoryPanel(myjsqlviewRepositoryList);
      manageTabsPane.addTab("MyJSQLView", null, myjsqlviewRepositoryPanel, "MyJSQLView");
      */
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
      
      tableModelLoadingPlugins = new MyJSQLView_TableModel(tableColumns, loadingPluginViewTableData);
      loadingPluginViewTable = new JTable(tableModelLoadingPlugins);
      
      systemBoldFont = new Font(mainPanel.getFont().getName(), Font.BOLD, mainPanel.getFont().getSize());
      loadingPluginViewTable.getTableHeader().setFont(systemBoldFont);
      tableColumn = loadingPluginViewTable.getColumnModel().getColumn(TABICON_COLUMN);
      tableColumn.setMaxWidth(200);
      
      tableScrollPane = new JScrollPane(loadingPluginViewTable);
      
      loadingViewPanel = new JPanel(new GridLayout(1, 1, 0, 0));
      loadingViewPanel.setBorder(BorderFactory.createLoweredBevelBorder());
      loadingViewPanel.add(tableScrollPane);
      
      return loadingViewPanel;
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