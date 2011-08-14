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
// Copyright (C) 2005-2011 Dana M. Proctor
// Version 6.9 08/14/2011
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
//         6.4 02/05/2011 Class Method actionPerformed() rowSizeDialog.pack() Instead of Sized.
//         6.5 04/27/2011 Class Instance queryTextArea.setDragEnabled(true) in Constructor.
//         6.6 06/11/2011 Removed Class Instance subProtocol and Replaced With dataSourceType.
//                        Derivation in Constructor From ConnectionManager.getDataSourceType().
//                        Effected queryFrameListener, & Method actionPerformed().
//         6.7 06/19/2011 Class Method actionPerformed() and queryFrameListener Change
//                        in Conditional for HSQL to Check for indexOf Instead of equals.
//                        Added Static Class Instances to Handle the Action Events From the
//                        Menu/ToolBar.
//         6.8 08/08/2011 Class Instance queryTabPanel Changed to tabPanel and Class JPanel.
//                        Removed Class Instance newQueryJButton and Renamed excuteQueryJButton
//                        to executeButton. Added Static Class Instance SQL_STATEMENT_TYPE.
//                        Removed Constructor Instance queryLabel, Added statementTypeComboBox
//                        & newTabCheckBox. Class Methods actionPerformed() and stateChanged()
//                        Corrected Processing for Changed Instances and Add SQLTabPanel
//                        Behavior. Added rowSizeLimit Setting in rowSizeTextField.
//         6.9 08/14/2011 Removed Argument currentQueryIndex from SQLTabPanel Class Instantiation.
//                        Added Correct getSelectedTab() for SQLTabPanel and QueryTabPanel. Also
//                        the Same for Processing. All in actionPerformed() Besides Noted Method.
//                   
//-----------------------------------------------------------------
//                danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview;

import java.awt.*;
import java.awt.event.*;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.DefaultEditorKit;

//=================================================================
//                     MyJSQLView QueryFrame
//=================================================================

/**
 *    The QueryFrame class is used to provide a framework to execute
 * queries on the current selected host by the user that has a
 * connection established in MyJSQLView.
 * 
 * @author Dana M. Proctor
 * @version 6.9 08/14/2011
 */

class QueryFrame extends JFrame implements ActionListener, ChangeListener
{
   // =============================================
   // Creation of the necessary class instance
   // variables for the QueryFrame.
   // =============================================

   private static final long serialVersionUID = -4085953364431028570L;

   private static JTabbedPane queryTabsPane = new JTabbedPane();
   private JPanel tabPanel;

   private int maxTabs = 50;
   private int currentQueryIndex = 0;
   private int oldQueryIndex = currentQueryIndex;
   private boolean clearingTabs = false;

   private JCheckBoxMenuItem showQueryCheckBox;
   private transient Connection query_dbConnection;

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

