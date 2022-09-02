package CodeSet;

import DTOs.DTO_MachineInfo;
import MainApp.AppController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class CodeSetController implements Initializable {

    private AppController appController;

    @FXML private VBox vb_mainSetCode;
    @FXML private VBox vb_rotors;
    @FXML private VBox vb_plugBoard;


    public void createSetCodeController(DTO_MachineInfo dto_machineInfo) {

        for(int i = 0; i < dto_machineInfo.getNumOfUsedRotors(); i++){
            HBox curHBox = new HBox(40);
            curHBox.setAlignment(Pos.CENTER);
            Label label = new Label("Rotor number " + i+1);
            ChoiceBox<Integer> choiceRotor = new ChoiceBox<>();
            ChoiceBox<Character> choiceStartingPoint = new ChoiceBox<>();
            curHBox.getChildren().add(label);
            curHBox.getChildren().add(choiceRotor);
            curHBox.getChildren().add(choiceStartingPoint);
            vb_rotors.getChildren().add(curHBox);
        }

    }
    public void setMainController(AppController mainController) {
        this.appController = mainController;
    }
    public VBox getCodeSetVbox(){return vb_mainSetCode;}

    @Override
    public void initialize(URL location, ResourceBundle resources) {




    }

}