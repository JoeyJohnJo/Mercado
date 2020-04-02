package mercado.controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import mercado.Driver;


public class AlertaController {

    @FXML
    StackPane rootAlertas;
    @FXML
    VBox vbox;
    @FXML
    private void initialize() {
        Driver.roots.add(rootAlertas);
        rootAlertas.getStylesheets().add(Configuracoes.temaSelecionado + "telaAlertas.css");
        Alertas alertas = new Alertas();
        for (HBox hb : alertas.getAlertas()) {
            vbox.getChildren().add(hb);
        }
    }
}
