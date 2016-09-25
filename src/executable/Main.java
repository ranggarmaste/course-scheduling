/**
 * Tester class.
 * Author: Garmastewira
 * Date: 20 September 2016
 */
public class Main {
    public static void main(String[] args) {
        Graph graph = new Graph("Testcase.txt");
        //graph.randomInitialize();
        
        //System.out.println("Conflicts: " + graph.getConflicts());

        //graph.printCurrDomain();
        //graph.mutation();
        //graph.printCurrDomain();

        Chromosome cr1 = new Chromosome(graph);
        cr1.getGraph().randomInitialize();
        Chromosome cr2 = new Chromosome(graph);
        cr2.getGraph().randomInitialize();

        Chromosome[] chromosomeArray = new Chromosome[2];
        chromosomeArray[0] = cr1;
        chromosomeArray[1] = cr2;

        DNA testDNA = new DNA(chromosomeArray);

        GeneticAlgorithm ga = new GeneticAlgorithm(true, true);

        //Chromosome test = new Chromosome(graph);
        //System.out.println("Conflicts: " + test.getGraph().getConflicts());
    }
}
