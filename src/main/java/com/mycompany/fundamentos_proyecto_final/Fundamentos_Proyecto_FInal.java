package com.mycompany.fundamentos_proyecto_final;
import io.LectorLaberinto;
import modelo.Grafo;
import modelo.GrafoMatriz;
import modelo.Vertice;
import ui.VisualizadorLaberinto;

import java.util.List;
import ui.MainFrame;
public class Fundamentos_Proyecto_FInal {
   public static GrafoMatriz laberinto = null;
    private static final java.util.Scanner s = new java.util.Scanner(System.in);

    private static final int ALG_DFS = 0, ALG_BFS = 1, ALG_DIJKSTRA = 2;
    private static final String[] NOMBRES = {"DFS", "BFS", "Dijkstra"};
    public static void main(String[] args) {
        menu();
    }//Close Main
    private static int leerEntero(){
        try{
            return Integer.parseInt(s.nextLine().trim());
        }catch(NumberFormatException e){
            return -1;
        }
    }//close leerEnter
    public static void menu(){
        int opcion = 0;
        do {   
            if (laberinto == null)
                System.out.println("No se ha cargado ningún laberinto");
            else
                System.out.printf("Se ha seleccionado el laberinto: %s%n", laberinto.getNombreArchivo());
            System.out.println("======MENU=======\n"+
                                    "Elecciones:\n" +
                                    "1.-Cargar laberinto\n" +
                                    "2.-Resolver laberinto con DFS\n" +
                                    "3.-Resolver laberinto con BFS\n" +
                                    "4-.Resolver laberinto con Dijkstra (camino más corto)\n" +
                                    "5.-Mostrar las tres soluciones\n" +
                                    "6.-Abrir Interfaz Gráfica\n" +
                                    "7.-Salir"
                                );
            opcion = leerEntero();
            switch (opcion) {
                case 1:
                    System.out.println("Cargando laberinto");
                    elegirLaberinto();
                    System.out.println("\n");
                    break;
                case 2:
                    System.out.println("Resolución por DFS:");
                    resolverDFS();
                    System.out.println("\n");
                    break;
                case 3:
                    System.out.println("Resolución BFS:");
                    resolverBFS();
                    System.out.println("\n");
                    break;
                case 4:
                    System.out.println("Camino más corto:");
                    resolverCorto();
                    System.out.println("\n");
                    break;
                case 5:
                    System.out.println("Imprimiento todas las soluciones:");
                    mostrarTodasSoluciones();
                    System.out.println("\n");
                    break;
                case 6:
                    System.out.println("Abriendo Interfaz Gráfica");
                    MainFrame frame = new MainFrame();
                    frame.setVisible(true);
                    break;
                case 7:
                    System.out.println("Adiós");
                    break;
                default:
                    System.out.println("Elija dentro del rango");
                    System.out.println("\n");
                    break;
            }
        } while (opcion != 6);

    }//Close Manu
    private static void elegirLaberinto(){
        java.io.File carpeta = new java.io.File(".");
        java.io.File[] archivos = carpeta.listFiles(
        (dir, nombre) -> nombre.toLowerCase().endsWith(".txt"));
        if (archivos == null || archivos.length == 0) {
            System.out.println("No se encontraron laberintos en: "+ carpeta.getPath());
            return;
        }
        java.util.Arrays.sort(archivos);
        System.out.println("Laberintos disponibles:");
        for (int i = 0; i < archivos.length; i++) {
            System.out.println((i+1) + ".- "+archivos[i].getName());
        }
        System.out.println("0.- Cancelar");
        int opcion;
        do {            
            System.out.println("Elige el laberinto");
            opcion = leerEntero();
            if (opcion < 0 || opcion>archivos.length){
                System.out.println("Elección no válida");
                opcion = -1;
            }
        } while (opcion == -1);
        if (opcion == 0) {
            System.out.println("Cancelando...");
            return;
        }
        java.io.File elegido =archivos[opcion - 1];
        try{
            GrafoMatriz nuevo = LectorLaberinto.cargarDesdeArchivo(elegido.getPath());
            laberinto= nuevo;
            System.out.println("Cargando: " + laberinto.getNombreArchivo());
        }catch (Exception e){
            System.out.println("No se pudo cargar el archivo, " + e.getMessage());
        }
    }//close elegirLbaerinto
    private static boolean hayLaberitno(){
        if (laberinto == null) {
            System.out.println("No hay laberitno");
            return false;
        }
        return true;
    }
    private static boolean preguntarAnimacion(){
        System.out.println("¿Desea ver la animación?(s/n)");
        return s.nextLine().trim().equalsIgnoreCase("s");
    }
    private static int resolver (int algoritmo, boolean animar){
        Grafo grafo = laberinto.getGrafo();
        long t0 = System.nanoTime();
        List<Vertice> orden;
        switch (algoritmo){
            case ALG_DFS: 
                orden = grafo.DFS();
                break;
            case ALG_BFS:
                orden = grafo.BFS();
                break;
            default:
                orden = grafo.caminoMasCorto();
                break;
        }
        long tiempo = System.nanoTime() - t0;
        if (animar) {
            VisualizadorLaberinto.despliegaLaberinto(laberinto, orden, grafo);
        }
        VisualizadorLaberinto.imprimirResultado(NOMBRES[algoritmo], laberinto, orden, grafo, tiempo);
        return grafo.haySolucion() ? grafo.getCamino().size() - 1 : -1;
    }
    private static void resolverDFS(){
        if (!hayLaberitno()) {
            return;
        }
        resolver(ALG_DFS, preguntarAnimacion());
    }//close resolverDFS
    private static void resolverBFS(){
        if (!hayLaberitno()) {
            return;
        }
        resolver(ALG_BFS, preguntarAnimacion());
    }//close resolverBFS
    private static void resolverCorto(){
        if (!hayLaberitno()) {
            return;
        }
        resolver(ALG_DIJKSTRA, preguntarAnimacion());
    }//Close resolverCorto
    private static void mostrarTodasSoluciones(){
        if (!hayLaberitno()) {
            return;
        }
        int[] longitudes = new int[3];
        for (int a = 0; a < 3; a++) {
            longitudes[a] = resolver(a, false);
        }
        System.out.println("======RESUMEN======");
        for (int a = 0; a < 3; a++) {
            String texto = longitudes[a] >= 0 ? longitudes[a] + " movimientos" : "sin solución";
            System.out.printf("%-9s %s%n", NOMBRES[a], texto);
        }
    }//Close mostrarTodasSoluciones
}
