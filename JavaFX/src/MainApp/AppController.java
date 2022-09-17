package MainApp;

import BruteForce.BruteForceController;
import CodeSet.CodeSetController;
import DTOs.DTO_CodeDescription;
import DTOs.DTO_ConsumerPrinter;
import DTOs.DTO_MachineInfo;
import DecryptionManager.DecryptionManager;
import DecryptionManager.Difficulty;
import EncryptDecrypt.EncryptDecryptController;
import EnginePackage.EngineCapabilities;
import EnginePackage.EnigmaEngine;
import Tools.Machine;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;


import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;


public class AppController implements Initializable {

    private EngineCapabilities engine = new EnigmaEngine();
    private final BooleanProperty isXmlLoaded = new SimpleBooleanProperty(false);
    private final BooleanProperty isCodeChosen = new SimpleBooleanProperty(false);
    private CodeSetController codeSetController;
    private EncryptDecryptController encryptDecryptController;
    private BruteForceController bruteForceController;
    private Node rootNode;
    private DTO_MachineInfo dto_machineInfo;
    private DTO_CodeDescription dto_codeDescription;
    private final Object pausingLock = new Object();
    private boolean isPause = false;
    private IntegerProperty allTaskSize = new SimpleIntegerProperty(0);
    //private AtomicInteger numberOfTasksDone = new AtomicInteger(0);
    private IntegerProperty numberOfTasksDone = new SimpleIntegerProperty(0);;
    private Stage primaryStage;
    private Thread bruteForceThread;

    @FXML private ScrollPane sp_mainPage;
    @FXML private VBox vb_MainApp;
    @FXML private TextField tf_xmlPath;
    @FXML private TextArea tf_machineDetails, tf_machineConfiguration;
    @FXML private HBox hb_setCode;
    @FXML private Button btn_RandomCode, btn_SetCode, btn_loadFile,btn_resume;
    @FXML private Tab tab_EncryptDecrypt, tab_bruteForce, tab_machine;


