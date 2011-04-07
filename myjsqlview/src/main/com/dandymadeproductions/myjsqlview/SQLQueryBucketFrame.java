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
// Copyright (C) 2005-2011 Dana M. Proctor
// Version 1.1 04/07/2011
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
//         
//-----------------------------------------------------------------
//                 danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
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
 * @version 1.1 04/07/2011
 */

public class SQLQueryBucketFrame extends JFrame implements ActionListener, MouseListener
{
   // Class Instances.
   private static final long serialVersionUID = -1903579213881382782L;

   private static final String FILE_OPEN = "FO";
   private static final String FILE_SAVE = "FS";
   private static final String FILE_SAVE_AS = "FSA";
   private static final String EXIT = "FE";

   private static final String VIEW = "View";
   private static final String ADD = "Add";
   private static final String EDIT = "Edit";
   private static final String DELETE = "Delete";

   private static final String parameterDelimiter = "%;%";

   private JList sqlQueryList;
   private JPopupMenu sqlListPopupMenu;

   private JButton viewButton;
   private JButton addButton;
   private JButton editButton;
   private JButton deleteButton;

   private MyJSQLView_ResourceBundle resourceBundle;
   private String fileSeparator, iconsDirectory;
   private String lastOpenSaveDirectory, savedFileName;

   private String fileName;
   private StringBuffer stringBuffer;
   private String resourceAlertTitle;

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

      resource = resourceBundle.getResource("SQLQueryBucketFrame.message.Title");
      if (resource.equals(""))
         setTitle("MyJSQLView Query Bucket Frame");
      else
         setTitle("MyJSQLView " + resource);

      resourceAlertTitle = resourceBundle.getResource("SQLQueryBucketFrame.dialogtitle.Alert");
      if (resourceAlertTitle.equals(""))
         resourceAlertTitle = "Alert";

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

      // SQL query drop container, JList and its popup menu.

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

