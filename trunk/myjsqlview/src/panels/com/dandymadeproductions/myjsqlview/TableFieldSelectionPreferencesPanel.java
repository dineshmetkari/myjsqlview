//=================================================================
//      MyJSQLView TableFieldSelectionsPreferencesPanel.
//=================================================================
//
//    This class provides the ability to select the preferred
// database table fields to be display in the MyJSQLView
// TableTabPanel summary table.
//
//         << TableFieldSelectionPreferencesPanel.java >>
//
//=================================================================
// Copyright (C) 2007-2011 Dana M. Proctor
// Version 4.3 09/13/2011
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
// Version 1.0 Original MyJSQLView TableFieldPreferences Class.
//         1.1 Removed frameListener().
//         1.2 Implement Setting of Fields.
//         1.3 Removed Class Method setFields().
//         1.4 Began Implementation of Class Method updateUserPreferences();
//         1.5 Completed Implementation of Class Method
//             updateUserPreferences().
//         1.6 Updated Class Method loadPreferences() to Properly
//             Select the Current Selected Table Headings.
//         1.7 Changed to TableFieldSelection Panel. No Longer
//             an Independent Frame.
//         1.8 Constructor Argument Changed, index. Changed Class
//             Method updatePreferences() & How Access to TableTabPanel.
//         1.9 Panel Border Changes and applyButton.setEnabled() Actions
//             to Include Class Method itemStateChanged().
//         2.0 Renamed to TableFieldSelectionPreferencesPanel.
//         2.1 Class Method updatePreferences() Mofified to Protected
//             and Check for applyButton.isEnabled().
//         2.2 Constructor Instances primaryKeys & columnNamesHashMap
//             to Properly Reserve Keys from Being Removed from List Table.
//         2.3 Checked primaryKeys Before Select All and Clear All
//             Actions.
//         2.4 Removed Unused Class Instance tableFields.
//         2.5 Performed checkBoxFields Modulus 2 Check In Conditional 
//             to Determine the rowNumber. Changed columnNumber to
//             rowNumber.
//         2.6 Cleaned Up Javadoc Comments.
//         2.7 Header Update.
//         2.8 Class Method updatePreferences Changed From setTableFields()
//             to setTableHeadings().
//         2.9 TableTabPanel Change of Class Method Changed getTableHeadings()
//             to getAllTableHeadings().
//         3.0 Added Class Instance serialVersionUID.
//         3.1 Replaced Class int myjsqlviewTabIndex With String tableName.
//             Replaced MyJSQLView.getTab(myjsqlviewTabIndex) With
//             DBTablesPanel.getTableTabPanel(tableName).
//         3.2 MyJSQLView Project Common Source Code Formatting.
//         3.3 Class Method updatePreferences() Added the Call To the DBTablesPanel
//             to setSelectedTableTabPanel().
//         3.4 Class Method updatePreferences() Added Processing as a Thread That
//             Allows Tracking Activity in DBTablesPanel.
//         3.5 Class Method updatePreferences() Provided a String Name for the
//             Thread updateTableTabPanelFieldsThread.
//         3.6 Header Format Changes/Update.
//         3.7 Changed Package to Reflect Dandy Made Productions Code.
//         3.8 Implementation of Internationalization via Class Instance resourceBundle.
//             Added Argument to Constructor, & Constructor Instance resource.
//         3.9 Parameterized Class Instances checkBoxesHashMap, columnNamesHashMap,
//             & primaryKeys in Order to Bring Code Into Compliance With Java
//             5.0 API. Also Constructor Argument checkBoxFields, Method Instances
//             tableHeadings, & newFields in loadPreferences() & updatePreferences().
//         4.0 Parameterized tableColumnNamesIterator in Constructor and
//             currentFieldIterator in Class Method loadPreferences().
//         4.1 Copyright Update.
//         4.2 Removed Arguments tableName, and checkBoxFields in Constructor and
//             Replaced With TableTabPanel. Removed Class Instance tableName added
//             tablePanelPanel. Removed All References to DBTablesPanel, loadPreferences()
//             & updatePreferences().
//         4.3 Added Constructor Instances iconsDirectory, keyUpIcon, keyDownIcon.
//             Setup in Constructor the Assignmet of keyUp/DownIcon for Key Field
//             Checkboxes & Made Them Selectable. Class Method actionPerformed()
//             Select All CheckBoxes Enabled for All, Including Keys Fields. Class
//             Method updatePrefences() Add Instance viewOnlyState & Implemented
//             With This Instance the Setting of the TableTablPanels setViewOnly()
//             Method.
//             
//-----------------------------------------------------------------
//                 danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *    The TableFieldSelectionPreferencesPanel class provides the
 * ability to select the preferred database table fields to be
 * display in the MyJSQLView TableTabPanel summary table.
 * 
 * @author Dana M. Proctor
 * @version 4.3 09/13/2011
 */

