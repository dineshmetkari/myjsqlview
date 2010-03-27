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
// Copyright (C) 2006-2010 Borislav Gizdov, Dana M. Proctor
// Version 6.78 03/27/2010
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
//         1.2 Corrected Un-Intentional Removal of "+" in
//             Class Method run(). Original File Had "+" at
//             Beginning of Each Line. ReplaceAll Screwed Up.
//		     1.3 Added create table statement, lock table, 
// 			   mysql version in header.
//         1.4 Minor Format Changes & Closing Statements in
//             Some of the Class Methods.
//         1.5 Reviewed & Commented for Next Release. Changed
//             dumpData Instance to StringBuilder As Suggested.
//         1.6 Removed Class Modifier "Public" & Comment/Style
//             Changes.
//         1.7 Class WriteDataFile Boolean Argument Addition, true.
//         1.8 Corrected Incrementing of ProgressBar.
//         1.9 Reverted Class Instance dumpData Back to Object,
//             Mainly to Maintain Compatibility to JRE1.4xxxx.
//         2.0 Partial Fix for Blob Data Output.
//         2.1 Class Method dumpChunkOfData().
//         2.2 Fixed Minor Problem in dumpData output.
//         2.3 Created Single Argument Constructor, (String[]
//             myJSQLView_Version). Beginning of Rebuild of Class. 
//         2.4 Bug Fix SQL Export Blob Data Coruption Task #135774.
//         2.5 SQL Export Options Drop Table, Type for Insert, and
//             Lock. Other Misc. Changes.
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
//             
//-----------------------------------------------------------------
//                poisonerbg@users.sourceforge.net
//                danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview;

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
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.JOptionPane;

/**
 *    The SQLDataDumpThread provides a thread to safely dump database
 * table data to a local file in SQL format. A status dialog with
 * cancel is provided to provide the ability to prematurely terminate
 * the dump.
 * 
 * @author Borislav Gizdov a.k.a. PoisoneR, Dana Proctor
 * @version 6.78 03/27/2010
 */

class SQLDataDumpThread implements Runnable
{
   // Class Instances.
   Thread dumpThread;
   private Object dumpData;
   private String exportedTable;
   private Vector columnNameFields;
   private HashMap tableColumnNames;
   private HashMap tableColumnClassHashMap;
   private HashMap tableColumnTypeHashMap;
   private String fileName;
   private String schemaName, tableName;
   private String dbSchemaTableName, schemaTableName;
   private String dbIdentifierQuoteString, identifierQuoteString;
   private String[] myJSQLView_Version;

   private boolean limits, insertReplaceDump, updateDump;
   private DataExportProperties sqlDataExportOptions;
   private BufferedOutputStream filebuff;
   private MyJSQLView_ProgressBar dumpProgressBar;

   //==============================================================
   // SQLDataDumpThread Constructor.
   //==============================================================

   SQLDataDumpThread(String[] myJSQLView_Version)
   {
      this.myJSQLView_Version = myJSQLView_Version;

      // Setup export options & identifer String.
      dbIdentifierQuoteString = MyJSQLView_Access.getIdentifierQuoteString();
      sqlDataExportOptions = DBTablesPanel.getDataExportProperties();
      identifierQuoteString = sqlDataExportOptions.getIdentifierQuoteString();
   }

   //==============================================================
   // SQLDataDumpThread Constructor.
   //==============================================================

   SQLDataDumpThread(Vector columnNameFields, HashMap tableColumnNames,
                     boolean limits, HashMap tableColumnClassHashMap,
                     HashMap tableColumnTypeHashMap, String exportedTable,
                     String fileName, String[] myJSQLView_Version)
   {
      this.columnNameFields = columnNameFields;
      this.tableColumnNames = tableColumnNames;
      this.limits = limits;
      this.tableColumnClassHashMap = tableColumnClassHashMap;
      this.tableColumnTypeHashMap = tableColumnTypeHashMap;
      this.myJSQLView_Version = myJSQLView_Version;
      this.exportedTable = exportedTable;
      this.fileName = fileName;
      updateDump = false;
      insertReplaceDump = false;

      // Setup export options, & identifier String.
      dbIdentifierQuoteString = MyJSQLView_Access.getIdentifierQuoteString();
      sqlDataExportOptions = DBTablesPanel.getDataExportProperties();
      identifierQuoteString = sqlDataExportOptions.getIdentifierQuoteString();

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

      // Create and start the class thread.
      dumpThread = new Thread(this, "SQLDataDumpThread");
      // System.out.println("SQL Data Dumb Thread");

      dumpThread.start();
   }

