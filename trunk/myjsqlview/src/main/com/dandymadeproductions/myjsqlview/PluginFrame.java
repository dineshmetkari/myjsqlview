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
// Version 1.0 08/04/2010
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

import javax.swing.*;

/**
 *    The PluginFrame class provides a frame that is used to view, remove,
 * and install new plugins to the MyJSQLView application.
 * 
 * @author Dana M. Proctor
 * @version 1.0 08/04/2010
 */

//=================================================================
//                   MyJSQLView PluginFrame
//=================================================================

class PluginFrame extends JFrame implements ActionListener
{
   //=============================================
   // Creation of the necessary class instance
   // variables for the JFrame.
   //=============================================
   
   private static final long serialVersionUID = 6203223580678904034L;

   private JPanel mainPanel, pluginViewPanel;
   private MyJSQLView_ResourceBundle resourceBundle;
   private JButton[] removeButtons;
   private JButton installButton, closeButton;

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
      // Stop PreferencesPanel thread and dispose.
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
      int removeIndex;

      // Overall action buttons.
      if (frameSource instanceof JButton)
      {
         // Install button action.
         if (frameSource == installButton)
         {
            System.out.println("Installing New Plugin");
         }
         // Close buttton action.
         else if (frameSource == closeButton)
         {
            MyJSQLView_JMenuBarActions.setPluginFrameNotVisisble();
            this.dispose();
         }
         // Must be action to remove a plugin.
         else
         {
            removeIndex = Integer.parseInt(((JButton) frameSource).getActionCommand()); 
            MyJSQLView_Frame.removeTab(removeIndex);
         }
      }
      else
         return;
   }
   
   //==============================================================
   // Class Method for setting up the view of the current installed
   // plugins that includes the tabIcon, name, version, and a 
   // remove button.
   //==============================================================

   private void createInstalledPluginsViewPanel(Vector<MyJSQLView_PluginModule> loadedPlugins)
   {
      // Class Method Instances
      GridBagLayout gridbag;
      GridBagConstraints constraints;
      JButton tabIconButton;
      JLabel pluginName, pluginVersion;
      
      gridbag = new GridBagLayout();
      constraints = new GridBagConstraints();
      pluginViewPanel = new JPanel(gridbag);
      removeButtons = new JButton[loadedPlugins.size()];
      
      for (int i = 0; i < loadedPlugins.size(); i++)
      {
         // Plugin Icon
         tabIconButton = new JButton(loadedPlugins.get(i).getTabIcon());
         tabIconButton.setFocusable(false);
         tabIconButton.setMargin(new Insets(0, 0, 0, 0));
         
         buildConstraints(constraints, 0, i, 1, 1, 100, 100);
         constraints.fill = GridBagConstraints.NONE;
         constraints.anchor = GridBagConstraints.CENTER;
         gridbag.setConstraints(tabIconButton, constraints);
         pluginViewPanel.add(tabIconButton);
         
         // Plugin Name
         pluginName = new JLabel(loadedPlugins.get(i).getName());
         
         buildConstraints(constraints, 1, i, 1, 1, 100, 100);
         constraints.fill = GridBagConstraints.NONE;
         constraints.anchor = GridBagConstraints.WEST;
         gridbag.setConstraints(pluginName, constraints);
         pluginViewPanel.add(pluginName);
         
         // Plugin Version
         pluginVersion = new JLabel(loadedPlugins.get(i).getVersion());
         
         buildConstraints(constraints, 2, i, 1, 1, 100, 100);
         constraints.fill = GridBagConstraints.NONE;
         constraints.anchor = GridBagConstraints.WEST;
         gridbag.setConstraints(pluginVersion, constraints);
         pluginViewPanel.add(pluginVersion);
         
         // Plugin Remove Button
         removeButtons[i] = new JButton("Remove");
         removeButtons[i].setActionCommand(Integer.toString(i));
         removeButtons[i].addActionListener(this);
         
         buildConstraints(constraints, 3, i, 1, 1, 100, 100);
         constraints.fill = GridBagConstraints.NONE;
         constraints.anchor = GridBagConstraints.WEST;
         gridbag.setConstraints(removeButtons[i], constraints);
         pluginViewPanel.add(removeButtons[i]);   
      }
      mainPanel.add(pluginViewPanel, BorderLayout.CENTER);
   }

   //==============================================================
   // Class Method for helping the parameters in gridbag.
   //==============================================================

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