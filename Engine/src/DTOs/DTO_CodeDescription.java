package DTOs;

import javafx.util.Pair;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DTO_CodeDescription implements Serializable {

    private List<Pair<String ,Pair<Integer,Integer>>> rotorInUseIDList;
    private List<Character> startingPositionList;
    private String reflectorID;
    private List<Pair<Character, Character>> plugsInUseList; // maybe Pair<int, int> ?
    private String abc;
    private String str = "Shay is HOMO";


    public DTO_CodeDescription (String abc,List<Pair<String ,Pair<Integer,Integer>>> rotorInUseIDList, List<Character> startingPositionList, String reflectorID,
                                List<Pair<Character, Character>> plugsInUseList) {
        this.abc = abc;
        this.rotorInUseIDList = rotorInUseIDList;
        this.startingPositionList = startingPositionList;
        this.reflectorID = reflectorID;
        this.plugsInUseList = plugsInUseList;

    }

    public List<Pair<String ,Pair<Integer,Integer>>> getRotorsInUseIDList() { return rotorInUseIDList; }
    public Integer getNotch(Pair<String ,Pair<Integer,Integer>> pair) { return pair.getValue().getKey();}
    public Integer getCurrent(Pair<String ,Pair<Integer,Integer>> pair) { return pair.getValue().getValue();}
    public List<Character> getStartingPositionList() { return startingPositionList; }
    public String getReflectorID() { return reflectorID; }
    public List<Pair<Character, Character>> getPlugsInUseList() { return plugsInUseList; }
    public int getNumOfUsedRotors() { return rotorInUseIDList.size(); }
    public String getABC() { return abc; }
}
