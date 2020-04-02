package mercado.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import mercado.Driver;
import mercado.graficos.Alerta;
import mercado.graficos.ConfirmaAlerta;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class DefinirProdutos {

    @FXML private StackPane rootDP;
    @FXML private VBox base;
    @FXML private TextField campoQtd, campoNome, campoCod;
    @FXML private TableView tabela;
    private TableColumn<Produto, Integer> qtd = new TableColumn<>("Qtd");
    private TableColumn<Produto, String> nome = new TableColumn<>("Produto");
    private  ArrayList<Produto> arrayProdutos = new ArrayList<>();
    private  ObservableList<Produto> listaProdutos = FXCollections.observableArrayList();
    private static String clienteNome, clienteEndereco, clienteTelefone;
    public static void setClienteNome(String s) {clienteNome = s;}
    public  static void setClienteEndereco(String s) {clienteEndereco = s;}
    public static void setClienteTelefone(String s) {clienteTelefone = s;}

    @FXML
    private void initialize() {
        Driver.roots.add(rootDP);
        rootDP.getStylesheets().add(Configuracoes.temaSelecionado + "definirProdutos.css");
        campoQtd.textProperty().addListener((observableValue, s, t1) -> StartScene.filtroDeNumero(campoQtd, s, t1));
        tabela.getColumns().addAll(qtd, nome);
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        campoNome.setPromptText("Produto");
        campoQtd.setPromptText("Qtd");
        campoCod.setPromptText("Codigo");
        campoCod.focusedProperty().addListener((ov, oldV, newV) -> {
            if (!campoCod.isFocused())
                if (campoCod.getText().trim().length() != 0) {
                BancoDeDados bd = BancoDeDados.getInstance();
                try {
                    ResultSet rs = bd.select("SELECT * FROM Produtos WHERE \"Codigo\" = \"" + campoCod.getText() + "\"");
                    if (rs.isBeforeFirst()) {
                        campoNome.setText(rs.getString("Nome"));
                    }
                    else {
                        Alerta ca = new Alerta(500,200,
                                "PECA NAO CADASTRADA, VERIFIQUE OS DADOS DO PRODUTO");
                        rootDP.getChildren().add(ca);
                    }

                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        });
        nome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        qtd.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        tabela.setItems(listaProdutos);
        rootDP.getChildren().addListener((ListChangeListener<Node>) change -> {
            int i = 0;
            while (change.next()) {
                if (change.getAddedSubList().size() > 0) {
                    if (change.getAddedSubList().get(i) instanceof Alerta || change.getAddedSubList().get(i) instanceof ConfirmaAlerta) {
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
    private void adicionarProduto() {
        int valorQtd = campoQtd.getText().length();
        int valorNome = campoNome.getText().length();
        if(valorQtd == 0 && valorNome == 0){
            rootDP.getChildren().add(new Alerta(500,200,"Por favor, Insira os dados primeiro!"));
        }else if(valorQtd == 0){
            rootDP.getChildren().add(new Alerta(500,200,"Por favor, Informe a quantidade!"));
        }else if(valorNome == 0){
            rootDP.getChildren().add(new Alerta(500,200,"Por favor, Informe o nome do produto!"));
        }else{
            //Se o produto estiver adicionado, n vai mudar de valor
            int n = 0;
            for (Produto i : arrayProdutos) {
                // testa se algum produto tem o mesmo nome que o campo de texto
                if(i.getNome().equalsIgnoreCase(campoNome.getText())) {
                    ConfirmaAlerta ca = new ConfirmaAlerta(500,200, "Produto ja adicionado, deseja adicionar mesmo assim?");
                    rootDP.getChildren().add(ca);
                    ca.getNegativeButton().setOnAction(e -> {
                        ((StackPane) ca.getParent()).getChildren().remove(ca);
                    });
                    ca.getPositiveButton().setOnAction(e -> {
                        //Escreva aqui o codigo caso o botao apertado for sim
                        ((StackPane) ca.getParent()).getChildren().remove(ca);
                        double preco = 0;
                        try {
                            BancoDeDados bd = BancoDeDados.getInstance();
                            ResultSet rs = bd.select("SELECT * FROM Produtos WHERE \"Codigo\" = \"" + campoCod.getText() + "\"");
                            if (rs.isBeforeFirst()) {
                                preco = Double.parseDouble(rs.getString("PrecoVenda"));
                            }
                        } catch (SQLException x) {
                            x.printStackTrace();
                        }
                        //Codigo pra caso nao estiver adicionado na array
                        arrayProdutos.add(new Produto(campoNome.getText(), campoCod.getText(), preco,Integer.parseInt(campoQtd.getText())));
                        listaProdutos.clear();
                        listaProdutos.addAll(arrayProdutos);
                        tabela.refresh();
                    });
                    //Aumenta o valor do n, significando que sim, ja está adiconado
                    n++;
                }
            }
            //n == 0 significa que nao passou pelo if de cima, ent nao esta adiconado
            if (n == 0) {
                double preco = 0;
                try {
                    BancoDeDados bd = BancoDeDados.getInstance();
                    ResultSet rs = bd.select("SELECT * FROM Produtos WHERE \"Codigo\" = \"" + campoCod.getText() + "\"");
                    if (rs.isBeforeFirst()) {
                        preco = Double.parseDouble(rs.getString("PrecoVenda"));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                //Codigo pra caso nao estiver adicionado na array
                arrayProdutos.add(new Produto(campoNome.getText(), campoCod.getText(), preco,Integer.parseInt(campoQtd.getText())));
                listaProdutos.clear();
                listaProdutos.addAll(arrayProdutos);
                tabela.refresh();
            }
        }
    }

    @FXML
    private void salvarInfo() {
        DateTimeFormatter formatData = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter formatHora = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalDateTime dataAtual = LocalDateTime.now();
        double precoTotal = 0;
        for (Produto p : arrayProdutos) {
            precoTotal += p.getPreco() * p.getQuantidade();
        }

        BancoDeDados bd = BancoDeDados.getInstance();
        try {
            bd.atualizar("INSERT INTO HistVendas VALUES (NULL, \"" + formatData.format(dataAtual)+ "\", \"" + formatHora.format(dataAtual)+"\", \""+ precoTotal + "\");");
            ResultSet rs = bd.select("SELECT * FROM HistVendas ORDER BY ID DESC LIMIT 1");
            int currentId = rs.getInt("ID");
            bd.createTable("Venda" + currentId, "Codigo",  "TEXT", "Nome", "TEXT", "Preco", "REAL", "Quantidade", "INTEGER");

            for (Produto c : arrayProdutos) {
                String cod = c.getCodigo();
                String nome = c.getNome();
                double preco = c.getPreco();
                int qtd = c.getQuantidade();
                bd.insertRowInto("Venda" + currentId, cod, nome, String.valueOf(preco), String.valueOf(qtd));
            }
            bd.insertRowInto("Clientes", clienteNome, clienteEndereco, clienteTelefone);
            bd.createTable("Cliente" + clienteNome, "Data", "TEXT", "Horario", "TEXT",
                    "Venda", "TEXT", "Total", "REAL");
            bd.insertRowInto("Cliente" + clienteNome, formatData.format(dataAtual), formatHora.format(dataAtual),
                    String.valueOf(currentId), String.valueOf(precoTotal));
            Alerta a = new Alerta(500, 200, "CLIENTE CADASTRADO COM SUCESSO");
            rootDP.getChildren().add(a);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //Retirando os produtos vendidos do estoque
        try {
            for (Produto c : arrayProdutos) {
                String cod = c.getCodigo();
                String nome = c.getNome();
                double preco = c.getPreco();
                int qtd = c.getQuantidade();
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
    }
}
