package twisk.mondeIG;

public enum Position {

    HAUT(0, "Haut"),
    GAUCHE(1, "Gauche"),
    BAS(2, "Bas"),
    DROITE(3, "Droite");

    private int id;
    private String nom;

    /**
     * Constructeur de position
     * @param id Indice de la position.
     * @param nom Nom de la position
     */
    private Position(int id, String nom)
    {
        assert(id == 0 || id == 1|| id == 2 || id == 3):"Indice non valide";
        assert(nom.equals("Haut") || nom.equals("Bas") || nom.equals("Gauche") || nom.equals("Droite")):"Position non valide";
        this.id = id;
        this.nom = nom;
    }

    /**
     * Retourne l'indice de la position.
     * @return Indice de la position.
     */
    public int getId(){

        return this.id;
    }

    /**
     * Retourne le nom de la position.
     * @return Nom de la position.
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
