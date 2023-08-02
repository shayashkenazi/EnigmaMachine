package DecryptionManager;
import DTOs.DTO_CodeDescription;
import DTOs.DTO_MachineInfo;
import EnginePackage.EngineCapabilities;
import EnginePackage.EnigmaEngine;
import Tools.Machine;
import Tools.Reflector;
import Tools.Rotor;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class DM { // Relevant for Ex3 only (for Ex2 use DecryptionManager)

    private final int capacity = 1000;
    private BlockingQueue<DmTask> tasks = new LinkedBlockingQueue<>(capacity);
    //private BlockingQueue<Runnable> results = new LinkedBlockingQueue<>(capacity);
    private EngineCapabilities copyEngine;
    private Difficulty difficulty;
    private int taskSize;
    private Thread createTaskDMThread;

    public int getAllTaskSize() {
        return allTaskSize.get();
    }


    private IntegerProperty allTaskSize = new SimpleIntegerProperty(0);
    private AtomicInteger counterOfCreatedTasks = new AtomicInteger(0);

    private String sentenceToCheck;

    public DM (EngineCapabilities copyEngine, Difficulty difficulty, int taskSize) {
        this.copyEngine = copyEngine;
        this.difficulty = difficulty;
        this.taskSize = taskSize;
    }
    public AtomicInteger getCounterOfCreatedTasks() {
        return counterOfCreatedTasks;
    }
    public void setSentenceToCheck(String sentenceToCheck) {
        this.sentenceToCheck = sentenceToCheck;
    }
    public synchronized void run() {
        createTaskDMThread = new Thread(new Runnable() {
            @Override
            public void run() {
                setAllTaskSize(difficulty);
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
        });
        createTaskDMThread.start();
    }
    //TODO SYNC
    public List<DmTask> getTasksForAgent(int numberOfTasks) {
        List<DmTask> tasksForAgent = new ArrayList<>();
        synchronized (tasks) {
            if (tasks.size() <= 0)
                return tasksForAgent;
            if (tasks.size() < numberOfTasks)
                numberOfTasks = tasks.size();
            for (int i = 0; i < numberOfTasks; i++) {
                tasksForAgent.add(tasks.poll());
            }
            return tasksForAgent;
        }
    }
    private void setAllTaskSize(Difficulty difficulty) {
        Machine machine = copyEngine.getMachine();
        int easy = (int) Math.pow(machine.getABCsize(), machine.getRotorsInUseCount());
        int medium = easy * machine.getReflectorsMapSize();
        int hard = medium * factorial(machine.getRotorsInUseCount());
        int impossible = hard * factorial(machine.getRotorsMapSize()) / (factorial(machine.getRotorsInUseCount()) * factorial(machine.getRotorsMapSize() - machine.getRotorsInUseCount())); // TODO: FIX !!!!!!!!!!1

        switch (difficulty){
            case EASY:
                allTaskSize.setValue(easy);
                break;
            case MEDIUM:
                allTaskSize.setValue(medium);
                break;
            case HARD:
                allTaskSize.setValue(hard);
                break;
            case IMPOSSIBLE:
                allTaskSize.setValue(impossible);
                break;
        }
    }
    public int factorial (int x) {
        int res = 1;

        for (int i = 1; i <= x; i++)
            res *= i;
        return res;
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

                DmTask decryptionTask = new DmTask(/*engineCopy.clone(),*/ taskSize, sentenceToCheck, engineCopy.createAgentMachineDTO(), engineCopy.createCodeDescriptionDTO());
                tasks.put(decryptionTask);
                counterOfCreatedTasks.incrementAndGet();
                engineCopy.moveRotorsToPosition(taskSize);
            }

            // Last little task
            DmTask decryptionTask = new DmTask(/*engineCopy.clone(),*/ lastTaskSize, sentenceToCheck,engineCopy.createAgentMachineDTO(),engineCopy.createCodeDescriptionDTO());
            tasks.put(decryptionTask);
            counterOfCreatedTasks.incrementAndGet();


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
