//=================================================================
//                     LoginFrame Class 
//=================================================================
//
//   This class provides the framework for a login access dialog
// to a database. The class provides the main frame used to allow
// the user entry to the MyJSQLView application. Several input
// preferences must be specified in this login in order to establish
// a valid connection to a database. 
//
//                   << LoginFrame.java >>
//
//=================================================================
// Copyright (C) 2005-2012 Dana M. Proctor
// Version 6.82 01/01/2012
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
// Version 1.0 Original AccessDialog Class.
//         1.1 Conversion to Required Frame for New Access Interface Allowing
//             Connection Manager Access.
//         1.2 JMenuBar for Access ot ConnectionManager, SelectConnection,
//             and Advanced Options.
//         1.3 Rename from AccessDialog to Replace MyJSQLView_Access.
//         1.4 Finished Class's Main Functionality.
//         1.5 Changed Advanced Button ImageIcon Name.
//         1.6 Class Method displaySQLErrors(), Add Alert Dialog to Give
//             Users Information About Errors Occuring.
//         1.7 Connection Manager Access.
//         1.8 Removed WindowFrameListener.
//         1.9 Release Version 2.63 Site Manager Play, Class Method
//             actionPerformed().
//         2.0 Implemented XMLTranslator getSites() to Fill JMenu Site Toolbar.
//         2.1 Modified Class Method displaySQLErrors(), e.getMessage()
//             Chopped if Larger Than 200 Charaters.
//         2.2 Implemented Parameters ComboBoxes Filling With Sites in
//             myjsqlview.xml File.
//         2.3 Implemented JMenu siteSelectionMenu actionPerformed() Action,
//             Fill Combobox, TextField, and Checkbox.
//         2.4 Creating of Instance lastSite and Saving Before Main Window
//             Display.
//         2.5 Loading Last Site Parameters and SSH CheckBox Icon.
//         2.6 ConnectionManager Access & Basic Setup of Handling Actions
//             Through Instances connectionManagerSaveExitButton, &
//             connectionManagerCancelButton.
//         2.7 Replacement of Parameters ComboBoxes & Associated Panels with
//             Instances standardParametersPanel & advancedParametersPanel.
//         2.8 Sizing Change of Connection Manager Frame.
//         2.9 Added Class Method setSitesJMenu() to Handle Creating the JMenuBar
//             for the Frame.
//         3.0 Renamed Class Method setSitesJMenu to fillSitesDataStructures().
//         3.1 Removed this, JFrame Argument to standard/advancedParamatersPanels().
//         3.2 Class Method actionPerformed() save/exit Action Retrieval of
//             Modified Sites Hashtable From ConnectionManager Before Disposing.
//         3.3 Resetting JMenuBar After Sites Modification in ConnectionManager
//             Class Method actionPerformed() save/exit Action.
//         3.4 Class Method actionPerformed Commented Out Debug, and Added
//             Update of Sites in XML File, ConnectionManager Save/Exit.
//         3.5 Class Instance ssh For Handling sshCheckBox. Addition of ssh
//             to Connnection Properties.
//         3.6 Layout Change centerPanel Layout to Null. Manually Setting Bounds
//             on Content Panels. Overall Sizing Changes.
//         3.7 Set Look and Feel as System Dependent if Possible.
//         3.8 Added Popup Menu for Cut, Copy, and Paste.
//         3.9 Added Class Method getPopupMenuListener().
//         4.0 Added Dandy Made Productions accessIcon.
//         4.1 ImageIcon Instance accessIcon Based on Time of Day.
//         4.2 Changes to Password Obtaining/Setting.
//         4.3 Minor Comment Changes.
//         4.4 Class Method accessCheck() standardParametersPanel.getPassword()
//             Check for byte Length 0. Code Cleanup.
//         4.5 Removed Class Instance myjsqlview.
//         4.6 Used System.getProperty("file.separator") for All File System
//             Resources Accesses Through Instance fileSeparator.
//         4.7 Removed static Class Instance tables Initialization from Constructor.
//         4.8 Class Instance debug, and Argument Scanning in main().
//         4.9 Modifications in Class Method accessCheck() for PostgreSQL Database
//             Access. Also Bunch of Debuging Output for Testing. Class Method
//             getSubProtocol().
//         5.0 Removed Instantiation of Instance currentSiteParameter in Class
//             Method fillSiteDataStructure().
//         5.1 Cleaned Up Javadoc Comments.
//         5.2 Class Instance identifierQuoteString. Class Method
//             getIdentifierQuoteString. Also Changes in Comments and Database
//             Information Gathering in Class Method accessCheck().
//         5.3 Changed Where the Debug Information is Output in the Class Method
//             accessCheck().
//         5.4 Class Method getSubProtocol() Returned as LowerCase String.
//         5.5 More Database Debug Gathering Information in Class Method
//             accessCheck(). Also Check for PostgreSQL Databases User
//             Access to Tables in Same Class Method.
//         5.6 Header Update.
//         5.7 Removed the Conditional Check for password.length = 0 As
//             Validation for Proceeding to Getting a Connection in Class Method
//             accessCheck().
//         5.8 Condition Check on identifierQuoteString, If NULL Set to "".
//         5.9 Modified the Table Types That May Be Processed to Include Views
//             in Class Method accessCheck().
//         6.0 Testing for HSQL Database Access. Class Method accessCheck()
//             Outputing Data on Connection & Temporary Modifications metaData
//             getTables().
//         6.1 Still Testing, Fix so CVS Code Will Function Properly With the
//             MySQL Database By Commenting Out getTablePriviledges in Class
//             Method accessCheck().
//         6.2 Commented Test Area of Class Method accessCheck(). Finished At
//             This Time for HSQLDB.
//        6.21 Changed Default Behavior for Getting Tables & Removed Check On
//             Empty Database String in Class Method accessCheck().
//        6.22 Class Method accessCheck(), dbMetaData.getTables() tableNamePattern
//             Changed to % For HSQLDB Access.
//        6.23 Added Class Instance dbProductNameVersion & Class Method 
//             getDBProductName_And_Version().
//        6.24 Class Method accessCheck() Added a JOptionPane.showMessageDialog()
//             On Exception of Not Being Able to Load Database JDBC Driver.
//        6.25 Development Test Case for Implementing PostgreSQL & HSQL Schema.
//             Added Class Method setSchema(). Modifications to getConnection()
//             Class Method to SET SCHEMA When Necessary. Changes Also in
//             accessCheck() to Accomodate These Changes.
//        6.26 Added the "Default" to Choice to Schema Selection for PostgreSQL
//             So the User May Use its Default Search Path. Class Method
//             accessCheck() & setSchema().
//        6.27 Undid Changes From Versions 6.25 & 6.26.
//        6.28 Class Method accessCheck(), Vector tableName Modified to Hold
//             Complete Table Name Qualifier Including Schema As Needed.
//        6.29 Modification to Appropriately Handle the Oracle Database.
//             Changes Class Method accessCheck() connectionProperties Creation
//             & Passing Table Types to dbMetaData.getTables(). Exclusion
//             Hash for Oracle System Tables. Class Method getConnection()
//             Change in How DriverManager.getConnection for Oracle.
//        6.30 Removed toString() Method on connectionProperties in Class
//             Method accessCheck() and getConnection().
//        6.31 Added Class Instance serialVersionUID. Declared lastSite,
//             & xmlTranslator transient.
//        6.32 Class Instance driver Moved Locally to Class Method accessCheck().
//             Changed Class Instance loggedIn Removed static Declaration.
//        6.33 Added System.out for Table Information if Want Just Abreviated
//             and Filter. Also Exclusion of Oracle Recycle BIN$XX Tables.
//             All in Class Method accessCheck().
//        6.34 Added Commented System.out for Obtaining Functions From the
//             Database for Development in Class Method accessCheck().
//        6.35 Reviewed, Class Method accessCheck(). Removed the Collection
//             of the Instance identifierQuoteString Twice.
//        6.36 Corrected Spelling in Class Method displaySQLErrors() for
//             Instance optionPaneStringErrors.
//        6.37 Implemented Safe Swing Invocation as Recommended by Sun Tutorials.
//             Moved the Sizing and Showing of the Frame Aspect of the Class
//             to the End of the Constructor.
//        6.38 MyJSQLView Project Common Source Code Formatting.
//        6.39 Additional Standardation to Comments.
//        6.40 Changed Tooltip for advancedOptionsButton. Set Pressed Icon for
//             accessIconItem. File Name Changes for sshUpIcon & sshDownIcon.
//             Added Constructor Instance iconsDirectory.
//        6.41 Removed As the Main Application Entry Point.
//        6.42 Reviewed & Cleaned Up. Moved ImageIcon Creation to Beginning of
//             Constructor. Class Method accessCheck() Cleared Instance tables
//             At Beginning of Table Loading So That Feature Reloading DBTables
//             Can Take Place After Data SQL Imports.
//        6.43 Added Class Method loadDBTables(). Moved Code From accessCheck()
//             That Had to Do With Such to This New Method.
//        6.44 Removed boolean Argument argDebug From Constructor and Class
//             Instance debug. Replace debug With MyJSQLView.getDebug().
//        6.45 Added Class Instance schemas. Class Methods setSchemasPattern(),
//             getSchemas(), and loadDBParameters().
//        6.46 Class Method loadDBParameters() Implemented The Reading of the 
//             Configuration File, myjsqlview.conf, For Overriding Table Loading
//             Parameters.
//        6.47 Set The Frame Invisible on a Login Attempt and Then Showed Again
//             If Login Attempt Fails. Added Comments in Method accessCheck().
//        6.48 Implemented a Splash Screen to Reflect Startup Status. Added Class
//             Instances splashWindow, splashPanel, and Class Method createSplashWindow().
//        6.49 Class Method actionPerformed() Changed Inner Thread Class Sleep
//             Block Catch to InterruptedException.
//        6.50 Class Name Change SiteParameter to SiteParameters.
//        6.51 Added Class Instance errorSoundClip and Its Functionality.
//        6.52 Modified passwordString Replacement of % Chararcter With Its Appropriate
//             Hexadecimal Sequence For Proper URL Decoding. Class Method accessCheck().
//        6.53 Moved Instance fileSeparator From Constructor to Class Instance.
//             Added Method Instance javaExtDir to accessChech() and Provided this
//             additonal Information to JDBC Driver Loading Error String.
//        6.54 Class Method createSplashWindow() Changed splashWindow to setLayout
//             and add Through contentPane() Instead of Directly.
//        6.55 Class Instance fileSeparator and Constuctor Instance iconsDiroctory
//             Obtained From MyJSQlView_Utils Class. Removed Method Instance
//             myjsqlviewHomeDirectory From loadDBParameters().
//        6.56 Added fileSeparator to iconsDirectory.
//        6.57 Check In Constructor for Valid Processing of myjsqlview.xml file.
//             If not New Class Method fillSitesDefaults(). Disable
//             connectionManangerAccessButton on Same.
//        6.58 Class Method fillSitesDefaults(), Corrections in defaultDrivers Set.
//             Change in defaultDatabases to Just in Indicating Database Name.
//        6.59 Changed Package to Reflect Dandy Made Productions Code.
//        6.60 Added Class Instance resourceBundle & resouce. Internationalization
//             Code.
//        6.61 Resource Loading for connectionManagerSaveExit/CancelButtons. Class
//             Method ConnectionManager Argument resourceBundle.
//        6.62 SplashPanel Constructor Creation, resourceBundle Addition in
//             createSplashWindow().
//        6.63 Moved cancelButton in Constructor to Proceed loginButton.
//        6.64 Conditional Checks in Methods actionPerformed() & fillSiteDataStructures()
//             From Non-Short-Circut to Short-Circuit &&.
//        6.65 Reviewed All Methods and Reassigned as Needed to private or public
//             to Properly Protect and Allow Access for Plugins. Made Class Itself
//             public.
//        6.66 Parameterized Class Instances sitesNamesList, driverList, protocolList,
//             subProtocolList, hostList, portList, databaseList, userList, sites,
//             schemas, and tables in Order to Bring Code Into Compliance With Java
//             5.0 API. Same for Instances oracleSystemSchemaHash and sitesTreeSet
//             in Class Methods loadDBTables() and fillSiteDataStructures(). Also
//             Parameterized Return Types for Class Methods getSchemas() &
//             getTableNames().
//        6.67 Parameterized siteNames and sitesTreeIterator in Class Method
//             fillSiteDataStructure(). Also tablesIterator in loadDBTables().
//        6.68 Class Method getSchemas() & getTableNames() Returned a Copy of the
//             Vector schemas & tables.
//        6.69 Modification to Support SQLite Database. Changes to Include Defaults
//             for That Database in fillSitesDefaults() and Changes to accessCheck().
//        6.70 Changed replaceAll() With Replace for SQLite connectionProperties
//             String for db in accessCheck().
//        6.71 Class Method accessCheck() identifierQuoteString Conditional Check for
//             NULL and SPACE Returns Empty String.
//        6.72 Minor Format Change.
//        6.73 Class Method loadDBParameters() Output if I/O Error & Debug On.
//        6.74 Added Class Instance memoryConnection to Implement Support for In
//             Memory Databases of HSQL & SQLite. Changes in accessCheck(), getConnection(),
//             closeConnection().
//        6.75 Renamed From MyJSQLView_Access to LoginFrame. Moved All Aspects of
//             Connnections to New Class ConnectionManager, Except the Initial accessCheck().
//             The Method accessCheck() Saves the Parameters and Properly Directs the
//             Type of Processing for the Data Source Desired.
//        6.76 Increased Bounds Width for the Standard & AdvancedParametersPanels.
//             Increased the Size of Frame Setting in actionPerformed().
//        6.77 Added StringBuffer tempBuffer in Class Method accessCheck() to Build
//             the passwordString.
//        6.78 All References to Instances/Names of ConnectionManagerFrame Changed to
//             LoginManagerFrame, LoginManager.
//        6.79 Class Method accessCheck() Correction for HSQL Memory Detection of subProtocol
//             for HSQL Not SQLite. System.out.println()s to connectionString.
//        6.80 Class Method fillSiteDefaults() Added MS Access to Default String Arrays.
//             Class Method accessCheck() Addition of connectionString & dbProductNameVersion
//             Entries for MS Access.
//        6.81 Class Method accessCheck() Added Instance catalogSeparator and Collected
//             in Same Along With Setting in ConnectionManager. Additional Comments.
//        6.82 Minor Comment Changes. Update to Copyright. Change to Method accessCheck()
//             ConnectionManager.setMemoryConnection() Routine That Was Renamed.
//             
//-----------------------------------------------------------------
//                  danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.Vector;
import javax.swing.*;

