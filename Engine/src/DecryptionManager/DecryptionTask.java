package DecryptionManager;

import DTOs.DTO_CodeDescription;
import DTOs.DTO_ConsumerPrinter;
import EnginePackage.EngineCapabilities;
import javafx.scene.control.TextArea;
import javafx.util.Pair;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

public class DecryptionTask implements Runnable {

    private EngineCapabilities engine;
    private final int taskSize;

    private BlockingQueue<Runnable> results;
    private String sentenceToCheck;
    private Consumer<DTO_ConsumerPrinter> msgConsumer;


    public DecryptionTask(EngineCapabilities engine, int taskSize, Consumer<DTO_ConsumerPrinter> msgConsumer,
                          String sentenceToCheck, BlockingQueue<Runnable> results) {
        this.engine = engine;
        this.taskSize = taskSize;
        this.results = results;
        this.sentenceToCheck = sentenceToCheck;
        this.msgConsumer = msgConsumer;

    }

    @Override
    public void run() {
        for (int i = 0; i < taskSize; i++) {
            EngineCapabilities e = engine.clone(); // TODO: why do we need to clone ?
            if (checkInDictionary(e))
                engine.rotateRotorByABC();
        }
    }

    private void printDetailsThread() {
        System.out.println(Thread.currentThread().getId() + ": found some words at dictionary!");
    }
    private boolean checkInDictionary(EngineCapabilities engineClone) {
        DTO_CodeDescription tmpDTO = engineClone.createCodeDescriptionDTO();
        String res = engineClone.encodeDecodeMsg(sentenceToCheck.toUpperCase(), false);
        if (engineClone.checkAtDictionary(res)) {
            DTO_ConsumerPrinter dto_consumerPrinter = new DTO_ConsumerPrinter(tmpDTO,res,Thread.currentThread().getId());
            msgConsumer.accept(dto_consumerPrinter);
            return true;
        }
        return false;
    }

}