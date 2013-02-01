//=================================================================
//                   QueryFrame Class
//=================================================================
//   This class is used to provide a framework to execute
// queries on the current selected host by the user that
// has a connection established in MyJSQLView.
//
//                  << QueryFrame.java >>
//
//=================================================================
// Copyright (C) 2005-2013 Dana M. Proctor
// Version 8.7 10/18/2012
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
// Version 1.0 08/05/2006 MyJSQLView Initial QueryFrame.
//         1.1 08/06/2006 Added Panel centerPanel & Basic Functionality in Place.
//         1.2 08/14/2006 Added Instance maxTabs & queryTextAreaData.
//         1.3 08/14/2006 Added ChangeListener to JTabbedPane.
//         1.4 08/17/2006 Modified Interface to Allow Same Query in Same Tab
//                        and New Queries.
//         1.5 08/17/2006 Query Text Area Saving for Each Tab.
//         1.6 11/14/2006 Fixed So That the queryResultTextArea is Cleared
//                        Before the Query Execution. Bug/Patches Task #132766.
//         1.7 06/08/2007 Implemented MenuBar and Appropriate Actions. Also
//                        Class Methods getSelectTab(), getSelectedTabTitle() &
//                        menuItem().
//         1.8 06/11/2007 Super("MyJSQLView Query Frame").
//         1.9 09/01/2007 Cleaned Out and Added Menu Item Edit| Preferences |
//                        Table Row Size and Appropriate Action.
//         2.0 09/08/2007 MyJSQLView_JMenuBarActions.setQueryFrameNotVisible On
//                        Window Closing Event and File Menu | Exit. Code Cleanup.
//         2.1 09/09/2007 Removed Constructor Instance JMenuItem menuItem Called 
//                        DataDumpThread Directly. Also Removed Class Method
//                        summaryListTable Created in Action Block.
//         2.2 09/19/2007 Used System.getProperty("file.separator") for All
//                        File System Resources Accesses Through Instance
//                        fileSeparator.
//         2.3 09/26/2007 Changed Name of Data | Export | 'CSV' Format. Reviewed
//                        Implementaion of the CSV Table, and SQL Dumps, but
//                        These Actions Require Separate Connection Threads.
//                        The TempTables Created in This Frame Are Only Available
//                        to This Classes' Connections. Put Off for Now Other
//                        Dumps.
//         2.4 10/22/2007 Data | Export | CVS Format | Tab Summary Table Dump
//                        By Creating a new DataTableDumpThread Class in 
//                        Class Method actionPerformed().
//         2.5 11/19/2007 Closed Connection in MenuBar on File | Exit in Class
//                        Method actionPerformed().
//         2.6 12/12/2007 Header Update.
//         2.7 12/19/2007 Added Cut, Copy, & Paste to the JMenuBar | Edit.
//         2.8 12/24/2007 Added Instance tableColumnNamesHashMap to DataTableDumpThread()
//                        During Data | Export | CSV | Tab Table Summary.
//         2.9 12/25/2007 Update Edit Menu Items Information to Ctl+x, Ctl+c, &
//                        Ctl+v.
//         3.0 12/31/2007 HashMaps Loaded for Data | Export | CSV Format | Tab
//                        Summary Table in Class Method actionPerformed().
//         3.1 02/08/2008 Edit | Preferences Menu Added "Show Query". Added
//                        Class Method clearQueryResultTextArea().
//         3.2 02/09/2008 Class Method clearQueryResultTextArea(), setText("");
//                        Added Class Method clearHSQLDBMemoryTable().
//         3.3 02/20/2008 Implemented Open Script & Save Script in Class Method
//                        actionPerformed(). Considered Parsing This Method, But
//                        Did Not At This Time.
//         3.4 03/17/2008 Removed Class Method Instance intRowSize and Replaced
//                        With Class Instance summaryTableRowSize Array. Allowed
//                        the Controlling of Table Row Size in Each Tab. Initialized
//                        to Default 50. Class Methods Effected Constructor, and
//                        actionPerformed().
//         3.5 05/13/2008 Added Class Instance serialVersionUID. Declared
//                        query_dbConnection transient.
//         3.6 05/14/2008 Intialized static Class Instances queryTabsPane &
//                        queryResultTextArea at Declaration Instead of in
//                        Constructor.
//         3.7 06/13/2008 Class Instance queryTabPanel Constructor Argument Change
//                        to Pass Current Tab Index Row Limit.
//         3.8 08/25/2008 Fix a Bug to Eliminate Tabs, Temporary Tables Are Gone
//                        After a File | Exit or Window Closing Event So Tabs Need
//                        to be Remvoed. Added Class Instance clearingTabs. Class
//                        WindowAdapter() & Class Method actionPerformed().
//         3.9 08/26/2008 Added Class Method clearOracleDBTemporaryTables() to
//                        Handle Removaly of Temporary Tables Created During
//                        Query Tool Processing.
//         4.0 08/29/2008 Commented Out the Removal of the SEQUENCE in Class
//                        Method  clearOracleDBTemporaryTables(). Need to Review
//                        the Implementation of Threading for the Oracle Temporary
//                        Table Removal, or Maybe the Whole Query Frame Activity.
//         4.1 08/31/2008 Removed Class Methods clearHSQLDBMemoryTables &
//                        clearOracleDBTemporaryTables. Executed Process Through
//                        Class TableClearingThread.
//         4.2 09/09/2008 Corrected to Only Call TableCearingThread Class When
//                        HSQL or Oracle Databases Being Used.
//         4.3 10/20/2008 MyJSQLView Project Common Source Code Formatting.
//         4.4 10/21/2008 Additional Standardation to Comments.
//         4.5 11/07/2008 Added Constructor Instance iconsDirectory and Corrected
//                        the logoIcon File Name.
//         4.6 11/13/2008 Added Conditional Check for NULL query_Connection in
//                        Constructor.
//         4.7 11/14/2008 Changed the Way Invalid Connections Are Handled From 4.6.
//                        Now Just Checks for NULL in query_Connection and Does
//                        Not Allow Action to Take Place. Class Method actionPerfomed()
//                        and Inner Class QueryFrameListener.
//         4.8 11/12/2008 Added queryFrameToolBar.
//         4.9 05/14/2009 Class DataTableDumpThread Instantiation Constructor Argument
//                        tableColumnTypeHashMap Addition in Class Method actionPerformed().
//         5.0 10/19/2009 Added hostname and dbname to super() Call in Constructor.
//         5.1 10/24/2009 Obtained Instances fileSeparator and iconsDirectory From
//                        MyJSQLView_Utils Class.
//         5.2 10/25/2009 Moved ToolBar Icons Around to Match Menu. Corrected Icon
//                        Image Name for Export CSV Summary Table.
//         5.3 10/25/2009 Added fileSeparator to iconsDirectory.
//         5.4 11/10/2009 Added Class Instance lastDirectory, For Storing The Last
//                        Directory Used to Save/Open a Script. Implemented in
//                        actionPerformed().
//         5.5 02/18/2010 Changed Package to Reflect Dandy Made Productions Code.
//         5.6 03/05/2010 Implementation of Internationalization Through Class Instance
//                        resourceBundle. Methods Effected Constructor, & actionPerformed().
//                        Added Class Instances resourceAlert, resourceFileNOTFound
//                        and Methods createMenuBar() & createToolBar(). Cleared
//                        queryResultTextArea on Close.
//         5.7 03/27/2010 Commented columnNameFields = new Vector() in Class Method
//                        actionPerformed().
//         5.8 05/17/2010 Class Method actionPerformed() Parameterized Method
//                        Instances tableColumnNamesHashMap, tableColumnClassHashMap,
//                        tableColumnTypeHashMap, tableColumnSizeHashMap, &
//                        columnNameFields to Bring Code Into Compliance With Java
//                        5.0 API.
//         5.9 08/26/2010 Internationalization of Table Row Preferences Setting in
//                        actionPerformed().
//         6.0 08/26/2010 Correction to Table Row Preferences Warning Label Resources.
//         6.1 10/10/2010 Added Data | Export | PDF to Menu and ToolBar.
//         6.2 01/15/2011 Constructor Cast Object Returned by MyJSQLView_Access.
//                        getConnection() to Connection.
//         6.3 01/26/2011 Added Class Instance subProtocol and Constructor Instances
//                        connectionProperties, hostName, & databaseName. Connection
//                        Parameters Obtained From New Class ConnectionManager in
//                        Constructor and actionPerformed().
//         6.4 02/05/2011 Class Method actionPerformed() rowSizeDialog.pack() Instead
//                        of Sized.
//         6.5 04/27/2011 Class Instance queryTextArea.setDragEnabled(true) in Constructor.
//         6.6 06/11/2011 Removed Class Instance subProtocol and Replaced With dataSourceType.
//                        Derivation in Constructor From ConnectionManager.getDataSourceType().
//                        Effected queryFrameListener, & Method actionPerformed().
//         6.7 06/19/2011 Class Method actionPerformed() and queryFrameListener Change
//                        in Conditional for HSQL to Check for indexOf Instead of equals.
//                        Added Static Class Instances to Handle the Action Events From
//                        the Menu/ToolBar.
//         6.8 08/08/2011 Class Instance queryTabPanel Changed to tabPanel and Class JPanel.
//                        Removed Class Instance newQueryJButton and Renamed excuteQueryJButton
//                        to executeButton. Added Static Class Instance SQL_STATEMENT_TYPE.
//                        Removed Constructor Instance queryLabel, Added statementTypeComboBox
//                        & newTabCheckBox. Class Methods actionPerformed() and stateChanged()
//                        Corrected Processing for Changed Instances and Add SQLTabPanel
//                        Behavior. Added rowSizeLimit Setting in rowSizeTextField.
//         6.9 08/14/2011 Removed Argument currentQueryIndex from SQLTabPanel Class
//                        Instantiation. Added Correct getSelectedTab() for SQLTabPanel
//                        and QueryTabPanel. Also the Same for Processing. All in actionPerformed()
//                        Besides Noted Method.
//         7.0 08/16/2011 Added a ScrollPane to the Input queryTextArea. Organized the
//                        Method actionPerformed() to Further Evaluate Threading by Creating
//                        Separate Action Handlers openScriptFile(), saveScriptFile(),
//                        exportData(), print(), & setRowPreferences().
//         7.1 08/18/2011 Rebuilt Top Part of GUI to Comformalize and Add Status Indicator.
//                        As Such Added Class Instances workingQueryIndex, newTabState,
//                        status, statusIdle/WorkingIcon, statusIndicator, and statusLabel.
//                        Initialized in Constructor Along With Added in Same statusControlPanel,
//                        & statusPanel. Added Methods buildConstraints() & executeSQL().
//                        Added Method Instance scriptLineLimit to openScript(). Status
//                        Setting for Tabs in stateChanged().
//         7.2 08/18/2011 Made Class Instance maxTabs static.
//         7.3 08/19/2011 Method setRowPreferences() Removed Call to SQLTabPanel.setTableRowSize()
//                        and Just Reloaded a New SQLTabPanel.
//         7.4 09/18/2011 Added MyJSQLView.getPopupMenuListener() to queryTextArea and
//                        queryResultTextArea Instead of contentPane() in Constructor.
//         7.5 09/18/2011 Limiting MSAccess Type Databases to SQL_Statement Queries Only via
//                        the statementTypeComboBox Instance.
//         7.6 01/01/2012 Minor Code Placement Movement and Copyright Update.
//         7.7 01/11/2012 Removed the Casting of (Connection) for the Returned Instance for the
//                        ConnectionManager.getConnection() in Constructor.
//         7.8 02/16/2012 Added sqlQueryBucketButton & Implemented With it SQL Query Bucket Drops.
//                        Setup in Constructor and Action Event in actionPerformed().
//         7.9 05/07/2012 Changed columnNameFields in exportData() From Vector to ArrayList
//                        Data Type.
//         8.0 07/08/2012 Changes in Way MyJSQLView_ResourceBundle Handles the Collection
//                        of Resource Strings. Change to resource.getResourceString(key,
//                        default).
//         8.1 07/09/2012 Correction in Class Method createMenuBar() Resource Key QueryFrame.menu.
//                        CSV/PDFSummaryTable to ExportCSV/PDFSummaryTable.
//         8.2 08/06/2012 MyJSQLView Class Method Change of getLocaleResourceBundle()
//                        to getResourceBundle().
//         8.3 Collection of All Image Resources Through resourceBundle.
//         8.4 09/11/2012 Changed Package Name to com.dandymadeproductions.myjsqlview.gui.
//                        Made Class, Constructor, & Class Methods center(), getSelectedTab(),
//                        clearQueryResultTextArea(), getSelectedTab(), getSelectedTabTitle(),
//                        & setQueryResultTextArea() Public.
//         8.5 09/19/2012 Changed All Existing Toolbar Icons With 20x20 Pixels Ones.
//         8.6 09/21/2012 Creation of tableClearingThread in actionPerformed() & Used to Start
//                        Said Thread to Process Activity.
//         8.7 10/18/2012 Class Method setRowPreferences() Dressed Up JTextField.
//                                        
//-----------------------------------------------------------------
//                danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.DefaultEditorKit;

