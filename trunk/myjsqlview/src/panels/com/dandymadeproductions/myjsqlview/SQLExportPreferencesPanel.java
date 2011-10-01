//=================================================================
//       MyJSQLView SQLExportPreferencesPanel
//=================================================================
//
// 	This class provides a generic panel in the appearance of
// a form for selecting the data SQL export options.
//
//            << SQLExportPreferencesPanel.java >>
//
//=================================================================
// Copyright (C) 2007-2011 Dana M. Proctor
// Version 4.2 10/01/2011
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
// Version 1.0 04/28/2007 Original SQLExportPreferencesPanel.
//         1.1 04/30/2007 Basic Componenet Layout Completion.
//         1.2 05/01/2007 Clean Up Constructor and Began Implementing Action
//                        Events.
//         1.3 05/02/2007 Cleaned Up Some More. Completed Action
//                        Events and Implemented setSQLExportOptions().
//         1.4 05/02/2007 Added Class Method getSQLExportOptions().
//         1.5 05/14/2007 Class Method getSQLExportOptions() Get
//                        delimiter from MyJSQLView.getdelimiter().
//         1.6 05/18/2007 Added Update Options. Updated As Needed.
//                        Class Methods createInsert/Replace/Update
//                        OptionsPanels().
//         1.7 05/24/2007 Class Instance timeStampCheckBox Name Change
//                        to "Timestamp: NOW()".
//         1.8 06/01/2007 Class Instances insert/replaceExplicitCheckBox.
//         1.9 06/03/2007 Class SQLDataExportProperties Renamed to
//                        DataExportProperties.
//         2.0 06/04/2007 Modified the Way DataExportProperties Are
//                        Handled. MyJSQLView.getDataExportProperties().
//         2.1 08/08/2007 Code Cleanup.
//         2.2 09/09/2007 Removed Class Instance insertTypeButtonGroup
//                        in Class Method createInsertOptionsPanel().
//         2.3 12/12/2007 Header Update.
//         2.4 12/15/2007 Disabled insertExplicitCheckBox, insertTypeCheckBox,
//                        updateTypeCheckBox, and Removed Replace from
//                        insertReplaceUpdateComboBox for PostgreSQL
//                        Database Connection.
//         2.5 12/23/2007 Added Class Instance identifierQuoteTextField and
//                        Placement in dataPanel. Also Handling Mechanism.
//                        MyJSQLView.getDataExportProperties().
//         2.6 12/25/2007 Modifications to the Data Export Panel, Identifier
//                        Quote TextField & Label.
//         2.7 01/25/2008 Comment Changes.
//         2.8 02/07/2008 Replaced insertExplicitCheckBox With
//						  insertExpressionComboBox.
//         2.9 02/09/2008 Replaced replaceExplicitCheckBox With
//                        replaceExpressionComboBox.
//         3.0 03/10/2008 Added Method Instances expressionTypePanel to Method
//                        createInsertOptionsPanel() & createReplaceOptionsPanel().
//                        ReOrdered insert/replaceExpressionComboBoxes. Tightened
//                        Components.
//         3.1 05/12/2008 Added Class Instance serialVersionUID.
//         3.2 05/23/2008 Set Class Fields autoIncrementCheckBox &
//                        timeStampCheckBox at Creation to UnChecked.
//                        Same for Default Settings in actionPerformed().
//         3.3 08/21/2008 Changed autoIncrementCheckBox Label Auto-Increment:NULL
//                        to Auto-Increment: SEQ in Class Method
//                        createInsertOptionsPanel()
//         3.4 10/21/2008 MyJSQLView Project Common Source Code Formatting.
//         3.5 10/30/2008 Constructor, Class Methods actionPerformed() &
//                        dataExportProperties() getDataExportProperites
//                        Changed Over to the MyJSQLView_Frame Class.
//         3.6 11/12/2008 Changed MyJSQLView_Frame.getDatabaseExportProperties()
//                        Method Moved Over to the DBTablesPanel.
//         3.7 05/27/2009 Header Format Changes/Update.
//         3.8 02/18/2010 Changed Package to Reflect Dandy Made Productions Code.
//         3.9 03/09/2010 Implementation of Internationalization via Instance
//                        resourceBundle. Added Arguments to Constructor. Modified
//                        Methods createInsertOptionPanel(), createReplaceOptionPanel(),
//                        & createUpdateOptionPanel().
//         4.0 01/26/2011 Added Class Instance subProtocol That Is Intialized in
//                        Constructor By Obtaining Contents From New ConnectionManager
//                        Class. Class Method actionPerformed() identifierQuoteString
//                        Also Obtained From ConnectionManager.
//         4.1 06/11/2011 Replaced Class Instance subProtocol With dataSourceType.
//                        Constructor and Class Methods createInsert/UpdateOptionsPanel()
//                        Effected.
//         4.2 10/01/2011 Added Static Final Default and Other Common Class Instances. Used
//                        to Setup Instances and Restoring Defaults in actionPerformed().
//
//-----------------------------------------------------------------
//                 danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 *    The SQLExportPreferencesPanel class provides a generic panel
 * in the appearance of a form for selecting the data SQL export
 * options.
 * 
 * @author Dana M. Proctor
 * @version 4.2 10/01/2011
 */

