//=================================================================
//           MyJSQLView PreferencesPanelSpring
//=================================================================
//
//    This class provides a generic panel used in the Preferences
// Menu to highlight the top tree element during the northern
// hemisphere's spring months, May-June.
//
//           << PreferencesPanelSpring.java >>
//
//=================================================================
// Copyright (C) 2007-2010 Dana M. Proctor
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
// Version 1.0 01/05/2009 Initial PreferencesPanelSpring Class.
//         1.1 05/25/2009 Finalized the Panel By Implemented the Animated Images
//                        & Media Play.
//         1.2 05/26/2009 Modified the Position of the Location of currentOwlImage
//                        in the paint() Method. Other Minor Comment Changes.
//         1.3 05/27/2009 Added An Additional Owl Image to the owlImagesNumber
//                        and Array.
//         1.6 06/12/2009 Class Method checkImage() Dimension Conditional For Less
//                        Than or Equal.
//         1.7 09/03/2009 Comment Changes and owlSoundClip Closing in Method
//                        suspendPanel().
//         1.8 10/12/2009 Removed Sound Implementation for Panel. To Volatile.
//         1.9 10/25/2009 Obtained Constructor Instance fileSeparator From MyJSQLView_Utils
//                        Class.
//         2.0 02/18/2010 Changed Package to Reflect Dandy Made Productions Code.
//
//-----------------------------------------------------------------
//                 danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview;

import java.awt.*;
import java.awt.Image;
import java.util.*;
import javax.swing.*;

/**
 *    The PreferencesPanelSpring class provides a generic panel used
 * in the Preferences Menu to highlight the top tree element during
 * the northern hemisphere's spring months, May-June.
 * 
 * @author Dana M. Proctor
 * @version 2.0 02/18/2010
 */

class PreferencesPanelSpring extends PreferencesPanel implements Runnable
{
   // Class Instances.
   private static final long serialVersionUID = -976830171242087875L;

   private transient Image backgroundImage;
   private int backgroundImageWidth, backgroundImageHeight;
   private transient Image offScreenGraphicsImage;

   private static final int owlImagesNumber = 4;
   private transient Image[] owlImages = new Image[owlImagesNumber];
   private transient Image currentOwlImage;
   
   private volatile boolean runThread;
   private volatile boolean suspendThread;
   private static final int frameDelay = 3000;

   //==============================================================
   // PreferencesPanelSpring Constructor
   //==============================================================

   protected PreferencesPanelSpring()
   {
      // Class Instances
      Thread t;
      String fileSeparator;
      String[] owlImageName = {"owl1.gif", "owl2.gif", "owl3.gif", "owl4.gif"};

      // Setting up the panel stuff.
      setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(),
                                                   BorderFactory.createLoweredBevelBorder()));

      // ==========================================================
      // Obtaining the background image and setting up as
      // needed instances values.

      fileSeparator = MyJSQLView_Utils.getFileSeparator();

      backgroundImage = new ImageIcon("images" + fileSeparator + "PreferencesPanelSpring.jpg").getImage();
      backgroundImageWidth = backgroundImage.getWidth(null);
      backgroundImageHeight = backgroundImage.getHeight(null);

      // ===========================================================
      // Obtaing the animated panel images and setting up as needed.

      for (int i = 0; i < owlImagesNumber; i++)
         owlImages[i] = new ImageIcon("images" + fileSeparator + owlImageName[i]).getImage();
      
      // Run the panel's thread.
      runThread = true;
      suspendThread = false;

      t = new Thread(this, "PreferencesPanel");
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
         updateOwlImage();
         render();
         timeStep();
      }
   }

   //==============================================================
   // Class method to update the iamge that is used for the owl in
   // the panel.
   //==============================================================

   private void updateOwlImage()
   {
      // Class Method Instances
      Random randomNumber;
      
      randomNumber = new Random(System.currentTimeMillis());
      currentOwlImage = owlImages[Math.abs(randomNumber.nextInt() % 4)];
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
      if (offScreenGraphicsImage == null ||
                                    offScreenGraphicsImage.getWidth(null) != d.width ||
                                    offScreenGraphicsImage.getHeight(null) != d.height)
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

      // Draw the current date.
      g.setFont(fontSerifPlain_12);
      g.drawString(dateString, 10, 20);
      
      // Draw the animated owl gif.
      g.drawImage(currentOwlImage, 128, 77, this);
   }

   //==============================================================
   // Class Method to start and stop the thread.
   //==============================================================

   public synchronized void setThreadAction(boolean action)
   {
      suspendThread = action;
      if (!suspendThread)
         notify();
   }

   //==============================================================
   // Class Method to let the thread run() method naturally
   // finish.
   //==============================================================

   public void suspendPanel(boolean action)
   {
      runThread = !action;
   }
}