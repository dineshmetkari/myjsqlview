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
// Version 1.4 05/12/2012
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
//         1.1 Made Constructor Public.
//         1.2 Modified to Handle Connection Type Memory, File, Resource, and Server
//             Types. Major Changes Throughout, Made Class Methods loadDBParameters()
//             & loadDBTables() Private. Removed Most static Instances and Methods.
//         1.3 Changed Class Instances schemas & tables from Vector to ArrayList
//             Data Types. Same Change for Return Argument in Methods getSchemas()
//             & getTableNames() Along With Their Instances schemas & tables.
//         1.4 Added Class Instance maxColumnNameLength & Corresponding getter/
//             setter Methods.
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
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *    The ConnectionManagerHSQL class provides a specific instance to
 * manage connections to a HSQL database.
 * 
 * @author Dana M. Proctor
 * @version 1.4 05/12/2012
 */

public class ConnectionManagerHSQL
{
   // Class Instances.
   private static Connection memoryConnection;
   private static ConnectionProperties connectionProperties = new ConnectionProperties();

   private String dbProductNameVersion;
   private String schemaPattern, tableNamePattern;
   private String[] tableTypes;
   private ArrayList<String> schemas;
   private ArrayList<String> tables;

   private String catalogSeparator;
   private String identifierQuoteString;
   private int maxColumnNameLength;
   private static boolean debug = false;

   public static final String HSQL = "hsql";
   public static final String HSQL2 = "hsql2";
   public static final String MEMORY = "Memory";
   public static final String FILE = "File";
   public static final String RESOURCE = "Resource";

   private static final String TABLE_SCHEM = "TABLE_SCHEM";
   private static final String TABLE_NAME = "TABLE_NAME";
   private static final String TABLE_TYPE = "TABLE_TYPE";

   private static final String DRIVER = "org.hsqldb.jdbcDriver";
   private static final String PROTOCOL = "jdbc";
   private static final String SUBPROTOCOL = "hsqldb:hsql";
   private static final String HOST = "localhost";
   private static final String PORT = "9001";
   private static final String DB = "dbname";
   private static final String USER = "sa";
   private static final String PASSWORD = " ";
   private static final String SSH = "false";

   //==============================================================
   // ConnectionManagerHSQL Constructors
   // databaseType - HSQL, MEMORY, FILE, RESOURCE.
   //==============================================================
   
   public ConnectionManagerHSQL()
   {
      // Default to a Memory.
      this(MEMORY, HOST, PORT, DB, USER, PASSWORD);
   }
   
   public ConnectionManagerHSQL(String databaseType, String databaseName)
   {
      // Easy Entry for Memory, File, or Resource.
      this(databaseType, HOST, PORT, databaseName, USER, PASSWORD);
   }

