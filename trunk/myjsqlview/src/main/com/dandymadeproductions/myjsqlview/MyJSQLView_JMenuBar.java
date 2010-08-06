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
// Version 7.1 08/05/2010
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
 * @version 7.1 08/05/2010
 */

class MyJSQLView_JMenuBar extends JMenuBar implements MyJSQLView_MenuActionCommands
{
   // Instance & Class Fields.
   private static final long serialVersionUID = 949237817664557715L;
   
   private MyJSQLView_Frame mainFrame;
   private MyJSQLView_ResourceBundle resourceBundle;

   //==============================================================
   // MyJSQLView_JMenuBar JMenuBar Constructor.
   //==============================================================

   protected MyJSQLView_JMenuBar(MyJSQLView_Frame parent)
   {
      mainFrame = parent;

      // Constructor Instances.
      String iconsDirectory, resource;
      Vector<String> schemas;

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
      schemas = MyJSQLView_Access.getSchemas();
      
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
         flushButton.setActionCommand(ACTION_FLUSH);
         resource = resourceBundle.getResource("MyJSQLView_JMenuBar.tooltip.FlushPrivileges");
         if (resource.equals(""))
            flushButton.setToolTipText("Flush Privileges");
         else
            flushButton.setToolTipText(resource);
         flushButton.addActionListener(mainFrame);
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
      
      resource = resourceBundle.getResource("MyJSQLView_JMenuBar.menu.File");
      if (resource.equals(""))
         fileMenu = new JMenu("File");
      else
         fileMenu = new JMenu(resource);
      fileMenu.setFont(fileMenu.getFont().deriveFont(Font.BOLD));
      
      // Open
      resource = resourceBundle.getResource("MyJSQLView_JMenuBar.menu.Open");
      if (resource.equals(""))
         fileMenu.add(menuItem("Open", ACTION_OPEN));
      else
         fileMenu.add(menuItem(resource, ACTION_OPEN));
      fileMenu.addSeparator();
      
      // Save
      resource = resourceBundle.getResource("MyJSQLView_JMenuBar.menu.Save");
      if (resource.equals(""))
         fileMenu.add(menuItem("Save", ACTION_SAVE));
      else
         fileMenu.add(menuItem(resource, ACTION_SAVE));
      resource = resourceBundle.getResource("MyJSQLView_JMenuBar.menu.SaveAs");
      if (resource.equals(""))
         fileMenu.add(menuItem("Save As...", ACTION_SAVE_AS));
      else
         fileMenu.add(menuItem(resource, ACTION_SAVE_AS));
      fileMenu.addSeparator();
      
      // Print
      resource = resourceBundle.getResource("MyJSQLView_JMenuBar.menu.Print");
      if (resource.equals(""))
         fileMenu.add(menuItem("Print", ACTION_PRINT));
      else
         fileMenu.add(menuItem(resource, ACTION_PRINT));
      resource = resourceBundle.getResource("MyJSQLView_JMenuBar.menu.PageFormat");
      if (resource.equals(""))
         fileMenu.add(menuItem("Page Format", ACTION_PAGE_FORMAT));
      else
         fileMenu.add(menuItem(resource, ACTION_PAGE_FORMAT));
      fileMenu.addSeparator();
      
      // Exit
      resource = resourceBundle.getResource("MyJSQLView_JMenuBar.menu.Exit");
      if (resource.equals(""))
         fileMenu.add(menuItem("Exit", ACTION_EXIT));
      else
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
      
      resource = resourceBundle.getResource("MyJSQLView_JMenuBar.menu.Edit");
      if (resource.equals(""))
         editMenu = new JMenu("Edit");
      else
         editMenu = new JMenu(resource);
      editMenu.setFont(editMenu.getFont().deriveFont(Font.BOLD));
      
      // Cut, Copy, & Paste
      menuItem = new JMenuItem(new DefaultEditorKit.CutAction());
      resource = resourceBundle.getResource("MyJSQLView_JMenuBar.menu.Cut");
      if (resource.equals(""))
         menuItem.setText("Cut" + "          " + "Ctrl+x");
      else
         menuItem.setText(resource + "          " + "Ctrl+x");
      menuItem.setMnemonic(KeyEvent.VK_X);
      editMenu.add(menuItem);

      menuItem = new JMenuItem(new DefaultEditorKit.CopyAction());
      resource = resourceBundle.getResource("MyJSQLView_JMenuBar.menu.Copy");
      if (resource.equals(""))
         menuItem.setText("Copy" + "       " + "Ctrl+c");
      else
         menuItem.setText(resource + "       " + "Ctrl+c");
      menuItem.setMnemonic(KeyEvent.VK_C);
      editMenu.add(menuItem);

      menuItem = new JMenuItem(new DefaultEditorKit.PasteAction());
      resource = resourceBundle.getResource("MyJSQLView_JMenuBar.menu.Paste");
      if (resource.equals(""))
         menuItem.setText("Paste" + "       " + "Ctrl+v");
      else
         menuItem.setText(resource + "       " + "Ctrl+v");
      menuItem.setMnemonic(KeyEvent.VK_V);
      editMenu.add(menuItem);
      editMenu.addSeparator();

      // Preferences
      resource = resourceBundle.getResource("MyJSQLView_JMenuBar.menu.Preferences");
      if (resource.equals(""))
         editMenu.add(menuItem("Preferences", ACTION_PREFERENCES));
      else
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
      
      resource = resourceBundle.getResource("MyJSQLView_JMenuBar.menu.Data");
      if (resource.equals(""))
         dataMenu = new JMenu("Data");
      else
         dataMenu = new JMenu(resource);
      dataMenu.setFont(dataMenu.getFont().deriveFont(Font.BOLD));
      
      // Import
      resource = resourceBundle.getResource("MyJSQLView_JMenuBar.menu.Import");
      if (resource.equals(""))
         importMenu = new JMenu("Import");
      else
         importMenu = new JMenu(resource);

      resource = resourceBundle.getResource("MyJSQLView_JMenuBar.menu.ImportSQLDump");
      if (resource.equals(""))
         importMenu.add(menuItem("SQL Dump", ACTION_IMPORT_SQL_DUMP));
      else
         importMenu.add(menuItem(resource, ACTION_IMPORT_SQL_DUMP));
      resource = resourceBundle.getResource("MyJSQLView_JMenuBar.menu.ImportCSVFile");
      if (resource.equals(""))
         importMenu.add(menuItem("CSV File", ACTION_IMPORT_CSV_FILE));
      else
         importMenu.add(menuItem(resource, ACTION_IMPORT_CSV_FILE));
      
      dataMenu.add(importMenu);

      // Export
      resource = resourceBundle.getResource("MyJSQLView_JMenuBar.menu.Export");
      if (resource.equals(""))
         exportMenu = new JMenu("Export");
      else
         exportMenu = new JMenu(resource);
      
      // Export CSV
      resource = resourceBundle.getResource("MyJSQLView_JMenuBar.menu.ExportCSV");
      if (resource.equals(""))
        exportCSVMenu = new JMenu("CSV");
      else
         exportCSVMenu = new JMenu(resource);
      resource = resourceBundle.getResource("MyJSQLView_JMenuBar.menu.ExportCSVTable");
      if (resource.equals(""))
         exportCSVMenu.add(menuItem("Table", ACTION_EXPORT_CSV_TABLE));
      else
         exportCSVMenu.add(menuItem(resource, ACTION_EXPORT_CSV_TABLE));
      resource = resourceBundle.getResource("MyJSQLView_JMenuBar.menu.ExportCSVSummaryTable");
      if (resource.equals(""))
         exportCSVMenu.add(menuItem("Summary Table", ACTION_EXPORT_CSV_SUMMARY_TABLE));
      else
         exportCSVMenu.add(menuItem(resource, ACTION_EXPORT_CSV_SUMMARY_TABLE));
      exportMenu.add(exportCSVMenu);
      
      // Export PDF
      resource = resourceBundle.getResource("MyJSQLView_JMenuBar.menu.ExportPDF");
      if (resource.equals(""))
         exportPDFMenu = new JMenu("PDF");
      else
         exportPDFMenu = new JMenu(resource);
      resource = resourceBundle.getResource("MyJSQLView_JMenuBar.menu.ExportPDFSummaryTable");
      if (resource.equals(""))
         exportPDFMenu.add(menuItem("Summary Table", ACTION_EXPORT_PDF_SUMMARY_TABLE));
      else
         exportPDFMenu.add(menuItem(resource, ACTION_EXPORT_PDF_SUMMARY_TABLE));
      exportMenu.add(exportPDFMenu);
       
      // Export SQL
      resource = resourceBundle.getResource("MyJSQLView_JMenuBar.menu.ExportSQL");
      if (resource.equals(""))
         exportSQLMenu = new JMenu("SQL");
      else
         exportSQLMenu = new JMenu(resource);
      resource = resourceBundle.getResource("MyJSQLView_JMenuBar.menu.ExportSQLTable");
      if (resource.equals(""))
         exportSQLMenu.add(menuItem("Table", ACTION_EXPORT_SQL_TABLE));
      else
         exportSQLMenu.add(menuItem(resource, ACTION_EXPORT_SQL_TABLE));
      resource = resourceBundle.getResource("MyJSQLView_JMenuBar.menu.ExportSQLSummaryTable");
      if (resource.equals(""))
         exportSQLMenu.add(menuItem("Summary Table", ACTION_EXPORT_SQL_SUMMARY_TABLE));
      else
         exportSQLMenu.add(menuItem(resource, ACTION_EXPORT_SQL_SUMMARY_TABLE));
      resource = resourceBundle.getResource("MyJSQLView_JMenuBar.menu.ExportSQLDatabase");
      if (resource.equals(""))
         exportSQLMenu.add(menuItem("Database", ACTION_EXPORT_SQL_DATABASE));
      else
         exportSQLMenu.add(menuItem(resource, ACTION_EXPORT_SQL_DATABASE));
      resource = resourceBundle.getResource("MyJSQLView_JMenuBar.menu.ExportSQLDatabaseScheme");
      if (resource.equals(""))
         exportSQLMenu.add(menuItem("Database Scheme", ACTION_EXPORT_SQL_DATABASE_SCHEME));
      else
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
      
      resource = resourceBundle.getResource("MyJSQLView_JMenuBar.menu.Tools");
      if (resource.equals(""))
         toolsMenu = new JMenu("Tools");
      else
         toolsMenu = new JMenu(resource);
      toolsMenu.setFont(toolsMenu.getFont().deriveFont(Font.BOLD));
      
      // Query Frame, Reload Database, & Search Database.
      resource = resourceBundle.getResource("MyJSQLView_JMenuBar.menu.QueryFrame");
      if (resource.equals(""))
         toolsMenu.add(menuItem("Query Frame", ACTION_QUERY_FRAME));
      else
         toolsMenu.add(menuItem(resource, ACTION_QUERY_FRAME));
      resource = resourceBundle.getResource("MyJSQLView_JMenuBar.menu.ReloadDatabase");
      if (resource.equals(""))
         toolsMenu.add(menuItem("Reload Database", ACTION_RELOAD_DATABASE));
      else
         toolsMenu.add(menuItem(resource, ACTION_RELOAD_DATABASE));
      resource = resourceBundle.getResource("MyJSQLView_JMenuBar.menu.SearchDatabase");
      if (resource.equals(""))
         toolsMenu.add(menuItem("Search Database", ACTION_SEARCH_DATABASE));
      else
         toolsMenu.add(menuItem(resource, ACTION_SEARCH_DATABASE));
      
      add(toolsMenu);
   }
   
