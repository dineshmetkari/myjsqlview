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
// Copyright (C) 2005-2011 Dana M. Proctor
// Version 1.2 09/109/2011
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
//
//-----------------------------------------------------------------
//                 danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *    The GeneralPreferencesPanel class provides a generic panel
 * in the appearance of a form for selecting the MyJSQLView general
 * options.
 * 
 * @author Dana M. Proctor
 * @version 1.2 09/09/2011
 */

class GeneralPreferencesPanel extends JPanel implements ActionListener, ChangeListener
{
   // Class Instances.
   private static final long serialVersionUID = 4715186260795158107L;
   
   private JComboBox dateFormatComboBox;
   private JButton restoreDefaultsButton, applyButton;

   //===========================================================
   // GeneralPreferencesPanel Constructor
   //===========================================================

   protected GeneralPreferencesPanel(MyJSQLView_ResourceBundle resourceBundle)
   {
      // Class Instances
      JPanel mainPanel, dateFormatPanel;
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
      
      buildConstraints(constraints, 0, 0, 1, 1, 24, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(dateFormatLabel, constraints);
      dateFormatPanel.add(dateFormatLabel);
      
      dateFormatComboBox = new JComboBox(MyJSQLView_Utils.dateFormatOptions);
      dateFormatComboBox.addActionListener(this);
      
      buildConstraints(constraints, 1, 0, 1, 1, 76, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(dateFormatComboBox, constraints);
      dateFormatPanel.add(dateFormatComboBox);
      
      mainPanel.add(dateFormatPanel, BorderLayout.NORTH);
      add(mainPanel, BorderLayout.CENTER);

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
      
      // Date Format
      newGeneralProperties.setViewDateFormat((String)dateFormatComboBox.getSelectedItem());

      return newGeneralProperties;
   }

   //========================================================
   // Class method to set the panel options.
   //========================================================

   protected void setGeneralProperties(GeneralProperties generalProperties)
   {
      // Date Format
      dateFormatComboBox.setSelectedItem(generalProperties.getViewDateFormat());
   }
}