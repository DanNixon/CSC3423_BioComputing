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
    for (Instance i : m_trainingData.getInstances())
    {
      double x = (i.getRealAttribute(0) - m_minX) * m_xFact + PADDING - POINT_RADIUS;
      double y = (i.getRealAttribute(1) - m_minY) * m_yFact + PADDING - POINT_RADIUS;
      
      switch(i.getClassValue())
      {
        case 0:
          g.setColor(Color.BLUE);
          break;
        case 1:
          g.setColor(Color.RED);
          break;
      }
      
      g.fillOval((int) x, (int) y, POINT_RADIUS * 2, POINT_RADIUS * 2);
    }
    
    // Draw classifiers
    for (ClassifierHyperrectangle c : m_classifiers)
    {
      // TODO
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
  
  private final InstanceSet m_trainingData;
  private final List<ClassifierHyperrectangle> m_classifiers;
  private final double m_xFact;
  private final double m_yFact;
  private final double m_minX;
  private final double m_minY;
}
