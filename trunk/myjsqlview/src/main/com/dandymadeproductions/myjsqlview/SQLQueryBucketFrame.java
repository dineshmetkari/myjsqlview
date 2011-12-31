//=================================================================
//                  SQLQueryBucketFrame Class
//=================================================================
//
//    This class provides the main frame for the storage of SQL Query
// statements derived from MyJSQLView.
//
//                << SQLQueryBucketFrame.java >>
//
//=================================================================
// Copyright (C) 2005-2012 Dana M. Proctor
// Version 1.8 01/01/2012
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
// Version 1.0 03/17/2011 Initial SQLQueryBucketFrame Class.
//         1.1 04/07/2011 Basic Semi-Completed Outline of Functionality.
//         1.2 04/10/2011 Majority of Functionality Completed.
//         1.4 04/14/2011 Completed SQLQueryBucketFrame Class. Checked & Cleanedup.
//                        Added List Item Moving and Correction for Editing SQL
//                        Statements for Opening File When Added New Items. Corrected
//                        List File Save/Opening Parameter Sequence.
//         1.5 05/05/2011 Class Method actionPerformed() SQL Statement Editor Pane
//                        Action, Filtered Line Feeds and Carriage-Returns.
//         1.6 10/05/2011 Added Class Methods open/saveLastUsedList() to Handle
//                        the Automatic Saving and Loading of the Bucket's List.
//                        Added Class Instance DEFAULT_LIST_LAST_USED_FILENAME.
//         1.7 10/29/2011 Added Class Instance databaseName and Changed the String
//                        for DEFAULT_LIST_LAST_USED_FILENAME. Added to Argument
//                        the Same for openLastUsedList(). Used in saveLastUsedList().
//         1.8 01/01/2012 Copyright Update.
//         
//-----------------------------------------------------------------
//                 danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.*;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.*;

//=================================================================
//                  MyJSQLView SQLQueryBucketFrame
//=================================================================

/**
 * This SQLQueryBucketFrame class provides the main frame for the storage of SQL
 * Query statements derived from MyJSQLView.
 * 
 * @author Dana M. Proctor
 * @version 1.8 01/01/2012
 */

public class SQLQueryBucketFrame extends JFrame implements ActionListener, MouseListener
{
   // Class Instances.
   private static final long serialVersionUID = -1903579213881382782L;

   private JList sqlQueryList;
   private JPopupMenu sqlListPopupMenu;

   private JButton viewButton;
   private JButton addButton;
   private JButton editButton;
   private JButton deleteButton;
   
   private JButton dialog_sqlStatementButton;
   private JButton dialog_colorButton;

   private MyJSQLView_ResourceBundle resourceBundle;
   private String databaseName;
   private String fileSeparator, iconsDirectory;
   private String lastOpenSaveDirectory, savedFileName;

   private String fileName;
   private StringBuffer stringBuffer;
   private String resourceAlert;
   
   private SQLQueryBucketListObject processingBucketListObject;
   private JColorChooser panelColorChooser;
   private boolean processItem;
   private boolean colorAction;
   private String lastActionCommand;
   private int selectedListIndex;
   
   private static final String FILE_OPEN = "FO";
   private static final String FILE_SAVE = "FS";
   private static final String FILE_SAVE_AS = "FSA";
   private static final String EXIT = "FE";

   private static final String VIEW = "View";
   private static final String ADD = "Add";
   private static final String EDIT = "Edit";
   private static final String DELETE = "Delete";
   
   private static final String DIALOG_SQLSTATEMENT = "DialogSQLStatement";
   private static final String DIALOG_COLOR = "DialogColor";

   private static final String parameterDelimiter = "%;%";
   private static final String DEFAULT_LIST_LAST_USED_FILENAME = "sqlquerybucket_";

   // ==============================================================
   // SQLQueryBucketFrame Constructor
   // ==============================================================

