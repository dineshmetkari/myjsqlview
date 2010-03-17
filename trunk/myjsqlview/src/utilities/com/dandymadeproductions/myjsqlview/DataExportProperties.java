//=================================================================
//              DataExportProperties Class
//=================================================================
//	This class provides the structure for the data export
// properties storage.
//
//           << DataExportProperties.java >>
//
//=================================================================
// Copyright (C) 2006-2010 Dana Proctor
// Version 3.1 02/18/2010
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
// Version 1.0 Initial SQLDataExportProperties Class.
//         1.1 Author Comment Changes.
//         1.2 Added Update Options. Updated As Needed.
//         1.3 Class Methods getType() & getLock().
//         1.4 Class Instances insert/replaceExplicit and
//             Same getter/setter Methods.
//         1.5 Modified getAutoIncrement() and getTimeStamp()
//             to Return autoIncrement if Insert Selected or
//             False Otherwise.
//         1.6 Renamed From SQLDataExportProperties to DataExportProperties.
//         1.7 Added Instances textInclusion & textCharsNumber
//             Along With get/setter Methods.
//         1.8 Set the Default Comma Separateed Value to Comma.
//         1.9 Comment Changes.
//         2.0 Header Update.
//         2.1 Added Class Instance identifierQuoteString and Class
//             Methods getIdentifierQuoteString & setIdentifierQuoteString.
//         2.2 Deliminator to Delimiter.
//         2.3 Replaced Class Instance insertExplicit With insertExpression.
//             Removed Class Method getExplicit(). Replaced Class Methods
//             get/setInsertExplicit() With get/setInsertExpression().
//         2.4 Replaced Class Instance replaceExplicit With replaceExpression.
//             get/setReplaceExplicit() With get/setReplaceExpression().
//         2.5 Set the Initialization Content for insert/replaceExpression
//             to "Singular".
//         2.6 Changed Class Instances autoIncrement & timeStamp Initialization
//             in Constructor to false.
//         2.7 Added Class Instance dateFormat and Corresponding get/setter
//             Methods.
//         2,8 Class Instance dateFormat Initialization Changed From MM-DD-YYYY
//             to MM-dd-YYYY.
//         2.9 Implemented a Preferences API to Save State. Class Instances
//             dataExportPreferences, All Static Preferences Identifiers. Changes
//             to Constructor and Setter Methods. Addition Methods savePreferences().
//         3.0 Removed the identifierQuoteString From Preferences Saving/Loading.
//             When Switching Between Databases This Can Cause SQL Statements to
//             be Exported With the Incorrect Identifier Quote. This Causes More
//             Confusion Than it is Worth in Saving. Removed IDENTIFIERQUOTESTRING.
//         3.1 Changed Package to Reflect Dandy Made Productions Code.
//
//-----------------------------------------------------------------
//                 danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview;

import java.util.prefs.Preferences;

/**
 *    The DataExportProperties class provides the structure for the
 * data export properties storage.
 * 
 * @author Dana M. Proctor
 * @version 3.1 02/18/2010
 */

class DataExportProperties
{
   // Class Instances.
   private boolean tableStructure;
   private boolean tableData;
   private boolean insertLock;
   private boolean replaceLock;
   private boolean updateLock;
   private String insertExpression;
   private String replaceExpression;
   private boolean autoIncrement;
   private boolean timeStamp;
   private boolean insertType;
   private boolean replaceType;
   private boolean updateType;
   private String insertReplaceUpdate;
   private String insertTypeSetting;
   private String replaceTypeSetting;
   private String updateTypeSetting;
   private String identifierQuoteString;

   private boolean textInclusion;
   private int textCharsNumber;
   private String dataDelimiter;
   private String dateFormat;
   
   private Preferences dataExportPreferences;
   
