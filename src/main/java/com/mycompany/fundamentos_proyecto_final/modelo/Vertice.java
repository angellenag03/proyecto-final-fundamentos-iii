package com.mycompany.fundamentos_proyecto_final.modelo;

/**
 * Representa un vértice del grafo de laberinto.
 * Cada carácter transitable (fila, columna) es un Vertice individual con id único.
 * equals y hashCode se basan únicamente en id.
 */
public class Vertice {

    private final int id;
    private final int fila;
    private final int columna;

    public Vertice(int id, int fila, int columna) {
        this.id = id;
        this.fila = fila;
        this.columna = columna;
    }

    public int getId() {
        return id;
    }

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Vertice)) return false;
        Vertice otro = (Vertice) obj;
        return this.id == otro.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    @Override
    public String toString() {
        return "V(" + id + ",[" + fila + "," + columna + "])";
    }
}
