//=================================================================
//                     ConnectionManager
//=================================================================
//    This class provides a central class to manage all connections
// that are used by the MyJSQLView application to access the various
// databases support.
//
//                 << ConnectionManager.java >>
//
//=================================================================
// Copyright (C) 2005-2014 Dana M. Proctor
// Version 4.6 01/20/2014
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
// Version 1.0 Initial ConnectionManager Class.
//         1.1 Made Class Public Along With Class Method getConnectionProperties().
//         1.2 Added static final Class Instances TABLE_CAT, TABLE_TYPE, TABLE_SCHEM,
//             TABLE_NAME, REMARKS. Added Corresponding Class Methods Strings to
//             loadDBTables(). Change of Class Identification for Class Methods
//             get/closeConnection() to ConnectionManager.
//         1.3 Class Method loadDBTables(), PostgreSQL getTablePrivileges() Space
//             for schemaPattern Because of a Bug in pgJDBC 9.0-801
//         1.4 Class Methods loadDBParameters() & loadDBTables() Created Additional
//             Information in catch() by Creating new SQLException().
//         1.5 Modified the Way System.outs Handle Content by Not Using ResultSet
//             But Rather by Instances Variables That Were Already Available. Class
//             Methods loadDBParameters() & loadDBTables().
//         1.6 Added Static Class Instance MSACCESS & Method getDataSourceType().
//         1.7 Change in Class Method getConnection() to Pass user & passwordString
//             to Creation of DriverManager.getConnection().
//         1.8 Added Static Class Instance HSQL2 & Returning As Such As Required
//             in Class Method getDataSourceType().
//         1.9 Added Static Class Instance catalogSeparator and Getter/Setter Methods.
//         2.0 Added Additional Static Class Instances, Commented, That Can be Used
//             to Collect All Information if Desired in Method loadDBTables() for 
//             dbMetaData.getTables().
//         2.1 Changed in Method Name setMemoryConnectoin() to setMemoryConnection().
//         2.2 Change in Method getConnection() to Handle HSQL File & Resource
//             Connnections Using Separate Arguments for User & Password.
//         2.3 Changed Class Instances schemas & tables from Vector to ArrayList.
//             Also the Same for schemas & tables in Class Methods getSchemas()
//             & getTableNames().
//         2.4 Correction in Methods getSchemas() & getTableNames() to Returned a
//             Proper Copy of tables & schemas.
//         2.5 Changed loadDBTables() tables Addition by Concating tableSchema &
//             tableName Together With catalogSeperator() Instead of String With
//             Period.
//         2.6 Changed Method getCatalogSeparator() to public.
//         2.7 Added Class Instance maxColumnNameLength & Corresponding getter/
//             setter Methods.
//         2.8 Changed Package Name to com.dandymadeproductions.myjsqlview.datasource.
//             Changed Constructor & All Methods to Public.
//         2.9 Added static Class Instance DERBY. Changes in Method getConnection()
//             to Generalize Connections to Use getConnection(URL, Properties). Allows
//             the Ability to Address Adding Connection Properties to the connectionString.
//             Method loadDBParameters() to Include Derby Parameters. Class Method
//             getDataSourceType() Added Derby.
//         3.0 Method loadDBTables() Returned schemaPattern of Empty String for PostreSQL
//             Database getTablePrivileges() Since pgJDBC Bug for ACL Fixed.
//         3.1 Methods get/closeConnection() Check for Derby Memory Connections to
//             Insure memoryConnection is Returned & Not Closed.
//         3.2 Minor Format & Comment Changes in getConnection(). Added Class Methods
//             shutdown(), closeMemoryConnection(), & shutdownDatabase().
//         3.3 Method shutdownDatabase() Added HSQL File & Memory Database Shutdown
//             Code.
//         3.4 Updated Example dbMetaData.getTables() Parameter Arguments for SQLite &
//             Derby in loadDBParameters.
//         3.5 Added Class Method getAllSchemasPattern().
//         3.6 Commented schemasName Output in loadDBTables().
//         3.7 Class Method shutdownDatabase() Insertion of finally to Handle Connection
//             That was Created.
//         3.8 Class Method loadDBTables() Change Temporarily to Use a Space for PostgreSQL
//             getTablePrivileges(). See Note in Code.
//         3.9 Additional of static final Instances H2 & OTHERDB. Method getConnection()
//             Inclusion of H2 mem: for Memory Connections. Exclusion of H2 mem: in Method
//             closeConnection(). Addition of Parameters for H2 in Method loadDBParameters().
//             Return as Appropriate of H2 for Method getDataSourceType().
//         4.0 Class Method getConnection() Insured Use of SSL Property for HSQL & HSQL2.
//         4.1 Added Class Instance databaseProperties & Transfered All Aspects of Collecting
//             Database Parameters to New Class DatabaseProperties. Removed Associated
//             Setter Methods for Database Attributes & Added Method setDatabasePropeties().
//             Referenced Getter Methods for Parameters to databaseProperties.
//         4.2 Class Method createConnectionURLString() Changed Argument Name to properties
//             to Advoid Confusion With Class Instance connectionProperties.
//         4.3 Debug System.output Description Properly Identified Class Name Throughout.
//         4.4 Removed Debug Output in Method createConnectURLString().
//         4.5 Commented Out Derby Shutdown Code in Method shutdownDatabase(). See Note.
//         4.6 Added static final Class Instance MSSQL. Class Method createConnectionURLString()
//             Addition of Code to Create MS SQL Server Connection String.
//        
//-----------------------------------------------------------------
//                 danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview.datasource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

