package mercado.controllers;
import java.sql.*;

class BancoDeDados {

	private Connection conexao;
	private String url = "jdbc:sqlite:" + System.getProperty("user.home") + "\\Mercado\\bancodedados.sbd.db";

	private static class Criador {
        private static final BancoDeDados INSTANCE = new BancoDeDados();
    }
    private BancoDeDados() {
        conectar();
    }
    static BancoDeDados getInstance() {
        return Criador.INSTANCE;
    }
    
    private void conectar(){
        System.out.println(url);
        try {
            if (conexao == null) {
                conexao = DriverManager.getConnection(url);
                if (conexao != null) {
                    System.out.println("connected");
                }
            }
            else {
                System.out.println("Já conectado");
            }
        } catch (SQLException e) {
            System.out.println("could not connect");
            System.out.println("Connection object is:" + conexao);
        }
    }
    
    ResultSet select(String command) throws SQLException{
        Statement statement = conexao.createStatement();
        return statement.executeQuery(command);
    }
    
    void atualizar(String command) throws SQLException{
        Statement statement = conexao.createStatement();
        statement.executeUpdate(command);
    }

    void insertColumnInto(String tableName, String column, String...values) {
        for (String s : values) {
            try {
                atualizar("INSERT INTO " + tableName + " (" + column + ") VALUES (\" " + s + "\");");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    void insertRowInto(String tableName, String...values) {
        String query = "INSERT INTO " + tableName + " VALUES (\"";
        int valuesSize = values.length;
        System.out.println("Length is " + valuesSize );

        for (int currentValue = 0; currentValue < values.length; currentValue++) {
            if (currentValue == (valuesSize - 1)) query += values[currentValue] + "\");";
            else query += values[currentValue] + "\", \"";
        }
        System.out.println(query);
        try {
            atualizar(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    ResultSet selectAll(String tableName) throws SQLException {
        return select("SELECT * FROM " + tableName + ";");
    }
    public void createTable(String tableName, String...columns) {
        if (columns.length % 2 != 0) {
            System.out.println("Error: Wrong number of arguments.\nDid you forget to specify a column's data type? ");
        }
        else {
            String query = "CREATE TABLE " + tableName + " ( ";
            for (int i = 0; i < columns.length; i++) {
                if (i == (columns.length -1)) {
                    query += columns[i] + " );";
                }
                else {
                    if ((i % 2) == 0 ) {
                        query  += columns[i] + " ";
                    }
                    else query += columns[i] + ", ";}

            }
            try {
                atualizar(query);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            System.out.println(query);
        }

    }

}