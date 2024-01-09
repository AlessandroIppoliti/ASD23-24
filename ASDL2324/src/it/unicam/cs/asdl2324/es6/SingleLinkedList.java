package it.unicam.cs.asdl2324.es6;

import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Lista concatenata singola che non accetta valori null, ma permette elementi
 * duplicati. Le seguenti operazioni non sono supportate:
 * 
 * <ul>
 * <li>ListIterator<E> listIterator()</li>
 * <li>ListIterator<E> listIterator(int index)</li>
 * <li>List<E> subList(int fromIndex, int toIndex)</li>
 * <li>T[] toArray(T[] a)</li>
 * <li>boolean containsAll(Collection<?> c)</li>
 * <li>addAll(Collection<? extends E> c)</li>
 * <li>boolean addAll(int index, Collection<? extends E> c)</li>
 * <li>boolean removeAll(Collection<?> c)</li>
 * <li>boolean retainAll(Collection<?> c)</li>
 * </ul>
 * 
 * L'iteratore restituito dal metodo {@code Iterator<E> iterator()} è fail-fast,
 * cioè se c'è una modifica strutturale alla lista durante l'uso dell'iteratore
 * allora lancia una {@code ConcurrentMopdificationException} appena possibile,
 * cioè alla prima chiamata del metodo {@code next()}.
 * 
 * @author Luca Tesei
 *
 * @param <E>
 *                il tipo degli elementi della lista
 */
public class SingleLinkedList<E> implements List<E> {

    private int size;

    private Node<E> head;

    private Node<E> tail;

    private int numeroModifiche;

    /**
     * Crea una lista vuota.
     */
    public SingleLinkedList() {
        this.size = 0;
        this.head = null;
        this.tail = null;
        this.numeroModifiche = 0;
    }

    /*
     * Classe per i nodi della lista concatenata. E' dichiarata static perché
     * gli oggetti della classe Node<E> non hanno bisogno di accedere ai campi
     * della classe principale per funzionare.
     */
    private static class Node<E> {
        private E item;

        private Node<E> next;

        /*
         * Crea un nodo "singolo" equivalente a una lista con un solo elemento.
         */
        Node(E item, Node<E> next) {
            this.item = item;
            this.next = next;
        }

    }

    /*
     * Classe che realizza un iteratore per SingleLinkedList.
     * L'iteratore deve essere fail-fast, cioè deve lanciare una eccezione
     * ConcurrentModificationException se a una chiamata di next() si "accorge"
     * che la lista è stata cambiata rispetto a quando l'iteratore è stato
     * creato.
     * 
     * La classe è non-static perché l'oggetto iteratore, per funzionare
     * correttamente, ha bisogno di accedere ai campi dell'oggetto della classe
     * principale presso cui è stato creato.
     */
    private class Itr implements Iterator<E> {

        private Node<E> lastReturned;

        private int numeroModificheAtteso;

        private Itr() {
            // All'inizio non è stato fatto nessun next
            this.lastReturned = null;
            this.numeroModificheAtteso = SingleLinkedList.this.numeroModifiche;
        }

        @Override
        public boolean hasNext() {
            if (this.lastReturned == null)
                // sono all'inizio dell'iterazione
                return SingleLinkedList.this.head != null;
            else
                // almeno un next è stato fatto
                return lastReturned.next != null;

        }

        @Override
        public E next() {
            // controllo concorrenza
            if (this.numeroModificheAtteso != SingleLinkedList.this.numeroModifiche) {
                throw new ConcurrentModificationException(
                        "Lista modificata durante l'iterazione");
            }
            // controllo hasNext()
            if (!hasNext())
                throw new NoSuchElementException(
                        "Richiesta di next quando hasNext è falso");
            // c'è sicuramente un elemento di cui fare next
            // aggiorno lastReturned e restituisco l'elemento next
            if (this.lastReturned == null) {
                // sono all’inizio e la lista non è vuota
                this.lastReturned = SingleLinkedList.this.head;
                return SingleLinkedList.this.head.item;
            } else {
                // non sono all’inizio, ma c’è ancora qualcuno
                lastReturned = lastReturned.next;
                return lastReturned.item;
            }

        }

    }

