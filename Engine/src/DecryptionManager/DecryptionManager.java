package DecryptionManager;


import EnginePackage.EngineCapabilities;
import EnginePackage.EnigmaEngine;
import Tools.Machine;
import Tools.Reflector;
import Tools.Rotor;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.concurrent.*;
import java.util.function.Consumer;

public class DecryptionManager {

    private final int capacity = 1000;
    private BlockingQueue<Runnable> tasks = new LinkedBlockingQueue<>(capacity);
    private BlockingQueue<Runnable> results = new LinkedBlockingQueue<>(capacity);
    private EnigmaEngine copyEngine;
    private ThreadPoolExecutor poolMission;
    private ThreadPoolExecutor poolResult;
    private String sentenceToCheck;
    private Difficulty difficulty;
    private int numberOfAgents;
    private int taskSize;
    private Consumer<String> MsgConsumer;
    private TextArea ta_candidates;

    public DecryptionManager(EnigmaEngine engine, String sentence, int numOfAgents,
                             Difficulty difficulty, int taskSize, Consumer<String> msgConsumer, TextArea ta_candidates){
        copyEngine = engine;
        sentenceToCheck = sentence;
        this.difficulty = difficulty;
        numberOfAgents = numOfAgents;
        this.taskSize = taskSize;
        poolMission = new ThreadPoolExecutor(numberOfAgents,numberOfAgents,5,TimeUnit.MILLISECONDS, tasks);
        poolResult = new ThreadPoolExecutor(1, 1, 5, TimeUnit.MILLISECONDS, results);
        this.ta_candidates = ta_candidates;
        // TODO: what is keepAlive?
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
        poolResult.prestartAllCoreThreads();
        try {
            // Full Tasks
            for (int i = 0; i < sizeOfFullTasks; i++) {

                DecryptionTask decryptionTask = new DecryptionTask(engineCopy.clone(), taskSize, sentenceToCheck, results,ta_candidates);
                tasks.put(decryptionTask);
                engineCopy.moveRotorsToPosition(taskSize);
            }

            // Last little task
            DecryptionTask decryptionTask = new DecryptionTask(engineCopy.clone(), lastTaskSize,sentenceToCheck, results,ta_candidates);
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

    }

    public void createMediumTasks(EngineCapabilities engineCopy) {
        String[] RotorsId = {"I", "II", "III", "IV", "V"};
        for (int i = 0; i < engineCopy.getMachine().getReflectorsMapSize(); i++) {
            EngineCapabilities e = engineCopy.clone();
            setMachineReflector(RotorsId[i],e.getMachine());
            createEasyTasks(e.clone());
        }
    }
    public void createHardTasks(EngineCapabilities engineCopy) {
        String[] arrRotors = new String[engineCopy.getMachine().getRotorsInUseCount()];
        for (int i = 0; i < engineCopy.getMachine().getRotorsInUseCount(); i++) {
            arrRotors[i] = engineCopy.getMachine().getRotorsStack().get(i).getID();
        }
        permuteAndMedTask(arrRotors,0);
    }
    public void permuteAndMedTask(String[] RotorIdArray, int start) {
        for(int i = start; i < RotorIdArray.length; i++){
            String temp = RotorIdArray[start];
            RotorIdArray[start] = RotorIdArray[i];
            RotorIdArray[i] = temp;
            permuteAndMedTask(RotorIdArray, start + 1);
            RotorIdArray[i] = RotorIdArray[start];
            RotorIdArray[start] = temp;
        }
        if (start == RotorIdArray.length - 1) {
            EngineCapabilities e = updateSpecificRotorsOrder(RotorIdArray,copyEngine.clone());
            createMediumTasks(e);
        }
    }
    public EngineCapabilities updateSpecificRotorsOrder(String[] rotorsOrder,EngineCapabilities copyEngine) {
        int index = 0;
        for (String rotorId : rotorsOrder) {
            copyEngine.getMachine().getRotorsStack().set(index++, copyEngine.getMachine().getRotorsMap().get(rotorId));
        }
        return copyEngine;
    }

    public void createImpossibleTasks(EngineCapabilities engineCopy) {
        String[] arrRotorsID = new String[engineCopy.getMachine().getRotorsMapSize()];
        for(int i = 1; i < engineCopy.getMachine().getRotorsMapSize() + 1; i++){
            arrRotorsID[i-1] =  String.valueOf(i);
        }
        String[] res = new String[copyEngine.getMachine().getRotorsInUseCount()];
        combinationsAndHardTask(arrRotorsID,copyEngine.getMachine().getRotorsInUseCount(),0,res);
    }
    private void combinationsAndHardTask(String[] arr, int len, int startPosition, String[] result){
        if (len == 0){
            EngineCapabilities e = updateSpecificRotorsOrder(result,copyEngine.clone());
            createHardTasks(e);
            return;
        }
        for (int i = startPosition; i <= arr.length-len; i++){
            result[result.length - len] = arr[i];
            combinationsAndHardTask(arr, len-1, i+1, result);
        }
    }
}

