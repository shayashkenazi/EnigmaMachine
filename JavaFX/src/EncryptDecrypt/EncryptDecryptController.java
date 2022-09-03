package EncryptDecrypt;

import Interfaces.SubController;
import MainApp.AppController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class EncryptDecryptController implements SubController {

    private AppController appController;

    @FXML private Button btn_clear;
    @FXML private Button btn_proccess;
    @FXML private TextField tf_input;
    @FXML private TextField tf_output;
    @FXML private TextArea ta_statistics;

    //----------------------------------------- FXML Methods -----------------------------------------
    @FXML void clearBtnClick(ActionEvent event) {
        tf_input.setText("");
        tf_output.setText("");
    }

    @FXML void proccessBtnClick(ActionEvent event) {
        appController.encryptDecryptController_proccessBtnClick();
    }
    //------------------------------------------------------------------------------------------------
    @Override
    public void setMainController(AppController mainController) {
        appController = mainController;
    }

    public TextField getTf_input() { return tf_input; }

    public TextField getTf_output() { return tf_output; }

    public TextArea getTa_statistics() { return ta_statistics; }
}
