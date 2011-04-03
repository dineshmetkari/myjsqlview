//=================================================================
//                   SearchFrame Class
//=================================================================
//   This class is used to provide a framework to execute
// searches on the current selected host database by the user that
// has a connection established in MyJSQLView.
//
//                  << SearchFrame.java >>
//
//=================================================================
// Copyright (C) 2005-2011 Dana M. Proctor
// Version 3.6 04/03/2011
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
// Version 1.0 MyJSQLView Initial SearchFrame Class.
//         1.1 Basic Structure and Code Finalized. Still Additional Features
//             to be Implemented.
//         1.2 Class SearchDatabase Changed to SearchDatabaseThread. Menu
//             Item for Print and Associated Actions. Added Popup to resultTable.
//             Handled resultTable Mouse Clicks to DBTablesPanel, TableTabPanel.
//             Added Inner Class ResultTableCellRenderer. Clean up and Commented.
//         1.3 Removed Private Class ResultTableCellRenderer, Moved to Independent
//             File.
//         1.4 Simplified Code Some and Provided a System.out(actionCommand)
//             in Class Method actionPerformed().
//         1.5 Declared searchDatabase Transient.
//         1.6 Inner Class MouseAdapter().mouseClicked() Added Conditional Check
//             for the Returned TableTabPanel.
//         1.7 Inner Class MouseAdapter Method mouseClicked() Added the Showing
//             the Selected Table via DBTablesPanel.setSelectedTableTabPanel().
//         1.8 Removed Inner MouseAdapter Class & Replaced With MouseListener.
//             Added Class Instances selectedTableTab & selectedTable; Created an
//             Inner Class Thread to Handle the Showing of Tables on Click.
//         1.9 Class Method mouseClicked() Provided a String Name for the Thread
//             showResultsTableTabPanelThread.
//         2.0 Header Format Changes/Update.
//         2.1 Removed Constructor Instance fileSeparator. Obtained iconsDirectory
//             From MyJSQLView_utils Class.
//         2.2 Added fileSeparator to iconsDirectory.
//         2.3 Added MyJSQLView.getPopupMenuListener() to searchTextField.
//         2.4 Added Copy PopupMenuItem to resultTablePopup, Then Associated to the
//             resultTable. Class Method Effected Construtor(), and actionPerformed().
//         2.5 Changed Package to Reflect Dandy Made Productions Code.
//         2.6 Implementation of Internationalization Through Class Instacne
//             resourceBundle. Added Constructor Instance resource and Methods
//             createMenuBar() & createPopupMenuItem().
//         2.7 Set Title via resourceBundle in Constructor.
//         2.8 Parameterized Instance tableHeadings in Constructor to Bring Code
//             Into Compliance With Java 5.0 API.
//         2.9 Parameterized Method Instance databaseTables in actionPerformed(().
//         3.0 Constructor Changed the Assignment of defaultTableData From new
//             Integer to Integer.valueOf().
//         3.1 Parameterized Constructor Instance tableNamesIterator.
//         3.2 Implemented a Include Table Selectiion Column. Constructor and Methods
//             actionPerformed(), & mouseClicked() Effected. Added Action of Include
//             All Tables and Exclude All Tables in Popup Menu. Disabled searchButton
//             Till clearSearchTextFieldButton After Search. Added Argument selectedTables
//             to SearchDatabaseThread Instantiation.
//         3.3 Constructor Assignment of Boolean Object to defaultTableData via
//             Boolean.valueOf() Instead of Creating a New Boolean Object. Likewise for
//             resultTable in actionPerformed() & mounseClicked().
//         3.4 Updated Comments and Organized the Setting Up the resultTable in the
//             Constructor.
//         3.5 Table Names Obtained From New Class ConnectionManager in Constructor
//             and actionPerformed().
//         3.6 Minor Comment Change.
//                            
//-----------------------------------------------------------------
//                 danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.*;
import javax.swing.table.TableColumn;
import javax.swing.text.DefaultEditorKit;

