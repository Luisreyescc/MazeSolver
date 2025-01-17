package mx.unam.ciencias.edd;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Clase para gráficas. Una gráfica es un conjunto de vértices y aristas, tales
 * que las aristas son un subconjunto del producto cruz de los vértices.
 */
public class Grafica<T> implements Coleccion<T> {

    /* Clase interna privada para iteradores. */
    private class Iterador implements Iterator<T> {

        /* Iterador auxiliar. */
        private Iterator<Vertice> iterador;

        /* Construye un nuevo iterador, auxiliándose de la lista de vértices. */
        public Iterador() {
            // Aquí va su código.
		    iterador = vertices.iterator();
        }

        /* Nos dice si hay un siguiente elemento. */
        @Override public boolean hasNext() {
            // Aquí va su código.
		    return iterador.hasNext();
        }

        /* Regresa el siguiente elemento. */
        @Override public T next() {
            // Aquí va su código.
		    return iterador.next().elemento;
        }
    }

    /* Clase interna privada para vértices. */
    private class Vertice implements VerticeGrafica<T>,
                          ComparableIndexable<Vertice> {

        /* El elemento del vértice. */
        private T elemento;
        /* El color del vértice. */
        private Color color;
        /* La distancia del vértice. */
        private double distancia;
        /* El índice del vértice. */
        private int indice;
        /* La lista de vecinos del vértice. */
        private Lista<Vecino> vecinos;

        /* Crea un nuevo vértice a partir de un elemento. */
        public Vertice(T elemento) {
	    // Aquí va su código.
		    this.elemento = elemento;
		    color = Color.NINGUNO;
		    vecinos = new Lista<>();
        }

        /* Regresa el elemento del vértice. */
        @Override public T get() {
            // Aquí va su código.
		    return elemento;
        }

        /* Regresa el grado del vértice. */
        @Override public int getGrado() {
            // Aquí va su código.
		    return vecinos.getLongitud();
        }

        /* Regresa el color del vértice. */
        @Override public Color getColor() {
            // Aquí va su código.
		    return color;
        }

        /* Regresa un iterable para los vecinos. */
        @Override public Iterable<? extends VerticeGrafica<T>> vecinos() {
            // Aquí va su código.
		    return vecinos;
        }

        /* Define el índice del vértice. */
        @Override public void setIndice(int indice) {
            // Aquí va su código.
			this.indice = indice;
        }

        /* Regresa el índice del vértice. */
        @Override public int getIndice() {
            // Aquí va su código.
			return indice;
        }

        /* Compara dos vértices por distancia. */
        @Override public int compareTo(Vertice vertice) {
            // Aquí va su código.
			return Double.compare(distancia, vertice.distancia); 
		}
    }

    /* Clase interna privada para vértices vecinos. */
    private class Vecino implements VerticeGrafica<T> {

        /* El vértice vecino. */
        public Vertice vecino;
        /* El peso de la arista conectando al vértice con su vértice vecino. */
        public double peso;

        /* Construye un nuevo vecino con el vértice recibido como vecino y el
         * peso especificado. */
        public Vecino(Vertice vecino, double peso) {
            // Aquí va su código.
			this.vecino = vecino;
			this.peso = peso;
		}

        /* Regresa el elemento del vecino. */
        @Override public T get() {
            // Aquí va su código.
			return vecino.elemento;
        }

        /* Regresa el grado del vecino. */
        @Override public int getGrado() {
            // Aquí va su código.
			return vecino.getGrado();
        }

        /* Regresa el color del vecino. */
        @Override public Color getColor() {
            // Aquí va su código.
			return vecino.color;
        }

        /* Regresa un iterable para los vecinos del vecino. */
        @Override public Iterable<? extends VerticeGrafica<T>> vecinos() {
            // Aquí va su código.
			return vecino.vecinos;
        }
    }

    /* Interface para poder usar lambdas al buscar el elemento que sigue al
     * reconstruir un camino. */
    @FunctionalInterface
    private interface BuscadorCamino<T> {
        /* Regresa true si el vértice se sigue del vecino. */
        public boolean seSiguen(Grafica<T>.Vertice v, Grafica<T>.Vecino a);
    }

    /* Vértices. */
    private Lista<Vertice> vertices;
    /* Número de aristas. */
    private int aristas;

