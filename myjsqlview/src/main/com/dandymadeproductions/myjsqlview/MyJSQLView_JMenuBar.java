//=================================================================
//            MenuBar for the MyJSQLView Application.
//=================================================================
//
//    This class is used to constructed the menubar for the
// MyJSQLView application frame.
//
//               << MyJSQLView_JMenuBar.java >>
//
//=================================================================
// Copyright (C) 2005-2010 Dana M. Proctor.
// Version 6.3 05/03/2010
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
// Version 1.0 Original MyJSQLView JMenuBar Class.
//         1.1 Preferences Summary Table Fields and Date Format
//             Additions, YYYY-MM-DD & MM-DD-YYYY.
//         1.2 Removed Preferences Date Format.
//         1.3 Added Logo.
//         1.4 Modified Export Menu.
//         1.5 Added Data Preferences Menu Item.
//         1.6 Main Class Name Change to MyJSQLView.
//         1.7 Modified Help Menu.
//         1.8 Changed Edit|Preferences|Summary Table Fields to
//             Just Edit|Preferences|Table Fields.
//         1.9 Added Tools|Query Frame Menu.
//         2.0 Data Export Menu, Clarification CSV and SQL.
//         2.1 Added Per Developer poisonerbg, SQL Table Export
//             Menu Item.
//         2.2 Removed Data Import | Table, Replaced With Import |
//             SQL Table.
//         2.3 Modified logoItem Name and ImageIcon.
//         2.4 Changed Data Export | SQL, Wording for Clarification.
//         2.5 Data Export SQL Table Text Change.
//         2.6 Added Shortcut Information for Cut, Copy, & Paste.
//         2.7 Edit | Preferences Menu Changed. Removed Data | Preferences
//             Menu.
//         2.8 Modified Data | Export Menu to Include SQL Tab Summary Table
//             & Database Scheme. Changed Action Strings in Same.
//         2.9 Modifications to Help Menu, Manual, Separator, Legal, Release
//             Notes, Separator, About.
//         3.0 Corrected Reference to CSV.
//         3.1 Flush Button Addition. Changed Flush Icon Name.
//         3.2 Added Data | Export | SQL Format | Database Menu Item.
//         3.3 Added ToolTip "Flush" to flushButton.
//         3.4 Code Cleanup.
//         3.5 Constructor Instance flushButton Set ToolTip to "Flush Privileges".
//         3.6 Used System.getProperty("file.separator") for All File System
//             Resources Accesses Through Instance fileSeparator.
//         3.7 Cleaned Up Javadoc Comments.
//         3.8 Changed Data | Import | SQL Table to Dump & Changed Action
//             Label to "DISQLD". Added Data | Import | CSV Data.
//         3.9 Changed Data | Import | CSV Data to CSV File. Also
//             Action Command.
//         4.0 Header Update.
//         4.1 Update Edit Menu Items Information to Ctl+x, Ctl+c, &
//             Ctl+v.
//         4.2 Added Separator to Filemenu.
//         4.3 Added Class Instance serialVersionUID. Declared Class Instance
//             mainFrame private transient.
//         4.4 MyJSQLView Project Common Source Code Formatting.
//         4.5 Additional Standardation to Comments.
//         4.6 Instance flushIcon Icon File Name Change. logoIcon Changed to
//             myjsqlviewIcon and Icon File Name Change. Add Constructor Instance
//             iconsDirectory.
//         4.7 Constructor Argument Changed to MyJSQLView_Frame. Also Cast in
//             in Same for actionListener Instantiation.
//         4.8 Added Menu Item Reload Database to the Tools Menu.
//         4.9 Menu Item Under Tools, Search Database.
//         5.0 Added a New Menu Item Schemas.
//         5.1 Added All Item to Schemas Menu As Needed.
//         5.2 Limited the Addition of Schemas Menu Items to More Than One.
//         5.3 Removed the Word Tab From the SQL & CSV Data Export Menu Items
//             CSV/SQL Summary Table.
//         5.4 Header Format Changes/Update.
//         5.5 Added to the File Menu, Open, Save, and Save As.
//         5.6 Added a Pressed Icon to the flushButton in Constructor.
//         5.7 Changed Flush Icons Names for flushButton in Constuctor.
//         5.8 Removed Constructor Instance fileSeparator. Obtained iconsDirectory
//             From MyJSQLView_Utils Class.
//         5.9 Added fileSeparator to iconsDirectory.
//         6.0 Changed Package to Reflect Dandy Made Productions Code.
//         6.1 Added Class Instance resourceBundle. Implementation of
//             Internationalization. Added Methods createFileMenu, createEditMenu,
//             createDataMenu, createToolsMenu, createSchemasMenu, & createHelpMenu.
//         6.2 Made Class Instance resourceBundle private.
//         6.3 Minor Comment Changes.
//         
//-----------------------------------------------------------------
//                 danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview;

