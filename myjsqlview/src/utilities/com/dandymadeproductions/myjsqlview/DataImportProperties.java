//=================================================================
//              DataImportProperties Class
//=================================================================
//	This class provides the structure for the data import
// properties storage.
//
//           << DataImportProperties.java >>
//
//=================================================================
// Copyright (C) 2007-2012 Dana Proctor
// Version 2.1 01/01/2012
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
// Version 1.0 Initial DataImportProperties Class.
//         1.1 Header Update.
//         1.2 Deliminator to Delimiter.
//         1.3 MyJSQLView Project Common Source Code Formatting.
//         1.4 Added Class Instance dateFormat and Corresponding get/setter
//             Methods.
//         1,5 Class Instance dateFormat Initialization Changed From MM-DD-YYYY
//             to MM-dd-YYYY.
//         1.6 Implemented a Preferences API to Save State. Class Instances
//             dataImportPreferences, All Static Preferences Identifiers. Changes
//             to Constructor and Setter Methods. Additional Method savePreference().
//         1.7 Changed Package to Reflect Dandy Made Productions Code.
//         1.8 Organized Imports.
//         1.9 Copyright Update.
//         2.0 Set Default dateFormat to MyJSQLView_Utils.MMddyyyy_DASH in the
//             Constructor.
//         2.1 Copyright Update.
//
//-----------------------------------------------------------------
//                  danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview;

import java.util.prefs.Preferences;

/**
 *    The DataImportProperties class provides the structure for
 * data import properties storage.
 * 
 * @author Dana M. Proctor
 * @version 2.1 01/01/2012
 */

class DataImportProperties
{
   // Class Instances.
   private String dataDelimiter;
   private String dateFormat;
   
   private Preferences dataImportPreferences;
   
   private static final String DATADELIMITER = "ImportDataDelimiter";
   private static final String DATEFORMAT = "ImportDateFormat";

   //==============================================================
   // DataImportProperties Constructor
   //==============================================================

   protected DataImportProperties()
   {  
      // Set default state.
      dataDelimiter = ",";
      dateFormat = MyJSQLView_Utils.MMddyyyy_DASH;
      
      // Try to retrieve state from Preferences.
      try
      {
         dataImportPreferences = Preferences.userNodeForPackage(DataImportProperties.class);
      }
      catch (SecurityException se){return;}
      
      try
      {
         dataDelimiter = dataImportPreferences.get(DATADELIMITER, ",");
         dateFormat = dataImportPreferences.get(DATEFORMAT, MyJSQLView_Utils.MMddyyyy_DASH);
      }
      catch (NullPointerException npe){}
      catch (IllegalStateException ise){}
   }
   
   //==============================================================
   // Class methods to allow classes to get the data import
   // object components.
   //==============================================================

   protected String getDataDelimiter()
   {
      return dataDelimiter;
   }
   
   protected String getDateFormat()
   {
      return dateFormat;
   }

   //==============================================================
   // Class methods to allow classes to set the data import
   // object components.
   //==============================================================

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

   private void savePreference(String key, String content)
   {
      try
      {
         if (dataImportPreferences != null)
            dataImportPreferences.put(key, content);
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
      StringBuffer parameters = new StringBuffer("[DataImportProperties: ");
      parameters.append("[dataDelimiter = " + dataDelimiter + "]");
      parameters.append("[dataFormat = " + dateFormat + "]");

      return parameters.toString();
   }
}