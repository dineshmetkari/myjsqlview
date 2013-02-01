//=================================================================
//       PopupMenu Mouse Events Class for the MyJSQLView
//=================================================================
//
//    This class provides an extension of the Mouse Adapter so
// that specfic mouse listener interfaces can be implemented for
// various panels in the MyJSQLView application. Mouse events
// for MyJSQLView that relate to a right mouse press/release
// popup menu are executed here.
//
//             << MyJSQLView_MouseAdapter.java >>
//
//=================================================================
// Copyright (C) 2005-2013 Dana M. Proctor
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
// Revision History
// Changes to the code should be documented here and reflected
// in the present version number. Author information should
// also be included with the original copyright author.
//=================================================================
// Version 1.0 Original MyJSQLView Mouse Adapter.
//         1.1 Cleaned Up Javadoc Comments.
//         1.2 Header Update.
//         1.3 MyJSQLView Project Common Source Code Formatting.
//         1.4 Additional Standardation to Comments.
//         1.5 Header Format Changes/Update.
//         1.6 Changed Package to Reflect Dandy Made Productions Code.
//         1.7 Organized Imports.
//         1.8 Copyright Update.
//         1.9 Copyright Update.
//         2.0 Changed Package Name to com.dandymadeproductions.myjsqlview.gui.
//             Made Class, Constructor & showPopup() Public.
//         
//-----------------------------------------------------------------
//                 danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview.gui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPopupMenu;

/**
 *    The MyJSQLView_MouseAdapter class provides an extension of the Mouse
 * Adapter so that specfic mouse listener interfaces can be implemented
 * for various panels in the MyJSQLView application. Mouse events for
 * MyJSQLView that relate to a right mouse press/release popup menu are
 * executed here.
 * 
 * @author Dana M. Proctor
 * @version 2.0 09/11/2012
 */

public class MyJSQLView_MouseAdapter extends MouseAdapter
{
   // Instances
   JPopupMenu popup;

   //==============================================================
   // MyJSQLView_MouseAdapter Constructor
   //==============================================================

   public MyJSQLView_MouseAdapter(JPopupMenu myJSQLViewPopUp)
   {
      popup = myJSQLViewPopUp;
   }

   //==============================================================
   // MouseEvent Listener methods for detecting the user
   // right clicking the mouse within the various panels.
   //==============================================================

   public void mousePressed(MouseEvent evt)
   {
      showPopUp(evt);
   }

   public void mouseReleased(MouseEvent evt)
   {
      showPopUp(evt);
   }

   //==============================================================
   // Class method to show the IDMS's popup menu.
   //==============================================================

   public void showPopUp(MouseEvent e)
   {
      if (e.isPopupTrigger())
         popup.show(e.getComponent(), e.getX(), e.getY());
   }
}
