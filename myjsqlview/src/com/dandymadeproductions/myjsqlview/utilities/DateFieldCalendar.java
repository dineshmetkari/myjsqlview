//=================================================================
//              MyJSQLView DateFieldCalendar Frame.
//=================================================================
//    This class provides a frame for creating a calendar to be
// used in selecting a date for a field in the TableEntryForm.
//
//                     << DateFieldCalendar.java >>
//
//=================================================================
// Copyright (C) 2005-2015 Dana M. Proctor
// Version 3.5 10/20/2014
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
// Version 1.0 05/22/2007 Original MyJSQLView DateFieldCalendar Class.
//         1.1 05/29/2007 Cleaned Out Some Instances That Should Have
//             			  Not Been Here from the Outlining Class.
//         1.2 06/15/2007 Initial GUI Layout.
//         1.3 06/16/2007 Completed GUI Major Components and Funtionality
//                        to Change Year and Month Aspects for Date.
//                        Code to Update EntryForm Implemented.
//         1.4 06/16/2007 Completed GUI, Calendar, and All Functionality.
//         1.5 06/17/2007 Cleaned Up Some. Comments & Higlight Color.
//         1.6 09/09/2007 Commented Out Some Debug Instances in Class
//                        Method updateCalendarData().
//         1.7 09/19/2007 Used System.getProperty("file.separator") for
//                        All File System Resources Accesses Through
//                        Instance fileSeparator.
//         1.8 12/12/2007 Header Update.
//         1.9 05/14/2008 Added Class Instance serialVersionUID. Declared
//                        WindowListener calendarFrameListener private
//                        transient.
//         2.0 10/23/2008 MyJSQLView Project Common Source Code Formatting.
//         2.1 10/25/2008 Add Class Instance iconsDirectory.
//         2.2 05/27/2009 Header Format Changes/Update.
//         2.3 10/25/2009 Removed Constructor Instance fileSeparator. Obtained
//                        iconsDirectory From MyJSQLView_Utils Class.
//         2.4 10/25/2009 Added fileSeparator to iconsDirectory.
//         2.5 02/18/2010 Changed Package to Reflect Dandy Made Productions Code.
//         2.6 03/01/2010 Added Class Instance resourceBundle to Implement Basic
//                        Internationalization. Does NOT Address Date Formats.
//                        Added Constructor resource Instance and Method
//                        showAlertDialog().
//         2.7 03/03/2010 Correction in Constructor to Include resource for okButton,
//                        & cancelButton.
//         2.8 01/11/2011 Class Methods actionPerformed() & createInitialCalendarDate()
//                        Changes to Reflect the Formatting of Dates According to
//                        the Selected General Date Preferences.
//         2.9 01/01/2012 Copyright Update.
//         3.0 07/08/2012 Changes in Way MyJSQLView_ResourceBundle Handles the Collection
//                        of Resource Strings. Change to resource.getResourceString(key,
//                        default).
//         3.1 08/06/2012 MyJSQLView Class Method Change of getLocaleResourceBundle()
//                        to getResourceBundle().
//         3.2 08/19/2012 Collection of All Image Resources Through resourceBundle.
//         3.3 09/11/2012 Changed Package Name to com.dandymadeproductions.myjsqlview.utilities.
//                        Made Class, Constructor, & center() Method Public.
//         3.4 07/02/2013 Change in actionPerformed() & createInitialCalendarDate() to
//                        Use DBTablePanel.getGeneralDBProperties().
//         3.5 10/20/2014 Parameterized Class Instance monthComboBox to Conform to JRE 7.
//
//-----------------------------------------------------------------
//                    danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview.utilities;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Date;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.dandymadeproductions.myjsqlview.MyJSQLView;
import com.dandymadeproductions.myjsqlview.gui.forms.TableEntryForm;
import com.dandymadeproductions.myjsqlview.gui.panels.DBTablesPanel;

/**
 *    The DateFieldCalendar class provides a frame for creating a
 * calendar to be used for selecting a date for a field in the
 * TableEntryForm.
 * 
 * @author Dana M. Proctor
 * @version 3.5 10/20/2014
 */

public class DateFieldCalendar extends JFrame implements ActionListener, KeyListener, MouseListener
{
   // =============================================
   // Creation of the necessary class instance
   // variables for the JFrame.
   // =============================================

   private static final long serialVersionUID = -3871275960307705000L;

   private TableEntryForm callingForm;
   private Object columnName;
   private String columnType;
   private MyJSQLView_ResourceBundle resourceBundle;

