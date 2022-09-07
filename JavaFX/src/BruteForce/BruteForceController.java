package BruteForce;

import Interfaces.SubController;
import MainApp.AppController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

public class BruteForceController implements SubController {

    private AppController appController;
    @FXML
    private TextArea ta_test;
    @FXML
    private Button btn_button;

    @FXML
    void testToDeleteShayGay(ActionEvent event) {
        appController.testToDeleteShayHomo();
    }

    @Override
    public void setMainController(AppController mainController) {
        appController = mainController;
    }
}

