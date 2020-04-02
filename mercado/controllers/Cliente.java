package mercado.controllers;

public class Cliente {
    private String nome, endereco, telefone;

    public Cliente(String nome, String endereco, String telefone) {
        this.nome = nome;
        this.endereco = endereco;
        this.telefone = telefone;
    }
    public String getTelefone() {return telefone;}
    public String getNome() {return nome;}
    public String getEndereco() {return endereco;}
}
