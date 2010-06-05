//=================================================================
//         MyJSQLView MyJSQLView_PluginModule Class
//=================================================================
//
//    This class provides the abstract framework for plugin classes
// to extends in order to properly function within the MyJSQLView
// application.
//
//             << MyJSQLView_PluginModule.java >>
//
//=================================================================
// Copyright (C) 2005-2010 Dana M. Proctor
// Version 1.7 05/08/2010
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
// Version 1.0 Initial MyJSQLView_PluginModule Class.
//         1.1 Added Interface Methods run() & setDBTables().
//         1.2 Added Interface Methods getModuleName() & getMenuBar().
//         1.3 Added Interface Method getModuleTabIcon().
//         1.4 Added Interface Method getToolBar(). Changed Methods getModuleName()
//             to getName(), getModuleTabIcon() to getTabIcon(), and getModulePanel()
//             to getPanel().
//         1.5 Added Interface Method setParentFrame().
//         1.6 Added Interface Method initPlugin().
//         1.7 Changed to an Abstract Class That Implements PluginModuleInterface.
//             Removed Class Method setParentFrame() and Commented initPlugin()
//             With New Argument MyJSQLView_Frame So That Plugins Must Implement.
//             
//-----------------------------------------------------------------
//                 danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview;

import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JToolBar;

/**
 *    The MyJSQLView_PluginModule class provides the abstract framework
 * for plugin classes to extends in order to properly function within
 * the MyJSQLView application.
 * 
 * @author Dana M. Proctor
 * @version 1.7 05/08/2010
 */

public abstract class MyJSQLView_PluginModule implements PluginModuleInterface
{
   // Class Instances.
   //protected MyJSQLView_Frame parent;

   //===========================================================
   // MyJSQLView_PluginModule Constructor
   //===========================================================

   public MyJSQLView_PluginModule()
   {
      // Nothing to do here.
   }
   
   //==============================================================
   // Class method to setup up your plugin.
   // OVERIDE THIS METHOD!
   //==============================================================

   /*
   public void initPlugin(MyJSQLView_Frame mainFrame)
   {
      // This is where the plugin should be initialized.
      parent = mainFrame;
   }
   */
   
   //==============================================================
   // Class method to obtain the plugin's name.
   // Interface requirement.
   //==============================================================

   public String getName()
   {
      return null;
   }

   //==============================================================
   // Class method to allow the collection of a image icon that
   // will be used as an identifier in the MyJSQLView tab structure.
   // NOTE: The tab icon should be no larger than 12 x 12.
   // Interface requirement.
   //==============================================================

   public ImageIcon getTabIcon()
   {
      return null;
   }

   //==============================================================
   // Class method to obtain the plugin's JMenuBar that can be used
   // to control various aspects of the modules functionality.
   // Interface requirement.
   //==============================================================

   public JMenuBar getMenuBar()
   {
      return null;
   }

   //==============================================================
   // Class method to allow the collection of a JToolBar to be
   // used with the plugin module.
   // Interface requirement.
   //==============================================================

   public JToolBar getToolBar()
   {
      return null;
   }

   //==============================================================
   // Class method for returning a JPanel for inclusion in the
   // MyJSQLView application's main tab.
   // Interface requirement.
   //==============================================================

   public JPanel getPanel()
   {
      return null;
   }

   //==============================================================
   // Class method for being able to set the database tables, occurs
   // if the database is reloaded.
   // Interface requirement.
   //==============================================================

   public void setDBTables(Vector tableNames)
   {
      // Do what you will if you need database table names.
   }
}