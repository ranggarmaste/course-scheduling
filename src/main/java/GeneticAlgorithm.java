import java.util.Random;

public class GeneticAlgorithm {
	//Peluang ganti nilai variabel
	private static final double mutationProb = 0.05;
	//Berapa kromosom yang akan dipilih secara random untuk crossover
	private static final int forSelection = 10;
	//Nanti nilai variabel dari child dapet dari parent 1 atau parent 2 bergantung ini
	private static final double forCrossover = 0.5;
	//Kromosom dengan fitness function paling bagus ditaro di paling atas, gk diubah-ubah 
	//alias gak ikut mutasi sama crossover
	private static boolean keepBestChromosome;
	//Pake GA yang steady state atau generational?
	private static boolean isSteadyState;
	//Cara crossover
	private static String crossType;
	//Cara parent selection
	private static String parentType;

	public GeneticAlgorithm(boolean best, boolean steady, String ct, String pt) {
		keepBestChromosome = best;
		isSteadyState = steady;
		crossType = ct;
		parentType = pt;
	}

	public static DNA evolve(DNA dna) {
		if (isSteadyState) {
			System.out.println("Evolving incrementally");
			return evolveIncremental(dna);
		} else {
			System.out.println("Evolving generationally");
			return evolveGenerational(dna);
		}
	}

	public static DNA evolveGenerational(DNA dna) {
		//Bikin DNA kosong
		DNA newDNA = new DNA(dna.getGraph(), dna.size(), false);

		if(keepBestChromosome) {
			//Simpen best chromosome dari populasi sebelumnya
			newDNA.saveChromosome(0, dna.getFittestChromosome());
			System.out.println("Saving best Chromosome");
			//Tuker. Fittest chromosome jadi di paling depan
			/*
			int fittest = dna.getFittestChromosome();
			if (fittest!=0) {
				dna.switchChromosome(0, fittest);
			}
			*/
		}

		int mulaiIterasi;
		if (keepBestChromosome) {
			//Kalo best kromosom di keep, mulai dari 1
			mulaiIterasi = 1;
		} else {
			//Kalo ngk, dari awal
			mulaiIterasi = 0;
		}

		if(isSteadyState) {

		} else {
			if (crossType.equals("uniform")) {
				for (int i=mulaiIterasi; i<dna.size(); i++) {
					//Chromosome parent1 = parentSelection(dna);
					//Chromosome parent2 = parentSelection(dna);
					Chromosome parent1 = new Chromosome();
					parent1.setGraph(parentSelection(dna).getGraph());
					Chromosome parent2 = new Chromosome();
					parent2.setGraph(parentSelection(dna).getGraph());
					Chromosome newChrome = new Chromosome();
					newChrome.setGraph(uniformCrossover(dna, parent1, parent2).getGraph());
					//Chromosome newChrome = uniformCrossover(dna, parent1, parent2);
					newDNA.saveChromosome(i, newChrome);
				}
			}
		}

		//Mutasi 
		for (int i=mulaiIterasi; i<dna.size(); i++) {
			if (Math.random() <= mutationProb) {
				newDNA.getChromosome(i).mutate();
			}
		}

		return newDNA;
	}

	public static DNA evolveIncremental(DNA dna) {
		if(keepBestChromosome) {
			//Tuker. Fittest chromosome jadi di paling depan
			int fittest = dna.getFittestNumber();
			System.out.println("Fittest : " + fittest);
			if (fittest!=0) {
				dna.switchChromosome(0, fittest);
				System.out.println("Saving best Chromosome");
			}
		}

		int mulaiIterasi;
		if (keepBestChromosome) {
			//Kalo best kromosom di keep, mulai dari 1
			mulaiIterasi = 1;
		} else {
			//Kalo ngk, dari awal
			mulaiIterasi = 0;
		}

		//crossover
		Chromosome parent1 = new Chromosome();
		parent1.setGraph(parentSelection(dna).getGraph());
		Chromosome parent2 = new Chromosome();
		parent2.setGraph(parentSelection(dna).getGraph());
		Chromosome[] child = uniformCrossoverArray(dna, parent1, parent2);

		int random1 = dna.getRandomInteger(keepBestChromosome);
		int random2;
		do {
			random2 = dna.getRandomInteger(keepBestChromosome);
		} while (random1==random2);

		if ((random1 == 0 || random2==0) && (keepBestChromosome)) {
			System.exit(1);
		}

		dna.saveChromosome(random1, child[0]);
		dna.saveChromosome(random2, child[1]);

		//mutation
		for (int i=mulaiIterasi; i<dna.size(); i++) {
			if (Math.random() <= mutationProb) {
				dna.getChromosome(i).mutate();
			}
		}

		//return
		return dna;
	}

	public static Chromosome uniformCrossover(DNA dna, Chromosome parent1, Chromosome parent2) {
		//dummy
		Chromosome newChrome = dna.getChromosome(0);

		//Loop ke tiap variabel
		for (int i=0; i<parent1.size(); i++) {
			double rand = Math.random();
			if (rand<=forCrossover) {
				newChrome.setGene(i, parent1.getGene(i));
			} else {
				newChrome.setGene(i, parent2.getGene(i));
			}
		}

		return newChrome;
	}

	public static Chromosome[] uniformCrossoverArray(DNA dna, Chromosome parent1, Chromosome parent2) {
		//Dummy
		Chromosome[] newChrome = new Chromosome[2];
		//newChrome[0] = dna.getChromosome(0);
		//newChrome[1] = dna.getChromosome(0);
		newChrome[0] = new Chromosome();
		newChrome[1] = new Chromosome();

		newChrome[0].setGraph(dna.getChromosome(0).getGraph());
		newChrome[1].setGraph(dna.getChromosome(0).getGraph());

		//Loop ke tiap variabel
		for (int i=0; i<parent1.size(); i++) {
			double rand = Math.random();
			if (rand<=forCrossover) {
				newChrome[0].setGene(i, parent1.getGene(i));
				newChrome[1].setGene(i, parent2.getGene(i));
			} else {
				newChrome[0].setGene(i, parent2.getGene(i));
				newChrome[1].setGene(i, parent1.getGene(i));
			}
		}

		return newChrome;
	}

	public static Chromosome parentSelection(DNA dna) {
		if (parentType.equals("tournament")) {
			//Bikin DNA kosong
			DNA selection = new DNA(dna.getGraph(), forSelection, false);

			//Buat tiap kromosom, pilih acak
			for (int i=0; i<selection.size(); i++) {
				int rand = (int) (Math.random() * dna.size());
		        selection.saveChromosome(i, dna.getChromosome(rand));
			}

			//Pilih yang paling bagus
			Chromosome fittest = selection.getFittestChromosome();
			return fittest;
		} else
		if (parentType.equals("roulette")) {
			//Jumlahkan total fitness
			int totalFitness = 0;
			for (int i=0; i<dna.size(); i++) {
				totalFitness+=dna.getChromosome(i).getFitness();
			}

			Random bilBul = new Random();
			//Generate random integer dari 0 sampe totalFitness-1
			int rand = bilBul.nextInt(totalFitness);

			int partSum = 0;
			for (int i=0; i<dna.size(); i++) {
				partSum+=dna.getChromosome(i).getFitness();
				if (partSum>=rand) {
					return dna.getChromosome(i);
				}
			}			
			return dna.getChromosome(0);
		} else 
		if (parentType.equals("rank")) {
			return dna.getChromosome(0);
		} else
		if (parentType.equals("sampling")) {
			return dna.getChromosome(0);
		} else {
			return dna.getChromosome(0);
		}
	}
}