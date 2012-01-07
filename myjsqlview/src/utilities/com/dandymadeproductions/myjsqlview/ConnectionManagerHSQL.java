//=================================================================
//                   ConnectionManagerHSQL
//=================================================================
//    This class provides a specific instance to manage connections
// to a HSQL database.
//
//               << ConnectionManagerHSQL.java >>
//
//=================================================================
// Copyright (C) 2005-2012 Dana M. Proctor
// Version 1.0 01/06/2012
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
// Version 1.0 Initial ConnectionManagerHSQL Class.
//        
//-----------------------------------------------------------------
//                 danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.JOptionPane;

/**
 *    The ConnectionManagerHSQL class provides a specific instance to
 * manage connections to a HSQL database.
 * 
 * @author Dana M. Proctor
 * @version 1.0 01/06/2012
 */

public class ConnectionManagerHSQL
{
   // Class Instances.
   private static Connection memoryConnection;
   private static ConnectionProperties connectionProperties = new ConnectionProperties();

   private static String dbProductNameVersion;
   private static String catalog, schemaPattern, tableNamePattern;
   private static String[] tableTypes;
   private static Vector<String> schemas = new Vector<String>();
   private static Vector<String> tables = new Vector<String>();

   private static String catalogSeparator;
   private static String identifierQuoteString;
   private static boolean filter = true;
   private static boolean debug = true;

   public static final String HSQL = "hsql";
   public static final String HSQL2 = "hsql2";
   public static final String MEMORY = "Memory";

   private static final String TABLE_SCHEM = "TABLE_SCHEM";
   private static final String TABLE_NAME = "TABLE_NAME";
   private static final String TABLE_TYPE = "TABLE_TYPE";

   private static final String DRIVER = "org.hsqldb.jdbcDriver";
   private static final String PROTOCOL = "jdbc";
   private static final String SUBPROTOCOL = "hsqldb:hsql";
   private static final String HOST = "localhost";
   private static final String PORT = "9001";
   private static final String DB = "mem:dbname";
   private static final String USER = "sa";
   private static final String PASSWORD = " ";
   private static final String SSH = "false";

   //==============================================================
   // ConnectionManagerHSQL Constructor
   //==============================================================

