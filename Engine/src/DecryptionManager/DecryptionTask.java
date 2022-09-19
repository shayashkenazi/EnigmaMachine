package DecryptionManager;

import DTOs.DTO_CodeDescription;
import DTOs.DTO_ConsumerPrinter;
import EnginePackage.EngineCapabilities;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

public class DecryptionTask implements Runnable {

    private EngineCapabilities engine;
    private final int taskSize;

    private BlockingQueue<Runnable> results;
    private String sentenceToCheck;
    private Consumer<DTO_ConsumerPrinter> msgConsumer;
    private Consumer<Integer> checkFinish;
    private Object pausingLock ;
    private BooleanProperty isPause ;

    private AtomicInteger numberOfDoneTasksAtomic;
    private AtomicLong timeOfDMOperation;
    private Consumer<Integer> showNumberOfTasksConsumer;
    BooleanProperty isDMWorking;

    public DecryptionTask(EngineCapabilities engine, Consumer<Integer> checkFinish, int taskSize, BooleanProperty isDMWorking,
                          Consumer<DTO_ConsumerPrinter> msgConsumer, Consumer<Integer> showNumberOfTasks,
                          String sentenceToCheck, BlockingQueue<Runnable> results,
                          AtomicInteger numberOfTasksDone,
                          Object pausingLock , BooleanProperty isPause, AtomicLong timeOfDMOperation) {
        this.engine = engine;
        this.taskSize = taskSize;
        this.results = results;
        this.sentenceToCheck = sentenceToCheck;
        this.msgConsumer = msgConsumer;
        this.showNumberOfTasksConsumer = showNumberOfTasks;
        this.numberOfDoneTasksAtomic = numberOfTasksDone;
        this.isDMWorking = isDMWorking;
        this.checkFinish = checkFinish;
        this.isPause = isPause;
        this.pausingLock = pausingLock;
        this.timeOfDMOperation = timeOfDMOperation;
    }

    @Override
    public void run() {
        long start = System.nanoTime();
        for (int i = 0; i < taskSize; i++) {
            isPause();
            EngineCapabilities e = engine.clone();
            numberOfDoneTasksAtomic.incrementAndGet();
            progressBarUpdate(numberOfDoneTasksAtomic);
            //showNumberOfTasksConsumer.accept(numberOfDoneTasksAtomic.get());
            checkInDictionary(e);
            engine.rotateRotorByABC();
            checkFinish.accept(numberOfDoneTasksAtomic.get());
        }
        long end = System.nanoTime();
        timeOfDMOperation.addAndGet(end-start);

    }

    private boolean checkInDictionary(EngineCapabilities engineClone) {
        DTO_CodeDescription tmpDTO = engineClone.createCodeDescriptionDTO();
        tmpDTO.resetPlugBoard();
        String res = engineClone.encodeDecodeMsg(sentenceToCheck.toUpperCase(), false);
        try {
            if (engineClone.checkAtDictionary(res)) {

                DTO_ConsumerPrinter dto_consumerPrinter = new DTO_ConsumerPrinter(tmpDTO, res, Thread.currentThread().getId());
                results.put(new Runnable() {
                    @Override
                    public void run() {
                        showResult(dto_consumerPrinter);
                    }
                });
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

    private void showResult(DTO_ConsumerPrinter dto_consumerPrinter) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                msgConsumer.accept(dto_consumerPrinter);
            }
        });
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

    private void progressBarUpdate(AtomicInteger numberOfDoneTasksAtomic){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                showNumberOfTasksConsumer.accept(numberOfDoneTasksAtomic.get());
            }
        });
    }
}