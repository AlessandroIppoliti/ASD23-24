package it.unicam.cs.asdl2324.es5;

import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
// TODO importare eventuali classi o interfacce che servono
import java.util.Iterator;

/**
 * Un oggetto della classe aula rappresenta una certa aula con le sue facilities
 * e le sue prenotazioni.
 *
 * @author Template: Luca Tesei, Implementazione: Collettiva
 */
public class Aula implements Comparable<Aula> {
    // Identificativo unico di un'aula
    private final String nome;

    // Location dell'aula
    private final String location;

    // Insieme delle facilities di quest'aula
    private final Set<Facility> facilities;

    // Insieme delle prenotazioni per quest'aula, segue l'ordinamento naturale
    // delle prenotazioni
    private final SortedSet<Prenotazione> prenotazioni;

    /**
     * Costruisce una certa aula con nome e location. Il set delle facilities è
     * vuoto. L'aula non ha inizialmente nessuna prenotazione.
     *
     * @param nome     il nome dell'aula
     * @param location la location dell'aula
     * @throws NullPointerException se una qualsiasi delle informazioni
     *                              richieste è nulla
     */
    public Aula(String nome, String location) {
        // TODO implementare
        if (nome == null || location == null)
            throw new NullPointerException("Informazioni nulle");
        this.nome = nome;
        this.location = location;
        this.facilities = new HashSet<Facility>();
        this.prenotazioni = new TreeSet<Prenotazione>();
    }

    /**
     * Costruisce una certa aula con nome, location e insieme delle facilities.
     * L'aula non ha inizialmente nessuna prenotazione.
     *
     * @param nome       il nome dell'aula
     * @param location   la location dell'aula
     * @param facilities l'insieme delle facilities dell'aula
     * @throws NullPointerException se una qualsiasi delle informazioni
     *                              richieste è nulla
     */
    public Aula(String nome, String location, Set<Facility> facilities) {
        // TODO implementare
        if (nome == null || location == null || facilities == null) {
            throw new NullPointerException("Informazioni nulle");
        }
        this.nome = nome;
        this.location = location;
        this.facilities = facilities;
        this.prenotazioni = new TreeSet<Prenotazione>();
    }

    /*
     * Ridefinire in accordo con equals
     */
    @Override
    public int hashCode() {
        // TODO implementare
        return nome.hashCode();
    }

    /* Due aule sono uguali se e solo se hanno lo stesso nome */
    @Override
    public boolean equals(Object obj) {
        // TODO implementare
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Aula otherAula = (Aula) obj;

        return nome.equals(otherAula.nome);
    }

    /* L'ordinamento naturale si basa sul nome dell'aula */
    @Override
    public int compareTo(Aula o) {
        // TODO implementare
        return this.nome.compareTo(o.nome);
    }

    /**
     * @return the facilities
     */
    public Set<Facility> getFacilities() {
        return facilities;
    }

    /**
     * @return the nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * @return the prenotazioni
     */
    public SortedSet<Prenotazione> getPrenotazioni() {
        return prenotazioni;
    }

    /**
     * Aggiunge una faciltity a questa aula.
     *
     * @param f la facility da aggiungere
     * @return true se la facility non era già presente e quindi è stata
     * aggiunta, false altrimenti
     * @throws NullPointerException se la facility passata è nulla
     */
    public boolean addFacility(Facility f) {
        // TODO implementare
        if(f == null)
            throw new NullPointerException("La facility è nulla");
        boolean added = facilities.add(f);
        return added;
    }

