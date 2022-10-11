package main;

import com.google.gson.reflect.TypeToken;
import http.HttpClientUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import login.LoginController;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import utils.Constants;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Set;

import static utils.Constants.GSON_INSTANCE;

public class AgentMainController {

    private LoginController loginComponentController;
    private Node rootNode;
    private final StringProperty userName;

    @FXML private ScrollPane sp_mainPage;

    @FXML void initialize() {
        rootNode = sp_mainPage.getContent();
        showAllies();
    }
    public AgentMainController() {
        userName = new SimpleStringProperty("Anonymous");
    }

    public void setLoginController(LoginController loginController) {
        this.loginComponentController = loginController;
        loginController.setMainController(this);
    }
    private void showAllies(){
        String finalUrl = HttpUrl
                .parse(Constants.DTO)
                .newBuilder()
                .addQueryParameter(WebConstants.Constants.DTO_TYPE, Constants.DTO_ALLIES)
                .addQueryParameter(Constants.USERNAME, userName.getValue())
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("Ohhhhh NOOOOOOOOOOOO !!!!!\n\n\n\n\n\n\nNOOOOOOO");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String json_dictionary = response.body().string();
                Type AlliesType = new TypeToken<Set<String>>() { }.getType();
                Set<String> alliesUsers = GSON_INSTANCE.fromJson(json_dictionary, AlliesType);

                for (String word : alliesUsers) {
                   loginComponentController.getCb_allies().getItems().add(word);
                }
            }
        });
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