    /**
     * Constructor único.
     */
    public Grafica() {
        // Aquí va su código.
		vertices = new Lista<>();
    }

    /**
     * Regresa el número de elementos en la gráfica. El número de elementos es
     * igual al número de vértices.
     * @return el número de elementos en la gráfica.
     */
    @Override public int getElementos() {
        // Aquí va su código.
		return vertices.getLongitud();
    }

    /**
     * Regresa el número de aristas.
     * @return el número de aristas.
     */
    public int getAristas() {
        // Aquí va su código.
		return aristas;
    }

    /**
     * Agrega un nuevo elemento a la gráfica.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si el elemento ya había sido agregado a
     *         la gráfica.
     */
    @Override public void agrega(T elemento) {
        // Aquí va su código.
		if(elemento == null || contiene(elemento))
		    throw new IllegalArgumentException();
	
		Vertice v = new Vertice(elemento);
		vertices.agrega(v);
	    }

    /**
     * Conecta dos elementos de la gráfica. Los elementos deben estar en la
     * gráfica. El peso de la arista que conecte a los elementos será 1.
     * @param a el primer elemento a conectar.
     * @param b el segundo elemento a conectar.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b ya están conectados, o si a es
     *         igual a b.
     */
    public void conecta(T a, T b) {
        // Aquí va su código.
		conecta(a,b,1);
    }

    /**
     * Conecta dos elementos de la gráfica. Los elementos deben estar en la
     * gráfica.
     * @param a el primer elemento a conectar.
     * @param b el segundo elemento a conectar.
     * @param peso el peso de la nueva vecino.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b ya están conectados, si a es
     *         igual a b, o si el peso es no positivo.
     */
    public void conecta(T a, T b, double peso) {
        // Aquí va su código.
		if (a.equals(b))
            throw new IllegalArgumentException();

        if (peso <= 0)
            throw new IllegalArgumentException();

        Vertice u = (Vertice) vertice(a);
        Vertice v = (Vertice) vertice(b);

        if (sonVecinos(u.elemento, v.elemento))
            throw new IllegalArgumentException();

        u.vecinos.agrega(new Vecino(v, peso));
        v.vecinos.agrega(new Vecino(u, peso));
        aristas++;
    }

    /**
     * Desconecta dos elementos de la gráfica. Los elementos deben estar en la
     * gráfica y estar conectados entre ellos.
     * @param a el primer elemento a desconectar.
     * @param b el segundo elemento a desconectar.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b no están conectados.
     */
    public void desconecta(T a, T b) {
        // Aquí va su código.
        Vertice u = busca(a);
        Vertice v = busca(b);

        if(u == null || v == null)
            throw new NoSuchElementException();

        if(!sonVecinos(a, b))
            throw new IllegalArgumentException();

        eliminaVecino(u.vecinos, b);
        eliminaVecino(v.vecinos, a);
        aristas--;
   }

    /**
     * Nos dice si el elemento está contenido en la gráfica.
     * @return <code>true</code> si el elemento está contenido en la gráfica,
     *         <code>false</code> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
        // Aquí va su código.
		for(Vertice v : vertices)
		    if(v.elemento.equals(elemento))
			return true;
		
		return false;
    }

    /**
     * Elimina un elemento de la gráfica. El elemento tiene que estar contenido
     * en la gráfica.
     * @param elemento el elemento a eliminar.
     * @throws NoSuchElementException si el elemento no está contenido en la
     *         gráfica.
     */
    @Override public void elimina(T elemento) {
        // Aquí va su código.
        Vertice v = busca(elemento);

        if(v == null)
            throw new NoSuchElementException();

        for(Vecino vecino : v.vecinos){
            eliminaVecino(vecino.vecino.vecinos, elemento);
            aristas--;
        }

        vertices.elimina(v);
	}

    private void eliminaVecino(Lista<Vecino> vecinos, T elemento){
		for(Vecino vecino :  vecinos)
			if(vecino.vecino.elemento.equals(elemento)){
				vecinos.elimina(vecino);
				return;
		}
	}

    /* Método auxiliar que regresa el vertice dado un elemento (Si es que este
     * se eneuntra en la grafica). */

    private Vertice busca(T elemento){
		for(Vertice v : vertices)
		    if(v.elemento.equals(elemento))
			return v;
	
		return null;
	    }
    
