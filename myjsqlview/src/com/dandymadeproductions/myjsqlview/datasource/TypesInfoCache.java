//=================================================================
//                     MyJSQLView TypesInfoCache
//=================================================================
//
//    This class provides a storage cache for characterizing data
// types information for the various support databases.
//
//                     << TypesInfoCache.java >>
//
//=================================================================
// Copyright (C) 2005-2013 Dana M. Proctor
// Version 1.6 10/22/2013
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
// Version 1.0 Original Initial Incomplete, but Functional TypesInfoCache Class.
//             Concept Based on PostgreSQL TypeInfoCache, but Generalized to be
//             a More Efficient Lookup Table Mapping of integers Not Objects.
//         1.1 Change in Class Instance DEFAULT_DATASINK_TYPE to HSQL2. Renaming
//             of static Class final Instances to Uppercase. Changes in POSTGRESQL_TYPES
//             for ID of HSQL Types.
//         1.2 Corrections to Sync With Changes in TypeID Instances.
//         1.3 Syncronized Class Instance POSTGRESQL__ to POSTGRESQL_ARRAYS. Assigning
//             the Same to PostgreSQL Arrays Types, _INT2 etc., in Method getType(). 
//             Change in Conversion of H2 & PostgreSQL Arrays for HSQL & Derby to
//             Long Varchars.
//         1.4 Changes to Class Instances H2_TYPES & POSTGRESQL_TYPES for Correct
//             Conversion Processing.
//         1.5 Added Class Instances HSQL_TYPES & DERBY_TYPES Along With Conditional
//             Check in Constructor of Such.
//         1.6 Added Class Instances MYSQL_TYPES, ORACLE_TYPES, SQLITE_TYPES,
//             & MSACCESS_TYPES Along With Conditional Check in Constructor of Such.
//        
//-----------------------------------------------------------------
//                 danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview.datasource;

import java.util.HashMap;
import java.util.Map;

/**
 *    The TypesInfoCache class provides a storage cache for characterizing
 * data types information for the various support databases.
 * 
 * @author Dana M. Proctor
 * @version 1.6 10/22/2013
 */

public class TypesInfoCache
{
   // Class Instances
   private String dataSourceType, dataSinkType;
   private Map<String, Integer> nameToType;
   
   // Basic types infomation:
   // 0 - Source type id
   // 1 - H2 Sink type id
   // 2 - HSQL Sink type id
   // 3 - Derby Sink type id
   
   private static final int TYPE_NAME = 0;
   private static final int H2_TYPE = 1;
   private static final int HSQL_TYPE = 2;
   private static final int DERBY_TYPE = 3;
   
   private static final String DEFAULT_DATASINK_TYPE = ConnectionManager.HSQL2; 
   
   // Conversion Instances
   
   private static final int[][] H2_TYPES = {
       {TypeID.H2_IDENTITY, TypeID.H2_IDENTITY, TypeID.HSQL_IDENTITY, TypeID.DERBY_BIGINT},
       {TypeID.H2_CHAR, TypeID.H2_CHAR, TypeID.HSQL_CHAR, TypeID.DERBY_CHAR},
       {TypeID.H2_VARCHAR, TypeID.H2_VARCHAR, TypeID.HSQL_VARCHAR, TypeID.DERBY_VARCHAR},
       {TypeID.H2_VARCHAR_IGNORECASE, TypeID.H2_VARCHAR_IGNORECASE, TypeID.HSQL_VARCHAR, TypeID.DERBY_VARCHAR},
       {TypeID.H2_BINARY, TypeID.H2_BINARY, TypeID.HSQL_VARBINARY, TypeID.DERBY_BLOB},
       {TypeID.H2_BLOB, TypeID.H2_BLOB, TypeID.HSQL_BLOB, TypeID.DERBY_BLOB},
       {TypeID.H2_CLOB, TypeID.H2_CLOB, TypeID.HSQL_CLOB, TypeID.DERBY_CLOB},
       {TypeID.H2_OTHER, TypeID.H2_OTHER, TypeID.HSQL_BLOB, TypeID.DERBY_BLOB},
       {TypeID.H2_UUID, TypeID.H2_UUID, TypeID.HSQL_LONGVARCHAR, TypeID.DERBY_LONG_VARCHAR},
       {TypeID.H2_BOOLEAN, TypeID.H2_BOOLEAN, TypeID.HSQL_BOOLEAN, TypeID.DERBY_BOOLEAN},
       {TypeID.H2_TINYINT, TypeID.H2_TINYINT, TypeID.HSQL_TINYINT, TypeID.DERBY_SMALLINT},
       {TypeID.H2_SMALLINT, TypeID.H2_SMALLINT, TypeID.HSQL_SMALLINT, TypeID.DERBY_SMALLINT},
       {TypeID.H2_INTEGER, TypeID.H2_INTEGER, TypeID.HSQL_INTEGER, TypeID.DERBY_INTEGER},
       {TypeID.H2_BIGINT, TypeID.H2_BIGINT, TypeID.HSQL_BIGINT, TypeID.DERBY_BIGINT},
       {TypeID.H2_REAL, TypeID.H2_REAL, TypeID.HSQL_REAL, TypeID.DERBY_REAL},
       {TypeID.H2_DOUBLE, TypeID.H2_DOUBLE, TypeID.HSQL_DOUBLE, TypeID.DERBY_DOUBLE},
       {TypeID.H2_DECIMAL, TypeID.H2_DECIMAL, TypeID.HSQL_NUMERIC, TypeID.DERBY_DECIMAL},
       {TypeID.H2_DATE, TypeID.H2_DATE, TypeID.HSQL_DATE, TypeID.DERBY_DATE},
       {TypeID.H2_TIME, TypeID.H2_TIME, TypeID.HSQL_TIME, TypeID.DERBY_TIME},
       {TypeID.H2_TIMESTAMP, TypeID.H2_TIMESTAMP, TypeID.HSQL_TIMESTAMP, TypeID.DERBY_TIMESTAMP},
       {TypeID.H2_ARRAY, TypeID.H2_ARRAY, TypeID.HSQL_LONGVARCHAR, TypeID.DERBY_LONG_VARCHAR}};
    