class SQLExportPreferencesPanel extends JPanel implements ActionListener
{
   // Class Instances.
   private static final long serialVersionUID = -2687714755497016988L;

   private JPanel dataContentOptionsPanel;
   private CardLayout dataOptionsCardLayout;

   private JCheckBox tableStructureCheckBox, tableDataCheckBox;
   private JComboBox insertExpressionComboBox, replaceExpressionComboBox;
   private JCheckBox insertLockTableCheckBox, insertTypeCheckBox;
   private JCheckBox replaceLockTableCheckBox, replaceTypeCheckBox;
   private JCheckBox updateLockTableCheckBox, updateTypeCheckBox;
   private JCheckBox autoIncrementCheckBox, timeStampCheckBox;
   private JComboBox insertReplaceUpdateComboBox;
   private JComboBox insertTypeComboBox, replaceTypeComboBox, updateTypeComboBox;
   private JTextField identifierQuoteTextField;

   private JButton restoreDefaultsButton, applyButton;
   private String dataSourceType;
   
   protected static final boolean DEFAULT_TABLE_STRUCTURE = false;
   protected static final boolean DEFAULT_TABLE_DATA = true;
   protected static final boolean DEFAULT_INSERT_LOCK = true;
   protected static final boolean DEFAULT_INSERT_TYPE = false;
   protected static final boolean DEFAULT_REPLACE_LOCK = true;
   protected static final boolean DEFAULT_REPLACE_TYPE = false;
   protected static final boolean DEFAULT_UPDATE_LOCK = true;
   protected static final boolean DEFAULT_UPDATE_TYPE = false;
   protected static final boolean DEFAULT_AUTO_INCREMENT = false;
   protected static final boolean DEFAULT_TIMESTAMP = false;
   
   protected static final String EXPRESSION_SINGULAR = "Singular";
   protected static final String EXPRESSION_PLURAL = "Plural";
   protected static final String EXPRESSION_EXPLICIT = "Explicit";
   
   protected static final String TYPE_INSERT = "Insert";
   protected static final String TYPE_REPLACE = "Replace";
   protected static final String TYPE_UPDATE = "Update";
   
   protected static final String PRIORITY_LOW = "Low_Priority";
   protected static final String PRIORITY_DELAYED = "Delayed";
   protected static final String PRIORITY_IGNORE = "Ignore";

   //==============================================================
   // DataPreferencesPreferencesDialog Constructor
   //==============================================================

