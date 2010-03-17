//=================================================================
//                  TableFieldPlotter PiePanel
//=================================================================
//
//    This class provides a panel container where a pie chart graphic
// can be created.
//
//                        << PiePanel.java >>
//
//=================================================================
// Copyright (C) 2006-2010 Vivek Singh, Dana M. Proctor
// Version 2.2 02/20/2010
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
// Version 1.0 2006 Original GNU PiePanel Class, By Vivek Singh
//                  Arrah Technology http://www.arrah.in
//         1.1 11/09/2009 Integration into MyJSQLView As TableFieldProfiler
//                        Plugin, Lots of Crappy Coding Cleanup.
//         1.2 11/15/2009 Modification of Graphics Creation in Class Method
//                        drawPieChart() to Clarify Code and Allow Easier
//                        Ability to Translate to 2D Graphics in Future.
//         1.3 11/20/2009 Moved panelPopopMenu Creation to Constructor Added
//                        Zooms, Background Color, and Restore Defaults.
//                        Added Class Instances panelColorChooser, scaleFactorX,
//                        scaleFactorY, and backgroundColor.
//         1.4 11/21/2009 Moved Creation of Popup Menu and Color Chooser to
//                        New Class Methods createPanelPopupMenu() and 
//                        createColorChooser(), Respectively. Class Methods
//                        setLabel() Changed to setLabels().
//         1.5 11/24/2009 Class Method createPanelPopupMenu() Add Menu Items,
//                        Title Color, & Labels Color and Event Selector to 
//                        Handle Those Changes in actionPerformed(). Added Class
//                        Instances titleColor, labelsColor, and actionCommand.
//                        Set in Constructor().
//         1.6 11/24/2009 Comment Changes. Added Class Instance minValue and Its
//                        Corresponding Setter Method. Implemented Changes to Label
//                        to Reflect Clarification in What the Chart Indicates, in
//                        Class Method drawPieChart().
//         1.7 11/25/2009 Cleaned Up Code in Creating Popup Menu, createPanelPopupMenu().
//                        Added Class Method menuItem(). Set Existing Color On
//                        panelColorChooser For Each Popup Action That Involved a
//                        Color Change, actionPerformed().
//         1.8 11/26/2009 Minor Changes in Argument Names in getter/setter Methods.
//         1.9 11/29/2009 Implemented a offScreenGraphicsImage Rendering Scheme for
//                        the Panel in Class Method drawPieChart(). Add Class Method
//                        checkImage().
//         2.0 11/30/2009 Removed getParent.repaint() in actionPerformed(). Added
//                        Class Instances sliceColors, and sliceColor0-5. Implemented
//                        Change of Pie Slice Colors in createPanelPopupMenu() and
//                        actionPerformed(). Initialized in Constructor.
//         2.1 02/18/2010 Changed Package to Reflect Dandy Made Productions Code.
//         2.2 02/20/2010 Added Class Instance lastSaveDirectory and Processing for it
//                        in the Constructor and actionPerformed() in Call to ImageUtil.
//
//-----------------------------------------------------------------
//                 danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.colorchooser.*;
import java.util.regex.Pattern;

/**
 *    The PiePanel class provides a panel container where a pie
 * chart graphic can be created.
 * 
 * @author Vivek Singh, Dana M. Proctor
 * @version 2.2 02/18/2010
 */

class PiePanel extends JPanel implements ActionListener, MouseListener
{
   // Class Instances.
   private static final long serialVersionUID = -3180507188778607635L;

   private String legendLabels[];
   private double pieSliceValues[];
   private String title, tableName, columnName;
   private String minValue;
   
   private double scaleFactorX, scaleFactorY;
   private Color titleColor, labelsColor, backgroundColor;
   private Color[] sliceColors;
   private Color sliceColor0, sliceColor1, sliceColor2;
   private Color sliceColor3, sliceColor4, sliceColor5;
   private Image offScreenGraphicsImage;
   private String actionCommand;
   private String lastSaveDirectory;
   
   private JPopupMenu panelPopupMenu;
   private JColorChooser panelColorChooser;

   //==============================================================
   // PiePanel Constructor
   //==============================================================

