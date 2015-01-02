//=================================================================
//             MyJSQLView FocusTraversalPolicy
//=================================================================
//
//    This class provides a means for panels within the
// MyJSQLView application to set a desired component focus
// sequence for themselves.
//
//          << MyJSQLView_FocusTraversalPolicy.java >>
//
//=================================================================
// Copyright (C) 2005-2015 Dana M. Proctor
// Version 2.1 09/11/2012
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
// Version 1.0 Original MyJSQLView_FocusTraversalPolicy Class.
//         1.1 Cleaned Up Javadoc Comments.
//         1.2 Header Update.
//         1.3 MyJSQLView Project Common Source Code Formatting.
//         1.4 Header Format Changes/Update.
//         1.5 Changed Package to Reflect Dandy Made Productions Code.
//         1.6 Organized Imports and Parameterized Class Instance
//             componentSequence and Argument to Constructor components
//             to Bring Code Into Compliance With Java 5.0 API,
//         1.7 Parameterized componentsIterator in Constructor.
//         1.8 Copyright Update.
//         1.9 Copyright Update.
//         2.0 Changed Class Instance componentSequence from Vector Data
//             Type to ArrayList.
//         2.1 Changed Package Name to com.dandymadeproductions.myjsqlview.utilities.
//             Made Class & Constructor Public.
//
//-----------------------------------------------------------------
//                 danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview.utilities;

import java.awt.Component;
import java.awt.Container;
import java.awt.FocusTraversalPolicy;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *    The MyJSQLView_FocusTraversalPolicy class provides a means for
 * panels within the MyJSQLView application to set a desired component
 * focus sequence for themselves.
 * 
 * @author Dana M. Proctor
 * @version 2.1 09/11/2012
 */

public class MyJSQLView_FocusTraversalPolicy extends FocusTraversalPolicy
{
   // Class Instances.
   private ArrayList<Component> componentSequence = new ArrayList <Component>();
   private int lastIndex = 0;

   //==============================================================
   // MyJSQLView_FocusTraversalPolicy Constructor
   //==============================================================

   public MyJSQLView_FocusTraversalPolicy(ArrayList<Component> components)
   {
      Iterator<Component> componentsIterator = components.iterator();

      while (componentsIterator.hasNext())
      {
         componentSequence.add(componentsIterator.next());
      }
   }

   //==============================================================
   // Class method to return the default component that should
   // be focused.
   //==============================================================

   public Component getDefaultComponent(Container focusCycleRoot)
   {
      return (Component) componentSequence.get(0);
   }

   //==============================================================
   // Class method to return the first component that should
   // be focused.
   //==============================================================

   public Component getFirstComponent(Container focusCycleRoot)
   {
      return (Component) componentSequence.get(0);
   }

   //==============================================================
   // Class method to return the last component that should
   // be focused.
   //==============================================================

   public Component getLastComponent(Container focusCycleRoot)
   {
      return (Component) componentSequence.get(componentSequence.size() - 1);
   }

   //==============================================================
   // Class method to return the next component given the current
   // focused component.
   //==============================================================

   public Component getComponentAfter(Container focusCycleRoot, Component aComponent)
   {
      int currentLocation = componentSequence.indexOf(aComponent);
      int nextIndex;
      // System.out.println(currentLocation +" "+ lastIndex);

      if (currentLocation != -1)
      {
         if (currentLocation >= componentSequence.size() - 1)
         {
            lastIndex = 0;
            return (Component) componentSequence.get(0);
         }
         else
         {
            lastIndex = currentLocation;
            return (Component) componentSequence.get(currentLocation + 1);
         }
      }
      else
      {
         if (lastIndex == 0)
            nextIndex = lastIndex + 1;
         else
            nextIndex = lastIndex + 2;
         // System.out.println("Fault setting index to " + nextIndex);
         lastIndex = lastIndex + 1;
         return (Component) componentSequence.get(nextIndex);
      }
   }

   //==============================================================
   // Class method to return the previous component given the
   // current focused component.
   //==============================================================

   public Component getComponentBefore(Container focusCycleRoot, Component aComponent)
   {
      int currentLocation = componentSequence.indexOf(aComponent);

      if (currentLocation <= 0)
         return (Component) componentSequence.get(componentSequence.size() - 1);
      else
         return (Component) componentSequence.get(currentLocation - 1);
   }
}