   // HSQL2
   private static final int[][] HSQL_TYPES = {
       {TypeID.HSQL_IDENTITY, TypeID.H2_IDENTITY, TypeID.HSQL_IDENTITY, TypeID.DERBY_BIGINT},
       {TypeID.HSQL_CHAR, TypeID.H2_CHAR, TypeID.HSQL_CHAR, TypeID.DERBY_CHAR},
       {TypeID.HSQL_VARCHAR, TypeID.H2_VARCHAR, TypeID.HSQL_VARCHAR, TypeID.DERBY_VARCHAR},
       {TypeID.HSQL_LONGVARCHAR, TypeID.H2_LONGVARCHAR, TypeID.HSQL_LONGVARCHAR, TypeID.DERBY_LONG_VARCHAR},
       {TypeID.HSQL_CLOB, TypeID.H2_CLOB, TypeID.HSQL_CLOB, TypeID.DERBY_CLOB},
       {TypeID.HSQL_BINARY, TypeID.H2_BINARY, TypeID.HSQL_BINARY, TypeID.DERBY_BLOB},
       {TypeID.HSQL_VARBINARY, TypeID.H2_BLOB, TypeID.HSQL_VARBINARY, TypeID.DERBY_BLOB},
       {TypeID.HSQL_LONGVARBINARY, TypeID.H2_BLOB, TypeID.HSQL_LONGVARBINARY, TypeID.DERBY_BLOB},
       {TypeID.HSQL_BLOB, TypeID.H2_BLOB, TypeID.HSQL_BLOB, TypeID.DERBY_BLOB},
       {TypeID.HSQL_TINYINT, TypeID.H2_TINYINT, TypeID.HSQL_TINYINT, TypeID.DERBY_SMALLINT},
       {TypeID.HSQL_SMALLINT, TypeID.H2_SMALLINT, TypeID.HSQL_SMALLINT, TypeID.DERBY_SMALLINT},
       {TypeID.HSQL_INTEGER, TypeID.H2_INTEGER, TypeID.HSQL_INTEGER, TypeID.DERBY_INTEGER},
       {TypeID.HSQL_BIGINT, TypeID.H2_BIGINT, TypeID.HSQL_BIGINT, TypeID.DERBY_BIGINT},
       {TypeID.HSQL_FLOAT, TypeID.H2_REAL, TypeID.HSQL_FLOAT, TypeID.DERBY_REAL},
       {TypeID.HSQL_DOUBLE, TypeID.H2_DOUBLE, TypeID.HSQL_DOUBLE, TypeID.DERBY_DOUBLE},
       {TypeID.HSQL_REAL, TypeID.H2_REAL, TypeID.HSQL_REAL, TypeID.DERBY_REAL},
       {TypeID.HSQL_DECIMAL, TypeID.H2_DECIMAL, TypeID.HSQL_DECIMAL, TypeID.DERBY_DECIMAL},
       {TypeID.HSQL_NUMERIC, TypeID.H2_DECIMAL, TypeID.HSQL_NUMERIC, TypeID.DERBY_DECIMAL},
       {TypeID.HSQL_BOOLEAN, TypeID.H2_BOOLEAN, TypeID.HSQL_BOOLEAN, TypeID.DERBY_BOOLEAN},
       {TypeID.HSQL_BIT, TypeID.H2_BOOLEAN, TypeID.HSQL_BIT, TypeID.DERBY_BOOLEAN},
       {TypeID.HSQL_BIT_VARYING, TypeID.H2_VARCHAR, TypeID.HSQL_BIT_VARYING, TypeID.DERBY_CHAR_FOR_BIT_DATA},
       {TypeID.HSQL_DATE, TypeID.H2_DATE, TypeID.HSQL_DATE, TypeID.DERBY_DATE},
       {TypeID.HSQL_TIME, TypeID.H2_TIME, TypeID.HSQL_TIME, TypeID.DERBY_TIME},
       {TypeID.HSQL_TIME_WITH_TIME_ZONE, TypeID.H2_TIME, TypeID.HSQL_TIME_WITH_TIME_ZONE, TypeID.DERBY_TIME},
       {TypeID.HSQL_DATETIME, TypeID.H2_TIMESTAMP, TypeID.HSQL_DATETIME, TypeID.DERBY_TIMESTAMP},
       {TypeID.HSQL_TIMESTAMP, TypeID.H2_TIMESTAMP, TypeID.HSQL_TIMESTAMP, TypeID.DERBY_TIMESTAMP},
       {TypeID.HSQL_TIMESTAMP_WITH_TIME_ZONE, TypeID.H2_TIMESTAMP, TypeID.HSQL_TIMESTAMP_WITH_TIME_ZONE, TypeID.DERBY_TIMESTAMP},
       {TypeID.HSQL_INTERVAL, TypeID.H2_VARCHAR, TypeID.HSQL_INTERVAL, TypeID.DERBY_VARCHAR},
       {TypeID.HSQL_INTERVAL_YEAR_TO_MONTH, TypeID.H2_VARCHAR, TypeID.HSQL_INTERVAL_YEAR_TO_MONTH, TypeID.DERBY_VARCHAR},
       {TypeID.HSQL_INTERVAL_YEAR, TypeID.H2_VARCHAR, TypeID.HSQL_INTERVAL_YEAR, TypeID.DERBY_VARCHAR},
       {TypeID.HSQL_INTERVAL_DAY_TO_HOUR, TypeID.H2_VARCHAR, TypeID.HSQL_INTERVAL_DAY_TO_HOUR, TypeID.DERBY_VARCHAR},
       {TypeID.HSQL_INTERVAL_MINUTE_TO_SECOND, TypeID.H2_VARCHAR, TypeID.HSQL_INTERVAL_MINUTE_TO_SECOND, TypeID.DERBY_VARCHAR},
       {TypeID.HSQL_INTERVAL_SECOND, TypeID.H2_VARCHAR, TypeID.HSQL_INTERVAL_SECOND, TypeID.DERBY_VARCHAR}};
   