    /**
     * Determina se l'aula è libera in un certo time slot.
     *
     * @param ts il time slot da controllare
     * @return true se l'aula risulta libera per tutto il periodo del time slot
     * specificato
     * @throws NullPointerException se il time slot passato è nullo
     */
    public boolean isFree(TimeSlot ts) {
        // TODO implementare
        /*
         * NOTA: sfruttare l'ordinamento tra le prenotazioni per rispondere in
         * maniera efficiente: poiché le prenotazioni sono in ordine crescente
         * di time slot se arrivo a una prenotazione che segue il time slot
         * specificato posso concludere che l'aula è libera nel time slot
         * desiderato e posso interrompere la ricerca
         */
        if (ts == null) {
            throw new NullPointerException("Il time slot passato è nullo");
        }

        for (Prenotazione prenotazione : prenotazioni) {
            // Verifica se c'è sovrapposizione tra il time slot della prenotazione e il time slot specificato
            if (prenotazione.getTimeSlot().overlapsWith(ts)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Determina se questa aula soddisfa tutte le facilities richieste
     * rappresentate da un certo insieme dato.
     *
     * @param requestedFacilities l'insieme di facilities richieste da
     *                            soddisfare
     * @return true se e solo se tutte le facilities di
     * {@code requestedFacilities} sono soddisfatte da questa aula.
     * @throws NullPointerException se il set di facility richieste è nullo
     */
    public boolean satisfiesFacilities(Set<Facility> requestedFacilities) {
        // TODO implementare
        if (requestedFacilities == null) {
            throw new NullPointerException("Il set di facility richieste è nullo");
        }

        return facilities.containsAll(requestedFacilities);
    }

    /**
     * Prenota l'aula controllando eventuali sovrapposizioni.
     *
     * @param ts
     * @param docente
     * @param motivo
     * @throws IllegalArgumentException se la prenotazione comporta una
     *                                  sovrapposizione con un'altra
     *                                  prenotazione nella stessa aula.
     * @throws NullPointerException     se una qualsiasi delle informazioni
     *                                  richieste è nulla.
     */
    public void addPrenotazione(TimeSlot ts, String docente, String motivo) {
        // TODO implementare
        if (ts == null || docente == null || motivo == null) {
            throw new NullPointerException("I parametri non possono essere nulli");
        }

        for (Prenotazione existingPrenotazione : prenotazioni) {
            if (existingPrenotazione.getTimeSlot().overlapsWith(ts)) {
                throw new IllegalArgumentException("La prenotazione comporta una sovrapposizione con un'altra prenotazione nella stessa aula.");
            }
        }

        Prenotazione newPrenotazione = new Prenotazione(this, ts, docente, motivo);
        prenotazioni.add(newPrenotazione);
    }

    /**
     * Cancella una prenotazione di questa aula.
     *
     * @param p la prenotazione da cancellare
     * @return true se la prenotazione è stata cancellata, false se non era
     * presente.
     * @throws NullPointerException se la prenotazione passata è null
     */
    public boolean removePrenotazione(Prenotazione p) {
        // TODO implementare
        if (p == null) {
            throw new NullPointerException("La prenotazione passata è null");
        }

        boolean removed = prenotazioni.remove(p);

        return removed;
    }

    /**
     * Rimuove tutte le prenotazioni di questa aula che iniziano prima (o
     * esattamente in) di un punto nel tempo specificato.
     *
     * @param timePoint un certo punto nel tempo
     * @return true se almeno una prenotazione è stata cancellata, false
     * altrimenti.
     * @throws NullPointerException se il punto nel tempo passato è nullo.
     */
    public boolean removePrenotazioniBefore(GregorianCalendar timePoint) {
        // TODO implementare
        /*
         * NOTA: sfruttare l'ordinamento tra le prenotazioni per rispondere in
         * maniera efficiente: poiché le prenotazioni sono in ordine crescente
         * di time slot se ho raggiunto una prenotazione con tempo di inizio
         * maggiore del tempo indicato posso smettere la procedura
         */
        if (timePoint == null) {
            throw new NullPointerException("Il punto nel tempo passato è nullo.");
        }

        boolean removedAtLeastOne = false; // Flag per tener traccia se almeno una prenotazione è stata rimossa

        Iterator<Prenotazione> iterator = prenotazioni.iterator();
        while (iterator.hasNext()) {
            Prenotazione prenotazione = iterator.next();
            if (prenotazione.getTimeSlot().getStart().compareTo(timePoint) <= 0) {
                iterator.remove();
                removedAtLeastOne = true;
            } else {
                break;
            }
        }

        return removedAtLeastOne;
    }
}
