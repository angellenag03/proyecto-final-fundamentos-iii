package ui;
/*
Este codigo fue creado usando de apoyo y guia a Claude,
el codigo fue revisado a mano por una persona real, reescrito y adaptado
*/

import modelo.GrafoMatriz;
import modelo.Vertice;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.JPanel;

public class PanelLaberinto extends JPanel {
    private GrafoMatriz lab;
    private final Set<Vertice> visitados = new HashSet<>();
    private Vertice actual;
    private List<Vertice> camino = new ArrayList<>();
    
    public PanelLaberinto(){
        setBackground(Color.WHITE);
    }
    public void limpiar() {
    visitados.clear();
    actual = null;
    camino = new ArrayList<>();
    repaint();
}
    public void setLaberinto(GrafoMatriz l) {
        lab = l;
        limpiar();
    }
    public void marcarVisitado(Vertice v){
        visitados.add(v);
        actual=v;
    }
    public void setCamino(List<Vertice> c){
        camino = c;
        actual = null;
        repaint();
    }
    @Override
    protected void paintComponent(Graphics g0){
        super.paintComponent(g0);
        if(lab == null)
            return;
        Graphics2D g = (Graphics2D) g0;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        char[][] cu = lab.getCuadricula();
        boolean logico = lab.isCeldasLogicas();
        int filas = logico ? (cu.length -1)/2 : cu.length;
        int cols = logico ? (cu[0].length - 1)/3:cu[0].length;
        int margen = 20;
        int s = Math.max(1, Math.min((getWidth() - 2 * margen) / cols,
                                     (getHeight() - 2 * margen) / filas));
        int x0 = (getWidth() - s * cols) / 2;
        int y0 = (getHeight() - s * filas) / 2;     
        
         g.setColor(new Color(173, 216, 230));
        for (Vertice v : visitados) {
            g.fillRect(x0 + v.getColumna() * s, y0 + v.getFila() * s, s, s);
        }
        if (actual != null) {
            g.setColor(new Color(255, 140, 0));
            g.fillRect(x0 + actual.getColumna() * s, y0 + actual.getFila() * s, s, s);
        }
         g.setColor(Color.DARK_GRAY);
        if (logico) {
            g.setStroke(new BasicStroke(2f));
            for (int r = 0; r < filas; r++) {
                for (int c = 0; c < cols; c++) {
                    int x = x0 + c * s;
                    int y = y0 + r * s;
                    if (cu[2 * r][3 * c + 1] == '-') g.drawLine(x, y, x + s, y); 
                    if (cu[2 * r + 1][3 * c] == '|') g.drawLine(x, y, x, y + s);
                    if (c == cols - 1 && cu[2 * r + 1][3 * c + 3] == '|')
                        g.drawLine(x + s, y, x + s, y + s);
                    if (r == filas - 1 && cu[2 * r + 2][3 * c + 1] == '-')
                        g.drawLine(x, y + s, x + s, y + s);                           
                }
            }
        } else {
            for (int f = 0; f < filas; f++) {
                for (int c = 0; c < cols; c++) {
                    char ch = cu[f][c];
                    if (ch == '+' || ch == '-' || ch == '|') {
                        g.fillRect(x0 + c * s, y0 + f * s, s, s);
                    }
                }
            }
        }

        if (camino.size() > 1) {
            g.setColor(new Color(220, 20, 60));
            g.setStroke(new BasicStroke(Math.max(2f, s / 4f),
                    BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            for (int i = 1; i < camino.size(); i++) {
                Vertice a = camino.get(i - 1);
                Vertice b = camino.get(i);
                g.drawLine(x0 + a.getColumna() * s + s / 2, y0 + a.getFila() * s + s / 2,
                           x0 + b.getColumna() * s + s / 2, y0 + b.getFila() * s + s / 2);
            }
        }
        int d = Math.max(6, s / 2);
        Vertice ent = lab.getGrafo().getEntrada();
        Vertice sal = lab.getGrafo().getSalida();
        if (ent != null) {
            g.setColor(new Color(0, 160, 0));
            g.fillOval(x0 + ent.getColumna() * s + (s - d) / 2, y0 + ent.getFila() * s + (s - d) / 2, d, d);
        }
        if (sal != null) {
            g.setColor(new Color(30, 60, 220));
            g.fillOval(x0 + sal.getColumna() * s + (s - d) / 2, y0 + sal.getFila() * s + (s - d) / 2, d, d);
        }
    }
}