   private static final int[][] DERBY_TYPES = {
       {TypeID.DERBY_IDENTITY, TypeID.H2_IDENTITY, TypeID.HSQL_IDENTITY, TypeID.DERBY_IDENTITY},
       {TypeID.DERBY_CHAR, TypeID.H2_CHAR, TypeID.HSQL_CHAR, TypeID.DERBY_CHAR},
       {TypeID.DERBY_CHAR_FOR_BIT_DATA, TypeID.H2_VARCHAR, TypeID.HSQL_BIT_VARYING, TypeID.DERBY_CHAR_FOR_BIT_DATA},
       {TypeID.DERBY_VARCHAR, TypeID.H2_VARCHAR, TypeID.HSQL_VARCHAR, TypeID.DERBY_VARCHAR},
       {TypeID.DERBY_VARCHAR_FOR_BIT_DATA, TypeID.H2_BINARY, TypeID.HSQL_VARBINARY, TypeID.DERBY_VARCHAR_FOR_BIT_DATA},
       {TypeID.DERBY_BLOB, TypeID.H2_BLOB, TypeID.HSQL_BLOB, TypeID.DERBY_BLOB},
       {TypeID.DERBY_LONG_VARCHAR, TypeID.H2_LONGVARCHAR, TypeID.HSQL_LONGVARCHAR, TypeID.DERBY_LONG_VARCHAR},
       {TypeID.DERBY_LONG_VARCHAR_FOR_BIT_DATA, TypeID.H2_BINARY, TypeID.HSQL_VARBINARY, TypeID.DERBY_LONG_VARCHAR_FOR_BIT_DATA},
       {TypeID.DERBY_CLOB, TypeID.H2_CLOB, TypeID.HSQL_CLOB, TypeID.DERBY_CLOB},
       {TypeID.DERBY_BOOLEAN, TypeID.H2_BOOLEAN, TypeID.HSQL_BOOLEAN, TypeID.DERBY_BOOLEAN},
       {TypeID.DERBY_SMALLINT, TypeID.H2_SMALLINT, TypeID.HSQL_SMALLINT, TypeID.DERBY_SMALLINT},
       {TypeID.DERBY_INTEGER, TypeID.H2_INTEGER, TypeID.HSQL_INTEGER, TypeID.DERBY_INTEGER},
       {TypeID.DERBY_BIGINT, TypeID.H2_BIGINT, TypeID.HSQL_BIGINT, TypeID.DERBY_BIGINT},
       {TypeID.DERBY_FLOAT, TypeID.H2_DOUBLE, TypeID.HSQL_FLOAT, TypeID.DERBY_FLOAT},
       {TypeID.DERBY_REAL, TypeID.H2_REAL, TypeID.HSQL_REAL, TypeID.DERBY_REAL},
       {TypeID.DERBY_DOUBLE, TypeID.H2_DOUBLE, TypeID.HSQL_DOUBLE, TypeID.DERBY_DOUBLE},
       {TypeID.DERBY_DECIMAL, TypeID.H2_DECIMAL, TypeID.HSQL_DECIMAL, TypeID.DERBY_DECIMAL},
       {TypeID.DERBY_DATE, TypeID.H2_DATE, TypeID.HSQL_DATE, TypeID.DERBY_DATE},
       {TypeID.DERBY_TIME, TypeID.H2_TIME, TypeID.HSQL_TIME, TypeID.DERBY_TIME},
       {TypeID.DERBY_TIMESTAMP, TypeID.H2_TIMESTAMP, TypeID.HSQL_TIMESTAMP, TypeID.DERBY_TIMESTAMP}};
    
