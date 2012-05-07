///================================================================
//                  MyJSQLView Table View Form
//=================================================================
//
//    This class provides a generic panel in the appearance of a
// form for viewing the current selected item in the TableTabPanel
// summary table.
//
//                 << TableViewForm.java >>
//
//=================================================================
// Copyright (C) 2005-2012 Dana M. Proctor
// Version 6.4 05/07/2012
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
// Version 1.0 12/27/2005 Original MyJSQLView TableViewForm Class.
//         1.1 01/18/2006 Code Clean Up to Remove Any Special
//                        Formatting and Header Panel.
//         1.2 01/22/2006 Passed doneButton as Argument in Constructor.
//         1.3 05/29/2006 Removed Class Instance size.
//         1.4 07/16/2006 Constructor Arguments previousViewButton &
//                        nextViewButton.
//         1.5 08/14/2006 Constructor Argument typeColumnType and
//                        Class Method setFormFieldBytes().
//         1.6 08/14/2006 Class Method actionPerformed to Handle
//                        the Blob Type JButton Elements. Saving
//                        of Blob Data.
//         1,7 09/28/2006 Blob Data Type Distinction in Constructor.
//         1.8 09/28/2006 Constructor Argument tableColumnClass and
//                        Class Instance.
//         1.9 04/20/2007 Added PopupListener for Copying Data.
//         2.0 05/30/2007 Added Key Listener for Allowing the Return
//                        to the Summary Table on Done, VK_ENTER, and
//                        Movement to Previous/Next Entries Via Left/Right
//                        Arrow Keys.
//         2.1 05/31/2007 Implemented Text, MediumText, & LongText Similar
//                        to Blob Types. Buttons to Save/Indicate Bytes.
//         2.2 05/31/2007 Class Method saveBlobTextField() and Taking Care
//                        of Viewing Text, MediumText, and LongText in the
//                        actionPerformed() Method.
//         2.3 05/31/2007 InputDialog String Argument "close" for actionPerformed()
//                        textDialog Instance.
//         2.4 06/01/2007 Modified the Way the EditorPane Displays Data for A
//                        TEXT Field in the Class Method actionPerformed().
//                        Added Class Method clearBlobBytesHashMap.
//         2.5 06/06/2007 Modified Detection of TEXT, MEDIUMTEXT, and LONGTEXT
//                        for Newer Version of MySQL 5.0 in Class Methods
//                        Constructor & setFormField(). Added Class Methods
//                        setFormFieldBlob() & setFormFieldText(). Removed
//                        setFormField(). Insure Blob Data Obtained as Bytes
//                        in Class Method saveBlobTextField().
//         2.6 06/11/2007 Removed TEXT From a Text Button. Limited the Size
//                        of String Content to 600 and Set Caret Back to Zero
//                        in Class Method setFormField().
//         2.7 08/24/2007 Set the formPanel Instance Preferred Size So That
//                        the formScrollPane Would Properly Display Scrollers
//                        as Needed.
//         2.8 09/08/2007 Code Cleanup.
//         2.9 09/14/2007 Returned TEXT Fields To Be Viewed As Buttons in the
//                        Form. Class Constructor & Method setFormField(),
//                        columnSizeHashMap.get(currentColumnName)).intValue() > 255.
//         3.0 09/19/2007 Used System.getProperty("file.separator") for All File
//                        System Resources Accesses Through Instance fileSeparator.
//         3.1 09/26/2007 Added PopupMenu and MenuBar for Copy, & Select All
//                        for EditorPane in Class Method actionPerformed(). Cut &
//                        Paste Not Enabled in View Panel.Added Class Method
//                        createEditMenu().
//         3.2 10/21/2007 Cleaned Up Javadoc Comments.
//         3.3 10/22/2007 Corrected Javadoc Comments.
//         3.4 10/30/2007 Handling of PostgreSQL BYTEA Data in Constructor &
//                        Class Methods actionPerformed(), saveBlobTextField() &
//                        setFormField().
//         3.5 10/31/2007 Reviewed, Changed Comments. Looks Clean. Corrected OR
//                        Operator to || in Class Method actionPerformed().
//         3.6 12/12/2007 Header Update.
//         3.7 01/20/2008 Handling of HSQL BINARY Data in Constructor &
//                        Class Methods actionPerformed(), saveBlobTextField()
//                        & setFormField().
//         3.8 02/06/2008 Replaced doneButton With closeViewButton.
//         3.9 03/05/2008 Added Arrays to Displayed & Edited Like Text Fields.
//                        Effected Methods Constructor, & setFormField(Object,Object).
//                        Added Instances columnClass & columnType to Constructor
//                        & setFormField(Object,Object).
//         4.0 05/13/2008 Added Class Instance serialVersionUID.
//         4.1 05/24/2008 Implemtation of Oracle LONG Type Fields as TEXT
//                        Content. Class Methods Effected Constructor, &
//                        setFormField().
//         4.2 05/25/2008 Class Method setFormField(Object,Object) Proper
//                        Conditional Check to Catch Oracle BLOB Field Types.
//         4.3 05/26/2008 Implementation of Processing for Oracle RAW & LONG
//                        RAW Data Types for the Oracle Database. Class Methods
//                        Effected Constructor, actionPerformed(),
//                        saveBlobTextField() & setFormField().
//         4.4 06/02/2008 Initial Implementation of Processing for CLOB Data
//                        Types. Class Methods Effected Constructor,
//                        actionPerformed(), saveBlobTextField() & setFormField().
//         4.5 06/03/2008 Moved CLOB Data Types to Be Treated the Same as
//                        BLOB & RAW Data. Can Only Save to Disk Instead of
//                        Viewing in a Edit Text Frame.
//         4.6 06/04/2008 Corrected View Selection of Data to Obtain the Button
//                        Data From CLOB as byte[] in Class Method
//                        saveBlobTextField().
//         4.7 06/04/2008 Reformatted.
//         4.8 10/21/2008 MyJSQLView Project Common Source Code Formatting.
//         4.9 05/27/2009 Header Format Changes/Update.
//         5.0 05/29/2009 Modified the Layout in the formPanel to be a GridBagLayout
//                        Instead of NULL.
//         5.1 06/13/2009 Removed the Setting of the currentField For TextFields of
//                        Non-Editable, in Constructor.
//         5.2 11/06/2009 Class Method saveBlobTextField() Organized Method Instances
//                        and Implemented MyJSQLView_Utils.processFileChooserSelection()
//                        to Allow Warning of Overwriting file.
//         5.3 02/18/2010 Changed Package to Reflect Dandy Made Productions Code.
//         5.4 04/28/2010 Added Class Instance resourceBundle and Implementation of
//                        Iternationlization. Class Methods actionPerformed() &
//                        createEditMenu().
//         5.5 05/19/2010 Parameterized Class Instances fieldHashMap, blobBytesHashMap,
//                        fieldTypeHashMap, fieldClassHashMap, & fieldSizeHashMap In
//                        Order to Bring The Code Into Compiance With Java 5.0 API.
//         5.6 05/19/2010 Parameterized columnNamesIterator in Constructor.
//         5.7 08/25/2010 Added Resource For Message Dialog in saveBlobTextField().
//         5.8 01/27/2011 Copyright Update.
//         5.9 04/09/2011 Moved Class Method createEditMenu() to MyJSQLView_Utils Class.
//         6.0 04/09/2011 Class Method actionPerformed() Standardized Text/Array Viewing
//                        & Saving via the Help of New Methods MyJSQLView_Utils.createTextDialog()
//                        & MyJSQLView_Utils.createEditMenu(true);
//         6.1 04/11/2011 Correction in Class Method saveBlobTextField() When resourceMessage
//                        is an Empty String.
//         6.2 07/25/2011 Clob Types Treated as Text, Character, Data. Class Methods
//                        Effected actionPerformed() & saveBlobTextField().
//         6.3 01/01/2012 Copyright Update.
//         6.4 05/07/2012 Constructor Argument tableColumnNames Changed from Vector Data
//                        Type to ArrayList.
//
//-----------------------------------------------------------------
//                 danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ArrayList;
import javax.swing.*;

