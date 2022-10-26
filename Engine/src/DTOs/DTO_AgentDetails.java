package DTOs;

public class DTO_AgentDetails {
    private String agentName;
    private int countOfThreads;
    private int sizeOfTask;


    public DTO_AgentDetails(String agentName, int countOfThreads, int sizeOfTask) {
        this.agentName = agentName;
        this.countOfThreads = countOfThreads;
        this.sizeOfTask = sizeOfTask;
    }

    public String printDetailsAgent(){
        StringBuilder sb = new StringBuilder();
        sb.append("Agent name - ").append(agentName).append("\n");
        sb.append("count Of Threads - ").append(countOfThreads).append("\n");
        sb.append("size Of Task - ").append(sizeOfTask).append("\n");
        return sb.toString();
    }

}