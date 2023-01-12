package tests.simulation;

import org.junit.jupiter.api.Test;
import twisk.monde.Activite;
import twisk.simulation.Client;
import twisk.simulation.GestionnaireClients;
import static org.junit.jupiter.api.Assertions.*;

public class GestionnaireClientTest {

    @Test
    void setClients(){

        GestionnaireClients gc = new GestionnaireClients();
        int[] tabClients = new int[4];
        for(int i = 0; i < 4; i++){
            tabClients[i] = 150+i;
        }
        gc.setClients(tabClients);
        int i = 0;
        for(Client c : gc){
            assertEquals(150+i, c.getNumero());
            i++;
        }
    }

    @Test
    void allerA(){

        GestionnaireClients gc = new GestionnaireClients();
        int[] tabClients = new int[4];
        for(int i = 0; i < 4; i++){
            tabClients[i] = 150+i;
        }
        gc.setClients(tabClients);

        Activite a = new Activite("activite");
        int i = 0;
        for(Client c : gc) {
            gc.allerA(c.getNumero(), a, 1+i);
            assertEquals(a, c.getEtape());
            assertEquals(i+1, c.getRang());
            i++;
        }
    }

    @Test
    void nettoyer(){

        GestionnaireClients gc = new GestionnaireClients();
        int[] tabClients = new int[4];
        for(int i = 0; i < 4; i++){
            tabClients[i] = 150+i;
        }
        gc.setClients(tabClients);
        gc.nettoyer();
        assertFalse(gc.iterator().hasNext());
    }
}
