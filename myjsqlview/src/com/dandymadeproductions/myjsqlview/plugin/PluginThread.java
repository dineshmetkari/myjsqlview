//=================================================================
//                   MyJSQLView PluginThread
//=================================================================
//    This class provides a thread to set in motion the adding of
// a corrected located plugin module to the MyJSQLView's main frame. 
//
//                   << PluginThread.java >>
//
//=================================================================
// Copyright (C) 2005-2015 Dana M. Proctor
// Version 1.9 09/29/2012
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
// Version 1.0 Original PluginThread Class.
//         1.1 Added the Processing of the Storage of Version Information
//             for the Plugin in Class Method run().
//         1.2 Class Method run() Call to MyJSQLView_Frame.addTab() Added
//             Argument parentFrame.
//         1.3 Class Method run() Obtain String path to Pass to New Argument
//             of PluginModuleInterface Method initPlugin().
//         1.4 Copyright Update.
//         1.5 Copyright Update.
//         1.6 Change in the Derivation of the path in run() to Not Use the
//             Local File System File Separator Since Ultimately Everything
//             is Turned Into a URL.
//         1.7 Changed Package Name to com.dandymadeproductions.myjsqlview.plugin.
//         1.8 Change in run() to Access MyJSQLView_PluginModule Protected
//             Class Instances Directly for Setting.
//         1.9 Change in run() to Set the pluginModule.author.
//
//-----------------------------------------------------------------
//                   danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview.plugin;

import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import com.dandymadeproductions.myjsqlview.gui.MyJSQLView_Frame;

/**
 *    The PluginThread class provides a thread to set in motion the
 * adding of a correctly located plugin module to the MyJSQLView's
 * main frame.
 * 
 * @author Dana M. Proctor
 * @version 1.9 09/29/2012
 */

class PluginThread implements Runnable
{
   // Class Instances
   Thread t;

   private MyJSQLView_Frame parentFrame;
   private MyJSQLView_PluginModule pluginModule;
   private ImageIcon defaultIcon;

   //==============================================================
   // PluginThread Constructor.
   //==============================================================

   PluginThread(MyJSQLView_Frame parent, MyJSQLView_PluginModule module, ImageIcon defaultModuleIcon)
   {
      parentFrame = parent;
      pluginModule = module;
      defaultIcon = defaultModuleIcon;

      // Create and start the class thread.
      t = new Thread(this, "PluginThread");
      // System.out.println("Plugin Thread");

      t.start();
   }

   //==============================================================
   // Class method for normal start of the thread. Adds the plugin
   // module to the MyJSQLView_Frame.
   //==============================================================

   public void run()
   {
      // Method Instances
      String path;
      
      // Collect path, since code will always be dealing
      // with URLs for these plugins to not use a local
      // file System file separator.
      
      path = pluginModule.getPath_FileName().substring(0, pluginModule.getPath_FileName().indexOf("<$$$>"));
      
      if (path.indexOf("/") != -1)
         path = path.substring(0, path.lastIndexOf("/"));
     
      // Call the plugin's initializing code.
      pluginModule.initPlugin(parentFrame, path);

      // Check all the main aspects needed by MyJSQLView
      // in the loaded plugin module and isolate the
      // application from deviants

      // Name
      if (pluginModule.getName() == null)
         pluginModule.name = "";
      else
      {
         if ((pluginModule.getName()).length() > 50)
            pluginModule.name = (pluginModule.getName()).substring(0, 49);
         else
            pluginModule.name = pluginModule.getName();
      }
      
      // Author
      if (pluginModule.getAuthor() == null)
         pluginModule.author = "Not Identified";
      else
         pluginModule.author = pluginModule.getAuthor();
      
      // Version
      if (pluginModule.getVersion() == null)
         pluginModule.version = "Not Identified";
      else
         pluginModule.version = pluginModule.getVersion();
      
      // Description
      if (pluginModule.getDescription() == null)
         pluginModule.description = "Not Given";
      else
         pluginModule.description = pluginModule.getDescription();
      
      // Tab Icon
      if (pluginModule.getTabIcon() == null)
         pluginModule.tabIcon = defaultIcon;
      else
         pluginModule.tabIcon = new ImageIcon((pluginModule.getTabIcon()).getImage().getScaledInstance(12,
            12, Image.SCALE_FAST));

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
      
      // Main Panel
      if (pluginModule.getPanel() == null)
         pluginModule.panel = new JPanel();
      else
         pluginModule.panel = (pluginModule.getPanel());

      // Store/Add Plugin
      MyJSQLView_Frame.addTab(pluginModule, parentFrame);
   }
}
