//=================================================================
//                    MyJSQLView Utils Class
//=================================================================
//
//    This class provides various usedful methods used in the
// MyJSQLView application.
// 
//                 << MyJSQLView_Utils.java >>
//
//=================================================================
// Copyright (C) 2005-2015 Dana M. Proctor
// Version 9.9 11/20/2014
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
// Version 1.0 Original Class to Provide Common Utilities.
//         1.1 Main Class Name Change to MyJSQLView.
//         1.2 Cleaned out. Left Only some of the class methods. Currently not
//             being used by any classes, but wish to keep for possible future
//             use.
//         1.3 Updated Comments.
//         1.4 Cleaned Up Javadoc Comments.
//         1.5 Header Update.
//         1,6 Added Class Method setLocalTimeZone() For Oracle Sessions Editing
//             TIMESTAMPLTZ Fields.
//         1.7 MyJSQLView Project Common Source Code Formatting.
//         1.8 Added Class Method nDigitChop().
//         1.9 Added Class Method convertCharMonthToDecimal().
//         2.0 Renamed formatCSVDateString() to formatCSVExportDateString().
//             Added Class Method convertDecimalToCharMonth().
//         2.1 Correction in Class Method formatCSVExportDateString()
//             Conditional Clause Check for YYYY-MMM-dd.
//         2.2 Formatting and Class Method getAudioClip().
//         2.3 Restructured Class Method getAudioClip() try, catch blocks
//             to Properly Catch Each type of Exception.
//         2.4 System.outs Commented Out in Class Method getAudioClip().
//         2.5 Added Class Method stateConvert().
//         2.6 Class Method stateConvert() Check for NumberFormatException.
//         2.7 Created Class Method processFileChooser() to Control JFileChooser
//             to Query Confirmation for OverWriting Existing Files.
//         2.8 Added deleteFileIcon to Class Method processFileChooserSelection().
//         2.9 Added Class Methods getFileSeparator(), getIconsDirectory(), &
//             getMyJSQLViewDirectory().
//         3.0 Removed fileSeparator From End of String in Class Methods
//             getIconsDirectory() and getMyJSQLViewDirectory().
//         3.1 Added Class Method formatJavaDateString().
//         3.2 Class Method convertPasswordToString() Changed passwordString From
//             A String to StringBuffer. Same in stateConvert() Except Instance
//             convertedString.
//         3.3 Class Method convertPasswordToString() Returned Empty String As
//             Needed.
//         3.4 Changed Package to Reflect Dandy Made Productions Code.
//         3.5 Added Class Method processLocaleLanguage().
//         3.6 Comment Changes and getStandardCharacters(().
//         3.7 Added Class Method displayMyDateString().
//         3.8 Fix in Class Method processLocaleLanguage to Check and Create if
//             Necessary the .myjsqlview Directory Before Trying to create the
//             myjsqlview_locale.txt File. Added fileError Instance to Same.
//         3.9 Minor Changes in Declarations in Method processLocaleLanguage().
//         4.0 Made Class Public So That Plugins Can Gain Access to Key Methods.
//             Made Methods buildConst(), nDigitChop(), convertCharMonthToDecimal(),
//             convertDecimalToCharMonth(), getAudioClip(), getFileSeparator(),
//             & getIconsDirectory().
//         4.1 Added Class Method getPluginsDirectory().
//         4.2 Parameterized Instance localesData in Class Method processLocaleLanguage()
//             to Bring Code Into Compliance With Java 5.0 API.
//         4.3 Added Class Method getSchemaTableName().
//         4.4 Class Method formatExportDateString() Added Argument exportType.
//         4.5 Made Class Method processFileChooserSelection() Public.
//         4.6 Made Class Method stateConvert() Public.
//         4.7 Implemented Internationalization for InputDialogs in Class Methods
//             processFileChooserSelection() & processLocaleLanguage().
//         4.8 Removed Class Method getPluginsDirectory().
//         4.9 Backed Out 4.7 For Class Method processLocaleLanguage(), Duh.
//         5.0 Added Class Methods convertDBDateString_To_ViewDateString() and
//             convertViewDateString_To_DBDateString(). Removed Class Methods
//             formatExportDateString(), displayMyDateString(), & formatJavaDateString().
//         5.1 Reorganized Methods. Return Type for convertDecimalToCharMonth() to
//             String. Added Class Method processDateFormatSearch().
//         5.2 Class Method getSchemaTableName() identifierQuoteString Obtained From
//             Redefined ConnectionManager Class. Same for Class Method 
//             setLocalTimeZone() Except for SQL Errors.
//         5.3 Added Class Method getUnlimitedSQLStatementString(). Also Some Moving
//             Methods Around to List Alphabetically.
//         5.4 Added Class Method createColorChooser().
//         5.5 Class Method createColorChooser() Argument JComponent Changed to Component.
//             Class Method getUnlimitedSQLStatementString() Additional Fault Tolerance.
//         5.6 Added Class Method createEditMenu().
//         5.7 Added Class Method createTextDialog() and Modified createEditMenu()
//             Method to Return JMenuBar.
//         5.8 Class Method createTextDialog() setPreferred/MinimumSize() on JScrollPane.
//             Correction in Same for resourceCloseOpen From resourceSave for typeEdit.
//         5.9 Class Method getUnlimitedSQLStatementString() Correction to Check for
//             Oracle SQL Statement BETWEEN.
//         6.0 Added Class Method getCondtionString().
//         6.1 Made Class Methods convertDBDateString_To_ViewDateString() and
//             convertViewDateString_To_DBDateString().
//         6.2 Added Class Method processTableData_To_PDFOutput().
//         6.3 Class Method getConditionString() Changes to Check if Oracle Has BETWEEN
//             So That Can Remove Limits.
//         6.4 Correction in Class Method getConditionString() to Pattern Match replaceAll()
//             in query to be Non-Case Sensitive for WHERE.
//         6.5 Class Methods getConditionString() & getUnlimitedSQLStatementString()
//             Replace Instance subProtocol With dataSourceType.
//         6.6 Class Method getUnlimitedSQLStatement Inclusion of HSQL2 With HSQL for
//             Processing LIMIT Keyword.
//         6.7 Class Method getSchemaTableName() Replaced Hard Coded Period With the
//             catalogSeparator.
//         6.8 Removal of colorChooserPanels Instance and Setting of Only One of the 
//             Panels Because Lack of Support One in Gnome Desktop. Class Method
//             createColorChooser().
//         6.9 Added static Class Instances for Date Formats and dateFormatOptions.
//             Used New Date Formats in Class Methods convertDBDateString(),
//             convertViewDateString(), & processDateFormatSearch().
//         7.0 Changed Class Instance dateFormatOptions From public to private.
//             Added Class Method getDateFormatOptions().
//         7.1 Copyright Update.
//         7.2 Recoded getConditionString() Method to Handle GROUP BY in Query. Commented
//             Method getDateFormat().
//         7.3 Class Method processLocaleLanguage() Instance localesData from Vector
//             Data Type to ArrayList.
//         7.4 Changes in Way MyJSQLView_ResourceBundle Handles the Collection
//             of Resource Strings. Change to resource.getResourceString(key,
//             default).
//         7.5 Added Class Methods clearCache() & getCacheDirectory().
//         7.6 MyJSQLView Class Method Change of getLocaleResourceBundle()
//             to getResourceBundle().
//         7.7 Collection of All Image Resources Through resourceBundle.
//         7.8 Changed Package Name to com.dandymadeproductions.myjsqlview.utilities.
//             Made getMyJSQLViewDirectory(), processLocaleLanguage(), setLocalTimeZone(),
//             clearCache(), & getUnlimitedSQLStatementString() Public.
//         7.9 Made Method getStandardCharacters() Public.
//         8.0 Removed the Use of MyJSQLView_ResouceBundle For Getting localeIcon
//             in Method processLocaleLanguage().
//         8.1 Class Method clearCache() Identifiable Error Output to Routine &
//             Skipping Directory Entries. Commented Out System.out in Method
//             processLocaleLanguage().
//         8.2 Class Method getCacheDirectory() Implemented the Attempt to Try & Create
//             the Cache Directory on the File System if Does not Exist.
//         8.3 Correction in Method convertViewDateString() to Check for Two Occurrences
//             of the Dash Character to Insure a Valid Date Format.
//         8.4 Method getUnlimitedSQLStatementString() Addition of Derby Processing and
//             Instance unLimitedSQLStatementString.
//         8.5 Alternate StringBuffer Manipulation in getUnLimitdSQLStatementString().
//         8.6 Changed in convertViewDateString() to Properly Check for Dashes in the
//             Date String, 2, NOT IndexOf 2.
//         8.7 Added Class Method setUIManagerFont().
//         8.8 Change in processDateFormatSearch() to Use DBTablePanel.getGeneralDBProperties().
//         8.9 Improved the Efficiency Method setUIManagerFont() By Not Assigning of
//             Given Argument if Equal to the System Setting.
//         9.0 Change in Method processTableData_To_PDFOutput() to Correct the Starting
//             of the Thread to Properly Output.
//         9.1 Changed Method buildConst() to buildConstraints().
//         9.2 Cleaned Up System.outs To Give More Consistent Information. Minor Formatting
//             Changes.
//         9.3 Method setUIManagerFont() Check of uiObject NULL Before instanceof Font.
//         9.4 Added Class Method getFrameIcon().
//         9.5 Added Overloaded Class Method getSchemaTableName().
//         9.6 Class Methods getConditionString() & getUnlimitedSQLStatementString()
//             Detection of MSSQL for Processing. Sample Query Comment for MSSQL in Latter
//             Method.
//         9.7 Class Method processLocaleLanguage() Additional Check on localeFile for
//             Length & Also Insuring the Configuration File Gets Written to Even if
//             the User Cancels or Disposes the Dialog.
//         9.8 Parameterized Class Instance localeComboBox in Method processLocaleLanguage()
//             to Bring Into Compliance With JRE 7. Minor System.out Comment Changes in
//             Method getAudioClip().
//         9.9 Added Class Method getChartList().
//       
//-----------------------------------------------------------------
//                danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview.utilities;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
// import javax.sound.sampled.LineEvent;
// import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.DefaultEditorKit;

