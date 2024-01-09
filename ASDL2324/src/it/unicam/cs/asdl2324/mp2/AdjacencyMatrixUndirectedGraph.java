/**
 *
 */
package it.unicam.cs.asdl2324.mp2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Collections;
import java.util.HashSet;
// ATTENZIONE: è vietato includere import a pacchetti che non siano della Java SE

/**
 * Classe che implementa un grafo non orientato tramite matrice di adiacenza.
 * Non sono accettate etichette dei nodi null e non sono accettate etichette
 * duplicate nei nodi (che in quel caso sono lo stesso nodo).
 * <p>
 * I nodi sono indicizzati da 0 a nodeCoount() - 1 seguendo l'ordine del loro
 * inserimento (0 è l'indice del primo nodo inserito, 1 del secondo e così via)
 * e quindi in ogni istante la matrice di adiacenza ha dimensione nodeCount() *
 * nodeCount(). La matrice, sempre quadrata, deve quindi aumentare di dimensione
 * ad ogni inserimento di un nodo. Per questo non è rappresentata tramite array
 * ma tramite ArrayList.
 * <p>
 * Gli oggetti GraphNode<L>, cioè i nodi, sono memorizzati in una mappa che
 * associa ad ogni nodo l'indice assegnato in fase di inserimento. Il dominio
 * della mappa rappresenta quindi l'insieme dei nodi.
 * <p>
 * Gli archi sono memorizzati nella matrice di adiacenza. A differenza della
 * rappresentazione standard con matrice di adiacenza, la posizione i,j della
 * matrice non contiene un flag di presenza, ma è null se i nodi i e j non sono
 * collegati da un arco e contiene un oggetto della classe GraphEdge<L> se lo
 * sono. Tale oggetto rappresenta l'arco. Un oggetto uguale (secondo equals) e
 * con lo stesso peso (se gli archi sono pesati) deve essere presente nella
 * posizione j, i della matrice.
 * <p>
 * Questa classe non supporta i metodi di cancellazione di nodi e archi, ma
 * supporta tutti i metodi che usano indici, utilizzando l'indice assegnato a
 * ogni nodo in fase di inserimento.
 *
 * @author Luca Tesei (template) **ALESSANDRO IPPOLITI
 * alessand.ippoliti@studenti.unicam.it** (implementazione)
 */
public class AdjacencyMatrixUndirectedGraph<L> extends Graph<L> {
    /*
     * Le seguenti variabili istanza sono protected al solo scopo di agevolare
     * il JUnit testing
     */

    /*
     * Insieme dei nodi e associazione di ogni nodo con il proprio indice nella
     * matrice di adiacenza
     */
    protected Map<GraphNode<L>, Integer> nodesIndex;

    /*
     * Matrice di adiacenza, gli elementi sono null o oggetti della classe
     * GraphEdge<L>. L'uso di ArrayList permette alla matrice di aumentare di
     * dimensione gradualmente ad ogni inserimento di un nuovo nodo e di
     * ridimensionarsi se un nodo viene cancellato.
     */
    protected ArrayList<ArrayList<GraphEdge<L>>> matrix;

    /**
     * Crea un grafo vuoto.
     */
    public AdjacencyMatrixUndirectedGraph() {
        this.matrix = new ArrayList<ArrayList<GraphEdge<L>>>();
        this.nodesIndex = new HashMap<GraphNode<L>, Integer>();
    }

    @Override
    public int nodeCount() {
        return matrix.size();
    }

    @Override
    public int edgeCount() {
        int count = 0;

        for (int i = 0; i < matrix.size(); i++) {
            for (int j = 0; j < matrix.get(i).size(); j++) {
                if (matrix.get(i).get(j) != null) {
                    count++;
                }
            }
        }
        return count / 2;
    }

    @Override
    public void clear() {
        matrix.clear();
        nodesIndex.clear();
    }

    @Override
    public boolean isDirected() {
        return false;
    }

    /*
     * Gli indici dei nodi vanno assegnati nell'ordine di inserimento a partire
     * da zero
     */
    @Override
    public boolean addNode(GraphNode<L> node) {
        if (node == null)
            throw new NullPointerException("Il nodo è nullo");

        int index = nodesIndex.size();
        nodesIndex.put(node, index);
        for (ArrayList<GraphEdge<L>> row : matrix) {
            row.add(null);
        }
        matrix.add(new ArrayList<>(Collections.nCopies(index + 1, null)));

        return true;
    }

