//=================================================================
//                        MyJSQLView TypeID
//=================================================================
//
//    This class provides an identification definition for data
// types that follows a prescribe naming scheme.
//
//                        << TypeID.java >>
//
//=================================================================
// Copyright (C) 2005-2013 Dana M. Proctor
// Version 1.1 09/30/2013
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
// Version 1.0 Original Initial TypeID Class, Concept from PostgreSQL JDBC
//             Class OID.
//         1.1 Correction of Class Instance HSQL_BITVARYING to HSQL_BIT_VARYING.
//             Correction in toString() to Replace Additional Underscore Characters
//             to Space Instead of Removal.
//        
//-----------------------------------------------------------------
//                 danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview.datasource;

import java.lang.reflect.Field;

/**
 *    The TypeID class provides an identification definition for data
 * types that follows a prescribe naming scheme.
 * 
 * @author Dana M. Proctor
 * @version 1.1 09/30/2013
 */

public class TypeID
{
   // Default Unknown Type.
   public static final int _UNSPECIFIED = 0;
   
   // H2 Data Type IDs
   public static final int H2_IDENTITY = 5;
   public static final int H2_CHAR = 10;
   public static final int H2_VARCHAR = 15;
   public static final int H2_VARCHAR_IGNORECASE = 20;
   public static final int H2_BINARY = 25;
   public static final int H2_BLOB = 30;
   public static final int H2_CLOB = 35;
   public static final int H2_OTHER = 40;
   public static final int H2_UUID = 45;
   public static final int H2_BOOLEAN = 50;
   public static final int H2_TINYINT = 55;
   public static final int H2_SMALLINT = 60;
   public static final int H2_INTEGER = 65;
   public static final int H2_BIGINT = 70;
   public static final int H2_REAL = 75;
   public static final int H2_DOUBLE = 80;
   public static final int H2_DECIMAL = 85;
   public static final int H2_DATE = 90;
   public static final int H2_TIME = 95;
   public static final int H2_TIMESTAMP = 100;
   public static final int H2_ARRAY = 105;
   
   // HSQL 2.0 Data Type IDs
   public static final int HSQL_IDENTITY = 205;
   public static final int HSQL_CHAR = 210;
   public static final int HSQL_VARCHAR = 215;
   public static final int HSQL_LONGVARCHAR = 220;
   public static final int HSQL_CLOB = 225;
   public static final int HSQL_BINARY = 230;
   public static final int HSQL_VARBINARY = 235;
   public static final int HSQL_LONGVARBINARY = 240;
   public static final int HSQL_BLOB = 245;
   public static final int HSQL_TINYINT = 250;
   public static final int HSQL_SMALLINT = 255;
   public static final int HSQL_INTEGER = 260;
   public static final int HSQL_BIGINT = 265;
   public static final int HSQL_FLOAT = 270;
   public static final int HSQL_DOUBLE = 275;
   public static final int HSQL_REAL = 280;
   public static final int HSQL_DECIMAL = 285;
   public static final int HSQL_NUMERIC = 290;
   public static final int HSQL_BOOLEAN = 300;
   public static final int HSQL_BIT = 305;
   public static final int HSQL_BIT_VARYING = 310;
   public static final int HSQL_DATE = 315;
   public static final int HSQL_TIME = 320;
   public static final int HSQL_TIMETMZ = 325;
   public static final int HSQL_DATETIME = 330;
   public static final int HSQL_TIMESTAMP = 335;
   public static final int HSQL_TIMESTAMPTMZ = 340;
   public static final int HSQL_INTERVAL = 345;

