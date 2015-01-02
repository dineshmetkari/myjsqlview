//=================================================================
//                 MyJSQLView FILE_PluginRepository.
//=================================================================
//
//    This class provides the general framework to create a File
// type repository that would be derived from a local or networked
// file system.
//
//                 << FILE_PluginRepository.java >>
//
//=================================================================
// Copyright (C) 2005-2015 Dana M. Proctor
// Version 1.4 12/22/2014
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
// Version 1.0 Initial MyJSQLView FILE_PluginRepository Class.
//         1.1 Added Return boolean Type for setRepository() As Defined With
//             PluginRepositoryInterface.
//         1.2 Inserted Place Holder for PluginRepositoryInterface Requirement
//             refresh();
//         1.3 Added Class Instances. Implemented setRepository(), & refresh().
//             Added Class Method loadPluginList() & readPluginList().
//         1.4 Commented Out a System.out in Method setRepository().
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
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.zip.GZIPInputStream;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import com.dandymadeproductions.myjsqlview.utilities.MyJSQLView_Utils;

/**
 *    The FILE_PluginRepository class provides the general framework to
 * create a File type repository that would be derived from a local or
 * networked file system.
 * 
 * @author Dana M. Proctor
 * @version 1.4 12/22/2014
 */

public class FILE_PluginRepository extends PluginRepository
{
   // Class Instances
   private String remoteRepositoryURL, cachedRepositoryURL;
   private boolean downloadRepository;
   
   //==============================================================
   // FILE_PluginRepository Constructor
   //==============================================================

   public FILE_PluginRepository()
   {
      super();
      
      // Just Setup. The downloadRepository used
      // here for mainly testing, debugging. Since
      // this type of repository will always try to
      // download the plugin list if not cached.
      
      downloadRepository = true;
      
      setType(PluginRepository.FILE);
   }
   
   //==============================================================
   // Class method to setup up the repository in this case type
   // File. The file repository could either be locally or lan.
   // ex. C:\Users\Documents\plugin
   // ex. \\pc101\Users\jim\Documents\plugin
   // ex. /home/user/documents/plugin
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
         validRepository = loadPluginList();
      
      // Read the plugin list from the cache & set path to
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
            
            // System.out.println("FILE_PluginRepository setRepository() plugin path: "
            //                    + currentPlugin.getPath_FileName()); 
         }
      }
      
      return validRepository;
   }
   
   //==============================================================
   // Class method to refresh the repository by trying to download
   // the plugin list and reading again.
   //==============================================================
   
   public void refresh()
   {
      if (isRepositoryCached && loadPluginList())
         readPluginList(true);
   }
   
   //==============================================================
   // Class method to load the repository list and in so doing
   // populate the cache.
   //==============================================================
   
   private boolean loadPluginList()
   {
      // Method Instances
      URL loadURL;
      URLConnection urlConnection;
      
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
         // System.out.println("FILE_PluginRepository loadPluginList() Downloading Repository List");
         
         loadURL = new URL(remoteRepositoryURL);
         urlConnection = loadURL.openConnection();
         
         // Authorization Needed.
         if(urlConnection.getPermission().getActions().indexOf("read") == -1)
         {
            displayErrors("FILE_PluginRepository loadPluginList()\n"
                          + "Repository URL Requires Read Access.");
         }
         // Looks Good.
         else
         {
            cacheFileName = cachedRepositoryURL.replaceFirst("file:", "");
            
            // Create Streams
            inputStream = urlConnection.getInputStream();
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
      }
      // MalformedURLException, IOException, FileNotFoundException, UnknownHostException?
      catch(Exception e)
      {
         displayErrors("FILE_PluginRepository loadPluginList() Exception:\n" + e.toString());
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
               System.out.println("FILE_PluginRepository loadloadPluginList() "
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
                  System.out.println("FILE_PluginRepository downloadPluginList() "
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
                     System.out.println("FILE_PluginRepository loadPluginList() "
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
                        System.out.println("FILE_PluginRepository loadPluginList() "
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
            // System.out.println("FILE_PluginRepository readPluginList() Using Cached Plugin List.");
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
               loadPluginList();
            
            readPluginList(false);
         }
         else
            displayErrors("File_PluginRepository readPluginList() Exception: " + e.toString());
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
               System.out.println("FILE_PluginRepository readPluginList() "
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
                  System.out.println("FILE_PluginRepository readPluginList() "
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
                     System.out.println("FILE_PluginRepository readPluginList() "
                                        + "Failed to close inputStreamReader. " + ioe1.toString());
               }
            }
         }
      }
      return validRead;
   }
}
