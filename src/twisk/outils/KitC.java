package twisk.outils;

import twisk.simulation.Client;
import twisk.simulation.GestionnaireClients;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class KitC {

    private String nomLib;

    /**
     * Constructeur du KitC
     */
    public KitC() {
        nomLib = "/tmp/twisk/libTwisk.so";
    }

    public String getNomLib(){
        return nomLib;
    }

    /**
     * Créer l'environnement de travail sous /tmp
     */
    public void creerEnvironnement(){
        try {
            // création du répertoire twisk sous /tmp. Ne déclenche pas d’erreur si le répertoire existe déjà.
            Path directories = Files.createDirectories(Paths.get("/tmp/twisk"));
            // copie des fichiers depuis le projet sous /tmp/twisk
            String[] liste = {"programmeC.o", "def.h", "codeNatif.o"};
            for (String nom : liste) {
                InputStream source = getClass().getResource("/codeC/" + nom).openStream() ;
                File destination = new File("/tmp/twisk/" + nom) ;
                copier(source, destination);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Permet la copie d'un fichier d'un endroit source vers une destination
     * @param source
     * @param dest
     * @throws IOException
     */
    private void copier(InputStream source, File dest) throws IOException {

        InputStream sourceFile = source;
        OutputStream destinationFile = new FileOutputStream(dest) ;
        // Lecture par segment de 0.5Mo
        byte buffer[] = new byte[512 * 1024];
        int nbLecture;
        while ((nbLecture = sourceFile.read(buffer)) != -1){
            destinationFile.write(buffer, 0, nbLecture);
        }
        destinationFile.close();
        sourceFile.close();
    }

    /**
     * Compile le client
     */
    public void compiler(){

        Runtime runtime = Runtime.getRuntime();
        try {
            Process p = runtime.exec("gcc -Wall -fPIC -c /tmp/twisk/client.c -o /tmp/twisk/client.o");
            BufferedReader output = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader error = new BufferedReader(new InputStreamReader(p.getErrorStream()));

            String ligne ;
            while ((ligne = output.readLine()) != null) {
                System.out.println(ligne);
            }
            while ((ligne = error.readLine()) != null) {
                System.out.println(ligne);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Créer le fichier client.c
     * @param codeC
     */
    public void creerFichier(String codeC){
        FileWriter flot ;
        try {
            Path file = Paths.get("/tmp/twisk/client.c");
            File fichier = new File(file.toString());
            if(fichier.exists())
                fichier.delete();
            flot = new FileWriter(fichier);
            flot.write(codeC);
            flot.close();
        } catch(IOException exp) { System.out.println("Erreur lors de la création de client.c");}

    }

    /**
     * Permet de compiler la librairie libTwisk.so grâce aux fichiers programmeC.o, codeNatif.o et client.o sous /tmp
     */
    public void construireLaLibrairie()
    {
        Runtime runtime = Runtime.getRuntime();
        try {
            int n = FabriqueNumero.getInstance().getNumeroLib();
            nomLib = "/tmp/twisk/libTwisk" + n + ".so";
            Process p = runtime.exec("gcc -shared /tmp/twisk/programmeC.o /tmp/twisk/codeNatif.o /tmp/twisk/client.o -lm -o "+ getNomLib());
            BufferedReader output = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader error = new BufferedReader(new InputStreamReader(p.getErrorStream()));

            String ligne ;
            while ((ligne = output.readLine()) != null) {
                System.out.println(ligne);
            }
            while ((ligne = error.readLine()) != null) {
                System.out.println(ligne);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Permet de tuer tous les processus C associé à une simulation
     */
    public void tuerProcessus(GestionnaireClients clients)
    {
        for(Client c : clients)
        {
            Runtime runtime = Runtime.getRuntime();
            try {
                String cmd = "kill -9 " + c.getNumero();
                runtime.exec(cmd);
            } catch (IOException e) { e.printStackTrace(); }
        }
    }
}
