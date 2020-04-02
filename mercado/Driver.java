package mercado;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.ArrayList;
import java.util.ResourceBundle;

public class Driver extends Application {
    public static ArrayList<StackPane> roots = new ArrayList<>();
    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.initStyle(StageStyle.UNDECORATED);
        ResourceBundle bundle = ResourceBundle.getBundle("mercado.resources.config");
        Parent root = FXMLLoader.load(getClass().getResource("fxml/StartScene.fxml"),bundle);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

    }
    public static void main(String[] args) {
        launch(args);
    }
}
