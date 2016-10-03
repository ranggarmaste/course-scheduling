import java.util.Random;

public class GeneticAlgorithm {
	//Peluang ganti nilai variabel
	private static double mutationProb = 0.05;
	//Peluang crossover
	private static double crossProb = 0.95;
	//Berapa kromosom yang akan dipilih secara random untuk crossover
	private static double selectionPercentage = 0.01;
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
	//Cara ngekick kalo steady
	private static String kickType;

	public GeneticAlgorithm(boolean best, boolean steady, String ct, String pt) {
		keepBestChromosome = best;
		isSteadyState = steady;
		crossType = ct;
		parentType = pt;
		kickType = "none";
	}

	public GeneticAlgorithm(boolean best, boolean steady, String ct, String pt, String kt) {
		keepBestChromosome = best;
		isSteadyState = steady;
		crossType = ct;
		parentType = pt;
		kickType = kt;
	}

	public void setParameters(int mp, int cp, int sp) {
		mutationProb = (double)mp/100;
		crossProb = (double)cp/100;
		selectionPercentage = (double)sp/100;
	}

	public static DNA evolve(DNA dna) {
		if (isSteadyState) {
			//System.out.println("Evolving incrementally");
			return evolveIncremental(dna);
		} else {
			//System.out.println("Evolving generationally");
			return evolveGenerational(dna);
		}
	}

