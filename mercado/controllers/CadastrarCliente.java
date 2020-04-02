package mercado.controllers;

import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import mercado.Driver;
import mercado.graficos.Alerta;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CadastrarCliente {

    private String caminhoDefinirProdutos = "../fxml/DefinirProdutos.fxml";
    private Parent telaDefinirProdutos = null;
    @FXML VBox base;
    @FXML TextField campoNome, campoEndereco, campoTelefone;
    @FXML StackPane rootCC;
    BorderPane parent;

    @FXML
    private void initialize() {
        Driver.roots.add(rootCC);
        rootCC.getStylesheets().add(Configuracoes.temaSelecionado + "cadastrarProduto.css");
        Platform.runLater(() -> campoNome.requestFocus());
        campoNome.setPromptText("Nome");
        campoEndereco.setPromptText("Endereco");
        campoTelefone.setPromptText("Telefone");
        campoNome.focusedProperty().addListener((ov, oldV, newV) -> {
            if (newV) {
                campoNome.setPromptText("");
                ScaleTransition st = new ScaleTransition();
                st.setToX(1.5);
                st.setDuration(Duration.seconds(0.25));
                st.setNode(campoNome);
                st.play();
            }
            if (!newV) {
                ScaleTransition st = new ScaleTransition();
                st.setToX(1.0);
                st.setToY(1.0);
                st.setDuration(Duration.seconds(0.25));
                st.setNode(campoNome);
                st.play();
                campoNome.setPromptText("Nome");
            }

        });
        campoEndereco.focusedProperty().addListener((ov, oldV, newV) -> {
            if (newV) {
                campoEndereco.setPromptText("");
                ScaleTransition st = new ScaleTransition();
                st.setToX(1.5);
                st.setDuration(Duration.seconds(0.25));
                st.setNode(campoEndereco);
                st.play();
            }
            if (!newV) {
                ScaleTransition st = new ScaleTransition();
                st.setToX(1.0);
                st.setToY(1.0);
                st.setDuration(Duration.seconds(0.25));
                st.setNode(campoEndereco);
                st.play();
                campoEndereco.setPromptText("Endereco");
            }

        });
        campoTelefone.focusedProperty().addListener((ov, oldV, newV) -> {
            if (newV) {
                campoTelefone.setPromptText("");
                ScaleTransition st = new ScaleTransition();
                st.setToX(1.5);
                st.setDuration(Duration.seconds(0.25));
                st.setNode(campoTelefone);
                st.play();
            }
            if (!newV) {
                ScaleTransition st = new ScaleTransition();
                st.setToX(1.0);
                st.setToY(1.0);
                st.setDuration(Duration.seconds(0.25));
                st.setNode(campoTelefone);
                st.play();
                campoTelefone.setPromptText("Telefone");
            }

        });
        campoTelefone.textProperty().addListener((observableValue, s, t1) -> {
            if (!t1.matches("\\d{0,15}?")) {
                campoTelefone.setText(s);
            }
        });
        rootCC.getChildren().addListener((ListChangeListener<Node>) change -> {
            int i = 0;
            while (change.next()) {
                if (change.getAddedSubList().size() > 0) {
                    if (change.getAddedSubList().get(i) instanceof Alerta) {
                        base.setDisable(true);
                    } else {
                        base.setDisable(false);
                    }
                    i++;
                }
                else {
                    base.setDisable(false);
                }
            }
        });
    }

    @FXML
    private void carregarTelaDefinirProdutos() {
        if (campoTelefone.getText().trim().length() == 0 ||
                campoNome.getText().trim().length() == 0 ||
                campoEndereco.getText().trim().length() == 0) {
            Alerta a = new Alerta(500,200, "POR FAVOR COMPLETE OS DADOS DO CLIENTE PRIMEIRO");
            rootCC.getChildren().add(a);
        }
        else {
            BancoDeDados bd = BancoDeDados.getInstance();
            try {
                ResultSet rs = bd.select("SELECT * FROM Clientes WHERE \"Nome\" = \"" + campoNome.getText() + "\"");
                if (rs.isBeforeFirst()) {
                    Alerta a = new Alerta(500, 200, "ESTE CLIENTE JA ESTA CADASTRADO");
                    rootCC.getChildren().add(a);
                }
                else {
                    loadFXML(telaDefinirProdutos, caminhoDefinirProdutos);
                    DefinirProdutos.setClienteNome(campoNome.getText());
                    DefinirProdutos.setClienteEndereco(campoEndereco.getText());
                    DefinirProdutos.setClienteTelefone(campoTelefone.getText());
                }
            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void cadastrar() {
        BancoDeDados bd = BancoDeDados.getInstance();
        try {
            ResultSet rs = bd.select("SELECT * FROM Clientes WHERE \"Nome\" = \"" + campoNome.getText() + "\"");
            if (rs.isBeforeFirst()) {
                Alerta a = new Alerta(500, 200, "ESTE CLIENTE JA ESTA CADASTRADO");
                rootCC.getChildren().add(a);
            }
            else {
                bd.insertRowInto("Clientes", campoNome.getText(), campoEndereco.getText(), campoTelefone.getText());
                bd.createTable("Cliente" + campoNome.getText(), "Data", "TEXT", "Horario", "TEXT",
                        "Venda", "TEXT", "Total", "REAL");
                Alerta a = new Alerta(500, 200, "CLIENTE CADASTRADO COM SUCESSO");
                rootCC.getChildren().add(a);
            }
        } catch (SQLException e) { e.printStackTrace(); }

    }

    private void loadFXML(Parent p, String path) throws IOException {
        parent = (BorderPane) rootCC.getParent();
        if (!parent.getChildren().contains(p)) {
            if (p == null)
                p = FXMLLoader.load(getClass().getResource(path));
            else {
                p = null;
                p = FXMLLoader.load(getClass().getResource(path));
            }
            parent.getChildren().clear();
            parent.setCenter(p);
        }
        else { System.out.println("Cena ja inserida");}
    }
}
