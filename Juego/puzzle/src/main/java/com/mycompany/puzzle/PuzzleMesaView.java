package com.mycompany.puzzle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class PuzzleMesaView extends JPanel {
    private PuzzleModel modelo;
    private PuzzlePieceModel piezaArrastrada = null;
    private double offsetX, offsetY;
    private boolean mostrarOverlay = false;
    private double zoom = 1.0;
    private PuzzleController controlador; 
    private Rectangle miniaturaRect = null;

    private Rectangle rectTablero = new Rectangle(200, 100, 400, 400);

    public PuzzleMesaView(PuzzleModel modelo) {
        this.modelo = modelo;
        setPreferredSize(new Dimension(950, 650));
        setBackground(new Color(120, 70, 30));

        addMouseWheelListener(e -> {
            if (e.getPreciseWheelRotation() < 0) zoom *= 1.1;
            else zoom /= 1.1;
            zoom = Math.max(0.3, Math.min(zoom, 2.5));
            repaint();
        });

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (miniaturaRect != null && miniaturaRect.contains(e.getPoint())) {
                    mostrarImagenAmpliada();
                    return;
                }

                for (int i = modelo.getPiezas().size() - 1; i >= 0; i--) {
                    PuzzlePieceModel pieza = modelo.getPiezas().get(i);
                    if (pieza.isFija()) continue;
                    int px = (int)(pieza.getPosX() * zoom), py = (int)(pieza.getPosY() * zoom);
                    int w = (int)(modelo.getPiezaAncho() * zoom), h = (int)(modelo.getPiezaAlto() * zoom);
                    if (e.getX() >= px && e.getX() < px + w && e.getY() >= py && e.getY() < py + h) {
                        piezaArrastrada = pieza;
                        offsetX = e.getX() - px;
                        offsetY = e.getY() - py;
                        modelo.getPiezas().remove(i);
                        modelo.getPiezas().add(pieza);
                        repaint();
                        break;
                    }
                }
            }
            public void mouseReleased(MouseEvent e) {
                if (piezaArrastrada != null) {
                    int tableroX = (int)(rectTablero.x * zoom), tableroY = (int)(rectTablero.y * zoom);
                    int tableroW = (int)(rectTablero.width * zoom), tableroH = (int)(rectTablero.height * zoom);
                    Rectangle tablero = new Rectangle(tableroX, tableroY, tableroW, tableroH);
                    if (tablero.contains(e.getPoint())) {
                        int col = (int)((e.getX() - tableroX) / (modelo.getPiezaAncho() * zoom));
                        int fila = (int)((e.getY() - tableroY) / (modelo.getPiezaAlto() * zoom));
                        double nx = rectTablero.x + col * modelo.getPiezaAncho();
                        double ny = rectTablero.y + fila * modelo.getPiezaAlto();
                        piezaArrastrada.setPos(nx, ny);
                        piezaArrastrada.setSobreTablero(true);

                        if (col == piezaArrastrada.getOriginalX() && fila == piezaArrastrada.getOriginalY()) {
                            piezaArrastrada.setFija(true);
                        }
                    } else {
                        piezaArrastrada.setSobreTablero(false);
                        piezaArrastrada.setPos((e.getX() - offsetX) / zoom, (e.getY() - offsetY) / zoom);
                    }
                    piezaArrastrada = null;
                    repaint();

                    if (isPuzzleCompleto()) {
                        SwingUtilities.invokeLater(() -> mostrarDialogoCompletado());
                    }
                }
            }
            public void mouseClicked(MouseEvent e) {
                int tableroX = (int)(rectTablero.x * zoom), tableroY = (int)(rectTablero.y * zoom);
                int tableroW = (int)(rectTablero.width * zoom), tableroH = (int)(rectTablero.height * zoom);
                Rectangle tablero = new Rectangle(tableroX, tableroY, tableroW, tableroH);
                if (tablero.contains(e.getPoint()) && e.getClickCount() == 2) {
                    mostrarOverlay = !mostrarOverlay;
                    repaint();
                }
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                if (piezaArrastrada != null) {
                    piezaArrastrada.setPos((e.getX() - offsetX) / zoom, (e.getY() - offsetY) / zoom);
                    repaint();
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D)g.create();
        g2d.scale(zoom, zoom);

        g2d.setColor(new Color(0, 150, 60));
        g2d.fillRect(rectTablero.x, rectTablero.y, rectTablero.width, rectTablero.height);

        if (modelo.getPiezas() != null) {
            for (PuzzlePieceModel pieza : modelo.getPiezas()) {
                int px = (int)pieza.getPosX(), py = (int)pieza.getPosY();
                g2d.drawImage(pieza.getImagen(), px, py, modelo.getPiezaAncho(), modelo.getPiezaAlto(), this);
            }
        }

        if (mostrarOverlay && modelo.getImagenOriginal() != null) {
            Composite original = g2d.getComposite();
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.25f)); 
            g2d.drawImage(
                modelo.getImagenOriginal(),
                rectTablero.x, rectTablero.y,
                rectTablero.width, rectTablero.height,
                this
            );
            g2d.setComposite(original);
        }
        g2d.dispose();

        BufferedImage original = modelo.getImagenOriginal();
        if (original != null) {
            int margin = 12;
            int thumbWidth = 150;
            int thumbHeight = 120;
            int x = getWidth() - thumbWidth - margin;
            int y = margin;

            g.setColor(new Color(255,255,255,220));
            g.fillRoundRect(x-4, y-4, thumbWidth+8, thumbHeight+8, 14, 14);

            g.drawImage(original, x, y, thumbWidth, thumbHeight, null);

            g.setColor(Color.GRAY);
            g.drawRoundRect(x-4, y-4, thumbWidth+8, thumbHeight+8, 14, 14);

            this.miniaturaRect = new Rectangle(x-4, y-4, thumbWidth+8, thumbHeight+8);
        } else {
            this.miniaturaRect = null;
        }
    }

    private void mostrarImagenAmpliada() {
        BufferedImage original = modelo.getImagenOriginal();
        if (original == null) return;
        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), "Imagen Original", Dialog.ModalityType.MODELESS);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setSize( Math.min(original.getWidth()+32, 900), Math.min(original.getHeight()+64, 700) );
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                int w = getWidth(), h = getHeight();
                double ratio = Math.min((w-20.0)/original.getWidth(), (h-20.0)/original.getHeight());
                int imgW = (int)(original.getWidth() * ratio);
                int imgH = (int)(original.getHeight() * ratio);
                g.drawImage(original, (w-imgW)/2, (h-imgH)/2, imgW, imgH, null);
            }
        };
        dialog.setContentPane(panel);
        dialog.setVisible(true);
    }

    public boolean isPuzzleCompleto() {
        for (PuzzlePieceModel pieza : modelo.getPiezas()) {
            if (!pieza.isFija()) return false;
        }
        return true;
    }

    public void setControlador(PuzzleController controlador) {
        this.controlador = controlador;
    }

    private void mostrarDialogoCompletado() {
        int opcion = JOptionPane.showOptionDialog(
            this,
            "¡Has completado el puzzle!\n¿Qué quieres hacer ahora?",
            "¡Felicidades!",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.INFORMATION_MESSAGE,
            null,
            new Object[]{"Hacer otro puzzle", "Reiniciar"},
            "Hacer otro puzzle"
        );

        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (opcion == JOptionPane.YES_OPTION && controlador != null) {
            controlador.formatearDatosYNuevoPuzzle(frame);
        } else if (opcion == JOptionPane.NO_OPTION && controlador != null) {
            controlador.reiniciarPuzzle();
        }
    }

    public Rectangle getRectTablero() { return rectTablero; }
    public double getZoom() { return zoom; }
    public void setZoom(double zoom) { this.zoom = zoom; repaint(); }
}