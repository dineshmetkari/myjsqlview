//=================================================================
//               MyJSQLView DataTableDumpThread
//=================================================================
//
//    This class provides a thread to safely dump a TableTabPanel
// summary table data to a local file. A status dialog with cancel
// is provided to allow the ability to prematurely terminate the
// dump.
//
//                << DataTableDumpThread.java >>
//
//=================================================================
// Copyright (C) 2007-2012 Dana M. Proctor
// Version 3.3 09/21/2012
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
// Version 1.0 Original DataTableDumpThread Class.
//         1.1 Header Update.
//         1.2 MyJSQLView.getDataExportProperties().
//         1.3 Added Class Instance tableColumnNamesHashMap. Also Same to
//             Arguments to Constructor. Output Summary Table List Headers
//             as Database Field Name.
//         1.4 Deliminator to Delimiter.
//         1.5 MyJSQLView Project Common Source Code Formatting.
//         1.6 Class Method run() getDataExportProperites Changed Over to
//             the MyJSQLView_Frame Class.
//         1.7 Changed MyJSQLView_Frame.getDatabaseExportProperties() Method
//             Moved Over to the DBTablesPanel.
//         1.8 Added Formatting of Date Fields in run() Method. Added Instances
//             tableColumnTypeHashMap & currentType. Constructor Argument
//             tableColumnTypeHashMap Addition.
//         1.9 Changed Method Instance currentTableName in run() to
//             currentTableFieldName.
//         2.0 Changed Package to Reflect Dandy Made Productions Code.
//         2.1 Conditional Check in run() Between (i < rowNumber) and
//             dumpProgressBar.isCanceled() to Short-Circuit &&. Organized
//             imports.
//         2.2 Class Method run() Changed Instance currentEntry to Type
//             StringBuffer.
//         2.3 Correction in Conparison of currentEntry to length() !=0 
//             Instead of String Equality in Class Method run().
//         2.4 Parameterized Class Instances tableColumnNamesHashMap and
//             tableColumnTypeHashMap in Addition to Arguments to Bring
//             Code Into Compliance With Java 5.0 API.
//         2.5 Thread Identifier String Changes in Constructor and Minor 
//             Changes in run().
//         2.6 Corrected Conditional Check for currentType Date to UpperCase.
//         2.7 Class Method run() Change in MyJSQLView_Utils.formatCSVExportDateString()
//             to formatExportDateString().
//         2.8 Class Method run() Change in the Handling of Date, DateTime,
//             and Timestamp Output, By Formatting Through New Routines in
//             MyJSQLView_Utils.
//         2.9 Change in currentEntry Clipping by the delimiterString.length in run().
//         3.0 Copyright Update.
//         3.1 Exclusion of Array Types From Date, DateTime Conversions in run() &
//             Some Minor Formatting Changes.
//         3.2 Changed Package Name to com.dandymadeproductions.myjsqlview.io.
//             Made Class, & Constructor Public.
//         3.3 Removal of Starting the Panel's Runnable Thread in the Constructor.
//             
//-----------------------------------------------------------------
//                    danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview.io;

import java.util.HashMap;

import javax.swing.JTable;

import com.dandymadeproductions.myjsqlview.gui.panels.DBTablesPanel;
import com.dandymadeproductions.myjsqlview.utilities.MyJSQLView_ProgressBar;
import com.dandymadeproductions.myjsqlview.utilities.MyJSQLView_Utils;

/**
 *    The DataTableDumpThread class provides a thread to safely
 * dump a TableTabPanel summary table data to a local file. A
 * status dialog with cancel is provided to allow the ability to
 * prematurely terminate the dump.
 * 
 * @author Dana M. Proctor
 * @version 3.3 09/21/2012
 */

public class DataTableDumpThread implements Runnable
{
   // Class Instances
   private JTable summaryListTable;
   private HashMap<String, String> tableColumnNamesHashMap;
   private HashMap<String, String> tableColumnTypeHashMap;
   private String exportedTable, fileName;

   //==============================================================
   // DataDumpThread Constructor.
   //==============================================================

