package com.mycompany.fundamentos_proyecto_final.modelo;

/**
 * Representa una arista (arco) del grafo no dirigido de laberinto.
 * Contiene origen, destino y costo (siempre 1 para movimientos ortogonales).
 */
public class Arcos {

    private final Vertice origen;
    private final Vertice destino;
    private final int costo;

    public Arcos(Vertice origen, Vertice destino, int costo) {
        this.origen = origen;
        this.destino = destino;
        this.costo = costo;
    }

    /** Constructor con costo por defecto = 1 */
    public Arcos(Vertice origen, Vertice destino) {
        this(origen, destino, 1);
    }

    public Vertice getOrigen() {
        return origen;
    }

    public Vertice getDestino() {
        return destino;
    }

    public int getCosto() {
        return costo;
    }

    @Override
    public String toString() {
        return origen + " --(" + costo + ")--> " + destino;
    }
}
