package mercado.controllers;

import java.sql.*;

class BancoDeDados {

	private Connection conexao;
	private String url = "jdbc:sqlite:src/mercado/resources/bancodedados.sbd.db";
    private static class Criador {
        private static final BancoDeDados INSTANCE = new BancoDeDados();
    }
    private BancoDeDados() {
        try {
            conectar();
        }
        catch (SQLException e) {
            System.out.println("Could not connect to " + url);
        }
    }
    static BancoDeDados getInstance() {
        return Criador.INSTANCE;
    }
    
    private void conectar() throws SQLException{
    if (conexao == null) {
      conexao = DriverManager.getConnection(url);
    }
    else {
      System.out.println("Já conectado");
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

    static class DataTypes {
        static final String TEXT = "TEXT";
        static final String CHAR = "CHAR";
        static final String VARCHAR = "VARCHAR";
        static final String BINARY = "BINARY";
        static final String VARBINARY = "BINARY";
        static final String TINYBLOB = "TINYBLOB";
        static final String TINYTEXT = "TINYTEXT";
        static final String BLOB = "BLOB";
        static final String MEDIUMTEXT = "MEDIUMTEXT";
        static final String LONGTEXT = "LONGTEXT";
        static final String LONGBLOB = "LONGBLOB";
        static final String BIT = "BIT";
        static final String TINYBIT = "TINYBIT";
        static final String BOOL = "BOOL";
        static final String BOOLEAN = "BOOLEAN";
        static final String MEDIUMINT = "MEDIUMINT";
        static final String INT = "INT";
        static final String INTEGER = "INTEGER";
        static final String BIGINT = "BIGINT";
        static final String FLOAT = "FLOAT";
        static final String DOUBLE = "DOUBLE";
        static final String DECIMAL = "DECIMAL";
        static final String DEC = "DEC";

        static String[] types = new String[] {TEXT, CHAR, VARCHAR, BINARY, VARBINARY, TINYBLOB, BLOB,
                MEDIUMTEXT, TINYTEXT, LONGTEXT, LONGBLOB, BIT, TINYBIT, BOOL, BOOLEAN, MEDIUMINT, INT,
                INTEGER, BIGINT, FLOAT, DOUBLE, DECIMAL, DEC};

        static String VARCHAR(int i) { return "varchar(" + i + ")"; }
        static String CHAR(int i) { return "char(" + i + ")";}
        static String BINARY(int i) {return "binary(" + i + ")";}
        static String VARBINARY(int i) {return "varbinary(" + i + ")";}
        static String TEXT(int i) {return "text(" + i + ")";}
        static String BLOB(int i) {return "blob(" + i + ")";}
        static String BIT(int i) {return "bit(" + i + ")";}
        static String TINYINT(int i) {return "TINYINT(" + i + ")";}
        static String SMALLINT(int i) {return "SMALLINT(" + i + ")";}
        static String MEDIUMINT(int i) {return "MEDIUMINT(" + i + ")";}
        static String INT(int i) {return "INT(" + i + ")";}
        static String INTEGER(int i) {return "INTEGER(" + i + ")";}
        static String BIGINT(int i) {return "BIGINT(" + i + ")";}
        static String FLOAT(int i) {return "FLAOT(" + i + ")";}
        static String FLOAT(int i, int j) {return "FLOAT(" + i + ", "+ j + ")";}
        static String DECIMAL(int i, int j) {return "FLOAT(" + i + ", "+ j + ")";}
        static String DEC(int i, int j) {return "FLOAT(" + i + ", "+ j + ")";}
    }
}