//=================================================================
//               MyJSQLView_ResourceBundle Class
//=================================================================
//
//    The MyJSQLView_ResourceBundle class provides a method to more
// closely control the loading of locale, image, & other types of
// resource files in the MyJSQLView program. Handles also the methods
// needed to retrieve these various resource.
//
//             << MyJSQLView_ResourceBundle.java >>
//
//=================================================================
// Copyright (C) 2005-2014 Dana M. Proctor
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
// Version 1.0 02/23/2010 Initial MyJSQLView_ResourceBundle Class.
//         1.1 02/24/2010 Class Method getResource() Changed Conditional Check From
//                        localeListData.contains() to localeListData.containsKey().
//         1.2 04/28/2010 Constructor Added the MyJSQLView.getDebug() to Catching
//                        the IOException and Additional Output Information.
//         1.3 05/16/2010 Parameterized Class Instance localeListData to Bring Code
//                        Into Compliance With Java 5.0 API.
//         1.4 06/16/2010 Conversion of Using a FileReader to Using FileInputStream
//                        and a InputStreamReader With Specification of UTF-16 For
//                        CharsetName.
//         1.5 06/27/2010 Removed Constructor Instance baseName and Placed As An 
//                        Argument in Constructor. Changed Class to Public and Its
//                        Method getResource(). Cleaned Up Some and Created An OptionPane
//                        For When IOException Created in Unable to Load Resource.
//         1.6 06/27/2010 Added Constructor Argument localeDirectory.
//         1.7 01/27/2011 Copyright Update.
//         1.8 01/01/2012 Copyright Update.
//         1.9 01/01/2012 Implemented Class is Serializable.
//         2.0 05/07/2012 Changed Class Instance localeListData from Hashtable Data
//                        Type to HashMap.
//         2.1 07/16/2012 Preliminary Upgrade of Class to Deal With More Versatile
//                        Resource Locations Files, & URLs. Changes to Constructor,
//                        Added createFile/HTTP_Resource(), & getDefaultResourceString().
//                        Change in getResource(String) to getResourceString(String, String).
//         2.2 08/06/2012 Complete Redesign of Class to Handle Resources Outside the
//                        Context of Local Files. Now Handles Http & Jar File Resouces
//                        for Locale & Image Files.
//         2.3 08/08/2012 Removal of ioExceptionString from Methods createFile/Http/JAR_
//                        LocaleResource(). Removed Dead Code for Checking imageResourceURL
//                        in getResourceImage() & Same for imageBytes in getJAR_ImageResource().
//                        Changed Creation of resourceURL to Just Use String in Constructor.
//         2.4 08/18/2012 Created the Two Argument Constructor With Caching. Output in Debug
//                        Mode For Identifying Failed Loading of Local Image Files in Method
//                        getResourceImage().
//         2.5 08/19/2012 Added Class Methods getImage() & setImage(), Along With Class
//                        Instance imagesData.
//         2.6 08/20/2012 Change in Constructor boolean Argument cacheJar to Just cache.
//         2.7 09/10/2012 Changed Package Name to com.dandymadeproductions.myjsqlview.utilities.
//         2.8 10/05/2012 All debug Output Displays Class & Method Information. Added Class
//                        Methods getResourceFile() & getResourceBytes(). Class Method
//                        getJAR_ImageResource() Changed to getJAR_Resource().
//         2.9 10/10/2012 Class Method Use of New Method Instance fileResource to Return
//                        Valid Object from Routine.
//         3.0 10/11/2012 Class Method getResourceFile() Change in Creation of fileResource
//                        to Use a Generic OS Independent Path Creation for File Types.
//         3.1 10/18/2012 Class Method setLocaleResource() Correction in Creation of File
//                        Type Resource to Handle WinOS Network Paths.
//         3.2 10/19/2012 Class Method getResourceFile() Catch Output Resource URL from
//                        resourceURL.
//         3.3 02/26/2013 Added Three Argument Constructor & Changed Two Argument to Instance
//                        debugMode. Removed Collect of Same Class Instance Name to Assignment
//                        to Argument.
//         3.4 08/12/2013 Class Method getResourceImage/Bytes() Change of IOException Catching
//                        for JAR Resources to General Exception, Possible Null, When Improperly
//                        Built Plugin With No Resource in JAR.
//         3.5 10/20/2014 Created Class Method getResourceFXImage() To Handle Conforming
//                        to Use JavaFX Plugins and Their Image Requirements.
//                        
//-----------------------------------------------------------------
//                 danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview.utilities;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import javafx.scene.image.Image;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import com.dandymadeproductions.myjsqlview.MyJSQLView;

