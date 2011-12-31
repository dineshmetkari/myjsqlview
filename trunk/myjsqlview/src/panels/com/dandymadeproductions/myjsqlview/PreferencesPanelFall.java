//=================================================================
//           MyJSQLView PreferencesPanelFall
//=================================================================
//
//    This class provides a generic panel used in the Preferences
// Menu to highlight the top tree element during the northern
// hemisphere's fall months, October-November.
//
//           << PreferencesPanelFall.java >>
//
//=================================================================
// Copyright (C) 2007-2012 Dana M. Proctor
// Version 2.5 01/01/2012
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
// Version 1.0 08/24/2008 Initial PreferencesPanelFall Class.
//         1.1 08/25/2008 Increased frameDelay & leafCount. Added Additional Leaf
//                        Color Yellow.
//         1.2 10/21/2008 MyJSQLView Project Common Source Code Formatting.
//         1.3 12/16/2008 Class Methods updateLeafs(), render(), checkImage(),
//                        and timeStep() Made Private.
//         1.4 01/05/2009 Updated Some Comments.
//         1.5 05/25/2009 Check on Constructor Instance fileSeparator.
//         1.6 06/12/2009 Class Method checkImage() Dimension Conditional For Less
//                        Than or Equal.
//         1.7 09/04/2009 Implemented Wind Sound in Panel. Added Class Instance
//                        windSoundClip. Modifications to Constructor to Load,
//                        run() to Start, Methods setThreadAction() & suspendPanel()
//                        to Control the Sounds Activity.
//         1.8 10/12/2009 Removed Sound Implementation for Panel. To Volatile.
//         1.9 10/25/2009 Obtained Constructor Instance fileSeparator From MyJSQLView_Utils
//                        Class.
//         2.0 02/18/2010 Changed Package to Reflect Dandy Made Productions Code.
//         2.1 05/18/2010 Parameterized Class Instance leafs to Bring Code Into
//                        Compliance With Java 5.0 API. Organized Imports.
//         2.2 10/18/2010 Updated to Have Rendering Done With the paintComponent() Method
//                        for Panels Instead of paint(). Added paintComponent() and
//                        Changed paint() to drawPanel(). Removed Use of Graphics2D in
//                        render() and removed Setting of Border in Constructor.
//         2.3 11/15/2010 Changed paintComponent() to public and drawPanel() to private.
//         2.4 01/27/2011 Copyright Update.
//         2.5 01/01/2012 Copyright Update.
//
//-----------------------------------------------------------------
//              danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Random;
import java.util.Vector;
import javax.swing.ImageIcon;

/**
 *    The PreferencesPanelFall class provides a generic panel used
 * in the Preferences Menu to highlight the top tree element during
 * the northern hemisphere's fall months, October-November.
 * 
 * @author Dana M. Proctor
 * @version 2.5 01/01/2012
 */

class PreferencesPanelFall extends PreferencesPanel implements Runnable
{
   // Class Instances.
   private static final long serialVersionUID = -976830171242087875L;

   private transient Image backgroundImage;
   private int backgroundImageWidth, backgroundImageHeight;
   private transient Image offScreenGraphicsImage;

   private static final int leafColors = 5;
   private transient Image[] leafImages = new Image[leafColors];
   private int leafImageWidth, leafImageHeight;
   private Vector<Leaf> leafs;
   
   private volatile boolean runThread;
   private volatile boolean suspendThread;
   private static final int frameDelay = 85;

   //==============================================================
   // PreferencesPanelFall Constructor
   //==============================================================

