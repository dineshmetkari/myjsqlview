//=================================================================
//             MyJSQLView PreferencesPanelSummer
//=================================================================
//
//    This class provides a generic panel used in the Preferences
// Menu to highlight the top tree element during the northern
// hemisphere's summer months, June-August.
//
//           << PreferencesPanelSummer.java >>
//
//=================================================================
// Copyright (C) 2005-2013 Dana M. Proctor
// Version 3.6 09/21/2012
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
// Version 1.0 05/03/2007 Original PreferencesPanel Class.
//         1.1 09/09/2007 Removed Unused Class Instance dateFormat.
//         1.2 09/18/2007 Implemented Runnable Interface. Added Class Method
//                        setThreadAction().
//         1.3 09/21/2007 Changed Class Name to PreferencesPanelSummer.
//                        Implementation of FireFlies.
//         1.4 10/22/2007 Change in Javadoc Comments.
//         1.5 11/23/2007 Changed the backGroundImage to PreferencesPanelSummer.jpg.
//         1.6 11/24/2007 Relinquished Control of Calendar Date Creation to
//                        Parent PreferencesPanel.
//         1.7 12/12/2007 Header Update.
//         1.8 03/02/2008 Comment Change for Active Season, July-September.
//         1.9 05/12/2008 Added Class Instance serialVersionUID. Declared
//                        Instances backgroundImage, fireFlyImages, &
//                        offScreenGraphicsImage.
//         2.0 10/21/2008 MyJSQLView Project Common Source Code Formatting.
//         2.1 12/16/2008 Class Methods updateFireFlies(), render(), checkImage(),
//                        and timeStep() Made Private.
//         2.2 05/25/2009 Check on Constructor Instance fileSeparator.
//         2.3 06/12/2009 Class Method checkImage() Dimension Conditional For
//                        Less Than or Equal.
//         2.4 09/03/2009 Implemented Crickets Sound in Panel. Added Class Instance
//                        cricketsSoundClip. Modifications to Constructor to Load,
//                        run() to Start, Methods setThreadAction() & suspendPanel()
//                        to Control the Sounds Activity.
//         2.5 10/12/2009 Removed Sound Implementation for Panel. To Volatile.
//         2.6 10/25/2009 Obtained Constructor Instance fileSeparator From MyJSQLView_Utils
//                        Class.
//         2.7 02/18/2010 Changed Package to Reflect Dandy Made Productions Code.
//         2.8 05/18/2010 Parameterized Class Instance fireFlies to Bring Code Into
//                        Compliance With Java 5.0 API. Organized Imports.
//         2.9 10/18/2010 Updated to Have Rendering Done With the paintComponent() Method
//                        for Panels Instead of paint(). Added paintComponent() and
//                        Changed paint() to drawPanel(). Removed Use of Graphics2D in
//                        render() and removed Setting of Border in Constructor.
//         3.0 11/15/2010 Changed paintComponent() to public and drawPanel() to private.
//         3.1 01/27/2011 Copyright Update.
//         3.2 01/01/2012 Copyright Update.
//         3.3 05/10/2112 Changed Class Instance fireFlies from Vector Data Type to
//                        ArrayList.
//         3.4 08/19/2012 Collection of All Image Resources Through resourceBundle.
//         3.5 09/11/2012 Changed Package Name to com.dandymadeproductions.myjsqlview.gui.panels.
//                        Made Class & Constructor Public.
//         3.6 09/21/2012 Removal of Starting the Panel's Runnable Thread in the Constructor.
//
//-----------------------------------------------------------------
//                danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview.gui.panels;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;

import com.dandymadeproductions.myjsqlview.MyJSQLView;
import com.dandymadeproductions.myjsqlview.gui.sprites.FireFly;
import com.dandymadeproductions.myjsqlview.utilities.MyJSQLView_ResourceBundle;
import com.dandymadeproductions.myjsqlview.utilities.MyJSQLView_Utils;

/**
 *    The PreferencesPanelSummer class provides a generic panel used in the
 * Preferences Menu to highlight the top tree element during the northern
 * hemisphere's summer months, July-September.
 * @author Dana M. Proctor
 * 
 * @version 3.6 09/21/2012
 */

public class PreferencesPanelSummer extends PreferencesPanel implements Runnable
{
   // Class Instances.
   private static final long serialVersionUID = 2484935547937050383L;
   private transient Image backgroundImage;
   private int backgroundImageWidth, backgroundImageHeight;
   private transient Image offScreenGraphicsImage;

   private static final int fireFlyColors = 6;
   private transient Image[] fireFlyImages = new Image[fireFlyColors];
   private int fireFlyImageWidth, fireFlyImageHeight;
   private ArrayList<FireFly> fireFlies;

   private volatile boolean runThread;
   private volatile boolean suspendThread;
   private static final int frameDelay = 40;

   //==============================================================
   // PreferencesPanelSummer Constructor
   //==============================================================

