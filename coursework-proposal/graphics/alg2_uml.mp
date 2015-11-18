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

  Class.ClassifierNN("ClassifierNN")
  (
      "classifyInstance(Instance): int",
      "printClassifier(): void"
  )
  ();

  topToBottom(10)(
      Classifier,
      ClassifierNN);

  drawObjects(
      Classifier,
      ClassifierNN);

endfig;

end
