/**
 * Definisi string untuk genetic algorithm
 */

public class Chromosome {
	private Graph graph;
	//Untuk caching, jadi ngk ngitung fitness berulang2
	private int fitness = 0;

	public Chromosome() {
		//System.out.println("cctor 1");		
	}

	public Chromosome(Graph graph) {
		//System.out.println("cctor 2");
		this.graph = graph;
	}

	public Chromosome(Chromosome cr) {
		//System.out.println("cctor 3");
		this.graph = cr.graph;
	}

	public Graph getGraph() {
		return graph;
	}

	public void setGraph(Graph graph) {
		this.graph = new Graph(graph);
	}

	public void setGene(int idx, Node nd) {
		graph.setGene(idx, nd);
	}

	public Node getGene(int idx) {
		return graph.getGene(idx);
	}

	public int size() {
		return graph.getVariables().length;
	}

	public int getFitness() {
		/*
		if (fitness==0) {
			fitness = graph.getFitness();
		}
		return fitness;
		*/
		return graph.getFitness();
	}

	//Ini yang dimutasi satu aja
	public void mutate() {
		graph.mutate();
	}

	public void print() {
		graph.print();
	}

	 public int getMaxFitness() {
        return graph.getMaxFitness();
    }

}