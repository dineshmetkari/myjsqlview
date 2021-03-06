//=================================================================
//                MyJSQLView TableTabPanel
//=================================================================
//
//    This class provides the general framework and link to
// the TableTab Interface inheritance for all TableTabPanels in
// MyJSQLView. The class is used mainly to define the requirements
// for implementing alternate database access in MyJSQLView, while
// maintaining limited extensions.
//
//                  << TableTabPanel.java >>
//
//=================================================================
// Copyright (C) 2005-2015 Dana M. Proctor
// Version 5.23 10/30/2014
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
// Version 1.0 Converted TableTabPanel Class.
//         1.1 Cleaned Up Javadoc Comments.
//         1.2 Comment Changes.
//         1.3 Header Update.
//         1.4 Added Class Instances & All Common Class Methods
//             From Sub-Classes to Here. Trying to Clean Up Children Sub-Classes.
//         1.5 Changes to rowLabel, and doneButton to closeViewButton.
//         1.6 Class Method setTableRowSize() Changed setTableFields() to
//             setTableHeadings().
//         1.7 Changed Class Instance tableHeadings to currentTableHeadings.
//             Added Class Instance allTableHeadings. Added Class Method
//             getAllTableHeadings().
//         1.8 Added Class Instance schemaTableName.
//         1.9 Added Class Instance sqlTableFieldsString.
//         2.0 Added Class Instance sqlTableStatement and Class Method
//             getTableSQLStatement().
//         2.1 Removed Class Instance sortButton.
//         2.2 MyJSQLView Project Common Source Code Formatting.
//         2.3 Added Class Instance clearSearchTextFieldButton.
//         2.4 Added More of Initialization From Database TableTabPanels to
//             Constructor. Added Class Instance columnsSearchStrings.
//         2.5 Added ImageIcons for The TableTabPanels and Loading in Constructor.
//             Class Instances previous/nextButton Changed to previous/next
//             TableRowsButton.
//         2.6 Added Class Instances ascSortRadioButton, descSortRadioButton,
//             & ascDescString.
//         2.7 Added Class Method getValidDataRowCount().
//         2.8 Removed Class Instance columnsSearchStrings.
//         2.9 Added Class Method setSearchTextField().
//         3.0 Added Class Instances menuItemView,Add,Edit,Delete,Select All,
//             and DeSelect All.
//         3.1 Removed Class Instances From 3.0 and Their Creation in the
//             Constructor.
//         3.2 Commented Out System.out.println() in Method keyTyped(). Also
//             Removed in Same Method Detection of Alt-V Being Acted On.
//         3.3 Added Class Method formatJavaDateString().
//         3.4 Added Class Instances panelSource & busyProcessing.
//         3.5 Corrections to Some Method Comments.
//         3.6 Class Instance ascDescString Moved to be With Rest of Initialization
//             Instances at Beginning of Constructor.
//         3.7 Header Format Changes/Update.
//         3.8 Added Class Methods get/setSaveFileName().
//         3.9 Added Class Methods get/setState() and getStateDelimiter().
//         4.0 Added Additional Robustness for ComboBox Setting in Class Method
//             setState().
//         4.1 Set the rowsLabel After Refreshing the Table Summary View in the
//             Class Method setState().
//         4.2 Removed the Refreshing of Table Summary View and setTableHeadings From
//             the Class Method setState(). All Done in New Class LoadTableStateThread.
//         4.3 Class Method setSTate() Check For Valid Table Field Names.
//        4.30 Minor Correction to Fix Redundant Call to toString() in getState().
//        4.31 Class Method setState(), Correction to Toggle asc/descRadioButton
//             Properly and Set the ascDescString.
//        4.32 Added Class Instances selectedRow, horizontalScrollBarPosition, &
//             verticalScrollBarPosition.
//        4.33 Added Class Instances activateReplaceButton and replaceIcon.
//        4.34 Added Class Instance updateFrame.
//        4.35 Renamed replaceIcon to updateIcon, and activateReplaceButton to
//             activateUpdateButton. Added Class Instance updateFormFindButton.
//        4.36 Added Class Instance deleteAllButton.
//        4.37 Added Class Instance deleteDataIcon.
//        4.38 Removed Constructor Instance fileSeparator. Obtained iconsDirectory
//             From MyJSQLView_Utils Class.
//        4.39 10/25/2009 Added fileSeparator to iconsDirectory.
//        4.40 Removed Class Method formatJavaDateString(). Class Method Moved to
//             MyJSQLView_Utils Class.
//        4.41 Integration of TableTabPanels. Moved All Common Methods From the
//             Individual TableTabPanels, ex. TableTabPanel_MySQL, to Here.
//        4.42 Class Method firstLetterToUpperCase() Check for Empty String.
//        4.43 Class Methods getState(), parseColumnNameField(), & pasteClipboardContents(),
//             Method Instances currentState, columnName, & tableHeadingsString
//             Changed to StringBuffers.
//        4.44 Changed Package to Reflect Dandy Made Productions Code.
//        4.45 Implementation of Internationalization via MyJSQLView_ResourceBundle
//             Class Instance resourceBundle. Constructor and Class Methods deleteSelectedItems(),
//             deleteAllItems(), pasteClipboardContents(), & setState(). Added Method
//             setRowsLabel().
//        4.46 Class Method setRowsLabel() Correction in Setting rowsLabel. Removal
//             of Some Comments.
//        4.47 Added resourceBundle Instance to Creation of the AdvancedSortSearchForm
//             & UpdateForm in Class Methods createAdvancedSortSearchFrame() and
//             createUpdateFrame().
//        4.48 Organized Constructor Instances.
//        4.49 Class Method deleteSelectedItem() Changed Method Instance sqlStatementString
//             to a StringBuffer.
//        4.50 Class Method createAdvancedSortSearchFrame() Added Instance columnTypeHashMap
//             to AdvancedSortSeaarchForm Creation.
//        4.51 Added Class Instances lob_sqlTableFieldsString & lobDataTypesHashMap.
//        4.52 Moved Actions in displayMyDateString() Method to MyJSQLView_Utils
//             Class.
//        4.53 Made Class public to Allow Plugin Classes Access.
//        4.54 Added Class Instance serialVersionUID.
//        4.55 Parameterized All Vector, & HashMap Types to Bring Code Into
//             Compliance With Java 5.0 API.
//        4.56 Minor Comment Changes and the Removal of Unecessary Casts for
//             HashMap Types.
//        4.57 Minor Format Changes.
//        4.58 Parameteriezed headings in Constructor, keyIterator in deleteSelectedItem(),
//             headingsIterator in getState(), and headings in setTableHeadings().
//        4.59 Collected Class Instance schemaTableName in Constructor From MyJSQLView_Utils
//             Method getSchemaTableName().
//        4.60 Added TableTabInterface Method setActionButtonsVisible().
//        4.61 Constructor Instance listTable Set Header Font Bold.
//        4.62 Added Class Instance viewOnly and Argument boolean viewOnlyTable to Constructor.
//             Removed Class Method setActionButtonVisible().
//        4.63 Added Class Instance sqlTableSearchString and Method getTableSQLSearchString().
//        4.64 Undid 4.63 Revision, Short Sighted.
//        4.65 Made Class Method getStateDelimiter() Public.
//        4.66 Class Methods getTableFields(), getCurrentTableHeadings(), getAllTableHeadings(),
//             & getPrimaryKeys() Returned Copy of Vectors.
//        4.67 Removed Code for Collection schemaTableName. Done Through MyJSQLView_Utils in
//             Constructor. Also Updated Methods deleteSelectedItems() & deleteAllItems() Removed
//             BEGIN Statement SQL Query Execution for SQLite Database.
//        4.68 Changed Class Instance refreshButton to Protected.
//        4.69 Class Method displayMyDateString() Changed the Call to MyJSQLView_Utils
//             Method convertDBDateString_To_ViewDateString(). Also the Same in
//             Method deleteSelectedItems() Except convertViewDateString_To_DBDateString
//             for Date Keys.
//        4.70 Class Methods executeActions() & setTableHeadings() Cast Object Returned
//             by MyJSQLView_Access.getConnection() to Connection.
//        4.71 Constructor identifierQuoteString Obtained From Redefined ConnectionManager
//             and Also Connections/Errors in execcuteActions(), setSpecialFieldData(),
//             deleteSelectedItems(), deleteAllItems(), & setTableHeadings(). Collection
//             of subProtocol Instance in deleteSelected/AllItems().
//        4.72 Added Select Fields to Popup in createListTablePopup(). Added Action Event in
//             actionPerformed() to Handle New Popup Menu Select Fields Along With New Method
//             selectTableFields(). Removed resourceRowsOf in setRowsLabel().
//        4.73 Class Method getTableFields() Check for fields NULL, Then Return.
//        4.74 Class Method pasteClipboardContents() insertUpdateDialog.pack() Instead of
//             Sized.
//        4.75 Addition of Save As Image to createListTablePopup(). Handling in actionPerformed().
//        4.76 Added columnSize Instance to Constructor. Set Minimum Table Column preferredSize
//             to 45.
//        4.77 Added Class Instance stateDelimiter, Cleaned Up Some and Comment Changes.
//        4.78 Minor Comment Changes.
//        4.79 Constuctor JTable listTable.setDragEnabled(true).
//        4.80 Implemented Table History Mechanism. Added Class Instances historyAction,
//             stateHistoryLimit, previous/nextStateIcons previous/nextStateButton, &
//             stateHistory. Added Class Methods saveHistory() & executeHistoryAction().
//        4.81 Reversed the Logic of saveAction Class Instance and Used to Not Save
//             State Changes During Add, Edit, Delete, DeleteAll, Refresh, & Row Increment/
//             Decrements.
//        4.82 Class Method selectTableField() Passed this to Creation of
//             TableFieldSelectionPreferencesPanel.
//        4.83 Correction in Class Method setSpecialFieldData for Checking for Index of
//             Parenthsis of listStrings. Class Method deleteSelectedItems() &
//             deleteAllItems() Changed in BEGIN Statement Addition for TRUE Conditional.
//             Class Method deleteSelectedItem() Added Method Instance currentColumnClass,
//             & Exclusion of Quoting Numeric Fields for Key Validation for MS Access.
//        4.84 Method createAdvancedSortSearchFrame Addition of columnClassHashMap to
//             Argument for AdvancedSortSearchForm.
//        4.85 Class Methods deleteSelectedItem() & deleteAllItems() Changed Instance
//             subProtocol with dataSourceType.
//        4.86 Additional Conditional Qualifier in executeActions() for Checking the
//             sortComboBox Selection Before Setting to Index 1.
//        4.87 Modified Code From 4.86 to More Fully Comply With Desired Results of
//             Sort Column Name Not Being Null or Empty.
//        4.88 Added Class Method setViewOnly() to Meet Interface Requirements.
//        4.89 Modification of Processing for DateTime Field for MSAccess to Use the
//             # Character for Quotes in deleteSelectedItems() Keys.
//        4.90 Removed Class Instances advancedSort/SearchButton & Replaced With
//             advSortSearchApplyButton. Class Methods Effected executeActions() &
//             createAdvancedSortSearchFrame().
//        4.91 Implemented TableColumnModelListener for Detecting Moves in Columns for
//             the listTable. Added Interface Requirements and Listener Addition in
//             Constructor, setTableHeadings() & setViewOnly() Methods.
//        4.92 Copyright Update.
//        4.93 Removed the Casting of (Connection) for the Returned Instance for the
//             ConnectionManager.getConnection() in executeActions() & setTableHeadings().
//        4.94 Class Method getTableSQLStatement() Returned toString().
//        4.95 Retracted Version Changed 4.94.
//        4.96 Class Method setViewOnly() Check to Insure Action Buttons Not NULL.
//        4.97 Commented Out dbConnection.setAutoCommit() Statements and commit()
//             for Deletes of All Table Data, deleteAllItems().
//        4.98 Backed Out Revision 4.97.
//        4.99 Change in deleteAllItems() to Use TRUNCATE Instead of DELETE SQL
//             Command for HSQL Because of Heap Memory Errors for Tables With a
//             Large Number of Rows, 300k+.
//        5.00 Addition of validColumnNames Instance to Constructor. Class Methods
//             setSpecialFieldData(), deleteSelectedItems(), & deleteAllItems()
//             to Throw SQLException With finally for Closing SQLStatement. Try
//             catch Statement in executeActions() for These Methods in Addition
//             to editSelectedItem() Call.
//        5.01 Method getTableSQLStatement() Return to.String().
//        5.02 Changed sqlTableStatement Class Instance to a StringBuffer.
//        5.03 Returned a new String() for getTableSQLStatement().
//        5.04 Backed Out Revision 5.03.
//        5.05 Moved the Creation of the viewButton Out of the Block of Only if
//             !primaryKeys.isEmpty(). Allows View Tables Content Data Blob/Binary/Text
//             Content to be Accessed.
//        5.06 Changed Return Type for Class Methods getCurrentTableHeadings(),
//             getTableFields(), getAllTableHeadings(), & getPrimaryKeys() from
//             Vector to ArrayList. Class Instances fields, formFields, viewFormFields,
//             comboBoxFields, currentTableHeadings, allTableHeadings, & primaryKeys
//             Changed from Vector to ArrayList. All Corresponding Interactions,
//             getter/setters for Those Instances Also Changed.
//        5.07 Added Class Methods getForeignKeys() & getExportedKeys() to Conform
//             to Interface Requirement.
//        5.08 Changes in Way MyJSQLView_ResourceBundle Handles the Collection
//             of Resource Strings. Change to resource.getResourceString(key,
//             default).
//        5.09 MyJSQLView Class Method Change of getLocaleResourceBundle()
//             to getResourceBundle().
//        5.10 Closure for db_resultSet in setSpecialFieldData() Moved to finally.
//        5.11 Collection of All Image Resources Through resourceBundle.
//        5.12 Changed Package Name to com.dandymadeproductions.myjsqlview.gui.panels.
//             Made Method parseColumnNameField() Public.
//        5.13 advSortSearchApplyButton Instance Related to Form advancedSort
//             SearchFrame.getApplyButton(). Class Instance createUpdateFrame()
//             Instance updateFormFindButton Related to UpdateForm Through
//             getFindButton(); Same Except Just Assign ActionListener for the
//             Form's disposeButton Through getDisposeButton().
//        5.14 Class Method deleteSelectedItem() Inclution of Both Derby & HSQL
//             Databases to Removing Quotes in Normal Keys for Numeric Types.
//        5.15 Class Method getTableSQLStatement() Returns a New String().
//        5.16 Change in displayMyDateString() & deleteSelectedItem() to Use
//             DBTablePanel.getGeneralDBProperties().
//        5.17 Correction in Method pasteClipboardContents() to Create a Thread
//             Instance for the CSVDataImportThead and Start it.
//        5.18 Method getTableSqlStatement() Changed Return Type From String to
//             StringBuffer.
//        5.19 Creation of Method Instance newStringBuffer in getTableSQLStatement().
//        5.20 Method setSpecialFieldData() Changes in Conditional for Inclution
//             of Bit Column Types for True/False Option. Uppercase Conversion for
//             Check.
//        5.21 Added The Clearing, Setting to Index 0, searchComboBox in Method
//             setSearchTextField().
//        5.22 Class Methods deletSelectedItems() & deleteAllItems() Use of BEGIN
//             Statement for MariaDB. In Former Date Formatting for Same DB.
//        5.23 Parameterized JComboBox Class Instances sortComboBox & searchComboBox
//             to Conform With JRE 7.
//        
//-----------------------------------------------------------------
//                 danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview.gui.panels;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.StringTokenizer;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
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
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.table.TableColumn;

