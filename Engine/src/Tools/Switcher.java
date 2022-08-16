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
    /*public int SwitchFromReflectorToRotors(int input) { // from the first rotor to the reflector
        Integer ch = inputOutput.get(input).getKey();
        for(int i = 0; i < inputOutput.size(); i++) {
            if(inputOutput.get(i).getValue().equals(ch))
                return i;
        }
        return -1; // checking if exception
    }
    public int SwitchFromRototrsToReflectors(int input) { // from the reflector to the start rotor

        Integer ch = inputOutput.get(input).getValue();
        for(int i = 0; i < inputOutput.size(); i++) {
            if(inputOutput.get(i).getKey().equals(ch))
                return i;
        }
        return -1; // checking if exception
    }*/

    public String getID(){ return id;}

    public void setPair(Integer input, Integer output) {
        inputOutput.add(new Pair<>(input, output));
    }
    public void setPair(int index, Integer input, Integer output) {
        inputOutput.set(index,new Pair<>(input, output));
    }

}
