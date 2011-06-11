//=================================================================
//              MyJSQLView TableEntryForm
//=================================================================
//
//    This class is used to provide a form panel to add, or
// edit a table entry in a SQL database table.
//
//                  << TableEntryForm.java >>
//
//=================================================================
// Copyright (C) 2005-2011 Dana M. Proctor
// Version 8.81 06/10/2011
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
// Version 1.0 12/27/2005 Original MyJSQLView TableForm Class.
//         1.1 01/18/2006 sqlTable Instance Derived from MyJSQLView_Access.getTable();
//         1.2 01/22/2006 sqlTable Passed as Argument in Constructor.
//         1.3 02/02/2006 Class Method createSetComboBoxField().
//         1.4 02/07/2006 Removed Class Instance fields.
//         1.5 02/07/2006 Set PrimaryKey field to Not Editable.
//         1.6 02/08/2006 Corrections to Field Positioning in Form.
//         1.7 02/08/2006 Transaction-Based Table Locking in Class Methods
//                        addTableEntry() & updateTableEntry().
//         1.8 02/09/2006 Form Fields in Constructor Formatted Based on Field Type.
//         1.9 02/10/2006 Class Method addTableEntry Initial Numeric Type Validation
//                        & Setting.
//         2.0 02/11/2006 Replaced Class Methods addTableEntry and updateTableEntry
//                        With addUpdateTableEntry().
//         2.1 02/11/2006 Class Method addUpdateTableEntry() Change BigDecimal
//                        Numeric Conversion.
//         2.2 02/12/2006 Included Class Instance columnClassHashMap.
//         2.3 02/13/2006 Finished Class Method addUpdateTableEntry() Date Input
//                        Table Fields.
//         2.4 02/13/2006 Date Formating, Preferences Selection.
//         2.5 02/14/2006 Date Formating, Preferences Selection Removed. Defaulting
//                        to MM-DD-YYYY Format.
//         2.6 02/14/2006 Removed JTextField Filling in Constructor Besides Those
//                        Special Fields.
//         2.7 02/15/2006 Class Method addUpdateTable() Chop Input String for Date,
//                        Year Fields.
//         2.8 02/16/2006 Implemented Enum & Set JComboBox Fields.
//         2.9 05/29/2006 Removed Unused Local Instance name.
//         3.0 07/30/2006 Main Class Name Change to MyJSQLView.
//         3.1 08/14/2006 Correction for Creating formField Element JButton for Blob
//                        Types.
//         3.2 08/14/2006 Class Methods Renamed setFormField() and getFormField().
//         3.3 08/14/2006 Added Class Method setFormFieldBytes().
//         3.4 08/16/2006 Implemented Blob Implemetation to Class Method addUpdateTableEntry().
//         3.5 08/16/2006 Added Class Method getFormFieldBlob().
//         3.6 08/19/2006 Class Method formatJavaDateString().
//         3.7 09/28/2006 ColumnClass Check for Blob of Not Having String in Class
//                        Methods addUpdateTableEntry() & setFormField().
//         3.8 10/22/2006 Commented Out Debugging System.out.
//         3.9 11/19/2006 Class Method addUpdateTableEntry() TIMESTAMP Format Change.
//         4.0 11/23/2006 Renamed to TableEntryForm.
//         4.1 11/25/2006 Class Method addUpdateTable() TIMESTAMP Edit Allowed. Added
//                        Checks and Additional Formatting.
//         4.2 11/27/2006 Class Method addUpdateTableEntry() ParseExeception e Converted
//                        to String Before Throwing new IllegalArgumentExeception.
//         4.3 11/29/2006 Constructor Argument autoIncrement Added. Also Taking Care
//                        of primaryKeys That May Not be  Auto-Increment in Class
//                        Method addUpdateTableEntry(). Short Edit Primary Keys.
//         4.4 05/12/2007 Modifications to Properly Implement Table Key(s) Behavior.
//         4.5 05/13/2007 Continueing Key(s) Behavior Modifications, Auto-Increment.
//         4.6 05/14/2007 Commented Out System.out for a SQL Statement String.
//         4.7 05/15/2007 Class Instances limitCheckBox & limitTextField for Managing
//                        Entry Limits for Edits.
//         4.8 05/16/2007 Right Justified Auto-Increments Column.
//         4.9 05/17/2007 Class addUpdateTableEntry(), Blob Key Implementation for Edit.
//                        Comments.
//         5.0 05/19/2007 Class Instances blobRemoveCheckBoxesHashMap, removeUpIcon, &
//                        removeDownIcon for Checkbox(s) to Set Blob Fields to NULL on
//                        Edit if Desired.
//         5.1 05/23/2007 Creation of Mechanism to Have Date & DateTime Fields to be set
//                        by the DateFieldCalendar Class.
//         5.2 05/24/2007 Date and DateTime String Check for Valid Input in Class Method
//                        addUpdateTableEntry().
//         5.3 05/25/2007 Class Instances setButtonHashMap, setFieldsHashMap and Class
//                        Method setSetFields(). Implementation of Proper Set Selection
//                        for Fields.
//         5.4 06/01/2007 Implmented the Add/Edit of TEXT, MEDIUMTEXT, & LONGTEXT Via
//                        Either an Opening of a File or Entering in an EditorPane. Class
//                        Methods openBlobTextField() & get/setFormFieldText(). TableViewForm
//                        Argument.
//         5.5 06/05/2007 Class Method addUpdateTableEntry Years Set as Strin Instead of
//                        Date in SQL Statement.
//         5.6 06/06/2007 Modified Detection of TEXT, MEDIUMTEXT, and LONGTEXT for Newer
//                        Version of MySQL 5.0 in Class Methods Constructor, & setFormField().
//         5.7 06/14/2007 Made dateCalendar & setDialog Class Instances and Disposed In
//                        New Inner Class tableEntryFormFrameListener on Exit. Also
//                        Disposed Same Instances As Needed on Cancel/Update.
//         5.8 06/15/2007 Argument to DateCalendar Changed to columnType Instance.
//         5.9 06/16/2007 Class Instance dateCalendar.setResizable(false).
//         6.0 06/18/2007 Modified Class Method addUpdateTableEntry() On Throwing A
//                        NumberFormatException With Integer Fields to Display Complete
//                        columnType String.
//         6.1 08/24/2007 Implemented a ScrollPane for Instance formPanel.
//         6.2 09/05/2007 Added Class Instance functionButtonHashMap & functionIcon.
//                        Included a Function Button in Interface for Each Field & Action
//                        Detection in Class Method actionPerformed().
//         6.3 09/06/2007 Added Class Instance functionsHashMap and Class Method selectFunction().
//                        Basic Action of Selecting Function for a Field.
//         6.4 09/07/2007 Implemented Function Operations in Class Method addUpdateTableEntry().
//                        Added Class Method selectFunctionOperations() &  createFunctionSQLStatement().
//         6.5 09/07/2007 Code Cleanup.
//         6.6 09/08/2007 Class Method createFunctionSQLStatement() Modification to the Way
//                        Blob Data is Handled with Functions. This Really Needs to Be Looked
//                        At Further.
//         6.7 09/09/2007 Removed Unused Class Instance calendarButton, columnSetHashMap,
//                        & id Either Because They Were Unused Or Only Used Locally in
//                        Constructor.
//         6.8 09/19/2007 Used System.getProperty("file.separator") for All File System
//                        Resources Accesses Through Instance fileSeparator.
//         6.9 09/26/2007 Added PopupMenu and MenuBar for Cut, Copy, Paste, & Select All
//                        for Text Entry Editor in Class Method actionPerformed(). Added
//                        Class Method createEditMenu().
//         7.0 09/27/2007 Class Method createFunctionSQLStatement() Blob Data Added toString
//                        for Byte[]. Apparently JRE1.4x Something Does Not Support
//                        Arrays.toString(). Created Message Alert for Function Operations
//                        on Blob Data in Class Method actionPerformed().
//         7.1 10/14/2007 Changed Text in Connections to "TableEntryForm". Also Check to
//                        Remove LIMIT from PostgreSQL Database Tables in Class Method
//                        addUpdateTableEntry().
//         7.2 10/21/2007 Cleaned Up Javadoc Comments.
//         7.3 10/29/2007 Class Method addUpdateTableEntry() Set Content for Properly
//                        Processing TIMETZ, TIMESTAMPTZ, BYTEA, & BIT Fields. Class
//                        Method setFormField Button for BYTEA Fields. Constructor Form
//                        Filling for TIMESTAMPTZ BYTEA Button.
//         7.4 10/30/2007 Properly Implementation of Upon PostgreSQL Fields in Updates.
//                        Change in Display of Button Text for BYTEA.
//         7.5 11/07/2007 Maybe Some Minor Format/Comment Changes. Class Method addUpdateTableEntry
//                        Corrected Text Field setString() When currentRemoveBlobCheckBox.isSelected().
//         7.6 11/12/2007 Changed Class Instance String autoIncrement to HashMap autoIncrementHashMap.
//                        Constructor Changed Appropriately and Some Formatting. Also
//                        Changed the Way AutoIncrement, Sequenced Fields are Handled in
//                        addUpdateTableEntry() Class Method.
//         7.7 11/12/2007 Reviewed for Proper Boolean Enumeration Implementation Through the
//                        columnEnumHashMap. Changes in Class Method addUpdateTableEntry()
//                        to Correct Handling of Bit Fields in MySQL.
//         7.8 11/16/2007 Reviewed Code & Cleaned Out. Implemented Empty & NULL Field
//                        Entries in Class Method addUpdateTableEntry(). Cleared Function
//                        Assignment by Cancel Operation in Class Method selectFunctionOperation().
//         7.9 11/19/2007 Placed Conditionals to Handle PostgreSQL Geometric Types in
//                        Class Method addUpdateTableEntry.
//        8.10 11/30/2007 Class Method addUpdateTableEntry() on Add Check to Insure No
//                        Field Data Entry sqlStatementString is Properly Constructed
//                        to Not Throw String Index Error.
//        8.11 12/03/2007 Placed Conditionals to Handle PostgreSQL Interval Types in
//                        Class Method addUpdateTableEntry.
//        8.12 12/08/2007 Added Specific Functions File Based On Database Connection in
//                        Class Method selectFunctionOperator(). Conditional Check for
//                        NULL Field Entry in Class Method createFunctionSQLStatement().
//        8.13 12/09/2007 Class Method setFormField(Object, Object) Normal Text Field
//                        Set Caret Position to Zero After Fill.
//        8.14 12/09/2007 Corrected Conditional Check for Class Instance sqlValuesString
//                        Length in Class Method addUpdateTableEntry().
//        8.15 12/10/2007 Commented a System.out.println() in Class Method addUpdateTableEtnry().
//        8.16 12/12/2007 Header Update.
//        8.17 12/22/2007 Added/Implemented identiferQuoteString.
//        8.18 01/12/2008 Comment Changes.
//        8.19 01/20/2008 Handling of HSQL BINARY Data in Constructor & Class Methods
//                        actionPerformed(), openBlobTextField(), addUpdateTableEntry(),
//                        createFunctionSQLStatementString() & setFormField().
//        8.20 02/01/2008 Added HSQL Function File Name Initialization in Class Method
//                        selectFunctionOperator(). Removed Instance sampleFunctionsFileName
//                        in Same Method.
//        8.21 02/06/2008 Class Method addUpdateTableEntry() mysqlStatement Instance
//                        Changed to sqlStatement and sqlStatement Changed to prepared_sqlStatement.
//        8.22 02/21/2008 Added Basic Support for Arrays, Add/Edit, In Class Method
//                        addUpdateTableEnry().
//        8.23 02/25/2008 Added Array Support in Class Method addUpdateTableEntry().
//        8.24 03/04/2008 Clarified Conditional With Additional Operands for Array
//                        Detection in Class Method addUpdateTableEntry().
//        8.25 03/05/2008 Implemented Array Data to be Entered Just Like Text Fields.
//                        Changes in Constructor, actionPerformed(), addUpdateTableEntry(),
//                        & setFormField(Object,Object).
//        8.26 03/12/2008 Escaped Single Quotes in String Class Type Fields for Keys in
//                        Class Method addUpdateTableEntry().
//        8.27 03/13/2008 Correction for Induced Problem With Prior Fix for Escape
//                        Key Strings.
//        8.28 03/14/2008 Proper Proccessing of Function Operations for Functionns With
//                        No Arguments, ie. VERSION(), and Multiple Arguments. Effected
//                        Class Methods addUpdateTableEntry() and createFunctionSQLStatement().
//        8.29 03/14/2008 SQL Correction in Class Method createFunctionSQLStatement().
//        8.30 04/09/2008 Class Instance sqlTable Passed In as A Fully Qualified Table
//                        Name With Identifier Quote Built In.
//        8.31 04/10/2008 Correction in Class Method addUpdateTableEntry() For Proper
//                        Auto Increment Field Name Sequences With PostgreSQL.
//        8.32 05/11/2008 Removed SQL BEGIN Statement for Oracle Database in Class
//                        Method addUpdateTableEntry().
//        8.33 05/12/2008 Added Class Instance serialVersionUID. Moved Class Instance
//                        focusSequence to Construtor. Declared WindowListener
//                        tableEntryFormFrameListener private transient.
//        8.34 05/14/2008 Additional Comment in Class Method createFunctionSQLStatement()
//                        on Function Operation for Binary Data. This aspect is broken
//                        at this time, kinda purposely.
//        8.35 05/22/2008 Oracle BLOB Field Recognition, TYPE & CLASS BLOB. Added Proper
//                        Recognition of Oracle TIMESTAMPLTZ Fields. Oracle Timestamp
//                        on Add SYSTIMPSTAMP Substituted for NOW() Function.
//        8.36 05/24/2008 Oracle LONG Text Field Recognition & Processing. Class Methods
//                        Effected Constructor, addUpdateTableEntry(), & setFormField().
//                        Added Instances columnClass & columnType to Constructor.
//        8.37 05/25/2008 Class Method setFormField(Object,Object) Proper Conditional
//                        Check to Catch Oracle BLOB Field Types. Also Class Method 
//                        addUpdateTableEntry() isBlobType Proper Catch of Oracle BLOB
//                        Data Types.
//        8.38 05/26/2008 Implementation of Processing for Oracle RAW & LONG RAW Data
//                        Types for the Oracle Database. Class Methods Effected  Constructor,
//                        actionPerformed(), openBlobTextField(), addUpdateTableEntry(),
//                        createFunctionSQLStatement() & setFormField().
//        8.39 06/02/2008 Initial Implementation of Processing for CLOB Data Types.
//                        Class Methods Effected Constructor, actionPerformed(),
//                        openBlobTextField(), addUpdateTableEntry(),
//                        createFunctionSQLStatement() & setFormField().
//        8.40 06/03/2008 Moved Context of Applications Processing of CLOB Data Types
//                        to be the Same as BLOB & RAW. Kept Though the Data Processing
//                        As String.
//        8.41 06/04/2008 Added Support for Oracle Functions in Entry Form By Way of
//                        Selecting Function File in Class Method selectFunctionOperator().
//        8.42 06/04/2008 Class Method openBlobTextField() Modified the Way the String
//                        for TEXT & CLOB Data Read From Input File, Used a StringBuffer.
//                        Also Proper Labeling of Button With Either TEXT or CLOB as
//                        Appropriate.
//        8.43 06/16/2008 Updated Class Method addUpdateTableEntry() To Support Key Word
//                        DEFAULT As An Entry. Also Added Sequence Processing for Oracle
//                        Database in Same Method.
//        8.44 08/12/2008 Implemented Oracle BFILE Add/Edit in Class Method addUpdateTableEntry().
//        8.45 08/13/2008 Removed the Edit Capability of Oracle BFILE Types. Class Methods
//                        Effected Constructor, addUpdateTableEntry(), & setFormField().
//        8.46 08/15/2008 Class Method addUpdateTableEntry() Added Processing of Oracle
//                        TIMESTAMPLTZ Field Type. Added ALTER SESSION TIMEZONE For Same
//                        & Some String Processing to Accomodate. Eventhough Session Set
//                        Properly Oracle 10g Insists on Modifing and Screwing With the
//                        Hours on Edit. Day Light Savings?
//        8.47 08/20/2008 Added Some Comments and System.out in Method addUpdateTableEntry()
//                        for TimeStamp Fields.
//        8.48 08/24/2008 Minor Format Changes.
//        8.49 08/31/2008 Updated Class Method addUpdateTableEntry() to Process a Date
//                        Key Field Through New Class Method formatDatabaseDateString().
//                        Not Complete. Needs MySQL, HSQL, & PostgreSQL.
//        8.50 10/10/2008 Added Method Instance currentTableTabPanel to addUpdateTableEntry().
//                        MyJSQLView.getSelectedTab() With DBTablesPanel.getSelectedTableTabPanel().
//        8.51 10/21/2008 MyJSQLView Project Common Source Code Formatting.
//        8.52 10/25/2008 Added Class Instance iconsDirectory. Changed the File Name
//                        for Instance functionsIcon and That Instance to functionsPaletteIcon
//                        in class Method selectFunctionOperator().
//        8.53 11/28/2008 Class Method addUpdateTableEntry() Key Processing Cleaned/Reduced
//                        Code. Modified Class Method formatDatabaseDateString() to
//                        Forward Other Databases to Method formatJavaDateString().
//        8.54 12/02/2008 Class Method actionPerformed() Added a Inner Class Thread to
//                        Process the Add/Update Action. Also Set Form Invisble While
//                        Processing.
//        8.55 12/03/2008 Class Method actionPerformed() Provided a String Name for the
//                        Thread processEntryThread.
//        8.56 05/27/2009 Header Format Changes/Update.
//        8.57 09/02/2009 Added Class Instances selectedRow, & selectedTableTabPanel. Added
//                        Arguments selectedRow & selectedTableTabPanel to Constructor.
//                        Class Method addUpdateTableEntry() Removed Instance currentTableTabPanel
//                        & Replaced With selectedTableTabPanel.
//        8.58 09/25/2009 Added the sqlTable to super() for Constructor.
//        8.59 09/25/2009 Class Method addUpdateTableEntry() Check for db_Connection Being NULL.
//        8.60 10/25/2009 Removed Constructor Instance fileSeparator. Obtained iconsDirectory
//                        From MyJSQLView_Utils Class.
//        8.61 10/25/2009 Added fileSeparator to iconsDirectory.
//        8.62 10/28/2009 Removed Class Method formatJavaDateString(). Moved to MyJSQLView_Utils
//                        Class.
//        8.63 11/06/2009 Class Method addUpdateTableEntry() Date/Time Processing in Final Prepare
//                        Statement Added Instance dateTimeFormString Trimmed. Additional Checks
//                        in The Processing of the These Types, Always be Wary of substring().
//        8.64 02/09/2010 Class Method createFunctionSQLStatement() Changed sqlStatementString
//                        From String to StringBuffer.
//        8.65 02/18/2010 Changed Package to Reflect Dandy Made Productions Code.
//        8.66 02/22/2010 Class Method selectFunctionOperator() Used MyJSQLView_Utils Class to
//                        Obtain MyJSQLView Users Home Directory and FileSeparator.
//        8.67 03/03/2010 Added Class Instance resourceBundle to Facilitate Internationalization.
//                        Added Constructor Instance resource and Same to createEditMenu(). Added
//                        Instances resource & message to Methods actionPerformed(), openBlobTextField(),
//                        addUpdateTableEntry(), and selectFunctionOperator().
//        8.68 03/26/2010 Class Method addUpdateTableEntry() Changed Instance sqlStatementString to
//                        Type StringBuffer.
//        8.69 04/06/2010 Removed Class Method formatDatabaseDateString() and Moved That Processing,
//                        Oracle Only, to the Place it is Being Used for Key Processing in the
//                        addUpdateTableEntry() Method. Consolidated There For Later Date Processing
//                        Modification. User Controls the Date Format Displayed.
//        8.70 05/19/2010 Parameterized All HashMap and Vector Instances in Order to Bring Code
//                        Into Compliance With Java 5.0 API. Reviewed All Assignments to These
//                        Objects to Insure Proper Configuration.
//        8.71 05/19/2010 Parameterized columnNamesIterator in Constructor, columnNamesIterator &
//                        keyIterator in addUpdateTableEntry(), and contentsIterator in setSetFields(),
//                        & setComboBoxField().
//        8.72 05/20/2010 Class Method createFunctionSQLStatement() Converted byte[] Array to String
//                        via Arrays.toString(getFormFieldBlob(columnName).
//        8.73 07/21/2010 Updated Method addUpdateTableEntry() Removed BEGIN Statement SQL Query
//                        Execution for SQLite Database.
//        8.74 07/28/2010 Class Method selectFunctionOperator() Added SQLite Function Selection
//                        File Loading.
//        8.75 01/11/2010 Class Method addUpdateTableEntry() Change to Convert Date/DateTime/Timestamp
//                        Entries According to the GeneralPreferences.getViewDateFormat(). Conversion
//                        via MyJSQLView_Utils.convertViewDateString_To_DBDateString().
//        8.76 01/15/2011 Class Method addUpdateTableEntry() Cast Object Returned by MyJSQLView_Access.
//                        getConnection() to Connection.
//        8.77 01/26/2011 Changes to Class Method addUpdateTableEntry() to Use Newly Redefined
//                        ConnectionManager to Display SQL Errors. Also identifierQuoteString 
//                        Collected From ConnectionManager. Added Class Instance subProtocol.
//        8.78 04/09/2011 Moved Class Method createEditMenu() to MyJSQLView_Utils Class.
//        8.79 04/09/2011 Class Method actionPerformed() Standardized Text Entry/File Opening via
//                        the Help of New Methods MyJSQLView_Utils.createTextDialog() &
//                        MyJSQLView_Utils.createEditMenu(true);
//        8.80 04/10/2011 Minor Cleanup.
//        8.81 06/10/2011 Implmented Support for MS Access Database. Changes Localized to the Method
//                        addUpdateTableEntry(). Also Fix in Same Method for Handling NULL Content
//                        Properly, With IS NULL for Keys.
//        
//-----------------------------------------------------------------
//                 danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview;

