package com.mycompany.fundamentos_proyecto_final.ui;

import com.mycompany.fundamentos_proyecto_final.modelo.Grafo;
import com.mycompany.fundamentos_proyecto_final.modelo.Laberinto;
import com.mycompany.fundamentos_proyecto_final.modelo.Vertice;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Visualizador de laberintos completamente en texto (consola).
 *
 * Funciones principales:
 *   - imprimirLaberinto()          → muestra el laberinto en su estado base.
 *   - despliegaLaberinto(visitados) → animación paso a paso en texto.
 *   - imprimirResultado(nombre, visitados, grafo, tiempoNs) → resumen completo.
 *   - imprimirTresSoluciones(...)  → compara DFS, BFS y Dijkstra lado a lado.
 *
 * Leyenda de símbolos usada en la visualización:
 *   '+' '-' '|'  → paredes
 *   ' '          → pasillo libre
 *   'E'          → entrada
 *   'S'          → salida
 *   '·'          → vértice visitado durante la búsqueda
 *   '█'          → vértice actual que se está procesando
 *   '*'          → celda perteneciente al camino solución
 */
public class VisualizadorLaberinto {

    // Pausa entre frames de la animación (milisegundos). 0 = esperar tecla.
    private static final long PAUSA_MS = 80;

    // =========================================================================
    // Impresión básica del laberinto
    // =========================================================================

    /**
     * Imprime el laberinto tal como está en la cuadrícula,
     * sin resaltar ningún vértice.
     */
    public static void imprimirLaberinto(Laberinto lab) {
        if (lab == null) {
            System.out.println("[Sin laberinto cargado]");
            return;
        }
        char[][] c = lab.getCuadricula();
        System.out.println("┌── Laberinto: " + lab.getNombreArchivo()
                + " (" + lab.getFilas() + "×" + lab.getColumnas() + ") ──┐");
        for (char[] fila : c) {
            System.out.println(new String(fila));
        }
        imprimirLeyenda();
    }

    // =========================================================================
    // Animación paso a paso (texto)
    // =========================================================================

    /**
     * Muestra el laberinto paso a paso, resaltando cada vértice en
     * {@code ordenVisita} a medida que es "procesado".
     * Al final resalta el camino solución si existe.
     *
     * @param lab        Laberinto cargado.
     * @param ordenVisita Lista cronológica de vértices visitados.
     * @param grafo       Grafo con el camino solución ya calculado.
     */
    public static void despliegaLaberinto(Laberinto lab,
                                          List<Vertice> ordenVisita,
                                          Grafo grafo) {
        if (lab == null || ordenVisita == null) return;

        char[][] base = lab.getCuadricula();
        Set<Integer> visitados = new HashSet<>();

        for (int paso = 0; paso < ordenVisita.size(); paso++) {
            Vertice actual = ordenVisita.get(paso);

            // Construir frame
            limpiarConsola();
            System.out.println("Paso " + (paso + 1) + "/" + ordenVisita.size()
                    + "  →  " + actual);
            System.out.println();

            imprimirFrame(base, visitados, actual, null);

            visitados.add(actual.getId());

            pausar();
        }

        // Frame final: mostrar camino solución
        limpiarConsola();
        if (grafo.haySolucion()) {
            System.out.println("✔ Solución encontrada — longitud: "
                    + (grafo.getCamino().size() - 1) + " movimientos");
            Set<Integer> camino = new HashSet<>();
            for (Vertice v : grafo.getCamino()) camino.add(v.getId());
            imprimirFrame(base, visitados, null, camino);
        } else {
            System.out.println("✘ No hay solución.");
            imprimirFrame(base, visitados, null, null);
        }
        imprimirLeyenda();
    }

    // =========================================================================
    // Resumen de un algoritmo (sin animación)
    // =========================================================================

    /**
     * Imprime un resumen completo del resultado de un algoritmo.
     *
     * @param nombreAlgoritmo  "DFS", "BFS" o "Dijkstra".
     * @param lab              Laberinto.
     * @param ordenVisita      Lista de vértices visitados.
     * @param grafo            Grafo con el camino calculado.
     * @param tiempoNs         Tiempo de ejecución en nanosegundos.
     */
    public static void imprimirResultado(String nombreAlgoritmo,
                                         Laberinto lab,
                                         List<Vertice> ordenVisita,
                                         Grafo grafo,
                                         long tiempoNs) {
        System.out.println("════════════════════════════════════════");
        System.out.println("  Algoritmo : " + nombreAlgoritmo);
        System.out.println("  Laberinto : " + (lab != null ? lab.getNombreArchivo() : "?"));
        System.out.println("  Vértices visitados : " + ordenVisita.size());
        System.out.printf ("  Tiempo     : %.3f ms%n", tiempoNs / 1_000_000.0);

        if (grafo.haySolucion()) {
            System.out.println("  Solución   : SÍ — longitud "
                    + (grafo.getCamino().size() - 1) + " movimientos");
        } else {
            System.out.println("  Solución   : NO");
        }
        System.out.println("════════════════════════════════════════");

        // Laberinto con camino resaltado (sin animación)
        if (lab != null) {
            Set<Integer> visitados = new HashSet<>();
            for (Vertice v : ordenVisita) visitados.add(v.getId());

            Set<Integer> camino = null;
            if (grafo.haySolucion()) {
                camino = new HashSet<>();
                for (Vertice v : grafo.getCamino()) camino.add(v.getId());
            }
            imprimirFrame(lab.getCuadricula(), visitados, null, camino);
        }
        imprimirLeyenda();
    }

