package com.mycompany.fundamentos_proyecto_final.modelo;

/**
 * Representa el laberinto cargado desde un archivo de texto.
 *
 * Almacena:
 *   - La cuadrícula de caracteres original (char[][]).
 *   - El Grafo construido a partir de ella.
 *   - Referencias a los vértices de entrada y salida.
 */
public class GrafoMatriz {

    private char[][] cuadricula;
    private int filas;
    private int columnas;
    private Grafo grafo;
    private String nombreArchivo;

    public GrafoMatriz() {
        this.grafo = new Grafo();
    }

    // -------------------------------------------------------------------------
    // Getters / Setters
    // -------------------------------------------------------------------------

    public char[][] getCuadricula() { return cuadricula; }
    public void setCuadricula(char[][] cuadricula) {
        this.cuadricula = cuadricula;
        this.filas = cuadricula.length;
        this.columnas = (filas > 0) ? cuadricula[0].length : 0;
    }

    public int getFilas()    { return filas; }
    public int getColumnas() { return columnas; }

    public Grafo getGrafo()         { return grafo; }
    public void  setGrafo(Grafo g)  { this.grafo = g; }

    public String getNombreArchivo()           { return nombreArchivo; }
    public void   setNombreArchivo(String n)   { this.nombreArchivo = n; }

    /**
     * Retorna el carácter en la posición (fila, col).
     * Devuelve '\0' si las coordenadas están fuera de rango.
     */
    public char getChar(int fila, int col) {
        if (fila < 0 || fila >= filas || col < 0 || col >= columnas) return '\0';
        return cuadricula[fila][col];
    }

    /**
     * Verifica si el carácter en (fila, col) es transitable.
     * Transitables: ' ', '#', 'E', 'S'.
     */
    public static boolean esTransitable(char c) {
        return c == ' ' || c == '#' || c == 'E' || c == 'S';
    }

    // -------------------------------------------------------------------------
    // Shortcuts para entrada y salida (delegando al Grafo)
    // -------------------------------------------------------------------------

    /** Fila del vértice de entrada, o -1 si no hay entrada definida. */
    public int getFilaEntrada() {
        Vertice e = grafo.getEntrada();
        return e != null ? e.getFila() : -1;
    }

    /** Columna del vértice de entrada, o -1 si no hay entrada definida. */
    public int getColEntrada() {
        Vertice e = grafo.getEntrada();
        return e != null ? e.getColumna() : -1;
    }

    /** Fila del vértice de salida, o -1 si no hay salida definida. */
    public int getFilaSalida() {
        Vertice s = grafo.getSalida();
        return s != null ? s.getFila() : -1;
    }

    /** Columna del vértice de salida, o -1 si no hay salida definida. */
    public int getColSalida() {
        Vertice s = grafo.getSalida();
        return s != null ? s.getColumna() : -1;
    }
}
