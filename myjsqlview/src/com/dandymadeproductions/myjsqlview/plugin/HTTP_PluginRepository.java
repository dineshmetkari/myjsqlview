//=================================================================
//                 MyJSQLView HTTP_PluginRepository.
//=================================================================
//
//    This class provides the general framework to create a HTTP
// type repository that would be derived from a web server. A web
// HTTP type repository will try to cache the plugin list as derived
// from a XML file at the resource.
//
//                 << HTTP_PluginRepository.java >>
//
//=================================================================
// Copyright (C) 2005-2015 Dana M. Proctor
// Version 1.6 01/02/2015
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
//         1.2 Implemented PluginRepositoryInterface Requirement refresh(). Removed
//             Class Instance cacheRepository. Moved isRepositoryCached from
//             Constructor to Class Instance. Added Argument allowRetry to Method
//             readPluginList(). Reviewed, Commented, & Used Various Output Routines
//             Either debugMode or displayErrors() to Provide Information/Exceptions.
//         1.3 Moved Class Instances debugMode & isRepositoryCached to Parent
//             PluginRepository Class. Changed REMOTE_REPOSITORY_FILENAME to a gz
//             File. In Method setRepository() setPath() on validRepository. Commented
//             Out Some debugMode System Outs.
//         1.4 Moved a System.out, Commented, from setRepostiory() to downloadPluginList().
//             Class Method readPluginList() in Catch Check for a SAXException on File
//             Cache Parsing to Try to Re-Download Plugin List to Correct.
//         1.5 Moved Class Instances GZIP_MAGIC_1/2, & REMOTE_REPOSITORY_FILENAME to
//             Parent, PluginRepository Class. Renamed the Latter to REPOSITORY_FILENAME.
//             Class Method setRepository() On validRepository Set Each Plugin in List
//             to the remoteRepositoryURL Path.
//         1.6 Added Class Instance generalProperties & Use for Implementation of Setting
//             Proxy in Method downloadPluginList(). *Note Not Implemented in GUI for
//             PluginFrame Because Individual Plugin Jar Files Throw UnsupportedOperation
//             Exception: Method not Implemented When Used. Can be Used for Repository.
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
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.Iterator;
import java.util.zip.GZIPInputStream;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import com.dandymadeproductions.myjsqlview.MyJSQLView;
import com.dandymadeproductions.myjsqlview.structures.GeneralProperties;
import com.dandymadeproductions.myjsqlview.utilities.MyJSQLView_Utils;

/**
 *    The HTTP_PluginRepository class provides the general framework to
 * create a HTTP type repository that would be derived from a web server.
 * A web HTTP type repository will try to cache the plugin list as derived
 * from a XML file at the resource.
 * 
 * @author Dana M. Proctor
 * @version 1.6 01/02/2015
 */

public class HTTP_PluginRepository extends PluginRepository
{
   // Class Instances
   private GeneralProperties generalProperties;
   private String remoteRepositoryURL, cachedRepositoryURL;
   private boolean downloadRepository;
   
   //==============================================================
   // HTTP_PluginRepository Constructor
   //==============================================================

   public HTTP_PluginRepository()
   {  
      super();
      
      // Just Setup. The downloadRepository used
      // here for mainly testing, debugging. Since
      // this type of repository will always try to
      // download the plugin list if not cached.
      
      generalProperties = MyJSQLView.getGeneralProperties();
      downloadRepository = true;
      
      setType(PluginRepository.HTTP);
   }
   
   //==============================================================
   // Class method to setup up the repository, in this case type
   // HTTP.
   //==============================================================
   
