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
// Copyright (C) 2005-2011 Dana M. Proctor
// Version 3.8 06/11/2011
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
//         2.4 03/30/2010 Implemented Capability to Perform Updates on Oracle Date and Timestamp
//                        Data Types From WHERE Clause. Class Method getWhereSQLExpression().
//                        Code Needs Consolidated.
//         2.5 04/01/2010 Consolidated Code in createUpdateWhereInterface(), Constructor, 
//                        actionPerformed(), and getWhereSQLExpression(). Replaced Comboboxes,
//                        and Textfields With Arrays.
//         2.6 04/02/2010 Minor Comment Changes and Move of Instance unionString to Outside
//                        do Loop in Method getWhereSQLExpression().
//         2.7 04/06/2010 Class Method updateTable() Correction in Processing DateTime Types.
//                        Same Method Addition of Bit Type Processing for MySQL. Introduction
//                        of Method Instance quoteCheckBoxState.
//         2.8 04/06/2010 Class Method updateTable() Addition of TO_DATE() and TO_TIMESTAMP()
//                        Processing for Oracle Database.
//         2.9 04/06/2010 Commented System.out in UpdateTable().
//         3.0 05/18/2010 Parameterized Class Instances columnNamesHashMap, columnTypeHashMap,
//                        columnSizeHashMap, comboBoxColumnNames & stateComponents in Order to
//                        Bring Code Into Compliance With Java 5.0 API. Also the Same With
//                        Constructor Arguments. Changed Method swapEndComponent From Object
//                        to JComponent in createUpdateWhereInterface().
//         3.1 05/19/2010 Parameterized keyComponentIterator in Class Method getKeyComponentsState().
//         3.2 07/27/2010 Updated Method updateTable() Removed BEGIN Statement SQL Query
//                        Execution for SQLite Database.
//         3.3 08/27/2010 Added sqliteWhereOperators in Method createUpdateWhereInterface().
//         3.4 01/14/2011 Class Method getWhereSQLExpression() Changes to Give the Ability to
//                        Properly Search Given Input for Date/DateTime/Timestamp Fields.
//         3.5 01/15/2011 Class Method updateTable() Cast Object Returned by MyJSQLView_Access.
//                        getConnection() to Connection.
//         3.6 01/26/2011 Changes to Class Method updateTable() to Use Newly Redefined
//                        ConnectionManager to Collect Connections & Display SQL Errors. Also
//                        identifierQuoteString Collected From ConnectionManager. Added
//                        Class Instance subProtocol.
//         3.7 03/11/2011 Class Method getWhereSQLExpression() Minor Changes to Timestamp Type
//                        sqlStatementString.
//         3.8 06/11/2011 Replaced Class Instance subProtocol With dataSourceType. Methods Effected
//                        createUpdateWhereInterface(), updateTable(), & getWhereSQLExpression().
//
//-----------------------------------------------------------------
//                 danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.*;

/**
 *    This class provides a generic form that is used by each TableTabPanel to
 * execute a SQL update statement on the current table.
 * 
 * @author Dana M. Proctor
 * @version 3.8 06/11/2011
 */

class UpdateForm extends JFrame implements ActionListener
{
   // Class Instances.
   private static final long serialVersionUID = -3252154506926965611L;

   private String sqlTable;
   private String dataSourceType;
   private String identifierQuoteString;
   private HashMap<String, String> columnNamesHashMap;
   private HashMap<String, String> columnClassHashMap;
   private HashMap<String, String> columnTypeHashMap;
   private HashMap<String, Integer> columnSizeHashMap;
   private Vector<String> comboBoxColumnNames;
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

   private static final int updateFormExpressionNumber = 5;
   private JComboBox[] whereComboBox, operatorComboBox, andOrComboBox;
   private JTextField[] whereTextField;
   private Vector<JComponent> stateComponents;

   private JButton updateButton, closeButton, clearButton;
   protected JButton findButton, disposeButton;

   private ImageIcon deleteDataIcon;

   //==============================================================
   // UpdateForm Constructor
   //==============================================================

   protected UpdateForm(String table, MyJSQLView_ResourceBundle resourceBundle,
                        HashMap<String, String> columnNamesHashMap,
                        HashMap<String, String> columnClassHashMap,
                        HashMap<String, String> columnTypeHashMap,
                        HashMap<String, Integer> columnSizeHashMap,
                        Vector<String> comboBoxColumnNames)
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

      dataSourceType = ConnectionManager.getDataSourceType();
      iconsDirectory = MyJSQLView_Utils.getIconsDirectory() + MyJSQLView_Utils.getFileSeparator();
      identifierQuoteString = ConnectionManager.getIdentifierQuoteString();

