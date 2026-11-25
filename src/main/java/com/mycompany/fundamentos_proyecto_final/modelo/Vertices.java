package com.mycompany.fundamentos_proyecto_final.modelo;

import java.util.Hashtable;
import java.util.Collection;

/**
 * Administra el conjunto de vértices del grafo.
 * Usa obligatoriamente Hashtable<Integer, Vertice> como estructura interna.
 */
public class Vertices {

    // Estructura obligatoria según especificación
    private final Hashtable<Integer, Vertice> tabla;

    public Vertices() {
        this.tabla = new Hashtable<>();
    }

    /**
     * Agrega un vértice. Si ya existe un id igual, lo reemplaza.
     */
    public void agregar(Vertice v) {
        tabla.put(v.getId(), v);
    }

    /**
     * Obtiene un vértice por su id. Retorna null si no existe.
     */
    public Vertice obtener(int id) {
        return tabla.get(id);
    }

    /**
     * Verifica si existe un vértice con ese id.
     */
    public boolean contiene(int id) {
        return tabla.containsKey(id);
    }

    /**
     * Elimina el vértice con el id dado.
     */
    public void eliminar(int id) {
        tabla.remove(id);
    }

    /**
     * Retorna todos los vértices almacenados.
     */
    public Collection<Vertice> todos() {
        return tabla.values();
    }

    /**
     * Cantidad de vértices almacenados.
     */
    public int tamanio() {
        return tabla.size();
    }

    /**
     * Vacía la colección.
     */
    public void limpiar() {
        tabla.clear();
    }
}
