package mx.unam.ciencias.edd;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * <p>Clase genérica para listas doblemente ligadas.</p>
 *
 * <p>Las listas nos permiten agregar elementos al inicio o final de la lista,
 * eliminar elementos de la lista, comprobar si un elemento está o no en la
 * lista, y otras operaciones básicas.</p>
 *
 * <p>Las listas no aceptan a <code>null</code> como elemento.</p>
 *
 * @param <T> El tipo de los elementos de la lista.
 */
public class Lista<T> implements Coleccion<T> {

    /* Clase interna privada para nodos. */
    private class Nodo {
        /* El elemento del nodo. */
        private T elemento;
        /* El nodo anterior. */
        private Nodo anterior;
        /* El nodo siguiente. */
        private Nodo siguiente;

        /* Construye un nodo con un elemento. */
        private Nodo(T elemento) {
            // Aquí va su código.
            this.elemento = elemento;
            anterior = null;
            siguiente = null;

        }
    }

    /* Clase interna privada para iteradores. */
    private class Iterador implements IteradorLista<T> {
        /* El nodo anterior. */
        private Nodo anterior;
        /* El nodo siguiente. */
        private Nodo siguiente;

        /* Construye un nuevo iterador. */
        private Iterador() {
            // Aquí va su código.
            start();
        }

        /* Nos dice si hay un elemento siguiente. */
        @Override public boolean hasNext() {
            // Aquí va su código.
            return siguiente != null;

        }

        /* Nos da el elemento siguiente. */
        @Override public T next() {
            // Aquí va su código.
            if(hasNext() == false){
                throw new NoSuchElementException("No hay siguiente");
            } else {
                anterior = siguiente;
                siguiente = siguiente.siguiente;
                return anterior.elemento;
            }

        }

        /* Nos dice si hay un elemento anterior. */
        @Override public boolean hasPrevious() {
            // Aquí va su código.
            return anterior != null;
        }

        /* Nos da el elemento anterior. */
        @Override public T previous() {
            // Aquí va su código.
            if(hasPrevious() == false){
                throw new NoSuchElementException("No hay anterior");
            } else {
                siguiente = anterior;
                anterior = anterior.anterior;
                return siguiente.elemento;
            }

        }

        /* Mueve el iterador al inicio de la lista. */
        @Override public void start() {
            // Aquí va su código.
            siguiente = cabeza;
            anterior = null;
        }

        /* Mueve el iterador al final de la lista. */
        @Override public void end() {
            // Aquí va su código.
            anterior = rabo;
            siguiente = null;
        }
    }

    /* Primer elemento de la lista. */
    private Nodo cabeza;
    /* Último elemento de la lista. */
    private Nodo rabo;
    /* Número de elementos en la lista. */
    private int longitud;

    /**
     * Regresa la longitud de la lista. El método es idéntico a {@link
     * #getElementos}.
     * @return la longitud de la lista, el número de elementos que contiene.
     */
    public int getLongitud() {
        // Aquí va su código.
        return longitud;
    }

    /**
     * Regresa el número elementos en la lista. El método es idéntico a {@link
     * #getLongitud}.
     * @return el número elementos en la lista.
     */
    @Override public int getElementos() {
        // Aquí va su código.
        return getLongitud();
    }

    /**
     * Nos dice si la lista es vacía.
     * @return <code>true</code> si la lista es vacía, <code>false</code> en
     *         otro caso.
     */
    @Override public boolean esVacia() {
        // Aquí va su código.
        return cabeza == null;
    }

    /**
     * Agrega un elemento a la lista. Si la lista no tiene elementos, el
     * elemento a agregar será el primero y último. El método es idéntico a
     * {@link #agregaFinal}.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    @Override public void agrega(T elemento) {
        // Aquí va su código.
        if(elemento != null){

            Nodo e = new Nodo(elemento);
            longitud++;

            if(esVacia() != true){
                rabo.siguiente = e;
                e.anterior = rabo;
                rabo = e;
            } else {
                cabeza = rabo = e;
            }

        } else {
            throw new IllegalArgumentException("Elemento nulo.");
        }

    }

    /**
     * Agrega un elemento al final de la lista. Si la lista no tiene elementos,
     * el elemento a agregar será el primero y último.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void agregaFinal(T elemento) {
        // Aquí va su código.
        agrega(elemento);
    }

    /**
     * Agrega un elemento al inicio de la lista. Si la lista no tiene elementos,
     * el elemento a agregar será el primero y último.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void agregaInicio(T elemento) {
        // Aquí va su código.
        if(elemento != null){

            Nodo e = new Nodo(elemento);
            longitud++;

            if(esVacia() != true){
                cabeza.anterior = e;
                e.siguiente = cabeza;
                cabeza = e;
            } else {
                cabeza = rabo = e;
            }

        } else {
            throw new IllegalArgumentException("Elemento nulo.");
        }

    }

    /**
     * Método auxiliar.
     * Regresa el i-esimo elemento de una lista
     * @param i el indice del nodo que queremos buscar.
     * @return el Nodo de indice i.*/

