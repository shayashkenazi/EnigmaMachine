package Main;

import EnginePackage.EnigmaEngine;
import codeCalibration.CodeCalibrationController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import utils.ServletUtils;

import java.io.File;

public class UBoatMainController {

    @FXML private CodeCalibrationController codeCalibrationComponentController;
    @FXML private VBox codeCalibrationComponent;

    @FXML private Button btn_loadFile;

    @FXML private TextField tf_filePath;

    @FXML public void initialize() {
        if (codeCalibrationComponentController != null) {
            codeCalibrationComponentController.setMainController(this);
        }
    }

    @FXML void loadFileBtnClick(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml files", "*.xml"));

        File fileSelected = fileChooser.showOpenDialog(/*primaryStage*/null); // TODO: add stage

        if (fileSelected == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR,"Could NOT choose a file!");
            alert.show();
            return;
        }

        // create request for loading xml file





/*        try {
            EnigmaEngine engine = ServletUtils.getEngine(getServletContext());
            engine.createEnigmaMachineFromXML(fileSelected.getAbsolutePath(), true);
            isXmlLoaded.set(false);
            isXmlLoaded.set(true);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR,"Could NOT choose a file! " + e.getMessage());
            alert.show();
            isXmlLoaded.set(false);
        }*/
    }

    public void codeCalibrationController_randomCodeBtnClick() {

    }
}