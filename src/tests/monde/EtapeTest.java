package tests.monde;

import org.junit.jupiter.api.Test;
import twisk.monde.*;
import twisk.outils.FabriqueNumero;

import static org.junit.jupiter.api.Assertions.*;

class EtapeTest {

    @Test
    void ajouterSuccesseur() {
        Etape g1 = new Guichet("g1");
        assertEquals(0,g1.nbSuccesseurs());

        Etape a1 = new ActiviteRestreinte("a1");
        g1.ajouterSuccesseur(a1);
        assertEquals(1,g1.nbSuccesseurs());

        Etape g2 = new Guichet("g2");
        g1.ajouterSuccesseur(g2);
        assertEquals(2,g1.nbSuccesseurs());

        Etape g3 = new Guichet("g3");
        Etape g4 = new Guichet("g4");
        Etape a2 = new Guichet("a2");
        g1.ajouterSuccesseur(g3,g4,a2);
        assertEquals(5,g1.nbSuccesseurs());

    }

    @Test
    void getNomDefine(){

        FabriqueNumero.getInstance().reset();
        Etape e = new Activite("éàùçïâ");
        assertEquals("EAUCIA_0", e.getNomDefine());

        e = new Activite("Test nom compliqué");
        assertEquals("TEST_NOM_COMPLIQUE_1", e.getNomDefine());

        e = new Activite("Test nom compliqué");
        assertEquals("TEST_NOM_COMPLIQUE_2", e.getNomDefine());
    }

    @Test
    void equals(){
        Activite a1 = new Activite("Activite");
        Activite a2 = new Activite("Activite");
        assertTrue(a1.equals(a1));
        assertFalse(a1.equals(a2));
    }

}