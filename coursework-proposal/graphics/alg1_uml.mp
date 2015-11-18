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
      "-m_population: Genotype"
  );

  topToBottom(10)(
      Classifier,
      ClassifierHyperrectangle);

  drawObjects(
      Classifier,
      ClassifierHyperrectangle);

endfig;

end
