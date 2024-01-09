package it.unicam.cs.asdl2324.es4;

/**
 * Un oggetto della classe aula rappresenta una certa aula con le sue facilities
 * e le sue prenotazioni.
 *
 * @author Template: Luca Tesei, Implementation: Collective
 */
public class Aula implements Comparable<Aula> {

    /*
     * numero iniziale delle posizioni dell'array facilities. Se viene richiesto
     * di inserire una facility e l'array è pieno questo viene raddoppiato. La
     * costante è protected solo per consentirne l'accesso ai test JUnit
     */
    protected static final int INIT_NUM_FACILITIES = 5;

    /*
     * numero iniziale delle posizioni dell'array prenotazioni. Se viene
     * richiesto di inserire una prenotazione e l'array è pieno questo viene
     * raddoppiato. La costante è protected solo per consentirne l'accesso ai
     * test JUnit.
     */
    protected static final int INIT_NUM_PRENOTAZIONI = 100;

    // Identificativo unico di un'aula
    private final String nome;

    // Location dell'aula
    private final String location;

    /*
     * Insieme delle facilities di quest'aula. L'array viene creato all'inizio
     * della dimensione specificata nella costante INIT_NUM_FACILITIES. Il
     * metodo addFacility(Facility) raddoppia l'array qualora non ci sia più
     * spazio per inserire la facility.
     */
    private Facility[] facilities;

    // numero corrente di facilities inserite
    private int numFacilities;

    /*
     * Insieme delle prenotazioni per quest'aula. L'array viene creato
     * all'inizio della dimensione specificata nella costante
     * INIT_NUM_PRENOTAZIONI. Il metodo addPrenotazione(TimeSlot, String,
     * String) raddoppia l'array qualora non ci sia più spazio per inserire la
     * prenotazione.
     */
    private Prenotazione[] prenotazioni;

    // numero corrente di prenotazioni inserite
    private int numPrenotazioni;

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
            throw new NullPointerException("Le informazioni non possono essere nulle");
        this.nome = nome;
        this.location = location;

        facilities = new Facility[INIT_NUM_FACILITIES];
        numFacilities = 0;

