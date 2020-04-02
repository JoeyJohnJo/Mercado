package mercado.graficos;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import mercado.controllers.Configuracoes;

import java.util.ResourceBundle;

public class TextoAlerta extends StackPane {

    private TextField textField = new TextField();
    private String text;
    private Text mensagem;
    private Rectangle frame;
    private Rectangle fundo;
    private Button positivo;
    private Button negativo;
    private VBox vBox;
    private HBox hBox;
    private int insets = 25;
    private int widthPercent = 50;
    private int heightPercent = 30;

    public TextoAlerta(int width, int height, String mensagem) {
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
    public TextField getTextField() { return textField;}

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
    private void defineTextField() {
        textField = new TextField();
        textField.setPromptText(mensagem.getText());
        textField.setAlignment(Pos.CENTER);
        textField.setMinWidth((frame.getWidth() / 100) * 75);
        textField.setMaxHeight((frame.getWidth() / 100) * 30);

    }

    private void definePositivo() {
        positivo = new Button();
        positivo.setId("botao");
        positivo.setText("CONFIRMAR");
        positivo.setMaxWidth((frame.getWidth() / 100) * widthPercent);
        positivo.setMaxHeight((frame.getWidth() / 100) * heightPercent);
        positivo.setOnAction(e -> {
            if (this.getParent() instanceof Pane) {
                if (textField.getText().trim().length() > 0) {
                    text = textField.getText();
                    ((Pane) this.getParent()).getChildren().remove(this);
                }
            }
        });
    }
    private void defineNegativo() {
        negativo = new Button();
        negativo.setId("botao");
        negativo.setText("CANCELAR");
        negativo.setMaxWidth((frame.getWidth() / 100) * widthPercent);
        negativo.setMaxHeight((frame.getWidth() / 100) * heightPercent);
        negativo.setOnAction(e -> {
            if (this.getParent() instanceof Pane) {
                ((Pane) this.getParent()).getChildren().remove(this);
            }
        });
    }

    public String getText() {
        return text;
    }

    private void constructorDefaults(double width, double height) {
        fundo = new Rectangle(width + insets, height + insets);
        fundo.setFill(Paint.valueOf("#8844ee"));
        frame = new Rectangle(width, height);
        definePositivo();
        defineNegativo();
        defineTextField();
        hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(20);
        hBox.getChildren().addAll(negativo, positivo);
        vBox = new VBox();
        vBox.getChildren().addAll(this.mensagem, textField, hBox);
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
