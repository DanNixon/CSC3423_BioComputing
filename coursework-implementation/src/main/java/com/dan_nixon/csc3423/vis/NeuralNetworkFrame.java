package com.dan_nixon.csc3423.vis;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import org.neuroph.core.Connection;
import org.neuroph.core.Layer;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.nnet.comp.neuron.BiasNeuron;

/**
 * Draws a live visualisation of the network and connection weights.
 *
 * Input layer is green.
 * Hidden layers are blue.
 * Output layer is red.
 *
 * Regular neurons are solid.
 * Bias neurons are outlined.
 */
public class NeuralNetworkFrame extends Frame
{
  public static final int PADDING = 20;
  public static final int NODE_DIAMETER = 20;
  public static final double HUE_MIN = 0.0;
  public static final double HUE_MAX = 0.9;

  /**
   * Retrieve the minimum value in an array of Double objects.
   * @param a Double array
   * @return Minimum value
   */
  public static double min(Double[] a)
  {
    double max = Double.MAX_VALUE;
    for (double v : a)
    {
      if (v < max)
        max = v;
    }
    return max;
  }

  /**
   * Retrieve the maximum value in an array of Double objects.
   * @param a Double array
   * @return Maximum value
   */
  public static double max(Double[] a)
  {
    double max = Double.MIN_VALUE;
    for (double v : a)
    {
      if (v > max)
        max = v;
    }
    return max;
  }

  /**
   * Retrieve the maximum number of neurons in a neural network.
   * @param net Neural network
   * @return Maximum layer size
   */
  public static int maxLayerSize(NeuralNetwork net)
  {
    int max = Integer.MIN_VALUE;
    for (int i = 0; i < net.getLayersCount(); i++)
    {
      int c = net.getLayerAt(i).getNeuronsCount();
      if (c > max)
        max = c;
    }
    return max;
  }

  /**
   * Create a new visualisation for a given network.
   * @param network The network to visualise
   */
  public NeuralNetworkFrame(NeuralNetwork network)
  {
    super("Neural Network Visualisation (2D classification)");

    m_network = network;

    // Set a reasonable size
    int x = m_network.getLayersCount() * (NODE_DIAMETER + 250) + (2 * PADDING);
    int y = maxLayerSize(m_network) * (NODE_DIAMETER + 15) + (2 * PADDING);
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
    int numLayers = m_network.getLayersCount();
    int heightMinusPadding = getHeight() - (PADDING * 2);
    int paddingPlusRadius = PADDING + (NODE_DIAMETER / 2);
    int sepX = (getWidth() - (PADDING * 2) - NODE_DIAMETER) / (numLayers - 1);

    // Draw network nodes
    for (int i = 0; i < numLayers; i++)
    {
      int x = (i * sepX) + PADDING;

      if (i == 0)
        g.setColor(Color.GREEN);
      else if (i == (numLayers - 1))
        g.setColor(Color.RED);
      else
        g.setColor(Color.BLUE);

      int neuronCount = m_network.getLayerAt(i).getNeuronsCount();
      int sepY = heightMinusPadding / (neuronCount + 1);
      for (int j = 0; j < neuronCount; j++)
      {
        int y = ((j + 1) * sepY) + PADDING;
        if (m_network.getLayerAt(i).getNeuronAt(j) instanceof BiasNeuron)
          g.drawOval(x, y, NODE_DIAMETER, NODE_DIAMETER);
        else
          g.fillOval(x, y, NODE_DIAMETER, NODE_DIAMETER);
      }
    }

    double maxWeight = max(m_network.getWeights());
    double minWeight = min(m_network.getWeights());
    double weightFactor = (HUE_MAX - HUE_MIN) / (maxWeight - minWeight);

    // Draw network links
    for (int i = 0; i < numLayers - 1; i++)
    {
      Layer netLayer = m_network.getLayerAt(i);

      int x1 = (i * sepX) + PADDING + NODE_DIAMETER;
      int x2 = ((i + 1) * sepX) + PADDING;

      int neuronCount1 = m_network.getLayerAt(i).getNeuronsCount();
      int sepY1 = heightMinusPadding / (neuronCount1 + 1);
      for (int j = 0; j < neuronCount1; j++)
      {
        Connection[] neuronConns = netLayer.getNeuronAt(j).getOutConnections();

        int y1 = ((j + 1) * sepY1) + paddingPlusRadius;

        int neuronCount2 = m_network.getLayerAt(i + 1).getNeuronsCount();
        int sepY2 = heightMinusPadding / (neuronCount2 + 1);
        for (int k = 0; k < neuronCount2; k++)
        {
          // If this test fails then the target neuron is most likely a bias neuron
          if (k < neuronConns.length)
          {
            // Set colour based on weight
            double w = neuronConns[k].getWeight().getValue();
            double h = ((w - minWeight) * weightFactor) + HUE_MIN;
            g.setColor(Color.getHSBColor((float) h, 0.9f, 0.9f));

            int y2 = ((k + 1) * sepY2) + paddingPlusRadius;
            g.drawLine(x1, y1, x2, y2);
          }
        }
      }
    }

    // Draw weight scale
    for (int i = 0; i < 100; i++)
    {
      double h = ((0.01 * (double) i) * (HUE_MAX - HUE_MIN)) + HUE_MIN;
      g.setColor(Color.getHSBColor((float) h, 0.9f, 0.9f));
      int y = 150 - i;
      g.drawLine(50, y, 60, y);
    }

    // Draw weight scale range numbers
    g.setColor(Color.BLACK);
    g.drawString(Double.toString(maxWeight), 65, 65);
    g.drawString(Double.toString(minWeight), 65, 150);
  }

  /**
   * Set the visualised network.
   * @param network The network to visualise
   */
  public void setNetwork(NeuralNetwork network)
  {
    m_network = network;
  }

  private NeuralNetwork m_network;
}
