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
      "classifyInstance(Instance): int",
      "printClassifier(): void"
  )
  (
  );

  EClass.FitnessFunction(iClassNameOnly)("FitnessFunction")()();

  Class.HyperrectangleClassificationFitnessFunction("HyperrectangleClassificationFitnessFunction")
  (
    "-evaluate(IChromosome): double"
  )
  (
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
