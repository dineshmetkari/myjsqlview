//=================================================================
//                    SQL DataDumpThread
//=================================================================
//   This class provides a thread to safely dump database table
// data to a local file in SQL format. A status dialog with cancel
// is provided to provide the ability to prematurely terminate the
// dump.
//
//                   << SQLDataDumpThread.java >>
//
//=================================================================
// Copyright (C) 2005-2015 Dana M. Proctor
// Version 7.15 05/09/2015
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
// Version 1.0 Original SQLDataDumpThread Class.
//         1.1 Minor Names/Info/Format Changes, CVS Addition.
//         1.2 Corrected Un-Intentional Removal of "+" in Class Method run().
//             Original File Had "+" at Beginning of Each Line. ReplaceAll
//             Screwed Up.
//		     1.3 Added create table statement, lock table, mysql version in
//             header.
//         1.4 Minor Format Changes & Closing Statements in Some of the Class
//             Methods.
//         1.5 Reviewed & Commented for Next Release. Changed dumpData Instance
//             to StringBuilder As Suggested.
//         1.6 Removed Class Modifier "Public" & Comment/Style Changes.
//         1.7 Class WriteDataFile Boolean Argument Addition, true.
//         1.8 Corrected Incrementing of ProgressBar.
//         1.9 Reverted Class Instance dumpData Back to Object, Mainly to Maintain
//             Compatibility to JRE1.4xxxx.
//         2.0 Partial Fix for Blob Data Output.
//         2.1 Class Method dumpChunkOfData().
//         2.2 Fixed Minor Problem in dumpData output.
//         2.3 Created Single Argument Constructor, (String[] myJSQLView_Version).
//             Beginning of Rebuild of Class. 
//         2.4 Bug Fix SQL Export Blob Data Coruption Task #135774.
//         2.5 SQL Export Options Drop Table, Type for Insert, and Lock.
//             Other Misc. Changes.
//         2.6 Reviewing/Cleanup. Class Methods insertReplaceStatementData(),
//             Completed & Initial updateStatementData().
//         2.7 Initial Pass at Completion of Class Method updateStatementData().
//             Seems to be Correct, but fails Import.
//         2.8 Minor Changes to Class Method updateStatementData(). Validation
//             of Proper Output. Tested, Acts as Suspected With No WHERE.
//         2.9 Implemented Auto-Increment and TimeStamp Field Entries Based
//             on Preferences SQL Data Export Options.
//         3.0 Took Care of NULL Values. Added Constructor Argument limits.
//             Implemented Limits on Select Statement for Data.
//         3.1 Changed Info in Class Method generateHeaders().
//         3.2 Implemented Explicit, (SET) Data for INSERT & REPLACE
//             Exports. Name Change Class Method replaceStatementData() to
//             explicitStatementData(). Added Auto-Increment & Timestamp
//             Functionality to Class Method explicitStatementData().
//         3.3 Commented Out a System.out.println SQLStatementString.
//         3.4 Renamed Class SQLDataExportProperties to DataExportProperties.
//         3.5 Change in Class Instance sqlDataExportOptions Assignment.
//             MyJSQLView.getDataExportProperties().
//         3.6 Class Method explicitStatementData() Correction to Properly
//             Create SQL Statement in Right Order with Type for Insert/Replace.
//         3.7 Class Instance dumpProgressBar Set pack() and setVisible(true).
//         3.8 Corrected Bug in SQL Statements When the Exported Table Contains
//             No Data. Check in Class Method run(). Fixed Showing of dumpProgressBar
//             In Class Method explicitStatementData().
//         3.9 Deleted Exported File if Class Instance dumpProgressBar.isCanceled() &
//             Disposed Same All in Class Method run().
//         4.0 Code Cleanup.
//         4.1 Fix for Year Fields That Were Reading A Complete Date, YYYY-MM-DD,
//             From the Database. Used subString(0,4) in Class Methods
//             insertReplaceStatementData() & explicitStatementData(). Had to
//             Detect the Index of YEAR Fields in the Latter Method.
//         4.2 Removed Backtick/Accent Marks From Table/Column Name Identifiers.
//             Changed Limit Statements to LIMIT XX, OFFSET XX Syntax. Minor Changes
//             to Version in generateHeaders(). Lock Information Generation Based
//             on Database Connection, MyJSQLView_Access.getSubProtocol().
//         4.3 Change in Javadoc Comments.
//         4.4 Added Class Instance identifierQuoteString. Properly Dumping Just
//             Table Structure, Bug 142947. Check to Only Generate Header if
//             Edit | Preferences Set for No Table Structure & No Data.
//         4.5 Class Methods insertReplaceStatementData() &
//             explicitStatementData() TableTabPanel.getAutoIncrementHashMap().
//             Exchanged Class Instance autoIncrement With autoIncrementFieldIndexes
//             in Class Method insertReplaceStatementData().
//         4.6 Class Method insertReplaceStatementData() Changed Method Instance
//             autoIncrementFieldIndexes to HashMap. Modified the Auto-Increment
//             Field Handling in the Same Method & explicitStatementData() Method
//             to Sequence Properly in PostgreSQL Database.
//         4.7 Header Update.
//         4.8 Addition of Bit Type and Modification of How Binary, Bytea, Data is
//             Handled in Class Method explicitStatementData(). Change of Class 
//             Method dumpBlobData() to dumpBinaryData(). Modification to Dump
//             Binary Octal Values for PostgreSQL database.
//         4.9 Some Cleanup and Comment Changes. Class Method addSlashes() Changed
//             to addEscapes().
//         5.0 Escaped Backslashes in Class Method addEscapes(). BIT Fields to
//             Class Method explicitStatementData().
//         5.1 BIT Field Single Quote Addition for MyJSQL in Class Methods
//             insertReplaceStatementData() & explicitStatementData(). Implement
//             Proper Lock SQL Statement for PostgreSQL, Default.
//         5.2 Class Method explicitStatementData() Modified to Handle the UPDATE
//             WHERE Condition. Corrected to Handle PostgreSQL Data.
//         5.3 Comment Changes. Class Method getMySQLVersion() Changed to 
//             getDatabaseVersion().
//         5.4 Initial PostgreSQL Table Structure Code in Class Method
//             getTableDefinition().
//         5.5 Changes in Class Method getTableDefinition() for PostgreSQL
//             Column Default, Characters Output. Implemented identifierQuoteString
//             to All Aspects of SQL Statements, Class Methods 
//             insertReplaceStatementData() & explicitStatementData().
//         5.6 String Identifier Quote for MySQL Lock Statement. Added database
//             Instance for Class Method generateHeaders().
//         5.7 Identifer Quotes in Class Method getTableDefinition() for Constraint
//             of Foreign Keys. Changed database Setting for MySQL Database in
//             Class Method generateHeaders().
//         5.8 MyJSQLView.getDataExportProperties() and Retrieval of identifierQuoteString
//             from Same.
//         5.9 Format Changes and Additional identifierQuoteString Adage.
//         6.0 Class Instance identfierQuoteString Add to Table Name in Class
//             Method explicitStatementData().
//         6.1 Moved Creation of Instance identifierQuoteString to Each of the
//             Constructors. Class Method getTableDefinition() Replaced Instance
//             exportedTable With Method Argument tableName. Proper Creation
//             of SQL Statement With identifierQuoteString in Class Method
//             insertReplaceStatementData().
//         6.2 Changed the Handling of Escaping Single Quote Characters in the
//             Class Method addEscapes(). Also Replace the Sequence ";\n" With "; \n".
//         6.3 MySQL Bit Types Handled by Standard Format Prefix B Followed By
//             String in Bit Format in Class Methods insertReplaceStatementData() &
//             explicitStatementData().
//         6.4 Correction in Setting SQL Statement identifierQuoteString for
//             Successive INSERTS for Table Name in Class Method explicitStatementData().
//        6.41 Added 'As t' to sqlStatement String in Class Method run() for HSQLDB. 
//             Removed Class Method getDatabaseVersion(). Added
//             getDBProductionNmae_And_Version() to Class Method generateheaders() &
//             Removed Connection Argument. Class Method getRowsCount() Changed SQL
//             Statement String To Be More Generalized.
//        6.42 Binary Field Detection in Class Methods insertReplaceStatementData()
//             & explicitStatementData().
//        6.43 DataExportProperties Change From getInsertExplicit() to getInsertExpression().
//        6.44 Implemented Singular & Plural SQL Statement in Class Method
//             insertReplaceStatementData().
//        6.45 Proper Export of Binary Data Format, Hexadecimal With Quotes in
//             Class Methods explicitStatementData() & insertReplaceStatementData()
//             for HSQLDB.
//        6.46 Removal of Class Method getTableDefinition(). Process Transfered to
//             New Class TableDefinitionGenerator.
//        6.47 HSQL LIMIT Expression Parsing Problem Correction for SQL Table Summary
//             Exports in Class Methods explicitStatementData() & insertReplaceStatementData().
//        6.48 Class Method insertReplaceStatementData() Added arrayIndexes. Both Class
//             Methods insertReplace/explicitStatementData() Modifications to Fields
//             TimeStamps & Bit for Arrays.
//        6.49 Added Class Instance dbIdentiferQuoteString. Fix So That Exports May
//             Properly Have the Selected Identifer String Used From the Preferences
//             Panel.
//        6.50 Applied dbIdentifierQuoteString to Table Name in Class Method
//             getRowCount().
//        6.51 Implemented Fully Qualified Table Name. Added Class Instances
//             schemaName, tableName, schemaTableName, dbSchemaTableName. Localized
//             exportedTable Too Constructor.
//        6.52 Class Method run() Check for Data Conditional Properly Created the
//             LIMIT of One Row Return for Oracle With the ROWNUM=1 SQL Statement.
//             Clarification in the Class Methods insertRplace/explicitStatementData()
//             for Statement ResultSet to be SCROLL_INSENSITIVE & CONCUR_READ_ONLY.
//             Oracle Defaults to FORWARD_ONLY.
//        6.53 LIMIT sqlStatementString Obtained Directly From Selected Table Tab
//             Panel in Class Methods explicit/insertReplaceStatementData().
//        6.54 Detection for BLOB Fields for Oracle and Replacement of SYSTIMESTAMP
//             for NOW() Also for Oracle. All in Class Methods
//             insertReplace/explicitStatementData().
//        6.55 Moved the LOCK/UNLOCK Statements Creation Inside the Test for Data
//             in Class Method run().
//        6.56 Initial Major Modifications to Support Oracle Database SQL Exports.
//             Rearrangement of ResultSet for Obtaining Data After Row Count in
//             insertReplace/explicitStatement() Methods. Added Separate Processing
//             of DATE Fields in Same Methods. Change in Block for Check of NULL
//             Data in explicitStatementData() to Each Data Field Process. Oracle
//             Does Not Support Generic ResultSet getString() for All Fields,
//             Throws Exceptions, ie Timestamps.
//        6.57 Removed sqlStatement Creation for ResultSet.TYPE_SCROLL_INSENSITIVE,
//             ResultSet.CONCUR_READ_ONLY) in Methods insertReplace/
//             explicitStatementData(). Removed rs.isLast() to be Replaced by
//             rowCount Conditional Check. Created contentString in Default Data
//             Type Processing in insertReplace/explicitStatementData() Methods.
//             Oracle LONG & CLOB Data Type Processing. Explicit Statement Generation
//             Broken. Needs to be Fixed.
//        6.58 Fixed Explicit Statement Generation, currentRow Conditional Less
//             Than rowCount Not Larger Than. Added the Processing of Oracle RAW
//             & BLOB Data Types for Export, Class Methods Effected
//             insertReplaceStatementData(), & dumpBinaryData().
//        6.59 Added Field Processing for Oracle Data Types TIMESTAMP & TIMESTAMPTZ
//             to insertReplaceStatmentData().
//        6.60 Added Field Processing for Oracle Data Types TIMESTAMP & TIMESTAMPTZ
//             to explicitStatementData() Class Method. Corrections to Fix UPDATE
//             SQL Creation in Same Method.
//        6.61 General Review With SQLDatabaseDumpThread, Format Changes.
//        6.62 Implemented Auto-Increment of Sequence Fields for Oracle on
//             Preferences Panel Selection. Class Methods insertReplace/
//             explicitStatementData(). Added Class Instance insertReplaceDump.
//        6.63 Removed DROP TABLE IF EXISTS for Oracle Table Structure Creation
//             in run().
//        6.64 Correction in Determining rowsCount During Table Summary SQL Export.
//             Class Methods Effected insertReplace/explicitStatementData().
//        6.65 Replaced MyJSQLView.getSelectedTab() With DBTablesPanel.
//             getSelectedTableTabPanel().
//        6.66 MyJSQLView Project Common Source Code Formatting.
//        6.67 Replaced Current TableTabPanel.getListTable.getRowCount() With
//             getValidDataRowCount() in Class Methods insert/ReplaceStatementData().
//        6.68 Constructor getDataExportProperites Changed Over to the
//             MyJSQLView_Frame Class.
//        6.69 Changed MyJSQLView_Frame.getDatabaseExportProperties() Method Moved
//             Over to the DBTablesPanel.
//        6.70 Conditional Check for NULL dbConnection in run().
//        6.71 Removed DROP TABLE Statement Generation in run() Because It is Now
//             Handled in the TableDefinitionGenerator Class.
//        6.72 Update WebSite URL in headers String in Class Method generateHeaders().
//        6.73 Header Format Changes/Update.
//        6.74 Added Class Instance exportedTable. Provided to dumpProgressBar 
//             Construction Argument.
//        6.75 Check for SecurityException in run() for makeNewFile.delete().
//        6.76 Class Method explicitStatementData() Instance columnNameString.
//        6.77 Changed Package to Reflect Dandy Made Productions Code.
//        6.78 Class Method explicitStatementData() Instance keyStringStatement
//             Changed to Type StringBuffer. Organized imports.
//        6.79 Parameterized Class Instances tableColumnNames, tableColumnClassHashMap,
//             tableColumnTypeHashMap, & columnNamesFields. Also Method Instances
//             Class Methods insertReplaceStatementData() & explicitStatementData().
//             Brings Code Into Compliance With Java 5.0 API.
//        6.80 Parameterized Class Instance columnNamesIterator, and in Constructor.
//             Also columnNamesIterator in Class Methods insertReplaceStatementData()
//             & explicitStatementData().
//        6.81 Class Method insertReplaceStatementData() Removed Instance currentIndex.
//             Replaced by Proper Contains Assertion Integer.valueOf(i).
//        6.82 Added Support for Exporting BLOB Types for the SQLite Database. Format,
//             (x'0500'), in Class Methods insertReplaceStatement(), dumpBinaryData(),
//             & explicitStatementData().
//        6.83 Correction in run() to Properly Catch the Condition for REPLACE Explicit
//             SQL Output.
//        6.84 Removed System.out.println() in explicitStatementData().
//        6.85 Class Method run() Cast Object Returned by MyJSQLView_Access.
//             getConnection() to Connection.
//        6.86 Changes to Class Methods run(), insertReplaceStatementData(),
//             getRowsCount(), & explicitStatementData() to Use Newly Redefined
//             ConnectionManager to Obtain Connections and Display SQL Errors.
//             Also identifierQuoteString Collected From ConnectionManager. Added
//             Methods Instances connectionProperties, hostName & databaseName in
//             generateHeaders().
//        6.87 Class Methods explicitStatementData() and insertReplaceStatementData()
//             Changes to Oracle TimestampLTZ to Output in YYYY-MM-DD Format.
//        6.88 Replaced Class Instance subProtocol With dataSourceType. Intialized
//             in Constructors and Implemented in run(), insertReplaceStatementData(),
//             explicitStatementData(), & dumpBinaryData().
//        6.89 Method run() Change for SELECT to Exclude LIMIT Aspect of SQL Statement
//             for MS Access. Class Methods insertReplace/explicitStatementData()
//             Removal of Multiple Accesses of the ResultSet, rs, More Than Once
//             for Each Field. Several Accomodations for Properly Dumping Data for
//             MS Access Databases and Fix for BLOB/Binary, 0 Bytes, for Same and MySQL.
//        6.90 Code Cleanup & Minor Corrections.
//        6.91 Class Methods insert/explicitStatementData() & dumpBinaryData() Replaced
//             HSQL Conditional Check from equals to indexOf to Pickup HSQL2.
//        6.92 Change in the Format for Dump of Binary Data for HSQL2 in Class Methods
//             insertReplace/explicitStatementData(). Same Methods the Addition of
//             Processing for Bit Varying Data Types.
//        6.93 Copyright Update.
//        6.94 Removed the Casting of (Connection) for the Returned Instance for the
//             ConnectionManager.getConnection() in run().
//        6.95 Introduction of Class Instance limitIncrement. Addition of Class Method
//             Instances columnNamesString, firstField, & currentTableIncrement in
//             insertReplace/explicitStatementData(). Change in These Same Methods to
//             Force the Block Reading of Results Sets for Table Exports by limitIncrement.
//        6.96 Removed Static From Class Instance limitIncrement & Set in Constructor
//             sqlDataExportProperties.
//        6.97 Correction in insertReplace/explicitStatementData() Methods to Correctly
//             Create the sqlStatementString from MSAccess.
//        6.98 Addition of oracleColumnNamesString in insertReplace/explicitStatementData()
//             Methods to Correct Oracle SELECT Query. Un-quoting Numeric Fields in Same
//             Methods.
//        6.99 Obtained limitIncrement From GeneralProperties Instead of sqlExportOptions
//             in Constructor.
//        7.00 Added Class Method dumpData() to be Called From run() So That IO & SQL
//             Exceptions Can be Caught With Closing filebuff and sqlStatement in Finally
//             Clauses. Same finally for sqlStatement in insertReplace/explicitStatementData()
//             Methods & getRowsCount().
//        7.01 Introduced Class Instance pluralValueLimit & Used to Constrol the Number
//             of Values to Export During Insert & Replace Plural Dumps in Class Method
//             insertReplaceStatementData().
//        7.02 Class Instance columnNameFields Changed from Vector Data Type to ArrayList.
//             Same for columnNameFields in Method dumpDatabaseData() & indexes/keys in 
//             insertReplace/explicitStatementData().
//        7.03 Closure for ResultSet, rs, in dumpData() & getRowCount() Moved to finally.
//        7.04 Changed Package Name to com.dandymadeproductions.myjsqlview.io.
//             Made Class, Constructor & Method generateHeaders() & genCommentsSep()
//             Public.
//        7.05 Created Clone of Argument myJSQLView_Version in Both Constructors for Local
//             Same Class Instance. Removed the Starting of Thread From Constructor.
//        7.06 Method dumpData() Change for SELECT to Properly Format LIMIT Aspect of SQL
//             Statement for Apache Derby. Class Methods insertReplace/explicitStatementData()
//             Changes to Properly Implement Derby BIT DATA, BLOB, AutoIncrement, & Current
//             TimeStamps Features. Added Argument boolean derbyBit to Method dumpBinaryData()
//             & Use Therein to Handle Derby BIT DATA & BLOB Types.
//        7.07 Change in Constructor to Use DBTablePanel.getGeneralDBProperties().
//        7.08 Additions in Methods insert/explicitStatementData() & dumpBinaryData()
//             to Handle H2 Database Blob Hexidecimal Data Output."
//        7.09 Class Method dumpData() Inclusion of a finally Clause for Insuring Instances
//             fileStream & filebuff Get Closed on IOException.
//        7.10 Conversion of StringBuffer TableTabPanel.getTableSQLStatement().toString()
//             in insertReplace/explicitStatementData() Methods.
//        7.11 Methods dumpData(), & insertReplace/explicitStatementData() Query Update for
//             MSSQL. Latter Methods Proper Handling of Bit & Timestamp Also for MSSQL.
//        7.12 Commented Out a System.out in insertReplaceStatementData(). Proper Handling
//             of MSSQL Identity Fields When SQL Export Option AutoIncrement Set in Method
//             insertReplaceStatementData().
//        7.13 Method dumpData() Inclusion of LOCK & UNLOCK Statements for MariaDB.
//        7.14 Methods insertReplace/explicitStatementData() Update for Oracle 11 in Processing
//             Types TIMESTAMP WITH TIME ZONE, & TIMESTAMP WITH LOCAL TIME ZONE.
//        7.15 Methods insertReplace/explicitStatementData() Update for SQLite in Processing
//             TIMESTAMP Fields As Defined by Column Type.
//             
//-----------------------------------------------------------------
//                poisonerbg@users.sourceforge.net
//                danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JOptionPane;

