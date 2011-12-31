//=================================================================
//            MyJSQLView PreferencesPanel
//=================================================================
//
//    This class provides the general framework and link to
// the PreferencesPanel Interface inheritance for all
// PreferencesPanels in MyJSQLView. The class is used to
// display varying top preferences panels based on the
// current season.
//
//            << PreferencesPanel.java >>
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
// Version 1.0 Initial PreferencesPanel Class.
//         1.1 Included the Calendar Date to be Created in
//             the Constructor. Class Instances dateString &
//             fontSerifPlain_12.
//         1.2 Header Update.
//         1.3 MyJSQLView Project Common Source Code Formatting.
//         1.4 Header Format Changes/Update.
//         1.5 Changed Package to Reflect Dandy Made Productions Code.
//         1.6 Organized Imports.
//         1.7 Added Class Instance serialVersionUID.
//         1.8 Copyright Update.
//         1.9 Copyright Update.
//        
//-----------------------------------------------------------------
//                 danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview;

import java.awt.Font;
import java.util.Calendar;
import javax.swing.JPanel;

/**
 *    The PreferencesPanel class provides the general framework and
 * link to the PreferencesPanel Interface inheritance for all
 * PreferencesPanels in MyJSQLView. The class is used to display
 * varying top preferences panels based on the current season.
 * 
 * @author Dana M. Proctor
 * @version 1.9 01/01/2012
 */

abstract class PreferencesPanel extends JPanel implements PreferencesPanelInterface
{
   // Class Instances
   private static final long serialVersionUID = -8833294551437322538L;
   
   String dateString;
   Font fontSerifPlain_12;

   //===========================================================
   // PreferencesPanel Constructor
   //===========================================================

   PreferencesPanel()
   {
      super();

      // Constructor Instances
      Calendar calendar;
      int month, day, year;
      String[] months = {"January", "February", "March", "April", "May", "June",
                         "July", "August", "September", "October", "November",
                         "December"};

      // Setting up the calendar instance that will be used
      // to create a date string for all preferences panels.

      calendar = Calendar.getInstance();

      year = calendar.get(Calendar.YEAR);
      month = calendar.get(Calendar.MONTH);
      day = calendar.get(Calendar.DAY_OF_MONTH);

      dateString = months[month] + " " + day + ", " + year;
      fontSerifPlain_12 = new Font("Serif", Font.ITALIC, 12);
   }
}