package it.unicam.cs.asdl2324.es7;

import java.util.List;

// TODO completare import

/**
 * Implementazione dell'algoritmo di Insertion Sort integrata nel framework di
 * valutazione numerica. L'implementazione è in loco.
 * 
 * @author Template: Luca Tesei, Implementazione: Collettiva
 *
 * @param <E>
 *                Una classe su cui sia definito un ordinamento naturale.
 */
public class InsertionSort<E extends Comparable<E>>
        implements SortingAlgorithm<E> {

    public SortingAlgorithmResult<E> sort(List<E> l) {
        // TODO implementare
        if (l == null)
            throw new NullPointerException("Tentativo di ordinare una lista null");
        if (l.size() <= 1)
            return new SortingAlgorithmResult<>(l, 0);

        int countCompare = insertionSort(l);

        return new SortingAlgorithmResult<>(l, countCompare);
    }

    private int insertionSort(List<E> l) {
        int n = l.size();
        int countCompare = 0;

        for (int i = 1; i < n; i++) {
            E current = l.get(i);
            int j = i - 1;

            // Sposta gli elementi maggiori di current a destra
            while (j >= 0 && l.get(j).compareTo(current) > 0) {
                l.set(j + 1, l.get(j));
                j--;
                countCompare++;
            }

            // Inserisci current nella posizione corretta
            l.set(j + 1, current);
        }

        return countCompare;
    }

    public String getName() {
        return "InsertionSort";
    }
}
