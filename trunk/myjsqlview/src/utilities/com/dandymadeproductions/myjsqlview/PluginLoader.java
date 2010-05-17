//=================================================================
//                    PluginLoader Class
//=================================================================
// 
//    This class is used to cycle through the jar/zip files located
// in the plugin directory under MyJSQLView's installation directory
// lib to find Plugin Modules. Only classes that match the interface
// PluginModule will be loaded.
//
//                     << PluginLoader.java >>
//
//=================================================================
// Copyright (C) 2005-2010 Dana M. Proctor
// Version 1.3 05/16/2010
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
// Version 1.0 04/22/2010 Initial PluginLoader Class.
//         1.1 04/28/2010 Commented System.out in Class Method loadPluginModule()
//                        and Organized imports.
//         1.2 05/07/2010 Changed validPluginModuleName to PluginModule.class.
//         1.3 05/16/2010 Parameterized Class Instance pluginModulesHashMap to
//                        Bring Code Into Compliance With Java 5.0 API.
//
//-----------------------------------------------------------------
//                 danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview;

import java.io.File;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 *    The PluginLoader class is used to cycle through the jar/zip files
 * located in the plugin directory under MyJSQLView's installation directory
 * lib to find Plugin Modules. Only classes that match the interface
 * PluginModule will be loaded.
 * 
 * @author Dana M. Proctor
 * @version 1.3 05/16/2010
 */

class PluginLoader
{
   // Class Instances.
   private String fileSeparator;
   private String pluginDirectoryString;
   private static final String validPluginModuleName = "PluginModule.class";
   private HashMap<String, String> pluginModulesHashMap;
   
   //==============================================================
   // PluginLoader Constructor
   //==============================================================

   protected PluginLoader()
   {
      // Setup various needing instances.
      fileSeparator = MyJSQLView_Utils.getFileSeparator();
      pluginDirectoryString = System.getProperty("user.dir") + fileSeparator + "lib" + fileSeparator
                              + "plugins" + fileSeparator;
      
      pluginModulesHashMap = new HashMap <String, String>();
      
      // Obtain the Plugin Modules.
      loadPluginModules();
   }
   
   //==============================================================
   // Class Method for reviewing the JAR files in the lib/plugin/
   // directory in search of a MyJSQLView_PluginModule to be then
   // loaded.
   //==============================================================

   private void loadPluginModules()
   {
      // Method Instances
      File filePluginsDirectory;
      String[] jarFileNames; 
      String pathKey, currentFileName;
      ZipFile jarFile;
      
      // Create the file for the plugin directory & load
      // directory contents.
      
      filePluginsDirectory = new File(pluginDirectoryString);
      jarFileNames = filePluginsDirectory.list();
      
      // Cycle through the files in search of plugins.
      for (int i=0; i<jarFileNames.length; i++)
      {
         if (!jarFileNames[i].toLowerCase().endsWith(".jar"))
            continue;
         
         try
         {
            jarFile = new ZipFile(pluginDirectoryString + jarFileNames[i]);
            
            for(Enumeration entries = jarFile.entries(); entries.hasMoreElements();)
            {
               currentFileName = ((ZipEntry)entries.nextElement()).getName();
               
               // Plugin Qualifier
               if (currentFileName.endsWith(".class") && currentFileName.indexOf("$")==-1
                   && currentFileName.indexOf(validPluginModuleName) != -1)
               {
                  pathKey = pluginDirectoryString + jarFileNames[i];
                  currentFileName = currentFileName.replaceAll("/", ".");
                  currentFileName = currentFileName.substring(0, currentFileName.indexOf(".class"));
                  
                  pluginModulesHashMap.put(pathKey, currentFileName);
                  // System.out.println("Located:" + pathKey + " " + currentFileName);
               }
            }
            jarFile.close();
         }
         catch(Exception e)
         {
            String errorString = "PluginLoader loadPluginModule() Exception: " + jarFileNames[i]
                                 + "\n" + e.toString();
            displayErrors(errorString);
         }
      }   
   }
   
   //==============================================================
   // Class method to display an error to the standard output if
   // the debug option is active.
   //==============================================================

   protected void displayErrors(String errorString)
   {
      if (MyJSQLView.getDebug())
      {
         System.out.println(errorString);
      }
   }
   
   //==============================================================
   // Class method to allow outside classes to get the plugins
   // that were found.
   //==============================================================

   protected HashMap<String, String> getPluginsHashMap()
   {
      return pluginModulesHashMap;
   }
}