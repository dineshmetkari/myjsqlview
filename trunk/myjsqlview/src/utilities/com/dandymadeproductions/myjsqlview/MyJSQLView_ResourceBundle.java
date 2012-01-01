//=================================================================
//                 MyJSQLView_ResourceBundle Class
//=================================================================
//
//    This class provides a method to more closely control the loading
// of locale resource files in the MyJSQLView program. Handles also
// the methods needed to retrieve a resource key.
//
//               << MyJSQLView_ResourceBundle.java >>
//
//=================================================================
// Copyright (C) 2005-2012 Dana M. Proctor
// Version 1.9 01/01/2012
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
// Version 1.0 02/23/2010 Initial MyJSQLView_ResourceBundle Class.
//         1.1 02/24/2010 Class Method getResource() Changed Conditional Check From
//                        localeListData.contains() to localeListData.containsKey().
//         1.2 04/28/2010 Constructor Added the MyJSQLView.getDebug() to Catching
//                        the IOException and Additional Output Information.
//         1.3 05/16/2010 Parameterized Class Instance localeListData to Bring Code
//                        Into Compliance With Java 5.0 API.
//         1.4 06/16/2010 Conversion of Using a FileReader to Using FileInputStream
//                        and a InputStreamReader With Specification of UTF-16 For
//                        CharsetName.
//         1.5 06/27/2010 Removed Constructor Instance baseName and Placed As An 
//                        Argument in Constructor. Changed Class to Public and Its
//                        Method getResource(). Cleaned Up Some and Created An OptionPane
//                        For When IOException Created in Unable to Load Resource.
//         1.6 06/27/2010 Added Constructor Argument localeDirectory.
//         1.7 01/27/2011 Copyright Update.
//         1.8 01/01/2012 Copyright Update.
//         1.9 01/01/2012 Implemented Class is Serializable.
//                        
//-----------------------------------------------------------------
//                 danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.Hashtable;

import javax.swing.JOptionPane;

/**
 *    The MyJSQLView_ResourceBundle class provides a method to more closely
 * control the loading of locale resource files in the MyJSQLView program.
 * Handles also the methods needed to retrieve a resource key.
 * 
 * @author Dana M. Proctor
 * @version 1.9 01/01/2012
 */

public class MyJSQLView_ResourceBundle implements Serializable
{
   // Class Instances.
   private static final long serialVersionUID = -6752902010674915905L;
   
   private Hashtable<String, String> localeListData;

   //==============================================================
   // MyJSQLView_ResourceBundle Constructor
   //==============================================================

   public MyJSQLView_ResourceBundle(String localeDirectory, String baseName, String localeString)
   {
      // Constructor Instances.
      String localeFileName;

      String currentEntry;
      String key, resource;

      FileInputStream fileInputStream;
      InputStreamReader inputStreamReader;
      BufferedReader bufferedReader;

      // Setup to process.
      if (localeString.equals(""))
         return;
      else
      {
         localeFileName = localeDirectory + MyJSQLView_Utils.getFileSeparator() + baseName
                          + "_" + localeString + ".properties";
      }

      // Begin processing the given locale file to obtain a hashtable
      // of the key, resource pairs.

      localeListData = new Hashtable <String, String>();
      
      try
      {
         fileInputStream = new FileInputStream(localeFileName);
         inputStreamReader = new InputStreamReader(fileInputStream, "UTF-16");
         bufferedReader = new BufferedReader(inputStreamReader);
         
         while ((currentEntry = bufferedReader.readLine()) != null)
         {
            currentEntry = currentEntry.trim();

            if (currentEntry.indexOf("=") != -1)
            {
               key = currentEntry.substring(0, currentEntry.indexOf("=")).trim();
               resource = currentEntry.substring(currentEntry.indexOf("=") + 1).trim();
               // System.out.println(key + " " + resource);
               
               localeListData.put(key, resource);
            }
         }
         bufferedReader.close();
         inputStreamReader.close();
         fileInputStream.close();
      }
      catch (IOException ioe)
      {
         String ioExceptionString = ioe.toString();
         if (ioExceptionString.length() > 200)
            ioExceptionString = ioe.getMessage().substring(0, 200);

            String optionPaneStringErrors = "MyJSQLView_ResourceBundle Constructor() \n" +
                                            "Failed to process the given locale file, "
                                            + localeFileName + "\n"
                                            + ioExceptionString;

            JOptionPane.showMessageDialog(null, optionPaneStringErrors, "Alert",
                                          JOptionPane.ERROR_MESSAGE);
      }
   }

   //==============================================================
   // Class Method for allowing package classes to obtain the locale
   // resource given a key.
   //==============================================================

   public String getResource(String resourceKey)
   {
      // System.out.println(resourceKey);
      
      if (localeListData != null && resourceKey != null)
      {
         if (localeListData.containsKey(resourceKey))
            return localeListData.get(resourceKey);
         else
            return "";
      }
      else
         return "";
   }
}