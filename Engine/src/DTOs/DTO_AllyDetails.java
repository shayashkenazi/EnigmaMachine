package DTOs;

public class DTO_AllyDetails {

    private String uBoatName;
    private String allyName;
    private int numOfAgents = 0;
    private int taskSize;


    public DTO_AllyDetails(String uBoatName,String allyName, int numOfAgents, int taskSize) {
        this.allyName = allyName;
        this.numOfAgents = numOfAgents;
        this.taskSize = taskSize;
        this.uBoatName = uBoatName;

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

    public void setNumOfAgents(int numOfAgents) {
        this.numOfAgents = numOfAgents;
    }
    public String getuBoatName() {
        return uBoatName;
    }

    public void setuBoatName(String uBoatName) {
        this.uBoatName = uBoatName;
    }
    public void setAllyName(String allyName) {
        this.allyName = allyName;
    }

    public void setTaskSize(int taskSize) {
        this.taskSize = taskSize;
    }
    public String getDetailsFormat(){
        StringBuilder sb = new StringBuilder();
        sb.append("ally name - ").append(allyName).append("\n");
        sb.append("num of agents - ").append(numOfAgents).append("\n");
        sb.append("task size - ").append(taskSize).append("\n");
        return sb.toString();
    }

}
