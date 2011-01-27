//=================================================================
//                ConnectionManager Frame
//=================================================================
//    This class provides a frame that is accessed from the
// MyJSQLView_Access, login, class to create and save a user's
// connection sites. The class reads and outputs the connection
// sites' data to the myjsqlview.xml file.
//
//               << ConnectionManagerFrame.java >>
//
//=================================================================
// Copyright (C) 2005-2011 Dana M. Proctor
// Version 4.6 01/27/2011
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
// Version 1.0 Initial ConnectionManager Class.
//         1.1 Setup Basic Class Instances Required.
//         1.2 Beginning GUI Construction.
//         1.3 All Major Components Added.
//         1.4 Replaced Parameters ComboBoxes with
//             StandardParametersPanel & AdvancedParametersPanel.
//         1.5 JMenu Addition to Handle Site Tree Operations.
//             Removed Class Method buildConstraints().
//         1.6 Added Creation of JTree to the Class SitesTreePanel.
//         1.7 Removed ComboBoxes Components and Created Class
//             Method setSelectedSite(). Passed this to the
//             SitesTreePanel.
//         1.8 Created a Class Instance sitesClone to Be Passed
//             to SitesTreePanel and Returned by New Class Method
//             getSites().
//         1.9 Class Method actionPerformed() Add Site Action.
//         2.0 Passed standardParametersPanel & advancedParametersPanel
//             to SitesTreePanel.
//         2.1 Class Method getSSH();
//         2.2 Removed New Folder and Copy Buttons/Actions. Added
//             Update Button/Action.
//         2.3 Call to Class Method SitesTreePanel.renameSite() in
//             actionPerformed().
//         2.4 Modified Class Method setSelectedSites() to Check for
//             Valid siteName Before Loading Panel.
//         2.5 Layout Change centerPanel Layout to Null. Manually Setting Bounds
//             on Content Panels. Overall Sizing Changes.
//         2.6 Added MouseListener to mainPanel.
//         2.7 Removed Class Instances sites, saveExitButtion, & cancelButton.
//         2.8 Removed Constructor Instance currentSiteParameter.
//         2.9 Used System.getProperty("file.separator") for All File System
//             Resources Accesses Through Instance fileSeparator.
//         3.0 Cleaned Up Javadoc Comments.
//         3.1 Header Update.
//         3.2 Added Class Instance serialVersionUID.
//         3.3 MyJSQLView Project Common Source Code Formatting.
//         3.4 Additional Standardation to Comments.
//         3.5 Instances deleteButton, updateSiteButton, renameSiteButton,
//             sshUpIcon, & sshDownIcon Changed Icon File Names. Add Constructor
//             Instance iconsDirectory.
//         3.6 MouseListener Changed to be Obtained From the MyJSQLView Class
//             in Constructor.
//         3.7 Class Name Change SiteParameter to SiteParameters.
//         3.8 Header Format Changes/Update.
//         3.9 Removed Constructor Instance fileSeparator. Obtained iconsDirectory
//             From MyJSQLView_Utils Class.
//         4.0 Added fileSeparator to iconsDirectory.
//         4.1 Changed Package to Reflect Dandy Made Productions Code.
//         4.2 Constructor Argument resourceBundle Added and Constructor
//             Instance resource. Implementation of Internationalization.
//         4.3 Parameterized Class Instance sitesClone and Return Type of Same for
//             Class Method getSites() In Order to Properly Comply with Java
//             5.0 API.
//         4.4 Parameterized Constructor Instance siteKeys.
//         4.5 Renamed to ConnectionManagerFrame.
//         4.6 Correction to resourceBundle.getResource() to Correctly Identify
//             New Class Name ConnectionManagerFrame in Constructor.
//        
//-----------------------------------------------------------------
//                 danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 *    The ConnectionManagerFrame class provides a frame that is accessed
 * from the MyJSQLView_Access, login, class to create and save a
 * user's connection sites. The class reads and outputs the connection
 * sites' data to the myjsqlview.xml file.
 * 
 * @author Dana M. Proctor
 * @version 4.6 01/27/2011
 */

class ConnectionManagerFrame extends JFrame implements ActionListener
{
   // Class Instances.
   private static final long serialVersionUID = -6318776869218785624L;
   
   private JMenuBar connectionManagerMenuBar;
   private String resource;

   private SitesTreePanel treePanel;
   private Hashtable<String, SiteParameters> sitesClone;

   private StandardParametersPanel standardParametersPanel;
   private AdvancedParametersPanel advancedParametersPanel;

   private JCheckBox sshCheckBox;
   private ImageIcon sshUpIcon, sshDownIcon;

   private JButton newSiteButton;
   private JButton updateSiteButton;
   private JButton renameSiteButton;
   private JButton deleteButton;
   private JButton advancedButton;

   private boolean advancedOptionsShowing = false;

   //==============================================================
   // ConnectionManagerFrame Constructor
   //==============================================================

