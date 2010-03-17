//=================================================================
//                 TableFieldProfiler PlotterPanel
//=================================================================
//
//    This class provides a panel container where a vertical bar chart
// graphic can be created.
//
//                      << PlotterPanel.java >>
//
//=================================================================
// Copyright (C) 2006-2010 Vivek Singh, Dana M. Proctor
// Version 12.0 02/20/2010
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
// Version 1.0 2006 Original GNU PlotterPanel Class, By Vivek Singh
//                  Arrah Technology http://www.arrah.in
//         1.1 11/24/2009 Integration into MyJSQLView As TableFieldProfiler Plugin,
//                        Lots of Crappy Coding Cleanup.
//         1.2 11/25/2009 Additional Control of Graphics Colors, Class Instances
//                        valuesColor, histogramColor. Clean Up and Implementation
//                        in actionPerformed() & createPanelPopup(). Added Class
//                        Method menuItem().
//         1.3 11/25/2009 Set Existing Color On panelColorChooser For Each Popup
//                        Action That Involved a Color Change, actionPerformed().
//         1.4 11/28/2009 Class Instances scaleFactorX, scaleFactorY, titleColor,
//                        valuesColor, labelsColor, histogramColor, backgroundColor,
//                        & margin Declared As Protected.
//         1.5 11/29/2009 Removed static final Declaration for Class Instance margin.
//                        Set in Constructor, Code Comment Changes.
//         1.6 11/29/2009 Implemented a offScreenGraphicsImage Rendering Scheme for
//                        the Panel in Class Method drawBarChart(). Add Class Method
//                        checkImage().
//         1.7 11/30/2009 Added Instances barColor, barColor0-3, and horizontalPlotter.
//                        Modification of Method createPanelPopupMenu() to Handle
//                        HorizontalPlotter Classes Bar Color Control. Implemented
//                        in actionPerformed() the Setting of Same.
//         1.8 12/01/2009 Corrected Assignment of barColor[3] to barColor3 on Restore
//                        Defaults in actionPerformed().
//         1.9 02/18/2010 Changed Package to Reflect Dandy Made Productions Code.
//         2.0 02/20/2010 Added Class Instance lastSaveDirectory and Processing for it
//                        in the Constructor and actionPerformed() in Call to ImageUtil.
//                        
//-----------------------------------------------------------------
//                 danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.colorchooser.AbstractColorChooserPanel;

/**
 *    The PlotterPanel class provides a panel container where a vertical
 * pie chart graphic can be created.
 * 
 * @author Vivek Singh, Dana M. Proctor
 * @version 2.0 02/20/2010
 */

class PlotterPanel extends JPanel implements ActionListener, MouseListener
{
   // Class Instances.
   private static final long serialVersionUID = 7958711494933269734L;
   
   protected boolean init;
   protected String title, tableName, columnName;
   
   protected Point titlePosition;
   protected int panelWidth, panelHeight;
   protected Rectangle histogramRectangle;
   protected byte margin;
   
   protected String barLabels[];
   protected double barValues[];
   protected int scaledBarValues[];
   
   protected double zoomFactor;
   protected int barColorIndex;
   protected Color[] barColor;
   protected Color barColor0, barColor1, barColor2, barColor3;
   protected double scaleFactorX, scaleFactorY;
   protected Color titleColor, valuesColor, labelsColor;
   protected Color histogramColor, backgroundColor;
   protected Image offScreenGraphicsImage;
   protected boolean horizontalPlotter;
   private String actionCommand;
   private String lastSaveDirectory;
   
   private JPopupMenu panelPopupMenu;
   private JColorChooser panelColorChooser;
   
   //==============================================================
   // PlotterPanel Constructor
   //==============================================================
   
