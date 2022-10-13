package users;

import DecryptionManager.DecryptionManager;
import DecryptionManager.DM;
import java.util.HashMap;
import java.util.Map;

public class DMManager {

    //      map < allyName, dm for ally>
    private final Map<String, DM> DMmap = new HashMap<>();

    public synchronized void addDM(String username) {

        //map.put(username);
    }

    public synchronized DM getDM(String allyName){
        return DMmap.get(allyName);
    }
}