import java.awt.Font;
import java.awt.event.KeyEvent;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.text.DefaultEditorKit;

/**
 *    The MyJSQLView_JMenuBar class is used to constructed the menubar for the
 * MyJSQLView application frame.
 * 
 * @author Dana M. Proctor
 * @version 6.3 05/03/2010
 */

class MyJSQLView_JMenuBar extends JMenuBar
{
   // Instance & Class Fields.
   private static final long serialVersionUID = 949237817664557715L;
   private transient MyJSQLView_Frame mainFrame;
   
   private MyJSQLView_ResourceBundle resourceBundle;

   //==============================================================
   // MyJSQLView_JMenuBar JMenuBar Constructor.
   //==============================================================

   protected MyJSQLView_JMenuBar(MyJSQLView_Frame parent)
   {
      mainFrame = parent;

      // Constructor Instances.
      String iconsDirectory, resource;

      // Setting up a icons directory instance.
      resourceBundle = MyJSQLView.getLocaleResourceBundle();
      iconsDirectory = MyJSQLView_Utils.getIconsDirectory() + MyJSQLView_Utils.getFileSeparator();

      // JMenu Bar for the Frame.
      setBorder(BorderFactory.createEtchedBorder());

      // Creating the File, Edit, Data, & Tools Menus
      createFileMenu();
      createEditMenu();
      createDataMenu();
      createToolsMenu();
      
      // Schemas Menu
      Vector schemas = MyJSQLView_Access.getSchemas();
      if (!schemas.isEmpty() && schemas.size() > 1)
      {
         createSchemasMenu(schemas);
      }
      
      // Help Menu
      createHelpMenu();
      
      add(Box.createHorizontalGlue());

      // Root User Flush Privileges Button
      if (MyJSQLView_Access.getDBName().equals("mysql") &&
          MyJSQLView_Access.getCurrentUser().equals("root"))
      {
         ImageIcon flushIcon = new ImageIcon(iconsDirectory + "flushIcon.png");
         ImageIcon flushIconPressed = new ImageIcon(iconsDirectory + "flushIconPressed.png");
         JButton flushButton = new JButton(flushIcon);
         flushButton.setPressedIcon(flushIconPressed);
         flushButton.setDisabledIcon(flushIcon);
         flushButton.setFocusPainted(false);
         flushButton.setBorder(BorderFactory.createLoweredBevelBorder());
         flushButton.setActionCommand("flush");
         resource = resourceBundle.getResource("MyJSQLView_JMenBar.tooltip.FlushPrivileges");
         if (resource.equals(""))
            flushButton.setToolTipText("Flush Privileges");
         else
            flushButton.setToolTipText(resource);
         flushButton.addActionListener((MyJSQLView_Frame) mainFrame);
         add(flushButton);
      }

      // Logo
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
      
      // Open
      resource = resourceBundle.getResource("MyJSQLView_JMenBar.menu.Open");
      if (resource.equals(""))
         fileMenu.add(menuItem("Open", "FO"));
      else
         fileMenu.add(menuItem(resource, "FO"));
      fileMenu.addSeparator();
      
      // Save
      resource = resourceBundle.getResource("MyJSQLView_JMenBar.menu.Save");
      if (resource.equals(""))
         fileMenu.add(menuItem("Save", "FS"));
      else
         fileMenu.add(menuItem(resource, "FS"));
      resource = resourceBundle.getResource("MyJSQLView_JMenBar.menu.SaveAs");
      if (resource.equals(""))
         fileMenu.add(menuItem("Save As...", "FSA"));
      else
         fileMenu.add(menuItem(resource, "FSA"));
      fileMenu.addSeparator();
      
      // Print
      resource = resourceBundle.getResource("MyJSQLView_JMenBar.menu.Print");
      if (resource.equals(""))
         fileMenu.add(menuItem("Print", "FP"));
      else
         fileMenu.add(menuItem(resource, "FP"));
      resource = resourceBundle.getResource("MyJSQLView_JMenBar.menu.PageFormat");
      if (resource.equals(""))
         fileMenu.add(menuItem("Page Format", "FPG"));
      else
         fileMenu.add(menuItem(resource, "FPG"));
      fileMenu.addSeparator();
      
      // Exit
      resource = resourceBundle.getResource("MyJSQLView_JMenBar.menu.Exit");
      if (resource.equals(""))
         fileMenu.add(menuItem("Exit", "FE"));
      else
         fileMenu.add(menuItem(resource, "FE"));
      
      add(fileMenu);
   }
   
