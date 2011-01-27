//=================================================================
//       MyJSQLView TableRowSelectionPreferencesPanel.
//=================================================================
//
//    This class provides the ability to select the preferred table
// row size to be display in the MyJSQLView TableTabPanel summary
// table.
//
//        << TableRowSelectionPreferencesPanel.java >>
//
//=================================================================
// Copyright (C) 2007-2011 Dana M. Proctor
// Version 3.3 01/27/2011
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
// Version 1.0 Original MyJSQLView TableRowSelectionPanel Class.
//         1.1 Completed The Implemetation of the Panel.
//         1.2 Class Method stateChanged() Check for applyButton Not Null.
//         1.3 Changed to TableRowSelectionPreferencesPanel.
//         1.4 Removed Vector checkBoxFields from Constructor Argument. Cleaned
//             Out Some Un-Used Instances.
//         1.5 Class Method updatePreferences() Changed to Protected and
//             applyButton.isEnabled().
//         1.6 Removed Unused Class Instance currentRowSize. Class Instance
//             defaultRowSize to Static.
//         1.7 Cleaned Up Javadoc Comments.
//         1.8 Correnctions in Javadoc Comments.
//         1.9 Header Update.
//         2.0 Consructor Instance maxRowSize Increased to 20000.
//         2.1 Added Class Instance serialVersionUID.
//         2.2 Replaced Class int myjsqlviewTabIndex With String tableName.
//             Replaced MyJSQLView.getTab(myjsqlviewTabIndex) With
//             DBTablesPanel.getTableTabPanel(tableName).
//         2.3 MyJSQLView Project Common Source Code Formatting.
//         2.4 Class Method loadRowSize() Conditional Check for NULL TableTabPanel
//             and Default Setting of rowSize to 50.
//         2.5 Class Method updatePreferences() Added the Call To the DBTablesPanel
//             to setSelectedTableTabPanel().
//         2.6 Class Method updatePreferences() Added Processing as a Thread That
//             Allows Tracking Activity in DBTablesPanel.
//         2.7 Class Method updatePreferences() Provided a String Name for the
//             Thread updateTableTabPanelRowsThread.
//         2.8 Header Format Changes/Update.
//         2.9 Class Method loadRowSize() Set rowSize to defaultRowSize on else
//             Condition.
//         3.0 Changed Package to Reflect Dandy Made Productions Code.
//         3.1 Implementation of Internationalization via Class Instance resourceBundle.
//             Argument Added to Constructor and Constructor Instance resource.
//         3.2 Class Methods actionPerformed() & loadRowSize() Instance rowSizeSpinner
//             Assigned via Integer.valueOf() Instead of new Integer().
//         3.3 Copyright Update.
//
//-----------------------------------------------------------------
//                 danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *    The TableRowSelectionPreferencesPanel class provides the
 * ability to select the preferred table row size to be display
 * in the MyJSQLView TableTabPanel summary table.
 * 
 * @author Dana M. Proctor
 * @version 3.3 01/27/2011
 */

class TableRowSelectionPreferencesPanel extends JPanel implements ActionListener, ChangeListener
{
   // =============================================
   // Creation of the necessary class instance
   // variables for the JPanel.
   // =============================================

   private static final long serialVersionUID = -5133428503591922714L;

   private String tableName;
   private JSpinner rowSizeSpinner;
   private static final int defaultRowSize = 50;
   private JButton restoreDefaultsButton, applyButton;

   //==============================================================
   // TableFieldSelectionPreferencesPanel Constructor
   //==============================================================

