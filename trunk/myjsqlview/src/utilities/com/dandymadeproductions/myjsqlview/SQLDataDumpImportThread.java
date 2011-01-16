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
// Copyright (C) 2006-2011 Borislav Gizdov, Dana M. Proctor
// Version 3.9 01/15/2011
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
//          
//-----------------------------------------------------------------
//             poisonerbg@users.sourceforge.net
//              danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview;

import java.io.File;
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
 * @version 3.9 01/15/2011
 */

class SQLDataDumpImportThread implements Runnable
{
   // Class Instance Fields.
   Thread importThread;
   String fileName, failedQuery;
   boolean validImport, reloadDatabase;

   //==============================================================
   // SQLDataDumpImportThread Constructor.
   //==============================================================

   SQLDataDumpImportThread(String fileName, boolean reloadedDatabase)
   {
      this.fileName = fileName;
      this.reloadDatabase = reloadedDatabase;

      importThread = new Thread(this, "SQLDataDumpImportThread");
      // System.out.println("SQL Data Import Thread");

      failedQuery = "";
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

      String sqlDump;
      String[] queries;
      int currentLine = 0;
      byte[] dump;

      MyJSQLView_ProgressBar sqlDumpProgressBar;

      // Obtain the data bytes from the selected file.
      dump = ReadDataFile.mainReadDataString(fileName, true);

      // Begin the separation of SQL statement queries and
      // execution for import.
      if (dump != null)
      {
         // Obtain database connection & setting up.
         dbConnection = (Connection) MyJSQLView_Access.getConnection(
            "SQLDataDumpImportThread importSQLFile()");
         
         if (dbConnection == null)
         {
            validImport = false;
            return;
         }

         sqlDumpProgressBar = new MyJSQLView_ProgressBar("SQL Import");
         sqlDump = new String(dump);
         // System.out.println(sqlDump);

         try
         {
            // Disable autocommit and begin the start
            // of transactions.
            dbConnection.setAutoCommit(false);
            sqlStatement = dbConnection.createStatement();

            // HSQL, Oracle, & SQLite does not support.
            
            if (MyJSQLView_Access.getSubProtocol().indexOf("hsql") == -1 &&
                MyJSQLView_Access.getSubProtocol().indexOf("oracle") == -1 &&
                MyJSQLView_Access.getSubProtocol().indexOf("sqlite") == -1)
               sqlStatement.executeUpdate("BEGIN");

            // Creating seperate queries and beginning
            // the monitor of the dump.
            queries = separateQueries(sqlDump);

            sqlDumpProgressBar.setTaskLength(queries.length);
            sqlDumpProgressBar.pack();
            sqlDumpProgressBar.center();
            sqlDumpProgressBar.setVisible(true);
            validImport = true;

            // Cycle through the queries and execute
            // the SQL statement. Note commented queries
            // will probably throw an exception if on
            // their own. This is relatively easily fixed
            // here, but the questions then lies in what
            // chararcters represent a comment for a
            // line. Preferences? Addressed this slightly,
            // 3.5, For Oracle Only.

            for (int i = 0; i < queries.length; i++)
            {
               // Check to see if user wishes to stop.
               if (sqlDumpProgressBar.isCanceled())
               {
                  validImport = false;
                  break;
               }

               sqlDumpProgressBar.setCurrentValue(i);
               currentLine = i;
               
               queries[i] = queries[i].trim();

               if (!queries[i].equals(""))
               {
                  // Save the query in case exception thrown.
                  if (queries[i].length() > 50)
                     failedQuery = queries[i].substring(0, 50);
                  else
                     failedQuery = queries[i];

                  // Process the query. Remove any ending semicolons for
                  // Oracle SQL statements and bypassing any single commented
                  // SQL statement lines in Same Also.
                  
                  if (MyJSQLView_Access.getSubProtocol().indexOf("oracle") != -1)
                  {
                     if ((queries[i].substring(0, 2)).matches("^-{2}?") && queries[i].indexOf("\n") == -1)
                         continue;
                     else
                        if (queries[i].endsWith(";"))
                           queries[i] = (queries[i].substring(0, queries[i].length() -1));
                  }
                  sqlStatement.execute(queries[i]);
               }
            }
            sqlDumpProgressBar.dispose();

            // Commiting the transactions as necessary
            // and cleaning up.

            if (validImport)
               dbConnection.commit();
            else
               dbConnection.rollback();

            sqlStatement.close();
            dbConnection.setAutoCommit(true);

            MyJSQLView_Access.closeConnection(dbConnection, "SQLDataDumpImportThread importSQLFile()");
         }
         catch (SQLException e)
         {
            sqlDumpProgressBar.dispose();
            validImport = false;
            MyJSQLView_Access.displaySQLErrors(e, "line# " + currentLine + " " + failedQuery
                                                  + " SQLDataDumpImportThread importSQLFile()");
            try
            {
               dbConnection.rollback();
               dbConnection.setAutoCommit(true);
               MyJSQLView_Access.closeConnection(dbConnection, "SQLDataDumpImportThread importSQLFile() rollback");
            }
            catch (SQLException error)
            {
               MyJSQLView_Access.displaySQLErrors(e, "SQLDataDumpImportThread importSQLFile() rollback failed");
            }
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

   //==============================================================
   // Class method for separating each query.
   //==============================================================

   private String[] separateQueries(String dump)
   {
      /*
       * Pattern pattern; Patcher matcher; pattern = Pattern.compile("(.*);",
       * Pattern.MULTILINE); matcher = pattern.matcher(dump); while
       * (matcher.find()) { System.out.println("-------------------");
       * System.out.println(matcher.group() + "\n"); }
       */

      String[] queries;
      // FIXME must be better solutiin with regex
      // this will fail if have ";" in some row
      // I'm not so sure I did some testing and seems ok, dmp.
      queries = dump.split(";\n");
      return queries;
   }
}