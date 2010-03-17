//=================================================================
//       MyJSQLView TableSummaryViewPreferencesPanel
//=================================================================
//
//    This class provides a generic panel used in the Preferences
// Menu to highlight the Table Summary View options top tree
// node
//
//      << TableSummaryViewPreferencesPanel.java >>
//
//=================================================================
// Copyright (C) 2006-2010 Dana M. Proctor
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
// Version 1.0 04/26/2007 Original TableSummaryView Outline Class.
//         1.1 04/28/2007 Renamed to DataPreferencesPanel.
//         1.2 06/18/2007 Added Graphics Background Image.
//         1.3 09/19/2007 Used System.getProperty("file.separator")
//                        for All File System Resources Accesses
//                        Through Instance fileSeparator.
//         1.4 Cleaned Up Javadoc Comments.
//         1.5 Header Update.
//         1.6 05/13/2008 Added Class Instance serialVersionUID.
//                        Declared backgroundImage transient.
//         1.7 10/21/2008 MyJSQLView Project Common Source Code
//                        Formatting.
//         1.8 05/27/2009 Header Format Changes/Update.
//         1.9 10/25/2009 Constructor Instance fileSeparator Obtained From
//                        MyJSQLView_Utils Class.
//         2.0 02/18/2010 Changed Package to Reflect Dandy Made Productions Code.
//
//-----------------------------------------------------------------
//                 danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.*;

/**
 *    The TableSummaryViewPreferencesPanel class provides a generic
 * panel used in the Preferences Menu to highlight the Table Summary
 * View options top tree node.
 * 
 * @author Dana M. Proctor
 * @version 2.0 02/18/2010
 */

class TableSummaryViewPreferencesPanel extends JPanel
{
   // Class Instances.
   private static final long serialVersionUID = 4578215770644473422L;

   private transient Image backgroundImage;
   private int backgroundImageWidth;
   private int backgroundImageHeight;

   //==============================================================
   // TableSummaryViewPreferencesPanel Constructor
   //==============================================================

   protected TableSummaryViewPreferencesPanel()
   {
      // Setting up a file separator instance.
      String fileSeparator = MyJSQLView_Utils.getFileSeparator();

      // Setting up the panel stuff.
      setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(),
                                                   BorderFactory.createLoweredBevelBorder()));

      // Obtaining the image and setting up as needed
      // instances values.
      backgroundImage = new ImageIcon("images" + fileSeparator 
                                      + "SummaryPreferencesPanel.jpg").getImage();
      backgroundImageWidth = backgroundImage.getWidth(null);
      backgroundImageHeight = backgroundImage.getHeight(null);
   }

   //==============================================================
   // Overiding public update method that the panel will not
   // be cleared then refilled.
   //==============================================================

   public void update(Graphics g)
   {
      paint(g);
   }

   //==============================================================
   // Overiding public paint method so that a image may be placed
   // in the background.
   //==============================================================

   public void paint(Graphics g)
   {
      // Class Methods
      int panelWidth, panelHeight;
      int xPosition, yPosition;

      // Panel parameters.
      panelWidth = this.getWidth();
      panelHeight = this.getHeight();

      // Make sure refill background color.
      g.setColor(this.getBackground());
      g.fillRect(0, 0, panelWidth, panelHeight);

      // Draw image in center.
      xPosition = (panelWidth - backgroundImageWidth) / 2;
      if (xPosition < 0)
         xPosition = 0;

      yPosition = (panelHeight - backgroundImageHeight) / 2;
      if (yPosition < 0)
         yPosition = 0;

      g.drawImage(backgroundImage, xPosition, yPosition, this);
   }
}