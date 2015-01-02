//=================================================================
//                   MyJSQLView's About Frame 
//=================================================================
//
//    This class provides the user access to information about
// the MyJSQLView application when the Help About selection is
// made in the menu bar.
//
//                   << AboutFrame.java >>
//
//=================================================================
// Copyright (C) 2005-2015 Dana M. Proctor
// Version 2.8 10/05/2013
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
// Version 1.0 Original AboutDialog Class.
//         1.1 Rename to AboutFrame. Rebuild With MyJSQLView
//             Icon Image and Credits Scroller.
//         1.2 Cleaned Up Javadoc Comments.
//         1.3 Header Update.
//         1.4 Class Instance serialVersionUID & Declaration
//             transient for WindowListener aboutFrameListener.
//         1.5 Declared WindowListener aboutFrameListener private.
//         1.6 MyJSQLView Project Common Source Code Formatting.
//         1.7 Additional Standardation to Comments.
//         1.8 Header Format Changes/Update.
//         1.9 Moved creditsPanel to Class Instance. Called suspendPanel on
//             same in WindowListener and actionPerformed() to Terminate.
//         2.0 Changed Package to Reflect Dandy Made Productions Code.
//         2.1 Implementation of Internationalization Through Instance 
//             resourceBundle in Constructor. Added Instance resource also
//             in Constructor.
//         2.2 Copyright Update.
//         2.3 Copyright Update.
//         2.4 Changes in Way MyJSQLView_ResourceBundle Handles the Collection
//             of Resource Strings. Change to resource.getResourceString(key,
//             default).
//         2.5 MyJSQLView Class Method Change of getLocaleResourceBundle()
//             to getResourceBundle().
//         2.6 Changed Package Name to com.dandymadeproductions.myjsqlview.gui.
//         2.7 Constructor Starting of creditsPanel Threading Done Locally Here.
//         2.8 Constructor Set Frame's Icon.
//         
//-----------------------------------------------------------------
//                 danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.dandymadeproductions.myjsqlview.MyJSQLView;
import com.dandymadeproductions.myjsqlview.gui.panels.CreditsPanel;
import com.dandymadeproductions.myjsqlview.utilities.MyJSQLView_ResourceBundle;
import com.dandymadeproductions.myjsqlview.utilities.MyJSQLView_Utils;

/**
 *    The AboutFrame class provides the user access to information about the
 * MyJSQLView application when the Help About selection is made in the menu bar.
 * 
 * @author Dana M. Proctor
 * @version 2.8 10/05/2013
 */

class AboutFrame extends JFrame implements ActionListener
{
   // Instance & Class Fields.
   private static final long serialVersionUID = -1109976899311733334L;

   private CreditsPanel creditsPanel;
   private JButton closeButton;

   //==============================================================
   // AboutDialog Constructor
   //==============================================================

   protected AboutFrame(String[] myJSQLView_Version, String webSiteString, ImageIcon logoIcon)
   {
      // Constructor Instances.
      MyJSQLView_ResourceBundle resourceBundle;
      String resource;
      
      JPanel mainPanel;
      JPanel centerPanel, southButtonPanel;

      // Setting up the frame.
      resourceBundle = MyJSQLView.getResourceBundle();
      resource = resourceBundle.getResourceString("AboutFrame.message.Title", "About");
      setTitle(resource + " MyJSQLView");
      setIconImage(MyJSQLView_Utils.getFrameIcon());

      GridBagLayout gridbag = new GridBagLayout();
      GridBagConstraints constraints = new GridBagConstraints();

      mainPanel = new JPanel(new BorderLayout());

      // Center panel and components
      centerPanel = new JPanel(gridbag);
      centerPanel.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));

      // MyJSQLView Icon
      JLabel iconLabel = new JLabel(logoIcon);

      buildConstraints(constraints, 0, 0, 1, 1, 5, 100);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.LINE_START;
      gridbag.setConstraints(iconLabel, constraints);
      centerPanel.add(iconLabel);

      // MyJSQLView Information/Credits
      creditsPanel = new CreditsPanel(myJSQLView_Version, webSiteString);
      Thread creditsPanelThread = new Thread(creditsPanel, "Credits");
      creditsPanelThread.start();

      buildConstraints(constraints, 1, 0, 1, 1, 95, 100);
      constraints.fill = GridBagConstraints.BOTH;
      constraints.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints(creditsPanel, constraints);
      centerPanel.add(creditsPanel);

      mainPanel.add(centerPanel, BorderLayout.CENTER);

      // South panel close button
      southButtonPanel = new JPanel();
      southButtonPanel.setBorder(BorderFactory.createEtchedBorder());

      resource = resourceBundle.getResourceString("AboutFrame.button.Close", "Close");
      closeButton = new JButton(resource);
      closeButton.setFocusPainted(false);
      closeButton.addActionListener(this);
      southButtonPanel.add(closeButton);

      mainPanel.add(southButtonPanel, BorderLayout.SOUTH);

      getContentPane().add(mainPanel);
      this.addWindowListener(aboutFrameListener);
   }

   //==============================================================
   // WindowListener for insuring that the dialog disapears if
   // the window is closed. (x).
   //==============================================================

   private transient WindowListener aboutFrameListener = new WindowAdapter()
   {
      public void windowClosing(WindowEvent e)
      {
         creditsPanel.suspendPanel(true);
         dispose();
      }
   };

   //==============================================================
   // ActionEvent Listener method for determining when the selections
   // have been made so an update can be performed or an appropriate
   // action taken.
   //==============================================================

   public void actionPerformed(ActionEvent evt)
   {
      Object frameSource = evt.getSource();

      // Overall action buttons.
      if (frameSource instanceof JButton)
      {
         // OK Button Action.
         if (frameSource == closeButton)
         {
            creditsPanel.suspendPanel(true);
            dispose();
         }
      }
   }

   //==============================================================
   // Class Method for helping the parameters in gridbag.
   //==============================================================

   private void buildConstraints(GridBagConstraints gbc, int gx, int gy, int gw,
                                 int gh, double wx, double wy)
   {
      gbc.gridx = gx;
      gbc.gridy = gy;
      gbc.gridwidth = gw;
      gbc.gridheight = gh;
      gbc.weightx = wx;
      gbc.weighty = wy;
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