   //==============================================================
   // Class method for normal start of the thread
   //==============================================================

   public void run()
   {
      // Class Method Instances.
      FileOutputStream fileStream;
      Statement sqlStatement;
      ResultSet rs;

      // Get Connection to Database.
      Connection dbConnection = MyJSQLView_Access.getConnection("SQLDataDumpThread run()");
      
      if (dbConnection == null)
         return;

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
            filebuff.close();
            MyJSQLView_Access.closeConnection(dbConnection, "SQLDataDumpThread run()");
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
               filebuff.close();
               MyJSQLView_Access.closeConnection(dbConnection, "SQLDataDumpThread run()");
               return;
            }
         }

         // Comments for Table.
         dumpData = dumpData + genCommentSep("Dumping data for table " + schemaTableName);

         // Check to see if there is any data to actually be
         // dumped from the table and proceeding as necesary.
         try
         {
            sqlStatement = dbConnection.createStatement();

            if (MyJSQLView_Access.getSubProtocol().indexOf("oracle") != -1)
               rs = sqlStatement.executeQuery("SELECT * FROM " + dbSchemaTableName + " WHERE ROWNUM=1");
            else
               rs = sqlStatement.executeQuery("SELECT * FROM " + dbSchemaTableName + " AS t LIMIT 1");

            if (rs.next())
            {
               // Lock.
               if (sqlDataExportOptions.getLock())
               {
                  if (MyJSQLView_Access.getSubProtocol().equals("mysql"))
                  {
                     dumpData = dumpData + ("/*!40000 ALTER TABLE " 
                                + schemaTableName + " DISABLE KEYS */;\n");
                     dumpData = dumpData + ("LOCK TABLES " + schemaTableName + " WRITE;\n");
                  }
                  else if (MyJSQLView_Access.getSubProtocol().equals("postgresql"))
                     dumpData = dumpData + ("LOCK TABLE " + schemaTableName + ";\n");
               }

               // Create the Appropriate Insert,Replace or Update Statements
               // with data as needed.

               if ((sqlDataExportOptions.getInsertReplaceUpdate().equals("Insert") ||
                    sqlDataExportOptions.getInsertReplaceUpdate().equals("Replace")) &&
                   !sqlDataExportOptions.getInsertExpression().equals("Explicit"))
                  insertReplaceStatementData(dbConnection);
               else
                  explicitStatementData(dbConnection);
               dumpData = dumpData + ";\n";

               // Finishing up.
               if (sqlDataExportOptions.getLock())
               {
                  if (MyJSQLView_Access.getSubProtocol().equals("mysql"))
                  {
                     dumpData = dumpData + "UNLOCK TABLES;\n";
                     dumpData = dumpData + "/*!40000 ALTER TABLE " + schemaTableName 
                                + " ENABLE KEYS */;\n";
                  }
               }
            }
            rs.close();
            sqlStatement.close();
         }
         catch (SQLException e)
         {
            MyJSQLView_Access.displaySQLErrors(e, "SQLDataDumpThread run()");
         }
         dumpChunkOfData(dumpData);
         dumpData = "";

         MyJSQLView_Access.closeConnection(dbConnection, "SQLDataDumpThread run()");

         if (dumpProgressBar.isCanceled())
         {
            try
            {
               makeNewFile.delete();
            }
            catch (SecurityException se){}
         }
         dumpProgressBar.dispose();
         filebuff.close();
      }
      catch (IOException e)
      {
         String msg = "Unable to Create filestream for: '" + fileName + "'.";
         JOptionPane.showMessageDialog(null, msg, fileName, JOptionPane.ERROR_MESSAGE);
         return;
      }
   }

   //==============================================================
   // Class method to create the insert/replace statement and data.
   //==============================================================

   private void insertReplaceStatementData(Connection dbConnection)
   {
      // Class Method Instances
      Iterator columnNamesIterator;
      HashMap autoIncrementFieldIndexes;
      Vector blobFieldIndexes;
      Vector bitFieldIndexes;
      Vector timeStampIndexes;
      Vector oracleTimeStamp_TZIndexes;
      Vector oracleTimeStamp_LTZIndexes;
      Vector dateIndexes;
      Vector yearIndexes;
      Vector arrayIndexes;
      String field, columnClass, columnType;
      String sqlFieldValuesString;
      String expressionType;
      int rowsCount, currentRow, columnsCount;

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

      // Obtain the table fields and obtain list of
      // specific fields that need special handling
      // while at the same time begin creating the
      // data select SQLStatement.

      columnsCount = 0;
      sqlStatementString = "SELECT ";
      columnNamesIterator = columnNameFields.iterator();
      autoIncrementFieldIndexes = new HashMap();
      blobFieldIndexes = new Vector();
      bitFieldIndexes = new Vector();
      timeStampIndexes = new Vector();
      oracleTimeStamp_TZIndexes = new Vector();
      oracleTimeStamp_LTZIndexes = new Vector();
      dateIndexes = new Vector();
      yearIndexes = new Vector();
      arrayIndexes = new Vector();

      while (columnNamesIterator.hasNext())
      {
         field = (String) columnNamesIterator.next();
         columnClass = (String) tableColumnClassHashMap.get(field);
         columnType = (String) tableColumnTypeHashMap.get(field);
         // System.out.println("field:" + field + " class:" + columnClass + "
         // type:" + columnType);

         // Save the index of autoIncrement entries.
         if (DBTablesPanel.getSelectedTableTabPanel().getAutoIncrementHashMap().containsKey(field))
         {
            String currentIndex = columnsCount + 1 + "";
            if (MyJSQLView_Access.getSubProtocol().indexOf("oracle") != -1)
               autoIncrementFieldIndexes.put(currentIndex,
                                             DBTablesPanel.getSelectedTableTabPanel().getAutoIncrementHashMap().get(field));
            else
               autoIncrementFieldIndexes.put(currentIndex, tableColumnNames.get(field));
         }

         // Save the index of blob/bytea/binary entries.
         if ((columnClass.indexOf("String") == -1 && columnType.indexOf("BLOB") != -1) ||
             (columnClass.indexOf("BLOB") != -1 && columnType.indexOf("BLOB") != -1) ||
             (columnType.indexOf("BYTEA") != -1) || (columnType.indexOf("BINARY") != -1) ||
             (columnType.indexOf("RAW") != -1))
         {
            String currentIndex = columnsCount + 1 + "";
            blobFieldIndexes.add(currentIndex);
         }

         // Save the index of bit entries.
         if (columnType.indexOf("BIT") != -1)
         {
            String currentIndex = columnsCount + 1 + "";
            bitFieldIndexes.add(currentIndex);
         }

         // Save the index of TimeStamp Fields.
         if (columnType.indexOf("TIMESTAMP") != -1)
         {
            String currentIndex = columnsCount + 1 + "";
            timeStampIndexes.add(currentIndex);
         }

         // Save the index of Oracle TimeStamp(TZ) Fields.
         if (MyJSQLView_Access.getSubProtocol().indexOf("oracle") != -1 &&
             (columnType.equals("TIMESTAMP") || columnType.equals("TIMESTAMPTZ")))
         {
            String currentIndex = columnsCount + 1 + "";
            oracleTimeStamp_TZIndexes.add(currentIndex);
         }

         // Save the index of Oracle TimeStamp(TZ) Fields.
         if (MyJSQLView_Access.getSubProtocol().indexOf("oracle") != -1 &&
             columnType.equals("TIMESTAMPLTZ"))
         {
            String currentIndex = columnsCount + 1 + "";
            oracleTimeStamp_LTZIndexes.add(currentIndex);
         }

         // Save the index of date entries.
         if (columnType.equals("DATE"))
         {
            String currentIndex = columnsCount + 1 + "";
            dateIndexes.add(currentIndex);
         }

         // Save the index of year entries.
         if (columnType.indexOf("YEAR") != -1)
         {
            String currentIndex = columnsCount + 1 + "";
            yearIndexes.add(currentIndex);
         }

         // Save the index of array entries.
         if (columnType.indexOf("_") != -1)
         {
            String currentIndex = columnsCount + 1 + "";
            arrayIndexes.add(currentIndex);
         }

         // Modify Statement as needed for Oracle TIMESTAMPLTZ Fields.
         if (MyJSQLView_Access.getSubProtocol().indexOf("oracle") != -1 &&
             columnType.equals("TIMESTAMPLTZ"))
         {
            sqlStatementString += "TO_CHAR(" + dbIdentifierQuoteString + tableColumnNames.get(field)
                                  + dbIdentifierQuoteString + ", 'MM-DD-YYYY HH24:MM:SS TZR') AS "
                                  + dbIdentifierQuoteString + tableColumnNames.get(field)
                                  + dbIdentifierQuoteString + ", ";
         }
         else
            sqlStatementString += dbIdentifierQuoteString + tableColumnNames.get(field)
                                  + dbIdentifierQuoteString + ", ";
         sqlFieldValuesString += (identifierQuoteString + tableColumnNames.get(field) 
                                 + identifierQuoteString + ", ");

         columnsCount++;
      }

      // Finishing creating the Select statement to retrieve data.

      sqlStatementString = sqlStatementString.substring(0, sqlStatementString.length() - 2);
      sqlStatementString += " FROM " + dbSchemaTableName;
      if (limits)
         sqlStatementString = DBTablesPanel.getSelectedTableTabPanel().getTableSQLStatement();
      // System.out.println(sqlStatementString);

      // Do an initial dump of data created so far.

      sqlFieldValuesString = sqlFieldValuesString.substring(0, sqlFieldValuesString.length() - 2);
      sqlFieldValuesString += ") VALUES";
      dumpData = dumpData + sqlFieldValuesString;

      dumpChunkOfData(dumpData);
      dumpData = "";

      // Ok now ready so beginning by connecting to database for
      // data and proceeding with building the dump data.
      try
      {
         sqlStatement = dbConnection.createStatement();

         // Setting up to begin insert statements.
         if (limits)
            rowsCount = DBTablesPanel.getSelectedTableTabPanel().getValidDataRowCount();
         else
            rowsCount = getRowsCount(dbConnection, dbSchemaTableName);
         currentRow = 0;

         // Start a progress bar for tracking/canceling.
         dumpProgressBar.setTaskLength(rowsCount);
         dumpProgressBar.pack();
         dumpProgressBar.center();
         dumpProgressBar.setVisible(true);

         rs = sqlStatement.executeQuery(sqlStatementString);

         // Begin the creation of insert statements.
         while (rs.next() && !dumpProgressBar.isCanceled())
         {
            dumpProgressBar.setCurrentValue(currentRow++);

            // SQL Singular Statement
            if (expressionType.equals("Singular"))
               dumpData = dumpData + "(";
            // SQL Plural Statement
            else
               dumpData = dumpData + ("\n(");

            for (int i = 1; i <= columnsCount; i++)
            {
               String currentIndex = i + "";
               // System.out.print(i + " ");

               // Determining binary types and acting appropriately.
               if (blobFieldIndexes.contains(currentIndex))
               {
                  byte[] theBytes = rs.getBytes(i);

                  if (theBytes != null)
                  {
                     if (MyJSQLView_Access.getSubProtocol().equals("postgresql"))
                        dumpData = dumpData + "E'";
                     else if (MyJSQLView_Access.getSubProtocol().indexOf("hsql") != -1)
                        dumpData = dumpData + "'";
                     else if (MyJSQLView_Access.getSubProtocol().indexOf("oracle") != -1)
                        dumpData = dumpData + "HEXTORAW('";
                     else
                        dumpData = dumpData + "0x";

                     // Go convert to hexadecimal/octal values
                     // and dump data as we go for blob/bytea.
                     dumpBinaryData(theBytes);
                  }
                  else
                     dumpData = dumpData + "NULL, ";
               }
               // Regular Fields
               else
               {
                  // Check for an AutoIncrement
                  if (autoIncrementFieldIndexes.containsKey(currentIndex)
                      && sqlDataExportOptions.getAutoIncrement())
                  {
                     if (MyJSQLView_Access.getSubProtocol().equals("postgresql"))
                     {
                        schemaName = schemaTableName.substring(0, schemaTableName.indexOf(".") + 2);
                        tableName = (schemaTableName.substring(schemaTableName.indexOf(".") 
                                    + 1)).replaceAll(identifierQuoteString, "");

                        dumpData = dumpData + "nextval('" + schemaName + tableName + "_"
                                   + autoIncrementFieldIndexes.get(currentIndex) + "_seq\"'), ";
                     }
                     else if (MyJSQLView_Access.getSubProtocol().indexOf("oracle") != -1)
                     {
                        dumpData = dumpData + identifierQuoteString
                                   + autoIncrementFieldIndexes.get(currentIndex) 
                                   + identifierQuoteString + ".NEXTVAL, ";
                     }
                     else
                        dumpData = dumpData + "NULL, ";
                  }
                  else
                  {
                     // Check for a TimeStamp
                     if (timeStampIndexes.contains(currentIndex) && sqlDataExportOptions.getTimeStamp())
                     {
                        if (arrayIndexes.contains(currentIndex))
                           dumpData = dumpData + "'{NOW()}', ";
                        else
                        {
                           if (MyJSQLView_Access.getSubProtocol().indexOf("oracle") != -1)
                              dumpData = dumpData + "SYSTIMESTAMP, ";
                           else
                              dumpData = dumpData + "NOW(), ";
                        }
                     }

                     // Check for Oracle TimeStamp(TZ)
                     else if (oracleTimeStamp_TZIndexes.contains(currentIndex)
                              && !sqlDataExportOptions.getTimeStamp())
                     {
                        if (rs.getTimestamp(i) != null)
                        {
                           Object currentData = rs.getTimestamp(i);
                           dumpData = dumpData + "TO_TIMESTAMP('" + currentData
                                      + "', 'YYYY-MM-DD HH24:MI:SS:FF'), ";
                        }
                        else
                           dumpData = dumpData + "NULL, ";
                     }

                     // Check for a Date
                     else if (dateIndexes.contains(currentIndex))
                     {
                        if (rs.getString(i) != null)
                        {
                           if (MyJSQLView_Access.getSubProtocol().indexOf("oracle") != -1)
                              dumpData = dumpData + "TO_DATE('" + rs.getDate(i) + "', 'YYYY-MM-DD'), ";
                           else
                              dumpData = dumpData + "'" + addEscapes(rs.getString(i) + "") + "', ";
                        }
                        else
                           dumpData = dumpData + "NULL, ";
                     }

                     // Check for a Year
                     else if (yearIndexes.contains(currentIndex))
                     {
                        // Fix for a bug in connectorJ, I think, that returns
                        // a whole date YYYY-MM-DD. Don't know what else
                        // to do it hangs my imports, but works with
                        // mysql console.
                        if (rs.getString(i) != null)
                        {
                           if (rs.getString(i).length() > 4)
                              dumpData = dumpData + "'" + addEscapes(rs.getString(i).substring(0, 4)) + "', ";
                           else
                              dumpData = dumpData + "'" + addEscapes(rs.getString(i)) + "', ";
                        }
                        else
                           dumpData = dumpData + "NULL, ";
                     }

                     // Check for Bit fields.
                     else if (bitFieldIndexes.contains(currentIndex))
                     {
                        if (rs.getString(i) != null)
                        {
                           if (MyJSQLView_Access.getSubProtocol().equals("postgresql"))
                           {
                              if (arrayIndexes.contains(currentIndex))
                                 dumpData = dumpData + "'" + rs.getString(i) + "', ";
                              else
                                 dumpData = dumpData + "B'" + rs.getString(i) + "', ";
                           }
                           else
                           {
                              try
                              {
                                 dumpData = dumpData + "B'"
                                            + Integer.toBinaryString(Integer.parseInt(rs.getString(i)))
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
                           if (oracleTimeStamp_LTZIndexes.contains(currentIndex) &&
                               !sqlDataExportOptions.getTimeStamp())
                              dumpData = dumpData + "TO_TIMESTAMP_TZ('" + contentString
                                         + "', 'MM-DD-YYYY HH24:MI:SS TZH:TZM'), ";
                           else
                              dumpData = dumpData + "'" + addEscapes(contentString + "") + "', ";
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
               dumpData = dumpData + "),";

            if (currentRow >= rowsCount)
            {
               // SQL Singular Statement
               if (expressionType.equals("Singular"))
                  dumpData = ((String) dumpData).substring(0, ((String) dumpData).length() - 2);
               // SQL Plural Statement
               else
                  dumpData = ((String) dumpData).substring(0, ((String) dumpData).length() - 1);
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
                  dumpData = "";
            }
         }
         // Closing out
         rs.close();
         sqlStatement.close();
         dumpProgressBar.dispose();
      }
      catch (SQLException e)
      {
         dumpProgressBar.setCanceled(true);
         MyJSQLView_Access.displaySQLErrors(e, "SQLDataDumpThread insertReplaceStatementData()");
      }
   }

   //==============================================================
   // Class method to create the explicit or update statement and
   // data.
   //==============================================================

   private void explicitStatementData(Connection dbConnection)
   {
      // Class Method Instances
      StringBuffer columnNamesString;
      Iterator columnNamesIterator;
      String field, columnClass, columnType;
      
      Vector keys;
      StringBuffer keyStringStatement;
      int rowsCount, currentRow;

      String sqlStatementString;
      Statement sqlStatement;
      ResultSet rs;

      // Setting up for possible update dump.

      keys = new Vector();
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

      sqlStatementString = "SELECT ";
      columnNamesString = new StringBuffer();
      columnNamesIterator = columnNameFields.iterator();

      while (columnNamesIterator.hasNext())
      {
         field = (String) columnNamesIterator.next();

         if (MyJSQLView_Access.getSubProtocol().indexOf("oracle") != -1
             && ((String) tableColumnTypeHashMap.get(field)).equals("TIMESTAMPLTZ"))
         {
            columnNamesString.append("TO_CHAR(" + dbIdentifierQuoteString + tableColumnNames.get(field)
                                  + dbIdentifierQuoteString + ", 'MM-DD-YYYY HH24:MM:SS TZR') AS "
                                  + dbIdentifierQuoteString + tableColumnNames.get(field)
                                  + dbIdentifierQuoteString + ", ");
         }
         else
            columnNamesString.append(dbIdentifierQuoteString + tableColumnNames.get(field)
                                  + dbIdentifierQuoteString + ", ");
      }
      columnNamesString.delete((columnNamesString.length() - 2), columnNamesString.length());
      sqlStatementString += columnNamesString.toString() + " FROM " + dbSchemaTableName;
      
      if (limits)
         sqlStatementString = DBTablesPanel.getSelectedTableTabPanel().getTableSQLStatement();
      //System.out.println(sqlStatementString);

      // Do an initial dump of data created so far.
      dumpChunkOfData(dumpData);
      dumpData = "";

      // Ok now ready so beginning by connecting to database for
      // data and proceeding with building the dump data.
      try
      {
         sqlStatement = dbConnection.createStatement();

         // Setting up to begin update statements.
         if (limits)
            rowsCount = DBTablesPanel.getSelectedTableTabPanel().getValidDataRowCount();
         else
            rowsCount = getRowsCount(dbConnection, dbSchemaTableName);
         currentRow = 0;

         // Start a progress bar for tracking/canceling.
         dumpProgressBar.setTaskLength(rowsCount);
         dumpProgressBar.pack();
         dumpProgressBar.center();
         dumpProgressBar.setVisible(true);

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
               columnClass = (String) tableColumnClassHashMap.get(field);
               columnType = (String) tableColumnTypeHashMap.get(field);
               // System.out.println("field:" + field + " class:" + columnClass
               // + " type:" + columnType);

               // Setting up WHERE Statement for Update Dump.
               if (keys.contains(tableColumnNames.get(field)) && updateDump)
               {
                  keyStringStatement.append(identifierQuoteString + ((String) tableColumnNames.get(field)) 
                                            + identifierQuoteString + "=");

                  if (rs.getString((String) tableColumnNames.get(field)) != null)
                  {
                     keyStringStatement.append("'" + rs.getString((String) tableColumnNames.get(field)) 
                                               + "' AND ");
                  }
                  else
                     keyStringStatement.append("NULL AND ");
               }
               else
               {
                  dumpData = dumpData + identifierQuoteString 
                             + ((String) tableColumnNames.get(field))
                             + identifierQuoteString + "=";

                  // Blob/Bytea/Binary data adding
                  if ((columnClass.indexOf("String") == -1 && columnType.indexOf("BLOB") != -1) ||
                      (columnClass.indexOf("BLOB") != -1 && columnType.indexOf("BLOB") != -1) ||
                      (columnType.indexOf("BYTEA") != -1) || (columnType.indexOf("BINARY") != -1) ||
                      (columnType.indexOf("RAW") != -1))
                  {
                     byte[] theBytes = rs.getBytes((String) tableColumnNames.get(field));

                     if (theBytes != null)
                     {
                        // Let Oracle RAW & BLOB fall through if not update
                        // since
                        // an explicit statement is not supported. Allows to
                        // convert
                        // these from Oracle to MySQL.

                        if (MyJSQLView_Access.getSubProtocol().equals("postgresql"))
                           dumpData = dumpData + "E'";
                        else if (MyJSQLView_Access.getSubProtocol().indexOf("hsql") != -1)
                           dumpData = dumpData + "'";
                        else if (MyJSQLView_Access.getSubProtocol().indexOf("oracle") != -1 && updateDump)
                           dumpData = dumpData + "HEXTORAW('";
                        else
                           dumpData = dumpData + "0x";

                        // Go convert to hexadecimal/octal values
                        // and dump data as we go for blob/bytea.
                        dumpBinaryData(theBytes);
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
                        if (MyJSQLView_Access.getSubProtocol().equals("postgresql"))
                        {
                           schemaName = schemaTableName.substring(0, schemaTableName.indexOf(".") + 2);
                           tableName = (schemaTableName.substring(schemaTableName.indexOf(".") + 1)).replaceAll(identifierQuoteString, "");

                           dumpData = dumpData + "nextval('" + schemaName + tableName + "_" + field
                                      + "_seq\"'), ";
                        }
                        else if (MyJSQLView_Access.getSubProtocol().indexOf("oracle") != -1)
                        {
                           dumpData = dumpData
                                      + identifierQuoteString
                                      + DBTablesPanel.getSelectedTableTabPanel().getAutoIncrementHashMap().get(field) + identifierQuoteString + ".NEXTVAL, ";
                        }
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
                           if (MyJSQLView_Access.getSubProtocol().indexOf("oracle") != -1)
                              dumpData = dumpData + "SYSTIMESTAMP, ";
                           else
                              dumpData = dumpData + "NOW(), ";
                        }
                     }

                     // Setting Oracle TimeStamp(TZ)
                     else if ((columnType.equals("TIMESTAMP") || columnType.equals("TIMESTAMPTZ")) &&
                              MyJSQLView_Access.getSubProtocol().indexOf("oracle") != -1 &&
                              !sqlDataExportOptions.getTimeStamp())
                     {
                        if (rs.getTimestamp((String) tableColumnNames.get(field)) != null)
                        {
                           Object currentData = rs.getTimestamp((String) tableColumnNames.get(field));
                           dumpData = dumpData + "TO_TIMESTAMP('" + currentData
                                      + "', 'YYYY-MM-DD HH24:MI:SS:FF'), ";
                        }
                        else
                           dumpData = dumpData + "NULL, ";
                     }

                     // Setting Date Fields
                     else if (columnType.equals("DATE"))
                     {
                        if (rs.getString((String) tableColumnNames.get(field)) != null)
                        {
                           if (MyJSQLView_Access.getSubProtocol().indexOf("oracle") != -1)
                              dumpData = dumpData + "TO_DATE('"
                                         + rs.getDate((String) tableColumnNames.get(field))
                                         + "', 'YYYY-MM-DD'), ";
                           else
                              dumpData = dumpData + "'"
                                         + addEscapes(rs.getString((String) tableColumnNames.get(field)))
                                         + "', ";
                        }
                        else
                           dumpData = dumpData + "NULL, ";
                     }

                     // Fix for a bug in connectorJ, I think, that returns
                     // a whole date YYYY-MM-DD. Don't know what else
                     // to do it hangs my imports, but works with
                     // mysql console.
                     else if (columnType.equals("YEAR"))
                     {
                        if (rs.getString((String) tableColumnNames.get(field)) != null)
                        {
                           if (rs.getString((String) tableColumnNames.get(field)).length() > 4)
                              dumpData = dumpData
                                         + "'"
                                         + addEscapes(rs.getString((String) tableColumnNames.get(field))
                                               .substring(0, 4)) + "', ";
                           else
                              dumpData = dumpData + "'"
                                         + addEscapes(rs.getString((String) tableColumnNames.get(field)))
                                         + "', ";
                        }
                        else
                           dumpData = dumpData + "NULL, ";
                     }

                     // Setting Bit Fields
                     else if (columnType.indexOf("BIT") != -1)
                     {
                        if (rs.getString((String) tableColumnNames.get(field)) != null)
                        {
                           if (MyJSQLView_Access.getSubProtocol().equals("postgresql"))
                           {
                              if (columnType.indexOf("_") != -1)
                                 dumpData = dumpData + "'"
                                            + rs.getString((String) tableColumnNames.get(field)) + "', ";
                              else
                                 dumpData = dumpData + "B'"
                                            + rs.getString((String) tableColumnNames.get(field)) + "', ";
                           }
                           else
                           {
                              try
                              {
                                 dumpData = dumpData
                                            + "B'"
                                            + Integer.toBinaryString(Integer.parseInt(rs
                                                  .getString((String) tableColumnNames.get(field)))) + "', ";
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

                        String contentString = rs.getString((String) tableColumnNames.get(field));

                        if (contentString != null)
                        {
                           if (columnType.equals("TIMESTAMPLTZ") &&
                               MyJSQLView_Access.getSubProtocol().indexOf("oracle") != -1)
                              dumpData = dumpData + "TO_TIMESTAMP_TZ('" + contentString
                                         + "', 'MM-DD-YYYY HH24:MI:SS TZH:TZM'), ";
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
                  dumpData = (String)dumpData
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
         // Closing out
         rs.close();
         sqlStatement.close();
         dumpProgressBar.dispose();
      }
      catch (SQLException e)
      {
         dumpProgressBar.setCanceled(true);
         MyJSQLView_Access.displaySQLErrors(e, "SQLDataDumpThread explicitStatementData()");
      }
   }

   //==============================================================
   // Class method to get the table data row count
   //==============================================================

   private int getRowsCount(Connection dbConnection, String tableName)
   {
      String sqlStatementString;
      Statement sqlStatement;
      ResultSet rs;
      int rowCount = 0;
      
      try
      {
         sqlStatement = dbConnection.createStatement();
         sqlStatementString = "SELECT COUNT(*) FROM " + tableName;
         // System.out.println(sqlStatementString);

         rs = sqlStatement.executeQuery(sqlStatementString);
         rs.next();
         rowCount = rs.getInt(1);

         rs.close();
         sqlStatement.close();
         return rowCount;
      }
      catch (SQLException e)
      {
         MyJSQLView_Access.displaySQLErrors(e, "SQLDataDumpThread getRowsCount()");
         return rowCount;  
      }
   }

   //==============================================================
   // Class method to get the table data row count
   //==============================================================

   private void dumpBinaryData(byte[] theBytes)
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
               if (MyJSQLView_Access.getSubProtocol().equals("postgresql"))
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
            if (MyJSQLView_Access.getSubProtocol().equals("postgresql") ||
                MyJSQLView_Access.getSubProtocol().indexOf("hsql") != -1)
               dumpData = dumpData + "', ";
            else if (MyJSQLView_Access.getSubProtocol().indexOf("oracle") != -1 &&
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

   protected String generateHeaders()
   {
      // Class Method Instances.
      String dateTime, headers;
      SimpleDateFormat dateTimeFormat;

      // Create Header.
      dateTimeFormat = new SimpleDateFormat("yyyy.MM.dd G 'at' hh:mm:ss z");
      dateTime = dateTimeFormat.format(new Date());

      headers = "--\n" + "-- MyJSQLView SQL Dump\n" + "-- Version: " + myJSQLView_Version[1] 
                + "\n" + "-- WebSite: http://myjsqlview.org\n" 
                + "--\n" + "-- Host: " + MyJSQLView_Access.getHostName() + "\n" 
                + "-- Generated On: " + dateTime + "\n" + "-- SQL version: " 
                + MyJSQLView_Access.getDBProductName_And_Version() + "\n"
                + "-- Database: " + MyJSQLView_Access.getDBName() + "\n" 
                + "--\n\n" + "-- ------------------------------------------\n";

      // System.out.println(headers);
      return headers;
   }

   //==============================================================
   // Class method for generating
   //==============================================================

   protected String genCommentSep(String str)
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