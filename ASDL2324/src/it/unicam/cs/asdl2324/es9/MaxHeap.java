package it.unicam.cs.asdl2324.es9;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe che implementa uno heap binario che può contenere elementi non nulli
 * possibilmente ripetuti.
 *
 * @param <E> il tipo degli elementi dello heap, che devono avere un
 *            ordinamento naturale.
 * @author Template: Luca Tesei, Implementation: collettiva
 */
public class MaxHeap<E extends Comparable<E>> {

    /*
     * L'array che serve come base per lo heap
     */
    private ArrayList<E> heap;

    /**
     * Costruisce uno heap vuoto.
     */
    public MaxHeap() {
        this.heap = new ArrayList<E>();
    }

    /**
     * Restituisce il numero di elementi nello heap.
     *
     * @return il numero di elementi nello heap
     */
    public int size() {
        return this.heap.size();
    }

    /**
     * Determina se lo heap è vuoto.
     *
     * @return true se lo heap è vuoto.
     */
    public boolean isEmpty() {
        return this.heap.isEmpty();
    }

    /**
     * Costruisce uno heap a partire da una lista di elementi.
     *
     * @param list lista di elementi
     * @throws NullPointerException se la lista è nulla
     */
    public MaxHeap(List<E> list) {
        // TODO implementare
        if (list == null) {
            throw new NullPointerException("La lista non può essere nulla.");
        }

        // Inizializzazione dell'heap come una nuova ArrayList usando la lista fornita
        this.heap = new ArrayList<>(list);

        // Inizio a partire dalla metà dell'array e vado fino all'inizio, chiamando heapify su ciascun nodo
        for (int i = (size() / 2) - 1; i >= 0; i--) {
            heapify(i);
        }
    }

    /**
     * Inserisce un elemento nello heap
     *
     * @param el l'elemento da inserire
     * @throws NullPointerException se l'elemento è null
     */
    public void insert(E el) {
        // TODO implementare
        if (el == null) {
            throw new NullPointerException("L'elemento non può essere null.");
        }

        // Aggiunge l'elemento alla fine dell'heap
        heap.add(el);

        // Ottiene l'indice dell'elemento appena inserito
        int currentIndex = size() - 1;

        // Trova l'indice del genitore dell'elemento appena inserito
        int parentIndex = (currentIndex - 1) / 2;

        // Continua a riorganizzare l'heap finché non viene ripristinata la correttezza delle proprietà dello heap
        while (currentIndex > 0 && heap.get(currentIndex).compareTo(heap.get(parentIndex)) > 0) {
            // Scambia l'elemento corrente con il suo genitore
            E temp = heap.get(currentIndex);
            heap.set(currentIndex, heap.get(parentIndex));
            heap.set(parentIndex, temp);

            // Aggiorna gli indici per continuare la verifica con il genitore successivo
            currentIndex = parentIndex;
            parentIndex = (currentIndex - 1) / 2;
        }
    }

    /*
     * Funzione di comodo per calcolare l'indice del figlio sinistro del nodo in
     * posizione i. Si noti che la posizione 0 è significativa e contiene sempre
     * la radice dello heap.
     */
    private int leftIndex(int i) {
        // TODO implementare
        return 2 * i + 1;
    }

    /*
     * Funzione di comodo per calcolare l'indice del figlio destro del nodo in
     * posizione i. Si noti che la posizione 0 è significativa e contiene sempre
     * la radice dello heap.
     */
    private int rightIndex(int i) {
        // TODO implementare
        return 2 * i + 2;
    }

    /*
     * Funzione di comodo per calcolare l'indice del genitore del nodo in
     * posizione i. Si noti che la posizione 0 è significativa e contiene sempre
     * la radice dello heap.
     */
    private int parentIndex(int i) {
        // TODO implementare
        return (i - 1) / 2;
    }

    /**
     * Ritorna l'elemento massimo senza toglierlo.
     *
     * @return l'elemento massimo dello heap oppure null se lo heap è vuoto
     */
    public E getMax() {
        // TODO implementare
        if (isEmpty()) {
            return null; // Restituisce null se l'heap è vuoto
        }
        return heap.get(0);
    }

    /**
     * Estrae l'elemento massimo dallo heap. Dopo la chiamata tale elemento non
     * è più presente nello heap.
     *
     * @return l'elemento massimo di questo heap oppure null se lo heap è vuoto
     */
    public E extractMax() {
        // TODO implementare
        if (isEmpty()) {
            return null; // Restituisce null se l'heap è vuoto
        }

        // Ottieni l'elemento massimo (radice dello heap)
        E max = heap.get(0);

        // Sostituisci la radice con l'ultimo elemento dell'heap
        heap.set(0, heap.get(size() - 1));
        heap.remove(size() - 1); // Rimuovi l'ultimo elemento

        // Ripristina le proprietà dello heap dopo la rimozione
        heapify(0);

        return max;
    }

    /*
     * Ricostituisce uno heap a partire dal nodo in posizione i assumendo che i
     * suoi sottoalberi sinistro e destro (se esistono) siano heap.
     */
    private void heapify(int i) {
        // TODO implementare
        int largest = i;
        int leftChild = 2 * i + 1;
        int rightChild = 2 * i + 2;

        // Trova l'indice del nodo più grande tra il nodo corrente, il figlio sinistro e il figlio destro
        if (leftChild < size() && heap.get(leftChild).compareTo(heap.get(largest)) > 0) {
            largest = leftChild;
        }

        if (rightChild < size() && heap.get(rightChild).compareTo(heap.get(largest)) > 0) {
            largest = rightChild;
        }

        // Se il nodo più grande non è il nodo corrente, effettua lo scambio e chiamata ricorsiva a heapify
        if (largest != i) {
            E temp = heap.get(i);
            heap.set(i, heap.get(largest));
            heap.set(largest, temp);
            heapify(largest);
        }
    }

    /**
     * Only for JUnit testing purposes.
     *
     * @return the arraylist representing this max heap
     */
    protected ArrayList<E> getHeap() {
        return this.heap;
    }
}