   private static final int[][] POSTGRESQL_TYPES = {
       {TypeID.POSTGRESQL_SERIAL, TypeID.H2_IDENTITY, TypeID.HSQL_IDENTITY, TypeID.DERBY_BIGINT},
       {TypeID.POSTGRESQL_BIGSERIAL, TypeID.H2_IDENTITY, TypeID.HSQL_IDENTITY, TypeID.DERBY_BIGINT},
       {TypeID.POSTGRESQL_INT2, TypeID.H2_SMALLINT, TypeID.HSQL_SMALLINT, TypeID.DERBY_SMALLINT},
       {TypeID.POSTGRESQL_INT4, TypeID.H2_INTEGER, TypeID.HSQL_INTEGER, TypeID.DERBY_INTEGER},
       {TypeID.POSTGRESQL_OID, TypeID.H2_BIGINT, TypeID.HSQL_BIGINT, TypeID.DERBY_BIGINT},
       {TypeID.POSTGRESQL_INT8, TypeID.H2_BIGINT, TypeID.HSQL_BIGINT, TypeID.DERBY_BIGINT},
       {TypeID.POSTGRESQL_MONEY, TypeID.H2_DOUBLE, TypeID.HSQL_DOUBLE, TypeID.DERBY_DOUBLE},
       {TypeID.POSTGRESQL_NUMERIC, TypeID.H2_DECIMAL, TypeID.HSQL_NUMERIC, TypeID.DERBY_DECIMAL},
       {TypeID.POSTGRESQL_FLOAT4, TypeID.H2_REAL, TypeID.HSQL_FLOAT, TypeID.DERBY_REAL},
       {TypeID.POSTGRESQL_FLOAT8, TypeID.H2_DOUBLE, TypeID.HSQL_DOUBLE, TypeID.DERBY_DOUBLE},
       {TypeID.POSTGRESQL_CHAR, TypeID.H2_CHAR, TypeID.HSQL_CHAR, TypeID.DERBY_CHAR},
       {TypeID.POSTGRESQL_BPCHAR, TypeID.H2_CHAR, TypeID.HSQL_CHAR, TypeID.DERBY_CHAR},
       {TypeID.POSTGRESQL_VARCHAR, TypeID.H2_VARCHAR, TypeID.HSQL_VARCHAR, TypeID.DERBY_VARCHAR},
       {TypeID.POSTGRESQL_TEXT, TypeID.H2_CLOB, TypeID.HSQL_CLOB, TypeID.DERBY_CLOB},
       {TypeID.POSTGRESQL_NAME, TypeID.H2_VARCHAR, TypeID.HSQL_VARCHAR, TypeID.DERBY_VARCHAR},
       {TypeID.POSTGRESQL_BYTEA, TypeID.H2_BLOB, TypeID.HSQL_BLOB, TypeID.DERBY_BLOB},
       {TypeID.POSTGRESQL_BOOL, TypeID.H2_BOOLEAN, TypeID.HSQL_BOOLEAN, TypeID.DERBY_BOOLEAN},
       {TypeID.POSTGRESQL_BIT, TypeID.H2_BOOLEAN, TypeID.HSQL_BIT, TypeID.DERBY_BOOLEAN},
       {TypeID.POSTGRESQL_VARBIT, TypeID.H2_VARCHAR, TypeID.HSQL_BIT_VARYING, TypeID.DERBY_CHAR_FOR_BIT_DATA},
       {TypeID.POSTGRESQL_DATE, TypeID.H2_DATE, TypeID.HSQL_DATE, TypeID.DERBY_DATE},
       {TypeID.POSTGRESQL_TIME, TypeID.H2_TIME, TypeID.HSQL_TIME, TypeID.DERBY_TIME},
       {TypeID.POSTGRESQL_TIMETZ, TypeID.H2_TIME, TypeID.HSQL_TIME_WITH_TIME_ZONE, TypeID.DERBY_TIME},
       {TypeID.POSTGRESQL_TIMESTAMP, TypeID.H2_TIMESTAMP, TypeID.HSQL_TIMESTAMP, TypeID.DERBY_TIMESTAMP},
       {TypeID.POSTGRESQL_TIMESTAMPTZ, TypeID.H2_TIMESTAMP, TypeID.HSQL_TIMESTAMP_WITH_TIME_ZONE, TypeID.DERBY_TIMESTAMP},
       {TypeID.POSTGRESQL_INTERVAL, TypeID.H2_VARCHAR, TypeID.HSQL_INTERVAL_YEAR, TypeID.DERBY_VARCHAR},
       {TypeID.POSTGRESQL_CIDR, TypeID.H2_LONGVARCHAR, TypeID.HSQL_LONGVARCHAR, TypeID.DERBY_LONG_VARCHAR},
       {TypeID.POSTGRESQL_INET, TypeID.H2_LONGVARCHAR, TypeID.HSQL_LONGVARCHAR, TypeID.DERBY_LONG_VARCHAR},
       {TypeID.POSTGRESQL_MACADDR, TypeID.H2_LONGVARCHAR, TypeID.HSQL_LONGVARCHAR, TypeID.DERBY_LONG_VARCHAR},
       {TypeID.POSTGRESQL_POINT, TypeID.H2_LONGVARCHAR, TypeID.HSQL_LONGVARCHAR, TypeID.DERBY_LONG_VARCHAR},
       {TypeID.POSTGRESQL_LSEG, TypeID.H2_LONGVARCHAR, TypeID.HSQL_LONGVARCHAR, TypeID.DERBY_LONG_VARCHAR},
       {TypeID.POSTGRESQL_BOX, TypeID.H2_LONGVARCHAR, TypeID.HSQL_LONGVARCHAR, TypeID.DERBY_LONG_VARCHAR},
       {TypeID.POSTGRESQL_PATH, TypeID.H2_LONGVARCHAR, TypeID.HSQL_LONGVARCHAR, TypeID.DERBY_LONG_VARCHAR},
       {TypeID.POSTGRESQL_POLYGON, TypeID.H2_LONGVARCHAR, TypeID.HSQL_LONGVARCHAR, TypeID.DERBY_LONG_VARCHAR},
       {TypeID.POSTGRESQL_CIRCLE, TypeID.H2_LONGVARCHAR, TypeID.HSQL_LONGVARCHAR, TypeID.DERBY_LONG_VARCHAR},
       {TypeID.POSTGRESQL_ARRAYS, TypeID.H2_ARRAY, TypeID.HSQL_LONGVARCHAR, TypeID.DERBY_LONG_VARCHAR}};
   
