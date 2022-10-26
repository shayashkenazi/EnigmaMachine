package EnginePackage;

import DTOs.DTO_AgentMachine;
import DTOs.DTO_CodeDescription;
import DTOs.DTO_MachineInfo;
import Tools.Machine;
import enigmaException.EnigmaException;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

public interface EngineCapabilities {

    List<Integer> encodeDecodeMsgAsIntegerList(List<Integer> msgAsIntegerList);
    List<Integer> createIntegerListFromString(String msg);
    String createStringFromIntegerList(List<Integer> integerList);
    String encodeDecodeMsg(String msgToEncodeDecode,boolean withPlugBoard);
    void createEnigmaMachineFromXML(String xmlPath, boolean newMachine) throws EnigmaException, JAXBException,FileNotFoundException;
    public void createEnigmaMachineFromXMLInputStream(InputStream inputStream, boolean newMachine) throws EnigmaException, JAXBException,FileNotFoundException;
    Machine getMachine();
    void setMachine(Machine machine);
    DTO_MachineInfo createMachineInfoDTO();
    DTO_AgentMachine createAgentMachineDTO();
    Machine createMachineFromDTOAgentMachine(DTO_AgentMachine dto_agentMachine);
    void buildRotorsStack(DTO_CodeDescription codeDescription, boolean newMachine);

    UsageHistory getUsageHistory();
    DTO_CodeDescription createCodeDescriptionDTO();
    void saveInfoToFile(String filePathAndName) throws FileNotFoundException, Exception;
    void loadInfoFromFile(String filePathAndName) throws FileNotFoundException, Exception;
    Character encodeDecodeCharacter(Character ch);
    EnigmaEngine clone();
    void moveRotorsToPosition(int stepSize);
    void rotateRotorByABC();
    boolean checkAtDictionary(String sentenceToCheck);
}