import com.dandymadeproductions.myjsqlview.MyJSQLView;
import com.dandymadeproductions.myjsqlview.datasource.ConnectionManager;
import com.dandymadeproductions.myjsqlview.gui.MyJSQLView_MouseAdapter;
import com.dandymadeproductions.myjsqlview.gui.forms.AdvancedSortSearchForm;
import com.dandymadeproductions.myjsqlview.gui.forms.TableEntryForm;
import com.dandymadeproductions.myjsqlview.gui.forms.TableViewForm;
import com.dandymadeproductions.myjsqlview.gui.forms.UpdateForm;
import com.dandymadeproductions.myjsqlview.io.CSVDataImportThread;
import com.dandymadeproductions.myjsqlview.io.WriteDataFile;
import com.dandymadeproductions.myjsqlview.utilities.BlobTextKey;
import com.dandymadeproductions.myjsqlview.utilities.ImageUtil;
import com.dandymadeproductions.myjsqlview.utilities.InputDialog;
import com.dandymadeproductions.myjsqlview.utilities.MyJSQLView_ResourceBundle;
import com.dandymadeproductions.myjsqlview.utilities.MyJSQLView_TableModel;
import com.dandymadeproductions.myjsqlview.utilities.MyJSQLView_Utils;

/**
 *    The TableTabPanel class provides the general framework and link to the
 * TableTab Interface inheritance for all TableTabPanels in MyJSQLView. The
 * class is used mainly to define the requirements for implementing alternate
 * database access in MyJSQLView, while maintaining limited extensions.
 * 
 * @author Dana M. Proctor
 * @version 5.23 10/30/2014
 */

