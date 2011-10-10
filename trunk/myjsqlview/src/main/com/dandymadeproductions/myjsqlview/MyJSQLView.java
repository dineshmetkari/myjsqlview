//=================================================================
//                   MyJSQLView Class
//=================================================================
//
//    This class is used to control the running of the MyJSQLView
// application. Initial action is to bring up a login access frame,
// LoginFrame, to connect to a desired database. Once a valid
// connection has been made then the main framework for the
// application will be created through the MyJSQLView_Frame class
// to setup the main user interface.
//
//             Arguments -debug, -lang='locale'
//
//                  << MyJSQLView.java >>
//
//=================================================================
// Copyright (C) 2005-2011 Dana M. Proctor
// Version 3.31 10/10/2011
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
// Version 1.0 12/23/2005 MyJSQLView Main Application.
//         1.1 01/06/2006 Removed Unused Instances & Debugging System.outs.
//         1.2 01/07/2006 Cleaned up code.
//         1.3 01/11/2006 Changed Icon for Access JPG.
//         1.4 01/17/2006 Changed Icon Back to PNG.
//         1.5 01/18/2006 Table Info String Obtained from MyJSQLView_Access
//                        Static Method.
//         1.6 01/22/2006 Multiple Tabs Each a Table in DB.
//         1.7 02/13/2006 Class Instance standardDateFormat, & Associated Class
//                        Methods.
//         1.8 02/14/2006 Removed Class Instance standardDateFormat, &
//                        Associated Class Methods.
//         1.9 04/23/2006 Class Methods setDataDelimiter() & getDataDelimiter().
//         2.0 05/29/2006 Replaced Deprecated Method show().
//         2.1 07/30/2006 Changed Class Name to MyJSQLView.
//         2.2 07/30/2006 Class Instance webSiteString String Correction.
//         2.3 09/30/2006 Beta MyJSQLView Release Version 2.30.
//         2.4 10/15/2006 Beta MyJSQLView Documentation Update/Inclusion
//                        Version 2.40.
//         2.5 10/22/2006 Beta Revision Update for TimeStamp Display Bug. Data
//                        Format Returned by Different Systems?
//         2.6 11/01/2006 Changed Driver Check to MyJSQLView_Access for
//                        Connection Loading of Application to One Place.
//         2.61 11/05/2006 Test Code for New GUI Access and Temporary Bug Fix
//                         for Date String Field.
//         2.62 11/19/2006 Removed Class Method main() and All remants of the
//                         Old Login Method. MyJSQLView_Access() Class Now
//                         Handles Login, and If Successful Creates This Class.
//         2.63 11/29/2006 Release Version 2.63 Build String Updated.
//         2.64 01/28/2007 Removed Set Look & Feel to MyJSQLView_Access Class.
//                         Updated myJSQLView_Version String.
//         2.65 04/20/2007 Comment Changes.
//         2.66 04/25/2007 Added Class Method getTab().
//         2.67 05/02/2007 Added Class Instance dataProperties. Change of
//                         Class Methods to set/getDataDelimiter() to Use this
//                         Class Instance.
//         2.68 05/02/2007 Added Class Methods get/setSQLDataProperties().
//         2.69 05/28/2007 Removed Developers Information From Class Instance
//                         myJSQLView_Version.
//         2.70 06/03/2007 Class SQLDataExportProperties Renamed to
//                         DataExportProperties.
//         2.71 06/04/2007 Removed Class Methods get/setDelimiterString. Changed
//                         Class Methods get/setSQLDataProperties to
//                         get/setDataExportProperties.
//         2.72 06/18/2007 Class Instance myJSQLView_Version Update for
//                         Release 2.72.
//         2.73 07/23/2007 Added Host:Database to Super() for myJSQLViewFrame.
//         2.74 09/02/2007 Added Class Method getTableTabCount().
//         2.75 09/09/2007 Removed Argument Instance myJSQLViewFrame From Call
//                         to MyJSQLView_JMenuBarActions.actionsSelection in
//                         Class Method actionPerformed().
//         2.76 09/27/2007 Class Instance myJSQLView_Version Update for Release
//                         2.76.
//         2.77 10/14/2007 TableTabPanel Creation Based on New Abastract Class
//                         to Properly Select Database Type in Class Method
//                         createGUI().
//         2.78 10/21/2007 Cleaned Up Javadoc Comments.
//         2.79 12/02/2007 Class Instances dataImportProperties &
//                         dataExportProperties. Added Class Method 
//                         getDataImportPropeties() & setDataImportProperties.
//         2.80 12/23/2007 Changed Name of get/setDataExportProperties(). Updated
//                         Version Information & Build.
//         2.81 01/07/2008 Class Instance myJSQLView_Version Update for Release
//                         2.81.
//         2.82 01/20/2008 Expansion of Proper Selection of Correct TableTabPanel
//                         in Class Method createGUI().
//         2.83 01/25/2008 Comment Changes.
//         2.84 02/03/2008 TableTabPanel Constructor Argument JFrame Removed.
//         2.85 03/16/2008 Conditional Check in createGUI() to Determine if Problems
//                         Occur In Loading a Database Table So That Its Associated
//                         TableTabPanel Will Not Be Loaded.
//         2.86 03/19/2008 Class Instance myJSQLView_Version Update for Release
//                         2.86.
//        2.861 03/29/2008 Development Build.
//        2.862 04/14/2008 Development Build.
//         2.87 04/16/2008 Class Instance myJSQLView_Version Update for Release
//                         2.87.
//         2.88 05/12/2008 Inclusion of Selection of TableTabPanle_Oracle in
//                         Class Method createGUI().
//         2.89 05/15/2008 Passed myJSQLViewFrame to MyJSQLView_JMenuBarActions
//                         actionsSelection() Method.
//         2.90 05/24/2008 Changed each Table Tab's Name to Corespond to Just the
//                         Table Name and Added schema.tablename as Tooltip in
//                         Class Method createGUI().
//         2.91 08/15/2008 Class Instance myJSQLView_Version Update for Release
//                         2.91.
//         2.92 10/06/2008 Test 2.91++ Development GUI.
//         2.93 10/11/2008 Conversion to New GUI, With MyJSQLView Class Being a
//                         Top Container to Manage the Various Tabs, Database
//                         Tables, Plugins etc. Removal of Method getTabCount(),
//                         getSelectedTab(), & getTab().
//         2.94 10/20/2008 MyJSQLView Project Common Source Code Formatting.
//         2.95 10/21/2008 Additional Standardation to Comments.
//         2.96 10/25/2008 Added Constructor Instance fileSeparator & iconsDirectory.
//         2.97 10/30/2008 Complete Redesign to Again Make This the Main Starting
//                         Point of the Application. Removed Getter & Setters for
//                         Import/ExportProperties().
//         2.98 10/31/2008 Reviewed and Cleaned Up Some, Comment Changes.
//         2.99 11/13/2008 Added Class Method getDebug(). Removed Class Instance debug
//                         From Constructor Instantiation for MyJSQLView_Access().
//         3.00 12/17/2008 Added Class Method getVersion().
//         3.01 12/28/2008 Class Instance myJSQLView_Version Update for Release
//                         3.01.
//         3.02 05/27/2009 Header Format Changes/Update.
//         3.03 06/14/2009 Class Instance myJSQLView_Version Update for Release
//                         3.03.
//         3.04 10/15/2009 Test 3.03++ Development.
//         3.05 11/18/2009 Class Instance myJSQLView_Version Update for Release
//                         3.05.
//         3.06 12/03/2009 Class Instance myJSQLView_Version Update for Release
//                         3.06.
//         3.07 02/18/2010 Changed Package to Reflect Dandy Made Productions Code.
//         3.08 02/20/2010 Added Class Instances localeString, baseName, &
//                         myjsqlviewResourceBundle. Changed Behavior in main() to
//                         Collect Arguments. Constructor Added Code to Create a
//                         ResourceBundle. Added Class Method getLocaleResourceBundle().
//         3.09 02/22/2010 Class Method main() MyJSQLView_Utils.processLocaleLanguage().
//         3.10 02/23/2010 Removed Class Instance, from 3.08, baseName. Removed Checks
//                         in Constructor for localeString. Changed myjsqlviewResouceBundle
//                         Instance to New MyJSQLView Class MyJSQLView_BundleResource.
//         3.11 02/23/2010 Changed Instance myjsqlviewResourceBundle to resourceBundle.
//                         myjsqlviewPopupMenu Content Obtained From resourceBundle.
//         3.12 02/25/2010 Comment Change and Organized Imports.
//         3.13 02/26/2010 Updated myJSQLView_Version To Properly Keep Track of State
//                         of Code for 3.06++ Development.
//         3.14 03/07/2010 Changed Valid Argument -locale to -lang in main(). Updated Version.
//         3.15 04/26/2010 Moved the Creating & Assigning of the MyJSQLView_JMenuBar to
//                         the MyJSQLViewFrame createGUI() Method.
//         3.16 04/28/2010 Removed myJSQLViewFrame.createGUI() in actionPerformed().
//         3.17 05/06/2010 What Else Added myJSQLViewFrame.createGUI() Back Into actionPerformed().
//                         Needed to Enable the Passing of the Frame's ActionEvent Handler
//                         to Plugins.
//         3.18 05/15/2010 Updated Documentation for Class by Including the Argument -lang.
//         3.19 06/27/2010 Added Class Method getLocaleString() and baseName Argument to 
//                         MyJSQLView_ResourceBundle in Constructor. Made Class Public and
//                         Methods getLocaleString() & getVersion().
//         3.20 06/27/2010 Added localeDirectory String to MyJSQLView_RsourceBundle Instance
//                         Creation.
//         3.21 06/28/2010 Returned a Copy of myJSQLView_Version String Array in getLocaleString().
//                         Some Minor Reformatting.
//         3.22 07/13/2010 Class Instance myJSQLView_Version Update for Release
//         3.23 09/15/2010 Class Instance myJSQLView_Version Update for Release
//                         3.23.
//         3.24 01/26/2011 Instance Change of MyJSQLView_Access to LoginFrame. Addition of
//                         Class Instance connectionManager and Its Getter Method.
//         3.25 01/27/2011 Increased The Width for the Sizing of the loginFrame in Constructor.
//         3.26 03/14/2011 Class Instance myJSQLView_Version Update for Release
//                         3.26.
//         3.27 05/11/2011 Made Class Instance myJSQLViewPopupListner Public Along With
//                         Class Method getPopupMenuListener().
//         3.28 05/14/2011 Changed Class Instance myJSQLViewPopupListener Back to private.
//         3.29 05/22/2011 Class Instance myJSQLView_Version Update for Release
//                         3.29.
//         3.30 08/11/2011 Comment Changes, Update to Class Instance myJSQLView_Version
//                         for Release 3.30.
//         3.31 10/10/2011 Comment Changes, Update to Class Instance myJSQLView_Version
//                         for Release 3.31.
//         
//-----------------------------------------------------------------
//                 danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.UIManager;
import javax.swing.text.DefaultEditorKit;