   public DataTableDumpThread(JTable summaryListTable, HashMap<String, String> tableColumnNamesHashMap,
                              HashMap<String, String> tableColumnTypeHashMap, String exportedTable,
                              String fileName)
   {
      this.summaryListTable = summaryListTable;
      this.tableColumnNamesHashMap = tableColumnNamesHashMap;
      this.tableColumnTypeHashMap = tableColumnTypeHashMap;
      this.exportedTable = exportedTable;
      this.fileName = fileName;
   }

   //==============================================================
   // Class Method for Normal Start of the Thread.
   //==============================================================

   public void run()
   {
      // Class Method Instances
      MyJSQLView_ProgressBar dumpProgressBar;
      HashMap<String, String> summaryListTableNameTypes;
      StringBuffer currentEntry;
      String currentTableFieldName, delimiterString;
      String currentType, currentString;
      int rowNumber;

      // Setting up
      summaryListTableNameTypes = new HashMap <String, String>();
      delimiterString = DBTablesPanel.getDataExportProperties().getDataDelimiter();
      currentEntry = new StringBuffer();

      // Constructing progress bar.
      rowNumber = summaryListTable.getRowCount();
      dumpProgressBar = new MyJSQLView_ProgressBar(exportedTable + " Dump");
      dumpProgressBar.setTaskLength(rowNumber);
      dumpProgressBar.pack();
      dumpProgressBar.center();
      dumpProgressBar.setVisible(true);

      // Collecting Table Headers, Column Fields.
      for (int i = 0; i < summaryListTable.getColumnCount(); i++)
      {
         currentTableFieldName = summaryListTable.getColumnName(i);
         currentEntry.append(tableColumnNamesHashMap.get(currentTableFieldName) + delimiterString);
         summaryListTableNameTypes.put(Integer.toString(i),
                                       tableColumnTypeHashMap.get(currentTableFieldName));
      }
      if (currentEntry.length() != 0)
      {
         currentEntry.delete((currentEntry.length() - delimiterString.length()), currentEntry.length());
         currentEntry.append("\n");
      }

      int i = 0;
      while ((i < rowNumber) && !dumpProgressBar.isCanceled())
      {
         dumpProgressBar.setCurrentValue(i);

         // Collecting rows of data & formatting date & timestamps
         // as needed according to the CSV Export Properties.

         if (summaryListTable.getValueAt(i, 0) != null)
         {
            for (int j = 0; j < summaryListTable.getColumnCount(); j++)
            {
               currentString = summaryListTable.getValueAt(i, j) + "";
               currentString = currentString.replaceAll("\n", "");
               currentString = currentString.replaceAll("\r", "");

               // Format Date & Timestamp fields as needed. PostgreSQL
               // Array Timestamps to NOT be processed, identified by
               // underscore.
               currentType = summaryListTableNameTypes.get(Integer.toString(j));

               if ((currentType != null)
                   && (currentType.equals("DATE") || currentType.equals("DATETIME")
                       || (currentType.indexOf("TIMESTAMP") != -1) && currentType.indexOf("_") == -1))
               {
                  if (!currentString.toLowerCase().equals("null"))
                  {
                     int firstSpace;
                     String time;

                     // Dates fall through DateTime and Timestamps try
                     // to get the time separated before formatting
                     // the date.

                     if (currentString.indexOf(" ") != -1)
                     {
                        firstSpace = currentString.indexOf(" ");
                        time = currentString.substring(firstSpace);
                        currentString = currentString.substring(0, firstSpace);
                     }
                     else
                        time = "";

                     currentString = MyJSQLView_Utils.convertViewDateString_To_DBDateString(currentString,
                        DBTablesPanel.getGeneralProperties().getViewDateFormat());
                     currentString = MyJSQLView_Utils.convertDBDateString_To_ViewDateString(currentString,
                        DBTablesPanel.getDataExportProperties().getCSVDateFormat()) + time;
                  }
               }
               currentEntry.append(currentString + delimiterString);
            }
            currentEntry.delete((currentEntry.length() - delimiterString.length()), currentEntry.length());
            currentEntry.append("\n");
         }
         i++;
      }
      dumpProgressBar.dispose();

      // Outputting Summary Table to Selected File.
      WriteDataFile.mainWriteDataString(fileName, (currentEntry.toString()).getBytes(), false);
   }
}