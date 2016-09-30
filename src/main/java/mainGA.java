public class mainGA {
	public static void main (String[] args) {

        DNA dna = new DNA(50, true);
        
        int generationCount = 0;

        //Argumen pertama true or false -> best chromosome di keep atau ngk, pake true aja biar ketemu solusi
        //Argumen kedua true or false -> pake GA yang steady (true) ato generational(false)
        //Algo yang dibuat baru yang generational
        //Nanti mungkin ditambah parameter int buat nentuin mekanisme parentSelection, sementara ini masih pake 
        //tournamentSelection
        //Nanti mungkin ditambah parameter int buat nentuin mekanisme crossover, sementara ini masih pake 
        //uniformCrossover
        //nanti mungkin ditambah parameter int buat nentuin mekanisme mutation, sementara ini kemungkinan 
        //kecil satu gen/variabel akan berubah nilainya untuk setiap kromosom
        //Ini juga belum nerapin survirvor selection alias kick chromosome, soalnya gk wajib di buku
        boolean bool1, bool2;
        if (args[0]=="true") {
            bool1 = true;
        } else {
            bool1 = false;
        }
        if (args[1]=="true") {
            bool2 = true;
        } else {
            bool2 = false;
        }
        //Umumnya, run java mainGA true false
        GeneticAlgorithm ga = new GeneticAlgorithm(bool1, bool2, args[2]);

        System.out.println("Max fitness : " + dna.getFittestChromosome().getMaxFitness());

        while (dna.getFittestChromosome().getFitness() < dna.getFittestChromosome().getMaxFitness() && generationCount<=10) {
            generationCount++;
            System.out.println("Generation: " + generationCount + " Fittest: " + dna.getFittestChromosome().getFitness());
            dna = ga.evolve(dna);
        }

        generationCount++;
        System.out.println("Generation: " + generationCount);
        System.out.println("Fittest Genes:");
        //System.out.println(dna.getFittestChromosome().getFitness());
        dna.getFittestChromosome().print();
        if (dna.getFittestChromosome().getFitness()==28) {
        	System.out.println("Solution found!");
        }
	}
}