//=================================================================
//            SearchResultTableCellRenderer Class
//=================================================================
//   This class is used to provide a custom table cell renderer
// component used in the SearchFrame JTable.
//
//             << SearchResultTableCellRenderer.java >>
//
//=================================================================
// Copyright (C) 2005-2011 Dana M. Proctor
// Version 1.6 04/03/2011
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
// Version 1.0 MyJSQLView Initial SearchResultTableCellRenderer Class.
//         1.1 Header Format Changes/Update.
//         1.2 Changed Package to Reflect Dandy Made Productions Code.
//         1.3 Updated So Column 1 Gets the HTML Code Link.
//         1.4 Formatted to Conform With Rest of Project & Organized Imports.
//         1.5 Copyright Update.
//         1.6 Minor Comment Change.
//                            
//-----------------------------------------------------------------
//                 danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview;

import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *    The SearchResultTableCellRenderer class is used to provide a custom
 * table cell renderer component used in the SearchFrame JTable.
 * 
 * @author Dana M. Proctor
 * @version 1.6 04/03/2011
 */

class SearchResultTableCellRenderer extends DefaultTableCellRenderer
{
   // Class Instances
   private static final long serialVersionUID = 4561743986207432382L;

   //==============================================================
   // ResultTableCellRender Constructor
   //==============================================================

   protected SearchResultTableCellRenderer()
   {
      super();
   }

   //==============================================================
   // Class Method to get the columns default rendering scheme.
   //==============================================================

   public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                  boolean hasFocus, int row, int column)
   {
      Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
      String text = ((JLabel) c).getText();

      if (column == 1)
         ((JLabel) c).setText("<html><body><a href=\"\" style=\"text-decoration: none; "
                              + "font-weight: bold;\">" + text + "</a><body></html>");
      else
         ((JLabel) c).setHorizontalAlignment(JLabel.CENTER);

      return c;
   }
}