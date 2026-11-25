package com.mycompany.fundamentos_proyecto_final;

import com.mycompany.fundamentos_proyecto_final.io.LectorLaberinto;
import com.mycompany.fundamentos_proyecto_final.modelo.Grafo;
import com.mycompany.fundamentos_proyecto_final.modelo.GrafoMatriz;
import com.mycompany.fundamentos_proyecto_final.modelo.Vertice;

import java.util.List;

public class PruebaLectura {

    public static void main(String[] args) {
        String[] archivos = {"maze00.txt", "maze01.txt", "maze02.txt"};

        for (String ruta : archivos) {
            System.out.println("╔══════════════════════════════════════════╗");
            System.out.println("║  Probando: " + ruta);
            System.out.println("╚══════════════════════════════════════════╝");

            try {
                // ---- 1. Carga y validación -----------------------------------
                GrafoMatriz lab = LectorLaberinto.cargarDesdeArchivo(ruta);
                System.out.println("✔ Cargado con éxito");
                System.out.printf("  Dimensiones : %d filas x %d columnas%n",
                        lab.getFilas(), lab.getColumnas());
                System.out.printf("  Vertices     : %d%n",
                        lab.getGrafo().getVertices().tamanio());
                System.out.printf("  Entrada      : fila=%-3d col=%-3d  (id=%d)%n",
                        lab.getFilaEntrada(), lab.getColEntrada(),
                        lab.getGrafo().getEntrada().getId());
                System.out.printf("  Salida       : fila=%-3d col=%-3d  (id=%d)%n",
                        lab.getFilaSalida(), lab.getColSalida(),
                        lab.getGrafo().getSalida().getId());

                Grafo grafo = lab.getGrafo();

                // ---- 2. DFS -------------------------------------------------
                long t0 = System.nanoTime();
                List<Vertice> dfs = grafo.DFS();
                long tDFS = System.nanoTime() - t0;
                boolean solDFS = grafo.haySolucion();
                int     lonDFS = solDFS ? grafo.getCamino().size() - 1 : -1;
                System.out.printf("%n  [DFS]      visitados=%-5d  tiempo=%.3f ms%n",
                        dfs.size(), tDFS / 1_000_000.0);
                imprimirCamino(solDFS, lonDFS, grafo);

                // ---- 3. BFS -------------------------------------------------
                t0 = System.nanoTime();
                List<Vertice> bfs = grafo.BFS();
                long tBFS = System.nanoTime() - t0;
                boolean solBFS = grafo.haySolucion();
                int     lonBFS = solBFS ? grafo.getCamino().size() - 1 : -1;
                System.out.printf("  [BFS]      visitados=%-5d  tiempo=%.3f ms%n",
                        bfs.size(), tBFS / 1_000_000.0);
                imprimirCamino(solBFS, lonBFS, grafo);

                // ---- 4. Dijkstra --------------------------------------------
                t0 = System.nanoTime();
                List<Vertice> dij = grafo.caminoMasCorto();
                long tDij = System.nanoTime() - t0;
                boolean solDij = grafo.haySolucion();
                int     lonDij = solDij ? grafo.getCamino().size() - 1 : -1;
                System.out.printf("  [Dijkstra] visitados=%-5d  tiempo=%.3f ms%n",
                        dij.size(), tDij / 1_000_000.0);
                imprimirCamino(solDij, lonDij, grafo);

                // ---- 5. Verificacion de consistencia ------------------------
                System.out.println();
                if (solBFS || solDij) {
                    System.out.println("  Verificacion de consistencia:");
                    if (lonBFS == lonDij) {
                        System.out.println("  OK BFS y Dijkstra coinciden en longitud: " + lonBFS);
                    } else {
                        System.out.println("  FALLO BFS=" + lonBFS + " vs Dijkstra=" + lonDij);
                    }
                    if (solDFS) {
                        System.out.println("  OK DFS encontro solucion (longitud=" + lonDFS
                                + ", no necesariamente optima)");
                    } else {
                        System.out.println("  INFO DFS no encontro camino con este orden de vecinos");
                    }
                } else {
                    System.out.println("  INFO Sin solucion (laberinto valido pero sin camino).");
                }

            } catch (Exception e) {
                System.out.println("ERROR: " + e.getMessage());
                e.printStackTrace();
            }

            System.out.println();
        }
    }

    private static void imprimirCamino(boolean haySol, int longitud, Grafo grafo) {
        if (haySol) {
            List<Vertice> cam = grafo.getCamino();
            System.out.printf("           camino=%d movs  %s -> %s%n",
                    longitud,
                    cam.get(0),
                    cam.get(cam.size() - 1));
        } else {
            System.out.println("           sin solucion");
        }
    }
}
