package twisk.simulation;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import twisk.mondeIG.LoiClients;
import twisk.outils.FabriqueNumero;

import java.util.Arrays;
import java.util.OptionalDouble;
import java.util.OptionalInt;

public class ResultatSimulation {

    private int id;
    private int nbClients;
    private LoiClients loi;
    private double moyenneLoi, ecartTypeLoi;
    private int[] tempsDansMonde, tempsDansActivite, tempsDansGuichet;

    /**
     * Constructeur par défaut
     */
    public ResultatSimulation(){}

    /**
     * Constructeur
     * @param clients GestionnaireClients de la simulation
     */
    public ResultatSimulation(GestionnaireClients clients){

        this.id = FabriqueNumero.getInstance().getNumeroResultat();
        this.nbClients = clients.getNbClients();

        this.tempsDansMonde = new int[nbClients];
        this.tempsDansActivite = new int[nbClients];
        this.tempsDansGuichet = new int[nbClients];
        int i = 0;
        for(Client c : clients){
            tempsDansMonde[i] = c.getTempsDansMonde();
            tempsDansActivite[i] = c.getTempsDansActivite();
            tempsDansGuichet[i] = c.getTempsAttenteGuichet();
            i++;
        }
    }

    /**
     * Getter de l'identifiant du résultat
     * @return identifiant du résultat
     */
    @JsonGetter("id")
    public int getId(){
        return this.id;
    }

    /**
     * Getter du nombre de clients
     * @return nombre de clients
     */
    @JsonGetter("nbClients")
    public int getNbClients(){
        return this.nbClients;
    }

    /**
     * Getter de la loi utilisée
     * @return loi
     */
    @JsonGetter("loi")
    public LoiClients getLoi(){
        return this.loi;
    }

    /**
     * Getter de la moyenne de la loi
     * @return moyenne de la loi
     */
    @JsonGetter("moyenneLoi")
    public double getMoyenneLoi(){
        return this.moyenneLoi;
    }

    /**
     * Getter de l'écart-type de la loi
     * @return écart-type de la loi
     */
    @JsonGetter("ecartTypeLoi")
    public double getEcartTypeLoi(){
        return this.ecartTypeLoi;
    }

    /**
     * Getter des valeurs des temps passés dans le monde par les clients
     * @return Tableau des temps passés dans le monde
     */
    @JsonGetter("tempsDansMonde")
    public int[] getTempsDansMonde(){
        return this.tempsDansMonde;
    }

    /**
     * Getter des valeurs des temps passés dans les activités par les clients
     * @return Tableau des temps passés dans les activités
     */
    @JsonGetter("tempsDansActivite")
    public int[] getTempsDansActivite(){
        return this.tempsDansActivite;
    }

    /**
     * Getter des valeurs des temps passés dans les guichets par les clients
     * @return Tableau des temps passés dans les guichets
     */
    @JsonGetter("tempsDansGuichet")
    public int[] getTempsDansGuichet(){
        return this.tempsDansGuichet;
    }

    /**
     * Setter de l'identifiant du résultat
     * @param id identifiant du résultat
     */
    @JsonSetter("id")
    public void setId(int id){
        this.id = id;
    }

    /**
     * Setter du nombre de clients
     * @param nbClients nombre de clients
     */
    @JsonSetter("nbClients")
    public void setNbClients(int nbClients){
        this.nbClients = nbClients;
    }

    /**
     * Setter de la loi utilisée
     * @param loi loi
     * @param moyenne moyenne
     * @param ecartType écart-type
     */
    @JsonIgnore
    public void setLoi(LoiClients loi, double moyenne, double ecartType){
        setLoi(loi);
        setMoyenneLoi(moyenne);
        setEcartTypeLoi(ecartType);
    }

    /**
     * Setter de la loi utilisée
     * @param loi loi
     */
    @JsonSetter("loi")
    public void setLoi(LoiClients loi){
        this.loi = loi;
    }

    /**
     * Setter de la moyenne de la loi
     * @param moyenne moyenne de la loi
     */
    @JsonSetter("moyenneLoi")
    public void setMoyenneLoi(double moyenne){
        this.moyenneLoi = moyenne;
    }

    /**
     * Setter de l'écart-type de la loi
     * @param ecartType écart-type de la loi
     */
    @JsonSetter("ecartTypeLoi")
    public void setEcartTypeLoi(double ecartType){
        this.ecartTypeLoi = ecartType;
    }