   // Derby Data Type IDs
   public static final int DERBY_IDENTITY = 400;
   public static final int DERBY_CHAR = 405;
   public static final int DERBY_CHARBIT = 410;
   public static final int DERBY_VARCHAR = 415;
   public static final int DERBY_VARCHARBIT = 420;
   public static final int DERBY_BLOB = 425;
   public static final int DERBY_LONGVARCHAR = 430;
   public static final int DERBY_LONGVARCHARBIT = 435;
   public static final int DERBY_CLOB = 440;
   public static final int DERBY_BOOLEAN = 445;
   public static final int DERBY_SMALLINT = 450;
   public static final int DERBY_INTEGER = 455;
   public static final int DERBY_BIGINT = 460;
   public static final int DERBY_FLOAT = 465;
   public static final int DERBY_REAL = 470;
   public static final int DERBY_DOUBLE = 475;
   public static final int DERBY_DECIMAL = 480;
   public static final int DERBY_DATE = 485;
   public static final int DERBY_TIME = 490;
   public static final int DERBY_TIMESTAMP = 495;
   
   // PostgreSQL Data Type IDs
   public static final int POSTGRESQL_SERIAL = 605;
   public static final int POSTGRESQL_BIGSERIAL = 610;
   public static final int POSTGRESQL_INT2 = 615;
   public static final int POSTGRESQL_INT4 = 620;
   public static final int POSTGRESQL_OID = 625;
   public static final int POSTGRESQL_INT8 = 630;
   public static final int POSTGRESQL_MONEY = 635;
   public static final int POSTGRESQL_NUMERIC = 640;
   public static final int POSTGRESQL_FLOAT4 = 645;
   public static final int POSTGRESQL_FLOAT8 = 650;
   public static final int POSTGRESQL_CHAR = 655;
   public static final int POSTGRESQL_BPCHAR = 660;
   public static final int POSTGRESQL_VARCHAR = 665;
   public static final int POSTGRESQL_TEXT = 670;
   public static final int POSTGRESQL_NAME = 675;
   public static final int POSTGRESQL_BYTEA = 680;
   public static final int POSTGRESQL_BOOL = 685;
   public static final int POSTGRESQL_BIT = 690;
   public static final int POSTGRESQL_VARBIT = 695;
   public static final int POSTGRESQL_DATE = 700;
   public static final int POSTGRESQL_TIME = 705;
   public static final int POSTGRESQL_TIMETZ = 710;
   public static final int POSTGRESQL_TIMESTAMP = 715;
   public static final int POSTGRESQL_TIMESTAMPTZ = 720;
   public static final int POSTGRESQL_INTERVAL = 725;
   public static final int POSTGRESQL_CIDR = 730;
   public static final int POSTGRESQL_INET = 735;
   public static final int POSTGRESQL_MACADDR = 740;
   public static final int POSTGRESQL_POINT = 745;
   public static final int POSTGRESQL_LSEG = 750;
   public static final int POSTGRESQL_BOX = 755;
   public static final int POSTGRESQL_PATH = 780;
   public static final int POSTGRESQL_POLYGON = 785;
   public static final int POSTGRESQL_CIRCLE = 790;
   public static final int POSTGRESQL__ = 795;
   
   // MySQL Data Type IDs
   public static final int MYSQL_CHAR = 900;
   public static final int MYSQL_VARCHAR = 905;
   public static final int MYSQL_TINYBLOB = 910;
   public static final int MYSQL_BLOB = 915;
   public static final int MYSQL_MEDIUMBLOB = 920;
   public static final int MYSQL_LONGBLOB = 925;
   public static final int MYSQL_TINYINT = 930;
   public static final int MYSQL_TINYINT_UNSIGNED = 935;
   public static final int MYSQL_BIT = 940;
   public static final int MYSQL_SMALLINT = 945;
   public static final int MYSQL_SMALLINT_UNSIGNED = 950;
   public static final int MYSQL_MEDIUMINT = 955;
   public static final int MYSQL_MEDIUMINT_UNSIGNED = 960;
   public static final int MYSQL_INT = 965;
   public static final int MYSQL_INT_UNSIGNED = 970;
   public static final int MYSQL_BIGINT = 975;
   public static final int MYSQL_BIGINT_UNSIGNED = 980;
   public static final int MYSQL_FLOAT = 985;
   public static final int MYSQL_FLOAT_UNSIGNED = 990;
   public static final int MYSQL_DOUBLE = 995;
   public static final int MYSQL_DOUBLE_UNSIGNED = 1000;
   public static final int MYSQL_DECIMAL = 1005;
   public static final int MYSQL_DATE = 1010;
   public static final int MYSQL_TIME = 1015;
   public static final int MYSQL_DATETIME = 1020;
   public static final int MYSQL_TIMESTAMP = 1025;    
   public static final int MYSQL_YEAR = 1030;
   
