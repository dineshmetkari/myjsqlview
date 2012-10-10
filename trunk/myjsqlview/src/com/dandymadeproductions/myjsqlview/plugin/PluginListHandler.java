//=================================================================
//                 MyJSQLView PluginListHander.
//=================================================================
//    This class provides a processing routine for parsing a xml
// file that contains data for plugin modules information that may
// be used with the MyJSQLView application. The data encompasses
// characteristics like name, author, location, etc. Should be used
// in Conjuction With myjsqlview_plugins.dtd.
//
//                   << PluginListHandler.java >>
//
//=================================================================
// Copyright (C) 2001-2012 Slava Pestov, Dana M. Proctor
// Version 1.2 10/10/2012
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
// Version 1.0 XML handler for the plugin list used with jEdit Application.
//         1.1 JEdit PluginListHandler.java 12504 2008-04-22 23:12:43Z ezust
//         1.2 Modified for Use With the MyJSQLView Application.
//             
//-----------------------------------------------------------------
//                 danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview.plugin;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.helpers.DefaultHandler;

import com.dandymadeproductions.myjsqlview.MyJSQLView;
import com.dandymadeproductions.myjsqlview.utilities.MyJSQLView_ResourceBundle;

/**
 *    The PluginListHandler class provides a processing routine for
 * parsing a xml file that contains data for plugin modules information
 * that may be used with the MyJSQLView application. The data encompasses
 * characteristics like name, author, location, etc. Should be used in
 * Conjuction With myjsqlview_plugins.dtd.
 * 
 * @author Slava Pestov, Dana M. Proctor
 * @version 1.2 10/10/2012
 */

class PluginListHandler extends DefaultHandler
{
   // Class Instances
   private final PluginRepository pluginRepository;
   private Plugin plugin;
   
   private String name;
   private String jar;
   private StringBuilder author;
   private StringBuilder version;
   private StringBuilder path;
   private StringBuilder description;
   private StringBuilder category;
   private int size;
   
   boolean debug;
   
   private final Stack<String> stateStack = new Stack<String>();
   private static final String DTD_PARSER = "myjsqlview_plugins.dtd";
   
   //===========================================================
   // PluginListHandler Constructor
   //===========================================================

   PluginListHandler(PluginRepository repository)
   {
      this.pluginRepository = repository;

      author = new StringBuilder();
      version = new StringBuilder();
      path = new StringBuilder();
      description = new StringBuilder();
      category = new StringBuilder();
      
      debug = MyJSQLView.getDebug();  
   }
   
   // Normally always returns null, so that the parser will use the system
   // identifier provided in XML document. Use this overide if wish to
   // do special translations such as catalog lookups or URI redirection.
   // Works essentially correctly, but if trouble defaults to null.
   
   public InputSource resolveEntity(String publicId, String systemId)
   {
      // Method Instances
      File list_XML_DTD_File;
      MyJSQLView_ResourceBundle resourceBundle;
      
      if (systemId != null && systemId.endsWith(DTD_PARSER))
      { 
         resourceBundle = MyJSQLView.getResourceBundle();
         list_XML_DTD_File = resourceBundle.getResourceFile(DTD_PARSER);
         
         if (list_XML_DTD_File != null)
         {
            try
            {
               return new InputSource(new BufferedInputStream(new FileInputStream(list_XML_DTD_File)));
            }
            catch (Exception e)
            {
               if (debug)
                  System.out.println("PluginListHandler resolveEntity() " + e.toString());
            }
         }  
      }
      return null;
   }
   
   public void attribute(String aname, String value, boolean isSpecified)
   {
      if (aname.equals("NAME"))
         name = value;
      else if (aname.equals("JAR"))
         jar = value;
      else if (aname.equals("SIZE"))
      {
         try
         {
            size = Integer.parseInt(value);
         }
         catch (NumberFormatException nfe)
         {
            size = 0;
         }
         
         if (size == 0)
         {
            if (debug)
               System.out.println("PluginListHandler attribute() Size Zero");
         }
      }
   }
   
   public void characters(char[] c, int off, int len)
   {
      String tag = peekElement();

      if (tag.equals("AUTHOR"))
      {
         author.append(c, off, len);
      }
      else if (tag.equals("VERSION"))
      {
         version.append(c, off, len);
      }
      else if (tag.equals("PATH"))
      {
         path.append(c, off, len);
      }
      else if (tag.equals("DESCRIPTION"))
      {
         description.append(c, off, len);
      }
      else if (tag.equals("CATEGORY"))
      {
         category.append(c, off, len);
      }
   }

   public void startElement(String uri, String localName, String tag, Attributes attrs)
   {
      for (int i = 0; i < attrs.getLength(); i++)
      {
         String aName = attrs.getQName(i);
         String aValue = attrs.getValue(i);
         attribute(aName, aValue, true);
      }

      tag = pushElement(tag);

      if (tag.equals("PLUGIN"))
      {
         author.setLength(0);
         version.setLength(0);
         path.setLength(0);
         description.setLength(0);
         category.setLength(0);
         
         plugin = new Plugin();
      }
   }

   public void endElement(String uri, String localName, String tag)
   {
      popElement();

      if (tag.equals("PLUGIN"))
      {
         plugin.setName(name);
         plugin.setJAR(jar);
         plugin.setAuthor(author.toString());
         plugin.setVersion(version.toString());
         plugin.setPath_FileName(path.toString());
         plugin.setDescription(description.toString());
         plugin.setCategory(category.toString());
         plugin.setSize(size);
         
         pluginRepository.addPluginItem(plugin);
         
         name = null;
         jar = null;
         author.setLength(0);
         version.setLength(0);
         path.setLength(0);
         description.setLength(0);
         category.setLength(0);
         size = 0;
      }
   }

   public void startDocument()
   {
      try
      {
         pushElement(null);
      }
      catch (Exception e)
      {
         if (debug)
            System.out.println("PluginListHandler startDocument() " + e.toString());
      }
   }

   /*
   public void endDocument()
   {
   
   }
   */

   // end HandlerBase implementation
   
   private String pushElement(String name)
   {
      stateStack.push(name);
      return name;
   }

   private String peekElement()
   {
      return stateStack.peek();
   }

   private String popElement()
   {
      return stateStack.pop();
   }
}