//=================================================================
//                    MyJSQLView MyJFileFilter
//=================================================================
//
//    This class provides a custom FileFilter instance to be used
// in selecting MyJSQLView Table State Files.
//
//                  << MyJFileFilter.java >>
//
//=================================================================
// Copyright (C) 2005-2015 Dana M. Proctor
// Version 1.6 09/11/2012
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
// Version 1.0 Original MyJSQLView MyJFileFilter Class.
//         1.1 Changed Package to Reflect Dandy Made Productions Code.
//         1.2 Made Class Public.
//         1.3 Copyright Update.
//         1.4 Copyright Update.
//         1.5 Organized Imports.
//         1.6 Changed Package Name to com.dandymadeproductions.myjsqlview.utilities.
//             
//-----------------------------------------------------------------
//                 danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview.utilities;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 *    The MyJFileFilter class provides a customer FileFilter instance to
 * be used in selecting MyJSQLView Table State Files.
 * 
 * @author Dana M. Proctor
 * @version 1.6 09/11/2012
 */

public class MyJFileFilter extends FileFilter
{
   //==============================================================
   // Required class methods to implement the FileFilter. Accept
   // all directories and *.myj files.
   //==============================================================
   
   public boolean accept(File file)
   {
      String extension, fileName;
      int lastIndexOfDot;
      
      // All Directories
      if (file.isDirectory())
         return true;
      
      // Only MyJSQLView Table State Files, *.myj
      extension = "";
      fileName = file.getName();
      lastIndexOfDot = fileName.lastIndexOf('.');

      if (lastIndexOfDot > 0 &&  lastIndexOfDot < fileName.length() - 1)
          extension = fileName.substring(lastIndexOfDot + 1).toLowerCase();
      
      if (extension.equals("myj"))
         return true;
      
      // Nope.
      return false;
    }
   
   public String getDescription()
   {
      return "MyJSQLView Table State Files";
   }
}
