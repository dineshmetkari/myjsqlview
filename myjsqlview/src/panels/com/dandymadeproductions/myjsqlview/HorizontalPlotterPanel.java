//=================================================================
//            TableFieldProfiler HorizontalPlotterPanel
//=================================================================
//
//    This class provides a panel container where a horizontal bar
// chart graphic can be created.
//
//               << HorizontalPlotterPanel.java >>
//
//=================================================================
// Copyright (C) 2006-2010 Vivek Singh, Dana M. Proctor
// Version 1.5 02/18/2010
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
// Version 1.0 2006 Original GNU HorizontalPlotterPanel Class, By Vivek Singh
//                  Arrah Technology http://www.arrah.in
//         1.1 11/28/2009 Integration into MyJSQLView As TableFieldProfiler
//                        Plugin, Lots of Crappy Coding Cleanup.
//         1.2 11/29/2009 Implemented a offScreenGraphicsImage Rendering Scheme for
//                        the Panel in Class Method drawBarChart().
//         1.3 11/30/2009 Removed Method Instance ca for Bar Colors in drawBars().
//                        Instance Replaced With Class Instance barColors in Parent.
//                        Constructor set horizontalPlotter & createPanelPopupMenu().
//         1.4 12/04/2009 Increased Class Instance margin to 45.
//         1.5 02/18/2010 Changed Package to Reflect Dandy Made Productions Code.
//
//-----------------------------------------------------------------
//                 danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

/**
 *    The HorizontalPlotterPanel class provides a panel container where a horizontal
 * pie chart graphic can be created.
 * 
 * @author Vivek Singh, Dana M. Proctor
 * @version 1.5 02/18/2010
 */

class HorizontalPlotterPanel extends PlotterPanel implements MouseListener
{
   // Class Methods
   private static final long serialVersionUID = -896582493062385151L;
   
   private int barWidth = 20;
   private String minValue, maxValue;
   private Vector[] compositeData;

   //==============================================================
   // HorizontalPlotterPanel Constructor
   //==============================================================
   
   public HorizontalPlotterPanel()
   {
      minValue = "";
      maxValue = "";
      horizontalPlotter = true;
      createPanelPopupMenu();
      margin = 45;
   }
   
   //==============================================================
   // Class Methods to be called by classes in the package to draw
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
      int barNumber;
      Vector[] reCompositedData = {new Vector(), new Vector()};
      Graphics2D g2d, imageGraphics;
      
      if (!init || g == null)
         return;
      
      // Collect panel sizing parameters.
      
      panelWidth = getWidth();
      panelHeight = getHeight();
      histogramRectangle = new Rectangle(panelWidth - (2 * margin), panelHeight - (2 * margin));
      
      // Limiting bar content according to the sizing
      // of the height of the panel.
      
      barNumber = histogramRectangle.height / barWidth;
      
      reCompositedData[0] = new Vector(compositeData[0]);
      reCompositedData[1] = new Vector(compositeData[1]);
      
      if (reCompositedData[1].size() > barNumber)
      {
         double d = 0.0D;
         double d1 = 0.0D;
         int i;
         for (; reCompositedData[0].size() > (barNumber -1); reCompositedData[1].removeElementAt(i))
         {
            d1++;
            i = reCompositedData[0].size() - 1;
            d += ((Double) reCompositedData[1].elementAt(i)).doubleValue();
            reCompositedData[0].removeElementAt(i);
         }

         reCompositedData[0].add((barNumber - 1), "Others(" + Math.round(d1) + ")");
         reCompositedData[1].add((barNumber - 1), new Double(d));
      }
      setbarLabels(reCompositedData[0].toArray());
      setBarValues(reCompositedData[1].toArray());
      
      // Issue that value data to plot then proceed to
      // drawing the graphics.
      
