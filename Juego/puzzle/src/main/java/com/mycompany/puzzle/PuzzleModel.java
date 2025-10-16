package com.mycompany.puzzle;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.ArrayList;

public class PuzzleModel {
    private BufferedImage imagenOriginal;
    private int filas;
    private int columnas;
    private int piezaAncho;
    private int piezaAlto;
    private List<PuzzlePieceModel> piezas;

    public PuzzleModel(int filas, int columnas) {
        this.filas = filas;
        this.columnas = columnas;
        this.piezas = new ArrayList<>();
    }

    public BufferedImage getImagenOriginal() { return imagenOriginal; }
    public void setImagenOriginal(BufferedImage imagenOriginal) { this.imagenOriginal = imagenOriginal; }
    public int getFilas() { return filas; }
    public void setFilas(int filas) { this.filas = filas; }
    public int getColumnas() { return columnas; }
    public void setColumnas(int columnas) { this.columnas = columnas; }
    public int getPiezaAncho() { return piezaAncho; }
    public void setPiezaAncho(int piezaAncho) { this.piezaAncho = piezaAncho; }
    public int getPiezaAlto() { return piezaAlto; }
    public void setPiezaAlto(int piezaAlto) { this.piezaAlto = piezaAlto; }
    public List<PuzzlePieceModel> getPiezas() { return piezas; }
    public void setPiezas(List<PuzzlePieceModel> piezas) { this.piezas = piezas; }
}