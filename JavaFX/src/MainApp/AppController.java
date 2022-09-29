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
import java.util.concurrent.ArrayBlockingQueue;
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
    private DecryptionManager curDM;
    private IntegerProperty allTaskSize = new SimpleIntegerProperty(0);
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
    private ComboBox<String> cb_styles;

    @FXML
    void loadFileBtnClick(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml files", "*.xml"));

        File fileSelected = fileChooser.showOpenDialog(primaryStage);

        if (fileSelected == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR,"Could NOT choose a file!");
            alert.show();
            return;
        }

        try {
            engine.createEnigmaMachineFromXML(fileSelected.getAbsolutePath(), true);
            isXmlLoaded.set(false);
            isXmlLoaded.set(true);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR,"Could NOT choose a file! " + e.getMessage());
            alert.show();
            isXmlLoaded.set(false);
        }

    }
    @FXML
    void randomCodeBtnClick(ActionEvent event) {
        if (!isXmlLoaded.getValue()) {
            Alert alert = new Alert(Alert.AlertType.ERROR,"you should load xml file first!");
            alert.show();
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
        /*sp_mainPage.getStylesheets().add(getClass().getResource("/CSS/Dark.css").toExternalForm());
        vb_MainApp.getStylesheets().add(getClass().getResource("/CSS/Dark.css").toExternalForm());
        cb_styles.getItems().add("Default");
        cb_styles.getItems().add("Default");
        cb_styles.getItems().add("Default");
        cb_styles.getItems().add("Default");*/
        tab_EncryptDecrypt.setDisable(true);
        tab_bruteForce.setDisable(true);
        btn_RandomCode.setDisable(true);
        btn_SetCode.setDisable(true);

        isXmlLoaded.addListener((obs, old, newValue) -> {
            tf_machineDetails.setText(setMachineSettingsTextArea(newValue));

            if (newValue) {
                dto_machineInfo = engine.createMachineInfoDTO();
                tf_machineConfiguration.setText("");
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
                DTO_CodeDescription TMP = engine.createCodeDescriptionDTO();
                TMP.resetPlugBoard();
                bruteForceController.getTa_codeConfiguration().setText(createDescriptionFormat(TMP));
                tf_machineConfiguration.setText(codeConfigurationText);
                bruteForceController.getTa_candidates().clear();
                bruteForceController.initializeTabAfterCodeConfiguration();

                initializeDictionaryListView();
            }
        });



    }
    //----------------------------------------- EncryptDecrypt Component -----------------------------------------
    public void encryptDecryptController_proccessBtnClick() {

        String msg = encryptDecryptController.getTf_input().getText();
        if (msg.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Your message is Empty!");
            alert.show();
        }
        else if (!isMsgAllFromAbc(msg)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Your message should contain only letters from the abc");
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
        else if (!isMsgAllFromAbcAndDictionary(msg)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Your message should contain only letters from the abc and dictionary!");
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
    private boolean isMsgAllFromAbcAndDictionary(String msg) {
        for (int i = 0; i < msg.length(); i++){
            if (!engine.getMachine().isCharInACB(msg.charAt(i)))
                return false;
        }
        Set<String> dictionary = engine.getMachine().getMyDictionary();
        String[] wordsAll = msg.split(" ");
        for (String str : wordsAll){
            if (!dictionary.contains(str))
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
        isCodeChosen.set(false);
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
        MapNumbers.put(4,"IV");
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
            Alert alert = new Alert(Alert.AlertType.ERROR,"you should load xml file first!");
            alert.show();
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
            codeSetController.resetPlugBoard();
            Alert alert = new Alert(Alert.AlertType.ERROR,"you not enter all details");
            alert.show();
            return;
        }

        List<Pair<String ,Pair<Integer,Integer>>> rotorsIDList = createIDListForRotors(startPositionList);
        String reflectorID = codeSetController.getReflector().getValue();
        List<Pair<Character, Character>> plugBoard = createPlugBoard();
        DTO_CodeDescription res = new DTO_CodeDescription(engine.createMachineInfoDTO().getABC(),rotorsIDList,startPositionList,reflectorID,plugBoard);
        if (searchErrorInitInput(rotorsIDList,reflectorID,plugBoard)) {
            codeSetController.resetPlugBoard();
            return;
        }
        engine.buildRotorsStack(res, true);
        isCodeChosen.set(false);
        isCodeChosen.set(true);
        sp_mainPage.setContent(rootNode);

        //update Code Configuration in all sub-components
        dto_codeDescription = engine.createCodeDescriptionDTO();
        String codeConfigurationText = createDescriptionFormat(dto_codeDescription);
        encryptDecryptController.getTa_codeConfiguration().setText(codeConfigurationText);
        tf_machineConfiguration.setText(codeConfigurationText);
    }

    private boolean searchErrorInitInput(List<Pair<String, Pair<Integer, Integer>>> rotorsIDList,
                                         String reflectorID, List<Pair<Character, Character>> plugBoard) {
        if(!checkRotorsIDList(rotorsIDList))
            return true;
        if(reflectorID == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR,"you not enter reflector ID");
            alert.show();
            return true;
        }
        if(!checkPlugBoardList(plugBoard))
        {
            Alert alert = new Alert(Alert.AlertType.ERROR,"Please dont repeat chars!");
            alert.show();
            return true;
        }

        return false;

    }
    private boolean checkPlugBoardList(List<Pair<Character, Character>> plugBoard)
    {
        Set <Character> set = new HashSet<>();
        for(Pair<Character,Character> pair : plugBoard){
            if(pair.getKey().equals(pair.getValue()))
                return false;
            if(set.contains(pair.getKey()))
                return false;
            set.add(pair.getKey());
            if( set.contains(pair.getValue()))
                return false;
            set.add(pair.getValue());
        }
        return true;
    }
    private boolean checkRotorsIDList(List<Pair<String, Pair<Integer, Integer>>> rotorsIDList) {
        Set<String> set = new HashSet<>();
        if(rotorsIDList.size() < engine.getMachine().getRotorsInUseCount())
        {
            Alert alert = new Alert(Alert.AlertType.ERROR,"Error - You should select exactly " + dto_machineInfo.getNumOfUsedRotors()
                    + " Rotors!");
            alert.show();
            return false;
        }
        for (Pair<String, Pair<Integer, Integer>> id : rotorsIDList) {
            if (set.contains(id.getKey())) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "you enter duplicate rotor");
                alert.show();
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
        for(Pair<ChoiceBox<Character>, ChoiceBox<Character>> pair :codeSetController.getPlugBoardList()) {
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
        curDM.getPoolMission().shutdownNow();
        curDM.getPoolResult().shutdownNow();
        allTaskSize = new SimpleIntegerProperty(0);

        bruteForceThread.interrupt();
    }

    public void startBruteForce() {
        Consumer<DTO_ConsumerPrinter> MsgConsumer = getMsgConsumer();
        Consumer<Integer> showNumberOfTasks = getNumberOfTaskConsumer();
        setAllTaskSize(bruteForceController.getDifficulty());
        bruteForceController.getLb_missionCounter().setText(String.valueOf(allTaskSize.get()));
        Consumer<Integer> checkFinishConsumer =  getCheckFinishConsumer(allTaskSize.get());
        DecryptionManager DM = new DecryptionManager(engine.clone(),checkFinishConsumer, bruteForceController.getTf_output().getText(),
                bruteForceController.getNumOfAgents(), bruteForceController.getDifficulty(),
                bruteForceController.getTaskSize(),
                MsgConsumer,showNumberOfTasks,
                bruteForceController.getIsDMWorking(),allTaskSize.get());
        curDM = DM;
        bruteForceThread = new Thread(DM);
        bruteForceThread.start();

    }

    private Consumer<Integer> getNumberOfTaskConsumer() {
        return cf -> {
            double res = (double) cf / allTaskSize.getValue();
                bruteForceController.getPb_progress().setProgress(res);
                //System.out.println(res + "\n");
        };
    }
    private Consumer<Integer> getCheckFinishConsumer(Integer numOfAllTasks) {

        return cf -> {
            if(cf >= numOfAllTasks){
                String s = new String();
                long res =  allTaskSize.get() / bruteForceController.getTaskSize();
                long average = curDM.getTimeOfDMOperation().get() / res;
                DecimalFormat df = new DecimalFormat("#,###");
                s +=  "The average time is - " + df.format(average) + " nano-seconds\n";
                s += "The total time is - " + df.format(curDM.getTimeOfDMOperation().get()) + " nano-seconds";
                //Alert alert = new Alert(Alert.AlertType.INFORMATION,s);
                //alert.show();
                JOptionPane.showMessageDialog(null, "FINISH! \n" + s, "DM finished", JOptionPane.INFORMATION_MESSAGE);
                bruteForceController.getIsDMWorking().set(false);
                curDM.getPoolResult().shutdown();
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
        int impossible = hard * factorial(machine.getRotorsMapSize()) / (factorial(machine.getRotorsInUseCount()) * factorial(machine.getRotorsMapSize() - machine.getRotorsInUseCount())); // TODO: FIX !!!!!!!!!!1

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

    public void resetBtnClickBruteForce() {
        engine.getMachine().initializePositionsForRotorsInStack();
        DTO_CodeDescription tmpDTO = engine.createCodeDescriptionDTO();
        tmpDTO.resetPlugBoard();
        bruteForceController.getTa_codeConfiguration().setText(createDescriptionFormat(tmpDTO));
    }
    public void resetBtnClickEncryptDecrypt() {
        engine.getMachine().initializePositionsForRotorsInStack();
        encryptDecryptController.getTa_codeConfiguration().setText(createDescriptionFormat(engine.createCodeDescriptionDTO()));
    }

    public int factorial (int x) {
        int res = 1;

        for (int i = 1; i <= x; i++)
            res *= i;
        return res;
    }

    public IntegerProperty getAllTaskSize() { return allTaskSize; }
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void pauseBruteForce() {
        curDM.setIsPause(true);
    }
    public void saveSettings(){

    }

    public void resumeBruteForce() {
        curDM.resumeBruteForce();
    }

    public void encryptDecryptController_doneBtnClick() {
        engine.getUsageHistory().addMsgAndTimeToCurrentCodeSegment(
                encryptDecryptController.getTf_input().getText(),
                encryptDecryptController.getTf_output().getText(),
                100);
        encryptDecryptController.getTa_statistics().setText(showHistoryAndStatistics());
        encryptDecryptController.getTf_input().setText("");
        encryptDecryptController.getTf_output().setText("");

    }

    public void codeSetController_cancelBtnClick() {
        sp_mainPage.setContent(rootNode);
    }
}
