package modelo;

public class GrafoMatriz {

    private char[][] cuadricula;
    private int filas;
    private int columnas;
    private Grafo grafo;
    private String nombreArchivo;
    private boolean celdasLogicas;
    public GrafoMatriz() {
        this.grafo = new Grafo();
    }

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

    public char getChar(int fila, int col) {
        if (fila < 0 || fila >= filas || col < 0 || col >= columnas) return '\0';
        return cuadricula[fila][col];
    }

    public static boolean esTransitable(char c) {
        return c == ' ' || c == '#' || c == 'E' || c == 'S';
    }

    public int getFilaEntrada() {
        Vertice e = grafo.getEntrada();
        return e != null ? e.getFila() : -1;
    }

    public int getColEntrada() {
        Vertice e = grafo.getEntrada();
        return e != null ? e.getColumna() : -1;
    }

    public int getFilaSalida() {
        Vertice s = grafo.getSalida();
        return s != null ? s.getFila() : -1;
    }

    public int getColSalida() {
        Vertice s = grafo.getSalida();
        return s != null ? s.getColumna() : -1;
    }
    public boolean isCeldasLogicas() {return celdasLogicas;}
    public void setCeldasLogicas(boolean c){this.celdasLogicas = c;}
}
