//=================================================================
//                 GeneralProperties Class
//=================================================================
// This class provides the structure for the MyJSQLView application
// general properties storage.
//
//              << GeneralProperties.java >>
//
//=================================================================
// Copyright (C) 2005-2013 Dana M. Proctor
// Version 1.2 09/29/2013
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
// Version 1.0 Initial GeneralProperties Class for Overall Application.
//         1.1 Changed the Default fontSize Generation.
//         1.2 Constructor Check of uiObject NULL Before instanceof Font.
//
//-----------------------------------------------------------------
//                 danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview.structures;

import java.awt.Font;
import java.util.prefs.Preferences;

import javax.swing.UIManager;

/**
 *    The GeneralProperties class provides the structure for the
 * MyJSQLView application general properties storage.
 * 
 * @author Dana M. Proctor
 * @version 1.2 09/29/2013
 */

public class GeneralProperties
{
   // Class Instances.
   private int fontSize;
   
   private Preferences generalPreferences;

   public static final String APPFONTSIZE = "AppFontSize";
   
   //==============================================================
   // GeneralProperties Constructor
   //==============================================================

   public GeneralProperties()
   {
      // Set Default State.
      
      // Font Size
      Object uiObject = null;
      uiObject = UIManager.get("Label.font");
      
      if (uiObject != null && uiObject instanceof Font)
      {
         Font uiManagerFont = (Font) uiObject;
         fontSize = uiManagerFont.getSize();
      }
      else
         fontSize = 12;
      
      // Try to retrieve state from Preferences.
      try
      {
         generalPreferences = Preferences.userNodeForPackage(GeneralProperties.class);
      }
      catch (SecurityException se){return;}
      
      try
      {
         fontSize = generalPreferences.getInt(APPFONTSIZE, fontSize);
      }
      catch (NullPointerException npe){}
      catch (IllegalStateException ise){}
   }

   //==============================================================
   // Class methods to allow classes to get the general object
   // components.
   //==============================================================
   
   public int getFontSize()
   {
      return fontSize;
   }
   
   //==============================================================
   // Class methods to allow classes to set the data export
   // object components.
   //==============================================================
   
   public void setFontSize(int value)
   {
      fontSize = value;
      savePreference(APPFONTSIZE, value);
   }
   
   //==============================================================
   // Class methods to try and save the preferences state. 
   //==============================================================

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
   
   //==============================================================
   // Class method to properly implement the toString() method
   // for the object. Local method overides.
   //==============================================================

   public String toString()
   {
      StringBuffer parameters = new StringBuffer("[DataExportProperties: ");
      
      parameters.append("[limitIncrement = " + fontSize + "]");

      return parameters.toString();
   }
}