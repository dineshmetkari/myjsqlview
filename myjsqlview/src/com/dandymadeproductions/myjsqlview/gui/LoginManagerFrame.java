//=================================================================
//                      LoginManager Frame
//=================================================================
//    This class provides a frame that is accessed from the
// MyJSQLView_Access, login, class to create and save a user's
// connection sites. The class reads and outputs the connection
// sites' data to the myjsqlview.xml file.
//
//                 << LoginManagerFrame.java >>
//
//=================================================================
// Copyright (C) 2005-2012 Dana M. Proctor
// Version 5.8 10/18/2012
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
//         4.7 Increased Bounds Width for the Standard & AdvancedParametersPanels.
//             Increased the Size of Frame Setting in actionPerformed().
//         4.8 Pushed the advancedParametersPanel Over Some in its Bounds,
//             Constructor. Increased the Size of the Frame Slightly in
//             actionPerformed() for Advanced Action.
//         4.9 Renamed to LoginManagerFrame. All Instances That Reference The
//             Old ConnectionManager Changed to LoginManager.
//         5.0 Copyright Update.
//         5.1 Class Instance sitesClone & Same Argument in Constructor Change
//             from Hashtable to HashMap. Instance sitesKeys in Constructor
//             Changed from Enumeration to Iterator.
//         5.2 Change in Constructor for Creation of the sitesClone by Using
//             an EntrySet Iterator.
//         5.3 Changes in Way MyJSQLView_ResourceBundle Handles the Collection
//             of Resource Strings. Change to resource.getResourceString(key,
//             default).
//         5.4 Collection of All Image Resources Through resourceBundle.
//         5.5 Changed Package Name to com.dandymadeproductions.myjsqlview.gui.
//             Made Class, Constructor, & Getter/Setter Along With center()
//             Methods Public.
//         5.6 Moved SitesTreePanel Class Into This Code. Removed Import of
//             SiteParameters From Structures Since It Moved to this Classes'
//             Package.
//         5.7 Made Inner Class SitesTreePanel static.
//         5.8 Class Methods addSite() & renameSite() Dressed Up JTextField.
//        
//-----------------------------------------------------------------
//                 danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.dandymadeproductions.myjsqlview.MyJSQLView;
import com.dandymadeproductions.myjsqlview.gui.panels.AdvancedParametersPanel;
import com.dandymadeproductions.myjsqlview.gui.panels.StandardParametersPanel;
import com.dandymadeproductions.myjsqlview.utilities.InputDialog;
import com.dandymadeproductions.myjsqlview.utilities.MyJSQLView_ResourceBundle;
import com.dandymadeproductions.myjsqlview.utilities.MyJSQLView_Utils;

/**
 *    The LoginManagerFrame class provides a frame that is accessed
 * from the MyJSQLView_Access, login, class to create and save a
 * user's connection sites. The class reads and outputs the connection
 * sites' data to the myjsqlview.xml file.
 * 
 * @author Dana M. Proctor
 * @version 5.8 10/18/2012
 */

public class LoginManagerFrame extends JFrame implements ActionListener
{
   // Class Instances.
   private static final long serialVersionUID = -6318776869218785624L;
   
   private JMenuBar loginManagerMenuBar;
   private String resource;

   private SitesTreePanel treePanel;
   private HashMap<String, SiteParameters> sitesClone;

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
   // LoginManagerFrame Constructor
   //==============================================================

