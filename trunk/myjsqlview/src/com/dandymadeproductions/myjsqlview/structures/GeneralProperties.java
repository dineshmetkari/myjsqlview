//=================================================================
//                 GeneralProperties Class
//=================================================================
// This class provides the structure for the MyJSQLView application
// general properties storage.
//
//              << GeneralProperties.java >>
//
//=================================================================
// Copyright (C) 2005-2015 Dana M. Proctor
// Version 1.8 04/28/2015
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
//         1.5 Removed System.out in Constructor.
//         1.6 Added Class Instances proxyAddress & proxyPort Along With
//             Corresponding get/setter Methods.
//         1.7 Added Class Instances framePosition_X, framePosition_Y, frameWidth,
//             & frameHeight Along With Corresponding get/setter Methods.
//         1.8 Initialized Default Values for Property Instances in Constructor.
//
//-----------------------------------------------------------------
//                 danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview.structures;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.util.prefs.Preferences;

import javax.swing.UIManager;

import com.dandymadeproductions.myjsqlview.gui.MyJSQLView_Frame;
import com.dandymadeproductions.myjsqlview.gui.panels.GeneralPreferencesPanel;
import com.dandymadeproductions.myjsqlview.utilities.MyJSQLView_Utils;

/**
 *    The GeneralProperties class provides the structure for the
 * MyJSQLView application general properties storage.
 * 
 * @author Dana M. Proctor
 * @version 1.8 04/28/2015
 */

public class GeneralProperties
{
   // Class Instances.
   private int fontSize;
   private int framePosition_X, framePosition_Y;
   private int frameWidth, frameHeight;
   private String sequenceList;
   private String proxyAddress;
   private int proxyPort;
   
   private Preferences generalPreferences;
   
   public static final String APPFONTSIZE = "AppFontSize";
   public static final String APPFRAMEPOSITIONX = "AppFramePositionX";
   public static final String APPFRAMEPOSITIONY = "AppFramePositionY";
   public static final String APPFRAMEWIDTH = "AppFrameWidth";
   public static final String APPFRAMEHEIGHT = "AppFrameHeight";
   public static final String APPSEQUENCELIST = "AppSequenceList";
   public static final String PROXYADDRESS = "AppProxyAddress";
   public static final String PROXYPORT = "AppProxyPort";
   
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
      
      framePosition_X = 0;
      framePosition_Y = 0;
      frameWidth = 800;
      frameHeight = 600;
      sequenceList = null;
      proxyAddress = "127.0.0.1";
      proxyPort = 0;
      
      // Try to retrieve state from Preferences.
      try
      {
         generalPreferences = Preferences.userNodeForPackage(GeneralProperties.class);
      }
      catch (SecurityException se){return;}
      
      try
      {
         fontSize = generalPreferences.getInt(APPFONTSIZE, fontSize);
         framePosition_X = generalPreferences.getInt(APPFRAMEPOSITIONX, framePosition_X);
         framePosition_Y = generalPreferences.getInt(APPFRAMEPOSITIONY, framePosition_Y);
         frameWidth = generalPreferences.getInt(APPFRAMEWIDTH, frameWidth);
         frameHeight = generalPreferences.getInt(APPFRAMEHEIGHT, frameHeight);
         sequenceList = generalPreferences.get(APPSEQUENCELIST, sequenceList);
         proxyAddress = generalPreferences.get(PROXYADDRESS, proxyAddress);
         proxyPort = generalPreferences.getInt(PROXYPORT, proxyPort);
         
         if (framePosition_X < 0 || framePosition_Y < 0)
         {
            framePosition_X = 0;
            framePosition_Y = 0;
         }
         
         if (frameWidth <= 0 || frameHeight <= 0)
         {
            frameWidth = MyJSQLView_Frame.FRAME_DEFAULT_WIDTH;
            frameHeight = MyJSQLView_Frame.FRAME_DEFAULT_HEIGHT;
         }
         
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
   
   public Point getPosition()
   {
      return new Point(framePosition_X, framePosition_Y);
   }
   
   public Dimension getDimension()
   {
      return new Dimension(frameWidth, frameHeight);
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
   
   public String getProxyAddress()
   {
      return proxyAddress;
   }
   
   public int getProxyPort()
   {
      return proxyPort;
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
   
   public void setPosition(Point value)
   {
      if (value.x < 0 || value.y <= 0)
      {
         framePosition_X = 0;
         framePosition_Y = 0;
      }
      else
      {
         framePosition_X = value.x;
         framePosition_Y = value.y; 
      }
      savePreference(APPFRAMEPOSITIONX, value.x);
      savePreference(APPFRAMEPOSITIONY, value.y);
   }
   
   public void setDimension(Dimension value)
   {
      if (value.width <= 0 || value.height <= 0)
      {
         frameWidth = MyJSQLView_Frame.FRAME_DEFAULT_WIDTH;
         frameHeight = MyJSQLView_Frame.FRAME_DEFAULT_HEIGHT;
      }
      else
      {
         frameWidth = value.width;
         frameHeight = value.height; 
      }
      savePreference(APPFRAMEWIDTH, value.width);
      savePreference(APPFRAMEHEIGHT, value.height);
   }
   
   public void setSequenceList(int[] value)
   {
      StringBuffer sequenceBuffer = new StringBuffer();
      
      for (int i=0; i<value.length; i++)
         sequenceBuffer.append(value[i] + ":");
      sequenceList = sequenceBuffer.substring(0, sequenceBuffer.length() - 1);
      savePreference(APPSEQUENCELIST, sequenceList);
   }
   
   public void setProxyAddress(String value)
   {
      proxyAddress = value;
      savePreference(PROXYADDRESS, value);
   }
   
   public void setProxyPort(int value)
   {
      proxyPort = value;
      savePreference(PROXYPORT, value);
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
      parameters.append("[framePosition_X = " + framePosition_X + "]");
      parameters.append("[framePosition_Y = " + framePosition_Y + "]");
      parameters.append("[framewidth = " + frameWidth + "]");
      parameters.append("[frameheight = " + frameHeight + "]");
      parameters.append("[proxyAddress = " + proxyAddress + "]");
      parameters.append("proxyPort = " + proxyPort + "]");

      return parameters.toString();
   }
}
