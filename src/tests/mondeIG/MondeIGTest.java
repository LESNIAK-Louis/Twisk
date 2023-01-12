package tests.mondeIG;

import javafx.geometry.Point2D;
import org.junit.jupiter.api.Test;
import twisk.exceptions.ArcNonValideException;
import twisk.exceptions.MondeException;
import twisk.exceptions.ParametresIncorrectsException;
import twisk.mondeIG.*;

import static org.junit.jupiter.api.Assertions.*;

public class MondeIGTest {

    @Test
    void testConstructeur(){

        MondeIG monde = new MondeIG();
        assertEquals(0, monde.getNombreEtapes());
    }

    @Test
    void ajouterEtape(){

        MondeIG monde = new MondeIG();
        monde.ajouter("Activité");
        ActiviteIG act = new ActiviteIG("Activité1", "1", 40, 25);

        assertEquals(1, monde.getNombreEtapes());
        assertTrue(act.equals(monde.getEtape("1")));

        monde.ajouter("Activité");
        act = new ActiviteIG("Activité2", "2", 40, 25);

        assertEquals(2, monde.getNombreEtapes());
        assertTrue(act.equals(monde.getEtape("2")));
    }

    @Test
    void ajouterArc(){

        EtapeIG e1 = new ActiviteIG("e", "1", 50, 30);
        EtapeIG e2 = new ActiviteIG("e", "2", 50, 30);
        EtapeIG e3 = new ActiviteIG("e", "3", 50, 30);
        MondeIG monde = new MondeIG();
        PointDeControleIG p1 = new PointDeControleIG("P1", Position.GAUCHE, e1,10, 20);
        PointDeControleIG p2 = new PointDeControleIG("P2", Position.DROITE, e2,40, 30);
        try {
            monde.ajouter(p1, p2);
        } catch (ArcNonValideException e) {
            e.printStackTrace();
        }

        assertEquals(1, monde.getNombreArcs());
        assertTrue(monde.getArc(0).equals(new LigneDroiteIG(p1, p2)));

        PointDeControleIG p3 = new PointDeControleIG("P3", Position.GAUCHE, e1,0, 20);
        PointDeControleIG p4 = new PointDeControleIG("P4", Position.DROITE, e3,40, 10);
        try {
            monde.ajouter(p3, p4);
        } catch (ArcNonValideException e) {
            e.printStackTrace();
        }

        assertEquals(2, monde.getNombreArcs());
        assertTrue(monde.getArc(1).equals(new LigneDroiteIG(p3, p4)));

        try {
            monde.ajouter(p1, p3);
        } catch (ArcNonValideException e) {
            assertEquals(2, monde.getNombreArcs());
        }

        GuichetIG g = new GuichetIG("g", "4", 50, 30);
        PointDeControleIG pe = new PointDeControleIG("P1", Position.GAUCHE, e1,10, 20);
        PointDeControleIG pg = new PointDeControleIG("P2", Position.DROITE, g,40, 30);
        monde = new MondeIG();
        try {
            g.setSensFile(SensFile.INCONNU);
            monde.ajouter(pe, pg);
            assertEquals(SensFile.GAUCHE, g.getSensFile());
        } catch (ArcNonValideException e) {
            e.printStackTrace();
        }

        pe = new PointDeControleIG("P1", Position.DROITE, e1,10, 20);
        pg = new PointDeControleIG("P2", Position.GAUCHE, g,40, 30);
        monde = new MondeIG();
        try {
            g.setSensFile(SensFile.INCONNU);
            monde.ajouter(pe, pg);
            assertEquals(SensFile.DROITE, g.getSensFile());
        } catch (ArcNonValideException e) {
            e.printStackTrace();
        }

        pe = new PointDeControleIG("P1", Position.DROITE, e2,10, 20);
        pg = new PointDeControleIG("P2", Position.GAUCHE, g,40, 30);
        monde = new MondeIG();
        try {
            g.setSensFile(SensFile.INCONNU);
            monde.ajouter(pg, pe);
            assertEquals(SensFile.GAUCHE, g.getSensFile());
        } catch (ArcNonValideException e) {
            e.printStackTrace();
        }

        pe = new PointDeControleIG("P1", Position.GAUCHE, e2,10, 20);
        pg = new PointDeControleIG("P2", Position.DROITE, g,40, 30);
        monde = new MondeIG();
        try {
            g.setSensFile(SensFile.INCONNU);
            monde.ajouter(pg, pe);
            assertEquals(SensFile.DROITE, g.getSensFile());
        } catch (ArcNonValideException e) {
            e.printStackTrace();
        }

        pe = new PointDeControleIG("P1", Position.BAS, e1,10, 20);
        pg = new PointDeControleIG("P2", Position.HAUT, g,40, 30);
        monde = new MondeIG();
        try {
            g.setSensFile(SensFile.INCONNU);
            monde.ajouter(pe, pg);
            assertEquals(SensFile.DROITE, g.getSensFile());
        } catch (ArcNonValideException e) {
            e.printStackTrace();
        }

        pe = new PointDeControleIG("P1", Position.HAUT, e2,10, 20);
        pg = new PointDeControleIG("P2", Position.BAS, g,40, 30);
        monde = new MondeIG();
        try {
            g.setSensFile(SensFile.INCONNU);
            monde.ajouter(pg, pe);
            assertEquals(SensFile.DROITE, g.getSensFile());
        } catch (ArcNonValideException e) {
            e.printStackTrace();
        }

        pe = new PointDeControleIG("P1", Position.HAUT, e1,10, 20);
        pg = new PointDeControleIG("P2", Position.BAS, g,40, 30);
        monde = new MondeIG();
        try {
            g.setSensFile(SensFile.INCONNU);
            monde.ajouter(pe, pg);
            assertEquals(SensFile.GAUCHE, g.getSensFile());
        } catch (ArcNonValideException e) {
            e.printStackTrace();
        }

        pe = new PointDeControleIG("P1", Position.BAS, e2,10, 20);
        pg = new PointDeControleIG("P2", Position.HAUT, g,40, 30);
        monde = new MondeIG();
        try {
            g.setSensFile(SensFile.INCONNU);
            monde.ajouter(pg, pe);
            assertEquals(SensFile.GAUCHE, g.getSensFile());
        } catch (ArcNonValideException e) {
            e.printStackTrace();
        }
    }