/**
 *    The TableViewForm class provides a generic panel in the
 * appearance of a form for viewing the current selected item
 * in the TableTabPanel summary table.
 * 
 * @author Dana M. Proctor
 * @version 6.4 05/07/2012
 */

class TableViewForm extends JPanel implements ActionListener, KeyListener
{
   // Class Instances.
   private static final long serialVersionUID = 6369040292909526934L;

   private HashMap<String, JComponent> fieldHashMap;
   private HashMap<JButton, Object> blobBytesHashMap;
   private HashMap<String, String> fieldTypeHashMap;
   private HashMap<String, String> fieldClassHashMap;
   private HashMap<String, Integer> fieldSizeHashMap;
   private JButton previousViewButton, closeViewButton, nextViewButton;
   private MyJSQLView_ResourceBundle resourceBundle;

   //==============================================================
   // TableViewForm Constructor
   //==============================================================

   protected TableViewForm(ArrayList<String> tableColumnNames,
                           HashMap<String, String> tableColumnClass,
                           HashMap<String, String> tableColumnType,
                           HashMap<String, Integer> tableColumnSize,
                           JButton previousViewButton, JButton closeViewButton,
                           JButton nextViewButton)
   {
      this.previousViewButton = previousViewButton;
      this.closeViewButton = closeViewButton;
      this.nextViewButton = nextViewButton;
      fieldTypeHashMap = tableColumnType;
      fieldClassHashMap = tableColumnClass;
      fieldSizeHashMap = tableColumnSize;

      // Constructor Instances
      GridBagLayout gridbag;
      GridBagConstraints constraints;

      Iterator<String> columnNamesIterator;
      fieldHashMap = new HashMap <String, JComponent>();
      blobBytesHashMap = new HashMap <JButton, Object>();
      String itemName, columnClass, columnType;
      Object currentField;
      resourceBundle = MyJSQLView.getLocaleResourceBundle();

      // General Panel Configurations
      setLayout(new BorderLayout());
      setBorder(BorderFactory.createRaisedBevelBorder());

      // Creating the Labels and Text Fields/Buttons in
      // the form panel.
      
      gridbag = new GridBagLayout();
      constraints = new GridBagConstraints();

      JPanel formPanel = new JPanel();
      formPanel.setLayout(gridbag);
      formPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(),
                                                             BorderFactory.createEmptyBorder(10, 0, 10, 12)));
      formPanel.addMouseListener(MyJSQLView.getPopupMenuListener());
      
      columnNamesIterator = tableColumnNames.iterator();
      int x = 0;
      int y = 0;

      while (columnNamesIterator.hasNext())
      {
         itemName = columnNamesIterator.next();
         columnClass = fieldClassHashMap.get(itemName);
         columnType = fieldTypeHashMap.get(itemName);
         // System.out.println(x + " " + y + " " + itemName + " " + columnClass + " " + columnType);

         // =================================
         // Labels
         // =================================

         JLabel currentLabel = new JLabel(itemName);
         currentLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
         
         buildConstraints(constraints, x, y, 1, 1, 15, 100);
         constraints.fill = GridBagConstraints.HORIZONTAL;
         constraints.anchor = GridBagConstraints.WEST;
         gridbag.setConstraints(currentLabel, constraints);
         formPanel.add(currentLabel);

         // =====================================
         // Blob/Bytea/Binary/CLOB/Text/Array
         // Buttons & TextFields
         // =====================================

         if ((columnClass.indexOf("String") == -1 &&
              columnType.indexOf("BLOB") != -1) ||
             (columnType.indexOf("BYTEA") != -1) ||
             (columnType.indexOf("BINARY") != -1) ||
             (columnType.indexOf("RAW") != -1) ||
             (columnType.indexOf("CLOB") != -1))
         {
            currentField = new JButton();
            ((JButton) currentField).addActionListener(this);
         }

         else if ((columnClass.indexOf("String") != -1 &&
                  !columnType.equals("CHAR") &&
                  (fieldSizeHashMap.get(itemName)).intValue() > 255) ||
                   (columnClass.indexOf("String") != -1 && columnType.equals("LONG")))
         {
            currentField = new JButton();
            ((JButton) currentField).addActionListener(this);
         }
         else if ((columnClass.indexOf("Object") != -1 ||
                   columnClass.indexOf("Array") != -1) &&
                  columnType.indexOf("_") != -1)
         {
            currentField = new JButton();
            ((JButton) currentField).addActionListener(this);
         }
         else
         {
            currentField = new JTextField(formPanel.getWidth()/4);
            // ((JTextField) currentField).setEditable(false);
         }
         
         buildConstraints(constraints, x + 1, y, 1, 1, 35, 100);
         constraints.fill = GridBagConstraints.HORIZONTAL;
         constraints.anchor = GridBagConstraints.WEST;
         gridbag.setConstraints((Component)currentField, constraints);
         formPanel.add((JComponent) currentField);
         
         fieldHashMap.put(itemName, (JComponent) currentField);

         // Prepping for next position in the panel.
         if (y > (tableColumnNames.size() / 2) - 1)
         {
            x = 2;
            y = 0;
         }
         else
            y++;
      }

      // Setting the preferredSize so that a scrollpane can be
      // applied to the panel.
      
      formPanel.setPreferredSize(new Dimension(725, (tableColumnNames.size() / 2) * 32));

      JScrollPane formScrollPane = new JScrollPane(formPanel);
      formScrollPane.getVerticalScrollBar().setUnitIncrement(28);
      add(formScrollPane, BorderLayout.CENTER);

      // Creating Action Buttons.

      JPanel actionButtonPanel = new JPanel();

      previousViewButton.addKeyListener(this);
      actionButtonPanel.add(previousViewButton);

      closeViewButton.addKeyListener(this);
      actionButtonPanel.add(closeViewButton);

      nextViewButton.addKeyListener(this);
      actionButtonPanel.add(nextViewButton);

      add(actionButtonPanel, BorderLayout.SOUTH);
   }

   //==============================================================
   // ActionEvent Listener method for detecting the inputs from
   // the panel and directing to the appropriate routine.
   //==============================================================

   public void actionPerformed(ActionEvent evt)
   {
      Object panelSource = evt.getSource();

      // Detecting the selection of one of the binary type
      // or text fields buttons. This allows the saving of
      // the data to a file for viewing or viewing directly
      // in the case of a text field.

      if (panelSource instanceof JButton)
      {
         if (blobBytesHashMap.get((JButton) panelSource) != null)
         {
            // Save Binary/Clob Directly.
            if (((JButton) evt.getSource()).getText().indexOf("BLOB") != -1 ||
                ((JButton) evt.getSource()).getText().indexOf("BYTEA") != -1 ||
                ((JButton) evt.getSource()).getText().indexOf("BINARY") != -1 ||
                ((JButton) evt.getSource()).getText().indexOf("RAW") != -1)
               saveBlobTextField(panelSource);

            // View Text/Array and Allow Saving if Desired.
            else
            {
               JEditorPane editorPane;
               InputDialog textDialog;
               String textContent;
               JMenuBar editorMenuBar;
               
               // Create an EditorPane to view/edit content.
               
               textContent = ((String) blobBytesHashMap.get((JButton) panelSource));
               editorPane = new JEditorPane("text/plain", textContent);
               editorPane.addMouseListener(MyJSQLView.getPopupMenuListener());

               textDialog = MyJSQLView_Utils.createTextDialog(false, editorPane);

               editorMenuBar = MyJSQLView_Utils.createEditMenu(false);
               textDialog.setJMenuBar(editorMenuBar);
               textDialog.pack();
               textDialog.center();
               textDialog.setVisible(true);
               
               // Check to see if save data is desired..
               if (textDialog.isActionResult())
                  saveBlobTextField(panelSource);

               textDialog.dispose();
            }
         }
      }
   }

   //==============================================================
   // KeyEvent Listener method for detecting key pressed events of
   // left and right arrows to move to previous next entries in
   // the table.
   //==============================================================

   public void keyPressed(KeyEvent evt)
   {
      int keyCode = evt.getKeyCode();

      // Previous/Next entry views
      if (keyCode == KeyEvent.VK_LEFT)
         previousViewButton.doClick();
      else if (keyCode == KeyEvent.VK_RIGHT)
         nextViewButton.doClick();
   }

   //==============================================================
   // KeyEvent Listener method for detected key released events
   // to full fill KeyListener Interface requirements.
   //==============================================================

   public void keyReleased(KeyEvent evt)
   {
      // Do Nothing
   }

   //==============================================================
   // KeyEvent Listener method for detecting key pressed event,
   // Enter, to be used with the close action.
   //==============================================================

   public void keyTyped(KeyEvent evt)
   {
      // Derived from the searchTextField.
      char keyChar = evt.getKeyChar();

      // Fire the search button as required.
      if (keyChar == KeyEvent.VK_ENTER)
         closeViewButton.doClick();
   }
   
   //================================================================
   // Class Method for helping the parameters in gridbag.
   //================================================================

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
   // Class method to save data from a binary or text field to a
   // selected output file.
   //==============================================================

   protected void saveBlobTextField(Object panelSource)
   {
      // Class Method Instance
      String fileName;
      byte[] buf;
      JFileChooser exportDataChooser;
      int resultsOfFileChooser;

      // Setting up a file separator instance.
      String fileSeparator = MyJSQLView_Utils.getFileSeparator();

      // Don't Convert the Binary Data to a String
      if (((JButton) panelSource).getText().indexOf("BLOB") != -1 ||
          ((JButton) panelSource).getText().indexOf("BYTEA") != -1 ||
          ((JButton) panelSource).getText().indexOf("BINARY") != -1 ||
          ((JButton) panelSource).getText().indexOf("RAW") != -1)

         buf = (byte[]) blobBytesHashMap.get((JButton) panelSource);
      else
         buf = ((String) blobBytesHashMap.get((JButton) panelSource)).getBytes();

      // Choosing the file to export data to.
      exportDataChooser = new JFileChooser();
      resultsOfFileChooser = MyJSQLView_Utils.processFileChooserSelection(null, exportDataChooser);

      // Looks like might be good file name so lets check
      // and then output the blob/text data
      if (resultsOfFileChooser == JFileChooser.APPROVE_OPTION)
      {
         fileName = exportDataChooser.getSelectedFile().getName();
         fileName = exportDataChooser.getCurrentDirectory() + fileSeparator + fileName;
         // System.out.println(fileName);

         if (!fileName.equals(""))
         {
            // Creating the buffered data of the binary/text
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
               String resourceMessage, resourceAlert;
               
               resourceAlert = resourceBundle.getResource("TableViewForm.dialogtitle.Alert");
               if (resourceAlert.equals(""))
                  resourceAlert = "Alert";
               resourceMessage = resourceBundle.getResource("TableViewForm.dialogmessage.ErrorWritingDataFile");
               if (resourceMessage.equals(""))
                  resourceMessage = "Error Writing Data File";
               
               JOptionPane.showMessageDialog(null, resourceMessage + " " + fileName, resourceAlert,
                  JOptionPane.ERROR_MESSAGE);
            }
         }
         else
         {
            // System.out.println("File NOT Found");
         }
      }
      else
      {
         // System.out.println("File Selection Canceled");
      }
   }

   //==============================================================
   // Class method to clear the blobBytesHashMap. Takes place
   // anytime an edit is made on a table entry.
   //==============================================================
   
   protected void clearBlobBytesHashMap()
   {
      blobBytesHashMap = new HashMap <JButton, Object>();
   }
   
   //==============================================================
   // Class method to place text content into a selected
   // TextField/JButton.
   //==============================================================

   protected void setFormField(Object itemName, Object content)
   {
      // Method Instances.
      String columnClass, columnType;

      columnClass = fieldClassHashMap.get(itemName);
      columnType = fieldTypeHashMap.get(itemName);

      // Binary Button, Note all data with buttons processed
      // the same just grouping for clarity.
      if ((columnClass.indexOf("String") == -1 && columnType.indexOf("BLOB") != -1) ||
          (columnClass.indexOf("BLOB") != -1 && columnType.indexOf("BLOB") != -1) ||
          (columnType.indexOf("BYTEA") != -1) || (columnType.indexOf("BINARY") != -1) ||
          (columnType.indexOf("RAW") != -1) || (columnType.indexOf("CLOB") != -1))

         ((JButton) fieldHashMap.get(itemName)).setText((String) content);

      // Text Button, TEXT, MEDIUMTEXT, & LONGTEXT
      else if ((columnClass.indexOf("String") != -1 && !columnType.equals("CHAR") &&
                (fieldSizeHashMap.get(itemName)).intValue() > 255) ||
               (columnClass.indexOf("String") != -1 && columnType.equals("LONG")))
         ((JButton) fieldHashMap.get(itemName)).setText((String) content);

      // Array Button
      else if ((columnClass.indexOf("Object") != -1 || columnClass.indexOf("Array") != -1)
               && columnType.indexOf("_") != -1)
         ((JButton) fieldHashMap.get(itemName)).setText((String) content);

      // Standard TextField
      else
      {
         // Arbitrarly set string to no more than 600 characters, just trying
         // to limit textfield content, and put caret back to beginning.

         if (((String) content).length() > 600)
            ((JTextField) fieldHashMap.get(itemName)).setText(((String) content).substring(0, 599));
         else
            ((JTextField) fieldHashMap.get(itemName)).setText((String) content);
         ((JTextField) fieldHashMap.get(itemName)).setCaretPosition(0);
      }
   }

   //==============================================================
   // Class method to place content into a selected JButton
   // hashmap as bytes.
   //==============================================================

   protected void setFormFieldBlob(Object itemName, byte[] content)
   {
      blobBytesHashMap.put((JButton) fieldHashMap.get(itemName), content);
   }

   //==============================================================
   // Class method to place content into a selected JButton
   // hashmap as a string.
   //==============================================================

   protected void setFormFieldText(Object itemName, String content)
   {
      blobBytesHashMap.put((JButton) fieldHashMap.get(itemName), content);
   }

   //==============================================================
   // Class method to place the focus to the close view button.
   //==============================================================

   protected void setFocus()
   {
      closeViewButton.requestFocus();
   }
}