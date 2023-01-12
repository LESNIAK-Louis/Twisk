package twisk.mondeIG;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import twisk.monde.Etape;

import java.util.ArrayList;
import java.util.Iterator;

public class GestionnaireSuccesseursIG implements Iterable<EtapeIG>{

    private ArrayList<EtapeIG> listeSuccesseurs;

    /**
     * Constructeur du GestionnaireSuccesseursIG
     */
    public GestionnaireSuccesseursIG(){

        listeSuccesseurs = new ArrayList<EtapeIG>();
    }

    /**
     * Permet d'ajouter les EtapeIG au GestionnaireSuccesseursIG
     * @param etapes
     */
    public void ajouter(EtapeIG... etapes){

        for(EtapeIG e : etapes){
            listeSuccesseurs.add(e);
        }
    }

    /**
     * Permet de supprimer les EtapeIG au GestionnaireSuccesseursIG
     * @param etapes
     */
    public void supprimer(EtapeIG... etapes){

        for(EtapeIG e : etapes){
            if(listeSuccesseurs.contains(e))
                listeSuccesseurs.remove(e);
        }
    }

    /**
     * Permet de connaitre le nombre d'EtapeIG composant le GestionnaireSuccesseursIG
     * @return entier correspondant au nombre d'EtapeIG dans le GestionnaireSuccesseursIG
     */
    @JsonIgnore
    public int nbEtapes(){
        return listeSuccesseurs.size();
    }

    /**
     * Retourne la liste des successeurs
     * @return listeSuccesseurs
     */
    @JsonGetter("listeSuccesseurs")
    public ArrayList<EtapeIG> getListeSuccesseurs(){
        return listeSuccesseurs;
    }

    /**
     * Retourne le premier successeur dans GestionnaireSuccesseursIG
     * @return une EtapeIG
     */
    @JsonIgnore
    public EtapeIG getSuccesseur()
    {
        Iterator<EtapeIG> ite = this.iterator();
        if(ite.hasNext())
            return ite.next();
        return null;
    }

    /**
     * Retourne si etape est dans la liste des successeurs
     * @return boolean
     */
    public boolean contains(EtapeIG etape)
    {
        for(EtapeIG e : this){
            if(etape.equals(e))
                return true;
        }
        return false;
    }

    /**
     * Setter de la liste des successeurs
     * @param listeSuccesseurs Liste des successeurs
     */
    @JsonSetter("listeSuccesseurs")
    public void setListeSuccesseurs(ArrayList<EtapeIG> listeSuccesseurs){
        this.listeSuccesseurs = listeSuccesseurs;
    }

    /**
     * l'Iterateur des Etape dans le GestionnaireSuccesseursIG
     * @return Iterator<EtapeIG>
     */
    @Override
    public Iterator<EtapeIG> iterator() {

        return listeSuccesseurs.iterator();
    }
}
