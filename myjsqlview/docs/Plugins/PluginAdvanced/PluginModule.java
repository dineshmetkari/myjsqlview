//=================================================================
//               TableFieldProfiler PluginModule 
//=================================================================
//
//    This class provides the hook to incorporate a external plugin
// module into the MyJSQLView application.
//
//                   << PluginModule.java >>
//
//=================================================================
// Copyright (C) 2005-2011 Dana M. Proctor
// Version 1.7 01/28/2011
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
// Version 1.0 Initial Dandy Data Profiler MyJSQLView_PluginModule Class.
//         1.1 Changed Name to TableFieldProfiler_PluginModule. Modified
//             the Way the Class Thread pluginThread is Handled.
//         1.2 Changed Name to PluginModule. Removed Class Method setParentFrame()
//             and Added Argument MyJSQLView_Frame to initPlugin().
//         1.3 Modified Class Method getTabIcon() to Return New Instacne tabIcon
//             That is Created in the initPlugin() Method.
//         1.4 Moved Over to TableFieldProfiler Code.
//         1.5 Implemented Interface Method getVersion().
//         1.6 Added Required Interface Argument String path to initPlugin().
//             Modified in Same tabIcon Using path and Passed to TableFieldProfiler
//             Class Constructor.
//         1.7 Update to Reflect Change In MyJSQLView Connection Now All Derived
//             From ConnectionManager Instead of MyJSQLView_Access.
//
//-----------------------------------------------------------------
//                 danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.tablefieldprofiler;

import javax.swing.JPanel;
import javax.swing.ImageIcon;
import javax.swing.JMenuBar;
import javax.swing.JToolBar;
import java.util.Vector;

import com.dandymadeproductions.myjsqlview.MyJSQLView_Frame;
import com.dandymadeproductions.myjsqlview.ConnectionManager;
import com.dandymadeproductions.myjsqlview.MyJSQLView_PluginModule;
import com.dandymadeproductions.myjsqlview.MyJSQLView_Utils;

/**
 *    The PluginModule class provides the hook to incorporate a external
 * plugin module into the MyJSQLView application.
 * 
 * @author Dana M. Proctor
 * @version 1.7 01/28/2011
 */

public class PluginModule extends MyJSQLView_PluginModule
{
   // Class Instances
   // private MyJSQLView_Frame parent;
   private TableFieldProfiler dataProfiler;
   private ImageIcon tabIcon;

   //==============================================================
   // MyJSQLView_PluginModule Constructor.
   //==============================================================

   public PluginModule()
   {
      super();
   }

   //==============================================================
   // Class method to start the classes thread.
   //==============================================================

   public void initPlugin(MyJSQLView_Frame mainFrame, String path)
   {
      // Instance Methods
      String fileSeparator, iconsDirectory;

      // Main Class
      dataProfiler = new TableFieldProfiler(mainFrame, path, ConnectionManager.getTableNames());

      // Plugin Tab Icon.
      fileSeparator = MyJSQLView_Utils.getFileSeparator();
      iconsDirectory = path + fileSeparator + "TableFieldProfiler" + fileSeparator + "images"
                       + fileSeparator + "icons" + fileSeparator;
      tabIcon = new ImageIcon(iconsDirectory + "informationIcon.png");
   }

   //==============================================================
   // Class method to meet the interface requirements for returning
   // the name of the module.
   //==============================================================

   public String getName()
   {
      return "Table Field Profiler";
   }
   
   //==============================================================
   // Class method to obtain the plugin's version number.
   // Interface requirement.
   //==============================================================

   public String getVersion()
   {
      return TableFieldProfiler.getVersion();
   }

   //==============================================================
   // Class method to meet the interface requirements of returning
   // a ImageIcon that will be used as the plugin's tab Icon.
   //==============================================================

   public ImageIcon getTabIcon()
   {
      return tabIcon;
   }

   //==============================================================
   // Class method to meet the interface requirements of returning
   // a JMenuBar that can be used to control various aspects of
   // the modules functionality.
   //==============================================================

   public JMenuBar getMenuBar()
   {
      return dataProfiler.getMenuBar();
   }

   //==============================================================
   // Class method to allow the collection of a JToolBar to be
   // used with the plugin module.
   //==============================================================

   public JToolBar getToolBar()
   {
      return dataProfiler.getToolBar();
   }

   //==============================================================
   // Class method to meet the interface requirements for returning
   // a JPanel for inclusion in the MyJSQLView application's main
   // tab.
   //==============================================================

   public JPanel getPanel()
   {
      return dataProfiler.getPanel();
   }

   //==============================================================
   // Class method to meet the interface requirements for being
   // able to set the database tables.
   //==============================================================

   public void setDBTables(Vector tableNames)
   {
      dataProfiler.setDBTables(tableNames);
   }
}