package users;

import DTOs.DTO_AgentDetails;
import DTOs.DTO_CandidateResult;
import DecryptionManager.DecryptionManager;

import java.util.*;

public class ResultsManager {

    // map agentName, results per agents
    //private final Map<String, String> ResultsMap = new HashMap<>();
    Set<DTO_CandidateResult> dto_candidateResultSet = new HashSet<>();
    public synchronized void addCandidateResults(DTO_CandidateResult dto_candidateResult) {
        dto_candidateResultSet.add(dto_candidateResult);
    }
    public synchronized void clearCandidates(){
        dto_candidateResultSet.clear();
    }

    public Set<DTO_CandidateResult> getDto_candidateResultSet() {
        return dto_candidateResultSet;
    }



}