//=================================================================
//                     MyJSQLView SearchFrame
//=================================================================

/**
 *    The SearchFrame class is used to provide a framework to execute
 * searches on the current selected host database by the user that has
 * a connection established in MyJSQLView.
 * 
 * @author Dana M. Proctor
 * @version 3.6 04/03/2011
 */

class SearchFrame extends JFrame implements ActionListener, KeyListener, MouseListener
{
   // =============================================
   // Creation of the necessary class instance
   // variables for the SearchFrame.
   // =============================================

   private static final long serialVersionUID = -4730639399999597935L;

   private transient SearchDatabaseThread searchDatabase;
   private JPopupMenu resultTablePopupMenu;
   private JTextField searchTextField;
   private JButton searchButton;
   private JButton clearSearchTextFieldButton;
   private JButton searchCompleteButton;

   private MyJSQLView_ResourceBundle resourceBundle;
   private Object[][] defaultTableData;
   private JTable resultTable;
   private MyJSQLView_TableModel tableModel;

   private JProgressBar searchProgressBar;
   private JButton cancelButton;
   private TableTabPanel selectedTableTab;
   private String selectedTable;
   private String iconsDirectory;
   
   //==============================================================
   // SearchFrame Constructor
   //==============================================================

   SearchFrame()
   {
      // Constructor Instances.
      JMenuBar searchFrameMenuBar;
      Vector<String> tableHeadings;
      JScrollPane tableScrollPane;
      TableColumn column;
      String resource, resourceInclude;
      
      JPanel mainPanel, centerPanel, searchPanel, statusCancelPanel;
      JLabel searchLabel;

      ImageIcon searchIcon, removeIcon;

      // Setting up the title, file separator and other needed
      // instance elements.

      iconsDirectory = MyJSQLView_Utils.getIconsDirectory() + MyJSQLView_Utils.getFileSeparator();
      resourceBundle = MyJSQLView.getLocaleResourceBundle();
      
      resource = resourceBundle.getResource("SearchFrame.message.Title");
      if (resource.equals(""))
         setTitle("MyJSQLView Search Frame");
      else
         setTitle("MyJSQLView " + resource);

      searchIcon = new ImageIcon(iconsDirectory + "searchIcon.png");
      removeIcon = new ImageIcon(iconsDirectory + "removeIcon.png");

      // ==================================================
      // Frame Window Closing Addition. Also method for
      // reactivating if desired/needed.
      // ==================================================

      WindowListener searchFrameListener = new WindowAdapter()
      {
         public void windowClosing(WindowEvent e)
         {
            MyJSQLView_JMenuBarActions.setSearchFrameNotVisisble();
            dispose();
         }

         public void windowActivated(WindowEvent e)
         {
         }
      };

      this.addWindowListener(searchFrameListener);

      // ===============================================
      // JMenu Bar for the Frame.
      // ===============================================

      searchFrameMenuBar = new JMenuBar();
      searchFrameMenuBar.setBorder(BorderFactory.createEtchedBorder());
      createMenuBar(searchFrameMenuBar);
      this.setJMenuBar(searchFrameMenuBar);

      // ===============================================
      // Popup Menu for the Center JTable.
      // ===============================================

      resultTablePopupMenu = new JPopupMenu();
      createPopupMenu();

      // ===============================================
      // Setting up the various panels that are used in
      // the search frame
      // ===============================================

      mainPanel = new JPanel(new BorderLayout());
      mainPanel.setBorder(BorderFactory.createRaisedBevelBorder());

      // ====================
      // Search Interface

      searchPanel = new JPanel();
      searchPanel.setBorder(BorderFactory.createRaisedBevelBorder());

      resource = resourceBundle.getResource("SearchFrame.label.SearchDatabaseFor");
      if (resource.equals(""))
         searchLabel = new JLabel("Search Database For : ");
      else
         searchLabel = new JLabel(resource + " : ");
      searchPanel.add(searchLabel);

      searchTextField = new JTextField(15);
      searchTextField.setBorder(BorderFactory.createLoweredBevelBorder());
      searchTextField.addMouseListener(MyJSQLView.getPopupMenuListener());
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

      mainPanel.add(searchPanel, BorderLayout.NORTH);

      // =========================================
      // Resultant Search Result Panel/Table

      centerPanel = new JPanel(new GridLayout(1, 1, 0, 0));
      centerPanel.setBorder(BorderFactory.createEtchedBorder());

      // Setup Headings.
      tableHeadings = new Vector<String>();
      
      resourceInclude = resourceBundle.getResource("SearchFrame.label.Include");
      if (resourceInclude.equals(""))
         tableHeadings.addElement("Include");
      else
         tableHeadings.addElement(resourceInclude);
      
      resource = resourceBundle.getResource("SearchFrame.label.Table");
      if (resource.equals(""))
         tableHeadings.addElement("Table");
      else
         tableHeadings.addElement(resource);
      
      resource = resourceBundle.getResource("SearchFrame.label.SearchResultCount");
      if (resource.equals(""))
         tableHeadings.addElement("Search Result Count");
      else
         tableHeadings.addElement(resource);

      // Fill the result table structure with default data.
      
      defaultTableData = new Object[DBTablesPanel.getTableCount()][3];

      Iterator<String> tableNamesIterator = ConnectionManager.getTableNames().iterator();
      int i = 0;

      while (tableNamesIterator.hasNext())
      {
         defaultTableData[i][0] = Boolean.valueOf(true);
         defaultTableData[i][1] = "   " + tableNamesIterator.next();
         defaultTableData[i++][2] = Integer.valueOf(0);
      }
      
      // Setup the table.
      
      tableModel = new MyJSQLView_TableModel(tableHeadings, defaultTableData);

      resultTable = new JTable(tableModel);
      resultTable.getTableHeader().setFont(new Font(centerPanel.getFont().getName(), Font.BOLD,
                                           centerPanel.getFont().getSize()));
      resultTable.getActionMap().put(TransferHandler.getCopyAction().getValue(Action.NAME),
                                     TransferHandler.getCopyAction());
      //resultTable.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK), "copy");
      
      column = resultTable.getColumnModel().getColumn(0);
      column.setPreferredWidth(resourceInclude.length());
      resultTable.getColumnModel().getColumn(1).setCellRenderer(new SearchResultTableCellRenderer());
      resultTable.getColumnModel().getColumn(2).setCellRenderer(new SearchResultTableCellRenderer());
      
      resultTable.addMouseListener(this);

      // Create a scrollpane for the search count result table.
      
      tableScrollPane = new JScrollPane(resultTable);
      centerPanel.add(tableScrollPane);
      mainPanel.add(centerPanel, BorderLayout.CENTER);

      // ==================================
      // SearchFrame Progress/Cancel Panel

      statusCancelPanel = new JPanel();
      statusCancelPanel.setBorder(BorderFactory.createEtchedBorder());

      searchProgressBar = new JProgressBar();
      searchProgressBar.setIndeterminate(true);
      searchProgressBar.setValue(0);
      searchProgressBar.setStringPainted(true);
      statusCancelPanel.add(searchProgressBar);

      resource = resourceBundle.getResource("SearchFrame.label.Cancel");
      if (resource.equals(""))
         cancelButton = new JButton("Cancel");
      else
         cancelButton = new JButton(resource);
      cancelButton.setMargin(new Insets(0, 0, 0, 0));
      cancelButton.setFocusPainted(false);
      cancelButton.addActionListener(this);
      statusCancelPanel.add(cancelButton);

      mainPanel.add(statusCancelPanel, BorderLayout.SOUTH);
      
      getContentPane().add(mainPanel);
      getContentPane().addMouseListener(MyJSQLView.getPopupMenuListener());
      
      // Dummy button for updating data from the thread
      // created with searchDatabase.
      searchCompleteButton = new JButton();
      searchCompleteButton.addActionListener(this);
      
      searchTextField.grabFocus();
   }

