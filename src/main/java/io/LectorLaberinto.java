package io;

import modelo.Arcos;
import modelo.Grafo;
import modelo.GrafoMatriz;
import modelo.Vertice;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Lee y valida un laberinto desde un String ya cargado.
 *
 * Reglas de lectura (según especificación):
 *  - SIN trim(): se conservan espacios iniciales y finales de cada línea.
 *  - Dimensiones dinámicas (no cuadradas fijas); todas las líneas deben tener
 *    la misma longitud > 0.
 *  - Caracteres permitidos: '+', '-', '|', ' ', '#', 'E', 'S'.
 *    No se permiten tabulaciones ni barras invertidas.
 *  - Transitables: ' ', '#', 'E', 'S'.  Tratar '#' igual que espacio.
 *  - Entrada/Salida:
 *      · Si existe 'E' o 'S' debe haber exactamente una de cada una.
 *      · Si no hay E/S, recorrer el perímetro (arriba-abajo, izq-der,
 *        esquinas una sola vez); exactamente 2 celdas transitables en el borde:
 *        la primera = entrada, la segunda = salida.
 *  - Errores: lanzar excepción descriptiva; el laberinto previo debe quedar
 *    intacto. Un laberinto sin solución es válido.
 */
public class LectorLaberinto {

    /**
     * Carga un laberinto directamente desde la ruta de un archivo .txt.
     * Busca el archivo primero en el directorio de trabajo actual, luego
     * en la subcarpeta "laberintos/".
     *
     * @param ruta  Ruta o nombre del archivo (p.ej. "maze00.txt").
     * @return      Laberinto listo para usar.
     * @throws Exception Si el archivo no se encuentra o no pasa la validación.
     */
    public static GrafoMatriz cargarDesdeArchivo(String ruta) throws Exception {
        java.io.File archivo = new java.io.File(ruta);
        if (!archivo.exists()) {
            archivo = new java.io.File("laberintos/" + ruta);
        }
        if (!archivo.exists()) {
            throw new Exception("No se encontró el archivo: " + ruta);
        }

        StringBuilder sb = new StringBuilder();
        try (java.util.Scanner sc = new java.util.Scanner(archivo)) {
            while (sc.hasNextLine()) {
                sb.append(sc.nextLine()).append("\n");
            }
        }
        return parsear(sb.toString(), archivo.getName());
    }

