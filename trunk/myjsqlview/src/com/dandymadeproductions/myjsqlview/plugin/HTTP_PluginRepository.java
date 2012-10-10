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
// Version 1.1 10/10/2012
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
//         1.1 Semi-Complete Build to Perform the Essential Requirements of a HTTP
//             Repository. Implemented Class Methods setRepository(), downloadPluginList()
//             & readPluginList(). Still Needs to be Throughly Reviewed & Verify Checks
//             When Errors Occur.
//             
//-----------------------------------------------------------------
//                 danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview.plugin;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.GZIPInputStream;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import com.dandymadeproductions.myjsqlview.MyJSQLView;
import com.dandymadeproductions.myjsqlview.utilities.MyJSQLView_Utils;

/**
 *    The HTTP_PluginRepository class provides the general framework to
 * create a HTTP type repository that is derived from a web server.   
 * 
 * @author Dana M. Proctor
 * @version 1.1 10/10/2012
 */

public class HTTP_PluginRepository extends PluginRepository
{
   // Class Instances
   private String remoteRepositoryURL, cachedRepositoryURL;
   private boolean debugMode, cacheRepository;
   private boolean downloadRepository;
   
   private static final int GZIP_MAGIC_1 = 0x1f;
   private static final int GZIP_MAGIC_2 = 0x8b;
   private static final String REMOTE_REPOSITORY_FILENAME = "myjsqlview_plugin_list.xml";
   
   //==============================================================
   // HTTP_PluginRepository Constructor
   //==============================================================

   public HTTP_PluginRepository()
   {  
      // Just Setup
      debugMode = MyJSQLView.getDebug();
      cacheRepository = false;
      downloadRepository = true;
      
      setType(PluginRepository.HTTP);
   }
   
   //==============================================================
   // Class method to setup up the repository, type HTTP.
   //==============================================================
   
   public boolean setRepository(String path)
   {
      // Method Instances
      String cachedRepositoryDirectoryString;
      File cachedRepositoryDirectory, cachedRepositoryListFile;
      String localSystemFileSeparator;
      boolean isRepositoryCached, validRepository;
      
      // Setup
      
      if (path.endsWith("/"))
         remoteRepositoryURL = path + REMOTE_REPOSITORY_FILENAME;
      else
         remoteRepositoryURL = path + "/" + REMOTE_REPOSITORY_FILENAME;
      
      cachedRepositoryDirectoryString = MyJSQLView_Utils.getCacheDirectory() + getName();
      localSystemFileSeparator = MyJSQLView_Utils.getFileSeparator();
      isRepositoryCached = false;
      validRepository = false;
      
      // Check for a valid repository and if not create at least
      // the directory.
      
      cachedRepositoryDirectory = new File(cachedRepositoryDirectoryString);
      
      if (cachedRepositoryDirectory.exists() && cachedRepositoryDirectory.isDirectory())
      {
         isRepositoryCached = true;
         
         cachedRepositoryListFile = new File(cachedRepositoryDirectoryString
                                             + localSystemFileSeparator
                                             + PluginRepository.REPOSITORY_CACHED_FILE);
         
         if (cachedRepositoryListFile.isFile())
            validRepository = true;
         else
            validRepository = false;
      }
      else
      {
         isRepositoryCached = cachedRepositoryDirectory.mkdir();
         validRepository = false;
      }
      
      // Create the cached repository URL String
      
      if (isRepositoryCached)
         cachedRepositoryURL = "file:" + cachedRepositoryDirectoryString
                               + localSystemFileSeparator
                               + PluginRepository.REPOSITORY_CACHED_FILE;
      else
         cachedRepositoryURL = remoteRepositoryURL;
      
      // Determine if a download should occur.
      
      if (downloadRepository && isRepositoryCached && !validRepository)
      {
         System.out.println("Downloading Repository List");
         validRepository = downloadPluginList();
      }
      
      // Read the plugin list from the cache.
      
      if (validRepository)
         validRepository = readPluginList();
      
      return validRepository;
   }
   
   //==============================================================
   // Class method to download the repository list and in so doing
   // populate the cache.
   //==============================================================
   
