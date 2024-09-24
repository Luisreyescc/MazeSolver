package mx.unam.ciencias.edd.proyecto3;

import mx.unam.ciencias.edd.*;
import java.io.BufferedInputStream;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.Random;

/**
 * Clase que representa un laberinto.
 * Implementa Iterable para poder iterar sobre los cuartos del laberinto.
 */
public class Laberinto implements Iterable<Cuarto> {
    private Cuarto[][] mz;
    private Cuarto inicio;
    private Cuarto fin;
    private final int d = 20;
    private Grafica<Cuarto> grafica;
    private StringBuilder strbdr;
    private int ancho, alto;
    private Random r = new Random();

    /**
     * Constructor que crea un laberinto con el ancho y alto especificados.
     *
     * @param ancho el ancho del laberinto.
     * @param alto  el alto del laberinto.
     */
    public Laberinto(int ancho, int alto) {
        this.ancho = ancho;
        this.alto = alto;
        this.mz = new Cuarto[alto][ancho];
        for (int vertical = 0; vertical < alto; vertical++) {
            for (int horizontal = 0; horizontal < ancho; horizontal++)
                mz[vertical][horizontal] = new Cuarto(vertical, horizontal, ancho, alto);
        }

        strbdr = new StringBuilder();
        inicio = inicioLab();
        inicio.InicioCamino();

        fin = finalLab();
        fin.FinalCamino();
        generaLaberinto();
    }

    /**
     * Constructor que crea un laberinto con el ancho, alto y semilla especificados.
     *
     * @param ancho   el ancho del laberinto.
     * @param alto    el alto del laberinto.
     * @param semilla la semilla para el generador de números aleatorios.
     */
    public Laberinto(int ancho, int alto, int semilla) {
        this.r = new Random(semilla);
        this.ancho = ancho;
        this.alto = alto;
        this.mz = new Cuarto[alto][ancho];
        for (int vertical = 0; vertical < alto; vertical++) {
            for (int horizontal = 0; horizontal < ancho; horizontal++)
                mz[vertical][horizontal] = new Cuarto(vertical, horizontal, ancho, alto);
        }

        strbdr = new StringBuilder();
        inicio = inicioLab();
        inicio.InicioCamino();

        fin = finalLab();
        fin.FinalCamino();
        generaLaberinto();
    }

    /**
     * Constructor que crea un laberinto a partir de un flujo de entrada.
     *
     * @param in el flujo de entrada desde el cual se leerá el laberinto.
     */
    public Laberinto(BufferedInputStream in) {
        strbdr = new StringBuilder();
        try {
            int xyz = 0;
            int vertical = 0;

            int y = 0;
            int x = 0;
            while ((vertical = in.read()) != -1) {
                if (xyz > 5) {
                    mz[x][y] = new Cuarto(x, y, (byte) vertical, ancho, alto);
                    if (y == ancho - 1) {
                        y = 0;
                        x = x + 1;
                    } else {
                        y = y + 1;
                    }
                } else {
                    if (xyz == 0 && vertical != 0x4d)
                        throw new Exception("Necesario: 0x4d");
                    if (xyz == 1 && vertical != 0x41)
                        throw new Exception("Necesario: 0x41");
                    if (xyz == 2 && vertical != 0x5a)
                        throw new Exception("Necesario: 0x5a");
                    if (xyz == 3 && vertical != 0x45)
                        throw new Exception("Necesario: 0x45");
                    if (xyz == 4) {
                        this.alto = vertical;
                    }
                    if (xyz == 5) {
                        this.ancho = vertical;
                        mz = new Cuarto[alto][ancho];
                    }
                    xyz++;
                }
            }
            in.close();
        } catch (Exception x) {
            System.err.printf("Error con el archivo MAZE");
        }

        Lista<Cuarto> vecinillos = new Lista<>();
        for (Cuarto cuarto : this) {
            if (cuarto.tipo != 9) {
                if ((cuarto.tipo == 8 || cuarto.tipo == 7 || cuarto.tipo == 6) && cuarto.este == false) {
                    vecinillos.agrega(cuarto);
                }
                if ((cuarto.tipo == 2 || cuarto.tipo == 3 || cuarto.tipo == 4) && cuarto.oeste == false) {
                    vecinillos.agrega(cuarto);
                }
            }
        }

        if (vecinillos.getElementos() != 2) {
            System.err.println("No se encontraron puntos de inicio y fin válidos.");
            return;
        }

        this.inicio = vecinillos.getPrimero();
        inicio.InicioCamino();

        this.fin = vecinillos.getUltimo();
        fin.FinalCamino();
        generaGrafo();
    }