import com.dandymadeproductions.myjsqlview.MyJSQLView;
import com.dandymadeproductions.myjsqlview.datasource.ConnectionManager;
import com.dandymadeproductions.myjsqlview.gui.panels.DBTablesPanel;
import com.dandymadeproductions.myjsqlview.io.PDFDataTableDumpThread;
import com.dandymadeproductions.myjsqlview.io.WriteDataFile;

/**
 *    The MyJSQLView_Utils class provides various usedful methods
 * used in the MyJSQLView application.
 * 
 * @author Dana M. Proctor
 * @version 9.9 11/20/2014
 */

public class MyJSQLView_Utils extends MyJSQLView
{
   // Class Instances
   
   public static final String MMddyyyy_DASH = "MM-dd-yyyy";
   public static final String MMddyyyy_SLASH = "MM/dd/yyyy";
   public static final String MMMddyyyy_DASH = "MMM-dd-yyyy";
   public static final String ddMMyyyy_DASH = "dd-MM-yyyy";
   public static final String ddMMyyyy_SLASH = "dd/MM/yyyy";
   public static final String ddMMMyyyy_DASH = "dd-MMM-yyyy";
   public static final String yyyyMMdd_DASH = "yyyy-MM-dd";
   public static final String yyyyMMdd_SLASH = "yyyy/MM/dd";
   public static final String yyyyMMMdd_DASH = "yyyy-MMM-dd";
   public static final String MMM = "MMM";
   
   private static final Object[] dateFormatOptions = {MMddyyyy_DASH, MMddyyyy_SLASH, MMMddyyyy_DASH,
                                                     ddMMyyyy_DASH, ddMMyyyy_SLASH, ddMMMyyyy_DASH,
                                                     yyyyMMdd_DASH, yyyyMMdd_SLASH, yyyyMMMdd_DASH};
   
   //==============================================================
   // Protected class Method for helping the parameters in gridbag.
   // Most GUI panels call this class method.
   //==============================================================

   public static void buildConstraints(GridBagConstraints gbc, int gx, int gy, int gw, int gh,
                                       double wx, double wy)
   {
      gbc.gridx = gx;
      gbc.gridy = gy;
      gbc.gridwidth = gw;
      gbc.gridheight = gh;
      gbc.weightx = wx;
      gbc.weighty = wy;
   }
   
   //==============================================================
   // Method for clearing the cache directory of files. Assumes
   // that only files have been placed there, does not crawl.
   //==============================================================
   
   public static void clearCache()
   {
      // Method Instances
      File cacheDirectory;
      File[] cacheContents;
      boolean fileDeleted, cacheClearFailure;
      
      // Setup
      cacheDirectory = new File(MyJSQLView_Utils.getCacheDirectory());
      cacheClearFailure = false;
      
      // See if cache exists
      if (cacheDirectory.exists() && cacheDirectory.isDirectory())
      {
         // Collect contents and delete.
         try
         {
            cacheContents = cacheDirectory.listFiles();
            
            int i = 0;
            while (i < cacheContents.length)
            {
               if (cacheContents[i].isFile())
               {
                  fileDeleted = cacheContents[i].delete();
                  
                  if (!fileDeleted)
                     cacheClearFailure = true;  
               }
               i++;
            }
         }
         catch (SecurityException se)
         {
           if (MyJSQLView.getDebug()) 
              System.out.println("MyJSQLView_Utils clearCache() Failed to Clear Cache: " + se.toString());
         }
         
         if (cacheClearFailure)
            if (MyJSQLView.getDebug())
               System.out.println("MyJSQLView_Utils clearCache() Failed to Clear a Cache File.");
      }
   }
   
   //==============================================================
   // Method for converting a month text character input to the
   // valid numeric value.
   //==============================================================
   
   public static String convertCharMonthToDecimal(String month)
   {
      if (month.toLowerCase().indexOf("jan") != -1)
         return "01";
      else if (month.toLowerCase().indexOf("feb") != -1)
         return "02";
      else if (month.toLowerCase().indexOf("mar") != -1)
         return "03";
      else if (month.toLowerCase().indexOf("apr") != -1)
         return "04";
      else if (month.toLowerCase().indexOf("may") != -1)
         return "05";
      else if (month.toLowerCase().indexOf("jun") != -1)
         return "06";
      else if (month.toLowerCase().indexOf("jul") != -1)
         return "07";
      else if (month.toLowerCase().indexOf("aug") != -1)
         return "08";
      else if (month.toLowerCase().indexOf("sep") != -1)
         return "09";
      else if (month.toLowerCase().indexOf("oct") != -1)
         return "10";
      else if (month.toLowerCase().indexOf("nov") != -1)
         return "11";
      else if (month.toLowerCase().indexOf("dec") != -1)
         return "12";
      else
         return "0";
   }

