package twisk.mondeIG;

import com.fasterxml.jackson.annotation.*;
import twisk.monde.GestionnaireSuccesseurs;
import twisk.outils.TailleComposants;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ActiviteIG.class, name = "activite"),
        @JsonSubTypes.Type(value = GuichetIG.class, name = "guichet")
})
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public abstract class EtapeIG implements Iterable<PointDeControleIG> {

    protected String nom;
    protected String id;
    protected int posX, posY;
    protected int largeur, hauteur;
    protected boolean entree, sortie;
    protected GestionnaireSuccesseursIG successeurs;

    protected HashMap<Position, PointDeControleIG> controles;

    @JsonIgnore
    protected boolean enDeplacement;

    /**
     * Constructeur par défaut
     */
    public EtapeIG(){}

    /**
     * Constructeur d'une étape
     * @param nom
     * @param idf
     * @param larg
     * @param haut
     */
    public EtapeIG(String nom, String idf, int larg, int haut){

        this.nom = nom;
        this.id = idf;
        this.largeur = larg;
        this.hauteur = haut;
        Random r = new Random();
        this.posX = r.nextInt(600-larg);
        this.posY = r.nextInt(600-haut - TailleComposants.getInstance().getTailleBoutonOutils());
        this.entree = false;
        this.sortie = false;
        this.successeurs = new GestionnaireSuccesseursIG();
        this.enDeplacement = false;
        placerPointDeControles();
    }
    /**
     * Retourne la hashmap des points de controle
     * @return controles
     */
    @JsonGetter("controles")
    public HashMap<Position, PointDeControleIG> getControles() {
        return controles;
    }

    /**
     * Retourne GestionnaireSuccesseursIG
     * @return successeurs
     */
    @JsonGetter("successeurs")
    public GestionnaireSuccesseursIG getSuccesseurs() {
        return successeurs;
    }

    /**
     * Retourne le nom de l'étape
     * @return nom
     */
    @JsonGetter("nom")
    public String getNom(){
        return this.nom;
    }

    /**
     * Retourne l'identifiant de l'étape
     * @return id
     */
    @JsonGetter("id")
    public String getId(){
        return this.id;
    }

    /**
     * Retourne l'abscisse de l'étape
     * @return posX
     */
    @JsonGetter("posX")
    public int getPosX(){
        return this.posX;
    }

    /**
     * Retourne l'ordonnée de l'étape
     * @return posY
     */
    @JsonGetter("posY")
    public int getPosY(){
        return this.posY;
    }

    /**
     * Retourne la largeur de l'étape
     * @return largeur
     */
    @JsonGetter("largeur")
    public int getLargeur(){
        return this.largeur;
    }

    /**
     * Retourne la hauteur de l'étape
     * @return hauteur
     */
    @JsonGetter("hauteur")
    public int getHauteur(){
        return this.hauteur;
    }

    /**
     * Vérifie si l'étape est une entrée
     * @return true si l'étape est une entrée
     */
    @JsonGetter("entree")
    public boolean estUneEntree() { return this.entree; }
    /**
     * Vérifie si l'étape est une sortie
     * @return true si l'étape est une sortie
     */
    @JsonGetter("sortie")
    public boolean estUneSortie() { return this.sortie; }

    /**
     * Vérifie si l'étape est une activité
     * @return true si l'étape est une activité
     */
    public boolean estUneActivite() { return false; }

    /**
     * Vérifie si l'étape est une activité restrinte
     * @return true si l'étape est une activité restreinte
     */
    public boolean estUneActiviteRestreinte() { return false; }
    /**
     * Vérifie si l'étape est un guichet
     * @return true si l'étape est un guichet
     */
    public boolean estUnGuichet() { return false; }

    /**
     * Retourne le point de contrôle de l'étape à la position indiquée
     * @param position
     * @return point de contrôle
     */
    public PointDeControleIG getPointDeControle(Position position){
        return controles.get(position);
    }

    /**
     * Getter du nombre de successeur de l'EtapeIG actuelle
     * @return nombre de successeurs
     */
    @JsonIgnore
    public int nbSuccesseurs() { return successeurs.nbEtapes(); }

    /**
     * Getter du booléen qui indique si l'étape est en train d'être déplacée
     * @return true si l'étape est en train d'être déplacée
     */
    public boolean enDeplacement(){
        return this.enDeplacement;
    }

    /**
     * Setter du booléen qui indique si l'étape est en train d'être déplacée
     * @param b true si l'étape est en train d'être déplacée
     */
    @JsonIgnore
    public void setEnDeplacement(boolean b){
        this.enDeplacement = b;
    }

    /**
     * Ajout d'une ou d'une collection d'EtapeIG en successeur
     * @param e Collection d'EtapeIG
     */
    public void ajouterSuccesseur(EtapeIG ... e) {
        successeurs.ajouter(e);
    }

    /**
     * Suppression d'une ou d'une collection d'EtapeIG en successeur
     * @param e Collection d'EtapeIG
     */
    public void supprimerSuccesseur(EtapeIG ... e) {
        successeurs.supprimer(e);
    }

    /**
     * Retourne le premier successeur de l'EtapeIG
     * @return une EtapeIG
     */
    @JsonIgnore
    public EtapeIG getSuccesseur() { return successeurs.getSuccesseur(); }

    /**
     * Setter de la hashmap des points de contrôle
     * @param controles Points de contrôle
     */
    @JsonSetter("controles")
    public void setControles(HashMap<Position, PointDeControleIG> controles) {
        this.controles = controles;
    }

    /**
     * Setter du gestionnaire de successeurs
     * @param successeurs Gestionnaire de successeurs
     */
    @JsonSetter("successeurs")
    public void setSuccesseurs(GestionnaireSuccesseursIG successeurs){
        this.successeurs = successeurs;
    }

    /**
     * Change le nom de l'étape
     * @param nom
     */
    @JsonSetter("nom")
    public void setNom(String nom){
        this.nom = nom;
    }

    /**
     * Change l'identifiant de l'étape
     * @param id Identifiant
     */
    @JsonSetter("id")
    public void setId(String id) { this.id = id; }

    /**
     * Change l'état d'entrée de l'étape
     * @param b
     */
    @JsonSetter("entree")
    public void setEntree(boolean b) { this.entree = b; }

    /**
     * Change l'état de sortie de l'étape
     * @param b
     */
    @JsonSetter("sortie")
    public void setSortie(boolean b) { this.sortie = b; }

    /**
     * Change la position en x de l'étape
     * @param posX posX
     */
    @JsonSetter("posX")
    public void setPosX(int posX) { this.posX = posX; }

    /**
     * Change la position en y de l'étape
     * @param posY posY
     */
    @JsonSetter("posY")
    public void setPosY(int posY) { this.posY = posY; }

    /**
     * Change la largeur de l'étape
     * @param largeur Largeur
     */
    @JsonSetter("largeur")
    public void setLargeur(int largeur) { this.largeur = largeur; }

    /**
     * Change la hauteur de l'étape
     * @param hauteur Hauteur
     */
    @JsonSetter("hauteur")
    public void setHauteur(int hauteur) { this.hauteur = hauteur; }

    /**
     * Déplace l'étape aux coordonnées données
     * @param x
     * @param y
     */
    public void deplacerVers(int x, int y){

        for(PointDeControleIG pt : this){
            pt.deplacerVers(pt.getPosX() + x - this.getPosX(), pt.getPosY() + y - this.getPosY());
        }
        this.posX = x;
        this.posY = y;
    }

    /**
     * Itérateur sur les points de contrôle de l'étape
     * @return Itérateur
     */
    @Override
    public Iterator<PointDeControleIG> iterator() {
        return controles.values().iterator();
    }

    /**
     * Itérateur sur les successeurs de l'étape
     * @return Itérateur
     */
    public Iterator<EtapeIG> iteratorSucesseurs() {
        return successeurs.iterator();
    }

    /**
     * Place les points de contrôle de l'étape
     */
    private void placerPointDeControles(){
        this.controles = new HashMap<Position, PointDeControleIG>(4);

        controles.put(Position.GAUCHE, new PointDeControleIG(getNom() + getId() + "Gauche", Position.GAUCHE,this, posX, posY+(hauteur/2)));
        controles.put(Position.DROITE, new PointDeControleIG(getNom() + getId() + "Droit", Position.DROITE,this, posX+largeur, posY+(hauteur/2)));

        if(estUneActivite()){
            controles.put(Position.HAUT, new PointDeControleIG(getNom() + getId() + "Haut", Position.HAUT, this, posX+(largeur/2),posY));
            controles.put(Position.BAS, new PointDeControleIG(getNom() + getId() + "Bas", Position.BAS,this,posX+(largeur/2),posY+hauteur));
        }
    }

    /**
     * chercher si l’étape est accessible depuis l’étape etape.
     * @param etape
     * @return
     */
    public boolean estAccessibleDepuis(EtapeIG etape){
       if(etape.successeurs.contains(this))
           return true;

       boolean ret = false;
       Iterator<EtapeIG> ite = etape.iteratorSucesseurs();
       while(ite.hasNext())
       {
           ret = ret || estAccessibleDepuis(ite.next());
       }

       return ret;
    }

    /**
     * Fonction equals
     * @return equals
     */
    @Override
    public boolean equals(Object o) {
        if(o instanceof EtapeIG){
            EtapeIG e = (EtapeIG) o;
            return this.getNom().equals(e.getNom())
                    && this.getId().equals(e.getId());
        }else return false;
    }

    /**
     * toString
     * @return string
     */
    public String toString(){
        StringBuilder str = new StringBuilder();
        str.append(getNom()).append("-").append(getId()).append("-").append(getPosX()).append(',');
        str.append(getPosY()).append("-").append(getLargeur()).append(',').append(getHauteur());
        return str.toString();
    }
}
