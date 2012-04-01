//=================================================================
//         MyJSQLView GeneralOptionsPreferencesFiller
//=================================================================
//
//    This class provides a generic canvas used in the Preferences
// Menu General Options to provide a generic animated filler graphic.
//
//          << GeneralOptionsPreferencesFiller.java >>
//
//=================================================================
// Copyright (C) 2007-2012 Dana M. Proctor
// Version 1.0 03/31/2012
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
// Version 1.0 03/31/2012 Initial Outlined GeneralOptionsPreferencesFiller Class.
//
//-----------------------------------------------------------------
//                 danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;

/**
 *    The GeneralOptionsPreferencesFiller class provides a generic panel used in
 * the Preferences Menu General Options to provide a generic filler animated graphic.
 * 
 * @author Dana M. Proctor
 * @version 1.0 03/31/2012
 */

class GeneralOptionsPreferencesFiller extends Canvas implements Runnable
{
   // Class Instances.
   private static final long serialVersionUID = 4300192939824055345L;
   
   private transient Image offScreenGraphicsImage;
   private Blossom blossom;
   private boolean restartBlossoms;
   
   private boolean runThread;
   private boolean suspendThread;
   private static final int frameDelay = 30;

   //==============================================================
   // GeneralOptionsPreferencesFiller Constructor
   //==============================================================

   protected GeneralOptionsPreferencesFiller()
   {
      // Class Instances
      Thread t;
      
      // Setup some blossoms to be animated.
      
      blossom = new Blossom();
      restartBlossoms = true;

      // ======================================================
      // Run the panel's thread.

      runThread = true;
      suspendThread = false;

      t = new Thread(this, "GeneralOptionsPreferencesPanelFiller");
      t.start();
   }

   //==============================================================
   // Class method for starting the runnable thread.
   //==============================================================

   public void run()
   {
      // Cycling through the routine
      // to animate the panel.
      while (runThread)
      {
         updateAnimatedObjects();
         render();
         timeStep();
      }
   }

   //==============================================================
   // Class method to update the animated elements.
   //==============================================================

   private void updateAnimatedObjects()
   {
      // Class Method Instances
      
      
   }

   //==============================================================
   // Class method to create a double buffered offscreen graphic.
   //==============================================================

   private void render()
   {
      // Class Instances
      Graphics g2 = (Graphics2D) getGraphics();
      Graphics2D imageGraphics;

      // Clear and redraw the graphics background then
      // draw the component offscreen.
      if (g2 != null)
      {
         Dimension d = getSize();
         if (checkImage(d))
         {
            imageGraphics = (Graphics2D) offScreenGraphicsImage.getGraphics();

            // Draw this component offscreen then to screen.
            paint(imageGraphics);
            g2.drawImage(offScreenGraphicsImage, 0, 0, null);

            imageGraphics.dispose();
         }
         g2.dispose();
      }
   }

   //==============================================================
   // Class method to setup a offscreen image.
   //==============================================================

   private boolean checkImage(Dimension d)
   {
      if (d.width <= 0 || d.height <= 0)
         return false;
      if (offScreenGraphicsImage == null || offScreenGraphicsImage.getWidth(null) != d.width
          || offScreenGraphicsImage.getHeight(null) != d.height)
      {
         offScreenGraphicsImage = createImage(d.width, d.height);
      }
      return true;
   }

   //==============================================================
   // Class method for delaying the animation/framerate change.
   //==============================================================

   private void timeStep()
   {
      try
      {
         Thread.sleep(frameDelay);
         synchronized (this)
         {
            while (suspendThread)
               wait();
         }
      }
      catch (InterruptedException e)
      {
         System.out.println("Process Interrupted.");
      }
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
   // Overiding public paint method so that a the filler animation
   // may be produced.
   //==============================================================

   public void paint(Graphics g)
   {
      // Class Methods
      Graphics2D g2;
      
      int panelWidth, panelHeight;
      
      // Panel parameters.
      panelWidth = this.getWidth();
      panelHeight = this.getHeight();
      
      g2 = (Graphics2D)g;
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      
      // Make sure refill background color at
      // start of new blossoms generation.
      
      if (restartBlossoms)
      {
         g.setColor(Color.BLACK);
         g.fillRect(0, 0, panelWidth, panelHeight);
         restartBlossoms = false;
      }
      
      Point2D currentPoint = new Point();
      currentPoint.setLocation(panelWidth / 2.0, panelHeight / 2.0);
      
      blossom.setPosition(currentPoint);
      blossom.isPetalFilled(true);
      blossom.draw(g2); 
   }
   
   //==============================================================
   // Class Method to start and stop the thread.
   //==============================================================

   protected synchronized void setThreadAction(boolean action)
   {
      suspendThread = action;

      if (!suspendThread)
         notify();
   }

   //==============================================================
   // Class Method to let the thread run() method naturally
   // finish.
   //==============================================================

   protected void suspendPanel(boolean action)
   {
      runThread = !action;
   }
}
