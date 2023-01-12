package tests.monde;

import org.junit.jupiter.api.Test;
import twisk.monde.*;
import twisk.outils.FabriqueNumero;

import static org.junit.jupiter.api.Assertions.*;

public class SasEntreeTest extends ActiviteTest{

    @Test
    void testEstUneEntree(){

        SasEntree a = new SasEntree();
        assertTrue(a.estUneEntree());
        
    }

    @Test
    void testEstUneSortie(){

        SasEntree a = new SasEntree();
        assertFalse(a.estUneSortie());
    }

    @Test
    void toC()
    {
        FabriqueNumero.getInstance().reset();
        Etape etape = new SasEntree();
        etape.ajouterSuccesseur(new SasSortie());
        StringBuilder res = new StringBuilder();
        res.append("entrer(ENTREE_0);\n");
        res.append("delaiLoi(0, 4.0, 2.0);\n");
        res.append("transfert(ENTREE_0, SORTIE_1);\n");

        assertEquals(res.toString(), etape.toC() );
    }
}