   //==============================================================
   // Method for converting a month integer value 01-12 to a valid
   // three character month string.
   //==============================================================

   public static String convertDecimalToCharMonth(int month)
   {
      if (month == 1)
         return "Jan";
      else if (month == 2)
         return "Feb";
      else if (month == 3)
         return "Mar";
      else if (month == 4)
         return "Apr";
      else if (month == 5)
         return "May";
      else if (month == 6)
         return "Jun";
      else if (month == 7)
         return "Jul";
      else if (month == 8)
         return "Aug";
      else if (month == 9)
         return "Sep";
      else if (month == 10)
         return "Oct";
      else if (month == 11)
         return "Nov";
      else if (month == 12)
         return "Dec";
      else
         return month + "";
   }

   //==============================================================
   // Method for converting a date input string from the standard
   // database format, yyyy-MM-DD, to a selected view date string.
   //==============================================================

   public static String convertDBDateString_To_ViewDateString(String db_DateString, String dateFormat)
   {
      // Method Instances
      String year, month, day;
      String formattedDateString;
      int firstDashIndex, lastDashIndex;
      
      if (db_DateString != null && !db_DateString.equals(""))
      {
         // Collect dashes
         db_DateString = db_DateString.trim();
         firstDashIndex = db_DateString.indexOf("-");
         lastDashIndex = db_DateString.lastIndexOf("-");
         
         if (firstDashIndex == -1 || lastDashIndex == -1)
            return "";
         
         // Collect the year, month, & day.
         year = db_DateString.substring(0, firstDashIndex);
         month = db_DateString.substring((firstDashIndex + 1), lastDashIndex);
         day = db_DateString.substring(db_DateString.lastIndexOf("-") + 1);
            
         // Convert to the selected format.
         
         // yyyy-MM-dd
         if (dateFormat.equals(yyyyMMdd_DASH) || dateFormat.equals(yyyyMMdd_SLASH)
             || dateFormat.equals(yyyyMMMdd_DASH))
         {
            if (dateFormat.indexOf(MMM) != -1)
               formattedDateString = year + "-"
                                 + MyJSQLView_Utils.convertDecimalToCharMonth(Integer.parseInt(month)) + "-"
                                 + day;
            else
               formattedDateString = year + "-" + month + "-" + day;
         }

         // dd-MM-yyyy
         else if (dateFormat.equals(ddMMyyyy_DASH) || dateFormat.equals(ddMMyyyy_SLASH)
                  || dateFormat.equals(ddMMMyyyy_DASH))
         {
            if (dateFormat.indexOf(MMM) != -1)
               formattedDateString = day + "-"
                                 + MyJSQLView_Utils.convertDecimalToCharMonth(Integer.parseInt(month)) + "-"
                                 + year;
            else
               formattedDateString = day + "-" + month + "-" + year;
         }

         // MM-dd-yyyy
         else
         {
            if (dateFormat.indexOf(MMM) != -1)
               formattedDateString = MyJSQLView_Utils.convertDecimalToCharMonth(Integer.parseInt(month)) + "-"
                                     + day + "-" + year;
            else
               formattedDateString = month + "-" + day + "-" + year;
         }

         // Replace standard dashes in date with slashes as required.
         if (dateFormat.indexOf("/") != -1)
            formattedDateString = formattedDateString.replaceAll("-", "/"); 
         
         // System.out.println("MyJSQLView_Utils convertDBDateString()" + "Year:" + year + " Month:" + month
         //                    + " Day:" + day);
         return formattedDateString;
      }
      else
         return db_DateString;    
   }
   
   //==============================================================
   // Method for converting a date input string of the selected
   // date format to the standard database format yyyy-MM-DD.
   //==============================================================
   
   public static String convertViewDateString_To_DBDateString(String view_DateString, String dateFormat)
   {
      // Method Instances.
      String year, month, day;
      int firstDashIndex, lastDashIndex;
      
      // Generally just replace all forward slashes to required dash
      // and check to make sure there is some kind of valid format.
      // Routine does not allow dates with spaces, ex. Jan. 01, 2009.
      
      if (view_DateString.indexOf("/") != -1)
         view_DateString = view_DateString.replaceAll("/", "-");
      
      firstDashIndex = view_DateString.indexOf("-");
      lastDashIndex = view_DateString.lastIndexOf("-");
      
      if (firstDashIndex == -1 || lastDashIndex == -1)
         return "";
      
      // Convert the input date string to the appropriate format.
      
      // yyyy-MM-dd
      if (dateFormat.equals(yyyyMMdd_DASH) || dateFormat.equals(yyyyMMdd_SLASH)
          || dateFormat.equals(yyyyMMMdd_DASH))
      {
         year = view_DateString.substring(0, firstDashIndex);
         month = view_DateString.substring(firstDashIndex + 1, lastDashIndex);
         if (month.length() > 2)
            month = MyJSQLView_Utils.convertCharMonthToDecimal(month);
         day = view_DateString.substring(lastDashIndex + 1);
      }
      
      // dd-MM-yyyy
      else if (dateFormat.equals(ddMMyyyy_DASH) || dateFormat.equals(ddMMyyyy_SLASH)
            || dateFormat.equals(ddMMMyyyy_DASH))
      {
         year = view_DateString.substring(lastDashIndex + 1);
         month = view_DateString.substring(firstDashIndex + 1, lastDashIndex);
         if (month.length() > 2)
            month = MyJSQLView_Utils.convertCharMonthToDecimal(month);
         day = view_DateString.substring(0, firstDashIndex);
      }
      
      // MM-dd-yyyy
      else
      {  
         year = view_DateString.substring(lastDashIndex + 1);
         month = view_DateString.substring(0, firstDashIndex);
         if (month.length() > 2)
            month = MyJSQLView_Utils.convertCharMonthToDecimal(month);
         day = view_DateString.substring(firstDashIndex + 1, lastDashIndex); 
      }
      
      // System.out.println("MyJSQLView_Utils convertViewDateString_To_DBDateString()" + "Year:" + year
      //                    + " Month:" + month + " Day:" + day);
      return year + "-" + month + "-" + day; 
   }
   
   //==============================================================
   // Class method to convert a password character set to a string.
   //==============================================================

   protected static String convertPasswordToString(char[] passwordCharacters)
   {
      StringBuffer passwordString = new StringBuffer();

      // Obtaining the password & clearing.
      for (int i = 0; i < passwordCharacters.length; i++)
         passwordString.append(passwordCharacters[i]);

      if (passwordString.length() == 0)
         return "";
      else
         return passwordString.toString();
   }
   
   //==============================================================
   // Class Method to create a color chooser used to select the
   // color for the title, header, and border options.
   //==============================================================

   public static JColorChooser createColorChooser(Component component)
   {
      // Method Instances.
      JColorChooser colorChooser;

      // Create color chooser.
      colorChooser = new JColorChooser();
      colorChooser.setBorder(BorderFactory.createTitledBorder("Color"));
      colorChooser.setColor(component.getBackground());
      colorChooser.setPreviewPanel(new JPanel());
      
      return colorChooser;
   }
   
   //==============================================================
   // Class method to create a InputDialog with a EditorPane. 
   //==============================================================

