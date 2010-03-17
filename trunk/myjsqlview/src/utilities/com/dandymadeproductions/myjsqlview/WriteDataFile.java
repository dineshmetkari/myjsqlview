//=================================================================
//               MyJSQLView Write Data File Class
//=================================================================
//
//    This class allows data in a selected database table to
// be outputed to specified file. A progress bar may be called
// to allow the control of the data being dumped. The class
// also provides a generic means to output byte[] data to a file.
// 
//                  << WriteDataFile.java >>
//
//=================================================================
// Copyright (C) 2005-2010 Dana M. Proctor
// Version 2.0 02/18/2010
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
// Version 1.0 12/27/2005 Original Class to Output Data to a Text File.
//         1.1 05/29/2006 Removed Unused Local Instance in.
//         1.2 01/21/2007 JOptionPane Dialog for IOException Output.
//         1.3 01/21/2007 DumbProgressBar Option Argument to Class Method
//                        mainWriteDataString().
//         1.4 04/17/2007 Class Instance fileStream Flushed and Closed Before
//                        Finishing. Bug With XML Configuration File Corruption,
//                        Fix?
//         1.5 09/03.2007 Class Instance dumpProgressBar Set pack() and
//                        setVisible(true).
//         1.6 10/21/2007 Cleaned Up Javadoc Comments.
//         1.7 12/12/2008 Header Update.
//         1.8 10/23/2008 MyJSQLView Project Common Source Code Formatting.
//         1.9 05/27/2009 Header Format Changes/Update.
//         2.0 02/18/2010 Changed Package to Reflect Dandy Made Productions Code.
//
//-----------------------------------------------------------------
//                  danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview;

import java.io.*;
import javax.swing.JOptionPane;

/**
 *     The WriteDataFile class allows data in a selected database
 * table to be outputed to specified file. A progress bar may be
 * called to allow the control of the data being dumped. The class
 * also provides a generic means to output byte[] data to a file.
 * 
 * @author Dana M. Proctor
 * @version 2.0 02/18/2010
 */

class WriteDataFile
{
   // =================================================
   // Creation of the data types needed to perform the
   // Writing of an Input File.
   // =================================================

   FileOutputStream fileStream;
   BufferedOutputStream filebuff;
   MyJSQLView_ProgressBar dumpProgressBar;

   //==============================================================
   // Method for writing the file containing output data.
   //==============================================================

   private void writeDataFileText(String outputFileString, byte[] buf, boolean showDumpProgressBar)
   {
      try
      {
         // Setting up OutputStream
         fileStream = new FileOutputStream(outputFileString);
         filebuff = new BufferedOutputStream(fileStream);

         // Creating a dump dialog progress bar as needed
         // and writing the specified data to the ouput file.

         int i = 0;

         if (showDumpProgressBar)
         {
            dumpProgressBar = new MyJSQLView_ProgressBar("Writing to File: " + outputFileString);
            dumpProgressBar.setTaskLength(buf.length);
            dumpProgressBar.pack();
            dumpProgressBar.center();
            dumpProgressBar.setVisible(true);

            while (i < buf.length & !dumpProgressBar.isCanceled())
            {
               dumpProgressBar.setCurrentValue(i);
               filebuff.write(buf[i++]);
            }
            dumpProgressBar.dispose();
         }
         else
         {
            while (i < buf.length)
               filebuff.write(buf[i++]);
         }
         filebuff.flush();
         filebuff.close();
         fileStream.flush();
         fileStream.close();
      }
      catch (IOException e)
      {
         String ioExceptionString = e.toString();
         if (ioExceptionString.length() > 200)
            ioExceptionString = e.getMessage().substring(0, 200);

         String optionPaneStringErrors = "Error Writing File: " + outputFileString 
                                         + "\n" + "IOException: "
                                         + ioExceptionString;

         JOptionPane.showMessageDialog(null, optionPaneStringErrors, "Alert",
                                       JOptionPane.ERROR_MESSAGE);
      }
   }

   //==============================================================
   // Creation of the main WriteDataFile class that is called
   // by outside classes to perform the write file routine.
   //==============================================================

   protected static void mainWriteDataString(String outputFileString, byte[] data, boolean showDumpProgressBar)
   {
      WriteDataFile w = new WriteDataFile();
      {
         w.writeDataFileText(outputFileString, data, showDumpProgressBar);
      }
   }
}