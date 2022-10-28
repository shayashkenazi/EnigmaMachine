package main;

import DTOs.DTO_CandidateResult;
import DecryptionManager.DmTask;
import EnginePackage.EngineCapabilities;
import EnginePackage.EnigmaEngine;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import http.HttpClientUtil;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.util.Pair;
import login.LoginController;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import utils.Constants;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static utils.Constants.GSON_INSTANCE;

public class AgentMainController {

    private LoginController loginComponentController;
    private Node rootNode;
    private final StringProperty userName;

    private int numberOfTasks;
    private int numberOfThreads;
    private TimerTask readyRefresher;
    private String allyName;
    private List<String> decodedCandidates;
    private ExecutorService threadPool;
    private List<DmTask> tasks;
    private BooleanProperty isBattleReady = new SimpleBooleanProperty(false);
    private Timer timer;
    private Thread takeMissionThread;
    List<DTO_CandidateResult> listDtoCandidates = new ArrayList<>();
    private CountDownLatch agentTasksLeft;
    private EngineCapabilities engine;
    private static final Object takeMissionLock = new Object();
    @FXML private TextArea ta_contestAndTeam, ta_agentProgressAndStatus, ta_agentCandidates;
    @FXML private ScrollPane sp_mainPage;

    @FXML void initialize() {
        rootNode = sp_mainPage.getContent();
        showAllies();
        isBattleReady.addListener((observable, oldValue, newValue) -> {
            if(newValue){
                takeMissionThread = new Thread(takeMissionFromAlly());
                takeMissionThread.start();
            }
        });
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
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("Ohhhhh NOOOOOOOOOOOO !!!!!  ALLIESSSS");
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

    public Runnable takeMissionFromAlly() {
        return new Runnable() {
            @Override
            public void run() {
                String finalUrl = HttpUrl
                        .parse(Constants.TASKS)
                        .newBuilder()
                        .addQueryParameter("numberOfTasks", String.valueOf(numberOfTasks)) // TODO: constant
                        .addQueryParameter("allyName", allyName)
                        .build()
                        .toString();
                agentTasksLeft = new CountDownLatch(numberOfTasks);
                Call call = HttpClientUtil.sync(finalUrl);
                try {
                    final Response response = call.execute();
                    if (response.code() == 200) {
                        String json_dmTasks = response.body().string();
                        Type dmTasksType = new TypeToken<List<DmTask>>() {
                        }.getType();
                        List<DmTask> dmTasks = GSON_INSTANCE.fromJson(json_dmTasks, dmTasksType);
                        System.out.println("is ok ok");
                        tasks = dmTasks;
                        System.out.println("tasks size" + tasks.size());
                        if(tasks.size() < numberOfTasks)
                            agentTasksLeft = new CountDownLatch(tasks.size());
                        if(tasks.size() == 0){
                            return;
                        }
                        for (DmTask task : tasks) {
                            EngineCapabilities engine = new EnigmaEngine();
                            engine.setMachine(engine.createMachineFromDTOAgentMachine(task.getDto_agentMachine()));
                            engine.buildRotorsStack(task.getDto_codeDescription(), false);
                            task.setListDtoCandidates(listDtoCandidates);
                            task.setEngine(engine);
                            task.setAgentTasksLeft(agentTasksLeft);
                            //task.setTakeMissionLock(takeMissionLock);
                        }
                        runMissionFromQueue();

                    } else {
                        System.out.println("not 200");
                    }
                }
                catch (IOException e){
                    System.out.println("fail" + e.getMessage());
                }
                try {
                    agentTasksLeft.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                sendResultToServer();
            }


        };
    }

    private void sendResultToServer() {
        Gson gson = new Gson();
        String json_listCandidates = gson.toJson(listDtoCandidates);
        String finalUrl = HttpUrl
                .parse(Constants.CANDIDATES)
                .newBuilder()
                .build()
                .toString();
        Request request = new Request.Builder()
                .url(finalUrl)
                .post(RequestBody.create(json_listCandidates.getBytes()))
                .build();


        Call call = HttpClientUtil.HTTP_CLIENT.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("on failure candidates");
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.code() == 200){
                    listDtoCandidates.clear();
                    System.out.println("on 200 at candidates!!!");
                    takeMissionThread = new Thread(takeMissionFromAlly());
                    takeMissionThread.start();
                }
                else
                    System.out.println("on not 200 candidates");
            }
        });
    }

    public void runMissionFromQueue() {
        for (DmTask task : tasks) threadPool.submit(task);
    }

   /* private void readyBattleListener(){
        String finalUrl = HttpUrl
                .parse(Constants.CHECK_READY_BATTLE)
                .newBuilder()
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
                if(response.code() == 200){

                }
            }
        });
    }*/

    public void checkReadyRefresher() {
        readyRefresher = new TimerTask() {
            @Override
            public void run() {
                String finalUrl = HttpUrl
                        .parse(Constants.CHECK_READY_BATTLE)
                        .newBuilder()
                        .addQueryParameter(WebConstants.Constants.CLASS_TYPE,WebConstants.Constants.AGENT_CLASS)
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
                        else
                        {
                            System.out.println("response code" + response.code());
                        }
                    }
                });
            }
        };
        timer = new Timer();
        timer.schedule(readyRefresher, Constants.REFRESH_RATE, Constants.REFRESH_RATE);
    }
}
