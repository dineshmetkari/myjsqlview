//=================================================================
//            MyJSQLView AdvancedSortSearchForm
//=================================================================
//
// 	This class provides a generic panel that is used to create
// an advanced sort/search query for the calling TableTabPanel's
// database table.
//
//              << AdvancedSorSearchForm.java >>
//
//=================================================================
// Copyright (C) 2005-2010 Dana M. Proctor
// Version 4.72 03/30/2010
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
// Version 1.0 08/26/2007 Original MyJSQLView AdvancedSortSearchForm Class.
//         1.1 08/27/2007 Outlined Basic Actions Events & Added Components
//                        for Sort/Search.
//         1.2 08/27/2007 Class Method createSortSearchInterface(), and
//                        Initial Pass at Layout of Compoenents.
//         1.3 08/29/2007 Completed Basic Query, Limited Testing, on Class
//                        Method getAdvancedSortSearchSQL(). Added Class
//                        Instance columnNamesHashMap & Class Method orderString().
//         1.4 08/30/2007 Added Operators Beside 'LIKE' to Search. Completed
//                        Class Method getAdvancedSortSearch() And Did Moderate
//                        Amount of Testing to Insure Funtionality. Cleaned Up.
//         1.5 09/07/2007 Added Objects 'NOT LIKE' & 'NOT REGEXP' to Instance
//                        whereOperators in Class Method createSortSearchInterface().
//         1.6 09/09/2007 Removed Class Method getAdvancedSortSearch Local
//                        Instance tempSortString.
//         1.7 09/12/2007 Modified Class Method orderString() to Adhere to a
//                        Classic Switch. Also Added Class Method Instance
//                        notFieldSort to Class Method  getAdvancedSortSearchSQL(),
//                        Corrected Sort for ASC/DSC.
//         1.8 09/12/2007 Added Class Instance clearButton & Actions to Perform
//                        the Operation on Form Components in actionPerform().
//         1.9 09/19/2007 Used System.getProperty("file.separator") for All
//						        File System Resources Accesses Through Instance
//						        fileSeparator.
//         2.0 09/26/2007 Moved closeButton in actionButtonPanel to Far Right.
//         2.1 09/28/2007 Removed BorderFactory.createEmptyBorder() From
//                        sortComboBoxes, ascendingDescendingComboBoxes,
//                        searchComboBoxes, operatorComboBoxes, & andOrComboBoxes.
//         2.2 09/28/2007 Removed fileSeparator From Advanced Sort/Search File
//                        String in Class Method actionPerformed().
//         2.3 10/14/2007 Modified SELECT Statement to Reflect LIMIT Syntax
//                        to LIMIT XX OFFSET XX in Class Method
//                        getAdvancedSortSearchSQL().
//         2.4 11/03/2007 Added Instances mysqlWhereOperators &
//                        postgreSQLWhereOperators in Class Method createSortSearch().
//         2.5 11/17/2007 Corrected Operator <=> to Properly Allow NULL Entries
//                        in Search. Class Method getAdvancedSortSearch().
//         2.6 11/18/2007 Corrected Operators IS NULL & IS NOT NULL to Properly
//                        Implement Search for Null Fields in Class Method
//                        getAdvancedSortSearchSQL().
//         2.7 11/29/2007 Logical Operator Changes in Class Method
//                        getAdvancedSortSearchSQL().
//         2.8 12/08/2007 Changed the Width of the informaitonFrame in Class
//                        actionPerformed().
//         2.9 12/12/2007 Header Update.
//         3.0 12/21/2007 Added/Implemented identiferQuoteString.
//         3.1 12/22/2007 Commented System.out.
//         3.2 02/03/2008 Added Instance hsqlWhereOperators in Class Method
//                        createSortSearch().
//         3.3 03/17/2008 Added ~= Operation to postgreSQLWhereOperators in Class
//                        Method createSortSearchInterface().
//         3.4 04/09/2008 Argument title Changed to table. The Argument Must Now
//                        Reflect a Fully Qualified Table Name, schema.table Plus
//                        Identifier String Built In. Removed Identifier Quote
//                        String From sqlTable in Class Method getAdvancedSortSearch().
//         3.5 05/12/2008 Class Instance serialVersionUID & Declaration of transient
//                        for WindowListener tableEntryFormFrameListener.
//         3.6 05/13/2008 Declared WindowListener tableEntryFormFrameListener private.
//         3.7 05/16/2008 Added String Argument sqlTableFieldsString to Class Method
//                        getAdvancedSortSearchSQL().
//         3.8 06/04/2008 Reformatted.
//         3.9 06/20/2008 Added Method Field oracleWhereOperators in Class Method
//                        createSortSearchInterface().
//         4.0 10/24/2008 File Name Change for questionIcon. Added Constructor
//                        Instance iconsDirectory.
//         4.1 12/10/2008 Reverted URL String Argument to HelpFrame Back to Forward
//                        Slashes Instead of the System fileSeparator.
//         4.2 01/09/2009 Added Two More Search/WHERE Conditionals.
//         4.3 05/27/2009 Header Format Changes/Update.
//         4.4 06/02/2009 Added Class Instance stateComponents and Its Initialization
//                        in the Constructor In Addtion to the New Methods get/set
//                        KeyComponentsState() and getKeyComponentDelimiter().
//         4.5 06/03/2009 Modified the Way Error Handled and Increased Robustness
//                        of Class Method setKeyComponentState().
//         4.6 06/06/2009 Even More Checking in setKeyComponentsState() Method to
//                        Insure Robustness of Application Maintained.
//        4.60 06/08/2009 Added IS NULL & IS NOT NULL to mysqlWhereOperators in Class
//                        Method createSortSearchInterface().
//        4.61 09/22/2009 Updated Copyright Date and Some Other Comments.
//        4.62 09/23/2009 MyJSQLView Project Common Source Code Formatting.
//        4.63 10/24/2009 Removed Class Instance fileSeparator. Obtained Constructor
//                        Instance iconsDirectory From MyJSQLView_Utils Class.
//        4.64 10/25/2009 Added fileSeparator to iconsDirectory.
//        4.65 11/08/2009 Correction in Class Method getAdvancedSortSearchSQL() in
//                        quoting, identifierQuoteString, sqlTable.
//        4.66 11/08/2009 Undid 4.65. Correction Was Needed In Passing the Correct
//                        scheme Table Name by the Calling Class. In the Case the
//                        QueryPanel.
//        4.67 02/18/2010 Changed Package to Reflect Dandy Made Productions Code.
//        4.68 02/28/2010 Added Argument MyJSQLView_ResourceBundle to Constructor and
//                        Implemented Internationalization. Added Constructor Instance
//                        resource. Class Method createSortSeachInterface() Added
//                        resourceBundle & resource Instances.
//        4.69 02/28/2010 Class Method setKeyComponentsState() Created Resource Bundle
//                        Message for Internationalization.
//        4.70 03/01/2010 Moved Constructor resourceBundle Instance to Class Instance.
//                        Cleaned Up a Bit.
//        4.71 03/30/2010 Bug Fix For Inability to Search Date Data Types in Oracle. Class
//                        Method getAdvancedSortSearchSQL(). Added Class Instance columnTypesHashMap
//                        Instantiated Through Constructor.
//        4.72 03/30/2010 Implemented Basic Support For Search TIMESTAMP Data Types in Oracle.
//                        Class Method getAdvancedSortSearchSQL().
//
//-----------------------------------------------------------------
//                 danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview;

