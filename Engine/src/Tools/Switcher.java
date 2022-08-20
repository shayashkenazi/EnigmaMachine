package Tools;

import javafx.util.Pair;

import java.io.Serializable;
import java.util.*;

public abstract class Switcher implements Serializable {

    private String id;
    protected List<Pair<Integer, Integer>> inputOutput;

    public Switcher(String ID, int lengthABC) {
        inputOutput = new ArrayList<Pair<Integer, Integer>>(lengthABC);
        for (int i = 0; i < lengthABC; i++) {
            inputOutput.add(new Pair<>(0,0));
        }
        id = ID;
    }

    public abstract int Switch(int input,boolean direction);

    public String getID(){ return id;}

    public void setPair(Integer input, Integer output) {
        inputOutput.add(new Pair<>(input, output));
    }
    public void setPair(int index, Integer input, Integer output) {
        inputOutput.set(index,new Pair<>(input, output));
    }

}
