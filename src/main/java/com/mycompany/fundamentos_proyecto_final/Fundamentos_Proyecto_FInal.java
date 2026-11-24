package com.mycompany.fundamentos_proyecto_final;
import java.util.Scanner;
public class Fundamentos_Proyecto_FInal {
    public static String texto = "";
    private static final java.util.Scanner s = new java.util.Scanner(System.in);
    public static String labetintoSeleccionado="";
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
        byte menu = 0;
        do {   
            if(texto.isEmpty())
                System.out.println("No se ha cargago ningún laberinto");
            if(!texto.isEmpty())
                System.out.printf("Se ha seleccionado el laberinto: %s\n", labetintoSeleccionado);
            System.out.println("======MENU=======\n"+
                                    "Elecciones:\n" +
                                    "1.-Cargar laberinto\n" +
                                    "2.-Resolver laberinto con DFS\n" +
                                    "3.-Resolver laberinto con BFS\n" +
                                    "4-.Resolver laberinto con Dijkstra (camino más corto)\n" +
                                    "5.-Mostrar las tres soluciones\n" +
                                    "6.-Salir");
            menu = s.nextByte();
            switch (menu) {
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
                    System.out.println("Adiós");
                    break;
                default:
                    System.out.println("Elija dentro del rango");
                    System.out.println("\n");
                    break;
            }
        } while (menu != 6);

    }//Close Manu
    public static String cargarLaberinto(String ruta){
        String contenido = "";
        try{
            java.io.File archivo = new java.io.File(ruta);
            java.util.Scanner scanner = new java.util.Scanner(archivo);
            while (scanner.hasNextLine()) {
                contenido += scanner.nextLine() + "\n";
            }
        }catch (Exception e){
            System.out.println("Problema al cargar archivo: " + e.getMessage());
        }
        return contenido;
    }//Close cargarLaberinto
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
            System.out.println((i+1) + ".- "+archivos[i]);
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
        String contenido = cargarLaberinto(elegido.getPath());
        if (contenido.isEmpty()) {
            System.out.println("Archivo vacio o error al leer");
            return;
       }
        texto = contenido;
        labetintoSeleccionado = elegido.getName();
        System.out.println("Cargando: " + labetintoSeleccionado); 
    }//close elegirLbaerinto
    private static void resolverDFS(){
    
    }//close resolverDFS
    private static void resolverBFS(){
    
    }//close resolverBFS
    private static void resolverCorto(){
    
    }//Close resolverCorto
    private static void mostrarTodasSoluciones(){
    
    }//Close mostrarTodasSoluciones
}