   public static InputDialog createTextDialog(boolean typeEdit, JEditorPane editorPane)
   {
      // Method Instances.
      InputDialog textDialog;
      MyJSQLView_ResourceBundle resourceBundle;
      String resourceTitle, resourceSave, resourceCloseOpen;

      // Create an EditorPane with a ScrollPane to view
      // the content.
      
      if (!typeEdit)
         editorPane.setEditable(false);

      JScrollPane scrollPane = new JScrollPane(editorPane);
      scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
      scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
      scrollPane.setPreferredSize(new Dimension(500, 350));
      scrollPane.setMinimumSize(new Dimension(400, 200));

      Object[] content = {scrollPane};

      // Create a frame to show with menubar.
      
      resourceBundle = MyJSQLView.getResourceBundle();
      resourceTitle = resourceBundle.getResourceString("MyJSQLView_Utils.dialogtitle.TextData",
                                                       "Text Data");
      resourceSave = resourceBundle.getResourceString("MyJSQLView_Utils.dialogbutton.Save", "Save");
      
      if (typeEdit)
         resourceCloseOpen = resourceBundle.getResourceString("MyJSQLView_Utils.dialogbutton.Open",
                                                              "Open");
      else
         resourceCloseOpen = resourceBundle.getResourceString("MyJSQLView_Utils.dialogbutton.Close",
                                                              "Close");
      
      textDialog = new InputDialog(null, resourceTitle, resourceSave, resourceCloseOpen, content, null);
      return textDialog;
   }
   
   //==============================================================
   // Class method to create a Edit Menu Bar for an EditorPane. 
   //==============================================================

   public static JMenuBar createEditMenu(boolean typeEdit)
   {
      // Method Instances.
      JMenuBar editorMenuBar;
      JMenu editMenu;
      JMenuItem menuItem;
      JTextPane textComponent;
      MyJSQLView_ResourceBundle resourceBundle;
      String resource;

      // Create menu items cut, copy, paste, and
      // select all.
      
      editorMenuBar = new JMenuBar();
      
      resourceBundle = MyJSQLView.getResourceBundle();
      resource = resourceBundle.getResourceString("MyJSQLView_Utils.menu.Edit", "Edit");
      editMenu = new JMenu(resource);
      editMenu.setFont(editMenu.getFont().deriveFont(Font.BOLD));

      if (typeEdit)
      {
         menuItem = new JMenuItem(new DefaultEditorKit.CutAction());
         resource = resourceBundle.getResourceString("MyJSQLView_Utils.menu.Cut", "Cut");
         menuItem.setText(resource + "          " + "Ctrl-x");
         menuItem.setMnemonic(KeyEvent.VK_X);
         editMenu.add(menuItem);
      }

      menuItem = new JMenuItem(new DefaultEditorKit.CopyAction());
      resource = resourceBundle.getResourceString("MyJSQLView_Utils.menu.Copy", "Copy");
      menuItem.setText(resource + "       " + "Ctrl-c");
      menuItem.setMnemonic(KeyEvent.VK_C);
      editMenu.add(menuItem);

      if (typeEdit)
      {
         menuItem = new JMenuItem(new DefaultEditorKit.PasteAction());
         resource = resourceBundle.getResourceString("MyJSQLView_Utils.menu.Paste", "Paste");
         menuItem.setText(resource + "       " + "Ctrl-v");
         menuItem.setMnemonic(KeyEvent.VK_V);
         editMenu.add(menuItem);
      }

      editMenu.addSeparator();

      // Seems the DefaultEditorKit does not provide the ability
      // to obtain the action command directly for select-all?
      // editMenu.add(action.getActionByName(DefaultEditorKit.selectAllAction));

      textComponent = new JTextPane();
      Action[] textActionsArray = textComponent.getActions();
      for (int i = 0; i < textActionsArray.length; i++)
      {
         Action currentAction = textActionsArray[i];
         // System.out.println("MyJSQLview_Utils createEditMenu()" + currentAction.getValue(Action.NAME));
         
         if (currentAction.getValue(Action.NAME).equals("select-all"))
         {
            menuItem = new JMenuItem(currentAction);
            resource = resourceBundle.getResourceString("MyJSQLView_Utils.menu.SelectAll", "Select All");
            menuItem.setText(resource);
            editMenu.add(menuItem);
         }
      }
      editMenu.add(menuItem);
      editorMenuBar.add(editMenu);
      
      return editorMenuBar;
   }
   
   //==============================================================
   // Class method to load a provided sound file into a audio clip.
   // A check of the returned clip should be made to insure it was
   // properly created. The method will return NULL if it was not.
   //==============================================================

   public static Clip getAudioClip(String fileName)
   {
      // Method Instances
      File audioFile;
      AudioInputStream audioInputStream;
      AudioFormat audioFormat;
      DataLine.Info dataLineInfo;
      Clip clip;

      audioFile = new File(fileName);

      // Create a audio stream that will be used to load the
      // audio file then open a line that will be associated
      // with the clip.

      try
      {
         try
         {
            audioInputStream = AudioSystem.getAudioInputStream(audioFile);
         }
         catch (UnsupportedAudioFileException e)
         {
            // System.out.println("MyJSQLView_Utils getAudioClip() Unsupported Audio File Format.\n" + e);
            return null;
         }

         audioFormat = audioInputStream.getFormat();
         dataLineInfo = new DataLine.Info(Clip.class, audioFormat);

         if (!AudioSystem.isLineSupported(dataLineInfo))
         {
            // System.out.println("MyJSQLView_Utils getAudioClip() Line NOT Supported");
            audioInputStream.close();
            return null;
         }
         else
         {
            try
            {
               clip = (Clip) AudioSystem.getLine(dataLineInfo);

               // The follwing class may be uncommented to
               // provide a event listener to monitor the
               // activity of the sound clip.
               
               /*
               class lineEvent implements LineListener
               {
                  public void update(LineEvent e)
                  {
                     Object eventSource = e.getSource();
                     if (eventSource instanceof Clip)
                     {
                        System.out.println("MyJSQLView_Utils getAudioClip() " + e.getType());
                         
                        if (e.getType() == LineEvent.Type.STOP)
                           ((Clip)e.getSource()).close();
                     }
                  }
               }
               clip.addLineListener(new lineEvent());
               */
               
               clip.open(audioInputStream);
               audioInputStream.close();
               return clip;
            }
            catch (LineUnavailableException e)
            {
               // System.out.println("MyJSQLView_Utils getAudioClip() Line Unavailable.\n" + e);
               return null;
            }
         }
      }
      catch (IOException e)
      {
         // System.out.println("MyJSQLView_Utils getAudioClip() IO Exception in InputStream.\n" + e);
         return null;
      }
   }
   
   //==============================================================
   // Class method to return the MyJSQLView's cache directory.
   //==============================================================

   public static String getCacheDirectory()
   {
      // Method Instances
      String cacheDirectory;
      File cacheDirectoryFile;
      
      cacheDirectory =  MyJSQLView_Utils.getMyJSQLViewDirectory() + MyJSQLView_Utils.getFileSeparator()
                        + "cache" + MyJSQLView_Utils.getFileSeparator();
      
      cacheDirectoryFile = new File(cacheDirectory);
      
      if (!cacheDirectoryFile.isDirectory())
      {
         try
         {
            cacheDirectoryFile.mkdirs();
         }
         catch (SecurityException se)
         {
            if (MyJSQLView.getDebug())
               System.out.println("MyJSQLView_Utils getCacheDirectory() Failed to Make Cache Directory.\n"
                                  + se.toString());
         }
      }
      return cacheDirectory;
   }
   
   //==============================================================
   // Class method to return the chart's list.
   //==============================================================

