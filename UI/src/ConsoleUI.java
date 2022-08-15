import DTOs.DTO_CodeDescription;
import DTOs.DTO_MachineInfo;
import javafx.util.Pair;
import java.time.Duration;
import java.util.*;

public class ConsoleUI implements UIprogram{

    private final EngineCapabilities engine = new EnigmaEngine();
    private final Scanner sc = new Scanner(System.in);
    private UserOptions userOption;
    private boolean isXmlLoaded = false;
    private boolean isCodeChosen = false;

    @Override
    public void run() {

        do {
            showManu();
            if (!getUserChoiceAsEnum())
                continue;
            ManageOptions();

        } while (userOption != UserOptions.EXIT);
    }

    @Override
    public void showHistoryAndStatistics() {
        for (Pair<DTO_CodeDescription, List<Pair<Pair<String, String>, Duration>>> codeAndList
                : engine.getUsageHistory().getData()) {

            printDescriptionFormat(codeAndList.getKey());

            int index = 1;
            for (Pair<Pair<String, String>, Duration> msgAndTime : codeAndList.getValue()) {

                printEncodedAndDecodedMsgAndTime(msgAndTime, index);
                index++;
            }
        }
    }
    private void printEncodedAndDecodedMsgAndTime(Pair<Pair<String, String>, Duration> msgAndTime, int index) {

        //StringBuilder sb = new StringBuilder();
        String s = new String();
        s += index + ". <" + msgAndTime.getKey().getKey() + "> " + "--> <" + msgAndTime.getKey().getValue() + ">"
                + " (" + msgAndTime.getValue() + " nano-seconds)";
        System.out.println(s);
    }

