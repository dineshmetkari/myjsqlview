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
// Version 1.3 10/03/2013
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
 * @version 1.3 10/03/2013
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
   
   private static final int[][] H2_TYPES = {
       {TypeID.H2_IDENTITY, TypeID.H2_IDENTITY, TypeID.HSQL_IDENTITY, TypeID.DERBY_IDENTITY},
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
   
   private static final int[][] POSTGRESQL_TYPES = {
       {TypeID.POSTGRESQL_SERIAL, TypeID.H2_IDENTITY, TypeID.HSQL_IDENTITY, TypeID.DERBY_IDENTITY},
       {TypeID.POSTGRESQL_BIGSERIAL, TypeID.H2_IDENTITY, TypeID.HSQL_IDENTITY, TypeID.DERBY_IDENTITY},
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
       {TypeID.POSTGRESQL_TEXT, TypeID.H2_VARCHAR, TypeID.HSQL_CLOB, TypeID.DERBY_VARCHAR},
       {TypeID.POSTGRESQL_NAME, TypeID.H2_VARCHAR, TypeID.HSQL_VARCHAR, TypeID.DERBY_VARCHAR},
       {TypeID.POSTGRESQL_BYTEA, TypeID.H2_BLOB, TypeID.HSQL_BLOB, TypeID.DERBY_BLOB},
       {TypeID.POSTGRESQL_BOOL, TypeID.H2_BOOLEAN, TypeID.HSQL_BOOLEAN, TypeID.DERBY_BOOLEAN},
       {TypeID.POSTGRESQL_BIT, TypeID.H2_BOOLEAN, TypeID.HSQL_BIT, TypeID.DERBY_BOOLEAN},
       {TypeID.POSTGRESQL_VARBIT, TypeID.H2_OTHER, TypeID.HSQL_BIT_VARYING, TypeID.DERBY_CHAR_FOR_BIT_DATA},
       {TypeID.POSTGRESQL_DATE, TypeID.H2_DATE, TypeID.HSQL_DATE, TypeID.DERBY_DATE},
       {TypeID.POSTGRESQL_TIME, TypeID.H2_TIME, TypeID.HSQL_TIME, TypeID.DERBY_TIME},
       {TypeID.POSTGRESQL_TIMETZ, TypeID.H2_TIME, TypeID.HSQL_TIME_WITH_TIME_ZONE, TypeID.DERBY_TIME},
       {TypeID.POSTGRESQL_TIMESTAMP, TypeID.H2_TIMESTAMP, TypeID.HSQL_TIMESTAMP, TypeID.DERBY_TIMESTAMP},
       {TypeID.POSTGRESQL_TIMESTAMPTZ, TypeID.H2_TIMESTAMP, TypeID.HSQL_TIMESTAMP_WITH_TIME_ZONE, TypeID.DERBY_TIMESTAMP},
       {TypeID.POSTGRESQL_INTERVAL, TypeID.H2_VARCHAR, TypeID.HSQL_INTERVAL, TypeID.DERBY_VARCHAR},
       {TypeID.POSTGRESQL_CIDR, TypeID.H2_VARCHAR, TypeID.HSQL_VARCHAR, TypeID.DERBY_VARCHAR},
       {TypeID.POSTGRESQL_INET, TypeID.H2_VARCHAR, TypeID.HSQL_VARCHAR, TypeID.DERBY_VARCHAR},
       {TypeID.POSTGRESQL_MACADDR, TypeID.H2_VARCHAR, TypeID.HSQL_VARCHAR, TypeID.DERBY_VARCHAR},
       {TypeID.POSTGRESQL_POINT, TypeID.H2_VARCHAR, TypeID.HSQL_VARCHAR, TypeID.DERBY_VARCHAR},
       {TypeID.POSTGRESQL_LSEG, TypeID.H2_VARCHAR, TypeID.HSQL_VARCHAR, TypeID.DERBY_VARCHAR},
       {TypeID.POSTGRESQL_BOX, TypeID.H2_VARCHAR, TypeID.HSQL_VARCHAR, TypeID.DERBY_VARCHAR},
       {TypeID.POSTGRESQL_PATH, TypeID.H2_VARCHAR, TypeID.HSQL_VARCHAR, TypeID.DERBY_VARCHAR},
       {TypeID.POSTGRESQL_POLYGON, TypeID.H2_VARCHAR, TypeID.HSQL_VARCHAR, TypeID.DERBY_VARCHAR},
       {TypeID.POSTGRESQL_CIRCLE, TypeID.H2_VARCHAR, TypeID.HSQL_VARCHAR, TypeID.DERBY_VARCHAR},
       {TypeID.POSTGRESQL_ARRAYS, TypeID.H2_ARRAY, TypeID.HSQL_LONGVARCHAR, TypeID.DERBY_LONG_VARCHAR}};
   
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
      
      if (dataSourceType.equals(ConnectionManager.POSTGRESQL))
         addSourceSinkType(POSTGRESQL_TYPES);
      else if (dataSourceType.equals(ConnectionManager.H2))
         addSourceSinkType(H2_TYPES);
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
      
      System.out.println(sourceTypeName);
      
      if (nameToType.containsKey(sourceTypeName))
         return TypeID.toString(nameToType.get(sourceTypeName));
      else
         return TypeID.toString(TypeID._UNSPECIFIED);
   } 
}