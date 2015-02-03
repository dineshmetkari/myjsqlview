//=================================================================
//         MyJSQLView Advanced Parameters Panel
//=================================================================
//
// 	This class provides a panel that is used to hold the
// MyJSQLView advanced parameters components used in the
// MyJSQLView_Access and ConnectionManager classes.
//
//           << AdvancedParametersPanel.java >>
//
//=================================================================
// Copyright (C) 2005-2015 Dana M. Proctor
// Version 2.8 10/29/2014
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
// Version 1.0 Original AdvancedParametersPanel Class.
//         1.1 Removed JFrame Argument From Constructor.
//         1.2 Removed Class Instances Vector driverList, protocolList,
//             subProtocolList, portList Since They Were Passed As
//             Arguments to Constructor And Only Used Locally.
//         1.3 Header Update.
//         1.4 Added Class Instance serialVersionUID.
//         1.5 MyJSQLView Project Common Source Code Formatting.
//         1.6 Header Format Changes/Update.
//         1.7 Changed Package to Reflect Dandy Made Productions Code.
//         1.8 Constructor Argument resourceBundle Added and Constructor
//             Instance resource. Implementation of Internationalization.
//         1.9 Parameterized Vector Arguments driverList, protocolList,
//             subProtocolList, & portList to Bring Code Into Compliance
//             With Java 5.0 API. Also the Same for Argument content in
//             Class Methods setDriver(), setProtocol(), setSubProtocol(),
//             and setPort().
//         2.0 Parameterized Instance contentsIterator in Class Methods
//             setDriver(), setProtocol(), setSubProtocol(), & setPort().
//         2.1 Copyright Update.
//         2.2 Copyright Update.
//         2.3 Change Constructor Arguments driverList, protocolList,
//             subProtocolList, & portList from Vector Data Type to
//             ArrayList.
//         2.4 Changes in Way MyJSQLView_ResourceBundle Handles the Collection
//             of Resource Strings. Change to resource.getResourceString(key,
//             default).
//         2.5 Changed Package Name to com.dandymadeproductions.myjsqlview.gui.panels.
//             Made Class, Constructor, & All Getter/Setter Methods Public.
//         2.6 Constructor Dressed Up JComboBoxes.
//         2.7 Class Methods getDriver(), getProtocol(), getSubProtocol(), &
//             getPort() Return Empty String If No Selection in ComboBox.
//         2.8 Parameterized JComboBox Class Instances to Conform With JRE 7.
//        
//-----------------------------------------------------------------
//                 danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview.gui.panels;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.dandymadeproductions.myjsqlview.utilities.MyJSQLView_ResourceBundle;

/**
 *    The AdvancedParametersPanel class provides a panel that is used
 * to hold the MyJSQLView advanced parameters components used in the
 * MyJSQLView_Access and ConnectionManager classes.
 * 
 * @author Dana M. Proctor
 * @version 2.8 10/29/2014
 */

public class AdvancedParametersPanel extends JPanel
{
   // Class Instances.
   private static final long serialVersionUID = 3822991816957600977L;

   private JComboBox<Object> driverJComboBox;
   private JComboBox<Object> protocolJComboBox;
   private JComboBox<Object> subProtocolJComboBox;
   private JComboBox<Object> portJComboBox;

   //===========================================================
   // AdvancedParametersPanel Constructor
   //===========================================================

