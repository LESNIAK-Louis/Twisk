package twisk.mondeIG;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = LigneDroiteIG.class, name = "ligneDroite"),
        @JsonSubTypes.Type(value = CourbeIG.class, name = "courbe")
})
public abstract class ArcIG {

    protected PointDeControleIG p1,p2;

    /**
     * Constructeur par défaut
     */
    public ArcIG(){

    }

    /**
     * Constructeur d'un arc à partir de 2 points de contrôle
     * @param p1
     * @param p2
     */
    public ArcIG(PointDeControleIG p1, PointDeControleIG p2){
        this.p1 = p1;
        this.p2 = p2;
    }

    /**
     * Retourne le premier point de contrôle
     * @return point
     */
    @JsonGetter("p1")
    public PointDeControleIG getP1(){
        return p1;
    }

    /**
     * Retourne le second point de contrôle
     * @return point
     */
    @JsonGetter("p2")
    public PointDeControleIG getP2(){
        return p2;
    }

    /**
     * Change le premier point de contrôle
     * @param p1 point
     */
    @JsonSetter("p1")
    public void setP1(PointDeControleIG p1){
        this.p1 = p1;
    }

    /**
     * Change le second point de contrôle
     * @param p2 point
     */
    @JsonSetter("p2")
    public void setP2(PointDeControleIG p2){
        this.p2 = p2;
    }

    /**
     * Vérifie si l'arc est une ligne droite
     * @return true si l'arc est une ligne droite
     */
    public abstract boolean estUneLigneDroite();

    /**
     * Vérifie si l'arc est une courbe
     * @return true si l'arc est une courbe
     */
    public abstract boolean estUneCourbe();

    /**
     * Fonction equals
     * @return equals
     */
    @Override
    public boolean equals(Object o) {
        if(o instanceof ArcIG){
            ArcIG a = (ArcIG) o;
            return this.getP1().getEtape().equals(a.getP1().getEtape())
                    && this.getP2().getEtape().equals(a.getP2().getEtape());
        }else return false;
    }
}
