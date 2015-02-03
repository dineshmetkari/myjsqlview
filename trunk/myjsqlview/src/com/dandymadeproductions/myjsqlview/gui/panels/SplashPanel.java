//=================================================================
//                  MyJSQLView SplashPanel
//=================================================================
//
//    This class provides a startup splash panel that is used on
// a successful login to indicate the progress of the application
// initialization.
//
//                   << SplashPanel.java >>
//
//=================================================================
// Copyright (C) 2005-2015 Dana M. Proctor
// Version 2.4 09/21/2012
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
// Version 1.0 12/16/2008 Initial SplashPanel Class.
//         1.1 12/18/2008 Added Symbol, and Loading, Finalized.
//         1.2 12/18/2008 Privatized Several of the Class Instances. Class
//                        Method paint() Changed Random Generation to nextInt().
//         1.3 01/05/2009 Commented the Drawing of the loadingString to Speed
//                        Up paint() Method. Updated backgroundImage to Maintain
//                        Graphic.
//         1.4 05/27/2009 Header Format Changes/Update.
//         1.5 10/25/2009 Obtained Constructor Instance fileSeparator From
//                        MyJSQLView_Utils Class.
//         1.6 02/18/2010 Changed Package to Reflect Dandy Made Productions Code.
//         1.7 02/26/2010 Constructor Argument MyJSQLView_ResourceBundle. Implementation
//                        of versionString from Same Resource.
//         1.8 02/26/2010 Correction of key String for resource in Constructor
//                        versionString.
//         1.9 01/27/2011 Copyright Update.
//         2.0 01/01/2012 Copyright Update.
//         2.1 07/08/2012 Changes in Way MyJSQLView_ResourceBundle Handles the Collection
//                        of Resource Strings. Change to resource.getResourceString(key,
//                        default).
//         2.2 08/19/2012 Collection of All Image Resources Through resourceBundle.
//         2.3 09/10/2012 Changed Package Name to com.dandymadeproductions.myjsqlview.gui.panels.
//                        Made Class, Constructor, setThreadAction(), & suspendPanel()
//                        Public.
//         2.4 09/21/2012 Removal of Starting the Panel's Runnable Thread in the Constructor.
//
//-----------------------------------------------------------------
//                 danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview.gui.panels;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import com.dandymadeproductions.myjsqlview.MyJSQLView;
import com.dandymadeproductions.myjsqlview.utilities.MyJSQLView_ResourceBundle;
import com.dandymadeproductions.myjsqlview.utilities.MyJSQLView_Utils;

/**
 *    The SplashPanel class provides a startup splash panel that is used on a
 * successful login to indicate the progress of the application initialization.
 * 
 * @author Dana M. Proctor
 * @version 2.4 09/21/2012
 */

public class SplashPanel extends JPanel implements Runnable
{
   // Class Instances.
   private static final long serialVersionUID = 2484935547937050383L;

   private transient Image backgroundImage;
   private int backgroundImageWidth, backgroundImageHeight;
   private transient Image offScreenGraphicsImage;
   
   private Font versionFont;
   //private Font loadingFont;
   //private String loadingString;
   
   private double xCoordinate, yCoordinate;
   private double symbolWidth, symbolHeight;
   private double symbol_XPosition, symbol_YPosition;
   private Random random;
   
   private boolean runThread;
   private boolean suspendThread;
   private static final int frameDelay = 200;
   
   private String versionString;

   //==============================================================
   // SplashPanel Constructor
   //==============================================================

