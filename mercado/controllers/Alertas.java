package mercado.controllers;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Alertas {

    BancoDeDados bd = BancoDeDados.getInstance();
    ArrayList<Produto> produtos;
    public static ArrayList<HBox> visuals;

    public Alertas() {
        visuals = new ArrayList<>(criarAlertas());
    }
    private ArrayList<Produto> buscarProdutos() {
        ArrayList<Produto> produtos = new ArrayList<>();
        try {
            ResultSet rs;
            rs = bd.select("SELECT * FROM Estoque");
            while (rs.next()) {
                produtos.add(new Produto(rs.getString("Nome"), rs.getInt("Quantidade")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return produtos;
    }
    private ArrayList<HBox> criarAlertas() {
        produtos = new ArrayList<>(buscarProdutos());
        ArrayList<HBox> visuals = new ArrayList<>();
        for (Produto p : produtos) {
            if (p.getQuantidade() <= Configuracoes.qtdAlerta) {
                visuals.add(new Componente(p));
            }
        }
        return visuals;
    }
    public ArrayList<HBox> getAlertas() {return visuals;}
    private class Componente extends HBox {
        Componente(Produto p){
            super();
            this.setId("alerta");
            this.getStylesheets().add(Configuracoes.temaSelecionado+ "config.css");
            Label l = new Label("ATENÇÃO! O produto " + p.getNome() + " está em quantidade abaixo de " + Configuracoes.qtdAlerta);
            ImageView imagem = new ImageView(new Image(getClass().getResourceAsStream("/mercado/resources/images/alerta.png"),
                    70, 70, false, false));
            l.setMinSize(900,70);
            l.setId("textoAlerta");
            this.getChildren().addAll(imagem, l);
        }
    }
}
