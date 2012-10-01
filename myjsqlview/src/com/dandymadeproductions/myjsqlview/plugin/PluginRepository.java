//=================================================================
//                MyJSQLView PluginRepository
//=================================================================
//
//    This class provides the general framework and link to the
// PluginRepository Interface inheritance for all PluginReposities
// in MyJSQLView. The aspects that are needed in order to properly
// define a file/network repository.
//
//                 << PluginRepository.java >>
//
//=================================================================
// Copyright (C) 2005-2012 Dana M. Proctor
// Version 1.2 10/01/2012
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
// Version 1.0 Initial PluginRepository Class.
//         1.1 Changed Package Name to com.dandymadeproductions.myjsqlview.plugin.
//             Made Class & Constructor Public.
//         1.2 Changed Class Instance pluginList Parameter from String to
//             MyJSQLView_PluginModule. Likewise for Method addPluginItem() &
//             getPluginItems().
//        
//-----------------------------------------------------------------
//                 danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview.plugin;

import java.util.ArrayList;

import javax.swing.JOptionPane;

/**
 *    The PluginRepository class provides the general framework and link
 * to the PluginRepository Interface inheritance for all PluginReposities
 * in MyJSQLView. The class defines the aspects that are needed in order
 * to properly derive a file/network repository.   
 * 
 * @author Dana M. Proctor
 * @version 1.2 10/01/2012
 */

public abstract class PluginRepository implements PluginRepositoryInterface
{
   // Class Instances
   private String repositoryName;
   private String repositoryPath;
   private String repositoryType;
   private ArrayList<MyJSQLView_PluginModule> pluginsList;
   
   public static final String FILE = "file";
   public static final String HTTP = "http";
   public static final String FTP = "ftp";
   public static final String UNKNOWN = "unknown";
   
   //===========================================================
   // PluginRepository Constructor
   //===========================================================
   
   public PluginRepository()
   {
      repositoryName = "";
      repositoryPath = "";
      repositoryType = UNKNOWN;
      pluginsList = new ArrayList <MyJSQLView_PluginModule>();
   }
   
   //==============================================================
   // Class method to set the repository name.
   //==============================================================
   
   public void setName(String name)
   {
      repositoryName = name; 
   }
   
   //==============================================================
   // Class method to add an plugin item to the repository.
   //==============================================================

   public void addPluginItem(MyJSQLView_PluginModule pluginItem)
   {
      pluginsList.add(pluginItem);
   }
   
   //==============================================================
   // Class method to allow the collection of a name that will be
   // associated with the repository.
   //==============================================================

   public String getName()
   {
      return repositoryName;
   }
   
   //==============================================================
   // Class method to allow the collection of a path that will be
   // associated with the repository.
   //==============================================================
   
   public String getPath()
   {
      return repositoryPath;
   }
   
   //==============================================================
   // Class method to allow the return of some predifined type,
   // example URL, http, ftp, other?
   //==============================================================
   
   public String getRepositoryType()
   {
      return repositoryType;
   }
   
   //==============================================================
   // Class method to allow the collection of the list of plugins
   // that are associated with the repository.
   //==============================================================

   public ArrayList<MyJSQLView_PluginModule> getPluginItems()
   {
      return pluginsList;
   }
   
   //==============================================================
   // Class method to allow the displaying an error to the user
   // if something goes wrong with the loading of repository.
   //==============================================================

   public void displayErrors(String errorString)
   {
      if (errorString.length() > 200)
         errorString = errorString.substring(0, 200);

      String optionPaneStringErrors = "Error Loading Repository\n" + errorString;

      JOptionPane.showMessageDialog(null, optionPaneStringErrors, "Alert", JOptionPane.ERROR_MESSAGE);
   }
}