        prenotazioni = new Prenotazione[INIT_NUM_PRENOTAZIONI];
        numPrenotazioni = 0;

    }

    /*
     * Ridefinire in accordo con equals
     */
    @Override
    public int hashCode() {
        // TODO implementare
        int result = 17;
        result = 31 * result + (nome != null ? nome.hashCode() : 0);
        return result;
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

        Aula o = (Aula) obj;

        if (nome == null) {
            return o.nome == null;
        } else {
            return nome.equals(o.nome);
        }
    }

    /* L'ordinamento naturale si basa sul nome dell'aula */
    @Override
    public int compareTo(Aula o) {
        // TODO implementare
        if (o == null) {
            throw new NullPointerException("Impossibile confrontare con un oggetto nullo.");
        }

        if (nome == null) {
            if (o.nome == null) {
                return 0;
            } else {
                return -1;
            }
        } else {
            if (o.nome == null) {
                return 1;
            } else {
                return nome.compareTo(o.nome);
            }
        }
    }

    /**
     * @return the facilities
     */
    public Facility[] getFacilities() {
        return this.facilities;
    }

    /**
     * @return il numero corrente di facilities
     */
    public int getNumeroFacilities() {
        return this.numFacilities;
    }

    /**
     * @return the nome
     */
    public String getNome() {
        return this.nome;
    }

    /**
     * @return the location
     */
    public String getLocation() {
        return this.location;
    }

    /**
     * @return the prenotazioni
     */
    public Prenotazione[] getPrenotazioni() {
        return this.prenotazioni;
    }

    /**
     * @return il numero corrente di prenotazioni
     */
    public int getNumeroPrenotazioni() {
        return this.numPrenotazioni;
    }

    /**
     * Aggiunge una faciltity a questa aula. Controlla se la facility è già
     * presente, nel qual caso non la inserisce.
     *
     * @param f la facility da aggiungere
     * @return true se la facility non era già presente e quindi è stata
     * aggiunta, false altrimenti
     * @throws NullPointerException se la facility passata è nulla
     */
    public boolean addFacility(Facility f) {
        /*
         * Nota: attenzione! Per controllare se una facility è già presente
         * bisogna usare il metodo equals della classe Facility.
         *
         * Nota: attenzione bis! Si noti che per le sottoclassi di Facility non
         * è richiesto di ridefinire ulteriormente il metodo equals...
         */
        // TODO implementare
        if (f == null) {
            throw new NullPointerException("La facility non può essere nulla.");
        }

        // Controlla se la facility è già presente nell'array delle facilities
        for (int i = 0; i < numFacilities; i++) {
            if (facilities[i] != null && facilities[i].equals(f)) {
                return false;
            }
        }

        // Aggiungiamo la facilities
        if (numFacilities == facilities.length) {
            Facility[] newFacilities = new Facility[facilities.length * 2];
            System.arraycopy(facilities, 0, newFacilities, 0, facilities.length);
            facilities = newFacilities;
        }

        facilities[numFacilities] = f;
        numFacilities++;
        return true;
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
        if (ts == null) {
            throw new NullPointerException("Il time slot passato è nullo.");
        }

        for (int i = 0; i < numPrenotazioni; i++) {
            Prenotazione existingPrenotazione = prenotazioni[i];
            if (existingPrenotazione != null && existingPrenotazione.getTimeSlot().overlapsWith(ts)) {
                // C'è sovrapposizione, l'aula non è libera
                return false;
            }
        }

        // Nessuna sovrapposizione trovata, l'aula è libera
        return true;
    }

    /**
     * Determina se questa aula soddisfa tutte le facilities richieste
     * rappresentate da un certo insieme dato.
     *
     * @param requestedFacilities l'insieme di facilities richieste da
     *                            soddisfare, sono da considerare solo le
     *                            posizioni diverse da null
     * @return true se e solo se tutte le facilities di
     * {@code requestedFacilities} sono soddisfatte da questa aula.
     * @throws NullPointerException se il set di facility richieste è nullo
     */
    public boolean satisfiesFacilities(Facility[] requestedFacilities) {
        // TODO implementare
        if (requestedFacilities == null) {
            throw new NullPointerException("L'insieme di facilities richieste non può essere nullo");
        }

        for (Facility facility : requestedFacilities) {
            if (facility != null && !hasFacility(facility)) {
                return false;
            }
        }

        return true;
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
            throw new NullPointerException("Le informazioni non possono essere nulle.");
        }

        // Verifica se la prenotazione comporta sovrapposizioni
        for (int i = 0; i < numPrenotazioni; i++) {
            Prenotazione existingPrenotazione = prenotazioni[i];
            if (existingPrenotazione != null && existingPrenotazione.getAula().equals(this) && existingPrenotazione.getTimeSlot().overlapsWith(ts)) {
                throw new IllegalArgumentException("La prenotazione si sovrappone con un'altra prenotazione nella stessa aula.");
            }
        }

        if (numPrenotazioni == prenotazioni.length) {
            // Se l'array è pieno, espandiamo manualmente la dimensione
            Prenotazione[] newPrenotazioni = new Prenotazione[prenotazioni.length * 2];
            System.arraycopy(prenotazioni, 0, newPrenotazioni, 0, prenotazioni.length);
            prenotazioni = newPrenotazioni;
        }

        prenotazioni[numPrenotazioni] = new Prenotazione(this, ts, docente, motivo);
        numPrenotazioni++;
    }

    // TODO inserire eventuali metodi privati per questioni di organizzazione

    /**
     * Metodo per verificare se questa aula ha una specifica facility
     *
     * @param facility
     */
    private boolean hasFacility(Facility facility) {
        for (Facility aulaFacility : facilities) {
            if (aulaFacility != null && aulaFacility.equals(facility)) {
                return true;
            }
        }
        return false;
    }
}