    /**
     * Tumba paredes aleatorias en el laberinto.
     *
     * @param walls el número de paredes a tumbar.
     */
    private void tumbaAleatorio(int walls) {
        for (int vertical = 0; vertical < walls; vertical++) {
            int fAleatoria;
            int cAleatoria;
            if (alto > 2) {
                fAleatoria = r.nextInt(alto - 2) + 1;
            } else {
                continue;
            }
            if (ancho > 2) {
                cAleatoria = r.nextInt(ancho - 2) + 1;
            } else {
                continue;
            }

            Cuarto cuarto = mz[fAleatoria][cAleatoria];
            boolean[] paredes = { cuarto.norte, cuarto.este, cuarto.sur, cuarto.oeste };

            for (int horizontal = 0; horizontal < 4; horizontal++) {
                int paredAleatoria = r.nextInt(4);

                if (paredes[paredAleatoria]) {
                    switch (paredAleatoria) {
                        case 0:
                            cuarto.norte = false;
                            break;
                        case 1:
                            cuarto.este = false;
                            break;
                        case 2:
                            cuarto.sur = false;
                            break;
                        case 3:
                            cuarto.oeste = false;
                            break;
                    }
                    break;
                }
            }
        }
    }

    /**
     * Genera el laberinto usando DFS.
     */
    private void generaLaberinto() {
        Pila<Cuarto> pila = new Pila<>();

        pila.mete(inicio);
        while (!pila.esVacia()) {
            Cuarto cuarto = pila.mira();
            cuarto.visitado = true;

            Lista<Cuarto> n = meteNei(cuarto);
            if (!n.esVacia()) {
                if (n.getLongitud() == 1) {
                    quitaAdyacentes(cuarto, n.getPrimero());
                    pila.mete(n.getPrimero());
                } else {
                    int random = r.nextInt(n.getLongitud());
                    Cuarto siguiente = n.get(random);
                    quitaAdyacentes(cuarto, siguiente);
                    pila.mete(siguiente);
                }
            } else {
                pila.saca();
            }
        }

        int total = (int) (0.1 * alto * ancho);
        tumbaAleatorio(total);

        generaGrafo();
    }

    /**
     * Resuelve el laberinto utilizando el algoritmo de Dijkstra.
     */
    public void resuelveMaze() {
        if (grafica == null) {
            System.err.println("La gráfica no ha sido generada.");
            return;
        }

        Lista<VerticeGrafica<Cuarto>> l = grafica.dijkstra(inicio, fin);
        Lista<Cuarto> cuarto = new Lista<>();

        for (VerticeGrafica<Cuarto> v : l) {
            cuarto.agrega(v.get());
        }

        for (Cuarto a : cuarto) {
            if (!a.oeste && a.horizontal < ancho - 1 && cuarto.contiene(mz[a.vertical][a.horizontal + 1])) {
                strbdr.append(a.rectaEnX());
            }

            if (!a.sur && a.vertical < alto - 1 && cuarto.contiene(mz[a.vertical + 1][a.horizontal])) {
                strbdr.append(a.rectaEnY());
            }
        }
    }

    /**
     * Quita las paredes adyacentes entre dos cuartos.
     *
     * @param cuartoX el primer cuarto.
     * @param cuartoZ el segundo cuarto.
     */
    private void quitaAdyacentes(Cuarto cuartoX, Cuarto cuartoZ) {
        if (cuartoX.vertical == cuartoZ.vertical) {
            if (cuartoX.horizontal < cuartoZ.horizontal) {
                cuartoZ.este = false;
                cuartoX.oeste = false;
            } else {
                cuartoX.este = false;
                cuartoZ.oeste = false;
            }
        } else {
            if (cuartoX.vertical < cuartoZ.vertical) {
                cuartoZ.norte = false;
                cuartoX.sur = false;
            } else {
                cuartoX.norte = false;
                cuartoZ.sur = false;
            }
        }
    }

