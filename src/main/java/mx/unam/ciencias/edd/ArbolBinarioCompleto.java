package mx.unam.ciencias.edd;

import java.util.Iterator;

/**
 * <p>Clase para árboles binarios completos.</p>
 *
 * <p>Un árbol binario completo agrega y elimina elementos de tal forma que el
 * árbol siempre es lo más cercano posible a estar lleno.</p>
 */
public class ArbolBinarioCompleto<T> extends ArbolBinario<T> {

    /* Clase interna privada para iteradores. */
    private class Iterador implements Iterator<T> {

        /* Cola para recorrer los vértices en BFS. */
        private Cola<Vertice> cola;

        /* Inicializa al iterador. */
        private Iterador() {
            // Aquí va su código.
            this.cola = new Cola<Vertice>();

            if (raiz != null)
                cola.mete(raiz);
        }

        /* Nos dice si hay un elemento siguiente. */
        @Override public boolean hasNext() {
            // Aquí va su código.
			return !cola.esVacia();
        }

        /* Regresa el siguiente elemento en orden BFS. */
        @Override public T next() {
            // Aquí va su código.
			Vertice v = cola.saca();

			if(v.hayIzquierdo())
				cola.mete(v.izquierdo);
			if(v.hayDerecho())
				cola.mete(v.derecho);

			return v.elemento;
			
        }
    }

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinario}.
     */
    public ArbolBinarioCompleto() { super(); }

    /**
     * Construye un árbol binario completo a partir de una colección. El árbol
     * binario completo tiene los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        binario completo.
     */
    public ArbolBinarioCompleto(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
     * Agrega un elemento al árbol binario completo. El nuevo elemento se coloca
     * a la derecha del último nivel, o a la izquierda de un nuevo nivel.
     * @param elemento el elemento a agregar al árbol.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    @Override public void agrega(T elemento) {
        // Aquí va su código.
		if(elemento == null)
			throw new IllegalArgumentException("Elemento inexistente");
		// Vertice a agregar
		Vertice v = nuevoVertice(elemento);
		elementos ++;
		
		if(raiz == null){
			raiz = v;
			return;
		}
		// Vertice con el primer hoyo.
		Vertice ver = vertice(hoyoAux());
		v.padre = ver;

		if(ver.izquierdo == null)
			ver.izquierdo = v;
		else
			ver.derecho = v;
    }
	
	/* Nos da el primer vertice que no tiene hijo izquierdo
	 * e hijo derecho, es decir, el primer hoyo. Para posteriormente
	 * hacerlo padre el vertice a agregar. */
	
    private VerticeArbolBinario<T> hoyoAux() {
        Cola<Vertice> cola = new Cola<Vertice>();
        cola.mete(raiz);

        Vertice v;
        while(!cola.esVacia()) {
            v = cola.saca();

            if (v.izquierdo == null || v.derecho == null)
                return v;

            cola.mete(v.izquierdo);
            cola.mete(v.derecho);
        }

        return null;
    }

	
    /**
     * Elimina un elemento del árbol. El elemento a eliminar cambia lugares con
     * el último elemento del árbol al recorrerlo por BFS, y entonces es
     * eliminado.
     * @param elemento el elemento a eliminar.
     */
    @Override public void elimina(T elemento) {
        // Aquí va su código.
		Vertice v = vertice(busca(elemento));

		if(v == null)
			return;

		elementos --;

		if(elementos == 0){
			raiz = null;
			return;
		}

		Vertice ultimo = vertice(ultimoVertice());
		v.elemento = ultimo.elemento;

		if(ultimo.padre.izquierdo == ultimo)
			ultimo.padre.izquierdo = null;

		if(ultimo.padre.derecho == ultimo)
			ultimo.padre.derecho = null;
		
		
    }

	/* Nos devuelve el último vertice agregado.
	 * Por definición es el último que queda en la cola despús
	 * de recorrer el árbol por BFS */
	
	private VerticeArbolBinario<T> ultimoVertice(){

        Cola<Vertice> cola = new Cola<Vertice>();
        cola.mete(raiz);
		Vertice ultimo = raiz;
        Vertice v;

        while(!cola.esVacia()) {
            v = cola.saca();
			ultimo = v;
			
            if(v.izquierdo != null)
				cola.mete(v.izquierdo);

            if(v.derecho != null)
				cola.mete(v.derecho);

        }

        return ultimo;

	}
	
    /**
     * Regresa la altura del árbol. La altura de un árbol binario completo
     * siempre es ⌊log<sub>2</sub><em>n</em>⌋.
     * @return la altura del árbol.
     */
    @Override public int altura() {
        // Aquí va su código.
		if(elementos == 0)
			return -1;
		
		return (int) Math.floor(Math.log(elementos) / Math.log(2));
    }

    /**
     * Realiza un recorrido BFS en el árbol, ejecutando la acción recibida en
     * cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void bfs(AccionVerticeArbolBinario<T> accion) {
        // Aquí va su código.
		if(esVacia())
			return;

        Cola<Vertice> cola = new Cola<>();
        cola.mete(raiz);
	
		while(!cola.esVacia()){
			
			Vertice v = cola.saca();
			accion.actua(v);

            if (v.izquierdo != null)
                cola.mete(v.izquierdo);

            if (v.derecho != null)
                cola.mete(v.derecho);			
			
		}
		
	}

    /**
     * Regresa un iterador para iterar el árbol. El árbol se itera en orden BFS.
     * @return un iterador para iterar el árbol.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }
}