import java.awt.Component;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.*;
import java.math.BigDecimal;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.*;

/**
 *    The TableEntryForm class is used to provide a form panel to add, or
 * edit a table entry in a SQL database table.
 * 
 * @author Dana M. Proctor
 * @version 8.81 06/10/2011
 */

class TableEntryForm extends JFrame implements ActionListener
{
   // Class Instances.
   private static final long serialVersionUID = -911009413706734602L;

   private int selectedRow;
   private TableTabPanel selectedTableTabPanel;
   private HashMap<String, JComponent> fieldHashMap;
   private HashMap<String, String> columnNamesHashMap;
   private HashMap<String, String> columnClassHashMap;
   private HashMap<String, String> columnTypeHashMap;
   private HashMap<String, Integer> columnSizeHashMap;
   private HashMap<String, String> columnEnumHashMap;
   private HashMap<JButton, Object> blobBytesHashMap;
   private HashMap<String, JCheckBox> blobRemoveCheckBoxesHashMap;
   private HashMap<JButton, String> calendarButtonHashMap;
   private HashMap<JButton, String> setButtonHashMap;
   private HashMap<JButton, String> functionButtonHashMap;
   private HashMap<Object, Vector<String>> setFieldsHashMap;
   private HashMap<Object, String> functionsHashMap;
   private MyJSQLView_ResourceBundle resourceBundle;

   private String sqlTable;
   private String subProtocol;
   private String iconsDirectory;
   private String identifierQuoteString;
   private String resourceInvalidInput, resourceType, resourceAlert;
   private Vector<String> primaryKeys;
   private HashMap<String, String> autoIncrementHashMap;
   private Vector<String> formFields;
   private TableViewForm tableViewForm;
   private Vector<Component> componentFocusSequence;;
   private JCheckBox limitCheckBox;
   private JTextField limitTextField;
   private boolean addItem;
   private boolean validEntry;
   private JButton cancelButton, updateButton;
   protected JButton disposeButton;

   private DateFieldCalendar dateCalendar;
   private SetListDialog setDialog;

   //==============================================================
   // TableEntryForm Constructor
   //==============================================================

