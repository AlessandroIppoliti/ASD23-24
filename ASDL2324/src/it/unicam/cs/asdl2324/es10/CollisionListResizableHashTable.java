/**
 * 
 */
package it.unicam.cs.asdl2324.es10;

import java.util.*;

/**
 * Realizza un insieme tramite una tabella hash con indirizzamento primario (la
 * funzione di hash primario deve essere passata come parametro nel costruttore
 * e deve implementare l'interface PrimaryHashFunction) e liste di collisione.
 * 
 * La tabella, poiché implementa l'interfaccia Set<E> non accetta elementi
 * duplicati (individuati tramite il metodo equals() che si assume sia
 * opportunamente ridefinito nella classe E) e non accetta elementi null.
 * 
 * La tabella ha una dimensione iniziale di default (16) e un fattore di
 * caricamento di defaut (0.75). Quando il fattore di bilanciamento effettivo
 * eccede quello di default la tabella viene raddoppiata e viene fatto un
 * riposizionamento di tutti gli elementi.
 * 
 * @author Template: Luca Tesei, Implementazione: collettiva
 *
 */
public class CollisionListResizableHashTable<E> implements Set<E> {

    /*
     * La capacità iniziale. E' una potenza di due e quindi la capacità sarà
     * sempre una potenza di due, in quanto ogni resize raddoppia la tabella.
     */
    private static final int INITIAL_CAPACITY = 16;

    /*
     * Fattore di bilanciamento di default. Tipico valore.
     */
    private static final double LOAD_FACTOR = 0.75;

    /*
     * Numero di elementi effettivamente presenti nella hash table in questo
     * momento. ATTENZIONE: questo valore è diverso dalla capacity, che è la
     * lunghezza attuale dell'array di Object che rappresenta la tabella.
     */
    private int size;

    /*
     * L'idea è che l'elemento in posizione i della tabella hash è un bucket che
     * contiene null oppure il puntatore al primo nodo di una lista concatenata
     * di elementi. Si può riprendere e adattare il proprio codice della
     * Esercitazione 6 che realizzava una lista concatenata di elementi
     * generici. La classe interna Node<E> è ripresa proprio da lì.
     * 
     * ATTENZIONE: la tabella hash vera e propria può essere solo un generico
     * array di Object e non di Node<E> per una impossibilità del compilatore di
     * accettare di creare array a runtime con un tipo generics. Ciò infatti
     * comporterebbe dei problemi nel sistema di check dei tipi Java che, a
     * run-time, potrebbe eseguire degli assegnamenti in violazione del tipo
     * effettivo della variabile. Quindi usiamo un array di Object che
     * riempiremo sempre con null o con puntatori a oggetti di tipo Node<E>.
     * 
     * Per inserire un elemento nella tabella possiamo usare il polimorfismo di
     * Object:
     * 
     * this.table[i] = new Node<E>(item, next);
     * 
     * ma quando dobbiamo prendere un elemento dalla tabella saremo costretti a
     * fare un cast esplicito:
     * 
     * Node<E> myNode = (Node<E>) this.table[i];
     * 
     * Ci sarà dato un warning di cast non controllato, ma possiamo eliminarlo
     * con un tag @SuppressWarning,
     */
    private Object[] table;

    /*
     * Funzion di hash primaria usata da questa hash table. Va inizializzata nel
     * costruttore all'atto di creazione dell'oggetto.
     */
    private final PrimaryHashFunction phf;

    /*
     * Contatore del numero di modifiche. Serve per rendere l'iterator
     * fail-fast.
     */
    private int modCount;

    // I due metodi seguenti sono di comodo per gestire la capacity e la soglia
    // oltre la quale bisogna fare il resize.

    /* Numero di elementi della tabella corrente */
    private int getCurrentCapacity() {
        return this.table.length;
    };

    /*
     * Valore corrente soglia oltre la quale si deve fare la resize,
     * getCurrentCapacity * LOAD_FACTOR
     */
    private int getCurrentThreshold() {
        return (int) (getCurrentCapacity() * LOAD_FACTOR);
    }

