package DecryptionManager;
import EnginePackage.EngineCapabilities;
import EnginePackage.EnigmaEngine;
import Tools.Machine;
import Tools.Reflector;
import Tools.Rotor;

import java.util.Arrays;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class DM { // Relevant for Ex3 only (for Ex2 use DecryptionManager)

    private final int capacity = 1000;
    private BlockingQueue<DmTask> tasks = new LinkedBlockingQueue<>(capacity);
    //private BlockingQueue<Runnable> results = new LinkedBlockingQueue<>(capacity);
    private EnigmaEngine copyEngine;
    private Difficulty difficulty;
    private int taskSize;
    private String sentenceToCheck;

    public DM (EnigmaEngine copyEngine, Difficulty difficulty, int taskSize,String sentenceToCheck) {
        this.copyEngine = copyEngine;
        this.difficulty = difficulty;
        this.taskSize = taskSize;
        this.sentenceToCheck = sentenceToCheck;
    }

    public void run() {
        switch (difficulty){
            case EASY:
                createEasyTasks(copyEngine);
                break;
            case MEDIUM:
                createMediumTasks(copyEngine);
                break;
            case HARD:
                createHardTasks(copyEngine);
                break;
            case IMPOSSIBLE:
                createImpossibleTasks(copyEngine);
                break;
        }
    }

    public void createEasyTasks(EngineCapabilities engineCopy) {
        // Initialize all Rotors to start index position 0,0,0...
        for (int i = 0; i < engineCopy.getMachine().getRotorsInUseCount(); i++) {
            Rotor currRotor = (Rotor) engineCopy.getMachine().getRotorsStack().get(i);
            currRotor.setCurrentPairIndex(0);
        }

        // Current State - All rotors are positioned to the first char in the ABC
        double sizeOfAllTasks = Math.pow(engineCopy.getMachine().getABCsize(), engineCopy.getMachine().getRotorsInUseCount());
        int sizeOfFullTasks = (int) (sizeOfAllTasks / taskSize);
        int lastTaskSize = (int) (sizeOfAllTasks % taskSize);
        try {
            // Full Tasks
            for (int i = 0; i < sizeOfFullTasks; i++) {

                DmTask decryptionTask = new DmTask(engineCopy.clone(), taskSize,sentenceToCheck);
                tasks.put(decryptionTask);
                engineCopy.moveRotorsToPosition(taskSize);
            }

            // Last little task
            DmTask decryptionTask = new DmTask(engineCopy.clone(), lastTaskSize,sentenceToCheck);
            tasks.put(decryptionTask);

        } catch (Exception ee) {
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
        Arrays.sort(arrRotors);
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

    public void createImpossibleTasks(EngineCapabilities engineCopy) {
        String[] arrRotorsID = new String[engineCopy.getMachine().getRotorsMapSize()];
        for(int i = 1; i < engineCopy.getMachine().getRotorsMapSize() + 1; i++){
            arrRotorsID[i-1] =  String.valueOf(i);
        }
        String[] res = new String[copyEngine.getMachine().getRotorsInUseCount()];
        combinationsAndHardTask(arrRotorsID,copyEngine.getMachine().getRotorsInUseCount(),0,res);
    }
    private void setMachineReflector(String reflectorID, Machine machine){

        int size = machine.getRotorsStack().size();
        Reflector ref = (Reflector)machine.getRotorsStack().get(size - 1);
        machine.getRotorsStack().set(size - 1,machine.getReflectorsMap().get(reflectorID));
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
    public EngineCapabilities updateSpecificRotorsOrder(String[] rotorsOrder,EngineCapabilities copyEngine) {
        int index = 0;
        for (String rotorId : rotorsOrder) {
            copyEngine.getMachine().getRotorsStack().set(index++, copyEngine.getMachine().getRotorsMap().get(rotorId));
        }
        return copyEngine;
    }
}
