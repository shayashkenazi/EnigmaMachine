import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

public class Controller {

    private EngineCapabilities engine = new EnigmaEngine();
    private boolean isXmlLoaded = false;
    private boolean isCodeChosen = false;

    @FXML private Button btn_loadFile;
    @FXML private TextField tf_xmlPath;

    @FXML void loadFileBtnClick(ActionEvent event) {

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
            tf_xmlPath.textProperty().set(fileChooser.getSelectedFile().getAbsolutePath()); // TODO: binding
            isXmlLoaded = true;
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Could NOT choose a file!", "???", JOptionPane.ERROR_MESSAGE);
            isXmlLoaded = false;
        }

    }

}
