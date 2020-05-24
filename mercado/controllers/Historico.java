package mercado.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import mercado.Driver;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Historico {
    @FXML
    BorderPane root;
    @FXML
    StackPane rootH;
    private VendaDO rowData;
    private Parent telaDetalhesCompra = null;
    private String caminhoDetalhesCompra = "/mercado/fxml/DetalhesCompra.fxml";

    @FXML
    private TableView tabela = new TableView();
    private TableColumn<Produto, String> id = new TableColumn<>("ID");
    private TableColumn<Produto, String> data = new TableColumn<>("DATA");
    private TableColumn<Produto, Double> horario = new TableColumn<>("HORARIO");
    private TableColumn<Produto, Double> valorFinal = new TableColumn<>("VALOR FINAL");
    private static ArrayList<VendaDO> vendas = new ArrayList<>();
    private static ObservableList<VendaDO> listaVendas = FXCollections.observableArrayList();
    public static int selectedRowID;

    @FXML
    private void initialize() {
        Driver.roots.add(rootH);
        rootH.getStylesheets().add(Configuracoes.temaSelecionado + "tabela.css");
        tabela.getColumns().addAll(id, data, horario, valorFinal);
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        data.setCellValueFactory(new PropertyValueFactory<>("data"));
        horario.setCellValueFactory(new PropertyValueFactory<>("horario"));
        valorFinal.setCellValueFactory(new PropertyValueFactory<>("valorFinal"));
        tabela.setItems(listaVendas);
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        buscarHistorico();
        tabela.setRowFactory(tv -> {
            TableRow<VendaDO> linha = new TableRow<>();
            linha.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! linha.isEmpty()) ) {
                    rowData = linha.getItem();
                    selectedRowID = rowData.getId();
                    try {
                        loadFXML(telaDetalhesCompra, caminhoDetalhesCompra);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            return linha ;
        });
    }
    private void loadFXML(Parent p, String path) throws IOException {
        if (!rootH.getChildren().contains(p)) {
            if (p == null)
                p = new FXMLLoader().load(getClass().getResourceAsStream(path));
            else {
                p = null;
                p = new FXMLLoader().load(getClass().getResourceAsStream(path));
            }

            rootH.getChildren().add(p);
        }
        else { System.out.println("Cena ja inserida");}
    }
    static void buscarHistorico() {
        BancoDeDados bd = BancoDeDados.getInstance();
        try {
            ResultSet rs = bd.select("SELECT * FROM HistVendas");
            while (rs.next()) {
                vendas.add(new VendaDO(rs.getInt("ID"), rs.getString("Data"), rs.getString("Horario"), rs.getDouble("ValorFinal")));
            }
            rs.close();
            listaVendas.clear();
            listaVendas.addAll(vendas);
            vendas.clear();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