   private JButton previousYearButton, nextYearButton;
   private JButton previousMonthButton, nextMonthButton;
   private JComboBox<String> monthComboBox;
   private JTextField yearTextField;
   private JLabel dateSelectionLabel;
   private JPanel centerPanel, daysPanel;
   private JLabel[][] dayLabel = new JLabel[7][6];

   private Calendar calendar;
   private String timeString;
   private Object dateSelection;

   private int month, day, year;
   private int daysInMonth;
   private int firstDayOfMonth;
   private String[] months = {"January", "February", "March", "April", "May",
                              "June", "July", "August", "September", "October",
                              "November", "December"};

   private JButton okButton, cancelButton;

   //==============================================================
   // DateFieldCalendar Constructor
   //==============================================================

   public DateFieldCalendar(TableEntryForm callingForm, Object columnName, String columnType)
   {
      this.callingForm = callingForm;
      this.columnName = columnName;
      this.columnType = columnType;

      // Constructor Instances.
      JPanel mainPanel;
      JPanel northPanel, southButtonPanel;

      JPanel daysOfWeekPanel;
      String iconsDirectory, resource;
      JLabel[] daysOfWeekLabel = new JLabel[7];
      String[] days = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
      Dimension yearComponentDimension;
      Font dateLabelFont;

      // Setting up a icons directory  & other instances.
      iconsDirectory = MyJSQLView_Utils.getIconsDirectory() + MyJSQLView_Utils.getFileSeparator();
      resourceBundle = MyJSQLView.getResourceBundle();

      // Setting the frame's main layout & other required
      // fields.
      resource = resourceBundle.getResourceString("DateFieldCalendar.title.DateSelection",
                                                  "Date Selection");
      setTitle(resource);
      mainPanel = new JPanel(new BorderLayout());
      mainPanel.setBorder(BorderFactory.createRaisedBevelBorder());

      // Setting up a bold Date Label font.
      if (this.getFont() != null)
      {
         String fontName = this.getFont().getFontName();
         int fontSize = this.getFont().getSize();
         dateLabelFont = new Font(fontName, Font.BOLD, fontSize);
      }
      else
         dateLabelFont = new Font("Serif", Font.BOLD, 12);

      // =================================
      // North panel and components

      northPanel = new JPanel();
      northPanel.setBorder(BorderFactory.createEtchedBorder());

      previousYearButton = new JButton(resourceBundle.getResourceImage(iconsDirectory 
                                                                       + "previousYearIcon.png"));
      previousYearButton.setMargin(new Insets(0, 0, 0, 0));
      previousYearButton.setBorder(BorderFactory.createLoweredBevelBorder());
      previousYearButton.setToolTipText("Previous Year");
      previousYearButton.addActionListener(this);
      northPanel.add(previousYearButton);

      previousMonthButton = new JButton(resourceBundle.getResourceImage(iconsDirectory 
                                                                        + "previousMonthIcon.png"));
      previousMonthButton.setMargin(new Insets(0, 0, 0, 0));
      previousMonthButton.setBorder(BorderFactory.createLoweredBevelBorder());
      previousMonthButton.setToolTipText("Previous Month");
      previousMonthButton.addActionListener(this);
      northPanel.add(previousMonthButton);

      monthComboBox = new JComboBox<String>(months);
      northPanel.add(monthComboBox);
      monthComboBox.addActionListener(this);

      yearTextField = new JTextField(4);
      yearComponentDimension = monthComboBox.getPreferredSize();
      yearComponentDimension.width = monthComboBox.getPreferredSize().width - 40;
      yearTextField.setPreferredSize(yearComponentDimension);
      yearTextField.addKeyListener(this);
      northPanel.add(yearTextField);

      nextMonthButton = new JButton(resourceBundle.getResourceImage(iconsDirectory 
                                                                    + "nextMonthIcon.png"));
      nextMonthButton.setMargin(new Insets(0, 0, 0, 0));
      nextMonthButton.setBorder(BorderFactory.createRaisedBevelBorder());
      nextMonthButton.setToolTipText("Next Month");
      nextMonthButton.addActionListener(this);
      northPanel.add(nextMonthButton);

      nextYearButton = new JButton(resourceBundle.getResourceImage(iconsDirectory 
                                                                   + "nextYearIcon.png"));
      nextYearButton.setMargin(new Insets(0, 0, 0, 0));
      nextYearButton.setBorder(BorderFactory.createRaisedBevelBorder());
      nextYearButton.setToolTipText("Next Year");
      nextYearButton.addActionListener(this);
      northPanel.add(nextYearButton);

      mainPanel.add(northPanel, BorderLayout.NORTH);

      // =================================
      // Center Calendar panel

      centerPanel = new JPanel(new BorderLayout());

      // Setting up the calendar
      createInitialCalendarDate();

      // Create Calendar Components
      daysOfWeekPanel = new JPanel(new GridLayout(1, 7, 0, 0));
      daysOfWeekPanel.setBorder(BorderFactory.createLoweredBevelBorder());

      // Days of Weeks Labels
      for (int i = 0; i < days.length; i++)
      {
         daysOfWeekLabel[i] = new JLabel(days[i], JLabel.CENTER);
         daysOfWeekLabel[i].setFont(dateLabelFont);
         // daysOfWeekLabel[i].setPreferredSize(new Dimension(10,10));
         daysOfWeekLabel[i].setBorder(null);
         daysOfWeekPanel.add(daysOfWeekLabel[i]);
      }
      centerPanel.add(daysOfWeekPanel, BorderLayout.NORTH);

      // Calendar Days
      daysPanel = new JPanel(new GridLayout(6, 7, 0, 0));
      daysPanel.setBorder(BorderFactory.createLoweredBevelBorder());
      createCalendar(true);
      centerPanel.add(daysPanel, BorderLayout.CENTER);

      mainPanel.add(centerPanel, BorderLayout.CENTER);

      // =================================
      // South panel and action buttons

      southButtonPanel = new JPanel();
      southButtonPanel.setBorder(BorderFactory.createEtchedBorder());

      resource = resourceBundle.getResourceString("DateFieldCalendar.button.OK", "OK");
      okButton = new JButton(resource);
      okButton.setFocusPainted(false);
      okButton.setMargin(new Insets(0, 0, 0, 0));
      okButton.addActionListener(this);
      southButtonPanel.add(okButton);

      dateSelectionLabel = new JLabel(dateSelection + "", JLabel.CENTER);
      dateSelectionLabel.setFont(dateLabelFont);
      dateSelectionLabel.setPreferredSize(monthComboBox.getPreferredSize());
      dateSelectionLabel.setBorder(BorderFactory.createLoweredBevelBorder());
      southButtonPanel.add(dateSelectionLabel);

      resource = resourceBundle.getResourceString("DateFieldCalendar.button.Cancel", "Cancel");
      cancelButton = new JButton(resource);
      cancelButton.setFocusPainted(false);
      cancelButton.setMargin(new Insets(0, 0, 0, 0));
      cancelButton.addActionListener(this);
      southButtonPanel.add(cancelButton);

      mainPanel.add(southButtonPanel, BorderLayout.SOUTH);

      getContentPane().add(mainPanel);
      this.addWindowListener(calendarFrameListener);
   }

