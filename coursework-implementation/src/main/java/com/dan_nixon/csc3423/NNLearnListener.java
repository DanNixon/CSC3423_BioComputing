package com.dan_nixon.csc3423;

import com.dan_nixon.csc3423.vis.NeuralNetworkFrame;
import org.neuroph.core.events.LearningEvent;
import org.neuroph.core.events.LearningEventListener;
import org.neuroph.nnet.learning.BackPropagation;

/**
 * Listener to provide feedback during the learning process.
 */
class NNLearnListener implements LearningEventListener
{
  /**
   * Create a new listener.
   * @param vis Optional if not set to null a visualisation will be shown
   */
  public NNLearnListener(NeuralNetworkFrame vis)
  {
      m_vis = vis;
  }
  
  public void handleLearningEvent(LearningEvent event)
  {
    BackPropagation bp = (BackPropagation) event.getSource();
    
    // Handle printing to stdout
    StringBuilder sb = new StringBuilder();
    sb.append("Iteration: ");
    sb.append(bp.getCurrentIteration());
    sb.append(", error: ");
    sb.append(bp.getTotalNetworkError());
    System.out.println(sb);
    
    // Handle visualisation
    if (m_vis != null)
    {
      m_vis.setNetwork(bp.getNeuralNetwork());
      m_vis.repaint();
    }
  }
    
  NeuralNetworkFrame m_vis;
}