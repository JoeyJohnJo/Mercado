package mercado.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import mercado.Driver;

public class FinalizarCompra {
    @FXML StackPane rootFinalizar;
    @FXML Label total, troco;
    @FXML TextField valorRecebido;
    @FXML Button bVoltar;
    @FXML HBox buttons;
    @FXML VBox top;
    static Button bFinalizar = new Button("FINALIZAR");
    static TextField tCliente = new TextField();
    static double precoTotal;

    public static void setTotal(double total) { precoTotal = total;}
    @FXML
    private void initialize() {
        Driver.roots.add(rootFinalizar);
        rootFinalizar.getStylesheets().add(Configuracoes.temaSelecionado + "finalizarCompra.css");

        buttons.getChildren().add(bFinalizar);
        top.getChildren().add(tCliente);
        tCliente.setId("tCliente");

        total.setText(String.valueOf(precoTotal));
        Platform.runLater(() -> valorRecebido.requestFocus());
        valorRecebido.textProperty().addListener((observableValue, s, t1) -> StartScene.filtroDeNumero(valorRecebido, s, t1));
        valorRecebido.setOnKeyPressed(e -> {
            if (valorRecebido.isFocused())
                if (valorRecebido.getText().length() > 0)
                    if (e.getCode() == KeyCode.ENTER) {
                        double valorRec = Double.parseDouble(valorRecebido.getText());
                        double valorTroco = valorRec - precoTotal;
                        troco.setText("R$: " + valorTroco);
                    }
        });
    }

    @FXML
    private void voltar() {
        ((StackPane) rootFinalizar.getParent()).getChildren().remove(rootFinalizar);
    }

}