   public static int[] getChartList(int n, int value)
   {
      // Method Instances
      Random g;
      HashMap<Integer, Integer> m;
      int over, post;
      int[] chList;
      
      g = new Random();
      m = new HashMap<Integer, Integer>();
      chList = new int[n];
      post = 0;
      
      while (post < n - 1)
      {
         over = g.nextInt(value);
         while (m.containsValue(Integer.valueOf(over)))
            over = g.nextInt(value);
         m.put(Integer.valueOf(post), Integer.valueOf(over));
         chList[post] = over;
         post++;  
      }
      m.put(Integer.valueOf(post), Integer.valueOf(n));
      chList[post] = value;
      
      return chList;
   }
   
   //==============================================================
   // Class Method to parse the WHERE condition given a SQL query
   // statement.
   //==============================================================
   
   public static String getConditionString(String query)
   {
      // Method Instances.
      String dataSourceType, conditionString;
      
      conditionString = "";
      dataSourceType = ConnectionManager.getDataSourceType();
      
      if (query.toUpperCase().indexOf("WHERE") == -1)
         return conditionString;
      
      query = query.replaceAll("(?i)where", "WHERE");
     
      // Oracle & MSSQL
      if (dataSourceType.equals(ConnectionManager.ORACLE) || dataSourceType.equals(ConnectionManager.MSSQL))
      {
         if (query.toUpperCase().indexOf("BETWEEN") != -1)
            query = getUnlimitedSQLStatementString(query);
         
         if (query.toUpperCase().indexOf(" GROUP BY ") != -1)
            conditionString = query.substring(query.indexOf("WHERE"),
                                      query.toUpperCase().indexOf(" GROUP BY "));
         else
         {
            if (query.toUpperCase().indexOf(" ORDER BY ") != -1)
               conditionString = query.substring(query.indexOf("WHERE"),
                                         query.toUpperCase().indexOf(" ORDER BY "));
            else
               conditionString = query.substring(query.indexOf("WHERE"));
         }
      }
      // All Other Databases.
      else
      {
         if (query.toUpperCase().indexOf(" GROUP BY ") != -1)
            conditionString = query.substring(query.indexOf("WHERE"),
                                      query.toUpperCase().indexOf(" GROUP BY "));
         else
         {
            if (query.toUpperCase().indexOf(" ORDER BY ") != -1)
               conditionString = query.substring(query.indexOf("WHERE"),
                                         query.toUpperCase().indexOf(" ORDER BY "));
            else
            {
               if (query.toUpperCase().indexOf("LIMIT") != -1)
                  conditionString = query.substring(query.indexOf("WHERE"),
                                         query.toUpperCase().indexOf(" LIMIT"));
               else
                  conditionString = query.substring(query.indexOf("WHERE"));
            } 
         }
      }
      return conditionString;
   }
   
   //==============================================================
   // Class method to return a copy of the Date Format options that
   // MyJSQLView recognizes as valid dates, example MM/dd/YYYY.
   //==============================================================
   
   public static Object[] getDateFormatOption()
   {
      Object[] dateFormatOptionsCopy = new Object[dateFormatOptions.length];
      
      for (int i = 0; i < dateFormatOptions.length; i++)
         dateFormatOptionsCopy[i] = dateFormatOptions[i];
      
      return dateFormatOptionsCopy;
   }
   
   //==============================================================
   // Class method to return a standardized frame icon.
   //==============================================================

   public static Image getFrameIcon()
   {
      return MyJSQLView.getResourceBundle().getResourceImage(MyJSQLView_Utils.getIconsDirectory() +
                                                      MyJSQLView_Utils.getFileSeparator()
                                                      + "searchIcon.png").getImage();
   }
   
   //==============================================================
   // Class method to return the system file separator character.
   //==============================================================

   public static String getFileSeparator()
   {
      String fileSeparator;

      fileSeparator = System.getProperty("file.separator");

      if (fileSeparator == null || fileSeparator.equals(""))
         fileSeparator = "/";

      return fileSeparator;
   }

   //==============================================================
   // Class method to return the image icons directory path.
   //==============================================================

   public static String getIconsDirectory()
   {
      return "images" + getFileSeparator() + "icons";
   }
   
   //==============================================================
   // Class method to derive the user's home directory path.
   //==============================================================
   
   public static String getMyJSQLViewDirectory()
   {
      return System.getProperty("user.home") + getFileSeparator() + ".myjsqlview";
   }
   
   //==============================================================
   // Class method to return the properly format SQL database table
   // name to be used in query statement. The argumnet must be a
   // valid table name for the current database that MyJSQLView is
   // connected to.
   //==============================================================
   
   public static String getSchemaTableName(String sqlTable)
   {
      return getSchemaTableName(sqlTable, ConnectionManager.getCatalogSeparator(),
                                ConnectionManager.getIdentifierQuoteString());
   }
   
   //==============================================================
   // Class method to return the properly format SQL database table
   // name to be used in a query statement given the catalog &
   // identifier quote string.
   //==============================================================

   public static String getSchemaTableName(String sqlTable, String catalogSeparator,
                                           String identifierQuoteString)
   {
      String schemaTableName;
      
      if (sqlTable.indexOf(catalogSeparator) != -1)
      {
         schemaTableName = identifierQuoteString
                           + sqlTable.substring(0, sqlTable.indexOf(catalogSeparator))
                           + identifierQuoteString + catalogSeparator + identifierQuoteString
                           + sqlTable.substring(sqlTable.indexOf(catalogSeparator) + 1)
                           + identifierQuoteString;
      }
      else
         schemaTableName = identifierQuoteString + sqlTable + identifierQuoteString;
      
      // System.out.println("MyJSQLView_Utils getSchemaTableName() " + schemaTableName);
      
      return schemaTableName;
   }
   
   //==============================================================
   // Class method to allow standard characters retrieval.
   //==============================================================

   public static char[] getStandardCharacters()
   {
      return "k8^ef1209rEW-+$xB1aH".toCharArray();
   }
   
   //==============================================================
   // Class method to allow the parsing of the standard SQL
   // statement string from a TableTabPanel to remove the LIMIT
   // condition.
   //==============================================================
   
