//=================================================================
//                  TableFieldProfiler ImageUtil
//=================================================================
//
//   This class provides a means to save a JComponent Object graphics
// as png image.    
//
//                   << ImageUtil.java >>
//
//=================================================================
// Copyright (C) 2005-2015 Dana M. Proctor
// Version 2.3 09/11/2012
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
//This class provides a generic panel in the appearance of
// a form for selecting the CSV data export options.
//=================================================================
// Revision History
// Changes to the code should be documented here and reflected
// in the present version number. Author information should
// also be included with the original copyright author.
//=================================================================
// Version 1.0 2006 Original GNU ImageUtil Class, By Vivek Singh
//                  Arrah Technology http://www.arrah.in
//         1.1 11/09/2009 Integration in MyJSQLView As TableFieldProfiler Plugin,
//                        Lots of Crappy Coding Cleanup.
//         1.2 11/15/2009 Class Save Image Commented Out the Setting of the
//                        fileChooser.setCurrentDirectory() So That the 
//                        Current Directory Defaults to the User's.
//         1.3 02/18/2010 Changed Package to Reflect Dandy Made Productions Code.
//         1.4 02/20/2010 Added Class Instance lastSaveDirectory, and Processing
//                        in Constructor and saveImage() Method. Added getter/Setter
//                        Methods for the Instance.
//         1.5 04/20/2010 Made Class public Along With the Method getLastSaveDirectory()
//                        & setLastSaveDirectory().
//         1.6 08/26/2010 Added Class Instance resourceBundle and Implemented
//                        Internationalization.
//         1.7 01/27/2011 Copyright Update.
//         1.8 02/13/2011 Changed in Constructor and Method saveImage() of JComponent
//                        to Component. Check of component Moved to Just Constructor
//                        and Check for Width & Height.
//         1.9 01/01/2012 Copyright Update.
//         2.0 03/25/2012 Change to Insure a Passed Component in saveImage() is Not
//                        NUll Before Proceeding. Also Addition of Commented Code
//                        That May Reduce the Possibility of Throwning a Heap Memory
//                        Error.
//         2.1 07/08/2012 Changes in Way MyJSQLView_ResourceBundle Handles the Collection
//                        of Resource Strings. Change to resource.getResourceString(key,
//                        default).
//         2.2 08/06/2012 MyJSQLView Class Method Change of getLocaleResourceBundle()
//                        to getResourceBundle().
//         2.3 09/11/2012 Changed Package Name to com.dandymadeproductions.myjsqlview.utilities.
//                        
//-----------------------------------------------------------------
//                 danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview.utilities;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import com.dandymadeproductions.myjsqlview.MyJSQLView;

/**
 *    The ImageUtil class provides a means to save a JComponent Object graphics
 * as png image.
 * 
 * @author Vivek Singh, Dana M. Proctor
 * @version 2.3 09/11/2012
 */

public class ImageUtil
{
   // Class Instances.
   private String lastSaveDirectory = "";
   private MyJSQLView_ResourceBundle resourceBundle;
   
   //==============================================================
   // ImageUtil Constructor
   //==============================================================

   public ImageUtil(Component component, String lastSaveDirectory, String imageType)
   {
      // Do some checking and setup.
      if (component == null || component.getWidth() == 0 || component.getHeight() == 0)
         return;
      
      if (lastSaveDirectory == null)
         this.lastSaveDirectory = "";
      else
         this.lastSaveDirectory = lastSaveDirectory;
      
      resourceBundle = MyJSQLView.getResourceBundle();
      
      // Process.
      saveImage(component, imageType);
   }

   //==============================================================
   // ImageUtil Constructor
   //==============================================================

