package DecryptionManager;

import DTOs.DTO_CodeDescription;
import EnginePackage.EngineCapabilities;
import javafx.util.Pair;

import java.util.Collections;
import java.util.List;

public class DecryptionTask implements Runnable {

    private EngineCapabilities engine;
    private final int taskSize;

    public DecryptionTask (EngineCapabilities engine, int taskSize) {
        this.engine = engine;
        this.taskSize = taskSize;
    }

    @Override
    public void run() {

        printDescriptionFormat(engine.createCodeDescriptionDTO());

        for (int i = 0; i < taskSize; i++) {

            EngineCapabilities e = engine.clone();
            engine.rotateRotorByABC();
            e.encodeDecodeMsg("Shay ben zinaaaaaaaaaaaaaa");
        }
    }












    private void printDescriptionFormat(DTO_CodeDescription dto_codeDescription) {
        StringBuilder sb = new StringBuilder();
        sb.append("<");
        int index = 0;

        for(int i = dto_codeDescription.getRotorsInUseIDList().size() - 1; i >= 0; i--)
        {
            Pair<String , Pair<Integer,Integer>> rotorId = dto_codeDescription.getRotorsInUseIDList().get(i);
            int distance = Math.floorMod(dto_codeDescription.getNotch(rotorId) - dto_codeDescription.getCurrent(rotorId), dto_codeDescription.getABC().length()) ; //'% dto_codeDescription.'
            sb.append(rotorId.getKey()).append("(").append(distance).append("),"); // need to have curr index eac h rotor
        }
        sb.replace(sb.length() - 1,sb.length(),">"); // for the last ','
        Collections.reverse(dto_codeDescription.getStartingPositionList());
        sb.append("<").append(String.join(",", dto_codeDescription.getStartingPositionList().toString()
                .replace(",", "")
                .replace("[", "")
                .replace("]", ""))).append(">");
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

        System.out.println(sb.toString());
    }
}