   private static final int[][] MYSQL_TYPES = {
       {TypeID.MYSQL_CHAR, TypeID.H2_CHAR, TypeID.HSQL_CHAR, TypeID.DERBY_CHAR},
       {TypeID.MYSQL_VARCHAR, TypeID.H2_VARCHAR, TypeID.HSQL_VARCHAR, TypeID.DERBY_VARCHAR},
       {TypeID.MYSQL_TINYBLOB, TypeID.H2_BLOB, TypeID.HSQL_VARBINARY, TypeID.DERBY_VARCHAR_FOR_BIT_DATA},
       {TypeID.MYSQL_BLOB, TypeID.H2_BLOB, TypeID.HSQL_BLOB, TypeID.DERBY_BLOB},
       {TypeID.MYSQL_MEDIUMBLOB, TypeID.H2_BLOB, TypeID.HSQL_BLOB, TypeID.DERBY_BLOB},
       {TypeID.MYSQL_LONGBLOB, TypeID.H2_BLOB, TypeID.HSQL_BLOB, TypeID.DERBY_BLOB},
       {TypeID.MYSQL_TINYINT, TypeID.H2_TINYINT, TypeID.HSQL_TINYINT, TypeID.DERBY_SMALLINT},
       {TypeID.MYSQL_SMALLINT_UNSIGNED, TypeID.H2_TINYINT, TypeID.HSQL_TINYINT, TypeID.DERBY_SMALLINT},
       {TypeID.MYSQL_BIT, TypeID.H2_BOOLEAN, TypeID.HSQL_BIT, TypeID.DERBY_BOOLEAN},
       {TypeID.MYSQL_SMALLINT, TypeID.H2_SMALLINT, TypeID.HSQL_SMALLINT, TypeID.DERBY_SMALLINT},
       {TypeID.MYSQL_SMALLINT_UNSIGNED, TypeID.H2_SMALLINT, TypeID.HSQL_SMALLINT, TypeID.DERBY_SMALLINT},
       {TypeID.MYSQL_MEDIUMINT, TypeID.H2_INTEGER, TypeID.HSQL_INTEGER, TypeID.DERBY_INTEGER},
       {TypeID.MYSQL_MEDIUMINT_UNSIGNED, TypeID.H2_INTEGER, TypeID.HSQL_INTEGER, TypeID.DERBY_INTEGER},
       {TypeID.MYSQL_INT, TypeID.H2_INTEGER, TypeID.HSQL_INTEGER, TypeID.DERBY_INTEGER},
       {TypeID.MYSQL_INT_UNSIGNED, TypeID.H2_INTEGER, TypeID.HSQL_INTEGER, TypeID.DERBY_INTEGER},
       {TypeID.MYSQL_BIGINT, TypeID.H2_BIGINT, TypeID.HSQL_BIGINT, TypeID.DERBY_BIGINT},
       {TypeID.MYSQL_BIGINT_UNSIGNED, TypeID.H2_BIGINT, TypeID.HSQL_BIGINT, TypeID.DERBY_BIGINT},
       {TypeID.MYSQL_FLOAT, TypeID.H2_REAL, TypeID.HSQL_FLOAT, TypeID.DERBY_REAL},
       {TypeID.MYSQL_FLOAT_UNSIGNED, TypeID.H2_REAL, TypeID.HSQL_FLOAT, TypeID.DERBY_REAL},
       {TypeID.MYSQL_DOUBLE, TypeID.H2_DOUBLE, TypeID.HSQL_DOUBLE, TypeID.DERBY_DOUBLE},
       {TypeID.MYSQL_DOUBLE_UNSIGNED, TypeID.H2_DOUBLE, TypeID.HSQL_DOUBLE, TypeID.DERBY_DOUBLE},
       {TypeID.MYSQL_DECIMAL, TypeID.H2_DECIMAL, TypeID.HSQL_DECIMAL, TypeID.DERBY_DECIMAL},
       {TypeID.MYSQL_DATE, TypeID.H2_DATE, TypeID.HSQL_DATE, TypeID.DERBY_DATE},
       {TypeID.MYSQL_TIME, TypeID.H2_TIME, TypeID.HSQL_TIME, TypeID.DERBY_TIME},
       {TypeID.MYSQL_DATETIME, TypeID.H2_TIMESTAMP, TypeID.HSQL_DATETIME, TypeID.DERBY_TIMESTAMP},
       {TypeID.MYSQL_TIMESTAMP, TypeID.H2_TIMESTAMP, TypeID.HSQL_TIMESTAMP, TypeID.DERBY_TIMESTAMP},
       {TypeID.MYSQL_YEAR, TypeID.H2_DATE, TypeID.HSQL_DATE, TypeID.DERBY_DATE}};
   
