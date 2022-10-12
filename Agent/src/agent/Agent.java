package agent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Agent {

    private final String name;
    private final int numberOfTasks;
    private final int numberOfThreads;
    private final String allyName;

    private ExecutorService threadPool;
    private List<Runnable> tasks;

    public Agent (String name, String allyName, int numberOfTasks, int numberOfThreads) {

        this.name = name;
        this.numberOfTasks = numberOfTasks;
        this.numberOfThreads = numberOfThreads;
        this.allyName = allyName;
        tasks = new ArrayList<>();
        threadPool = Executors.newFixedThreadPool(numberOfThreads);
    }
}
