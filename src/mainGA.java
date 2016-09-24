public class mainGA {
	public static void main (String[] args) {

        DNA dna = new DNA(50, true);
        
        int generationCount = 0;

        GeneticAlgorithm ga = new GeneticAlgorithm(true, false);

        while (dna.getFittestChromosome().getFitness() < dna.getFittestChromosome().getMaxFitness()) {
            generationCount++;
            System.out.println("Generation: " + generationCount + " Fittest: " + dna.getFittestChromosome().getFitness());
            dna = ga.evolve(dna);
        }

        System.out.println("Solution found!");
        System.out.println("Generation: " + generationCount);
        System.out.println("Genes:");
        System.out.println(dna.getFittestChromosome());
	}
}