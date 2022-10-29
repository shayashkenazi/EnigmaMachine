package setCode;

import DTOs.DTO_MachineInfo;
import Main.UBoatMainController;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SetCodeController {

    @FXML private Button btn_cancel, btn_set;
    @FXML private ChoiceBox<String> cb_reflector;
    @FXML private HBox hb_rotors;
    @FXML private ScrollPane sp_rotors;
    @FXML private VBox vb_mainSetCode;
    @FXML private ScrollPane sp_mainPage;

    private UBoatMainController uBoatMainController;
    private List<Pair<ChoiceBox<String>,ChoiceBox<Character>>> rotorsChoiceBoxes = new ArrayList<>();
    private BooleanProperty isRotorsSelected = new SimpleBooleanProperty(false);
    private BooleanProperty isReflectorSelected = new SimpleBooleanProperty(false);

    @FXML public void initialize() {

        btn_set.disableProperty().bind(isReflectorSelected.not().or(isRotorsSelected.not()));


    }

    @FXML
    void cancelBtnClick(ActionEvent event) {
        uBoatMainController.switchToMainPanel();
    }

    @FXML
    void setBtnClick(ActionEvent event) {

        uBoatMainController.codeSetController_setBtnClick();
        uBoatMainController.switchToMainPanel();
        uBoatMainController.setContestTab();
    }

    public void setMainController(UBoatMainController uBoatMainController) {
        this.uBoatMainController = uBoatMainController;
    }

    public ScrollPane getMainPage() {
        return sp_mainPage;
    }

    public void createSetCodeController(DTO_MachineInfo dto_machineInfo) {


        hb_rotors.setPrefWidth(sp_rotors.getPrefWidth());
        rotorsChoiceBoxes = new ArrayList<>();
        hb_rotors.getChildren().clear();
        cb_reflector.setValue("");
        for(int i = 0; i < dto_machineInfo.getNumOfUsedRotors(); i++){
            HBox curHBox = new HBox(20);
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
        // TODO: doesnt work, after fix - remove the comment from line 41
        for(Pair<ChoiceBox<String>, ChoiceBox<Character>> pair : rotorsChoiceBoxes) {

            pair.getKey().valueProperty().addListener((observable, oldValue, newValue) -> {
                isRotorsSelected.set(isAllRotorsChoose());
            });
            pair.getValue().valueProperty().addListener((observable, oldValue, newValue) -> {
                isRotorsSelected.set(isAllRotorsChoose());
            });
        }
        cb_reflector.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null)
                isReflectorSelected.set(!newValue.equals(""));
        });
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
        MapNumbers.put(4,"IV");
        MapNumbers.put(5,"V");
        List<String> res1 = new ArrayList<>();
        for(int i = 0; i < numOfReflectors; i++) {
            res1.add(MapNumbers.get(i+1));
        }
        ObservableList<String> res2 = FXCollections.observableArrayList(res1);
        return res2;
    }

    private boolean isAllRotorsChoose(){
        for(Pair<ChoiceBox<String>, ChoiceBox<Character>> pair : rotorsChoiceBoxes) {
            if(pair.getValue().getValue() == null || pair.getKey().getValue() == null)
                return false;
        }
        return true;
    }

    public List<Pair<ChoiceBox<String>,ChoiceBox<Character>>> getRotorsChoiceBoxes (){return rotorsChoiceBoxes;}
    public ChoiceBox<String> getReflector(){return cb_reflector;}
}