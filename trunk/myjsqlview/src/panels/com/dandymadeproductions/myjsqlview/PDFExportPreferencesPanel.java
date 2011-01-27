//=================================================================
//             MyJSQLView PDFExportPreferencesPanel
//=================================================================
//
//    This class provides a generic panel in the appearance of
// a form for selecting the PDF data export options.
//
//             << PDFExportPreferencesPanel.java >>
//
//=================================================================
// Copyright (C) 2005-2011 Dana M. Proctor
// Version 1.4 01/27/2011
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
// Version 1.0 06/13/2010 Original PDFExportPreferencesPanel Class.
//         1.1 06/15/2010 Class Method fillHeaderPanel() Changed headerFontLabel
//                        & headerBorderLabel Text.
//         1.2 06/15/2010 Minor Changes in Positioning of Label in fillTitlePanel()
//                        & fillHeaderPanel() Class Methods.
//         1.3 06/15/2010 Changed Class Instance titleTableNameRadioButton to
//                        titleDefaultRadioButton. Class Methods Effected fillHeaderPanel(),
//                        getPDFExportOptions(), & setPDFExportOptions().
//         1.4 01/27/2011 Copyright Update.
//
//-----------------------------------------------------------------
//                 danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Color;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *    The PDFExportPreferencesPanel class provides a generic panel
 * in the appearance of a form for selecting the PDF data export options.
 * 
 * @author Dana M. Proctor
 * @version 1.4 01/27/2011
 */

class PDFExportPreferencesPanel extends JPanel implements ActionListener, ChangeListener
{
   // Class Instances.
   private static final long serialVersionUID = 7846961366728748725L;

   private GridBagLayout gridbag;
   private GridBagConstraints constraints;

   private JRadioButton titleNoneRadioButton, titleDefaultRadioButton, titleCustomRadioButton;
   private JTextField customTitleTextField;
   private JSpinner titleFontSizeSpinner;
   private JButton titleColorButton;
   private JSpinner headerFontSizeSpinner, headerBorderSizeSpinner;
   private JButton headerColorButton, headerBorderColorButton;
   private JComboBox numberAlignmentComboBox;
   private JComboBox dateAlignmentComboBox, dateFormatComboBox;
   private JButton restoreDefaultsButton, applyButton;

   private JColorChooser panelColorChooser;
   private String actionCommand;

   private static final int defaultTitleFontSize = 14;
   private static final int defaultHeaderFontSize = 12;
   private static final int minimumFontSize = 8;
   private static final int maxFontSize = 32;
   private static final int defaultBorderSize = 1;
   private static final int minimumBorderSize = 1;
   private static final int maxBorderSize = 6;
   private static final int spinnerSizeStep = 1;

   private static final Object[] dateFormatOptions = {"MM-dd-YYYY", "MM/dd/YYYY", "MMM-dd-YYYY",
                                                      "dd-MM-YYYY", "dd/MM/YYYY", "dd-MMM-YYYY",
                                                      "YYYY-MM-dd", "YYYY/MM/dd", "YYYY-MMM-dd"};
   private static final Object[] alignmentOptions = {"LEFT", "CENTER", "RIGHT"};

   //===========================================================
   // DataPreferencesPreferencesDialog Constructor
   //===========================================================

