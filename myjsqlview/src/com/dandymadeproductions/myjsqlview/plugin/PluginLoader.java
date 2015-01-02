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
// Copyright (C) 2005-2015 Dana M. Proctor
// Version 3.7 12/06/2014
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
//         2.5 07/16/2012 Class Instances, fileSeparator to localSystemFileSeparator, 
//                        Capitalized configurationFileName & validPluginModule, Added
//                        FILE_URL & HTTP_URL. Change in loadPluginEntry() to Handle
//                        Network URL, Http. Method loadDefaultPluginEntries() Now Throws
//                        IOException to Insure Closing jarFile. Same in loadConfiguration
//                        FilePluginEntries() Except for bufferedReader & fileReader.
//                        Change in loadPluginModules() to Handle Processing of URLs.
//         2.6 07/18/2012 Changed Class Instance pluginEntriesHashMap from <String, String>
//                        to <URL, String>. Same for Arguments to Constructors & init().
//                        Change in Each of the load Methods for Collecting the Entry(s)
//                        to Check for Java Packages. General Clean up to Finalize for
//                        Upgrade in Loading Plugins from URLs.
//         2.7 07/20/2012 Added Class Instance pluginURL. Removed String Argument from
//                        Method loadPluginEntry(). In Same Method Removed Instance
//                        pluginURL & pathKey, Also closed jarFile in finally.
//         2.8 08/10/2012 Error Output for Empty Constructor in Catch. General Cleanup.
//                        Class Instance pluginEntriesHashMap Change From <URL,String>
//                        to <String,String>.
//         2.9 08/19/2012 Collection of All Image Resources Through resourceBundle.
//         3.0 09/11/2012 Changed Package Name to com.dandymadeproductions.myjsqlview.plugin.
//                        Made Class & Constructor Public.
//         3.1 09/20/2012 Class Method loadPluginModules() Direct Setting of the Modules
//                        Path by Using the Protected Class Instance pathFileName.
//         3.2 09/26/2012 Added Class Instance pathClassSeparator.
//         3.3 10/10/2012 Class Method loadPluginModules() Attempt to Remove an Offending
//                        pluginURLString When an Exception is Caught from the Plugin
//                        Configuration File.
//         3.4 10/18/2012 Class Method loadPluginEntry() Correction in Creation of File
//                        Type Resource to Handle WinOS Network Paths.
//         3.5 10/19/2012 Threaded Calls to PluginFrame.removePluginConfigurationModule()
//                        in loadPluginModules().
//         3.6 10/21/2012 Change in Derivation in loadDefaultPluginEntries() to Proper
//                        Format pathKey for WinOS.
//         3.7 12/06/2014 Class Method loadPluginModules() Internal ClassLoader Additional
//                        Debug Output & Checks for Bad Actors With Exceptions ClassNotFound
//                        & NoClassDefFound.
//                        
//-----------------------------------------------------------------
//                 danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview.plugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.JarURLConnection;
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

import com.dandymadeproductions.myjsqlview.MyJSQLView;
import com.dandymadeproductions.myjsqlview.gui.MyJSQLView_Frame;
import com.dandymadeproductions.myjsqlview.gui.PluginFrame;
import com.dandymadeproductions.myjsqlview.io.WriteDataFile;
import com.dandymadeproductions.myjsqlview.utilities.MyJSQLView_Utils;

/**
 *    The PluginLoader class is used to cycle through the jar/zip files
 * located in the plugin directory under MyJSQLView's installation directory
 * lib to find Plugin Modules. Only classes that match the interface
 * PluginModule will be loaded.
 * 
 * @author Dana M. Proctor
 * @version 3.7 12/06/2014
 */

public class PluginLoader implements Runnable
{
   // Class Instances.
   Thread pluginLoaderThread;
   private MyJSQLView_Frame parentFrame;
   private String localSystemFileSeparator;
   private String pluginDirectoryString;
   private String pluginFileName;
   private URL pluginURL;
   private String pluginConfigFileString;
   private HashMap<String, String> pluginEntriesHashMap;
   
   private static final String CONFIGURATION_FILENAME = "myjsqlview_plugin.conf";
   private static final String VALID_PLUGIN_MODULENAME = "PluginModule.class";
   public static final String pathClassSeparator = "<$$$>";
   
