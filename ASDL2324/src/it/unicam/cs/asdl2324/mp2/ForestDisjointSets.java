package it.unicam.cs.asdl2324.mp2;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
//ATTENZIONE: è vietato includere import a pacchetti che non siano della Java SE

/**
 * Implementazione dell'interfaccia <code>DisjointSets<E></code> tramite una
 * foresta di alberi ognuno dei quali rappresenta un insieme disgiunto. Si
 * vedano le istruzioni o il libro di testo Cormen et al. (terza edizione)
 * Capitolo 21 Sezione 3.
 *
 * @param <E> il tipo degli elementi degli insiemi disgiunti
 * @author Luca Tesei (template) **ALESSANDRO IPPOLITI
 * *         alessand.ippoliti@studenti.unicam.it** (implementazione)
 */
public class ForestDisjointSets<E> implements DisjointSets<E> {

    /*
     * Mappa che associa ad ogni elemento inserito il corrispondente nodo di un
     * albero della foresta. La variabile è protected unicamente per permettere
     * i test JUnit.
     */
    protected Map<E, Node<E>> currentElements;

    /*
     * Classe interna statica che rappresenta i nodi degli alberi della foresta.
     * Gli specificatori sono tutti protected unicamente per permettere i test
     * JUnit.
     */
    protected static class Node<E> {
        /*
         * L'elemento associato a questo nodo
         */
        protected E item;

        /*
         * Il parent di questo nodo nell'albero corrispondente. Nel caso in cui
         * il nodo sia la radice allora questo puntatore punta al nodo stesso.
         */
        protected Node<E> parent;

        /*
         * Il rango del nodo definito come limite superiore all'altezza del
         * (sotto)albero di cui questo nodo è radice.
         */
        protected int rank;

        /**
         * Costruisce un nodo radice con parent che punta a se stesso e rango
         * zero.
         *
         * @param item l'elemento conservato in questo nodo
         */
        public Node(E item) {
            this.item = item;
            this.parent = this;
            this.rank = 0;
        }

    }

    /**
     * Costruisce una foresta vuota di insiemi disgiunti rappresentati da
     * alberi.
     */
    public ForestDisjointSets() {
        this.currentElements = new HashMap<>();
    }

    @Override
    public boolean isPresent(E e) {
        if (e == null)
            throw new NullPointerException("L'elemento è nullo");

        return currentElements.containsKey(e);
    }

    /*
     * Crea un albero della foresta consistente di un solo nodo di rango zero il
     * cui parent è se stesso.
     */
    @Override
    public void makeSet(E e) {
        if (e == null)
            throw new NullPointerException("L'elemento è nullo");

        if (currentElements.containsKey(e)) {
            throw new IllegalArgumentException("L'elemento è già presente nel set");
        } else {
            Node<E> newNode = new Node<>(e);
            currentElements.put(e, newNode);
        }

    }

    /*
     * L'implementazione del find-set deve realizzare l'euristica
     * "compressione del cammino". Si vedano le istruzioni o il libro di testo
     * Cormen et al. (terza edizione) Capitolo 21 Sezione 3.
     */
    @Override
    public E findSet(E e) {
        if (e == null)
            throw new NullPointerException("L'elemento è nullo");

        if (!currentElements.containsKey(e))
            return null;

        Node<E> node = currentElements.get(e);

        if (node != node.parent) {
            E representative = findSet(node.parent.item);
            node.parent = currentElements.get(representative);
        }

        return node.parent.item;
    }

    /*
     * L'implementazione dell'unione deve realizzare l'euristica
     * "unione per rango". Si vedano le istruzioni o il libro di testo Cormen et
     * al. (terza edizione) Capitolo 21 Sezione 3. In particolare, il
     * rappresentante dell'unione dovrà essere il rappresentante dell'insieme il
     * cui corrispondente albero ha radice con rango più alto. Nel caso in cui
     * il rango della radice dell'albero di cui fa parte e1 sia uguale al rango
     * della radice dell'albero di cui fa parte e2 il rappresentante dell'unione
     * sarà il rappresentante dell'insieme di cui fa parte e2.
     */
    @Override
    public void union(E e1, E e2) {
        if (e1 == null || e2 == null)
            throw new NullPointerException("Uno degli elementi è nullo");

        if (!currentElements.containsKey(e1) || !currentElements.containsKey(e2))
            throw new IllegalArgumentException("Uno degli elementi non esiste nell'insieme");

        E set1 = findSet(e1);
        E set2 = findSet(e2);
        Node<E> node1 = currentElements.get(set1);
        Node<E> node2 = currentElements.get(set2);

        if (node1.rank > node2.rank) {
            node2.parent = node1;
        } else {
            node1.parent = node2;
            if (node1.rank == node2.rank)
                node2.rank++;
        }
    }

    @Override
    public Set<E> getCurrentRepresentatives() {
        Set<E> representatives = new HashSet<>();
        for (Node<E> node : currentElements.values()) {
            representatives.add(findSet(node.item));
        }
        return representatives;
    }

    @Override
    public Set<E> getCurrentElementsOfSetContaining(E e) {
        if (e == null)
            throw new NullPointerException("L'elemento passato è nullo");

        if (!currentElements.containsKey(e))
            throw new IllegalArgumentException("L'elemento non è presente nell'insieme disgiunto");

        Set<E> elementsOfSet = new HashSet<>();
        if (!isPresent(e))
            return elementsOfSet;

        E representative = findSet(e);

        for (Node<E> node : currentElements.values()) {
            E currentRepresentative = findSet(node.item);
            if (currentRepresentative.equals(representative)) {
                elementsOfSet.add(node.item);
            }
        }

        return elementsOfSet;
    }

    @Override
    public void clear() {
        currentElements.clear();
    }

}