    /**
     * Obtiene una lista de cuartos vecinos no visitados del cuarto dado.
     *
     * @param cuarto el cuarto para el cual se obtendrán los vecinos.
     * @return una lista de cuartos vecinos no visitados.
     */
    private Lista<Cuarto> meteNei(Cuarto cuarto) {
        Cuarto cuartoX = null, cuartoZ = null, cuartoR = null, cuartoY = null;
        Lista<Cuarto> n = new Lista<>();

        if (cuarto.vertical >= 0 && cuarto.vertical < alto && cuarto.horizontal >= 0 && cuarto.horizontal < ancho) {
            switch (cuarto.tipo) {
                case 1:
                    if (cuarto.horizontal + 1 < ancho)
                        cuartoX = mz[cuarto.vertical][cuarto.horizontal + 1];
                    if (cuarto.vertical + 1 < alto)
                        cuartoZ = mz[cuarto.vertical + 1][cuarto.horizontal];
                    if (cuarto.horizontal - 1 >= 0)
                        cuartoR = mz[cuarto.vertical][cuarto.horizontal - 1];

                    if (cuartoX != null && !cuartoX.visitado)
                        n.agrega(cuartoX);
                    if (cuartoZ != null && !cuartoZ.visitado)
                        n.agrega(cuartoZ);
                    if (cuartoR != null && !cuartoR.visitado)
                        n.agrega(cuartoR);
                    break;
                case 2:
                    if (cuarto.horizontal - 1 >= 0)
                        cuartoX = mz[cuarto.vertical][cuarto.horizontal - 1];
                    if (cuarto.vertical + 1 < alto)
                        cuartoZ = mz[cuarto.vertical + 1][cuarto.horizontal];

                    if (cuartoX != null && !cuartoX.visitado)
                        n.agrega(cuartoX);
                    if (cuartoZ != null && !cuartoZ.visitado)
                        n.agrega(cuartoZ);
                    break;
                case 3:
                    if (cuarto.vertical + 1 < alto)
                        cuartoX = mz[cuarto.vertical + 1][cuarto.horizontal];
                    if (cuarto.horizontal - 1 >= 0)
                        cuartoZ = mz[cuarto.vertical][cuarto.horizontal - 1];
                    if (cuarto.vertical - 1 >= 0)
                        cuartoR = mz[cuarto.vertical - 1][cuarto.horizontal];

                    if (cuartoX != null && !cuartoX.visitado)
                        n.agrega(cuartoX);
                    if (cuartoZ != null && !cuartoZ.visitado)
                        n.agrega(cuartoZ);
                    if (cuartoR != null && !cuartoR.visitado)
                        n.agrega(cuartoR);
                    break;
                case 4:
                    if (cuarto.horizontal - 1 >= 0)
                        cuartoX = mz[cuarto.vertical][cuarto.horizontal - 1];
                    if (cuarto.vertical - 1 >= 0)
                        cuartoZ = mz[cuarto.vertical - 1][cuarto.horizontal];

                    if (cuartoX != null && !cuartoX.visitado)
                        n.agrega(cuartoX);
                    if (cuartoZ != null && !cuartoZ.visitado)
                        n.agrega(cuartoZ);
                    break;
                case 5:
                    if (cuarto.horizontal - 1 >= 0)
                        cuartoX = mz[cuarto.vertical][cuarto.horizontal - 1];
                    if (cuarto.vertical - 1 >= 0)
                        cuartoZ = mz[cuarto.vertical - 1][cuarto.horizontal];
                    if (cuarto.horizontal + 1 < ancho)
                        cuartoR = mz[cuarto.vertical][cuarto.horizontal + 1];

                    if (cuartoX != null && !cuartoX.visitado)
                        n.agrega(cuartoX);
                    if (cuartoZ != null && !cuartoZ.visitado)
                        n.agrega(cuartoZ);
                    if (cuartoR != null && !cuartoR.visitado)
                        n.agrega(cuartoR);
                    break;
                case 6:
                    if (cuarto.vertical - 1 >= 0)
                        cuartoX = mz[cuarto.vertical - 1][cuarto.horizontal];
                    if (cuarto.horizontal + 1 < ancho)
                        cuartoZ = mz[cuarto.vertical][cuarto.horizontal + 1];

                    if (cuartoX != null && !cuartoX.visitado)
                        n.agrega(cuartoX);
                    if (cuartoZ != null && !cuartoZ.visitado)
                        n.agrega(cuartoZ);
                    break;
                case 7:
                    if (cuarto.vertical + 1 < alto)
                        cuartoX = mz[cuarto.vertical + 1][cuarto.horizontal];
                    if (cuarto.horizontal + 1 < ancho)
                        cuartoZ = mz[cuarto.vertical][cuarto.horizontal + 1];
                    if (cuarto.vertical - 1 >= 0)
                        cuartoR = mz[cuarto.vertical - 1][cuarto.horizontal];

                    if (cuartoX != null && !cuartoX.visitado)
                        n.agrega(cuartoX);
                    if (cuartoZ != null && !cuartoZ.visitado)
                        n.agrega(cuartoZ);
                    if (cuartoR != null && !cuartoR.visitado)
                        n.agrega(cuartoR);
                    break;
                case 8:
                    if (cuarto.horizontal + 1 < ancho)
                        cuartoX = mz[cuarto.vertical][cuarto.horizontal + 1];
                    if (cuarto.vertical + 1 < alto)
                        cuartoZ = mz[cuarto.vertical + 1][cuarto.horizontal];

                    if (cuartoX != null && !cuartoX.visitado)
                        n.agrega(cuartoX);
                    if (cuartoZ != null && !cuartoZ.visitado)
                        n.agrega(cuartoZ);
                    break;
                case 9:
                    if (cuarto.vertical + 1 < alto)
                        cuartoX = mz[cuarto.vertical + 1][cuarto.horizontal];
                    if (cuarto.horizontal - 1 >= 0)
                        cuartoZ = mz[cuarto.vertical][cuarto.horizontal - 1];
                    if (cuarto.vertical - 1 >= 0)
                        cuartoR = mz[cuarto.vertical - 1][cuarto.horizontal];
                    if (cuarto.horizontal + 1 < ancho)
                        cuartoY = mz[cuarto.vertical][cuarto.horizontal + 1];

                    if (cuartoX != null && !cuartoX.visitado)
                        n.agrega(cuartoX);
                    if (cuartoZ != null && !cuartoZ.visitado)
                        n.agrega(cuartoZ);
                    if (cuartoR != null && !cuartoR.visitado)
                        n.agrega(cuartoR);
                    if (cuartoY != null && !cuartoY.visitado)
                        n.agrega(cuartoY);
                    break;
                default:
                    break;
            }
        }
        return n;
    }

