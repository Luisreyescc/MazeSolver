package mx.unam.ciencias.edd;

/**
 * Clase para pilas genéricas.
 */
public class Pila<T> extends MeteSaca<T> {

    /**
     * Regresa una representación en cadena de la pila.
     * @return una representación en cadena de la pila.
     */
    @Override public String toString() {
        // Aquí va su código.
        Nodo n = cabeza;
        String str = "";

        while(n != null){
			str += n.elemento + "\n";
			n = n.siguiente;
        }

		return str;
        
    }

    /**
     * Agrega un elemento al tope de la pila.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    @Override public void mete(T elemento) {
        // Aquí va su código.
        if(elemento == null)
            throw new IllegalArgumentException("El elemento es nulo.");

        Nodo n = new Nodo(elemento);

        if(cabeza != null){
            n.siguiente = cabeza;
            cabeza = n;
        }

        else
            cabeza = rabo = n;

    }
}
