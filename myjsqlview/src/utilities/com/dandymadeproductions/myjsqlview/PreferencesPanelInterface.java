//=================================================================
//            MyJSQLView PreferencesPanelInterface
//=================================================================
//    This class defines the methods that are required by all
// PreferencesPanels in order to properly function within the 
// MyJSQLView application with other classes. PreferencesPanels
// Should All be Animatied Scenes
//
//            << PreferencesPanelInterface.java >>
//
//=================================================================
// Copyright (C) 2007-2010 Dana M. Proctor
// Version 1.4 02/18/2010
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
// Version 1.0 Initial MyJSQLView PreferencesPanelInterface Class.
//         1.1 Header Update.
//         1.2 MyJSQLView Project Common Source Code Formatting.
//         1.3 Header Format Changes/Update.
//         1.4 Changed Package to Reflect Dandy Made Productions Code.
//
//-----------------------------------------------------------------
//                  danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview;

/**
 *    The PreferencesPanelInterface class defines the methods that
 * are required by all PreferencesPanels in order to properly
 * function within the MyJSQLView application with other classes.
 * PreferencesPanels Should All be Animatied Scenes.
 * 
 * @author Dana M. Proctor
 * @version 1.4 02/18/2010
 */

interface PreferencesPanelInterface
{
   //==============================================================
   // Class Method to start and stop the thread.
   //==============================================================

   void setThreadAction(boolean action);

   //==============================================================
   // Class Method to let the thread run() method naturally
   // finish.
   //==============================================================

   void suspendPanel(boolean action);

}