    /**
     * Costruisce una Hash Table con capacità iniziale di default e fattore di
     * caricamento di default.
     */
    public CollisionListResizableHashTable(PrimaryHashFunction phf) {
        this.phf = phf;
        this.table = new Object[INITIAL_CAPACITY];
        this.size = 0;
        this.modCount = 0;
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
        /*
         * ATTENZIONE: usare l'hashCode dell'oggetto e la funzione di hash
         * primaria passata all'atto della creazione: il bucket in cui cercare
         * l'oggetto o è la posizione
         * this.phf.hash(o.hashCode(),this.getCurrentCapacity)
         * 
         * In questa posizione, se non vuota, si deve cercare l'elemento o
         * utilizzando il metodo equals() su tutti gli elementi della lista
         * concatenata lì presente
         * 
         */
        int index = this.phf.hash(o.hashCode(), this.getCurrentCapacity());
        Object currentNode = this.table[index];

        if (currentNode instanceof Node) {
            @SuppressWarnings("unchecked")
            Node<E> node = (Node<E>) currentNode;

            while (node != null) {
                if (node.item.equals(o)) {
                    return true; // Trovato l'elemento
                }
                node = node.next;
            }
        }

        return false;
    }

    @Override
    public Iterator<E> iterator() {
        return new Itr();
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException("Operazione non supportata");
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException("Operazione non supportata");
    }

    @Override
    public boolean add(E e) {
        // TODO implementare
        /*
         * ATTENZIONE: usare l'hashCode dell'oggetto e la funzione di hash
         * primaria passata all'atto della creazione: il bucket in cui inserire
         * l'oggetto o è la posizione
         * this.phf.hash(o.hashCode(),this.getCurrentCapacity)
         * 
         * In questa posizione, se non vuota, si deve inserire l'elemento o
         * nella lista concatenata lì presente. Se vuota, si crea la lista
         * concatenata e si inserisce l'elemento, che sarà l'unico.
         * 
         */
        // ATTENZIONE, si inserisca prima il nuovo elemento e poi si controlli
        // se bisogna fare resize(), cioè se this.size >
        // this.getCurrentThreshold()
        int index = this.phf.hash(e.hashCode(), this.getCurrentCapacity());
        Node<E> currentNode = (Node<E>) this.table[index];

        if (currentNode == null) {
            this.table[index] = new Node<>(e, null);
            this.size++;
            this.modCount++;

            // Controlla se è necessario ridimensionare la tabella
            if (this.size > this.getCurrentThreshold()) {
                resize();
            }

            return true;
        } else {
            Node<E> prevNode = null;
            while (currentNode != null) {
                if (currentNode.item.equals(e)) {
                    return false; // Elemento già presente, non aggiungerlo di nuovo
                }
                prevNode = currentNode;
                currentNode = currentNode.next;
            }
            prevNode.next = new Node<>(e, null);
            this.size++;
            this.modCount++;

            // Controlla se è necessario ridimensionare la tabella
            if (this.size > this.getCurrentThreshold()) {
                resize();
            }

            return true;
        }
    }

    /*
     * Raddoppia la tabella corrente e riposiziona tutti gli elementi. Da
     * chiamare quando this.size diventa maggiore di getCurrentThreshold()
     */
    private void resize() {
        // TODO implementare
        int newCapacity = this.getCurrentCapacity() * 2;
        Object[] newTable = new Object[newCapacity];

        // Ri-hash e ricolloca tutti gli elementi nella nuova tabella
        for (int i = 0; i < this.table.length; i++) {
            Node<E> currentNode = (Node<E>) this.table[i];
            while (currentNode != null) {
                int newIndex = this.phf.hash(currentNode.item.hashCode(), newCapacity);
                Node<E> temp = currentNode.next;
                currentNode.next = (Node<E>) newTable[newIndex];
                newTable[newIndex] = currentNode;
                currentNode = temp;
            }
        }
        this.table = newTable;
    }

