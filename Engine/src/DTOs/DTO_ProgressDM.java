package DTOs;

import java.util.Set;

public class DTO_ProgressDM {
    private int sizeOfAllTasks;
    private int sizeOfTasksCreated;
    private int sizeOfTaskFinished;

    private Set<DTO_AgentTasksDetails> dto_AgentTasksDetailsSet;


    public DTO_ProgressDM(int sizeOfAllTasks, int sizeOfTasksCreated, int sizeOfTaskFinished) {
        this.sizeOfAllTasks = sizeOfAllTasks;
        this.sizeOfTasksCreated = sizeOfTasksCreated;
        this.sizeOfTaskFinished = sizeOfTaskFinished;
    }

    public int getSizeOfAllTasks() {
        return sizeOfAllTasks;
    }
    public Set<DTO_AgentTasksDetails> getDto_AgentTasksDetailsSet() {
        return dto_AgentTasksDetailsSet;
    }

    public void setDto_AgentTasksDetailsSet(Set<DTO_AgentTasksDetails> dto_AgentTasksDetailsSet) {
        this.dto_AgentTasksDetailsSet = dto_AgentTasksDetailsSet;
    }
    public void setSizeOfAllTasks(int sizeOfAllTasks) {
        this.sizeOfAllTasks = sizeOfAllTasks;
    }

    public int getSizeOfTasksCreated() {
        return sizeOfTasksCreated;
    }

    public void setSizeOfTasksCreated(int sizeOfTasksCreated) {
        this.sizeOfTasksCreated = sizeOfTasksCreated;
    }

    public int getSizeOfTaskFinished() {
        return sizeOfTaskFinished;
    }

    public void setSizeOfTaskFinished(int sizeOfTaskFinished) {
        this.sizeOfTaskFinished = sizeOfTaskFinished;
    }
    public String printDetails(){
        StringBuilder sb = new StringBuilder();
        sb.append("size Of All Tasks - ").append(sizeOfAllTasks).append("\n");
        sb.append("size Of Tasks Created - ").append(sizeOfTasksCreated).append("\n");
        sb.append("size Of Tasks Finished - ").append(sizeOfTaskFinished).append("\n");
        return sb.toString();
    }
}
