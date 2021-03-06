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
// Copyright (C) 2005-2015 Dana M. Proctor
// Version 8.2 06/15/2014
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
//         1.1 Preferences Summary Table Fields and Date Format Additions,
//             YYYY-MM-DD & MM-DD-YYYY.
//         1.2 Removed Preferences Date Format.
//         1.3 Added Logo.
//         1.4 Modified Export Menu.
//         1.5 Added Data Preferences Menu Item.
//         1.6 Main Class Name Change to MyJSQLView.
//         1.7 Modified Help Menu.
//         1.8 Changed Edit|Preferences|Summary Table Fields to Just Edit|
//             Preferences|Table Fields.
//         1.9 Added Tools|Query Frame Menu.
//         2.0 Data Export Menu, Clarification CSV and SQL.
//         2.1 Added Per Developer poisonerbg, SQL Table Export Menu Item.
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
//         6.4 Implemented MyJSQLView_MenuActionCommands.
//         6.5 Restored Class Back to a Prior State After Modification to Test
//             the Passing of the Class Instance mainFrame Through the Method
//             menuItem(). Removed static From Both of These Items.
//         6.6 Parameterized Constructor Instance schemas and Argument to Class
//             Method createSchemasMenu() in Order to Bring Code Into Compliance
//             With Java 5.0 API.
//         6.7 Parameterized schemasIterator in Class Method createSchemasMenu().
//         6.8 Added Data | Export | PDF Format | Summary Table in Class Method
//             createDataMenu().
//         6.9 Changed Data | Export | CSV/PDF/SQL Format to Just CSV/PDF/SQL.
//         7.0 Corrected the resourceBundle.getResource() Text For MyJSQLView_JMenBar.
//             to MyJSQLView_JMenuBar.
//         7.1 Constructor Resource Correction for flushButton.setToolTipText.
//         7.2 Changes to Access Database Properties schemas and ConnectionProperties
//             to the New Redefined Class ConnectionManager.
//         7.3 Added Tools | SQL Query Bucket in Class Method createToolsMenu().
//         7.4 Change in Constructor for ConnectionManager.getDataSourceType()
//             From getConnectionProperties().
//         7.5 Copyright Update.
//         7.6 Changed Constructor Instance schemas from Vector to ArrayList.
//         7.7 Changes in Way MyJSQLView_ResourceBundle Handles the Collection
//             of Resource Strings. Change to resource.getResourceString(key,
//             default).
//         7.8 MyJSQLView Class Method Change of getLocaleResourceBundle()
//             to getResourceBundle().
//         7.9 Collection of All Image Resources Through resourceBundle.
//         8.0 Changed Package Name to com.dandymadeproductions.myjsqlview.gui.
//         8.1 Made Class & Constructor Public. Created Class Instance schemasMenu,
//             Removed from createSchemasMenu(). Made mainFrame static. Added
//             Class Method reloadSchemasMenu().
//         8.2 Constructor, Inclusion of MariaDb root User Flush Button.
//         
//-----------------------------------------------------------------
//                 danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview.gui;

import java.awt.Font;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;

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

import com.dandymadeproductions.myjsqlview.MyJSQLView;
import com.dandymadeproductions.myjsqlview.datasource.ConnectionManager;
import com.dandymadeproductions.myjsqlview.datasource.ConnectionProperties;
import com.dandymadeproductions.myjsqlview.utilities.MyJSQLView_ResourceBundle;
import com.dandymadeproductions.myjsqlview.utilities.MyJSQLView_Utils;

/**
 *    The MyJSQLView_JMenuBar class is used to constructed the menubar for the
 * MyJSQLView application frame.
 * 
 * @author Dana M. Proctor
 * @version 8.2 06/15/2014
 */

public class MyJSQLView_JMenuBar extends JMenuBar implements MyJSQLView_MenuActionCommands
{
   // Instance & Class Fields.
   private static final long serialVersionUID = 949237817664557715L;
   
   private static JMenu schemasMenu = new JMenu();
   private static MyJSQLView_Frame mainFrame;
   private MyJSQLView_ResourceBundle resourceBundle;

   //==============================================================
   // MyJSQLView_JMenuBar JMenuBar Constructor.
   //==============================================================