      sqlQueryList.setTransferHandler(new TransferHandler()
      {
         private static final long serialVersionUID = 2558730071314905153L;

         // No imports to list allowed.
         public boolean canImport(TransferHandler.TransferSupport info)
         {
            return false;
         }

         public boolean importData(TransferHandler.TransferSupport info)
         {
            return false;
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

      sqlQueryList.addMouseListener(this);

      JScrollPane scrollPane = new JScrollPane(sqlQueryList);
      scrollPane.setOpaque(false);
      scrollPane.getViewport().setOpaque(false);
      scrollPane.add(sqlListPopupMenu);
      // scrollPane.setPreferredSize(new Dimension(400, 100));

      listPanel.add(scrollPane, BorderLayout.CENTER);
      mainPanel.add(listPanel, BorderLayout.CENTER);

      // Control components used to manipulate the list items.

      actionButtonPanel = new JPanel();
      actionButtonPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(),
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

   // ==============================================================
   // ActionEvent Listener method for detecting the user's selection
   // of various components in the frame and taking the appropriate
   // action as required.
   // ==============================================================

   public void actionPerformed(ActionEvent evt)
   {
      Object panelSource = evt.getSource();
      // System.out.println(panelSource);

      // MenuBar Actions
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
            this.setVisible(false);

         // ====================================
         // Popup Menu & Button Selection Routing
         // ====================================

         else if (actionCommand.equals(VIEW))
         {
            System.out.println(VIEW);
         }

         else if (actionCommand.equals(ADD))
         {
            System.out.println(ADD);
            addSQLStatement();
         }

         else if (actionCommand.equals(EDIT))
         {
            System.out.println(EDIT);
         }

         // Delete
         else
         {
            if (sqlQueryList.getSelectedIndex() != -1)
            {
               Object[] sqlObjects = sqlQueryList.getSelectedValues();

               for (int i = 0; i < sqlObjects.length; i++)
                  ((DefaultListModel) sqlQueryList.getModel()).removeElement(sqlObjects[i]);
            }
         }
      }
   }

   // ==============================================================
   // MouseEvent Listener methods for detecting mouse events.
   // MounseListner Interface requirements.
   // ==============================================================

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
   }

   public void mouseReleased(MouseEvent evt)
   {
      if (evt.isPopupTrigger())
         sqlListPopupMenu.show(evt.getComponent(), evt.getX(), evt.getY());
   }

   public void mouseClicked(MouseEvent e)
   {

   }

   // ==============================================================
   // Method used for creation of the menu bar that will be used
   // with the frame.
   // ==============================================================

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

   // ==============================================================
   // Method used for the creation of a pop menu for the SQL Query
   // List in the frame.
   // ==============================================================

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

   // ==============================================================
   // Method used for the creation of menu bar items. Helper Method.
   // ==============================================================

   private JMenuItem menuItem(String label, String actionLabel)
   {
      JMenuItem item = new JMenuItem(label);
      item.addActionListener(this);
      item.setActionCommand(actionLabel);
      return item;
   }

   // ==============================================================
   // Class Method to open a saved configuration state file for
   // a database table.
   // ==============================================================

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
                        currentLoadingSQLObject = new SQLQueryBucketListObject();

                        currentLoadingSQLObject.setText(sqlObjectParameters[0]);
                        currentLoadingSQLObject.setBackground(new Color(Integer
                              .parseInt(sqlObjectParameters[1])));
                        currentLoadingSQLObject.setSQLStatementString(sqlObjectParameters[2]);
                        if (sqlObjectParameters[2].equals("true"))
                           currentLoadingSQLObject.setLimited(true);
                        else
                           currentLoadingSQLObject.setLimited(false);

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

   // ==============================================================
   // Class Method to save the SQL Statements contained in the
   // list to the file system.
   // ==============================================================

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

               // Object's Visible Name, Color, SQL Statement, & Limited
               // parameter.

               stringBuffer.append(currentSQLBucketObject.getText() + parameterDelimiter);
               stringBuffer.append(currentSQLBucketObject.getBackground().getRGB() + parameterDelimiter);
               stringBuffer.append(currentSQLBucketObject.getSQLStatementString() + parameterDelimiter);
               stringBuffer.append(currentSQLBucketObject.isLimited() + "\n");
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

   // ==============================================================
   // Class Method to show a problem with loading file data dialog.
   // ==============================================================

   private void createUnableToReadFileDialog()
   {
      // Method Instances
      String resourceMessage;

      resourceMessage = resourceBundle.getResource("SQLQueryBucketFrame.dialogmessage.InputFile");
      if (resourceMessage.equals(""))
         resourceMessage = "Unable to Read Input File!";

      JOptionPane.showMessageDialog(null, resourceMessage, resourceAlertTitle, JOptionPane.ERROR_MESSAGE);
   }

   // ==============================================================
   // Class Method to show a file not found dialog.
   // ==============================================================

   private void createFileNotFoundDialog()
   {
      // Method Instances
      String resourceMessage;

      resourceMessage = resourceBundle.getResource("SQLQueryBucketFrame.dialogmessage.FileNOTFound");
      if (resourceMessage.equals(""))
         resourceMessage = "File NOT Found";

      JOptionPane.showMessageDialog(null, resourceMessage, resourceAlertTitle, JOptionPane.ERROR_MESSAGE);
   }

   // ==============================================================
   // Class methods to add a SQL statement to the bucket's list.
   // ==============================================================

   private void addSQLStatement()
   {
      createListItem("");
   }

   public void addSQLStatement(String sqlStatement)
   {
      System.out.println(sqlStatement);
      createListItem(sqlStatement);
      setVisible(true);
   }

   //==============================================================
   // Class method to add an item, SQLQueryBucketListObject, to
   // the list that contains the SQL Statements Objects associated
   // with the bucket.
   //==============================================================

   private void createListItem(String sqlStatement)
   {
      // New list item from this frame.
      if (sqlStatement.equals(""))
      {
         System.out.println("Creating New List Item");
      }
      // New list item from calling class.
      else
      {
         SQLQueryBucketListObject listItemObject = new SQLQueryBucketListObject();
         listItemObject.setSQLStatementString(sqlStatement);
         listItemObject.setText(sqlQueryList.getModel().getSize() + "");
         ((DefaultListModel) sqlQueryList.getModel()).addElement(listItemObject);
      }
   }

   //==============================================================
   // Private Class used exclusively by this frame to paint a
   // background behind the sqlQueryList component. Hey functionality
   // gets boring, how about some creativity from the LandMines
   // project!
   //==============================================================

   private class ListHoldingPanel extends JPanel
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