   //==============================================================
   // ActionEvent Listener method for detecting the user's selection
   // of various components in the frame and taking the appropriate
   // action as required. Mouse events handled in the inner class
   // mouse adapter created in the constructor.
   //==============================================================

   public void actionPerformed(ActionEvent evt)
   {
      Object panelSource = evt.getSource();

      // MenuBar Actions
      if (panelSource instanceof JMenuItem)
      {
         // Instances & Setting Up.
         JMenuItem item = (JMenuItem) panelSource;
         String actionCommand = item.getActionCommand();
         //System.out.println(actionCommand);

         // ==================================
         // File Menu Item Selection Routing
         // ==================================

         // Exit
         if (actionCommand.equals("FE"))
         {
            MyJSQLView_JMenuBarActions.setSearchFrameNotVisisble();
            this.dispose();
         }

         // Popup resultTable Select All
         if (actionCommand.equals("SelectAll"))
         {
            resultTable.selectAll();
            for (int i = 0; i < resultTable.getRowCount(); i++)
               resultTable.setValueAt(Boolean.valueOf(true), i, 0);
            
         }

         // Popup resultTable DeSelect All
         if (actionCommand.equals("DeSelectAll"))
         {
            resultTable.clearSelection();
            for (int i = 0; i < resultTable.getRowCount(); i++)
               resultTable.setValueAt(Boolean.valueOf(false), i, 0);
         }
         
         // Popup resultTable Copy
         else if (actionCommand.equals((String)TransferHandler.getCopyAction().getValue(Action.NAME)))
         {
            Action a = resultTable.getActionMap().get(actionCommand);
            if (a != null)
               a.actionPerformed(new ActionEvent(resultTable, ActionEvent.ACTION_PERFORMED, null));
         }
      }

      // Button Actions
      if (panelSource instanceof JButton)
      {
         // Execute search action.
         if (panelSource == searchButton)
         {
            if (!searchTextField.getText().equals(""))
            {
               // Collect the database table names directly from MyJSQLView_Access
               // instead of relying on the names in the search frame, just in case
               // a database reload took place.
               
               Vector<String> databaseTables = ConnectionManager.getTableNames();
               boolean[] selectedTables = new boolean[databaseTables.size()];
               
               // Create a list of included tables to be searched.
               if (!databaseTables.isEmpty())
               {
                  for (int i = 0; i < databaseTables.size(); i++)
                  {
                     if (databaseTables.contains(resultTable.getValueAt(i, 1).toString().trim())
                         && ((Boolean)resultTable.getValueAt(i, 0)).booleanValue() == true)
                        selectedTables[i] = true;
                     else
                        selectedTables[i] = false;
                  }
                  
                  // Execute query
                  searchProgressBar.setMaximum(databaseTables.size());
                  searchProgressBar.setValue(0);
                  searchProgressBar.setIndeterminate(false);
                  searchDatabase = new SearchDatabaseThread(databaseTables, selectedTables,
                                                            searchTextField.getText(),
                                                            searchProgressBar,
                                                            searchCompleteButton); 
               }
            }
         }

         // Clear search field action.
         if (panelSource == clearSearchTextFieldButton)
         {
            searchTextField.setText("");
            searchProgressBar.setValue(0);
            searchProgressBar.setIndeterminate(true);
            tableModel.setValues(defaultTableData);
            searchButton.setEnabled(true);
         }

         // Cancel search action.
         if (panelSource == cancelButton)
         {
            if (searchDatabase != null)
               searchDatabase.cancel();
         }
         
         // Database search complete action.
         if (panelSource == searchCompleteButton)
         {
            if (searchDatabase != null)
            {
               Object[][] tableData = searchDatabase.getResultData();
               if (tableData != null)
               {
                  tableModel.setValues(tableData);
                  searchButton.setEnabled(false);
               }
            }
         }
      }
   }

