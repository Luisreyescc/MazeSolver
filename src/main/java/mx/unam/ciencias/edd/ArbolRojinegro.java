package mx.unam.ciencias.edd;

/**
 * Clase para árboles rojinegros. Un árbol rojinegro cumple las siguientes
 * propiedades:
 * 
 * <ol>
 *  <li>Todos los vértices son NEGROS o ROJOS.</li>
 *  <li>La raíz es NEGRA.</li>
 *  <li>Todas las hojas (<code>null</code>) son NEGRAS (al igual que la raíz).</li>
 *  <li>Un vértice ROJO siempre tiene dos hijos NEGROS.</li>
 *  <li>Todo camino de un vértice a alguna de sus hojas descendientes tiene el
 *      mismo número de vértices NEGROS.</li>
 * </ol>
 *
 * Los árboles rojinegros se autobalancean.
 */
public class ArbolRojinegro<T extends Comparable<T>>
    extends ArbolBinarioOrdenado<T> {

    /**
     * Clase interna protegida para vértices.
     */
    protected class VerticeRojinegro extends Vertice {

        /** El color del vértice. */
        public Color color;

        /**
         * Constructor único que recibe un elemento.
         * @param elemento el elemento del vértice.
         */
        public VerticeRojinegro(T elemento) {
            // Aquí va su código.
            super(elemento);
            color = Color.NINGUNO;
        }
        
        /**
         * Regresa una representación en cadena del vértice rojinegro.
         * @return una representación en cadena del vértice rojinegro.
         */
        @Override public String toString() {
            // Aquí va su código.
            return (color == Color.ROJO ? "R" : "N" ) + "{" + elemento.toString() + "}";  
        }

        /**
         * Compara el vértice con otro objeto. La comparación es
         * <em>recursiva</em>.
         * @param objeto el objeto con el cual se comparará el vértice.
         * @return <code>true</code> si el objeto es instancia de la clase
         *         {@link VerticeRojinegro}, su elemento es igual al elemento de
         *         éste vértice, los descendientes de ambos son recursivamente
         *         iguales, y los colores son iguales; <code>false</code> en
         *         otro caso.
         */
        @Override public boolean equals(Object objeto) {
            if (objeto == null || getClass() != objeto.getClass())
                return false;
            @SuppressWarnings("unchecked")
                VerticeRojinegro vertice = (VerticeRojinegro)objeto;
            // Aquí va su código.
            return color == vertice.color && super.equals(objeto);
        }
    }

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinarioOrdenado}.
     */
    public ArbolRojinegro() { super(); }

    /**
     * Construye un árbol rojinegro a partir de una colección. El árbol
     * rojinegro tiene los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        rojinegro.
     */
    public ArbolRojinegro(Coleccion<T> coleccion) {
        // Aquí va su código.
        super(coleccion);
    }

    /**
     * Construye un nuevo vértice, usando una instancia de {@link
     * VerticeRojinegro}.
     * @param elemento el elemento dentro del vértice.
     * @return un nuevo vértice rojinegro con el elemento recibido dentro del mismo.
     */
    @Override protected Vertice nuevoVertice(T elemento) {
        // Aquí va su código.
        return new VerticeRojinegro(elemento);
    }

    private VerticeRojinegro vRojinegro(VerticeArbolBinario<T> vertice){
	return (VerticeRojinegro)vertice;
	
    } 

    /**
     * Regresa el color del vértice rojinegro.
     * @param vertice el vértice del que queremos el color.
     * @return el color del vértice rojinegro.
     * @throws ClassCastException si el vértice no es instancia de {@link
     *         VerticeRojinegro}.
     */
    public Color getColor(VerticeArbolBinario<T> vertice) {
        // Aquí va su código.
        return vRojinegro(vertice).color;
    }

    /**
     * Agrega un nuevo elemento al árbol. El método invoca al método {@link
     * ArbolBinarioOrdenado#agrega}, y después balancea el árbol recoloreando
     * vértices y girando el árbol como sea necesario.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) {
        // Aquí va su código.
	super.agrega(elemento);
	VerticeRojinegro v = vRojinegro(ultimoAgregado);
	v.color = Color.ROJO;
	agregaRebalanceado(v);
    }

    
    /* Algoritmo auxiliar para rebalancear */
    private void agregaRebalanceado(VerticeRojinegro v){

        // Caso 1
	if(v.padre == null){
	    v.color = Color.NEGRO;
	    return;
	}
	
	VerticeRojinegro p = vRojinegro(v.padre); 
    
	// Caso 2
	if(color(p) == Color.NEGRO){
	    return;
	}

	VerticeRojinegro a = vRojinegro(v.padre.padre);
	VerticeRojinegro t = vRojinegro(esDerecho(p) ? a.izquierdo : a.derecho);
	
	// Caso 3
	if(color(t) == Color.ROJO){
	    t.color = Color.NEGRO;
	    p.color = Color.NEGRO;
	    a.color = Color.ROJO;

	    agregaRebalanceado(a);
	    return;
	}
	
	// Caso 4
	if(esDerecho(v) && !esDerecho(p)){
	    super.giraIzquierda(p);
	    VerticeRojinegro aux = p;
	    p = v;
	    v = aux;
	} else if (esDerecho(p) && !esDerecho(v)) {
	    super.giraDerecha(p);
	    VerticeRojinegro aux = p;
	    p = v;
	    v = aux;	    
	}

	// Caso 5
	p.color = Color.NEGRO;
	a.color = Color.ROJO;

	if(!esDerecho(v))
	    super.giraDerecha(a);
	else
	    super.giraIzquierda(a);
	
    }
    
    /* Auxiliar que regresa el color de un vertice dado.0 */
    private Color color(VerticeRojinegro v){
	return v != null ? v.color : Color.NEGRO;
    }
    
    /* Auxiliar que nos dice si un vertice es izquierdo */
    private boolean esDerecho(Vertice v){
	return v.padre.derecho == v;
    }

    
    /**
     * Elimina un elemento del árbol. El método elimina el vértice que contiene
     * el elemento, y recolorea y gira el árbol como sea necesario para
     * rebalancearlo.
     * @param elemento el elemento a eliminar del árbol.
     */
    @Override public void elimina(T elemento) {
        // Aquí va su código.
	VerticeRojinegro v = vRojinegro(busca(elemento));

	if(v == null)
	    return;

	elementos --;

	if (v.izquierdo != null && v.derecho != null)
            v = vRojinegro(intercambiaEliminable(v));
	
        VerticeRojinegro fantasma = null;
        VerticeRojinegro h;
	
        if (v.izquierdo == null && v.derecho == null) {
            fantasma = vRojinegro(nuevoVertice(null));
            fantasma.color = Color.NEGRO;
            fantasma.padre = v;
            v.izquierdo = fantasma;
            h = fantasma;
        } else
            h = vRojinegro(v.izquierdo != null ? v.izquierdo : v.derecho);

        eliminaVertice(v);

        if (color(h) == Color.ROJO || color(v) == Color.ROJO)
            h.color = Color.NEGRO;
        else
            eliminaRebalanceado(h);

        if (fantasma != null)
            eliminaVertice(fantasma);      
	
    }

    /* Algoritmo auxiliar para reabalancear */
    
    private void eliminaRebalanceado(VerticeRojinegro v){
	// Caso 1
	if(v.padre == null)
	    return;

	VerticeRojinegro p = vRojinegro(v.padre); 
	VerticeRojinegro h = vRojinegro(esDerecho(v) ? p.izquierdo : p.derecho); 
	
	// Caso 2
	if(color(h) == Color.ROJO){
	    p.color = Color.ROJO;
	    h.color = Color.NEGRO;

	    if(esDerecho(v))
		super.giraDerecha(p);
	    else
		super.giraIzquierda(p);

	p = vRojinegro(v.padre); 
	h = vRojinegro(esDerecho(v) ? p.izquierdo : p.derecho); 
       
	}

	// Caso 3
	VerticeRojinegro hi = vRojinegro(h.izquierdo); 
	VerticeRojinegro hd = vRojinegro(h.derecho);

	if(color(p) == Color.NEGRO && color(h) == Color.NEGRO &&
	   color(hi) == Color.NEGRO && color(hd) == Color.NEGRO){
	    h.color = Color.ROJO;
	    eliminaRebalanceado(p);
	    return;
	}

	// Caso 4

	if(color(p) == Color.ROJO && color(h) == Color.NEGRO &&
	   color(hi) == Color.NEGRO && color(hd) == Color.NEGRO){
	    h.color = Color.ROJO;
	    p.color = Color.NEGRO;
	    return;
	}

	// Caso 5

	if((!esDerecho(v) && color(hi) == Color.ROJO && color(hd) == Color.NEGRO)
	   || (esDerecho(v) && color(hi) == Color.NEGRO && color(hd) == Color.ROJO)){
	    h.color = Color.ROJO;

	    if(color(hd) == Color.ROJO)
		hd.color = Color.NEGRO;
	    if(color(hi) == Color.ROJO)
		hi.color = Color.NEGRO;

	    if(esDerecho(v))
		super.giraIzquierda(h);
	    else
		super.giraDerecha(h);

	    h = vRojinegro(esDerecho(v) ? p.izquierdo : p.derecho);
	    hi = vRojinegro(h.izquierdo); 
	    hd = vRojinegro(h.derecho);	    

	}

	// Caso 6

	h.color = p.color;
	p.color = Color.NEGRO;
	    
	if(esDerecho(v)){
	    hi.color = Color.NEGRO;
	    super.giraDerecha(p);
	} else {
	    hd.color = Color.NEGRO;
	    super.giraIzquierda(p);
	}
		
    }
    
    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles
     * rojinegros no pueden ser girados a la izquierda por los usuarios de la
     * clase, porque se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraIzquierda(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles rojinegros no " +
                                                "pueden girar a la izquierda " +
                                                "por el usuario.");
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles
     * rojinegros no pueden ser girados a la derecha por los usuarios de la
     * clase, porque se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraDerecha(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles rojinegros no " +
                                                "pueden girar a la derecha " +
                                                "por el usuario.");
    }
}
