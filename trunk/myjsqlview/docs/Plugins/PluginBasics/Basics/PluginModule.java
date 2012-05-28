//=================================================================
//                  MyPlugin PluginModule Class
//=================================================================
//
//    This class provides the hook to incorporate a external plugin
// module into the MyJSQLView application.
//
//                  << PluginModule.java >>
//
//=================================================================
// Copyright (C) 2006-2012 Dana M. Proctor
// Version 1.5 05/28/2012
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
//This class provides a generic panel in the appearance of
// a form for selecting the CSV data export options.
//=================================================================
// Revision History
// Changes to the code should be documented here and reflected
// in the present version number. Author information should
// also be included with the original copyright author.
//=================================================================
// Version 1.0 Original MyPlugin MyJSQLView_PluginModule.
//         1.1 Addition of Argument String path to initPlugin() Method.
//         1.2 Update to Sync With MyJSQLView v3.31 Release.
//         1.3 Update to Sync With MyJSQLView v3.33 Release.
//         1.4 Update to Sync With MyJSQLView v3.34 Release.
//         1.5 Update to Sync With MyJSQLView v3.35 Release.
//
//-----------------------------------------------------------------
//                 danap@dandymadeproductions.com
//=================================================================

package myplugin;

import javax.swing.JPanel;
import javax.swing.JMenuBar;
import javax.swing.JToolBar;
import javax.swing.ImageIcon;
import java.util.ArrayList;

import com.dandymadeproductions.myjsqlview.MyJSQLView_Frame;
import com.dandymadeproductions.myjsqlview.MyJSQLView_PluginModule;

/**
 *    The PluginModule class provides the hook to incorporate a external
 * plugin module into the MyJSQLView application.
 * 
 * @author Dana M. Proctor
 * @version 1.5 05/28/2012
 */

public class PluginModule extends MyJSQLView_PluginModule
{
   // Class Instances
   private JPanel myPluginPanel;

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
      myPluginPanel = new JPanel();
   }

   //==============================================================
   // Class method to meet the interface requirements for returning
   // the name of the module.
   //==============================================================

   public String getName()
   {
      return "My Plugin";
   }

   //==============================================================
   // Class method to allow the collection of a image icon that
   // will be used as an identifier in the MyJSQLView tab structure.
   // NOTE: The tab icon should be no larger than 12 x 12.
   //==============================================================

   public ImageIcon getTabIcon()
   {
      return null;
   }

   //==============================================================
   // Class method to meet the interface requirements of returning
   // a JMenuBar that can be used to control various aspects of
   // the modules functionality.
   //==============================================================

   public JMenuBar getMenuBar()
   {
      return null;
   }

   //==============================================================
   // Class method to allow the collection of a JToolBar to be
   // used with the plugin module.
   //==============================================================

   public JToolBar getToolBar()
   {
      return null;
   }

   //==============================================================
   // Class method to meet the interface requirements for returning
   // a JPanel for inclusion in the MyJSQLView application's main
   // tab.
   //==============================================================

   public JPanel getPanel()
   {
      return myPluginPanel;
   }

   //==============================================================
   // Class method to meet the interface requirements for being
   // able to set the database tables.
   //==============================================================

   public void setDBTables(ArrayList<String> tableNames)
   {
      myPluginPanel.repaint();
   }
}