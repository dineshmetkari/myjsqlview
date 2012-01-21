//=================================================================
//                    SQLDataDumpImportThread
//=================================================================
//
//    This class provide the means to import a sql dump file into
// the current selected MyJSQLView database via a safe thread method.
// A progress bar is offered to address the ability to cancel the
// import.
//
//              << SQLDataDumpImportThread.java >>
//
//=================================================================
// Copyright (C) 2006-2012 Borislav Gizdov, Dana M. Proctor
// Version 5.0 01/12/2012
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
// Version 1.0 Original SQLDataDumpImportThread Class.
//         1.1 Minor Format/My Comments Changes. Added to CVS.
//         1.2 Added progress bar and transaction.
//         1.3 Several Checks at Reading File and Data Dump
//             Cancel. Class Instance validDump.
//         1.4 Class Method run() JOptionPane, msg Change.
//         1.5 Cleaned & Reviewed for Bug in 2.64 on FileSize Errors.
//             The SQL Statements Are Separated Properly. Must Use
//             Explicit SQL Statements for Large File Data Imports.
//         1.6 Class Instances validDump Changed to validImport. Check
//             Class Method refreshTablePanel() for Null Table Tab.
//         1.7 Class Method importSQLFile Check to Include Statement
//             Execution BEGIN if the Database is MySQL.
//         1.8 Cleaned Up Javadoc Comments.
//         1.9 Set "BEGIN" SQL Statement to be a executeUpdate() in
//             Class Method importSQLFile().
//         2.0 Class Method Instance sqlDumpProgressBar Properly Set
//             to Visible & Packed in Class Method importSQLFile().
//         2.1 Header Update.
//         2.2 Removed BEGIN Statement Execution for HSQL Database
//             in Class Method importSQLFile().
//         2.3 Minor Format Change.
//         2.4 Class Method refreshTableTabPanel() get/setTableFields()
//             Changed to get/setTableHeadings().
//         2.5 Added the Class Method Instance currentLine to SQLIOException
//             Information in Class Method importSQLFile().
//         2.6 TableTabPanel getTableHeadings() to getCurrentTableHeadings()
//             in Class Method refreshTableTabPanel().
//         2.7 Exclusion of BEGIN SQL Statement for Oracle Database in
//             Class Method importSQLFile().
//         2.8 Removed the Semicolons From the Query Statements, Oracle
//             Does Not Like the Termination Delimiter. Class Method
//             importSQLFile(). There is Still A Problem With Commented
//             Queries, Right Now The Import Just Runs Them Through. They
//             Will Be Executed Which Throws and Exception.
//         2.9 Added Class Instance failedQuery.
//         3.0 Replaced MyJSQLView.getSelectedTab() With DBTablesPanel.
//             getSelectedTableTabPanel().
//         3.1 MyJSQLView Project Common Source Code Formatting.
//         3.2 Added Instance reloadDatabase and Such to Constructor Argument.
//             Also Appropriate Action on Same After Completion of Import.
//         3.3 Conditional Check for NULL dbConnection in Method importSQLFile().
//         3.4 Set Class Instance validImport to False on an SQLException in 
//             Class Method importSQLFile(). Fixes a Reload Database Tables
//             Being Blank if Import Problem.
//         3.5 Undid 2.8 to Only Remove Semicolons From the Ending of SQL Statements
//             for Oracle. Also Bypassed any Single Line Commented SQL Statements.
//             Minor Changes to queries to trim() Only Once. All in importSQLFile().
//         3.6 Changed Package to Reflect Dandy Made Productions Code.
//         3.7 Parameterized tableFields in Class Method refreshTablePanel() to
//             Bring Code Into Compliance With Java 5.0 API.
//         3.8 Updated Method importSQLFile() Removed BEGIN Statement SQL Query
//             Execution for SQLite Database.
//         3.9 Class Method importSQLFile() Cast Object Returned by MyJSQLView_Access.
//             getConnection() to Connection.
//         4.0 Class Method importSQL() Added Instance subProtocol and Collected
//             Connections and Displayed SQL Errors via the New Class ConnectionManager.
//         4.1 Removed From Method importSQLFile() Instance subProtocol & Replaced
//             With dataSourceType.
//         4.2 Class Method importSQLFile() Chopped Out Any Comments From Statements
//             & Only Allow SubString Containing INSERT | UPDATE For MS Access.
//         4.3 Addressed Commented Input Lines More Fully Since 4.2 Addressed This
//             for MS Access and HSQLDB 2.x Does Not Allow. Modifications to importSQLFile()
//             Method to Parse Commented Lines Out of Queries. Commented Lines are
//             Identified by '--', and May Need to be Addressed Again.
//         4.4 Minor Correction in Parsing From 4.3 to Accept Lines That Are Less Than
//             Two Characters.
//         4.5 Changed in Method importSQLFile() to Remove Empty Lines and Added
//             Lines That Contain One Character.
//         4.6 Correction in Catching Single Line Commented, --, Input in Method
//             importSQLFile().
//         4.7 Commented Out a System.Out in ImportSQLFile().
//         4.8 Sorry Borislav, But Complete Rebuild of importSQLFile() Method to
//             Eliminate the HeapSize Error Created by Overloading a String by the
//             Complete Data Dump. Now the Dump is Read a Single Line at a Time
//             Then Once a Query is Built is Execute, to Reiterate Again. Removed
//             Class Method separateQueries().
//         4.9 Copyright Update.
//         5.0 Removed the Casting of (Connection) for the Returned Instance for the
//             ConnectionManager.getConnection() in importSQLFile().
//          
//-----------------------------------------------------------------
//             poisonerbg@users.sourceforge.net
//              danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import javax.swing.JOptionPane;