   //==============================================================
   // KeyEvent Listener methods for detected key pressed events to
   // full fill KeyListener Interface requirements.
   //==============================================================
   
   public void keyPressed(KeyEvent evt)
   {
      // Do Nothing
   }

   public void keyReleased(KeyEvent evt)
   {
      // Do Nothing
   }

   //==============================================================
   // KeyEvent Listener method for detecting key pressed event,
   // Enter, used with the search action text field.
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
   // MouseEvent Listener methods for detecting mouse events.
   // MounseListner Interface requirements.
   //==============================================================

   public void mouseEntered(MouseEvent evt)
   {
      // Do Nothing.
   }
   
   public void mouseExited(MouseEvent evt)
   {
      // Do Nothing.
   }
   
   public void mousePressed(MouseEvent evt)
   {
      if (evt.isPopupTrigger())
         resultTablePopupMenu.show(evt.getComponent(), evt.getX(), evt.getY());
   }

   public void mouseReleased(MouseEvent evt)
   {
      if (evt.isPopupTrigger())
         resultTablePopupMenu.show(evt.getComponent(), evt.getX(), evt.getY());
   }

   //==============================================================
   // MouseEvent Listener methods for detecting mouse clicked events.
   // Collects the row & column selected. Then either set or deselects
   // Include checkbox or shows the appropriate database table in
   // the MyJSQLView_Frame, DBTablesPanel and
   //==============================================================   