   TableRowSelectionPreferencesPanel(String tableName, MyJSQLView_ResourceBundle resourceBundle)
   {
      this.tableName = tableName;

      // Class Instances
      JPanel mainPanel, southButtonPanel;
      JLabel rowSizeLabel;
      SpinnerNumberModel rowSizeSpinnerModel;

      final int minimumRowSize = 0;
      final int maxRowSize = 20000;
      final int spinnerRowSizeStep = 25;
      String resource;

      // Setting up
      setLayout(new BorderLayout());

      GridBagLayout gridbag = new GridBagLayout();
      GridBagConstraints constraints = new GridBagConstraints();

      // Setting up a label and textfield that will used
      // to select the desired table row size to be displayed.
      mainPanel = new JPanel(gridbag);
      mainPanel.setBorder(BorderFactory.createLoweredBevelBorder());

      resource = resourceBundle.getResource("TableRowSelectionPreferencesPanel.label.SummaryTableRowSize");
      if (resource.equals(""))
         rowSizeLabel = new JLabel("Summary Table Row Size");
      else
         rowSizeLabel = new JLabel(resource);

      buildConstraints(constraints, 0, 0, 1, 1, 100, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(rowSizeLabel, constraints);
      mainPanel.add(rowSizeLabel);

      rowSizeSpinnerModel = new SpinnerNumberModel(defaultRowSize, minimumRowSize, maxRowSize,
                                                   spinnerRowSizeStep);
      rowSizeSpinner = new JSpinner(rowSizeSpinnerModel);
      rowSizeSpinner.addChangeListener(this);

      buildConstraints(constraints, 1, 0, 1, 1, 100, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.WEST;
      gridbag.setConstraints(rowSizeSpinner, constraints);
      mainPanel.add(rowSizeSpinner);

      loadRowSize();

      add(mainPanel, BorderLayout.CENTER);

      // Buttons to set restore defaults or apply the changes
      // to the selected table summary view.

      southButtonPanel = new JPanel();
      southButtonPanel.setBorder(BorderFactory.createEmptyBorder());

      southButtonPanel = new JPanel();
      
      resource = resourceBundle.getResource("TableRowSelectionPreferencesPanel.button.RestoreDefaults");
      if (resource.equals(""))
         restoreDefaultsButton = new JButton("Restore Defaults");
      else
         restoreDefaultsButton = new JButton(resource);
      restoreDefaultsButton.addActionListener(this);
      southButtonPanel.add(restoreDefaultsButton);

      resource = resourceBundle.getResource("TableRowSelectionPreferencesPanel.button.Apply");
      if (resource.equals(""))
         applyButton = new JButton("Apply");
      else
         applyButton = new JButton(resource);
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

      if (panelSource instanceof JButton)
      {
         // Restore Defaults Button Action
         if (panelSource == restoreDefaultsButton)
         {
            rowSizeSpinner.setValue(Integer.valueOf(defaultRowSize));
            applyButton.setEnabled(true);
         }

         // Apply Button Action
         else if (panelSource == applyButton)
         {
            updatePreferences();
            applyButton.setEnabled(false);
         }
      }
   }

   //==============================================================
   // ActionEvent Listener method for determining when the selections
   // have been made so an update can be performed on the summary
   // table being displayed in the tab(s).
   //==============================================================

   public void stateChanged(ChangeEvent evt)
   {
      Object panelSource = evt.getSource();

      if (panelSource instanceof JSpinner && applyButton != null)
         applyButton.setEnabled(true);
   }
   
   //==============================================================
   // Class Method for helping the parameters in gridbag.
   //==============================================================

   private void buildConstraints(GridBagConstraints gbc, int gx, int gy,
                                 int gw, int gh, double wx, double wy)
   {
      gbc.gridx = gx;
      gbc.gridy = gy;
      gbc.gridwidth = gw;
      gbc.gridheight = gh;
      gbc.weightx = wx;
      gbc.weighty = wy;
   }

   //==============================================================
   // Class method to load the current users fields preferences.
   //==============================================================

   private void loadRowSize()
   {
      // Method Instances
      TableTabPanel summaryTableTab;
      int rowSize;

      // Loading the current row size and setting
      // in the spinner.
      summaryTableTab = (TableTabPanel) DBTablesPanel.getTableTabPanel(tableName);
      
      if (summaryTableTab != null)
         rowSize = summaryTableTab.getTableRowSize();
      else
         rowSize = defaultRowSize;
      
      rowSizeSpinner.setValue(Integer.valueOf(rowSize));
   }

   //==============================================================
   // Class method to allow the setting of TableTabPanel preferences
   // that will be used to view the summary table of data.
   //==============================================================

   protected void updatePreferences()
   {
      if (applyButton.isEnabled())
      {
         DBTablesPanel.startStatusTimer();
         
         Thread updateTableTabPanelRowsThread = new Thread(new Runnable()
         {
            public void run()
            {
               // Instances
               TableTabPanel summaryTableTab;
               int rowSize;

               // Setting the new row size preferences and calling main class
               // to redisplay table tab.
                  
               rowSize = Integer.parseInt(rowSizeSpinner.getValue().toString());
               summaryTableTab = DBTablesPanel.getTableTabPanel(tableName);
               summaryTableTab.setTableRowSize(rowSize);
               DBTablesPanel.setSelectedTableTabPanel(tableName);
               
               DBTablesPanel.stopStatusTimer();
            }
         }, "TableRowSelectionPreferencesPanel.updateTableTabPanelRowsThread");
         updateTableTabPanelRowsThread.start();
      }
   }
}