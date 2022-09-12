package BruteForce;

import DataStructures.Trie;
import Interfaces.SubController;
import MainApp.AppController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.layout.FlowPane;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BruteForceController implements SubController {

    private Trie trie = new Trie();
    private AppController appController;
    private Map<String, Button> dictionaryMap = new LinkedHashMap<>();


    @FXML private Button btn_clear, btn_proccess, btn_reset;
    @FXML private TextField tf_codeConfiguration, tf_input, tf_output, tf_searchBar;
    @FXML private FlowPane fp_dictionary;


    @FXML void clearBtnClick(ActionEvent event) {

    }

    @FXML void proccessBtnClick(ActionEvent event) {
        tf_searchBar.setText("Where are you ???");
    }

    @FXML void resetBtnClick(ActionEvent event) {

    }

    @FXML void searchBarTextChanged(InputMethodEvent event) {

        hideAllDictionaryButtons();

        List<String> allChildren = trie.returnAllChildren(tf_searchBar.getText());

        for (String childWord : allChildren) {
            dictionaryMap.get(childWord).setVisible(true);
        }
    }

    private void hideAllDictionaryButtons() {

        for (Map.Entry<String, Button> entry : dictionaryMap.entrySet()) {
            entry.getValue().setVisible(false);
        }
    }

    @Override
    public void setMainController(AppController mainController) {
        appController = mainController;
    }

    public void initializeTabAfterCodeConfiguration() {

        appController.initializeTrieWithDictionary();
        appController.initializeButtonsWithDictionary();

    }


    public Trie getTrie() { return trie; }
    public FlowPane getFp_dictionary() { return fp_dictionary; }

    public Map<String, Button> getDictionaryMap() { return dictionaryMap; }
}
