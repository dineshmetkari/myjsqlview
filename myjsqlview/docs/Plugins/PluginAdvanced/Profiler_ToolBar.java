//=================================================================
//           TableFieldProfiler Profiler_ToolBar Class
//=================================================================
//
//    This class is used to construct the toolbar to be used in the
// Table Field Profiler plugin.
//
//                  << Profiler_ToolBar.java >>
//
//=================================================================
// Copyright (C) 2010-2012 Dana M. Proctor.
// Version 2.1 01/28/2012
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
// Version 1.0 Original Dandy Data Profiler Profiler_ToolBar Class.
//         1.1 Added Separators.
//         1.2 Moved Over to Table Field Profiler Code.
//         1.3 Added Field Menu Items to ToolBar. Also MyJSQLView_ResourceBundle
//             Argument to Constructor.
//         1.4 Implemented/Reviewed Locale Instances.
//         1.5 Added path Argument to Constructor.
//         1.6 Changed the Derivation of the Action Command for the File
//             Menu Exit to MyJSQLView_MenuActionCommands.ACTION_EXIT.
//         1.7 Removed Tool Bar Items File | Open & Save. Added Toolbar Item
//             SQL Bucket Frame.
//         1.8 Added Back ToolBar Item File | Open.
//         1.9 Added Constructor Instance dataClusterIcon. With New Icon Created
//             Field Cluster Analysis Menu Item in Toolbar.
//         2.0 Commented Out the Tools Cluster Analysis for Version Release 4.0.
//         2.1 Uncomment the Tools Cluster Analysis Button Items. 
//         
//-----------------------------------------------------------------
//                 danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.tablefieldprofiler;

import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToolBar;

import com.dandymadeproductions.myjsqlview.MyJSQLView_Frame;
import com.dandymadeproductions.myjsqlview.MyJSQLView_ResourceBundle;
import com.dandymadeproductions.myjsqlview.MyJSQLView_MenuActionCommands;
import com.dandymadeproductions.myjsqlview.MyJSQLView_Utils;

/**
 *    The Profiler_TooBar class is used to construct the toolbar to be used
 * in the Table Field Profiler plugin.
 * 
 * @author Dana M. Proctor
 * @version 2.1 01/28/2012
 */

class Profiler_ToolBar extends JToolBar
{
   // Instance & Class Fields.
   private static final long serialVersionUID = 4305177851277903429L;

   private MyJSQLView_Frame mainFrame;
   private transient MenuActionListener menuListener;

   //==============================================================
   // Profiler_ToolBar Constructor.
   //==============================================================