    /**
     * Parsea el contenido de texto de un laberinto y construye el objeto
     * {@link GrafoMatriz} con su {@link Grafo} completo.
     *
     * @param contenido  Texto completo del archivo (tal como lo lee el Main).
     * @param nombre     Nombre del archivo (para identificación).
     * @return           Laberinto listo para usar.
     * @throws Exception Si el contenido no pasa la validación.
     */
    public static GrafoMatriz parsear(String contenido, String nombre) throws Exception {

        // ---- 1. Leer líneas SIN trim() ---------------------------------------
        List<String> lineas = new ArrayList<>();
        BufferedReader br = new BufferedReader(new StringReader(contenido));
        String linea;
        while ((linea = br.readLine()) != null) {
            lineas.add(linea);          // sin trim
        }

        if (lineas.isEmpty()) {
            throw new Exception("El archivo '" + nombre + "' está vacío.");
        }

        // ---- 2. Validar que todas las líneas tienen la misma longitud > 0 ---
        int anchoEsperado = lineas.get(0).length();
        if (anchoEsperado == 0) {
            throw new Exception("La primera línea del laberinto está vacía.");
        }
        for (int i = 1; i < lineas.size(); i++) {
            if (lineas.get(i).length() != anchoEsperado) {
                throw new Exception(
                    "Línea " + (i + 1) + " tiene longitud " + lineas.get(i).length()
                    + " pero se esperaba " + anchoEsperado + ".");
            }
        }

        int filas    = lineas.size();
        int columnas = anchoEsperado;

        // ---- 3. Construir la cuadrícula y validar caracteres ----------------
        char[][] cuadricula = new char[filas][columnas];
        for (int f = 0; f < filas; f++) {
            String l = lineas.get(f);
            for (int c = 0; c < columnas; c++) {
                char ch = l.charAt(c);
                if (!esCaracterValido(ch)) {
                    throw new Exception(
                        "Carácter inválido '" + ch + "' (ASCII " + (int) ch
                        + ") en [" + f + "," + c + "].");
                }
                cuadricula[f][c] = ch;
            }
        }

        // ---- 4. Localizar Entrada y Salida ----------------------------------
        int[] posEntrada = null;
        int[] posSalida  = null;
        int cuentaE = 0, cuentaS = 0;

        for (int f = 0; f < filas; f++) {
            for (int c = 0; c < columnas; c++) {
                if (cuadricula[f][c] == 'E') { posEntrada = new int[]{f, c}; cuentaE++; }
                if (cuadricula[f][c] == 'S') { posSalida  = new int[]{f, c}; cuentaS++; }
            }
        }

        boolean hayES = (cuentaE > 0 || cuentaS > 0);

        if (hayES) {
            // Debe haber exactamente una E y exactamente una S
            if (cuentaE != 1) throw new Exception("Se requiere exactamente 1 'E', pero se encontraron " + cuentaE + ".");
            if (cuentaS != 1) throw new Exception("Se requiere exactamente 1 'S', pero se encontraron " + cuentaS + ".");
        } else {
            // Recorrer el perímetro: arriba-abajo, izquierda-derecha,
            // esquinas contadas una sola vez.
            List<int[]> celdas = perimetroEnOrden(cuadricula, filas, columnas);
            List<int[]> transitables = new ArrayList<>();
            for (int[] pos : celdas) {
                if (GrafoMatriz.esTransitable(cuadricula[pos[0]][pos[1]])) {
                    transitables.add(pos);
                }
            }
            if (transitables.size() != 2) {
                throw new Exception(
                    "Sin 'E'/'S' explícita, se esperaban exactamente 2 celdas transitables "
                    + "en el borde, pero se encontraron " + transitables.size() + ".");
            }
            posEntrada = transitables.get(0);
            posSalida  = transitables.get(1);
            // Marcar en la cuadrícula para consistencia visual
            cuadricula[posEntrada[0]][posEntrada[1]] = 'E';
            cuadricula[posSalida[0]][posSalida[1]]   = 'S';
        }

                // ---- 5. Construir el Grafo ------------------------------------------
        Grafo grafo = new Grafo();
        boolean usaCeldasLogicas = esCuadriculaCeldasLogicas(cuadricula, filas, columnas);

        if (usaCeldasLogicas) {
            construirGrafoCeldasLogicas(cuadricula, filas, columnas, posEntrada, posSalida, grafo);
        } else {
            construirGrafoPorCaracter(cuadricula, filas, columnas, posEntrada, posSalida, grafo);
        }

        // ---- 6. Ensamblar el Laberinto --------------------------------------
        GrafoMatriz lab = new GrafoMatriz();
        lab.setCuadricula(cuadricula);
        lab.setGrafo(grafo);
        lab.setNombreArchivo(nombre);
        lab.setCeldasLogicas(usaCeldasLogicas);

        return lab;
    }
        /**
     * Detecta el formato +--+ : filas = 2R+1, columnas = 3C+1 y un '+'
     * en cada intersección (2r, 3c).
     */
    private static boolean esCuadriculaCeldasLogicas(char[][] cuadricula, int filas, int columnas) {
        if (filas < 3 || columnas < 4) return false;
        if (filas % 2 != 1 || (columnas - 1) % 3 != 0) return false;
        for (int f = 0; f < filas; f += 2) {
            for (int c = 0; c < columnas; c += 3) {
                if (cuadricula[f][c] != '+') return false;
            }
        }
        return true;
    }

