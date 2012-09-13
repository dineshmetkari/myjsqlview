//=================================================================
//                  MyJSQLView Print Manager
//=================================================================
//
//    This class provides the mechanism to print various
// MyJSQLView data panes using the Java Print Service API.  
//
//                  << PrintManager.java >>
//
//=================================================================
// Copyright (C) 2005-2012 Dana M. Proctor
// Version 2.0 09/11/2012
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
//Revision History
//Changes to the code should be documented here and reflected
//in the present version number. Author information should
//also be included with the original copyright author.
//=================================================================
// Version 1.0 Original Print Manager Class.
//         1.1 Main Class Name Change to MyJSQLView.
//         1.3 Cleaned Up Javadoc Comments.
//         1.4 Header Update.
//         1.5 MyJSQLView Project Common Source Code Formatting.
//         1.6 Header Format Changes/Update.
//         1.7 Changed Package to Reflect Dandy Made Productions Code.
//         1.8 Copyright Update.
//         1.9 Copyright Update.
//         2.0 Changed Package Name to com.dandymadeproductions.myjsqlview.utilities.
//             Made Class & Constructor Public.
//
//-----------------------------------------------------------------
//                  danap@dandymadeproductions.com 
//=================================================================

package com.dandymadeproductions.myjsqlview.utilities;

import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import com.dandymadeproductions.myjsqlview.MyJSQLView;

/**
 *    This PrintManager class provides the mechanism to print
 * various MyJSQLView data panes using the Java Print Service API.
 * 
 * @author Dana Proctor
 * @version 2.0 09/11/2012
 */

public class PrintManager extends MyJSQLView
{
   //==============================================================
   // Main method that is called from the MyJSQLView Main Frame
   // JMenuBar when the print option is implemented. Set up
   // necessary aspect of the printing.
   //==============================================================

   public static void printPage() throws PrinterException
   {
      PrinterJob printJob = PrinterJob.getPrinterJob();
      printJob.setJobName("MyJSQLView");
      printJob.setCopies(1);
      // printJob.setPrintable();

      if (printJob.printDialog())
      {
         printJob.print();
      }
   }
}