/**
 *
 */
package it.unicam.cs.asdl2324.es7;

import java.util.List;

// TODO completare import

/**
 * Implementazione del QuickSort con scelta della posizione del pivot fissa.
 * L'implementazione è in loco.
 *
 * @author Template: Luca Tesei, Implementazione: collettiva
 * @param <E>
 *                il tipo degli elementi della sequenza da ordinare.
 *
 */
public class QuickSort<E extends Comparable<E>> implements SortingAlgorithm<E> {

    @Override
    public SortingAlgorithmResult<E> sort(List<E> l) {
        // TODO implementare
        if (l == null)
            throw new NullPointerException("Tentativo di ordinare una lista null");
        if (l.size() <= 1)
            return new SortingAlgorithmResult<>(l, 0);

        int countCompare = quickSort(l, 0, l.size() - 1);

        return new SortingAlgorithmResult<>(l, countCompare);
    }

    private int quickSort(List<E> l, int low, int high) {
        int countCompare = 0;
        if (low < high) {
            // Trova la posizione del pivot
            int pivotIndex = partition(l, low, high);

            // Ricorsivamente ordina le due parti
            countCompare += quickSort(l, low, pivotIndex - 1);
            countCompare += quickSort(l, pivotIndex + 1, high);
        }
        return countCompare;
    }

    private int partition(List<E> l, int low, int high) {
        int countCompare = 0;

        // Scegli il pivot come elemento medio
        int middle = low + (high - low) / 2;
        E pivot = l.get(middle);

        // Metti il pivot all'inizio
        swap(l, middle, low);

        int i = low + 1;
        int j = high;

        while (i <= j) {
            // Trova un elemento più grande del pivot
            while (i <= j && l.get(i).compareTo(pivot) <= 0) {
                i++;
                countCompare++;
            }
            // Trova un elemento più piccolo del pivot
            while (i <= j && l.get(j).compareTo(pivot) > 0) {
                j--;
                countCompare++;
            }
            // Scambia gli elementi trovati
            if (i < j) {
                swap(l, i, j);
            }
        }

        // Ripristina il pivot nella sua posizione finale
        swap(l, low, j);

        return j;
    }

    private void swap(List<E> l, int i, int j) {
        E temp = l.get(i);
        l.set(i, l.get(j));
        l.set(j, temp);
    }

    @Override
    public String getName() {
        return "QuickSort";
    }

}