   protected PDFExportPreferencesPanel(MyJSQLView_ResourceBundle resourceBundle)
   {
      // Class Instances
      JPanel mainPanel, centerPanel;
      JPanel titlePanel, headerPanel, numberPanel, datePanel;
      JPanel buttonPanel;

      String resource;

      // Setting up
      setLayout(new BorderLayout());

      gridbag = new GridBagLayout();
      constraints = new GridBagConstraints();

      mainPanel = new JPanel(new BorderLayout());
      mainPanel.setBorder(BorderFactory.createCompoundBorder(
                               BorderFactory.createEmptyBorder(1, 0, 0, 0),
                               BorderFactory.createLoweredBevelBorder()));
      centerPanel = new JPanel(gridbag);

      createColorChooser();
      actionCommand = "";

      // ==================================================
      // Title Panel.

      titlePanel = new JPanel(gridbag);
      titlePanel.setBorder(BorderFactory.createCompoundBorder(
                                BorderFactory.createEmptyBorder(2, 2, 2, 2),
                                BorderFactory.createEtchedBorder()));
      fillTitlePanel(titlePanel, resourceBundle);

      mainPanel.add(titlePanel, BorderLayout.NORTH);

      // =====================================================
      // Header Panel.

      headerPanel = new JPanel(gridbag);
      headerPanel.setBorder(BorderFactory.createCompoundBorder(
                                 BorderFactory.createEmptyBorder(2, 2, 2, 2),
                                 BorderFactory.createEtchedBorder()));
      fillHeaderPanel(headerPanel, resourceBundle);

      buildConstraints(constraints, 0, 0, 1, 1, 100, 70);
      constraints.fill = GridBagConstraints.BOTH;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(headerPanel, constraints);
      centerPanel.add(headerPanel);

      // =====================================================
      // Number Panel.

      numberPanel = new JPanel(gridbag);
      numberPanel.setBorder(BorderFactory.createCompoundBorder(
                                BorderFactory.createEmptyBorder(2, 2, 2, 2),
                                BorderFactory.createEtchedBorder()));
      fillNumberPanel(numberPanel, resourceBundle);

      buildConstraints(constraints, 0, 1, 1, 1, 100, 30);
      constraints.fill = GridBagConstraints.BOTH;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(numberPanel, constraints);
      centerPanel.add(numberPanel);

      mainPanel.add(centerPanel, BorderLayout.CENTER);

      // =====================================================
      // Date Panel.

      datePanel = new JPanel(gridbag);
      datePanel.setBorder(BorderFactory.createCompoundBorder(
                              BorderFactory.createEmptyBorder(2, 2, 2, 2),
                              BorderFactory.createEtchedBorder()));
      fillDatePanel(datePanel, resourceBundle);
      mainPanel.add(datePanel, BorderLayout.SOUTH);

      add(mainPanel, BorderLayout.CENTER);

      // Button Action Options Panel
      buttonPanel = new JPanel();

      resource = resourceBundle.getResource("PDFExportPreferencesPanel.button.RestoreDefaults");
      if (resource.equals(""))
         restoreDefaultsButton = new JButton("Restore Defaults");
      else
         restoreDefaultsButton = new JButton(resource);
      restoreDefaultsButton.addActionListener(this);
      buttonPanel.add(restoreDefaultsButton);

      resource = resourceBundle.getResource("PDFExportPreferencesPanel.button.Apply");
      if (resource.equals(""))
         applyButton = new JButton("Apply");
      else
         applyButton = new JButton(resource);
      applyButton.addActionListener(this);
      buttonPanel.add(applyButton);

      add(buttonPanel, BorderLayout.SOUTH);

      // Retrieve existing state and set accordingly.
      setPDFExportProperties(DBTablesPanel.getDataExportProperties());
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
         // Title Font & Header Font/Border Color Selection Action
         if (formSource == titleColorButton || formSource == headerColorButton
             || formSource == headerBorderColorButton)
         {
            JButton currentButton = (JButton) formSource;
            actionCommand = currentButton.getActionCommand();

            if (currentButton == titleColorButton)
            {
               panelColorChooser.setBorder(BorderFactory.createTitledBorder("Title Color"));
               panelColorChooser.setColor(titleColorButton.getBackground());
            }
            else if (currentButton == headerColorButton)
            {
               panelColorChooser.setBorder(BorderFactory.createTitledBorder("Header Color"));
               panelColorChooser.setColor(headerColorButton.getBackground());
            }
            else
            {
               panelColorChooser.setBorder(BorderFactory.createTitledBorder("Header Border Color"));
               panelColorChooser.setColor(headerBorderColorButton.getBackground());
            }

            // Create the color chooser dialog.
            JDialog dialog;
            dialog = JColorChooser.createDialog(this, "Color Selector", true, panelColorChooser, this, null);
            dialog.setVisible(true);
            dialog.dispose();
            return;
         }

         // Color Chooser Action
         if (((JButton) formSource).getText().equals("OK"))
         {
            applyButton.setEnabled(true);

            if (actionCommand.equals("Title Color"))
               titleColorButton.setBackground(panelColorChooser.getColor());
            else if (actionCommand.equals("Header Color"))
               headerColorButton.setBackground(panelColorChooser.getColor());
            else
               headerBorderColorButton.setBackground(panelColorChooser.getColor());

            actionCommand = "";
         }

         // Restore Defaults Button Action
         if (formSource == restoreDefaultsButton)
         {
            titleNoneRadioButton.setSelected(true);
            customTitleTextField.setText("");
            customTitleTextField.setEnabled(false);
            titleFontSizeSpinner.setValue(Integer.valueOf(defaultTitleFontSize));
            titleColorButton.setBackground(Color.BLACK);
            headerFontSizeSpinner.setValue(defaultHeaderFontSize);
            headerColorButton.setBackground(Color.BLACK);
            headerBorderSizeSpinner.setValue(defaultBorderSize);
            headerBorderColorButton.setBackground(Color.BLACK);
            numberAlignmentComboBox.setSelectedIndex(2);
            dateFormatComboBox.setSelectedItem("MM-dd-YYYY");
            dateAlignmentComboBox.setSelectedIndex(1);
         }

         // Apply Button Action
         else if (formSource == applyButton)
         {
            DBTablesPanel.setDataExportProperties(getPDFExportOptions());
            applyButton.setEnabled(false);
         }
      }

