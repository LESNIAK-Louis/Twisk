package tests.monde;

import org.junit.jupiter.api.Test;
import twisk.monde.*;
import twisk.outils.FabriqueNumero;

import static org.junit.jupiter.api.Assertions.*;

public class SasSortieTest extends ActiviteTest{

    @Test
    void testEstUneEntree(){

        SasSortie a = new SasSortie();
        assertFalse(a.estUneEntree());
    }

    @Test
    void testEstUneSortie(){

        SasSortie a = new SasSortie();
        assertTrue(a.estUneSortie());
    }

    @Test
    void toC()
    {
        FabriqueNumero.getInstance().reset();
        Etape etape = new SasSortie();
        StringBuilder res = new StringBuilder();
        res.append("");

        assertEquals(res.toString(), etape.toC());
    }
}
