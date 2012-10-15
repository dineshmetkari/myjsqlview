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
// Version 1.8 10/15/2012
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
//         1.3 Changed Class Instance pluginList Parameter from MyJSQLView_PluginModule
//             to Plugin Likewise for Method addPluginItem() & getPluginItems().
//         1.4 Added static final Class Instance REPOSITORY_CACHED_FILE.
//         1.5 Added Interface Requirements setRepositoryType() & clearPluginItems().
//         1.6 Provided Additional Comments.
//         1.7 Implement PluginRepositoryInterface Interface setPath(). Added Class
//             Instances cachedRepositoryDirectoryString, debugMode, isRepositoryCached,
//             & REPOSITORY_PATH_FILE.
//         1.8 Class Instance REPOSITORY_CACHED_FILE Changed to Compressed File Name.
//        
//-----------------------------------------------------------------
//                 danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview.plugin;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import com.dandymadeproductions.myjsqlview.MyJSQLView;
import com.dandymadeproductions.myjsqlview.io.WriteDataFile;
import com.dandymadeproductions.myjsqlview.utilities.MyJSQLView_Utils;

/**
 *    The PluginRepository class provides the general framework and link
 * to the PluginRepository Interface inheritance for all PluginReposities
 * in MyJSQLView. The class defines the aspects that are needed in order
 * to properly derive a file/network repository.   
 * 
 * @author Dana M. Proctor
 * @version 1.8 10/15/2012
 */

public abstract class PluginRepository implements PluginRepositoryInterface
{
   // Class Instances
   private String repositoryName;
   private String repositoryPath;
   private String repositoryType;
   private ArrayList<Plugin> pluginsList;
   
   protected String cachedRepositoryDirectoryString;
   protected boolean debugMode, isRepositoryCached;
   
   public static final String FILE = "file";
   public static final String HTTP = "http";
   public static final String FTP = "ftp";
   public static final String UNKNOWN = "unknown";
   public static final String REPOSITORY_PATH_FILE = ".path";
   public static final String REPOSITORY_CACHED_FILE = "repository-cache.xml.gz";
   
   //===========================================================
   // PluginRepository Constructor
   //
   // **NOTE**
   //
   // A repository can not be just created from the constructor
   // alone. The repository needs to use the methods setName()
   // and setRepository() to properly set the repository for use.
   // The method setName() can be called from here, but each
   // unique repository type that extends this class should
   // implement their own setRepository() method.
   //===========================================================
   
   public PluginRepository()
   {
      repositoryName = "";
      repositoryPath = "";
      repositoryType = UNKNOWN;
      
      debugMode = MyJSQLView.getDebug();
      isRepositoryCached = false;
      
      pluginsList = new ArrayList <Plugin>();
   }
   
   //==============================================================
   // Class method to set the repository name.
   //==============================================================
   
   public void setName(String name)
   {
      repositoryName = name; 
   }
   
   //==============================================================
   // Class method to set the repository path.
   //==============================================================
   
   public void setPath(String path)
   {
      // Method Instances
      String cachedRepositoryPathFile;
      
      repositoryPath = path;
      
      if (!path.isEmpty() && isRepositoryCached)
      {
         cachedRepositoryPathFile = cachedRepositoryDirectoryString
                                    + MyJSQLView_Utils.getFileSeparator()
                                    + PluginRepository.REPOSITORY_PATH_FILE;
         WriteDataFile.mainWriteDataString(cachedRepositoryPathFile, path.getBytes(), false);
      }
   }
   
   //==============================================================
   // Class method to set the repository type.
   //==============================================================
   
   public void setType(String type)
   {
      repositoryType = type; 
   }
  
   //==============================================================
   // Class method to add an plugin item to the repository.
   //==============================================================

   public void addPluginItem(Plugin pluginItem)
   {
      pluginsList.add(pluginItem);
   }
   
   //==============================================================
   // Class method to clear the plugin items in the repository
   // list.
   //==============================================================

   public void clearPluginItems()
   {
      pluginsList.clear();
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

   public ArrayList<Plugin> getPluginItems()
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