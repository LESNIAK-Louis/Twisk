package twisk.outils;

public class TailleComposants {

    private int largeurFenetre = 850;
    private int hauteurFenetre = 600;
    private int largeurActivite = 200;
    private int hauteurActivite = 115;
    private int largeurGuichet = 260;
    private int hauteurGuichet = 65;
    private int tailleEmplacementClient = 30;
    private int taillePointControle = 7;
    private int tailleTriangleFleche = 15;
    private int tailleBoutonOutils = 50;
    private int tailleClients = 5;
    private int tailleSliderClients = 250;
    private int tailleLargeurLoi = 150;
    private int tailleHauteurLoi = 40;

    private static TailleComposants instance = new TailleComposants();

    /**
     * Retourne l'instance de la classe
     * @return instance
     */
    public static TailleComposants getInstance() {
        return instance;
    }

    /**
     * Constructeur privé de TailleComposants
     */
    private TailleComposants(){ }

    /**
     * Getter de la hauteur de la combobox des lois
     * @return largeurFenetre
     */
    public int getTailleHauteurLoi() {
        return tailleHauteurLoi;
    }

    /**
     * Getter de la largeur de la combobox des lois
     * @return largeurFenetre
     */
    public int getTailleLargeurLoi() {
        return tailleLargeurLoi;
    }

    /**
     * Getter de la largeur de la fenêtre
     * @return largeurFenetre
     */
    public int getLargeurFenetre(){ return largeurFenetre; }

    /**
     * Getter de la hauteur de la fenêtre
     * @return hauteurFenetre
     */
    public int getHauteurFenetre(){ return hauteurFenetre; }

    /**
     * Getter de la largeur d'une Activité
     * @return largeurActivite
     */
    public int getLargeurActivite(){ return largeurActivite; }

    /**
     * Getter de la hauteur d'une Activité
     * @return hauteurGuichet
     */
    public int getHauteurActivite(){ return hauteurActivite; }

    /**
     * Getter de la hauteur d'un Guichet
     * @return hauteurActivite
     */
    public int getHauteurGuichet(){ return hauteurGuichet; }

    /**
     * Getter de la largeur d'un Guichet
     * @return largeurGuichet
     */
    public int getLargeurGuichet(){ return largeurGuichet; }

    /**
     * Getter de la taille d'un emplacement de client dans la file d'un guichet
     * @return tailleEmplacementClient
     */
    public int getTailleEmplacementClient(){ return tailleEmplacementClient; }

    /**
     * Getter de la taille d'un PointDeControleIG
     * @return taillePointControle
     */
    public int getTaillePointControle(){ return taillePointControle; }
    /**
     * Getter de la taille du triangle formant un flèche à chaque ArcIG créé
     * @return tailleTriangleFleche
     */
    public int getTailleTriangleFleche(){ return tailleTriangleFleche; }
    /**
     * Getter de la taille du bouton dans VueOutils
     * @return tailleBoutonOutils
     */
    public int getTailleBoutonOutils(){ return tailleBoutonOutils; }
    /**
     * Getter de la taille du cercle représentant un client
     * @return tailleClients
     */
    public int getTailleClients(){ return tailleClients; }
    /**
     * Getter de la largeur du slider de sélection du nombre de clients
     * @return tailleSliderClients
     */
    public int getTailleSliderClients(){ return tailleSliderClients; }
}