   protected ConnectionManagerHSQL(String memoryDatabase)
   {
      // Constructor Instances
      String passwordString;
      String connectionString;
      Connection dbConnection;
      DatabaseMetaData dbMetaData;

      // Load Driver.
      try
      {
         Class.forName(DRIVER);
         // System.out.println("Driver Loaded");
      }
      catch (ClassNotFoundException e)
      {
         // Alert Dialog Output.
         String exceptionString = e.getMessage();
         if (exceptionString != null && exceptionString.length() > 200)
            exceptionString = exceptionString.substring(0, 200);

         String optionPaneStringErrors = "Unable to Find or Load JDBC Driver" + "\n"
                                         + "Insure the Appropriate HSQL JDBC Driver is " + "Located in the "
                                         + "\n" + "MyJSQLView installation lib directory." + "\n"
                                         + "Exeception: " + exceptionString;
         JOptionPane.showMessageDialog(null, optionPaneStringErrors, "Alert", JOptionPane.ERROR_MESSAGE);
         return;
      }

      // Connection Requirements.
      try
      {
         connectionString = PROTOCOL + ":";

         if (memoryDatabase.equals(MEMORY))
         {
            passwordString = PASSWORD.replaceAll("%", "%" + Integer.toHexString(37));
            connectionString += "hsqldb:" + DB + "?user=" + USER + "&password=" + passwordString + "&useSSL="
                                + SSH;
         }
         else
         {
            passwordString = PASSWORD.replaceAll("%", "%" + Integer.toHexString(37));
            connectionString += SUBPROTOCOL + "://" + HOST + ":" + PORT + "/" + DB + "?user=" + USER
                                + "&password=" + passwordString + "&useSSL=" + SSH;
         }
         // System.out.println(connectionString);

         connectionProperties.setConnectionString(connectionString);
         connectionProperties.setProperty(ConnectionProperties.DRIVER, DRIVER);
         connectionProperties.setProperty(ConnectionProperties.PROTOCOL, PROTOCOL);
         connectionProperties.setProperty(ConnectionProperties.SUBPROTOCOL, SUBPROTOCOL);
         connectionProperties.setProperty(ConnectionProperties.HOST, HOST);
         connectionProperties.setProperty(ConnectionProperties.PORT, PORT);
         connectionProperties.setProperty(ConnectionProperties.DB, DB);
         connectionProperties.setProperty(ConnectionProperties.USER, USER);
         connectionProperties.setProperty(ConnectionProperties.PASSWORD, passwordString);
         connectionProperties.setProperty(ConnectionProperties.SSH, SSH);

         dbConnection = DriverManager.getConnection(connectionString);

         // Collect Database Information
         dbMetaData = dbConnection.getMetaData();

         // Product
         if (dbMetaData.getDatabaseProductName() != null)
            dbProductNameVersion = dbMetaData.getDatabaseProductName() + " ";
         else
         {
            dbProductNameVersion = "HSQL ";
         }
         if (dbMetaData.getDatabaseProductVersion() != null)
            dbProductNameVersion += dbMetaData.getDatabaseProductVersion();

         setDBProductName_And_Version(dbProductNameVersion);
         // System.out.println("DB Product Name & Version: " +
         // dbProductNameVersion);

         // Separator
         catalogSeparator = dbMetaData.getCatalogSeparator();
         if (catalogSeparator == null || catalogSeparator.equals(""))
            catalogSeparator = ".";

         setCatalogSeparator(catalogSeparator);
         // System.out.println("Catalog Separator: " + catalogSeparator);

         // Identifier
         identifierQuoteString = dbMetaData.getIdentifierQuoteString();
         if (identifierQuoteString == null || identifierQuoteString.equals(" "))
            identifierQuoteString = "";

         setIdentifierQuoteString(identifierQuoteString);
         // System.out.println("Identifier Quote String: " +
         // identifierQuoteString);

         // Load parameters and the databases tables.
         loadDBParameters(dbConnection);
         loadDBTables(dbConnection);

         // Setting Memory Connection.
         if (memoryDatabase.equals(MEMORY))
            setMemoryConnection(dbConnection);
      }
      catch (SQLException e)
      {
         displaySQLErrors(e, "ConnectionManagerHSQL constructor()");
      }
   }

   //==============================================================
   // Class method that provides the ability to make a valid
   // connection to the HSQL database. A test should be made for
   // any class accessing this method for a null return, no
   // connection made.
   //==============================================================

   public static Connection getConnection(String description)
   {
      // Method Instances.
      String connectionString;
      String db, subProtocol;

      // Setup.
      connectionString = connectionProperties.getConnectionString();
      db = connectionProperties.getProperty(ConnectionProperties.DB);
      subProtocol = connectionProperties.getProperty(ConnectionProperties.SUBPROTOCOL);

      // Select and try to return an appropriate connection
      // type.

      try
      {
         if (debug)
            System.out.println(description + " Connection Created");

         // Create the appropriate connection as needed.

         // HSQL Memory Connections
         if ((memoryConnection != null)
             && (subProtocol.indexOf(HSQL) != -1 && db.toLowerCase().indexOf("mem:") != -1))
            return memoryConnection;

         // All Others
         else
            return DriverManager.getConnection(connectionString);
      }
      catch (SQLException e)
      {
         displaySQLErrors(e, "ConnectionManagerHSQL getConnection");
         return null;
      }
   }

   //==============================================================
   // Class method that provides the ability to close the
   // connection.
   //==============================================================

   public static void closeConnection(Connection dbConnection, String description)
   {
      // Method Instances.
      String db, subProtocol;

      // Setup.
      db = connectionProperties.getProperty(ConnectionProperties.DB);
      subProtocol = connectionProperties.getProperty(ConnectionProperties.SUBPROTOCOL);

      try
      {
         if (debug)
            System.out.println(description + " Connection Closed");

         // Close connection as needed.
         if ((memoryConnection != null)
             && (subProtocol.indexOf(HSQL) != -1 && db.toLowerCase().indexOf("mem:") != -1))
            return;
         else
            dbConnection.close();
      }
      catch (SQLException e)
      {
         displaySQLErrors(e, "ConnectionManagerHSQL closeConnection");
      }
   }

