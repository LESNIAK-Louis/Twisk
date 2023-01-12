package twisk.outils;

public class FabriqueNumero {

    int cptEtape;
    int cptSemaphore;
    int cptLib;
    int cptResultat;
    private static FabriqueNumero instance = new FabriqueNumero();

    /**
     * Constructeur du singleton FabriqueNumero
     */
    private FabriqueNumero(){
        cptEtape = 0;
        cptSemaphore = 1;
        cptLib = 1;
        cptResultat = 1;
    }

    /**
     * Getter de l'instance du singleton FabriqueNumero
     * @return instance de FabriqueNumero
     */
    public static FabriqueNumero getInstance() { return instance; }

    /**
     * Getter d'un nouveau numéro d'Etape (différent des autres = unique)
     * @return entier correspondant à un numéro d'Etape
     */
    public int getNumeroEtape() { return cptEtape++; }

    /**
     * Getter d'un nouveau numéro sémaphore pour un Guichet (différent des autres = unique)
     * @return entier correspondant à un numéro sémaphore pour un Guichet
     */
    public int getNumeroSemaphore() { return cptSemaphore++; }

    /**
     * Getter d'un nouveau numéro de libTwisk
     * @return entier correspondant à un numéro de libTwisk
     */
    public int getNumeroLib() { return cptLib++; }

    /**
     * Getter d'un nouveau numéro de résultat de simulation
     * @return entier correspondant à un numéro de simulation
     */
    public int getNumeroResultat() { return cptResultat++; }

    /**
     * Setter du numéro de résultat de simulation
     * @param n entier correspondant au numéro de simulation actuel
     */
    public void setNumeroResultat(int n) { this.cptResultat = n; }

    /**
     * Réinitialise les compteurs des numéros d'Etape et des numéros sémaphores des Guichet
     */
    public void reset() { cptEtape = 0; cptSemaphore = 1; }

}