//=================================================================
//                     MyJSQLView Application
//=================================================================

/**
 *    The MyJSQLView class is used to control the running of the
 * MyJSQLView application. Initial action is to bring up a login
 * access frame, LoginFrame, to connect to a desired database.
 * Once a valid connection has been made then the main framework
 * for the application will be created through the MyJSQLView_Frame
 * class to setup the main user interface.
 * 
 * Arguments -debug, -lang='locale'
 * 
 * @author Dana M. Proctor
 * @version 3.31 10/10/2011
 */

public class MyJSQLView implements ActionListener
{
   // =============================================
   // Creation of the necessary class instance
   // variables for the MyJSQLView user interface.
   // =============================================

   // MyJSQLView Main Frame and Panels.
   private static boolean debug;
   private static String localeString;
   private JButton validLoginButton;

   private ConnectionManager connectionManager;
   private LoginFrame loginFrame;
   protected MyJSQLView_Frame myJSQLViewFrame;
   private static MouseListener myJSQLViewPopupListener;
   private static MyJSQLView_ResourceBundle resourceBundle;

   // String for Information About the MyJSQLView.
   private static String[] myJSQLView_Version = {"MyJSQLView", "3.31", "Build ID: 20111010"};
   private String webSiteString = "http://myjsqlview.org";

