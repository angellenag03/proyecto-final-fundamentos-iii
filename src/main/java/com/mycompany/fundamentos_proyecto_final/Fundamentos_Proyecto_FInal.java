package com.mycompany.fundamentos_proyecto_final;

public class Fundamentos_Proyecto_FInal {
    public static String texto = "";
    public static byte labetintoSeleccionado=-1;
    public static void main(String[] args) {
        menu();
    }//Close Main
    public static void menu(){
        java.util.Scanner s = new java.util.Scanner(System.in);
        byte menu = 0;
        do {   
            if(texto.isEmpty())
                System.out.println("No se ha cargago ningún laberinto");
            if(!texto.isEmpty())
                System.out.printf("Se ha seleccionado el laberinto: %d\n", labetintoSeleccionado);
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
