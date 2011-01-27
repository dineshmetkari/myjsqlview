//=================================================================
//                    MyJSQLView_JToolBar Class
//=================================================================
//
//    This class is used to construct the toolbar for the MyJSQLView
// application frame's database tables tab.
//
//               << MyJSQLView_JToolBar.java >>
//
//=================================================================
// Copyright (C) 2005-2011 Dana M. Proctor.
// Version 2.3 01/26/2011
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
// Version 1.0 Original MyJSQLView JToolBar Class.
//         1.1 Added Preferences ToolBar Item.
//         1.2 Removed the Word Tab From the SQL & CSV Data Export Menu Items
//             CSV/SQL Summary Table in Constructor. Also Changed the ImageIcons
//             For These Same ToolBar Components.
//         1.3 Header Format Changes/Update.
//         1.4 Added File Open/Save ToolBar Items.
//         1.5 Removed fileSeparator Instance From Constructor and Obtained
//             iconsDirectory Directly From MyJSQLView_Utils.
//         1.6 Added fileSeparator to iconsDirectory.
//         1.7 Changed Package to Reflect Dandy Made Productions Code.
//         1.8 Added Constructor Instances resourceBundle & resource. Implementation
//             of Internationalization.
//         1.9 Changed Description.
//         2.0 Implemented MyJSQLView_MenuActionCommands.
//         2.1 Added Data Export PDF Summary Table ToolBar Button.
//         2.2 Corrected the Arrangement of the Data Export PDF Summary Table
//             ToolBar Button.
//         2.3 Copyright Update.
//         
//-----------------------------------------------------------------
//                 danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview;

import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToolBar;

/**
 *    The MyJSQLView_JToolBar class is used to construct the toolbar
 * for the MyJSQLView application frame's database tables tab.
 * 
 * @author Dana M. Proctor
 * @version 2.3 01/26/2011
 */

class MyJSQLView_JToolBar extends JToolBar implements MyJSQLView_MenuActionCommands
{
   // Instance & Class Fields.
   private static final long serialVersionUID = -6991628371324254711L;
   private transient MyJSQLView_Frame mainFrame;

   //==============================================================
   // MyJSQLView_JToolBar Constructor.
   //==============================================================

