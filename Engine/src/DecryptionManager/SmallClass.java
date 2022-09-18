package DecryptionManager;

import DTOs.DTO_ConsumerPrinter;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.concurrent.BlockingQueue;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class SmallClass implements Runnable {

    //private BlockingQueue<String> results;
    private Consumer<DTO_ConsumerPrinter> msgConsumer;
    private DTO_ConsumerPrinter dto_consumerPrinter;

    public SmallClass(DTO_ConsumerPrinter dto_consumerPrinter, Consumer<DTO_ConsumerPrinter> msgConsumer)
    {
        this.msgConsumer = msgConsumer;
        this.dto_consumerPrinter = dto_consumerPrinter;
    }

    @Override
    public void run() {
        msgConsumer.accept(dto_consumerPrinter);
    }
}
