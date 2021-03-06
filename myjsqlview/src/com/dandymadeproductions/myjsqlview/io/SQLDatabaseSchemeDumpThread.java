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
// Copyright (C) 2005-2015 Dana M. Proctor
// Version 2.3 09/20/2012
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
//         1.5 Organized Imports.
//         1.6 Parameterized Instance tableNamesIterator in run().
//         1.7 Class Method run() Cast Object Returned by MyJSQLView_Access.
//             getConnection() to Connection.
//         1.8 Changes to Class Method run(), to Use Newly Redefined ConnectionManager
//             to Obtain Connections and Display SQL Errors. Also identifierQuoteString
//             and Tables Names Collected From ConnectionManager. Added Methods Instances
//             connectionProperties, hostName & databaseName in generateHeaders().
//         1.9 Copyright Update.
//         2.0 Removed the Casting of (Connection) for the Returned Instance for the
//             ConnectionManager.getConnection() in run().
//         2.1 Correction in generateHeaders() for Website.
//         2.2 Changed Package Name to com.dandymadeproductions.myjsqlview.io.
//             Made Class, Constructor & Methods generateHeaders() & genCommentSep()
//             Public.
//         2.3 Created Clone of Argument myJSQLView_Version in Constructor for
//             Local Same Class Instance. Removed the Starting of Thread From
//             Constructor.
//                         
//-----------------------------------------------------------------
//                    danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview.io;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import com.dandymadeproductions.myjsqlview.datasource.ConnectionManager;
import com.dandymadeproductions.myjsqlview.datasource.ConnectionProperties;
import com.dandymadeproductions.myjsqlview.utilities.MyJSQLView_ProgressBar;
import com.dandymadeproductions.myjsqlview.utilities.TableDefinitionGenerator;

/**
 *    The SQLDatabaseSchemeDumpThread class provides a thread to safely
 * dump the current database scheme, all tables, to a local file in SQL
 * format. A status dialog with cancel is provided to provide the ability
 * to prematurely terminate the dump.
 * 
 * @author Dana Proctor
 * @version 2.3 09/20/2012
 */

public class SQLDatabaseSchemeDumpThread implements Runnable
{
   // Class Instances.
   String fileName;
   String[] myJSQLView_Version;

   private MyJSQLView_ProgressBar databaseDumpProgressBar;

   //==============================================================
   // SQLDatabaseSchemeDumpThread Constructor.
   //==============================================================

   public SQLDatabaseSchemeDumpThread(String fileName, String[] myJSQLView_Version)
   {
      this.fileName = fileName;
      this.myJSQLView_Version = myJSQLView_Version.clone();
   }

   //==============================================================
   // Class method for normal start of the thread
   //==============================================================

   public void run()
   {
      // Class Method Instances.
      Iterator<String> tableNamesIterator;
      String exportedTable, dbIdentifierQuoteString;
      Object dumpData;

      // Get Connection to Database.
      Connection dbConnection = ConnectionManager.getConnection("DatabaseSchemeDumpThread run()");

      if (dbConnection == null)
         return;

      //identifierQuoteString = DBTablesPanel.getDataExportProperties().getIdentifierQuoteString();
      dbIdentifierQuoteString = ConnectionManager.getIdentifierQuoteString();

      // Create a progress bar for giving the user a
      // visual and cancel ability.
      databaseDumpProgressBar = new MyJSQLView_ProgressBar("SQL Database Scheme Dump");

      // Setup the dump Class and Header.
      dumpData = generateHeaders(dbConnection);

      // Start a progress bar for tracking/canceling.
      databaseDumpProgressBar.setTaskLength(ConnectionManager.getTableNames().size());
      databaseDumpProgressBar.pack();
      databaseDumpProgressBar.center();
      databaseDumpProgressBar.setVisible(true);

      // Cycle through the tables, scheme dumping.
      int i = 0;
      tableNamesIterator = ConnectionManager.getTableNames().iterator();

      while (tableNamesIterator.hasNext() && !databaseDumpProgressBar.isCanceled())
      {
         databaseDumpProgressBar.setCurrentValue(i + 1);

         // Properly construct the schema.table.

         exportedTable = tableNamesIterator.next();
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
         ConnectionManager.closeConnection(dbConnection, "DatabaseSchemeDumpThread run()");
      }

      ConnectionManager.closeConnection(dbConnection, "DatabaseSchemeDumpThread run()");
   }

   //==============================================================
   // Class method for generating dump header info
   //==============================================================

   public String generateHeaders(Connection dbConnection)
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
                + "-- WebSite: http://myjsqlview.org\n" + "--\n" + "-- Host: "
                + hostName + "\n" + "-- Generated On: " + dateTime + "\n"
                + "-- SQL version: " + ConnectionManager.getDBProductName_And_Version() + "\n"
                + "-- Database: " + databaseName + "\n" + "--\n\n"
                + "-- ------------------------------------------\n";

      // System.out.println(headers);
      return headers;
   }

   //==============================================================
   // Class method for generating
   //==============================================================

   public String genCommentSep(String str)
   {
      String res;
      res = "\n--\n";
      res += "-- " + str;
      res += "\n--\n\n";
      return res;
   }
}
