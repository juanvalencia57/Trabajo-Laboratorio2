package com.mycompany.sistemapublico;

public class Teleferico implements SistemaTarifario {

    @Override
    public double calcularTarifa(double distancia) {
        return 1000+500*distancia;
    }

    @Override
    public void mostrarRuta() {
        System.out.println("Ruta de conexión veredal (tarifa variable, base $1000 COP)");
    }
    
}
