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
// Copyright (C) 2005-2010 Dana M. Proctor
// Version 2.0 05/19/2010
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
//         1.2 Removed Class Instances Vector driverList,
//             protocolList, subProtocolList, portList Since
//             They Were Passed As Arguments to Constructor
//             And Only Used Locally.
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

/**
 *    The AdvancedParametersPanel class provides a panel that is used
 * to hold the MyJSQLView advanced parameters components used in the
 * MyJSQLView_Access and ConnectionManager classes.
 * 
 * @author Dana M. Proctor
 * @version 2.0 05/19/2010
 */

class AdvancedParametersPanel extends JPanel
{
   // Class Instances.
   private static final long serialVersionUID = 3822991816957600977L;

   private JComboBox driverJComboBox;
   private JComboBox protocolJComboBox;
   private JComboBox subProtocolJComboBox;
   private JComboBox portJComboBox;

   //===========================================================
   // AdvancedParametersPanel Constructor
   //===========================================================

   protected AdvancedParametersPanel(MyJSQLView_ResourceBundle resourceBundle, Vector<String> driverList,
                                     Vector<String> protocolList, Vector<String> subProtocolList,
                                     Vector<String> portList)
   {
      // Constructor Instances.
      JLabel driverLabel, protocolLabel, subProtocolLabel, portLabel;
      String resource;
      
      // Advanced Parameters Panel & Components
      setLayout(new GridLayout(8, 1));

      // Driver
      resource = resourceBundle.getResource("AdvancedParametersPanel.label.Driver");
      if (resource.equals(""))
         driverLabel = new JLabel("Driver");
      else
         driverLabel = new JLabel(resource);
      add(driverLabel);

      driverJComboBox = new JComboBox(driverList);
      driverJComboBox.setEditable(true);
      driverJComboBox.setBounds(0, 0, 40, 12);
      driverJComboBox.addItem("");
      add(driverJComboBox);

      // Protocol
      resource = resourceBundle.getResource("AdvancedParametersPanel.label.Protocol");
      if (resource.equals(""))
         protocolLabel = new JLabel("Protocol");
      else
         protocolLabel = new JLabel(resource);
      add(protocolLabel);

      protocolJComboBox = new JComboBox(protocolList);
      protocolJComboBox.setEditable(true);
      protocolJComboBox.setBounds(0, 0, 40, 12);
      protocolJComboBox.addItem("");
      add(protocolJComboBox);

      // SubProtocol
      resource = resourceBundle.getResource("AdvancedParametersPanel.label.SubProtocol");
      if (resource.equals(""))
         subProtocolLabel = new JLabel("SubProtocol");
      else
         subProtocolLabel = new JLabel(resource);
      add(subProtocolLabel);

      subProtocolJComboBox = new JComboBox(subProtocolList);
      subProtocolJComboBox.setEditable(true);
      subProtocolJComboBox.setBounds(0, 0, 40, 12);
      subProtocolJComboBox.addItem("");
      add(subProtocolJComboBox);

      // Port
      resource = resourceBundle.getResource("AdvancedParametersPanel.label.Port");
      if (resource.equals(""))
         portLabel = new JLabel("Port");
      else
         portLabel = new JLabel(resource);
      add(portLabel);

      portJComboBox = new JComboBox(portList);
      portJComboBox.setEditable(true);
      portJComboBox.setBounds(0, 0, 40, 12);
      portJComboBox.addItem("");
      add(portJComboBox);
   }

   //===============================================================
   // Class methods to get the various panels' components content.
   //===============================================================

   protected String getDriver()
   {
      return (String) driverJComboBox.getSelectedItem();
   }

   protected String getProtocol()
   {
      return (String) protocolJComboBox.getSelectedItem();
   }

   protected String getSubProtocol()
   {
      return (String) subProtocolJComboBox.getSelectedItem();
   }

   protected String getPort()
   {
      return (String) portJComboBox.getSelectedItem();
   }

   //===============================================================
   // Class methods to set driver combobox contents or item.
   //===============================================================

   protected void setDriver(String content)
   {
      driverJComboBox.setSelectedItem(content);
   }

   protected void setDriver(Vector<String> content)
   {
      driverJComboBox.removeAllItems();

      Iterator<String> contentsIterator = content.iterator();
      
      while (contentsIterator.hasNext())
         driverJComboBox.addItem(contentsIterator.next());
   }

   protected void setDriverItem(String item)
   {
      driverJComboBox.setSelectedItem(item);
   }

   //===============================================================
   // Class methods to set protocol combobox contents or item.
   //===============================================================

   protected void setProtocol(String content)
   {
      protocolJComboBox.setSelectedItem(content);
   }

   protected void setProtocol(Vector<String> content)
   {
      protocolJComboBox.removeAllItems();

      Iterator<String> contentsIterator = content.iterator();
      
      while (contentsIterator.hasNext())
         protocolJComboBox.addItem(contentsIterator.next());
   }

   protected void setProtocolItem(String item)
   {
      protocolJComboBox.setSelectedItem(item);
   }

   //===============================================================
   // Class methods to set subProtocol combobox contents or item.
   //===============================================================

   protected void setSubProtocol(String content)
   {
      subProtocolJComboBox.setSelectedItem(content);
   }

   protected void setSubProtocol(Vector<String> content)
   {
      subProtocolJComboBox.removeAllItems();

      Iterator<String> contentsIterator = content.iterator();
      
      while (contentsIterator.hasNext())
         subProtocolJComboBox.addItem(contentsIterator.next());
   }

   protected void setSubProtocolItem(String item)
   {
      subProtocolJComboBox.setSelectedItem(item);
   }

   //===============================================================
   // Class methods to set port combobox contents or item.
   //===============================================================

   protected void setPort(String content)
   {
      portJComboBox.setSelectedItem(content);
   }

   protected void setPort(Vector<String> content)
   {
      portJComboBox.removeAllItems();

      Iterator<String> contentsIterator = content.iterator();
      
      while (contentsIterator.hasNext())
         portJComboBox.addItem(contentsIterator.next());
   }

   protected void setPortItem(String item)
   {
      portJComboBox.setSelectedItem(item);
   }
}