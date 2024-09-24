package mx.unam.ciencias.edd;

/**
 * Clase para colas genéricas.
 */
public class Cola<T> extends MeteSaca<T> {

    /**
     * Regresa una representación en cadena de la cola.
     * @return una representación en cadena de la cola.
     */
    @Override public String toString() {
        // Aquí va su código.
        Nodo n = cabeza;
        String str = "";

        while(n != null){
			str += n.elemento + ",";
			n = n.siguiente;
		}

		return str;
 
    }

    /**
     * Agrega un elemento al final de la cola.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    @Override public void mete(T elemento) {
        // Aquí va su código.
        if(elemento == null)
            throw new IllegalArgumentException("El elemento es nulo.");

        Nodo n = new Nodo(elemento);

        if(rabo != null){
            rabo.siguiente = n;
            rabo = n;
        }

        else
            cabeza = rabo = n;

    }
}