    /**
     * Nos dice si dos elementos de la gráfica están conectados. Los elementos
     * deben estar en la gráfica.
     * @param a el primer elemento.
     * @param b el segundo elemento.
     * @return <code>true</code> si a y b son vecinos, <code>false</code> en otro caso.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     */
    public boolean sonVecinos(T a, T b) {
        // Aquí va su código.
        Vertice u = (Vertice) vertice(a);
        Vertice v = (Vertice) vertice(b);

        for (Vecino vecino : u.vecinos)
            if (vecino.vecino.elemento.equals(v.elemento))
                return true;

        return false;
	}

    /**
     * Regresa el peso de la arista que comparten los vértices que contienen a
     * los elementos recibidos.
     * @param a el primer elemento.
     * @param b el segundo elemento.
     * @return el peso de la arista que comparten los vértices que contienen a
     *         los elementos recibidos.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b no están conectados.
     */
    public double getPeso(T a, T b) {
        // Aquí va su código.
        if (!contiene(a))
            throw new NoSuchElementException("El vertice no pertenece a V(G).");

        Vertice u = (Vertice) vertice(b);

        for (Vecino vecino : u.vecinos)
            if (vecino.vecino.elemento.equals(a))
                return vecino.peso;

        throw new IllegalArgumentException("Vertices deconectados.");
	}

    /**
     * Define el peso de la arista que comparten los vértices que contienen a
     * los elementos recibidos.
     * @param a el primer elemento.
     * @param b el segundo elemento.
     * @param peso el nuevo peso de la arista que comparten los vértices que
     *        contienen a los elementos recibidos.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b no están conectados, o si peso
     *         es menor o igual que cero.
     */
    public void setPeso(T a, T b, double peso) {
        // Aquí va su código.
	    Vertice u = (Vertice) vertice(a);
        Vertice v = (Vertice) vertice(b);

        if (peso <= 0)
            throw new IllegalArgumentException("Peso erroneo.");

        if (!sonVecinos(u.elemento, v.elemento))
            throw new IllegalArgumentException();

        for (Vecino vecino : u.vecinos)
            if (vecino.vecino.equals(v)) {
                vecino.peso = peso;
                break;
            }

        for (Vecino vecino : v.vecinos)
            if (vecino.vecino.equals(u)) {
                vecino.peso = peso;
                break;
            }	
    }

    /**
     * Regresa el vértice correspondiente el elemento recibido.
     * @param elemento el elemento del que queremos el vértice.
     * @throws NoSuchElementException si elemento no es elemento de la gráfica.
     * @return el vértice correspondiente el elemento recibido.
     */
    public VerticeGrafica<T> vertice(T elemento) {
        // Aquí va su código.
		for(Vertice v : vertices)
		    if(v.elemento.equals(elemento))
			return v;
	
		throw new NoSuchElementException("El elemento no está en la grafica");	
    }

    /**
     * Define el color del vértice recibido.
     * @param vertice el vértice al que queremos definirle el color.
     * @param color el nuevo color del vértice.
     * @throws IllegalArgumentException si el vértice no es válido.
     */
    public void setColor(VerticeGrafica<T> vertice, Color color) {
        // Aquí va su código.
        if (vertice == null || (vertice.getClass() != Vertice.class &&
			vertice.getClass() != Vecino.class)) 
			throw new IllegalArgumentException();

        if (vertice.getClass() == Vertice.class) {
            Vertice x = (Vertice) vertice;
            x.color = color;
        }

        if (vertice.getClass() == Vecino.class) {
            Vecino r = (Vecino) vertice;
            r.vecino.color = color;
        }
    }

    /**
     * Nos dice si la gráfica es conexa.
     * @return <code>true</code> si la gráfica es conexa, <code>false</code> en
     *         otro caso.
     */
    public boolean esConexa() {
        // Aquí va su código.
	    Lista<Vertice> l = new Lista<Vertice>();
        bfs(vertices.getPrimero().get(), v -> l.agrega((Vertice)v));

        return vertices.getLongitud() == l.getLongitud();
	}

