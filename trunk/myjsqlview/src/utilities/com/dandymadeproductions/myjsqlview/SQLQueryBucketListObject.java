//=================================================================
//                   SQLQueryBucketListObject Class
//=================================================================
//   This class is used to create a variant from a standard Swing
// button that exempts translucency.
//
//                  << SQLQueryBucketListObject.java >>
//
//=================================================================
// 
// Copyright (c) 2007-2012, Sun Microsystems, Inc., Chet, Dana M. Proctor
// All rights reserved.
// Version 1.5 01/01/2012
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
// Version 1.0 05/01/2007 Original Sun Microsystems, Inc TransparentButton Class.
//         1.1 03/31/2011 Integration Into MyJSQLView as SQLQueryBucketListObject.
//         1.2 04/10/2011 Class Method getSQLStatementString() Correction to NOT
//                        Change the Object's sqlStatementString, but Just Return.
//                        Also !isLimited.
//         1.3 05/21/2011 Class Method constructionSQLQueryBucketListObject() Set the
//                        Button's Derived Font to BOLD. Also Increased Value of alphaValue.
//         1.4 09/18/2011 Set the Components setContentAreaFilled() to false in the
//                        Method constructSQLQueryBucketListObject().
//         1.5 01/01/2012 Copyright Update.
//                            
//-----------------------------------------------------------------
//                 danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview;

import java.awt.AlphaComposite;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JButton;

//=================================================================
//                   MyJSQLView SQLQueryBucketListObject
//=================================================================

/**
 *    The SQLQueryBucketListObject class is used to a variant from a standard
 * Swing button that exempts translucency.
 * 
 * @author Chet, Dana M. Proctor
 * @version 1.5 01/01/2012
 */

class SQLQueryBucketListObject extends JButton
{
   // Class Instances.
   private static final long serialVersionUID = -3584530005550405169L;
   private static final float alphaValue = 0.76f;
   
   private BufferedImage buttonImage = null;
   private String sqlStatementString;
   private boolean isLimited;

   //==============================================================
   // SQLQueryBucketListObject Constructor
   //==============================================================
   
   public SQLQueryBucketListObject()
   {
      super();
      constructSQLQueryBucketListObject();
   }
   
   public SQLQueryBucketListObject(String label)
   {
      super(label);
      constructSQLQueryBucketListObject();
   }
   
   private void constructSQLQueryBucketListObject()
   {
      setFont(getFont().deriveFont(Font.BOLD));
      isLimited = false;
      setContentAreaFilled(false);
      setOpaque(false);
   }
   
   //==============================================================
   // Overide the standard paint component method.
   //==============================================================
   
   public void paint(Graphics g)
   {
      // Create an image for the button graphics if necessary
      if (buttonImage == null || buttonImage.getWidth() != getWidth()
          || buttonImage.getHeight() != getHeight())
      {
         buttonImage = getGraphicsConfiguration().createCompatibleImage(getWidth(), getHeight());
      }
      Graphics gButton = buttonImage.getGraphics();
      gButton.setClip(g.getClip());

      // Have the superclass render the button for us
      super.paint(gButton);

      // Make the graphics object sent to this paint() method translucent
      Graphics2D g2d = (Graphics2D) g;
      AlphaComposite newComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaValue);
      g2d.setComposite(newComposite);

      // Copy the button's image to the destination graphics, translucently
      g2d.drawImage(buttonImage, 0, 0, null);
   }
   
   //==============================================================
   // Class Method to set the LIMIT aspect of the SQL Statement.
   //==============================================================
   
   public boolean isLimited()
   {
      return isLimited;
   }
   
   //==============================================================
   // Class Method to get the SQL Statement String associated with
   // this translucent button. Duh, this is a special class to be
   // used with the MyJSQLView SQLQueryBucketFrame Class.
   //==============================================================
   
   public String getSQLStatementString()
   {
      if (!isLimited)
         return MyJSQLView_Utils.getUnlimitedSQLStatementString(sqlStatementString);
      else
         return sqlStatementString;
   }
   
   //==============================================================
   // Class Method to set the SQL Statement String associated with
   // this translucent button.
   //==============================================================
   
   public void setSQLStatementString(String value)
   {
      sqlStatementString = value;
   }
   
   //==============================================================
   // Class Method to set the LIMIT aspect of the SQL Statement.
   //==============================================================
   
   public void setLimited(boolean value)
   {
      isLimited = value;
   }  
}