class TableFieldSelectionPreferencesPanel extends JPanel implements ActionListener, ItemListener
{
   // =============================================
   // Creation of the necessary class instance
   // variables for the JPanel.
   // =============================================

   private static final long serialVersionUID = 188742030301366822L;

   private TableTabPanel tableTabPanel;
   private Vector<String> checkBoxFields;
   private JCheckBox[] columnNamesCheckBoxes;
   private HashMap<String, JCheckBox> checkBoxesHashMap;
   private HashMap<String, String> columnNamesHashMap;
   private Vector<String> primaryKeys;
   private JButton selectAllButton, clearAllButton, applyButton;

   //==============================================================
   // TableFieldSelectionPreferencesPanel Constructor
   //==============================================================

   TableFieldSelectionPreferencesPanel(TableTabPanel tableTabPanel, MyJSQLView_ResourceBundle resourceBundle)
   {
      this.tableTabPanel = tableTabPanel;
      
      // Class Instances
      JPanel itemSelections, southButtonPanel;
      int rowNumber;
      String iconsDirectory, resource;
      ImageIcon keyUpIcon, keyDownIcon;

      // Setting up
      setLayout(new BorderLayout());
      checkBoxesHashMap = new HashMap <String, JCheckBox>();
      
      iconsDirectory = MyJSQLView_Utils.getIconsDirectory() + MyJSQLView_Utils.getFileSeparator();
      keyUpIcon = new ImageIcon(iconsDirectory + "keyUpIcon.png");
      keyDownIcon = new ImageIcon(iconsDirectory + "keyDownIcon.png");
      
      // Setting up table column names' checkboxes that will be used
      // to select the desired tabel fields to be displayed.
      
      checkBoxFields = tableTabPanel.getAllTableHeadings();

      rowNumber = checkBoxFields.size() / 2;
      if (checkBoxFields.size() % 2 > 0)
         rowNumber = checkBoxFields.size() / 2 + 1;

      itemSelections = new JPanel(new GridLayout(rowNumber, 4, 0, 0));
      itemSelections.setBorder(BorderFactory.createLoweredBevelBorder());

      Iterator<String> tableColumnNamesIterator = checkBoxFields.iterator();
      columnNamesCheckBoxes = new JCheckBox[checkBoxFields.size()];

      columnNamesHashMap = tableTabPanel.getColumnNamesHashMap();
      primaryKeys = tableTabPanel.getPrimaryKeys();

      int i = 0;
      while (tableColumnNamesIterator.hasNext())
      {
         String columnName = (String)tableColumnNamesIterator.next();

         if (primaryKeys.contains(columnNamesHashMap.get(columnName)))
         {
            columnNamesCheckBoxes[i] = new JCheckBox(columnName, keyUpIcon);
            columnNamesCheckBoxes[i].setSelectedIcon(keyDownIcon);
         }
         else
            columnNamesCheckBoxes[i] = new JCheckBox(columnName);

         itemSelections.add(columnNamesCheckBoxes[i]);
         checkBoxesHashMap.put(columnName, columnNamesCheckBoxes[i++]);
      }

      loadPreferences();
      JScrollPane listScrollPane = new JScrollPane(itemSelections);
      listScrollPane.getVerticalScrollBar().setUnitIncrement(10);

      add(listScrollPane, BorderLayout.CENTER);

      // Buttons to set all, clear all the checkboxes
      // or apply the changes to the selected table summary
      // view.
      southButtonPanel = new JPanel();
      southButtonPanel.setBorder(BorderFactory.createEmptyBorder());

      resource = resourceBundle.getResource("TableFieldSelectionPreferencesPanel.button.SelectAll");
      if (resource.equals(""))
         selectAllButton = new JButton("Select All");
      else
         selectAllButton = new JButton(resource);
      selectAllButton.setFocusPainted(false);
      selectAllButton.addActionListener(this);
      southButtonPanel.add(selectAllButton);

      resource = resourceBundle.getResource("TableFieldSelectionPreferencesPanel.button.ClearAll");
      if (resource.equals(""))
         clearAllButton = new JButton("Clear All");
      else
         clearAllButton = new JButton(resource);
      clearAllButton.setFocusPainted(false);
      clearAllButton.addActionListener(this);
      southButtonPanel.add(clearAllButton);

      resource = resourceBundle.getResource("TableFieldSelectionPreferencesPanel.button.Apply");
      if (resource.equals(""))
         applyButton = new JButton("Apply");
      else
         applyButton = new JButton(resource);
      applyButton.setFocusPainted(false);
      applyButton.setEnabled(false);
      applyButton.addActionListener(this);
      southButtonPanel.add(applyButton);

      add(southButtonPanel, BorderLayout.SOUTH);
   }

