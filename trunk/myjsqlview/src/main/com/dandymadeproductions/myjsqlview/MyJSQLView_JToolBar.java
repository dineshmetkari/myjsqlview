//=================================================================
//                    MyJSQLView_JToolBar Class
//=================================================================
//
//    This class is used to constructed the toolbar for the
// MyJSQLView application frame.
//
//               << MyJSQLView_JToolBar.java >>
//
//=================================================================
// Copyright (C) 2005-2010 Dana M. Proctor.
// Version 1.8 02/26/2010
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
 *    The MyJSQLView_JToolBar class is used to constructed the toolbar
 * for the MyJSQLView application frame.
 * 
 * @author Dana M. Proctor
 * @version 1.8 02/26/2010
 */

class MyJSQLView_JToolBar extends JToolBar
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

      // ===============
      // File Menu
      
      // File Open
      openIcon = new ImageIcon(iconsDirectory + "openIcon.png");
      resource = resourceBundle.getResource("MyJSQLView_JToolBar.tooltip.Open");
      if (resource.equals(""))
         buttonItem = buttonItem("Open", openIcon, "FO");
      else
         buttonItem = buttonItem(resource, openIcon, "FO");
      add(buttonItem);
      
      // File Save
      saveIcon = new ImageIcon(iconsDirectory + "saveIcon.png");
      resource = resourceBundle.getResource("MyJSQLView_JToolBar.tooltip.Save");
      if (resource.equals(""))
         buttonItem = buttonItem("Save", saveIcon, "FS");
      else
         buttonItem = buttonItem(resource, saveIcon, "FS");
      add(buttonItem);
      
      // File Print
      printIcon = new ImageIcon(iconsDirectory + "printIcon.png");
      resource = resourceBundle.getResource("MyJSQLView_JToolBar.tooltip.Print");
      if (resource.equals(""))
         buttonItem = buttonItem("Print", printIcon, "FP");
      else
         buttonItem = buttonItem(resource, printIcon, "FP");
      add(buttonItem);
      
      // Page Format
      pageFormatIcon = new ImageIcon(iconsDirectory + "pageFormatIcon.png");
      resource = resourceBundle.getResource("MyJSQLView_JToolBar.tooltip.PageFormat");
      if (resource.equals(""))
         buttonItem = buttonItem("Page Format", pageFormatIcon, "FPG");
      else
         buttonItem = buttonItem(resource, pageFormatIcon, "FPG");
      add(buttonItem);
      
      // Exit
      exitIcon = new ImageIcon(iconsDirectory + "exitIcon.png");
      resource = resourceBundle.getResource("MyJSQLView_JToolBar.tooltip.Exit");
      if (resource.equals(""))
         buttonItem = buttonItem("Exit", exitIcon, "FE");
      else
         buttonItem = buttonItem(resource, exitIcon, "FE");
      add(buttonItem);
      
      addSeparator();
      
      // ===============
      // Edit Menu
      
      // Preferences
      preferencesIcon = new ImageIcon(iconsDirectory + "preferencesIcon.png");
      resource = resourceBundle.getResource("MyJSQLView_JToolBar.tooltip.Preferences");
      if (resource.equals(""))
         buttonItem = buttonItem("Preferences", preferencesIcon, "EP");
      else
         buttonItem = buttonItem(resource, preferencesIcon, "EP");
      add(buttonItem);
      
      addSeparator();
     
      // ===============
      // Data Menu
      
      // Import CSV File
      csvImportIcon = new ImageIcon(iconsDirectory + "csvImportIcon.png");
      resource = resourceBundle.getResource("MyJSQLView_JToolBar.tooltip.ImportCSVFile");
      if (resource.equals(""))
         buttonItem = buttonItem("Import CSV File", csvImportIcon, "DICSVF");
      else
         buttonItem = buttonItem(resource, csvImportIcon, "DICSVF");
      add(buttonItem);
      
      // Import SQL Dump
      sqlImportIcon = new ImageIcon(iconsDirectory + "sqlImportIcon.png");
      resource = resourceBundle.getResource("MyJSQLView_JToolBar.tooltip.ImportSQLDump");
      if (resource.equals(""))
         buttonItem = buttonItem("Import SQL Dump", sqlImportIcon, "DISQLD");
      else
         buttonItem = buttonItem(resource, sqlImportIcon, "DISQLD");
      add(buttonItem);
      
      addSeparator();
      
      // Export CSV Tab Summary Table
      csvExportTabSummaryTableIcon = new ImageIcon(iconsDirectory + "csvExportSummaryTableIcon.png");
      resource = resourceBundle.getResource("MyJSQLView_JToolBar.tooltip.ExportCSVSummaryTable");
      if (resource.equals(""))
         buttonItem = buttonItem("Export CSV Summary Table", csvExportTabSummaryTableIcon, "DECSVST");
      else
         buttonItem = buttonItem(resource, csvExportTabSummaryTableIcon, "DECSVST");
      add(buttonItem);
      
      // Export CSV Table
      csvExportTableIcon = new ImageIcon(iconsDirectory + "csvExportTableIcon.png");
      resource = resourceBundle.getResource("MyJSQLView_JToolBar.tooltip.ExportCSVTable");
      if (resource.equals(""))
         buttonItem = buttonItem("Export CSV Table", csvExportTableIcon, "DECSVT");
      else
         buttonItem = buttonItem(resource, csvExportTableIcon, "DECSVT");
      add(buttonItem);
      
      addSeparator();
      
      // Export SQL Tab Summary Table
      sqlExportTabSummaryTableIcon = new ImageIcon(iconsDirectory + "sqlExportSummaryTableIcon.png");
      resource = resourceBundle.getResource("MyJSQLView_JToolBar.tooltip.ExportSQLSummaryTable");
      if (resource.equals(""))
         buttonItem = buttonItem("Export SQL Summary Table", sqlExportTabSummaryTableIcon, "DESQLST");
      else
         buttonItem = buttonItem(resource, sqlExportTabSummaryTableIcon, "DESQLST");
      add(buttonItem);
      
      // Export SQL Table
      sqlExportTableIcon = new ImageIcon(iconsDirectory + "sqlExportTableIcon.png");
      resource = resourceBundle.getResource("MyJSQLView_JToolBar.tooltip.ExportSQLTable");
      if (resource.equals(""))
         buttonItem = buttonItem("Export SQL Table", sqlExportTableIcon, "DESQLT");
      else
         buttonItem = buttonItem(resource, sqlExportTableIcon, "DESQLT");
      add(buttonItem);
      
      // Export SQL Database
      sqlExportDatabaseIcon = new ImageIcon(iconsDirectory + "sqlExportDatabaseIcon.png");
      resource = resourceBundle.getResource("MyJSQLView_JToolBar.tooltip.ExportSQLDatabase");
      if (resource.equals(""))
         buttonItem = buttonItem("Export SQL Database", sqlExportDatabaseIcon, "DESQLD");
      else
         buttonItem = buttonItem(resource, sqlExportDatabaseIcon, "DESQLD");
      add(buttonItem);
      
      // Export SQL Database Scheme
      sqlExportDatabaseSchemeIcon = new ImageIcon(iconsDirectory + "sqlExportDatabaseSchemeIcon.png");
      resource = resourceBundle.getResource("MyJSQLView_JToolBar.tooltip.ExportSQLDatabaseScheme");
      if (resource.equals(""))
         buttonItem = buttonItem("Export SQL Database Scheme", sqlExportDatabaseSchemeIcon, "DESQLDS");
      else
         buttonItem = buttonItem(resource, sqlExportDatabaseSchemeIcon, "DESQLDS");
      add(buttonItem);
      
      addSeparator();
      
      // ===============
      // Tools Menu
      
      // Query Frame
      queryFrameIcon = new ImageIcon(iconsDirectory + "queryFrameIcon.png");
      resource = resourceBundle.getResource("MyJSQLView_JToolBar.tooltip.QueryFrame");
      if (resource.equals(""))
         buttonItem = buttonItem("Query Frame", queryFrameIcon, "TQ");
      else
         buttonItem = buttonItem(resource, queryFrameIcon, "TQ");
      add(buttonItem);
      
      // Reload Database
      reloadDatabaseIcon = new ImageIcon(iconsDirectory + "reloadDatabaseIcon.png");
      resource = resourceBundle.getResource("MyJSQLView_JToolBar.tooltip.ReloadDatabase");
      if (resource.equals(""))
         buttonItem = buttonItem("Reload Database", reloadDatabaseIcon, "TRD");
      else
         buttonItem = buttonItem(resource, reloadDatabaseIcon, "TRD");
      add(buttonItem);
      
      // Search Database
      searchDatabaseIcon = new ImageIcon(iconsDirectory + "searchDatabaseIcon.png");
      resource = resourceBundle.getResource("MyJSQLView_JToolBar.tooltip.SearchDatabase");
      if (resource.equals(""))
         buttonItem = buttonItem("Search Database", searchDatabaseIcon, "TSD");
      else
         buttonItem = buttonItem(resource, searchDatabaseIcon, "TSD");
      add(buttonItem);
      
      addSeparator();
      
      // ===============
      // Help Menu
      
      // Manual
      manualIcon = new ImageIcon(iconsDirectory + "manualIcon.png");
      resource = resourceBundle.getResource("MyJSQLView_JToolBar.tooltip.Manual");
      if (resource.equals(""))
         buttonItem = buttonItem("Manual", manualIcon, "HM");
      else
         buttonItem = buttonItem(resource, manualIcon, "HM");
      add(buttonItem);
      
      // Legal
      //legalIcon = new ImageIcon(iconsDirectory + "legalIcon.png");
      //resource = resourceBundle.getResource("MyJSQLView_JToolBar.tooltip.Legal");
      //if (resource.equals(""))
      //   buttonItem = buttonItem("Legal", legalIcon, "HL");
      //else
      //   buttonItem = buttonItem(resource, legalIcon, "HL");
      //add(buttonItem);
      
      // Release
      //releaseIcon = new ImageIcon(iconsDirectory + "releaseIcon.png");
      //resource = resourceBundle.getResource("MyJSQLView_JToolBar.tooltip.Release");
      //if (resource.equals(""))
      //   buttonItem = buttonItem("Release", releaseIcon, "HR");
      //else
      //   buttonItem = buttonItem(resource, releaseIcon, "HR");
      //add(buttonItem);
      
      // About
      //aboutIcon = new ImageIcon(iconsDirectory + "aboutIcon.png");
      //resource = resourceBundle.getResource("MyJSQLView_JToolBar.tooltip.About");
      //if (resource.equals(""))
      //   buttonItem = buttonItem("About", aboutIcon, "HA");
      //else
      //   buttonItem = buttonItem(resource, aboutIcon, "HA");
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