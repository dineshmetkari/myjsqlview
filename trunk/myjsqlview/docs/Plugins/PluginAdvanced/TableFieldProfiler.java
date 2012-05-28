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
// Copyright (C) 2010-2012 Dana M. Proctor
// Version 4.5 05/07/2012
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
//         2.8 Updated Release Version to 2.8 So That Can Match Next Version Release
//             That Was Classified via the TableFieldChartsPanel Class.
//         2.9 TableFieldAnalsyisPanel Correction in Initialization So That conditional
//             Statement Recongnized for Initial/Single Table Analysis.
//         3.0 Added Class Instance version and Method getVersion().
//         3.1 Added Class Instance path and Assigned From New Constructor Argument
//             of the Same.
//         3.2 Modified Various Aspects to Accomodate Change of MyJSQLView_Access
//             to ConnectionManager Class.
//         3.3 Overhaul to Allow Dropping SQL Query String Into the Charting and
//             Analysis Panels. Removal of TableTabPanel in Field Analysis. Deletion
//             of Capabilities to Save/Open TableTabPanel State.
//         3.4 Added resourceBundle Constructor Argument for MenuActionListener.
//         3.5 Added Class Instance parent, and Passed as Argument to
//             TableFieldAnalsysisPanel.
//         3.6 Parameterized All Vector, & HashMap Types to Bring Code Into
//             Compliance With Java 5.0 API.
//         3.7 Added Class Instances tableFieldInformationPanel & tableFieldAnalysisPanel
//             to Arguments in Creation of MenuActionListener in Constructor.
//         3.8 Update in Classes PiePanel and Plotter Panel to Remove Panels in the
//             Color Chooser Because of Reported Problems With Ubuntu Gnome Desktop.
//         3.9 Updated Version Number to Relect Additions to Locale Information for
//             Popups in PiePanel & PlotterPanel.
//         4.0 Added Class Instances tableFieldClusterAnalysisPanel, INFORMATION_CARD.
//             NUMBER_ANALYSIS_CARD, & CLUSTER_ANALYSIS_CARD. Instantiated tableField
//             clusterAnalysisPanel in Constructor and Passed to pluginMenuListener.
//             Update version.
//         4.1 Updated Version to Reflect Addition of TableFieldClusterAnalysisPanel.
//         4.2 Changes in Classes TableFieldAnalysisPanel, Frequency Table, and
//             TableFieldClusterAnalysisPanel to Limit Results So That For Large
//             Tuples Memory Errors Are Eliminated.
//         4.3 Removal of Not Creating the Analysis Panels if tableNames.isEmpty().
//         4.4 Conditional Check of tableFieldClusterAnalysisPanel for NULL in Method
//             setDBTables().
//         4.5 Changed in Constructor Argument tableNames from Vector to ArrayList.
//                           
//-----------------------------------------------------------------
//                 danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.tablefieldprofiler;

import java.awt.CardLayout;
import java.util.ArrayList;
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
 * @version 4.5 05/07/2012
 */

class TableFieldProfiler
{
   // Class Instances
   private MyJSQLView_Frame parent;
   private String path;
   private Profiler_MenuBar menuBar;
   private Profiler_ToolBar toolBar;
   private JPanel dataProfilerMainPanel;
   private CardLayout profilerCardLayout;
   private TableFieldChartsPanel tableFieldInformationPanel;
   private TableFieldAnalysisPanel tableFieldAnalysisPanel;
   private TableFieldClusterAnalysisPanel tableFieldClusterAnalysisPanel;
   
   private MyJSQLView_ResourceBundle resourceBundle;
   
   protected static final String INFORMATION_CARD = "Information";
   protected static final String NUMBER_ANALYSIS_CARD = "Number Analysis";
   protected static final String CLUSTER_ANALYSIS_CARD = "Cluster Analysis";
   
   private final static String version = "Version 4.5";
   
   //==============================================================
   // TableFieldProfiler Constructor
   //==============================================================

   TableFieldProfiler(MyJSQLView_Frame parent, String path, ArrayList<String> tableNames)
   {
      this.parent = parent;
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

      // Table Field Information Panel.
      tableFieldInformationPanel = new TableFieldChartsPanel(path, resourceBundle, tableNames);
      dataProfilerMainPanel.add(INFORMATION_CARD, tableFieldInformationPanel);

      // Table Field Analysis Panel.
      tableFieldAnalysisPanel = new TableFieldAnalysisPanel(parent, path, resourceBundle, tableNames);
      dataProfilerMainPanel.add(NUMBER_ANALYSIS_CARD, tableFieldAnalysisPanel);
         
      // Table Field Cluster Analysis Panel.
      tableFieldClusterAnalysisPanel = new TableFieldClusterAnalysisPanel(path, resourceBundle, tableNames);
      dataProfilerMainPanel.add(CLUSTER_ANALYSIS_CARD, tableFieldClusterAnalysisPanel);

      // Setup the MenuBar and ToolBar to be used by the plugin.

      pluginMenuListener = new MenuActionListener(parent, resourceBundle, tableFieldInformationPanel,
                                                  tableFieldAnalysisPanel, tableFieldClusterAnalysisPanel,
                                                  dataProfilerMainPanel, profilerCardLayout);
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

   protected void setDBTables(ArrayList<String> tableNames)
   {
      // Create panels if needed.
      
      if (tableFieldInformationPanel == null || tableFieldAnalysisPanel == null
            || tableFieldClusterAnalysisPanel == null)
      {
         tableFieldInformationPanel = new TableFieldChartsPanel(path, resourceBundle, tableNames);
         dataProfilerMainPanel.add("Information", tableFieldInformationPanel);

         tableFieldAnalysisPanel = new TableFieldAnalysisPanel(parent, path, resourceBundle, tableNames);
         dataProfilerMainPanel.add("Analysis", tableFieldAnalysisPanel);
         
         tableFieldClusterAnalysisPanel = new TableFieldClusterAnalysisPanel(path, resourceBundle, tableNames);
         dataProfilerMainPanel.add(CLUSTER_ANALYSIS_CARD, tableFieldClusterAnalysisPanel); 
      }
      else
      {
         // Reload Information Charts.
         tableFieldInformationPanel.reloadPanel(tableNames);
         tableFieldInformationPanel.repaint();

         // Reload Analysis.
         tableFieldAnalysisPanel.reloadPanel(tableNames);
         tableFieldAnalysisPanel.repaint();
         
         tableFieldClusterAnalysisPanel.reloadPanel(tableNames);
         tableFieldClusterAnalysisPanel.repaint();
      }
   }
}