import javax.sound.sampled.Clip;
import javax.swing.JOptionPane;

import com.dandymadeproductions.myjsqlview.MyJSQLView;
import com.dandymadeproductions.myjsqlview.utilities.MyJSQLView_Utils;

/**
 *    The ConnectionManager class provides a central class to manage all
 * connections that are used by the MyJSQLView application to access the
 * various databases support.   
 * 
 * @author Dana M. Proctor
 * @version 4.6 01/20/2014
 */

public class ConnectionManager
{
   // Class Instances.
   private static Connection memoryConnection;
   private static ConnectionProperties connectionProperties = new ConnectionProperties();
   private static DatabaseProperties databaseProperties = new DatabaseProperties(connectionProperties);
   
   private static Clip errorSoundClip;
   
   public static final String MYSQL = "mysql";
   public static final String POSTGRESQL = "postgresql";
   public static final String HSQL = "hsql";
   public static final String HSQL2 = "hsql2";
   public static final String ORACLE = "oracle";
   public static final String SQLITE = "sqlite";
   public static final String MSACCESS = "odbc";
   public static final String MSSQL = "sqlserver";
   public static final String DERBY = "derby";
   public static final String H2 = "h2";
   public static final String OTHERDB = "other";
   
   //==============================================================
   // ConnectionManager Constructor
   //==============================================================

   public ConnectionManager()
   {
      // Constructor Instances
      String fileSeparator;
      
      // Obtaining & creating a sound clip to be played
      // during errors..
      
      fileSeparator = MyJSQLView_Utils.getFileSeparator();
      errorSoundClip = MyJSQLView_Utils.getAudioClip("sounds" + fileSeparator + "huh.wav");
   }
   
   //==============================================================
   // Class method that provides the ability to make a valid
   // connection to the database based on the initial login, host,
   // db, user and password. A test should be made for any class
   // accessing this method for a null return, no connection made.
   //==============================================================

