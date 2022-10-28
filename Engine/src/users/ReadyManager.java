package users;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.util.Pair;

import java.util.HashSet;
import java.util.Set;

public class ReadyManager {

    //      pair<uboatName,sentenceToCheck>
    private Pair<String,String> uBoatAndSentence;
    private Set<String> alliesSet = new HashSet<>();
    private BooleanProperty isAllReady = new SimpleBooleanProperty(false);

    public boolean isIsAllReady() {
        return isAllReady.get();
    }

    public BooleanProperty getIsAllReadyProperty() {
        return isAllReady;
    }

    public void setIsAllReady(boolean isAllReady) {
        this.isAllReady.set(isAllReady);
    }


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
    public void removeAlliesToSet(String allyName) {
        /*for(String allyNameCur : alliesSet){
            if(allyNameCur.equals(allyName))
                this.alliesSet.remove(allyNameCur);
        }*/
        this.alliesSet.remove(allyName);

    }

}
