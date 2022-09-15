package DecryptionManager;

import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.concurrent.BlockingQueue;
import java.util.function.Supplier;

public class SmallClass implements Supplier<String> {

    private BlockingQueue<String> results;

    @Override
    public String get() {
        try {
            return results.take();
        }
        catch (Exception e){

        }
            return "";
    }
}
