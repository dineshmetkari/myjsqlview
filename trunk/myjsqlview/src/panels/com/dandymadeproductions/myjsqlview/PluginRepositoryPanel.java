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
// Copyright (C) 2005-2012 Dana M. Proctor
// Version 1.0 09/08/2012
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
//             
//-----------------------------------------------------------------
//                  danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview;

import java.awt.BorderLayout;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/**
 *    The PluginRepositoryPanel class provides a panel that allows
 * the showing of plugins that have been defined from a PluginRepository.
 * A PluginRepository should provide the list that will be used in
 * the panel to display and allow selecting of plugins.
 * 
 * @author Dana M. Proctor
 * @version 1.0 09/08/2012
 */

public class PluginRepositoryPanel extends JPanel
{
   // Class Instances.
   private static final long serialVersionUID = -7266741803741987318L;
   
   private Object[][] pluginsTableData;
   private MyJSQLView_TableModel tableModel;
   private JTable pluginsTable;

   //===========================================================
   // PluginRepositoryPanel Constructor
   //===========================================================

   PluginRepositoryPanel(ArrayList<String> pluginsList)
   {
      // Instances
      ArrayList<String> tableHeadings;
      MyJSQLView_ResourceBundle resourceBundle;
      JScrollPane tableScrollPane;
      String resource;
      
      // Setup
      
      tableHeadings = new ArrayList <String>();
      pluginsTableData = new Object[pluginsList.size()][1];
      resourceBundle = MyJSQLView.getResourceBundle();
      setLayout(new BorderLayout());
      
      // Setup table, listing of plugins.
      
      resource = resourceBundle.getResourceString("PluginRepositoryPanel.label.Plugin", "Plugin");
      tableHeadings.add(resource);
      
      Iterator<String> pluginsListIterator = pluginsList.iterator();
      int i = 0;

      while (pluginsListIterator.hasNext())
         pluginsTableData[i++][0] = pluginsListIterator.next();
      
      
      tableModel = new MyJSQLView_TableModel(tableHeadings, pluginsTableData);

      pluginsTable = new JTable(tableModel);
      pluginsTable.getTableHeader().setFont(new Font(getFont().getName(), Font.BOLD,
                                                     getFont().getSize()));
      
      // Create a scrollpane for the plugins table and place
      // in the center of the panel.
      
      tableScrollPane = new JScrollPane(pluginsTable);
      add(tableScrollPane, BorderLayout.CENTER);
   }
   
   //==============================================================
   // Class method to return the selected plugin entry in the
   // panel if any.
   //==============================================================
   
   public String getSelectedPlugin()
   {
      int selectedRow;
      String pluginName = "";
      
      // Obtain the selected plugin if any.
      
      selectedRow = pluginsTable.getSelectedRow();

      if (selectedRow != -1)
      {
         if (pluginsTable.getValueAt(selectedRow, 0) != null)
         {
            pluginName = (String) pluginsTable.getValueAt(selectedRow, 0);
         }
      }
      return pluginName;
   }
}