   public MyJSQLView_JMenuBar(MyJSQLView_Frame parent)
   {
      mainFrame = parent;

      // Constructor Instances.
      String iconsDirectory, resource;
      ArrayList<String> schemas;

      // Setting up a icons directory instance.
      resourceBundle = MyJSQLView.getResourceBundle();
      iconsDirectory = MyJSQLView_Utils.getIconsDirectory() + MyJSQLView_Utils.getFileSeparator();

      // JMenu Bar for the Frame.
      setBorder(BorderFactory.createEtchedBorder());

      // Creating the File, Edit, Data, & Tools Menus
      createFileMenu();
      createEditMenu();
      createDataMenu();
      createToolsMenu();
      
      // Schemas Menu
      schemas = ConnectionManager.getSchemas();
      
      if (!schemas.isEmpty() && schemas.size() > 1)
      {
         createSchemasMenu(schemas);
      }
      
      // Help Menu
      createHelpMenu();
      
      add(Box.createHorizontalGlue());

      // Root User Flush Privileges Button
      if ((ConnectionManager.getDataSourceType().equals(ConnectionManager.MYSQL)
           || ConnectionManager.getDataSourceType().equals(ConnectionManager.MARIADB))
           && ConnectionManager.getConnectionProperties().getProperty(
                         ConnectionProperties.USER).equals("root"))
      {
         ImageIcon flushIcon = resourceBundle.getResourceImage(iconsDirectory + "flushIcon.png");
         ImageIcon flushIconPressed = resourceBundle.getResourceImage(iconsDirectory
                                                                      + "flushIconPressed.png");
         JButton flushButton = new JButton(flushIcon);
         flushButton.setPressedIcon(flushIconPressed);
         flushButton.setDisabledIcon(flushIcon);
         flushButton.setFocusPainted(false);
         flushButton.setBorder(BorderFactory.createLoweredBevelBorder());
         flushButton.setActionCommand(ACTION_FLUSH);
         resource = resourceBundle.getResourceString("MyJSQLView_JMenuBar.tooltip.FlushPrivileges",
                                                     "Flush Privileges");
         flushButton.setToolTipText(resource);
         flushButton.addActionListener(mainFrame);
         add(flushButton);
      }

      // Logo
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
      
      // Open
      resource = resourceBundle.getResourceString("MyJSQLView_JMenuBar.menu.Open", "Open");
      fileMenu.add(menuItem(resource, ACTION_OPEN));
      fileMenu.addSeparator();
      
      // Save
      resource = resourceBundle.getResourceString("MyJSQLView_JMenuBar.menu.Save", "Save");
      fileMenu.add(menuItem(resource, ACTION_SAVE));
      
      resource = resourceBundle.getResourceString("MyJSQLView_JMenuBar.menu.SaveAs", "Save As...");
      fileMenu.add(menuItem(resource, ACTION_SAVE_AS));
      fileMenu.addSeparator();
      
      // Print
      resource = resourceBundle.getResourceString("MyJSQLView_JMenuBar.menu.Print", "Print");
      fileMenu.add(menuItem(resource, ACTION_PRINT));
      
      resource = resourceBundle.getResourceString("MyJSQLView_JMenuBar.menu.PageFormat", "Page Format");
      fileMenu.add(menuItem(resource, ACTION_PAGE_FORMAT));
      fileMenu.addSeparator();
      
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
      JMenuItem menuItem;
      String resource;
            
      //===========
      // Edit Menu
      
      menuItem = null;
      
      resource = resourceBundle.getResourceString("MyJSQLView_JMenuBar.menu.Edit", "Edit");
      editMenu = new JMenu(resource);
      editMenu.setFont(editMenu.getFont().deriveFont(Font.BOLD));
      
      // Cut, Copy, & Paste
      menuItem = new JMenuItem(new DefaultEditorKit.CutAction());
      resource = resourceBundle.getResourceString("MyJSQLView_JMenuBar.menu.Cut", "Cut");
      menuItem.setText(resource + "          " + "Ctrl+x");
      menuItem.setMnemonic(KeyEvent.VK_X);
      editMenu.add(menuItem);

      menuItem = new JMenuItem(new DefaultEditorKit.CopyAction());
      resource = resourceBundle.getResourceString("MyJSQLView_JMenuBar.menu.Copy", "Copy");
      menuItem.setText(resource + "       " + "Ctrl+c");
      menuItem.setMnemonic(KeyEvent.VK_C);
      editMenu.add(menuItem);

      menuItem = new JMenuItem(new DefaultEditorKit.PasteAction());
      resource = resourceBundle.getResourceString("MyJSQLView_JMenuBar.menu.Paste", "Paste");
      menuItem.setText(resource + "       " + "Ctrl+v");
      menuItem.setMnemonic(KeyEvent.VK_V);
      editMenu.add(menuItem);
      editMenu.addSeparator();

      // Preferences
      resource = resourceBundle.getResourceString("MyJSQLView_JMenuBar.menu.Preferences", "Preferences");
      editMenu.add(menuItem(resource, ACTION_PREFERENCES));
      
      add(editMenu);
   }
   
