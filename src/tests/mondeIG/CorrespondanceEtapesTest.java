package tests.mondeIG;

import org.junit.jupiter.api.Test;
import twisk.monde.Activite;
import twisk.monde.Etape;
import twisk.monde.Guichet;
import twisk.mondeIG.ActiviteIG;
import twisk.mondeIG.CorrespondanceEtapes;
import twisk.mondeIG.EtapeIG;
import twisk.mondeIG.GuichetIG;

import static org.junit.jupiter.api.Assertions.*;

class CorrespondanceEtapesTest {

    @Test
    void ajouter() {

        CorrespondanceEtapes ce = new CorrespondanceEtapes();
        EtapeIG etig1 = new ActiviteIG("Activite1", "1", 50, 50);
        EtapeIG etig2 = new ActiviteIG("Activite2", "2", 50, 50);
        EtapeIG etig3 = new GuichetIG("Guichet1", "3", 50, 50);

        Etape et1 = new Activite("Activite1");
        Etape et2 = new Activite("Activite2");
        Etape et3 = new Guichet("Guichet1");

        assertEquals(0, ce.getNbEtapes());
        ce.ajouter(etig1, et1);
        assertEquals(1, ce.getNbEtapes());
        ce.ajouter(etig2, et2);
        assertEquals(2, ce.getNbEtapes());
        ce.ajouter(etig3, et3);
        assertEquals(3, ce.getNbEtapes());
    }

    @Test
    void get() {

        CorrespondanceEtapes ce = new CorrespondanceEtapes();
        EtapeIG etig1 = new ActiviteIG("Activite1", "1", 50, 50);
        EtapeIG etig2 = new ActiviteIG("Activite2", "2", 50, 50);
        EtapeIG etig3 = new GuichetIG("Guichet1", "3", 50, 50);

        Etape et1 = new Activite("Activite1");
        Etape et2 = new Activite("Activite2");
        Etape et3 = new Guichet("Guichet1");

        ce.ajouter(etig1, et1);
        ce.ajouter(etig2, et2);
        ce.ajouter(etig3, et3);

        assertEquals(et1, ce.get(etig1));
        assertEquals(et2, ce.get(etig2));
        assertEquals(et3, ce.get(etig3));
    }
}