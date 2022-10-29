package users;

import DTOs.DTO_AgentDetails;
import DTOs.DTO_AgentTasksDetails;

import java.util.*;

public class AgentsDetailsManager {

    //ALLYNAME,LIST<DTO CANDIDATE
    Map<String, List<DTO_AgentDetails>> mapAgentsDetails = new HashMap<>();
    Set<DTO_AgentTasksDetails> setAgentTasksDetails = new HashSet<>();
    public Set<DTO_AgentTasksDetails> getSetAgentTasksDetails() {
        return setAgentTasksDetails;
    }
    public synchronized void addAgentDetails(String allyName,DTO_AgentDetails dto_agentDetails) {
        if(!mapAgentsDetails.containsKey(allyName))
            mapAgentsDetails.put(allyName,new ArrayList<>());
        mapAgentsDetails.get(allyName).add(dto_agentDetails);
    }
    public List<DTO_AgentDetails> getAgentTeamDetails(String allyName){
        if(!mapAgentsDetails.containsKey(allyName))
            mapAgentsDetails.put(allyName,new ArrayList<>());
        return mapAgentsDetails.get(allyName);
    }
    public synchronized void addAgentTasksDetails(DTO_AgentTasksDetails dto_agentTasksDetails) {
        setAgentTasksDetails.add(dto_agentTasksDetails);
    }
    public DTO_AgentTasksDetails getAgentTasksDetails(String agentName){
        for (DTO_AgentTasksDetails dto_agentTasksDetails : setAgentTasksDetails){
            if(dto_agentTasksDetails.getAgentName().equals(agentName)){
                return dto_agentTasksDetails;
            }
        }
        return null;
    }


}