/**
 *    This LoginFrame class provides the framework for a login access
 * dialog to a database. The class provides the main frame used to allow
 * the user entry to the MyJSQLView application. Several input preferences
 * must be specified in this login in order to establish a valid connection
 * to a database. 
 * 
 * @author Dana M. Proctor
 * @version 6.82 01/01/2012
 */

public class LoginFrame extends JFrame implements ActionListener
{
   // Class Instances
   private static final long serialVersionUID = 9021939696353674626L;

   private LoginManagerFrame loginManagerFrame;
   private MyJSQLView_ResourceBundle resourceBundle;
   private JWindow splashWindow;
   private SplashPanel splashPanel;
   
   private String resource, fileSeparator;
   
   private JMenuBar accessDialogMenuBar;
   private JButton loginManagerFrame_AccessButton;
   private JButton advancedOptionsButton;

   private StandardParametersPanel standardParametersPanel;
   private AdvancedParametersPanel advancedParametersPanel;

   private JCheckBox sshCheckBox;

   private JButton validLoginButton, loginButton, cancelButton;

   private Vector<String> sitesNameList, driverList, protocolList, subProtocolList,
                  hostList, portList, databaseList, userList;
   
   private Hashtable<String, SiteParameters> sites;
   private transient SiteParameters lastSite;
   private transient XMLTranslator xmlTranslator;
   
