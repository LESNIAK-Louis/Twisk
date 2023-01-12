package tests.outils;

import org.junit.jupiter.api.Test;
import twisk.monde.*;
import twisk.outils.KitC;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class KitCTest {

    @Test
    void creerEnvironnement(){

        KitC kitC = new KitC();
        kitC.creerEnvironnement();
        boolean resDir = Files.isDirectory(Paths.get("/tmp/twisk"));
        assertTrue(resDir);
        boolean resProgrammeC = Files.isRegularFile(Paths.get("/tmp/twisk/programmeC.o"));
        assertTrue(resProgrammeC);
        boolean resDefh = Files.isRegularFile(Paths.get("/tmp/twisk/def.h"));
        assertTrue(resDefh);
        boolean resCodeNatif = Files.isRegularFile(Paths.get("/tmp/twisk/codeNatif.o"));
        assertTrue(resCodeNatif);
    }

    @Test
    void creerFichier() {
        KitC kitC = new KitC();
        kitC.creerEnvironnement();
        kitC.creerFichier("test123\nhello");
        boolean resFichier = Files.isRegularFile(Paths.get("/tmp/twisk/client.c"));
        assertTrue(resFichier);

        try {
            Path file = Paths.get("/tmp/twisk/client.c");
            File fichier = new File(file.toString());
            assertTrue(fichier.exists());
            Scanner lecture = new Scanner(fichier);
            assertEquals(lecture.nextLine(), "test123");
            assertEquals(lecture.nextLine(), "hello");
            lecture.close();
        } catch (IOException exp) { assertEquals("IOException", exp.getMessage()); }
    }

    @Test
    void compiler(){

        KitC kitC = new KitC();
        kitC.creerEnvironnement();

        Monde monde = new Monde();
        Etape activite1 = new Activite("Activite1");
        Etape guichet = new Guichet("Guichet");
        Etape activite2 = new ActiviteRestreinte("Activite2");

        monde.ajouter(activite1, guichet, activite2);
        monde.aCommeEntree(activite1);
        monde.aCommeSortie(activite2);
        activite1.ajouterSuccesseur(guichet);
        guichet.ajouterSuccesseur(activite2);

        kitC.creerFichier(monde.toC());
        kitC.compiler();
        boolean res = Files.isRegularFile(Paths.get("/tmp/twisk/client.o"));
        assertTrue(res);

    }

    @Test
    void construireLaLibrairie(){
        KitC kitC = new KitC();
        kitC.creerEnvironnement();
        Monde monde = new Monde();
        Etape activite1 = new Activite("Activite1");
        Etape guichet = new Guichet("Guichet");
        Etape activite2 = new ActiviteRestreinte("Activite2");

        monde.ajouter(activite1, guichet, activite2);
        monde.aCommeEntree(activite1);
        monde.aCommeSortie(activite2);
        activite1.ajouterSuccesseur(guichet);
        guichet.ajouterSuccesseur(activite2);

        kitC.creerFichier(monde.toC());
        kitC.compiler();
        kitC.construireLaLibrairie();
        boolean res = Files.isRegularFile(Paths.get("/tmp/twisk/libTwisk1.so"));
        assertTrue(res);
    }
}
