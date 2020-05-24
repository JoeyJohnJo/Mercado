package mercado.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import mercado.Driver;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import static java.lang.Double.parseDouble;
import static java.lang.String.format;
import static java.lang.String.valueOf;

public class RendaGastos {

    @FXML StackPane rootRG;
    @FXML Label labelDailyIn, labelDailyOut, labelDailyProfit, labelDailySales, labelDailyInsertions;
    @FXML Label labelWeeklyIn, labelWeeklyOut, labelWeeklyProfit, labelWeeklySales, labelWeeklyInsertions;
    @FXML Label labelMonthlyIn, labelMonthlyOut, labelMonthlyProfit, labelMonthlySales, labelMonthlyInsertions;
    @FXML Label labelAllTimeIn, labelAllTimeOut, labelAllTimeProfit, labelAllTimeSales, labelAllTimeInsertions;
    @FXML VBox dailyContainer, weeklyContainer, monthlyContainer, allTimeContainer;
    private int dailyChart = 0,
                weeklyChart = 0,
                monthlyChart = 0,
                allTimeChart = 0;
    HashMap<Integer, String> months = new HashMap<>();

    Map<Integer,String> chartTypes = new HashMap<>();

    @FXML
    private void initialize() {
        Driver.roots.add(rootRG);
        rootRG.getStylesheets().add(Configuracoes.temaSelecionado + "rendaGastos.css");
        dailyContainer.setMinHeight(400);
        weeklyContainer.setMinHeight(400);
        monthlyContainer.setMinHeight(400);
        allTimeContainer.setMinHeight(400);
        populateMonthsMap();
        populateChartsMap();
        dailyChart();
        weeklyChart();
        monthlyChart();
        allTimeChart();
    }

    private void populateMonthsMap() {
        months.put(1, "Janeiro");
        months.put(2, "Fevereiro");
        months.put(3, "Março");
        months.put(4, "Abril");
        months.put(5, "Maio");
        months.put(6, "Junho");
        months.put(7, "Julho");
        months.put(8, "Agosto");
        months.put(9, "Setembro");
        months.put(10, "October");
        months.put(11, "Novembro");
        months.put(12, "Dezembro");
    }
    private void populateChartsMap() {
        chartTypes.put(0,"BarChart");
        chartTypes.put(1, "LineChart");
        chartTypes.put(2, "ScatterChart");
        chartTypes.put(3,"AreaChart");
        chartTypes.put(4, "StackedAreaChart");
        chartTypes.put(5, "StackedBarChart");
    }
    void dailyChart() {
        dailyContainer.getChildren().add(chartData(rendaDiaria(), entradasDiaria(),
                chartTypes.get(dailyChart)));
        labelDailyProfit.setText(
                valueOf( parseDouble(labelDailyIn.getText()) - parseDouble(labelDailyOut.getText()) ));
    }
    void weeklyChart() {
        weeklyContainer.getChildren().add(chartData(rendaSemanal(), entradasSemanal(),
                chartTypes.get(weeklyChart)));
        labelWeeklyProfit.setText(
                valueOf( parseDouble(labelWeeklyIn.getText()) - parseDouble(labelWeeklyOut.getText()) ));
    }
    void monthlyChart() {
        monthlyContainer.getChildren().add(chartData(rendaMensal(), entradasMensal(),
                chartTypes.get(monthlyChart)));
        labelMonthlyProfit.setText(
                valueOf( parseDouble(labelMonthlyIn.getText()) - parseDouble(labelMonthlyOut.getText()) ));
    }
    void allTimeChart() {
        allTimeContainer.getChildren().add(chartData(rendaTodosOsTempos(),
                entradasTodosOsTempos(), chartTypes.get(allTimeChart)));
        labelAllTimeProfit.setText(
                valueOf( parseDouble(labelAllTimeIn.getText()) - parseDouble(labelAllTimeOut.getText()) ));
    }

