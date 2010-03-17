//=================================================================
//           MyJSQLView PreferencesPanelWinter
//=================================================================
//
//    This class provides a generic panel used in the Preferences
// Menu to highlight the top tree element during the northern
// hemisphere's winter months, December-February.
//
//           << PreferencesPanelWinter.java >>
//
//=================================================================
// Copyright (C) 2007-2010 Dana M. Proctor
// Version 2.1 02/18/2010
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
// Version 1.0 11/23/2007 Initial PreferencesPanelWinter Class.
//         1.1 11/24/2007 Relinquished Control of Calendar Date Creation to
//                        Parent PreferencesPanel.
//         1.2 12/12/2007 Header Update.
//         1.3 05/13/2008 Added Class Instance serialVersionUID. Declared
//                        Instances backgroundImage, offScreenGraphicsImage,
//                        & snowFlakeImage.
//         1.4 10/21/2008 MyJSQLView Project Common Source Code Formatting.
//         1,5 12/16/2008 Class Methods updateSnowFlakes(), render(), checkImage(),
//                        and timeStep() Made Private.
//         1.6 05/25/2009 Check on Constructor Instance fileSeparator.
//         1.7 06/12/2009 Class Method checkImage() Dimension Conditional For
//                        Less Than or Equal.
//         1.8 09/04/2009 Implemented Random windSoundClip in Panel. Constructor
//                        Loading, and run() Method playMedia control. Status of
//                        Sound Clip Control for Panel in setThreadAction() and
//                        suspendPanel().
//         1.9 10/12/2009 Removed Sound Implementation for Panel. To Volatile.
//         2.0 10/25/2009 Obtained Constructor Instance fileSeparator From MyJSQLView_Utils
//                        Class.
//         2.1 02/18/2010 Changed Package to Reflect Dandy Made Productions Code.
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
 *    The PreferencesPanelWinter class provides a generic panel used
 * in the Preferences Menu to highlight the top tree element during
 * the northern hemisphere's winter months, December-February.
 * 
 * @author Dana M. Proctor
 * @version 2.1 02/18/2010
 */

class PreferencesPanelWinter extends PreferencesPanel implements Runnable
{
   // Class Instances.
   private static final long serialVersionUID = 1655048617297205169L;

   private transient Image backgroundImage;
   private int backgroundImageWidth, backgroundImageHeight;
   private transient Image offScreenGraphicsImage;

   private transient Image snowFlakeImage;
   private int snowFlakeImageWidth, snowFlakeImageHeight;
   private Vector snowFlakes;
   
   private volatile boolean runThread;
   private volatile boolean suspendThread;
   private static final int frameDelay = 40;

   //==============================================================
   // PreferencesPanelWinter Constructor
   //==============================================================