   //==============================================================
   // MyJSQLView Constructor
   //==============================================================

   public MyJSQLView()
   {
      // Constructor Instances.
      JPopupMenu myJSQLViewPopupMenu;
      JMenuItem menuItem;
      String popupMenuResource;

      // ==================================================
      // Setting up the look and feel of the frame
      // by accessing the user's system configuration.

      try
      {
         UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
         // System.out.println("UIManager Look & Feel Set");
      }
      catch (Exception e)
      {
         System.err.println("Can't set look and feel.");
      }
      
      // ==================================================
      // Obtain resouce bundle for internationalization.
      
      resourceBundle = new MyJSQLView_ResourceBundle("locale", "MyJSQLViewBundle", localeString);
      
      // ==================================================
      // Setting up a PopupMenu for cut, copy, and pasting.

      myJSQLViewPopupMenu = new JPopupMenu();

      menuItem = new JMenuItem(new DefaultEditorKit.CutAction()); 
      
      popupMenuResource = resourceBundle.getResource("myJSQLViewPopupMenu.action.Cut");
      if (popupMenuResource.equals(""))
         menuItem.setText("Cut");
      else
         menuItem.setText(popupMenuResource);
      menuItem.setMnemonic(KeyEvent.VK_X);
      menuItem.addActionListener(this);
      myJSQLViewPopupMenu.add(menuItem);

      menuItem = new JMenuItem(new DefaultEditorKit.CopyAction());
      
      popupMenuResource = resourceBundle.getResource("myJSQLViewPopupMenu.action.Copy");
      if (popupMenuResource.equals(""))
         menuItem.setText("Copy");
      else
         menuItem.setText(popupMenuResource);
      menuItem.setMnemonic(KeyEvent.VK_C);
      menuItem.addActionListener(this);
      myJSQLViewPopupMenu.add(menuItem);

      menuItem = new JMenuItem(new DefaultEditorKit.PasteAction());
      
      popupMenuResource = resourceBundle.getResource("myJSQLViewPopupMenu.action.Paste");
      if (popupMenuResource.equals(""))
         menuItem.setText("Paste");
      else
         menuItem.setText(popupMenuResource);
      menuItem.setText("Paste");
      menuItem.setMnemonic(KeyEvent.VK_V);
      menuItem.addActionListener(this);
      myJSQLViewPopupMenu.add(menuItem);

      myJSQLViewPopupListener = new MyJSQLView_MouseAdapter(myJSQLViewPopupMenu);

      // ==================================================
      // Setting up a component that will allow the
      // determination of a valid login to the database.

      validLoginButton = new JButton();
      validLoginButton.addActionListener(this);

      // ==================================================
      // Show the database login access frame.
      
      connectionManager = new ConnectionManager();
      loginFrame = new LoginFrame(validLoginButton);
      loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      loginFrame.addMouseListener(myJSQLViewPopupListener);
      loginFrame.setSize(355, 320);
      loginFrame.setResizable(false);
      loginFrame.center();
      loginFrame.setVisible(true);
   }

