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
import mercado.graficos.ConfirmaAlerta;
import mercado.graficos.TextoAlerta;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicReference;

public class InserirEstoque {
    private AtomicReference<String> textoPrecoVenda = new AtomicReference<>();
    private AtomicReference<String> textoPrecoCompra = new AtomicReference<>();
    @FXML
    TextField campoNome, campoCodigo, campoQuantidade;
    @FXML
    StackPane rootIE;
    @FXML
    VBox base;
    @FXML
    private void initialize() {
        Driver.roots.add(rootIE);
        rootIE.getStylesheets().add(Configuracoes.temaSelecionado + "inserirEstoque.css");
        Platform.runLater(() -> campoCodigo.requestFocus());
        campoNome.setPromptText("Nome");
        campoCodigo.setPromptText("Codigo");
        campoQuantidade.setPromptText("Quantidade");
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

                if (campoCodigo.getText().trim().length() != 0) {
                    BancoDeDados bd = BancoDeDados.getInstance();
                    try {
                        ResultSet rs = bd.select("SELECT * FROM Produtos WHERE \"Codigo\" = \"" + campoCodigo.getText() + "\"");
                        if (rs.isBeforeFirst()) {
                            campoNome.setText(rs.getString("Nome"));
                        }
                        else {
                            ConfirmaAlerta ca = new ConfirmaAlerta(500,200,
                                    "PECA NAO CADASTRADA, DESEJA INSERIR MESMO ASSIM?");
                            rootIE.getChildren().add(ca);
                            ca.getNegativeButton().setOnAction( e -> {
                                campoQuantidade.clear();
                                campoCodigo.clear();
                                campoNome.clear();
                                rootIE.getChildren().remove(ca);
                            });
                            ca.getPositiveButton().setOnAction( e -> {
                                TextoAlerta ta = new TextoAlerta(500, 200, "PRECO DE VENDA");
                                rootIE.getChildren().remove(ca);
                                rootIE.getChildren().add(ta);
                                ta.getTextField().textProperty().addListener((observableValue, s, t1) -> StartScene.filtroDeNumero(ta.getTextField(), s, t1));
                                ta.getNegativeButton().setOnAction( f -> {
                                    campoQuantidade.clear();
                                    campoCodigo.clear();
                                    campoNome.clear();
                                    rootIE.getChildren().remove(ta);
                                });
                                ta.getPositiveButton().setOnAction( f -> {
                                    if (ta.getTextField().getText().trim().length() > 0) {
                                        TextoAlerta ta2 = new TextoAlerta(500, 200, "PRECO DE COMPRA");
                                        textoPrecoVenda.set(ta.getTextField().getText());
                                        rootIE.getChildren().remove(ta);
                                        rootIE.getChildren().add(ta2);
                                        ta2.getTextField().textProperty().addListener((observableValue, s, t1) -> StartScene.filtroDeNumero(ta2.getTextField(), s, t1));
                                        ta2.getPositiveButton().setOnAction( g -> {
                                            if (ta2.getTextField().getText().trim().length() > 0) {
                                                textoPrecoCompra.set(ta2.getTextField().getText());
                                                rootIE.getChildren().remove(ta2);
                                                campoNome.setDisable(false);
                                            }
                                        });
                                        ta2.getNegativeButton().setOnAction( g -> {
                                            campoQuantidade.clear();
                                            campoCodigo.clear();
                                            campoNome.clear();
                                            rootIE.getChildren().remove(ta2);
                                        });

                                    }
                                });
                            });
                        }

                    }
                    catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }

        });
        campoQuantidade.focusedProperty().addListener((ov, oldV, newV) -> {
            if (newV) {
                campoQuantidade.setPromptText("");
                ScaleTransition st = new ScaleTransition();
                st.setToX(1.5);
                st.setDuration(Duration.seconds(0.25));
                st.setNode(campoQuantidade);
                st.play();
            }
            if (!newV) {
                ScaleTransition st = new ScaleTransition();
                st.setToX(1.0);
                st.setToY(1.0);
                st.setDuration(Duration.seconds(0.25));
                st.setNode(campoQuantidade);
                st.play();
                campoQuantidade.setPromptText("Quantidade");
            }

        });
        campoQuantidade.textProperty().addListener((observableValue, s, t1) -> StartScene.filtroDeNumero(campoQuantidade, s, t1));
        rootIE.getChildren().addListener((ListChangeListener<Node>) change -> {
            int i = 0;
            while (change.next()) {
                if (change.getAddedSubList().size() > 0) {
                    if (change.getAddedSubList().get(i) instanceof Alerta ||
                    change.getAddedSubList().get(i) instanceof ConfirmaAlerta || change.getAddedSubList().get(i) instanceof TextoAlerta) {
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
        campoNome.setDisable(true);
    }

    @FXML
    private void enviarFormulario() {
        BancoDeDados bd = BancoDeDados.getInstance();
        String nome = campoNome.getText();
        String codigo = campoCodigo.getText();
        String quantidade = campoQuantidade.getText();
        //Nao adicionar se os dois campos estiverem em branco
        if ((codigo.trim().length() == 0) && (nome.trim().length() == 0) && (quantidade.trim().length() == 0)) {
            Alerta alerta = new Alerta(500, 200,
                    "POR FAVOR PREENCHER DADOS DO PRODUTO");
            rootIE.getChildren().add(alerta);
        }
        //Nao adicionar se o nome estiver em branco
        else if (nome.trim().length() == 0) {
            Alerta alerta = new Alerta(500, 200,
                    "POR FAVOR INSERIR NOME DO PRODUTO");
            rootIE.getChildren().add(alerta);
        }
        //Nao adicionar se o codigo estiver em branco
        else if (codigo.trim().length() == 0) {
            Alerta alerta = new Alerta(500, 200,
                    "POR FAVOR INSERIR CODIGO DO PRODUTO");
            rootIE.getChildren().add(alerta);
        }
        else if (quantidade.trim().length() == 0) {
            Alerta alerta = new Alerta(500, 200,
                    "POR FAVOR INSERIR QUANTIDADE DE PRODUTOS");
            rootIE.getChildren().add(alerta);
        }
        else {
            if (textoPrecoVenda.get() == null) {
                try {
                    //Atualizar o banco de dados com o produto novo
                    ResultSet rs = bd.select("SELECT PrecoVenda FROM Produtos WHERE Produtos.Codigo = \"" + codigo + "\";");
                    ResultSet rs2 = bd.select("SELECT PrecoCompra FROM Produtos WHERE Produtos.Codigo = \"" + codigo + "\";");
                    String precoVenda = rs.getString(1);
                    String precoCompra = rs2.getString(1);
                    bd.atualizar("INSERT INTO Estoque VALUES (\"" + codigo + "\", \"" + nome + "\", \"" + precoVenda + "\", \"" + precoCompra +
                            "\", \"" + quantidade + "\")" +
                            " ON CONFLICT(Codigo) DO UPDATE SET Quantidade = Quantidade + " + quantidade + ";");
                    bd.insertRowInto("HistEstoque", DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDateTime.now()),
                            DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalDateTime.now()), nome, precoCompra);
                    // Alerta pro usuario saber que teve sucesso
                    Alerta alerta = new Alerta(500, 200,
                            "ESTOQUE ATUALIZADO COM SUCESSO");
                    rootIE.getChildren().add(alerta);
                    campoNome.setDisable(true);
                } catch (SQLException e) { e.printStackTrace(); }
            }
            else {
                try {
                    bd.atualizar("INSERT OR IGNORE INTO Produtos VALUES (\"" + codigo + "\", \"" + nome + "\", \"" + textoPrecoVenda.get()  + "\", \"" + textoPrecoCompra.get() + "\");");
                    bd.atualizar("INSERT INTO Estoque VALUES (\"" + codigo + "\", \"" + nome + "\", \"" + textoPrecoVenda.get() +
                            "\", \""+ textoPrecoCompra.get()+"\", \"" + quantidade + "\")" +
                            " ON CONFLICT(Codigo) DO UPDATE SET Quantidade = Quantidade + " + quantidade + ";");
                    // Alerta pro usuario saber que teve sucesso
                    Alerta alerta = new Alerta(500, 200,
                            "ESTOQUE ATUALIZADO COM SUCESSO");
                    rootIE.getChildren().add(alerta);
                    campoNome.setDisable(true);
                } catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }

}