    /*
     * Gli indici dei nodi vanno assegnati nell'ordine di inserimento a partire
     * da zero
     */
    @Override
    public boolean addNode(L label) {
        if (label == null)
            throw new NullPointerException("L'etichetta è nulla");

        if (getNode(label) != null)
            return false;

        GraphNode<L> newNode = new GraphNode<>(label);
        return addNode(newNode);
    }

    /*
     * Gli indici dei nodi il cui valore sia maggiore dell'indice del nodo da
     * cancellare devono essere decrementati di uno dopo la cancellazione del
     * nodo
     */
    @Override
    public void removeNode(GraphNode<L> node) {
        if (node == null)
            throw new NullPointerException("Il nodo è nullo");

        if (!nodesIndex.containsKey(node))
            throw new IllegalArgumentException("Il nodo non esiste nel grafo");

        int indexToRemove = nodesIndex.get(node);
        nodesIndex.remove(node);

        for (Map.Entry<GraphNode<L>, Integer> entry : nodesIndex.entrySet()) {
            if (entry.getValue() > indexToRemove)
                entry.setValue(entry.getValue() - 1);
        }

        matrix.remove(indexToRemove);
        for (ArrayList<GraphEdge<L>> row : matrix)
            row.remove(indexToRemove);
    }

    /*
     * Gli indici dei nodi il cui valore sia maggiore dell'indice del nodo da
     * cancellare devono essere decrementati di uno dopo la cancellazione del
     * nodo
     */
    @Override
    public void removeNode(L label) {
        if (label == null)
            throw new NullPointerException("L'etichetta è nulla");

        GraphNode<L> nodeToRemove = null;
        for (GraphNode<L> node : nodesIndex.keySet()) {
            if (node.getLabel().equals(label)) {
                nodeToRemove = node;
                break;
            }
        }

        if (nodeToRemove == null)
            throw new IllegalArgumentException("Il nodo non esiste nel grafo");

        int nodeIndexToRemove = nodesIndex.get(nodeToRemove);
        nodesIndex.remove(nodeToRemove);

        matrix.remove(nodeIndexToRemove);
        for (ArrayList<GraphEdge<L>> row : matrix)
            row.remove(nodeIndexToRemove);

        for (Map.Entry<GraphNode<L>, Integer> entry : nodesIndex.entrySet()) {
            int currentIndex = entry.getValue();
            if (currentIndex > nodeIndexToRemove) {
                entry.setValue(currentIndex - 1);
            }
        }
    }

    /*
     * Gli indici dei nodi il cui valore sia maggiore dell'indice del nodo da
     * cancellare devono essere decrementati di uno dopo la cancellazione del
     * nodo
     */
    @Override
    public void removeNode(int i) {
        if (i < 0 || i >= nodesIndex.size())
            throw new IndexOutOfBoundsException("Indice non valido");

        GraphNode<L> nodeToRemove = null;
        for (Map.Entry<GraphNode<L>, Integer> entry : nodesIndex.entrySet()) {
            if (entry.getValue() == i) {
                nodeToRemove = entry.getKey();
                break;
            }
        }

        if (nodeToRemove != null)
            removeNode(nodeToRemove);
    }

    @Override
    public GraphNode<L> getNode(GraphNode<L> node) {
        if (node == null)
            throw new NullPointerException("Il nodo è nullo");

        for (GraphNode<L> graphNode : nodesIndex.keySet()) {
            if (graphNode.equals(node))
                return graphNode;
        }

        return null;
    }

    @Override
    public GraphNode<L> getNode(L label) {
        if (label == null)
            throw new NullPointerException("L'etichetta è nulla");

        for (GraphNode<L> node : nodesIndex.keySet()) {
            if (node.getLabel().equals(label))
                return node;
        }

        return null;
    }

    @Override
    public GraphNode<L> getNode(int i) {
        if (i < 0 || i >= nodesIndex.size())
            throw new IndexOutOfBoundsException("Indice non valido");

        for (Map.Entry<GraphNode<L>, Integer> entry : nodesIndex.entrySet()) {
            if (entry.getValue() == i)
                return entry.getKey();
        }

        return null;
    }

    @Override
    public int getNodeIndexOf(GraphNode<L> node) {
        if (node == null)
            throw new NullPointerException("Il nodo è nullo");

        if (!nodesIndex.containsKey(node))
            throw new IllegalArgumentException("Il nodo non esiste nel grafo");

        return nodesIndex.get(node);
    }

    @Override
    public int getNodeIndexOf(L label) {
        if (label == null)
            throw new NullPointerException("L'etichetta è nulla");

        for (Map.Entry<GraphNode<L>, Integer> entry : nodesIndex.entrySet()) {
            if (entry.getKey().getLabel().equals(label))
                return entry.getValue();
        }

        throw new IllegalArgumentException("Nodo con l'etichetta specificata non trovato");
    }

