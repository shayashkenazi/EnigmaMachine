package EnginePackage;

//import Generated.*;
import DecryptionManager.Difficulty;
import Generated.generated_3.*; //ex3
//import DecryptionManager.Generated.*; //ex2
import Tools.*;
import enigmaException.EnigmaException;

import javax.xml.bind.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

public class Factory {

    private final static String JAXB_XML_GAME_PACKAGE_NAME = "Generated.generated_3";
    private final CTEEnigma cteEnigma;

    public CTEEnigma getCteEnigma() {
        return cteEnigma;
    }

    public Factory(String xmlFilePath) throws FileNotFoundException,JAXBException {

        InputStream inputStream = new FileInputStream(new File(xmlFilePath));
        cteEnigma = deserializeFrom(inputStream);
    }
    public Factory( InputStream inputStream) throws FileNotFoundException,JAXBException {
        cteEnigma = deserializeFrom(inputStream);
    }

    private static CTEEnigma deserializeFrom(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_GAME_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        return (CTEEnigma) u.unmarshal(in);
    }

    private Map<String,Reflector> createReflectors() throws EnigmaException{

        Map<String,Reflector> reflectors = new HashMap<>();

        checkRomaNumerals(cteEnigma.getCTEMachine().getCTEReflectors().getCTEReflector());
        for (CTEReflector cteReflector : cteEnigma.getCTEMachine().getCTEReflectors().getCTEReflector()) {

            reflectors.put(cteReflector.getId().toUpperCase(),createReflector(cteReflector));
        }

        return reflectors;
    }
    private Reflector createReflector(CTEReflector cteReflector) throws EnigmaException {

        Map<Character,Integer> tempABC = createABC();
        Reflector reflector = new Reflector(cteReflector.getId(),tempABC.size());
        checkDuplicatesAtReflect(cteReflector.getCTEReflect());
        for (CTEReflect cteReflect : cteReflector.getCTEReflect()) {
            reflector.setPair(cteReflect.getInput() - 1, cteReflect.getInput() - 1,cteReflect.getOutput() - 1);
            reflector.setPair(cteReflect.getOutput() - 1, cteReflect.getOutput() - 1,cteReflect.getInput() - 1);
        }
        return reflector;
    }
    private void checkRomaNumerals(List<CTEReflector> reflectorArrayList) throws EnigmaException {

        //List<String> list = Arrays.asList("I","II","III","IV","V");
        Map<String, Integer> MapNumbers = new LinkedHashMap<>();
        MapNumbers.put("I", 0);
        MapNumbers.put("II", 0);
        MapNumbers.put("III", 0);
        MapNumbers.put("IV", 0);
        MapNumbers.put("V", 0);
        for (CTEReflector cteRef : reflectorArrayList) {
            if (MapNumbers.containsKey(cteRef.getId().toUpperCase())) {
                int count = MapNumbers.get(cteRef.getId().toUpperCase());
                MapNumbers.put(cteRef.getId(), count + 1);
                if (MapNumbers.get(cteRef.getId().toUpperCase()) != 1)
                    throw new EnigmaException("Same reflector ID insert");
            } else
                throw new EnigmaException("reflector ID incorrect");
        }
        int size = reflectorArrayList.size();
        for (Map.Entry<String, Integer> entry : MapNumbers.entrySet()) {
            if (size != 0) {
                if(entry.getValue() != 1)
                    throw new EnigmaException("reflector ID incorrect");
            }
            else
                break;
            size--;
        }
    }

    private void checkDuplicatesAtReflect(List<CTEReflect> curReflect) throws EnigmaException {
        Map<Integer, Integer> mapCheck = new HashMap<>();
        Set<Integer> set = new HashSet<>();
        for(CTEReflect ref: curReflect )
        {
            if(ref.getInput() == ref.getOutput())
                throw new EnigmaException("There is the same input output at reflector!");
            if(ref.getInput() < 1 || ref.getOutput() < 1 || ref.getInput() > curReflect.size()*2 || ref.getOutput() > curReflect.size()*2)
                throw new EnigmaException("There is the input output out of range!");
            if(!mapCheck.containsKey(ref.getInput()))
                mapCheck.put(ref.getInput(),ref.getOutput());
            else
                throw new EnigmaException("There is duplicate input!");
            if(set.contains(ref.getInput()) || set.contains(ref.getOutput()))
                throw new EnigmaException("There is duplicate input output!");
            set.add(ref.getInput());
            set.add(ref.getOutput());
        }
        if(!isOneToOne(mapCheck))
            throw new EnigmaException("There is duplicate output!");
        if(mapCheck.size() * 2 != createABC().size())
            throw new EnigmaException("Not all the abc assert!");
    }

    private void checkDuplicatesAtRotor(List<CTEPositioning> curRotorPosition) throws EnigmaException {
        Map<Character,Character> mapCheck = new HashMap<>();
        for(CTEPositioning pos: curRotorPosition )
        {
            if(pos.getLeft().length() != 1 || pos.getRight().length() != 1)
                throw new EnigmaException("There is incorrect input");
            if(!mapCheck.containsKey(pos.getRight().toUpperCase().charAt(0)))
                mapCheck.put(pos.getRight().toUpperCase().charAt(0),pos.getLeft().toUpperCase().charAt(0));
            else
                throw new EnigmaException("There is duplicate Right!");
        }
        if(!isOneToOne(mapCheck))
            throw new EnigmaException("There is duplicate Left!");
        if(mapCheck.size() != createABC().size())
            throw new EnigmaException("Not all the specific abc assert!");

    }
    private boolean isOneToOne(Map<?, ?> map) {
        Set<?> set = new HashSet<>(map.values());
        return set.size() == map.keySet().size();
    }

