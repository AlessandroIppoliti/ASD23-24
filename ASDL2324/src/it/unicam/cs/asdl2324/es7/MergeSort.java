/**
 * 
 */
package it.unicam.cs.asdl2324.es7;

import java.util.List;

// TODO completare import
import java.util.ArrayList;
/**
 * Implementazione dell'algoritmo di Merge Sort integrata nel framework di
 * valutazione numerica. Non è richiesta l'implementazione in loco.
 * 
 * @author Template: Luca Tesei, Implementazione: collettiva
 *
 */
public class MergeSort<E extends Comparable<E>> implements SortingAlgorithm<E> {

    public SortingAlgorithmResult<E> sort(List<E> l) {
        // TODO implementare
        if (l == null)
            throw new NullPointerException("Tentativo di ordinare una lista null");
        if (l.size() <= 1)
            return new SortingAlgorithmResult<>(l, 0);

        List<E> sortedList = mergeSort(l);

        return new SortingAlgorithmResult<>(sortedList, 0);
    }

    private List<E> mergeSort(List<E> l) {
        int n = l.size();
        if (n <= 1)
            return l;

        // Trova il punto medio della lista
        int mid = n / 2;

        // Dividi la lista in due parti
        List<E> left = l.subList(0, mid);
        List<E> right = l.subList(mid, n);

        // Ricorsivamente esegui il merge sort sulle due parti
        left = mergeSort(left);
        right = mergeSort(right);

        // Fonde le due parti ordinate
        return merge(left, right);
    }

    private List<E> merge(List<E> left, List<E> right) {
        int leftSize = left.size();
        int rightSize = right.size();
        int i = 0, j = 0, k = 0;

        // Lista temporanea per memorizzare la fusione delle due parti
        List<E> mergedList = new ArrayList<>(leftSize + rightSize);

        // Fusione ordinata delle due parti
        while (i < leftSize && j < rightSize) {
            if (left.get(i).compareTo(right.get(j)) <= 0) {
                mergedList.add(left.get(i));
                i++;
            } else {
                mergedList.add(right.get(j));
                j++;
            }
            k++;
        }

        // Aggiungi gli elementi rimanenti dalla parte sinistra
        while (i < leftSize) {
            mergedList.add(left.get(i));
            i++;
            k++;
        }

        // Aggiungi gli elementi rimanenti dalla parte destra
        while (j < rightSize) {
            mergedList.add(right.get(j));
            j++;
            k++;
        }

        return mergedList;
    }

    public String getName() {
        return "MergeSort";
    }
}
