package DecryptionManager;

import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class MsgPrinter implements Runnable{
    private TextArea ta_candidates;
    private String msgSentence;

    public MsgPrinter(TextArea ta_candidates,String msgSentence) {
        this.msgSentence = msgSentence;
        this.ta_candidates = ta_candidates;
    }
    @Override
    synchronized public void run() {
        ta_candidates.appendText("\n" + msgSentence);
    }
}