   private static final int[][] ORACLE_TYPES = {
       {TypeID.ORACLE_CHAR, TypeID.H2_CHAR, TypeID.HSQL_CHAR, TypeID.DERBY_CHAR},
       {TypeID.ORACLE_VARCHAR2, TypeID.H2_VARCHAR, TypeID.HSQL_VARCHAR, TypeID.DERBY_VARCHAR},
       {TypeID.ORACLE_LONG, TypeID.H2_LONGVARCHAR, TypeID.HSQL_LONGVARCHAR, TypeID.DERBY_LONG_VARCHAR},
       {TypeID.ORACLE_RAW, TypeID.H2_BLOB, TypeID.HSQL_VARBINARY, TypeID.DERBY_VARCHAR_FOR_BIT_DATA},
       {TypeID.ORACLE_BLOB, TypeID.H2_BLOB, TypeID.HSQL_BLOB, TypeID.DERBY_BLOB},
       {TypeID.ORACLE_CLOB, TypeID.H2_CLOB, TypeID.HSQL_CLOB, TypeID.DERBY_CLOB},
       {TypeID.ORACLE_BFILE, TypeID.H2_OTHER, TypeID.HSQL_BLOB, TypeID.DERBY_BLOB},
       {TypeID.ORACLE_NUMBER, TypeID.H2_DECIMAL, TypeID.HSQL_NUMERIC, TypeID.DERBY_DECIMAL},
       {TypeID.ORACLE_BINARY_FLOAT, TypeID.H2_OTHER, TypeID.HSQL_BLOB, TypeID.DERBY_BLOB},
       {TypeID.ORACLE_BINARY_DOUBLE, TypeID.H2_OTHER, TypeID.HSQL_BLOB, TypeID.DERBY_BLOB},
       {TypeID.ORACLE_DATE, TypeID.H2_DATE, TypeID.HSQL_DATE, TypeID.DERBY_DATE},
       {TypeID.ORACLE_TIMESTAMP, TypeID.H2_TIMESTAMP, TypeID.HSQL_TIMESTAMP, TypeID.DERBY_TIMESTAMP},
       {TypeID.ORACLE_TIMESTAMPTZ, TypeID.H2_TIMESTAMP, TypeID.HSQL_TIMESTAMP_WITH_TIME_ZONE, TypeID.DERBY_TIMESTAMP},
       {TypeID.ORACLE_INTERVALYM, TypeID.H2_VARCHAR, TypeID.HSQL_INTERVAL_YEAR_TO_MONTH, TypeID.DERBY_VARCHAR},
       {TypeID.ORACLE_INTERVALDS, TypeID.H2_VARCHAR, TypeID.HSQL_VARCHAR, TypeID.DERBY_VARCHAR}};
   
