package mercado.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import mercado.Driver;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DetalhesCompra {
    @FXML
    BorderPane root;
    @FXML
    StackPane rootDC;

    @FXML
    private TableView tabela = new TableView();
    private TableColumn<Produto, String> codigo = new TableColumn<>("CODIGO");
    private TableColumn<Produto, String> nome = new TableColumn<>("NOME");
    private TableColumn<Produto, Double> preco = new TableColumn<>("PRECO");
    private TableColumn<Produto, Integer> quantidade = new TableColumn<>("QUANTIDADE");
    private static ArrayList<Produto> produtos = new ArrayList<>();
    private static ObservableList<Produto> listaProdutos = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        Driver.roots.add(rootDC);
        rootDC.getStylesheets().add(Configuracoes.temaSelecionado + "tabela.css");
        tabela.getColumns().addAll(codigo, nome, preco, quantidade);
        codigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        nome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        preco.setCellValueFactory(new PropertyValueFactory<>("preco"));
        quantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        tabela.setItems(listaProdutos);
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        buscarProdutos();
    }

    @FXML
    private void voltar() {
        ((StackPane) rootDC.getParent()).getChildren().remove(rootDC);
    }
    static void buscarProdutos() {
        BancoDeDados bd = BancoDeDados.getInstance();
        try {
            ResultSet rs = bd.select("SELECT * FROM Venda" + Historico.selectedRowID);
            while (rs.next()) {
                produtos.add(new Produto(rs.getString("Nome"), rs.getString("Codigo"), rs.getDouble("Preco"), rs.getInt("Quantidade")));
            }
            rs.close();
            listaProdutos.clear();
            listaProdutos.addAll(produtos);
            produtos.clear();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
