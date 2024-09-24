package mx.unam.ciencias.edd.proyecto3;

import mx.unam.ciencias.edd.Lista;

/**
 * Clase que maneja y procesa los argumentos de la línea de comandos para el
 * proyecto.
 * 
 */
public class Argumentos {
    private Integer semilla = null;
    private int alto;
    private int ancho;
    private Lista<String> parametro;

    /**
     * Método que imprime el mensaje de uso del programa.
     */
    public void uso() {
        System.err.println("\nPara generar un laberinto:");
        System.err.println("java -jar target/proyecto3.jar -g -s <semilla> -w <ancho> -h <alto>");
        System.err.println("\nPara resolver un laberinto:");
        System.err.println("java -jar target/proyecto3.jar < input.mze > output.svg");
    }

    /**
     * Obtiene la semilla utilizada para la generación del laberinto.
     * 
     * @return la semilla o null si no se especificó.
     */
    public Integer getSemilla() {
        return semilla;
    }

    /**
     * Obtiene el valor del alto del laberinto.
     * 
     * @return el alto del laberinto.
     */

    public int getAlto() {
        return alto;
    }

    /**
     * Obtiene el valor del ancho del laberinto.
     * 
     * @return el ancho del laberinto.
     */
    public int getAncho() {
        return ancho;
    }

    /**
     * Constructor que procesa los argumentos de la línea de comandos.
     * 
     * @param args los argumentos de la línea de comandos.
     */
    public Argumentos(String[] args) {
        this.parametro = new Lista<>();

        for (String s : args) {
            parametro.agrega(s);
        }

        if (!parametro.contiene("-g")) {
            System.err.print("Es necesario el parametro -g.");
            uso();
            System.exit(1);
        } else {
            int vertical = parametro.indiceDe("-g");
            String g = parametro.get(vertical);
            parametro.elimina(g);
        }

        processFlag("-w");
        processFlag("-h");

        if (parametro.contiene("-s")) {
            processFlag("-s");
        }
    }

    /**
     * Constructor que procesa los argumentos de la línea de comandos.
     * 
     * @param args los argumentos de la línea de comandos.
     */
    private void processFlag(String arg) {
        if (!parametro.contiene(arg)) {
            if (!arg.equals("-s")) {

                System.err.printf("Parametro %s inexistente", arg);
                uso();
                System.exit(1);
            }
        } else {
            int vertical = parametro.indiceDe(arg);
            try {

                if (vertical + 1 >= parametro.getLongitud()) {
                    throw new Exception();
                }
                String value = parametro.get(vertical + 1);
                int i = Integer.parseInt(value);

                if (arg.equals("-w") || arg.equals("-h")) {
                    if (i < 2) {
                        System.err.printf("%s tiene que ser mayor a 2%n", arg);
                        uso();
                        System.exit(1);
                    }
                    if (i > 255) {
                        System.err.printf("%s tiene que ser menor que 256%n", arg);
                        uso();
                        System.exit(1);
                    }

                }

                switch (arg) {
                    case "-w":
                        this.ancho = i;
                        break;
                    case "-h":
                        this.alto = i;
                        break;
                    case "-s":
                        this.semilla = i;
                        break;
                }

                parametro.elimina(arg);
                parametro.elimina(value);
            } catch (Exception e) {

                System.err.printf("Es necesario un entero.", arg);
                uso();
                System.exit(1);
            }
        }
    }

}
