//=================================================================
//         MyJSQLView MyJSQLView_PluginModule Class
//=================================================================
//
//    This class provides the abstract framework for plugin classes
// to extends in order to properly function within the MyJSQLView
// application.
//
//             << MyJSQLView_PluginModule.java >>
//
//=================================================================
// Copyright (C) 2005-2015 Dana M. Proctor
// Version 3.6 10/19/2014
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
//         1.4 Added Interface Method getToolBar(). Changed Methods getModuleName()
//             to getName(), getModuleTabIcon() to getTabIcon(), and getModulePanel()
//             to getPanel().
//         1.5 Added Interface Method setParentFrame().
//         1.6 Added Interface Method initPlugin().
//         1.7 Changed to an Abstract Class That Implements PluginModuleInterface.
//             Removed Class Method setParentFrame() and Commented initPlugin()
//             With New Argument MyJSQLView_Frame So That Plugins Must Implement.
//         1.8 Parameterized Argument tableNames in Class Method setDBTables()
//             in Order For Code to Be in Compliance With Java 5.0 API.
//         1.9 Added Class Instances pluginName, pluginIcon, pluginMenuBar, pluginToolBar,
//             & pluginPanel. Set All to Null in Constructor. Created Setter Methods
//             to Accompany Getter Methods. Returned Newly Created Instances in
//             Getter Methods.
//         2.0 Removed the Setter Methods. Made Class Instances From 1.9 Protected and
//             Reverted Back to the Default Instances Returned by the Getters to Null.
//         2.1 Added Interface Method getVersion() and Class Instance version.
//         2.2 Added Instance pathFileName and Protected Methods get/setPath_FileName().
//         2.3 Added Argument String path to Method initPlugin().
//         2.4 Copyright Update.
//         2.5 Copyright Update.
//         2.6 Changed Argument Requirement tableNames in setDBTables to Data Type
//             ArrayList from Vector.
//         2.7 Added Class Instance description. Set getPath_FileName() public &
//             Added Method getDescription().
//         2.8 Changed Package Name to com.dandymadeproductions.myjsqlview.plugin.
//             Added Class Method setName(). Changed Getter Methods to Return the
//             Appropriate Class Instance Instead of Null.
//         2.9 Correction of Class Method setname() to setName().
//         3.0 Made All Class Instances Protected. Added Controlled Getter Methods.
//         3.1 Added Class Instance author & Methods getAuthor() & getControlledAuthor().
//         3.2 Added Class Instances category, size, & Their Getter Methods.
//         3.3 Comment Change.
//         3.4 Added Interface Method shutdown().
//         3.5 Changed the Return Types for Methods getToolBar(), getPanel(),
//             getControlledToolBar(), & getControlledPanel() From JToolBar
//             & JPanel to JComponent. Class Instance toolBar & panel Changed
//             Accordingly.
//         3.6 Added Interface Methods start() & stop().
//             
//-----------------------------------------------------------------
//                 danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview.plugin;

import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JMenuBar;

/**
 *    The MyJSQLView_PluginModule class provides the abstract framework
 * for plugin classes to extends in order to properly function within
 * the MyJSQLView application.
 * 
 * @author Dana M. Proctor
 * @version 3.6 10/19/2014
 */

public abstract class MyJSQLView_PluginModule implements PluginModuleInterface
{
   // Class Instances.
   //protected MyJSQLView_Frame parent;
   protected String pathFileName;
   public String name, author;
   protected String version, description, category;
   protected int size;
   protected ImageIcon tabIcon;
   protected JMenuBar menuBar;
   protected JComponent toolBar;
   protected JComponent panel;

   //===========================================================
   // MyJSQLView_PluginModule Constructor
   //===========================================================

   public MyJSQLView_PluginModule()
   {
      // Just Initialize to a NULL condition.
      
      pathFileName = null;
      name = null;
      author = null;
      version = null;
      description = null;
      category = null;
      size = 0;
      tabIcon = null;
      menuBar = null;
      toolBar = null;
      panel = null;
   }
   
   //==============================================================
   // Class method to setup up your plugin.
   // OVERIDE THIS METHOD!
   //==============================================================