   //==============================================================
   // ActionEvent Listener method for detecting the user's valid
   // login to a database with the MyJSQLView_Acces class frame.
   //==============================================================

   public void actionPerformed(ActionEvent evt)
   {
      Object panelSource = evt.getSource();

      // Button Actions
      if (panelSource instanceof JButton)
      {
         if (panelSource == validLoginButton)
         {
            // Dispose the login access frame.

            loginFrame.dispose();

            // Create the MyJSQLView main application frame.

            myJSQLViewFrame = new MyJSQLView_Frame(myJSQLView_Version, webSiteString);
            myJSQLViewFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            myJSQLViewFrame.createGUI();
            myJSQLViewFrame.addMouseListener(myJSQLViewPopupListener);
            myJSQLViewFrame.setSize(800, 600);
            myJSQLViewFrame.setVisible(true);
         }
      }
   }

   //============================================================
   // Main public access point method for instantiating the
   // MyJSQLView application. Valid Arguments: -debug, -lang.
   //
   // Example Startup: java -jar MyJSQLView.jar -debug -lang=en_US
   //==============================================================

   public static void main(String[] args)
   {
      debug = false;
      localeString = "";
      
      // Collect allowed arguments.
      if (args != null)
      {
         for (int i = 0; i < args.length; i++)
         {
            if (args[i].equals("-debug"))
               debug = true;
            if (args[i].indexOf("-lang") != -1)
            {
               if (args[i].indexOf("=") != -1)
                  localeString = args[i].substring(args[i].indexOf("=") + 1);
            }
         }
      }

      // Create the Login and Access.
      java.awt.EventQueue.invokeLater(new Runnable()
      {
         public void run()
         {
            // Try to setup/retrieve language support
            // if not provided as an argument.
            
            if (localeString.equals(""))
               localeString = MyJSQLView_Utils.processLocaleLanguage();
            
            // Getter On!
            
            new MyJSQLView();
         }
      });
   }
   
   //==============================================================
   // Class Method to return to the Connection Manager.
   //==============================================================
   
   public ConnectionManager getConnectionManager()
   {
      return connectionManager;
   }
   
   //==============================================================
   // Class Method to return the debug argument.
   //==============================================================

   protected static boolean getDebug()
   {
      return debug;
   }
   
   //==============================================================
   // Class Method to return the resource bundle requred for program
   // internationlization.
   //==============================================================

   protected static MyJSQLView_ResourceBundle getLocaleResourceBundle()
   {
      return resourceBundle;
   }
   
   //==============================================================
   // Class Method to return the locale, language, string selection.
   //==============================================================

   public static String getLocaleString()
   {
      return localeString;
   }
   
   //==============================================================
   // Class Method to return to temporary panels a JPopupMenu for
   // cutting, coping, and pasteing.
   //==============================================================

   public static MouseListener getPopupMenuListener()
   {
      return myJSQLViewPopupListener;
   }
   
   //==============================================================
   // Class Method to return to the MyJSQLView version.
   //==============================================================

   public static String[] getVersion()
   {
      String[] versionCopy = new String[myJSQLView_Version.length];
      
      for (int i = 0; i < myJSQLView_Version.length; i++)
         versionCopy[i] = myJSQLView_Version[i];
      
      return versionCopy;
   }
}