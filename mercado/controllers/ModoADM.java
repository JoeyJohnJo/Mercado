package mercado.controllers;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import mercado.Driver;
import java.io.IOException;
import java.net.URL;
import mercado.graficos.Alerta;

public class ModoADM {

    private String caminhoCadastrarCliente = "../fxml/CadastrarCliente.fxml";
    private String caminhoVerProdutos = "../fxml/VerProdutos.fxml";
    private String caminhoCadastrarProduto = "../fxml/CadastrarProduto.fxml";
    private String caminhoVerEstoque = "../fxml/VerEstoque.fxml";
    private String caminhoInserirEstoque = "../fxml/InserirEstoque.fxml";
    private String caminhoConfig = "../fxml/Configuracoes.fxml";
    private String caminhoAlertas = "../fxml/Alertas.fxml";
    private String caminhoHistorico = "../fxml/Historico.fxml";
    private String caminhoEntrada = "../fxml/HistEntradas.fxml";
    private String caminhoRendaGastos = "../fxml/RendaGastos.fxml";
    private String caminhoVerClientes = "../fxml/VerClientes.fxml";
    private static Parent telaVerClientes = null;
    private static Parent telaRendaGastos = null;
    private static Parent telaVerProdutos = null;
    private static Parent telaCadastrarProduto = null;
    private static Parent telaVerEstoque = null;
    private static Parent telaInserirEstoque = null;
    private static Parent telaAlertas = null;
    private static Parent telaCadastrarCliente = null;
    private static Parent telaHistorico = null;
    private static Parent telaHistEntrada = null;
    private Parent telaConfig = null;
    private URL html;
    @FXML private StackPane rootADM;
    @FXML private VBox vbox, bBar;
    @FXML private BorderPane ribbon;
    @FXML private Pane ribbonBar;
    @FXML private BorderPane contentPane;
    @FXML private Button bEstoque, bAddEstoque, bProdutos, bAddProdutos, bClientes,
            bAddClientes, bVendas, bGastos, bAlertas, bEntrada;

    private Button[] buttons;
    private Parent p;

    @FXML
    private void initialize() throws IOException {
        Alertas alertas = new Alertas();
        Driver.roots.add(rootADM);
        rootADM.getStylesheets().add(Configuracoes.temaSelecionado + "startScene.css");
        telaVerEstoque = FXMLLoader.load(getClass().getResource(caminhoVerEstoque));
        telaVerProdutos = FXMLLoader.load(getClass().getResource(caminhoVerProdutos));
        buttons = new Button[]{bEstoque, bAddEstoque, bProdutos, bAddProdutos, bClientes, bAddClientes, bVendas, bGastos, bAlertas, bEntrada};
        for (HBox hb : alertas.getAlertas()) {
            vbox.getChildren().add(hb);
        }

        Atalhos atalhos = new Atalhos();
        Platform.runLater(() -> {
            p = rootADM.getParent();
            p.getParent().setOnKeyPressed( keyEvent -> {
                boolean ctrlPressed;

                if (keyEvent.getCode().equals(KeyCode.CONTROL)) ctrlPressed = true;
                else ctrlPressed = false;

                if (ctrlPressed) p.getParent().addEventHandler(KeyEvent.KEY_PRESSED, atalhos);
                else p.getParent().removeEventHandler(KeyEvent.KEY_PRESSED, atalhos);

            });
        });
    }

