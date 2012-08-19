//=================================================================
//                   MyJSQLView ProgressBar
//=================================================================
//
//    This class provides the frame and components to create a basic
// independent progress bar with a cancel button.
//
//                << MyJSQLView_ProgressBar.java >>
//
//=================================================================
// Copyright (C) 2005-2012 Dana M. Proctor
// Version 3.0 08/19/2012
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
// Version 1.0 Original MyJSQLView_ProgressBar Class.
//         1.1 Replaced Deprecated Method show() With setVisible().
//         1.2 Removed in Constructor setVisible(true) and pack(). Left to Calling
//             Classes.
//         1.3 Added Class Method setCanceled().
//         1.4 Cleaned Up Javadoc Comments.
//         1.5 Header Update.
//         1.6 Added Class Instance serialVersionUID
//         1.7 MyJSQLView Project Common Source Code Formatting.
//         1.8 Minor Code Cleanup.
//         1.9 Changed Class Access From Public to More Restrictive.
//         2.0 Header Format Changes/Update.
//         2.1 Added Instances iconsDirectory, progressBarIcon, and animationLabel
//             to Constructor in Creation of Animation for Frame.
//         2.2 Removed Constructor Instance fileSeparator. Obtained iconsDirectory
//             From MyJSQLView_Utils Class.
//         2.3 Added fileSeparator to iconsDirectory.
//         2.4 Changed Package to Reflect Dandy Made Productions Code.
//         2.5 Added Class Instance resourceBundle and Implemented Internationalization.
//         2.6 Copyright Update.
//         2.7 Copyright Update.
//         2.8 Changes in Way MyJSQLView_ResourceBundle Handles the Collection
//             of Resource Strings. Change to resource.getResourceString(key,
//             default).
//         2.9 MyJSQLView Class Method Change of getLocaleResourceBundle()
//             to getResourceBundle().
//         3.0 Collection of All Image Resources Through resourceBundle.
//
//-----------------------------------------------------------------
//                danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

/**
 *    The MyJSQLView_ProgressBar class provides the frame and
 * components to create a basic independent progress bar with a
 * cancel button.
 * 
 * @author Dana M. Proctor
 * @version 3.0 08/19/2012
 */

class MyJSQLView_ProgressBar extends JFrame implements ActionListener
{
   // Class Instances

   private static final long serialVersionUID = -9154480981519054457L;

   private MyJSQLView_ResourceBundle resourceBundle;
   private JProgressBar progressBar;
   private JButton cancelButton;
   private int taskLength;
   private boolean taskCanceled;

   //==============================================================
   // MyJSQLView_ProgressBar Constructor.
   //==============================================================

   protected MyJSQLView_ProgressBar(String progressTitle)
   {
      super(progressTitle);
      
      // Constructor Instances
      String iconsDirectory, resource;
      ImageIcon progressBarIcon;
      
      // Setup various instances to be used in the panel.
      
      resourceBundle = MyJSQLView.getResourceBundle();
      iconsDirectory = MyJSQLView_Utils.getIconsDirectory() + MyJSQLView_Utils.getFileSeparator();
      progressBarIcon = resourceBundle.getResourceImage(iconsDirectory + "progressBarIcon.gif");
      
      // Create the components.
      this.getContentPane().setLayout(new BorderLayout());

      JPanel mainPanel = new JPanel();
      mainPanel.setBorder(BorderFactory.createEtchedBorder());

      resource = resourceBundle.getResourceString("MyJSQLView_ProgressBar.button.Cancel", "Cancel");
      cancelButton = new JButton(resource);
      cancelButton.setActionCommand("cancel");
      cancelButton.setFocusable(false);
      cancelButton.addActionListener(this);
      mainPanel.add(cancelButton);

      progressBar = new JProgressBar();
      progressBar.setIndeterminate(true);
      progressBar.setValue(0);
      progressBar.setStringPainted(true);
      mainPanel.add(progressBar);

      this.getContentPane().add(mainPanel, BorderLayout.CENTER);
      
      JLabel animationLabel = new JLabel(progressBarIcon);
      
      this.getContentPane().add(animationLabel, BorderLayout.SOUTH);

      taskCanceled = false;
   }

   //==============================================================
   // ActionEvent Listener method for detecting the inputs
   // from the panel and taking the appropriate action.
   //==============================================================

   public void actionPerformed(ActionEvent evt)
   {
      taskCanceled = true;
   }

   //==============================================================
   // Class Method to set the taskCanceled instance.
   //==============================================================

   protected void setCanceled(boolean value)
   {
      taskCanceled = value;
   }

   //==============================================================
   // Class Method to set the current status bar display value.
   //==============================================================

   protected void setCurrentValue(int value)
   {
      progressBar.setValue(value);
   }

   //==============================================================
   // Class Method to set the maximum value of the status bar
   // that will be displayed.
   //==============================================================

   protected void setTaskLength(int value)
   {
      progressBar.setIndeterminate(false);
      taskLength = value;
      progressBar.setMaximum(taskLength);
   }

   //==============================================================
   // Class Method to return the indication of the button cancel
   // being pressed.
   //==============================================================

   protected boolean isCanceled()
   {
      return taskCanceled;
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