import com.dandymadeproductions.myjsqlview.MyJSQLView;
import com.dandymadeproductions.myjsqlview.datasource.ConnectionManager;
import com.dandymadeproductions.myjsqlview.datasource.ConnectionProperties;
import com.dandymadeproductions.myjsqlview.gui.panels.DBTablesPanel;
import com.dandymadeproductions.myjsqlview.structures.DataExportProperties;
import com.dandymadeproductions.myjsqlview.utilities.MyJSQLView_ProgressBar;
import com.dandymadeproductions.myjsqlview.utilities.TableDefinitionGenerator;

/**
 *    The SQLDataDumpThread provides a thread to safely dump database
 * table data to a local file in SQL format. A status dialog with
 * cancel is provided to provide the ability to prematurely terminate
 * the dump.
 * 
 * @author Borislav Gizdov a.k.a. PoisoneR, Dana Proctor
 * @version 7.145 05/10/2015
 */

public class SQLDataDumpThread implements Runnable
{
   // Class Instances.
   private Object dumpData;
   private String exportedTable;
   private ArrayList<String> columnNameFields;
   private HashMap<String, String> tableColumnNames;
   private HashMap<String, String> tableColumnClassHashMap;
   private HashMap<String, String> tableColumnTypeHashMap;
   private String fileName;
   private String dataSourceType, schemaName, tableName;
   private String dbSchemaTableName, schemaTableName;
   private String dbIdentifierQuoteString, identifierQuoteString;
   private String[] myJSQLView_Version;