   private static final String TABLESTRUCTURE = "TableStructure";
   private static final String  TABLEDATA = "TableData";
   private static final String INSERTLOCK = "InsertLock";
   private static final String REPLACELOCK = "replaceLock";
   private static final String UPDATELOCK = "UpdateLock";
   private static final String INSERTEXPRESSION = "InsertExpression";
   private static final String REPLACEEXPRESSION = "replaceExpression";
   private static final String AUTOINCREMENT = "AutoIncrement";
   private static final String TIMESTAMP = "TimeStamp";
   private static final String INSERTTYPE = "InsertType";
   private static final String REPLACETYPE = "ReplaceType";
   private static final String UPDATETYPE = "UpdateType";
   private static final String INSERTREPLACEUPDATE = "InsertReplaceUpdate";
   private static final String INSERTTYPESETTING = "InsertTypeSetting";
   private static final String REPLACETYPESETTING = "ReplaceTypeSetting";
   private static final String UPDATETYPESETTING = "UpdateTypeSetting";

   private static final String TEXTINCLUSION = "TextInclusion";
   private static final String TEXTCHARSNUMBER = "TextCharsNumber";
   private static final String DATADELIMITER = "ExportDataDelimiter";
   private static final String DATEFORMAT = "ExportDateFormat";

   //==============================================================
   // DataExportProperties Constructor
   //==============================================================

   protected DataExportProperties()
   {
      // Set Default State.
      tableStructure = false;
      tableData = true;
      insertLock = true;
      replaceLock = true;
      updateLock = true;
      insertExpression = "Singular";
      replaceExpression = "Singular";
      autoIncrement = false;
      timeStamp = false;
      insertType = false;
      replaceType = false;
      updateType = false;
      insertReplaceUpdate = "Insert";
      insertTypeSetting = "Low_Priority";
      replaceTypeSetting = "Low_Priority";
      updateTypeSetting = "Low_Priority";
      identifierQuoteString = MyJSQLView_Access.getIdentifierQuoteString();

      textInclusion = false;
      textCharsNumber = 50;
      dataDelimiter = ",";
      dateFormat = "MM-dd-YYYY";
      
      // Try to retrieve state from Preferences.
      try
      {
         dataExportPreferences = Preferences.userNodeForPackage(DataExportProperties.class);
      }
      catch (SecurityException se){return;}
      
      try
      {
         tableStructure = dataExportPreferences.getBoolean(TABLESTRUCTURE, false);
         tableData = dataExportPreferences.getBoolean(TABLEDATA, true);
         insertLock = dataExportPreferences.getBoolean(INSERTLOCK, true);
         replaceLock = dataExportPreferences.getBoolean(REPLACELOCK, true);
         updateLock = dataExportPreferences.getBoolean(UPDATELOCK, true);
         insertExpression = dataExportPreferences.get(INSERTEXPRESSION, "Singular");
         replaceExpression = dataExportPreferences.get(REPLACEEXPRESSION, "Singular");
         autoIncrement = dataExportPreferences.getBoolean(AUTOINCREMENT, false);
         timeStamp = dataExportPreferences.getBoolean(TIMESTAMP, false);
         insertType = dataExportPreferences.getBoolean(INSERTTYPE, false);
         replaceType = dataExportPreferences.getBoolean(REPLACETYPE, false);
         updateType = dataExportPreferences.getBoolean(UPDATETYPE, false);
         insertReplaceUpdate = dataExportPreferences.get(INSERTREPLACEUPDATE, "Insert");
         insertTypeSetting = dataExportPreferences.get(INSERTTYPESETTING, "Low_Priority");
         replaceTypeSetting = dataExportPreferences.get(REPLACETYPESETTING, "Low_Priority");
         updateTypeSetting = dataExportPreferences.get(UPDATETYPESETTING, "Low_Priority");
         
         textInclusion = dataExportPreferences.getBoolean(TEXTINCLUSION, false);
         textCharsNumber = dataExportPreferences.getInt(TEXTCHARSNUMBER, 50);
         dataDelimiter = dataExportPreferences.get(DATADELIMITER, ",");
         dateFormat = dataExportPreferences.get(DATEFORMAT, "MM-DD-YYYY"); 
      }
      catch (NullPointerException npe){}
      catch (IllegalStateException ise){}
   }

   //==============================================================
   // Class methods to allow classes to get the data export
   // object components.
   //==============================================================

