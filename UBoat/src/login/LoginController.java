package login;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML private Button btn_login;
    @FXML private TextField tf_userName;

    @FXML void loginBtnClick(ActionEvent event) {
        uBoatMainController.func(tf_userName.getText());
    }

}
