//=================================================================
//            MyJSQLView TableTabPanel_MySQL
//=================================================================
//
//    This class provides the table summary view of data in
// a MySQL database that in MyJSQLView is listed according to
// a specified sort and search. Entries from the database table
// may be viewed, added, edited, or deleted by means of this
// panel. The panel also provides the mechanism to page through
// the database table's data.
//
//            << TableTabPanel_MySQL.java >>
//
//=================================================================
// Copyright (C) 2005-2012 Dana M. Proctor
// Version 11.54 08/11/2012
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
// Version 1.0 Original TableTabPanel Class.
//         1.1 SQLExceptions Comment Changes.
//         1.2 Temporary Modifications to setColumnNamesHeadings
//             Method.
//         1.3 Corrections to ViewForm Display in Center Panel
//             in ActionPerformed Method.
//         1.4 Need to Derive Primary Key for Deletions.
//         1.5 sqlTable Instance Passed via Constructor.
//         1.6 TableForm Object Constructor sqlTable Object.
//         1.7 Removed All Static Instances.
//         1.8 Class Instance searchText Changed searchTextField.
//         1.9 PrimaryKey Location Column Verification on View/Edit.
//         2.0 Class Method addItem() Examples Outline of ComboBox,
//             Date, and Time Data.
//         2.1 Class Method viewSelectedItem() sql Statement Correction.
//         2.2 Layout Set to Null in searchPanel, & Components setBounds().
//         2.3 Class TableForm Constructor Removed fields, Added
//             columnTypeHashMap Argument.
//         2.4 TableForm Sizing Based on the Number of Fields in the
//             Table.
//         2.5 Comment Updates and Various Other Cleaning.
//         2.6 Class Method deleteSelectedItem Set to Transactional Type
//             Database Transactions.
//         2.7 Class Method deleteButton Action Test of Null id.
//         2.8 Class Instance columnClassHashMap.
//         2.9 Class Methods addItem & editSelectedItem Proper Filling
//             of Data to Form.
//         3.0 Class Method viewSelectedItem Date Formating.
//         3.1 Implemented Enum & Set JComboBox Fields.
//         3.2 Class Method getListTable().
//         3.3 Class Method getColumnNamesHashMap().
//         3.4 TimeStamp Data Type Change in ViewSelectedItem 
//             Class Method.
//         3.5 Removed Unused Local Instances.
//         3.6 Instances previousViewButton and nextViewButton &
//             Implementation to View Form.
//         3.7 Modifications to Sort/Search Panels to Gridbag Layout.
//         3.8 Sort and Searchs Revert Center Panel to Summary Table.
//         3.9 Removed Reference to UserSetupPreferences.
//         4.0 Added Class Method setTableFields().
//         4.1 Removed Boolean Argument newData from Class Method
//             loadTable();
//         4.2 Added PopupListener to addForm.
//         4.3 Modified Table Summary Data and ViewTableForm Fields
//             of Type Blob to Display Blob and Byte Count.
//         4.4 TableViewForm Class Constructor Argument Change,
//             columnTypeHashMap & Same Class Method Name Changed
//             to setFormField & getFormField.
//         4.5 TableViewForm Class Method viewSelectedItem() adding
//             Setting of Data for Blob Type Fields. Allows Saving
//             of Blob.
//         4.6 TableForm Class Method Method Name Changed to
//             setFormField & getFormField.
//         4.7 Class Method editSelectedItem() Blob Type Field
//             Setting of JButton Text in editForm.
//         4.8 Added Class Method getTableHeadings().
//         4.9 Completed Implementation of Selecting Table
//             Summary Columns, Edit|Preferences|Table Fields.
//         5.0 Connector-Java-3.1.13 Fails to Return Class Type
//             Object Like Prior Versions for Blobs. Temp. Fix
//             in loadTable() Class Method.
//         5.1 Class Method buildConstraints().
//         5.2 TableForm Class Method Name Change to setComboBoxField().
//         5.3 Class Method displayMyDateString().
//         5.4 Class Methods parseColumnNameString() &
//             firstLetterToUpperCase().
//         5.5 Blob Type Distinction in Class Method viewSelectedItem()
//             and editSelectedItem().
//         5.6 Class Method loadTable(), TIMESTAMP, Fields Removed Formatting.
//         5.7 Class Method loadTable(), DATE, Fields Removed Formating.
//         5.8 Corrected All Query Statement Where Proper Escaping was Not
//             Implemented.
//         5.9 Class Methods loadTable(), viewSelectedItem(), and 
//             editSelectedItem() TIMESTAMP Formating.
//         6.0 Class Method addItem() Setting of Year Fields Values to
//             Either YY or YYYY By columnSize Value.
//         6.1 Class Methods loadTable(), & viewSelectedItem() Check
//             for NULL Entries, and Complete Redo of DATE, DATETIME,
//             and TIMESTAMP Fields.
//         6.2 Class Method displayMyDateString() Removed and Check
//             for NULL Entries, and Complete Redo of DATE, DATETIME,
//             and TIMESTAMP Fields in Class Method editSelectedItem().
//         6.3 References to TableForm Changed to TableEntryForm Class.
//         6.4 Instance refreshButton and Display of rows Indicator
//             Position.
//         6.5 Corrected YEAR Field Display in Class Method viewSelectedItem().
//         6.6 Added Argument columnSizeHashMap to Calls to TableEntryForm.
//         6.7 Temporary Fix for Tables for No Primary Key. Revert by to
//             Original Routines for Date, and DateTime Fields in loadTable(),
//             viewSelectedItem(), and editSelectedItem() Class Methods.
//         6.8 Class Method displayMyDateString() Back.
//         6.9 Class Instance autoIncrement Passed to Constructor for
//             TableEntryForm().
//         7.0 Class Method actionPerformed() Next & Previous Actions
//             Switches Back to Table View.
//         7.1 Comment Changes.
//         7.2 Modified Delete Dialog Frame in Class Method deleteSelectedItems().
//         7.3 Added Class Methods getColumnClassHashMap() & getColumnTypeHashMap(). 
//         7.4 Added Class Instance preferredColumnSizeHashMap to Size
//             Summary Table Columns.
//         7.5 Added Comments In Effect to Enable a Table Sorter.
//         7.6 Added Class Instance maxPreferredColumnSize;
//         7.7 Class Method setTableFields() Modified to Use preferredColumnsizeHashMap.
//         7.8 Created Class Methods getTableRowSize() & setTableRowSize().
//         7.9 Correction to previousButton Action to Insure tableRowStart
//             Is Not Less Than Zero.
//         8.0 Fixed Class Method setTableRowSize() to Use the Existing Summary
//             Table Headings Not Table Fields.
//         8.1 Modifications to Properly Implement Table Key(s) Behavior.
//         8.1 Continueing Key(s) Behavior Modifications, Auto-Increment.
//         8.2 Class Method getPrimaryKeys().
//         8.3 Commented TableSorter. Adjusted Column Sizes After Edits/Adds In
//             Actions for dummy DisposeButton. Reset Copy to listTable in Class
//             Method setTablesFields().
//         8.4 Class Instance keyLengthHashMap, Filling Same During loadTable().
//         8.5 Implmented for the Case of a Blob Type Being the Key for the
//             Table. Will Handle Text, But Not Binary Files Like Images.
//         8.6 Check for keyLength Not NULL and Cleanup Some of the Blob Key
//             Code in loadTable().
//         8.7 Correction to 2-Digit Year Form Filling in Class Methods
//             viewSelectedItem() & editSelectedItem().
//         8.8 Corrected YY Field to Properly Display Value in ListTable.
//         8.9 Added Class Method getTableRowStart() & getTableRowLimit().
//         9.0 Class Instances columnEnumHashMap & columnSetHashMap. Accomodations
//             to Separate Enum & Set So That The TableEntryForm May Properly
//             Handle Set Fields. New Popup List Frame for the Set Fields.
//         9.1 Minor Correction in Class Method setSpecialFieldData() for Set
//             Fields.
//         9.2 Sort Button Mnemonic(KeyEvent.VK_S). KeyEvent Listener For the
//             searchTextField and VK_ENTER. Instances previousViewButton &
//             nextViewButton Mnemonics VK_Z & VK_X.
//         9.3 Implemented Text, MediumText, and LongText Fields to Be Setup
//             Similar to Blob Data. Appreviate, Identifier in Summary Table.
//         9.4 InputDialog String Argument "cancel".
//         9.5 Modified the Way TEXT, MEDIUMTEXT, & LONGTEXT Fields Are Handled
//             in the ClassMethods viewSelectedItem() & editSelectedItem().
//             Added Field Identifier for BLOB and TEXT Instead of Just Browse
//             in Class Method addItem().
//         9.6 Added Class Method getColumnSizeHashMap(). Class Method loadTable().
//             Correction in Recognition of TEXT, MEDIUMTEXT, & LONGTEXT Field
//             for Filling in Summary Table Data.
//         9.7 Cleaned Up Some & Changes in Comments.
//         9.8 Removed TEXT Fields From Being Handled the Same as Blobs in Class
//             Methods loadTable() & viewSelectedItem. Left Add/Edit TEXT Alone.
//         9.9 Comment Changes for TEXT Fields.
//        10.0 Set tableScrollPane Vertical to Zero After previousButton, nextButton,
//             sort, search, and refresh Actions.
//       10.10 Limited the Size of the Frames for the addForm and editForm in
//             Class Methods addItem() and editItem() to Max. 600 Height.
//       10.11 Began the Initial Advanced Sort/Search Interface. Basic Activation
//             Handled and Needed Components.
//       10.12 Completed Initial Implementation of Advanced Sort/Search Interface.
//             Added Class Instance advancedSortSearch and Handling in Class Method
//             loadTable().
//       10.13 Added Class Instance columnNamesHashMap to Arguments to Class
//             Instance advancedSortSearchFrame. Also Changed Rev. Numbering.
//             Set rowLabel Back to 0 on Search in Class Method actionPerformed().
//       10.14 Minor Comment Changes.
//       10.15 Class Instance tableScrollPane Set Corner Icon in Class Method
//             setTableFields() After Row Size or Column Updates.
//       10.16 Code Cleanup. Removed Unused Class Method setColumnNamesHeadings().
//       10.17 Class Method viewSelectedItem() & editSelectItem() Modified the
//             Check from NULL to Length Check to Properly Fill Button to Either
//             byte Count or 0.
//       10.18 Code Cleanup. Class Method editSelectedItem() Set Argument in
//             setFormField() to String Required, NOT new String().
//       10.19 Yea Already Found a Introduced Bug from Code Cleanup with BugFinder.
//             Look Carefully Before You Follow These Tools Suggestions. Class
//             Method deleteSelectedItem() Return Multiple Delete Rows.
//             sqlStatementString Returned to "DELETE FROM " + sqlTable + " WHERE "
//             at End of For. Sometimes Though it Looks Wrong, a Lot of Thought
//             Probably Went Into Composing the Code a Certain Way.
//       10.20 Removed Local Instance currentColumnName in Class Method
//             deleteSelectedItems(). Removed Local Instance columnHeadingSize in
//             Class Method setTableFields(). Final Class Instance
//             maxPreferredColumnSize Made Static.
//       10.21 Corrected Edit in Text & Blob Fields When the Data is 0 Bytes, Set
//             As Such Instead of NULL. Class Method editSelectedItem().
//       10.22 Returned TEXT Fields To Be Viewed As Buttons in the TableViewForm.
//             Class Method viewSelectedItem(),
//             columnSizeHashMap.get(currentColumnName)).intValue() > 255.
//       10.23 Used System.getProperty("file.separator") for All File System
//             Resources Accesses Through Instance fileSeparator.
//       10.24 Class Instance tableScrollPane.setCorner(ScrollPaneConstants.UPPER_RIGHT_CORNER,
//             activateAdvancedSortSearchButton).
//       10.25 Renamed to TableTabPanel_MySQL.
//       10.26 Minor Changes to SELECT Statements fo Quoting Column Names. Also
//             LIMIT Change to LIMIT XX OFFSET XX.
//       10.27 Cleaned Up Javadoc Comments.
//       10.28 Removed Reference of Commented Out TableSorter Class in Constructor.
//       10.29 Added Comment to Code for addForm Height Sizing in Class Method
//             addItem().
//       10.30 Backed Out Quotes from 10.26.
//       10.31 Comments in Class Method loadTable() About the Fall Through of
//             Placing an Object in the JTable Cell.
//       10.32 Added BOOLEAN & BIT Data Type Handling in Class Method loadTable(), 
//             viewSelectedItem(), & editSelectedItem().
//       10.33 Changed Class Method String autoIncrement to HashMap
//             autoIncrementHashMap. Changed the Way auto-increment,
//             Sequenced, SERIAL Fields Are Handled in Class Methods
//             getColumnNames(), addItem(), & editItem(). Changed the
//             Class Method getAutoIncrement() to getAutoIncrementHashMap().
//       10.34 Implemented the Boolean Field Single Bit As a Component of
//             a Enumeration, columnEnumHashMap.
//       10.35 Reviewed/Commented and Cleaned Up. Changed maxPreferredColumnSize
//             Value.
//       10.36 Class Method Corrected Setting of editForm.setFormField().
//             Should Have Been setFormField(ColumnName, Object). Set Content
//             in TableEntryForm to "NULL" if Content is NUll in Class
//             Method editSelectedItem().
//       10.37 Class Method getColumnNames() columnType.toUpperCase() Before
//             Storing in HashMap. Boolean Operator Changes.
//       10.38 Cleaned Out Some More By Insuring HashMaps columnClass, columnType,
//             and columnSize Are Accessed Once for Each Method by Assigning
//             to Class Method Instances.
//       10.39 Header Update.
//       10.40 Added/Implemented identiferQuoteString.
//       10.41 Modified the Way keyLengthHashMap Values Are Added. Minor
//             Code Changes in primaryKeys Creation. All in Class Method
//             getColumnNames().
//       10.42 Changed the Way BIT Field Types Are Handled in Class Methods
//             loadTable(), viewSelectedItem(), & editSelectedItem(). For
//             Test Data Types BIT(5) 11110 Is NOT Returned as a Proper
//             INT. So Changed to getString() and Check Integer Conversion.
//             Set Summary Table Back to Main View When Viewing Item and a Delete
//             or Refresh Action Occurs.
//       10.43 Removed the Creation of Action Buttons, View, Edit, Add, and
//             Delete if No Primary Key is Present for the Table/View. This
//             Was A last Minute Inclusion For Version 2.81 To Allow Views.
//       10.44 Instance mysqlStatement Changed to sqlStatement in Class
//             Methods getColumnNames(), viewSelecteditem(), & setSpecialFieldData().
//       10.45 Moved Class Instances and All Non-Essential Class
//             Methods to Parent, TableTabPanel.
//       10.46 Removed doneButton Replaced With closeViewButton. Revamped Actions
//             Buttons Panel. Changed record to row.
//       10.47 Minor Format Changes. Removed KeyListener Methods.
//       10.48 Class Method setTableFields() Renamed to setTableHeadings().
//             Stored Actual Database Column Nmaes in fields Vector.
//       10.49 Class Instance tableHeadings Changed to currentTableHeadings.
//             Added Class Instance allTableHeadings.
//       10.50 Reduced Text Data Type Field Size Storage in Summary Table
//             from 65535 to 512 Characters in Class Method loadTable().
//       10.51 Fixed What 10.50 Broke. Incremented j++ When Should NOT.
//             ALWAYS TEST EVEN WHEN CHANGE SEEMS MINOR!
//       10.52 Commented Out a System.out.println().
//       10.53 Removed the trim() From the Default Content Placed in the
//             listTable in Class Method loadTable(). This causes Problems
//             With Keys that Have Leading/Trailing Spaces.
//       10.54 Escaped Key Strings in Class Methods viewSelectedItem(), 
//             editSelectedItem(), & deleteSelectedItem().
//       10.55 Correction for Induced Problem With Prior Fix for Escape
//             Key Strings.
//       10.56 Class Method loadTable() Instance preferredColumnSize Determined
//             by A trim() of the Contents of Table. Compensation for Character
//             Fields That Pad With Spaces.
//       10.57 Class Method getColumnNames() and loadTable() Changed to Return
//             Type Boolean. Check at Calling of These Methods So That TableTabPanel
//             Can Allow the MyJSQLView Application to Exit Instead of Crash If
//             Things Go Astray.
//       10.58 Class Method loadTable() Single Quotes Placed Around WHERE Condition
//             "1" on NULL.
//       10.59 Implemented a Fully Qualified Table Name That Includes the Schema
//             and Table Name. Class Instance schemaTableName.
//       10.60 Check In getColumnNames() to Insure sqlTable Does Include Schema
//             Before Parsing.
//       10.61 Class Method getColumnNames() Replaced sqlTable With schemaTableName.
//       10.62 Added Class Instance serialVersionUID. Moved schemaTableName to
//             Parent.
//       10.63 Established the Control of the Summary Table Fields List Through
//             sqlTableFieldsString. Class Methods Effected getColumnNames(),
//             loadTable(), & setTableHeadings().
//       10.64 Added sqlTableStatement Instance in Class Method
//             loadTable().
//       10.65 Fault Tolerance Test in Class Method getColumnNames() for Class
//             & Type Columns Definitions That May be NULL. Thanks Oracle Sweet.
//       10.66 Class Instance searchTextField Cleared on No Field Selection in
//             searchComboBox. Constructor and actionPerformed() Method Changes.
//       10.67 On searchTextField Clearing Fired searchButton in Class Method
//             actionPerformed().
//       10.68 Removed sortButton and Its Action and Replaced With the
//             sortCombobBox.
//       10.69 Search Panel Redesign. Search Button Text Replaced With an Icon.
//       10.70 Fixed Bug of During Delete of Selecting a Empty Row Throws a
//             NULL Pointer. Class Method deleteSelectedItem().
//       10.71 Added clearSearchTextFieldButton and Its Actions.
//       10.72 Implemented Search Text Field Saving via Class Instance
//             columnsSearchString.
//       10.73 Removed Loading of ImageIcon for the Various Buttons to Parent
//             TableTabPanel. Class Instance previous/nextButton Changed
//             to previous/nextTableRowsButton.
//       10.74 Implemeted Basic ASC/DESC Sorts Through Instances ascSortRadioButton,
//             descSortRadioButton, and ascDescString. Class Instances Effected
//             Constructor, actionPerformed(), & loadTable().
//       10.75 Class Method loadTable() Added Instance searchQueryString, & Used
//             to Defined the Way the WHERE Columns LIKE Aspect of the Query is
//             Defined. Essentially All Columns Search for Key Phase. Removed
//             Clearing Action of searchTextField When searchComboBox Set None.
//       10.76 Removed Class Instance columnsSearchStrings and Its Actions.
//       10.77 Popup MenuItem for listTable. Actions in actionPerformed() & Reseting
//             in setTableHeadings().
//       10.78 Applied MyJSQLView Standard Code Reformatting. Changed the Way the
//             Popup Menu is Created for the listTable. Added Class Methods menuItem()
//             & createListTablePopupMenu().
//       10.79 Minor Format Changes.
//       10.80 Added Conditional Check for NULL Connection in Class Methods 
//             actionPerformed() & setTableHeadings().
//       10.81 Class Method createListTablePopupMenu() Only Allowed View, Add, Edit,
//             & Delete Popup Item for Tables That Can be Modified via a Key.
//       10.82 Class Method setTableHeadings() Check for No sqlTableFields.
//       10.83 Class Methods viewSelectedItem(), editSelectedItem(), &
//             deletedSelectedItems() Modified So a Date Key is Properly Converted
//             Back to the Database Format for Processing. Cleaned Up Code for Key
//             Processing in Same Methods.
//       10.84 Class Method actionPerformed() Changed Component Type Conditional
//             From if to if/else if.
//       10.85 Added Class Method executeActions(). Also actionThread in Class
//             Method actionPerformed() to Handle Events.
//       10.86 Class Method setTableHeadings() Corrections to Connection Information
//             String.
//       10.87 Moved Processing of dummyButton From Thread in Class Method
//             executeActions() to Method actionPerformed().
//       10.88 Class Method actionPerformed() Provided a String Name for the
//             Thread actionThread.
//       10.89 Provided a Conformalization of DATE Field Types By Obtaining Data
//             From Database via the getDate() Method. Class Methods Effected
//             loadTable(), viewSelectedItem(), & editSelectedItem().
//       10.90 DATETIME Data Fields Obtained Through a getTimestamp() Method.
//             Class Methods loadTable(), viewSelectedItem(), & editSelectedItem().
//       10.91 Cleared the TableViewForm's Blob Byte HashMash in the Class Method
//             executeActions() via clearBlobBytesHashMap() on next/previousViewButton.
//       10.92 Moved the advancedSortSearchFrame Creation Over to the New Interface
//             Method createAdvancedSortSearchFrame() So That it Could Be Accessed
//             During a setState().
//       10.93 Class Instance tableScrollPane Set the VerticalScrollPolicy to Always.
//             Class Contructor & Method setTableHeadings().
//       10.94 Removed Reference to Class Instance settingState.
//       10.95 Class Instance settingState Back.
//       10.96 Exclusion of Blob Fields From Initially Loaded Through currentTableFields
//             in Class Method getColumnNames().
//       10.97 Included Class Instance sqlTableFieldsString in Exclusion of Blob Fields
//             in Class Method getColumnNames(). TEMPORARY PERFORMANCE ISSUE SOLUTION.
//       10.98 Class Method actionPerformed() Changed the Handling of closeViewButton
//             & searchComboBox Actions Since They Do Not Need Connection Processing.
//             Removed Them From executeActions().
//       10.99 TableEntryForm Constructor Change to Pass Arguments selectedRow, and this
//             TableTabPanel. Added Argument selectedRow int to Class Method editSelectedItem().
//       11.00 Implemented a Saving/Restoring of the Current selectedRow, & tableScrollPane
//             ScrollBars. Class Methods Effected actionPerformed(), executeActions(),
//             viewSelectedItem(), and editSelectedItem(). Changed selectedRow Argument
//             in editSelectedItem() to rowToEdit, & Added Argument rowToView to
//             viewSelectedItem().
//       11.01 Moved activateAdvancedSortSearchButton Action Processing From executeActions()
//             Method Back to actionPerformed() Because it Takes No Initial Connection
//             Creation.
//       11.02 Implemented Update Feature. Activation via LOWER_RIGHT_CORNER of the
//             tableScrollPane. Added Class Method createUpdateFrame(). Class Methods
//             Effected Constructor(), actionPerformed(), executeActions(), setTableHeadings().
//             Added sqlTable to TableEntryForm Title.
//       11.03 Implemented Delete All Items Feature. Class Instance deletAllButton Added
//             Only As a Place Holder. Selection via summaryTablePopupMenu. Class Methods
//             Effected Constructor(), createListTablePopupMenu(), actionPerformed(), &
//             executeActions(). Added Class Method deleteAllItems().
//       11.04 Added deleteDataIcon to Dialog in Class Methods deleteSelectedItems()
//             and deleteAllItems().
//       11.05 Implemented a Copy/Paste Popup Menu Addition to the listTable. Implemented
//             the Copy Feature, But Just Provided the Outline for the Paste. Class Methods
//             Effected Constructor(), actionPerformed(), createListTablePopupMenu(), and
//             setTableheadings().
//       11.06 Implemented the Basic Paste Feature for the listTable, in actionPerformed().
//             Added Class Method pasteClipboardContents().
//       11.07 Minor Comment Changes.
//       11.08 Class Method pasteClipboardContents() Modified tableHeadingsString to be
//             Derived Directly From the listTable, Columns Therefore Moveable.
//       11.09 Added confirmCheckbox and Font Changes to message in Classs Method
//             deleteAllItems().
//       11.10 Class Method pasteClipboardContents() Changed the Method Used to Obtain
//             the Clipboard Contents, Checked for NULL, and Catch for IllegalStateException.
//       11.11 Class Method createUpdateFrame() Arguments columnClass/Type/SizeHashMap.
//       11.12 Class Method createListTablePopupMenu() Paste Excluded if No Keys Found.
//       11.13 Integration of TableTabPanels. Moved All Common Methods to the TableTabPanel.
//       11.14 Class Method loadTable() Changed searchQueryString From String to StringBuffer.
//       11.15 Changed Package to Reflect Dandy Made Productions Code.
//       11.16 Correction in Class Method getColumnNames() To Handle Possible Event of No
//             Fields in the Table. Detected Because 6.2 Exclused Blob and key_tables Has
//             table key_table3 With One Field and It is Blob.
//       11.17 Class Methods viewSelectedItem() and editSelectedItem() Changed Method Instance
//             sqlStatementString to a StringBuffer.
//       11.18 Class Methods viewSelectedItem() & editSelectedItem(), Removed the Formatting
//             for Date Keys Form STR_TO_DATE() to the MyJSQLView_Utils.formatJavaDateString().
//             Consolidation for Later Date Displays Controlled by User.
//       11.19 Removed the Exclusion of Blob Types in Initial Loading, Class Method
//             getColumnNames().
//       11.20 Implemented Fix For the Performance Issued That was Temporary Corrected in
//             10.96 & 10.97. Permanently Excluded Any Loading of LOBs in the Summary Table,
//             Unless They Are Identified As Keys. Class Methods Effected getColumnNames()
//             & loadTable(). Added Instance lobLessSQLStatementString to Class Method
//             loadTable().
//       11.21 Minor Format and Comment Changes. Correction to Conditional in Class Method
//             getColumnNames() For Collecting LOBs.
//       11.22 Class Method getColumnNames() Instance columnType in Some Cases Converted to
//             UpperCase for Comparisons.
//       11.23 Class Method getColumnNames() Changed Instance columnSize From Type Object
//             to Integer. Class Method loadTable() Changed Instance keyLength From String
//             to Integer. Removed Unecessary Casts for the HashMaps.
//       11.24 Class Methods getColumnNames() and loadTable() Assigned Integer via valueOf()
//             Instead of new Integer for Instances columnSize & preferredColumnSizeHashMap.
//       11.25 Parameterized headings in Method loadTable(), keyIterator & textFieldIterator
//             in viewSelectedItem() & editSelectedItem(), and textFieldNamesIterator in
//             addItem().
//       11.26 Made the Class and Constructor Public.
//       11.27 Implemented a View Only Table, via Constructor Argument viewOnlyTable.
//       11.28 Assigned searchQueryString to sqlTableSearchString in loadTable().
//       11.29 Undid Last Revision. Short Sighted.
//       11.30 Check for All Fields Possibly LOBs. Class Method loadTable().
//       11.31 Class Method getColumnNames() Cleaned Up the Check Output for primaryKeys.
//       11.32 Class Methods loadTable(), viewSelectedItem(), addItem() & editSelectedItem()
//             Changed Default Entry for Date/DateTime/TimeStamp Type Entry to
//             GeneralProperties.getDateViewFormat(). Class Methods view/editSelectedItem()
//             Change for Date Key Conversion to MyJSQLView_utils.convertViewDateString_To_
//             DBDateString().
//       11.33 Class Method loadTable() Changes to Give the Ability to Properly Search
//             Given Input for Date/DateTime/Timestamp Fields.
//       11.34 Changes to Class Methods getColumnNames(), loadTable(), viewSelectedItem(),
//             & deleteSelectedItem() to Used Newly Redefined ConnectionManager to Display
//             SQL Errors.
//       11.35 Commented Out System.out in Class Method viewSelectedItem().
//       11.36 Corrections in Method loadTable to Properly Build searchTextString For Date,
//             DateTime, & Timestamp When No Specific Column is Selected.
//       11.37 Called saveHistory() in Class Method loadTable().
//       11.38 Changed the Conditional Check for saveAction by Removing the NOT Logic.
//       11.39 Correction in loadTable() for Not Modifiying searchTextString
//             During Composition When No Field Specified.
//       11.40 Set sqlTableStatement to an Empty String Prior to Each New
//             Assignment in Class Method loadTable().
//       11.41 Set sqlTableStatement to NULL and Then Created a new String() for it of
//             sqlStatementString to Insure it is Flushed in Class Method loadTable().
//       11.42 Set sqlTableStatement to Not be a new String() of sqlStatementString in
//             Method loadTable().
//       11.43 Removed Method Instance sqlStatementString & Replaced With Parent
//             Class Instance sqlTableStatement.
//       11.44 Class Method addItem() Added a try catch for setSpecialFields(). Methods
//             viewSelectedItem(), editSelectedItem() & getColumnNames() Throws for
//             SQLException Through finally Clause for Closing sqlStatment.
//       11.45 Changes in loadTable to Add Back Instance sqlStatementString and Then
//             Have sqlTableStatement New StringBuffer Designation Loaded From it.
//       11.46 Method loadTable() Conversion of Date From searchString Failed, Due
//             to Possible Generic Search of All Fields for Given Characters. So
//             Just Use Original Characters.
//       11.47 Removed sqlStatementString in loadTable(), Replaced With sqlTableStatement.
//       11.48 Class Method loadTable() Insured That sqlTableStatement is Given a
//             New Instance Before Reasignment. Replaced delete() Which Was Not
//             Propagating Changes to Object Outside the Method.
//       11.49 Change in viewSelectedItem() to Use Brute Force WHERE Creation for
//             Selected listTable Entry if primaryKeys().isEmpty().
//       11.50 Change in Class Method getColumnNames() of Adding Items to New ArrayList
//             Instances by Way of add() Instead of addElement().
//       11.51 Change in getColumnNames() to Always Make a Check for Indexes.
//       11.52 Class Method loadTable() Changed lobLessSQLStatementString to StringBuffer
//             & Chopped String Off Name.
//       11.53 Closure for db_resultSet in editSelectedItem(), viewSelectedItem() &
//             getColumnNames() Moved to finally.
//       11.54 Closure for rs & sqlStatement in loadTable() Moved to finally.
//        
//-----------------------------------------------------------------
//                danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Iterator;

