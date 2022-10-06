package Main;

import EnginePackage.EnigmaEngine;
import codeCalibration.CodeCalibrationController;
import encryptMessage.EncryptMessageController;
import http.HttpClientUtil;
import jakarta.servlet.http.HttpServletResponse;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
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

    private final BooleanProperty isXmlLoaded = new SimpleBooleanProperty(false);
    private final BooleanProperty isCodeChosen = new SimpleBooleanProperty(false);

    @FXML private CodeCalibrationController codeCalibrationComponentController;
    @FXML private VBox codeCalibrationComponent;
    @FXML private EncryptMessageController encryptMessageComponentController;
    @FXML private VBox encryptMessageComponent;
    @FXML private Button btn_loadFile, btn_logOut;
    @FXML private TextField tf_filePath;
    @FXML private TextArea ta_machineDetails, ta_candidates, ta_teamsDetails;

    @FXML public void initialize() {

        if (codeCalibrationComponentController != null &&
            encryptMessageComponentController  != null) {

            codeCalibrationComponentController.setMainController(this);
            encryptMessageComponentController.setMainController(this);
        }

        isXmlLoaded.addListener((observable, oldValue, newValue) -> {

            codeCalibrationComponentController.enableDisableCodeCalibrationButtons(newValue);
            setMachineDetailsTextArea(newValue);
        });
    }

    @FXML void loadFileBtnClick(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml files", "*.xml"));

        File fileSelected = fileChooser.showOpenDialog(/*primaryStage*/null); // TODO: add stage

        if (fileSelected == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could NOT choose a file!");
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
                System.out.println("on fail" + e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() == HttpServletResponse.SC_OK) {
                    isXmlLoaded.set(true);
                    Platform.runLater(() ->
                            tf_filePath.setText(fileSelected.getAbsolutePath())
                    );
                } else { // TODO: fix - don't work? - WORK
                    Platform.runLater(() -> {
                        String errorMsg = null;
                        try {
                            errorMsg = response.body().string();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        Alert errorAlert = new Alert(Alert.AlertType.ERROR, errorMsg);
                        errorAlert.show();
                    });
                }
            }
        });
    }

    @FXML void logOutBtnClick(ActionEvent event) {

    }

    public void codeCalibrationController_randomCodeBtnClick() {
        //TODO : CREATE RANDOM MACHINE
        isCodeChosen.set(true);
    }
    public void codeCalibrationController_SETCodeBtnClick() {
        //TODO : CREATE MACHINE
        isCodeChosen.set(true);
    }
    public void EncryptMessageController_processBtnClick(String msgToDecode){

        String finalUrl = HttpUrl
                .parse(Constants.DTO)
                .newBuilder()
                .addQueryParameter("decodeMsg",msgToDecode ) // TODO: constant
                .build()
                .toString();
        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String text = response.body().string();  // this is decode msg
                Platform.runLater(() -> {
                    encryptMessageComponentController.getTf_output().setText(text);
                });
            }
        });

    }
    private void setMachineDetailsTextArea(Boolean newValue) {

        // request for dto
        String finalUrl = HttpUrl
                .parse(Constants.DTO)
                .newBuilder()
                .addQueryParameter("dtoType", "machineConfiguration") // TODO: constant
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String text = newValue ? response.body().string() : "";
                Platform.runLater(() -> {
                    ta_machineDetails.setText(text);

                });
            }
        });
    }
    private void setMachineConfigurationTextArea(Boolean newValue) {

        // request for dto
        String finalUrl = HttpUrl
                .parse(Constants.DTO)
                .newBuilder()
                .addQueryParameter("dtoType", "machineInfo") // TODO: constant
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String text = newValue ? response.body().string() : "";
                Platform.runLater(() -> {
                    encryptMessageComponentController.getTf_codeConfiguration().setText(text);
                });
            }
        });
    }
}