package DTOs;

public class DTO_AgentTasksDetails {
    private String agentName;
    private int countOfTasksTaken;
    private int countTaskLeftInThreadPool;


    public DTO_AgentTasksDetails(String agentName, int countOfTasksTaken, int countTaskLeftInThreadPool) {
        this.agentName = agentName;
        this.countOfTasksTaken = countOfTasksTaken;
        this.countTaskLeftInThreadPool = countTaskLeftInThreadPool;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public int getCountOfTasksTaken() {
        return countOfTasksTaken;
    }

    public void setCountOfTasksTaken(int countOfTasksTaken) {
        this.countOfTasksTaken = countOfTasksTaken;
    }

    public int getCountTaskLeftInThreadPool() {
        return countTaskLeftInThreadPool;
    }

    public void setCountTaskLeftInThreadPool(int countTaskLeftInThreadPool) {
        this.countTaskLeftInThreadPool = countTaskLeftInThreadPool;
    }

    public String printDetailsForAgentsTask(){
        StringBuilder sb = new StringBuilder();
        sb.append("Agent name - ").append(agentName).append("\n");
        sb.append("count Of tasks taken - ").append(countOfTasksTaken).append("\n");
        sb.append("count Of Task left - ").append(countTaskLeftInThreadPool).append("\n");
        return sb.toString();
    }
}
