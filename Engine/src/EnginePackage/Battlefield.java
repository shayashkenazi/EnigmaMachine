package EnginePackage;

import DecryptionManager.Difficulty;
import Tools.Machine;
import enigmaException.EnigmaException;
import users.DMManager;
import users.ReadyManager;
import users.ResultsManager;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class Battlefield {

    private EngineCapabilities engine = new EnigmaEngine();
    private String name;
    private int numOfAllies;
    private Difficulty level;
    private DMManager dmManager = new DMManager();
    private ReadyManager readyManager = new ReadyManager();
    private ResultsManager resultsManager = new ResultsManager();

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

    public DMManager getDmManager() {
        return dmManager;
    }

    public ReadyManager getReadyManager() {
        return readyManager;
    }
    public ResultsManager getResultsManager() {
        return resultsManager;
    }
}