   //==============================================================
   // WindowListener for insuring that the frame is closed. (x).
   //==============================================================

   private transient WindowListener calendarFrameListener = new WindowAdapter()
   {
      public void windowClosing(WindowEvent e)
      {
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

      // Class Instances
      int monthIndex, yearValue;
      String dateString; // MM-DD-YYYY
      Date dateValue; // YYYY-MM-DD

      // Action buttons.
      if (frameSource instanceof JButton)
      {
         // Previous & Next Year Actions
         if (frameSource == previousYearButton | frameSource == nextYearButton)
         {
            try
            {
               yearValue = Integer.parseInt((String) yearTextField.getText());
               if (frameSource == previousYearButton)
                  yearValue--;
               else
                  yearValue++;

               // Set components and date data.
               yearTextField.setText(yearValue + "");
               dateString = createDateString((month + 1), day, yearValue);
               dateSelectionLabel.setText(dateString);

               dateValue = java.sql.Date.valueOf(formatJavaDateString(dateString));
               calendar.setTime(dateValue);
               updateCalendarData();
               createCalendar(false);
            }
            catch (NumberFormatException e)
            {
               showAlertDialog();
            }
         }

         // Previous Month Action
         if (frameSource == previousMonthButton | frameSource == nextMonthButton)
         {
            // Collect the current month and take
            // action to change.
            monthComboBox.removeActionListener(this);
            monthIndex = monthComboBox.getSelectedIndex();

            if (frameSource == previousMonthButton)
            {
               if (monthIndex == 0)
                  monthComboBox.setSelectedIndex(months.length - 1);
               else
                  monthComboBox.setSelectedIndex(monthIndex - 1);
            }
            else
            {
               if (monthIndex == months.length - 1)
                  monthComboBox.setSelectedIndex(0);
               else
                  monthComboBox.setSelectedIndex(monthIndex + 1);
            }

            // Set components and date data.
            monthIndex = monthComboBox.getSelectedIndex();
            dateString = createDateString((monthIndex + 1), day, year);
            dateSelectionLabel.setText(dateString);

            dateValue = java.sql.Date.valueOf(formatJavaDateString(dateString));
            calendar.setTime(dateValue);
            updateCalendarData();
            createCalendar(false);
            monthComboBox.addActionListener(this);
         }

         // OK Button Action.
         if (frameSource == okButton)
         {
            Object formattedDateSelection;
            
            dateSelection = dateSelectionLabel.getText() + timeString;
            
            formattedDateSelection = MyJSQLView_Utils.convertViewDateString_To_DBDateString(
               dateSelectionLabel.getText(), "MM-dd-yyyy");
            formattedDateSelection = MyJSQLView_Utils.convertDBDateString_To_ViewDateString(
               formattedDateSelection + "", DBTablesPanel.getGeneralDBProperties().getViewDateFormat());
            formattedDateSelection = formattedDateSelection + timeString;
            
            callingForm.setFormField(columnName, formattedDateSelection);
            this.dispose();
         }

         // Cancel Buttton Action.
         else if (frameSource == cancelButton)
         {
            this.dispose();
         }
      }

      // ComboBox Actions
      if (frameSource instanceof JComboBox)
      {
         // Month ComboBox
         if (frameSource == monthComboBox & dateSelectionLabel != null)
         {
            // Collect the current month and take
            // action to change.
            monthIndex = monthComboBox.getSelectedIndex();
            dateString = createDateString((monthIndex + 1), day, year);
            dateSelectionLabel.setText(dateString);

            dateValue = java.sql.Date.valueOf(formatJavaDateString(dateString));
            calendar.setTime(dateValue);
            updateCalendarData();
            createCalendar(false);
         }
      }
   }

   //==============================================================
   // KeyEvent Listener method for detected key pressed events to
   // full fill KeyListener Interface requirements.
   //==============================================================

   public void keyPressed(KeyEvent evt)
   {
      // Do Nothing
   }

   //==============================================================
   // KeyEvent Listener method for detecting key released events
   // to full fill KeyListener Interface requirements.
   //==============================================================

   public void keyReleased(KeyEvent evt)
   {
      // Do Nothing
   }

   //==============================================================
   // KeyEvent Listener method for detecting key pressed event,
   // Enter, to be used with the year text field.
   //==============================================================

   public void keyTyped(KeyEvent evt)
   {
      // Class Method Instances
      char keyChar;
      int yearValue;
      String dateString;
      Date dateValue;

      // Derived from the searchTextField.
      keyChar = evt.getKeyChar();

      // Fire the search button as required.
      if (keyChar == KeyEvent.VK_ENTER)
      {
         try
         {
            yearValue = Integer.parseInt((String) yearTextField.getText());

            // Set date data.
            dateString = createDateString((month + 1), day, yearValue);
            dateSelectionLabel.setText(dateString);

            try
            {
               dateValue = java.sql.Date.valueOf(formatJavaDateString(dateString));
               calendar.setTime(dateValue);
               updateCalendarData();
               createCalendar(false);
            }
            catch (IllegalArgumentException e)
            {
               Integer.parseInt("error");
            }
         }
         catch (NumberFormatException e)
         {
            showAlertDialog();
         }
      }
   }

   //==============================================================
   // MouseEvent Listener method for detecting the user clicking
   // the mouse within center panel.
   //==============================================================

   public void mouseClicked(MouseEvent evt)
   {
      // Class Method Instances
      Object frameSource;
      String dateString;
      Date dateValue;

      // Collecting action object.
      frameSource = evt.getSource();

      if (frameSource instanceof JLabel)
      {
         // Setting the new day if one is selected
         // and calendar data.

         if (!((JLabel) frameSource).getText().equals(" "))
         {
            day = Integer.parseInt(((JLabel) frameSource).getText());

            dateString = createDateString((month + 1), day, year);
            dateSelectionLabel.setText(dateString);

            dateValue = java.sql.Date.valueOf(formatJavaDateString(dateString));
            calendar.setTime(dateValue);

            // Focus new day.
            for (int i = 0; i < dayLabel.length; i++)
               for (int j = 0; j < dayLabel[0].length; j++)
                  dayLabel[i][j].setBorder(null);

            ((JLabel) frameSource).setBorder(BorderFactory.createEtchedBorder());
         }
      }
   }

   //==============================================================
   // Mouse Event Listener methods for detecting aspects of mouse
   // actions to fulfill MouseListener Interface requirements.
   //==============================================================

   public void mouseEntered(MouseEvent evt)
   {
      // Do Nothing.
   }

   public void mouseExited(MouseEvent evt)
   {
      // Do Nothing.
   }

   public void mousePressed(MouseEvent evt)
   {
      // Do Nothing.
   }

   public void mouseReleased(MouseEvent evt)
   {
      // Don Nothing.
   }

   //==============================================================
   // Method to create a valid date given the input form fields
   // value. The method also sets up the calendar to be used and
   // necessary values, month, day, & year for tracking changes.
   //==============================================================

   private void createInitialCalendarDate()
   {
      // Class Method Instances
      Date dateValue;
      String dateString, formFieldEntry;

      // Setting up the Calendar Instance.
      calendar = Calendar.getInstance();
      formFieldEntry = callingForm.getFormField((String) columnName);
      dateString = "";
      timeString = "";

      // Try to obtain a valid date given the input
      // from the form field.
      try
      {
         // Date
         if (columnType.equals("DATE"))
         {
            // Check for some kind of valid input.
            if (!(formFieldEntry.length() >= 10 && formFieldEntry.length() < 12))
               java.sql.Date.valueOf("error");
            else
            {
               dateString = MyJSQLView_Utils.convertViewDateString_To_DBDateString(formFieldEntry,
                  DBTablesPanel.getGeneralDBProperties().getViewDateFormat());
               dateValue = java.sql.Date.valueOf(dateString);
               
               dateString = MyJSQLView_Utils.convertDBDateString_To_ViewDateString(dateString, "MM-dd-yyyy");
               timeString = "";
               
               calendar.setTime(dateValue);
            }
         }
         // DateTime
         else
         {
            // Check for some kind of valid input.
            if (formFieldEntry.indexOf(" ") == -1 | formFieldEntry.length() < 10)
               java.sql.Date.valueOf("error");
            else
            {
               dateString = formFieldEntry.substring(0, formFieldEntry.indexOf(" "));
               dateString = MyJSQLView_Utils.convertViewDateString_To_DBDateString(dateString,
                  DBTablesPanel.getGeneralDBProperties().getViewDateFormat());
               timeString = formFieldEntry.substring(formFieldEntry.indexOf(" "));
               timeString = timeString.trim();
               if (timeString.length() > 8)
                  timeString = timeString.substring(0, 8);
               timeString = " " + timeString;
               dateValue = java.sql.Date.valueOf(dateString);
               
               dateString = MyJSQLView_Utils.convertDBDateString_To_ViewDateString(dateString, "MM-dd-yyyy");
               
               calendar.setTime(dateValue);
            }
         }
      }
      // Something wrong with the input date or add form
      // possibly so set date.
      catch (IllegalArgumentException e)
      {
         // Invalid Input so set to current date
         dateString = createDateString((calendar.get(Calendar.MONTH) + 1), calendar
               .get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.YEAR));

         // Create the timeString as needed.
         if (columnType.equals("DATE"))
            timeString = "";
         else
         {
            if (calendar.get(Calendar.HOUR) < 10)
               timeString = "0" + calendar.get(Calendar.HOUR) + ":";
            else
               timeString = calendar.get(Calendar.HOUR) + ":";

            if (calendar.get(Calendar.MINUTE) < 10)
               timeString = timeString + "0" + calendar.get(Calendar.MINUTE) + ":";
            else
               timeString = timeString + calendar.get(Calendar.MINUTE) + ":";

            if (calendar.get(Calendar.SECOND) < 10)
               timeString = timeString + "0" + calendar.get(Calendar.SECOND);
            else
               timeString = " " + timeString + calendar.get(Calendar.SECOND);
         }
      }
      dateSelection = dateString;
      updateCalendarData();
   }

