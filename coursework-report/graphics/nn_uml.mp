input metauml;

beginfig(1);

  Class.Classifier("Classifier")
  (
      "+getFitness() : double",
      "+computeFitness(InstanceSet): void",
      "+computeStats(InstanceSet): void",
  )
  (
      "-fitness: double"
  );

  Class.ClassifierNN("ClassifierNN")
  (
      "+instanceToRow(Instance): DataSetRow",
      "+ClassifierNN(InstanceSet, boolean)",
      "+classifyInstance(Instance): int",
      "+printClassifier(): void"
  )
  (
      "-m_mlPerceptron: MultiLayerPerceptron"
  );

  EClass.LearningEventListener(iClassNameOnly)("LearningEventListener")()();

  Class.NNLearnListener("NNLearnListener")
  (
      "+NNLearnListener(NeuralNetworkFrame)",
      "+handleLearningEvent(LearningEvent): void"
  )
  (
      "-m_vis: NeuralNetworkFrame"
  );

  EClass.Frame(iClassNameOnly)("Frame")()();

  Class.NeuralNetworkFrame("NeuralNetworkFrame")
  (
      "+min(Double[]): double",
      "+max(Double[]): double",
      "+maxLayerSize(NeuralNetwork): int",
      "+NeuralNetworkFrame(NeuralNetwork)",
      "+paint(Graphics): void",
      "+setNetwork(NeuralNetwork): void"
  )
  (
      "+PADDING: int",
      "+NODE_DIAMETER: int",
      "+HUE_MIN: double",
      "+HUE_MAX: double",
      "-m_network: NeuralNetwork"
  );

  topToBottom(25)(
      Classifier,
      ClassifierNN);
  Group.gl(
      Classifier,
      ClassifierNN);

  topToBottom(25)(
      LearningEventListener,
      NNLearnListener);
  Group.gc(
      LearningEventListener,
      NNLearnListener);

  topToBottom(25)(
      Frame,
      NeuralNetworkFrame);
  Group.gr(
      Frame,
      NeuralNetworkFrame);

  leftToRight(25)(gl, gc, gr);

  drawObjects(gl, gc, gr);

  link(inheritance)(ClassifierNN.n -- Classifier.s);
  link(inheritance)(NNLearnListener.n -- LearningEventListener.s);
  link(aggregation)(ClassifierNN.e -- NNLearnListener.w);
  link(inheritance)(NeuralNetworkFrame.n -- Frame.s);
  link(aggregation)(NNLearnListener.e -- NeuralNetworkFrame.w);

endfig;

end