    @Override
    public Set<GraphNode<L>> getNodes() {
        return nodesIndex.keySet();
    }

    @Override
    public boolean addEdge(GraphEdge<L> edge) {
        if (edge == null)
            throw new NullPointerException("L'arco è nullo");

        if (edge.isDirected())
            throw new IllegalArgumentException("L'arco è orientato");

        GraphNode<L> source = edge.getNode1();
        GraphNode<L> destination = edge.getNode2();

        if (source == null || destination == null)
            throw new IllegalArgumentException("Uno dei nodi è nullo");

        if (!nodesIndex.containsKey(source) || !nodesIndex.containsKey(destination))
            throw new IllegalArgumentException("Uno dei nodi non è presente nel grafo");

        if (getEdge(source, destination) != null)
            return false;

        int sourceIndex = nodesIndex.get(source);
        int destinationIndex = nodesIndex.get(destination);

        matrix.get(sourceIndex).set(destinationIndex, edge);
        matrix.get(destinationIndex).set(sourceIndex, edge);

        return true;
    }

    @Override
    public boolean addEdge(GraphNode<L> node1, GraphNode<L> node2) {
        if (node1 == null || node2 == null)
            throw new NullPointerException("Uno dei nodi è nullo");

        if (!nodesIndex.containsKey(node1) || !nodesIndex.containsKey(node2))
            throw new NullPointerException("Uno dei nodi non è presente nel grafo");

        int index1 = nodesIndex.get(node1);
        int index2 = nodesIndex.get(node2);

        matrix.get(index1).set(index2, new GraphEdge<>(node1, node2, false));
        matrix.get(index2).set(index1, new GraphEdge<>(node2, node1, false));

        return true;
    }

    @Override
    public boolean addWeightedEdge(GraphNode<L> node1, GraphNode<L> node2,
                                   double weight) {
        if (node1 == null || node2 == null)
            return false;

        if (!nodesIndex.containsKey(node1) || !nodesIndex.containsKey(node2))
            return false;

        int index1 = nodesIndex.get(node1);
        int index2 = nodesIndex.get(node2);

        matrix.get(index1).set(index2, new GraphEdge<>(node1, node2, false, weight));
        matrix.get(index2).set(index1, new GraphEdge<>(node2, node1, false, weight));

        return true;
    }

    @Override
    public boolean addEdge(L label1, L label2) {
        if (label1 == null || label2 == null)
            throw new IllegalArgumentException("Una delle etichette dei nodi è nulla");

        GraphNode<L> node1 = getNode(label1);
        GraphNode<L> node2 = getNode(label2);

        if (node1 == null || node2 == null)
            throw new IllegalArgumentException("Uno dei nodi non esiste nel grafo");

        if (getEdge(node1, node2) != null)
            return false;

        return addEdge(new GraphEdge<>(node1, node2, false));
    }

    @Override
    public boolean addWeightedEdge(L label1, L label2, double weight) {
        GraphNode<L> node1 = getNode(label1);
        GraphNode<L> node2 = getNode(label2);

        if (node1 == null || node2 == null)
            return false;

        int index1 = nodesIndex.get(node1);
        int index2 = nodesIndex.get(node2);

        matrix.get(index1).set(index2, new GraphEdge<>(node1, node2, false, weight));
        matrix.get(index2).set(index1, new GraphEdge<>(node2, node1, false, weight));

        return true;
    }

    @Override
    public boolean addEdge(int i, int j) {
        if (i < 0 || j < 0 || i >= matrix.size() || j >= matrix.size()) {
            return false;
        }

        GraphNode<L> node1 = getNode(i);
        GraphNode<L> node2 = getNode(j);

        if (node1 == null || node2 == null)
            throw new NullPointerException("Uno dei nodi è nullo");

        matrix.get(i).set(j, new GraphEdge<>(node1, node2, false));
        matrix.get(j).set(i, new GraphEdge<>(node2, node1, false));

        return true;
    }

    @Override
    public boolean addWeightedEdge(int i, int j, double weight) {

        GraphNode<L> node1 = getNode(i);
        GraphNode<L> node2 = getNode(j);

        if (node1 == null || node2 == null)
            throw new NullPointerException("Uno dei nodi è nullo");

        matrix.get(i).set(j, new GraphEdge<>(node1, node2, false, weight));
        matrix.get(j).set(i, new GraphEdge<>(node2, node1, false, weight));

        return true;
    }

