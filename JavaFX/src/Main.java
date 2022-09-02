import CodeSet.CodeSetController;
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

        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("/CodeSet/SetCode.fxml");
        fxmlLoader.setLocation(url);
        ScrollPane codeSetComponent = fxmlLoader.load(url.openStream());
        CodeSetController codeSetController = fxmlLoader.getController();

        fxmlLoader = new FXMLLoader();
        url = getClass().getResource("/MainApp/MainForm.fxml");
        fxmlLoader.setLocation(url);
        VBox root = fxmlLoader.load(url.openStream());

        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
