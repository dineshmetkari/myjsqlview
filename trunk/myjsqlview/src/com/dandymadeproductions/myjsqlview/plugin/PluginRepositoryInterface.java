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
// Copyright (C) 2005-2015 Dana M. Proctor
// Version 1.7 10/13/2012
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
//         1.3 Interfaces addPluginItem() & getPluginItems() Change to Plugin.
//         1.4 Minor Code Formatting & Return Type for setRepository().
//         1.5 Added Interfaces setRepositoryType() & clearPluginItems().
//         1.6 Added Interface refresh().
//         1.7 Added Interface setPath().
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
 * @version 1.7 10/13/2012
 */

public interface PluginRepositoryInterface
{  
   //==============================================================
   // Interface method to set the repository name.
   //==============================================================
   
   void setName(String repositoryName);
   
   //==============================================================
   // Interface method to set the repository path.
   //==============================================================
   
   void setPath(String repositoryPath);
   
   //==============================================================
   // Interface method to set the repository type.
   //==============================================================
   
   void setType(String repositoryType);
   
   //==============================================================
   // Interface method to setup up the repository.
   // The argument should be a reference to a network location.
   // exp. http://dandymadeproductions.com/ ???
   //==============================================================

   boolean setRepository(String repository);
   
   //==============================================================
   // Interface method to add an plugin item to the repository.
   //==============================================================

   void addPluginItem(Plugin pluginItem);
   
   //==============================================================
   // Interface method to clear the plugin items in the repository
   // list.
   //==============================================================

   void clearPluginItems();
   
   //==============================================================
   // Class method to allow the refreshing the collection of plugin
   // item in the repository list.
   //==============================================================
   
   void refresh();
   
   //==============================================================
   // Interface method to allow the collection of a name that will
   // be associated with the repository.
   //==============================================================

   String getName();
   
   //==============================================================
   // Interface method to allow the collection of a path that will
   // be associated with the repository.
   //==============================================================
   
   String getPath();
   
   //==============================================================
   // Interface method to allow the return of some predifined type,
   // example URL, http, ftp, other?
   //==============================================================
   
   String getRepositoryType();
   
   //==============================================================
   // Interface method to allow the collection of the list of
   // plugins that are associated with the repository.
   //==============================================================

   ArrayList<Plugin> getPluginItems();
}