    /*
     * Una lista concatenata è uguale a un'altra lista se questa è una lista
     * concatenata e contiene gli stessi elementi nello stesso ordine.
     * 
     * Si noti che si poteva anche ridefinire il metodo equals in modo da
     * accettare qualsiasi oggetto che implementi List<E> senza richiedere che
     * sia un oggetto di questa classe:
     * 
     * obj instanceof List
     * 
     * In quel caso si può fare il cast a List<?>:
     * 
     * List<?> other = (List<?>) obj;
     * 
     * e usando l'iteratore si possono tranquillamente controllare tutti gli
     * elementi (come è stato fatto anche qui):
     * 
     * Iterator<E> thisIterator = this.iterator();
     * 
     * Iterator<?> otherIterator = other.iterator();
     * 
     * ...
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (this == obj)
            return true;
        if (!(obj instanceof SingleLinkedList))
            return false;
        SingleLinkedList<?> other = (SingleLinkedList<?>) obj;
        // Controllo se entrambe liste vuote
        if (head == null) {
            if (other.head != null)
                return false;
            else
                return true;
        }
        // Liste non vuote, scorro gli elementi di entrambe
        Iterator<E> thisIterator = this.iterator();
        Iterator<?> otherIterator = other.iterator();
        while (thisIterator.hasNext() && otherIterator.hasNext()) {
            E o1 = thisIterator.next();
            // uso il polimorfismo di Object perché non conosco il tipo ?
            Object o2 = otherIterator.next();
            // il metodo equals che si usa è quello della classe E
            if (!o1.equals(o2))
                return false;
        }
        // Controllo che entrambe le liste siano terminate
        return !(thisIterator.hasNext() || otherIterator.hasNext());
    }

    /*
     * L'hashcode è calcolato usando gli hashcode di tutti gli elementi della
     * lista.
     */
    @Override
    public int hashCode() {
        int hashCode = 1;
        // implicitamente, col for-each, uso l'iterator di questa classe
        for (E e : this)
            hashCode = 31 * hashCode + e.hashCode();
        return hashCode;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override
    public boolean contains(Object o) {
        // TODO implementare
        if (o == null) {
            throw new NullPointerException("La lista non accetta valori null");
        }

        Node<E> current = head;

        while (current != null) {
            if (current.item.equals(o)) {
                return true;
            }
            current = current.next;
        }

        return false;
    }

    @Override
    public boolean add(E e) {
        // TODO implementare
        if (e == null) {
            throw new NullPointerException("La lista non accetta valori null");
        }

        Node<E> newNode = new Node<>(e, null);
        if (head == null) {
            // La lista è vuota, il nuovo nodo diventa sia la testa che la coda.
            head = newNode;
            tail = newNode;
        } else {
            // Altrimenti, il nuovo nodo diventa la coda.
            tail.next = newNode;
            tail = newNode;
        }
        size++;
        numeroModifiche++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        // TODO implementare
        if (o == null) {
            throw new NullPointerException("La lista non accetta valori null");
        }

        if (head == null) {
            // La lista è vuota, non c'è nulla da rimuovere.
            return false;
        }

        if (head.item.equals(o)) {
            // Se l'elemento da rimuovere è nella testa
            head = head.next;
            if (head == null) {
                // La lista è ora vuota
                tail = null;
            }
            size--;
            numeroModifiche++;
            return true;
        }

        // Cerca l'elemento nella lista
        Node<E> current = head;
        while (current.next != null && !current.next.item.equals(o)) {
            current = current.next;
        }

        if (current.next != null) {
            // L'elemento è stato trovato nella lista, lo rimuoviamo
            current.next = current.next.next;
            if (current.next == null) {
                // Se abbiamo rimosso l'ultimo elemento, aggiorniamo la coda
                tail = current;
            }
            size--;
            numeroModifiche++;
            return true;
        }

        // L'elemento specificato non è presente nella lista
        return false;
    }

    @Override
    public void clear() {
        // TODO implementare
        head = null;
        tail = null;
        size = 0;
        numeroModifiche++;
    }

    @Override
    public E get(int index) {
        // TODO implementare
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Indice non valido: " + index);
        }

        Node<E> current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }

