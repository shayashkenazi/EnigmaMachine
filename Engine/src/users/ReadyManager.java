package users;

import javafx.util.Pair;

import java.util.Set;

public class ReadyManager {

    //      pair<uboatName,sentenceToCheck>
    private Pair<String,String> uBoatAndSentence;
    private Set<String> alliesSet;

    public synchronized Pair<String, String> getuBoatAndSentence() {
        return uBoatAndSentence;
    }

    public synchronized Set<String> getAlliesSet() {
        return alliesSet;
    }

    public void setuBoatAndSentence(Pair<String, String> uBoatAndSentence) {
        this.uBoatAndSentence = uBoatAndSentence;
    }

    public void addAlliesToSet(String allyName) {
        this.alliesSet.add(allyName);
    }
}