    @Override
    public void removeEdge(GraphEdge<L> edge) {
        if (edge == null)
            throw new NullPointerException("L'arco è nullo");

        GraphNode<L> source = edge.getNode1();
        GraphNode<L> destination = edge.getNode2();

        if (source == null || destination == null)
            throw new NullPointerException("Uno dei nodi è nullo");

        if (!nodesIndex.containsKey(source) || !nodesIndex.containsKey(destination))
            throw new IllegalArgumentException("Uno dei nodi non è presente nel grafo");

        int sourceIndex = nodesIndex.get(source);
        int destinationIndex = nodesIndex.get(destination);

        matrix.get(sourceIndex).set(destinationIndex, null);
        matrix.get(destinationIndex).set(sourceIndex, null);
    }

    @Override
    public void removeEdge(GraphNode<L> node1, GraphNode<L> node2) {
        if (node1 == null || node2 == null)
            throw new NullPointerException("Uno dei nodi è nullo");

        if (!nodesIndex.containsKey(node1) || !nodesIndex.containsKey(node2))
            throw new IllegalArgumentException("Uno dei nodi non esiste nel grafo");

        int index1 = nodesIndex.get(node1);
        int index2 = nodesIndex.get(node2);

        if (matrix.get(index1).get(index2) == null || matrix.get(index2).get(index1) == null)
            throw new IllegalArgumentException("L'arco specificato non esiste");

        matrix.get(index1).set(index2, null);
        matrix.get(index2).set(index1, null);
    }

    @Override
    public void removeEdge(L label1, L label2) {
        GraphNode<L> node1 = getNode(label1);
        GraphNode<L> node2 = getNode(label2);

        if (node1 == null || node2 == null)
            throw new NullPointerException("Uno dei nodi è nullo");

        int index1 = nodesIndex.get(node1);
        int index2 = nodesIndex.get(node2);

        matrix.get(index1).set(index2, null);
        matrix.get(index2).set(index1, null);
    }

    @Override
    public void removeEdge(int i, int j) {
        matrix.get(i).set(j, null);
        matrix.get(j).set(i, null);
    }

    @Override
    public GraphEdge<L> getEdge(GraphEdge<L> edge) {
        if (edge == null)
            throw new NullPointerException("L'arco è nullo");

        GraphNode<L> source = edge.getNode1();
        GraphNode<L> destination = edge.getNode2();

        if (source == null || destination == null)
            throw new NullPointerException("Uno dei nodi è nullo");

        if (!nodesIndex.containsKey(source) || !nodesIndex.containsKey(destination))
            throw new IllegalArgumentException("Uno dei nodi non esiste nel grafo");

        int sourceIndex = nodesIndex.get(source);
        int destinationIndex = nodesIndex.get(destination);

        return matrix.get(sourceIndex).get(destinationIndex);
    }

    @Override
    public GraphEdge<L> getEdge(GraphNode<L> node1, GraphNode<L> node2) {
        if (node1 == null || node2 == null)
            throw new NullPointerException("Uno dei nodi è nullo");

        if (!nodesIndex.containsKey(node1) || !nodesIndex.containsKey(node2))
            throw new IllegalArgumentException("Uno dei nodi non esiste nel grafo");

        int index1 = nodesIndex.get(node1);
        int index2 = nodesIndex.get(node2);

        return matrix.get(index1).get(index2);
    }

    @Override
    public GraphEdge<L> getEdge(L label1, L label2) {
        GraphNode<L> node1 = getNode(label1);
        GraphNode<L> node2 = getNode(label2);

        if (node1 == null || node2 == null)
            throw new NullPointerException("Uno dei nodi non esiste nel grafo");

        int index1 = nodesIndex.get(node1);
        int index2 = nodesIndex.get(node2);

        return matrix.get(index1).get(index2);
    }

    @Override
    public GraphEdge<L> getEdge(int i, int j) {
        if (i < 0 || j < 0 || i >= matrix.size() || j >= matrix.size())
            throw new IndexOutOfBoundsException("Uno degli indici è fuori dai limiti della matrice");

        return matrix.get(i).get(j);
    }

    @Override
    public Set<GraphNode<L>> getAdjacentNodesOf(GraphNode<L> node) {

        if (node == null)
            throw new NullPointerException("Il nodo è nullo");

        if (!nodesIndex.containsKey(node))
            throw new IllegalArgumentException("Il nodo non esiste");
        Set<GraphNode<L>> adjacentNodes = new HashSet<>();

        int nodeIndex = nodesIndex.get(node);

        for (int i = 0; i < matrix.size(); i++) {
            if (matrix.get(nodeIndex).get(i) != null) {
                adjacentNodes.add(getNode(i));
            }
        }

        return adjacentNodes;
    }

