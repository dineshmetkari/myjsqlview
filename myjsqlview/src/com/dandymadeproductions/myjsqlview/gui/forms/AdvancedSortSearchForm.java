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
// Copyright (C) 2005-2015 Dana M. Proctor
// Version 5.02 10/28/2014
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
//        4.68 02/28/2010 Added Argument MyJSQLView_ResourceBundle to Constructor
//                        and Implemented Internationalization. Added Constructor
//                        Instance resource. Class Method createSortSeachInterface()
//                        Added resourceBundle & resource Instances.
//        4.69 02/28/2010 Class Method setKeyComponentsState() Created Resource
//                        Bundle Message for Internationalization.
//        4.70 03/01/2010 Moved Constructor resourceBundle Instance to Class
//                        Instance. Cleaned Up a Bit.
//        4.71 03/30/2010 Bug Fix For Inability to Search Date Data Types in Oracle.
//                        Class Method getAdvancedSortSearchSQL(). Added Class
//                        Instance columnTypesHashMap Instantiated Through
//                        Constructor.
//        4.72 03/30/2010 Implemented Basic Support For Search TIMESTAMP Data Types
//                        in Oracle. Class Method getAdvancedSortSearchSQL().
//        4.73 04/02/2010 Consolidated Code in Constructor, actionPerformed(), 
//                        createSortSearchInterface(), getAdvancedSortSearchSQL(),
//                        and orderString(). Changed ComboBoxes and Texfields Class
//                        Instances to Arrays.
//        4.74 04/07/2010 Removed and Commented System.out in Class Method orderString().
//        4.75 05/16/2010 Parameterized Class Instances columnNamesHashMap, columnTypesHashMap,
//                        comboBoxColumnNames, and stateComponents, Along With Arguments
//                        In Constructor to Bring Code Into Compliance with Java 5.0 API.
//                        Same in Method createSortSearchInterface Instance swapEndCompnents.
//        4.76 05/19/2010 Parameterized Instance keyComponentIterator in Class Methods
//                        getKeyComponentsState() & setKeyComponentsState().
//        4.77 08/27/2010 Added sqliteWhereOperators in Method createSortSearchInterface().
//        4.78 01/14/2011 Class Method getAdvancedSortSearchSQL() Changes to Give the
//                        Ability to Properly Search Given Input for Date/DateTime/Timestamp
//                        Fields.
//        4.79 01/26/2011 Added Instances connectionProperties & subProtocol. Instantiated
//                        in Constructor. Use and Change of Said in createSortSearchInterface(),
//                        & getAdvancedSortSearchSQL().
//        4.80 03/10/2011 Class Method getAdvancedSortSearch() Changes in sqlStatementString
//                        to Properly Format Searches to Oracle Date & Timestamp Fields With
//                        TO_DATE() & TO_TIMESTAMP().
//        4.81 06/10/2011 Added Argument columnClassHashMap to Constructor. Class Method 
//                        getAdvancedSortSearchSQL() Added Method Instance columnClassString,
//                        & Exclusion of Quoting Numeric Fields for Key Validation for MS
//                        Access. In Same Method & DB Removal of LIMIT to SQL Statement.
//        4.82 06/11/2011 Changed Class Instance subProtocol to dataSourceType. Derivation
//                        in Constructor From ConnectionManager.getDataSourceType(). Class
//                        Methods Effected createSortSearchInterface() & getAdvancedSortSearchSQL().
//                        Removed in Constructor Instance connectionProperties.
//        4.83 06/12/2011 Class Method getAdvancedSortSearchSQL() Implements Quotes of # for
//                        MS Access Datetime Fields Keys.
//        4.84 11/02/2011 Implemented Aggregate & GROUP BY SQL. Added Class Instances
//                        aggregateFunction/ComboBox, groupFormExpressionNumber, groupComboBox,
//                        group_AscendingDecendingComboBox. Removed sort/searchButton & Replaced
//                        with applyButton. Aggregate Implemented in Constructor via aggregatePanel
//                        & GROUP BY createSortSearchInterface(). SQL Statements Generation Changes
//                        to New Feature in getAdvancedSortSearchSQL().
//        4.85 11/13/2011 Increase of groupFormExpressionNumber to 5 & Condensed SQL Statement
//                        Generation for GROUP BY in getAdvancedSortSearchSQL(). aggregateFunctions
//                        Changed of Ave to Avg. Proper Resetting of aggregateFunctionComboBox &
//                        aggregateComboBox in actionPerformed(). Resource Collection for Ascending
//                        Descending in createSortSearchInterface(). Changes in orderString() to
//                        Accomodate for Empty String in Order ComboBoxes.
//        4.86 01/01/2012 Copyright Update.
//        4.87 01/16/2012 Made a Copy of columnNames Argument to comboBoxColumnNames in Constructor.
//                        Correction in Class Method createSortSearchSQL() to GROUP BY SQL Creation
//                        to Properly Exclude When ascDescString is Empty String.
//        4.88 01/21/2012 Correction After Revision 4.87 Changed ascDescString Determination Outside
//                        Conditional Check for GROUP BY Aspect.
//        4.89 05/07/2012 Changed Class Instances comboBoxColumnNames & stateComponents from Vector
//                        Data Types to ArrayList. Same for Constructor Argument columnNames.
//        4.90 05/08/2012 Added an Emtpy String at Beginning of comboBoxColumnNames. All the Result
//                        of Different Behavior of 4.89 ArrayList Change.
//        4.91 07/02/2012 Changed Return Type on getAdvancedSortSearchSQL() to StringBuffer.
//        4.92 07/07/2012 Changes in Way MyJSQLView_ResourceBundle Handles the Collection
//                        of Resource Strings. Change to resource.getResourceString(key,
//                        default).
//        4.93 08/19/2012 Collection of All Image Resources Through resourceBundle.
//        4.94 09/11/2012 Changed Package Name to com.dandymadeproductions.myjsqlview.gui.forms.
//                        Made Class, Constructor, & Class Methods center(), getKeyComponents(),
//                        setKeyComponents(), getAdvancedSortSearchSQL() Public.
//        4.95 09/18/2012 Made Class Instance applyButton Private and Added getApplyButton().
//        4.96 10/19/2012 Dressed All JComboBoxes & JTextFields in Form With New Borders.
//        4.97 02/17/2013 Added derbyWhereOperators in Method createSortSearchInterface() & Implemented
//                        Proper LIMIT Construct for Derby in getAdvancedSortSearchSQL().
//        4.98 07/02/2013 Change in getAdvancedSortSearchSQL() to Use DBTablePanel.
//                        getGeneralDBProperties().
//        4.99 10/06/2013 Constructor Set Frame's Icon.
//        5.00 02/14/2014 Added Instance mssqlWhereOperators in createSortSearchInterface() &
//                        Selection Condition.
//        5.01 06/16/2014 Method createSortSearchInterface() Inclusion of whereOperators to
//                        be Defined as mysqlWhereOperators for MariaDB.
//        5.02 10/28/2015 Parameterized All JComboBox Class Instances to Conform With JRE 7.
//                      
//-----------------------------------------------------------------
//                 danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview.gui.forms;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

