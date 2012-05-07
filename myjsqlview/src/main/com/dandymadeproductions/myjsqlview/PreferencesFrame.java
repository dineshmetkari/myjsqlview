//=================================================================
//                 MyJSQLView Preferences Frame.
//=================================================================
//   This class provides the mechanism in the MyJSQLView application
// to create a preferences frame for setting properties.
//
//             << PreferencesFrame.java >>
//
//=================================================================
// Copyright (C) 2007-2012 Dana M. Proctor
// Version 8.6 05/07/2012
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
// Version 1.0 Original MyJSQLView PreferencesFrame Class.
//         1.1 Added TableFieldSelectionPanel.
//         1.2 Completely Revamped Table Field Preferences Panel.
//         1.3 Code Cleanup and Comments Added.
//         1.4 Modified Some of the Panel Borders.
//         1.5 Implemented the TableRowSelectionpanel.
//         1.6 Corrections After Changing the Major Panel Names.
//         1.7 Implemented the SQLExportPreferencesPanel.
//         1.8 Added preferencesFrameLisenter, WindowAdapter.
//         1.9 Optimized the Creation of the TableRow/FieldSelectionPanels.
//         2.0 Ok Button Action Made Sure Both DataDelimiter and SQL
//             Export Preferences Saved.
//         2.1 Ok Button Action Made Sure Both Table Fields and Table
//             Rows Preferences Saved. All Functionality in Place.
//             Class Instances tableField/RowPanelsHashtable.
//         2.2 Class Instance preferencesPanel Type PreferencesPanel.
//             Also Tree LeafIcon.
//         2.3 Cleaned Up. Added Class Methods createTableFieldsOptionPanel()
//             & createTableRowsOptionPanel and Change Data Node to
//             Data Export.
//         2.4 Correction to dataPanel Label and Name, Data Export.
//         2.5 Panel Name Changes. HelpFrame helpCloseButton Argument.
//             Class Name Changes for SQLDataExportProperties to
//             DataExportProperties and CSVDelimiterPreferencesPanel
//             to CSVExportPreferencesPanel.
//         2.6 Changed in Class Method actionPerformed() in the Way
//             DataExportProperties Set.
//         2.7 Class Instance currentPreferencesSelectionLabel Font Setting.
//         2.8 Corrected References to CSV.
//         2.9 Added the DataPreferencesPanel & TableSummaryViewPreferencesPanel.
//         3.0 Removed Constructor Instance JPanel tableSummaryViewPanel.
//         3.1 Moved Instance preferencesTopPanel to Class Instance and
//             Controlled its Runnable Thread as Needed in Class Method
//             valueChanged().
//         3.2 Used System.getProperty("file.separator") for All File System
//             Resources Accesses Through Instance fileSeparator.
//         3.3 Added Class Methods windowDeiconified() & windowIconified()
//             to preferencesFrameListener, to Control the PreferencesPanel
//             Thread.
//         3.4 Suspended PreferencesPanel Thread on windowClosing(), okButton
//             & cancelButton Actions.
//         3.5 Set Class Instance currentPreferencesSelectionLabel to 
//             "Preferences" on Initialization. Also Changed Class Name
//             PreferencesPanel to PreferencesPanelSummer.
//         3.6 Corrected Class Instance fileSeparator Declaration.
//         3.7 Removed fileSeparator helpButton Action in Class Method
//             actionPerformed().
//         3.8 Cleaned Up Javadoc Comments.
//         3.9 Class Instance PreferencesTopPanel Changed to Type Object
//             PreferencesPanel. Set Up Season Change for This Top
//             Panel.
//         4.0 Activated preferencesPanelTop Seasonal Selection. For Now
//             Winter Panel Covers Fall & Winter. Summer Panel Covers
//             Spring & Summer.
//         4.1 Added CSVImportPreferencesPanel and Proper Detection
//             Scheme in Class Method valueChanged(). Renamed Some of the
//             Data Export Instances.
//         4.2 Header Update.
//         4.3 Comment Changes.
//         4.4 Removed Reference to MySQL in Instance rowSizeWarningMessage
//             in Class Method createTableRowOptionsPanel().
//         4.5 Class Instance tableFieldPreferences Obtained From Current
//             Tab getTableHeadings() Instead of getTableFields().
//         4.6 TableTabPanel Method Fields Derived from getAllTableHeadings().
//             Class Methods actionPerformed() & createTableFieldsOptions().
//         4.7 Redid the Season Changes for the Top Panel, preferencesPanelTop.
//             Added Early Spring.
//         4.8 Added Class Instance serialVersionUID. Declared WindowListner
//             preferencesFrameListener private transient.
//         4.9 Added Season Change PreferencesPanelFall, for Months October-
//             November in Constructor.
//         5.0 Tested All The Seasonal Changes Logic For the PreferencesPanels.
//             Corrected the Conditional for Winter. Still Need Spring.
//         5.1 Removed System.out From Constructor Seasonal Conditional Summer.
//         5.2 Replace MyJSQLView.getTab(index) With DBTablesPanel.
//             getTableTabPanel(tableName). TableFieldSelectionPreferencesPanel
//             & TableRowSelectionPreferencesPanel Class Instantiation
//             Argument Changed to tableName String. Removed Instance index.
//             Added DBTablesPanel.showTables() To Changes in tableSelectionComboBox
//             & tableSelectionComboBox_2.
//         5.3 MyJSQLView Project Common Source Code Formatting.
//         5.4 Additional Standardation to Comments.
//         5.5 Added Constructor Instance iconsDirectory.
//         5.6 Class Method actionPerformed() getDataImport/ExportProperites
//             Changed Over to the MyJSQLView_Frame Class.
//         5.7 Changed DBTablesPanel.show() Call to showTableTabPanel() in Class
//             Method actionPerformed(). Also Fault Tolerance for TableTabPanel
//             Retrievals From the DBTablesPanel.
//         5.8 Added Comment.
//         5.9 Changed MyJSQLView_Frame.getDatabaseImport/ExportProperties() Method.
//             Moved Over to the DBTablesPanel.
//         6.0 Conditional Check for NULL in createTableRowsOptionPanel() and
//             createTableFieldsOptionPanel() for tableName and currentTabPanel.
//         6.1 Changed showTableTabPanel() to setSelectedTableTabPanel() in
//             actionPerformed() for the DBTableTabPanel.
//         6.2 Moved the Adding of ActionListtener to tableSelectionComboBoxes for
//             Rows and Fields to After createTableFields/RowsOptionPanel(). Changed
//             the Layout for selectionPanel in These Same Methods to GridLayout.
//         6.3 Commented System.out.println() in Inner Class preferencesFrameListener.
//         6.4 Changed Preferences Panel for Spring to PreferencesPanelSpring.
//         6.5 Header Format Changes/Update.
//         6.6 Control of CSVImportPreferencesPanel Filler Thread in Class Methods
//             Window Listener, actionPerformed(), & valueChanged().
//         6.7 Several Corrections to Insure csvImportPanel's Thread Properly Controlled
//             During Various Node Selection, Cancel, and Window Iconification.
//         6.8 Set csvImportPanelFiller.setThreadAction(true), in Constructor to 
//             Suspend the Thread After Creation.
//         6.9 Removed Constructor fileSeparator Instance, Obtained It and iconsDirectory
//             From MyJSQLView_Utils.
//         7.0 Changed Package to Reflect Dandy Made Productions Code.
//         7.1 Implementation of Internationalization Through Class Instance resourceBundle
//             in Constructor & Methods valueChanged(), createTreeNodes(), createTableField
//             OptionsPanel(), & createTableRowsOptionPanel(). Added Class Method
//             createNodeResourceNames().
//         7.2 Added Required Argument Instance resourceBundle to CSVImportPreferencesPanel,
//             CSVExportPreferencesPanel, SQLExportPreferencesPanel,
//             TableFieldSelectionPreferencesPanel & TableRowSelectionPreferencesPanel.
//         7.3 Parameterized Class Instances tableFieldCards, tableFieldPanelsHashtable,
//             tableRowsCards, & tableRowPanelsHashtable in Order for Code to Comply
//             With Java 5.0 API.
//         7.4 Parameterized cardsIterator in Class Method actionPerformed().
//         7.5 Addition of the PDFExportPreferencesPanel to Control PDF Export Options.
//             New Class Instance pdfExportPanel, Changes in Constructor, actionPerformed(),
//             & createTreeNodes(DefaultMutableTreeNode().
//         7.6 Spelling Corrections and Cleanup.
//         7.7 Changed Constructor Instances  dataImportPreferencesPanel, summaryPreferencesPanel,
//             & dataExportPreferencesPanel to Type GraphicCanvasPanel.
//         7.8 Removed the Setting of the preferencesTopPanel Border in the Constructor.
//         7.9 Added the General Options Node. Added Class Instances generalPreferencesPanel,
//             resourceGeneral, resourceGeneralOptions. Effected Class Methods actionPerformed(),
//             Constructor, createNodeResourceNames(), & createTreeNodes().
//         8.0 Class Methods createTableFieldsOptionPanel() & createTableRowsOptionPanel()
//             Table Names Obtained From New ConnectionManager Class.
//         8.1 Increased Width of treeScrollPane Preferred Size in Constructor.
//         8.2 Changes to Pass TableTabPanel Argument to TableFieldSelectionPreferencesPanel
//             in actionPerformed() & createTableFieldsOptionPanel(). Removed Ref. to
//             DBTablesPanel.Start/StopStatusTimer() and Reloaded Selected Table in actionPerformed()
//             on OK.
//         8.3 Copyright Update.
//         8.4 Implemented the Control of the generalOptionsPanelFiller Thread in Constructor,
//             actionPerformed(), valueChanged(), & Window Events by Suspending or Set Thread
//             State.
//         8.5 Suspended generalPreferencesPanel.generalOptionsPanelFiller on Cancel
//             Action in actionPerformed().
//         8.6 Changed Class Instances tableFieldCards & tableRowCards From Vector Data
//             Type to ArrayList. Changed Class Instances tableFieldPanelsHashtable &
//             tableRowPanelsHashtable from HashTable to HashMap & Name Accordingly to
//             End With Map.
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeSelectionModel;

