package tests.mondeIG;

import org.junit.jupiter.api.Test;
import twisk.mondeIG.*;

import static org.junit.jupiter.api.Assertions.*;

class CourbeIGTest {

    @Test
    void estUneLigneDroite() {

        EtapeIG e1 = new ActiviteIG("e", "1", 50, 30);
        EtapeIG e2 = new ActiviteIG("e", "2", 50, 30);
        PointDeControleIG p1 = new PointDeControleIG("P1", Position.HAUT, e1, 0, 20);
        PointDeControleIG p2 = new PointDeControleIG("P2", Position.GAUCHE, e2, 0, 50);
        CourbeIG arc = new CourbeIG(p1, 0,0,0, 0,p2);
        assertFalse(arc.estUneLigneDroite());
    }

    @Test
    void estUneCourbe() {

        EtapeIG e1 = new ActiviteIG("e", "1", 50, 30);
        EtapeIG e2 = new ActiviteIG("e", "2", 50, 30);
        PointDeControleIG p1 = new PointDeControleIG("P1", Position.HAUT, e1, 0, 20);;
        PointDeControleIG p2 = new PointDeControleIG("P2", Position.GAUCHE, e2, 0, 50);
        CourbeIG arc = new CourbeIG(p1, 0,0,0, 0,p2);
        assertTrue(arc.estUneCourbe());
    }
}