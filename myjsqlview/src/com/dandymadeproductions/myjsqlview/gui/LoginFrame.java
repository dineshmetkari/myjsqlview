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
// Copyright (C) 2005-2015 Dana M. Proctor
// Version 7.10 12/08/2014
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
//        6.83 Modified Class Method accessCheck() to Accomodate HSQL File and Resource
//             Database Connections via db Name.
//        6.84 HSQL mem,file,res Connection Parameters Changed to Use Three Argument
//             Constructor for DriverManager.getConnection() in accessCheck().
//        6.85 Changed Class Instances sitesNameList, driverList, protocolList, hostList,
//             subProtocolList, portList, databaseList, & userList from Vector Data Type
//             to ArrayList.
//        6.86 Class Instance sites Changed from Hashtable to HashMap. Method Instance
//             siteNames in fillSiteDataStructure() Changed from Enumeration to Iterator.
//        6.87 Class Method accessCheck() Added Instance maxColumNameLength. Collected &
//             Set in ConnectionManager in Same.
//        6.88 Changes in Way MyJSQLView_ResourceBundle Handles the Collection
//             of Resource Strings. Change to resource.getResourceString(key,
//             default).
//        6.89 MyJSQLView Class Method Change of getLocaleResourceBundle()
//             to getResourceBundle().
//        6.90 Class Method accessCheck() Change of Conditional Check for null to Empty
//             String for dbMetaData.getDatabaseProductName/Version().
//        6.91 Changed Package Name to com.dandymadeproductions.myjsqlview.gui.
//             Made Constructor & Method center() Public.
//        6.92 Removed Import of XMLUtilities & SiteParameters From Utilities
//             & Structures Since Both Moved to this Classes' Package.
//        6.93 Creation of splashPanelThread in Method createSplashWindow() for
//             Starting the splashPanel Animation.
//        6.94 Correction in accessCheck() for dbMetaData.getDatabaseProductName/
//             Version() Conditional Checks for Negation of Empty String.
//        6.95 Changes in accessCheck() to Generalize getConnection(URL, Properties)
//             to Allow Connection Properties to be Passed via the db TextField in
//             the StandardParametersPanel. Additional Output Info for Testing &
//             Default dbProductionNameVersion for Derby in Same Method.
//        6.96 Class Method Additions to Handle Both Derby Memory & Embedded Database
//             Connections. Also the Inclusion of Debug Output for connectionString.
//        6.97 Correction to Insure Memory Connection is Passed On to ConnectionManager
//             for Derby Memory Databases.
//        6.98 Method accessCheck() Debug Output for Driver & When Its Loaded.
//        6.99 Class Method fillSiteDefaults() Addition of Derby & H2 Defaults. Method
//             accessCheck() Addition of connectionString & dbProductNameVersion
//             Entries for H2 Database & Setting Memory Connection for Same. Correct
//             in Setting Memory Connection to Properly Use Properties As Change of 6.95.
//        7.00 SSL Connection Types Detection for H2 Database in connectionString for
//             Method accessCheck().
//        7.01 Method accessCheck() Correction for H2 Conditional Check for tcp to Use
//             else if.
//        7.02 Constructor Set Frame's Icon.
//        7.03 Class Method accessCheck() Changed Instance connectionString to
//             connectionURLString. Cleaned All Aspects of Collecting Database
//             Attributes from Same Method to New Declared Instance DatabaseProperties.
//        7.04 Class Method accessCheck() Debug Output for connectionURLString.
//        7.05 Minor Changes/Additions to Debug Output in Method accessCheck().
//        7.06 Class Method accessCheck() Conditional Check for Subprotocol Being
//             Identified as MariaDB if so Assign to Standard MySQL Database.
//        7.07 Class Method accessCheck() Removal of Change 7.06. In Same Method
//             Inclusion of MariaDB % Character Processing for Password.
//        7.08 Class Method accessCheck() Inclusion of Not Only Setting the SSH
//             Property for Select Databases, But Also Use in Connection Attempt.
//             Commented Out System.Out for Debug Purposes of Connect Properties
//             in Same Method.
//        7.09 Added Class Instance normString & Its Use in setSelectedSite().
//        7.10 Commented Out System.out in accessCheck() for Properties in Debug
//             Mode.
//
//-----------------------------------------------------------------
//                  danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.TreeSet;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.SwingConstants;