      statusIdleIcon = new ImageIcon(iconsDirectory + "statusIdleIcon.png");
      statusWorkingIcon = new ImageIcon(iconsDirectory + "statusWorkingIcon.png");
      deleteDataIcon = new ImageIcon(iconsDirectory + "deleteDataIcon.gif");
      
      whereComboBox = new JComboBox[updateFormExpressionNumber];
      operatorComboBox = new JComboBox[updateFormExpressionNumber];
      andOrComboBox = new JComboBox[whereComboBox.length - 1];
      whereTextField = new JTextField[updateFormExpressionNumber];
      
      stateComponents = new Vector <JComponent>();

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
            
            int i = 0;
            do
            {
               whereComboBox[i].setSelectedIndex(0);
               operatorComboBox[i].setSelectedIndex(0);
               if (i < andOrComboBox.length)
                  andOrComboBox[i].setSelectedIndex(0);
               whereTextField[i].setText("");
               
               i++;
            }
            while (i < updateFormExpressionNumber);
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
      JLabel[] whereLabel;
      //JLabel whereLabel1, whereLabel2, whereLabel3, whereLabel4, whereLabel5;
      JComponent swapEndComponent;

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
      Object[] sqliteWhereOperators = {"LIKE", "NOT LIKE", "IS NULL", "IS NOT NULL", "IS NOT", "IN", "NOT IN",
                                       "OR", "|", "AND", "&", "||", "BETWEEN", "GLOB", "REGEXP", "MATCH",  
                                       "=", "<", "<<", "<=", ">", ">>", ">=", "<>", "!=", "==", };

      // Assigning the appropriate string array WHERE operators.

      if (dataSourceType.equals(ConnectionManager.MYSQL))
         whereOperators = mysqlWhereOperators;
      else if (dataSourceType.equals(ConnectionManager.POSTGRESQL))
         whereOperators = postgreSQLWhereOperators;
      else if (dataSourceType.equals(ConnectionManager.ORACLE))
         whereOperators = oracleWhereOperators;
      else if (dataSourceType.equals(ConnectionManager.SQLITE))
         whereOperators = sqliteWhereOperators;
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

      updateColumnComboBox = new JComboBox(comboBoxColumnNames);
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
      
      whereLabel = new JLabel[updateFormExpressionNumber];
      resourceWhere = resourceBundle.getResource("UpdateForm.label.Where");
      
      int i = 0;
      do
      {
         if (resourceWhere.equals(""))
            whereLabel[i] = new JLabel("Where : ", JLabel.LEFT);
         else
            whereLabel[i] = new JLabel(resourceWhere + " : ", JLabel.LEFT);

         buildConstraints(constraints, 0, (i + 3), 1, 1, 100, 100);
         constraints.fill = GridBagConstraints.NONE;
         constraints.anchor = GridBagConstraints.CENTER;
         gridbag.setConstraints(whereLabel[i], constraints);
         wherePanel.add(whereLabel[i]);

         whereComboBox[i] = new JComboBox(comboBoxColumnNames);
         stateComponents.addElement(whereComboBox[i]);

         buildConstraints(constraints, 1, (i + 3), 1, 1, 100, 100);
         constraints.fill = GridBagConstraints.NONE;
         constraints.anchor = GridBagConstraints.CENTER;
         gridbag.setConstraints(whereComboBox[i], constraints);
         wherePanel.add(whereComboBox[i]);

         operatorComboBox[i] = new JComboBox(whereOperators);
         stateComponents.addElement(operatorComboBox[i]);

         buildConstraints(constraints, 2, (i + 3), 1, 1, 100, 100);
         constraints.fill = GridBagConstraints.NONE;
         constraints.anchor = GridBagConstraints.CENTER;
         gridbag.setConstraints(operatorComboBox[i], constraints);
         wherePanel.add(operatorComboBox[i]);

         whereTextField[i] = new JTextField(15);
         stateComponents.addElement(whereTextField[i]);

         buildConstraints(constraints, 3, (i + 3), 1, 1, 100, 100);
         constraints.fill = GridBagConstraints.NONE;
         constraints.anchor = GridBagConstraints.CENTER;
         gridbag.setConstraints(whereTextField[i], constraints);
         wherePanel.add(whereTextField[i]);

         if (i < andOrComboBox.length)
         {
            andOrComboBox[i] = new JComboBox();
            andOrComboBox[i].addItem("And");
            andOrComboBox[i].addItem("Or");
            stateComponents.addElement(andOrComboBox[i]);

            buildConstraints(constraints, 4, (i + 3), 1, 1, 100, 100);
            constraints.fill = GridBagConstraints.NONE;
            constraints.anchor = GridBagConstraints.CENTER;
            gridbag.setConstraints(andOrComboBox[i], constraints);
            wherePanel.add(andOrComboBox[i]);
         }
         i++;
      }
      while (i < updateFormExpressionNumber);
      
