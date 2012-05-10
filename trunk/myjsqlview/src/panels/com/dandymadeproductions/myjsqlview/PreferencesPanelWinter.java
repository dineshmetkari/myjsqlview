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
// Copyright (C) 2007-2012 Dana M. Proctor
// Version 2.7 05/10/2012
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
//         1.5 12/16/2008 Class Methods updateSnowFlakes(), render(), checkImage(),
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
//         2.2 05/18/2010 Parameterized Class Instance snowFlakes to Bring Code Into
//                        Compliance With Java 5.0 API. Organized Imports.
//         2.3 10/18/2010 Updated to Have Rendering Done With the paintComponent() Method
//                        for Panels Instead of paint(). Added paintComponent() and
//                        Changed paint() to drawPanel(). Removed Use of Graphics2D in
//                        render() and removed Setting of Border in Constructor.
//         2.4 11/15/2010 Changed paintComponent() to public and drawPanel() to private.
//         2.5 01/27/2011 Copyright Update.
//         2.6 01/01/2012 Copyright Update.
//         2.7 05/10/2112 Changed Class Instance snowFlakes from Vector Data Type to
//                        ArrayList.
//         
//-----------------------------------------------------------------
//                 danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Random;
import java.util.ArrayList;
import javax.swing.ImageIcon;

/**
 *    The PreferencesPanelWinter class provides a generic panel used
 * in the Preferences Menu to highlight the top tree element during
 * the northern hemisphere's winter months, December-February.
 * 
 * @author Dana M. Proctor
 * @version 2.7 05/10/2012
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
   private ArrayList<SnowFlake> snowFlakes;
   
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
      
      snowFlakes = new ArrayList <SnowFlake>();

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
            Rectangle testSpaceOccupied = ((snowFlakes.get(i))).getSpaceOccupied();
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
         currentSnowFlake = snowFlakes.get(i);
         currentSnowFlake.updatePosition();

         // Collision check and recoil action as needed.
         snowFlakeOccupiedIndex = testForCollision(currentSnowFlake);

         if (snowFlakeOccupiedIndex >= 0)
         {
            tempSwapPoint = currentSnowFlake.getNextPosition();
            currentSnowFlake.setNextPosition(
               (snowFlakes.get(snowFlakeOccupiedIndex)).getNextPosition());
            (snowFlakes.get(snowFlakeOccupiedIndex)).setNextPosition(tempSwapPoint);
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
         currentSnowFlake = snowFlakes.get(i);

         // Don't need to check itself.
         if (currentSnowFlake == testSnowFlake)
            continue;

         if (testSnowFlake.testCollision(currentSnowFlake))
            return i;
      }
      return -1;
   }

   //==============================================================
   // Class method to create a double buffered offscreen graphic
   // then rendering to the screen.
   //==============================================================

   private void render()
   {
      // Check then draw the component offscreen before
      // to the screen.
      
      if (getGraphics() != null)
      {
         Dimension d = getSize();
         if (checkImage(d))
         {
            drawPanel(offScreenGraphicsImage.getGraphics());
            getGraphics().drawImage(offScreenGraphicsImage, 0, 0, null);
         }
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
   // Class method to overide the standard panel paintComponents
   // routine.
   //==============================================================

   public void paintComponent(Graphics g)
   {
      super.paintComponent(g);
      drawPanel(g);
   }

   //==============================================================
   // Overiding public paint method so that a image may be placed
   // in the background.
   //==============================================================

   private void drawPanel(Graphics g)
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
         g.drawImage((snowFlakes.get(i)).getImage(),
                     (snowFlakes.get(i)).getSpaceOccupied().x,
                     (snowFlakes.get(i)).getSpaceOccupied().y, this);
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