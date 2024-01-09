/**
 *
 */
package it.unicam.cs.asdl2324.es12;

import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Implementazione della classe astratta {@code Graph<L>} che realizza un grafo
 * non orientato. Per la rappresentazione viene usata una variante della
 * rappresentazione a liste di adiacenza. A differenza della rappresentazione
 * standard si usano strutture dati più efficienti per quanto riguarda la
 * complessità in tempo della ricerca se un nodo è presente (pseudocostante, con
 * tabella hash) e se un arco è presente (pseudocostante, con tabella hash). Lo
 * spazio occupato per la rappresentazione risultà tuttavia più grande di quello
 * che servirebbe con la rappresentazione standard.
 *
 * Le liste di adiacenza sono rappresentate con una mappa (implementata con
 * tabelle hash) che associa ad ogni nodo del grafo i nodi adiacenti. In questo
 * modo il dominio delle chiavi della mappa è il set dei nodi, su cui è
 * possibile chiamare il metodo contains per testare la presenza o meno di un
 * nodo. Ad ogni chiave della mappa, cioè ad ogni nodo del grafo, non è
 * associata una lista concatenata dei nodi collegati, ma un set di oggetti
 * della classe GraphEdge<L> che rappresentano gli archi connessi al nodo: in
 * questo modo la rappresentazione riesce a contenere anche l'eventuale peso
 * dell'arco (memorizzato nell'oggetto della classe GraphEdge<L>). Per
 * controllare se un arco è presente basta richiamare il metodo contains in
 * questo set. I test di presenza si basano sui metodi equals ridefiniti per
 * nodi e archi nelle classi GraphNode<L> e GraphEdge<L>.
 *
 * Questa classe non supporta le operazioni di rimozione di nodi e archi e le
 * operazioni indicizzate di ricerca di nodi e archi.
 *
 * @author Template: Luca Tesei, Implementazione: collettiva
 *
 * @param <L>
 *                etichette dei nodi del grafo
 */
public class MapAdjacentListUndirectedGraph<L> extends Graph<L> {

    /*
     * Le liste di adiacenza sono rappresentate con una mappa. Ogni nodo viene
     * associato con l'insieme degli archi uscenti. Nel caso in cui un nodo non
     * abbia archi uscenti è associato con un insieme vuoto.
     */
    private final Map<GraphNode<L>, Set<GraphEdge<L>>> adjacentLists;

    /**
     * Crea un grafo vuoto.
     */
    public MapAdjacentListUndirectedGraph() {
        // Inizializza la mappa con la mappa vuota
        this.adjacentLists = new HashMap<GraphNode<L>, Set<GraphEdge<L>>>();
    }

    @Override
    public int nodeCount() {
        // TODO implementare
        return this.adjacentLists.size();
    }

    @Override
    public int edgeCount() {
        // TODO implementare
        int count = 0;
        for (Map.Entry<GraphNode<L>, Set<GraphEdge<L>>> entry : this.adjacentLists
                .entrySet())
            count += entry.getValue().size();
        return count / 2;
    }

    @Override
    public void clear() {
        this.adjacentLists.clear();
    }

    @Override
    public boolean isDirected() {
        return false;
    }

    @Override
    public Set<GraphNode<L>> getNodes() {
        // TODO implementare
        return this.adjacentLists.keySet();
    }

    @Override
    public boolean addNode(GraphNode<L> node) {
        // TODO implementare
        if (node == null)
            throw new NullPointerException("Inserimento di nodo null");
        if (this.adjacentLists.keySet().contains(node))
            return false;
        this.adjacentLists.put(node, new HashSet<GraphEdge<L>>());
        return true;
    }

    @Override
    public boolean removeNode(GraphNode<L> node) {
        if (node == null)
            throw new NullPointerException(
                    "Tentativo di rimuovere un nodo null");
        throw new UnsupportedOperationException(
                "Rimozione dei nodi non supportata");
    }

    @Override
    public boolean containsNode(GraphNode<L> node) {
        // TODO implementare
        if (node == null)
            throw new NullPointerException("Ricerca di nodo nullo");

        return this.adjacentLists.keySet().contains(node);
    }

