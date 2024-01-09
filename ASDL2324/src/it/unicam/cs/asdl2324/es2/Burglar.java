package it.unicam.cs.asdl2324.es2;

/**
 * Uno scassinatore è un oggetto che prende una certa cassaforte e trova la
 * combinazione utilizzando la "forza bruta".
 *
 * @author Luca Tesei
 */
public class Burglar {

    // TODO inserire le variabili istanza che servono
    private String forcedCombination;
    private CombinationLock combinationLock;

    private long attempts;

    private boolean foundCombination;

    /**
     * Costruisce uno scassinatore per una certa cassaforte.
     *
     * @param aCombinationLock
     * @throw NullPointerException se la cassaforte passata è nulla
     */
    public Burglar(CombinationLock aCombinationLock) {
        // TODO implementare
        if (aCombinationLock == null)
            throw new NullPointerException("La cassaforte passata è nulla");
        combinationLock = aCombinationLock;
    }

    /**
     * Forza la cassaforte e restituisce la combinazione.
     *
     * @return la combinazione della cassaforte forzata.
     */
    public String findCombination() {
        // TODO implementare
        StringBuilder forcedCombination = new StringBuilder();
        for (char firstLetter = 'A'; firstLetter <= 'Z' && !foundCombination; firstLetter++) {
            for (char secondLetter = 'A'; secondLetter <= 'Z' && !foundCombination; secondLetter++) {
                for (char thirdLetter = 'A'; thirdLetter <= 'Z' && !foundCombination; thirdLetter++) {

                    // Impostiamo la combinazione attuale sulla cassaforte
                    combinationLock.setPosition(firstLetter);
                    combinationLock.setPosition(secondLetter);
                    combinationLock.setPosition(thirdLetter);

                    combinationLock.open();

                    attempts++;

                    // Se la cassaforte si apre, abbiamo trovato la combinazione
                    if (combinationLock.isOpen()) {
                        foundCombination = true;
                        forcedCombination.append(firstLetter).append(secondLetter).append(thirdLetter);
                    }
                }
            }
        }
        return forcedCombination.toString();
    }

    /**
     * Restituisce il numero di tentativi che ci sono voluti per trovare la
     * combinazione. Se la cassaforte non è stata ancora forzata restituisce -1.
     *
     * @return il numero di tentativi che ci sono voluti per trovare la
     * combinazione, oppure -1 se la cassaforte non è stata ancora
     * forzata.
     */
    public long getAttempts() {
        // TODO implementare
        return foundCombination ? attempts : -1;
    }
}
