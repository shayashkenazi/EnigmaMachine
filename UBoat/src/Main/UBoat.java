package Main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;
import login.LoginController;
import setCode.SetCodeController;

import java.net.URL;


public class UBoat extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {

        // Login Controller
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("/login/LoginController.fxml");
        fxmlLoader.setLocation(url);
        ScrollPane loginComponent = fxmlLoader.load(url.openStream());
        LoginController loginController = fxmlLoader.getController();

        // SetCode Controller
        fxmlLoader = new FXMLLoader();
        url = getClass().getResource("/setCode/SetCodeController.fxml");
        fxmlLoader.setLocation(url);
        ScrollPane setCodeComponent = fxmlLoader.load(url.openStream());
        SetCodeController setCodeController = fxmlLoader.getController();


        // UBoat Main Controller
        fxmlLoader = new FXMLLoader();
        url = getClass().getResource("/Main/UBoatMainController.fxml");
        fxmlLoader.setLocation(url);
        Parent root = fxmlLoader.load(url.openStream());
        UBoatMainController uBoatMainController = fxmlLoader.getController();

        // Bindings
        uBoatMainController.setLoginController(loginController);
        uBoatMainController.setSetCodeController(setCodeController);
        uBoatMainController.setContentScene();

        Scene scene = new Scene(root, 850, 600);

        //uBoatMainController.setWithProp(scene.widthProperty(),scene.heightProperty());
        primaryStage.setScene(scene);
        primaryStage.setTitle("UBoat Application");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
