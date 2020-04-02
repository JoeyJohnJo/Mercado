package mercado.controllers;

import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import mercado.Driver;
import mercado.graficos.Alerta;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CadastrarProduto {

    @FXML
    VBox base;
    @FXML
    TextField campoNome;
    @FXML
    TextField campoCodigo, campoPreco, campoPrecoCompra;
    @FXML
    StackPane rootCP;

    @FXML
    private void initialize() {
        Driver.roots.add(rootCP);
        rootCP.getStylesheets().add(Configuracoes.temaSelecionado + "cadastrarProduto.css");
        Platform.runLater(() -> campoNome.requestFocus());
        campoNome.setPromptText("Nome");
        campoCodigo.setPromptText("Codigo");
        campoPreco.setPromptText("Preco de Venda");
        campoPrecoCompra.setPromptText("Preco de Compra");
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
        campoCodigo.focusedProperty().addListener((ov, oldV, newV) -> {
            if (newV) {
                campoCodigo.setPromptText("");
                ScaleTransition st = new ScaleTransition();
                st.setToX(1.5);
                st.setDuration(Duration.seconds(0.25));
                st.setNode(campoCodigo);
                st.play();
            }
            if (!newV) {
                ScaleTransition st = new ScaleTransition();
                st.setToX(1.0);
                st.setToY(1.0);
                st.setDuration(Duration.seconds(0.25));
                st.setNode(campoCodigo);
                st.play();
                campoCodigo.setPromptText("Codigo");
            }

        });
        campoPreco.textProperty().addListener((observableValue, s, t1) -> StartScene.filtroDeNumero(campoPreco, s, t1));
        campoPrecoCompra.textProperty().addListener((observableValue, s, t1) -> StartScene.filtroDeNumero(campoPrecoCompra, s, t1));
        rootCP.getChildren().addListener((ListChangeListener<Node>) change -> {
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
    private void enviarFormulario() {
        BancoDeDados bd = BancoDeDados.getInstance();
        String nome = campoNome.getText();
        String codigo = campoCodigo.getText();
        String preco = campoPreco.getText();
        String precoCompra = campoPrecoCompra.getText();
        //Nao adicionar se os dois campos estiverem em branco
        if ((codigo.trim().length() == 0) && (nome.trim().length() == 0) && (preco.trim().length() == 0) && (precoCompra.trim().length() == 0)) {
            Alerta alerta = new Alerta(500, 200,
                    "POR FAVOR PREENCHER DADOS DO PRODUTO");
            rootCP.getChildren().add(alerta);
        }
        //Nao adicionar se o nome estiver em branco
        else if (nome.trim().length() == 0) {
            Alerta alerta = new Alerta(500, 200,
                    "POR FAVOR INSERIR NOME DO PRODUTO");
            rootCP.getChildren().add(alerta);
        }
        //Nao adicionar se o codigo estiver em branco
        else if (codigo.trim().length() == 0) {
            Alerta alerta = new Alerta(500, 200,
                    "POR FAVOR INSERIR CODIGO DO PRODUTO");
            rootCP.getChildren().add(alerta);
        }
        else if (preco.trim().length() == 0) {
            Alerta alerta = new Alerta(500, 200,
                    "POR FAVOR INSERIR PRECO DE VENDA DO PRODUTO");
            rootCP.getChildren().add(alerta);
        }
        else if (precoCompra.trim().length() == 0) {
            Alerta alerta = new Alerta(500, 200,
                    "POR FAVOR INSERIR PRECO DE COMPRA DO PRODUTO");
            rootCP.getChildren().add(alerta);
        }
        else {
            //Testar se o produto ja esta inserido no banco de dados
            //usando o codigo de barras, que é a parte especifica de cada produto
            try {
                ResultSet rs = bd.select("SELECT * FROM Produtos WHERE \"Codigo\" = \"" + codigo + "\"");
                if (rs.isBeforeFirst()) {
                    Alerta alerta = new Alerta(500, 200,
                            "PRODUTO JA INSERIDO, NÃO É NECESSÁRIO CADASTRAR");
                    rootCP.getChildren().add(alerta);
                }
                else {
                    //Atualizar o banco de dados com o produto novo
                    bd.atualizar("INSERT INTO Produtos VALUES (\"" + codigo + "\", \"" + nome + "\", \"" + preco + "\", \"" + precoCompra + "\");");
                    // Alerta pro usuario saber que teve sucesso
                    Alerta alerta = new Alerta(500, 200,
                            "PRODUTO CADASTRADO COM SUCESSO");
                    rootCP.getChildren().add(alerta);
                   }
            } catch (SQLException e) { e.printStackTrace(); }
        }
    }

}