/**
 *    The PreferencesFrame class provides the mechanism in the MyJSQLView
 * application to create a preferences frame for setting properties.
 * 
 * @author Dana M. Proctor
 * @version 8.6 05/07/2012
 */

//=================================================================
//                   MyJSQLView PreferencesFrame
//=================================================================

class PreferencesFrame extends JFrame implements ActionListener, TreeSelectionListener
{
   //=============================================
   // Creation of the necessary class instance
   // variables for the JFrame.
   //=============================================

   private static final long serialVersionUID = 2720784368480735754L;

   private JTree preferencesTree;
   private JPanel mainPanel, optionsPanel, tableFieldsPanel, tableRowsPanel;
   private CardLayout centerCardLayout, tableFieldCardLayout, tableRowCardLayout;

   private PreferencesPanel preferencesTopPanel;
   private GeneralPreferencesPanel generalPreferencesPanel;
   private TableFieldSelectionPreferencesPanel tableFieldPreferences;
   private TableRowSelectionPreferencesPanel tableRowPreferences;
   private CSVImportPreferencesPanel csvImportPanel;
   private CSVExportPreferencesPanel csvExportPanel;
   private PDFExportPreferencesPanel pdfExportPanel;
   private SQLExportPreferencesPanel sqlExportPanel;
   private MyJSQLView_ResourceBundle resourceBundle;
   private String resourcePreferences;
   private String resourceGeneral, resourceGeneralOptions;
   private String resourceTableSummaryView, resourceTableFields, resourceTableRows;
   private String resourceDataImport, resourceImport, resourceDataExport, resourceExport;
   private String tableName, iconsDirectory;
   private boolean helpShowing;

