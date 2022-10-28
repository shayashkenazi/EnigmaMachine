package users;

import DTOs.DTO_AgentDetails;
import DTOs.DTO_AllyDetails;

import java.util.*;

public class AllyManager {

    // ally name, dto
    Set<DTO_AllyDetails> setAlliesDetails = new HashSet<>();
    //every new ally add ally details once
    public synchronized void addAllyDetails(DTO_AllyDetails dto_allyDetails) {
        setAlliesDetails.add(dto_allyDetails);
    }
    public synchronized void increaseAgentNumber(String allyName){
        synchronized (setAlliesDetails) {
            System.out.println("before the if");
            for (DTO_AllyDetails dto_allyDetailsCur : setAlliesDetails) {
                if (dto_allyDetailsCur.getAllyName().equals(allyName)) {
                    System.out.println("inside the if");
                    dto_allyDetailsCur.setNumOfAgents(dto_allyDetailsCur.getNumOfAgents() + 1);
                    break;
                }
            }
        }
    }
    public synchronized List<DTO_AllyDetails> getListOfAllyDetailsWithUboatName(String uBoatName){
        List<DTO_AllyDetails> dto_allyDetailsList = new ArrayList<>();
        synchronized (setAlliesDetails) {
            for (DTO_AllyDetails dto_allyDetailsCur : setAlliesDetails) {
                if (dto_allyDetailsCur.getuBoatName().equals(uBoatName)) {
                    dto_allyDetailsList.add(dto_allyDetailsCur);
                }
            }
        }
        return dto_allyDetailsList;
    }
    public synchronized void setuBoatNameByAllyName(String allyName,String uBoatName) {
        for (DTO_AllyDetails dto_allyDetails : setAlliesDetails) {
            if (dto_allyDetails.getAllyName().equals(allyName))
                dto_allyDetails.setuBoatName(uBoatName);
        }
    }


    public void setTaskSizeByAllyName(String allyName,int taskSize) {
        for (DTO_AllyDetails dto_allyDetails : setAlliesDetails) {
            if (dto_allyDetails.getAllyName().equals(allyName))
                dto_allyDetails.setTaskSize(taskSize);
        }
    }
}
