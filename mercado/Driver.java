package mercado;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import mercado.controllers.Configuracoes;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

public class Driver extends Application {
    public static ArrayList<StackPane> roots = new ArrayList<>();
    String bundleLocation = System.getProperty("user.home") + "\\Mercado\\";

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.initStyle(StageStyle.UNDECORATED);

        File file = new File(bundleLocation);
        URL[] urls = {file.toURI().toURL()};
        ClassLoader clLoader = new URLClassLoader(urls);
        ResourceBundle rb = ResourceBundle.getBundle("config", Locale.getDefault(), clLoader);
        FXMLLoader loader = new FXMLLoader();
        loader.setResources(rb);
        Configuracoes.loadProps();
        Parent root = loader.load(getClass().getResourceAsStream("fxml/StartScene.fxml"));
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

    }
    public static void main(String[] args) {
        launch(args);
    }
}
