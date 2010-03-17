//=================================================================
//                  TableDefinitionGenerator
//=================================================================
//   This class provides a common focus for creating the various
// database table definitions for table structures that output
// via the SQL data export feature in MyJSQLView
//
//              << TableDefinitionGenerator.java >>
//
//=================================================================
// Copyright (C) 2007-2010 Dana M. Proctor
// Version 3.6 02/18/2010
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
// Version 1.0 Original TableDefinitionGenerator Class.
//         1.1 Began Work On HSQL Table Structure Creation in Class
//             Method createHSQLTableDefinition(). Correction in
//             Class Method createPostgreSQLTableDefinition for
//             Time & Timestamp Types, Missing Closing Parenthesis
//             for Precision Statement.
//         1.2 Basic Completion of Class Method createHSQLTableDefinition().
//         1.3 Comment Changes and Removal of Constraint Definition
//             for Foreign Keys in Class Method createHSQLTableDefinition().
//             Still Needs On Delete/On Update and Identity for HSQL
//             Tables.
//         1.4 Completely Redo of Code in Class Method createHSQLTableDefinition()
//             to Use Wholely the JDBC and NOT Information_Schema.
//         1.5 Removed Constraint Configuration for Foreign Key in Class
//             Method createPostgreSQLTableDefinition(). Generalized Format
//             for Cross Database Functionality.
//         1.6 Added Support for Arrays for PostgreSQL Database in Class
//             Method createPostgreSQLTableDefinition().
//         1.7 Modified Class Method createPostgreSQLTableDefinition() in
//             the Way it Handles the Array Fields. Specifically How the
//             Data Type Statement is Created.
//         1.8 Added Class Instance dbIdentiferQuoteString. Fix So That
//			      Exports May Properly Have the Selected Identifer String Used
//             From the Preferences Panel.
//         1.9 Removed a Comma in the Foreign Key Composition in Class Method
//             createSQLTableDefinition().
//         2.0 Class Instance tableName Changed to schemaTableName. Sorted Out
//             The Use of Schema and Table Name in Class Method
//             createPostgreSQLTableDefinition. This Method Needs to Be Changed
//             Over to Use Only the JDBC if Possible, Instead of the
//             Information_Schema.
//         2.1 Corrections in Class Method createPostgreSQLTableDefinition
//             to Properly Select Correct Data Based on Schema and Table Name.
//             Added in Same Class Method referenceSchemaName.
//         2.2 Added the selection of Oracle Database in Class Method
//             getTableDefinition(). Added Class Method createOracleTable-
//             Definition() With a Basic Outline from the HSQL Definition
//             Method.
//         2.3 Initial Build Functionality of Class Method createOracleTableDefinition().
//             Tested With Basic Table Types, Working. Lacking Sequence(s)
//             Inclusion.
//         2.4 Updated the createOracleTableDefinition() Class Method to Handle
//             Sequences. Changed Method Instance tableDefinition to a StringBuffer.
//             Reviewed, Cleaned, & Basic Testing of Oracle Table DDL Exports.
//         2.5 Reviewed CASCADE RULE for Class Method createOracleTableDefinition.
//             Left to Default to CASCADE. Same Method dbMetaData.getIndexInfo()
//             Had to Add identifierQuoteString.
//         2.6 Changed Method Instance tableDefinition to a StringBuffer in Class
//             Method createHSQLTableDefinition().
//         2.7 Changed Method Instance tableDefinition to a StringBuffer in Class
//             Method createPostgreSQLTableDefinition().
//         2.8 Replaced replace() With replaceAll() for schemaName and tableName
//             in Class Method createOracleTableDefinition().
//         2.9 MyJSQLView Project Common Source Code Formatting.
//         3.0 Constructor getDataExportProperites Changed Over to the
//             MyJSQLView_Frame Class.
//         3.1 Changed MyJSQLView_Frame.getDatabaseExportProperties() Method Moved
//             Over to the DBTablesPanel.
//         3.2 Revised to Properly Detect VIEW Type Tables and Provide the Basic
//             SQL Statement Needed to Create the DDL. All Database Methods Modified.
//             Also Corrected Substitution of Identifier Quote From Preferences.
//         3.3 Class Method createOracleTableDefinition() Changed DROP TABLE IF
//             EXISTS Statement to Just DROP TABLE.
//         3.4 Header Format Changes/Update.
//         3.5 Class Method createHSQLTableDefinition() Instance uniqueKeys Changed
//             From String to StringBuffer. Class Method createPostgreSQLTableDefinition()
//             Changed Instances primaryKeys, uniqueKeys, foreignKeys to StringBuffers.
//         3.6 Changed Package to Reflect Dandy Made Productions Code.
//             
//-----------------------------------------------------------------
//                    danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview;

import java.sql.*;
import java.util.HashMap;

/**
 *    The TableDefinitionGenerator class provides a common focus
 * for creating the various database table definitions for table
 * structures that output via the SQL data export feature in MyJSQLView.
 * 
 * @author Dana Proctor
 * @version 3.6 02/18/2010
 */

class TableDefinitionGenerator
{
   // Class Instances.
   private String schemaTableName, schemaName, tableName;
   private Connection dbConnection;
   private String dbIdentifierQuoteString;
   private String identifierQuoteString;
   private DataExportProperties sqlDataExportOptions;

   //==============================================================
   // TableDefinitionGenerator Constructor.
   //==============================================================

