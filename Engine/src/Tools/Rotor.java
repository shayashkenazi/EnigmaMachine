package Tools;

import javafx.util.Pair;

public class Rotor extends Switcher {

    private int notch;
    private int currentPairIndex;

    private Pair<Character, Integer> firstPair;

    public Rotor (String ID, int notch, int lengthABC) {
        super(ID,lengthABC);
        this.notch = notch;
    }

    @Override
    public int Switch(int input, boolean direction) {

        if (direction)
            return  SwitchFromReflectorToRotors(input);
        else
            return SwitchFromRototrsToReflectors(input);
    }

    public int SwitchFromReflectorToRotors(int input) { // from the first rotor to the reflector

        Integer keyToSearch = inputOutput.get((currentPairIndex + input) % inputOutput.size()).getKey();
        int currentIndex = currentPairIndex;
        int numOfStepsFromCurrentPosition = 0;

        while (!inputOutput.get(currentIndex).getValue().equals(keyToSearch)) {

            currentIndex = (currentIndex + 1) % inputOutput.size();
            numOfStepsFromCurrentPosition++;
        }

        return numOfStepsFromCurrentPosition;
    }
    public int SwitchFromRototrsToReflectors(int input) { // from the reflector to the start rotor

        Integer keyToSearch = inputOutput.get((currentPairIndex + input) % inputOutput.size()).getValue();
        int currentIndex = currentPairIndex;
        int numOfStepsFromCurrentPosition = 0;

        while (!inputOutput.get(currentIndex).getKey().equals(keyToSearch)) {

            currentIndex = (currentIndex + 1) % inputOutput.size();
            numOfStepsFromCurrentPosition++;
        }

        return numOfStepsFromCurrentPosition;
    }

    public int getCurrentPairIndex() { return currentPairIndex; }
    public Pair<Character, Integer> getFirstPairPosition(){ return firstPair;}

    public int getNotch(){return notch;}
    public void setCurrentPairIndex(int pairIndex) { currentPairIndex = pairIndex; }
    public void setFirstPairIndex(Pair<Character, Integer> firstPositionPair) { firstPair = firstPositionPair; }
    public void rotateRotor() {

        currentPairIndex = (currentPairIndex + 1) % inputOutput.size();
    }

    public void initilaizeCurrentPosition() {
        //find the firstChar on the right side of the rotor and use the index
        currentPairIndex = firstPair.getValue();
    }
}
