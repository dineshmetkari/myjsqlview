//=================================================================
//                   ConnectionManagerHSQL
//=================================================================
//    This class provides a specific instance to manage connections
// to a HSQL database. Note the connection default constructor
// assumes parameters are statically defined based on defaults for
// HSQL.
//
//               << ConnectionManagerHSQL.java >>
//
//=================================================================
// Copyright (C) 2005-2013 Dana M. Proctor
// Version 2.0 09/16/2013
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
//         1.5 Constructor Change of Conditional Check for null to Empty String
//             for dbMetaData.getDatabaseProductName/Version().
//         1.6 Changed Package Name to com.dandymadeproductions.myjsqlview.datasource.
//         1.7 Added Methods shutdownDatabase(), closeMemoryConnection(), & shutdown().
//         1.8 Added Class Instance ALL_TABLE_SCHEMAS_PATTERN & Method getAllSchemasPattern().
//         1.9 Class Method shutdownDatabase() Insertion of finally to Handle Connection
//             That was Created.
//         2.0 Added Additional Constructors to More Precisely Control Instantiation.
//             Removed static Definition for Class Instances memoryConnection,
//             connectionProperties, & debug. Added static Class Instance OTHER. Removed
//             All static Methods Definitions Except for displaySQLErrors(). Added
//             boolean debug Argument to displySQLErrors() Method.
//        
//-----------------------------------------------------------------
//                 danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview.datasource;

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
 * manage connections to a HSQL database. Note the connection default
 * constructor assumes parameters are statically defined based on
 * defaults for HSQL.
 * 
 * @author Dana M. Proctor
 * @version 2.0 09/16/2013
 */

public class ConnectionManagerHSQL
{
   // Class Instances.
   private Connection memoryConnection;
   private ConnectionProperties connectionProperties;

   private String dbProductNameVersion;
   private String schemaPattern, tableNamePattern;
   private String[] tableTypes;
   private ArrayList<String> schemas;
   private ArrayList<String> tables;

   private String catalogSeparator;
   private String identifierQuoteString;
   private int maxColumnNameLength;
   private boolean debug;

   public static final String HSQL = "hsql";
   public static final String HSQL2 = "hsql2";
   public static final String OTHER = "other";
   public static final String MEMORY = "Memory";
   public static final String FILE = "File";
   public static final String RESOURCE = "Resource";

   private static final String TABLE_SCHEM = "TABLE_SCHEM";
   private static final String TABLE_NAME = "TABLE_NAME";
   private static final String TABLE_TYPE = "TABLE_TYPE";
   private static final String ALL_TABLE_SCHEMAS_PATTERN = "%";

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
   // 
   //==============================================================
   
   public ConnectionManagerHSQL()
   {
      // Default to a Memory.
      this(MEMORY, HOST, PORT, DB, USER, PASSWORD, false);
   }
   
   public ConnectionManagerHSQL(boolean debug)
   {
      // Default to a Memory.
      this(MEMORY, HOST, PORT, DB, USER, PASSWORD, debug);
   }
   
   public ConnectionManagerHSQL(String databaseType, String databaseName, boolean debug)
   {
      // Easy Entry for Memory, File, or Resource.
      this(databaseType, HOST, PORT, databaseName, USER, PASSWORD, debug);
   }

   public ConnectionManagerHSQL(String databaseType, String host, String port, String databaseName,
                                String user, String password, boolean deBug)
   {
      // Constructor Instances
      String passwordString;
      String connectionString;
      Connection dbConnection;
      DatabaseMetaData dbMetaData;
      
      // Setup
      debug = deBug;
      
      schemas = new ArrayList<String>();
      tables = new ArrayList<String>();
      connectionProperties = new ConnectionProperties();

      // Load Driver.
      try
      {
         Class.forName(DRIVER);
         if (deBug)
            System.out.println("Driver Loaded");
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
         if (deBug)
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
         if (dbMetaData.getDatabaseProductName().equals(""))
            dbProductNameVersion = dbMetaData.getDatabaseProductName() + " ";
         else
         {
            dbProductNameVersion = "HSQL ";
         }
         if (dbMetaData.getDatabaseProductVersion().equals(""))
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
         displaySQLErrors(e, "ConnectionManagerHSQL constructor()", debug);
      }
   }

