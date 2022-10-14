package main;

import DTOs.DTO_CandidateResult;
import DecryptionManager.DmTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import http.HttpClientUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.util.Pair;
import login.LoginController;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import utils.Constants;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static utils.Constants.GSON_INSTANCE;

public class AgentMainController {

    private LoginController loginComponentController;
    private Node rootNode;
    private final StringProperty userName;

    private int numberOfTasks;
    private int numberOfThreads;
    private String allyName;
    private List<String> decodedCandidates;
    private ExecutorService threadPool;
    private List<DmTask> tasks;
    List<DTO_CandidateResult> listDtoCandidates = new ArrayList<>();


    @FXML private ScrollPane sp_mainPage;

    @FXML void initialize() {
        rootNode = sp_mainPage.getContent();
        showAllies();
    }
    public AgentMainController() {
        userName = new SimpleStringProperty("Anonymous");
    }

    public void setLoginController(LoginController loginController) {
        this.loginComponentController = loginController;
        loginController.setMainController(this);
    }
    private void showAllies(){
        String finalUrl = HttpUrl
                .parse(Constants.DTO)
                .newBuilder()
                .addQueryParameter(WebConstants.Constants.DTO_TYPE, Constants.DTO_ALLIES)
                .addQueryParameter(Constants.USERNAME, userName.getValue())
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("Ohhhhh NOOOOOOOOOOOO !!!!!\n\n\n\n\n\n\nNOOOOOOO");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String json_dictionary = response.body().string();
                Type AlliesType = new TypeToken<Set<String>>() { }.getType();
                Set<String> alliesUsers = GSON_INSTANCE.fromJson(json_dictionary, AlliesType);

                for (String word : alliesUsers) {
                   loginComponentController.getCb_allies().getItems().add(word);
                }
            }
        });
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
    public void setDetailsForAgent(String name, String allyName, int numberOfTasks, int numberOfThreads){
        this.userName.set(name);
        this.allyName = allyName;
        this.numberOfTasks = numberOfTasks;
        this.numberOfThreads = numberOfThreads;
        threadPool = Executors.newFixedThreadPool(numberOfThreads);
        tasks = new LinkedList<>();
    }

    public void takeMissionFromAlly() {

        String finalUrl = HttpUrl
                .parse(Constants.TASKS)
                .newBuilder()
                .addQueryParameter("numberOfTasks", String.valueOf(numberOfTasks)) // TODO: constant
                .addQueryParameter("allyName", allyName)
                .addQueryParameter("agentName", userName.getValue())// TODO: constant
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String json_dmTasks = response.body().string();
                Type dictionaryType = new TypeToken<Pair<String, List<DmTask>>>() {}.getType();
                Pair<String, List<DmTask>> dmTasks = GSON_INSTANCE.fromJson(json_dmTasks, dictionaryType);
                tasks = dmTasks.getValue();
                for (DmTask task : tasks){
                    task.setListDtoCandidates(listDtoCandidates);
                }
            }
        });
        runMissionFromQueue();
        sendResultToServer();
        listDtoCandidates.clear();
    }

    private void sendResultToServer() {
        Gson gson = new Gson();
        String json_listCandidates = gson.toJson(listDtoCandidates);
        RequestBody bodyCandidates = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("candidatesResult",json_listCandidates)
                .build();

        String finalUrl = HttpUrl
                .parse(Constants.CANDIDATES)
                .newBuilder()
                .build()
                .toString();

        Request request = new Request.Builder()
                .url(finalUrl)
                .post(bodyCandidates)
                .build();

        Call call = HttpClientUtil.HTTP_CLIENT.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.code() == 200){

                }
            }
        });
    }

    public void runMissionFromQueue() {
        for (DmTask task : tasks) threadPool.submit(task);

    }
}