   protected SQLExportPreferencesPanel(MyJSQLView_ResourceBundle resourceBundle)
   {
      // Class Instances
      JPanel structurePanel, warningPanel;
      JPanel dataPanel;
      JPanel identifierQuotePanel;
      JPanel sqlExportPanel, dataContentPanel;
      JPanel insertOptionsPanel, replaceOptionsPanel, updateOptionsPanel;

      JPanel buttonPanel;
      JLabel warningLabel, tableStructureWarningLabel, identifierQuoteLabel;
      
      String resource;
      
      // Setting up the panel's main panel and other needed
      // instances.
      setLayout(new BorderLayout());

      GridBagLayout gridbag = new GridBagLayout();
      GridBagConstraints constraints = new GridBagConstraints();
      
      dataSourceType = ConnectionManager.getDataSourceType();

      // SQL Main Panel & Components
      sqlExportPanel = new JPanel(new BorderLayout());
      sqlExportPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(1,0,0,0),
                                                                  BorderFactory.createLoweredBevelBorder()));

      // ==================================================
      // Top structure panel.

      structurePanel = new JPanel(new GridLayout(2, 1, 0, 0));
      structurePanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(1,1,1,1),
                                                                  BorderFactory.createEtchedBorder()));

      resource = resourceBundle.getResource("SQLExportPreferencesPanel.checkbox.TableStructure");
      if (resource.equals(""))
         tableStructureCheckBox = new JCheckBox("Table Structure", DEFAULT_TABLE_STRUCTURE);
      else
         tableStructureCheckBox = new JCheckBox(resource, DEFAULT_TABLE_STRUCTURE);
      tableStructureCheckBox.setFocusPainted(false);
      tableStructureCheckBox.addActionListener(this);
      structurePanel.add(tableStructureCheckBox);

      warningPanel = new JPanel();
      warningPanel.setBorder(BorderFactory.createEmptyBorder());

      resource = resourceBundle.getResource("SQLExportPreferencesPanel.label.Warning");
      if (resource.equals(""))
         warningLabel = new JLabel(" Warning! ", JLabel.LEADING);
      else
         warningLabel = new JLabel(" " + resource + "! ", JLabel.LEADING);
      warningLabel.setBorder(BorderFactory.createEmptyBorder());
      warningLabel.setForeground(Color.RED);
      warningPanel.add(warningLabel);

      resource = resourceBundle.getResource("SQLExportPreferencesPanel.label.DropExistingTable");
      if (resource.equals(""))
         tableStructureWarningLabel = new JLabel("Inserts SQL statement DROP EXISTING TABLE.");
      else
         tableStructureWarningLabel = new JLabel(resource);
      tableStructureWarningLabel.setBorder(BorderFactory.createEmptyBorder());
      warningPanel.add(tableStructureWarningLabel);

      structurePanel.add(warningPanel);

      sqlExportPanel.add(structurePanel, BorderLayout.NORTH);

      // =====================================================
      // Center data panel and components.

      dataPanel = new JPanel();
      dataPanel.setLayout(gridbag);
      dataPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(0,1,1,1),
                                                             BorderFactory.createEtchedBorder()));

      // Data CheckBox
      resource = resourceBundle.getResource("SQLExportPreferencesPanel.checkbox.TableData");
      if (resource.equals(""))
         tableDataCheckBox = new JCheckBox("Table Data", DEFAULT_TABLE_DATA);
      else
         tableDataCheckBox = new JCheckBox(resource, DEFAULT_TABLE_DATA);
      tableDataCheckBox.setFocusPainted(false);
      tableDataCheckBox.addActionListener(this);

      buildConstraints(constraints, 0, 0, 1, 1, 100, 18);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.WEST;
      gridbag.setConstraints(tableDataCheckBox, constraints);
      dataPanel.add(tableDataCheckBox);

      // Identifier Quote
      identifierQuotePanel = new JPanel(gridbag);
      identifierQuotePanel.setBorder(BorderFactory.createEmptyBorder(1, 0, 0, 0));

      identifierQuoteTextField = new JTextField(3);
      identifierQuoteTextField.addActionListener(this);

      buildConstraints(constraints, 0, 0, 1, 1, 30, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.WEST;
      gridbag.setConstraints(identifierQuoteTextField, constraints);
      identifierQuotePanel.add(identifierQuoteTextField);

      resource = resourceBundle.getResource("SQLExportPreferencesPanel.label.IdentifierQuoteString");
      if (resource.equals(""))
         identifierQuoteLabel = new JLabel(" Identifier Quote String");
      else
         identifierQuoteLabel = new JLabel(" " + resource);

      buildConstraints(constraints, 1, 0, 1, 1, 70, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(identifierQuoteLabel, constraints);
      identifierQuotePanel.add(identifierQuoteLabel);

      buildConstraints(constraints, 1, 0, 1, 1, 100, 0);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(identifierQuotePanel, constraints);
      dataPanel.add(identifierQuotePanel);

      dataContentPanel = new JPanel();

      // Insert/Replace ComboBox
      insertReplaceUpdateComboBox = new JComboBox();
      insertReplaceUpdateComboBox.addItem(TYPE_INSERT);
      if (!dataSourceType.equals(ConnectionManager.POSTGRESQL)
            && !dataSourceType.equals(ConnectionManager.MSACCESS))
         insertReplaceUpdateComboBox.addItem(TYPE_REPLACE);
      insertReplaceUpdateComboBox.addItem(TYPE_UPDATE);
      insertReplaceUpdateComboBox.addActionListener(this);
      dataContentPanel.add(insertReplaceUpdateComboBox);

      // Insert/Replace/Update Content Pane
      dataContentOptionsPanel = new JPanel(dataOptionsCardLayout = new CardLayout());
      dataContentOptionsPanel.setBorder(BorderFactory.createLoweredBevelBorder());

      // ===========================
      // Insert

      insertOptionsPanel = new JPanel();
      insertOptionsPanel.setLayout(gridbag);
      createInsertOptionsPanel(insertOptionsPanel, gridbag, constraints, resourceBundle);
      dataContentOptionsPanel.add(insertOptionsPanel, "Insert");

      // =================================
      // Replace

      replaceOptionsPanel = new JPanel();
      replaceOptionsPanel.setLayout(gridbag);
      createReplaceOptionsPanel(replaceOptionsPanel, gridbag, constraints, resourceBundle);
      dataContentOptionsPanel.add(replaceOptionsPanel, "Replace");

      // =================================
      // Update

      updateOptionsPanel = new JPanel();
      updateOptionsPanel.setLayout(gridbag);
      createUpdateOptionsPanel(updateOptionsPanel, gridbag, constraints, resourceBundle);
      dataContentOptionsPanel.add(updateOptionsPanel, "Update");

      /*
       * Is this the end of this mess yet! Well not quite but almost. I once had
       * a person ask me if I had hand built these GUIs. I replied yes. The
       * person then wondered if it could be done with a tool, since it
       * looked like a lot of typing. I indicated well I suppose so, and I'm
       * sure there is a tool to do it. What I did not tell the person though is
       * that the code generated by one of these tools turns out even worst than
       * this. Good luck following it. I suppose soon though it will not matter
       * and all code will be never be looked at by the human eye.
       * 
       * Well that day has arrived I've tried NetBeans, I'll stick to still
       * hand coding. No matter you are still going to need to get into the
       * code to advance the logic.
       */

      dataContentPanel.add(dataContentOptionsPanel);

      buildConstraints(constraints, 0, 1, 2, 1, 100, 82);
      constraints.fill = GridBagConstraints.BOTH;
      constraints.anchor = GridBagConstraints.WEST;
      gridbag.setConstraints(dataContentPanel, constraints);
      dataPanel.add(dataContentPanel);

      sqlExportPanel.add(dataPanel, BorderLayout.CENTER);
      add(sqlExportPanel, BorderLayout.CENTER);

      // Retrieve existing state and set accordingly.
      setSQLExportOptions(DBTablesPanel.getDataExportProperties());

      // =====================================================
      // Button Action Options Panel South

      buttonPanel = new JPanel();
      
      resource = resourceBundle.getResource("SQLExportPreferencesPanel.button.RestoreDefaults");
      if (resource.equals(""))
         restoreDefaultsButton = new JButton("Restore Defaults");
      else
         restoreDefaultsButton = new JButton(resource);
      restoreDefaultsButton.addActionListener(this);
      buttonPanel.add(restoreDefaultsButton);

      resource = resourceBundle.getResource("SQLExportPreferencesPanel.button.Apply");
      if (resource.equals(""))
         applyButton = new JButton("Apply");
      else
         applyButton = new JButton(resource);
      applyButton.setEnabled(false);
      applyButton.addActionListener(this);
      buttonPanel.add(applyButton);

      add(buttonPanel, BorderLayout.SOUTH);
   }

   //==============================================================
   // ActionEvent Listener method for detecting the inputs
   // from the panel and directing to the appropriate routine.
   //==============================================================

   public void actionPerformed(ActionEvent evt)
   {
      Object formSource = evt.getSource();

      // ==================================
      // Button actions.

      if (formSource instanceof JButton)
      {
         // Restore Defaults Content Settings
         if (formSource == restoreDefaultsButton)
         {
            tableStructureCheckBox.setSelected(DEFAULT_TABLE_STRUCTURE);
            tableDataCheckBox.setSelected(DEFAULT_TABLE_DATA);
            identifierQuoteTextField.setText(ConnectionManager.getIdentifierQuoteString());
            insertLockTableCheckBox.setSelected(DEFAULT_INSERT_LOCK);
            replaceLockTableCheckBox.setSelected(DEFAULT_REPLACE_LOCK);
            updateLockTableCheckBox.setSelected(DEFAULT_UPDATE_LOCK);
            autoIncrementCheckBox.setSelected(DEFAULT_AUTO_INCREMENT);
            timeStampCheckBox.setSelected(DEFAULT_TIMESTAMP);
            if (dataSourceType.equals(ConnectionManager.MYSQL) ||
                dataSourceType.equals(ConnectionManager.POSTGRESQL))
            {
               insertExpressionComboBox.setSelectedItem(EXPRESSION_PLURAL);
               replaceExpressionComboBox.setSelectedItem(EXPRESSION_PLURAL);
            }
            else
            {
               insertExpressionComboBox.setSelectedItem(EXPRESSION_SINGULAR);
               replaceExpressionComboBox.setSelectedItem(EXPRESSION_SINGULAR);
            }
            insertTypeCheckBox.setSelected(DEFAULT_INSERT_TYPE);
            replaceTypeCheckBox.setSelected(DEFAULT_REPLACE_TYPE);
            updateTypeCheckBox.setSelected(DEFAULT_UPDATE_TYPE);
            insertReplaceUpdateComboBox.setSelectedItem(TYPE_INSERT);
            insertReplaceUpdateComboBox.setEnabled(true);
            insertTypeComboBox.setSelectedItem(PRIORITY_LOW);
            insertTypeComboBox.setEnabled(false);
            replaceTypeComboBox.setSelectedItem(PRIORITY_LOW);
            replaceTypeComboBox.setEnabled(false);
            updateTypeComboBox.setSelectedItem(PRIORITY_LOW);
            updateTypeComboBox.setEnabled(false);

            applyButton.setEnabled(true);
         }

         // Setting the options that have been selected
         // to be used during an export.
         else if (formSource == applyButton)
         {
            DBTablesPanel.setDataExportProperties(getSQLExportOptions());
            applyButton.setEnabled(false);
         }
      }

      // ============================================
      // CheckBox Actions.

      if (formSource instanceof JCheckBox && applyButton != null)
      {
         // If table structure is enabled then a drop
         // table will be inserted so, therefore no
         // replaces can take place.
         if (formSource == tableStructureCheckBox)
         {
            if (tableStructureCheckBox.isSelected())
            {
               insertReplaceUpdateComboBox.setSelectedItem(TYPE_INSERT);
               insertReplaceUpdateComboBox.setEnabled(false);
            }
            else
               insertReplaceUpdateComboBox.setEnabled(true);
         }

         // Type options from combobox only available
         // when insert type selected.
         if (formSource == insertTypeCheckBox)
         {
            if (insertTypeCheckBox.isSelected())
               insertTypeComboBox.setEnabled(true);
            else
               insertTypeComboBox.setEnabled(false);
         }

         // Type options from combobox only available
         // when replace type selected.
         if (formSource == replaceTypeCheckBox)
         {
            if (replaceTypeCheckBox.isSelected())
               replaceTypeComboBox.setEnabled(true);
            else
               replaceTypeComboBox.setEnabled(false);
         }

         // Type options from combobox only available
         // when update type selected.
         if (formSource == updateTypeCheckBox)
         {
            if (updateTypeCheckBox.isSelected())
               updateTypeComboBox.setEnabled(true);
            else
               updateTypeComboBox.setEnabled(false);
         }

         // Something changed so let it be applied.
         applyButton.setEnabled(true);
      }

      // ============================================
      // TextField Actions.

      if (formSource instanceof JTextField && applyButton != null)
      {
         // Identifier Quote String Changed so let it be applied.
         applyButton.setEnabled(true);
      }

      // ============================================
      // ComboBox Actions.

      if (formSource instanceof JComboBox && applyButton != null)
      {
         // Show the appropriate panel for data options
         // insert, replace, or update.
         if (formSource == insertReplaceUpdateComboBox)
            dataOptionsCardLayout.show(dataContentOptionsPanel,
                                       (String) insertReplaceUpdateComboBox.getSelectedItem());

         // A delayed insert or replace can not be locked
         // so uncheck the appropriate lock table checkbox.
         if (formSource == insertTypeComboBox)
         {
            if (insertTypeComboBox.getSelectedItem().equals(PRIORITY_DELAYED))
               insertLockTableCheckBox.setSelected(false);
         }

         if (formSource == replaceTypeComboBox)
         {
            if (replaceTypeComboBox.getSelectedItem().equals(PRIORITY_DELAYED))
               replaceLockTableCheckBox.setSelected(false);
         }

         // Something changed so let it be applied.
         applyButton.setEnabled(true);
      }
   }

   //==============================================================
   // Class Method to create the Insert Options Panel.
   //==============================================================

   private void createInsertOptionsPanel(JPanel insertOptionsPanel,
                                         GridBagLayout gridbag, GridBagConstraints constraints,
                                         MyJSQLView_ResourceBundle resourceBundle)
   {
      // Class Method Instances.
      JPanel expressionTypePanel, insertTypePanel;
      String resource;
      
      // Setup.
      expressionTypePanel = new JPanel();
      expressionTypePanel.setBorder(BorderFactory.createEmptyBorder());

      // Insert Explicit ComboBox
      insertExpressionComboBox = new JComboBox();
      insertExpressionComboBox.addItem(EXPRESSION_SINGULAR);
      insertExpressionComboBox.addItem(EXPRESSION_PLURAL);
      insertExpressionComboBox.addItem(EXPRESSION_EXPLICIT);
      insertExpressionComboBox.addActionListener(this);
      expressionTypePanel.add(insertExpressionComboBox);

      buildConstraints(constraints, 1, 0, 1, 1, 100, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.WEST;
      gridbag.setConstraints(expressionTypePanel, constraints);
      insertOptionsPanel.add(expressionTypePanel);

      // Insert Lock Table CheckBox
      resource = resourceBundle.getResource("SQLExportPreferencesPanel.checkbox.LockTable");
      if (resource.equals(""))
         insertLockTableCheckBox = new JCheckBox("Lock Table", DEFAULT_INSERT_LOCK);
      else
         insertLockTableCheckBox = new JCheckBox(resource, DEFAULT_INSERT_LOCK);
      insertLockTableCheckBox.setFocusPainted(false);
      insertLockTableCheckBox.addActionListener(this);

      buildConstraints(constraints, 1, 1, 1, 1, 100, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.WEST;
      gridbag.setConstraints(insertLockTableCheckBox, constraints);
      insertOptionsPanel.add(insertLockTableCheckBox);

      // Auto-Increment CheckBox
      resource = resourceBundle.getResource("SQLExportPreferencesPanel.checkbox.Auto-Increment");
      if (resource.equals(""))
         autoIncrementCheckBox = new JCheckBox("Auto-Increment: SEQ", DEFAULT_AUTO_INCREMENT);
      else
         autoIncrementCheckBox = new JCheckBox(resource + ": SEQ", DEFAULT_AUTO_INCREMENT);
      autoIncrementCheckBox.setFocusPainted(false);
      autoIncrementCheckBox.addActionListener(this);

      buildConstraints(constraints, 1, 2, 1, 1, 100, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.WEST;
      gridbag.setConstraints(autoIncrementCheckBox, constraints);
      insertOptionsPanel.add(autoIncrementCheckBox);

      // TimeStamp CheckBox
      resource = resourceBundle.getResource("SQLExportPreferencesPanel.checkbox.Timestamp");
      if (resource.equals(""))
         timeStampCheckBox = new JCheckBox("Timestamp: NOW( )", DEFAULT_TIMESTAMP);
      else
         timeStampCheckBox = new JCheckBox(resource + ": NOW( )", DEFAULT_TIMESTAMP);
      timeStampCheckBox.setFocusPainted(false);
      timeStampCheckBox.addActionListener(this);

      buildConstraints(constraints, 1, 3, 1, 1, 100, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.WEST;
      gridbag.setConstraints(timeStampCheckBox, constraints);
      insertOptionsPanel.add(timeStampCheckBox);

      // Insert Type CheckBox
      resource = resourceBundle.getResource("SQLExportPreferencesPanel.checkbox.Type");
      if (resource.equals(""))
         insertTypeCheckBox = new JCheckBox("Type", DEFAULT_INSERT_TYPE);
      else
         insertTypeCheckBox = new JCheckBox(resource, DEFAULT_INSERT_TYPE);
      insertTypeCheckBox.setFocusPainted(false);
      insertTypeCheckBox.addActionListener(this);

      if (dataSourceType.equals(ConnectionManager.POSTGRESQL))
         insertTypeCheckBox.setEnabled(false);

      buildConstraints(constraints, 1, 4, 1, 1, 100, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.WEST;
      gridbag.setConstraints(insertTypeCheckBox, constraints);
      insertOptionsPanel.add(insertTypeCheckBox);

      insertTypePanel = new JPanel();
      insertTypePanel.setBorder(BorderFactory.createEmptyBorder());

      // Insert Type Options
      insertTypeComboBox = new JComboBox();
      insertTypeComboBox.addItem(PRIORITY_LOW);
      insertTypeComboBox.addItem(PRIORITY_DELAYED);
      insertTypeComboBox.addItem(PRIORITY_IGNORE);
      insertTypeComboBox.setEnabled(false);
      insertTypeComboBox.addActionListener(this);
      insertTypePanel.add(insertTypeComboBox);

      buildConstraints(constraints, 0, 5, 2, 1, 100, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(insertTypePanel, constraints);
      insertOptionsPanel.add(insertTypePanel);
   }

   //==============================================================
   // Class Method to create the Replace Options Panel.
   //==============================================================

   private void createReplaceOptionsPanel(JPanel replaceOptionsPanel,
                                          GridBagLayout gridbag, GridBagConstraints constraints,
                                          MyJSQLView_ResourceBundle resourceBundle)
   {
      // Class Method Instances.
      JPanel expressionTypePanel, replaceTypePanel;
      String resource;
      
      // Setup
      expressionTypePanel = new JPanel();
      expressionTypePanel.setBorder(BorderFactory.createEmptyBorder());

      // Replace Explicit ComboBox
      replaceExpressionComboBox = new JComboBox();
      replaceExpressionComboBox.addItem(EXPRESSION_SINGULAR);
      replaceExpressionComboBox.addItem(EXPRESSION_PLURAL);
      replaceExpressionComboBox.addItem(EXPRESSION_EXPLICIT);
      replaceExpressionComboBox.addActionListener(this);
      expressionTypePanel.add(replaceExpressionComboBox);

      buildConstraints(constraints, 1, 0, 1, 1, 100, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.WEST;
      gridbag.setConstraints(expressionTypePanel, constraints);
      replaceOptionsPanel.add(expressionTypePanel);

      // Replace Lock Table CheckBox
      resource = resourceBundle.getResource("SQLExportPreferencesPanel.checkbox.LockTable");
      if (resource.equals(""))
         replaceLockTableCheckBox = new JCheckBox("Lock Table", DEFAULT_REPLACE_LOCK);
      else
         replaceLockTableCheckBox = new JCheckBox(resource, DEFAULT_REPLACE_LOCK);
      replaceLockTableCheckBox.setFocusPainted(false);
      replaceLockTableCheckBox.addActionListener(this);

      buildConstraints(constraints, 1, 1, 1, 1, 100, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.WEST;
      gridbag.setConstraints(replaceLockTableCheckBox, constraints);
      replaceOptionsPanel.add(replaceLockTableCheckBox);

      // Replace Type CheckBox
      resource = resourceBundle.getResource("SQLExportPreferencesPanel.checkbox.Type");
      if (resource.equals(""))
         replaceTypeCheckBox = new JCheckBox("Type", DEFAULT_REPLACE_TYPE);
      else
         replaceTypeCheckBox = new JCheckBox(resource, DEFAULT_REPLACE_TYPE);
      replaceTypeCheckBox.setFocusPainted(false);
      replaceTypeCheckBox.addActionListener(this);

      buildConstraints(constraints, 1, 2, 1, 1, 100, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.WEST;
      gridbag.setConstraints(replaceTypeCheckBox, constraints);
      replaceOptionsPanel.add(replaceTypeCheckBox);

      replaceTypePanel = new JPanel();
      replaceTypePanel.setBorder(BorderFactory.createEmptyBorder());

      // Replace Type Options
      replaceTypeComboBox = new JComboBox();
      replaceTypeComboBox.addItem(PRIORITY_LOW);
      replaceTypeComboBox.addItem(PRIORITY_DELAYED);
      replaceTypeComboBox.setEnabled(false);
      replaceTypeComboBox.addActionListener(this);
      replaceTypePanel.add(replaceTypeComboBox);

      buildConstraints(constraints, 0, 3, 2, 1, 100, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(replaceTypePanel, constraints);
      replaceOptionsPanel.add(replaceTypePanel);
   }

   //==============================================================
   // Class Method to create the Update Options Panel.
   //==============================================================

   private void createUpdateOptionsPanel(JPanel updateOptionsPanel,
                                         GridBagLayout gridbag, GridBagConstraints constraints,
                                         MyJSQLView_ResourceBundle resourceBundle)
   {
      // Class Method Instances.
      JPanel updateTypePanel;
      String resource;

      // UpdateLock Table CheckBox
      resource = resourceBundle.getResource("SQLExportPreferencesPanel.checkbox.LockTable");
      if (resource.equals(""))
         updateLockTableCheckBox = new JCheckBox("Lock Table", DEFAULT_UPDATE_LOCK);
      else
         updateLockTableCheckBox = new JCheckBox(resource, DEFAULT_UPDATE_LOCK);
      updateLockTableCheckBox.setFocusPainted(false);
      updateLockTableCheckBox.addActionListener(this);

      buildConstraints(constraints, 1, 0, 1, 1, 100, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.WEST;
      gridbag.setConstraints(updateLockTableCheckBox, constraints);
      updateOptionsPanel.add(updateLockTableCheckBox);

      // Type CheckBox
      resource = resourceBundle.getResource("SQLExportPreferencesPanel.checkbox.Type");
      if (resource.equals(""))
         updateTypeCheckBox = new JCheckBox("Type", DEFAULT_UPDATE_TYPE);
      else
         updateTypeCheckBox = new JCheckBox(resource, DEFAULT_UPDATE_TYPE);
      updateTypeCheckBox.setFocusPainted(false);
      updateTypeCheckBox.addActionListener(this);

      if (dataSourceType.equals(ConnectionManager.POSTGRESQL))
         updateTypeCheckBox.setEnabled(false);

      buildConstraints(constraints, 1, 1, 1, 1, 100, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.WEST;
      gridbag.setConstraints(updateTypeCheckBox, constraints);
      updateOptionsPanel.add(updateTypeCheckBox);

      updateTypePanel = new JPanel();

      // Type Options
      updateTypeComboBox = new JComboBox();
      updateTypeComboBox.addItem(PRIORITY_LOW);
      updateTypeComboBox.addItem(PRIORITY_IGNORE);
      updateTypeComboBox.setEnabled(false);
      updateTypeComboBox.addActionListener(this);
      updateTypePanel.add(updateTypeComboBox);

      buildConstraints(constraints, 0, 2, 2, 1, 100, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(updateTypePanel, constraints);
      updateOptionsPanel.add(updateTypePanel);
   }

   //==============================================================
   // Class Method for helping the parameters in gridbag.
   //==============================================================

   private void buildConstraints(GridBagConstraints gbc, int gx, int gy, int gw,
                                 int gh, double wx, double wy)
   {
      gbc.gridx = gx;
      gbc.gridy = gy;
      gbc.gridwidth = gw;
      gbc.gridheight = gh;
      gbc.weightx = wx;
      gbc.weighty = wy;
   }

   //==============================================================
   // Class method to get the panel's components/data
   // properties.
   //==============================================================

   protected DataExportProperties getSQLExportOptions()
   {
      DataExportProperties newDataProperties = DBTablesPanel.getDataExportProperties();

      newDataProperties.setTableStructure(tableStructureCheckBox.isSelected());
      newDataProperties.setTableData(tableDataCheckBox.isSelected());
      newDataProperties.setIdentifierQuoteString(identifierQuoteTextField.getText());

      newDataProperties.setInsertLock(insertLockTableCheckBox.isSelected());
      newDataProperties.setReplaceLock(replaceLockTableCheckBox.isSelected());
      newDataProperties.setUpdateLock(updateLockTableCheckBox.isSelected());

      newDataProperties.setInsertExpression((String) insertExpressionComboBox.getSelectedItem());
      newDataProperties.setReplaceExpression((String) replaceExpressionComboBox.getSelectedItem());

      newDataProperties.setAutoIncrement(autoIncrementCheckBox.isSelected());
      newDataProperties.setTimeStamp(timeStampCheckBox.isSelected());

      newDataProperties.setInsertType(insertTypeCheckBox.isSelected());
      newDataProperties.setReplaceType(replaceTypeCheckBox.isSelected());
      newDataProperties.setUpdateType(updateTypeCheckBox.isSelected());

      newDataProperties.setInsertReplaceUpdate((String) insertReplaceUpdateComboBox.getSelectedItem());

      newDataProperties.setInsertTypeSetting((String) insertTypeComboBox.getSelectedItem());
      newDataProperties.setReplaceTypeSetting((String) replaceTypeComboBox.getSelectedItem());
      newDataProperties.setUpdateTypeSetting((String) updateTypeComboBox.getSelectedItem());

      return newDataProperties;
   }

   //==============================================================
   // Class method to set the panel's components for display.
   //==============================================================

   protected void setSQLExportOptions(DataExportProperties dataProperties)
   {
      tableStructureCheckBox.setSelected(dataProperties.getTableStructure());
      insertReplaceUpdateComboBox.setEnabled(!tableStructureCheckBox.isSelected());

      tableDataCheckBox.setSelected(dataProperties.getTableData());
      identifierQuoteTextField.setText(dataProperties.getIdentifierQuoteString());

      insertLockTableCheckBox.setSelected(dataProperties.getInsertLock());
      replaceLockTableCheckBox.setSelected(dataProperties.getReplaceLock());
      updateLockTableCheckBox.setSelected(dataProperties.getUpdateLock());

      insertExpressionComboBox.setSelectedItem(dataProperties.getInsertExpression());
      replaceExpressionComboBox.setSelectedItem(dataProperties.getReplaceExpression());

      autoIncrementCheckBox.setSelected(dataProperties.getAutoIncrement());
      timeStampCheckBox.setSelected(dataProperties.getTimeStamp());

      insertTypeCheckBox.setSelected(dataProperties.getInsertType());
      replaceTypeCheckBox.setSelected(dataProperties.getReplaceType());
      updateTypeCheckBox.setSelected(dataProperties.getUpdateType());

      insertReplaceUpdateComboBox.setSelectedItem(dataProperties.getInsertReplaceUpdate());
      dataOptionsCardLayout.show(dataContentOptionsPanel,
                                 (String) insertReplaceUpdateComboBox.getSelectedItem());

      insertTypeComboBox.setSelectedItem(dataProperties.getInsertTypeSetting());
      insertTypeComboBox.setEnabled(insertTypeCheckBox.isSelected());

      replaceTypeComboBox.setSelectedItem(dataProperties.getReplaceTypeSetting());
      replaceTypeComboBox.setEnabled(replaceTypeCheckBox.isSelected());

      updateTypeComboBox.setSelectedItem(dataProperties.getUpdateTypeSetting());
      updateTypeComboBox.setEnabled(updateTypeCheckBox.isSelected());
   }
}