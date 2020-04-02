package mercado.controllers;

public class EntradaDO {
    private String data, horario, produto;
    private double preco;

    public EntradaDO(String data, String horario, String produto, double preco) {
        this.data = data;
        this.horario = horario;
        this.produto = produto;
        this.preco = preco;
    }

    public double getPreco() {
        return preco;
    }

    public String getData() {
        return data;
    }

    public String getHorario() {
        return horario;
    }

    public String getProduto() {
        return produto;
    }
}
