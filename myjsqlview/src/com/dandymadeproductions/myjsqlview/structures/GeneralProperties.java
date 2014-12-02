//=================================================================
//                 GeneralProperties Class
//=================================================================
// This class provides the structure for the MyJSQLView application
// general properties storage.
//
//              << GeneralProperties.java >>
//
//=================================================================
// Copyright (C) 2005-2014 Dana M. Proctor
// Version 1.4 12/01/2014
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
//         1.3 Added Class Instance sequenceList and Corresponding get/setter
//             Methods.
//         1.4 Constructor Setting sequenceList Defaults According to
//             GeneralPreferencesPanel Instances. Correction to Save in
//             Method savePreferences(String, String).
//
//-----------------------------------------------------------------
//                 danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview.structures;

import java.awt.Font;
import java.util.prefs.Preferences;

import javax.swing.UIManager;

import com.dandymadeproductions.myjsqlview.gui.panels.GeneralPreferencesPanel;
import com.dandymadeproductions.myjsqlview.utilities.MyJSQLView_Utils;

/**
 *    The GeneralProperties class provides the structure for the
 * MyJSQLView application general properties storage.
 * 
 * @author Dana M. Proctor
 * @version 1.4 12/01/2014
 */

public class GeneralProperties
{
   // Class Instances.
   private int fontSize;
   private String sequenceList;
   
   private Preferences generalPreferences;
   
   public static final String APPFONTSIZE = "AppFontSize";
   public static final String APPSEQUENCELIST = "AppSequenceList";
   
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
         sequenceList = generalPreferences.get(APPSEQUENCELIST, sequenceList);
         System.out.println("GeneralProperties sequenceList: '" + sequenceList + "'");
         
         if (sequenceList == null || sequenceList.isEmpty())
         {
            setSequenceList(MyJSQLView_Utils.getChartList(GeneralPreferencesPanel.DEFAULT_SEQUENCE_SIZE,
                                                          GeneralPreferencesPanel.DEFAULT_SEQUENCE_MAX));
            savePreference(APPSEQUENCELIST, sequenceList);
         }
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
   
   public int[] getSequenceList()
   {
      String[] sequence = sequenceList.split(":");
      int[] sequenceListArray = new int[sequence.length];
      try
      {
         for (int i=0; i<sequence.length; i++)
            sequenceListArray[i] = Integer.parseInt(sequence[i]);
         return sequenceListArray;
      }
      catch (NumberFormatException nfe)
      {
         return null;
      }
   }
   
   //==============================================================
   // Class methods to allow classes to set the general object
   // components.
   //==============================================================
   
   public void setFontSize(int value)
   {
      fontSize = value;
      savePreference(APPFONTSIZE, value);
   }
   
   public void setSequenceList(int[] value)
   {
      StringBuffer sequenceBuffer = new StringBuffer();
      
      for (int i=0; i<value.length; i++)
         sequenceBuffer.append(value[i] + ":");
      sequenceList = sequenceBuffer.substring(0, sequenceBuffer.length() - 1);
      savePreference(APPSEQUENCELIST, sequenceList);
   }
   
   //==============================================================
   // Class methods to try and save the preferences state. 
   //==============================================================

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
      
      parameters.append("[fontSize = " + fontSize + "]");

      return parameters.toString();
   }
}