   protected boolean getTableStructure()
   {
      return tableStructure;
   }

   protected boolean getTableData()
   {
      return tableData;
   }

   protected boolean getInsertLock()
   {
      return insertLock;
   }

   protected boolean getReplaceLock()
   {
      return replaceLock;
   }

   protected boolean getUpdateLock()
   {
      return updateLock;
   }

   protected String getInsertExpression()
   {
      return insertExpression;
   }

   protected String getReplaceExpression()
   {
      return replaceExpression;
   }

   protected boolean getAutoIncrement()
   {
      if (insertReplaceUpdate.equals("Insert"))
         return autoIncrement;
      else
         return false;
   }

   protected boolean getTimeStamp()
   {
      if (insertReplaceUpdate.equals("Insert"))
         return timeStamp;
      else
         return false;
   }

   protected boolean getInsertType()
   {
      return insertType;
   }

   protected boolean getReplaceType()
   {
      return replaceType;
   }

   protected boolean getUpdateType()
   {
      return updateType;
   }

   protected String getInsertReplaceUpdate()
   {
      return insertReplaceUpdate;
   }

   protected String getInsertTypeSetting()
   {
      return insertTypeSetting;
   }

   protected String getReplaceTypeSetting()
   {
      return replaceTypeSetting;
   }

   protected String getUpdateTypeSetting()
   {
      return updateTypeSetting;
   }

   protected boolean getLock()
   {
      if (insertReplaceUpdate.equals("Insert"))
         return insertLock;
      else if (insertReplaceUpdate.equals("Replace"))
         return replaceLock;
      else
         return updateLock;
   }

   protected String getType()
   {
      if (tableStructure)
      {
         if (insertType)
            return " " + insertTypeSetting + " ";
         else
            return " ";
      }
      else
      {
         if (insertReplaceUpdate.equals("Insert"))
         {
            if (insertType)
               return " " + insertTypeSetting + " ";
            else
               return " ";
         }
         else if (insertReplaceUpdate.equals("Replace"))
         {
            if (replaceType)
               return " " + replaceTypeSetting + " ";
            else
               return " ";
         }
         else if (insertReplaceUpdate.equals("Update"))
         {
            if (updateType)
               return " " + updateTypeSetting + " ";
            else
               return " ";
         }
         else
            return " ";
      }
   }

   protected String getIdentifierQuoteString()
   {
      return identifierQuoteString;
   }

   protected boolean getTextInclusion()
   {
      return textInclusion;
   }

   protected int getTextCharsNumber()
   {
      return textCharsNumber;
   }

   protected String getDataDelimiter()
   {
      return dataDelimiter;
   }
   
   protected String getDateFormat()
   {
      return dateFormat;
   }

   //==============================================================
   // Class methods to allow classes to set the data export
   // object components.
   //==============================================================

   protected void setTableStructure(boolean value)
   {
      tableStructure = value;
      savePreference(TABLESTRUCTURE, value);
   }

   protected void setTableData(boolean value)
   {
      tableData = value;
      savePreference(TABLEDATA, value);
   }

   protected void setInsertLock(boolean value)
   {
      insertLock = value;
      savePreference(INSERTLOCK, value);
   }

   protected void setReplaceLock(boolean value)
   {
      replaceLock = value;
      savePreference(REPLACELOCK, value);
   }

   protected void setUpdateLock(boolean value)
   {
      updateLock = value;
      savePreference(UPDATELOCK, value);
   }

   protected void setInsertExpression(String content)
   {
      insertExpression = content;
      savePreference(INSERTEXPRESSION, content);
   }

   protected void setReplaceExpression(String content)
   {
      replaceExpression = content;
      savePreference(REPLACEEXPRESSION, content);
   }

   protected void setAutoIncrement(boolean value)
   {
      autoIncrement = value;
      savePreference(AUTOINCREMENT, value);
   }

   protected void setTimeStamp(boolean value)
   {
      timeStamp = value;
      savePreference(TIMESTAMP, value);
   }

