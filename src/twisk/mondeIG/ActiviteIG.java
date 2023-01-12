package twisk.mondeIG;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("activite")
public class ActiviteIG extends EtapeIG {

    protected int temps, ecartTemps;
    protected boolean activiteRestreinte;

    /**
     * Constructeur par défaut
     */
    public ActiviteIG(){
        super();
    }

    /**
     * Constructeur d'une activité
     * @param nom
     * @param idf
     * @param larg
     * @param haut
     */
    public ActiviteIG(String nom, String idf, int larg, int haut) {

        super(nom, idf, larg, haut);
        this.temps = 4;
        this.ecartTemps = 2;
        this.activiteRestreinte = false;
    }

    /**
     * Retourne le temps
     * @return temps
     */
    @JsonGetter("temps")
    public int getTemps(){
        return this.temps;
    }

    /**
     * Retourne l'ecart-temps
     * @return ecart-temps
     */
    @JsonGetter("ecartTemps")
    public int getEcartTemps(){
        return this.ecartTemps;
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
     * Vérifie si cette étape est une activité restreinte
     * @return activiteRestreinte
     */
    @Override
    @JsonGetter("activiteRestreinte")
    public boolean estUneActiviteRestreinte(){
        return this.activiteRestreinte;
    }

    /**
     * Setter de activiteRestreinte
     * @param activiteRestreinte Activite restreinte
     */
    @JsonSetter("activiteRestreinte")
    public void setActiviteRestreinte(boolean activiteRestreinte) { this.activiteRestreinte = activiteRestreinte; }

    /**
     * Change le temps
     * @param temps
     */
    @JsonSetter("temps")
    public void setTemps(int temps){
        this.temps = temps;
    }

    /**
     * Change l'ecart-temps
     * @param ecartTemps
     */
    @JsonSetter("ecartTemps")
    public void setEcartTemps(int ecartTemps){
        this.ecartTemps = ecartTemps;
    }

    /**
     * Fonction equals
     * @return equals
     */
    @Override
    public boolean equals(Object o) {
        if(o instanceof ActiviteIG) {
            ActiviteIG a = (ActiviteIG) o;
            return super.equals(a);
        }else{
            return false;
        }
    }

    /**
     * toString
     * @return string
     */
    public String toString(){
        return super.toString();
    }
}