    @Test
    void supprimerArcsEtape(){

        EtapeIG e1 = new ActiviteIG("e", "1", 50, 30);
        EtapeIG e2 = new ActiviteIG("e", "2", 50, 30);
        EtapeIG e3 = new ActiviteIG("e", "3", 50, 30);
        MondeIG monde = new MondeIG();

        try {
            monde.ajouter(e1.getPointDeControle(Position.BAS), e2.getPointDeControle(Position.BAS));
            monde.ajouter(e2.getPointDeControle(Position.GAUCHE), e3.getPointDeControle(Position.GAUCHE));
        } catch (ArcNonValideException e) {
            e.printStackTrace();
        }

        monde.supprimerArcs(e1);
        assertEquals(1, monde.getNombreArcs());
        assertEquals(0, e1.nbSuccesseurs());
        monde.supprimerArcs(e3);
        assertEquals(0, monde.getNombreArcs());
        assertEquals(0, e2.nbSuccesseurs());
    }

    @Test
    void selectionnerPointDeControle(){

        MondeIG monde = new MondeIG();
        monde.ajouter("Activite");
        monde.ajouter("Activite");
        EtapeIG e1 = monde.getEtape("1");
        EtapeIG e2 = monde.getEtape("2");

        PointDeControleIG point1 = e1.getPointDeControle(Position.GAUCHE);
        PointDeControleIG point2 = e2.getPointDeControle(Position.DROITE);

        try {
            monde.selectionnerPoint(point1);
            assertTrue(monde.estSelectionne(point1));
            monde.selectionnerPoint(point2);
            assertFalse(monde.estSelectionne(point1));
            assertFalse(monde.estSelectionne(point2));
        } catch (ArcNonValideException e) {
            e.printStackTrace();
        }
    }

    @Test
    void selectionnerPointCourbe(){

        MondeIG monde = new MondeIG();
        monde.ajouter("Activite");
        monde.ajouter("Activite");
        EtapeIG e1 = monde.getEtape("1");
        EtapeIG e2 = monde.getEtape("2");

        try {
            monde.selectionnerPoint(new Point2D(0,0));
            assertEquals(0, monde.getNombreCoordsPointsCourbe());
            monde.selectionnerPoint(e1.getPointDeControle(Position.DROITE));
            monde.selectionnerPoint(new Point2D(0,0));
            assertEquals(1, monde.getNombreCoordsPointsCourbe());
            monde.selectionnerPoint(new Point2D(10,20));
            assertEquals(2, monde.getNombreCoordsPointsCourbe());
            monde.selectionnerPoint(new Point2D(130,220));
            assertEquals(3, monde.getNombreCoordsPointsCourbe());
        } catch (ArcNonValideException e) {
            e.printStackTrace();
        }
    }

