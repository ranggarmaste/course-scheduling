import java.util.ArrayList;

/**
 * Hill Climbing ALgorithm
 * Author: Sekar Anglila Hapsari
 * Date: 25 September 2016
 */

public class HillClimbing {
	private Graph graph;
	private int iteration;

	public HillClimbing(Graph graph) {
		this.graph = new Graph(graph);
		iteration = 0;
	}

	public Graph getGraph() {
		return graph;
	}

	public int getIteration() {
		return iteration;
	}

	private Graph getMin(ArrayList<Graph> LGraph){
		Graph GMin = LGraph.get(0);
		for (int i = 1; i < LGraph.size(); i++){
			if (GMin.getConflicts() > LGraph.get(i).getConflicts()){
				GMin = LGraph.get(i);
			}
		}
		return new Graph(GMin);
	}

	public void run() {
		boolean finish = false;
		Graph GCurrent = new Graph(graph);

		while (!finish) {
            iteration++;
			ArrayList<Graph> LGraph = new ArrayList<>();
			for (int v = 0; v < GCurrent.getVariables().length; v++) {
                for (int d = 0; d < GCurrent.getVariables()[v].getDomainList().size(); d++){
					Domain a = GCurrent.getVariables()[v].getDomainList().get(d);
					if (!Domain.isSame(a, GCurrent.getVariables()[v].getCurrDomain())) {
						Graph GCopy = new Graph(GCurrent);
						GCopy.getVariables()[v].setCurrDomain(a);
						LGraph.add(new Graph(GCopy));
					}
				}
			}
			Graph GMin = getMin(LGraph);
			if (GMin.getConflicts() < GCurrent.getConflicts()){
				GCurrent = GMin;
			} else {
				finish = true;
			}
		}
		graph = new Graph(GCurrent);
	}
}