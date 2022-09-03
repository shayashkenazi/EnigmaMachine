package CodeSet;

import DTOs.DTO_MachineInfo;
import MainApp.AppController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.ResourceBundle;

public class CodeSetController implements Initializable {

    private AppController appController;

    @FXML private VBox vb_mainSetCode;
    @FXML private VBox vb_rotors;
    @FXML private VBox vb_plugBoard;
    @FXML private Button btn_cancel;
    @FXML private Button btn_set;
    @FXML private ScrollPane sp_rotors;

    @FXML void setBtnClick(ActionEvent event) {
        appController.codeSetController_setBtnClick();
    }


    public void createSetCodeController(DTO_MachineInfo dto_machineInfo) {

        vb_rotors.setPrefWidth(sp_rotors.getPrefWidth());

        for(int i = 0; i < dto_machineInfo.getNumOfUsedRotors(); i++){
            HBox curHBox = new HBox(40);
            curHBox.setAlignment(Pos.CENTER);
            int rotorNum = i + 1;
            Label label = new Label("Rotor number " + rotorNum);
            ChoiceBox<String> choiceRotor = new ChoiceBox<>();

            // Choose rotors
            choiceRotor.setItems(getIntRange(dto_machineInfo.getNumOfUsedRotors()));
            
            // Choose Starting Point
            ChoiceBox<Character> choiceStartingPoint = new ChoiceBox<>();
            curHBox.getChildren().add(label);
            curHBox.getChildren().add(choiceRotor);
            curHBox.getChildren().add(choiceStartingPoint);
            vb_rotors.getChildren().add(curHBox);
        }

    }
    private ObservableList<String> getIntRange(int numOfRotors) {

        List<String> res1 = new ArrayList<>();
        for (int i = 1; i <= numOfRotors; i++)
            res1.add(String.valueOf(i));

        ObservableList<String> res2 = FXCollections.observableArrayList(res1);
        return res2;
    }

    public void setMainController(AppController mainController) {
        this.appController = mainController;
    }
    public VBox getCodeSetVbox(){return vb_mainSetCode;}

    @Override
    public void initialize(URL location, ResourceBundle resources) {




    }

}