package DecryptionManager;


import DTOs.DTO_CodeDescription;
import EnginePackage.EngineCapabilities;
import EnginePackage.EnigmaEngine;
import Tools.Machine;
import Tools.Reflector;
import Tools.Rotor;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class DecryptionManager {

    private final int capacity = 10;
    private BlockingQueue<Runnable> tasks = new LinkedBlockingQueue<>(capacity);
    private int numOfAgents;
    private EnigmaEngine copyEngine;
    private ThreadPoolExecutor poolMission;
    private String sentenceToCheck;

    public DecryptionManager(EnigmaEngine engine,String sentence){
        copyEngine = engine.clone();
        poolMission = new ThreadPoolExecutor(3,3,5,TimeUnit.MILLISECONDS,tasks);
        sentenceToCheck = sentence;
    }

    private void runTasks(){
        poolMission.shutdown();
    }

    private void setMachineReflector(String reflectorID,Machine machine){

        int size = machine.getRotorsStack().size();
        Reflector ref = (Reflector)machine.getRotorsStack().get(size - 1);
        machine.getRotorsStack().set(size - 1,machine.getReflectorsMap().get(reflectorID));
    }

    //
    public void createEasyTasks(EngineCapabilities engineCopy) {

        int taskSize = 7; // TODO: take this parameter from the fxml property ?? No

        // Initialize all Rotors to start index position 0,0,0...
        for (int i = 0; i < engineCopy.getMachine().getRotorsInUseCount(); i++) {
            Rotor currRotor = (Rotor) engineCopy.getMachine().getRotorsStack().get(i);
            currRotor.setCurrentPairIndex(0);
        }

        // Current State - All rotors are positioned to the first char in the ABC
        double sizeOfAllTasks = Math.pow(engineCopy.getMachine().getABCsize(), engineCopy.getMachine().getRotorsInUseCount());
        int sizeOfFullTasks = (int) (sizeOfAllTasks / taskSize);
        int lastTaskSize = (int) (sizeOfAllTasks % taskSize);
        poolMission.prestartAllCoreThreads();
        try {
            // Full Tasks
            for (int i = 0; i < sizeOfFullTasks; i++) {

                DecryptionTask decryptionTask = new DecryptionTask(engineCopy.clone(), taskSize,sentenceToCheck);
                tasks.put(decryptionTask);
                engineCopy.moveRotorsToPosition(taskSize);
            }

            // Last little task
            DecryptionTask decryptionTask = new DecryptionTask(engineCopy.clone(), lastTaskSize,sentenceToCheck);
            tasks.put(decryptionTask);
        /*try {
            tasks.put(decryptionTask);
        }
        catch (InterruptedException e){
            System.out.println(e.getMessage());
        }*/
        }
        catch (Exception ee){
        System.out.println(ee.getMessage());
        }


        for (Runnable task : tasks){ // TODO: DELETEEEEEEEEEEEEEEE
            task.run();
        }
    }

    public void createMediumTasks(EngineCapabilities engineCopy) {
        String[] RotorsId = {"I", "II", "III", "IV", "V"};
        for (int i = 0; i < engineCopy.getMachine().getReflectorsMapSize(); i++) {
            EngineCapabilities e = engineCopy.clone();
            setMachineReflector(RotorsId[i],e.getMachine());
            createEasyTasks(e.clone());
        }
    }
}