import com.dandymadeproductions.myjsqlview.MyJSQLView;
import com.dandymadeproductions.myjsqlview.datasource.ConnectionManager;
import com.dandymadeproductions.myjsqlview.datasource.ConnectionProperties;
import com.dandymadeproductions.myjsqlview.datasource.DatabaseProperties;
import com.dandymadeproductions.myjsqlview.gui.panels.AdvancedParametersPanel;
import com.dandymadeproductions.myjsqlview.gui.panels.SplashPanel;
import com.dandymadeproductions.myjsqlview.gui.panels.StandardParametersPanel;
import com.dandymadeproductions.myjsqlview.utilities.MyJSQLView_ResourceBundle;
import com.dandymadeproductions.myjsqlview.utilities.MyJSQLView_Utils;
import com.dandymadeproductions.myjsqlview.utilities.NormalizeString;

/**
 *    This LoginFrame class provides the framework for a login access
 * dialog to a database. The class provides the main frame used to allow
 * the user entry to the MyJSQLView application. Several input preferences
 * must be specified in this login in order to establish a valid connection
 * to a database. 
 * 
 * @author Dana M. Proctor
 * @version 7.10 12/08/2014
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

   private ArrayList<String> sitesNameList, driverList, protocolList, subProtocolList,
                    hostList, portList, databaseList, userList;
   
   private HashMap<String, SiteParameters> sites;
   private transient SiteParameters lastSite;
   private transient XMLTranslator xmlTranslator;
   private NormalizeString normString;
   
   private JButton loginManagerFrame_SaveExitButton, loginManagerFrame_CancelButton;

   private boolean loggedIn = false;
   private boolean advancedOptionsShowing = false;

   //==============================================================
   // LoginFrame Constructor
   //==============================================================

   public LoginFrame(JButton validLoginButton)
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
      
      resourceBundle = MyJSQLView.getResourceBundle();
      
      // Set Frame Parameters
      
      resource = resourceBundle.getResourceString("LoginFrame.message.Title", "Login");
      setTitle("MyJSQLView " + resource);
      setIconImage(MyJSQLView_Utils.getFrameIcon());
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      // Setting up Various Instances.
      
      xmlTranslator = new XMLTranslator();
      normString = new NormalizeString();
      sites = new HashMap <String, SiteParameters>();
      sitesNameList = new ArrayList <String>();
      driverList = new ArrayList <String>();
      protocolList = new ArrayList <String>();
      subProtocolList = new ArrayList <String>();
      hostList = new ArrayList <String>();
      portList = new ArrayList <String>();
      databaseList = new ArrayList <String>();
      userList = new ArrayList <String>();

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
      resource = resourceBundle.getResourceString("LoginFrame.menu.Sites", "Sites");
      siteSelectMenu = new JMenu(resource);
      
      resource = resourceBundle.getResourceString("LoginFrame.tooltip.SiteSelection", "Site Selection");
      siteSelectMenu.setToolTipText(resource);
      fillSiteDataStructures(siteSelectMenu);
      accessDialogMenuBar.add(siteSelectMenu);

      // Login Manager Frame Components.
      loginManagerFrame_AccessButton = new JButton(loginManagerIcon);
      loginManagerFrame_AccessButton.setFocusable(false);
      loginManagerFrame_AccessButton.setMargin(new Insets(0, 0, 0, 0));
      
      resource = resourceBundle.getResourceString("LoginFrame.tooltip.LoginManager", "Login Manager");
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
      resource = resourceBundle.getResourceString("LoginFrame.tooltip.AdvancedOptions", "Advanced Options");
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

      resource = resourceBundle.getResourceString("LoginFrame.checkbox.SSH", "SSH");
      sshCheckBox = new JCheckBox(resource, sshUpIcon);
      sshCheckBox.setSelectedIcon(sshDownIcon);
      sshCheckBox.setHorizontalTextPosition(SwingConstants.LEADING);
      sshCheckBox.setFocusPainted(false);
      actionPanel.add(sshCheckBox);
      
      resource = resourceBundle.getResourceString("LoginFrame.button.cancel", "cancel");
      cancelButton = new JButton(resource);
      cancelButton.addActionListener(this);
      actionPanel.add(cancelButton);
      
      resource = resourceBundle.getResourceString("LoginFrame.button.login", "login");
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
      resource = resourceBundle.getResourceString("LoginFrame.button.saveandexit", "save and exit");
      loginManagerFrame_SaveExitButton = new JButton(resource);
      loginManagerFrame_SaveExitButton.addActionListener(this);
      
      resource = resourceBundle.getResourceString("LoginFrame.button.cancel", "cancel");
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

               loginManagerFrame = new LoginManagerFrame(resourceBundle, sites, normString,
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
            resource = resourceBundle.getResourceString("LoginFrame.menu.Sites", "Sites");
            siteSelectMenu = new JMenu(resource);
            
            resource = resourceBundle.getResourceString("LoginFrame.tooltip.SiteSelection", "Site Selection");
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
            SiteParameters selectedSite = sites.get(actionCommandName);
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
      Iterator<String> siteNames;
      Iterator<String> sitesTreeIterator;

      TreeSet<String> sitesTreeSet;
      HashMap<String, JMenu> sitesJMenus;
      String siteName;

      // Remove all previous sites.
      siteSelectMenu.removeAll();

      // Create a natural order of JMenus
      // of the given sites' names.
      siteNames = sites.keySet().iterator();
      sitesTreeSet = new TreeSet <String>();

      while (siteNames.hasNext())
      {
         siteName = siteNames.next();

         if (!siteName.equals("Last Site") && siteName.indexOf('#') != -1)
            siteName = siteName.substring(0, siteName.indexOf('#'));
         else
            siteSelectMenu.add(createMenuItem(siteName, siteName));

         if (!sitesTreeSet.contains(siteName) && !siteName.equals("Last Site"))
            sitesTreeSet.add(siteName);
      }

      sitesTreeIterator = sitesTreeSet.iterator();
      sitesJMenus = new HashMap <String, JMenu>();

      while (sitesTreeIterator.hasNext())
      {
         String currentSiteName = sitesTreeIterator.next();
         JMenu currentSiteJMenu = new JMenu(currentSiteName);
         sitesJMenus.put(currentSiteName, currentSiteJMenu);
         siteSelectMenu.add(currentSiteJMenu);
      }

      // Now that the data has been organized
      // then clear any contents from the vectors
      // used for the comboboxes and refill also
      // in the process add databases to the site
      // JMenus.

      driverList.clear();
      protocolList.clear();
      subProtocolList.clear();
      hostList.clear();
      portList.clear();
      databaseList.clear();
      userList.clear();

      siteNames = sites.keySet().iterator();
      while (siteNames.hasNext())
      {
         String jmenuSiteName;
         String jmenuDBName;

         // Fill the JMenu first then create comboboxes
         // elements.

         siteName = (String) siteNames.next();
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
                                 "sun.jdbc.odbc.JdbcOdbcDriver", "org.apache.derby.jdbc.ClientDriver",
                                 "org.h2.Driver"};
      String[] defaultSubProtocols = {"mysql", "postgresql", "hsqldb:hsql", "oracle:thin", "sqlite", "odbc",
                                      "derby", "h2"};
      
      String[] defaultPorts = {"3306", "5432", "9001", "1521", "0000", "0000", "1527", "9092"};
      String[] defaultDatabases = {"mysql", "postgresql", "hsql;", "oracle", "test/sqlite_test.db",
                                   "ms_access", "test/derby_db/toursdb", "test/h2_db/h2_test"};
      
      // Clear contents to start anewed.
      driverList.clear();
      protocolList.clear();
      subProtocolList.clear();
      hostList.clear();
      portList.clear();
      databaseList.clear();
      userList.clear();
                                   
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
      standardParametersPanel.setPassword(
         XMLTranslator.textConversion(
            normString.execute(String.valueOf(selectedSite.getPassword()), false), true));
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
      
      String driver, protocol, subProtocol, host, port, db, user, passwordString, ssh;
      String connectionURLString;
      Properties connectProperties;
      
      char[] passwordCharacters;

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
            if (MyJSQLView.getDebug())
               System.out.println("LoginFrame accessCheck() Driver: " + driver);
            
            // Run SQLite in pure Java mode to maintain compatibility,
            // slower, but works with older versions of JVM. Revisit
            // Later if really needed.
            
            if (advancedParametersPanel.getSubProtocol().indexOf("sqlite") != -1)
               System.setProperty("sqlite.purejava", "true");
               
            Class.forName(driver);
            if (MyJSQLView.getDebug())
               System.out.println("LoginFrame accessCheck() Driver Loaded");
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

         connectProperties = new Properties();
         
         protocol = advancedParametersPanel.getProtocol();
         subProtocol = advancedParametersPanel.getSubProtocol().toLowerCase();
         host = standardParametersPanel.getHost();
         port = advancedParametersPanel.getPort();
         db = standardParametersPanel.getDataBase();
         
         user = standardParametersPanel.getUser();
         connectProperties.setProperty("user", user);
      
         if (sshCheckBox.isSelected())
         {
            ssh = "true";
            
            if (subProtocol.indexOf(ConnectionManager.HSQL) != -1
                || subProtocol.equals(ConnectionManager.MYSQL)
                || subProtocol.equals(ConnectionManager.POSTGRESQL))
            connectProperties.setProperty("useSSL", "1");
         }
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
         
         // The % character is interpreted as the start of a special escaped sequence,
         // two digit hexadeciaml value. So replace passwordString characters with that
         // character with that characters hexadecimal value as sequence, %37. Java
         // API URLDecoder.
         
         if (subProtocol.indexOf(ConnectionManager.HSQL) != -1
             || subProtocol.equals(ConnectionManager.DERBY)
             || subProtocol.equals(ConnectionManager.POSTGRESQL)
             || subProtocol.equals(ConnectionManager.MARIADB)
             || subProtocol.equals(ConnectionManager.MYSQL))
            passwordString = passwordString.replaceAll("%", "%" + Integer.toHexString(37));
         
         connectProperties.setProperty("password", passwordString);
         
         // Store parameters.
         ConnectionProperties connectionProperties = new ConnectionProperties();
         
         connectionProperties.setProperty(ConnectionProperties.DRIVER, driver);
         connectionProperties.setProperty(ConnectionProperties.PROTOCOL, protocol);
         connectionProperties.setProperty(ConnectionProperties.SUBPROTOCOL, subProtocol);
         connectionProperties.setProperty(ConnectionProperties.HOST, host);
         connectionProperties.setProperty(ConnectionProperties.PORT, port);
         connectionProperties.setProperty(ConnectionProperties.DB, db);
         connectionProperties.setProperty(ConnectionProperties.USER, user);
         connectionProperties.setProperty(ConnectionProperties.PASSWORD, passwordString);
         connectionProperties.setProperty(ConnectionProperties.SSH, ssh);
         
         connectionURLString = ConnectionManager.createConnectionURLString(connectionProperties);
         
         if (MyJSQLView.getDebug())
         {
            System.out.println("LoginFrame accessCheck() Connection URL: " + connectionURLString);
            // System.out.println("LoginFrame accessCheck() Connection Properties: "
            //                    + connectProperties.toString());
         }
         
         connectionProperties.setConnectionURLString(connectionURLString);

         // ===============================================
         // Connection Attempt.
         // ===============================================

         try
         {
            dbConnection = DriverManager.getConnection(connectionURLString, connectProperties);
            
            if (MyJSQLView.getDebug())
               System.out.println("LoginFrame accessCheck() Connection Established Initializing:");
            
            // The Connection is valid if it does not throw a SQL Exception.
            // So save the connection properties and collect the associated
            // database tables and other pertinent information necessary to
            // bring up the application with the DatabaseProperties Instance.
            
            createSplashWindow();
            ConnectionManager.setConnectionProperties(connectionProperties);
            
            DatabaseProperties databaseProperties = new DatabaseProperties(connectionProperties);
            databaseProperties.init(dbConnection);
            ConnectionManager.setDatabaseProperties(databaseProperties);
            
            // Override defaults with configuration file
            // if needed & load database tables.
            databaseProperties.overideDefaults();
            databaseProperties.loadDBTables(dbConnection);
            
            // Must be good so close things out and create a
            // costant connection for memory database connections.
            
            if ((subProtocol.equals(ConnectionManager.SQLITE) && db.toLowerCase().equals(":memory:"))
                 || (subProtocol.indexOf(ConnectionManager.HSQL) != -1
                     && db.toLowerCase().indexOf("mem:") != -1)    
                 || (subProtocol.equals(ConnectionManager.DERBY)
                     && db.toLowerCase().indexOf("memory:") != -1)
                 || (subProtocol.equals(ConnectionManager.H2)
                     && db.toLowerCase().indexOf("mem:") != -1))
            {
               ConnectionManager.setMemoryConnection(DriverManager.getConnection(connectionURLString,
                                                                                 connectProperties));
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
      Thread splashPanelThread = new Thread(splashPanel, "SplashPanelThread");
      splashPanelThread.start();
      
      splashWindow.getContentPane().add(splashPanel, BorderLayout.CENTER);
      splashWindow.setLocation((screenSize.width - imageSize.width) / 2,
                               (screenSize.height - imageSize.height) / 2);
      splashWindow.setVisible(true);
   }
   
   //==============================================================
   // Class method to center the frame.
   //==============================================================
   
   public void center()
   {
      Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
      Dimension us = getSize();
      int x = (screen.width - us.width) / 2;
      int y = (screen.height - us.height) / 2;
      setLocation(x, y);
   }
}