   QueryFrame()
   {
      // Constructor Instances.
      JMenuBar queryFrameMenuBar;
      JToolBar queryFrameToolBar;
      
      JPanel framePanel, mainPanel, queryPanel, buttonPanel;
      JPanel centerPanel, queryResultPanel;
      
      ConnectionProperties connectionProperties;
      String hostName, databaseName, resource;
      
      // Setting up title, and other instances.
      
      resourceBundle = MyJSQLView.getLocaleResourceBundle();
      
      connectionProperties = ConnectionManager.getConnectionProperties();
      dataSourceType = ConnectionManager.getDataSourceType();
      hostName = connectionProperties.getProperty(ConnectionProperties.HOST);
      databaseName = connectionProperties.getProperty(ConnectionProperties.DB);
      
      resource = resourceBundle.getResource("QueryFrame.message.Title");
      if (resource.equals(""))
         setTitle("MyJSQLView Query Frame   " + hostName + ":" + databaseName);
      else
         setTitle("MyJSQLView " + resource + "   " + hostName + ":" + databaseName);
      
      resource = resourceBundle.getResource("QueryFrame.dialogtitle.Alert");
      if (resource.equals(""))
         resourceAlert = "Alert";
      else
         resourceAlert = resource;
      
      resource = resourceBundle.getResource("QueryFrame.dialogmessage.FileNOTFound");
      if (resource.equals(""))
         resourceFileNOTFound = "File NOT Found";
      else
         resourceFileNOTFound = resource;
      
      fileSeparator = MyJSQLView_Utils.getFileSeparator();
      iconsDirectory = MyJSQLView_Utils.getIconsDirectory() + fileSeparator;
      lastDirectory = "";

      for (int i = 0; i < maxTabs; i++)
         summaryTableRowSize[i] = 50;

      // Setting up a connection.
      query_dbConnection = (Connection) ConnectionManager.getConnection("QueryFrame");
      
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
      // QueryFrame Entry TextArea

      queryPanel = new JPanel();
      queryPanel.setBorder(BorderFactory.createEtchedBorder());
      
      statementTypeComboBox = new JComboBox();
      
      // SQL_STATEMENT_TYPE:0
      resource = resourceBundle.getResource("QueryFrame.combobox.SQLStatement");
      if (resource.equals(""))
         statementTypeComboBox.addItem("SQL Statement : ");
      else
         statementTypeComboBox.addItem(resource + " : ");
      
      queryPanel.add(statementTypeComboBox);
      
      // QUERY_STATEMENT_TYPE:1
      resource = resourceBundle.getResource("QueryFrame.combobox.QueryStatement");
      if (resource.equals(""))
         statementTypeComboBox.addItem("Query Statement : ");
      else
         statementTypeComboBox.addItem(resource + " : ");
      
      queryTextArea = new JTextArea(4, 40);
      queryTextArea.setBorder(BorderFactory.createLoweredBevelBorder());
      queryTextArea.setLineWrap(true);
      queryTextArea.setDragEnabled(true);
      queryPanel.add(queryTextArea);

      buttonPanel = new JPanel();
      buttonPanel.setLayout(new GridLayout(2, 1, 4, 8));

      resource = resourceBundle.getResource("QueryFrame.button.Execute");
      if (resource.equals(""))
         executeButton = new JButton("Execute");
      else
         executeButton = new JButton(resource);
      executeButton.setMnemonic(KeyEvent.VK_ENTER);
      executeButton.addActionListener(this);
      buttonPanel.add(executeButton);

      resource = resourceBundle.getResource("QueryFrame.checkbox.NewTab");
      if (resource.equals(""))
         newTabCheckBox = new JCheckBox("New Tab", true);
      else
         newTabCheckBox = new JCheckBox(resource, true);
      
      newTabCheckBox.setIcon(new ImageIcon(iconsDirectory + "limitUpIcon.png"));
      newTabCheckBox.setSelectedIcon(new ImageIcon(iconsDirectory + "limitDownIcon.png"));
      newTabCheckBox.setMargin(new Insets(4, 1, 4, 1));
      newTabCheckBox.setBorder(BorderFactory.createRaisedBevelBorder());
      newTabCheckBox.setFocusPainted(false);
      
      buttonPanel.add(newTabCheckBox);

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

      // QueryFrame SQL Feedback TextArea.

      queryResultPanel = new JPanel(new GridLayout(1, 1, 0, 0));
      queryResultPanel.setBorder(BorderFactory.createEtchedBorder());

      queryResultTextArea.setBorder(BorderFactory.createLoweredBevelBorder());
      queryResultTextArea.setLineWrap(true);

      JScrollPane resultScrollPane = new JScrollPane(queryResultTextArea);
      queryResultPanel.add(resultScrollPane);

      mainPanel.add(queryResultPanel, BorderLayout.SOUTH);
      
      framePanel.add(mainPanel, BorderLayout.CENTER);

      getContentPane().add(framePanel);
      getContentPane().addMouseListener(MyJSQLView.getPopupMenuListener());

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
      int fileChooserResult;
      String resource, resourceOK, resourceCancel, message;

      // Button Actions
      if (panelSource == executeButton)
      {
         queryTabsPane.removeChangeListener(this);
         
         // Execute query action.
         if (panelSource == executeButton && query_dbConnection != null)
         {
            // Lets clear any left over query errors.
            queryResultTextArea.setText("");

            // Get tab index to use.
            if (newTabCheckBox.isSelected())
            {
               oldQueryIndex = currentQueryIndex;
               currentQueryIndex = queryTabsPane.getTabCount();
            }
            else
            {
               if (queryTabsPane.getSelectedComponent() != null)
                  queryTabsPane.remove(currentQueryIndex);
            }
            
            // SQL Statement
            if (statementTypeComboBox.getSelectedIndex() == SQL_STATEMENT_TYPE)
            {
               tabPanel = new SQLTabPanel(queryTextArea.getText(),
                                          summaryTableRowSize[currentQueryIndex],
                                          resourceBundle);
            }
            // Query Statement
            else
            {
               tabPanel = new QueryTabPanel(currentQueryIndex + "",
                                            queryTextArea.getText(),
                                            showQueryCheckBox.isSelected(),
                                            summaryTableRowSize[currentQueryIndex],
                                            query_dbConnection, resourceBundle);
            }
            if (newTabCheckBox.isSelected())
               queryTabsPane.addTab(currentQueryIndex + "", tabPanel);
            else
               queryTabsPane.insertTab(currentQueryIndex + "", null, tabPanel,
                                       "", currentQueryIndex);
            queryTabsPane.setSelectedIndex(queryTabsPane.indexOfTab(currentQueryIndex + ""));

            // Save text and statement type.
            queryTextAreaData[currentQueryIndex] = queryTextArea.getText();
            tabStatementType[currentQueryIndex] = statementTypeComboBox.getSelectedIndex();
         }
         
         queryTabsPane.addChangeListener(this);
         newTabCheckBox.setSelected(false);
      }

      // MenuBar Actions
      if (panelSource instanceof JMenuItem || panelSource instanceof JButton)
      {
         // Instances & Setting Up.
         String fileName, actionCommand;

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
            // Choosing the file to import data from.
            JFileChooser openScriptFileChooser;
            
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

                     while ((currentLine = bufferedReader.readLine()) != null && lineNumber < 100)
                     {
                        queryTextArea.append(currentLine);
                        lineNumber++;
                     }
                     bufferedReader.close();
                     fileReader.close();
                  }
                  catch (IOException e)
                  {
                     resource = resourceBundle.getResource("QueryFrame.dialogmessage.InputFile");
                     if (resource.equals(""))
                        message = "Unable to Read Input File!";
                     else
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

         // Save Script
         if (actionCommand.equals(FILE_SAVE_SCRIPT))
         {  
            // Setup a file chooser and showing.
            JFileChooser saveScriptFileChooser;
            
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

         // Print
         if (actionCommand.equals(FILE_PRINT))
         {
            // Setting up the printing.

            Paper customPaper = new Paper();
            double margin = 36;
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

               // Should have graphics to print now so
               // lets try to print.
               if (currentPrintJob.printDialog())
               {
                  try
                  {
                     currentPrintJob.print();
                  }
                  catch (PrinterException e)
                  {
                     resource = resourceBundle.getResource("QueryFrame.dialogmessage.PrinterException");
                     if (resource.equals(""))
                        message = "Printer Exception";
                     else
                        message = resource;
                     
                     JOptionPane.showMessageDialog(null, message, e.getMessage(),
                                                   JOptionPane.ERROR_MESSAGE);
                  }
               }
            }
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
               new TableClearingThread(queryTabsPane.getTabCount());

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
            // Setup and display a option pane to collect the new
            // summary table row size.

            JTextField rowSizeTextField;
            JLabel warning, warningMessage1, warningMessage2;
            
            rowSizeTextField = new JTextField();
            if (currentQueryIndex <= summaryTableRowSize.length)
               rowSizeTextField.setText(Integer.toString(summaryTableRowSize[currentQueryIndex]));
            
            resource = resourceBundle.getResource("QueryFrame.label.Warning");
            if (resource.equals(""))
               warning = new JLabel("Warning!", JLabel.CENTER);
            else
               warning = new JLabel(resource, JLabel.CENTER);
            warning.setForeground(Color.RED);
            
            resource = resourceBundle.getResource("QueryFrame.label.LargeRowSize");
            if (resource.equals(""))
               warningMessage1 = new JLabel("A large row size may adversely effect", JLabel.CENTER);
            else
               warningMessage1 = new JLabel(resource, JLabel.CENTER);
            
            resource = resourceBundle.getResource("QueryFrame.label.ApplicationServerPerformance");
            if (resource.equals(""))
               warningMessage2 = new JLabel("application/server performance.", JLabel.CENTER);
            else
               warningMessage2 = new JLabel(resource, JLabel.CENTER);

            Object content[] = {warning, warningMessage1, warningMessage2, rowSizeTextField};

            resource = resourceBundle.getResource("QueryFrame.label.SetSummaryTableRowSize");
            if (resource.equals(""))
               resource = "Set Summary Table Row Size";
            
            resourceOK = resourceBundle.getResource("QueryFrame.button.OK");
            if (resourceOK.equals(""))
               resourceOK = "OK";
            
            resourceCancel = resourceBundle.getResource("QueryFrame.button.Cancel");
            if (resourceCancel.equals(""))
               resourceCancel = "Cancel";
            
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
                     ((SQLTabPanel) currentTab).setTableRowSize(summaryTableRowSize[currentQueryIndex]);
                  
                  else { /* Something not right. */}
               }
               catch (NumberFormatException e)
               {
                  resource = resourceBundle.getResource("QueryFrame.dialogmessage.RowSize");
                  if (resource.equals(""))
                     message = "The Row Size Input Appears To NOT Be A Valid Integer!";
                  else
                     message = resource;
                  
                  JOptionPane.showMessageDialog(null, message, resourceAlert, JOptionPane.ERROR_MESSAGE);
               }
            }
         }

