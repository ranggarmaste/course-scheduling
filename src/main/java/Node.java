import java.util.ArrayList;

/**
 * CSP variable's representation in constraint graph.
 * Author: Garmastewira
 * Date: 20 September 2016
 */
public class Node {
    private String courseName;
    private Domain currDomain;
    private ArrayList<RoomDomain> roomDomains;
    private ArrayList<Domain> domainList;

    public Node(String courseName, Domain currDomain, ArrayList<RoomDomain> roomDomains, ArrayList<Domain> domainList) {
        this.courseName = courseName;
        this.currDomain = currDomain;
        this.roomDomains = roomDomains;
        this.domainList = domainList;
    }

    public Node(Node n) {
        courseName = n.courseName;
        currDomain = new Domain(n.currDomain);
        roomDomains = n.roomDomains;
        domainList = new ArrayList<>();
        for (Domain d : n.domainList) {
            domainList.add(new Domain(d));
        }
    }

    public int conflictWith(Node o) {
        if (getCurrDomain().getDay() == o.getCurrDomain().getDay()) {
            if (getCurrDomain().getRoom().equals(o.getCurrDomain().getRoom())) {
                if (getCurrDomain().getFinishTime() > o.getCurrDomain().getStartTime()
                        && o.getCurrDomain().getFinishTime() > getCurrDomain().getStartTime()) {
                    return 1;
                } else {
                    return 0;
                }
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }

    public int conflictWithTS(Node o) {
        if (getCurrDomain().getDay() == o.getCurrDomain().getDay()) {
            if (getCurrDomain().getRoom().equals(o.getCurrDomain().getRoom())) {
                if (getCurrDomain().getFinishTime() > o.getCurrDomain().getStartTime()
                        && o.getCurrDomain().getFinishTime() > getCurrDomain().getStartTime()) {
                    int laterStart = getCurrDomain().getStartTime() > o.getCurrDomain().getStartTime() ?
                            getCurrDomain().getStartTime() : o.getCurrDomain().getStartTime();
                    int earlierFinish = getCurrDomain().getFinishTime() < o.getCurrDomain().getFinishTime() ?
                            getCurrDomain().getFinishTime() : o.getCurrDomain().getFinishTime();
                    return earlierFinish - laterStart;
                } else {
                    return 0;
                }
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }

    public String getCourseName() {
        return courseName;
    }

    public Domain getCurrDomain() {
        return currDomain;
    }

    public void setCurrDomain(Domain currDomain) {
        this.currDomain = currDomain;
    }

    public ArrayList<Domain> getDomainList() {
        return domainList;
    }

    public void print() {
        System.out.println("====" + courseName + "====");
        System.out.println("**Current Domain**");
        if (currDomain != null) {
            currDomain.print();
        } else {
            System.out.println("Not set yet.");
        }
    }

    public void printCurrDomain() {
        if (currDomain != null) {
            currDomain.print();
        } else {
            System.out.println("Not set yet.");
        }
    }
}
