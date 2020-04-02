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

public class HistoricoEntrada {

    @FXML StackPane rootHE;
    @FXML private TableView tabela = new TableView();
    private TableColumn<Produto, String> data = new TableColumn<>("Data");
    private TableColumn<Produto, String> hora = new TableColumn<>("Horario");
    private TableColumn<Produto, String> produto = new TableColumn<>("Produto");
    private TableColumn<Produto, String> preco = new TableColumn<>("Preco de Compra");
    private static ArrayList<EntradaDO> entradas = new ArrayList<>();
    private static ObservableList<EntradaDO> listaEntradas = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        Driver.roots.add(rootHE);
        rootHE.getStylesheets().add(Configuracoes.temaSelecionado + "tabela.css");
        tabela.getColumns().addAll(data, hora, produto, preco);
        data.setCellValueFactory(new PropertyValueFactory<>("data"));
        hora.setCellValueFactory(new PropertyValueFactory<>("horario"));
        produto.setCellValueFactory(new PropertyValueFactory<>("produto"));
        preco.setCellValueFactory(new PropertyValueFactory<>("preco"));
        tabela.setItems(listaEntradas);
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        buscarHistorico();
    }

    static void buscarHistorico(){
        BancoDeDados bd = BancoDeDados.getInstance();
        try {
            ResultSet rs = bd.select("SELECT * FROM HistEstoque");
            while (rs.next()) {
                entradas.add(new EntradaDO(rs.getString("Data"), rs.getString("Horario"), rs.getString("Produto"), rs.getDouble("PrecoCompra")));
            }
            rs.close();
            listaEntradas.clear();
            listaEntradas.addAll(entradas);
            entradas.clear();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
