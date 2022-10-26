package DecryptionManager;

import DTOs.DTO_AgentMachine;
import DTOs.DTO_CandidateResult;
import DTOs.DTO_CodeDescription;
import DTOs.DTO_MachineInfo;
import EnginePackage.EngineCapabilities;
import javafx.application.Platform;
import javafx.util.Pair;
//import okhttp3.HttpUrl;
import users.ResultsManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class DmTask implements Runnable {

    public void setEngine(EngineCapabilities engine) {
        this.engine = engine;
    }

    //private final int capacity = 1000;
    private EngineCapabilities engine;
    private final int taskSize;
    private String sentenceToCheck;
    private String agentExecuteName;
    private String allyName;
    private CountDownLatch agentTasksLeft;

    private DTO_AgentMachine dto_agentMachine;
    private DTO_CodeDescription dto_codeDescription;

    public void setTakeMissionLock(Object takeMissionLock) {
        this.takeMissionLock = takeMissionLock;
    }

    private Object takeMissionLock;


    List<DTO_CandidateResult> listDtoCandidates;

    public DmTask(/*EngineCapabilities engine,*/ int taskSize,String sentenceToCheck,DTO_AgentMachine dto_agentMachine,DTO_CodeDescription dto_codeDescription) {
        //this.engine = engine;
        this.taskSize = taskSize;
        this.sentenceToCheck = sentenceToCheck;
        this.dto_agentMachine = dto_agentMachine;
        this.dto_codeDescription = dto_codeDescription;
        //this.takeMissionLock = takeMissionLock;
    }
    public EngineCapabilities getEngine(){
        return engine;
    }
    public int getTaskSize() {
        return taskSize;
    }
    public String getSentenceToCheck(){
        return sentenceToCheck;
    }
    public void setAgentExecuteName(String agentName){
        agentExecuteName = agentName;
    }
    public void setListDtoCandidates(List<DTO_CandidateResult> listDtoCandidates) {
        this.listDtoCandidates = listDtoCandidates;
    }
    public void setAgentTasksLeft(CountDownLatch agentTasksLeft){
        this.agentTasksLeft = agentTasksLeft;
    }
    public DTO_AgentMachine getDto_agentMachine() {
        return dto_agentMachine;
    }

    public DTO_CodeDescription getDto_codeDescription() {
        return dto_codeDescription;
    }
    @Override
    public void run() {
        for (int i = 0; i < taskSize; i++) {
            EngineCapabilities e = engine.clone();
            foundDecodeCandidate(e,sentenceToCheck);
            engine.rotateRotorByABC();
        }
        agentTasksLeft.countDown();

    }

    private void foundDecodeCandidate(EngineCapabilities engineClone,String sentenceToCheck ) {
        DTO_CodeDescription tmpDTO = engineClone.createCodeDescriptionDTO();
        tmpDTO.resetPlugBoard();
        String res = engineClone.encodeDecodeMsg(sentenceToCheck.toUpperCase(), false);
        try {
            if (engineClone.checkAtDictionary(res)) {
                DTO_CandidateResult dto_candidateResult = new DTO_CandidateResult(agentExecuteName,allyName,sentenceToCheck,createDescriptionFormat(tmpDTO));
                listDtoCandidates.add(dto_candidateResult); // TODO SYNC?
            }
        }
        catch (Exception ee) {
            System.out.println(ee.getMessage());
        }
    }

    public String createDescriptionFormat(DTO_CodeDescription dto_codeDescription) {
        StringBuilder sb = new StringBuilder();
        sb.append("<");
        int index = dto_codeDescription.getRotorsInUseIDList().size() - 1;

        for(int i = dto_codeDescription.getRotorsInUseIDList().size() - 1; i >= 0; i--)
        {
            Pair<String , Pair<Integer,Integer>> rotorId = dto_codeDescription.getRotorsInUseIDList().get(i);
            sb.append(rotorId.getKey()).append(","); // need to have curr index eac h rotor
        }
        sb.replace(sb.length() - 1,sb.length(),">"); // for the last ','
        sb.append("<");
        Collections.reverse(dto_codeDescription.getStartingPositionList());
        for(Character ch : dto_codeDescription.getStartingPositionList()){
            Pair<String ,Pair<Integer,Integer>> rotorId = dto_codeDescription.getRotorsInUseIDList().get(index);
            int distance = Math.floorMod(dto_codeDescription.getNotch(rotorId) - dto_codeDescription.getCurrent(rotorId), dto_codeDescription.getABC().length()) ; //'% dto_codeDescription.'
            sb.append(ch).append("(").append(distance).append("),"); // need to have curr index eac h rotor
            index--;
        }
        sb.replace(sb.length() - 1,sb.length(),">"); // for the last ','
        sb.append("<").append(dto_codeDescription.getReflectorID()).append(">");
        Collections.reverse(dto_codeDescription.getStartingPositionList());

        if (dto_codeDescription.getPlugsInUseList().size() != 0) {
            sb.append("<");
            for (int i = 0; i < dto_codeDescription.getPlugsInUseList().size(); i++) {
                Pair<Character, Character> pair = dto_codeDescription.getPlugsInUseList().get(i);
                sb.append(pair.getKey()).append("|").append(pair.getValue()).append(",");
            }
            sb.replace(sb.length() - 1,sb.length(),">");// replace the last ',' with '>'
        }

        return sb.toString();
    }

    public void setAllyName(String allyName) {
        this.allyName = allyName;
    }
}
