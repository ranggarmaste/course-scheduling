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

	public GeneticAlgorithm(boolean best, boolean steady) {
		keepBestChromosome = best;
		isSteadyState = steady;
	}

	public static DNA evolve(DNA dna) {
		if (isSteadyState) {
			return evolveGenerational(dna);
		} else {
			return evolveGenerational(dna);
		}
	}

	public static DNA evolveGenerational(DNA dna) {
		//Bikin DNA kosong
		DNA newDNA = new DNA(dna.size(), false);

		if(keepBestChromosome) {
			//Simpen best chromosome dari populasi sebelumnya
			newDNA.saveChromosome(0, dna.getFittestChromosome());
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
			for (int i=mulaiIterasi; i<dna.size(); i++) {
				/*
				Chromosome parent1 = parentSelection(dna);
				Chromosome parent2 = parentSelection(dna);
				Chromosome newChrome = crossover(parent1, parent2);
				dna.saveChromosome(i, newChrome);
				*/
			}
		}

		//Mutasi 
		for (int i=mulaiIterasi; i<dna.size(); i++) {
			newDNA.getChromosome(i).mutate();
		}

		return newDNA;
	}

	private static Chromosome crossover(Chromosome parent1, Chromosome parent2) {
		Chromosome newChrome = new Chromosome();

		//Loop ke tiap variabel
		for (int i=0; i<parent1.size(); i++) {
			if (Math.random()<=forCrossover) {
				newChrome.setGene(i, parent1.getGene(i));
			} else {
				newChrome.setGene(i, parent2.getGene(i));
			}
		}

		return newChrome;
	}

	private static Chromosome selection(DNA dna) {
		//Bikin DNA kosong
		DNA selection = new DNA(forSelection, false);

		//Buat tiap kromosom, pilih acak
		for (int i=0; i<selection.size(); i++) {
			int rand = (int) (Math.random() * dna.size());
            selection.saveChromosome(i, dna.getChromosome(rand));
		}

		//Pilih yang paling bagus
		Chromosome fittest = selection.getFittestChromosome();
		return fittest;
	}
}