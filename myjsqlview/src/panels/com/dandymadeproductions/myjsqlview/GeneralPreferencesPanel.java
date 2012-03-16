//=================================================================
//             MyJSQLView GeneralPreferencesPanel
//=================================================================
//
//    This class provides a generic panel in the appearance of
// a form for selecting the MyJSQLView general options.
//
//             << GeneralPreferencesPanel.java >>
//
//=================================================================
// Copyright (C) 2005-2012 Dana M. Proctor
// Version 1.5 03/16/2012
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
// Version 1.0 01/08/2011 Original GeneralPreferencesPanel Class.
//         1.1 01/10/2011 Changed Format for Year From YYYY to yyyy for dateFormatOptions
//                        in Constructor.
//         1.2 09/09/2011 Removed Instance dateFormatOptions in Constuctor and Replaced
//                        by Obtaining from MyJSQLView_Utils Class.
//         1.3 09/13/2011 Constructor Obtained dateFormatComboBox From MyJSQLView_Utils.
//                        getDateFormatOptions().
//         1.4 01/01/2012 Copyright Update.
//         1.5 03/16/2012 Implement Limit Increment Option. Added Class Instances limit
//                        IncrementSpinner & DEFAULT_LIMIT_INCREMENT. Created in Constructor.
//                        Added set/getter to get/setGeneralOptionsProperties() Methods.
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

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *    The GeneralPreferencesPanel class provides a generic panel
 * in the appearance of a form for selecting the MyJSQLView general
 * options.
 * 
 * @author Dana M. Proctor
 * @version 1.5 03/16/2012
 */

class GeneralPreferencesPanel extends JPanel implements ActionListener, ChangeListener
{
   // Class Instances.
   private static final long serialVersionUID = 4715186260795158107L;
   
   private JComboBox dateFormatComboBox;
   private JSpinner limitIncrementSpinner;
   private JButton restoreDefaultsButton, applyButton;
   
   protected static final int DEFAULT_LIMIT_INCREMENT = 10000;

   //===========================================================
   // GeneralPreferencesPanel Constructor
   //===========================================================

