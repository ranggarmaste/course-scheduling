/**
 * Hill Climbing ALgorithm
 * Author: Sekar Anglila Hapsari
 * Date: 25 September 2016
 */

public class HillClimbing{

	private Graph graph;

	public HillClimbing(Graph G){
		this.graph = G;
	}

	private Graph getMin(Graph[] LGraph){
		Graph GMin = new Graph(LGraph[0]);
		for (i = 1; i < LGraph.size(); i++){
			if (GMin.getConflicts() <= LGraph[i].getConflicts()){
				GMin = LGraph[i];
			}
		}
		return GMin;
	}
	
	public Graph HillClimb(Graph G){
		//Kamus
		boolean finish = false;
		GCurrent = new Graph(G);

		//Algoritma
		while !(finish){
			Graph[] LGraph = new Graph[100];
			for (int v = 0; v < GCurrent.getVariables().size(); v++){
				for (int d = 0; d < GCurrent.getVariables[v].getDomainList().size(); d++){
					Domain a = GCurrent.getVariables[v].getDomainList[d];
					if (isSame(a, GCurrent.getVariables[v].getCurrDomain())) {
						//do nothing
					} else {
						GCopy = new Graph(GCurrent);
						GCopy.getVariables[v].setCurrDomain(d);
						LGraph.add(Graph(GCopy));
					}
				}
			}
			Graph GMin = getMin(LGraph));
			if (GMin.getConflicts() <= GCurrent.getConflicts()){
				GCurrent = Gmin;
			} else {
				finish = true;
			}
		}
		return GCurrent;
	}

	public static void main(String[] args) {
        Graph graph = new Graph("Testcase.txt");
        HillClimbing HC = new HillClimbing(graph);
        Graph GSolution = HC.HillClimb(graph);
        GSolution.print();
        System.out.println("Conflicts: " + GSolution.getConflicts());
    }
}