   //==============================================================
   // Class method for converting a standard Java date string into
   // a java.sql.date string. MM-dd-YYYY to YYYY-MM-dd.
   //==============================================================

   private String formatJavaDateString(String displayString)
   {
      // Class Method Instances
      String javaDateString, year, monthDay;
      int dashIndex, lastDashIndex;

      // Format
      javaDateString = displayString.trim();
      dashIndex = javaDateString.indexOf("-");
      lastDashIndex = javaDateString.lastIndexOf("-");

      if ((lastDashIndex != -1) & (dashIndex >= 1 & dashIndex <= 2)
          & ((lastDashIndex - dashIndex >= 1) & (lastDashIndex - dashIndex <= 3)))
      {
         year = javaDateString.substring(javaDateString.lastIndexOf("-") + 1);
         monthDay = javaDateString.substring(0, javaDateString.lastIndexOf("-"));
         return year + "-" + monthDay;
      }
      else
         return "";
   }

   //==============================================================
   // Class method to set the calendar values month, day, & year
   // in the correct format MM-DD-YYYY.
   //==============================================================

   private String createDateString(int inputMonth, int inputDay, int inputYear)
   {
      // Class Method Instances
      String dateString = "";

      // Month
      if (inputMonth < 10)
         dateString = "0" + inputMonth + "-";
      else
         dateString = inputMonth + "-";

      // Day
      if (inputDay < 10)
         dateString = dateString + "0" + inputDay + "-";
      else
         dateString = dateString + inputDay + "-";

      // Year
      return dateString = dateString + inputYear;
   }