   private boolean downloadPluginList()
   {
      // Method Instances
      URL downloadURL;
      HttpURLConnection httpConnection;
      
      InputStream inputStream;
      BufferedInputStream bufferedInputStream;
      
      FileOutputStream fileOutputStream;
      BufferedOutputStream bufferedOutputStream;
      
      String cacheFileName;
      byte[] inputBytes;
      
      boolean validDownload;
      
      // Setup
      inputStream = null;
      bufferedInputStream = null;
      fileOutputStream = null;
      bufferedOutputStream = null;
      validDownload = false;
      
      try
      {
         downloadURL = new URL(remoteRepositoryURL);
         httpConnection = (HttpURLConnection) downloadURL.openConnection();
         
         // Authorization Needed.
         if(httpConnection.getResponseCode() == HttpURLConnection.HTTP_PROXY_AUTH)
         {
            if (debugMode)
               System.out.println("HTTP_PluginRepository downloadPluginList() Need Authorization.");
         }
         // Looks Good.
         else if(httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK)
         {
            cacheFileName = cachedRepositoryURL.replaceFirst("file:", "");
            
            // Create Streams
            inputStream = httpConnection.getInputStream();
            bufferedInputStream = new BufferedInputStream(inputStream);
            
            fileOutputStream = new FileOutputStream(cacheFileName);
            bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
             
            // Setup Buffer, read, write, flush.
            inputBytes = new byte[4096];
            
            int n;
            
            while ((n = bufferedInputStream.read(inputBytes)) != -1)
               bufferedOutputStream.write(inputBytes, 0, n);
            
            bufferedOutputStream.flush();
            fileOutputStream.flush();
            
            validDownload = true;
         }
         else
         {
            if (debugMode)
               System.out.println("HTTP_PluginRepository downloadPluginList() HTTP Error:\n"
                                  + httpConnection.getResponseCode() + " : "
                                  + httpConnection.getResponseMessage());
         }
      }
      // MalformedURLException, IOException, FileNotFoundException, UnknownHostException?
      catch(Exception e)
      {
         if (debugMode)
            System.out.println("HTTP_PluginRepository downloadPluginList() HTTP Error:\n" + e.toString());
      }  
      finally
      {
         try
         {
            if (bufferedOutputStream != null)
               bufferedOutputStream.close();
         }
         catch (IOException ioe1)
         {
            if (debugMode)
               System.out.println("HTTP_PluginRepository downloadPluginList() "
                                  + "Failed to close BufferedOutputStream. " + ioe1.toString());
         }
         finally
         {
            try
            {
               if (fileOutputStream != null)
                  fileOutputStream.close();
            }
            catch (IOException ioe2)
            {
               if (debugMode)
                  System.out.println("HTTP_PluginRepository downloadPluginList() "
                                     + "Failed to close FileOutputStream. " + ioe2.toString());
            }
            finally
            {
               try
               {
                  if (bufferedInputStream != null)
                     bufferedInputStream.close();
               }
               catch (IOException ioe3)
               {
                  if (debugMode)
                     System.out.println("HTTP_PluginRepository downloadPluginList() "
                                        + "Failed to close BufferedInputStream. " + ioe3.toString());
               }
               finally
               {
                  try
                  {
                     if (inputStream != null)
                        inputStream.close();
                  }
                  catch (IOException ioe4)
                  {
                     if (debugMode)
                        System.out.println("HTTP_PluginRepository downloadPluginList() "
                                           + "Failed to close InputStream. " + ioe4.toString());
                  }
               }
            }
         }
      }
      return validDownload;
   }
   
   //==============================================================
   // Class method to read the repository plugin list from the
   // cache.
   //==============================================================
   
   private boolean readPluginList()
   {
      // Class methods
      XMLReader xmlParser;
      PluginListHandler pluginListHandler;
      InputStream urlInputStream;
      InputStream inputStream;
      InputStreamReader inputStreamReader;
      InputSource inputSource;
      
      boolean validRead;
      
      // Setup
      urlInputStream = null;
      inputStream = null;
      inputStreamReader = null;
      validRead = false;
      
      // Try reading & parsing list.
      try
      {
         if (cachedRepositoryURL != remoteRepositoryURL) 
         {
            if (debugMode)
               System.out.println("HTTP_PluginRepository readPluginList() Using Cached Plugin List.");
         }
         
         urlInputStream = new URL(cachedRepositoryURL).openStream();
         
         xmlParser = XMLReaderFactory.createXMLReader();
         pluginListHandler = new PluginListHandler(this);
         inputStream = new BufferedInputStream(urlInputStream);
         
         // Check to see if list is xml, or zip/gz file.
         if(inputStream.markSupported())
         {
            inputStream.mark(2);
            int b1 = inputStream.read();
            int b2 = inputStream.read();
            inputStream.reset();

            if(b1 == GZIP_MAGIC_1 && b2 == GZIP_MAGIC_2)
               inputStream = new GZIPInputStream(inputStream);
         }
         
         // Setup stream and parse.
         inputStreamReader = new InputStreamReader(inputStream,"UTF8");
         inputSource = new InputSource(inputStreamReader);
         
         inputSource.setSystemId("MyJSQLView.jar");
         xmlParser.setContentHandler(pluginListHandler);
         xmlParser.setDTDHandler(pluginListHandler);
         xmlParser.setEntityResolver(pluginListHandler);
         xmlParser.setErrorHandler(pluginListHandler);
         xmlParser.parse(inputSource);
         
         validRead = true;
      }
      // MalFormedURLException, SAXException, IOException
      catch (Exception e)
      {
         if (cachedRepositoryURL.startsWith("file:"))
         {
            /*
            // "Unable to read plugin list, deleting cached file and try again");
            new File(cachedURL.substring(8)).delete();
            if (allowRetry)
            {
               plugins.clear();
               readPluginList(false);
            }
            */
         }
         if (debugMode)
            System.out.println("HTTP_PluginRepository readPluginList() Exception: " + e.toString());
      }
      finally
      {
         try
         {
            if (urlInputStream != null)
               urlInputStream.close();
         }
         catch (IOException ioe)
         {
            if (debugMode)
               System.out.println("HTTP_PluginRepository readPluginList() "
                                  + "Failed to close URL InputStream. " + ioe.toString());
         }
         finally
         {
            try
            {
               if (inputStream != null)
                  inputStream.close();  
            }
            catch (IOException ioe1)
            {
               if (debugMode)
                  System.out.println("HTTP_PluginRepository readPluginList() "
                                     + "Failed to close InputStream. " + ioe1.toString());
            }
            finally
            {
               try
               {
                  if (inputStreamReader != null)
                     inputStreamReader.close();  
               }
               catch (IOException ioe1)
               {
                  if (debugMode)
                     System.out.println("HTTP_PluginRepository readPluginList() "
                                        + "Failed to close InputStreamReader. " + ioe1.toString());
               }
            }
         }
      }
      return validRead;
   }
}