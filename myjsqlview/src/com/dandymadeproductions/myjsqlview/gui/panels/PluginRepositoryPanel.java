//=============================================================
//             MyJSQLView PluginRepositoryPanel
//=============================================================
//
//    This class provides a panel that allows the showing of
// plugins that have been defined from a PluginRepository. A
// PluginRepository should provide the list that will be used
// in the panel to display and allow selecting of plugins.
//
//             << PluginRepositoryPanel.java >>
//
//================================================================
// Copyright (C) 2005-2015 Dana M. Proctor
// Version 1.7 10/19/2012
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
// Version 1.0 Original PluginRepositoryPanel Class.
//         1.1 Changed Package Name to com.dandymadeproductions.myjsqlview.plugin.
//             Made Constructor Public.
//         1.2 Changed Package Name to com.dandymadeproductions.myjsqlview.gui.panels.
//         1.3 Rolling Update. Change in Constructor Argument from ArrayList to
//             PluginRepository. Added Methods getRepositoryName/Path/Type().
//         1.4 Changed Class Instance pluginsTable to pluginListTable. Added static
//             final Class Instances for Table Column Definitions. Added Constructor
//             Argument ListSelectionListener. Added Constructor Instances pluginModule,
//             tabIcon, tableColumn, & resourceTabIcon. Also in Constructor Additions
//             to tableHeadings & Proper Filling of Plugin Listing Table. Changed
//             Method getSelectedPlugin() to getSelectedPluginInfo() & Return Type
//             & Composition. Added Method getSelectedPluginPath().
//         1.5 Change in Constructor Instance pluginModule to plugin & Type Plugin
//             Along pluginListIterator to Reflect. Also the Same Type Change in
//             getSelectPluginInfo() for selectedPlugin.
//         1.6 Added Class Methods refreshRepository(), loadPluginTableData(), &
//             formatSize(). Moved tableScrollPane, & tabIcon from Constructor
//             Instances to Class.
//         1.7 Cleanup in Constructor for Instance tableColumn.
//             
//-----------------------------------------------------------------
//                  danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview.gui.panels;

import java.awt.BorderLayout;
import java.awt.Font;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;

import com.dandymadeproductions.myjsqlview.MyJSQLView;
import com.dandymadeproductions.myjsqlview.plugin.Plugin;
import com.dandymadeproductions.myjsqlview.plugin.PluginRepository;
import com.dandymadeproductions.myjsqlview.utilities.MyJSQLView_ResourceBundle;
import com.dandymadeproductions.myjsqlview.utilities.MyJSQLView_TableModel;

/**
 *    The PluginRepositoryPanel class provides a panel that allows
 * the showing of plugins that have been defined from a PluginRepository.
 * A PluginRepository should provide the list that will be used in
 * the panel to display and allow selecting of plugins.
 * 
 * @author Dana M. Proctor
 * @version 1.7 10/19/2012
 */

public class PluginRepositoryPanel extends JPanel
{
   // Class Instances.
   private static final long serialVersionUID = -7266741803741987318L;
   
   private PluginRepository pluginRepository;
   private JTable pluginListTable;
   private MyJSQLView_TableModel tableModel;
   private Object[][] pluginsTableData;
   private JScrollPane tableScrollPane;
   private ImageIcon tabIcon;
   
   private static final int TABICON_COLUMN = 0;
   private static final int NAME_COLUMN = 1;
   private static final int VERSION_COLUMN = 2;
   private static final int CATEGORY_COLUMN = 3;
   private static final int SIZE_COLUMN = 4;
   
   //===========================================================
   // PluginRepositoryPanel Constructor
   //===========================================================

   public PluginRepositoryPanel(PluginRepository pluginRepository,
                                ListSelectionListener listSelectionListener)
   {
      this.pluginRepository = pluginRepository;
      
      // Instances
      ArrayList<String> tableHeadings;
      TableColumn tableColumn;
      
      MyJSQLView_ResourceBundle resourceBundle;
      String resource, resourceTabIcon;
      
      // Setup
      tableHeadings = new ArrayList <String>();
      resourceBundle = MyJSQLView.getResourceBundle();
      setLayout(new BorderLayout());
      
      // Setup table, headings then listing of plugins.
      
      resourceTabIcon = resourceBundle.getResourceString("PluginRepositoryPanel.label.TabIcon", "Tab Icon");
      tableHeadings.add(resourceTabIcon);
      
      tabIcon = resourceBundle.getResourceImage("images/icons/" + "newsiteLeafIcon.png");
      
      resource = resourceBundle.getResourceString("PluginRepositoryPanel.label.Name", "Name");
      tableHeadings.add(resource);
      
      resource = resourceBundle.getResourceString("PluginRepositoryPanel.label.Version", "Version");
      tableHeadings.add(resource);
      
      resource = resourceBundle.getResourceString("PluginRepositoryPanel.label.Category", "Category");
      tableHeadings.add(resource);
      
      resource = resourceBundle.getResourceString("PluginRepositoryPanel.label.Size", "Size");
      tableHeadings.add(resource);
      
      loadPluginTableData();
      tableModel = new MyJSQLView_TableModel(tableHeadings, pluginsTableData);

      pluginListTable = new JTable(tableModel);
      pluginListTable.getTableHeader().setFont(new Font(getFont().getName(), Font.BOLD,
                                                     getFont().getSize()));
      tableColumn = pluginListTable.getColumnModel().getColumn(TABICON_COLUMN);
      tableColumn.setPreferredWidth(resourceTabIcon.length() - 10);
      pluginListTable.getSelectionModel().addListSelectionListener(listSelectionListener);
      
      // Create a scrollpane for the plugins table and place
      // in the center of the panel.
      
      tableScrollPane = new JScrollPane(pluginListTable);
      add(tableScrollPane, BorderLayout.CENTER);
   }
   
