import DTOs.DTO_CodeDescription;
import DTOs.DTO_MachineInfo;
import Tools.*;
import javafx.util.Pair;
import java.time.Duration;
import java.time.Instant;

import java.util.*;

public class EnigmaEngine implements EngineCapabilities{

    private Machine machine;
    private UsageHistory usageHistory = new UsageHistory();


    @Override
    public UsageHistory getUsageHistory() {
        return usageHistory;
    }
    @Override
    public Machine getMachine() {
        return machine;
    }


    @Override
    public DTO_MachineInfo createMachineInfoDTO() {

        List<Integer> notchPositionList = new LinkedList<>();

        for (int i = 0; i < machine.getRotorsMapSize(); i++)
            notchPositionList.add(machine.getRotorsMap().get(String.valueOf(i+1)).getNotch());

        return new DTO_MachineInfo(machine.getAbc(), machine.getRotorsMapSize(), machine.getRotorsInUseCount(),
                                   notchPositionList, machine.getReflectorsMapSize(), machine.getNumOfProccessedMsg());
    }

    @Override
    public void buildRotorsStack(DTO_CodeDescription codeDescription) {
        usageHistory.addCodeSegment(codeDescription);
        machine.buildRotorsStack(codeDescription);
    }
    @Override
    public DTO_CodeDescription createCodeDescriptionDTO() { // From the engine to the UI

        List<Pair<String ,Pair<Integer,Integer>>> rotorInUseIDList = new ArrayList<>();
        List<Character> startingPositionList = new LinkedList<>(); // maybe make it <Character, Integer> like in Rotor class field

        for (int i = 0; i < machine.getRotorsInUseCount() - 1; i++) { // The last one is a Reflector

            Rotor currentRotor = (Rotor)machine.getRotorsStack().get(i);
            rotorInUseIDList.add(new Pair<>(currentRotor.getID(),new Pair<>(currentRotor.getNotch(),currentRotor.getCurrentPairIndex())));
            startingPositionList.add(currentRotor.getFirstPairPosition().getKey());
        }

        String reflectorID = machine.getRotorsStack().get(machine.getRotorsInUseCount() - 1).getID();
        List<Pair<Character, Character>> plugsInUseList = new LinkedList<>();

        for (Map.Entry<Character, Character> entry : machine.getFirstSidePlugBoardMap().entrySet()) // One side is enough
            plugsInUseList.add(new Pair<>(entry.getKey(), entry.getValue()));

        return new DTO_CodeDescription(machine.getAbc(), rotorInUseIDList, startingPositionList, reflectorID, plugsInUseList);
    }

    @Override
    public void createEnigmaMachineFromXML(String xmlPath) throws Exception {
        Factory factory = new Factory(xmlPath);
        machine = factory.createMachine();
    }

    @Override
    public List<Integer> encodeDecodeMsgAsIntegerList(List<Integer> msgAsIntegerList) { // where to convert to integers and strings?

        List<Integer> encodeDecodeMsgAsInteger = new ArrayList<>();

        for (Integer integer : msgAsIntegerList)
            encodeDecodeMsgAsInteger.add(encodeDecodeLetterAsInteger(integer));

        return encodeDecodeMsgAsInteger;
    }

    @Override
    public List<Integer> createIntegerListFromString(String msg) { // TBD - should it be privete?

        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < msg.length(); i++)
            list.add(machine.convertCharToInt(msg.charAt(i)));
        return  list;
    }

    @Override
    public String createStringFromIntegerList(List<Integer> integerList) { // TBD - should it be privete?

        String str = new String();
        for (Integer integer : integerList)
            str += machine.convertIntToChar(integer);
        return str;
    }

    @Override
    public String encodeDecodeMsg(String msgToEncodeDecode) {
        Instant start = Instant.now();
        String MsgAfterPlugBoard = machine.buildStringWithPlugBoard(msgToEncodeDecode);
        List<Integer> msgAsIntegerList = createIntegerListFromString(MsgAfterPlugBoard);
        List<Integer> encodedDecodedMsgAsIntegerList = encodeDecodeMsgAsIntegerList(msgAsIntegerList);

        String res = createStringFromIntegerList(encodedDecodedMsgAsIntegerList);
        String resMsg = machine.buildStringWithPlugBoard(res);

        Instant end = Instant.now();
        usageHistory.addMsgAndTimeToCurrentCodeSegment(msgToEncodeDecode,resMsg, Duration.between(start, end));


        return resMsg;
    }

    private Integer encodeDecodeLetterAsInteger(Integer letterAsInt) { // maybe we should get as a Cheracter?? and convert inside

        Integer encodeDecodeLetterAsInteger = letterAsInt;
        Rotor currentRotor = (Rotor)machine.getRotorsStack().get(0);
        int size = machine.getABCsize();
        List<Integer> trackChanges = new ArrayList<>(); // maybe for the future

        rotateRotors();

        for (Switcher switcher : machine.getRotorsStack()){

            encodeDecodeLetterAsInteger = switcher.Switch(encodeDecodeLetterAsInteger, true); // maybe the opposite ??
            trackChanges.add(encodeDecodeLetterAsInteger);
        }

        for (int j = machine.getRotorsStack().size() - 2; j >= 0; j--){

            Switcher currentSwitcher = machine.getRotorsStack().get(j); // fix this !!! Switcher and not Rotor
            encodeDecodeLetterAsInteger = currentSwitcher.Switch(encodeDecodeLetterAsInteger, false);
            trackChanges.add(encodeDecodeLetterAsInteger);
        }
        return encodeDecodeLetterAsInteger;
    }

    private void rotateRotors() {

        int index = 0;
        Rotor currentRotor = (Rotor)machine.getRotorsStack().get(index++);
        currentRotor.rotateRotor();
        while(index != machine.getRotorsStack().size() - 1) // TBD run as a rotor reflection ,while I'm not reflector
        {
            if(currentRotor.getCurrentPairIndex() == currentRotor.getNotch())
            {
                currentRotor = (Rotor)machine.getRotorsStack().get(index++);
                currentRotor.rotateRotor();
            }
            else
                break;
        }
    }
}
