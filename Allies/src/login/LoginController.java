package login;

import com.google.gson.reflect.TypeToken;
import http.HttpClientUtil;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Pair;
import main.AlliesMainController;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import utils.Constants;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Set;

import static utils.Constants.DTO_UBOATS;
import static utils.Constants.GSON_INSTANCE;

public class LoginController {

    @FXML private Button btn_login;
    @FXML private TextField tf_userName;
    @FXML private ScrollPane sp_loginPage;
    private AlliesMainController alliesMainController;

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
                .addQueryParameter("username", userName)
                .addQueryParameter(Constants.CLASS_TYPE,"allies")
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
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.ERROR,responseBody);
                        alert.show();
                    });
                } else {
                    Platform.runLater(() -> {
                        alliesMainController.setUserName(userName);
                        alliesMainController.switchToMainPanel();
                        setBattlefieldToCB();
                    });
                    alliesMainController.refresherTeamsAgentDetails();
                    alliesMainController.refresherContestsDataDetails();
                }
            }
        });




    }

    private void setBattlefieldToCB(){
        //noinspection ConstantConditions
        String finalUrl = HttpUrl
                .parse(Constants.DTO)
                .newBuilder()
                .addQueryParameter(Constants.USERNAME, tf_userName.getText())
                .addQueryParameter(Constants.DTO_TYPE,Constants.DTO_UBOATS)
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                }
                else {
                    String json_uboatBattlefield = response.body().string();
                    Type uBoatsBattlefieldType = new TypeToken< Set<Pair<String,String>> >() { }.getType();
                    Set<Pair<String,String>> uBoatsBattlefieldSet = GSON_INSTANCE.fromJson(json_uboatBattlefield, uBoatsBattlefieldType);
                    alliesMainController.initializeCB(uBoatsBattlefieldSet);
                }
            }
        });
    }
    public ScrollPane getLoginPage() {
        return sp_loginPage;
    }

    public void setMainController(AlliesMainController alliesMainController) {
        this.alliesMainController = alliesMainController;
    }
}
