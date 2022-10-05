package encryptMessage;

import Main.UBoatMainController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class EncryptMessageController {

    private UBoatMainController uBoatMainController;

    @FXML private Button btn_clear, btn_process, btn_ready, btn_reset;
    @FXML private ListView<?> lv_dictionary;
    @FXML private TextField tf_codeConfiguration, tf_input, tf_output, tf_searchBar;

    @FXML void clearBtnClick(ActionEvent event) {

    }

    @FXML void processBtnClick(ActionEvent event) {

    }

    @FXML void readyBtnClick(ActionEvent event) {

    }

    @FXML void resetBtnClick(ActionEvent event) {

    }

    public void setMainController(UBoatMainController uBoatMainController) {
        this.uBoatMainController = uBoatMainController;
    }
}