    @Override
    public GraphNode<L> getNodeOf(L label) {
        // TODO implementare
        if (label == null)
            throw new NullPointerException(
                    "Tentativo di ricercare un nodo con etichetta null");
        for (GraphNode<L> n : this.adjacentLists.keySet())
            if (n.getLabel().equals(label))
                return n;
        return null;
    }

    @Override
    public int getNodeIndexOf(L label) {
        if (label == null)
            throw new NullPointerException(
                    "Tentativo di ricercare un nodo con etichetta null");
        throw new UnsupportedOperationException(
                "Ricerca dei nodi con indice non supportata");
    }

    @Override
    public GraphNode<L> getNodeAtIndex(int i) {
        throw new UnsupportedOperationException(
                "Ricerca dei nodi con indice non supportata");
    }

    @Override
    public Set<GraphNode<L>> getAdjacentNodesOf(GraphNode<L> node) {
        // TODO implementare
        if (node == null)
            throw new NullPointerException(
                    "Ricerca di nodi adiacenti a un nodo nullo");
        if (!this.containsNode(node))
            throw new IllegalArgumentException(
                    "Ricerca di nodi adiacenti a un nodo non esistente");
        Set<GraphNode<L>> r = new HashSet<GraphNode<L>>();
        for (GraphEdge<L> e : this.adjacentLists.get(node)) {
            if (!e.getNode1().equals(node))
                r.add(e.getNode1());
            if (!e.getNode2().equals(node))
                r.add(e.getNode2());
        }
        return r;
    }

    @Override
    public Set<GraphNode<L>> getPredecessorNodesOf(GraphNode<L> node) {
        // TODO implementare
        throw new UnsupportedOperationException(
                "Ricerca dei nodi predecessori non supportata in un grafo non orientato");
    }

    @Override
    public Set<GraphEdge<L>> getEdges() {
        // TODO implementare
        Set<GraphEdge<L>> r = new HashSet<GraphEdge<L>>();
        for (Map.Entry<GraphNode<L>, Set<GraphEdge<L>>> entry : this.adjacentLists
                .entrySet())
            r.addAll(entry.getValue());
        return r;
    }

    @Override
    public boolean addEdge(GraphEdge<L> edge) {
        // TODO implementare
        if (edge == null)
            throw new NullPointerException("Inserimento di arco nullo");
        if (edge.isDirected())
            throw new IllegalArgumentException(
                    "Inserimento di un arco orientato in un grafo non orientato");
        if (!this.containsNode(edge.getNode1())
                || !this.containsNode(edge.getNode2()))
            throw new IllegalArgumentException(
                    "Inserimento di un arco in cui almeno uno dei due nodi "
                            + "collegati non esiste in questo grafo");
        if (this.containsEdge(edge))
            return false;
        this.adjacentLists.get(edge.getNode1()).add(edge);
        this.adjacentLists.get(edge.getNode2()).add(edge);
        return true;
    }

    @Override
    public boolean removeEdge(GraphEdge<L> edge) {
        throw new UnsupportedOperationException(
                "Rimozione degli archi non supportata");
    }

    @Override
    public boolean containsEdge(GraphEdge<L> edge) {
        // TODO implementare
        if (edge == null)
            throw new NullPointerException("Ricerca di arco nullo");
        if (!this.containsNode(edge.getNode1())
                || !this.containsNode(edge.getNode2()))
            throw new IllegalArgumentException(
                    "Ricerca di un arco in cui almeno uno dei due nodi "
                            + "collegati non esiste in questo grafo");
        return this.adjacentLists.get(edge.getNode1()).contains(edge);
    }

    @Override
    public Set<GraphEdge<L>> getEdgesOf(GraphNode<L> node) {
        // TODO implementare
        if (node == null)
            throw new NullPointerException(
                    "Ricerca degli archi di un nodo nullo");
        if (!this.containsNode(node))
            throw new IllegalArgumentException(
                    "Ricerca degli archi di un nodo non esistente");
        return this.adjacentLists.get(node);
    }

    @Override
    public Set<GraphEdge<L>> getIngoingEdgesOf(GraphNode<L> node) {
        throw new UnsupportedOperationException(
                "Archi entranti non significativi in un grafo non orientato");
    }

}