   public LoginManagerFrame(MyJSQLView_ResourceBundle resourceBundle,
                               HashMap<String, SiteParameters> sites,
                               StandardParametersPanel standardParametersPanel,
                               AdvancedParametersPanel advancedParametersPanel,
                               JButton saveExitButton, JButton cancelButton)
   {
      super("MyJSQLView Login Manager");

      this.standardParametersPanel = standardParametersPanel;
      this.advancedParametersPanel = advancedParametersPanel;
      
      // Constructor Instances
      String iconsDirectory;
      Iterator<Map.Entry<String, SiteParameters>> sitesIterator;
      JPanel mainPanel, centerPanel, actionPanel;
      
      iconsDirectory = MyJSQLView_Utils.getIconsDirectory() + MyJSQLView_Utils.getFileSeparator();

      sitesIterator = sites.entrySet().iterator();
      sitesClone = new HashMap <String, SiteParameters>();
      
      while (sitesIterator.hasNext())
      {
         String currentKey = sitesIterator.next().getKey();
         sitesClone.put(currentKey, sites.get(currentKey));
      }

      // ======================================================
      // Setting up the MenuBar that will be used to perform
      // the various site operations.

      loginManagerMenuBar = new JMenuBar();
      loginManagerMenuBar.setBorder(BorderFactory.createEtchedBorder());
      loginManagerMenuBar.setMargin(new Insets(0, 0, 0, 0));

      // New Site
      newSiteButton = new JButton(resourceBundle.getResourceImage(iconsDirectory
                                                                  + "newsiteIcon.png"));
      newSiteButton.setFocusable(false);
      newSiteButton.setMargin(new Insets(0, 0, 0, 0));
      resource = resourceBundle.getResourceString("LoginManagerFrame.tooltip.NewSite", "New Site");
      newSiteButton.setToolTipText(resource);
      newSiteButton.addActionListener(this);
      loginManagerMenuBar.add(newSiteButton);

      // Update Site
      updateSiteButton = new JButton(resourceBundle.getResourceImage(iconsDirectory
                                                                     + "updateSiteIcon.png"));
      updateSiteButton.setFocusable(false);
      updateSiteButton.setMargin(new Insets(0, 0, 0, 0));
      resource = resourceBundle.getResourceString("LoginManagerFrame.tooltip.UpdateSite", "Update Site");
      updateSiteButton.setToolTipText(resource);
      updateSiteButton.addActionListener(this);
      loginManagerMenuBar.add(updateSiteButton);

      // Rename
      renameSiteButton = new JButton(resourceBundle.getResourceImage(iconsDirectory
                                                                     + "renameSiteIcon.png"));
      renameSiteButton.setFocusable(false);
      renameSiteButton.setMargin(new Insets(0, 0, 0, 0));
      resource = resourceBundle.getResourceString("LoginManagerFrame.tooltip.RenameSite", "Rename Site");
      renameSiteButton.setToolTipText(resource);
      renameSiteButton.addActionListener(this);
      loginManagerMenuBar.add(renameSiteButton);

      // Delete
      deleteButton = new JButton(resourceBundle.getResourceImage(iconsDirectory
                                                                 + "removeIcon.png"));
      deleteButton.setFocusable(false);
      deleteButton.setMargin(new Insets(2, 2, 2, 2));
      resource = resourceBundle.getResourceString("LoginManagerFrame.tooltip.Delete", "Delete");
      deleteButton.setToolTipText(resource);
      deleteButton.addActionListener(this);
      loginManagerMenuBar.add(deleteButton);

      // Advanced
      advancedButton = new JButton(resourceBundle.getResourceImage(iconsDirectory
                                                                   + "advancedConnectionsIcon.png"));
      advancedButton.setFocusable(false);
      advancedButton.setMargin(new Insets(0, 0, 0, 0));
      resource = resourceBundle.getResourceString("LoginManagerFrame.tooltip.AdvancedParameters",
                                                  "Advanced Parameters");
      advancedButton.setToolTipText(resource);
      advancedButton.addActionListener(this);
      loginManagerMenuBar.add(advancedButton);

      setJMenuBar(loginManagerMenuBar);

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

      standardParametersPanel.setBounds(10, 0, 210, 200);
      centerPanel.add(standardParametersPanel);

      advancedParametersPanel.setBounds(235, 0, 210, 200);
      centerPanel.add(advancedParametersPanel);

      mainPanel.add(centerPanel, BorderLayout.CENTER);

      // ====================================================
      // Setting up the action button panel.

      actionPanel = new JPanel();
      actionPanel.setBorder(BorderFactory.createRaisedBevelBorder());

      // SSH Just Added Here For Convience
      sshUpIcon = resourceBundle.getResourceImage(iconsDirectory + "sshUpIcon.png");
      sshDownIcon = resourceBundle.getResourceImage(iconsDirectory + "sshDownIcon.png");
      resource = resourceBundle.getResourceString("LoginManagerFrame.checkbox.SSH", "SSH");
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
               setSize(505, 345);
               advancedOptionsShowing = false;
            }
            else
            {
               setSize(730, 345);
               advancedOptionsShowing = true;
            }
         }
      }
   }

   //==============================================================
   // Class method that gets the possibly modified sites Hashtable
   // by this class or the SitesTreePanel.
   //==============================================================

   public HashMap<String, SiteParameters> getSites()
   {
      return sitesClone;
   }

   //==============================================================
   // Class method that gets the state of the SSH Checkbox.
   //==============================================================

   public String getSSH()
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

   public void setSelectedSite(SiteParameters selectedSite)
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

   public void center(int offSetX, int offSetY)
   {
      Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
      Dimension us = getSize();
      int x = (screen.width - us.width) / 2 + 50;
      int y = (screen.height - us.height) / 2;
      setLocation(x + offSetX, y + offSetY);
   }
   
   /**
    *    The SitesTreePanel class provides the construction of a sites
    * tree panel that used in the ConnectionManager class for managing
    * site connections and associated parameters.
    * 
    * @author Dana M. Proctor
    * @version 4.9 09/19/2012
    */
   
   static class SitesTreePanel extends JPanel implements TreeModelListener, TreeSelectionListener
   {
      // Class Instances.
      private static final long serialVersionUID = -3291619646627993470L;

      private LoginManagerFrame loginManagerFrame;
      private DefaultMutableTreeNode sitesNode;
      private DefaultTreeModel treeModel;
      private JTree sitesTree;
      private ArrayList<String> siteNameCollection;
      private HashMap<String, SiteParameters> sites;

      private StandardParametersPanel standardParametersPanel;
      private AdvancedParametersPanel advancedParametersPanel;
      private MyJSQLView_ResourceBundle resourceBundle;

      //==============================================================
      // SitesTreePanel Constructor
      //==============================================================

      SitesTreePanel(LoginManagerFrame parent,
                     HashMap <String, SiteParameters> sites,
                     StandardParametersPanel standardParametersPanel,
                     AdvancedParametersPanel advancedParametersPanel)
      {
         this.loginManagerFrame = parent;
         this.sites = sites;
         this.standardParametersPanel = standardParametersPanel;
         this.advancedParametersPanel = advancedParametersPanel;

         // Setting up a file separator and other instances.
         
         String iconsDirectory = MyJSQLView_Utils.getIconsDirectory() + MyJSQLView_Utils.getFileSeparator();
         resourceBundle = MyJSQLView.getResourceBundle();

         // Panel Decor Stuff.
         setBorder(BorderFactory.createBevelBorder(1));

         // Setup the panel with the JTree.
         sitesNode = new DefaultMutableTreeNode("Sites");
         treeModel = new DefaultTreeModel(sitesNode);
         treeModel.addTreeModelListener(this);

         sitesTree = new JTree(treeModel);
         sitesTree.setBorder(BorderFactory.createLoweredBevelBorder());
         sitesTree.setEditable(false);
         sitesTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
         sitesTree.setShowsRootHandles(true);

         // Setting the leaf icon for nodes.
         ImageIcon leafIcon = resourceBundle.getResourceImage(iconsDirectory + "newsiteLeafIcon.png");
         if (leafIcon != null)
         {
            DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
            renderer.setLeafIcon(leafIcon);
            sitesTree.setCellRenderer(renderer);
         }

         sitesTree.addTreeSelectionListener(this);

         JScrollPane treeScrollPane = new JScrollPane(sitesTree);
         treeScrollPane.setPreferredSize(new Dimension(250, 230));
         treeScrollPane.setAutoscrolls(true);
         add(treeScrollPane);

         createSitesTree();
      }

      //==============================================================
      // Class method to create the sites JTree parent child nodes
      // from the sites Hashtable.
      //==============================================================

      private void createSitesTree()
      {
         // Class Method Instances.
         DefaultMutableTreeNode currentSiteNode;
         String siteKey, siteName, databaseName;
         HashMap<String, DefaultMutableTreeNode> siteNodes;
         TreeSet<String> sitesTreeSet;

         Iterator<String> siteNames;
         Iterator<String> sitesTreeIterator;

         // Create a collection of site names.

         siteNameCollection = new ArrayList <String>();
         siteNames = sites.keySet().iterator();

         while (siteNames.hasNext())
         {
            siteKey = siteNames.next();

            if (!siteKey.equals("Last Site") && siteKey.indexOf('#') != -1)
            {
               siteName = siteKey.substring(0, siteKey.indexOf('#'));

               if (!siteNameCollection.contains(siteName))
                  siteNameCollection.add(siteName);
            }
         }

         // Provide a natural order of the site names
         // and adding to the JTree.
    
         sitesTreeSet = new TreeSet <String>(siteNameCollection);
         
         siteNodes = new HashMap <String, DefaultMutableTreeNode>();
         sitesTreeIterator = sitesTreeSet.iterator();

         while (sitesTreeIterator.hasNext())
         {
            siteName = sitesTreeIterator.next();
            currentSiteNode = addSite(null, siteName, false);
            siteNodes.put(siteName, currentSiteNode);
         }

         // Populating the JTree with the databases associated
         // with each site.

         siteNames = sites.keySet().iterator();

         while (siteNames.hasNext())
         {
            siteKey = (String) siteNames.next();

            if (!siteKey.equals("Last Site") && siteKey.indexOf('#') != -1)
            {
               siteName = siteKey.substring(0, siteKey.indexOf('#'));
               databaseName = siteKey.substring(siteKey.indexOf('#') + 1);
               addSite(siteNodes.get(siteName), databaseName, false);
            }
         }
      }

      //==============================================================
      // TreeModelEvent Listener methods for detecting the changes
      // that take place on the JTree Component.
      //==============================================================

      public void treeNodesChanged(TreeModelEvent e)
      {
         /*
          * DefaultMutableTreeNode node; node =
          * (DefaultMutableTreeNode)(e.getTreePath().getLastPathComponent());
          * //System.out.println(e.getTreePath()); try { int index =
          * e.getChildIndices()[0]; node =
          * (DefaultMutableTreeNode)(node.getChildAt(index)); } catch
          * (NullPointerException exc) { //System.out.println("treeNodeChanged was
          * null"); }
          */
      }

      public void treeNodesInserted(TreeModelEvent e)
      {
         // DefaultMutableTreeNode node;
         // node =
         // (DefaultMutableTreeNode)(e.getTreePath().getLastPathComponent());
         // System.out.println("Tree Nodes Inserted: " + e.getTreePath() + " " +
         // e.getChildIndices()[0]);
      }

      public void treeNodesRemoved(TreeModelEvent e)
      {
         // DefaultMutableTreeNode node;
         // node =
         // (DefaultMutableTreeNode)(e.getTreePath().getLastPathComponent());
         // System.out.println("Tree Nodes Removed: " + e.getTreePath() + " " +
         // e.getChildIndices()[0]);
      }

      public void treeStructureChanged(TreeModelEvent e)
      {
         // DefaultMutableTreeNode node;
         // node =
         // (DefaultMutableTreeNode)(e.getTreePath().getLastPathComponent());
         // System.out.println("Tree Structure Changed: " + e.getTreePath());
      }

      //==============================================================
      // TreeSelection Listener method for detecting the selection of
      // nodes in the JTree and setting the appropriate data in the
      // ConnectionManager comboboxes.
      //==============================================================

      public void valueChanged(TreeSelectionEvent e)
      {
         // Class Method Instances
         DefaultMutableTreeNode node;

         // Make sure there really is a node selected.
         node = (DefaultMutableTreeNode) sitesTree.getLastSelectedPathComponent();
         if (node == null)
            return;

         // Looks like we have a site so if it is a
         // database leaf show parameters.
         if (node.isLeaf() && node.getParent() != sitesNode)
         {
            String siteName = node.getParent().toString() + "#" 
                              + node.getUserObject().toString();
            // System.out.println(siteName);

            SiteParameters selectedSite = sites.get(siteName);
            loginManagerFrame.setSelectedSite(selectedSite);
         }
      }

      //==============================================================
      // Class method to add a new site or database to an existing
      // node in the JTree of the panel.
      //==============================================================

      void addSite()
      {
         String newSite;
         String child = "";
         DefaultMutableTreeNode parentNode = null;
         TreePath selectedTreePath;
         String resource, resourceOK, resourceCancel;

         // Obtain the whole tree path so the
         // appropriate addtion can take place.
         selectedTreePath = sitesTree.getSelectionPath();

         // Set new node parent on main tree root
         if (selectedTreePath == null)
            parentNode = sitesNode;

         // Set new node parent to existing host, new
         // node may not be added to a leaf host
         // database though.
         else
         {
            parentNode = (DefaultMutableTreeNode) (selectedTreePath.getLastPathComponent());
            if (parentNode.isLeaf() && parentNode.getParent() != sitesNode)
               return;
         }

         // Add to root
         if (parentNode == sitesNode)
         {
            // Create a dialog to obtain the new
            // site node name.
            InputDialog siteNameDialog;
            JLabel message;
            
            resource = resourceBundle.getResourceString("SitesTreePanel.message.EnterNewSiteName",
                                                        "Enter New Site Name");
            message = new JLabel(resource, JLabel.CENTER);
            
            JTextField siteNameTextField = new JTextField(10);
            siteNameTextField.setBorder(BorderFactory.createCompoundBorder(
               BorderFactory.createEtchedBorder(EtchedBorder.RAISED),
               BorderFactory.createLoweredBevelBorder()));
            Object[] content = {message, siteNameTextField};
            
            resource = resourceBundle.getResourceString("SitesTreePanel.dialogTitle.SiteDialog",
                                                        "Site Dialog");
            resourceOK = resourceBundle.getResourceString("SitesTreePanel.button.OK", "OK");
            resourceCancel = resourceBundle.getResourceString("SitesTreePanel.button.Cancel", "Cancel");
            
            // Obtaining the new site name.
            siteNameDialog = new InputDialog(loginManagerFrame, resource, resourceOK, resourceCancel,
                                             content, null);
            siteNameDialog.pack();
            siteNameDialog.center();
            siteNameDialog.setResizable(false);
            siteNameDialog.setVisible(true);

            // Obtain new site name.
            if (siteNameDialog.isActionResult())
            {
               newSite = siteNameTextField.getText();
               // System.out.println(newSite);

               // Check to see if trying to add an
               // already existing site or empty string.

               if (!siteNameCollection.contains(newSite) && !newSite.equals(""))
               {
                  siteNameDialog.dispose();
                  parentNode = addSite(parentNode, newSite, false);
               }
               else
               {
                  siteNameDialog.dispose();
                  showSiteExistsDialog();
                  return;
               }
            }
            // Canceled Dialog.
            else
            {
               siteNameDialog.dispose();
               return;
            }
         }

         // Create the new SiteParmeter to store.
         SiteParameters newSiteParameters = new SiteParameters();
         newSiteParameters.setSiteName(parentNode + "#" + standardParametersPanel.getDataBase());
         newSiteParameters.setDriver(advancedParametersPanel.getDriver());
         newSiteParameters.setProtocol(advancedParametersPanel.getProtocol());
         newSiteParameters.setSubProtocol(advancedParametersPanel.getSubProtocol());
         newSiteParameters.setPort(advancedParametersPanel.getPort());

         // Make sure the host for the site maintains some
         // consistensty.
         if (parentNode.getChildCount() == 0)
            newSiteParameters.setHost(standardParametersPanel.getHost());
         else
         {
            String childString = parentNode.getChildAt(0).toString();
            String hostString = (sites.get(parentNode.toString() 
                                + "#" + childString)).getHost();
            newSiteParameters.setHost(hostString);
         }
         newSiteParameters.setDatabase(standardParametersPanel.getDataBase());
         newSiteParameters.setUser(standardParametersPanel.getUser());
         newSiteParameters.setPassword(standardParametersPanel.getPassword());
         newSiteParameters.setSsh(loginManagerFrame.getSSH());

         child = standardParametersPanel.getDataBase();
         sites.put((parentNode + "#" + child), newSiteParameters);
         addSite(parentNode, child, true);
      }

      //==============================================================
      // Class method to add a site to the JTree in the panel.
      //==============================================================

      DefaultMutableTreeNode addSite(DefaultMutableTreeNode parent, Object child,
                                               boolean shouldBeVisible)
      {
         DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(child);

         if (parent == null)
            parent = sitesNode;

         treeModel.insertNodeInto(childNode, parent, parent.getChildCount());

         // Expand the select node to be seen.
         if (shouldBeVisible)
            sitesTree.scrollPathToVisible(new TreePath(childNode.getPath()));

         return childNode;
      }

      //==============================================================
      // Class method to update an existing site database node.
      //==============================================================

      void updateSiteNode()
      {
         String siteName;
         DefaultMutableTreeNode currentNode, parentNode;
         TreePath selectedTreePath;

         // Obtain the parent path so the
         // appropriate addtion can take place.
         selectedTreePath = sitesTree.getSelectionPath();

         // No node selected on main tree.
         if (selectedTreePath == null)
            return;

         // A node appears to be selected so collect the
         // node data and check to see if selected node
         // is a site leaf
         else
         {
            currentNode = (DefaultMutableTreeNode) (selectedTreePath.getLastPathComponent());
            parentNode = (DefaultMutableTreeNode) currentNode.getParent();

            if (currentNode.isLeaf() && currentNode.getParent() != sitesNode)
            {
               // Proceed to create a new updated SiteParameter.
               SiteParameters newSiteParameters = new SiteParameters();

               siteName = parentNode + "#" + currentNode;

               newSiteParameters.setSiteName(siteName);
               newSiteParameters.setHost((sites.get(siteName)).getHost());
               newSiteParameters.setDatabase(standardParametersPanel.getDataBase());
               newSiteParameters.setUser(standardParametersPanel.getUser());
               newSiteParameters.setPassword(standardParametersPanel.getPassword());
               newSiteParameters.setSsh(loginManagerFrame.getSSH());
               newSiteParameters.setDriver(advancedParametersPanel.getDriver());
               newSiteParameters.setProtocol(advancedParametersPanel.getProtocol());
               newSiteParameters.setSubProtocol(advancedParametersPanel.getSubProtocol());
               newSiteParameters.setPort(advancedParametersPanel.getPort());

               // Update the sites and JTree structures.
               int nodeIndex = parentNode.getIndex(currentNode);

               treeModel.removeNodeFromParent(currentNode);
               sites.remove(siteName);

               DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(standardParametersPanel
                     .getDataBase());
               treeModel.insertNodeInto(childNode, parentNode, nodeIndex);
               sitesTree.scrollPathToVisible(new TreePath(childNode.getPath()));

               siteName = parentNode + "#" + standardParametersPanel.getDataBase();
               sites.put(siteName, newSiteParameters);
            }
         }
      }

      //==============================================================
      // Class method to rename a site. Here is where we find out
      // that it may be better to create your own Tree Model based
      // on the data structure used. Apparently can not just rename
      // a node? Have to rebuild the tree? Sometimes its just better
      // to go ahead and do things the hard way at first to simply
      // life later, like this below.
      //==============================================================

      void renameSite()
      {
         String siteName;
         String resource, resourceOK, resourceCancel;
         DefaultMutableTreeNode selectedNode, parentNode, newSiteNode;
         TreePath selectedTreePath;

         // Obtain the parent path & nodes so the
         // appropriate addtion can take place.
         selectedTreePath = sitesTree.getSelectionPath();

         // No node selected on main tree.
         if (selectedTreePath == null)
            return;

         // A node appears to be selected so collect the
         // node data and check to see if selected node
         // is a site.
         else
         {
            selectedNode = (DefaultMutableTreeNode) (selectedTreePath.getLastPathComponent());
            parentNode = sitesNode;

            // Create a dialog to obtain a new name for the site.
            // Pre-setup
            InputDialog siteNameDialog;
            JLabel message;
            
            resource = resourceBundle.getResourceString("SitesTreePanel.message.EnterNewSiteName",
                                                        "Enter New Site Name");
            message = new JLabel(resource, JLabel.CENTER);

            JTextField siteNameTextField = new JTextField(10);
            siteNameTextField.setBorder(BorderFactory.createCompoundBorder(
               BorderFactory.createEtchedBorder(EtchedBorder.RAISED),
               BorderFactory.createLoweredBevelBorder()));
            Object[] content = {message, siteNameTextField};
            
            resource = resourceBundle.getResourceString("SitesTreePanel.dialogTitle.SiteDialog",
                                                        "Site Dialog");  
            resourceOK = resourceBundle.getResourceString("SitesTreePanel.button.OK", "OK");
            resourceCancel = resourceBundle.getResourceString("SitesTreePanel.button.Cancel", "Cancel");
            
            siteNameDialog = new InputDialog(loginManagerFrame, resource, resourceOK, resourceCancel,
                                             content, null);
            siteNameDialog.pack();
            siteNameDialog.center();
            siteNameDialog.setResizable(false);

            // Normal site rename.
            if (!selectedNode.isLeaf() && selectedNode.getParent() == sitesNode)
            {
               siteNameDialog.setVisible(true);

               // Obtain new site name.
               if (siteNameDialog.isActionResult())
               {
                  String newSite = siteNameTextField.getText();
                  String oldSiteName = selectedNode.toString();

                  // Check to see if trying to add an
                  // already existing site or empty string.

                  if (!siteNameCollection.contains(newSite) && !newSite.equals(""))
                  {
                     siteNameDialog.dispose();
                     String currentNewSite, currentSiteDatabase;
                     SiteParameters oldSiteParameter;

                     // Create the new renamed site node.
                     newSiteNode = addSite(parentNode, newSite, false);

                     // Cycle through the old child nodes to be added to the
                     // new site name node..
                     int childNumber = selectedNode.getChildCount();

                     for (int i = 0; i < childNumber; i++)
                     {
                        // Proceed to create a new updated SiteParameter.
                        SiteParameters newSiteParameter = new SiteParameters();

                        siteName = oldSiteName + "#" + selectedNode.getChildAt(i).toString();
                        oldSiteParameter = sites.get(siteName);
                        currentNewSite = newSite + "#" + oldSiteParameter.getDatabase();

                        newSiteParameter.setSiteName(currentNewSite);

                        // Allow change of host along with site node name.
                        if (oldSiteParameter.getHost().equals(standardParametersPanel.getHost()))
                           newSiteParameter.setHost(oldSiteParameter.getHost());
                        else
                           newSiteParameter.setHost(standardParametersPanel.getHost());

                        currentSiteDatabase = oldSiteParameter.getDatabase();
                        newSiteParameter.setDatabase(currentSiteDatabase);
                        newSiteParameter.setUser(oldSiteParameter.getUser());
                        newSiteParameter.setPassword(oldSiteParameter.getPassword());
                        newSiteParameter.setSsh(oldSiteParameter.getSsh());
                        newSiteParameter.setDriver(oldSiteParameter.getDriver());
                        newSiteParameter.setProtocol(oldSiteParameter.getProtocol());
                        newSiteParameter.setSubProtocol(oldSiteParameter.getSubProtocol());
                        newSiteParameter.setPort(oldSiteParameter.getPort());

                        sites.put(currentNewSite, newSiteParameter);
                        sites.remove(siteName);

                        // Insert the database node in the rename site.
                        DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(currentSiteDatabase);
                        treeModel.insertNodeInto(childNode, newSiteNode, i);

                     }
                     // Remove old site name and scroll to.
                     treeModel.removeNodeFromParent(selectedNode);
                     sitesTree.scrollPathToVisible(new TreePath(newSiteNode.getPath()));
                  }
                  else
                  {
                     siteNameDialog.dispose();
                     showSiteExistsDialog();
                     return;
                  }
               }
               // Canceled Dialog.
               else
               {
                  siteNameDialog.dispose();
                  return;
               }
            }
            // For some reason the user has created a site, but
            // it has no nodes, databases associated with it. So
            // just rename the site. The data will be lost though
            // after a save since there is no data to write for
            // a SiteParameter.
            else if (selectedNode.isLeaf() && selectedNode.getParent() == sitesNode)
            {
               siteNameDialog.setVisible(true);

               // Obtain new site name.
               if (siteNameDialog.isActionResult())
               {
                  String newSite = siteNameTextField.getText();
                  // System.out.println(newSite);

                  // Check to see if trying to add an
                  // already existing site or empty string.

                  if (!siteNameCollection.contains(newSite) && !newSite.equals(""))
                  {
                     siteNameDialog.dispose();
                     treeModel.removeNodeFromParent(selectedNode);
                     addSite(parentNode, newSite, false);
                  }
                  else
                  {
                     siteNameDialog.dispose();
                     showSiteExistsDialog();
                     return;
                  }
               }
               // Canceled Dialog.
               else
               {
                  siteNameDialog.dispose();
                  return;
               }
            }
         }
      }
      
      //==============================================================
      // Class method to show a dialog indicating an error of site
      // already exists.
      //==============================================================

      private void showSiteExistsDialog()
      {
         String resourceTitle, resourceMessage;
         
         resourceTitle = resourceBundle.getResourceString("SitesTreePanel.dialogTitle.Alert", "Alert");
         resourceMessage = resourceBundle.getResourceString("SitesTreePanel.dialogMessage.SiteAlreadyExist",
                                                            "Site Already Exists or Empty String!");
         
         JOptionPane.showMessageDialog(null, resourceMessage, resourceTitle,
                                       JOptionPane.ERROR_MESSAGE);
      }

      //==============================================================
      // Class method to remove the currently selected node in the
      // JTree be it a site or site/database.
      //==============================================================

      void removeCurrentNode()
      {
         // Class Method Instances
         TreePath currentSelection;
         DefaultMutableTreeNode currentNode;
         MutableTreeNode parent;
         String siteName;

         // Insure a node path is avaiable.
         currentSelection = sitesTree.getSelectionPath();

         if (currentSelection != null)
         {
            // Looks like a valid node so remove, and clear the
            // SiteParameter from the sites Hashtable.

            currentNode = (DefaultMutableTreeNode) currentSelection.getLastPathComponent();
            parent = (MutableTreeNode) currentNode.getParent();

            if (parent != null)
            {
               // Remove just the leaf/database.
               if (currentNode.isLeaf())
               {
                  siteName = currentNode.getParent().toString() + "#" 
                             + currentNode.getUserObject().toString();
                  // System.out.println(siteName);
                  if (sites.containsKey(siteName))
                     sites.remove(siteName);
               }
               // Remove all leaves/databases from site.
               else
               {
                  for (int i = 0; i < currentNode.getChildCount(); i++)
                  {
                     siteName = currentNode.getUserObject().toString() + "#"
                                + currentNode.getChildAt(i).toString();
                     // System.out.println(siteName);
                     if (sites.containsKey(siteName))
                        sites.remove(siteName);
                  }
               }
               treeModel.removeNodeFromParent(currentNode);
               return;
            }
         }
         Toolkit.getDefaultToolkit().beep();
      }
   }
}