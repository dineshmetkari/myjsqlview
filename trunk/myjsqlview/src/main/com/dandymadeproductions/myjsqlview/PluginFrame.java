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
// Version 1.9 08/06/2012
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
//             
//-----------------------------------------------------------------
//                 danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview;

import java.awt.*;
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
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.table.TableColumn;
import javax.swing.JTable;

/**
 *    The PluginFrame class provides a frame that is used to view, remove,
 * and install new plugins to the MyJSQLView application.
 * 
 * @author Dana M. Proctor
 * @version 1.9 08/06/2012
 */

//=================================================================
//                   MyJSQLView PluginFrame
//=================================================================

class PluginFrame extends JFrame implements ActionListener, MouseListener
{
   // Creation of the necessary class instance
   
   private static final long serialVersionUID = 6203223580678904034L;

   private MyJSQLView_Frame parentFrame;
   private JPanel mainPanel;
   private MyJSQLView_ResourceBundle resourceBundle;
   private ImageIcon statusWorkingIcon, defaultModuleIcon, removeIcon;
   private JButton installButton, closeButton;
   
   private ArrayList<String> loadingPluginsList;
   private Object[][] pluginViewTableData;
   private Object[][] loadingPluginViewTableData;
   private MyJSQLView_TableModel tableModelPlugins;
   private MyJSQLView_TableModel tableModelLoadingPlugins;
   private JTable pluginViewTable, loadingPluginViewTable;
   private String fileSeparator, lastPluginDirectory;
   private String resourceAlert;
   
   private static final int TABICON_COLUMN = 0;
   private static final int NAME_COLUMN = 1;
   private static final int VERSION_COLUMN = 2;
   private static final int REMOVE_COLUMN = 3;
   
   //==============================================================
   // PluginFrame Constructor
   //==============================================================

   protected PluginFrame(MyJSQLView_Frame parent)
   {
      parentFrame = parent;
      
      // Constructor Instances.
      JPanel northInstallPanel, southButtonPanel;
      JPanel pluginViewPanel, loadingViewPanel;
      String iconsDirectory, resource;

      // Setting up resources & instances.
      
      resourceBundle = MyJSQLView.getResourceBundle();
      iconsDirectory = MyJSQLView_Utils.getIconsDirectory() + MyJSQLView_Utils.getFileSeparator();
      
      statusWorkingIcon = new ImageIcon(iconsDirectory + "statusWorkingIcon.png");
      removeIcon = new ImageIcon(iconsDirectory + "removeIcon.png");
      defaultModuleIcon = new ImageIcon(iconsDirectory + "newsiteLeafIcon.png");
      
      loadingPluginsList = new ArrayList <String>();
      fileSeparator = MyJSQLView_Utils.getFileSeparator();
      lastPluginDirectory = "";
      
      resourceAlert = resourceBundle.getResourceString("PluginFrame.dialogtitle.Alert", "Alert");
      
      // Setting the frame's title layout and main panel.
      
      resource = resourceBundle.getResourceString("PluginFrame.message.Title", "Plugin Management");
      setTitle(resource);
      
      mainPanel = new JPanel(new BorderLayout());
      mainPanel.setBorder(BorderFactory.createRaisedBevelBorder());
      
      // ======================================================
      // New Plugin install option components.
      
      northInstallPanel = new JPanel()
      {
         private static final long serialVersionUID = -2724112697915703694L;
         private String imageFileName = "images" + fileSeparator + "pluginframe.jpg";
         private Image backgroundImage = new ImageIcon(imageFileName).getImage();
         
         public void paintComponent(Graphics g)
         {
            super.paintComponent(g);
            g.drawImage(backgroundImage, 0, 0, this);
         }
      };
      northInstallPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
      northInstallPanel.setBorder(BorderFactory.createRaisedBevelBorder());
      
      resource = resourceBundle.getResourceString("PluginFrame.button.Install", "Install");
      installButton = new JButton(resource);
      installButton.setFocusPainted(false);
      installButton.addActionListener(this);
      northInstallPanel.add(installButton);
      
      mainPanel.add(northInstallPanel, BorderLayout.NORTH);
      
      // ======================================================
      // Installed plugin view, and loading view objects.
      
      pluginViewPanel = createInstalledPluginsViewPanel(MyJSQLView_Frame.getPlugins());
      loadingViewPanel = createLoadingPluginsViewPanel();
      
      JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, pluginViewPanel, loadingViewPanel);
      splitPane.setOneTouchExpandable(true);
      splitPane.setDividerLocation(200);
      
      mainPanel.add(splitPane, BorderLayout.CENTER);
      
      // ======================================================
      // Button to close down the frame.
      
      southButtonPanel = new JPanel();
      southButtonPanel.setBorder(BorderFactory.createEtchedBorder());

      resource = resourceBundle.getResourceString("PluginFrame.button.Close", "Close");
      closeButton = new JButton(resource);
      closeButton.setFocusPainted(false);
      closeButton.addActionListener(this);
      southButtonPanel.add(closeButton);
      
      mainPanel.add(southButtonPanel, BorderLayout.SOUTH);

      getContentPane().add(mainPanel);
      northInstallPanel.validate();
      this.addWindowListener(pluginFrameListener);
   }

   //==============================================================
   // WindowListener for insuring that the frame is closed. (x).
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
   // ActionEvent Listener method for determining when the selections
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
         // MyJSQLView_Frame Tab Addition Notification.
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
   // Collects the row & column selected and then used to remove
   // the selected Plugin Module.
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
   // Class Method for setting up the view of the current installed
   // plugins that includes the tabIcon, name, version, and a 
   // remove icon.
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
   // Class Method for loading the plugin modules data, tab icon,
   // name, etc. in the view table.
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
   // Class Method for loading the loading plugin modules name. 
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