package DecryptionManager;

import DTOs.DTO_CodeDescription;
import DTOs.DTO_ConsumerPrinter;
import EnginePackage.EngineCapabilities;

import java.util.List;
import java.util.concurrent.BlockingQueue;

public class DmTask {

    //private final int capacity = 1000;
    private EngineCapabilities engine;
    private final int taskSize;
    private String sentenceToCheck;

    public DmTask(EngineCapabilities engine,int taskSize,String sentenceToCheck) {
        this.engine = engine;
        this.taskSize = taskSize;
        this.sentenceToCheck = sentenceToCheck;
    }
    public EngineCapabilities getEngine(){
        return engine;
    }
    public int getTaskSize() {
        return taskSize;
    }
    public String getSentenceToCheck(){
        return sentenceToCheck;
    }
}