import com.dandymadeproductions.myjsqlview.MyJSQLView;
import com.dandymadeproductions.myjsqlview.datasource.ConnectionManager;
import com.dandymadeproductions.myjsqlview.datasource.ConnectionProperties;
import com.dandymadeproductions.myjsqlview.gui.panels.QueryTabPanel;
import com.dandymadeproductions.myjsqlview.gui.panels.SQLTabPanel;
import com.dandymadeproductions.myjsqlview.io.DataDumpThread;
import com.dandymadeproductions.myjsqlview.io.DataTableDumpThread;
import com.dandymadeproductions.myjsqlview.io.PDFDataTableDumpThread;
import com.dandymadeproductions.myjsqlview.io.WriteDataFile;
import com.dandymadeproductions.myjsqlview.utilities.InputDialog;
import com.dandymadeproductions.myjsqlview.utilities.MyJSQLView_ResourceBundle;
import com.dandymadeproductions.myjsqlview.utilities.MyJSQLView_Utils;
import com.dandymadeproductions.myjsqlview.utilities.TableClearingThread;

//=================================================================
//                     MyJSQLView QueryFrame
//=================================================================

/**
 *    The QueryFrame class is used to provide a framework to execute
 * queries on the current selected host by the user that has a
 * connection established in MyJSQLView.
 * 
 * @author Dana M. Proctor
 * @version 8.7 10/18/2012
 */

public class QueryFrame extends JFrame implements ActionListener, ChangeListener
{
   // =============================================
   // Creation of the necessary class instance
   // variables for the QueryFrame.
   // =============================================

   private static final long serialVersionUID = -4085953364431028570L;

   private static JTabbedPane queryTabsPane = new JTabbedPane();
   private JPanel tabPanel;

   private static final int maxTabs = 50;
   private int currentQueryIndex;
   private int workingQueryIndex;
   private int oldQueryIndex;
   private boolean clearingTabs, newTabState; 

   private JCheckBoxMenuItem showQueryCheckBox;
   private transient Connection query_dbConnection;

