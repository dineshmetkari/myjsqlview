//=================================================================
//                SQL DatabaseSchemeDumpThread
//=================================================================
//   This class provides a thread to safely dump the current
// database scheme, all tables, to a local file in SQL format. A
// status dialog with cancel is provided to provide the ability
// to prematurely terminate the dump.
//
//            << SQLDatabaseSchemeDumpThread.java >>
//
//=================================================================
// Copyright (C) 2007-2010 Dana M. Proctor
// Version 1.4 02/18/2010
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
// Version 1.0 Original SQLDatabaseSchemeDumpThread Class.
//         1.1 Corrected identifierQuoteString Being Obtained From MyJSQLView_Access
//             Rather From the DbTablesPanel.getDataExportProperties().
//             Renamed tp dbIdentifierQuoteString.
//         1.2 Moved DROP TABLE Statement to TableDefinitionGenerator Class.
//         1.3 Header Format Changes/Update.
//         1.4 Changed Package to Reflect Dandy Made Productions Code.
//                         
//-----------------------------------------------------------------
//                    danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview;

import java.sql.*;
import java.util.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *    The SQLDatabaseSchemeDumpThread class provides a thread to safely
 * dump the current database scheme, all tables, to a local file in SQL
 * format. A status dialog with cancel is provided to provide the ability
 * to prematurely terminate the dump.
 * 
 * @author Dana Proctor
 * @version 1.4 02/18/2010
 */

class SQLDatabaseSchemeDumpThread implements Runnable
{
   // Class Instances.
   Thread dumpThread;
   String fileName;
   String[] myJSQLView_Version;

   private MyJSQLView_ProgressBar databaseDumpProgressBar;

   //==============================================================
   // SQLDatabaseSchemeDumpThread Constructor.
   //==============================================================

   SQLDatabaseSchemeDumpThread(String fileName, String[] myJSQLView_Version)
   {
      this.fileName = fileName;
      this.myJSQLView_Version = myJSQLView_Version;

      // Create and start the class thread.
      dumpThread = new Thread(this, "SQLDatabaseSchemeDumpThread");
      // System.out.println("SQL Data Dumb Thread");

      dumpThread.start();
   }

   //==============================================================
   // Class method for normal start of the thread
   //==============================================================

   public void run()
   {
      // Class Method Instances.
      Iterator tableNamesIterator;
      String exportedTable, dbIdentifierQuoteString;
      Object dumpData;

      // Get Connection to Database.
      Connection dbConnection = MyJSQLView_Access.getConnection("DatabaseSchemeDumpThread run()");

      if (dbConnection == null)
         return;

      //identifierQuoteString = DBTablesPanel.getDataExportProperties().getIdentifierQuoteString();
      dbIdentifierQuoteString = MyJSQLView_Access.getIdentifierQuoteString();

      // Create a progress bar for giving the user a
      // visual and cancel ability.
      databaseDumpProgressBar = new MyJSQLView_ProgressBar("SQL Database Scheme Dump");

      // Setup the dump Class and Header.
      dumpData = generateHeaders(dbConnection);

      // Start a progress bar for tracking/canceling.
      databaseDumpProgressBar.setTaskLength(MyJSQLView_Access.getTableNames().size());
      databaseDumpProgressBar.pack();
      databaseDumpProgressBar.center();
      databaseDumpProgressBar.setVisible(true);

      // Cycle through the tables, scheme dumping.
      int i = 0;
      tableNamesIterator = MyJSQLView_Access.getTableNames().iterator();

      while (tableNamesIterator.hasNext() && !databaseDumpProgressBar.isCanceled())
      {
         databaseDumpProgressBar.setCurrentValue(i + 1);

         // Properly construct the schema.table.

         exportedTable = (String) tableNamesIterator.next();
         if (exportedTable.indexOf(".") != -1)
         {
            exportedTable = dbIdentifierQuoteString + exportedTable.substring(0, exportedTable.indexOf("."))
                            + dbIdentifierQuoteString + "." + dbIdentifierQuoteString
                            + exportedTable.substring(exportedTable.indexOf(".") + 1) + dbIdentifierQuoteString;
         }
         else
            exportedTable = dbIdentifierQuoteString + exportedTable + dbIdentifierQuoteString;

         dumpData = dumpData + genCommentSep("Table structure for table " + exportedTable);

         dumpData = dumpData
                    + (new TableDefinitionGenerator(dbConnection, exportedTable)).getTableDefinition();
         i++;
      }

      // Write the dump to the file.
      
      if (!databaseDumpProgressBar.isCanceled())
         WriteDataFile.mainWriteDataString(fileName, ((String) dumpData).getBytes(), false);
      databaseDumpProgressBar.dispose();

      // Closing up.
      try
      {
         dbConnection.close();
      }
      catch (SQLException e)
      {
         MyJSQLView_Access.closeConnection(dbConnection, "DatabaseSchemeDumpThread run()");
      }

      MyJSQLView_Access.closeConnection(dbConnection, "DatabaseSchemeDumpThread run()");
   }

   //==============================================================
   // Class method for generating dump header info
   //==============================================================

   protected String generateHeaders(Connection dbConnection)
   {
      // Class Method Instances.
      String dateTime, headers;
      SimpleDateFormat dateTimeFormat;

      // Create Header.
      dateTimeFormat = new SimpleDateFormat("yyyy.MM.dd G 'at' hh:mm:ss z");
      dateTime = dateTimeFormat.format(new Date());

      headers = "--\n" + "-- MyJSQLView SQL Dump\n" + "-- Version: " + myJSQLView_Version[1] + "\n"
                + "-- WebSite: http://myjsqlview.sourceforge.net\n" + "--\n" + "-- Host: "
                + MyJSQLView_Access.getHostName() + "\n" + "-- Generated On: " + dateTime + "\n"
                + "-- SQL version: " + MyJSQLView_Access.getDBProductName_And_Version() + "\n"
                + "-- Database: " + MyJSQLView_Access.getDBName() + "\n" + "--\n\n"
                + "-- ------------------------------------------\n";

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
}