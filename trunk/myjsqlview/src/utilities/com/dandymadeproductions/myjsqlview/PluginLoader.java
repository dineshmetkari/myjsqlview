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
// Version 1.7 06/07/2010
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
//
//-----------------------------------------------------------------
//                 danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview;

import java.awt.Image;
import java.io.File;
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
import javax.swing.JPanel;

/**
 *    The PluginLoader class is used to cycle through the jar/zip
 * files located in the plugin directory under MyJSQLView's installation
 * directory lib to find Plugin Modules. Only classes that match the
 * interface PluginModule will be loaded.
 * 
 * @author Dana M. Proctor
 * @version 1.7 06/07/2010
 */

class PluginLoader implements Runnable
{
   // Class Instances.
   Thread pluginLoaderThread;
   private MyJSQLView_Frame parentFrame;
   private String fileSeparator;
   private String pluginDirectoryString;
   private static final String validPluginModuleName = "PluginModule.class";
   private HashMap<String, String> pluginEntriesHashMap;

   //==============================================================
   // PluginLoader Constructor
   //==============================================================

   protected PluginLoader(MyJSQLView_Frame parent)
   {
      // Setup various needing instances.
      parentFrame = parent;
      fileSeparator = MyJSQLView_Utils.getFileSeparator();
      pluginDirectoryString = System.getProperty("user.dir") + fileSeparator + "lib" + fileSeparator
                              + "plugins" + fileSeparator;

      pluginEntriesHashMap = new HashMap<String, String>();

      // Create and start the class thread.
      pluginLoaderThread = new Thread(this, "PluginLoader Thread");
      // System.out.println("PluginLoader Thread");

      pluginLoaderThread.start();
   }

   //==============================================================
   // Class method for normal start of the thread
   //==============================================================

   public void run()
   {
      // Obtain the Plugin Modules.
      loadPluginEntries();

      if (pluginEntriesHashMap != null && !pluginEntriesHashMap.isEmpty())
         loadPluginModules();
   }

   //==============================================================
   // Class Method for reviewing the JAR files in the lib/plugin/
   // directory in search of a MyJSQLView_PluginModule to be then
   // loaded.
   //==============================================================

   private void loadPluginEntries()
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
                  currentFileName = currentFileName.substring(0, currentFileName.indexOf(".class"));

                  pluginEntriesHashMap.put(pathKey, currentFileName);
                  // System.out.println("Located:" + pathKey + " " +
                  // currentFileName);
               }
            }
            jarFile.close();
         }
         catch (Exception e)
         {
            String errorString = "PluginLoader loadPluginModule() Exception: " + jarFileNames[i] + "\n"
                                 + e.toString();
            displayErrors(errorString);
         }
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

      // Iterator through the found plugins and load.

      Set<Map.Entry<String, String>> keySet = pluginEntriesHashMap.entrySet();
      Iterator<Map.Entry<String, String>> pluginIterator = keySet.iterator();

      while (pluginIterator.hasNext())
      {
         Map.Entry<String, String> pluginEntry = pluginIterator.next();
         try
         {
            final File jarFile = new File((String) pluginEntry.getKey());
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
                     if (MyJSQLView.getDebug())
                        System.out.println("MyJSQLView_Frame createGUI() URLClassLoader: \n" + me.toString());
                     return null;
                  }
               }
            });

            // If looks like a good plugin load it.

            if (classLoader != null)
            {
               // Create the instance.
               Class<?> module = Class.forName(pluginEntry.getValue(), true, classLoader);
               MyJSQLView_PluginModule pluginModule = (MyJSQLView_PluginModule) module.newInstance();
               pluginModule.initPlugin(parentFrame);

               // Check all the main aspects needed by MyJSQLView
               // in the loaded plugin module and isolate the
               // application from deviants

               // Name
               if (pluginModule.getName() == null)
                  pluginModule.name = "";
               else
               {
                  if ((pluginModule.getName()).length() > 50)
                     pluginModule.name = ((pluginModule.getName()).substring(0, 49));
                  else
                     pluginModule.name = pluginModule.getName();
               }

               // Main Panel
               if (pluginModule.getPanel() == null)
                  pluginModule.panel = (new JPanel());
               else
                  pluginModule.panel = pluginModule.getPanel();

               // Tab Icon
               if (pluginModule.getTabIcon() == null)
                  pluginModule.tabIcon = defaultModuleIcon;
               else
                  pluginModule.tabIcon = new ImageIcon((pluginModule.getTabIcon()).getImage()
                        .getScaledInstance(12, 12, Image.SCALE_FAST));

               // MenuBar
               if (pluginModule.getMenuBar() == null)
                  pluginModule.menuBar = new Default_JMenuBar(parentFrame);
               else
                  pluginModule.menuBar = pluginModule.getMenuBar();

               // ToolBar
               if (pluginModule.getToolBar() == null)
                  pluginModule.toolBar = new Default_JToolBar("");
               else
                  pluginModule.toolBar = pluginModule.getToolBar();

               // Store/Add Plugin
               MyJSQLView_Frame.addTab(pluginModule);
            }
         }
         catch (Exception e)
         {
            if (MyJSQLView.getDebug())
               System.out.println("MyJSQLView_Frame createGUI() Exception: \n" + e.toString());
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