import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *    The AdvancedSortSearchForm class provides a generic panel that is used to
 * create an advanced sort/search query for the calling TableTabPanel's database
 * table.
 * 
 * @author Dana M. Proctor
 * @version 4.72 03/30/2010
 */

class AdvancedSortSearchForm extends JFrame implements ActionListener
{
   // Class Instances.
   private static final long serialVersionUID = -2663401193471271657L;

   private String sqlTable;
   private String identifierQuoteString;
   private HashMap columnNamesHashMap, columnTypesHashMap;
   private Vector comboBoxColumnNames;
   private MyJSQLView_ResourceBundle resourceBundle;

   private GridBagLayout gridbag;
   private GridBagConstraints constraints;

   private JPanel sortSearchPanel;
   private JButton questionButton;
   private JComboBox selectTypeComboBox;
   private JComboBox sortComboBox1, sortComboBox2, sortComboBox3;
   private JComboBox ascendingDescendingComboBox1, ascendingDescendingComboBox2,
                     ascendingDescendingComboBox3;
   private JComboBox searchComboBox1, searchComboBox2, searchComboBox3;
   private JComboBox searchComboBox4, searchComboBox5;
   private JComboBox operatorComboBox1, operatorComboBox2, operatorComboBox3;
   private JComboBox operatorComboBox4, operatorComboBox5;
   private JTextField searchTextField1, searchTextField2, searchTextField3;
   private JTextField searchTextField4, searchTextField5;
   private JComboBox andOrComboBox1, andOrComboBox2, andOrComboBox3, andOrComboBox4;
   private Vector stateComponents;

   private JButton closeButton, clearButton;
   protected JButton sortButton, searchButton;

   //==============================================================
   // AdvancedSortSearchForm Constructor
   //==============================================================

