package mercado.controllers;

public class VendaDO {

    private int id;
    private double valorFinal;
    private String data, horario;

    VendaDO(int id, String data, String horario, double valorFinal) {
        this.id = id;
        this.data = data;
        this.horario = horario;
        this.valorFinal = valorFinal;
    }

    public double getValorFinal() {return valorFinal;}
    public String getData() {return data;}
    public String getHorario() {return horario;}
    public int getId() {return id;}

}