    /**
     * Realiza la acción recibida en cada uno de los vértices de la gráfica, en
     * el orden en que fueron agregados.
     * @param accion la acción a realizar.
     */
    public void paraCadaVertice(AccionVerticeGrafica<T> accion) {
        // Aquí va su código.
		for(Vertice v : vertices)
		    accion.actua(v);
    }

    private void recorrer(T elemento, AccionVerticeGrafica<T> a, MeteSaca<Vertice> msv){
        Vertice u = busca(elemento);

        if(u == null)
            throw new NoSuchElementException();

        paraCadaVertice(v -> setColor(v, Color.ROJO));
        setColor(u, Color.NEGRO);
        msv.mete(u);
        Vertice auxiliar;

        while(!msv.esVacia()) {
            auxiliar = msv.saca();
            a.actua(auxiliar);
            for(Vecino vecino : auxiliar.vecinos)
                if(vecino.getColor() == Color.ROJO) {
                    setColor(vecino, Color.NEGRO);
                    msv.mete(vecino.vecino);
                }
        }

        paraCadaVertice(v -> setColor(v, Color.NINGUNO));  
	}
    
    /**
     * Realiza la acción recibida en todos los vértices de la gráfica, en el
     * orden determinado por BFS, comenzando por el vértice correspondiente al
     * elemento recibido. Al terminar el método, todos los vértices tendrán
     * color {@link Color#NINGUNO}.
     * @param elemento el elemento sobre cuyo vértice queremos comenzar el
     *        recorrido.
     * @param accion la acción a realizar.
     * @throws NoSuchElementException si el elemento no está en la gráfica.
     */
    public void bfs(T elemento, AccionVerticeGrafica<T> accion) {
        // Aquí va su código.
		Cola<Vertice> cola = new Cola<>();
		recorrer(elemento, accion, cola);
    }

    /**
     * Realiza la acción recibida en todos los vértices de la gráfica, en el
     * orden determinado por DFS, comenzando por el vértice correspondiente al
     * elemento recibido. Al terminar el método, todos los vértices tendrán
     * color {@link Color#NINGUNO}.
     * @param elemento el elemento sobre cuyo vértice queremos comenzar el
     *        recorrido.
     * @param accion la acción a realizar.
     * @throws NoSuchElementException si el elemento no está en la gráfica.
     */
    public void dfs(T elemento, AccionVerticeGrafica<T> accion) {
        // Aquí va su código.
		Pila<Vertice> pila = new Pila<>();
		recorrer(elemento, accion, pila);
	}

    /**
     * Nos dice si la gráfica es vacía.
     * @return <code>true</code> si la gráfica es vacía, <code>false</code> en
     *         otro caso.
     */
    @Override public boolean esVacia() {
        // Aquí va su código.
		return vertices.esVacia();
    }

    /**
     * Limpia la gráfica de vértices y aristas, dejándola vacía.
     */
    @Override public void limpia() {
        // Aquí va su código.
		vertices.limpia();
		aristas = 0;
    }

    /**
     * Regresa una representación en cadena de la gráfica.
     * @return una representación en cadena de la gráfica.
     */
    @Override public String toString() {
        // Aquí va su código.
		String s = "{";
		Lista<Vertice> l = new Lista<>();
		for(Vertice v : vertices)
		    s += v.elemento.toString() + ", ";
		s += "}, {";
		for(Vertice n : vertices){
		    for(Vertice k : vertices)
			if(sonVecinos(n.elemento, k.elemento) && !l.contiene(k))
			    s += "(" + n.elemento.toString() + ", " + k.elemento.toString() + "), ";
		    l.agrega(n);
		}
		return s + "}";
    }

    /**
     * Nos dice si la gráfica es igual al objeto recibido.
     * @param objeto el objeto con el que hay que comparar.
     * @return <code>true</code> si la gráfica es igual al objeto recibido;
     *         <code>false</code> en otro caso.
     */
    @Override public boolean equals(Object objeto) {
        if (objeto == null || getClass() != objeto.getClass())
            return false;
        @SuppressWarnings("unchecked") Grafica<T> grafica = (Grafica<T>)objeto;
        // Aquí va su código.
        if(getAristas() != grafica.getAristas() || vertices.getLongitud() != grafica.vertices.getLongitud())
            return false;

        for(Vertice v : vertices)
            if(!grafica.contiene(v.get()))
                return false;
            else
                for(Vecino vecino : v.vecinos)
                    if(!grafica.sonVecinos(v.get(), vecino.get()))
                        return false;
         
         return true;
    }

