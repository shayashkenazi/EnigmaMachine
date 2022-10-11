package main;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import login.LoginController;

public class AlliesMainController {

    private LoginController loginComponentController;
    private Node rootNode;
    private final StringProperty userName;

    @FXML
    private ScrollPane sp_mainPage;

    @FXML
    private TextArea ta_TeamsAgentsData;

    @FXML
    private TextArea ta_ContestsData;

    @FXML void initialize() {

        rootNode = sp_mainPage.getContent();

    }
    public AlliesMainController() {
        userName = new SimpleStringProperty("Anonymous");
    }

    public void setLoginController(LoginController loginController) {
        this.loginComponentController = loginController;
        loginController.setMainController(this);
    }

    public void setContentScene() {
        sp_mainPage.setContent(loginComponentController.getLoginPage());
    }

    public void setUserName(String userName) {
        this.userName.set(userName);
    }

    public void switchToMainPanel() {
        sp_mainPage.setContent(rootNode);
    }
}