/**
 *    The MyJSQLView_ResourceBundle class provides a method to more closely
 * control the loading of locale, image, & other types of resource files in the
 * MyJSQLView program. Handles also the methods needed to retrieve these various
 * resource.
 * 
 * @author Dana M. Proctor
 * @version 3.5 10/20/2014
 */

public class MyJSQLView_ResourceBundle implements Serializable
{
   // Class Instances.
   private static final long serialVersionUID = -6752902010674915905L;

   private URL resourceURL;
   private String resourceType;
   private String cacheDirectory, cachedJAR_FileName;
   private HashMap<String, String> localeListData;
   private HashMap<String, ImageIcon> imagesData;

   boolean debugMode, cacheJar;

   private static final String FILE_RESOURCE = "file";
   private static final String HTTP_RESOURCE = "http";
   private static final String JAR_RESOUCE = "jar";

   //==============================================================
   // MyJSQLView_ResourceBundle Constructor
   //
   // Examples: Should be the path to the jar for plugins. A local
   // application just needs to specify the path to the
   // root directory of resources. If the path ends with
   // the jar name then all resource will be treated as
   // being in the jar file and may be cached if not local.
   // 
   // Local Files - /home/user/myplugin/, /home/user/myplugin/my.jar
   // Network Files - http://xyz.com/plugin/, http://xyz.com/my.jar
   //
   // Just instantiating this classes does not complete the process
   // of setting a resource bundle. The locale resource type must
   // also be initialized by a call to he the corresponding
   // setLocaleResource() so that data strings may be loaded.
   //==============================================================
   
   public MyJSQLView_ResourceBundle(String resourceURLString)
   {
      this(resourceURLString, false, true);
   }
   
   public MyJSQLView_ResourceBundle(String resourceURLString, boolean debugMode)
   {
      this(resourceURLString, debugMode, true);
   }
   
   public MyJSQLView_ResourceBundle(String resourceURLString, boolean debugMode, boolean cache)
   {
      // Setup to process.

      cacheDirectory = MyJSQLView_Utils.getCacheDirectory();
      this.debugMode = debugMode;
      cacheJar = cache;

      // Yea, nothing here move on.

      if (resourceURLString == null || resourceURLString.equals(""))
         return;

      // Create resource URL.
      else
      {
         try
         {
            // Special handling for JARs.

            if (resourceURLString.endsWith(".jar") && resourceURLString.indexOf(":") != -1)
            {
               resourceURL = new URL("jar:" + resourceURLString + "!/");

               // Determining if caching desired.
               if (cacheJar && (resourceURL.toExternalForm().indexOf("jar:file:") == -1))
               {
                  cachedJAR_FileName = cacheDirectory
                                       + (Long.toString(System.currentTimeMillis()).substring(0, 10));
                  try
                  {
                     cacheJar = cacheJAR(resourceURLString);
                  }
                  catch (IOException ioe)
                  {
                     if (debugMode)
                        System.out.println("MyJSQLView_ResourceBundle Constructor()\n"
                                           + "Failed to Close Cache Stream.\n" + ioe.toString());
                  }
               }
            }
            else
            {
               resourceURL = new URL(resourceURLString);
               cacheJar = false;
            }

            // System.out.println("MyJSQLView Resource URL:" + resourceURL.toExternalForm());

            resourceType = resourceURL.getProtocol();
         }
         catch (MalformedURLException mfe)
         {
            displayErrors("MyJSQLView_ResourceBundle Constructor() \n"
                          + "Failed to create locale resouce URL from, " + resourceURLString + "\n"
                          + mfe.toString());
            return;
         }
      }

      // Setup a hashmap to be used in storing locale resource file
      // key, value pairs.

      localeListData = new HashMap<String, String>();
   }

