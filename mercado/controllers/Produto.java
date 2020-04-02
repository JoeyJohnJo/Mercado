package mercado.controllers;

public class Produto {
    private String nome, codigo;
    private double preco, precoCompra;
    private int quantidade;

    public String getNome(){return nome;}
    public String getCodigo(){return codigo;}
    public double getPreco(){return preco;}
    public int getQuantidade(){return quantidade;}
    public double getPrecoCompra(){return precoCompra;}

    public Produto (int quantidade, String nome) {
        this.quantidade = quantidade;
        this.nome = nome;
    }
    public Produto(String nome, String codigo, double preco, int quantidade) {
        this.nome = nome;
        this.codigo = codigo;
        this.preco = preco;
        this.quantidade = quantidade;
    }
    public Produto(String nome, String codigo, double preco, double precoCompra) {
        this.nome = nome;
        this.codigo = codigo;
        this.preco = preco;
        this.precoCompra = precoCompra;
    }
    public Produto(String nome, String codigo, double preco, double precoCompra, int quantidade) {
        this.nome = nome;
        this.codigo = codigo;
        this.preco = preco;
        this.precoCompra = precoCompra;
        this.quantidade = quantidade;
    }
    public Produto(String nome, int quantidade){
        this.nome = nome;
        this.quantidade = quantidade;
    }
}
