//=================================================================
//                   MyJSQLView Blossom
//=================================================================
//
//    This class provides a means to generate a blooming flower
// object along with drawing to a graphics space.
//
//                   << Blossom.java >>
//
//=================================================================
// Copyright (C) 2012 Dana M. Proctor
// Version 1.0 03/31/2012
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
// Version 1.0 Initial Outlined Blossom Class.
//                           
//-----------------------------------------------------------------
//                 danap@dandymadeproductions.com
//=================================================================

package com.dandymadeproductions.myjsqlview;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Dimension2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

/**
 *    The Blossom class provides a means to generate a blooming flower
 * object along with drawing to a graphics space.     
 * 
 * @author Dana M. Proctor
 * @version 1.0 03/31/2012
 */

class Blossom
{
   // Class Instances.
   private Point2D position;
   private Dimension2D petalSize;
   private double rotationAngle;
   private Stroke stroke;
   private Paint outlinePaint;
   private Paint fillPaint;
   
   private boolean petalFilled;
   private double symbol_XPosition, symbol_YPosition;
   
   private static float STROKE_WIDTH = 1.0f;
   private static double DEFAULT_WIDTH = 20.0;
   private static double DEFAULT_HEIGHT = 50.0;
   private static double DEFAULT_ANGLE = Math.PI/4.0;
   private static Paint DEFAULT_COLOR = Color.ORANGE;
   private static Paint DEFAULT_FILL = Color.BLACK;
   
   
   //==============================================================
   // Blossom Constructors
   //==============================================================
   
   protected Blossom()
   {
      this(null, null);
   }
   
   protected Blossom(Point2D position, Dimension2D petalSize)
   {
      this(position, petalSize, DEFAULT_ANGLE,
           new BasicStroke(STROKE_WIDTH, BasicStroke.CAP_SQUARE,
           BasicStroke.JOIN_BEVEL), DEFAULT_COLOR, DEFAULT_FILL);
   }
   
   protected Blossom(Point2D position, Dimension2D petalSize, Paint outlinePaint)
   {
      this(position, petalSize, DEFAULT_ANGLE,
           new BasicStroke(STROKE_WIDTH, BasicStroke.CAP_SQUARE,
           BasicStroke.JOIN_BEVEL), outlinePaint, DEFAULT_FILL);
   }

   protected Blossom(Point2D position, Dimension2D petalSize, double rotationAngle, Stroke stroke,
                     Paint outlinePaint, Paint fillPaint)
   {
      // Asign Position
      if (position == null)
      {
         position = new Point();
         position.setLocation(0.0, 0.0);
      }
      this.position = position;
      
      // Asign Petal Size
      if (petalSize == null)
      {
         petalSize = new Dimension();
         petalSize.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
         
      }
      this.petalSize = petalSize;
      
      // Asign Rotation
      this.rotationAngle = rotationAngle;
      
      // Asign Stroke
      if (stroke == null)
         stroke = new BasicStroke(STROKE_WIDTH, BasicStroke.CAP_SQUARE,
                                  BasicStroke.JOIN_BEVEL);
      this.stroke = stroke;
      
      // Asign Draw Paint
      if (outlinePaint == null)
         outlinePaint = DEFAULT_COLOR;
      this.outlinePaint = outlinePaint;
      
      // Asign Fill Paint
      if (fillPaint == null)
         fillPaint = DEFAULT_FILL;
      this.fillPaint = fillPaint;
      
      // Default to Fill
      petalFilled = true;
      
      symbol_XPosition = 50;
      symbol_YPosition = 0;
   }

   //==============================================================
   // Class Method to actually draw the graphics involved with
   // blossom.
   //==============================================================
   
   protected void draw(Graphics2D g2)
   {
      // Method Instances
      float alpha;
      AffineTransform affineTransform;
      AlphaComposite alphaComposite;
      Ellipse2D.Double petal;
      double petalWidth, petalLength;
      
      Area area;
      
      // Setup to draw the blossom.
      
      alpha = 0.9F;
      alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
      petalWidth = petalSize.getWidth();
      petalLength = petalSize.getHeight();
      
      // Draw decorative blossom.
      
      g2.setComposite(alphaComposite);
      
      petal = new Ellipse2D.Double(position.getX(), position.getY(), petalWidth, petalLength);
      petal.setFrame(symbol_XPosition, symbol_YPosition, petalWidth, petalLength);
      area = new Area(petal);
      
      g2.setStroke(stroke);
      
      if (petalFilled)
      {
         g2.setPaint(fillPaint);
         g2.fill(petal);
      }
      
      g2.setPaint(outlinePaint);
      g2.draw(petal);
      
      affineTransform = AffineTransform.getRotateInstance(rotationAngle,
                                                       symbol_XPosition + petalWidth / 2.0,
                                                       symbol_YPosition + petalLength);
      // Drawing the symbol.
      for (int i = 0; i < 7; i++)
      {
         g2.draw(area);
         g2.transform(affineTransform);
         area.exclusiveOr(new Area(affineTransform.createTransformedShape(petal)));
         affineTransform.rotate(rotationAngle,
                             symbol_XPosition + petalWidth / 2.0,
                             symbol_YPosition + petalLength);
      }
      rotationAngle += Math.PI / 8.0;
      symbol_XPosition += 1.0;
      symbol_YPosition += 1.0;
      
      // Restore the original transformation.
      affineTransform.setToIdentity();
      g2.setTransform(affineTransform);
   }
   
   //==============================================================
   // Class Methods to get the various characteristics of the
   // blossom.
   //==============================================================
   
   protected Point2D getPosition()
   {
      return position;
   }
   
   protected Dimension2D getPetalSize()
   {
      return petalSize;
   }
   
   protected double getRotationAngle()
   {
      return rotationAngle;
   }
   
   protected Stroke getStroke()
   {
      return stroke;
   }
   
   protected Paint getOutlinePaint()
   {
      return outlinePaint;
   }
   
   protected Paint getFillPaint()
   {
      return fillPaint;
   }
   
   //==============================================================
   // Class Methods to set the various characteristics of the
   // blossom.
   //==============================================================
   
   protected void setPosition(Point2D newPosition)
   {
      position = newPosition;
   }
   
   protected void setPetalSize(Dimension2D newPetalSize)
   {
      petalSize = newPetalSize;
   }
   
   protected void setRotationAngle(double newRotationAngle)
   {
      rotationAngle = newRotationAngle;
   }
   
   protected void setStroke(Stroke newStroke)
   {
      stroke = newStroke;
   }
   
   protected void setOutlinePaint(Paint newOutlinePaint)
   {
      outlinePaint = newOutlinePaint;
   }
   
   protected void setFillPaint(Paint newFillPaint)
   {
      fillPaint = newFillPaint;
   }
   
   protected void isPetalFilled(boolean isFilled)
   {
      petalFilled = isFilled;
   }
}