   private JButton loginManagerFrame_SaveExitButton, loginManagerFrame_CancelButton;

   private boolean loggedIn = false;
   private boolean advancedOptionsShowing = false;

   //==============================================================
   // LoginFrame Constructor
   //==============================================================

   protected LoginFrame(JButton validLoginButton)
   {
      this.validLoginButton = validLoginButton;
      
      // Constructor Instances.
      String iconsDirectory;
      ImageIcon loginManagerIcon, advancedConnectionsIcon;
      ImageIcon accessIcon, sshUpIcon, sshDownIcon;
      ImageIcon logoPanelIcon;
      JMenu siteSelectMenu;
      JPanel mainPanel, centerPanel, actionPanel;
      JPanel myjsqlviewLogoPanel;
      
      resourceBundle = MyJSQLView.getLocaleResourceBundle();
      
      // Set Frame Parameters
      
      resource = resourceBundle.getResource("LoginFrame.message.Title");
      if (resource.equals(""))
         setTitle("MyJSQLView Login");
      else
         setTitle("MyJSQLView " + resource);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      // Setting up Various Instances.
      
      xmlTranslator = new XMLTranslator();
      sites = new Hashtable <String, SiteParameters>();
      sitesNameList = new Vector <String>();
      driverList = new Vector <String>();
      protocolList = new Vector <String>();
      subProtocolList = new Vector <String>();
      hostList = new Vector <String>();
      portList = new Vector <String>();
      databaseList = new Vector <String>();
      userList = new Vector <String>();

      fileSeparator = MyJSQLView_Utils.getFileSeparator();
      iconsDirectory = MyJSQLView_Utils.getIconsDirectory() + fileSeparator;
      
      // Obtain & create Image Icons.
      
      loginManagerIcon = new ImageIcon(iconsDirectory + "connectionManagerIcon.png");
      advancedConnectionsIcon = new ImageIcon(iconsDirectory + "advancedConnectionsIcon.png");
      
      SimpleDateFormat format = new SimpleDateFormat("k");
      int dateHour = Integer.parseInt(format.format(new Date()));
      
      if (dateHour >= 5 & dateHour <= 10)
         accessIcon = new ImageIcon(iconsDirectory + "loginFrameIconA.gif");
      else if (dateHour >= 11 & dateHour <= 16)
         accessIcon = new ImageIcon(iconsDirectory + "loginFrameIconB.gif");
      else if (dateHour >= 17 & dateHour <= 21)
         accessIcon = new ImageIcon(iconsDirectory + "loginFrameIconC.gif");
      else
         accessIcon = new ImageIcon(iconsDirectory + "loginFrameIconD.gif");
      
      logoPanelIcon = new ImageIcon("images" + fileSeparator + "MyJSQLView_Logo.png");
      sshUpIcon = new ImageIcon(iconsDirectory + "sshUpIcon.png");
      sshDownIcon = new ImageIcon(iconsDirectory + "sshDownIcon.png");
      
      // Setting up the MenuBar for the access of the
      // LoginFrame, connection selection & advanced options.

      accessDialogMenuBar = new JMenuBar();
      accessDialogMenuBar.setBorder(BorderFactory.createEtchedBorder());
      accessDialogMenuBar.setMargin(new Insets(0, 0, 0, 0));
      
      if (xmlTranslator.getXMLTranslatorResult())
      {
         // Site Connections
         sites = xmlTranslator.getSites();
         lastSite = xmlTranslator.getLastSite();
         sites.put(lastSite.getSiteName(), lastSite);
      }
      else
         fillSitesDefaults();
     
      // Setup Sites Slection JMenu.
      resource = resourceBundle.getResource("LoginFrame.menu.Sites");
      if (resource.equals(""))
         siteSelectMenu = new JMenu("Sites");
      else
         siteSelectMenu = new JMenu(resource);
      resource = resourceBundle.getResource("LoginFrame.tooltip.SiteSelection");
      if (resource.equals(""))
         siteSelectMenu.setToolTipText("Site Selection");
      else
         siteSelectMenu.setToolTipText(resource);
      fillSiteDataStructures(siteSelectMenu);
      accessDialogMenuBar.add(siteSelectMenu);

      // Login Manager Frame Components.
      loginManagerFrame_AccessButton = new JButton(loginManagerIcon);
      loginManagerFrame_AccessButton.setFocusable(false);
      loginManagerFrame_AccessButton.setMargin(new Insets(0, 0, 0, 0));
      resource = resourceBundle.getResource("LoginFrame.tooltip.LoginManager");
      if (resource.equals(""))
         loginManagerFrame_AccessButton.setToolTipText("Login Manager");
      else
         loginManagerFrame_AccessButton.setToolTipText(resource);
      
      if (xmlTranslator.getXMLTranslatorResult())
         loginManagerFrame_AccessButton.addActionListener(this);
      else
         loginManagerFrame_AccessButton.setEnabled(false);
      
      accessDialogMenuBar.add(loginManagerFrame_AccessButton);
         
      // Advanced Options Selection
      advancedOptionsButton = new JButton(advancedConnectionsIcon);
      advancedOptionsButton.setFocusable(false);
      advancedOptionsButton.setMargin(new Insets(0, 0, 0, 0));
      resource = resourceBundle.getResource("LoginFrame.tooltip.AdvancedOptions");
      if (resource.equals(""))
         advancedOptionsButton.setToolTipText("Advanced Options");
      else
         advancedOptionsButton.setToolTipText(resource);
      advancedOptionsButton.addActionListener(this);
      accessDialogMenuBar.add(advancedOptionsButton);

      accessDialogMenuBar.add(Box.createHorizontalGlue());

      // Access dmp Icon
      JButton accessIconItem = new JButton(accessIcon);
      accessIconItem.setPressedIcon(accessIcon);
      accessIconItem.setDisabledIcon(accessIcon);
      accessIconItem.setFocusPainted(false);
      accessIconItem.setMargin(new Insets(1, 0, 0, 0));
      accessIconItem.setBorder(BorderFactory.createEtchedBorder());
      accessDialogMenuBar.add(accessIconItem);

      setJMenuBar(accessDialogMenuBar);

      // ================================================
      // Setting up the main panel that will be needed
      // in the frame. The main panel borderlayout will
      // have most compenents in the center with actions
      // buttons in the south.
      
      mainPanel = new JPanel(new BorderLayout());
      mainPanel.setBorder(BorderFactory.createEtchedBorder());

      centerPanel = new JPanel();
      centerPanel.setLayout(null);

      // MyJSQLView Logo & Components
      myjsqlviewLogoPanel = new JPanel();
      myjsqlviewLogoPanel.add(new JLabel(logoPanelIcon, JLabel.LEFT));
      myjsqlviewLogoPanel.setBounds(0, 0, 120, 210);
      centerPanel.add(myjsqlviewLogoPanel);

      // Standard Parameters Panel & Components
      standardParametersPanel = new StandardParametersPanel(resourceBundle, hostList, databaseList,
                                                            userList);
      standardParametersPanel.setBounds(125, 0, 210, 200);
      centerPanel.add(standardParametersPanel);

      advancedParametersPanel = new AdvancedParametersPanel(resourceBundle, driverList, protocolList,
                                                            subProtocolList, portList);
      advancedParametersPanel.setBounds(345, 0, 210, 200);
      centerPanel.add(advancedParametersPanel);

      mainPanel.add(centerPanel, BorderLayout.CENTER);

      // Action Button Panel
      actionPanel = new JPanel();
      actionPanel.setBorder(BorderFactory.createRaisedBevelBorder());

      resource = resourceBundle.getResource("LoginFrame.checkbox.SSH");
      if (resource.equals(""))
         sshCheckBox = new JCheckBox("SSH", sshUpIcon);
      else
         sshCheckBox = new JCheckBox(resource, sshUpIcon);
      sshCheckBox.setSelectedIcon(sshDownIcon);
      sshCheckBox.setHorizontalTextPosition(SwingConstants.LEADING);
      sshCheckBox.setFocusPainted(false);
      actionPanel.add(sshCheckBox);
      
      resource = resourceBundle.getResource("LoginFrame.button.cancel");
      if (resource.equals(""))
         cancelButton = new JButton("cancel");
      else
         cancelButton = new JButton(resource);
      cancelButton.addActionListener(this);
      actionPanel.add(cancelButton);
      
      resource = resourceBundle.getResource("LoginFrame.button.login");
      if (resource.equals(""))
         loginButton = new JButton("login");
      else
         loginButton = new JButton(resource);
      loginButton.addActionListener(this);
      actionPanel.add(loginButton);
      
      // Setting the component, connection parameters,
      // to last site, and then adding everything to the
      // frame.
      
      setSelectedSite(lastSite);
      mainPanel.add(actionPanel, BorderLayout.SOUTH);
      getContentPane().add(mainPanel);
      (this.getRootPane()).setDefaultButton(loginButton);

      // Creating the LoginManager Action Buttons
      resource = resourceBundle.getResource("LoginFrame.button.saveandexit");
      if (resource.equals(""))
         loginManagerFrame_SaveExitButton = new JButton("save and exit");
      else
         loginManagerFrame_SaveExitButton = new JButton(resource);
      loginManagerFrame_SaveExitButton.addActionListener(this);
      resource = resourceBundle.getResource("LoginFrame.button.cancel");
      if (resource.equals(""))
         loginManagerFrame_CancelButton = new JButton("cancel");
      else
         loginManagerFrame_CancelButton = new JButton(resource);
      loginManagerFrame_CancelButton.addActionListener(this);
   }

