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
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import utils.Constants;

import java.io.IOException;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class AlliesMainController {

    private LoginController loginComponentController;
    private Node rootNode;
    private final StringProperty userName;
    @FXML private Button btn_ready;
    @FXML private ComboBox<String> cb_battlefieldNames;
    @FXML private ScrollPane sp_mainPage;
    @FXML private TextField tf_taskSize;
    @FXML private TextArea ta_teamsAgentsData, ta_contestsData, ta_contestData, ta_contestTeams, ta_teamAgents, ta_teamCandidates;
    BooleanProperty  isBattlefieldSelected, isTaskSizeSelected,isReady,isBattleReady;
    Set<Pair<String,String>> uboatBattlefieldSet;
    private TimerTask readyRefresher;
    private Timer timer;
    private Thread createTaskDMThread;

    @FXML void initialize() {
        isBattlefieldSelected = new SimpleBooleanProperty(false);
        isTaskSizeSelected = new SimpleBooleanProperty(false);
        isBattleReady = new SimpleBooleanProperty(false);
        rootNode = sp_mainPage.getContent();
        cb_battlefieldNames.valueProperty().addListener(observable -> {
            isBattlefieldSelected.set(cb_battlefieldNames.getValue() != null);
        });

        btn_ready.disableProperty().bind(isBattlefieldSelected.not().or(isTaskSizeSelected.not()));

        // Always keep Task Size Text-Field valid
        tf_taskSize.textProperty().addListener((observable, oldValue, newValue) -> {

            if (newValue.equals("")) {
                isTaskSizeSelected.set(false);
                return;
            }

            if (!newValue.matches("^[0-9]*[1-9][0-9]*$")) // Invalid number
                tf_taskSize.setText(oldValue);
            else
                isTaskSizeSelected.set(true);
        });
        isBattleReady.addListener((observable, oldValue, newValue) -> {
            //TODO new Thread RUN THIS SHIT
            if(newValue)
                createTasksDM();
        });
        isReady.addListener((observable, oldValue, newValue) -> {
            if(newValue)
                checkReadyRefresher();
        });
    }
    public AlliesMainController() {
        userName = new SimpleStringProperty("Anonymous");
        isReady = new SimpleBooleanProperty(false);
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
        updateHierarchy();
        //createDM();
        //updateReadyManager();
        //createTasksDM();// TODO ITS NOT HEREEE ONLY WHEN ALL IS READY
        //checkReadyRefresher();
    }
    private void updateReadyManager(){
        String uBoatName = getUboatNameByBattlefieldName(cb_battlefieldNames.getValue());
        String finalUrl = HttpUrl
                .parse(Constants.READY)
                .newBuilder()
                .addQueryParameter(Constants.BATTLEFIELD_NAME,cb_battlefieldNames.getValue())
                .addQueryParameter(Constants.UBOAT_NAME, uBoatName)
                .addQueryParameter(Constants.CLASS_TYPE,Constants.ALLIES_CLASS)
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
               /* String text = response.body().string();  // this is decode msg
                Platform.runLater(() -> {

                });*/
                System.out.println("ready rep");
                isReady.set(true);
            }
        });
    }
    private void createDM() {
        String uBoatName = getUboatNameByBattlefieldName(cb_battlefieldNames.getValue());
        String finalUrl = HttpUrl
                .parse(Constants.ALLY_DM)
                .newBuilder()
                .addQueryParameter(Constants.TASK_SIZE, tf_taskSize.getText())
                .addQueryParameter(Constants.UBOAT_NAME, uBoatName)
                .addQueryParameter(Constants.BATTLEFIELD_NAME,cb_battlefieldNames.getValue())
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println(e.getMessage() + "dm  hooooo");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String text = response.body().string();  // this is decode msg
                Platform.runLater(() -> {
                    System.out.println(response.code());
                });
                System.out.println("hey im in dm task response");
                updateReadyManager();
            }
        });

    }
    private String getUboatNameByBattlefieldName(String battlefieldName){
        for(Pair<String, String> pair :uboatBattlefieldSet){
            if(pair.getValue().equals(battlefieldName))
                return pair.getKey();
        }
        return null;
    }
    private void updateHierarchy(){
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
                .addQueryParameter(Constants.TASK_SIZE,tf_taskSize.getText())
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        System.out.println("failure "  + e.getMessage())
                );
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
                                System.out.println("hey its hierarchy response  :")
                        );
                        createDM();
                        //isReady.set(true);
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

    public void checkReadyRefresher() {
        readyRefresher = new TimerTask() {
            @Override
            public void run() {
                String finalUrl = HttpUrl
                        .parse(Constants.CHECK_READY_BATTLE)
                        .newBuilder()
                        .addQueryParameter(Constants.CLASS_TYPE,Constants.ALLIES_CLASS)
                        .build()
                        .toString();
                HttpClientUtil.runAsync(finalUrl, new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {

                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        if (response.code() == 200) {
                            isBattleReady.set(true);
                        }
                        //System.out.println("hey im in ready servlet res");
                        //isReady.set(true);
                    }
                });
            }
        };
        timer = new Timer();
        timer.schedule(readyRefresher, Constants.REFRESH_RATE, Constants.REFRESH_RATE);
    }

    private void createTasksDM() {
        System.out.println("thread id - " + Thread.currentThread().getId());
        //Runnable workerThread = () -> {
        String uBoatName = getUboatNameByBattlefieldName(cb_battlefieldNames.getValue());
        String finalUrl = HttpUrl
                .parse(Constants.CREATE_TASKS)
                .newBuilder()
                .addQueryParameter(Constants.UBOAT_NAME, uBoatName)
                .build()
                .toString();
        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println(e.getMessage() + " failure" + Thread.currentThread().getId());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() == 200) {
                    System.out.println("hey im in create tasks servlet res" + "thread" + Thread.currentThread().getId());
                }
                //System.out.println("hey im in ready servlet res");
                //isReady.set(true);
            }
        });

       /* Request request = new Request.Builder()
                .url(finalUrl)
                .build();*/

        /*Call call = HttpClientUtil.HTTP_CLIENT.newCall(request);
        try {
            call.execute();
            System.out.println("thread id is" + Thread.currentThread().getId());
        } catch (IOException e) {
            System.out.println(e.getMessage() + "thread" + Thread.currentThread().getId());
        }*/
            /*HttpClientUtil.runAsync(finalUrl, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    System.out.println(e.getMessage() + "create task dm hooooo"+ Thread.currentThread().getId());
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    //String text = response.body().string();  // this is decode msg
                    System.out.println("hey im in dm task response , thread id" + Thread.currentThread().getId());
                }
            });*/
        /*createTaskDMThread = new Thread(workerThread);
        createTaskDMThread.start();*/

        System.out.println("thread idddddd" + Thread.currentThread().getId());
    }
}
