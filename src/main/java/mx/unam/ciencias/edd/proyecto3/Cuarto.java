package mx.unam.ciencias.edd.proyecto3;

import java.util.Random;

/**
 * Clase que representa un cuarto en el laberinto.
 */
public class Cuarto {
    public boolean norte, sur, este, oeste, visitado;
    public int vertical, horizontal;
    public int tipo;
    public int puntaje;
    public boolean inicio, fin;
    public final int d = 20;
    public int ancho, alto;
    public Random r = new Random();

    /**
     * Constructor que crea un cuarto con la posición, ancho y alto especificados.
     *
     * @param vertical   la posición vertical del cuarto.
     * @param horizontal la posición horizontal del cuarto.
     * @param ancho      el ancho del laberinto.
     * @param alto       el alto del laberinto.
     */
    public Cuarto(int vertical, int horizontal, int ancho, int alto) {
        this.vertical = vertical;
        this.horizontal = horizontal;
        this.ancho = ancho;
        this.alto = alto;
        norte = este = oeste = sur = true;
        posicionCuarto();
        puntaje = r.nextInt(16);
    }

    /**
     * Marca este cuarto como el inicio del camino.
     */
    public void InicioCamino() {
        this.este = false;
        this.inicio = true;
    }

    /**
     * Marca este cuarto como el final del camino.
     */
    public void FinalCamino() {
        this.oeste = false;
        this.fin = true;
    }

    /**
     * Determina el tipo de cuarto según su posición.
     */
    public void posicionCuarto() {
        if (vertical == 0 && horizontal == 0)
            tipo = 8;
        else if (vertical == 0 && horizontal > 0 && horizontal < ancho - 1)
            tipo = 1;
        else if (vertical == 0 && horizontal == ancho - 1)
            tipo = 2;
        else if (vertical > 0 && vertical < alto - 1 && horizontal == ancho - 1)
            tipo = 3;
        else if (vertical == alto - 1 && horizontal == ancho - 1)
            tipo = 4;
        else if (vertical == alto - 1 && horizontal > 0 && horizontal < ancho - 1)
            tipo = 5;
        else if (vertical == alto - 1 && horizontal == 0)
            tipo = 6;
        else if (vertical > 0 && vertical < alto - 1 && horizontal == 0)
            tipo = 7;
        else
            tipo = 9;
    }

    /**
     * Constructor que crea un cuarto a partir de un byte y su posición.
     *
     * @param vertical   la posición vertical del cuarto.
     * @param horizontal la posición horizontal del cuarto.
     * @param b          el byte que representa las características del cuarto.
     * @param ancho      el ancho del laberinto.
     * @param alto       el alto del laberinto.
     */
    public Cuarto(int vertical, int horizontal, byte b, int ancho, int alto) {
        this.puntaje = (b & 0xf0) >> 4;
        int casilla = b & (0x0f);

        switch (casilla) {
            case 0:
                sur = este = norte = oeste = false;
                break;
            case 1:
                oeste = true;
                sur = este = norte = false;
                break;
            case 2:
                norte = true;
                sur = este = oeste = false;
                break;
            case 3:
                norte = oeste = true;
                sur = este = false;
                break;
            case 4:
                este = true;
                sur = norte = oeste = false;
                break;
            case 5:
                este = oeste = true;
                sur = norte = false;
                break;
            case 6:
                norte = este = true;
                sur = oeste = false;
                break;
            case 7:
                este = norte = oeste = true;
                sur = false;
                break;
            case 8:
                sur = true;
                este = norte = oeste = false;
                break;
            case 9:
                sur = oeste = true;
                este = norte = false;
                break;
            case 10:
                sur = norte = true;
                este = oeste = false;
                break;
            case 11:
                sur = norte = oeste = true;
                este = false;
                break;
            case 12:
                sur = este = true;
                norte = oeste = false;
                break;
            case 13:
                sur = este = oeste = true;
                norte = false;
                break;
            case 14:
                sur = este = norte = true;
                oeste = false;
                break;
            default:
                break;
        }
        this.vertical = vertical;
        this.horizontal = horizontal;
        this.ancho = ancho;
        this.alto = alto;
        posicionCuarto();
    }

