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
// Copyright (C) 2005-2012 Dana M. Proctor
// Version 2.4 03/21/2012
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
//         1.4 05/20/2010 Parameterized entries in Class Method loadPluginModules().
//         1.5 06/06/2010 Rebuilt Class To Handle Complete Loading of Plugin Modules.
//                        Added Class Method loadPluginEntries().
//         1.6 06/07/2010 Changed the Way Plugin Data is Stored for Later Retrieval
//                        By Setting the Plugin's Instances. Changed Occured in
//                        Class Method loadPluginModules(). Formatted.
//         1.7 06/07/2010 Implemented Runnable. Class Methods loadePluginEntries()
//                        & loadPluginModules to run() Method. Removed Vector Argument
//                        From Constructor. Class Method loadPluginModules Stored
//                        Each Loaded Plugin via MyJSQLView_Frame.addTab().
//         1.8 06/09/2010 Offloaded the Adding of a Plugin Module to the MyJSQlView's
//                        Main Frame to the New PluginThread Class in Method
//                        loadPluginModule().
//         1.9 08/18/2010 Modified to Handle Manually Loading Plugins Through Two
//                        Argument Constructor and Method init(). Additional Methods
//                        Also Added to Properly Process, loadPluginEntry(),
//                        loadConfigurationFilePluginEntries(). Changed Class Method
//                        loadPluginEntries() to loadDefaultPluginEntries().
//         2.0 09/06/2010 Changed the Delimiter Between pathKey & className to '<$$$>'.
//                        Methods Effected loadPluginEntry(), loadConfigurationFilePluginEntries(),
//                        & loadPluginModules().
//         2.1 01/27/2011 Copyright Update.
//         2.2 10/05/2011 Updated errorString Information in run for URL ClassLoader
//                        and Class Loading Exceptions.
//         2.3 01/01/2012 Copyright Update.
//         2.4 03/21/2012 Change in loadPluginEntry() to Throw IOException Through Finally
//                        to Close fileWriter. Calling Method run() try and catch.
//                        
//-----------------------------------------------------------------
//                 danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.swing.ImageIcon;

/**
 *    The PluginLoader class is used to cycle through the jar/zip files
 * located in the plugin directory under MyJSQLView's installation directory
 * lib to find Plugin Modules. Only classes that match the interface
 * PluginModule will be loaded.
 * 
 * @author Dana M. Proctor
 * @version 2.4 03/21/2012
 */

class PluginLoader implements Runnable
{
   // Class Instances.
   Thread pluginLoaderThread;
   private MyJSQLView_Frame parentFrame;
   private String fileSeparator;
   private String pluginDirectoryString;
   private String pluginFileName;
   private String pluginConfigFileString;
   private static final String configurationFileName = "myjsqlview_plugin.conf";
   private static final String validPluginModuleName = "PluginModule.class";
   private HashMap<String, String> pluginEntriesHashMap;

   //==============================================================
   // PluginLoader Constructor(s)
   //==============================================================

   protected PluginLoader(MyJSQLView_Frame parent)
   {
      init(parent, "");
   }
   
   protected PluginLoader(MyJSQLView_Frame parent, String pluginFileName)
   {
      init(parent, pluginFileName);
   }
   
   //==============================================================
   // Class method for initialization and starting the class thread.
   //==============================================================
   
   public void init(MyJSQLView_Frame parent, String pluginFileName)
   {
      parentFrame = parent;
      this.pluginFileName = pluginFileName;
      
      // Setup.
      fileSeparator = MyJSQLView_Utils.getFileSeparator();
      pluginConfigFileString = MyJSQLView_Utils.getMyJSQLViewDirectory()
                               + MyJSQLView_Utils.getFileSeparator() + configurationFileName;
      
      if (pluginFileName.equals(""))
         pluginDirectoryString = System.getProperty("user.dir") + fileSeparator + "lib" + fileSeparator
                                 + "plugins" + fileSeparator;
      else
         pluginDirectoryString = "";
         
      pluginEntriesHashMap = new HashMap<String, String>();
      
      // Create and start the class thread.
      pluginLoaderThread = new Thread(this, "PluginLoader Thread");
      // System.out.println("PluginLoader Thread");
      
      pluginLoaderThread.start();
   }