      // Triggering the apply button back to enabled
      // when option changes and controlling the
      // custom title textfield.
      
      if (formSource instanceof JRadioButton || formSource instanceof JComboBox)
      {
         applyButton.setEnabled(true);

         if (formSource instanceof JRadioButton)
         {
            if (titleCustomRadioButton.isSelected())
               customTitleTextField.setEnabled(true);
            else
               customTitleTextField.setEnabled(false);
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
   // Class Method to fill the title panel with the needed components.
   //================================================================

   private void fillTitlePanel(JPanel titlePanel, MyJSQLView_ResourceBundle resourceBundle)
   {
      // Class Method Instances
      JPanel titleSelectionPanel;
      JLabel titleLabel, titleFontSizeLabel, titleColorLabel;
      SpinnerNumberModel titleFontSizeSpinnerModel;
      String resource;

      // Title Label.

      resource = resourceBundle.getResource("PDFExportPreferencesPanel.label.Title");
      if (resource.equals(""))
         titleLabel = new JLabel("Title");
      else
         titleLabel = new JLabel(resource);
      titleLabel.setBorder(BorderFactory.createEmptyBorder(3, 0, 0, 0));

      buildConstraints(constraints, 0, 0, 4, 1, 100, 33);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(titleLabel, constraints);
      titlePanel.add(titleLabel);

      // Title Selection.

      titleSelectionPanel = new JPanel(gridbag);
      titleSelectionPanel.setBorder(BorderFactory.createCompoundBorder(
                                          BorderFactory.createEmptyBorder(3, 0, 3, 0),
                                          BorderFactory.createLoweredBevelBorder()));

      ButtonGroup titleButtonGroup = new ButtonGroup();

      resource = resourceBundle.getResource("PDFExportPreferencesPanel.radiobutton.None");
      if (resource.equals(""))
         titleNoneRadioButton = new JRadioButton("None", true);
      else
         titleNoneRadioButton = new JRadioButton(resource, true);
      titleNoneRadioButton.setFocusPainted(false);
      titleNoneRadioButton.addActionListener(this);
      titleButtonGroup.add(titleNoneRadioButton);

      buildConstraints(constraints, 0, 0, 1, 1, 12, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(titleNoneRadioButton, constraints);
      titleSelectionPanel.add(titleNoneRadioButton);

      resource = resourceBundle.getResource("PDFExportPreferencesPanel.radiobutton.TableName");
      if (resource.equals(""))
         titleDefaultRadioButton = new JRadioButton("Default", false);
      else
         titleDefaultRadioButton = new JRadioButton(resource, false);
      titleDefaultRadioButton.setFocusPainted(false);
      titleDefaultRadioButton.addActionListener(this);
      titleButtonGroup.add(titleDefaultRadioButton);

      buildConstraints(constraints, 1, 0, 1, 1, 12, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(titleDefaultRadioButton, constraints);
      titleSelectionPanel.add(titleDefaultRadioButton);

      resource = resourceBundle.getResource("PDFExportPreferencesPanel.radiobutton.Custom");
      if (resource.equals(""))
         titleCustomRadioButton = new JRadioButton("Custom", true);
      else
         titleCustomRadioButton = new JRadioButton(resource, true);
      titleCustomRadioButton.setFocusPainted(false);
      titleCustomRadioButton.addActionListener(this);
      titleButtonGroup.add(titleCustomRadioButton);

      buildConstraints(constraints, 2, 0, 1, 1, 12, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(titleCustomRadioButton, constraints);
      titleSelectionPanel.add(titleCustomRadioButton);

      customTitleTextField = new JTextField(10);
      customTitleTextField.setEnabled(false);

      buildConstraints(constraints, 3, 0, 1, 1, 64, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(customTitleTextField, constraints);
      titleSelectionPanel.add(customTitleTextField);

      buildConstraints(constraints, 0, 1, 4, 1, 100, 33);
      constraints.fill = GridBagConstraints.HORIZONTAL;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(titleSelectionPanel, constraints);
      titlePanel.add(titleSelectionPanel);

      // Title Font Size

      resource = resourceBundle.getResource("PDFExportPreferencesPanel.label.TitleFontSize");
      if (resource.equals(""))
         titleFontSizeLabel = new JLabel("Font Size");
      else
         titleFontSizeLabel = new JLabel(resource);
      titleFontSizeLabel.setBorder(BorderFactory.createEmptyBorder(3, 0, 8, 0));

      buildConstraints(constraints, 0, 2, 1, 1, 12, 33);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.EAST;
      gridbag.setConstraints(titleFontSizeLabel, constraints);
      titlePanel.add(titleFontSizeLabel);

      titleFontSizeSpinnerModel = new SpinnerNumberModel(defaultTitleFontSize, minimumFontSize,
                                                         maxFontSize, spinnerSizeStep);
      titleFontSizeSpinner = new JSpinner(titleFontSizeSpinnerModel);
      titleFontSizeSpinner.addChangeListener(this);

      buildConstraints(constraints, 1, 2, 1, 1, 38, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(titleFontSizeSpinner, constraints);
      titlePanel.add(titleFontSizeSpinner);

      // Title Font Color

      resource = resourceBundle.getResource("PDFExportPreferencesPanel.label.TitleFontColor");
      if (resource.equals(""))
         titleColorLabel = new JLabel("Font Color");
      else
         titleColorLabel = new JLabel(resource);
      titleColorLabel.setBorder(BorderFactory.createEmptyBorder(3, 0, 8, 0));

      buildConstraints(constraints, 2, 2, 1, 1, 12, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.EAST;
      gridbag.setConstraints(titleColorLabel, constraints);
      titlePanel.add(titleColorLabel);

      titleColorButton = new JButton();
      titleColorButton.setActionCommand("Title Color");
      titleColorButton.setBackground(Color.BLACK);
      titleColorButton.setFocusable(false);
      titleColorButton.setMargin(new Insets(5, 15, 5, 15));
      titleColorButton.addActionListener(this);

      buildConstraints(constraints, 3, 2, 1, 1, 38, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(titleColorButton, constraints);
      titlePanel.add(titleColorButton);
   }

   //================================================================
   // Class Method to fill the header panel with the needed components.
   //================================================================

   private void fillHeaderPanel(JPanel headerPanel, MyJSQLView_ResourceBundle resourceBundle)
   {
      // Class Method Instances
      JLabel headerLabel;
      JLabel headerFontLabel, headerBorderLabel;
      SpinnerNumberModel headerFontSizeSpinnerModel;
      SpinnerNumberModel headerBorderSizeSpinnerModel;

      String resource;

      // Header Label.

      resource = resourceBundle.getResource("PDFExportPreferencesPanel.label.HeaderColumns");
      if (resource.equals(""))
         headerLabel = new JLabel("Header Columns");
      else
         headerLabel = new JLabel(resource);
      headerLabel.setBorder(BorderFactory.createEmptyBorder(3, 0, 0, 0));

      buildConstraints(constraints, 0, 0, 6, 1, 100, 50);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(headerLabel, constraints);
      headerPanel.add(headerLabel);

      // Header Font Size

      resource = resourceBundle.getResource("PDFExportPreferencesPanel.label.HeaderFont");
      if (resource.equals(""))
         headerFontLabel = new JLabel("Font");
      else
         headerFontLabel = new JLabel(resource);
      headerFontLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 6, 0));

      buildConstraints(constraints, 0, 1, 1, 1, 18, 50);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.EAST;
      gridbag.setConstraints(headerFontLabel, constraints);
      headerPanel.add(headerFontLabel);

      headerFontSizeSpinnerModel = new SpinnerNumberModel(defaultHeaderFontSize, minimumFontSize,
                                                          maxFontSize, spinnerSizeStep);
      headerFontSizeSpinner = new JSpinner(headerFontSizeSpinnerModel);
      headerFontSizeSpinner.addChangeListener(this);

      buildConstraints(constraints, 1, 1, 1, 1, 16, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(headerFontSizeSpinner, constraints);
      headerPanel.add(headerFontSizeSpinner);

      // Header Color

      headerColorButton = new JButton();
      headerColorButton.setActionCommand("Header Color");
      headerColorButton.setBackground(Color.BLACK);
      headerColorButton.setFocusable(false);
      headerColorButton.setMargin(new Insets(5, 10, 5, 10));
      headerColorButton.addActionListener(this);

      buildConstraints(constraints, 2, 1, 1, 1, 16, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(headerColorButton, constraints);
      headerPanel.add(headerColorButton);

      // Header Border Size

      resource = resourceBundle.getResource("PDFExportPreferencesPanel.label.HeaderBorder");
      if (resource.equals(""))
         headerBorderLabel = new JLabel("Border");
      else
         headerBorderLabel = new JLabel(resource);
      headerBorderLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 6, 0));

      buildConstraints(constraints, 3, 1, 1, 1, 18, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.EAST;
      gridbag.setConstraints(headerBorderLabel, constraints);
      headerPanel.add(headerBorderLabel);

      headerBorderSizeSpinnerModel = new SpinnerNumberModel(defaultBorderSize, minimumBorderSize,
                                                            maxBorderSize, spinnerSizeStep);
      headerBorderSizeSpinner = new JSpinner(headerBorderSizeSpinnerModel);
      headerBorderSizeSpinner.addChangeListener(this);

      buildConstraints(constraints, 4, 1, 1, 1, 16, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(headerBorderSizeSpinner, constraints);
      headerPanel.add(headerBorderSizeSpinner);

      // Header Border Color

      headerBorderColorButton = new JButton();
      headerBorderColorButton.setActionCommand("Border Color");
      headerBorderColorButton.setBackground(Color.BLACK);
      headerBorderColorButton.setFocusable(false);
      headerBorderColorButton.setMargin(new Insets(5, 10, 5, 10));
      headerBorderColorButton.addActionListener(this);

      buildConstraints(constraints, 5, 1, 1, 1, 16, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(headerBorderColorButton, constraints);
      headerPanel.add(headerBorderColorButton);
   }

   //================================================================
   // Class Method to fill the Number Panel with the needed components.
   //================================================================

   private void fillNumberPanel(JPanel numberPanel, MyJSQLView_ResourceBundle resourceBundle)
   {
      // Class Method Instances
      String resource;
      JLabel numberFieldsAlignmentLabel;

      // Number Fields Alignment Label.

      resource = resourceBundle.getResource("PDFExportPreferencesPanel.label.NumberFieldsAlignment");
      if (resource.equals(""))
         numberFieldsAlignmentLabel = new JLabel("Number Fields Alignment");
      else
         numberFieldsAlignmentLabel = new JLabel(resource);
      numberFieldsAlignmentLabel.setBorder(BorderFactory.createEmptyBorder(3, 10, 10, 10));

      buildConstraints(constraints, 0, 1, 1, 1, 50, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.EAST;
      gridbag.setConstraints(numberFieldsAlignmentLabel, constraints);
      numberPanel.add(numberFieldsAlignmentLabel);

      numberAlignmentComboBox = new JComboBox(alignmentOptions);
      numberAlignmentComboBox.setSelectedIndex(2);
      numberAlignmentComboBox.addActionListener(this);

      buildConstraints(constraints, 1, 1, 1, 1, 50, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.WEST;
      gridbag.setConstraints(numberAlignmentComboBox, constraints);
      numberPanel.add(numberAlignmentComboBox);
   }

   //================================================================
   // Class Method to create the Date Options Components.
   //================================================================

   private void fillDatePanel(JPanel datePanel, MyJSQLView_ResourceBundle resourceBundle)
   {
      // Class Method Instances
      String resource;
      JLabel dateLabel, dateAlignmentLabel, dateFormatLabel;

      // Date Field Label.

      resource = resourceBundle.getResource("PDFExportPreferencesPanel.label.DateFields");
      if (resource.equals(""))
         dateLabel = new JLabel("Date Fields");
      else
         dateLabel = new JLabel(resource);
      dateLabel.setBorder(BorderFactory.createEmptyBorder(3, 0, 0, 0));

      buildConstraints(constraints, 0, 0, 4, 1, 100, 50);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(dateLabel, constraints);
      datePanel.add(dateLabel);

      // Date Format & Alignment.

      resource = resourceBundle.getResource("PDFExportPreferencesPanel.label.DateFormat");
      if (resource.equals(""))
         dateFormatLabel = new JLabel("Format");
      else
         dateFormatLabel = new JLabel(resource);
      dateFormatLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

      buildConstraints(constraints, 0, 1, 1, 1, 12, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(dateFormatLabel, constraints);
      datePanel.add(dateFormatLabel);

      dateFormatComboBox = new JComboBox(dateFormatOptions);
      dateFormatComboBox.addActionListener(this);

      buildConstraints(constraints, 1, 1, 1, 1, 38, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(dateFormatComboBox, constraints);
      datePanel.add(dateFormatComboBox);

      resource = resourceBundle.getResource("PDFExportPreferencesPanel.label.DateAlignment");
      if (resource.equals(""))
         dateAlignmentLabel = new JLabel("Alignment");
      else
         dateAlignmentLabel = new JLabel(resource);
      dateAlignmentLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

      buildConstraints(constraints, 2, 1, 1, 1, 12, 50);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(dateAlignmentLabel, constraints);
      datePanel.add(dateAlignmentLabel);

      dateAlignmentComboBox = new JComboBox(alignmentOptions);
      dateAlignmentComboBox.setSelectedIndex(1);
      dateAlignmentComboBox.addActionListener(this);

      buildConstraints(constraints, 3, 1, 1, 1, 38, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(dateAlignmentComboBox, constraints);
      datePanel.add(dateAlignmentComboBox);
   }

   //==============================================================
   // Class Method to create a color chooser used to select the
   // color for the title, header, and border options.
   //==============================================================

   private void createColorChooser()
   {
      // Method Instances.
      AbstractColorChooserPanel[] colorChooserPanels;

      // Create color chooser.
      panelColorChooser = new JColorChooser();
      panelColorChooser.setBorder(BorderFactory.createTitledBorder("Color"));
      panelColorChooser.setColor(this.getBackground());
      colorChooserPanels = panelColorChooser.getChooserPanels();
      panelColorChooser.removeChooserPanel(colorChooserPanels[0]);
      panelColorChooser.removeChooserPanel(colorChooserPanels[2]);
      panelColorChooser.setPreviewPanel(new JPanel());
   }

   //================================================================
   // Class Method for helping the parameters in gridbag.
   //================================================================

   private void buildConstraints(GridBagConstraints gbc, int gx, int gy, int gw, int gh, double wx, double wy)
   {
      gbc.gridx = gx;
      gbc.gridy = gy;
      gbc.gridwidth = gw;
      gbc.gridheight = gh;
      gbc.weightx = wx;
      gbc.weighty = wy;
   }

   //===============================================================
   // Class method to get the PDF Export Properties.
   //===============================================================

   protected DataExportProperties getPDFExportOptions()
   {

      DataExportProperties newDataProperties = DBTablesPanel.getDataExportProperties();

      // Title Options

      if (titleNoneRadioButton.isSelected())
         newDataProperties.setTitle("");
      else if (titleDefaultRadioButton.isSelected())
         newDataProperties.setTitle("EXPORTED TABLE");
      else
         newDataProperties.setTitle(customTitleTextField.getText());

      newDataProperties.setTitleFontSize(Integer.parseInt(titleFontSizeSpinner.getValue().toString()));
      newDataProperties.setTitleColor(titleColorButton.getBackground());

      // Header Options

      newDataProperties.setHeaderFontSize(Integer.parseInt(headerFontSizeSpinner.getValue().toString()));
      newDataProperties.setHeaderColor(headerColorButton.getBackground());
      newDataProperties.setHeaderBorderSize(Integer.parseInt(headerBorderSizeSpinner.getValue().toString()));
      newDataProperties.setHeaderBorderColor(headerBorderColorButton.getBackground());

      // Number Alignment Option

      newDataProperties.setNumberAlignment(numberAlignmentComboBox.getSelectedIndex());

      // Date Options

      newDataProperties.setPDFDateFormat((String) dateFormatComboBox.getSelectedItem());
      newDataProperties.setDateAlignment(dateAlignmentComboBox.getSelectedIndex());

      return newDataProperties;
   }

   //========================================================
   // Class method to set the PDF Export Properties.
   //========================================================

   protected void setPDFExportProperties(DataExportProperties dataProperties)
   {
      // Title Options

      if (dataProperties.getTitle().equals(""))
         titleNoneRadioButton.setSelected(true);
      else if (dataProperties.getTitle().equals("EXPORTED TABLE"))
         titleDefaultRadioButton.setSelected(true);
      else
      {
         titleCustomRadioButton.setSelected(true);
         customTitleTextField.setText(dataProperties.getTitle());
      }

      titleFontSizeSpinner.setValue(Integer.valueOf(dataProperties.getTitleFontSize()));
      titleColorButton.setBackground(dataProperties.getTitleColor());

      // Header Options

      headerFontSizeSpinner.setValue(Integer.valueOf(dataProperties.getHeaderFontSize()));
      headerColorButton.setBackground(dataProperties.getHeaderColor());
      headerBorderSizeSpinner.setValue(Integer.valueOf(dataProperties.getHeaderBorderSize()));
      headerBorderColorButton.setBackground(dataProperties.getHeaderBorderColor());

      // Number Alignment Option

      numberAlignmentComboBox.setSelectedIndex(dataProperties.getNumberAlignment());

      // Date Options

      dateFormatComboBox.setSelectedItem(dataProperties.getPDFDateFormat());
      dateAlignmentComboBox.setSelectedIndex(dataProperties.getDateAlignment());
   }
}