    private Nodo buscaNodo(int i){

            int aux = 0;
            Nodo n = cabeza;

            while(aux != i){
                n = n.siguiente;
                aux ++;
            }

            return n;

    }

    /**
     * Inserta un elemento en un índice explícito.
     *
     * Si el índice es menor o igual que cero, el elemento se agrega al inicio
     * de la lista. Si el índice es mayor o igual que el número de elementos en
     * la lista, el elemento se agrega al fina de la misma. En otro caso,
     * después de mandar llamar el método, el elemento tendrá el índice que se
     * especifica en la lista.
     * @param i el índice dónde insertar el elemento. Si es menor que 0 el
     *          elemento se agrega al inicio de la lista, y si es mayor o igual
     *          que el número de elementos en la lista se agrega al final.
     * @param elemento el elemento a insertar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void inserta(int i, T elemento) {
        // Aquí va su código.
        if(elemento == null)
            throw new IllegalArgumentException("El elemento es nulo.");

            if(i <= 0){
                agregaInicio(elemento);
            } else if (i >= longitud){
                agregaFinal(elemento);
            } else {
                longitud++;

                Nodo n = new Nodo(elemento);
                Nodo s = buscaNodo(i);

                n.anterior = s.anterior;
                s.anterior.siguiente = n;
                n.siguiente = s;
                s.anterior = n;

            }

    }

    /**
     * Elimina un elemento de la lista. Si el elemento no está contenido en la
     * lista, el método no la modifica.
     * @param elemento el elemento a eliminar.
     */
    @Override public void elimina(T elemento) {
        // Aquí va su código.
        Nodo n = cabeza;

        while (n != null) {
            if (n.elemento.equals(elemento))
                break;

            n = n.siguiente;
        }

        if (n == null) // Si el n es nulo, quiere decir que no se encontró y no se modifica la lista :)
            return;

        if (n.anterior == null) // Si n es la cabeza (su anterior es null), la nueva cabeza será el n.siguiente, ya que la cabeza original será elminada.
            cabeza = n.siguiente;
        else
            n.anterior.siguiente = n.siguiente; // Conectamos al siguiente del anterior de n al siguiente de n por su izquierda.

        if (n.siguiente == null) // Si el n es el rabo (su siguiente es null), el nuevo rabo será el n.anterior, ya que el rabo original será eliminado.
            rabo = n.anterior;
        else
            n.siguiente.anterior = n.anterior; // Conectamos al anterior del siguiente de n al anterior de n por la su derecha.

        longitud--; // Disminuimos la longitud en uno.

    }

    /**
     * Elimina el primer elemento de la lista y lo regresa.
     * @return el primer elemento de la lista antes de eliminarlo.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T eliminaPrimero() {
        // Aquí va su código.
        if(esVacia())
            throw new NoSuchElementException("Lista vacia");

            T n = cabeza.elemento;
            cabeza = cabeza.siguiente;

            if(cabeza != null)
                cabeza.anterior = null;
            else
                rabo = null;

            longitud --;
            return n;

    }

    /**
     * Elimina el último elemento de la lista y lo regresa.
     * @return el último elemento de la lista antes de eliminarlo.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T eliminaUltimo() {
        // Aquí va su código.
        if(esVacia())
            throw new NoSuchElementException("Lista vacia");

            T n = rabo.elemento;
            rabo = rabo.anterior;

            if(rabo != null)
                rabo.siguiente = null;
            else
                cabeza = null;

            longitud --;
            return n;

    }

    /**
     * Nos dice si un elemento está en la lista.
     * @param elemento el elemento que queremos saber si está en la lista.
     * @return <code>true</code> si <code>elemento</code> está en la lista,
     *         <code>false</code> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
        // Aquí va su código.
        for(T t : this){
            if(t.equals(elemento))
                return true;
            }
        return false;
    }

    /**
     * Regresa la reversa de la lista.
     * @return una nueva lista que es la reversa la que manda llamar el método.
     */
    public Lista<T> reversa() {
        // Aquí va su código.
       Lista<T> l = new Lista <>();
       for(T t : this){
           l.agregaInicio(t);
       }
       return l;

    }