   protected SQLQueryBucketFrame()
   {
      // Constructor Instances.
      JMenuBar sqlBucketFrameMenuBar;

      String resource;
      DefaultListModel listModel;
      JPanel mainPanel, listPanel, actionButtonPanel;

      // Setting up the needed elements and title.

      fileSeparator = MyJSQLView_Utils.getFileSeparator();

      resourceBundle = MyJSQLView.getLocaleResourceBundle();
      iconsDirectory = MyJSQLView_Utils.getIconsDirectory() + fileSeparator;

      lastOpenSaveDirectory = "";
      savedFileName = "";
      selectedListIndex = -1;
      colorAction = false;
      processItem = false;
      lastActionCommand = "";

      // Setting title and common alert resource.
      
      resource = resourceBundle.getResource("SQLQueryBucketFrame.message.Title");
      if (resource.equals(""))
         setTitle("MyJSQLView Query Bucket Frame");
      else
         setTitle("MyJSQLView " + resource);

      resourceAlert = resourceBundle.getResource("SQLQueryBucketFrame.dialogtitle.Alert");
      if (resourceAlert.equals(""))
         resourceAlert = "Alert";

      // ==================================================
      // Frame Window Closing Addition. Also method for
      // reactivating if desired/needed.
      // ==================================================

      WindowListener sqlQueryBucketFrameListener = new WindowAdapter()
      {
         public void windowClosing(WindowEvent e)
         {
            setVisible(false);
            MyJSQLView_JMenuBarActions.setSQLQueryBucketFrameNotVisisble();
         }

         public void windowActivated(WindowEvent e)
         {
         }
      };

      this.addWindowListener(sqlQueryBucketFrameListener);

      // ===============================================
      // JMenu Bar for the Frame.
      // ===============================================

      sqlBucketFrameMenuBar = new JMenuBar();
      sqlBucketFrameMenuBar.setBorder(BorderFactory.createEtchedBorder());
      createMenuBar(sqlBucketFrameMenuBar);
      setJMenuBar(sqlBucketFrameMenuBar);

      // ===============================================
      // Setting up the various panels and components
      // that are used in the frame.
      // ===============================================

      mainPanel = new JPanel(new BorderLayout());
      mainPanel.setBorder(BorderFactory.createRaisedBevelBorder());
      panelColorChooser = MyJSQLView_Utils.createColorChooser(this);

      // SQL query drop container, JList and its Popup Menu,
      // Transfer Handler.

      listPanel = new ListHoldingPanel();
      listPanel.setLayout(new BorderLayout());

      listModel = new DefaultListModel();
      sqlQueryList = new JList(listModel);
      
      sqlQueryList.setOpaque(false);
      sqlQueryList.setDragEnabled(true);
      sqlQueryList.setCellRenderer(new SQLQueryBucketListCellRenderer());
      sqlQueryList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
      
      sqlListPopupMenu = createPopupMenu();
      sqlQueryList.add(sqlListPopupMenu);

      // ==================================================
      // Transfer handler for moving items around in the
      // list and allowing SQL Statement to be dropped into
      // other applications, tabs/plugins.
      // ==================================================
      
      sqlQueryList.setTransferHandler(new TransferHandler()
      {
         private static final long serialVersionUID = 2558730071314905153L;
         private boolean isThisDropping;

         // No imports to list allowed unless to itself.
         public boolean canImport(TransferHandler.TransferSupport info)
         {
            // Method Instances.
            JList.DropLocation dropLocation;
            
            // Setting up and checking to see if
            // this is drop location.
            
            isThisDropping = false;
            
            if (info.getComponent().equals(sqlQueryList))
            {
               dropLocation = (JList.DropLocation) info.getDropLocation();
               if (dropLocation.getIndex() == -1)
                  return false;
               else
               {
                  isThisDropping = true;
                  return true;
               }
            }
            else
               return false;
         }

         // This dropping.
         public boolean importData(TransferHandler.TransferSupport info)
         {
            // Method Instances.
            SQLQueryBucketListObject listObjectToBeDropped;
            JList.DropLocation dropLocation;
            DefaultListModel listModel;
            int dropLocationIndex, listSize;
            boolean isInsert;
            
            if (!info.isDrop() || !isThisDropping)
            {
               return false;
            }
            else
            {
               dropLocation = (JList.DropLocation) info.getDropLocation();
               listModel = (DefaultListModel) sqlQueryList.getModel();
               dropLocationIndex = dropLocation.getIndex();
               isInsert = dropLocation.isInsert();
               
               // Get the current list object under the drop.
               
               listObjectToBeDropped = (SQLQueryBucketListObject) listModel.getElementAt(selectedListIndex);

               // Inserting the object into the list.
               if (isInsert)
               {
                  listSize = sqlQueryList.getModel().getSize();
                  
                  // Before
                  if (dropLocationIndex == 0)
                  {
                     listModel.add(dropLocationIndex, listModel.getElementAt(selectedListIndex));
                     listModel.remove(selectedListIndex + 1); 
                  }
                  // End
                  else if (dropLocationIndex >= listSize)
                  {
                     listModel.addElement(listObjectToBeDropped);
                     listModel.remove(selectedListIndex);
                  }
                  // In
                  else
                  {
                     listModel.add(dropLocationIndex, listModel.getElementAt(selectedListIndex));
                     if (dropLocationIndex < selectedListIndex)
                        listModel.remove(selectedListIndex + 1); 
                     else
                        listModel.remove(selectedListIndex);      
                  }
               }
               // Implemented if want replace. Must set sqlQueryList.setDropMode().
               else
               {
                  //listModel.set(dropLocationIndex,
                                //((DefaultListModel) sqlQueryList.getModel()).getElementAt(selectedListIndex));
               }
               selectedListIndex = -1;
               isThisDropping = false;
               return true;
            }
         }

         // Handling of export of SQL statement.
         public int getSourceActions(JComponent c)
         {
            return COPY;
         }

         protected Transferable createTransferable(JComponent c)
         {
            JList list = (JList) c;
            String sqlStatement = ((SQLQueryBucketListObject) list.getSelectedValue())
                                     .getSQLStatementString();
            return new StringSelection(sqlStatement);
         }
      });
      sqlQueryList.setDropMode(DropMode.INSERT);
      sqlQueryList.addMouseListener(this);

      JScrollPane scrollPane = new JScrollPane(sqlQueryList);
      scrollPane.setOpaque(false);
      scrollPane.getViewport().setOpaque(false);
      scrollPane.add(sqlListPopupMenu);

      listPanel.add(scrollPane, BorderLayout.CENTER);
      mainPanel.add(listPanel, BorderLayout.CENTER);

      // Control components used to manipulate the list items.

      actionButtonPanel = new JPanel();
      actionButtonPanel.setBorder(BorderFactory.createCompoundBorder(
                                               BorderFactory.createEtchedBorder(),
                                               BorderFactory.createEmptyBorder(0, 0, 0, 0)));
      // View Button
      resource = resourceBundle.getResource("SQLQueryBucketFrame.button.View");
      if (resource.equals(""))
         viewButton = new JButton(VIEW);
      else
         viewButton = new JButton(resource);

      viewButton.setMnemonic(KeyEvent.VK_V);
      viewButton.addActionListener(this);
      actionButtonPanel.add(viewButton);

      // Add Button
      resource = resourceBundle.getResource("SQLQueryBucketFrame.button.Add");
      if (resource.equals(""))
         addButton = new JButton(ADD);
      else
         addButton = new JButton(resource);
      addButton.setMnemonic(KeyEvent.VK_A);
      addButton.addActionListener(this);
      actionButtonPanel.add(addButton);

      // Edit Button
      resource = resourceBundle.getResource("SQLQueryBucketFrame.button.Edit");
      if (resource.equals(""))
         editButton = new JButton(EDIT);
      else
         editButton = new JButton(resource);
      editButton.setMnemonic(KeyEvent.VK_E);
      editButton.addActionListener(this);
      actionButtonPanel.add(editButton);

      // Delete Button
      resource = resourceBundle.getResource("SQLQueryBucketFrame.button.Delete");
      if (resource.equals(""))
         deleteButton = new JButton(DELETE);
      else
         deleteButton = new JButton(resource);
      deleteButton.setMnemonic(KeyEvent.VK_D);
      deleteButton.addActionListener(this);
      actionButtonPanel.add(deleteButton);

      mainPanel.add(actionButtonPanel, BorderLayout.SOUTH);

      getContentPane().add(mainPanel);
   }

