package ui;

import modelo.Grafo;
import modelo.GrafoMatriz;
import modelo.Vertice;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class VisualizadorLaberinto {

    private static final long PAUSA_MS = 80;
    private static final String LINEA = "========================================";

    public static void despliegaLaberinto(GrafoMatriz lab, List<Vertice> ordenVisita, Grafo grafo) {
        if (lab == null || ordenVisita == null) return;
        Set<Integer> visitados = new HashSet<>();

        for (int paso = 0; paso < ordenVisita.size(); paso++) {
            Vertice actual = ordenVisita.get(paso);
            System.out.println();
            System.out.println(LINEA);
            System.out.println("Paso " + (paso + 1) + "/" + ordenVisita.size() + "  ->  " + actual);
            imprimirFrame(lab, visitados, actual, null);
            visitados.add(actual.getId());
            try { Thread.sleep(PAUSA_MS); } catch (InterruptedException ignored) {}
        }

        System.out.println(LINEA);
        if (grafo.haySolucion()) {
            System.out.println("Solucion encontrada - longitud: " + (grafo.getCamino().size() - 1) + " movimientos");
            imprimirFrame(lab, visitados, null, grafo.getCamino());
        } else {
            System.out.println("No hay solucion.");
            imprimirFrame(lab, visitados, null, null);
        }
        imprimirLeyenda();
    }

    public static void imprimirResultado(String nombreAlgoritmo, GrafoMatriz lab,
                                         List<Vertice> ordenVisita, Grafo grafo, long tiempoNs) {
        System.out.println(LINEA);
        System.out.println("  Algoritmo : " + nombreAlgoritmo);
        System.out.println("  Laberinto : " + (lab != null ? lab.getNombreArchivo() : "?"));
        System.out.println("  Vertices visitados : " + ordenVisita.size());
        System.out.printf ("  Tiempo     : %.3f ms%n", tiempoNs / 1_000_000.0);
        if (grafo.haySolucion()) {
            System.out.println("  Solucion   : SI - longitud " + (grafo.getCamino().size() - 1) + " movimientos");
        } else {
            System.out.println("  Solucion   : NO");
        }
        System.out.println(LINEA);

        if (lab != null) {
            Set<Integer> visitados = new HashSet<>();
            for (Vertice v : ordenVisita) visitados.add(v.getId());
            imprimirFrame(lab, visitados, null, grafo.haySolucion() ? grafo.getCamino() : null);
        }
        imprimirLeyenda();
    }

    private static long clave(int a, int b) {
        return (long) Math.min(a, b) * 1_000_000L + Math.max(a, b);
    }

    private static void imprimirFrame(GrafoMatriz lab, Set<Integer> visitados,
                                      Vertice actual, List<Vertice> camino) {
        char[][] base = lab.getCuadricula();
        int filas = base.length;
        int columnas = base[0].length;
        boolean logico = lab.isCeldasLogicas();
        int colsLog = (columnas - 1) / 3;

        Set<Integer> idsCamino = new HashSet<>();
        Set<Long> aristas = new HashSet<>();
        if (camino != null) {
            for (int i = 0; i < camino.size(); i++) {
                idsCamino.add(camino.get(i).getId());
                if (i > 0) aristas.add(clave(camino.get(i - 1).getId(), camino.get(i).getId()));
            }
        }

        for (int f = 0; f < filas; f++) {
            StringBuilder sb = new StringBuilder();
            for (int c = 0; c < columnas; c++) {
                char ch = base[f][c];
                char out = ch;

                if (!logico) {
                    int id = f * columnas + c;
                    if (actual != null && actual.getId() == id) out = '@';
                    else if (ch != 'E' && ch != 'S') {
                        if (idsCamino.contains(id)) out = '*';
                        else if (visitados.contains(id)) out = '.';
                    }
                } else if (ch != '+' && ch != '-' && ch != '|') {
                    if (f % 2 == 1 && c % 3 != 0) {                       // interior de celda
                        int id = ((f - 1) / 2) * colsLog + (c - 1) / 3;
                        if (actual != null && actual.getId() == id) out = '@';
                        else if (ch != 'E' && ch != 'S') {
                            if (idsCamino.contains(id)) out = '*';
                            else if (visitados.contains(id)) out = '.';
                        }
                    } else if (f % 2 == 1 && c % 3 == 0 && c > 0 && c < columnas - 1) { // paso horizontal
                        int r = (f - 1) / 2;
                        int a = r * colsLog + c / 3 - 1;
                        int b = r * colsLog + c / 3;
                        if (aristas.contains(clave(a, b))) out = '*';
                    } else if (f % 2 == 0 && c % 3 != 0 && f > 0 && f < filas - 1) {    // paso vertical
                        int cl = (c - 1) / 3;
                        int a = (f / 2 - 1) * colsLog + cl;
                        int b = (f / 2) * colsLog + cl;
                        if (aristas.contains(clave(a, b))) out = '*';
                    }
                }
                sb.append(out);
            }
            System.out.println(sb);
        }
    }

    private static void imprimirLeyenda() {
        System.out.println();
        System.out.println("Leyenda: E=Entrada  S=Salida  .=visitado  @=actual  *=camino");
    }
}