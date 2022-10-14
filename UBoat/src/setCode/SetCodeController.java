package setCode;

import Main.UBoatMainController;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
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
    private BooleanProperty isRotorsSelected = new SimpleBooleanProperty(false);
    private BooleanProperty isReflectorSelected = new SimpleBooleanProperty(false);

    @FXML public void initialize() {

        btn_set.disableProperty().bind(isReflectorSelected.not().or(isRotorsSelected.not()));

        cb_reflector.valueProperty().addListener(observable -> {
            isReflectorSelected.set(cb_reflector.getValue() != null);
        });

        // TODO: use old logic that Shay did to know when all the Rotors were selected
    }

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