import com.dandymadeproductions.myjsqlview.MyJSQLView;
import com.dandymadeproductions.myjsqlview.datasource.ConnectionManager;
import com.dandymadeproductions.myjsqlview.gui.HelpFrame;
import com.dandymadeproductions.myjsqlview.gui.panels.DBTablesPanel;
import com.dandymadeproductions.myjsqlview.utilities.MyJSQLView_ResourceBundle;
import com.dandymadeproductions.myjsqlview.utilities.MyJSQLView_Utils;

/**
 *    The AdvancedSortSearchForm class provides a generic panel that is used to
 * create an advanced sort/search query for the calling TableTabPanel's database
 * table.
 * 
 * @author Dana M. Proctor
 * @version 5.02 10/28/2014
 */

public class AdvancedSortSearchForm extends JFrame implements ActionListener
{
   // Class Instances.
   private static final long serialVersionUID = -2663401193471271657L;

   private String sqlTable;
   private String dataSourceType, identifierQuoteString;
   private HashMap<String, String> columnNamesHashMap;
   private HashMap<String, String> columnClassHashMap;
   private HashMap<String, String> columnTypesHashMap;
   private ArrayList<String> comboBoxColumnNames;
   private MyJSQLView_ResourceBundle resourceBundle;

   private GridBagLayout gridbag;
   private GridBagConstraints constraints;

   private JPanel sortSearchPanel;
   private JButton questionButton;
   private JComboBox<String> selectTypeComboBox;
   private JComboBox<Object> aggregateFunctionComboBox, aggregateComboBox;
   
   private static final int sortFormExpressionNumber = 3;
   private JComboBox<Object>[] sortComboBox, sort_AscendingDescendingComboBox;
   
   private static final int groupFormExpressionNumber = 5;
   private JComboBox<Object> groupComboBox[], group_AscendingDescendingComboBox[];
   
   private static final int searchFormExpressionNumber = 5;
   private JComboBox<Object>[] searchComboBox, operatorComboBox, andOrComboBox;
   private JTextField[] searchTextField;
   private ArrayList<JComponent> stateComponents;

   private JButton closeButton, clearButton;
   private JButton applyButton;

   //==============================================================
   // AdvancedSortSearchForm Constructor
   //==============================================================

