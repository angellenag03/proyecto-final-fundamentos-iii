package ui;

import modelo.Grafo;
import modelo.GrafoMatriz;
import modelo.Vertice;

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
 *   '#'          → celda perteneciente al camino solución
 */
public class VisualizadorLaberinto {

    // Pausa entre frames de la animación (milisegundos). 0 = esperar tecla.
    private static final long PAUSA_MS = 200;

    // Impresión básica del laberinto
    /**
     * Imprime el laberinto tal como está en la cuadrícula,
     * sin resaltar ningún vértice.
     */
    public static void imprimirLaberinto(GrafoMatriz lab) {
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
   
    // Animación paso a paso (texto)
    /**
     * Muestra el laberinto paso a paso, resaltando cada vértice en
     * {@code ordenVisita} a medida que es "procesado".
     * Al final resalta el camino solución si existe.
     *
     * @param lab        Laberinto cargado.
     * @param ordenVisita Lista cronológica de vértices visitados.
     * @param grafo       Grafo con el camino solución ya calculado.
     */
    public static void despliegaLaberinto(GrafoMatriz lab,
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

            imprimirFrame(base, visitados, actual, null, lab.isCeldasLogicas());

            visitados.add(actual.getId());

            pausar();
        }

        // Frame final: mostrar camino solución
        limpiarConsola();
        if (grafo.haySolucion()) {
            System.out.println("Solución encontrada — longitud: "
                    + (grafo.getCamino().size() - 1) + " movimientos");
            Set<Integer> camino = new HashSet<>();
            for (Vertice v : grafo.getCamino()) camino.add(v.getId());
            imprimirFrame(base, visitados, null, camino, lab.isCeldasLogicas());
        } else {
            System.out.println("No hay solución.");
            imprimirFrame(base, visitados, null, null, lab.isCeldasLogicas());
        }
        imprimirLeyenda();
    }

    // Resumen de un algoritmo (sin animación)
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
                                         GrafoMatriz lab,
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
            imprimirFrame(lab.getCuadricula(), visitados, null, camino, lab.isCeldasLogicas());
        }
        imprimirLeyenda();
    }

    // Tres soluciones juntas
    /**
     * Muestra las tres soluciones (DFS, BFS, Dijkstra) en texto de forma
     * comparativa y consecutiva. Reutiliza los resultados ya cacheados.
     */
    public static void imprimirTresSoluciones(GrafoMatriz lab, Grafo grafo,
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

    // Utilidades internas
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
                                      Set<Integer> camino,
                                      boolean celdasLogicas) {
        int filas = base.length;
        int columnas = (filas > 0) ? base[0].length : 0;

        for (int f = 0; f < filas; f++) {
            StringBuilder sb = new StringBuilder();
            for (int c = 0; c < columnas; c++) {
                char ch = base[f][c];
                if (celdasLogicas) {
                    sb.append(caracterCeldaLogica(ch, f, c, filas, columnas, visitados, actual, camino));
                } else {
                    int id = f * columnas + c;
                    if (actual != null && actual.getFila() == f && actual.getColumna() == c) {
                        sb.append('█');
                    } else if (camino != null && camino.contains(id)
                            && ch != 'E' && ch != 'S') {
                        sb.append('#');
                    } else if (visitados.contains(id) && ch != 'E' && ch != 'S') {
                        sb.append('·');
                    } else {
                        sb.append(ch);
                    }
                }
            }
            System.out.println(sb);
        }
    }

    /**
     * Resuelve el carácter a mostrar en (f, c) cuando el grafo usa celdas lógicas,
     * iluminando ambos caracteres de la celda y los pasos abiertos entre celdas.
     */
    private static char caracterCeldaLogica(char ch, int f, int c, int filas, int columnas,
                                            Set<Integer> visitados, Vertice actual, Set<Integer> camino) {
        if (ch == '+' || ch == '-' || ch == '|') {
            return ch;
        }
        int colsLog = (columnas - 1) / 3;

        // Interior de una celda lógica (2 caracteres de ancho)
        if (f % 2 == 1 && c % 3 != 0) {
            int r = (f - 1) / 2;
            int cl = (c - 1) / 3;
            int id = r * colsLog + cl;

            if (actual != null && actual.getFila() == r && actual.getColumna() == cl) {
                return '█';
            }
            if (ch != 'E' && ch != 'S') {
                if (camino != null && camino.contains(id)) return '#';
                if (visitados.contains(id)) return '·';
            }
            return ch;
        }

        // Conector horizontal abierto entre dos celdas vecinas
        if (f % 2 == 1 && c % 3 == 0 && c > 0 && c < columnas - 1 && ch != 'E' && ch != 'S') {
            int r = (f - 1) / 2;
            int idIzq = r * colsLog + (c / 3 - 1);
            int idDer = r * colsLog + (c / 3);
            if (camino != null && camino.contains(idIzq) && camino.contains(idDer)) return '#';
            if (visitados.contains(idIzq) && visitados.contains(idDer)) return '·';
            return ch;
        }

        // Conector vertical abierto entre dos celdas vecinas
        if (f % 2 == 0 && c % 3 != 0 && f > 0 && f < filas - 1 && ch != 'E' && ch != 'S') {
            int cl = (c - 1) / 3;
            int idArr = (f / 2 - 1) * colsLog + cl;
            int idAbj = (f / 2) * colsLog + cl;
            if (camino != null && camino.contains(idArr) && camino.contains(idAbj)) return '#';
            if (visitados.contains(idArr) && visitados.contains(idAbj)) return '·';
            return ch;
        }

        return ch;
    }

    private static void imprimirLeyenda() {
        System.out.println();
        System.out.println("Leyenda: E=Entrada  S=Salida  · =visitado  █=actual  #=camino");
    }

    private static void limpiarConsola() { // ojalá limpiara...
        // En Windows la secuencia ANSI puede no estar habilitada; usar saltos.
        System.out.print("\033[H");
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
