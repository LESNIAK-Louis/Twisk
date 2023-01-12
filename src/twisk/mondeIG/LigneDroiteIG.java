package twisk.mondeIG;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("ligneDroite")
public class LigneDroiteIG extends ArcIG{

    /**
     * Constructeur par défaut
     */
    public LigneDroiteIG(){
        super();
    }

    /**
     * Constructeur d'une ligne droite à partir de 2 points de contrôle
     * @param p1 Premier point de contrôle
     * @param p2 Second point de contrôle
     */
    public LigneDroiteIG(PointDeControleIG p1, PointDeControleIG p2){

        super(p1, p2);
    }

    /**
     * Vérifie si l'arc est une ligne droite
     * @return true si l'arc est une ligne droite
     */
    @Override
    public boolean estUneLigneDroite() {
        return true;
    }

    /**
     * Vérifie si l'arc est une courbe
     * @return true si l'arc est une courbe
     */
    @Override
    public boolean estUneCourbe() {
        return false;
    }
}
