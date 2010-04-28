//=================================================================
//                    MyJSQLView_PluginModule
//=================================================================
//    This class defines the methods that are required by all
// Plugin Modules in order to properly function within the 
// MyJSQLView application as a plugin.
//
//                << MyJSQLView_PluginModule.java >>
//
//=================================================================
// Copyright (C) 2005-2010 Dana M. Proctor
// Version 1.3 04/28/2010
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
//
//-----------------------------------------------------------------
//                danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview;

import javax.swing.JPanel;
import javax.swing.JMenuBar;
import javax.swing.ImageIcon;
import java.util.Vector;

/**
 *    The MyJSQLView_PluginModule class This class defines the methods that
 * are required by all Plugin Modules in order to properly function within
 * the MyJSQLView application as a plugin.
 * 
 * @author Dana M. Proctor
 * @version 1.3 04/28/2010
 */

//=================================================================
// Class method to return the required panel that will be added
// to the MyJSQLView's mainTabsPane in the MyJSQLView_Frame class.
//=================================================================

public interface MyJSQLView_PluginModule
{
   //==============================================================
   // Class method to collect the column names from the table.
   //==============================================================

   void run();
   
   //==============================================================
   // Class method to allow the collection of a name that will be
   // used as the tooltip in the MyJSQLViews tab structure.
   //==============================================================

   String getModuleName();
   
   //==============================================================
   // Class method to allow the collection of a image icon that
   // will be used as an identifier in the MyJSQLView tab structure.
   // NOTE: The tab icon should be no larger than 12 x 12.
   //==============================================================

   ImageIcon getModuleTabIcon();
   
   //==============================================================
   // Class method to allow the collection of a JMenuBar to be
   // used with the plugin module.
   //==============================================================

   JMenuBar getMenuBar();
   
   //==============================================================
   // Class method to return the panel associated with the module.
   // ALL MyJSQLView_PluginModules MUST HAVE A JPANEL TO BE USED
   // BY THE MyJSQLView APPLICATION TO POPULATE THE MAIN TAB TO
   // BE ACCESSABLE!
   //==============================================================
   
   JPanel getPluginPanel();
   
   //==============================================================
   // Class method to allow the setting the database tables.
   //==============================================================

   void setDBTables(Vector tables);
}