   TableDefinitionGenerator(Connection dbConnection, String table)
   {
      // Common class Instances.
      this.dbConnection = dbConnection;
      this.schemaTableName = table;
      //System.out.println(schemaTableName);

      // Setting up required instances.
      dbIdentifierQuoteString = MyJSQLView_Access.getIdentifierQuoteString();
      sqlDataExportOptions = DBTablesPanel.getDataExportProperties();
      identifierQuoteString = sqlDataExportOptions.getIdentifierQuoteString();
      
      if (schemaTableName.indexOf(".") != -1)
      {
         schemaName = schemaTableName.substring(0, schemaTableName.indexOf("."));
         schemaName = schemaName.replaceAll(dbIdentifierQuoteString, "");
         tableName = schemaTableName.substring(schemaTableName.indexOf(".") + 1);
         tableName = tableName.replaceAll(dbIdentifierQuoteString, "");
      }
      else
      {
         schemaName = "";
         tableName = schemaTableName.replaceAll(dbIdentifierQuoteString, "");
      }
   }

   //==============================================================
   // Class method for creating a given MySQL TABLE definition.
   //==============================================================

   private String createMySQLTableDefinition()
   {
      // Class Method Instances.
      StringBuffer tableDefinition = new StringBuffer("");

      String sqlStatementString;
      Statement sqlStatement;
      ResultSet resultSet;

      // Beginning the creation of the string description
      // of the table Structure.
      try
      {
         // Setup a connection statement.
         sqlStatement = dbConnection.createStatement();

         // MySQL does all the work here with the handy
         // SHOW SQL Command.

         sqlStatementString = "SHOW CREATE TABLE " + schemaTableName;
         resultSet = sqlStatement.executeQuery(sqlStatementString);
         
         // Drop Table Statements As Needed.
         if (DBTablesPanel.getDataExportProperties().getTableStructure())
         {
            tableDefinition.append("DROP "
                                   + resultSet.getMetaData().getColumnName(1).toUpperCase()
                                   + " IF EXISTS " + schemaTableName + ";\n");
         }
         // Create Table column
         resultSet.next();
         tableDefinition.append(resultSet.getString(2) + ";\n");

         resultSet.close();
         sqlStatement.close();
         return (tableDefinition.toString().replaceAll(dbIdentifierQuoteString, identifierQuoteString));
      }
      catch (SQLException e)
      {
         MyJSQLView_Access.displaySQLErrors(e, "TableDefinitionGenerator createMySQLTableDefinition()");
         return (tableDefinition.toString().replaceAll(dbIdentifierQuoteString, identifierQuoteString));
      }
   }

   //==============================================================
   // Class method for creating a given PostgreSQL TABLE definition.
   //==============================================================