   //==============================================================
   // Class method to output to the console and a alert dialog the
   // errors that occured during a connection to the database.
   //==============================================================

   public static void displaySQLErrors(SQLException e, String classCaller)
   {
      String sqlExceptionString;

      // Standard Console Output.
      if (debug)
      {
         System.out.println(classCaller);
         System.out.println("SQLException: " + e.getMessage());
         System.out.println("SQLState: " + e.getSQLState());
         System.out.println("VendorError: " + e.getErrorCode());
      }

      // Alert Dialog Output.
      sqlExceptionString = e.getMessage();
      if (sqlExceptionString.length() > 200)
         sqlExceptionString = e.getMessage().substring(0, 200);

      String optionPaneStringErrors = classCaller + "\n" + "SQLException: " + sqlExceptionString + "\n"
                                      + "SQLState: " + e.getSQLState() + ",  " + "VendorError: "
                                      + e.getErrorCode();
      JOptionPane.showMessageDialog(null, optionPaneStringErrors, "Alert", JOptionPane.ERROR_MESSAGE);
   }

   //==============================================================
   // Class method that provides the ability to load/reload database
   // parameters.
   //==============================================================

   protected static void loadDBParameters(Connection dbConnection) throws SQLException
   {
      // Method Instances
      DatabaseMetaData dbMetaData;
      ResultSet db_resultSet;

      String db, subProtocol;

      // ======================================================
      // Collect the appropriate default database information.

      filter = true;
      db = connectionProperties.getProperty(ConnectionProperties.DB);
      subProtocol = connectionProperties.getProperty(ConnectionProperties.SUBPROTOCOL);

      // HSQL
      if (subProtocol.indexOf(HSQL) != -1)
      {
         catalog = null;
         schemaPattern = "%";
         tableNamePattern = "%";
         // db_resultSet = dbMetaData.getTables(null, "%", "%", tableTypes);
      }
      // Unknown
      else
      {
         catalog = null;
         schemaPattern = null;
         tableNamePattern = null;
         // db_resultSet = dbMetaData.getTables(null, null, null, tableTypes);
      }

      if (db.toLowerCase().equals("null"))
         catalog = null;

      try
      {
         dbMetaData = dbConnection.getMetaData();

         // ========================
         // Table Types, to be used.

         int i = 0;
         db_resultSet = dbMetaData.getTableTypes();
         while (db_resultSet.next())
            i++;

         tableTypes = new String[i];

         i = 0;
         db_resultSet = dbMetaData.getTableTypes();
         while (db_resultSet.next())
         {
            tableTypes[i] = db_resultSet.getString(TABLE_TYPE);
            // System.out.println("Table Types: " + tableTypes[i]);
            i++;
         }
      }
      catch (SQLException e)
      {
         throw new SQLException("ConnectionManagerHSQL loadDBParameters() " + e);
      }
   }

   //==============================================================
   // Class method that provides the ability to load/reload the
   // database schemas & tables.
   //==============================================================

   protected static void loadDBTables(Connection dbConnection) throws SQLException
   {
      // Method Instances
      DatabaseMetaData dbMetaData;
      ResultSet db_resultSet;
      String tableSchem, tableName, tableType;

      try
      {
         dbMetaData = dbConnection.getMetaData();

         // ============================
         // Obtain the databases tables.

         // System.out.println("'" + catalog + "' '" + schemaPattern + "' '" +
         // tableNamePattern + "'");
         db_resultSet = dbMetaData.getTables(catalog, schemaPattern, tableNamePattern, tableTypes);

         // Clear the tables vector and load it with the databases
         // tables.
         schemas.removeAllElements();
         tables.removeAllElements();

         while (db_resultSet.next())
         {
            tableSchem = db_resultSet.getString(TABLE_SCHEM);
            tableName = db_resultSet.getString(TABLE_NAME);
            tableType = db_resultSet.getString(TABLE_TYPE);

            // Filter, only TABLEs & VIEWs allowed for standard
            // database connection.

            if (tableType != null && !(tableType.indexOf("INDEX") != -1)
                && !(tableType.indexOf("SEQUENCE") != -1) && !(tableType.indexOf("SYNONYM") != -1)
                && (tableType.equals("TABLE") || tableType.equals("VIEW") || !filter))
            {
               // Abreviated and filtered information.
               // System.out.println(tableType + " " + tableSchem + "." +
               // tableName);

               if (tableSchem != null && !tableSchem.equals(""))
               {
                  tables.add(tableSchem + "." + tableName);
               }
               else
                  tables.add(tableName);
            }
         }

         // ============================
         // Obtain the databases schemas.

         Iterator<String> tablesIterator = tables.iterator();

         while (tablesIterator.hasNext())
         {
            tableName = tablesIterator.next();

            if (tableName.indexOf(".") != -1)
            {
               String schemasName = tableName.substring(0, tableName.indexOf("."));
               if (!schemas.contains(schemasName))
               {
                  schemas.add(tableName.substring(0, tableName.indexOf(".")));
                  // System.out.println(tableName.substring(0,
                  // tableName.indexOf(".")));
               }
            }
         }

         db_resultSet.close();
      }
      catch (SQLException e)
      {
         throw new SQLException("ConnectionManagerHSQL loadDBTables() " + e);
      }
   }