   public static Connection getConnection(String description)
   {
      // Method Instances.
      Properties connectProperties;
      String connectionURLString;
      String db, subProtocol;
      
      // Setup.
      connectProperties = new Properties();
      
      connectionURLString = connectionProperties.getConnectionURLString();
      db = connectionProperties.getProperty(ConnectionProperties.DB);
      subProtocol = connectionProperties.getProperty(ConnectionProperties.SUBPROTOCOL);
      
      connectProperties.setProperty("user", connectionProperties.getProperty(ConnectionProperties.USER));
      connectProperties.setProperty("password", connectionProperties.getPassword());
      
      // Handle SSL
      if (subProtocol.indexOf(ConnectionManager.HSQL) != -1 || subProtocol.equals(ConnectionManager.MYSQL)
          || subProtocol.equals(ConnectionManager.POSTGRESQL))
      {
           connectProperties.setProperty("useSSL",
              connectionProperties.getProperty(ConnectionProperties.SSH));  
      }
            
      // Select and try to return an appropriate connection
      // type.
      
      try
      {
         if (MyJSQLView.getDebug())
            System.out.println(description + " (CM) Connection Created");
         
         // Create the appropriate connection as needed.
         
         // HSQL, SQLite, Derby, & H2 Memory Connections
         if ((memoryConnection != null)
              && (subProtocol.equals(SQLITE) && db.toLowerCase().equals(":memory:"))
                  || (subProtocol.indexOf(HSQL) != -1 && db.toLowerCase().indexOf("mem:") != -1)
                  || (subProtocol.equals(DERBY) && db.toLowerCase().indexOf("memory:") != -1)
                  || (subProtocol.equals(H2) && db.toLowerCase().indexOf("mem:") != -1))
            return memoryConnection;
         // All others
         else
         {
            return DriverManager.getConnection(connectionURLString, connectProperties);
         }
      }
      catch (SQLException e)
      {
         displaySQLErrors(e, "ConnectionManager getConnection()");
         return null;
      }
   }
   
   //==============================================================
   // Class method that provides the ability to close the connection
   // that was created to the database based on the initial login,
   // host, db, user and password.
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
         if (MyJSQLView.getDebug())
            System.out.println(description + " (CM) Connection Closed");
         