   //==============================================================
   // ActionEvent Listener method for detecting the user's selection
   // of various components in the frame and taking the appropriate
   // action as required.
   //==============================================================

   public void actionPerformed(ActionEvent evt)
   {
      Object panelSource = evt.getSource();
      // System.out.println(panelSource);

      // MenuBar, Popup, & Button Actions
      if (panelSource instanceof JMenuItem || panelSource instanceof JButton)
      {
         // Instances & Setting Up.
         String actionCommand;

         if (panelSource instanceof JMenuItem)
            actionCommand = ((JMenuItem) panelSource).getActionCommand();
         else
            actionCommand = ((JButton) panelSource).getActionCommand();

         // System.out.println(actionCommand);

         // ====================================
         // File Menu Item Selection Routing
         // ====================================

         // Open
         if (actionCommand.equals(FILE_OPEN))
            openAction(this);

         // Save & Save As...
         else if ((actionCommand.equals(FILE_SAVE) || actionCommand.equals(FILE_SAVE_AS))
                  && ((DefaultListModel) sqlQueryList.getModel()).getSize() != 0)
            saveAction(this, actionCommand);

         // Exit
         else if (actionCommand.equals(EXIT))
         {
            setVisible(false);
            MyJSQLView_JMenuBarActions.setSQLQueryBucketFrameNotVisisble();
         }

         // ====================================
         // Popup Menu & Button Selection Routing
         // ====================================

         // View & Edit
         else if (actionCommand.equals(VIEW) || actionCommand.equals(EDIT))
         {
            if (sqlQueryList.getSelectedIndex() != -1)
            {
               processingBucketListObject = (SQLQueryBucketListObject) sqlQueryList.getSelectedValue();
               createSQLObjectDialog(actionCommand);
               
               if (processItem)
               {
                  DefaultListModel listModel = (DefaultListModel) sqlQueryList.getModel();
                  listModel.set(sqlQueryList.getSelectedIndex(), processingBucketListObject);
                  processItem = false;
               }
            }
         }

         // Add
         else if (actionCommand.equals(ADD))
            addSQLStatement("");
            
         // Delete
         else if (actionCommand.equals(DELETE))
         {
            if (sqlQueryList.getSelectedIndex() != -1)
            {
               Object[] sqlObjects = sqlQueryList.getSelectedValues();

               for (int i = 0; i < sqlObjects.length; i++)
                  ((DefaultListModel) sqlQueryList.getModel()).removeElement(sqlObjects[i]);
            }
         }
         
         // SQL Statement Editor Pane
         else if (actionCommand.equals(DIALOG_SQLSTATEMENT))
         {
            JEditorPane editorPane;
            InputDialog textDialog;
            String textContent;
            JMenuBar editorMenuBar;
            
            // Create an EditorPane to view/edit content.
            
            textContent = processingBucketListObject.getSQLStatementString();
            editorPane = new JEditorPane("text/plain", textContent);
            editorPane.addMouseListener(MyJSQLView.getPopupMenuListener());

            // Create Appropriate Dialog.
            if (lastActionCommand.equals(VIEW))
            {
               textDialog = MyJSQLView_Utils.createTextDialog(false, editorPane);
               editorMenuBar = MyJSQLView_Utils.createEditMenu(false);
            }
            // Edit
            else
            {
               textDialog = MyJSQLView_Utils.createTextDialog(true, editorPane);
               editorMenuBar = MyJSQLView_Utils.createEditMenu(true);
            }
            
            textDialog.setJMenuBar(editorMenuBar);
            textDialog.pack();
            textDialog.center();
            textDialog.setVisible(true);
            
            // Check to see if save data is desired..
            if (textDialog.isActionResult())
            {
               if (lastActionCommand.equals(EDIT) || lastActionCommand.equals(ADD))
               {
                  String editorPaneSQLStatement = editorPane.getText();
                  
                  editorPaneSQLStatement = editorPaneSQLStatement.replaceAll("\r\n", " ");
                  editorPaneSQLStatement = editorPaneSQLStatement.replaceAll("\r", " ");
                  editorPaneSQLStatement = editorPaneSQLStatement.replaceAll("\n", " ");
                  processingBucketListObject.setSQLStatementString(editorPaneSQLStatement);
               }
               else
                  saveSQLStatementFile();
            }
            else if (!textDialog.getActionResult().equals("close")
                     && (lastActionCommand.equals(EDIT) || lastActionCommand.equals(ADD)))
            {
               openSQLStatementFile();
            }

            textDialog.dispose();
         }
         
         // SQL Object Color Setting
         else if (actionCommand.equals(DIALOG_COLOR))
         {
            String resource = resourceBundle.getResource("SQLQueryBucketFrame.title.ItemListColor");
            if (resource.equals(""))
               resource = "Item List Color";
            
            panelColorChooser.setBorder(BorderFactory.createTitledBorder(resource));
            panelColorChooser.setColor(processingBucketListObject.getBackground());
            colorAction = true;

            // Create the color chooser dialog.
            JDialog dialog;
            dialog = JColorChooser.createDialog(this, "Color Selector", true, panelColorChooser, this, null);
            dialog.setVisible(true);
            dialog.dispose();
            return;
         }
         
         // Color Chooser Action
         else if (actionCommand.equals("OK"))
         {
            if (colorAction)
            {
               dialog_colorButton.setBackground(panelColorChooser.getColor());
               colorAction = false;
            }
         }
      }
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
         sqlListPopupMenu.show(evt.getComponent(), evt.getX(), evt.getY());
      else if (evt.getComponent().equals(sqlQueryList))
      {
         selectedListIndex = sqlQueryList.getSelectedIndex();
         // System.out.println(selectedListIndex);
      }
   }

