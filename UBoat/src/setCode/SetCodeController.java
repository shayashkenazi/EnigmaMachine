package setCode;

import Main.UBoatMainController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class SetCodeController {

    @FXML private Button btn_cancel, btn_set;
    @FXML private ChoiceBox<String> cb_reflector;
    @FXML private HBox hb_rotors;
    @FXML private ScrollPane sp_rotors;
    @FXML private VBox vb_mainSetCode;
    @FXML private ScrollPane sp_mainPage;

    private UBoatMainController uBoatMainController;

    @FXML
    void cancelBtnClick(ActionEvent event) {
        uBoatMainController.switchToMainPanel();
    }

    @FXML
    void setBtnClick(ActionEvent event) {

        // TODO: enter all the details to the engine
        uBoatMainController.switchToMainPanel();
    }

    public void setMainController(UBoatMainController uBoatMainController) {
        this.uBoatMainController = uBoatMainController;
    }

    public ScrollPane getMainPage() {
        return sp_mainPage;
    }
}