   public AdvancedParametersPanel(MyJSQLView_ResourceBundle resourceBundle, ArrayList<String> driverList,
                                  ArrayList<String> protocolList, ArrayList<String> subProtocolList,
                                  ArrayList<String> portList)
   {
      // Constructor Instances.
      JLabel driverLabel, protocolLabel, subProtocolLabel, portLabel;
      String resource;
      
      // Advanced Parameters Panel & Components
      setLayout(new GridLayout(8, 1));

      // Driver
      resource = resourceBundle.getResourceString("AdvancedParametersPanel.label.Driver", "Driver");
      driverLabel = new JLabel(resource);
      add(driverLabel);

      driverJComboBox = new JComboBox<Object>(driverList.toArray());
      driverJComboBox.setBorder(BorderFactory.createLoweredBevelBorder());
      driverJComboBox.setEditable(true);
      driverJComboBox.setBounds(0, 0, 40, 12);
      driverJComboBox.addItem("");
      add(driverJComboBox);

      // Protocol
      resource = resourceBundle.getResourceString("AdvancedParametersPanel.label.Protocol", "Protocol");
      protocolLabel = new JLabel(resource);
      add(protocolLabel);

      protocolJComboBox = new JComboBox<Object>(protocolList.toArray());
      protocolJComboBox.setBorder(BorderFactory.createLoweredBevelBorder());
      protocolJComboBox.setEditable(true);
      protocolJComboBox.setBounds(0, 0, 40, 12);
      protocolJComboBox.addItem("");
      add(protocolJComboBox);

      // SubProtocol
      resource = resourceBundle.getResourceString("AdvancedParametersPanel.label.SubProtocol",
                                                  "SubProtocol");
      subProtocolLabel = new JLabel(resource);
      add(subProtocolLabel);

      subProtocolJComboBox = new JComboBox<Object>(subProtocolList.toArray());
      subProtocolJComboBox.setBorder(BorderFactory.createLoweredBevelBorder());
      subProtocolJComboBox.setEditable(true);
      subProtocolJComboBox.setBounds(0, 0, 40, 12);
      subProtocolJComboBox.addItem("");
      add(subProtocolJComboBox);

      // Port
      resource = resourceBundle.getResourceString("AdvancedParametersPanel.label.Port", "Port");
      portLabel = new JLabel(resource);
      add(portLabel);

      portJComboBox = new JComboBox<Object>(portList.toArray());
      portJComboBox.setBorder(BorderFactory.createLoweredBevelBorder());
      portJComboBox.setEditable(true);
      portJComboBox.setBounds(0, 0, 40, 12);
      portJComboBox.addItem("");
      add(portJComboBox);
   }

   //===============================================================
   // Class methods to get the various panels' components content.
   //===============================================================

   public String getDriver()
   {
      if (driverJComboBox.getSelectedItem() == null)
         return "";
      else
         return (String) driverJComboBox.getSelectedItem();
   }

   public String getProtocol()
   {
      if (protocolJComboBox.getSelectedItem() == null)
         return "";
      else
         return (String) protocolJComboBox.getSelectedItem();
   }

   public String getSubProtocol()
   {
      if (subProtocolJComboBox.getSelectedItem() == null)
         return "";
      else
         return (String) subProtocolJComboBox.getSelectedItem();
   }

   public String getPort()
   {
      if (portJComboBox.getSelectedItem() == null)
         return "";
      else
         return (String) portJComboBox.getSelectedItem();
   }

   //===============================================================
   // Class methods to set driver combobox contents or item.
   //===============================================================

   public void setDriver(String content)
   {
      driverJComboBox.setSelectedItem(content);
   }

   public void setDriver(ArrayList<String> content)
   {
      driverJComboBox.removeAllItems();

      Iterator<String> contentsIterator = content.iterator();
      
      while (contentsIterator.hasNext())
         driverJComboBox.addItem(contentsIterator.next());
   }

   public void setDriverItem(String item)
   {
      driverJComboBox.setSelectedItem(item);
   }

   //===============================================================
   // Class methods to set protocol combobox contents or item.
   //===============================================================

   public void setProtocol(String content)
   {
      protocolJComboBox.setSelectedItem(content);
   }

   public void setProtocol(ArrayList<String> content)
   {
      protocolJComboBox.removeAllItems();

      Iterator<String> contentsIterator = content.iterator();
      
      while (contentsIterator.hasNext())
         protocolJComboBox.addItem(contentsIterator.next());
   }

   public void setProtocolItem(String item)
   {
      protocolJComboBox.setSelectedItem(item);
   }

   //===============================================================
   // Class methods to set subProtocol combobox contents or item.
   //===============================================================

   public void setSubProtocol(String content)
   {
      subProtocolJComboBox.setSelectedItem(content);
   }

   public void setSubProtocol(ArrayList<String> content)
   {
      subProtocolJComboBox.removeAllItems();

      Iterator<String> contentsIterator = content.iterator();
      
      while (contentsIterator.hasNext())
         subProtocolJComboBox.addItem(contentsIterator.next());
   }

   public void setSubProtocolItem(String item)
   {
      subProtocolJComboBox.setSelectedItem(item);
   }

   //===============================================================
   // Class methods to set port combobox contents or item.
   //===============================================================

   public void setPort(String content)
   {
      portJComboBox.setSelectedItem(content);
   }

   public void setPort(ArrayList<String> content)
   {
      portJComboBox.removeAllItems();

      Iterator<String> contentsIterator = content.iterator();
      
      while (contentsIterator.hasNext())
         portJComboBox.addItem(contentsIterator.next());
   }

   public void setPortItem(String item)
   {
      portJComboBox.setSelectedItem(item);
   }
}