   private static final int[][] SQLITE_TYPES = {
       {TypeID.SQLITE_INTEGER, TypeID.H2_INTEGER, TypeID.HSQL_INTEGER, TypeID.DERBY_INTEGER},
       {TypeID.SQLITE_REAL, TypeID.H2_REAL, TypeID.HSQL_REAL, TypeID.DERBY_REAL},
       {TypeID.SQLITE_TEXT, TypeID.H2_VARCHAR, TypeID.HSQL_LONGVARCHAR, TypeID.DERBY_LONG_VARCHAR},
       {TypeID.SQLITE_BLOB, TypeID.H2_BLOB, TypeID.HSQL_BLOB, TypeID.DERBY_BLOB}};
   
   private static final int[][] MSACCESS_TYPES = {
       {TypeID.MSACCESS_COUNTER, TypeID.H2_IDENTITY, TypeID.HSQL_IDENTITY, TypeID.DERBY_IDENTITY},
       {TypeID.MSACCESS_BINARY, TypeID.H2_BINARY, TypeID.HSQL_BINARY, TypeID.DERBY_BLOB},
       {TypeID.MSACCESS_LONGBINARY, TypeID.H2_BLOB, TypeID.HSQL_VARBINARY, TypeID.DERBY_LONG_VARCHAR_FOR_BIT_DATA},
       {TypeID.MSACCESS_VARCHAR, TypeID.H2_VARCHAR, TypeID.HSQL_VARCHAR, TypeID.DERBY_VARCHAR},
       {TypeID.MSACCESS_LONGCHAR, TypeID.H2_LONGVARCHAR, TypeID.HSQL_LONGVARCHAR, TypeID.DERBY_LONG_VARCHAR},
       {TypeID.MSACCESS_BIT, TypeID.H2_BOOLEAN, TypeID.HSQL_BOOLEAN, TypeID.DERBY_BOOLEAN},
       {TypeID.MSACCESS_BYTE, TypeID.H2_TINYINT, TypeID.HSQL_TINYINT, TypeID.DERBY_SMALLINT},
       {TypeID.MSACCESS_SMALLINT, TypeID.H2_SMALLINT, TypeID.HSQL_SMALLINT, TypeID.DERBY_SMALLINT},
       {TypeID.MSACCESS_INTEGER, TypeID.H2_INTEGER, TypeID.HSQL_INTEGER, TypeID.DERBY_INTEGER},
       {TypeID.MSACCESS_REAL, TypeID.H2_REAL, TypeID.HSQL_REAL, TypeID.DERBY_REAL},
       {TypeID.MSACCESS_DOUBLE, TypeID.H2_DOUBLE, TypeID.HSQL_DOUBLE, TypeID.DERBY_DOUBLE},
       {TypeID.MSACCESS_CURRENCY, TypeID.H2_DECIMAL, TypeID.HSQL_NUMERIC, TypeID.DERBY_DECIMAL},
       {TypeID.MSACCESS_GUID, TypeID.H2_OTHER, TypeID.HSQL_BLOB, TypeID.DERBY_BLOB},
       {TypeID.MSACCESS_DATETIME, TypeID.H2_TIMESTAMP, TypeID.HSQL_TIMESTAMP, TypeID.DERBY_TIMESTAMP}};
   
