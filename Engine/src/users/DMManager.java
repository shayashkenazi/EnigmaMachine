package users;

import DecryptionManager.DecryptionManager;

import java.util.HashMap;
import java.util.Map;

public class DMManager {

    private final Map<String, DecryptionManager> map = new HashMap<>();

    public synchronized void addDM(String username) {

        map.put(username)
    }
}
