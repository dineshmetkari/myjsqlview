//=================================================================
//                   SiteParameters Class
//=================================================================
//	   This class provides the object that MyJSQLView uses to
// define/store individual site parameters created by the
// XMLTranslator.
//                  << SiteParameter.java >>
//
//=================================================================
// Copyright (C) 2005-2012 Nil_lin, Dana Proctor
// Version 2.2 01/01/2012
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
// Version 1.0 Initial DatabaseConfig, Nil_lin.
//         1.1 Initial Integration Into MyJSQLView, Open
//             Software Header and Version Indicator Comments.
//         1.2 Redefined to SiteParameter.
//         1.3 Reformated and Commeted.
//         1.4 Changed All get/setters to Protected.
//         1.5 Changed password Class Instance to char[].
//         1.6 Header Update.
//         1.7 MyJSQLView Project Common Source Code Formatting.
//         1.8 Renamed to SiteParameters.
//         1.9 Header Format Changes/Update.
//         2.0 Changed Package to Reflect Dandy Made Productions Code.
//         2.1 Copyright Update.
//         2.2 Copyright Update.
//
//-----------------------------------------------------------------
//                 nil_lin@users.sourceforge.net
//                 danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview;

/**
 * The SiteParameters class provides the object that MyJSQLView uses
 * to define/store individual site parameters created by the
 * XMLTranslator.
 * 
 * @author Nil, Dana M. Proctor
 * @version 2.2 01/01/2012
 */

class SiteParameters
{
   // Class Instances.
   private String siteName;
   private String driver;
   private String protocol;
   private String subProtocol;
   private String host;
   private String port;
   private String database;
   private String user;
   private char[] password;
   private String ssh;

   //==============================================================
   // SiteParameter Constructor
   //==============================================================

   protected SiteParameters()
   {
      // None
   }

   //==============================================================
   // Class methods to allow classes to get the site parameter
   // object components.
   //==============================================================

   protected String getSiteName()
   {
      return siteName;
   }

   protected String getDriver()
   {
      return driver;
   }

   protected String getProtocol()
   {
      return protocol;
   }

   protected String getSubProtocol()
   {
      return subProtocol;
   }

   protected String getHost()
   {
      return host;
   }

   protected String getPort()
   {
      return port;
   }

   protected String getDatabase()
   {
      return database;
   }

   protected String getUser()
   {
      return user;
   }

   protected char[] getPassword()
   {
      return password;
   }

   protected String getSsh()
   {
      return ssh;
   }

   //==============================================================
   // Class methods to allow classes to set the site parameter
   // object components.
   //==============================================================

   protected void setSiteName(String siteName)
   {
      this.siteName = siteName;
   }

   protected void setDriver(String driver)
   {
      this.driver = driver;
   }

   protected void setProtocol(String protocol)
   {
      this.protocol = protocol;
   }

   protected void setSubProtocol(String subProtocol)
   {
      this.subProtocol = subProtocol;
   }

   protected void setHost(String host)
   {
      this.host = host;
   }

   protected void setPort(String port)
   {
      this.port = port;
   }

   protected void setDatabase(String database)
   {
      this.database = database;
   }

   protected void setUser(String user)
   {
      this.user = user;
   }

   protected void setPassword(char[] password)
   {
      if (password.length != 0)
      {
         this.password = new char[password.length];
         for (int i = 0; i < password.length; i++)
            this.password[i] = password[i];
      }
      else
      {
         this.password = new char[1];
         this.password[0] = ' ';
      }
   }

   protected void setSsh(String ssh)
   {
      this.ssh = ssh;
   }

   //==============================================================
   // Class method to properly implement the toString() method
   // for the object. Local method overides.
   //==============================================================

   public String toString()
   {
      StringBuffer parameters = new StringBuffer("[SiteParameters: ");
      parameters.append("[name = " + siteName + "]");
      parameters.append("[driver = " + driver + "]");
      parameters.append("[protocol = " + protocol + "]");
      parameters.append("[subProtocol = " + subProtocol + "]");
      parameters.append("[host = " + host + "]");
      parameters.append("[port = " + port + "]");
      parameters.append("[database = " + database + "]");
      parameters.append("[user = " + user + "]");
      parameters.append("[password = " + "" + "]");
      parameters.append("[ssh = " + ssh + "]");

      return parameters.toString();
   }
}