   private String createPostgreSQLTableDefinition()
   {
      // Class Method Instances.
      StringBuffer tableDefinition = new StringBuffer();
      String tableType;
      String columnName, columnType, constraint;
      StringBuffer primaryKeys, uniqueKeys, foreignKeys;
      String referenceSchemaName, referenceTableName, referenceColumnName;
      String onDeleteRule;

      String sqlStatementString;
      Statement sqlStatement, sqlStatement2;
      ResultSet resultSet, resultSet2;

      // Beginning the creation of the string description
      // of the table Structure.
      try
      {
         // Setup a connection statement.
         sqlStatement = dbConnection.createStatement();
         sqlStatement2 = dbConnection.createStatement();

         // No easy method here for PostgreSQL have to create each
         // field name, type, defaults, etc. Also Keys. As of 2.81
         // Keys are used instead of sequences, this may need to
         // change.
         
         // Collect table type for the table.
         
         sqlStatementString = "SELECT table_type FROM information_schema.tables WHERE "
                               + "table_catalog='" + MyJSQLView_Access.getDBName() + "' AND "
                               + "table_schema='" + schemaName + "' AND "
                               + "table_name='" + tableName + "'";
         //System.out.println(sqlStatementString);
         
         resultSet = sqlStatement.executeQuery(sqlStatementString);
         
         if (resultSet.next())
         {
            if (resultSet.getString(1).equals("BASE TABLE"))
               tableType = "TABLE";
            else
               tableType = resultSet.getString(1);
         }
         else
            tableType = "TABLE";
         
         // Drop Table Statements As Needed.
         if (DBTablesPanel.getDataExportProperties().getTableStructure())
         {
            tableDefinition.append("DROP " + tableType + " IF EXISTS " + schemaTableName + ";\n");
         }
         
         // Table Creation Statement.
         if (tableType.equals("VIEW"))
         {
            sqlStatementString = "SELECT view_definition FROM information_schema.views WHERE "
                                 + "table_catalog='" + MyJSQLView_Access.getDBName() + "' AND "
                                 + "table_schema='" + schemaName + "' AND "
                                 + "table_name='" + tableName + "'";
            //System.out.println(sqlStatementString);

            resultSet = sqlStatement.executeQuery(sqlStatementString);

            if (resultSet.next())
            {
               tableDefinition.append("CREATE " + tableType + " " + schemaTableName
                                      + " AS " + resultSet.getString(1) + "\n");
            }
            resultSet.close();
            sqlStatement2.close();
            sqlStatement.close();
            return (tableDefinition.toString().replaceAll(dbIdentifierQuoteString, identifierQuoteString));
         }
         // TABLE
         else
            tableDefinition.append("CREATE " + tableType + " " + schemaTableName + " (\n    ");

         // Begin by creating the individual column field definitions.
         // Column name, data type, default, and isNullable.

         // Use information_schema view.
         sqlStatementString = "SELECT table_catalog, table_name, column_name, ordinal_position, "
                              + "column_default, is_nullable, data_type, character_maximum_length, "
                              + "numeric_precision, numeric_scale, datetime_precision FROM "
                              + "information_schema.columns " + "WHERE table_catalog='"
                              + MyJSQLView_Access.getDBName() + "' AND " + "table_schema='" + schemaName
                              + "' AND table_name='" + tableName + "' ORDER BY ordinal_position";
         // System.out.println(sqlStatementString);

         resultSet = sqlStatement.executeQuery(sqlStatementString);

         while (resultSet.next())
         {
            // =============
            // Column name.

            tableDefinition.append(identifierQuoteString + resultSet.getString("column_name")
                                   + identifierQuoteString + " ");

            // =============
            // Column type.

            columnType = resultSet.getString("data_type");
            columnName = resultSet.getString("column_name");

            // Integer & Sequences
            if (columnType.indexOf("int") != -1)
            {
               if (columnType.equals("integer") && resultSet.getString("column_default") != null
                   && resultSet.getString("column_default").indexOf("nextval") != -1)
                  tableDefinition.append("serial");
               else if (columnType.equals("bigint") && resultSet.getString("column_default") != null
                        && resultSet.getString("column_default").indexOf("nextval") != -1)
                  tableDefinition.append("bigserial");
               else
                  tableDefinition.append(columnType);
            }
            // Numeric Types
            else if (columnType.equals("numeric"))
            {
               if (resultSet.getString("numeric_precision") != null
                   || resultSet.getString("numeric_scale") != null)
                  tableDefinition.append(columnType + "(" + resultSet.getString("numeric_precision") + ","
                                         + resultSet.getString("numeric_scale") + ")");
               else
                  tableDefinition.append(columnType);

            }
            // Character Types
            else if (columnType.indexOf("character") != -1)
            {
               if (columnType.equals("character"))
                  tableDefinition.append("char(" + resultSet.getString("character_maximum_length") + ")");
               else
                  tableDefinition.append("varchar(" + resultSet.getString("character_maximum_length") + ")");
            }
            // Time & Timestamp
            else if (columnType.indexOf("time") != -1)
            {
               if (resultSet.getString("datetime_precision") != null)
               {
                  if (columnType.indexOf("timestamp") != -1)
                  {
                     if (columnType.indexOf("without") != -1)
                        tableDefinition.append("timestamp(" + resultSet.getString("datetime_precision")
                                               + ") without time zone");
                     else
                        tableDefinition.append("timestamp(" + resultSet.getString("datetime_precision")
                                               + ") with time zone");
                  }
                  else
                  {
                     if (columnType.indexOf("without") != -1)
                        tableDefinition.append("time(" + resultSet.getString("datetime_precision")
                                               + ") without time zone");
                     else
                        tableDefinition.append("time(" + resultSet.getString("datetime_precision")
                                               + ") with time zone");
                  }
               }
               else
                  tableDefinition.append(columnType);
            }
            // Interval
            else if (columnType.equals("interval") && resultSet.getString("datetime_precision") != null)
            {
               tableDefinition.append(columnType + "(" + resultSet.getString("datetime_precision") + ")");
            }
            // Bit
            else if (columnType.indexOf("bit") != -1
                     && resultSet.getString("character_maximum_length") != null)
            {
               tableDefinition.append(columnType + "(" + resultSet.getString("character_maximum_length")
                                      + ")");
            }
            // Arrays
            else if (columnType.equals("ARRAY"))
            {
               sqlStatementString = "SELECT format_type(atttypid, atttypmod) FROM pg_attribute WHERE "
                                    + "attrelid='" + tableName + "'::regclass " + "AND attname='"
                                    + columnName + "'";
               // System.out.println(sqlStatementString);

               resultSet2 = sqlStatement2.executeQuery(sqlStatementString);
               resultSet2.next();
               tableDefinition.append(resultSet2.getString(1));
               resultSet2.close();
            }
            else
               tableDefinition.append(columnType);

            // ==========================
            // Column Default & NOT NULL

            if (resultSet.getString("column_default") != null
                && resultSet.getString("column_default").indexOf("nextval") == -1)
            {
               String defaultString = resultSet.getString("column_default");

               if (defaultString.indexOf("::") != -1)
                  tableDefinition.append(" DEFAULT "
                                         + defaultString.substring(0, defaultString.indexOf("::")));
               else
                  tableDefinition.append(" DEFAULT " + defaultString);

               if (resultSet.getString("is_nullable").equals("NO"))
                  tableDefinition.append(" NOT NULL,\n    ");
               else
                  tableDefinition.append(",\n    ");
            }
            else
            {
               if (resultSet.getString("column_default") == null
                   && resultSet.getString("is_nullable").equals("YES"))
                  tableDefinition.append(" DEFAULT NULL,\n    ");
               else
               {
                  if (resultSet.getString("is_nullable").equals("NO"))
                     tableDefinition.append(" NOT NULL,\n    ");
                  else
                     tableDefinition.append(",\n    ");
               }
            }
         }

         // Create the keys for the table. Again use the
         // information_schema.

         sqlStatementString = "SELECT table_catalog, table_name, column_name, constraint_name FROM "
                              + "information_schema.key_column_usage " + "WHERE table_catalog='"
                              + MyJSQLView_Access.getDBName() + "' AND " + "table_schema='" + schemaName
                              + "' AND table_name='" + tableName + "'";
         // System.out.println(sqlStatementString);

         resultSet = sqlStatement.executeQuery(sqlStatementString);

         primaryKeys = new StringBuffer();
         uniqueKeys = new StringBuffer();
         foreignKeys = new StringBuffer();
         referenceSchemaName = "";
         referenceTableName = "";
         referenceColumnName = "";
         constraint = "";
         onDeleteRule = "";

         while (resultSet.next())
         {
            constraint = resultSet.getString("constraint_name");
            columnName = resultSet.getString("column_name");

            // Unique Keys
            if (constraint.indexOf("_key") != -1)
               uniqueKeys.append(identifierQuoteString + columnName + identifierQuoteString + ",");

            // Primary Keys
            else if (constraint.indexOf("pkey") != -1)
               primaryKeys.append(identifierQuoteString + columnName + identifierQuoteString + ",");

            // Foreign Keys. There should be only one?
            else if (constraint.indexOf("fkey") != -1)
               foreignKeys.append(identifierQuoteString + columnName + identifierQuoteString + ",");
         }

         // Create the appropriate SQL for the keys. Choosen to
         // use a breviated form. A future version may need to
         // more closely/duplicate pg_dump.

         if (!(uniqueKeys.toString()).equals(""))
            tableDefinition.append("UNIQUE (" 
                                   + uniqueKeys.substring(0, uniqueKeys.length() - 1) 
                                   + "),\n    ");

         if (!(primaryKeys.toString()).equals(""))
            tableDefinition.append("PRIMARY KEY (" 
                                   + primaryKeys.substring(0, primaryKeys.length() - 1)
                                   + "),\n    ");

         if (!(foreignKeys.toString()).equals(""))
         {
            // Obtaining the table who owns the foreign key
            // and its name.

            sqlStatementString = "SELECT table_catalog, table_schema, table_name, " 
                                 + "column_name, constraint_name FROM "
                                 + "information_schema.constraint_column_usage "
                                 + "WHERE table_catalog='"
                                 + MyJSQLView_Access.getDBName()
                                 + "' AND "
                                 + "constraint_name='"
                                 + constraint + "'";
            // System.out.println(sqlStatementString);

            resultSet = sqlStatement.executeQuery(sqlStatementString);
            resultSet.next();
            referenceSchemaName = identifierQuoteString + resultSet.getString("table_schema")
                                  + identifierQuoteString;
            referenceTableName = identifierQuoteString + resultSet.getString("table_name")
                                 + identifierQuoteString;
            referenceColumnName = identifierQuoteString + resultSet.getString("column_name")
                                  + identifierQuoteString;

            // Obtaining the constraint for ON DELETE.

            sqlStatementString = "SELECT constraint_catalog, constraint_name, delete_rule FROM "
                                 + "information_schema.referential_constraints "
                                 + "WHERE constraint_catalog='" + MyJSQLView_Access.getDBName()
                                 + "' AND " + "constraint_name='" + constraint + "'";
            // System.out.println(sqlStatementString);

            resultSet = sqlStatement.executeQuery(sqlStatementString);
            resultSet.next();
            onDeleteRule = resultSet.getString("delete_rule");

            // Finally create the SQL for the key.
            tableDefinition.append("FOREIGN KEY (" + foreignKeys.substring(0, foreignKeys.length() - 1)
                                   + ") REFERENCES " + referenceSchemaName + "." + referenceTableName + "("
                                   + referenceColumnName + ") ON DELETE " + onDeleteRule + " \n    ");
         }
         tableDefinition.delete(tableDefinition.length() - 6, tableDefinition.length());
         tableDefinition.append("\n);\n");

         resultSet.close();
         sqlStatement2.close();
         sqlStatement.close();
         return (tableDefinition.toString().replaceAll(dbIdentifierQuoteString, identifierQuoteString));
      }
      catch (SQLException e)
      {
         MyJSQLView_Access.displaySQLErrors(e, "TableDefinitionGenerator createPostgreSQLTableDefinition()");
         return (tableDefinition.toString().replaceAll(dbIdentifierQuoteString, identifierQuoteString));
      }
   }