   //==============================================================
   // ActionEvent Listener method for detecting the user's selection
   // of the frame's various JButtons and JMenu then directing to
   // the appropriate action..
   //==============================================================

   public void actionPerformed(ActionEvent evt)
   {
      Object panelSource = evt.getSource();

      // Button Actions
      if (panelSource instanceof JButton)
      {
         JButton actionButton = (JButton) panelSource;

         // LoginManager Option.
         if (actionButton == loginManagerFrame_AccessButton)
         {
            // Creation of the LoginManager Main Frame
            // as required.
            if (loginManagerFrame == null)
            {
               StandardParametersPanel standardPanelClone = new StandardParametersPanel(resourceBundle,
                                                                                        hostList,
                                                                                        databaseList,
                                                                                        userList);
               AdvancedParametersPanel advancedPanelClone = new AdvancedParametersPanel(resourceBundle,
                                                                                        driverList,
                                                                                        protocolList,
                                                                                        subProtocolList,
                                                                                        portList);

               loginManagerFrame = new LoginManagerFrame(resourceBundle, sites,
                                                         standardPanelClone, advancedPanelClone,
                                                         loginManagerFrame_SaveExitButton,
                                                         loginManagerFrame_CancelButton);
               loginManagerFrame.setSize(505, 345);
               loginManagerFrame.setResizable(false);
               loginManagerFrame.center(-50, 50);
               loginManagerFrame.setVisible(true);
            }
            else
            {
               loginManagerFrame.setVisible(false);
               loginManagerFrame.dispose();
               loginManagerFrame = null;
            }
         }

         // Advanced Options
         else if (actionButton == advancedOptionsButton)
         {
            if (advancedOptionsShowing)
            {
               setSize(355, 320);
               advancedOptionsShowing = false;
            }
            else
            {
               setSize(575, 320);
               advancedOptionsShowing = true;
            }
         }

         // Login Attempt
         else if (actionButton == loginButton)
         {
            setVisible(false);
            
            if (accessCheck())
            {     
               // Getting and saving the current selected
               // site parameters before heading off to
               // main window. Check for problems first.
               
               if (xmlTranslator.getXMLTranslatorResult())
               {
                  lastSite.setSiteName("Last Site");
                  lastSite.setDriver(advancedParametersPanel.getDriver());
                  lastSite.setProtocol(advancedParametersPanel.getProtocol());
                  lastSite.setSubProtocol(advancedParametersPanel.getSubProtocol());
                  lastSite.setHost(standardParametersPanel.getHost());
                  lastSite.setPort(advancedParametersPanel.getPort());
                  lastSite.setDatabase(standardParametersPanel.getDataBase());
                  lastSite.setUser(standardParametersPanel.getUser());
                  lastSite.setPassword(" ".toCharArray());
                  if (sshCheckBox.isSelected())
                     lastSite.setSsh("1");
                  else
                     lastSite.setSsh("0");
                  xmlTranslator.setLastSite(lastSite);
               }

               // Making sure the LoginManager gets closed down.
               if (loginManagerFrame != null)
               {
                  loginManagerFrame.setVisible(false);
                  loginManagerFrame.dispose();
                  loginManagerFrame = null;
               }
               
               // Close Down the Splash Window.
               Thread splashDelayThread = new Thread(new Runnable()
               {
                  public void run()
                  {
                     try
                     {
                        Thread.sleep(1000);
                        splashPanel.suspendPanel(true);
                        splashWindow.dispose();
                     }
                     catch (InterruptedException e) {}
                  }
               }, "LoginFrame.splashDelayThread");
               splashDelayThread.start();
              
               validLoginButton.doClick();
            }
            else
            {
               standardParametersPanel.setPassword(" ".toCharArray());
               setVisible(true);
            }
         }

         // Cancel Action
         else if (actionButton == cancelButton)
         {
            dispose();
         }

         // LoginManager Save/Exit Action
         else if (actionButton == loginManagerFrame_SaveExitButton)
         {
            // Collect the possibly modified sites.
            sites = loginManagerFrame.getSites();

            // Temp debug.
            /*
             * Enumeration sitesKeys = sites.keys(); while
             * (sitesKeys.hasMoreElements()) { String currentKey = new
             * String((String)sitesKeys.nextElement());
             * System.out.println(currentKey); }
             */

            // Closing out LoginManagerFrame and taking
            // action to update JMenu.
            loginManagerFrame.setVisible(false);
            loginManagerFrame.dispose();
            loginManagerFrame = null;

            // Updating JMenuBar and ComboBoxes.
            JMenu siteSelectMenu;
            resource = resourceBundle.getResource("LoginFrame.menu.Sites");
            if (resource.equals(""))
               siteSelectMenu = new JMenu("Sites");
            else
               siteSelectMenu = new JMenu(resource);
            resource = resourceBundle.getResource("LoginFrame.tooltip.SiteSelection");
            if (resource.equals(""))
               siteSelectMenu.setToolTipText("Site Selection");
            else
               siteSelectMenu.setToolTipText(resource);
            fillSiteDataStructures(siteSelectMenu);
            accessDialogMenuBar.remove(0);
            accessDialogMenuBar.add(siteSelectMenu, 0);
            setJMenuBar(accessDialogMenuBar);

            // Update the Local XML File.
            xmlTranslator.setSites(sites);
         }

         // LoginManagerFrame Cancel Action
         else if (actionButton == loginManagerFrame_CancelButton)
         {
            // Do take any action other than closing out
            // LoginManagerFrame.
            loginManagerFrame.setVisible(false);
            loginManagerFrame.dispose();
            loginManagerFrame = null;
         }
      }
      // JMenu Actions
      else if (panelSource instanceof JMenuItem)
      {
         JMenuItem selectedMenuItem = (JMenuItem) panelSource;
         String actionCommandName = selectedMenuItem.getActionCommand();
         // System.out.println(actionCommandName);

         if (!actionCommandName.equals("Cut") && !actionCommandName.equals("Copy")
             && !actionCommandName.equals("Paste"))
         {
            SiteParameters selectedSite = (SiteParameters) sites.get(actionCommandName);
            setSelectedSite(selectedSite);
         }
      }
   }