   public static String getUnlimitedSQLStatementString(String sqlStatementString)
   {
      // Method Instances
      String dataSourceType, trimmedString;
      StringBuffer unLimitedSQLStatementString;
      int index1, index2, index3, index4, index5;
      
      // Collect the database protocol & setup.
      
      dataSourceType = ConnectionManager.getDataSourceType();
      unLimitedSQLStatementString = new StringBuffer();
      
      // Check if process can be performed.
      
      if ((!dataSourceType.equals(ConnectionManager.ORACLE)
            && !dataSourceType.equals(ConnectionManager.MSSQL)
            && !dataSourceType.equals(ConnectionManager.DERBY)
            && (sqlStatementString.indexOf("LIMIT") == -1))
          ||
           (((dataSourceType.equals(ConnectionManager.ORACLE)
             || dataSourceType.equals(ConnectionManager.MSSQL)))
            && (sqlStatementString.indexOf("BETWEEN") == -1))
          ||
           ((dataSourceType.equals(ConnectionManager.DERBY))
             && (sqlStatementString.indexOf("OFFSET") == -1)))
         return sqlStatementString;
      
      // Parsing
      
      // Oracle
      if (dataSourceType.equals(ConnectionManager.ORACLE)
          || dataSourceType.equals(ConnectionManager.MSSQL))
      {
         // Samples
         
         // Oracle
         // SELECT "PARENT_ID", "NAME" FROM (SELECT ROW_NUMBER() OVER (ORDER BY "PARENT_ID" ASC)
         // AS dmprownumber, "PARENT_ID", "NAME" FROM "DANAP"."CHILD" WHERE '1' LIKE '%') WHERE
         // dmprownumber BETWEEN 1 AND 50
         
         // MSSQL
         // SELECT "parent_id", "name" FROM (SELECT "parent_id", "name", ROW_NUMBER() OVER 
         // (ORDER BY t."parent_id" ASC) AS dmprownumber FROM "dbo"."child" AS t WHERE '1' LIKE '%')
         // AS t1 WHERE t1.dmprownumber BETWEEN 1 AND 50
         
         index1 = sqlStatementString.indexOf("(");
         index2 = sqlStatementString.lastIndexOf("FROM");
         index3 = sqlStatementString.lastIndexOf(")");
         index4 = sqlStatementString.indexOf("ORDER");
         index5 = sqlStatementString.toUpperCase().indexOf("AS DMPROWNUMBER");
         
         if (index1 != -1 && index2 != -1 && index3 != -1 && index4 != -1 && index5 != -1)
         {
            unLimitedSQLStatementString.append(sqlStatementString.substring(0, (index1 - 1))
                                               + sqlStatementString.substring((index2 + 4), index3)
                                               + " " + sqlStatementString.substring(index4, (index5 - 2)));
         }
      }
      // HSQL, & HSQL2
      else if (dataSourceType.equals(ConnectionManager.HSQL)
               || dataSourceType.equals(ConnectionManager.HSQL2))
      {
         // Sample
         // SELECT LIMIT 0 50 "PARENT_ID", "NAME" FROM "PUBLIC"."CHILD" WHERE TRUE LIKE '%' ORDER
         // BY "PARENT_ID" ASC
         
         if (sqlStatementString.indexOf("LIMIT") != -1)
         {
            index1 = sqlStatementString.indexOf("LIMIT");
            index2 = sqlStatementString.indexOf(ConnectionManager.getIdentifierQuoteString());
            
            // Select string creation by if no criteral specified,
            // HSQL LIMIT Problem.
            
            if (index1 < index2)
               unLimitedSQLStatementString.append(sqlStatementString.substring(0, index1)
                                                  + sqlStatementString.substring(index2));
            // Same as MySQL, Postgresql, & SQLite.
            else
               unLimitedSQLStatementString.append(sqlStatementString.substring(0, index1));
         }
      }
      // Derby
      else if (dataSourceType.equals(ConnectionManager.DERBY))
      {
         // Sample
         // SELECT `parent_id`, `name` FROM `child` WHERE '1' LIKE '%' ORDER BY `parent_id` ASC
         // OFFSET 0 ROWS FETCH NEXT 50
         
         if (sqlStatementString.indexOf("OFFSET") != -1)
            unLimitedSQLStatementString.append(sqlStatementString.substring(
               0, sqlStatementString.lastIndexOf("OFFSET")));
      }
      // MySQL, PostgreSQL, & SQLite.
      else
      {
         // Sample
         // SELECT `parent_id`, `name` FROM `child` WHERE '1' LIKE '%' ORDER BY `parent_id` ASC
         // LIMIT 50 OFFSET 0
         
         if (sqlStatementString.indexOf("LIMIT") != -1)
            unLimitedSQLStatementString.append(sqlStatementString.substring(
               0, sqlStatementString.lastIndexOf("LIMIT")));
      }
      
      trimmedString = (unLimitedSQLStatementString.toString()).trim();
      unLimitedSQLStatementString.delete(0, unLimitedSQLStatementString.length());
      unLimitedSQLStatementString.append(trimmedString);
      // System.out.println("MyJSQLView_Utils getUnlimitedSQLStatementString() "
      //                    + unLimitedSQLStatementString);
      
      return new String(unLimitedSQLStatementString);
   }
   
   //==============================================================
   // Method for providing a mechanism to process a given input
   // DATE data type search string and process into a manageable
   // format of the standard SQL Date format of YYYY-MM-DD. This
   // must handle partial date entries.
   //==============================================================
   
   public static String processDateFormatSearch(String searchString)
   {
      if (searchString == null)
         return null;
      
      if (searchString.indexOf("/") != -1)
         searchString = searchString.replaceAll("/", "-");
      
      // Short date either month, day, or year so just return it.
      if (searchString.indexOf("-") == -1)
      {
         if (searchString.length() == 3)
            searchString = MyJSQLView_Utils.convertCharMonthToDecimal(searchString);
         return searchString;
      }
      else
      {  
         // Looks like complete standard date so process according
         // to current selected date fromat.
         
         if (searchString.length() >= 10 && searchString.length() < 12)
            return convertViewDateString_To_DBDateString(searchString,
               DBTablesPanel.getGeneralDBProperties().getViewDateFormat());
         
         // Either (day and month) or (month and year) or some other
         // combination.
         else
         {
            String dateFormat, day, month, year;
            String[] dateContents = searchString.split("-");
            
            // Only one aspect of date given.
            if (dateContents.length == 1)
            {
               if (dateContents[0].length() == 3)
               {
                  dateContents[0] = MyJSQLView_Utils.convertCharMonthToDecimal(dateContents[0]);
                  if (searchString.indexOf("-") == 0)
                     searchString = "-" + dateContents[0];
                  else
                     searchString = dateContents[0] + "-";
               }
               return searchString;
            }
            
            dateFormat = DBTablesPanel.getGeneralDBProperties().getViewDateFormat();
            
            // yyyy-MM-dd
            if (dateFormat.equals(yyyyMMdd_DASH) || dateFormat.equals(yyyyMMdd_SLASH)
                || dateFormat.equals(yyyyMMMdd_DASH))
            {
               // Something with yyyy-MM-dd, but not standard format.
               if (dateContents.length == 3)
                  return dateContents[0] + "-" + dateContents[1] + "-" + dateContents[2];
               
               // yyyy-MM
               if (dateContents[0].length() == 4)
               {
                  year = dateContents[0];
                  month = dateContents[1];
                  
                  if (month.length() > 2)
                     month = MyJSQLView_Utils.convertCharMonthToDecimal(month);
                  
                  return year + "-" + month;
               }
               // MM-dd
               else
               {
                  month = dateContents[0];
                  day = dateContents[1];
                  
                  if (month.length() > 2)
                     month = MyJSQLView_Utils.convertCharMonthToDecimal(month);
                  
                  return month + "-" + day;
               }
            }
            
            // dd-MM-yyyy
            else if (dateFormat.equals(ddMMyyyy_DASH) || dateFormat.equals(ddMMyyyy_SLASH)
                  || dateFormat.equals(ddMMMyyyy_DASH))
            {
               // Something with dd-MM-yyyy, but not standard format.
               if (dateContents.length == 3)
                  return dateContents[2] + "-" + dateContents[1] + "-" + dateContents[0];
               
               // MM-yyyy
               if (dateContents[1].length() == 4)
               {
                  year = dateContents[1];
                  month = dateContents[0];
                  
                  if (month.length() > 2)
                     month = MyJSQLView_Utils.convertCharMonthToDecimal(month);
                  
                  return year + "-" + month;
               }
               // dd-MM
               else
               {
                  month = dateContents[1];
                  day = dateContents[0];
                  
                  if (month.length() > 2)
                     month = MyJSQLView_Utils.convertCharMonthToDecimal(month);
                  
                  return month + "-" + day;
               }
            }
            
            // MM-dd-yyyy
            else
            {  
               // Something with MM-dd-yyyy, but not standard format.
               if (dateContents.length == 3)
                  return dateContents[2] + "-" + dateContents[0] + "-" + dateContents[1];
               
               // dd-yyyy
               if (dateContents[1].length() == 4)
               {
                  year = dateContents[1];
                  day = dateContents[0];
                  return year + "-%-" + day;
               }
               // MM-dd
               else
               {
                  month = dateContents[0];
                  day = dateContents[1];
                  
                  if (month.length() > 2)
                     month = MyJSQLView_Utils.convertCharMonthToDecimal(month);
                  
                  return month + "-" + day;
               }
            }
         }
      }  
   }
   