    private Chart chartData(Map<String, Double> sales, Map<String, Double> in, String chartType) {
        Object chart = null;
        Double greatestSale = 0.0;
        Double greatestCost = 0.0;
        Double greatestOverall = 0.0;
        ObservableList<String> categories = FXCollections.observableArrayList();
        categories.addAll(sales.keySet());

        Series salesSeries = new Series();
        salesSeries.setName("Vendas");
        for (Map.Entry<String, Double> entry : sales.entrySet()) {
            Double v = entry.getValue();
            String k = entry.getKey();
            if (greatestSale < v) greatestSale = v;

            salesSeries.getData().add(new XYChart.Data(k, v));
        }

        Series inSeries = new Series();
        inSeries.setName("Entradas");
        for (Map.Entry<String, Double> entry : in.entrySet()) {
            Double v = entry.getValue();
            String k = entry.getKey();
            if (greatestCost < v) greatestCost = v;

            if (!categories.contains(k)) categories.add(k);

            inSeries.getData().add(new XYChart.Data(k, v));
        }
        greatestOverall = greatestCost > greatestSale ? greatestCost : greatestSale;
        NumberAxis yAxis = new NumberAxis(0, greatestOverall, 0.1*greatestOverall);
        CategoryAxis xAxis = new CategoryAxis(categories);

        yAxis.setLabel("Renda/Gastos");
        xAxis.setLabel("Tempo");

        try {
            String fullClassName = "javafx.scene.chart." + chartType;
            Class<?> cl = Class.forName(fullClassName);
            Constructor<?> cons = cl.getConstructor(Axis.class, Axis.class);
            chart = cons.newInstance(xAxis, yAxis);
        } catch (ClassNotFoundException | NoSuchMethodException |
                InvocationTargetException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }

        ((XYChart) chart).getData().add(salesSeries);
        ((XYChart) chart).getData().add(inSeries);
        return ((XYChart) chart);
    }

    private Map<String, Double> rendaDiaria() {
        Map<String, Double> data = new LinkedHashMap<>();
        String day = DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDateTime.now());
        BancoDeDados bd = BancoDeDados.getInstance();
        double dailyEarnings = 0;
        int quantityOfSales = 0;

