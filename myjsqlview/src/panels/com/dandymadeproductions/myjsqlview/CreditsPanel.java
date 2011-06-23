//=================================================================
//                   MyJSQLView Credits Panel 
//=================================================================
//
//    This class provides a general container to display the
// basic information about the MyJSQLView application's version,
// build, webSite, and credits.
//
//                   << CreditsPanel.java >>
//
//=================================================================
// Copyright (C) 2005-2011 Dana M. Proctor
// Version 3.5 06/20/2011
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
// Version 1.0 Original CreditsPanel Class.
//         1.1 Background Additions and Misc. Cleanup.
//         1.2 Updated Developers Titles.
//         1.3 Updated Developers Titles.
//         1.4 Final Class Instance scrollPadding & scrollSpeed
//             Made Static.
//         1.5 Used System.getProperty("file.separator") for All File
//             System Resources Accesses Through Instance fileSeparator.
//         1.6 Cleaned Up Javadoc Comments.
//         1.7 Change in Javadoc Comments.
//         1.8 Header Update.
//         1.9 Background Image Based on Database Connection.
//         2.0 HSQL Database Background Image Addition.
//         2.1 Class Instance serialVersionUID Addition & transient
//             Declaration for Class Instances offScreenGraphicsImage,
//             backgroundImage, & t.
//         2.2 Removed t As a Class Instance, Moved to Constructor.
//         2.3 Oracle Database Background Image Addition.
//         2.4 MyJSQLView Project Common Source Code Formatting.
//         2.5 Updated Developers List.
//         2.6 Header Format Changes/Update.
//         2.7 Constructor fileSeparator Obtained From MyJSQLView_Utils
//             Class.
//         2.8 Added Class Method suspendPanel() to Allow AboutFrame to Naturally
//             End the Thread in the Class.
//         2.9 Class Instance runThread Changed to volatile.
//         3.0 Changed Package to Reflect Dandy Made Productions Code.
//         3.1 Organized Imports.
//         3.2 SQLite Database Background Image Addition.
//         3.3 Added Method Instance subProtocol to Constructor.
//         3.4 Replaced Method Instance subProtocol With dataSourceType in the
//             Constructor. Added backgroundImage key for MSAccess.
//         3.5 Constructor Conditional Check for HSQL Changed From equals to
//             indexOf.
//         
//-----------------------------------------------------------------
//                 danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 *    The CreditsPanel provides a general container to display the basic
 * information about the MyJSQLView application's version, build,
 * webSite, and credits.
 * 
 * @author Dana M. Proctor
 * @version 3.5 06/20/2011
 */

class CreditsPanel extends JPanel implements Runnable
{
   // Class Instances.
   private static final long serialVersionUID = 2801540859259238431L;

   private String[] myJSQLView_Version;
   private String webSiteString;
   private volatile boolean runThread;
   private transient Image offScreenGraphicsImage;
   private transient Image backgroundImage;
   private int yOffset;

   private static final int scrollPadding = 75;
   private static final long scrollSpeed = 40;

   private String[] developersNames = {"Dana M. Proctor", "Jujubi", "Lookfwd",
                                       "Blarkin", "Nil_lin", "Gashogtoo", "Poisonerbg",
                                       "Reydelcompas"};
   private String[] developersTitles = {"Project Manager", "Developer", "Design/Analysis",
                                        "Developer", "Developer", "Tester", "Developer",
                                        "Tester"};

   //===========================================================
   // CreditsPanel Constructor
   //===========================================================

   protected CreditsPanel(String[] myJSQLView_Version, String webSiteString)
   {
      this.myJSQLView_Version = myJSQLView_Version;
      this.webSiteString = webSiteString;

      String fileSeparator = MyJSQLView_Utils.getFileSeparator();
      String dataSourceType = ConnectionManager.getDataSourceType();
      
      runThread = true;

      // Get the corresponding background image.

      if (dataSourceType.equals(ConnectionManager.MYSQL))
         backgroundImage = (new ImageIcon("images" + fileSeparator + "dolphin.jpg")).getImage();
      else if (dataSourceType.equals(ConnectionManager.POSTGRESQL))
         backgroundImage = (new ImageIcon("images" + fileSeparator + "elephant.jpg")).getImage();
      else if (dataSourceType.indexOf(ConnectionManager.HSQL) != -1)
         backgroundImage = (new ImageIcon("images" + fileSeparator + "spiral.jpg")).getImage();
      else if (dataSourceType.equals(ConnectionManager.ORACLE))
         backgroundImage = (new ImageIcon("images" + fileSeparator + "letterO.jpg")).getImage();
      else if (dataSourceType.equals(ConnectionManager.SQLITE))
         backgroundImage = (new ImageIcon("images" + fileSeparator + "feather.jpg")).getImage();
      else if (dataSourceType.equals(ConnectionManager.MSACCESS))
         backgroundImage = (new ImageIcon("images" + fileSeparator + "key.jpg")).getImage();
      else
         backgroundImage = (new ImageIcon("images" + fileSeparator + "battleship.jpg")).getImage();

      Thread t = new Thread(this, "Credits");
      t.start();
   }

   //================================================================
   // Class method for starting the runnable thread.
   //================================================================

   public void run()
   {
      // Cycling through the routine
      // to animate the credits.
      while (runThread)
      {
         render();
         yOffset--;
         timeStep();
      }
   }

   //================================================================
   // Class method to create a double buffered offscreen graphic.
   //================================================================

   protected void render()
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
            imageGraphics.setColor(Color.WHITE);
            imageGraphics.fillRect(0, 0, d.width, d.height);