   public PreferencesPanelSummer()
   {
      // Class Instances
      MyJSQLView_ResourceBundle resourceBundle;
      String fileSeparator;
      String[] fireFlyImageName = {"red_firefly.gif", "green_firefly.gif", "blue_firefly.gif",
                                   "yellow_firefly.gif", "purple_firefly.gif", "white_firefly.gif"};

      // ==========================================================
      // Obtaining the background image and setting up as
      // needed instances values.

      resourceBundle = MyJSQLView.getResourceBundle();
      fileSeparator = MyJSQLView_Utils.getFileSeparator();

      backgroundImage = resourceBundle.getResourceImage("images" + fileSeparator
                                                        + "PreferencesPanelSummer.jpg").getImage();
      backgroundImageWidth = backgroundImage.getWidth(null);
      backgroundImageHeight = backgroundImage.getHeight(null);

      // ===========================================================
      // Obtaing the firefly images and setting up as needed.

      for (int i = 0; i < fireFlyColors; i++)
         fireFlyImages[i] = resourceBundle.getResourceImage("images" + fileSeparator
                                                            + fireFlyImageName[i]).getImage();
      fireFlyImageWidth = fireFlyImages[0].getWidth(null);
      fireFlyImageHeight = fireFlyImages[0].getHeight(null);
      fireFlies = new ArrayList <FireFly>();

      runThread = true;
      suspendThread = false;
   }

   //==============================================================
   // Class method for starting the runnable thread.
   //==============================================================

   public void run()
   {
      // Class Instances
      int fireFlyCount;
      Random randomNumber;
      Rectangle panelBounds;

      // Create fireflies from the firefly image files.
      fireFlyCount = 15;
      randomNumber = new Random(System.currentTimeMillis());
      panelBounds = new Rectangle(0, 0, backgroundImageWidth, backgroundImageHeight);

      for (int i = 0; i < fireFlyCount; i++)
      {
         Point currentFireFlyPosition = getEmptyPosition();
         fireFlies.add(new FireFly(this, panelBounds, fireFlyImages[i % fireFlyImages.length],
                                   currentFireFlyPosition, new Point(randomNumber.nextInt() % 4,
                                   randomNumber.nextInt() % 4)));
      }

      // Cycling through the routine
      // to animate the panel.
      while (runThread)
      {
         updateFireFlies();
         render();
         timeStep();
      }
   }

   //==============================================================
   // Class method to obtain an empty postion in the panel that
   // fire fly may be placed.
   //==============================================================

   private Point getEmptyPosition()
   {
      // Class Method Instances
      Rectangle trialSpaceOccupied;
      Random randomNumber;
      boolean empty, collision;
      int numberOfTries;

      // Setting up.
      trialSpaceOccupied = new Rectangle(0, 0, fireFlyImageWidth, fireFlyImageHeight);
      randomNumber = new Random(System.currentTimeMillis());
      empty = false;
      numberOfTries = 0;

      // Begin the search for an empty position
      while (!empty && numberOfTries++ < 100)
      {
         // Obtain a random postion.
         trialSpaceOccupied.x = Math.abs(randomNumber.nextInt() % backgroundImageWidth);
         trialSpaceOccupied.y = Math.abs(randomNumber.nextInt() % backgroundImageHeight);

         // Check to see if an existing firefly occupies
         // the randomly selected postion.
         collision = false;
         for (int i = 0; i < fireFlies.size(); i++)
         {
            Rectangle testSpaceOccupied = (fireFlies.get(i)).getSpaceOccupied();
            if (trialSpaceOccupied.intersects(testSpaceOccupied))
               collision = true;
         }
         empty = !collision;
      }
      // Return the empty postion.
      return new Point(trialSpaceOccupied.x, trialSpaceOccupied.y);
   }

   // ==============================================================
   // Class method to update the fireflies' positions in the panel.
   // ==============================================================

   private void updateFireFlies()
   {
      // Class Method Instances
      FireFly currentFireFly;
      int fireFlyOccupiedIndex;
      Point tempSwapPoint;

      // Cycle through the fireflies, updating postion and
      // testing for collision.
      for (int i = 0; i < fireFlies.size(); i++)
      {
         currentFireFly = fireFlies.get(i);
         currentFireFly.updatePosition();

         // Collision check and recoil action as needed.
         fireFlyOccupiedIndex = testForCollision(currentFireFly);

         if (fireFlyOccupiedIndex >= 0)
         {
            tempSwapPoint = currentFireFly.getNextPosition();
            currentFireFly.setNextPosition((fireFlies.get(fireFlyOccupiedIndex)).getNextPosition());
            (fireFlies.get(fireFlyOccupiedIndex)).setNextPosition(tempSwapPoint);
         }
      }
   }

   //==============================================================
   // Class Method to check if the input FireFly collides with any
   // of the other fireflies. If does return the index in the vector
   // of offending firefly, else -1.
   //==============================================================

   private int testForCollision(FireFly testFireFly)
   {
      // Class Method Instances.
      FireFly currentFireFly;

      // Cycle through the fireflies, checking against input
      // firefly.
      for (int i = 0; i < fireFlies.size(); i++)
      {
         currentFireFly = fireFlies.get(i);

         // Don't need to check itself.
         if (currentFireFly == testFireFly)
            continue;

         if (testFireFly.testCollision(currentFireFly))
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
   // Class method to overide the standard panel paintComponents
   // routine.
   //==============================================================

   public void paintComponent(Graphics g)
   {
      super.paintComponent(g);
      drawPanel(g);
   }
   
   //==============================================================
   // Class method to create, paint, the graphics for the panel.
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

      // Draw Fireflies
      for (int i = 0; i < fireFlies.size(); i++)
      {
         g.drawImage((fireFlies.get(i)).getImage(),
                     (fireFlies.get(i)).getSpaceOccupied().x,
                     (fireFlies.get(i)).getSpaceOccupied().y, this);
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

   public void suspendPanel(boolean action)
   {
      runThread = !action;
   }
}
