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

    private final int capacity = 5000;
    private BlockingQueue<Runnable> tasks = new LinkedBlockingQueue<>(capacity);
    private int numOfAgents;
    private EnigmaEngine copyEngine;
    private ThreadPoolExecutor poolMission;

    public DecryptionManager(EnigmaEngine engine){
        copyEngine = engine.clone();
    }

    private void runTasks(){
        poolMission.shutdown();
    }

    private void setMachineReflector(String reflectorID,Machine machine){

        int size = copyEngine.getMachine().getRotorsStack().size();
        Reflector ref = (Reflector)copyEngine.getMachine().getRotorsStack().get(size - 1);
        machine.getRotorsStack().set(size - 1,ref);
    }

    //
    public void createEasyTasks(EngineCapabilities engineCopy) {

        int taskSize = 7; // TODO: take this parameter from the fxml property

        // Initialize all Rotors to start index position 0,0,0...
        for (int i = 0; i < engineCopy.getMachine().getRotorsInUseCount(); i++) {
            Rotor currRotor = (Rotor) engineCopy.getMachine().getRotorsStack().get(i);
            currRotor.setCurrentPairIndex(0);
        }

        // Current State - All rotors are positioned to the first char in the ABC
        double sizeOfAllTasks = Math.pow(engineCopy.getMachine().getABCsize(), engineCopy.getMachine().getRotorsInUseCount());
        int sizeOfFullTasks = (int) (sizeOfAllTasks / taskSize);
        int lastTaskSize = (int) (sizeOfAllTasks % taskSize);

        // Full Tasks
        for (int i = 0; i < sizeOfFullTasks; i++) {

            DecryptionTask decryptionTask = new DecryptionTask(engineCopy.clone(), taskSize);
            tasks.offer(decryptionTask);
            engineCopy.moveRotorsToPosition(taskSize);
        }

        // Last little task
        DecryptionTask decryptionTask = new DecryptionTask(engineCopy.clone(), lastTaskSize);
        tasks.offer(decryptionTask);

        for (Runnable task : tasks){ // TODO: DELETEEEEEEEEEEEEEEE
            task.run();
        }
    }
}