    /**
     * Regresa un iterador para iterar la gráfica. La gráfica se itera en el
     * orden en que fueron agregados sus elementos.
     * @return un iterador para iterar la gráfica.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }

    private Lista<VerticeGrafica<T>> reconstruirTrayectoria(BuscadorCamino<T> bc, Vertice destino) {
        Vertice v = destino;
        Lista<VerticeGrafica<T>> l = new Lista<>();

        if (v.distancia == Double.MAX_VALUE)
            return new Lista<VerticeGrafica<T>>();

        l.agrega(v);

        while (v.distancia != 0) {
            for (Vecino vecino : v.vecinos) {
                if (bc.seSiguen(v, vecino)) {
                    l.agrega(vecino.vecino);
                    v = vecino.vecino;
                    break;
                }

            }
        }

        return l.reversa();
    }

    /**
     * Calcula una trayectoria de distancia mínima entre dos vértices.
     * @param origen el vértice de origen.
     * @param destino el vértice de destino.
     * @return Una lista con vértices de la gráfica, tal que forman una
     *         trayectoria de distancia mínima entre los vértices <code>a</code> y
     *         <code>b</code>. Si los elementos se encuentran en componentes conexos
     *         distintos, el algoritmo regresa una lista vacía.
     * @throws NoSuchElementException si alguno de los dos elementos no está en
     *         la gráfica.
     */
    public Lista<VerticeGrafica<T>> trayectoriaMinima(T origen, T destino) {
        // Aquí va su código.
        if (!contiene(origen) || !contiene(destino))
            throw new NoSuchElementException();

        Vertice u = (Vertice) vertice(origen);
        if (origen.equals(destino)) {
            Lista<VerticeGrafica<T>> l = new Lista<>();
            l.agrega(u);
            return l;
        }

        for (Vertice v : vertices)
            v.distancia = Double.MAX_VALUE;

        u.distancia = 0;

        Cola<Vertice> cola = new Cola<>();
        cola.mete(u);

        while (!cola.esVacia()) {
            u = cola.saca();
            for (Vecino vecino : u.vecinos)
                if (vecino.vecino.distancia == Double.MAX_VALUE) {
                    vecino.vecino.distancia = u.distancia + 1;
                    cola.mete(vecino.vecino);
                }
        }

        return reconstruirTrayectoria(
                (aux, vecino) -> vecino.vecino.distancia == aux.distancia - 1,
                (Vertice) vertice(destino));
	}

    /**
     * Calcula la ruta de peso mínimo entre el elemento de origen y el elemento
     * de destino.
     * @param origen el vértice origen.
     * @param destino el vértice destino.
     * @return una trayectoria de peso mínimo entre el vértice <code>origen</code> y
     *         el vértice <code>destino</code>. Si los vértices están en componentes
     *         conexas distintas, regresa una lista vacía.
     * @throws NoSuchElementException si alguno de los dos elementos no está en
     *         la gráfica.
     */
    public Lista<VerticeGrafica<T>> dijkstra(T origen, T destino) {
        // Aquí va su código.
        if (!contiene(origen) || !contiene(destino))
            throw new NoSuchElementException();

        for (Vertice v : vertices)
            v.distancia = Double.MAX_VALUE;

        Vertice u = (Vertice) vertice(origen);
        u.distancia = 0;

        MonticuloDijkstra<Vertice> monticulo;
        int n = vertices.getElementos();
        if (aristas > ((n*(n - 1))/2)-n)
            monticulo = new MonticuloArreglo<>(vertices);
        else
            monticulo = new MonticuloMinimo<>(vertices);

        while (!monticulo.esVacia()) {
            Vertice raiz = monticulo.elimina();

            for (Vecino vecino : raiz.vecinos)
                if (vecino.vecino.distancia > raiz.distancia + vecino.peso) {
                    vecino.vecino.distancia = raiz.distancia + vecino.peso;
                    monticulo.reordena(vecino.vecino);
                }
        }

        return reconstruirTrayectoria(
                (vertice, vecino) -> vecino.vecino.distancia + vecino.peso == vertice.distancia,
                (Vertice) vertice(destino));
	}
}
