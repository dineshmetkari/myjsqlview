//=================================================================
//                  TableClearingThread
//=================================================================
//   This class provides a thread to operate in the background to
// drop temporary tables that have been generated during the use
// of the Query Tool.
//
//                 << TableClearingThread.java >>
//
//=================================================================
// Copyright (C) 2007-2012 Dana M. Proctor
// Version 1.9 01/01/2012
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
// Version 1.0 Original TableClearingThread Class.
//         1.1 MyJSQLView Project Common Source Code Formatting.
//         1.2 Header Format Changes/Update.
//         1.3 Changed Package to Reflect Dandy Made Productions Code.
//         1.4 Organized Imports.
//         1.5 Class Method flushPrivileges() Cast Object Returned by MyJSQLView_Access.
//             getConnection() to Connection.
//         1.6 Methods run(), clearHSQLDBMemoryTables(), & clearOracleDBTemporaryTables()
//             Changed the Collection of Connections and Displaying SQL Errors
//             from New Class ConnectionManager. Also identifierQuoteString in Method
//             clearHSQL/OracleDBMemoryTables(). Added Instance subProtocol to run().
//         1.7 Replaced Method Instance subProtocol in run() With dataSourceType.
//         1.8 Change for HSQL Conditional Check in run() from equals to indexOf.
//         1.9 Copyright Update.
//                         
//-----------------------------------------------------------------
//                  danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *    This class provides a thread to operate in the background to
 * drop temporary tables that have been generated during the use
 * of the Query Tool.
 * 
 * @author Dana Proctor
 * @version 1.9 01/01/2012
 */

class TableClearingThread implements Runnable
{
   // Class Instances.
   Thread clearTableThread;
   int tabNumber;

   //==============================================================
   // TableClearingThread Constructor.
   //==============================================================

   TableClearingThread(int tabNumber)
   {
      this.tabNumber = tabNumber;

      // Create and start the class thread.
      clearTableThread = new Thread(this, "SQLDataDumpThread");
      // System.out.println("ClearTable Thread");

      clearTableThread.start();
   }

   //==============================================================
   // Class method for normal start of the thread
   //==============================================================

   public void run()
   {
      // Method Instances.
      String dataSourceType;
      
      // Get Connection to Database & Export Options.
      Connection dbConnection = (Connection) ConnectionManager.getConnection("ClearingTableThread run()");

      // Remove the appropriate Memory/Temporary Table(s) for HSQL
      // or Oracle databases.
      
      dataSourceType = ConnectionManager.getDataSourceType();
      
      if (dataSourceType.indexOf(ConnectionManager.HSQL) != -1)
         clearHSQLDBMemoryTables(dbConnection);

      if (dataSourceType.equals(ConnectionManager.ORACLE))
         clearOracleDBTemporaryTables(dbConnection);

      // Closing down.
      ConnectionManager.closeConnection(dbConnection, "ClearingTableThread run()");
   }

   //==============================================================
   // Class method to remove Memory tables and sequences created
   // with HSQL database.
   //==============================================================

   private void clearHSQLDBMemoryTables(Connection dbConnection)
   {
      Statement sqlStatement;

      while (tabNumber >= 0)
      {
         try
         {
            sqlStatement = dbConnection.createStatement();
            sqlStatement.executeUpdate("DROP TABLE " 
                                       + ConnectionManager.getIdentifierQuoteString()
                                       + "temptable" + tabNumber
                                       + ConnectionManager.getIdentifierQuoteString() 
                                       + " IF EXISTS");
            sqlStatement.executeUpdate("DROP SEQUENCE id_" + tabNumber-- + " IF EXISTS");
            sqlStatement.close();
         }
         catch (SQLException err)
         {
            ConnectionManager.displaySQLErrors(err, "QueryFrame clearHSQLDBMemoryTables()");
         }
      }
   }

   //==============================================================
   // Class method to remove temporary tables and sequences created
   // with Oracle database.
   //==============================================================

   private void clearOracleDBTemporaryTables(Connection dbConnection)
   {
      // Method Instances
      String sqlStatementString;
      String identifierQuoteString;
      Statement sqlStatement;
      ResultSet rs;

      // Get the tab count and removing tables
      // as needed.
      identifierQuoteString = ConnectionManager.getIdentifierQuoteString();

      try
      {
         sqlStatement = dbConnection.createStatement();

         while (tabNumber >= 0)
         {
            // Query for table and remove as needed.

            sqlStatementString = "SELECT OBJECT_NAME FROM USER_OBJECTS " 
                                 + "WHERE OBJECT_NAME='temptable"
                                 + tabNumber + "' AND OBJECT_TYPE='TABLE'";
            rs = sqlStatement.executeQuery(sqlStatementString);

            if (rs.next())
            {
               sqlStatementString = "DROP TABLE " + identifierQuoteString 
                                    + "temptable" + tabNumber
                                    + identifierQuoteString + " PURGE";
               sqlStatement.executeUpdate(sqlStatementString);
            }

            // Query for sequence and remove as needed.
            /*
             * sqlStatementString = "SELECT OBJECT_NAME FROM USER_OBJECTS " +
             * "WHERE OBJECT_NAME='id_" + tabNumber + "'" + " AND
             * OBJECT_TYPE='SEQUENCE'"; rs =
             * sqlStatement.executeQuery(sqlStatementString); if (rs.next()) {
             * sqlStatementString = "DROP SEQUENCE " + identifierQuoteString +
             * "id_" + tabNumber + identifierQuoteString;
             * sqlStatement.executeUpdate(sqlStatementString); }
             */
            tabNumber--;
            rs.close();
         }
         sqlStatement.close();
      }
      catch (SQLException err)
      {
         ConnectionManager.displaySQLErrors(err, "QueryFrame clearOracleDBTemporaryTables()");
      }
   }
}