/**
 * Tester class.
 * Author: Garmastewira
 * Date: 20 September 2016
 */
public class Main {
    public static void main(String[] args) {
        Graph graph = new Graph("Testcase.txt");
        graph.randomInitialize();
        graph.print();
        System.out.println("Conflicts: " + graph.getConflicts());
    }
}
