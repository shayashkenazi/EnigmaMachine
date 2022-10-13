package users;

import DTOs.DTO_CandidateResult;
import DecryptionManager.DecryptionManager;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ResultsManager {

    // map agentName, results per agents
    //private final Map<String, String> ResultsMap = new HashMap<>();

    Set<DTO_CandidateResult> dto_candidateResultSet = new HashSet<>();
    public synchronized void addCandidateResults(DTO_CandidateResult dto_candidateResult) {
        dto_candidateResultSet.add(dto_candidateResult);
    }



}