   protected TableEntryForm(String title, boolean addItem, String sqlTable, int selectedRow,
                            TableTabPanel selectedTableTabPanel, Vector<String> primaryKeys,
                            HashMap<String, String> autoIncrementHashMap, Object id,
                            Vector<String> formFields, TableViewForm tableViewForm,
                            HashMap<String, String> columnNamesHashMap,
                            HashMap<String, String> columnClassHashMap,
                            HashMap<String, String> columnTypeHashMap,
                            HashMap<String, Integer> columnSizeHashMap,
                            HashMap<String, String> columnEnumHashMap,
                            HashMap<String, String> columnSetHashMap)
   {
      this.sqlTable = sqlTable;
      this.selectedRow = selectedRow;
      this.selectedTableTabPanel = selectedTableTabPanel;
      this.primaryKeys = primaryKeys;
      this.autoIncrementHashMap = autoIncrementHashMap;
      this.formFields = formFields;
      this.tableViewForm = tableViewForm;
      this.columnNamesHashMap = columnNamesHashMap;
      this.columnClassHashMap = columnClassHashMap;
      this.columnTypeHashMap = columnTypeHashMap;
      this.columnSizeHashMap = columnSizeHashMap;
      this.columnEnumHashMap = columnEnumHashMap;

      this.addItem = addItem;

      // Constructor Instances

      MyJSQLView_FocusTraversalPolicy focusSequence;
      Iterator<String> columnNamesIterator;
      ImageIcon removeUpIcon, removeDownIcon;
      ImageIcon functionIcon, calendarIcon, setIcon;
      validEntry = false;
      String resource;
      String columnName, columnClass, columnType;
      Object currentField;

      fieldHashMap = new HashMap <String, JComponent>();
      blobBytesHashMap = new HashMap <JButton, Object>();
      blobRemoveCheckBoxesHashMap = new HashMap <String, JCheckBox>();
      calendarButtonHashMap = new HashMap <JButton, String>();
      setButtonHashMap = new HashMap <JButton, String>();
      functionButtonHashMap = new HashMap <JButton, String>();
      setFieldsHashMap = new HashMap <Object, Vector<String>>();
      functionsHashMap = new HashMap <Object, String>();
      componentFocusSequence = new Vector <Component>();
      resourceBundle = MyJSQLView.getLocaleResourceBundle();

      // Setting up a icons directory identifier quote character,
      // & other instances.
      
      subProtocol = ConnectionManager.getConnectionProperties().getProperty(
         ConnectionProperties.SUBPROTOCOL);
      iconsDirectory = MyJSQLView_Utils.getIconsDirectory() + MyJSQLView_Utils.getFileSeparator();
      identifierQuoteString = ConnectionManager.getIdentifierQuoteString();
      
      resource = resourceBundle.getResource("TableEntryForm.dialogtitle.Alert");
      if (resource.equals(""))
         resourceAlert = "Alert";
      else
         resourceAlert = resource;
      
      resource = resourceBundle.getResource("TableEntryForm.dialogmessage.InvalidInput");
      if (resource.equals(""))
         resourceInvalidInput = "Invalid Input for Field";
      else
         resourceInvalidInput = resource;
      
      resource = resourceBundle.getResource("TableEntryForm.dialogmessage.Type");
      if (resource.equals(""))
         resourceType = "Type";
      else
         resourceType = resource;
      
      // Setting up the frame's title & main panel.

      if (title.indexOf("Add") != -1)
         resource = resourceBundle.getResource("TableEntryForm.message.TitleAdd");
      else if (title.indexOf("Edit") != -1)
         resource = resourceBundle.getResource("TableEntryForm.message.TitleEdit");
      else
         resource = "";

      if (resource.equals(""))
         setTitle(title + sqlTable);
      else
         setTitle(resource + ": " + sqlTable);

      JPanel mainPanel = new JPanel(new BorderLayout());
      mainPanel.setBorder(BorderFactory.createEtchedBorder());

      // Setting up and Creating the panel.

      JPanel formPanel = new JPanel();
      formPanel.setLayout(null);
      formPanel.setBorder(BorderFactory.createRaisedBevelBorder());
      formPanel.addMouseListener(MyJSQLView.getPopupMenuListener());

      removeUpIcon = new ImageIcon(iconsDirectory + "removeNotIcon.png");
      removeDownIcon = new ImageIcon(iconsDirectory + "removeIcon.png");
      functionIcon = new ImageIcon(iconsDirectory + "functionIcon.png");
      calendarIcon = new ImageIcon(iconsDirectory + "calendarIcon.png");
      setIcon = new ImageIcon(iconsDirectory + "setIcon.png");

      // Start Position for components
      int x = 15;
      int y = 20;

      columnNamesIterator = formFields.iterator();

      while (columnNamesIterator.hasNext())
      {
         columnName = (String) columnNamesIterator.next();
         columnClass = (String) columnClassHashMap.get(columnName);
         columnType = (String) columnTypeHashMap.get(columnName);
         // System.out.println(columnNamesHashMap.get(columnName) + " " +
         // columnClassHashMap.get(columnName) + " " +
         // columnTypeHashMap.get(columnName) + " " +
         // columnSizeHashMap.get(columnName));

         // BFile types not supported.
         if (columnType.equals("BFILE") && !addItem)
            continue;

         // =================================
         // Labels
         // =================================

         JLabel currentLabel = new JLabel(columnName);
         currentLabel.setBounds(x, y, 120, 20);
         formPanel.add(currentLabel);

         // =================================
         // Function Buttons
         // =================================

         currentField = new JButton(functionIcon);
         ((JButton) currentField).setBounds(x + 120, y, 20, 20);
         ((JButton) currentField).addActionListener(this);
         functionButtonHashMap.put((JButton) currentField, columnName);
         formPanel.add((JButton) currentField);

         // ===============================
         // TextFields & ComboBoxFields
         // ===============================

         // Auto Increment
         if (autoIncrementHashMap.containsValue(columnName))
         {
            currentField = new JTextField();
            ((JTextField) currentField).setHorizontalAlignment(JTextField.RIGHT);
            ((JTextField) currentField).setBounds(x + 145, y, 220, 20);
            componentFocusSequence.add((JTextField) currentField);
            formPanel.add((JTextField) currentField);
         }

         // ENUM Type Fields
         else if (columnEnumHashMap.containsKey(columnName))
         {
            currentField = new JComboBox();
            ((JComboBox) currentField).setBounds(x + 145, y, 220, 20);
            componentFocusSequence.add((JComboBox) currentField);
            formPanel.add((JComboBox) currentField);
         }

         // SET Type Fields
         else if (columnSetHashMap.containsKey(columnName))
         {
            currentField = new JButton(setIcon);
            ((JButton) currentField).setBounds(x + 345, y, 20, 20);
            ((JButton) currentField).addActionListener(this);
            setButtonHashMap.put((JButton) currentField, columnName);
            formPanel.add((JButton) currentField);

            currentField = new JTextField();
            ((JTextField) currentField).setText("");
            ((JTextField) currentField).setBounds(x + 145, y, 195, 20);
            componentFocusSequence.add((JTextField) currentField);
            formPanel.add((JTextField) currentField);
         }

         // TINYINT, SMALLINT, MEDIUMINT, INT, & BIGINT Type Fields
         else if (columnClass.indexOf("Byte") != -1 || columnClass.indexOf("Short") != -1
                  || columnClass.indexOf("Integer") != -1 || columnClass.indexOf("Long") != -1)
         {
            currentField = new JTextField();
            ((JTextField) currentField).setHorizontalAlignment(JTextField.RIGHT);
            ((JTextField) currentField).setText("000");
            ((JTextField) currentField).setBounds(x + 145, y, 220, 20);
            componentFocusSequence.add((JTextField) currentField);
            formPanel.add((JTextField) currentField);
         }

         // FLOAT, DOUBLE, and DECIMAL Type Fields
         else if (columnClass.indexOf("Float") != -1 || columnClass.indexOf("Double") != -1
                  || columnClass.indexOf("BigDecimal") != -1)
         {
            currentField = new JTextField();
            ((JTextField) currentField).setHorizontalAlignment(JTextField.RIGHT);
            ((JTextField) currentField).setText("00.00");
            ((JTextField) currentField).setBounds(x + 145, y, 220, 20);
            componentFocusSequence.add((JTextField) currentField);
            formPanel.add((JTextField) currentField);
         }

         // BLOB, BYTEA, BINARY, RAW, & CLOB Type Fields
         else if ((columnClass.indexOf("String") == -1 && columnType.indexOf("BLOB") != -1)
                  || (columnClass.indexOf("BLOB") != -1 && columnType.indexOf("BLOB") != -1)
                  || (columnType.indexOf("BYTEA") != -1) || (columnType.indexOf("BINARY") != -1)
                  || (columnType.indexOf("RAW") != -1) || (columnType.indexOf("CLOB") != -1))
         {
            // Place the remove checkbox for eliminating
            // existing data as desired during edit.
            if (!addItem)
            {
               currentField = new JCheckBox(removeUpIcon, false);
               ((JCheckBox) currentField).setSelectedIcon(removeDownIcon);
               ((JCheckBox) currentField).setBounds(x + 345, y, 20, 20);
               blobRemoveCheckBoxesHashMap.put(columnName, (JCheckBox) currentField);
               formPanel.add((JCheckBox) currentField);
            }

            // Normal field component.
            currentField = new JButton("Browse");
            ((JButton) currentField).addActionListener(this);

            if (!addItem)
               ((JButton) currentField).setBounds(x + 145, y, 195, 20);
            else
               ((JButton) currentField).setBounds(x + 145, y, 220, 20);
            componentFocusSequence.add((JButton) currentField);
            formPanel.add((JButton) currentField);
         }

         // TEXT Type Fields
         else if ((columnClass.indexOf("String") != -1 && !columnType.equals("CHAR") && ((Integer) columnSizeHashMap
               .get(columnName)).intValue() > 255)
                  || (columnClass.indexOf("String") != -1 && columnType.equals("LONG")))
         {
            // Place the remove checkbox for eliminating
            // existing text data as desired during edit.
            if (!addItem)
            {
               currentField = new JCheckBox(removeUpIcon, false);
               ((JCheckBox) currentField).setSelectedIcon(removeDownIcon);
               ((JCheckBox) currentField).setBounds(x + 345, y, 20, 20);
               blobRemoveCheckBoxesHashMap.put(columnName, (JCheckBox) currentField);
               formPanel.add((JCheckBox) currentField);
            }

            // Normal text field component.
            currentField = new JButton("TEXT");
            ((JButton) currentField).addActionListener(this);
            if (!addItem)
               ((JButton) currentField).setBounds(x + 145, y, 195, 20);
            else
               ((JButton) currentField).setBounds(x + 145, y, 220, 20);
            componentFocusSequence.add((JButton) currentField);
            formPanel.add((JButton) currentField);
         }

         // ARRAY Type Fields
         else if ((columnClass.indexOf("Array") != -1 || columnClass.indexOf("Object") != -1)
                  && columnType.indexOf("_") != -1)
         {
            // Place the remove checkbox for eliminating
            // existing text data as desired during edit.
            if (!addItem)
            {
               currentField = new JCheckBox(removeUpIcon, false);
               ((JCheckBox) currentField).setSelectedIcon(removeDownIcon);
               ((JCheckBox) currentField).setBounds(x + 345, y, 20, 20);
               blobRemoveCheckBoxesHashMap.put(columnName, (JCheckBox) currentField);
               formPanel.add((JCheckBox) currentField);
            }

            // Normal text field component.
            currentField = new JButton("ARRAY");
            ((JButton) currentField).addActionListener(this);
            if (!addItem)
               ((JButton) currentField).setBounds(x + 145, y, 195, 20);
            else
               ((JButton) currentField).setBounds(x + 145, y, 220, 20);
            componentFocusSequence.add((JButton) currentField);
            formPanel.add((JButton) currentField);
         }

         // TIMESTAMP Type Fields.
         else if (columnType.equals("TIMESTAMP") || columnType.equals("TIMESTAMPTZ")
                  || columnType.equals("TIMESTAMPLTZ"))
         {
            currentField = new JTextField();
            if (addItem)
               ((JTextField) currentField).setEnabled(false);
            ((JTextField) currentField).setBounds(x + 145, y, 220, 20);
            componentFocusSequence.add((JTextField) currentField);
            formPanel.add((JTextField) currentField);
         }

         // DATE, DATETIME, YEAR, CHAR, VARCHAR,
         // TINYTEXT, & All Other Type Fields
         else
         {
            if (columnType.indexOf("DATE") != -1)
            {
               currentField = new JButton(calendarIcon);
               ((JButton) currentField).setBounds(x + 345, y, 20, 20);
               ((JButton) currentField).addActionListener(this);
               calendarButtonHashMap.put((JButton) currentField, columnName);
               formPanel.add((JButton) currentField);
            }

            currentField = new JTextField();
            ((JTextField) currentField).setText("");
            if (columnType.indexOf("DATE") != -1)
               ((JTextField) currentField).setBounds(x + 145, y, 195, 20);
            else
               ((JTextField) currentField).setBounds(x + 145, y, 220, 20);
            componentFocusSequence.add((JTextField) currentField);
            formPanel.add((JTextField) currentField);
         }

         // Build a list and moving to next object's position.
         fieldHashMap.put(columnName, (JComponent) currentField);
         if (y > (((formFields.size() + 1) / 2) * 28) - 20)
         {
            x = 395;
            y = 20;
         }
         else
            y += 28;
      }
      // Setup a scrollpane just in case sizing does not properly give
      // a large enough frame. Also in case user resizes frame.
      formPanel.setPreferredSize(new Dimension(780, formFields.size() / 2 * 32));

      JScrollPane formScrollPane = new JScrollPane(formPanel);
      formScrollPane.getVerticalScrollBar().setUnitIncrement(28);
      mainPanel.add(formScrollPane, BorderLayout.CENTER);

      // Creating Action Buttons & Limit Components.

      JPanel actionButtonPanel = new JPanel();
      formPanel.setBorder(BorderFactory.createRaisedBevelBorder());

      // Cancel Button
      resource = resourceBundle.getResource("TableEntryForm.button.Cancel");
      if (resource.equals(""))
         cancelButton = new JButton("Cancel");
      else
         cancelButton = new JButton(resource);
      cancelButton.addActionListener(this);
      componentFocusSequence.add(cancelButton);
      actionButtonPanel.add(cancelButton);

      // Add/Update Button & Limit Components
      if (addItem)
      {
         resource = resourceBundle.getResource("TableEntryForm.button.Add");
         if (resource.equals(""))
            updateButton = new JButton("Add");
         else
            updateButton = new JButton(resource);
      }
      else
      {
         resource = resourceBundle.getResource("TableEntryForm.button.Update");
         if (resource.equals(""))
            updateButton = new JButton("Update");
         else
            updateButton = new JButton(resource);
      }

      updateButton.addActionListener(this);
      componentFocusSequence.add(updateButton);
      actionButtonPanel.add(updateButton);

      if (!addItem)
      {
         JPanel limitPanel = new JPanel();
         limitPanel.setBorder(BorderFactory.createLoweredBevelBorder());

         resource = resourceBundle.getResource("TableEntryForm.checkbox.Limit");
         if (resource.equals(""))
            limitCheckBox = new JCheckBox("Limit", false);
         else
            limitCheckBox = new JCheckBox(resource, false);
         limitCheckBox.addActionListener(this);
         componentFocusSequence.add(limitCheckBox);
         limitPanel.add(limitCheckBox);

         limitTextField = new JTextField(4);
         limitTextField.setText("1");
         limitTextField.setEnabled(false);
         componentFocusSequence.add(limitTextField);
         limitPanel.add(limitTextField);

         actionButtonPanel.add(limitPanel);
      }

      mainPanel.add(actionButtonPanel, BorderLayout.SOUTH);
      getContentPane().add(mainPanel);
      (this.getRootPane()).setDefaultButton(updateButton);

      // Dummy Button to Fire Events.
      disposeButton = new JButton();

      // Setting the FocusTraversalPolicy
      focusSequence = new MyJSQLView_FocusTraversalPolicy(componentFocusSequence);
      this.setFocusTraversalPolicy(focusSequence);
      this.addWindowListener(tableEntryFormFrameListener);
   }