   private ImageIcon statusIdleIcon, statusWorkingIcon;
   private ImageIcon[] statusIndicatorIcon = new ImageIcon[maxTabs];
   private JLabel statusIndicator;
   private String[] status = new String[maxTabs];
   private JTextField statusLabel;
   private JButton sqlQueryBucketButton;
   private JComboBox statementTypeComboBox;
   private int[] tabStatementType = new int[maxTabs];
   
   private JTextArea queryTextArea;
   private String[] queryTextAreaData = new String[maxTabs];
   
   private int[] summaryTableRowSize = new int[maxTabs];
   
   private JButton executeButton;
   private JCheckBox newTabCheckBox;
   private static JTextArea queryResultTextArea = new JTextArea(4, 40);
   private MyJSQLView_ResourceBundle resourceBundle;
   private String resourceAlert, resourceFileNOTFound;
   private String dataSourceType, fileSeparator, iconsDirectory;
   private String lastDirectory;
   
   private static PrinterJob currentPrintJob = PrinterJob.getPrinterJob();
   private static PageFormat mPageFormat = currentPrintJob.defaultPage();
   
   private static String FILE_OPEN_SCRIPT = "FOS";
   private static String FILE_SAVE_SCRIPT = "FSS";
   private static String FILE_PRINT = "FP";
   private static String FILE_PAGE_FORMAT = "FPG";
   private static String FILE_EXIT = "FE";
   
   private static String EDITPREFERENCES_TABLE_ROWS = "EPTR";
   
   private static String DATAEXPORT_CSV_TABLE = "DECSVT";
   private static String DATAEXPORT_CSV_SUMMARY_TABLE = "DECSVTST";
   private static String DATAEXPORT_PDF_SUMMARY_TABLE = "DEPDFTST";
   
   private static int SQL_STATEMENT_TYPE = 0;
   // private static int QUERY_STATEMENT_TYPE = 1;

   //==============================================================
   // QueryFrame Constructor
   //==============================================================

   public QueryFrame()
   {
      // Constructor Instances.
      JMenuBar queryFrameMenuBar;
      JToolBar queryFrameToolBar;
      
      JPanel framePanel, mainPanel;
      JPanel queryPanel, centerPanel, queryResultPanel;
      JPanel statusControlPanel, controlPanel, statusPanel, buttonPanel;
      
      ImageIcon sqlQueryBucketIcon;
      ConnectionProperties connectionProperties;
      String hostName, databaseName, resource;
      
      // Setting up title, and other instances.
      
      resourceBundle = MyJSQLView.getResourceBundle();
      
      connectionProperties = ConnectionManager.getConnectionProperties();
      dataSourceType = ConnectionManager.getDataSourceType();
      hostName = connectionProperties.getProperty(ConnectionProperties.HOST);
      databaseName = connectionProperties.getProperty(ConnectionProperties.DB);
      
      resource = resourceBundle.getResourceString("QueryFrame.message.Title", "Query Frame");
      setTitle("MyJSQLView " + resource + "   " + hostName + ":" + databaseName);
      
      resource = resourceBundle.getResourceString("QueryFrame.dialogtitle.Alert", "Alert");
      resourceAlert = resource;
      
      resource = resourceBundle.getResourceString("QueryFrame.dialogmessage.FileNOTFound",
                                                  "File NOT Found");
      resourceFileNOTFound = resource;
      
      currentQueryIndex = 0;
      workingQueryIndex = 0;
      oldQueryIndex = currentQueryIndex;
      clearingTabs = false;
      newTabState = true;
      
      fileSeparator = MyJSQLView_Utils.getFileSeparator();
      iconsDirectory = MyJSQLView_Utils.getIconsDirectory() + fileSeparator;
      lastDirectory = "";
      
      statusIdleIcon = resourceBundle.getResourceImage(iconsDirectory + "statusIdleIcon.png");
      statusWorkingIcon = resourceBundle.getResourceImage(iconsDirectory + "statusWorkingIcon.png");
      sqlQueryBucketIcon = resourceBundle.getResourceImage(iconsDirectory + "addSQLQueryIcon.png");

      for (int i = 0; i < maxTabs; i++)
      {
         statusIndicatorIcon[i] = statusIdleIcon;
         status[i] = "Idle";
         summaryTableRowSize[i] = 50;
      }

      // Setting up a connection.
      query_dbConnection = ConnectionManager.getConnection("QueryFrame");
      
      //==================================================
      // Frame Window Closing Addition. Also method for
      // reactivating if desired/needed.
      //==================================================

      WindowListener queryFrameListener = new WindowAdapter()
      {
         public void windowClosing(WindowEvent e)
         {
            // Remove Memory/Temporary Table(s) for HSQL & Oracle
            if (dataSourceType.indexOf(ConnectionManager.HSQL) != -1
                || dataSourceType.equals(ConnectionManager.ORACLE))
               new TableClearingThread(queryTabsPane.getTabCount());

            // Clear out any query tab panes.
            clearingTabs = true;
            queryTabsPane.removeAll();
            
            // Clear Feedback Text Area.
            queryResultTextArea.setText("");

            // Close Connection
            if (query_dbConnection != null)
               ConnectionManager.closeConnection(query_dbConnection, "QueryFrame");
            MyJSQLView_JMenuBarActions.setQueryFrameNotVisisble();
            dispose();
         }

         public void windowActivated(WindowEvent e)
         {
         }
      };

      this.addWindowListener(queryFrameListener);

      // ===============================================
      // JMenu Bar for the Frame.
      // ===============================================

      queryFrameMenuBar = new JMenuBar();
      queryFrameMenuBar.setBorder(BorderFactory.createEtchedBorder());
      createMenuBar(queryFrameMenuBar);
      this.setJMenuBar(queryFrameMenuBar);
      
      // ===============================================
      // JTool Bar for the Frame.
      // ===============================================
      
      queryFrameToolBar = new JToolBar("MyJSQLView QueryFrame ToolBar");
      queryFrameToolBar.setBorder(BorderFactory.createLoweredBevelBorder());
      createToolBar(queryFrameToolBar);

      // ===============================================
      // Setting up the various panels that are used in
      // the QueryFrame
      // ===============================================
      
      framePanel = new JPanel(new BorderLayout());
      framePanel.add(queryFrameToolBar, BorderLayout.PAGE_START);

      mainPanel = new JPanel(new BorderLayout());
      mainPanel.setBorder(BorderFactory.createRaisedBevelBorder());

      // =====================================
      // QueryFrame SQL Entry Text Area

      GridBagLayout gridbag = new GridBagLayout();
      GridBagConstraints constraints = new GridBagConstraints();
      
      queryPanel = new JPanel(gridbag);
      queryPanel.setBorder(BorderFactory.createEtchedBorder());
      
      statusControlPanel = new JPanel(gridbag);
      statusControlPanel.setBorder(BorderFactory.createRaisedBevelBorder());
      
      // Query Bucket, SQL Type & Status Indicator
      
      controlPanel = new JPanel(gridbag);
      
      // SQL Query Bucket Drop Button.
      sqlQueryBucketButton = new JButton(sqlQueryBucketIcon);
      sqlQueryBucketButton.setMargin(new Insets(0, 0, 0, 0));
      sqlQueryBucketButton.setBorder(BorderFactory.createCompoundBorder(
                                                    BorderFactory.createRaisedBevelBorder(),
                                                    sqlQueryBucketButton.getBorder()));
      sqlQueryBucketButton.setFocusPainted(false);
      sqlQueryBucketButton.addActionListener(this);
      
      buildConstraints(constraints, 0, 0, 1, 1, 1, 100);
      constraints.fill = GridBagConstraints.HORIZONTAL;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(sqlQueryBucketButton, constraints);
      controlPanel.add(sqlQueryBucketButton);
      
      // SQL Query Type
      statementTypeComboBox = new JComboBox();
      statementTypeComboBox.setBorder(BorderFactory.createRaisedBevelBorder());
      
      // SQL_STATEMENT_TYPE:0
      resource = resourceBundle.getResourceString("QueryFrame.combobox.SQLStatement", "SQL Statement");
      statementTypeComboBox.addItem(resource + " : ");
      
      // QUERY_STATEMENT_TYPE:1
      if (!dataSourceType.equals(ConnectionManager.MSACCESS))
      {
         resource = resourceBundle.getResourceString("QueryFrame.combobox.QueryStatement",
                                                     "Query Statement");
         statementTypeComboBox.addItem(resource + " : ");
      }
      
      buildConstraints(constraints, 1, 0, 1, 1, 99, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(statementTypeComboBox, constraints);
      controlPanel.add(statementTypeComboBox);
      
      buildConstraints(constraints, 0, 0, 1, 1, 100, 50);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(controlPanel, constraints);
      statusControlPanel.add(controlPanel);
      
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
      
      statusLabel = new JTextField("Idle", 15);
      statusLabel.setHorizontalAlignment(JTextField.LEFT);
      statusLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
      statusLabel.setEditable(false);
      
      buildConstraints(constraints, 1, 0, 1, 1, 80, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.WEST;
      gridbag.setConstraints(statusLabel, constraints);
      statusPanel.add(statusLabel);
      
      buildConstraints(constraints, 0, 1, 1, 1, 100, 50);
      constraints.fill = GridBagConstraints.BOTH;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(statusPanel, constraints);
      statusControlPanel.add(statusPanel);
      
      buildConstraints(constraints, 0, 0, 1, 1, 20, 100);
      constraints.fill = GridBagConstraints.BOTH;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(statusControlPanel, constraints);
      queryPanel.add(statusControlPanel);
      
      queryTextArea = new JTextArea(5, 40);
      queryTextArea.setBorder(BorderFactory.createLoweredBevelBorder());
      queryTextArea.setLineWrap(true);
      queryTextArea.setDragEnabled(true);
      queryTextArea.addMouseListener(MyJSQLView.getPopupMenuListener());
      
      JScrollPane queryScrollPane = new JScrollPane(queryTextArea);
      
      buildConstraints(constraints, 1, 0, 1, 1, 60, 100);
      constraints.fill = GridBagConstraints.BOTH;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(queryScrollPane, constraints);
      queryPanel.add(queryScrollPane);
      
      buttonPanel = new JPanel();
      buttonPanel.setLayout(new GridLayout(2, 1, 4, 8));
      buttonPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(),
                                                               BorderFactory.createEmptyBorder(4, 4, 4, 4)));

      resource = resourceBundle.getResourceString("QueryFrame.button.Execute", "Execute");
      executeButton = new JButton(resource);
      executeButton.setMnemonic(KeyEvent.VK_ENTER);
      executeButton.addActionListener(this);
      buttonPanel.add(executeButton);

      resource = resourceBundle.getResourceString("QueryFrame.checkbox.NewTab", "New Tab");
      newTabCheckBox = new JCheckBox(resource, true);
      
      newTabCheckBox.setIcon(resourceBundle.getResourceImage(iconsDirectory + "limitUpIcon.png"));
      newTabCheckBox.setSelectedIcon(resourceBundle.getResourceImage(iconsDirectory + "limitDownIcon.png"));
      newTabCheckBox.setMargin(new Insets(4, 1, 4, 1));
      newTabCheckBox.setBorder(BorderFactory.createRaisedBevelBorder());
      newTabCheckBox.setFocusPainted(false);
      
      buttonPanel.add(newTabCheckBox);
      
      buildConstraints(constraints, 2, 0, 1, 1, 20, 100);
      constraints.fill = GridBagConstraints.BOTH;
      constraints.anchor = GridBagConstraints.WEST;
      gridbag.setConstraints(buttonPanel, constraints);
      queryPanel.add(buttonPanel);

      mainPanel.add(queryPanel, BorderLayout.NORTH);

      // =====================================
      // QueryFrame Resultant Data Set Panel

      centerPanel = new JPanel(new GridLayout(1, 1, 0, 0));
      centerPanel.setBorder(BorderFactory.createEtchedBorder());

      queryTabsPane.setTabPlacement(JTabbedPane.TOP);
      queryTabsPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
      queryTabsPane.addChangeListener(this);
      centerPanel.add(queryTabsPane);

      mainPanel.add(centerPanel, BorderLayout.CENTER);

      // =====================================
      // QueryFrame SQL Feedback TextArea.

      queryResultPanel = new JPanel(new GridLayout(1, 1, 0, 0));
      queryResultPanel.setBorder(BorderFactory.createEtchedBorder());

      queryResultTextArea.setBorder(BorderFactory.createLoweredBevelBorder());
      queryResultTextArea.setLineWrap(true);
      queryResultTextArea.addMouseListener(MyJSQLView.getPopupMenuListener());

      JScrollPane resultScrollPane = new JScrollPane(queryResultTextArea);
      queryResultPanel.add(resultScrollPane);

      mainPanel.add(queryResultPanel, BorderLayout.SOUTH);
      
      framePanel.add(mainPanel, BorderLayout.CENTER);

      getContentPane().add(framePanel);

      queryTextArea.requestFocus();
   }

