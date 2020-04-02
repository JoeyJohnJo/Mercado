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

public class DetalhesCliente {

    @FXML
    BorderPane root;
    @FXML
    StackPane rootDCl;

    @FXML
    private TableView tabela = new TableView();
    private TableColumn<VendaDO, String> data = new TableColumn<>("DATA");
    private TableColumn<VendaDO, String> horario = new TableColumn<>("HORARIO");
    private TableColumn<VendaDO, Double> venda = new TableColumn<>("ID VENDA");
    private TableColumn<VendaDO, Integer> total = new TableColumn<>("TOTAL");
    private static ArrayList<VendaDO> vendas = new ArrayList<>();
    private static ObservableList<VendaDO> listaVendas = FXCollections.observableArrayList();
    public static int selectedRowID;
    private VendaDO rowData;
    private Parent telaDetalhesCompra = null;
    private String caminhoDetalhesCompra = "../fxml/DetalhesCompra.fxml";

    @FXML
    private void initialize() {
        Driver.roots.add(rootDCl);
        rootDCl.getStylesheets().add(Configuracoes.temaSelecionado + "tabela.css");
        tabela.getColumns().addAll(data, horario, venda, total);
        data.setCellValueFactory(new PropertyValueFactory<>("data"));
        horario.setCellValueFactory(new PropertyValueFactory<>("horario"));
        venda.setCellValueFactory(new PropertyValueFactory<>("id"));
        total.setCellValueFactory(new PropertyValueFactory<>("valorFinal"));
        tabela.setItems(listaVendas);
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tabela.setRowFactory(tv -> {
            TableRow<VendaDO> linha = new TableRow<>();
            linha.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! linha.isEmpty()) ) {
                    rowData = linha.getItem();
                    selectedRowID = rowData.getId();
                    try {
                        Historico.selectedRowID = selectedRowID;
                        loadFXML(telaDetalhesCompra, caminhoDetalhesCompra);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            return linha ;
        });
        buscarVendas();
    }

    @FXML
    private void voltar() {
        ((StackPane) rootDCl.getParent()).getChildren().remove(rootDCl);
    }
    private void loadFXML(Parent p, String path) throws IOException {
        if (!rootDCl.getChildren().contains(p)) {
            if (p == null)
                p = FXMLLoader.load(getClass().getResource(path));
            else {
                p = null;
                p = FXMLLoader.load(getClass().getResource(path));
            }

            rootDCl.getChildren().add(p);
        }
        else { System.out.println("Cena ja inserida");}
    }

    static void buscarVendas() {
        BancoDeDados bd = BancoDeDados.getInstance();
        try {
            ResultSet rs = bd.select("SELECT * FROM Cliente" + VerClientes.selectedRowID);
            while (rs.next()) {
                vendas.add(new VendaDO(rs.getInt("Venda"), rs.getString("Data"),
                    rs.getString("Horario"), rs.getDouble("Total")));
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
