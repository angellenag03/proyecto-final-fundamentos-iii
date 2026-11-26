package modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.Stack;

public class Grafo {

    private final Map<Integer, LinkedList<Arcos>> adyacencia;
    private final Vertices vertices;

    // Entrada / Salida del laberinto
    private Vertice entrada;
    private Vertice salida;

    // Resultados cacheados
    private List<Vertice> resultadoDFS;
    private List<Vertice> resultadoBFS;
    private List<Vertice> resultadoDijkstra;

    /** Camino solución reconstruido desde entrada hasta salida */
    private List<Vertice> camino;
    private boolean haySolucion;

    // Constructor
    public Grafo() {
        this.adyacencia = new HashMap<>();
        this.vertices = new Vertices();
        this.camino = new ArrayList<>();
        this.haySolucion = false;
    }

    // Construcción del grafo
    /**
     * Agrega un vértice al grafo.
     * Crea su lista de adyacencia si aún no existe.
     */
    public void agregarVertice(Vertice v) {
        vertices.agregar(v);
        adyacencia.putIfAbsent(v.getId(), new LinkedList<>());
    }

    /**
     * Agrega una arista NO DIRIGIDA entre origen y destino con el costo dado.
     * Ambos vértices deben existir previamente.
     */
    public void agregarArco(Vertice origen, Vertice destino, int costo) {
        adyacencia.get(origen.getId()).add(new Arcos(origen, destino, costo));
        adyacencia.get(destino.getId()).add(new Arcos(destino, origen, costo));
    }

    /** Versión con costo = 1 */
    public void agregarArco(Vertice origen, Vertice destino) {
        agregarArco(origen, destino, 1);
    }

    // Entrada / Salida
    public void setEntrada(Vertice v) { this.entrada = v; }
    public void setSalida(Vertice v)  { this.salida  = v; }
    public Vertice getEntrada() { return entrada; }
    public Vertice getSalida()  { return salida;  }

    // Accesos generales
    public Vertices getVertices() { return vertices; }

    public LinkedList<Arcos> getVecinos(int id) {
        return adyacencia.getOrDefault(id, new LinkedList<>());
    }

    // Resultados
    public boolean haySolucion()           { return haySolucion; }
    public List<Vertice> getCamino()       { return Collections.unmodifiableList(camino); }
    public List<Vertice> getResultadoDFS() { return resultadoDFS  != null ? Collections.unmodifiableList(resultadoDFS)  : null; }
    public List<Vertice> getResultadoBFS() { return resultadoBFS  != null ? Collections.unmodifiableList(resultadoBFS)  : null; }
    public List<Vertice> getResultadoDijkstra() { return resultadoDijkstra != null ? Collections.unmodifiableList(resultadoDijkstra) : null; }

    // ALGORITMO 1: DFS  (iterativo con pila LIFO)
    /**
     * Recorre el grafo en profundidad a partir de la entrada.
     * Orden de vecinos real: Arriba → Derecha → Abajo → Izquierda.
     * Pila LIFO → se apilan en orden inverso: Izq, Abajo, Der, Arriba.
     *
     * @return Lista de vértices visitados en orden cronológico (sin repetir).
     *         Incluye entrada y salida si se llega a ella.
     */
    public List<Vertice> DFS() {
        List<Vertice> orden = new ArrayList<>();
        
        if (entrada == null) return orden;

        Set<Integer> visitado = new HashSet<>();
        Map<Integer, Integer> predecesor = new HashMap<>();
        Stack<Vertice> pila = new Stack<>();

        pila.push(entrada);
        predecesor.put(entrada.getId(), -1);

        boolean encontrado = false;

        while (!pila.isEmpty()) {
            Vertice actual = pila.pop();

            if (visitado.contains(actual.getId())) continue;

            visitado.add(actual.getId());
            orden.add(actual);

            if (actual.equals(salida)) {
                encontrado = true;
                break;   // detenerse al procesar salida
            }

            // Obtener vecinos ordenados: Arriba, Derecha, Abajo, Izquierda
            List<Vertice> vecinosOrdenados = vecinosOrdenados(actual);

            // Apilar en ORDEN INVERSO para que Arriba se procese primero
            for (int i = vecinosOrdenados.size() - 1; i >= 0; i--) {
                Vertice vecino = vecinosOrdenados.get(i);
                if (!visitado.contains(vecino.getId())) {
                    // Solo registrar predecesor si aún no tiene uno asignado
                    predecesor.put(vecino.getId(), actual.getId());
                    pila.push(vecino);
                }
            }
        }

        this.resultadoDFS = orden;

        if (encontrado) {
            this.camino = reconstruirCamino(predecesor);
            this.haySolucion = true;
        } else {
            this.camino = new ArrayList<>();
            this.haySolucion = false;
        }

        return Collections.unmodifiableList(orden);
    }

