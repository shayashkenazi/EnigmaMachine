package BruteForce;

import DataStructures.Trie;
import DecryptionManager.Difficulty;
import Interfaces.SubController;
import MainApp.AppController;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.MouseEvent;

import javax.naming.Binding;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class BruteForceController implements SubController, Initializable {

    private Trie trie = new Trie();
    private AppController appController;
    private Map<String, Button> dictionaryMap = new LinkedHashMap<>();
    BooleanProperty isTaskSizeSelected, isDifficultySelected, isDMWorking;

    @FXML private Button btn_clear, btn_proccess, btn_reset, btn_start, btn_pause, btn_stop;
    @FXML private TextField tf_codeConfiguration, tf_input, tf_output, tf_searchBar, tf_taskSize;
    @FXML private ListView<String> lv_dictionary;
    @FXML private ComboBox<Difficulty> cb_level;
    @FXML private Slider s_agents;
    @FXML private TextArea ta_candidates;
    @FXML private ProgressBar pb_progress;


    @FXML void clearBtnClick(ActionEvent event) {
        tf_input.setText("");
        tf_output.setText("");
    }

    @FXML void proccessBtnClick(ActionEvent event) {
        appController.BruteForceController_proccessBtnClick();
    }

    @FXML void resetBtnClick(ActionEvent event) {
        appController.resetBtnClick();
    }

    @FXML void searchBarTextChanged(InputMethodEvent event) { // TODO: Delete - doesn't work

        lv_dictionary.getItems().clear();
        List<String> allChildren = trie.returnAllChildren(tf_searchBar.getText());

        for (String childWord : allChildren) {
            lv_dictionary.getItems().add(childWord);
        }
    }


    @FXML void startBtnClick(ActionEvent event) {
        ta_candidates.clear();
        appController.startBruteForce();
        isDMWorking.set(true);
    }
    @FXML
    void stopBtnClick(ActionEvent event) {
        resetSetting();
        appController.stopBruteForce();
    }

    @Override
    public void setMainController(AppController mainController) {
        appController = mainController;
    }

    public void initializeTabAfterCodeConfiguration() {

        appController.initializeTrieWithDictionary();

    }
    private void resetSetting(){
        pb_progress.setProgress(0);
        ta_candidates.clear();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) { // TODO: use Scene Builder

        isDifficultySelected = new SimpleBooleanProperty(false);
        isTaskSizeSelected = new SimpleBooleanProperty(false);
        isDMWorking = new SimpleBooleanProperty(false);

        btn_start.disableProperty().bind(isTaskSizeSelected.not().or(isDifficultySelected.not()));

        tf_searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            lv_dictionary.getItems().clear();

            List<String> allChildren = trie.returnAllChildren(newValue);

            for (String childWord : allChildren) {
                lv_dictionary.getItems().add(childWord);
            }
        });

        // Input
        tf_input.textProperty().addListener((observable, oldValue, newValue) -> {
            tf_input.setText(newValue.toUpperCase());
        });

        lv_dictionary.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent click) {

                if (click.getClickCount() == 2) {
                    String itemString = lv_dictionary.getSelectionModel().getSelectedItem();
                    tf_input.appendText(itemString);
                }
            }
        });

        // Initialize Slider
        s_agents.valueProperty().addListener((observable, oldValue, newValue) -> {
            s_agents.setValue(newValue.intValue());
        });

        // Initialize difficulty ComboBox
        cb_level.getItems().add(Difficulty.EASY);
        cb_level.getItems().add(Difficulty.MEDIUM);
        cb_level.getItems().add(Difficulty.HARD);
        cb_level.getItems().add(Difficulty.IMPOSSIBLE);

        cb_level.valueProperty().addListener(observable -> {
            isDifficultySelected.set(cb_level.getValue() != null);
        });

        // Always keep Task Size Text-Field valid
        tf_taskSize.textProperty().addListener((observable, oldValue, newValue) -> {

            if (newValue.equals("")) {
                isTaskSizeSelected.set(false);
                return;
            }

            if (!newValue.matches("^[0-9]*[1-9][0-9]*$")) // Invalid number
                tf_taskSize.setText(oldValue);
            else
                isTaskSizeSelected.set(true);
        });
        /*isDMWorking.addListener((observable, oldValue, newValue) -> {

            if (newValue) {
               appController.stopBruteForce();
            }

        });*/
        /*numberOfTasksDone.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                pb_progress.progressProperty().bind(Bindings.divide(appController.getNumberOfTasksDone(),
                        appController.getAllTaskSize()));
            }
        });*/

    }

    public Trie getTrie() { return trie; }
    public ListView<String> getLv_dictionary() { return lv_dictionary; }
    public Map<String, Button> getDictionaryMap() { return dictionaryMap; }
    public Slider getS_agents() { return s_agents; }

    public TextField getTf_input() {
        return tf_input;
    }

    public TextField getTf_output() {
        return tf_output;
    }

    public TextField getTa_codeConfiguration() {
        return tf_codeConfiguration;
    }

    public TextArea getTa_candidates() {
        return ta_candidates;
    }
    public int getNumOfAgents(){
        return (int)s_agents.getValue();
    }
    public Difficulty getDifficulty(){
        return cb_level.getValue();
    }
    public int getTaskSize() {
        return Integer.parseInt(tf_taskSize.getText());
    }

    public ProgressBar getPb_progress() { return pb_progress; }

    public BooleanProperty getIsDMWorking() { return isDMWorking; }
}
