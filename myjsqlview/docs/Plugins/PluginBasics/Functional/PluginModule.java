//=================================================================
//            TableRecordCount PluginModule Class
//=================================================================
//
//    This class provides the hook to incorporate a external plugin
// module into the MyJSQLView application.
//
//                  << PluginModule.java >>
//
//=================================================================
// Copyright (C) 2006-2012 Dana M. Proctor
// Version 2.1 09/29/2012
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
// Version 1.0 Original TableRecordCount MyJSQLView_PluginModule.
//         1.1 Formatted and Added Delay in Class Method initPlugin().
//         1.2 Removed Class Methods getTabIcon(), getMenuBar(), & getToolBar().
//             Removed Delay in initPlugin() Class Method. Parameterized Argument
//             tableNames in Class Method setDBTables() to Bring Code Into
//             Compliance With Java 5.0 API.
//         1.3 Added Method getVersion().
//         1.4 Method getVersion() String Returned Through Static Access to
//             TableRecordCountPanel.getVersion().
//         1.5 Added Required Interface Argument String path For Method
//             initPlugin().
//         1.6 Change in MyJSQLView Package of MyJSQLView_Access Change to
//             ConnectionManager.
//         1.7 Changed Argument tableNames in setDBTables() to ArrayList.
//         1.8 Requirement Addition for TableRecordCountPanel Argument of path.
//         1.9 Added Class Method getTabIcon() for Testing With MyJSQLView
//             in the Rebuild of the Plugin Loading Framework.
//         2.0 Added Class Instance pluginName to Meet New MyJSQLView v3.35++
//             Plugin Requirements.
//         2.1 Added Class Instance pluginAuthor to Meet New MyJSQLView v3.35++
//             Plugin Requirements.
//
//-----------------------------------------------------------------
//                 danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.tablerecordcount;

import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import com.dandymadeproductions.myjsqlview.datasource.ConnectionManager;
import com.dandymadeproductions.myjsqlview.gui.MyJSQLView_Frame;
import com.dandymadeproductions.myjsqlview.plugin.MyJSQLView_PluginModule;

/**
 *    The PluginModule class provides the hook to incorporate a external plugin
 * module into the MyJSQLView application.
 * 
 * @author Dana M. Proctor
 * @version 2.1 09/29/2012
 */

public class PluginModule extends MyJSQLView_PluginModule
{
   // Class Instances
   private String pluginName, pluginAuthor;
   private TableRecordCountPanel tableRecordCountPanel;

   //==============================================================
   // PluginModule Constructor.
   //==============================================================

   public PluginModule()
   {
      super();
   }

   //==============================================================
   // Class method to initialize your plugin.
   //==============================================================

   public void initPlugin(MyJSQLView_Frame parentFrame, String path)
   {
      pluginName = "Table Record Count";
      pluginAuthor = "Dandy Made Productions";
      tableRecordCountPanel = new TableRecordCountPanel(path, ConnectionManager.getTableNames());
   }

   //==============================================================
   // Class method to meet the interface requirements for getting
   // the name of the module.
   //==============================================================

   public String getName()
   {
      return pluginName;
   }
   
   //==============================================================
   // Class method to meet the interface requirements for getting
   // the author of the module.
   //==============================================================

   public String getAuthor()
   {
      return pluginAuthor;
   }
   
   //==============================================================
   // Class method to return the version release number of the
   // plugin module.
   //==============================================================
   
   public String getVersion()
   {
      return TableRecordCountPanel.getVersion();
   }
   
   //==============================================================
   // Class method to meet the interface requirements of returning
   // a ImageIcon that will be used as the plugin's tab Icon.
   //==============================================================

   public ImageIcon getTabIcon()
   {
      return tableRecordCountPanel.getTabIcon();
   }

   //==============================================================
   // Class method to meet the interface requirements for returning
   // a JPanel for inclusion in the MyJSQLView application's main
   // tab.
   //==============================================================

   public JPanel getPanel()
   {
      return tableRecordCountPanel;
   }

   //==============================================================
   // Class method to meet the interface requirements for being
   // able to set the database tables.
   //==============================================================

   public void setDBTables(ArrayList<String> tableNames)
   {
      tableRecordCountPanel.reloadPanel(tableNames);
      tableRecordCountPanel.repaint();
   }
}