   public ConnectionManagerHSQL(String databaseType, String host, String port, String databaseName,
                                String user, String password)
   {
      // Constructor Instances
      String passwordString;
      String connectionString;
      Connection dbConnection;
      DatabaseMetaData dbMetaData;
      
      // Setup
      schemas = new ArrayList<String>();
      tables = new ArrayList<String>();

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
         passwordString = password.replaceAll("%", "%" + Integer.toHexString(37));
         connectionString = PROTOCOL + ":";
         
         if (databaseType.equals(MEMORY))
         {
            connectionString += "hsqldb:mem:" + databaseName + "?user=" + user + "&password=" + passwordString
                                 + "&useSSL=" + SSH;
         }
         else if (databaseType.equals(FILE))
         {
            connectionString += "hsqldb:file:" + databaseName + "?user=" + user + "&password=" + passwordString
            + "&useSSL=" + SSH;
         }
         else if (databaseType.equals(RESOURCE))
         {
            connectionString += "hsqldb:res:" + databaseName + "?user=" + user + "&password=" + passwordString
            + "&useSSL=" + SSH;
         }
         // Default to HSQL server.
         else
         {
            connectionString += SUBPROTOCOL + "://" + host + ":" + port + "/" + databaseName + "?user=" + user
                                + "&password=" + passwordString + "&useSSL=" + SSH;
         }
         System.out.println(connectionString);

         connectionProperties.setConnectionString(connectionString);
         connectionProperties.setProperty(ConnectionProperties.DRIVER, DRIVER);
         connectionProperties.setProperty(ConnectionProperties.PROTOCOL, PROTOCOL);
         connectionProperties.setProperty(ConnectionProperties.SUBPROTOCOL, SUBPROTOCOL);
         connectionProperties.setProperty(ConnectionProperties.HOST, host);
         connectionProperties.setProperty(ConnectionProperties.PORT, port);
         connectionProperties.setProperty(ConnectionProperties.DB, databaseName);
         connectionProperties.setProperty(ConnectionProperties.USER, user);
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
         if (databaseType.equals(MEMORY))
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

   private void loadDBParameters(Connection dbConnection) throws SQLException
   {
      // Method Instances
      DatabaseMetaData dbMetaData;
      ResultSet db_resultSet;

      String subProtocol;

      // ======================================================
      // Collect the appropriate default database information.

      subProtocol = connectionProperties.getProperty(ConnectionProperties.SUBPROTOCOL);

      // HSQL
      if (subProtocol.indexOf(HSQL) != -1)
      {
         schemaPattern = "%";
         tableNamePattern = "%";
         // db_resultSet = dbMetaData.getTables(null, "%", "%", tableTypes);
      }
      // Unknown
      else
      {
         schemaPattern = null;
         tableNamePattern = null;
         // db_resultSet = dbMetaData.getTables(null, null, null, tableTypes);
      }

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

   private void loadDBTables(Connection dbConnection) throws SQLException
   {
      // Method Instances
      DatabaseMetaData dbMetaData;
      ResultSet db_resultSet;
      String tableSchem, tableName, tableType;
      boolean filter = true;

      try
      {
         dbMetaData = dbConnection.getMetaData();

         // ============================
         // Obtain the databases tables.

         // System.out.println("'" + catalog + "' '" + schemaPattern + "' '" +
         // tableNamePattern + "'");
         db_resultSet = dbMetaData.getTables(null, schemaPattern, tableNamePattern, tableTypes);

         // Clear the tables vector and load it with the databases
         // tables.
         schemas.clear();
         tables.clear();

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

   public String getCatalogSeparator()
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

   public String getDataSourceType()
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

   public String getDBProductName_And_Version()
   {
      return dbProductNameVersion;
   }

   //==============================================================
   // Class method to return the current identifier quote string
   // that is used by the database.
   //==============================================================

   public String getIdentifierQuoteString()
   {
      return identifierQuoteString;
   }
   
   //==============================================================
   // Class method to return the max column name length that is
   // used by the database.
   //==============================================================

   public int getMaxColumnNameLength()
   {
      return maxColumnNameLength;
   }

   //==============================================================
   // Class method to return a copy of the available database
   // schemas names.
   //==============================================================

   public ArrayList<String> getSchemas()
   {
      ArrayList<String> schemasVector = new ArrayList<String>();
      Iterator<String> schemasIterator = schemas.iterator();

      while (schemasIterator.hasNext())
         schemasVector.add(schemasIterator.next());

      return schemasVector;
   }

   //==============================================================
   // Class method to return a copy of the default database table
   // names.
   //==============================================================

   public ArrayList<String> getTableNames()
   {
      ArrayList<String> tablesVector = new ArrayList<String>();
      Iterator<String> tablesIterator = tables.iterator();

      while (tablesIterator.hasNext())
         tablesVector.add(tablesIterator.next());

      return tablesVector;
   }

   //==============================================================
   // Class method to set the current database catalog separator.
   //==============================================================

   public void setCatalogSeparator(String separator)
   {
      catalogSeparator = separator;
   }

   //==============================================================
   // Class method to set the connection properties.
   //==============================================================

   public static void setConnectionProperties(ConnectionProperties properties)
   {
      connectionProperties = properties;
   }

   //==============================================================
   // Class method to set the current database product name &
   // version.
   //==============================================================

   public void setDBProductName_And_Version(String name_And_Version)
   {
      dbProductNameVersion = name_And_Version;
   }

   //==============================================================
   // Class method to set the current user that is presently
   // logged.
   //==============================================================

   public void setIdentifierQuoteString(String identifier)
   {
      identifierQuoteString = identifier;
   }
   
   //==============================================================
   // Class method to set the max column name length that the
   // application will use.
   //==============================================================

   protected void setMaxColumnNameLength(int maxLength)
   {
      maxColumnNameLength = maxLength;
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

   public void setSchemaPattern(String pattern)
   {
      schemaPattern = pattern;
   }
}