    private Map<String,Rotor> createRotors() throws EnigmaException {
        Map<String,Rotor> rotors = new HashMap<>();
        CTEMachine cteMachine = cteEnigma.getCTEMachine();

        if (cteMachine.getRotorsCount() > cteMachine.getCTERotors().getCTERotor().size() || cteMachine.getRotorsCount() < 2)
            throw new EnigmaException("Number of provided rotors is NOT valid !");

        cteMachine.getCTERotors().getCTERotor().sort(new Comparator<CTERotor>() {
            @Override
            public int compare(CTERotor o1, CTERotor o2) {
                return o1.getId() - o2.getId();
            }
        });
        int idCounter = 1;
        for (CTERotor cteRotor : cteMachine.getCTERotors().getCTERotor()) {
            if (cteRotor.getId() != idCounter)
                throw new EnigmaException("ID for the Rotor are invalid !");

            rotors.put(Integer.toString(cteRotor.getId()), createRotor(cteRotor));
            idCounter++;
        }

        return rotors;
    }

    public String getBattleName() {
        return cteEnigma.getCTEBattlefield().getBattleName();
    }

    public int getNumberOfAllies() throws EnigmaException {
        int res = cteEnigma.getCTEBattlefield().getAllies();

        if (res < 1)
            throw new EnigmaException("Number of Allies should be a positive number!");

        return res;
    }

    public Difficulty getDifficulty() throws EnigmaException {
        String difficulty = cteEnigma.getCTEBattlefield().getLevel().toUpperCase();
        Difficulty res;

        switch (difficulty) {
            case "EASY":
                res = Difficulty.EASY;
                break;

            case "MEDIUM":
                res = Difficulty.MEDIUM;
                break;

            case "HARD":
                res = Difficulty.HARD;
                break;

            case "IMPOSSIBLE":
                res = Difficulty.IMPOSSIBLE;
                break;

            default:
                throw new EnigmaException("Difficulty is wrong!");
        }

        return res;
    }

    private Map<Character,Integer> createABC() throws EnigmaException {

        String abc = cteEnigma.getCTEMachine().getABC().trim().toUpperCase();
        if (abc.length() % 2 != 0)
            throw new EnigmaException(" ABC is NOT Even!");
        Map<Character,Integer> map = new HashMap<>();
        for(Integer i = 0; i < abc.length(); i++)
            map.put(abc.charAt(i),i);
        return map;
    }

    private Rotor createRotor(CTERotor cteRotor) throws EnigmaException {

        Map<Character,Integer> tempABC = createABC();
        if(cteRotor.getNotch() < 1 || cteRotor.getNotch() > tempABC.size())
            throw new EnigmaException("The notch is out of range!");
        Rotor rotor = new Rotor(Integer.toString(cteRotor.getId()), cteRotor.getNotch() - 1,tempABC.size());
        checkDuplicatesAtRotor(cteRotor.getCTEPositioning());
        int i = 0;
        for (CTEPositioning position : cteRotor.getCTEPositioning()) {
            rotor.setPair(i, tempABC.get(position.getRight().toUpperCase().charAt(0)),tempABC.get(position.getLeft().toUpperCase().charAt(0)));
            //rotor.setPair(i, tempABC.get(position.getLeft().charAt(0)),tempABC.get(position.getRight().charAt(0)));
            i++;
        }
        return rotor;
    }

    private String createAbc() {
        return cteEnigma.getCTEMachine().getABC().trim();
    }
    /*private int createNumOfMaxAgents() throws EnigmaException {
        int res = cteEnigma.getCTEDecipher().getAgents();
        if(res< 2 || res > 50)
            throw new EnigmaException("The number of agents is out of range!");
        return res;
    }*/

    private Set<String> createDictionary(){

        String words = cteEnigma.getCTEDecipher().getCTEDictionary().getWords();
        words = words.trim();
        String exclude = cteEnigma.getCTEDecipher().getCTEDictionary().getExcludeChars();
        words = words.toUpperCase();
        for(int i = 0; i < exclude.length(); i++) {
            String ch = String.valueOf(exclude.charAt(i));
            words = words.replace(ch,"");
        }
        String[] allWords = words.split(" ");

        Set<String> set = new HashSet<>(Arrays.asList(allWords));

        return set;

    }
    private Set<Character> createExcludeChars(){
        String exclude = cteEnigma.getCTEDecipher().getCTEDictionary().getExcludeChars();
        List<Character> myChars = exclude
                .chars()
                .mapToObj(e -> (char)e)
                .collect(Collectors.toList());
        Set<Character> set = new HashSet<>(myChars);
        return set;
    }
    private String getBattlefieldSetting(){
        return cteEnigma.getCTEBattlefield().getBattleName();
    }
    private int getNumOfAllies(){
        return cteEnigma.getCTEBattlefield().getAllies();
    }
    private String getLevel(){
        return cteEnigma.getCTEBattlefield().getLevel();
    }

    public Machine createMachine() throws EnigmaException {

        Machine machine = new Machine();
        machine.setABC(createAbc());
        machine.setABCmap(createABC());
        machine.setRotorsCount(cteEnigma.getCTEMachine().getRotorsCount());
        machine.setRotors(createRotors());
        machine.setReflectors(createReflectors());
        machine.setExcludeChars(createExcludeChars());
        machine.setMyDictionary(createDictionary());
        //machine.setNumOfMaxAgents(createNumOfMaxAgents()); // EX2
        return machine;
    }
}