	public static DNA evolveGenerational(DNA dna) {
		//Bikin DNA kosong
		DNA newDNA = new DNA(dna.getGraph(), dna.size(), false);

		if(keepBestChromosome) {
			//Simpen best chromosome dari populasi sebelumnya
			newDNA.saveChromosome(0, dna.getFittestChromosome());
			//System.out.println("Saving best Chromosome");
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
		for (int i=mulaiIterasi; i<dna.size(); i++) {
			if (Math.random()<=crossProb) {
				//Chromosome parent1 = parentSelection(dna);
				//Chromosome parent2 = parentSelection(dna);
				Chromosome parent1 = new Chromosome();
				Chromosome parent2 = new Chromosome();

				//GATAU KENAPA TESTCASE TUJUH PARENT2nya BISA KEPILIH SIZE 8, KAN HARUSNYA 20 SEMUA
				//INI HANDLERNYA
				if (parentType.equals("sampling")) {
					do {
						Chromosome[] parents = samplingParentSelection(dna);
						parent1.setGraph(parents[0].getGraph());
						parent2.setGraph(parents[1].getGraph());
					} while (parent1.size()!=parent2.size());
				} else {
					do {
						parent1.setGraph(parentSelection(dna).getGraph());
						parent2.setGraph(parentSelection(dna).getGraph());
					} while (parent1.size()!=parent2.size());
				}

				//System.out.println("Parent1 : " + parent1.size());
				//System.out.println("Parent2 : " + parent2.size());

				Chromosome newChrome = new Chromosome();
				newChrome.setGraph(crossover(dna, parent1, parent2).getGraph());
				//Chromosome newChrome = uniformCrossover(dna, parent1, parent2);
				newDNA.saveChromosome(i, newChrome);
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
			//System.out.println("Fittest : " + fittest);
			if (fittest!=0) {
				dna.switchChromosome(0, fittest);
				//System.out.println("Saving best Chromosome");
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
		if (Math.random()<=crossProb) {
			Chromosome parent1 = new Chromosome();
			Chromosome parent2 = new Chromosome();

			//GATAU KENAPA TESTCASE TUJUH PARENT2nya BISA KEPILIH SIZE 8, KAN HARUSNYA 20 SEMUA
			//INI HANDLERNYA
			if (parentType.equals("sampling")) {
				do {
					Chromosome[] parents = samplingParentSelection(dna);
					parent1.setGraph(parents[0].getGraph());
					parent2.setGraph(parents[1].getGraph());
				} while (parent1.size()!=parent2.size());
			} else {
				do {
					parent1.setGraph(parentSelection(dna).getGraph());
					parent2.setGraph(parentSelection(dna).getGraph());
				} while (parent1.size()!=parent2.size());
			}

			Chromosome[] child = crossoverArray(dna, parent1, parent2);

			if (kickType.equals("random")) {
				int random1 = dna.getRandomInteger(keepBestChromosome);
				int random2;
				do {
					random2 = dna.getRandomInteger(keepBestChromosome);
				} while (random1==random2);

				/*
				if ((random1 == 0 || random2==0) && (keepBestChromosome)) {
					System.exit(1);
				}
				*/

				dna.saveChromosome(random1, child[0]);
				dna.saveChromosome(random2, child[1]);

			} else
			if (kickType.equals("worst")) {
				int worst[] = dna.getWorstNumbers();

				int worst1 = worst[0];
				int worst2 = worst[1];

				dna.saveChromosome(worst1, child[0]);
				dna.saveChromosome(worst2, child[1]);
			} else {
				int worst[] = dna.getWorstNumbers();

				int worst1 = worst[0];
				int worst2 = worst[1];

				dna.saveChromosome(worst1, child[0]);
				dna.saveChromosome(worst2, child[1]);
			}
		}

		//mutation
		for (int i=mulaiIterasi; i<dna.size(); i++) {
			if (Math.random() <= mutationProb) {
				dna.getChromosome(i).mutate();
			}
		}

		//return
		return dna;
	}

	public static Chromosome crossover(DNA dna, Chromosome parent1, Chromosome parent2) {
		//dummy
		Chromosome newChrome = new Chromosome();
		newChrome.setGraph(dna.getChromosome(0).getGraph());

		if (crossType.equals("uniform")) {
			//Loop ke tiap variabel
			for (int i=0; i<parent1.size(); i++) {
				double rand = Math.random();
				if (rand<=forCrossover) {
					newChrome.setGene(i, parent1.getGene(i));
				} else {
					newChrome.setGene(i, parent2.getGene(i));
				}
			}
		} else 
		if (crossType.equals("one-point")) {
			//Tentukan point
			Random rand = new Random();
			int point = rand.nextInt(parent1.size());

			for (int i=0; i<parent1.size(); i++) {
				if (i<=point) {
					newChrome.setGene(i, parent1.getGene(i));
				} else {
					newChrome.setGene(i, parent2.getGene(i));
				}
			}
		} else
		if (crossType.equals("two-point")) {
			//Tentukan point1
			Random rand = new Random();
			int point1 = rand.nextInt(parent1.size());

			//Tentukan point2
			int point2;
			do {
				point2 = rand.nextInt(parent1.size());
			} while (point1==point2);

			for (int i=0; i<parent1.size(); i++) {
				if (i<=point1) {
					newChrome.setGene(i, parent1.getGene(i));
				} else 
				if (i>point1 && i<point2) {
					newChrome.setGene(i, parent2.getGene(i));
				} else {
					newChrome.setGene(i, parent1.getGene(i));
				}
			}
		}
		else {
			//Loop ke tiap variabel
			for (int i=0; i<parent1.size(); i++) {
				double rand = Math.random();
				if (rand<=forCrossover) {
					newChrome.setGene(i, parent1.getGene(i));
				} else {
					newChrome.setGene(i, parent2.getGene(i));
				}
			}
		}

		return newChrome;
	}

	public static Chromosome[] crossoverArray(DNA dna, Chromosome parent1, Chromosome parent2) {
		//Dummy
		Chromosome[] newChrome = new Chromosome[2];
		//newChrome[0] = dna.getChromosome(0);
		//newChrome[1] = dna.getChromosome(0);
		newChrome[0] = new Chromosome();
		newChrome[1] = new Chromosome();

		newChrome[0].setGraph(dna.getChromosome(0).getGraph());
		newChrome[1].setGraph(dna.getChromosome(0).getGraph());

		if (crossType.equals("uniform")) {
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
		} else 
		if (crossType.equals("one-point")) {
			//Tentukan point
			Random rand = new Random();
			int point = rand.nextInt(parent1.size());

			for (int i=0; i<parent1.size(); i++) {
				if (i<=point) {
					newChrome[0].setGene(i, parent1.getGene(i));
					newChrome[1].setGene(i, parent2.getGene(i));
				} else {
					newChrome[0].setGene(i, parent2.getGene(i));
					newChrome[1].setGene(i, parent1.getGene(i));
				}
			}
		} else 
		if (crossType.equals("two-point")) {
			//Tentukan point1
			Random rand = new Random();
			int point1 = rand.nextInt(parent1.size());

			//Tentukan point2
			int point2;
			do {
				point2 = rand.nextInt(parent1.size());
			} while (point1==point2);

			for (int i=0; i<parent1.size(); i++) {
				if (i<=point1) {
					newChrome[0].setGene(i, parent1.getGene(i));
					newChrome[1].setGene(i, parent2.getGene(i));
				} else 
				if (i>point1 && i<point2) {
					newChrome[0].setGene(i, parent2.getGene(i));
					newChrome[1].setGene(i, parent1.getGene(i));
				} else {
					newChrome[0].setGene(i, parent1.getGene(i));
					newChrome[1].setGene(i, parent2.getGene(i));
				}
			}
		} else {
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
		}

		return newChrome;
	}

	public static Chromosome parentSelection(DNA dna) {
		if (parentType.equals("tournament")) {
			//System.out.println("TOURNAMENT");
			//Bikin DNA kosong
			int forSelection = (int) (selectionPercentage*dna.size());
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
			//System.out.println("ROULETTE");
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
		} else {
			return dna.getChromosome(0);
		}
	}

	public static Chromosome[] samplingParentSelection(DNA dna) {
		//System.out.println("SAMPLING");
		Chromosome[] selection = new Chromosome[2];
		for (int i=0; i<2; i++) {
			selection[i] = new Chromosome();
		}

		//Jumlahkan total fitness
		int totalFitness = 0;
		for (int i=0; i<dna.size(); i++) {
			totalFitness+=dna.getChromosome(i).getFitness();
		}

		Random bilBul = new Random();
		//Generate random integer dari 0 sampe totalFitness-1
		int rand1 = bilBul.nextInt(totalFitness);
		//Generate pasangannya
		int rand2 = rand1 + totalFitness/2;
		if (rand2>=totalFitness) {
			rand2-=totalFitness;
		}

		int partSum1 = 0;
		for (int i=0; i<dna.size(); i++) {
			partSum1+=dna.getChromosome(i).getFitness();
			if (partSum1>=rand1) {
				selection[0].setGraph(dna.getChromosome(i).getGraph());
				i = dna.size();
				break;
			}
		}			

		int partSum2 = 0;
		for (int i=0; i<dna.size(); i++) {
			partSum2+=dna.getChromosome(i).getFitness();
			if (partSum2>=rand2) {
				selection[1].setGraph(dna.getChromosome(i).getGraph());
				i = dna.size();
				break;
			}
		}			

		return selection;
	}
}