   //==============================================================
   // Method for providing a mechanism to process a choosen file
   // through a JFileChooser to allow the querying of the user to
   // confirm overwriting an existing file.
   //==============================================================
   
   public static int processFileChooserSelection(JFrame parent, JFileChooser fileChooser)
   {
      // Method Instances.
      MyJSQLView_ResourceBundle resourceBundle;
      String fileName, fileSeparator;
      String iconsDirectory;
      String resource, resourceYes, resourceNo;
      File desiredFileName;
      int resultsOfFileChooser;
      ImageIcon deleteFileIcon;
      boolean operationCanceled;

      // Setting up some of the method instances.
      
      resourceBundle = MyJSQLView.getResourceBundle();
      
      fileSeparator = System.getProperty("file.separator");
      if (fileSeparator == null || fileSeparator.equals(""))
         fileSeparator = "/";

      iconsDirectory = "images" + fileSeparator + "icons" + fileSeparator;
      deleteFileIcon = resourceBundle.getResourceImage(iconsDirectory + "deleteFileIcon.gif");

      operationCanceled = false;
      resultsOfFileChooser = JFileChooser.CANCEL_OPTION;

      // Proceed to present the JFileChooser dialog to optain a
      // file to write to, or cancel saving.

      while (!operationCanceled)
      {
         resultsOfFileChooser = fileChooser.showSaveDialog(parent);

         if (resultsOfFileChooser == JFileChooser.APPROVE_OPTION)
         {
            // Get the file name.
            fileName = fileChooser.getSelectedFile().getName();
            fileName = fileChooser.getCurrentDirectory() + fileSeparator + fileName;

            // Looks like a file name selected, so check to see
            // it exists.
            if (!fileName.equals(""))
            {
               desiredFileName = new File(fileName);

               if (desiredFileName.exists())
               {
                  InputDialog importWarningDialog;
                  
                  JLabel message;
                  
                  resource = resourceBundle.getResourceString("MyJSQLView_Utils.message.FileExistsOverWrite",
                                                              "File Exists, Over Write?");
                  message = new JLabel(resource, JLabel.CENTER);
                  Object[] content = {message};

                  resource = resourceBundle.getResourceString("MyJSQLView_Utils.dialogtitle.SaveWarning",
                                                              "Save Warning");
                  resourceYes = resourceBundle.getResourceString("MyJSQLView_Utils.dialogbutton.Yes", "Yes");
                  resourceNo = resourceBundle.getResourceString("MyJSQLView_Utils.dialogbutton.No", "No");
                  
                  importWarningDialog = new InputDialog(null, resource, resourceYes, resourceNo, content,
                                                        deleteFileIcon);
                  importWarningDialog.pack();
                  importWarningDialog.center();
                  importWarningDialog.setResizable(false);
                  importWarningDialog.setVisible(true);

                  // If yes overwrite.
                  if (importWarningDialog.isActionResult())
                  {
                     operationCanceled = true;
                     resultsOfFileChooser = JFileChooser.APPROVE_OPTION;
                  }
                  // No, so loop.
                  else
                  {
                     operationCanceled = false;
                     resultsOfFileChooser = JFileChooser.CANCEL_OPTION;
                  }
               }
               else
               {
                  operationCanceled = true;
                  resultsOfFileChooser = JFileChooser.APPROVE_OPTION;
               }
            }
            else
            {
               operationCanceled = true;
               resultsOfFileChooser = JFileChooser.APPROVE_OPTION;
            }
         }
         else
         {
            operationCanceled = true;
            resultsOfFileChooser = JFileChooser.CANCEL_OPTION;
         }
      }
      return resultsOfFileChooser;
   }
   
   //==============================================================
   // Class method for allowing the setting of the language locale
   // if the program has not been run before and the MyJSQLView
   // users directory does not contain the file holding the setting.
   //==============================================================

   public static String processLocaleLanguage()
   {
      // Method Instances
      String localeString;
      String localeFileName;
      String localeFileDirectoryName;
      String localeFileString;
      String[] localeFileNames;
      String errorString;
      boolean fileError;

      File localeFile;
      File localeFileDirectory;
      File myjsqlviewDirectoryFile;
      FileReader fileReader;
      BufferedReader bufferedReader;

      ArrayList<String> localesData;
      JComboBox<Object> localeComboBox;
      ImageIcon localeIcon;
      InputDialog localeSelectDialog;
      int lastIndexOfDot;

      // Setup the instances and required data to start.

      localeString = "";
      localeFileName = "myjsqlview_locale.txt";
      localeFileDirectoryName = "locale";
      fileError = false;

      localeFileString = MyJSQLView_Utils.getMyJSQLViewDirectory() + MyJSQLView_Utils.getFileSeparator()
                         + localeFileName;

      localeFileDirectory = new File(localeFileDirectoryName);
      localeFile = new File(localeFileString);
      
      // Make the .myjsqlview director if does not exist.
      
      myjsqlviewDirectoryFile = new File(MyJSQLView_Utils.getMyJSQLViewDirectory());
      
      if (!myjsqlviewDirectoryFile.isDirectory())
      {
         try
         {
            fileError = !myjsqlviewDirectoryFile.mkdir();
         }
         catch (SecurityException se)
         {
            errorString = "File Error: Failed to create .myjsqlview directory.\n" + se;
            JOptionPane.showMessageDialog(null, errorString, "Alert", JOptionPane.ERROR_MESSAGE);
            return "";
         }
      }

      // Make a check to see if the locale file is present and if
      // so then load the language string and return. Otherwise
      // present a optionPane to allow the user to select a language
      // then save that data in locale file and return.

      try
      {  
         // Language not selected so allow the user to choose.
         if (!fileError && (localeFile.createNewFile() || localeFile.length() == 0))
         {
            // Collect the locale file names from the
            // locale directory.
            localeFileNames = localeFileDirectory.list();

            if (localeFileNames == null)
               return "";

            localesData = new ArrayList <String>();

            for (int i = 0; i < localeFileNames.length; i++)
            {
               lastIndexOfDot = localeFileNames[i].lastIndexOf(".");
               // System.out.println("MyJSQLView_Utils processLocaleLanguage() " + localeFileNames[i]);

               if (lastIndexOfDot > 0
                   && (localeFileNames[i].substring(lastIndexOfDot + 1).equals("properties")))
                  localesData.add(localeFileNames[i].substring((lastIndexOfDot - 5), lastIndexOfDot));
            }

            if (localesData.size() == 0)
               return "";

            // Ok looks like we have the supported language files so
            // present them to the user for selection.

            localeComboBox = new JComboBox<Object>(localesData.toArray());
            localeComboBox.setBorder(BorderFactory.createLoweredBevelBorder());

            Object[] content = {localeComboBox};

            // Do not use ResourceBundle Since Not Initialized.
            localeIcon = new ImageIcon(MyJSQLView_Utils.getIconsDirectory() + MyJSQLView_Utils.getFileSeparator()
                                       + "localeIcon.gif");

            localeSelectDialog = new InputDialog(null, "Language Selection", "ok", "cancel", content,
                                                 localeIcon);
            localeSelectDialog.pack();
            localeSelectDialog.center();
            localeSelectDialog.setResizable(false);
            localeSelectDialog.setVisible(true);
            
            // System.out.println("MyJSQLView_Utils processLocaleLanguage() " +
            //                    "File Does Not Exist, Creating.");  

            if (localeSelectDialog.isActionResult())
               localeString = (String) localeComboBox.getSelectedItem();
            else
               localeString = "en_US";
            
            WriteDataFile.mainWriteDataString(localeFileString, (localeString).getBytes(), false);
            localeSelectDialog.dispose();
         }

         // Language file present so load and return setting.
         else
         {
            fileReader = new FileReader(localeFileString);
            bufferedReader = new BufferedReader(fileReader);

            localeString = bufferedReader.readLine();

            bufferedReader.close();
            fileReader.close();

            // Make a few checks could be more stringent.
            if (localeString == null || localeString.length() != 5
                || localeString.indexOf("_") == -1)
               localeString = "";
         }
      }
      catch (IOException ioe)
      {
         errorString = "Failed to open/create myjsqlview_locale.txt File.\n";
         JOptionPane.showMessageDialog(null, errorString, "Alert", JOptionPane.ERROR_MESSAGE);
         localeString = "";
      }
      catch (SecurityException se)
      {
         errorString = "Failed to load language options form locale directory.\n";
         JOptionPane.showMessageDialog(null, errorString, "Alert", JOptionPane.ERROR_MESSAGE);
         localeString = "";
      }

      // System.out.println("MyJSQLView_Utils processLocaleLanguage() " + localeString);
      return localeString;
   }
   
