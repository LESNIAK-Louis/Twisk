package twisk;

import twisk.monde.*;
import twisk.outils.ClassLoaderPerso;
import twisk.simulation.Simulation;
import java.lang.reflect.*;

import java.lang.reflect.Constructor;

public class ClientTwisk {

    /**
     * Fonction main : définit un Monde et demande sa simulation
     * @param args
     */
    public static void main(String[] args) {
        Monde monde1 = ConstruireMonde1();
        simuler(monde1);
        Monde monde2 = ConstruireMonde2();
        simuler(monde2);
    }

    /**
     * Permet de load la class Simulation et de simuler le monde donné
     * @param monde
     */
    private static void simuler(Monde monde)
    {
        ClassLoaderPerso classLoader = new ClassLoaderPerso(ClientTwisk.class.getClassLoader());
        try {
            Class<?> simulation = classLoader.loadClass("twisk.simulation.Simulation");
            Constructor construct = simulation.getConstructor();
            Object simu = construct.newInstance();
            Method setNbClients = simulation.getMethod("setNbClients", int.class);
            setNbClients.invoke(simu, 5);
            Method simuler = simulation.getMethod("simuler", Monde.class);
            simuler.invoke(simu, monde);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * Construction d'un monde sans bifurcation
     * @return Monde
     */
    private static Monde ConstruireMonde1(){

        Monde monde = new Monde();
        Etape fileTobbogan = new Guichet("File tobbogan", 2);
        Etape filePlage = new Guichet("File Plage", 2);

        Etape tobbogan = new ActiviteRestreinte("Tobbogan");
        Etape piscine = new Activite("Piscine");
        Etape plage = new ActiviteRestreinte("Plage");

        fileTobbogan.ajouterSuccesseur(tobbogan);
        tobbogan.ajouterSuccesseur(piscine);
        piscine.ajouterSuccesseur(filePlage);
        filePlage.ajouterSuccesseur(plage);

        monde.ajouter(fileTobbogan, filePlage, tobbogan, piscine, plage);
        monde.aCommeEntree(fileTobbogan);
        monde.aCommeSortie(plage);

        return monde;
    }

    /**
     * Construction d'un monde avec bifurcations
     * @return Monde
     */
    private static Monde ConstruireMonde2(){

        Monde monde = new Monde();
        Guichet guichetC = new Guichet("Guichet caisse", 2);
        ActiviteRestreinte caisse = new ActiviteRestreinte("Caisse", 3, 1);
        Activite zoo = new Activite("Zoo", 5, 2);
        Activite parc = new Activite("Parc", 4, 3);
        Guichet guichetT = new Guichet("Guichet toboggan", 1);
        ActiviteRestreinte toboggan = new ActiviteRestreinte("Toboggan", 2, 1);
        Guichet guichetG = new Guichet("Guichet glace", 3);
        ActiviteRestreinte glace = new ActiviteRestreinte("Glace", 3, 1);

        monde.ajouter(guichetC, caisse, zoo, parc, guichetT, toboggan, guichetG, glace);
        monde.aCommeEntree(guichetC, parc, guichetG);
        monde.aCommeSortie(toboggan, parc, glace);
        guichetC.ajouterSuccesseur(caisse);
        caisse.ajouterSuccesseur(zoo);
        zoo.ajouterSuccesseur(guichetT);
        guichetT.ajouterSuccesseur(toboggan);
        parc.ajouterSuccesseur(guichetT);
        guichetG.ajouterSuccesseur(glace);

        return monde;
    }
}