   //==============================================================
   // Class Method for classes to obtain the locale resource given
   // a key.
   //==============================================================

   public String getResourceString(String resourceKey, String defaultValue)
   {
      // System.out.println(resourceKey);

      if (localeListData != null && resourceKey != null)
      {
         if (localeListData.containsKey(resourceKey))
            return localeListData.get(resourceKey);
         else
         {
            if (!localeListData.isEmpty() && debugMode)
               System.out.println("MyJSQLView_ResourceBundle getResourceString()\n"
                                  + "Invalid Resource Key: " + resourceKey);

            return getDefaultResourceString(defaultValue);
         }
      }
      else
      {
         if (debugMode)
            System.out.println("MyJSQLView_ResourceBundle getResourceString()\n"
                               + "Either Undefined Locale or Resource Key.");

         return getDefaultResourceString(defaultValue);
      }
   }

   //==============================================================
   // Class Method for returning a default resource string.
   //==============================================================

   private String getDefaultResourceString(String defaultValue)
   {
      if (defaultValue != null)
         return defaultValue;
      else
      {
         if (debugMode)
            System.out.println("MyJSQLView_ResourceBundle getDefaultResourceString()\n"
                               + "Undefined Resource Default Value.");

         return "";
      }
   }

   //==============================================================
   // Class Method for allowing classes to obtain a specified
   // image file to be used as an image icon.
   //==============================================================

   public ImageIcon getResourceImage(String imageFileName)
   {
      // Method Instances
      URL imageResourceURL = null;

      // Check some type of valid input.
      if (resourceURL != null && imageFileName != null)
      {
         // System.out.println("Image Resource: " + resourceURL.toExternalForm());
         
         try
         {
            //====
            // Handle resource from file & http locations.
            if (resourceType.equals(FILE_RESOURCE) || resourceType.equals(HTTP_RESOURCE))
            {  
               imageResourceURL = new URL(resourceURL.toExternalForm() + imageFileName);
               ImageIcon imageIcon = new ImageIcon(imageResourceURL);
               
               if (debugMode && (imageIcon.getIconWidth() == -1 || imageIcon.getIconHeight() == -1))
                  System.out.println("MyJSQLView_ResourceBundle getResourceImage()\n"
                                     + "Failed to find image file: " + imageResourceURL.getFile() + "\n");
               
               return imageIcon;
            }

            //====
            // Handle resource from a jar file.
            else if (resourceType.equals(JAR_RESOUCE))
            {
               try
               {
                  if (cacheJar && (resourceURL.toExternalForm().indexOf("jar:file:") == -1))
                     return new ImageIcon(getJAR_Resource(
                        imageFileName, new URL("jar:file:" + cachedJAR_FileName + "!/")));
                  else
                     return new ImageIcon(getJAR_Resource(imageFileName, resourceURL));
               }
               catch (Exception io_null_exception)
               {
                  if (debugMode)
                     System.out.println("MyJSQLView_ResourceBundle getResourceImage()\n"
                                        + "Failed to close resources or create cache URL.\n"
                                        + io_null_exception.toString());
                  return null;
               }
            }

            //====
            // Unknown
            else
            {
               displayErrors("MyJSQLView_ResourceBundle getResourceImage() \n"
                             + "Failed to identity URL protocol in order to process, , "
                             + resourceURL.getProtocol());
               return null;
            }

         }
         catch (MalformedURLException mfe)
         {
            if (debugMode)
               System.out.println("MyJSQLView_ResourceBundle getResourceImage()\n"
                                  + "Failed to Create Image URL.\n" + mfe.toString());
            return null;
         }
      }
      else
      {
         if (debugMode)
            System.out.println("MyJSQLView_ResourceBundle getResourceImage()\n"
                               + "Either Undefined resourceURL or image file.");

         return null;
      }
   }
   
   //==============================================================
   // Class Method for allowing classes to obtain a specified
   // image file to be used as an image icon.
   //==============================================================