   public SplashPanel(MyJSQLView_ResourceBundle resourceBundle)
   {
      // Class Instances
      String fileSeparator, resource;

      // Setting up the panel stuff.
      setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(),
                                                   BorderFactory.createLoweredBevelBorder()));

      // ==========================================================
      // Obtaining the background image and setting up as
      // needed instances values.

      fileSeparator = MyJSQLView_Utils.getFileSeparator();
      
      backgroundImage = resourceBundle.getResourceImage("images" + fileSeparator
                                                        + "MyJSQLView_Splash.jpg").getImage();
      backgroundImageWidth = backgroundImage.getWidth(null);
      backgroundImageHeight = backgroundImage.getHeight(null);
      
      versionFont = new Font("Serif", Font.ITALIC, 10);
      resource = resourceBundle.getResourceString("SplashPanel.label.Version", "Version");
      versionString = resource + " " + (MyJSQLView.getVersion())[1];
      
      // Symbol Parameters
      xCoordinate = -5.0;
      yCoordinate = -5.0;
      symbolWidth = 8.0;
      symbolHeight = 20.0;
      symbol_XPosition = 35;
      symbol_YPosition = 180;
      random = new Random();
      
      //loadingString = "Loading";
      //loadingFont = new Font("Serif", Font.PLAIN, 38);
      
      runThread = true;
      suspendThread = false;
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
         render();
         timeStep();
      }
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
      if (d.width == 0 || d.height == 0)
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
   // Overiding public paint method so that a image may be placed
   // in the background and animation created.
   //==============================================================

   public void paint(Graphics g)
   {
      // Method Instances
      Graphics2D g2;
      
      AffineTransform affineTransform;
      //AffineTransform oldTransform;
      int panelWidth, panelHeight;
      int imagePosition_X, imagePosition_Y;
      float alpha;
      double rotationAngle;
      Color gradientColor_0, gradientColor_1;
      AlphaComposite alphaComposite;
      GradientPaint gradientPaint;
      Area area;
      Ellipse2D.Double symbolEllipse;
      
      // Setting parameters.
      panelWidth = this.getWidth();
      panelHeight = this.getHeight();
      
      // Make sure refill background color.
      g.setColor(this.getBackground());
      g.fillRect(0, 0, panelWidth, panelHeight);

      // ==========================
      // Draw image in center.
      
      imagePosition_X = (panelWidth - backgroundImageWidth) / 2;
      if (imagePosition_X < 0)
         imagePosition_X = 0;

      imagePosition_Y = (panelHeight - backgroundImageHeight) / 2;
      if (imagePosition_Y < 0)
         imagePosition_Y = 0;
      
      g.drawImage(backgroundImage, imagePosition_X, imagePosition_Y, this);
      
      // ==========================
      // Draw version information.
      
      g2 = (Graphics2D)g;
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    
      g2.setColor(Color.black);
      g2.setFont(versionFont);
      g2.drawString(versionString, 240, 20);
      
      // ==========================
      // Draw decorative symbol.
      
      alpha = 0.9F;
      alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
      g2.setComposite(alphaComposite);
      
      gradientColor_0 = new Color(249, random.nextInt(256), 31);
      gradientColor_1 = new Color(random.nextInt(256), 157, 71);
      gradientPaint = new GradientPaint(20.0F, 70.0F, gradientColor_0,
                                              40.0F, 90.0F, gradientColor_1, true);
      
      symbolEllipse = new Ellipse2D.Double(xCoordinate, yCoordinate, symbolWidth, symbolHeight);
      symbolEllipse.setFrame(symbol_XPosition, symbol_YPosition, symbolWidth, symbolHeight);
      area = new Area(symbolEllipse);
      
      g2.setStroke(new BasicStroke(1.0F));
      g2.setPaint(gradientPaint);
      g2.draw(symbolEllipse);
      
      rotationAngle = Math.PI/4; 
      affineTransform = AffineTransform.getRotateInstance(rotationAngle,
                                                       symbol_XPosition + symbolWidth / 2.0,
                                                       symbol_YPosition + symbolHeight);
      // Drawing the symbol.
      for (int i = 0; i < 7; i++)
      {
         g2.draw(area);
         g2.transform(affineTransform);
         area.exclusiveOr(new Area(affineTransform.createTransformedShape(symbolEllipse)));
         affineTransform.rotate(rotationAngle,
                             symbol_XPosition + symbolWidth / 2.0,
                             symbol_YPosition + symbolHeight);
      }
      
      // Restore the original transformation.
      affineTransform.setToIdentity();
      g2.setTransform(affineTransform);
      
      // ==========================
      // Draw animated status progress loading indicator.
      
      // Commented to speed up loading of splash window
      // incorporated directly into image that is loaded.
      // Left intact to create as desired.
      /*
      affineTransform = AffineTransform.getTranslateInstance(panelWidth / 2.0, panelHeight * 4.0 / 5.0);
      g2.transform(affineTransform);
      
      gradientColor_0 = new Color(23, 190, 45);
      gradientColor_1 = new Color(140, 30, 90);
      gradientPaint = new GradientPaint(15.0F, 30.0F, gradientColor_0,
                                              40.0F, 60.0F, gradientColor_1, true);
      g2.setPaint(gradientPaint);
      g2.setFont(loadingFont);
      
      int limit = 6;
      for (int i = 1; i <= limit; i++)
      {
        // Save the original transformation.
        oldTransform = g2.getTransform();
            
        float ratio = (float)i / (float)limit;
        g2.transform(AffineTransform.getRotateInstance(Math.PI * (ratio - 1.0f)));
        alpha = ((i == limit) ? 1.0f : ratio / 3);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2.drawString(loadingString, 0, 0);
        
        // Restore the original transformation.
        g2.setTransform(oldTransform);
      }
      */
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
