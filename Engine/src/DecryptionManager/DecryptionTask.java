package DecryptionManager;

import DTOs.DTO_CodeDescription;
import DTOs.DTO_ConsumerPrinter;
import EnginePackage.EngineCapabilities;
import javafx.beans.property.IntegerProperty;
import javafx.scene.control.TextArea;
import javafx.util.Pair;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class DecryptionTask implements Runnable {

    private EngineCapabilities engine;
    private final int taskSize;

    private BlockingQueue<Runnable> results;
    private String sentenceToCheck;
    private Consumer<DTO_ConsumerPrinter> msgConsumer;
    private IntegerProperty numberOfDoneTasks;

    private AtomicInteger numberOfDoneTasksAtomic;
    private Consumer<Integer> showNumberOfTasksConsumer;

    public DecryptionTask(EngineCapabilities engine, int taskSize,
                          Consumer<DTO_ConsumerPrinter> msgConsumer,Consumer<Integer> showNumberOfTasks,
                          String sentenceToCheck, BlockingQueue<Runnable> results, IntegerProperty numberOfDoneTasks,AtomicInteger numberOfTasks) {
        this.engine = engine;
        this.taskSize = taskSize;
        this.results = results;
        this.sentenceToCheck = sentenceToCheck;
        this.msgConsumer = msgConsumer;
        this.numberOfDoneTasks = numberOfDoneTasks;
        this.showNumberOfTasksConsumer = showNumberOfTasks;
        this.numberOfDoneTasksAtomic = numberOfTasks;
    }

    @Override
    public void run() {
        //showNumberOfTasksConsumer.accept(numberOfDoneTasks.getValue());
        for (int i = 0; i < taskSize; i++) {
            EngineCapabilities e = engine.clone(); // TODO: why do we need to clone ?
            checkInDictionary(e);
            engine.rotateRotorByABC();
            numberOfDoneTasksAtomic.incrementAndGet();
            numberOfDoneTasks.set(numberOfDoneTasksAtomic.get());
            /*synchronized (numberOfDoneTasks){
                numberOfDoneTasks.setValue(numberOfDoneTasks.getValue() + 1);
            }*/
        }


    }

    private boolean checkInDictionary(EngineCapabilities engineClone) {
        showNumberOfTasksConsumer.accept(numberOfDoneTasksAtomic.get());
        DTO_CodeDescription tmpDTO = engineClone.createCodeDescriptionDTO();
        tmpDTO.resetPlugBoard();
        String res = engineClone.encodeDecodeMsg(sentenceToCheck.toUpperCase(), false);
        if (engineClone.checkAtDictionary(res)) {
            DTO_ConsumerPrinter dto_consumerPrinter = new DTO_ConsumerPrinter(tmpDTO,res,Thread.currentThread().getId());
            msgConsumer.accept(dto_consumerPrinter);
            return true;
        }
        return false;
    }

}