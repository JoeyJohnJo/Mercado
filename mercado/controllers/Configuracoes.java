package mercado.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import mercado.Driver;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Properties;
import java.util.ResourceBundle;

public class Configuracoes {

    @FXML
    TextField tVerEstoque, tInserirEstoque, tVerProduto, tCadastrarProduto, tVerClientes,
            tCadastrarClientes, tHistorico, tRendaeGastos, tConfig, tQtdEstoque, tVerAlertas, tEntradas;
    private ArrayList<TextField> textFields;
    @FXML
    private StackPane rootConfig;
    @FXML
    BorderPane borderPane;
    private String local = Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("")).getPath();

    public static String temaSelecionado = ResourceBundle.getBundle("mercado.resources.config").getString("Tema");
    static String atalhoCP = ResourceBundle.getBundle("mercado.resources.config").getString("CadastrarProduto");
    static String atalhoIE = ResourceBundle.getBundle("mercado.resources.config").getString("InserirEstoque");
    static String atalhoVP = ResourceBundle.getBundle("mercado.resources.config").getString("VerProdutos");
    static String atalhoVE = ResourceBundle.getBundle("mercado.resources.config").getString("VerEstoque");
    static String atalhoVC = ResourceBundle.getBundle("mercado.resources.config").getString("VerClientes");
    static String atalhoCC = ResourceBundle.getBundle("mercado.resources.config").getString("CadastrarCliente");
    static String atalhoConfig = ResourceBundle.getBundle("mercado.resources.config").getString("Configuracoes");
    static String atalhoHV = ResourceBundle.getBundle("mercado.resources.config").getString("Historico");
    static String atalhoRG = ResourceBundle.getBundle("mercado.resources.config").getString("RendaeGastos");
    static String atalhoAl = ResourceBundle.getBundle("mercado.resources.config").getString("Alertas");
    static String atalhoHE = ResourceBundle.getBundle("mercado.resources.config").getString("HistoricoEntrada");
    static int qtdAlerta = Integer.parseInt(ResourceBundle.getBundle("mercado.resources.config").getString("QtdMinEstoque"));
    private ArrayList<String> atalhosExistentes = new ArrayList<>();

    private String caminhoConfig = local + "mercado/resources/config.properties";
    private Properties props;
    @FXML
    private MenuButton mTema;
    @FXML
    private MenuItem temaDark, temaLight;

    @FXML
    private void initialize() throws IOException {
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //Array de campos de texto
        textFields = new ArrayList<>();
        textFields.add(tVerEstoque);
        textFields.add(tInserirEstoque);
        textFields.add(tVerProduto);
        textFields.add(tCadastrarProduto);
        textFields.add(tVerClientes);
        textFields.add(tCadastrarClientes);
        textFields.add(tHistorico);
        textFields.add(tRendaeGastos);
        textFields.add(tConfig);
        textFields.add(tVerAlertas);
        textFields.add(tEntradas);
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        Driver.roots.add(rootConfig);
        rootConfig.getStylesheets().add(temaSelecionado + "config.css");
        temaDark.setOnAction(e -> {
            temaSelecionado = "/mercado/styles/dark/";
            mTema.setText("Tema escuro");
        });
        temaLight.setOnAction(e -> {
            temaSelecionado = "/mercado/styles/light/";
            mTema.setText("Tema claro");

        });
        props = new Properties();
        props.load(new FileInputStream(caminhoConfig));
        for (TextField tf : textFields) {
            tf.textProperty().addListener((observableValue, s, t1) -> filtroDeCaractere(tf, s, t1));
        }
        tQtdEstoque.textProperty().addListener((observableValue, s, t1) ->StartScene.filtroDeNumero(tQtdEstoque, s, t1));
        props.forEach((k, v) -> {
            if (!k.toString().equalsIgnoreCase("Tema") && !k.toString().equalsIgnoreCase("QtdMinEstoque"))
                atalhosExistentes.add(v.toString());
        });
    }
    @FXML
    private void salvar() throws IOException {
        salvarTema();
        salvarAtalhos();
        salvarAlertas();
        props.store(new FileOutputStream(caminhoConfig), null);
    }

    private void filtroDeCaractere(TextField tf, String a, String b) {

        //Expressão regex para aceitar só 1 caractere no campo de texto
        if (!b.matches("\\w??")) {
            tf.setText(a);
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //Lógica para não aceitar o mesmo caractere em mais de um campo de texto


        //Copias da array de campos de texto
        ArrayList<TextField> copiaOriginal = new ArrayList<>(textFields);
        ArrayList<TextField> copy = new ArrayList<>(textFields);

        //Remove o campo de texto da array para nao comparar com ele mesmo
        copy.remove(tf);
        //Para cada campo de texto na array ( for each in )
        for (TextField e : copy) {
            //Se o texto for igual, ignorando letra maiúscula ou minúscula
            if (tf.getText().equalsIgnoreCase(e.getText()) || atalhosExistentes.contains(tf.getText().toUpperCase())) {
                //Setta o texto em nada
                tf.setText("");
            }
        }
        copy.clear();
        copy.addAll(copiaOriginal);
    }

    private void salvarTema() {
        props.setProperty("Tema", temaSelecionado);
        for (StackPane e : Driver.roots) {
            if ( ( e.getId().matches("rootSS") ) || ( e.getId().matches("rootADM") ) ){
                e.getStylesheets().clear();
                e.getStylesheets().add(temaSelecionado + "startScene.css");
            }
            else if (e.getId().matches("rootVE") || e.getId().matches("rootVP") ||
                    e.getId().matches("rootH")|| e.getId().matches("rootDC") ||
                    e.getId().matches("rootHE") || e.getId().matches("rootVC") || e.getId().matches("rootDCl")) {
                e.getStylesheets().clear();
                e.getStylesheets().add(temaSelecionado + "tabela.css");
            }
            else if (e.getId().matches("rootIE")) {
                e.getStylesheets().clear();
                e.getStylesheets().add(temaSelecionado + "inserirEstoque.css");
            }
            else if (e.getId().matches("rootRG")) {
                e.getStylesheets().clear();
                e.getStylesheets().add(temaSelecionado + "rendaGastos.css");
            }
            else if (e.getId().matches("rootCP")){
                e.getStylesheets().clear();
                e.getStylesheets().add(temaSelecionado + "cadastrarProduto.css");
            }
            else if (e.getId().matches("rootDP")){
                e.getStylesheets().clear();
                e.getStylesheets().add(temaSelecionado + "definirProdutos.css");
            }
            else if (e.getId().matches("rootCC")){
                e.getStylesheets().clear();
                e.getStylesheets().add(temaSelecionado + "cadastrarCliente.css");
            }
            else if (e.getId().matches("rootConfig")){
                e.getStylesheets().clear();
                e.getStylesheets().add(temaSelecionado + "config.css");
            }
            else if (e.getId().matches("rootAlertas")){
                e.getStylesheets().clear();
                e.getStylesheets().add(temaSelecionado + "telaAlertas.css");
            }
            else if (e.getId().matches("rootVendas") || e.getId().matches("rootFinalizar")){
                e.getStylesheets().clear();
                e.getStylesheets().add(temaSelecionado + "modoVendas.css");
            }
            for (HBox hb : Alertas.visuals) {
                hb.getStylesheets().clear();
                hb.getStylesheets().add(temaSelecionado + "config.css");
            }
        }
    }
    private void salvarAtalhos() {
        if (tVerEstoque.getText().length() != 0) {
            props.setProperty("VerEstoque", tVerEstoque.getText().toUpperCase());
            atalhoVE = tVerEstoque.getText().toUpperCase();
        }
        if (tInserirEstoque.getText().length() != 0) {
            props.setProperty("InserirEstoque", tInserirEstoque.getText().toUpperCase());
            atalhoIE = tInserirEstoque.getText().toUpperCase();
        }
        if (tVerProduto.getText().length() != 0) {
            props.setProperty("VerProdutos", tVerProduto.getText().toUpperCase());
            atalhoVP = tVerProduto.getText().toUpperCase();
        }
        if (tCadastrarProduto.getText().length() != 0) {
            props.setProperty("CadastrarProduto", tCadastrarProduto.getText().toUpperCase());
            atalhoCP = tCadastrarProduto.getText().toUpperCase();
        }
        if (tVerClientes.getText().length() != 0) {
            props.setProperty("VerClientes", tVerClientes.getText().toUpperCase());
            atalhoVC = tVerClientes.getText().toUpperCase();
        }
        if (tCadastrarClientes.getText().length() != 0) {
            props.setProperty("CadastrarCliente", tCadastrarClientes.getText().toUpperCase());
            atalhoCC = tCadastrarClientes.getText().toUpperCase();
        }
        if (tHistorico.getText().length() != 0) {
            props.setProperty("Historico", tHistorico.getText().toUpperCase());
            atalhoHV = tHistorico.getText().toUpperCase();
        }
        if (tRendaeGastos.getText().length() != 0) {
            props.setProperty("RendaeGastos", tRendaeGastos.getText().toUpperCase());
            atalhoRG = tRendaeGastos.getText().toUpperCase();
        }
        if (tConfig.getText().length() != 0) {
            props.setProperty("Configuracoes", tConfig.getText().toUpperCase());
            atalhoConfig = tConfig.getText().toUpperCase();
        }
        if (tVerAlertas.getText().length() != 0) {
            props.setProperty("Alertas", tVerAlertas.getText().toUpperCase());
            atalhoAl = tVerAlertas.getText().toUpperCase();
        }
        if (tEntradas.getText().length() != 0) {
            props.setProperty("HistoricoEntrada", tEntradas.getText().toUpperCase());
            atalhoHE = tEntradas.getText().toUpperCase();
        }
    }
    private void salvarAlertas() {
        if (tQtdEstoque.getText().length() > 0) {
            props.setProperty("QtdMinEstoque", tQtdEstoque.getText());
            qtdAlerta = Integer.parseInt(tQtdEstoque.getText());
        }
    }
}
