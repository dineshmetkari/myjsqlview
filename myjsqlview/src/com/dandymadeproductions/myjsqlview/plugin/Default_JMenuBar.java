//=================================================================
//                Default_JMenuBar Class
//=================================================================
//
//    This class is used to construct a menubar that contains
// essentially only the MyJSQLView File|Exit & Logo.
//
//               << Default_JMenuBar.java >>
//
//=================================================================
// Copyright (C) 2005-2012 Dana M. Proctor.
// Version 1.9 09/11/2012
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
// Version 1.0 Original Default_JMenuBar Class.
//         1.1 Implemented MyJSQLView_MenuActionCommands.
//         1.2 Minor Format Changes.
//         1.3 Correction to Resource MyJSQLView_JMenuBar Name.
//         1.4 Copyright Update.
//         1.5 Copyright Update.
//         1.6 Changes in Way MyJSQLView_ResourceBundle Handles the Collection
//             of Resource Strings. Change to resource.getResourceString(key,
//             default).
//         1.7 MyJSQLView Class Method Change of getLocaleResourceBundle()
//             to getResourceBundle().
//         1.8 Collection of All Image Resources Through resourceBundle.
//         1.9 Changed Package Name to com.dandymadeproductions.myjsqlview.plugin.
//             Made Class & Constructor Public.
//         
//-----------------------------------------------------------------
//                 danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview.plugin;

import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import com.dandymadeproductions.myjsqlview.MyJSQLView;
import com.dandymadeproductions.myjsqlview.gui.MyJSQLView_Frame;
import com.dandymadeproductions.myjsqlview.gui.MyJSQLView_MenuActionCommands;
import com.dandymadeproductions.myjsqlview.utilities.MyJSQLView_ResourceBundle;
import com.dandymadeproductions.myjsqlview.utilities.MyJSQLView_Utils;

/**
 *    The Default_JMenuBar class is used to construct a menubar that
 * contains essentially only the MyJSQLView File|Exit & Logo.  
 * 
 * @author Dana M. Proctor
 * @version 1.9 09/11/2012
 */

public class Default_JMenuBar extends JMenuBar implements MyJSQLView_MenuActionCommands
{
   // Instance & Class Fields.
   private static final long serialVersionUID = -2391184942367595206L;
   private transient MyJSQLView_Frame mainFrame;
   
   private MyJSQLView_ResourceBundle resourceBundle;

   //==============================================================
   // Default_JMenuBar Constructor.
   //==============================================================

   public Default_JMenuBar(MyJSQLView_Frame parent)
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
      
      add(Box.createHorizontalGlue());

      // Logo (Keep This)
      ImageIcon logoIcon = resourceBundle.getResourceImage(iconsDirectory + "myjsqlviewIcon.gif");
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