   protected PiePanel()
   {
      // Setup Pie Chart Names.
      
      title = "Distribution Chart";
      tableName = "";
      columnName = "";
      
      scaleFactorX = 1.0;
      scaleFactorY = 1.0;
      titleColor = new Color((Color.RED).getRGB());
      labelsColor = new Color((Color.DARK_GRAY).getRGB());
      sliceColor0 = new Color(255, 255, 123); 
      sliceColor1 = new Color(231, 123, 255); 
      sliceColor2 = new Color(255, 139, 75); 
      sliceColor3 = new Color(0, 176, 255); 
      sliceColor4 = new Color(146, 236, 84); 
      sliceColor5 = new Color(255, 43, 97);
      sliceColors = new Color[] {sliceColor0, sliceColor1, sliceColor2, sliceColor3,
                                 sliceColor4, sliceColor5};
      backgroundColor = getBackground();
      actionCommand = "";
      lastSaveDirectory = "";
      minValue = "";
      
      
      // Create a popup menu and color chooser to allow
      // control of graphics in panel and save the image
      // of the chart.
      
      createPanelPopupMenu();
      createColorChooser();
      
      addMouseListener(this); 
   }

   //==============================================================
   // ActionEvent Listener method for detecting the inputs from the
   // panel and directing to the appropriate routine.
   //==============================================================

   public void actionPerformed(ActionEvent evt)
   {
      Object panelSource = evt.getSource();
      
      if (panelSource instanceof JMenuItem)
      {
         actionCommand = ((JMenuItem) panelSource).getActionCommand();
         
         // Scale Graphics Up
         if (actionCommand.equals("Zoom In"))
         {
            scaleFactorX += 0.1;
            scaleFactorY += 0.1;
            drawPieChart();
            return;
         }
         
         // Scale Graphics Down.
         if (actionCommand.equals("Zoom Out"))
         {
            scaleFactorX -= 0.1;
            scaleFactorY -= 0.1;
            drawPieChart();
            return;
         }
         
         // Change Title, Labels, or Background Color.
         if ((actionCommand.equals("Change Title")) || (actionCommand.equals("Change Labels"))
               || (actionCommand.indexOf("Change Pie") != -1) || (actionCommand.equals("Change Background")))
         {
            // Get current color.
            if (actionCommand.equals("Change Title"))
               panelColorChooser.setColor(titleColor);
            else if (actionCommand.equals("Change Labels"))
               panelColorChooser.setColor(labelsColor);
            else if (actionCommand.indexOf("Change Pie") != -1)
            {
               int index = Integer.parseInt(actionCommand.substring(actionCommand.length() - 1));
               panelColorChooser.setColor(sliceColors[index]);
            }
            else
               panelColorChooser.setColor(backgroundColor);
            
            JDialog dialog;
            dialog = JColorChooser.createDialog(this, "Title Color", true, panelColorChooser, this, null);
            dialog.setVisible(true);
            dialog.dispose();
            return;
         }
         
         // Restore Defaults.
         if (actionCommand.equals("Restore Defaults"))
         {
            scaleFactorX = 1.0;
            scaleFactorY = 1.0;
            titleColor = new Color((Color.RED).getRGB());
            labelsColor = new Color((Color.DARK_GRAY).getRGB());
            sliceColors[0] = sliceColor0;
            sliceColors[1] = sliceColor1;
            sliceColors[2] = sliceColor2;
            sliceColors[3] = sliceColor3;
            sliceColors[4] = sliceColor4;
            sliceColors[5] = sliceColor5;
            backgroundColor = getParent().getBackground();
            setBackground(backgroundColor);
            drawPieChart();
            return;
         }
         
         // Save Image
         if (actionCommand.equals("Save Image"))
         {
            ImageUtil pieImageUtil = new ImageUtil(this, lastSaveDirectory, "png");
            lastSaveDirectory = pieImageUtil.getLastSaveDiretory();
            return;
         }
      }
      
      // JColorChooser OK Action.
      if (panelSource instanceof JButton)
      {
         if (!actionCommand.equals(""))
         {
            if (actionCommand.equals("Change Title"))
            {
               titleColor = panelColorChooser.getColor();
               drawPieChart();
            }
            
            if (actionCommand.equals("Change Labels"))
            {
               labelsColor = panelColorChooser.getColor();
               drawPieChart();
            }
            
            if (actionCommand.indexOf("Change Pie") != -1)
            {
               int index = Integer.parseInt(actionCommand.substring(actionCommand.length() - 1));
               sliceColors[index] = panelColorChooser.getColor();
               drawPieChart();
            }
               
            if (actionCommand.equals("Change Background"))
            {
               backgroundColor = panelColorChooser.getColor();
               setBackground(backgroundColor);
            }
            actionCommand = "";
         }
      }
   }
   
