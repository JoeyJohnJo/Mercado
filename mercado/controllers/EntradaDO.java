package mercado.controllers;

public class EntradaDO {
    private String data, horario, produto;
    private double preco;
    private int quantidade;

    public EntradaDO(String data, String horario, String produto, double preco, int quantidade) {
        this.data = data;
        this.horario = horario;
        this.produto = produto;
        this.preco = preco;
        this.quantidade = quantidade;
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

    public int getQuantidade() {
        return quantidade;
    }
}
