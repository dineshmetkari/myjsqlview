//=================================================================
//                      MyJSQLView UpdateForm
//=================================================================
//
//    This class provides a generic form that is used by each
// TableTabPanel to execute a SQL update statement on the current
// table.
//
//                      << UpdateForm.java >>
//
//=================================================================
// Copyright (C) 2005-2010 Dana M. Proctor
// Version 2.3 03/01/2010
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
// Version 1.0 09/21/2009 Original MyJSQLView UpdateForm Class.
//         1.1 09/24/2009 Basic Completed UpdateForm Class.
//         1.2 09/25/2009 Threaded updateButton Action and Activated DBTablesPanel
//                        Status Timer for Same. Removed callingToRefreshButton
//                        and Replaced with disposeButton. Updated Class Method
//                        updateTable() to boolean Return Type. All This to More
//                        Closely Follow the Structure of the Existing TableEntryForm
//                        Architecture.
//         1.3 09/25/2009 Removed DBTablesPanel Status Timer and Created The Status
//                        Timer Here in This Class. Added Class Methods statusTimer(),
//                        and stopStatusTimer(). Added Class Instance refreshCheckBox.
//         1.4 09/26/2009 Removed Class Methods statusTimer() & stopStatusTimer().
//                        Implemented Just Basic Activity Working/Idle for statusIndicator.
//                        Reviewed and Cleaned Up for Finalization.
//         1.5 09/28/2009 Added Class Instance deleteDataIcon.
//         1.6 10/25/2009 Removed Class Instance fileSeparator. Obtained iconsDirectory
//                        From MyJSQLView_Utils Class.
//         1.7 10/25/2009 Added fileSeparator to iconsDirectory.
//         1.8 10/28/2009 Added Class Instance questionButton and Its Addition to the
//                        northPanel Along With Action Processing in actionPerformed)().
//         1.9 10/29/2009 MyJSQLView Project Common Source Code Formatting. Implemented
//                        Proper Handling of Date, Time, DateTime, Year, and TimeStamp
//                        Updates in Class Method updateTable(). Added Class Instance
//                        colmunType/Class & SizeHashMap Class Instances and Constructor
//                        Arguments.
//         2.0 11/01/2009 Class Method updateTable() Additional Cleanup of Code and
//                        Checks on Input.
//         2.1 02/18/2010 Changed Package to Reflect Dandy Made Productions Code.
//         2.2 02/28/2010 Added Argument MyJSQLView_ResourceBundle to Constructor and
//                        Implemented Internationalization. Added Constructor Instance
//                        resource. Class Method createUPdateWhereInterface() Added
//                        resourceBundle & resource Instances. Class Method updateTable()
//                        Created Resource Bundle Message and Several Instances for
//                        Internationalization With Dialog.
//         2.3 03/01/2010 Correction in Class Method createUpdateWhereInterface() for
//                        Setting whereLabel to resourceWhere. Cleaned Up a Bit. Moved
//                        Constructor Instance resourceBundle to Class Instance. Removed
//                        MyJSQLView_ResourceBundle Argument From createUpdateWhereInterface().
//
//-----------------------------------------------------------------
//                 danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.*;

/**
 *    This class provides a generic form that is used by each TableTabPanel to
 * execute a SQL update statement on the current table.
 * 
 * @author Dana M. Proctor
 * @version 2.3 03/01/2010
 */

class UpdateForm extends JFrame implements ActionListener
{
   // Class Instances.
   private static final long serialVersionUID = -3252154506926965611L;

   private String sqlTable;
   private String identifierQuoteString;
   private HashMap columnNamesHashMap, columnClassHashMap;
   private HashMap columnTypeHashMap, columnSizeHashMap;
   private Vector comboBoxColumnNames;
   private MyJSQLView_ResourceBundle resourceBundle;

   private GridBagLayout gridbag;
   private GridBagConstraints constraints;

   private JPanel updateWherePanel;

   private JLabel statusIndicator;
   private JTextField statusLabel;
   private JCheckBox refreshCheckBox;
   private JButton questionButton;

   private JComboBox updateColumnComboBox;
   private JTextField updateColumnToTextField;
   private JCheckBox quoteCheckBox;

   private JComboBox whereComboBox1, whereComboBox2, whereComboBox3;
   private JComboBox whereComboBox4, whereComboBox5;
   private JComboBox operatorComboBox1, operatorComboBox2, operatorComboBox3;
   private JComboBox operatorComboBox4, operatorComboBox5;
   private JTextField whereTextField1, whereTextField2, whereTextField3;
   private JTextField whereTextField4, whereTextField5;
   private JComboBox andOrComboBox1, andOrComboBox2, andOrComboBox3, andOrComboBox4;
   private Vector stateComponents;

   private JButton updateButton, closeButton, clearButton;
   protected JButton findButton, disposeButton;

   private ImageIcon deleteDataIcon;

   //==============================================================
   // UpdateForm Constructor
   //==============================================================