        return current.item;
    }

    @Override
    public E set(int index, E element) {
        // TODO implementare
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Indice non valido: " + index);
        }

        if (element == null) {
            throw new NullPointerException("La lista non accetta valori null");
        }

        Node<E> current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }

        E previousElement = current.item;
        current.item = element;
        return previousElement;
    }

    @Override
    public void add(int index, E element) {
        // TODO implementare
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Indice non valido: " + index);
        }

        if (element == null) {
            throw new NullPointerException("La lista non accetta valori null");
        }

        if (index == size) {
            // Se l'indice è uguale alla dimensione attuale, aggiungi alla fine
            add(element);
        } else if (index == 0) {
            // Se l'indice è 0, il nuovo elemento diventa la nuova testa
            Node<E> newNode = new Node<>(element, head);
            head = newNode;
            size++;
            numeroModifiche++;
        } else {
            // Altrimenti, inserisci il nuovo elemento tra due nodi esistenti
            Node<E> current = head;
            for (int i = 0; i < index - 1; i++) {
                current = current.next;
            }
            Node<E> newNode = new Node<>(element, current.next);
            current.next = newNode;
            size++;
            numeroModifiche++;
        }
    }

    @Override
    public E remove(int index) {
        // TODO implementare
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Indice non valido: " + index);
        }

        if (index == 0) {
            // Se l'indice è 0, rimuovi la testa della lista
            E removedItem = head.item;
            head = head.next;
            size--;
            numeroModifiche++;
            return removedItem;
        } else {
            // Altrimenti, cerca l'elemento da rimuovere
            Node<E> current = head;
            for (int i = 0; i < index - 1; i++) {
                current = current.next;
            }
            E removedItem = current.next.item;
            current.next = current.next.next;
            if (current.next == null) {
                // Se l'elemento rimosso era l'ultimo, aggiorna la coda
                tail = current;
            }
            size--;
            numeroModifiche++;
            return removedItem;
        }
    }

    @Override
    public int indexOf(Object o) {
        // TODO implementare
        if (o == null) {
            throw new NullPointerException("La lista non accetta valori null");
        }

        Node<E> current = head;
        int index = 0;
        while (current != null) {
            if (current.item.equals(o)) {
                return index;
            }
            current = current.next;
            index++;
        }
        return -1; // Elemento non trovato
    }

    @Override
    public int lastIndexOf(Object o) {
        // TODO implementare
        if (o == null) {
            throw new NullPointerException("La lista non accetta valori null");
        }

        int lastIndex = -1;
        int index = 0;
        Node<E> current = head;

        while (current != null) {
            if (current.item.equals(o)) {
                lastIndex = index;
            }
            current = current.next;
            index++;
        }

        return lastIndex;
    }

    @Override
    public Object[] toArray() {
        // TODO implementare
        Object[] array = new Object[size];
        int index = 0;
        Node<E> current = head;

        while (current != null) {
            array[index] = current.item;
            current = current.next;
            index++;
        }

        return array;
    }

    @Override
    public Iterator<E> iterator() {
        return new Itr();
    }

    @Override
    public ListIterator<E> listIterator() {
        throw new UnsupportedOperationException("Operazione non supportata.");
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        throw new UnsupportedOperationException("Operazione non supportata.");
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException("Operazione non supportata.");
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException("Operazione non supportata.");
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException("Operazione non supportata.");
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        throw new UnsupportedOperationException("Operazione non supportata.");
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        throw new UnsupportedOperationException("Operazione non supportata.");
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException("Operazione non supportata.");
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("Operazione non supportata.");
    }
}
