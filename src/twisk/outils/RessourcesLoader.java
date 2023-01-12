package twisk.outils;

import javafx.scene.image.Image;
import twisk.exceptions.TwiskIOException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

public class RessourcesLoader {

    private static RessourcesLoader instance = new RessourcesLoader();

    private Image imageEntree, imageSortie;
    private HashMap<String, Image> fonds;

    /**
     * Constructeur privé
     */
    private RessourcesLoader(){

        imageEntree = new Image(getClass().getResourceAsStream("/images/entree.png"), 12, 16, true, false);
        imageSortie = new Image(getClass().getResourceAsStream("/images/sortie.png"), 12, 16, true, false);
        fonds = new HashMap<String, Image>(3);
        fonds.put("vide", new Image(getClass().getResourceAsStream("/images/fond_vide.png")));
        fonds.put("grille", new Image(getClass().getResourceAsStream("/images/fond_grille.png")));
        fonds.put("points", new Image(getClass().getResourceAsStream("/images/fond_points.png")));
    }

    /**
     * Getter de l'instance de la classe
     * @return instance
     */
    public static RessourcesLoader getInstance(){ return instance;}

    /**
     * Getter de l'image d'entrée
     * @return image d'entrée
     */
    public Image getImageEntree() {
        return imageEntree;
    }

    /**
     * Getter de l'image de sortie
     * @return image de sortie
     */
    public Image getImageSortie() {
        return imageSortie;
    }

    /**
     * Getter de l'image d'arrière-plan
     * @param nom Nom de l'arrière-plan
     * @return image d'arrière-plan
     */
    public Image getFond(String nom) {
        return fonds.get(nom);
    }

    /**
     * Recopie les fichiers présents dans /ressources/mondes dans un dossier à l'extérieur du jar
     */
    public void extraireMondesPreExistants() throws TwiskIOException {
        if(!Files.exists(Paths.get("./mondes"))) {
            try {
                Files.createDirectory(Paths.get("./mondes"));

                InputStream stream = getClass().getResourceAsStream("/mondes/mondes.txt");
                if(stream != null) {
                    InputStreamReader reader = new InputStreamReader(stream);
                    BufferedReader buffer = new BufferedReader(reader);
                    String line = buffer.readLine();
                    while(line != null){
                        line = line.replaceAll("[\\r\\n]+","");
                        InputStream mondeStream = getClass().getResourceAsStream("/mondes/" + line);
                        if(mondeStream != null) {
                            Files.createFile(Paths.get("./mondes/" + line));
                            Files.write(Paths.get("./mondes/" + line), mondeStream.readAllBytes());
                        }
                        line = buffer.readLine();
                    }
                }
            } catch (IOException e) {
                throw new TwiskIOException("Erreur lors de la copie des mondes préexistants.");
            }
        }
    }
}
