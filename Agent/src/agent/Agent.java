package agent;

import DTOs.DTO_CodeDescription;
import DecryptionManager.DmTask;
import EnginePackage.EngineCapabilities;
import http.HttpClientUtil;
import javafx.application.Platform;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import utils.Constants;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Agent {

    private final String name;
    private final int numberOfTasks;
    private final int numberOfThreads;
    private final String allyName;
    private List<String> decodedCandidates;
    private ExecutorService threadPool;
    private Queue<DmTask> tasks;

    public Agent (String name, String allyName, int numberOfTasks, int numberOfThreads) {
        this.name = name;
        this.numberOfTasks = numberOfTasks;
        this.numberOfThreads = numberOfThreads;
        this.allyName = allyName;
        tasks = new LinkedList<>();
        threadPool = Executors.newFixedThreadPool(numberOfThreads);
    }

    public void takeMissionFromAlly(){

        String finalUrl = HttpUrl
                .parse(Constants.TASKS)
                .newBuilder()
                .addQueryParameter("numberOfTasks",String.valueOf(numberOfTasks)) // TODO: constant
                .addQueryParameter("allyName",allyName)
                .addQueryParameter("agentName",name)// TODO: constant
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
    public void runMissionFromQueue() {
        threadPool.submit(new Runnable() {
            @Override
            public void run() {
                DmTask task = tasks.poll();
                EngineCapabilities engine = task.getEngine();
                for (int i = 0; i < task.getTaskSize(); i++) {
                    EngineCapabilities e = engine.clone();
                    foundDecodeCandidate(e, task.getSentenceToCheck());
                    engine.rotateRotorByABC();
                }
            }
        });
    }

    private void foundDecodeCandidate(EngineCapabilities engineClone,String sentenceToCheck) {
        DTO_CodeDescription tmpDTO = engineClone.createCodeDescriptionDTO();
        tmpDTO.resetPlugBoard();
        String res = engineClone.encodeDecodeMsg(sentenceToCheck.toUpperCase(), false);
        try {
            if (engineClone.checkAtDictionary(res)) {
                decodedCandidates.add(res);
                //msgConsumer.accept(dto_consumerPrinter);
            }
        }
        catch (Exception ee) {
            System.out.println(ee.getMessage());
        }
    }



}
