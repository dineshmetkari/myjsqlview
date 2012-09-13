//=================================================================
//                 MyJSQLView Image Graphics Frame
//=================================================================
//   The ImageGraphicsFrame class provides a canvas/frame that
// can be painted on to display a table item image.
//
//                << ImageGraphicsFrame.java >>
//
//=================================================================
// Copyright (C) 2005-2012 Dana M. Proctor
// Version 2.1 09/11/2012
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
// Version 1.0 12/27/2005 Original MyJSQLView ImageGraphicsFrame Class.
//         1.1 05/29/2006 Removed frameListener.
//         1.2 11/29/2006 Constructor super(String);
//         1.3 12/12/2007 Header Update.
//         1.4 05/14/2008 Added Class Instance serialVersionUID. Declared
//                        itemImage private transient.
//         1.5 10/23/2008 MyJSQLView Project Common Source Code Formatting.
//         1.6 05/27/2009 Header Format Changes/Update.
//         1.7 02/18/2010 Changed Package to Reflect Dandy Made Productions Code.
//         1.8 01/27/2011 Copyright Update.
//         1.9 Copyright Update.
//         2.0 08/19/2012 Organized Imports.
//         2.1 09/11/2012 Changed Package Name to com.dandymadeproductions.myjsqlview.utilities.
//                        Made Class, Constructor, & Methods center(), setImage() Public.
//
//-----------------------------------------------------------------
//                danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview.utilities;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * The ImageGraphicsFrame class provides a canvas/frame that can
 * be painted on to display a table item image.
 * 
 * @author Dana M. Proctor
 * @version 2.1 09/11/2011
 */

public class ImageGraphicsFrame extends JFrame
{
   // Creation of the necessary class instance variables
   // for the JFrame.

   private static final long serialVersionUID = 5520696961286789397L;

   private transient Image itemImage;

   //==============================================================
   // ImageGraphicsFrame Constructor
   //==============================================================

   public ImageGraphicsFrame(String frameSuperString)
   {
      super(frameSuperString);

      JPanel mainPanel = new JPanel();
      mainPanel.setBorder(BorderFactory.createEtchedBorder());
      getContentPane().add(mainPanel);
   }

   //==============================================================
   // Overiding the normal paint sequence for this frame.
   //==============================================================

   public void paint(Graphics g)
   {
      if (itemImage != null)
         g.drawImage(itemImage, 0, 0, null);
   }

   //==============================================================
   // Class method to center the frame.
   //==============================================================

   protected void center()
   {
      Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
      Dimension us = getSize();
      int x = (screen.width - us.width) / 2;
      int y = (screen.height - us.height) / 2;
      setLocation(x, y);
   }

   //==============================================================
   // Class method to set the image that will be painted in this
   // frame.
   //==============================================================

   protected void setImage(Image tableFieldImage)
   {
      itemImage = tableFieldImage;
   }
}