      // Ok whats going on here? Well the Update Form uses the AdvancedSortSearchForm
      // to perform the search. Data from this panel on find is sent to that form
      // via the setKeyComponents(). The string is parsed with delimiter, but does
      // not catch the whereTextField if its the last element if is empty. So the
      // panel complains since it does not think the correct number of paramerters
      // are sent to fill the form. Remember there are only updateFormExpressionNumber
      // minus one andOrComboBoxes, so the last parameter assigned will be a text
      // field. So swap the last two entries, to get text:text:combobox.
      
      swapEndComponent = stateComponents.get(stateComponents.size() - 1);
      stateComponents.setElementAt(stateComponents.get(stateComponents.size() - 2), stateComponents.size() - 1);
      stateComponents.setElementAt(swapEndComponent, stateComponents.size() - 2);
      
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
      boolean quoteCheckBoxState;

      // Obtain connection to database & setup.

      Connection dbConnection = (Connection) ConnectionManager.getConnection("UpdateForm updateTable()");

      if (dbConnection == null)
         return false;

      // Keep track of update and if a attempt
      // was made.
      tableUpdated = false;
      tryingUpdate = false;
      quoteCheckBoxState = quoteCheckBox.isSelected();

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

               // Only MySQL & PostgreSQL support.
               if (dataSourceType.equals(ConnectionManager.MYSQL)
                    || dataSourceType.equals(ConnectionManager.POSTGRESQL))
                  sqlStatement.executeUpdate("BEGIN");

               // Setup some instances needed for processing.
               updateTextString = updateColumnToTextField.getText();
               columnName = (String) updateColumnComboBox.getSelectedItem();
               columnClass = columnClassHashMap.get(columnName);
               columnType = columnTypeHashMap.get(columnName);
               columnSize = (columnSizeHashMap.get(columnName)).intValue();
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
                           if (!(updateTextString.length() >= 10 && updateTextString.length() < 12))
                              java.sql.Date.valueOf("error");
                           
                           // Process
                           dateString = updateTextString.trim();
                           dateString = MyJSQLView_Utils.convertViewDateString_To_DBDateString(
                              dateString, DBTablesPanel.getGeneralProperties().getViewDateFormat());
                           