   public Image getResourceFXImage(String imageFileName)
   {
      // Method Instances
      URL imageResourceURL = null;
      ByteArrayInputStream byteInputStream = null;

      // Check some type of valid input.
      if (resourceURL != null && imageFileName != null)
      {
         // System.out.println("Image Resource: " + resourceURL.toExternalForm());
         
         try
         {
            //====
            // Handle resource from file & http locations.
            if (resourceType.equals(FILE_RESOURCE) || resourceType.equals(HTTP_RESOURCE))
            {  
               imageResourceURL = new URL(resourceURL.toExternalForm() + imageFileName);
               Image image = new Image(resourceURL.toExternalForm() + imageFileName);
               
               
               if (debugMode && (image.getWidth() == -1 || image.getHeight() == -1))
                  System.out.println("LindyFrame_ResourceBundle getResourceFXImage()\n"
                                     + "Failed to find image file: " + imageResourceURL.getFile() + "\n");
               
               return image;
            }
            
            //====
            // Handle resource from a jar file.
            else if (resourceType.equals(JAR_RESOUCE))
            {
               try
               {
                  if (cacheJar && (resourceURL.toExternalForm().indexOf("jar:file:") == -1))
                  {
                     byteInputStream = new ByteArrayInputStream(getJAR_Resource(
                        imageFileName, new URL("jar:file:" + cachedJAR_FileName + "!/")));
                     return new Image(byteInputStream);
                  }
                  else
                  {
                     byteInputStream = new ByteArrayInputStream(getJAR_Resource(
                        imageFileName, resourceURL));
                     return new Image(byteInputStream);
                  }
               }
               catch (Exception io_null_exception)
               {
                  if (debugMode)
                     System.out.println("LindyFrame_ResourceBundle getResourceImage()\n"
                                        + "Failed to close resources or create cache URL.\n"
                                        + io_null_exception.toString());
                  return null;
               }
               finally
               {
                  try
                  {
                     if (byteInputStream != null)
                        byteInputStream.close();
                  }
                  catch (IOException ioe)
                  {
                     if (debugMode)
                        System.out.println("LindyFrame_ResourceBundle getResourceImage()\n"
                                           + "Failed to close resource.\n" + ioe.toString());
                  }
               }
            }

            //====
            // Unknown
            else
            {
               displayErrors("LindyFrame_ResourceBundle getResourceFXImage() \n"
                             + "Failed to identity URL protocol in order to process, , "
                             + resourceURL.getProtocol());
               return null;
            }
         }
         catch (MalformedURLException mfe)
         {
            if (debugMode)
               System.out.println("LindyFrame_ResourceBundle getResourceFXImage()\n"
                                  + "Failed to Create Image URL.\n" + mfe.toString());
            return null;
         }
      }
      else
      {
         if (debugMode)
            System.out.println("LindyFrame_ResourceBundle getResourceFXImage()\n"
                               + "Either Undefined resourceURL or image file.");
         return null;
      }
   }
   
   //==============================================================
   // Class Method for allowing classes to obtain a specified
   // file resource.
   //==============================================================

   public File getResourceFile(String fileName)
   {
      // Method Instances
      URL fileResourceURL = null;
      File fileResource;

      // Check some type of valid input.
      if (resourceURL != null && fileName != null)
      {
         // System.out.println("File Resource: " + resourceURL.toExternalForm());
            
         //====
         // Handle resource from file & http locations.
         if (resourceType.equals(FILE_RESOURCE))
         {  
            try
            {
               fileResourceURL = new URL(resourceURL.toExternalForm() + fileName);
               fileResource = new File(fileResourceURL.getFile());
            }
            catch (Exception e)
            {
               displayErrors("MyJSQLView_ResourceBundle getResourceFile() \n"
                             + "Failed to identity URL protocol in order to process, , "
                             + resourceURL.toExternalForm() + fileName);
               return null;
            }
            return fileResource;
         }
         
         //====
         // Does not resource from a HTTP or JARS files.
         else if (resourceType.equals(HTTP_RESOURCE) || resourceType.equals(JAR_RESOUCE))
         {
            JOptionPane.showMessageDialog(null, "MyJSQLView_ResourceBundle getResourceFile() Only Supports\n"
                                                + "Local File Resources!", "Alert", JOptionPane.ERROR_MESSAGE);
            return null;
         }

         //====
         // Unknown
         else
         {
            displayErrors("MyJSQLView_ResourceBundle getResourceFile() \n"
                          + "Failed to identity URL protocol in order to process, , "
                          + resourceURL.getProtocol());
            return null;
          }   
      }
      else
      {
         if (debugMode)
            System.out.println("MyJSQLView_ResourceBundle getResourceImage()\n"
                               + "Either Undefined resourceURL or image file.");

         return null;
      }
   }
   
