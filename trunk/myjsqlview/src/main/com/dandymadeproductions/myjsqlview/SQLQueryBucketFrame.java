//=================================================================
//                  SQLQueryBucketFrame Class
//=================================================================
//
//    This class provides the main frame for the storage of SQL Query
// statements derived from MyJSQLView.
//
//                << SQLQueryBucketFrame.java >>
//
//=================================================================
// Copyright (C) 2005-2011 Dana M. Proctor
// Version 1.0 03/17/2011
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
// Version 1.0 03/17/2011 Initial SQLQueryBucketFrame Class.
//         
//-----------------------------------------------------------------
//                 danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JFrame;

//=================================================================
//                  MyJSQLView SQLQueryBucketFrame
//=================================================================

/**
 *     This SQLQueryBucketFrame class provides the main frame for the storage
 * of SQL Query statements derived from MyJSQLView.  
 * 
 * @author Dana M. Proctor
 * @version 1.0 03/17/2011
 */

public class SQLQueryBucketFrame extends JFrame implements ActionListener
{
   // Class Instances.
   private static final long serialVersionUID = -1903579213881382782L;

   

   //==============================================================
   // SQLQueryBucketFrame Constructor
   //==============================================================

   protected SQLQueryBucketFrame()
   {
      // Constructor Instances.
      String iconsDirectory;
      MyJSQLView_ResourceBundle resourceBundle;
      String resource;
      
      // Setting up the needed elements and title.

      iconsDirectory = MyJSQLView_Utils.getIconsDirectory() + MyJSQLView_Utils.getFileSeparator();
      resourceBundle = MyJSQLView.getLocaleResourceBundle();
      
      resource = resourceBundle.getResource("QueryBucketFrame.message.Title");
      if (resource.equals(""))
         setTitle("MyJSQLView Query Bucket Frame");
      else
         setTitle("MyJSQLView " + resource);
      
      
      
      
      
      
      
      // ==================================================
      // Frame Window Closing Addition. Also method for
      // reactivating if desired/needed.
      // ==================================================

      WindowListener sqlQueryBucketFrameListener = new WindowAdapter()
      {
         public void windowClosing(WindowEvent e)
         {
            setVisible(false);
            MyJSQLView_JMenuBarActions.setSQLQueryBucketFrameNotVisisble();
         }

         public void windowActivated(WindowEvent e)
         {
         }
      };

      this.addWindowListener(sqlQueryBucketFrameListener);
      
   }

   //==============================================================
   // ActionEvent Listener method for detecting the user's selection
   // of various components in the frame and taking the appropriate
   // action as required.
   //==============================================================

   public void actionPerformed(ActionEvent evt)
   {
      Object panelSource = evt.getSource();

      // Button Actions
      if (panelSource instanceof JButton)
      {
         
      }
   }
   
   //==============================================================
   // Class method to add a SQL statement to the bucket's list.
   //==============================================================

   public void addSQLStatement(String sqlStatement)
   {
      setVisible(true);
      System.out.println(sqlStatement);
   }
   
   //==============================================================
   // Class method to center the frame.
   //==============================================================

   protected void center()
   {
      Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
      Dimension us = getSize();
      int x = (screen.width - us.width) / 2;
      int y = (screen.height - us.height) / 2;
      setLocation(x, y);
   }
}