   protected PlotterPanel()
   {
      // Initializing.
      
      init = false;
      title = "Bar Chart";
      tableName = "";
      columnName = "";
      
      zoomFactor = 1.0;
      scaleFactorX = 1.0;
      scaleFactorY = 1.0;
      
      barColorIndex = 3;
      barColor0 = new Color(230, 165, 75);
      barColor1 = new Color(95, 190, 155);
      barColor2 = new Color(185, 220, 100);
      barColor3 = new Color(70, 200, 200);
      barColor = new Color[] {barColor0, barColor1, barColor2, barColor3};
      titleColor = new Color((Color.RED).getRGB());
      valuesColor = new Color((Color.BLUE).getRGB());
      labelsColor = new Color((Color.DARK_GRAY).getRGB());
      histogramColor = new Color((Color.GRAY).getRGB());
      
      backgroundColor = getBackground();
      horizontalPlotter = false;
      actionCommand = "";
      lastSaveDirectory = "";
      
      titlePosition = new Point(10, 20);
      panelWidth = getWidth();
      panelHeight = getHeight();
      margin = 25;
      histogramRectangle = new Rectangle(panelWidth - (2 * margin), panelHeight - (2 * margin));
      
      // Create a popup menu and color chooser to allow
      // control of graphics in panel and save the image
      // of the chart.
      
      createPanelPopupMenu();
      createColorChooser();
      
      addMouseListener(this);
      ToolTipManager.sharedInstance().registerComponent(this);
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
            drawBarChart();
            return;
         }
         
         // Scale Graphics Down.
         if (actionCommand.equals("Zoom Out"))
         {
            scaleFactorX -= 0.1;
            scaleFactorY -= 0.1;
            drawBarChart();
            return;
         }
         
         // Change Title, values, Labels, Histogram, or Background Color.
         if ((actionCommand.equals("Change Title")) ||  (actionCommand.equals("Change Values")) 
             || (actionCommand.equals("Change Labels")) || (actionCommand.equals("Change Histogram"))
             || (actionCommand.indexOf("Change HBar") != -1) || (actionCommand.equals("Change Background")))
         {
            // Get current color.
            if (actionCommand.equals("Change Title"))
               panelColorChooser.setColor(titleColor);
            else if (actionCommand.equals("Change Values"))
               panelColorChooser.setColor(valuesColor);
            else if (actionCommand.equals("Change Labels"))
               panelColorChooser.setColor(labelsColor);
            else if (actionCommand.equals("Change Histogram"))
               panelColorChooser.setColor(histogramColor);
            else if (actionCommand.indexOf("Change HBar") != -1)
            {
               int index = Integer.parseInt(actionCommand.substring(actionCommand.length() - 1));
               panelColorChooser.setColor(barColor[index]);
            }
            else
               panelColorChooser.setColor(backgroundColor);
            
            JDialog dialog;
            dialog = JColorChooser.createDialog(this, "Title Color", true, panelColorChooser, this, null);
            dialog.setVisible(true);
            dialog.dispose();
            return;
         }
         
         // Change Bar Color.
         if (actionCommand.indexOf("VBar") != -1)
         {
            if (actionCommand.indexOf("Red") != -1)
               barColorIndex = 0;
            else if (actionCommand.indexOf("Green") != -1)
               barColorIndex = 1;
            else if (actionCommand.indexOf("Yellow") != -1)
               barColorIndex = 2;
            else if (actionCommand.indexOf("Blue") != -1)
               barColorIndex = 3;
            else
               barColorIndex = 4;
            
            drawBarChart();
            return;
         }
         
         // Restore Defaults.
         if (actionCommand.equals("Restore Defaults"))
         {
            scaleFactorX = 1.0;
            scaleFactorY = 1.0;
            titleColor = new Color((Color.RED).getRGB());
            valuesColor = new Color((Color.BLUE).getRGB());
            barColorIndex = 3;
            barColor[0] = barColor0;
            barColor[1] = barColor1;
            barColor[2] = barColor2;
            barColor[3] = barColor3;
            labelsColor = new Color((Color.DARK_GRAY).getRGB());
            histogramColor = new Color((Color.GRAY).getRGB());
            backgroundColor = getParent().getBackground();
            setBackground(backgroundColor);
            
            drawBarChart();
            return;
         }
         
         // Save Image
         if (actionCommand.equals("Save Image"))
         {
            ImageUtil plotterImageUtil = new ImageUtil(this, lastSaveDirectory, "png");
            lastSaveDirectory = plotterImageUtil.getLastSaveDiretory();
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
               drawBarChart();
            }
            
            if (actionCommand.equals("Change Values"))
            {
               valuesColor = panelColorChooser.getColor();
               drawBarChart();
            }
            
            if (actionCommand.equals("Change Labels"))
            {
               labelsColor = panelColorChooser.getColor();
               drawBarChart();
            }
            
            if (actionCommand.equals("Change Histogram"))
            {
               histogramColor = panelColorChooser.getColor();
               drawBarChart();
            }
            
