//=================================================================
//                      TableFieldProfiler
//=================================================================
//
//    This class provides the main access point for setting up the
// requirements for the Table Field Profiler Module for the MyJSQLView
// application.
//
//                  << TableFieldProfiler.java >>
//
//=================================================================
// Copyright (C) 2005-2011 Dana M. Proctor
// Version 3.2 02/03/2011
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
// Version 1.0 Initial TableFieldProfiler Class.
//         1.1 Expansion of Module to Include TableFieldAnalysis In Addition
//             to TableFieldCharts. Menu and ToolBar Inclusion.
//         1.2 Added Class Instances localeDirectory, fileSeparator & resourceBundle
//             Also Class Method getLocaleResourceBundle().
//         1.3 Added parent JFrame Argument to pluginMenuListener Instantiation.
//         1.4 Added resouceBundle to Profiler_MenuBar/ToolBar Instantiation. Also
//             Profiler_ToolBar Added parent to Argument.
//         1.5 Added resourceBundle to tableFieldInformationPanel & tableFieldAnalysisPanel
//             Instantiation in Constructor and Method setDBTables().
//         2.8 Updated Release Version to 2.8 So That Can Match Next Version Release That
//             Was Classified via the TableFieldChartsPanel Class.
//         2.9 TableFieldAnalsyisPanel Correction in Initialization So That conditional
//             Statement Recongnized for Initial/Single Table Analysis.
//         3.0 Added Class Instance version and Method getVersion().
//         3.1 Added Class Instance path and Assigned From New Constructor Argument of
//             the Same.
//         3.2 Modified Various Aspects to Accomodate Change of MyJSQLView_Access to
//             ConnectionManager Class.
//                           
//-----------------------------------------------------------------
//                 danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.tablefieldprofiler;

import java.awt.CardLayout;
import java.util.Vector;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import com.dandymadeproductions.myjsqlview.MyJSQLView;
import com.dandymadeproductions.myjsqlview.MyJSQLView_Frame;
import com.dandymadeproductions.myjsqlview.MyJSQLView_ResourceBundle;
import com.dandymadeproductions.myjsqlview.MyJSQLView_Utils;

/**
 *    The TableFieldProfiler class provides the main access point for setting up
 * the requirements for the Table Field Profiler Module for the MyJSQLView
 * application.
 * 
 * @author Dana M. Proctor
 * @version 3.2 02/03/2011
 */

class TableFieldProfiler
{
   // Class Instances
   private String path;
   private Profiler_MenuBar menuBar;
   private Profiler_ToolBar toolBar;
   private JPanel dataProfilerMainPanel;
   private CardLayout profilerCardLayout;
   private TableFieldChartsPanel tableFieldInformationPanel;
   private TableFieldAnalysisPanel tableFieldAnalysisPanel;
   
   private MyJSQLView_ResourceBundle resourceBundle;
   private final static String version = "Version 3.2";
   
   //==============================================================
   // TableFieldProfiler Constructor
   //==============================================================

   TableFieldProfiler(MyJSQLView_Frame parent, String path, Vector tableNames)
   {
      this.path = path;
      
      // Constructor Instances.
      String fileSeparator, localeDirectory;
      MenuActionListener pluginMenuListener;

      // Setup the Main panel and the plugin's components.

      profilerCardLayout = new CardLayout();
      dataProfilerMainPanel = new JPanel(profilerCardLayout);
      
      fileSeparator = MyJSQLView_Utils.getFileSeparator();
      localeDirectory = path + fileSeparator + "TableFieldProfiler" + fileSeparator + "locale";
      resourceBundle = new MyJSQLView_ResourceBundle(localeDirectory, "TableFieldProfiler",
                                                     MyJSQLView.getLocaleString());

      if (!tableNames.isEmpty())
      {
         // Table Field Information Panel.
         tableFieldInformationPanel = new TableFieldChartsPanel(path, resourceBundle, tableNames);
         dataProfilerMainPanel.add("Information", tableFieldInformationPanel);

         // Table Field Analysis Panel.
         tableFieldAnalysisPanel = new TableFieldAnalysisPanel(path, resourceBundle, tableNames);
         dataProfilerMainPanel.add("Analysis", tableFieldAnalysisPanel);
      }

      // Setup the MenuBar and ToolBar to be used by the plugin.

      pluginMenuListener = new MenuActionListener(parent, dataProfilerMainPanel, profilerCardLayout);
      menuBar = new Profiler_MenuBar(parent, resourceBundle, pluginMenuListener);
      toolBar = new Profiler_ToolBar("Table Field Profiler ToolBar", parent, path, resourceBundle,
                                     pluginMenuListener);
   }

   //==============================================================
   // Class method to to the plugin's JMenuBar
   //==============================================================

   protected JMenuBar getMenuBar()
   {
      return menuBar;
   }

   //==============================================================
   // Class method get the plugin's JToolBar
   //==============================================================

   protected JToolBar getToolBar()
   {
      return toolBar;
   }

   //==============================================================
   // Class method to get the main panel associated with the plugin.
   //==============================================================

   protected JPanel getPanel()
   {
      return dataProfilerMainPanel;
   }
   
   //==============================================================
   // Class method to get the plugin's version.
   //==============================================================

   protected static String getVersion()
   {
      return version;
   }

   //==============================================================
   // Class method to set the database tables.
   //==============================================================

   protected void setDBTables(Vector tableNames)
   {
      // Create panels if needed.
      
      if (tableFieldInformationPanel == null || tableFieldAnalysisPanel == null)
      {
         tableFieldInformationPanel = new TableFieldChartsPanel(path, resourceBundle, tableNames);
         dataProfilerMainPanel.add("Information", tableFieldInformationPanel);

         tableFieldAnalysisPanel = new TableFieldAnalysisPanel(path, resourceBundle, tableNames);
         dataProfilerMainPanel.add("Analysis", tableFieldAnalysisPanel);
      }
      else
      {
         // Reload Information Charts.
         tableFieldInformationPanel.reloadPanel(tableNames);
         tableFieldInformationPanel.repaint();

         // Reload Analysis.
         tableFieldAnalysisPanel.reloadPanel(tableNames);
         tableFieldAnalysisPanel.repaint();
      }
   }
}