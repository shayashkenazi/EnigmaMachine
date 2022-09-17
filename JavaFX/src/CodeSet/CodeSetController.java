package CodeSet;

import DTOs.DTO_MachineInfo;
import Interfaces.SubController;
import MainApp.AppController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Pair;

import java.net.URL;
import java.util.*;

public class CodeSetController implements Initializable, SubController {

    private AppController appController;
    private List<Pair<ChoiceBox<String>,ChoiceBox<Character>>> rotorsChoiceBoxes = new ArrayList<>();

    private List<Pair<ChoiceBox<Character>,ChoiceBox<Character>>> plugBoardList = new ArrayList<>();
    @FXML private VBox vb_mainSetCode;
    @FXML private VBox vb_rotors;
    @FXML private HBox hb_rotors;
    @FXML private VBox vb_plugBoard;
    @FXML private Button btn_cancel;
    @FXML private Button btn_set;
    @FXML private Button btn_addPairToPlugBoard;
    @FXML private ScrollPane sp_rotors;
    @FXML private ChoiceBox<String> cb_reflector;

    @FXML void setBtnClick(ActionEvent event) {

        appController.codeSetController_setBtnClick();

    }
    @FXML void addToPlugBoardBtnClick(ActionEvent event) {
        vb_plugBoard.getChildren().clear();
        plugBoardList = new ArrayList<>();
        HBox curHBox = new HBox(40);
        curHBox.setAlignment(Pos.CENTER);
        DTO_MachineInfo dto_machineInfo = appController.getDtoMachineInfo();
        ChoiceBox<Character> choiceChar1 = new ChoiceBox<>();
        choiceChar1.setItems(getChoicesABC(dto_machineInfo.getABC()));
        curHBox.getChildren().add(choiceChar1);
        ChoiceBox<Character> choiceChar2 = new ChoiceBox<>();
        choiceChar2.setItems(getChoicesABC(dto_machineInfo.getABC()));
        curHBox.getChildren().add(choiceChar2);
        vb_plugBoard.getChildren().add(curHBox);
        plugBoardList.add(new Pair<>(choiceChar1,choiceChar2));

    }
    public List<Pair<ChoiceBox<String>,ChoiceBox<Character>>> getRotorsChoiceBoxes (){return rotorsChoiceBoxes;}
    public List<Pair<ChoiceBox<Character>,ChoiceBox<Character>>> getPlugBoardList (){return plugBoardList;}
    public ChoiceBox<String> getReflector(){return cb_reflector;}
    public void createSetCodeController(DTO_MachineInfo dto_machineInfo) {

        hb_rotors.setPrefWidth(sp_rotors.getPrefWidth());
        rotorsChoiceBoxes = new ArrayList<>();
        hb_rotors.getChildren().clear();
        cb_reflector.setValue("");
        for(int i = 0; i < dto_machineInfo.getNumOfUsedRotors(); i++){
            HBox curHBox = new HBox(40);
            curHBox.setAlignment(Pos.CENTER);
            int rotorNum = i + 1;
            Label label = new Label("Rotor number " + rotorNum);

            ChoiceBox<String> choiceRotor = new ChoiceBox<>();

            // Choose rotors
            choiceRotor.setItems(getIntRange(dto_machineInfo.getNumOfPossibleRotors()));
            // Choose Starting Point
            ChoiceBox<Character> choiceStartingPoint = new ChoiceBox<>();
            choiceStartingPoint.setItems(getChoicesABC(dto_machineInfo.getABC()));
            curHBox.getChildren().add(label);
            curHBox.getChildren().add(choiceRotor);
            curHBox.getChildren().add(choiceStartingPoint);
            hb_rotors.getChildren().add(curHBox);
            //add to list of choice boxes
            rotorsChoiceBoxes.add(new Pair<>(choiceRotor,choiceStartingPoint));
            cb_reflector.setItems(getIdReflectors(dto_machineInfo.getNumOfReflectors()));

        }

    }
    public void createRandomSetCodeController(DTO_MachineInfo dto_machineInfo) {

    }
    private ObservableList<String> getIntRange(int numOfRotors) {

        List<String> res1 = new ArrayList<>();
        for (int i = 1; i <= numOfRotors; i++)
            res1.add(String.valueOf(i));

        ObservableList<String> res2 = FXCollections.observableArrayList(res1);
        return res2;
    }
    private ObservableList<Character> getChoicesABC(String abc) {

        List<Character> res1 = new ArrayList<>();
        for (int i = 0; i < abc.length(); i++)
            res1.add(abc.charAt(i));

        ObservableList<Character> res2 = FXCollections.observableArrayList(res1);
        return res2;
    }
    private ObservableList<String> getIdReflectors(int numOfReflectors) {

        Map<Integer, String> MapNumbers = new LinkedHashMap<>();
        MapNumbers.put(1,"I");
        MapNumbers.put(2,"II");
        MapNumbers.put(3,"III");
        MapNumbers.put(4,"VI");
        MapNumbers.put(5,"V");
        List<String> res1 = new ArrayList<>();
        for(int i = 0; i < numOfReflectors; i++) {
            res1.add(MapNumbers.get(i+1));
        }
        ObservableList<String> res2 = FXCollections.observableArrayList(res1);
        return res2;
    }

    @Override
    public void setMainController(AppController mainController) {
        this.appController = mainController;
    }
    public VBox getCodeSetVbox(){return vb_mainSetCode;}

    @Override
    public void initialize(URL location, ResourceBundle resources) {




    }

}