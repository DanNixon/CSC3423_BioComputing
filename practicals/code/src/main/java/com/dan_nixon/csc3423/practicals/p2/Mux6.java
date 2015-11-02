package com.dan_nixon.csc3423.practicals.p2;

import java.util.*;

import static org.epochx.stats.StatField.*;

import org.epochx.gp.model.*;
import org.epochx.gp.op.crossover.KozaCrossover;
import org.epochx.life.*;
import org.epochx.op.selection.TournamentSelector;
import org.epochx.stats.*;
import org.epochx.tools.random.MersenneTwisterFast;

import org.epochx.epox.*;
import org.epochx.epox.bool.*;
import org.epochx.epox.lang.IfFunction;
import org.epochx.gp.representation.*;
import org.epochx.representation.CandidateProgram;
import org.epochx.tools.util.BoolUtils;


public class Mux6 extends GPModel {
  private Variable d3;
  private Variable d2;
  private Variable d1;
  private Variable d0;
  private Variable a1;
  private Variable a0;

  // The boolean inputs/outputs that we will test solutions against.
  private boolean[][] inputs;
  private boolean[] outputs;

  public Mux6() {
    // Construct the variables into fields.
    d3 = new Variable("d3",Boolean.class);
    d2 = new Variable("d2",Boolean.class);
    d1 = new Variable("d1",Boolean.class);
    d0 = new Variable("d0",Boolean.class);
    a1 = new Variable("a1",Boolean.class);
    a0 = new Variable("a0",Boolean.class);
    List<Node> syntax = new ArrayList<Node>();

    // Functions.
    syntax.add(new IfFunction());
    syntax.add(new AndFunction());
    syntax.add(new OrFunction());
    syntax.add(new NotFunction());

    // Terminals.
    syntax.add(d3);
    syntax.add(d2);
    syntax.add(d1);
    syntax.add(d0);
    syntax.add(a1);
    syntax.add(a0);
    setSyntax(syntax);

    // Generate set of test inputs and corresponding correct output.
    inputs = BoolUtils.generateBoolSequences(6);
    outputs = generateOutputs(inputs);
  }

  @Override
  public double getFitness(CandidateProgram p) {
    GPCandidateProgram program = (GPCandidateProgram) p;

    double score = 0;
    for (int i=0; i<inputs.length; i++) {
      // Set the variables.
      a0.setValue(inputs[i][0]);
      a1.setValue(inputs[i][1]);
      d0.setValue(inputs[i][2]);
      d1.setValue(inputs[i][3]);
      d2.setValue(inputs[i][4]);
      d3.setValue(inputs[i][5]);

      Boolean result = (Boolean) program.evaluate();
      if (result == outputs[i]) {
        score++;
      }
    }

    double rawFitness = 64 - score;
    return rawFitness; // + 0.1 * program.getProgramLength();
  }

  @Override
  public Class<?> getReturnType() {
    return Boolean.class;
  }

  /*
  * Generates the correct outputs for the 6-bit multiplexer from
  * the given inputs to test against.
  */
  private boolean[] generateOutputs(boolean[][] in) {
    boolean[] out = new boolean[in.length];

    for (int i=0; i<in.length; i++) {
      if(in[i][0] && in[i][1]) {
          out[i] = in[i][2];
      } else if(in[i][0] && !in[i][1]) {
          out[i] = in[i][3];
      } else if(!in[i][0] && in[i][1]) {
          out[i] = in[i][4];
      } else if(!in[i][0] && !in[i][1]) {
          out[i] = in[i][5];
      }
    }

    return out;
  }

  public static void main(String[] args) {
    // Construct the model.
    Mux6 model = new Mux6();

    // Set parameters.
    model.setPopulationSize(1000);
    model.setNoGenerations(100);
    model.setNoElites(10);
    model.setCrossoverProbability(0.9);
    model.setMutationProbability(0.1);
    model.setReproductionProbability(0.1);

    // Set operators and components.
    model.setProgramSelector(new TournamentSelector(model, 10));

    Life.get().addGenerationListener(new GenerationAdapter(){
      public void onGenerationEnd() {
        Stats.get().print(GEN_NUMBER, GEN_FITNESS_MIN, GEN_FITTEST_PROGRAM);
      }
    });

    // Run the model.
    model.run();
  }
}