    /**
     * Genera la representación SVG de las paredes del cuarto.
     *
     * @return una cadena con la representación SVG de las paredes.
     */
    public String paredSVG() {
        StringBuilder stb = new StringBuilder();
        int x1, y1, x2, y2;

        if (norte && vertical == 0) {
            x1 = 0 + horizontal * d;
            y1 = 0 + vertical * d;
            x2 = d + horizontal * d;
            y2 = 0 + vertical * d;

            stb.append("\t\t<line x1=\"").append(x1).append("\" y1=\"").append(y1).append("\" x2=\"").append(x2)
                    .append("\" y2=\"").append(y2).append("\" stroke=\"#353535\" stroke-width=\"2\" />\n");
        }

        if (oeste) {
            x1 = d + horizontal * d;
            y1 = 0 + vertical * d;
            x2 = d + horizontal * d;
            y2 = d + vertical * d;

            stb.append("\t\t<line x1=\"").append(x1).append("\" y1=\"").append(y1).append("\" x2=\"").append(x2)
                    .append("\" y2=\"").append(y2).append("\" stroke=\"#353535\" stroke-width=\"2\" />\n");
        }

        if (sur) {
            x1 = d + horizontal * d;
            y1 = d + vertical * d;
            x2 = 0 + horizontal * d;
            y2 = d + vertical * d;

            stb.append("\t\t<line x1=\"").append(x1).append("\" y1=\"").append(y1).append("\" x2=\"").append(x2)
                    .append("\" y2=\"").append(y2).append("\" stroke=\"#353535\" stroke-width=\"2\" />\n");
        }

        if (este && horizontal == 0) {
            x1 = 0 + horizontal * d;
            y1 = d + vertical * d;
            x2 = 0 + horizontal * d;
            y2 = 0 + vertical * d;

            stb.append("\t\t<line x1=\"").append(x1).append("\" y1=\"").append(y1).append("\" x2=\"").append(x2)
                    .append("\" y2=\"").append(y2).append("\" stroke=\"#353535\" stroke-width=\"2\" />\n");
        }

        return stb.toString();
    }

    /**
     * Marca el final del camino en SVG.
     *
     * @return una cadena con la representación SVG del final del camino.
     */
    public StringBuilder marcaFinal() {
        String cx = String.valueOf(10 + horizontal * d);
        String cy = String.valueOf(10 + vertical * d);
        StringBuilder stb = new StringBuilder("\t<circle cx=\"");
        stb.append(cx);
        stb.append("\" cy=\"");
        stb.append(cy);
        stb.append("\" r=\"6\" fill=\"red\" />\n");
        return stb;
    }

    /**
     * Marca el inicio del camino en SVG.
     *
     * @return una cadena con la representación SVG del inicio del camino.
     */
    public StringBuilder marcaInicio() {
        String cx = String.valueOf(10 + horizontal * d);
        String cy = String.valueOf(10 + vertical * d);
        StringBuilder stb = new StringBuilder("\t<circle cx=\"");
        stb.append(cx);
        stb.append("\" cy=\"");
        stb.append(cy);
        stb.append("\" r=\"6\" fill=\"lime\" />\n");
        return stb;
    }

    /**
     * Genera una línea en la dirección horizontal en SVG.
     *
     * @return una cadena con la representación SVG de la línea horizontal.
     */
    public StringBuilder rectaEnX() {
        String cx = String.valueOf(10 + horizontal * d);
        String cy = String.valueOf(10 + vertical * d);
        StringBuilder stb = new StringBuilder("\t<line x1=\"");
        stb.append(cx);
        stb.append("\" y1=\"").append(cy);
        cx = String.valueOf(10 + horizontal * d + 20);
        cy = String.valueOf(10 + vertical * d);
        stb.append("\" x2=\"");
        stb.append(cx);
        stb.append("\" y2=\"");
        stb.append(cy);
        stb.append("\" stroke=\"orange\" stroke-width=\"3\" />\n");
        return stb;
    }

    /**
     * Genera una línea en la dirección vertical en SVG.
     *
     * @return una cadena con la representación SVG de la línea vertical.
     */
    public StringBuilder rectaEnY() {
        String cx = String.valueOf(10 + horizontal * d);
        String cy = String.valueOf(10 + vertical * d);
        StringBuilder stb = new StringBuilder("\t<line x1=\"");
        stb.append(cx);
        stb.append("\" y1=\"");
        stb.append(cy);
        cx = String.valueOf(10 + horizontal * d);
        cy = String.valueOf(10 + vertical * d + 20);
        stb.append("\" x2=\"");
        stb.append(cx);
        stb.append("\" y2=\"");
        stb.append(cy);
        stb.append("\" stroke=\"orange\" stroke-width=\"3\" />\n");
        return stb;
    }

    /**
     * Convierte las características del cuarto a un byte.
     *
     * @return el byte que representa las características del cuarto.
     */
    public byte byteFormatter() {
        int puerta = 0;
        if (!sur && !este && !norte && !oeste)
            puerta = 0;
        if (!sur && !este && !norte && oeste)
            puerta = 1;
        if (!sur && !este && norte && !oeste)
            puerta = 2;
        if (!sur && !este && norte && oeste)
            puerta = 3;
        if (!sur && este && !norte && !oeste)
            puerta = 4;
        if (!sur && este && !norte && oeste)
            puerta = 5;
        if (!sur && este && norte && !oeste)
            puerta = 6;
        if (!sur && este && norte && oeste)
            puerta = 7;
        if (sur && !este && !norte && !oeste)
            puerta = 8;
        if (sur && !este && !norte && oeste)
            puerta = 9;
        if (sur && !este && norte && !oeste)
            puerta = 10;
        if (sur && !este && norte && oeste)
            puerta = 11;
        if (sur && este && !norte && !oeste)
            puerta = 12;
        if (sur && este && !norte && oeste)
            puerta = 13;
        if (sur && este && norte && !oeste)
            puerta = 14;
        if (sur && este && norte && oeste)
            puerta = 15;

        puntaje = puntaje << 4;
        puerta = puntaje + puerta;

        return (byte) (puerta & 0xFF);
    }
}
