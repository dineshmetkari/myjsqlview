//=================================================================
//                MyJSQLView_Top_JMenuBar Class
//=================================================================
//
//    This class is used to construct a menubar that contains
// MyJSQLView File|Exit, Plugin Management, & Logo.
//
//            << MyJSQLView_Top_JMenuBar.java >>
//
//=================================================================
// Copyright (C) 2005-2015 Dana M. Proctor
// Version 1.8 07/01/2013
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
// Version 1.0 Original MyJSQLView_Top_JMenuBar Class.
//         1.1 Added Help | About Menu.
//         1.2 Copyright Update.
//         1.3 Copyright Update.
//         1.4 Changes in Way MyJSQLView_ResourceBundle Handles the Collection
//             of Resource Strings. Change to resource.getResourceString(key,
//                default).
//         1.5 MyJSQLView Class Method Change of getLocaleResourceBundle()
//             to getResourceBundle().
//         1.6 Collection of All Image Resources Through resourceBundle.
//         1.7 Changed Package Name to com.dandymadeproductions.myjsqlview.gui.
//         1.8 Added Edit Menu.
//         
//-----------------------------------------------------------------
//                 danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview.gui;

import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import com.dandymadeproductions.myjsqlview.MyJSQLView;
import com.dandymadeproductions.myjsqlview.utilities.MyJSQLView_ResourceBundle;
import com.dandymadeproductions.myjsqlview.utilities.MyJSQLView_Utils;

/**
 *    The MyJSQLView_Top_JMenuBar class is used to construct a menubar that
 * contains the MyJSQLView File|Exit, Plugin Management, & Logo.  
 * 
 * @author Dana M. Proctor
 * @version 1.8 07/01/2013
 */

class MyJSQLView_Top_JMenuBar extends JMenuBar implements MyJSQLView_MenuActionCommands
{
   // Instance & Class Fields.
   private static final long serialVersionUID = -3268359435152713180L;

   private transient MyJSQLView_Frame mainFrame;
   
   private MyJSQLView_ResourceBundle resourceBundle;

   //==============================================================
   // MyJSQLView_Top_JMenuBar Constructor.
   //==============================================================

   protected MyJSQLView_Top_JMenuBar(MyJSQLView_Frame parent)
   {
      mainFrame = parent;

      // Constructor Instances.
      String iconsDirectory;

      // Setting up a icons directory instance.
      resourceBundle = MyJSQLView.getResourceBundle();
      iconsDirectory = MyJSQLView_Utils.getIconsDirectory() + MyJSQLView_Utils.getFileSeparator();

      // Setting up a icons directory instance.
      iconsDirectory = MyJSQLView_Utils.getIconsDirectory() + MyJSQLView_Utils.getFileSeparator();

      // JMenu Bar for the tab.
      setBorder(BorderFactory.createEtchedBorder());

      // Creating your menu items here, see MyJSQLView_JMenuBar.
      createFileMenu();
      createEditMenu();
      createToolsMenu();
      createHelpMenu();
      
      add(Box.createHorizontalGlue());

      // Logo (Keep This)
      ImageIcon logoIcon = MyJSQLView.getResourceBundle().getResourceImage(iconsDirectory
                                                                           + "myjsqlviewIcon.gif");
      JButton logoIconItem = new JButton(logoIcon);
      logoIconItem.setDisabledIcon(logoIcon);
      logoIconItem.setFocusPainted(false);
      logoIconItem.setBorder(BorderFactory.createLoweredBevelBorder());
      add(logoIconItem);
   }
   
   //==============================================================
   // Helper Method to create the File Menu.
   //==============================================================

   private void createFileMenu()
   {
      // Method Instances.
      JMenu fileMenu;
      String resource;
      
      //===========
      // File Menu
      
      resource = resourceBundle.getResourceString("MyJSQLView_JMenuBar.menu.File", "File");
      fileMenu = new JMenu(resource);
      fileMenu.setFont(fileMenu.getFont().deriveFont(Font.BOLD));
      
      // Exit
      resource = resourceBundle.getResourceString("MyJSQLView_JMenuBar.menu.Exit", "Exit");
      fileMenu.add(menuItem(resource, ACTION_EXIT));
      
      add(fileMenu);
   }
   
   //==============================================================
   // Helper Method to create the Edit Menu.
   //==============================================================

   private void createEditMenu()
   {
      // Method Instances.
      JMenu editMenu;
      String resource;
      
      //===========
      // Edit Menu
      
      resource = resourceBundle.getResourceString("MyJSQLView_JMenuBar.menu.Edit", "Edit");
      editMenu = new JMenu(resource);
      editMenu.setFont(editMenu.getFont().deriveFont(Font.BOLD));
      
      resource = resourceBundle.getResourceString("MyJSQLView_JMenuBar.menu.Preferences", "Preferences");
      editMenu.add(menuItem(resource, ACTION_GENERAL_PROPERTIES));
      
      add(editMenu);
   }
   
   //==============================================================
   // Helper Method to create the Tools Menu.
   //==============================================================

   private void createToolsMenu()
   {
      // Method Instances.
      JMenu fileMenu;
      String resource;
      
      //===========
      // Tools Menu
      
      resource = resourceBundle.getResourceString("MyJSQLView_JMenuBar.menu.Tools", "Tools");
      fileMenu = new JMenu(resource);
      fileMenu.setFont(fileMenu.getFont().deriveFont(Font.BOLD));
      
      // Plugin Management
      resource = resourceBundle.getResourceString("MyJSQLView_JMenuBar.menu.PluginManagment",
                                                  "Plugin Management");
      fileMenu.add(menuItem(resource, ACTION_PLUGIN_MANAGEMENT));
      
      add(fileMenu);
   }
   
   //==============================================================
   // Helper Method to create the Help Menu.
   //==============================================================

   private void createHelpMenu()
   {
      // Method Instances.
      JMenu helpMenu;
      String resource;
      
      //===========
      // Help Menu
      
      resource = resourceBundle.getResourceString("MyJSQLView_JMenuBar.menu.Help", "Help");
      helpMenu = new JMenu(resource);
      helpMenu.setFont(helpMenu.getFont().deriveFont(Font.BOLD));
      
      // About
      resource = resourceBundle.getResourceString("MyJSQLView_JMenuBar.menu.About", "About");
      helpMenu.add(menuItem(resource, ACTION_ABOUT));
      
      add(helpMenu);
   }
   
   //==============================================================
   // Instance method used for the MyJSQLView's creation of menu
   // bar items. Helper Method.
   // ==============================================================

   private JMenuItem menuItem(String label, String actionLabel)
   {
      JMenuItem item = new JMenuItem(label);
      item.addActionListener(mainFrame);
      item.setActionCommand(actionLabel);
      return item;
   }
}
