package tests.monde;

import org.junit.jupiter.api.Test;
import twisk.monde.Activite;
import twisk.monde.Etape;
import twisk.monde.SasSortie;
import twisk.outils.FabriqueNumero;

import static org.junit.jupiter.api.Assertions.*;

class ActiviteTest extends EtapeTest {

    @Test
    void testConstructeurSimpleActivite(){

        Activite a = new Activite("ActiviteTest");
        assertEquals("ActiviteTest", a.getNom());
        assertEquals(4, a.getTemps());
        assertEquals(2, a.getEcartTemps());
    }

    @Test
    void testConstructeurActivite(){

        Activite a = new Activite("ActiviteTest", 5, 2);
        assertEquals("ActiviteTest", a.getNom());
        assertEquals(5, a.getTemps());
        assertEquals(2, a.getEcartTemps());
    }

    @Test
    void testEstUneActivite(){

        Activite a = new Activite("Test", 5, 2);
        assertTrue(a.estUneActivite());
    }

    @Test
    void testEstUnGuichet(){

        Activite a = new Activite("Test", 5, 2);
        assertFalse(a.estUnGuichet());
    }

    @Test
    void toC()
    {
        FabriqueNumero.getInstance().reset();
        Activite a = new Activite("ActiviteTest", 5, 2);
        int num_act_a = a.getNumero();
        SasSortie sortie = new SasSortie();
        a.ajouterSuccesseur(sortie);

        StringBuilder str = new StringBuilder();
        str.append("delai(5, 2);\n");
        str.append("transfert(ACTIVITETEST_0, SORTIE_1);\n");

        assertEquals(str.toString(), a.toC());

        FabriqueNumero.getInstance().reset();
        a = new Activite("ActiviteTest", 5, 2);
        sortie = new SasSortie();
        Activite a2 = new Activite("ActiviteTest", 6, 3);
        a.ajouterSuccesseur(a2);
        a2.ajouterSuccesseur(sortie);

        str = new StringBuilder();
        str.append("delai(5, 2);\n");
        str.append("transfert(ACTIVITETEST_0, ACTIVITETEST_2);\n");
        str.append("delai(6, 3);\n");
        str.append("transfert(ACTIVITETEST_2, SORTIE_1);\n");

        assertEquals(str.toString(), a.toC());

    }

}