   protected UpdateForm(String table, MyJSQLView_ResourceBundle resourceBundle, HashMap columnNamesHashMap,
                        HashMap columnClassHashMap, HashMap columnTypeHashMap, HashMap columnSizeHashMap,
                        Vector comboBoxColumnNames)
   {
      sqlTable = table;
      this.columnNamesHashMap = columnNamesHashMap;
      this.comboBoxColumnNames = comboBoxColumnNames;
      this.columnClassHashMap = columnClassHashMap;
      this.columnTypeHashMap = columnTypeHashMap;
      this.columnSizeHashMap = columnSizeHashMap;
      this.resourceBundle = resourceBundle;

      // Constructor Instances
      ImageIcon statusIdleIcon, statusWorkingIcon;
      JPanel mainPanel, formPanel;
      JPanel northPanel, statusPanel, optionsPanel, helpPanel;
      JPanel southPanel, actionButtonPanel, clearPanel;
      String resource, iconsDirectory;
      ImageIcon questionIcon;
      ImageIcon clearIcon;

      // Setting up a icons directory and other instances.
      
      resource = resourceBundle.getResource("UpdateForm.message.Title");
      if (resource.equals(""))
         setTitle("Update : " + table);
      else
         setTitle(resource + " : " + table);

      iconsDirectory = MyJSQLView_Utils.getIconsDirectory() + MyJSQLView_Utils.getFileSeparator();
      identifierQuoteString = MyJSQLView_Access.getIdentifierQuoteString();

      statusIdleIcon = new ImageIcon(iconsDirectory + "statusIdleIcon.png");
      statusWorkingIcon = new ImageIcon(iconsDirectory + "statusWorkingIcon.png");
      deleteDataIcon = new ImageIcon(iconsDirectory + "deleteDataIcon.gif");
      stateComponents = new Vector();

      // Setting up the frame's main panel.
      mainPanel = new JPanel(new BorderLayout());
      mainPanel.setBorder(BorderFactory.createEtchedBorder());

      // Setting up and Creating the input panel.
      gridbag = new GridBagLayout();
      constraints = new GridBagConstraints();

      formPanel = new JPanel();
      formPanel.setLayout(gridbag);
      formPanel.setBorder(BorderFactory.createRaisedBevelBorder());
      formPanel.addMouseListener(MyJSQLView.getPopupMenuListener());

      // Status, Options, & Help Panel Components.

      northPanel = new JPanel();
      northPanel.setLayout(gridbag);
      northPanel.setBorder(BorderFactory.createRaisedBevelBorder());

      // Status
      statusPanel = new JPanel(gridbag);
      statusPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLoweredBevelBorder(),
                                                     BorderFactory.createEmptyBorder(0, 2, 0, 1)));

      statusIndicator = new JLabel("", JLabel.LEFT);
      statusIndicator.setIcon(statusIdleIcon);
      statusIndicator.setDisabledIcon(statusWorkingIcon);
      statusIndicator.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 2));

      buildConstraints(constraints, 0, 0, 1, 1, 20, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.WEST;
      gridbag.setConstraints(statusIndicator, constraints);
      statusPanel.add(statusIndicator);

      statusLabel = new JTextField("Idle", 8);
      statusLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
      statusLabel.setEditable(false);

      buildConstraints(constraints, 1, 0, 1, 1, 80, 100);
      constraints.fill = GridBagConstraints.HORIZONTAL;
      constraints.anchor = GridBagConstraints.WEST;
      gridbag.setConstraints(statusLabel, constraints);
      statusPanel.add(statusLabel);

      buildConstraints(constraints, 0, 0, 1, 1, 4, 100);
      constraints.fill = GridBagConstraints.BOTH;
      constraints.anchor = GridBagConstraints.WEST;
      gridbag.setConstraints(statusPanel, constraints);
      northPanel.add(statusPanel);

      // Options
      optionsPanel = new JPanel();

      resource = resourceBundle.getResource("UpdateForm.label.Refresh");
      if (resource.equals(""))
         refreshCheckBox = new JCheckBox("Refresh Summary Table on Update", true);
      else
         refreshCheckBox = new JCheckBox(resource, true);
      optionsPanel.add(refreshCheckBox);

      buildConstraints(constraints, 1, 0, 1, 1, 90, 100);
      constraints.fill = GridBagConstraints.HORIZONTAL;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(optionsPanel, constraints);
      northPanel.add(optionsPanel);

      // Help
      helpPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));

      questionIcon = new ImageIcon(iconsDirectory + "bulbIcon.png");
      questionButton = new JButton(questionIcon);
      questionButton.setFocusPainted(false);
      questionButton.setBorder(BorderFactory.createRaisedBevelBorder());
      questionButton.addActionListener(this);
      helpPanel.add(questionButton);

      buildConstraints(constraints, 2, 0, 1, 1, 6, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.EAST;
      gridbag.setConstraints(helpPanel, constraints);
      northPanel.add(helpPanel);

      mainPanel.add(northPanel, BorderLayout.NORTH);

      // Update/Where Expression Components.

      updateWherePanel = new JPanel();
      updateWherePanel.setLayout(gridbag);
      updateWherePanel.setBorder(BorderFactory.createLoweredBevelBorder());

      createUpdateWhereInterface();

      buildConstraints(constraints, 0, 1, 1, 1, 100, 100);
      constraints.fill = GridBagConstraints.BOTH;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(updateWherePanel, constraints);
      formPanel.add(updateWherePanel);

      mainPanel.add(formPanel, BorderLayout.CENTER);

      // Creating Action Buttons.

      southPanel = new JPanel(gridbag);
      southPanel.setBorder(BorderFactory.createEtchedBorder());

      actionButtonPanel = new JPanel();

      // Find Button
      resource = resourceBundle.getResource("UpdateForm.button.Find");
      if (resource.equals(""))
         findButton = new JButton("Find");
      else
         findButton = new JButton(resource);
      findButton.setFocusPainted(false);
      actionButtonPanel.add(findButton);

      // Update Button
      resource = resourceBundle.getResource("UpdateForm.button.Update");
      if (resource.equals(""))
         updateButton = new JButton("Update");
      else
         updateButton = new JButton(resource);
      updateButton.setFocusPainted(false);
      updateButton.addActionListener(this);
      actionButtonPanel.add(updateButton);

      // Close Button
      resource = resourceBundle.getResource("UpdateForm.button.Close");
      if (resource.equals(""))
         closeButton = new JButton("Close");
      else
         closeButton = new JButton(resource);
      closeButton.setFocusPainted(false);
      closeButton.addActionListener(this);
      actionButtonPanel.add(closeButton);

      buildConstraints(constraints, 0, 0, 1, 1, 99, 100);
      constraints.fill = GridBagConstraints.BOTH;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(actionButtonPanel, constraints);
      southPanel.add(actionButtonPanel);

      clearPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
      clearPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

      // Clear Button
      clearIcon = new ImageIcon(iconsDirectory + "deleteIcon.png");
      clearButton = new JButton(clearIcon);
      clearButton.setFocusPainted(false);
      clearButton.setBorder(BorderFactory.createRaisedBevelBorder());
      clearButton.addActionListener(this);
      clearPanel.add(clearButton);

      buildConstraints(constraints, 1, 0, 1, 1, 1, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.EAST;
      gridbag.setConstraints(clearPanel, constraints);
      southPanel.add(clearPanel);

      mainPanel.add(southPanel, BorderLayout.SOUTH);
      getContentPane().add(mainPanel);
      (this.getRootPane()).setDefaultButton(findButton);

      // Dummy Button to Fire Update Event.
      disposeButton = new JButton();

      // Adding WindowListener
      this.addWindowListener(tableEntryFormFrameListener);
   }

   //==============================================================
   // WindowListener for insuring that the frame is closed. (x).
   //==============================================================

   private transient WindowListener tableEntryFormFrameListener = new WindowAdapter()
   {
      public void windowClosing(WindowEvent e)
      {
         dispose();
      }
   };

   //==============================================================
   // ActionEvent Listener method for detecting the inputs from
   // the panel and directing to the appropriate routine. Note all
   // actions associated with Find are handled from the calling
   // class TableTabPanel.
   //==============================================================

   public void actionPerformed(ActionEvent evt)
   {
      Object formSource = evt.getSource();

      if (formSource instanceof JButton)
      {
         // Question Button
         if (formSource == questionButton)
         {
            // Show a basic information file.
            HelpFrame informationFrame = new HelpFrame("Information", "/docs/Manual/AdvSortSearchInfo.html",
                                                       null);
            informationFrame.setSize(550, 360);
            informationFrame.center();
            informationFrame.setVisible(true);
         }

         // Update Button Action
         if (formSource == updateButton)
         {
            // Set status.
            statusIndicator.setEnabled(false);
            statusLabel.setText("Working");

            // Create a thread to handle the update.

            Thread processUpdateThread = new Thread(new Runnable()
            {
               public void run()
               {
                  if (updateTable())
                  {
                     // Notify Calling Panel to
                     // update table.
                     if (refreshCheckBox.isSelected())
                        disposeButton.doClick();
                  }
                  // Update status.
                  statusIndicator.setEnabled(true);
                  statusLabel.setText("Idle");
               }
            }, "UpdateForm.processUpdateThread");
            processUpdateThread.start();
         }

         // Clear Button Action
         if (formSource == clearButton)
         {
            statusIndicator.setEnabled(true);
            statusLabel.setText("Idle");
            refreshCheckBox.setSelected(true);
            statusIndicator.setEnabled(true);
            updateColumnComboBox.setSelectedIndex(0);
            updateColumnToTextField.setText("");
            quoteCheckBox.setSelected(true);
            whereComboBox1.setSelectedIndex(0);
            whereComboBox2.setSelectedIndex(0);
            whereComboBox3.setSelectedIndex(0);
            whereComboBox4.setSelectedIndex(0);
            whereComboBox5.setSelectedIndex(0);
            operatorComboBox1.setSelectedIndex(0);
            operatorComboBox2.setSelectedIndex(0);
            operatorComboBox3.setSelectedIndex(0);
            operatorComboBox4.setSelectedIndex(0);
            operatorComboBox5.setSelectedIndex(0);
            andOrComboBox1.setSelectedIndex(0);
            andOrComboBox2.setSelectedIndex(0);
            andOrComboBox3.setSelectedIndex(0);
            andOrComboBox4.setSelectedIndex(0);
            whereTextField1.setText("");
            whereTextField2.setText("");
            whereTextField3.setText("");
            whereTextField4.setText("");
            whereTextField5.setText("");
         }

         // Close Button Action
         if (formSource == closeButton)
         {
            // Keep form active with data just
            // hide. The form remains active
            // until a normal update is again
            // called for from the TableTabPanel.
            setVisible(false);
         }
      }
   }

   //==============================================================
   // Class Method for helping the parameters in gridbag.
   //==============================================================

   private void buildConstraints(GridBagConstraints gbc, int gx, int gy, int gw, int gh,
                                 double wx, double wy)
   {
      gbc.gridx = gx;
      gbc.gridy = gy;
      gbc.gridwidth = gw;
      gbc.gridheight = gh;
      gbc.weightx = wx;
      gbc.weighty = wy;
   }

   //==============================================================
   // Class method to center the frame.
   //========================================================

   protected void center()
   {
      Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
      Dimension us = getSize();
      int x = (screen.width - us.width) / 2;
      int y = (screen.height - us.height) / 2;
      setLocation(x, y);
   }

   //==============================================================
   // Class method to setup the Update/Where GUI Componenets.
   //==============================================================

   private void createUpdateWhereInterface()
   {
      // Method Instance
      JPanel updatePanel, wherePanel;
      String resource, resourceWhere;
      
      JLabel setLabel, withLabel;
      JLabel whereLabel1, whereLabel2, whereLabel3, whereLabel4, whereLabel5;

      Object[] whereOperators;
      Object[] mysqlWhereOperators = {"LIKE", "LIKE BINARY", "NOT LIKE", "REGEXP", "NOT REGEXP", "IS NULL",
                                      "IS NOT NULL", "=", "<=>", "!=", "<", "<=", ">=", ">"};
      Object[] postgreSQLWhereOperators = {"LIKE", "NOT LIKE", "SIMILAR TO", "NOT SIMILAR TO", "IS NULL",
                                           "IS NOT NULL", "~", "~*", "!~", "!~*", "=", "!=", "~=", "<", "<=",
                                           ">=", ">"};
      Object[] hsqlWhereOperators = {"LIKE", "NOT LIKE", "IS NULL", "IS NOT NULL", "IN", "NOT IN", "BETWEEN",
                                     "=", "<", "<=", ">", ">=", "<>", "!="};
      Object[] oracleWhereOperators = {"LIKE", "NOT LIKE", "REGEXP_LIKE", "IS NULL", "IS NOT NULL", "IS NAN",
                                       "IS NOT NAN", "IS INFINITE", "IS NOT INFINITE", "IS EMPTY",
                                       "IS NOT EMPTY", "IN", "NOT IN", "BETWEEN", "NOT BETWEEN", "EXISTS",
                                       "EQUALS_PATH", "UNDER_PATH", "=", "<", "<=", ">", ">=", "<>", "!=",
                                       "^="};

      // Assigning the appropriate string array WHERE operators.

      if (MyJSQLView_Access.getSubProtocol().equals("mysql"))
         whereOperators = mysqlWhereOperators;
      else if (MyJSQLView_Access.getSubProtocol().equals("postgresql"))
         whereOperators = postgreSQLWhereOperators;
      else if (MyJSQLView_Access.getSubProtocol().indexOf("oracle") != -1)
         whereOperators = oracleWhereOperators;
      // Make HSQL Default
      else
         whereOperators = hsqlWhereOperators;

      // ========================
      // Update Interface Setup.

      updatePanel = new JPanel();
      updatePanel.setLayout(gridbag);
      updatePanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLoweredBevelBorder(),
                                           BorderFactory.createEmptyBorder(10, 6, 10, 6)));

      resource = resourceBundle.getResource("UpdateForm.label.Update");
      if (resource.equals(""))
         setLabel = new JLabel("Update : ", JLabel.LEADING);
      else
         setLabel = new JLabel(resource + " : ", JLabel.LEADING);

      buildConstraints(constraints, 0, 0, 1, 1, 100, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.WEST;
      gridbag.setConstraints(setLabel, constraints);
      updatePanel.add(setLabel);

      updateColumnComboBox = new JComboBox(new Vector(comboBoxColumnNames));
      updateColumnComboBox.removeItemAt(0);

      buildConstraints(constraints, 1, 0, 2, 1, 100, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.WEST;
      gridbag.setConstraints(updateColumnComboBox, constraints);
      updatePanel.add(updateColumnComboBox);

      resource = resourceBundle.getResource("UpdateForm.label.With");
      if (resource.equals(""))
         withLabel = new JLabel(" With ", JLabel.LEADING);
      else
         withLabel = new JLabel(" " + resource + " ", JLabel.LEADING);

      buildConstraints(constraints, 3, 0, 1, 1, 100, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.WEST;
      gridbag.setConstraints(withLabel, constraints);
      updatePanel.add(withLabel);

      updateColumnToTextField = new JTextField(15);

      buildConstraints(constraints, 4, 0, 1, 1, 100, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(updateColumnToTextField, constraints);
      updatePanel.add(updateColumnToTextField);

      resource = resourceBundle.getResource("UpdateForm.checkbox.Quote");
      if (resource.equals(""))
         quoteCheckBox = new JCheckBox("Quote", true);
      else
         quoteCheckBox = new JCheckBox(resource, true);

      buildConstraints(constraints, 5, 0, 1, 1, 100, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(quoteCheckBox, constraints);
      updatePanel.add(quoteCheckBox);

      buildConstraints(constraints, 0, 0, 1, 1, 100, 20);
      constraints.fill = GridBagConstraints.BOTH;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(updatePanel, constraints);
      updateWherePanel.add(updatePanel);

      // ============================
      // Where Interface Setup

      wherePanel = new JPanel();
      wherePanel.setLayout(gridbag);
      wherePanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLoweredBevelBorder(),
                                         BorderFactory.createEmptyBorder(10, 6, 10, 6)));

      resourceWhere = resourceBundle.getResource("UpdateForm.label.Where");
      if (resourceWhere.equals(""))
         whereLabel1 = new JLabel("Where : ", JLabel.LEFT);
      else
         whereLabel1 = new JLabel(resourceWhere + " : ", JLabel.LEFT);

      buildConstraints(constraints, 0, 3, 1, 1, 100, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(whereLabel1, constraints);
      wherePanel.add(whereLabel1);

      whereComboBox1 = new JComboBox(comboBoxColumnNames);
      stateComponents.addElement(whereComboBox1);

      buildConstraints(constraints, 1, 3, 1, 1, 100, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(whereComboBox1, constraints);
      wherePanel.add(whereComboBox1);

      operatorComboBox1 = new JComboBox(whereOperators);
      stateComponents.addElement(operatorComboBox1);

      buildConstraints(constraints, 2, 3, 1, 1, 100, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(operatorComboBox1, constraints);
      wherePanel.add(operatorComboBox1);

      whereTextField1 = new JTextField(15);
      stateComponents.addElement(whereTextField1);

      buildConstraints(constraints, 3, 3, 1, 1, 100, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(whereTextField1, constraints);
      wherePanel.add(whereTextField1);

      andOrComboBox1 = new JComboBox();
      andOrComboBox1.addItem("And");
      andOrComboBox1.addItem("Or");
      stateComponents.addElement(andOrComboBox1);

      buildConstraints(constraints, 4, 3, 1, 1, 100, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(andOrComboBox1, constraints);
      wherePanel.add(andOrComboBox1);

      if (resourceWhere.equals(""))
         whereLabel2 = new JLabel("Where : ", JLabel.LEFT);
      else
         whereLabel2 = new JLabel(resourceWhere + " : ", JLabel.LEFT);

      buildConstraints(constraints, 0, 4, 1, 1, 100, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(whereLabel2, constraints);
      wherePanel.add(whereLabel2);

      whereComboBox2 = new JComboBox(comboBoxColumnNames);
      stateComponents.addElement(whereComboBox2);

      buildConstraints(constraints, 1, 4, 1, 1, 100, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(whereComboBox2, constraints);
      wherePanel.add(whereComboBox2);

      operatorComboBox2 = new JComboBox(whereOperators);
      stateComponents.addElement(operatorComboBox2);

      buildConstraints(constraints, 2, 4, 1, 1, 100, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(operatorComboBox2, constraints);
      wherePanel.add(operatorComboBox2);

      whereTextField2 = new JTextField(15);
      stateComponents.addElement(whereTextField2);

      buildConstraints(constraints, 3, 4, 1, 1, 100, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(whereTextField2, constraints);
      wherePanel.add(whereTextField2);

      andOrComboBox2 = new JComboBox();
      andOrComboBox2.addItem("And");
      andOrComboBox2.addItem("Or");
      stateComponents.addElement(andOrComboBox2);

      buildConstraints(constraints, 4, 4, 1, 1, 100, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(andOrComboBox2, constraints);
      wherePanel.add(andOrComboBox2);

      if (resourceWhere.equals(""))
         whereLabel3 = new JLabel("Where : ", JLabel.LEFT);
      else
         whereLabel3 = new JLabel(resourceWhere + " : ", JLabel.LEFT);

      buildConstraints(constraints, 0, 5, 1, 1, 100, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(whereLabel3, constraints);
      wherePanel.add(whereLabel3);

      whereComboBox3 = new JComboBox(comboBoxColumnNames);
      stateComponents.addElement(whereComboBox3);

      buildConstraints(constraints, 1, 5, 1, 1, 100, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(whereComboBox3, constraints);
      wherePanel.add(whereComboBox3);

      operatorComboBox3 = new JComboBox(whereOperators);
      stateComponents.addElement(operatorComboBox3);

      buildConstraints(constraints, 2, 5, 1, 1, 100, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(operatorComboBox3, constraints);
      wherePanel.add(operatorComboBox3);

      whereTextField3 = new JTextField(15);
      stateComponents.addElement(whereTextField3);

      buildConstraints(constraints, 3, 5, 1, 1, 100, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(whereTextField3, constraints);
      wherePanel.add(whereTextField3);

      andOrComboBox3 = new JComboBox();
      andOrComboBox3.addItem("And");
      andOrComboBox3.addItem("Or");
      stateComponents.addElement(andOrComboBox3);

      buildConstraints(constraints, 4, 5, 1, 1, 100, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(andOrComboBox3, constraints);
      wherePanel.add(andOrComboBox3);

      if (resourceWhere.equals(""))
         whereLabel4 = new JLabel("Where : ", JLabel.LEFT);
      else
         whereLabel4 = new JLabel(resourceWhere + " : ", JLabel.LEFT);

      buildConstraints(constraints, 0, 6, 1, 1, 100, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(whereLabel4, constraints);
      wherePanel.add(whereLabel4);

      whereComboBox4 = new JComboBox(comboBoxColumnNames);
      stateComponents.addElement(whereComboBox4);

      buildConstraints(constraints, 1, 6, 1, 1, 100, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(whereComboBox4, constraints);
      wherePanel.add(whereComboBox4);

      operatorComboBox4 = new JComboBox(whereOperators);
      stateComponents.addElement(operatorComboBox4);

      buildConstraints(constraints, 2, 6, 1, 1, 100, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(operatorComboBox4, constraints);
      wherePanel.add(operatorComboBox4);

      whereTextField4 = new JTextField(15);
      stateComponents.addElement(whereTextField4);

      buildConstraints(constraints, 3, 6, 1, 1, 100, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(whereTextField4, constraints);
      wherePanel.add(whereTextField4);

      andOrComboBox4 = new JComboBox();
      andOrComboBox4.addItem("And");
      andOrComboBox4.addItem("Or");
      stateComponents.addElement(andOrComboBox4);

      buildConstraints(constraints, 4, 6, 1, 1, 100, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(andOrComboBox4, constraints);
      wherePanel.add(andOrComboBox4);

      if (resourceWhere.equals(""))
         whereLabel5 = new JLabel("Where : ", JLabel.LEFT);
      else
         whereLabel5 = new JLabel(resourceWhere + " : ", JLabel.LEFT);
         
      buildConstraints(constraints, 0, 7, 1, 1, 100, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(whereLabel5, constraints);
      wherePanel.add(whereLabel5);

      whereComboBox5 = new JComboBox(comboBoxColumnNames);
      stateComponents.addElement(whereComboBox5);

      buildConstraints(constraints, 1, 7, 1, 1, 100, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(whereComboBox5, constraints);
      wherePanel.add(whereComboBox5);

      operatorComboBox5 = new JComboBox(whereOperators);

      buildConstraints(constraints, 2, 7, 1, 1, 100, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(operatorComboBox5, constraints);
      wherePanel.add(operatorComboBox5);

      whereTextField5 = new JTextField(15);
      stateComponents.addElement(whereTextField5);
      stateComponents.addElement(operatorComboBox5);

      buildConstraints(constraints, 3, 7, 1, 1, 100, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(whereTextField5, constraints);
      wherePanel.add(whereTextField5);

      buildConstraints(constraints, 0, 1, 1, 1, 100, 80);
      constraints.fill = GridBagConstraints.BOTH;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(wherePanel, constraints);
      updateWherePanel.add(wherePanel);
   }

   //==============================================================
   // Class method to obtain the data from the form to be used to
   // create the required SQL statement to update the table.
   //==============================================================

   private boolean updateTable()
   {
      // Method Instances.
      String columnName, columnClass, columnType;
      String sqlStatementString;
      Statement sqlStatement;
      ResultSet db_resultSet;

      InputDialog updateDialog;
      int updateRowCount, columnSize;
      String updateTextString, updateString;
      String dateString, timeString;
      String resourceMessage1, resourceMessage2, resourceTitle, resourceOK, resourceCancel;
      boolean tableUpdated, tryingUpdate;

      // Obtain connection to database & setup.

      Connection dbConnection = MyJSQLView_Access.getConnection("UpdateForm updateTable()");

      if (dbConnection == null)
         return false;

      // Keep track of update and if a attempt
      // was made.
      tableUpdated = false;
      tryingUpdate = false;

      try
      {
         // Create and initial UPDATE warning count dialog for
         // confirmation to continue with update.

         sqlStatement = dbConnection.createStatement();
         sqlStatementString = "SELECT COUNT(*) FROM " + sqlTable + " " + getWhereSQLExpression();
         // System.out.println(sqlStatementString);

         // Obtain row count to update.
         db_resultSet = sqlStatement.executeQuery(sqlStatementString);
         db_resultSet.next();
         updateRowCount = db_resultSet.getInt(1);
         db_resultSet.close();

         // Show dialog as needed.
         if (updateRowCount != 0)
         {
            JLabel message;
            String messageUpdate, messageRows; 
            
            resourceMessage1 = resourceBundle.getResource("UpdateForm.dialogmessage.Update");
            if (resourceMessage1.equals(""))
               messageUpdate = "Update";
            else
               messageUpdate = resourceMessage1;
            resourceMessage2 = resourceBundle.getResource("UpdateForm.dialogmessage.Rows");
            if (resourceMessage2.equals(""))
               messageRows = "Rows(s)?";
            else
               messageRows = resourceMessage2;
            
            message = new JLabel(messageUpdate + " " + updateRowCount + " " + messageRows, JLabel.CENTER);
            Object[] content = {message};
            
            resourceTitle = resourceBundle.getResource("UpdateForm.dialogtitle.AlertDialog");
            resourceOK = resourceBundle.getResource("UpdateForm.dialogbutton.OK");
            resourceCancel = resourceBundle.getResource("UpdateForm.dialogbutton.Cancel");
            
            if (resourceTitle.equals("") || resourceOK.equals("") || resourceCancel.equals(""))
               updateDialog = new InputDialog(this, "Alert Dialog", "ok", "cancel", content, deleteDataIcon);
            else
               updateDialog = new InputDialog(this, resourceTitle, resourceOK, resourceCancel,
                                              content, deleteDataIcon);
            updateDialog.pack();
            updateDialog.setLocation(this.getLocation().x + this.getWidth() / 4, this.getLocation().y
                                                                                 + this.getHeight() / 4);
            // updateDialog.center();
            updateDialog.setResizable(false);
            updateDialog.setVisible(true);

            // Proceed with the UPDATE to the row(s) in the table with the
            // desired new content data.

            if (updateDialog.isActionResult())
            {
               dbConnection.setAutoCommit(false);
               tryingUpdate = true;

               // HSQL & Oracle does not support.
               if (MyJSQLView_Access.getSubProtocol().indexOf("hsql") == -1
                   && MyJSQLView_Access.getSubProtocol().indexOf("oracle") == -1)
                  sqlStatement.executeUpdate("BEGIN");

               // Setup some instances needed for processing.
               updateTextString = updateColumnToTextField.getText();
               columnName = (String) updateColumnComboBox.getSelectedItem();
               columnClass = (String) columnClassHashMap.get(columnName);
               columnType = (String) columnTypeHashMap.get(columnName);
               columnSize = ((Integer) columnSizeHashMap.get(columnName)).intValue();
               //System.out.println(updateTextString + " " + columnName + " " + columnClass + " " + columnType
               //                   + " " + columnSize);

               // Create basic initial SQL.
               sqlStatementString = ("UPDATE " + sqlTable + " SET " + identifierQuoteString
                                     + columnNamesHashMap.get(columnName) + identifierQuoteString + "=");

               // Obtain Update Text.

               // Default
               if (updateTextString.equals(""))
                  updateString = "DEFAULT";

               // NULL
               else if (updateTextString.toLowerCase().equals("null"))
                  updateString = "NULL";

               // User Input
               else
               {
                  // Date Type Fields
                  if (columnClass.indexOf("Date") != -1 || (columnClass.toUpperCase()).indexOf("TIME") != -1)
                  {
                     try
                     {
                        // Date
                        if (columnType.equals("DATE"))
                        {
                           Date dateValue;
                           updateTextString = updateTextString.trim();

                           // Check for some kind of valid input.
                           if (updateTextString.length() != 10)
                              java.sql.Date.valueOf("error");
                           
                           // Process
                           dateString = updateTextString.substring(0, 10);
                           dateString = MyJSQLView_Utils.formatJavaDateString(dateString);
                           dateValue = java.sql.Date.valueOf(dateString);
                           updateString = dateValue.toString();
                        }
                        // Time
                        else if (columnType.equals("TIME") || columnType.equals("TIMETZ"))
                        {
                           Time timeValue;
                           updateTextString = updateTextString.trim();
                           
                           // Check for some kind of valid input.
                           if (updateTextString.length() < 8)
                              timeValue = Time.valueOf("error");
                           
                           // Process
                           timeValue = Time.valueOf(updateTextString.substring(0, 7));
                           updateString = timeValue.toString();
                        }
                        // DateTime
                        else if (columnType.equals("DATETIME"))
                        {
                           Timestamp dateTimeValue;
                           dateString = "";
                           timeString = "";
                           updateTextString = updateTextString.trim();

                           // Check for some kind of valid input.
                           if (updateTextString.indexOf(" ") == -1 || updateTextString.length() < 10)
                              java.sql.Date.valueOf("error");
                        
                           // Process
                           updateTextString = updateTextString.substring(0, 10);
                           dateString = MyJSQLView_Utils.formatJavaDateString(updateTextString);
                           timeString = updateTextString.substring(updateTextString.indexOf(" "));
                           dateTimeValue = Timestamp.valueOf(dateString + timeString);
                           updateString = dateTimeValue.toString();
                        }
                        // Timestamp
                        else if (columnType.equals("TIMESTAMP") || columnType.equals("TIMESTAMPTZ")
                                 || columnType.equals("TIMESTAMPLTZ"))
                        {
                           if (columnType.equals("TIMESTAMPLTZ"))
                              MyJSQLView_Utils.setLocalTimeZone(sqlStatement);

                           SimpleDateFormat timeStampFormat;
                           Timestamp dateTimeValue;
                           Date dateParse;

                           try
                           {
                              // Create a Timestamp Format.
                              if (columnType.equals("TIMESTAMP"))
                              {
                                 if (columnSize == 2)
                                    timeStampFormat = new SimpleDateFormat("yy");
                                 else if (columnSize == 4)
                                    timeStampFormat = new SimpleDateFormat("MM-yy");
                                 else if (columnSize == 6)
                                    timeStampFormat = new SimpleDateFormat("MM-dd-yy");
                                 else if (columnSize == 8)
                                    timeStampFormat = new SimpleDateFormat("MM-dd-yyyy");
                                 else if (columnSize == 10)
                                    timeStampFormat = new SimpleDateFormat("MM-dd-yy HH:mm");
                                 else if (columnSize == 12)
                                    timeStampFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm");
                                 else
                                    timeStampFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
                              }
                              else
                              {
                                 if (columnType.equals("TIMESTAMPLTZ"))
                                    timeStampFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss Z");
                                 else
                                    timeStampFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss z");
                              }

                              // Parse the TimeStamp Format.
                              if (columnType.equals("TIMESTAMPLTZ"))
                              {
                                 dateString = updateTextString.trim();
                                 dateString = dateString.substring(0, dateString.lastIndexOf(':'))
                                              + dateString.substring(dateString.lastIndexOf(':') + 1);
                                 dateParse = timeStampFormat.parse(dateString);
                              }
                              else
                                 dateParse = timeStampFormat.parse(updateTextString.trim());

                              dateTimeValue = new Timestamp(dateParse.getTime());
                              updateString = dateTimeValue.toString();
                           }
                           catch (ParseException e)
                           {
                              throw (new IllegalArgumentException(e + ""));
                           }
                        }
                        // Must be Year
                        else
                        {
                           dateString = updateTextString.trim();
                           java.sql.Date yearValue = java.sql.Date.valueOf(dateString + "-01-01");
                           updateString = yearValue.toString().substring(0, 4);
                        }
                     }
                     catch (IllegalArgumentException e)
                     {
                        resourceMessage1 = resourceBundle.getResource("UpdateForm.dialogmessage.InvalidDateTime");
                        resourceMessage2 = resourceBundle.getResource("UpdateForm.dialogmessage.ColumnType");
                        resourceTitle = resourceBundle.getResource("UpdateForm.dialogtitle.Alert");
                        
                        if (resourceMessage1.equals("") || resourceMessage2.equals("")
                            || resourceTitle.equals(""))
                           JOptionPane.showMessageDialog(null, "Invalid Date/Time Input for Field " + columnName
                                                         + ", Type: " + columnType, "Alert",
                                                         JOptionPane.ERROR_MESSAGE);
                        else
                           JOptionPane.showMessageDialog(null, resourceMessage1 + " " + columnName
                                                         + ", " + resourceMessage2 + ": " + columnType,
                                                         resourceTitle, JOptionPane.ERROR_MESSAGE);
                        sqlStatement.close();
                        dbConnection.setAutoCommit(true);
                        MyJSQLView_Access.closeConnection(dbConnection,
                                                          "TableEntryForm addUpdateTableEntry()");
                        return false;
                     }
                  }
                  // None Processed Field.
                  else
                     updateString = updateTextString;
               }

               // Finalizing SQL statement by addeding identifier
               // quotes as needed and adding WHERE.

               if (quoteCheckBox.isSelected() && !updateString.equals("DEFAULT")
                   && !updateString.equals("NULL"))
                  sqlStatementString += "'" + updateString + "'" + " " + getWhereSQLExpression();
               else
                  sqlStatementString += updateString + " " + getWhereSQLExpression();
               // System.out.println(sqlStatementString);

               // Proceed with execution and finish up.
               sqlStatement.executeUpdate(sqlStatementString);
               dbConnection.commit();
               dbConnection.setAutoCommit(true);

               tableUpdated = true;
            }
         }
         sqlStatement.close();
      }
      catch (SQLException e)
      {
         MyJSQLView_Access.displaySQLErrors(e, "UpdateForm updateTable()");
         if (tryingUpdate)
         {
            try
            {
               dbConnection.rollback();
               dbConnection.setAutoCommit(true);
            }
            catch (SQLException error)
            {
               MyJSQLView_Access.displaySQLErrors(e, "UpdateForm updateTable() rollback failed");
            }
         }
      }

      // Close the connection and return results.
      MyJSQLView_Access.closeConnection(dbConnection, "UpdateForm updateTable()");
      return tableUpdated;
   }

   //==============================================================
   // Class method to get the SQL statement string that corresponds
   // to the WHERE aspect of the update.
   //==============================================================

   private String getWhereSQLExpression()
   {
      String sqlStatementString;
      String columnNameString, operatorString, tempSearchString;
      String unionString;

      sqlStatementString = "";

      // ========================================
      // Adding the search(s), WHERE, option.

      columnNameString = (String) columnNamesHashMap.get(whereComboBox1.getSelectedItem());
      operatorString = (String) operatorComboBox1.getSelectedItem();
      tempSearchString = whereTextField1.getText();
      unionString = "";

      if (columnNameString != null
          && (!tempSearchString.equals("") || operatorString.toLowerCase().indexOf("null") != -1))
      {
         if (operatorString.toLowerCase().indexOf("null") != -1)
            sqlStatementString += "WHERE " + identifierQuoteString + columnNameString + identifierQuoteString
                                  + " " + operatorString + " ";
         else
         {
            if (operatorString.equals("<=>") && tempSearchString.toLowerCase().equals("null"))
               sqlStatementString += "WHERE " + identifierQuoteString + columnNameString
                                     + identifierQuoteString + " " + operatorString + " " + tempSearchString
                                     + " ";
            else
               sqlStatementString += "WHERE " + identifierQuoteString + columnNameString
                                     + identifierQuoteString + " " + operatorString + " '" + tempSearchString
                                     + "' ";
         }

         unionString = ((String) andOrComboBox1.getSelectedItem()).toUpperCase() + " ";
      }

      columnNameString = (String) columnNamesHashMap.get(whereComboBox2.getSelectedItem());
      operatorString = (String) operatorComboBox2.getSelectedItem();
      tempSearchString = whereTextField2.getText();

      if (columnNameString != null
          && (!tempSearchString.equals("") || operatorString.toLowerCase().indexOf("null") != -1))
      {
         sqlStatementString += unionString.equals("") ? "WHERE " : unionString;

         if (operatorString.toLowerCase().indexOf("null") != -1)
            sqlStatementString += identifierQuoteString + columnNameString + identifierQuoteString + " "
                                  + operatorString + " ";
         else
         {
            if (operatorString.equals("<=>") && tempSearchString.toLowerCase().equals("null"))
               sqlStatementString += identifierQuoteString + columnNameString + identifierQuoteString + " "
                                     + operatorString + " " + tempSearchString + " ";
            else
               sqlStatementString += identifierQuoteString + columnNameString + identifierQuoteString + " "
                                     + operatorString + " '" + tempSearchString + "' ";
         }

         unionString = ((String) andOrComboBox2.getSelectedItem()).toUpperCase() + " ";
      }

      columnNameString = (String) columnNamesHashMap.get(whereComboBox3.getSelectedItem());
      operatorString = (String) operatorComboBox3.getSelectedItem();
      tempSearchString = whereTextField3.getText();

      if (columnNameString != null
          && (!tempSearchString.equals("") || operatorString.toLowerCase().indexOf("null") != -1))
      {
         sqlStatementString += unionString.equals("") ? "WHERE " : unionString;

         if (operatorString.toLowerCase().indexOf("null") != -1)
            sqlStatementString += identifierQuoteString + columnNameString + identifierQuoteString + " "
                                  + operatorString + " ";
         else
         {
            if (operatorString.equals("<=>") && tempSearchString.toLowerCase().equals("null"))
               sqlStatementString += identifierQuoteString + columnNameString + identifierQuoteString + " "
                                     + operatorString + " " + tempSearchString + " ";
            else
               sqlStatementString += identifierQuoteString + columnNameString + identifierQuoteString + " "
                                     + operatorString + " '" + tempSearchString + "' ";
         }

         unionString = ((String) andOrComboBox3.getSelectedItem()).toUpperCase() + " ";
      }

      columnNameString = (String) columnNamesHashMap.get(whereComboBox4.getSelectedItem());
      operatorString = (String) operatorComboBox4.getSelectedItem();
      tempSearchString = whereTextField4.getText();

      if (columnNameString != null
          && (!tempSearchString.equals("") || operatorString.toLowerCase().indexOf("null") != -1))
      {
         sqlStatementString += unionString.equals("") ? "WHERE " : unionString;

         if (operatorString.toLowerCase().indexOf("null") != -1)
            sqlStatementString += identifierQuoteString + columnNameString + identifierQuoteString + " "
                                  + operatorString + " ";
         else
         {
            if (operatorString.equals("<=>") && tempSearchString.toLowerCase().equals("null"))
               sqlStatementString += identifierQuoteString + columnNameString + identifierQuoteString + " "
                                     + operatorString + " " + tempSearchString + " ";
            else
               sqlStatementString += identifierQuoteString + columnNameString + identifierQuoteString + " "
                                     + operatorString + " '" + tempSearchString + "' ";
         }

         unionString = ((String) andOrComboBox4.getSelectedItem()).toUpperCase() + " ";
      }

      columnNameString = (String) columnNamesHashMap.get(whereComboBox5.getSelectedItem());
      operatorString = (String) operatorComboBox5.getSelectedItem();
      tempSearchString = whereTextField5.getText();

      if (columnNameString != null
          && (!tempSearchString.equals("") || operatorString.toLowerCase().indexOf("null") != -1))
      {
         sqlStatementString += unionString.equals("") ? "WHERE " : unionString;

         if (operatorString.toLowerCase().indexOf("null") != -1)
            sqlStatementString += identifierQuoteString + columnNameString + identifierQuoteString + " "
                                  + operatorString + " ";
         else
         {
            if (operatorString.equals("<=>") && tempSearchString.toLowerCase().equals("null"))
               sqlStatementString += identifierQuoteString + columnNameString + identifierQuoteString + " "
                                     + operatorString + " " + tempSearchString + " ";
            else
               sqlStatementString += identifierQuoteString + columnNameString + identifierQuoteString + " "
                                     + operatorString + " '" + tempSearchString + "' ";
         }
      }

      return sqlStatementString;
   }

   //==============================================================
   // Class method for outside classes to obtain the current state
   // of the form. WARNING replicates the Advanced SortSearch Form,
   // returns and empty sort state and then the where state only.
   //==============================================================

   protected String getKeyComponentsState()
   {
      // Method Instances
      StringBuffer stateString;
      String delimiter;
      Iterator keyComponentIterator;
      Object currentComponent;

      // Initialize and obtain key components state.

      stateString = new StringBuffer("");
      delimiter = AdvancedSortSearchForm.getKeyComponentsDelimiter();

      // Replicate the AdvancedSortSearchForm SORT empty state form

      stateString.append("0" + delimiter + "0" + delimiter + "0" + delimiter + "0" + delimiter + "0"
                         + delimiter + "0" + delimiter + "0" + delimiter);

      // Cycle through the WHERE components to add to the state string.

      keyComponentIterator = stateComponents.iterator();

      while (keyComponentIterator.hasNext())
      {
         currentComponent = keyComponentIterator.next();

         if (currentComponent instanceof JComboBox)
            stateString.append(((JComboBox) currentComponent).getSelectedIndex() + delimiter);

         if (currentComponent instanceof JTextField)
            stateString.append(((JTextField) currentComponent).getText() + delimiter);
      }
      // System.out.println(stateString.toString());
      return stateString.toString();
   }
}