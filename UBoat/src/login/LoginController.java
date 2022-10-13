package login;

import Main.UBoat;
import Main.UBoatMainController;
import http.HttpClientUtil;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import utils.Constants;

import java.io.IOException;

public class LoginController {

    @FXML private Button btn_login;
    @FXML private TextField tf_userName;
    @FXML private ScrollPane sp_loginPage;
    private UBoatMainController uBoatMainController;

    @FXML public void initialize() {

        btn_login.setDisable(true);

        tf_userName.textProperty().addListener((observable, oldValue, newValue) -> {
            btn_login.setDisable(newValue.equals(""));
        });
    }

    @FXML void loginBtnClick(ActionEvent event) {
        String userName = tf_userName.getText();

        if (userName.isEmpty()) {
            //errorMessageProperty.set("User name is empty. You can't login with empty user name");
            return;
        }

        //noinspection ConstantConditions
        String finalUrl = HttpUrl
                .parse(Constants.LOGIN_PAGE)
                .newBuilder()
                .addQueryParameter(Constants.USERNAME, userName)
                .addQueryParameter(Constants.CLASS_TYPE, "uBoat")
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                /*Platform.runLater(() ->
                        errorMessageProperty.set("Something went wrong: " + e.getMessage())
                );*/
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            System.out.println("Something went wrong: " + responseBody)
                    );
                } else {
                    Platform.runLater(() -> {
                        uBoatMainController.setUserName(userName);
                        uBoatMainController.switchToMainPanel();
                    });
                }
            }
        });
    }

    public ScrollPane getLoginPage() {
        return sp_loginPage;
    }

    public void setMainController(UBoatMainController uBoatMainController) {
        this.uBoatMainController = uBoatMainController;
    }

    //public void
}
