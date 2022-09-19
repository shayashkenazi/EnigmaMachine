package DecryptionManager;

import DTOs.DTO_CodeDescription;
import DTOs.DTO_ConsumerPrinter;
import EnginePackage.EngineCapabilities;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
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
    private Consumer<Integer> checkFinish;
    private IntegerProperty numberOfDoneTasks;
    private Object pausingLock ;
    private BooleanProperty isPause ;

    private AtomicInteger numberOfDoneTasksAtomic;
    private Consumer<Integer> showNumberOfTasksConsumer;
    BooleanProperty isDMWorking;

    public DecryptionTask(EngineCapabilities engine,Consumer<Integer> checkFinish, int taskSize,BooleanProperty isDMWorking,
                          Consumer<DTO_ConsumerPrinter> msgConsumer,Consumer<Integer> showNumberOfTasks,
                          String sentenceToCheck, BlockingQueue<Runnable> results,
                          IntegerProperty numberOfDoneTasks,AtomicInteger numberOfTasks,
                          Object pausingLock ,BooleanProperty isPause) {
        this.engine = engine;
        this.taskSize = taskSize;
        this.results = results;
        this.sentenceToCheck = sentenceToCheck;
        this.msgConsumer = msgConsumer;
        this.numberOfDoneTasks = numberOfDoneTasks;
        this.showNumberOfTasksConsumer = showNumberOfTasks;
        this.numberOfDoneTasksAtomic = numberOfTasks;
        this.isDMWorking = isDMWorking;
        this.checkFinish = checkFinish;
        this.isPause = isPause;
        this.pausingLock = pausingLock;
    }

    @Override
    public void run() {
        //showNumberOfTasksConsumer.accept(numberOfDoneTasks.getValue());
        for (int i = 0; i < taskSize; i++) {
            isPause();
            EngineCapabilities e = engine.clone(); // TODO: why do we need to clone ?
            checkInDictionary(e);
            engine.rotateRotorByABC();
            numberOfDoneTasksAtomic.incrementAndGet();
            synchronized (numberOfDoneTasks){
                numberOfDoneTasks.set(numberOfDoneTasksAtomic.get());
            }
            checkFinish.accept(numberOfDoneTasksAtomic.get());
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
        try {
            if (engineClone.checkAtDictionary(res)) {

                DTO_ConsumerPrinter dto_consumerPrinter = new DTO_ConsumerPrinter(tmpDTO, res, Thread.currentThread().getId());
                SmallClass smallClass = new SmallClass(dto_consumerPrinter, msgConsumer);
                results.add(smallClass);
                //msgConsumer.accept(dto_consumerPrinter);
                return true;
            }
            return false;
        }
        catch (Exception ee) {
            System.out.println(ee.getMessage());
        }
        return false;
    }

    public void isPause(){

        synchronized (pausingLock) {
            if(isPause.getValue()) {
                while (isPause.getValue()) {
                    try {
                        //startTimeInPause = Instant.now();
                        pausingLock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //sumTimeInPause += Duration.between(startTimeInPause, Instant.now()).toMillis();
            }
        }
    }
}