package EnginePackage;

import DTOs.DTO_CodeDescription;
import DTOs.DTO_MachineInfo;
import Tools.Machine;

import java.io.FileNotFoundException;
import java.util.List;

public interface EngineCapabilities {

    List<Integer> encodeDecodeMsgAsIntegerList(List<Integer> msgAsIntegerList);
    List<Integer> createIntegerListFromString(String msg);
    String createStringFromIntegerList(List<Integer> integerList);
    String encodeDecodeMsg(String msgToEncodeDecode);
    void createEnigmaMachineFromXML(String xmlPath, boolean newMachine) throws Exception;
    Machine getMachine();
    DTO_MachineInfo createMachineInfoDTO();
    void buildRotorsStack(DTO_CodeDescription codeDescription, boolean newMachine);

    UsageHistory getUsageHistory();
    DTO_CodeDescription createCodeDescriptionDTO();
    void saveInfoToFile(String filePathAndName) throws FileNotFoundException, Exception;
    void loadInfoFromFile(String filePathAndName) throws FileNotFoundException, Exception;
    Character encodeDecodeCharacter(Character ch);
    EnigmaEngine clone();
}
