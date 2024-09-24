package mx.unam.ciencias.edd;

import java.util.Comparator;

/**
 * Clase para ordenar y buscar arreglos genéricos.
 */
public class Arreglos {

    /* Constructor privado para evitar instanciación. */
    private Arreglos() {}

    /**
     * Ordena el arreglo recibido usando QickSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo el arreglo a ordenar.
     * @param comparador el comparador para ordenar el arreglo.
     */
    public static <T> void
    quickSort(T[] arreglo, Comparator<T> comparador) {
        // Aquí va su código.
        quickAux(arreglo, comparador, 0, arreglo.length-1);
    }

    private static <T> void
    quickAux(T[] arreglo, Comparator<T> comparador, int ini, int fin) {

		if (fin <= ini) 
            return;
		
        int i = ini + 1; // Apunta al elemento inmediatamente a la derecha del pivote (ini). i es izquierda.
        int j = fin; // Apunta al último elemento del arreglo. j es derecha.

        while (i < j)
            if (comparador.compare(arreglo[i], arreglo[ini]) > 0 && comparador.compare(arreglo[j], arreglo[ini]) <= 0)
                intercambia(arreglo, i++, j--); //intercambiamos los elementos de i y de j y los recorremos en uno.
			else if (comparador.compare(arreglo[i], arreglo[ini]) <= 0)
                i++;
            else 
                j--;
		
        if (comparador.compare(arreglo[i], arreglo[ini]) > 0)
            i--;

        intercambia(arreglo, ini, i);

		/* Hacemos las llamadas recursivas. */
        quickAux(arreglo, comparador, ini, i-1);
        quickAux(arreglo, comparador, i+1, fin);
    }


	/**
     * Ordena el arreglo recibido usando QickSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo un arreglo cuyos elementos son comparables.
     */
    public static <T extends Comparable<T>> void
    quickSort(T[] arreglo) {
        quickSort(arreglo, (a, b) -> a.compareTo(b));
    }

    /**
     * Método auxiliar.
     * @param <T> tipo del que puede ser el arreglo.
     * @param entero de la primera poscición a intercambiar.
     * @param entero de la segunda poscición a intercambiar.
     */

    private static <T> void intercambia(T[] arreglo, int i, int j){
      T tmp = arreglo[i];
      arreglo[i] = arreglo[j];
      arreglo[j] = tmp;
    }


    /**
     * Ordena el arreglo recibido usando SelectionSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo el arreglo a ordenar.
     * @param comparador el comparador para ordernar el arreglo.
     */
    public static <T> void
    selectionSort(T[] arreglo, Comparator<T> comparador) {
        // Aquí va su código.
        for(int i = 0; i<arreglo.length; i++){
            int min = i;

            for(int j = i+1; j<arreglo.length; j++){
                if(comparador.compare(arreglo[j], arreglo[min]) < 0)
                    min = j;
            }

            intercambia(arreglo, i, min);
        }
    }

    /**
     * Ordena el arreglo recibido usando SelectionSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo un arreglo cuyos elementos son comparables.
     */
    public static <T extends Comparable<T>> void
    selectionSort(T[] arreglo) {
        selectionSort(arreglo, (a, b) -> a.compareTo(b));
    }

    /**
     * Hace una búsqueda binaria del elemento en el arreglo. Regresa el índice
     * del elemento en el arreglo, o -1 si no se encuentra.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo el arreglo dónde buscar.
     * @param elemento el elemento a buscar.
     * @param comparador el comparador para hacer la búsqueda.
     * @return el índice del elemento en el arreglo, o -1 si no se encuentra.
     */
    public static <T> int
    busquedaBinaria(T[] arreglo, T elemento, Comparator<T> comparador) {
        // Aquí va su código.
        return auxBinaria(arreglo, elemento, comparador, 0, arreglo.length -1);
    }

	public static <T> int
	auxBinaria(T[] arreglo, T elemento, Comparator<T> comparador, int ini, int fin) {

		/* Si el indice final fin es menor que el indice de inicio ini, el arreglo
		 * es vacío y regresamos -1. */

		if (fin < ini) 
            return -1;
		
        int mid = (ini + fin)/2; // Índice medio.
		
		/* Si el elemento que buscamos es igual al elemento del índice medio,
		 * regresamos el mismo índice medio. */
		if(comparador.compare(arreglo[mid], elemento) == 0)
			return mid;
		else if(comparador.compare(arreglo[mid], elemento) > 0)
			return auxBinaria(arreglo, elemento, comparador, ini, mid-1);
		else
			return auxBinaria(arreglo, elemento, comparador, mid+1, fin);
		
	}	
	
    /**
     * Hace una búsqueda binaria del elemento en el arreglo. Regresa el índice
     * del elemento en el arreglo, o -1 si no se encuentra.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo un arreglo cuyos elementos son comparables.
     * @param elemento el elemento a buscar.
     * @return el índice del elemento en el arreglo, o -1 si no se encuentra.
     */
    public static <T extends Comparable<T>> int
    busquedaBinaria(T[] arreglo, T elemento) {
        return busquedaBinaria(arreglo, elemento, (a, b) -> a.compareTo(b));
    }
}