    /**
     * Define el punto de inicio del laberinto.
     *
     * @return el cuarto de inicio.
     */
    private Cuarto inicioLab() {
        int alturaRandom = r.nextInt(alto);
        return mz[alturaRandom][0];
    }

    /**
     * Define el punto final del laberinto.
     *
     * @return el cuarto final.
     */
    private Cuarto finalLab() {
        int alturaRandom = r.nextInt(alto);
        return mz[alturaRandom][ancho - 1];
    }

    /**
     * Convierte el laberinto al formato de bytes.
     */
    public void formatoBytes() {
        byte b1 = (byte) (0x4d);
        byte b2 = (byte) (0x41);
        byte b3 = (byte) (0x5a);
        byte b4 = (byte) (0x45);

        byte f = (byte) (alto & 0xFF);
        byte c = (byte) (ancho & 0xFF);

        try {
            PrintStream o = new PrintStream(System.out);
            o.write(b1);
            o.write(b2);
            o.write(b3);
            o.write(b4);
            o.write(f);
            o.write(c);
            for (Cuarto cuarto : this) {
                int b = ((int) (cuarto.byteFormatter())) & 0xff;
                o.write(b);
            }
            o.close();
        } catch (Exception ioe) {
        }
    }

    /**
     * Genera la gráfica que representa el laberinto.
     */
    private void generaGrafo() {
        grafica = new Grafica<>();

        for (Cuarto cuarto : this)
            grafica.agrega(cuarto);

        for (Cuarto cuarto : this) {
            if (!cuarto.oeste && cuarto.horizontal < ancho - 1)
                grafica.conecta(cuarto, mz[cuarto.vertical][cuarto.horizontal + 1],
                        1 + cuarto.puntaje + mz[cuarto.vertical][cuarto.horizontal + 1].puntaje);

            if (!cuarto.sur && cuarto.vertical < alto - 1)
                grafica.conecta(cuarto, mz[cuarto.vertical + 1][cuarto.horizontal],
                        1 + cuarto.puntaje + mz[cuarto.vertical + 1][cuarto.horizontal].puntaje);
        }
    }

    /**
     * Imprime el laberinto en formato SVG.
     *
     * @return una cadena con el contenido en formato SVG.
     */
    public String imprimeSVG() {
        if (inicio == null || fin == null) {
            throw new IllegalStateException();
        }

        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version = \"1.0\" encoding = \"utf-8\" ?>\n");
        sb.append(String.format("<svg width=\"%d\" height=\"%d\">\n", ancho * d, alto * d));
        for (Cuarto cuarto : this)
            sb.append(cuarto.paredSVG());

        sb.append(strbdr);
        sb.append(inicio.marcaInicio());
        sb.append(fin.marcaFinal());

        sb.append("</svg>");
        return sb.toString();
    }

    @Override
    public Iterator<Cuarto> iterator() {
        return new Iterador();
    }

    /**
     * Clase interna para iterar sobre los cuartos del laberinto.
     */
    private class Iterador implements Iterator<Cuarto> {
        private int fila;
        private int columna;

        /**
         * Constructor del iterador.
         */
        public Iterador() {
        }

        /**
         * Verifica si hay más elementos en el iterador.
         *
         * @return true si hay más elementos, false en caso contrario.
         */
        @Override
        public boolean hasNext() {
            return fila < alto && columna < ancho;
        }

        /**
         * Obtiene el siguiente cuarto en la iteración.
         *
         * @return el siguiente cuarto.
         */
        @Override
        public Cuarto next() {
            Cuarto cuarto = mz[fila][columna];
            if (columna == ancho - 1) {
                columna = 0;
                fila++;
                return cuarto;
            }
            columna++;
            return cuarto;
        }
    }
}