public abstract class TableTabPanel extends JPanel implements TableTabInterface, ActionListener, KeyListener,
                                                       TableColumnModelListener, Printable
{
   // Class Instances.
   private static final long serialVersionUID = 3857137515618481882L;
   
   protected int stateHistoryIndex;
   private int selectedRow;
   private int horizontalScrollBarPosition, verticalScrollBarPosition;
   protected int tableRowStart, tableRowLimit;
   private Object panelSource;
   protected boolean historyAction; 
   private boolean busyProcessing = false;
   private boolean settingState;
   private boolean viewOnly;
   
   protected static final int maxPreferredColumnSize = 350;
   protected static final int stateHistoryLimit = 25;
   private static final String stateDelimiter = "%;%";

   protected String sqlTable;
   protected String schemaTableName;
   private String saveFileName;
   private String lastSaveDirectory;
   protected String lob_sqlTableFieldsString;
   protected String sqlTableFieldsString;
   protected StringBuffer sqlTableStatement;
   protected String identifierQuoteString;
   protected ArrayList<String> fields, formFields, viewFormFields, comboBoxFields;
   protected ArrayList<String> currentTableHeadings, allTableHeadings;
   protected ArrayList<String> primaryKeys, foreignKeys, exportedKeys;
   private MyJSQLView_ResourceBundle resourceBundle;

   private ImageIcon previousStateIcon, nextStateIcon;
   private ImageIcon ascUpIcon, ascDownIcon, descUpIcon, descDownIcon;
   private ImageIcon searchIcon, removeIcon, updateIcon, advancedSortSearchIcon;
   private ImageIcon previousViewIcon, nextViewIcon, refreshIcon;
   private ImageIcon previousTableRowsIcon, nextTableRowsIcon;
   private ImageIcon deleteDataIcon;

   private JButton previousStateButton , nextStateButton;
   private JButton searchButton, clearSearchTextFieldButton;
   protected String ascDescString;
   private  JRadioButton ascSortRadioButton, descSortRadioButton;
   protected JComboBox<Object> sortComboBox, searchComboBox;
   protected JTextField searchTextField;
   protected MouseListener summaryTablePopupListener;

   protected AdvancedSortSearchForm advancedSortSearchFrame;
   protected JButton activateAdvancedSortSearchButton;
   private JButton advSortSearchApplyButton;
   protected boolean advancedSortSearch;

   private UpdateForm updateFrame;
   protected JButton activateUpdateButton;
   private JButton updateFormFindButton;

   protected JPanel centerPanel;
   protected JScrollPane tableScrollPane;
   protected CardLayout centerCardLayout;
   protected JTable listTable;
   protected MyJSQLView_TableModel tableModel;
   protected TableViewForm tableViewForm;

   protected Object[][] tableData;
   protected LinkedList<String> stateHistory = new LinkedList <String>();
   protected HashMap<String, String> columnNamesHashMap;
   protected HashMap<String, String> columnClassHashMap;
   protected HashMap<String, String> columnTypeHashMap;
   protected HashMap<String, Integer> columnSizeHashMap;
   protected HashMap<String, Integer> preferredColumnSizeHashMap;
   
   protected HashMap<String, String> lobDataTypesHashMap;
   protected HashMap<String, String> autoIncrementHashMap;
   protected HashMap<String, Integer> keyLengthHashMap;
   protected HashMap<String, String> columnEnumHashMap;
   protected HashMap<String, String> columnSetHashMap;
   
   private JLabel rowsLabel;
   protected JButton refreshButton;
   private JButton previousTableRowsButton, nextTableRowsButton;
   private JButton viewButton, addButton, editButton, deleteButton, deleteAllButton;
   private JButton previousViewButton, nextViewButton;
   private JButton closeViewButton;

   //==============================================================
   // TableTabPanel Constructor
   //==============================================================

   TableTabPanel(String table, Connection setup_dbConnection, boolean viewOnlyTable)
   {
      sqlTable = table;
      viewOnly = viewOnlyTable;
      
      // Constructor Instances.
      String iconsDirectory, resource;
      boolean validColumnNames;
      
      JPanel stateSortSearchPanel, statePanel, sortPanel, sortOrderButtonPanel;
      JPanel searchPanel, actionPanel, actionButtonPanel;
      JPanel tableControlIndicatorPanel, tableRowsIndicatorPanel, rowControlPanel;
      JLabel sortByLabel, searchLabel, searchForLabel;
      
      // Setting up a icons directory instance, identifier quote
      // character, & proper table name qualifier.

      iconsDirectory = MyJSQLView_Utils.getIconsDirectory() + MyJSQLView_Utils.getFileSeparator();
      validColumnNames = false;

      identifierQuoteString = ConnectionManager.getIdentifierQuoteString();
      schemaTableName = MyJSQLView_Utils.getSchemaTableName(sqlTable);
      // System.out.println(schemaTableName);

      // Initializing.
      stateHistoryIndex = -1;
      selectedRow = -1;
      horizontalScrollBarPosition = -1;
      verticalScrollBarPosition = -1;
      tableRowStart = 0;
      tableRowLimit = 50;

      saveFileName = "";
      lastSaveDirectory = "";
      sqlTableStatement = new StringBuffer();
      fields = new ArrayList <String>();
      formFields = new ArrayList <String>();
      viewFormFields = new ArrayList <String>();
      comboBoxFields = new ArrayList <String>();
      currentTableHeadings = new ArrayList <String>();
      allTableHeadings = new ArrayList <String>();
      primaryKeys = new ArrayList <String>();
      foreignKeys = new ArrayList <String>();
      exportedKeys = new ArrayList <String>();
      resourceBundle = MyJSQLView.getResourceBundle();
      
      columnNamesHashMap = new HashMap <String, String>();
      columnClassHashMap = new HashMap <String, String>();
      columnTypeHashMap = new HashMap <String, String>();
      columnSizeHashMap = new HashMap <String, Integer>();
      preferredColumnSizeHashMap = new HashMap <String, Integer>();
      
      lobDataTypesHashMap = new HashMap <String, String>();
      autoIncrementHashMap = new HashMap <String, String>();
      keyLengthHashMap = new HashMap <String, Integer>();
      columnEnumHashMap = new HashMap <String, String>();
      columnSetHashMap = new HashMap <String, String>();
      historyAction = true;
      advancedSortSearch = false;
      settingState = false;
      ascDescString = "ASC";

      previousStateIcon = resourceBundle.getResourceImage(iconsDirectory + "previousStateIcon.png");
      nextStateIcon = resourceBundle.getResourceImage(iconsDirectory + "nextStateIcon.png");
      ascUpIcon = resourceBundle.getResourceImage(iconsDirectory + "ascUpIcon.png");
      ascDownIcon = resourceBundle.getResourceImage(iconsDirectory + "ascDownIcon.png");
      descUpIcon = resourceBundle.getResourceImage(iconsDirectory + "descUpIcon.png");
      descDownIcon = resourceBundle.getResourceImage(iconsDirectory + "descDownIcon.png");
      searchIcon = resourceBundle.getResourceImage(iconsDirectory + "searchIcon.png");
      removeIcon = resourceBundle.getResourceImage(iconsDirectory + "removeIcon.png");
      advancedSortSearchIcon = resourceBundle.getResourceImage(iconsDirectory + "advancedSortSearchIcon.gif");
      updateIcon = resourceBundle.getResourceImage(iconsDirectory + "updateIcon.gif");
      previousViewIcon = resourceBundle.getResourceImage(iconsDirectory + "previousViewIcon.png");
      nextViewIcon = resourceBundle.getResourceImage(iconsDirectory + "nextViewIcon.png");
      refreshIcon = resourceBundle.getResourceImage(iconsDirectory + "refreshIcon.png");
      previousTableRowsIcon = resourceBundle.getResourceImage(iconsDirectory + "previousTableRowsIcon.png");
      nextTableRowsIcon = resourceBundle.getResourceImage(iconsDirectory + "nextTableRowsIcon.png");
      deleteDataIcon = resourceBundle.getResourceImage(iconsDirectory + "deleteDataIcon.gif");

      // General Panel Configurations
      setLayout(new BorderLayout());
      setBorder(BorderFactory.createRaisedBevelBorder());

      // ==================================================
      // Setting up the Sort/Search Interface.
      // ==================================================

      GridBagLayout gridbag = new GridBagLayout();
      GridBagConstraints constraints = new GridBagConstraints();

      stateSortSearchPanel = new JPanel(gridbag);
      stateSortSearchPanel.setBorder(BorderFactory.createEtchedBorder());

      // State Interface

      statePanel = new JPanel();
      statePanel.setLayout(new BoxLayout(statePanel, BoxLayout.X_AXIS));
      statePanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(),
                                                              BorderFactory.createEmptyBorder(0, 1, 0, 2)));
      
      previousStateButton = new JButton(previousStateIcon);
      previousStateButton.setMargin(new Insets(0, 0, 0, 0));
      previousStateButton.setEnabled(false);
      previousStateButton.addActionListener(this);
      statePanel.add(previousStateButton);
      
      nextStateButton = new JButton(nextStateIcon);
      nextStateButton.setMargin(new Insets(0, 0, 0, 0));
      nextStateButton.setEnabled(false);
      nextStateButton.addActionListener(this);
      statePanel.add(nextStateButton);
      
      buildConstraints(constraints, 0, 0, 1, 1, 1, 100);
      constraints.fill = GridBagConstraints.BOTH;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(statePanel, constraints);
      stateSortSearchPanel.add(statePanel);
      
      // Sort Interface
      
      sortPanel = new JPanel();
      sortPanel.setBorder(BorderFactory.createRaisedBevelBorder());
      
      resource = resourceBundle.getResourceString("TableTabPanel.label.SortBy", "Sort By");
      sortByLabel = new JLabel(resource + " : ", JLabel.LEFT);
      sortPanel.add(sortByLabel);

      // Connecting to the database table for obtaining
      // the column names. Sets up all the characteristics
      // of the table needed in the panel. If problems
      // arise set the fields to null and return so that
      // the application can detect this later in the checking
      // for successful initialization.

      try
      {
         validColumnNames = getColumnNames(setup_dbConnection);
      }
      catch (SQLException e)
      {
         ConnectionManager.displaySQLErrors(e, "TableTabPanel executeActions()");
      }
      if (!validColumnNames)
      {
         fields = null;
         return;
      }

      sortComboBox = new JComboBox<Object>(comboBoxFields.toArray());
      sortComboBox.addActionListener(this);
      sortPanel.add(sortComboBox);

      sortOrderButtonPanel = new JPanel();
      sortOrderButtonPanel.setLayout(new BoxLayout(sortOrderButtonPanel, BoxLayout.Y_AXIS));
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

      buildConstraints(constraints, 1, 0, 4, 1, 47, 100);
      constraints.fill = GridBagConstraints.BOTH;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(sortPanel, constraints);
      stateSortSearchPanel.add(sortPanel);

      // Search Interface

      searchPanel = new JPanel();
      searchPanel.setBorder(BorderFactory.createRaisedBevelBorder());

      resource = resourceBundle.getResourceString("TableTabPanel.label.Search", "Search");
      searchLabel = new JLabel(resource + " : ");
      searchPanel.add(searchLabel);

      searchComboBox = new JComboBox<Object>(comboBoxFields.toArray());
      searchComboBox.insertItemAt("", 0);
      searchComboBox.setSelectedIndex(0);
      searchComboBox.addActionListener(this);
      searchPanel.add(searchComboBox);

      resource = resourceBundle.getResourceString("TableTabPanel.label.SearchFor", "For");
      searchForLabel = new JLabel(resource + " : ", JLabel.LEFT);
      searchPanel.add(searchForLabel);

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

      buildConstraints(constraints, 5, 0, 8, 1, 52, 100);
      constraints.fill = GridBagConstraints.BOTH;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(searchPanel, constraints);
      stateSortSearchPanel.add(searchPanel);

      add(stateSortSearchPanel, BorderLayout.NORTH);

      // ==================================================
      // Setting up the Summary Table View.
      // ==================================================

      centerPanel = new JPanel(centerCardLayout = new CardLayout());

      // Summary table, setting up. If not successful then return.
      if (!loadTable(setup_dbConnection))
      {
         fields = null;
         return;
      }

      tableModel = new MyJSQLView_TableModel(currentTableHeadings, tableData);

      listTable = new JTable(tableModel);
      listTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
      listTable.getActionMap().put(TransferHandler.getCopyAction().getValue(Action.NAME),
                                   TransferHandler.getCopyAction());
      listTable.getActionMap().put(TransferHandler.getPasteAction().getValue(Action.NAME),
                                   TransferHandler.getPasteAction());
      listTable.getTableHeader().setFont(new Font(this.getFont().getName(), Font.BOLD,
                                                  this.getFont().getSize()));
      listTable.setDragEnabled(true);
      
      createListTablePopupMenu();
      listTable.addMouseListener(summaryTablePopupListener);
      listTable.getColumnModel().addColumnModelListener(this);

      // Sizing columns
      Iterator<String> headings = currentTableHeadings.iterator();
      TableColumn column = null;
      int columnSize;
      int i = 0;

      while (headings.hasNext())
      {
         column = listTable.getColumnModel().getColumn(i++);
         columnSize = preferredColumnSizeHashMap.get(headings.next()).intValue();
         
         if (columnSize < 20)
            columnSize = 45;
         column.setPreferredWidth(columnSize);
      }

      // Create a scrollpane for the summary table and
      // add a advanced sort/search button to corner.
      tableScrollPane = new JScrollPane(listTable);

      activateAdvancedSortSearchButton = new JButton(advancedSortSearchIcon);
      activateAdvancedSortSearchButton.addActionListener(this);
      tableScrollPane.setCorner(ScrollPaneConstants.UPPER_RIGHT_CORNER, activateAdvancedSortSearchButton);

      activateUpdateButton = new JButton(updateIcon);
      activateUpdateButton.addActionListener(this);
      tableScrollPane.setCorner(ScrollPaneConstants.LOWER_RIGHT_CORNER, activateUpdateButton);

      tableScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
      tableScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
      centerPanel.add(sqlTable, tableScrollPane);

      // Table View Form

      previousViewButton = new JButton(previousViewIcon);
      previousViewButton.setMargin(new Insets(0, 0, 0, 0));
      previousViewButton.addActionListener(this);

      resource = resourceBundle.getResourceString("TableTabPanel.button.Close", "Close");
      closeViewButton = new JButton(resource);
      closeViewButton.addActionListener(this);

      nextViewButton = new JButton(nextViewIcon);
      nextViewButton.setMargin(new Insets(0, 0, 0, 0));
      nextViewButton.addActionListener(this);

      tableViewForm = new TableViewForm(viewFormFields, columnClassHashMap, columnTypeHashMap,
                                        columnSizeHashMap, previousViewButton, closeViewButton,
                                        nextViewButton);
      centerPanel.add(sqlTable + " Form", tableViewForm);

      add(centerPanel, BorderLayout.CENTER);

      // ==================================================
      // Setting up the panels to show row indcator,
      // action buttons refresh, paging, view, add, edit,
      // and delete.
      // ==================================================

      actionPanel = new JPanel(gridbag);
      actionPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(),
                                                               BorderFactory.createEmptyBorder(0, 0, 0, 0)));

      // Panel to hold the action buttons used to
      // manupulate table entries.

      actionButtonPanel = new JPanel();
      
      // View Button
      resource = resourceBundle.getResourceString("TableTabPanel.button.View", "View");
      viewButton = new JButton(resource);
      viewButton.setMnemonic(KeyEvent.VK_V);
      viewButton.addActionListener(this);
      actionButtonPanel.add(viewButton);
      
      // No key then kind of hard to manipulate.
      
      if (!primaryKeys.isEmpty())
      {
         if (!viewOnly)
         {
            // Add Button
            resource = resourceBundle.getResourceString("TableTabPanel.button.Add", "Add");
            addButton = new JButton(resource);
            addButton.setMnemonic(KeyEvent.VK_A);
            addButton.addActionListener(this);
            actionButtonPanel.add(addButton);

            // Edit Button
            resource = resourceBundle.getResourceString("TableTabPanel.button.Edit", "Edit");
            editButton = new JButton(resource);
            editButton.setMnemonic(KeyEvent.VK_E);
            editButton.addActionListener(this);
            actionButtonPanel.add(editButton);

            // Delete Button
            resource = resourceBundle.getResourceString("TableTabPanel.button.Delete", "Delete");
            deleteButton = new JButton(resource);
            deleteButton.setMnemonic(KeyEvent.VK_D);
            deleteButton.addActionListener(this);
            actionButtonPanel.add(deleteButton);

            // Delete All Button
            resource = resourceBundle.getResourceString("TableTabPanel.button.DeleteAll", "Delete All");
            deleteAllButton = new JButton(resource);
            deleteAllButton.addActionListener(this);
         }
      }

      buildConstraints(constraints, 0, 0, 1, 1, 98, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(actionButtonPanel, constraints);
      actionPanel.add(actionButtonPanel);

      // Panel for table control and row indicator.

      tableControlIndicatorPanel = new JPanel();

      tableRowsIndicatorPanel = new JPanel();
      tableRowsIndicatorPanel.setBorder(BorderFactory.createLoweredBevelBorder());

      // Refresh Button
      refreshButton = new JButton(refreshIcon);
      refreshButton.setHorizontalAlignment(JButton.RIGHT);
      refreshButton.setMargin(new Insets(0, 0, 0, 0));
      refreshButton.setMnemonic((KeyEvent.VK_R));
      refreshButton.addActionListener(this);
      tableControlIndicatorPanel.add(refreshButton);

      // Row Indicator
      rowsLabel = new JLabel();
      setRowsLabel((tableRowStart + 1), (tableRowStart + tableRowLimit));
      tableRowsIndicatorPanel.add(rowsLabel);
      tableControlIndicatorPanel.add(tableRowsIndicatorPanel);

      rowControlPanel = new JPanel();
      rowControlPanel.setLayout(gridbag);

      // Previous Table Rows Button
      previousTableRowsButton = new JButton(previousTableRowsIcon);
      previousTableRowsButton.setHorizontalAlignment(JButton.CENTER);
      previousTableRowsButton.setMargin(new Insets(0, 0, 0, 0));
      previousTableRowsButton.setMnemonic(KeyEvent.VK_Z);
      previousTableRowsButton.addActionListener(this);

      buildConstraints(constraints, 0, 0, 1, 1, 100, 50);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(previousTableRowsButton, constraints);
      rowControlPanel.add(previousTableRowsButton);

      // Next Table Rows Button
      nextTableRowsButton = new JButton(nextTableRowsIcon);
      nextTableRowsButton.setHorizontalAlignment(JButton.CENTER);
      nextTableRowsButton.setMargin(new Insets(0, 0, 0, 0));
      nextTableRowsButton.setMnemonic(KeyEvent.VK_X);
      nextTableRowsButton.addActionListener(this);

      buildConstraints(constraints, 0, 1, 1, 1, 100, 50);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(nextTableRowsButton, constraints);
      rowControlPanel.add(nextTableRowsButton);

      tableControlIndicatorPanel.add(rowControlPanel);

      buildConstraints(constraints, 1, 0, 1, 1, 2, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.EAST;
      gridbag.setConstraints(nextTableRowsButton, constraints);
      actionPanel.add(tableControlIndicatorPanel);

      add(actionPanel, BorderLayout.SOUTH);
   }
   
   //==============================================================
   // ActionEvent Listener method for detecting the inputs from
   // the panel and directing to the appropriate routine via
   // a thread executing executeActions().
   //==============================================================

   public void actionPerformed(ActionEvent evt)
   {
      // Make sure we only allowing processing when the
      // thread is not already active.
      if (!busyProcessing && !settingState)
      {
         panelSource = evt.getSource();
         // System.out.println(panelSource);

         if (panelSource instanceof JButton || panelSource instanceof JRadioButton
             || panelSource instanceof JComboBox || panelSource instanceof JMenuItem)
         {
            // listTable Popup Menu Actions
            if (panelSource instanceof JMenuItem)
            {
               String actionCommand = ((JMenuItem) panelSource).getActionCommand();
               // System.out.println(actionCommand);

               if (actionCommand.equals("Select Fields"))
                  selectTableFields();
               else if (actionCommand.equals("View"))
                  viewButton.doClick();
               else if (actionCommand.equals("Add"))
                  addButton.doClick();
               else if (actionCommand.equals("Edit"))
                  editButton.doClick();
               else if (actionCommand.equals("Delete"))
                  deleteButton.doClick();
               else if (actionCommand.equals("Delete All"))
                  deleteAllButton.doClick();
               else if (actionCommand.equals("Select All"))
                  listTable.selectAll();
               else if (actionCommand.equals("DeSelect All"))
                  listTable.clearSelection();
               // Copy
               else if (actionCommand.equals((String) TransferHandler.getCopyAction().getValue(Action.NAME)))
               {
                  Action a = listTable.getActionMap().get(actionCommand);
                  if (a != null)
                     a.actionPerformed(new ActionEvent(listTable, ActionEvent.ACTION_PERFORMED, null));
               }
               // Paste
               else if (actionCommand.equals((String) TransferHandler.getPasteAction().getValue(Action.NAME)))
                  pasteClipboardContents();
               else if (actionCommand.equals("Save As Image"))
               {
                  listTable.clearSelection();
                  tableScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
                  tableScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
                  
                  ImageUtil pieImageUtil = new ImageUtil(tableScrollPane, lastSaveDirectory, "png");
                  lastSaveDirectory = pieImageUtil.getLastSaveDiretory();
                  
                  tableScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                  tableScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
               }
               // ?
               else
               {
                  // System.out.println(actionCommand);
               }
            }
            // Other Components
            else
            {
               if ((panelSource instanceof JButton && ((JButton) panelSource).isShowing())
                   || (panelSource instanceof JButton && panelSource == deleteAllButton)
                   || panelSource instanceof JRadioButton || panelSource instanceof JComboBox)
               {
                  // History Action
                  if (panelSource == previousStateButton || panelSource == nextStateButton)
                  {   
                     executeHistoryAction();
                  }
                  // Search ComboBox Action
                  else if (panelSource instanceof JComboBox && panelSource == searchComboBox)
                  {
                     searchTextField.grabFocus();
                     searchTextField.setCaretPosition(searchTextField.getText().length());
                  }
                  // Activate Advanced Sort/Search Form.
                  else if (panelSource instanceof JButton && panelSource == activateAdvancedSortSearchButton)
                  {
                     if (advancedSortSearchFrame == null)
                     {
                        createAdvancedSortSearchFrame();
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
                  // Activate Update Form.
                  else if (panelSource instanceof JButton && panelSource == activateUpdateButton)
                  {
                     if (updateFrame == null)
                     {
                        createUpdateFrame();
                        updateFrame.setVisible(true);
                     }
                     else
                     {
                        if (updateFrame.isShowing())
                           updateFrame.setVisible(false);
                        else
                           updateFrame.setVisible(true);
                     }
                  }
                  // View Form Close Button Action
                  else if (panelSource instanceof JButton && panelSource == closeViewButton)
                  {
                     toggleCenterPanel();
                     tableViewForm.clearBlobBytesHashMap();
                  }
                  else
                  {
                     // Just save the current scroll window so can restore.
                     selectedRow = listTable.getSelectedRow();
                     horizontalScrollBarPosition = tableScrollPane.getHorizontalScrollBar().getValue();
                     verticalScrollBarPosition = tableScrollPane.getVerticalScrollBar().getValue();

                     // Don't save history state for these actions.

                     if (panelSource instanceof JButton)
                     {
                        if (panelSource == addButton || panelSource == editButton
                            || panelSource == deleteButton || panelSource == deleteAllButton
                            || panelSource == refreshButton || panelSource == previousTableRowsButton
                            || panelSource == nextTableRowsButton)
                           historyAction = false;
                     }
                     
                     // Process the request.
                     
                     DBTablesPanel.startStatusTimer();
                     busyProcessing = true;

                     Thread actionThread = new Thread(new Runnable()
                     {
                        public void run()
                        {
                           executeActions();
                           DBTablesPanel.stopStatusTimer();
                           busyProcessing = false;
                           historyAction = true;
                        }
                     }, "TableTabPanel.actionThread");
                     actionThread.start();
                  }
               }
               else
               {
                  // Catches the dummy disposeButton, that occurs
                  // when the TableEntryForm or the UpdateForm
                  // fires the updateButton. Reload and resize
                  // list table & columns.
                  
                  historyAction = false;
                  setTableHeadings(getCurrentTableHeadings());

                  // Restore scrollpane window.
                  if (selectedRow != -1)
                     listTable.changeSelection(selectedRow, 0, false, false);
                  if (horizontalScrollBarPosition != -1)
                     tableScrollPane.getHorizontalScrollBar().setValue(horizontalScrollBarPosition);
                  if (verticalScrollBarPosition != -1)
                     tableScrollPane.getVerticalScrollBar().setValue(verticalScrollBarPosition);
                  
                  historyAction = true;
               }
            }
         }
      }
   }
   
   //==============================================================
   // Class method to select the summary table fields directly.
   //==============================================================

   private void selectTableFields()
   {
      // Method Instances
      TableFieldSelectionPreferencesPanel tableFieldPreferences;
      String resource, resourceOK, resourceCancel;
      InputDialog selectFieldsDialog;
      
      resource = resourceBundle.getResourceString("TableTabPanel.dialogtitle.SelectSummaryTableFields",
                                                   "Set Summary Table Fields");
      resourceOK = resourceBundle.getResourceString("TableTabPanel.dialogbutton.OK", "OK");
      resourceCancel = resourceBundle.getResourceString("TableTabPanel.dialogbutton.Cancel", "Cancel");
       
      tableFieldPreferences = new TableFieldSelectionPreferencesPanel(this, resourceBundle);
      Object[] content = {tableFieldPreferences};
      
      selectFieldsDialog = new InputDialog(null, resource, resourceOK, resourceCancel,
                                                  content, null);
      selectFieldsDialog.pack();
      selectFieldsDialog.center();
      selectFieldsDialog.setVisible(true);
      
      if (selectFieldsDialog.isActionResult())
         tableFieldPreferences.updatePreferences();
   }
   
   //==============================================================
   // Class method to control the history indexing and load table
   // accordingly.
   //==============================================================

   private void executeHistoryAction()
   {
      // Method Instances.
      String state;
      
      // Previouse History Action
      if (panelSource == previousStateButton)
      {
         // Decrement and check lower bound.
         stateHistoryIndex--;
         if (stateHistoryIndex <= 0)
         {
            stateHistoryIndex = 0;
            previousStateButton.setEnabled(false);
         }
      }
      
      // Next History Action
      else if (panelSource == nextStateButton)
      {
         // Increment and check upper bound.
         stateHistoryIndex++;
         if (stateHistoryIndex > (stateHistoryLimit - 1))
         {
            stateHistoryIndex = (stateHistoryLimit - 1);
            nextStateButton.setEnabled(false);
         }
      }
      
      // Check Overall movement of index against
      // history buttons and bound as needed.
      if (stateHistoryIndex == 0)
         previousStateButton.setEnabled(false);
      else
         previousStateButton.setEnabled(true);
      
      if (stateHistoryIndex < (stateHistory.size() - 1))
         nextStateButton.setEnabled(true);
      else
         nextStateButton.setEnabled(false);
      
      // Reload the table based on the history selection.
      DBTablesPanel.startStatusTimer();
      historyAction = false;
      
      state = MyJSQLView_Utils.stateConvert((stateHistory.get(stateHistoryIndex)).getBytes(), true);
      setState(state);
      setTableHeadings(getCurrentTableHeadings());
      
      historyAction = true;
      DBTablesPanel.stopStatusTimer();
   }

   //==============================================================
   // Class method to process the action events detected by the
   // panel via the thread in the actionPerformed method.
   //==============================================================

   private void executeActions()
   {
      // Method Instances.
      Object id, columnName;
      int primaryKeyColumn = 0;

      // Button Actions
      if (panelSource instanceof JButton)
      {
         Connection work_dbConnection = ConnectionManager.getConnection("TableTabPanel actionPerformed()");

         if (work_dbConnection == null)
            return;
         
         // Search Action.
         else if (panelSource == searchButton || panelSource == clearSearchTextFieldButton)
         {
            advancedSortSearch = false;
            tableRowStart = 0;
            if (panelSource == clearSearchTextFieldButton)
               searchTextField.setText("");

            loadTable(work_dbConnection);
            tableModel.setValues(tableData);
            tableScrollPane.getVerticalScrollBar().setValue(0);
            centerCardLayout.show(centerPanel, sqlTable);
            setRowsLabel((tableRowStart + 1), (tableRowStart + tableRowLimit));
         }

         // Advanced Sort/Search Apply Action.
         else if (panelSource == advSortSearchApplyButton)
         {
            advancedSortSearch = true;
            tableRowStart = 0;
            loadTable(work_dbConnection);
            tableModel.setValues(tableData);
            tableScrollPane.getVerticalScrollBar().setValue(0);
            centerCardLayout.show(centerPanel, sqlTable);
            setRowsLabel((tableRowStart + 1), (tableRowStart + tableRowLimit));
         }

         // Previous Table Rows Action.
         else if (panelSource == previousTableRowsButton)
         {
            tableRowStart -= tableRowLimit;

            if (tableRowStart <= 0)
               tableRowStart = 0;

            loadTable(work_dbConnection);
            tableModel.setValues(tableData);
            tableScrollPane.getVerticalScrollBar().setValue(0);
            centerCardLayout.show(centerPanel, sqlTable);
            setRowsLabel((tableRowStart + 1), (tableRowStart + tableRowLimit));
         }

         // View Action.
         else if (panelSource == viewButton)
         {
            // Make sure an entry is selected.
            if (selectedRow != -1)
            {
               // Just insures the selected row has data.
               id = listTable.getValueAt(selectedRow, primaryKeyColumn);
               if (id != null)
               {
                  try
                  {
                     viewSelectedItem(work_dbConnection, selectedRow);
                  }
                  catch (SQLException e)
                  {
                     ConnectionManager.displaySQLErrors(e, "TableTabPanel executeActions()");
                  }
                  centerCardLayout.show(centerPanel, sqlTable + " Form");
                  tableViewForm.setFocus();
               }
            }
         }

         // Add Entry Action.
         else if (panelSource == addButton)
         {
            addItem(work_dbConnection);
         }

         // Edit Entry Action.
         else if (panelSource == editButton)
         {
            // Make sure an entry is selected.
            if (selectedRow != -1)
            {
               // Find the primary key column.
               for (int i = 0; i < listTable.getColumnCount(); i++)
               {
                  // System.out.println(i + " " +
                  // listTable.getColumnName(i));
                  if (columnNamesHashMap.get(listTable.getColumnName(i)).equals(primaryKeys.get(0)))
                     primaryKeyColumn = i;
               }

               id = listTable.getValueAt(selectedRow, primaryKeyColumn);
               columnName = columnNamesHashMap.get(listTable.getColumnName(primaryKeyColumn));
               if (id != null)
               {
                  try
                  {
                     editSelectedItem(work_dbConnection, selectedRow, columnName, id);
                  }
                  catch (SQLException e)
                  {
                     ConnectionManager.displaySQLErrors(e, "TableTabPanel executeActions()");
                  }
               }
            }
         }

         // Update Form Find Action.
         else if (panelSource == updateFormFindButton)
         {
            advancedSortSearch = true;
            tableRowStart = 0;
            if (advancedSortSearchFrame == null)
               createAdvancedSortSearchFrame();
            advancedSortSearchFrame.setKeyComponentsState(updateFrame.getKeyComponentsState());
            loadTable(work_dbConnection);
            tableModel.setValues(tableData);
            tableScrollPane.getVerticalScrollBar().setValue(0);
            centerCardLayout.show(centerPanel, sqlTable);
            setRowsLabel((tableRowStart + 1), (tableRowStart + tableRowLimit));
         }

         // Delete Action.
         else if (panelSource == deleteButton)
         {
            // Make sure an entry is selected.
            if (selectedRow != -1)
            {
               // Just insures the selected row has data.
               id = listTable.getValueAt(selectedRow, primaryKeyColumn);

               if (id != null)
               {
                  try
                  {
                     deleteSelectedItems(work_dbConnection);
                  }
                  catch (SQLException e)
                  {
                     ConnectionManager.displaySQLErrors(e, "TableTabPanel executeActions()");
                  }
                  loadTable(work_dbConnection);
                  tableModel.setValues(tableData);
                  centerCardLayout.show(centerPanel, sqlTable);
               }
            }
         }

         // Delete All Action.
         else if (panelSource == deleteAllButton)
         {
            try
            {
               deleteAllItems(work_dbConnection);
            }
            catch (SQLException e)
            {
               ConnectionManager.displaySQLErrors(e, "TableTabPanel executeActions()");
            }
            loadTable(work_dbConnection);
            tableModel.setValues(tableData);
            centerCardLayout.show(centerPanel, sqlTable);
         }

         // Next Table Rows Action.
         else if (panelSource == nextTableRowsButton)
         {
            tableRowStart += tableRowLimit;
            loadTable(work_dbConnection);
            tableModel.setValues(tableData);
            tableScrollPane.getVerticalScrollBar().setValue(0);
            centerCardLayout.show(centerPanel, sqlTable);
            setRowsLabel((tableRowStart + 1), (tableRowStart + tableRowLimit));
         }

         // Previous View Form Button & Next View Form Button Actions
         else if (panelSource == previousViewButton | panelSource == nextViewButton)
         {
            if (selectedRow != -1)
            {
               int oldRow, rowPointer;

               // Save row pointer
               oldRow = Integer.parseInt(selectedRow + "");

               if (panelSource == previousViewButton)
                  rowPointer = Integer.parseInt(selectedRow + "") - 1;
               else
                  rowPointer = Integer.parseInt(selectedRow + "") + 1;

               if (rowPointer < 0 | rowPointer > (tableRowLimit - 1))
                  rowPointer = oldRow;

               // Change the row pointer then collect table data.
               // Otherwise maintain old row pointer.

               listTable.changeSelection(rowPointer, 0, false, false);
               selectedRow = listTable.getSelectedRow();
               id = listTable.getValueAt(selectedRow, 0);
               if (id != null)
               {
                  tableViewForm.clearBlobBytesHashMap();
                  try
                  {
                     viewSelectedItem(work_dbConnection, selectedRow);
                  }
                  catch (SQLException e)
                  {
                     ConnectionManager.displaySQLErrors(e, "TableTabPanel executeActions()");
                  }
                  centerCardLayout.show(centerPanel, sqlTable + " Form");
                  tableViewForm.setFocus();
               }
               else
                  listTable.changeSelection(oldRow, 0, false, false);
            }
         }

         // Refresh Button Action
         else if (panelSource == refreshButton)
         {
            loadTable(work_dbConnection);
            tableModel.setValues(tableData);
            tableScrollPane.getVerticalScrollBar().setValue(0);
            centerCardLayout.show(centerPanel, sqlTable);
         }

         ConnectionManager.closeConnection(work_dbConnection, "TableTabPanel actionPerformed()");
      }

      // JRadio Button Actions
      else if (panelSource instanceof JRadioButton)
      {
         if (panelSource == ascSortRadioButton || panelSource == descSortRadioButton)
         {
            Connection work_dbConnection = ConnectionManager.getConnection(
               "TableTabPanel actionPerformed()");

            if (work_dbConnection == null)
               return;

            if (ascSortRadioButton.isSelected())
               ascDescString = "ASC";
            else
               ascDescString = "DESC";

            advancedSortSearch = false;
            loadTable(work_dbConnection);
            tableModel.setValues(tableData);
            tableScrollPane.getVerticalScrollBar().setValue(0);
            centerCardLayout.show(centerPanel, sqlTable);

            ConnectionManager.closeConnection(work_dbConnection, "TableTabPanel actionPerformed()");
         }
      }

      // Combobox Actions
      else if (panelSource instanceof JComboBox)
      {
         // Sort ComboBox Action
         if (panelSource == sortComboBox)
         {
            Connection work_dbConnection = ConnectionManager.getConnection(
               "TableTabPanel actionPerformed()");

            if (work_dbConnection == null)
               return;
            
            // Just insure have column field selected.
            Object selectedSortItem = sortComboBox.getSelectedItem();
            
            if (!columnNamesHashMap.containsKey(selectedSortItem))
            {
               sortComboBox.removeActionListener(this);
               sortComboBox.setSelectedIndex(1);
               sortComboBox.addActionListener(this);
            }
            advancedSortSearch = false;
            loadTable(work_dbConnection);
            tableModel.setValues(tableData);
            tableScrollPane.getVerticalScrollBar().setValue(0);
            centerCardLayout.show(centerPanel, sqlTable);

            ConnectionManager.closeConnection(work_dbConnection, "TableTabPanel actionPerformed()");
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
      // System.out.println(keyChar);

      // Fire the search button as required.
      if (keyChar == KeyEvent.VK_ENTER)
         searchButton.doClick();
   }
   
   //==============================================================
   // TableColumnModelEvent Listener method for detecting column
   // additions to full fill TableColumnModelListener Interface
   // requirements.
   //==============================================================
   
   public void columnAdded(TableColumnModelEvent evt)
   {
      // Do Nothing
   }
   
   //==============================================================
   // TableColumnModelEvent Listener method for detecting column
   // margin changes to full fill TableColumnModelListener Interface
   // requirements.
   //==============================================================
   
   public void columnMarginChanged(ChangeEvent evt)
   {
      // Do Nothing 
   }
   
   //==============================================================
   // TableColumnModelEvent Listener method for detecting moves
   // in columns so that the table field names sequence can be
   // reconstituted.
   //==============================================================
   
   public void columnMoved(TableColumnModelEvent evt)
   {
      /* Test Procedure.
      for (int i = 0; i < listTable.getColumnCount(); i++)
      {
         int index = listTable.getColumnModel().getColumn(i).getModelIndex();
         System.out.print(index + ",");
      }
      */
      
      if (evt.getFromIndex() != evt.getToIndex())
      {
         sqlTableFieldsString = "";
         
         for (int j = 0; j < listTable.getColumnCount(); j++)
            sqlTableFieldsString += identifierQuoteString + columnNamesHashMap.get(listTable.getColumnName(j))
                                       + identifierQuoteString + ", ";
         
         if (!sqlTableFieldsString.equals(""))
            sqlTableFieldsString = sqlTableFieldsString.substring(0, sqlTableFieldsString.length() - 2);
      }
   }
   
   //==============================================================
   // TableColumnModelEvent Listener method for detecting column
   // removals to full fill TableColumnModelListener Interface
   // requirements.
   //==============================================================
   
   public void columnRemoved(TableColumnModelEvent evt)
   {
      // Do Nothing 
   }
   
   //==============================================================
   // TableColumnModelEvent Listener method for detecting column
   // selection changes to full fill TableColumnModelListener
   // Interface requirements.
   //==============================================================
   
   public void columnSelectionChanged(ListSelectionEvent evt)
   {
      // Do Nothing 
   }
   
   //==============================================================
   // Class method to obtain the column names from the table.
   // 
   //                          ******
   // YOU MUST OVERIDE THIS METHOD IN EACH DATABASE TableTabPanel.
   //
   //==============================================================
   /*
   public boolean getColumnNames(Connection dbConnection)
   {
      // OVERIDE THIS METHOD!
      return false;
   }
   */

   //==============================================================
   // Class method to load the current table's data.
   //
   //                          ******
   // YOU MUST OVERIDE THIS METHOD IN EACH DATABASE TableTabPanel.
   //
   //==============================================================
   /*
   public boolean loadTable(Connection dbConnection)
   {
      // OVERIDE THIS METHOD!
      return false;
   }
   */

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
      
      resource = resourceBundle.getResourceString("TableTabPanel.popup.SelectFields", "Select Fields");
      menuItem = menuItem(resource, "Select Fields");
      summaryTablePopupMenu.add(menuItem);
      
      summaryTablePopupMenu.addSeparator();

      // No keys than cannot perform these operations.
      if (!primaryKeys.isEmpty())
      {
         resource = resourceBundle.getResourceString("TableTabPanel.button.View", "View");
         menuItem = menuItem(resource, "View");
         summaryTablePopupMenu.add(menuItem);

         if (!viewOnly)
         {
            resource = resourceBundle.getResourceString("TableTabPanel.button.Add", "Add");
            menuItem = menuItem(resource, "Add");
            summaryTablePopupMenu.add(menuItem);

            resource = resourceBundle.getResourceString("TableTabPanel.button.Edit", "Edit");
            menuItem = menuItem(resource, "Edit");
            summaryTablePopupMenu.add(menuItem);

            resource = resourceBundle.getResourceString("TableTabPanel.button.Delete", "Delete");
            menuItem = menuItem(resource, "Delete");
            summaryTablePopupMenu.add(menuItem);

            summaryTablePopupMenu.addSeparator();

            resource = resourceBundle.getResourceString("TableTabPanel.button.DeleteAll", "Delete All");
            menuItem = menuItem(resource, "Delete All");
            summaryTablePopupMenu.add(menuItem);
         }
         summaryTablePopupMenu.addSeparator();
      }

      // Summary Table select actions.

      resource = resourceBundle.getResourceString("TableTabPanel.popup.SelectAll", "Select All");
      menuItem = menuItem(resource, "Select All");
      summaryTablePopupMenu.add(menuItem);

      resource = resourceBundle.getResourceString("TableTabPanel.popup.DeSelectAll", "DeSelect All");
      menuItem = menuItem(resource, "DeSelect All");
      summaryTablePopupMenu.add(menuItem);

      // Summary Table copy/paste actions

      summaryTablePopupMenu.addSeparator();

      resource = resourceBundle.getResourceString("TableTabPanel.popup.Copy", "Copy");
      if (resource.equals(""))
         menuItem = new JMenuItem("Copy");
      else
         menuItem = new JMenuItem(resource);
      menuItem.setActionCommand((String) TransferHandler.getCopyAction().getValue(Action.NAME));
      menuItem.setMnemonic(KeyEvent.VK_C);
      menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
      menuItem.addActionListener(this);
      summaryTablePopupMenu.add(menuItem);

      // No keys then cannot perform paste.
      if (!primaryKeys.isEmpty() && !viewOnly)
      {
         resource = resourceBundle.getResourceString("TableTabPanel.popup.Paste", "Paste");
         menuItem = new JMenuItem(resource);
         menuItem.setActionCommand((String) TransferHandler.getPasteAction().getValue(Action.NAME));
         menuItem.setMnemonic(KeyEvent.VK_V);
         menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
         menuItem.addActionListener(this);
         summaryTablePopupMenu.add(menuItem);
      }
      
      // Summary Table Save as Image, PNG.
      
      summaryTablePopupMenu.addSeparator();
      
      resource = resourceBundle.getResourceString("TableTabPanel.popup.SaveAsImage",
                                                  "Save As Image");
      menuItem = menuItem(resource, "Save As Image");
      summaryTablePopupMenu.add(menuItem);
      

      summaryTablePopupListener = new MyJSQLView_MouseAdapter(summaryTablePopupMenu);
   }

   //==============================================================
   // Class method to create the advancedSortSearchFrame.
   //==============================================================

   public void createAdvancedSortSearchFrame()
   {
      advancedSortSearchFrame = new AdvancedSortSearchForm(schemaTableName, resourceBundle,
                                                           columnNamesHashMap, columnClassHashMap,
                                                           columnTypeHashMap, comboBoxFields);
      advSortSearchApplyButton = advancedSortSearchFrame.getApplyButton();
      advSortSearchApplyButton.addActionListener(this);

      advancedSortSearchFrame.pack();
      advancedSortSearchFrame.center();
   }

   //==============================================================
   // Class method to create the updateFrame.
   //==============================================================

   public void createUpdateFrame()
   {
      updateFrame = new UpdateForm(schemaTableName, resourceBundle, columnNamesHashMap,
                                   columnClassHashMap, columnTypeHashMap, columnSizeHashMap,
                                   comboBoxFields);
      updateFormFindButton = updateFrame.getFindButton();
      updateFormFindButton.addActionListener(this);
      updateFrame.getDisposeButton().addActionListener(this);

      updateFrame.pack();
      updateFrame.center();
   }

   //==============================================================
   // Helper Method, for creating JMenuItems.
   //==============================================================

   private JMenuItem menuItem(String label, String actionLabel)
   {
      JMenuItem item = new JMenuItem(label);
      item.addActionListener(this);
      item.setActionCommand(actionLabel);
      return item;
   }

   //=============================================================
   // Class method for displaying the SQL Database standard date
   // format from a java.sql.date string. YYYY-MM-dd to the selected
   // MyJSQLView general date view preferences.
   //=============================================================
   
   protected String displayMyDateString(String javaDateString)
   {
      return MyJSQLView_Utils.convertDBDateString_To_ViewDateString(javaDateString,
                                                  DBTablesPanel.getGeneralDBProperties().getViewDateFormat());
   }

   //==============================================================
   // Class method to parse the MyJSQLView table's column name
   // fields. The parsed strings creates a more user friendly
   // format that will be displayed in the sort/search ComboBoxes,
   // summary table, and TableEntryForm.
   //==============================================================

   public String parseColumnNameField(String columnString)
   {
      // Method Instances.
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

   private void buildConstraints(GridBagConstraints gbc, int gx, int gy, int gw, int gh, double wx, double wy)
   {
      gbc.gridx = gx;
      gbc.gridy = gy;
      gbc.gridwidth = gw;
      gbc.gridheight = gh;
      gbc.weightx = wx;
      gbc.weighty = wy;
   }
   
   //==============================================================
   // Class Method for saving the state history.
   //==============================================================

   public void saveHistory()
   {  
      // Save the state into the queue.
      if (stateHistory.size() > stateHistoryLimit)
         stateHistory.removeFirst();
      
      stateHistory.add(getState());
      stateHistoryIndex++;
      
      // Check the previous/next button history.
      if (stateHistoryIndex == 0)
         previousStateButton.setEnabled(false);
      else
         previousStateButton.setEnabled(true);
      
      if (stateHistoryIndex < (stateHistory.size() - 1))
         nextStateButton.setEnabled(true);
      else
         nextStateButton.setEnabled(false);
   }
   

   //==============================================================
   // Class method to view the current selected item in the table.
   //
   //                          ******
   // YOU MUST OVERIDE THIS METHOD IN EACH DATABASE TableTabPanel.
   //
   //==============================================================
   /*
   public void viewSelectedItem(Connection dbConnection, int rowToView)
   {
      // OVERIDE THIS METHOD!
   }
   */

   //==============================================================
   // Class method to add a table entry.
   //
   //                          ******
   // YOU MUST OVERIDE THIS METHOD IN EACH DATABASE TableTabPanel.
   //
   //==============================================================
   /*
   public void addItem(Connection dbConnection)
   {
      // OVERIDE THIS METHOD!
   }
   */

   //==============================================================
   // Class method to edit the current selected item.
   //
   //                          ******
   // YOU MUST OVERIDE THIS METHOD IN EACH DATABASE TableTabPanel.
   //
   //==============================================================
   /*
   public void editSelectedItem(Connection dbConnection, int rowToEdit, Object columnName, Object id)
   {
      // OVERIDE THIS METHOD!
   }
   */

   //==============================================================
   // Class method to set up special field data that may be needed
   // by the add or edit ItemForm. Can be used for separate table
   // access to create combobox data. At this time used for creating
   // enum & boolean ComboBoxes and set Lists.
   //==============================================================
   
   protected void setSpecialFieldData(TableEntryForm fillForm, Connection dbConnection,
                                      Object currentColumnName, Object currentContentData)
                                      throws SQLException
   {
      // Method Instances
      Statement sqlStatement;
      ResultSet db_resultSet;

      // Setting up & Filling ComboBox Fields
      sqlStatement = null;
      db_resultSet = null;
      
      try
      {
         sqlStatement = dbConnection.createStatement();

         // Not Used in MyJSQLView
         // Your Table Field ComboBox Content
         if (((String) currentColumnName).equals("Your Table Field"))
         {
            db_resultSet = sqlStatement.executeQuery("SELECT YourTableField FROM YourTable");

            ArrayList<String> comboBoxList = new ArrayList <String>();
            
            while (db_resultSet.next())
               comboBoxList.add(db_resultSet.getString(1));
            fillForm.setComboBoxField(currentColumnName, comboBoxList, currentContentData);
         }

         // MyJSQLView Use
         // Enum ComboBox/Content & Set List/Content
         if (columnEnumHashMap.containsKey(currentColumnName)
             || columnSetHashMap.containsKey(currentColumnName))
         {
            ArrayList<String> comboBoxList = new ArrayList <String>();
            String listStrings;

            if (columnEnumHashMap.containsKey(currentColumnName))
            {
               listStrings = columnEnumHashMap.get(currentColumnName);

               // Special case of enum field Boolean single bit.
               if (listStrings.equals("tinyint(1)") || listStrings.toUpperCase().equals("BOOL")
                   || listStrings.toUpperCase().equals("BOOLEAN")
                   || listStrings.toUpperCase().equals("BIT"))
                  listStrings = "(\'TRUE\',\'FALSE\')";
            }
            else
               listStrings = columnSetHashMap.get(currentColumnName);

            // The format is ('a','b','c'), so just extracting
            // the indiviual elements.

            if (listStrings.indexOf("(") != -1 && listStrings.indexOf(")") != -1)
               listStrings = listStrings.substring(listStrings.indexOf("(") + 1, listStrings.lastIndexOf(")"));
            listStrings = listStrings.replaceAll(",", "");
            listStrings = listStrings.replaceAll("''", "'");
            String[] enumSetString = listStrings.split("\\'");

            for (int i = 1; i < enumSetString.length; i++)
            {
               comboBoxList.add(enumSetString[i]);
               // System.out.println(i + " " + enumSetString[i]);
            }

            // Set Add/Edit form fields
            if (columnEnumHashMap.containsKey(currentColumnName))
               fillForm.setComboBoxField(currentColumnName, comboBoxList, currentContentData);
            else
               fillForm.setSetFields(currentColumnName, comboBoxList, currentContentData);
         } 
      }
      catch (SQLException e)
      {
         ConnectionManager.displaySQLErrors(e, "TableTabPanel setSpecialFieldData()");
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
            ConnectionManager.displaySQLErrors(sqle, "TableTabPanel setSpecialFieldData()");
         }
         finally
         {
            if (sqlStatement != null)
               sqlStatement.close();
         }
      }
   }
   
   //==============================================================
   // Class method to delete table entry item(s) from the database.
   // Either single or multiple entries can be removed.
   //==============================================================

   public void deleteSelectedItems(Connection dbConnection) throws SQLException
   {
      // Method Instances
      StringBuffer sqlStatementString;
      Statement sqlStatement;
      int[] selectedRows;
      
      JLabel message;
      InputDialog deleteDialog;
      String dataSourceType, currentDB_ColumnName, currentColumnClass, currentColumnType;
      String  resourceMessage, resourceTitle, resourceCancel, resourceOK;
      Object currentContentData;
      int keyColumn = 0;

      // Obtain the selected rows to be deleted
      selectedRows = listTable.getSelectedRows();

      if (selectedRows.length != 0)
      {
         // Confirming really want to delete.
         resourceMessage = resourceBundle.getResourceString("TableTabPanel.message.DeleteItems",
                                                            "Delete Selected Items(s)?");
         message = new JLabel(resourceMessage, JLabel.CENTER);
         message.setFont(new Font("DIALOG", Font.BOLD, 14));
         message.setForeground(Color.RED);
         Object[] content = {message};
         
         resourceTitle = resourceBundle.getResourceString("TableTabPanel.dialogtitle.AlertDialog",
                                                          "Alert Dialog");
         resourceCancel = resourceBundle.getResourceString("TableTabPanel.dialogbutton.Cancel", "Cancel");
         resourceOK = resourceBundle.getResourceString("TableTabPanel.dialogbutton.OK", "OK");
         
         deleteDialog = new InputDialog(null, resourceTitle, resourceOK, resourceCancel,
                                        content, deleteDataIcon);
         deleteDialog.pack();
         deleteDialog.center();
         deleteDialog.setResizable(false);
         deleteDialog.setVisible(true);

         // Deleting
         if (deleteDialog.isActionResult())
         {
            // Connect to database to delete the selected row(s).
            sqlStatement = null;
            
            try
            {
               dbConnection.setAutoCommit(false);
               sqlStatement = dbConnection.createStatement();

               // HSQL, Oracle, & SQLite does not support.
               dataSourceType = ConnectionManager.getDataSourceType();
               
               if (dataSourceType.equals(ConnectionManager.MYSQL)
                   || dataSourceType.equals(ConnectionManager.MARIADB)
                   || dataSourceType.equals(ConnectionManager.POSTGRESQL))
                  sqlStatement.executeUpdate("BEGIN");

               // Begin the SQL statement(s) creation.
               sqlStatementString = new StringBuffer();
               sqlStatementString.append("DELETE FROM " + schemaTableName + " WHERE ");

               // Delete multiple rows if selected.
               for (int i = 0; i < selectedRows.length; i++)
               {
                  // Don't process empty rows.
                  if (listTable.getValueAt(selectedRows[i], 0) != null)
                  {
                     Iterator<String> keyIterator = primaryKeys.iterator();

                     // Find the key column, in clase it has been moved
                     // in the summary table, then obtain entry content.

                     while (keyIterator.hasNext())
                     {
                        currentDB_ColumnName = keyIterator.next();
                        currentColumnType = columnTypeHashMap.get(
                                     parseColumnNameField(currentDB_ColumnName));
                        currentColumnClass = columnClassHashMap.get(
                                     parseColumnNameField(currentDB_ColumnName));
                        
                        for (int j = 0; j < listTable.getColumnCount(); j++)
                           if (listTable.getColumnName(j).equals(parseColumnNameField(currentDB_ColumnName)))
                              keyColumn = j;

                        // Found now get key info.
                        currentContentData = listTable.getValueAt(selectedRows[i], keyColumn);

                        // Special case of blob/text key.
                        if (currentContentData instanceof BlobTextKey)
                        {
                           String keyString = ((BlobTextKey) currentContentData).getContent();
                           keyString = keyString.replaceAll("'", "''");

                           sqlStatementString.append(identifierQuoteString + currentDB_ColumnName
                                                     + identifierQuoteString + " LIKE '"
                                                     + keyString + "%' AND ");
                        }
                        // Normal keys.
                        else
                        {
                           // Escape single quotes.
                           if ((columnClassHashMap.get(parseColumnNameField(currentDB_ColumnName)))
                                 .indexOf("String") != -1)
                              currentContentData = ((String) currentContentData).replaceAll("'", "''");

                           // Reformat date keys.
                           
                           if (currentColumnType.equals("DATE"))
                           {
                              // MySQL, MariaDB, & Oracle Require Special Handling.
                              if (dataSourceType.equals(ConnectionManager.MYSQL)
                                  || dataSourceType.equals(ConnectionManager.MARIADB))
                              {
                                 sqlStatementString.append(identifierQuoteString + currentDB_ColumnName
                                                           + identifierQuoteString + "=STR_TO_DATE('"
                                                           + MyJSQLView_Utils.convertViewDateString_To_DBDateString(
                                                              currentContentData + "",
                                                              DBTablesPanel.getGeneralDBProperties().getViewDateFormat())
                                                           + "', '%Y-%m-%d') AND ");
                              }
                              else if (dataSourceType.equals(ConnectionManager.ORACLE))
                              {
                                 sqlStatementString.append(identifierQuoteString + currentDB_ColumnName
                                                           + identifierQuoteString + "=TO_DATE('"
                                                           + MyJSQLView_Utils.convertViewDateString_To_DBDateString(
                                                              currentContentData + "",
                                                              DBTablesPanel.getGeneralDBProperties().getViewDateFormat())
                                                           + "', 'YYYY-MM-dd') AND ");
                              }
                              else
                              {
                                 sqlStatementString.append(identifierQuoteString + currentDB_ColumnName
                                                           + identifierQuoteString + "='"
                                                           + MyJSQLView_Utils.convertViewDateString_To_DBDateString(
                                                              currentContentData + "",
                                                              DBTablesPanel.getGeneralDBProperties().getViewDateFormat())
                                                           + "' AND ");
                              }
                           }
                           else if (currentColumnType.equals("DATETIME"))
                           {
                              String dateString = (String) currentContentData;
                              
                              if (dateString.indexOf(" ") != -1)
                                 currentContentData = MyJSQLView_Utils.processDateFormatSearch(
                                    dateString.substring(0, dateString.indexOf(" ")))
                                    + dateString.substring(dateString.indexOf(" "));
                              else if (dateString.indexOf("-") != -1 || dateString.indexOf("/") != -1)
                                 currentContentData = MyJSQLView_Utils.processDateFormatSearch(dateString);
                                 
                              if (dataSourceType.equals(ConnectionManager.MSACCESS))
                                 sqlStatementString.append(identifierQuoteString + currentDB_ColumnName
                                                           + identifierQuoteString + "=#"
                                                           + currentContentData
                                                           + "# AND ");
                              else
                                 sqlStatementString.append(identifierQuoteString + currentDB_ColumnName
                                                           + identifierQuoteString + "='"
                                                           + currentContentData
                                                           + "' AND ");
                           }
                           else
                           {
                              // Character data gets single quotes for some databases,
                              // not numbers though.
                              
                              if ((dataSourceType.equals(ConnectionManager.MSACCESS)
                                    || dataSourceType.equals(ConnectionManager.DERBY)
                                    || dataSourceType.indexOf(ConnectionManager.HSQL) != -1)
                                  && currentColumnClass.toLowerCase().indexOf("string") == -1)
                              {
                                 sqlStatementString.append(identifierQuoteString + currentDB_ColumnName
                                                           + identifierQuoteString + "="
                                                           + currentContentData + " AND ");
                              }
                              else
                                 sqlStatementString.append(identifierQuoteString + currentDB_ColumnName
                                                           + identifierQuoteString + "='"
                                                           + currentContentData + "' AND ");
                           }
                        }
                     }
                     sqlStatementString.delete((sqlStatementString.length() - 5), sqlStatementString.length());
                     // System.out.println(sqlStatementString);
                     sqlStatement.executeUpdate(sqlStatementString.toString());
                     sqlStatementString.delete(0, sqlStatementString.length());
                     sqlStatementString.append("DELETE FROM " + schemaTableName + " WHERE ");
                  }
               }
               dbConnection.commit();
            }
            catch (SQLException e)
            {
               ConnectionManager.displaySQLErrors(e, "TableTabPanel deleteSelectedItems()");
               try
               {
                  dbConnection.rollback();
               }
               catch (SQLException error)
               {
                  ConnectionManager.displaySQLErrors(e,
                                     "TableTabPanel deleteSelectedItems() rollback failed");
               }
            }
            finally
            {
               if (sqlStatement != null)
                  sqlStatement.close();
               dbConnection.setAutoCommit(true);
            }
         }
         deleteDialog.dispose();
      }
   }

   //==============================================================
   // Class method to delete all table entry items.
   //==============================================================

   public void deleteAllItems(Connection dbConnection) throws SQLException
   {
      // Method Instances
      String sqlStatementString;
      Statement sqlStatement;
      
      JLabel message;
      InputDialog deleteAllDialog;
      JCheckBox confirmCheckbox;
      String dataSourceType;
      String resource, resourceMessage;
      String resourceTitle, resourceCancel, resourceOK;

      // Confirming really want to delete all rows.
      resourceMessage = resourceBundle.getResourceString("TableTabPanel.message.DeleteAllItems",
                                                         "Delete All Table Rows!");
      message = new JLabel(resourceMessage, JLabel.CENTER);
      message.setFont(new Font("DIALOG", Font.BOLD, 14));
      message.setForeground(Color.RED);
      
      resource = resourceBundle.getResourceString("TableTabPanel.checkbox.ConfirmDeletion",
                                                  "Confirm Deletion");
      confirmCheckbox = new JCheckBox(resource, false);
      
      Object[] content = {message, confirmCheckbox};
      
      resourceTitle = resourceBundle.getResourceString("TableTabPanel.dialogtitle.AlertDialog",
                                                       "Alert Dialog");
      resourceCancel = resourceBundle.getResourceString("TableTabPanel.dialogbutton.Cancel", "Cancel");
      resourceOK = resourceBundle.getResourceString("TableTabPanel.dialogbutton.OK", "OK");
     
      deleteAllDialog = new InputDialog(null, resourceTitle, resourceOK, resourceCancel,
                                        content, deleteDataIcon);
      deleteAllDialog.pack();
      deleteAllDialog.center();
      deleteAllDialog.setResizable(false);
      deleteAllDialog.setVisible(true);

      // Deleting
      if (deleteAllDialog.isActionResult() && confirmCheckbox.isSelected())
      {
         // Connect to database to delete the selected row(s).
         sqlStatement = null;
         
         try
         {
            dbConnection.setAutoCommit(false);
            sqlStatement = dbConnection.createStatement();

            dataSourceType = ConnectionManager.getDataSourceType();
         
            // HSQL, SQLite & Oracle does not support.
            if (dataSourceType.equals(ConnectionManager.MYSQL)
                || dataSourceType.equals(ConnectionManager.MARIADB)
                || dataSourceType.equals(ConnectionManager.POSTGRESQL))
               sqlStatement.executeUpdate("BEGIN");

            // SQL statement creation.
            if (dataSourceType.indexOf(ConnectionManager.HSQL) != -1)
               sqlStatementString = "TRUNCATE TABLE " + schemaTableName + " RESTART IDENTITY "
                                    + " AND COMMIT";
            else
               sqlStatementString = "DELETE FROM " + schemaTableName;

            // System.out.println(sqlStatementString);
            sqlStatement.executeUpdate(sqlStatementString);

            dbConnection.commit();
         }
         catch (SQLException e)
         {
            ConnectionManager.displaySQLErrors(e, "TableTabPanel deleteAllItems()");
            try
            {
               dbConnection.rollback(); 
            }
            catch (SQLException error)
            {
               ConnectionManager.displaySQLErrors(error, "TableTabPanel deleteAllItems() rollback failed");
            }
         }
         finally
         {
            if (sqlStatement != null)
               sqlStatement.close();
            dbConnection.setAutoCommit(true);
         }
      }
      deleteAllDialog.dispose();
   }

   //==============================================================
   // Class method to paste the clipboard contents into the selected
   // table.
   //==============================================================

   public void pasteClipboardContents()
   {
      // Method Instances
      Toolkit systemToolKit;
      String tempDataFileName;
      Object clipboardContents;
      StringBuffer tableHeadingsString;
      String errorString;
      
      JLabel message;
      InputDialog insertUpdateDialog;
      JRadioButton insertRadioButton, updateRadioButton;
      String resource, resourceMessage;
      String resourceTitle, resourceCancel, resourceOK;

      // Setting up.
      systemToolKit = Toolkit.getDefaultToolkit();
      tempDataFileName = System.getProperty("user.home") + System.getProperty("file.separator")
                         + "tmpdata.txt";
      tableHeadingsString = new StringBuffer();
      errorString = "";

      try
      {
         // Create the table headings.

         for (int i = 0; i < listTable.getColumnCount(); i++)
            tableHeadingsString.append(columnNamesHashMap.get(listTable.getColumnName(i))
                                   + (DBTablesPanel.getDataImportProperties()).getDataDelimiter());
         tableHeadingsString.append("\n");

         // Combine the table headings and clipboard contents
         // and then write to a temporary file.

         clipboardContents = (systemToolKit.getSystemClipboard()).getContents(this)
                              .getTransferData(DataFlavor.stringFlavor);

         if (clipboardContents != null)
            WriteDataFile.mainWriteDataString(tempDataFileName, (tableHeadingsString.toString()
                                              + (String) clipboardContents).getBytes(),
                                              false);
         else
            return;

         // Create a dialog to warn the user of possible data
         // overwriting and selecting of Insert or Update.

         // Insert/Update Content.
         ButtonGroup insertUpdateOptionsGroup = new ButtonGroup();
         
         resource = resourceBundle.getResourceString("TableTabPanel.radiobutton.Insert", "Insert");
         insertRadioButton = new JRadioButton(resource, true);
         insertUpdateOptionsGroup.add(insertRadioButton);
         
         resource = resourceBundle.getResourceString("TableTabPanel.radiobutton.Update", "Update");
         updateRadioButton = new JRadioButton(resource, false);
         insertUpdateOptionsGroup.add(updateRadioButton);

         resourceMessage = resourceBundle.getResourceString("TableTabPanel.message.WarningLossData",
                                                            "Warning Import May Cause Loss of Data!");
         message = new JLabel(resourceMessage, JLabel.CENTER);
         message.setForeground(Color.RED);
         
         Object content[] = {message, insertRadioButton, updateRadioButton};
         
         resourceTitle = resourceBundle.getResourceString("TableTabPanel.dialogtitle.InsertOrUpdate",
                                                          "Insert Or Update");
         resourceCancel = resourceBundle.getResourceString("TableTabPanel.dialogbutton.Cancel", "Cancel");
         resourceOK = resourceBundle.getResourceString("TableTabPanel.dialogbutton.OK", "OK");
         
         insertUpdateDialog = new InputDialog(null, resourceTitle, resourceOK, resourceCancel,
                                              content, deleteDataIcon);
         
         insertUpdateDialog.pack();
         insertUpdateDialog.setResizable(false);
         insertUpdateDialog.center();
         insertUpdateDialog.setVisible(true);

         // If ok proceed to paste data.
         if (insertUpdateDialog.isActionResult())
         {
            String csvOption;

            if (insertRadioButton.isSelected())
               csvOption = insertRadioButton.getText();
            else
               csvOption = updateRadioButton.getText();
            
            Thread csvDataImportThread = new Thread(new CSVDataImportThread(
               tempDataFileName, csvOption, true), "CSVDataImportThread");
            
            csvDataImportThread.start();
         }
         insertUpdateDialog.dispose();
      }
      catch (IllegalStateException ise)
      {
         resourceMessage = resourceBundle.getResourceString("TableTabPanel.message.ClipboardUnavailable",
                                                            "Clipboard Currently Unavailable");
         errorString = resourceMessage + " : " + ise;
      }
      catch (UnsupportedFlavorException ufe)
      {
         resourceMessage = resourceBundle.getResourceString("TableTabPanel.message.UnsupportedData",
                                                            "Unsupported Data Flavor");
         errorString = resourceMessage + " : " + ufe;
      }
      catch (IOException ioe)
      {
         resourceMessage = resourceBundle.getResourceString("TableTabPanel.message.I/OException",
                                                            "I/O Exception");
         errorString = resourceMessage + " : " + ioe;
      }

      // Displays Errors as Needed.
      if (!errorString.equals(""))
      {
         resourceTitle = resourceBundle.getResourceString("TableTabPanel.dialogtitle.Alert", "Alert");
         
         JOptionPane.showMessageDialog(null, "TableTabPanel.pasteClipboardContents(), "
                                       + errorString, resourceTitle, JOptionPane.ERROR_MESSAGE);
            
      }
   }

   //==============================================================
   // Class Method to print the panel's current information.
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
      if (fields == null)
         return null;
      
      ArrayList<String> fieldsVector = new ArrayList <String>();
      Iterator<String> fieldsIterator = fields.iterator();
      
      while (fieldsIterator.hasNext())
         fieldsVector.add(fieldsIterator.next());
      
      return fieldsVector;
   }

   //==============================================================
   // Class method to allow classes to obtain the list of allowed
   // column names that are presently in the summary table.
   //==============================================================

   public ArrayList<String> getCurrentTableHeadings()
   {
      ArrayList<String> tableHeadingsVector = new ArrayList <String>();
      Iterator<String> tableHeadingsIterator = currentTableHeadings.iterator();
      
      while (tableHeadingsIterator.hasNext())
         tableHeadingsVector.add(tableHeadingsIterator.next());
      
      return tableHeadingsVector;
   }

   //==============================================================
   // Class method to allow classes to obtain the list of all
   // column names that are possible in the summary table.
   //==============================================================

   public ArrayList<String> getAllTableHeadings()
   {
      ArrayList<String> allTableHeadingsVector = new ArrayList <String>();
      Iterator<String> allTableHeadingsIterator = allTableHeadings.iterator();
      
      while (allTableHeadingsIterator.hasNext())
         allTableHeadingsVector.add(allTableHeadingsIterator.next());
      
      return allTableHeadingsVector;
   }

   //==============================================================
   // Class method to allow classes to obtain name of the table.
   //==============================================================

   public String getTableName()
   {
      return sqlTable;
   }

   //==============================================================
   // Class method to allow classes to obtain the name of the last
   // saved file name for the table.
   //==============================================================

   public String getSaveFileName()
   {
      return saveFileName;
   }
   
   //==============================================================
   // Class method to allow classes to obtain current summary
   // table SQL statement.
   //==============================================================
   
   public StringBuffer getTableSQLStatement()
   {
      StringBuffer newStringBuffer = new StringBuffer();
      
      newStringBuffer.append(sqlTableStatement.substring(0, sqlTableStatement.length()));
      return newStringBuffer;
   }

   //==============================================================
   // Class method to allow classes to obtain the table row start.
   //==============================================================

   public int getTableRowStart()
   {
      return tableRowStart;
   }

   //==============================================================
   // Class method to allow classes to obtain the table row limit.
   //==============================================================

   public int getTableRowLimit()
   {
      return tableRowLimit;
   }

   //==============================================================
   // Class method to allow classes to get the summary table row
   // size.
   //==============================================================

   public int getTableRowSize()
   {
      return tableRowLimit;
   }

   //==============================================================
   // Class method to allow classes to obtain the summary list table
   // presently displayed in the tab.
   //==============================================================
   
   public JTable getListTable()
   {
      return listTable;
   }

   //==============================================================
   // Class method to allow classes to obtain the number of valid
   // rows of summary table data.
   //==============================================================

   public int getValidDataRowCount()
   {
      int validDataRowCount;

      validDataRowCount = 0;
      for (int i = 0; i < listTable.getRowCount(); i++)
      {
         if (listTable.getValueAt(i, 0) != null)
            validDataRowCount++;
      }
      return validDataRowCount;
   }

   //==============================================================
   // Class method to allow classes to obtain the primary key(s)/
   // index(s) used by this list table.
   //==============================================================

   public ArrayList<String> getPrimaryKeys()
   {
      ArrayList<String> keysVector = new ArrayList <String>();
      Iterator<String> keysIterator = primaryKeys.iterator();
      
      while (keysIterator.hasNext())
         keysVector.add(keysIterator.next());
      
      return keysVector;
   }
   
   //==============================================================
   // Class method to allow classes to obtain the foreign key(s)/
   // index(s) used by this list table.
   //==============================================================

   public ArrayList<String> getForeignKeys()
   {
      ArrayList<String> keysVector = new ArrayList <String>();
      Iterator<String> keysIterator = foreignKeys.iterator();
      
      while (keysIterator.hasNext())
         keysVector.add(keysIterator.next());
      
      return keysVector;
   }
   
   //==============================================================
   // Class method to allow classes to obtain the exported key(s)/
   // index(s) used by this list table.
   //==============================================================

   public ArrayList<String> getExportedKeys()
   {
      ArrayList<String> keysVector = new ArrayList <String>();
      Iterator<String> keysIterator = exportedKeys.iterator();
      
      while (keysIterator.hasNext())
         keysVector.add(keysIterator.next());
      
      return keysVector;
   }

   //==============================================================
   // Class method to allow classes to obtain the auto-increment
   // hashmap.
   //==============================================================

   public HashMap<String, String> getAutoIncrementHashMap()
   {
      return autoIncrementHashMap;
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
   // Class method to allow classes to obtain the state of the
   // table panel.
   //==============================================================

   public String getState()
   {
      // Method Instances
      StringBuffer currentState;
      String delimiter;
      Iterator<String> headingsIterator;

      // Setting up and beginning getting the state.
      currentState = new StringBuffer();
      delimiter = getStateDelimiter();

      /*
       * Components in TableTabPanel to save.
       * 0. tableName
       * 1. tableRowStart
       * 2. tableRowLimit
       * 3. currentTableHeadings
       * 4. sqlTableStatement
       * 5. advancedSortSearch
       * 6. advancedSortSearchFrame stateComponents
       * 7. ascSortRadioButton
       * 8. descSortRadioButton
       * 9. sortComboBox
       * A. searchTextField
       * B. searchComboBox
       */

      // 0
      currentState.append(sqlTable + delimiter);

      // 1 & 2
      currentState.append(tableRowStart + delimiter + tableRowLimit + delimiter);

      // 3
      headingsIterator = currentTableHeadings.iterator();

      while (headingsIterator.hasNext())
         currentState.append(headingsIterator.next() + ";");
      currentState.append(delimiter);

      // 4 & 5
      currentState.append(sqlTableStatement + delimiter + advancedSortSearch + delimiter);

      // 6
      if (advancedSortSearchFrame != null)
         currentState.append(advancedSortSearchFrame.getKeyComponentsState() + delimiter);
      else
         currentState.append(delimiter);

      // 7 & 8
      currentState.append(ascSortRadioButton.isSelected() + delimiter + descSortRadioButton.isSelected()
                      + delimiter);

      // 9
      currentState.append(sortComboBox.getSelectedIndex() + delimiter);

      // A
      currentState.append(searchTextField.getText() + delimiter);

      // B
      currentState.append(searchComboBox.getSelectedIndex() + delimiter);

      // System.out.println(currentState.toString());
      return MyJSQLView_Utils.stateConvert((currentState.toString()).getBytes(), false);
   }

   //==============================================================
   // Class method to allow classes to obtain the state delimiter.
   //==============================================================

   public static String getStateDelimiter()
   {
      return stateDelimiter;
   }
   
   //==============================================================
   // Class method to allow classes to set the table heading fields.
   //==============================================================
   
   public void setTableHeadings(ArrayList<String> newHeadingFields)
   {
      // Create connection, remove old summary table and
      // reload the center panel.

      Connection work_dbConnection = ConnectionManager.getConnection("TableTabPanel setTableHeadings()");

      if (work_dbConnection == null)
         return;

      centerPanel.remove(tableScrollPane);
      currentTableHeadings = newHeadingFields;
      sqlTableFieldsString = "";
      
      // Reconstitute the table field names.

      Iterator<String> headings = currentTableHeadings.iterator();

      while (headings.hasNext())
      {
         sqlTableFieldsString += identifierQuoteString + columnNamesHashMap.get(headings.next())
                                 + identifierQuoteString + ", ";
      }
      // No fields, just load empty table else
      // clean up and load.
      if (sqlTableFieldsString.equals(""))
         tableData = new Object[0][0];
      else
      {
         sqlTableFieldsString = sqlTableFieldsString.substring(0, sqlTableFieldsString.length() - 2);
         loadTable(work_dbConnection);
      }

      tableModel = new MyJSQLView_TableModel(currentTableHeadings, tableData);
      tableModel.setValues(tableData);

      listTable = new JTable(tableModel);
      listTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
      listTable.getActionMap().put(TransferHandler.getCopyAction().getValue(Action.NAME),
                                   TransferHandler.getCopyAction());
      listTable.getActionMap().put(TransferHandler.getPasteAction().getValue(Action.NAME),
                                   TransferHandler.getPasteAction());
      listTable.addMouseListener(summaryTablePopupListener);
      listTable.getColumnModel().addColumnModelListener(this);
      
      // Sizing columns
      headings = currentTableHeadings.iterator();
      TableColumn column = null;

      int i = 0;
      while (headings.hasNext())
      {
         Object currentHeading = headings.next();
         column = listTable.getColumnModel().getColumn(i++);
         column.setPreferredWidth((preferredColumnSizeHashMap.get(currentHeading)).intValue());
      }

      tableScrollPane = new JScrollPane(listTable);

      tableScrollPane.setCorner(ScrollPaneConstants.UPPER_RIGHT_CORNER, activateAdvancedSortSearchButton);
      tableScrollPane.setCorner(ScrollPaneConstants.LOWER_RIGHT_CORNER, activateUpdateButton);

      tableScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
      tableScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

      centerPanel.add(sqlTable, tableScrollPane);
      centerCardLayout.show(centerPanel, sqlTable);

      ConnectionManager.closeConnection(work_dbConnection, "TableTabPanel setTableHeadings()");
   }

   //==============================================================
   // Class method to allow classes to set the summary table row
   // size.
   //==============================================================

   public void setTableRowSize(int numberOfRows)
   {
      tableRowLimit = numberOfRows;
      setTableHeadings(getCurrentTableHeadings());
      setRowsLabel((tableRowStart + 1), (tableRowStart + tableRowLimit));
   }

   //==============================================================
   // Class method to allow classes to set the name of the last
   // saved file name for the table.
   //==============================================================

   public void setSaveFileName(String fileName)
   {
      saveFileName = fileName;
   }

   //==============================================================
   // Class method to allow classes to set the summary table
   // search string and reload the table.
   //==============================================================

   public void setSearchTextField(String searchString)
   {
      searchComboBox.setSelectedIndex(0);
      searchTextField.setText(searchString);
      setTableRowSize(tableRowLimit);
   }
   
   //==============================================================
   // Class method to simplify the setting of the rows indicator
   // label for internationalization.
   //==============================================================

   private void setRowsLabel(int start, int end)
   {
      String resourceRows;
      
      resourceRows = resourceBundle.getResourceString("TableTabPanel.label.Rows", "Rows");
      rowsLabel.setText(resourceRows + ": " + start + " - " + end); 
   }

   //==============================================================
   // Class method to allow the setting of the state of the
   // table panel.
   //==============================================================

   public void setState(String stateString)
   {
      // Method Instances
      String delimiter;
      String[] tableStates;
      String resourceTitle, resourceMessage;
      int comboBoxItemCount, stateIndex;

      // Setting up and beginning setting the state.

      delimiter = getStateDelimiter();
      tableStates = stateString.split(delimiter);

      /*
       * Components in TableTabPanel to save.
       * 0. tableName
       * 1. tableRowStart
       * 2. tableRowLimit
       * 3. currentTableHeadings
       * 4. sqlTableStatement
       * 5. advancedSortSearch
       * 6. advancedSortSearchFrame stateComponents
       * 7. ascSortRadioButton
       * 8. descSortRadioButton
       * 9. sortComboBox
       * A. searchTextField
       * B. searchComboBox
       */

      if (tableStates.length == 12)
      {
         try
         {
            settingState = true;

            for (int i = 1; i < tableStates.length; i++)
            {
               // System.out.println(i + " " + tableStates[i]);

               if (i == 1)
                  tableRowStart = Integer.parseInt(tableStates[i]);

               else if (i == 2)
                  tableRowLimit = Integer.parseInt(tableStates[i]);

               else if (i == 3)
               {
                  String[] tableHeadings = tableStates[i].split(";");

                  if (tableHeadings.length != 0)
                  {
                     ArrayList<String> newTableHeadings = new ArrayList <String>();
                     boolean validFields = true;

                     for (int j = 0; j < tableHeadings.length; j++)
                     {
                        if (allTableHeadings.contains(tableHeadings[j]))
                           newTableHeadings.add(tableHeadings[j]);
                        else
                        {
                           resourceMessage = resourceBundle.getResourceString(
                              "TableTabPanel.message.SetTableField",
                              "Unable to Set Table Fields. Possible Corrupt File!");
                           
                           resourceTitle = resourceBundle.getResourceString(
                              "TableTabPanel.dialogtitle.Alert", "Alert");
                           
                           JOptionPane.showMessageDialog(null, resourceMessage, resourceTitle,
                                                         JOptionPane.ERROR_MESSAGE);
                           validFields = false;
                           break;
                        }
                     }
                     if (validFields)
                        currentTableHeadings = newTableHeadings;
                  }
               }

               else if (i == 4)
               {
                  sqlTableStatement.delete(0, sqlTableStatement.length());
                  sqlTableStatement.append(tableStates[i]);
               }

               else if (i == 5)
               {
                  if (tableStates[i].equals("true"))
                     advancedSortSearch = true;
                  else
                     advancedSortSearch = false;
               }

               else if (i == 6)
               {
                  if (advancedSortSearchFrame != null)
                     advancedSortSearchFrame.setKeyComponentsState(tableStates[i]);
                  else
                  {
                     if (!tableStates[i].equals(""))
                     {
                        createAdvancedSortSearchFrame();
                        advancedSortSearchFrame.setKeyComponentsState(tableStates[i]);
                     }
                  }
               }

               else if (i == 7)
               {
                  if (tableStates[i].equals("true"))
                  {
                     ascSortRadioButton.setSelected(true);
                     ascDescString = "ASC";
                  }
                  else
                  {
                     descSortRadioButton.setSelected(true);
                     ascDescString = "DESC";
                  }
               }

               else if (i == 8)
               {
                  // Nothing set in 7.
               }

               else if (i == 9)
               {
                  comboBoxItemCount = sortComboBox.getItemCount();
                  stateIndex = Integer.parseInt(tableStates[i]);

                  if (stateIndex > -1 && stateIndex < comboBoxItemCount)
                     sortComboBox.setSelectedIndex(stateIndex);
               }

               else if (i == 10)
                  searchTextField.setText(tableStates[i]);

               else
               {
                  comboBoxItemCount = sortComboBox.getItemCount();
                  stateIndex = Integer.parseInt(tableStates[i]);

                  if (stateIndex > -1 && stateIndex < comboBoxItemCount)
                     searchComboBox.setSelectedIndex(stateIndex);
               }
            }

            setRowsLabel((tableRowStart + 1), (tableRowStart + tableRowLimit));
            settingState = false;
         }
         catch (NumberFormatException e)
         {
            settingState = false;
            String optionPaneStringErrors = "Unable to Set Sort or Search Fields. Possible Corrupt File!";
            JOptionPane.showMessageDialog(null, optionPaneStringErrors, "Alert", JOptionPane.ERROR_MESSAGE);
         }
      }
      else
      {
         String optionPaneStringErrors = "Unable to Set Table State. Possible Corrupt File!";
         JOptionPane.showMessageDialog(null, optionPaneStringErrors, "Alert", JOptionPane.ERROR_MESSAGE);
      }
   }
   
   //==============================================================
   // Class method to limit the ability to add, edit, delete, and
   // view items in the summary list. Essentially make the table
   // view only. Allows the ablilty to generate export of summary
   // table without the keys, field select modification.
   //==============================================================

   public void setViewOnly(boolean viewState)
   {
      viewOnly = viewState;
      
      if (viewButton != null)
         viewButton.setEnabled(!viewState);
      
      if (addButton != null && editButton != null && deleteButton != null)
      {
         addButton.setEnabled(!viewState);
         editButton.setEnabled(!viewState);
         deleteButton.setEnabled(!viewState);
      }
      
      createListTablePopupMenu();
      listTable.addMouseListener(summaryTablePopupListener);
      listTable.getColumnModel().addColumnModelListener(this);
   }

   //==============================================================
   // Class method to toggle the current cardlayout in the center
   // panel. The method toggles between the summary list table
   // the tableview form.
   //==============================================================

   private void toggleCenterPanel()
   {
      centerCardLayout.next(centerPanel);
      centerPanel.requestFocus();
   }
}