   //==============================================================
   // Class method for allowing access to the MyJSQLView PDF table
   // dump thread class.
   //==============================================================

   public static void processTableData_To_PDFOutput(JTable summaryListTable, HashMap<String,
                                                    String> tableColumnTypeHashMap,
                                                    String exportedTable, String fileName)
   {
      if (summaryListTable != null)
      {  
         Thread pdfDataTableDumpThread = new Thread(new PDFDataTableDumpThread(summaryListTable,
            tableColumnTypeHashMap, exportedTable, fileName));
         
         pdfDataTableDumpThread.start();
      }
   }
   
   //==============================================================
   // Class method to set the Oracle sesion timezone.
   //==============================================================

   public static void setLocalTimeZone(Statement sqlStatement)
   {
      // Method Instances
      Calendar calendar;
      String timeZone;

      // Create a calendar and get the platforms timezone
      // to be used for setting the session timezone.

      calendar = Calendar.getInstance();
      // 1s/1000ms x 1min/60s x 1hr/60min
      timeZone = ((calendar.get(Calendar.ZONE_OFFSET)) / (60 * 60 * 1000)) + ":00";
      // System.out.println("MyJSQLView_Utils setLocalTimeZone() " + timeZone);

      try
      {
         sqlStatement.executeUpdate("ALTER SESSION SET TIME_ZONE = '" + timeZone + "'");
      }
      catch (SQLException e)
      {
         ConnectionManager.displaySQLErrors(e, "MyJSQLView_Utils setLocalTimeZone()");
         return;
      }
   }
   
   //==============================================================
   // Class method to set update the UIManager font size.
   //==============================================================

   public static void setUIManagerFont(int fontSize)
   {
      // Method Instances
      Object uiObject;
      Font uiManagerFont;
      UIDefaults uiDefaults;
      
      // Setup
      uiObject = null;
      uiObject = UIManager.get("Label.font");
      
      if (uiObject != null && uiObject instanceof Font)
         uiManagerFont = (Font) uiObject;
      else
         return;
      
      if (uiManagerFont.getSize() == fontSize)
         return;
      
      // Collect the UI Manager keys that are fonts
      // and update them to the new font size.
      
      uiDefaults = UIManager.getLookAndFeelDefaults();
      Set<Object> hash = uiDefaults.keySet();
      Iterator<Object> iterator = hash.iterator();
      
      while (iterator.hasNext())
      {
         Object curObj = iterator.next();
         // System.out.println("MyJSQLView_Utils setUIManager() " + curObj.toString());
         
         if (curObj.toString().indexOf("font") != -1)
         {
            // System.out.println("MyJSQLView_Utils setUIManager() " + curObj);
            UIManager.put(curObj, new FontUIResource(uiManagerFont.getFontName(),
                                                     uiManagerFont.getStyle(), fontSize));
         }
      }
   }
   
   //==============================================================
   // Method for converting an input byte array either extracting
   // or dumping per a determined conversion definition.
   //==============================================================
   
   public static String stateConvert(byte[] bytesToProcess, boolean in)
   {
      // Method Instances
      int b;
      String base10_1, base10_10;
      StringBuffer convertedString;
      String hexadecimalString;
      BufferedInputStream inputStream;

      // Otain byes in a stream and convert to
      // hex for extraction/dumping.

      if (bytesToProcess != null)
      {
         convertedString = new StringBuffer();
         inputStream = new BufferedInputStream(new ByteArrayInputStream(bytesToProcess));

         if (in)
            convertedString.append("");
         else
            convertedString.append("0x");

         try
         {
            int skipBytes = 1;

            while ((b = inputStream.read()) != -1)
            {
               if (in)
               {
                  if (skipBytes++ > 2)
                  {
                     // Extract
                     try
                     {
                        base10_10 = (char) b + "";
                        base10_1 = (char) inputStream.read() + "";

                        int b10 = (Integer.valueOf(base10_10, 16).intValue()) * 16;
                        int b1 = (Integer.valueOf(base10_1, 16).intValue());
                        convertedString.append((char) (b10 + b1));
                     }
                     catch (NumberFormatException e)
                     {
                        String msg = "Unable to Decode Input State File Format.";
                        JOptionPane.showMessageDialog(null, msg, "Alert", JOptionPane.ERROR_MESSAGE);
                        inputStream.close();
                        return "";
                     }
                  }
               }
               else
               {
                  // Dump.
                  hexadecimalString = Integer.toString(b, 16);

                  if (hexadecimalString.length() < 2)
                     hexadecimalString = "0" + hexadecimalString;
                  if (hexadecimalString.length() > 2)
                     hexadecimalString = hexadecimalString.substring(hexadecimalString.length() - 2);
                  convertedString.append(hexadecimalString);
               }
            }
            inputStream.close();
         }
         catch (IOException e)
         {
            String msg = "I/O BufferedInputStream Failure.";
            JOptionPane.showMessageDialog(null, msg, "Alert", JOptionPane.ERROR_MESSAGE);
            return "";
         }
         return convertedString.toString();
      }
      else
         return "";
   }
   
   //==============================================================
   // Method for performing the n-digit chopping operation on an
   // input number.
   //==============================================================

   public static double nDigitChop(double numberToChop, int n)
   {
      // Method Instances
      int decimal;
      int chop = n;
      String pass_string;

      pass_string = Double.toString(numberToChop);
      decimal = pass_string.indexOf(".");
      if (decimal != -1 & pass_string.length() >= decimal + chop + 1)
      {
         numberToChop = (Double.valueOf(pass_string.substring(0, decimal + chop + 1))).doubleValue();
      }
      else
         numberToChop = Double.valueOf(pass_string).doubleValue();

      return numberToChop;
   }
}
