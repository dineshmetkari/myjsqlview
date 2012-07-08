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
// Copyright (C) 2005-2012 Dana M. Proctor.
// Version 2.6 07/08/2012
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
//         2.4 Added Tools | SQL Query Bucket ToolBar Button.
//         2.5 Copyright Update.
//         2.6 Changes in Way MyJSQLView_ResourceBundle Handles the Collection
//             of Resource Strings. Change to resource.getResourceString(key,
//             default).
//         
//-----------------------------------------------------------------
//                 danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview;

import java.awt.Insets;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToolBar;

/**
 *    The MyJSQLView_JToolBar class is used to construct the toolbar
 * for the MyJSQLView application frame's database tables tab.
 * 
 * @author Dana M. Proctor
 * @version 2.6 07/08/2012
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
      ImageIcon sqlQueryBucketIcon, queryFrameIcon;
      ImageIcon reloadDatabaseIcon, searchDatabaseIcon; 
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
      
      /* test
      String path;
      path = "http://dandymadeproductions.com:80/temp/openScriptIcon.png";
      path = "file:" + "images/icons/" + "openIcon.png";
      URL url = null;
      openIcon = null;
      try
      {
         url = new URL(path);
         System.out.println("protocol:" + url.getProtocol() + " host:" + url.getHost()
                            + " port:" + url.getPort() +  " file:" + url.getFile()
                            + "\n" + "URL:" + url.toExternalForm());
         openIcon = new ImageIcon(url);
      }
      catch (MalformedURLException mfe)
      {
         System.out.println("failed");
      }
      System.out.println("iconsDirectory: " + iconsDirectory);
      
      test ended */
      
      openIcon = new ImageIcon(iconsDirectory + "openIcon.png");
      resource = resourceBundle.getResourceString("MyJSQLView_JToolBar.tooltip.Open", "Open");
      buttonItem = buttonItem(resource, openIcon, ACTION_OPEN);
      add(buttonItem);
      
      // File Save
      saveIcon = new ImageIcon(iconsDirectory + "saveIcon.png");
      resource = resourceBundle.getResourceString("MyJSQLView_JToolBar.tooltip.Save", "Save");
      buttonItem = buttonItem(resource, saveIcon, ACTION_SAVE);
      add(buttonItem);
      
      // File Print
      printIcon = new ImageIcon(iconsDirectory + "printIcon.png");
      resource = resourceBundle.getResourceString("MyJSQLView_JToolBar.tooltip.Print", "Pring");
      buttonItem = buttonItem(resource, printIcon, ACTION_PRINT);
      add(buttonItem);
      
      // Page Format
      pageFormatIcon = new ImageIcon(iconsDirectory + "pageFormatIcon.png");
      resource = resourceBundle.getResourceString("MyJSQLView_JToolBar.tooltip.PageFormat", "Page Format");
      buttonItem = buttonItem(resource, pageFormatIcon, ACTION_PAGE_FORMAT);
      add(buttonItem);
      
      // Exit
      exitIcon = new ImageIcon(iconsDirectory + "exitIcon.png");
      resource = resourceBundle.getResourceString("MyJSQLView_JToolBar.tooltip.Exit", "Exit");
      buttonItem = buttonItem(resource, exitIcon, ACTION_EXIT);
      add(buttonItem);
      
      addSeparator();
      
      // ===============
      // Edit Menu
      
      // Preferences
      preferencesIcon = new ImageIcon(iconsDirectory + "preferencesIcon.png");
      resource = resourceBundle.getResourceString("MyJSQLView_JToolBar.tooltip.Preferences", "Preferences");
      buttonItem = buttonItem(resource, preferencesIcon, ACTION_PREFERENCES);
      add(buttonItem);
      
      addSeparator();
     
      // ===============
      // Data Menu
      
      // Import CSV File
      csvImportIcon = new ImageIcon(iconsDirectory + "csvImportIcon.png");
      resource = resourceBundle.getResourceString("MyJSQLView_JToolBar.tooltip.ImportCSVFile",
                                                  "Import CSV File");
      buttonItem = buttonItem(resource, csvImportIcon, ACTION_IMPORT_CSV_FILE);
      add(buttonItem);
      
      // Import SQL Dump
      sqlImportIcon = new ImageIcon(iconsDirectory + "sqlImportIcon.png");
      resource = resourceBundle.getResourceString("MyJSQLView_JToolBar.tooltip.ImportSQLDump",
                                                  "Import SQL Dump");
      buttonItem = buttonItem(resource, sqlImportIcon, ACTION_IMPORT_SQL_DUMP);
      add(buttonItem);
      
      addSeparator();
      
      // Export CSV Tab Summary Table
      csvExportTabSummaryTableIcon = new ImageIcon(iconsDirectory + "csvExportSummaryTableIcon.png");
      resource = resourceBundle.getResourceString("MyJSQLView_JToolBar.tooltip.ExportCSVSummaryTable",
                                                  "Export CSV Summary Table");
      buttonItem = buttonItem(resource, csvExportTabSummaryTableIcon, ACTION_EXPORT_CSV_SUMMARY_TABLE);
      add(buttonItem);
      
      // Export CSV Table
      csvExportTableIcon = new ImageIcon(iconsDirectory + "csvExportTableIcon.png");
      resource = resourceBundle.getResourceString("MyJSQLView_JToolBar.tooltip.ExportCSVTable",
                                                  "Export CSV Table");
      buttonItem = buttonItem(resource, csvExportTableIcon, ACTION_EXPORT_CSV_TABLE);
      add(buttonItem);
      
      addSeparator();
      
      // Export PDF Tab Summary Table
      pdfExportTabSummaryTableIcon = new ImageIcon(iconsDirectory + "pdfExportSummaryTableIcon.png");
      resource = resourceBundle.getResourceString("MyJSQLView_JToolBar.tooltip.ExportPDFSummaryTable",
                                                  "Export PDF Summary Table");
      buttonItem = buttonItem(resource, pdfExportTabSummaryTableIcon, ACTION_EXPORT_PDF_SUMMARY_TABLE);
      add(buttonItem);
      
      addSeparator();
      
      // Export SQL Tab Summary Table
      sqlExportTabSummaryTableIcon = new ImageIcon(iconsDirectory + "sqlExportSummaryTableIcon.png");
      resource = resourceBundle.getResourceString("MyJSQLView_JToolBar.tooltip.ExportSQLSummaryTable",
                                                  "Export SQL Summary Table");
      buttonItem = buttonItem(resource, sqlExportTabSummaryTableIcon, ACTION_EXPORT_SQL_SUMMARY_TABLE);
      add(buttonItem);
      
      // Export SQL Table
      sqlExportTableIcon = new ImageIcon(iconsDirectory + "sqlExportTableIcon.png");
      resource = resourceBundle.getResourceString("MyJSQLView_JToolBar.tooltip.ExportSQLTable",
                                                  "Export SQL Table");
      buttonItem = buttonItem(resource, sqlExportTableIcon, ACTION_EXPORT_SQL_TABLE);
      add(buttonItem);
      
      // Export SQL Database
      sqlExportDatabaseIcon = new ImageIcon(iconsDirectory + "sqlExportDatabaseIcon.png");
      resource = resourceBundle.getResourceString("MyJSQLView_JToolBar.tooltip.ExportSQLDatabase",
                                                  "Export SQL Database");
      buttonItem = buttonItem(resource, sqlExportDatabaseIcon, ACTION_EXPORT_SQL_DATABASE);
      add(buttonItem);
      
      // Export SQL Database Scheme
      sqlExportDatabaseSchemeIcon = new ImageIcon(iconsDirectory + "sqlExportDatabaseSchemeIcon.png");
      resource = resourceBundle.getResourceString("MyJSQLView_JToolBar.tooltip.ExportSQLDatabaseScheme",
                                                  "Export SQL Database Scheme");
      buttonItem = buttonItem(resource, sqlExportDatabaseSchemeIcon, ACTION_EXPORT_SQL_DATABASE_SCHEME);
      add(buttonItem);
      
      addSeparator();
      
      // ===============
      // Tools Menu
      
      // SQL Query Bucket Frame
      sqlQueryBucketIcon = new ImageIcon(iconsDirectory + "sqlQueryBucketIcon.png");
      resource = resourceBundle.getResourceString("MyJSQLView_JToolBar.tooltip.SQLQueryBucket",
                                                  "SQL Query Bucket");
     buttonItem = buttonItem(resource, sqlQueryBucketIcon, ACTION_SQL_QUERY_BUCKET);
      add(buttonItem);
      
      // Query Frame
      queryFrameIcon = new ImageIcon(iconsDirectory + "queryFrameIcon.png");
      resource = resourceBundle.getResourceString("MyJSQLView_JToolBar.tooltip.QueryFrame",
                                                  "Query Frame");
      buttonItem = buttonItem(resource, queryFrameIcon, ACTION_QUERY_FRAME);
      add(buttonItem);
      
      // Reload Database
      reloadDatabaseIcon = new ImageIcon(iconsDirectory + "reloadDatabaseIcon.png");
      resource = resourceBundle.getResourceString("MyJSQLView_JToolBar.tooltip.ReloadDatabase",
                                                  "Reload Database");
      buttonItem = buttonItem(resource, reloadDatabaseIcon, ACTION_RELOAD_DATABASE);
      add(buttonItem);
      
      // Search Database
      searchDatabaseIcon = new ImageIcon(iconsDirectory + "searchDatabaseIcon.png");
      resource = resourceBundle.getResourceString("MyJSQLView_JToolBar.tooltip.SearchDatabase",
                                                  "Search Database");
      buttonItem = buttonItem(resource, searchDatabaseIcon, ACTION_SEARCH_DATABASE);
      add(buttonItem);
      
      addSeparator();
      
      // ===============
      // Help Menu
      
      // Manual
      manualIcon = new ImageIcon(iconsDirectory + "manualIcon.png");
      resource = resourceBundle.getResourceString("MyJSQLView_JToolBar.tooltip.Manual", "Manual");
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