/**
 *    The SQLDataDumpImportThread class provide the means to import
 * a sql dump file into the current selected MyJSQLView database via
 * a safe thread method. A progress bar is offered to address the
 * ability to cancel the import.
 * 
 * @author Borislav Gizdov a.k.a. PoisoneR, Dana M. Proctor
 * @version 5.0 01/12/2012
 */

class SQLDataDumpImportThread implements Runnable
{
   // Class Instance Fields.
   Thread importThread;
   String fileName;
   String dataSourceType;
   boolean validImport, reloadDatabase;

   //==============================================================
   // SQLDataDumpImportThread Constructor.
   //==============================================================

   SQLDataDumpImportThread(String fileName, boolean reloadedDatabase)
   {
      this.fileName = fileName;
      this.reloadDatabase = reloadedDatabase;
      
      dataSourceType = ConnectionManager.getDataSourceType();

      importThread = new Thread(this, "SQLDataDumpImportThread");

      importThread.start();
   }

   //==============================================================
   // Class Method for Normal Start of the Thread
   //==============================================================

   public void run()
   {
      // Class Method Instances
      File file;

      // Begin Execution of getting the
      // file and trying an import.

      file = new File(fileName);

      if (file.exists())
      {
         // Importing data dump from SQL file
         importSQLFile();

         // Refreshing database tables or selected table
         // panel to see new inserted data
         if (validImport)
         {
            if (reloadDatabase)
               MyJSQLView_Frame.reloadDBTables();
            else
               refreshTableTabPanel();
         }
      }
      else
      {
         String msg = "The file '" + fileName + "' does not exists.";
         JOptionPane.showMessageDialog(null, msg, fileName, JOptionPane.ERROR_MESSAGE);
      }
   }

   //==============================================================
   // Class method for importing a sql dump file
   //==============================================================