   //==============================================================
   // MouseEvent Listener methods for detecting mouse events.
   // MounseListner Interface requirements.
   //==============================================================

   public void mouseEntered(MouseEvent evt)
   {
      // Do Nothing.
   }

   public void mouseExited(MouseEvent evt)
   {
      // Do Nothing.
   }

   public void mousePressed(MouseEvent evt)
   {
      showPopup(evt);
   }

   public void mouseReleased(MouseEvent evt)
   {
      showPopup(evt);
   }

   public void mouseClicked(MouseEvent e)
   {
      // Do Nothing
   }

   //==============================================================
   // Class method to show a popup menu.
   //==============================================================

   private void showPopup(MouseEvent e)
   {
      if (e.isPopupTrigger())
      {
         panelPopupMenu.show(e.getComponent(), e.getX(), e.getY());
      }
   }
   
   //==============================================================
   // Class Method to create a popup menu for the panel to control
   // various aspects of the pie chart.
   //==============================================================

   private void createPanelPopupMenu()
   {  
      // Method Instances.
      JMenu pieColorMenu;
      
      // Create popup.
      panelPopupMenu = new JPopupMenu();

      // Zoom In/Out.
      panelPopupMenu.add(menuItem("Zoom In", "Zoom In"));
      panelPopupMenu.add(menuItem("Zoom Out", "Zoom Out"));
      
      panelPopupMenu.addSeparator();
      
      // Color Control.
      panelPopupMenu.add(menuItem("Title Color", "Change Title"));
      panelPopupMenu.add(menuItem("Labels Color", "Change Labels"));
      
      pieColorMenu = new JMenu("Pie Slice Color");
      
      pieColorMenu.add(menuItem("0", "Change Pie 0"));
      pieColorMenu.add(menuItem("1", "Change Pie 1"));
      pieColorMenu.add(menuItem("2", "Change Pie 2"));
      pieColorMenu.add(menuItem("3", "Change Pie 3"));
      pieColorMenu.add(menuItem("4", "Change Pie 4"));
      pieColorMenu.add(menuItem("5", "Change Pie 5"));
      
      panelPopupMenu.add(pieColorMenu);
      
      panelPopupMenu.add(menuItem("Background Color", "Change Background"));
      
      panelPopupMenu.addSeparator();
      
      // Restoration.
      panelPopupMenu.add(menuItem("Restore Defaults", "Restore Defaults"));
      
      panelPopupMenu.addSeparator();
      
      // Image Saving.
      panelPopupMenu.add(menuItem("Save as Image", "Save Image"));
   }
   
   //==============================================================
   // Instance method used for the creation of menu bar items.
   // ==============================================================

   protected JMenuItem menuItem(String label, String actionLabel)
   {
      JMenuItem item = new JMenuItem(label);
      item.addActionListener(this);
      item.setActionCommand(actionLabel);
      return item;
   }
   
   //==============================================================
   // Class Method to create a color chooser used to control the
   // pie chart's title, label, & background color.
   //==============================================================

   private void createColorChooser()
   {
      // Method Instances.
      AbstractColorChooserPanel[] colorChooserPanels;
      
      // Create color chooser.
      panelColorChooser = new JColorChooser();
      panelColorChooser.setBorder(BorderFactory.createTitledBorder("Background Color"));
      panelColorChooser.setColor(backgroundColor);
      colorChooserPanels = panelColorChooser.getChooserPanels();
      panelColorChooser.removeChooserPanel(colorChooserPanels[0]);
      panelColorChooser.removeChooserPanel(colorChooserPanels[2]);
      panelColorChooser.setPreviewPanel(new JPanel()); 
   }
   
