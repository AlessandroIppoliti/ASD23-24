/**
 *
 */
package it.unicam.cs.asdl2324.es3;

// TODO completare gli import se necessario

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

/**
 * Un time slot è un intervallo di tempo continuo che può essere associato ad
 * una prenotazione. Gli oggetti della classe sono immutabili. Non sono ammessi
 * time slot che iniziano e finiscono nello stesso istante.
 *
 * @author Luca Tesei
 *
 */
public class TimeSlot implements Comparable<TimeSlot> {

    /**
     * Rappresenta la soglia di tolleranza da considerare nella sovrapposizione
     * di due Time Slot. Se si sovrappongono per un numero di minuti minore o
     * uguale a questa soglia allora NON vengono considerati sovrapposti.
     */
    public static final int MINUTES_OF_TOLERANCE_FOR_OVERLAPPING = 5;

    private final GregorianCalendar start;

    private final GregorianCalendar stop;

    /**
     * Crea un time slot tra due istanti di inizio e fine
     *
     * @param start
     *                  inizio del time slot
     * @param stop
     *                  fine del time slot
     * @throws NullPointerException
     *                                      se uno dei due istanti, start o
     *                                      stop, è null
     * @throws IllegalArgumentException
     *                                      se start è uguale o successivo a
     *                                      stop
     */
    public TimeSlot(GregorianCalendar start, GregorianCalendar stop) {
        // TODO implementare
        if (start == null || stop == null)
            throw new NullPointerException("Il valore non può essere nullo!");
        if (start.equals(stop) || start.after(stop))
            throw new IllegalArgumentException("Il valore non può essere successivo a stop!");
        this.start = start;
        this.stop = stop;
    }

    /**
     * @return the start
     */
    public GregorianCalendar getStart() {
        return start;
    }

    /**
     * @return the stop
     */
    public GregorianCalendar getStop() {
        return stop;
    }

    /*
     * Un time slot è uguale a un altro se rappresenta esattamente lo stesso
     * intervallo di tempo, cioè se inizia nello stesso istante e termina nello
     * stesso istante.
     */
    @Override
    public boolean equals(Object obj) {
        // TODO implementare
        if (this == obj) {
            return true; // Sono lo stesso oggetto
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false; // L'oggetto passato non è una istanza di TimeSlot
        }

        TimeSlot other = (TimeSlot) obj; // Effettua il cast dell'oggetto a TimeSlot

        // Verifica se inizia e termina nello stesso istante
        return this.start.equals(other.start) && this.stop.equals(other.stop);
    }

    /*
     * Il codice hash associato a un timeslot viene calcolato a partire dei due
     * istanti di inizio e fine, in accordo con i campi usati per il metodo
     * equals.
     */
    @Override
    public int hashCode() {
        // TODO implementare
        int result = 17; // Inizializza con un numero primo arbitrario
        result = 31 * result + start.hashCode();
        result = 31 * result + stop.hashCode();
        return result;
    }

    /*
     * Un time slot precede un altro se inizia prima. Se due time slot iniziano
     * nello stesso momento quello che finisce prima precede l'altro. Se hanno
     * stesso inizio e stessa fine sono uguali, in compatibilità con equals.
     */
    @Override
    public int compareTo(TimeSlot o) {
        // TODO implementare
        if (this.equals(o)) {
            return 0; // Sono uguali
        } else if (this.start.before(o.start) || (this.start.equals(o.start) && this.stop.before(o.stop))) {
            return -1; // Questo time slot precede o
        } else {
            return 1; // Questo time slot segue o
        }
    }

