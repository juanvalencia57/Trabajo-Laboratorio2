package com.mycompany.sistemapublico;

public class BusArticulado implements SistemaTarifario {
    @Override
    public double calcularTarifa(double distancia) {
        return 2950;
    }

    @Override
    public void mostrarRuta() {
        System.out.println("Ruta troncal (estandar de $2950 COP)");
    }
    
}
