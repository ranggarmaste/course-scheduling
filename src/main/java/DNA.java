public class DNA {
	private Chromosome[] chromosomeArray;
	private Graph graph;

	public DNA(Chromosome[] chromosomeArray) {
		this.chromosomeArray = chromosomeArray;
	}

	public DNA(int jumlahKromosom, boolean isInitialize) {
		graph = new Graph("Testcase.txt");
		chromosomeArray = new Chromosome[jumlahKromosom];
		if (isInitialize) {
			//Buat solusi random untuk tiap kromosom
			for (int i=0; i<jumlahKromosom; i++) {
				Chromosome cr = new Chromosome(graph);
				cr.getGraph().randomInitialize();
				chromosomeArray[i] = cr;
			}
		}
	}

	// New constructor, just to make things consistent
	public DNA(Graph initGraph, int jumlahKromosom, boolean isInitialize) {
		graph = new Graph(initGraph);
		chromosomeArray = new Chromosome[jumlahKromosom];
		if (isInitialize) {
			//Buat solusi random untuk tiap kromosom
			for (int i=0; i<jumlahKromosom; i++) {
				Chromosome cr = new Chromosome(graph);
				cr.getGraph().randomInitialize();
				chromosomeArray[i] = cr;
			}
		}
	}

	//Getter
	public Chromosome getChromosome(int idx) {
		return chromosomeArray[idx];
	}

	public int getFittestNumber() {
		int ret = 0;
		Chromosome fittest = chromosomeArray[ret];

		for (int i=1; i<chromosomeArray.length; i++) {
			if (chromosomeArray[i].getFitness()>fittest.getFitness()) {
				ret = i;
			}
			fittest = chromosomeArray[ret];
		}

		return ret;
	}

	public Chromosome getFittestChromosome() {
		int ret = 0;
		Chromosome fittest = chromosomeArray[ret];

		for (int i=1; i<chromosomeArray.length; i++) {
			if (chromosomeArray[i].getFitness()>fittest.getFitness()) {
				ret = i;
			}
			fittest = chromosomeArray[ret];
		}

		return fittest;
	}

	public int size() {
		return chromosomeArray.length;
	}

	public void switchChromosome(int i, int j) {
		Chromosome temp = new Chromosome(chromosomeArray[i]);
		chromosomeArray[i] = chromosomeArray[j];
		chromosomeArray[j] = temp;
	}

	public void saveChromosome(int idx, Chromosome cr) {
		chromosomeArray[idx] = cr;
	}
}