/**
 * Definisi string untuk genetic algorithm
 */

public class Chromosome {
	private Graph graph;
	//Untuk caching, jadi ngk ngitung fitness berulang2
	private int fitness = 0;

	public Chromosome() {

	}

	public Chromosome(Graph graph) {
		this.graph = new Graph(graph);
	}

	public Chromosome(Chromosome cr) {
		this.graph = cr.graph;
	}

	public Graph getGraph() {
		return graph;
	}

	public void setGraph(Graph graph) {
		this.graph = graph;
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
		if (fitness==0) {
			fitness = graph.getFitness();
		}
		return fitness;
	}

	//Ini yang dimutasi satu aja
	public void mutate() {
		graph.mutate();
	}

	 public int getMaxFitness() {
        return graph.getMaxFitness();
    }

}