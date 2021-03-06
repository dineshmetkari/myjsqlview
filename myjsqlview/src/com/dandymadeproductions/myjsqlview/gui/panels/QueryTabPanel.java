//=================================================================
//                MyJSQLView Query Tab Panel
//=================================================================
//
// 	This class provides the view of resultant data from a
// query. The panel allows the copying, sorting, and searching
// of the data.
//
//                 << QueryTabPanel.java >>
//
//=================================================================
// Copyright (C) 2005-2015 Dana M. Proctor
// Version 10.6 05/09/2015
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
// Version 1.0 Original QueryTabPanel Class.
//         1.1 Replaced Blob Data In Table With Just Blob Type Information.
//         1.2 Modified Table Creation to be Aborted if Class Method loadTable
//             Query Fails.
//         1.3 Class Method buildConstraints().
//         1.4 Class Method displayMyDateString().
//         1.5 Class Methods parseColumnNameString() & firstLetterToUpperCase().
//         1.6 Class Method loadTable() Replaced the DATE, DATETIME, TIMESTAMP,
//             and YEAR Filling with More Robustness. Removed Class Method
//             displayMyDateString().
//         1.7 Added a System.out for Instance mysqlStatementString In getColumnNames()
//             Class Method.
//         1.8 Stripped ";" From inputQuery Constructor Argument Instance
//             Per Fearow Recommedation.
//         1.9 Added Class Instances rowsLabel & preferredColumnSizeHashMap.
//             Implentation of Same in Panel, Row Position Indicator
//             and Auto-Sizing of Columns Based on Header/Content.
//         2.0 Implemented KeyListener for Class Instance searchTextField.
//         2.1 Implemented View of Entry, TableViewForm.
//         2.2 Added sqlTable Identifer Label, tableLabel.
//         2.3 Added All Getter Class Methods for Later Use in Data Export.
//         2.4 Removed TEXT Fields From Being Handled the Same as Blobs in
//             Class Methods loadTable() & viewSelectedItem.
//         2.5 Integrated the Advanced Sort/Search Interface Into the Panel.
//             Also Added Class Method setTableRowSize().
//         2.6 Class Method viewSelectedItem() Modified the Check from NULL
//             to Length Check to Properly Fill Button to Either byte Count
//             or 0. Code Cleanup.
//         2.7 More Code Cleanup.
//         2.8 Removed Unused Instance keyIterator & keyColumn in Class
//             Method viewSelectedItem(). Class Instance maxPreferredColumnSize
//             to Static.
//         2.9 Used System.getProperty("file.separator") for All File System
//             Resources Accesses Through Instance fileSeparator.
//         3.0 Class Instance tableScrollPane.setCorner(ScrollPaneConstants.UPPER_RIGHT_CORNER,
//             activateAdvancedSortSearchButton).
//         3.1 Set Class Instance advancedSortSearch Back to False in
//             actionPerformed() Method on Normal Sort/Search. Also Set
//             TEXT Fields to Be Properly Displayed in TableViewForm as
//             Buttons on Content On Activation in Class Method viewSelectedItem(),
//             Bug 143415.
//         3.2 Changed the Name of the Instances, sqlTable & primaryKey, to
//             Have the queryNumber as a Trailing Numerical Character.
//         3.3 Began Looking at PostgreSQL, Some Initial Changes in Class
//             Method loadTable(). Currently Broken for PostgreSQL.
//         3.4 Reviewed and Fixed to Properly Handle PostgreSQL Database
//             Queries. Changes to Mainly Class Method loadTable() &
//             viewSelectedItem().
//         3.5 Allowed TEXT to Be Viewed in Summary Table for PostgreSQL
//             Database, But Limited to No Larger Than 65535 in Class
//             Method loadTable().
//         3.6 Added Class Instance validQuery. Controlled in Class Method
//             getColumnNames().
//         3.7 Quoted the ORDER BY Identifier in Class Method loadTable().
//         3.8 Modified Query so That a More Robust Check is Made to Not
//             Include WHERE and ORDER Key Words as Needed in Class Method
//             loadTable().
//         3.9 Header Update.
//         4.0 Added/Implemented identiferQuoteString. Trimmed Default
//             Content in Summary Table.
//         4.1 Set the searchComboBox and searchTextField to Index 0 and Empty
//             Text During an Advanced Sort/Search Action. The Normal Fields
//             Always Take Precedence if No WHERE Stimpulation is Present, This
//             Limits the TemporyTable Content. This NOT the Indent of an
//             Advanced Sort/Search. The Sort is Alright Because it is Redone.
//             This Makes More Work Though for the Database.
//         4.2 Reviewed and Fixed to Properly Handle HSQL Database Queries.
//             Changes to Mainly Class Method loadTable() & viewSelectedItem().
//             Also Modifications to LIMIT Statement in getColumnNames().
//             Needs To Properly Handle SQL Keywords That May be Combinations
//             of UpperCase & LowerCase Characters. Also Needs Review Cleaning.
//         4.3 Added Class Method Instance tempQueryString to Class Method
//             loadTable(). Also Placed identifierQuoteString in sqlStatementString
//             to Properly Identify tempTable.
//         4.4 Added Class Method formatQuery(). Additional Check to Properly
//             Handle Lame LIMIT Parsing Problem With HSQLDB in Class Method
//             getColumnNames(). Other Minor Modifications to Processing of
//             Composing sqlStatementString in loadTable().
//         4.5 Removed doneButton Replaced With closeViewButton. Revamped
//             Actions Buttons Panel. Changed record to row. Added Argument
//             showQuery to Constructor. Class Method viewSelecteditem()
//             identifierQuoteString for sqlTable. Began Implementation of
//             Show Query.
//         4.6 Minor Format Changes. Instances mysqlStatement Changed to
//             sqlStatement.
//         4.7 Class Method viewSelectedItem() changes: Instance columnSize
//             to currentColumnSize, added currentColumnClass. Added Ability
//             to View Array the Same Way Text Data is Handled.
//         4.8 Class Method loadTable(), Limited Text Summary Table Content
//             to No Larger Then 512. Also the Same With Array Data.
//         4.9 Class Method loadTable() Single Quotes Placed Around WHERE
//             Condition "1" on NULL. Causes MyJSQLView to Crash With
//             PostgreSQL 8.3.0.
//         5.0 Added Additional Operand, validQuery, for Conditional Check in
//             Class Method actionPerformed() for Button Action.
//         5.1 Added Class Instance serialVersionUID. Declared dbConnection
//             Class Instance transient.
//         5.2 Added Argument "*" to Call to AdvancedSortSearchForm Method
//             getAdvancedSortSearchSQL() in Class Method loadTable().
//         5.3 Added int Field queryRowLimit to Constructor.
//         5.4 Reviewed and Fixed to Properly Handle Oracle Database Queries.
//             Changes to Mainly Class Method loadTable(). Implementation for
//             Oracle Lacks the VIEW Capability Becasue the Inability to Tie
//             a SEQUENCE, id_x, To the Generated Table Through a SubQuery.
//             Needs Review Cleaning & Testing With oracleTypes Table.
//         5.5 Changes in Class Method getColumnNames() to Properly Handle
//             Oracle BINARY_FLOAT/DOUBLE. Also Corrections in Same Method
//             for TIMESTAMPlTZ Fields, in Instance sqlTableFieldsString.
//             Tested With oracletypes.sql Table, Works But Still a Problem
//             With LONG Types. Looks Like the CREATE TABLE FROM SELECT.
//         5.6 Class Instance searchTextField Cleared on No Field Selection in
//             searchComboBox Activates Update Search.
//         5.7 MyJSQLView Project Common Source Code Formatting.
//         5.8 ReWorked to Reflect the New GUI Changes Associated With 2.91++
//             Development. Changes to Sort, Search, and Somewhat Action Panel.
//         5.9 Implemented the ASC/DESC New GUI Sort Option via the Standard
//             Sort/Search Panel.
//         6.0 Header Format Changes/Update.
//         6.1 Class Method loadTable() Conditional Creation in sqlStatement
//             String for ORDER Clause Modified to Detect ' order' With a
//             Initial Space In Case Table Name Has Key Word 'Order'.
//         6.2 Removed Constructor Instance fileSeparator. Obtained Constructor
//             Instance iconsDirectory From MyJSQlView_Utils Class."
//         6.3 Added fileSeparator to iconsDirectory.
//         6.4 Implemented listTable JPopupMenu. Added Constructor Instance
//             summaryTablePopupListener, Class Method createListTablePopupMenu().
//             Action Events Coupled in actionPerformed().
//         6.5 Class Method loadTable() Changed the Way BIT Types Handled to be
//             Displayed in listTable for MySQL Database.
//         6.6 Class Method loadTable() Added Data Type Handling for RAW, CLOB,
//             and LONG Types. Note Oracle Does NOT Allow LONG in Temporary Table.
//         6.7 Class Method actionPerformed() Creations of advancedSortSearchForm()
//             Quoted, identifierQuoteString, for sqlTable.
//         6.8 Changed Class Instance summaryTablePopupListener to transient.
//         6.9 Class Method firstLetterToUpperCase() Check for Empty String.
//         7.0 Class Method parseColumnNameField() Class Method columnName Changed
//             From String to StringBuffer.
//         7.1 Changed Package to Reflect Dandy Made Productions Code.
//         7.2 Implementation of Internationalization Through Constructor Argument
//             resourceBundle. Added Class Instance resource to Same and Method
//             createListTablePopupMenu(). Added Method setRowsLabel().
//         7.3 Class Method actionPerformed() Added Instance columnTypeHashMap to
//             AdvancedSortSearchForm Creation.
//         7.4 Fixed Oracle WHERE & GROUP BY Non-Support in Class Method getColumnNames().
//         7.5 Removed Class Method displayMyDateString(). Part of MyJSQLView_Utils.
//         7.6 Parameterized Class Instances fields, comboBoxFields, tableHeadings,
//             columnNamesHashMap, columnClassHashMap, columnTypeHashMap, columnSizeHashMap,
//             & preferredColumnSizeHashMap in Order to Bring Code Into Compliance
//             With Java 5.0 API. Removed Casts Associated With These Instances That
//             Were Not Needed and Insured Proper Parmeters Were Loaded/Retrieved.
//         7.7 Parameterized Instance headings in Constructor & Method loadtTable().
//             Also Instance tableFieldIterator in Class Method viewSelectedItem().
//         7.8 Implemented Support for SQLite Database. Constructor and Class Methods
//             getColumnNames() & loadTable() Effected.
//         7.9 Class Methods loadTable(), & viewSelectedItem(), Changed Default Entry
//             for Date/DateTime/TimeStamp Type to GeneralProperties.getDateViewFormat().
//             Also Class Method getColumnNames() Oracle sqlTableFieldsString Added
//             for TimestampLTZ the Return of Date Foramt to YYYY-MM-DD.
//         8.0 Class Method loadTable() Changes to Give the Ability to Properly Search
//             Given Input for Date/DateTime/Timestamp Fields.
//         8.1 Added Class Instance subProtocol. Constructor Obtained identifierQuoteString
//             From New Class ConnectionManger Along With subProtocol.
//         8.2 Removed resourceRowsOf in setRowsLabel().
//         8.3 Class Method loadTable() Changes to Properly Search Date Field for Oracle.
//         8.4 Correction in Method loadTable() to Properly Add Space When Adding ORDER
//             as needed. Class Method getColumnNames() ORDER Check When WHERE Present
//             for Oracle to Properly Insert ROWNUM=1.
//         8.5 Method actionPerformed() Addition of columnClassHashMap to Argument for
//             AdvancedSortSearchForm.
//         8.6 Replaced Class Instance subProtocol With dataSourceType. Class Methods
//             Effected getColumnNames() & loadTable() and Constructor.
//         8.7 Exclusion of viewButton for HSQL2 in Constructor. Implementation of HSQL2
//             Temporary Table Creation in loadTable Along With Only Processing Sizing
//             Information for MySQL Timestamps. Same Timestamp Processing for MySQL in
//             viewSelectedItem().
//         8.8 Minor Comment Changes.
//         8.9 Class Method loadTable() Set validQuery to True if Loading Properly Takes
//             Place, But False if Exception Generated.
//         9.0 Removed Class Instances advancedSort/SearchButton & Replaced With
//             advSortSearchApplyButton. Class Methods Effected actionPerformed() &
//             setTableRowSize().
//         9.1 Copyright Update.
//         9.2 Change in getColumnNames() & viewSelectedItem() to Throw SQLException
//             Through Finally to Close sqlStatement. Try & catch in Constructor
//             & actionPerformed() for Calls to These Methods.
//         9.3 Changed Class Instances fields, comboBoxFields, & tableHeadings from
//             Vector to ArrayList.
//         9.4 Class Method getColumnNames() Change in Collection of Column Names by
//             Using tableMetaData.getColumnLabel() Instead of getColumnName() So That
//             Aliases Can Properly be Collected.
//         9.5 Class Method loadTable() Change in Return Type for advancedSortSearchFrame.
//             getAdvancedSortSearchSQL().
//         9.6 Changes in Way MyJSQLView_ResourceBundle Handles the Collection
//             of Resource Strings. Change to resource.getResourceString(key,
//             default).
//         9.7 Closure for db_resultSet in getColumnNames() & viewSelectedItem()
//             Moved to finally.
//         9.8 Collection of All Image Resources Through resourceBundle.
//         9.9 Change Package Name to com.dandymadeproductions.myjsqlview.gui.panels.
//             Made Class, Constructor, & Getter/Setter Methods Public.
//        10.0 advSortSearchApplyButton Instance Related to Form advancedSort
//             SearchFrame.getApplyButton().
//        10.1 Change in loadTable() & viewSelectedItem() to Use DBTablePanel.
//             getGeneralDBProperties().
//        10.2 Class Method loadTable() Processing of Temporary Table, Bit, & Text,
//             Fields for MariaDB. Same DB Processing in viewSelectedItem() for
//             Timestamp & Bit Fields.
//        10.3 Parameterized JComboBox Class Instances sortComboBox & searchComboBox
//             to Conform With JRE 7. Class Method loadTable() Increment of j Only
//             After try/catch Clause for BIT Types of MySQL & MariaDB. Same Method
//             Conditional Check for YEAR in Same Databases Size of 4 Before Sub-Stringing.
//        10.4 Method getColumnNames() Excluded primaryKey From All Field Attributes for
//             the Databases Oracle, HSQL2, & SQLite, for Proper Selecting of a Key in
//             viewSelectedItem(). Method loadTable() & viewSelectedItem() Update for
//             Oracle 11 TIMESTAMP WITH (LOCAL) TIME ZONE, & NCHAR Column Types.
//        10.5 Class Method loadTable() & viewSelectedItem() Added TIME Processing to
//             Handle SQLite Inconsistencies in getString() Default.
//        10.6 Modifications in loadTable() To Properly Process SQLite TIMESTAMP
//             & DATE Types.
//
//-----------------------------------------------------------------
//                 danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview.gui.panels;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.RepaintManager;
import javax.swing.ScrollPaneConstants;
import javax.swing.TransferHandler;
import javax.swing.table.TableColumn;

