package mercado.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import mercado.Driver;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RendaGastos {

    @FXML StackPane rootRG;
    @FXML VBox vbRenda, vbGasto;
    @FXML Label labelRenda, labelGasto, labelLiquido;

    @FXML
    private void initialize() {
        Driver.roots.add(rootRG);
        rootRG.getStylesheets().add(Configuracoes.temaSelecionado + "rendaGastos.css");
    }

    @FXML
    private void rendaDiaria() {
        double total = 0;
        String day = DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDateTime.now());
        BancoDeDados bd = BancoDeDados.getInstance();

        try {
            ResultSet rs = bd.select("SELECT * FROM HistVendas WHERE Data = \"" + day + "\";");
            while (rs.next()) {
                total += rs.getDouble("ValorFinal");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        labelRenda.setText("Renda de Hoje: R$ " + total);
    }
    @FXML
    private void rendaSemanal() {
        double total = 0;
        BancoDeDados bd = BancoDeDados.getInstance();
        LocalDateTime now = LocalDateTime.now();
        DayOfWeek dayOfWeek = now.getDayOfWeek();
        int dayOfMonth = now.getDayOfMonth();
        System.out.println(dayOfWeek.getValue());
        int i = dayOfWeek.getValue();

        if (i == 1) rendaDiaria();
        else {
            while (i > 0) {
                String data = dayOfMonth + DateTimeFormatter.ofPattern("/MM/yyyy").format(now);
                System.out.println(data);
                try {
                    ResultSet rs = bd.select("SELECT * FROM HistVendas WHERE Data = \"" + data + "\";");
                    while (rs.next()) {
                        total += rs.getDouble("ValorFinal");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                dayOfMonth--;
                i--;
            }
        }
        labelRenda.setText("Renda da Semana: R$ " + total);
    }
    @FXML
    private void rendaMensal() {
        double total = 0;
        BancoDeDados bd = BancoDeDados.getInstance();
        LocalDateTime now = LocalDateTime.now();
        int dayOfMonth = now.getDayOfMonth();
        int i = dayOfMonth;

        if (i == 1) rendaDiaria();
        else {
            while (i > 0) {
                String data = dayOfMonth + DateTimeFormatter.ofPattern("/MM/yyyy").format(now);
                System.out.println(data);
                try {
                    ResultSet rs = bd.select("SELECT * FROM HistVendas WHERE Data = \"" + data + "\";");
                    while (rs.next()) {
                        total += rs.getDouble("ValorFinal");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                dayOfMonth--;
                i--;
            }
        }
        labelRenda.setText("Renda do Mês: R$ " + total);
    }
    @FXML
    private void rendaTodosOsTempos() {
        BancoDeDados bd = BancoDeDados.getInstance();
        double total = 0;
        try {
            ResultSet rs = bd.selectAll("HistVendas");
            while (rs.next()) {
                total += rs.getDouble("ValorFinal");
            }
        } catch (SQLException e) { e.printStackTrace();}

        labelRenda.setText("Renda Total: R$ " + total);
    }

    @FXML
    private void gastoDiario() {
        double total = 0;
        String day = DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDateTime.now());
        BancoDeDados bd = BancoDeDados.getInstance();

        try {
            ResultSet rs = bd.select("SELECT * FROM HistEstoque WHERE Data = \"" + day + "\";");
            while (rs.next()) {
                total += rs.getDouble("PrecoCompra");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        labelGasto.setText("Gasto De Hoje: R$ " + total);
    }
    @FXML
    private void gastoSemanal() {
        double total = 0;
        BancoDeDados bd = BancoDeDados.getInstance();
        LocalDateTime now = LocalDateTime.now();
        DayOfWeek dayOfWeek = now.getDayOfWeek();
        int dayOfMonth = now.getDayOfMonth();
        System.out.println(dayOfWeek.getValue());
        int i = dayOfWeek.getValue();

        if (i == 1) rendaDiaria();
        else {
            while (i > 0) {
                String data = dayOfMonth + DateTimeFormatter.ofPattern("/MM/yyyy").format(now);
                System.out.println(data);
                try {
                    ResultSet rs = bd.select("SELECT * FROM HistEstoque WHERE Data = \"" + data + "\";");
                    while (rs.next()) {
                        total += rs.getDouble("PrecoCompra");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                dayOfMonth--;
                i--;
            }
        }
        labelGasto.setText("Gasto da Semana: R$ " + total);
    }
    @FXML
    private void gastoMensal() {
        double total = 0;
        BancoDeDados bd = BancoDeDados.getInstance();
        LocalDateTime now = LocalDateTime.now();
        int dayOfMonth = now.getDayOfMonth();
        int i = dayOfMonth;

        if (i == 1) rendaDiaria();
        else {
            while (i > 0) {
                String data = dayOfMonth + DateTimeFormatter.ofPattern("/MM/yyyy").format(now);
                System.out.println(data);
                try {
                    ResultSet rs = bd.select("SELECT * FROM HistEstoque WHERE Data = \"" + data + "\";");
                    while (rs.next()) {
                        total += rs.getDouble("PrecoCompra");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                dayOfMonth--;
                i--;
            }
        }
        labelGasto.setText("Gasto do Mês: R$ " + total);
    }
    @FXML
    private void gastoTodosOsTempos() {
        BancoDeDados bd = BancoDeDados.getInstance();
        double total = 0;
        try {
            ResultSet rs = bd.selectAll("HistEstoque");
            while (rs.next()) {
                total += rs.getDouble("PrecoCompra");
            }
        } catch (SQLException e) { e.printStackTrace();}

        labelGasto.setText("Gasto Total: R$ " + total);
    }

    @FXML
    private void liquidoDiario() {
        gastoDiario();
        rendaDiaria();
        double liquido = Double.parseDouble(labelRenda.getText().substring(18)) - Double.parseDouble(labelGasto.getText().substring(18));
        labelLiquido.setText("Liquido: " + liquido);
    }
    @FXML
    private void liquidoSemanal() {
        gastoSemanal();
        rendaSemanal();
        double liquido = Double.parseDouble(labelRenda.getText().substring(20)) - Double.parseDouble(labelGasto.getText().substring(20));
        labelLiquido.setText("Liquido: " + liquido);
    }

    @FXML
    private void liquidoMensal() {
        gastoMensal();
        rendaMensal();
        double liquido = Double.parseDouble(labelRenda.getText().substring(17)) - Double.parseDouble(labelGasto.getText().substring(17));
        labelLiquido.setText("Liquido: " + liquido);

    }
    @FXML
    private void liquidoTodosOsTempos(){
        rendaTodosOsTempos();
        gastoTodosOsTempos();
        double liquido = Double.parseDouble(labelRenda.getText().substring(16)) - Double.parseDouble(labelGasto.getText().substring(16));
        labelLiquido.setText("Liquido: " + liquido);
    }
}
