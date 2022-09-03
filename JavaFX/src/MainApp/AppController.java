package MainApp;

import CodeSet.CodeSetController;
import DTOs.DTO_CodeDescription;
import DTOs.DTO_MachineInfo;
import EnginePackage.EngineCapabilities;
import EnginePackage.EnigmaEngine;
import com.sun.glass.ui.CommonDialogs;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.util.Pair;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class AppController implements Initializable {

    private EngineCapabilities engine = new EnigmaEngine();
    //private boolean isXmlLoaded = false;
    private boolean isCodeChosen = false;
    private final BooleanProperty isXmlLoaded = new SimpleBooleanProperty(false);
    private CodeSetController codeSetController;
    private Node rootNode;

    @FXML private ScrollPane sp_mainPage;
    @FXML private VBox vb_MainApp;
    @FXML private Button btn_loadFile;
    @FXML private TextField tf_xmlPath;
    @FXML private TextArea tf_machineDetails;
    @FXML private HBox hb_setCode;
    @FXML private Button btn_RandomCode;
    @FXML private Button btn_SetCode;
    @FXML private TextArea tf_machineConfiguration;
    @FXML
    void loadFileBtnClick(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml files", "*.xml"));

        File fileSelected = fileChooser.showOpenDialog(null);

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

    }

    private void createRandomMachineSetting() {
        DTO_MachineInfo dto_machineInfo = engine.createMachineInfoDTO();
        List<Pair<String ,Pair<Integer,Integer>>>  rotorsIDList = randomCreateIDListForRotors(dto_machineInfo.getNumOfPossibleRotors(),dto_machineInfo.getNumOfUsedRotors());
        List<Character>  startPositionList = randomCreateListForStartPosition(dto_machineInfo,rotorsIDList,dto_machineInfo.getABC(),rotorsIDList.size());
        String reflectorID = randomCreateReflectorID(dto_machineInfo.getNumOfReflectors());
        List<Pair<Character, Character>> plugBoard = randomCreatePlugBoard(dto_machineInfo.getABC());
        DTO_CodeDescription res = new DTO_CodeDescription(dto_machineInfo.getABC(),rotorsIDList,startPositionList,reflectorID,plugBoard);
        engine.buildRotorsStack(res, true);
        isCodeChosen = true;
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
        List<Pair<String ,Pair<Integer,Integer>>> rotorsIDList = createIDListForRotors(startPositionList);
        String reflectorID = codeSetController.getReflector().getValue();
        List<Pair<Character, Character>> plugBoard = createPlugBoard();
        DTO_CodeDescription res = new DTO_CodeDescription(engine.createMachineInfoDTO().getABC(),rotorsIDList,startPositionList,reflectorID,plugBoard);
        engine.buildRotorsStack(res, true);
        isCodeChosen = true;
        sp_mainPage.setContent(rootNode);
    }

    private List<Pair<Character, Character>> createPlugBoard() {
        DTO_MachineInfo dto_machineInfo = engine.createMachineInfoDTO();
        List<Pair<Character, Character>> plugBoardList = new ArrayList<>();

        for(Pair<ChoiceBox<Character>, ChoiceBox<Character>> pair :codeSetController.getPlugBoardList()) {
            plugBoardList.add(new Pair<>(pair.getKey().getValue(),pair.getValue().getValue()));
        }
        return plugBoardList;
    }

    private List<Pair<String ,Pair<Integer,Integer>>> createIDListForRotors(List<Character> startPositionList)
    {
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        isXmlLoaded.addListener((obs, old, newValue) -> {
            tf_machineDetails.setText(setMachineSettingsTextArea(newValue));
        });

        isXmlLoaded.addListener((obs, old, newValue) -> {
          tf_xmlPath.setText(newValue ? engine.getUsageHistory().getXmlPath() : "");
        });
    }
}
