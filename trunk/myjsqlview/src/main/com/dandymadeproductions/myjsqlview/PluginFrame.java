//=================================================================
//                 MyJSQLView Plugin Frame.
//=================================================================
//   This class provides a frame that is used to view, remove, and
// install new plugins to the MyJSQLView application.
//
//                   << PluginFrame.java >>
//
//=================================================================
// Copyright (C) 2005-2010 Dana M. Proctor
// Version 1.1 08/09/2010
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
//             
//-----------------------------------------------------------------
//                 danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/**
 *    The PluginFrame class provides a frame that is used to view, remove,
 * and install new plugins to the MyJSQLView application.
 * 
 * @author Dana M. Proctor
 * @version 1.1 08/09/2010
 */

//=================================================================
//                   MyJSQLView PluginFrame
//=================================================================

class PluginFrame extends JFrame implements ActionListener
{
   // Creation of the necessary class instance
   
   private static final long serialVersionUID = 6203223580678904034L;

   private JPanel mainPanel;
   private MyJSQLView_ResourceBundle resourceBundle;
   private ImageIcon defaultModuleIcon, removeIcon;
   private JButton installButton, closeButton;
   
   private Object[][] pluginViewTableData;

   //==============================================================
   // PluginFrame Constructor
   //==============================================================

   protected PluginFrame()
   {
      // Constructor Instances.
      JPanel northInstallPanel, southButtonPanel;
      Vector<MyJSQLView_PluginModule> loadedPlugins;
      String resource;

      // Setting up resources.
      
      resourceBundle = MyJSQLView.getLocaleResourceBundle();
      
      // Setting the frame's title layout and main panel.
      
      resource = resourceBundle.getResource("PluginFrame.message.Title");
      if (resource.equals(""))
         setTitle("Plugin Management");
      else
         setTitle(resource);
      
      mainPanel = new JPanel(new BorderLayout());
      mainPanel.setBorder(BorderFactory.createRaisedBevelBorder());
      
      // ======================================================
      // New Plugin install option components.
      
      northInstallPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
      northInstallPanel.setBorder(BorderFactory.createEtchedBorder());
      
      resource = resourceBundle.getResource("PluginFrame.button.Install");
      if (resource.equals(""))
         installButton = new JButton("Install");
      else
         installButton = new JButton(resource);
      installButton.setFocusPainted(false);
      installButton.addActionListener(this);
      northInstallPanel.add(installButton);
      
      mainPanel.add(northInstallPanel, BorderLayout.NORTH);
      
      // ======================================================
      // Installed plugin view and removal objects.
      
      loadedPlugins = MyJSQLView_Frame.getPlugins();
      createInstalledPluginsViewPanel(loadedPlugins);
      
      // ======================================================
      // Button to close down the frame.
      
      southButtonPanel = new JPanel();
      southButtonPanel.setBorder(BorderFactory.createEtchedBorder());

      resource = resourceBundle.getResource("PluginFrame.button.Close");
      if (resource.equals(""))
         closeButton = new JButton("Close");
      else
         closeButton = new JButton(resource);
      closeButton.setFocusPainted(false);
      closeButton.addActionListener(this);
      southButtonPanel.add(closeButton);
      
      mainPanel.add(southButtonPanel, BorderLayout.SOUTH);

      getContentPane().add(mainPanel);
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

      public void windowDeiconified(WindowEvent e)
      {
         
      }

      public void windowIconified(WindowEvent e)
      {
         
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
            System.out.println("Installing New Plugin");
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
   // Class Method for setting up the view of the current installed
   // plugins that includes the tabIcon, name, version, and a 
   // remove icon.
   //==============================================================

   private void createInstalledPluginsViewPanel(Vector<MyJSQLView_PluginModule> loadedPlugins)
   {
      // Class Method Instances
      JPanel pluginViewPanel;
      Vector<String> tableColumns;
      MyJSQLView_TableModel tableModel;
      JTable pluginViewTable;
      Font systemBoldFont;
      JScrollPane tableScrollPane;
      
      String resource, resourceTabIcon, resourceRemove;
      String iconsDirectory;

      // Setup the plugin items to be listed and columns
      // for the plugin table view.

      tableColumns = new Vector<String>();
      
      resourceTabIcon = resourceBundle.getResource("PluginFrame.label.TabIcon");
      if (resourceTabIcon.equals(""))
         tableColumns.add("Tab Icon");
      else
         tableColumns.add(resourceTabIcon);
      
      resource = resourceBundle.getResource("PluginFrame.label.Name");
      if (resource.equals(""))
         tableColumns.add("Name");
      else
         tableColumns.add(resource);
      
      resource = resourceBundle.getResource("PluginFrame.label.Version");
      if (resource.equals(""))
         tableColumns.add("Version");
      else
         tableColumns.add(resource);
      
      resourceRemove = resourceBundle.getResource("PluginFrame.label.Remove");
      if (resource.equals(""))
         tableColumns.add("Remove");
      else
         tableColumns.add(resourceRemove);
      
      // Fill the plugin view table with data.
      
      pluginViewTableData = new Object[loadedPlugins.size()][4];
      
      iconsDirectory = MyJSQLView_Utils.getIconsDirectory() + MyJSQLView_Utils.getFileSeparator();
      removeIcon = new ImageIcon(iconsDirectory + "removeIcon.png");
      defaultModuleIcon = new ImageIcon(iconsDirectory + "newsiteLeafIcon.png");
       
      for (int i = 0; i < loadedPlugins.size(); i++)
      {
         // Plugin tab icon, name, version and remove element.
         
         if (loadedPlugins.get(i).getTabIcon() == null)
            pluginViewTableData[i][0] = defaultModuleIcon;
         else
            pluginViewTableData[i][0] = loadedPlugins.get(i).getTabIcon();
         
         pluginViewTableData[i][1] = loadedPlugins.get(i).getName();
         pluginViewTableData[i][2] = loadedPlugins.get(i).getVersion();
         pluginViewTableData[i][3] = removeIcon;
      }
         
      // Create the plugin table view and scrollpane.
      
      tableModel = new MyJSQLView_TableModel(tableColumns, pluginViewTableData);
      pluginViewTable = new JTable(tableModel);
      
      systemBoldFont = new Font(mainPanel.getFont().getName(), Font.BOLD, mainPanel.getFont().getSize());
      pluginViewTable.getTableHeader().setFont(systemBoldFont);
      //pluginViewTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
      pluginViewTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
      
      pluginViewTable.getColumnModel().getColumn(0).setPreferredWidth(resourceTabIcon.length() - 10);
      pluginViewTable.getColumnModel().getColumn(3).setPreferredWidth(resourceTabIcon.length() - 10);
      
      //pluginViewTable.addMouseListener(this);
      
      tableScrollPane = new JScrollPane(pluginViewTable);
      
      pluginViewPanel = new JPanel(new GridLayout(1, 1, 0, 0));
      pluginViewPanel.setBorder(BorderFactory.createEtchedBorder());
      pluginViewPanel.add(tableScrollPane);
      mainPanel.add(pluginViewPanel, BorderLayout.CENTER);
   }
   
   //==============================================================
   // Class Method for helping the parameters in gridbag.
   //==============================================================
   /*
   private void buildConstraints(GridBagConstraints gbc, int gx, int gy, int gw,
                                 int gh, double wx, double wy)
   {
      gbc.gridx = gx;
      gbc.gridy = gy;
      gbc.gridwidth = gw;
      gbc.gridheight = gh;
      gbc.weightx = wx;
      gbc.weighty = wy;
   }
   */

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