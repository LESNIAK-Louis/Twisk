package twisk.monde;

public class ActiviteRestreinte extends Activite {

    /**
     * Constructeur d'une Activité Restreinte via son nom
     * @param nom
     */
    public ActiviteRestreinte(String nom){
        super(nom);
    }

    /**
     * Constructeur d'une Activité Restreinte via son nom, le temps et l'ecart-temps
     * @param nom
     * @param t Temps
     * @param e Ecart-temps
     */
    public ActiviteRestreinte(String nom, int t, int e){
        super(nom, t, e);
    }

    /**
     * Transformation du monde java en C
     * @return instructions en C
     */
    @Override
    public String toC(){
        StringBuilder codeC = new StringBuilder();
        codeC.append("delai(" + this.getTemps() + ", " + this.getEcartTemps() + ");" + "\n");
        return codeC.toString();
    }
}
