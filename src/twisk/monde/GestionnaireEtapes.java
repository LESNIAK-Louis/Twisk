package twisk.monde;

import java.util.ArrayList;
import java.util.Iterator;

public class GestionnaireEtapes implements Iterable<Etape>{

    private ArrayList<Etape> listeEtapes;

    /**
     * Constructeur du GestionnaireEtapes
     */
    public GestionnaireEtapes(){
        listeEtapes = new ArrayList<Etape>();
    }

    /**
     * Permet d'ajouter les Etape au GestionnaireEtapes
     * @param etapes
     */
    public void ajouter(Etape... etapes){

        for(Etape e : etapes){
            listeEtapes.add(e);
        }
    }

    /**
     * Permet de connaitre le nombre d'Etape composant le GestionnaireEtapes
     * @return entier correspondant au nombre d'Etapes dans le GestionnaireEtapes
     */
    public int nbEtapes(){
        return listeEtapes.size();
    }

    /**
     * l'Iterateur des Etape dans le GestionnaireEtapes
     * @return Iterator<Etape>
     */
    @Override
    public Iterator<Etape> iterator() {

        return listeEtapes.iterator();
    }
}