   protected GeneralPreferencesPanel(MyJSQLView_ResourceBundle resourceBundle)
   {
      // Class Instances
      JPanel mainPanel, dateFormatPanel, limitIncrementPanel;
      JPanel centerPanel;
      JPanel buttonPanel;
      JLabel dateFormatLabel, limitIncrementLabel;;
      SpinnerNumberModel limitIncrementSpinnerModel;
      final int minimumLimitIncrementSize = 2;
      final int maxLimitIncrementSize = 100000;
      final int spinnerLimitIncrementStep = 1000;
      String resource;

      // Setting up
      setLayout(new BorderLayout());

      GridBagLayout gridbag = new GridBagLayout();
      GridBagConstraints constraints = new GridBagConstraints();

      mainPanel = new JPanel(new GridLayout(2, 1, 2, 2));
      mainPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(1, 0, 0, 0),
                          BorderFactory.createLoweredBevelBorder()));
      
      // =====================================================
      // Date Format Panel & Components
      
      dateFormatPanel = new JPanel(gridbag);
      dateFormatPanel.setBorder(BorderFactory.createCompoundBorder(
         BorderFactory.createEmptyBorder(4, 4, 4, 4), BorderFactory.createEtchedBorder()));
      
      resource = resourceBundle.getResource("GeneralPreferencesPanel.label.DateFormat");
      if (resource.equals(""))
         dateFormatLabel = new JLabel("Date Format");
      else
         dateFormatLabel = new JLabel(resource);
      dateFormatLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
      
      buildConstraints(constraints, 0, 0, 1, 1, 50, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(dateFormatLabel, constraints);
      dateFormatPanel.add(dateFormatLabel);
      
      dateFormatComboBox = new JComboBox(MyJSQLView_Utils.getDateFormatOption());
      dateFormatComboBox.addActionListener(this);
      
      buildConstraints(constraints, 1, 0, 1, 1, 50, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(dateFormatComboBox, constraints);
      dateFormatPanel.add(dateFormatComboBox);
      
      mainPanel.add(dateFormatPanel);
      
      // =====================================================
      // Limit Increment Panel & Components
      
      limitIncrementPanel = new JPanel(gridbag);
      limitIncrementPanel.setBorder(BorderFactory.createCompoundBorder(
         BorderFactory.createEmptyBorder(4, 4, 4, 4), BorderFactory.createEtchedBorder()));

      resource = resourceBundle.getResource("GeneralPreferencesPanel.label.TableReadLimitIcrement");
      if (resource.equals(""))
         limitIncrementLabel = new JLabel(" Table Read Limit Increment");
      else
         limitIncrementLabel = new JLabel(" " + resource);

      buildConstraints(constraints, 0, 0, 1, 1, 50, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(limitIncrementLabel, constraints);
      limitIncrementPanel.add(limitIncrementLabel);
      
      
      limitIncrementSpinnerModel = new SpinnerNumberModel(DEFAULT_LIMIT_INCREMENT,
         minimumLimitIncrementSize, maxLimitIncrementSize, spinnerLimitIncrementStep);
      limitIncrementSpinner = new JSpinner(limitIncrementSpinnerModel);
      limitIncrementSpinner.addChangeListener(this);

      buildConstraints(constraints, 1, 0, 1, 1, 50, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(limitIncrementSpinner, constraints);
      limitIncrementPanel.add(limitIncrementSpinner);

      mainPanel.add(limitIncrementPanel);
      
      add(mainPanel, BorderLayout.NORTH);
      
      // Dummy Center Panel
      centerPanel = new JPanel();
      centerPanel.setBorder(BorderFactory.createLoweredBevelBorder());
      add(centerPanel, BorderLayout.CENTER);

      // Button Action Options Panel
      buttonPanel = new JPanel();
      
      resource = resourceBundle.getResource("GeneralPreferencesPanel.button.RestoreDefaults");
      if (resource.equals(""))
         restoreDefaultsButton = new JButton("Restore Defaults");
      else
         restoreDefaultsButton = new JButton(resource);
      restoreDefaultsButton.addActionListener(this);
      buttonPanel.add(restoreDefaultsButton);

      resource = resourceBundle.getResource("GeneralPreferencesPanel.button.Apply");
      if (resource.equals(""))
         applyButton = new JButton("Apply");
      else
         applyButton = new JButton(resource);
      applyButton.addActionListener(this);
      buttonPanel.add(applyButton);

      add(buttonPanel, BorderLayout.SOUTH);
      
      // Retrieve existing state and set accordingly.
      setGeneralProperties(DBTablesPanel.getGeneralProperties());
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
            dateFormatComboBox.setSelectedIndex(0);
            limitIncrementSpinner.setValue(Integer.valueOf(DEFAULT_LIMIT_INCREMENT));
            applyButton.setEnabled(true);
         }

         // Apply Button Action
         else if (formSource == applyButton)
         {
            DBTablesPanel.setGeneralProperties(getGeneralOptions());
            applyButton.setEnabled(false);
         }
      }

      // Triggering the apply button back to enabled
      // when option changes and controlling the
      // other textfield.
      if (formSource instanceof JRadioButton || formSource instanceof JComboBox)
      {
         applyButton.setEnabled(true);
      }
   }

   //==============================================================
   // ChangeEvent Listener method for determined when the limit
   // increment spinner has changed so that the apply button can be
   // enabled.
   //==============================================================

   public void stateChanged(ChangeEvent evt)
   {
      Object panelSource = evt.getSource();

      if (panelSource instanceof JSpinner && applyButton != null)
         applyButton.setEnabled(true);
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
   // Class method to get the panels options.
   //===============================================================

   protected GeneralProperties getGeneralOptions()
   {
      GeneralProperties newGeneralProperties = DBTablesPanel.getGeneralProperties();
      
      // Date Format & Limit Increment
      newGeneralProperties.setViewDateFormat((String)dateFormatComboBox.getSelectedItem());
      newGeneralProperties.setLimitIncrement(Integer.parseInt(limitIncrementSpinner.getValue().toString()));

      return newGeneralProperties;
   }

   //========================================================
   // Class method to set the panel options.
   //========================================================

   protected void setGeneralProperties(GeneralProperties generalProperties)
   {
      // Date Format & Limit Increment
      dateFormatComboBox.setSelectedItem(generalProperties.getViewDateFormat());
      limitIncrementSpinner.setValue(Integer.valueOf(generalProperties.getLimitIncrement()));
   }
}