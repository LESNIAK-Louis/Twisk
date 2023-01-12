package tests.mondeIG;

import org.junit.jupiter.api.Test;
import twisk.mondeIG.*;

import static org.junit.jupiter.api.Assertions.*;

public class LigneDroiteIGTest {

    @Test
    void testConstructeur(){

        EtapeIG e1 = new ActiviteIG("e", "1", 50, 30);
        EtapeIG e2 = new ActiviteIG("e", "2", 50, 30);
        PointDeControleIG p1 = new PointDeControleIG("P1", Position.GAUCHE, e1, 0, 50);
        PointDeControleIG p2 = new PointDeControleIG("P2", Position.DROITE, e2,0, 50);
        LigneDroiteIG arc = new LigneDroiteIG(p1, p2);

        assertEquals(p1, arc.getP1());
        assertEquals(p2, arc.getP2());

        p1 = new PointDeControleIG("P1", Position.GAUCHE, e1,30, 10);
        p2 = new PointDeControleIG("Point2", Position.DROITE, e2,60, 70);
        arc = new LigneDroiteIG(p1, p2);

        assertEquals(p1, arc.getP1());
        assertEquals(p2, arc.getP2());
    }

    @Test
    void estUneLigneDroite() {

        EtapeIG e1 = new ActiviteIG("e", "1", 50, 30);
        EtapeIG e2 = new ActiviteIG("e", "2", 50, 30);
        PointDeControleIG p1 = new PointDeControleIG("P1", Position.GAUCHE, e1, 0, 20);
        PointDeControleIG p2 = new PointDeControleIG("P2", Position.DROITE, e2, 0, 50);
        LigneDroiteIG arc = new LigneDroiteIG(p1, p2);
        assertTrue(arc.estUneLigneDroite());
    }

    @Test
    void estUneCourbe() {

        EtapeIG e1 = new ActiviteIG("e", "1", 50, 30);
        EtapeIG e2 = new ActiviteIG("e", "2", 50, 30);
        PointDeControleIG p1 = new PointDeControleIG("P1", Position.GAUCHE, e1, 0, 20);
        PointDeControleIG p2 = new PointDeControleIG("P2", Position.DROITE, e2, 0, 50);
        LigneDroiteIG arc = new LigneDroiteIG(p1, p2);
        assertFalse(arc.estUneCourbe());
    }

    @Test
    void equals(){

        EtapeIG e1 = new ActiviteIG("e", "1", 50, 30);
        EtapeIG e2 = new ActiviteIG("e", "2", 50, 30);
        EtapeIG e3 = new ActiviteIG("e", "3", 50, 30);
        PointDeControleIG p1 = new PointDeControleIG("P1", Position.GAUCHE, e1,0, 20);
        PointDeControleIG p2 = new PointDeControleIG("P2", Position.DROITE, e2,0, 50);
        LigneDroiteIG arc1 = new LigneDroiteIG(p1, p2);
        LigneDroiteIG arc2 = new LigneDroiteIG(p1, p2);
        assertTrue(arc1.equals(arc2));

        PointDeControleIG p3 = new PointDeControleIG("P3", Position.DROITE, e2,30, 10);
        LigneDroiteIG arc3 = new LigneDroiteIG(p1, p3);
        assertTrue(arc1.equals(arc3));

        PointDeControleIG p4 = new PointDeControleIG("P3", Position.DROITE, e3,30, 10);
        LigneDroiteIG arc4 = new LigneDroiteIG(p1, p4);
        assertFalse(arc1.equals(arc4));
    }
}