   protected void setInsertType(boolean value)
   {
      insertType = value;
      savePreference(INSERTTYPE, value);
   }

   protected void setReplaceType(boolean value)
   {
      replaceType = value;
      savePreference(REPLACETYPE, value);
   }

   protected void setUpdateType(boolean value)
   {
      updateType = value;
      savePreference(UPDATETYPE, value);
   }

   protected void setInsertReplaceUpdate(String content)
   {
      insertReplaceUpdate = content;
      savePreference(INSERTREPLACEUPDATE, content);
   }

   protected void setInsertTypeSetting(String content)
   {
      insertTypeSetting = content;
      savePreference(INSERTTYPESETTING, content);
   }

   protected void setReplaceTypeSetting(String content)
   {
      replaceTypeSetting = content;
      savePreference(REPLACETYPESETTING, content);
   }

   protected void setUpdateTypeSetting(String content)
   {
      updateTypeSetting = content;
      savePreference(UPDATETYPESETTING, content);
   }

   protected void setIdentifierQuoteString(String content)
   {
      identifierQuoteString = content;
   }

   protected void setTextInclusion(boolean value)
   {
      textInclusion = value;
      savePreference(TEXTINCLUSION, value);
   }

   protected void setTextCharsNumber(int value)
   {
      textCharsNumber = value;
      savePreference(TEXTCHARSNUMBER, value);
   }

   protected void setDataDelimiter(String content)
   {
      dataDelimiter = content;
      savePreference(DATADELIMITER, content);
   }
   
   protected void setDateFormat(String content)
   {
      dateFormat = content;
      savePreference(DATEFORMAT, content);
   }
   
   //==============================================================
   // Class methods to try and save the preferences state. 
   //==============================================================

   private void savePreference(String key, boolean value)
   {
      try
      {
         if (dataExportPreferences != null)
            dataExportPreferences.putBoolean(key, value);
      }
      catch (IllegalArgumentException iae){}
      catch (IllegalStateException ise){}
   }
   
   private void savePreference(String key, String content)
   {
      try
      {
         if (dataExportPreferences != null)
            dataExportPreferences.put(key, content);
      }
      catch (IllegalArgumentException iae){}
      catch (IllegalStateException ise){}
   }
   
   private void savePreference(String key, int value)
   {
      try
      {
         if (dataExportPreferences != null)
            dataExportPreferences.putInt(key, value);
      }
      catch (IllegalArgumentException iae){}
      catch (IllegalStateException ise){}
   }
   
   //==============================================================
   // Class method to properly implement the toString() method
   // for the object. Local method overides.
   //==============================================================

   public String toString()
   {
      StringBuffer parameters = new StringBuffer("[DataExportProperties: ");
      parameters.append("[tableStructure = " + tableStructure + "]");
      parameters.append("[tableData = " + tableData + "]");
      parameters.append("[insertLock = " + insertLock + "]");
      parameters.append("[replaceLock = " + replaceLock + "]");
      parameters.append("[updateLock = " + updateLock + "]");
      parameters.append("[insertExplicit = " + insertExpression + "]");
      parameters.append("[replaceExplicit = " + replaceExpression + "]");
      parameters.append("[autoIncrement = " + autoIncrement + "]");
      parameters.append("[timeStamp = " + timeStamp + "]");
      parameters.append("[insertType = " + insertType + "]");
      parameters.append("[replaceType = " + replaceType + "]");
      parameters.append("[updateType = " + updateType + "]");
      parameters.append("[insertReplaceUpdate = " + insertReplaceUpdate + "]");
      parameters.append("[insertTypeSetting = " + insertTypeSetting + "]");
      parameters.append("[replaceTypeSetting = " + replaceTypeSetting + "]");
      parameters.append("[updateTypeSetting = " + updateTypeSetting + "]");
      parameters.append("[textInclusion = " + textInclusion + "]");
      parameters.append("[textCharsNumber = " + textCharsNumber + "]");
      parameters.append("[dataDelimiter = " + dataDelimiter + "]");
      parameters.append("[dataFormat = " + dateFormat + "]");

      return parameters.toString();
   }
}