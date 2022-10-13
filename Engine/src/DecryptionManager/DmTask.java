package DecryptionManager;

import DTOs.DTO_CandidateResult;
import DTOs.DTO_CodeDescription;
import EnginePackage.EngineCapabilities;
import javafx.application.Platform;
import javafx.util.Pair;
import users.ResultsManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DmTask implements Runnable {

    //private final int capacity = 1000;
    private EngineCapabilities engine;
    private final int taskSize;
    private String sentenceToCheck;
    private String agentExecuteName;
    private String allyName;

    public DmTask(EngineCapabilities engine,int taskSize,String sentenceToCheck) {
        this.engine = engine;
        this.taskSize = taskSize;
        this.sentenceToCheck = sentenceToCheck;
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

    @Override
    public void run() {
        List<DTO_CandidateResult> listDtoCandidates = new ArrayList<>();
        for (int i = 0; i < taskSize; i++) {
            EngineCapabilities e = engine.clone();
            foundDecodeCandidate(e,sentenceToCheck,listDtoCandidates);
            engine.rotateRotorByABC();
        }

        sendResultsCandidates(listDtoCandidates);
    }

    private void sendResultsCandidates(List<DTO_CandidateResult> listDtoCandidates) {
       /* String finalUrl = HttpUrl
                .parse(Constants.DTO)
                .newBuilder()
                .addQueryParameter("dtoType", "machineConfiguration") // TODO: constant
                .addQueryParameter(Constants.USERNAME, userName.getValue())
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String text = newValue ? response.body().string() : "";
                Platform.runLater(() -> {
                    encryptMessageComponentController.getTf_codeConfiguration().setText(text);
                });
            }
        });*/
    }

    private void foundDecodeCandidate(EngineCapabilities engineClone,String sentenceToCheck,List<DTO_CandidateResult> listDtoCandidates ) {
        DTO_CodeDescription tmpDTO = engineClone.createCodeDescriptionDTO();
        tmpDTO.resetPlugBoard();
        String res = engineClone.encodeDecodeMsg(sentenceToCheck.toUpperCase(), false);
        try {
            if (engineClone.checkAtDictionary(res)) {
                DTO_CandidateResult dto_candidateResult = new DTO_CandidateResult(agentExecuteName,allyName,sentenceToCheck,createDescriptionFormat(tmpDTO));
                listDtoCandidates.add(dto_candidateResult);
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