    @FXML
    private void redimensionarMenu() {
        ribbonBar.setOnMouseDragged(e -> {
            double posicao = e.getSceneX();
            if (posicao < 315) {
                ribbon.setPrefWidth(posicao);
                bBar.setPrefWidth(posicao);
            }
            ribbonBar.setCursor(Cursor.H_RESIZE);
            for (Button b : buttons) {
                b.setPrefWidth(ribbon.getPrefWidth());
            }
        });
    }
    @FXML
    private void carregarTelaCadastrarProduto() throws IOException {
        loadFXML(telaCadastrarProduto, caminhoCadastrarProduto);
    }
    @FXML
    private void carregarTelaVerProdutos() {
        if (!contentPane.getChildren().contains(telaVerProdutos)) {
            contentPane.getChildren().clear();
            VerProdutos.buscarProdutos();
            contentPane.setCenter(telaVerProdutos);
        }
        else { System.out.println("Cena ja inserida");}
    }
    @FXML
    private void carregarTelaCadastrarCliente() {
        try {
            loadFXML(telaCadastrarCliente, caminhoCadastrarCliente);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void carregarTelaVerEstoque() {
        if (!contentPane.getChildren().contains(telaVerEstoque)) {
            contentPane.getChildren().clear();
            VerEstoque.buscarEstoque();
            contentPane.setCenter(telaVerEstoque);
        }
        else { System.out.println("Cena ja inserida");}

    }
    @FXML
    private void carregarTelaInserirEstoque() throws IOException {loadFXML(telaInserirEstoque, caminhoInserirEstoque);}
    @FXML
    private void carregarTelaConfiguracoes() throws IOException { loadFXML(telaConfig, caminhoConfig);}
    @FXML
    private void carregarTelaAlertas() throws IOException {loadFXML(telaAlertas, caminhoAlertas);}
    @FXML
    private void carregarTelaHistorico() throws IOException {
        loadFXML(telaHistorico, caminhoHistorico);
        Historico.buscarHistorico();
    }
    @FXML
    private void carregarTelaRendimentoGastos() {
        try {
            loadFXML(telaRendaGastos, caminhoRendaGastos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void carregarTelaEntradas() throws IOException {
        loadFXML(telaHistEntrada, caminhoEntrada);
        HistoricoEntrada.buscarHistorico();
    }
    @FXML
    private void carregarTelaVerClientes() throws IOException {
        loadFXML(telaVerClientes, caminhoVerClientes);
        VerClientes.buscarClientes();
    }

    private void loadFXML(Parent p, String path) throws IOException {
        /*Testa se a janela ja está com esta tela aberta, se estiver nao
         * adiciona a tela, pois isso duplicaria o conteudo na janela.
         * Se a tela nao estiver aberta mas ter outro conteudo na janela,
         * limoa este conteudo para que somente a janela desejada apareça*/
        if (!contentPane.getChildren().contains(p)) {
            if (p == null)
                p = FXMLLoader.load(getClass().getResource(path));
            else {
                p = null;
                p = FXMLLoader.load(getClass().getResource(path));
            }
            contentPane.getChildren().clear();
            contentPane.setCenter(p);

        }
        else { System.out.println("Cena ja inserida");}
    }

    class Atalhos implements EventHandler<KeyEvent> {
        @Override
        public void handle(KeyEvent e) {
            if (e.getCode().equals(KeyCode.getKeyCode(Configuracoes.atalhoCP)) ) {
                try {
                    carregarTelaCadastrarProduto();
                } catch (IOException a) {
                    a.printStackTrace();
                }
            }
            else if (e.getCode().equals(KeyCode.getKeyCode(Configuracoes.atalhoIE)) ) {
                try {
                    carregarTelaInserirEstoque();

                } catch (IOException a) {
                    a.printStackTrace();
                }
            }
            else if (e.getCode().equals(KeyCode.getKeyCode(Configuracoes.atalhoHE)) ) {
                try {
                    carregarTelaEntradas();
                } catch (IOException a) {
                    a.printStackTrace();
                }
            }
            else if (e.getCode().equals(KeyCode.getKeyCode(Configuracoes.atalhoVE)) ) {
                carregarTelaVerEstoque();
            }
            else if (e.getCode().equals(KeyCode.getKeyCode(Configuracoes.atalhoVP)) ) {
                carregarTelaVerProdutos();
            }
            else if (e.getCode().equals(KeyCode.getKeyCode(Configuracoes.atalhoCC)) ) {
                carregarTelaCadastrarCliente();
            }
            else if (e.getCode().equals(KeyCode.getKeyCode(Configuracoes.atalhoConfig)) ){
                try {
                    carregarTelaConfiguracoes();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            else if (e.getCode().equals(KeyCode.getKeyCode(Configuracoes.atalhoRG)) ) {
                carregarTelaRendimentoGastos();
            }
            else if (e.getCode().equals(KeyCode.getKeyCode(Configuracoes.atalhoHV)) ) {
                try {
                    carregarTelaHistorico();
                } catch (IOException ex) {
                ex.printStackTrace();
                }
            }
            else if (e.getCode().equals(KeyCode.getKeyCode(Configuracoes.atalhoVC)) ) {
                try {
                    carregarTelaVerClientes();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            else if (e.getCode().equals(KeyCode.getKeyCode(Configuracoes.atalhoAl)) ) {
                try {
                    carregarTelaAlertas();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
