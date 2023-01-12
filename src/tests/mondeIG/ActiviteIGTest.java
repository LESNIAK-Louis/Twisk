package tests.mondeIG;

import org.junit.jupiter.api.Test;
import twisk.mondeIG.ActiviteIG;
import twisk.mondeIG.Position;

import static org.junit.jupiter.api.Assertions.*;

class ActiviteIGTest {

    @Test
    void testConstructeur(){

        ActiviteIG act = new ActiviteIG("ActiviteTest", "1", 30, 20);
        assertEquals("ActiviteTest", act.getNom());
        assertEquals("1", act.getId());
        assertEquals(30, act.getLargeur());
        assertEquals(20, act.getHauteur());
        assertEquals(act.getPosX() + 15, act.getPointDeControle(Position.HAUT).getPosX());
        assertEquals(act.getPosY(), act.getPointDeControle(Position.HAUT).getPosY());
        assertEquals(act.getPosX() + 15, act.getPointDeControle(Position.BAS).getPosX());
        assertEquals(act.getPosY() + 20, act.getPointDeControle(Position.BAS).getPosY());
        assertEquals(act.getPosX(), act.getPointDeControle(Position.GAUCHE).getPosX());
        assertEquals(act.getPosY() + 10, act.getPointDeControle(Position.GAUCHE).getPosY());
        assertEquals(act.getPosX() + 30, act.getPointDeControle(Position.DROITE).getPosX());
        assertEquals(act.getPosY() + 10, act.getPointDeControle(Position.DROITE).getPosY());

        ActiviteIG act2 = new ActiviteIG("ActiviteTest2", "2", 50, 30);
        assertEquals("ActiviteTest2", act2.getNom());
        assertEquals("2", act2.getId());
        assertEquals(50, act2.getLargeur());
        assertEquals(30, act2.getHauteur());
        assertEquals(act2.getPosX() + 25, act2.getPointDeControle(Position.HAUT).getPosX());
        assertEquals(act2.getPosY(), act2.getPointDeControle(Position.HAUT).getPosY());
        assertEquals(act2.getPosX() + 25, act2.getPointDeControle(Position.BAS).getPosX());
        assertEquals(act2.getPosY() + 30, act2.getPointDeControle(Position.BAS).getPosY());
        assertEquals(act2.getPosX(), act2.getPointDeControle(Position.GAUCHE).getPosX());
        assertEquals(act2.getPosY() + 15, act2.getPointDeControle(Position.GAUCHE).getPosY());
        assertEquals(act2.getPosX() + 50, act2.getPointDeControle(Position.DROITE).getPosX());
        assertEquals(act2.getPosY() + 15, act2.getPointDeControle(Position.DROITE).getPosY());
    }

    @Test
    void testEquals(){

        ActiviteIG act = new ActiviteIG("ActiviteTest", "1", 30, 20);
        ActiviteIG act1 = new ActiviteIG("ActiviteTest", "1", 30, 20);
        assertTrue(act.equals(act1));

        act1 = new ActiviteIG("ActiviteTest2", "1", 50, 30);
        assertFalse(act.equals(act1));

        act1 = new ActiviteIG("ActiviteTest", "2", 50, 30);
        assertFalse(act.equals(act1));
    }
}