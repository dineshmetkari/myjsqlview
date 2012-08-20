//=================================================================
//               MyJSQLView PluginModuleInterface
//=================================================================
//    This class defines the methods that are required by all
// Plugin Modules in order to properly function within the 
// MyJSQLView application as a plugin.
//
//              << PluginModuleInterface.java >>
//
//=================================================================
// Copyright (C) 2005-2012 Dana M. Proctor
// Version 1.8 08/20/2012
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
// Version 1.0 Initial PluginModuleInterface Class.
//         1.1 Comment Changes to Description of Class.
//         1.2 Parameterized Argument tables In Interface Method setDBTables()
//             to Bring Code Into Compliance With Java 5.0 API. Minor Comment
//             Changes.
//         1.3 Added Interface getVersion().
//         1.4 Added Argument String path to Interface Method initPlugin().
//         1.5 Copyright Update.
//         1.6 Copyright Update.
//         1.7 Interface setDBTables() Changed to Have Argument tables to
//             be a ArrayList Instead of Vector.
//         1.8 Added Interface getPath_FileName() & getDescription().
//
//-----------------------------------------------------------------
//                danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview;

import javax.swing.JPanel;
import javax.swing.JMenuBar;
import javax.swing.JToolBar;
import javax.swing.ImageIcon;
import java.util.ArrayList;

/**
 *    The MyJSQLView_PluginModule class defines the methods that
 * are required by all Plugin Modules in order to properly function within
 * the MyJSQLView application as a plugin.
 * 
 * @author Dana M. Proctor
 * @version 1.8 08/20/2012
 */

public interface PluginModuleInterface
{  
   //==============================================================
   // Class method to setup up the plugin.
   //==============================================================

   void initPlugin(MyJSQLView_Frame mainFrame, String path);
   
   //==============================================================
   // Class method to allow the collection of the path and file
   // name that was used to for the plugin.
   //==============================================================
   
   String getPath_FileName();
   
   //==============================================================
   // Class method to allow the collection of a name that will be
   // used as the tooltip in the MyJSQLViews tab structure.
   //==============================================================

   String getName();
   
   //==============================================================
   // Class method to allow the collection of a image icon that
   // will be used as an identifier in the MyJSQLView tab structure.
   // NOTE: The tab icon should be no larger than 12 x 12.
   //==============================================================

   ImageIcon getTabIcon();
   
   //==============================================================
   // Class method to allow the collection of a JMenuBar to be
   // used with the plugin module.
   //==============================================================

   JMenuBar getMenuBar();
   
   //==============================================================
   // Class method to allow the collection of a JToolBar to be
   // used with the plugin module.
   //==============================================================

   JToolBar getToolBar();
   
   //==============================================================
   // Class method to return the panel associated with the module.
   // ALL MyJSQLView_PluginModules MUST HAVE A JPANEL TO BE USED
   // BY THE MyJSQLView APPLICATION TO POPULATE THE MAIN TAB TO
   // BE ACCESSABLE!
   //==============================================================
   
   JPanel getPanel();
   
   //==============================================================
   // Class method to return the version release number of the
   // plugin module.
   //==============================================================
   
   String getVersion();
   
   //==============================================================
   // Class method to return the description of the plugin module.
   //==============================================================
   
   String getDescription();
   
   //==============================================================
   // Class method to allow the setting the database tables.
   //==============================================================

   void setDBTables(ArrayList<String> tables);
}