    /**
     * Determina il numero di minuti di sovrapposizione tra questo timeslot e
     * quello passato.
     *
     * @param o
     *              il time slot da confrontare con questo
     * @return il numero di minuti di sovrapposizione tra questo time slot e
     *         quello passato, oppure -1 se non c'è sovrapposizione. Se questo
     *         time slot finisce esattamente al millisecondo dove inizia il time
     *         slot <code>o</code> non c'è sovrapposizione, così come se questo
     *         time slot inizia esattamente al millisecondo in cui finisce il
     *         time slot <code>o</code>. In questi ultimi due casi il risultato
     *         deve essere -1 e non 0. Nel caso in cui la sovrapposizione non è
     *         di un numero esatto di minuti, cioè ci sono secondi e
     *         millisecondi che avanzano, il numero dei minuti di
     *         sovrapposizione da restituire deve essere arrotondato per difetto
     * @throws NullPointerException
     *                                      se il time slot passato è nullo
     * @throws IllegalArgumentException
     *                                      se i minuti di sovrapposizione
     *                                      superano Integer.MAX_VALUE
     */
    public int getMinutesOfOverlappingWith(TimeSlot o) {
        // TODO implementare
        if (o == null) {
            throw new NullPointerException("Il time slot passato è nullo");
        }

        long thisStartTime = this.start.getTimeInMillis();
        long thisStopTime = this.stop.getTimeInMillis();
        long otherStartTime = o.start.getTimeInMillis();
        long otherStopTime = o.stop.getTimeInMillis();

        // Calcola il minimo tra gli istanti di fine
        long minStop = Math.min(thisStopTime, otherStopTime);

        // Calcola il massimo tra gli istanti di inizio
        long maxStart = Math.max(thisStartTime, otherStartTime);

        if (minStop <= maxStart) {
            // Non c'è sovrapposizione
            return -1;
        }

        // Calcola la sovrapposizione in millisecondi
        long overlappingMillis = minStop - maxStart;

        // Converti i millisecondi in minuti, arrotondando per difetto
        int overlappingMinutes = (int) (overlappingMillis / (60 * 1000));

        if (overlappingMinutes < 0) {
            throw new IllegalArgumentException("I minuti di sovrapposizione superano Integer.MAX_VALUE");
        }

        return overlappingMinutes;
    }

    /**
     * Determina se questo time slot si sovrappone a un altro time slot dato,
     * considerando la soglia di tolleranza.
     *
     * @param o
     *              il time slot che viene passato per il controllo di
     *              sovrapposizione
     * @return true se questo time slot si sovrappone per più (strettamente) di
     *         MINUTES_OF_TOLERANCE_FOR_OVERLAPPING minuti a quello passato
     * @throws NullPointerException
     *                                  se il time slot passato è nullo
     */
    public boolean overlapsWith(TimeSlot o) {
        // TODO implementare
        if (o == null) {
            throw new NullPointerException("Il time slot passato è nullo.");
        }

        long start1 = this.start.getTimeInMillis();
        long stop1 = this.stop.getTimeInMillis();
        long start2 = o.start.getTimeInMillis();
        long stop2 = o.stop.getTimeInMillis();

        long overlapStart = Math.max(start1, start2);
        long overlapStop = Math.min(stop1, stop2);

        long overlapDurationInMillis = overlapStop - overlapStart;

        if (overlapDurationInMillis > MINUTES_OF_TOLERANCE_FOR_OVERLAPPING * 60 * 1000) {
            return true; // Si sovrappongono per più di MINUTES_OF_TOLERANCE_FOR_OVERLAPPING minuti
        }

        return false; // Non si sovrappongono o la sovrapposizione è inferiore alla soglia
    }

    /*
     * Ridefinisce il modo in cui viene reso un TimeSlot con una String.
     *
     * Esempio 1, stringa da restituire: "[4/11/2019 11.0 - 4/11/2019 13.0]"
     *
     * Esempio 2, stringa da restituire: "[10/11/2019 11.15 - 10/11/2019 23.45]"
     *
     * I secondi e i millisecondi eventuali non vengono scritti.
     */
    @Override
    public String toString() {
        // TODO implementare
        SimpleDateFormat dateFormat = new SimpleDateFormat("d/M/yyyy H.m");
        String startTime = dateFormat.format(start.getTime());
        String stopTime = dateFormat.format(stop.getTime());
        return "[" + startTime + " - " + stopTime + "]";
    }

}
