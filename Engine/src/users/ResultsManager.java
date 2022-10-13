package users;

import DecryptionManager.DecryptionManager;

import java.util.HashMap;
import java.util.Map;

public class ResultsManager {

    // map agentName, results per agents
    private final Map<String, String> ResultsMap = new HashMap<>();

    public synchronized void addResults(String username,String results) {

        ResultsMap.put(username,results);
    }
}
