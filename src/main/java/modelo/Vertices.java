package modelo;

import java.util.Hashtable;
import java.util.Collection;

public class Vertices {

    private final Hashtable<Integer, Vertice> tabla;

    public Vertices() {
        this.tabla = new Hashtable<>();
    }

    public void agregar(Vertice v) {
        tabla.put(v.getId(), v);
    }

    public Vertice obtener(int id) {
        return tabla.get(id);
    }

    public boolean contiene(int id) {
        return tabla.containsKey(id);
    }

    public void eliminar(int id) {
        tabla.remove(id);
    }

    public Collection<Vertice> todos() {
        return tabla.values();
    }

    public int tamanio() {
        return tabla.size();
    }

    public void limpiar() {
        tabla.clear();
    }
}