    @Test
    void selectionnerEtape(){

        MondeIG monde = new MondeIG();
        monde.ajouter("Activite");
        monde.ajouter("Activite");
        monde.ajouter("Activite");
        EtapeIG e1 = monde.getEtape("1");
        EtapeIG e2 = monde.getEtape("2");
        EtapeIG e3 = monde.getEtape("3");

        monde.selectionnerEtape(e2);
        monde.selectionnerEtape(e1);
        assertEquals(2, monde.getNombreEtapesSelectionnes());
    }

    @Test
    void selectionnerArc(){

        MondeIG monde = new MondeIG();
        monde.ajouter("Activite");
        monde.ajouter("Activite");
        monde.ajouter("Activite");
        EtapeIG e1 = monde.getEtape("1");
        EtapeIG e2 = monde.getEtape("2");
        EtapeIG e3 = monde.getEtape("3");

        try {
            monde.ajouter(e1.getPointDeControle(Position.DROITE), e2.getPointDeControle(Position.DROITE));
            monde.ajouter(e2.getPointDeControle(Position.GAUCHE), e3.getPointDeControle(Position.GAUCHE));
        } catch (ArcNonValideException e) {
            e.printStackTrace();
        }
        monde.selectionnerArc(monde.getArc(0));
        assertEquals(1, monde.getNombreArcsSelectiones());
        monde.selectionnerArc(monde.getArc(1));
        assertEquals(2, monde.getNombreArcsSelectiones());
    }

    @Test
    void supprimerSelection(){

        MondeIG monde = new MondeIG();
        monde.ajouter("Activite");
        monde.ajouter("Activite");
        monde.ajouter("Activite");
        EtapeIG e1 = monde.getEtape("1");
        EtapeIG e2 = monde.getEtape("2");
        EtapeIG e3 = monde.getEtape("3");

        try {
            monde.ajouter(e1.getPointDeControle(Position.DROITE), e2.getPointDeControle(Position.DROITE));
            monde.ajouter(e2.getPointDeControle(Position.GAUCHE), e3.getPointDeControle(Position.GAUCHE));
        } catch (ArcNonValideException e) {
            e.printStackTrace();
        }
        monde.selectionnerEtape(e2);
        monde.supprimerSelection();
        assertEquals(0, monde.getNombreArcs());
        assertEquals(2, monde.getNombreEtapes());
        assertEquals(0, e1.nbSuccesseurs());

        try {
            monde.ajouter(e1.getPointDeControle(Position.DROITE), e3.getPointDeControle(Position.DROITE));
        } catch (ArcNonValideException e) {
            e.printStackTrace();
        }
        monde.selectionnerArc(monde.getArc(0));
        monde.supprimerSelection();
        assertEquals(0, monde.getNombreArcs());
        assertEquals(2, monde.getNombreEtapes());
        assertEquals(0, e1.nbSuccesseurs());

        monde.ajouter("Activite");
        EtapeIG e4 = monde.getEtape("4");
        try {
            monde.ajouter(e1.getPointDeControle(Position.DROITE), e3.getPointDeControle(Position.DROITE));
            monde.ajouter(e1.getPointDeControle(Position.DROITE), e4.getPointDeControle(Position.GAUCHE));
        } catch (ArcNonValideException e) {
            e.printStackTrace();
        }
        monde.selectionnerArc(monde.getArc(0));
        monde.selectionnerEtape(e4);
        monde.supprimerSelection();
        assertEquals(0, monde.getNombreArcs());
        assertEquals(2, monde.getNombreEtapes());
        assertEquals(0, e1.nbSuccesseurs());
    }