   //==============================================================
   // Class method to get the current database catalog separator.
   //==============================================================

   protected static String getCatalogSeparator()
   {
      return catalogSeparator;
   }

   //==============================================================
   // Class method to get the current connection properties.
   //==============================================================

   public static ConnectionProperties getConnectionProperties()
   {
      return connectionProperties;
   }

   //==============================================================
   // Class method to get the current data source type.
   //==============================================================

   public static String getDataSourceType()
   {
      // Method Instances.
      String subProtocol;

      subProtocol = connectionProperties.getProperty(ConnectionProperties.SUBPROTOCOL);

      if (subProtocol.indexOf(HSQL) != -1)
      {
         if (dbProductNameVersion.indexOf(" 2.") != -1)
            return HSQL2;
         else
            return HSQL;
      }
      else
         return "other";
   }

   //==============================================================
   // Class method to return the current database product name &
   // version.
   //==============================================================

   public static String getDBProductName_And_Version()
   {
      return dbProductNameVersion;
   }

   //==============================================================
   // Class method to return the current identifier quote string
   // that is used by the database.
   //==============================================================

   public static String getIdentifierQuoteString()
   {
      return identifierQuoteString;
   }

   //==============================================================
   // Class method to return a copy of the available database
   // schemas names.
   //==============================================================

   public static Vector<String> getSchemas()
   {
      Vector<String> schemasVector = new Vector<String>();
      Iterator<String> schemasIterator = schemas.iterator();

      while (schemasIterator.hasNext())
         schemasVector.addElement(schemasIterator.next());

      return schemasVector;
   }

   //==============================================================
   // Class method to return a copy of the default database table
   // names.
   //==============================================================

   public static Vector<String> getTableNames()
   {
      Vector<String> tablesVector = new Vector<String>();
      Iterator<String> tablesIterator = tables.iterator();

      while (tablesIterator.hasNext())
         tablesVector.addElement(tablesIterator.next());

      return tablesVector;
   }

   //==============================================================
   // Class method to set the current database catalog separator.
   //==============================================================

   protected static void setCatalogSeparator(String separator)
   {
      catalogSeparator = separator;
   }

   //==============================================================
   // Class method to set the current database product name &
   // version.
   //==============================================================

   public static void setConnectionProperties(ConnectionProperties properties)
   {
      connectionProperties = properties;
   }

   //==============================================================
   // Class method to set the current database product name &
   // version.
   //==============================================================

   public static void setDBProductName_And_Version(String name_And_Version)
   {
      dbProductNameVersion = name_And_Version;
   }

   //==============================================================
   // Class method to set the current user that is presently
   // logged.
   //==============================================================

   public static void setIdentifierQuoteString(String identifier)
   {
      identifierQuoteString = identifier;
   }

   //==============================================================
   // Class method to set a memory type connection.
   //==============================================================

   public static void setMemoryConnection(Connection connection)
   {
      memoryConnection = connection;
   }

   //==============================================================
   // Class method to set the schemaPattern.
   //==============================================================

   public static void setSchemaPattern(String pattern)
   {
      schemaPattern = pattern;
   }
}