   //================================================================
   // Class method for creating a given HSQL TABLE definition.
   //================================================================

   private String createHSQLTableDefinition()
   {
      // Class Method Instances.
      StringBuffer tableDefinition = new StringBuffer();
      String tableType;
      String autoIncrementColumnName;
      String columnName, columnType;
      StringBuffer uniqueKeys;
      String primaryKeys, foreignKeys;
      String referenceTableName, referenceColumnName;
      String onDeleteRule;

      DatabaseMetaData dbMetaData;
      ResultSetMetaData tableMetaData;

      String sqlStatementString;
      Statement sqlStatement;
      ResultSet resultSet;
      
      // Beginning the creation of the string description
      // of the table Structure.
      try
      {
         sqlStatement = dbConnection.createStatement();
         
         // Collect the table type for the table.
         
         sqlStatementString = "SELECT " + dbIdentifierQuoteString + "TABLE_TYPE"
                               + dbIdentifierQuoteString + " FROM " + dbIdentifierQuoteString
                               + "INFORMATION_SCHEMA" + dbIdentifierQuoteString + "."
                               + dbIdentifierQuoteString + "SYSTEM_TABLES" + dbIdentifierQuoteString
                               + " WHERE " + dbIdentifierQuoteString + "TABLE_SCHEM"
                               + dbIdentifierQuoteString + "='" + schemaName
                               + "' AND TABLE_NAME='" + tableName + "'";
         //System.out.println(sqlStatementString);
         
         resultSet = sqlStatement.executeQuery(sqlStatementString);
         
         if (resultSet.next())
            tableType = resultSet.getString(1);
         else
            tableType = "TABLE";
         
         // Drop Table Statements As Needed.
         if (DBTablesPanel.getDataExportProperties().getTableStructure())
         {
            tableDefinition.append("DROP " + tableType + " IF EXISTS " + schemaTableName + ";\n");
         }
         
         // Table Creation Statement.
         if (tableType.equals("VIEW"))
         {
            sqlStatementString = "SELECT " + dbIdentifierQuoteString + "VIEW_DEFINITION"
                                 + dbIdentifierQuoteString + " FROM " + dbIdentifierQuoteString
                                 + "INFORMATION_SCHEMA" + dbIdentifierQuoteString + "."
                                 + dbIdentifierQuoteString + "SYSTEM_VIEWS" + dbIdentifierQuoteString
                                 + " WHERE " + dbIdentifierQuoteString + "TABLE_SCHEMA"
                                 + dbIdentifierQuoteString + "='" + schemaName
                                 + "' AND TABLE_NAME='" + tableName + "'";
            //System.out.println(sqlStatementString);

            resultSet = sqlStatement.executeQuery(sqlStatementString);

            if (resultSet.next())
            {
               tableDefinition.append("CREATE " + tableType + " " + schemaTableName
                                      + " AS " + resultSet.getString(1) + ";\n");
            }
            resultSet.close();
            sqlStatement.close();
            return (tableDefinition.toString().replaceAll(dbIdentifierQuoteString, identifierQuoteString));
         }
         // TABLE
         else
            tableDefinition.append("CREATE " + tableType + " " + schemaTableName + " (\n    ");

         // Begin by creating the individual column field definitions.
         // Column name, data type, default, and isNullable.

         dbMetaData = dbConnection.getMetaData();

         sqlStatementString = "SELECT LIMIT 0 1 * FROM " + schemaTableName;
         // System.out.println(sqlStatementString);

         resultSet = sqlStatement.executeQuery(sqlStatementString);
         tableMetaData = resultSet.getMetaData();

         resultSet = dbMetaData.getColumns(tableMetaData.getCatalogName(1),
                                           tableMetaData.getSchemaName(1),
                                           tableMetaData.getTableName(1), "%");

         // Obtain IDENTITY column if there is one.
         autoIncrementColumnName = "";
         for (int i = 1; i < tableMetaData.getColumnCount() + 1; i++)
         {
            if (tableMetaData.isAutoIncrement(i))
               autoIncrementColumnName = tableMetaData.getColumnName(i);
         }

         // Now proceed with rest of structure.
         while (resultSet.next())
         {
            // =============
            // Column name.

            tableDefinition.append(identifierQuoteString + resultSet.getString("COLUMN_NAME")
                                   + identifierQuoteString + " ");

            // =============
            // Column type.

            columnType = resultSet.getString("TYPE_NAME");

            // Character Types
            if (columnType.indexOf("CHAR") != -1)
            {
               if (columnType.equals("CHAR"))
                  tableDefinition.append("CHAR(" + resultSet.getString("COLUMN_SIZE") + ")");
               else if (columnType.equals("VARCHAR") || columnType.equals("VARCHAR_IGNORECASE"))
                  tableDefinition.append("VARCHAR(" + resultSet.getString("COLUMN_SIZE") + ")");
               else
                  tableDefinition.append(columnType);

            }
            // Integer/BigInt Types
            else if (columnType.equals("INTEGER") || columnType.equals("BIGINT"))
            {
               tableDefinition.append(columnType);

               // Assign IDENTITY as needed.
               if (resultSet.getString("COLUMN_NAME").equals(autoIncrementColumnName))
                  tableDefinition.append(" IDENTITY");
            }
            // Double Types
            else if (columnType.equals("DOUBLE"))
            {
               // Manual Indicates a precision, but
               // can not create DOUBLE(p).
               tableDefinition.append(columnType);
            }
            // Numeric Types
            else if (columnType.equals("NUMERIC"))
            {
               // Manual Indicates a precision, takes,
               // but SYSTEM_COLUMNS gives not clue to precision.
               tableDefinition.append(columnType);
            }
            // Decimal Types
            else if (columnType.equals("DECIMAL"))
            {
               tableDefinition.append(columnType + "(" + resultSet.getString("COLUMN_SIZE")
                                      + "," + resultSet.getString("DECIMAL_DIGITS") + ")");
            }
            // Time & Timestamp
            else if (columnType.indexOf("TIMESTAMP") != -1)
            {
               // Unable to tell where the precision is
               // located in system table. Column_Size of
               // 29 Seems to Indicate Timestamp(0)
               if (resultSet.getString("COLUMN_SIZE").equals("29"))
                  tableDefinition.append(columnType + "(0)");
               else
                  tableDefinition.append(columnType);
            }
            // Integer, Float, Real, Boolean, Bit, Date, Time
            else
            {
               tableDefinition.append(columnType);
            }

            // ==========================
            // Column Default & NOT NULL

            if (resultSet.getString("COLUMN_DEF") != null)
            {
               String defaultString = resultSet.getString("COLUMN_DEF");

               if (defaultString.indexOf("::") != -1)
                  tableDefinition.append(" DEFAULT " 
                                         + defaultString.substring(0, defaultString.indexOf(":")));
               else
                  tableDefinition.append(" DEFAULT " + defaultString);

               if (resultSet.getString("IS_NULLABLE").equals("NO"))
                  tableDefinition.append(" NOT NULL,\n    ");
               else
                  tableDefinition.append(",\n    ");
            }
            else
            {
               if (resultSet.getString("COLUMN_DEF") == null
                   && resultSet.getString("IS_NULLABLE").equals("YES"))
                  tableDefinition.append(" DEFAULT NULL,\n    ");
               else
               {
                  if (resultSet.getString("IS_NULLABLE").equals("NO"))
                     tableDefinition.append(" NOT NULL,\n    ");
                  else
                     tableDefinition.append(",\n    ");
               }
            }
         }

         // Create the keys for the table.
         columnName = "";
         primaryKeys = "";
         uniqueKeys = new StringBuffer();
         foreignKeys = "";
         referenceTableName = "";
         referenceColumnName = "";
         // onUpdateRule = "";
         onDeleteRule = "";

         // Primary Keys
         resultSet = dbMetaData.getPrimaryKeys(tableMetaData.getCatalogName(1),
                                               tableMetaData.getSchemaName(1),
                                               tableMetaData.getTableName(1));
         while (resultSet.next())
         {
            columnName = resultSet.getString("COLUMN_NAME");

            primaryKeys += identifierQuoteString + columnName + identifierQuoteString + ",";
         }

         // Unique Keys
         resultSet = dbMetaData.getIndexInfo(tableMetaData.getCatalogName(1),
                                             tableMetaData.getSchemaName(1),
                                             tableMetaData.getTableName(1), true, false);
         while (resultSet.next())
         {
            columnName = resultSet.getString("COLUMN_NAME");

            // Only place unidentitied keys in uniqe string.
            if (primaryKeys.indexOf(columnName) == -1)
               uniqueKeys.append(identifierQuoteString + columnName + identifierQuoteString + ",");
         }

         // Add Unique & Primary Keys.
         if (!(uniqueKeys.toString()).equals(""))
            tableDefinition.append("UNIQUE ("
                                   + uniqueKeys.delete((uniqueKeys.length() - 1), uniqueKeys.length())
                                   + "),\n    ");

         if (!primaryKeys.equals(""))
            tableDefinition.append("PRIMARY KEY (" 
                                   + primaryKeys.substring(0, primaryKeys.length() - 1)
                                   + "),\n    ");

         // Foreign Keys
         resultSet = dbMetaData.getImportedKeys(tableMetaData.getCatalogName(1),
                                                tableMetaData.getSchemaName(1),
                                                tableMetaData.getTableName(1));
         while (resultSet.next())
         {
            columnName = resultSet.getString("FKCOLUMN_NAME");
            referenceTableName = resultSet.getString("PKTABLE_NAME");
            referenceColumnName = resultSet.getString("PKCOLUMN_NAME");

            // These rules return integer values that can not
            // be correlated to a specific rule. Default to
            // DELETE ON CASCADE.
            // onUpdateRule = resultSet.getString("UPDATE_RULE");
            // onDeleteRule = resultSet.getString("DELETE_RULE");
            onDeleteRule = "CASCADE";

            foreignKeys += identifierQuoteString + columnName + identifierQuoteString;

            tableDefinition.append("FOREIGN KEY (" + foreignKeys + ") REFERENCES "
                                   + identifierQuoteString + referenceTableName 
                                   + identifierQuoteString + "(" + identifierQuoteString
                                   + referenceColumnName + identifierQuoteString 
                                   + ") ON DELETE " + onDeleteRule + " \n    ");
         }
         tableDefinition.delete(tableDefinition.length() - 6, tableDefinition.length());
         tableDefinition.append("\n);\n");

         resultSet.close();
         sqlStatement.close();
         return (tableDefinition.toString().replaceAll(dbIdentifierQuoteString, identifierQuoteString));
      }
      catch (SQLException e)
      {
         MyJSQLView_Access.displaySQLErrors(e, "TableDefinitionGenerator createHSQLTableDefinition()");
         return (tableDefinition.toString().replaceAll(dbIdentifierQuoteString, identifierQuoteString));
      }
   }