    // =========================================================================
    // Tres soluciones juntas
    // =========================================================================

    /**
     * Muestra las tres soluciones (DFS, BFS, Dijkstra) en texto de forma
     * comparativa y consecutiva. Reutiliza los resultados ya cacheados.
     */
    public static void imprimirTresSoluciones(Laberinto lab, Grafo grafo,
                                               List<Vertice> resDFS,
                                               List<Vertice> resBFS,
                                               List<Vertice> resDijkstra,
                                               long tDFS, long tBFS, long tDij) {
        System.out.println("\n╔══════════════════════════════════════╗");
        System.out.println("║      COMPARATIVA DE SOLUCIONES       ║");
        System.out.println("╚══════════════════════════════════════╝");

        String[][] filas = {
            {"Algoritmo",    "DFS",
                             "BFS",
                             "Dijkstra"},
            {"Visitados",    resDFS  != null ? String.valueOf(resDFS.size())      : "N/A",
                             resBFS  != null ? String.valueOf(resBFS.size())      : "N/A",
                             resDijkstra != null ? String.valueOf(resDijkstra.size()) : "N/A"},
            {"Long. camino", longitudStr(grafo, resDFS),
                             longitudStr(grafo, resBFS),
                             longitudStr(grafo, resDijkstra)},
            {"Tiempo (ms)",  String.format("%.3f", tDFS / 1_000_000.0),
                             String.format("%.3f", tBFS / 1_000_000.0),
                             String.format("%.3f", tDij / 1_000_000.0)}
        };

        // Ancho de columnas
        int[] anchos = {14, 10, 10, 10};
        for (String[] fila : filas) {
            StringBuilder sb = new StringBuilder("║");
            for (int i = 0; i < fila.length; i++) {
                sb.append(String.format(" %-" + anchos[i] + "s", fila[i])).append("│");
            }
            System.out.println(sb);
        }
        System.out.println("╚══════════════════════════════════════╝\n");
    }

    // =========================================================================
    // Utilidades internas
    // =========================================================================

    /**
     * Dibuja un frame del laberinto sobre la cuadrícula base.
     *
     * @param base      Cuadrícula original (no se modifica).
     * @param visitados Ids de vértices ya marcados como visitados.
     * @param actual    Vértice que se está procesando ahora (puede ser null).
     * @param camino    Ids del camino solución (puede ser null).
     */
    private static void imprimirFrame(char[][] base,
                                      Set<Integer> visitados,
                                      Vertice actual,
                                      Set<Integer> camino) {
        int filas = base.length;
        int columnas = (filas > 0) ? base[0].length : 0;

        for (int f = 0; f < filas; f++) {
            StringBuilder sb = new StringBuilder();
            for (int c = 0; c < columnas; c++) {
                char ch = base[f][c];
                int id = f * columnas + c;

                if (actual != null && actual.getFila() == f && actual.getColumna() == c) {
                    sb.append('█');
                } else if (camino != null && camino.contains(id)
                        && ch != 'E' && ch != 'S') {
                    sb.append('*');
                } else if (visitados.contains(id) && ch != 'E' && ch != 'S') {
                    sb.append('·');
                } else {
                    sb.append(ch);
                }
            }
            System.out.println(sb);
        }
    }

    private static void imprimirLeyenda() {
        System.out.println();
        System.out.println("Leyenda: E=Entrada  S=Salida  · =visitado  █=actual  *=camino");
    }

    private static void limpiarConsola() {
        // En Windows la secuencia ANSI puede no estar habilitada; usar saltos.
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private static void pausar() {
        if (PAUSA_MS <= 0) {
            try { System.in.read(); } catch (Exception ignored) {}
        } else {
            try { Thread.sleep(PAUSA_MS); } catch (InterruptedException ignored) {}
        }
    }

    private static String longitudStr(Grafo grafo, List<Vertice> res) {
        if (res == null) return "N/A";
        if (!grafo.haySolucion()) return "Sin sol.";
        return String.valueOf(grafo.getCamino().size() - 1);
    }
}