   protected Profiler_ToolBar(String title, MyJSQLView_Frame parent, String path,
                              MyJSQLView_ResourceBundle resourceBundle, MenuActionListener plugin)
   {
      super(title);
      mainFrame = parent;
      menuListener = plugin;

      // Constructor Instances
      String fileSeparator, iconsDirectory, resource;
      ImageIcon openIcon, exitIcon;
      ImageIcon sqlQueryBucketIcon, dataInformationIcon, dataAnalysisIcon; 
      ImageIcon dataClusterIcon;
      JButton buttonItem;

      // Setting up icons directory and other instances.

      buttonItem = null;
      fileSeparator = MyJSQLView_Utils.getFileSeparator();
      iconsDirectory = path + fileSeparator + "TableFieldProfiler" + fileSeparator + "images"
                       + fileSeparator + "icons" + fileSeparator;

      // Tool Bar Configuration.
      setBorder(BorderFactory.createLoweredBevelBorder());
      setFloatable(false);

      // ===============
      // File Menu
      
      openIcon = new ImageIcon(iconsDirectory + "openIcon.png");
      
      resource = resourceBundle.getResource("Profiler_MenuBar.menu.Open");
      if (resource.equals(""))
         buttonItem = buttonItem("Open", openIcon, Profiler_MenuBar.ACTION_FILE_OPEN);
      else
         buttonItem = buttonItem(resource, openIcon, Profiler_MenuBar.ACTION_FILE_OPEN);
      add(buttonItem);
      
      // Exit
      exitIcon = new ImageIcon(iconsDirectory + "exitIcon.png");
      
      buttonItem = new JButton(exitIcon);
      buttonItem.setFocusable(false);
      buttonItem.setMargin(new Insets(0, 0, 0, 0));
      buttonItem.setActionCommand(MyJSQLView_MenuActionCommands.ACTION_EXIT);
      buttonItem.addActionListener(mainFrame);
      
      resource = resourceBundle.getResource("Profiler_MenuBar.menu.Exit");
      if (resource.equals(""))
         buttonItem.setToolTipText("Exit");
      else
         buttonItem.setToolTipText(resource);
      add(buttonItem);
      
      addSeparator();
            
      // ===============
      // Tool Menu
      
      // SQL Query Bucket
      sqlQueryBucketIcon = new ImageIcon(iconsDirectory + "sqlQueryBucketIcon.png");
      
      buttonItem = new JButton(sqlQueryBucketIcon);
      buttonItem.setFocusable(false);
      buttonItem.setMargin(new Insets(0, 0, 0, 0));
      buttonItem.setActionCommand(MyJSQLView_MenuActionCommands.ACTION_SQL_QUERY_BUCKET);
      buttonItem.addActionListener(mainFrame);
      
      resource = resourceBundle.getResource("Profiler_MenuBar.menu.SQLQueryBucket");
      if (resource.equals(""))
         buttonItem.setToolTipText("SQL Query Bucket");
      else
         buttonItem.setToolTipText(resource);
      add(buttonItem);
      
      addSeparator();

      // Information
      dataInformationIcon = new ImageIcon(iconsDirectory + "informationIcon.png");
      
      resource = resourceBundle.getResource("Profiler_MenuBar.menu.FieldInformation");
      if (resource.equals(""))
         buttonItem = buttonItem("Field Information", dataInformationIcon,
                                 Profiler_MenuBar.ACTION_FIELD_INFORMATION);
      else
         buttonItem = buttonItem(resource, dataInformationIcon, Profiler_MenuBar.ACTION_FIELD_INFORMATION);
      add(buttonItem);

      // Analysis
      dataAnalysisIcon = new ImageIcon(iconsDirectory + "analysisIcon.png");
      
      resource = resourceBundle.getResource("Profiler_MenuBar.menu.FieldNumberAnalysis");
      if (resource.equals(""))
         buttonItem = buttonItem("Field Number Analysis", dataAnalysisIcon,
            Profiler_MenuBar.ACTION_FIELD_NUMBER_ANALYSIS);
      else
         buttonItem = buttonItem(resource, dataAnalysisIcon, Profiler_MenuBar.ACTION_FIELD_NUMBER_ANALYSIS);
      add(buttonItem);
      
      // Cluster
      dataClusterIcon = new ImageIcon(iconsDirectory + "clusterIcon.png");
      
      resource = resourceBundle.getResource("Profiler_MenuBar.menu.FieldClusterAnalysis");
      if (resource.equals(""))
         buttonItem = buttonItem("Field Cluster Analysis", dataClusterIcon,
            Profiler_MenuBar.ACTION_FIELD_CLUSTER_ANALYSIS);
      else
         buttonItem = buttonItem(resource, dataClusterIcon, Profiler_MenuBar.ACTION_FIELD_CLUSTER_ANALYSIS);
      add(buttonItem);
   }

   //==============================================================
   // Instance method used for the helping in the creation of tool
   // bar button items.
   //==============================================================

   private JButton buttonItem(String toolTip, ImageIcon icon, String actionLabel)
   {
      JButton item = new JButton(icon);
      item.setFocusable(false);
      item.setMargin(new Insets(0, 0, 0, 0));
      item.setToolTipText(toolTip);
      item.setActionCommand(actionLabel);
      item.addActionListener(menuListener);

      return item;
   }
}