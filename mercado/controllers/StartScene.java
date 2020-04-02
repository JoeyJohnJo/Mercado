/*E recomendado listar todas as telas (HTML e FXML) que serão
* utilizados, no topo da classe e colocar o camimho de cada tela
* na funcao initialize(). Assim, fica mais facil editar o caminho
* dos arquivos sem ter que mexer em nenhuma outra funcao que use os,
* pois estariam referenciando apenas a variavel declarada no topo da classe*/
package mercado.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import mercado.Driver;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

public class StartScene {
    private String ModoADM = "../fxml/ModoADM.fxml";
    private String ModoVendas = "../fxml/ModoVendas.fxml";
    private static Parent telaModoADM = null;
    private static Parent telaModoVendas = null;
    @FXML private StackPane rootSS;
    @FXML private BorderPane borderPane, contentPane;
    @FXML private Slider slider;
    @FXML private Label sliderText;
    @FXML HBox top;

    @FXML
    private void initialize() {
        Driver.roots.add(rootSS);
        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(slider.getValue() == 1){
                try {
                    loadFXML(telaModoADM, ModoADM);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                sliderText.setText("ADM");
            }
            else if(slider.getValue() == 0){
                try {
                    loadFXML(telaModoVendas, ModoVendas);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                sliderText.setText("VENDAS");
            }
        });
        rootSS.getStylesheets().add(Configuracoes.temaSelecionado + "startScene.css");
    }
    @FXML
    private void arrastar() {
        DraggableStage(top, (Stage) rootSS.getScene().getWindow());
    }
    @FXML
    private void minimize() {
        Stage s = (Stage) borderPane.getScene().getWindow();
        s.setIconified(true);
    }
    @FXML
    private void maximize() {
        Stage s = (Stage) borderPane.getScene().getWindow();
        if (s.isFullScreen()) {
            s.setFullScreen(false);

        }
        else {
            s.setFullScreen(true);
        }
    }
    @FXML
    private void close() {
        Stage s = (Stage) borderPane.getScene().getWindow();
        s.close();
    }
    static void filtroDeNumero(TextField tf, String a, String b) {
        if (!b.matches("\\d{0,9}([\\.]\\d{0,2})?")) {
            tf.setText(a);
        }
    }

    private void loadFXML(Parent p, String path) throws IOException {
        /*Testa se a janela ja está com esta tela aberta, se estiver nao
         * adiciona a tela, pois isso duplicaria o conteudo na janela.
         * Se a tela nao estiver aberta mas ter outro conteudo na janela,
         * limoa este conteudo para que somente a janela desejada apareca*/
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
    //Make a Stage Draggable
    public void DraggableStage(Node node, Stage stage) {
        double[] xOffset = {0}, yOffset = {0};
        node.setOnMousePressed(event -> {
            xOffset[0] = stage.getX() - event.getScreenX();
            yOffset[0] = stage.getY() - event.getScreenY();

        });

        node.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() + xOffset[0]);
            stage.setY(event.getScreenY() + yOffset[0]);

        });
    }

}
