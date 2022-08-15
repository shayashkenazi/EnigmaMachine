package Tools;

import DTOs.DTO_CodeDescription;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Machine {

    private Map<String, Rotor> rotorsMap;
    private Map<String,Reflector> reflectorsMap;
    private List <Switcher> rotorsStack = new ArrayList<>();
    private Map<Character,Integer> abcMap;
    private String abc;
    private int rotorsCount;
    private PlugBoard plugBoard = new PlugBoard();
    private int numOfProccessedMsg = 0;


    public int getNumOfProccessedMsg() { return numOfProccessedMsg; }
    public void increaseNumOfProccessedMsg() { numOfProccessedMsg++; }
    public int getRotorsMapSize() {
        return rotorsMap.size();
    }
    public Map<String, Rotor> getRotorsMap(){ return rotorsMap;}
    public int getRotorsInUseCount(){ return rotorsCount;}
    public String getAbc() { return abc;}
    public Map<Character,Character> getFirstSidePlugBoardMap(){
        return plugBoard.getFirstSidePlugBoardMap();
    }
    public Map<Character,Character> getSecondSidePlugBoardMap(){
        return plugBoard.getSecondSidePlugBoardMap();
    }
    public int getReflectorsMapSize() {
        return reflectorsMap.size();
    }

    public List<Switcher> getRotorsStack() {
        return rotorsStack;
    }

    public int getABCsize() { return abcMap.size();}
    public void setRotors(Map<String,Rotor> rotors) {
        this.rotorsMap = rotors;
    }

    public void setRotorsCount(int numOfRotors){
        this.rotorsCount = numOfRotors;
    }

    public void setReflectors(Map<String,Reflector> reflectors) {
        this.reflectorsMap = reflectors;
    }
    public void setABCmap(Map<Character,Integer> mapABC) {
        this.abcMap = mapABC;
    }
    public void setABC(String ABC) {this.abc = ABC; }
    public void buildRotorsStack(DTO_CodeDescription codeDescription) {

        for (int i = 0; i < codeDescription.getRotorsInUseIDList().size(); i++) { // Set all the info for the Rotors

            Rotor currentRotor = rotorsMap.get(codeDescription.getRotorsInUseIDList().get(i).getKey()); // get the rotor with the same ID
            currentRotor.setCurrentPairIndex(searchStartPoint(currentRotor, abcMap.get(codeDescription.getStartingPositionList().get(i)))); // set pointer to the starting point for this Rotor
            Pair<Character, Integer> firstPair = new Pair<>(codeDescription.getStartingPositionList().get(i), searchStartPoint(currentRotor, abcMap.get(codeDescription.getStartingPositionList().get(i))));
            currentRotor.setFirstPairIndex(firstPair); // set the starting char for this Rotor
            rotorsStack.add(currentRotor);
        }
        rotorsStack.add(reflectorsMap.get(codeDescription.getReflectorID()));


    }

    private int searchStartPoint(Rotor rotor, Integer ch) {
        int i = 0;
        for(Pair<Integer, Integer> pair : rotor.inputOutput) {
            if(pair.getKey().equals(ch))
                return i;
            i++;
        }
        return -1;
    }

    public void initializePositionsForRotorsInStack() {

        for (int rotorInd = 0; rotorInd < rotorsStack.size() - 1; rotorInd++){
            Rotor currRotor = (Rotor)rotorsStack.get(rotorInd);
            currRotor.initilaizeCurrentPosition();
        }
    }

    public void buildPlugBoard(List<Pair<Character,Character>> listOfPlugBoard ) {
        for(Pair<Character,Character> pair: listOfPlugBoard) {
            plugBoard.addToMap(pair);
        }
    }
    public String buildStringWithPlugBoard(String curString) {
        String res = new String();

        for(int i =0; i < curString.length(); i++) {
            res += plugBoard.switchChar(curString.charAt(i));
        }
        return res;
    }

    public Integer convertCharToInt(Character charToConvert) {
        return abcMap.get(charToConvert);
    }

    public Character convertIntToChar(Integer intToConvert) {

        for (Map.Entry<Character, Integer> entry : abcMap.entrySet()) {

            if (entry.getValue() == intToConvert)
                return entry.getKey();
        }
        return '@'; // TBD
    }
}
