package com.dan_nixon.csc3423;

import com.dan_nixon.csc3423.framework.InstanceSet;
import static org.hamcrest.CoreMatchers.instanceOf;
import org.jgap.Chromosome;
import org.jgap.Configuration;
import org.jgap.Gene;
import org.jgap.InvalidConfigurationException;
import org.jgap.impl.CompositeGene;
import org.jgap.impl.DefaultConfiguration;
import org.jgap.impl.IntegerGene;
import org.junit.*;
import static org.junit.Assert.*;

public class ClassifierHyperrectangleTest
{
  public static final double TH = 0.0001;

  private static InstanceSet m_testInstances;

  @BeforeClass
  public static void initTests()
  {
    m_testInstances = new InstanceSet("src/test/resources/test_data", true);
  }

  @Test
  public void test_CreateGenes() throws InvalidConfigurationException
  {
    double bounds[][] = new double[][]{{-6.0, 6.0}, {-6.0, 6.0}};

    DefaultConfiguration.reset();
    Configuration c = new DefaultConfiguration();
    Gene[] g = ClassifierHyperrectangle.createSampleGenes(c, 2, bounds);

    assertEquals(3, g.length);

    assertThat(g[0], instanceOf(CompositeGene.class));
    assertEquals(2, ((CompositeGene) g[0]).getGenes().size());
    assertThat(g[1], instanceOf(CompositeGene.class));
    assertEquals(2, ((CompositeGene) g[1]).getGenes().size());
    assertThat(g[2], instanceOf(IntegerGene.class));
  }

  @Test
  public void test_GetBounds()
  {
    double[][] bounds = ClassifierHyperrectangle.getInstanceSetBounds(m_testInstances);

    // Validate array shape
    assertEquals(2, bounds.length);
    assertEquals(2, bounds[0].length);
    assertEquals(2, bounds[1].length);

    // Validate values
    assertEquals(0.45, bounds[0][0], TH);
    assertEquals(0.75, bounds[0][1], TH);
    assertEquals(0.75, bounds[1][0], TH);
    assertEquals(1.25, bounds[1][1], TH);
  }

  @Test
  public void test_PadBounds()
  {
    double[][] bounds = new double[][]{{-0.5, 1.0}, {-1.0, 0.0}};
    bounds = ClassifierHyperrectangle.padBounds(bounds, 0.5);

    assertEquals(-1.0, bounds[0][0], TH);
    assertEquals(1.5, bounds[0][1], TH);
    assertEquals(-1.5, bounds[1][0], TH);
    assertEquals(0.5, bounds[1][1], TH);
  }

  @Test
  public void test_CreateFromGenes() throws InvalidConfigurationException
  {
    double bounds[][] = new double[][]{{-6.0, 6.0}, {-6.0, 6.0}};

    DefaultConfiguration.reset();
    Configuration c = new DefaultConfiguration();
    Gene[] g = ClassifierHyperrectangle.createSampleGenes(c, 2, bounds);

    ((CompositeGene) g[0]).geneAt(0).setAllele(0.5);
    ((CompositeGene) g[0]).geneAt(1).setAllele(0.7);

    ((CompositeGene) g[1]).geneAt(0).setAllele(1.0);
    ((CompositeGene) g[1]).geneAt(1).setAllele(1.2);

    g[2].setAllele(1);

    Chromosome sample = new Chromosome(c, g);
    ClassifierHyperrectangle classifier = new ClassifierHyperrectangle(sample);

    assertEquals(1, classifier.getClassValue());

    double d[][] = classifier.getDimensions();

    assertEquals(2, d.length);

    assertEquals(0.5, d[0][0], TH);
    assertEquals(0.7, d[0][1], TH);

    assertEquals(1.0, d[1][0], TH);
    assertEquals(1.2, d[1][1], TH);
  }

  @Test
  public void test_Classify() throws InvalidConfigurationException
  {
    double bounds[][] = new double[][]{{-6.0, 6.0}, {-6.0, 6.0}};

    DefaultConfiguration.reset();
    Configuration c = new DefaultConfiguration();
    Gene[] g = ClassifierHyperrectangle.createSampleGenes(c, 2, bounds);

    ((CompositeGene) g[0]).geneAt(0).setAllele(0.5);
    ((CompositeGene) g[0]).geneAt(1).setAllele(0.7);

    ((CompositeGene) g[1]).geneAt(0).setAllele(1.0);
    ((CompositeGene) g[1]).geneAt(1).setAllele(1.2);

    g[2].setAllele(1);

    Chromosome sample = new Chromosome(c, g);
    ClassifierHyperrectangle classifier = new ClassifierHyperrectangle(sample);

    assertEquals(1, classifier.classifyInstance(m_testInstances.getInstance(0)));
    assertEquals(1, classifier.classifyInstance(m_testInstances.getInstance(1)));
    assertEquals(-1, classifier.classifyInstance(m_testInstances.getInstance(2)));
    assertEquals(-1, classifier.classifyInstance(m_testInstances.getInstance(3)));
    assertEquals(-1, classifier.classifyInstance(m_testInstances.getInstance(4)));
    assertEquals(-1, classifier.classifyInstance(m_testInstances.getInstance(5)));
  }
}