   //==============================================================
   // Class method that provides the ability to make a valid
   // connection to the HSQL database. A test should be made for
   // any class accessing this method for a null return, no
   // connection made.
   //==============================================================

   public Connection getConnection(String description)
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
         displaySQLErrors(e, "ConnectionManagerHSQL getConnection", debug);
         return null;
      }
   }

   //==============================================================
   // Class method that provides the ability to close the
   // connection.
   //==============================================================

   public void closeConnection(Connection dbConnection, String description)
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
         displaySQLErrors(e, "ConnectionManagerHSQL closeConnection", debug);
      }
   }
   
   //==============================================================
   // Class method that provides the ability to attempt to shutdown
   // database activity appropriately.
   //==============================================================

   public void shutdown(String description)
   {
      closeMemoryConnection(description);
      shutdownDatabase(description);
   }
   
   //==============================================================
   // Class method that provides the ability to close the memory
   // connection upon termination of the application
   //==============================================================

   private void closeMemoryConnection(String description)
   {  
      try
      {
         // Close connection as needed.
         if (memoryConnection != null)
         {
            if (debug)
               System.out.println(description + " Memory Connection Closed");
            
            memoryConnection.close();  
         }
      }
      catch (SQLException e)
      {
         displaySQLErrors(e, "ConnectionManagerHSQL closeMemoryConnection()", debug);
      }
   }
   
   //==============================================================
   // Class method that provides the ability to shutdown Databases
   // that advocate such, file & memory types.
   //==============================================================

   private void shutdownDatabase(String description)
   {
      // Method Instances.
      Connection dbConnection;
      String connectionString;
      String databaseShutdownString;
      
      // Setup.
      connectionString = connectionProperties.getConnectionString();
      
      if (connectionString.indexOf(";") != -1)
         databaseShutdownString = connectionString.substring(0, connectionString.indexOf(";"));
      else
         databaseShutdownString = connectionString;
      
      dbConnection = null;
      
      try
      {  
         if (databaseShutdownString.toLowerCase().indexOf("file:") != -1
             || databaseShutdownString.toLowerCase().indexOf("mem:") != -1)
         {
            if (debug)
               System.out.println(description + " Shutting Down HSQL File/Memory Database");
               
            dbConnection = DriverManager.getConnection(databaseShutdownString + ";shutdown=true");
            dbConnection.close();
         }
         return; 
      }
      catch (SQLException e)
      {
         displaySQLErrors(e, "ConnectionManagerHSQL shutdownDatabase()", debug);
      }
      finally
      {
         try
         {
            if (dbConnection != null)
               dbConnection.close();
         }
         catch (SQLException sqle)
         {
            displaySQLErrors(sqle, "ConnectionManagerHSQL shutdownDatabase()", debug);
         }
      }
   }

   //==============================================================
   // Class method to output to the console and a alert dialog the
   // errors that occured during a connection to the database.
   //==============================================================

   public static void displaySQLErrors(SQLException e, String classCaller, boolean debug)
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

   public ConnectionProperties getConnectionProperties()
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
         return OTHER;
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
   // Class method to return the schemas pattern that will derive
   // no restriction on tables collect with a DatabaseMetaData
   // getTables().
   //==============================================================

   public static String getAllSchemasPattern()
   {
      return ALL_TABLE_SCHEMAS_PATTERN;
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

   public void setConnectionProperties(ConnectionProperties properties)
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

   public void setMaxColumnNameLength(int maxLength)
   {
      maxColumnNameLength = maxLength;
   }

   //==============================================================
   // Class method to set a memory type connection.
   //==============================================================

   private void setMemoryConnection(Connection connection)
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