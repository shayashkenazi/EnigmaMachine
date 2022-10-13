package agent;

import DTOs.DTO_CodeDescription;
import DecryptionManager.DmTask;
import EnginePackage.EngineCapabilities;
import com.google.gson.reflect.TypeToken;
import http.HttpClientUtil;
import javafx.application.Platform;
import javafx.util.Pair;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import utils.Constants;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static utils.Constants.GSON_INSTANCE;

public class Agent {

    private final String name;
    private final int numberOfTasks;
    private final int numberOfThreads;
    private final String allyName;
    private List<String> decodedCandidates;
    private ExecutorService threadPool;
    private List<DmTask> tasks;

    public Agent(String name, String allyName, int numberOfTasks, int numberOfThreads) {
        this.name = name;
        this.numberOfTasks = numberOfTasks;
        this.numberOfThreads = numberOfThreads;
        this.allyName = allyName;
        tasks = new LinkedList<>();
        threadPool = Executors.newFixedThreadPool(numberOfThreads);
    }

    public void takeMissionFromAlly() {

        String finalUrl = HttpUrl
                .parse(Constants.TASKS)
                .newBuilder()
                .addQueryParameter("numberOfTasks", String.valueOf(numberOfTasks)) // TODO: constant
                .addQueryParameter("allyName", allyName)
                .addQueryParameter("agentName", name)// TODO: constant
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
            }
        });

        runMissionFromQueue();

    }

    public void runMissionFromQueue() {
        for (DmTask task : tasks) threadPool.submit(task);
    }
}
