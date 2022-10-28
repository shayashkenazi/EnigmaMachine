package users;

import DecryptionManager.DecryptionManager;
import DecryptionManager.DM;
import java.util.HashMap;
import java.util.Map;

public class DMManager {

    //      map < allyName, dm for ally>
    private final Map<String, DM> DMmap = new HashMap<>();

    public synchronized void addDM(String username,DM dm) {
        DMmap.put(username,dm);
    }

    public synchronized DM getDM(String allyName){
        if(!DMmap.containsKey(allyName))
            return null;
        return DMmap.get(allyName);
    }
    public synchronized DM removeDM(String allyName){
        return DMmap.remove(allyName);
    }

}