    /**
     * Setter des valeurs des temps passés dans le monde par les clients
     * @param resultats Tableau des temps passés dans le monde
     */
    @JsonSetter("tempsDansMonde")
    public void setTempsDansMonde(int[] resultats){
        this.tempsDansMonde = resultats;
    }

    /**
     * Setter des valeurs des temps passés dans les activités par les clients
     * @param resultats Tableau des temps passés dans les activités
     */
    @JsonSetter("tempsDansActivite")
    public void setTempsDansActivite(int[] resultats){
        this.tempsDansActivite = resultats;
    }

    /**
     * Setter des valeurs des temps passés dans les guichets par les clients
     * @param resultats Tableau des temps passés dans les guichets
     */
    @JsonSetter("tempsDansGuichet")
    public void setTempsDansGuichet(int[] resultats){
        this.tempsDansGuichet = resultats;
    }

    /**
     * Calcule le temps minimum passé par un client dans le monde
     * @return temps minimum dans le monde
     */
    @JsonIgnore
    public int calculerTempsMinDansMonde(){

        OptionalInt res = Arrays.stream(tempsDansMonde).min();
        if(res.isPresent())
            return res.getAsInt();
        else
            return 0;
    }

    /**
     * Calcule le temps maximum passé par un client dans le monde
     * @return temps maximum dans le monde
     */
    @JsonIgnore
    public int calculerTempsMaxDansMonde(){

        OptionalInt res = Arrays.stream(tempsDansMonde).max();
        if(res.isPresent())
            return res.getAsInt();
        else
            return 0;
    }

    /**
     * Calcule le temps minimum passé par un client dans les activités
     * @return temps minimum dans les activités
     */
    @JsonIgnore
    public int calculerTempsMinDansActivite(){

        OptionalInt res = Arrays.stream(tempsDansActivite).min();
        if(res.isPresent())
            return res.getAsInt();
        else
            return 0;
    }

    /**
     * Calcule le temps maximum passé par un client dans les activités
     * @return temps maximum dans les activités
     */
    @JsonIgnore
    public int calculerTempsMaxDansActivite(){

        OptionalInt res = Arrays.stream(tempsDansActivite).max();
        if(res.isPresent())
            return res.getAsInt();
        else
            return 0;
    }

    /**
     * Calcule le temps minimum passé par un client dans les guichets
     * @return temps minimum dans les guichets
     */
    @JsonIgnore
    public int calculerTempsMinDansGuichet(){

        OptionalInt res = Arrays.stream(tempsDansGuichet).min();
        if(res.isPresent())
            return res.getAsInt();
        else
            return 0;
    }

    /**
     * Calcule le temps maximum passé par un client dans les guichets
     * @return temps maximum dans les guichets
     */
    @JsonIgnore
    public int calculerTempsMaxDansGuichet(){

        OptionalInt res = Arrays.stream(tempsDansGuichet).max();
        if(res.isPresent())
            return res.getAsInt();
        else
            return 0;
    }

    /**
     * Calcule le temps moyen passé par un client dans le monde
     * @return temps moyen dans le monde
     */
    @JsonIgnore
    public double calculerTempsMoyenDansMonde(){

        OptionalDouble res = Arrays.stream(tempsDansMonde).average();
        if(res.isPresent())
            return res.getAsDouble();
        else
            return 0;
    }

    /**
     * Calcule le temps moyen passé par un client dans les activités
     * @return temps moyen dans les activités
     */
    @JsonIgnore
    public double calculerTempsMoyenDansActivite(){

        OptionalDouble res = Arrays.stream(tempsDansActivite).average();
        if(res.isPresent())
            return res.getAsDouble();
        else
            return 0;
    }

    /**
     * Calcule le temps moyen passé par un client dans les guichets
     * @return temps moyen dans les guichets
     */
    @JsonIgnore
    public double calculerTempsMoyenDansGuichet(){

        OptionalDouble res = Arrays.stream(tempsDansGuichet).average();
        if(res.isPresent())
            return res.getAsDouble();
        else
            return 0;
    }

    /**
     * ToString
     * @return "Simulation n°X" où X est l'id
     */
    @Override
    public String toString(){
        return "Simulation n°" + id;
    }
}