    @FXML
    void loadFileBtnClick(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml files", "*.xml"));

        File fileSelected = fileChooser.showOpenDialog(primaryStage);

        if (fileSelected == null) {
            JOptionPane.showMessageDialog(null, "Could NOT choose a file!", "???", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            engine.createEnigmaMachineFromXML(fileSelected.getAbsolutePath(), true);
            isXmlLoaded.set(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Could NOT choose a file!", "???", JOptionPane.ERROR_MESSAGE);
            isXmlLoaded.set(false);
        }

    }
    @FXML
    void randomCodeBtnClick(ActionEvent event) {
        if (!isXmlLoaded.getValue()) {
            JOptionPane.showMessageDialog(null, "you should load xml file first!", "???", JOptionPane.ERROR_MESSAGE);
            return;
        }
        createRandomMachineSetting();

        //update Code Configuration in all sub-components
        dto_codeDescription = engine.createCodeDescriptionDTO();
        String codeConfigurationText = createDescriptionFormat(dto_codeDescription);
        encryptDecryptController.getTa_codeConfiguration().setText(codeConfigurationText);
        tf_machineConfiguration.setText(codeConfigurationText);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        vb_MainApp.getStylesheets().add(getClass().getResource("/CSS/MainCss.css").toExternalForm());

        tab_EncryptDecrypt.setDisable(true);
        tab_bruteForce.setDisable(true);
        btn_RandomCode.setDisable(true);
        btn_SetCode.setDisable(true);

        isXmlLoaded.addListener((obs, old, newValue) -> {
            tf_machineDetails.setText(setMachineSettingsTextArea(newValue));

            if (newValue) {
                dto_machineInfo = engine.createMachineInfoDTO();
                initializeSliderMaxValue();
            }
        });

        isXmlLoaded.addListener((obs, old, newValue) -> {
            tf_xmlPath.setText(newValue ? engine.getUsageHistory().getXmlPath() : "");
        });

        isXmlLoaded.addListener((obs, old, newValue) -> {
            btn_RandomCode.setDisable(!newValue);
            btn_SetCode.setDisable(!newValue);
        });

        isCodeChosen.addListener((obs, old, newValue) -> {
            tab_EncryptDecrypt.setDisable(!(newValue && isXmlLoaded.getValue()));
            tab_bruteForce.setDisable(!(newValue && isXmlLoaded.getValue()));
            encryptDecryptController.initializeTab();
            if (newValue) {
                dto_codeDescription = engine.createCodeDescriptionDTO();
                //update Code Configuration in all sub-components
                String codeConfigurationText = createDescriptionFormat(dto_codeDescription);
                encryptDecryptController.getTa_codeConfiguration().setText(codeConfigurationText);
                tf_machineConfiguration.setText(codeConfigurationText);

                bruteForceController.initializeTabAfterCodeConfiguration();

                initializeDictionaryListView();
            }
        });

    }

    //----------------------------------------- EncryptDecrypt Component -----------------------------------------
/*    @FXML void EncryptDecryptTabClick(ActionEvent event) {
        encryptDecryptController.initializeTab();
    }*/
    public void encryptDecryptController_proccessBtnClick() {

        String msg = encryptDecryptController.getTf_input().getText();
        if (msg.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Your message is Empty!");
            alert.show();
        }
        else if (!isMsgAllFromAbc(msg)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Your message should contain only letters from the abc!");
            alert.show();
        }
        else {
            encryptDecryptController.getTf_output().setText(engine.encodeDecodeMsg(msg,true));
        }

        // Update Statistics
        encryptDecryptController.getTa_statistics().setText(showHistoryAndStatistics());
        encryptDecryptController.getTa_codeConfiguration().setText(createDescriptionFormat(engine.createCodeDescriptionDTO()));
    }
    public void BruteForceController_proccessBtnClick() {

        String msg = bruteForceController.getTf_input().getText();
        if (msg.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Your message is Empty!");
            alert.show();
        }
        else if (!isMsgAllFromAbc(msg)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Your message should contain only letters from the abc!");
            alert.show();
        }
        else {
            bruteForceController.getTf_output().setText(engine.encodeDecodeMsg(msg,false));
        }

        // Update Statistics
        DTO_CodeDescription tmpDTO = engine.createCodeDescriptionDTO();
        tmpDTO.resetPlugBoard();
        bruteForceController.getTa_codeConfiguration().setText(createDescriptionFormat(tmpDTO));
    }

    public Character encryptDecryptController_keyboardBtnClick(Character btnChar) {

        return engine.encodeDecodeCharacter(btnChar);
    }

    public String showHistoryAndStatistics() {

        StringBuilder sb = new StringBuilder();
        for (Pair<DTO_CodeDescription, List<Pair<Pair<String, String>, Long>>> codeAndList
                : engine.getUsageHistory().getData()) {

            sb.append(createDescriptionFormat(codeAndList.getKey()));

            int index = 1;
            for (Pair<Pair<String, String>, Long> msgAndTime : codeAndList.getValue()) {

                sb.append("\n");
                sb.append(createEncodedAndDecodedMsgAndTime(msgAndTime, index));
                index++;
            }
        }
        sb.append("\n");
        return sb.toString();
    }

    private String createEncodedAndDecodedMsgAndTime(Pair<Pair<String, String>, Long> msgAndTime, int index) {

        String s = new String();
        DecimalFormat df = new DecimalFormat("#,###");
        s += "   " + index + ". <" + msgAndTime.getKey().getKey() + "> " + "--> <" + msgAndTime.getKey().getValue() + ">"
                + " (" + df.format(msgAndTime.getValue()) + " nano-seconds)";
        return s;
    }

    public String createDescriptionFormat() {

        DTO_CodeDescription dto_codeDescription = engine.createCodeDescriptionDTO();
        return createDescriptionFormat(dto_codeDescription);
    }

    public String createDescriptionFormat(DTO_CodeDescription dto_codeDescription) {
        StringBuilder sb = new StringBuilder();
        sb.append("<");
        int index = dto_codeDescription.getRotorsInUseIDList().size() - 1;

        for(int i = dto_codeDescription.getRotorsInUseIDList().size() - 1; i >= 0; i--)
        {
            Pair<String ,Pair<Integer,Integer>> rotorId = dto_codeDescription.getRotorsInUseIDList().get(i);
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

    private boolean isMsgAllFromAbc(String msg) {

        for (int i = 0; i < msg.length(); i++){
            if (!engine.getMachine().isCharInACB(msg.charAt(i)))
                return false;
        }
        return true;
    }
    //----------------------------------------- ?????????????? Component -----------------------------------------

    private void createRandomMachineSetting() {

        List<Pair<String ,Pair<Integer,Integer>>>  rotorsIDList = randomCreateIDListForRotors(dto_machineInfo.getNumOfPossibleRotors(),dto_machineInfo.getNumOfUsedRotors());
        List<Character>  startPositionList = randomCreateListForStartPosition(dto_machineInfo,rotorsIDList,dto_machineInfo.getABC(),rotorsIDList.size());
        String reflectorID = randomCreateReflectorID(dto_machineInfo.getNumOfReflectors());
        List<Pair<Character, Character>> plugBoard = randomCreatePlugBoard(dto_machineInfo.getABC());
        DTO_CodeDescription res = new DTO_CodeDescription(dto_machineInfo.getABC(),rotorsIDList,startPositionList,reflectorID,plugBoard);
        engine.buildRotorsStack(res, true);
        isCodeChosen.set(true);
    }
    private List<Pair<String ,Pair<Integer,Integer>>>  randomCreateIDListForRotors(int numOfRotors,int numOfUsedRotors) {
        List<Pair<String ,Pair<Integer,Integer>>>  rotorsIDList = new ArrayList<>();
        Random rand = new Random();
        Set<Integer > set = new HashSet<>();
        int randomNum;
        for(int i = 0; i < numOfUsedRotors; i++){
            randomNum = rand.nextInt(numOfRotors) + 1;
            while(set.contains(randomNum)) {
                randomNum = rand.nextInt(numOfRotors) + 1;
            }
            rotorsIDList.add(new Pair<>(String.valueOf(randomNum),null));
            set.add(randomNum);
        }
        return rotorsIDList;
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
            int curNotch = dto_machineInfo.getNotchPositionList().get(Integer.parseInt(tmp.getKey()) -1);
            rotorsIDList.set(i,new Pair<>(tmp.getKey(),new Pair<>(curNotch,dto_machineInfo.getABCOrderOfSpecificRotor(Integer.parseInt(tmp.getKey()) -1).indexOf(abc.charAt(randomNum)))));
        }
        return rotorsStartPositionList;
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

    public void setTab_EncryptDecrypt(Node encryptDecryptComponent) {
        this.tab_EncryptDecrypt.setContent(encryptDecryptComponent);
    }

    public void setTab_BruteForce(Node bruteForceComponent) {
        this.tab_bruteForce.setContent(bruteForceComponent);
    }

    @FXML
    void setCodeBtnClick(ActionEvent event) throws IOException {
        if (!isXmlLoaded.getValue()) {
            JOptionPane.showMessageDialog(null, "you should load xml file first!", "???", JOptionPane.ERROR_MESSAGE);
            return;
        }
        codeSetController.createSetCodeController(engine.createMachineInfoDTO());
        VBox new1 = codeSetController.getCodeSetVbox();
        rootNode = sp_mainPage.getContent();
        sp_mainPage.setContent(new1);
    }
    public DTO_MachineInfo getDtoMachineInfo() { return engine.createMachineInfoDTO();}
    public void codeSetController_setBtnClick() {

        List<Character> startPositionList = new ArrayList<>();
        if(!checkAllChoose()) {
            JOptionPane.showMessageDialog(null, "you not enter all details", "???", JOptionPane.ERROR_MESSAGE);
            return;
        }
        List<Pair<String ,Pair<Integer,Integer>>> rotorsIDList = createIDListForRotors(startPositionList);
        String reflectorID = codeSetController.getReflector().getValue();
        List<Pair<Character, Character>> plugBoard = createPlugBoard();
        DTO_CodeDescription res = new DTO_CodeDescription(engine.createMachineInfoDTO().getABC(),rotorsIDList,startPositionList,reflectorID,plugBoard);
        if (searchErrorInitInput(rotorsIDList,reflectorID,plugBoard))
            return;
        engine.buildRotorsStack(res, true);
        isCodeChosen.set(true);
        sp_mainPage.setContent(rootNode);

        //update Code Configuration in all sub-components
        dto_codeDescription = engine.createCodeDescriptionDTO();
        String codeConfigurationText = createDescriptionFormat(dto_codeDescription);
        encryptDecryptController.getTa_codeConfiguration().setText(codeConfigurationText);
        tf_machineConfiguration.setText(codeConfigurationText);
    }

    private boolean searchErrorInitInput(List<Pair<String, Pair<Integer, Integer>>> rotorsIDList, String reflectorID, List<Pair<Character, Character>> plugBoard) {
        if(!checkRotorsIDList(rotorsIDList))
            return true;
        if(reflectorID == null) {
            JOptionPane.showMessageDialog(null, "you not enter reflector ID", "???", JOptionPane.ERROR_MESSAGE);
            return true;
        }

        return false;

    }
    private boolean checkRotorsIDList(List<Pair<String, Pair<Integer, Integer>>> rotorsIDList) {
        Set<String> set = new HashSet<>();
        if(rotorsIDList.size() < engine.getMachine().getRotorsInUseCount())
        {
            JOptionPane.showMessageDialog(null,  "Error - You should select exactly " + dto_machineInfo.getNumOfUsedRotors()
                    + " Rotors!", "???", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        for (Pair<String, Pair<Integer, Integer>> id : rotorsIDList) {
            if (set.contains(id.getKey())) {
                JOptionPane.showMessageDialog(null, "you enter duplicate rotor", "???", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            else
                set.add(id.getKey());
        }
        return true;
    }
    private boolean checkAllChoose(){
        for(Pair<ChoiceBox<String>, ChoiceBox<Character>> pair :codeSetController.getRotorsChoiceBoxes()) {
            if(pair.getValue().getValue() == null || pair.getKey().getValue() == null)
                return false;
        }
        return true;

    }


    private List<Pair<Character, Character>> createPlugBoard() {
        DTO_MachineInfo dto_machineInfo = engine.createMachineInfoDTO();
        List<Pair<Character, Character>> plugBoardList = new ArrayList<>();

        for(Pair<ChoiceBox<Character>, ChoiceBox<Character>> pair :codeSetController.getPlugBoardList()) {
            plugBoardList.add(new Pair<>(pair.getKey().getValue(),pair.getValue().getValue()));
        }
        return plugBoardList;
    }

    private List<Pair<String ,Pair<Integer,Integer>>> createIDListForRotors(List<Character> startPositionList) {
        DTO_MachineInfo dto_machineInfo = engine.createMachineInfoDTO();
        List<Pair<String ,Pair<Integer,Integer>>> rotorsIDList = new ArrayList<>();
        for(Pair<ChoiceBox<String>, ChoiceBox<Character>> pair :codeSetController.getRotorsChoiceBoxes()){
            String curRotorId = pair.getKey().getValue();
            int curNotch = dto_machineInfo.getNotchPositionList().get(Integer.parseInt(curRotorId) -1);
            char curStartPosition = pair.getValue().getValue();
            rotorsIDList.add(new Pair<> (curRotorId,new Pair<>(curNotch,dto_machineInfo.getABCOrderOfSpecificRotor(Integer.parseInt(curRotorId) -1).indexOf(curStartPosition))));
            startPositionList.add(pair.getValue().getValue());
        }
        Collections.reverse(rotorsIDList);
        Collections.reverse(startPositionList);
        return rotorsIDList;
    }

    public void setCodeSetController(CodeSetController codeSetController) {
        this.codeSetController = codeSetController;
        codeSetController.setMainController(this);
    }

    public void setEncryptDecryptController(EncryptDecryptController encryptDecryptController) {
        this.encryptDecryptController = encryptDecryptController;
        encryptDecryptController.setMainController(this);
    }

    public void setBruteForceController(BruteForceController bruteForceController) {
        this.bruteForceController = bruteForceController;
        bruteForceController.setMainController(this);
    }

    private String setMachineSettingsTextArea(boolean isLoaded) {
        if(isLoaded) {
            return showMachineStatus(engine.createMachineInfoDTO());
        }
        else{
            return "";
        }
    }
    public String showMachineStatus(DTO_MachineInfo machineInfo) {
       StringBuilder sb = new StringBuilder();
        sb.append("Machine Status:\n");
        sb.append("1.    a) Number of Possible Rotors: " + machineInfo.getNumOfPossibleRotors());
        sb.append("\n      b) Number of Rotors in use: " + machineInfo.getNumOfUsedRotors());
        sb.append("\n2. Number of Reflectors: " + machineInfo.getNumOfReflectors());
        sb.append("\n3. Machine number of messages processed: " + engine.getUsageHistory().getNumOfProcessMsg() + "\n");
    return sb.toString();
    }

    public DTO_MachineInfo getDto_machineInfo() { return dto_machineInfo; }

    public DTO_CodeDescription getDtoCodeDescription() { return dto_codeDescription; }



    //-------------------------------------- BruteForce Component --------------------------------------

    public void initializeTrieWithDictionary() {

        Set<String> dictionary = engine.getMachine().getMyDictionary();

        for (String word : dictionary) {
            bruteForceController.getTrie().insert(word);
        }
    }

    public void initializeDictionaryListView() {

        Set<String> dictionary = engine.getMachine().getMyDictionary();

        for (String word : dictionary) {
            bruteForceController.getLv_dictionary().getItems().add(word);
        }
    }

    public void initializeSliderMaxValue() {
        int maxAgents = engine.getMachine().getNumOfMaxAgents();
        bruteForceController.getS_agents().setMax(maxAgents);
    }
    public void stopBruteForce(){
        bruteForceThread.interrupt();
        allTaskSize.set(0);
        numberOfTasksDone.set(0);


    }

    public void startBruteForce() {
        Consumer<DTO_ConsumerPrinter> MsgConsumer = getMsgConsumer();
        Consumer<Integer> showNumberOfTasks = getNumberOfTaskConsumer();
        setAllTaskSize(bruteForceController.getDifficulty());
        Consumer<Integer> checkFinishConsumer =  getCheckFinishConsumer(allTaskSize.get());
        DecryptionManager DM = new DecryptionManager(engine.clone(),checkFinishConsumer, bruteForceController.getTf_output().getText(),
                bruteForceController.getNumOfAgents(), bruteForceController.getDifficulty(),
                bruteForceController.getTaskSize(),
                MsgConsumer,showNumberOfTasks, numberOfTasksDone,
                bruteForceController.getIsDMWorking(),allTaskSize.get());



        bruteForceThread = new Thread(DM);
        bruteForceThread.start();

    }

    private Consumer<Integer> getNumberOfTaskConsumer() {
        return cf -> {
            double res = (double) cf / allTaskSize.getValue();
            bruteForceController.getPb_progress().setProgress(res);
        };
    }
    private Consumer<Integer> getCheckFinishConsumer(Integer numOfAllTasks)
    {
        return cf -> {
            if(cf >= numOfAllTasks){
                JOptionPane.showMessageDialog(null, "FINISH!", "DM finished", JOptionPane.ERROR_MESSAGE);
            }
        };
    }

    private Consumer<DTO_ConsumerPrinter> getMsgConsumer() {
        return cf -> {
            String configuration = createDescriptionFormat(cf.getDto_codeDescription());
            StringBuilder sb = new StringBuilder();
            sb.append(configuration).append(" candidate sentence :")
                    .append(cf.getMsgResult())
                    .append(" found by thread -")
                    .append(cf.getThreadId())
                    .append("\n");
            bruteForceController.getTa_candidates().appendText(sb.toString());
        };
    }

    private void setAllTaskSize(Difficulty difficulty) {
        Machine machine = engine.getMachine();
        int easy = (int) Math.pow(machine.getABCsize(), machine.getRotorsInUseCount());
        int medium = easy * machine.getReflectorsMapSize();
        int hard = medium * factorial(machine.getRotorsInUseCount());
        int impossible = hard * factorial(machine.getRotorsMapSize() / (factorial(machine.getRotorsInUseCount()) * factorial(machine.getRotorsMapSize() - factorial(machine.getRotorsInUseCount())))); // TODO: FIX !!!!!!!!!!1

        switch (difficulty){
            case EASY:
                allTaskSize.setValue(easy);
                break;
            case MEDIUM:
                allTaskSize.setValue(medium);
                break;
            case HARD:
                allTaskSize.setValue(hard);
                break;
            case IMPOSSIBLE:
                allTaskSize.setValue(impossible);
                break;
        }
    }

    //-------------------------------------- General --------------------------------------

    public void resetBtnClick() {
        engine.getMachine().initializePositionsForRotorsInStack();
        DTO_CodeDescription tmpDTO = engine.createCodeDescriptionDTO();
        tmpDTO.resetPlugBoard();
        bruteForceController.getTa_codeConfiguration().setText(createDescriptionFormat(tmpDTO));
    }

    public int factorial (int x) {
        int res = 1;

        for (int i = 1; i <= x; i++)
            res *= i;
        return res;
    }

    public IntegerProperty getNumberOfTasksDone() { return numberOfTasksDone; }
    public IntegerProperty getAllTaskSize() { return allTaskSize; }
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void pauseBruteForce() {
        isPause = true;
        synchronized (pausingLock) {
            if(isPause) {
                while (isPause) {
                    try {
                        //startTimeInPause = Instant.now();
                        pausingLock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //sumTimeInPause += Duration.between(startTimeInPause, Instant.now()).toMillis();
            }
        }
    }

    public void resumeBruteForce() {
        synchronized (pausingLock) {
            isPause = false;
            pausingLock.notifyAll();
        }
    }
}
