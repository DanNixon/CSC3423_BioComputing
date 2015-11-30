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
  
  @Test
  public void test_CreateGenes() throws InvalidConfigurationException
  {
    DefaultConfiguration.reset();
    Configuration c = new DefaultConfiguration();
    Gene[] g = ClassifierHyperrectangle.createSampleGenes(c, 2);
    
    assertEquals(3, g.length);
    
    assertThat(g[0], instanceOf(CompositeGene.class));
    assertEquals(2, ((CompositeGene) g[0]).getGenes().size());
    assertThat(g[1], instanceOf(CompositeGene.class));
    assertEquals(2, ((CompositeGene) g[1]).getGenes().size());
    assertThat(g[2], instanceOf(IntegerGene.class));
  }
  
  @Test
  public void test_CreateFromGenes() throws InvalidConfigurationException
  {
    DefaultConfiguration.reset();
    Configuration c = new DefaultConfiguration();
    Gene[] g = ClassifierHyperrectangle.createSampleGenes(c, 2);
    
    ((CompositeGene) g[0]).geneAt(0).setAllele(0.5);
    ((CompositeGene) g[0]).geneAt(1).setAllele(0.7);
    
    ((CompositeGene) g[1]).geneAt(0).setAllele(1.0);
    ((CompositeGene) g[1]).geneAt(1).setAllele(1.2);
    
    g[2].setAllele(1);
    
    Chromosome sample = new Chromosome(c, g);
    ClassifierHyperrectangle classifier = new ClassifierHyperrectangle(sample);
    
    assertEquals(1, classifier.getClassValue());
    
    double v[][] = classifier.getVerticies();
    
    assertEquals(2, v.length);
    
    assertEquals(0.5, v[0][0], TH);
    assertEquals(0.7, v[0][1], TH);
    
    assertEquals(1.0, v[1][0], TH);
    assertEquals(1.2, v[1][1], TH);
  }
  
  @Test
  public void test_Classify() throws InvalidConfigurationException
  {
    DefaultConfiguration.reset();
    Configuration c = new DefaultConfiguration();
    Gene[] g = ClassifierHyperrectangle.createSampleGenes(c, 2);
    
    ((CompositeGene) g[0]).geneAt(0).setAllele(0.5);
    ((CompositeGene) g[0]).geneAt(1).setAllele(0.7);
    
    ((CompositeGene) g[1]).geneAt(0).setAllele(1.0);
    ((CompositeGene) g[1]).geneAt(1).setAllele(1.2);
    
    g[2].setAllele(1);
    
    Chromosome sample = new Chromosome(c, g);
    ClassifierHyperrectangle classifier = new ClassifierHyperrectangle(sample);
    
    InstanceSet ts = new InstanceSet("src/test/resources/test_data", true);
    
    assertEquals(1, classifier.classifyInstance(ts.getInstance(0)));
    assertEquals(1, classifier.classifyInstance(ts.getInstance(1)));
    assertEquals(-1, classifier.classifyInstance(ts.getInstance(2)));
    assertEquals(-1, classifier.classifyInstance(ts.getInstance(3)));
    assertEquals(-1, classifier.classifyInstance(ts.getInstance(4)));
    assertEquals(-1, classifier.classifyInstance(ts.getInstance(5)));
  }
}