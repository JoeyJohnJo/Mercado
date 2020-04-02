package mercado.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import mercado.Driver;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class VerProdutos {

    @FXML
    StackPane rootVP;
    @FXML
    private TableView tabela = new TableView();
    private TableColumn<Produto, String> codigo = new TableColumn<>("CODIGO");
    private TableColumn<Produto, String> nome = new TableColumn<>("NOME");
    private TableColumn<Produto, Double> preco = new TableColumn<>("PREÇO DE VENDA");
    private TableColumn<Produto, Double> precoCompra = new TableColumn<>("PREÇO DE COMPRA");
    private static ObservableList<Produto> listaProdutos = FXCollections.observableArrayList();
    private static ArrayList<Produto> produtos = new ArrayList<>();

    @FXML
    private void initialize() {
        Driver.roots.add(rootVP);
        rootVP.getStylesheets().add(Configuracoes.temaSelecionado + "tabela.css");
        tabela.getColumns().addAll(codigo, nome, preco, precoCompra);
        codigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        nome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        preco.setCellValueFactory(new PropertyValueFactory<>("preco"));
        precoCompra.setCellValueFactory(new PropertyValueFactory<>("precoCompra"));
        tabela.setItems(listaProdutos);
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    static void buscarProdutos() {
        BancoDeDados bd = BancoDeDados.getInstance();
        try {
            ResultSet rs = bd.select("SELECT * FROM Produtos");
            while (rs.next()) {
                produtos.add(new Produto(rs.getString("Nome"), rs.getString("Codigo"), rs.getDouble("PrecoVenda"), rs.getDouble("PrecoCompra")));
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
