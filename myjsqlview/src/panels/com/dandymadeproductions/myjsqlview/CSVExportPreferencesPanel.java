//=================================================================
//       MyJSQLView CSVExportPreferencesPanel
//=================================================================
//
// 	This class provides a generic panel in the appearance of
// a form for selecting the CSV data export options.
//
//             << CSVExportPreferencesPanel.java >>
//
//=================================================================
// Copyright (C) 2005-2010 Dana M. Proctor
// Version 4.8 06/13/2010
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
// Version 1.0 03/20/2006 Original DataDeliminatorDialog Class.
//         1.1 03/21/2006 Corrected Spelling commaRadioButton Label.
//         1.2 05/29/2006 Removed Unused Local Instances.
//         1.3 07/30/2006 Main Class Name to MyJSQLView.
//         1.4 08/04/2006 Comment Changes.
//         1.5 04/23/2007 Changed to the DataDeliminatorPanel Class
//                        Extending JPanel.
//         1.6 04/23/2007 Removed Frame References & Changed Action
//                        Buttons to restoreDefaultsButton and applyButton.
//         1.7 04/23/2007 Modified Actions for Default and Finished Class
//                        Method getDelimiter().
//         1.8 04/26/2007 Set Up applyButton.setEnabled() Based on Changes.
//         1.9 04/28/2007 Renamed to DataDeliminatorPreferencesPanel.
//         2.0 04/30/2007 Removed Class Method getLineTerminator() & Set
//                        Class Instance to Private as Needed.
//         2.1 05/02/2007 Comment Changes After Review Since Modified How
//                        the DataDelimiter is Used.
//         2.2 05/14/2007 Class Method actionPerformed() applyButton Action
//                        Minor Code Changes.
//         2.3 06/03/2007 Renamed From CSVDeliminatorPreferencesPanel to
//                        CSVExportPreferencesPanel.
//         2.4 06/04/2007 Changes to Layout & Implemented Components for
//                        TEXT FIELD Options.
//         2.5 06/04/2007 Class Method setCSVExportProperties() Insured Spinner
//                        is Enabled if includeTextCheckBox is Set.
//         2.6 06/13/2007 Changed Name/Reference to CSV.
//         2.7 06/14/2007 Set the Default Comma Separated Value to Comma.
//         2.8 09/08/2007 Code Cleanup.
//         2.9 09/09/2007 Final Class Instance defaultCharsInclude Made Static.
//         3.0 12/12/2007 Header Update.
//         3.1 12/23/2007 MyJSQLView.getDataExportProperties().
//         3.2 01/25/2008 Deliminator to Delimiter.
//         3.3 05/13/2008 Added Class Instance serialVersionUID.
//         3.4 08/23/2008 Class Instance includeTextCheckBox Added CLOB
//                        to Label.
//         3.5 10/21/2008 MyJSQLView Project Common Source Code Formatting.
//         3.6 10/30/2008 Constructor, Class Methods actionPerformed() &
//                        dataExportProperties() getDataExportProperites
//                        Changed Over to the MyJSQLView_Frame Class.
//         3.7 11/12/2008 Changed MyJSQLView_Frame.getDatabaseExportProperties()
//                        Method Moved Over to the DBTablesPanel.
//         3.8 05/06/2009 Date Format Option Panel, Corresponding Components and
//                        Actions. Constructor, Methods actionPerformed(),
//                        getCSVImportOptions, & setCSVImportProperties().
//         3.9 05/07/2009 Constructor Instance dateFormatOptions Chararcters DD
//                        Changed to dd.
//         4.0 05/14/2009 Instance dateFormatOptions Changed the Sequence of the
//                        Elements in the Array and Added Elements MMM.
//         4.1 05/22/2009 Class Instance otherTextField Set To Be Enabled After
//                        Selection As Delimiter Option Then Panel Re-Opened.
//                        Class Method setCSVExportProperties().
//         4.2 05/27/2009 Removed the Empty Border Around the dateFormatComboBox
//                        Instance & Moved it Over to the dateFormatLabel in the
//                        Constructor.
//         4.3 10/24/2009 Minor Format Change.
//         4.4 02/18/2010 Changed Package to Reflect Dandy Made Productions Code.
//         4.5 03/08/2010 Implementation of Internationalization via Class Instance
//                        resourceBundle. Argument Added to Constructor. Added Arguments
//                        to Method createTextOptionPanel() & createDelimiterPanel().
//         4.6 05/18/2010 Class Method actionPerformed() & setCSVExportProperites() 
//                        textMaxCharsSpinner Set Value via valueOf() Instead of
//                        Creating a new Double().
//         4.7 06/13/2010 DataExportPropertes Class Change of get/setDataFormat() Method
//                        to get/setCSVDataFormat(). Affected Methods getCSVExportOptions()
//                        & setCSVExportProperties().
//         4.8 06/13/2010 Minor Comment Changes to Methods Information.
//
//-----------------------------------------------------------------
//                 danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *    The CSVExportPreferencesPanel class provides a generic panel
 * in the appearance of a form for selecting the CSV data export
 * options.
 * 
 * @author Dana M. Proctor
 * @version 4.8 06/13/2010
 */