   protected MyJSQLView_JToolBar(MyJSQLView_Frame parent, String title)
   {
      super(title);
      mainFrame = parent;

      // Constructor Instances
      String iconsDirectory;
      JButton buttonItem;
      
      ImageIcon openIcon, saveIcon;
      ImageIcon printIcon, pageFormatIcon, exitIcon;
      ImageIcon preferencesIcon, sqlImportIcon, csvImportIcon;
      ImageIcon csvExportTableIcon, csvExportTabSummaryTableIcon;
      ImageIcon pdfExportTabSummaryTableIcon;
      ImageIcon sqlExportTableIcon, sqlExportTabSummaryTableIcon;
      ImageIcon sqlExportDatabaseIcon, sqlExportDatabaseSchemeIcon;
      ImageIcon queryFrameIcon, reloadDatabaseIcon, searchDatabaseIcon; 
      ImageIcon manualIcon; //legalIcon, releaseIcon, aboutIcon;
      
      MyJSQLView_ResourceBundle resourceBundle;
      String resource;
      
      // Setting up icons directory  & resource instances.

      iconsDirectory = MyJSQLView_Utils.getIconsDirectory() + MyJSQLView_Utils.getFileSeparator();
      resourceBundle = MyJSQLView.getLocaleResourceBundle();

      // JTool Bar for the Frame.
      buttonItem = null;
      setBorder(BorderFactory.createLoweredBevelBorder());
      setFloatable(false);

      // ===============
      // File Menu
      
      // File Open
      openIcon = new ImageIcon(iconsDirectory + "openIcon.png");
      resource = resourceBundle.getResource("MyJSQLView_JToolBar.tooltip.Open");
      if (resource.equals(""))
         buttonItem = buttonItem("Open", openIcon, ACTION_OPEN);
      else
         buttonItem = buttonItem(resource, openIcon, ACTION_OPEN);
      add(buttonItem);
      
      // File Save
      saveIcon = new ImageIcon(iconsDirectory + "saveIcon.png");
      resource = resourceBundle.getResource("MyJSQLView_JToolBar.tooltip.Save");
      if (resource.equals(""))
         buttonItem = buttonItem("Save", saveIcon, ACTION_SAVE);
      else
         buttonItem = buttonItem(resource, saveIcon, ACTION_SAVE);
      add(buttonItem);
      
      // File Print
      printIcon = new ImageIcon(iconsDirectory + "printIcon.png");
      resource = resourceBundle.getResource("MyJSQLView_JToolBar.tooltip.Print");
      if (resource.equals(""))
         buttonItem = buttonItem("Print", printIcon, ACTION_PRINT);
      else
         buttonItem = buttonItem(resource, printIcon, ACTION_PRINT);
      add(buttonItem);
      
      // Page Format
      pageFormatIcon = new ImageIcon(iconsDirectory + "pageFormatIcon.png");
      resource = resourceBundle.getResource("MyJSQLView_JToolBar.tooltip.PageFormat");
      if (resource.equals(""))
         buttonItem = buttonItem("Page Format", pageFormatIcon, ACTION_PAGE_FORMAT);
      else
         buttonItem = buttonItem(resource, pageFormatIcon, ACTION_PAGE_FORMAT);
      add(buttonItem);
      
      // Exit
      exitIcon = new ImageIcon(iconsDirectory + "exitIcon.png");
      resource = resourceBundle.getResource("MyJSQLView_JToolBar.tooltip.Exit");
      if (resource.equals(""))
         buttonItem = buttonItem("Exit", exitIcon, ACTION_EXIT);
      else
         buttonItem = buttonItem(resource, exitIcon, ACTION_EXIT);
      add(buttonItem);
      
      addSeparator();
      
      // ===============
      // Edit Menu
      
      // Preferences
      preferencesIcon = new ImageIcon(iconsDirectory + "preferencesIcon.png");
      resource = resourceBundle.getResource("MyJSQLView_JToolBar.tooltip.Preferences");
      if (resource.equals(""))
         buttonItem = buttonItem("Preferences", preferencesIcon, ACTION_PREFERENCES);
      else
         buttonItem = buttonItem(resource, preferencesIcon, ACTION_PREFERENCES);
      add(buttonItem);
      
      addSeparator();
     
      // ===============
      // Data Menu
      
      // Import CSV File
      csvImportIcon = new ImageIcon(iconsDirectory + "csvImportIcon.png");
      resource = resourceBundle.getResource("MyJSQLView_JToolBar.tooltip.ImportCSVFile");
      if (resource.equals(""))
         buttonItem = buttonItem("Import CSV File", csvImportIcon, ACTION_IMPORT_CSV_FILE);
      else
         buttonItem = buttonItem(resource, csvImportIcon, ACTION_IMPORT_CSV_FILE);
      add(buttonItem);
      
      // Import SQL Dump
      sqlImportIcon = new ImageIcon(iconsDirectory + "sqlImportIcon.png");
      resource = resourceBundle.getResource("MyJSQLView_JToolBar.tooltip.ImportSQLDump");
      if (resource.equals(""))
         buttonItem = buttonItem("Import SQL Dump", sqlImportIcon, ACTION_IMPORT_SQL_DUMP);
      else
         buttonItem = buttonItem(resource, sqlImportIcon, ACTION_IMPORT_SQL_DUMP);
      add(buttonItem);
      
      addSeparator();
      
      // Export CSV Tab Summary Table
      csvExportTabSummaryTableIcon = new ImageIcon(iconsDirectory + "csvExportSummaryTableIcon.png");
      resource = resourceBundle.getResource("MyJSQLView_JToolBar.tooltip.ExportCSVSummaryTable");
      if (resource.equals(""))
         buttonItem = buttonItem("Export CSV Summary Table", csvExportTabSummaryTableIcon,
                                 ACTION_EXPORT_CSV_SUMMARY_TABLE);
      else
         buttonItem = buttonItem(resource, csvExportTabSummaryTableIcon, ACTION_EXPORT_CSV_SUMMARY_TABLE);
      add(buttonItem);
      
      // Export CSV Table
      csvExportTableIcon = new ImageIcon(iconsDirectory + "csvExportTableIcon.png");
      resource = resourceBundle.getResource("MyJSQLView_JToolBar.tooltip.ExportCSVTable");
      if (resource.equals(""))
         buttonItem = buttonItem("Export CSV Table", csvExportTableIcon, ACTION_EXPORT_CSV_TABLE);
      else
         buttonItem = buttonItem(resource, csvExportTableIcon, ACTION_EXPORT_CSV_TABLE);
      add(buttonItem);
      
      addSeparator();
      
      // Export PDF Tab Summary Table
      pdfExportTabSummaryTableIcon = new ImageIcon(iconsDirectory + "pdfExportSummaryTableIcon.png");
      resource = resourceBundle.getResource("MyJSQLView_JToolBar.tooltip.ExportPDFSummaryTable");
      if (resource.equals(""))
         buttonItem = buttonItem("Export PDF Summary Table", pdfExportTabSummaryTableIcon,
                                 ACTION_EXPORT_PDF_SUMMARY_TABLE);
      else
         buttonItem = buttonItem(resource, pdfExportTabSummaryTableIcon, ACTION_EXPORT_PDF_SUMMARY_TABLE);
      add(buttonItem);
      
      addSeparator();
      
      // Export SQL Tab Summary Table
      sqlExportTabSummaryTableIcon = new ImageIcon(iconsDirectory + "sqlExportSummaryTableIcon.png");
      resource = resourceBundle.getResource("MyJSQLView_JToolBar.tooltip.ExportSQLSummaryTable");
      if (resource.equals(""))
         buttonItem = buttonItem("Export SQL Summary Table", sqlExportTabSummaryTableIcon,
                                 ACTION_EXPORT_SQL_SUMMARY_TABLE);
      else
         buttonItem = buttonItem(resource, sqlExportTabSummaryTableIcon, ACTION_EXPORT_SQL_SUMMARY_TABLE);
      add(buttonItem);
      
      // Export SQL Table
      sqlExportTableIcon = new ImageIcon(iconsDirectory + "sqlExportTableIcon.png");
      resource = resourceBundle.getResource("MyJSQLView_JToolBar.tooltip.ExportSQLTable");
      if (resource.equals(""))
         buttonItem = buttonItem("Export SQL Table", sqlExportTableIcon, ACTION_EXPORT_SQL_TABLE);
      else
         buttonItem = buttonItem(resource, sqlExportTableIcon, ACTION_EXPORT_SQL_TABLE);
      add(buttonItem);
      
      // Export SQL Database
      sqlExportDatabaseIcon = new ImageIcon(iconsDirectory + "sqlExportDatabaseIcon.png");
      resource = resourceBundle.getResource("MyJSQLView_JToolBar.tooltip.ExportSQLDatabase");
      if (resource.equals(""))
         buttonItem = buttonItem("Export SQL Database", sqlExportDatabaseIcon, ACTION_EXPORT_SQL_DATABASE);
      else
         buttonItem = buttonItem(resource, sqlExportDatabaseIcon, ACTION_EXPORT_SQL_DATABASE);
      add(buttonItem);
      
      // Export SQL Database Scheme
      sqlExportDatabaseSchemeIcon = new ImageIcon(iconsDirectory + "sqlExportDatabaseSchemeIcon.png");
      resource = resourceBundle.getResource("MyJSQLView_JToolBar.tooltip.ExportSQLDatabaseScheme");
      if (resource.equals(""))
         buttonItem = buttonItem("Export SQL Database Scheme", sqlExportDatabaseSchemeIcon,
                                 ACTION_EXPORT_SQL_DATABASE_SCHEME);
      else
         buttonItem = buttonItem(resource, sqlExportDatabaseSchemeIcon, ACTION_EXPORT_SQL_DATABASE_SCHEME);
      add(buttonItem);
      
      addSeparator();
      
      // ===============
      // Tools Menu
      
      // Query Frame
      queryFrameIcon = new ImageIcon(iconsDirectory + "queryFrameIcon.png");
      resource = resourceBundle.getResource("MyJSQLView_JToolBar.tooltip.QueryFrame");
      if (resource.equals(""))
         buttonItem = buttonItem("Query Frame", queryFrameIcon, ACTION_QUERY_FRAME);
      else
         buttonItem = buttonItem(resource, queryFrameIcon, ACTION_QUERY_FRAME);
      add(buttonItem);
      
      // Reload Database
      reloadDatabaseIcon = new ImageIcon(iconsDirectory + "reloadDatabaseIcon.png");
      resource = resourceBundle.getResource("MyJSQLView_JToolBar.tooltip.ReloadDatabase");
      if (resource.equals(""))
         buttonItem = buttonItem("Reload Database", reloadDatabaseIcon, ACTION_RELOAD_DATABASE);
      else
         buttonItem = buttonItem(resource, reloadDatabaseIcon, ACTION_RELOAD_DATABASE);
      add(buttonItem);
      
      // Search Database
      searchDatabaseIcon = new ImageIcon(iconsDirectory + "searchDatabaseIcon.png");
      resource = resourceBundle.getResource("MyJSQLView_JToolBar.tooltip.SearchDatabase");
      if (resource.equals(""))
         buttonItem = buttonItem("Search Database", searchDatabaseIcon, ACTION_SEARCH_DATABASE);
      else
         buttonItem = buttonItem(resource, searchDatabaseIcon, ACTION_SEARCH_DATABASE);
      add(buttonItem);
      
      addSeparator();
      
      // ===============
      // Help Menu
      
      // Manual
      manualIcon = new ImageIcon(iconsDirectory + "manualIcon.png");
      resource = resourceBundle.getResource("MyJSQLView_JToolBar.tooltip.Manual");
      if (resource.equals(""))
         buttonItem = buttonItem("Manual", manualIcon, ACTION_MANUAL);
      else
         buttonItem = buttonItem(resource, manualIcon, ACTION_MANUAL);
      add(buttonItem);
      
      // Legal
      //legalIcon = new ImageIcon(iconsDirectory + "legalIcon.png");
      //resource = resourceBundle.getResource("MyJSQLView_JToolBar.tooltip.Legal");
      //if (resource.equals(""))
      //   buttonItem = buttonItem("Legal", legalIcon, ACTION_LEGAL);
      //else
      //   buttonItem = buttonItem(resource, legalIcon, ACTION_LEGAL);
      //add(buttonItem);
      
      // Release
      //releaseIcon = new ImageIcon(iconsDirectory + "releaseIcon.png");
      //resource = resourceBundle.getResource("MyJSQLView_JToolBar.tooltip.Release");
      //if (resource.equals(""))
      //   buttonItem = buttonItem("Release", releaseIcon, ACTION_RELEASE_NOTES);
      //else
      //   buttonItem = buttonItem(resource, releaseIcon, ACTION_RELEASE_NOTES);
      //add(buttonItem);
      
      // About
      //aboutIcon = new ImageIcon(iconsDirectory + "aboutIcon.png");
      //resource = resourceBundle.getResource("MyJSQLView_JToolBar.tooltip.About");
      //if (resource.equals(""))
      //   buttonItem = buttonItem("About", aboutIcon, ACTION_ABOUT);
      //else
      //   buttonItem = buttonItem(resource, aboutIcon, ACTION_ABOUT);
      //add(buttonItem);
   }

   //==============================================================
   // Instance method used for the MyJSQLView's creation of tool
   // bar button items. Helper Method.
   //==============================================================

   private JButton buttonItem(String toolTip, ImageIcon icon, String actionLabel)
   {
      JButton item = new JButton(icon);
      item.setFocusable(false);
      item.setMargin(new Insets(0, 0, 0, 0));
      item.setToolTipText(toolTip);
      item.setActionCommand(actionLabel);
      item.addActionListener(mainFrame);
      
      return item;
   }
}