package twisk.mondeIG;

import twisk.vues.Observateur;

import java.util.ArrayList;

public class SujetObserve {

    protected ArrayList<Observateur> obs;

    /**
     * Constructeur de SujetObserve
     */
    public SujetObserve(){
        obs = new ArrayList<Observateur>();
    }

    /**
     * Enregistre un observateur
     * @param v observateur
     */
    public void ajouterObservateur(Observateur v){
        this.obs.add(v);
    }

    /**
     * Notifie les observateurs qu'un événement s'est produit
     */
    public void notifierObservateurs(){
        for(int i = 0; i < obs.size(); i++){
            obs.get(i).reagir();
        }
    }
}
