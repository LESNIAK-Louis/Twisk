package tests.monde;

import tests.monde.ActiviteTest;
import twisk.monde.ActiviteRestreinte;
import twisk.outils.FabriqueNumero;

import static org.junit.jupiter.api.Assertions.*;

class ActiviteRestreinteTest extends ActiviteTest {

    @org.junit.jupiter.api.Test
    void toC() {

        FabriqueNumero.getInstance().reset();
        ActiviteRestreinte ar = new ActiviteRestreinte("Activite Test", 4, 2);
        String res = "delai(4, 2);\n";
        assertEquals(res, ar.toC());

        ar = new ActiviteRestreinte("Activite Test", 6, 3);
        res = "delai(6, 3);\n";
        assertEquals(res, ar.toC());
    }
}