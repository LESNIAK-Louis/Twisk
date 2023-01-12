package tests.monde;

import org.junit.jupiter.api.Test;
import twisk.monde.Activite;
import twisk.monde.ActiviteRestreinte;
import twisk.monde.Guichet;
import twisk.monde.SasSortie;
import twisk.outils.FabriqueNumero;

import static org.junit.jupiter.api.Assertions.*;

class GuichetTest extends EtapeTest {

    @Test
    void testConstructeurSimpleGuichet(){

        Guichet g = new Guichet("GuichetTest");
        assertEquals("GuichetTest", g.getNom());
        assertEquals(4, g.getNbJetons());
    }

    @Test
    void testConstructeurGuichet(){

        Guichet g = new Guichet("GuichetTest", 4);
        assertEquals("GuichetTest", g.getNom());
        assertEquals(4, g.getNbJetons());
    }

    @Test
    void testEstUneActivite(){

        Guichet a = new Guichet("Test", 3);
        assertFalse(a.estUneActivite());
    }

    @Test
    void testEstUnGuichet(){

        Guichet a = new Guichet("Test", 3);
        assertTrue(a.estUnGuichet());
    }

    @Test
    void toC(){

        FabriqueNumero.getInstance().reset();
        Guichet g = new Guichet("Guichet Test", 3);
        ActiviteRestreinte ar = new ActiviteRestreinte("ActRestreinte Test", 4, 2);
        g.ajouterSuccesseur(ar);
        SasSortie sortie = new SasSortie();
        ar.ajouterSuccesseur(sortie);

        StringBuilder res = new StringBuilder();
        res.append("P(ids, SEM_GUICHET_TEST_0);\n");
        res.append("transfert(GUICHET_TEST_0, ACTRESTREINTE_TEST_1);\n");
        res.append("delai(4, 2);\n");
        res.append("V(ids, SEM_GUICHET_TEST_0);\n");
        res.append("transfert(ACTRESTREINTE_TEST_1, SORTIE_2);\n");
        assertEquals(res.toString(), g.toC());

        FabriqueNumero.getInstance().reset();
        g = new Guichet("Guichet Test", 3);
        ar = new ActiviteRestreinte("ActRestreinte Test", 6, 3);
        g.ajouterSuccesseur(ar);
        ar.ajouterSuccesseur(sortie);

        res = new StringBuilder();
        res.append("P(ids, SEM_GUICHET_TEST_0);\n");
        res.append("transfert(GUICHET_TEST_0, ACTRESTREINTE_TEST_1);\n");
        res.append("delai(6, 3);\n");
        res.append("V(ids, SEM_GUICHET_TEST_0);\n");
        res.append("transfert(ACTRESTREINTE_TEST_1, SORTIE_2);\n");
        assertEquals(res.toString(), g.toC());
    }
}