         // Close connection as needed.
         if ((memoryConnection != null)
              && (subProtocol.equals(SQLITE) && db.toLowerCase().equals(":memory:"))
                  || (subProtocol.indexOf(HSQL) != -1 && db.toLowerCase().indexOf("mem:") != -1)
                  || (subProtocol.equals(DERBY) && db.toLowerCase().indexOf("memory:") != -1)
                  || (subProtocol.equals(H2) && db.toLowerCase().indexOf("mem:") != -1))
            return;
         else
            dbConnection.close();
      }
      catch (SQLException e)
      {
         displaySQLErrors(e, "ConnectionManager closeConnection()");
      }
   }
   
   //==============================================================
   // Class method that provides the ability to attempt to shutdown
   // database activity appropriately.
   //==============================================================

   public static void shutdown(String description)
   {
      closeMemoryConnection(description);
      shutdownDatabase(description);
   }
   
   //==============================================================
   // Class method that provides the ability to close the memory
   // connection upon termination of the application
   //==============================================================

   private static void closeMemoryConnection(String description)
   {  
      try
      {
         // Close connection as needed.
         if (memoryConnection != null)
         {
            if (MyJSQLView.getDebug())
               System.out.println(description + " (CM) Memory Connection Closed");
            
            memoryConnection.close();  
         }
      }
      catch (SQLException e)
      {
         displaySQLErrors(e, "ConnectionManager closeMemoryConnection()");
      }
   }
   
   //==============================================================
   // Class method that provides the ability to shutdown Databases
   // that advocate such, file & embedded types.
   //==============================================================

   private static void shutdownDatabase(String description)
   {
      // Method Instances.
      Connection dbConnection;
      String connectionURLString;
      // String driver;
      String subProtocol;
      String databaseShutdownString;
      
      // Setup.
      connectionURLString = connectionProperties.getConnectionURLString();
      // driver = connectionProperties.getProperty(ConnectionProperties.DRIVER);
      subProtocol = connectionProperties.getProperty(ConnectionProperties.SUBPROTOCOL);
      
      if (connectionURLString.indexOf(";") != -1)
         databaseShutdownString = connectionURLString.substring(0, connectionURLString.indexOf(";"));
      else
         databaseShutdownString = connectionURLString;
      
      dbConnection = null;
      
      try
      {
         // Try to shutdown Derby & HSQL database properly.
         
         /*
         Derby:
         
         Unable to get a drop or shutdown to function without the
         following error message. Even Derby examples give same error.
         
         SQLException: invalid database address: jdbc:derby:~~
         SQLState: null
         VendorError: 0
         
         if (subProtocol.equals(DERBY))
         {
            /*
            // Drop Memory Databases
            if (databaseShutdownString.toLowerCase().indexOf("memory:") != -1)
            {
               if (MyJSQLView.getDebug())
                  System.out.println(description + " (CM) Dropping Derby Memory Database");
               
               dbConnection = DriverManager.getConnection(databaseShutdownString + ";drop=true");
               dbConnection.close();
            }
            
            // Shutdown Embedded Only
            if (driver.indexOf("EmbeddedDriver") != -1)
            {
               if (MyJSQLView.getDebug())
                  System.out.println(description + " (CM) Shutting Down Derby Embedded Database");
               
               dbConnection = DriverManager.getConnection("jdbc:derby:;shutdown=true");
               dbConnection.close();
            }
            return;
         }
         */
         
         if (subProtocol.indexOf(HSQL) != -1)
         {
            if (databaseShutdownString.toLowerCase().indexOf("file:") != -1
                || databaseShutdownString.toLowerCase().indexOf("mem:") != -1)
            {
               if (MyJSQLView.getDebug())
                  System.out.println(description + " (CM) Shutting Down HSQL File/Memory Database");
               
               dbConnection = DriverManager.getConnection(databaseShutdownString + ";shutdown=true");
               dbConnection.close();
            }
            return;
         }
      }
      catch (SQLException e)
      {
         if (subProtocol.equals(DERBY) && e.getSQLState() != null &&
             (e.getSQLState().equals("08006") || e.getSQLState().equals("XJ015")))
         {
            if (MyJSQLView.getDebug())
            {
               System.out.println("SQLException: " + e.getMessage());
               System.out.println("SQLState: " + e.getSQLState());
            }
         }
         else
            displaySQLErrors(e, "ConnectionManager shutdownDatabase()");
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
            displaySQLErrors(sqle, "ConnectionManager shutdownDatabase()");
         }
      }
   }
   
   //==============================================================
   // Class method to output to the console and a alert dialog the
   // errors that occured during a connection to the database.
   //==============================================================

   public static void displaySQLErrors(SQLException e, String classCaller)
   {
      String sqlExceptionString;
      
      // Generated a sound warning.
      if (errorSoundClip != null)
      {
         errorSoundClip.setFramePosition(0);
         errorSoundClip.start();
      }

      // Standard Console Output.
      if (MyJSQLView.getDebug())
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
   // Class method that provides the ability to load/reload the
   // database schemas & tables.
   //==============================================================
   
   public static void loadDBTables(Connection dbConnection) throws SQLException
   {
      databaseProperties.loadDBTables(dbConnection);
   }
   
   //==============================================================
   // Class method to create a connection URL string based on the
   // given connection properties.
   //==============================================================

   public static String createConnectionURLString(ConnectionProperties properties)
   {
      // Method Instances
      String connectionURLString;
      String driver, protocol, subProtocol, host, port, db;
      
      // Collect Instances
      driver = properties.getProperty(ConnectionProperties.DRIVER);
      protocol = properties.getProperty(ConnectionProperties.PROTOCOL);
      subProtocol = properties.getProperty(ConnectionProperties.SUBPROTOCOL);
      host = properties.getProperty(ConnectionProperties.HOST);
      port = properties.getProperty(ConnectionProperties.PORT);
      db = properties.getProperty(ConnectionProperties.DB);
      
      // Take into consideration various database requirements.
      connectionURLString = protocol + ":";

      // Oracle
      if (subProtocol.indexOf(ConnectionManager.ORACLE) != -1)
      {
         if (subProtocol.indexOf("thin") != -1)
            connectionURLString += subProtocol + ":@//" + host + ":" + port + "/" + db;
         else
            connectionURLString += subProtocol + ":@" + db;
      }
      // SQLite
      else if (subProtocol.equals(ConnectionManager.SQLITE))
      {
         connectionURLString += subProtocol + ":" + db.replace("\\", "/");
      }
      // HSQL Memory, File, & Resource
      else if (subProtocol.indexOf(ConnectionManager.HSQL) != -1 &&
                (db.indexOf("mem:") != -1) || db.indexOf("file:") != -1 || db.indexOf("res:") != -1)
      {
         connectionURLString += "hsqldb:" + db;
      }
      // Derby Memory
      else if (subProtocol.indexOf(ConnectionManager.DERBY) != -1 &&
               db.indexOf("memory:") != -1)
      {
         if (db.toLowerCase().indexOf(";create=true") == -1)
            db += ";create=true";
         
         if (driver.indexOf("EmbeddedDriver") != -1)
            connectionURLString += subProtocol + ":" + db;
         else
            connectionURLString += subProtocol + "://" + host + ":" + port + "/" + db;
      }
      // MS Access
      else if (subProtocol.equals(ConnectionManager.MSACCESS))
      {
         connectionURLString += subProtocol + ":" + db;
      }
      // MSSQL
      else if (subProtocol.equals(ConnectionManager.MSSQL))
      {
         if (db.isEmpty())
            connectionURLString += subProtocol + "://" + host + ":" + port;
         else
            connectionURLString += subProtocol + "://" + host + ":" + port + ";databaseName=" + db;
      }
      // H2
      else if (subProtocol.equals(ConnectionManager.H2))
      {
         if (db.indexOf("tcp:") != -1)
            connectionURLString += subProtocol + ":tcp://" + host + ":" + port + "/"
                             + db.substring(db.indexOf("tcp:") + 4);
         else if (db.indexOf("ssl:") != -1)
            connectionURLString += subProtocol + ":ssl://" + host + ":" + port + "/"
                             + db.substring(db.indexOf("ssl:") + 4);
         else
            connectionURLString += subProtocol + ":" + db;
            
      }
      // MySQL, PostgreSQL, HSQL, & Derby
      else
      {
         // The % character is interpreted as the start of a special escaped sequence,
         // two digit hexadeciaml value. So replace passwordString characters with that
         // character with that characters hexadecimal value as sequence, %37. Java
         // API URLDecoder.
         
         if (subProtocol.indexOf(ConnectionManager.DERBY) != -1 &&
               driver.indexOf("EmbeddedDriver") != -1)
            connectionURLString += subProtocol + ":" + db;
         else
            connectionURLString += subProtocol + "://" + host + ":" + port + "/" + db;
      }
      return connectionURLString;
   }
   
   //==============================================================
   // Class method to get the current database catalog separator.
   //==============================================================

   public static String getCatalogSeparator()
   {
      return databaseProperties.getCatalogSeparator();
   }
   
   //==============================================================
   // Class method to get the current connection properties.
   //==============================================================

   public static ConnectionProperties getConnectionProperties()
   {
      return connectionProperties;
   }
   
   //==============================================================
   // Class method to get the current data source type given by a
   // ConnectionProperties & DatabaseProperties Classes.
   //==============================================================
   
   public static String getDataSourceType()
   {
      return databaseProperties.getDataSourceType();
   }
   
   //==============================================================
   // Class method to return the current database product name &
   // version.
   //==============================================================

   public static String getDBProductName_And_Version()
   {
      return databaseProperties.getDBProductName_And_Version();
   }
   
   //==============================================================
   // Class method to return the current identifier quote string
   // that is used by the database.
   //==============================================================

   public static String getIdentifierQuoteString()
   {
      return databaseProperties.getIdentifierQuoteString();
   }
   
   //==============================================================
   // Class method to return the max column name length that is
   // used by the database.
   //==============================================================

   public static int getMaxColumnNameLength()
   {
      return databaseProperties.getMaxColumnNameLength();
   }

   //==============================================================
   // Class method to return a copy of the available database
   // schemas names.
   //==============================================================

   public static ArrayList<String> getSchemas()
   {
      return databaseProperties.getSchemas();
   }
   
   //==============================================================
   // Class method to return the schemas pattern that will derive
   // no restriction on tables collect with a DatabaseMetaData
   // getTables().
   //==============================================================

   public static String getAllSchemasPattern()
   {
      return databaseProperties.getAllSchemasPattern();
   }
   
   //==============================================================
   // Class method to return a copy of the default database table
   // names.
   //==============================================================

   public static ArrayList<String> getTableNames()
   {
      return databaseProperties.getTableNames();
   }
   
   //==============================================================
   // Class method to set the current database product name &
   // version.
   //==============================================================

   public static void setConnectionProperties(ConnectionProperties properties)
   {
      connectionProperties = properties;
   }
   
   public static void setDatabaseProperties(DatabaseProperties properties)
   {
      databaseProperties = properties;
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
      databaseProperties.setSchemaPattern(pattern);
   }
}