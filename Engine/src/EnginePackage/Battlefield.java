package EnginePackage;

import DecryptionManager.Difficulty;
import Tools.Machine;
import enigmaException.EnigmaException;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class Battlefield {

    private EngineCapabilities engine = new EnigmaEngine();
    private String name;
    private int numOfAllies;
    private Difficulty level;

    public EngineCapabilities getEngine() { return engine; }

    public void createBattlefieldFromXMLInputStream(InputStream inputStream)
            throws EnigmaException, JAXBException, FileNotFoundException {

        Factory factory = new Factory(inputStream);
        engine.setMachine(factory.createMachine());
        name = factory.getBattleName();
        numOfAllies = factory.getNumberOfAllies();
        level = factory.getDifficulty();
    }
    public String getName(){
        return name;
    }

    public int getNumOfAllies() {
        return numOfAllies;
    }

    public Difficulty getLevel() {
        return level;
    }
}