   protected ConnectionManagerFrame(MyJSQLView_ResourceBundle resourceBundle,
                               Hashtable<String, SiteParameters> sites,
                               StandardParametersPanel standardParametersPanel,
                               AdvancedParametersPanel advancedParametersPanel,
                               JButton saveExitButton, JButton cancelButton)
   {
      super("MyJSQLView Connection Manager");

      this.standardParametersPanel = standardParametersPanel;
      this.advancedParametersPanel = advancedParametersPanel;
      
      // Constructor Instances
      String iconsDirectory;
      Enumeration<String> sitesKeys;
      JPanel mainPanel, centerPanel, actionPanel;
      
      iconsDirectory = MyJSQLView_Utils.getIconsDirectory() + MyJSQLView_Utils.getFileSeparator();

      sitesKeys = sites.keys();
      sitesClone = new Hashtable <String, SiteParameters>();
      while (sitesKeys.hasMoreElements())
      {
         String currentKey = sitesKeys.nextElement();
         sitesClone.put(currentKey, sites.get(currentKey));
         // System.out.println(currentKey);
      }

      // ======================================================
      // Setting up the MenuBar that will be used to perform
      // the various site operations.

      connectionManagerMenuBar = new JMenuBar();
      connectionManagerMenuBar.setBorder(BorderFactory.createEtchedBorder());
      connectionManagerMenuBar.setMargin(new Insets(0, 0, 0, 0));

      // New Site
      newSiteButton = new JButton(new ImageIcon(iconsDirectory + "newsiteIcon.png"));
      newSiteButton.setFocusable(false);
      newSiteButton.setMargin(new Insets(0, 0, 0, 0));
      resource = resourceBundle.getResource("ConnectionManagerFrame.tooltip.NewSite");
      if (resource.equals(""))
         newSiteButton.setToolTipText("New Site");
      else
         newSiteButton.setToolTipText(resource);
      newSiteButton.addActionListener(this);
      connectionManagerMenuBar.add(newSiteButton);

      // Update Site
      updateSiteButton = new JButton(new ImageIcon(iconsDirectory + "updateSiteIcon.png"));
      updateSiteButton.setFocusable(false);
      updateSiteButton.setMargin(new Insets(0, 0, 0, 0));
      resource = resourceBundle.getResource("ConnectionManagerFrame.tooltip.UpdateSite");
      if (resource.equals(""))
         updateSiteButton.setToolTipText("Update Site");
      else
         updateSiteButton.setToolTipText(resource);
      updateSiteButton.addActionListener(this);
      connectionManagerMenuBar.add(updateSiteButton);

      // Rename
      renameSiteButton = new JButton(new ImageIcon(iconsDirectory + "renameSiteIcon.png"));
      renameSiteButton.setFocusable(false);
      renameSiteButton.setMargin(new Insets(0, 0, 0, 0));
      resource = resourceBundle.getResource("ConnectionManagerFrame.tooltip.RenameSite");
      if (resource.equals(""))
         renameSiteButton.setToolTipText("Rename Site");
      else
         renameSiteButton.setToolTipText(resource);
      renameSiteButton.addActionListener(this);
      connectionManagerMenuBar.add(renameSiteButton);

      // Delete
      deleteButton = new JButton(new ImageIcon(iconsDirectory + "removeIcon.png"));
      deleteButton.setFocusable(false);
      deleteButton.setMargin(new Insets(2, 2, 2, 2));
      resource = resourceBundle.getResource("ConnectionManagerFrame.tooltip.Delete");
      if (resource.equals(""))
         deleteButton.setToolTipText("Delete");
      else
         deleteButton.setToolTipText(resource);
      deleteButton.addActionListener(this);
      connectionManagerMenuBar.add(deleteButton);

      // Advanced
      advancedButton = new JButton(new ImageIcon(iconsDirectory + "advancedConnectionsIcon.png"));
      advancedButton.setFocusable(false);
      advancedButton.setMargin(new Insets(0, 0, 0, 0));
      resource = resourceBundle.getResource("ConnectionManagerFrame.tooltip.AdvancedParameters");
      if (resource.equals(""))
         advancedButton.setToolTipText("Advanced Parameters");
      else
         advancedButton.setToolTipText(resource);
      advancedButton.addActionListener(this);
      connectionManagerMenuBar.add(advancedButton);

      setJMenuBar(connectionManagerMenuBar);

      // Setting up the main panel that will be needed
      // in the frame.

      mainPanel = new JPanel(new BorderLayout());
      mainPanel.setBorder(BorderFactory.createEtchedBorder());
      mainPanel.addMouseListener(MyJSQLView.getPopupMenuListener());

      // ====================================================
      // Setting up the sites JTree structure panel and
      // adding to the panel.

      treePanel = new SitesTreePanel(this, sitesClone, standardParametersPanel, advancedParametersPanel);
      mainPanel.add(treePanel, BorderLayout.WEST);

      // ====================================================
      // Adding constructor arguments standard & advanced
      // connection parameters panels with various components.

      centerPanel = new JPanel();
      centerPanel.setLayout(null);

      standardParametersPanel.setBounds(10, 0, 190, 200);
      centerPanel.add(standardParametersPanel);

      advancedParametersPanel.setBounds(210, 0, 180, 200);
      centerPanel.add(advancedParametersPanel);

      mainPanel.add(centerPanel, BorderLayout.CENTER);

      // ====================================================
      // Setting up the action button panel.

      actionPanel = new JPanel();
      actionPanel.setBorder(BorderFactory.createRaisedBevelBorder());

      // SSH Just Added Here For Convience
      sshUpIcon = new ImageIcon(iconsDirectory + "sshUpIcon.png");
      sshDownIcon = new ImageIcon(iconsDirectory + "sshDownIcon.png");
      resource = resourceBundle.getResource("ConnectionManagerFrame.checkbox.SSH");
      if (resource.equals(""))
         sshCheckBox = new JCheckBox("SSH", sshUpIcon);
      else
         sshCheckBox = new JCheckBox(resource, sshUpIcon);
      sshCheckBox.setSelectedIcon(sshDownIcon);
      sshCheckBox.setHorizontalTextPosition(SwingConstants.LEADING);
      sshCheckBox.setFocusPainted(false);
      actionPanel.add(sshCheckBox);

      actionPanel.add(saveExitButton);
      actionPanel.add(cancelButton);

      mainPanel.add(actionPanel, BorderLayout.SOUTH);
      getContentPane().add(mainPanel);
   }

