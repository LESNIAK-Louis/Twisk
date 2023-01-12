package tests.mondeIG;

import org.junit.jupiter.api.Test;
import twisk.monde.Etape;
import twisk.mondeIG.ActiviteIG;
import twisk.mondeIG.EtapeIG;
import twisk.mondeIG.GestionnaireSuccesseursIG;
import twisk.mondeIG.GuichetIG;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

class GestionnaireSuccesseursIGTest {

    @Test
    void testConstructeur(){

        GestionnaireSuccesseursIG gs = new GestionnaireSuccesseursIG();
        assertEquals(0, gs.nbEtapes());
    }

    @Test
    void ajouter() {

        GestionnaireSuccesseursIG gs = new GestionnaireSuccesseursIG();
        assertEquals(0, gs.nbEtapes());

        gs.ajouter(new ActiviteIG("Activite1", "1", 50, 50), new ActiviteIG("Activite2", "2", 50, 50));
        assertEquals(2, gs.nbEtapes());

        gs.ajouter(new GuichetIG("Guichet1", "1", 50, 50));
        assertEquals(3, gs.nbEtapes());
    }

    @Test
    void supprimer() {

        GestionnaireSuccesseursIG gs = new GestionnaireSuccesseursIG();
        assertEquals(0, gs.nbEtapes());

        EtapeIG e1 = new ActiviteIG("Activite1", "1", 50, 50);
        EtapeIG e2 = new ActiviteIG("Activite2", "2", 50, 50);
        EtapeIG e3 = new GuichetIG("Guichet1", "1", 50, 50);

        gs.ajouter(e1, e2, e3);

        gs.supprimer(e2);
        assertEquals(2, gs.nbEtapes());
        gs.supprimer(e1, e3);
        assertEquals(0, gs.nbEtapes());
    }

    @Test
    void testNbEtapes() {

        GestionnaireSuccesseursIG gs = new GestionnaireSuccesseursIG();
        assertEquals(0, gs.nbEtapes());

        gs.ajouter(new ActiviteIG("Activite1", "1", 50, 50), new ActiviteIG("Activite2", "2",50, 50));
        assertEquals(2, gs.nbEtapes());

        gs.ajouter(new ActiviteIG("Activite3", "3", 50, 50), new ActiviteIG("Activite4", "4",50, 50));
        assertEquals(4, gs.nbEtapes());
    }

    @Test
    void getSuccesseur(){

        GestionnaireSuccesseursIG gs = new GestionnaireSuccesseursIG();
        assertNull(gs.getSuccesseur());

        ActiviteIG a1 = new ActiviteIG("Activite1", "1", 50, 50);
        gs.ajouter(a1);
        assertEquals(a1, gs.getSuccesseur());
    }

    @Test
    void testIterator() {

        GestionnaireSuccesseursIG gs = new GestionnaireSuccesseursIG();
        ActiviteIG a1 = new ActiviteIG("Activite1","1", 50, 50);
        ActiviteIG a2 = new ActiviteIG("Activite2", "2", 50, 50);
        GuichetIG g1 = new GuichetIG("Guichet1", "1",50, 50);
        GuichetIG g2 = new GuichetIG("Guichet2", "2", 50, 50);
        gs.ajouter(a1, g1, a2, g2);

        Iterator<EtapeIG> ite = gs.iterator();
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