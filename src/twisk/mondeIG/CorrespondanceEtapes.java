package twisk.mondeIG;

import twisk.monde.Etape;

import java.util.HashMap;

public class CorrespondanceEtapes {

    private HashMap<EtapeIG, Etape> etapes;
    private HashMap<Etape, EtapeIG> etapesIG;

    /**
     * Constructeur de CorrespondanceEtapes
     */
    public CorrespondanceEtapes(){

        etapes = new HashMap<EtapeIG, Etape>();
        etapesIG = new HashMap<Etape, EtapeIG>();
    }

    /**
     * Permet d'ajouter une étape à la table des correspondances
     * @param etig EtapeIG
     * @param et Etape correspondante
     */
    public void ajouter(EtapeIG etig, Etape et){

        etapes.put(etig, et);
        etapesIG.put(et, etig);
    }

    /**
     * Permet de récupérer l'Etape correspondant à une EtapeIG
     * @param e EtapeIG
     * @return Etape correspondante
     */
    public Etape get(EtapeIG e){
        return etapes.get(e);
    }

    /**
     * Permet de récupérer l'EtapeIG correspondant à une Etape
     * @param e Etape
     * @return EtapeIG correspondante
     */
    public EtapeIG get(Etape e){
        return etapesIG.get(e);
    }

    /**
     * Retourne le nombre d'étapes enregistrées dans la table
     * @return Le nombre d'étapes
     */
    public int getNbEtapes(){
        return etapes.size();
    }
}
