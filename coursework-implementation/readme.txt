The code is provided as an Apache Maven project.

On Linux (and probably Mac) this can be built using: mvn package
And executed using: ./run
(the run Bash script allows the training and testing data sets to be changed)

Note that there is an issue with JGAP that causes issues using CompositeGene
when compiling on JVM 7 and above, to fix this the
java.util.Arrays.useLegacyMergeSort=true is set.