import com.dandymadeproductions.myjsqlview.MyJSQLView;
import com.dandymadeproductions.myjsqlview.datasource.ConnectionManager;
import com.dandymadeproductions.myjsqlview.gui.MyJSQLView_MouseAdapter;
import com.dandymadeproductions.myjsqlview.gui.QueryFrame;
import com.dandymadeproductions.myjsqlview.gui.forms.AdvancedSortSearchForm;
import com.dandymadeproductions.myjsqlview.gui.forms.TableViewForm;
import com.dandymadeproductions.myjsqlview.utilities.MyJSQLView_ResourceBundle;
import com.dandymadeproductions.myjsqlview.utilities.MyJSQLView_TableModel;
import com.dandymadeproductions.myjsqlview.utilities.MyJSQLView_Utils;

/**
 *    The QueryTabPanel provides the view of resultant data from
 * a query. The panel allows the copying, sorting, and searching
 * of the data.
 * 
 * @author Dana M. Proctor
 * @version 10.6 05/09/2015
 */

public class QueryTabPanel extends JPanel implements ActionListener, KeyListener, Printable
{
   // Class Instances.
   private static final long serialVersionUID = -3722283242259255519L;

   private String queryNumber, query;
   private boolean showQuery;
   private transient Connection dbConnection;
   private boolean validQuery;

   private int tableRowStart = 0;
   private int tableRowLimit = 50;
   private static final int maxPreferredColumnSize = 350;

   private String sqlTable;

   private String dataSourceType;
   private String identifierQuoteString;
   private String primaryKey;
   private String sqlTableFieldsString;
   private ArrayList<String> fields, comboBoxFields;
   private ArrayList<String> tableHeadings;
   private MyJSQLView_ResourceBundle resourceBundle;
   
   private ImageIcon ascUpIcon, ascDownIcon, descUpIcon, descDownIcon;
   private ImageIcon searchIcon, removeIcon, advancedSortSearchIcon;
   private ImageIcon previousViewIcon, nextViewIcon;
   private ImageIcon previousTableRowsIcon, nextTableRowsIcon;

   private JButton searchButton, clearSearchTextFieldButton;
   private String ascDescString;
   private JRadioButton ascSortRadioButton, descSortRadioButton;
   private JComboBox<Object> sortComboBox, searchComboBox;
   private JTextField searchTextField;
   private transient MouseListener summaryTablePopupListener;

   private AdvancedSortSearchForm advancedSortSearchFrame;
   private JButton activateAdvancedSortSearchButton, advSortSearchApplyButton;
   private boolean advancedSortSearch;

   private JPanel centerPanel;
   private JScrollPane tableScrollPane;
   private CardLayout centerCardLayout;
   private JTable listTable;
   protected MyJSQLView_TableModel tableModel;
   private TableViewForm tableViewForm;

   protected Object[][] tableData;
   private HashMap<String, String> columnNamesHashMap;
   private HashMap<String, String> columnClassHashMap;
   private HashMap<String, String> columnTypeHashMap;
   private HashMap<String, Integer> columnSizeHashMap;
   private HashMap<String, Integer> preferredColumnSizeHashMap;

   private JLabel rowsLabel;
   private JButton previousButton, viewButton, nextButton;
   private JButton previousViewButton, closeViewButton, nextViewButton;

   //==============================================================
   // QueryTabPanel Constructor
   //==============================================================