   //==============================================================
   // Class method to create the visual calendar to be displayed
   // for selecting days. Based on calendar month & year.
   //==============================================================

   private void createCalendar(boolean initialCalendar)
   {
      // Class Instances
      int dayValue;
      int currentIndex;

      // Creating the calendar
      currentIndex = 1;

      for (int i = 0; i < dayLabel.length; i++)
         for (int j = 0; j < dayLabel[0].length; j++)
         {
            dayValue = currentIndex - firstDayOfMonth + 1;

            if (initialCalendar)
               dayLabel[i][j] = new JLabel();

            // Blank days in month grid.
            if (currentIndex < firstDayOfMonth | dayValue > daysInMonth)
            {
               dayLabel[i][j].setText(" ");
               dayLabel[i][j].removeMouseListener(this);
            }
            // Valid days in month grid.
            else
            {
               dayLabel[i][j].setText(dayValue + "");
               dayLabel[i][j].addMouseListener(this);
            }

            // Hightlight the current day.
            if (dayValue == day)
               dayLabel[i][j].setBorder(BorderFactory.createEtchedBorder());
            else
               dayLabel[i][j].setBorder(null);

            // Intial setup stuff
            if (initialCalendar)
            {
               dayLabel[i][j].setHorizontalAlignment(JLabel.CENTER);
               dayLabel[i][j].setBackground(new Color(0, 255, 0));
               daysPanel.add(dayLabel[i][j]);
            }
            currentIndex++;
         }
   }

