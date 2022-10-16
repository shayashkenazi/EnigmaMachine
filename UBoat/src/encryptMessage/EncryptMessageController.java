package encryptMessage;

import Main.UBoatMainController;
import dataStructures.Trie;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.util.List;

public class EncryptMessageController {

    private Trie trie = new Trie();
    private UBoatMainController uBoatMainController;

    @FXML private Button btn_clear, btn_process, btn_ready, btn_reset;
    @FXML private ListView<String> lv_dictionary;
    @FXML private TextField tf_codeConfiguration, tf_input, tf_output, tf_searchBar;

    @FXML public void initialize() {

        lv_dictionary.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent click) {

                if (click.getClickCount() == 2) {
                    String itemString = lv_dictionary.getSelectionModel().getSelectedItem();
                    tf_input.appendText(itemString);
                }
            }
        });

        tf_searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            tf_searchBar.setText(newValue.toUpperCase());
            lv_dictionary.getItems().clear();

            List<String> allChildren = trie.returnAllChildren(tf_searchBar.getText());

            for (String childWord : allChildren) {
                lv_dictionary.getItems().add(childWord);
            }
        });
    }

    @FXML void clearBtnClick(ActionEvent event) {
        tf_input.setText("");
        tf_output.setText("");
    }

    @FXML void processBtnClick(ActionEvent event) {
        uBoatMainController.EncryptMessageController_processBtnClick(tf_input.getText());
    }

    @FXML void readyBtnClick(ActionEvent event) {
        uBoatMainController.setIsReady(true);
        uBoatMainController.updateReadyManager();
    }

    @FXML void resetBtnClick(ActionEvent event) {

    }

    public void setMainController(UBoatMainController uBoatMainController) {
        this.uBoatMainController = uBoatMainController;
    }
    public TextField getTf_codeConfiguration() {
        return tf_codeConfiguration;
    }
    public TextField getTf_input(){
        return tf_input;
    }
    public TextField getTf_output(){
        return tf_output;
    }

    public Trie getTrie() { return trie; }
    public ListView<String> getLv_dictionary() { return lv_dictionary; }
}
