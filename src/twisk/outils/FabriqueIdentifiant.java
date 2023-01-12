package twisk.outils;

public class FabriqueIdentifiant {

    private int noEtape;

    private static FabriqueIdentifiant instance = new FabriqueIdentifiant();

    /**
     * Getter de l'instance de la classe
     * @return instance
     */
    public static FabriqueIdentifiant getInstance() {
        return instance;
    }

    /**
     * Constructeur privé de FabriqueIdentifiant
     */
    private FabriqueIdentifiant(){
        this.noEtape = 0;
    }

    /**
     * Retourne l'entier (sous forme de String) correspondant à l'identifiant d'une nouvelle étape
     * @return  Integer.toString(noEtape)
     */
    public String getIdentifiantEtape(){

        noEtape++;
        return Integer.toString(noEtape);
    }

    /**
     * Setter pour forcer le début des numéros
     * @param numeroEtape Numéro de départ
     */
    public void setNumeroEtape(int numeroEtape){
        this.noEtape = numeroEtape;
    }

    /**
     * Réinitialise le compteur des numéros d'étape à 0
     */
    public void reset(){
        this.noEtape = 0;
    }
}
