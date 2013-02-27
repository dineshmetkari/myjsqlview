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
// Copyright (C) 2005-2013 Dana M. Proctor
// Version 3.2 02/27/2013
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
//        
//-----------------------------------------------------------------
//                 danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview.datasource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
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
 * @version 3.2 02/27/2012
 */

public class ConnectionManager
{
   // Class Instances.
   private static Connection memoryConnection;
   private static ConnectionProperties connectionProperties = new ConnectionProperties();
   private static final String configurationFileName = "myjsqlview.conf";
   
   private static String dbProductNameVersion;
   private static String catalog, schemaPattern, tableNamePattern;
   private static String[] tableTypes;
   private static ArrayList<String> schemas = new ArrayList <String>();
   private static ArrayList<String> tables = new ArrayList <String>();
   
   private static String catalogSeparator;
   private static String identifierQuoteString;
   private static int maxColumnNameLength;
   private static Clip errorSoundClip;
   private static boolean filter = true;
   
   public static final String MYSQL = "mysql";
   public static final String POSTGRESQL = "postgresql";
   public static final String HSQL = "hsql";
   public static final String HSQL2 = "hsql2";
   public static final String ORACLE = "oracle";
   public static final String SQLITE = "sqlite";
   public static final String MSACCESS = "odbc";
   public static final String DERBY = "derby";
   
   // private static final String TABLE_CAT = "TABLE_CAT";
   private static final String TABLE_SCHEM = "TABLE_SCHEM";
   private static final String TABLE_NAME = "TABLE_NAME";
   private static final String TABLE_TYPE = "TABLE_TYPE";
   // private static final String REMARKS = "REMARKS";
   // private static final String TYPE_CAT = "TYPE_CAT";
   // /private static final String TYPE_SCHEM = "TYPE_SCHEM";
   // private static final String TYPE_NAME = "TYPE_NAME";
   // private static final String SELF_REFERENCING_COL_NAME = "SELF_REFERENCING_COL_NAME";
   // private static final String REF_GENERATION = "REF_GENERATION";
   
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
      String connectionString;
      String db, subProtocol;
      
      // Setup.
      connectProperties = new Properties();
      
      connectionString = connectionProperties.getConnectionString();
      db = connectionProperties.getProperty(ConnectionProperties.DB);
      subProtocol = connectionProperties.getProperty(ConnectionProperties.SUBPROTOCOL);
      
      connectProperties.setProperty("user", connectionProperties.getProperty(ConnectionProperties.USER));
      connectProperties.setProperty("password", connectionProperties.getPassword());
      
      // Handle SSL
      if (subProtocol.equals(ConnectionManager.HSQL) || subProtocol.equals(ConnectionManager.MYSQL)
          || subProtocol.equals(ConnectionManager.POSTGRESQL))
      {
           connectProperties.setProperty("useSSL",
              connectionProperties.getProperty(ConnectionProperties.SSH));  
      }
      // System.out.println(connectionString);
            
      // Select and try to return an appropriate connection
      // type.
      
