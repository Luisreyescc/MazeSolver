package mx.unam.ciencias.edd;

import java.util.Iterator;

/**
 * <p>Clase para árboles binarios ordenados. Los árboles son genéricos, pero
 * acotados a la interfaz {@link Comparable}.</p>
 *
 * <p>Un árbol instancia de esta clase siempre cumple que:</p>
 * <ul>
 *   <li>Cualquier elemento en el árbol es mayor o igual que todos sus
 *       descendientes por la izquierda.</li>
 *   <li>Cualquier elemento en el árbol es menor o igual que todos sus
 *       descendientes por la derecha.</li>
 * </ul>
 */
public class ArbolBinarioOrdenado<T extends Comparable<T>>
    extends ArbolBinario<T> {

    /* Clase interna privada para iteradores. */
    private class Iterador implements Iterator<T> {

        /* Pila para recorrer los vértices en DFS in-order. */
        private Pila<Vertice> pila;

        /* Inicializa al iterador. */
        private Iterador() {
            // Aquí va su código.
	    pila = new Pila<Vertice>();
	    if(raiz == null)
		return;

	    pila.mete(raiz);
	    Vertice v = raiz;

	    while((v = v.izquierdo) != null)
		pila.mete(v);
        }

        /* Nos dice si hay un elemento siguiente. */
        @Override public boolean hasNext() {
            // Aquí va su código.
	    return !pila.esVacia();
        }

        /* Regresa el siguiente elemento en orden DFS in-order. */
        @Override public T next() {
            // Aquí va su código.
	    Vertice v = pila.saca();

	    if(v.hayDerecho()){
		Vertice vder = v.derecho;
		pila.mete(vder);

		while((vder = vder.izquierdo) != null)
		    pila.mete(vder);
	    }

	    return v.elemento;
        }
    }

    /**
     * El vértice del último elemento agegado. Este vértice sólo se puede
     * garantizar que existe <em>inmediatamente</em> después de haber agregado
     * un elemento al árbol. Si cualquier operación distinta a agregar sobre el
     * árbol se ejecuta después de haber agregado un elemento, el estado de esta
     * variable es indefinido.
     */
    protected Vertice ultimoAgregado;

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinario}.
     */
    public ArbolBinarioOrdenado() { super(); }

    /**
     * Construye un árbol binario ordenado a partir de una colección. El árbol
     * binario ordenado tiene los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        binario ordenado.
     */
    public ArbolBinarioOrdenado(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
     * Agrega un nuevo elemento al árbol. El árbol conserva su orden in-order.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) {
        // Aquí va su código.
	if(elemento == null)
	    throw new IllegalArgumentException("Elemento invalido");
	    
	Vertice v = nuevoVertice(elemento);
	elementos++;

	if(raiz == null)
	    raiz = v;
	else
	   agregaAux(raiz, v);

	ultimoAgregado = v;
	
    }

    private void agregaAux(Vertice actual, Vertice nuevo){
	if(nuevo.elemento.compareTo(actual.elemento) <= 0){
	    if(actual.izquierdo == null){
		actual.izquierdo = nuevo;
		nuevo.padre = actual;
	    } else {
		agregaAux(actual.izquierdo, nuevo);
	    }
	} else {
	    if(actual.derecho == null){
		actual.derecho = nuevo;
		nuevo.padre = actual;
	    } else {
		agregaAux(actual.derecho, nuevo);
	    }
	      
	} 
	
    }
    
    /**
     * Elimina un elemento. Si el elemento no está en el árbol, no hace nada; si
     * está varias veces, elimina el primero que encuentre (in-order). El árbol
     * conserva su orden in-order.
     * @param elemento el elemento a eliminar.
     */
    @Override public void elimina(T elemento) {
        Vertice v = vertice(busca(elemento));

	if(v == null)
	    return;

	elementos--;
	
	if(v.izquierdo != null && v.derecho != null)
	    v = intercambiaEliminable(v);

	eliminaVertice(v);
    }

    /**
     * Intercambia el elemento de un vértice con dos hijos distintos de
     * <code>null</code> con el elemento de un descendiente que tenga a lo más
     * un hijo.
     * @param vertice un vértice con dos hijos distintos de <code>null</code>.
     * @return el vértice descendiente con el que vértice recibido se
     *         intercambió. El vértice regresado tiene a lo más un hijo distinto
     *         de <code>null</code>.
     */
    protected Vertice intercambiaEliminable(Vertice vertice) {
        // Aquí va su código.
	Vertice maximal = maximoEnSubarbol(vertice.izquierdo);
	T maximalElemento = maximal.elemento;

	maximal.elemento = vertice.elemento;
	vertice.elemento = maximalElemento;

	return maximal;
	
    }

    private Vertice maximoEnSubarbol(Vertice v){
	if(v.derecho == null)
	    return v;

	return maximoEnSubarbol(v.derecho);
    }
    
    /**
     * Elimina un vértice que a lo más tiene un hijo distinto de
     * <code>null</code> subiendo ese hijo (si existe).
     * @param vertice el vértice a eliminar; debe tener a lo más un hijo
     *                distinto de <code>null</code>.
     */
    protected void eliminaVertice(Vertice vertice) {
        // Aquí va su código.
	Vertice hijo = vertice.izquierdo != null ? vertice.izquierdo : vertice.derecho;

	if(vertice.padre == null)
	    raiz = hijo;
	else
	    if(vertice.padre.izquierdo == vertice)
		vertice.padre.izquierdo = hijo;
	    else
		vertice.padre.derecho = hijo;

	if(hijo != null)
	    hijo.padre = vertice.padre;	
	    
    }

    /**
     * Busca un elemento en el árbol recorriéndolo in-order. Si lo encuentra,
     * regresa el vértice que lo contiene; si no, regresa <code>null</code>.
     * @param elemento el elemento a buscar.
     * @return un vértice que contiene al elemento buscado si lo
     *         encuentra; <code>null</code> en otro caso.
     */
    @Override public VerticeArbolBinario<T> busca(T elemento) {
        // Aquí va su código.
	return buscaAux(elemento, raiz);
    }

    private VerticeArbolBinario<T> buscaAux(T elemento, Vertice v){
	if(v == null)
	    return null;

	if(elemento.equals(v.elemento))
	    return v;

	if(elemento.compareTo(v.elemento) < 0)
	    return buscaAux(elemento, v.izquierdo);

	return buscaAux(elemento, v.derecho);
		
    }

    /**
     * Regresa el vértice que contiene el último elemento agregado al
     * árbol. Este método sólo se puede garantizar que funcione
     * <em>inmediatamente</em> después de haber invocado al método {@link
     * agrega}. Si cualquier operación distinta a agregar sobre el árbol se
     * ejecuta después de haber agregado un elemento, el comportamiento de este
     * método es indefinido.
     * @return el vértice que contiene el último elemento agregado al árbol, si
     *         el método es invocado inmediatamente después de agregar un
     *         elemento al árbol.
     */
    public VerticeArbolBinario<T> getUltimoVerticeAgregado() {
        return ultimoAgregado;
    }

    /**
     * Gira el árbol a la derecha sobre el vértice recibido. Si el vértice no
     * tiene hijo izquierdo, el método no hace nada.
     * @param vertice el vértice sobre el que vamos a girar.
     */
    public void giraDerecha(VerticeArbolBinario<T> vertice) {
        // Aquí va su código.
        Vertice actual = vertice(vertice);

        if(actual.izquierdo == null)
            return;

        Vertice hijo = actual.izquierdo;
        hijo.padre = actual.padre;

        if(actual.padre == null)
            raiz = hijo;
        else {
            if (actual.padre.izquierdo == actual)
                actual.padre.izquierdo = hijo;
            else
                actual.padre.derecho = hijo;
        }

        actual.izquierdo = hijo.derecho;

        if(actual.izquierdo != null)
            actual.izquierdo.padre = actual;
	
        hijo.derecho = actual;
        actual.padre = hijo;
    }

    /**
     * Gira el árbol a la izquierda sobre el vértice recibido. Si el vértice no
     * tiene hijo derecho, el método no hace nada.
     * @param vertice el vértice sobre el que vamos a girar.
     */
    public void giraIzquierda(VerticeArbolBinario<T> vertice) {
	// Aquí va su código.
        Vertice actual = vertice(vertice);

        if(actual.derecho == null)
            return;

        Vertice hijo = actual.derecho;
        hijo.padre = actual.padre;

        if(actual.padre == null)
            raiz = hijo;
        else {
            if(actual.padre.derecho == actual)
                actual.padre.derecho = hijo;
            else
                actual.padre.izquierdo = hijo;
        }

        actual.derecho = hijo.izquierdo;

        if(actual.derecho != null)
            actual.derecho.padre = actual;

	hijo.izquierdo = actual;
        actual.padre = hijo;
	
    }

    /**
     * Realiza un recorrido DFS <em>pre-order</em> en el árbol, ejecutando la
     * acción recibida en cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void dfsPreOrder(AccionVerticeArbolBinario<T> accion) {
        // Aquí va su código.
	dfsPreOrderAux(accion, raiz);
    }

    private void dfsPreOrderAux(AccionVerticeArbolBinario<T> accion, Vertice v){
	if(v == null)
	    return;
	
	accion.actua(v);
	dfsPreOrderAux(accion, v.izquierdo);
	dfsPreOrderAux(accion, v.derecho);
	    
    }

    /**
     * Realiza un recorrido DFS <em>in-order</em> en el árbol, ejecutando la
     * acción recibida en cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void dfsInOrder(AccionVerticeArbolBinario<T> accion) {
        // Aquí va su código.
	dfsInOrderAux(accion, raiz);
    }

    private void dfsInOrderAux(AccionVerticeArbolBinario<T> accion, Vertice v){
	if(v == null)
	    return;
	
	dfsInOrderAux(accion, v.izquierdo);
	accion.actua(v);
	dfsInOrderAux(accion, v.derecho);
	    
    }

    /**
     * Realiza un recorrido DFS <em>post-order</em> en el árbol, ejecutando la
     * acción recibida en cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void dfsPostOrder(AccionVerticeArbolBinario<T> accion) {
        // Aquí va su código.
	dfsPostOrderAux(accion, raiz);
    }

    private void dfsPostOrderAux(AccionVerticeArbolBinario<T> accion, Vertice v){
	if(v == null)
	    return;
	
	dfsPostOrderAux(accion, v.izquierdo);
	dfsPostOrderAux(accion, v.derecho);
	accion.actua(v);
	
    }

    
    /**
     * Regresa un iterador para iterar el árbol. El árbol se itera en orden.
     * @return un iterador para iterar el árbol.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }
}
