package com.dan_nixon.csc3423.vis;

import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class HyperrectangleFrame extends Frame
{
  public int PADDING = 20;
  
  public HyperrectangleFrame()
  {
    super("Hyperrectangle Visualisation (2D classification)");
    
    setSize(200, 200);
    
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
    //TODO
  }
}