   //==============================================================
   // ActionEvent Listener method for detecting the user's selection
   // of a Menu Bar item. Upon detection an action is the completed
   // by calling the MyJSQLView_JMenuBarActions class. Created to
   // reduce clutter in this top level main class.
   //==============================================================

   public void actionPerformed(ActionEvent evt)
   {
      Object panelSource = evt.getSource();

      // SQL Query Bucket Drop
      if (panelSource == sqlQueryBucketButton)
      {
         String query = queryTextArea.getText();
         
         if (!query.equals(""))
            MyJSQLView_Frame.getSQLBucket().addSQLStatement(query);
         return;
      }
      
      // Execute SQL Action
      if (panelSource == executeButton)
      {
         queryTabsPane.removeChangeListener(this);
         
         executeSQL();
         
         queryTabsPane.addChangeListener(this);
         newTabCheckBox.setSelected(false);
         return;
      }

      // MenuBar Actions
      if (panelSource instanceof JMenuItem || panelSource instanceof JButton)
      {
         // Instances & Setting Up.
         String actionCommand;

         if (panelSource instanceof JMenuItem)
         {
            JMenuItem item = (JMenuItem) panelSource;
            actionCommand = item.getActionCommand();
         }
         else
         {
            JButton item = (JButton) panelSource;
            actionCommand = item.getActionCommand();
         }

         // ==================================
         // File Menu Item Selection Routing
         // ==================================

         // Open Script
         if (actionCommand.equals(FILE_OPEN_SCRIPT))
         {
            openScriptFile();
         }

         // Save Script
         if (actionCommand.equals(FILE_SAVE_SCRIPT))
         {  
            saveScriptFile();
         }

         // Print
         if (actionCommand.equals(FILE_PRINT))
         {
            printData();
         }

         // Print PageFormat Dialog
         if (actionCommand.equals(FILE_PAGE_FORMAT))
         {
            PrinterJob pj = PrinterJob.getPrinterJob();
            mPageFormat = pj.pageDialog(mPageFormat);
         }

         // Exit
         if (actionCommand.equals(FILE_EXIT))
         {
            // Remove Memory/Temp Table(s) for HSQL & Oracle
            if (dataSourceType.indexOf(ConnectionManager.HSQL) != -1
                || dataSourceType.equals(ConnectionManager.ORACLE))
            {
               Thread tableClearingThread = new Thread(new TableClearingThread(queryTabsPane.getTabCount()),
                                                       "TableClearingThread");
               tableClearingThread.start();
            }

            // Clear out any query tab panes.
            clearingTabs = true;
            queryTabsPane.removeAll();
            
            // Clear Feedback Text Area.
            queryResultTextArea.setText("");

            // Close connection and dispose.
            if (query_dbConnection != null)
               ConnectionManager.closeConnection(query_dbConnection, "QueryFrame");
            MyJSQLView_JMenuBarActions.setQueryFrameNotVisisble();
            this.dispose();
         }

         // ==================================
         // Edit Menu Item Selection Routing
         // ==================================

         // Table Row Preferences
         if (actionCommand.equals(EDITPREFERENCES_TABLE_ROWS)
               && queryTabsPane.getSelectedComponent() != null)
         {
            setRowPreferences();
         }

         // ==================================
         // Data Menu Item Selection Routing
         // ==================================

         // Data Export
         if (getSelectedTab() != null
             && (actionCommand.indexOf("DECSV") != -1 || actionCommand.indexOf("DEPDF") != -1
                 || actionCommand.indexOf("DESQL") != -1))
         {
            exportData(actionCommand);
         }
      }
   }

