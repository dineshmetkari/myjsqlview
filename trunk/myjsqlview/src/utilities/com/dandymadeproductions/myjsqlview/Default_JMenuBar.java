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
// Copyright (C) 2005-2010 Dana M. Proctor.
// Version 1.0 04/26/2010
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
//         
//-----------------------------------------------------------------
//                 danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview;

import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 *  The Default_JMenuBar class is used to construct a menubar that
 * contains essentially only the MyJSQLView File|Exit & Logo.  
 * 
 * @author Dana M. Proctor
 * @version 1.0 04/26/2010
 */

class Default_JMenuBar extends JMenuBar
{
   // Instance & Class Fields.
   private static final long serialVersionUID = -2391184942367595206L;
   private transient MyJSQLView_Frame mainFrame;
   
   private MyJSQLView_ResourceBundle resourceBundle;

   //==============================================================
   // Default_JMenuBar Constructor.
   //==============================================================

   protected Default_JMenuBar(MyJSQLView_Frame parent)
   {
      mainFrame = parent;

      // Constructor Instances.
      String iconsDirectory;

      // Setting up a icons directory instance.
      resourceBundle = MyJSQLView.getLocaleResourceBundle();
      iconsDirectory = MyJSQLView_Utils.getIconsDirectory() + MyJSQLView_Utils.getFileSeparator();

      // Setting up a icons directory instance.
      iconsDirectory = MyJSQLView_Utils.getIconsDirectory() + MyJSQLView_Utils.getFileSeparator();

      // JMenu Bar for the tab.
      setBorder(BorderFactory.createEtchedBorder());

      // Creating your menu items here, see MyJSQLView_JMenuBar.
      createFileMenu();
      
      add(Box.createHorizontalGlue());

      // Logo (Keep This)
      ImageIcon logoIcon = new ImageIcon(iconsDirectory + "myjsqlviewIcon.gif");
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
      
      resource = resourceBundle.getResource("MyJSQLView_JMenBar.menu.File");
      if (resource.equals(""))
         fileMenu = new JMenu("File");
      else
         fileMenu = new JMenu(resource);
      fileMenu.setFont(fileMenu.getFont().deriveFont(Font.BOLD));
      
      // Exit
      resource = resourceBundle.getResource("MyJSQLView_JMenBar.menu.Exit");
      if (resource.equals(""))
         fileMenu.add(menuItem("Exit", "FE"));
      else
         fileMenu.add(menuItem(resource, "FE"));
      
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