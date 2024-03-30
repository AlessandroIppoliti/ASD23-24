package it.unicam.cs.asdl2324.mp1;

import java.util.*;

//TODO inserire import della Java SE che si ritengono necessari


/**
 * // TODO spiegare come viene implementato il multiset.
 * Il multiset implementato all'interno della classe MyMultiset,
 * utilizza una mappa (elementsMap) per tenere traccia degli elementi e delle loro occorrenze.
 *
 * @param <E> il tipo degli elementi del multiset
 * @author Luca Tesei (template) **
 * *         ** (implementazione)
 */
public class MyMultiset<E> implements Multiset<E> {

    // TODO inserire le variabili istanza private che si ritengono necessarie
    private final Map<E, Integer> elementsMap;
    private int modCount;
    // TODO inserire le classi interne che si ritengono necessarie

    /**
     * Crea un multiset vuoto.
     */
    public MyMultiset() {
        // TODO Implementare
        this.elementsMap = new HashMap<>();
        this.modCount = 0;
    }

    @Override
    public int size() {
        // TODO Implementare
        int totalSize = 0;
        for (int occurrences : elementsMap.values()) {
            totalSize += occurrences;
        }
        return totalSize;
    }

    @Override
    public int count(Object element) {
        // TODO Implementare
        if (element == null) throw new NullPointerException("L'elemento non può essere null");

        if (!elementsMap.containsKey(element)) return 0;

        return elementsMap.get(element);
    }

    @Override
    public int add(E element, int occurrences) {
        // TODO Implementare
        if (occurrences < 0) throw new IllegalArgumentException("Occurrences non può essere negativo");

        int currentOccurrences = elementsMap.getOrDefault(element, 0);
        if (currentOccurrences > Integer.MAX_VALUE - occurrences)
            throw new IllegalArgumentException("L'aggiunta di occorrenze supererebbe Integer.MAX_VALUE");

        if (element == null || occurrences == 0) return count(element);

        elementsMap.put(element, currentOccurrences + occurrences);
        modCount++;
        return currentOccurrences;
    }

    @Override
    public void add(E element) {
        // TODO Implementare
        add(element, 1);
    }

    @Override
    public int remove(Object element, int occurrences) {
        // TODO implementare
        if (element == null) throw new NullPointerException("L'elemento non può essere null");
        if (occurrences < 0) throw new IllegalArgumentException("Occurrences non può essere negativo");

        int currentOccurrences = elementsMap.getOrDefault(element, 0);

        int newCount = Math.max(0, currentOccurrences - occurrences);

        if (currentOccurrences > 0 && occurrences > 0) {
            modCount++;
            elementsMap.put((E) element, newCount);
            return currentOccurrences;
        }

        return currentOccurrences;
    }

    @Override
    public boolean remove(Object element) {
        // TODO implementare
        if (element == null) throw new NullPointerException("L'elemento non può essere null");

        if (!elementsMap.containsKey(element)) return false;

        int currentOccurrences = elementsMap.get(element);

        if (currentOccurrences > 1) elementsMap.put((E) element, currentOccurrences - 1);

        else elementsMap.remove(element);

        modCount++;
        return true;
    }

    @Override
    public int setCount(E element, int count) {
        // TODO implementare
        if (element == null) throw new NullPointerException("L'elemento non può essere null");
        if (count < 0) throw new IllegalArgumentException("Occurrences non può essere negativo");

        int currentOccurrences = elementsMap.getOrDefault(element, 0);

        if (currentOccurrences != count) {
            elementsMap.put(element, count);
            modCount++;
        }

        return currentOccurrences;
    }

    @Override
    public Set<E> elementSet() {
        // TODO implementare
        return new HashSet<>(elementsMap.keySet());
    }

    @Override
    public Iterator<E> iterator() {
        // TODO implementare
        return new Iterator<E>() {
            private final Iterator<Map.Entry<E, Integer>> mapIterator = elementsMap.entrySet().iterator();
            private Map.Entry<E, Integer> currentEntry;
            private int occurrencesLeft;
            private int expectedModCount = modCount; // Counter per le modifiche strutturali

            @Override
            public boolean hasNext() {
                checkForModification(); // Controllo delle modifiche strutturali
                return occurrencesLeft > 0 || mapIterator.hasNext();
            }

            @Override
            public E next() {
                checkForModification(); // Controlla le modifiche strutturali
                if (occurrencesLeft == 0) {
                    currentEntry = mapIterator.next();
                    occurrencesLeft = currentEntry.getValue();
                }
                E element = currentEntry.getKey();
                occurrencesLeft--;
                return element;
            }

            @Override
            public void remove() {
                checkForModification(); // Controlla le modifiche strutturali
                if (occurrencesLeft == currentEntry.getValue()) {
                    // Rimuovi completamente l'elemento dalla mappa
                    mapIterator.remove();
                } else {
                    // Decrementa il numero di occorrenze dell'elemento
                    currentEntry.setValue(occurrencesLeft);
                }
                occurrencesLeft = 0;
                expectedModCount = modCount;
            }

            // Metodo per il controllo delle modifiche strutturali
            private void checkForModification() {
                if (expectedModCount != modCount) {
                    throw new ConcurrentModificationException();
                }
            }
        };
    }

    @Override
    public boolean contains(Object element) {
        // TODO implementare
        if (element == null) throw new NullPointerException("L'elemento non può essere null");

        return elementsMap.containsKey(element);
    }

    @Override
    public void clear() {
        // TODO implementare
        elementsMap.clear();
        modCount++;
    }

    @Override
    public boolean isEmpty() {
        // TODO implementare
        return elementsMap.isEmpty();
    }

    /*
     * Due multinsiemi sono uguali se e solo se contengono esattamente gli
     * stessi elementi (utilizzando l'equals della classe E) con le stesse
     * molteplicità.
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        // TODO implementare
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        MyMultiset<?> other = (MyMultiset<?>) obj;
        if (this.size() != other.size()) {
            return false;
        }

        for (Map.Entry<E, Integer> entry : elementsMap.entrySet()) {
            E element = entry.getKey();
            int occurrences = entry.getValue();

            if (other.count(element) != occurrences) {
                return false;
            }
        }

        return true;
    }

    /*
     * Da ridefinire in accordo con la ridefinizione di equals.
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        // TODO implementare
        int hash = 17;
        for (Map.Entry<E, Integer> entry : elementsMap.entrySet()) {
            E element = entry.getKey();
            int occurrences = entry.getValue();
            hash = 31 * hash + (element == null ? 0 : element.hashCode());
            hash = 31 * hash + occurrences;
        }
        return hash;
    }

}
