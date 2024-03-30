package it.unicam.cs.asdl2324.mp2;

import java.util.Set;
import java.util.HashSet;

//ATTENZIONE: è vietato includere import a pacchetti che non siano della Java SE

/**
 *
 * Classe singoletto che implementa l'algoritmo di Kruskal per trovare un
 * Minimum Spanning Tree di un grafo non orientato, pesato e con pesi non
 * negativi. L'algoritmo implementato si avvale della classe
 * {@code ForestDisjointSets<GraphNode<L>>} per gestire una collezione di
 * insiemi disgiunti di nodi del grafo.
 *
 * @author Luca Tesei (template) **
 *  *         ** (implementazione)
 *
 * @param <L>
 *                tipo delle etichette dei nodi del grafo
 *
 */
public class KruskalMST<L> {

    /*
     * Struttura dati per rappresentare gli insiemi disgiunti utilizzata
     * dall'algoritmo di Kruskal.
     */
    private ForestDisjointSets<GraphNode<L>> disjointSets;

    /**
     * Costruisce un calcolatore di un albero di copertura minimo che usa
     * l'algoritmo di Kruskal su un grafo non orientato e pesato.
     */
    public KruskalMST() {
        this.disjointSets = new ForestDisjointSets<GraphNode<L>>();
    }

    /**
     * Utilizza l'algoritmo goloso di Kruskal per trovare un albero di copertura
     * minimo in un grafo non orientato e pesato, con pesi degli archi non
     * negativi. L'albero restituito non è radicato, quindi è rappresentato
     * semplicemente con un sottoinsieme degli archi del grafo.
     *
     * @param g
     *              un grafo non orientato, pesato, con pesi non negativi
     * @return l'insieme degli archi del grafo g che costituiscono l'albero di
     *         copertura minimo trovato
     * @throw NullPointerException se il grafo g è null
     * @throw IllegalArgumentException se il grafo g è orientato, non pesato o
     *        con pesi negativi
     */
    public Set<GraphEdge<L>> computeMSP(Graph<L> g) {
        if (g == null)
            throw new NullPointerException("Grafo nullo");

        if (g.isDirected())
            throw new IllegalArgumentException("Il grafo non può essere orientato");

        disjointSets = new ForestDisjointSets<GraphNode<L>>();
        for (GraphNode<L> node : g.getNodes())
            disjointSets.makeSet(node);

        Set<GraphEdge<L>> edges = g.getEdges();

        GraphEdge<L> [] orderEdges = new GraphEdge[edges.size()];
        int arrIndex = 0;

        for (GraphEdge<L> edge : edges){
            if (edge.getWeight() < 0 || Double.isNaN(edge.getWeight()))
                throw new IllegalArgumentException("Peso negativo o albero non pesato");

            orderEdges[arrIndex] = edge;
            arrIndex++;
        }

        this.sort(orderEdges);
        Set<GraphEdge<L>> ret = new HashSet<GraphEdge<L>>();

        for (int i = 0; i < orderEdges.length; i++){
            GraphEdge<L> edge = orderEdges[i];
            if (!this.isCyclic(edge.getNode1(), edge.getNode2())){
                ret.add(edge);
                disjointSets.union(edge.getNode1(), edge.getNode2());
            }
        }
        return ret;
    }
    private void sort(GraphEdge<L> arr[])
    {
        int n = arr.length;
        for (int i = 1; i < n; ++i) {
            GraphEdge<L> edge = arr[i];
            int j = i - 1;

            while (j >= 0 && arr[j].getWeight() > edge.getWeight()) {
                arr[j + 1] = arr[j];
                j = j - 1;
            }
            arr[j + 1] = edge;
        }
    }
    private boolean isCyclic (GraphNode<L> e1, GraphNode<L> e2){
        return this.disjointSets.findSet(e1) == this.disjointSets.findSet(e2);
    }
}
