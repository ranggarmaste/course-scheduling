import java.util.Random;

public class DNA {
	private Chromosome[] chromosomeArray;
	private Graph graph;

	public DNA(Chromosome[] chromosomeArray) {
		this.chromosomeArray = chromosomeArray;
	}

	public DNA(int jumlahKromosom, boolean isInitialize, String inputFile) {
		graph = new Graph(inputFile);
		chromosomeArray = new Chromosome[jumlahKromosom];
		if (isInitialize) {
			//Buat solusi random untuk tiap kromosom
			for (int i=0; i<jumlahKromosom; i++) {
				Chromosome cr = new Chromosome(graph);
				cr.getGraph().randomInitialize();
				//FUCK THIS BUG
				//chromosomeArray[i] = cr;
				chromosomeArray[i] = new Chromosome();
				chromosomeArray[i].setGraph(cr.getGraph());
			}
		} else {
			//Isi graph dengan testcase.txt, ini dummy aja
			Graph dummy = new Graph("Testcase.txt");
			for (int i=0; i<jumlahKromosom; i++) {
				Chromosome cr = new Chromosome(dummy);
				cr.getGraph().randomInitialize();
				chromosomeArray[i] = new Chromosome();
				chromosomeArray[i].setGraph(cr.getGraph());
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
				chromosomeArray[i] = new Chromosome();
				chromosomeArray[i].setGraph(cr.getGraph());
			}
		}
		//INI PENTING, jgn diilangin, kalo ngk NGEBUG
		else {
			//Isi graph dengan testcase.txt, ini dummy aja
			Graph dummy = new Graph("Testcase.txt");
			for (int i=0; i<jumlahKromosom; i++) {
				Chromosome cr = new Chromosome(dummy);
				cr.getGraph().randomInitialize();
				chromosomeArray[i] = new Chromosome();
				chromosomeArray[i].setGraph(cr.getGraph());
			}
		}
	}

	//Getter
	public Chromosome getChromosome(int idx) {
		return chromosomeArray[idx];
	}

	//Getter
	public Graph getGraph() {
		return graph;
	}

	public int getFittestNumber() {
		int ret = 0;
		//Chromosome fittest = new Chromosome();
		//fittest.setGraph(chromosomeArray[ret].getGraph());
		int fittest = chromosomeArray[ret].getFitness();
		//System.out.println("Length : " + chromosomeArray.length);
		for (int i=1; i<chromosomeArray.length; i++) {
			if (chromosomeArray[i].getFitness()>fittest) {
				ret = i;
				fittest = chromosomeArray[ret].getFitness();
			}
			//fittest.setGraph(chromosomeArray[ret].getGraph());
		}
		return ret;
	}

	public Chromosome getFittestChromosome() {
		return chromosomeArray[getFittestNumber()];
	}

	public int size() {
		return chromosomeArray.length;
	}

	public void switchChromosome(int i, int j) {
		Chromosome temp = new Chromosome();
		temp.setGraph(chromosomeArray[i].getGraph());
		chromosomeArray[i].setGraph(chromosomeArray[j].getGraph());
		chromosomeArray[j].setGraph(temp.getGraph());
	}

	public void saveChromosome(int idx, Chromosome cr) {
		chromosomeArray[idx].setGraph(cr.getGraph());
	}

	public int getRandomInteger(boolean keepBest) {
		Random bilBul = new Random();
		int ret; 

		if (keepBest) {
			do {
			ret = bilBul.nextInt(size());
			} while (ret==0);	
		} else {
			ret = bilBul.nextInt(size());
		}

		return ret;
	}

	public Chromosome getRandomChromosome(boolean keepBest) {
		int ret = getRandomInteger(keepBest);

		return chromosomeArray[ret];
	}
}