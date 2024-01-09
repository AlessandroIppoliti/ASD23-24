package it.unicam.cs.asdl2324.mp1;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * // TODO spiegare come viene implementata la classe
 * implementa un insieme disgiunto utilizzando una rappresentazione con liste concatenate.
 * Questa rappresentazione tiene traccia degli insiemi e dei loro elementi utilizzando
 * una struttura di dati basata su nodi collegati.
 *
 * @author Luca Tesei (template) **ALESSANDRO IPPOLITI
 * alessand.ippoliti@studenti.unicam.it** (implementazione)
 */
public class LinkedListDisjointSets implements DisjointSets {

    // TODO inserire le variabili istanza private che si ritengono necessarie
    private Map<DisjointSetElement, Set<DisjointSetElement>> disjointSets;

    /**
     * Crea una collezione vuota di insiemi disgiunti.
     */
    public LinkedListDisjointSets() {
        // TODO implementare
        this.disjointSets = new HashMap<>();
    }

    /*
     * Nella rappresentazione con liste concatenate un elemento è presente in
     * qualche insieme disgiunto se il puntatore al suo elemento rappresentante
     * (ref1) non è null.
     */
    @Override
    public boolean isPresent(DisjointSetElement e) {
        // TODO implementare
        // Se il riferimento al rappresentante non è null, l'elemento è presente in un insieme
        return e.getRef1() != null;
    }

    /*
     * Nella rappresentazione con liste concatenate un nuovo insieme disgiunto è
     * rappresentato da una lista concatenata che contiene l'unico elemento. Il
     * rappresentante deve essere l'elemento stesso e la cardinalità deve essere
     * 1.
     */
    @Override
    public void makeSet(DisjointSetElement e) {
        // TODO implementare
        if (!isPresent(e)) {
            // Creazione di un nuovo insieme disgiunto con un unico elemento
            e.setRef1(e); // Imposta il riferimento 1 a se stesso
            e.setNumber(1); // Imposta la cardinalità a 1

            // Aggiungi l'elemento come rappresentante dell'insieme nella mappa disjointSets
            Set<DisjointSetElement> set = new HashSet<>();
            set.add(e);
            disjointSets.put(e, set);
        } else {
            throw new IllegalArgumentException("Elemento già presente in un insieme disgiunto");
        }
    }

    /*
     * Nella rappresentazione con liste concatenate per trovare il
     * rappresentante di un elemento basta far riferimento al suo puntatore
     * ref1.
     */
    @Override
    public DisjointSetElement findSet(DisjointSetElement e) {
        // TODO implementare
        // Verifica se l'elemento è presente in un insieme disgiunto
        if (!isPresent(e)) throw new IllegalArgumentException("Elemento non presente in un insieme disgiunto");

        // Trova il rappresentante dell'insieme
        DisjointSetElement representative = e;
        while (representative != representative.getRef1()) {
            representative = representative.getRef1();
        }

        return representative;
    }

    /*
     * Dopo l'unione di due insiemi effettivamente disgiunti il rappresentante
     * dell'insieme unito è il rappresentate dell'insieme che aveva il numero
     * maggiore di elementi tra l'insieme di cui faceva parte {@code e1} e
     * l'insieme di cui faceva parte {@code e2}. Nel caso in cui entrambi gli
     * insiemi avevano lo stesso numero di elementi il rappresentante
     * dell'insieme unito è il rappresentante del vecchio insieme di cui faceva
     * parte {@code e1}.
     *
     * Questo comportamento è la risultante naturale di una strategia che
     * minimizza il numero di operazioni da fare per realizzare l'unione nel
     * caso di rappresentazione con liste concatenate.
     *
     */
    @Override
    public void union(DisjointSetElement e1, DisjointSetElement e2) {
        // Verifica dei casi nulli
        if (e1 == null || e2 == null) throw new NullPointerException("L'elemento non può essere null");

        // Trova i rappresentanti degli insiemi a cui appartengono e1 ed e2
        DisjointSetElement set1 = findSet(e1);
        DisjointSetElement set2 = findSet(e2);

        // Se i rappresentanti sono diversi
        if (set1 != set2) {
            Set<DisjointSetElement> set1Elements = disjointSets.get(set1);
            Set<DisjointSetElement> set2Elements = disjointSets.get(set2);

            // Unione dei due insiemi
            if (set1Elements.size() >= set2Elements.size()) {
                // Aggiorna i riferimenti ref1
                for (DisjointSetElement element : set2Elements) {
                    element.setRef1(set1);
                }
                // Aggiorna i riferimenti ref2
                DisjointSetElement last = null;
                for (DisjointSetElement element : set1Elements) {
                    if (element.getRef2() == null) {
                        last = element;
                    }
                }
                last.setRef2(set2);
                // Unione degli insiemi
                set1Elements.addAll(set2Elements);
                disjointSets.remove(set2);
                // Aggiorna la dimensione del set combinato
                set1.setNumber(set1Elements.size());
            } else {
                // Aggiorna i riferimenti ref1
                for (DisjointSetElement element : set1Elements) {
                    element.setRef1(set2);
                }
                // Aggiorna i riferimenti ref2
                DisjointSetElement last = null;
                for (DisjointSetElement element : set2Elements) {
                    if (element.getRef2() == null) {
                        last = element;
                    }
                }
                if (last != null) last.setRef2(set1);
                // Unione degli insiemi
                set2Elements.addAll(set1Elements);
                disjointSets.remove(set1);
                // Aggiorna la dimensione del set combinato
                set2.setNumber(set2Elements.size());
            }
        }
    }


    @Override
    public Set<DisjointSetElement> getCurrentRepresentatives() {
        // TODO implementare
        return disjointSets.keySet();
    }

    @Override
    public Set<DisjointSetElement> getCurrentElementsOfSetContaining(DisjointSetElement e) {
        // TODO implementare
        if (e == null) throw new NullPointerException("L'elemento non può essere null");

        DisjointSetElement representativeE = findSet(e);
        if (representativeE == null || !disjointSets.containsKey(representativeE))
            throw new IllegalArgumentException("Elemento non presente in un insieme disgiunto");


        Set<DisjointSetElement> set = disjointSets.get(representativeE);
        if (!set.contains(e)) throw new IllegalArgumentException("Elemento non presente in un insieme disgiunto");


        return set;
    }

    @Override
    public int getCardinalityOfSetContaining(DisjointSetElement e) {
        // TODO implementare
        DisjointSetElement representativeE = findSet(e);
        Set<DisjointSetElement> setElements = disjointSets.get(representativeE);
        return setElements.size();
    }

}
