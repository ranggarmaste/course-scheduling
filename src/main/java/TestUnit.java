public class TestUnit {

	//Test mutation
	public static void testMutation(String inputFile) {
		DNA dna = new DNA(1, true, inputFile);
		dna.getChromosome(0).print();
		dna.getChromosome(0).mutate();
		for (int i=0; i<5; i++) {
			System.out.println();
		}
		dna.getChromosome(0).print();
	}

	//Crossover works
	public static void testCrossover(String inputFile) {
		//BUG : KENAPA SEMUA RANDOM INITIALIZATION SAMA UNTUK TIAP CHROMOSOME
		DNA dna = new DNA(2, true, inputFile);
		
		Chromosome parent1 = new Chromosome();
		parent1.setGraph(dna.getChromosome(0).getGraph());
		parent1.print();
		for (int i=0; i<3; i++) {
			System.out.println();
		}

		Chromosome parent2 = new Chromosome();
		parent2.setGraph(dna.getChromosome(1).getGraph());
		parent2.print();
		for (int i=0; i<5; i++) {
			System.out.println();
		}

		Chromosome[] child = GeneticAlgorithm.uniformCrossoverArray(dna, parent1, parent2);

		child[0].print();
		for (int i=0; i<3; i++) {
			System.out.println();
		}
		//BUG KENAPA CHILD 1 sama 0 sama
		child[1].print();
	}

	public static void main(String[] args) {
		//Mutation works
		//testMutation("TestUnit.txt");

		//Crossover
		testCrossover("TestUnit.txt");
	}
}