   private boolean limits, insertReplaceDump, updateDump;
   private int limitIncrement, pluralValueLimit;
   private DataExportProperties sqlDataExportOptions;
   private BufferedOutputStream filebuff;
   private MyJSQLView_ProgressBar dumpProgressBar;

   //==============================================================
   // SQLDataDumpThread Constructor.
   //==============================================================

   public SQLDataDumpThread(String[] myJSQLView_Version)
   {
      this.myJSQLView_Version = myJSQLView_Version.clone();

      // Setup export options & identifer String.
      dataSourceType = ConnectionManager.getDataSourceType();
      dbIdentifierQuoteString = ConnectionManager.getIdentifierQuoteString();
      sqlDataExportOptions = DBTablesPanel.getDataExportProperties();
      identifierQuoteString = sqlDataExportOptions.getIdentifierQuoteString();
      if (sqlDataExportOptions.getInsertReplaceUpdate().equals("Insert"))
         pluralValueLimit = sqlDataExportOptions.getInsertPluralSize();
      else
         pluralValueLimit = sqlDataExportOptions.getReplacePluralSize();
   }

   //==============================================================
   // SQLDataDumpThread Constructor.
   //==============================================================

   public SQLDataDumpThread(ArrayList<String> columnNameFields, HashMap<String, String> tableColumnNames,
                            boolean limits, HashMap<String, String> tableColumnClassHashMap,
                            HashMap<String, String> tableColumnTypeHashMap, String exportedTable,
                            String fileName, String[] myJSQLView_Version)
   {
      this.columnNameFields = columnNameFields;
      this.tableColumnNames = tableColumnNames;
      this.limits = limits;
      this.tableColumnClassHashMap = tableColumnClassHashMap;
      this.tableColumnTypeHashMap = tableColumnTypeHashMap;
      this.myJSQLView_Version = myJSQLView_Version.clone();
      this.exportedTable = exportedTable;
      this.fileName = fileName;
      
      // Setup control instances, export options, & identifier String.
      updateDump = false;
      insertReplaceDump = false;
      dataSourceType = ConnectionManager.getDataSourceType();
      dbIdentifierQuoteString = ConnectionManager.getIdentifierQuoteString();
      sqlDataExportOptions = DBTablesPanel.getDataExportProperties();
      identifierQuoteString = sqlDataExportOptions.getIdentifierQuoteString();
      if (sqlDataExportOptions.getInsertReplaceUpdate().equals("Insert"))
         pluralValueLimit = sqlDataExportOptions.getInsertPluralSize();
      else
         pluralValueLimit = sqlDataExportOptions.getReplacePluralSize();
      limitIncrement = DBTablesPanel.getGeneralDBProperties().getLimitIncrement();

      // Create the appropriate SQL table name qualifier.
      if (exportedTable.indexOf(".") != -1)
      {
         dbSchemaTableName = dbIdentifierQuoteString 
                             + exportedTable.substring(0, exportedTable.indexOf("."))
                             + dbIdentifierQuoteString + "." + dbIdentifierQuoteString
                             + exportedTable.substring(exportedTable.indexOf(".") + 1)
                             + dbIdentifierQuoteString;
         schemaTableName = identifierQuoteString + exportedTable.substring(0, exportedTable.indexOf("."))
                           + identifierQuoteString + "." + identifierQuoteString
                           + exportedTable.substring(exportedTable.indexOf(".") + 1) + identifierQuoteString;
      }
      else
      {
         dbSchemaTableName = dbIdentifierQuoteString + exportedTable + dbIdentifierQuoteString;
         schemaTableName = identifierQuoteString + exportedTable + identifierQuoteString;
      }
   }

   //==============================================================
   // Class method for normal start of the thread
   //==============================================================

   public void run()
   {
      try
      {
         dumpData();
      }
      catch (Exception e)
      {
         // Failed to close resource.
      }
   }
   
   //==============================================================
   // Class method to handle the data dump.
   //==============================================================
   
