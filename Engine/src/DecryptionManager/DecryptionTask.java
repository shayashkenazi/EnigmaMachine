package DecryptionManager;

import DTOs.DTO_CodeDescription;
import EnginePackage.EngineCapabilities;
import javafx.scene.control.TextArea;
import javafx.util.Pair;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class DecryptionTask implements Runnable {

    private EngineCapabilities engine;
    private final int taskSize;

    private BlockingQueue<Runnable> results;
    private String sentenceToCheck;
    private TextArea ta_candidates;


    public DecryptionTask(EngineCapabilities engine, int taskSize,
                          String sentenceToCheck, BlockingQueue<Runnable> results, TextArea ta_candidates) {
        this.engine = engine;
        this.taskSize = taskSize;
        this.results = results;
        this.sentenceToCheck = sentenceToCheck;
        this.ta_candidates = ta_candidates;
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
        String res = engineClone.encodeDecodeMsg(sentenceToCheck.toUpperCase(), false);
        if (engineClone.checkAtDictionary(res)) {
            engineClone.createCodeDescriptionDTO();
            results.offer(new MsgPrinter(ta_candidates, res));
            return true;
        }
        return false;
    }

}