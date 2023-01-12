package twisk.monde;

public class SasEntree extends Activite {

    private int loiClient;
    private double moyenneLoi;
    private double ecartTypeLoi;

    /**
     * Constructeur du SasEntree
     */
    public SasEntree(){

        super("Entrée");
        this.loiClient = 0;
        this.moyenneLoi = 4.0;
        this.ecartTypeLoi = 2.0;
    }

    /**
     * Vérifie si cette étape est une entrée.
     * @return true
     */
    @Override
    public boolean estUneEntree(){
        return true;
    }

    /**
     * Getter de la loi d'arrivée des clients.
     * @return loi d'arrivée des clients
     */
    public int getLoiClient(){
        return this.loiClient;
    }

    /**
     * Getter de la moyenne de la loi d'arrivée des clients.
     * @return moyenne de la loi d'arrivée des clients
     */
    public double getMoyenneLoiClient(){
        return this.moyenneLoi;
    }

    /**
     * Getter de l'écart-type de la loi d'arrivée des clients.
     * @return écart-type de la loi d'arrivée des clients
     */
    public double getEcartTypeLoiClient(){
        return this.ecartTypeLoi;
    }

    /**
     * Setter de la loi d'arrivée des clients.
     * @param loiClient Loi d'arrivée des clients
     * @param moyenne Moyenne de la loi
     * @param ecartType Ecart-type de la loi
     */
    public void setLoiClient(int loiClient, double moyenne, double ecartType){
        this.loiClient = loiClient;
        this.moyenneLoi = moyenne;
        this.ecartTypeLoi = ecartType;
    }

    /**
     * Transformation du monde java en C
     * @return instructions en C
     */
    @Override
    public String toC()
    {
        StringBuilder str = new StringBuilder();
        str.append("entrer(" + this.getNomDefine() + ");\n");
        str.append(super.toC());

        return str.toString();
    }

}
