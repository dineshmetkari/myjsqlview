//=================================================================
//                 GeneralProperties Class
//=================================================================
// This class provides the structure for the MyJSQLView general
// properties storage.
//
//              << GeneralProperties.java >>
//
//=================================================================
// Copyright (C) 2006-2011 Dana Proctor
// Version 1.1 05/07/2011
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
// Version 1.0 Initial GeneralProperties Class.
//         1.1 Made Class public Along With Class Method getViewDateFormat().
//
//-----------------------------------------------------------------
//                 danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview;

import java.util.prefs.Preferences;

/**
 *    The GeneralProperties class provides the structure for the
 * MyJSQLView general properties storage.
 * 
 * @author Dana M. Proctor
 * @version 1.1 05/07/2011
 */

public class GeneralProperties
{
   // Class Instances.
   
   private String viewDateFormat;
   
   private Preferences generalPreferences;

   private static final String VIEWDATEFORMAT = "ViewDateFormat";
   
   //==============================================================
   // GeneralProperties Constructor
   //==============================================================

   protected GeneralProperties()
   {
      // Set Default State.
      
      viewDateFormat = "MM-dd-YYYY";
      
      // Try to retrieve state from Preferences.
      try
      {
         generalPreferences = Preferences.userNodeForPackage(GeneralProperties.class);
      }
      catch (SecurityException se){return;}
      
      try
      {
         viewDateFormat = generalPreferences.get(VIEWDATEFORMAT, "MM-DD-YYYY");
         
      }
      catch (NullPointerException npe){}
      catch (IllegalStateException ise){}
   }

   //==============================================================
   // Class methods to allow classes to get the general object
   // components.
   //==============================================================

   public String getViewDateFormat()
   {
      return viewDateFormat;
   }
   
   //==============================================================
   // Class methods to allow classes to set the data export
   // object components.
   //==============================================================
   
   protected void setViewDateFormat(String content)
   {
      viewDateFormat = content;
      savePreference(VIEWDATEFORMAT, content);
   }
   
   //==============================================================
   // Class methods to try and save the preferences state. 
   //==============================================================

   /*
   private void savePreference(String key, boolean value)
   {
      try
      {
         if (generalPreferences != null)
            generalPreferences.putBoolean(key, value);
      }
      catch (IllegalArgumentException iae){}
      catch (IllegalStateException ise){}
   }
   */
   
   private void savePreference(String key, String content)
   {
      try
      {
         if (generalPreferences != null)
            generalPreferences.put(key, content);
      }
      catch (IllegalArgumentException iae){}
      catch (IllegalStateException ise){}
   }
   
   /*
   private void savePreference(String key, int value)
   {
      try
      {
         if (generalPreferences != null)
            generalPreferences.putInt(key, value);
      }
      catch (IllegalArgumentException iae){}
      catch (IllegalStateException ise){}
   }
   */
   
   //==============================================================
   // Class method to properly implement the toString() method
   // for the object. Local method overides.
   //==============================================================

   public String toString()
   {
      StringBuffer parameters = new StringBuffer("[DataExportProperties: ");
      
      parameters.append("[viewDataFormat = " + viewDateFormat + "]");

      return parameters.toString();
   }
}