input metauml;

beginfig(1);

  Class.Classifier("Classifier")
  (
    "getFitness() : double",
    "computeFitness(InstanceSet): void",
    "computeStats(InstanceSet): void",
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
    "+EXIT_THRESHOLD: double",
    "-m_classValue: int",
    "-m_dimensions: double[][]"
  );

  EClass.FitnessFunction(iClassNameOnly)("FitnessFunction")()();

  Class.HyperrectangleClassificationFitnessFunction("HyperrectangleClassificationFitnessFunction")
  (
    "+HyperrectangleClassificationFitnessFunction(InstanceSet)",
    "-evaluate(IChromosome): double"
  )
  (
    "-m_trainingSet: InstanceSet"
  );

  topToBottom(25)(
      Classifier,
      ClassifierHyperrectangle);
  Group.gl(
      Classifier,
      ClassifierHyperrectangle);

  topToBottom(25)(
      FitnessFunction,
      HyperrectangleClassificationFitnessFunction);
  Group.gr(
      FitnessFunction,
      HyperrectangleClassificationFitnessFunction);

  leftToRight(25)(gl, gr);

  drawObjects(gl, gr);

  link(inheritance)(ClassifierHyperrectangle.n -- Classifier.s);
  link(inheritance)(HyperrectangleClassificationFitnessFunction.n -- FitnessFunction.s);
  link(aggregation)(ClassifierHyperrectangle.e -- HyperrectangleClassificationFitnessFunction.w);

endfig;

end
