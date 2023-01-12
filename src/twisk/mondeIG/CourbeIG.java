package twisk.mondeIG;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("courbe")
public class CourbeIG extends ArcIG{

    private int x1, y1, x2, y2;

    /**
     * Constructeur par défaut
     */
    public CourbeIG(){
        super();
    }

    /**
     * Constructeur d'une courbe à partir de 2 points de contrôle
     * et des coordonnées de 2 points intermédiaires
     * @param p1 Premier point de contrôle
     * @param x1 Abscisse du premier point intermédiaire
     * @param y1 Ordonnée du premier point intermédiaire
     * @param x2 Abscisse du second point intermédiaire
     * @param y2 Ordonnée du second point intermédiaire
     * @param p2 Second point de contrôle
     */
    public CourbeIG(PointDeControleIG p1, int x1, int y1, int x2, int y2, PointDeControleIG p2){

        super(p1, p2);
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    /**
     * Retourne l'abscisse du premier point intermédiaire
     * @return int
     */
    @JsonGetter("x1")
    public int getX1(){ return this.x1; }

    /**
     * Retourne l'ordonnée du premier point intermédiaire
     * @return int
     */
    @JsonGetter("y1")
    public int getY1(){ return this.y1; }

    /**
     * Retourne l'abscisse du second point intermédiaire
     * @return int
     */
    @JsonGetter("x2")
    public int getX2(){ return this.x2; }
    /**
     * Retourne l'ordonnée du second point intermédiaire
     * @return int
     */
    @JsonGetter("y2")
    public int getY2(){ return this.y2; }

    /**
     * Change l'abscisse du premier point intermédiaire
     * @param x1 x1
     */
    @JsonSetter("x1")
    public void setX1(int x1){ this.x1 = x1; }

    /**
     * Change l'ordonnée du premier point intermédiaire
     * @param y1 y1
     */
    @JsonSetter("y1")
    public void setY1(int y1){ this.y1 = y1; }

    /**
     * Change l'abscisse du second point intermédiaire
     * @param x2 x2
     */
    @JsonSetter("x2")
    public void setX2(int x2){ this.x2 = x2; }

    /**
     * Change l'ordonnée du second point intermédiaire
     * @param y2 y2
     */
    @JsonSetter("y2")
    public void setY2(int y2){ this.y2 = y2; }

    /**
     * Vérifie si l'arc est une ligne droite
     * @return true si l'arc est une ligne droite
     */
    @Override
    public boolean estUneLigneDroite() {
        return false;
    }

    /**
     * Vérifie si l'arc est une courbe
     * @return true si l'arc est une courbe
     */
    @Override
    public boolean estUneCourbe() {
        return true;
    }
}
