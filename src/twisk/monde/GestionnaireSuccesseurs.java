package twisk.monde;

import java.util.ArrayList;
import java.util.Iterator;

public class GestionnaireSuccesseurs implements Iterable<Etape>{

    private ArrayList<Etape> listeSuccesseurs;

    /**
     * Constructeur du GestionnaireSuccesseurs
     */
    public GestionnaireSuccesseurs(){

        listeSuccesseurs = new ArrayList<Etape>();
    }

    /**
     * Permet d'ajouter les Etape au GestionnaireSuccesseurs
     * @param etapes
     */
    public void ajouter(Etape... etapes){

        for(Etape e : etapes){
            listeSuccesseurs.add(e);
        }
    }

    /**
     * Permet de connaitre le nombre d'Etape composant le GestionnaireSuccesseurs
     * @return entier correspondant au nombre d'Etape dans le GestionnaireSuccesseurs
     */
    public int nbEtapes(){
        return listeSuccesseurs.size();
    }

    /**
     * Retourne le premier successeur dans GestionnaireSuccesseurs
     * @return une Etape
     */
    public Etape getSuccesseur()
    {
        Iterator<Etape> ite = this.iterator();
        if(ite.hasNext())
            return ite.next();
        return null;
    }

    /**
     * l'Iterateur des Etape dans le GestionnaireSuccesseurs
     * @return Iterator<Etape>
     */
    @Override
    public Iterator<Etape> iterator() {

        return listeSuccesseurs.iterator();
    }
}
