package com.mycompany.sistemapublico;

public class CentralControl {
    public static void procesarViaje(SistemaTarifario vehiculo, double km){
        vehiculo.mostrarRuta();
        System.out.println("Tarifa a cobrar: " + vehiculo.calcularTarifa(km)+" COP");
    }
    public static void main(String[] args) {
        SistemaTarifario miBus = new BusArticulado();
        SistemaTarifario miTeleferico = new Teleferico();
        
        procesarViaje(miBus, 5);
        procesarViaje(miTeleferico, 2);
    }
}
