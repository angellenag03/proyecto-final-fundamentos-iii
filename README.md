# proyecto-final-fundamentos-iii 
# Alumnos: Chavez Salcido Jesus Andres y Sanches Navarro Angel Adrian
Uso en forma apoyo del agente Claude en:
Funfamentos_Proyecto_Final para clase resolver,elegirLaberinto, y correcciones generales
en las demás clases, creacion y borrador de clase MainFrame.java.

Uso del agente Gemini antigraviti en:
Creación de Clases io, modelo, ui.

Aclaraciones de uso:
Los agentes se usaron bajo supervisión humana, nos encargamos de corregir,
editar, implementar y comprender cada línea de código que este proyecto presenta
En los métodos en los cuales se mantienen los comentarios es para una mejor comprensión 
del funcinamiento de estos, pero el código y lógica es comprendida por ambos miembro del 
equipo. Este READNE fue escrito a mano como prueba de la comprension del funcionamiento de los metodos

Explicaciones del proyecto:
# Java with maven
# IDE 
recomendada para visualizar la animación: Netbeans (Donde fue desarrollada) e Intelliji
# ui: 
es donde se aloja el visualizador gráfico de la resolución del laberinto y los elementos de la
interfaz grafica
# modelo: 
es donde se aloja el esqueleto de los grafos, los vertices, el conjunto de vertices,
sus arcos, etc.
# io: 
validación de todo el laberitno en general
# InterfazGrafica
Se corre desde la clase FrameTest.java, es una interfaz grafica, sin animacion ni comparacion
de los metodos de resolucion

# Explicacion de clases
En lo que queda de README Escplicaremos de manera rapida y sencilla el comportamiento de las clases
# FrameTest.java
Esta clase se encarga solamente de llamar a la clase MainFrame.java donde realmente ocurre la logica y diseño
# Fundamentos_Proyecto_Final.java
Es la clase principal del apartado de consola, en esta clase se encuentra el menu, la elección y carga de los laberintos,
y en general todo lo que se puede llamar en el menu, y lo que se necesita para que todo funcione de manera correcta
# PruebaLectura.java
Es una clase que puede elimarse, sirve para hacer pruebas rapidas con los laberitnos, y sus metodos funcionan correctamente
puede eliminarse sin afectar el uso del programa
# LectorLaberinto.java
Clase usada para convertie el arhivo txt a grafos con los cuales podamos trabajar
# Arcos
Clase que representa una arista/arco del grafo, se declara el oriden, destino y costo, aunque
al ser un no dirigido el costo es de 1
# Grafo.java
Guarda todas las conexiones, dandole a cada id su lista de arcos saliente, actuando mas como una lista de adyacencia,
tambien es la clase donde los algoritmos se encuentran
# GrafosMatriz.java
Es el contenedor de todo el laberinto, dibuja el laberinto como una matriz 2x2 y le da formato,
se encarga de cuadricular todo el laberinto y sus soluciones
# Vertice
Representa cada celda del laberinto y guarda los datos, donde se puede devolver el id de la celda en un hashCode
# Vertices
Es donde se guardan todos los vertices en una Hashtable, la manera en la que se guarda permite que la busqueda sea 
de una manera mas optima y rapida
# MainFrame.java
Ventana  de la interfaz grafica, tiene todos los metodos de busqueda, resumiendo, es todo el proyecto, pero en interfaz
grafica
# Visualizador Laberitno
Se encarga de dibujar todo el laberitno en la consola, la animacion, y demas elementos del laberinto.