   //==============================================================
   // ChangeEvent Listener method for detecting the user's selection
   // of the JTabbedPane. Used to load the querytext field for
   // selected pane and keep track of the current active pane.
   //==============================================================

   public void stateChanged(ChangeEvent evt)
   {
      Object panelSource = evt.getSource();

      if (panelSource instanceof JTabbedPane && !clearingTabs)
      {
         // Set the query index for the pane.
         oldQueryIndex = currentQueryIndex;
         currentQueryIndex = queryTabsPane.getSelectedIndex();

         // Set the tabs various paramerters.
         if (oldQueryIndex != currentQueryIndex)
         {
            queryTextArea.setText(queryTextAreaData[currentQueryIndex]);
            statementTypeComboBox.setSelectedIndex(tabStatementType[currentQueryIndex]);
            statusIndicator.setIcon(statusIndicatorIcon[currentQueryIndex]);
            statusLabel.setText(status[currentQueryIndex]);
         }
         // System.out.println("tab changed: " + currentQueryIndex);
      }
   }
   
   //==============================================================
   // Class method used to execute the given SQL input by the user
   // as defined in the query text area.
   //==============================================================

   private void executeSQL()
   {
      if (query_dbConnection != null)
      {
         // Lets clear any left over query errors.
         queryResultTextArea.setText("");

         // Get tab index to use.
         if (newTabCheckBox.isSelected())
         {
            oldQueryIndex = currentQueryIndex;
            currentQueryIndex = queryTabsPane.getTabCount();
            newTabState = true;
         }
         else
            newTabState = false;
         
         // Save query text, statement type, and status.
         queryTextAreaData[currentQueryIndex] = queryTextArea.getText();
         tabStatementType[currentQueryIndex] = statementTypeComboBox.getSelectedIndex();
         workingQueryIndex = currentQueryIndex;
         
         // Set Status
         statusIndicator.setIcon(statusWorkingIcon);
         statusIndicatorIcon[currentQueryIndex] = statusWorkingIcon;
         statusLabel.setText("Working");
         status[currentQueryIndex] = "Working";
         
         // Create a thread to create the appropriate
         // panel that will be used to run the SQL.
         
         Thread executeSQLThread = new Thread(new Runnable()
         {
            boolean isNewTab = newTabState;
            
            public void run()
            {
               // SQL Statement
               if (statementTypeComboBox.getSelectedIndex() == SQL_STATEMENT_TYPE)
               {
                  tabPanel = new SQLTabPanel(queryTextArea.getText(),
                                             summaryTableRowSize[workingQueryIndex],
                                             resourceBundle);
               }
               // Query Statement
               else
               {
                  tabPanel = new QueryTabPanel(workingQueryIndex + "",
                                               queryTextArea.getText(),
                                               showQueryCheckBox.isSelected(),
                                               summaryTableRowSize[workingQueryIndex],
                                               query_dbConnection, resourceBundle);
               }
               
               if (isNewTab)
                  queryTabsPane.addTab(workingQueryIndex + "", tabPanel);
               else
                  queryTabsPane.setComponentAt(workingQueryIndex, tabPanel);
               
               // Show tab and return status to idle.
               queryTabsPane.setSelectedIndex(queryTabsPane.indexOfTab(workingQueryIndex + ""));
               statusIndicator.setIcon(statusIdleIcon);
               statusIndicatorIcon[workingQueryIndex] = statusIdleIcon;
               statusLabel.setText("Idle");
               status[currentQueryIndex] = "Idle";      
            }
         }, "QueryFrame.executeSQLThread");
         executeSQLThread.start();   
      }
   }
   
   //==============================================================
   // Class method used to handle the loading of a SQL script file.
   // Note: The script file is limited to importing only 100 lines.
   //==============================================================

   private void openScriptFile()
   {
      // Method Instances.
      JFileChooser openScriptFileChooser;
      int fileChooserResult;
      int scriptLineLimit = 100;
      String fileName, resource, message;
      
      // Choosing the file to import data from.
      
      if (lastDirectory.equals(""))
         openScriptFileChooser = new JFileChooser();
      else
         openScriptFileChooser = new JFileChooser(new File(lastDirectory));
      
      fileChooserResult = openScriptFileChooser.showOpenDialog(null);

      // Looks like might be good so lets check and write data.
      if (fileChooserResult == JFileChooser.APPROVE_OPTION)
      {
         lastDirectory = openScriptFileChooser.getCurrentDirectory().toString();
         fileName = openScriptFileChooser.getSelectedFile().getName();
         fileName = openScriptFileChooser.getCurrentDirectory() + fileSeparator + fileName;
         // System.out.println(fileName);

         if (!fileName.equals(""))
         {
            try
            {
               FileReader fileReader = new FileReader(fileName);
               BufferedReader bufferedReader = new BufferedReader(fileReader);
               String currentLine;

               int lineNumber = 1;
               queryTextArea.setText("");

               while ((currentLine = bufferedReader.readLine()) != null && lineNumber < scriptLineLimit)
               {
                  queryTextArea.append(currentLine);
                  lineNumber++;
               }
               bufferedReader.close();
               fileReader.close();
            }
            catch (IOException e)
            {
               resource = resourceBundle.getResourceString("QueryFrame.dialogmessage.InputFile",
                                                           "Unalbe to Read Input File");
               message = resource; 
               JOptionPane.showMessageDialog(null, message, resourceAlert, JOptionPane.ERROR_MESSAGE);
            }
         }
         else
         {
            JOptionPane.showMessageDialog(null, resourceFileNOTFound, resourceAlert,
                                          JOptionPane.ERROR_MESSAGE);
         }
      } 
   }
   
   //==============================================================
   // Class method used to handle the saving of a script file.
   //==============================================================

   private void saveScriptFile()
   {
      // Method Instances.
      JFileChooser saveScriptFileChooser;
      int fileChooserResult;
      String fileName;
      
      // Setup a file chooser and showing.
      
      if (lastDirectory.equals(""))
         saveScriptFileChooser = new JFileChooser();
      else
         saveScriptFileChooser = new JFileChooser(new File(lastDirectory));
         
      fileChooserResult = saveScriptFileChooser.showSaveDialog(null);

      // Looks like might be good so lets check and then write data.
      if (fileChooserResult == JFileChooser.APPROVE_OPTION)
      {
         lastDirectory = saveScriptFileChooser.getCurrentDirectory().toString();
         fileName = saveScriptFileChooser.getSelectedFile().getName();
         fileName = saveScriptFileChooser.getCurrentDirectory() + fileSeparator + fileName;
         // System.out.println(fileName);

         if (!fileName.equals(""))
         {
            WriteDataFile.mainWriteDataString(fileName, ((String) queryTextArea.getText()).getBytes(),
               false);
         }
      }
   }
   