   //==============================================================
   // Class method for normal start of the thread.
   //==============================================================

   public void run()
   {
      // Obtain the plugin module(s) & install into application.
      
      // Plugin Management Tool Load
      if (!pluginFileName.equals(""))
      {
         try
         {
            loadPluginEntry(pluginFileName);
         }
         catch (IOException ioe){};
      }
      
      // Default lib/plugin and configuration load.
      else
      {
         loadDefaultPluginEntries();
         loadConfigurationFilePluginEntries();
      }
      loadPluginModules();
   }
   
   //==============================================================
   // Class Method for loading a plugin module manually through
   // the MyJSQLView Plugin Management tool in the top tab.
   //==============================================================

   private void loadPluginEntry(String fileName) throws IOException
   {
      // Method Instances
      String pathKey, className, currentFileName, pluginEntry;
      ZipFile jarFile;
      File configurationFile;
      FileWriter fileWriter;
      
      // Check for a a valid jar file to be processed
      // then search for a plugin module.
      
      if (!fileName.toLowerCase().endsWith(".jar"))
         return;
      
      fileWriter = null;
      
      try
      {
         jarFile = new ZipFile(fileName);
         pathKey = "";
         className = "";
         
         for (Enumeration<?> entries = jarFile.entries(); entries.hasMoreElements();)
         {
            currentFileName = ((ZipEntry) entries.nextElement()).getName();

            // Plugin Qualifier
            if (currentFileName.endsWith(".class") && currentFileName.indexOf("$") == -1
                && currentFileName.indexOf(validPluginModuleName) != -1)
            {
               pathKey = fileName;
               currentFileName = currentFileName.replaceAll("/", ".");
               currentFileName = currentFileName.substring(0, currentFileName.indexOf(".class"));
               className = currentFileName;
               
               pluginEntriesHashMap.put(pathKey, className);
            }
         }
         jarFile.close();
         
         // Update the configuration file indicating valid
         // plugin modules that have been loaded manually.
         
         if (!pathKey.equals("") && !className.equals(""))
         {
            pluginEntry = pathKey + "<$$$>" + className + "\n";
            
            // Write new or appending. 
            configurationFile = new File(pluginConfigFileString);
               
            if (!configurationFile.exists())
               WriteDataFile.mainWriteDataString(pluginConfigFileString, pluginEntry.getBytes(), false);
            else
            {
               fileWriter = new FileWriter(pluginConfigFileString, true);
               char[] buffer = new char[pluginEntry.length()];
               pluginEntry.getChars(0, pluginEntry.length(), buffer, 0);
               fileWriter.write(buffer);
            }
         }
      }
      catch (Exception e)
      {
         String errorString = "PluginLoader loadPluginEntry() Exception: " + fileName + "\n"
                              + e.toString();
         displayErrors(errorString);
      }
      finally
      {
         if (fileWriter != null)
            fileWriter.close();
      }
   }

   //==============================================================
   // Class Method for reviewing the JAR files in the lib/plugin/
   // directory in search of a MyJSQLView_PluginModule to be then
   // loaded.
   //==============================================================