        try {
            ResultSet rs = bd.select("SELECT * FROM HistVendas WHERE Data = \"" + day + "\";");
            while (rs.next()) {
                double amount = rs.getDouble("ValorFinal");
                data.put(rs.getString("Horario"), amount);
                dailyEarnings+=amount;
                quantityOfSales++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(data);
        System.out.println("Total in day: " + dailyEarnings);
        labelDailyIn.setText(String.valueOf(dailyEarnings));
        labelDailySales.setText(String.valueOf(quantityOfSales));
        return data;
    }

    private Map<String, Double> rendaSemanal() {
        Map<String, Double> map = new LinkedHashMap<>();
        BancoDeDados bd = BancoDeDados.getInstance();
        LocalDateTime now = LocalDateTime.now();
        DayOfWeek dayOfWeek = now.getDayOfWeek();
        System.out.println("Today is: " + dayOfWeek);
        int dayOfMonth = now.getDayOfMonth();
        System.out.printf("Today is the %d th day of the month\n", dayOfMonth);
        System.out.println("Day of week: " + dayOfWeek.getValue());
        int currentDayInWeek = dayOfWeek.getValue();
        int fistDayOfWeekInMonth = dayOfMonth - currentDayInWeek+1;
        int lastDayOfWeekInMonth = fistDayOfWeekInMonth + 6;
        double earnedInWeek = 0;
        int salesInWeek = 0;
        System.out.println("The first day of this week was on the " + fistDayOfWeekInMonth);

        if (currentDayInWeek == 1) return rendaDiaria();
        int dayToGetInDB = fistDayOfWeekInMonth;
        while (dayToGetInDB <= lastDayOfWeekInMonth) {

            double earningInDay = 0;
            String data = dayToGetInDB + DateTimeFormatter.ofPattern("/MM/yyyy").format(now);
            System.out.println("Fetching date: " + data);
            try {
                ResultSet rs = bd.select("SELECT * FROM HistVendas WHERE Data = \"" + data + "\";");
                while (rs.next()) {
                    earningInDay += rs.getDouble("ValorFinal");
                    earnedInWeek += rs.getDouble("ValorFinal");
                    salesInWeek++;
                }
                map.put(data, earningInDay);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            dayToGetInDB++;
        }
        labelWeeklyIn.setText(valueOf(earnedInWeek));
        labelWeeklySales.setText(valueOf(salesInWeek));
        return map;
    }

    //Renda do mes dividida em 4 semanas
    private Map<String, Double> rendaMensal() {
        BancoDeDados bd = BancoDeDados.getInstance();
        Map<String, Double> map = new LinkedHashMap<>();
        double weekly = 0;
        int currentWeek = 0;
        double earningsInMonth = 0;
        int quantityOfSales = 0;
        for (int i = 1; i < 31; i++) {
            String data = i + DateTimeFormatter.ofPattern("/MM/yyyy").format(LocalDateTime.now());
            try {
                ResultSet rs = bd.select("SELECT * FROM HistVendas WHERE Data = \"" + data + "\";");
                while (rs.next()) {
                    weekly += rs.getDouble("ValorFinal");
                    earningsInMonth += rs.getDouble("ValorFinal");
                    quantityOfSales++;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (i % 7 == 0 ) {
                currentWeek++;
                if (currentWeek < 4) {
                    map.put(format("Semana %d", currentWeek), weekly);
                    weekly = 0;
                }
            }
            else if (i >= 28) {
                map.put(format("Semana %d", 4), weekly);
            }

        }
        System.out.println(map);
        labelMonthlyIn.setText(valueOf(earningsInMonth));
        labelMonthlySales.setText(valueOf(quantityOfSales));
        return map;
    }

    //Renda de todos os tempos dividida em meses
    private Map<String, Double> rendaTodosOsTempos() {
        BancoDeDados bd = BancoDeDados.getInstance();
        Map<String, Double> map = new LinkedHashMap<>();
        String mapKey = "";
        String previousMonth = "";
        String previousYear = "";
        double monthlyRev = 0;
        double allTimeIn = 0;
        int resultSetSize = 0;
        try {
            //Tem que fazer dois loops para descobrir quantas entradas tem
            // porque o suporte pra SQLite é limitado, mas aproveitando isso
            //ja da pra descobrir o valor total de todos os tempos e a quantidade de vendas
            ResultSet rsB = bd.selectAll("HistVendas");
            int currentIndex = 0;

            while (rsB.next()) {
                resultSetSize++;
                allTimeIn += rsB.getDouble("ValorFinal");
            }

            ResultSet rs = bd.selectAll("HistVendas");

            while (rs.next())
            {
                String currentMonth = rs.getString("Data").substring(3, 5);

                if (!previousMonth.equals(currentMonth)) {
                    //Na primeira iteração, currentMonth é vazio ent vai vir aqui, mas
                    //não podemos adicionar um mês vazio
                    if (previousMonth.trim().length() != 0) {
                        //Nome do mês + ano
                        mapKey = months.get(Integer.parseInt(previousMonth)) + " - " + previousYear;
                        map.put(mapKey, monthlyRev);
                    }
                    //Após adicionar, reinicia a data e renda
                    previousYear = rs.getString("Data").substring(6);
                    previousMonth = rs.getString("Data").substring(3, 5);
                    monthlyRev = 0;
                }
                monthlyRev += rs.getDouble("ValorFinal");
                currentIndex++;
                if (currentIndex == resultSetSize) {
                    mapKey = months.get(Integer.parseInt(previousMonth)) + " - " + previousYear;
                    map.put(mapKey, monthlyRev);
                }
            }
        } catch (SQLException e) { e.printStackTrace();}

        System.out.println(map);
        labelAllTimeSales.setText(valueOf(resultSetSize));
        labelAllTimeIn.setText(valueOf(allTimeIn));
        return map;
    }

    private Map<String, Double> entradasDiaria() {
        Map<String, Double> map = new LinkedHashMap<>();
        String day = DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDateTime.now());
        BancoDeDados bd = BancoDeDados.getInstance();
        double dailyCosts = 0;
        int quantityOfEntries = 0;

        try {
            ResultSet rs = bd.select("SELECT * FROM HistEstoque WHERE Data = \"" + day + "\";");
            while (rs.next()) {
                double price = rs.getDouble("PrecoCompra");
                int quantity = rs.getInt("Quantidade");
                map.put(rs.getString("Horario"), (price * quantity));
                dailyCosts+=(price * quantity);
                quantityOfEntries++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(map);
        System.out.println("Total spent in day: " + dailyCosts);
        labelDailyOut.setText(String.valueOf(dailyCosts));
        labelDailyInsertions.setText(String.valueOf(quantityOfEntries));
        return map;
    }

    private Map<String, Double> entradasSemanal() {
        Map<String, Double> map = new LinkedHashMap<>();
        BancoDeDados bd = BancoDeDados.getInstance();
        LocalDateTime now = LocalDateTime.now();
        DayOfWeek dayOfWeek = now.getDayOfWeek();
        System.out.println("Today is: " + dayOfWeek);
        int dayOfMonth = now.getDayOfMonth();
        System.out.printf("Today is the %d th day of the month\n", dayOfMonth);
        System.out.println("Day of week: " + dayOfWeek.getValue());
        int currentDayInWeek = dayOfWeek.getValue();
        int fistDayOfWeekInMonth = dayOfMonth - currentDayInWeek+1;
        int lastDayOfWeekInMonth = fistDayOfWeekInMonth + 6;
        double spentInWeek = 0;
        int insertionsInWeek = 0;
        System.out.println("The first day of this week was on the " + fistDayOfWeekInMonth);

        if (currentDayInWeek == 1) return entradasDiaria();
        int dayToGetInDB = fistDayOfWeekInMonth;
        while (dayToGetInDB <= lastDayOfWeekInMonth) {

            double spentInDay = 0;
            String data = dayToGetInDB + DateTimeFormatter.ofPattern("/MM/yyyy").format(now);
            System.out.println("Fetching date: " + data);
            try {
                ResultSet rs = bd.select("SELECT * FROM HistEstoque WHERE Data = \"" + data + "\";");
                while (rs.next()) {
                    spentInDay += (rs.getDouble("PrecoCompra") * rs.getInt("Quantidade"));
                    spentInWeek += (rs.getDouble("PrecoCompra") * rs.getInt("Quantidade"));
                    insertionsInWeek++;
                }
                map.put(data, spentInDay);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            dayToGetInDB++;
        }
        labelWeeklyOut.setText(valueOf(spentInWeek));
        labelWeeklyInsertions.setText(valueOf(insertionsInWeek));
        return map;
    }

    private Map<String, Double> entradasMensal() {
        BancoDeDados bd = BancoDeDados.getInstance();
        Map<String, Double> map = new LinkedHashMap<>();
        double weekly = 0;
        int currentWeek = 0;
        int quantityOfInsertions = 0;
        double totalSpent = 0;
        for (int i = 1; i < 31; i++) {
            String data = i + DateTimeFormatter.ofPattern("/MM/yyyy").format(LocalDateTime.now());
            try {
                ResultSet rs = bd.select("SELECT * FROM HistEstoque WHERE Data = \"" + data + "\";");
                while (rs.next()) {
                    weekly += (rs.getDouble("PrecoCompra") * rs.getInt("Quantidade") );
                    totalSpent += (rs.getDouble("PrecoCompra") * rs.getInt("Quantidade") );
                    quantityOfInsertions++;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (i % 7 == 0 ) {
                currentWeek++;
                if (currentWeek < 4) {
                    map.put(format("Semana %d", currentWeek), weekly);
                    weekly = 0;
                }
            }
            else if (i >= 28) {
                map.put(format("Semana %d", 4), weekly);
            }

        }
        System.out.println(map);
        labelMonthlyOut.setText(valueOf(totalSpent));
        labelMonthlyInsertions.setText(valueOf(quantityOfInsertions));
        return map;
    }

    private Map<String, Double> entradasTodosOsTempos() {
        BancoDeDados bd = BancoDeDados.getInstance();
        Map<String, Double> map = new LinkedHashMap<>();
        String mapKey = "";
        String previousMonth = "";
        String previousYear = "";
        double monthlySpendings = 0;
        double allTimeOut = 0;
        int resultSetSize = 0;
        try {
            //Tem que fazer dois loops para descobrir quantas entradas tem
            // porque o suporte pra SQLite é limitado, mas aproveitando isso
            //ja da pra descobrir o valor total de todos os tempos e a quantidade de vendas
            ResultSet rsB = bd.selectAll("HistEstoque");
            int currentIndex = 0;
            while (rsB.next()) {
                resultSetSize++;
                allTimeOut += (rsB.getDouble("PrecoCompra") * rsB.getInt("Quantidade"));
            }

            ResultSet rs = bd.selectAll("HistEstoque");

            while (rs.next())
            {
                String currentMonth = rs.getString("Data").substring(3, 5);

                if (!previousMonth.equals(currentMonth)) {
                    //Na primeira iteração, currentMonth é vazio ent vai vir aqui, mas
                    //não podemos adicionar um mês vazio
                    if (previousMonth.trim().length() != 0) {
                        //Nome do mês + ano
                        mapKey = months.get(Integer.parseInt(previousMonth)) + " - " + previousYear;
                        map.put(mapKey, monthlySpendings);
                    }
                    //Após adicionar, reinicia a data e renda
                    previousYear = rs.getString("Data").substring(6);
                    previousMonth = rs.getString("Data").substring(3, 5);
                    monthlySpendings = 0;
                }
                monthlySpendings += (rs.getDouble("PrecoCompra") * rs.getInt("Quantidade"));
                currentIndex++;
                if (currentIndex == resultSetSize) {
                    mapKey = months.get(Integer.parseInt(previousMonth)) + " - " + previousYear;
                    map.put(mapKey, monthlySpendings);
                }
            }
        } catch (SQLException e) { e.printStackTrace();}

        System.out.println(map);
        labelAllTimeInsertions.setText(valueOf(resultSetSize));
        labelAllTimeOut.setText(valueOf(allTimeOut));
        return map;
    }

    @FXML void nextDaily() {
        if (dailyChart == 5) return;
        dailyContainer.getChildren().remove(0);
        dailyChart++;
        dailyChart();
    }
    @FXML void nextWeekly() {
        if (weeklyChart == 5) return;
        weeklyContainer.getChildren().remove(0);
        weeklyChart++;
        weeklyChart();
    }
    @FXML void nextMonthly() {
        if (monthlyChart == 5) return;
        monthlyContainer.getChildren().remove(0);
        monthlyChart++;
        monthlyChart();
    }
    @FXML void nextAllTime() {
        if (allTimeChart == 5) return;
        allTimeContainer.getChildren().remove(0);
        allTimeChart++;
        allTimeChart();
    }

    @FXML void previousDaily() {
        if (dailyChart == 0) return;
        dailyContainer.getChildren().remove(0);
        dailyChart--;
        dailyChart();
    }
    @FXML void previousWeekly() {
        if (weeklyChart == 0) return;
        weeklyContainer.getChildren().remove(0);
        weeklyChart--;
        weeklyChart();
    }
    @FXML void previousMonthly() {
        if (monthlyChart == 0) return;
        monthlyContainer.getChildren().remove(0);
        monthlyChart--;
        monthlyChart();
    }
    @FXML void previousAllTime() {
        if (allTimeChart == 0) return;
        allTimeContainer.getChildren().remove(0);
        allTimeChart--;
        allTimeChart();
    }
}