   private void importSQLFile()
   {
      // Class Method Instances.
      Connection dbConnection;
      Statement sqlStatement;

      FileReader fileReader;
      BufferedReader bufferedReader;

      String currentLine;
      StringBuffer query;
      StringBuffer queryStatement;
      String[] multiLineQueries;
      String failedQuery;
      int fileLineLength, line;
      
      MyJSQLView_ProgressBar sqlImportProgressBar;

      // Obtain database connection & setting up.

      dbConnection = ConnectionManager.getConnection("SQLDataDumpImportThread importSQLFile()");
      
      if (dbConnection == null)
      {
         validImport = false;
         return;
      }
      
      sqlImportProgressBar = new MyJSQLView_ProgressBar("SQL Import");
      fileLineLength = 0;
      line = 0;
      failedQuery = "";
      
      // Begin the processing of the input SQL file by reading
      // each line and checking before insert if it is valid
      // not a comment.
      
      try
      {
         // Disable autocommit and begin the start
         // of transactions.
         dbConnection.setAutoCommit(false);
         sqlStatement = dbConnection.createStatement();

         // Only MySQL & PostgreSQL supports.
         if (dataSourceType.equals(ConnectionManager.MYSQL)
               || dataSourceType.equals(ConnectionManager.POSTGRESQL))
            sqlStatement.executeUpdate("BEGIN");

         try
         {
            // Setting file reader & progress bar.
            fileReader = new FileReader(fileName);
            bufferedReader = new BufferedReader(fileReader);

            while ((currentLine = bufferedReader.readLine()) != null)
               fileLineLength++;

            sqlImportProgressBar.setTaskLength(fileLineLength);
            sqlImportProgressBar.pack();
            sqlImportProgressBar.center();
            sqlImportProgressBar.setVisible(true);
            validImport = true;

            // Beginning processing the input file for insertions
            // into the database table.

            bufferedReader.close();
            fileReader = new FileReader(fileName);
            bufferedReader = new BufferedReader(fileReader);
            
            line = 1;
            query = new StringBuffer();

            while ((currentLine = bufferedReader.readLine()) != null)
            {
               // System.out.println(currentLine);

               // Check to see if user wishes to stop.
               if (sqlImportProgressBar.isCanceled())
               {
                  validImport = false;
                  break;
               }
               
               // Collect input lines.
               query.append(currentLine + "\n");
               
               // Check to see if a complete query statement has
               // been collect, determined by ending with a semicolon.
               // Small chance that TEXT field could end with that,
               // but very low.
               
               if (!currentLine.equals("") && currentLine.endsWith(";"))
               {
                  queryStatement = new StringBuffer((query.toString()).trim());
                  queryStatement.substring(0, (queryStatement.length() - 1));
                  
                  // Process the possibly multi-line statement.

                  if (!(queryStatement.toString()).equals(""))
                  {
                     // Parse the query further to remove commented
                     // lines as needed, The standard is --, but also
                     // include /*, *, & */
                     
                     multiLineQueries = (queryStatement.toString()).split("\n");
                     
                     if (multiLineQueries.length > 1)
                     {
                        int j = 0;
                        queryStatement.delete(0, queryStatement.length());
                        
                        while (j < multiLineQueries.length)
                        {
                           multiLineQueries[j] = multiLineQueries[j].trim();
                           // System.out.println("multi: " + multiLineQueries[j]);
                           
                           // Continue with building the query.
                           
                           if (multiLineQueries[j].length() < 2 && !multiLineQueries[j].isEmpty())
                              queryStatement.append(multiLineQueries[j] + "\n");
                           else
                           {
                              if (!multiLineQueries[j].isEmpty())
                              {
                                 if (multiLineQueries[j].length() >= 2 &&
                                     !(multiLineQueries[j].substring(0, 2)).matches("^-{2}?"))
                                 {
                                    queryStatement.append(multiLineQueries[j] + "\n");
                                 }
                              }
                           }
                           j++;
                        }
                     }
                     
                     // Check for empty queries and just commented
                     // single line queries.
                     
                     if (((queryStatement.toString()).equals(""))
                         || (queryStatement.length() >= 2 && ((queryStatement.toString()).substring(0, 2)).matches("^-{2}?")))
                        continue;
                     
                     // Save the query in case exception thrown.
                     if (queryStatement.length() > 50)
                        failedQuery = queryStatement.substring(0, 50);
                     else
                        failedQuery = queryStatement.toString();

                     // Process the query.
                     
                     // System.out.println("query: " + queryStatement);
                     sqlStatement.execute(queryStatement.toString());
                  }
                  query = new StringBuffer();
               }
               sqlImportProgressBar.setCurrentValue(line++);
            }
            sqlImportProgressBar.dispose();

            // Commiting the transactions as necessary
            // and cleaning up.

            if (validImport)
               dbConnection.commit();
            else
               dbConnection.rollback();

            fileReader.close();
            bufferedReader.close();
            sqlStatement.close();
            dbConnection.setAutoCommit(true);
            ConnectionManager.closeConnection(dbConnection, "SQLDataDumpImportThread importSQLFile()");
         }
         catch (IOException e)
         {
            sqlImportProgressBar.dispose();
            JOptionPane.showMessageDialog(null, "Unable to Read Input File!", "Alert",
               JOptionPane.ERROR_MESSAGE);
            try
            {
               sqlStatement.close();
               dbConnection.rollback();
               dbConnection.setAutoCommit(true);
               ConnectionManager
                     .closeConnection(dbConnection, "SQLDataDumpImportThread importSQLFile() rollback");
            }
            catch (SQLException error)
            {
               ConnectionManager.displaySQLErrors(error,
                  "SQLDataDumpImportThread importSQLFile() rollback failed");
            }
         }
      }
      catch (SQLException e)
      {
         sqlImportProgressBar.dispose();
         ConnectionManager.displaySQLErrors(e, "line# " + line + " " + failedQuery
                                               + " SQLDataDumpImportThread importSQLFile()");
         try
         {
            dbConnection.rollback();
            dbConnection.setAutoCommit(true);
            ConnectionManager.closeConnection(dbConnection, "SQLDataDumpImportThread importSQLFile() rollback");
         }
         catch (SQLException error)
         {
            ConnectionManager.displaySQLErrors(e, "SQLDataDumpImportThread importSQLFile() rollback failed");
         }
      }
      
   }
   
   //==============================================================
   // Class method to refresh table tab panel.
   //==============================================================

   private void refreshTableTabPanel()
   {
      TableTabPanel currentTableTabPanel = DBTablesPanel.getSelectedTableTabPanel();
      if (currentTableTabPanel != null)
      {
         Vector<String> tableFields = currentTableTabPanel.getCurrentTableHeadings();
         currentTableTabPanel.setTableHeadings(tableFields);
      }
   }
}