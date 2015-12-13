package com.dan_nixon.csc3423.vis;

import com.dan_nixon.csc3423.ClassifierHyperrectangle;
import com.dan_nixon.csc3423.framework.Instance;
import com.dan_nixon.csc3423.framework.InstanceSet;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

public class HyperrectangleFrame extends Frame
{
  public static final int PADDING = 50;
  public static final int POINT_RADIUS = 2;

  public HyperrectangleFrame(InstanceSet trainingData)
  {
    super("Hyperrectangle Visualisation (2D classification)");

    m_trainingData = trainingData;
    m_classifiers = new ArrayList<ClassifierHyperrectangle>();
    
    setSize(600, 600);
    
    // Cache data for coordinate generation
    double[][] bounds = ClassifierHyperrectangle.getInstanceSetBounds(trainingData);
    if (bounds.length != 2)
      throw new RuntimeException("Must be 2D data");
    
    m_xFact = (getWidth() - 2 * PADDING) / (bounds[0][1] - bounds[0][0]);
    m_yFact = (getHeight() - 2 * PADDING) / (bounds[1][1] - bounds[1][0]);
    m_minX = bounds[0][0];
    m_minY = bounds[1][0];

    setVisible(true);
    
    addWindowListener(new WindowAdapter()
      {
        @Override
        public void windowClosing(WindowEvent e) { dispose(); }
      }
     );
  }

  @Override
  public void paint(Graphics g)
  {
    // Draw data points
    for (Instance i : m_trainingData.getInstancesOrig())
    {
      switch(i.getClassValue())
      {
        case 0:
          g.setColor(Color.BLUE);
          break;
        case 1:
          g.setColor(Color.RED);
          break;
      }
      
      g.fillOval((int) getXDim(i.getRealAttribute(0)),
                 (int) getYDim(i.getRealAttribute(1)),
                 POINT_RADIUS * 2,
                 POINT_RADIUS * 2);
    }
    
    // Draw classifiers
    for (ClassifierHyperrectangle c : m_classifiers)
    { 
      switch(c.getClassValue())
      {
        case 0:
          g.setColor(Color.BLUE);
          break;
        case 1:
          g.setColor(Color.RED);
          break;
      }
      
      double[][] dimensions = c.getDimensions();
      
      int x = (int) getXDim(dimensions[0][0]);
      int y = (int) getYDim(dimensions[1][0]);
      int w = (int) getXDim(dimensions[0][1]) - x;
      int h = (int) getYDim(dimensions[1][1]) - y;
      
      g.drawRect(x, y, w, h);
    }
  }
  
  /**
   * Adds a new classifier to the visualisation.
   * @param c Classifier to add
   */
  public void addClassifier(ClassifierHyperrectangle c)
  {
    m_classifiers.add(c);
  }
  
  /**
   * Calculate X dimension form cached values.
   * @param xValue Raw X value
   * @return Position on screen
   */
  public double getXDim(double xValue)
  {
    return (xValue - m_minX) * m_xFact + PADDING - POINT_RADIUS;
  }
  
  /**
   * Calculate Y dimension form cached values.
   * @param yValue Raw Y value
   * @return Position on screen
   */
  public double getYDim(double yValue)
  {
    return (yValue - m_minY) * m_yFact + PADDING - POINT_RADIUS;
  }
  
  private final InstanceSet m_trainingData;
  private final List<ClassifierHyperrectangle> m_classifiers;
  private final double m_xFact;
  private final double m_yFact;
  private final double m_minX;
  private final double m_minY;
}