   //==============================================================
   // Class method used print the summary table data from the
   // current showing tab.
   //==============================================================

   private void printData()
   {
      // Method Instances.
      Paper customPaper;
      double margin;
      String resource, message;
      
      // Setting up the printing.

      customPaper = new Paper();
      margin = 36;
      customPaper.setImageableArea(margin, margin, customPaper.getWidth() - margin * 2,
                                   customPaper.getHeight() - margin * 2);
      mPageFormat.setPaper(customPaper);

      // Printing the selected Tab
      if (getSelectedTab() != null)
      {  
         if (getSelectedTab() instanceof QueryTabPanel)
            currentPrintJob.setPrintable(((QueryTabPanel) getSelectedTab()), mPageFormat);
         else
            currentPrintJob.setPrintable(((SQLTabPanel) getSelectedTab()), mPageFormat);

         // Should have graphics to print now so lets try to print.
         
         if (currentPrintJob.printDialog())
         {
            try
            {
               currentPrintJob.print();
            }
            catch (PrinterException e)
            {
               resource = resourceBundle.getResourceString("QueryFrame.dialogmessage.PrinterException",
                                                           "Printer Exception");
               message = resource;
               
               JOptionPane.showMessageDialog(null, message, e.getMessage(),
                                             JOptionPane.ERROR_MESSAGE);
            }
         }
      }
   }
   
   //==============================================================
   // Class method used set the current visible tab data panel's
   // maxium number of returned rows from the result set.
   //==============================================================

   private void setRowPreferences()
   {
      // Method Instances.
      String resource, resourceOK, resourceCancel;
      String message;
      
      // Setup and display a option pane to collect the new
      // summary table row size.

      JTextField rowSizeTextField;
      JLabel warning, warningMessage1, warningMessage2;
      
      rowSizeTextField = new JTextField();
      rowSizeTextField.setBorder(BorderFactory.createCompoundBorder(
         BorderFactory.createEtchedBorder(EtchedBorder.RAISED),
         BorderFactory.createLoweredBevelBorder()));
      if (currentQueryIndex <= summaryTableRowSize.length)
         rowSizeTextField.setText(Integer.toString(summaryTableRowSize[currentQueryIndex]));
      
      resource = resourceBundle.getResourceString("QueryFrame.label.Warning", "Warning!");
      warning = new JLabel(resource, JLabel.CENTER);
      warning.setForeground(Color.RED);
      
      resource = resourceBundle.getResourceString("QueryFrame.label.LargeRowSize",
                                                  "A large row size may adversely effect");
      warningMessage1 = new JLabel(resource, JLabel.CENTER);
      
      resource = resourceBundle.getResourceString("QueryFrame.label.ApplicationServerPerformance",
                                                  "application/server performance");
      warningMessage2 = new JLabel(resource, JLabel.CENTER);

      Object content[] = {warning, warningMessage1, warningMessage2, rowSizeTextField};

      resource = resourceBundle.getResourceString("QueryFrame.label.SetSummaryTableRowSize",
                                                  "Set Summary Table Row Size");
      resourceOK = resourceBundle.getResourceString("QueryFrame.button.OK", "OK");
      resourceCancel = resourceBundle.getResourceString("QueryFrame.button.Cancel", "Cancel");
      
      InputDialog rowSizeDialog = new InputDialog(null, resource, resourceOK, resourceCancel,
                                                  content, null);
      rowSizeDialog.pack();
      rowSizeDialog.setResizable(false);
      rowSizeDialog.center();
      rowSizeDialog.setVisible(true);

      // Collect the new row size input and updating
      // the current selected summary table.

      if (rowSizeDialog.isActionResult())
      {
         try
         {
            summaryTableRowSize[currentQueryIndex] = Integer.parseInt(rowSizeTextField.getText());
            
            JPanel currentTab = (JPanel) queryTabsPane.getSelectedComponent();
            
            // Select the correct panel type.
            
            // Query Statement
            if (currentTab != null && currentTab instanceof QueryTabPanel)
               ((QueryTabPanel) currentTab).setTableRowSize(summaryTableRowSize[currentQueryIndex]);
            
            // SQL Statement
            else if (currentTab != null && currentTab instanceof SQLTabPanel)
            {
               queryTabsPane.removeChangeListener(this);
               executeSQL();
               queryTabsPane.addChangeListener(this);
            }
            
            else { /* Something not right. */}
         }
         catch (NumberFormatException e)
         {
            message = resourceBundle.getResourceString("QueryFrame.dialogmessage.RowSize",
               "The Row Size Input Appears To NOT Be A Valid Integer!");
            
            JOptionPane.showMessageDialog(null, message, resourceAlert, JOptionPane.ERROR_MESSAGE);
         }
      }
   }
   
   //==============================================================
   // Class method to execute the selected type of data export of
   // the data in summary table of the current visible tab.
   //==============================================================

