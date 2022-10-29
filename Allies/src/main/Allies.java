package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;
import login.LoginController;

import java.net.URL;

public class Allies  extends Application {


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
        url = getClass().getResource("/main/AlliesMainController.fxml");
        fxmlLoader.setLocation(url);
        Parent root = fxmlLoader.load(url.openStream());
        AlliesMainController alliesMainController = fxmlLoader.getController();

        // Bindings
        alliesMainController.setLoginController(loginController);
        alliesMainController.setContentScene();
        Scene scene = new Scene(root, 850, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Allies Application");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
