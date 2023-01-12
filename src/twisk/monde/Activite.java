package twisk.monde;

public class Activite extends Etape {

    protected int temps;
    protected int ecartTemps;

    /**
     * Constructeur d'Activité via son nom
     * @param nom
     */
    public Activite(String nom) {

        super(nom);
        temps = 4;
        ecartTemps = 2;
    }

    /**
     * Constructeur d'Activité via son nom, le temps et l'ecart-temps
     * @param nom
     * @param t Temps
     * @param e Ecart-temps
     */
    public Activite(String nom, int t, int e){

        this(nom);
        this.temps = t;
        this.ecartTemps = e;
    }

    /**
     * Getter du temps de l'activité
     * @return temps
     */
    public int getTemps(){
        return temps;
    }

    /**
     * Getter de l'écart-temps de l'activité
     * @return ecartTemps
     */
    public int getEcartTemps(){
        return ecartTemps;
    }


    /**
     * Vérifie si cette étape est une activité
     * @return true
     */
    @Override
    public boolean estUneActivite(){
        return true;
    }

    /**
     * Transformation du monde java en C
     * @return instructions en C
     */
    public String toC()
    {
        StringBuilder str = new StringBuilder();

        if(this.estUneEntree())
            str.append("delaiLoi(" + ((SasEntree)this).getLoiClient() + ", " + ((SasEntree)this).getMoyenneLoiClient() + ", " + ((SasEntree)this).getEcartTypeLoiClient() + ");" + "\n");
        else
            str.append("delai(" + this.getTemps() + ", " + this.getEcartTemps() + ");" + "\n");

        str.append(super.toC());
        
        return str.toString();
    }
}