   //==============================================================
   // WindowListener for insuring that the frame is closed, (x), &
   // various objects are prompely disposed.
   //==============================================================

   private transient WindowListener tableEntryFormFrameListener = new WindowAdapter()
   {
      public void windowClosing(WindowEvent e)
      {
         if (dateCalendar != null)
            dateCalendar.dispose();
         if (setDialog != null)
            setDialog.dispose();
         dispose();
      }
   };

   //==============================================================
   // ActionEvent Listener method for detecting the inputs from
   // the panel and directing to the appropriate routine.
   //==============================================================

   public void actionPerformed(ActionEvent evt)
   {
      Object formSource = evt.getSource();

      if (formSource instanceof JButton)
      {
         // Function Button Action
         if (functionButtonHashMap.containsKey((JButton) formSource))
         {
            // Collect the function operator.
            String columnName = functionButtonHashMap.get((JButton) formSource);

            // 2.76 Blob Function Warning.
            if (((columnClassHashMap.get(columnName)).indexOf("String") == -1 && (columnTypeHashMap
                  .get(columnName)).indexOf("BLOB") != -1)
                || ((columnClassHashMap.get(columnName)).indexOf("BLOB") != -1 && (columnTypeHashMap
                      .get(columnName)).indexOf("BLOB") != -1)
                || ((columnTypeHashMap.get(columnName)).indexOf("BYTEA") != -1)
                || ((columnTypeHashMap.get(columnName)).indexOf("BINARY") != -1)
                || ((columnTypeHashMap.get(columnName)).indexOf("RAW") != -1))
            {
               String resource, message;

               resource = resourceBundle.getResource("TableEntryForm.dialogmessage.WarningFunction");
               if (resource.equals(""))
                  message = "Warning Function Operations on Blob Data NOT Tested! Likely Data Corruption!";
               else
                  message = resource;

               JOptionPane.showMessageDialog(null, message, resourceAlert, JOptionPane.ERROR_MESSAGE);
            }
            selectFunctionOperator(columnName);
         }

         // Blob, Text, MediumText, & LongText Button Actions
         else if (fieldHashMap.containsValue((JButton) formSource))
         {
            // Open Blob, Raw, & Clob File Directly.
            if (((JButton) evt.getSource()).getText().indexOf("BLOB") != -1
                || ((JButton) evt.getSource()).getText().indexOf("BYTEA") != -1
                || ((JButton) evt.getSource()).getText().indexOf("BINARY") != -1
                || ((JButton) evt.getSource()).getText().indexOf("RAW") != -1
                || ((JButton) evt.getSource()).getText().indexOf("CLOB") != -1)
               openBlobTextField(formSource);

            // Open Text entry or open file if desired.
            else
            {
               JEditorPane editorPane;
               InputDialog textDialog;
               String textContent;
               JMenuBar editorMenuBar;
               
               // Create an EditorPane to view/edit content.
               
               textContent = ((String) blobBytesHashMap.get((JButton) formSource));
               editorPane = new JEditorPane("text/plain", textContent);
               editorPane.addMouseListener(MyJSQLView.getPopupMenuListener());

               textDialog = MyJSQLView_Utils.createTextDialog(true, editorPane);

               editorMenuBar = MyJSQLView_Utils.createEditMenu(true);
               textDialog.setJMenuBar(editorMenuBar);
               textDialog.pack();
               textDialog.center();
               textDialog.setVisible(true);

               // Check to see if save data is desired.
               if (textDialog.isActionResult())
               {
                  blobBytesHashMap.put((JButton) formSource, editorPane.getText());
                  if (((JButton) evt.getSource()).getText().indexOf("TEXT") != -1)
                     ((JButton) formSource).setText("TEXT " + editorPane.getText().length() + " Bytes");
                  // Array
                  else
                     ((JButton) formSource).setText("ARRAY Data");
               }
               else if (!textDialog.getActionResult().equals("close"))
                  openBlobTextField(formSource);

               textDialog.dispose();
            }
         }

         // Date Field Button Action
         else if (calendarButtonHashMap.containsKey((JButton) formSource))
         {
            // Collect the info needed to pass to the DateFieldCalendar class.
            Object columnName = calendarButtonHashMap.get((JButton) formSource);
            String columnType = columnTypeHashMap.get(columnName);

            // Date selection frame.
            dateCalendar = new DateFieldCalendar(this, columnName, columnType);
            dateCalendar.setResizable(false);
            dateCalendar.pack();
            dateCalendar.center();
            dateCalendar.setVisible(true);
         }

         // Set Field Button Action
         else if (setButtonHashMap.containsKey((JButton) formSource))
         {
            // Collect the info needed to pass to the DateFieldCalendar class.
            Object columnName = setButtonHashMap.get((JButton) formSource);

            // Set list selection frame.
            setDialog = new SetListDialog(this, columnName, setFieldsHashMap.get(columnName));
            setDialog.setSize(new Dimension(325, 200));
            setDialog.center();
            setDialog.setVisible(true);
         }

         // Cancel Button Action
         else if (formSource == cancelButton)
         {
            if (dateCalendar != null)
               dateCalendar.dispose();
            if (setDialog != null)
               setDialog.dispose();
            dispose();
         }

         // Update Button Action
         else if (formSource == updateButton)
         {
            setVisible(false);
            validEntry = false;

            if (dateCalendar != null)
               dateCalendar.dispose();
            if (setDialog != null)
               setDialog.dispose();

            DBTablesPanel.startStatusTimer();

            Thread processEntryThread = new Thread(new Runnable()
            {
               public void run()
               {
                  addUpdateTableEntry();

                  if (validEntry)
                  {
                     // Make sure start clean
                     // in the viewForm for blob/text.
                     tableViewForm.clearBlobBytesHashMap();

                     // Notify Calling Panel to
                     // update item table.
                     disposeButton.doClick();
                     dispose();
                  }
                  DBTablesPanel.stopStatusTimer();
               }
            }, "TableEntryForm.processEntryThread");
            processEntryThread.start();
         }
      }

      // Limit TextField Access
      if (formSource instanceof JCheckBox)
      {
         if (formSource == limitCheckBox)
         {
            if (limitCheckBox.isSelected())
               limitTextField.setEnabled(true);
            else
            {
               limitTextField.setText("1");
               limitTextField.setEnabled(false);
            }
         }
      }
   }

   //==============================================================
   // Class method to obtain data for a blog or text field from a
   // selected input file.
   //==============================================================

