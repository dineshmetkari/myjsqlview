//=================================================================
//              MyJSQLView_MenuActionCommands Class
//=================================================================
//
//    This class is used to provide a commom access point for
// allocation of Action Commands used by the MyJSQLView menu system.
//
//            << MyJSQLView_MenuActionCommands.java >>
//
//=================================================================
// Copyright (C) 2005-2010 Dana M. Proctor.
// Version 1.1 06/10/2010
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
// Version 1.0 Original MyJSQLView_MenuActionCommnds Class.
//         1.1 Added Interface Instance ACTION_EXPORT_PDF_SUMMARY_TABLE.
//         
//-----------------------------------------------------------------
//                 danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview;

/**
 *    The MyJSQLView_MenActionCommands class is used to provide a commom
 * access point for allocation of Action Commands used by the MyJSQLView
 * menu system.  
 * 
 * @author Dana M. Proctor
 * @version 1.1 06/10/2010
 */

public interface MyJSQLView_MenuActionCommands
{
   // ===========
   // File Menu
   
   // Save
   public static final String ACTION_OPEN = "FO";
   public static final String ACTION_SAVE = "FS";
   public static final String ACTION_SAVE_AS = "FSA";
   
   // Print
   public static final String ACTION_PRINT = "FP";
   public static final String ACTION_PAGE_FORMAT = "FPG";

   // Exit
   public static final String ACTION_EXIT = "FE";
   
   // ===========
   // Edit Menu
   
   // Preferences
   public static final String ACTION_PREFERENCES = "EP";
   
   // ===========
   // Data Menu
   
   // Import
   public static final String ACTION_IMPORT_SQL_DUMP = "DISQLD";
   public static final String ACTION_IMPORT_CSV_FILE = "DICSVF";
   
   // Export
   public static final String ACTION_EXPORT_CSV_TABLE = "DECSVT";
   public static final String ACTION_EXPORT_CSV_SUMMARY_TABLE = "DECSVST";
   public static final String ACTION_EXPORT_PDF_SUMMARY_TABLE = "DEPDFST";
   public static final String ACTION_EXPORT_SQL_TABLE = "DESQLT";
   public static final String ACTION_EXPORT_SQL_SUMMARY_TABLE = "DESQLST";
   public static final String ACTION_EXPORT_SQL_DATABASE = "DESQLD";
   public static final String ACTION_EXPORT_SQL_DATABASE_SCHEME = "DESQLDS";
   
   // ===========
   // Tools Menu
   
   // Query Frame
   public static final String ACTION_QUERY_FRAME = "TQF";
   
   // Reload Database
   public static final String ACTION_RELOAD_DATABASE = "TRD";
   
   // Search Database
   public static final String ACTION_SEARCH_DATABASE = "TSD";
   
   // ===========
   // Help Menu
   
   // Manual
   public static final String ACTION_MANUAL = "HM";
   
   // Legal & Release Notes
   public static final String ACTION_LEGAL = "HL";
   public static final String ACTION_RELEASE_NOTES = "HRN";
   
   // About
   public static final String ACTION_ABOUT = "HA";  
   
   // ===========
   // MySQL Flush Button
   
   public static final String ACTION_FLUSH = "FLUSH";  
}