   private void exportData(String actionCommand)
   {
      // Method Instances
      JFileChooser exportData;
      SimpleDateFormat dateFormat;
      String fileName, exportedTable;
      int fileChooserResult;
      
      HashMap<String, String> tableColumnNamesHashMap = new HashMap <String, String>();
      HashMap<String, String> tableColumnClassHashMap = new HashMap <String, String>();
      HashMap<String, String> tableColumnTypeHashMap = new HashMap <String, String>();
      HashMap<String, Integer> tableColumnSizeHashMap = new HashMap <String, Integer>();

      // Creating and showing a file chooser based on a default file name
      // derived from the table and date.

      exportData = new JFileChooser();
      dateFormat = new SimpleDateFormat("yyyyMMdd");
      
      if (getSelectedTab() instanceof QueryTabPanel)
         exportedTable = ((QueryTabPanel) getSelectedTab()).getTableName();
      else
         exportedTable = "sqlData";

      fileName = exportedTable + "_" + dateFormat.format(new Date());

      if (actionCommand.indexOf("DECSV") != -1)
         fileName += ".csv";
      else if (actionCommand.indexOf("DEPDF") != -1)
         fileName += ".pdf";
      else
         fileName += ".sql";

      exportData.setSelectedFile(new File(fileName));

      fileChooserResult = exportData.showSaveDialog(null);

      // Looks like might be good so lets check and then write data.
      if (fileChooserResult == JFileChooser.APPROVE_OPTION)
      {
         fileName = exportData.getSelectedFile().getName();
         fileName = exportData.getCurrentDirectory() + fileSeparator + fileName;
         // System.out.println(fileName);

         if (!fileName.equals(""))
         {
            ArrayList<String> columnNameFields = new ArrayList <String>();
            
            if (actionCommand.indexOf("DECSVT") != -1 || actionCommand.indexOf("DESQL") != -1)
            {
               if (getSelectedTab() instanceof QueryTabPanel)
               {
                  //columnNameFields = new Vector();
                  columnNameFields = ((QueryTabPanel) getSelectedTab()).getTableFields();
                  tableColumnNamesHashMap = ((QueryTabPanel) getSelectedTab()).getColumnNamesHashMap();
                  tableColumnClassHashMap = ((QueryTabPanel) getSelectedTab()).getColumnClassHashMap();
                  tableColumnTypeHashMap = ((QueryTabPanel) getSelectedTab()).getColumnTypeHashMap();
                  tableColumnSizeHashMap = ((QueryTabPanel) getSelectedTab()).getColumnSizeHashMap();
                  
               }
               else
               {
                  columnNameFields = ((SQLTabPanel) getSelectedTab()).getTableHeadings();
                  tableColumnNamesHashMap = ((SQLTabPanel) getSelectedTab()).getColumnNamesHashMap();
                  tableColumnClassHashMap = ((SQLTabPanel) getSelectedTab()).getColumnClassHashMap();
                  tableColumnTypeHashMap = ((SQLTabPanel) getSelectedTab()).getColumnTypeHashMap();
                  tableColumnSizeHashMap = ((SQLTabPanel) getSelectedTab()).getColumnSizeHashMap();
               }
            }

            // Select the table and fields to export and
            // then outputting the data via the appropriate
            // approach. Basic Summary Table CSV Implemented
            // Only in 2.72, 2.76.

            // Data Export CVS Table
            if (actionCommand.equals(DATAEXPORT_CSV_TABLE))
            {
               new DataDumpThread(columnNameFields, tableColumnNamesHashMap,
                                  tableColumnClassHashMap, tableColumnTypeHashMap,
                                  tableColumnSizeHashMap, exportedTable,
                                  fileName);
            }

            // Data Export CVS & PDF Tab Summary Table
            else if (actionCommand.equals(DATAEXPORT_CSV_SUMMARY_TABLE) ||
                  actionCommand.equals(DATAEXPORT_PDF_SUMMARY_TABLE))
            {
               JTable summaryListTable;
               
               if (getSelectedTab() instanceof QueryTabPanel)
                  summaryListTable = ((QueryTabPanel) getSelectedTab()).getListTable();
               else
                  summaryListTable = ((SQLTabPanel) getSelectedTab()).getListTable();
               
               if (summaryListTable != null)
               {
                  if (actionCommand.equals(DATAEXPORT_CSV_SUMMARY_TABLE))
                     new DataTableDumpThread(summaryListTable, tableColumnNamesHashMap,
                                             tableColumnTypeHashMap, exportedTable,
                                             fileName);
                  else
                     new PDFDataTableDumpThread(summaryListTable, tableColumnTypeHashMap,
                        exportedTable, fileName); 
               }
            }
            
            /*
            // Data Export SQL Table
            else if (actionCommand.equals("DESQLT"))
            {
                SQLDataDumpThread sqlDump = new SQLDataDumpThread(columnNameFields,
                                                                  tableColumnNamesHashMap,
                                                                  false, tableColumnClassHashMap,
                                                                  tableColumnTypeHashMap,
                                                                  exportedTable, fileName,
                                                                  myJSQLView_Version);
            }
            
            // Data Export SQL Tab Summary Table
            else if (actionCommand.equals("DESQLTST"))
            {
                columnNameFields = new Vector();
                columnNameFields = (getSelectedTab()).getTableHeadings();
                SQLDataDumpThread sqlDump = new SQLDataDumpThread(columnNameFields,
                                                                  tableColumnNamesHashMap,
                                                                  true, tableColumnClassHashMap,
                                                                  tableColumnTypeHashMap,
                                                                  exportedTable, fileName,
                                                                  myJSQLView_Version);
             }
             */
         }
         else
         {
            JOptionPane.showMessageDialog(null, resourceFileNOTFound, resourceAlert,
                                          JOptionPane.ERROR_MESSAGE);
         }
      }
   }
   
   //==============================================================
   // Private method used for creation of the menu bar that will be
   // used with the frame.
   //==============================================================

   private void createMenuBar(JMenuBar queryFrameMenuBar)
   {
      // Method Instances
      String resource;
      ImageIcon logoIcon;
      JButton logoIconItem;
      
      JMenuItem menuItem = null;
      JMenu fileMenu, editMenu, dataMenu;
      JMenu preferencesMenu, exportMenu, exportCVSMenu;

      // ===============
      // File Menu
      
      resource = resourceBundle.getResourceString("QueryFrame.menu.File", "File");
      fileMenu = new JMenu(resource);
      fileMenu.setFont(fileMenu.getFont().deriveFont(Font.BOLD));
      
      resource = resourceBundle.getResourceString("QueryFrame.menu.OpenScript", "Open Script");
      fileMenu.add(menuItem(resource, FILE_OPEN_SCRIPT));
      
      resource = resourceBundle.getResourceString("QueryFrame.menu.SaveScript", "Save Script");
      fileMenu.add(menuItem(resource, FILE_SAVE_SCRIPT));
      fileMenu.addSeparator();
      
      resource = resourceBundle.getResourceString("QueryFrame.menu.Print", "Print");
      fileMenu.add(menuItem(resource, FILE_PRINT));
      
      resource = resourceBundle.getResourceString("QueryFrame.menu.PageFormat", "Page Format");
      fileMenu.add(menuItem(resource, FILE_PAGE_FORMAT));
      fileMenu.addSeparator();
      
      resource = resourceBundle.getResourceString("QueryFrame.menu.Exit", "Exit");
      fileMenu.add(menuItem(resource, FILE_EXIT));
      queryFrameMenuBar.add(fileMenu);

      // ===============
      // Edit Menu
      
      resource = resourceBundle.getResourceString("QueryFrame.menu.Edit", "Edit");
      editMenu = new JMenu(resource);
      editMenu.setFont(editMenu.getFont().deriveFont(Font.BOLD));
      
      menuItem = new JMenuItem(new DefaultEditorKit.CutAction());
      resource = resourceBundle.getResourceString("QueryFrame.menu.Cut", "Cut");
      menuItem.setText(resource + "          " + "Ctrl+x");
      menuItem.setMnemonic(KeyEvent.VK_X);
      editMenu.add(menuItem);

      menuItem = new JMenuItem(new DefaultEditorKit.CopyAction());
      resource = resourceBundle.getResourceString("QueryFrame.menu.Copy", "Copy");
      menuItem.setText(resource + "       " + "Ctrl+c");
      menuItem.setMnemonic(KeyEvent.VK_C);
      editMenu.add(menuItem);

      menuItem = new JMenuItem(new DefaultEditorKit.PasteAction());
      resource = resourceBundle.getResourceString("QueryFrame.menu.Paste", "Paste");
      menuItem.setText(resource + "       " + "Ctrl+v");
      menuItem.setMnemonic(KeyEvent.VK_V);
      editMenu.add(menuItem);

      editMenu.addSeparator();

      resource = resourceBundle.getResourceString("QueryFrame.menu.Preferences", "Preferences");
      preferencesMenu = new JMenu(resource);

      resource = resourceBundle.getResourceString("QueryFrame.menu.TableRows", "Table Rows");
      preferencesMenu.add(menuItem(resource, EDITPREFERENCES_TABLE_ROWS));

      resource = resourceBundle.getResourceString("QueryFrame.menu.ShowQuery", "Show Query");
      showQueryCheckBox = new JCheckBoxMenuItem(resource, false);
      preferencesMenu.add(showQueryCheckBox);

      editMenu.add(preferencesMenu);

      queryFrameMenuBar.add(editMenu);

      // ===============
      // Data Menu
      // Only Basic Export CVS Summary Table
      // in 2.72, 2.76. Reviewed Implementation
      // of CSV Table, SQL Table, SQL Table Tab Summary,
      // all these required the connection to be passed
      // to the dump threads. The temp tables are only
      // available to this class's connection.

      resource = resourceBundle.getResourceString("QueryFrame.menu.Data", "Data");
      dataMenu = new JMenu(resource);
      dataMenu.setFont(dataMenu.getFont().deriveFont(Font.BOLD));

      resource = resourceBundle.getResourceString("QueryFrame.menu.Export", "Export");
      exportMenu = new JMenu(resource);

      resource = resourceBundle.getResourceString("QueryFrame.menu.ExportCSV", "CSV");
      exportCVSMenu = new JMenu(resource);
      
      /*
      resource = resourceBundle.getResourceString("QueryFrame.menu.CSVTable", "Table);
      exportCVSMenu.add(menuItem(resource, DATAEXPORT_CSV_TABLE));
      */
      
      resource = resourceBundle.getResourceString("QueryFrame.menu.ExportCSVSummaryTable", "Summary Table");
      exportCVSMenu.add(menuItem(resource, DATAEXPORT_CSV_SUMMARY_TABLE));
      exportMenu.add(exportCVSMenu);
      
      resource = resourceBundle.getResourceString("QueryFrame.menu.ExportPDF", "PDF");
      exportCVSMenu = new JMenu(resource);
      
      resource = resourceBundle.getResourceString("QueryFrame.menu.ExportPDFSummaryTable", "Summary Table");
      exportCVSMenu.add(menuItem(resource, DATAEXPORT_PDF_SUMMARY_TABLE));
      exportMenu.add(exportCVSMenu);

      // JMenu exportSQLMenu = new JMenu("SQL");
      // exportSQLMenu.add(menuItem("Table", "DESQLT"));
      // exportSQLMenu.add(menuItem("Tab Summary Table", "DESQLTST"));
      // exportMenu.add(exportSQLMenu);

      dataMenu.add(exportMenu);
      queryFrameMenuBar.add(dataMenu);
      queryFrameMenuBar.add(Box.createHorizontalGlue());
      
      // ===============
      // Logo
      
      logoIcon = resourceBundle.getResourceImage(iconsDirectory + "myjsqlviewIcon.gif");
      logoIconItem = new JButton(logoIcon);
      logoIconItem.setDisabledIcon(logoIcon);
      logoIconItem.setFocusPainted(false);
      logoIconItem.setBorder(BorderFactory.createLoweredBevelBorder());
      queryFrameMenuBar.add(logoIconItem);
   }
   