   //================================================================
   // Class method for creating a given Oracle TABLE definition.
   //================================================================

   private String createOracleTableDefinition()
   {
      // Class Method Instances.
      StringBuffer tableDefinition = new StringBuffer();
      String tableType;
      String columnName, columnClass, columnType;
      int columnSize, columnDecimalDigits;
      String columnDefault, columnIsNullable;
      HashMap autoIncrementColumnNameHashMap;
      String sequenceKeyPresent;

      String primaryKeys, uniqueKeys, foreignKeys;
      String referenceTableName, referenceColumnName;
      // String onUpdateRule;
      String onDeleteRule;

      DatabaseMetaData dbMetaData;
      ResultSetMetaData tableMetaData;

      String sqlStatementString;
      Statement sqlStatement;
      ResultSet resultSet;

      // Begin creating the table structure scheme.

      try
      {
         sqlStatement = dbConnection.createStatement();
         dbMetaData = dbConnection.getMetaData();
         
         // Collect table type for the table.
         
         sqlStatementString = "SELECT OBJECT_TYPE FROM USER_OBJECTS WHERE "
                               + "OBJECT_NAME='" + tableName + "'";
         //System.out.println(sqlStatementString);
         
         resultSet = sqlStatement.executeQuery(sqlStatementString);
         
         if (resultSet.next())
            tableType = resultSet.getString(1);
         else
            tableType = "TABLE";
         
         // Drop Table Statements As Needed.
         if (DBTablesPanel.getDataExportProperties().getTableStructure())
         {
            if (tableType.equals("VIEW"))
               tableDefinition.append("DROP VIEW " + schemaTableName + ";\n");
            else
               tableDefinition.append("DROP " + tableType + " " + schemaTableName + ";\n");
         }
         
         // Table Creation Statement.
         if (tableType.equals("VIEW"))
         {
            
            sqlStatementString = "SELECT TEXT FROM ALL_VIEWS WHERE "
                                 + "VIEW_NAME='" + tableName + "'";
            //System.out.println(sqlStatementString);

            resultSet = sqlStatement.executeQuery(sqlStatementString);

            if (resultSet.next())
            {
               tableDefinition.append("CREATE " + tableType + " " + schemaTableName
                                      + " AS " + resultSet.getString(1) + ";\n");
            }
            resultSet.close();
            sqlStatement.close();
            return (tableDefinition.toString().replaceAll(dbIdentifierQuoteString, identifierQuoteString));
         }
         // TABLE
         else
            tableDefinition.append("CREATE " + tableType + " " + schemaTableName + " (\n    ");
         
         // Obtain SEQUENCE column if there is one.

         sqlStatementString = "SELECT * FROM " + schemaTableName + " WHERE ROWNUM=1";
         // System.out.println(sqlStatementString);

         resultSet = sqlStatement.executeQuery(sqlStatementString);
         tableMetaData = resultSet.getMetaData();

         sequenceKeyPresent = "";
         autoIncrementColumnNameHashMap = new HashMap();

         for (int i = 1; i < tableMetaData.getColumnCount() + 1; i++)
         {
            columnName = tableMetaData.getColumnName(i);

            sqlStatementString = "SELECT USER_IND_COLUMNS.INDEX_NAME FROM USER_IND_COLUMNS, "
                                 + "ALL_SEQUENCES WHERE USER_IND_COLUMNS.INDEX_NAME="
                                 + "ALL_SEQUENCES.SEQUENCE_NAME AND USER_IND_COLUMNS.TABLE_NAME='"
                                 + tableName + "' AND USER_IND_COLUMNS.COLUMN_NAME='" + columnName + "'";
            // System.out.println(sqlStatementString);

            resultSet = sqlStatement.executeQuery(sqlStatementString);

            if (resultSet.next())
            {
               // System.out.println(columnName + ": " +
               // resultSet.getString("INDEX_NAME"));
               autoIncrementColumnNameHashMap.put(columnName, resultSet.getString("INDEX_NAME"));
            }
         }

         // Create the individual column field definitions.
         // Column name, data type, default, and isNullable.

         resultSet = dbMetaData.getColumns(MyJSQLView_Access.getDBName(), schemaName, tableName, "%");

         while (resultSet.next())
         {
            // Collect all necessary columns information from
            // the resultset

            columnName = resultSet.getString("COLUMN_NAME");
            columnClass = resultSet.getString("DATA_TYPE");
            columnType = resultSet.getString("TYPE_NAME");
            columnSize = resultSet.getInt("COLUMN_SIZE");
            columnDecimalDigits = resultSet.getInt("DECIMAL_DIGITS");
            columnDefault = resultSet.getString("COLUMN_DEF");
            columnIsNullable = resultSet.getString("IS_NULLABLE");
            // System.out.println(columnName + " " + columnClass + " " +
            // columnType +
            // " " + columnSize + " " + columnDecimalDigits + " " +
            // columnDefault + " " + columnIsNullable);

            // =============
            // Column name.

            tableDefinition.append(identifierQuoteString + columnName + identifierQuoteString + " ");

            // =============
            // Column type.

            // Character Types
            if (columnType.indexOf("CHAR") != -1)
            {
               if (columnType.equals("CHAR"))
                  tableDefinition.append("CHAR(" + columnSize + ")");
               else if (columnType.equals("NCHAR"))
                  tableDefinition.append("NCHAR(" + columnSize / 2 + ")");
               else if (columnType.equals("VARCHAR2"))
                  tableDefinition.append("VARCHAR2(" + columnSize + ")");
               else if (columnType.equals("NVARCHAR2"))
                  tableDefinition.append("NVARCHAR2(" + columnSize / 2 + ")");
               else
                  tableDefinition.append(columnType);
            }
            // Number Types
            else if (columnType.equals("NUMBER"))
            {
               if (columnClass.toLowerCase().indexOf("double") != -1)
                  tableDefinition.append("FLOAT");
               else
               {
                  tableDefinition.append("NUMBER(" + columnSize + "," + columnDecimalDigits + ")");
               }
            }
            // Binary_Float Types
            else if (columnType.equals("BINARY_FLOAT"))
            {
               tableDefinition.append(columnType);
            }
            // Binary_Double Types
            else if (columnType.equals("BINARY_DOUBLE"))
            {
               tableDefinition.append(columnType);
            }
            // Raw Types
            else if (columnType.equals("RAW"))
            {
               tableDefinition.append("RAW(" + columnSize + ")");
            }
            // Timestamp
            else if (columnType.equals("TIMESTAMP"))
            {
               tableDefinition.append(columnType);
            }
            // TimestampTZ
            else if (columnType.equals("TIMESTAMPTZ"))
            {
               tableDefinition.append("TIMESTAMP WITH TIME ZONE");
            }
            // TimestampLTZ
            else if (columnType.equals("TIMESTAMPLTZ"))
            {
               tableDefinition.append("TIMESTAMP WITH LOCAL TIME ZONE");
            }
            // Interval
            else if (columnType.indexOf("INTERVAL") != -1)
            {
               if (columnType.equals("INTERVALYM"))
               {
                  tableDefinition.append("INTERVAL YEAR TO MONTH");
               }
               else if (columnType.equals("INTERVALDS"))
               {
                  tableDefinition.append("INTERVAL DAY TO SECOND");
               }
               else
                  tableDefinition.append(columnType);
            }
            // Long, Long Raw, Blob, BFile,
            else
            {
               tableDefinition.append(columnType);
            }

            // ==========================
            // Column Default & NOT NULL

            if (columnDefault != null)
            {
               if (columnDefault.indexOf("::") != -1)
                  tableDefinition.append(" DEFAULT " 
                                         + (columnDefault.substring(0, columnDefault.indexOf(":"))).trim());
               else
                  tableDefinition.append(" DEFAULT " + columnDefault.trim());

               if (columnIsNullable.equals("NO"))
                  tableDefinition.append(" NOT NULL,\n    ");
               else
                  tableDefinition.append(",\n    ");
            }
            else
            {
               if (columnDefault == null && columnIsNullable.equals("YES"))
                  tableDefinition.append(" DEFAULT NULL,\n    ");
               else
               {
                  if (columnIsNullable.equals("NO"))
                     tableDefinition.append(" NOT NULL,\n    ");
                  else
                     tableDefinition.append(",\n    ");
               }
            }
         }

         // ===============================
         // Create the keys for the table.

         columnName = "";
         primaryKeys = "";
         uniqueKeys = "";
         foreignKeys = "";
         referenceTableName = "";
         referenceColumnName = "";
         // onUpdateRule = "";
         onDeleteRule = "";

         // Primary Keys
         resultSet = dbMetaData.getPrimaryKeys(MyJSQLView_Access.getDBName(), schemaName, tableName);

         while (resultSet.next())
         {
            columnName = resultSet.getString("COLUMN_NAME");
            if (autoIncrementColumnNameHashMap.containsKey(columnName))
               sequenceKeyPresent = "primary_" + columnName;

            primaryKeys += identifierQuoteString + columnName + identifierQuoteString + ",";
         }

         // Unique Keys
         // Clueless why needs quotes?
         resultSet = dbMetaData.getIndexInfo(MyJSQLView_Access.getDBName(), dbIdentifierQuoteString
                                                                            + schemaName
                                                                            + dbIdentifierQuoteString,
            dbIdentifierQuoteString + tableName + dbIdentifierQuoteString, false, false);

         while (resultSet.next())
         {
            columnName = resultSet.getString("COLUMN_NAME");

            // Only place unidentitied keys in uniqe string.
            if (columnName != null && primaryKeys.indexOf(columnName) == -1)
            {
               if (autoIncrementColumnNameHashMap.containsKey(columnName))
                  sequenceKeyPresent = "unique_" + columnName;

               uniqueKeys += identifierQuoteString + columnName + identifierQuoteString + ",";
            }
         }

         // Add Unique & Primary Keys.
         if (!uniqueKeys.equals("") && sequenceKeyPresent.indexOf("unique") == -1)
            tableDefinition.append("UNIQUE (" 
                                   + uniqueKeys.substring(0, uniqueKeys.length() - 1)
                                   + "),\n    ");

         if (!primaryKeys.equals("") && sequenceKeyPresent.indexOf("primary") == -1)
            tableDefinition.append("PRIMARY KEY (" 
                                   + primaryKeys.substring(0, primaryKeys.length() - 1)
                                   + "),\n    ");

         // Foreign Keys
         // The Oracle database is having considerable delay right here
         // with collecting the imported keys.
         resultSet = dbMetaData.getImportedKeys(MyJSQLView_Access.getDBName(), schemaName, tableName);

         while (resultSet.next())
         {
            columnName = resultSet.getString("FKCOLUMN_NAME");
            referenceTableName = resultSet.getString("PKTABLE_NAME");
            referenceColumnName = resultSet.getString("PKCOLUMN_NAME");

            // These rules return integer values that can not
            // be correlated to a specific rule. Default to
            // DELETE ON CASCADE.
            // onDeleteRule = resultSet.getString("DELETE_RULE");
            onDeleteRule = "CASCADE";

            foreignKeys += identifierQuoteString + columnName + identifierQuoteString;

            tableDefinition.append("FOREIGN KEY (" + foreignKeys + ") REFERENCES " 
                                   + identifierQuoteString + referenceTableName 
                                   + identifierQuoteString + "(" + identifierQuoteString
                                   + referenceColumnName + identifierQuoteString + ") ON DELETE "
                                   + onDeleteRule + " \n    ");
         }
         tableDefinition.delete(tableDefinition.length() - 6, tableDefinition.length());
         tableDefinition.append("\n);\n");

         // Add Sequence as needed.
         if (!sequenceKeyPresent.equals(""))
         {
            columnName = sequenceKeyPresent.substring(sequenceKeyPresent.indexOf("_") + 1);
            tableDefinition.append("CREATE SEQUENCE " + identifierQuoteString
                                   + autoIncrementColumnNameHashMap.get(columnName)
                                   + identifierQuoteString + ";\n");
            tableDefinition.append("ALTER TABLE " + identifierQuoteString + tableName
                                   + identifierQuoteString + " ADD CONSTRAINT " 
                                   + identifierQuoteString
                                   + autoIncrementColumnNameHashMap.get(columnName) 
                                   + identifierQuoteString + " ");
            if (sequenceKeyPresent.indexOf("primary") != -1)
               tableDefinition.append("PRIMARY KEY (" 
                                      + primaryKeys.substring(0, primaryKeys.length() - 1)
                                      + ");\n    ");
            else
               tableDefinition.append("UNIQUE (" 
                                      + uniqueKeys.substring(0, uniqueKeys.length() - 1)
                                      + ");\n    ");
         }

         resultSet.close();
         sqlStatement.close();
         return (tableDefinition.toString().replaceAll(dbIdentifierQuoteString, identifierQuoteString));
      }
      catch (SQLException e)
      {
         MyJSQLView_Access.displaySQLErrors(e, "TableDefinitionGenerator createOracleTableDefinition()");
         return (tableDefinition.toString().replaceAll(dbIdentifierQuoteString, identifierQuoteString));
      }
   }

   //==============================================================
   // Class method for getting CREATE TABLE definition.
   //==============================================================

   protected String getTableDefinition()
   {
      // Determine the correct table structure method to
      // apply and proceed with creating a string of
      // the table definition.

      // MySQL
      if (MyJSQLView_Access.getSubProtocol().equals("mysql"))
      {
         return createMySQLTableDefinition();
      }

      // PostgreSQL
      else if (MyJSQLView_Access.getSubProtocol().equals("postgresql"))
      {
         return createPostgreSQLTableDefinition();
      }

      // HSQL
      else if (MyJSQLView_Access.getSubProtocol().indexOf("hsql") != -1)
      {
         return createHSQLTableDefinition();
      }

      // Oracle
      else if (MyJSQLView_Access.getSubProtocol().indexOf("oracle") != -1)
      {
         return createOracleTableDefinition();
      }

      // Default
      else
         return ";\n";
   }
}