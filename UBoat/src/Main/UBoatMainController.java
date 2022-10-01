package Main;

import EnginePackage.EnigmaEngine;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import utils.ServletUtils;

import java.io.File;

public class UBoatMainController {

    @FXML private Button btn_loadFile;

    @FXML private TextField tf_filePath;

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





        try {
            EnigmaEngine engine = ServletUtils.getEngine(getServletContext());
            engine.createEnigmaMachineFromXML(fileSelected.getAbsolutePath(), true);
            isXmlLoaded.set(false);
            isXmlLoaded.set(true);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR,"Could NOT choose a file! " + e.getMessage());
            alert.show();
            isXmlLoaded.set(false);
        }
    }

}