      if (barLabels.length == 0)
         return;
      else
      {
         // Setup a 2D graphics object to work with.
         
         g2d = (Graphics2D) g;
         g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
         
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
            showMinMaxValues(imageGraphics);
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
   // Class Method to draw the field minimum and maximum values.
   //==============================================================

   private void showMinMaxValues(Graphics2D g)
   {
      // Method Instances
      Font titleFont;
      FontMetrics labelsFontMetrics;
      int minPosition, maxPosition;
      
      // Setting up font.
      titleFont = new Font("Helvetika", Font.BOLD, 12);
      labelsFontMetrics = getFontMetrics(titleFont);
      minPosition = titlePosition.x + labelsFontMetrics.stringWidth(tableName +  " : " + columnName) + 50;
      maxPosition = minPosition + labelsFontMetrics.stringWidth("Min. Value : " + minValue + 20);
      
      // Draw label and min/max values.
      g.setFont(titleFont);
      g.setColor(labelsColor);
      g.drawString("Min. Value : ", minPosition, titlePosition.y + 20);
      g.drawString("Max. Value : ", maxPosition, titlePosition.y + 20);
      
      g.setColor(valuesColor);
      g.drawString(minValue, minPosition + labelsFontMetrics.stringWidth("Min. Value : "),
                   titlePosition.y + 20);
      g.drawString(maxValue, maxPosition + labelsFontMetrics.stringWidth("Max. Value : "),
                   titlePosition.y + 20);
   }
    
   //==============================================================
   // Class Method to draw the bar charts histogram outline. 
   //==============================================================

   protected void drawgridForHistogram(Graphics2D g)
   {
      g.setColor(histogramColor);
      g.drawRect(margin, (margin + titlePosition.y),
                 histogramRectangle.width, histogramRectangle.height);
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
      barScale = 0;
      scale = 1.0;

      barScale = maxBarValue / (double) (histogramRectangle.width - 5);
      scale = barScale * zoomFactor;

      for (int i = 0; i < scaledBarValues.length; i++)
         scaledBarValues[i] = scale != 0 ? (int) (barValues[i] / scale) : (int) (barValues[i]);
   }
   
   //==============================================================
   // Class Method to draw the bar graphics.
   //==============================================================

   private void drawBars(Graphics2D graphics)
   {
      // Method Instances.
      int baseLine, yPos;
      
      if (scaledBarValues.length == 0)
         return;
      
      // Setup the vertical positioning.
      baseLine = panelHeight - margin;

      // Draw horizontal bars.
      
      int ci = 0;
      for (int i = 0; i < scaledBarValues.length; i++)
      {
         yPos = baseLine - (i * barWidth);
         
         graphics.setColor(barColor[ci++]);
         graphics.fillRect(margin, yPos, scaledBarValues[i], barWidth);
         
         graphics.setColor(Color.BLACK);
         graphics.drawRect(margin, yPos, scaledBarValues[i], barWidth);
         if (ci == barColor.length)
            ci = 0;
      }
   }
   
   //==============================================================
   // Class Method to draw the bar values on the bar graphics.
   //==============================================================
   
   protected void showDataLabels(Graphics2D g)
   {
      // Method Instances.
      Font dataLabelFont;
      int baseLine, yPos;
      
      if (scaledBarValues.length == 0)
         return;
      
      // Setup font, vertical positioning and drawing color.
      
      dataLabelFont = new Font("Helvetika", Font.BOLD, 12);
      g.setFont(dataLabelFont);
      g.setColor(labelsColor);
      baseLine = (panelHeight + barWidth) - margin;

      // Cycle through the values and draw on top of the
      // horizontal bars.
      
      for (int i = 0; i < scaledBarValues.length; i++)
      {
         yPos = baseLine -  (i * barWidth) - (barWidth / 4);
         
         if (barValues[i] < 0)
            g.drawString(" N/A", 5, yPos);
         else
            g.drawString(String.valueOf(Math.round(barValues[i])),
                         margin - 3 - String.valueOf(Math.round(barValues[i])).length() * 8,
                         yPos);
      }
   }
   
   //==============================================================
   // Class Method to draw the bar chart labels.
   //==============================================================
   
   protected void showBarLabels(Graphics2D g)
   {
      // Method Instances.
      Font barLabelFont;
      int baseLine, yPos;
      
      if (barLabels.length == 0)
         return;
      
      // Setup font, vertical positioning and drawing color.
      
      barLabelFont = new Font("Helvetika", Font.BOLD, 12);
      g.setFont(barLabelFont);
      g.setColor(valuesColor);
      baseLine = (panelHeight + barWidth) - margin;

      // Cycle through the values and draw to the left of
      // the horizontal bars.

      for (int i = 0; i < barLabels.length; i++)
      {
         yPos = baseLine -  (i * barWidth) - (barWidth / 4);
         
         g.drawString(barLabels[i], margin + 5, yPos);
      }
   }
   
   //==============================================================
   // Class get/setter Methods used to gain access to the classes
   // instances.
   //==============================================================

   protected void setData(Vector[] data)
   {
      compositeData = data;
   }
   
   protected void setMinValue(String fieldMinValue)
   {
      minValue = fieldMinValue;
   }
   
   protected void setMaxValue(String fieldMaxValue)
   {
      maxValue = fieldMaxValue;
   }
   
   //==============================================================
   // Class Method to overide the parent class, PlotterPanel, get
   // ToolTipText. Don't draw any.
   //==============================================================
   
   public String getToolTipText(MouseEvent e)
   {
      return null;
   }
}