    @Override
    public boolean remove(Object o) {
        // TODO implementare
        /*
         * ATTENZIONE: usare l'hashCode dell'oggetto e la funzione di hash
         * primaria passata all'atto della creazione: il bucket in cui cercare
         * l'oggetto o è la posizione
         * this.phf.hash(o.hashCode(),this.getCurrentCapacity)
         * 
         * In questa posizione, se non vuota, si deve cercare l'elemento o
         * utilizzando il metodo equals() su tutti gli elementi della lista
         * concatenata lì presente. Se presente, l'elemento deve essere
         * eliminato dalla lista concatenata
         * 
         */
        // ATTENZIONE: la rimozione, in questa implementazione, **non** comporta
        // mai una resize "al ribasso", cioè un dimezzamento della tabella se si
        // scende sotto il fattore di bilanciamento desiderato.
        int index = this.phf.hash(o.hashCode(), this.getCurrentCapacity());
        Node<E> currentNode = (Node<E>) this.table[index];
        Node<E> prevNode = null;

        while (currentNode != null) {
            if (currentNode.item.equals(o)) {
                if (prevNode == null) {
                    // Se l'elemento da rimuovere è il primo nella lista
                    this.table[index] = currentNode.next;
                } else {
                    prevNode.next = currentNode.next;
                }
                this.size--;
                this.modCount++;
                return true; // Elemento trovato e rimosso
            }
            prevNode = currentNode;
            currentNode = currentNode.next;
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        // TODO implementare
        for (Object element : c) {
            if (!contains(element)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        // TODO implementare
        // utilizzare un iteratore della collection e chiamare il metodo add
        boolean modified = false;
        for (E element : c) {
            if (add(element)) {
                modified = true; // Se almeno un elemento è stato aggiunto, imposta modified a true
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("Operazione non supportata");
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        // TODO implementare
        // utilizzare un iteratore della collection e chiamare il metodo remove
        boolean modified = false;
        for (Object element : c) {
            if (remove(element)) {
                modified = true; // Se almeno un elemento è stato rimosso, imposta modified a true
            }
        }
        return modified;
    }

    @Override
    public void clear() {
        // Ritorno alla situazione iniziale
        this.table = new Object[INITIAL_CAPACITY];
        this.size = 0;
        this.modCount = 0;
    }

    /*
     * Classe per i nodi della lista concatenata. Lo specificatore è protected
     * solo per permettere i test JUnit.
     */
    protected static class Node<E> {
        protected E item;

        protected Node<E> next;

        /*
         * Crea un nodo "singolo" equivalente a una lista con un solo elemento.
         */
        Node(E item, Node<E> next) {
            this.item = item;
            this.next = next;
        }
    }

    /*
     * Classe che realizza un iteratore per questa hash table. L'ordine in cui
     * vengono restituiti gli oggetti presenti non è rilevante, ma ogni oggetto
     * presente deve essere restituito dall'iteratore una e una sola volta.
     * L'iteratore deve essere fail-fast, cioè deve lanciare una eccezione
     * ConcurrentModificationException se a una chiamata di next() si "accorge"
     * che la tabella è stata cambiata rispetto a quando l'iteratore è stato
     * creato.
     */
    private class Itr implements Iterator<E> {

        // TODO inserire le variabili che servono
        private int currentIndex;

        private Node<E> currentNode;
        private Node<E> nextNode;

        private int numeroModificheAtteso;

        private Itr() {
            // TODO implementare il resto
            this.numeroModificheAtteso = modCount;
            this.currentIndex = 0;
            this.currentNode = null;
            this.nextNode = null;

            while (currentIndex < table.length && table[currentIndex] == null) {
                currentIndex++;
            }
            if (currentIndex < table.length) {
                this.nextNode = (Node<E>) table[currentIndex];
            }
        }

        @Override
        public boolean hasNext() {
            // TODO implementare
            return nextNode != null;
        }

        @Override
        public E next() {
            // TODO implementare
            if (modCount != numeroModificheAtteso) {
                throw new ConcurrentModificationException("Tabella modificata durante l'iterazione");
            }
            if (!hasNext()) {
                throw new NoSuchElementException("Non ci sono più elementi");
            }

            currentNode = nextNode;

            // Trova il prossimo elemento non nullo per il prossimo next()
            if (currentNode.next != null) {
                nextNode = currentNode.next;
            } else {
                currentIndex++;
                while (currentIndex < table.length && table[currentIndex] == null) {
                    currentIndex++;
                }
                if (currentIndex < table.length) {
                    nextNode = (Node<E>) table[currentIndex];
                } else {
                    nextNode = null;
                }
            }

            return currentNode.item;
        }

    }

    /*
     * Only for JUnit testing purposes.
     */
    protected Object[] getTable() {
        return this.table;
    }

    /*
     * Only for JUnit testing purposes.
     */
    protected PrimaryHashFunction getPhf() {
        return this.phf;
    }

}
