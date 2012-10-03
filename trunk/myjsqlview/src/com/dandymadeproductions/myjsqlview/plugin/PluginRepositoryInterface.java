//=================================================================
//            MyJSQLView PluginRepositoryInterface
//=================================================================
//    This class defines the methods that are required by all
// Plugin Repositories in order to properly function within the 
// MyJSQLView as a repository definition for plugins.
//
//              << PluginRepositoryInterface.java >>
//
//=================================================================
// Copyright (C) 2005-2012 Dana M. Proctor
// Version 1.4 10/03/2012
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
// Version 1.0 Initial PluginRepositoryInterface Class.
//         1.1 Changed Package Name to com.dandymadeproductions.myjsqlview.plugin.
//         1.2 Changed Interface addPluginItem(String) to MyJSQLView_PluginModule
//             Argument. Same With getPluginItems().
//         1.3 Class Methods addPluginItem() & getPluginItems() Change to Plugin.
//         1.4 Minor Code Formatting.
//
//-----------------------------------------------------------------
//                danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview.plugin;

import java.util.ArrayList;

/**
 *    The PluginRepositoryInterface class defines the methods that are
 * required by all Plugin Repositories in order to properly function
 * within the  MyJSQLView as a repository definition for plugins.   
 * 
 * @author Dana M. Proctor
 * @version 1.4 10/03/2012
 */

public interface PluginRepositoryInterface
{  
   //==============================================================
   // Class method to set the repository name.
   //==============================================================
   
   void setName(String repositoryName);
   
   //==============================================================
   // Class method to setup up the repository.
   // The argument should be a reference to a network location.
   // exp. http://dandymadeproductions.com/
   //==============================================================

   void setRepository(String repository);
   
   //==============================================================
   // Class method to add an plugin item to the repository.
   //==============================================================

   void addPluginItem(Plugin pluginItem);
   
   //==============================================================
   // Class method to allow the collection of a name that will be
   // associated with the repository.
   //==============================================================

   String getName();
   
   //==============================================================
   // Class method to allow the collection of a path that will be
   // associated with the repository.
   //==============================================================
   
   String getPath();
   
   //==============================================================
   // Class method to allow the return of some predifined type,
   // example URL, http, ftp, other?
   //==============================================================
   
   String getRepositoryType();
   
   
   //==============================================================
   // Class method to allow the collection of the list of plugins
   // that are associated with the repository.
   //==============================================================

   ArrayList<Plugin> getPluginItems();
}