   //==============================================================
   // Class Methods to be called by classes in the package to draw
   // the pie chart.
   //==============================================================

   protected void drawPieChart()
   {
      // Method Instances.
      Graphics g;
      
      // Collect graphics then draw the pie chart.
      g = getGraphics();
      
      if (g == null)
         return;
      else
         drawPieChart(g);
   }

   //==============================================================
   // Class Methods to actually draw the graphics involved with
   // the pie chart in the panel.
   //==============================================================

   private void drawPieChart(Graphics g)
   {
      // Method Instances.
      Graphics2D g2d, imageGraphics;
      int panelWidth, panelHeight;
      Point titlePosition;
      Point piePosition, legendPosition;
      int pieWidth, pieHeight;
      int pieChartScaleFactorX, pieChartScaleFactorY;
      byte legendSymbolWidth, legendSymbolHeight;
      Font titleFont, labelFont;
      
      int startAngle, arcAngle;
      double slicesSum;
      double pieSliceSum;
      
      int colorIndex;
      
      // Check to see if valid input.
      if (g == null || pieSliceValues == null)
         return;

      // ==================================
      // Setup various required variables.
      
      g2d = (Graphics2D) g;
      g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      
      panelWidth = getWidth();
      panelHeight = getHeight();
      pieChartScaleFactorX = 2;
      pieChartScaleFactorY = 3;
      
      titlePosition = new Point(10, 20);
      titleFont = new Font("Helvetika", Font.BOLD, 14);
      
      pieWidth = (panelWidth / pieChartScaleFactorX) + 100;
      pieHeight = (panelHeight / pieChartScaleFactorY) + 60;
      piePosition = new Point((panelWidth / 2) - (pieWidth / 2), (panelHeight / 3) - (pieHeight / 3));
      
      legendPosition = new Point(titlePosition.x, pieHeight + 100);
      legendSymbolWidth = 20;
      legendSymbolHeight = 18;
      labelFont = new Font("Helvetika", Font.BOLD, 12);
      
      slicesSum = 0.0;
      for(int k = 0; k < pieSliceValues.length; k++)
         slicesSum += pieSliceValues[k];

      // Create a offscreen graphics.
      if (checkImage(new Dimension(panelWidth, panelHeight)))
      {
         imageGraphics = (Graphics2D) offScreenGraphicsImage.getGraphics();
         imageGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                        RenderingHints.VALUE_ANTIALIAS_ON);
         
         // Clear & Scale.
         imageGraphics.setColor(backgroundColor);
         imageGraphics.fillRect(0, 0, panelWidth, panelHeight);
         imageGraphics.scale(scaleFactorX, scaleFactorY);
         
         // ==========================================
         // Draw chart title and corresponding table,
         // field names.
         
         imageGraphics.setFont(titleFont);
         
         imageGraphics.setColor(titleColor);
         imageGraphics.drawString(title, titlePosition.x, titlePosition.y);
         
         imageGraphics.setColor(labelsColor);
         imageGraphics.drawString(tableName + " : " + columnName, titlePosition.x, titlePosition.y + 20);
         
         imageGraphics.setFont(labelFont);
         
         // ===============================
         // Draw the pie chart and legend.
         
         startAngle = 0;
         arcAngle = 0;
         pieSliceSum = 0.0;
         
         imageGraphics.setColor(Color.LIGHT_GRAY);
         imageGraphics.fillArc((piePosition.x + 2), (piePosition.y + 2), (pieWidth + 2), (pieHeight + 2), 0, 360);
         imageGraphics.setColor(Color.BLACK);
         imageGraphics.drawArc((piePosition.x + 2), (piePosition.y + 2), (pieWidth + 2), (pieHeight + 2), 0, 360);
         
         // Cycle through drawing the pie and label graphics.
         
         colorIndex = 0;
         for(int i = 0; i < pieSliceValues.length; i++)
         {
             startAngle += arcAngle;
             arcAngle = (int) ((pieSliceValues[i] / slicesSum) * 360);
             
             if(i == pieSliceValues.length - 1)
                 arcAngle = 360 - startAngle;
             
             // Draw individual pie slice and it legend symbol
             // identifier.
             
             imageGraphics.setColor(sliceColors[colorIndex++]);
             
             imageGraphics.fillArc(piePosition.x, piePosition.y, pieWidth, pieHeight, startAngle, arcAngle);
             imageGraphics.fillRect(legendPosition.x, (legendPosition.y + (i * legendSymbolHeight)),
                                    legendSymbolWidth, legendSymbolHeight);
             
             imageGraphics.setColor(Color.BLACK);
             
             imageGraphics.drawArc(piePosition.x, piePosition.y, pieWidth, pieHeight, startAngle, arcAngle);
             imageGraphics.drawRect(legendPosition.x, (legendPosition.y + (i * legendSymbolHeight)),
                                           legendSymbolWidth, legendSymbolHeight);
             
             // Draw the slices label identifier.
             
             imageGraphics.setColor(labelsColor);
             
             if (i == 0)
             {
                if (Pattern.matches("\\d*", legendLabels[i]))
                   imageGraphics.drawString("0 < (" + Math.round(pieSliceValues[i]) + ") <= "
                                            + legendLabels[i], legendPosition.x + 30,
                                            legendPosition.y + 14 +(i * legendSymbolHeight));
                else
                   imageGraphics.drawString(minValue + " < (" + Math.round(pieSliceValues[i])
                                            + ") <= " + legendLabels[i], legendPosition.x + 30,
                                            legendPosition.y + 14 +(i * legendSymbolHeight));
             }
             else
                imageGraphics.drawString(legendLabels[i-1] + " < (" + Math.round(pieSliceValues[i])
                                         + ") <= " + legendLabels[i], legendPosition.x + 30,
                                         legendPosition.y + 14 +(i * legendSymbolHeight));
             
             pieSliceSum += pieSliceValues[i];
             
             if(colorIndex == sliceColors.length)
                 colorIndex = 0;
         }
         
         // Render to panel.
         g2d.drawImage(offScreenGraphicsImage, 0, 0, this);
         
         imageGraphics.dispose();
      }
      g2d.dispose();
   }
   
   //==============================================================
   // Class method to setup a offscreen image.
   //==============================================================

   protected boolean checkImage(Dimension d)
   {
      if (d.width == 0 || d.height == 0)
         return false;
      if (offScreenGraphicsImage == null || offScreenGraphicsImage.getWidth(null) != d.width
          || offScreenGraphicsImage.getHeight(null) != d.height)
      {
         offScreenGraphicsImage = createImage(d.width, d.height);
      }
      return true;
   }

   //==============================================================
   // Class method to overide the standard panel paintComponents
   // routine.
   //==============================================================
   
   protected void paintComponent(Graphics g)
   {
      super.paintComponent(g);
      drawPieChart(g);
   }
   
   //==============================================================
   // Class setter methods to set up the title, and legend labels
   // for the pie chart.
   //==============================================================

   protected void setTitle(String titleString)
   {
      title = titleString;
   }

   protected void setTableName(String name)
   {
      tableName = name;
   }

   protected void setColumnName(String name)
   {
      columnName = name;
   }

   protected void setLabels(String labels[])
   {
      legendLabels = labels;
   }

   protected void setLabels(Object objectLabels[])
   {
      legendLabels = new String[objectLabels.length];

      for (int i = 0; i < objectLabels.length; i++)
         legendLabels[i] = objectLabels[i].toString();
   }

   protected void setSliceValues(double slices[])
   {
      pieSliceValues = slices;
   }

   protected void setSliceValues(Object slicesObject[])
   {
      pieSliceValues = new double[slicesObject.length];
      for (int i = 0; i < slicesObject.length; i++)
        pieSliceValues[i] = ((Number) slicesObject[i]).doubleValue();
   }
   
   protected void setMinValue(String fieldMinValue)
   {
      minValue = fieldMinValue;
   } 
}