   //==============================================================
   // Class method to load/reload the plugin data into the list
   // plugins table.
   //==============================================================
   
   private void loadPluginTableData()
   {
      // Method Instances
      Plugin plugin;
      ArrayList<Plugin> pluginsList;
      Iterator<Plugin> pluginsListIterator;
      
      pluginsList = pluginRepository.getPluginItems();
      
      pluginsTableData = new Object[pluginsList.size()][5];
      
      pluginsListIterator = pluginRepository.getPluginItems().iterator();
      
      int i = 0;

      while (pluginsListIterator.hasNext())
      {
         plugin = pluginsListIterator.next();
         
         pluginsTableData[i][TABICON_COLUMN] = tabIcon;
         pluginsTableData[i][NAME_COLUMN] = plugin.getControlledName();
         pluginsTableData[i][VERSION_COLUMN] = plugin.getControlledVersion();
         pluginsTableData[i][CATEGORY_COLUMN] = plugin.getControlledCategory();
         pluginsTableData[i][SIZE_COLUMN] = formatSize(plugin.getSize());
         
         i++;
      }  
   }
   
   //==============================================================
   // Class method to format the size table model column to an
   // abreviated scientific format.
   //==============================================================
   
   private static String formatSize(int size)
   {
      // Mehthod Instances
      NumberFormat sizeFormat;
      String sizeString;
      
      sizeFormat = NumberFormat.getInstance();
      sizeFormat.setMaximumFractionDigits(3);
      sizeFormat.setMinimumFractionDigits(1);
      
      // 1048576?
      if (size < 1048576)
         sizeString = sizeFormat.format(size/1000.0d) + "KB";
      else
         sizeString = sizeFormat.format(size/1048576.0d) + "MB";
      
      return sizeString;
   }
    
   //==============================================================
   // Class method to return the name of plugin repository that
   // the panel houses.
   //==============================================================
   
   public String getRepositoryName()
   {
      return pluginRepository.getName();
   }
   
   //==============================================================
   // Class method to return the name of plugin repository that
   // the panel houses.
   //==============================================================
   
   public String getRepositoryPath()
   {
      return pluginRepository.getPath();
   }
   
   //==============================================================
   // Class method to return the type of plugin repository that
   // the panel houses.
   //==============================================================
   
   public String getRepositoryType()
   {
      return pluginRepository.getRepositoryType();
   }
   
   //==============================================================
   // Class method to return the selected plugin entry information.
   //==============================================================
   
   public Object[] getSelectedPluginInfo()
   {
      int selectedRow;
      
      // Obtain the selected plugin if any.
      
      selectedRow = pluginListTable.getSelectedRow();

      if (selectedRow != -1)
      {
         if (pluginListTable.getValueAt(selectedRow, 0) != null)
         {
            Object[] pluginInfo = new Object[5];
            Plugin selectedPlugin;
            
            selectedPlugin = (pluginRepository.getPluginItems()).get(selectedRow);
            pluginInfo[0] = selectedPlugin.getName();
            pluginInfo[1] = selectedPlugin.getAuthor();
            pluginInfo[2] = selectedPlugin.getVersion();
            pluginInfo[3] = selectedPlugin.getPath_FileName();
            pluginInfo[4] = selectedPlugin.getDescription();
            
            return pluginInfo;
         }
      }
      return null;
   }
   
   //==============================================================
   // Class method to return the selected plugin entry path.
   //==============================================================
   
   public String getSelectedPluginPath()
   {
      int selectedRow;
      String pluginPath = "";
      
      // Obtain the selected plugin if any.
      
      selectedRow = pluginListTable.getSelectedRow();

      if (selectedRow != -1)
      {
         if (pluginListTable.getValueAt(selectedRow, 0) != null)
            pluginPath = ((pluginRepository.getPluginItems()).get(selectedRow)).getPath_FileName();
      }
      return pluginPath;
   }
   
   //==============================================================
   // Class method to accces the repository in this panel and try
   // refresh its plugin listing.
   //==============================================================
   
   public void refreshRepository()
   {
      pluginRepository.clearPluginItems();
      pluginRepository.refresh();
      loadPluginTableData();
      tableModel.setValues(pluginsTableData);
      tableScrollPane.getVerticalScrollBar().setValue(0);
   }
}