   protected PreferencesPanelFall()
   {
      // Class Instances
      Thread t;
      String fileSeparator;
      String[] leafImageName = {"red_leaf.gif", "orange_leaf.gif", "tan_leaf.gif",
                                "red2_leaf.gif", "yellow_leaf.gif"};

      // ==========================================================
      // Obtaining the background image and setting up as
      // needed instances values.
      
      fileSeparator = MyJSQLView_Utils.getFileSeparator();

      backgroundImage = new ImageIcon("images" + fileSeparator + "PreferencesPanelFall.jpg").getImage();
      backgroundImageWidth = backgroundImage.getWidth(null);
      backgroundImageHeight = backgroundImage.getHeight(null);

      // ===========================================================
      // Obtaing the leaf images and setting up as needed.

      for (int i = 0; i < leafColors; i++)
         leafImages[i] = new ImageIcon("images" + fileSeparator + leafImageName[i]).getImage();
      leafImageWidth = leafImages[0].getWidth(null);
      leafImageHeight = leafImages[0].getHeight(null);
      leafs = new Vector <Leaf>();
      
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
      int leafCount;
      Random randomNumber;
      Rectangle panelBounds;

      // Create leafs from the leaf image files.
      leafCount = 14;
      randomNumber = new Random(System.currentTimeMillis());
      panelBounds = new Rectangle(0, 0, backgroundImageWidth, backgroundImageHeight);

      for (int i = 0; i < leafCount; i++)
      {
         Point currentLeafPosition = getEmptyPosition();

         leafs.add(new Leaf(this, panelBounds, leafImages[i % leafImages.length], currentLeafPosition,
                            new Point(randomNumber.nextInt() % 10, randomNumber.nextInt() % 10)));
      }
      
      // Cycling through the routine
      // to animate the panel.
      while (runThread)
      {
         updateLeafs();
         render();
         timeStep();
      }
   }

   //==============================================================
   // Class method to obtain an empty postion in the panel that
   // leaf may be placed.
   //==============================================================

   private Point getEmptyPosition()
   {
      // Class Method Instances
      Rectangle trialSpaceOccupied;
      Random randomNumber;
      boolean empty, collision;
      int numberOfTries;

      // Setting up.
      trialSpaceOccupied = new Rectangle(0, 0, leafImageWidth, leafImageHeight);
      randomNumber = new Random(System.currentTimeMillis());
      empty = false;
      numberOfTries = 0;

      // Begin the search for an empty position
      while (!empty && numberOfTries++ < 100)
      {
         // Obtain a random postion.
         trialSpaceOccupied.x = Math.abs(randomNumber.nextInt() % backgroundImageWidth);
         trialSpaceOccupied.y = Math.abs(randomNumber.nextInt() % backgroundImageHeight);

         // Check to see if an existing leaf occupies
         // the randomly selected postion.
         collision = false;
         for (int i = 0; i < leafs.size(); i++)
         {
            Rectangle testSpaceOccupied = (leafs.elementAt(i)).getSpaceOccupied();
            if (trialSpaceOccupied.intersects(testSpaceOccupied))
               collision = true;
         }
         empty = !collision;
      }
      // Return the empty postion.
      return new Point(trialSpaceOccupied.x, trialSpaceOccupied.y);
   }

   //==============================================================
   // Class method to update the leafs' positions in the panel.
   //==============================================================

   private void updateLeafs()
   {
      // Class Method Instances
      Leaf currentLeaf;
      int leafOccupiedIndex;
      Point tempSwapPoint;

      // Cycle through the leafs, updating postion and
      // testing for collision.
      for (int i = 0; i < leafs.size(); i++)
      {
         currentLeaf = leafs.elementAt(i);
         currentLeaf.updatePosition();

         // Collision check and recoil action as needed.
         leafOccupiedIndex = testForCollision(currentLeaf);

         if (leafOccupiedIndex >= 0)
         {
            tempSwapPoint = currentLeaf.getNextPosition();
            currentLeaf.setNextPosition((leafs.elementAt(leafOccupiedIndex)).getNextPosition());
            (leafs.elementAt(leafOccupiedIndex)).setNextPosition(tempSwapPoint);
         }
      }
   }

   //==============================================================
   // Class Method to check if the input leaf collides with any
   // of the other leafs. If does return the index in the vector
   // of offending leaf, else -1.
   //==============================================================

   private int testForCollision(Leaf testLeaf)
   {
      // Class Method Instances.
      Leaf currentLeaf;

      // Cycle through the leafs, checking against input
      // leaf.
      for (int i = 0; i < leafs.size(); i++)
      {
         currentLeaf = leafs.elementAt(i);

         // Don't need to check itself.
         if (currentLeaf == testLeaf)
            continue;

         if (testLeaf.testCollision(currentLeaf))
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

      // Draw Leafs
      for (int i = 0; i < leafs.size(); i++)
      {
         g.drawImage((leafs.elementAt(i)).getImage(),
                     (leafs.elementAt(i)).getSpaceOccupied().x,
                     (leafs.elementAt(i)).getSpaceOccupied().y, this);
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