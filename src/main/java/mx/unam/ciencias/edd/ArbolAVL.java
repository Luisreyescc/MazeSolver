package mx.unam.ciencias.edd;

/**
 * <p>Clase para árboles AVL.</p>
 *
 * <p>Un árbol AVL cumple que para cada uno de sus vértices, la diferencia entre
 * la áltura de sus subárboles izquierdo y derecho está entre -1 y 1.</p>
 */
public class ArbolAVL<T extends Comparable<T>>
    extends ArbolBinarioOrdenado<T> {

    /**
     * Clase interna protegida para vértices.
     */
    protected class VerticeAVL extends Vertice {

        /** La altura del vértice. */
        public int altura;

        /**
         * Constructor único que recibe un elemento.
         * @param elemento el elemento del vértice.
         */
        public VerticeAVL(T elemento) {
            // Aquí va su código.
	    super(elemento);
        }

        /**
         * Regresa la altura del vértice.
         * @return la altura del vértice.
         */
        @Override public int altura() {
            // Aquí va su código.
	    return altura;
        }

        /**
         * Regresa una representación en cadena del vértice AVL.
         * @return una representación en cadena del vértice AVL.
         */
        @Override public String toString() {
            // Aquí va su código.
	    return elemento + " " + altura + "/" + balance(this);
        }

        /**
         * Compara el vértice con otro objeto. La comparación es
         * <em>recursiva</em>.
         * @param objeto el objeto con el cual se comparará el vértice.
         * @return <code>true</code> si el objeto es instancia de la clase
         *         {@link VerticeAVL}, su elemento es igual al elemento de éste
         *         vértice, los descendientes de ambos son recursivamente
         *         iguales, y las alturas son iguales; <code>false</code> en
         *         otro caso.
         */
        @Override public boolean equals(Object objeto) {
            if (objeto == null || getClass() != objeto.getClass())
                return false;
            @SuppressWarnings("unchecked") VerticeAVL vertice = (VerticeAVL)objeto;
	    return (altura == vertice.altura && super.equals(objeto));
        }
    }

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinarioOrdenado}.
     */
    public ArbolAVL() { super(); }

    /**
     * Construye un árbol AVL a partir de una colección. El árbol AVL tiene los
     * mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol AVL.
     */
    public ArbolAVL(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
     * Construye un nuevo vértice, usando una instancia de {@link VerticeAVL}.
     * @param elemento el elemento dentro del vértice.
     * @return un nuevo vértice con el elemento recibido dentro del mismo.
     */
    @Override protected Vertice nuevoVertice(T elemento) {
        // Aquí va su código.
	return new VerticeAVL(elemento);
    }

    private void rebalanceo(VerticeAVL v){
	if(v == null)
	    return;

	v.altura = Math.max(altura(vAVL(v.izquierdo)), altura(vAVL(v.derecho))) + 1;
	
	if(balance(v) == -2){
	    VerticeAVL hd = vAVL(v.derecho);

	    if(balance(hd) == 1){
		super.giraDerecha(hd);
		hd.altura = Math.max(altura(vAVL(hd.izquierdo)), altura(vAVL(hd.derecho))) + 1;
	    }

	    super.giraIzquierda(v);
	    hd.altura = Math.max(altura(vAVL(hd.izquierdo)), altura(vAVL(hd.derecho))) + 1;
	    v.altura = Math.max(altura(vAVL(v.izquierdo)), altura(vAVL(v.derecho))) + 1;
	}

	if(balance(v) == 2){
	    VerticeAVL hi = vAVL(v.izquierdo);

	    if(balance(hi) == -1){
		super.giraIzquierda(hi);
		hi.altura = Math.max(altura(vAVL(hi.izquierdo)), altura(vAVL(hi.derecho))) + 1;
	    }

	    super.giraDerecha(v);
	    hi.altura = Math.max(altura(vAVL(hi.izquierdo)), altura(vAVL(hi.derecho))) + 1;
	    v.altura = Math.max(altura(vAVL(v.izquierdo)), altura(vAVL(v.derecho))) + 1;	    
	}

	rebalanceo(vAVL(v.padre));
	
    }

    private int balance(VerticeAVL v){
	return altura(vAVL(v.izquierdo)) - altura(vAVL(v.derecho));
    }
    
    private int altura(VerticeAVL v){
	return v == null? -1 : v.altura;
    }

    private VerticeAVL vAVL(VerticeArbolBinario<T> v){
	return (VerticeAVL) v;
    }
    /**
     * Agrega un nuevo elemento al árbol. El método invoca al método {@link
     * ArbolBinarioOrdenado#agrega}, y después balancea el árbol girándolo como
     * sea necesario.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) {
        // Aquí va su código.
	super.agrega(elemento);
	rebalanceo(vAVL(ultimoAgregado.padre));
    }

    /**
     * Elimina un elemento del árbol. El método elimina el vértice que contiene
     * el elemento, y gira el árbol como sea necesario para rebalancearlo.
     * @param elemento el elemento a eliminar del árbol.
     */
    @Override public void elimina(T elemento) {
        // Aquí va su código.
	VerticeAVL v = vAVL(busca(elemento));

	if(v == null)
	    return;

	elementos --;
	
	if(v.derecho != null && v.izquierdo != null)
	    v = vAVL(intercambiaEliminable(v));

	eliminaVertice(v);
	rebalanceo(vAVL(v.padre));
	   
	
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles AVL
     * no pueden ser girados a la derecha por los usuarios de la clase, porque
     * se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraDerecha(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles AVL no  pueden " +
                                                "girar a la izquierda por el " +
                                                "usuario.");
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles AVL
     * no pueden ser girados a la izquierda por los usuarios de la clase, porque
     * se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraIzquierda(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles AVL no  pueden " +
                                                "girar a la derecha por el " +
                                                "usuario.");
    }
}