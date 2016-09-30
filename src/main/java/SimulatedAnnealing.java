import java.util.Random;

/**
 * Simulated Annealing Algorithm
 * Author: Bang Radz
 * Date: 24 September 2016
 */
class SimulatedAnnealing {
	private Graph graph;
	private double temperature;
	private double decreament;
	private int iteration;

	public SimulatedAnnealing(Graph G, double T, double D) {
		this.graph = new Graph(G);
		this.temperature = T;
		this.decreament = D;
		this.iteration = 0;
	}
	
	public Graph getGraph() {
		return graph;
	}
	
	public double getTemperature() {
		return temperature;
	}
	
	public double getDecreament() {
		return decreament;
	}

	public int getIteration() {
		return iteration;
	}
		
	public void run() {
		while ((graph.getConflicts() != 0) && (temperature > 0.0000001)) {
			iteration++;
			Random gen = new Random();
        	int randNode = gen.nextInt(graph.getVariables().length);
        	int randInd = gen.nextInt(graph.getVariables()[randNode].getDomainList().size());
        	Domain randDomain = graph.getVariables()[randNode].getDomainList().get(randInd);
        	Domain DomainTemp = graph.getVariables()[randNode].getCurrDomain();
            int conflictTemp = graph.getConflicts();
            graph.getVariables()[randNode].setCurrDomain(randDomain);
            int conflictAfter = graph.getConflicts();
            if (conflictTemp < conflictAfter) {
            	double valueTemp = (Math.exp((conflictTemp - conflictAfter)/ temperature))*100;
            	int valueRand = (int) Math.round(valueTemp);
            	valueRand = 100/valueRand;
            	Random rand = new Random();
            	boolean val = rand.nextInt(valueRand)==0;
            	if (val == false) {
            		graph.getVariables()[randNode].setCurrDomain(DomainTemp);
            	}
            }
            temperature = temperature * decreament;
        }
    }
}
        