    /**
     * Regresa una copia de la lista. La copia tiene los mismos elementos que la
     * lista que manda llamar el método, en el mismo orden.
     * @return una copiad de la lista.
     */
    public Lista<T> copia() {
        // Aquí va su código.
       Lista<T> l = new Lista <>();
       for(T t : this){
           l.agrega(t);
       }
       return l;

    }

    /**
     * Limpia la lista de elementos, dejándola vacía.
     */
    @Override public void limpia() {
        // Aquí va su código.
        longitud = 0;
        cabeza = rabo = null;

    }

    /**
     * Regresa el primer elemento de la lista.
     * @return el primer elemento de la lista.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T getPrimero() {
        // Aquí va su código.
        if(esVacia() != false){
            throw new NoSuchElementException("La lista es vacia");
        } else {
            return cabeza.elemento;

        }

    }

    /**
     * Regresa el último elemento de la lista.
     * @return el primer elemento de la lista.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T getUltimo() {
        // Aquí va su código.
        if(esVacia()){
            throw new NoSuchElementException("La lista es vacia");
        }
        return rabo.elemento;

    }

    /**
     * Regresa el <em>i</em>-ésimo elemento de la lista.
     * @param i el índice del elemento que queremos.
     * @return el <em>i</em>-ésimo elemento de la lista.
     * @throws ExcepcionIndiceInvalido si <em>i</em> es menor que cero o mayor o
     *         igual que el número de elementos en la lista.
     */
    public T get(int i) {
        // Aquí va su código.
        if(i < 0 || i >= longitud){
            throw new ExcepcionIndiceInvalido("Indice invalido.");
        } else {
            int aux = 0;
            Nodo n = cabeza;
            while(n != null){
                if(i == aux)
                    return n.elemento;
                aux++;
                n = n.siguiente;
            }
        } return null;

    }

    /**
     * Regresa el índice del elemento recibido en la lista.
     * @param elemento el elemento del que se busca el índice.
     * @return el índice del elemento recibido en la lista, o -1 si el elemento
     *         no está contenido en la lista.
     */
    public int indiceDe(T elemento) {
        // Aquí va su código.
        int contador = 0;
        Nodo n = cabeza;
        while(n != null){
            if(n.elemento.equals(elemento))
                return contador;

            n = n.siguiente;
            contador++;

        }
        return -1;
    }

    /**
     * Regresa una representación en cadena de la lista.
     * @return una representación en cadena de la lista.
     */
    @Override public String toString() {
        // Aquí va su código.
        Nodo n = cabeza;
            if(n != null){
                String r = "[" + n.elemento;

                while(n.siguiente != null){
                    n = n.siguiente;
                    if(n != null)
                        r += ", " + n.elemento;
                }
                return r = r + "]";
            }

            return "[]";
    }

    /**
     * Nos dice si la lista es igual al objeto recibido.
     * @param objeto el objeto con el que hay que comparar.
     * @return <code>true</code> si la lista es igual al objeto recibido;
     *         <code>false</code> en otro caso.
     */
    @Override public boolean equals(Object objeto) {
        if (objeto == null || getClass() != objeto.getClass())
            return false;
        @SuppressWarnings("unchecked") Lista<T> lista = (Lista<T>)objeto;
        // Aquí va su código.
        if(longitud != lista.longitud)
            return false;

		Iterator<T> i = iterator();
		
		for(T t : lista)
			if(!t.equals(i.next()))
				return false;

		return true;
    }

    /**
     * Regresa un iterador para recorrer la lista en una dirección.
     * @return un iterador para recorrer la lista en una dirección.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }

    /**
     * Regresa un iterador para recorrer la lista en ambas direcciones.
     * @return un iterador para recorrer la lista en ambas direcciones.
     */
    public IteradorLista<T> iteradorLista() {
        return new Iterador();
    }

