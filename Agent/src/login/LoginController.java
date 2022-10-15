package login;

import http.HttpClientUtil;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import main.AgentMainController;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import utils.Constants;

import java.io.IOException;

public class LoginController {

    @FXML private ScrollPane sp_loginPage;
    @FXML private TextField tf_userName;
    @FXML private Slider s_threadsNumber;
    @FXML private TextField tf_numberOfTasks;
    @FXML private ComboBox<String> cb_allies;
    @FXML private Button btn_login;

    private AgentMainController agentMainController;
    BooleanProperty isNumberOfTasksSelected, isAlliesSelected, isUsernameSelected;

    @FXML public void initialize() {

        btn_login.setDisable(true);
        isNumberOfTasksSelected = new SimpleBooleanProperty(false);
        isAlliesSelected = new SimpleBooleanProperty(false);
        isUsernameSelected = new SimpleBooleanProperty(false);

        btn_login.disableProperty().bind(isNumberOfTasksSelected.not().or(isAlliesSelected.not()).or(isUsernameSelected.not()));

        // Makes the slider to show only INT values
        s_threadsNumber.valueProperty().addListener((observable, oldValue, newValue) -> {
            s_threadsNumber.setValue(newValue.intValue());
        });

        // Always keep Task Size Text-Field valid
        tf_numberOfTasks.textProperty().addListener((observable, oldValue, newValue) -> {

            if (newValue.equals("")) {
                isNumberOfTasksSelected.set(false);
                return;
            }

            if (!newValue.matches("^[0-9]*[1-9][0-9]*$")) // Invalid number
                tf_numberOfTasks.setText(oldValue);
            else
                isNumberOfTasksSelected.set(true);
        });

        cb_allies.valueProperty().addListener(observable -> {
            isAlliesSelected.set(cb_allies.getValue() != null);
        });

        tf_userName.textProperty().addListener(observable -> {
            isUsernameSelected.set(! tf_userName.getText().equals(""));
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
                .addQueryParameter(Constants.CLASS_TYPE,"agent")
                .addQueryParameter("ally",cb_allies.getValue())
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
                        agentMainController.setUserName(userName);
                        agentMainController.switchToMainPanel();
                    });
                }
            }
        });
        agentMainController.setDetailsForAgent(userName,cb_allies.getValue(),Integer.parseInt(tf_numberOfTasks.getText()), (int)s_threadsNumber.getValue());
    
    }


    public ScrollPane getLoginPage() {
        return sp_loginPage;
    }

    public void setMainController(AgentMainController agentMainController) {
        this.agentMainController = agentMainController;
    }
    public ComboBox<String> getCb_allies(){
        return cb_allies;
    }
}
