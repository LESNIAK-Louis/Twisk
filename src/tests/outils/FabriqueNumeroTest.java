package tests.outils;

import org.junit.jupiter.api.Test;
import twisk.outils.FabriqueNumero;

import static org.junit.jupiter.api.Assertions.*;

class FabriqueNumeroTest {

    @Test
    void getNumeroEtape() {
        FabriqueNumero fn = FabriqueNumero.getInstance();

        fn.reset();

        assertEquals(0, fn.getNumeroEtape());
        assertEquals(1, fn.getNumeroEtape());
    }

    @Test
    void getNumeroSemaphore(){
        FabriqueNumero fn = FabriqueNumero.getInstance();

        fn.reset();

        assertEquals(0, fn.getNumeroEtape());
        assertEquals(1, fn.getNumeroEtape());

        assertEquals(1, fn.getNumeroSemaphore());
        assertEquals(2, fn.getNumeroSemaphore());
    }

    @Test
    void getNumeroLib(){
        FabriqueNumero fn = FabriqueNumero.getInstance();

        fn.reset();

        assertEquals(2, fn.getNumeroLib());
        assertEquals(3, fn.getNumeroLib());

        assertEquals(4, fn.getNumeroLib());
        assertEquals(5, fn.getNumeroLib());
    }

    @Test
    void reset() {
        FabriqueNumero fn = FabriqueNumero.getInstance();

        fn.reset();

        assertEquals(0, fn.getNumeroEtape());
        assertEquals(1, fn.getNumeroEtape());
        assertEquals(1, fn.getNumeroSemaphore());
        assertEquals(2, fn.getNumeroSemaphore());

        fn.reset();

        assertEquals(0, fn.getNumeroEtape());
        assertEquals(1, fn.getNumeroSemaphore());
    }
}