package EnginePackage;

import DTOs.DTO_CodeDescription;
import DTOs.DTO_MachineInfo;
import Tools.*;
import javafx.util.Pair;

import java.io.*;

import java.util.*;

public class EnigmaEngine implements EngineCapabilities,Serializable{

    private Machine machine;
    private UsageHistory usageHistory;


    @Override
    public UsageHistory getUsageHistory() {
        return usageHistory;
    }
    @Override
    public Machine getMachine() {
        return machine;
    }

    @Override
    public EnigmaEngine clone(){
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(this);
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            return (EnigmaEngine) ois.readObject();
        } catch (IOException e) {
            return null;
        } catch (ClassNotFoundException e) {
            return null;
        }
    }
    @Override
    public DTO_MachineInfo createMachineInfoDTO() {

        List<Integer> notchPositionList = new LinkedList<>();
        List<String> listOfSpecificRotorAbcOrder = new ArrayList<>();
        for (int i = 0; i < machine.getRotorsMapSize(); i++) {
            notchPositionList.add(machine.getRotorsMap().get(String.valueOf(i + 1)).getNotch());
            listOfSpecificRotorAbcOrder.add(machine.getOrderOfSpecificRotor(machine.getRotorsMap().get(String.valueOf(i + 1))));
        }

        return new DTO_MachineInfo(machine.getAbc(), machine.getRotorsMapSize(), machine.getRotorsInUseCount(),
                                   notchPositionList, machine.getReflectorsMapSize(),listOfSpecificRotorAbcOrder);
    }

    @Override
    public void buildRotorsStack(DTO_CodeDescription codeDescription, boolean newMachine) {

        if (newMachine)
            usageHistory.addCodeSegment(codeDescription);
        machine.buildRotorsStack(codeDescription);
    }
    @Override
    public DTO_CodeDescription createCodeDescriptionDTO() { // From the engine to the UI

        List<Pair<String ,Pair<Integer,Integer>>> rotorInUseIDList = new ArrayList<>();
        List<Character> startingPositionList = new LinkedList<>(); // maybe make it <Character, Integer> like in Rotor class field
        String str;
        for (int i = 0; i < machine.getRotorsInUseCount(); i++) { // The last one is a Reflector
            Rotor currentRotor = (Rotor)machine.getRotorsStack().get(i);
            rotorInUseIDList.add(new Pair<>(currentRotor.getID(),new Pair<>(currentRotor.getNotch(),currentRotor.getCurrentPairIndex())));
            str = machine.getOrderOfSpecificRotor(currentRotor);
            startingPositionList.add(str.charAt(currentRotor.getCurrentPairIndex()));
        }

        String reflectorID = machine.getRotorsStack().get(machine.getRotorsInUseCount()).getID();
        List<Pair<Character, Character>> plugsInUseList = new ArrayList<>();

        for (Map.Entry<Character, Character> entry : machine.getFirstSidePlugBoardMap().entrySet()) // One side is enough
            plugsInUseList.add(new Pair<>(entry.getKey(), entry.getValue()));

        return new DTO_CodeDescription(machine.getAbc(), rotorInUseIDList, startingPositionList, reflectorID, plugsInUseList);
    }

    @Override
    public void saveInfoToFile(String filePathAndName) throws Exception {

        FileOutputStream fos = new FileOutputStream(filePathAndName);
        ObjectOutputStream out = new ObjectOutputStream(fos);
        out.writeObject(usageHistory);
        out.close();
    }

    @Override
    public void loadInfoFromFile(String filePathAndName) throws Exception {

        FileInputStream fis = new FileInputStream(filePathAndName);
        ObjectInputStream in = new ObjectInputStream(fis);

        usageHistory = (UsageHistory) in.readObject();
        createEnigmaMachineFromXML(usageHistory.getXmlPath(), false);
        buildRotorsStack(usageHistory.getCurrentCodeDescription(), false);
    }

    @Override
    public void createEnigmaMachineFromXML(String xmlPath, boolean newMachine) throws Exception {
        Factory factory = new Factory(xmlPath);
        machine = factory.createMachine();

        if (newMachine) // The machine is NOT built from the Load option, therefor need New
            usageHistory = new UsageHistory();
        usageHistory.setXmlPath(xmlPath);
    }

    @Override
    public List<Integer> encodeDecodeMsgAsIntegerList(List<Integer> msgAsIntegerList) { // where to convert to integers and strings?

        List<Integer> encodeDecodeMsgAsInteger = new ArrayList<>();

        for (Integer integer : msgAsIntegerList)
            encodeDecodeMsgAsInteger.add(encodeDecodeLetterAsInteger(integer));

        return encodeDecodeMsgAsInteger;
    }

    @Override
    public List<Integer> createIntegerListFromString(String msg) {

        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < msg.length(); i++)
            list.add(machine.convertCharToInt(msg.charAt(i)));
        return  list;
    }

    @Override
    public String createStringFromIntegerList(List<Integer> integerList) {

        String str = new String();
        for (Integer integer : integerList)
            str += machine.convertIntToChar(integer);
        return str;
    }

    @Override
    public String encodeDecodeMsg(String msgToEncodeDecode) {

        long start = System.nanoTime();
        String MsgAfterPlugBoard = machine.buildStringWithPlugBoard(msgToEncodeDecode);
        List<Integer> msgAsIntegerList = createIntegerListFromString(MsgAfterPlugBoard);
        List<Integer> encodedDecodedMsgAsIntegerList = encodeDecodeMsgAsIntegerList(msgAsIntegerList);

        String res = createStringFromIntegerList(encodedDecodedMsgAsIntegerList);
        String resMsg = machine.buildStringWithPlugBoard(res);

        long end = System.nanoTime();
        usageHistory.addMsgAndTimeToCurrentCodeSegment(msgToEncodeDecode, resMsg, end - start);

        return resMsg;
    }

    @Override
    public Character encodeDecodeCharacter(Character ch) {

        Character charAfterPlugBoard = machine.buildCharacterWithPlugBoard(ch);
        Integer charAsInteger = machine.convertCharToInt(charAfterPlugBoard);
        Integer encodeDecodeCharacterAsInteger = encodeDecodeLetterAsInteger(charAsInteger);
        Character res = machine.convertIntToChar(encodeDecodeCharacterAsInteger);
        Character resChar = machine.buildCharacterWithPlugBoard(res);
        return resChar;
    }

    private Integer encodeDecodeLetterAsInteger(Integer letterAsInt) {

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
        while(index != machine.getRotorsStack().size() - 1)
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