   private void loadDefaultPluginEntries()
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
      for (int i = 0; i < jarFileNames.length; i++)
      {
         if (!jarFileNames[i].toLowerCase().endsWith(".jar"))
            continue;

         try
         {
            jarFile = new ZipFile(pluginDirectoryString + jarFileNames[i]);

            for (Enumeration<?> entries = jarFile.entries(); entries.hasMoreElements();)
            {
               currentFileName = ((ZipEntry) entries.nextElement()).getName();

               // Plugin Qualifier
               if (currentFileName.endsWith(".class") && currentFileName.indexOf("$") == -1
                   && currentFileName.indexOf(validPluginModuleName) != -1)
               {
                  pathKey = pluginDirectoryString + jarFileNames[i];
                  
                  currentFileName = currentFileName.replaceAll("/", ".");
                  currentFileName = currentFileName.substring(0, currentFileName.indexOf(".class"));;

                  pluginEntriesHashMap.put(pathKey, currentFileName);
                  // System.out.println("Located:" + pathKey + " " + currentFileName);
               }
            }
            jarFile.close();
         }
         catch (Exception e)
         {
            String errorString = "PluginLoader loadPluginEntries() Exception: " + jarFileNames[i] + "\n"
                                 + e.toString();
            displayErrors(errorString);
         }
      }
   }
   
   //==============================================================
   // Class Method for reviewing the entries in the configuration
   // file myjsqlview_plugin.conf file to be loaded as plugins.
   //==============================================================

   private void loadConfigurationFilePluginEntries()
   {
      // Method Instances
      String currentLine, pathKey, className;
      File configurationFile;
      FileReader fileReader;
      BufferedReader bufferedReader;
      
      try
      {
         // Check to see if file exists.
         configurationFile = new File(pluginConfigFileString);
         try
         { 
            if (!configurationFile.exists())
               return;
         }
         catch (SecurityException e)
         {
            displayErrors("Security Exception. " + e);
            return;
         }
         
         // Looks like there is a plugin configuration file
         // so collect the entries.
         
         fileReader = new FileReader(pluginConfigFileString);
         bufferedReader = new BufferedReader(fileReader);
            
         while ((currentLine = bufferedReader.readLine()) != null)
         {
            currentLine = currentLine.trim();
            
            if (currentLine.indexOf("<$$$>") != -1)
            {
               pathKey = currentLine.substring(0, currentLine.indexOf("<$$$>"));
               className = currentLine.substring(currentLine.indexOf("<$$$>") + 5);
               pluginEntriesHashMap.put(pathKey, className);
            }
            else
               continue;
         }
         bufferedReader.close();
         fileReader.close();
      }
      catch (IOException ioe) 
      {
         displayErrors("File I/O Problem. " + ioe);
      }
   }

   //==============================================================
   // Class Method for loading the actual plugin module class
   // instances
   //==============================================================

   private void loadPluginModules()
   {
      // Method Instances.
      String iconsDirectory;
      ImageIcon defaultModuleIcon;

      // Obtain & create default Image Icons.

      iconsDirectory = MyJSQLView_Utils.getIconsDirectory() + fileSeparator;
      defaultModuleIcon = new ImageIcon(iconsDirectory + "newsiteLeafIcon.png");

      // Iterator through the found plugins and load them.

      Set<Map.Entry<String, String>> keySet = pluginEntriesHashMap.entrySet();
      Iterator<Map.Entry<String, String>> pluginIterator = keySet.iterator();

      while (pluginIterator.hasNext())
      {
         Map.Entry<String, String> pluginEntry = pluginIterator.next();

         final File jarFile = new File(pluginEntry.getKey());
         ClassLoader classLoader = AccessController.doPrivileged(new PrivilegedAction<ClassLoader>()
         {
            public ClassLoader run()
            {
               try
               {
                  return new URLClassLoader(new URL[] {jarFile.toURI().toURL()}, ClassLoader
                        .getSystemClassLoader());
               }
               catch (MalformedURLException me)
               {
                  String errorString = "PluginLoader loadPluginModules() URLClassLoader: \n" + me.toString();
                  displayErrors(errorString);
                  return null;
               }
            }
         });

         // If looks like a good plugin try to load it.

         if (classLoader != null)
         {
            // Create the instance and add to MyJSQLView.
            try
            {
               Class<?> module = Class.forName(pluginEntry.getValue(), true, classLoader);
               MyJSQLView_PluginModule pluginModule = (MyJSQLView_PluginModule) module.newInstance();
               pluginModule.setPath_FileName(pluginEntry.getKey() + "<$$$>" + pluginEntry.getValue());

               new PluginThread(parentFrame, pluginModule, defaultModuleIcon);
            }
            catch (Exception e)
            {
               String errorString = "PluginLoader loadPluginModules() Exception: \n" + e.toString();
               displayErrors(errorString);
            }
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
}