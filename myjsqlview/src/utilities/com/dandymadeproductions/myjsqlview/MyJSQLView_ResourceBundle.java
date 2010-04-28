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
// Copyright (C) 2005-2010 Dana M. Proctor
// Version 1.2 04/28/2010
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
//
//-----------------------------------------------------------------
//                 danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;

/**
 *    The MyJSQLView_ResourceBundle class provides a method to more closely
 * control the loading of locale resource files in the MyJSQLView program.
 * Handles also the methods needed to retrieve a resource key.
 * 
 * @author Dana M. Proctor
 * @version 1.2 02/28/2010
 */

class MyJSQLView_ResourceBundle
{
   // Class Instances.
   private Hashtable localeListData;

   //==============================================================
   // MyJSQLView_ResourceBundle Constructor
   //==============================================================

   protected MyJSQLView_ResourceBundle(String localeString)
   {
      // Constructor Instances.
      String baseName;
      String localeFileName;

      String currentEntry;
      String key, resource;

      FileReader fileReader;
      BufferedReader bufferedReader;

      // Setup to process.
      if (localeString.equals(""))
         return;
      else
      {
         baseName = "MyJSQLViewBundle";
         localeFileName = "locale" + MyJSQLView_Utils.getFileSeparator() + baseName + "_" + localeString
                          + ".properties";
      }

      // Begin processing the given locale file to obtain a hashtable
      // of the key, resource pairs.

      localeListData = new Hashtable();

      try
      {

         fileReader = new FileReader(localeFileName);
         bufferedReader = new BufferedReader(fileReader);

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
         fileReader.close();
      }
      catch (IOException ioe)
      {
         if (MyJSQLView.getDebug())
         {
            System.err.println("MyJSQLView_ResourceBundle Constructor() \n" +
                               "Failed to process the given locale file, " + localeFileName + "\n"
                               + ioe);
         }
      }
   }

   //==============================================================
   // Class Method for allowing package classes to obtain the locale
   // resource given a key.
   //==============================================================

   protected String getResource(String resourceKey)
   {
      // System.out.println(resourceKey);
      
      if (localeListData != null && resourceKey != null)
      {
         if (localeListData.containsKey(resourceKey))
            return (String) localeListData.get(resourceKey);
         else
            return "";
      }
      else
         return "";
   }
}