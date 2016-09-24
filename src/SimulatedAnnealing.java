import java.util.Random;

/**
 * Simulated Annealing Algorithm
 * Author: Bang Radz
 * Date: 24 September 2016
 */
class SimulatedAnnealing {
	private Graph Graf;
	private double Temperatur;
	private double Decreament;
	
	public SimulatedAnnealing(Graph G, double T, double D) {
		this.Graf = G;
		this.Temperatur = T;
		this.Decreament = D;
	}
	
	public Graph getGraf() {
		return Graf;
	}
	
	public double getTemperatur() {
		return Temperatur;
	}
	
	public double getDecreament() {
		return Decreament;
	}
		
	public void run() {
		while ((Graf.getConflicts() != 0) && (Temperatur > 0.0000001)) {
			Random gen = new Random();
        	int randNode = gen.nextInt(Graf.getVariables().length);
        	int randInd = gen.nextInt(Graf.getVariables()[randNode].getDomainList().size());
        	Domain randDomain = Graf.getVariables()[randNode].getDomainList().get(randInd);
        	Domain DomainTemp = Graf.getVariables()[randNode].getCurrDomain();
            int conflictTemp = Graf.getConflicts();
            Graf.getVariables()[randNode].setCurrDomain(randDomain);
            int conflictAfter = Graf.getConflicts();
            if (conflictTemp < conflictAfter) {
            	double valueTemp = (Math.exp((conflictTemp - conflictAfter)/Temperatur))*100;
            	int valueRand = (int) Math.round(valueTemp);
            	valueRand = 100/valueRand;
            	Random rand = new Random();
            	boolean val = rand.nextInt(valueRand)==0;
            	if (val == false) {
            		Graf.getVariables()[randNode].setCurrDomain(DomainTemp);
            	}
            }
            Temperatur = Temperatur*Decreament;
        }
    }
    
    /* CARA PAKE
     public static void main(String[] args) {
        Graph graph = new Graph("Testcase.txt");
        graph.randomInitialize();
        SimulatedAnnealing SA = new SimulatedAnnealing(graph,100000,0.97);
        SA.run();
        graph.print();
        System.out.println("Conflicts: " + graph.getConflicts() + " Temperatur: " + SA.getTemperatur());
    }
    */
}
        