   //==============================================================
   // Class Method for allowing classes to obtain a specified
   // resource via a byte array. Use this for local & remote
   // resources.
   //==============================================================

   public byte[] getResourceBytes(String resourceName)
   {
      // Method Instances
      URL byteResourceURL = null;
      InputStream inputStream;
      BufferedInputStream bufferedInputStream;
      int inSize;
      byte[] resourceBytes;

      // Check some type of valid input.
      if (resourceURL != null && resourceName != null)
      {
         // System.out.println("Byte Resource: " + resourceURL.toExternalForm());
         
         try
         {
            //====
            // Handle resource from file & http locations.
            if (resourceType.equals(FILE_RESOURCE) || resourceType.equals(HTTP_RESOURCE))
            {  
               byteResourceURL = new URL(resourceURL.toExternalForm() + resourceName);
               
               inputStream = null;
               bufferedInputStream = null;
               
               try
               {
                  if (resourceType.equals(FILE_RESOURCE))
                     inputStream = new FileInputStream(new File(resourceURL.toExternalForm()
                                                       + resourceName));
                  else
                  {
                     URLConnection urlConnection = byteResourceURL.openConnection();
                     inputStream = urlConnection.getInputStream();
                     
                  }
                  bufferedInputStream = new BufferedInputStream(inputStream);
                  inSize = bufferedInputStream.available();
                  resourceBytes = new byte[inSize];
                  
                  int i = 0;
                  
                  while (i < inSize)
                     resourceBytes[i++] = (byte) bufferedInputStream.read();
                  
                  return resourceBytes;
                  
               }
               catch (IOException ioe)
               {
                  if (MyJSQLView.getDebug())
                     System.out.println("MyJSQLView_ResourceBundle getResourceBytes() \n"
                                        + "Error Reading Resource. " + ioe.toString());
                  return null;
               }
               finally
               {
                  try
                  {
                     if (bufferedInputStream != null)
                        bufferedInputStream.close();
                  }
                  catch (IOException ioe)
                  {
                     if (MyJSQLView.getDebug())
                        System.out.println("MyJSQLView_ResourceBundle getResourceBytes() \n"
                                           + "Failed to Close BufferedInputStream. " + ioe.toString());
                  }
                  finally
                  {
                     try
                     {
                        if (inputStream != null)
                           inputStream.close();
                     }
                     catch (IOException ioe)
                     {
                        if (MyJSQLView.getDebug())
                           System.out.println("WriteDataFile writeDataFileText() \n"
                                              + "Failed to Close FileOutputStream. " + ioe.toString());
                     }     
                  }
               }
            }

            //====
            // Handle resource from a jar file.
            else if (resourceType.equals(JAR_RESOUCE))
            {
               try
               {
                  if (cacheJar && (resourceURL.toExternalForm().indexOf("jar:file:") == -1))
                     return getJAR_Resource(resourceName, new URL("jar:file:" + cachedJAR_FileName
                                                                        + "!/"));
                  else
                     return getJAR_Resource(resourceName, resourceURL);
               }
               catch (Exception io_null_exception)
               {
                  if (debugMode)
                     System.out.println("MyJSQLView_ResourceBundle getResourceBytes()\n"
                                        + "Failed to close resources or create cache URL.\n"
                                        + io_null_exception.toString());
                  return null;
               }
            }

            //====
            // Unknown
            else
            {
               displayErrors("MyJSQLView_ResourceBundle getResourceBytes() \n"
                             + "Failed to identity URL protocol in order to process, , "
                             + resourceURL.getProtocol());
               return null;
            }

         }
         catch (MalformedURLException mfe)
         {
            if (debugMode)
               System.out.println("MyJSQLView_ResourceBundle getResourceBytes()\n"
                                  + "Failed to Create Resource URL.\n" + mfe.toString());
            return null;
         }
      }
      else
      {
         if (debugMode)
            System.out.println("MyJSQLView_ResourceBundle getResourceBytes()\n"
                               + "Either Undefined resourceURL or resource name.");

         return null;
      }
   }
   
