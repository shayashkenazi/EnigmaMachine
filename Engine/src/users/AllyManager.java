package users;

import DTOs.DTO_AgentDetails;
import DTOs.DTO_AllyDetails;

import java.util.*;

public class AllyManager {

    // ally name, dto
    Map<String, DTO_AllyDetails> mapAlliesDetails = new HashMap<>();


    public synchronized void addAllyDetails(String allyName,DTO_AllyDetails dto_allyDetails) {
        if(!mapAlliesDetails.containsKey(allyName))
            mapAlliesDetails.put(allyName,);
        mapAlliesDetails.get(allyName).add(dto_allyDetails);
    }
    public List<DTO_AgentDetails> getAgentTeamDetails(String allyName){
        if(!mapAlliesDetails.containsKey(allyName))
            mapAlliesDetails.put(allyName,new ArrayList<>());
        return mapAlliesDetails.get(allyName);
    }
}
