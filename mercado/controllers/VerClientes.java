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

public class VerClientes {
    @FXML BorderPane root;
    @FXML StackPane rootVC;
    @FXML private TableView tabela = new TableView();


    private Cliente rowData;
    private Parent telaDetalhesCliente = null;
    private String caminhoDetalhesCliente = "../fxml/DetalhesCliente.fxml";

    private TableColumn<Cliente, String> nome = new TableColumn<>("NOME");
    private TableColumn<Cliente, String> endereco = new TableColumn<>("ENDEREÇO");
    private TableColumn<Cliente, Double> telefone = new TableColumn<>("TELEFONE");

    private static ArrayList<Cliente> clientes = new ArrayList<>();
    private static ObservableList<Cliente> listaClientes = FXCollections.observableArrayList();
    public static String selectedRowID;

    @FXML
    private void initialize() {
        Driver.roots.add(rootVC);
        rootVC.getStylesheets().add(Configuracoes.temaSelecionado + "tabela.css");
        tabela.getColumns().addAll(nome, endereco, telefone);
        nome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        endereco.setCellValueFactory(new PropertyValueFactory<>("endereco"));
        telefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));
        tabela.setItems(listaClientes);
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tabela.setRowFactory(tv -> {
            TableRow<Cliente> linha = new TableRow<>();
            linha.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! linha.isEmpty()) ) {
                    rowData = linha.getItem();
                    selectedRowID = rowData.getNome();
                    try {
                        loadFXML(telaDetalhesCliente, caminhoDetalhesCliente);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            return linha ;
        });
        buscarClientes();
    }

    static void buscarClientes() {
        BancoDeDados bd = BancoDeDados.getInstance();
        try {
            ResultSet rs = bd.select("SELECT * FROM Clientes");
            while (rs.next()) {
                clientes.add(new Cliente(rs.getString("Nome"), rs.getString("Endereco"), rs.getString("Telefone")));
            }
            rs.close();
            listaClientes.clear();
            listaClientes.addAll(clientes);
            clientes.clear();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadFXML(Parent p, String path) throws IOException {
        if (!rootVC.getChildren().contains(p)) {
            if (p == null)
                p = FXMLLoader.load(getClass().getResource(path));
            else {
                p = null;
                p = FXMLLoader.load(getClass().getResource(path));
            }

            rootVC.getChildren().add(p);
        }
        else { System.out.println("Cena ja inserida");}
    }
}
