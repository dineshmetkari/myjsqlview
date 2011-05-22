//=================================================================
//           SQLQueryBucketListListCellRenderer Class
//=================================================================
//   This class is used to provide a custom list cell renderer
// component used in the SQLQueryBucketFrame JList.
//
//            << SQLQueryBucketListCellRenderer.java >>
//
//=================================================================
// Copyright (C) 2005-2011 Dana M. Proctor
// Version 1.2 05/22/2011
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
// Version 1.0 MyJSQLView Initial SQLQueryBucketListCellRenderer Class.
//         1.1 Class Method getListCellRendererComponent() Added Instances
//             buttonColor, & buttonFont. Set background() in Same.
//         1.2 Set the Font to BOLD in getListCellRendererComponent().
//                            
//-----------------------------------------------------------------
//                 danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JList;
import javax.swing.ListCellRenderer;


/**
 *    The SQLQueryBucketListCellRenderer class is used to provide a custom
 * list cell renderer component used in the SQLQueryBucketFrame JList.
 * 
 * @author Dana M. Proctor
 * @version 1.2 05/22/2011
 */

class SQLQueryBucketListCellRenderer extends SQLQueryBucketListObject implements ListCellRenderer
{
   // Class Instances
   private static final long serialVersionUID = 7335900455732034997L;

   //==============================================================
   // SQLQueryBucketListCellRender Constructor
   //==============================================================

   protected SQLQueryBucketListCellRenderer()
   {
      super();
   }

   //==============================================================
   // Class Method to set the list cell default rendering scheme
   // for a SQLQueryBucketListObject.
   //==============================================================

   public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
                                                 boolean cellHasFocus)
   {
      // Method Instances.
      String buttonTextLabel;
      Color buttonColor;
      Font buttonFont;
      
      // Set label, background, selection highlight, & font.
      
      buttonTextLabel = ((SQLQueryBucketListObject) value).getText();
      buttonColor = ((SQLQueryBucketListObject) value).getBackground();
      buttonFont = list.getFont();
      
      setText(buttonTextLabel);
      setBackground(buttonColor);

      if (isSelected)
      {
         setBorder(BorderFactory.createLineBorder(list.getSelectionForeground(), 1));
      }
      else
      {
         setBorder(((SQLQueryBucketListObject) value).getBorder());
      }

      setEnabled(list.isEnabled());
      setFont(buttonFont.deriveFont(Font.BOLD));
      setOpaque(true);
      return this;
   }
}