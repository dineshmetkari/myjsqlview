//=================================================================
//                 MyJSQLView GraphicsCanvasPanel
//=================================================================
//
//    This class provides a generic panel used to paint a supplied
// image as its main component.
//
//                   << GraphicsCanvasPanel.java >>
//
//=================================================================
// Copyright (C) 2005-2015 Dana M. Proctor
// Version 1.4 09/10/2012
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
// Version 1.0 09/10/2010 Original GraphicsCanvasPanel Class.
//         1.1 01/27/2011 Copyright Update.
//         1.2 01/01/2012 Copyright Update.
//         1.3 08/19/2012 Collection of All Image Resources Through resourceBundle.
//         1.4 09/10/2012 Changed Package Name to com.dandymadeproductions.myjsqlview.gui.panels.
//             Made Class, & Constructor Public.
//
//-----------------------------------------------------------------
//                 danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview.gui.panels;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import com.dandymadeproductions.myjsqlview.MyJSQLView;
import com.dandymadeproductions.myjsqlview.utilities.MyJSQLView_ResourceBundle;
import com.dandymadeproductions.myjsqlview.utilities.MyJSQLView_Utils;

/**
 *    The GraphicsCanvasPanel provides a generic panel used to paint a
 * supplied image as its main component.
 * 
 * @author Dana M. Proctor
 * @version 1.3 08/19/2012
 */

public class GraphicsCanvasPanel extends JPanel
{
   // Class Instances
   private static final long serialVersionUID = -4394212581609505075L;
   
   private transient Image backgroundImage;
   private int backgroundImageWidth;
   private int backgroundImageHeight;

   //==============================================================
   // GraphicsCanvasPanel Constructor
   //==============================================================

   public GraphicsCanvasPanel(String imageFileNameString)
   {
      // Constructor Instances
      MyJSQLView_ResourceBundle resourceBundle = MyJSQLView.getResourceBundle();
      String fileSeparator = MyJSQLView_Utils.getFileSeparator();
      
      // Setting up the panel stuff.
      setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(),
                BorderFactory.createLoweredBevelBorder()));

      // Obtaining the image and setting up as needed
      // instances values.
      backgroundImage = resourceBundle.getResourceImage("images"
                                                        + fileSeparator + imageFileNameString).getImage();
      backgroundImageWidth = backgroundImage.getWidth(null);
      backgroundImageHeight = backgroundImage.getHeight(null);
   }
   
   //==============================================================
   // Class method to overide the standard panel paintComponents
   // routine.
   //==============================================================
   
   public void paintComponent(Graphics g)
   {
      super.paintComponent(g);
      
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
