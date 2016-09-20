import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Constraint graph representation of a CSP problem.
 * Author: Garmastewira
 * Date: 20 September 2016
 */
public class Graph {
    private Node[] variables;
    ArrayList<DomainLimit> scheduleDomainLimits;
    ArrayList<DomainLimit> roomDomainLimits;

    public Graph(String filepath) {
        BufferedReader br = null;

        try {
            String currLine;
            br = new BufferedReader(new FileReader(filepath));

            //Read room domain limit
            roomDomainLimits = new ArrayList<>();
            while (!(currLine = br.readLine()).equals("")) {
                if (currLine.equals("Ruangan")) continue;
                String[] data = currLine.split(";");
                String[] days = data[3].split(",");
                roomDomainLimits.add(new DomainLimit("Ruangan", data[0], data[1], data[2], "0", days));
            }

            //Read schedule domain limit
            scheduleDomainLimits = new ArrayList<>();
            while ((currLine = br.readLine()) != null) {
                if (currLine.equals("Jadwal")) continue;
                String[] data = currLine.split(";");
                String[] days = data[5].split(",");
                scheduleDomainLimits.add(new DomainLimit(data[0], data[1], data[2], data[3], data[4], days));
            }

            //Create variables from both domain limits
            ArrayList<Node> nodes = new ArrayList<>();
            for (DomainLimit scheduleDL : scheduleDomainLimits) {
                ArrayList<Domain> domains = new ArrayList<>();
                for (DomainLimit roomDL : roomDomainLimits) {
                    if (scheduleDL.getRoom().equals("-") || scheduleDL.getRoom().equals(roomDL.getRoom())) {
                        int startTime = scheduleDL.getStartTime() > roomDL.getStartTime() ?
                                scheduleDL.getStartTime() : roomDL.getStartTime();
                        int finishTime = scheduleDL.getFinishTime() < roomDL.getFinishTime() ?
                                scheduleDL.getFinishTime() : roomDL.getFinishTime();

                        //Check maximum start time
                        int startTimeLimit = finishTime - scheduleDL.getDuration();
                        //Check available days
                        for (int d : scheduleDL.getDays()) {
                            if (roomDL.getDays().contains(d)) {
                                for (int st = startTime; st <= startTimeLimit; st++) {
                                    domains.add(new Domain(d, st, scheduleDL.getDuration(), roomDL.getRoom()));
                                }
                            }
                        }
                    }
                }
                nodes.add(new Node(scheduleDL.getName(), null, domains));
            }

            //Create Graph
            this.variables = nodes.toArray(new Node[nodes.size()]);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)
                    br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Graph(Graph graph) {
        variables = new Node[graph.variables.length];
        for (int i = 0; i < graph.variables.length; i++) {
            variables[i] = new Node(graph.variables[i]);
        }
    }

    public void print() {
        for (Node node : variables) {
            node.print();
            System.out.println();
        }
    }

    public void randomInitialize() {
        for (Node var : variables) {
            Random gen = new Random();
            int randInd = gen.nextInt(var.getDomainList().size());
            Domain randDomain = var.getDomainList().get(randInd);
            var.setCurrDomain(randDomain);
        }
    }

    public int getConflicts() {
        System.out.println("**Conflicts**");
        int count = 0;
        for (int i = 0; i < variables.length - 1; i++) {
            for (int j = i + 1; j < variables.length; j++) {
                count += variables[i].conflictWith(variables[j]);
            }
        }
        return count;
    }
}
