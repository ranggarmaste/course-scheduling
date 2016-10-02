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
    private ArrayList<DomainLimit> scheduleDomainLimits;
    private ArrayList<DomainLimit> roomDomainLimits;

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

    public void printCurrDomain() {
        for (Node node : variables) {
            node.printCurrDomain();
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
        int count = 0;
        for (int i = 0; i < variables.length - 1; i++) {
            for (int j = i + 1; j < variables.length; j++) {
                count += variables[i].conflictWith(variables[j]);
            }
        }
        return count;
    }

    public double getEffectivity() {
        return 100 - (getConflicts() / (double) variables.length * 100);
    }

    public int getConflictsTS() {
        int count = 0;
        for (int i = 0; i < variables.length - 1; i++) {
            for (int j = i + 1; j < variables.length; j++) {
                count += variables[i].conflictWithTS(variables[j]);
            }
        }
        return count;
    }

    public double getEffectivityTS() {
        double count = 0.0;
        for (int i = 0; i < variables.length; i++) {
            count += variables[i].getCurrDomain().getDuration();
        }
        return 100 - (getConflictsTS() / count * 100);
    }

    //Strategi masih bisa diubah
    //Ini yang dimutasi satu aja, random dari domainList rangga
    public void mutate() {
        //Jumlah variabel
        int jumlahVar = variables.length;

        //Random dari jumlah variabel, pilih variabel mana yang mau dipilih
        Random gen1 = new Random();
        int randInd1 = gen1.nextInt(jumlahVar);   

        //Pilih variable mana yang mau dijadiin pengganti
        //while not the same
        Domain randDomain = new Domain(0,0,0,"inisialisasi");
        do {
            Random gen2 = new Random();
            int randInd2 = gen2.nextInt(variables[randInd1].getDomainList().size());
            //Pilih domain random dari variabel tersebut
            randDomain = variables[randInd1].getDomainList().get(randInd2);
        } while(Domain.isSame(randDomain,variables[randInd1].getCurrDomain()));
        //while(randDomain==variables[randInd1].getCurrDomain());

        //mutate
        variables[randInd1].setCurrDomain(randDomain);
    }

    public void setGene(int idx, Node nd) {
        variables[idx].setCurrDomain(nd.getCurrDomain());
    }

    public Node getGene(int idx) {
        return variables[idx];
    }

    public Node[] getVariables() {
        return variables;
    }

    public ArrayList<DomainLimit> getScheduleDomainLimits() {
        return scheduleDomainLimits;
    }

    public ArrayList<DomainLimit> getRoomDomainLimits() {
        return roomDomainLimits;
    }

    public int getFitness() {
        int n = variables.length;
        return (n * (n - 1) / 2) - getConflicts();
    }

    public int getMaxFitness() {
        int n = variables.length;
        return (n * (n - 1) / 2);
    }
}