   public void mouseClicked(MouseEvent e)
   {  
      Point coordinatePoint;
      int tableRow, tableColumn;

      // Collect coordinate to determine cell selected.
      coordinatePoint = e.getPoint();
      tableRow = resultTable.rowAtPoint(coordinatePoint);
      tableColumn = resultTable.columnAtPoint(coordinatePoint);

      if (tableRow >= resultTable.getRowCount() || tableRow < 0)
         return;
      else
      {
         // Include Action
         if (tableColumn == 0)
         {
            Boolean s = (Boolean) resultTable.getValueAt(tableRow, tableColumn);
            
            if (s.booleanValue() == true)
               resultTable.setValueAt(Boolean.valueOf(false), tableRow, tableColumn);
            else
               resultTable.setValueAt(Boolean.valueOf(true), tableRow, tableColumn);
            return;
         }
         
         // Show Table Action
         selectedTable = resultTable.getValueAt(tableRow, 1).toString().trim();
         //System.out.println(selectedTable);

         selectedTableTab = DBTablesPanel.getTableTabPanel(selectedTable);
         
         if (selectedTableTab != null)
         {
            DBTablesPanel.startStatusTimer();
            
            Thread showResultsTableTabPanelThread = new Thread(new Runnable()
            {
               public void run()
               {
                  selectedTableTab.setSearchTextField(searchTextField.getText());
                  DBTablesPanel.setSelectedTableTabPanel(selectedTable);
                  
                  DBTablesPanel.stopStatusTimer();
               }
            }, "SearchFrame.showResultsTableTabPanelThread");
            showResultsTableTabPanelThread.start();  
         }
      }
   }
   
   //==============================================================
   // Method used for creation of the menu bar that will be used
   // with the frame.
   //==============================================================