   /*
   public void initPlugin(MyJSQLView_Frame mainFrame, String path)
   {
      // This is where the plugin should be initialized.
      parent = mainFrame;
   }
   */
   
   //==============================================================
   // Class methods to get/set the plugin's file name.
   //==============================================================
   
   public String getPath_FileName()
   {
      return pathFileName;
   }
   
   protected String getControlledPath_FileName()
   {
      return pathFileName;
   }
   
   //==============================================================
   // Class method to get/set the plugin's name.
   // Interface requirement.
   //==============================================================

   public String getName()
   {
      return name;
   }
   
   public String getControlledName()
   {
      return name;
   }
   
   //==============================================================
   // Class method to get/set the plugin's author.
   // Interface requirement.
   //==============================================================

   public String getAuthor()
   {
      return author;
   }
   
   public String getControlledAuthor()
   {
      return author;
   }
   
   //==============================================================
   // Class method to obtain the plugin's version number.
   // Interface requirement.
   //==============================================================

   public String getVersion()
   {
      return version;
   }
   
   public String getControlledVersion()
   {
      return version;
   }
   
   //==============================================================
   // Class method to obtain the plugin's description.
   // Interface requirement.
   //==============================================================

   public String getDescription()
   {
      return description;
   }
   
   public String getControlledDescription()
   {
      return description;
   }
   
   //==============================================================
   // Class method to obtain the plugin's category.
   // Interface requirement.
   //==============================================================

   public String getCategory()
   {
      return category;
   }
   
   public String getControlledCategory()
   {
      return category;
   }
   
   //==============================================================
   // Class method to obtain the plugin's size.
   // Interface requirement.
   //==============================================================

   public int getSize()
   {
      return size;
   }
   
   public int getControlledSize()
   {
      return size;
   }

   //==============================================================
   // Class method to allow the collection of a image icon that
   // will be used as an identifier in the MyJSQLView tab structure.
   // NOTE: The tab icon should be no larger than 12 x 12.
   // Interface requirement.
   //==============================================================

   public ImageIcon getTabIcon()
   {
      return tabIcon;
   }
   
   public ImageIcon getControlledTabIcon()
   {
      return tabIcon;
   }

   //==============================================================
   // Class method to obtain the plugin's JMenuBar that can be
   // used to control various aspects of the modules functionality.
   // Interface requirement.
   //==============================================================

   public JMenuBar getMenuBar()
   {
      return menuBar;
   }
   
   public JMenuBar getControlledMenuBar()
   {
      return menuBar;
   }
   
   //==============================================================
   // Class method to allow the collection of a JToolBar to be
   // used with the plugin module.
   // Interface requirement.
   //==============================================================

   public JComponent getToolBar()
   {
      return toolBar;
   }
   
   public JComponent getControlledToolBar()
   {
      return toolBar;
   }
   
   //==============================================================
   // Class method for returning a JPanel for inclusion in the
   // MyJSQLView application's main tab. Interface requirement.
   //==============================================================

   public JComponent getPanel()
   {
      return panel;
   }
   
   public JComponent getControlledPanel()
   {
      return panel;
   }
   
   //==============================================================
   // Class method for being able to set the database tables, occurs
   // if the database is reloaded.
   // Interface requirement.
   //==============================================================

   public void setDBTables(ArrayList<String> tableNames)
   {
      // Do what you will if you need database table names.
   }
   
   //==============================================================
   // Class method to allow the plugin to start activities back
   // up after a stop() sequence.
   // (USED FOR CONTROLLING THREADS)
   //==============================================================

   public void start()
   {
      // Do what you will to start again from stop.
   }
   
   //==============================================================
   // Class method to allow the plugin to temporarily stop 
   // activities that may then be started again.
   // (USED FOR CONTROLLING THREADS)
   //==============================================================

   public void stop()
   {
      // Do what you will to notify stop.
   }
   
   //==============================================================
   // Class method to allow the plugin to close activities pending
   // a closing of the application.
   //==============================================================
   
   public void shutdown()
   {
      // Do what you will to notify pending closing.
   }
}