   //==============================================================
   // Class method used for the filling the data structures
   // that hold the site menu items in the menu bar and list
   // parameters in the combobox fields.
   //==============================================================
   
   private void fillSiteDataStructures(JMenu siteSelectMenu)
   {
      // Class Method Instances
      Enumeration<String> siteNames;
      Iterator<String> sitesTreeIterator;

      TreeSet<String> sitesTreeSet;
      Hashtable<String, JMenu> sitesJMenus;
      String siteName;

      // Remove all previous sites.
      siteSelectMenu.removeAll();

      // Create a natural order of JMenus
      // of the given sites' names.

      siteNames = sites.keys();
      sitesTreeSet = new TreeSet <String>();

      while (siteNames.hasMoreElements())
      {
         siteName = siteNames.nextElement();

         if (!siteName.equals("Last Site") && siteName.indexOf('#') != -1)
            siteName = siteName.substring(0, siteName.indexOf('#'));
         else
            siteSelectMenu.add(createMenuItem(siteName, siteName));

         if (!sitesTreeSet.contains(siteName) && !siteName.equals("Last Site"))
            sitesTreeSet.add(siteName);
      }

      sitesTreeIterator = sitesTreeSet.iterator();
      sitesJMenus = new Hashtable <String, JMenu>();

      while (sitesTreeIterator.hasNext())
      {
         String currentSiteName = (String) sitesTreeIterator.next();
         JMenu currentSiteJMenu = new JMenu(currentSiteName);
         sitesJMenus.put(currentSiteName, currentSiteJMenu);
         siteSelectMenu.add(currentSiteJMenu);
      }

      // Now that the data has been organized
      // then clear any contents from the vectors
      // used for the comboboxes and refill also
      // in the process add databases to the site
      // JMenus.

      driverList.removeAllElements();
      protocolList.removeAllElements();
      subProtocolList.removeAllElements();
      hostList.removeAllElements();
      portList.removeAllElements();
      databaseList.removeAllElements();
      userList.removeAllElements();

      siteNames = sites.keys();
      while (siteNames.hasMoreElements())
      {
         String jmenuSiteName;
         String jmenuDBName;

         // Fill the JMenu first then create comboboxes
         // elements.

         siteName = (String) siteNames.nextElement();
         sitesNameList.add(siteName);

         if (!siteName.equals("Last Site") && siteName.indexOf('#') != -1)
         {
            jmenuSiteName = siteName.substring(0, siteName.indexOf('#'));
            jmenuDBName = siteName.substring(siteName.indexOf('#') + 1);

            JMenu currentSiteJMenu = sitesJMenus.get(jmenuSiteName);
            currentSiteJMenu.add(createMenuItem(jmenuDBName, siteName));
         }

         SiteParameters currentSiteParameter = (SiteParameters) sites.get(siteName);

         if (!driverList.contains(currentSiteParameter.getDriver()))
            driverList.add(currentSiteParameter.getDriver());
         if (!protocolList.contains(currentSiteParameter.getProtocol()))
            protocolList.add(currentSiteParameter.getProtocol());
         if (!subProtocolList.contains(currentSiteParameter.getSubProtocol()))
            subProtocolList.add(currentSiteParameter.getSubProtocol());
         if (!hostList.contains(currentSiteParameter.getHost()))
            hostList.add(currentSiteParameter.getHost());
         if (!portList.contains(currentSiteParameter.getPort()))
            portList.add(currentSiteParameter.getPort());
         if (!databaseList.contains(currentSiteParameter.getDatabase()))
            databaseList.add(currentSiteParameter.getDatabase());
         if (!userList.contains(currentSiteParameter.getUser()))
            userList.add(currentSiteParameter.getUser());
      }
   }
   