   //==============================================================
   // Class Method for allowing classes to obtain a specified
   // image icon that has already been stored from setImage().
   //==============================================================

   public ImageIcon getImage(String imageKey)
   {
      if (imagesData != null && imagesData.containsKey(imageKey))
         return imagesData.get(imageKey);
      else
      {
         if (debugMode)
            System.out.println("MyJSQLView_ResourceBundle getImage()\n"
                               + "Failed to find image key: " + imageKey);
         return null;
      }
   }

   //==============================================================
   // Class Method to set the locale aspects of the resource
   // bundle. The localeDirectory should reference where the locale
   // directory resource files are located. The baseName and
   // localeString is used to compose the specific file to use.
   //==============================================================

   public void setLocaleResource(String localeDirectory, String baseName, String localeString)
   {
      // Method Instances
      String localeFileName;

      if (resourceURL == null || baseName == null || localeString == null)
         return;

      // Process the given locale file to obtain a hashmap
      // of the key, resource pairs.

      localeFileName = localeDirectory + baseName + "_" + localeString + ".properties";

      try
      {
         if (resourceType.equals(FILE_RESOURCE))
         {
            createFile_LocaleResource((resourceURL.toExternalForm()).substring(
               resourceURL.toExternalForm().indexOf("file:") + 5) + localeFileName);
         }

         else if (resourceType.equals(HTTP_RESOURCE))
         {
            createHttp_LocaleResource(new URL(resourceURL.toExternalForm() + localeFileName));
         }

         else if (resourceType.equals(JAR_RESOUCE))
         {
            createJAR_LocaleResource(localeFileName);
         }

         // Unknown
         else
         {
            displayErrors("MyJSQLView_ResourceBundle setLocaleResource() \n"
                          + "Failed to identity URL protocol in order to process, , "
                          + resourceURL.getProtocol());
         }
      }
      catch (IOException ioe)
      {
         displayErrors("MyJSQLView_ResourceBundle setLocaleResource() \n" + "Failed to close " + resourceType
                       + " in process.\n" + ioe.toString());
      }
   }
   
   //==============================================================
   // Class Method to set images data. Allows the loading of image
   // resources at initialization of program rather then collect
   // as boot occurs over startup classes requests.
   //==============================================================

   public void setImage(String imageKey, String imageFileName)
   {
      if (imagesData == null)
         imagesData = new HashMap<String, ImageIcon>();
      
      imagesData.put(imageKey, getResourceImage(imageFileName));   
   }

   //==============================================================
   // Class Method to create the localization resource source from
   // the local file system.
   //==============================================================

   private void createFile_LocaleResource(String fileName) throws IOException
   {
      // Method Instances
      InputStream inputStream;

      inputStream = null;
      // System.out.println("Creating File Locale Resource.");

      try
      {
         inputStream = new FileInputStream(fileName);
         readLocaleResource(inputStream);
      }
      catch (IOException ioe)
      {
         displayErrors("MyJSQLView_ResourceBundle createFile_LocaleResource() \n"
                       + "Failed to process the given locale file, " + resourceURL.toExternalForm() + "\n"
                       + ioe.toString());
      }
      finally
      {
         if (inputStream != null)
            inputStream.close();
      }
   }

   //==============================================================
   // Class Method to create the localization resource source from
   // the network.
   //==============================================================

   private void createHttp_LocaleResource(URL url) throws IOException
   {
      // Method Instances
      HttpURLConnection urlConnection;
      InputStream inputStream;

      inputStream = null;
      // System.out.println("Creating Http Locale Resource.");

      try
      {
         urlConnection = (HttpURLConnection) url.openConnection();
         urlConnection.connect();

         inputStream = urlConnection.getInputStream();
         readLocaleResource(inputStream);
      }
      catch (IOException ioe)
      {
         displayErrors("MyJSQLView_ResourceBundle createHTTP_Resource() \n"
                       + "Failed to process the given locale http, " + resourceURL.toExternalForm() + "\n"
                       + ioe.toString());
      }
      finally
      {
         if (inputStream != null)
            inputStream.close();
      }
   }