   //==============================================================
   // Helper Method to create the Data Menu.
   //==============================================================

   private void createDataMenu()
   {
      // Method Instances.
      JMenu dataMenu;
      JMenu importMenu, exportMenu, exportCSVMenu, exportPDFMenu, exportSQLMenu;
      String resource;
      
      //===========
      // Data Menu
      
      resource = resourceBundle.getResourceString("MyJSQLView_JMenuBar.menu.Data", "Data");
      dataMenu = new JMenu(resource);
      dataMenu.setFont(dataMenu.getFont().deriveFont(Font.BOLD));
      
      // Import
      resource = resourceBundle.getResourceString("MyJSQLView_JMenuBar.menu.Import", "Import");
      importMenu = new JMenu(resource);

      resource = resourceBundle.getResourceString("MyJSQLView_JMenuBar.menu.ImportSQLDump", "SQL Dump");
      importMenu.add(menuItem(resource, ACTION_IMPORT_SQL_DUMP));
      
      resource = resourceBundle.getResourceString("MyJSQLView_JMenuBar.menu.ImportCSVFile", "CSV File");
      importMenu.add(menuItem(resource, ACTION_IMPORT_CSV_FILE));
      
      dataMenu.add(importMenu);

      // Export
      resource = resourceBundle.getResourceString("MyJSQLView_JMenuBar.menu.Export", "Export");
      exportMenu = new JMenu(resource);
      
      // Export CSV
      resource = resourceBundle.getResourceString("MyJSQLView_JMenuBar.menu.ExportCSV", "CSV");
      exportCSVMenu = new JMenu(resource);
      
      resource = resourceBundle.getResourceString("MyJSQLView_JMenuBar.menu.ExportCSVTable", "Table");
      exportCSVMenu.add(menuItem(resource, ACTION_EXPORT_CSV_TABLE));
      
      resource = resourceBundle.getResourceString("MyJSQLView_JMenuBar.menu.ExportCSVSummaryTable",
                                                  "Summary Table");
      exportCSVMenu.add(menuItem(resource, ACTION_EXPORT_CSV_SUMMARY_TABLE));
      exportMenu.add(exportCSVMenu);
      
      // Export PDF
      resource = resourceBundle.getResourceString("MyJSQLView_JMenuBar.menu.ExportPDF", "PDF");
      exportPDFMenu = new JMenu(resource);
      
      resource = resourceBundle.getResourceString("MyJSQLView_JMenuBar.menu.ExportPDFSummaryTable",
                                                  "Summary Table");
      exportPDFMenu.add(menuItem(resource, ACTION_EXPORT_PDF_SUMMARY_TABLE));
      exportMenu.add(exportPDFMenu);
       
      // Export SQL
      resource = resourceBundle.getResourceString("MyJSQLView_JMenuBar.menu.ExportSQL", "SQL");
      exportSQLMenu = new JMenu(resource);
      
      resource = resourceBundle.getResourceString("MyJSQLView_JMenuBar.menu.ExportSQLTable", "Table");
      exportSQLMenu.add(menuItem(resource, ACTION_EXPORT_SQL_TABLE));
      
      resource = resourceBundle.getResourceString("MyJSQLView_JMenuBar.menu.ExportSQLSummaryTable",
                                                  "Summary Table");
      exportSQLMenu.add(menuItem(resource, ACTION_EXPORT_SQL_SUMMARY_TABLE));
      
      resource = resourceBundle.getResourceString("MyJSQLView_JMenuBar.menu.ExportSQLDatabase", "Database");
      exportSQLMenu.add(menuItem(resource, ACTION_EXPORT_SQL_DATABASE));
      
      resource = resourceBundle.getResourceString("MyJSQLView_JMenuBar.menu.ExportSQLDatabaseScheme",
                                                  "Database Scheme");
      exportSQLMenu.add(menuItem(resource, ACTION_EXPORT_SQL_DATABASE_SCHEME));
      
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
      
      resource = resourceBundle.getResourceString("MyJSQLView_JMenuBar.menu.Tools", "Tools");
      toolsMenu = new JMenu(resource);
      toolsMenu.setFont(toolsMenu.getFont().deriveFont(Font.BOLD));
      
      // SQL Query Bucket, Query Frame, Reload Database, & Search Database.
      
      resource = resourceBundle.getResourceString("MyJSQLView_JMenuBar.menu.SQLQueryBucket",
                                                  "SQL Query Bucket");
      toolsMenu.add(menuItem(resource, ACTION_SQL_QUERY_BUCKET));
      
      resource = resourceBundle.getResourceString("MyJSQLView_JMenuBar.menu.QueryFrame", "Query Frame");
      toolsMenu.add(menuItem(resource, ACTION_QUERY_FRAME));
      
      resource = resourceBundle.getResourceString("MyJSQLView_JMenuBar.menu.ReloadDatabase",
                                                  "Reload Database");
      toolsMenu.add(menuItem(resource, ACTION_RELOAD_DATABASE));
      
      resource = resourceBundle.getResourceString("MyJSQLView_JMenuBar.menu.SearchDatabase",
                                                  "Search Database");
      toolsMenu.add(menuItem(resource, ACTION_SEARCH_DATABASE));
      
      add(toolsMenu);
   }
   