         // ==================================
         // Data Menu Item Selection Routing
         // ==================================

         // Data Export
         if (getSelectedTab() != null
             && (actionCommand.indexOf("DECSV") != -1 || actionCommand.indexOf("DEPDF") != -1
                 || actionCommand.indexOf("DESQL") != -1))
         {
            String exportedTable;
            HashMap<String, String> tableColumnNamesHashMap = new HashMap <String, String>();
            HashMap<String, String> tableColumnClassHashMap = new HashMap <String, String>();
            HashMap<String, String> tableColumnTypeHashMap = new HashMap <String, String>();
            HashMap<String, Integer> tableColumnSizeHashMap = new HashMap <String, Integer>();

            // Setting up a default file name based on the selected
            // database, or table and date.

            JFileChooser exportData = new JFileChooser();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            
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
                  Vector<String> columnNameFields = new Vector <String>();
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

         // Save the query text string.
         if (oldQueryIndex != currentQueryIndex)
         {
            queryTextArea.setText(queryTextAreaData[currentQueryIndex]);
            statementTypeComboBox.setSelectedIndex(tabStatementType[currentQueryIndex]);
         }
         // System.out.println("tab changed: " + currentQueryIndex);
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
      
      resource = resourceBundle.getResource("QueryFrame.menu.File");
      if (resource.equals(""))
         fileMenu = new JMenu("File");
      else
         fileMenu = new JMenu(resource);
      fileMenu.setFont(fileMenu.getFont().deriveFont(Font.BOLD));
      
      resource = resourceBundle.getResource("QueryFrame.menu.OpenScript");
      if (resource.equals(""))
         fileMenu.add(menuItem("Open Script", FILE_OPEN_SCRIPT));
      else
         fileMenu.add(menuItem(resource, FILE_OPEN_SCRIPT));
      
      resource = resourceBundle.getResource("QueryFrame.menu.SaveScript");
      if (resource.equals(""))
         fileMenu.add(menuItem("Save Script", FILE_SAVE_SCRIPT));
      else
         fileMenu.add(menuItem(resource, FILE_SAVE_SCRIPT));
      fileMenu.addSeparator();
      
      resource = resourceBundle.getResource("QueryFrame.menu.Print");
      if (resource.equals(""))
         fileMenu.add(menuItem("Print", FILE_PRINT));
      else
         fileMenu.add(menuItem(resource, FILE_PRINT));
      
      resource = resourceBundle.getResource("QueryFrame.menu.PageFormat");
      if (resource.equals(""))
         fileMenu.add(menuItem("Page Format", FILE_PAGE_FORMAT));
      else
         fileMenu.add(menuItem(resource, FILE_PAGE_FORMAT));
      fileMenu.addSeparator();
      
      resource = resourceBundle.getResource("QueryFrame.menu.Exit");
      if (resource.equals(""))
         fileMenu.add(menuItem("Exit", FILE_EXIT));
      else
         fileMenu.add(menuItem(resource, FILE_EXIT));
      queryFrameMenuBar.add(fileMenu);

      // ===============
      // Edit Menu
      
      resource = resourceBundle.getResource("QueryFrame.menu.Edit");
      if (resource.equals(""))
         editMenu = new JMenu("Edit");
      else
         editMenu = new JMenu(resource);
      editMenu.setFont(editMenu.getFont().deriveFont(Font.BOLD));
      
      menuItem = new JMenuItem(new DefaultEditorKit.CutAction());
      resource = resourceBundle.getResource("QueryFrame.menu.Cut");
      if (resource.equals(""))
         menuItem.setText("Cut" + "          " + "Ctrl+x");
      else
         menuItem.setText(resource + "          " + "Ctrl+x");
      menuItem.setMnemonic(KeyEvent.VK_X);
      editMenu.add(menuItem);

      menuItem = new JMenuItem(new DefaultEditorKit.CopyAction());
      resource = resourceBundle.getResource("QueryFrame.menu.Copy");
      if (resource.equals(""))
         menuItem.setText("Copy" + "       " + "Ctrl+c");
      else
         menuItem.setText(resource + "       " + "Ctrl+c");
      menuItem.setMnemonic(KeyEvent.VK_C);
      editMenu.add(menuItem);

      menuItem = new JMenuItem(new DefaultEditorKit.PasteAction());
      resource = resourceBundle.getResource("QueryFrame.menu.Paste");
      if (resource.equals(""))
         menuItem.setText("Paste" + "       " + "Ctrl+v");
      else
         menuItem.setText(resource + "       " + "Ctrl+v");
      menuItem.setMnemonic(KeyEvent.VK_V);
      editMenu.add(menuItem);

      editMenu.addSeparator();

      resource = resourceBundle.getResource("QueryFrame.menu.Preferences");
      if (resource.equals(""))
         preferencesMenu = new JMenu("Preferences");
      else
         preferencesMenu = new JMenu(resource);

      resource = resourceBundle.getResource("QueryFrame.menu.TableRows");
      if (resource.equals(""))
         preferencesMenu.add(menuItem("Table Rows", EDITPREFERENCES_TABLE_ROWS));
      else
         preferencesMenu.add(menuItem(resource, EDITPREFERENCES_TABLE_ROWS));

      resource = resourceBundle.getResource("QueryFrame.menu.ShowQuery");
      if (resource.equals(""))
         showQueryCheckBox = new JCheckBoxMenuItem("Show Query", false);
      else
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

      resource = resourceBundle.getResource("QueryFrame.menu.Data");
      if (resource.equals(""))
         dataMenu = new JMenu("Data");
      else
         dataMenu = new JMenu(resource);
      dataMenu.setFont(dataMenu.getFont().deriveFont(Font.BOLD));

      resource = resourceBundle.getResource("QueryFrame.menu.Export");
      if (resource.equals(""))
         exportMenu = new JMenu("Export");
      else
         exportMenu = new JMenu(resource);

      resource = resourceBundle.getResource("QueryFrame.menu.ExportCSV");
      if (resource.equals(""))
         exportCVSMenu = new JMenu("CSV");
      else
         exportCVSMenu = new JMenu(resource);
      
      /*
      resource = resourceBundle.getResource("QueryFrame.menu.CSVTable");
      if (resource.equals(""))
         exportCVSMenu.add(menuItem("Table", DATAEXPORT_CSV_TABLE));
      else
         exportCVSMenu.add(menuItem(resource, DATAEXPORT_CSV_TABLE));
      */
      
      resource = resourceBundle.getResource("QueryFrame.menu.CSVSummaryTable");
      if (resource.equals(""))
         exportCVSMenu.add(menuItem("Summary Table", DATAEXPORT_CSV_SUMMARY_TABLE));
      else
         exportCVSMenu.add(menuItem(resource, DATAEXPORT_CSV_SUMMARY_TABLE));
      exportMenu.add(exportCVSMenu);
      
      resource = resourceBundle.getResource("QueryFrame.menu.ExportPDF");
      if (resource.equals(""))
         exportCVSMenu = new JMenu("PDF");
      else
         exportCVSMenu = new JMenu(resource);
      
      resource = resourceBundle.getResource("QueryFrame.menu.PDFSummaryTable");
      if (resource.equals(""))
         exportCVSMenu.add(menuItem("Summary Table", DATAEXPORT_PDF_SUMMARY_TABLE));
      else
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
      
      logoIcon = new ImageIcon(iconsDirectory + "myjsqlviewIcon.gif");
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
      openScriptIcon = new ImageIcon(iconsDirectory + "openScriptIcon.png");
      resource = resourceBundle.getResource("QueryFrame.tooltip.OpenScript");
      if (resource.equals(""))
         buttonItem = buttonItem("Open Script", openScriptIcon, FILE_OPEN_SCRIPT);
      else
         buttonItem = buttonItem(resource, openScriptIcon, FILE_OPEN_SCRIPT);
      queryFrameToolBar.add(buttonItem);
      
      // Save Script
      saveScriptIcon = new ImageIcon(iconsDirectory + "saveScriptIcon.png");
      resource = resourceBundle.getResource("QueryFrame.tooltip.SaveScript");
      if (resource.equals(""))
         buttonItem = buttonItem("Save Script", saveScriptIcon, FILE_SAVE_SCRIPT);
      else
         buttonItem = buttonItem(resource, saveScriptIcon, FILE_SAVE_SCRIPT);
      queryFrameToolBar.add(buttonItem);
      
      // File Print
      printIcon = new ImageIcon(iconsDirectory + "printIcon.png");
      resource = resourceBundle.getResource("QueryFrame.tooltip.Print");
      if (resource.equals(""))
         buttonItem = buttonItem("Print", printIcon, FILE_PRINT);
      else
         buttonItem = buttonItem(resource, printIcon, FILE_PRINT);
      queryFrameToolBar.add(buttonItem);
      
      // Page Format
      pageFormatIcon = new ImageIcon(iconsDirectory + "pageFormatIcon.png");
      resource = resourceBundle.getResource("QueryFrame.tooltip.PageFormat");
      if (resource.equals(""))
         buttonItem = buttonItem("Page Format", pageFormatIcon, FILE_PAGE_FORMAT);
      else
         buttonItem = buttonItem(resource, pageFormatIcon, FILE_PAGE_FORMAT);
      queryFrameToolBar.add(buttonItem);
      
      // Exit
      exitIcon = new ImageIcon(iconsDirectory + "exitIcon.png");
      resource = resourceBundle.getResource("QueryFrame.tooltip.Exit");
      if (resource.equals(""))
         buttonItem = buttonItem("Exit", exitIcon, FILE_EXIT);
      else
         buttonItem = buttonItem(resource, exitIcon, FILE_EXIT);
      queryFrameToolBar.add(buttonItem);
      
      queryFrameToolBar.addSeparator();
      
      // ===============
      // Edit Menu
      
      // Preferences Table Rows
      tableRowsIcon = new ImageIcon(iconsDirectory + "tableRowsIcon.png");
      resource = resourceBundle.getResource("QueryFrame.tooltip.TableRows");
      if (resource.equals(""))
         buttonItem = buttonItem("Table Rows", tableRowsIcon, EDITPREFERENCES_TABLE_ROWS);
      else
         buttonItem = buttonItem(resource, tableRowsIcon, EDITPREFERENCES_TABLE_ROWS);
      queryFrameToolBar.add(buttonItem);
      
      queryFrameToolBar.addSeparator();
      
      // ===============
      // Data Menu
      
      // Export CSV Summary Table
      csvExportTabSummaryTableIcon = new ImageIcon(iconsDirectory + "csvExportSummaryTableIcon.png");
      resource = resourceBundle.getResource("QueryFrame.tooltip.ExportCSVSummaryTable");
      if (resource.equals(""))
         buttonItem = buttonItem("Export CSV Tab Summary Table", csvExportTabSummaryTableIcon,
                                 DATAEXPORT_CSV_SUMMARY_TABLE);
      else
         buttonItem = buttonItem(resource, csvExportTabSummaryTableIcon, DATAEXPORT_CSV_SUMMARY_TABLE);
      queryFrameToolBar.add(buttonItem);
      
      // Export PDF Summary Table
      pdfExportTabSummaryTableIcon = new ImageIcon(iconsDirectory + "pdfExportSummaryTableIcon.png");
      resource = resourceBundle.getResource("QueryFrame.tooltip.ExportPDFSummaryTable");
      if (resource.equals(""))
         buttonItem = buttonItem("Export PDF Tab Summary Table", pdfExportTabSummaryTableIcon,
                                 DATAEXPORT_PDF_SUMMARY_TABLE);
      else
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
   // Class method to provide a mechanism to clear the
   // queryResultTextArea.
   //==============================================================

   protected static void clearQueryResultTextArea()
   {
      queryResultTextArea.setText("");
   }

   //==============================================================
   // Class Method to return the current selected tab, in the
   // main center panel.
   //==============================================================
   
   protected static Object getSelectedTab()
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

   protected static String getSelectedTabTitle()
   {
      return queryTabsPane.getTitleAt(queryTabsPane.getSelectedIndex());
   }

   //==============================================================
   // Class method to provide a mechanism to place resultset
   // feedback or query warnings into the queryResultTextArea.
   //==============================================================

   protected static void setQueryResultTextArea(String feedback)
   {
      queryResultTextArea.append(feedback + "\n");
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
}