    /** Un Vertice por celda lógica (r, cl). id = r * colsLog + cl. */
    private static void construirGrafoCeldasLogicas(char[][] cuadricula, int filas, int columnas,
                                                    int[] posEntrada, int[] posSalida, Grafo grafo) throws Exception {
        int filasLog = (filas - 1) / 2;
        int colsLog  = (columnas - 1) / 3;
        Vertice[][] mapa = new Vertice[filasLog][colsLog];

        for (int r = 0; r < filasLog; r++) {
            int f = 2 * r + 1;
            for (int cl = 0; cl < colsLog; cl++) {
                if (GrafoMatriz.esTransitable(cuadricula[f][3 * cl + 1])
                        || GrafoMatriz.esTransitable(cuadricula[f][3 * cl + 2])) {
                    Vertice v = new Vertice(r * colsLog + cl, r, cl);
                    mapa[r][cl] = v;
                    grafo.agregarVertice(v);
                }
            }
        }

        // Conectar hacia abajo y hacia la derecha si no hay pared en medio
        for (int r = 0; r < filasLog; r++) {
            for (int cl = 0; cl < colsLog; cl++) {
                Vertice actual = mapa[r][cl];
                if (actual == null) continue;

                if (r + 1 < filasLog && mapa[r + 1][cl] != null) {
                    int fPared = 2 * (r + 1);
                    if (GrafoMatriz.esTransitable(cuadricula[fPared][3 * cl + 1])
                            && GrafoMatriz.esTransitable(cuadricula[fPared][3 * cl + 2])) {
                        grafo.agregarArco(actual, mapa[r + 1][cl]);
                    }
                }
                if (cl + 1 < colsLog && mapa[r][cl + 1] != null) {
                    if (GrafoMatriz.esTransitable(cuadricula[2 * r + 1][3 * (cl + 1)])) {
                        grafo.agregarArco(actual, mapa[r][cl + 1]);
                    }
                }
            }
        }

        int[] ent = aCeldaLogica(posEntrada[0], posEntrada[1], filasLog, colsLog, columnas);
        int[] sal = aCeldaLogica(posSalida[0], posSalida[1], filasLog, colsLog, columnas);
        if (mapa[ent[0]][ent[1]] == null || mapa[sal[0]][sal[1]] == null) {
            throw new Exception("La entrada o la salida no cae en una celda transitable.");
        }
        grafo.setEntrada(mapa[ent[0]][ent[1]]);
        grafo.setSalida(mapa[sal[0]][sal[1]]);
    }

    /** Convierte una coordenada del texto (f, c) a su celda lógica (r, cl). */
    private static int[] aCeldaLogica(int f, int c, int filasLog, int colsLog, int columnas) {
        int r = Math.min(filasLog - 1, Math.max(0, (f - 1) / 2));
        int cl;
        if (c <= 0)                 cl = 0;
        else if (c >= columnas - 1) cl = colsLog - 1;
        else                        cl = Math.min(colsLog - 1, (c - 1) / 3);
        return new int[]{r, cl};
    }

    /** Respaldo para archivos que NO tienen el formato +--+ (un vértice por carácter). */
    private static void construirGrafoPorCaracter(char[][] cuadricula, int filas, int columnas,
                                                  int[] posEntrada, int[] posSalida, Grafo grafo) {
        Vertice[][] mapa = new Vertice[filas][columnas];
        for (int f = 0; f < filas; f++) {
            for (int c = 0; c < columnas; c++) {
                if (GrafoMatriz.esTransitable(cuadricula[f][c])) {
                    Vertice v = new Vertice(f * columnas + c, f, c);
                    mapa[f][c] = v;
                    grafo.agregarVertice(v);
                }
            }
        }
        for (int f = 0; f < filas; f++) {
            for (int c = 0; c < columnas; c++) {
                if (mapa[f][c] == null) continue;
                if (c + 1 < columnas && mapa[f][c + 1] != null) grafo.agregarArco(mapa[f][c], mapa[f][c + 1]);
                if (f + 1 < filas && mapa[f + 1][c] != null)    grafo.agregarArco(mapa[f][c], mapa[f + 1][c]);
            }
        }
        grafo.setEntrada(mapa[posEntrada[0]][posEntrada[1]]);
        grafo.setSalida(mapa[posSalida[0]][posSalida[1]]);
    }
    // Utilidades privadas
    /** Verifica que el carácter es uno de los permitidos por la especificación. */
    private static boolean esCaracterValido(char c) {
        return c == '+' || c == '-' || c == '|'
            || c == ' ' || c == '#' || c == 'E' || c == 'S';
    }

    /**
     * Recorre el perímetro del laberinto en orden:
     * fila 0 de izq a der → columna derecha de arriba a abajo →
     * última fila de der a izq → columna izquierda de abajo a arriba.
     * Las esquinas se cuentan una sola vez.
     */
    private static List<int[]> perimetroEnOrden(char[][] c, int filas, int columnas) {
        List<int[]> lista = new ArrayList<>();
        for (int f = 0; f < filas; f++) {
            for (int col = 0; col < columnas; col++) {
                boolean esBorde = (f == 0 || f == filas - 1 || col == 0 || col == columnas - 1);
                if (esBorde) lista.add(new int[]{f, col});
            }
        }
        return lista;
    }
}
