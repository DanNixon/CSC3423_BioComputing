package com.dan_nixon.csc3423.vis;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class NeuralNetworkFrame extends Frame
{
  public int PADDING = 20;
  public int NODE_DIAMETER = 20;
  
  public static int max(int[] a)
  {
    int max = Integer.MIN_VALUE;
    for (int i : a)
    {
      if (i > max)
        max = i;
    }
    return max;
  }
  
  public NeuralNetworkFrame()
  {
    super("Neural Network Visualisation");
    
    // TODO
    m_netLayout = new int[]{2, 20, 20, 1};
    
    int x = m_netLayout.length * (NODE_DIAMETER + 100) + (2 * PADDING);
    int y = max(m_netLayout) * (NODE_DIAMETER + 10) + (2 * PADDING);
    setSize(x, y);
    
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
    // Draw network nodes
    int sep_x = (getWidth() - (PADDING * 2)) / (m_netLayout.length + 1);
    for (int i = 0; i < m_netLayout.length; i++)
    {
      if (i == 0)
        g.setColor(Color.GREEN);
      else if (i == (m_netLayout.length - 1))
        g.setColor(Color.RED);
      else
        g.setColor(Color.BLUE);
      
      int sep_y = (getHeight() - (PADDING * 2)) / (m_netLayout[i] + 1);
      for (int j = 0; j < m_netLayout[i]; j++)
      {
        int x = ((i + 1) * sep_x) + PADDING;
        int y = ((j + 1) * sep_y) + PADDING;
        g.drawOval(x, y, NODE_DIAMETER, NODE_DIAMETER);
      }
    }
  }
  
  int m_netLayout[];
}