class CSVExportPreferencesPanel extends JPanel implements ActionListener, ChangeListener
{
   // Class Instances.
   private static final long serialVersionUID = -6715093777643439840L;

   private JCheckBox includeTextCheckBox;
   private JSpinner textMaxCharsSpinner;
   private static final int defaultCharsInclude = 50;
   private JRadioButton tabRadioButton, semicolonRadioButton, commaRadioButton, spaceRadioButton,
           otherRadioButton;
   private JTextField otherTextField;
   private JComboBox dateFormatComboBox;
   private JButton restoreDefaultsButton, applyButton;

   //===========================================================
   // DataPreferencesPreferencesDialog Constructor
   //===========================================================

   protected CSVExportPreferencesPanel(MyJSQLView_ResourceBundle resourceBundle)
   {
      // Class Instances
      JPanel mainPanel, textOptionsPanel, delimiterPanel, dateFormatPanel;
      JPanel buttonPanel;
      JLabel dateFormatLabel;
      String resource;

      // Setting up
      setLayout(new BorderLayout());

      GridBagLayout gridbag = new GridBagLayout();
      GridBagConstraints constraints = new GridBagConstraints();

      mainPanel = new JPanel(new BorderLayout());
      mainPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(1, 0, 0, 0),
                          BorderFactory.createLoweredBevelBorder()));

      // ==================================================
      // TEXT Options Panel & Compoenent .

      textOptionsPanel = new JPanel(new GridLayout(2, 1, 2, 2));
      textOptionsPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4),
                                 BorderFactory.createEtchedBorder()));
      createTextOptionsPanel(textOptionsPanel, gridbag, constraints, resourceBundle);
      mainPanel.add(textOptionsPanel, BorderLayout.NORTH);

      // =====================================================
      // Delimiter Panel & Components

      delimiterPanel = new JPanel(gridbag);
      delimiterPanel.setBorder(BorderFactory.createCompoundBorder(
         BorderFactory.createEmptyBorder(4, 4, 4, 4), BorderFactory.createEtchedBorder()));
      createDelimiterPanel(delimiterPanel, gridbag, constraints, resourceBundle);
      mainPanel.add(delimiterPanel, BorderLayout.CENTER);
      add(mainPanel, BorderLayout.CENTER);
      
      // =====================================================
      // Date Format Panel & Components
      
      dateFormatPanel = new JPanel(gridbag);
      dateFormatPanel.setBorder(BorderFactory.createCompoundBorder(
         BorderFactory.createEmptyBorder(4, 4, 4, 4), BorderFactory.createEtchedBorder()));
      
      resource = resourceBundle.getResource("CSVExportPreferencesPanel.label.DateFormat");
      if (resource.equals(""))
         dateFormatLabel = new JLabel("Date Format");
      else
         dateFormatLabel = new JLabel(resource);
      dateFormatLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
      
      buildConstraints(constraints, 0, 0, 1, 1, 24, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(dateFormatLabel, constraints);
      dateFormatPanel.add(dateFormatLabel);
      
      Object[] dateFormatOptions = {"MM-dd-YYYY", "MM/dd/YYYY", "MMM-dd-YYYY", "dd-MM-YYYY",
                                    "dd/MM/YYYY", "dd-MMM-YYYY", "YYYY-MM-dd", "YYYY/MM/dd",
                                    "YYYY-MMM-dd"};
      
      dateFormatComboBox = new JComboBox(dateFormatOptions);
      dateFormatComboBox.addActionListener(this);
      
      buildConstraints(constraints, 1, 0, 1, 1, 76, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(dateFormatComboBox, constraints);
      dateFormatPanel.add(dateFormatComboBox);
      
      mainPanel.add(dateFormatPanel, BorderLayout.SOUTH);
      add(mainPanel, BorderLayout.CENTER);

      // Button Action Options Panel
      buttonPanel = new JPanel();
      
      resource = resourceBundle.getResource("CSVExportPreferencesPanel.button.RestoreDefaults");
      if (resource.equals(""))
         restoreDefaultsButton = new JButton("Restore Defaults");
      else
         restoreDefaultsButton = new JButton(resource);
      restoreDefaultsButton.addActionListener(this);
      buttonPanel.add(restoreDefaultsButton);

      resource = resourceBundle.getResource("CSVExportPreferencesPanel.button.Apply");
      if (resource.equals(""))
         applyButton = new JButton("Apply");
      else
         applyButton = new JButton(resource);
      applyButton.addActionListener(this);
      buttonPanel.add(applyButton);

      add(buttonPanel, BorderLayout.SOUTH);
      
      // Retrieve existing state and set accordingly.
      setCSVExportProperties(DBTablesPanel.getDataExportProperties());
      applyButton.setEnabled(false);
   }

   //========================================================
   // ActionEvent Listener method for detecting the inputs
   // from the panel and directing to the appropriate routine.
   //========================================================

   public void actionPerformed(ActionEvent evt)
   {
      Object formSource = evt.getSource();

      if (formSource instanceof JButton)
      {
         // Restore Defaults Button Action
         if (formSource == restoreDefaultsButton)
         {
            includeTextCheckBox.setSelected(false);
            textMaxCharsSpinner.setValue(Integer.valueOf(defaultCharsInclude));
            textMaxCharsSpinner.setEnabled(false);
            commaRadioButton.setSelected(true);
            otherTextField.setEnabled(false);
            dateFormatComboBox.setSelectedIndex(0);
            applyButton.setEnabled(true);
         }

         // Apply Button Action
         else if (formSource == applyButton)
         {
            DBTablesPanel.setDataExportProperties(getCSVExportOptions());
            applyButton.setEnabled(false);
         }
      }

      // Detect the selection of TEXT inclusion
      // for enabling the chars numbers spinner.
      if (formSource instanceof JCheckBox)
      {
         if (formSource == includeTextCheckBox)
         {
            applyButton.setEnabled(true);

            if (includeTextCheckBox.isSelected())
               textMaxCharsSpinner.setEnabled(true);
            else
               textMaxCharsSpinner.setEnabled(false);
         }
      }

      // Triggering the apply button back to enabled
      // when option changes and controlling the
      // other textfield.
      if (formSource instanceof JRadioButton || formSource instanceof JComboBox)
      {
         applyButton.setEnabled(true);

         if (formSource instanceof JRadioButton)
         {
            if (otherRadioButton.isSelected())
               otherTextField.setEnabled(true);
            else
               otherTextField.setEnabled(false);
         }
      }
   }

   //================================================================
   // ActionEvent Listener method for determining when the selections
   // have been made so an update can be performed on the DataExport
   // Properties.
   //================================================================

   public void stateChanged(ChangeEvent evt)
   {
      Object panelSource = evt.getSource();

      if (panelSource instanceof JSpinner && applyButton != null)
         applyButton.setEnabled(true);
   }

   //================================================================
   // Class Method to create the Text Options Components.
   //================================================================

   private void createTextOptionsPanel(JPanel textOptionsPanel,
                                       GridBagLayout gridbag, GridBagConstraints constraints,
                                       MyJSQLView_ResourceBundle resourceBundle)
   {
      // Class Method Instances
      JPanel charNumberSelectionPanel;
      SpinnerNumberModel textCharSpinnerModel;
      JLabel charNumberLabel;

      final int minimumCharsIncluded = 0;
      final int maxCharsIncluded = 16777215;
      final int spinnerCharsIncludedStep = 50;
      String resource;
      
      // Add Checkbox and Spinner

      resource = resourceBundle.getResource("CSVExportPreferencesPanel.checkbox.Include");
      if (resource.equals(""))
         includeTextCheckBox = new JCheckBox("Include CLOB, TEXT, MEDIUMTEXT & LONGTEXT", false);
      else
         includeTextCheckBox = new JCheckBox(resource + " CLOB, TEXT, MEDIUMTEXT & LONGTEXT", false);
      includeTextCheckBox.setFocusPainted(false);
      includeTextCheckBox.addActionListener(this);
      textOptionsPanel.add(includeTextCheckBox);

      charNumberSelectionPanel = new JPanel(gridbag);
      charNumberSelectionPanel.setBorder(BorderFactory.createLoweredBevelBorder());

      resource = resourceBundle.getResource("CSVExportPreferencesPanel.label.TextCharacterNumber");
      if (resource.equals(""))
         charNumberLabel = new JLabel("Text Character Number");
      else
         charNumberLabel = new JLabel(resource);
      charNumberLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

      buildConstraints(constraints, 0, 0, 1, 1, 100, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(charNumberLabel, constraints);
      charNumberSelectionPanel.add(charNumberLabel);

      textCharSpinnerModel = new SpinnerNumberModel(defaultCharsInclude, minimumCharsIncluded,
                                                    maxCharsIncluded, spinnerCharsIncludedStep);
      textMaxCharsSpinner = new JSpinner(textCharSpinnerModel);
      textMaxCharsSpinner.setEnabled(false);
      textMaxCharsSpinner.addChangeListener(this);

      buildConstraints(constraints, 1, 0, 1, 1, 100, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(textMaxCharsSpinner, constraints);
      charNumberSelectionPanel.add(textMaxCharsSpinner);
      textOptionsPanel.add(charNumberSelectionPanel);
   }

   //================================================================
   // Class Method to create the Delimiter Options Components.
   //================================================================

   private void createDelimiterPanel(JPanel delimiterPanel,
                                     GridBagLayout gridbag, GridBagConstraints constraints,
                                     MyJSQLView_ResourceBundle resourceBundle)
   {
      // Class Method Instances
      JPanel delimiterSelectionPanel;
      JLabel delimiterLabel;
      String resource;

      // Delimiter Label & Radio Buttons

      resource = resourceBundle.getResource("CSVExportPreferencesPanel.label.Delimiter");
      if (resource.equals(""))
         delimiterLabel = new JLabel("Delimiter");
      else
         delimiterLabel = new JLabel(resource);

      buildConstraints(constraints, 0, 0, 1, 1, 30, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(delimiterLabel, constraints);
      delimiterPanel.add(delimiterLabel);

      delimiterSelectionPanel = new JPanel(gridbag);
      delimiterSelectionPanel.setBorder(BorderFactory.createCompoundBorder(
                              BorderFactory.createEmptyBorder(0, 0, 0, 0),
                              BorderFactory.createLoweredBevelBorder()));

      ButtonGroup delimiterButtonGroup = new ButtonGroup();

      resource = resourceBundle.getResource("CSVExportPreferencesPanel.radiobutton.Tab");
      if (resource.equals(""))
         tabRadioButton = new JRadioButton("Tab", false);
      else
         tabRadioButton = new JRadioButton(resource, false);
      tabRadioButton.setFocusPainted(false);
      tabRadioButton.addActionListener(this);
      delimiterButtonGroup.add(tabRadioButton);

      buildConstraints(constraints, 0, 0, 1, 1, 100, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.WEST;
      gridbag.setConstraints(tabRadioButton, constraints);
      delimiterSelectionPanel.add(tabRadioButton);

      resource = resourceBundle.getResource("CSVExportPreferencesPanel.radiobutton.Semicolon");
      if (resource.equals(""))
         semicolonRadioButton = new JRadioButton("Semicolon", false);
      else
         semicolonRadioButton = new JRadioButton(resource, false);
      semicolonRadioButton.setFocusPainted(false);
      semicolonRadioButton.addActionListener(this);
      delimiterButtonGroup.add(semicolonRadioButton);

      buildConstraints(constraints, 1, 0, 1, 1, 100, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.WEST;
      gridbag.setConstraints(semicolonRadioButton, constraints);
      delimiterSelectionPanel.add(semicolonRadioButton);

      resource = resourceBundle.getResource("CSVExportPreferencesPanel.radiobutton.Comma");
      if (resource.equals(""))
         commaRadioButton = new JRadioButton("Comma", true);
      else
         commaRadioButton = new JRadioButton(resource, true);
      commaRadioButton.setFocusPainted(false);
      commaRadioButton.addActionListener(this);
      delimiterButtonGroup.add(commaRadioButton);

      buildConstraints(constraints, 0, 1, 1, 1, 100, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.WEST;
      gridbag.setConstraints(commaRadioButton, constraints);
      delimiterSelectionPanel.add(commaRadioButton);

      resource = resourceBundle.getResource("CSVExportPreferencesPanel.radiobutton.Space");
      if (resource.equals(""))
         spaceRadioButton = new JRadioButton("Space", false);
      else
         spaceRadioButton = new JRadioButton(resource, false);
      spaceRadioButton.setFocusPainted(false);
      spaceRadioButton.addActionListener(this);
      delimiterButtonGroup.add(spaceRadioButton);

      buildConstraints(constraints, 1, 1, 1, 1, 100, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.WEST;
      gridbag.setConstraints(spaceRadioButton, constraints);
      delimiterSelectionPanel.add(spaceRadioButton);

      resource = resourceBundle.getResource("CSVExportPreferencesPanel.radiobutton.Other");
      if (resource.equals(""))
         otherRadioButton = new JRadioButton("Other", false);
      else
         otherRadioButton = new JRadioButton(resource, false);
      otherRadioButton.setFocusPainted(false);
      otherRadioButton.addActionListener(this);
      delimiterButtonGroup.add(otherRadioButton);

      buildConstraints(constraints, 0, 2, 1, 1, 100, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.WEST;
      gridbag.setConstraints(otherRadioButton, constraints);
      delimiterSelectionPanel.add(otherRadioButton);

      otherTextField = new JTextField(10);
      otherTextField.setEnabled(false);

      buildConstraints(constraints, 1, 2, 1, 1, 100, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.WEST;
      gridbag.setConstraints(otherTextField, constraints);
      delimiterSelectionPanel.add(otherTextField);

      buildConstraints(constraints, 1, 0, 1, 1, 70, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(delimiterSelectionPanel, constraints);
      delimiterPanel.add(delimiterSelectionPanel);
   }

   //================================================================
   // Class Method for helping the parameters in gridbag.
   //================================================================

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

   //===============================================================
   // Class method to get the delimiter string.
   //===============================================================

   protected DataExportProperties getCSVExportOptions()
   {
      DataExportProperties newDataProperties = DBTablesPanel.getDataExportProperties();

      // TEXT Insclusion
      newDataProperties.setTextInclusion(includeTextCheckBox.isSelected());
      newDataProperties.setTextCharsNumber(Integer.parseInt(textMaxCharsSpinner.getValue().toString()));

      // Delimiter
      if (tabRadioButton.isSelected())
         newDataProperties.setDataDelimiter("\t");
      else if (semicolonRadioButton.isSelected())
         newDataProperties.setDataDelimiter(";");
      else if (commaRadioButton.isSelected())
         newDataProperties.setDataDelimiter(",");
      else if (spaceRadioButton.isSelected())
         newDataProperties.setDataDelimiter(" ");
      else
         newDataProperties.setDataDelimiter(otherTextField.getText());
      
      // Date Format
      newDataProperties.setCSVDateFormat((String)dateFormatComboBox.getSelectedItem());

      return newDataProperties;
   }

   //========================================================
   // Class method to get the delimiter string.
   //========================================================

   protected void setCSVExportProperties(DataExportProperties dataProperties)
   {
      // TEXT Inclusion
      includeTextCheckBox.setSelected(dataProperties.getTextInclusion());
      if (includeTextCheckBox.isSelected())
         textMaxCharsSpinner.setEnabled(true);

      textMaxCharsSpinner.setValue(Integer.valueOf(dataProperties.getTextCharsNumber()));

      // Delimiter
      if (dataProperties.getDataDelimiter().equals("\t"))
         tabRadioButton.setSelected(true);
      else if (dataProperties.getDataDelimiter().equals(";"))
         semicolonRadioButton.setSelected(true);
      else if (dataProperties.getDataDelimiter().equals(","))
         commaRadioButton.setSelected(true);
      else if (dataProperties.getDataDelimiter().equals(" "))
         spaceRadioButton.setSelected(true);
      else
      {
         otherRadioButton.setSelected(true);
         otherTextField.setText(dataProperties.getDataDelimiter());
         otherTextField.setEnabled(true);
      }
      
      // Date Format
      dateFormatComboBox.setSelectedItem(dataProperties.getCSVDateFormat());
   }
}