   // Oracle Data Type IDs
   public static final int ORACLE_CHAR = 1100;
   public static final int ORACLE_VARCHAR2 = 1105;
   public static final int ORACLE_LONG = 1110;
   public static final int ORACLE_RAW = 1115;
   public static final int ORACLE_BLOB = 1120;
   public static final int ORACLE_CLOB = 1125;
   public static final int ORACLE_BFILE = 1130;
   public static final int ORACLE_NUMBER = 1135;
   public static final int ORACLE_BINARY_FLOAT = 1140;
   public static final int ORACLE_BINARY_DOUBLE = 1145;
   public static final int ORACLE_DATE = 1150;
   public static final int ORACLE_TIMESTAMP = 1155;
   public static final int ORACLE_TIMESTAMPTZ = 1160;
   public static final int ORACLE_INTERVALYM = 1165;
   public static final int ORACLE_INTERVALDS = 1170;
   
   // SQLite Data Type IDs
   public static final int SQLITE_INTEGER = 1200;
   public static final int SQLITE_REAL = 1205;
   public static final int SQLITE_TEXT = 1210;
   public static final int SQLITE_BLOB = 1215;
   
   // SQLite Data Type IDs
   public static final int MSACCESS_COUNTER = 1300;
   public static final int MSACCESS_BINARY = 1305;
   public static final int MSACCESS_LONGBINARY = 1310;
   public static final int MSACCESS_VARCHAR = 1315;
   public static final int MSACCESS_LONGCHAR = 1320;
   public static final int MSACCESS_BIT = 1325;
   public static final int MSACCESS_BYTE = 1330;
   public static final int MSACCESS_SMALLINT = 1335;
   public static final int MSACCESS_INTEGER = 1340;
   public static final int MSACCESS_REAL = 1345;
   public static final int MSACCESS_DOUBLE = 1350;
   public static final int MSACCESS_CURRENCY = 1355;
   public static final int MSACCESS_GUID = 1360;
   public static final int MSACCESS_DATETIME = 1365;
   
   //==============================================================
   // Class method to allow classes to try and obtain the field
   // name with the given TypeID value.
   //
   // ** NOTE **
   // All the fields should adhere to the prescribe naming scheme
   // of having the DB name followed by an underscore and then the
   // data type.
   //==============================================================
    
   public static String toString(int typeid)
   {
      try
      {
         Field[] fields = TypeID.class.getFields();
         
         for (int i = 0; i < fields.length; ++i)
         {
            if (fields[i].getInt(null) == typeid)
            {
               if (fields[i].getName().indexOf("_") != -1)
               {
                  String name = fields[i].getName().substring(fields[i].getName().indexOf("_") + 1);
                  
                  if (name.indexOf("_") != -1)
                     return name.replaceAll("_", " ");
                  else
                     return name;
               }
               else
                  return fields[i].getName();
            }
         }
      }
      catch (IllegalAccessException e)
      {
         // never happens
      }
      return "<unknown:" + typeid + ">";
   }
   
   //==============================================================
   // Class method to allow classes to try and obtain the field
   // integer value given the TypeID name.
   //==============================================================

   public static int valueOf(String typeidName) throws Exception
   {
      // Just return the number given an input
      // number.
      try
      {
         return (int) Long.parseLong(typeidName);
      }
      catch (NumberFormatException ex)
      {
      }
      
      try
      {
         typeidName = typeidName.toUpperCase();
         
         Field[] fields = TypeID.class.getFields();
         
         for (int i = 0; i < fields.length; ++i)
         {
            if (fields[i].getName().toUpperCase().equals(typeidName))
            {
               return fields[i].getInt(null);
            }
         }
      }
      catch (IllegalAccessException e)
      {
         // never happens
      }
      throw new Exception("TypeID name type {0} not known and not a number");
   }
}