   //==============================================================
   // Class method used for the filling the data structures that
   // hold the site menu items in the menu bar and list parameters
   // in the combobox fields with default values. Something went
   // wrong with processing the the myjsqlview.xml file.
   //==============================================================

   private void fillSitesDefaults()
   {
      // Class Method Instances
      SiteParameters currentSiteParameter;
      String siteName;
      
      // Example defaults database settings.
      String[] defaultDrivers = {"com.mysql.jdbc.Driver", "org.postgresql.Driver", "org.hsqldb.jdbcDriver",
                                 "oracle.jdbc.driver.OracleDriver", "org.sqlite.JDBC",
                                 "sun.jdbc.odbc.JdbcOdbcDriver"};
      String[] defaultSubProtocols = {"mysql", "postgresql", "hsqldb:hsql", "oracle:thin", "sqlite", "odbc"};
      
      String[] defaultPorts = {"3306", "5432", "9001", "1521", "0000", "0000"};
      String[] defaultDatabases = {"mysql", "postgresql", "hsql;", "oracle", "test/sqlite.db", "ms_access"};
      
      // Clear contents to start anewed.
      driverList.removeAllElements();
      protocolList.removeAllElements();
      subProtocolList.removeAllElements();
      hostList.removeAllElements();
      portList.removeAllElements();
      databaseList.removeAllElements();
      userList.removeAllElements();
                                   
      // Create the SiteParmeters with the defaults for
      // each site name entry. Then fill the login frame
      // components with the defaults.
      
      for (int i=0; i < defaultDatabases.length; i++)
      {
         // SiteParameters
         siteName = "LocalHost#" + defaultDatabases[i];
         
         currentSiteParameter = new SiteParameters();
         
         currentSiteParameter.setSiteName("LocalHost");
         currentSiteParameter.setDriver(defaultDrivers[i]);
         currentSiteParameter.setProtocol("jdbc");
         currentSiteParameter.setSubProtocol(defaultSubProtocols[i]);
         currentSiteParameter.setHost("127.0.0.1");
         currentSiteParameter.setPort(defaultPorts[i]);
         currentSiteParameter.setDatabase(defaultDatabases[i]);
         currentSiteParameter.setUser("");
         currentSiteParameter.setPassword("".toCharArray());
         currentSiteParameter.setSsh("0");
        
         sites.put(siteName, currentSiteParameter);
         
         // Fill Login Access Components
         sitesNameList.add(siteName);
         
         if (!driverList.contains(currentSiteParameter.getDriver()))
            driverList.add(currentSiteParameter.getDriver());
         if (!protocolList.contains(currentSiteParameter.getProtocol()))
            protocolList.add(currentSiteParameter.getProtocol());
         if (!subProtocolList.contains(currentSiteParameter.getSubProtocol()))
            subProtocolList.add(currentSiteParameter.getSubProtocol());
         if (!hostList.contains(currentSiteParameter.getHost()))
            hostList.add(currentSiteParameter.getHost());
         if (!portList.contains(currentSiteParameter.getPort()))
            portList.add(currentSiteParameter.getPort());
         if (!databaseList.contains(currentSiteParameter.getDatabase()))
            databaseList.add(currentSiteParameter.getDatabase());
         if (!userList.contains(currentSiteParameter.getUser()))
            userList.add(currentSiteParameter.getUser());
      } 
   }  
   
