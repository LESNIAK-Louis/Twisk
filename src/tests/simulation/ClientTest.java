package tests.simulation;

import org.junit.jupiter.api.Test;
import twisk.monde.*;
import twisk.outils.FabriqueNumero;
import twisk.simulation.Client;

import static org.junit.jupiter.api.Assertions.*;

class ClientTest {

    @Test
    void allerA() {

        FabriqueNumero.getInstance().reset();
        Activite a = new Activite("activite");
        Client c = new Client(0);
        c.allerA(a, 1);

        assertEquals(0, c.getNumero());
        assertEquals(1, c.getRang());
        assertEquals(a, c.getEtape());
        assertEquals(1, c.getTempsDansMonde());
        assertEquals(1, c.getTempsDansActivite());
        assertEquals(0, c.getTempsAttenteGuichet());

        Guichet g = new Guichet("guichet");
        c.allerA(g, 2);
        assertEquals(2, c.getRang());
        assertEquals(g, c.getEtape());
        assertEquals(2, c.getTempsDansMonde());
        assertEquals(1, c.getTempsDansActivite());
        assertEquals(1, c.getTempsAttenteGuichet());

        ActiviteRestreinte ar = new ActiviteRestreinte("activite restreinte");
        c.allerA(ar, 5);
        assertEquals(5, c.getRang());
        assertEquals(ar, c.getEtape());
        assertEquals(3, c.getTempsDansMonde());
        assertEquals(2, c.getTempsDansActivite());
        assertEquals(1, c.getTempsAttenteGuichet());

        SasEntree entree = new SasEntree();
        c.allerA(entree, 0);
        assertEquals(0, c.getRang());
        assertEquals(entree, c.getEtape());
        assertEquals(3, c.getTempsDansMonde());
        assertEquals(2, c.getTempsDansActivite());
        assertEquals(1, c.getTempsAttenteGuichet());

        SasSortie sortie = new SasSortie();
        c.allerA(sortie, 0);
        assertEquals(0, c.getRang());
        assertEquals(sortie, c.getEtape());
        assertEquals(3, c.getTempsDansMonde());
        assertEquals(2, c.getTempsDansActivite());
        assertEquals(1, c.getTempsAttenteGuichet());
    }

}