   private static final String FILE_URL = "file";
   private static final String HTTP_URL = "http";

   //==============================================================
   // PluginLoader Constructor(s)
   //
   // One Argument Constructor is used by MyJSQLView during startup
   // to load default plugins that installed from the lib/plugins
   // directory.
   //
   // Two Argument Constructor should be used to either load plugins
   // from the Plugin Management tool, or possibly externally by
   // outside classes.
   //==============================================================

   public PluginLoader(MyJSQLView_Frame parent)
   {
      try
      {
         init(parent, new URL("file:"));
      }
      catch (MalformedURLException mfe)
      {
         displayErrors("PluginLoader Constructor failed to create empty file:URL\n"
                       + mfe.toString());
      }
   }
   
   public PluginLoader(MyJSQLView_Frame parent, URL pluginURL)
   {
      init(parent, pluginURL);
   }
   
   //==============================================================
   // Class method for initialization and starting the class thread.
   //==============================================================
   
   private void init(MyJSQLView_Frame parent, URL pluginURL)
   {
      parentFrame = parent;
      this.pluginURL = pluginURL;
      
      if (pluginURL == null)
         return;
      
      pluginFileName = pluginURL.getFile();
      // System.out.println("Plugin Name: '" + pluginFileName + "'");
      
      // Setup.
      localSystemFileSeparator = MyJSQLView_Utils.getFileSeparator();
      pluginConfigFileString = MyJSQLView_Utils.getMyJSQLViewDirectory()
                               + MyJSQLView_Utils.getFileSeparator() + CONFIGURATION_FILENAME;
      
      // Default install directory lib/plugins
      if (pluginFileName.equals(""))
         pluginDirectoryString = System.getProperty("user.dir") + localSystemFileSeparator + "lib"
                                 + localSystemFileSeparator + "plugins" + localSystemFileSeparator;
      // Specified plugin
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
            loadPluginEntry();
         }
         catch (IOException ioe){};
      }
      
      // Default lib/plugins and configuration load.
      else
      {
         try
         {
            loadDefaultPluginEntries();
            loadConfigurationFilePluginEntries();
         }
         catch (IOException ioe)
         {
            displayErrors("PluginLoader run() \n" + ioe.toString());
         }
      }
      loadPluginModules();
   }
   
   //==============================================================
   // Class Method for loading a plugin module manually through
   // the MyJSQLView Plugin Management tool in the top tab.
   //
   // File URL - file:lib/plugins/TableRecordCount.jar
   // Http URL - http://dandymadeproductions.com/temp/TableRecordCount.jar
   // JAR URL - jar:file:/home/duke/duke.jar!
   // FTP URL - ftp:/dandymadeproductions.com/
   //
   // Other Valid Java URL protocols, gopher, mailto, appletresource,
   // doc, netdoc, systemresource, & verbatim.
   //==============================================================

   private void loadPluginEntry() throws IOException
   {
      // Method Instances
      String className, currentFileName, pluginEntry;
      
      ZipFile jarFile;
      File configurationFile;
      FileWriter fileWriter;
      
      // Check for a a valid jar file to be processed
      // then search for a plugin module.
      
      if (!pluginFileName.toLowerCase().endsWith(".jar"))
         return;
      
      jarFile = null;
      fileWriter = null;
      
      try
      {
         // Create a URL & then file to the JAR file
         // so that it can be searched.
         
         // Local File system plugin
         if (pluginURL.getProtocol().equals(FILE_URL))
         {
            jarFile = new ZipFile(new File((pluginURL.toExternalForm()).substring(
               pluginURL.toExternalForm().indexOf("file:") + 5)));
         }
         
         // Http plugin
         else if (pluginURL.getProtocol().equals(HTTP_URL))
         {  
            URL jarUrl = new URL(pluginURL, "jar:" + pluginURL + "!/");
            JarURLConnection conn = (JarURLConnection) jarUrl.openConnection();
            jarFile = conn.getJarFile();
         }
         
         // Unknown
         else
            return;
          
         // Search
         className = "";
         
         for (Enumeration<?> entries = jarFile.entries(); entries.hasMoreElements();)
         {
            currentFileName = ((ZipEntry) entries.nextElement()).getName();

            // Plugin Qualifier
            if (currentFileName.endsWith(".class") && currentFileName.indexOf("$") == -1
                && currentFileName.indexOf(VALID_PLUGIN_MODULENAME) != -1)
            {
               currentFileName = currentFileName.replaceAll("/", ".");
               currentFileName = currentFileName.substring(0, currentFileName.indexOf(".class"));
               className = currentFileName;
               
               if (className.startsWith("java.") || className.startsWith("javax."))
                  continue;
               
               pluginEntriesHashMap.put(pluginURL.toExternalForm(), className);
               // System.out.println("Located:" + pathKey + " " + className);
            }
         }
         
         // Update the configuration file indicating valid
         // plugin modules that have been loaded manually.
         
         if (!pluginFileName.equals("") && !className.equals(""))
         {
            pluginEntry = pluginURL.toExternalForm() + pathClassSeparator + className + "\n";
            
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
               fileWriter.flush();
            }
         }
      }
      catch (Exception e)
      {
         displayErrors("PluginLoader loadPluginEntry() Exception: " + pluginFileName + "\n"
                        + e.toString());
      }
      finally
      {
         try
         {
            if (jarFile != null)
               jarFile.close();
         }
         catch (IOException ioe)
         {
            displayErrors("PluginLoader loadPluginEntry() Failed to close jarFile\n"
                          + ioe.toString());
         }
         finally
         {
            if (fileWriter != null)
               fileWriter.close();
         }
      }
   }

   //==============================================================
   // Class Method for reviewing the JAR files in the lib/plugin/
   // directory in search of a MyJSQLView_PluginModule to be then
   // loaded.
   //==============================================================

   private void loadDefaultPluginEntries() throws IOException
   {
      // Method Instances
      File filePluginsDirectory;
      String[] jarFileNames;
      String pathKey, currentFileName;
      ZipFile jarFile;

      // Create the file for the plugin directory & load
      // directory contents.

      filePluginsDirectory = new File(pluginDirectoryString);
      
      if (!filePluginsDirectory.exists())
         return;
      
      jarFileNames = filePluginsDirectory.list();
      
      jarFile = null;

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
                   && currentFileName.indexOf(VALID_PLUGIN_MODULENAME) != -1)
               {
                  pathKey =  (new URL("file:" + pluginDirectoryString + jarFileNames[i])).toExternalForm();
                  // System.out.println("Located:" + pathKey);
                  
                  currentFileName = currentFileName.replaceAll("/", ".");
                  currentFileName = currentFileName.substring(0, currentFileName.indexOf(".class"));
                  
                  if (currentFileName.startsWith("java.") || currentFileName.startsWith("javax."))
                     continue;
                  
                  pluginEntriesHashMap.put(pathKey, currentFileName);
               }
            }
            jarFile.close();
         }
         catch (Exception e)
         {
            displayErrors("PluginLoader loadPluginEntries() Exception: " + jarFileNames[i] + "\n"
                                 + e.toString());
         }
         finally
         {
            if (jarFile != null)
               jarFile.close();
         }
      }
   }
   
   //==============================================================
   // Class Method for reviewing the entries in the configuration
   // file myjsqlview_plugin.conf file to be loaded as plugins.
   //==============================================================

   private void loadConfigurationFilePluginEntries() throws IOException
   {
      // Method Instances
      String currentLine, pathKey, className;
      File configurationFile;
      FileReader fileReader;
      BufferedReader bufferedReader;
      
      fileReader = null;
      bufferedReader = null;
      
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
            displayErrors("PluginLoader loadConfigurationFilePluginEntries() Security Exception: "
                          + e.toString());
            return;
         }
         
         // Looks like there is a plugin configuration file
         // so collect the entries.
         
         fileReader = new FileReader(pluginConfigFileString);
         bufferedReader = new BufferedReader(fileReader);
            
         while ((currentLine = bufferedReader.readLine()) != null)
         {
            currentLine = currentLine.trim();
            
            if (currentLine.indexOf(pathClassSeparator) != -1)
            {
               pathKey = currentLine.substring(0, currentLine.indexOf(pathClassSeparator));
               className = currentLine.substring(currentLine.indexOf(pathClassSeparator) + 5);
               
               if (className.startsWith("java.") || className.startsWith("javax."))
                  continue;
              
               pluginEntriesHashMap.put(pathKey, className);
               // System.out.println("Located:" + pathKey);  
            }
            else
               continue;
         }
      }
      catch (IOException ioe) 
      {
         displayErrors("PluginLoader loadConfigurationFilePluginEntries() File I/O problem, reading "
                       + pluginConfigFileString + "\n" + ioe.toString());
      }
      finally
      {
         try
         {
            if (bufferedReader != null)
               bufferedReader.close();
         }
         catch (IOException ioe)
         {
            displayErrors("PluginLoader loadConfigurationFilePluginEntries() Failed to close bufferReader\n"
                          +ioe.toString());
         }
         finally
         {
            if (fileReader != null)
               fileReader.close();
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

      // Obtain & create default Image Icon.

      iconsDirectory = MyJSQLView_Utils.getIconsDirectory() + localSystemFileSeparator;
      defaultModuleIcon = MyJSQLView.getResourceBundle().getResourceImage(iconsDirectory
                                                                          + "newsiteLeafIcon.png");

      // Iterator through the found plugins and load them.

      Set<Map.Entry<String, String>> keySet = pluginEntriesHashMap.entrySet();
      Iterator<Map.Entry<String, String>> pluginIterator = keySet.iterator();

      while (pluginIterator.hasNext())
      {
         Map.Entry<String, String> pluginEntry = pluginIterator.next();
         
         final String pluginURLString = pluginEntry.getKey();
         // System.out.println(pluginURLString);
            
         ClassLoader classLoader = AccessController.doPrivileged(new PrivilegedAction<ClassLoader>()
         {
            public ClassLoader run()
            {
               try
               {
                  return new URLClassLoader(new URL[] {new URL(pluginURLString)},
                                            ClassLoader.getSystemClassLoader());
               }
               catch (MalformedURLException mfe)
               {
                  displayErrors("PluginLoader classLoader Exception: \n" + mfe.toString());
                  Thread removePluginConfigurationModuleThread = new Thread(new Runnable()
                  {
                     public void run()
                     {
                        PluginFrame.removePluginConfigurationModule(pluginURLString);
                     }
                  }, "PluginLoader.removePluginConfigurationModuleThread1");
                  removePluginConfigurationModuleThread.start();
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
               if (MyJSQLView.getDebug())
                  System.out.println("PluginLoader loadPluginModules() " + pluginEntry.getValue().toString());
               
               try
               {
                  Class<?> module = Class.forName(pluginEntry.getValue(), true, classLoader);
                  
                  if (module.newInstance() instanceof MyJSQLView_PluginModule)
                  {
                     MyJSQLView_PluginModule pluginModule = (MyJSQLView_PluginModule) module.newInstance();
                     pluginModule.pathFileName = pluginEntry.getKey() + pathClassSeparator + pluginEntry.getValue();

                     new PluginThread(parentFrame, pluginModule, defaultModuleIcon);
                  }
                  else
                     throw new Exception();
               }
               catch (ClassNotFoundException cnfe)
               {
                  throw new Exception(cnfe);
               }
               catch (NoClassDefFoundError ncdfe)
               {
                  throw new Exception(ncdfe);
               }
            }
            catch (Exception e)
            {
               displayErrors("PluginLoader loadPluginModules() Exception: \n" + e.toString());
               Thread removePluginConfigurationModuleThread = new Thread(new Runnable()
               {
                  public void run()
                  {
                     PluginFrame.removePluginConfigurationModule(pluginURLString);
                  }
               }, "PluginLoader.removePluginConfigurationModuleThread2");
               removePluginConfigurationModuleThread.start();
            }
         }
      }
   }

   //==============================================================
   // Class method to display an error to the standard output if
   // the debug option is active.
   //==============================================================

   public void displayErrors(String errorString)
   {
      if (MyJSQLView.getDebug())
      {
         System.out.println(errorString);
      }
   }
}
