import DTOs.DTO_CodeDescription;
import DTOs.DTO_MachineInfo;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.util.Pair;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    private EngineCapabilities engine = new EnigmaEngine();
    //private boolean isXmlLoaded = false;
    private boolean isCodeChosen = false;
    private final BooleanProperty isXmlLoaded = new SimpleBooleanProperty(false);


    @FXML
    private Button btn_loadFile;
    @FXML
    private TextField tf_xmlPath;
    @FXML
    private TextArea tf_machineDetails;
    @FXML
    private Button btn_RandomCode;

    @FXML
    private Button btn_SetCode;

    @FXML
    private TextArea tf_machineConfiguration;
    @FXML
    void loadFileBtnClick(ActionEvent event) {

        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter xmlFilter = new FileNameExtensionFilter("xml files (*.xml)", "xml");
        fileChooser.setFileFilter(xmlFilter);

        int returnCode = fileChooser.showOpenDialog(null);

        if (returnCode != JFileChooser.APPROVE_OPTION) {
            JOptionPane.showMessageDialog(null, "Could NOT choose a file!", "???", JOptionPane.ERROR_MESSAGE);
            return;
        }


        try {
            engine.createEnigmaMachineFromXML(fileChooser.getSelectedFile().getAbsolutePath(), true);
            //tf_xmlPath.textProperty().set(fileChooser.getSelectedFile().getAbsolutePath()); // TODO: binding
            isXmlLoaded.set(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Could NOT choose a file!", "???", JOptionPane.ERROR_MESSAGE);
            isXmlLoaded.set(false);
        }

    }
    @FXML
    void randomCodeBtnClick(ActionEvent event) {
        /*if (!isXmlLoaded)
            System.out.println("Error - In order to select this option, you should load xml file first!");
        else
            //createRandomMachineSetting();*/
    }

    @FXML
    void setCodeBtnClick(ActionEvent event) {
        if (!isXmlLoaded.getValue()) {
            JOptionPane.showMessageDialog(null, "you should load xml file first!", "???", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            DTO_MachineInfo dto_machineInfo = engine.createMachineInfoDTO();
            List<Pair<String , Pair<Integer,Integer>>> rotorsIDList = createIDListForRotors(dto_machineInfo);
            List<Character>  startPositionList = createListForStartPosition(dto_machineInfo.getABC(),rotorsIDList.size(),rotorsIDList,dto_machineInfo);
            String reflectorID = createReflectorID(dto_machineInfo.getNumOfReflectors());
            List<Pair<Character, Character>> plugBoard = createPlugBoard(dto_machineInfo.getABC());
            DTO_CodeDescription res = new DTO_CodeDescription(dto_machineInfo.getABC(),rotorsIDList,startPositionList,reflectorID,plugBoard);
            engine.buildRotorsStack(res, true);
            isCodeChosen = true;
        }
        catch(Exception e) {
            System.out.println("Error - " + e.getMessage());
            isCodeChosen = false;
        }
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
