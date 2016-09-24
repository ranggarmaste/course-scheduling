public class mainGA {
	public static void main (String[] args) {

        DNA dna = new DNA(50, true);
        
        int generationCount = 0;

        GeneticAlgorithm ga = new GeneticAlgorithm(true, false);

        System.out.println("Max fitness : " + dna.getFittestChromosome().getMaxFitness());

        while (dna.getFittestChromosome().getFitness() < dna.getFittestChromosome().getMaxFitness() && generationCount<=10) {
            generationCount++;
            System.out.println("Generation: " + generationCount + " Fittest: " + dna.getFittestChromosome().getFitness());
            dna = ga.evolve(dna);
        }

        generationCount++;
        System.out.println("Generation: " + generationCount);
        System.out.println("Fittest Genes:");
        System.out.println(dna.getFittestChromosome().getFitness());
        if (dna.getFittestChromosome().getFitness()==28) {
        	System.out.println("Solution found!");
        }
	}
}