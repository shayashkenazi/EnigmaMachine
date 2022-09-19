package BruteForce;

import DataStructures.Trie;
import DecryptionManager.DecryptionManager;
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
import java.util.*;

public class BruteForceController implements SubController, Initializable {

    private Trie trie = new Trie();
    private AppController appController;
    private Map<String, Button> dictionaryMap = new LinkedHashMap<>();
    BooleanProperty isTaskSizeSelected, isDifficultySelected, isDMWorking, isInputSelected, isPaused;

    @FXML private Button btn_clear, btn_proccess, btn_reset, btn_start, btn_pause, btn_stop,btn_resume;
    @FXML private TextField tf_codeConfiguration, tf_input, tf_output, tf_searchBar, tf_taskSize;
    @FXML private ListView<String> lv_dictionary;
    @FXML private ComboBox<Difficulty> cb_level;
    @FXML private Slider s_agents;
    @FXML private TextArea ta_candidates;
    @FXML private ProgressBar pb_progress;
    @FXML
    private Label lb_counterMission;

    @FXML void clearBtnClick(ActionEvent event) {
        tf_input.setText("");
        tf_output.setText("");
    }

    @FXML void proccessBtnClick(ActionEvent event) {
        appController.BruteForceController_proccessBtnClick();
    }

    @FXML void resetBtnClick(ActionEvent event) {
        appController.resetBtnClickBruteForce();
    }

    @FXML void startBtnClick(ActionEvent event) {
        ta_candidates.clear();
        appController.startBruteForce();
        isDMWorking.set(true);
    }
    @FXML
    void stopBtnClick(ActionEvent event) {
        resetSetting();
        isPaused.set(false);
        isDMWorking.set(false);
        appController.stopBruteForce();
    }
    @FXML
    void pauseBtnClick(ActionEvent event) {
        isPaused.set(true);
        isDMWorking.set(false);
        appController.pauseBruteForce();
    }
    @FXML
    void resumeBtnClick(ActionEvent event) {
        isDMWorking.set(true);
        isPaused.set(false);
        appController.resumeBruteForce();
    }

    @Override
    public void setMainController(AppController mainController) {
        appController = mainController;
    }

    public void initializeTabAfterCodeConfiguration() {

        appController.initializeTrieWithDictionary();

    }
    private void resetSetting(){
        pb_progress.setProgress(0); // TODO: fix
        ta_candidates.clear();
        lb_counterMission.setText("");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        isDifficultySelected = new SimpleBooleanProperty(false);
        isTaskSizeSelected = new SimpleBooleanProperty(false);
        isDMWorking = new SimpleBooleanProperty(false);
        isInputSelected = new SimpleBooleanProperty(false);
        isPaused = new SimpleBooleanProperty(false);

        tf_output.textProperty().addListener((observable, oldValue, newValue) -> {
            isInputSelected.setValue(!Objects.equals(newValue, ""));
        });

        tf_searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            tf_searchBar.setText(newValue.toUpperCase());
            lv_dictionary.getItems().clear();

            List<String> allChildren = trie.returnAllChildren(tf_searchBar.getText());

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
        isDMWorking.addListener((observable, oldValue, newValue) -> {

            if (!newValue && !isPaused.getValue()) {
               appController.stopBruteForce();
            }
        });

        initializeDMButtons();
    }

    private void initializeDMButtons(){

        btn_start.disableProperty().bind(isTaskSizeSelected.not()
                .or(isDifficultySelected.not()
                        .or(isInputSelected.not()
                                .or(isDMWorking.not().and(isPaused)).or(isDMWorking))));

        btn_resume.disableProperty().bind(isPaused.not());
        btn_pause.disableProperty().bind(isDMWorking.not());
        btn_stop.disableProperty().bind(isDMWorking.not()); // TODO: add stop when paused
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
    public Label getLb_missionCounter(){
        return lb_counterMission;
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
    public Label getLb_counterMission(){ return lb_counterMission;}
}