   //==============================================================
   // ActionEvent Listener method for detecting the inputs
   // from the panels and directing to the appropriate routine.
   //==============================================================

   public void actionPerformed(ActionEvent evt)
   {
      Object panelSource = evt.getSource();

      if (panelSource instanceof JButton)
      {
         JButton actionButton = (JButton) panelSource;

         // New Site Action
         // Selected node must be on root or existing
         // site node.
         if (actionButton == newSiteButton)
         {
            treePanel.addSite();
         }

         // Update Site Action
         // Selected node must be a leaf. May
         // not update site site node. This update
         // parameters for site.
         if (actionButton == updateSiteButton)
         {
            treePanel.updateSiteNode();
         }

         // Rename Action
         // Selected node is a site node. The
         // name and host may be changed. Site
         // leafs remain unchanged.
         if (actionButton == renameSiteButton)
         {
            treePanel.renameSite();
         }

         // Delete Action
         // Selected node other than root is
         // deleted.
         if (actionButton == deleteButton)
         {
            treePanel.removeCurrentNode();
         }

         // Advanced Action
         if (actionButton == advancedButton)
         {
            if (advancedOptionsShowing)
            {
               setSize(420, 310);
               advancedOptionsShowing = false;
            }
            else
            {
               setSize(615, 310);
               advancedOptionsShowing = true;
            }
         }
      }
   }

   //==============================================================
   // Class method that gets the possibly modified sites Hashtable
   // by this class or the SitesTreePanel.
   //==============================================================

   protected Hashtable<String, SiteParameters> getSites()
   {
      return sitesClone;
   }

   //==============================================================
   // Class method that gets the state of the SSH Checkbox.
   //==============================================================

   protected String getSSH()
   {
      if (sshCheckBox.isSelected())
         return "1";
      else
         return "0";
   }

   //==============================================================
   // Class method used for setting values in the components of
   // the login interface, aka. connection parameters.
   //==============================================================

   protected void setSelectedSite(SiteParameters selectedSite)
   {
      // Class Method Instances.
      String siteName;

      // Debug & Check.
      siteName = selectedSite.getSiteName();
      // System.out.println(siteName);

      if (siteName != null)
      {
         // panel component setting.
         advancedParametersPanel.setDriver(selectedSite.getDriver());
         advancedParametersPanel.setProtocol(selectedSite.getProtocol());
         advancedParametersPanel.setSubProtocol(selectedSite.getSubProtocol());
         standardParametersPanel.setHostItem(selectedSite.getHost());
         advancedParametersPanel.setPort(selectedSite.getPort());
         standardParametersPanel.setDatabaseItem(selectedSite.getDatabase());
         standardParametersPanel.setUserItem(selectedSite.getUser());
         standardParametersPanel.setPassword(selectedSite.getPassword());
         if (selectedSite.getSsh().equals("0"))
            sshCheckBox.setSelected(false);
         else
            sshCheckBox.setSelected(true);
      }
   }

   //==============================================================
   // Class method to center the frame. Offset slightly.
   //==============================================================

   protected void center(int offSetX, int offSetY)
   {
      Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
      Dimension us = getSize();
      int x = (screen.width - us.width) / 2 + 50;
      int y = (screen.height - us.height) / 2;
      setLocation(x + offSetX, y + offSetY);
   }
}