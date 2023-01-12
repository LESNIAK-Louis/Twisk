package twisk.mondeIG;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("guichet")
public class GuichetIG extends EtapeIG {

    protected int nbJetons;
    protected SensFile sensFile;

    /**
     * Constructeur par défaut
     */
    public GuichetIG(){
        super();
    }

    /**
     * Constructeur d'un guichet
     * @param nom
     * @param idf
     * @param larg
     * @param haut
     */
    public GuichetIG(String nom, String idf, int larg, int haut) {

        super(nom, idf, larg, haut);
        this.nbJetons = 3;
        this.sensFile = SensFile.INCONNU;
    }

    /**
     * Retourne le nombre de jetons
     * @return nbJetons
     */
    @JsonGetter("nbJetons")
    public int getNbJetons(){
        return this.nbJetons;
    }

    /**
     * Retourne le sens de la file
     * @return sens de la file
     */
    @JsonGetter("sensFile")
    public SensFile getSensFile(){
        return this.sensFile;
    }

    /**
     * Vérifie si cette étape est un guichet
     * @return true
     */
    @Override
    public boolean estUnGuichet(){
        return true;
    }

    /**
     * Défini un nombre de jeton(s) au guichet
     * @param nbJetons
     */
    @JsonSetter("nbJetons")
    public void setNbJetons(int nbJetons){
        this.nbJetons = nbJetons;
    }

    /**
     * Défini le sens de circulation dans la file
     * @param sens
     */
    @JsonSetter("sensFile")
    public void setSensFile(SensFile sens){
        this.sensFile = sens;
    }

    /**
     * Fonction equals
     * @return equals
     */
    @Override
    public boolean equals(Object o) {
        if(o instanceof GuichetIG) {
            GuichetIG a = (GuichetIG) o;
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
