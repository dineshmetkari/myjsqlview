//=================================================================
//                 MyJSQLView HTTP_PluginRepository.
//=================================================================
//
//    This class provides the general framework to create a HTTP
// type repository that would be derived from a web server.
//
//                 << HTTP_PluginRepository.java >>
//
//=================================================================
// Copyright (C) 2005-2012 Dana M. Proctor
// Version 1.0 09/13/2012
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
// Version 1.0 Initial MyJSQLView HTTP_PluginRepository Class.
//             
//-----------------------------------------------------------------
//                 danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview.plugin;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import com.dandymadeproductions.myjsqlview.MyJSQLView;

/**
 *    The HTTP_PluginRepository class provides the general framework to
 * create a HTTP type repository that is derived from a web server.   
 * 
 * @author Dana M. Proctor
 * @version 1.1 09/11/2012
 */

public class HTTP_PluginRepository extends PluginRepository
{
   // Class Instances
   
   
     
   //==============================================================
   // HTTP_PluginRepository Constructor
   //==============================================================

   public HTTP_PluginRepository()
   {
      super();
   }
   
   //==============================================================
   // Class method to setup up the repository.
   // The argument should be a reference to a network location.
   // exp. http://dandymadeproductions.com/
   //==============================================================

   public void setRepository(String repository)
   {
      // Method Instances
      URL repositoryURL;
      URLConnection repositoryConnection;
      InputStream inputStream;
      int contentLength, c;
      
      inputStream = null;
      
      try
      {
         repositoryURL = new URL(repository);
         repositoryConnection = repositoryURL.openConnection();
         contentLength = repositoryConnection.getContentLength();
         
         System.out.println("Content-Length: " + contentLength);
         
         if (contentLength > 0)
         {
            System.out.println("=== Content ===");
            inputStream = repositoryConnection.getInputStream();
            
            int i = contentLength;
            
            while (((c = inputStream.read()) != -1) && (--i > 0))
            {
               System.out.print((char) c);
            }
            
         }
         else
         {
            if (MyJSQLView.getDebug())
               System.out.println("HTTP_PluginRepository setRepository() No Content Available");
         }
      }
      catch (Exception e)
      {
         displayErrors(e.getMessage());
      }
      finally
      {
         try
         {
            inputStream.close();
         }
         catch (IOException ioe)
         {
            if (MyJSQLView.getDebug())
               System.out.println("HTTP_PluginRepository setRepository() Exception: \n" + ioe.toString());
         }
      }
   }
}