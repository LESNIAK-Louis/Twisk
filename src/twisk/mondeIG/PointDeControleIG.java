package twisk.mondeIG;

import com.fasterxml.jackson.annotation.*;

import java.awt.*;

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class PointDeControleIG {

    private String id;
    private Position position;
    private int posX, posY;

    private EtapeIG etape;

    @JsonIgnore
    private boolean visible;

    /**
     * Constructeur par défaut
     */
    public PointDeControleIG(){
        visible = true;
    }

    /**
     * Constructeur d'un point de contrôle
     * @param nom
     * @param etape
     * @param x
     * @param y
     */
    public PointDeControleIG(String nom, Position pos, EtapeIG etape, int x, int y){

        id = nom;
        position = pos;
        posX = x;
        posY = y;
        this.etape = etape;
        visible = true;
    }

    /**
     * Retourne l'identifiant du point de contrôle
     * @return id
     */
    @JsonGetter("id")
    public String getId() {
        return id;
    }

    /**
     * Retourne la position du point de contrôle
     * @return position
     */
    @JsonGetter("position")
    public Position getPosition() {
        return position;
    }

    /**
     * Retourne l'abscisse du point de contrôle
     * @return posX
     */
    @JsonGetter("posX")
    public int getPosX() {
        return posX;
    }

    /**
     * Retourne l'ordonnée du point de contrôle
     * @return posY
     */
    @JsonGetter("posY")
    public int getPosY() {
        return posY;
    }

    /**
     * Retourne l'étape à laquelle le point de contrôle est attaché
     * @return etape
     */
    @JsonGetter("etape")
    public EtapeIG getEtape() {
        return etape;
    }

    /**
     * Vérifie si le point de contrôle est visible
     * @return true si le point de contrôle est visible
     */
    public boolean estVisible(){ return visible; }

    /**
     * Change l'identifiant du point de contrôle
     * @param id Identifiant
     */
    @JsonSetter("id")
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Change la position du point de contrôle
     * @param position Position
     */
    @JsonSetter("position")
    public void setPosition(Position position) {
        this.position = position;
    }

    /**
     * Change l'abscisse du point de contrôle
     * @param posX posX
     */
    @JsonSetter("posX")
    public void setPosX(int posX) {
        this.posX = posX;
    }

    /**
     * Change l'ordonnée du point de contrôle
     * @param posY posY
     */
    @JsonSetter("posY")
    public void setPosY(int posY) {
        this.posY = posY;
    }

    /**
     * Change l'étape à laquelle le point de contrôle est attaché
     * @param etape Etape
     */
    @JsonSetter("etape")
    public void setEtape(EtapeIG etape) {
        this.etape = etape;
    }

    /**
     * Déplace le point de contrôle aux coordonnées données
     * @param x
     * @param y
     */
    public void deplacerVers(int x, int y){
        this.posX = x;
        this.posY = y;
    }

    /**
     * Fonction equals
     * @return equals
     */
    @Override
    public boolean equals(Object o) {
        if(o instanceof PointDeControleIG){
            PointDeControleIG p = (PointDeControleIG) o;
            return this.getId().equals(p.getId());
        } else return false;
    }

    /**
     * toString
     * @return string
     */
    @Override
    public String toString(){
        return "PointDeControle " + id;
    }
}
