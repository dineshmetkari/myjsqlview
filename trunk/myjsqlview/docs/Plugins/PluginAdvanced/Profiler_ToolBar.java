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
// Copyright (C) 2005-2010 Dana M. Proctor.
// Version 1.6 09/12/2010
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
 * @version 1.6 09/12/2010
 */

class Profiler_ToolBar extends JToolBar
{
   // Instance & Class Fields.
   private static final long serialVersionUID = 4305177851277903429L;

   private MyJSQLView_Frame mainFrame;
   private MenuActionListener menuListener;

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
      ImageIcon openIcon, saveIcon, exitIcon;
      ImageIcon dataInformationIcon, dataAnalysisIcon;
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
      
      // File Open
      openIcon = new ImageIcon(iconsDirectory + "openIcon.png");
      
      resource = resourceBundle.getResource("Profiler_MenuBar.menu.Open");
      if (resource.equals(""))
         buttonItem = buttonItem("Open", openIcon, "FO");
      else
         buttonItem = buttonItem(resource, openIcon, "FO");
      add(buttonItem);
      
      // File Save
      saveIcon = new ImageIcon(iconsDirectory + "saveIcon.png");
      
      resource = resourceBundle.getResource("Profiler_MenuBar.menu.Save");
      if (resource.equals(""))
         buttonItem = buttonItem("Save", saveIcon, "FS");
      else
         buttonItem = buttonItem(resource, saveIcon, "FS");
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

      // Information
      dataInformationIcon = new ImageIcon(iconsDirectory + "informationIcon.png");
      
      resource = resourceBundle.getResource("Profiler_MenuBar.menu.FieldInformation");
      if (resource.equals(""))
         buttonItem = buttonItem("Field Information", dataInformationIcon, "TFI");
      else
         buttonItem = buttonItem(resource, dataInformationIcon, "TFI");
      add(buttonItem);

      // Analysis
      dataAnalysisIcon = new ImageIcon(iconsDirectory + "analysisIcon.png");
      
      resource = resourceBundle.getResource("Profiler_MenuBar.menu.FieldAnalysis");
      if (resource.equals(""))
         buttonItem = buttonItem("Field Analysis", dataAnalysisIcon, "TFA");
      else
         buttonItem = buttonItem(resource, dataAnalysisIcon, "TFA");
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