    @Override
    public Set<GraphNode<L>> getAdjacentNodesOf(L label) {
        Set<GraphNode<L>> adjacentNodes = new HashSet<>();

        GraphNode<L> node = getNode(label);

        if (node == null)
            return adjacentNodes;

        int nodeIndex = nodesIndex.get(node);


        for (int i = 0; i < matrix.size(); i++) {
            if (matrix.get(nodeIndex).get(i) != null) {
                adjacentNodes.add(getNode(i));
            }
        }

        return adjacentNodes;
    }

    @Override
    public Set<GraphNode<L>> getAdjacentNodesOf(int i) {
        Set<GraphNode<L>> adjacentNodes = new HashSet<>();

        if (i < 0 || i >= matrix.size())
            throw new IndexOutOfBoundsException("L'indice è fuori dai limiti della matrice");

        for (int j = 0; j < matrix.size(); j++) {
            if (matrix.get(i).get(j) != null)
                adjacentNodes.add(getNode(j));
        }

        return adjacentNodes;
    }

    @Override
    public Set<GraphNode<L>> getPredecessorNodesOf(GraphNode<L> node) {
        throw new UnsupportedOperationException(
                "Operazione non supportata in un grafo non orientato");
    }

    @Override
    public Set<GraphNode<L>> getPredecessorNodesOf(L label) {
        throw new UnsupportedOperationException(
                "Operazione non supportata in un grafo non orientato");
    }

    @Override
    public Set<GraphNode<L>> getPredecessorNodesOf(int i) {
        throw new UnsupportedOperationException(
                "Operazione non supportata in un grafo non orientato");
    }

    @Override
    public Set<GraphEdge<L>> getEdgesOf(GraphNode<L> node) {
        if (node == null)
            throw new NullPointerException("Il nodo è nullo");

        if (!nodesIndex.containsKey(node))
            throw new IllegalArgumentException("Il nodo non esiste nel grafo");

        Set<GraphEdge<L>> incidentEdges = new HashSet<>();
        int nodeIndex = nodesIndex.get(node);

        for (int i = 0; i < matrix.size(); i++) {
            GraphEdge<L> edgeRow = matrix.get(nodeIndex).get(i);
            GraphEdge<L> edgeColumn = matrix.get(i).get(nodeIndex);

            if (edgeRow != null)
                incidentEdges.add(edgeRow);

            if (edgeColumn != null)
                incidentEdges.add(edgeColumn);

        }

        return incidentEdges;
    }

    @Override
    public Set<GraphEdge<L>> getEdgesOf(L label) {
        Set<GraphEdge<L>> incidentEdges = new HashSet<>();

        GraphNode<L> node = getNode(label);

        if (node == null)
            return incidentEdges;

        int nodeIndex = nodesIndex.get(node);

        for (int i = 0; i < matrix.size(); i++) {
            GraphEdge<L> edge = matrix.get(nodeIndex).get(i);
            if (edge != null)
                incidentEdges.add(edge);
        }

        return incidentEdges;
    }

    @Override
    public Set<GraphEdge<L>> getEdgesOf(int i) {
        Set<GraphEdge<L>> incidentEdges = new HashSet<>();

        if (i < 0 || i >= matrix.size())
            return incidentEdges;

        for (int j = 0; j < matrix.size(); j++) {
            GraphEdge<L> edge = matrix.get(i).get(j);
            if (edge != null)
                incidentEdges.add(edge);
        }

        return incidentEdges;
    }

    @Override
    public Set<GraphEdge<L>> getIngoingEdgesOf(GraphNode<L> node) {
        throw new UnsupportedOperationException(
                "Operazione non supportata in un grafo non orientato");
    }

    @Override
    public Set<GraphEdge<L>> getIngoingEdgesOf(L label) {
        throw new UnsupportedOperationException(
                "Operazione non supportata in un grafo non orientato");
    }

    @Override
    public Set<GraphEdge<L>> getIngoingEdgesOf(int i) {
        throw new UnsupportedOperationException(
                "Operazione non supportata in un grafo non orientato");
    }

    @Override
    public Set<GraphEdge<L>> getEdges() {
        Set<GraphEdge<L>> allEdges = new HashSet<>();

        for (ArrayList<GraphEdge<L>> row : matrix) {
            for (GraphEdge<L> edge : row) {
                if (edge != null)
                    allEdges.add(edge);
            }
        }

        return allEdges;
    }
}