   //==============================================================
   // Helper Method to create the Schemas Menu.
   //==============================================================

   private void createSchemasMenu(ArrayList<String> schemas)
   {
      // Method Instances.
      JRadioButtonMenuItem radioButtonMenuItem;
      ButtonGroup schemasButtonGroup;
      String resource;
      
      //===========
      // Schemas Menu
      
      resource = resourceBundle.getResourceString("MyJSQLView_JMenuBar.menu.Schemas", "Schemas");
      schemasMenu.setText(resource);
      schemasMenu.setFont(schemasMenu.getFont().deriveFont(Font.BOLD));
      
      // Create a drop down radio button selecter.
      schemasButtonGroup = new ButtonGroup();
      
      int radioButtonCount = 0;
      
      // Add an All schemas item as needed.
      if (schemas.size() != 1)
      {
         resource = resourceBundle.getResourceString("MyJSQLView_JMenuBar.radioButton.All", "All");
         radioButtonMenuItem = new JRadioButtonMenuItem(resource, true);
         radioButtonMenuItem.setActionCommand("All");
         radioButtonMenuItem.addActionListener(mainFrame);
         schemasButtonGroup.add(radioButtonMenuItem);
         schemasMenu.add(radioButtonMenuItem);
         radioButtonCount = 1;
      }
      
      // Create elements of schemas menu items.
      Iterator<String> schemasIterator = schemas.iterator();
      
      while (schemasIterator.hasNext())
      {
         String schemasName = schemasIterator.next();
         
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
   // Method to reload the schemas menu item.
   //==============================================================

   public static void reloadSchemasMenu()
   {
      if (!schemasMenu.getText().isEmpty())
      {
         // Method Instances.
         ArrayList<String> schemas;
         
         JRadioButtonMenuItem radioButtonMenuItem;
         ButtonGroup schemasButtonGroup;
         String schemasName, resource;
         
         // Collect the schemas
         
         schemas = ConnectionManager.getSchemas();
         Iterator<String> schemasIterator1 = schemas.iterator();
         
         while (schemasIterator1.hasNext())
            schemasName = schemasIterator1.next();
          
         // Clear & reset the menu.
         
         schemasMenu.removeAll();
         schemasButtonGroup = new ButtonGroup();
         
         int radioButtonCount = 0;
         
         // Add an All schemas item as needed.
         if (schemas.size() != 1)
         {
            resource = MyJSQLView.getResourceBundle().getResourceString("MyJSQLView_JMenuBar.radioButton.All", "All");
            radioButtonMenuItem = new JRadioButtonMenuItem(resource, true);
            radioButtonMenuItem.setActionCommand("All");
            radioButtonMenuItem.addActionListener(mainFrame);
            schemasButtonGroup.add(radioButtonMenuItem);
            schemasMenu.add(radioButtonMenuItem);
            radioButtonCount = 1;
         }
         
         // Create elements of schemas menu items.
         Iterator<String> schemasIterator = schemas.iterator();
         
         while (schemasIterator.hasNext())
         {
            schemasName = schemasIterator.next();
            
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
      }
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
      
      // Manual
      resource = resourceBundle.getResourceString("MyJSQLView_JMenuBar.menu.Manual", "Manual");
      helpMenu.add(menuItem(resource, ACTION_MANUAL));
      helpMenu.addSeparator();
      
      // Legal & Release Notes.
      resource = resourceBundle.getResourceString("MyJSQLView_JMenuBar.menu.Legal", "Legal");
      helpMenu.add(menuItem(resource, ACTION_LEGAL));
      
      resource = resourceBundle.getResourceString("MyJSQLView_JMenuBar.menu.ReleaseNotes",
                                                  "Release Notes");
      helpMenu.add(menuItem(resource, ACTION_RELEASE_NOTES));
      helpMenu.addSeparator();
      
      // About
      resource = resourceBundle.getResourceString("MyJSQLView_JMenuBar.menu.About", "About");
      helpMenu.add(menuItem(resource, ACTION_ABOUT));
      
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