   public void mouseReleased(MouseEvent evt)
   {
      if (evt.isPopupTrigger())
         sqlListPopupMenu.show(evt.getComponent(), evt.getX(), evt.getY());
   }

   public void mouseClicked(MouseEvent e)
   {

   }
   
   //==============================================================
   // Method used for creation of the menu bar that will be used
   // with the frame.
   //==============================================================

   private void createMenuBar(JMenuBar sqlBucketFrameMenuBar)
   {
      // Method Instances
      String resource;
      JMenu fileMenu;

      ImageIcon logoIcon;
      JButton logoIconItem;

      // File Menu
      resource = resourceBundle.getResource("SQLQueryBucketFrame.menu.File");
      if (resource.equals(""))
         fileMenu = new JMenu("File");
      else
         fileMenu = new JMenu(resource);
      fileMenu.setFont(fileMenu.getFont().deriveFont(Font.BOLD));
      fileMenu.addSeparator();

      // Open
      resource = resourceBundle.getResource("SQLQueryBucketFrame.menu.Open");
      if (resource.equals(""))
         fileMenu.add(menuItem("Open", FILE_OPEN));
      else
         fileMenu.add(menuItem(resource, FILE_OPEN));
      fileMenu.addSeparator();

      // Save
      resource = resourceBundle.getResource("SQLQueryBucketFrame.menu.Save");
      if (resource.equals(""))
         fileMenu.add(menuItem("Save", FILE_SAVE));
      else
         fileMenu.add(menuItem(resource, FILE_SAVE));
      resource = resourceBundle.getResource("SQLQueryBucketFrame.menu.SaveAs");
      if (resource.equals(""))
         fileMenu.add(menuItem("Save As...", FILE_SAVE_AS));
      else
         fileMenu.add(menuItem(resource, FILE_SAVE_AS));
      fileMenu.addSeparator();

      resource = resourceBundle.getResource("SQLQueryBucketFrame.menu.Exit");
      if (resource.equals(""))
         fileMenu.add(menuItem("Exit", EXIT));
      else
         fileMenu.add(menuItem(resource, EXIT));
      sqlBucketFrameMenuBar.add(fileMenu);

      fileMenu.addSeparator();

      sqlBucketFrameMenuBar.add(Box.createHorizontalGlue());

      // Logo
      logoIcon = new ImageIcon(iconsDirectory + "myjsqlviewIcon.gif");
      logoIconItem = new JButton(logoIcon);
      logoIconItem.setDisabledIcon(logoIcon);
      logoIconItem.setFocusPainted(false);
      logoIconItem.setBorder(BorderFactory.createLoweredBevelBorder());
      sqlBucketFrameMenuBar.add(logoIconItem);
   }

   //==============================================================
   // Method used for the creation of a pop menu for the SQL Query
   // List in the frame.
   //==============================================================

   private JPopupMenu createPopupMenu()
   {
      // Method Instances
      String resource;
      JMenuItem menuItem = null;
      
      sqlListPopupMenu = new JPopupMenu();

      resource = resourceBundle.getResource("SQLQueryBucketFrame.menu.View");
      if (resource.equals(""))
         menuItem = menuItem("View", VIEW);
      else
         menuItem = menuItem(resource, VIEW);
      sqlListPopupMenu.add(menuItem);

      resource = resourceBundle.getResource("SQLQueryBucketFrame.menu.Add");
      if (resource.equals(""))
         menuItem = menuItem("Add", ADD);
      else
         menuItem = menuItem(resource, ADD);
      sqlListPopupMenu.add(menuItem);

      resource = resourceBundle.getResource("SQLQueryBucketFrame.menu.Edit");
      if (resource.equals(""))
         menuItem = menuItem("Edit", EDIT);
      else
         menuItem = menuItem(resource, EDIT);
      sqlListPopupMenu.add(menuItem);

      resource = resourceBundle.getResource("SQLQueryBucketFrame.menu.Delete");
      if (resource.equals(""))
         menuItem = menuItem("Delete", DELETE);
      else
         menuItem = menuItem(resource, DELETE);

      sqlListPopupMenu.add(menuItem);
      
      return sqlListPopupMenu;
   }

   //==============================================================
   // Method used for the creation of menu bar items. Helper Method.
   //==============================================================

   private JMenuItem menuItem(String label, String actionLabel)
   {
      JMenuItem item = new JMenuItem(label);
      item.addActionListener(this);
      item.setActionCommand(actionLabel);
      return item;
   }

   //==============================================================
   // Class Method to open a saved configuration state file for
   // a database table.
   //==============================================================

