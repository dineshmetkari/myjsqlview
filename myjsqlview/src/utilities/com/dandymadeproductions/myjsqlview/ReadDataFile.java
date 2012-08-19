//=================================================================
//               MyJSQLView Read Data File Class
//=================================================================
//
//    This class allows the MyJSQLView application the means to
// import data that can be placed in the database table in an
// automated way. The class is also used to import data images
// files. The class provides a generic framework to read bytes
// of data from a given input file.
// 
//                   << ReadDataFile.java >>
//
//=================================================================
// Copyright (C) 2005-2012 Dana M. Proctor
// Version 2.5 08/19/2012
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
// Version 1.0 12/26/2005 Original Class to Import Data from a
//                        File
//         1.1 01/21/2007 Added JOptionPane Dialog for
//                        IOException Errors.
//         1.2 01/22/2007 DumpProgressBar Option Argument to
//                        Class Method mainReadDataString().
//         1.3 01/28/2007 Class Instance validRead & Returning
//                        Null as Needed on Cancel.
//         1.4 09/03/2007 Class Instance dumpProgressBar Set
//                        pack() and setVisible(true).
//         1.5 09/07/2007 Changed the Manner in Which the Bytes
//                        Are Read if Not Showing Progress Bar.
//                        Was filebuff.read(bytes).
//         1.6 10/21/2007 Cleaned Up Javadoc Comments.
//         1.7 10/22/2007 Corrected Javadoc Comments.
//         1.8 12/12/2007 Header Update.
//         1.9 10/23/2008 MyJSQLView Project Common Source Code Formatting.
//         2.0 05/27/2009 Header Format Changes/Update.
//         2.1 02/18/2010 Changed Package to Reflect Dandy Made Productions Code.
//         2.2 06/28/2010 Made Class Public in Addition to mainReadDataString().
//         2.3 01/27/2011 Copyright Update.
//         2.4 01/01/2012 Copyright Update.
//         2.5 08/19/2012 Organized Imports.
//
//-----------------------------------------------------------------
//                danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;

import javax.swing.JOptionPane;

/**
 *    The ReadDataFile class allows the MyJSQLView application the
 * means to import data that can be placed in the database table in
 * an automated way. The class is also used to import data images
 * files. The class provides a generic framework to read bytes of
 * data from a given input file.
 * 
 * @author Dana M. Proctor
 * @version 2.5 08/19/2012
 */

public class ReadDataFile
{
   // =================================================
   // Creation of the data types needed to perform the
   // Reading of an Input File.
   // =================================================

   FileInputStream fileStream;
   BufferedInputStream filebuff;
   MyJSQLView_ProgressBar dumpProgressBar;
   int inSize;
   byte[] inBytes;
   boolean validRead;

   //==============================================================
   // Method for reading the file containing input data.
   //==============================================================

   private byte[] readInputFileText(String inputFileString, boolean showDumpProgressBar)
   {
      try
      {
         // Setting up InputStreams

         fileStream = new FileInputStream(inputFileString);
         filebuff = new BufferedInputStream(fileStream);
         inSize = filebuff.available();
         inBytes = new byte[inSize];
         validRead = true;

         // Creating a dump dialog progress bar as needed;
         // reading the data from the specified input
         // file and placing input the byte array.

         int i = 0;

         if (showDumpProgressBar)
         {
            dumpProgressBar = new MyJSQLView_ProgressBar("Reading File: " + inputFileString);
            dumpProgressBar.setTaskLength(inSize);
            dumpProgressBar.pack();
            dumpProgressBar.center();
            dumpProgressBar.setVisible(true);

            while (i < inSize)
            {
               // Checking to see if user wishes to
               // quit operation.
               if (dumpProgressBar.isCanceled())
               {
                  validRead = false;
                  i = inSize;
               }
               else
               {
                  dumpProgressBar.setCurrentValue(i);
                  inBytes[i++] = (byte) filebuff.read();
               }
            }
            dumpProgressBar.dispose();
         }
         else
         {
            while (i < inSize)
               inBytes[i++] = (byte) filebuff.read();
         }
         filebuff.close();
         fileStream.close();

         // Check to see if canceled.
         if (validRead)
            return inBytes;
         else
            return null;
      }
      catch (IOException e)
      {
         String ioExceptionString = e.toString();
         if (ioExceptionString.length() > 200)
            ioExceptionString = e.getMessage().substring(0, 200);

         String optionPaneStringErrors = "Error Reading File: " + inputFileString + "\n" + "IOException: "
                                         + ioExceptionString;

         JOptionPane.showMessageDialog(null, optionPaneStringErrors, "Alert", JOptionPane.ERROR_MESSAGE);
         return inBytes = null;
      }
   }

   //==============================================================
   // Creation of the main ReadDataFile class that is called by
   // outside classes to perform the reading of data from a file.
   //==============================================================

   public static byte[] mainReadDataString(String inputFileString, boolean showDumpProgressBar)
   {
      ReadDataFile r = new ReadDataFile();
      {
         return r.readInputFileText(inputFileString, showDumpProgressBar);
      }
   }
}