package mercado.graficos;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import mercado.controllers.Configuracoes;

public class Alerta extends StackPane {
    private Text mensagem;
    private Rectangle frame, fundo;
    private Button botao;
    private VBox vBox;
    private int insets = 25;

    public Alerta(double width, double height, String mensagem) {
        this.mensagem = new Text(mensagem);
        constructorDefaults(width, height);
        styleRect();
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
    private void defineButton() {
        botao = new Button();
        botao.setId("botao");
        botao.setText("OK");
        botao.setMaxWidth((frame.getWidth() / 100) * 60);
        botao.setMaxHeight((frame.getWidth() / 100) * 30);
        botao.setOnAction(e -> {
            if (this.getParent() instanceof Pane) {
                ((Pane) this.getParent()).getChildren().remove(this);
            }
        });
    }

    private void constructorDefaults(double width, double height) {
        fundo = new Rectangle(width + insets, height + insets);
        frame = new Rectangle(width, height);
        defineButton();
        vBox = new VBox();
        vBox.getChildren().addAll(this.mensagem, botao);
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing((frame.getWidth() / 100) * 5);
        this.getChildren().addAll(fundo, frame, vBox);
        this.setMaxHeight(height + insets);
        this.setMaxWidth(width + insets);
        setAlignment(vBox, Pos.CENTER);
        this.mensagem.setId("texto");
    }
}