   protected void openBlobTextField(Object formSource)
   {
      // Class Method Instance
      String fileName;
      byte[] inBytes;

      // Choosing the file to import data from.
      JFileChooser importData = new JFileChooser();
      int result = importData.showOpenDialog(null);

      // Looks like might be good file name so lets check
      // and then set the blob data
      if (result == JFileChooser.APPROVE_OPTION)
      {
         fileName = importData.getSelectedFile().getName();
         fileName = importData.getCurrentDirectory() + "/" + fileName;
         // System.out.println(fileName);

         if (!fileName.equals(""))
         {
            try
            {
               // Blob data
               if (((JButton) formSource).getText().indexOf("BLOB") != -1
                   || ((JButton) formSource).getText().indexOf("BYTEA") != -1
                   || ((JButton) formSource).getText().indexOf("BINARY") != -1
                   || ((JButton) formSource).getText().indexOf("RAW") != -1)
               {
                  // Setting up InputStreams
                  FileInputStream fileStream = new FileInputStream(fileName);
                  BufferedInputStream filebuff = new BufferedInputStream(fileStream);
                  inBytes = new byte[filebuff.available()];

                  // Reading the Specified Input File and Placing
                  // Data Into a Byte Array.

                  filebuff.read(inBytes);
                  blobBytesHashMap.put((JButton) formSource, inBytes);
                  if (((JButton) formSource).getText().indexOf("BLOB") != -1)
                     ((JButton) formSource).setText("BLOB " + inBytes.length + " Bytes");
                  else if (((JButton) formSource).getText().indexOf("BYTEA") != -1)
                     ((JButton) formSource).setText("BYTEA " + inBytes.length + " Bytes");
                  else if (((JButton) formSource).getText().indexOf("BINARY") != -1)
                     ((JButton) formSource).setText("BINARY " + inBytes.length + " Bytes");
                  else
                     ((JButton) formSource).setText("RAW " + inBytes.length + " Bytes");
                  // Closing up.
                  filebuff.close();
                  fileStream.close();
               }
               // Text data
               else
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
                     textString.append(inputString + "\n");

                  blobBytesHashMap.put((JButton) formSource, textString.toString());
                  if (((JButton) formSource).getText().indexOf("CLOB") != -1)
                     ((JButton) formSource).setText("CLOB " + textString.length() + " Bytes");
                  else
                     ((JButton) formSource).setText("TEXT " + textString.length() + " Bytes");

                  filebuff.close();
                  fileReader.close();
               }
            }
            catch (IOException e)
            {
               String resource, message;

               resource = resourceBundle.getResource("TableEntryForm.dialogmessage.ErrorReading");
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
   // Class method to process the data in the table form to add or
   // update an item.
   //==============================================================

   private void addUpdateTableEntry()
   {
      // Method Instances
      String schemaName, tableName;
      String columnName, columnClass, columnType;
      StringBuffer sqlStatementString;
      String sqlFieldNamesString, sqlValuesString;
      Statement sqlStatement;
      PreparedStatement prepared_sqlStatement;
      Iterator<String> keyIterator, columnNamesIterator;

      String currentKey_ColumnName, currentDB_ColumnName;
      Object currentContentData;
      String dateString, timeString;
      String resource, message;
      boolean isTextField, isBlobField, isArrayField;
      int columnSize;
      int keyColumn = 0;

      // Get Connection to Database.
      Connection db_Connection = (Connection) ConnectionManager.getConnection(
         "TableEntryForm addUpdateTableEntry()");

      if (db_Connection == null)
      {
         validEntry = false;
         return;
      }

      // Form Processing.
      try
      {
         db_Connection.setAutoCommit(false);
         sqlStatement = db_Connection.createStatement();
         sqlStatementString = new StringBuffer();

         // Only MySQL & PostgreSQL support.
         if (subProtocol.equals(ConnectionManager.MYSQL)
             || subProtocol.equals(ConnectionManager.POSTGRESQL))
            sqlStatement.executeUpdate("BEGIN");

         // ====================
         // Adding an entry.

         if (addItem)
         {
            // Beginner SQL statement creation.
            sqlStatementString.append("INSERT INTO " + sqlTable + " ");
            sqlFieldNamesString = "(";
            sqlValuesString = "VALUES (";

            columnNamesIterator = formFields.iterator();

            while (columnNamesIterator.hasNext())
            {
               // Prepping some instances for making things more clear
               // and easier.

               columnName = columnNamesIterator.next();
               columnClass = columnClassHashMap.get(columnName);
               columnType = columnTypeHashMap.get(columnName);
               columnSize = (columnSizeHashMap.get(columnName)).intValue();
               isTextField = (columnClass.indexOf("String") != -1 && !columnType.equals("CHAR")
                              && columnSize > 255)
                              || (columnClass.indexOf("String") != -1 && columnType.equals("LONG"))
                             || (columnType.indexOf("CLOB") != -1);
               isBlobField = (columnClass.indexOf("String") == -1 && columnType.indexOf("BLOB") != -1)
                             || (columnClass.indexOf("BLOB") != -1 && columnType.indexOf("BLOB") != -1)
                             || columnType.indexOf("BYTEA") != -1 || columnType.indexOf("BINARY") != -1
                             || columnType.indexOf("RAW") != -1;
               isArrayField = (columnClass.indexOf("Array") != -1 || columnClass.indexOf("Object") != -1)
                              && columnType.indexOf("_") != -1;

               // Composing intial SQL prepareStatement with special
               // consideration
               // for certain types of entries/fields.

               // Empty entry field, or MS Access Autoincrement field.

               if ((!isTextField && !isBlobField && !isArrayField && !functionsHashMap.containsKey(columnName))
                   && ((getFormField(columnName).equals(""))
                        || (subProtocol.equals(ConnectionManager.MSACCESS) && 
                            getFormField(columnName).toLowerCase().equals("auto"))))
               {
                  // Do Nothing, Field Takes Default.
               }

               // Explicit Default Entry Field

               else if (!isTextField && !isBlobField && !isArrayField
                        && getFormField(columnName).toLowerCase().equals("default"))
               {
                  sqlFieldNamesString += identifierQuoteString + columnNamesHashMap.get(columnName)
                                         + identifierQuoteString + ", ";
                  sqlValuesString += "default, ";
               }

               // Null Entry Field

               else if (!isTextField && !isBlobField && !isArrayField
                        && getFormField(columnName).toLowerCase().equals("null"))
               {
                  sqlFieldNamesString += identifierQuoteString + columnNamesHashMap.get(columnName)
                                         + identifierQuoteString + ", ";
                  sqlValuesString += "null, ";
               }

               // Normal/special Data Entry Fields

               else
               {
                  // Add the current field that will be inserted.
                  sqlFieldNamesString += identifierQuoteString + columnNamesHashMap.get(columnName)
                                         + identifierQuoteString + ", ";

                  // AutoIncremnt, sequenced fields.
                  if (autoIncrementHashMap.containsKey(columnName))
                  {
                     if (getFormField(columnName).toLowerCase().equals("auto"))
                     {
                        if (subProtocol.equals(ConnectionManager.POSTGRESQL))
                        {
                           schemaName = sqlTable.substring(0, sqlTable.indexOf(".") + 2);
                           tableName = (sqlTable.substring(sqlTable.indexOf(".") + 1)).replaceAll(
                                        identifierQuoteString, "");

                           sqlValuesString += "nextval('" + schemaName + tableName + "_"
                                              + columnNamesHashMap.get(columnName) + "_seq\"'), ";
                        }
                        else if (subProtocol.indexOf(ConnectionManager.ORACLE) != -1)
                        {
                           schemaName = sqlTable.substring(0, sqlTable.indexOf(".") + 2);
                           tableName = (sqlTable.substring(sqlTable.indexOf(".") + 1)).replaceAll(
                                        identifierQuoteString, "");

                           sqlValuesString += autoIncrementHashMap.get(columnName) + ".NEXTVAL, ";
                        }
                        else
                           sqlValuesString += "null, ";
                     }
                     else
                        sqlValuesString += "?, ";
                  }

                  // TimeStamp fields.
                  else if (columnType.equals("TIMESTAMP") || columnType.equals("TIMESTAMPTZ")
                           || columnType.equals("TIMESTAMPLTZ"))
                  {
                     if (subProtocol.indexOf(ConnectionManager.ORACLE) != -1)
                        sqlValuesString += "SYSTIMESTAMP, ";
                     else
                        sqlValuesString += "NOW(), ";
                  }

                  // Special fields that can not be represented
                  // by java types.

                  // PostgreSQL Interval fields.
                  else if (columnType.equals("INTERVAL"))
                  {
                     sqlValuesString += "'" + getFormField(columnName) + "', ";
                  }

                  // PostgreSQL Bit fields.
                  else if (columnType.indexOf("BIT") != -1
                           && !subProtocol.equals(ConnectionManager.MYSQL)
                           && !subProtocol.equals(ConnectionManager.MSACCESS)
                           && columnType.indexOf("_") == -1)
                  {
                     sqlValuesString += "B'" + getFormField(columnName) + "', ";
                  }

                  // PostgreSQL Geometric fields.
                  else if (columnType.equals("POINT") || columnType.equals("LSEG")
                           || columnType.equals("BOX") || columnType.equals("PATH")
                           || columnType.equals("POLYGON") || columnType.equals("CIRCLE"))
                  {
                     sqlValuesString += "'" + getFormField(columnName) + "', ";
                  }

                  // PostgreSQL Network Address fields.
                  else if (columnType.equals("CIDR") || columnType.equals("INET")
                           || columnType.equals("MACADDR"))
                  {
                     sqlValuesString += "'" + getFormField(columnName) + "', ";
                  }

                  // Oracle BFILE fields.
                  else if (columnType.equals("BFILE"))
                  {
                     String directoryName, fileName;
                     int commaIndex;

                     if (getFormField(columnName).indexOf(',') != -1)
                     {
                        commaIndex = getFormField(columnName).indexOf(',');
                        directoryName = getFormField(columnName).substring(0, commaIndex - 1);
                        fileName = getFormField(columnName).substring((commaIndex + 1));

                        sqlValuesString += "BFILENAME('" + directoryName + "', '" + fileName + "'), ";
                     }
                     else
                     {
                        JOptionPane.showMessageDialog(null, resourceInvalidInput + " " + columnName
                                                            + ", " + resourceType + ": " + columnType,
                                                            resourceAlert, JOptionPane.ERROR_MESSAGE);
                        validEntry = false;
                        setVisible(true);
                        db_Connection.rollback();
                        sqlStatement.close();
                        db_Connection.setAutoCommit(true);
                        ConnectionManager.closeConnection(db_Connection,
                           "TableEntryForm addUpdateTableEntry()");
                        return;
                     }
                  }

                  else if (isArrayField)
                  {
                     // Make sure text button is present
                     if (fieldHashMap.get(columnName) != null)
                     {
                        // Check to see if data has been put in field.
                        if (blobBytesHashMap.get((JButton) fieldHashMap.get(columnName)) != null)
                        {
                           // Finally check to see if its an update and if so
                           // then is
                           // data to be updated or removed.
                           JCheckBox currentRemoveBlobCheckBox = blobRemoveCheckBoxesHashMap.get(columnName);
                           if (currentRemoveBlobCheckBox != null)
                           {
                              if (currentRemoveBlobCheckBox.isSelected())
                                 sqlValuesString += "null, ";
                              else
                                 sqlValuesString += "'" + getFormFieldText(columnName) + "', ";
                           }
                           else
                              sqlValuesString += "'" + getFormFieldText(columnName) + "', ";
                        }
                        else
                           sqlValuesString += "null, ";
                     }
                     else
                        sqlValuesString += "null, ";
                  }

                  // Function or Normal Fields
                  else
                  {
                     // Implement function operator as needed.
                     if (functionsHashMap.containsKey(columnName))
                        sqlValuesString += createFunctionSQLStatement(columnName);
                     else
                        sqlValuesString += "?, ";
                  }
               }
            }
            // Concatenate everything together.
            if (sqlFieldNamesString.length() >= 3)
               sqlFieldNamesString = sqlFieldNamesString.substring(0, sqlFieldNamesString.length() - 2) + ")";
            else
               sqlFieldNamesString += ")";

            if (sqlValuesString.length() > 8)
               sqlValuesString = sqlValuesString.substring(0, sqlValuesString.length() - 2) + ")";
            else
               sqlValuesString += ")";

            sqlStatementString.append(sqlFieldNamesString + " " + sqlValuesString);
            // System.out.println(sqlStatementString);
         }

         // =====================
         // Updating an entry.

         else
         {
            // Beginner SQL statement creation.
            sqlStatementString.append("UPDATE " + sqlTable + " SET ");

            columnNamesIterator = formFields.iterator();

            while (columnNamesIterator.hasNext())
            {
               // Prepping some instances for making things more clear
               // and easier.

               columnName = columnNamesIterator.next();
               columnClass = columnClassHashMap.get(columnName);
               columnType = columnTypeHashMap.get(columnName);
               columnSize = (columnSizeHashMap.get(columnName)).intValue();
               isTextField = (columnClass.indexOf("String") != -1 && !columnType.equals("CHAR") && columnSize > 255)
                             || (columnClass.indexOf("String") != -1 && columnType.equals("LONG"))
                             || (columnType.indexOf("CLOB") != -1);
               isBlobField = (columnClass.indexOf("String") == -1 && columnType.indexOf("BLOB") != -1)
                             || (columnClass.indexOf("BLOB") != -1 && columnType.indexOf("BLOB") != -1)
                             || columnType.indexOf("BYTEA") != -1 || columnType.indexOf("BINARY") != -1
                             || columnType.indexOf("RAW") != -1;
               isArrayField = (columnClass.indexOf("Array") != -1 || columnClass.indexOf("Object") != -1)
                              && columnType.indexOf("_") != -1;

               // Empty entry field.

               if (!isTextField && !isBlobField && !isArrayField && !functionsHashMap.containsKey(columnName)
                   && getFormField(columnName).equals(""))
               {
                  // Do Nothing, Field Takes Default.
               }

               // Explicit Default Entry Field

               else if (!isTextField && !isBlobField && !isArrayField
                        && getFormField(columnName).toLowerCase().equals("default"))
               {
                  sqlStatementString.append(identifierQuoteString + columnNamesHashMap.get(columnName)
                                            + identifierQuoteString + "=default, ");
               }

               // Null Entry Field

               else if (!isTextField && !isBlobField && !isArrayField
                        && getFormField(columnName).toLowerCase().equals("null"))
               {
                  sqlStatementString.append(identifierQuoteString + columnNamesHashMap.get(columnName)
                                            + identifierQuoteString + "=null, ");
               }

               // Normal/special Data Entry Fields

               else
               {
                  // AutoIncremnt, sequenced fields.
                  if (autoIncrementHashMap.containsKey(columnName))
                  {
                     if (getFormField(columnName).toLowerCase().equals("AUTO".toLowerCase()))
                     {
                        if (subProtocol.equals(ConnectionManager.POSTGRESQL))
                        {
                           schemaName = sqlTable.substring(0, sqlTable.indexOf(".") + 2);
                           tableName = (sqlTable.substring(sqlTable.indexOf(".") + 1)).replaceAll(
                              identifierQuoteString, "");

                           sqlStatementString.append("nextval('" + schemaName + tableName + "_"
                                                     + columnNamesHashMap.get(columnName) + "_seq\"'), ");
                        }
                        else if (subProtocol.indexOf(ConnectionManager.ORACLE) != -1)
                        {
                           schemaName = sqlTable.substring(0, sqlTable.indexOf(".") + 2);
                           tableName = (sqlTable.substring(sqlTable.indexOf(".") + 1)).replaceAll(
                              identifierQuoteString, "");

                           sqlStatementString.append(autoIncrementHashMap.get(columnName) + ".NEXTVAL, ");
                        }
                        else
                           sqlStatementString.append(identifierQuoteString + columnNamesHashMap.get(columnName)
                                                     + identifierQuoteString + "=null, ");
                     }
                     else
                     {
                        // Can't Update autoincrement in MS Access.
                        if (!subProtocol.equals(ConnectionManager.MSACCESS))
                           sqlStatementString.append(identifierQuoteString + columnNamesHashMap.get(columnName)
                                                     + identifierQuoteString + "=?, ");
                     }
                  }

                  // Special fields that can not be represented
                  // by java types.

                  // PostgreSQL Interval fields.
                  else if (columnType.equals("INTERVAL"))
                  {
                     sqlStatementString.append(identifierQuoteString + columnNamesHashMap.get(columnName)
                                               + identifierQuoteString + "='" + getFormField(columnName)
                                               + "', ");
                  }

                  // PostgreSQL Bit fields.
                  else if (columnType.indexOf("BIT") != -1
                           && !subProtocol.equals(ConnectionManager.MYSQL)
                           && !subProtocol.equals(ConnectionManager.MSACCESS)
                           && columnType.indexOf("_") == -1)
                  {
                     sqlStatementString.append(identifierQuoteString + columnNamesHashMap.get(columnName)
                                               + identifierQuoteString + "=B'" + getFormField(columnName)
                                               + "', ");
                  }

                  // PostgreSQL Geometric fields.
                  else if (columnType.equals("POINT") || columnType.equals("LSEG")
                           || columnType.equals("BOX") || columnType.equals("PATH")
                           || columnType.equals("POLYGON") || columnType.equals("CIRCLE"))
                  {
                     sqlStatementString.append(identifierQuoteString + columnNamesHashMap.get(columnName)
                                               + identifierQuoteString + "='" + getFormField(columnName)
                                               + "', ");
                  }

                  // PostgreSQL Network Address fields.
                  else if (columnType.equals("CIDR") || columnType.equals("INET")
                           || columnType.equals("MACADDR"))
                  {
                     sqlStatementString.append(identifierQuoteString + columnNamesHashMap.get(columnName)
                                               + identifierQuoteString + "='" + getFormField(columnName)
                                               + "', ");
                  }

                  // Oracle BFILE fields.
                  else if (columnType.equals("BFILE"))
                  {
                     // Do nothing not supported.
                  }

                  else if (isArrayField)
                  {
                     // Make sure text button is present
                     if (fieldHashMap.get(columnName) != null)
                     {
                        // Check to see if data has been put in field.
                        if (blobBytesHashMap.get((JButton) fieldHashMap.get(columnName)) != null)
                        {
                           // Finally check to see if its an update and if so
                           // then is
                           // data to be updated or removed.
                           JCheckBox currentRemoveBlobCheckBox = blobRemoveCheckBoxesHashMap.get(columnName);
                           if (currentRemoveBlobCheckBox != null)
                           {
                              if (currentRemoveBlobCheckBox.isSelected())
                                 sqlStatementString.append(identifierQuoteString
                                                           + columnNamesHashMap.get(columnName)
                                                           + identifierQuoteString + "=null, ");
                              else
                                 sqlStatementString.append(identifierQuoteString
                                                           + columnNamesHashMap.get(columnName)
                                                           + identifierQuoteString + "='"
                                                           + getFormFieldText(columnName) + "', ");
                           }
                           else
                              sqlStatementString.append(identifierQuoteString
                                                        + columnNamesHashMap.get(columnName)
                                                        + identifierQuoteString + "='"
                                                        + getFormFieldText(columnName) + "', ");
                        }
                        else
                           sqlStatementString.append(identifierQuoteString + columnNamesHashMap.get(columnName)
                                                     + identifierQuoteString + "=null, ");
                     }
                     else
                        sqlStatementString.append(identifierQuoteString + columnNamesHashMap.get(columnName)
                                                  + identifierQuoteString + "=null, ");
                  }

                  // Function or Normal Fields
                  else
                  {
                     // Implement function operator as needed.
                     if (functionsHashMap.containsKey(columnName))
                     {
                        sqlStatementString.append(identifierQuoteString + columnNamesHashMap.get(columnName)
                                                  + identifierQuoteString + "=");
                        sqlStatementString.append(createFunctionSQLStatement(columnName));
                     }
                     else
                        sqlStatementString.append(identifierQuoteString + columnNamesHashMap.get(columnName)
                                                  + identifierQuoteString + "=?, ");
                  }
               }
            }
            sqlStatementString.delete((sqlStatementString.length() - 2), sqlStatementString.length());
            sqlStatementString.append(" WHERE ");

            // ==============================
            // Continuing with construction
            // of the prepareStatment.

            // Find the column holding the key entry.
            keyIterator = primaryKeys.iterator();

            while (keyIterator.hasNext())
            {
               currentKey_ColumnName = keyIterator.next();

               for (int i = 0; i < selectedTableTabPanel.getListTable().getColumnCount(); i++)
               {
                  currentDB_ColumnName = columnNamesHashMap.get(selectedTableTabPanel.getListTable()
                        .getColumnName(i));
                  if (currentDB_ColumnName.equals(currentKey_ColumnName))
                     keyColumn = i;
               }

               // Key found so obtain info.
               currentContentData = selectedTableTabPanel.getListTable().getValueAt(selectedRow, keyColumn);

               // Special case where the table defines a blob/text for the key.
               if (currentContentData instanceof BlobTextKey)
               {
                  String keyString = ((BlobTextKey) currentContentData).getContent();
                  keyString = keyString.replaceAll("'", "''");

                  sqlStatementString.append(identifierQuoteString + currentKey_ColumnName + identifierQuoteString
                                            + " LIKE '" + keyString + "%' AND ");
               }
               // Normal key.
               else
               {
                  // Handle null content properly.
                  if ((currentContentData + "").toLowerCase().equals("null"))
                  {
                     currentContentData = "IS NULL";
                     sqlStatementString.append(identifierQuoteString + currentKey_ColumnName
                                               + identifierQuoteString + " " + currentContentData
                                               + " AND ");
                  }
                  else
                  {
                     // Escape single quotes.
                     columnClass = columnClassHashMap.get(selectedTableTabPanel
                           .parseColumnNameField(currentKey_ColumnName));
                     if (columnClass.indexOf("String") != -1)
                        currentContentData = ((String) currentContentData).replaceAll("'", "''");

                     columnType = columnTypeHashMap.get(selectedTableTabPanel
                           .parseColumnNameField(currentKey_ColumnName));
                     
                     if (columnType.indexOf("DATE") != -1)
                     {
                        if (subProtocol.indexOf(ConnectionManager.ORACLE) != -1)
                        {
                           currentContentData = "TO_DATE('"
                              + MyJSQLView_Utils.convertViewDateString_To_DBDateString(
                                 currentContentData + "",
                                 DBTablesPanel.getGeneralProperties().getViewDateFormat())
                                 + "', 'YYYY-MM-dd')";
                        }
                        else
                           currentContentData = "'" + MyJSQLView_Utils.convertViewDateString_To_DBDateString(
                              currentContentData + "", DBTablesPanel.getGeneralProperties().getViewDateFormat())
                              + "'";
                        
                        sqlStatementString.append(identifierQuoteString + currentKey_ColumnName
                                                  + identifierQuoteString + "="
                                                  + currentContentData + " AND ");
                     }
                     else
                     {
                        // Character data gets single quotes for some databases,
                        // not numbers though.
                        
                        if (subProtocol.equals("odbc"))
                        {
                           if (columnType.indexOf("CHAR") != -1 || columnType.indexOf("TEXT") != -1)
                              sqlStatementString.append(identifierQuoteString + currentKey_ColumnName
                                                        + identifierQuoteString + "='"
                                                        + currentContentData + "' AND ");
                           else
                              sqlStatementString.append(identifierQuoteString + currentKey_ColumnName
                                 + identifierQuoteString + "="
                                 + currentContentData + " AND ");
                              
                        }
                        else
                           sqlStatementString.append(identifierQuoteString + currentKey_ColumnName
                              + identifierQuoteString + "='"
                              + currentContentData + "' AND ");
                     }  
                  }
               }
            }
            sqlStatementString.delete((sqlStatementString.length() - 5), sqlStatementString.length());

            // Adding LIMIT expression for supported databases.
            if (subProtocol.equals(ConnectionManager.MYSQL))
            {
               if (limitCheckBox.isSelected())
               {
                  try
                  {
                     int limitValue = Integer.parseInt(limitTextField.getText());
                     if (limitValue <= 0)
                        Integer.parseInt("a");
                     sqlStatementString.append(" LIMIT " + limitValue);
                  }
                  catch (NumberFormatException e)
                  {
                     resource = resourceBundle.getResource("TableEntryForm.dialogmessage.InvalidLimit");
                     if (resource.equals(""))
                        message = "Invalid Input for LIMIT value! Must be an UNSIGNED INTEGER Larger Than Zero";
                     else
                        message = resource;
                     
                     JOptionPane.showMessageDialog(null, message, resourceAlert, JOptionPane.ERROR_MESSAGE);
                     
                     validEntry = false;
                     setVisible(true);
                     db_Connection.rollback();
                     sqlStatement.close();
                     db_Connection.setAutoCommit(true);
                     ConnectionManager.closeConnection(db_Connection, "TableEntryForm addUpdateTableEntry()");
                     return;
                  }
               }
               else
               {
                  sqlStatementString.append(" LIMIT 1");
               }
            }
         }
         // System.out.println(sqlStatementString);

         // ======================================================
         // Accessing the database and setting values for each
         // selected entry in the prepareStatement.

         prepared_sqlStatement = db_Connection.prepareStatement(sqlStatementString.toString());
         columnNamesIterator = formFields.iterator();
         int i = 1;

         while (columnNamesIterator.hasNext())
         {
            // Prepping some instances for making things more clear
            // and easier.

            columnName = columnNamesIterator.next();
            columnClass = columnClassHashMap.get(columnName);
            columnType = columnTypeHashMap.get(columnName);
            columnSize = (columnSizeHashMap.get(columnName)).intValue();
            isTextField = (columnClass.indexOf("String") != -1 && !columnType.equals("CHAR") && columnSize > 255)
                          || (columnClass.indexOf("String") != -1 && columnType.equals("LONG"))
                          || (columnType.indexOf("CLOB") != -1);
            isBlobField = (columnClass.indexOf("String") == -1 && columnType.indexOf("BLOB") != -1)
                          || (columnClass.indexOf("BLOB") != -1 && columnType.indexOf("BLOB") != -1)
                          || columnType.indexOf("BYTEA") != -1 || columnType.indexOf("BINARY") != -1
                          || columnType.indexOf("RAW") != -1;
            isArrayField = (columnClass.indexOf("Array") != -1 || columnClass.indexOf("Object") != -1)
                           && columnType.indexOf("_") != -1;
            // System.out.println(i + " " + columnName + " " + columnClass + " "
            //                   + columnType + ": " + getFormField(columnName));

            // Validating input and setting content to fields

            // Skip Fields That Have Already Been Set.
            if ((!isTextField && !isBlobField)
                && !isArrayField
                && (getFormField(columnName).equals("")
                    || getFormField(columnName).toLowerCase().equals("null")
                    || getFormField(columnName).toLowerCase().equals("default")
                    || (autoIncrementHashMap.containsKey(columnName)
                        && subProtocol.equals(ConnectionManager.MSACCESS))))
            {
               // Do Nothing, Field Already Set.
            }

            // Function Operation. Wide Open No Checks.
            else if (functionsHashMap.containsKey(columnName))
            {
               // Do Nothing.
            }

            // Auto-Increment Type Field
            else if (autoIncrementHashMap.containsKey(columnName))
            {
               if (getFormField(columnName).toLowerCase().equals("auto"))
               {
                  // Do Nothing.
               }
               else
               {
                  try
                  {
                     int int_value = Integer.parseInt(getFormField(columnName));
                     prepared_sqlStatement.setInt(i++, int_value);
                  }
                  catch (NumberFormatException e)
                  {
                     resource = resourceBundle.getResource("TableEntryForm.dialogmessage.TypeAuto");
                     if (resource.equals(""))
                        message = "Type: INTEGER or NULL(Auto-Increment)";
                     else
                        message = resource;
                     
                     JOptionPane.showMessageDialog(null, resourceInvalidInput + " " + columnName
                                                   + ", " + message, resourceAlert,
                                                   JOptionPane.ERROR_MESSAGE);
                     
                     validEntry = false;
                     setVisible(true);
                     db_Connection.rollback();
                     prepared_sqlStatement.close();
                     sqlStatement.close();
                     db_Connection.setAutoCommit(true);
                     ConnectionManager.closeConnection(db_Connection,
                                                       "TableEntryForm addUpdateTableEntry()");
                     return;
                  }
               }
            }

            // Numeric Type Fields
            else if (columnClass.indexOf("Byte") != -1 || columnClass.indexOf("Short") != -1
                     || columnClass.indexOf("Integer") != -1 || columnClass.indexOf("Long") != -1
                     || columnClass.indexOf("Float") != -1 || columnClass.indexOf("Double") != -1
                     || columnClass.indexOf("BigDecimal") != -1)
            {
               try
               {
                  String value = getFormField(columnName);
                  // System.out.println(columnType + " " + value);

                  // Byte
                  if (columnClass.indexOf("Byte") != -1)
                  {
                     byte byte_value = (Byte.valueOf(value)).byteValue();
                     prepared_sqlStatement.setByte(i++, byte_value);
                  }
                  // Short
                  else if (columnClass.indexOf("Short") != -1)
                  {
                     short short_value = (Short.valueOf(value)).shortValue();
                     prepared_sqlStatement.setShort(i++, short_value);
                  }
                  // Integer
                  else if (columnClass.indexOf("Integer") != -1)
                  {
                     int int_value = Integer.parseInt(value);
                     prepared_sqlStatement.setInt(i++, int_value);
                  }
                  // Long
                  else if (columnClass.indexOf("Long") != -1)
                  {
                     long long_value = (Long.valueOf(value)).longValue();
                     prepared_sqlStatement.setLong(i++, long_value);
                  }
                  // Float
                  else if (columnClass.indexOf("Float") != -1)
                  {
                     float float_value = (Float.valueOf(value)).floatValue();
                     prepared_sqlStatement.setFloat(i++, float_value);
                  }
                  // Double
                  else if (columnClass.indexOf("Double") != -1)
                  {
                     double double_value = (Double.valueOf(value)).doubleValue();
                     prepared_sqlStatement.setDouble(i++, double_value);
                  }
                  // Must Be BigDecimal
                  else
                  {
                     BigDecimal decimal_value = new BigDecimal(value);
                     prepared_sqlStatement.setBigDecimal(i++, decimal_value);
                  }
               }
               catch (NumberFormatException e)
               {
                  JOptionPane.showMessageDialog(null, resourceInvalidInput + " " + columnName + ", "
                                                      + resourceType + ": " + columnType, resourceAlert,
                                                      JOptionPane.ERROR_MESSAGE);
                  validEntry = false;
                  setVisible(true);
                  db_Connection.rollback();
                  prepared_sqlStatement.close();
                  sqlStatement.close();
                  db_Connection.setAutoCommit(true);
                  ConnectionManager.closeConnection(db_Connection, "TableEntryForm addUpdateTableEntry()");
                  return;
               }
            }

            // Date Type Fields
            else if (columnClass.indexOf("Date") != -1 || (columnClass.toUpperCase()).indexOf("TIME") != -1)
            {
               String dateTimeFormString = getFormField(columnName).trim();

               try
               {
                  // Date
                  if (columnType.equals("DATE"))
                  {
                     java.sql.Date dateValue;

                     // Check for some kind of valid input.
                     if (!(dateTimeFormString.length() >= 10 && dateTimeFormString.length() < 12))
                        java.sql.Date.valueOf("error");

                     dateString = MyJSQLView_Utils.convertViewDateString_To_DBDateString(
                        dateTimeFormString, DBTablesPanel.getGeneralProperties().getViewDateFormat());
                     dateValue = java.sql.Date.valueOf(dateString);
                     prepared_sqlStatement.setDate(i++, dateValue);
                  }
                  // Time
                  else if (columnType.equals("TIME") || columnType.equals("TIMETZ"))
                  {
                     java.sql.Time timeValue;

                     // Check for some kind of valid input.
                     if (dateTimeFormString.length() < 8)
                        timeValue = Time.valueOf("error");

                     timeValue = java.sql.Time.valueOf(dateTimeFormString.substring(0, 7));
                     prepared_sqlStatement.setTime(i++, timeValue);
                  }
                  // DateTime
                  else if (columnType.equals("DATETIME"))
                  {
                     java.sql.Timestamp dateTimeValue;
                     dateString = "";
                     timeString = "";

                     // Check for some kind of valid input.
                     if (dateTimeFormString.indexOf(" ") == -1 || dateTimeFormString.length() < 10)
                        java.sql.Date.valueOf("error");

                     dateString = dateTimeFormString.substring(0, dateTimeFormString.indexOf(" "));
                     dateString = MyJSQLView_Utils.convertViewDateString_To_DBDateString(dateString,
                        DBTablesPanel.getGeneralProperties().getViewDateFormat());
                     timeString = getFormField(columnName).substring(dateTimeFormString.indexOf(" "));
                     dateTimeValue = java.sql.Timestamp.valueOf(dateString + timeString);
                     prepared_sqlStatement.setTimestamp(i++, dateTimeValue);
                  }
                  // Timestamp
                  else if (columnType.equals("TIMESTAMP") || columnType.equals("TIMESTAMPTZ")
                           || columnType.equals("TIMESTAMPLTZ"))
                  {
                     if (columnType.equals("TIMESTAMPLTZ"))
                        MyJSQLView_Utils.setLocalTimeZone(sqlStatement);

                     if (addItem)
                     {
                        // Do Nothing, Already set to NOW().
                     }
                     // Allow editing a timestamp.
                     else
                     {
                        SimpleDateFormat timeStampFormat;
                        java.sql.Timestamp dateTimeValue;
                        java.util.Date dateParse;

                        try
                        {
                           // Create a Timestamp Format.
                           if (columnType.equals("TIMESTAMP"))
                           {
                              if (columnSize == 2)
                                 timeStampFormat = new SimpleDateFormat("yy");
                              else if (columnSize == 4)
                                 timeStampFormat = new SimpleDateFormat("MM-yy");
                              else if (columnSize == 6)
                                 timeStampFormat = new SimpleDateFormat("MM-dd-yy");
                              else if (columnSize == 8)
                                 timeStampFormat = new SimpleDateFormat("MM-dd-yyyy");
                              else if (columnSize == 10)
                                 timeStampFormat = new SimpleDateFormat("MM-dd-yy HH:mm");
                              else if (columnSize == 12)
                                 timeStampFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm");
                              // All current coloumnSizes for MySQL > 5.0 Should be 19.
                              else
                                 timeStampFormat = new SimpleDateFormat(
                                    DBTablesPanel.getGeneralProperties().getViewDateFormat()
                                    + " HH:mm:ss");
                           }
                           else
                           {
                              if (columnType.equals("TIMESTAMPLTZ"))
                                 timeStampFormat = new SimpleDateFormat(
                                    DBTablesPanel.getGeneralProperties().getViewDateFormat()
                                    + " HH:mm:ss Z");
                              else
                                 timeStampFormat = new SimpleDateFormat(
                                    DBTablesPanel.getGeneralProperties().getViewDateFormat()
                                    + " HH:mm:ss z");
                           }

                           // Parse the TimeStamp Format.
                           if (columnType.equals("TIMESTAMPLTZ"))
                           {
                              dateString = dateTimeFormString;
                              dateString = dateString.substring(0, dateString.lastIndexOf(':'))
                                           + dateString.substring(dateString.lastIndexOf(':') + 1);
                              dateParse = timeStampFormat.parse(dateString);
                           }
                           else
                              dateParse = timeStampFormat.parse(dateTimeFormString);

                           dateTimeValue = new Timestamp(dateParse.getTime());
                           // System.out.println(dateTimeValue);
                           prepared_sqlStatement.setTimestamp(i++, dateTimeValue);
                        }
                        catch (ParseException e)
                        {
                           throw (new IllegalArgumentException(e + ""));
                        }
                     }
                  }
                  // Must be Year
                  else
                  {
                     dateString = dateTimeFormString;
                     java.sql.Date yearValue = java.sql.Date.valueOf(dateString + "-01-01");
                     prepared_sqlStatement.setString(i++, yearValue.toString().substring(0, 4));
                  }
               }
               catch (IllegalArgumentException e)
               {
                  resource = resourceBundle.getResource("TableEntryForm.dialogmessage.InvalidDateTime");
                  if (resource.equals(""))
                     message = "Invalid Date/Time Input for Field";
                  else
                     message = resource;
                  
                  JOptionPane.showMessageDialog(null, message + " " + columnName
                                                      + ", " + resourceType + ": " + columnType,
                                                      resourceAlert, JOptionPane.ERROR_MESSAGE);
                  validEntry = false;
                  setVisible(true);
                  db_Connection.rollback();
                  prepared_sqlStatement.close();
                  sqlStatement.close();
                  db_Connection.setAutoCommit(true);
                  ConnectionManager.closeConnection(db_Connection, "TableEntryForm addUpdateTableEntry()");
                  return;
               }
            }

            // Interval Type Fields
            else if (columnType.equals("INTERVAL"))
            {
               // Do Nothing, Already set since undefined type.
            }

            // Blob/Bytea/Binary/Raw Type Fields
            else if (isBlobField)
            {
               // Make sure blob button is present
               if (fieldHashMap.get(columnName) != null)
               {
                  // Check to see if data has been put in field.
                  if (blobBytesHashMap.get((JButton) fieldHashMap.get(columnName)) != null)
                  {
                     // Finally check to see if its an update and if so then is
                     // data to be updated or removed.
                     JCheckBox currentRemoveBlobCheckBox = blobRemoveCheckBoxesHashMap.get(columnName);
                     if (currentRemoveBlobCheckBox != null)
                     {
                        if (currentRemoveBlobCheckBox.isSelected())
                           prepared_sqlStatement.setBytes(i++, null);
                        else
                           prepared_sqlStatement.setBytes(i++, (getFormFieldBlob(columnName)));
                     }
                     else
                        prepared_sqlStatement.setBytes(i++, (getFormFieldBlob(columnName)));
                  }
                  else
                     prepared_sqlStatement.setBytes(i++, null);
               }
               else
                  prepared_sqlStatement.setBytes(i++, null);
            }

            // PostgreSQL Geometric fields.
            else if (columnType.equals("POINT") || columnType.equals("LSEG") || columnType.equals("BOX")
                     || columnType.equals("PATH") || columnType.equals("POLYGON")
                     || columnType.equals("CIRCLE"))
            {
               // Do Nothing. Already set since undefined type.
            }

            // Network Type Fields
            else if (columnType.equals("CIDR") || columnType.equals("INET") || columnType.equals("MACADDR"))
            {
               // Do Nothing, Already set since undefined type.
            }

            // Array Type Fields
            else if (isArrayField)
            {
               // Do Nothing, Already set.
            }

            // Boolean Type Fields
            else if (columnClass.indexOf("Boolean") != -1 && columnSize == 1)
            {
               boolean boolean_value;
               if (getFormField(columnName).equals("TRUE"))
                  boolean_value = true;
               else
                  boolean_value = false;
               prepared_sqlStatement.setBoolean(i++, boolean_value);
            }

            // Bit Type Fields
            else if (columnType.indexOf("BIT") != -1 && columnType.indexOf("_") == -1)
            {
               if (subProtocol.equals(ConnectionManager.POSTGRESQL))
               {
                  // Do Nothing. Already set since undefined type.
               }
               else
               {
                  try
                  {
                     String bitString = getFormField(columnName);
                     int int_value = (Integer.valueOf(bitString, 2)).intValue();
                     prepared_sqlStatement.setInt(i++, int_value);

                  }
                  catch (NumberFormatException e)
                  {
                     resource = resourceBundle.getResource("TableEntryForm.dialogmessage.TypeBit");
                     if (resource.equals(""))
                        message = "Type: Bit String";
                     else
                        message = resource;
                     
                     JOptionPane.showMessageDialog(null, resourceInvalidInput + " " + columnName
                                                         + ", " + message, resourceAlert,
                                                         JOptionPane.ERROR_MESSAGE);
                     
                     validEntry = false;
                     setVisible(true);
                     db_Connection.rollback();
                     prepared_sqlStatement.close();
                     sqlStatement.close();
                     db_Connection.setAutoCommit(true);
                     ConnectionManager.closeConnection(db_Connection, "TableEntryForm addUpdateTableEntry()");
                     return;
                  }
               }
            }

            // TEXT/CLOB/ARRAY Type Fields
            else if (isTextField)
            {
               // Make sure text button is present
               if (fieldHashMap.get(columnName) != null)
               {
                  // Check to see if data has been put in field.
                  if (blobBytesHashMap.get((JButton) fieldHashMap.get(columnName)) != null)
                  {
                     // Finally check to see if its an update and if so then is
                     // data to be updated or removed.
                     JCheckBox currentRemoveBlobCheckBox = blobRemoveCheckBoxesHashMap
                           .get(columnName);
                     if (currentRemoveBlobCheckBox != null)
                     {
                        if (currentRemoveBlobCheckBox.isSelected())
                           prepared_sqlStatement.setString(i++, null);
                        else
                           prepared_sqlStatement.setString(i++, (getFormFieldText(columnName)));
                     }
                     else
                        prepared_sqlStatement.setString(i++, (getFormFieldText(columnName)));
                  }
                  else
                     prepared_sqlStatement.setString(i++, null);
               }
               else
                  prepared_sqlStatement.setString(i++, null);
            }

            // BFILE Type Fields
            else if (columnType.equals("BFILE"))
            {
               // Do Nothing, Already set as locator, filename.
            }

            // Standard fall through, should catch generic
            // text table fields.
            else
               prepared_sqlStatement.setString(i++, getFormField(columnName));
         }
         prepared_sqlStatement.executeUpdate();
         db_Connection.commit();

         prepared_sqlStatement.close();
         sqlStatement.close();

         db_Connection.setAutoCommit(true);
         ConnectionManager.closeConnection(db_Connection, "TableEntryForm addUpdateTableEntry()");
         validEntry = true;
      }
      catch (SQLException e)
      {
         ConnectionManager.displaySQLErrors(e, "TableEntryForm addUpdateTableEntry()");
         try
         {
            validEntry = false;
            setVisible(true);
            db_Connection.rollback();
            db_Connection.setAutoCommit(true);
            ConnectionManager.closeConnection(db_Connection,
                                              "TableEntryForm addUpdateTableEntry() rollback");
         }
         catch (SQLException error)
         {
            ConnectionManager.displaySQLErrors(e, "TableEntryForm addUpdateTableEntry() rollback failed");
         }
      }
   }

   //==============================================================
   // Class method for selecting a function operator to be applied
   // to a specific field on add or edit.
   //==============================================================

   private void selectFunctionOperator(Object columnName)
   {
      // Class Instances
      String errorString, currentFunction;
      String functionsFileName = "functions.txt";
      String myjsqlviewFunctionsFileString;
      String resource, title, buttonOK, buttonCancel;
      String message;

      File functionsFile;
      FileReader fileReader;
      BufferedReader bufferedReader;
      Vector<String> functions;

      InputDialog functionSelectDialog;
      JComboBox functionsComboBox;
      ImageIcon functionsPaletteIcon;

      // Setting up to read the user's .myjsqlview home directory
      // functions file. Creating one from the installed files
      // as needed.

      // Select database appropriate functions file.
      if (subProtocol.equals(ConnectionManager.POSTGRESQL))
         functionsFileName = "postgresql_" + functionsFileName;
      else if (subProtocol.indexOf(ConnectionManager.HSQL) != -1)
         functionsFileName = "hsql_" + functionsFileName;
      else if (subProtocol.indexOf(ConnectionManager.ORACLE) != -1)
         functionsFileName = "oracle_" + functionsFileName;
      else if (subProtocol.indexOf(ConnectionManager.SQLITE) != -1)
         functionsFileName = "sqlite_" + functionsFileName;
      else
         functionsFileName = "mysql_" + functionsFileName;

      myjsqlviewFunctionsFileString = MyJSQLView_Utils.getMyJSQLViewDirectory()
                                      + MyJSQLView_Utils.getFileSeparator() + functionsFileName;

      functionsFile = new File(myjsqlviewFunctionsFileString);
      // System.out.println(myjsqlviewFunctionsFileString);

      try
      {
         if (functionsFile.createNewFile())
         {
            // System.out.println("File Does Not Exist, Creating.");
            byte[] fileData = ReadDataFile.mainReadDataString(functionsFileName, false);

            if (fileData != null)
               WriteDataFile.mainWriteDataString(myjsqlviewFunctionsFileString, fileData, false);
            else
            { 
               resource = resourceBundle.getResource("TableEntryForm.dialogmessage.FailedFunction");
               if (resource.equals(""))
                  message = "Failed to Open Sample functions.txt File";
               else
                  message = resource;
               
               errorString = message + "\n";
               JOptionPane.showMessageDialog(null, errorString, resourceAlert, JOptionPane.ERROR_MESSAGE);
               return;
            }
         }

         // Looks like there is a functions file so
         // create a set of elements, functions, to be
         // used to create a vector object.

         fileReader = new FileReader(myjsqlviewFunctionsFileString);
         bufferedReader = new BufferedReader(fileReader);

         functions = new Vector <String>();

         while ((currentFunction = bufferedReader.readLine()) != null)
            functions.addElement(currentFunction);

         bufferedReader.close();
         fileReader.close();
      }
      catch (IOException ioe)
      {
         resource = resourceBundle.getResource("TableEntryForm.dialogmessage.ErrorDirectory");
         if (resource.equals(""))
            message = "Error in creating home directory .myjsqlview funtions file";
         else
            message = resource;
         
         errorString = message + "\n" + ioe;
         JOptionPane.showMessageDialog(null, errorString, resourceAlert, JOptionPane.ERROR_MESSAGE);
         return;
      }

      // Create a dialog with a combobox for allowing
      // the user to select the function operator.

      functionsComboBox = new JComboBox(functions);
      functionsComboBox.setBorder(BorderFactory.createLoweredBevelBorder());
      Object[] content = {functionsComboBox};
      functionsPaletteIcon = new ImageIcon(iconsDirectory + "functionsPaletteIcon.gif");

      resource = resourceBundle.getResource("TableEntryForm.dialogtitle.FunctionSelection");
      if (resource.equals(""))
         title = "Function Selection";
      else
         title = resource;
      resource = resourceBundle.getResource("TableEntryForm.dialogbutton.OK");
      if (resource.equals(""))
         buttonOK = "ok";
      else
         buttonOK = resource;
      resource = resourceBundle.getResource("TableEntryForm.dialogbutton.Cancel");
      if (resource.equals(""))
         buttonCancel = "cancel";
      else
         buttonCancel = resource;
      
      functionSelectDialog = new InputDialog(null, title, buttonOK, buttonCancel, content,
                                             functionsPaletteIcon);

      functionSelectDialog.pack();
      functionSelectDialog.center();
      functionSelectDialog.setResizable(false);
      functionSelectDialog.setVisible(true);

      // If ok proceed to store the fields function to be
      // retrieved later during and add/update. Cancel
      // removes function operator.
      if (functionSelectDialog.isActionResult())
      {
         functionsHashMap.put(columnName, (String) functionsComboBox.getSelectedItem());
         // System.out.println(columnName + " " +
         // functionsComboBox.getSelectedItem());
      }
      else
         functionsHashMap.remove(columnName);

      functionSelectDialog.dispose();
   }

   //==============================================================
   // Class method for selecting a function operator to be applied
   // to a specific field on add or edit.
   //==============================================================

   private String createFunctionSQLStatement(String columnName)
   {
      // Class Method Instances
      StringBuffer sqlStatementString;

      // Collect the function operator.
      sqlStatementString = new StringBuffer();
      sqlStatementString.append((String) functionsHashMap.get(columnName));

      // Get correct form data, TEXT, Blob, or Normal entry.
      // Take into account a possible no argument for the function.

      if (((columnClassHashMap.get(columnName)).indexOf("String") != -1
           && !(columnTypeHashMap.get(columnName)).equals("CHAR")
           && ((columnSizeHashMap.get(columnName)).intValue() > 255))
          || ((columnClassHashMap.get(columnName)).indexOf("String") != -1 && (columnTypeHashMap
                .get(columnName)).equals("LONG"))
          || ((columnTypeHashMap.get(columnName)).indexOf("CLOB") != -1))
      {
         if (getFormFieldText(columnName) == null || getFormFieldText(columnName).length() == 0)
            sqlStatementString.append("(), ");
         else
            sqlStatementString.append("('" + getFormFieldText(columnName) + "'), ");
      }
      // What needs fixed here? What does it mean to apply a function on a
      // binary field. The Java 6.0 API allow a change of binary data to a
      // string
      // of characters, but what operation would be performed? The current 1.4
      // API
      // kept here just returns a pointer to the object. Broken.
      else if (((columnClassHashMap.get(columnName)).indexOf("String") == -1 && (columnTypeHashMap
            .get(columnName)).indexOf("BLOB") != -1)
               || ((columnClassHashMap.get(columnName)).indexOf("BLOB") != -1 && (columnTypeHashMap
                     .get(columnName)).indexOf("BLOB") != -1)
               || ((columnTypeHashMap.get(columnName)).indexOf("BYTEA") != -1)
               || ((columnTypeHashMap.get(columnName)).indexOf("BINARY") != -1)
               || ((columnTypeHashMap.get(columnName)).indexOf("RAW") != -1))
      {
         if (getFormField(columnName) == null || getFormFieldBlob(columnName).length == 0)
            sqlStatementString.append("(), ");
         else
            sqlStatementString.append("('" + Arrays.toString(getFormFieldBlob(columnName)) + "'), ");
      }
      else
      {
         if (getFormField(columnName) == null || getFormField(columnName).length() == 0)
            sqlStatementString.append("(), ");
         else
         {
            // Take into count multiple arguments
            sqlStatementString.append("(");

            String[] argumentString = ((String) getFormField(columnName)).split(",");

            for (int i = 0; i < argumentString.length; i++)
            {
               if (i < (argumentString.length - 1))
                  sqlStatementString.append("'" + argumentString[i] + "',");
               else
                  sqlStatementString.append("'" + argumentString[i] + "'), ");
            }
         }
      }
      return sqlStatementString.toString();
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

   //==============================================================
   // Class method to get the string content of a field
   //==============================================================

   protected String getFormField(String columnName)
   {
      if (fieldHashMap.get(columnName) != null)
      {
         if (columnEnumHashMap.containsKey(columnName))
            return (String) ((JComboBox) fieldHashMap.get(columnName)).getSelectedItem();
         else
            return ((JTextField) fieldHashMap.get(columnName)).getText();
      }
      else
         return "";
   }

   //==============================================================
   // Class method to get content into a selected JButton
   // hashmap as bytes.
   //==============================================================

   protected byte[] getFormFieldBlob(Object itemName)
   {
      return (byte[]) blobBytesHashMap.get((JButton) fieldHashMap.get(itemName));
   }

   //==============================================================
   // Class method to get content into a selected JButton hashmap
   // as a string.
   //==============================================================

   protected String getFormFieldText(Object itemName)
   {
      return (String) blobBytesHashMap.get((JButton) fieldHashMap.get(itemName));
   }

   //==============================================================
   // Class method to place string content into a selected
   // TextField.
   //==============================================================

   protected void setFormField(Object columnName, Object content)
   {
      // Method Instances.
      String columnClass, columnType;

      if (fieldHashMap.get(columnName) != null)
      {
         columnClass = columnClassHashMap.get(columnName);
         columnType = columnTypeHashMap.get(columnName);

         // Blob/Bytea Button
         if ((columnClass.indexOf("String") == -1 && columnType.indexOf("BLOB") != -1)
             || (columnClass.indexOf("BLOB") != -1 && columnType.indexOf("BLOB") != -1)
             || (columnType.indexOf("BYTEA") != -1) || (columnType.indexOf("BINARY") != -1)
             || (columnType.indexOf("RAW") != -1) || (columnType.indexOf("CLOB") != -1))
            ((JButton) fieldHashMap.get(columnName)).setText((String) content);

         // Text Button
         else if ((columnClass.indexOf("String") != -1 && !columnType.equals("CHAR")
                   && (columnSizeHashMap.get(columnName)).intValue() > 255)
                  || (columnClass.indexOf("String") != -1 && columnType.equals("LONG")))
            ((JButton) fieldHashMap.get(columnName)).setText((String) content);

         // Array Button
         else if ((columnClass.indexOf("Array") != -1 || columnClass.indexOf("Object") != -1)
                  && columnType.indexOf("_") != -1)
            ((JButton) fieldHashMap.get(columnName)).setText((String) content);

         // Standard TextField
         else
         {
            if (columnType.equals("BFILE") && !addItem)
            {
               // Do nothing for edits not supported.
            }
            else
            {
               ((JTextField) fieldHashMap.get(columnName)).setText((String) content);
               ((JTextField) fieldHashMap.get(columnName)).setCaretPosition(0);
            }
         }
      }
   }

   //==============================================================
   // Class method to place string content into a selected
   // JComboBox.
   //==============================================================

   protected void setFormField(Object columnName, String data)
   {
      ((JComboBox) fieldHashMap.get(columnName)).addItem(data);
      ((JComboBox) fieldHashMap.get(columnName)).setSelectedItem(data);
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
   // Class method to place content into a selected JButton hashmap
   // as a string.
   //==============================================================

   protected void setFormFieldText(Object itemName, String content)
   {
      blobBytesHashMap.put((JButton) fieldHashMap.get(itemName), content);
   }

   //==============================================================
   // Class method to place string content into a selected
   // JComboBox.
   //==============================================================

   protected void setComboBoxField(Object columnName, Vector<String> content, Object data)
   {
      Iterator<String> contentsIterator = content.iterator();
      
      while (contentsIterator.hasNext())
         ((JComboBox) fieldHashMap.get(columnName)).addItem(contentsIterator.next());

      if (data == null)
         ((JComboBox) fieldHashMap.get(columnName)).setSelectedIndex(0);
      else
         ((JComboBox) fieldHashMap.get(columnName)).setSelectedItem(data);
   }

   //==============================================================
   // Class method to place string content into a selected Set
   // type fields and fill a vector with the complete set contents.
   //==============================================================

   protected void setSetFields(Object columnName, Vector<String> content, Object data)
   {
      // Method Instances.
      Vector<String> setFields = new Vector <String>();
      
      Iterator<String> contentsIterator = content.iterator();

      while (contentsIterator.hasNext())
         setFields.add(contentsIterator.next());
      setFieldsHashMap.put(columnName, setFields);

      if (data != null)
         ((JTextField) fieldHashMap.get(columnName)).setText((String) data);
   }
}