   //==============================================================
   // TypesInfoCache Constructors
   //==============================================================
   
   public TypesInfoCache()
   {
      this(ConnectionManager.getDataSourceType());
   }
   
   public TypesInfoCache(String dataSourceType)
   {
      this(dataSourceType, DEFAULT_DATASINK_TYPE);
   }
   
   public TypesInfoCache(String dataSourceType, String dataSinkType)
   {
      this.dataSourceType = dataSourceType;
      this.dataSinkType = dataSinkType;
      
      nameToType = new HashMap<String, Integer>();
      
      if (dataSourceType.equals(ConnectionManager.H2))
         addSourceSinkType(H2_TYPES);
      else if (dataSourceType.indexOf(ConnectionManager.HSQL) != -1)
         addSourceSinkType(HSQL_TYPES);
      else if (dataSourceType.indexOf(ConnectionManager.DERBY) != -1)
         addSourceSinkType(DERBY_TYPES);
      else if (dataSourceType.equals(ConnectionManager.POSTGRESQL))
         addSourceSinkType(POSTGRESQL_TYPES);
      else if (dataSourceType.equals(ConnectionManager.MYSQL))
         addSourceSinkType(MYSQL_TYPES);
      else if (dataSourceType.equals(ConnectionManager.ORACLE))
         addSourceSinkType(ORACLE_TYPES);
      else if (dataSourceType.equals(ConnectionManager.SQLITE))
         addSourceSinkType(SQLITE_TYPES);
      else if (dataSourceType.equals(ConnectionManager.MSACCESS))
         addSourceSinkType(MSACCESS_TYPES);
      
      // Source not found, all will become UNSPECIFIED.
   }
   
   //==============================================================
   // Class method to create the appropriate data source type
   // relationship to data sink type.
   //==============================================================
   
   private void addSourceSinkType(int[][] types)
   {
      // Method Instances
      String sourceTypeName;
      Integer sink_id;
      
      for (int i = 0; i < types.length; i++)
      {
         sourceTypeName = TypeID.toString(types[i][TYPE_NAME]);
         // System.out.println("TypesInfoCache addSourceSinkType() " + sourceTypeName);
         
         if (dataSinkType.equals(ConnectionManager.H2))
            sink_id = Integer.valueOf(types[i][H2_TYPE]);
         else if (dataSinkType.equals(ConnectionManager.DERBY))
            sink_id = Integer.valueOf(types[i][DERBY_TYPE]);
         // Not sure so default.
         else
            sink_id = Integer.valueOf(types[i][HSQL_TYPE]);
         
         nameToType.put(sourceTypeName, sink_id);
      }
   }
   
   //==============================================================
   // Class method to obtain the appropriate sink data type as
   // related to the given source data type.
   //==============================================================
   
   public String getType(String sourceTypeName)
   {
      // System.out.println("TypesInfoCache getType() " + sourceTypeName);
      
      // Deal with PostgreSQL Array Types
      if (dataSourceType.equals(ConnectionManager.POSTGRESQL) && sourceTypeName.startsWith("_"))
         sourceTypeName = TypeID.toString(TypeID.POSTGRESQL_ARRAYS);
      
      // System.out.println(sourceTypeName);
      
      if (nameToType.containsKey(sourceTypeName))
         return TypeID.toString(nameToType.get(sourceTypeName));
      else
         return TypeID.toString(TypeID._UNSPECIFIED);
   } 
}