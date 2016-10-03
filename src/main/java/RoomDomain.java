import java.util.ArrayList;

/**
 * Created by ranggarmaste on 10/3/16.
 */
public class RoomDomain {
    private String roomName;
    private ArrayList<Domain> domainList;

    public RoomDomain(String roomName) {
        this.roomName = roomName;
        domainList = new ArrayList<>();
    }

    public String getRoomName() {
        return roomName;
    }

    public ArrayList<Domain> getDomainList() {
        return domainList;
    }

    public void addDomain(Domain d) {
        domainList.add(d);
    }
}