   protected PreferencesPanelWinter()
   {
      // Class Instances
      Thread t;
      String fileSeparator;

      // Setting up the panel stuff.
      setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(),
                                                   BorderFactory.createLoweredBevelBorder()));

      // ==========================================================
      // Obtaining the background image and setting up as
      // needed instances values.

      fileSeparator = MyJSQLView_Utils.getFileSeparator();

      backgroundImage = new ImageIcon("images" + fileSeparator 
                                      + "PreferencesPanelWinter.jpg").getImage();
      backgroundImageWidth = backgroundImage.getWidth(null);
      backgroundImageHeight = backgroundImage.getHeight(null);

      // ===========================================================
      // Obtaing the snowflake image and setting up as needed.

      snowFlakeImage = new ImageIcon("images" + fileSeparator 
                                     + "snowflake.gif").getImage();
      snowFlakeImageWidth = snowFlakeImage.getWidth(null);
      snowFlakeImageHeight = snowFlakeImage.getHeight(null);
      
      snowFlakes = new Vector();

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
      // Class Instances
      int snowFlakeCount;
      Random randomNumber;
      Rectangle panelBounds;

      // Create snowflakes from the snowflake image file.
      snowFlakeCount = 75;
      randomNumber = new Random(System.currentTimeMillis());
      panelBounds = new Rectangle(0, 0, backgroundImageWidth, backgroundImageHeight);

      for (int i = 0; i < snowFlakeCount; i++)
      {
         Point currentSnowFlakePosition = getEmptyPosition();

         snowFlakes.add(new SnowFlake(this, panelBounds, snowFlakeImage,
                                      currentSnowFlakePosition,
                                      new Point(randomNumber.nextInt() % 4, randomNumber.nextInt() % 4)));
      }

      // Cycling through the routine
      // to animate the panel.
      while (runThread)
      {
         updateSnowFlakes();
         render();
         timeStep();
      }
   }

   //==============================================================
   // Class method to obtain an empty postion in the panel that
   // snowflake may be placed.
   //==============================================================

   private Point getEmptyPosition()
   {
      // Class Method Instances
      Rectangle trialSpaceOccupied;
      Random randomNumber;
      boolean empty, collision;
      int numberOfTries;

      // Setting up.
      trialSpaceOccupied = new Rectangle(0, 0, snowFlakeImageWidth, snowFlakeImageHeight);
      randomNumber = new Random(System.currentTimeMillis());
      empty = false;
      numberOfTries = 0;

      // Begin the search for an empty position
      while (!empty && numberOfTries++ < 100)
      {
         // Obtain a random postion.
         trialSpaceOccupied.x = Math.abs(randomNumber.nextInt() % backgroundImageWidth);
         trialSpaceOccupied.y = Math.abs(randomNumber.nextInt() % backgroundImageHeight);

         // Check to see if an existing snowflake occupies
         // the randomly selected postion.
         collision = false;
         for (int i = 0; i < snowFlakes.size(); i++)
         {
            Rectangle testSpaceOccupied = ((SnowFlake) (snowFlakes.elementAt(i))).getSpaceOccupied();
            if (trialSpaceOccupied.intersects(testSpaceOccupied))
               collision = true;
         }
         empty = !collision;
      }
      // Return the empty postion.
      return new Point(trialSpaceOccupied.x, trialSpaceOccupied.y);
   }

   //==============================================================
   // Class method to update the snowflakes' positions in the panel.
   //==============================================================
   
   private void updateSnowFlakes()
   {
      // Class Method Instances
      SnowFlake currentSnowFlake;
      int snowFlakeOccupiedIndex;
      Point tempSwapPoint;

      // Cycle through the snowflakes, updating postion and
      // testing for collision.
      for (int i = 0; i < snowFlakes.size(); i++)
      {
         currentSnowFlake = (SnowFlake) snowFlakes.elementAt(i);
         currentSnowFlake.updatePosition();

         // Collision check and recoil action as needed.
         snowFlakeOccupiedIndex = testForCollision(currentSnowFlake);

         if (snowFlakeOccupiedIndex >= 0)
         {
            tempSwapPoint = currentSnowFlake.getNextPosition();
            currentSnowFlake.setNextPosition(
               ((SnowFlake) snowFlakes.elementAt(snowFlakeOccupiedIndex)).getNextPosition());
            ((SnowFlake) snowFlakes.elementAt(snowFlakeOccupiedIndex)).setNextPosition(tempSwapPoint);
         }
      }
   }
   
   //==============================================================
   // Class Method to check if the input SnowFlake collides with
   // any of the other snowflakes. If does return the index in the
   // vector of offending snowflake, else -1.
   //==============================================================

   private int testForCollision(SnowFlake testSnowFlake)
   {
      // Class Method Instances.
      SnowFlake currentSnowFlake;

      // Cycle through the snowflakes, checking against input
      // snowflake.
      for (int i = 0; i < snowFlakes.size(); i++)
      {
         currentSnowFlake = (SnowFlake) snowFlakes.elementAt(i);

         // Don't need to check itself.
         if (currentSnowFlake == testSnowFlake)
            continue;

         if (testSnowFlake.testCollision(currentSnowFlake))
            return i;
      }
      return -1;
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

      // Draw SnowFlakes
      for (int i = 0; i < snowFlakes.size(); i++)
      {
         g.drawImage(((SnowFlake) snowFlakes.elementAt(i)).getImage(),
                     ((SnowFlake) snowFlakes.elementAt(i)).getSpaceOccupied().x,
                     ((SnowFlake) snowFlakes.elementAt(i)).getSpaceOccupied().y, this);
      }
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

   public synchronized void suspendPanel(boolean action)
   {
      runThread = !action;
   }
}