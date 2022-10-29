package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;
import login.LoginController;

import java.net.URL;

public class Agent extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        // Login Controller
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("/login/LoginController.fxml");
        fxmlLoader.setLocation(url);
        ScrollPane loginComponent = fxmlLoader.load(url.openStream());
        LoginController loginController = fxmlLoader.getController();


        // UBoat Main Controller
        fxmlLoader = new FXMLLoader();
        url = getClass().getResource("/main/AgentMainController.fxml");
        fxmlLoader.setLocation(url);
        Parent root = fxmlLoader.load(url.openStream());
        AgentMainController agentMainController = fxmlLoader.getController();

        // Bindings
        agentMainController.setLoginController(loginController);
        agentMainController.setContentScene();
        Scene scene = new Scene(root, 850, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Agent Application");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
