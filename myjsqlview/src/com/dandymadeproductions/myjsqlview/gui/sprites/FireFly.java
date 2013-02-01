//=================================================================
//                    MyJSQLView FireFly
//=================================================================
//
//    This class provides the characteristics of a firefly
// object that is used in the preferences frame, Preferences
// panel summer.
// 
//                    << FireFly.java >>
//
//=================================================================
// Copyright (C) 2005-2013 Dana M. Proctor
// Version 2.3 09/11/2012
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
// Version 1.0 Initial FireFly Class.
//         1.1 Fine Tuning Motion and Completion.
//         1.2 Reformatted.
//         1.3 Sub Classed to Sprite. Removed all Class Methods,
//             But updatePosition(). All Others Now Handled
//             by the Super Class Sprite. Moved Over to the
//             New Source Folder Misc.
//         1.4 Some Comment Changes.
//         1.5 Header Update.
//         1.6 MyJSQLView Project Common Source Code Formatting.
//         1.7 Additional Standardation to Comments.
//         1.8 Updated Header Information.
//         1.9 Changed Package to Reflect Dandy Made Productions Code.
//         2.0 Organized Imports.
//         2.1 Copyright Update.
//         2.2 Copyright Update.
//         2.3 Changed Package Name to com.dandymadeproductions.myjsqlview.gui.sprites.
//             Made Class, Constructors, & Methods Public.
//        
//-----------------------------------------------------------------
//               danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview.gui.sprites;

import java.awt.Component;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;

/**
 * The FireFly class provides the characteristics of a firefly object
 * that is used in the preferences frame, Preferences panel summer.
 * 
 * @author Dana M. Proctor
 * @version 2.3 09/11/2012
 */

public class FireFly extends Sprite
{
   // Class Instances

   //==============================================================
   // FireFly Object Constructor
   //==============================================================

   public FireFly(Component component, Rectangle panelBounds, Image image,
                     Point position, Point nextPosition)
   {
      super(component, panelBounds, image, position, nextPosition);
   }

   //==============================================================
   // Class method to allow the updating of this firefly's location
   // based on a random movement, point gravitron, and boundaries.
   //==============================================================

   public void updatePosition()
   {
      // Class Method Instances.
      Point position, tempNextPosition;
      boolean pullGravity;

      // =========================================
      // Generate random movement.

      if (random.nextInt() % 10 == 0)
      {
         Point randomOffset = new Point(random.nextInt() % 3, random.nextInt() % 3);
         nextPosition.x += randomOffset.x;
         if (nextPosition.x >= 6)
            nextPosition.x -= 6;
         if (nextPosition.x <= -6)
            nextPosition.x += 6;

         nextPosition.y += randomOffset.y;
         if (nextPosition.y >= 6)
            nextPosition.y -= 6;
         if (nextPosition.y <= -6)
            nextPosition.y += 6;
      }

      // ========================================
      // Modify nextPosition to tend toward a
      // gravity well at Rectangle (150,10,250,240)

      position = new Point(spaceOccupied.x, spaceOccupied.y);
      tempNextPosition = new Point(nextPosition.x, nextPosition.y);
      pullGravity = false;

      if (random.nextInt() % 10 == 0)
      {
         // Pull x movement.
         if (position.x < 150 && nextPosition.x < 0)
         {
            pullGravity = true;
            tempNextPosition.x = -nextPosition.x;
         }
         else if (position.x > 400 && nextPosition.x > 0)
         {
            pullGravity = true;
            tempNextPosition.x = -nextPosition.x;
         }

         // Pull y movement.
         if (position.y < 10 && nextPosition.y < 0)
         {
            pullGravity = true;
            tempNextPosition.y = -nextPosition.y;
         }
         else if (position.y > 250 && nextPosition.y > 0)
         {
            pullGravity = true;
            tempNextPosition.y = -nextPosition.y;
         }
      }

      // Pull as needed.
      if (pullGravity)
         setNextPosition(tempNextPosition);

      // Move the firefly on the screen
      position.translate(nextPosition.x, nextPosition.y);

      // ==========================================
      // Manage fireflies that exceed the boundary
      // of the allowed space.

      // Boundary in x-dimension
      if (position.x < bounds.x)
         position.x = bounds.x + bounds.width;

      else if ((position.x + spaceOccupied.width) > (bounds.x + bounds.width))
         position.x = bounds.x - spaceOccupied.width;

      // Boundary in y-dimension
      if (position.y < bounds.y)
         position.y = bounds.y + bounds.height;

      else if ((position.y + spaceOccupied.height) > (bounds.y + bounds.height))
         position.y = bounds.y - spaceOccupied.height;

      // Set the new update position of the
      // firefly.
      setSpaceOccupied(position);
   }
}
