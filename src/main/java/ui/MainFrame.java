package ui;

import io.LectorLaberinto;
import modelo.Grafo;
import modelo.GrafoMatriz;
import modelo.Vertice;

import java.awt.*;
import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.*;

public class MainFrame extends JFrame {
    private final JComboBox<String> combo = new JComboBox<>();
    private final JLabel estado = new JLabel("Elige un laberinto");
    private GrafoMatriz lab;
    private List<Vertice> visitados;
    private List<Vertice> camino;

    private final JPanel dibujo = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (lab == null) return;
            char[][] cu = lab.getCuadricula();
            boolean logico = lab.isCeldasLogicas();
            int filas = logico ? (cu.length - 1) / 2 : cu.length;
            int cols = logico ? (cu[0].length - 1) / 3 : cu[0].length;
            int s = Math.max(1, Math.min(getWidth() / cols, getHeight() / filas));

            if (visitados != null) {
                g.setColor(new Color(173, 216, 230));
                for (Vertice v : visitados)
                    g.fillRect(v.getColumna() * s, v.getFila() * s, s, s);
            }
            g.setColor(Color.DARK_GRAY);
            for (int r = 0; r < filas; r++) {
                for (int c = 0; c < cols; c++) {
                    int x = c * s, y = r * s;
                    if (logico) {
                        if (cu[2 * r][3 * c + 1] == '-') g.drawLine(x, y, x + s, y);
                        if (cu[2 * r + 1][3 * c] == '|') g.drawLine(x, y, x, y + s);
                        if (c == cols - 1 && cu[2 * r + 1][3 * c + 3] == '|') g.drawLine(x + s, y, x + s, y + s);
                        if (r == filas - 1 && cu[2 * r + 2][3 * c + 1] == '-') g.drawLine(x, y + s, x + s, y + s);
                    } else if ("+-|".indexOf(cu[r][c]) >= 0) {
                        g.fillRect(x, y, s, s);
                    }
                }
            }
            if (camino != null) {
                g.setColor(Color.RED);
                for (int i = 1; i < camino.size(); i++) {
                    Vertice a = camino.get(i - 1), b = camino.get(i);
                    g.drawLine(a.getColumna() * s + s / 2, a.getFila() * s + s / 2,
                               b.getColumna() * s + s / 2, b.getFila() * s + s / 2);
                }
            }
        }
    };

    public MainFrame() {
        super("Laberintos");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);

        JButton cargar = new JButton("Cargar");
        JButton dfs = new JButton("DFS");
        JButton bfs = new JButton("BFS");
        JButton dij = new JButton("Dijkstra");
        JPanel barra = new JPanel();
        for (Component c : new Component[]{combo, cargar, dfs, bfs, dij}) barra.add(c);

        add(barra, BorderLayout.NORTH);
        add(dibujo, BorderLayout.CENTER);
        add(estado, BorderLayout.SOUTH);

        File[] archivos = new File(".").listFiles((d, n) -> n.toLowerCase().endsWith(".txt"));
        if (archivos != null) {
            Arrays.sort(archivos);
            for (File a : archivos) combo.addItem(a.getName());
        }

        cargar.addActionListener(e -> cargar());
        dfs.addActionListener(e -> resolver(0));
        bfs.addActionListener(e -> resolver(1));
        dij.addActionListener(e -> resolver(2));
    }

    private void cargar() {
        try {
            lab = LectorLaberinto.cargarDesdeArchivo((String) combo.getSelectedItem());
            visitados = null;
            camino = null;
            estado.setText("Cargado: " + lab.getNombreArchivo());
            dibujo.repaint();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void resolver(int alg) {
        if (lab == null) return;
        Grafo g = lab.getGrafo();
        visitados = alg == 0 ? g.DFS() : alg == 1 ? g.BFS() : g.caminoMasCorto();
        camino = g.haySolucion() ? g.getCamino() : null;
        estado.setText(g.haySolucion() ? "Camino de " + (camino.size() - 1) + " movimientos" : "Sin solución");
        dibujo.repaint();
    }
}