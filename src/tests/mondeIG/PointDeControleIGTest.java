package tests.mondeIG;

import org.junit.jupiter.api.Test;
import twisk.mondeIG.ActiviteIG;
import twisk.mondeIG.EtapeIG;
import twisk.mondeIG.PointDeControleIG;
import twisk.mondeIG.Position;

import static org.junit.jupiter.api.Assertions.*;

public class PointDeControleIGTest {

    @Test
    void testConstructeur(){

        EtapeIG e1 = new ActiviteIG("e", "1", 50, 30);
        PointDeControleIG point1 = new PointDeControleIG("Point1", Position.GAUCHE, e1, 10, 20);
        assertEquals("Point1", point1.getId());
        assertEquals(10, point1.getPosX());
        assertEquals(20, point1.getPosY());

        EtapeIG e2 = new ActiviteIG("e", "2", 50, 30);
        PointDeControleIG point2 = new PointDeControleIG("Point2", Position.DROITE, e2,40, 30);
        assertEquals("Point2", point2.getId());
        assertEquals(40, point2.getPosX());
        assertEquals(30, point2.getPosY());
    }

    @Test
    void deplacerVers(){

        EtapeIG e1 = new ActiviteIG("e", "1", 50, 30);
        PointDeControleIG p = new PointDeControleIG("Point1", Position.GAUCHE, e1, 10, 20);
        p.deplacerVers(100,100);
        assertEquals(100, p.getPosX());
        assertEquals(100, p.getPosY());

        p.deplacerVers(130,40);
        assertEquals(130, p.getPosX());
        assertEquals(40, p.getPosY());
    }

    @Test
    void equals(){

        EtapeIG e1 = new ActiviteIG("e", "1", 50, 30);
        EtapeIG e2 = new ActiviteIG("e", "2", 50, 30);
        EtapeIG e3 = new ActiviteIG("e", "3", 50, 30);
        PointDeControleIG point1 = new PointDeControleIG("Point1", Position.GAUCHE, e1,10, 20);
        PointDeControleIG point1b = new PointDeControleIG("Point1", Position.DROITE, e2,40, 30);
        assertTrue(point1.equals(point1b));

        PointDeControleIG point2 = new PointDeControleIG("Point2", Position.HAUT, e3,40, 30);
        assertFalse(point1.equals(point2));

    }

}