    /**
     * Regresa una copia de la lista, pero ordenada. Para poder hacer el
     * ordenamiento, el método necesita una instancia de {@link Comparator} para
     * poder comparar los elementos de la lista.
     * @param comparador el comparador que la lista usará para hacer el
     *                   ordenamiento.
     * @return una copia de la lista, pero ordenada.
     */
    public Lista<T> mergeSort(Comparator<T> comparador) {
        // Aquí va su código.
		if(longitud <= 1) // Si la lista tiene 1 o menos elementos ya está ordenada por definición.
			return copia();

		int mid = longitud/2; // Para hallar la mitad de la lista para luego hacer las sub listas izquierda y derecha. 
		Lista<T> midUno = new Lista<>(); // Sublista de la mitad izquierda :)
		Lista<T> midDos = new Lista<>(); // Sublista de la mitad derecha :)

		Nodo n = cabeza; 
		int aux = 0;
		
		/* Llenamos ambas sub listas nodos correspondientes (aún no están ordenadas).
		 * Comparamos el entero aux (que cuenta en que poscición estamos) con mid (la mitad
		 * en la lista original) si es menor, se agrega a la lista de la izquieda, si es
		 * mayor a la lista de la derecha. Sumamos uno a nuestro contador y movemos n al
		 * siguiente. Hacemos esto hasta que n sea null. */

		while(n != null){
			
			if(aux < mid)
				midUno.agrega(n.elemento);
			else
				midDos.agrega(n.elemento);

			aux++;
			n = n.siguiente;
		}

		/* Volvemos a dividir esas listas usando recursión, es decir, estas listas que ya
		 * tienen los elementos correspondientes a su mitad se vuelven a dividir.*/
		
		midUno = midUno.mergeSort(comparador);
		midDos = midDos.mergeSort(comparador);

		/* Por último hacemos la mezcla de ambas mitades para obtener la lista ordenada.*/
		return merge(midUno, midDos, comparador);
		
	}

    /**
     * Método auxiliar, recibe dos listas ya ordenadas
     * @param dos listas ya ordenadas y un comparador.
     * @return una copia de la lista, pero ordenada.
     */	
		
    private Lista<T> merge(Lista<T> listauno, Lista<T> listados, Comparator<T> comparador){
	
	Lista<T> ordenada = new Lista <>(); // Lista que contendrá los elementos ya ordenados.
	
	/* Creamos dos nodos que servirán para recorrer las listas. */
	
	Nodo i = listauno.cabeza;
	Nodo j = listados.cabeza;

	/* Mientras ninguno de los nodos llegue al final de su lista, comparamos los elementos en
	 * cada uno. */
	
	while(i != null && j != null){
	    
	    /* Si el elemento de i es menor o igual que el elemento de j, agregamos el elemento
	     * de i a la nueva lista y movemos i a su siguiente; si no, agregamos el elemento de j a 
	     * la nueva lista y movemos j a su siguiente. */
	    
	    if(comparador.compare(i.elemento,j.elemento) <= 0){
		ordenada.agrega(i.elemento);
		i = i.siguiente;
	    } else {
		ordenada.agrega(j.elemento);
		j = j.siguiente;
	    }	
	}
	
	/* Si algún nodo llega al final de su lista, agregamos todos los elementos restantes
	 * de la lista del otro iterador a la nueva lista. */
	
	if(i == null){
	    while(j != null){
		ordenada.agrega(j.elemento);
		j = j.siguiente;
	    }
		
	} else if(j == null){
	    while(i != null){
		ordenada.agrega(i.elemento);
		i = i.siguiente;			
			}
	}
	/* Regresamios la lista ordeanada. */
	return ordenada;
    }
	
    /**
     * Regresa una copia de la lista recibida, pero ordenada. La lista recibida
     * tiene que contener nada más elementos que implementan la interfaz {@link
     * Comparable}.
     * @param <T> tipo del que puede ser la lista.
     * @param lista la lista que se ordenará.
     * @return una copia de la lista recibida, pero ordenada.
     */
    public static <T extends Comparable<T>>
    Lista<T> mergeSort(Lista<T> lista) {
        return lista.mergeSort((a, b) -> a.compareTo(b));
    }

    /**
     * Busca un elemento en la lista ordenada, usando el comparador recibido. El
     * método supone que la lista está ordenada usando el mismo comparador.
     * @param elemento el elemento a buscar.
     * @param comparador el comparador con el que la lista está ordenada.
     * @return <code>true</code> si el elemento está contenido en la lista,
     *         <code>false</code> en otro caso.
     */
    public boolean busquedaLineal(T elemento, Comparator<T> comparador) {
        // Aquí va su código.
		for(T t : this)
			if(comparador.compare(t,elemento) == 0)
			   return true;
		    else if(comparador.compare(t, elemento) > 0)
				return false;
	   
		return false;
    }

    /**
     * Busca un elemento en una lista ordenada. La lista recibida tiene que
     * contener nada más elementos que implementan la interfaz {@link
     * Comparable}, y se da por hecho que está ordenada.
     * @param <T> tipo del que puede ser la lista.
     * @param lista la lista donde se buscará.
     * @param elemento el elemento a buscar.
     * @return <code>true</code> si el elemento está contenido en la lista,
     *         <code>false</code> en otro caso.
     */
    public static <T extends Comparable<T>>
    boolean busquedaLineal(Lista<T> lista, T elemento) {
        return lista.busquedaLineal(elemento, (a, b) -> a.compareTo(b));
    }
}
