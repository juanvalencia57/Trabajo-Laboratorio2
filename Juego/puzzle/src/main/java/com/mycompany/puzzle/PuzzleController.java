package com.mycompany.puzzle;

import javax.swing.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.awt.Rectangle;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Collections;

public class PuzzleController {
    private PuzzleModel modelo;
    private PuzzleMesaView mesaView;

    public PuzzleController(PuzzleModel modelo, PuzzleMesaView mesaView) {
        this.modelo = modelo;
        this.mesaView = mesaView;
        mesaView.setControlador(this);
    }

    public void cargarImagen(JFrame frame) {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
            try {
                BufferedImage imagen = ImageIO.read(chooser.getSelectedFile());
                modelo.setImagenOriginal(imagen);

                Rectangle tablero = mesaView.getRectTablero();
                int filas = modelo.getFilas();
                int columnas = modelo.getColumnas();
                int piezaAncho = tablero.width / columnas;
                int piezaAlto = tablero.height / filas;
                modelo.setPiezaAncho(piezaAncho);
                modelo.setPiezaAlto(piezaAlto);

               
                Image imagenEscalada = imagen.getScaledInstance(
                    piezaAncho * columnas,
                    piezaAlto * filas,
                    Image.SCALE_SMOOTH
                );
                BufferedImage imgFinal = new BufferedImage(
                    piezaAncho * columnas,
                    piezaAlto * filas,
                    BufferedImage.TYPE_INT_ARGB
                );
                imgFinal.getGraphics().drawImage(imagenEscalada, 0, 0, null);

                ArrayList<PuzzlePieceModel> piezas = new ArrayList<>();
                for (int y = 0; y < filas; y++) {
                    for (int x = 0; x < columnas; x++) {
                        BufferedImage sub = imgFinal.getSubimage(
                            x * piezaAncho, y * piezaAlto, piezaAncho, piezaAlto
                        );
                        double posX, posY;
                        if (x % 2 == 0) {
                            posX = tablero.x - piezaAncho - 30 - Math.random() * 40;
                            posY = tablero.y + y * piezaAlto;
                        } else {
                            posX = tablero.x + tablero.width + 30 + Math.random() * 40;
                            posY = tablero.y + y * piezaAlto;
                        }
                        piezas.add(new PuzzlePieceModel(sub, x, y, posX, posY));
                    }
                }
                Collections.shuffle(piezas);
                modelo.setPiezas(piezas);
                mesaView.repaint();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "No se pudo cargar la imagen.");
            }
        }
    }

    
    public void formatearDatosYNuevoPuzzle(JFrame frame) {
        modelo.setImagenOriginal(null);
        modelo.getPiezas().clear();
        mesaView.repaint();
        cargarImagen(frame);
    }

   
    public void reiniciarPuzzle() {
        Rectangle tablero = mesaView.getRectTablero();
        int filas = modelo.getFilas();
        int columnas = modelo.getColumnas();
        int piezaAncho = modelo.getPiezaAncho();
        int piezaAlto = modelo.getPiezaAlto();

        ArrayList<PuzzlePieceModel> piezas = new ArrayList<>(modelo.getPiezas());
        for (PuzzlePieceModel pieza : piezas) {
            pieza.setFija(false);
            pieza.setSobreTablero(false);
            double posX, posY;
            int x = pieza.getOriginalX();
            int y = pieza.getOriginalY();
            if (x % 2 == 0) {
                posX = tablero.x - piezaAncho - 30 - Math.random() * 40;
                posY = tablero.y + y * piezaAlto;
            } else {
                posX = tablero.x + tablero.width + 30 + Math.random() * 40;
                posY = tablero.y + y * piezaAlto;
            }
            pieza.setPos(posX, posY);
        }
        Collections.shuffle(piezas);
        modelo.setPiezas(piezas);
        mesaView.repaint();
    }

   
    public void sacarNoFijasFuera() {
        Rectangle tablero = mesaView.getRectTablero();
        int piezaAncho = modelo.getPiezaAncho();
        int piezaAlto = modelo.getPiezaAlto();

        int idx = 0;
        for (PuzzlePieceModel pieza : modelo.getPiezas()) {
            if (!pieza.isFija()) {
                double posX, posY;
                if (idx % 2 == 0) {
                    posX = tablero.x - piezaAncho - 30 - Math.random()*40;
                    posY = tablero.y + (idx * piezaAlto) % tablero.height;
                } else {
                    posX = tablero.x + tablero.width + 30 + Math.random()*40;
                    posY = tablero.y + (idx * piezaAlto) % tablero.height;
                }
                pieza.setPos(posX, posY);
                pieza.setSobreTablero(false);
                idx++;
            }
        }
        mesaView.repaint();
    }
}