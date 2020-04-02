package mercado.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import mercado.Driver;
import mercado.graficos.Alerta;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ModoVendas {
    private String caminhoFinalizarCompra = "../fxml/FinalizarCompra.fxml";
    private Parent telaFinalizarCompra = null;
    @FXML StackPane rootVendas;
    @FXML BorderPane col0, col1, col2, col3;
    @FXML Button finalizar = new Button();
    @FXML VBox list;
    @FXML Label total;
    TextField codigo = new TextField(), qtd = new TextField();
    Label nome = new Label(), preco = new Label();

    @FXML
    private void initialize() {
        Driver.roots.add(rootVendas);
        rootVendas.getStylesheets().add(Configuracoes.temaSelecionado + "modoVendas.css");
        col0.setCenter(codigo);
        col1.setCenter(nome);
        col2.setCenter(preco);
        col3.setCenter(qtd);

        Platform.runLater(() -> {
            codigo.requestFocus();
        });

        codigo.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (codigo.getText().trim().length() != 0) {
                BancoDeDados bd = BancoDeDados.getInstance();
                try {
                    ResultSet rs = bd.select("SELECT * FROM Produtos WHERE \"Codigo\" = \"" + codigo.getText() + "\"");
                    if (rs.isBeforeFirst()) {
                        nome.setText(rs.getString("Nome"));
                        preco.setText( rs.getString("PrecoVenda"));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        codigo.setOnKeyPressed( e -> {
            if (codigo.isFocused()) {
                if (e.getCode() == KeyCode.ENTER) {
                    qtd.setText("1");

                    if (codigo.getText().trim().length() != 0) {
                        BancoDeDados bd = BancoDeDados.getInstance();
                        try {
                            ResultSet rs = bd.select("SELECT * FROM Produtos WHERE \"Codigo\" = \"" + codigo.getText() + "\"");
                            if (rs.isBeforeFirst()) {
                                nome.setText(rs.getString("Nome"));
                                preco.setText(rs.getString("PrecoVenda"));
                            }
                        } catch (SQLException i) {
                            i.printStackTrace();
                        }
                    }
                    list.getChildren().add(new Componente(codigo.getText(), nome.getText(),
                            Double.parseDouble(preco.getText()), Integer.parseInt(qtd.getText())));
                    total.setText(String.valueOf(somarTotal()));
                    codigo.clear();
                    nome.setText("");
                    preco.setText("");
                    qtd.clear();
                    codigo.requestFocus();
                }
            }
        });
        qtd.setOnKeyPressed(e -> {
            if (qtd.isFocused()) {
                if (e.getCode() == KeyCode.ENTER) {
                    list.getChildren().add(new Componente(codigo.getText(), nome.getText(),
                            Double.parseDouble(preco.getText()), Integer.parseInt(qtd.getText())));
                    total.setText(String.valueOf(somarTotal()));
                    codigo.clear();
                    nome.setText("");
                    preco.setText("");
                    qtd.clear();
                    codigo.requestFocus();

                }
            }
        });
        qtd.textProperty().addListener((observableValue, s, t1) -> filtroDeNumero(qtd, s, t1));
        finalizar.setOnAction(e -> {
            try {
                FinalizarCompra.setTotal(Double.parseDouble(total.getText()));
                loadFXML(telaFinalizarCompra, caminhoFinalizarCompra);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        FinalizarCompra.bFinalizar.setOnAction(e -> finalizar());
    }
    // O botao está em outra cena mas a acao está aqui porque todas as informaçoes necessarias estao nessa classe
    private void finalizar() {
        DateTimeFormatter formatData = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter formatHora = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalDateTime dataAtual = LocalDateTime.now();
        String cliente = FinalizarCompra.tCliente.getText();
        BancoDeDados bd = BancoDeDados.getInstance();
        if (cliente.trim().length() > 0) {
            try {
                ResultSet rs2 = bd.select("SELECT * FROM Clientes WHERE NOME = \"" + cliente + "\"");

                if (rs2.isBeforeFirst()) {
                    bd.atualizar("INSERT INTO HistVendas VALUES (NULL, \"" + formatData.format(dataAtual)+ "\", \"" + formatHora.format(dataAtual)+"\", \""+ total.getText()+ "\");");

                    ResultSet rs = bd.select("SELECT * FROM HistVendas ORDER BY ID DESC LIMIT 1");
                    int currentId = rs.getInt("ID");

                    bd.createTable("Venda" + currentId, "Codigo",  "TEXT", "Nome", "TEXT", "Preco", "REAL", "Quantidade", "INTEGER");
                    for (Node c : list.getChildren()) {
                        String cod = ((Componente) c).getCodigo();
                        String nome = ((Componente) c).getNome();
                        double preco = ((Componente) c).getPreco();
                        int qtd = ((Componente) c).getQtd();
                        bd.insertRowInto("Venda" + currentId, cod, nome, String.valueOf(preco), String.valueOf(qtd));
                    }

                    bd.insertRowInto("Cliente" + cliente, formatData.format(dataAtual), formatHora.format(dataAtual),
                            String.valueOf(currentId), total.getText());

                    //Retirando os produtos vendidos do estoque
                    try {
                        for (Node c : list.getChildren()) {
                            String cod = ((Componente) c).getCodigo();
                            String nome = ((Componente) c).getNome();
                            double preco = ((Componente) c).getPreco();
                            int qtd = ((Componente) c).getQtd();
                            ResultSet rs3 = bd.select("SELECT * FROM Estoque WHERE Codigo = \"" + cod + "\";");
                            if (rs3.isBeforeFirst()) {
                                int prevQtd = rs3.getInt("Quantidade");
                                int newQtd = prevQtd - qtd;
                                bd.atualizar("UPDATE Estoque SET Quantidade = \"" + newQtd + "\" WHERE Codigo = \"" + cod + "\";");
                            }
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    rootVendas.getChildren().remove(rootVendas.getChildren().get(1));
                    list.getChildren().clear();
                    total.setText("00.00");
                }
                else {
                    Alerta a = new Alerta(500,200, "CLIENTE NAO ENCONTRADO, VERIFIQUE O NOME E TENTE NOVAMENTE");
                    rootVendas.getChildren().add(a);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else {
            try {
                bd.atualizar("INSERT INTO HistVendas VALUES (NULL, \"" + formatData.format(dataAtual)+ "\", \"" + formatHora.format(dataAtual)+"\", \""+ total.getText()+ "\");");
                ResultSet rs = bd.select("SELECT * FROM HistVendas ORDER BY ID DESC LIMIT 1");
                int currentId = rs.getInt("ID");
                bd.createTable("Venda" + currentId, "Codigo",  "TEXT", "Nome", "TEXT", "Preco", "REAL", "Quantidade", "INTEGER");
                for (Node c : list.getChildren()) {
                    String cod = ((Componente) c).getCodigo();
                    String nome = ((Componente) c).getNome();
                    double preco = ((Componente) c).getPreco();
                    int qtd = ((Componente) c).getQtd();
                    bd.insertRowInto("Venda" + currentId, cod, nome, String.valueOf(preco), String.valueOf(qtd));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            //Retirando os produtos vendidos do estoque
            try {
                for (Node c : list.getChildren()) {
                    String cod = ((Componente) c).getCodigo();
                    String nome = ((Componente) c).getNome();
                    double preco = ((Componente) c).getPreco();
                    int qtd = ((Componente) c).getQtd();
                    ResultSet rs = bd.select("SELECT * FROM Estoque WHERE Codigo = \"" + cod + "\";");
                    if (rs.isBeforeFirst()) {
                        int prevQtd = rs.getInt("Quantidade");
                        int newQtd = prevQtd - qtd;
                        bd.atualizar("UPDATE Estoque SET Quantidade = \"" + newQtd + "\" WHERE Codigo = \"" + cod + "\";");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            rootVendas.getChildren().remove(rootVendas.getChildren().get(1));
            list.getChildren().clear();
            total.setText("00.00");
        }
    }
    private void loadFXML(Parent p, String path) throws IOException {
        if (!rootVendas.getChildren().contains(p)) {
            if (p == null)
                p = FXMLLoader.load(getClass().getResource(path));
            else {
                p = null;
                p = FXMLLoader.load(getClass().getResource(path));
            }
            rootVendas.getChildren().removeAll();
            rootVendas.getChildren().add(p);
        }
        else { System.out.println("Cena ja inserida");}
    }
    private static void filtroDeNumero(TextField tf, String a, String b) {
        if (!b.matches("\\d{0,9}?")) {
            tf.setText(a);
        }
    }
    private double somarTotal() {
        double prev = Double.parseDouble(total.getText());
        double newTot = prev + (Double.parseDouble(preco.getText()) * Integer.parseInt(qtd.getText()));
        return newTot;
    }
    private class Componente extends HBox {
        private String codigo, nome;
        private double preco;
        private int qtd;
        Componente(String codigo, String nome, double preco, int qtd){
            this.codigo = codigo;
            this.nome = nome;
            this.preco = preco;
            this.qtd = qtd;
            Region a = new Region();
            Region b = new Region();
            Region c = new Region();
            HBox.setHgrow(a, Priority.ALWAYS);
            HBox.setHgrow(b, Priority.ALWAYS);
            HBox.setHgrow(c, Priority.ALWAYS);
            Label cod = new Label(codigo);
            Label quant = new Label(String.valueOf(qtd));
            Label desc = new Label(nome);
            Label valor = new Label(String.valueOf(preco));
            this.getChildren().addAll(cod, a, desc, b, valor, c, quant);
        }

        String getCodigo() {return codigo;}
        String getNome() {return nome;}
        double getPreco() {return preco;}
        int getQtd() {return qtd;}
    }
}
