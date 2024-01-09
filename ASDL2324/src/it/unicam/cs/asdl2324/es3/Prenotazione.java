package it.unicam.cs.asdl2324.es3;

/**
 * Una prenotazione riguarda una certa aula per un certo time slot.
 * 
 * @author Luca Tesei
 *
 */
public class Prenotazione implements Comparable<Prenotazione> {

    private final String aula;

    private final TimeSlot timeSlot;

    private String docente;

    private String motivo;

    /**
     * Costruisce una prenotazione.
     * 
     * @param aula
     *                     l'aula a cui la prenotazione si riferisce
     * @param timeSlot
     *                     il time slot della prenotazione
     * @param docente
     *                     il nome del docente che ha prenotato l'aula
     * @param motivo
     *                     il motivo della prenotazione
     * @throws NullPointerException
     *                                  se uno qualsiasi degli oggetti passati è
     *                                  null
     */
    public Prenotazione(String aula, TimeSlot timeSlot, String docente,
            String motivo) {
        // TODO implementare
        if (aula == null || timeSlot == null || docente == null || motivo == null) {
            throw new NullPointerException("Nessuno dei parametri può essere nullo.");
        }
        this.aula = aula;
        this.timeSlot = timeSlot;
        this.docente = docente;
        this.motivo = motivo;
    }

    /**
     * @return the aula
     */
    public String getAula() {
        return aula;
    }

    /**
     * @return the timeSlot
     */
    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    /**
     * @return the docente
     */
    public String getDocente() {
        return docente;
    }

    /**
     * @return the motivo
     */
    public String getMotivo() {
        return motivo;
    }

    /**
     * @param docente
     *                    the docente to set
     */
    public void setDocente(String docente) {
        this.docente = docente;
    }

    /**
     * @param motivo
     *                   the motivo to set
     */
    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    /*
     * Due prenotazioni sono uguali se hanno la stessa aula e lo stesso time
     * slot. Il docente e il motivo possono cambiare senza influire
     * sull'uguaglianza.
     */
    @Override
    public boolean equals(Object obj) {
        // TODO implementare
        if (this == obj) {
            return true; // Sono lo stesso oggetto
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false; // L'oggetto passato non è una istanza di Prenotazione
        }

        Prenotazione other = (Prenotazione) obj; // Effettua il cast dell'oggetto a Prenotazione

        // Verifica se hanno la stessa aula e lo stesso time slot
        return this.aula.equals(other.aula) && this.timeSlot.equals(other.timeSlot);
    }

    /*
     * L'hashcode di una prenotazione si calcola a partire dai due campi usati
     * per equals.
     */
    @Override
    public int hashCode() {
        int result = 17; // Inizializza con un numero primo arbitrario
        result = 31 * result + aula.hashCode();
        result = 31 * result + timeSlot.hashCode();
        return result;
    }

    /*
     * Una prenotazione precede un altra in base all'ordine dei time slot. Se
     * due prenotazioni hanno lo stesso time slot allora una precede l'altra in
     * base all'ordine tra le aule.
     */
    @Override
    public int compareTo(Prenotazione o) {
        // TODO implementare
        if (this.equals(o)) {
            return 0; // Sono uguali
        }

        int compareTimeSlots = this.timeSlot.compareTo(o.timeSlot);
        if (compareTimeSlots != 0) {
            return compareTimeSlots; // Confronto basato sui time slot
        }

        return this.aula.compareTo(o.aula); // Confronto basato sulle aule
    }

    @Override
    public String toString() {
        return "Prenotazione [aula = " + aula + ", time slot =" + timeSlot
                + ", docente=" + docente + ", motivo=" + motivo + "]";
    }

}