    @Test
    void toutDeselectionner(){

        MondeIG monde = new MondeIG();
        monde.ajouter("Activite");
        monde.ajouter("Activite");
        monde.ajouter("Activite");
        EtapeIG e1 = monde.getEtape("1");
        EtapeIG e2 = monde.getEtape("2");
        EtapeIG e3 = monde.getEtape("3");

        try {
            monde.ajouter(e1.getPointDeControle(Position.DROITE), e2.getPointDeControle(Position.DROITE));
            monde.ajouter(e2.getPointDeControle(Position.GAUCHE), e3.getPointDeControle(Position.GAUCHE));
        } catch (ArcNonValideException e) {
            e.printStackTrace();
        }
        monde.selectionnerEtape(e2);
        monde.selectionnerArc(monde.getArc(0));
        monde.selectionnerEtape(e1);
        monde.toutDeselectionner();
        assertEquals(0, monde.getNombreEtapesSelectionnes());
        assertEquals(0, monde.getNombreArcsSelectiones());
    }

    @Test
    void renommerSelection(){

        MondeIG monde = new MondeIG();
        monde.ajouter("Activite");
        EtapeIG e1 = monde.getEtape("1");

        monde.selectionnerEtape(e1);
        monde.renommerSelection("NouveauNom");
        assertEquals("NouveauNom", monde.getEtape("1").getNom());

        monde.selectionnerEtape(e1);
        monde.renommerSelection("Nouveau Nom 2");
        assertEquals("Nouveau Nom 2", monde.getEtape("1").getNom());
    }

    @Test
    void deplacerEtape(){
        MondeIG monde = new MondeIG();
        monde.ajouter("Activite");
        monde.ajouter("Activite");
        monde.ajouter("Activite");

        monde.deplacerEtape("1", 100,100);
        monde.deplacerEtape("2", 50,250);
        monde.deplacerEtape("3", 120,40);
        assertEquals(100, monde.getEtape("1").getPosX());
        assertEquals(100, monde.getEtape("1").getPosY());

        assertEquals(50, monde.getEtape("2").getPosX());
        assertEquals(250, monde.getEtape("2").getPosY());
        assertEquals(120, monde.getEtape("3").getPosX());
        assertEquals(40, monde.getEtape("3").getPosY());
    }

    @Test
    void inverserEntree(){

        MondeIG monde = new MondeIG();
        monde.ajouter("Activite");
        EtapeIG e = monde.getEtape("1");
        assertFalse(e.estUneEntree());
        monde.selectionnerEtape(e);
        monde.inverserEntreeSelection();
        assertTrue(e.estUneEntree());
        monde.selectionnerEtape(e);
        monde.inverserEntreeSelection();
        assertFalse(e.estUneEntree());
    }

    @Test
    void inverserSortie(){

        MondeIG monde = new MondeIG();
        monde.ajouter("Activite");
        EtapeIG e = monde.getEtape("1");
        assertFalse(e.estUneSortie());
        monde.selectionnerEtape(e);
        monde.inverserSortieSelection();
        assertTrue(e.estUneSortie());
        monde.selectionnerEtape(e);
        monde.inverserSortieSelection();
        assertFalse(e.estUneSortie());
    }

    @Test
    void annulerSelectionPoints(){

        MondeIG monde = new MondeIG();
        monde.ajouter("Activite");
        monde.ajouter("Activite");
        EtapeIG e1 = monde.getEtape("1");
        EtapeIG e2 = monde.getEtape("2");

        PointDeControleIG point1 = e1.getPointDeControle(Position.GAUCHE);
        PointDeControleIG point2 = e2.getPointDeControle(Position.HAUT);

        try {
            monde.selectionnerPoint(point1);
            monde.annulerSelectionPoints();
            assertFalse(monde.estSelectionne(point1));

            monde.selectionnerPoint(new Point2D(0,0));
            monde.annulerSelectionPoints();
            assertEquals(0, monde.getNombreCoordsPointsCourbe());

            monde.selectionnerPoint(new Point2D(10,10));
            monde.selectionnerPoint(new Point2D(100,100));
            monde.annulerSelectionPoints();
            assertEquals(0, monde.getNombreCoordsPointsCourbe());

        } catch (ArcNonValideException e) {
            e.printStackTrace();
        }
    }

    @Test
    void estConnexe(){

        MondeIG monde = new MondeIG();
        EtapeIG e1 = new ActiviteIG("e", "1", 50, 30);
        EtapeIG e2 = new ActiviteIG("e", "2", 50, 30);
        EtapeIG e3 = new ActiviteIG("e", "3", 50, 30);

        monde.ajouter(e1,e2,e3);
        e1.ajouterSuccesseur(e2);
        e2.ajouterSuccesseur(e3);
        e1.setEntree(true);
        e3.setSortie(true);

        assertTrue(monde.estConnexe(monde.getEntree()));

        e2.supprimerSuccesseur(e3);
        assertFalse(monde.estConnexe(monde.getEntree()));
    }

