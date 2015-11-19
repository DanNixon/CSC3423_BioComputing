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

  topToBottom(25)(
      Classifier,
      ClassifierNN);
  Group.gl(
      Classifier,
      ClassifierNN);

  drawObjects(gl);

  link(inheritance)(ClassifierNN.n -- Classifier.s);

endfig;

end
