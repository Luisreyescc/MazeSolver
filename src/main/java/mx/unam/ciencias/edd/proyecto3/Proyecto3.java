package mx.unam.ciencias.edd.proyecto3;

import java.io.BufferedInputStream;

/**
 * Clase principal del proyecto 3.
 */
public class Proyecto3 {

    /**
     * Método principal que ejecuta el programa.
     *
     * @param args los argumentos de la línea de comandos.
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            try {
                BufferedInputStream in = new BufferedInputStream(System.in);
                Laberinto maze = new Laberinto(in);
                maze.resuelveMaze();
                System.out.println(maze.imprimeSVG());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Argumentos argumentos = new Argumentos(args);
            Laberinto maze;

            if (argumentos.getSemilla() == null) {
                maze = new Laberinto(argumentos.getAncho(), argumentos.getAlto());
            } else {
                maze = new Laberinto(argumentos.getAncho(), argumentos.getAlto(), argumentos.getSemilla());
            }

            maze.formatoBytes();
        }
    }
}
