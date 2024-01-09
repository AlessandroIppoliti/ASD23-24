package it.unicam.cs.asdl2324.es2;

/**
 * Un oggetto cassaforte con combinazione ha una manopola che può essere
 * impostata su certe posizioni contrassegnate da lettere maiuscole. La
 * serratura si apre solo se le ultime tre lettere impostate sono uguali alla
 * combinazione segreta.
 *
 * @author Luca Tesei
 */
public class CombinationLock {

    // TODO inserire le variabili istanza che servono

    private String secretCombination;
    private String currentCombination;
    private boolean open;

    /**
     * Costruisce una cassaforte <b>aperta</b> con una data combinazione
     *
     * @param aCombination la combinazione che deve essere una stringa di 3
     *                     lettere maiuscole dell'alfabeto inglese
     * @throw IllegalArgumentException se la combinazione fornita non è una
     * stringa di 3 lettere maiuscole dell'alfabeto inglese
     * @throw NullPointerException se la combinazione fornita è nulla
     */

    public CombinationLock(String aCombination) {
        // TODO implementare
        // Verifica se la combinazione fornita è nulla e lancia un'eccezione se lo è
        if (aCombination == null) throw new NullPointerException("La combinazione fornita è nulla");

        // Verifica se la combinazione fornita ha una lunghezza di 3 lettere maiuscole
        if (aCombination.length() != 3 || !aCombination.matches("[A-Z]{3}"))
            throw new IllegalArgumentException("La combinazione fornita non è una stringa di 3 lettere maiuscole dell'alfabeto inglese");

        secretCombination = aCombination;
        currentCombination = "";
        open = true;

    }

    /**
     * Imposta la manopola su una certaposizione.
     *
     * @param aPosition un carattere lettera maiuscola su cui viene
     *                  impostata la manopola
     * @throws IllegalArgumentException se il carattere fornito non è una
     *                                  lettera maiuscola dell'alfabeto
     *                                  inglese
     */
    public void setPosition(char aPosition) {
        // TODO implementare

        // Verifica se il carattere fornito è maiuscolo
        if (!Character.isUpperCase(aPosition))
            throw new IllegalArgumentException("Non è una lettera maiuscola");
        currentCombination += aPosition;

        // Se la lunghezza della posizione attuale supera 3 caratteri,
        // rimuovi il primo carattere per mantenere solo le ultime tre posizioni
        if (currentCombination.length() > 3)
            currentCombination = currentCombination.substring(currentCombination.length() - 3);
    }

    /**
     * Tenta di aprire la serratura considerando come combinazione fornita le
     * ultime tre posizioni impostate. Se l'apertura non va a buon fine le
     * lettere impostate precedentemente non devono essere considerate per i
     * prossimi tentativi di apertura.
     */
    public void open() {
        // TODO implementare
        // Verifica se la combinazione attuale (ultime tre posizioni impostate) coincide con la combinazione segreta
        if (currentCombination.equals(secretCombination)) {
            // La cassaforte è stata aperta con successo
            open = true;
        } else {
            // La combinazione attuale non coincide con la combinazione segreta
            // Quindi, la cassaforte non si apre e resettiamo la posizione attuale
            currentCombination = "";
            open = false;
        }
    }

    /**
     * Determina se la cassaforte è aperta.
     *
     * @return true se la cassaforte è attualmente aperta, false altrimenti
     */
    public boolean isOpen() {
        // TODO implementare
        if (open) return true;
        return false;
    }

    /**
     * Chiude la cassaforte senza modificare la combinazione attuale. Fa in modo
     * che se si prova a riaprire subito senza impostare nessuna nuova posizione
     * della manopola la cassaforte non si apre. Si noti che se la cassaforte
     * era stata aperta con la combinazione giusta le ultime posizioni impostate
     * sono proprio la combinazione attuale.
     */
    public void lock() {
        // TODO implementare
        open = false;
        currentCombination = "";
    }

    /**
     * Chiude la cassaforte e modifica la combinazione. Funziona solo se la
     * cassaforte è attualmente aperta. Se la cassaforte è attualmente chiusa
     * rimane chiusa e la combinazione non viene cambiata, ma in questo caso le
     * le lettere impostate precedentemente non devono essere considerate per i
     * prossimi tentativi di apertura.
     *
     * @param aCombination la nuova combinazione che deve essere una stringa
     *                     di 3 lettere maiuscole dell'alfabeto inglese
     * @throw IllegalArgumentException se la combinazione fornita non è una
     * stringa di 3 lettere maiuscole dell'alfabeto inglese
     * @throw NullPointerException se la combinazione fornita è nulla
     */
    public void lockAndChangeCombination(String aCombination) {
        // TODO implementare
        // Verifica se la combinazione fornita è nulla e lancia un'eccezione se lo è
        if (aCombination == null) throw new NullPointerException("La combinazione fornita è nulla");

        // Verifica se la combinazione fornita ha una lunghezza di 3 lettere maiuscole
        if (aCombination.length() != 3 || !aCombination.matches("[A-Z]{3}"))
            throw new IllegalArgumentException("La combinazione fornita non è una stringa di 3 lettere maiuscole dell'alfabeto inglese");

        if (isOpen()) {
            open = false;
            secretCombination = aCombination;
            currentCombination = "";
        } else {
            currentCombination = "";
        }
    }


}