            imageGraphics.setColor(getForeground());

            // Draw this component offscreen then to screen.
            paint(imageGraphics);
            g2.drawImage(offScreenGraphicsImage, 0, 0, null);

            imageGraphics.dispose();
         }
         g2.dispose();
      }
   }

   //================================================================
   // Class method to setup a offscreen image.
   //================================================================

   protected boolean checkImage(Dimension d)
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

   //================================================================
   // Class method for delaying the animation/framerate change.
   //================================================================

   public void timeStep()
   {
      try
      {
         Thread.sleep(scrollSpeed);
      }
      catch (InterruptedException e)
      {
         System.out.println("Process Interrupted.");
      }
   }
   
   //==============================================================
   // Class Method to let the thread run() method naturally
   // finish.
   //==============================================================

   public synchronized void suspendPanel(boolean action)
   {
      runThread = !action;
   }

   //================================================================
   // Overriding Class method to paint the panel's contents.
   //================================================================

   public void paint(Graphics g)
   {
      Graphics2D g2 = (Graphics2D) (g);
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

      // Class Method Instances
      Font nameFont, titleFont;
      FontMetrics fontMetrics;
      String fontName;
      BasicStroke pen2, pen3;
      Line2D borderLineTop, borderLineLeft, borderLineBottom, borderLineRight;
      int fontSize, stringWidth;
      int width, height, x, y;

      // Setting up a font and sizes
      if (this.getFont() != null)
      {
         fontName = this.getFont().getFontName();
         fontSize = this.getFont().getSize();
         nameFont = this.getFont();
         titleFont = new Font(fontName, Font.BOLD, fontSize);
      }
      else
      {
         nameFont = new Font("Serif", Font.PLAIN, 12);
         titleFont = new Font("Serif", Font.BOLD, 12);
      }

      // Create pens to use
      pen2 = new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND);
      pen3 = new BasicStroke(3.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND);

      // Setup general attributes to run the credits
      x = 0;
      y = 0;
      stringWidth = 0;
      width = getWidth();
      height = getHeight();

      // Draw the background detail.

      // Background
      g2.setColor(Color.WHITE);
      g2.fillRect(0, 0, width, height);

      g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) 0.22));
      g2.drawImage(backgroundImage, 0, 0, null);
      g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) 1.0));

      // Sunken Frame Look
      g2.setStroke(pen2);
      g2.setPaint(Color.LIGHT_GRAY);

      borderLineBottom = new Line2D.Double(0, (double) height, (double) width, (double) height);
      borderLineRight = new Line2D.Double((double) width, 0, (double) width, (double) height);
      g2.draw(borderLineBottom);
      g2.draw(borderLineRight);

      g2.setStroke(pen3);
      g2.setPaint(Color.DARK_GRAY);

      borderLineTop = new Line2D.Double(0, 0, (double) width, 0);
      borderLineLeft = new Line2D.Double(0, 0, 0, (double) height);
      g2.draw(borderLineTop);
      g2.draw(borderLineLeft);

      // Draw MyJSQLView
      g2.setColor(Color.BLACK);
      g2.setFont(titleFont);
      fontMetrics = g2.getFontMetrics();
      stringWidth = fontMetrics.stringWidth(myJSQLView_Version[0]);
      x = (width - stringWidth) / 2;
      y += fontMetrics.getHeight() + 1;
      g2.drawString(myJSQLView_Version[0], x, y + yOffset + height);

      // Draw Verion and Build ID
      g2.setFont(nameFont);
      stringWidth = fontMetrics.stringWidth(myJSQLView_Version[1]);
      x = (width - stringWidth) / 2;
      y += fontMetrics.getHeight() + 1;
      g2.drawString(myJSQLView_Version[1], x, y + yOffset + height);

      stringWidth = fontMetrics.stringWidth(myJSQLView_Version[2]);
      x = (width - stringWidth) / 2;
      y += fontMetrics.getHeight() + 1;
      g2.drawString(myJSQLView_Version[2], x, y + yOffset + height);

      y += 15;

      // Draw Credits
      for (int i = 0; i < developersNames.length; i++)
      {
         g2.setFont(titleFont);
         fontMetrics = g2.getFontMetrics();
         stringWidth = fontMetrics.stringWidth(developersTitles[i]);
         x = (width - stringWidth) / 2;
         y += fontMetrics.getHeight() + 1;
         g2.drawString(developersTitles[i], x, y + yOffset + height);

         g2.setFont(nameFont);
         fontMetrics = g2.getFontMetrics();
         stringWidth = fontMetrics.stringWidth(developersNames[i]);
         x = (width - stringWidth) / 2;
         y += fontMetrics.getHeight() + 1;
         g2.drawString(developersNames[i], x, y + yOffset + height);

         if (i == developersNames.length - 1)
            if (Math.abs(yOffset) >= (y + height + scrollPadding))
               yOffset = 0;
         y += 15;
      }

      // Draw Web Site String
      g2.setFont(titleFont);
      fontMetrics = g2.getFontMetrics();
      stringWidth = fontMetrics.stringWidth("WebSite");
      x = (width - stringWidth) / 2;
      y += fontMetrics.getHeight() + 1;
      g2.drawString("WebSite", x, y + yOffset + height);

      g2.setFont(nameFont);
      fontMetrics = g2.getFontMetrics();
      stringWidth = fontMetrics.stringWidth(webSiteString);
      x = (width - stringWidth) / 2;
      y += fontMetrics.getHeight() + 1;
      g2.drawString(webSiteString, x, y + yOffset + height);
   }
}