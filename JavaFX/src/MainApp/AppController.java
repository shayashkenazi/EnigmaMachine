package MainApp;

import CodeSet.CodeSetController;
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

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

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
        /*if (!isXmlLoaded)
            System.out.println("Error - In order to select this option, you should load xml file first!");
        else
            //createRandomMachineSetting();*/
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

    public void codeSetController_setBtnClick() {
        sp_mainPage.setContent(rootNode);
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