   private void saveImage(Component component, String itype)
   {
      // Method Instances
      int componentWidth, componentHeight, fileChooserResult;
      JFileChooser fileChooser;
      String resourceTitle, resourceMessage;
      File savedFile;

      Graphics2D g2;
      GraphicsEnvironment graphicsEnvironment;
      GraphicsDevice graphicsDevice;
      GraphicsConfiguration graphicsConfiguration;
      BufferedImage bufferedImage;
      
      // Check to see if any graphics to save.
      if (component == null)
         return;

      // Create a dialog for the user to save the image
      // file to a directory.

      if (lastSaveDirectory.equals(""))
         fileChooser = new JFileChooser();
      else
         fileChooser = new JFileChooser(new File(lastSaveDirectory));

      resourceTitle = resourceBundle.getResourceString("ImageUtil.dialogtitle.PNGImageSaveFile",
                                                       "Save PNG Image File");
      fileChooser.setDialogTitle(resourceTitle);
      fileChooser.setFileFilter(new PNG_FileFilter());

      fileChooserResult = fileChooser.showSaveDialog(null);

      // Proceed on selection and confirmation of saving the
      // file.

      if (fileChooserResult == JFileChooser.APPROVE_OPTION)
      {
         // Save the selected directory and file name so can be used again.
         lastSaveDirectory = fileChooser.getCurrentDirectory().toString();
         
         savedFile = fileChooser.getSelectedFile();

         // Check to see if file ends with .png, and
         // renaming if have to.
         if (savedFile.getName().toLowerCase().endsWith(".png") == false)
         {
            try
            {
               File renameFile = new File(savedFile.getAbsolutePath() + ".png");
               savedFile = renameFile;
            }
            catch (SecurityException se)
            {
               resourceMessage = resourceBundle.getResourceString("ImageUtil.dialogmessage.ImageSaveError",
                                                                  "Image Save Error");
                
               JOptionPane.showMessageDialog(null, se.getMessage(), resourceMessage,
                                                JOptionPane.ERROR_MESSAGE);
               return;
            }
         }

         // Confirm overwriting to existing file.
         if (savedFile.exists())
         {
            resourceMessage = resourceBundle.getResourceString(
               "ImageUtil.dialogmessage.OverwriteExistingFile", "Overwrite existing file?");
            resourceTitle = resourceBundle.getResourceString("ImageUtil.dialogtitle.ConfirmOverwrite",
                                                             "Confirm Overwite");
            
            int response = JOptionPane.showConfirmDialog(null, resourceMessage, resourceTitle,
                                                         JOptionPane.OK_CANCEL_OPTION,
                                                         JOptionPane.QUESTION_MESSAGE);
            if (response == JOptionPane.CANCEL_OPTION)
               return;
         }

         // Setting up and collecting components graphics and drawing
         // into a buffered image.

         componentWidth = component.getWidth();
         componentHeight = component.getHeight();
         
         // This maybe an alternative to the creation of a Graphics
         // Environment that may throw a heap memory error on createGraphics().
         /*
         bufferedImage = new BufferedImage(componentWidth, componentHeight, BufferedImage.TYPE_INT_RGB);
         g2 = bufferedImage.createGraphics();
         component.paint(g2);
         */

         graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
         graphicsDevice = graphicsEnvironment.getDefaultScreenDevice();
         graphicsConfiguration = graphicsDevice.getDefaultConfiguration();
         bufferedImage = graphicsConfiguration.createCompatibleImage(componentWidth, componentHeight,
                                                                     Transparency.BITMASK);
         g2 = bufferedImage.createGraphics();
         component.paint(g2);

         // Attempt to save image.
         try
         {
            ImageIO.write(bufferedImage, "png", savedFile);
         }
         catch (Exception exp)
         {
            resourceMessage = resourceBundle.getResourceString("ImageUtil.dialogmessage.ImageSaveError",
                                                               "Image Save Error");
            
            JOptionPane.showMessageDialog(null, exp.getMessage(), resourceMessage,
                                          JOptionPane.ERROR_MESSAGE);
         }
         g2.dispose();
      }
      else
         return;
   }

   //==============================================================
   // ImageUtil PNG File Filter Inner Class
   //==============================================================

   private static class PNG_FileFilter extends FileFilter
   {
      public boolean accept(File file)
      {
         return file.getName().toLowerCase().endsWith(".png") || file.isDirectory();
      }

      public String getDescription()
      {
         return "PNG image  (*.png) ";
      }
   }
   
   //==============================================================
   // Getter/Setter Methos for Class Instance lastSaveDirectory.
   //==============================================================
   
   public String getLastSaveDiretory()
   {
      return lastSaveDirectory;
   }
   
   public void setLastSaveDirectory(String lastSave)
   {
      if (lastSave != null)
         lastSaveDirectory = lastSave;
   }  
}