   public boolean setRepository(String path)
   {
      // Method Instances
      File cachedRepositoryDirectory, cachedRepositoryListFile;
      String localSystemFileSeparator;
      boolean validRepository;
      
      // Setup
      
      if (path.endsWith("/"))
         remoteRepositoryURL = path + PluginRepository.REPOSITORY_FILENAME;
      else
         remoteRepositoryURL = path + "/" + PluginRepository.REPOSITORY_FILENAME;
      
      cachedRepositoryDirectoryString = MyJSQLView_Utils.getCacheDirectory() + getName();
      localSystemFileSeparator = MyJSQLView_Utils.getFileSeparator();
      validRepository = false;
      
      // Check for a valid existing repository that is cached and if
      // not create at least the directory. A valid repository must
      // exist, isRepositoryCached, and contain the cached file.
      
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
         validRepository = downloadPluginList();
      
      // Read the plugin list from the cache & set path in
      // the cache directory for later identification when
      // reloading application's plugin frame.
      
      if (validRepository)
         validRepository = readPluginList(true);
      
      if (validRepository)
      {
         setPath(path);
         
         Iterator<Plugin> pluginIterator = getPluginItems().iterator();
         
         while (pluginIterator.hasNext())
         {
            Plugin currentPlugin = pluginIterator.next();
            
            currentPlugin.setPath_FileName(remoteRepositoryURL.substring(0,
               remoteRepositoryURL.length() - PluginRepository.REPOSITORY_FILENAME.length())
               + currentPlugin.getJAR());
            
            // System.out.println("HTTP_PluginRepository setRepository() plugin path: "
            //                    + currentPlugin.getPath_FileName());
         }
      }
      
      return validRepository;
   }
   
   //==============================================================
   // Class method to refresh the repository by trying to download
   // the plugin list and reading the cache again.
   //==============================================================
   
   public void refresh()
   {
      if (isRepositoryCached && downloadPluginList())
         readPluginList(true);
   }
   
   //==============================================================
   // Class method to download the repository list and in so doing
   // populate the cache.
   //==============================================================
   
   private boolean downloadPluginList()
   {
      // Method Instances
      Proxy httpProxy;
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
         // System.out.println("HTTP_PluginRepository downloadPluginList() Downloading Repository List");
         
         downloadURL = new URL(remoteRepositoryURL);
         
         if (!generalProperties.getProxyAddress().isEmpty())
         {
            httpProxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(generalProperties.getProxyAddress(),
               generalProperties.getProxyPort()));
            httpConnection = (HttpURLConnection) downloadURL.openConnection(httpProxy);
         }
         else
            httpConnection = (HttpURLConnection) downloadURL.openConnection();
         
         // Authorization Needed.
         if(httpConnection.getResponseCode() == HttpURLConnection.HTTP_PROXY_AUTH)
         {
            displayErrors("HTTP_PluginRepository downloadPlugins()\n"
                          + "Repository URL Requires Authorization.");
         }
         // Looks Good.
         else if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK)
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
            displayErrors("HTTP_PluginRepository downloadPluginList() HTTP Error:\n"
                          + httpConnection.getResponseCode() + " : "
                          + httpConnection.getResponseMessage());
         }
      }
      // MalformedURLException, IOException, FileNotFoundException, UnknownHostException?
      catch(Exception e)
      {
         displayErrors("HTTP_PluginRepository downloadPluginList() Exception:\n" + e.toString());
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
   
   private boolean readPluginList(boolean allowRetry)
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
            // System.out.println("HTTP_PluginRepository readPluginList() Using Cached Plugin List.");
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
         if (cachedRepositoryURL.startsWith("file:") && allowRetry)
         {
            clearPluginItems();
            
            if (e instanceof SAXException)
               downloadPluginList();
            
            readPluginList(false);
         }
         else
            displayErrors("HTTP_PluginRepository readPluginList() Exception: " + e.toString());
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
                                  + "Failed to close urlInputStream. " + ioe.toString());
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
                                     + "Failed to close inputStream. " + ioe1.toString());
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
                                        + "Failed to close inputStreamReader. " + ioe1.toString());
               }
            }
         }
      }
      return validRead;
   }
}