/**
 *    The TableTabPanel_MySQL class provides the table summary view of data in a
 * MySQL database that in MyJSQLView is listed according to a specified sort and
 * search. Entries from the database table may be viewed, added, edited, or
 * deleted by means of this panel. The panel also provides the mechanism to page
 * through the database table's data.
 * 
 * @author Dana M. Proctor
 * @version 11.54 08/11/2012
 */

public class TableTabPanel_MySQL extends TableTabPanel
{
   // Class Instances.
   private static final long serialVersionUID = -3305846942741359241L;

   //==============================================================
   // TableTabPanel Constructor
   //==============================================================

   public TableTabPanel_MySQL(String table, Connection setup_dbConnection, boolean viewOnlyTable)
   {
      super(table, setup_dbConnection, viewOnlyTable);
   }

   //==============================================================
   // Class method to obtain the column names from the table. The
   // names are modified for display and placed into a map for
   // later use. Additional information about the column, size,
   // type, etc., are also stored away for future use.
   //==============================================================

   public boolean getColumnNames(Connection dbConnection) throws SQLException
   {
      // Method Instances
      String sqlStatementString;
      Statement sqlStatement;
      ResultSet db_resultSet;
      ResultSetMetaData tableMetaData;

      String colNameString, comboBoxNameString;
      String columnClass, columnType, columnKey, columnExtra;
      Integer columnSize;

      // Connecting to the data base, to obtain
      // meta data, and column names.
      
      sqlStatement = null;
      db_resultSet = null;
      
      try
      {
         sqlStatement = dbConnection.createStatement();

         // ====================================================
         // Setting Up the Column Names, Form Fields, ComboBox
         // Text, Hashmaps, Special Fields, & Primary Key(s).

         sqlStatementString = "SELECT * FROM " + schemaTableName + " LIMIT 1";
         // System.out.println(sqlStatementString);

         db_resultSet = sqlStatement.executeQuery(sqlStatementString);
         tableMetaData = db_resultSet.getMetaData();
         db_resultSet.close();

         // Primary key(s) & special fields.

         sqlStatementString = "DESCRIBE " + schemaTableName;
         db_resultSet = sqlStatement.executeQuery(sqlStatementString);

         while (db_resultSet.next())
         {
            colNameString = db_resultSet.getString("Field");
            columnType = db_resultSet.getString("Type");
            columnKey = db_resultSet.getString("Key");
            columnExtra = db_resultSet.getString("Extra");
            //System.out.println(colNameString + " " + columnType + " " +
            //columnKey + " " + columnExtra);

            if (columnKey.indexOf("PRI") != -1)
               primaryKeys.add(colNameString);

            // Should be only one auto_increment column per table.
            if (columnExtra.indexOf("auto_increment") != -1)
               autoIncrementHashMap.put(parseColumnNameField(colNameString), colNameString);

            // Boolean fields are set as enum.
            if (columnType.indexOf("enum") != -1 || columnType.equals("tinyint(1)"))
               columnEnumHashMap.put(parseColumnNameField(colNameString), columnType);

            if (columnType.indexOf("set") != -1)
               columnSetHashMap.put(parseColumnNameField(colNameString), columnType);
         }
         db_resultSet.close();

         // Additional Indexes

         sqlStatementString = "SHOW INDEX FROM " + schemaTableName;
         db_resultSet = sqlStatement.executeQuery(sqlStatementString);

         while (db_resultSet.next())
         {
            colNameString = db_resultSet.getString("Column_name");
            primaryKeys.add(colNameString);
         }

         // Column Names, Form Fields, ComboBox Text and HashMaps.

         sqlTableFieldsString = "";
         lob_sqlTableFieldsString = "";

         for (int i = 1; i < tableMetaData.getColumnCount() + 1; i++)
         {
            // Collect Information on Column.

            colNameString = tableMetaData.getColumnName(i);
            comboBoxNameString = parseColumnNameField(colNameString);
            columnClass = tableMetaData.getColumnClassName(i);
            columnType = tableMetaData.getColumnTypeName(i);
            columnSize = Integer.valueOf(tableMetaData.getColumnDisplaySize(i));

            // System.out.println(i + " " + colNameString + " " +
            // comboBoxNameString + " " +
            // columnClass + " " + columnType + " " +
            // columnSize);

            // This going to be a problem so skip this column.

            if (columnClass == null && columnType == null)
               continue;

            if (columnClass == null)
               columnClass = columnType;

            // Process & Store.

            columnNamesHashMap.put(comboBoxNameString, colNameString);
            columnClassHashMap.put(comboBoxNameString, columnClass);
            columnTypeHashMap.put(comboBoxNameString, columnType.toUpperCase());
            columnSizeHashMap.put(comboBoxNameString, columnSize);
            preferredColumnSizeHashMap.put(comboBoxNameString,
                                           Integer.valueOf(comboBoxNameString.length() * 9));

            fields.add(colNameString);
            viewFormFields.add(comboBoxNameString);
            formFields.add(comboBoxNameString);
            comboBoxFields.add(comboBoxNameString);
            currentTableHeadings.add(comboBoxNameString);
            allTableHeadings.add(comboBoxNameString);
            sqlTableFieldsString += identifierQuoteString + colNameString + identifierQuoteString + ", ";
            
            //  Collect LOBs.
            if (((columnClass.indexOf("String") == -1 && columnType.toUpperCase().indexOf("BLOB") != -1)
                  || (columnClass.indexOf("String") != -1 && !columnType.toUpperCase().equals("CHAR")
                      && columnSize.intValue() > 65535)) && !primaryKeys.contains(colNameString))
            {
               lobDataTypesHashMap.put(comboBoxNameString, colNameString);
               lob_sqlTableFieldsString += identifierQuoteString + colNameString + identifierQuoteString + " ";
            }

            // Special Column Fields.
            if (primaryKeys.contains(colNameString))
            {
               if (columnSize == null || columnSize.intValue() > 255)
                   columnSize = new Integer("255");
               keyLengthHashMap.put(colNameString, columnSize);
            }
         }
         // Clean up the SQL field string for later use.
         if (sqlTableFieldsString.length() > 2)
            sqlTableFieldsString = sqlTableFieldsString.substring(0, sqlTableFieldsString.length() - 2);
         
         // Debug for key resolution varification.
         /*
         System.out.print(sqlTable + ": ");
         Iterator<String> temp = primaryKeys.iterator();
         while (temp.hasNext())
         {
            String currentKey = temp.next();
            System.out.print(currentKey + " " + keyLengthHashMap.get(currentKey) + ", ");
         }
         System.out.println();
         */

         return true;
      }
      catch (SQLException e)
      {
         ConnectionManager.displaySQLErrors(e, "TableTabPanel_MySQL getColumnNames()");
         return false;
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
            ConnectionManager.displaySQLErrors(sqle, "TableTabPanel_MySQL getColumnNames()");
         }
         finally
         {
            if (sqlStatement != null)
               sqlStatement.close();
         }
      }
   }

   //==============================================================
   // Class method to load the current table's data. The routine
   // will apply the sort and search parameters.
   //==============================================================

   public boolean loadTable(Connection dbConnection)
   {
      // Method Instances
      StringBuffer lobLessSQLStatement;
      Statement sqlStatement;
      ResultSet rs;

      StringBuffer searchQueryString;
      String columnSearchString, searchTextString;
      String lobLessFieldsString;
      String columnName, columnClass, columnType;
      Integer keyLength;
      int columnSize, preferredColumnSize;
      Object currentContentData;
      
      // Obtain search parameters column names as needed & saving
      // state for history.
      
      columnSearchString = columnNamesHashMap.get(searchComboBox.getSelectedItem());
      searchTextString = searchTextField.getText();
      
      if (historyAction)
         saveHistory();
      
      searchQueryString = new StringBuffer();
      if (searchTextString.equals(""))
         searchQueryString.append("'1' LIKE '%'");
      else
      {         
         // No field specified so build search for all.
         if (columnSearchString == null)
         {
            String[] tableColumns;
            tableColumns = sqlTableFieldsString.split(",");
            
            for (int i = 0; i < tableColumns.length; i++)
            {
               columnName = tableColumns[i].replaceAll(identifierQuoteString, "");
               columnType = columnTypeHashMap.get(parseColumnNameField(columnName.trim()));
               
               String searchString = searchTextString;
               
               if (columnType.equals("DATE"))
               {
                  searchString = MyJSQLView_Utils.processDateFormatSearch(searchString);
                  
                  // Something not right in conversion.
                  if (searchString.equals("0"))
                     searchString = searchTextString;
               }
               else if (columnType.equals("DATETIME") || columnType.equals("TIMESTAMP"))
               {
                  if (searchString.indexOf(" ") != -1)
                     searchString = MyJSQLView_Utils.processDateFormatSearch(
                        searchString.substring(0, searchString.indexOf(" ")))
                        + searchString.substring(searchString.indexOf(" "));
                  else if (searchString.indexOf("-") != -1 || searchString.indexOf("/") != -1)
                     searchString = MyJSQLView_Utils.processDateFormatSearch(searchString);
               }
                  
               if (i < tableColumns.length - 1)
                  searchQueryString.append(tableColumns[i] + " LIKE '%" + searchString + "%' OR");
               else
                  searchQueryString.append(tableColumns[i] + " LIKE '%" + searchString + "%'");
            }
         }
         // Field specified.
         else
         {
            columnType = columnTypeHashMap.get(searchComboBox.getSelectedItem());
            
            if (columnType.equals("DATE"))
               searchTextString = MyJSQLView_Utils.processDateFormatSearch(searchTextString);
            else if (columnType.equals("DATETIME") || columnType.equals("TIMESTAMP"))
            {
               if (searchTextString.indexOf(" ") != -1)
                  searchTextString = MyJSQLView_Utils.processDateFormatSearch(
                     searchTextString.substring(0, searchTextString.indexOf(" ")))
                     + searchTextString.substring(searchTextString.indexOf(" "));
               else if (searchTextString.indexOf("-") != -1 || searchTextString.indexOf("/") != -1)
                  searchTextString = MyJSQLView_Utils.processDateFormatSearch(searchTextString);
            }
            
            searchQueryString.append(identifierQuoteString + columnSearchString + identifierQuoteString
                                     + " LIKE '%" + searchTextString + "%'");
         }
      }
      //System.out.println(searchQueryString);

      // Connect to database to obtain the initial/new items set
      // and then sorting that set.
      
      sqlStatement = null;
      rs = null;
      
      try
      {
         sqlStatement = dbConnection.createStatement();
         
         lobLessFieldsString = sqlTableFieldsString;
         
         if (!lob_sqlTableFieldsString.equals(""))
         {
            String[]  lobColumns = lob_sqlTableFieldsString.split(" ");

            for (int i = 0; i < lobColumns.length; i++)
               lobLessFieldsString = lobLessFieldsString.replace(lobColumns[i], "");

            // All fields maybe lobs, so just include all. Network
            // performance hit.
            if (lobLessFieldsString.indexOf(identifierQuoteString) != -1)
               lobLessFieldsString = lobLessFieldsString.substring(lobLessFieldsString.indexOf(
                                                                          identifierQuoteString));
            else
               lobLessFieldsString = sqlTableFieldsString;
            
            lobLessFieldsString = lobLessFieldsString.replaceAll(" ,", "");
            if (lobLessFieldsString.endsWith(", "))
               lobLessFieldsString = lobLessFieldsString.substring(0, lobLessFieldsString.length() - 2);
         }
         
         lobLessSQLStatement = new StringBuffer();
         sqlTableStatement = new StringBuffer();
         
         if (advancedSortSearch)
         {
            // Complete With All Fields.
            sqlTableStatement.append(advancedSortSearchFrame.getAdvancedSortSearchSQL(sqlTableFieldsString,
                                             tableRowStart, tableRowLimit));
            // Summary Table Without LOBs
            lobLessSQLStatement.append(advancedSortSearchFrame.getAdvancedSortSearchSQL(lobLessFieldsString,
                                             tableRowStart, tableRowLimit));
         }
         else
         {
            // Complete With All Fields.
            sqlTableStatement.append("SELECT " + sqlTableFieldsString + " FROM " + schemaTableName
                                 + " " + "WHERE " + searchQueryString.toString() + " " + "ORDER BY "
                                 + identifierQuoteString
                                 + columnNamesHashMap.get(sortComboBox.getSelectedItem())
                                 + identifierQuoteString + " " + ascDescString + " " + "LIMIT "
                                 + tableRowLimit + " " + "OFFSET " + tableRowStart);
            
            // Summary Table Without LOBs
            lobLessSQLStatement.append("SELECT " + lobLessFieldsString + " FROM " + schemaTableName
                                        + " " + "WHERE " + searchQueryString.toString() + " " + "ORDER BY "
                                        + identifierQuoteString
                                        + columnNamesHashMap.get(sortComboBox.getSelectedItem())
                                        + identifierQuoteString + " " + ascDescString + " " + "LIMIT "
                                        + tableRowLimit + " " + "OFFSET " + tableRowStart);
         }
         // System.out.println(sqlTableStatement);
         // System.out.println(lobLessSQLStatementString);
         rs = sqlStatement.executeQuery(lobLessSQLStatement.toString());

         // Placing the results columns desired into the table that
         // will be display to the user.

         int i = 0;
         int j = 0;

         tableData = new Object[tableRowLimit][currentTableHeadings.size()];

         while (rs.next())
         {
            Iterator<String> headings = currentTableHeadings.iterator();
            while (headings.hasNext())
            {
               String currentHeading = headings.next();
               columnName = columnNamesHashMap.get(currentHeading);
               columnClass = columnClassHashMap.get(currentHeading);
               columnType = columnTypeHashMap.get(currentHeading);
               columnSize = (columnSizeHashMap.get(currentHeading)).intValue();
               keyLength = keyLengthHashMap.get(columnName);
               preferredColumnSize = (preferredColumnSizeHashMap.get(currentHeading)).intValue();

               // System.out.println(i + " " + j + " " + currentHeading + " " +
               // columnName + " " + columnClass + " " +
               // columnType + " " + columnSize + " " +
               // preferredColumnSize + " " + keyLength);

               // Storing data appropriately. If you have some date
               // or other formating, for a field here is where you
               // can take care of it.

               if (lobDataTypesHashMap.containsKey(currentHeading))
                  currentContentData = "lob";
               else
                  currentContentData = rs.getObject(columnName);

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
                     currentContentData = rs.getDate(columnName);
                     String displayDate = displayMyDateString(currentContentData + "");
                     tableData[i][j++] = displayDate;
                  }

                  // =============================================
                  // Datetime
                  else if (columnType.equals("DATETIME"))
                  {
                     currentContentData = rs.getTimestamp(columnName);
                     // System.out.println(currentContentData);
                     
                     tableData[i][j++] = (new SimpleDateFormat(
                        DBTablesPanel.getGeneralProperties().getViewDateFormat()
                        + " HH:mm:ss").format(currentContentData));
                  }

                  // =============================================
                  // Timestamp
                  else if (columnType.equals("TIMESTAMP"))
                  {
                     currentContentData = rs.getTimestamp(columnName);
                     // System.out.println(currentContentData);

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
                     // All current coloumnSizes for MySQL > 5.0 Should be 19.
                     else
                        tableData[i][j++] = (new SimpleDateFormat(
                           DBTablesPanel.getGeneralProperties().getViewDateFormat()
                           + " HH:mm:ss").format(currentContentData));
                  }

                  // =============================================
                  // Year
                  else if (columnType.equals("YEAR"))
                  {
                     String displayYear = currentContentData + "";
                     displayYear = displayYear.trim();

                     if (columnSize == 2)
                        displayYear = displayYear.substring(2, 4);
                     else
                        displayYear = displayYear.substring(0, 4);
                     tableData[i][j++] = displayYear;
                  }

                  // =============================================
                  // Blob
                  else if (columnClass.indexOf("String") == -1 && columnType.indexOf("BLOB") != -1)
                  {
                     String blobName;

                     if (columnSize == 255)
                        blobName = ("Tiny Blob");
                     else if (columnSize == 65535)
                        blobName = ("Blob");
                     else if (columnSize == 16777215)
                        blobName = ("Medium Blob");
                     else if (columnSize > 16777215)
                        blobName = ("Long Blob");
                     else
                        blobName = ("Blob");
                     
                     // Handles a key Blob
                     if (keyLength != null)
                     {
                        BlobTextKey currentBlobElement = new BlobTextKey();
                        currentBlobElement.setName(blobName);

                        String content = rs.getString(columnName);

                        if (content.length() > keyLength.intValue())
                           content = content.substring(0, keyLength.intValue());

                        currentBlobElement.setContent(content);
                        tableData[i][j++] = currentBlobElement;
                     }
                     else
                     {
                        tableData[i][j++] = blobName;
                     }
                  }

                  // =============================================
                  // Boolean
                  else if (columnClass.indexOf("Boolean") != -1)
                  {
                     tableData[i][j++] = rs.getString(columnName);
                  }

                  // =============================================
                  // Bit
                  else if (columnType.indexOf("BIT") != -1)
                  {
                     // The bit field entry of "11110" getInt
                     // returns a bogus value for some reason.
                     // Test datatypes BIT(5).
                     try
                     {
                        // int bitValue = rs.getInt(columnName);
                        // tableData[i][j++] = Integer.toBinaryString(bitValue);
                        tableData[i][j++] = Integer
                              .toBinaryString(Integer.parseInt(rs.getString(columnName)));
                     }
                     catch (NumberFormatException e)
                     {
                        tableData[i][j++] = "0";
                     }
                  }

                  // =============================================
                  // Text
                  else if (columnClass.indexOf("String") != -1 && !columnType.equals("CHAR")
                           && columnSize > 255)
                  {
                     String stringName;

                     if (columnSize <= 65535)
                     {
                        stringName = (String) currentContentData;

                        // Limit Table Cell Memory Usage.
                        if (stringName.length() > 512)
                           stringName = stringName.substring(0, 512);

                     }
                     else if (columnSize == 16777215)
                        stringName = ("Medium Text");
                     else
                        // (columnSize > 16777215)
                        stringName = ("Long Text");

                     // Handles a key String
                     if (keyLength != null & columnSize != 255)
                     {
                        BlobTextKey currentBlobElement = new BlobTextKey();
                        currentBlobElement.setName(stringName);

                        String content = rs.getString(columnName);

                        if (content.length() > keyLength.intValue())
                           content = content.substring(0, keyLength.intValue());

                        currentBlobElement.setContent(content);
                        tableData[i][j++] = currentBlobElement;
                     }
                     else
                     {
                        tableData[i][j++] = stringName;
                     }
                  }

                  // =============================================
                  // Any Other
                  else
                  {
                     tableData[i][j++] = rs.getString(columnName);
                     // tableData[i][j++] = currentContentData;
                  }
               }
               // Null Data
               else
               {
                  tableData[i][j++] = "NULL";
               }

               // Setup some sizing for the column in the summary
               // table.
               if ((tableData[i][j - 1] + "").trim().length() * 9 > preferredColumnSize)
               {
                  preferredColumnSize = (tableData[i][j - 1] + "").trim().length() * 9;
                  if (preferredColumnSize > maxPreferredColumnSize)
                     preferredColumnSize = maxPreferredColumnSize;
               }
               preferredColumnSizeHashMap.put(currentHeading, Integer.valueOf(preferredColumnSize));
            }
            j = 0;
            i++;
         }
         return true;
      }
      catch (SQLException e)
      {
         ConnectionManager.displaySQLErrors(e, "TableTabPanel_MySQL loadTable()");
         return false;
      }
      finally
      {
         try
         {
            if (rs != null)
               rs.close();
         }
         catch (SQLException sqle)
         {
            ConnectionManager.displaySQLErrors(sqle, "TableTabPanel_MySQL loadTable()");
         }
         finally
         {
            try
            {
               if (sqlStatement != null)
                  sqlStatement.close();
            }
            catch (SQLException sqle)
            {
               ConnectionManager.displaySQLErrors(sqle, "TableTabPanel_MySQL loadTable()");
            }
         }
      }
   }
   
   //==============================================================
   // Class method to view the current selected item in the table.
   //==============================================================

   public void viewSelectedItem(Connection dbConnection, int rowToView) throws SQLException
   {
      // Method Instances
      StringBuffer sqlStatementString;
      Statement sqlStatement;
      ResultSet db_resultSet;

      Iterator<String> keyIterator, textFieldNamesIterator;
      Object currentColumnName, currentContentData;
      String currentDB_ColumnName, currentColumnClass, currentColumnType;
      int columnSize;
      int keyColumn = 0;

      // Connecting to the data base, to obtain
      // the selected entry.
      
      sqlStatement = null;
      db_resultSet = null;
      
      try
      {
         // Begin the SQL statement creation.
         sqlStatement = dbConnection.createStatement();
         sqlStatementString = new StringBuffer();
         sqlStatementString.append("SELECT * FROM " + schemaTableName + " WHERE ");

         if (!primaryKeys.isEmpty())
         {
            // Find the key column, in case it has been moved
            // in the summary table, then obtain entry content.

            keyIterator = primaryKeys.iterator();

            while (keyIterator.hasNext())
            {
               currentDB_ColumnName = keyIterator.next();

               for (int i = 0; i < listTable.getColumnCount(); i++)
                  if (listTable.getColumnName(i).equals(parseColumnNameField(currentDB_ColumnName)))
                     keyColumn = i;

               // Found now get key info.
               currentContentData = listTable.getValueAt(rowToView, keyColumn);

               // Special case of blob/text key.
               if (currentContentData instanceof BlobTextKey)
               {
                  String keyString = ((BlobTextKey) currentContentData).getContent();
                  keyString = keyString.replaceAll("'", "''");

                  // select * from t1 where a like "hello%";
                  sqlStatementString.append(identifierQuoteString + currentDB_ColumnName + identifierQuoteString
                                            + " LIKE '" + keyString + "%' AND ");
               }
               // Normal keys
               else
               {
                  // Escape single quotes.
                  currentColumnClass = (String) columnClassHashMap
                        .get(parseColumnNameField(currentDB_ColumnName));
                  
                  if (currentColumnClass.indexOf("String") != -1)
                     currentContentData = ((String) currentContentData).replaceAll("'", "''");

                  // Reformat date keys.
                  currentColumnType = columnTypeHashMap.get(parseColumnNameField(currentDB_ColumnName));
                  if (currentColumnType.equals("DATE"))
                  {
                     sqlStatementString.append(identifierQuoteString + currentDB_ColumnName
                                               + identifierQuoteString + "='"
                                               + MyJSQLView_Utils.convertViewDateString_To_DBDateString(
                                                  currentContentData + "", DBTablesPanel.getGeneralProperties().getViewDateFormat())
                                               + "' AND ");
                  }
                  else
                     sqlStatementString.append(identifierQuoteString + currentDB_ColumnName
                                               + identifierQuoteString + "='" + currentContentData
                                               + "' AND ");
               }

            }
            sqlStatementString.delete((sqlStatementString.length() - 5), sqlStatementString.length());
         }
         // See if we can brute force an all fields
         // SELECT WHERE query.
         else
         {
            // Cycle through each field and set value.
            for (int i = 0; i < listTable.getColumnCount(); i++)
            {
               currentContentData = listTable.getValueAt(rowToView, i);
               currentDB_ColumnName = (String) columnNamesHashMap.get(listTable.getColumnName(i));
               currentColumnClass = columnClassHashMap.get(listTable.getColumnName(i));
               currentColumnType = columnTypeHashMap.get(listTable.getColumnName(i));
               columnSize = columnSizeHashMap.get(listTable.getColumnName(i)).intValue();
               
               // System.out.println("field:" + currentDB_ColumnName + " class:" + currentColumnClass
               //                    + " type:" + currentColumnType + " value:" + currentContentData);
               
               // Skip Blob, Text, & Float Unless NULL.
               if ((currentColumnClass.indexOf("String") == -1 && currentColumnType.indexOf("BLOB") != -1)
                     || (currentColumnClass.indexOf("String") != -1 && !currentColumnType.equals("CHAR")
                         && columnSize > 255)
                     || (currentColumnType.indexOf("FLOAT") != -1))
               {
                  if (currentContentData.toString().toUpperCase().equals("NULL"))
                     sqlStatementString.append(identifierQuoteString + currentDB_ColumnName
                        + identifierQuoteString + " IS NULL AND ");
                  continue;     
               }
               
               // NULL
               if (currentContentData.toString().toUpperCase().equals("NULL"))
               {
                  sqlStatementString.append(identifierQuoteString + currentDB_ColumnName
                     + identifierQuoteString + " IS NULL ");
               }
               // Try the Rest
               else
               {
                  sqlStatementString.append(identifierQuoteString + currentDB_ColumnName
                     + identifierQuoteString);
                  
                  // Process Date
                  if (currentColumnType.equals("DATE"))
                  {
                     String dateString = MyJSQLView_Utils.processDateFormatSearch(
                        (String) currentContentData);
                     
                     sqlStatementString.append("='" + dateString + "' ");
                  }
                  // Process DateTime
                  else if (currentColumnType.equals("DATETIME") || currentColumnType.equals("TIMESTAMP"))
                  {
                     String content, dateTimeString;
                     content = (String) currentContentData;
                     
                     dateTimeString = MyJSQLView_Utils.processDateFormatSearch(content.substring(0,
                        content.indexOf(" ")))
                           + content.substring(content.indexOf(" "));
                     
                     sqlStatementString.append("='" + dateTimeString + "' ");
                  }
                  // Process BIT
                  else if (currentColumnType.indexOf("BIT") != -1)
                  {
                     sqlStatementString.append("=B'" + currentContentData + "' ");
                  }
                  // All Others
                  else
                  {
                     if (currentColumnClass.indexOf("Integer") != -1
                           || currentColumnClass.indexOf("Long") != -1
                           || currentColumnClass.indexOf("Float") != -1
                           || currentColumnClass.indexOf("Double") != -1
                           || currentColumnClass.indexOf("Byte") != -1
                           || currentColumnClass.indexOf("BigDecimal") != -1
                           || currentColumnClass.indexOf("Short") != -1)
                        sqlStatementString.append("=" + currentContentData + " ");
                     else
                        sqlStatementString.append("='" + currentContentData + "' ");
                  }
               }
               sqlStatementString.append("AND ");
            }
            sqlStatementString.delete(sqlStatementString.length() - 4, sqlStatementString.length());
         }
         
         // System.out.println(sqlStatementString);
         db_resultSet = sqlStatement.executeQuery(sqlStatementString.toString());
         
         if (!db_resultSet.next())
            return;

         // Cycling through the item fields and setting
         // in the tableViewForm.

         textFieldNamesIterator = viewFormFields.iterator();
         int i = 0;

         while (textFieldNamesIterator.hasNext())
         {
            currentColumnName = textFieldNamesIterator.next();
            currentDB_ColumnName = columnNamesHashMap.get(currentColumnName);
            currentColumnClass = columnClassHashMap.get(currentColumnName);
            currentColumnType = columnTypeHashMap.get(currentColumnName);
            columnSize = (columnSizeHashMap.get(currentColumnName)).intValue();

            currentContentData = db_resultSet.getString(currentDB_ColumnName);
            // System.out.println(i + " " + currentColumnName + " " +
            // currentDB_ColumnName + " " +
            // currentColumnType + " " + columnSize + " " + currentContentData);

            if (currentContentData != null)
            {
               // DATE Type Field
               if (currentColumnType.equals("DATE"))
               {
                  currentContentData = db_resultSet.getDate(currentDB_ColumnName);
                  tableViewForm.setFormField(currentColumnName,
                     (Object) displayMyDateString(currentContentData + ""));
               }

               // DATETIME Type Field
               else if (currentColumnType.equals("DATETIME"))
               {
                  currentContentData = db_resultSet.getTimestamp(currentDB_ColumnName);
                  // System.out.println(currentContentData);
                  
                  tableViewForm.setFormField(currentColumnName,
                                             (new SimpleDateFormat(
                                                DBTablesPanel.getGeneralProperties().getViewDateFormat()
                                                + " HH:mm:ss").format(currentContentData)));
               }

               // TIMESTAMP Type Field
               else if (currentColumnType.equals("TIMESTAMP"))
               {
                  currentContentData = db_resultSet.getTimestamp(currentDB_ColumnName);

                  if (columnSize == 2)
                     tableViewForm.setFormField(currentColumnName, (new SimpleDateFormat("yy")
                           .format(currentContentData)));
                  else if (columnSize == 4)
                     tableViewForm.setFormField(currentColumnName, (new SimpleDateFormat("MM-yy")
                           .format(currentContentData)));
                  else if (columnSize == 6)
                     tableViewForm.setFormField(currentColumnName, (new SimpleDateFormat("MM-dd-yy")
                           .format(currentContentData)));
                  else if (columnSize == 8)
                     tableViewForm.setFormField(currentColumnName, (new SimpleDateFormat("MM-dd-yyyy")
                           .format(currentContentData)));
                  else if (columnSize == 10)
                     tableViewForm.setFormField(currentColumnName, (new SimpleDateFormat("MM-dd-yy HH:mm")
                           .format(currentContentData)));
                  else if (columnSize == 12)
                     tableViewForm.setFormField(currentColumnName, (new SimpleDateFormat("MM-dd-yyyy HH:mm")
                           .format(currentContentData)));
                  // All current coloumnSizes for MySQL > 5.0 Should be 19.
                  else
                     tableViewForm.setFormField(currentColumnName,
                        (new SimpleDateFormat(DBTablesPanel.getGeneralProperties().getViewDateFormat()
                           + " HH:mm:ss").format(currentContentData)));
               }

               // YEAR Type Field
               else if (currentColumnType.equals("YEAR"))
               {
                  String displayYear = currentContentData + "";
                  displayYear = displayYear.trim();

                  if (columnSize == 2)
                     displayYear = displayYear.substring(2, 4);
                  else
                     displayYear = displayYear.substring(0, 4);

                  currentContentData = displayYear;
                  tableViewForm.setFormField(currentColumnName, currentContentData);
               }

               // Blob Type Field
               else if (currentColumnClass.indexOf("String") == -1 && currentColumnType.indexOf("BLOB") != -1)
               {
                  if (((String) currentContentData).getBytes().length != 0)
                  {
                     currentContentData = db_resultSet.getBytes(currentDB_ColumnName);

                     int size = ((byte[]) currentContentData).length;
                     tableViewForm.setFormField(currentColumnName, (Object) ("BLOB " + size + " Bytes"));
                     tableViewForm.setFormFieldBlob(currentColumnName, (byte[]) currentContentData);
                  }
                  else
                     tableViewForm.setFormField(currentColumnName, (Object) "BLOB 0 Bytes");
               }

               // Bit Type Field
               else if (currentColumnType.equals("BIT"))
               {
                  try
                  {
                     int bitValue = Integer.parseInt(db_resultSet.getString(currentDB_ColumnName));
                     tableViewForm.setFormField(currentColumnName, (Object) Integer.toBinaryString(bitValue));
                  }
                  catch (NumberFormatException e)
                  {
                     tableViewForm.setFormField(currentColumnName, (Object) "0");
                  }
               }

               // Text, MediumText & LongText Type Fields
               else if (currentColumnClass.indexOf("String") != -1 && !currentColumnType.equals("CHAR")
                        && columnSize > 255)
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
         ConnectionManager.displaySQLErrors(e, "TableTabPanel_MySQL viewSelectedItem()");
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
            ConnectionManager.displaySQLErrors(sqle, "TableTabPanel_MySQL viewSelectedItem()");
         }
         finally
         {
            if (sqlStatement != null)
               sqlStatement.close();
         }
      }
   }

   //==============================================================
   // Class method to add a table entry.
   //==============================================================

   public void addItem(Connection dbConnection)
   {
      Iterator<String> textFieldNamesIterator;
      Object currentColumnName, currentContentData;
      String currentColumnClass, currentColumnType;

      // Showing the Table Entry Form
      TableEntryForm addForm = new TableEntryForm("Add Table Entry: ", true, schemaTableName,
                                                  -1, null, primaryKeys, autoIncrementHashMap, null,
                                                  formFields, tableViewForm, columnNamesHashMap,
                                                  columnClassHashMap, columnTypeHashMap,
                                                  columnSizeHashMap, columnEnumHashMap,
                                                  columnSetHashMap);

      // Doing some sizing of the height based on the number
      // of fields in the table. The entry form will though
      // provided scrollbars.

      if ((((formFields.size() / 2) + 1) * 35) > 400)
      {
         if (((formFields.size() / 2) + 1) * 35 < 600)
            addForm.setSize(800, (((formFields.size() / 2) + 1) * 35));
         else
            addForm.setSize(800, 600);
      }
      else
         addForm.setSize(800, 400);

      addForm.disposeButton.addActionListener(this);
      addForm.addMouseListener(MyJSQLView.getPopupMenuListener());
      addForm.center();
      addForm.setVisible(true);

      // Fields in the add form will be empty except the
      // ones where special data is specified to the
      // user. aka enum, set, date format.

      textFieldNamesIterator = formFields.iterator();

      while (textFieldNamesIterator.hasNext())
      {
         currentColumnName = textFieldNamesIterator.next();
         currentColumnClass = columnClassHashMap.get(currentColumnName);
         currentColumnType = columnTypeHashMap.get(currentColumnName);

         // Auto-Increment Type Field
         if (autoIncrementHashMap.containsKey(currentColumnName))
         {
            currentContentData = "AUTO";
            addForm.setFormField(currentColumnName, currentContentData);
         }

         // ENUM, BOOLEAN Single Bit, & SET Type Fields
         if (columnEnumHashMap.containsKey(currentColumnName)
             || columnSetHashMap.containsKey(currentColumnName))
         {
            try
            {
               setSpecialFieldData(addForm, dbConnection, currentColumnName, null);
            }
            catch (SQLException e)
            {
               ConnectionManager.displaySQLErrors(e, "TableTabPanel_MySQL addItem()");
            }  
         }

         // DATE Type Field
         if (currentColumnType.equals("DATE"))
         {
            currentContentData = DBTablesPanel.getGeneralProperties().getViewDateFormat();
            addForm.setFormField(currentColumnName, currentContentData);
         }

         // TIME Type Field
         if (currentColumnType.equals("TIME"))
         {
            currentContentData = "hh:mm:ss";
            addForm.setFormField(currentColumnName, currentContentData);
         }

         // DATETIME Type Field
         if (currentColumnType.equals("DATETIME"))
         {
            currentContentData = DBTablesPanel.getGeneralProperties().getViewDateFormat() + " hh:mm:ss";
            addForm.setFormField(currentColumnName, currentContentData);
         }

         // TIMESTAMP Type Field
         if (currentColumnType.equals("TIMESTAMP"))
         {
            currentContentData = "NOW()";
            addForm.setFormField(currentColumnName, currentContentData);
         }

         // BLOB Type Field
         if (currentColumnClass.indexOf("String") == -1 && currentColumnType.indexOf("BLOB") != -1)
         {
            addForm.setFormField(currentColumnName, (Object) ("BLOB Browse"));
         }

         // All TEXT, MEDIUMTEXT & LONGTEXT Type Field
         if (currentColumnClass.indexOf("String") != -1 && !currentColumnType.equals("CHAR")
             && ((columnSizeHashMap.get(currentColumnName)).intValue() > 255))
         {
            addForm.setFormField(currentColumnName, (Object) ("TEXT Browse"));
         }

         // YEAR Type Field
         if (currentColumnType.equals("YEAR"))
         {
            if (columnSizeHashMap.get(currentColumnName).intValue() == 2)
               currentContentData = "YY";
            else
               currentContentData = "YYYY";
            addForm.setFormField(currentColumnName, currentContentData);
         }
      }
   }

   //==============================================================
   // Class method to edit the current selected item.
   //==============================================================

   public void editSelectedItem(Connection dbConnection, int rowToEdit, Object columnName, Object id)
                                throws SQLException
   {
      // Method Instances
      StringBuffer sqlStatementString;
      Statement sqlStatement;
      ResultSet db_resultSet;

      Iterator<String> keyIterator, textFieldNamesIterator;
      Object currentColumnName, currentContentData;
      String currentDB_ColumnName, currentColumnClass, currentColumnType;
      int currentColumnSize;
      int keyColumn = 0;

      // Showing the edit form and trying to size appropriately.
      TableEntryForm editForm = new TableEntryForm("Edit Table Entry: ", false, schemaTableName,
                                                   rowToEdit, this, primaryKeys,
                                                   autoIncrementHashMap, id,
                                                   formFields, tableViewForm,
                                                   columnNamesHashMap, columnClassHashMap, columnTypeHashMap,
                                                   columnSizeHashMap, columnEnumHashMap, columnSetHashMap);

      if ((((formFields.size() / 2) + 1) * 35) > 400)
      {
         if (((formFields.size() / 2) + 1) * 35 < 600)
            editForm.setSize(800, (((formFields.size() / 2) + 1) * 35));
         else
            editForm.setSize(800, 600);
      }
      else
         editForm.setSize(800, 400);
      editForm.disposeButton.addActionListener(this);
      editForm.center();
      editForm.setVisible(true);

      // Connecting to the data base, to obtain
      // the selected entries field data.
      
      sqlStatement = null;
      db_resultSet = null;

      try
      {
         sqlStatement = dbConnection.createStatement();

         // Begin the SQL statement(s) creation.
         sqlStatementString = new StringBuffer();
         sqlStatementString.append("SELECT * FROM " + schemaTableName + " WHERE ");

         keyIterator = primaryKeys.iterator();

         // Find the key column, in case it has been moved
         // in the summary table, then obtain entry content.

         while (keyIterator.hasNext())
         {
            currentDB_ColumnName = keyIterator.next();

            for (int i = 0; i < listTable.getColumnCount(); i++)
               if (listTable.getColumnName(i).equals(parseColumnNameField(currentDB_ColumnName)))
                  keyColumn = i;

            // Found the key so get info.
            currentContentData = listTable.getValueAt(rowToEdit, keyColumn);

            // Special case of blob/text key.
            if (currentContentData instanceof BlobTextKey)
            {
               String keyString = ((BlobTextKey) currentContentData).getContent();
               keyString = keyString.replaceAll("'", "''");

               sqlStatementString.append(identifierQuoteString + currentDB_ColumnName
                                         + identifierQuoteString
                                         + " LIKE '" + keyString + "%' AND ");
            }
            // Normal key.
            else
            {
               // Escape single quotes.
               currentColumnClass = columnClassHashMap
                     .get(parseColumnNameField(currentDB_ColumnName));
               if (currentColumnClass.indexOf("String") != -1)
                  currentContentData = ((String) currentContentData).replaceAll("'", "''");

               // Reformat date keys.
               currentColumnType = columnTypeHashMap.get(parseColumnNameField(currentDB_ColumnName));
               if (currentColumnType.equals("DATE"))
               {
                  sqlStatementString.append(identifierQuoteString + currentDB_ColumnName
                                            + identifierQuoteString + "='"
                                            + MyJSQLView_Utils.convertViewDateString_To_DBDateString(
                                               currentContentData + "", DBTablesPanel.getGeneralProperties().getViewDateFormat())
                                            + "' AND ");
               }
               else
                  sqlStatementString.append(identifierQuoteString + currentDB_ColumnName + identifierQuoteString
                                            + "='" + currentContentData + "' AND ");
            }
         }
         sqlStatementString.delete((sqlStatementString.length() - 5), sqlStatementString.length());
         // System.out.println(sqlStatementString);
         db_resultSet = sqlStatement.executeQuery(sqlStatementString.toString());
         db_resultSet.next();

         // Now that we have the data for the selected field entry in
         // the table fill in the edit form.

         textFieldNamesIterator = formFields.iterator();

         while (textFieldNamesIterator.hasNext())
         {
            currentColumnName = textFieldNamesIterator.next();
            currentDB_ColumnName = columnNamesHashMap.get(currentColumnName);
            currentColumnClass = columnClassHashMap.get(currentColumnName);
            currentColumnType = columnTypeHashMap.get(currentColumnName);
            currentColumnSize = (columnSizeHashMap.get(currentColumnName)).intValue();

            currentContentData = db_resultSet.getString(currentDB_ColumnName);
            // System.out.println(currentColumnName + " " + currentContentData);

            // Special content from other tables, ComboBoxes, maybe.
            // Also time, date or your special field formatting.

            if (((String) currentColumnName).equals("Your Special Field Name"))
               setSpecialFieldData(editForm, dbConnection, currentColumnName, currentContentData);

            // ENUM, BOOLEAN Single Bit, & SET Type Fields
            else if (columnEnumHashMap.containsKey(currentColumnName)
                     || columnSetHashMap.containsKey(currentColumnName))
            {
               if (currentColumnClass.indexOf("Boolean") != -1 && currentColumnSize == 1)
               {
                  if (currentContentData.equals("0"))
                     currentContentData = "FALSE";
                  else
                     currentContentData = "TRUE";
               }
               setSpecialFieldData(editForm, dbConnection, currentColumnName, currentContentData);
            }

            // DATE Type Field
            else if (currentColumnType.equals("DATE"))
            {
               if (currentContentData != null)
               {
                  currentContentData = db_resultSet.getDate(currentDB_ColumnName);
                  editForm.setFormField(currentColumnName, (Object) displayMyDateString(currentContentData
                                                                                        + ""));
               }
               else
                  editForm.setFormField(currentColumnName,
                                        (Object) DBTablesPanel.getGeneralProperties().getViewDateFormat());
            }

            // DATETIME Type Field
            else if (currentColumnType.equals("DATETIME"))
            {
               if (currentContentData != null)
               {
                  currentContentData = db_resultSet.getTimestamp(currentDB_ColumnName);
                  // System.out.println(currentContentData);
                  
                  currentContentData = new SimpleDateFormat(
                     DBTablesPanel.getGeneralProperties().getViewDateFormat()
                     + " HH:mm:ss").format(currentContentData);
                  editForm.setFormField(currentColumnName, currentContentData);
               }
               else
                  editForm.setFormField(currentColumnName,
                     (Object) DBTablesPanel.getGeneralProperties().getViewDateFormat() + " HH:MM:SS");
            }

            // TIMESTAMP Type Field
            else if (currentColumnType.equals("TIMESTAMP"))
            {
               currentContentData = db_resultSet.getTimestamp(currentDB_ColumnName);
               // System.out.println(currentContentData);

               if (currentColumnSize == 2)
               {
                  if (currentContentData != null)
                  {
                     currentContentData = new SimpleDateFormat("yy").format(currentContentData);
                     editForm.setFormField(currentColumnName, currentContentData);
                  }
                  else
                     editForm.setFormField(currentColumnName, (Object) "YY");
               }
               else if (currentColumnSize == 4)
               {
                  if (currentContentData != null)
                  {
                     currentContentData = new SimpleDateFormat("MM-yy").format(currentContentData);
                     editForm.setFormField(currentColumnName, currentContentData);
                  }
                  else
                     editForm.setFormField(currentColumnName, (Object) "MM-YY");
               }
               else if (currentColumnSize == 6)
               {
                  if (currentContentData != null)
                  {
                     currentContentData = new SimpleDateFormat("MM-dd-yy").format(currentContentData);
                     editForm.setFormField(currentColumnName, currentContentData);
                  }
                  else
                     editForm.setFormField(currentColumnName, (Object) "MM-DD-YY");
               }
               else if (currentColumnSize == 8)
               {
                  if (currentContentData != null)
                  {
                     currentContentData = new SimpleDateFormat("MM-dd-yyyy").format(currentContentData);
                     editForm.setFormField(currentColumnName, currentContentData);
                  }
                  else
                     editForm.setFormField(currentColumnName, (Object) "MM-DD-YYYY");
               }
               else if (currentColumnSize == 10)
               {
                  if (currentContentData != null)
                  {
                     currentContentData = new SimpleDateFormat("MM-dd-yy HH:mm").format(currentContentData);
                     editForm.setFormField(currentColumnName, currentContentData);
                  }
                  else
                     editForm.setFormField(currentColumnName, (Object) "MM-DD-YY HH:MM");
               }
               else if (currentColumnSize == 12)
               {
                  if (currentContentData != null)
                  {
                     currentContentData = new SimpleDateFormat("MM-dd-yyyy HH:mm").format(currentContentData);
                     editForm.setFormField(currentColumnName, currentContentData);
                  }
                  else
                     editForm.setFormField(currentColumnName, (Object) "MM-DD-YYYY HH:MM");
               }
               // All current coloumnSizes for MySQL > 5.0 Should be 19.
               else
               {
                  if (currentContentData != null)
                  {
                     currentContentData = new SimpleDateFormat(
                        DBTablesPanel.getGeneralProperties().getViewDateFormat()
                        + " HH:mm:ss").format(currentContentData);
                     editForm.setFormField(currentColumnName, currentContentData);
                  }
                  else
                     editForm.setFormField(currentColumnName,
                        (Object) DBTablesPanel.getGeneralProperties().getViewDateFormat() + " HH:MM:SS");
               }
            }

            // YEAR Type Field
            else if (currentColumnType.equals("YEAR"))
            {
               String displayYear = currentContentData + "";
               displayYear = displayYear.trim();

               if (currentColumnSize == 2)
               {
                  if (currentContentData != null)
                  {
                     displayYear = displayYear.substring(2, 4);
                     currentContentData = displayYear;
                     editForm.setFormField(currentColumnName, currentContentData);
                  }
                  else
                     editForm.setFormField(currentColumnName, (Object) "YY");
               }
               else
               {
                  if (currentContentData != null)
                  {
                     displayYear = displayYear.substring(0, 4);
                     currentContentData = displayYear;
                     editForm.setFormField(currentColumnName, currentContentData);
                  }
                  else
                     editForm.setFormField(currentColumnName, (Object) "YYYY");
               }
            }

            // Blob Type Field
            else if (currentColumnClass.indexOf("String") == -1 && currentColumnType.indexOf("BLOB") != -1)
            {
               if (currentContentData != null)
               {
                  if (((String) currentContentData).getBytes().length != 0)
                  {
                     int size = ((String) currentContentData).getBytes().length;
                     editForm.setFormField(currentColumnName, (Object) ("BLOB " + size + " Bytes"));
                  }
                  else
                  {
                     editForm.setFormField(currentColumnName, (Object) ("BLOB 0 Bytes"));
                  }
                  editForm.setFormFieldBlob(currentColumnName, db_resultSet.getBytes(currentDB_ColumnName));

               }
               else
                  editForm.setFormField(currentColumnName, (Object) ("BLOB NULL"));
            }

            // Bit/Tinyint(1) Type Field
            else if (currentColumnType.equals("BIT"))
            {
               try
               {
                  int bitValue = Integer.parseInt(db_resultSet.getString(currentDB_ColumnName));
                  editForm.setFormField(currentColumnName, (Object) Integer.toBinaryString(bitValue));
               }
               catch (NumberFormatException e)
               {
                  editForm.setFormField(currentColumnName, (Object) "0");
               }
            }

            // All Text But TinyText Type Field
            else if (currentColumnClass.indexOf("String") != -1 && !currentColumnType.equals("CHAR")
                     && currentColumnSize > 255)
            {
               if (currentContentData != null)
               {
                  if (((String) currentContentData).getBytes().length != 0)
                  {
                     int size = ((String) currentContentData).getBytes().length;
                     editForm.setFormField(currentColumnName, (Object) ("TEXT " + size + " Bytes"));
                  }
                  else
                  {
                     editForm.setFormField(currentColumnName, (Object) "TEXT 0 Bytes");
                  }
                  editForm.setFormFieldText(currentColumnName, (String) currentContentData);
               }
               else
                  editForm.setFormField(currentColumnName, (Object) "TEXT NULL");
            }

            // Default Content. A normal table entry should
            // fall through here, to set content.
            else
            {
               if (currentContentData != null)
                  editForm.setFormField(currentColumnName, currentContentData);
               else
                  editForm.setFormField(currentColumnName, (Object) "NULL");
            }
         }
      }
      catch (SQLException e)
      {
         ConnectionManager.displaySQLErrors(e, "TableTabPanel_MySQL editSelectedEntry()");
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
            ConnectionManager.displaySQLErrors(sqle, "TableTabPanel_MySQL editSelectedEntry()");
         }
         finally
         {
            if (sqlStatement != null)
               sqlStatement.close();
         }
      }
   }
}