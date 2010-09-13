//=================================================================
//            TableFieldProfiler Profiler_MenuBar
//=================================================================
//
//    This class is used to constructed the menubar for the Table
// Field Profiler plugin module.
//
//                << Profiler_MenuBar.java >>
//
//=================================================================
// Copyright (C) 2010 Dana M. Proctor.
// Version 1.5 09/12/2010
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
// Version 1.0 Original Profiler_MenuBar Class.
//         1.1 Moved Over to Table Field Profiler Code.
//         1.2 Removed Method createDataQualityMenu() & createToolsMenu().
//         1.3 Removed DataQuality Menu. Added Field Information % Field
//             Analysis Items. Aded Class Method createToolsMenu() and
//             MyJSQLView_ResourceBundle Argument to Constructor.
//         1.4 Implemented/Reviewed Locale Instances.
//         1.5 Changed the Derivation of the Action Command for the File
//             Menu Exit to MyJSQLView_MenuActionCommands.ACTION_EXIT.
//         
//-----------------------------------------------------------------
//                 danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.tablefieldprofiler;

import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import com.dandymadeproductions.myjsqlview.MyJSQLView_Frame;
import com.dandymadeproductions.myjsqlview.MyJSQLView_ResourceBundle;
import com.dandymadeproductions.myjsqlview.MyJSQLView_MenuActionCommands;
import com.dandymadeproductions.myjsqlview.MyJSQLView_Utils;

/**
 *    The Profiler_MenuBar class is used to constructed the menubar for the
 * Table Field Profiler plugin module.
 * 
 * @author Dana M. Proctor
 * @version 1.5 09/12/2010
 */

class Profiler_MenuBar extends JMenuBar
{
   // Instance & Class Fields.
   private static final long serialVersionUID = 1227314757701051137L;

   private MyJSQLView_Frame mainFrame;
   private MyJSQLView_ResourceBundle resourceBundle;
   private MenuActionListener menuListener;

   //==============================================================
   // Profiler_MenuBar JMenuBar Constructor.
   //==============================================================

   protected Profiler_MenuBar(MyJSQLView_Frame parent, MyJSQLView_ResourceBundle resourceBundle,
                              MenuActionListener plugin)
   {
      mainFrame = parent;
      this.resourceBundle = resourceBundle;
      menuListener = plugin;

      // Constructor Instances.
      String iconsDirectory;

      // JMenu Bar for the plugin.
      setBorder(BorderFactory.createEtchedBorder());

      // Creating the File, & Tools Menus
      createFileMenu();
      createToolsMenu();

      add(Box.createHorizontalGlue());

      // Logo
      iconsDirectory = MyJSQLView_Utils.getIconsDirectory() + MyJSQLView_Utils.getFileSeparator();
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
      String resource;
      JMenu fileMenu;
      JMenuItem item;

      // ===========
      // File Menu

      resource = resourceBundle.getResource("Profier_MenuBar.menu.File");
      if (resource.equals(""))
         fileMenu = new JMenu("File");
      else
         fileMenu = new JMenu(resource);
      fileMenu.setFont(fileMenu.getFont().deriveFont(Font.BOLD));
      
      // Open
      resource = resourceBundle.getResource("Profiler_MenuBar.menu.Open");
      if (resource.equals(""))
         fileMenu.add(menuItem("Open", "FO"));
      else
         fileMenu.add(menuItem(resource, "FO"));
      fileMenu.addSeparator();
      
      // Save
      resource = resourceBundle.getResource("Profiler_MenuBar.menu.Save");
      if (resource.equals(""))
         fileMenu.add(menuItem("Save", "FS"));
      else
         fileMenu.add(menuItem(resource, "FS"));
      
      // Save As
      resource = resourceBundle.getResource("Profiler_MenuBar.menu.SaveAs");
      if (resource.equals(""))
         fileMenu.add(menuItem("Save As...", "FSA"));
      else
         fileMenu.add(menuItem(resource, "FSA"));
      fileMenu.addSeparator();
      
      // Exit
      resource = resourceBundle.getResource("Profiler_MenuBar.menu.Exit");
      if (resource.equals(""))
         item = new JMenuItem("Exit");
      else
         item = new JMenuItem(resource);
      item.addActionListener(mainFrame);
      item.setActionCommand(MyJSQLView_MenuActionCommands.ACTION_EXIT);
      fileMenu.add(item);

      add(fileMenu);
   }
   
   //==============================================================
   // Helper Method to create the Tool Menu.
   //==============================================================

   private void createToolsMenu()
   {
      // Method Instances.
      String resource;
      JMenu toolsMenu;

      // ===========
      // Tools Menu
      
      resource = resourceBundle.getResource("Profiler_MenuBar.menu.Tools");
      if (resource.equals(""))
         toolsMenu = new JMenu("Tools");
      else
         toolsMenu = new JMenu(resource);
      toolsMenu.setFont(toolsMenu.getFont().deriveFont(Font.BOLD));
      
      // Field Information
      resource = resourceBundle.getResource("Profiler_MenuBar.menu.FieldInformation");
      if (resource.equals(""))
         toolsMenu.add(menuItem("Field Information", "TFI"));
      else
         toolsMenu.add(menuItem(resource, "TFI"));
      
      // Field Information
      resource = resourceBundle.getResource("Profiler_MenuBar.menu.FieldAnalysis");
      if (resource.equals(""))
         toolsMenu.add(menuItem("Field Analysis", "TFA"));
      else
         toolsMenu.add(menuItem(resource, "TFA"));
      
      add(toolsMenu);
   }
   
   //==============================================================
   // Instance method used for the TableFieldProfiler's creation
   // of menu bar items. Helper Method.
   //==============================================================

   private JMenuItem menuItem(String label, String actionLabel)
   {
      JMenuItem item = new JMenuItem(label);
      item.addActionListener(menuListener);
      item.setActionCommand(actionLabel);
      return item;
   }
}