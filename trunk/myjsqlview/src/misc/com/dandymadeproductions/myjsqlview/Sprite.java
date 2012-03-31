//=================================================================
//                   MyJSQLView Sprite
//=================================================================
//
//    This class provides the basic characteristics of a
// animated object, sprite, that is used in the preferences
// frame, PreferencesPanels.
// 
//                    << Sprite.java >>
//
//=================================================================
// Copyright (C) 2007-2012 Dana M. Proctor
// Version 1.9 01/01/2012
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
// Version 1.0 Initial Sprite Class.
//         1.1 Comment Changes.
//         1.2 Header Update.
//         1.3 MyJSQLView Project Common Source Code Formatting.
//         1.4 Additional Standardation to Comments.
//         1.5 Updated Header Information.
//         1.6 Changed Package to Reflect Dandy Made Productions Code.
//         1.7 Organized Imports.
//         1.8 Copyright Update.
//         1.9 Copyright Update.
//        
//-----------------------------------------------------------------
//               danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview;

import java.awt.Component;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Random;

/**
 * The Sprite class provides the basic characteristics of a animated
 * object, sprite, that is used in the preferences frame, PreferencesPanels.
 * 
 * @author Dana M. Proctor
 * @version 1.9 01/01/2012
 */

class Sprite
{
   // Class Instances
   Image image;
   Rectangle spaceOccupied;
   Point nextPosition;
   Rectangle bounds;
   Random random;

   //==============================================================
   // Sprite Object Constructor
   //==============================================================

   protected Sprite(Component component, Rectangle panelBounds, Image image,
                    Point position, Point nextPosition)
   {
      this.image = image;
      this.nextPosition = nextPosition;
      bounds = panelBounds;

      // Seed a random number generator for this sprite based
      // on its position.
      random = new Random(position.x);

      setSpaceOccupied(new Rectangle(position.x, position.y, image.getWidth(component),
                       image.getHeight(component)));
   }

   //==============================================================
   // Class method to allow the updating of this sprite's
   // location. Override this method for desired behavior
   // in sub classes.
   //==============================================================

   protected void updatePosition()
   {
      // Overide in your sub-class.
   }

   //==============================================================
   // Class Method for checking if a test sprite object
   // occupies the same space as another sprite object.
   //==============================================================

   protected boolean testCollision(Sprite testObject)
   {
      if (testObject != this)
      {
         return spaceOccupied.intersects(testObject.getSpaceOccupied());
      }
      return false;
   }

   //==============================================================
   // Class method to allow classes to get the space occupied by
   // the sprite object.
   //==============================================================

   protected Rectangle getSpaceOccupied()
   {
      return spaceOccupied;
   }

   //==============================================================
   // Class method to allow classes to get the next postion that
   // the sprite will move toward. Next Translation.
   //==============================================================

   protected Point getNextPosition()
   {
      return nextPosition;
   }

   //==============================================================
   // Class method to allow classes to get the image associated
   // with sprite object.
   //==============================================================

   protected Image getImage()
   {
      return image;
   }

   //==============================================================
   // Class method to allow outside classes to set the boundary
   // that a sprite object may occupy.
   //==============================================================

   protected void setBounds(Rectangle bounds)
   {
      this.bounds = bounds;
   }

   //==============================================================
   // Class method to allow classes to set the next postion that
   // the sprite will move toward. Next Translation.
   //==============================================================

   protected void setNextPosition(Point nextPosition)
   {
      this.nextPosition = nextPosition;
   }

   //==============================================================
   // Class method to allow outside classes to set the space
   // occupied by the sprite object.
   //==============================================================

   protected void setSpaceOccupied(Rectangle spaceOccupied)
   {
      this.spaceOccupied = spaceOccupied;
   }

   //==============================================================
   // Class method to allow outside classes to set the position in
   // the boundary or the occupied space of the sprite object.
   //==============================================================

   protected void setSpaceOccupied(Point position)
   {
      spaceOccupied.setLocation(position.x, position.y);
   }
}