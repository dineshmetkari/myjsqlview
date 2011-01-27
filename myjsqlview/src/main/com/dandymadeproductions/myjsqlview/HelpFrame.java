//=================================================================
//           Help Frame in the MyJSQLView
//=================================================================
//     The HelpFrame is used to open a new JFrame window that
// displays help information in the MyJSQLView. The
// format displayed is of type html directly read from a file.
//
//                << HelpFrame.java >>
//
//=================================================================
// Copyright (C) 1999-2011 Dana M. Proctor
// Version 2.2 01/26/2011
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
// Verison 1.0 Original MyJSQLView HelpFrame.
//         1.1 Added helpFrameListener(), JButton Constructor
//             Argument & closeButton.
//         1.2 Added Class Method center().
//         1.3 Commented Out a System.out.println().
//         1.4 Set Output for Bad helpURL to Input htmlFile.
//             Code Cleanup.
//         1.5 Header Update.
//         1.6 Class Instance serialVersionUID & Declaration of
//             private transient WindowListener, helpFrameListener.
//         1.7 MyJSQLView Project Common Source Code Formatting.
//         1.8 Additional Standardation to Comments.
//         1.9 Header Format Changes/Update.
//         2.0 Changed Package to Reflect Dandy Made Productions Code.
//         2.1 Added Class Instance failedToLoadContents. Made the Catching
//             of Content Loading a Little More Robust With OptionPanes to
//             Display Error Information.
//         2.2 Copyright Update.
//         
//-----------------------------------------------------------------
//                 danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview;

import java.net.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;

//==================================================================
//                     MyJSQLView Help Frame
//==================================================================

/**
 * The HelpFrame class is used to display html help information.
 * 
 * @author Dana M. Proctor
 * @version 2.2 01/26/2011
 */

class HelpFrame extends JFrame
{
   // Instance variables as needed.
   private static final long serialVersionUID = -4143035957786665080L;

   private JEditorPane helpHTMLPane;
   private JScrollPane helpHTMLScrollPane;
   private JButton closeButton;
   protected boolean failedToLoadContents;

   //==============================================================
   // HelpFrame Constructor.
   //==============================================================

   protected HelpFrame(String frameTitle, String htmlFile, JButton closeButton)
   {
      // Setting the Frame Title.
      super(frameTitle);

      this.closeButton = closeButton;
      failedToLoadContents = false;

      // Setting up a scrollable html type editor pane.

      helpHTMLPane = new JEditorPane();
      helpHTMLPane.setEditable(false);

      // Obtaining the html document and adding to the
      // editor pane.

      URL helpURL = HelpFrame.class.getResource(htmlFile);
      // System.out.println(helpURL);

      if (helpURL != null)
      {
         try
         {
            helpHTMLPane.setPage(helpURL);
         }
         catch (IOException ioe)
         {
            String optionPaneStringErrors = "Unable to to SetPage - " + helpURL + "\n" + "IOException: "
                                             + ioe.toString();
            JOptionPane.showMessageDialog(null, optionPaneStringErrors, "Alert", JOptionPane.ERROR_MESSAGE);
            failedToLoadContents = true;
         }
      }
      else
      {
         String optionPaneStringErrors = "Unable to read URL - " + htmlFile;
         JOptionPane.showMessageDialog(null, optionPaneStringErrors, "Alert", JOptionPane.ERROR_MESSAGE);
         failedToLoadContents = true;
      }

      // Adding HyperLink Listener

      helpHTMLPane.addHyperlinkListener(new HyperlinkListener()
      {
         public void hyperlinkUpdate(HyperlinkEvent ev)
         {
            try
            {
               if (ev.getEventType() == HyperlinkEvent.EventType.ACTIVATED)
               {
                  helpHTMLPane.setPage(ev.getURL());
               }
            }
            catch (IOException ioe)
            {
               String optionPaneStringErrors = "Unable to Load HyperLink";
               JOptionPane.showMessageDialog(null, optionPaneStringErrors, "Alert", JOptionPane.ERROR_MESSAGE);
            }
         }
      });

      // Adding scrollbars to the editor pane and placing
      // in the frame.

      if (!failedToLoadContents)
      {
         helpHTMLScrollPane = new JScrollPane(helpHTMLPane);
         helpHTMLScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
         helpHTMLScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

         getContentPane().add(helpHTMLScrollPane, "Center");
      }
      
      this.addWindowListener(helpFrameListener);
   }

   //==============================================================
   // WindowListener for insuring that the dialog disapears if
   // the window is closed. (x).
   //==============================================================

   private transient WindowListener helpFrameListener = new WindowAdapter()
   {
      public void windowClosing(WindowEvent e)
      {
         if (closeButton != null)
            closeButton.doClick();
         dispose();
      }
   };

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