    private Boolean getUserChoiceAsEnum() {
        String tempChoice = sc.nextLine();
        try {
            userOption = validateUserChoiceAndConvertToEnum(Integer.parseInt(tempChoice));
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    private UserOptions validateUserChoiceAndConvertToEnum(Integer userChoiceInt) throws Exception {

        if (userChoiceInt < 1 || userChoiceInt > 8)
            throw new Exception("Error - This option is out of range! Please select a number in range 1-8.");

        return UserOptions.values()[userChoiceInt - 1]; // Option 1 for user is actually Option 0 for system
    }

    private void ManageOptions (){

        switch (userOption) {

            case LOAD_XML:
                createEnigmaMachineFromXmlIfPossible();
                System.out.println("Operation scudded!");
                break;

            case SHOW_MACHINE_STATUS: // only if xml is loaded AND code had been chosen
                showMachineStatusIfPossible();
                break;

            case CREATE_SPECIFIC_MACHINE: // only if xml is loaded
                createSpecificMachineSetting();
                isCodeChosen = true;
                break;

            case CREATE_RANDOM_MACHINE: // only if xml is loaded
                createRandomMachineSetting();
                isCodeChosen = true;
                break;

            case ENCODE_DECODE:
                PrintEncodeDecodeMsgIfPossible();
                break;

            case CODE_INITIALIZE:
                engine.getMachine().initializePositionsForRotorsInStack();
                System.out.println("Operation scudded!");
                break;

            case HISTORIC_AND_STATISTIC: // 7
                if (isXmlLoaded)
                    showHistoryAndStatistics();
                else
                    System.out.print("Error - In order to select this option, you should load xml file first!");

                break;

            case SAVE_MACHINE_INFORMATION: // Bonus


                break;

            case LOAD_MACHINE_INFORMATION: // Bonus


                break;

            case EXIT:
                break;
        }
    }

    @Override
    public void showManu() {

        int OptionIndex = 1;
        for (UserOptions option : UserOptions.values()) {
            System.out.println(OptionIndex + ". " + option.getDescription());
            OptionIndex++;
        }
    }

    private void PrintEncodeDecodeMsgIfPossible() {

        if (!isXmlLoaded)
            System.out.print("Error - In order to select this option, you should load xml file first!");
        else if (!isCodeChosen)
            System.out.print("Error - In order to select this option, you should load machine code first!");
        else {
            System.out.print("Please enter the message you would like to Encode / Decode:");
            System.out.print(engine.encodeDecodeMsg(sc.nextLine()));
        }
    }

    private void createEnigmaMachineFromXmlIfPossible() {

        System.out.print("Please enter FULL path for the XML file:");
        String xmlPath = sc.nextLine();
        if (!validateXMLFile(xmlPath)){
            System.out.println("Error - xml file should end with .xml!");
        }
        else {
            try {
                engine.createEnigmaMachineFromXML(xmlPath);
                isXmlLoaded = true;
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void showMachineStatusIfPossible() {

        if (!isXmlLoaded) {
            System.out.print("Error - In order to select this option, you should load xml file first!");
        }
        /*else if (!isCodeChosen) {
            System.out.print("Error - In order to select this option, you should load machine code first!");
        }*/
        else
            showMachineStatus(engine.createMachineInfoDTO());
    }
    @Override
    public void ReadMachineSetting() {

    }



    public void createRandomMachineSetting() {
        DTO_MachineInfo dto_machineInfo = engine.createMachineInfoDTO();
        List<Pair<String ,Pair<Integer,Integer>>>  rotorsIDList = randomCreateIDListForRotors(dto_machineInfo.getNumOfPossibleRotors());
        List<Character>  startPositionList = randomCreateListForStartPosition(dto_machineInfo,rotorsIDList,dto_machineInfo.getABC(),rotorsIDList.size());
        String reflectorID = randomCreateReflectorID(dto_machineInfo.getNumOfReflectors());
        List<Pair<Character, Character>> plugBoard = randomCreatePlugBoard(dto_machineInfo.getABC());
        DTO_CodeDescription res = new DTO_CodeDescription(dto_machineInfo.getABC(),rotorsIDList,startPositionList,reflectorID,plugBoard);
        engine.buildRotorsStack(res);
    }

    private List<Character> randomCreateListForStartPosition(DTO_MachineInfo dto_machineInfo,List<Pair<String ,Pair<Integer,Integer>>> rotorsIDList,String abc, int numOfRotors) {
        List<Character> rotorsStartPositionList = new ArrayList<>();
        Set<Character> set = new HashSet<>();
        Random rand = new Random();
        int randomNum;
        for(int i = 0; i < numOfRotors; i++) {
            randomNum = rand.nextInt(abc.length());
            while(set.contains(abc.charAt(randomNum))) {
                randomNum = rand.nextInt(abc.length());
            }
            set.add(abc.charAt(randomNum));
            rotorsStartPositionList.add(abc.charAt(randomNum));
            Pair<String, Pair<Integer, Integer>> tmp = rotorsIDList.get(i);
            int curNotch = dto_machineInfo.getNotchPositionList().get(i);
            rotorsIDList.set(i,new Pair<>(tmp.getKey(),new Pair<>(curNotch,randomNum)));
        }
     return rotorsStartPositionList;
    }
    private List<Pair<String ,Pair<Integer,Integer>>>  randomCreateIDListForRotors(int numOfRotors) {
        List<Pair<String ,Pair<Integer,Integer>>>  rotorsIDList = new ArrayList<>();
        Random rand = new Random();
        Set<Integer > set = new HashSet<>();
        int randomNum = rand.nextInt(numOfRotors - 2) + 2;
        for(int i = 0; i < randomNum; i++){
            randomNum = rand.nextInt(numOfRotors - 1) + 1;
            while(set.contains(randomNum)) {
                randomNum = rand.nextInt(numOfRotors - 1) + 1;
            }
            rotorsIDList.add(new Pair<>(String.valueOf(randomNum),null));
            set.add(randomNum);
        }
        return rotorsIDList;
    }
    private String randomCreateReflectorID(int numOfReflectors) {
        Random rand = new Random();
        Map<Integer, String> MapNumbers = new LinkedHashMap<>();
        MapNumbers.put(1,"I");
        MapNumbers.put(2,"II");
        MapNumbers.put(3,"III");
        MapNumbers.put(4,"VI");
        MapNumbers.put(5,"V");
        return MapNumbers.get(rand.nextInt(numOfReflectors) + 1);

    }

    private List<Pair<Character, Character>> randomCreatePlugBoard(String abc) {
        List<Pair<Character, Character>> res = new ArrayList<>();
        Random rand = new Random();
        int randomNum = rand.nextInt(abc.length()/2 + 1);
        int randLetter1,randLetter2;
        Set<Integer > set = new HashSet<>();
        for(int i = 0; i < randomNum; i++){
            randLetter1 = rand.nextInt(abc.length()) ;
            randLetter2 = rand.nextInt(abc.length()) ;
            while(set.contains(randLetter1)) {
                randLetter1 = rand.nextInt(abc.length());
            }
            set.add(randLetter1);
            while(set.contains(randLetter2)) {
                randLetter2 = rand.nextInt(abc.length());
            }
            set.add(randLetter2);
            res.add(new Pair<>(abc.charAt(randLetter1),abc.charAt(randLetter2)));

        }
        return res;
    }
    @Override
    public void createSpecificMachineSetting() {
        try {
            DTO_MachineInfo dto_machineInfo = engine.createMachineInfoDTO();
            List<Pair<String ,Pair<Integer,Integer>>> rotorsIDList = createIDListForRotors(dto_machineInfo.getNumOfPossibleRotors());
            List<Character>  startPositionList = createListForStartPosition(dto_machineInfo.getABC(),rotorsIDList.size(),rotorsIDList,dto_machineInfo);
            String reflectorID = createReflectorID(dto_machineInfo.getNumOfReflectors());
            List<Pair<Character, Character>> plugBoard = createPlugBoard(dto_machineInfo.getABC());
            DTO_CodeDescription res = new DTO_CodeDescription(dto_machineInfo.getABC(),rotorsIDList,startPositionList,reflectorID,plugBoard);
            engine.buildRotorsStack(res);
        }
        catch(Exception e) {
            System.out.println("you enter wrong input!\nDo you wnat  ");
        }
    }

    private List<Pair<Character, Character>> createPlugBoard(String abc) throws Exception {
        String msg = "please chose your plugboard letters:";
        String temp;
        System.out.println(msg);
        temp = sc.nextLine();
        temp = temp.toUpperCase();
        List<Pair<Character,Character>> res = new ArrayList<>();
        Set <Character> set = new HashSet<>();
        if(temp.length() % 2 != 0)
            throw new Exception("Need an even number of letters!");
        for(int i = 0; i < temp.length(); i += 2) {
            checkPlugBoardInput(abc, temp.substring(i, i + 2), set);
            res.add(new Pair<>(temp.charAt(i),temp.charAt(i+1)));
        }
        return res;
    }

    private String createReflectorID(Integer numOfReflectors) throws Exception {

        String msg = "please chose your reflector number:";
        String temp;
        System.out.println(msg);
        Map<Integer, String> MapNumbers = new LinkedHashMap<>();
        MapNumbers.put(1,"I");
        MapNumbers.put(2,"II");
        MapNumbers.put(3,"III");
        MapNumbers.put(4,"VI");
        MapNumbers.put(5,"V");
        for(int i = 0; i < numOfReflectors; i++)
        {
            System.out.println(i+1 + ". " + MapNumbers.get(i+1));
        }
        temp = sc.nextLine();
        checkRange(temp,numOfReflectors);
        return MapNumbers.get(Integer.parseInt(temp));
    }

    private List<Pair<String ,Pair<Integer,Integer>>> createIDListForRotors(int numOfRotors) throws Exception{

        String msg = "Please chose rotors numbers from 1 to - " + numOfRotors + " separate by comma";
        String temp;
        List<Pair<String ,Pair<Integer,Integer>>> rotorsIDList = new ArrayList<>();
        Set <Integer> set = new HashSet<>();
        System.out.println(msg);
        temp = sc.nextLine();
        List<String> items = Arrays.asList(temp.split(","));

        for(String str : items) {
            checkIntInput(str,numOfRotors,set);
        }
        for(String str : items) {
            rotorsIDList.add(new Pair<>(str,null));
        }

        System.out.println("your rotors list is -" + items);
        Collections.reverse(rotorsIDList);
        return rotorsIDList;
    }

    private void checkIntInput(String input, int size,Set <Integer> set) throws Exception{

        int check = Integer.parseInt(input);
        if (check < 1 || check > size)
            throw new Exception("Please chose intreeger in the range!");
        if(set.contains(check))
            throw new Exception("you enter duplicate rotor");
        else
            set.add(check);

    }
    private void checkPlugBoardInput(String abc,String input,Set <Character> set) throws Exception {
        char[] tempChars = input.toCharArray();
        if(abc.indexOf(tempChars[0]) == -1 || abc.indexOf(tempChars[1]) == -1)
            throw new Exception("Please chose chars from - " + abc);
        if (set.contains(tempChars[0]) || set.contains(tempChars[1]))
            throw new Exception("Please dont repeat chars!");

        set.add(tempChars[0]);
        set.add(tempChars[1]);
    }


    /*private void getStringAndAddToList(int numOfRotors, List<String> rotorsIDList, String msg) {
        String temp;
        System.out.println(msg);
        temp = sc.nextLine();
        while(!checkIntChoice(temp,msg) || !checkRange(temp,numOfRotors,msg)) {
            temp = sc.nextLine();
        }
        rotorsIDList.add(temp);
        System.out.println("your choice is - " + temp );

    }*/
    /*private String getCharAndAddToList(int numOfRotors, List<Character> rotorsIDList, String msg) {
        String temp;
        System.out.println(msg);
        temp = sc.nextLine();
        while(!checkIntChoice(temp,msg) || !checkRange(temp,numOfRotors,msg)) {
            temp = sc.nextLine();
        }
        rotorsIDList.add(temp.charAt(0));
        System.out.println("your choice is - " + temp );
        return temp;
    }*/
    private void checkRange(String input, int size) throws Exception {

        int check = Integer.parseInt(input);

        if (check < 1 || check > size)
            throw new Exception("Please chose integer in the range!");
    }


    private List<Character> createListForStartPosition(String abc, int numOfRotor,
                                      List<Pair<String, Pair<Integer, Integer>>> rotorIDList,
                                      DTO_MachineInfo dto_machineInfo) throws Exception{
        List<Character> rotorsStartPositionList = new ArrayList<>();
        Set <Character> set = new HashSet<>();
        String msg = "Please chose " + numOfRotor + "start position from the abc - " + abc;
        String temp;
        System.out.println(msg);
        temp = sc.nextLine();
        temp = temp.toUpperCase();
        char[] chars = temp.toCharArray();
        if(chars.length != numOfRotor)
            throw new Exception("you enter less letters than number of rotors!");
        for(char ch : chars) {
            checkCharInput(ch,abc,set);
        }
        int index = 0;
        for(char ch : chars) {
            rotorsStartPositionList.add(ch);
            Pair<String, Pair<Integer, Integer>> tmp = rotorIDList.get(index);
            int curNotch = dto_machineInfo.getNotchPositionList().get(index);
            rotorIDList.set(index++,new Pair<>(tmp.getKey(),new Pair<>(curNotch,abc.indexOf(ch))));

        }
        Collections.reverse(rotorsStartPositionList);
        return rotorsStartPositionList;
    }
    private void checkCharInput(char input, String abc,Set <Character> set) throws Exception{

        if (abc.indexOf(input) == -1) {
            throw new Exception("Please chose char from the abc");
        }
        set.add(input);

    }


    @Override
    public boolean handleChoice(int choice) {
        return false;
    }

    @Override
    public void showMachineStatus(DTO_MachineInfo machineInfo) {

        System.out.println("Machine Status:");
        System.out.println("1.    a) Number of Possible Rotors: " + machineInfo.getNumOfPossibleRotors() );
        System.out.println("      b) Number of Rotors in use: " + machineInfo.getNumOfUsedRotors());
        System.out.println("2. Number of Reflectors: " + machineInfo.getNumOfReflectors());
        System.out.println("3. Machine number of messages - " + machineInfo.getNumOfMsgProcessed());
        if(isXmlLoaded && isCodeChosen) {
            System.out.println("4.  Description for the original code:");
            printDescriptionFormat(engine.createCodeDescriptionDTO());
        }
        if(isXmlLoaded && isCodeChosen) {
            System.out.println("5.  Description for the current code:");
            printDescriptionFormat(engine.createCodeDescriptionDTO());
        }
        /*System.out.println("Machine Status:");
        System.out.println("1.    a) Number of Possible Rotors: " + machineInfo.getNumOfPossibleRotors() );
        System.out.println("      b) Number of Rotors in use: " + machineInfo.getNumOfUsedRotors());
        System.out.println("2. Notches Positions for every Rotor:");
        int rotorNumber = 0;
        for (Integer notchPos : machineInfo.getNotchPositionList()) {
            System.out.println("  The Notch position for Rotor number: " + rotorNumber + " is: " + notchPos);
            rotorNumber++;
        }
        System.out.println("3. Number of Reflectors: " + machineInfo.getNumOfReflectors());
        System.out.println("4. Number of Processed messages: " + machineInfo.getNumOfMsgProcessed());
        System.out.println("5. Description for the current code:");
        printDescriptionFormat(machineInfo);*/
    }

    private void printDescriptionFormat(DTO_CodeDescription dto_codeDescription) {

        StringBuilder sb = new StringBuilder();
        sb.append("<");
        int index = 0;
        for (Pair<String ,Pair<Integer,Integer>> rotorId : dto_codeDescription.getRotorsInUseIDList()) {
            int distance = Math.floorMod(dto_codeDescription.getNotch(rotorId) - dto_codeDescription.getCurrent(rotorId), dto_codeDescription.getABC().length()) ; //'% dto_codeDescription.'
            sb.append(rotorId.getKey()).append("(").append(distance).append("),"); // need to have curr index eac h rotor
        }
        sb.deleteCharAt(sb.length() - 1); // for the last ','
        sb.append("><").append(String.join(",", dto_codeDescription.getStartingPositionList().toString()
                .replace(" ", "")
                .replace("[", "")
                .replace("]", ""))).append(">");
        sb.append("<").append(dto_codeDescription.getReflectorID()).append(">");

        if (dto_codeDescription.getPlugsInUseList().size() != 0) {
            sb.append("<");
            for (int i = 0; i < dto_codeDescription.getPlugsInUseList().size(); i++) {
                Pair<Character, Character> pair = dto_codeDescription.getPlugsInUseList().get(i);
                sb.append(pair.getKey()).append("|").append(pair.getValue()).append(",");
            }
            sb.replace(sb.length() - 1,sb.length() - 1,">");// replace the last ',' with '>'
        }

        System.out.println(sb.toString());
    }


    public boolean validateXMLFile(String xmlPath) {
        return xmlPath.endsWith(".xml");
    }

    public String  foo() throws Exception {


        engine.createEnigmaMachineFromXML("C:\\Users\\shaya\\IdeaProjects\\EnigmaMachine\\Engine\\src\\Resources\\ex1-sanity-paper-enigma.xml"); // throws exception
        //engine.createEnigmaMachineFromXML("C:\\Users\\shaya\\IdeaProjects\\EnigmaMachine\\Engine\\src\\Resources\\ex1-sanity-small.xml");
        //engine.createEnigmaMachineFromXML("C:/Users/idofa/Desktop/College/JAVA/EnigmaMachine/Engine/src/Resources/ex1-sanity-small.xml");
        List<String> rotorsIDList = new ArrayList<>(Arrays.asList("3", "2","1"));
        List<Character> startPositionList = new ArrayList<>(Arrays.asList('X', 'D','O'));
        //List<String> rotorsIDList = new ArrayList<>(Arrays.asList("1","2"));
        //List<Character> startPositionList = new ArrayList<>(Arrays.asList('C','C'));
        String reflectorID = "I";
        //engine.buildRotorsStack(rotorsIDList, startPositionList, reflectorID);
        List<Pair<Character, Character>> listPlugBoard = new ArrayList<>();
        //listPlugBoard.add(new Pair<>('A', 'F'));
        engine.getMachine().buildPlugBoard(listPlugBoard);

        String res =  engine.encodeDecodeMsg("THERAINISDROPPING");
        showMachineStatus(engine.createMachineInfoDTO());
        return res;


    }
}
