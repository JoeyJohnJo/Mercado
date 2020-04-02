package mercado.graficos;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import mercado.controllers.Configuracoes;

import java.util.ResourceBundle;

public class ConfirmaAlerta extends StackPane {

    private Text mensagem;
    private Rectangle frame;
    private Rectangle fundo;
    private Button positivo;
    private Button negativo;
    private VBox vBox;
    private HBox hBox;
    private int insets = 25;

    public ConfirmaAlerta(double width, double height, String mensagem) {
        this.mensagem = new Text(mensagem);
        constructorDefaults(width, height);
        styleRect();
    }

    public Button getPositiveButton() {
        return positivo;
    }
    public Button getNegativeButton() {
        return negativo;
    }

    private void styleRect() {
        frame.setStrokeWidth(8.0);
        if (Configuracoes.temaSelecionado.matches("/mercado/styles/dark/")) {
            frame.setStroke(Paint.valueOf("#8844ee"));
            frame.setFill(Paint.valueOf("#212121"));
            fundo.setFill(Paint.valueOf("#8844ee"));
        }
        else {
            frame.setStroke(Paint.valueOf("#000099"));
            frame.setFill(Paint.valueOf("#999999"));
            fundo.setFill(Paint.valueOf("#990000"));
        }
        this.getStylesheets().add(Configuracoes.temaSelecionado + "alerta.css");
    }

    private void definePositivo() {
        positivo = new Button();
        positivo.setId("botao");
        positivo.setText("SIM");
        positivo.setMaxWidth((frame.getWidth() / 100) * 25);
        positivo.setMaxHeight((frame.getWidth() / 100) * 30);
        positivo.setOnAction(e -> {
            if (this.getParent() instanceof Pane) {
                ((Pane) this.getParent()).getChildren().remove(this);
            }
        });
    }
    private void defineNegativo() {
        negativo = new Button();
        negativo.setId("botao");
        negativo.setText("NAO");
        negativo.setMaxWidth((frame.getWidth() / 100) * 25);
        negativo.setMaxHeight((frame.getWidth() / 100) * 30);
        negativo.setOnAction(e -> {
            if (this.getParent() instanceof Pane) {
                ((Pane) this.getParent()).getChildren().remove(this);
            }
        });
    }

    private void constructorDefaults(double width, double height) {
        fundo = new Rectangle(width + insets, height + insets);
        frame = new Rectangle(width, height);
        definePositivo();
        defineNegativo();
        hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(20);
        hBox.getChildren().addAll(negativo, positivo);
        vBox = new VBox();
        vBox.getChildren().addAll(this.mensagem, hBox);
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing((frame.getWidth() / 100) * 5);
        this.getChildren().addAll(fundo, frame, vBox);
        this.setMaxHeight(height + insets);
        this.setMaxWidth(width + insets);
        setAlignment(vBox, Pos.CENTER);
        this.getStylesheets().add(ResourceBundle.getBundle("mercado.resources.config").getString("Tema") + "alerta.css");
        this.mensagem.setId("texto");

    }
}