                           if (dataSourceType.equals(ConnectionManager.ORACLE))
                           {
                              updateString = "TO_DATE('" + dateString + "', 'YYYY-MM-dd')";
                              quoteCheckBox.setSelected(false);
                           }
                           else
                           {
                              dateValue = java.sql.Date.valueOf(dateString);
                              updateString = dateValue.toString();
                           }
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
                           dateString = dateString.substring(0, updateTextString.indexOf(" "));
                           dateString = MyJSQLView_Utils.convertViewDateString_To_DBDateString(
                              dateString, DBTablesPanel.getGeneralProperties().getViewDateFormat());
                           
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
                                 // All current coloumnSizes for MySQL > 5.0 Should be 19.
                                 else
                                    timeStampFormat = new SimpleDateFormat(
                                       DBTablesPanel.getGeneralProperties().getViewDateFormat() + " HH:mm:ss");
                              }
                              else
                              {
                                 if (columnType.equals("TIMESTAMPLTZ"))
                                    timeStampFormat = new SimpleDateFormat(
                                       DBTablesPanel.getGeneralProperties().getViewDateFormat() + " HH:mm:ss Z");
                                 else
                                    timeStampFormat = new SimpleDateFormat(
                                       DBTablesPanel.getGeneralProperties().getViewDateFormat() + " HH:mm:ss z");
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
                              
                              if (dataSourceType.equals(ConnectionManager.ORACLE))
                              {
                                 updateString ="TO_TIMESTAMP('" + dateTimeValue.toString()
                                                + "', 'YYYY-MM-DD HH24:MI:SS:FF')";
                                 quoteCheckBox.setSelected(false);
                              }
                              else
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
                        ConnectionManager.closeConnection(dbConnection,
                                                          "TableEntryForm addUpdateTableEntry()");
                        return false;
                     }
                  }
                  // Bit Types
                  else if (columnType.equals("BIT"))
                  {
                     if (dataSourceType.equals(ConnectionManager.MYSQL))
                     {
                        updateString = "B'" + updateTextString + "'";
                        quoteCheckBox.setSelected(false);
                     }
                     else
                        updateString = updateTextString;
                  }
                  
                  // None Process Given Input.
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
              
               quoteCheckBox.setSelected(quoteCheckBoxState);
               
               // Proceed with execution and finish up.
               // System.out.println(sqlStatementString);
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
         ConnectionManager.displaySQLErrors(e, "UpdateForm updateTable()");
         if (tryingUpdate)
         {
            try
            {
               dbConnection.rollback();
               dbConnection.setAutoCommit(true);
            }
            catch (SQLException error)
            {
               ConnectionManager.displaySQLErrors(e, "UpdateForm updateTable() rollback failed");
            }
         }
      }

      // Close the connection and return results.
      ConnectionManager.closeConnection(dbConnection, "UpdateForm updateTable()");
      return tableUpdated;
   }

   //==============================================================
   // Class method to get the SQL statement string that corresponds
   // to the WHERE aspect of the update.
   //==============================================================

   private String getWhereSQLExpression()
   {
      // Method Instances
      StringBuffer sqlStatementString;
      String whereString;
      String columnNameString, columnTypeString;
      String operatorString, tempSearchString;
      String unionString;

      sqlStatementString = new StringBuffer();

      // ========================================
      // Adding the search(s), WHERE, option.
      
      int i = 0;
      whereString = "WHERE ";
      unionString = "";
      do
      {
         columnNameString = columnNamesHashMap.get(whereComboBox[i].getSelectedItem());
         columnTypeString = columnTypeHashMap.get(whereComboBox[i].getSelectedItem());
         operatorString = (String) operatorComboBox[i].getSelectedItem();
         tempSearchString = whereTextField[i].getText();

         if (columnNameString != null
             && (!tempSearchString.equals("") || operatorString.toLowerCase().indexOf("null") != -1))
         {
            if (i > 0)
               sqlStatementString.append(unionString.equals("") ? "WHERE " : unionString);
            
            if (operatorString.toLowerCase().indexOf("null") != -1)
               sqlStatementString.append(whereString + identifierQuoteString + columnNameString
                                         + identifierQuoteString + " " + operatorString + " ");
            else
            {
               if (operatorString.equals("<=>") && tempSearchString.toLowerCase().equals("null"))
                  sqlStatementString.append(whereString + identifierQuoteString + columnNameString
                                            + identifierQuoteString + " " + operatorString
                                            + " " + tempSearchString + " ");
               else
               {
                  if (columnTypeString.equals("DATE"))
                  {
                     if (dataSourceType.equals(ConnectionManager.ORACLE))
                     {
                        sqlStatementString.append(whereString + identifierQuoteString + columnNameString
                                                  + identifierQuoteString + " " + operatorString
                                                  + " TO_DATE('"
                                                  + MyJSQLView_Utils.convertViewDateString_To_DBDateString(
                                                    tempSearchString,
                                                    DBTablesPanel.getGeneralProperties().getViewDateFormat())
                                                    + "', 'YYYY-MM-dd') ");
                     }
                     else
                     {
                        tempSearchString = MyJSQLView_Utils.processDateFormatSearch(tempSearchString);
                        sqlStatementString.append(whereString + identifierQuoteString + columnNameString
                                                  + identifierQuoteString + " " + operatorString + " '"
                                                  + tempSearchString + "' ");
                     }
                  }
                  else if (columnTypeString.equals("DATETIME") || columnTypeString.indexOf("TIMESTAMP") != -1)
                  {
                     if (tempSearchString.indexOf(" ") != -1)
                        tempSearchString = MyJSQLView_Utils.processDateFormatSearch(
                           tempSearchString.substring(0, tempSearchString.indexOf(" ")))
                           + tempSearchString.substring(tempSearchString.indexOf(" "));
                     else if (tempSearchString.indexOf("-") != -1 || tempSearchString.indexOf("/") != -1)
                        tempSearchString = MyJSQLView_Utils.processDateFormatSearch(tempSearchString);
                     
                     if (dataSourceType.equals(ConnectionManager.ORACLE) 
                           && columnTypeString.indexOf("TIMESTAMP") != -1)
                     {
                        sqlStatementString.append(whereString + identifierQuoteString + columnNameString
                                                 + identifierQuoteString + " " + operatorString
                                                 + " TO_TIMESTAMP('" + tempSearchString
                                                 + "', 'MM-dd-YYYY HH24:MI:SS') ");
                     }
                     else
                     {
                        sqlStatementString.append(whereString + identifierQuoteString + columnNameString
                                                  + identifierQuoteString + " " + operatorString + " '"
                                                  + tempSearchString + "' ");
                     }
                  }
                  else
                     sqlStatementString.append(whereString + identifierQuoteString + columnNameString
                                               + identifierQuoteString + " " + operatorString + " '"
                                               + tempSearchString + "' ");
               }
            }

            if (i < andOrComboBox.length)
               unionString = ((String) andOrComboBox[i].getSelectedItem()).toUpperCase() + " ";
         }
         i++;
         whereString = "";
      }
      while (i < updateFormExpressionNumber);
      
      // System.out.println(sqlStatementString);
      return sqlStatementString.toString();
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
      Iterator<JComponent> keyComponentIterator;
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