    // ALGORITMO 2: BFS  (cola FIFO)
    /**
     * Recorre el grafo en anchura a partir de la entrada.
     * Orden de vecinos: Arriba → Derecha → Abajo → Izquierda.
     * También reconstruye el camino de entrada a salida.
     *
     * @return Lista de vértices visitados en orden cronológico (sin repetir).
     */
    public List<Vertice> BFS() {
        List<Vertice> orden = new ArrayList<>();

        if (entrada == null) return orden;

        Set<Integer> visitado = new HashSet<>();
        LinkedList<Vertice> cola = new LinkedList<>();
        Map<Integer, Integer> predecesor = new HashMap<>();

        cola.add(entrada);
        visitado.add(entrada.getId());
        predecesor.put(entrada.getId(), -1);  // la entrada no tiene predecesor

        boolean encontrado = false;

        while (!cola.isEmpty()) {
            Vertice actual = cola.poll();
            orden.add(actual);

            if (actual.equals(salida)) {
                encontrado = true;
                break;
            }

            for (Vertice vecino : vecinosOrdenados(actual)) {
                if (!visitado.contains(vecino.getId())) {
                    visitado.add(vecino.getId());
                    predecesor.put(vecino.getId(), actual.getId());
                    cola.add(vecino);
                }
            }
        }

        this.resultadoBFS = orden;

        // Reconstruir camino si BFS lo encontró
        if (encontrado) {
            this.camino = reconstruirCamino(predecesor);
            this.haySolucion = true;
        } else {
            this.camino = new ArrayList<>();
            this.haySolucion = false;
        }

        return Collections.unmodifiableList(orden);
    }

    // ALGORITMO 3: Dijkstra (caminoMasCorto)
    /**
     * Implementación de Dijkstra independiente de BFS.
     * Desempata distancias iguales eligiendo el Vertice con MENOR id.
     * Reconstruye el camino desde salida hasta entrada usando predecesores.
     *
     * @return Lista de vértices procesados en orden cronológico (sin repetir).
     */
    public List<Vertice> caminoMasCorto() {
        List<Vertice> orden = new ArrayList<>();

        if (entrada == null) return orden;

        // dist[id] = distancia mínima conocida desde entrada
        Map<Integer, Integer> dist = new HashMap<>();
        Map<Integer, Integer> predecesor = new HashMap<>();
        Set<Integer> procesado = new HashSet<>();

        // Inicializar distancias como infinito
        for (Vertice v : vertices.todos()) {
            dist.put(v.getId(), Integer.MAX_VALUE);
        }
        dist.put(entrada.getId(), 0);
        predecesor.put(entrada.getId(), -1);

        // Cola de prioridad: (distancia, id) — desempate por menor id
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> {
            if (a[0] != b[0]) return Integer.compare(a[0], b[0]);
            return Integer.compare(a[1], b[1]);   // desempate por menor id
        });
        pq.offer(new int[]{0, entrada.getId()});

        boolean encontrado = false;

        while (!pq.isEmpty()) {
            int[] par = pq.poll();
            int distActual = par[0];
            int idActual   = par[1];

            if (procesado.contains(idActual)) continue;
            procesado.add(idActual);

            Vertice actual = vertices.obtener(idActual);
            orden.add(actual);

            if (actual.equals(salida)) {
                encontrado = true;
                break;
            }

            // Si la distancia sacada de la cola ya es mayor a la conocida, saltar
            if (distActual > dist.get(idActual)) continue;

            for (Arcos arco : adyacencia.getOrDefault(idActual, new LinkedList<>())) {
                Vertice vecino = arco.getDestino();
                int nuevaDist = dist.get(idActual) + arco.getCosto();

                if (nuevaDist < dist.getOrDefault(vecino.getId(), Integer.MAX_VALUE)) {
                    dist.put(vecino.getId(), nuevaDist);
                    predecesor.put(vecino.getId(), idActual);
                    pq.offer(new int[]{nuevaDist, vecino.getId()});
                }
            }
        }

        this.resultadoDijkstra = orden;

        if (encontrado) {
            this.camino = reconstruirCamino(predecesor);
            this.haySolucion = true;
        } else {
            this.camino = new ArrayList<>();
            this.haySolucion = false;
        }

        return Collections.unmodifiableList(orden);
    }

    // Utilidades internas
    /**
     * Retorna los vecinos del vértice dado ordenados:
     * Arriba → Derecha → Abajo → Izquierda.
     */
    private List<Vertice> vecinosOrdenados(Vertice v) {
        int fila = v.getFila();
        int col  = v.getColumna();

        // Deltas en orden: Arriba, Derecha, Abajo, Izquierda
        int[][] deltas = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};

        List<Vertice> resultado = new ArrayList<>();
        for (int[] d : deltas) {
            // Buscar vecino real entre los arcos del vértice
            for (Arcos arco : adyacencia.getOrDefault(v.getId(), new LinkedList<>())) {
                Vertice dest = arco.getDestino();
                if (dest.getFila() == fila + d[0] && dest.getColumna() == col + d[1]) {
                    resultado.add(dest);
                    break;
                }
            }
        }
        return resultado;
    }

    /**
     * Reconstruye el camino desde la salida hasta la entrada usando el mapa
     * de predecesores y lo invierte para devolver entrada → salida.
     */
    private List<Vertice> reconstruirCamino(Map<Integer, Integer> predecesor) {
        List<Vertice> camino = new ArrayList<>();
        int idActual = salida.getId();

        while (idActual != -1) {
            camino.add(vertices.obtener(idActual));
            Integer pred = predecesor.get(idActual);
            if (pred == null) break;
            idActual = pred;
        }

        Collections.reverse(camino);
        return camino;
    }
}
