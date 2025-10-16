package com.mycompany.puzzle;

import java.awt.image.BufferedImage;

public class PuzzlePieceModel {
    private BufferedImage imagen;
    private double posX, posY;
    private final int originalX, originalY;
    private boolean sobreTablero;
    private boolean fija = false;

    public PuzzlePieceModel(BufferedImage imagen, int originalX, int originalY, double posX, double posY) {
        this.imagen = imagen;
        this.originalX = originalX;
        this.originalY = originalY;
        this.posX = posX;
        this.posY = posY;
        this.sobreTablero = false;
    }

    public BufferedImage getImagen() { return imagen; }
    public double getPosX() { return posX; }
    public double getPosY() { return posY; }
    public void setPos(double x, double y) { this.posX = x; this.posY = y; }
    public int getOriginalX() { return originalX; }
    public int getOriginalY() { return originalY; }
    public boolean isSobreTablero() { return sobreTablero; }
    public void setSobreTablero(boolean value) { this.sobreTablero = value; }
    public boolean isFija() { return fija; }
    public void setFija(boolean fija) { this.fija = fija; }
}