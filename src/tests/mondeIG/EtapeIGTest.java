package tests.mondeIG;

import org.junit.jupiter.api.Test;
import twisk.mondeIG.ActiviteIG;
import twisk.mondeIG.Position;

import static org.junit.jupiter.api.Assertions.*;

class EtapeIGTest {

    @Test
    void deplacerVers() {

        ActiviteIG a1 = new ActiviteIG("activite", "1", 100, 50);
        a1.deplacerVers(100, 100);
        assertEquals(150, a1.getPointDeControle(Position.HAUT).getPosX());
        assertEquals(100, a1.getPointDeControle(Position.HAUT).getPosY());
        assertEquals(150, a1.getPointDeControle(Position.BAS).getPosX());
        assertEquals(150, a1.getPointDeControle(Position.BAS).getPosY());
        assertEquals(100, a1.getPointDeControle(Position.GAUCHE).getPosX());
        assertEquals(125, a1.getPointDeControle(Position.GAUCHE).getPosY());
        assertEquals(200, a1.getPointDeControle(Position.DROITE).getPosX());
        assertEquals(125, a1.getPointDeControle(Position.DROITE).getPosY());
    }
}