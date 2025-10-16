package com.mycompany.puzzle;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        
        PuzzleModel modelo = new PuzzleModel(4, 5);
        PuzzleMesaView mesaView = new PuzzleMesaView(modelo);
        PuzzleController controlador = new PuzzleController(modelo, mesaView);

        JFrame frame = new JFrame("Puzzle Mesa");
        JMenuBar bar = new JMenuBar();

        
        JMenu archivo = new JMenu("Archivo");
        JMenuItem cargar = new JMenuItem("Cargar Imagen");
        JMenuItem salir = new JMenuItem("Salir");
        archivo.add(cargar);
        archivo.add(salir);

        
        JButton reiniciarBtn = new JButton("Reiniciar");
        JButton sacarFueraBtn = new JButton("Sacar fuera las fichas no fijas");

        bar.add(archivo);
        bar.add(reiniciarBtn);
        bar.add(sacarFueraBtn);

        frame.setJMenuBar(bar);

        cargar.addActionListener(e -> {
            String[] options = {"20", "40", "60", "80", "100"};
            String piezasSeleccionadas = (String) JOptionPane.showInputDialog(
                    frame,
                    "¿En cuántas piezas quieres dividir el puzzle?",
                    "Selecciona cantidad de piezas",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]
            );
            if (piezasSeleccionadas != null) {
                int totalPiezas = Integer.parseInt(piezasSeleccionadas);

               
                int filas = 1, columnas = totalPiezas;
                for (int i = (int)Math.sqrt(totalPiezas); i >= 1; i--) {
                    if (totalPiezas % i == 0) {
                        filas = i;
                        columnas = totalPiezas / i;
                        break;
                    }
                }
                modelo.setFilas(filas);
                modelo.setColumnas(columnas);

                controlador.cargarImagen(frame);
            }
        });

        reiniciarBtn.addActionListener(e -> controlador.reiniciarPuzzle());
        sacarFueraBtn.addActionListener(e -> controlador.sacarNoFijasFuera());
        salir.addActionListener(e -> System.exit(0));

        frame.add(mesaView);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}