   //==============================================================
   // Class Method to create the localization resource source from
   // the network that is packaged into a JAR file.
   //==============================================================

   private void createJAR_LocaleResource(String localeFileName) throws IOException
   {
      // Method Instances
      JarURLConnection jarURLConnection;
      JarFile jarFile;
      ZipEntry zipEntry;
      InputStream inputStream;

      // Setup
      jarFile = null;
      inputStream = null;

      try
      {
         if (cacheJar && (resourceURL.toExternalForm().indexOf("jar:file:") == -1))
         {
            URL cachedJarURL = new URL("jar:file:" + cachedJAR_FileName + "!/");
            jarURLConnection = (JarURLConnection) (cachedJarURL).openConnection();
         }
         else
            jarURLConnection = (JarURLConnection) resourceURL.openConnection();

         jarFile = jarURLConnection.getJarFile();
         
         zipEntry = jarFile.getEntry(localeFileName);

         // Try Brute Forceing It
         if (zipEntry == null)
         {
            for (Enumeration<?> entries = jarFile.entries(); entries.hasMoreElements();)
            {
               zipEntry = (ZipEntry) entries.nextElement();
               
               // Locale File Qualifier
               if (zipEntry.getName().endsWith(localeFileName))
               {
                  break;
               }
               else
                  zipEntry = null;
            }
         }

         // Resource found so process
         if (zipEntry != null)
         {
            inputStream = jarFile.getInputStream((zipEntry));
            readLocaleResource(inputStream);
         }
         else
         {
            displayErrors("MyJSQLView_ResourceBundle createJAR_LocaleResource() \n"
                          + "Failed to find the given locale file in JAR.\n" + localeFileName);
         }
      }
      catch (IOException ioe)
      {
         displayErrors("MyJSQLView_ResourceBundle createJAR_LocaleResource() \n"
                       + "Failed to process the given locale file, " + localeFileName + "\n"
                       + ioe.toString());
      }
      finally
      {
         if (inputStream != null)
            inputStream.close();

         if (jarFile != null)
            jarFile.close();
      }
   }

   //==============================================================
   // Class Method to read the input locale data given the input
   // stream from the selected resource type.
   //==============================================================

   private void readLocaleResource(InputStream inputStream) throws IOException
   {
      // Method Instances
      InputStreamReader inputStreamReader;
      BufferedReader bufferedReader;

      String currentEntry;
      String key, resource;

      // Setup
      inputStreamReader = null;
      bufferedReader = null;

      try
      {
         inputStreamReader = new InputStreamReader(inputStream, "UTF-16");
         bufferedReader = new BufferedReader(inputStreamReader);

         while ((currentEntry = bufferedReader.readLine()) != null)
         {
            currentEntry = currentEntry.trim();

            if (currentEntry.indexOf("=") != -1)
            {
               key = currentEntry.substring(0, currentEntry.indexOf("=")).trim();
               resource = currentEntry.substring(currentEntry.indexOf("=") + 1).trim();
               // System.out.println(key + " " + resource);

               localeListData.put(key, resource);
            }
         }
         bufferedReader.close();
         inputStreamReader.close();
      }
      catch (IOException ioe)
      {
         displayErrors("MyJSQLView_ResourceBundle readLocaleResource() \n"
                       + "Failed to process the given locale file, " + resourceURL.toExternalForm() + "\n"
                       + ioe.toString());
      }
      finally
      {
         if (bufferedReader != null)
            bufferedReader.close();

         if (inputStreamReader != null)
            inputStreamReader.close();
      }
   }

   //==============================================================
   // Class method to collect an image resource from a file
   // resource type.
   //==============================================================

