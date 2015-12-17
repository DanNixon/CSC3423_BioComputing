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

  Class.ClassifierHyperrectangle("ClassifierHyperrectangle")
  (
      "+createSampleGenes(Configuration, int, double[][]): Gene[]",
      "+getInstanceSetBounds(InstanceSet): double[][]",
      "+padBounds(double[][], double): double[][]",
      "+ClassifierHyperrectangle(InstanceSet)",
      "+ClassifierHyperrectangle(IChromosome)",
      "+classifyInstance(Instance): int",
      "+printClassifier(): void",
      "+getDimensions(): double[][]",
      "+getClassValue(): int",
      "-updateFromChromosome(IChromosome): void"
  )
  (
      "+MAX_ITERATIONS: int",
      "+POPULATION_SIZE: int",
      "+TARGET_LEARN_ACCURACY: double",
      "-m_classValue: int",
      "-m_dimensions: double[][]"
  );

  EClass.FitnessFunction(iClassNameOnly)("FitnessFunction")()();

  Class.HyperrectFitnessFunction("HyperrectFitnessFunction")
  (
      "+HyperrectFitnessFunction(InstanceSet)",
      "-evaluate(IChromosome): double"
  )
  (
      "-m_trainingSet: InstanceSet"
  );

  EClass.Frame(iClassNameOnly)("Frame")()();

  Class.HyperrectangleFrame("HyperrectangleFrame")
  (
      "+HyperrectangleFrame()",
      "+paint(Graphics): void",
      "+addClassifier(ClassifierHyperrectangle): void",
      "+getXDim(double): double",
      "+getYDim(double): double"
  )
  (
      "+PADDING: int",
      "+POINT_RADIUS: int",
      "-m_trainingData: InstanceSet",
      "-m_classifiers: List<ClassifierHyperrectangle>",
      "-m_xFact: double",
      "-m_yFact: double",
      "-m_minX: double",
      "-m_minY: double"
  );

  topToBottom(25)(
      Classifier,
      ClassifierHyperrectangle);
  Group.gl(
      Classifier,
      ClassifierHyperrectangle);

  topToBottom(25)(
      FitnessFunction,
      HyperrectFitnessFunction);
  Group.gc(
      FitnessFunction,
      HyperrectFitnessFunction);

  topToBottom(25)(
      Frame,
      HyperrectangleFrame);
  Group.gr(
      Frame,
      HyperrectangleFrame);

  leftToRight(25)(gl, gc, gr);

  drawObjects(gl, gc, gr);

  link(inheritance)(ClassifierHyperrectangle.n -- Classifier.s);
  link(inheritance)(HyperrectFitnessFunction.n -- FitnessFunction.s);
  link(aggregation)(ClassifierHyperrectangle.e -- HyperrectFitnessFunction.w);
  link(inheritance)(HyperrectangleFrame.n -- Frame.s);

endfig;

end
