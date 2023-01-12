package twisk.monde;

public class SasSortie extends Activite {

    /**
     * Constructeur du SasSortie
     */
    public SasSortie(){
        super("Sortie");
    }

    /**
     * Vérifie si cette étape est une sortie.
     * @return true
     */
    @Override
    public boolean estUneSortie(){
        return true;
    }

    /**
     * Transformation du monde java en C
     * @return instructions en C
     */
    public String toC() { return ""; }
}
