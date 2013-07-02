//=================================================================
//       MyJSQLView CSVImportPreferencesPanel
//=================================================================
//
// 	This class provides a generic panel in the appearance of
// a form for selecting the CSV data import options.
//
//             << CSVImportPreferencesPanel.java >>
//
//=================================================================
// Copyright (C) 2005-2013 Dana M. Proctor
// Version 3.9 07/01/2013
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
// Version 1.0 12/02/2007 Original CSVImportPreferencesPanel Class.
//         1.1 12/03/2007 Cleaned Out, Commented and Added fillPanel.
//         1.2 Header Update.
//         1.3 12/23/2007 MyJSQLView.getDataExportProperties().
//         1.4 01/25/2008 Deliminator to Delimiter.
//         1.5 Added Class Instance serialVersionUID.
//         1.6 10/21/2008 MyJSQLView Project Common Source Code Formatting.
//         1.7 10/30/2008 Constructor, Class Methods actionPerformed() &
//                        dataExportProperties() getDataImportProperites
//                        Changed Over to the MyJSQLView_Frame Class.
//         1.8 11/12/2008 Changed MyJSQLView_Frame.getDatabaseImportProperties()
//                        Method Moved Over to the DBTablesPanel.
//         1.9 05/06/2009 Date Format Option Panel, Corresponding Components and
//                        Actions. Constructor, Methods actionPerformed(),
//                        getCSVImportOptions, & setCSVImportProperties().
//         2.0 05/07/2009 Constructor Instance dateFormatOptions Chararcters DD
//                        Changed to dd.
//         2.1 05/14/2009 Instance dateFormatOptions Changed the Sequence of the
//                        Elements in the Array.
//         2.2 05/22/2009 Class Instance otherTextField Set To Be Enabled After
//                        Selection As Delimiter Option Then Panel Re-Opened.
//                        Class Method setCSVExportProperties().
//         2.3 05/27/2009 Removed the Empty Border Around the dateFormatComboBox
//                        Instance & Moved it Over to the dateFormatLabel in the
//                        Constructor.
//         2.4 06/11/2009 Added Class Instance csvImportPanelFiller & Associated
//                        Components/Setup.
//         2.5 06/12/2009 Added JPanel fillerDelimiterPanel to Balance Out the Filler
//                        And Delimiter Panels.
//         2.6 02/18/2010 Changed Package to Reflect Dandy Made Productions Code.
//         2.7 03/08/2010 Implementation of Internationalization via Class Instance
//                        resourceBundle. Argument Added to Constructor. Added Arguments
//                        to Method createDelimiterPanel().
//         2.8 03/08/2010 Organized Imports.
//         2.9 01/10/2011 Changed Format for Year From YYYY to yyyy for dateFormatOptions
//                        in Constructor.
//         3.0 02/13/2011 Added KeyListener to Detect When Changes Take Place in the
//                        otherTextField.
//         3.1 09/09/2011 Removed Instance dateFormatOptions in Constuctor and Replaced
//                        by Obtaining from MyJSQLView_Utils Class.
//         3.2 09/13/2011 Constructor Obtained dateFormatComboBox From MyJSQLView_Utils.
//                        getDateFormatOptions().
//         3.3 10/01/2011 Set Date Format to DBTablesPanel.getGeneralProperties().getViewDateFormat()
//                        on Restore Defaults in actionPerformed().
//         3.4 01/01/2012 Copyright Update.
//         3.5 07/08/2012 Changes in Way MyJSQLView_ResourceBundle Handles the Collection
//                        of Resource Strings. Change to resource.getResourceString(key,
//                        default).
//         3.6 09/10/2012 Changed Package Name to com.dandymadeproductions.myjsqlview.gui.panels.
//                        Made Class, Constructor, & Getter/Setter Methods Public Along With
//                        csvImportPanelFiller.
//         3.7 09/20/2012 Creation of csvImportPanelFillerThread in Constructor and Starting
//                        There for the CSVImportPreferencesFiller.
//         3.8 09/20/2012 Backed Out 3.7.
//         3.9 07/01/2013 Change in actionPerformed() to Use DBTablePanel.getGeneralDBProperties().
//
//-----------------------------------------------------------------
//                 danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview.gui.panels;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import com.dandymadeproductions.myjsqlview.structures.DataImportProperties;
import com.dandymadeproductions.myjsqlview.utilities.MyJSQLView_ResourceBundle;
import com.dandymadeproductions.myjsqlview.utilities.MyJSQLView_Utils;

/**
 *    The CSVImportPreferencesPanel class provides a generic panel in
 * the appearance of a form for selecting the CSV data import options.
 * 
 * @author Dana M. Proctor
 * @version 3.9 07/01/2013
 */

public class CSVImportPreferencesPanel extends JPanel implements ActionListener, KeyListener
{
   // Class Instances.
   private static final long serialVersionUID = -8955230851237916675L;

