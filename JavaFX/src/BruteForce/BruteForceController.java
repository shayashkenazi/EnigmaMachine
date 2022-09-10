package BruteForce;

import DataStructures.Trie;
import Interfaces.SubController;
import MainApp.AppController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.InputMethodEvent;

public class BruteForceController implements SubController {

    private Trie trie = new Trie();
    private AppController appController;


    @FXML private Button btn_clear, btn_proccess, btn_reset;

    @FXML private TextField tf_codeConfiguration, tf_input, tf_output, tf_searchBar;


    @FXML void clearBtnClick(ActionEvent event) {

    }

    @FXML void proccessBtnClick(ActionEvent event) {

    }

    @FXML void resetBtnClick(ActionEvent event) {

    }

    @FXML void searchBarTextChanged(InputMethodEvent event) {

    }

    @Override
    public void setMainController(AppController mainController) {
        appController = mainController;
    }

    void initializeTabAfterCodeConfiguration() {

        appController.initializeTrieWithDictionary();

    }


    public Trie getTrie() { return trie; }
}
