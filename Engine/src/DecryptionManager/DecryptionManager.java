package DecryptionManager;


import EnginePackage.EnigmaEngine;
import Tools.Machine;
import Tools.Reflector;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DecryptionManager {

    private List<Task> tasks = new ArrayList<>();

    private Thread tasksManager;
    private int numOfAgents;

    private EnigmaEngine copyEngine;

    private ExecutorService poolMission;

    public DecryptionManager(EnigmaEngine engine){
        copyEngine = engine.clone();
    }

    private void createNumOfThreadsAtPool() {
        poolMission = Executors.newFixedThreadPool(numOfAgents);
    }
    private void createPoolMission(){ //TODO Check if the poolMission is full
        for(Task task:tasks){
            poolMission.execute(task);
        }
    }
    private void runTasks(){
        poolMission.shutdown();
    }

    private void setMachineReflector(String reflectorID,Machine machine){

        int size = copyEngine.getMachine().getRotorsStack().size();
        Reflector ref = (Reflector)copyEngine.getMachine().getRotorsStack().get(size - 1);
        machine.getRotorsStack().set(size - 1,ref);
    }



}
