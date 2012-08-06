//=================================================================
//                 SitesTree Panel
//=================================================================
//
// 	This class provides the construction of a sites tree
// panel that used in the ConnectionManager class for managing
// site connections and associated parameters.
//
//                 << SitesTreePanel.java >>
//
//=================================================================
// Copyright (C) 2006-2012 Dana M. Proctor
// Version 4.6 08/06/2012
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
// Version 1.0 Initial SitesTreePanel Class.
//         1.1 Class Methods createSiteTree(), and All addSite().
//         1.2 Implemented TreeLSelectionListener Interface in Class Method
//             valueChanged(). Class Instance connectionManager, Constructor
//             Argument, parent.
//         1.3 Modified Class Method removeCurrentNode to Properly Handle
//             the Removal of Sites and Database Leafs Along With Removing
//             Said in the sites Hashtable.
//         1.4 Added Class Method addSite() No Argument. Basic Functionality
//             in Place.
//         1.5 Fixed Adding Site, So No Duplicates. Also Insured Site
//             Addition Does Not Take Place At Leaf.
//         1.6 Added Class Method updateSiteNode, for Updating an Existed
//             Site Node Database.
//         1.7 Began Cleaning Out for Finalizing. Removed Two Unused addSite()
//             Class Methods.
//         1.8 Added Class Method renameSite().
//         1.9 Commented System.out Statements and General Cleanout. Finalized.
//         2.0 Allowed the Site Node Host to be Changed Along With Site Node
//             Name in Class Method renameSite().
//         2.1 Password Changes.
//         2.2 LeafIcon to Sites Tree.
//         2.3 InputDialog String Argument "cancel".
//         2.4 Code Cleanup. Commented Out Unused Interfaces Methods.
//         2.5 Used System.getProperty("file.separator") for All File System
//             Resources Accesses Through Instance fileSeparator.
//         2.6 Removed Instantiation of Instance oldSiteParameter in Class
//             Method renameSite().
//         2.7 Header Update.
//         2.8 Added Class Instance serialVersionUID. Removed Class Instance
//             toolkit.
//         2.9 MyJSQLView Project Common Source Code Formatting.
//         3.0 Add Constructor Instance iconsDirectory.
//         3.1 Class Name Change SiteParameter to SiteParameters.
//         3.2 Header Format Changes/Update.
//         3.3 Removded Constructor Instance fileSeparator. Obtained
//             iconsDirectory From MyJSQLView_Utils Class.
//         3.4 Added fileSeparator to iconsDirectory.
//         3.5 Changed Package to Reflect Dandy Made Productions Code.
//         3.6 Conditional Checks in Method addSite() & renameSite() From
//             Non-Short-Circut to Short-Circuit &&. Organized Imports SomeWhat.
//         3.7 Parameterized Class Instances siteNameCollection, & sites In
//             Order to Bring Code Into Compliance With Java 5.0 API. Same for
//             Argument sites in Constructor and Method Instance sitesTreeSet
//             in createSitesTree().
//         3.8 Parameterized Instances siteNames & sitesTreeIterator in Class
//             Method createSitesTree().
//         3.9 Addition of Internationalization Support to Class Methods addSite()
//             & renameSite(). Also Added Method showSiteExistsDialog().
//         4.0 Class Instance connectionManager Changed to connectionManagerFrame.
//             Constructor, valueChanged(), addSite(), updateSiteNode(), &
//             renameSite() Methods Effected.
//         4.1 Increased the PreferredSize of the treeScrollPane Instance.
//         4.2 Class Instance connectionManagerFrame Changed to loginManagerFrame.
//         4.3 Copyright Update.
//         4.4 Class Instance sites and Same Argument in Constructor Changed
//             from Hashtable to HashMap. Class Method createSitesTree() Same
//             Change for siteNodes, Also Change of siteNames from Enumeration
//             to Iterator. Class Instance siteNameCollection Change from Vector
//             to ArrayList.
//         4.5 Changes in Way MyJSQLView_ResourceBundle Handles the Collection
//             of Resource Strings. Change to resource.getResourceString(key,
//             default).
//         4.6 MyJSQLView Class Method Change of getLocaleResourceBundle()
//             to getResourceBundle().
//             
//-----------------------------------------------------------------
//                 danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.ArrayList;
import javax.swing.*;
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

/**
 *    The SitesTreePanel class provides the construction of a sites
 * tree panel that used in the ConnectionManager class for managing
 * site connections and associated parameters.
 * 
 * @author Dana M. Proctor
 * @version 4.6 08/06/2012
 */

class SitesTreePanel extends JPanel implements TreeModelListener, TreeSelectionListener
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

   protected SitesTreePanel(LoginManagerFrame parent,
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
      ImageIcon leafIcon = new ImageIcon(iconsDirectory + "newsiteLeafIcon.png");
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

   protected void addSite()
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

   protected DefaultMutableTreeNode addSite(DefaultMutableTreeNode parent, Object child,
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

   protected void updateSiteNode()
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

   protected void renameSite()
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

   protected void removeCurrentNode()
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