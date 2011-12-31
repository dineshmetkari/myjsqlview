//=================================================================
//         MyJSQLView Standard Parameters Panel
//=================================================================
//
// 	This class provides a panel that is used to hold the
// MyJSQLView standard parameters components used in the
// MyJSQLView_Access and ConnectionManager classes.
//
//            << StandardParametersPanel.java >>
//
//=================================================================
// Copyright (C) 2006-2012 Dana M. Proctor
// Version 2.7 01/01/2012
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
// Version 1.0 Original StandardParametersPanel Class.
//         1.1 Removed JFrame Argument From Constructor.
//         1.2 Changed get/setPassword to char[].
//         1.3 Removed Implements Cloneable.
//         1.4 Removed Class Instances hostList, databaseList,
//             & userList That Were Used Only Locally in the
//             Constructor.
//         1.5 Header Update.
//         1.6 Added Class Instance serialVersionUID.
//         1.7 MyJSQLView Project Common Source Code Formatting.
//         1.8 Header Format Changes/Update.
//         1.9 Class Method setPassword() Instance contentString Changed
//             to StringBuffer.
//         2.0 Class Method setPassword() Conditional to Properly Set the
//             passwordTextField to an Empty String if Only Space(s).
//         2.1 Changed Package to Reflect Dandy Made Productions Code.
//         2.2 Constructor Argument resourceBundle Added and Constructor
//             Instance resource. Implementation of Internationalization.
//         2.3 Parameterized Vector Arguments hostList, databaseList,
//             & userList to Bring Code Into Compliance With Java 5.0 API.
//             Also the Same for Argument content in Class Methods setHost(),
//             setDatabase(), and setUser().
//         2.4 Minor Format Changes.
//         2.5 Parameterized Instance contentsIterator in CLass Methods
//             setHost(), setDatabaseItem(), & setUser().
//         2.6 Copyright Update.
//         2.7 Copyright Update.
//        
//-----------------------------------------------------------------
//                 danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview;

import java.awt.GridLayout;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

/**
 *    The StandardParametersPanel class provides a panel that is
 * used to hold the MyJSQLView standard parameters components used
 * in the MyJSQLView_Access and ConnectionManager classes.
 * 
 * @author Dana M. Proctor
 * @version 2.7 01/01/2012
 */

class StandardParametersPanel extends JPanel
{
   // Class Instances.
   private static final long serialVersionUID = -3737536982828473966L;

   private JComboBox hostJComboBox;
   private JComboBox dbJComboBox;
   private JComboBox userJComboBox;
   private JPasswordField passwordTextField;

   //==============================================================
   // StandardParametersPanel Constructor
   //==============================================================

   protected StandardParametersPanel(MyJSQLView_ResourceBundle resourceBundle, Vector<String> hostList,
                                     Vector<String> databaseList, Vector<String> userList)
   {
      // Constructor Instances.
      JLabel hostLabel, dbLabel, userLabel, passwordLabel;
      String resource;
      
      // Standard Parameters Panel & Components
      setLayout(new GridLayout(8, 1, 100, 0));

      // Host
      resource = resourceBundle.getResource("StandardParametersPanel.label.Host");
      if (resource.equals(""))
         hostLabel = new JLabel("Host", JLabel.LEFT);
      else
         hostLabel = new JLabel(resource, JLabel.LEFT); 
      add(hostLabel);

      hostJComboBox = new JComboBox(hostList);
      hostJComboBox.setEditable(true);
      hostJComboBox.setBounds(0, 0, 40, 12);
      hostJComboBox.addItem("");
      add(hostJComboBox);

      // Database
      resource = resourceBundle.getResource("StandardParametersPanel.label.Database");
      if (resource.equals(""))
         dbLabel = new JLabel("Database");
      else
         dbLabel = new JLabel(resource);
      add(dbLabel);

      dbJComboBox = new JComboBox(databaseList);
      dbJComboBox.setEditable(true);
      dbJComboBox.setBounds(0, 0, 40, 12);
      dbJComboBox.addItem("");
      add(dbJComboBox);

      // User
      resource = resourceBundle.getResource("StandardParametersPanel.label.User");
      if (resource.equals(""))
         userLabel = new JLabel("User", JLabel.LEFT);
      else
         userLabel = new JLabel(resource, JLabel.LEFT);
      add(userLabel);

      userJComboBox = new JComboBox(userList);
      userJComboBox.setEditable(true);
      userJComboBox.setBounds(0, 0, 40, 12);
      userJComboBox.addItem("");
      add(userJComboBox);

      // Password
      resource = resourceBundle.getResource("StandardParametersPanel.label.Password");
      if (resource.equals(""))
         passwordLabel = new JLabel("Password", JLabel.LEFT);
      else
         passwordLabel = new JLabel(resource, JLabel.LEFT);
      add(passwordLabel);

      passwordTextField = new JPasswordField();
      add(passwordTextField);
   }

   //==============================================================
   // Class methods to get the various panels' components content.
   //==============================================================

   protected String getHost()
   {
      return (String) hostJComboBox.getSelectedItem();
   }

   protected String getDataBase()
   {
      return (String) dbJComboBox.getSelectedItem();
   }

   protected String getUser()
   {
      return (String) userJComboBox.getSelectedItem();
   }

   protected char[] getPassword()
   {
      return passwordTextField.getPassword();
   }

   //==============================================================
   // Class methods to set hosts combobox contents or item.
   //==============================================================

   protected void setHost(String content)
   {
      hostJComboBox.setSelectedItem(content);
   }

   protected void setHost(Vector<String> content)
   {
      hostJComboBox.removeAllItems();

      Iterator<String> contentsIterator = content.iterator();
      
      while (contentsIterator.hasNext())
         hostJComboBox.addItem(contentsIterator.next());
   }

   protected void setHostItem(String item)
   {
      hostJComboBox.setSelectedItem(item);
   }

   //==============================================================
   // Class methods to set database combobox contents or item.
   //==============================================================

   protected void setDataBase(String content)
   {
      dbJComboBox.setSelectedItem(content);
   }

   protected void setDataBase(Vector<String> content)
   {
      dbJComboBox.removeAllItems();

      Iterator<String> contentsIterator = content.iterator();
      
      while (contentsIterator.hasNext())
         dbJComboBox.addItem(contentsIterator.next());
   }

   protected void setDatabaseItem(String item)
   {
      dbJComboBox.setSelectedItem(item);
   }

   //==============================================================
   // Class methods to set user combobox contents or item.
   //==============================================================

   protected void setUser(String content)
   {
      userJComboBox.setSelectedItem(content);
   }

   protected void setUser(Vector<String> content)
   {
      userJComboBox.removeAllItems();

      Iterator<String> contentsIterator = content.iterator();
      
      while (contentsIterator.hasNext())
         userJComboBox.addItem(contentsIterator.next());
   }

   protected void setUserItem(String item)
   {
      userJComboBox.setSelectedItem(item);
   }

   //==============================================================
   // Class method to set password textfield content.
   //==============================================================

   protected void setPassword(char[] content)
   {
      // Method Instances.
      StringBuffer contentString;
      
      contentString = new StringBuffer();

      for (int i = 0; i < content.length; i++)
         contentString.append(content[i]);

      if ((contentString.toString()).trim().equals(""))
         passwordTextField.setText("");
      else
         passwordTextField.setText(contentString.toString());
      
      // Clear contentString.
      contentString.delete(0, (contentString.length() - 1));
   }
}