   protected AdvancedSortSearchForm(String table, MyJSQLView_ResourceBundle resourceBundle,
                                    HashMap columnNamesHashMap, HashMap columnTypesHashMap,
                                    Vector comboBoxColumnNames)
   {
      sqlTable = table;
      this.resourceBundle = resourceBundle;
      this.columnNamesHashMap = columnNamesHashMap;
      this.columnTypesHashMap = columnTypesHashMap;
      this.comboBoxColumnNames = comboBoxColumnNames;

      // Constructor Instances
      JPanel mainPanel, formPanel, northPanel, selectTypePanel;
      JPanel helpPanel, southPanel, actionButtonPanel, clearPanel;
      JLabel selectTypeLabel;
      String resource, iconsDirectory;
      ImageIcon questionIcon;
      ImageIcon clearIcon;

      // Setting up a icons directory and other instances.
      
      resource = resourceBundle.getResource("AdvancedSortSearchForm.message.Title");
      if (resource.equals(""))
         setTitle("Advanced Sort/Search : " + table);
      else
         setTitle(resource + " : " + table);

      iconsDirectory = MyJSQLView_Utils.getIconsDirectory() + MyJSQLView_Utils.getFileSeparator();
      identifierQuoteString = MyJSQLView_Access.getIdentifierQuoteString();

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

      // Select Type Option */Distinct and Help

      northPanel = new JPanel(new GridLayout(1, 2, 0, 0));
      northPanel.setBorder(BorderFactory.createRaisedBevelBorder());

      selectTypePanel = new JPanel();
      selectTypePanel.setLayout(new FlowLayout(FlowLayout.LEADING));

      resource = resourceBundle.getResource("AdvancedSortSearchForm.label.Select");
      if (resource.equals(""))
         selectTypeLabel = new JLabel("Select : ");
      else
         selectTypeLabel = new JLabel(resource + " : ");
      selectTypePanel.add(selectTypeLabel);

      selectTypeComboBox = new JComboBox();
      selectTypeComboBox.addItem("All");
      selectTypeComboBox.addItem("Distinct");
      stateComponents.addElement(selectTypeComboBox);
      selectTypePanel.add(selectTypeComboBox);

      northPanel.add(selectTypePanel);

      helpPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));

      questionIcon = new ImageIcon(iconsDirectory + "bulbIcon.png");
      questionButton = new JButton(questionIcon);
      questionButton.setFocusPainted(false);
      questionButton.setBorder(BorderFactory.createRaisedBevelBorder());
      questionButton.addActionListener(this);
      helpPanel.add(questionButton);

      northPanel.add(helpPanel);
      mainPanel.add(northPanel, BorderLayout.NORTH);

      // Sort/Search Interface

      sortSearchPanel = new JPanel();
      sortSearchPanel.setLayout(gridbag);
      sortSearchPanel.setBorder(BorderFactory.createLoweredBevelBorder());

      createSortSearchInterface(resourceBundle);

      buildConstraints(constraints, 0, 1, 1, 1, 100, 100);
      constraints.fill = GridBagConstraints.BOTH;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(sortSearchPanel, constraints);
      formPanel.add(sortSearchPanel);

      mainPanel.add(formPanel, BorderLayout.CENTER);

      // Creating Action Buttons Components.

      southPanel = new JPanel(gridbag);
      southPanel.setBorder(BorderFactory.createEtchedBorder());

      actionButtonPanel = new JPanel();

      // Sort Button
      resource = resourceBundle.getResource("AdvancedSortSearchForm.button.Sort");
      if (resource.equals(""))
         sortButton = new JButton("Sort");
      else
         sortButton = new JButton(resource);
      sortButton.setFocusPainted(false);
      actionButtonPanel.add(sortButton);

      // Search Button
      resource = resourceBundle.getResource("AdvancedSortSearchForm.button.Search");
      if (resource.equals(""))
         searchButton = new JButton("Search");
      else
         searchButton = new JButton(resource);
      searchButton.setFocusPainted(false);
      actionButtonPanel.add(searchButton);

      // Close Button
      resource = resourceBundle.getResource("AdvancedSortSearchForm.button.Close");
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
      (this.getRootPane()).setDefaultButton(searchButton);

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
   // actions associated with sort/search are handled from the
   // calling class TableTabPanel. ActionListener assigned from
   // this class.
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

         // Clear Button Action
         if (formSource == clearButton)
         {
            selectTypeComboBox.setSelectedIndex(0);
            sortComboBox1.setSelectedIndex(0);
            sortComboBox2.setSelectedIndex(0);
            sortComboBox3.setSelectedIndex(0);
            ascendingDescendingComboBox1.setSelectedIndex(0);
            ascendingDescendingComboBox2.setSelectedIndex(0);
            ascendingDescendingComboBox3.setSelectedIndex(0);
            searchComboBox1.setSelectedIndex(0);
            searchComboBox2.setSelectedIndex(0);
            searchComboBox3.setSelectedIndex(0);
            searchComboBox4.setSelectedIndex(0);
            searchComboBox5.setSelectedIndex(0);
            operatorComboBox1.setSelectedIndex(0);
            operatorComboBox2.setSelectedIndex(0);
            operatorComboBox3.setSelectedIndex(0);
            operatorComboBox4.setSelectedIndex(0);
            operatorComboBox5.setSelectedIndex(0);
            andOrComboBox1.setSelectedIndex(0);
            andOrComboBox2.setSelectedIndex(0);
            andOrComboBox3.setSelectedIndex(0);
            andOrComboBox4.setSelectedIndex(0);
            searchTextField1.setText("");
            searchTextField2.setText("");
            searchTextField3.setText("");
            searchTextField4.setText("");
            searchTextField5.setText("");
         }

         // Close Button Action
         if (formSource == closeButton)
         {
            // Keep form active with data just
            // hide. The form remains active
            // until a normal search is again
            // executed from the TableTabPanel.
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
   //==============================================================

   protected void center()
   {
      Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
      Dimension us = getSize();
      int x = (screen.width - us.width) / 2;
      int y = (screen.height - us.height) / 2;
      setLocation(x, y);
   }

   //==============================================================
   // Class method to setup the Sort/Search GUI Components.
   //==============================================================

   private void createSortSearchInterface(MyJSQLView_ResourceBundle resourceBundle)
   {
      // Method Instance
      JPanel sortPanel, searchPanel;
      String resourceSortBy, resourceThen, resourceSearch;
      
      JLabel sortByLabel, sortByLabel2, sortByLabel3;
      JLabel sortThenLabel, sortThenLabel2; 
      JLabel searchLabel1, searchLabel2, searchLabel3, searchLabel4, searchLabel5;

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
      // Sort Interface Setup.

      sortPanel = new JPanel();
      sortPanel.setLayout(gridbag);
      sortPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLoweredBevelBorder(),
                                                             BorderFactory.createEmptyBorder(10, 6, 10, 6)));

      resourceSortBy = resourceBundle.getResource("AdvancedSortSearchForm.label.SortBy");
      if (resourceSortBy.equals(""))
         sortByLabel = new JLabel("Sort By : ", JLabel.LEADING);
      else
         sortByLabel = new JLabel(resourceSortBy + " : ", JLabel.LEADING);

      buildConstraints(constraints, 0, 0, 1, 1, 100, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.WEST;
      gridbag.setConstraints(sortByLabel, constraints);
      sortPanel.add(sortByLabel);

      sortComboBox1 = new JComboBox(comboBoxColumnNames);
      stateComponents.addElement(sortComboBox1);

      buildConstraints(constraints, 1, 0, 2, 1, 100, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.WEST;
      gridbag.setConstraints(sortComboBox1, constraints);
      sortPanel.add(sortComboBox1);

      ascendingDescendingComboBox1 = new JComboBox();
      ascendingDescendingComboBox1.addItem("Ascending");
      ascendingDescendingComboBox1.addItem("Descending");
      stateComponents.addElement(ascendingDescendingComboBox1);

      buildConstraints(constraints, 2, 0, 1, 1, 100, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.WEST;
      gridbag.setConstraints(ascendingDescendingComboBox1, constraints);
      sortPanel.add(ascendingDescendingComboBox1);

      resourceThen = resourceBundle.getResource("AdvancedSortSearchForm.label.Then");
      if (resourceThen.equals(""))
         sortThenLabel = new JLabel(" Then, ", JLabel.LEADING);
      else
         sortThenLabel = new JLabel(" " + resourceThen + ", ", JLabel.LEADING);

      buildConstraints(constraints, 3, 0, 1, 1, 100, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.WEST;
      gridbag.setConstraints(sortThenLabel, constraints);
      sortPanel.add(sortThenLabel);

      if (resourceSortBy.equals(""))
         sortByLabel2 = new JLabel("Sort By : ", JLabel.LEADING);
      else
         sortByLabel2 = new JLabel(resourceSortBy + " : ", JLabel.LEADING);

      buildConstraints(constraints, 0, 1, 1, 1, 100, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.WEST;
      gridbag.setConstraints(sortByLabel2, constraints);
      sortPanel.add(sortByLabel2);

      sortComboBox2 = new JComboBox(comboBoxColumnNames);
      stateComponents.addElement(sortComboBox2);

      buildConstraints(constraints, 1, 1, 1, 1, 100, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.WEST;
      gridbag.setConstraints(sortComboBox2, constraints);
      sortPanel.add(sortComboBox2);

      ascendingDescendingComboBox2 = new JComboBox();
      ascendingDescendingComboBox2.addItem("Ascending");
      ascendingDescendingComboBox2.addItem("Descending");
      stateComponents.addElement(ascendingDescendingComboBox2);

      buildConstraints(constraints, 2, 1, 1, 1, 100, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.WEST;
      gridbag.setConstraints(ascendingDescendingComboBox2, constraints);
      sortPanel.add(ascendingDescendingComboBox2);

      if (resourceThen.equals(""))
         sortThenLabel2 = new JLabel(" Then, ", JLabel.LEADING);
      else
         sortThenLabel2 = new JLabel(" " + resourceThen + ", ", JLabel.LEADING);

      buildConstraints(constraints, 3, 1, 1, 1, 100, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.WEST;
      gridbag.setConstraints(sortThenLabel2, constraints);
      sortPanel.add(sortThenLabel2);

      if (resourceSortBy.equals(""))
         sortByLabel3 = new JLabel("Sort By : ", JLabel.LEADING);
      else
         sortByLabel3 = new JLabel(resourceSortBy + " : ", JLabel.LEADING);

      buildConstraints(constraints, 0, 2, 1, 1, 100, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.WEST;
      gridbag.setConstraints(sortByLabel3, constraints);
      sortPanel.add(sortByLabel3);

      sortComboBox3 = new JComboBox(comboBoxColumnNames);
      stateComponents.addElement(sortComboBox3);

      buildConstraints(constraints, 1, 2, 1, 1, 100, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.WEST;
      gridbag.setConstraints(sortComboBox3, constraints);
      sortPanel.add(sortComboBox3);

      ascendingDescendingComboBox3 = new JComboBox();
      ascendingDescendingComboBox3.addItem("Ascending");
      ascendingDescendingComboBox3.addItem("Descending");
      stateComponents.addElement(ascendingDescendingComboBox3);

      buildConstraints(constraints, 2, 2, 1, 1, 100, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.WEST;
      gridbag.setConstraints(ascendingDescendingComboBox3, constraints);
      sortPanel.add(ascendingDescendingComboBox3);

      buildConstraints(constraints, 0, 0, 1, 1, 100, 37);
      constraints.fill = GridBagConstraints.BOTH;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(sortPanel, constraints);
      sortSearchPanel.add(sortPanel);

      // ============================
      // Search Interface Setup

      searchPanel = new JPanel();
      searchPanel.setLayout(gridbag);
      searchPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLoweredBevelBorder(),
                                                               BorderFactory.createEmptyBorder(10, 6, 10, 6)));

      resourceSearch = resourceBundle.getResource("AdvancedSortSearchForm.label.Search");
      if (resourceSearch.equals(""))
         searchLabel1 = new JLabel("Search : ", JLabel.LEFT);
      else
         searchLabel1 = new JLabel(resourceSearch + " : ", JLabel.LEFT);

      buildConstraints(constraints, 0, 3, 1, 1, 100, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(searchLabel1, constraints);
      searchPanel.add(searchLabel1);

      searchComboBox1 = new JComboBox(comboBoxColumnNames);
      stateComponents.addElement(searchComboBox1);

      buildConstraints(constraints, 1, 3, 1, 1, 100, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(searchComboBox1, constraints);
      searchPanel.add(searchComboBox1);

      operatorComboBox1 = new JComboBox(whereOperators);
      stateComponents.addElement(operatorComboBox1);

      buildConstraints(constraints, 2, 3, 1, 1, 100, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(operatorComboBox1, constraints);
      searchPanel.add(operatorComboBox1);

      searchTextField1 = new JTextField(15);
      stateComponents.addElement(searchTextField1);

      buildConstraints(constraints, 3, 3, 1, 1, 100, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(searchTextField1, constraints);
      searchPanel.add(searchTextField1);

      andOrComboBox1 = new JComboBox();
      andOrComboBox1.addItem("And");
      andOrComboBox1.addItem("Or");
      stateComponents.addElement(andOrComboBox1);

      buildConstraints(constraints, 4, 3, 1, 1, 100, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(andOrComboBox1, constraints);
      searchPanel.add(andOrComboBox1);

      if (resourceSearch.equals(""))
         searchLabel2 = new JLabel("Search : ", JLabel.LEFT);
      else
         searchLabel2 = new JLabel(resourceSearch + " : ", JLabel.LEFT);

      buildConstraints(constraints, 0, 4, 1, 1, 100, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(searchLabel2, constraints);
      searchPanel.add(searchLabel2);

      searchComboBox2 = new JComboBox(comboBoxColumnNames);
      stateComponents.addElement(searchComboBox2);

      buildConstraints(constraints, 1, 4, 1, 1, 100, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(searchComboBox2, constraints);
      searchPanel.add(searchComboBox2);

      operatorComboBox2 = new JComboBox(whereOperators);
      stateComponents.addElement(operatorComboBox2);

      buildConstraints(constraints, 2, 4, 1, 1, 100, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(operatorComboBox2, constraints);
      searchPanel.add(operatorComboBox2);

      searchTextField2 = new JTextField(15);
      stateComponents.addElement(searchTextField2);

      buildConstraints(constraints, 3, 4, 1, 1, 100, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(searchTextField2, constraints);
      searchPanel.add(searchTextField2);

      andOrComboBox2 = new JComboBox();
      andOrComboBox2.addItem("And");
      andOrComboBox2.addItem("Or");
      stateComponents.addElement(andOrComboBox2);

      buildConstraints(constraints, 4, 4, 1, 1, 100, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(andOrComboBox2, constraints);
      searchPanel.add(andOrComboBox2);

      if (resourceSearch.equals(""))
         searchLabel3 = new JLabel("Search : ", JLabel.LEFT);
      else
         searchLabel3 = new JLabel(resourceSearch + " : ", JLabel.LEFT);

      buildConstraints(constraints, 0, 5, 1, 1, 100, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(searchLabel3, constraints);
      searchPanel.add(searchLabel3);

      searchComboBox3 = new JComboBox(comboBoxColumnNames);
      stateComponents.addElement(searchComboBox3);

      buildConstraints(constraints, 1, 5, 1, 1, 100, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(searchComboBox3, constraints);
      searchPanel.add(searchComboBox3);

      operatorComboBox3 = new JComboBox(whereOperators);
      stateComponents.addElement(operatorComboBox3);

      buildConstraints(constraints, 2, 5, 1, 1, 100, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(operatorComboBox3, constraints);
      searchPanel.add(operatorComboBox3);

      searchTextField3 = new JTextField(15);
      stateComponents.addElement(searchTextField3);

      buildConstraints(constraints, 3, 5, 1, 1, 100, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(searchTextField3, constraints);
      searchPanel.add(searchTextField3);

      andOrComboBox3 = new JComboBox();
      andOrComboBox3.addItem("And");
      andOrComboBox3.addItem("Or");
      stateComponents.addElement(andOrComboBox3);

      buildConstraints(constraints, 4, 5, 1, 1, 100, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(andOrComboBox3, constraints);
      searchPanel.add(andOrComboBox3);

      if (resourceSearch.equals(""))
         searchLabel4 = new JLabel("Search : ", JLabel.LEFT);
      else
         searchLabel4 = new JLabel(resourceSearch + " : ", JLabel.LEFT);

      buildConstraints(constraints, 0, 6, 1, 1, 100, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(searchLabel4, constraints);
      searchPanel.add(searchLabel4);

      searchComboBox4 = new JComboBox(comboBoxColumnNames);
      stateComponents.addElement(searchComboBox4);

      buildConstraints(constraints, 1, 6, 1, 1, 100, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(searchComboBox4, constraints);
      searchPanel.add(searchComboBox4);

      operatorComboBox4 = new JComboBox(whereOperators);
      stateComponents.addElement(operatorComboBox4);

      buildConstraints(constraints, 2, 6, 1, 1, 100, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(operatorComboBox4, constraints);
      searchPanel.add(operatorComboBox4);

      searchTextField4 = new JTextField(15);
      stateComponents.addElement(searchTextField4);

      buildConstraints(constraints, 3, 6, 1, 1, 100, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(searchTextField4, constraints);
      searchPanel.add(searchTextField4);

      andOrComboBox4 = new JComboBox();
      andOrComboBox4.addItem("And");
      andOrComboBox4.addItem("Or");
      stateComponents.addElement(andOrComboBox4);

      buildConstraints(constraints, 4, 6, 1, 1, 100, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(andOrComboBox4, constraints);
      searchPanel.add(andOrComboBox4);

      if (resourceSearch.equals(""))
         searchLabel5 = new JLabel("Search : ", JLabel.LEFT);
      else
         searchLabel5 = new JLabel(resourceSearch + " : ", JLabel.LEFT);

      buildConstraints(constraints, 0, 7, 1, 1, 100, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(searchLabel5, constraints);
      searchPanel.add(searchLabel5);

      searchComboBox5 = new JComboBox(comboBoxColumnNames);
      stateComponents.addElement(searchComboBox5);

      buildConstraints(constraints, 1, 7, 1, 1, 100, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(searchComboBox5, constraints);
      searchPanel.add(searchComboBox5);

      operatorComboBox5 = new JComboBox(whereOperators);

      buildConstraints(constraints, 2, 7, 1, 1, 100, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(operatorComboBox5, constraints);
      searchPanel.add(operatorComboBox5);

      searchTextField5 = new JTextField(15);
      stateComponents.addElement(searchTextField5);
      stateComponents.addElement(operatorComboBox5);

      buildConstraints(constraints, 3, 7, 1, 1, 100, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(searchTextField5, constraints);
      searchPanel.add(searchTextField5);

      buildConstraints(constraints, 0, 1, 1, 1, 100, 63);
      constraints.fill = GridBagConstraints.BOTH;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(searchPanel, constraints);
      sortSearchPanel.add(searchPanel);
   }

   //==============================================================
   // Class method to get the SQL statement string that is a result
   // of the selected/input parameters of the form.
   //==============================================================

   protected String getAdvancedSortSearchSQL(String sqlTableFieldsString, int tableRowStart,
                                             int tableRowLimit)
   {
      String sqlStatementString;
      String columnNameString, columnTypeString;
      String operatorString, tempSearchString;
      String unionString;
      String ascDescString;
      boolean notFieldSort;

      sqlStatementString = "SELECT ";

      // ========================================
      // Adding DISTINCT option as needed.

      if (((String) selectTypeComboBox.getSelectedItem()).equals("All"))
         sqlStatementString += sqlTableFieldsString + " FROM " + sqlTable + " ";
      else
         sqlStatementString += "DISTINCT " + sqlTableFieldsString + " FROM " + sqlTable + " ";

      // ========================================
      // Adding the search(s), WHERE, option.

      columnNameString = (String) columnNamesHashMap.get(searchComboBox1.getSelectedItem());
      columnTypeString = (String) columnTypesHashMap.get(searchComboBox1.getSelectedItem());
      operatorString = (String) operatorComboBox1.getSelectedItem();
      tempSearchString = searchTextField1.getText();
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
            {
               if (MyJSQLView_Access.getSubProtocol().indexOf("oracle") != -1 
                   && columnTypeString.equals("DATE"))
                  sqlStatementString += "WHERE " + identifierQuoteString + columnNameString
                                         + identifierQuoteString + " " + operatorString
                                         + " TO_DATE('" + tempSearchString + "', 'MM-dd-YYYY') ";
               else if (MyJSQLView_Access.getSubProtocol().indexOf("oracle") != -1 
                        && columnTypeString.indexOf("TIMESTAMP") != -1)
                  sqlStatementString += "WHERE " + identifierQuoteString + columnNameString
                                        + identifierQuoteString + " " + operatorString
                                        + " TO_TIMESTAMP('" + tempSearchString + "', 'MM-dd-YYYY HH24:MI:SS') ";
               else
                  sqlStatementString += "WHERE " + identifierQuoteString + columnNameString
                                         + identifierQuoteString + " " + operatorString + " '"
                                         + tempSearchString + "' ";
            }
         }

         unionString = ((String) andOrComboBox1.getSelectedItem()).toUpperCase() + " ";
      }

      columnNameString = (String) columnNamesHashMap.get(searchComboBox2.getSelectedItem());
      columnTypeString = (String) columnTypesHashMap.get(searchComboBox2.getSelectedItem());
      operatorString = (String) operatorComboBox2.getSelectedItem();
      tempSearchString = searchTextField2.getText();

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
            {
               if (MyJSQLView_Access.getSubProtocol().indexOf("oracle") != -1 
                   && columnTypeString.equals("DATE"))
                  sqlStatementString += identifierQuoteString + columnNameString
                                        + identifierQuoteString + " " + operatorString
                                        + " TO_DATE('" + tempSearchString + "', 'MM-dd-YYYY') ";
               else if (MyJSQLView_Access.getSubProtocol().indexOf("oracle") != -1 
                        && columnTypeString.indexOf("TIMESTAMP") != -1)
                  sqlStatementString += identifierQuoteString + columnNameString
                                        + identifierQuoteString + " " + operatorString
                                        + " TO_TIMESTAMP('" + tempSearchString + "', 'MM-dd-YYYY HH24:MI:SS') ";
               else
                  sqlStatementString += identifierQuoteString + columnNameString + identifierQuoteString + " "
                                        + operatorString + " '" + tempSearchString + "' ";
            }
         }

         unionString = ((String) andOrComboBox2.getSelectedItem()).toUpperCase() + " ";
      }

      columnNameString = (String) columnNamesHashMap.get(searchComboBox3.getSelectedItem());
      columnTypeString = (String) columnTypesHashMap.get(searchComboBox3.getSelectedItem());
      operatorString = (String) operatorComboBox3.getSelectedItem();
      tempSearchString = searchTextField3.getText();

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
            {
               if (MyJSQLView_Access.getSubProtocol().indexOf("oracle") != -1 
                     && columnTypeString.equals("DATE"))
                  sqlStatementString += identifierQuoteString + columnNameString
                                        + identifierQuoteString + " " + operatorString
                                        + " TO_DATE('" + tempSearchString + "', 'MM-dd-YYYY') ";
               else if (MyJSQLView_Access.getSubProtocol().indexOf("oracle") != -1 
                        && columnTypeString.indexOf("TIMESTAMP") != -1)
                  sqlStatementString += identifierQuoteString + columnNameString
                                        + identifierQuoteString + " " + operatorString
                                        + " TO_TIMESTAMP('" + tempSearchString + "', 'MM-dd-YYYY HH24:MI:SS') ";
               else
                  sqlStatementString += identifierQuoteString + columnNameString + identifierQuoteString + " "
                                        + operatorString + " '" + tempSearchString + "' ";
            }
         }

         unionString = ((String) andOrComboBox3.getSelectedItem()).toUpperCase() + " ";
      }

      columnNameString = (String) columnNamesHashMap.get(searchComboBox4.getSelectedItem());
      columnTypeString = (String) columnTypesHashMap.get(searchComboBox4.getSelectedItem());
      operatorString = (String) operatorComboBox4.getSelectedItem();
      tempSearchString = searchTextField4.getText();

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
            {
               if (MyJSQLView_Access.getSubProtocol().indexOf("oracle") != -1 
                     && columnTypeString.equals("DATE"))
                  sqlStatementString += identifierQuoteString + columnNameString
                                        + identifierQuoteString + " " + operatorString
                                        + " TO_DATE('" + tempSearchString + "', 'MM-dd-YYYY') ";
               else if (MyJSQLView_Access.getSubProtocol().indexOf("oracle") != -1 
                     && columnTypeString.indexOf("TIMESTAMP") != -1)
                  sqlStatementString += identifierQuoteString + columnNameString
                                        + identifierQuoteString + " " + operatorString
                                        + " TO_TIMESTAMP('" + tempSearchString + "', 'MM-dd-YYYY HH24:MI:SS') ";
               else
                  sqlStatementString += identifierQuoteString + columnNameString + identifierQuoteString + " "
                                        + operatorString + " '" + tempSearchString + "' ";
            }
         }

         unionString = ((String) andOrComboBox4.getSelectedItem()).toUpperCase() + " ";
      }

      columnNameString = (String) columnNamesHashMap.get(searchComboBox5.getSelectedItem());
      columnTypeString = (String) columnTypesHashMap.get(searchComboBox5.getSelectedItem());
      operatorString = (String) operatorComboBox5.getSelectedItem();
      tempSearchString = searchTextField5.getText();

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
            {
               if (MyJSQLView_Access.getSubProtocol().indexOf("oracle") != -1 
                     && columnTypeString.equals("DATE"))
                  sqlStatementString += identifierQuoteString + columnNameString
                                        + identifierQuoteString + " " + operatorString
                                        + " TO_DATE('" + tempSearchString + "', 'MM-dd-YYYY') ";
               else if (MyJSQLView_Access.getSubProtocol().indexOf("oracle") != -1 
                     && columnTypeString.indexOf("TIMESTAMP") != -1)
                  sqlStatementString += identifierQuoteString + columnNameString
                                        + identifierQuoteString + " " + operatorString
                                        + " TO_TIMESTAMP('" + tempSearchString + "', 'MM-dd-YYYY HH24:MI:SS') ";
               else
                  sqlStatementString += identifierQuoteString + columnNameString + identifierQuoteString + " "
                                        + operatorString + " '" + tempSearchString + "' ";
            }
         }
      }

      // ========================================
      // Adding the sort(s), ORDER BY, option.

      columnNameString = (String) columnNamesHashMap.get(sortComboBox1.getSelectedItem());
      ascDescString = "";
      notFieldSort = true;

      if (columnNameString != null)
      {
         sqlStatementString += "ORDER BY " + identifierQuoteString + columnNameString + identifierQuoteString
                               + " ";
         ascDescString = orderString(1);
         notFieldSort = false;
      }

      columnNameString = (String) columnNamesHashMap.get(sortComboBox2.getSelectedItem());

      if (columnNameString != null)
      {
         sqlStatementString += notFieldSort ? "ORDER BY " : ascDescString + ", ";
         sqlStatementString += identifierQuoteString + columnNameString + identifierQuoteString + " ";
         ascDescString = orderString(2);
         notFieldSort = false;
      }

      columnNameString = (String) columnNamesHashMap.get(sortComboBox3.getSelectedItem());

      if (columnNameString != null)
      {
         sqlStatementString += notFieldSort ? "ORDER BY " : ascDescString + ", ";
         sqlStatementString += identifierQuoteString + columnNameString + identifierQuoteString + " ";
         ascDescString = orderString(3);
      }

      if (!ascDescString.equals(""))
         sqlStatementString += ascDescString + " ";

      // ========================================
      // Adding the LIMIT option.

      sqlStatementString += "LIMIT " + tableRowLimit + " OFFSET " + tableRowStart;

      // Return the resultant query.

      // System.out.println(sqlStatementString);
      return sqlStatementString;

      // Sample outline of what a basic SQL SELECT query should be.
      // It was determined in the initial version to not include
      // GROUP BY or HAVING options.

      /*
       * sqlStatementString = "SELECT * " + "FROM " + sqlTable + " " + "WHERE " +
       * columnSearchString + " " + "LIKE " + searchTextString + " " + "ORDER BY " +
       * columnNamesHashMap.get(sortComboBox.getSelectedItem()) + " " + "LIMIT " +
       * tableRowStart + "," + tableRowLimit;
       */
   }

   //==============================================================
   // Class method to return the properly formatted text for the
   // ORDER BY SQL statements using 'ASC' or 'DESC'.
   //==============================================================

   private String orderString(int option)
   {
      String ascendingDescendingString;

      switch (option)
      {
      case 1:
         if (ascendingDescendingComboBox1.getSelectedIndex() == 0)
            ascendingDescendingString = "ASC";
         else
            ascendingDescendingString = "DESC";
         break;
      case 2:
         if (ascendingDescendingComboBox2.getSelectedIndex() == 0)
            ascendingDescendingString = "ASC";
         else
            ascendingDescendingString = "DESC";
         break;
      case 3:
         if (ascendingDescendingComboBox3.getSelectedIndex() == 0)
            ascendingDescendingString = "ASC";
         else
            ascendingDescendingString = "DESC";
         break;
      default:
         ascendingDescendingString = "";
      }
      return ascendingDescendingString;
   }

   //==============================================================
   // Class method for outside classes to obtain the current state
   // of the form.
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
      delimiter = getKeyComponentsDelimiter();

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

   //==============================================================
   // Class method to allow classes to obtain the key components
   // delimiter.
   //==============================================================

   public static String getKeyComponentsDelimiter()
   {
      return ";&;";
   }

   //==============================================================
   // Class method for outside classes to set the current state of
   // the form.
   //==============================================================

   protected void setKeyComponentsState(String stateString)
   {
      // Method Instances
      String delimiter;
      String[] keyComponentSettings;
      Iterator keyComponentIterator;
      Object currentComponent;
      int comboBoxItemCount, stateIndex;
      boolean failedToLoadForm;

      // Initialize and check to see if there are any new
      // settings to even proceed.

      delimiter = getKeyComponentsDelimiter();
      failedToLoadForm = false;
      keyComponentSettings = stateString.split(delimiter);

      if ((keyComponentSettings.length == 0)
          || (keyComponentSettings.length == 1 && keyComponentSettings[0].equals("")))
         return;

      // Try loading the form.
      else if (keyComponentSettings.length == stateComponents.size())
      {
         try
         {
            int i = 0;
            keyComponentIterator = stateComponents.iterator();

            while (keyComponentIterator.hasNext())
            {
               currentComponent = keyComponentIterator.next();
               // System.out.println(keyComponentSettings[i]);

               if (currentComponent instanceof JComboBox)
               {
                  comboBoxItemCount = ((JComboBox) currentComponent).getItemCount();
                  stateIndex = Integer.parseInt(keyComponentSettings[i]);

                  if (stateIndex > -1 && stateIndex < comboBoxItemCount)
                     ((JComboBox) currentComponent).setSelectedIndex(stateIndex);
               }

               if (currentComponent instanceof JTextField)
                  ((JTextField) currentComponent).setText(keyComponentSettings[i]);

               i++;
            }
         }
         catch (NumberFormatException e)
         {
            failedToLoadForm = true;
         }
      }
      else
         failedToLoadForm = true;

      // Provide user information on result of loading form
      // as needed.
      if (failedToLoadForm)
      {
         String resourceMessage, resourceTitle;
         String title, optionPaneStringErrors;
         
         resourceMessage = resourceBundle.getResource("AdvancedSortSearchForm.dialogmessage.SetFields");
         if (resourceMessage.equals(""))
            optionPaneStringErrors = "Unable to Properly Set Advanced Sort/Search Fields."
                                     + " Possible Corrupt File!";
         else
            optionPaneStringErrors = resourceMessage;
            
         resourceTitle = resourceBundle.getResource("AdvancedSortSearchForm.dialogtitle.Alert");
         if (resourceTitle.equals(""))
            title = "Alert";
         else
            title = resourceTitle;
         
         JOptionPane.showMessageDialog(null, optionPaneStringErrors, title, JOptionPane.ERROR_MESSAGE);
      }
   }
}