   //==============================================================
   // Private method used for creation of the tool bar that will be
   // used with the frame.
   //==============================================================

   private void createToolBar(JToolBar queryFrameToolBar)
   {
      // Method Instances
      String resource;
      
      ImageIcon printIcon, pageFormatIcon, exitIcon;
      ImageIcon openScriptIcon, saveScriptIcon, tableRowsIcon;
      ImageIcon csvExportTabSummaryTableIcon, pdfExportTabSummaryTableIcon;
      JButton buttonItem;
      
      // ===============
      // File Menu
      
      // Open Script
      openScriptIcon = resourceBundle.getResourceImage(iconsDirectory + "openScriptIcon_20x20.png");
      resource = resourceBundle.getResourceString("QueryFrame.tooltip.OpenScript", "Open Script");
      buttonItem = buttonItem(resource, openScriptIcon, FILE_OPEN_SCRIPT);
      queryFrameToolBar.add(buttonItem);
      
      // Save Script
      saveScriptIcon = resourceBundle.getResourceImage(iconsDirectory + "saveScriptIcon_20x20.png");
      resource = resourceBundle.getResourceString("QueryFrame.tooltip.SaveScript", "Save Script");
      buttonItem = buttonItem(resource, saveScriptIcon, FILE_SAVE_SCRIPT);
      queryFrameToolBar.add(buttonItem);
      
      // File Print
      printIcon = resourceBundle.getResourceImage(iconsDirectory + "printIcon_20x20.png");
      resource = resourceBundle.getResourceString("QueryFrame.tooltip.Print", "Print");
      buttonItem = buttonItem(resource, printIcon, FILE_PRINT);
      queryFrameToolBar.add(buttonItem);
      
      // Page Format
      pageFormatIcon = resourceBundle.getResourceImage(iconsDirectory + "pageFormatIcon_20x20.png");
      resource = resourceBundle.getResourceString("QueryFrame.tooltip.PageFormat", "Page Format");
      buttonItem = buttonItem(resource, pageFormatIcon, FILE_PAGE_FORMAT);
      queryFrameToolBar.add(buttonItem);
      
      // Exit
      exitIcon = resourceBundle.getResourceImage(iconsDirectory + "exitIcon_20x20.png");
      resource = resourceBundle.getResourceString("QueryFrame.tooltip.Exit", "Exit");
      buttonItem = buttonItem(resource, exitIcon, FILE_EXIT);
      queryFrameToolBar.add(buttonItem);
      
      queryFrameToolBar.addSeparator();
      
      // ===============
      // Edit Menu
      
      // Preferences Table Rows
      tableRowsIcon = resourceBundle.getResourceImage(iconsDirectory + "tableRowsIcon_20x20.png");
      resource = resourceBundle.getResourceString("QueryFrame.tooltip.TableRows", "Table Rows");
      buttonItem = buttonItem(resource, tableRowsIcon, EDITPREFERENCES_TABLE_ROWS);
      queryFrameToolBar.add(buttonItem);
      
      queryFrameToolBar.addSeparator();
      
      // ===============
      // Data Menu
      
      // Export CSV Summary Table
      csvExportTabSummaryTableIcon = resourceBundle.getResourceImage(iconsDirectory
                                                                   + "csvExportSummaryTableIcon_20x20.png");
      resource = resourceBundle.getResourceString("QueryFrame.tooltip.ExportCSVSummaryTable",
                                                  "Export CSV Tab Summary Table");
      buttonItem = buttonItem(resource, csvExportTabSummaryTableIcon, DATAEXPORT_CSV_SUMMARY_TABLE);
      queryFrameToolBar.add(buttonItem);
      
      // Export PDF Summary Table
      pdfExportTabSummaryTableIcon = resourceBundle.getResourceImage(iconsDirectory
                                                                   + "pdfExportSummaryTableIcon_20x20.png");
      resource = resourceBundle.getResourceString("QueryFrame.tooltip.ExportPDFSummaryTable",
                                                  "Export PDF Tab Summary Table");
      buttonItem = buttonItem(resource, pdfExportTabSummaryTableIcon, DATAEXPORT_PDF_SUMMARY_TABLE);
      
      queryFrameToolBar.add(buttonItem);
   }

   //==============================================================
   // Protected instance method used for the MyJSQLView's
   // creation of menu bar items. Helper Method.
   //==============================================================

   private JMenuItem menuItem(String label, String actionLabel)
   {
      JMenuItem item = new JMenuItem(label);
      item.addActionListener(this);
      item.setActionCommand(actionLabel);
      return item;
   }
   
   //==============================================================
   // Instance method used for the MyJSQLView's creation of tool
   // bar button items. Helper Method.
   //==============================================================

   private JButton buttonItem(String toolTip, ImageIcon icon, String actionLabel)
   {
      JButton item = new JButton(icon);
      item.setFocusable(false);
      item.setMargin(new Insets(0, 0, 0, 0));
      item.setToolTipText(toolTip);
      item.setActionCommand(actionLabel);
      item.addActionListener(this);
      
      return item;
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
   // Class method to provide a mechanism to clear the
   // queryResultTextArea.
   //==============================================================

   public static void clearQueryResultTextArea()
   {
      queryResultTextArea.setText("");
   }

   //==============================================================
   // Class Method to return the current selected tab, in the
   // main center panel.
   //==============================================================
   
   public static Object getSelectedTab()
   {
      Object currentTab = queryTabsPane.getSelectedComponent();
      
      if (currentTab instanceof QueryTabPanel)
         return (QueryTabPanel) currentTab;
      else if (currentTab instanceof SQLTabPanel)
         return (SQLTabPanel) currentTab;
      else
         return null;
   }
   
   //==============================================================
   // Class Method to return the current selected tab titel, in
   // the main center panel.
   //==============================================================

   public static String getSelectedTabTitle()
   {
      return queryTabsPane.getTitleAt(queryTabsPane.getSelectedIndex());
   }

   //==============================================================
   // Class method to provide a mechanism to place resultset
   // feedback or query warnings into the queryResultTextArea.
   //==============================================================

   public static void setQueryResultTextArea(String feedback)
   {
      queryResultTextArea.append(feedback + "\n");
   }
}