   //==============================================================
   // Method to obtain the current Calendar Data.
   //==============================================================

   private void updateCalendarData()
   {
      // Debug Class Method Instances
      // String monthYearString;
      // int dayOfWeek;

      // Set the Calendar Data
      year = calendar.get(Calendar.YEAR);
      month = calendar.get(Calendar.MONTH);
      day = calendar.get(Calendar.DAY_OF_MONTH);
      // System.out.println(months[month] + " " + day + ", " + year);

      // monthYearString = months[month] + " " + year;
      // dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
      // System.out.println("Day of Week: " + dayOfWeek + " " +
      // monthYearString);

      daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
      // System.out.println("Days in Month: " + daysInMonth);

      Calendar calendar_1stDay = Calendar.getInstance();
      calendar_1stDay.set(year, month, 1);

      firstDayOfMonth = calendar_1stDay.get(Calendar.DAY_OF_WEEK);
      // System.out.println("First Day of Month: " + firstDayOfMonth);

      // Set the Components
      monthComboBox.setSelectedIndex(month);
      yearTextField.setText(year + "");
   }
   
   //==============================================================
   // Method to show an alert dialog indicating an error in the
   // input date. At this time only manual entry is the year. So
   // this routine gives the error message for invalid year input.
   //==============================================================

   private void showAlertDialog()
   {
      // Method Instances.
      String message, title;
      
      title = resourceBundle.getResourceString("DateFieldCalendar.dialogtitle.Alert", "Alert");
      message = resourceBundle.getResourceString("DateFieldCalendar.dialogmessage.InvalidYear",
                                                  "Invalid Input for Year");
      
      JOptionPane.showMessageDialog(null, message + " ", title, JOptionPane.ERROR_MESSAGE);
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