   public QueryTabPanel(String queryNumber, String inputQuery, boolean showQuery,
                           int queryRowLimit, Connection query_dbConnection,
                           MyJSQLView_ResourceBundle resourceBundle)
   {
      this.queryNumber = queryNumber;
      query = formatQuery(inputQuery);
      this.showQuery = showQuery;
      tableRowLimit = queryRowLimit;
      dbConnection = query_dbConnection;
      this.resourceBundle = resourceBundle;
      
      // Constructor Instances.
      JPanel sortSearchPanel, sortPanel, sortOrderButtonPanel;
      JLabel sortByLabel;
      JPanel searchPanel;
      JLabel searchLabel;
      JLabel forLabel;
      
      String iconsDirectory, resource;

      // Setting up a icons directory instance, identifier quote
      // character, & proper data source name qualifier.
      
      iconsDirectory = MyJSQLView_Utils.getIconsDirectory() + MyJSQLView_Utils.getFileSeparator();
      dataSourceType = ConnectionManager.getDataSourceType();
      identifierQuoteString = ConnectionManager.getIdentifierQuoteString();

      // Setting up.
      sqlTable = "temptable" + queryNumber;
      fields = new ArrayList <String>();
      comboBoxFields = new ArrayList <String>();
      tableHeadings = new ArrayList <String>();
      primaryKey = "id_" + queryNumber;
      columnNamesHashMap = new HashMap <String, String>();
      columnClassHashMap = new HashMap <String, String>();
      columnTypeHashMap = new HashMap <String, String>();
      columnSizeHashMap = new HashMap <String, Integer>();
      preferredColumnSizeHashMap = new HashMap <String, Integer>();
      validQuery = false;
      ascDescString = "ASC";

      ascUpIcon = resourceBundle.getResourceImage(iconsDirectory + "ascUpIcon.png");
      ascDownIcon = resourceBundle.getResourceImage(iconsDirectory + "ascDownIcon.png");
      descUpIcon = resourceBundle.getResourceImage(iconsDirectory + "descUpIcon.png");
      descDownIcon = resourceBundle.getResourceImage(iconsDirectory + "descDownIcon.png");
      searchIcon = resourceBundle.getResourceImage(iconsDirectory + "searchIcon.png");
      removeIcon = resourceBundle.getResourceImage(iconsDirectory + "removeIcon.png");
      advancedSortSearchIcon = resourceBundle.getResourceImage(iconsDirectory + "advancedSortSearchIcon.gif");
      previousViewIcon = resourceBundle.getResourceImage(iconsDirectory + "previousViewIcon.png");
      nextViewIcon = resourceBundle.getResourceImage(iconsDirectory + "nextViewIcon.png");
      previousTableRowsIcon = resourceBundle.getResourceImage(iconsDirectory + "previousTableRowsIcon.png");
      nextTableRowsIcon = resourceBundle.getResourceImage(iconsDirectory + "nextTableRowsIcon.png");

      // General Panel Configurations
      setLayout(new BorderLayout());
      setBorder(BorderFactory.createRaisedBevelBorder());

      // ==================================================
      // Setting up the Sort/Search Interface.
      // ==================================================

      GridBagLayout gridbag = new GridBagLayout();
      GridBagConstraints constraints = new GridBagConstraints();

      sortSearchPanel = new JPanel(gridbag);
      sortSearchPanel.setBorder(BorderFactory.createEtchedBorder());

      // Sort Interface

      sortPanel = new JPanel();
      sortPanel.setBorder(BorderFactory.createRaisedBevelBorder());

      resource = resourceBundle.getResourceString("QueryTabPanel.label.SortBy", "Sort By");
      sortByLabel = new JLabel(resource + " : ", JLabel.LEFT);
      sortPanel.add(sortByLabel);

      // Connecting to the database table for obtaining
      // the column names. Sets up the base vectors, hashmaps.
      try
      {
         getColumnNames(dbConnection, query);
      }
      catch (SQLException sqle)
      {
         QueryFrame.setQueryResultTextArea("SQLException: " + sqle.getMessage());
      }

      sortComboBox = new JComboBox<Object>(comboBoxFields.toArray());
      sortComboBox.addActionListener(this);
      sortPanel.add(sortComboBox);

      sortOrderButtonPanel = new JPanel();
      BoxLayout sortOrderLayout = new BoxLayout(sortOrderButtonPanel, BoxLayout.Y_AXIS);
      sortOrderButtonPanel.setLayout(sortOrderLayout);
      sortOrderButtonPanel.setBorder(BorderFactory.createEmptyBorder());
      ButtonGroup sortOrderButtonGroup = new ButtonGroup();

      ascSortRadioButton = new JRadioButton(ascUpIcon, true);
      ascSortRadioButton.setSelectedIcon(ascDownIcon);
      ascSortRadioButton.setMargin(new Insets(0, 0, 0, 0));
      ascSortRadioButton.setFocusPainted(false);
      ascSortRadioButton.addActionListener(this);
      sortOrderButtonGroup.add(ascSortRadioButton);

      descSortRadioButton = new JRadioButton(descUpIcon, false);
      descSortRadioButton.setSelectedIcon(descDownIcon);
      descSortRadioButton.setMargin(new Insets(0, 0, 0, 0));
      descSortRadioButton.setFocusPainted(false);
      descSortRadioButton.addActionListener(this);
      sortOrderButtonGroup.add(descSortRadioButton);

      sortOrderButtonPanel.add(ascSortRadioButton);
      sortOrderButtonPanel.add(descSortRadioButton);
      sortPanel.add(sortOrderButtonPanel);

      buildConstraints(constraints, 0, 0, 1, 1, 100, 100);
      constraints.fill = GridBagConstraints.BOTH;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(sortPanel, constraints);
      sortSearchPanel.add(sortPanel);

      // Search Interface

      searchPanel = new JPanel();
      searchPanel.setBorder(BorderFactory.createRaisedBevelBorder());

      resource = resourceBundle.getResourceString("QueryTabPanel.label.Search", "Search");
      searchLabel = new JLabel(resource + " : ");
      searchPanel.add(searchLabel);

      searchComboBox = new JComboBox<Object>(comboBoxFields.toArray());
      searchComboBox.insertItemAt("", 0);
      searchComboBox.setSelectedIndex(0);
      searchComboBox.addActionListener(this);
      searchPanel.add(searchComboBox);

      resource = resourceBundle.getResourceString("QueryTabPanel.label.For", "For");
      forLabel = new JLabel(resource + " : ", JLabel.LEFT);
      searchPanel.add(forLabel);

      searchTextField = new JTextField(12);
      searchTextField.setBorder(BorderFactory.createLoweredBevelBorder());
      searchTextField.addKeyListener(this);
      searchPanel.add(searchTextField);

      searchButton = new JButton(searchIcon);
      searchButton.setMargin(new Insets(0, 0, 0, 0));
      searchButton.setFocusPainted(false);
      searchButton.addActionListener(this);
      searchPanel.add(searchButton);

      clearSearchTextFieldButton = new JButton(removeIcon);
      clearSearchTextFieldButton.setMargin(new Insets(2, 2, 2, 2));
      clearSearchTextFieldButton.setFocusPainted(false);
      clearSearchTextFieldButton.addActionListener(this);
      searchPanel.add(clearSearchTextFieldButton);

      buildConstraints(constraints, 1, 0, 2, 1, 100, 100);
      constraints.fill = GridBagConstraints.BOTH;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(searchPanel, constraints);
      sortSearchPanel.add(searchPanel);

      add(sortSearchPanel, BorderLayout.NORTH);

      // ==================================================
      // Setting up the Summary Table View.
      // ==================================================

      centerPanel = new JPanel(centerCardLayout = new CardLayout());

      // Table, Setting up
      if (validQuery)
      {
         if (loadTable(dbConnection, true))
         {
            tableModel = new MyJSQLView_TableModel(tableHeadings, tableData);

            listTable = new JTable(tableModel);
            listTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            listTable.getActionMap().put(TransferHandler.getCopyAction().getValue(Action.NAME),
                                         TransferHandler.getCopyAction());
            createListTablePopupMenu();
            listTable.addMouseListener(summaryTablePopupListener);

            // Sizing columns
            Iterator<String> headings = tableHeadings.iterator();
            TableColumn column = null;
            int i = 0;

            while (headings.hasNext())
            {
               column = listTable.getColumnModel().getColumn(i++);
               column.setPreferredWidth((preferredColumnSizeHashMap.get(headings.next())).intValue());
            }

            // Create a scrollpane for the summary table and
            // add a advanced sort/search button to corner.
            tableScrollPane = new JScrollPane(listTable);
            activateAdvancedSortSearchButton = new JButton(advancedSortSearchIcon);
            activateAdvancedSortSearchButton.addActionListener(this);
            tableScrollPane.setCorner(ScrollPaneConstants.UPPER_RIGHT_CORNER,
                                      activateAdvancedSortSearchButton);
            tableScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            centerPanel.add(sqlTable, tableScrollPane);

            // Table View Form Instances

            previousViewButton = new JButton(previousViewIcon);
            previousViewButton.setMargin(new Insets(0, 0, 0, 0));
            previousViewButton.addActionListener(this);

            resource = resourceBundle.getResourceString("QueryTabPanel.button.Close", "Close");
            closeViewButton = new JButton(resource);
            closeViewButton.addActionListener(this);

            nextViewButton = new JButton(nextViewIcon);
            nextViewButton.setMargin(new Insets(0, 0, 0, 0));
            nextViewButton.addActionListener(this);
         }
      }

      add(centerPanel, BorderLayout.CENTER);

      // ==================================================
      // Setting up the Panels show row indcator,
      // and action button View.
      // ==================================================

      JPanel actionPanel = new JPanel(gridbag);
      actionPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(),
                                                               BorderFactory.createEmptyBorder(0, 0, 0, 0)));

      // Temporary Table Identifier Panel
      JPanel tableIdentifierPanel = new JPanel();
      tableIdentifierPanel.setBorder(BorderFactory.createLoweredBevelBorder());

      JLabel tableLabel = new JLabel(sqlTable);
      tableLabel.setHorizontalAlignment(JLabel.LEFT);
      tableIdentifierPanel.add(tableLabel);

      buildConstraints(constraints, 0, 0, 1, 1, 10, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.WEST;
      gridbag.setConstraints(tableIdentifierPanel, constraints);
      actionPanel.add(tableIdentifierPanel);

      // Panel to hold the action buttons used to
      // manupulate table entries.

      JPanel actionButtonPanel = new JPanel();

      // View Button
      resource = resourceBundle.getResourceString("QueryTabPanel.button.View", "View");
      viewButton = new JButton(resource);
      viewButton.setMnemonic(KeyEvent.VK_V);
      viewButton.addActionListener(this);
      actionButtonPanel.add(viewButton);
      
      if (dataSourceType.equals(ConnectionManager.ORACLE)
            || dataSourceType.equals(ConnectionManager.SQLITE)
            || dataSourceType.equals(ConnectionManager.HSQL2))
         viewButton.setVisible(false);

      buildConstraints(constraints, 1, 0, 1, 1, 80, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(actionButtonPanel, constraints);
      actionPanel.add(actionButtonPanel);

      // Panel for table control and row indicator.

      JPanel tableControlIndicatorPanel = new JPanel();

      JPanel tableRowsIndicatorPanel = new JPanel();
      tableRowsIndicatorPanel.setBorder(BorderFactory.createLoweredBevelBorder());

      // Row Indicator
      rowsLabel = new JLabel();
      setRowsLabel((tableRowStart + 1), (tableRowStart + tableRowLimit));
      tableRowsIndicatorPanel.add(rowsLabel);
      tableControlIndicatorPanel.add(tableRowsIndicatorPanel);

      JPanel rowControlPanel = new JPanel();
      rowControlPanel.setLayout(gridbag);

      // Previous Button
      previousButton = new JButton(previousTableRowsIcon);
      previousButton.setHorizontalAlignment(JButton.CENTER);
      previousButton.setMargin(new Insets(0, 0, 0, 0));
      previousButton.setMnemonic(KeyEvent.VK_Z);
      previousButton.addActionListener(this);

      buildConstraints(constraints, 0, 0, 1, 1, 100, 50);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(previousButton, constraints);
      rowControlPanel.add(previousButton);

      // Next Button
      nextButton = new JButton(nextTableRowsIcon);
      nextButton.setHorizontalAlignment(JButton.CENTER);
      nextButton.setMargin(new Insets(0, 0, 0, 0));
      nextButton.setMnemonic(KeyEvent.VK_X);
      nextButton.addActionListener(this);

      buildConstraints(constraints, 0, 1, 1, 1, 100, 50);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(nextButton, constraints);
      rowControlPanel.add(nextButton);

      tableControlIndicatorPanel.add(rowControlPanel);

      buildConstraints(constraints, 2, 0, 1, 1, 2, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.EAST;
      gridbag.setConstraints(nextButton, constraints);
      actionPanel.add(tableControlIndicatorPanel);

      add(actionPanel, BorderLayout.SOUTH);
      addMouseListener(MyJSQLView.getPopupMenuListener());
   }

   //==============================================================
   // ActionEvent Listener method for detecting the inputs from
   // the panel and directing to the appropriate routine.
   //=============================================================

   public void actionPerformed(ActionEvent evt)
   {
      Object panelSource = evt.getSource();
      Object id;
      int primaryKeyColumn = 0;

      if ((panelSource instanceof JButton || panelSource instanceof JMenuItem) && validQuery)
      {
         // listTable Popup Menu Actions
         if (panelSource instanceof JMenuItem)
         {
            String actionCommand = ((JMenuItem) panelSource).getActionCommand();
            // System.out.println(actionCommand);

            if (actionCommand.equals("View"))
               viewButton.doClick();
            else if (actionCommand.equals("Select All"))
               listTable.selectAll();
            else if (actionCommand.equals("DeSelect All"))
               listTable.clearSelection();
            // Copy
            else if (actionCommand.equals((String)TransferHandler.getCopyAction().getValue(Action.NAME)))
            {
               Action a = listTable.getActionMap().get(actionCommand);
               if (a != null)
                  a.actionPerformed(new ActionEvent(listTable, ActionEvent.ACTION_PERFORMED, null));
            }
         }
         
         
         // Search Action.
         if (panelSource == searchButton || panelSource == clearSearchTextFieldButton)
         {
            advancedSortSearch = false;
            tableRowStart = 0;
            if (panelSource == clearSearchTextFieldButton)
               searchTextField.setText("");
            
            loadTable(dbConnection, true);
            tableModel.setValues(tableData);
            tableScrollPane.getVerticalScrollBar().setValue(0);
            centerCardLayout.show(centerPanel, sqlTable);
            setRowsLabel((tableRowStart + 1), (tableRowStart + tableRowLimit));
         }

         // Advanced Sort/Search Apply Action.
         else if (panelSource == advSortSearchApplyButton)
         {
            advancedSortSearch = true;
            searchComboBox.removeActionListener(this);
            searchComboBox.setSelectedIndex(0);
            searchTextField.setText("");
            searchComboBox.addActionListener(this);
            tableRowStart = 0;
            loadTable(dbConnection, true);
            tableModel.setValues(tableData);
            tableScrollPane.getVerticalScrollBar().setValue(0);
            centerCardLayout.show(centerPanel, sqlTable);
            setRowsLabel((tableRowStart + 1), (tableRowStart + tableRowLimit));
         }

         // Advanced Sort/Search Action.
         else if (panelSource == activateAdvancedSortSearchButton)
         {
            if (advancedSortSearchFrame == null)
            {
               advancedSortSearchFrame = new AdvancedSortSearchForm(identifierQuoteString
                                                                    + sqlTable + identifierQuoteString,
                                                                    resourceBundle, columnNamesHashMap,
                                                                    columnClassHashMap, columnTypeHashMap,
                                                                    comboBoxFields);

               advSortSearchApplyButton = advancedSortSearchFrame.getApplyButton();
               advSortSearchApplyButton.addActionListener(this);

               advancedSortSearchFrame.pack();
               advancedSortSearchFrame.center();
               advancedSortSearchFrame.setVisible(true);
            }
            else
            {
               if (advancedSortSearchFrame.isShowing())
                  advancedSortSearchFrame.setVisible(false);
               else
                  advancedSortSearchFrame.setVisible(true);
            }
         }

         // Previous Table Row Action.
         else if (panelSource == previousButton)
         {
            tableRowStart -= tableRowLimit;

            if (tableRowStart <= 0)
               tableRowStart = 0;

            loadTable(dbConnection, false);
            tableModel.setValues(tableData);
            setRowsLabel((tableRowStart + 1), (tableRowStart + tableRowLimit));
         }

         // View Action.
         else if (panelSource == viewButton)
         {
            // Make sure an entry is selected.
            if (listTable.getSelectedRow() != -1)
            {
               // Just insures the selected row has data.
               id = listTable.getValueAt(listTable.getSelectedRow(), primaryKeyColumn);
               if (id != null)
               {
                  // Setting up and creating the TableViewForm to
                  // be used in displaying the selected entry.

                  if (tableViewForm != null)
                     centerPanel.remove(tableViewForm);

                  tableViewForm = new TableViewForm(fields, columnClassHashMap, columnTypeHashMap,
                                                    columnSizeHashMap, previousViewButton,
                                                    closeViewButton, nextViewButton);

                  centerPanel.add(sqlTable + " Form", tableViewForm);
                  try
                  {
                     viewSelectedItem(dbConnection);
                  }
                  catch (SQLException sqle)
                  {
                     QueryFrame.setQueryResultTextArea("SQLException: " + sqle.getMessage());
                  }
                  centerCardLayout.show(centerPanel, sqlTable + " Form");
                  tableViewForm.setFocus();
               }
            }
         }

         // Next Action.
         else if (panelSource == nextButton)
         {
            tableRowStart += tableRowLimit;
            loadTable(dbConnection, false);
            tableModel.setValues(tableData);
            setRowsLabel((tableRowStart + 1), (tableRowStart + tableRowLimit));
         }

         // View Form Close Button Action
         else if (panelSource == closeViewButton)
         {
            toggleCenterPanel();
            tableViewForm.clearBlobBytesHashMap();
         }

         // Previous View Button & Next View Button
         else if (panelSource == previousViewButton | panelSource == nextViewButton)
         {
            if (listTable.getSelectedRow() != -1)
            {
               int oldRow, rowPointer;

               // Save row pointer
               oldRow = Integer.parseInt(listTable.getSelectedRow() + "");

               if (panelSource == previousViewButton)
                  rowPointer = Integer.parseInt(listTable.getSelectedRow() + "") - 1;
               else
                  rowPointer = Integer.parseInt(listTable.getSelectedRow() + "") + 1;

               if (rowPointer < 0 | rowPointer > (tableRowLimit - 1))
                  rowPointer = oldRow;

               // Change the row pointer then collect table data.
               // Otherwise maintain old row pointer.

               listTable.changeSelection(rowPointer, 0, false, false);
               id = listTable.getValueAt(listTable.getSelectedRow(), 0);
               if (id != null)
               {
                  try
                  {
                     viewSelectedItem(dbConnection);
                  }
                  catch (SQLException sqle)
                  {
                     QueryFrame.setQueryResultTextArea("SQLException: " + sqle.getMessage());
                  }
                  centerCardLayout.show(centerPanel, sqlTable + " Form");
                  tableViewForm.setFocus();
               }
               else
                  listTable.changeSelection(oldRow, 0, false, false);
            }
         }
      }
      
      // JRadio Button Actions
      else if (panelSource instanceof JRadioButton && validQuery)
      {
         if (panelSource == ascSortRadioButton || panelSource == descSortRadioButton)
         {
            if (ascSortRadioButton.isSelected())
               ascDescString = "ASC";
            else
               ascDescString = "DESC";

            advancedSortSearch = false;
            loadTable(dbConnection, true);
            tableModel.setValues(tableData);
            tableScrollPane.getVerticalScrollBar().setValue(0);
            centerCardLayout.show(centerPanel, sqlTable);
         }
      }
      
      // Combobox Actions
      else if (panelSource instanceof JComboBox && validQuery)
      {
         // Sort ComboBox Action
         if (panelSource == sortComboBox)
         {
            // Just insure have column field selected.
            if (sortComboBox.getSelectedIndex() == 0)
            {
               sortComboBox.removeActionListener(this);
               sortComboBox.setSelectedIndex(1);
               sortComboBox.addActionListener(this);
            }
            advancedSortSearch = false;
            loadTable(dbConnection, true);
            tableModel.setValues(tableData);
            tableScrollPane.getVerticalScrollBar().setValue(0);
            centerCardLayout.show(centerPanel, sqlTable);
         }

         // Search ComboBox Action
         if (panelSource == searchComboBox)
         {
            searchTextField.grabFocus();
            searchTextField.setCaretPosition(searchTextField.getText().length());
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
   // Enter, to be used with the search actions.
   //==============================================================

   public void keyTyped(KeyEvent evt)
   {
      // Derived from the searchTextField.
      char keyChar = evt.getKeyChar();

      // Fire the search button as required.
      if (keyChar == KeyEvent.VK_ENTER)
         searchButton.doClick();
   }

   //==============================================================
   // Class method to properly format the input query string
   // key words of importance for the class to upper case.
   //==============================================================

   private String formatQuery(String inputQuery)
   {
      String tempQuery;

      // Remove ending quote and move to uppercase.
      tempQuery = inputQuery.endsWith(";") ? inputQuery.substring(0, inputQuery.lastIndexOf(";"))
                                           : inputQuery;

      // Insure SELECT & FROM keywords in upper case for testing
      // later in loadTable().
      tempQuery = tempQuery.replaceAll("(?i)select ", "SELECT ");
      tempQuery = tempQuery.replaceAll("(?i)from ", "FROM ");

      return tempQuery;
   }

   //==============================================================
   // Class method to obtain the column names from the table. The
   // names are modified for display and placed into a map for
   // later use. Additional information about the column, size &
   // type, are also stored away for future use.
   //==============================================================

   private void getColumnNames(Connection dbConnection, String query) throws SQLException
   {
      // Method Instances
      String sqlStatementString;
      Statement sqlStatement;
      ResultSet db_resultSet;
      ResultSetMetaData tableMetaData;

      String colNameString, comboBoxNameString;
      String columnClass, columnType;
      Integer columnSize;

      // Connecting to the data base, to obtain
      // meta data, and column names.
      sqlStatement = null;
      db_resultSet = null;
      
      try
      {
         sqlStatement = dbConnection.createStatement();

         // Setting Up the Column Names, Form Fields, ComboBox
         // Text, Hashmaps, Special Fields, & Primary Key(s).

         // *************** HSQL NEEDS FIXED ***********************
         // This is a lame work around for HSQLDB which does not
         // properly parse a standard short query with trailing
         // LIMIT. I hate crap like this.
         // ********************************************************
         if (dataSourceType.equals(ConnectionManager.HSQL))
         {
            if (query.toUpperCase().indexOf("WHERE") != -1 ||
                query.toUpperCase().indexOf("GROUP") != -1 ||
                query.toUpperCase().indexOf("HAVING") != -1 ||
                query.toUpperCase().indexOf("ORDER") != -1)
               sqlStatementString = query + " LIMIT 1";
            else
               sqlStatementString = query + " AS lame LIMIT 1";
         }
         else if (dataSourceType.equals(ConnectionManager.ORACLE))
         {
            if (query.toUpperCase().indexOf("WHERE") != -1)
            {
               if (query.toUpperCase().indexOf("GROUP BY") != -1)
               {
                  sqlStatementString = query.substring(0, query.indexOf("GROUP BY") - 1)
                                       + " AND ROWNUM=1 " + query.substring(query.indexOf("GROUP BY"));
               }
               else
               {
                  if (query.toUpperCase().indexOf("ORDER BY") != -1)
                     sqlStatementString = query.substring(0, query.indexOf("ORDER BY") - 1)
                                          + " AND ROWNUM=1 " + query.substring(query.indexOf("ORDER BY"));
                  else
                     sqlStatementString = query + " AND ROWNUM=1";
               }
            }
            else
            {
               sqlStatementString = query + " WHERE ROWNUM=1";
            }
         }
         else
            sqlStatementString = query + " LIMIT 1";
         // System.out.println(sqlStatementString);

         // ********************************************************

         db_resultSet = sqlStatement.executeQuery(sqlStatementString);
         tableMetaData = db_resultSet.getMetaData();

         // Key Field Characteristics.

         if (!dataSourceType.equals(ConnectionManager.ORACLE)
              && !dataSourceType.equals(ConnectionManager.SQLITE)
              && !dataSourceType.equals(ConnectionManager.HSQL2))
         {
            fields.add(primaryKey);
            columnNamesHashMap.put(primaryKey, primaryKey);
            tableHeadings.add(primaryKey);
            columnClassHashMap.put(primaryKey, "java.lang.Integer");
            columnTypeHashMap.put(primaryKey, "INTEGER");
            columnSizeHashMap.put(primaryKey, Integer.valueOf(10));
            preferredColumnSizeHashMap.put(primaryKey, Integer.valueOf(primaryKey.length() * 9));
         }
         
         // Column Names, Form Fields, ComboBox Text and HashMaps

         sqlTableFieldsString = "";

         for (int i = 1; i < tableMetaData.getColumnCount() + 1; i++)
         {
            colNameString = tableMetaData.getColumnLabel(i);
            comboBoxNameString = parseColumnNameField(colNameString);

            // Additional Information about each column.
            columnClass = tableMetaData.getColumnClassName(i);
            columnType = tableMetaData.getColumnTypeName(i);
            columnSize = Integer.valueOf(tableMetaData.getColumnDisplaySize(i));

            // System.out.println(i + " " + colNameString + " " +
            //                    comboBoxNameString + " " +
            //                    columnClass + " " + columnType + " " +
            //                    columnSize);

            // This going to be a problem so skip this column.
            // NOT TESTED. This is still problably not going to
            // help. Bound to crash in loadTable().

            if (columnClass == null && columnType == null)
               continue;

            // Handle some Oracle data types that have a null
            // class type and possibly others.

            if (columnClass == null)
            {
               if (columnType.equals("BINARY_FLOAT")
                   && dataSourceType.equals(ConnectionManager.ORACLE))
               {
                  columnClass = "java.lang.Float";
                  columnType = "FLOAT";
               }
               else if (columnType.equals("BINARY_DOUBLE")
                        && dataSourceType.equals(ConnectionManager.ORACLE))
               {
                  columnClass = "java.lang.Double";
                  columnType = "DOUBLE";
               }
               else
                  columnClass = columnType;
            }

            fields.add(comboBoxNameString);
            columnNamesHashMap.put(comboBoxNameString, colNameString);
            comboBoxFields.add(comboBoxNameString);
            tableHeadings.add(comboBoxNameString);
            columnClassHashMap.put(comboBoxNameString, columnClass);
            columnTypeHashMap.put(comboBoxNameString, columnType.toUpperCase());
            columnSizeHashMap.put(comboBoxNameString, columnSize);
            preferredColumnSizeHashMap.put(comboBoxNameString,
                                           Integer.valueOf(comboBoxNameString.length() * 9));

            // Create a string with all the table field names for Oracle.
            // Mofify TIMESTAMPLTZ field name because were are not going
            // to do alter the SESSION TIMEZONE.

            if (columnType.equals("TIMESTAMPLTZ"))
            {
               sqlTableFieldsString += "TO_CHAR(" + identifierQuoteString + colNameString
                                       + identifierQuoteString + ", 'YYYY-MM-DD HH24:MM:SS TZR') AS "
                                       + identifierQuoteString + colNameString + identifierQuoteString + ", ";
            }
            else
               sqlTableFieldsString += identifierQuoteString + colNameString + identifierQuoteString + ", ";
         }
         sqlTableFieldsString = sqlTableFieldsString.substring(0, sqlTableFieldsString.length() - 2);

         // Looks good so validate.
         validQuery = true;
      }
      catch (SQLException e)
      {
         String errorString = "SQLException: " + e.getMessage() + " " + "SQLState: " 
                              + e.getSQLState() + " " + "VendorError: " + e.getErrorCode();
         QueryFrame.setQueryResultTextArea(errorString);
         return;
      }
      finally
      {
         try
         {
            if (db_resultSet != null)
               db_resultSet.close();
         }
         catch (SQLException sqle)
         {
            QueryFrame.setQueryResultTextArea("SQLException: " + sqle.getMessage());
         }
         finally
         {
            if (sqlStatement != null)
               sqlStatement.close();
         }
      }
   }

   //==============================================================
   // Class method to loads the current table's data. The routine
   // will apply the sort and search parameters.
   //==============================================================

   private boolean loadTable(Connection dbConnection, boolean newData)
   {
      // Method Instances
      String sqlStatementString;
      Statement sqlStatement;
      ResultSet rs;

      String tempTable;
      String columnSearchString, searchTextString;
      String columnName, columnClass, columnType;
      String tempQueryString;
      int columnSize, preferredColumnSize;

      // Obtain the temp table name and search parameters
      // column names as needed.
      columnSearchString = columnNamesHashMap.get(searchComboBox.getSelectedItem());
      searchTextString = searchTextField.getText();

      if (columnSearchString == null)
      {
         if (dataSourceType.equals(ConnectionManager.HSQL))
            columnSearchString = "TRUE";
         else
            columnSearchString = "'1'";
      }
      else
      {
         columnSearchString = identifierQuoteString + columnSearchString 
                              + identifierQuoteString;
      }

      if (searchTextString.equals(""))
         searchTextString = "LIKE '%'";
      else
      {
         if (!columnSearchString.equals("TRUE") && !columnSearchString.equals("'1'"))
         {
            // Try and process Date/Datetime/TimeStamp Fields
            columnType = columnTypeHashMap.get(searchComboBox.getSelectedItem());
            
            if (columnType.equals("DATE"))
            {
               if (dataSourceType.equals(ConnectionManager.ORACLE))
               {
                  searchTextString = MyJSQLView_Utils.processDateFormatSearch(searchTextString);
                  searchTextString = "LIKE TO_DATE('" + searchTextString + "', 'YYYY-MM-dd')";
               }
               else
                  searchTextString = "LIKE '%"
                                     + MyJSQLView_Utils.processDateFormatSearch(searchTextString) + "%'";
            }
            else if (columnType.equals("DATETIME") || columnType.indexOf("TIMESTAMP") != -1)
            {
               if (searchTextString.indexOf(" ") != -1)
                  searchTextString = MyJSQLView_Utils.processDateFormatSearch(
                     searchTextString.substring(0, searchTextString.indexOf(" ")))
                     + searchTextString.substring(searchTextString.indexOf(" "));
               else if (searchTextString.indexOf("-") != -1 || searchTextString.indexOf("/") != -1)
                  searchTextString = MyJSQLView_Utils.processDateFormatSearch(searchTextString);
               
               searchTextString = "LIKE '%" + searchTextString + "%'";
            }
            else
               searchTextString = "LIKE '%" + searchTextString + "%'";
         }
         else
            searchTextString = "LIKE '%" + searchTextString + "%'";
      }
      
      // Connect to database to create a temporary table to be used
      // for the initial/new items set and then sorting that set.
      try
      {
         sqlStatement = dbConnection.createStatement();

         // Lets get the right temp table name first.
         if (newData)
            tempTable = "temptable" + queryNumber;
         else
            tempTable = "temptable" + QueryFrame.getSelectedTabTitle();

         // Clear the output text area if query is to be shown.
         if (showQuery)
            QueryFrame.clearQueryResultTextArea();

         // ============================================
         // initial/new data set, all but next/previous

         if (newData)
         {
            // Drop any previous temporary table that has been
            // created.

            if (dataSourceType.equals(ConnectionManager.ORACLE))
            {
               sqlStatementString = "SELECT OBJECT_NAME FROM USER_OBJECTS " + "WHERE OBJECT_NAME='"
                                    + tempTable + "'" + " AND OBJECT_TYPE='TABLE'";
               // System.out.println(sqlStatementString);
               rs = sqlStatement.executeQuery(sqlStatementString);
               if (rs.next())
               {
                  sqlStatementString = "DROP TABLE " + identifierQuoteString + tempTable
                                       + identifierQuoteString + " PURGE";
                  // System.out.println(sqlStatementString);
                  sqlStatement.executeUpdate(sqlStatementString);
               }
            }
            else
            {
               sqlStatementString = "DROP TABLE IF EXISTS " + identifierQuoteString + tempTable
                                    + identifierQuoteString;
               // System.out.println(sqlStatementString);
               sqlStatement.executeUpdate(sqlStatementString);
            }

            // MySQL/MariaDB SQL Statement.
            if (dataSourceType.equals(ConnectionManager.MYSQL)
                || dataSourceType.equals(ConnectionManager.MARIADB))
            {
               sqlStatementString = "CREATE TEMPORARY TABLE " + identifierQuoteString + tempTable
                                    + identifierQuoteString + " (" + primaryKey
                                    + " INT NOT NULL auto_increment PRIMARY KEY) " + query;
            }

            // PostgreSQL SQL Statement.
            else if (dataSourceType.equals(ConnectionManager.POSTGRESQL))
            {
               // Drop any existing primary key sequence and create
               // a new one.
               sqlStatementString = "DROP SEQUENCE IF EXISTS " + primaryKey;
               sqlStatement.executeUpdate(sqlStatementString);

               sqlStatementString = "CREATE TEMPORARY SEQUENCE " + primaryKey + " " + "START 1";
               sqlStatement.executeUpdate(sqlStatementString);

               // Create SQL statement.
               sqlStatementString = "CREATE TEMPORARY TABLE " + identifierQuoteString + tempTable
                                    + identifierQuoteString + " AS ";
               if (query.toUpperCase().indexOf("SELECT") != -1)
                  sqlStatementString += query.replaceFirst("SELECT", "SELECT nextval('" + primaryKey
                                                            + "') AS " + primaryKey + ",");
            }

            // HSQL SQL Statement.
            else if (dataSourceType.indexOf(ConnectionManager.HSQL) != -1)
            {
               // Create SQL Statement
               
               // HSQL
               if (dataSourceType.equals(ConnectionManager.HSQL))
               {
                  // Drop any existing primary key sequence and create
                  // a new one.
                  sqlStatementString = "DROP SEQUENCE " + primaryKey + " IF EXISTS ";
                  sqlStatement.executeUpdate(sqlStatementString);

                  sqlStatementString = "CREATE SEQUENCE " + primaryKey + " " + "AS BIGINT";
                  sqlStatement.executeUpdate(sqlStatementString);
                  
                  if (query.toUpperCase().indexOf("SELECT") != -1)
                     tempQueryString = query.replaceFirst("SELECT", "SELECT NEXT VALUE FOR " + primaryKey
                                                           + " AS " + primaryKey + ",");
                  else
                     tempQueryString = query;

                  if (tempQueryString.toUpperCase().indexOf("SELECT") != -1)
                     sqlStatementString = tempQueryString
                           .replaceFirst("FROM", "INTO MEMORY " + identifierQuoteString + tempTable
                                                 + identifierQuoteString + " FROM");
                  else
                     sqlStatementString = tempQueryString;
               }
               // HSQL2
               else
               {
                  // Example of what needs to be, but just to screwed up to
                  // extract the table name for * to be worth it.
                  // CREATE TEMPORARY TABLE "temptable0" AS (SELECT NEXT VALUE FOR id_0,
                  // SELECT KEY_TABLE4.* FROM KEY_TABLE4) WITH DATA 
                  
                  sqlStatementString = "CREATE TEMPORARY TABLE " + identifierQuoteString + tempTable
                                       + identifierQuoteString + " AS (" + query;
               }
            }

            // Oracle SQL Statement.
            else if (dataSourceType.equals(ConnectionManager.ORACLE))
            {
               // Drop any existing primary key sequence and create
               // a new one.
               /*
                * sqlStatementString = "SELECT OBJECT_NAME FROM USER_OBJECTS " +
                * "WHERE OBJECT_NAME='" + primaryKey + "'" + " AND
                * OBJECT_TYPE='SEQUENCE'"; rs =
                * sqlStatement.executeQuery(sqlStatementString); if (rs.next()) {
                * sqlStatementString = "DROP SEQUENCE " + identifierQuoteString +
                * primaryKey + identifierQuoteString;
                * sqlStatement.executeUpdate(sqlStatementString); }
                * sqlStatementString = "CREATE SEQUENCE " +
                * identifierQuoteString + primaryKey + identifierQuoteString + "
                * START WITH 1 INCREMENT BY 1 NOMAXVALUE";
                * sqlStatement.executeUpdate(sqlStatementString);
                */

               // Create SQL Statement
               sqlStatementString = "CREATE TABLE " + identifierQuoteString + tempTable
                                    + identifierQuoteString + " AS " + query;
            }
            
            // SQLite SQL Statement.
            else if (dataSourceType.equals(ConnectionManager.SQLITE))
            {
               // Create SQL Statement
               sqlStatementString = "CREATE TABLE " + identifierQuoteString + tempTable
                                    + identifierQuoteString + " AS " + query;
            }

            // Bite the dust.
            else
               return false;

            // Add Conditionals as needed.
            if (sqlStatementString.toLowerCase().indexOf("from") != -1)
            {
               // Adding WHERE as needed.
               if (sqlStatementString.toLowerCase().indexOf("where") == -1)
               {
                  // ORDER present so insert WHERE after.
                  if (sqlStatementString.toLowerCase().indexOf(" order") != -1)
                  {
                     int orderIndex = sqlStatementString.toLowerCase().indexOf(" order");

                     sqlStatementString = sqlStatementString.substring(0, orderIndex) 
                                          + " WHERE " + columnSearchString + " " 
                                          + searchTextString + " "
                                          + sqlStatementString.substring(orderIndex);
                  }
                  // ORDER not present so add on WHERE
                  else
                  {
                     sqlStatementString += " WHERE " + columnSearchString + " " 
                                           + searchTextString + " ";
                  }
               }

               // Adding ORDER as needed.
               if (sqlStatementString.toLowerCase().indexOf(" order") == -1)
               {
                  sqlStatementString += " ORDER BY " + identifierQuoteString
                                        + columnNamesHashMap.get(sortComboBox.getSelectedItem())
                                        + identifierQuoteString;
               }
            }
            
            if (dataSourceType.equals(ConnectionManager.HSQL2))
               sqlStatementString = sqlStatementString + ") WITH DATA ON COMMIT PRESERVE ROWS";

            if (showQuery)
               QueryFrame.setQueryResultTextArea(sqlStatementString);
            // System.out.println(sqlStatementString);
            sqlStatement.execute(sqlStatementString);
         }

         // ================================================
         // Sort, search, limit existing next/previous set.

         if (advancedSortSearch)
         {
            if (dataSourceType.equals(ConnectionManager.ORACLE))
            {
               String sqlWhereString = "";
               String sqlOrderString = "";

               sqlStatementString = advancedSortSearchFrame.getAdvancedSortSearchSQL(sqlTableFieldsString,
                                                                                     tableRowStart,
                                                                                     tableRowLimit).toString();

               // Clean up the standard sql to meet Oracle's lack of support
               // for the key word LIMIT.

               // Collect WHERE & ORDER structure.
               if (sqlStatementString.indexOf("WHERE") != -1)
               {
                  if (sqlStatementString.indexOf("ORDER") != -1)
                     sqlWhereString = sqlStatementString.substring(sqlStatementString.indexOf("WHERE"),
                        sqlStatementString.indexOf("ORDER") - 1);
                  else
                     sqlWhereString = sqlStatementString.substring(sqlStatementString.indexOf("WHERE"),
                        sqlStatementString.indexOf("LIMIT") - 1);
               }
               if (sqlStatementString.indexOf("ORDER") != -1)
                  sqlOrderString = sqlStatementString.substring(sqlStatementString.indexOf("ORDER"),
                     sqlStatementString.indexOf("LIMIT") - 1);
               // Finish creating modifed SQL.
               sqlStatementString = sqlStatementString.substring(0, sqlStatementString.indexOf("FROM") + 5);

               sqlStatementString += "(SELECT ROW_NUMBER() "
                                     + ((sqlOrderString.equals("")) ? ("OVER (ORDER BY "
                                                                       + sqlTableFieldsString.substring(0,
                                                                          sqlTableFieldsString.indexOf(',')) + ") ")
                                                                   : ("OVER (" + sqlOrderString + ") "))
                                     + "AS dmprownumber, " + sqlTableFieldsString + " " + "FROM "
                                     + identifierQuoteString + tempTable + identifierQuoteString + " "
                                     + sqlWhereString + ") " + "WHERE dmprownumber BETWEEN "
                                     + (tableRowStart + 1) + " AND " + (tableRowStart + tableRowLimit);
            }
            else
               sqlStatementString = advancedSortSearchFrame.getAdvancedSortSearchSQL("*", tableRowStart,
                                    tableRowLimit).toString();
         }
         else
         {
            if (dataSourceType.equals(ConnectionManager.ORACLE))
            {
               sqlStatementString = "SELECT * " + " FROM " + "(SELECT ROW_NUMBER() OVER " + "(ORDER BY "
                                    + identifierQuoteString
                                    + columnNamesHashMap.get(sortComboBox.getSelectedItem())
                                    + identifierQuoteString + " " + ascDescString + ") " + "AS dmprownumber, "
                                    + sqlTableFieldsString + " " + "FROM " + identifierQuoteString
                                    + tempTable + identifierQuoteString + " " + "WHERE " + columnSearchString
                                    + " " + searchTextString + ") " + "WHERE dmprownumber BETWEEN "
                                    + (tableRowStart + 1) + " AND " + (tableRowStart + tableRowLimit);
            }
            else
               sqlStatementString = "SELECT * " + "FROM " + identifierQuoteString + tempTable
                                    + identifierQuoteString + " " + "WHERE " + columnSearchString + " "
                                    + searchTextString + " " + "ORDER BY " + identifierQuoteString
                                    + columnNamesHashMap.get(sortComboBox.getSelectedItem())
                                    + identifierQuoteString + " " + ascDescString + " "
                                    + "LIMIT " + tableRowLimit + " " + "OFFSET " + tableRowStart;
         }
         if (showQuery)
            QueryFrame.setQueryResultTextArea(sqlStatementString);
         // System.out.println(sqlStatementString);
         rs = sqlStatement.executeQuery(sqlStatementString);

         // Placing the results columns desired into the table that
         // will be display to the user.

         int i = 0;
         int j = 0;

         tableData = new Object[tableRowLimit][tableHeadings.size()];

         while (rs.next())
         {
            Iterator<String> headings = tableHeadings.iterator();
            while (headings.hasNext())
            {
               String currentHeading = headings.next();
               columnName = columnNamesHashMap.get(currentHeading);
               columnClass = columnClassHashMap.get(currentHeading);
               columnType = columnTypeHashMap.get(currentHeading);
               columnSize = (columnSizeHashMap.get(currentHeading)).intValue();
               preferredColumnSize = (preferredColumnSizeHashMap.get(currentHeading)).intValue();

               // System.out.println(i + " " + j + " " + currentHeading + " " +
               //                   columnName + " " + columnClass + " " +
               //                   columnType + " " + columnSize + " " +
               //                   preferredColumnSize);

               // Storing data appropriately. If you have some
               // date or other formating, here is where you can
               // take care of it.

               Object currentContentData = rs.getObject(columnName);
               // System.out.println(currentContentData);

               if (currentContentData != null)
               {
                  // =============================================
                  // BigDecimal
                  if (columnClass.indexOf("BigDecimal") != -1)
                     tableData[i][j++] = new BigDecimal(rs.getString(columnName));

                  // =============================================
                  // Date
                  else if (columnType.equals("DATE"))
                  {
                     if (dataSourceType.equals(ConnectionManager.SQLITE))
                        currentContentData = rs.getDate(columnName);
                     
                     tableData[i][j++] = new SimpleDateFormat(
                        DBTablesPanel.getGeneralDBProperties().getViewDateFormat()).format(currentContentData);
                  }

                  // =============================================
                  // Datetime
                  else if (columnType.equals("DATETIME"))
                     tableData[i][j++] = new SimpleDateFormat(
                        DBTablesPanel.getGeneralDBProperties().getViewDateFormat() + " HH:mm:ss")
                           .format(currentContentData);

                  // =============================================
                  // Time
                  else if (columnType.equals("TIME"))
                  {
                     currentContentData = rs.getTime(columnName);
                     tableData[i][j++] = (new SimpleDateFormat("HH:mm:ss").format(currentContentData));
                  }
                  
                  // =============================================
                  // Time With Time Zone
                  else if (columnType.equals("TIMETZ"))
                  {
                     currentContentData = rs.getTime(columnName);
                     tableData[i][j++] = (new SimpleDateFormat("HH:mm:ss z").format(currentContentData));
                  }

                  // =============================================
                  // Timestamps
                  else if (columnType.equals("TIMESTAMP"))
                  {
                     currentContentData = rs.getTimestamp(columnName);
                     // System.out.println(currentContentData);

                     // Old MySQL Database Requirement, 4.x.
                     if (dataSourceType.equals(ConnectionManager.MYSQL)
                         || dataSourceType.equals(ConnectionManager.MARIADB))
                     {
                        if (columnSize == 2)
                           tableData[i][j++] = (new SimpleDateFormat("yy").format(currentContentData));
                        else if (columnSize == 4)
                           tableData[i][j++] = (new SimpleDateFormat("MM-yy").format(currentContentData));
                        else if (columnSize == 6)
                           tableData[i][j++] = (new SimpleDateFormat("MM-dd-yy").format(currentContentData));
                        else if (columnSize == 8)
                           tableData[i][j++] = (new SimpleDateFormat("MM-dd-yyyy").format(currentContentData));
                        else if (columnSize == 10)
                           tableData[i][j++] = (new SimpleDateFormat("MM-dd-yy HH:mm")
                                 .format(currentContentData));
                        else if (columnSize == 12)
                           tableData[i][j++] = (new SimpleDateFormat("MM-dd-yyyy HH:mm")
                                 .format(currentContentData));
                        // All current coloumnSizes for MariaDB & MySQL > 5.0 Should be 19.
                        else
                           tableData[i][j++] = (new SimpleDateFormat(
                              DBTablesPanel.getGeneralDBProperties().getViewDateFormat() + " HH:mm:ss")
                                 .format(currentContentData));
                     }
                     else if (dataSourceType.equals(ConnectionManager.SQLITE))
                     {
                        tableData[i][j++] = (new SimpleDateFormat(
                           DBTablesPanel.getGeneralDBProperties().getViewDateFormat() + " HH:mm:ss.SSS")
                              .format(currentContentData));
                     }
                     else
                        tableData[i][j++] = (new SimpleDateFormat(
                           DBTablesPanel.getGeneralDBProperties().getViewDateFormat() + " HH:mm:ss")
                              .format(currentContentData));  
                  }

                  else if (columnType.equals("TIMESTAMPTZ") || columnType.equals("TIMESTAMP WITH TIME ZONE")
                           || columnType.equals("TIMESTAMP WITH LOCAL TIME ZONE"))
                  {
                     currentContentData = rs.getTimestamp(columnName);
                     tableData[i][j++] = (new SimpleDateFormat(
                        DBTablesPanel.getGeneralDBProperties().getViewDateFormat() + " HH:mm:ss z")
                           .format(currentContentData));
                  }

                  // =============================================
                  // Year
                  else if (columnType.equals("YEAR"))
                  {
                     String displayYear = currentContentData + "";
                     displayYear = displayYear.trim();

                     if (columnSize == 2)
                     {
                        if (displayYear.length() == 4)
                           displayYear = displayYear.substring(2, 4);
                     }
                     else
                        displayYear = displayYear.substring(0, 4);
                     tableData[i][j++] = displayYear;
                  }

                  // =============================================
                  // Blob
                  else if (columnClass.indexOf("String") == -1 && columnType.indexOf("BLOB") != -1)
                  {
                     if (columnSize == 255)
                        tableData[i][j++] = "Tiny Blob";
                     else if (columnSize == 65535)
                        tableData[i][j++] = "Blob";
                     else if (columnSize == 16777215)
                        tableData[i][j++] = "Medium Blob";
                     else if (columnSize > 16777215)
                        tableData[i][j++] = "Long Blob";
                     else
                        tableData[i][j++] = "Blob";
                  }
                  
                  //=============================================
                  // CLOB
                  else if (columnType.indexOf("CLOB") != -1)
                  {
                     tableData[i][j++] = "Clob";
                  }

                  // =============================================
                  // BYTEA
                  else if (columnType.equals("BYTEA"))
                  {
                     tableData[i][j++] = "Bytea";
                  }

                  // =============================================
                  // BINARY
                  else if (columnType.indexOf("BINARY") != -1)
                  {
                     tableData[i][j++] = "Binary";
                  }
                  
                  //=============================================
                  // RAW
                  else if (columnType.indexOf("RAW") != -1)
                  {
                     tableData[i][j++] = "Raw";
                  }

                  // =============================================
                  // Boolean
                  else if (columnClass.indexOf("Boolean") != -1)
                  {
                     tableData[i][j++] = rs.getString(columnName);
                  }

                  // =============================================
                  // Bit
                  else if (columnType.indexOf("BIT") != -1
                           && (dataSourceType.equals(ConnectionManager.MYSQL)
                               || dataSourceType.equals(ConnectionManager.MARIADB)))
                  {
                     try
                     {
                        // int bitValue = rs.getInt(columnName);
                        // tableData[i][j] = Integer.toBinaryString(bitValue);
                        tableData[i][j] = Integer.toBinaryString(Integer.parseInt(rs.getString(columnName)));
                     }
                     catch (NumberFormatException e)
                     {
                        tableData[i][j] = "0";
                     }
                     j++;   
                  }

                  // =============================================
                  // Text
                  else if (columnClass.indexOf("String") != -1
                           && (!columnType.equals("CHAR") || !columnType.equals("NCHAR"))
                           & columnSize > 255)
                  {
                     if (columnSize <= 65535)
                        tableData[i][j++] = (String) currentContentData;
                     else if (columnSize == 16777215)
                        tableData[i][j++] = ("Medium Text");
                     else
                     // (columnSize > 16777215)
                     {
                        if (dataSourceType.equals(ConnectionManager.MYSQL)
                            || dataSourceType.equals(ConnectionManager.MARIADB))
                           tableData[i][j++] = ("Long Text");
                        else
                        {
                           // Limit Table Cell Memory Usage.
                           if (((String) currentContentData).length() > 512)
                              tableData[i][j++] = ((String) currentContentData).substring(0, 512);
                           else
                              tableData[i][j++] = (String) currentContentData;
                        }
                     }
                  }
                  
                  // =============================================
                  // LONG
                  else if (columnClass.indexOf("String") != -1 && columnType.equals("LONG"))
                  {
                     // Limit Table Cell Memory Usage.
                     if (((String) currentContentData).length() > 512)
                        tableData[i][j++] = ((String) currentContentData).substring(0, 512);
                     else
                        tableData[i][j++] = (String) currentContentData;
                  }

                  // =============================================
                  // Array
                  else if ((columnClass.indexOf("Object") != -1 || columnClass.indexOf("Array") != -1)
                           && (columnType.indexOf("_") != -1))
                  {
                     String stringName;
                     currentContentData = rs.getString(columnName);
                     stringName = (String) currentContentData;

                     // Limit Table Cell Memory Usage.
                     if (stringName.length() > 512)
                        tableData[i][j++] = stringName.substring(0, 512);
                     else
                        tableData[i][j++] = stringName;
                  }

                  // =============================================
                  // Any Other
                  else
                  {
                     tableData[i][j++] = rs.getString(columnName).trim();
                     // tableData[i][j++] = rs.getObject(columnName);
                  }
               }
               // Null Data
               else
               {
                  tableData[i][j++] = "NULL";
               }

               // Setup some sizing for the column in the summary
               // table.
               if ((tableData[i][j - 1] + "").length() * 9 > preferredColumnSize)
               {
                  preferredColumnSize = (tableData[i][j - 1] + "").length() * 9;
                  if (preferredColumnSize > maxPreferredColumnSize)
                     preferredColumnSize = maxPreferredColumnSize;
               }
               preferredColumnSizeHashMap.put(currentHeading, Integer.valueOf(preferredColumnSize));
            }
            j = 0;
            i++;
         }
         rs.close();
         sqlStatement.close();
         validQuery = true;
      }
      catch (SQLException e)
      {
         String errorString = "SQLException: " + e.getMessage() + " " + "SQLState: " + e.getSQLState() + " "
                              + "VendorError: " + e.getErrorCode();
         QueryFrame.setQueryResultTextArea(errorString);
         validQuery = false;
         return false;
      }
      return true;
   }
   
   //==============================================================
   // Class method to create the summary table view popup menu.
   //==============================================================

   private void createListTablePopupMenu()
   {
      // Method Instances.
      JPopupMenu summaryTablePopupMenu = new JPopupMenu();
      JMenuItem menuItem;
      String resource;
      
      // Basic table actions.

      // No keys than cannot perform these operations.
      //if (!primaryKey.isEmpty())
      //{
         resource = resourceBundle.getResourceString("QueryTabPanel.menu.View", "View");
         menuItem = new JMenuItem(resource);
         menuItem.setActionCommand("View");
         menuItem.addActionListener(this);
         summaryTablePopupMenu.add(menuItem);
         
         summaryTablePopupMenu.addSeparator();
      //}

      // Summary Table select actions.
      
      resource = resourceBundle.getResourceString("QueryTabPanel.menu.SelectAll", "Select All");
      menuItem = new JMenuItem(resource);
      menuItem.setActionCommand("Select All");
      menuItem.addActionListener(this);
      summaryTablePopupMenu.add(menuItem);

      resource = resourceBundle.getResourceString("QueryTabPanel.menu.DeSelectAll", "DeSelect All");
      menuItem = new JMenuItem(resource);
      menuItem.setActionCommand("DeSelect All");
      menuItem.addActionListener(this);
      summaryTablePopupMenu.add(menuItem);
      
      // Summary Table copy/paste? actions
      
      summaryTablePopupMenu.addSeparator();
      
      resource = resourceBundle.getResourceString("QueryTabPanel.menu.Copy", "Copy");
      menuItem = new JMenuItem(resource);
      menuItem.setActionCommand((String)TransferHandler.getCopyAction().getValue(Action.NAME));
      menuItem.setMnemonic(KeyEvent.VK_C);
      menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
      menuItem.addActionListener(this);
      summaryTablePopupMenu.add(menuItem);
      
      summaryTablePopupListener = new MyJSQLView_MouseAdapter(summaryTablePopupMenu);
   }

   //=============================================================
   // Class method to parse the MyJSQLView table's column name
   // fields. The parsed strings creates a more user friendly
   // format for use in the sort and search comboBoxes.
   //=============================================================

   private String parseColumnNameField(String columnString)
   {
      // Method Instances
      StringTokenizer field;
      StringBuffer columnName;
      
      // Initialize.
      columnName = new StringBuffer();

      // Delimiter '_' should seperate words in a name.

      // Multiple word name.
      if (columnString.indexOf('_') != -1)
      {
         field = new StringTokenizer(columnString, "_");

         while (field.hasMoreTokens())
         {
            if (field.countTokens() > 1)
               columnName.append(firstLetterToUpperCase(field.nextToken()) + " ");
            else
               columnName.append(firstLetterToUpperCase(field.nextToken()));

         }
         columnString = columnName.toString();
      }

      // Single word name.
      else
      {
         columnString = firstLetterToUpperCase(columnString);
      }
      return columnString;
   }

   //==============================================================
   // Class method to convert the first letter of the input string
   // to uppercase.
   //==============================================================

   private String firstLetterToUpperCase(String capitalizeString)
   {
      String firstLetter;

      if (capitalizeString.length() != 0)
      {
         firstLetter = capitalizeString.substring(0, 1);
         firstLetter = firstLetter.toUpperCase();
         return firstLetter + capitalizeString.substring(1);
      }
      else
         return capitalizeString;
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
   // Class method to view the current selected item in the table.
   //==============================================================

   private void viewSelectedItem(Connection dbConnection) throws SQLException
   {
      // Method Instances
      String sqlStatementString;
      Statement sqlStatement;
      ResultSet db_resultSet;

      Iterator<String> textFieldNamesIterator;
      String currentColumnName;
      Object key, currentContentData;
      String currentDB_ColumnName, currentColumnClass, currentColumnType;
      int currentColumnSize;
      int keyColumn = 0;

      // Obtain the key, this key made from the
      // the temporary table creation.
      for (int i = 0; i < listTable.getColumnCount(); i++)
         if (listTable.getColumnName(i).equals(primaryKey))
            keyColumn = i;

      key = listTable.getValueAt(listTable.getSelectedRow(), keyColumn);

      // Connecting to the data base, to obtain
      // the selected entry.
      sqlStatement = null;
      db_resultSet = null;
      
      try
      {
         // Begin the SQL statement creation.
         sqlStatement = dbConnection.createStatement();
         
         sqlStatementString = "SELECT * FROM " 
               + identifierQuoteString + sqlTable + identifierQuoteString
               + " WHERE " + columnNamesHashMap.get(fields.get(0)) + "='" + key + "'";
         
         // System.out.println(sqlStatementString);
         db_resultSet = sqlStatement.executeQuery(sqlStatementString);
         db_resultSet.next();

         // Cycling through the item fields and setting
         // in the tableViewForm.

         textFieldNamesIterator = fields.iterator();
         int i = 0;
         
         while (textFieldNamesIterator.hasNext())
         {
            currentColumnName = textFieldNamesIterator.next();
            currentDB_ColumnName = columnNamesHashMap.get(currentColumnName);
            currentColumnClass = columnClassHashMap.get(currentColumnName);
            currentColumnType = columnTypeHashMap.get(currentColumnName);
            currentColumnSize = (columnSizeHashMap.get(currentColumnName)).intValue();

            currentContentData = db_resultSet.getString(currentDB_ColumnName);
            // System.out.println(i + " " + currentColumnName + " " +
            // currentDB_ColumnName +
            // currentColumnType + " " + columnSize + " " + currentContentData);

            if (currentContentData != null)
            {
               // DATE Type Field
               if (currentColumnType.equals("DATE"))
               {
                  tableViewForm.setFormField(currentColumnName,
                     (Object) MyJSQLView_Utils.convertDBDateString_To_ViewDateString(currentContentData + "",
                        DBTablesPanel.getGeneralDBProperties().getViewDateFormat()));
               }

               // DATETIME Type Field
               else if (currentColumnType.equals("DATETIME"))
               {
                  String dateString = currentContentData + "";
                  dateString = dateString.substring(0, (dateString.indexOf(" ")));
                  dateString = MyJSQLView_Utils.convertDBDateString_To_ViewDateString(dateString + "",
                     DBTablesPanel.getGeneralDBProperties().getViewDateFormat());

                  String timeString = currentContentData + "";
                  timeString = timeString.substring(timeString.indexOf(" "));
                  currentContentData = dateString + timeString;
                  tableViewForm.setFormField(currentColumnName, currentContentData);
               }
               
               // Time
               else if (currentColumnType.equals("TIME"))
               {
                  currentContentData = db_resultSet.getTime(currentDB_ColumnName);
                  tableViewForm.setFormField(currentColumnName, (new SimpleDateFormat("HH:mm:ss")
                        .format(currentContentData)));
               }

               // Time With Time Zone
               else if (currentColumnType.equals("TIMETZ"))
               {
                  currentContentData = db_resultSet.getTime(currentDB_ColumnName);
                  tableViewForm.setFormField(currentColumnName, (new SimpleDateFormat("HH:mm:ss z")
                        .format(currentContentData)));
               }

               // TIMESTAMP Type Fields
               else if (currentColumnType.equals("TIMESTAMP"))
               {
                  currentContentData = db_resultSet.getTimestamp(currentDB_ColumnName);
                  currentColumnSize = (columnSizeHashMap.get(currentColumnName)).intValue();

                  // Old MySQL Database Requirement, 4.x.
                  if (dataSourceType.equals(ConnectionManager.MYSQL)
                      || dataSourceType.equals(ConnectionManager.MARIADB))
                  {
                     if (currentColumnSize == 2)
                        tableViewForm.setFormField(currentColumnName,
                                                   (new SimpleDateFormat("yy").format(currentContentData)));
                     else if (currentColumnSize == 4)
                        tableViewForm.setFormField(currentColumnName,
                                                   (new SimpleDateFormat("MM-yy").format(currentContentData)));
                     else if (currentColumnSize == 6)
                        tableViewForm.setFormField(currentColumnName,
                                                   (new SimpleDateFormat("MM-dd-yy").format(currentContentData)));
                     else if (currentColumnSize == 8)
                        tableViewForm.setFormField(currentColumnName,
                                                   (new SimpleDateFormat("MM-dd-yyyy").format(currentContentData)));
                     else if (currentColumnSize == 10)
                        tableViewForm.setFormField(currentColumnName,
                                                   (new SimpleDateFormat("MM-dd-yy HH:mm").format(currentContentData)));
                     else if (currentColumnSize == 12)
                        tableViewForm.setFormField(currentColumnName,
                                                   (new SimpleDateFormat("MM-dd-yyyy HH:mm").format(currentContentData)));
                     // All current coloumnSizes for MariaDB, MySQL > 5.0 Should be 19.
                     else
                        tableViewForm.setFormField(currentColumnName,
                           (new SimpleDateFormat(
                              DBTablesPanel.getGeneralDBProperties().getViewDateFormat()
                              + " HH:mm:ss").format(currentContentData)));
                  }
                  else
                     tableViewForm.setFormField(currentColumnName,
                        (new SimpleDateFormat(
                           DBTablesPanel.getGeneralDBProperties().getViewDateFormat()
                           + " HH:mm:ss").format(currentContentData)));        
               }

               else if (currentColumnType.equals("TIMESTAMPTZ") || currentColumnType.equals("TIMESTAMP WITH TIME ZONE")
                        || currentColumnType.equals("TIMESTAMP WITH LOCAL TIME ZONE"))
               {
                  currentContentData = db_resultSet.getTimestamp(currentDB_ColumnName);
                  tableViewForm.setFormField(currentColumnName, (new SimpleDateFormat(
                     DBTablesPanel.getGeneralDBProperties().getViewDateFormat()
                     + " HH:mm:ss z").format(currentContentData)));
               }

               // YEAR Type Field
               else if (currentColumnType.equals("YEAR"))
               {
                  String displayYear = currentContentData + "";
                  displayYear = displayYear.trim();

                  if (currentColumnSize == 2)
                     displayYear = displayYear.substring(2, 4);
                  else
                     displayYear = displayYear.substring(0, 4);

                  currentContentData = displayYear;
                  tableViewForm.setFormField(currentColumnName, currentContentData);
               }

               // Blob Type Field
               else if ((currentColumnClass.indexOf("String") == -1 &&
                         currentColumnType.indexOf("BLOB") != -1) ||
                        (currentColumnType.indexOf("BYTEA") != -1) ||
                        (currentColumnType.indexOf("BINARY") != -1))
               {
                  if (((String) currentContentData).getBytes().length != 0)
                  {
                     currentContentData = db_resultSet.getBytes(currentDB_ColumnName);

                     int size = ((byte[]) currentContentData).length;
                     if (currentColumnType.indexOf("BLOB") != -1)
                     {
                        tableViewForm.setFormField(currentColumnName, (Object) ("BLOB " + size + " Bytes"));
                        tableViewForm.setFormFieldBlob(currentColumnName, (byte[]) currentContentData);
                     }
                     else if (currentColumnType.equals("BYTEA"))
                     {
                        tableViewForm.setFormField(currentColumnName, (Object) ("BYTEA " + size + " Bytes"));
                        tableViewForm.setFormFieldBlob(currentColumnName, (byte[]) currentContentData);
                     }
                     else
                     {
                        tableViewForm.setFormField(currentColumnName, (Object) ("BINARY " + size + " Bytes"));
                        tableViewForm.setFormFieldBlob(currentColumnName, (byte[]) currentContentData);
                     }
                  }
                  else
                  {
                     if (currentColumnType.indexOf("BLOB") != -1)
                        tableViewForm.setFormField(currentColumnName, (Object) "BLOB 0 Bytes");
                     else if (currentColumnType.equals("BYTEA"))
                        tableViewForm.setFormField(currentColumnName, (Object) "BYTEA 0 Bytes");
                     else
                        tableViewForm.setFormField(currentColumnName, (Object) "BINARY 0 Bytes");
                  }

               }

               // Bit Type Field
               else if (currentColumnType.equals("BIT")
                        && (dataSourceType.equals(ConnectionManager.MYSQL)
                            || dataSourceType.equals(ConnectionManager.MARIADB)))
               {
                  int bitValue = db_resultSet.getInt(currentDB_ColumnName);
                  tableViewForm.setFormField(currentColumnName, Integer.toBinaryString(bitValue));
               }

               // Text, MediumText & LongText Type Fields
               else if (currentColumnClass.indexOf("String") != -1 &&
                        !currentColumnType.equals("CHAR") &&
                        ((columnSizeHashMap.get(currentColumnName)).intValue() > 255))
               {
                  if (((String) currentContentData).getBytes().length != 0)
                  {
                     int size = ((String) currentContentData).getBytes().length;
                     tableViewForm.setFormField(currentColumnName, (Object) ("TEXT " + size + " Bytes"));
                     tableViewForm.setFormFieldText(currentColumnName, (String) currentContentData);
                  }
                  else
                     tableViewForm.setFormField(currentColumnName, (Object) "TEXT 0 Bytes");
               }

               // Array, Fields
               else if ((currentColumnClass.indexOf("Object") != -1 ||
                         currentColumnClass.indexOf("Array") != -1) &&
                        (currentColumnType.indexOf("_") != -1))
               {
                  int size;

                  // Determine the number of elements in the array for
                  // display in the view form.

                  if (((String) currentContentData).getBytes().length != 0)
                  {
                     if (currentColumnType.indexOf("POINT") != -1 ||
                         currentColumnType.indexOf("LSEG") != -1 ||
                         currentColumnType.indexOf("PATH") != -1 ||
                         currentColumnType.indexOf("POLYGON") != -1 ||
                         currentColumnType.indexOf("CIRCLE") != -1)
                     {
                        String[] dimensionStrings = ((String) currentContentData).split("\"");
                        size = (dimensionStrings.length - 1) / 2;
                     }
                     else
                     {
                        size = 0;
                        int indexOfComma = 0;

                        String[] dimensionStrings = ((String) currentContentData).split("}");

                        for (int j = 0; j < dimensionStrings.length; j++)
                        {
                           indexOfComma = dimensionStrings[j].indexOf(',', 0);
                           while (indexOfComma != -1)
                           {
                              size++;
                              dimensionStrings[j] = dimensionStrings[j].substring(indexOfComma + 1);
                              indexOfComma = dimensionStrings[j].indexOf(',');
                           }
                        }
                        size++;

                        if (currentColumnType.indexOf("BOX") != -1)
                           size = size / 4;
                     }

                     tableViewForm.setFormField(currentColumnName, (Object) ("ARRAY " + size + " Elements"));
                     tableViewForm.setFormFieldText(currentColumnName, (String) currentContentData);
                  }
                  else
                     tableViewForm.setFormField(currentColumnName, (Object) "ARRAY 0 Elements");
               }

               // Default Content. A normal table entry should
               // fall through here, to set content.
               else
                  tableViewForm.setFormField(currentColumnName, currentContentData);
            }
            // Null fields fall through here.
            else
            {
               tableViewForm.setFormField(currentColumnName, "NULL");
            }
            i++;
         }
      }
      catch (SQLException e)
      {
         ConnectionManager.displaySQLErrors(e, "QueryTabPanel viewSelectedItem()");
      }
      finally
      {
         try
         {
            if (db_resultSet != null)
               db_resultSet.close();
         }
         catch (SQLException sqle)
         {
            ConnectionManager.displaySQLErrors(sqle, "QueryTabPanel viewSelectedItem()");
         }
         finally
         {
            if (sqlStatement != null)
               sqlStatement.close();
         }
      }
   }

   //==============================================================
   // Class method to toggle the current cardlayout in the center
   // panel. The method toggles between the summary list table
   // the tableview form.
   //==============================================================

   public void toggleCenterPanel()
   {
      centerCardLayout.next(centerPanel);
      centerPanel.requestFocus();
   }

   //==============================================================
   // Class Method to print the Panel's current information.
   //==============================================================

   public int print(Graphics g, PageFormat pageFormat, int pageIndex)
   {
      if (pageIndex > 0)
         return NO_SUCH_PAGE;
      Graphics2D g2 = (Graphics2D) g;
      g2.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

      RepaintManager currentManager = RepaintManager.currentManager(this);
      currentManager.setDoubleBufferingEnabled(false);
      final Rectangle rect = g2.getClipBounds();

      double scaleFactor = rect.getWidth() / this.getWidth();
      g2.scale(scaleFactor, scaleFactor);
      // pageFormat.setOrientation(PageFormat.LANDSCAPE);
      paintAll(g2);
      currentManager.setDoubleBufferingEnabled(true);
      return PAGE_EXISTS;
   }

   //==============================================================
   // Class method to allow classes to obtain the list of allowed
   // column names that can be viewed in the panel.
   //==============================================================

   public ArrayList<String> getTableFields()
   {
      return fields;
   }

   //==============================================================
   // Class method to allow classes to obtain the list of allowed
   // column names that is presently in the summary table.
   //==============================================================

   public ArrayList<String> getTableHeadings()
   {
      return tableHeadings;
   }

   //==============================================================
   // Class method to allow classes to obtain name of the table.
   //==============================================================

   public String getTableName()
   {
      return sqlTable;
   }

   //==============================================================
   // Class method to allow classes to obtain the summary list
   // table presently displayed in the tab.
   //==============================================================

   public JTable getListTable()
   {
      return listTable;
   }

   //==============================================================
   // Class method to allow classes to obtain the columnNamesHashMap.
   //==============================================================

   public HashMap<String, String> getColumnNamesHashMap()
   {
      return columnNamesHashMap;
   }

   //==============================================================
   // Class method to allow classes to obtain the columnClassHashMap.
   //==============================================================

   public HashMap<String, String> getColumnClassHashMap()
   {
      return columnClassHashMap;
   }

   //==============================================================
   // Class method to allow classes to obtain the columnTypeHashMap.
   //==============================================================

   public HashMap<String, String> getColumnTypeHashMap()
   {
      return columnTypeHashMap;
   }

   //==============================================================
   // Class method to allow classes to obtain the columnSizeHashMap.
   //==============================================================

   public HashMap<String, Integer> getColumnSizeHashMap()
   {
      return columnSizeHashMap;
   }
   
   //==============================================================
   // Class method to simplify the setting of the rows indicator
   // label for internationalization.
   //==============================================================

   private void setRowsLabel(int start, int end)
   {
      String resourceRows;
      
      resourceRows = resourceBundle.getResourceString("QueryTabPanel.label.Rows", "Rows");
      rowsLabel.setText(resourceRows + ": " + start + " - " + end); 
   }

   //==============================================================
   // Class method to allow classes to set the summary table row
   // size.
   //==============================================================

   public void setTableRowSize(int numberOfRows)
   {
      tableRowLimit = numberOfRows;
      if (advancedSortSearch)
         advSortSearchApplyButton.doClick();
      else
         searchButton.doClick();
      setRowsLabel((tableRowStart + 1), (tableRowStart + tableRowLimit));
   }
}