    @Test
    void verifierSensFile(){

        GuichetIG g;
        EtapeIG e;
        MondeIG monde;
        PointDeControleIG pe, pg;

        e = new ActiviteIG("e", "1", 50, 30);
        g = new GuichetIG("g", "2", 50, 30);
        monde = new MondeIG();

        pe = new PointDeControleIG("P1", Position.DROITE, e,10, 20);
        pg = new PointDeControleIG("P2", Position.GAUCHE, g,40, 30);
        assertTrue(monde.verifierSensFile(pe,pg));

        pe = new PointDeControleIG("P1", Position.HAUT, e,10, 20);
        pg = new PointDeControleIG("P2", Position.GAUCHE, g,40, 30);
        assertTrue(monde.verifierSensFile(pe,pg));

        pe = new PointDeControleIG("P1", Position.GAUCHE, e,10, 20);
        pg = new PointDeControleIG("P2", Position.GAUCHE, g,40, 30);
        assertTrue(monde.verifierSensFile(pe,pg));

        pe = new PointDeControleIG("P1", Position.BAS, e,10, 20);
        pg = new PointDeControleIG("P2", Position.GAUCHE, g,40, 30);
        assertTrue(monde.verifierSensFile(pe,pg));

        pe = new PointDeControleIG("P1", Position.DROITE, e,10, 20);
        pg = new PointDeControleIG("P2", Position.GAUCHE, g,40, 30);
        assertTrue(monde.verifierSensFile(pg,pe));

        pe = new PointDeControleIG("P1", Position.HAUT, e,10, 20);
        pg = new PointDeControleIG("P2", Position.GAUCHE, g,40, 30);
        assertTrue(monde.verifierSensFile(pg,pe));

        pe = new PointDeControleIG("P1", Position.GAUCHE, e,10, 20);
        pg = new PointDeControleIG("P2", Position.GAUCHE, g,40, 30);
        assertTrue(monde.verifierSensFile(pg,pe));

        pe = new PointDeControleIG("P1", Position.BAS, e,10, 20);
        pg = new PointDeControleIG("P2", Position.GAUCHE, g,40, 30);
        assertTrue(monde.verifierSensFile(pg,pe));

        g.setSensFile(SensFile.GAUCHE);
        pe = new PointDeControleIG("P1", Position.HAUT, e,10, 20);
        pg = new PointDeControleIG("P2", Position.DROITE, g,40, 30);
        assertFalse(monde.verifierSensFile(pg,pe));

        pe = new PointDeControleIG("P1", Position.HAUT, e,10, 20);
        pg = new PointDeControleIG("P2", Position.BAS, g,40, 30);
        assertFalse(monde.verifierSensFile(pg,pe));

        pe = new PointDeControleIG("P1", Position.HAUT, e,10, 20);
        pg = new PointDeControleIG("P2", Position.GAUCHE, g,40, 30);
        assertFalse(monde.verifierSensFile(pe,pg));

        pe = new PointDeControleIG("P1", Position.HAUT, e,10, 20);
        pg = new PointDeControleIG("P2", Position.HAUT, g,40, 30);
        assertFalse(monde.verifierSensFile(pe,pg));

        g.setSensFile(SensFile.DROITE);
        pe = new PointDeControleIG("P1", Position.HAUT, e,10, 20);
        pg = new PointDeControleIG("P2", Position.GAUCHE, g,40, 30);
        assertFalse(monde.verifierSensFile(pg,pe));

        pe = new PointDeControleIG("P1", Position.HAUT, e,10, 20);
        pg = new PointDeControleIG("P2", Position.HAUT, g,40, 30);
        assertFalse(monde.verifierSensFile(pg,pe));

        pe = new PointDeControleIG("P1", Position.HAUT, e,10, 20);
        pg = new PointDeControleIG("P2", Position.DROITE, g,40, 30);
        assertFalse(monde.verifierSensFile(pe,pg));

        pe = new PointDeControleIG("P1", Position.HAUT, e,10, 20);
        pg = new PointDeControleIG("P2", Position.BAS, g,40, 30);
        assertFalse(monde.verifierSensFile(pe,pg));
    }
}