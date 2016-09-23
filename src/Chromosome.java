/**
 * Definisi string untuk genetic algorithm
 */

public class Chromosome {
	private Graph graph;

	public Chromosome(Graph graph) {
		this.graph = new Graph(graph);
	}

	public Graph getGraph() {
		return graph;
	}

	public int fitnessFunction() {
		return graph.getConflicts();
	}

	//Ini yang dimutasi satu aja
	public void mutation() {
		graph.mutation();
	}
}