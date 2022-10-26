package DTOs;

import Tools.Reflector;
import Tools.Rotor;

import java.util.Map;
import java.util.Set;

public class DTO_AgentMachine {

    private String abc;
    private Map<String, Rotor> rotorsMap;
    private Map<String, Reflector> reflectorsMap;
    private Map<Character,Integer> abcMap;
    private int rotorsCount;
    private Set<String> myDictionary;

    public DTO_AgentMachine(String abc, Map<String, Rotor> rotorsMap, Map<String,
            Reflector> reflectorsMap, Map<Character, Integer> abcMap, int rotorsCount, Set<String> myDictionary) {
        this.abc = abc;
        this.rotorsMap = rotorsMap;
        this.reflectorsMap = reflectorsMap;
        this.abcMap = abcMap;
        this.rotorsCount = rotorsCount;
        this.myDictionary = myDictionary;
    }

    public String getAbc() {
        return abc;
    }

    public Map<String, Rotor> getRotorsMap() {
        return rotorsMap;
    }

    public Map<String, Reflector> getReflectorsMap() {
        return reflectorsMap;
    }

    public Map<Character, Integer> getAbcMap() {
        return abcMap;
    }

    public int getRotorsCount() {
        return rotorsCount;
    }

    public Set<String> getMyDictionary() {
        return myDictionary;
    }
}
