package twisk.mondeIG;

public enum LoiClients {

    UNIFORME(0, "Uniforme"),
    NORMALE(1, "Normale"),
    POISSON(2, "Poisson");

    private int id;
    private String nom;

    /**
     * Constructeur de loi
     * @param id Indice de la loi.
     * @param nom Nom de la loi
     */
    private LoiClients(int id, String nom)
    {
        this.id = id;
        this.nom = nom;
    }

    /**
     * Retourne l'indice de la loi
     * @return Indice de la loi.
     */
    public int getId(){

        return this.id;
    }

    /**
     * Retourne le nom de la loi.
     * @return Nom de la loi.
     */
    public String getNom(){
        return this.nom;
    }

    /**
     * toString
     */
    public String toString(){

        return getNom();
    }
}
