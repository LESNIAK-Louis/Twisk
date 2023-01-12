package tests.outils;

import org.junit.jupiter.api.Test;
import twisk.outils.FabriqueIdentifiant;

import static org.junit.jupiter.api.Assertions.*;

class FabriqueIdentifiantTest {

    @Test
    void getIdentifiantEtape() {

        FabriqueIdentifiant.getInstance().reset();
        String id = FabriqueIdentifiant.getInstance().getIdentifiantEtape();
        assertEquals("1", id);
        id = FabriqueIdentifiant.getInstance().getIdentifiantEtape();
        assertEquals("2", id);
        id = FabriqueIdentifiant.getInstance().getIdentifiantEtape();
        assertEquals("3", id);
        id = FabriqueIdentifiant.getInstance().getIdentifiantEtape();
        id = FabriqueIdentifiant.getInstance().getIdentifiantEtape();
        assertEquals("5", id);
    }

    @Test
    void reset() {

        FabriqueIdentifiant.getInstance().reset();
        String id = FabriqueIdentifiant.getInstance().getIdentifiantEtape();
        id = FabriqueIdentifiant.getInstance().getIdentifiantEtape();
        id = FabriqueIdentifiant.getInstance().getIdentifiantEtape();
        id = FabriqueIdentifiant.getInstance().getIdentifiantEtape();
        id = FabriqueIdentifiant.getInstance().getIdentifiantEtape();
        FabriqueIdentifiant.getInstance().reset();
        id = FabriqueIdentifiant.getInstance().getIdentifiantEtape();
        assertEquals("1", id);
    }
}