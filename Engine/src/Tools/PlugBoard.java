package Tools;

import javafx.util.Pair;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class PlugBoard implements Serializable {

    private Map<Character,Character> plugBoardMapFirstSide = new HashMap<>();
    private Map<Character,Character> plugBoardMapSecondSide = new HashMap<>();

    public Map<Character,Character> getFirstSidePlugBoardMap() { return plugBoardMapFirstSide;}
    public Map<Character,Character> getSecondSidePlugBoardMap() { return plugBoardMapSecondSide;}
    public void addToMap(Pair<Character,Character> PlugBoardPair) {

        plugBoardMapFirstSide.put(PlugBoardPair.getKey(), PlugBoardPair.getValue());
        plugBoardMapSecondSide.put(PlugBoardPair.getValue(), PlugBoardPair.getKey());
    }
    public Character switchChar(Character ch) {

        if (plugBoardMapFirstSide.containsKey(ch))
            return plugBoardMapFirstSide.get(ch);

        return plugBoardMapSecondSide.containsKey(ch) ? plugBoardMapSecondSide.get(ch) : ch;
    }
}