   private void createMenuBar(JMenuBar searchFrameMenuBar)
   {
      // Method Instances
      String resource;
      JMenu fileMenu, editMenu;
      JMenuItem menuItem = null;
      
      ImageIcon logoIcon;
      JButton logoIconItem;
      
      // File Menu
      resource = resourceBundle.getResource("SearchFrame.menu.File");
      if (resource.equals(""))
         fileMenu = new JMenu("File");
      else
         fileMenu = new JMenu(resource);
      fileMenu.setFont(fileMenu.getFont().deriveFont(Font.BOLD));
      fileMenu.addSeparator();
      
      resource = resourceBundle.getResource("SearchFrame.menu.Exit");
      if (resource.equals(""))
         fileMenu.add(menuItem("Exit", "FE"));
      else
         fileMenu.add(menuItem(resource, "FE"));
      searchFrameMenuBar.add(fileMenu);

      // Edit Menu
      resource = resourceBundle.getResource("SearchFrame.menu.Edit");
      if (resource.equals(""))
         editMenu = new JMenu("Edit");
      else
         editMenu = new JMenu(resource);
      editMenu.setFont(editMenu.getFont().deriveFont(Font.BOLD));
      
      menuItem = new JMenuItem(new DefaultEditorKit.CutAction());
      resource = resourceBundle.getResource("SearchFrame.menu.Cut");
      if (resource.equals(""))
         menuItem.setText("Cut" + "          " + "Ctrl+x");
      else
         menuItem.setText(resource + "          " + "Ctrl+x");
      menuItem.setMnemonic(KeyEvent.VK_X);
      editMenu.add(menuItem);

      menuItem = new JMenuItem(new DefaultEditorKit.CopyAction());
      resource = resourceBundle.getResource("SearchFrame.menu.Copy");
      if (resource.equals(""))
         menuItem.setText("Copy" + "       " + "Ctrl+c");
      else
         menuItem.setText(resource + "       " + "Ctrl+c");
      menuItem.setMnemonic(KeyEvent.VK_C);
      editMenu.add(menuItem);

      menuItem = new JMenuItem(new DefaultEditorKit.PasteAction());
      resource = resourceBundle.getResource("SearchFrame.menu.Paste");
      if (resource.equals(""))
         menuItem.setText("Paste" + "       " + "Ctrl+v");
      else
         menuItem.setText(resource + "       " + "Ctrl+v");
      menuItem.setMnemonic(KeyEvent.VK_V);
      editMenu.add(menuItem);

      searchFrameMenuBar.add(editMenu);

      searchFrameMenuBar.add(Box.createHorizontalGlue());

      // Logo
      logoIcon = new ImageIcon(iconsDirectory + "myjsqlviewIcon.gif");
      logoIconItem = new JButton(logoIcon);
      logoIconItem.setDisabledIcon(logoIcon);
      logoIconItem.setFocusPainted(false);
      logoIconItem.setBorder(BorderFactory.createLoweredBevelBorder());
      searchFrameMenuBar.add(logoIconItem);
   }
   
   //==============================================================
   // Method used for creation of a popup menu for the search
   // results table in the frame.
   //==============================================================

   private void createPopupMenu()
   {
      // Method Instances
      String resource;
      JMenuItem menuItem = null;
      
      resource = resourceBundle.getResource("SearchFrame.menu.SelectAll");
      if (resource.equals(""))
         menuItem = menuItem("Select All", "SelectAll");
      else
         menuItem = menuItem(resource, "SelectAll");
      resultTablePopupMenu.add(menuItem);

      resource = resourceBundle.getResource("SearchFrame.menu.DeSelectAll");
      if (resource.equals(""))
         menuItem = menuItem("DeSelect All", "DeSelectAll");
      else
         menuItem = menuItem(resource, "DeSelectAll");
      resultTablePopupMenu.add(menuItem);
      
      resultTablePopupMenu.addSeparator();
      
      resource = resourceBundle.getResource("SearchFrame.menu.Copy");
      if (resource.equals(""))
         menuItem = new JMenuItem("Copy");
      else
         menuItem = new JMenuItem(resource);
      menuItem.setActionCommand((String)TransferHandler.getCopyAction().getValue(Action.NAME));
      menuItem.setMnemonic(KeyEvent.VK_C);
      menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
      menuItem.addActionListener(this);
      resultTablePopupMenu.add(menuItem);
   }
   
   //==============================================================
   // Method used for the SearchFrame's creation of menu bar
   // items. Helper Method.
   //==============================================================

   private JMenuItem menuItem(String label, String actionLabel)
   {
      JMenuItem item = new JMenuItem(label);
      item.addActionListener(this);
      item.setActionCommand(actionLabel);
      return item;
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