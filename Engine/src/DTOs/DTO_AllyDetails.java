package DTOs;

public class DTO_AllyDetails {

    private String allyName;
    private int numOfAgents;
    private int taskSize;


    public DTO_AllyDetails(String allyName, int numOfAgents, int taskSize) {
        this.allyName = allyName;
        this.numOfAgents = numOfAgents;
        this.taskSize = taskSize;
    }

    public String getAllyName() {
        return allyName;
    }

    public int getNumOfAgents() {
        return numOfAgents;
    }

    public int getTaskSize() {
        return taskSize;
    }
}
