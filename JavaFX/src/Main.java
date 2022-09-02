import CodeSet.CodeSetController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.*;
import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("C:\\Users\\idofa\\Desktop\\College\\JAVA\\EnigmaMachineQ1\\JavaFX\\src\\CodeSet\\SetCode.fxml");
        fxmlLoader.setLocation(url);
        ScrollPane codeSetComponent = fxmlLoader.load(url.openStream());
        CodeSetController codeSetController = fxmlLoader.getController();

        fxmlLoader = new FXMLLoader();
        url = getClass().getResource("C:\\Users\\idofa\\Desktop\\College\\JAVA\\EnigmaMachineQ1\\JavaFX\\src\\fxml\\GUI.fxml");
        fxmlLoader.setLocation(url);
        VBox root = fxmlLoader.load(url.openStream());



        //Scene scene = new Scene(load, 600, 400);
        //primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
