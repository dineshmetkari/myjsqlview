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
// Copyright (C) 2006-2011 Dana M. Proctor
// Version 1.6 02/03/2011
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
//
//-----------------------------------------------------------------
//                 danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.tablerecordcount;

import javax.swing.JPanel;
import java.util.Vector;

import com.dandymadeproductions.myjsqlview.MyJSQLView_Frame;
import com.dandymadeproductions.myjsqlview.ConnectionManager;
import com.dandymadeproductions.myjsqlview.MyJSQLView_PluginModule;

/**
 *    The PluginModule class provides the hook to incorporate a external plugin
 * module into the MyJSQLView application.
 * 
 * @author Dana M. Proctor
 * @version 1.5 09/06/2010
 */

public class PluginModule extends MyJSQLView_PluginModule
{
   // Class Instances
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
      tableRecordCountPanel = new TableRecordCountPanel(ConnectionManager.getTableNames());
   }

   //==============================================================
   // Class method to meet the interface requirements for returning
   // the name of the module.
   //==============================================================

   public String getName()
   {
      return "Table Record Count";
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

   public void setDBTables(Vector<String> tableNames)
   {
      tableRecordCountPanel.reloadPanel(tableNames);
      tableRecordCountPanel.repaint();
   }
}