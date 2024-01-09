/**
 *
 */
package it.unicam.cs.asdl2324.es9;

import java.util.List;

// TODO completare import 

/**
 * Classe che implementa un algoritmo di ordinamento basato su heap.
 *
 * @author Template: Luca Tesei, Implementation: collettiva
 *
 */
public class HeapSort<E extends Comparable<E>> implements SortingAlgorithm<E> {

    @Override
    public SortingAlgorithmResult<E> sort(List<E> l) {
        // TODO implementare - Nota: usare una variante dei metodi della classe
        // MaxHeap in modo da implementare l'algoritmo utilizzando solo un array
        // (arraylist) e alcune variabili locali di appoggio (implementazione
        // cosiddetta "in loco" o "in place", si veda
        // https://it.wikipedia.org/wiki/Algoritmo_in_loco)
        if (l == null) {
            throw new NullPointerException("La lista non può essere nulla.");
        }

        if (l.isEmpty()) {
            return new SortingAlgorithmResult<>(l, 0); // Ritorna la lista invariata se è vuota
        }

        MaxHeap<E> maxHeap = new MaxHeap<>(l); // Costruisci uno heap a partire dalla lista

        // Estrai ripetutamente l'elemento massimo dallo heap e posizionalo nella parte ordinata
        for (int i = l.size() - 1; i >= 0; i--) {
            E max = maxHeap.extractMax();
            l.set(i, max);
        }

        return new SortingAlgorithmResult<>(l, 0);
    }

    @Override
    public String getName() {
        return "HeapSort";
    }

}
