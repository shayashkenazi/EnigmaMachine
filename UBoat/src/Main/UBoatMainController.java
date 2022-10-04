package Main;

import EnginePackage.EnigmaEngine;
import codeCalibration.CodeCalibrationController;
import http.HttpClientUtil;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import utils.Constants;
import utils.ServletUtils;

import java.io.File;
import java.io.IOException;

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
        RequestBody body =
                new MultipartBody.Builder()
                        .addFormDataPart("file1", fileSelected.getName(), RequestBody.create(fileSelected, MediaType.parse("text/plain")))
                        .build();

        Request request = new Request.Builder()
                .url(Constants.LOAD_XML)
                .post(body)
                .build();

        Call call = HttpClientUtil.HTTP_CLIENT.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

            }
        });


            if(response.isSuccessful())
                tf_filePath.setText(fileSelected.getAbsolutePath());
            else {
                Alert alert = new Alert(Alert.AlertType.ERROR,response.body().string());
                alert.show();
                return;
            }
        }






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