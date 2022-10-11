package EnginePackage;

import DecryptionManager.Difficulty;

public class Battlefield {

    private EngineCapabilities engine = new EnigmaEngine();
    private String battlefieldName;
    private int numOfAllies;
    private Difficulty level;

    public EngineCapabilities getEngine() { return engine; }

}
