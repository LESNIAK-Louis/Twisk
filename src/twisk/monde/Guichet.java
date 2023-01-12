package twisk.monde;

import twisk.outils.FabriqueNumero;

public class Guichet extends Etape {

    protected int nbJetons;
    private int numeroSemaphore;

    /**
     * Constructeur de Guichet via son nom
     * @param nom
     */
    public Guichet(String nom){
        
        super(nom);
        nbJetons = 4;
        FabriqueNumero fn = FabriqueNumero.getInstance();
        numeroSemaphore = fn.getNumeroSemaphore();
    }
    /**
     * Constructeur de Guichet via son nom et son nombre de jetons
     * @param nom nom du Guichet
     * @param nb nombre de jetons
     */
    public Guichet(String nom, int nb){

        this(nom);
        this.nbJetons = nb;
    }

    /**
     * Getter du numéro sémaphore du Guichet
     * @return entier correspodant au numeroSemaphore du Guichet
     */
    public int getNumeroSemaphore() {
        return numeroSemaphore;
    }

    /**
     * Getter du nombre de jetons du Guichet
     * @return entier correspodant au nombre de jetons du Guichet
     */
    public int getNbJetons(){
        return nbJetons;
    }

    /**
     * Vérifie si cette étape est un guichet
     * @return true
     */
    @Override
    public boolean estUnGuichet(){
        return true;
    }

    /**
     * Transformation du monde java en C
     * @return instructions en C
     */
    @Override
    public String toC(){

        StringBuilder codeC = new StringBuilder();

        Etape successeur = this.getSucesseur();

        codeC.append("P(ids, ").append("SEM_" + getNomDefine()).append(");\n");
        codeC.append("transfert(").append(getNomDefine()).append(", ").append(successeur.getNomDefine()).append(");\n");
        codeC.append(successeur.toC());
        codeC.append("V(ids, ").append("SEM_" + getNomDefine()).append(");\n");
        codeC.append(super.toC());

        return codeC.toString();
    }
}