   private void dumpData() throws Exception
   {
      // Class Method Instances.
      FileOutputStream fileStream;
      Statement sqlStatement;
      ResultSet rs;

      // Get Connection to Database.
      Connection dbConnection = ConnectionManager.getConnection("SQLDataDumpThread run()");
      
      if (dbConnection == null)
         return;
      
      fileStream = null;
      filebuff = null;

      // Setting up OutputStream
      try
      {
         File makeNewFile = new File(fileName);
         if (makeNewFile.exists())
         {
            try
            {
               makeNewFile.delete();
            }
            catch (SecurityException se){}
         }
         fileStream = new FileOutputStream(fileName, true);
         filebuff = new BufferedOutputStream(fileStream);

         // Create a progress bar for giving the user a
         // visual and cancel ability.
         dumpProgressBar = new MyJSQLView_ProgressBar(exportedTable + " SQL Data Dump");

         // =========================================================
         // Begin creating the data characters to be dumped into
         // the selected file.

         // Header info.
         dumpData = generateHeaders();

         // Check & see if anything is going to be exported
         if (!sqlDataExportOptions.getTableStructure() && !sqlDataExportOptions.getTableData())
         {
            dumpChunkOfData(dumpData);
            dumpProgressBar.dispose();
            ConnectionManager.closeConnection(dbConnection, "SQLDataDumpThread run()");
            return;
         }

         // Drop and Create Table Statements As Needed.
         if (sqlDataExportOptions.getTableStructure())
         {
            dumpData = dumpData + genCommentSep("Table structure for table " + schemaTableName);

            dumpData = dumpData
                       + (new TableDefinitionGenerator(dbConnection, dbSchemaTableName)).getTableDefinition();

            // Check to see if we need to proceed with dumping
            // data. If not dump structure and clean up.

            if (!sqlDataExportOptions.getTableData())
            {
               dumpChunkOfData(dumpData);
               dumpProgressBar.dispose();
               ConnectionManager.closeConnection(dbConnection, "SQLDataDumpThread run()");
               return;
            }
         }

         // Comments for Table.
         dumpData = dumpData + genCommentSep("Dumping data for table " + schemaTableName);

         // Check to see if there is any data to actually be
         // dumped from the table and proceeding as necesary.
         
         sqlStatement = null;
         rs = null;
         
         try
         {
            sqlStatement = dbConnection.createStatement();

            if (dataSourceType.equals(ConnectionManager.ORACLE))
               rs = sqlStatement.executeQuery("SELECT * FROM " + dbSchemaTableName + " WHERE ROWNUM=1");
            else if (dataSourceType.equals(ConnectionManager.MSACCESS))
               rs = sqlStatement.executeQuery("SELECT * FROM " + dbSchemaTableName + " AS t");
            else if (dataSourceType.equals(ConnectionManager.MSSQL))
               rs = sqlStatement.executeQuery("SELECT TOP 1 * FROM " + dbSchemaTableName + " AS t");
            else if (dataSourceType.equals(ConnectionManager.DERBY))
               rs = sqlStatement.executeQuery("SELECT * FROM " + schemaTableName 
                                              + " AS t FETCH FIRST ROW ONLY");
            else
               rs = sqlStatement.executeQuery("SELECT * FROM " + dbSchemaTableName + " AS t LIMIT 1");

            if (rs.next())
            {
               // Lock.
               if (sqlDataExportOptions.getLock())
               {
                  if (dataSourceType.equals(ConnectionManager.MYSQL)
                      || dataSourceType.equals(ConnectionManager.MARIADB))
                  {
                     dumpData = dumpData + ("/*!40000 ALTER TABLE " 
                                + schemaTableName + " DISABLE KEYS */;\n");
                     dumpData = dumpData + ("LOCK TABLES " + schemaTableName + " WRITE;\n");
                  }
                  else if (dataSourceType.equals(ConnectionManager.POSTGRESQL))
                     dumpData = dumpData + ("LOCK TABLE " + schemaTableName + ";\n");
               }

               // Create the Appropriate Insert,Replace or Update Statements
               // with data as needed.

               // Insert
               if (sqlDataExportOptions.getInsertReplaceUpdate().equals("Insert"))
               {
                  if (sqlDataExportOptions.getInsertExpression().equals("Explicit"))
                     explicitStatementData(dbConnection);
                  else
                     insertReplaceStatementData(dbConnection);     
               }
               // Replace
               else if (sqlDataExportOptions.getInsertReplaceUpdate().equals("Replace"))
               {
                  if (sqlDataExportOptions.getReplaceExpression().equals("Explicit"))
                     explicitStatementData(dbConnection);
                  else
                     insertReplaceStatementData(dbConnection);     
               }
               // Update
               else
                  explicitStatementData(dbConnection);
               
               dumpData = dumpData + ";\n";

               // Finishing up.
               if (sqlDataExportOptions.getLock())
               {
                  if (dataSourceType.equals(ConnectionManager.MYSQL)
                      || dataSourceType.equals(ConnectionManager.MARIADB))
                  {
                     dumpData = dumpData + "UNLOCK TABLES;\n";
                     dumpData = dumpData + "/*!40000 ALTER TABLE " + schemaTableName 
                                + " ENABLE KEYS */;\n";
                  }
               }
            }
         }
         catch (SQLException e)
         {
            ConnectionManager.displaySQLErrors(e, "SQLDataDumpThread run()");
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
               ConnectionManager.displaySQLErrors(sqle, "SQLDataDumpThread run()");
            }
            finally
            {
               if (sqlStatement != null)
                  sqlStatement.close();
            }
         }
         dumpChunkOfData(dumpData);
         dumpData = "";

         ConnectionManager.closeConnection(dbConnection, "SQLDataDumpThread run()");

         if (dumpProgressBar.isCanceled())
         {
            try
            {
               makeNewFile.delete();
            }
            catch (SecurityException se){}
         }
         dumpProgressBar.dispose();
      }
      catch (IOException e)
      {
         String msg = "Unable to Create filestream for: '" + fileName + "'.";
         JOptionPane.showMessageDialog(null, msg, fileName, JOptionPane.ERROR_MESSAGE);
         return;
      }
      finally
      {
         try
         {
            if (filebuff != null)
               filebuff.close();
         }
         catch (IOException ioe)
         {
            if (MyJSQLView.getDebug())
               System.out.println("SQLDataDumpThread dumpData() Failed to Close BufferedOutputStream. "
                                  + ioe);
         }
         finally
         {
            try
            {
               if (fileStream != null)
                  fileStream.close();
            }
            catch (IOException ioe)
            {
               if (MyJSQLView.getDebug())
                  System.out.println("SQLDataDumpThread dumpData() Failed to Close FileStream. "
                                     + ioe);
            }
         } 
      }
   }
   
   //==============================================================
   // Class method to create the insert/replace statement and data.
   //==============================================================

   private void insertReplaceStatementData(Connection dbConnection) throws SQLException
   {
      // Class Method Instances
      StringBuffer columnNamesString;
      StringBuffer oracleColumnNamesString;
      Iterator<String> columnNamesIterator;
      HashMap<Integer, String> autoIncrementFieldIndexes;
      ArrayList<Integer> blobFieldIndexes;
      ArrayList<Integer> bitFieldIndexes;
      ArrayList<Integer> timeStampIndexes;
      ArrayList<Integer> oracleTimeStamp_TZIndexes;
      ArrayList<Integer> oracleTimeStamp_LTZIndexes;
      ArrayList<Integer> dateIndexes;
      ArrayList<Integer> yearIndexes;
      ArrayList<Integer> arrayIndexes;
      ArrayList<Integer> numericIndexes;
      String field, columnClass, columnType;
      String firstField, sqlFieldValuesString;
      String expressionType;
      int rowsCount, currentTableIncrement, currentRow;
      int currentPluralValueCount;
      int columnsCount;

      String sqlStatementString;
      Statement sqlStatement;
      ResultSet rs;

      // Setting up the initial dump data string with insert/replace, type,
      // and table.

      insertReplaceDump = true;
      sqlFieldValuesString = (sqlDataExportOptions.getInsertReplaceUpdate().toUpperCase()
                              + sqlDataExportOptions.getType().toUpperCase() 
                              + "INTO " + schemaTableName + " (");

      if (sqlDataExportOptions.getInsertReplaceUpdate().toUpperCase().equals("INSERT"))
         expressionType = sqlDataExportOptions.getInsertExpression();
      else
         expressionType = sqlDataExportOptions.getReplaceExpression();

      // Obtain the table fields and obtain list of specific
      // fields that need special handling.

      columnsCount = 0;
      columnNamesString = new StringBuffer();
      oracleColumnNamesString = new StringBuffer();
      columnNamesIterator = columnNameFields.iterator();
      autoIncrementFieldIndexes = new HashMap <Integer, String>();
      blobFieldIndexes = new ArrayList <Integer>();
      bitFieldIndexes = new ArrayList <Integer>();
      timeStampIndexes = new ArrayList <Integer>();
      oracleTimeStamp_TZIndexes = new ArrayList <Integer>();
      oracleTimeStamp_LTZIndexes = new ArrayList <Integer>();
      dateIndexes = new ArrayList <Integer>();
      yearIndexes = new ArrayList <Integer>();
      arrayIndexes = new ArrayList <Integer>();
      numericIndexes = new ArrayList <Integer>();

      while (columnNamesIterator.hasNext())
      {
         field = columnNamesIterator.next();
         columnClass = tableColumnClassHashMap.get(field);
         columnType = tableColumnTypeHashMap.get(field);
         // System.out.println("field:" + field + " class:" + columnClass +
         //                   "type:" + columnType);

         // Save the index of autoIncrement entries.
         if (DBTablesPanel.getSelectedTableTabPanel().getAutoIncrementHashMap().containsKey(field))
         {
            if (dataSourceType.equals(ConnectionManager.ORACLE))
               autoIncrementFieldIndexes.put(Integer.valueOf(columnsCount + 1),
                                             DBTablesPanel.getSelectedTableTabPanel().getAutoIncrementHashMap().get(field));
            else if (dataSourceType.equals(ConnectionManager.MSSQL)
                     && sqlDataExportOptions.getAutoIncrement())
               continue;
            else
               autoIncrementFieldIndexes.put(Integer.valueOf(columnsCount + 1), tableColumnNames.get(field));
         }

         // Save the index of blob/bytea/binary/image/raw entries.
         if ((columnClass.indexOf("String") == -1 && columnType.indexOf("BLOB") != -1) ||
             (columnClass.indexOf("BLOB") != -1 && columnType.indexOf("BLOB") != -1) ||
             (columnType.indexOf("BYTEA") != -1) || (columnType.indexOf("BINARY") != -1) ||
             (columnType.indexOf("IMAGE")!= -1) || (columnType.indexOf("RAW") != -1))
         {
            blobFieldIndexes.add(Integer.valueOf(columnsCount + 1));
         }

         // Save the index of bit entries.
         if (columnType.indexOf("BIT") != -1)
         {
            if (!dataSourceType.equals(ConnectionManager.MSACCESS)
                && !dataSourceType.equals(ConnectionManager.MSSQL))
               bitFieldIndexes.add(Integer.valueOf(columnsCount + 1));
         }

         // Save the index of TimeStamp Fields.
         if (columnType.indexOf("TIMESTAMP") != -1)
         {
            if (!dataSourceType.equals(ConnectionManager.MSSQL))
               timeStampIndexes.add(Integer.valueOf(columnsCount + 1));
         }

         // Save the index of Oracle TimeStamp(TZ) Fields.
         if (dataSourceType.equals(ConnectionManager.ORACLE) &&
             (columnType.equals("TIMESTAMP") || columnType.equals("TIMESTAMPTZ")
              || columnType.equals("TIMESTAMP WITH TIME ZONE")
              || columnType.equals("TIMESTAMP WITH LOCAL TIME ZONE")))
         {
            oracleTimeStamp_TZIndexes.add(Integer.valueOf(columnsCount + 1));
         }

         // Save the index of Oracle TimeStamp(LTZ) Fields.
         if (dataSourceType.equals(ConnectionManager.ORACLE) &&
             columnType.equals("TIMESTAMPLTZ"))
         {
            oracleTimeStamp_LTZIndexes.add(Integer.valueOf(columnsCount + 1));
         }

         // Save the index of date entries.
         if (columnType.equals("DATE"))
         {
            dateIndexes.add(Integer.valueOf(columnsCount + 1));
         }

         // Save the index of year entries.
         if (columnType.indexOf("YEAR") != -1)
         {
            yearIndexes.add(Integer.valueOf(columnsCount + 1));
         }

         // Save the index of array entries.
         if (columnType.indexOf("_") != -1)
         {
            arrayIndexes.add(Integer.valueOf(columnsCount + 1));
         }
         
         // Save the index of numeric entries.
         if (columnClass.indexOf("Integer") != -1 || columnClass.indexOf("Long") != -1
             || columnClass.indexOf("Float") != -1 || columnClass.indexOf("Double") != -1
             || (columnClass.indexOf("Byte") != -1 && columnType.indexOf("CHAR") == -1)
             || columnClass.indexOf("BigDecimal") != -1 || columnClass.indexOf("Short") != -1)
         {
            numericIndexes.add(Integer.valueOf(columnsCount + 1));
         }

         // Modify Statement as needed for Oracle TIMESTAMPLTZ Fields.
         if (dataSourceType.equals(ConnectionManager.ORACLE) &&
             columnType.equals("TIMESTAMPLTZ"))
         {
            oracleColumnNamesString.append("TO_CHAR(" + dbIdentifierQuoteString + tableColumnNames.get(field)
                                           + dbIdentifierQuoteString + ", 'YYYY-MM-DD HH24:MM:SS TZR') AS "
                                           + dbIdentifierQuoteString + tableColumnNames.get(field)
                                           + dbIdentifierQuoteString + ", ");
         }
         else
            oracleColumnNamesString.append(dbIdentifierQuoteString + tableColumnNames.get(field)
                                           + dbIdentifierQuoteString + ", ");
         // Unmodified Names.
         columnNamesString.append(dbIdentifierQuoteString + tableColumnNames.get(field)
                                  + dbIdentifierQuoteString + ", ");
         sqlFieldValuesString += (identifierQuoteString + tableColumnNames.get(field) 
                                  + identifierQuoteString + ", ");

         columnsCount++;
      }
      oracleColumnNamesString.delete((oracleColumnNamesString.length() - 2),
                                      oracleColumnNamesString.length());
      columnNamesString.delete((columnNamesString.length() - 2), columnNamesString.length());
      firstField = columnNamesString.substring(0, columnNamesString.indexOf(","));

      // Do an initial dump of data created so far.

      sqlFieldValuesString = sqlFieldValuesString.substring(0, sqlFieldValuesString.length() - 2);
      sqlFieldValuesString += ") VALUES";
      dumpData = dumpData + sqlFieldValuesString;

      dumpChunkOfData(dumpData);
      dumpData = "";
      
      // Collect the row count of the table and setting
      // up a progress bar for tracking/canceling.
      
      rowsCount = 0;
      
      if (limits)
         rowsCount = DBTablesPanel.getSelectedTableTabPanel().getValidDataRowCount();
      else
      {
         try
         {
            rowsCount = getRowsCount(dbConnection, dbSchemaTableName);
         }
         catch (SQLException sqle)
         {
            ConnectionManager.displaySQLErrors(sqle, "SQLDataDumpThread insertReplaceStatementData()");
         }
      }
      
      currentPluralValueCount = 0;
      currentTableIncrement = 0;
      currentRow = 0;

      // Start a progress bar for tracking/canceling.
      dumpProgressBar.setTaskLength(rowsCount);
      dumpProgressBar.pack();
      dumpProgressBar.center();
      dumpProgressBar.setVisible(true);
      
      // Ok now ready so beginning by connecting to database for
      // data and proceeding with building the dump data.
      
      sqlStatement = null;
      
      try
      {
         sqlStatement = dbConnection.createStatement();

         // Setting up to begin insert statements.
         do
         {
            // Finishing creating the Select statement to retrieve data.
            if (limits)
               sqlStatementString = DBTablesPanel.getSelectedTableTabPanel().getTableSQLStatement().toString();
            else
            {
               // Oracle
               if (dataSourceType.equals(ConnectionManager.ORACLE))
                  sqlStatementString = "SELECT " + columnNamesString.toString() + " FROM "
                                       + "(SELECT ROW_NUMBER() OVER (ORDER BY " + firstField + " ASC) " 
                                       + "AS dmprownumber, " + oracleColumnNamesString.toString() + " "
                                       + "FROM " + dbSchemaTableName + ") " + "WHERE dmprownumber BETWEEN "
                                       + (currentTableIncrement + 1) + " AND " + (currentTableIncrement
                                       + limitIncrement);
               // MSAccess
               else if (dataSourceType.equals(ConnectionManager.MSACCESS))
                  sqlStatementString = "SELECT " + columnNamesString.toString() + " FROM "
                                        + dbSchemaTableName;
               // MSSQL
               else if (dataSourceType.equals(ConnectionManager.MSSQL))
               {
                  sqlStatementString = "SELECT " + columnNamesString.toString() + " FROM "
                                       + "(SELECT *, ROW_NUMBER() OVER (ORDER BY " + firstField + " ASC) "
                                       + "AS dmprownumber FROM " + schemaTableName + " AS t) AS t1 "
                                       + "WHERE t1.dmprownumber BETWEEN "
                                       + (currentTableIncrement + 1) + " AND " + (currentTableIncrement
                                             + limitIncrement);
               }
               // Derby
               else if (dataSourceType.equals(ConnectionManager.DERBY))
                  sqlStatementString = "SELECT " + columnNamesString.toString() + " FROM "
                                       + schemaTableName + " OFFSET " + currentTableIncrement + " ROWS "
                                       + "FETCH NEXT " + limitIncrement + " ROWS ONLY";
               else
                  sqlStatementString = "SELECT " + columnNamesString.toString() + " FROM "
                                       + dbSchemaTableName + " LIMIT " + limitIncrement + " OFFSET "
                                       + currentTableIncrement;
            }
            // System.out.println(sqlStatementString);
            
            rs = sqlStatement.executeQuery(sqlStatementString);
            
            // Begin the creation of insert statements.
            while (rs.next() && !dumpProgressBar.isCanceled())
            {
               dumpProgressBar.setCurrentValue(currentRow++);
               currentPluralValueCount++;

               // SQL Singular Statement
               if (expressionType.equals("Singular"))
                  dumpData = dumpData + "(";
               // SQL Plural Statement
               else
                  dumpData = dumpData + ("\n(");

               for (int i = 1; i <= columnsCount; i++)
               {
                  // System.out.print(i + " ");

                  // Determining binary types and acting appropriately.
                  if (blobFieldIndexes.contains(Integer.valueOf(i)))
                  {
                     byte[] theBytes = rs.getBytes(i);

                     if (theBytes != null)
                     {
                        if (dataSourceType.equals(ConnectionManager.POSTGRESQL))
                           dumpData = dumpData + "E'";
                        else if (dataSourceType.equals(ConnectionManager.HSQL))
                           dumpData = dumpData + "'";
                        else if (dataSourceType.equals(ConnectionManager.HSQL2))
                           dumpData = dumpData + "x'";
                        else if (dataSourceType.equals(ConnectionManager.ORACLE))
                           dumpData = dumpData + "HEXTORAW('";
                        else if (dataSourceType.equals(ConnectionManager.SQLITE))
                           dumpData = dumpData + "x'";
                        else if (dataSourceType.equals(ConnectionManager.DERBY))
                           dumpData = dumpData + "CAST(X'";
                        else if (dataSourceType.equals(ConnectionManager.H2))
                           dumpData = dumpData + "x'";
                        else
                        {
                           if (theBytes.length != 0)
                              dumpData = dumpData + "0x";
                           else
                              dumpData = dumpData + "''";
                        }

                        // Go convert to hexadecimal/octal values
                        // and dump data as we go for blob/bytea.
                        dumpBinaryData(theBytes, false);
                     }
                     else
                        dumpData = dumpData + "NULL, ";
                  }
                  // Regular Fields
                  else
                  {
                     // Check for an AutoIncrement
                     if (autoIncrementFieldIndexes.containsKey(Integer.valueOf(i))
                         && sqlDataExportOptions.getAutoIncrement())
                     {
                        if (dataSourceType.equals(ConnectionManager.POSTGRESQL))
                        {
                           schemaName = schemaTableName.substring(0, schemaTableName.indexOf(".") + 2);
                           tableName = (schemaTableName.substring(schemaTableName.indexOf(".") + 1)).replaceAll(
                                                                  identifierQuoteString, "");

                           dumpData = dumpData + "nextval('" + schemaName + tableName + "_"
                                      + autoIncrementFieldIndexes.get(Integer.valueOf(i)) + "_seq\"'), ";
                        }
                        else if (dataSourceType.equals(ConnectionManager.ORACLE))
                        {
                           dumpData = dumpData + identifierQuoteString
                                      + autoIncrementFieldIndexes.get(Integer.valueOf(i)) 
                                      + identifierQuoteString + ".NEXTVAL, ";
                        }
                        else if (dataSourceType.equals(ConnectionManager.DERBY))
                           dumpData = dumpData + "DEFAULT, ";
                        else
                           dumpData = dumpData + "NULL, ";
                     }
                     else
                     {
                        // Check for a TimeStamp
                        if (timeStampIndexes.contains(Integer.valueOf(i)) && sqlDataExportOptions.getTimeStamp())
                        {
                           if (arrayIndexes.contains(Integer.valueOf(i)))
                              dumpData = dumpData + "'{NOW()}', ";
                           else
                           {
                              if (dataSourceType.equals(ConnectionManager.ORACLE))
                                 dumpData = dumpData + "SYSTIMESTAMP, ";
                              else if (dataSourceType.equals(ConnectionManager.DERBY))
                                 dumpData = dumpData + "CURRENT_TIMESTAMP, ";
                              else if (dataSourceType.equals(ConnectionManager.SQLITE))
                                 dumpData = dumpData + "STRFTIME('%Y-%m-%d %H:%M:%S.%f', 'now', 'localtime'), ";
                              else
                                 dumpData = dumpData + "NOW(), ";
                           }
                        }
                        
                        // SQLite Timestamp
                        else if (timeStampIndexes.contains(Integer.valueOf(i))
                                 && dataSourceType.equals(ConnectionManager.SQLITE))
                        {
                           java.sql.Timestamp timestampValue = rs.getTimestamp(i);
                           
                           if (timestampValue != null)
                              dumpData = dumpData + ("'" + timestampValue + "', ");
                           else
                              dumpData = dumpData + "NULL, ";
                        }

                        // Check for Oracle TimeStamp(TZ)
                        else if (oracleTimeStamp_TZIndexes.contains(Integer.valueOf(i))
                                 && !sqlDataExportOptions.getTimeStamp())
                        {
                           Object currentData = rs.getTimestamp(i);
                           
                           if (currentData != null)
                              dumpData = dumpData + "TO_TIMESTAMP('" + currentData
                                         + "', 'YYYY-MM-DD HH24:MI:SS:FF'), ";
                           else
                              dumpData = dumpData + "NULL, ";
                        }

                        // Check for a Date
                        else if (dateIndexes.contains(Integer.valueOf(i)))
                        {
                           if (dataSourceType.equals(ConnectionManager.ORACLE))
                           {
                              java.sql.Date dateValue = rs.getDate(i);
                              
                              if (dateValue != null)
                                 dumpData = dumpData + "TO_DATE('" + dateValue + "', 'YYYY-MM-DD'), ";
                              else
                                 dumpData = dumpData + "NULL, ";   
                           }
                           else
                           {
                              String dateString = rs.getString(i);
                              
                              if (dateString != null)
                                 dumpData = dumpData + "'" + addEscapes(dateString) + "', ";
                              else
                                 dumpData = dumpData + "NULL, ";    
                           }
                        }

                        // Check for a Year
                        else if (yearIndexes.contains(Integer.valueOf(i)))
                        {
                           // Fix for a bug in connectorJ, I think, that returns
                           // a whole date YYYY-MM-DD. Don't know what else
                           // to do it hangs my imports, but works with
                           // mysql console.
                           
                           String yearValue = rs.getString(i);
                           
                           if (yearValue != null)
                           {
                              if (yearValue.length() > 4)
                                 dumpData = dumpData + "'" + addEscapes(yearValue.substring(0, 4)) + "', ";
                              else
                                 dumpData = dumpData + "'" + addEscapes(yearValue) + "', ";
                           }
                           else
                              dumpData = dumpData + "NULL, ";
                        }

                        // Check for Bit fields.
                        else if (bitFieldIndexes.contains(Integer.valueOf(i)))
                        {
                           String bitValue = rs.getString(i);
                           
                           if (bitValue != null)
                           {
                              if (dataSourceType.equals(ConnectionManager.POSTGRESQL)
                                  || dataSourceType.equals(ConnectionManager.HSQL2))
                              {
                                 if (arrayIndexes.contains(Integer.valueOf(i)))
                                    dumpData = dumpData + "'" + bitValue + "', ";
                                 else
                                    dumpData = dumpData + "B'" + bitValue + "', ";
                              }
                              else if (dataSourceType.equals(ConnectionManager.DERBY))
                              {
                                 dumpData = dumpData + "X'";
                                 dumpBinaryData(rs.getBytes(i), true);
                              }
                              else
                              {
                                 try
                                 {
                                    dumpData = dumpData + "B'"
                                               + Integer.toBinaryString(Integer.parseInt(bitValue))
                                               + "', ";
                                 }
                                 catch (NumberFormatException e)
                                 {
                                    dumpData = dumpData + "B'0', ";
                                 }
                              }
                           }
                           else
                              dumpData = dumpData + "NULL, ";
                        }

                        // All other fields
                        else
                        {
                           // Do not remove. Oracle LONG Types, which are
                           // processed here, only alows the resultSet get once.

                           String contentString = rs.getString(i);

                           if (contentString != null)
                           {
                              // Check for Oracle TimeStampLTZ
                              if (oracleTimeStamp_LTZIndexes.contains(Integer.valueOf(i)) &&
                                  !sqlDataExportOptions.getTimeStamp())
                                 dumpData = dumpData + "TO_TIMESTAMP_TZ('" + contentString
                                            + "', 'YYYY-MM-DD HH24:MI:SS TZH:TZM'), ";
                              // Don't Quote Numeric Values.
                              else if (numericIndexes.contains(Integer.valueOf(i))) 
                                 dumpData = dumpData + contentString + ", ";
                              else 
                                 dumpData = dumpData + "'" + addEscapes(contentString) + "', ";
                           }
                           else
                              dumpData = dumpData + "NULL, ";
                        }
                     }
                  }
               }
               dumpData = ((String) dumpData).substring(0, ((String) dumpData).length() - 2);

               // SQL Singular Statement
               if (expressionType.equals("Singular"))
                  dumpData = dumpData + ");\n";
               // SQL Plural Statement
               else
               {
                  if (currentPluralValueCount >= pluralValueLimit)
                     dumpData = dumpData + ");\n";
                  else
                     dumpData = dumpData + "),";
               }

               if (currentRow >= rowsCount)
               {
                  // SQL Singular Statement
                  if (expressionType.equals("Singular"))
                     dumpData = ((String) dumpData).substring(0, ((String) dumpData).length() - 2);
                  // SQL Plural Statement
                  else
                  {
                     if (currentPluralValueCount >= pluralValueLimit)
                        dumpData = ((String) dumpData).substring(0, ((String) dumpData).length() - 2);
                     else
                        dumpData = ((String) dumpData).substring(0, ((String) dumpData).length() - 1);
                  }
                  dumpChunkOfData(dumpData);
                  dumpData = "";
                  currentRow = 0;
               }
               else
               {
                  dumpChunkOfData(dumpData);
                  // SQL Singular Statement Resetup
                  if (expressionType.equals("Singular"))
                     dumpData = sqlFieldValuesString;
                  // SQL Plural Statement
                  else
                  {
                     if (currentPluralValueCount >= pluralValueLimit)
                     {
                        dumpData = sqlFieldValuesString;
                        currentPluralValueCount = 0;
                     }
                     else
                        dumpData = "";
                  }
               }
            }
            currentTableIncrement += limitIncrement;
         }
         while (!limits && currentTableIncrement < rowsCount && !dumpProgressBar.isCanceled());

         // Closing out
         rs.close();
         dumpProgressBar.dispose();
      }
      catch (SQLException e)
      {
         dumpProgressBar.setCanceled(true);
         ConnectionManager.displaySQLErrors(e, "SQLDataDumpThread insertReplaceStatementData()");
      }
      finally
      {
         if (sqlStatement != null)
            sqlStatement.close();
      }
   }

   //==============================================================
   // Class method to create the explicit or update statement and
   // data.
   //==============================================================

   private void explicitStatementData(Connection dbConnection) throws SQLException
   {
      // Class Method Instances
      StringBuffer columnNamesString;
      StringBuffer oracleColumnNamesString;
      Iterator<String> columnNamesIterator;
      String field, columnClass, columnType;
      String firstField;
      
      ArrayList<String> keys;
      StringBuffer keyStringStatement;
      int rowsCount, currentTableIncrement, currentRow;

      String sqlStatementString;
      Statement sqlStatement;
      ResultSet rs;

      // Setting up for possible update dump.

      keys = new ArrayList <String>();
      updateDump = false;
      keyStringStatement = new StringBuffer();
      keyStringStatement.append(" WHERE ");

      // Setting up the initial dump data string with insert/replace/update,
      // type, and table.
      
      dumpData = dumpData + sqlDataExportOptions.getInsertReplaceUpdate().toUpperCase();
      dumpData = dumpData + sqlDataExportOptions.getType().toUpperCase();

      // Explicit
      if (sqlDataExportOptions.getInsertReplaceUpdate().toUpperCase().equals("INSERT") ||
          sqlDataExportOptions.getInsertReplaceUpdate().toUpperCase().equals("REPLACE"))
         dumpData = dumpData + "INTO ";
      // Update
      else
      {
         updateDump = true;
         keys = DBTablesPanel.getSelectedTableTabPanel().getPrimaryKeys();
      }

      dumpData = dumpData + schemaTableName + " SET ";

      // Obtain the table fields and create select statement
      // to obtain the data.

      columnNamesString = new StringBuffer();
      oracleColumnNamesString = new StringBuffer();
      columnNamesIterator = columnNameFields.iterator();

      while (columnNamesIterator.hasNext())
      {
         field = columnNamesIterator.next();

         if (dataSourceType.equals(ConnectionManager.ORACLE)
             && (tableColumnTypeHashMap.get(field)).equals("TIMESTAMPLTZ"))
         {
            oracleColumnNamesString.append("TO_CHAR(" + dbIdentifierQuoteString + tableColumnNames.get(field)
                                           + dbIdentifierQuoteString + ", 'YYYY-MM-DD HH24:MM:SS TZR') AS "
                                           + dbIdentifierQuoteString + tableColumnNames.get(field)
                                           + dbIdentifierQuoteString + ", ");
         }
         else
            oracleColumnNamesString.append(dbIdentifierQuoteString + tableColumnNames.get(field)
                                           + dbIdentifierQuoteString + ", ");
         // Unmodified Names.
         columnNamesString.append(dbIdentifierQuoteString + tableColumnNames.get(field)
                                  + dbIdentifierQuoteString + ", ");
      }
      oracleColumnNamesString.delete((oracleColumnNamesString.length() - 2),
                                      oracleColumnNamesString.length());
      columnNamesString.delete((columnNamesString.length() - 2), columnNamesString.length());
      firstField = columnNamesString.substring(0, columnNamesString.indexOf(","));
      
      // Do an initial dump of data created so far.
      dumpChunkOfData(dumpData);
      dumpData = "";
      
      // Collect the row count of the table and setting
      // up a progress bar for tracking/canceling.
      
      rowsCount = 0;
      
      if (limits)
         rowsCount = DBTablesPanel.getSelectedTableTabPanel().getValidDataRowCount();
      else
      {
         try
         {
            rowsCount = getRowsCount(dbConnection, dbSchemaTableName);
         }
         catch (SQLException sqle)
         {
            ConnectionManager.displaySQLErrors(sqle, "SQLDataDumpThread explicitStatementData()");
         }
      }
      
      currentTableIncrement = 0;
      currentRow = 0;

      // Start a progress bar for tracking/canceling.
      dumpProgressBar.setTaskLength(rowsCount);
      dumpProgressBar.pack();
      dumpProgressBar.center();
      dumpProgressBar.setVisible(true);
      
      // Ok now ready so beginning by connecting to database for
      // data and proceeding with building the dump data.
      
      sqlStatement = null;
      
      try
      {
         sqlStatement = dbConnection.createStatement();

         // Setting up to begin update statements.
         do
         {
            if (limits)
               sqlStatementString = DBTablesPanel.getSelectedTableTabPanel().getTableSQLStatement().toString();
            else
            {
               // Oracle
               if (dataSourceType.equals(ConnectionManager.ORACLE))
                  sqlStatementString = "SELECT " + columnNamesString.toString() + " FROM "
                                       + "(SELECT ROW_NUMBER() OVER (ORDER BY " + firstField + " ASC) " 
                                       + "AS dmprownumber, " + oracleColumnNamesString.toString() + " "
                                       + "FROM " + dbSchemaTableName + ") " + "WHERE dmprownumber BETWEEN "
                                       + (currentTableIncrement + 1) + " AND " + (currentTableIncrement
                                       + limitIncrement);
               // MSAccess
               else if (dataSourceType.equals(ConnectionManager.MSACCESS))
                  sqlStatementString = "SELECT " + columnNamesString.toString() + " FROM "
                                        + dbSchemaTableName;
               // MSSQL
               else if (dataSourceType.equals(ConnectionManager.MSSQL))
               {
                  sqlStatementString = "SELECT " + columnNamesString.toString() + " FROM "
                                       + "(SELECT *, ROW_NUMBER() OVER (ORDER BY " + firstField + " ASC) "
                                       + "AS dmprownumber FROM " + schemaTableName + " AS t) AS t1 "
                                       + "WHERE t1.dmprownumber BETWEEN "
                                       + (currentTableIncrement + 1) + " AND " + (currentTableIncrement
                                             + limitIncrement);
               }
               // Derby
               else if (dataSourceType.equals(ConnectionManager.DERBY))
                  sqlStatementString = "SELECT " + columnNamesString.toString() + " FROM "
                                       + schemaTableName + " OFFSET " + currentTableIncrement + " ROWS "
                                       + "FETCH NEXT " + limitIncrement + " ROWS ONLY"; 
               else
                  sqlStatementString = "SELECT " + columnNamesString.toString() + " FROM "
                                       + dbSchemaTableName + " LIMIT " + limitIncrement + " OFFSET "
                                       + currentTableIncrement;
            }
            // System.out.println(sqlStatementString);
            
            rs = sqlStatement.executeQuery(sqlStatementString);
            
            // Begin the creation of statements.
            while (rs.next() && !dumpProgressBar.isCanceled())
            {
               dumpProgressBar.setCurrentValue(currentRow++);
               columnNamesIterator = columnNameFields.iterator();

               // Cycle through each field and set value.
               while (columnNamesIterator.hasNext())
               {
                  field = (String) columnNamesIterator.next();
                  columnClass = tableColumnClassHashMap.get(field);
                  columnType = tableColumnTypeHashMap.get(field);
                  // System.out.println("field:" + field + " class:" + columnClass
                  //                   + " type:" + columnType);

                  // Setting up WHERE Statement for Update Dump.
                  if (keys.contains(tableColumnNames.get(field)) && updateDump)
                  {
                     keyStringStatement.append(identifierQuoteString + tableColumnNames.get(field) 
                                               + identifierQuoteString + "=");
                     
                     String keyValue = rs.getString(tableColumnNames.get(field));

                     if (keyValue != null)
                     {
                        // Character data gets single quotes for some databases,
                        // not numbers though.
                        
                        if (dataSourceType.equals(ConnectionManager.MSACCESS)
                            || dataSourceType.equals(ConnectionManager.DERBY))
                        {
                           if (columnType.indexOf("CHAR") != -1 || columnType.indexOf("TEXT") != -1)
                              keyStringStatement.append("'" + keyValue + "' AND ");
                           else
                              keyStringStatement.append(keyValue + " AND ");   
                        }
                        else
                           keyStringStatement.append("'" + keyValue + "' AND ");    
                     }
                     else
                        keyStringStatement.append("NULL AND ");
                  }
                  else
                  {
                     dumpData = dumpData + identifierQuoteString + (tableColumnNames.get(field))
                                + identifierQuoteString + "=";

                     // Blob/Bytea/Binary data adding
                     if ((columnClass.indexOf("String") == -1 && columnType.indexOf("BLOB") != -1) ||
                         (columnClass.indexOf("BLOB") != -1 && columnType.indexOf("BLOB") != -1) ||
                         (columnType.indexOf("BYTEA") != -1) || (columnType.indexOf("BINARY") != -1) ||
                         (columnType.indexOf("IMAGE") != -1) || (columnType.indexOf("RAW") != -1))
                     {
                        byte[] theBytes = rs.getBytes(tableColumnNames.get(field));

                        if (theBytes != null)
                        {
                           // Let Oracle & SQLite LOBs fall through if not update
                           // since an explicit statement is not supported. Allows
                           // to convert these to MySQL compatible dump.

                           if (dataSourceType.equals(ConnectionManager.POSTGRESQL))
                              dumpData = dumpData + "E'";
                           else if (dataSourceType.equals(ConnectionManager.HSQL))
                              dumpData = dumpData + "'";
                           else if (dataSourceType.equals(ConnectionManager.HSQL2))
                              dumpData = dumpData + "x'";
                           else if (dataSourceType.equals(ConnectionManager.ORACLE) && updateDump)
                              dumpData = dumpData + "HEXTORAW('";
                           else if (dataSourceType.equals(ConnectionManager.SQLITE) && updateDump)
                              dumpData = dumpData + "x'";
                           else if (dataSourceType.equals(ConnectionManager.DERBY) && updateDump)
                              dumpData = dumpData + "CAST(X'";
                           else if (dataSourceType.equals(ConnectionManager.H2))
                              dumpData = dumpData + "x'";
                           else
                           {
                              if (theBytes.length != 0)
                                 dumpData = dumpData + "0x";
                              else
                                 dumpData = dumpData + "''";
                           }

                           // Go convert to hexadecimal/octal values
                           // and dump data as we go for blob/bytea.
                           dumpBinaryData(theBytes, false);
                        }
                        else
                           dumpData = dumpData + "NULL, ";
                     }
                     // Normal field
                     else
                     {
                        // Setting Auto-Increment Fields
                        if (DBTablesPanel.getSelectedTableTabPanel().getAutoIncrementHashMap()
                              .containsKey(field)
                            && sqlDataExportOptions.getAutoIncrement())
                        {
                           if (dataSourceType.equals(ConnectionManager.POSTGRESQL))
                           {
                              schemaName = schemaTableName.substring(0, schemaTableName.indexOf(".") + 2);
                              tableName = (schemaTableName.substring(schemaTableName.indexOf(".") + 1)).replaceAll(
                                                                     identifierQuoteString, "");

                              dumpData = dumpData + "nextval('" + schemaName + tableName + "_" + field
                                         + "_seq\"'), ";
                           }
                           else if (dataSourceType.equals(ConnectionManager.ORACLE))
                           {
                              dumpData = dumpData
                                         + identifierQuoteString
                                         + DBTablesPanel.getSelectedTableTabPanel().getAutoIncrementHashMap().get(field)
                                         + identifierQuoteString + ".NEXTVAL, ";
                           }
                           else if (dataSourceType.equals(ConnectionManager.DERBY))
                              dumpData = "DEFAULT, ";
                           else
                              dumpData = dumpData + "NULL, ";
                        }

                        // Setting TimeStamp Fields
                        else if (columnType.indexOf("TIMESTAMP") != -1 && sqlDataExportOptions.getTimeStamp())
                        {
                           if (columnType.indexOf("_") != -1)
                              dumpData = dumpData + "'{NOW()}'. ";
                           else
                           {
                              if (dataSourceType.equals(ConnectionManager.ORACLE))
                                 dumpData = dumpData + "SYSTIMESTAMP, ";
                              else if (dataSourceType.equals(ConnectionManager.DERBY))
                                 dumpData = dumpData + "CURRENT_TIMESTAMP, ";
                              else if (dataSourceType.equals(ConnectionManager.SQLITE))
                                 dumpData = dumpData + "STRFTIME(" +
                                 		"'%Y-%m-%d %H:%M:%S.%f', 'now', 'localtime'), ";
                              else
                                 dumpData = dumpData + "NOW(), ";
                           }
                        }
                        
                        // Setting SQLite Timestamp
                        else if (columnType.equals("TIMESTAMP")
                                 && dataSourceType.equals(ConnectionManager.SQLITE))
                        {
                           java.sql.Timestamp timestampValue = rs.getTimestamp(tableColumnNames.get(field));
                           
                           if (timestampValue != null)
                              dumpData = dumpData + ("'" + timestampValue + "', ");
                           else
                              dumpData = dumpData + "NULL, ";
                        }

                        // Setting Oracle TimeStamp(TZ)
                        else if ((columnType.equals("TIMESTAMP") || columnType.equals("TIMESTAMPTZ")
                                  || columnType.equals("TIMESTAMP WITH TIME ZONE")
                                  || columnType.equals("TIMESTAMP WITH LOCAL TIME ZONE")) &&
                                 dataSourceType.equals(ConnectionManager.ORACLE) &&
                                 !sqlDataExportOptions.getTimeStamp())
                        {
                           Object currentData = rs.getTimestamp(tableColumnNames.get(field));
                           
                           if (currentData != null)
                              dumpData = dumpData + "TO_TIMESTAMP('" + currentData
                                         + "', 'YYYY-MM-DD HH24:MI:SS:FF'), ";
                           else
                              dumpData = dumpData + "NULL, ";
                        }

                        // Setting Date Fields
                        else if (columnType.equals("DATE"))
                        {
                           if (dataSourceType.equals(ConnectionManager.ORACLE))
                           {
                              java.sql.Date dateValue = rs.getDate(tableColumnNames.get(field));
                              
                              if (dateValue != null)
                                 dumpData = dumpData + "TO_DATE('" + dateValue + "', 'YYYY-MM-DD'), ";
                              else
                                 dumpData = dumpData + "NULL, ";   
                           }
                           else
                           {
                              String dateString = rs.getString(tableColumnNames.get(field));
                              
                              if (dateString != null)
                                 dumpData = dumpData + "'" + addEscapes(dateString) + "', ";
                              else
                                 dumpData = dumpData + "NULL, ";    
                           }
                        }

                        // Fix for a bug in connectorJ, I think, that returns
                        // a whole date YYYY-MM-DD. Don't know what else
                        // to do it hangs my imports, but works with
                        // mysql console.
                        else if (columnType.equals("YEAR"))
                        {
                           String yearValue = rs.getString(tableColumnNames.get(field));
                           
                           if (yearValue != null)
                           {
                              if (yearValue.length() > 4)
                                 dumpData = dumpData + "'" + addEscapes(yearValue.substring(0, 4)) + "', ";
                              else
                                 dumpData = dumpData + "'" + addEscapes(yearValue) + "', ";
                           }
                           else
                              dumpData = dumpData + "NULL, ";
                        }

                        // Setting Bit Fields
                        else if (columnType.indexOf("BIT") != -1)
                        {
                           String bitValue = rs.getString(tableColumnNames.get(field));
                           
                           if (bitValue != null)
                           {
                              if (dataSourceType.equals(ConnectionManager.POSTGRESQL)
                                  || dataSourceType.equals(ConnectionManager.HSQL2))
                              {
                                 if (columnType.indexOf("_") != -1)
                                    dumpData = dumpData + "'" + bitValue + "', ";
                                 else
                                    dumpData = dumpData + "B'" + bitValue + "', ";
                              }
                              else if (dataSourceType.equals(ConnectionManager.DERBY))
                              {
                                 dumpData = dumpData + "X'";
                                 dumpBinaryData(rs.getBytes(tableColumnNames.get(field)), true);
                              }
                              else if (dataSourceType.equals(ConnectionManager.MSACCESS)
                                       || dataSourceType.equals(ConnectionManager.MSSQL))
                              {
                                 dumpData = dumpData + "'" + bitValue + "', ";
                              }
                              else
                              {
                                 try
                                 {
                                    dumpData = dumpData + "B'"
                                               + Integer.toBinaryString(Integer.parseInt(bitValue)) + "', ";
                                 }
                                 catch (NumberFormatException e)
                                 {
                                    dumpData = dumpData + "B'0', ";
                                 }
                              }
                           }
                           else
                              dumpData = dumpData + "NULL, ";
                        }

                        // All other fields
                        else
                        {
                           // Do not remove. Oracle LONG Types, which are
                           // processed here, only alows the resultSet get once.
                           // Oh, Oracle doesn't support the explicit INSERT,
                           // but what the hell maybe someone will use to export
                           // from Oracle to import into a MySQL database.

                           String contentString = rs.getString(tableColumnNames.get(field));

                           if (contentString != null)
                           {
                              if (columnType.equals("TIMESTAMPLTZ") &&
                                  dataSourceType.equals(ConnectionManager.ORACLE))
                                 dumpData = dumpData + "TO_TIMESTAMP_TZ('" + contentString
                                            + "', 'YYYY-MM-DD HH24:MI:SS TZH:TZM'), ";
                              else if (columnClass.indexOf("Integer") != -1 || columnClass.indexOf("Long") != -1
                                       || columnClass.indexOf("Float") != -1 || columnClass.indexOf("Double") != -1
                                       || (columnClass.indexOf("Byte") != -1 && columnType.indexOf("CHAR") == -1)
                                       || columnClass.indexOf("BigDecimal") != -1 || columnClass.indexOf("Short") != -1)
                                 dumpData = dumpData + contentString + ", ";
                              else
                                 dumpData = dumpData + "'" + addEscapes(contentString + "") + "', ";
                           }
                           else
                              dumpData = dumpData + "NULL, ";
                        }
                     }
                  }
               }

               // Creating end of extended SQL statement and
               // setting up for the next as needed.

               if (currentRow < rowsCount)
               {
                  dumpData = ((String) dumpData).substring(0, ((String) dumpData).length() - 2);

                  if (updateDump && !keys.isEmpty())
                     dumpData = (String) dumpData
                                + keyStringStatement.delete((keyStringStatement.length() - 5),
                                                             keyStringStatement.length())
                                + ";\n";
                  else
                     dumpData = dumpData + (";\n");

                  dumpChunkOfData(dumpData);
                  dumpData = "";

                  keyStringStatement.delete(0, keyStringStatement.length());
                  keyStringStatement.append(" WHERE ");

                  dumpData = dumpData + sqlDataExportOptions.getInsertReplaceUpdate().toUpperCase();
                  dumpData = dumpData + sqlDataExportOptions.getType().toUpperCase();

                  if (sqlDataExportOptions.getInsertReplaceUpdate().toUpperCase().equals("INSERT") ||
                      sqlDataExportOptions.getInsertReplaceUpdate().toUpperCase().equals("REPLACE"))
                     dumpData = dumpData + "INTO ";

                  dumpData = dumpData + schemaTableName + " SET ";
               }
               else
               {
                  if (updateDump)
                     dumpData = ((String) dumpData).substring(0, ((String) dumpData).length() - 2)
                                + keyStringStatement.delete((keyStringStatement.length() - 5),
                                                             keyStringStatement.length());
                  else
                     dumpData = ((String) dumpData).substring(0, ((String) dumpData).length() - 2);
               }
            }
            currentTableIncrement += limitIncrement;  
         }
         while (!limits && currentTableIncrement < rowsCount && !dumpProgressBar.isCanceled());
         
         // Closing out
         rs.close();
         sqlStatement.close();
         dumpProgressBar.dispose();
      }
      catch (SQLException e)
      {
         dumpProgressBar.setCanceled(true);
         ConnectionManager.displaySQLErrors(e, "SQLDataDumpThread explicitStatementData()");
      }
      finally
      {
         if (sqlStatement != null)
            sqlStatement.close();
      }
   }

   //==============================================================
   // Class method to get the table data row count
   //==============================================================

   private int getRowsCount(Connection dbConnection, String tableName) throws SQLException
   {
      // Method Instances
      String sqlStatementString;
      Statement sqlStatement;
      ResultSet rs;
      
      // Setup
      int rowCount = 0;
      sqlStatement = null;
      rs = null;
      
      try
      {
         sqlStatement = dbConnection.createStatement();
         sqlStatementString = "SELECT COUNT(*) FROM " + tableName;
         // System.out.println(sqlStatementString);

         rs = sqlStatement.executeQuery(sqlStatementString);
         rs.next();
         rowCount = rs.getInt(1);

         return rowCount;
      }
      catch (SQLException e)
      {
         ConnectionManager.displaySQLErrors(e, "SQLDataDumpThread getRowsCount()");
         return rowCount;  
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
            ConnectionManager.displaySQLErrors(sqle, "SQLDataDumpThread getRowsCount()");
         }
         finally
         {
            if (sqlStatement != null)
               sqlStatement.close();
         }
      }
   }

   //==============================================================
   // Class method to get the table data row count
   //==============================================================

   private void dumpBinaryData(byte[] theBytes, boolean derbyBit)
   {
      // Class Method Instances
      int b;
      String hexadecimalString, octalString;
      BufferedInputStream in;

      // Otain byes in a stream and convert to
      // hex for dumping.
      if (theBytes != null)
      {
         in = new BufferedInputStream(new ByteArrayInputStream(theBytes));

         try
         {
            while ((b = in.read()) != -1)
            {
               // Dump as octal data.
               if (dataSourceType.equals(ConnectionManager.POSTGRESQL))
               {
                  octalString = Integer.toString(b, 8);
                  if (octalString.length() == 1)
                     octalString = "00" + octalString;
                  if (octalString.length() > 1 && octalString.length() < 3)
                     octalString = "0" + octalString;
                  dumpData = dumpData + "\\\\" + octalString;
                  dumpChunkOfData(dumpData);
                  dumpData = "";
               }
               // Dump as hexadecimal data.
               else
               {
                  hexadecimalString = Integer.toString(b, 16);
                  if (hexadecimalString.length() < 2)
                     hexadecimalString = "0" + hexadecimalString;
                  if (hexadecimalString.length() > 2)
                     hexadecimalString = hexadecimalString.substring(hexadecimalString.length() - 2);
                  dumpData = dumpData + hexadecimalString;
                  dumpChunkOfData(dumpData);
                  dumpData = "";
               }
            }
            if (dataSourceType.equals(ConnectionManager.POSTGRESQL) ||
                dataSourceType.indexOf(ConnectionManager.HSQL) != -1 ||
                dataSourceType.equals(ConnectionManager.SQLITE) ||
                dataSourceType.equals(ConnectionManager.H2))
               dumpData = dumpData + "', ";
            else if (dataSourceType.equals(ConnectionManager.DERBY))
            {
               if (!derbyBit)
                  dumpData = dumpData + "' AS BLOB), ";
               else
                  dumpData = dumpData + "', ";
            }
            else if (dataSourceType.equals(ConnectionManager.ORACLE) &&
                     (updateDump || insertReplaceDump))
               dumpData = dumpData + "'), ";
            else
               dumpData = dumpData + ", ";
         }
         catch (IOException e)
         {
            String msg = "Unable to Create Buffered InputStream for Blob Data";
            JOptionPane.showMessageDialog(null, msg, fileName, JOptionPane.ERROR_MESSAGE);
            return;
         }
      }
      else
         dumpData = dumpData + "NULL, ";
   }

   //==============================================================
   // Class method for generating dump header info
   //==============================================================

   public String generateHeaders()
   {
      // Class Method Instances.
      ConnectionProperties connectionProperties;
      String hostName, databaseName;
      String dateTime, headers;
      SimpleDateFormat dateTimeFormat;
      
      // Create Header.
      
      connectionProperties = ConnectionManager.getConnectionProperties();
      hostName = connectionProperties.getProperty(ConnectionProperties.HOST);
      databaseName = connectionProperties.getProperty(ConnectionProperties.DB);

      dateTimeFormat = new SimpleDateFormat("yyyy.MM.dd G 'at' hh:mm:ss z");
      dateTime = dateTimeFormat.format(new Date());

      headers = "--\n" + "-- MyJSQLView SQL Dump\n" + "-- Version: " + myJSQLView_Version[1] + "\n" 
                + "-- WebSite: http://myjsqlview.org\n" 
                + "--\n" + "-- Host: " + hostName + "\n" 
                + "-- Generated On: " + dateTime + "\n" + "-- SQL version: " 
                + ConnectionManager.getDBProductName_And_Version() + "\n"
                + "-- Database: " + databaseName + "\n" + "--\n\n"
                + "-- ------------------------------------------\n";

      // System.out.println(headers);
      return headers;
   }

   //==============================================================
   // Class method for generating comment separator.
   //==============================================================

   public String genCommentSep(String str)
   {
      String res;
      res = "\n--\n";
      res += "-- " + str;
      res += "\n--\n\n";
      return res;
   }

   //==============================================================
   // Class method for escaping a string.
   //==============================================================

   private String addEscapes(String str)
   {
      if (str == null)
         return "";

      // For some reason the sequence ;\n is not
      // able to be properly pulled into either
      // the MySQL or PostgreSQL. So what else hack
      // it, add a space before newline. Could
      // find no reason for this in either manual
      // for characters that need escaping.

      str = str.replaceAll(";\\n", "; \\n");

      // Escape the single quote character which is
      // the character being used to deliminate the
      // content.
      StringBuffer s = new StringBuffer((String) str);
      for (int i = 0; i < s.length(); i++)
      {
         if (s.charAt(i) == '\'')
            s.insert(i++, '\'');
      }
      return s.toString();
   }

   //==============================================================
   // Class Method to dump a chunk of data to the output file.
   //==============================================================

   private void dumpChunkOfData(Object dumpData)
   {
      // Class Method Instances
      byte[] currentBytes;

      // Dump the Chunk.
      try
      {
         currentBytes = dumpData.toString().getBytes();
         filebuff.write(currentBytes);
         filebuff.flush();
      }
      catch (IOException e)
      {
         String msg = "Error outputing data to: '" + fileName + "'.";
         JOptionPane.showMessageDialog(null, msg, fileName, JOptionPane.ERROR_MESSAGE);
      }
   }
}