   //==============================================================
   // Helper Method to create the Edit Menu.
   //==============================================================

   private void createEditMenu()
   {
      // Method Instances.
      JMenu editMenu;
      JMenuItem menuItem;
      String resource;
            
      //===========
      // Edit Menu
      
      menuItem = null;
      
      resource = resourceBundle.getResource("MyJSQLView_JMenBar.menu.Edit");
      if (resource.equals(""))
         editMenu = new JMenu("Edit");
      else
         editMenu = new JMenu(resource);
      editMenu.setFont(editMenu.getFont().deriveFont(Font.BOLD));
      
      // Cut, Copy, & Paste
      menuItem = new JMenuItem(new DefaultEditorKit.CutAction());
      resource = resourceBundle.getResource("MyJSQLView_JMenBar.menu.Cut");
      if (resource.equals(""))
         menuItem.setText("Cut" + "          " + "Ctrl+x");
      else
         menuItem.setText(resource + "          " + "Ctrl+x");
      menuItem.setMnemonic(KeyEvent.VK_X);
      editMenu.add(menuItem);

      menuItem = new JMenuItem(new DefaultEditorKit.CopyAction());
      resource = resourceBundle.getResource("MyJSQLView_JMenBar.menu.Copy");
      if (resource.equals(""))
         menuItem.setText("Copy" + "       " + "Ctrl+c");
      else
         menuItem.setText(resource + "       " + "Ctrl+c");
      menuItem.setMnemonic(KeyEvent.VK_C);
      editMenu.add(menuItem);

      menuItem = new JMenuItem(new DefaultEditorKit.PasteAction());
      resource = resourceBundle.getResource("MyJSQLView_JMenBar.menu.Paste");
      if (resource.equals(""))
         menuItem.setText("Paste" + "       " + "Ctrl+v");
      else
         menuItem.setText(resource + "       " + "Ctrl+v");
      menuItem.setMnemonic(KeyEvent.VK_V);
      editMenu.add(menuItem);
      editMenu.addSeparator();

      // Preferences
      resource = resourceBundle.getResource("MyJSQLView_JMenBar.menu.Preferences");
      if (resource.equals(""))
         editMenu.add(menuItem("Preferences", "EP"));
      else
         editMenu.add(menuItem(resource, "EP"));
      
      add(editMenu);
   }
   
   //==============================================================
   // Helper Method to create the Data Menu.
   //==============================================================