   private byte[] getJAR_Resource(String resourceName, URL resourceURL) throws IOException
   {
      // Method Instances
      JarURLConnection jarURLConnection;
      JarFile jarFile;
      ZipEntry zipEntry;
      InputStream inputStream;
      int fileSize;
      byte[] resourceBytes;

      // Setup
      jarFile = null;
      inputStream = null;
      resourceBytes = null;

      try
      {
         // System.out.println("Resource JAR: " + imageResourceURL.toExternalForm());
         
         jarURLConnection = (JarURLConnection) resourceURL.openConnection();
         jarFile = jarURLConnection.getJarFile();

         zipEntry = jarFile.getEntry(resourceName);

         // Try Brute Forcing it.
         if (zipEntry == null)
         {
            for (Enumeration<?> entries = jarFile.entries(); entries.hasMoreElements();)
            {
               zipEntry = (ZipEntry) entries.nextElement();

               // Image File Qualifier
               if (zipEntry.getName().equals(resourceName))
                  break;
               else
                  zipEntry = null;
            }
         }

         // Resource found so process
         if (zipEntry != null)
         {
            inputStream = jarFile.getInputStream(zipEntry);

            fileSize = (int) zipEntry.getSize();

            // Obtain bytes
            if (fileSize != -1)
            {
               resourceBytes = new byte[fileSize];

               int readPosition = 0;
               int byteChunk = 0;

               while ((fileSize - readPosition) > 0)
               {
                  byteChunk = inputStream.read(resourceBytes, readPosition, fileSize - readPosition);
                  if (byteChunk == -1)
                     break;

                  readPosition += byteChunk;
               }
               return resourceBytes;
            }
            else
            {
               if (debugMode)
                  System.out.println("MyJSQLView_ResourceBundle getJAR_Resource()\n"
                                     + "Failed to Determine Resource Size: " + resourceName);
               return null;
            }
         }
         else
         {
            if (debugMode)
               System.out.println("MyJSQLView_ResourceBundle getJAR_Resource()\n"
                                  + "Resource Entry Not Found: " + resourceName);
            return null;
         }
      }
      catch (IOException ioe)
      {
         if (debugMode)
            System.out.println("MyJSQLView_ResourceBundle getJAR_Resource()\n"
                               + "Failed Processing of Resource from Jar: " + resourceName);
         return null;
      }
      finally
      {
         if (inputStream != null)
            inputStream.close();

         if (jarFile != null)
            jarFile.close();
      }
   }

   //==============================================================
   // Class Method to cache a JAR file if it is not a local file
   // system resource.
   //==============================================================

   private boolean cacheJAR(String resourceURLString) throws IOException
   {
      // Method Instances
      InputStream inputStream;
      BufferedInputStream inputBuffer;
      int inByte;

      File cacheDirectoryFile;
      FileOutputStream fileOutputStream;
      BufferedOutputStream fileOutputBuffer;

      boolean cached;

      // Setup
      cached = false;
      inputStream = null;
      inputBuffer = null;
      fileOutputStream = null;
      fileOutputBuffer = null;

      // Open a stream for the JAR then byte it to the
      // local cache file.
      try
      {
         inputStream = (new URL(resourceURLString)).openStream();
         inputBuffer = new BufferedInputStream(inputStream);

         cacheDirectoryFile = new File(cacheDirectory);

         if (!cacheDirectoryFile.isDirectory())
            if (!cacheDirectoryFile.mkdir())
               if (debugMode)
               {
                  System.out.println("MyJSQLView_ResourceBundle cacheJAR()\n"
                                     + "Failed to create Cache Directory");
                  return cached;
               }

         fileOutputStream = new FileOutputStream(cachedJAR_FileName);
         fileOutputBuffer = new BufferedOutputStream(fileOutputStream);

         while ((inByte = inputBuffer.read()) != -1)
            fileOutputBuffer.write(inByte);

         fileOutputBuffer.flush();
         cached = true;
      }
      catch (IOException ioe)
      {
         if (debugMode)
            System.out.println("MyJSQLView_ResourceBundle cacheJAR()\n"
                               + "Failed to Cache JAR.\n" + ioe.toString());
      }
      finally
      {
         if (fileOutputBuffer != null)
            fileOutputBuffer.close();

         if (fileOutputStream != null)
            fileOutputStream.close();

         if (inputBuffer != null)
            inputBuffer.close();

         if (inputStream != null)
            inputStream.close();
      }
      return cached;
   }

   //==============================================================
   // Class method to display an error to the standard output if
   // some type of resource creation/processing occured.
   //==============================================================

   private void displayErrors(String errorString)
   {
      JOptionPane.showMessageDialog(null, errorString, "Alert", JOptionPane.ERROR_MESSAGE);
   }
}