   //==============================================================
   // Helper Method to create the Schemas Menu.
   //==============================================================

   private void createSchemasMenu(Vector<String> schemas)
   {
      // Method Instances.
      JMenu schemasMenu;
      JRadioButtonMenuItem radioButtonMenuItem;
      ButtonGroup schemasButtonGroup;
      String resource;
      
      //===========
      // Schemas Menu
      
      resource = resourceBundle.getResource("MyJSQLView_JMenuBar.menu.Schemas");
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
         resource = resourceBundle.getResource("MyJSQLView_JMenuBar.radioButton.All");
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
   // Helper Method to create the Help Menu.
   //==============================================================

   private void createHelpMenu()
   {
      // Method Instances.
      JMenu helpMenu;
      String resource;
      
      //===========
      // Help Menu
      
      resource = resourceBundle.getResource("MyJSQLView_JMenuBar.menu.Help");
      if (resource.equals(""))
         helpMenu = new JMenu("Help");
      else
         helpMenu = new JMenu(resource);
      helpMenu.setFont(helpMenu.getFont().deriveFont(Font.BOLD));
      
      // Manual
      resource = resourceBundle.getResource("MyJSQLView_JMenuBar.menu.Manual");
      if (resource.equals(""))
         helpMenu.add(menuItem("Manual", ACTION_MANUAL));
      else
         helpMenu.add(menuItem(resource, ACTION_MANUAL));
      helpMenu.addSeparator();
      
      // Legal & Release Notes.
      resource = resourceBundle.getResource("MyJSQLView_JMenuBar.menu.Legal");
      if (resource.equals(""))
         helpMenu.add(menuItem("Legal", ACTION_LEGAL));
      else
         helpMenu.add(menuItem(resource, ACTION_LEGAL));
      resource = resourceBundle.getResource("MyJSQLView_JMenuBar.menu.ReleaseNotes");
      if (resource.equals(""))
         helpMenu.add(menuItem("Release Notes", ACTION_RELEASE_NOTES));
      else
         helpMenu.add(menuItem(resource, ACTION_RELEASE_NOTES));
      helpMenu.addSeparator();
      
      // About
      resource = resourceBundle.getResource("MyJSQLView_JMenuBar.menu.About");
      if (resource.equals(""))
         helpMenu.add(menuItem("About", ACTION_ABOUT));
      else
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