   private void createDataMenu()
   {
      // Method Instances.
      JMenu dataMenu;
      JMenu importMenu, exportMenu, exportCSVMenu, exportSQLMenu;
      String resource;
      
      //===========
      // Data Menu
      
      resource = resourceBundle.getResource("MyJSQLView_JMenBar.menu.Data");
      if (resource.equals(""))
         dataMenu = new JMenu("Data");
      else
         dataMenu = new JMenu(resource);
      dataMenu.setFont(dataMenu.getFont().deriveFont(Font.BOLD));
      
      // Import
      resource = resourceBundle.getResource("MyJSQLView_JMenBar.menu.Import");
      if (resource.equals(""))
         importMenu = new JMenu("Import");
      else
         importMenu = new JMenu(resource);

      resource = resourceBundle.getResource("MyJSQLView_JMenBar.menu.ImportSQLDump");
      if (resource.equals(""))
         importMenu.add(menuItem("SQL Dump", "DISQLD"));
      else
         importMenu.add(menuItem(resource, "DISQLD"));
      resource = resourceBundle.getResource("MyJSQLView_JMenBar.menu.ImportCSVFile");
      if (resource.equals(""))
         importMenu.add(menuItem("CSV File", "DICSVF"));
      else
         importMenu.add(menuItem(resource, "DICSVF"));
      
      dataMenu.add(importMenu);

      // Export
      resource = resourceBundle.getResource("MyJSQLView_JMenBar.menu.Export");
      if (resource.equals(""))
         exportMenu = new JMenu("Export");
      else
         exportMenu = new JMenu(resource);
      
      // Export CSV
      resource = resourceBundle.getResource("MyJSQLView_JMenBar.menu.ExportCSVFormat");
      if (resource.equals(""))
        exportCSVMenu = new JMenu("CSV Format");
      else
         exportCSVMenu = new JMenu(resource);
      resource = resourceBundle.getResource("MyJSQLView_JMenBar.menu.ExportCSVTable");
      if (resource.equals(""))
         exportCSVMenu.add(menuItem("Table", "DECSVT"));
      else
         exportCSVMenu.add(menuItem(resource, "DECSVT"));
      resource = resourceBundle.getResource("MyJSQLView_JMenBar.menu.ExportCSVSummaryTable");
      if (resource.equals(""))
         exportCSVMenu.add(menuItem("Summary Table", "DECSVST"));
      else
         exportCSVMenu.add(menuItem(resource, "DECSVST"));
      exportMenu.add(exportCSVMenu);

      // Export SQL
      resource = resourceBundle.getResource("MyJSQLView_JMenBar.menu.ExportSQLFormat");
      if (resource.equals(""))
         exportSQLMenu = new JMenu("SQL Format");
      else
         exportSQLMenu = new JMenu(resource);
      resource = resourceBundle.getResource("MyJSQLView_JMenBar.menu.ExportSQLTable");
      if (resource.equals(""))
         exportSQLMenu.add(menuItem("Table", "DESQLT"));
      else
         exportSQLMenu.add(menuItem(resource, "DESQLT"));
      resource = resourceBundle.getResource("MyJSQLView_JMenBar.menu.ExportSQLSummaryTable");
      if (resource.equals(""))
         exportSQLMenu.add(menuItem("Summary Table", "DESQLST"));
      else
         exportSQLMenu.add(menuItem(resource, "DESQLST"));
      resource = resourceBundle.getResource("MyJSQLView_JMenBar.menu.ExportSQLDatabase");
      if (resource.equals(""))
         exportSQLMenu.add(menuItem("Database", "DESQLD"));
      else
         exportSQLMenu.add(menuItem(resource, "DESQLD"));
      resource = resourceBundle.getResource("MyJSQLView_JMenBar.menu.ExportSQLDatabaseScheme");
      if (resource.equals(""))
         exportSQLMenu.add(menuItem("Database Scheme", "DESQLDS"));
      else
         exportSQLMenu.add(menuItem(resource, "DESQLDS"));
      exportMenu.add(exportSQLMenu);

      dataMenu.add(exportMenu);

      add(dataMenu);
   }
   
   //==============================================================
   // Helper Method to create the Tools Menu.
   //==============================================================

   private void createToolsMenu()
   {
      // Method Instances.
      JMenu toolsMenu;
      String resource;
      
      //===========
      // Tools Menu
      
      resource = resourceBundle.getResource("MyJSQLView_JMenBar.menu.Tools");
      if (resource.equals(""))
         toolsMenu = new JMenu("Tools");
      else
         toolsMenu = new JMenu(resource);
      toolsMenu.setFont(toolsMenu.getFont().deriveFont(Font.BOLD));
      
      // Query Frame, Reload Database, & Search Database.
      resource = resourceBundle.getResource("MyJSQLView_JMenBar.menu.QueryFrame");
      if (resource.equals(""))
         toolsMenu.add(menuItem("Query Frame", "TQ"));
      else
         toolsMenu.add(menuItem(resource, "TQ"));
      resource = resourceBundle.getResource("MyJSQLView_JMenBar.menu.ReloadDatabase");
      if (resource.equals(""))
         toolsMenu.add(menuItem("Reload Database", "TRD"));
      else
         toolsMenu.add(menuItem(resource, "TRD"));
      resource = resourceBundle.getResource("MyJSQLView_JMenBar.menu.SearchDatabase");
      if (resource.equals(""))
         toolsMenu.add(menuItem("Search Database", "TSD"));
      else
         toolsMenu.add(menuItem(resource, "TSD"));
      
      add(toolsMenu);
   }
   
