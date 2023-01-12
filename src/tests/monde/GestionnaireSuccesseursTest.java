package tests.monde;

import org.junit.jupiter.api.Test;
import twisk.monde.*;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

class GestionnaireSuccesseursTest {

    @Test
    void testConstructeur(){

        GestionnaireSuccesseurs gs = new GestionnaireSuccesseurs();
        assertEquals(0, gs.nbEtapes());
    }

    @Test
    void ajouter() {

        GestionnaireSuccesseurs gs = new GestionnaireSuccesseurs();
        assertEquals(0, gs.nbEtapes());

        gs.ajouter(new Activite("Activite1"), new Activite("Activite2"));
        assertEquals(2, gs.nbEtapes());

        gs.ajouter(new Guichet("Guichet1"));
        assertEquals(3, gs.nbEtapes());
    }

    @Test
    void testNbEtapes() {

        GestionnaireSuccesseurs gs = new GestionnaireSuccesseurs();
        assertEquals(0, gs.nbEtapes());

        gs.ajouter(new Activite("Activite1"), new Activite("Activite2"));
        assertEquals(2, gs.nbEtapes());

        gs.ajouter(new Activite("Activite3"), new Activite("Activite4"));
        assertEquals(4, gs.nbEtapes());
    }

    @Test
    void getSuccesseur(){

        GestionnaireSuccesseurs gs = new GestionnaireSuccesseurs();
        assertNull(gs.getSuccesseur());

        Activite a1 = new Activite("Activite1");
        gs.ajouter(a1);
        assertEquals(a1, gs.getSuccesseur());
    }

    @Test
    void testIterator() {

        GestionnaireSuccesseurs gs = new GestionnaireSuccesseurs();
        Activite a1 = new Activite("Activite1");
        Activite a2 = new Activite("Activite2");
        Guichet g1 = new Guichet("Guichet1");
        Guichet g2 = new Guichet("Guichet2");
        gs.ajouter(a1, g1, a2, g2);

        Iterator<Etape> ite = gs.iterator();
        assertTrue(ite.hasNext());
        assertTrue(ite.hasNext());
        assertEquals(a1, ite.next());
        assertTrue(ite.hasNext());
        assertEquals(g1, ite.next());
        assertTrue(ite.hasNext());
        assertEquals(a2, ite.next());
        assertTrue(ite.hasNext());
        assertEquals(g2, ite.next());
        assertFalse(ite.hasNext());
    }
}