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
// Copyright (C) 2006-2010 Vivek Singh, Dana M. Proctor
// Version 1.5 04/20/2010
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
//                        
//-----------------------------------------------------------------
//                 danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;

/**
 *    The ImageUtil class provides a means to save a JComponent Object graphics
 * as png image.
 * 
 * @author Vivek Singh, Dana M. Proctor
 * @version 1.5 04/20/2010
 */

public class ImageUtil
{
   // Class Instances.
   private String lastSaveDirectory = "";
   
   //==============================================================
   // ImageUtil Constructor
   //==============================================================

   public ImageUtil(JComponent component, String lastSaveDirectory, String imageType)
   {
      // Do some checking and setup.
      if (component == null)
         return;
      
      if (lastSaveDirectory == null)
         this.lastSaveDirectory = "";
      else
         this.lastSaveDirectory = lastSaveDirectory;
      
      // Process.
      saveImage(component, imageType);
   }

   //==============================================================
   // ImageUtil Constructor
   //==============================================================

   private void saveImage(JComponent component, String itype)
   {
      // Method Instances
      int componentWidth, componentHeight;
      JFileChooser fileChooser;
      int fileChooserResult;
      File savedFile;

      Graphics2D g2;
      GraphicsEnvironment graphicsEnvironment;
      GraphicsDevice graphicsDevice;
      GraphicsConfiguration graphicsConfiguration;
      BufferedImage bufferedImage;

      // Check for valid component
      if (component == null)
         return;

      // Create a dialog for the user to save the image
      // file to a directory.

      if (lastSaveDirectory.equals(""))
         fileChooser = new JFileChooser();
      else
         fileChooser = new JFileChooser(new File(lastSaveDirectory));

      fileChooser.setDialogTitle("PNG Image Save File");
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
               JOptionPane.showMessageDialog(null, se.getMessage(), "Image Save Error.",
                  JOptionPane.ERROR_MESSAGE);
               return;
            }
         }

         // Confirm overwriting to existing file.
         if (savedFile.exists())
         {
            int response = JOptionPane.showConfirmDialog(null, "Overwrite existing file?",
               "Confirm Overwrite", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (response == JOptionPane.CANCEL_OPTION)
               return;
         }

         // Setting up and collecting components graphics and drawing
         // into a buffered image.

         componentWidth = component.getWidth();
         componentHeight = component.getHeight();

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
            JOptionPane.showMessageDialog(null, exp.getMessage(), "Image Save Error.",
               JOptionPane.ERROR_MESSAGE);
         }
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