   private void openAction(JFrame parent)
   {
      // Method Instances.
      JFileChooser dataFileChooser;
      String fileName;
      FileReader fileReader;
      BufferedReader bufferedReader;
      SQLQueryBucketListObject currentLoadingSQLObject;
      String currentLine;
      String[] sqlObjectParameters;

      // Choosing the directory to import data from.
      if (lastOpenSaveDirectory.equals(""))
         dataFileChooser = new JFileChooser();
      else
         dataFileChooser = new JFileChooser(new File(lastOpenSaveDirectory));

      int result = dataFileChooser.showOpenDialog(parent);

      // Looks like might be good so lets check and read data.
      if (result == JFileChooser.APPROVE_OPTION)
      {
         // Save the selected directory so can be used again.
         lastOpenSaveDirectory = dataFileChooser.getCurrentDirectory().toString();

         // Collect file name.
         fileName = dataFileChooser.getSelectedFile().getName();
         fileName = dataFileChooser.getCurrentDirectory() + fileSeparator + fileName;

         // Try loading SQL Statement objects.
         if (!fileName.equals(""))
         {
            try
            {
               fileReader = new FileReader(fileName);
               bufferedReader = new BufferedReader(fileReader);

               int lineNumber = 1;

               while ((currentLine = bufferedReader.readLine()) != null && lineNumber < 25)
               {
                  // Parse parameters.
                  sqlObjectParameters = currentLine.split(parameterDelimiter, 0);

                  if (sqlObjectParameters.length == 4)
                  {
                     // Create the SQLStatement Object and load the individual
                     // parameters then add to bucket list.
                     try
                     {
                        // Name, SQL Statement, LIMIT, & Color.
                        currentLoadingSQLObject = new SQLQueryBucketListObject();

                        currentLoadingSQLObject.setText(sqlObjectParameters[0]);
                        currentLoadingSQLObject.setSQLStatementString(sqlObjectParameters[1]);
                        if (sqlObjectParameters[2].equals("true"))
                           currentLoadingSQLObject.setLimited(true);
                        else
                           currentLoadingSQLObject.setLimited(false);
                        currentLoadingSQLObject.setBackground(new Color(Integer
                                                   .parseInt(sqlObjectParameters[3])));

                        ((DefaultListModel) sqlQueryList.getModel()).addElement(currentLoadingSQLObject);
                     }
                     catch (NumberFormatException e)
                     {
                        createUnableToReadFileDialog();
                     }
                  }
                  lineNumber++;
               }
               bufferedReader.close();
               fileReader.close();
            }
            catch (IOException e)
            {
               createUnableToReadFileDialog();
            }
         }
         else
         {
            createFileNotFoundDialog();
         }
      }
   }

   //==============================================================
   // Class Method to save the SQL Statements contained in the
   // list to the file system.
   //==============================================================

   private void saveAction(JFrame parent, String actionCommand)
   {
      // Method Instances.
      JFileChooser dataFileChooser;
      int resultsOfFileChooser, listSize;
      DefaultListModel listModel;
      SQLQueryBucketListObject currentSQLBucketObject;

      // Setting up a file name based on whether there has
      // already been a save of the list or save as action.

      // Directory
      if (lastOpenSaveDirectory.equals(""))
         dataFileChooser = new JFileChooser();
      else
         dataFileChooser = new JFileChooser(new File(lastOpenSaveDirectory));

      // Create the File Name.

      if (actionCommand.equals(FILE_SAVE) && !savedFileName.equals(""))
         fileName = savedFileName;
      else
      {
         if (!savedFileName.equals(""))
            fileName = savedFileName;
         else
            fileName = ".txt";
      }

      dataFileChooser.setSelectedFile(new File(fileName));

      // Open the file chooser Dialog as needed.

      if (actionCommand.equals(FILE_SAVE) && !savedFileName.equals(""))
         resultsOfFileChooser = JFileChooser.APPROVE_OPTION;
      else
         resultsOfFileChooser = MyJSQLView_Utils.processFileChooserSelection(parent, dataFileChooser);

      // Looks like might be good so lets check and then write data.
      if (resultsOfFileChooser == JFileChooser.APPROVE_OPTION)
      {
         // Save the selected directory and file name so can be used again.
         lastOpenSaveDirectory = dataFileChooser.getCurrentDirectory().toString();
         fileName = dataFileChooser.getSelectedFile().getName();
         savedFileName = fileName;

         // Get the file name.
         fileName = dataFileChooser.getCurrentDirectory() + fileSeparator + fileName;

         // Collect the list contents as needed.
         if (!fileName.equals(""))
         {
            listModel = ((DefaultListModel) sqlQueryList.getModel());
            listSize = listModel.getSize();
            stringBuffer = new StringBuffer();

            int i = 0;
            while (i < listSize)
            {
               currentSQLBucketObject = (SQLQueryBucketListObject) listModel.getElementAt(i);

               // Object's Visible Name, SQL Statement, Limited, & Color
               // parameter.

               stringBuffer.append(currentSQLBucketObject.getText() + parameterDelimiter);
               stringBuffer.append(currentSQLBucketObject.getSQLStatementString() + parameterDelimiter);
               stringBuffer.append(currentSQLBucketObject.isLimited() + parameterDelimiter);
               stringBuffer.append(currentSQLBucketObject.getBackground().getRGB() + "\n");
               
               i++;
            }
            // System.out.println(stringBuffer.toString());

            // Save contents to file.
            Thread saveActionThread = new Thread(new Runnable()
            {
               public void run()
               {
                  WriteDataFile.mainWriteDataString(fileName, stringBuffer.toString().getBytes(), false);
               }
            }, "SQLQueryBucketFrame.saveActionThread");
            saveActionThread.start();
         }
         else
         {
            createFileNotFoundDialog();
         }
      }
   }

   //==============================================================
   // Class Method to show a problem with loading file data dialog.
   //==============================================================

   private void createUnableToReadFileDialog()
   {
      // Method Instances
      String resourceMessage;

      resourceMessage = resourceBundle.getResource("SQLQueryBucketFrame.dialogmessage.InputFile");
      if (resourceMessage.equals(""))
         resourceMessage = "Unable to Read Input File!";

      JOptionPane.showMessageDialog(null, resourceMessage, resourceAlert, JOptionPane.ERROR_MESSAGE);
   }

   //==============================================================
   // Class Method to show a file not found dialog.
   //==============================================================

   private void createFileNotFoundDialog()
   {
      // Method Instances
      String resourceMessage;

      resourceMessage = resourceBundle.getResource("SQLQueryBucketFrame.dialogmessage.FileNOTFound");
      if (resourceMessage.equals(""))
         resourceMessage = "File NOT Found";

      JOptionPane.showMessageDialog(null, resourceMessage, resourceAlert, JOptionPane.ERROR_MESSAGE);
   }
   
   //==============================================================
   // Class Method to create a dialog needed to view, add, or edit
   // the SQL Statement List objects.
   //==============================================================