      try
      {
         if (MyJSQLView.getDebug())
            System.out.println(description + " Connection Created");
         
         // Create the appropriate connection as needed.
         
         // HSQL, SQLite, & Derby Memory Connections
         if ((memoryConnection != null)
              && (subProtocol.equals(SQLITE) && db.toLowerCase().equals(":memory:"))
                  || (subProtocol.indexOf(HSQL) != -1 && db.toLowerCase().indexOf("mem:") != -1)
                  || (subProtocol.equals(DERBY) && db.toLowerCase().indexOf("memory:") != -1))
            return memoryConnection;
         // All others
         else
         {
            return DriverManager.getConnection(connectionString, connectProperties);
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
            System.out.println(description + " Connection Closed");
         
         // Close connection as needed.
         if ((memoryConnection != null)
              && (subProtocol.equals(SQLITE) && db.toLowerCase().equals(":memory:"))
                  || (subProtocol.indexOf(HSQL) != -1 && db.toLowerCase().indexOf("mem:") != -1)
                  || (subProtocol.equals(ConnectionManager.DERBY)
                      && db.toLowerCase().indexOf("memory:") != -1))
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
               System.out.println(description + " Memory Connection Closed");
            
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
      String connectionString, driver, subProtocol;
      String databaseShutdownString;
      
      // Setup.
      connectionString = connectionProperties.getConnectionString();
      driver = connectionProperties.getProperty(ConnectionProperties.DRIVER);
      subProtocol = connectionProperties.getProperty(ConnectionProperties.SUBPROTOCOL);
      
      if (connectionString.indexOf(";") != -1)
         databaseShutdownString = connectionString.substring(0, connectionString.indexOf(";"));
      else
         databaseShutdownString = connectionString;
      
      try
      {
         // Try to shutdown Derby database properly.
         if (subProtocol.equals(DERBY))
         {
            // Drop Memory Databases
            if (databaseShutdownString.toLowerCase().indexOf("memory:") != -1)
            {
               if (MyJSQLView.getDebug())
                  System.out.println(description + " Dropping Derby Memory Database");
               
               DriverManager.getConnection(databaseShutdownString + ";drop=true");
               return;
            }
            
            // Shutdown Embedded Only
            if (driver.indexOf("EmbeddedDriver") != -1)
            {
               if (MyJSQLView.getDebug())
                  System.out.println(description + " Shutting Down Derby Embedded Database");
               
               DriverManager.getConnection("jdbc:derby:;shutdown=true");
            }
         }    
      }
      catch (SQLException e)
      {
         if (subProtocol.equals(DERBY) &&
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
   // Class method that provides the ability to load/reload database
   // parameters. The default is always loaded and a check is also
   // made to load an advanced users peferences from the file
   // myjsqlview.conf in default home directory.
   //==============================================================

   public static void loadDBParameters(Connection dbConnection) throws SQLException
   {
      // Method Instances
      DatabaseMetaData dbMetaData;
      ResultSet db_resultSet;
      
      String db, subProtocol;
      String myjsqlviewConfigFileString;
      String dbType, currentLine;
      File configurationFile;
      FileReader fileReader;
      BufferedReader bufferedReader;
      
      //======================================================
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
         dbType = "hsql";
         //db_resultSet = dbMetaData.getTables(null, "%", "%", tableTypes);
      }
      // Oracle
      else if (subProtocol.indexOf(ORACLE) != -1)
      {
         catalog = db;
         schemaPattern = "%";
         tableNamePattern = "%";
         dbType = "oracle";
         //db_resultSet = dbMetaData.getTables(db, "%", "%", tableTypes);
      }
      // MySQL & PostgreSQL
      else if (subProtocol.equals(MYSQL) || subProtocol.equals(POSTGRESQL))
      {
         catalog = db;
         schemaPattern = "";
         tableNamePattern = "%";
         if (subProtocol.equals(MYSQL))
            dbType = "mysql";
         else
            dbType = "postgresql";
         //db_resultSet = dbMetaData.getTables(db, "", "%", tableTypes);
      }
      // SQLite
      else if (subProtocol.equals(SQLITE))
      {
         catalog = db;
         schemaPattern = null;
         tableNamePattern = null;
         dbType = "sqlite";
         //db_resultSet = dbMetaData.getTables(db, "%", "%", tableTypes);
         
      }
      // Derby
      else if (subProtocol.equals(DERBY))
      {
         catalog = db;
         schemaPattern = null;
         tableNamePattern = "%";
         dbType = "derby";
         //db_resultSet = dbMetaData.getTables(null, null, null, tableTypes);
      }
      // Unknown
      else
      {
         catalog = null;
         schemaPattern = null;
         tableNamePattern = null;
         dbType = "other";
         //db_resultSet = dbMetaData.getTables(null, null, null, tableTypes);
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
         throw new SQLException("ConnectionManager loadDBParameters() " + e);
      }
      
      //==============================================================
      // Try overriding the default parameters with a myjsqlview.conf
      // file.
      
      // Create file name for retrieval.
      myjsqlviewConfigFileString = MyJSQLView_Utils.getMyJSQLViewDirectory()
                                   + MyJSQLView_Utils.getFileSeparator() + configurationFileName;
      
      try
      {
         // Check to see if file exists.
         configurationFile = new File(myjsqlviewConfigFileString);
         try
         { 
            if (!configurationFile.exists())
               return;
         }
         catch (SecurityException e)
         {
            //System.out.println("SecurityException " + e);
            return;
         }
         
         // Looks good so create reader and buffer to read
         // in the lines from the file.
         fileReader = new FileReader(myjsqlviewConfigFileString);
         bufferedReader = new BufferedReader(fileReader);
            
         while ((currentLine = bufferedReader.readLine()) != null)
         {
            currentLine = currentLine.trim();
            
            if (!currentLine.startsWith("#"))
            {
               // Filter Parameter
               if (currentLine.toLowerCase().indexOf("filter") != -1)
               {
                  if (currentLine.toLowerCase().indexOf("on") != -1)
                     filter = true;
                  else if (currentLine.toLowerCase().indexOf("off") != -1)
                     filter = false;
               }
               
               if (currentLine.toLowerCase().indexOf(dbType) != -1)
               {
                  //System.out.println(currentLine);
                  
                  // schemaPattern Parameter
                  if (currentLine.toLowerCase().indexOf("schemapattern") != -1)
                  {
                     if (currentLine.indexOf("=") != -1)
                     {
                        schemaPattern = currentLine.substring(currentLine.indexOf("=") + 1).trim();
                        if (schemaPattern.equals("null"))
                           schemaPattern = null;
                     }
                  }
                  
                  // tableNamePattern Parameter
                  if (currentLine.toLowerCase().indexOf("tablenamepattern") != -1)
                  {
                     if (currentLine.indexOf("=") != -1)
                     {
                        tableNamePattern = currentLine.substring(currentLine.indexOf("=") + 1).trim();
                        if (tableNamePattern.equals("null"))
                           tableNamePattern = null;
                     }
                  }
                  
                  // tableTypes Parameter
                  if (currentLine.toLowerCase().indexOf("types") != -1)
                  {
                     if (currentLine.indexOf("=") != -1)
                     {
                        currentLine = currentLine.substring(currentLine.indexOf("=") + 1).trim();
                        if (currentLine.equals("null"))
                           tableTypes = null;
                        else
                           tableTypes = currentLine.split(",");
                     }
                  }
               }
            }
         }
         bufferedReader.close();
         fileReader.close();
      }
      catch (IOException ioe) 
      {
         if (MyJSQLView.getDebug())
            System.out.println("File I/O Problem. " + ioe);
      }
   }
   
   //==============================================================
   // Class method that provides the ability to load/reload the
   // database schemas & tables.
   //==============================================================
   
   public static void loadDBTables(Connection dbConnection) throws SQLException
   {
      // Method Instances
      DatabaseMetaData dbMetaData;
      ResultSet db_resultSet;
      HashSet<String> oracleSystemSchemaHash;
      String db, subProtocol;
      String tableSchem, tableName, tableType;
      // String tableCat, remarks, typeCat, typeSchem, typeName, selfReferencingColName, refGeneration;
      String grantee, user;
      
      try
      {
         db = connectionProperties.getProperty(ConnectionProperties.DB);
         subProtocol = connectionProperties.getProperty(ConnectionProperties.SUBPROTOCOL);
         dbMetaData = dbConnection.getMetaData();
         
         // ============================
         // Obtain the databases tables.
         
         //********************************************************
         // THIS IS WHERE EACH DATABASE'S TABLES/VIEWS ARE OBTAINED.
         // EACH DATABASE WILL NEED TO BE TESTED HERE TO PROPERLY
         // OBTAIN THE PROPER INPUT FOR the dbMetaData.getTables()
         // ARGUMENTS TO GET THINGS TO WORK.
         // *******************************************************
         
         // System.out.println("'" + catalog + "' '" + schemaPattern + "' '" + tableNamePattern + "'");
         db_resultSet = dbMetaData.getTables(catalog, schemaPattern, tableNamePattern, tableTypes);
         
         // Setup some Oracle system exclusion schema.
         oracleSystemSchemaHash = new HashSet <String>();
         String[] oracleSystemSchemas = {"CTXSYS", "DBSNMP", "DSSYS", "MDSYS",
                                         "ODM", "ODM_MTR", "OLAPSYS", "ORDPLUGINS",
                                         "ORDSYS", "OUTLN", "PERFSTAT", "REPADMIN",
                                         "SYS", "SYSTEM", "TRACESVR", "TSMSYS",
                                         "WKPROXY", "WKSYS", "WMSYS", "XDB"};

         for (int j = 0; j < oracleSystemSchemas.length; j++)
            oracleSystemSchemaHash.add(oracleSystemSchemas[j]);

         // This is where you can modifiy MyJSQLView to obtain all the
         // available tables you want. Uncomment the System.out below
         // and run to see what is available.
         
         // Clear the tables vector and load it with the databases
         // tables.
         schemas.clear();
         tables.clear();
         
         // ResultSetMetaData rsmd = db_resultSet.getMetaData();
         // for (int i = 1; i <= rsmd.getColumnCount(); i++)
         //    System.out.println(rsmd.getColumnName(i));

         
         while (db_resultSet.next())
         {
            // tableCat = db_resultSet.getString(TABLE_CAT);
            tableSchem = db_resultSet.getString(TABLE_SCHEM);
            tableName = db_resultSet.getString(TABLE_NAME);
            tableType = db_resultSet.getString(TABLE_TYPE);
            // remarks = db_resultSet.getString(REMARKS);
            // typeCat = db_resultSet.getString(TYPE_CAT);
            // typeSchem = db_resultSet.getString(TYPE_SCHEM);
            // typeName = db_resultSet.getString(TYPE_NAME);
            // selfReferencingColName = db_resultSet.getString(SELF_REFERENCING_COL_NAME);
            // refGeneration = db_resultSet.getString(REF_GENERATION);
            
            // All information, could be to much.
            // System.out.println("Table CAT: " + tableCat
                               // + " Table Schem: " + tableSchem
                               // + " Table Name: " + tableName
                               // + " Table Type: " + tableType
                               // + " Remarks: " + remarks
                               // + " Type Cat: " + typeCat
                               // + " Type Schem: " + typeSchem
                               // + " Type Name: " + typeName
                               // + " Self Referencing Col Name: " + selfReferencingColName
                               // + " refGeneration: " + refGeneration);

            // Filter, only TABLEs & VIEWs allowed in MyJSQLView
            // application.
            
            if (tableType != null && !(tableType.indexOf("INDEX") != -1)
                && !(tableType.indexOf("SEQUENCE") != -1) && !(tableType.indexOf("SYNONYM") != -1)
                && (tableType.equals("TABLE") || tableType.equals("VIEW") || !filter))
            {
               // Filter some more for Oracle.
               if ((subProtocol.indexOf(ORACLE) != -1 && filter)
                     && (oracleSystemSchemaHash.contains(tableSchem) || tableSchem.indexOf("FLOWS") != -1 ||
                         tableName.indexOf("BIN$") != -1))
                  continue;

               // Abreviated and filtered information.
               // System.out.println(tableType + " " + tableSchem + "." + tableName);

               if (tableSchem != null && !tableSchem.equals(""))
               {
                  tables.add(tableSchem + getCatalogSeparator() + tableName);
               }
               else
                  tables.add(tableName);
            }
         }

         // ************************************************************
         // PostgreSQL databases may have schemas that limit access to
         // tables by users. So make a check and remove tables that are
         // not accessable by the user.
         
         if (subProtocol.equals(POSTGRESQL))
         {
            db_resultSet = dbMetaData.getTablePrivileges(db, "", "%");
            
            while (db_resultSet.next())
            {
               tableName = db_resultSet.getString(TABLE_NAME);
               
               if (tables.contains(tableName))
               {
                  grantee = db_resultSet.getString("GRANTEE");
                  user = connectionProperties.getProperty(ConnectionProperties.USER);
                  
                  if (MyJSQLView.getDebug())
                     System.out.println("Unauthorized Tabel Access: " + tableName + " : "
                        + grantee + " : " + user);

                  if (tables.contains(tableName) && !grantee.equals(user))
                     tables.remove(tableName);
               }
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
                  // System.out.println(tableName.substring(0, tableName.indexOf(".")));
               }
            }
         }
         
         db_resultSet.close();
      }
      catch (SQLException e)
      {
         throw new SQLException("ConnectionManager loadDBTables() " + e);
      }
   }
   
   //==============================================================
   // Class method to get the current database catalog separator.
   //==============================================================

   public static String getCatalogSeparator()
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
      
      if (subProtocol.equals(MYSQL))
         return MYSQL;
      else if (subProtocol.equals(POSTGRESQL))
         return POSTGRESQL;
      else if (subProtocol.indexOf(HSQL) != -1)
      {
         if (dbProductNameVersion.indexOf(" 2.") != -1)
            return HSQL2;
         else
            return HSQL;
      }
      else if (subProtocol.indexOf(ORACLE) != -1)
         return ORACLE;
      else if (subProtocol.equals(SQLITE))
         return SQLITE;
      else if (subProtocol.equals(MSACCESS))
         return MSACCESS;
      else if (subProtocol.equals(DERBY))
         return DERBY;
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
   // Class method to return the max column name length that is
   // used by the database.
   //==============================================================

   public static int getMaxColumnNameLength()
   {
      return maxColumnNameLength;
   }

   //==============================================================
   // Class method to return a copy of the available database
   // schemas names.
   //==============================================================

   public static ArrayList<String> getSchemas()
   {
      ArrayList<String> schemasList = new ArrayList <String>();
      Iterator<String> schemasIterator = schemas.iterator();
      
      while (schemasIterator.hasNext())
         schemasList.add(schemasIterator.next());
      
      return schemasList;
   }
   
   //==============================================================
   // Class method to return a copy of the default database table
   // names.
   //==============================================================

   public static ArrayList<String> getTableNames()
   {
      ArrayList<String> tablesList = new ArrayList <String>();
      Iterator<String> tablesIterator = tables.iterator();
      
      while (tablesIterator.hasNext())
         tablesList.add(tablesIterator.next());
      
      return tablesList;
   }
   
   //==============================================================
   // Class method to set the current database catalog separator.
   //==============================================================

   public static void setCatalogSeparator(String separator)
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
   // Class method to set the identifier quote string that the
   // application will use.
   //==============================================================

   public static void setIdentifierQuoteString(String identifier)
   {
      identifierQuoteString = identifier;
   }
   
   //==============================================================
   // Class method to set the max column name length that the
   // application will use.
   //==============================================================

   public static void setMaxColumnNameLength(int maxLength)
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
   
   public static void setSchemaPattern(String pattern)
   {
      schemaPattern = pattern;
   }
}