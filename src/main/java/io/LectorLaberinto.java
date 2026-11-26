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

        // Generar id único por posición: id = fila * columnas + columna
        // (mismo esquema que GrafoMatriz existente)
        Vertice[][] mapa = new Vertice[filas][columnas];

        for (int f = 0; f < filas; f++) {
            for (int c = 0; c < columnas; c++) {
                if (GrafoMatriz.esTransitable(cuadricula[f][c])) {
                    int id = f * columnas + c;
                    Vertice v = new Vertice(id, f, c);
                    mapa[f][c] = v;
                    grafo.agregarVertice(v);
                }
            }
        }

        // Conectar vecinos ortogonales (no diagonal, no atravesar paredes)
        int[][] deltas = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};
        for (int f = 0; f < filas; f++) {
            for (int c = 0; c < columnas; c++) {
                if (mapa[f][c] == null) continue;
                for (int[] d : deltas) {
                    int nf = f + d[0];
                    int nc = c + d[1];
                    if (nf >= 0 && nf < filas && nc >= 0 && nc < columnas
                            && mapa[nf][nc] != null) {
                        // Agregar solo en una dirección; el grafo lo hace bidireccional
                        // pero como recorremos todo, evitar duplicados añadiendo
                        // solo hacia abajo y hacia la derecha.
                        if (d[0] > 0 || d[1] > 0) {
                            grafo.agregarArco(mapa[f][c], mapa[nf][nc]);
                        }
                    }
                }
            }
        }

        // Registrar entrada y salida en el grafo
        grafo.setEntrada(mapa[posEntrada[0]][posEntrada[1]]);
        grafo.setSalida(mapa[posSalida[0]][posSalida[1]]);

        // ---- 6. Ensamblar el Laberinto --------------------------------------
        GrafoMatriz lab = new GrafoMatriz();
        lab.setCuadricula(cuadricula);
        lab.setGrafo(grafo);
        lab.setNombreArchivo(nombre);

        return lab;
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