   //==============================================================
   // Class method used for the helping of creating menu items in
   // the menu bar items. Helper Method.
   //==============================================================

   private JMenuItem createMenuItem(String label, String actionLabel)
   {
      JMenuItem item = new JMenuItem(label);
      item.addActionListener(this);
      item.setActionCommand(actionLabel);
      return item;
   }

   //==============================================================
   // Class method used for setting values in the components of
   // the login interface, aka. connection parameters.
   //==============================================================

   private void setSelectedSite(SiteParameters selectedSite)
   {
      if (selectedSite == null)
         return;
      
      // Login component setting.
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

   //==============================================================
   // Class method to provide verification of a valid input for
   // access to the data source.
   //==============================================================

   private boolean accessCheck()
   {
      Connection dbConnection;
      
      String connectionString;
      String driver, protocol, subProtocol, host, port, db, user, passwordString, ssh;
      char[] passwordCharacters;
      String dbProductNameVersion;
      String catalogSeparator;
      String identifierQuoteString;
      
      DatabaseMetaData dbMetaData;
      // ResultSet db_resultSet;

      // Try to login in the user with the specified connection

      // Check for some kind of valid input.
      if (advancedParametersPanel.getDriver().equals("") ||
          advancedParametersPanel.getProtocol().equals("") ||
          advancedParametersPanel.getSubProtocol().equals("") ||
          standardParametersPanel.getHost().equals("") ||
          advancedParametersPanel.getPort().equals("") ||
          standardParametersPanel.getUser().equals(""))
      {
         loggedIn = false;
      }

      // All entries there so try to make a connecion to the
      // database.
      else
      {
         // =================================================
         // Checking to see if the jdbc driver is available
         // =================================================

         try
         {
            driver = advancedParametersPanel.getDriver();
            // System.out.println("driver: " + driver);
            
            // Run SQLite in pure Java mode to maintain compatibility,
            // slower, but works with older versions of JVM. Revisit
            // Later if really needed.
            
            if (advancedParametersPanel.getSubProtocol().indexOf("sqlite") != -1)
               System.setProperty("sqlite.purejava", "true");
               
            Class.forName(driver);
            // System.out.println("Driver Loaded");
         }
         catch (Exception e)
         {
            // Alert Dialog Output.
            String exceptionString = e.getMessage();
            if (exceptionString != null && exceptionString.length() > 200)
               exceptionString = exceptionString.substring(0, 200);
            
            String javaExtDir = System.getProperty("java.ext.dirs");
            if (javaExtDir == null || javaExtDir.equals(""))
               javaExtDir = "Java JRE/lib/ext"; 

            String optionPaneStringErrors = "Unable to Find or Load JDBC Driver" + "\n"
                                            + "Insure the Appropriate JDBC Driver is "
                                            + "Located in the " + "\n"
                                            + javaExtDir + fileSeparator + " directory."
                                            + "\n"
                                            + "Exeception: " + exceptionString;
            JOptionPane.showMessageDialog(null, optionPaneStringErrors, "Alert", JOptionPane.ERROR_MESSAGE);

            loggedIn = false;
            return false;
         }

         // ================================================
         // Obtaining the connection parameters & password.
         // ================================================

         protocol = advancedParametersPanel.getProtocol();
         subProtocol = advancedParametersPanel.getSubProtocol().toLowerCase();
         host = standardParametersPanel.getHost();
         port = advancedParametersPanel.getPort();
         db = standardParametersPanel.getDataBase();
         user = standardParametersPanel.getUser();
         if (sshCheckBox.isSelected())
            ssh = "true";
         else
            ssh = "false";

         passwordString = "";
         passwordCharacters = standardParametersPanel.getPassword();

         // Obtaining the password & clearing.
         
         StringBuffer tempBuffer = new StringBuffer();
         for (int i = 0; i < passwordCharacters.length; i++)
         {
            tempBuffer.append(passwordCharacters[i]);
            passwordCharacters[i] = '0';
         }
         passwordString = tempBuffer.toString();

         // ===============================================
         // Connection Attempt.
         // ===============================================

         try
         {
            // Take into consideration various database requirements.
            connectionString = protocol + ":";

            // Oracle
            if (subProtocol.indexOf(ConnectionManager.ORACLE) != -1)
            {
               if (subProtocol.indexOf("thin") != -1)
                  connectionString += subProtocol + ":@//" + host + ":" + port + "/" + db;
               else
                  connectionString += subProtocol + ":@" + db;

               // System.out.println(connectionString);
               dbConnection = DriverManager.getConnection(connectionString, user, passwordString);
            }
            // SQLite
            else if (subProtocol.equals(ConnectionManager.SQLITE))
            {
               connectionString += subProtocol + ":" + db.replace("\\", "/");
               // System.out.println(connectionString);
               dbConnection = DriverManager.getConnection(connectionString);
            }
            // HSQL Memory
            else if (subProtocol.indexOf(ConnectionManager.HSQL) != -1 && db.indexOf("mem:") != -1)
            {
               passwordString = passwordString.replaceAll("%", "%" + Integer.toHexString(37));
               connectionString += "hsqldb:" + db + "?user=" + user
                                       + "&password=" + passwordString + "&useSSL=" + ssh;
               // System.out.println(connectionString);
               dbConnection = DriverManager.getConnection(connectionString);
            }
            // MS Access
            else if (subProtocol.equals(ConnectionManager.MSACCESS))
            {
               connectionString += subProtocol + ":" + db;
               // System.out.println(connectionString);
               dbConnection = DriverManager.getConnection(connectionString, user, passwordString);
            }
            // MySQL, PostgreSQL, & HSQL
            else
            {
               // The % character is interpreted as the start of a special escaped sequence,
               // two digit hexadeciaml value. So replace passwordString characters with that
               // character with that characters hexadecimal value as sequence, %37. Java
               // API URLDecoder.
               
               passwordString = passwordString.replaceAll("%", "%" + Integer.toHexString(37));
               connectionString += subProtocol + "://" + host + ":" + port + "/" + db + "?user=" + user
                                       + "&password=" + passwordString + "&useSSL=" + ssh;
               // System.out.println(connectionString);
               dbConnection = DriverManager.getConnection(connectionString);
            }
            
            // The Connection is valid if it does not throw a SQL Exception.
            // So save the connection properties and collect the associated
            // database tables and other pertinent information necessary to
            // bring up the application.
            
            createSplashWindow();
            
            ConnectionProperties connectionProperties = new ConnectionProperties();
            
            connectionProperties.setConnectionString(connectionString);
            connectionProperties.setProperty(ConnectionProperties.DRIVER, driver);
            connectionProperties.setProperty(ConnectionProperties.PROTOCOL, protocol);
            connectionProperties.setProperty(ConnectionProperties.SUBPROTOCOL, subProtocol);
            connectionProperties.setProperty(ConnectionProperties.HOST, host);
            connectionProperties.setProperty(ConnectionProperties.PORT, port);
            connectionProperties.setProperty(ConnectionProperties.DB, db);
            connectionProperties.setProperty(ConnectionProperties.USER, user);
            connectionProperties.setProperty(ConnectionProperties.PASSWORD, passwordString);
            connectionProperties.setProperty(ConnectionProperties.SSH, ssh);
            
            ConnectionManager.setConnectionProperties(connectionProperties);
            
            dbMetaData = dbConnection.getMetaData();

            // =======================
            // Database Product Name & Version
            
            if (dbMetaData.getDatabaseProductName() != null)
               dbProductNameVersion = dbMetaData.getDatabaseProductName() + " ";
            else
            {
               if (subProtocol.equals(ConnectionManager.MYSQL))
                  dbProductNameVersion = "MySQL ";
               else if (subProtocol.equals(ConnectionManager.POSTGRESQL))
                  dbProductNameVersion = "PostgreSQL ";
               else if (subProtocol.indexOf(ConnectionManager.HSQL) != -1)
                  dbProductNameVersion = "HSQL ";
               else if (subProtocol.indexOf(ConnectionManager.ORACLE) != -1)
                  dbProductNameVersion = "Oracle ";
               else if (subProtocol.equals(ConnectionManager.SQLITE))
                  dbProductNameVersion = "SQLite ";
               else if (subProtocol.equals(ConnectionManager.MSACCESS))
                  dbProductNameVersion = "MS Access ";
               else
                  dbProductNameVersion = "Unknown Data Source ";
            }
            if (dbMetaData.getDatabaseProductVersion() != null)
               dbProductNameVersion += dbMetaData.getDatabaseProductVersion();
            
            ConnectionManager.setDBProductName_And_Version(dbProductNameVersion);
            
            // Lots of debug info for gathering database information during
            // testing. Some of these will fail, throw exceptions/null pointers
            // for some databases.

            // =======================
            // SQL Key Words
            
            // System.out.println(dbMetaData.getSQLKeywords());
            
            // =======================
            // Database Functions
            /*
            System.out.println(dbMetaData.getNumericFunctions());
            System.out.println(dbMetaData.getStringFunctions());
            System.out.println(dbMetaData.getSystemFunctions());
            System.out.println(dbMetaData.getTimeDateFunctions());
            */

            // =======================
            // Catalogs
            /*
            db_resultSet = dbMetaData.getCatalogs();
            while (db_resultSet.next())
               System.out.println("Catalogs: " + db_resultSet.getString("TABLE_CAT"));
            */

            // =======================
            // Schema
            /*
            db_resultSet = dbMetaData.getSchemas();
            while (db_resultSet.next())
               System.out.println("Table Scheme: " + db_resultSet.getString(1));
            */
            
            // =======================
            // Catalog Separator
            
            catalogSeparator = dbMetaData.getCatalogSeparator();
            if (catalogSeparator == null || catalogSeparator.equals(""))
               catalogSeparator = ".";
            ConnectionManager.setCatalogSeparator(catalogSeparator);
            // System.out.println("Catalog Separator: " + catalogSeparator);
            
            // =======================
            // SQL Identifier
            
            identifierQuoteString = dbMetaData.getIdentifierQuoteString();
            if (identifierQuoteString == null || identifierQuoteString.equals(" "))
               identifierQuoteString = "";
            ConnectionManager.setIdentifierQuoteString(identifierQuoteString);
            // System.out.println("Identifier Quote String: " + identifierQuoteString);

            // Load parameters and the databases tables.
            ConnectionManager.loadDBParameters(dbConnection);
            ConnectionManager.loadDBTables(dbConnection);
            
            // Must be good so close things out and create a
            // costant connection for memory database connections.
            
            //db_resultSet.close();
            
            if ((subProtocol.equals(ConnectionManager.SQLITE) && db.toLowerCase().equals(":memory:"))
                 || (subProtocol.indexOf(ConnectionManager.HSQL) != -1
                     && db.toLowerCase().indexOf("mem:") != -1))
            {
               ConnectionManager.setMemoryConnection(DriverManager.getConnection(connectionString));
            }
            
            dbConnection.close();
            loggedIn = true;
         }
         catch (SQLException e)
         {
            if (splashPanel != null)
            {
               splashPanel.suspendPanel(true);
               splashWindow.dispose();
            }
            loggedIn = false;
            
            ConnectionManager.displaySQLErrors(e, "LoginFrame accessCheck()");
            return false;
            // ? Remove driver Manager
         }
      }
      return loggedIn;
   }
   
   //==============================================================
   // Class method to create a splash panel to provide a visual
   // indication of the application initialization process.
   //==============================================================

   private void createSplashWindow()
   {
      // Method Instances
      Dimension screenSize, imageSize;
      
      // Collect screen size and image size.
      
      screenSize = getToolkit().getScreenSize();
      imageSize = new Dimension(320,240);
      
      // Create window and animated splash.
      
      splashWindow = new JWindow();
      splashWindow.getContentPane().setLayout(new BorderLayout());
      splashWindow.setSize(imageSize);
      
      splashPanel = new SplashPanel(resourceBundle);
      
      splashWindow.getContentPane().add(splashPanel, BorderLayout.CENTER);
      splashWindow.setLocation((screenSize.width - imageSize.width) / 2,
                               (screenSize.height - imageSize.height) / 2);
      splashWindow.setVisible(true);
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