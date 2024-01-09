/**
 * 
 */
package it.unicam.cs.asdl2324.es1;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * @author Template: Luca Tesei, Implementation: Collettiva da Esercitazione a
 *         Casa
 *
 */
class EquazioneSecondoGradoModificabileConRisolutoreTest {
    /*
     * Costante piccola per il confronto di due numeri double
     */
    static final double EPSILON = 1.0E-15;

    @Test
    final void testEquazioneSecondoGradoModificabileConRisolutore() {
        // controllo che il valore 0 su a lanci l'eccezione
        assertThrows(IllegalArgumentException.class,
                () -> new EquazioneSecondoGradoModificabileConRisolutore(0, 1,
                        1));
        // devo controllare che comunque nel caso normale il costruttore
        // funziona
        EquazioneSecondoGradoModificabileConRisolutore eq = new EquazioneSecondoGradoModificabileConRisolutore(
                1, 1, 1);
        // Controllo che all'inizio l'equazione non sia risolta
        assertFalse(eq.isSolved());
    }

    @Test
    final void testGetA() {
        double x = 10;

        assertThrows(IllegalArgumentException.class,
                () -> new EquazioneSecondoGradoModificabileConRisolutore(0, 1,
                        1));
        EquazioneSecondoGradoModificabileConRisolutore e1 = new EquazioneSecondoGradoModificabileConRisolutore(
                x, 1, 1);// controllo che il valore 0 su a lanci l'eccezione
        // controllo che il valore restituito sia quello che ho messo
        // all'interno
        // dell'oggetto
        assertTrue(x == e1.getA());
        // in generale si dovrebbe usare assertTrue(Math.abs(x -
        // e1.getA())<EPSILON) ma in
        // questo caso il valore che testiamo non ha subito manipolazioni quindi
        // la sua rappresentazione sarà la stessa di quella inserita nel
        // costruttore senza errori di approssimazione

    }

    @Test
    final void testSetA() {
        // TODO implementare
        double x = 12;
        EquazioneSecondoGradoModificabileConRisolutore e1 = new EquazioneSecondoGradoModificabileConRisolutore(
                1, 1, 1);
        e1.setA(x);
        assertTrue(x == e1.getA());
        // fail("Test non implementato");
    }

    @Test
    final void testGetB() {
        // TODO implementare
        double x = 10;
        EquazioneSecondoGradoModificabileConRisolutore e1 = new EquazioneSecondoGradoModificabileConRisolutore(
                1, x, 1);
        // controllo che il valore restituito sia quello che ho messo
        // all'interno
        // dell'oggetto
        assertTrue(x == e1.getB());
        // fail("Test non implementato");
    }

    @Test
    final void testSetB() {
        // TODO implementare
        double x = 12;
        EquazioneSecondoGradoModificabileConRisolutore e1 = new EquazioneSecondoGradoModificabileConRisolutore(
                1, 1, 1);
        e1.setB(x);
        assertTrue(x == e1.getB());
        //fail("Test non implementato");
    }

    @Test
    final void testGetC() {
        // TODO implementare
        double x = 10;
        EquazioneSecondoGradoModificabileConRisolutore e1 = new EquazioneSecondoGradoModificabileConRisolutore(
                1, 1, x);
        // controllo che il valore restituito sia quello che ho messo
        // all'interno
        // dell'oggetto
        assertTrue(x == e1.getC());
        // fail("Test non implementato");
    }

    @Test
    final void testSetC() {
        // TODO implementare
        double x = 12;
        EquazioneSecondoGradoModificabileConRisolutore e1 = new EquazioneSecondoGradoModificabileConRisolutore(
                1, 1, 1);
        e1.setC(x);
        assertTrue(x == e1.getC());
        //fail("Test non implementato");
    }

    @Test
    final void testIsSolved() {
        // TODO implementare
        EquazioneSecondoGradoModificabileConRisolutore e1 = new EquazioneSecondoGradoModificabileConRisolutore(
                1, 1, 1);
        assertFalse(e1.isSolved());
        e1.solve();
        assertTrue(e1.isSolved());
        e1.setB(9);
        assertFalse(e1.isSolved());
        e1.solve();
        assertTrue(e1.isSolved());
        //fail("Test non implementato");
    }

    @Test
    final void testSolve() {
        EquazioneSecondoGradoModificabileConRisolutore e3 = new EquazioneSecondoGradoModificabileConRisolutore(
                1, 1, 3);
        // controllo semplicemente che la chiamata a solve() non generi errori
        e3.solve();
        // i test con i valori delle soluzioni vanno fatti nel test del metodo
        // getSolution()
    }

    @Test
    final void testGetSolution() {
        // TODO implementare
        EquazioneSecondoGradoModificabileConRisolutore e3 = new EquazioneSecondoGradoModificabileConRisolutore(
                1, 1, 3);
        // controllo che il valore 0 su a lanci l'eccezione
        assertThrows(IllegalStateException.class,
                () -> e3.getSolution());
        e3.solve();
        assertTrue(e3.isSolved());
        EquazioneSecondoGrado e1 = new EquazioneSecondoGrado(1,2,1);
        SoluzioneEquazioneSecondoGrado s = new SoluzioneEquazioneSecondoGrado(e1, -1);
        assertTrue(s.equals(e3.getSolution()));

        //fail("Test non implementato");
    }

}
