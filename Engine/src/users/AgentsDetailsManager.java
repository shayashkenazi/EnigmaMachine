package users;

import DTOs.DTO_AgentDetails;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AgentsDetailsManager {

    //ALLYNAME,LIST<DTO CANDIDATE
    Map<String, List<DTO_AgentDetails>> mapAgentsDetails = new HashMap<>();

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
}