   private void createSQLObjectDialog(String actionCommand)
   {
      // Method Instances
      InputDialog formDialog;
      
      JPanel componentHoldingPanel;
      JPanel namePanel, sqlStatementPanel;
      JTextField listItemNameTextField;
      JLabel sqlStatementLabel, colorLabel, limitLabel;
      JCheckBox limitCheckBox;
      
      String resource, resourceOK, resourceCancel;
      
      GridBagLayout gridbag = new GridBagLayout();
      GridBagConstraints constraints = new GridBagConstraints();
      
      // Create container to hold the components for the dialog.
      
      componentHoldingPanel = new JPanel(gridbag);
      componentHoldingPanel.setBorder(BorderFactory.createEtchedBorder());
      lastActionCommand = actionCommand;
      
      // SQLQueryBucketListObject
      // Name, SQL Statement, Color, LIMIT
      
      // Name
      namePanel = new JPanel();
      namePanel.setBorder(BorderFactory.createRaisedBevelBorder());
      
      listItemNameTextField = new JTextField(35);
      listItemNameTextField.setMargin(new Insets(1, 0, 0, 0));
      
      if (actionCommand.equals(VIEW) || actionCommand.equals(EDIT))
         listItemNameTextField.setText(processingBucketListObject.getText());
      
      if (actionCommand.equals(VIEW))
         listItemNameTextField.setEnabled(false);
         
      namePanel.add(listItemNameTextField);
      
      buildConstraints(constraints, 0, 0, 1, 1, 100, 25);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(namePanel, constraints);
      componentHoldingPanel.add(namePanel);
      
      // SQL Statement
      sqlStatementPanel = new JPanel(gridbag);
      sqlStatementPanel.getInsets(new Insets(1, 1, 1, 1));
      sqlStatementPanel.setBorder(BorderFactory.createCompoundBorder(
                             BorderFactory.createLoweredBevelBorder(),
                             BorderFactory.createEmptyBorder(4, 1, 4, 1)));
      
      dialog_sqlStatementButton = new JButton(new ImageIcon(iconsDirectory + "editSQLQueryIcon.png"));
      dialog_sqlStatementButton.setFocusable(false);
      dialog_sqlStatementButton.setMargin(new Insets(0, 0, 0, 0));
      dialog_sqlStatementButton.setActionCommand(DIALOG_SQLSTATEMENT);
      dialog_sqlStatementButton.addActionListener(this);
      
      buildConstraints(constraints, 0, 0, 1, 1, 10, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(dialog_sqlStatementButton, constraints);
      sqlStatementPanel.add(dialog_sqlStatementButton);
      
      resource = resourceBundle.getResource("SQLQueryBucketFrame.label.SQLStatementString");
      if (resource.equals(""))
         sqlStatementLabel = new JLabel("SQL Statement String");
      else
         sqlStatementLabel = new JLabel(resource);
      
      buildConstraints(constraints, 1, 0, 1, 1, 90, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.WEST;
      gridbag.setConstraints(sqlStatementLabel, constraints);
      sqlStatementPanel.add(sqlStatementLabel);
      
      // LIMIT
      limitCheckBox = new JCheckBox(new ImageIcon(iconsDirectory + "limitUpIcon.png"));
      limitCheckBox.setSelectedIcon(new ImageIcon(iconsDirectory + "limitDownIcon.png"));
      limitCheckBox.setPreferredSize(dialog_sqlStatementButton.getPreferredSize());
      limitCheckBox.setMargin(new Insets(4, 1, 4, 1));
      limitCheckBox.setBorder(BorderFactory.createRaisedBevelBorder());
      limitCheckBox.setSelected(processingBucketListObject.isLimited());
      
      if (actionCommand.equals(VIEW))
         limitCheckBox.setEnabled(false);
      
      buildConstraints(constraints, 0, 1, 1, 1, 0, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(limitCheckBox, constraints);
      sqlStatementPanel.add(limitCheckBox);
      
      resource = resourceBundle.getResource("SQLQueryBucketFrame.label.LIMIT");
      if (resource.equals(""))
         limitLabel = new JLabel("LIMIT");
      else
         limitLabel = new JLabel(resource);
      
      buildConstraints(constraints, 1, 1, 1, 1, 0, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.WEST;
      gridbag.setConstraints(limitLabel, constraints);
      sqlStatementPanel.add(limitLabel);
      
      // Color
      dialog_colorButton = new JButton(new ImageIcon(iconsDirectory + "transparentUpIcon.png"));
      dialog_colorButton.setBackground(processingBucketListObject.getBackground());
      dialog_colorButton.setFocusable(false);
      dialog_colorButton.setMargin(new Insets(0, 0, 0, 0));
      dialog_colorButton.setActionCommand(DIALOG_COLOR);
      
      if (actionCommand.equals(VIEW))
         dialog_colorButton.setEnabled(false);
      else
         dialog_colorButton.addActionListener(this);
      
      buildConstraints(constraints, 0, 2, 1, 1, 0, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(dialog_colorButton, constraints);
      sqlStatementPanel.add(dialog_colorButton);
      
      resource = resourceBundle.getResource("SQLQueryBucketFrame.label.Color");
      if (resource.equals(""))
         colorLabel = new JLabel("Color");
      else
         colorLabel = new JLabel(resource);
      
      buildConstraints(constraints, 1, 2, 1, 1, 0, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.WEST;
      gridbag.setConstraints(colorLabel, constraints);
      sqlStatementPanel.add(colorLabel);
      
      buildConstraints(constraints, 0, 1, 1, 1, 100, 75);
      constraints.fill = GridBagConstraints.BOTH;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(sqlStatementPanel, constraints);
      componentHoldingPanel.add(sqlStatementPanel);

      Object content[] = {componentHoldingPanel};

      resource = resourceBundle.getResource("SQLQueryBucketFrame.title.SQLStatementObject");
      if (resource.equals(""))
         resource = "SQL Statement Object";
      
      resourceOK = resourceBundle.getResource("SQLQueryBucketFrame.button.OK");
      if (resourceOK.equals(""))
         resourceOK = "OK";
      
      resourceCancel = resourceBundle.getResource("SQLQueryBucketFrame.button.Cancel");
      if (resourceCancel.equals(""))
         resourceCancel = "Cancel";
      
      formDialog = new InputDialog(null, resource, resourceOK, resourceCancel,
                                   content, null);
      formDialog.pack();
      formDialog.setResizable(false);
      formDialog.center();
      formDialog.setVisible(true);
      
      // Collect the information for the SQL Statement Object
      // and Add it to the list as needed.

      if (formDialog.isActionResult())
      {
         if (!actionCommand.equals(VIEW))
         {  
            if (listItemNameTextField.getText().equals(""))
               processingBucketListObject.setText("");
            else
               processingBucketListObject.setText(listItemNameTextField.getText());
            
            // Don't Set it here, already set or will be if edited
            // via the Action Event DIALOG_STATEMENT.
            //processingBucketListObject.setSQLStatementString();
            
            processingBucketListObject.setLimited(limitCheckBox.isSelected());
            processingBucketListObject.setBackground(dialog_colorButton.getBackground());
            
            processItem = true;
         }
      }
   }
   
   //==============================================================
   // Class method to save a SQL Statement from an individual list
   // bucket object to a selected output file.
   //==============================================================

   private void saveSQLStatementFile()
   {
      // Class Method Instance
      String fileName;
      byte[] buf;
      JFileChooser exportDataChooser;
      int resultsOfFileChooser;

      // Setting up a file separator instance.
      String fileSeparator = MyJSQLView_Utils.getFileSeparator();
      
      buf = processingBucketListObject.getSQLStatementString().getBytes();

      // Choosing the file to export data to.
      exportDataChooser = new JFileChooser();
      resultsOfFileChooser = MyJSQLView_Utils.processFileChooserSelection(null, exportDataChooser);

      // Looks like might be good file name so lets check
      // and then output the text data
      
      if (resultsOfFileChooser == JFileChooser.APPROVE_OPTION)
      {
         fileName = exportDataChooser.getSelectedFile().getName();
         fileName = exportDataChooser.getCurrentDirectory() + fileSeparator + fileName;
         // System.out.println(fileName);

         if (!fileName.equals(""))
         {
            // Creating the buffered data of the text
            // and outputing.
            try
            {
               // Setting up OutputStream
               FileOutputStream fileStream = new FileOutputStream(fileName);
               BufferedOutputStream filebuff = new BufferedOutputStream(fileStream);

               // Writing to the Specified Ouput File.
               for (int i = 0; i < buf.length; i++)
               {
                  filebuff.write(buf[i]);
                  // System.out.print(buf[i]);
               }
               filebuff.flush();
               filebuff.close();
            }
            catch (IOException e)
            {
               String resourceMessage;
               
               resourceMessage = resourceBundle.getResource("SQLQueryBucketFrame.dialogmessage.ErrorWritingDataFile");
               if (resourceMessage.equals(""))
                  resourceMessage = "Error Writing Data File!";
               
               JOptionPane.showMessageDialog(null, resourceMessage + " " + fileName, resourceAlert,
                  JOptionPane.ERROR_MESSAGE);
            }
         }
         else
         {
            createUnableToReadFileDialog();
         }
      }
      else
      {
         // System.out.println("File Selection Canceled");
      }
   }
   
   //==============================================================
   // Class method to set a SQL Statement for the current editing
   // list bucket item from a selected input file.
   //==============================================================

   private void openSQLStatementFile()
   {
      // Class Method Instance
      String fileName;

      // Choosing the file to import data from.
      JFileChooser importData = new JFileChooser();
      int result = importData.showOpenDialog(null);

      // Looks like might be good file name so lets check
      // and then set the sql statement on the object.
      
      if (result == JFileChooser.APPROVE_OPTION)
      {
         fileName = importData.getSelectedFile().getName();
         fileName = importData.getCurrentDirectory() + "/" + fileName;
         // System.out.println(fileName);

         if (!fileName.equals(""))
         {
            try
            { 
               // Setting up InputReader
               FileReader fileReader = new FileReader(fileName);
               BufferedReader filebuff = new BufferedReader(fileReader);
               StringBuffer textString;
               String inputString;

               // Reading the Specified Input File and Placing
               // Data Into a StringBuffer.

               textString = new StringBuffer();
               
               while ((inputString = filebuff.readLine()) != null)
                  textString.append(inputString);
               
               processingBucketListObject.setSQLStatementString(textString.toString());
               
               filebuff.close();
               fileReader.close();
            }
            catch (IOException e)
            {
               String resource, message;

               resource = resourceBundle.getResource("SQLQueryBucketFrame.dialogmessage.ErrorReading");
               if (resource.equals(""))
                  message = "Error Reading Data File";
               else
                  message = resource;
               JOptionPane.showMessageDialog(null, message + ": " + fileName, resourceAlert,
                                             JOptionPane.ERROR_MESSAGE);
            }
         }
      }
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
   // Class methods to add a SQL statement to the bucket's list.
   //==============================================================

   public void addSQLStatement(String sqlStatement)
   {
      setVisible(true);
      // System.out.println(sqlStatement);
      
      processingBucketListObject = new SQLQueryBucketListObject();
      processingBucketListObject.setText("");
      processingBucketListObject.setSQLStatementString(sqlStatement);
      processingBucketListObject.setLimited(true);
      processingBucketListObject.setBackground(getBackground());
      
      createSQLObjectDialog(ADD);
      
      if (processItem == true)
         ((DefaultListModel) sqlQueryList.getModel()).addElement(processingBucketListObject);
      
      processItem = false;
   }
   
   //==============================================================
   // Class Method to open the last used list by the bucket.
   //==============================================================

   protected void openLastUsedList(String databaseName)
   {
      // Method Instances.
      FileReader fileReader;
      String fileName;
      BufferedReader bufferedReader;
      SQLQueryBucketListObject currentLoadingSQLObject;
      String currentLine;
      String[] sqlObjectParameters;
      
      this.databaseName = databaseName;

      try
      {
         fileName = MyJSQLView_Utils.getMyJSQLViewDirectory() + MyJSQLView_Utils.getFileSeparator()
                    + DEFAULT_LIST_LAST_USED_FILENAME + databaseName + ".txt"; 
         fileReader = new FileReader(fileName);
         bufferedReader = new BufferedReader(fileReader);

         int lineNumber = 1;

         while ((currentLine = bufferedReader.readLine()) != null && lineNumber < 25)
         {
            // Parse parameters.
            sqlObjectParameters = currentLine.split(parameterDelimiter, 0);

            if (sqlObjectParameters.length == 4)
            {
               // Create the SQLStatement Object and load the individual
               // parameters then add to bucket list.
               try
               {
                  // Name, SQL Statement, LIMIT, & Color.
                  currentLoadingSQLObject = new SQLQueryBucketListObject();

                  currentLoadingSQLObject.setText(sqlObjectParameters[0]);
                  currentLoadingSQLObject.setSQLStatementString(sqlObjectParameters[1]);
                  if (sqlObjectParameters[2].equals("true"))
                     currentLoadingSQLObject.setLimited(true);
                  else
                     currentLoadingSQLObject.setLimited(false);
                  currentLoadingSQLObject.setBackground(new Color(Integer
                                             .parseInt(sqlObjectParameters[3])));

                  ((DefaultListModel) sqlQueryList.getModel()).addElement(currentLoadingSQLObject);
               }
               catch (NumberFormatException e){}
            }
            lineNumber++;
         }
         bufferedReader.close();
         fileReader.close();
      }
      catch (IOException e){}
   }
   
   //==============================================================
   // Class Method to save the last used list for the bucket on
   // closing of the MyJSQLView application.
   //==============================================================

   protected void saveLastUsedList()
   {
      // Method Instances.
      int listSize;
      DefaultListModel listModel;
      SQLQueryBucketListObject currentSQLBucketObject;
      String fileName;

      // Setup
      listModel = ((DefaultListModel) sqlQueryList.getModel());
      listSize = listModel.getSize();
      stringBuffer = new StringBuffer();
      fileName = MyJSQLView_Utils.getMyJSQLViewDirectory() + MyJSQLView_Utils.getFileSeparator()
                 + DEFAULT_LIST_LAST_USED_FILENAME + databaseName + ".txt";  

      // Collect the list.
      int i = 0;
      while (i < listSize)
      {
         currentSQLBucketObject = (SQLQueryBucketListObject) listModel.getElementAt(i);

         // Object's Visible Name, SQL Statement, Limited, & Color
         // parameter.

         stringBuffer.append(currentSQLBucketObject.getText() + parameterDelimiter);
         stringBuffer.append(currentSQLBucketObject.getSQLStatementString() + parameterDelimiter);
         stringBuffer.append(currentSQLBucketObject.isLimited() + parameterDelimiter);
         stringBuffer.append(currentSQLBucketObject.getBackground().getRGB() + "\n");
         
         i++;
      }
      // System.out.println(stringBuffer.toString());
      
      // Save the list
      if (stringBuffer.length() != 0)
         WriteDataFile.mainWriteDataString(fileName, stringBuffer.toString().getBytes(), false);
   }

   //==============================================================
   // Private Class used exclusively by this frame to paint a
   // background behind the sqlQueryList component. Hey functionality
   // gets boring, how about some creativity from the LandMines
   // project!
   //==============================================================

   private static class ListHoldingPanel extends JPanel
   {
      private static final long serialVersionUID = -393463236488405775L;
      
      static final float alpha = 0.7f;

      public void paintComponent(Graphics g)
      {
         // Method Instances.
         int objectNumber = 65;
         
         Graphics2D g2D;
         BasicStroke pen_solid;
         int widthBound, heightBound;
         double randomNumber;
         int positionX, positionY;
         int shapeWidth, shapeHeight;
         Color[] baseColors = new Color[2];
         Paint paintGradient;
         
         // Setting up.
         g2D = (Graphics2D) g;
         g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
         g2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
         
         pen_solid = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND);
         
         g2D.setStroke(pen_solid);
         g2D.setPaint(getParent().getBackground());
         
         widthBound = getWidth();
         heightBound = getHeight();
         g2D.fillRect(0, 0, widthBound, heightBound);
         
         // Creating graphics.
         
         for (int i=0; i < objectNumber; i++)
         {
            // Postitioning
            randomNumber = widthBound * Math.random();
            positionX = (int) Math.rint(randomNumber) - 50;
            randomNumber = heightBound * Math.random();
            positionY = (int) Math.rint(randomNumber) - 50;
            
            // Sizing
            randomNumber = 10 + (200 * Math.random());
            shapeWidth = (int)Math.rint(randomNumber);
            randomNumber = 10 + (200 * Math.random());
            shapeHeight = (int)Math.rint(randomNumber);
            
            // Colors
            for (int j=0; j < baseColors.length; j++)
            {
               baseColors[j] = new Color((int)Math.rint(15 + 240*Math.random()),
                                         (int)Math.rint(15 + 240*Math.random()),
                                         (int)Math.rint(15 + 240*Math.random()));
            }
            
            paintGradient = new GradientPaint(0.0F, 0.0F, baseColors[0], 0.0F, (shapeHeight / 2.0F),
                                              baseColors[1], true);
            g2D.setPaint(paintGradient);
            
            // Painting
            randomNumber = 2 * Math.random();
            
            if ((int)Math.rint(randomNumber) == 0)
               g2D.fillOval(positionX, positionY, shapeWidth, shapeHeight);
            else if ((int)Math.rint(randomNumber) == 1)
               g2D.fillRect(positionX, positionY, shapeWidth, shapeHeight);
            else
            {
               g2D.setPaint(baseColors[1]);
               g2D.fill3DRect(positionX, positionY, shapeWidth, shapeHeight, true);
            }
         }
      }
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