            if (actionCommand.indexOf("Change HBar") != -1)
            {
               int index = Integer.parseInt(actionCommand.substring(actionCommand.length() - 1));
               barColor[index] = panelColorChooser.getColor();
               drawBarChart();
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

   protected void showPopup(MouseEvent e)
   {
      if (e.isPopupTrigger())
      {
         panelPopupMenu.show(e.getComponent(), e.getX(), e.getY());
      }
   }
   
   //==============================================================
   // Class Method to create a popup menu for the panel to control
   // various aspects of the bar chart.
   //==============================================================

   protected void createPanelPopupMenu()
   {
      // Method Instances.
      JMenu barColorMenu;
      
      // Create popup.
      panelPopupMenu = new JPopupMenu();

      // Zoom In/Out.
      panelPopupMenu.add(menuItem("Zoom In", "Zoom In"));
      panelPopupMenu.add(menuItem("Zoom Out", "Zoom Out"));
      
      panelPopupMenu.addSeparator();
      
      // Color Control.
      panelPopupMenu.add(menuItem("Title Color", "Change Title"));
      panelPopupMenu.add(menuItem("Data Values Color", "Change Values"));
      
      barColorMenu = new JMenu("Bar Color");
      
      if (horizontalPlotter)
      {
         barColorMenu.add(menuItem("0", "Change HBar 0"));
         barColorMenu.add(menuItem("1", "Change HBar 1"));
         barColorMenu.add(menuItem("2", "Change HBar 2"));
         barColorMenu.add(menuItem("3", "Change HBar 3")); 
      }
      else
      {
         barColorMenu.add(menuItem("Red", "VBar Red"));
         barColorMenu.add(menuItem("Green", "VBar Green"));
         barColorMenu.add(menuItem("Yellow", "VBar Yellow"));
         barColorMenu.add(menuItem("Blue", "VBar Blue")); 
      }
      panelPopupMenu.add(barColorMenu);
      
      panelPopupMenu.add(menuItem("Labels Color", "Change Labels"));
      panelPopupMenu.add(menuItem("Histogram Color", "Change Histogram"));
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
   // Class Method to create a color chooser used to control various
   // colors used in the bar chart.
   //==============================================================

   protected void createColorChooser()
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
   // Class Method to be called by classes in the package to draw
   // the bar chart.
   //==============================================================
   
   protected void drawBarChart()
   {
      if (!init)
         return;
      
      Graphics g = getGraphics();
      
      if (g == null)
         return;
      else
         drawBarChart(g);
   }
   
   //==============================================================
   // Class Method to actually draw the graphics involved with
   // the bar chart in the panel.
   //==============================================================

   protected void drawBarChart(Graphics g)
   {
      // Method Instances.
      Graphics2D g2d, imageGraphics;
      
      if (!init)
         return;
      
      if (g == null)
         return;
      else
      {
         // Create a 2D graphics image object to work with and
         // collect panel sizing parameters.
         
         g2d = (Graphics2D) g;
         g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
         
         panelWidth = getWidth();
         panelHeight = getHeight();
         histogramRectangle = new Rectangle(panelWidth - (2 * margin), panelHeight - (2 * margin));
         
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
            
            // Draw bar chart to offscreen.
            showTitle(imageGraphics);
            drawgridForHistogram(imageGraphics);
            scaleValuesUniformly();
            drawBars(imageGraphics);
            showDataLabels(imageGraphics);
            showBarLabels(imageGraphics);
            
            // Render to panel.
            g2d.drawImage(offScreenGraphicsImage, 0, 0, this);
            
            imageGraphics.dispose();
         }
         g2d.dispose();
      }
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
   // Class Method to override the nomral paintComponents() for 
   // the component to insure the drawBarChart() routine is called
   // to perform the paint activity.
   //==============================================================
   
   protected void paintComponent(Graphics g)
   {
      super.paintComponent(g);
      drawBarChart(g);
   }
   
   //==============================================================
   // Class Method to draw the chart title and current selected
   // table name & column name.
   //==============================================================

   protected void showTitle(Graphics2D g)
   {
      // Method Instances
      Font titleFont;
      
      // Setting up font.
      titleFont = new Font("Helvetika", Font.BOLD, 14);
      
      // Draw title and table, column name information.
      g.setFont(titleFont);
      g.setColor(titleColor);
      g.drawString(title, titlePosition.x, titlePosition.y);
      
      g.setColor(labelsColor);
      g.drawString(tableName + " : " + columnName, titlePosition.x, titlePosition.y + 20);
   }
   
   //==============================================================
   // Class Method to draw the bar charts histogram, to include
   // outline and horizontal & vertical grid scale. 
   //==============================================================
   
   protected void drawgridForHistogram(Graphics2D g)
   {
      // Method Instances
      int histogramWidthScale, histogramHeightScale;
      int histogramWidthIncrement, histogramHeightIncrement;
      
      // No sense drawing with out this.
      if (barLabels == null || barLabels.length == 0)
         return;
      
      // Setting up.
      histogramWidthScale = barLabels.length;
      histogramHeightScale = 10;
      histogramWidthIncrement = histogramRectangle.width / histogramWidthScale;
      histogramHeightIncrement = histogramRectangle.height / histogramHeightScale;
      
      // Draw the outline.
      g.setColor(histogramColor);
      g.drawRect(margin, (margin + titlePosition.y),
                 histogramRectangle.width, histogramRectangle.height - titlePosition.y);
      
      // Draw the histogram grid.
      
      // Draw the vertical historgram increments.
      for (int j1 = 1; j1 < histogramWidthScale; j1++)
      {
         int x = margin + j1 * histogramWidthIncrement;
         g.drawLine(x, margin + titlePosition.y, x, panelHeight - margin);
      }

      // Draw the horizontal historgram increments.
      for (int k1 = 0; k1 < histogramHeightScale; k1++)
      {
         int y = (panelHeight - margin) - (k1 * histogramHeightIncrement);
         g.drawLine(margin, y, panelWidth - margin, y);
      }
   }
   
   //==============================================================
   // Class Method to normalize the data values to the histogram.
   //==============================================================
   
   protected void scaleValuesUniformly()
   {
      // Method Instances.
      double maxBarValue, scale, barScale;
      
      // Initialize.
      maxBarValue = max(barValues);
      barScale = 0.0;
      scale = 1.0;
      
      // Scale the max bar value to normalize to the histogram.
      barScale = maxBarValue / (double) (histogramRectangle.height - 50);
      scale = barScale * zoomFactor;
      
      // Fill in the data to the normalized scale.
      for (int j = 0; j < scaledBarValues.length; j++)
         scaledBarValues[j] = scale == 0.0 ? (int) (25D + barValues[j]) : (int) (25D + barValues[j] / scale);
   }
   
   //==============================================================
   // Class Method to draw the bar graphics.
   //==============================================================
   
   private void drawBars(Graphics2D g2D)
   {
      // Method Instances.
      int baseLine;
      int barWidth;
      int barHeight;
      int currentBarPosition;
      
      if (scaledBarValues.length == 0)
         return;
      
      // Setup baseline, y, position and bar width.
      baseLine = panelHeight - margin;
      barWidth = histogramRectangle.width / scaledBarValues.length;
      g2D.setColor(Color.BLACK);
      
      // Draw vertical bars.
      for (int i = 0; i < scaledBarValues.length; i++)
      {
         currentBarPosition = margin + (i * barWidth) + (barWidth / 4);
         barHeight = panelHeight - scaledBarValues[i];
         
         // Shaded.
         g2D.setPaint(Color.LIGHT_GRAY);
         g2D.fillRect(currentBarPosition, (barHeight + 2), (barWidth / 2) + 5, (baseLine - barHeight));
         
         // Outline
         g2D.setPaint(getPaint(barColorIndex));
         g2D.fillRect(currentBarPosition, barHeight, (barWidth / 2), (baseLine - barHeight));
         
         // Bar
         g2D.setPaint(Color.BLACK);
         g2D.drawRect(currentBarPosition, barHeight, (barWidth / 2), (baseLine - barHeight));
      }
   }
   
   //==============================================================
   // Class Method to draw the bar values above the bar graphics.
   //==============================================================
   
   protected void showDataLabels(Graphics2D g)
   {
      // Method Instances.
      Font dataLabelFont;
      int barWidth;
      int currentPosition;
      int labelHeight;
      
      if (scaledBarValues.length == 0)
         return;
      
      // Setup font, horizontal positioning and drawing color.
      
      dataLabelFont = new Font("Helvetika", Font.BOLD, 12);
      g.setFont(dataLabelFont);
      g.setColor(valuesColor);
      barWidth = histogramRectangle.width / scaledBarValues.length;
      
      // Cycle through the values and draw above each graphic
      // bar.
      
      for (int i = 0; i < scaledBarValues.length; i++)
      {
         currentPosition = margin + (i * barWidth) + barWidth / 4;
         labelHeight = panelHeight - scaledBarValues[i] - 2;
         
         if (barValues[i] < 0.0D)
            g.drawString(" N/A", currentPosition, panelHeight - 50);
         else
            g.drawString(String.valueOf(Math.round(barValues[i])),
                 (currentPosition + barWidth / 4) - (String.valueOf(Math.round(barValues[i])).length() * 4),
                 labelHeight);
      }
   }
   
   //==============================================================
   // Class Method to draw the bar chart labels.
   //==============================================================
   
   protected void showBarLabels(Graphics2D g)
   {
      // Method Instances.
      Font barLabelFont;
      FontMetrics fontMetrics;
      int barWidth, labelPosition_Y;
      int currentLabelPosition;
      
      if (barLabels.length == 0)
         return;
      
      // Setup to draw the labels.
      
      labelPosition_Y = panelHeight - 5;
      barWidth = histogramRectangle.width / barLabels.length;
      
      barLabelFont = new Font("Helvetika", Font.BOLD, 12);
      fontMetrics = getFontMetrics(barLabelFont);
      g.setFont(barLabelFont);
      g.setColor(labelsColor);
      
      // Cycle through the labels and draw under the
      // appropriate bar.
      
      for (int i = 0; i < barLabels.length; i++)
      {
         currentLabelPosition = margin + (i * barWidth);
         
         // Handle trying to center label under bar.
         
         // Standard Position.
         if (fontMetrics.stringWidth(barLabels[i]) < (barWidth - 10))
         {
            g.drawString(barLabels[i], (currentLabelPosition + ((3 * barWidth) / 8)), labelPosition_Y);
         }
         // Larger label display, than panel width, Abreviate.
         else
         {
            String minS = "..";
            String newV = minS;
            int min = fontMetrics.stringWidth(minS);
            int strI = 0;
            
            while (min + fontMetrics.charWidth(barLabels[i].charAt(strI)) < (barWidth - 10))
            {
               newV = barLabels[i].substring(0, ++strI) + minS;
               min = fontMetrics.stringWidth(newV);
               if (strI >= barLabels[i].length())
                  break;
            }
            g.drawString(newV, currentLabelPosition + 10, labelPosition_Y);
         }
      }
   }
   
   //==============================================================
   // Class Method to draw a tool tip about each bar value's
   // statistics.
   //==============================================================

   public String getToolTipText(MouseEvent mouseevent)
   {
      // Method Instances.
      int x, y;
      int barWidth;
      int currentIndexedValue;
      double barValuesSum;
      double binSum;
      
      // See if mouse position with histogram.
      x = mouseevent.getX();
      y = mouseevent.getY();
      
      if (!histogramRectangle.contains(new Point(x, (y - titlePosition.y))))
         return null;
      
      // If mouse position in histogram and valid data 
      // proceed to displaying statistices.
      
      if (barLabels != null && barLabels.length != 0)
      {
         // Collect the aggregate sum of values.
         barValuesSum = 0.0;
         for (int j1 = 0; j1 < barLabels.length; j1++)
            barValuesSum += barValues[j1];
         
         if (barValuesSum == 0.0D)
            return null;
         else
         {
            barWidth = histogramRectangle.width / barLabels.length;
            currentIndexedValue = -1;
            binSum = 0.0D;

            // Move through values summing bin and deteriming
            // which bar is being hovered over.
            
            int i = 0;
            do
            {
               binSum += barValues[i];
               if (x >= margin + i * barWidth && x < margin + (i + 1) * barWidth)
               {
                  currentIndexedValue = i;
                  break;
               }
               i++;
            }
            while (i < barValues.length);
            
            // Display tooltip if resulting data collected.
            
            if (currentIndexedValue == -1)
               return null;
            else
            {
               String s1 = "<html><B><I>";
               String s2 = Long.toString(Math.round(barValues[currentIndexedValue]));
               s1 = s1 + "Bin Count=  " + s2 + "  Total Count= " + barValuesSum + "<BR>";
               String s3 = Double.toString(nDigitChop(((barValues[currentIndexedValue] * 100D)
                                           / barValuesSum), 3));
               s1 = s1 + "Bin %age=  " + s3 + "%  ";
               String s4 = Double.toString(nDigitChop(((binSum * 100D) / barValuesSum), 3));
               s1 = s1 + "Cumulative %age=  " + s4 + "% </I></B><html>";
               return s1;
            }
         }
      }
      else
         return null;
   }
   
   //==============================================================
   // Class methods to perform standard search for the max and
   // min values in int and double arrays.
   //==============================================================
   
   protected static int max(int ai[])
   {
      if (ai == null || ai.length == 0)
         return 0;
      int i = ai[0];
      for (int j = 0; j < ai.length; j++)
         if (ai[j] > i)
            i = ai[j];

      return i;
   }

   protected static double max(double ad[])
   {
      if (ad == null || ad.length == 0)
         return 0.0D;
      double d = ad[0];
      for (int i = 0; i < ad.length; i++)
         if (ad[i] > d)
            d = ad[i];

      return d;
   }

   protected static int min(int ai[])
   {
      if (ai == null || ai.length == 0)
         return 0;
      int i = ai[0];
      for (int j = 0; j < ai.length; j++)
         if (ai[j] < i)
            i = ai[j];

      return i;
   }

   protected static double min(double ad[])
   {
      if (ad == null || ad.length == 0)
         return 0.0D;
      double d = ad[0];
      for (int i = 0; i < ad.length; i++)
         if (ad[i] < d)
            d = ad[i];

      return d;
   }
   
   //==========================================================
   // Class method for performing the n-digit chopping operation
   // on the input double.
   //==========================================================
   
   protected static double nDigitChop(double mij, int n)
   {
      int decimal;
      int chop = n;
      String pass_string;
      
      pass_string = Double.toString(mij);
      decimal = pass_string.indexOf(".");
      if (decimal != -1 & pass_string.length() >= decimal+chop+1)
      {  
         mij = (Double.valueOf(pass_string.substring(0,decimal+chop+1))).doubleValue();
      }
      else
         mij = Double.valueOf(pass_string).doubleValue();
      
      return mij;
   }
   
   //==============================================================
   // Class Method to create a paint gradient for the bars.
   //==============================================================
   
   protected Paint getPaint(int i)
   {
      switch (i)
      {
      case 0: // '\0'
         return new GradientPaint(0.0F, 0.0F, new Color(255, 0, 0),
                                  0.0F, (panelHeight / 2.0F), new Color(255, 200, 200), true);

      case 1: // '\001'
         return new GradientPaint(0.0F, 0.0F, new Color(0, 255, 0),
                                  0.0F, (panelHeight / 2.0F), new Color(200, 255, 200), true);

      case 2: // '\002'
         return new GradientPaint(0.0F, 0.0F, new Color(255, 255, 0),
                                  0.0F, (panelHeight / 2.0F), new Color(255, 255, 200), true);

      case 3: // '\003'
         return new GradientPaint(0.0F, 0.0F, new Color(0, 0, 255),
                                  0.0F, (panelHeight / 2.0F), new Color(200, 200, 255), true);
      }
      return Color.black;
   }
   
   //==============================================================
   // Class get/setter Methods used to gain access to the classes
   // instances.
   //==============================================================
   
   protected boolean getInit()
   {
      return init;
   }
   
   protected void setInit()
   {
      init = true;
   }
   
   protected void setTitle(String name)
   {
      title = name;
   }
   
   protected void setTableName(String name)
   {
      tableName = name;
   }

   protected void setColumnName(String name)
   {
      columnName = name;
   }
   
   protected int getMargin()
   {
      return margin;
   }
   
   protected String[] getbarLabels()
   {
      return barLabels;
   }
   
   protected void setbarLabels(String as[])
   {
      barLabels = as;
   }

   protected void setbarLabels(Object aobj[])
   {
      barLabels = new String[aobj.length];
      for (int i = 0; i < aobj.length; i++)
         barLabels[i] = aobj[i].toString();

   }
   
   protected double[] getBarValues()
   {
      return barValues;
   }

   protected void setBarValues(double ad[])
   {
      barValues = new double[ad.length];
      scaledBarValues = new int[ad.length];
      for (int i = 0; i < ad.length; i++)
         barValues[i] = ad[i];
   }

   protected void setBarValues(Object aobj[])
   {
      barValues = new double[aobj.length];
      scaledBarValues = new int[aobj.length];
      for (int i = 0; i < aobj.length; i++)
         barValues[i] = ((Number) aobj[i]).doubleValue();

   }
   
   protected void setBarColorIndex(int index)
   {
      barColorIndex = index;
   }

   protected double getZoomFactor()
   {
      return zoomFactor;
   }

   protected void setZoomFactor(double value)
   {
      zoomFactor = value;
   }
}