   public CSVImportPreferencesFiller csvImportPanelFiller;
   private JRadioButton tabRadioButton, semicolonRadioButton, commaRadioButton,
                        spaceRadioButton, otherRadioButton;
   private JTextField otherTextField;
   private JComboBox dateFormatComboBox;
   private JButton restoreDefaultsButton, applyButton;

   //===========================================================
   // CSVImportPreferencesPanel Constructor
   //===========================================================

   public CSVImportPreferencesPanel(MyJSQLView_ResourceBundle resourceBundle)
   {
      // Class Instances
      JPanel mainPanel, fillerDelimiterPanel;
      JPanel fillerPanel, delimiterPanel, dateFormatPanel;
      JPanel buttonPanel;
      
      JLabel dateFormatLabel;
      String resource;

      // Setting up
      setLayout(new BorderLayout());

      GridBagLayout gridbag = new GridBagLayout();
      GridBagConstraints constraints = new GridBagConstraints();

      mainPanel = new JPanel(new BorderLayout());
      mainPanel.setBorder(BorderFactory.createCompoundBorder(
                                 BorderFactory.createEmptyBorder(0, 0, 0, 0),
                                 BorderFactory.createLoweredBevelBorder()));
      
      fillerDelimiterPanel = new JPanel(new GridLayout(2, 1, 0, 0));
      fillerDelimiterPanel.setBorder(BorderFactory.createEmptyBorder());

      // ==================================================
      // Filler Panel.
      
      fillerPanel = new JPanel();
      fillerPanel.setBorder(BorderFactory.createCompoundBorder(
         BorderFactory.createEmptyBorder(0, 0, 0, 0),
         BorderFactory.createLoweredBevelBorder()));
      
      csvImportPanelFiller = new CSVImportPreferencesFiller();
      csvImportPanelFiller.setThreadAction(true);
     
      fillerPanel.add(csvImportPanelFiller);
      fillerDelimiterPanel.add(fillerPanel);
      
      // =====================================================
      // Delimiter Panel & Components
 
      delimiterPanel = new JPanel(gridbag);
      delimiterPanel.setBorder(BorderFactory.createCompoundBorder(
         BorderFactory.createEmptyBorder(4, 4, 4, 4), BorderFactory.createEtchedBorder()));
      createDelimiterPanel(delimiterPanel, gridbag, constraints, resourceBundle);
      fillerDelimiterPanel.add(delimiterPanel);
      
      mainPanel.add(fillerDelimiterPanel, BorderLayout.CENTER);
      
      // =====================================================
      // Date Format Panel & Components
      
      dateFormatPanel = new JPanel(gridbag);
      dateFormatPanel.setBorder(BorderFactory.createCompoundBorder(
         BorderFactory.createEmptyBorder(4, 4, 4, 4), BorderFactory.createEtchedBorder()));
      
      resource = resourceBundle.getResourceString("CSVImportPreferencesPanel.label.DateFormat",
                                                  "Date Format");
      dateFormatLabel = new JLabel(resource);
      dateFormatLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
      
      buildConstraints(constraints, 0, 0, 1, 1, 24, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(dateFormatLabel, constraints);
      dateFormatPanel.add(dateFormatLabel);
      
      dateFormatComboBox = new JComboBox(MyJSQLView_Utils.getDateFormatOption());
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
      
      resource = resourceBundle.getResourceString("CSVImportPreferencesPanel.button.RestoreDefaults",
                                                  "Restore Defaults");
      restoreDefaultsButton = new JButton(resource);
      restoreDefaultsButton.addActionListener(this);
      buttonPanel.add(restoreDefaultsButton);

      resource = resourceBundle.getResourceString("CSVImportPreferencesPanel.button.Apply", "Apply");
      applyButton = new JButton(resource);
      applyButton.addActionListener(this);
      buttonPanel.add(applyButton);

      add(buttonPanel, BorderLayout.SOUTH);
      
      // Retrieve existing state and set accordingly.
      setCSVImportProperties(DBTablesPanel.getDataImportProperties());
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
            commaRadioButton.setSelected(true);
            otherTextField.setEnabled(false);
            dateFormatComboBox.setSelectedItem(DBTablesPanel.getGeneralDBProperties().getViewDateFormat());
            applyButton.setEnabled(true);
         }

         // Apply Button Action
         else if (formSource == applyButton)
         {
            DBTablesPanel.setDataImportProperties(getCSVImportOptions());
            applyButton.setEnabled(false);
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
   
   //==============================================================
   // KeyEvent Listener method for detected key pressed events to
   // full fill KeyListener Interface requirements.
   //==============================================================

   public void keyPressed(KeyEvent evt)
   {
      // Do Nothing
   }

   //==============================================================
   // KeyEvent Listener method for detecting key released events
   // to full fill KeyListener Interface requirements.
   //==============================================================

   public void keyReleased(KeyEvent evt)
   {
      // Do Nothing
   }

   //==============================================================
   // KeyEvent Listener method for detecting key pressed event,
   // in this case the otherTextField.
   //==============================================================

   public void keyTyped(KeyEvent evt)
   {
      if (evt.getSource() == otherTextField)
         applyButton.setEnabled(true);
   }

   //================================================================
   // Class Method to create the Delimiter Options Panel.
   //================================================================

   private void createDelimiterPanel(JPanel delimiterPanel,
                                     GridBagLayout gridbag, GridBagConstraints constraints,
                                     MyJSQLView_ResourceBundle resourceBundle)
   {
      // Class Method Instances
      JPanel checkBoxPanel;
      JLabel delimiterLabel;
      String resource;

      // Delimiter Label & Radio Buttons

      resource = resourceBundle.getResourceString("CSVImportPreferencesPanel.label.Delimiter",
                                                  "Delimiter");
      delimiterLabel = new JLabel(resource);

      buildConstraints(constraints, 0, 0, 1, 1, 30, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(delimiterLabel, constraints);
      delimiterPanel.add(delimiterLabel);

      checkBoxPanel = new JPanel(gridbag);
      checkBoxPanel.setBorder(BorderFactory.createCompoundBorder(
                              BorderFactory.createEmptyBorder(0, 0, 0, 0),
                              BorderFactory.createLoweredBevelBorder()));

      ButtonGroup delimiterButtonGroup = new ButtonGroup();

      resource = resourceBundle.getResourceString("CSVImportPreferencesPanel.radiobutton.Tab",
                                                  "Tab");
      tabRadioButton = new JRadioButton(resource, false);
      tabRadioButton.setFocusPainted(false);
      tabRadioButton.addActionListener(this);
      delimiterButtonGroup.add(tabRadioButton);

      buildConstraints(constraints, 0, 0, 1, 1, 100, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.WEST;
      gridbag.setConstraints(tabRadioButton, constraints);
      checkBoxPanel.add(tabRadioButton);

      resource = resourceBundle.getResourceString("CSVImportPreferencesPanel.radiobutton.Semicolon",
                                                  "Semicolon");
      semicolonRadioButton = new JRadioButton(resource, false);
      semicolonRadioButton.setFocusPainted(false);
      semicolonRadioButton.addActionListener(this);
      delimiterButtonGroup.add(semicolonRadioButton);

      buildConstraints(constraints, 1, 0, 1, 1, 100, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.WEST;
      gridbag.setConstraints(semicolonRadioButton, constraints);
      checkBoxPanel.add(semicolonRadioButton);

      resource = resourceBundle.getResourceString("CSVImportPreferencesPanel.radiobutton.Comma", "Comma");
      commaRadioButton = new JRadioButton(resource, true);
      commaRadioButton.setFocusPainted(false);
      commaRadioButton.addActionListener(this);
      delimiterButtonGroup.add(commaRadioButton);

      buildConstraints(constraints, 0, 1, 1, 1, 100, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.WEST;
      gridbag.setConstraints(commaRadioButton, constraints);
      checkBoxPanel.add(commaRadioButton);

      resource = resourceBundle.getResourceString("CSVImportPreferencesPanel.radiobutton.Space", "Space");
      spaceRadioButton = new JRadioButton(resource, false);
      spaceRadioButton.setFocusPainted(false);
      spaceRadioButton.addActionListener(this);
      delimiterButtonGroup.add(spaceRadioButton);

      buildConstraints(constraints, 1, 1, 1, 1, 100, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.WEST;
      gridbag.setConstraints(spaceRadioButton, constraints);
      checkBoxPanel.add(spaceRadioButton);

      resource = resourceBundle.getResourceString("CSVImportPreferencesPanel.radiobutton.Other", "Other");
      otherRadioButton = new JRadioButton(resource, false);
      otherRadioButton.setFocusPainted(false);
      otherRadioButton.addActionListener(this);
      delimiterButtonGroup.add(otherRadioButton);

      buildConstraints(constraints, 0, 2, 1, 1, 100, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.WEST;
      gridbag.setConstraints(otherRadioButton, constraints);
      checkBoxPanel.add(otherRadioButton);

      otherTextField = new JTextField(10);
      otherTextField.addKeyListener(this);
      otherTextField.setEnabled(false);

      buildConstraints(constraints, 1, 2, 1, 1, 100, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.WEST;
      gridbag.setConstraints(otherTextField, constraints);
      checkBoxPanel.add(otherTextField);

      buildConstraints(constraints, 1, 0, 1, 1, 70, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(checkBoxPanel, constraints);
      delimiterPanel.add(checkBoxPanel);
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
   // Class method to get the data import properties.
   //===============================================================

   public DataImportProperties getCSVImportOptions()
   {
      DataImportProperties newDataProperties = DBTablesPanel.getDataImportProperties();

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
      newDataProperties.setDateFormat((String)dateFormatComboBox.getSelectedItem());

      return newDataProperties;
   }

   //===============================================================
   // Class method to set the data import properties.
   //===============================================================

   public void setCSVImportProperties(DataImportProperties dataProperties)
   {
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
      dateFormatComboBox.setSelectedItem(dataProperties.getDateFormat());
   }
}