   //==============================================================
   // ActionEvent Listener method for determining when the selections
   // have been made so an update can be performed on the summary
   // table being displayed in the tab(s).
   //==============================================================

   public void actionPerformed(ActionEvent evt)
   {
      Object panelSource = evt.getSource();

      // Apply Button Action.
      if (panelSource == applyButton)
      {
         updatePreferences();
         applyButton.setEnabled(false);
      }

      // Select All Checkboxes
      else if (panelSource == selectAllButton)
      {
         for (int i = 0; i < columnNamesCheckBoxes.length; i++)
            columnNamesCheckBoxes[i].setSelected(true);
         applyButton.setEnabled(true);
      }

      // Clear All Checkboxes.
      else if (panelSource == clearAllButton)
      {
         for (int i = 0; i < columnNamesCheckBoxes.length; i++)
         {
            if (!primaryKeys.contains(columnNamesHashMap.get(columnNamesCheckBoxes[i].getText())))
               columnNamesCheckBoxes[i].setSelected(false);
         }
         applyButton.setEnabled(true);
      }
   }

   //==============================================================
   // ActionEvent Listener method for determining when the selections
   // have been made so an update can be performed on the summary
   // table being displayed in the tab(s).
   //==============================================================

   public void itemStateChanged(ItemEvent evt)
   {
      Object panelSource = evt.getSource();

      if (panelSource instanceof JCheckBox)
         applyButton.setEnabled(true);
   }

   //==============================================================
   // Class method to load the current users fields preferences.
   //==============================================================

   private void loadPreferences()
   {
      // Method Instances
      Vector<String> tableHeadings;
      Iterator<String> currentFieldIterator;
      String currentElementName;
      JCheckBox currentCheckBox;

      // Loading the current table fields.
      tableHeadings = tableTabPanel.getCurrentTableHeadings();

      currentFieldIterator = checkBoxFields.iterator();

      while (currentFieldIterator.hasNext())
      {
         currentElementName = (String) currentFieldIterator.next();
         currentCheckBox = checkBoxesHashMap.get(currentElementName);

         if (tableHeadings.contains(currentElementName))
            currentCheckBox.setSelected(true);

         currentCheckBox.addItemListener(this);
      }
   }

   //==============================================================
   // Class method to allow the setting of TableTabPanel preferences
   // that will be used to view the summary table of data.
   //==============================================================

   protected void updatePreferences()
   {
      if (applyButton.isEnabled())
      {
         Thread updateTableTabPanelFieldsThread = new Thread(new Runnable()
         {
            public void run()
            {
               // Instances
               int checkBoxCount;
               Vector<String> newFields;
               Boolean viewOnlyState;

               // Determine which of the table fields have been
               // selected.
               checkBoxCount = columnNamesCheckBoxes.length;
               newFields = new Vector <String>();
               viewOnlyState = false;

               for (int i = 0; i < checkBoxCount; i++)
               {
                  if (columnNamesCheckBoxes[i].isSelected())
                     newFields.add((String) columnNamesCheckBoxes[i].getText());
                  else
                     if (primaryKeys.contains(columnNamesHashMap.get(columnNamesCheckBoxes[i].getText())))
                        viewOnlyState = true;
               }

               // Setting the new field preferences.
               tableTabPanel.setTableHeadings(newFields);
               tableTabPanel.setViewOnly(viewOnlyState);
            }
         }, "TableFieldSelectionPreferencesPanel.updatetableTabPanelFieldsThread");
         updateTableTabPanelFieldsThread.start();
      }
   }
}