/**
 *
 */
package it.unicam.cs.asdl2324.es11;

import java.util.Map;
import java.util.HashMap;
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
 * <p>
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
 * <p>
 * Questa classe non supporta le operazioni di rimozione di nodi e archi e le
 * operazioni indicizzate di ricerca di nodi e archi.
 *
 * @param <L> etichette dei nodi del grafo
 * @author Template: Luca Tesei, Implementazione: collettiva
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
        return adjacentLists.size();
    }

    @Override
    public int edgeCount() {
        // TODO implementare
        int count = 0;
        for (Set<GraphEdge<L>> edges : adjacentLists.values()) {
            count += edges.size();
        }
        // In un grafo non orientato, ogni arco viene contato due volte, quindi la divisione per 2
        return count / 2;
    }

    @Override
    public void clear() {
        this.adjacentLists.clear();
    }

    @Override
    public boolean isDirected() {
        // Questa classe implementa grafi non orientati
        return false;
    }

    @Override
    public Set<GraphNode<L>> getNodes() {
        // TODO implementare
        return adjacentLists.keySet();
    }

    @Override
    public boolean addNode(GraphNode<L> node) {
        // TODO implementare
        if (node == null) {
            throw new NullPointerException("Il nodo non può essere nullo");
        }

        // Verifica se il nodo è già presente nella struttura dati del grafo
        if (!adjacentLists.containsKey(node)) {
            // Se il nodo non è presente, aggiungilo alla struttura dati
            adjacentLists.put(node, null);
            return true; // Nodo aggiunto con successo
        }
        return false; // Il nodo è già presente nel grafo
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
        if (node == null) {
            throw new NullPointerException("Il nodo non può essere nullo");
        }
        return adjacentLists.containsKey(node);
    }

    @Override
    public GraphNode<L> getNodeOf(L label) {
        // TODO implementare
        if (label == null) {
            throw new NullPointerException("Etichetta null non consentita");
        }
        for (GraphNode<L> node : adjacentLists.keySet()) {
            if (node.getLabel().equals(label)) {
                return node;
            }
        }
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
        if (node == null) {
            throw new NullPointerException("Il nodo non può essere nullo");
        }

        if (!containsNode(node)) {
            throw new IllegalArgumentException("Il nodo non è presente nel grafo");
        }

        HashMap<GraphNode<L>, Boolean> adjacentNodes = new HashMap<>();
        Set<GraphEdge<L>> edges = adjacentLists.get(node);
        if (edges != null) {
            for (GraphEdge<L> edge : edges) {
                if (edge.getNode1().equals(node)) {
                    adjacentNodes.put(edge.getNode2(), true);
                } else {
                    adjacentNodes.put(edge.getNode1(), true);
                }
            }
        }

        return adjacentNodes.keySet();
    }

    @Override
    public Set<GraphNode<L>> getPredecessorNodesOf(GraphNode<L> node) {
        // TODO implementare
        HashMap<GraphNode<L>, Boolean> predecessorNodes = new HashMap<>();
        for (GraphNode<L> adjNode : adjacentLists.keySet()) {
            if (!adjNode.equals(node)) {
                for (GraphEdge<L> edge : adjacentLists.get(adjNode)) {
                    if (edge.getNode2().equals(node)) {
                        predecessorNodes.put(adjNode, true);
                        break;
                    }
                }
            }
        }
        return predecessorNodes.keySet();
    }

    @Override
    public Set<GraphEdge<L>> getEdges() {
        // TODO implementare
        Map<GraphEdge<L>, Boolean> edgesMap = new HashMap<>();
        for (Set<GraphEdge<L>> edgeSet : adjacentLists.values()) {
            if (edgeSet != null) {
                for (GraphEdge<L> edge : edgeSet) {
                    edgesMap.put(edge, true);
                }
            }
        }
        return edgesMap.keySet();
    }

    @Override
    public boolean addEdge(GraphEdge<L> edge) {
        // TODO implementare
        if (edge == null) {
            throw new NullPointerException("L'arco non può essere nullo");
        }

        GraphNode<L> node1 = edge.getNode1();
        GraphNode<L> node2 = edge.getNode2();

        if (!containsNode(node1) || !containsNode(node2)) {
            throw new IllegalArgumentException("I nodi dell'arco non sono presenti nel grafo");
        }
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
        if (edge == null) {
            throw new NullPointerException("L'arco non può essere nullo");
        }

        GraphNode<L> node1 = edge.getNode1();
        GraphNode<L> node2 = edge.getNode2();

        // Controlla se entrambi i nodi esistono nel grafo
        if (!adjacentLists.containsKey(node1) || !adjacentLists.containsKey(node2)) {
            throw new IllegalArgumentException("I nodi dell'arco non sono presenti nel grafo");
        }

        // Verifica se gli insiemi di archi sono nulli
        Set<GraphEdge<L>> edgeSet1 = adjacentLists.get(node1);
        Set<GraphEdge<L>> edgeSet2 = adjacentLists.get(node2);

        if (edgeSet1 == null || edgeSet2 == null) {
            return false;
        }

        // Verifica se l'arco è presente nei set di archi dei nodi
        return edgeSet1.contains(edge) && edgeSet2.contains(edge);
    }

    @Override
    public Set<GraphEdge<L>> getEdgesOf(GraphNode<L> node) {
        // TODO implementare
        if (node == null) {
            throw new NullPointerException("Il nodo non può essere nullo");
        }
        if (!adjacentLists.containsKey(node)) {
            throw new IllegalArgumentException("Il nodo non esiste nel grafo");
        }
        return adjacentLists.get(node);
    }

    @Override
    public Set<GraphEdge<L>> getIngoingEdgesOf(GraphNode<L> node) {
        throw new UnsupportedOperationException(
                "Archi entranti non significativi in un grafo non orientato");
    }

}