   private JLabel currentPreferencesSelectionLabel;
   private JComboBox tableSelectionFieldsComboBox, tableSelectionRowsComboBox;
   private ArrayList<String> tableFieldCards, tableRowCards;
   private HashMap<String, TableFieldSelectionPreferencesPanel> tableFieldPanelsHashMap;
   private HashMap<String, TableRowSelectionPreferencesPanel> tableRowPanelsHashMap;
   private JButton okButton, cancelButton, helpButton, helpCloseButton;

   //==============================================================
   // PreferencesFrame Constructor
   //==============================================================

   protected PreferencesFrame()
   {
      // Constructor Instances.
      DefaultMutableTreeNode top;
      JPanel treePreferencesSelectionPanel, centerPanel;
      GraphicsCanvasPanel generalPanel, summaryPreferencesPanel;
      GraphicsCanvasPanel dataImportPreferencesPanel, dataExportPreferencesPanel;
      JPanel tableFieldPanel;
      JPanel tableRowPanel;
      JPanel southButtonPanel;
      String resource;
      Calendar calendar;
      int currentMonth;

      String fontName;
      Font titleFont;
      int fontSize;
      ImageIcon leafIcon;

      // Setting up resources and icons directory.
      
      resourceBundle = MyJSQLView.getLocaleResourceBundle();
      createNodeResourceNames();
      iconsDirectory = MyJSQLView_Utils.getIconsDirectory() + MyJSQLView_Utils.getFileSeparator();
      
      // Setting the frame's main layout & other required
      // fields.
      
      resource = resourceBundle.getResource("PreferencesFrame.message.Title");
      if (resource.equals(""))
         setTitle("Preferences");
      else
         setTitle(resource);
      
      mainPanel = new JPanel(new BorderLayout());

      tableFieldCards = new ArrayList <String>();
      tableRowCards = new ArrayList <String>();

      tableFieldPanelsHashMap = new HashMap <String, TableFieldSelectionPreferencesPanel>();
      tableRowPanelsHashMap = new HashMap <String, TableRowSelectionPreferencesPanel>();

      // =====================================
      // Creating the tree preferences panel.

      treePreferencesSelectionPanel = new JPanel();
      treePreferencesSelectionPanel.setBorder(BorderFactory.createRaisedBevelBorder());

      top = new DefaultMutableTreeNode(resourcePreferences);
      createTreeNodes(top);

      preferencesTree = new JTree(top);
      preferencesTree.setBorder(BorderFactory.createLoweredBevelBorder());
      preferencesTree.setEditable(false);
      preferencesTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

      // Setting the leaf icon for nodes.
      leafIcon = new ImageIcon(iconsDirectory + "preferencesLeafIcon.png");
      if (leafIcon != null)
      {
         DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
         renderer.setLeafIcon(leafIcon);
         preferencesTree.setCellRenderer(renderer);
      }

      preferencesTree.addTreeSelectionListener(this);

      JScrollPane treeScrollPane = new JScrollPane(preferencesTree);
      treeScrollPane.setPreferredSize(new Dimension(250, 315));
      treeScrollPane.setAutoscrolls(true);

      treePreferencesSelectionPanel.add(treeScrollPane);

      mainPanel.add(treePreferencesSelectionPanel, BorderLayout.WEST);

      // ======================================================
      // Creating the preferences panel holding options panels
      // for the application.

      centerPanel = new JPanel(new BorderLayout());

      // Title Indicator

      // Setting up a bold title font.
      if (this.getFont() != null)
      {
         fontName = this.getFont().getFontName();
         fontSize = this.getFont().getSize();
         titleFont = new Font(fontName, Font.BOLD, fontSize);
      }
      else
         titleFont = new Font("Serif", Font.BOLD, 12);

      currentPreferencesSelectionLabel = new JLabel(resourcePreferences);
      currentPreferencesSelectionLabel.setFont(titleFont);
      currentPreferencesSelectionLabel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

      centerPanel.add(currentPreferencesSelectionLabel, BorderLayout.NORTH);

      // =========================
      // Options Panel & Contents
      
      optionsPanel = new JPanel(centerCardLayout = new CardLayout());
      optionsPanel.setBorder(BorderFactory.createEtchedBorder());
      optionsPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(),
                                                                BorderFactory.createLoweredBevelBorder()));

      // ***************************************
      // Preferences Decorative Panel

      // Setting up a calendar instance to determine
      // the season of preferences top panel to display.

      calendar = Calendar.getInstance();
      currentMonth = calendar.get(Calendar.MONTH);

      // {11,0,1} December-February (Winter)
      if (currentMonth == 11 || currentMonth == 1 || currentMonth == 0)
         preferencesTopPanel = new PreferencesPanelWinter();
      // {2,3} March-Arpil (Early Spring)
      else if (currentMonth >= 2 && currentMonth <= 3)
         preferencesTopPanel = new PreferencesPanelEarlySpring();
      // {4,5} May-June (Spring)
      else if (currentMonth >= 4 && currentMonth <= 5)
         preferencesTopPanel = new PreferencesPanelSpring();
      // {6,7,8} July-September (Summer)
      else if (currentMonth >= 6 && currentMonth <= 8)
         preferencesTopPanel = new PreferencesPanelSummer();
      // {9,10} October-November (Fall)
      else
         preferencesTopPanel = new PreferencesPanelFall();
      
      optionsPanel.add(resourcePreferences, preferencesTopPanel);
      
      // ***************************************
      // General Options Decorative Panel
      generalPanel = new GraphicsCanvasPanel("GeneralPreferencesPanel.jpg");
      generalPanel.setBorder(BorderFactory.createLoweredBevelBorder());
      optionsPanel.add(resourceGeneral, generalPanel);
      
      // ***************************************
      // General Options Panel
      generalPreferencesPanel = new GeneralPreferencesPanel(resourceBundle);
      generalPreferencesPanel.setBorder(BorderFactory.createLoweredBevelBorder());
      optionsPanel.add(resourceGeneralOptions, generalPreferencesPanel);

      // ***************************************
      // Table Summary View Decorative Panel
      summaryPreferencesPanel = new GraphicsCanvasPanel("SummaryPreferencesPanel.jpg");
      summaryPreferencesPanel.setBorder(BorderFactory.createLoweredBevelBorder());
      optionsPanel.add(resourceTableSummaryView, summaryPreferencesPanel);

      // ***************************************
      // Table Fields Option Panel
      tableFieldPanel = new JPanel(new BorderLayout());
      createTableFieldsOptionPanel(tableFieldPanel);
      tableSelectionFieldsComboBox.addActionListener(this);
      optionsPanel.add(resourceTableFields, tableFieldPanel);

      // ***************************************
      // Table Rows Option Panel
      tableRowPanel = new JPanel(new BorderLayout());
      createTableRowsOptionPanel(tableRowPanel);
      tableSelectionRowsComboBox.addActionListener(this);
      optionsPanel.add(resourceTableRows, tableRowPanel);

      // ***************************************
      // Data Import Decorative Panel
      dataImportPreferencesPanel = new GraphicsCanvasPanel("DataImportPreferencesPanel.jpg");
      dataImportPreferencesPanel.setBorder(BorderFactory.createLoweredBevelBorder());
      optionsPanel.add(resourceDataImport, dataImportPreferencesPanel);

      // ***************************************
      // Data CSV Import Option Panel
      csvImportPanel = new CSVImportPreferencesPanel(resourceBundle);
      csvImportPanel.csvImportPanelFiller.setThreadAction(true);  
      optionsPanel.add("CSV " + resourceImport, csvImportPanel);

      // ***************************************
      // Data Export Decorative Panel
      dataExportPreferencesPanel = new GraphicsCanvasPanel("DataExportPreferencesPanel.jpg");
      dataExportPreferencesPanel.setBorder(BorderFactory.createLoweredBevelBorder());
      optionsPanel.add(resourceDataExport, dataExportPreferencesPanel);

      // ***************************************
      // Data CSV Export Option Panel
      csvExportPanel = new CSVExportPreferencesPanel(resourceBundle);
      optionsPanel.add("CSV " + resourceExport, csvExportPanel);
      
      // ***************************************
      // Data PDF Export Option Panel
      pdfExportPanel = new PDFExportPreferencesPanel(resourceBundle);
      optionsPanel.add("PDF " + resourceExport, pdfExportPanel);

      // ***************************************
      // Data SQL Export Options Panel
      sqlExportPanel = new SQLExportPreferencesPanel(resourceBundle);
      optionsPanel.add("SQL " + resourceExport, sqlExportPanel);

      centerPanel.add(optionsPanel, BorderLayout.CENTER);
      mainPanel.add(centerPanel, BorderLayout.CENTER);

      // ======================================================
      // Buttons to close, cancel, or access the MyJSQLView Manual.
      southButtonPanel = new JPanel();
      southButtonPanel.setBorder(BorderFactory.createEtchedBorder());

      resource = resourceBundle.getResource("PreferencesFrame.button.OK");
      if (resource.equals(""))
         okButton = new JButton("OK");
      else
         okButton = new JButton(resource);
      okButton.setFocusPainted(false);
      okButton.addActionListener(this);
      southButtonPanel.add(okButton);

      resource = resourceBundle.getResource("PreferencesFrame.button.Cancel");
      if (resource.equals(""))
         cancelButton = new JButton("Cancel");
      else
         cancelButton = new JButton(resource);
      cancelButton.setFocusPainted(false);
      cancelButton.addActionListener(this);
      southButtonPanel.add(cancelButton);

      resource = resourceBundle.getResource("PreferencesFrame.button.Help");
      if (resource.equals(""))
         helpButton = new JButton("Help");
      else
         helpButton = new JButton(resource);
      helpButton.setFocusPainted(false);
      helpButton.addActionListener(this);
      southButtonPanel.add(helpButton);

      mainPanel.add(southButtonPanel, BorderLayout.SOUTH);

      getContentPane().add(mainPanel);
      this.addWindowListener(preferencesFrameListener);

      // Dummy Button for HelpFrame
      helpCloseButton = new JButton();
      helpCloseButton.addActionListener(this);
      helpShowing = false;
   }

   //==============================================================
   // WindowListener for insuring that the frame is closed. (x).
   //==============================================================

   private transient WindowListener preferencesFrameListener = new WindowAdapter()
   {
      // Stop PreferencesPanel thread and dispose.
      public void windowClosing(WindowEvent e)
      {
         MyJSQLView_JMenuBarActions.setPreferencesNotVisisble();
         preferencesTopPanel.suspendPanel(true);
         generalPreferencesPanel.generalOptionsPanelFiller.suspendPanel(true);
         csvImportPanel.csvImportPanelFiller.suspendPanel(true);
         dispose();
      }

      // Start PreferencesPanel thread if needed.
      public void windowDeiconified(WindowEvent e)
      {
         DefaultMutableTreeNode node;
         String optionName;

         // Make sure there really is a node selected.
         node = (DefaultMutableTreeNode) preferencesTree.getLastSelectedPathComponent();
         if (node == null)
            return;
         else
         {
            optionName = node.getUserObject().toString();
           
            if (optionName.equals(resourcePreferences))
               preferencesTopPanel.setThreadAction(false);
            
            if (optionName.equals(resourceGeneralOptions))
               generalPreferencesPanel.generalOptionsPanelFiller.setThreadAction(false);
            
            if (optionName.equals("CSV"))
               if (node.getParent().toString().equals(resourceDataImport))
                  csvImportPanel.csvImportPanelFiller.setThreadAction(false);  
         }
      }

      // Stop PreferencesPanel thread.
      public void windowIconified(WindowEvent e)
      {
         DefaultMutableTreeNode node;
         String optionName;

         // Make sure there really is a node selected.
         node = (DefaultMutableTreeNode) preferencesTree.getLastSelectedPathComponent();
         if (node == null)
            return;
         else
         {
            optionName = node.getUserObject().toString();
            
            if (optionName.equals(resourcePreferences))
               preferencesTopPanel.setThreadAction(true);
            
            if (optionName.equals(resourceGeneralOptions))
               generalPreferencesPanel.generalOptionsPanelFiller.setThreadAction(true);
            
            if (optionName.equals("CSV"))
               if (node.getParent().toString().equals(resourceDataImport))
                  csvImportPanel.csvImportPanelFiller.setThreadAction(true);
         }
      }
   };

   //==============================================================
   // ActionEvent Listener method for determining when the selections
   // have been made so an update can be performed or an appropriate
   // action taken.
   //==============================================================

   public void actionPerformed(ActionEvent evt)
   {
      Object frameSource = evt.getSource();

      // Class Instances
      Iterator<String> cardsIterator;
      TableTabPanel currentTableTabPanel;
      TableFieldSelectionPreferencesPanel tableFieldPreferences;
      TableRowSelectionPreferencesPanel tableRowPreferences;

      // Overall action buttons.
      if (frameSource instanceof JButton)
      {
         // OK Button Action.
         if (frameSource == okButton)
         {
            // Update the selected preferences from the panels.
            
            DBTablesPanel.startStatusTimer();
            
            cardsIterator = tableFieldCards.iterator();
            
            while (cardsIterator.hasNext())
               (tableFieldPanelsHashMap.get(cardsIterator.next())).updatePreferences();
                 
            cardsIterator = tableRowCards.iterator();

            while (cardsIterator.hasNext())
               (tableRowPanelsHashMap.get(cardsIterator.next())).updatePreferences();

            DBTablesPanel.setGeneralProperties(generalPreferencesPanel.getGeneralOptions());
            DBTablesPanel.setDataImportProperties(csvImportPanel.getCSVImportOptions());
            DBTablesPanel.setDataExportProperties(csvExportPanel.getCSVExportOptions());
            DBTablesPanel.setDataExportProperties(pdfExportPanel.getPDFExportOptions());
            DBTablesPanel.setDataExportProperties(sqlExportPanel.getSQLExportOptions());
            
            DBTablesPanel.stopStatusTimer();
            DBTablesPanel.setSelectedTableTabPanel((String) tableSelectionFieldsComboBox.getSelectedItem());

            MyJSQLView_JMenuBarActions.setPreferencesNotVisisble();
            preferencesTopPanel.suspendPanel(true);
            generalPreferencesPanel.generalOptionsPanelFiller.suspendPanel(true);
            csvImportPanel.csvImportPanelFiller.suspendPanel(true);
            this.dispose();
         }

         // Cancel Buttton Action.
         else if (frameSource == cancelButton)
         {
            MyJSQLView_JMenuBarActions.setPreferencesNotVisisble();
            preferencesTopPanel.suspendPanel(true);
            generalPreferencesPanel.generalOptionsPanelFiller.suspendPanel(true);
            csvImportPanel.csvImportPanelFiller.suspendPanel(true);
            this.dispose();
         }

         // Help Button Action.
         else if (frameSource == helpButton)
         {
            if (!helpShowing)
            {
               HelpFrame manualContents = new HelpFrame("MyJSQLView Manual",
                                                        "/docs/Manual/MyJSQLView_Manual.html",
                                                        helpCloseButton);
               manualContents.setSize(640, 400);
               manualContents.setVisible(true);
               helpShowing = true;
            }
         }

         // Dummy Help Closing Button Action.
         else if (frameSource == helpCloseButton)
         {
            helpShowing = false;
         }

      }
      // Used to select appropriate table summary tab's
      // preferences for manipulation.
      else if (frameSource instanceof JComboBox)
      {
         String tableName;

         // Table Fields
         if (frameSource == tableSelectionFieldsComboBox)
         {
            tableName = (String) tableSelectionFieldsComboBox.getSelectedItem();

            // During the creation of the frame table field panels
            // only the current table tab was added/shown. On the the fly
            // create the others as needed.

            if (tableFieldCards.contains(tableName))
               tableFieldCardLayout.show(tableFieldsPanel, tableName);
            else
            {
               currentTableTabPanel = DBTablesPanel.getTableTabPanel(tableName);
               
               if (currentTableTabPanel != null)
               {
                  tableName = currentTableTabPanel.getTableName();
                  tableFieldCards.add(tableName);

                  tableFieldPreferences = new TableFieldSelectionPreferencesPanel(currentTableTabPanel, resourceBundle);
                  tableFieldsPanel.add(tableName, tableFieldPreferences);
                  tableFieldPanelsHashMap.put(tableName, tableFieldPreferences);
                  tableFieldCardLayout.show(tableFieldsPanel, tableName);
               }
            }
            DBTablesPanel.setSelectedTableTabPanel(tableName);
         }
         // Table Rows
         else
         {
            tableName = (String) tableSelectionRowsComboBox.getSelectedItem();

            // Likewise as the table field panels create on the fly the
            // required table row selection panels.

            if (tableRowCards.contains(tableName))
               tableRowCardLayout.show(tableRowsPanel, tableName);
            else
            {
               currentTableTabPanel = DBTablesPanel.getTableTabPanel(tableName);
               
               if (currentTableTabPanel != null)
               {
                  tableName = currentTableTabPanel.getTableName();
                  tableRowCards.add(tableName);

                  tableRowPreferences = new TableRowSelectionPreferencesPanel(tableName, resourceBundle);
                  tableRowsPanel.add(tableName, tableRowPreferences);
                  tableRowPanelsHashMap.put(tableName, tableRowPreferences);
                  tableRowCardLayout.show(tableRowsPanel, tableName);
               }
            }
            DBTablesPanel.setSelectedTableTabPanel(tableName);
         }
      }
      else
         return;
   }

   //==============================================================
   // TreeSelection Listener method for detecting the selection of
   // nodes in the JTree and setting the appropriate preferences
   // option panel in the center panel.
   //==============================================================

   public void valueChanged(TreeSelectionEvent e)
   {
      // Class Method Instances
      DefaultMutableTreeNode node;
      String optionName;

      // Make sure there really is a node selected.
      node = (DefaultMutableTreeNode) preferencesTree.getLastSelectedPathComponent();
      if (node == null)
         return;
      else
      {
         optionName = node.getUserObject().toString();
         // System.out.println(node.getParent().toString() + " " +
         // node.getUserObject().toString());

         // Looks like we have a option selected so show
         // the appropriate option panel. Left this like
         // this so later could differentiate between nodes
         // if need be.

         // Top Node
         if (node.isRoot())
            optionName = node.getUserObject().toString();
         // Leaf Node
         else if (node.isLeaf())
         {
            if (node.getParent().toString().equals(resourceDataImport))
               optionName = node.getUserObject().toString() + " " + resourceImport;
            else if (node.getParent().toString().equals(resourceDataExport))
               optionName = node.getUserObject().toString() + " " + resourceExport;
            else
               optionName = node.getUserObject().toString();
         }
         // Parent Node
         else
            optionName = node.getUserObject().toString();
         
         // System.out.println(optionName);

         // Set Title & Current Panel
         currentPreferencesSelectionLabel.setText(optionName);
         centerCardLayout.show(optionsPanel, optionName);

         // If the selected optionPanel is the PreferencesPanel
         // then start or stop its thread.
         if (optionName.equals(resourcePreferences))
            preferencesTopPanel.setThreadAction(false);
         else
            preferencesTopPanel.setThreadAction(true);
         
         // If the selected optionPanel is the General Options Panel
         // then start or stop its thread.
         if (optionName.equals(resourceGeneralOptions))
            generalPreferencesPanel.generalOptionsPanelFiller.setThreadAction(false);
         else
            generalPreferencesPanel.generalOptionsPanelFiller.setThreadAction(true);
         
         // If the selected optionPanel is the CSV Import Panel
         // then start or stop its thread.
         if (optionName.equals("CSV " + resourceImport))
            csvImportPanel.csvImportPanelFiller.setThreadAction(false);
         else
            csvImportPanel.csvImportPanelFiller.setThreadAction(true);
      }
   }
   
   //==============================================================
   // Class Method for setting up the resource names for the option
   // nodes in the panel for Internationalization.
   //==============================================================

   private void createNodeResourceNames()
   {
      // Method Instances.
      String resource;
      
      // Preferences Node Name.
      
      resource = resourceBundle.getResource("PreferencesFrame.node.Preferences");
      if (resource.equals(""))
         resourcePreferences = "Preferences";
      else
         resourcePreferences = resource;
      
      // General View Nodes.
      
      resource = resourceBundle.getResource("PreferencesFrame.node.General");
      if (resource.equals(""))
         resourceGeneral = "General";
      else
         resourceGeneral = resource;
      resource = resourceBundle.getResource("PreferencesFrame.node.GeneralOptions");
      if (resource.equals(""))
         resourceGeneralOptions = "Options";
      else
         resourceGeneralOptions = resource;
      
      // Table Summary View Nodes.
      
      resource = resourceBundle.getResource("PreferencesFrame.node.TableSummaryView");
      if (resource.equals(""))
         resourceTableSummaryView = "Table Summary View";
      else
         resourceTableSummaryView = resource;
      resource = resourceBundle.getResource("PreferencesFrame.node.TableFields");
      if (resource.equals(""))
         resourceTableFields = "Table Fields";
      else
         resourceTableFields = resource;
      resource = resourceBundle.getResource("PreferencesFrame.node.TableRows");
      if (resource.equals(""))
         resourceTableRows = "Table Rows";
      else
         resourceTableRows = resource;
      
      // Data Import &  Export Nodes.
      
      resource = resourceBundle.getResource("PreferencesFrame.node.DataImport");
      if (resource.equals(""))
         resourceDataImport = "Data Import";
      else
         resourceDataImport = resource;
      resource = resourceBundle.getResource("PreferencesFrame.node.Import");
      if (resource.equals(""))
         resourceImport = "Import";
      else
         resourceImport = resource;
      
      resource = resourceBundle.getResource("PreferencesFrame.node.DataExport");
      if (resource.equals(""))
         resourceDataExport = "Data Export";
      else
         resourceDataExport = resource; 
      resource = resourceBundle.getResource("PreferencesFrame.node.Export");
      if (resource.equals(""))
         resourceExport = "Export";
      else
         resourceExport = resource;
   }

   //==============================================================
   // Class Method for setting up the preferences options tree nodes.
   //==============================================================

   private void createTreeNodes(DefaultMutableTreeNode top)
   {
      // Class Method Instances.
      DefaultMutableTreeNode generalNode, generalOptionsNode;
      DefaultMutableTreeNode summaryTableNode, tableFieldsNode, tableRowsNode;
      DefaultMutableTreeNode dataImportNode, csvImportNode;
      DefaultMutableTreeNode dataExportNode, csvExportNode, pdfExportNode, sqlExportNode;

      // General View Node
      generalNode = null;
      generalNode = new DefaultMutableTreeNode(resourceGeneral);
      top.add(generalNode);
      
      // General Options Node
      generalOptionsNode = null;
      generalOptionsNode = new DefaultMutableTreeNode(resourceGeneralOptions);
      generalNode.add(generalOptionsNode);
      
      // Summary Table View Node
      summaryTableNode = null;
      summaryTableNode = new DefaultMutableTreeNode(resourceTableSummaryView);
      top.add(summaryTableNode);

      // Summary Table View Option Nodes
      tableFieldsNode = new DefaultMutableTreeNode(resourceTableFields);
      summaryTableNode.add(tableFieldsNode);

      tableRowsNode = new DefaultMutableTreeNode(resourceTableRows);
      summaryTableNode.add(tableRowsNode);

      // Data Import Node
      dataImportNode = null;
      dataImportNode = new DefaultMutableTreeNode(resourceDataImport);
      top.add(dataImportNode);

      // Data Import Option Nodes
      csvImportNode = new DefaultMutableTreeNode("CSV");
      dataImportNode.add(csvImportNode);

      // Data Export Node
      dataExportNode = null;
      dataExportNode = new DefaultMutableTreeNode(resourceDataExport);
      top.add(dataExportNode);

      // Data Export Option Nodes
      csvExportNode = new DefaultMutableTreeNode("CSV");
      dataExportNode.add(csvExportNode);
      
      pdfExportNode = new DefaultMutableTreeNode("PDF");
      dataExportNode.add(pdfExportNode);

      sqlExportNode = new DefaultMutableTreeNode("SQL");
      dataExportNode.add(sqlExportNode);
   }

   //==============================================================
   // Class Method for setting up the Table Fields Options Panel.
   //==============================================================

   private void createTableFieldsOptionPanel(JPanel tableFieldPanel)
   {
      // Class Method Instances
      JPanel selectionPanel;
      JLabel tableSelectionLabel;
      TableTabPanel currentTableTab;
      String resource;

      // Create and setup the table fields option
      // panel components.
      selectionPanel = new JPanel(new GridLayout(2,1,0,0));

      resource = resourceBundle.getResource("PreferencesFrame.label.TableSelection");
      if (resource.equals(""))
         tableSelectionLabel = new JLabel("Table Selection", JLabel.CENTER);
      else
         tableSelectionLabel = new JLabel(resource, JLabel.CENTER);
      selectionPanel.add(tableSelectionLabel);
      tableSelectionFieldsComboBox = new JComboBox(ConnectionManager.getTableNames());
      selectionPanel.add(tableSelectionFieldsComboBox);

      tableFieldPanel.add(selectionPanel, BorderLayout.NORTH);

      // To save memory and speed up the preferences panel
      // display add only the current selected tab fields
      // to the panel's card layout.

      tableFieldsPanel = new JPanel(tableFieldCardLayout = new CardLayout());
      
      currentTableTab = DBTablesPanel.getSelectedTableTabPanel();
      
      if (currentTableTab == null)
         return;
      
      tableName = currentTableTab.getTableName();
      tableFieldCards.add(tableName);
      tableSelectionFieldsComboBox.setSelectedItem(tableName);

      tableFieldPreferences = new TableFieldSelectionPreferencesPanel(currentTableTab, resourceBundle);
      tableFieldsPanel.add(tableName, tableFieldPreferences);
      tableFieldPanelsHashMap.put(tableName, tableFieldPreferences);
      
      tableFieldPanel.add(tableFieldsPanel, BorderLayout.CENTER);
   }

   //=============================================================
   // Class Method for setting up the Table Rows Options Panel.
   //=============================================================

   private void createTableRowsOptionPanel(JPanel tableRowPanel)
   {
      // Class Method Instances
      JPanel selectionPanel;
      JLabel tableSelectionLabel;
      String resource, resourceWarning, rowSizeWarningMessage;
      
      resource = resourceBundle.getResource("PreferencesFrame.message.Warning");
      if (resource.equals(""))
         resourceWarning = "WARNING!";
      else
         resourceWarning = resource;
      
      resource = resourceBundle.getResource("PreferencesFrame.message.RowSizeWarning");
      if (resource.equals("") || resourceWarning.equals(""))
      {
         rowSizeWarningMessage = "<html><body><p><span style='color: rgb(255, 0, 0);'>WARNING! </span>"
                                     + "A large row size may adversely effect this application and the database "
                                     + "server performance. It is recommended that a reasonable row size be "
                                     + "selected based on special needs such as CSV/SQL exports. A basic paging "
                                     + "mechanism is in place for each table summary view that already allows "
                                     + "access to all table data.</p></body></html>";
      }
      else
      {
         rowSizeWarningMessage = "<html><body><p><span style='color: rgb(255, 0, 0);'>" + resourceWarning
                                  + " </span>" + resource + "</p></body></html>";
      }

      // Create and setup the table rows option
      // panel components.

      selectionPanel = new JPanel(new GridLayout(2,1,0,0));

      resource = resourceBundle.getResource("PreferencesFrame.label.TableSelection");
      if (resource.equals(""))
         tableSelectionLabel = new JLabel("Table Selection", JLabel.CENTER);
      else
         tableSelectionLabel = new JLabel(resource, JLabel.CENTER);
      selectionPanel.add(tableSelectionLabel);

      tableSelectionRowsComboBox = new JComboBox(ConnectionManager.getTableNames());
      selectionPanel.add(tableSelectionRowsComboBox);

      tableRowPanel.add(selectionPanel, BorderLayout.NORTH);

      GridBagLayout gridbag = new GridBagLayout();
      GridBagConstraints constraints = new GridBagConstraints();

      JPanel rowCenterPanel = new JPanel(gridbag);

      JEditorPane htmlContentPane;
      htmlContentPane = new JEditorPane("text/html", rowSizeWarningMessage);
      htmlContentPane.setMargin(new Insets(10, 15, 10, 10));
      htmlContentPane.setBackground(rowCenterPanel.getBackground());

      buildConstraints(constraints, 0, 0, 1, 1, 100, 100);
      constraints.fill = GridBagConstraints.BOTH;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(htmlContentPane, constraints);
      rowCenterPanel.add(htmlContentPane);

      tableRowsPanel = new JPanel(tableRowCardLayout = new CardLayout());

      // To save memory and speed up the preferences panel
      // display add only the current selected tab row size
      // to the panels card layout.
      
      if (tableName == null)
         return;

      tableRowCards.add(tableName);

      tableSelectionRowsComboBox.setSelectedItem(tableName);

      tableRowPreferences = new TableRowSelectionPreferencesPanel(tableName, resourceBundle);
      tableRowsPanel.add(tableName, tableRowPreferences);
      tableRowPanelsHashMap.put(tableName, tableRowPreferences);

      buildConstraints(constraints, 0, 1, 1, 1, 100, 100);
      constraints.fill = GridBagConstraints.BOTH;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(tableRowsPanel, constraints);
      mainPanel.add(tableRowsPanel);
      rowCenterPanel.add(tableRowsPanel);

      tableRowPanel.add(rowCenterPanel, BorderLayout.CENTER);
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