   @SuppressWarnings("unchecked")
   public AdvancedSortSearchForm(String table, MyJSQLView_ResourceBundle resourceBundle,
                                    HashMap<String, String> columnNamesHashMap,
                                    HashMap<String, String> columnClassHashMap,
                                    HashMap<String, String> columnTypesHashMap,
                                    ArrayList<String> columnNames)
   {
      sqlTable = table;
      this.resourceBundle = resourceBundle;
      this.columnNamesHashMap = columnNamesHashMap;
      this.columnClassHashMap = columnClassHashMap;
      this.columnTypesHashMap = columnTypesHashMap;
      
      // Constructor Instances
      JPanel mainPanel, formPanel, northPanel, selectTypePanel, aggregatePanel;
      JPanel helpPanel, southPanel, actionButtonPanel, clearPanel;
      JLabel selectTypeLabel, aggregateLabel;
      Object[] aggregateFunctions = {"", "Avg", "Count", "First", "Last", "Max", "Min", "Sum"};   
      String resource, iconsDirectory;
      ImageIcon questionIcon;
      ImageIcon clearIcon;

      // Setting up icons directory and other instances.
      
      comboBoxColumnNames = new ArrayList<String> ();
      comboBoxColumnNames.add("");
      
      for (int i = 0; i < columnNames.size(); i++)
         comboBoxColumnNames.add(columnNames.get(i));
      
      dataSourceType = ConnectionManager.getDataSourceType();
      
      resource = resourceBundle.getResourceString("AdvancedSortSearchForm.message.Title",
                                                  "Advanced Sort/Search");
      setTitle(resource + " : " + table);
      setIconImage(MyJSQLView_Utils.getFrameIcon());

      iconsDirectory = MyJSQLView_Utils.getIconsDirectory() + MyJSQLView_Utils.getFileSeparator();
      identifierQuoteString = ConnectionManager.getIdentifierQuoteString();

      stateComponents = new ArrayList <JComponent>();

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

      // Select Type Option */Distinct, Aggregate, & Help

      northPanel = new JPanel(gridbag);

      // All/Distinct
      selectTypePanel = new JPanel();
      selectTypePanel.setLayout(new FlowLayout(FlowLayout.LEADING));

      resource = resourceBundle.getResourceString("AdvancedSortSearchForm.label.Select", "Select");
      selectTypeLabel = new JLabel(resource + " : ");
      selectTypePanel.add(selectTypeLabel);

      selectTypeComboBox = new JComboBox<String>();
      selectTypeComboBox.setBorder(BorderFactory.createLoweredBevelBorder());
      selectTypeComboBox.addItem("All");
      selectTypeComboBox.addItem("Distinct");
      stateComponents.add(selectTypeComboBox);
      selectTypePanel.add(selectTypeComboBox);
      
      buildConstraints(constraints, 0, 0, 1, 1, 35, 100);
      constraints.fill = GridBagConstraints.BOTH;
      constraints.anchor = GridBagConstraints.WEST;
      gridbag.setConstraints(selectTypePanel, constraints);
      northPanel.add(selectTypePanel);
      
      // Aggregate
      aggregatePanel = new JPanel();
      aggregatePanel.setLayout(new FlowLayout(FlowLayout.LEADING));
      
      resource = resourceBundle.getResourceString("AdvancedSortSearchForm.label.Aggregate", "Aggregate");
      aggregateLabel = new JLabel(resource + " : ");
      aggregatePanel.add(aggregateLabel);

      aggregateFunctionComboBox = new JComboBox<Object>(aggregateFunctions);
      aggregateFunctionComboBox.setBorder(BorderFactory.createLoweredBevelBorder());
      stateComponents.add(aggregateFunctionComboBox);
      aggregatePanel.add(aggregateFunctionComboBox);
      
      aggregateComboBox = new JComboBox<Object>(comboBoxColumnNames.toArray());
      aggregateComboBox.setBorder(BorderFactory.createLoweredBevelBorder());
      stateComponents.add(aggregateComboBox);
      aggregatePanel.add(aggregateComboBox);
      
      buildConstraints(constraints, 1, 0, 1, 1, 60, 100);
      constraints.fill = GridBagConstraints.BOTH;
      constraints.anchor = GridBagConstraints.WEST;
      gridbag.setConstraints(aggregatePanel, constraints);
      northPanel.add(aggregatePanel);

      // Help
      helpPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));

      questionIcon = resourceBundle.getResourceImage(iconsDirectory + "bulbIcon.png");
      questionButton = new JButton(questionIcon);
      questionButton.setFocusPainted(false);
      questionButton.setBorder(BorderFactory.createRaisedBevelBorder());
      questionButton.addActionListener(this);
      helpPanel.add(questionButton);

      buildConstraints(constraints, 2, 0, 1, 1, 60, 100);
      constraints.fill = GridBagConstraints.BOTH;
      constraints.anchor = GridBagConstraints.EAST;
      gridbag.setConstraints(helpPanel, constraints);
      northPanel.add(helpPanel);
      
      mainPanel.add(northPanel, BorderLayout.NORTH);

      // Sort/Group/Search Interface

      sortSearchPanel = new JPanel();
      sortSearchPanel.setLayout(gridbag);
      sortSearchPanel.setBorder(BorderFactory.createLoweredBevelBorder());

      sortComboBox = new JComboBox[sortFormExpressionNumber];
      sort_AscendingDescendingComboBox = new JComboBox[sortFormExpressionNumber];
      groupComboBox = new JComboBox[groupFormExpressionNumber];
      group_AscendingDescendingComboBox = new JComboBox[groupFormExpressionNumber];
      searchComboBox = new JComboBox[searchFormExpressionNumber];
      operatorComboBox = new JComboBox[searchFormExpressionNumber];
      andOrComboBox = new JComboBox[searchComboBox.length - 1];
      searchTextField = new JTextField[searchFormExpressionNumber];
      
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

      // Apply Button
      resource = resourceBundle.getResourceString("AdvancedSortSearchForm.button.Apply", "Apply");
      applyButton = new JButton(resource);
      applyButton.setFocusPainted(false);
      actionButtonPanel.add(applyButton);
      
      // Close Button
      resource = resourceBundle.getResourceString("AdvancedSortSearchForm.button.Close", "Close");
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
      clearIcon = resourceBundle.getResourceImage(iconsDirectory + "deleteIcon.png");
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
      (this.getRootPane()).setDefaultButton(applyButton);

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
            // Reset all the forms components.
            
            selectTypeComboBox.setSelectedIndex(0);
            aggregateFunctionComboBox.setSelectedIndex(0);
            aggregateComboBox.setSelectedIndex(0);
            
            int i = 0;
            do
            {
               if (i < sortFormExpressionNumber)
               {
                  sortComboBox[i].setSelectedIndex(0);
                  sort_AscendingDescendingComboBox[i].setSelectedIndex(0);
               }
               
               if (i < groupFormExpressionNumber)
               {
                  groupComboBox[i].setSelectedIndex(0);
                  group_AscendingDescendingComboBox[i].setSelectedIndex(0);
               }
               
               searchComboBox[i].setSelectedIndex(0);
               operatorComboBox[i].setSelectedIndex(0);
               if (i < andOrComboBox.length)
                  andOrComboBox[i].setSelectedIndex(0);
               searchTextField[i].setText("");
               
               i++;
            }
            while (i < searchFormExpressionNumber);
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

   public void center()
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
      JPanel sortPanel, groupPanel, searchPanel;
      String resourceSortBy, resourceOrderASC, resourceOrderDESC, resourceThen,
             resourceGroupBy, resourceSearch;
      
      JLabel[] sortByLabel, sortThenLabel; 
      JLabel[] groupByLabel, groupThenLabel;
      JLabel[] searchLabel;
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
      Object[] derbyWhereOperators = {"LIKE", "NOT LIKE", "IS NULL", "IS NOT NULL", "IN", "NOT IN", "BETWEEN",
                                      "NOT BETWEEN", "EXISTS", "OR", "AND", "=", "<", "<=", ">", ">=", "<>"};
      Object[] mssqlWhereOperators = {"LIKE", "NOT LIKE", "IS NULL", "IS NOT NULL", "IN", "NOT IN", "BETWEEN",
                                      "=", "<", "<=", ">", ">=", "<>", "!=", "!<", "!>"};

      // Assigning the appropriate string array WHERE operators.
      
      if (dataSourceType.equals(ConnectionManager.MYSQL)
          || dataSourceType.equals(ConnectionManager.MARIADB))
         whereOperators = mysqlWhereOperators;
      else if (dataSourceType.equals(ConnectionManager.POSTGRESQL))
         whereOperators = postgreSQLWhereOperators;
      else if (dataSourceType.equals(ConnectionManager.ORACLE))
         whereOperators = oracleWhereOperators;
      else if (dataSourceType.equals(ConnectionManager.SQLITE))
         whereOperators = sqliteWhereOperators;
      else if (dataSourceType.equals(ConnectionManager.DERBY))
         whereOperators = derbyWhereOperators;
      else if (dataSourceType.equals(ConnectionManager.MSSQL))
         whereOperators = mssqlWhereOperators;
      // Make HSQL Default
      else
         whereOperators = hsqlWhereOperators;

      // ========================
      // Sort Interface Setup.

      sortPanel = new JPanel();
      sortPanel.setLayout(gridbag);
      sortPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLoweredBevelBorder(),
                                                             BorderFactory.createEmptyBorder(10, 6, 10, 6)));
      
      sortByLabel = new JLabel[sortFormExpressionNumber];
      sortThenLabel = new JLabel[sortFormExpressionNumber -1];

      resourceSortBy = resourceBundle.getResourceString("AdvancedSortSearchForm.label.SortBy",
                                                        "Sort By");
      resourceSortBy = resourceSortBy + " : ";
      
      resourceThen = resourceBundle.getResourceString("AdvancedSortSearchForm.label.Then", "Then");
      resourceThen = resourceThen + ", ";
      
      resourceOrderASC = resourceBundle.getResourceString("AdvancedSortSearchForm.combobox.Ascending",
                                                          "Ascending");
      
      resourceOrderDESC = resourceBundle.getResourceString("AdvancedSortSearchForm.combobox.Descending",
                                                           "Descending");
      
      int i = 0;
      do
      {
         sortByLabel[i] = new JLabel(resourceSortBy, JLabel.LEADING);
        
         buildConstraints(constraints, 0, i, 1, 1, 100, 100);
         constraints.fill = GridBagConstraints.NONE;
         constraints.anchor = GridBagConstraints.WEST;
         gridbag.setConstraints(sortByLabel[i], constraints);
         sortPanel.add(sortByLabel[i]);

         sortComboBox[i] = new JComboBox<Object>(comboBoxColumnNames.toArray());
         sortComboBox[i].setBorder(BorderFactory.createLoweredBevelBorder());
         stateComponents.add(sortComboBox[i]);

         buildConstraints(constraints, 1, i, 1, 1, 100, 100);
         constraints.fill = GridBagConstraints.NONE;
         constraints.anchor = GridBagConstraints.WEST;
         gridbag.setConstraints(sortComboBox[i], constraints);
         sortPanel.add(sortComboBox[i]);

         sort_AscendingDescendingComboBox[i] = new JComboBox<Object>();
         sort_AscendingDescendingComboBox[i].setBorder(BorderFactory.createLoweredBevelBorder());
         sort_AscendingDescendingComboBox[i].addItem(resourceOrderASC);
         sort_AscendingDescendingComboBox[i].addItem(resourceOrderDESC);
         stateComponents.add(sort_AscendingDescendingComboBox[i]);

         buildConstraints(constraints, 2, i, 1, 1, 100, 100);
         constraints.fill = GridBagConstraints.NONE;
         constraints.anchor = GridBagConstraints.WEST;
         gridbag.setConstraints(sort_AscendingDescendingComboBox[i], constraints);
         sortPanel.add(sort_AscendingDescendingComboBox[i]);
         
         if (i < sortThenLabel.length)
         {
            sortThenLabel[i] = new JLabel(resourceThen, JLabel.LEADING);

            buildConstraints(constraints, 3, i, 1, 1, 100, 100);
            constraints.fill = GridBagConstraints.NONE;
            constraints.anchor = GridBagConstraints.WEST;
            gridbag.setConstraints(sortThenLabel[i], constraints);
            sortPanel.add(sortThenLabel[i]);
         }

         i++;
      }
      while (i < sortFormExpressionNumber);
      
      buildConstraints(constraints, 0, 0, 1, 1, 100, 25);
      constraints.fill = GridBagConstraints.BOTH;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(sortPanel, constraints);
      sortSearchPanel.add(sortPanel);
      
      // ============================
      // Group By Interface Setup
      
      groupPanel = new JPanel();
      groupPanel.setLayout(gridbag);
      groupPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLoweredBevelBorder(),
                                          BorderFactory.createEmptyBorder(10, 6, 10, 6)));
      
      groupByLabel = new JLabel[groupFormExpressionNumber];
      groupThenLabel = new JLabel[groupFormExpressionNumber -1];

      resourceGroupBy = resourceBundle.getResourceString("AdvancedSortSearchForm.label.GroupBy",
                                                         "Group By");
      resourceGroupBy = resourceGroupBy + " : ";
      
      i = 0;
      do
      {
         groupByLabel[i] = new JLabel(resourceGroupBy, JLabel.LEADING);
        
         buildConstraints(constraints, 0, i, 1, 1, 100, 100);
         constraints.fill = GridBagConstraints.NONE;
         constraints.anchor = GridBagConstraints.WEST;
         gridbag.setConstraints(groupByLabel[i], constraints);
         groupPanel.add(groupByLabel[i]);

         groupComboBox[i] = new JComboBox<Object>(comboBoxColumnNames.toArray());
         groupComboBox[i].setBorder(BorderFactory.createLoweredBevelBorder());
         stateComponents.add(groupComboBox[i]);

         buildConstraints(constraints, 1, i, 1, 1, 100, 100);
         constraints.fill = GridBagConstraints.NONE;
         constraints.anchor = GridBagConstraints.WEST;
         gridbag.setConstraints(groupComboBox[i], constraints);
         groupPanel.add(groupComboBox[i]);

         group_AscendingDescendingComboBox[i] = new JComboBox<Object>();
         group_AscendingDescendingComboBox[i].setBorder(BorderFactory.createLoweredBevelBorder());
         group_AscendingDescendingComboBox[i].addItem("");
         group_AscendingDescendingComboBox[i].addItem(resourceOrderASC);
         group_AscendingDescendingComboBox[i].addItem(resourceOrderDESC);
         stateComponents.add(group_AscendingDescendingComboBox[i]);

         buildConstraints(constraints, 2, i, 1, 1, 100, 100);
         constraints.fill = GridBagConstraints.NONE;
         constraints.anchor = GridBagConstraints.WEST;
         gridbag.setConstraints(group_AscendingDescendingComboBox[i], constraints);
         groupPanel.add(group_AscendingDescendingComboBox[i]);
         
         if (i < groupThenLabel.length)
         {
            groupThenLabel[i] = new JLabel(resourceThen, JLabel.LEADING);

            buildConstraints(constraints, 3, i, 1, 1, 100, 100);
            constraints.fill = GridBagConstraints.NONE;
            constraints.anchor = GridBagConstraints.WEST;
            gridbag.setConstraints(groupThenLabel[i], constraints);
            groupPanel.add(groupThenLabel[i]);
         }

         i++;
      }
      while (i < groupFormExpressionNumber);
      
      buildConstraints(constraints, 0, 1, 1, 1, 100, 25);
      constraints.fill = GridBagConstraints.BOTH;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(groupPanel, constraints);
      sortSearchPanel.add(groupPanel);
      
      // ============================
      // Search Interface Setup
      
      searchPanel = new JPanel();
      searchPanel.setLayout(gridbag);
      searchPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLoweredBevelBorder(),
                                          BorderFactory.createEmptyBorder(10, 6, 10, 6)));
      
      searchLabel = new JLabel[searchFormExpressionNumber];
      
      resourceSearch = resourceBundle.getResourceString("AdvancedSortSearchForm.label.Search",
                                                        "Search");
      resourceSearch = resourceSearch + " : ";
      
      i = 0;
      do
      {
         searchLabel[i] = new JLabel(resourceSearch, JLabel.LEFT);

         buildConstraints(constraints, 0, (i + 3), 1, 1, 100, 100);
         constraints.fill = GridBagConstraints.NONE;
         constraints.anchor = GridBagConstraints.CENTER;
         gridbag.setConstraints(searchLabel[i], constraints);
         searchPanel.add(searchLabel[i]);

         searchComboBox[i] = new JComboBox<Object>(comboBoxColumnNames.toArray());
         searchComboBox[i].setBorder(BorderFactory.createLoweredBevelBorder());
         stateComponents.add(searchComboBox[i]);

         buildConstraints(constraints, 1, (i + 3), 1, 1, 100, 100);
         constraints.fill = GridBagConstraints.NONE;
         constraints.anchor = GridBagConstraints.CENTER;
         gridbag.setConstraints(searchComboBox[i], constraints);
         searchPanel.add(searchComboBox[i]);

         operatorComboBox[i] = new JComboBox<Object>(whereOperators);
         operatorComboBox[i].setBorder(BorderFactory.createLoweredBevelBorder());
         stateComponents.add(operatorComboBox[i]);

         buildConstraints(constraints, 2, (i + 3), 1, 1, 100, 100);
         constraints.fill = GridBagConstraints.NONE;
         constraints.anchor = GridBagConstraints.CENTER;
         gridbag.setConstraints(operatorComboBox[i], constraints);
         searchPanel.add(operatorComboBox[i]);

         searchTextField[i] = new JTextField(15);
         searchTextField[i].setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEtchedBorder(EtchedBorder.RAISED),
            BorderFactory.createLoweredBevelBorder()));
         stateComponents.add(searchTextField[i]);

         buildConstraints(constraints, 3, (i + 3), 1, 1, 100, 100);
         constraints.fill = GridBagConstraints.NONE;
         constraints.anchor = GridBagConstraints.CENTER;
         gridbag.setConstraints(searchTextField[i], constraints);
         searchPanel.add(searchTextField[i]);

         if (i < andOrComboBox.length)
         {
            andOrComboBox[i] = new JComboBox<Object>();
            andOrComboBox[i].setBorder(BorderFactory.createLoweredBevelBorder());
            andOrComboBox[i].addItem("And");
            andOrComboBox[i].addItem("Or");
            stateComponents.add(andOrComboBox[i]);

            buildConstraints(constraints, 4, (i + 3), 1, 1, 100, 100);
            constraints.fill = GridBagConstraints.NONE;
            constraints.anchor = GridBagConstraints.CENTER;
            gridbag.setConstraints(andOrComboBox[i], constraints);
            searchPanel.add(andOrComboBox[i]);
         }
         
         i++;
      }
      while (i < searchFormExpressionNumber);
      
      // Swap last state components so split in setKeyComponents() will behave
      // correctly. Will not except an empty string, our searchTextField, as the
      // last field. They are only two andOrComboBoxes.
      
      swapEndComponent = stateComponents.get(stateComponents.size() - 1);
      stateComponents.set(stateComponents.size() - 1, stateComponents.get(stateComponents.size() - 2));
      stateComponents.set(stateComponents.size() - 2, swapEndComponent);
      
      buildConstraints(constraints, 0, 2, 1, 1, 100, 50);
      constraints.fill = GridBagConstraints.BOTH;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(searchPanel, constraints);
      sortSearchPanel.add(searchPanel);
   }

   //==============================================================
   // Class method to get the SQL statement string that is a result
   // of the selected/input parameters of the form.
   //==============================================================

   public StringBuffer getAdvancedSortSearchSQL(String sqlTableFieldsString, int tableRowStart,
                                             int tableRowLimit)
   {
      // Method Instances
      StringBuffer sqlStatementString;
      String aggregateFunctionString, aggregateField;
      String whereString, unionString, orderString, ascDescString;
      String columnNameString, columnClassString, columnTypeString;
      String operatorString, searchString;
      boolean notFieldSort, notFieldGroup;

      sqlStatementString = new StringBuffer();
      sqlStatementString.append("SELECT ");

      // ========================================
      // Adding DISTINCT & Aggregate options as needed.

      if (selectTypeComboBox.getSelectedItem().equals("All"))
      {
         aggregateFunctionString = (String) aggregateFunctionComboBox.getSelectedItem();
         aggregateField = columnNamesHashMap.get((String) aggregateComboBox.getSelectedItem());
         
         if (aggregateField != null && !aggregateFunctionString.equals("")
               && sqlTableFieldsString.indexOf(aggregateField) != -1)
         {
            aggregateField = identifierQuoteString + aggregateField + identifierQuoteString;
            sqlTableFieldsString = sqlTableFieldsString.replace(aggregateField, aggregateFunctionString
                                                                + "(" + aggregateField + ") AS "
                                                                + aggregateField);
         }
            
         sqlStatementString.append(sqlTableFieldsString + " FROM " + sqlTable + " ");
      }
      else
         sqlStatementString.append("DISTINCT " + sqlTableFieldsString + " FROM " + sqlTable + " ");

      // ========================================
      // Adding the search(s), WHERE, option(s).
      
      int i = 0;
      whereString = "WHERE ";
      unionString = "";
      do
      {
         columnNameString = columnNamesHashMap.get(searchComboBox[i].getSelectedItem());
         columnClassString = columnClassHashMap.get(searchComboBox[i].getSelectedItem());
         columnTypeString = columnTypesHashMap.get(searchComboBox[i].getSelectedItem());
         operatorString = (String) operatorComboBox[i].getSelectedItem();
         searchString = searchTextField[i].getText();

         if (columnNameString != null
             && (!searchString.equals("") || operatorString.toLowerCase().indexOf("null") != -1))
         {
            if (i > 0)
               sqlStatementString.append(unionString.equals("") ? "WHERE " : unionString);
            
            if (operatorString.toLowerCase().indexOf("null") != -1)
               sqlStatementString.append(whereString + identifierQuoteString + columnNameString
                                         + identifierQuoteString + " " + operatorString + " ");
            else
            {
               if (operatorString.equals("<=>") && searchString.toLowerCase().equals("null"))
                  sqlStatementString.append(whereString + identifierQuoteString + columnNameString
                                            + identifierQuoteString + " " + operatorString
                                            + " " + searchString + " ");
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
                                                    searchString,
                                                    DBTablesPanel.getGeneralDBProperties().getViewDateFormat())
                                                    + "', 'YYYY-MM-dd') ");
                     }
                     else
                     {
                        searchString = MyJSQLView_Utils.processDateFormatSearch(searchString);
                        sqlStatementString.append(whereString + identifierQuoteString + columnNameString
                                                  + identifierQuoteString + " " + operatorString + " '"
                                                  + searchString + "' ");
                     }
                  }
                  else if (columnTypeString.equals("DATETIME") || columnTypeString.indexOf("TIMESTAMP") != -1)
                  {
                     if (searchString.indexOf(" ") != -1)
                        searchString = MyJSQLView_Utils.processDateFormatSearch(
                           searchString.substring(0, searchString.indexOf(" ")))
                           + searchString.substring(searchString.indexOf(" "));
                     else if (searchString.indexOf("-") != -1 || searchString.indexOf("/") != -1)
                        searchString = MyJSQLView_Utils.processDateFormatSearch(searchString);
                     
                     if (dataSourceType.equals(ConnectionManager.ORACLE) 
                           && columnTypeString.indexOf("TIMESTAMP") != -1)
                     {
                        sqlStatementString.append(whereString + identifierQuoteString + columnNameString
                                                  + identifierQuoteString + " " + operatorString
                                                  + " TO_TIMESTAMP('" + searchString
                                                  + "', 'YYYY-MM-dd HH24:MI:SS') ");
                     }
                     else
                     {
                        if (dataSourceType.equals(ConnectionManager.MSACCESS))
                           sqlStatementString.append(whereString + identifierQuoteString + columnNameString
                                                     + identifierQuoteString + " " + operatorString + " #"
                                                     + searchString + "# ");
                        else
                           sqlStatementString.append(whereString + identifierQuoteString + columnNameString
                                                     + identifierQuoteString + " " + operatorString + " '"
                                                     + searchString + "' ");
                     }
                  }
                  else
                  {
                     // Character data gets single quotes for some databases,
                     // not numbers though.
                          
                     if ((dataSourceType.equals(ConnectionManager.MSACCESS)
                          || dataSourceType.indexOf(ConnectionManager.HSQL) != -1
                          || dataSourceType.equals(ConnectionManager.DERBY))
                         && columnClassString.toLowerCase().indexOf("string") == -1)
                        sqlStatementString.append(whereString + identifierQuoteString + columnNameString
                                                  + identifierQuoteString + " " + operatorString + " "
                                                  + searchString + " ");
                     else
                        sqlStatementString.append(whereString + identifierQuoteString + columnNameString
                                                  + identifierQuoteString + " " + operatorString + " '"
                                                  + searchString + "' ");
                  }
               }
            }
            
            if (i < andOrComboBox.length)
               unionString = ((String) andOrComboBox[i].getSelectedItem()).toUpperCase() + " ";
         }
         i++;
         whereString = "";
      }
      while (i < searchFormExpressionNumber);
      
      // ========================================
      // Adding the sort(s), GROUP BY, option.
      
      orderString = "";
      ascDescString = "";
      notFieldGroup = true;
      
      i = 0;
      do
      {
         columnNameString = columnNamesHashMap.get(groupComboBox[i].getSelectedItem());
         
         if (columnNameString != null)
         {
            ascDescString = orderString(i, group_AscendingDescendingComboBox);    
            ascDescString = ascDescString.equals("") ? ascDescString : " " + ascDescString;
            orderString = identifierQuoteString + columnNameString + identifierQuoteString + ascDescString + ", ";
            sqlStatementString.append((notFieldGroup ? "GROUP BY " : "") + orderString);
            
            if (sqlStatementString.toString().contains("GROUP BY"))
               notFieldGroup = false;   
         }
         
         i++;
         orderString = "";
      }
      while (i < groupFormExpressionNumber);
      
      // Remove trailing commas as needed.
      if (!notFieldGroup && sqlStatementString.length() >= 2)
      {
         sqlStatementString.delete(sqlStatementString.length() - 2, sqlStatementString.length());
         sqlStatementString.append(" ");
      }
        
      // ========================================
      // Adding the sort(s), ORDER BY, option.
      
      columnNameString = columnNamesHashMap.get(sortComboBox[0].getSelectedItem());
      ascDescString = "";
      notFieldSort = true;

      if (columnNameString != null)
      {
         sqlStatementString.append("ORDER BY " + identifierQuoteString + columnNameString + identifierQuoteString
                               + " ");
         ascDescString = orderString(0, sort_AscendingDescendingComboBox);
         notFieldSort = false;
      }

      columnNameString = columnNamesHashMap.get(sortComboBox[1].getSelectedItem());

      if (columnNameString != null)
      {
         sqlStatementString.append(notFieldSort ? "ORDER BY " : ascDescString + ", ");
         sqlStatementString.append(identifierQuoteString + columnNameString + identifierQuoteString + " ");
         ascDescString = orderString(1, sort_AscendingDescendingComboBox);
         notFieldSort = false;
      }

      columnNameString = columnNamesHashMap.get(sortComboBox[2].getSelectedItem());

      if (columnNameString != null)
      {
         sqlStatementString.append(notFieldSort ? "ORDER BY " : ascDescString + ", ");
         sqlStatementString.append(identifierQuoteString + columnNameString + identifierQuoteString + " ");
         ascDescString = orderString(2, sort_AscendingDescendingComboBox);
      }

      if (!ascDescString.equals(""))
         sqlStatementString.append(ascDescString + " ");
      
      // ========================================
      // Adding the LIMIT option.

      if (!dataSourceType.equals(ConnectionManager.MSACCESS))
      {
         if (dataSourceType.equals(ConnectionManager.DERBY))
            sqlStatementString.append("OFFSET " + tableRowStart + " ROWS FETCH NEXT " + tableRowLimit
                                      + " ROWS ONLY");
         else
            sqlStatementString.append("LIMIT " + tableRowLimit + " OFFSET " + tableRowStart);
            
      }

      // Return the resultant query.

      // System.out.println(sqlStatementString.toString());
      return sqlStatementString;
      
      // Sample outline of what a basic SQL SELECT query should be.
      // It was determined in the initial version to not include
      // HAVING option.

      /*
       * sqlStatementString = "SELECT * " + "FROM " + sqlTable + " "
       * "GROUP BY " + columnNamesHashMap.get(sortComboBox.getSelectedItem()) + " " +
       * "WHERE " + columnSearchString + " " + "LIKE " + searchTextString + " " + "ORDER BY " +
       * columnNamesHashMap.get(sortComboBox.getSelectedItem()) + " " + "LIMIT " +
       * tableRowStart + "," + tableRowLimit;
       */ 
   }

   //==============================================================
   // Class method to return the properly formatted text for the
   // GROUP BY & ORDER BY SQL statements using 'ASC' or 'DESC'.
   //==============================================================

   private String orderString(int option, JComboBox<Object>[] orderComboBox)
   {
      // Method Instances.
      String ascendingDescendingString;
      int indexer, selectedIndex;
      
      if (option >= 0 && option < orderComboBox.length)
      {
         // Order ComboBox may have three options, "".
         if (orderComboBox[option].getItemCount() == 2)
            indexer = 1;
         else
            indexer = 0;
         
         selectedIndex = orderComboBox[option].getSelectedIndex() + indexer;
            
         if (selectedIndex == 0)
            ascendingDescendingString = "";
         else if (selectedIndex == 1)
            ascendingDescendingString = "ASC";
         else
            ascendingDescendingString = "DESC";
      }
      else
         ascendingDescendingString = "";
      
      // System.out.println(ascendingDescendingString);
      return ascendingDescendingString;
   }
   
   //==============================================================
   // Class method for outside classes to obtain the apply button
   // which indicates the user wishes to process the forms
   // selections.
   //==============================================================

   public JButton getApplyButton()
   {
      return applyButton;
   }
   
   //==============================================================
   // Class method for outside classes to obtain the current state
   // of the form.
   //==============================================================

   @SuppressWarnings("unchecked")
   public String getKeyComponentsState()
   {
      // Method Instances
      StringBuffer stateString;
      String delimiter;
      Iterator<JComponent> keyComponentIterator;
      Object currentComponent;

      // Initialize and obtain key components state.

      stateString = new StringBuffer("");
      delimiter = getKeyComponentsDelimiter();

      keyComponentIterator = stateComponents.iterator();

      while (keyComponentIterator.hasNext())
      {
         currentComponent = keyComponentIterator.next();

         if (currentComponent instanceof JComboBox)
            stateString.append(((JComboBox<Object>) currentComponent).getSelectedIndex() + delimiter);

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
   
   @SuppressWarnings("unchecked")
   public void setKeyComponentsState(String stateString)
   {
      // Method Instances
      String delimiter;
      String[] keyComponentSettings;
      Iterator<JComponent> keyComponentIterator;
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

               if (currentComponent instanceof JComboBox)
               {
                  comboBoxItemCount = ((JComboBox<Object>) currentComponent).getItemCount();
                  stateIndex = Integer.parseInt(keyComponentSettings[i]);

                  if (stateIndex > -1 && stateIndex < comboBoxItemCount)
                     ((JComboBox<Object>) currentComponent).setSelectedIndex(stateIndex);
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
         
         resourceMessage = resourceBundle.getResourceString("AdvancedSortSearchForm.dialogmessage.SetFields",
            "Unable to Properly Set Advanced Sort/Search Fields.");  
         resourceTitle = resourceBundle.getResourceString("AdvancedSortSearchForm.dialogtitle.Alert",
                                                          "Alert");
         
         JOptionPane.showMessageDialog(null, resourceMessage, resourceTitle, JOptionPane.ERROR_MESSAGE);
      }
   }
}
