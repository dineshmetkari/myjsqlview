//=================================================================
//                    Default_JToolBar Class
//=================================================================
//
//    This class is used to construct a default toolbar for the
// MyJSQLView application frame when no toolbar is specified for
// a tab/plugin.
//
//               << Default_JToolBar.java >>
//
//=================================================================
// Copyright (C) 2005-2015 Dana M. Proctor
// Version 1.4 09/11/2012
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
// Version 1.0 Original Default_JToolBar Class.
//         1.1 Copyright Update.
//         1.2 Copyright Update.
//         1.3 Collection of All Image Resources Through resourceBundle.
//         1.4 Changed Package Name to com.dandymadeproductions.myjsqlview.plugin.
//             Made Class & Constructor Public.
//         
//-----------------------------------------------------------------
//                 danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview.plugin;

import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToolBar;

import com.dandymadeproductions.myjsqlview.MyJSQLView;
import com.dandymadeproductions.myjsqlview.utilities.MyJSQLView_Utils;

/**
 *    The Default_JToolBar class is used to construct a default toolbar
 * for the MyJSQLView application frame when no toolbar is specified for
 * a tab/plugin.
 * 
 * @author Dana M. Proctor
 * @version 1.4 09/11/2012
 */

public class Default_JToolBar extends JToolBar
{
   // Instance & Class Fields.
   private static final long serialVersionUID = -7180467032043284772L;

   //==============================================================
   // MyJSQLView_JToolBar Constructor.
   //==============================================================

   public Default_JToolBar(String title)
   {
      super(title);

      // Constructor Instances
      String iconsDirectory;
      JButton buttonItem;
      ImageIcon blankIcon;
      
      // toolbar characteristics and other instances.
      setBorder(BorderFactory.createLoweredBevelBorder());
      setFloatable(false);
      iconsDirectory = MyJSQLView_Utils.getIconsDirectory() + MyJSQLView_Utils.getFileSeparator();      
      buttonItem = null;

      // ===============
      // Add Single Blank
      
      blankIcon = MyJSQLView.getResourceBundle().getResourceImage(iconsDirectory + "blankIcon.png");
      buttonItem = new JButton(blankIcon);
      buttonItem.setBorderPainted(false);
      buttonItem.setFocusable(false);
      buttonItem.setEnabled(false);
      buttonItem.setOpaque(false);
      buttonItem.setMargin(new Insets(0, 0, 0, 0));
      add(buttonItem);
   }
}
