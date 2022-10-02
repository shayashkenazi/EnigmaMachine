package codeCalibration;

import Main.UBoatMainController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class CodeCalibrationController {

    private UBoatMainController uBoatMainController;
    @FXML private Button btn_RandomCode, btn_SetCode;
    @FXML private HBox hb_setCode;

    @FXML void randomCodeBtnClick(ActionEvent event) {
        uBoatMainController.codeCalibrationController_randomCodeBtnClick();
    }

    @FXML void setCodeBtnClick(ActionEvent event) {

    }

    public void setMainController(UBoatMainController uBoatMainController) {
        this.uBoatMainController = uBoatMainController;
    }
}