   //==============================================================
   // Helper Method to create the Schemas Menu.
   //==============================================================

   private void createSchemasMenu(Vector schemas)
   {
      // Method Instances.
      JMenu schemasMenu;
      JRadioButtonMenuItem radioButtonMenuItem;
      ButtonGroup schemasButtonGroup;
      String resource;
      
      //===========
      // Schemas Menu
      
      resource = resourceBundle.getResource("MyJSQLView_JMenBar.menu.Schemas");
      if (resource.equals(""))
         schemasMenu = new JMenu("Schemas");
      else
         schemasMenu = new JMenu(resource);
      schemasMenu.setFont(schemasMenu.getFont().deriveFont(Font.BOLD));
      
      // Create a drop down radio button selecter.
      schemasButtonGroup = new ButtonGroup();
      
      int radioButtonCount = 0;
      
      // Add an All schemas item as needed.
      if (schemas.size() != 1)
      {
         resource = resourceBundle.getResource("MyJSQLView_JMenBar.radioButton.All");
         if (resource.equals(""))
            radioButtonMenuItem = new JRadioButtonMenuItem("All", true);
         else
            radioButtonMenuItem = new JRadioButtonMenuItem(resource, true);
         radioButtonMenuItem.setActionCommand("All");
         radioButtonMenuItem.addActionListener(mainFrame);
         schemasButtonGroup.add(radioButtonMenuItem);
         schemasMenu.add(radioButtonMenuItem);
         radioButtonCount = 1;
      }
      
      // Create elements of schemas menu items.
      Iterator schemasIterator = schemas.iterator();
      while (schemasIterator.hasNext())
      {
         String schemasName = (String)schemasIterator.next();
         
         if (radioButtonCount == 0)
            radioButtonMenuItem = new JRadioButtonMenuItem(schemasName, true);
         else
            radioButtonMenuItem = new JRadioButtonMenuItem(schemasName);
         radioButtonMenuItem.setActionCommand(schemasName);
         radioButtonMenuItem.addActionListener(mainFrame);
         schemasButtonGroup.add(radioButtonMenuItem);
         schemasMenu.add(radioButtonMenuItem);
         radioButtonCount++;
      }
      
      add(schemasMenu);
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
      
      resource = resourceBundle.getResource("MyJSQLView_JMenBar.menu.Help");
      if (resource.equals(""))
         helpMenu = new JMenu("Help");
      else
         helpMenu = new JMenu(resource);
      helpMenu.setFont(helpMenu.getFont().deriveFont(Font.BOLD));
      
      // Manual
      resource = resourceBundle.getResource("MyJSQLView_JMenBar.menu.Manual");
      if (resource.equals(""))
         helpMenu.add(menuItem("Manual", "HM"));
      else
         helpMenu.add(menuItem(resource, "HM"));
      helpMenu.addSeparator();
      
      // Legal & Release Notes.
      resource = resourceBundle.getResource("MyJSQLView_JMenBar.menu.Legal");
      if (resource.equals(""))
         helpMenu.add(menuItem("Legal", "HL"));
      else
         helpMenu.add(menuItem(resource, "HL"));
      resource = resourceBundle.getResource("MyJSQLView_JMenBar.menu.ReleaseNotes");
      if (resource.equals(""))
         helpMenu.add(menuItem("Release Notes", "HR"));
      else
         helpMenu.add(menuItem(resource, "HR"));
      helpMenu.addSeparator();
      
      // About
      resource = resourceBundle.getResource("MyJSQLView_JMenBar.menu.About");
      if (resource.equals(""))
         helpMenu.add(menuItem("About", "HA"));
      else
         helpMenu.add(menuItem(resource, "HA"));
      
      add(helpMenu);
   }

   // ==============================================================
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