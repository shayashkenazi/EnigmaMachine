import BruteForce.BruteForceController;
import CodeSet.CodeSetController;
import EncryptDecrypt.EncryptDecryptController;
import MainApp.AppController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        // CodeSet Controller
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("/CodeSet/SetCode.fxml");
        fxmlLoader.setLocation(url);
        ScrollPane codeSetComponent = fxmlLoader.load(url.openStream());
        CodeSetController codeSetController = fxmlLoader.getController();

        // EncryptDecrypt Controller
        fxmlLoader = new FXMLLoader();
        url = getClass().getResource("/EncryptDecrypt/EncryptDecrypt.fxml");
        fxmlLoader.setLocation(url);
        ScrollPane encryptDecryptComponent = fxmlLoader.load(url.openStream());
        EncryptDecryptController encryptDecryptController = fxmlLoader.getController();

        // BruteForce Controller
        fxmlLoader = new FXMLLoader();
        url = getClass().getResource("/BruteForce/BruteForce.fxml");
        fxmlLoader.setLocation(url);
        ScrollPane bruteForceComponent = fxmlLoader.load(url.openStream());
        BruteForceController bruteForceController = fxmlLoader.getController();

        // MainApp Controller
        fxmlLoader = new FXMLLoader();
        url = getClass().getResource("/MainApp/MainForm.fxml");
        fxmlLoader.setLocation(url);
        ScrollPane root = fxmlLoader.load(url.openStream());
        AppController appController = fxmlLoader.getController();
        appController.setPrimaryStage(primaryStage);

        // Size Bindings

        appController.setCodeSetController(codeSetController);
        appController.setEncryptDecryptController(encryptDecryptController);
        appController.setBruteForceController(bruteForceController);


        appController.setTab_EncryptDecrypt(encryptDecryptComponent);
        appController.setTab_BruteForce(bruteForceComponent);


        // Start Scene
        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Enigma Machine");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
