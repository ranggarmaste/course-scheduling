public class mainGA {
	public static void main (String[] args) {

        DNA dna = new DNA(Integer.parseInt(args[3]), true, args[4]);
        
        int generationCount = 0;

        //args[0] true or false -> best chromosome di keep atau ngk, pake true aja biar ketemu solusi
        //args[1] true or false -> pake GA yang steady (true) ato generational(false)
        //Algo yang dibuat baru yang generational
        //Nanti mungkin ditambah parameter int buat nentuin mekanisme parentSelection, sementara ini masih pake 
        //tournamentSelection
        //Nanti mungkin ditambah parameter int buat nentuin mekanisme crossover, sementara ini masih pake 
        //uniformCrossover
        //nanti mungkin ditambah parameter int buat nentuin mekanisme mutation, sementara ini kemungkinan 
        //kecil satu gen/variabel akan berubah nilainya untuk setiap kromosom
        //Ini juga belum nerapin survirvor selection alias kick chromosome, soalnya gk wajib di buku
        //args[2] mekanisme crossover -> uniform
        //args[3] jumlah kromosom
        //args[4] string inputfile
        //args[5] Maks iterasi
        //args[6] mekanisme parent selection -> roulette, rank, sampling, tournament
        //args[7] pake seleksi alam atau ngk
        boolean bool1, bool2;
        if (args[0].equals("true")) {
            bool1 = true;
        } else {
            bool1 = false;
        }
        if (args[1].equals("true")) {
            bool2 = true;
        } else {
            bool2 = false;
        }
        //Umumnya, run java mainGA true false
        GeneticAlgorithm ga = new GeneticAlgorithm(bool1, bool2, args[2], args[6]);

        System.out.println("Max fitness : " + dna.getFittestChromosome().getMaxFitness());

        while (dna.getFittestChromosome().getFitness() < dna.getFittestChromosome().getMaxFitness() && generationCount<Integer.parseInt(args[5])) {
            generationCount++;
            System.out.println("Generation: " + generationCount + " Fittest: " + dna.getFittestChromosome().getFitness());
            dna = ga.evolve(dna);
        }

        System.out.println("Generation: " + generationCount);
        if (dna.getFittestChromosome().getFitness()==dna.getFittestChromosome().getMaxFitness()) {
            System.out.println("Solution found!");
        } else if (generationCount >= Integer.parseInt(args[5])) {
            System.out.println("Max iteration reached");
            System.out.println("Fittest : " + dna.getFittestChromosome().getFitness());
        }
        System.out.println("Fittest Genes:");
        dna.getFittestChromosome().print();
	}
}