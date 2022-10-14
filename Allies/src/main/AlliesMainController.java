package main;

import http.HttpClientUtil;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.util.Pair;
import login.LoginController;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import users.HierarchyManager;
import utils.Constants;

import java.io.IOException;
import java.util.Set;

public class AlliesMainController {

    private LoginController loginComponentController;
    private Node rootNode;
    private final StringProperty userName;
    @FXML private Button btn_ready;
    @FXML private ComboBox<String> cb_battlefieldNames;
    @FXML private ScrollPane sp_mainPage;
    @FXML private TextArea ta_teamsAgentsData, ta_contestsData, ta_contestData, ta_contestTeams, ta_teamAgents, ta_teamCandidates;
    BooleanProperty  isBattlefieldSelected;
    Set<Pair<String,String>> uboatBattlefieldSet;

    @FXML void initialize() {
        isBattlefieldSelected = new SimpleBooleanProperty(false);
        rootNode = sp_mainPage.getContent();
        cb_battlefieldNames.valueProperty().addListener(observable -> {
            isBattlefieldSelected.set(cb_battlefieldNames.getValue() != null);
        });

        btn_ready.disableProperty().bind(isBattlefieldSelected.not());
    }
    public AlliesMainController() {
        userName = new SimpleStringProperty("Anonymous");
    }

    public void setLoginController(LoginController loginController) {
        this.loginComponentController = loginController;
        loginController.setMainController(this);
    }

    public void setContentScene() {
        sp_mainPage.setContent(loginComponentController.getLoginPage());
    }

    public void setUserName(String userName) {
        this.userName.set(userName);
    }

    public void switchToMainPanel() {
        sp_mainPage.setContent(rootNode);
    }

    @FXML
    void readyBtnClick(ActionEvent event) {

        String BattlefieldSelected = cb_battlefieldNames.getValue();
        String uBoatNameSelected = "unknown Name";
        for(Pair<String, String> pair :uboatBattlefieldSet){
            if(pair.getValue().equals(BattlefieldSelected))
                uBoatNameSelected = pair.getKey();
        }
        //noinspection ConstantConditions
        String finalUrl = HttpUrl
                .parse(Constants.HIERARCHY)
                .newBuilder()
                .addQueryParameter(Constants.USERNAME, userName.getValue())
                .addQueryParameter(Constants.UBOAT_NAME, uBoatNameSelected)
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                /*Platform.runLater(() ->
                        errorMessageProperty.set("Something went wrong: " + e.getMessage())
                );*/
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            System.out.println("Something went wrong: " + responseBody)
                    );
                }
                else {
                    if(response.code() == 200){
                        Platform.runLater(() ->
                                System.out.println("Something went GOOD:")
                        );
                    }
                }
            }
        });
    }
/*    public void setUboatBattlefieldSet(Set<Pair<String,String>> uboatBattlefieldSet){
        this.uboatBattlefieldSet = uboatBattlefieldSet;
    }*/
    public void initializeCB(Set<Pair<String,String>> uboatBattlefieldSet){
        this.uboatBattlefieldSet = uboatBattlefieldSet;
        for(Pair<String, String> pair :uboatBattlefieldSet){
            cb_battlefieldNames.getItems().add(pair.getValue());
        }

    }
}
