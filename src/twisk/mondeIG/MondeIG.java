package twisk.mondeIG;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import javafx.concurrent.Task;
import javafx.geometry.Point2D;
import twisk.ClientTwisk;
import twisk.exceptions.ArcNonValideException;
import twisk.exceptions.MondeException;
import twisk.exceptions.ParametresIncorrectsException;
import twisk.exceptions.TwiskIOException;
import twisk.monde.*;
import twisk.outils.*;
import twisk.simulation.Client;
import twisk.simulation.GestionnaireClients;
import twisk.simulation.ResultatSimulation;
import twisk.vues.Observateur;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class MondeIG extends SujetObserve implements Observateur, Iterable<EtapeIG> {

    private HashMap<String, EtapeIG> etapes;
    private ArrayList<ArcIG> arcs;
    private int nbClients;
    private LoiClients loiClient;
    private double moyenneLoi;
    private double ecartTypeLoi;
    private ArrayList<ResultatSimulation> resultatsSimulations;

    @JsonIgnore
    private PointDeControleIG pointSelectionne;
    @JsonIgnore
    private ArrayList<EtapeIG> etapesSelect;
    @JsonIgnore
    private ArrayList<ArcIG> arcsSelect;
    @JsonIgnore
    private ArrayList<Point2D> coordsPointsCourbe;
    @JsonIgnore
    private String fond;
    @JsonIgnore
    private CorrespondanceEtapes correspondanceEtapes;
    @JsonIgnore
    private GestionnaireClients clients;

    /**
     * Constructeur de MondeIG
     */
    public MondeIG(){

        initialiser();
    }

    /**
     * Initialisation d'un nouveau monde
     */
    public void initialiser(){

        this.etapes = new HashMap<String, EtapeIG>();
        this.arcs = new ArrayList<ArcIG>();
        this.etapesSelect = new ArrayList<EtapeIG>();
        this.arcsSelect = new ArrayList<ArcIG>();
        this.coordsPointsCourbe = new ArrayList<Point2D>(3);
        this.fond = "grille";
        this.nbClients = 5;
        this.loiClient = LoiClients.UNIFORME;
        this.moyenneLoi = 4.0;
        this.ecartTypeLoi = 2.0;
        this.resultatsSimulations = new ArrayList<ResultatSimulation>();
        FabriqueIdentifiant.getInstance().reset();
        FabriqueNumero.getInstance().reset();
        FabriqueNumero.getInstance().setNumeroResultat(1);

        notifierObservateurs();
    }

    /**
     * Retourne la loi choisie
     * @return loi
     */
    @JsonGetter("loiClient")
    public LoiClients getLoi() {
        return this.loiClient;
    }

    /**
     * Retourne la valeur de la moyenne en param??tre de la loi.
     * @return moyenne de la loi
     */
    @JsonGetter("moyenneLoi")
    public double getMoyenneLoi() {
        return this.moyenneLoi;
    }

    /**
     * Retourne la valeur de l'??cart-type en param??tre de la loi.
     * @return ??cart-type de la loi
     */
    @JsonGetter("ecartTypeLoi")
    public double getEcartTypeLoi() {
        return this.ecartTypeLoi;
    }

    /**
     * Retourne le nombre de clients
     * @return nombre de clients
     */
    @JsonGetter("nbClients")
    public int getNbClients(){
        return this.nbClients;
    }

    /**
     * Retourne le nombre d'entr??e
     * @return nombre d'entr??e
     */
    @JsonIgnore
    public int getNombreEntree(){
        int nbEntree = 0;
        for(EtapeIG e : this)
        {
            if(e.estUneEntree()) nbEntree++;
        }
        return nbEntree;
    }

    /**
     * Retourne le nombre de sortie
     * @return nombre de sortie
     */
    @JsonIgnore
    public int getNombreSortie(){
        int nbSortie = 0;
        for(EtapeIG e : this)
        {
            if(e.estUneSortie()) nbSortie++;
        }
        return nbSortie;
    }

    /**
     * Retourne le nombre d'??tapes
     * @return nombre d'??tapes
     */
    @JsonIgnore
    public int getNombreEtapes(){
        return etapes.size();
    }

    /**
     * Retourne l'??tape correspondant ?? l'identifiant
     * @param id identifiant
     * @return etape
     */
    @JsonIgnore
    public EtapeIG getEtape(String id){
        return etapes.get(id);
    }

    /**
     * Retourne l'??tapeIG correspondant ?? l'??tape
     * @param e Etape
     * @return etape
     */
    @JsonIgnore
    public EtapeIG getEtape(Etape e){
        assert(correspondanceEtapes != null):"correspondanceEtapes n'existe encore pas";

        return correspondanceEtapes.get(e);
    }

    /**
     * Retourne la hashmap des ??tapes
     * @return etapes
     */
    @JsonGetter("etapes")
    public HashMap<String, EtapeIG> getEtapes(){return etapes;}

    /**
     * Retourne l'arraylist des arcs
     * @return arcs
     */
    @JsonGetter("arcs")
    public ArrayList<ArcIG> getArcs(){return arcs;}

    /**
     * Retourne l'??tape correspondant ?? l'entr??e
     * @return null si aucune, uniquement la premi??re si il y en a plusieurs
     */
    @JsonIgnore
    public EtapeIG getEntree() {
        for(EtapeIG e : this)
        {
            if(e.estUneEntree())
                return e;
        }
        return null;
    }

    /**
     * Retourne l'??tape correspondant ?? la sortie
     * @return null si aucune, uniquement la premi??re si il y en a plusieurs
     */
    @JsonIgnore
    public EtapeIG getSortie() {
        for(EtapeIG e : this)
        {
            if(e.estUneSortie())
                return e;
        }
        return null;
    }

    /**
     * Retourne le nombre d'arcs
     * @return nombre d'arcs
     */
    @JsonIgnore
    public int getNombreArcs(){ return arcs.size(); }

    /**
     * Retourne l'arc correspondant
     * @param i indice de l'arc
     * @return arc
     */
    @JsonIgnore
    public ArcIG getArc(int i){ return arcs.get(i); }

    /**
     * V??rifie si l'??tape est s??lectionn??e
     * @param etape
     * @return true si l'??tape est s??lectionn??e
     */
    public boolean estSelectionnee(EtapeIG etape){
        return etapesSelect.contains(etape);
    }

    /**
     * V??rifie si l'arc est s??lectionn??
     * @param arc
     * @return true si l'arc est s??lectionn??
     */
    public boolean estSelectionne(ArcIG arc){
        return arcsSelect.contains(arc);
    }

    /**
     * V??rifie si le point de contr??le est s??lectionn??
     * @param point
     * @return true si le point de contr??le est s??lectionn??
     */
    public boolean estSelectionne(PointDeControleIG point){
        return pointSelectionne != null && pointSelectionne.equals(point);
    }

    /**
     * V??rifie si un arc est en train d'??tre cr????
     * @return true si un arc est en train d'??tre cr????
     */
    public boolean arcEnCoursDeCreation(){
        return pointSelectionne != null;
    }

    /**
     * Retourne le nombre d'??tapes s??lectionn??es
     * @return nombre d'??tapes s??lectionn??es
     */
    @JsonIgnore
    public int getNombreEtapesSelectionnes(){
        return etapesSelect.size();
    }

    /**
     * Retourne le nombre d'arcs s??lectionn??s
     * @return nombre d'arcs s??lectionn??s
     */
    @JsonIgnore
    public int getNombreArcsSelectiones(){
        return arcsSelect.size();
    }

    /**
     * Retourne le nombre de points interm??diaires s??lectinn??s sur la courbe
     * @return nombre de points interm??diaires s??lectinn??s sur la courbe
     */
    @JsonIgnore
    public int getNombreCoordsPointsCourbe(){ return coordsPointsCourbe.size(); }

    /**
     * Retourne la premi??re ??tape s??lectionn??e
     * @return etape s??lectionn??e
     */
    public EtapeIG premEtapeSelectionnee() { return etapesSelect.get(0); }

    /**
     * Retourne le chemin de l'image du fond
     * @return le chemin de l'image du fond
     */
    @JsonIgnore
    public String getFond(){
        return this.fond;
    }

    /**
     * Retourne la liste des r??sultats de simulation
     * @return liste des r??sultats de simulation
     */
    @JsonGetter("resultatsSimulations")
    public ArrayList<ResultatSimulation> getResultatsSimulation(){
        return this.resultatsSimulations;
    }

    /**
     * Retourne le nombre de r??sultats de simulation enregistr??s
     * @return nombre de r??sultats de simulation
     */
    @JsonIgnore
    public int nbResultatsSimulation(){
        return this.resultatsSimulations.size();
    }

    /**
     * Retourne un boolean correspondant ?? si la simulation est en cours ou pas
     * @return boolean true si en cours, false sinon
     */
    public boolean enSimulation() {
        return ThreadsManager.getInstance().getNbThreads() > 0;
    }

    /**
     * Change la loi choisie
     * @param loiClient Indice de la loi
     */
    @JsonSetter("loiClient")
    public void setLoi(LoiClients loiClient) {
        this.loiClient = loiClient;
    }

    /**
     * Change la valeur de la moyenne en param??tre de la loi.
     * @param moyenne Moyenne de la loi
     */
    @JsonSetter("moyenneLoi")
    public void setMoyenneLoi(double moyenne) {
        this.moyenneLoi = moyenne;
    }

    /**
     * Change la valeur de l'??cart-type en param??tre de la loi.
     * @param ecartType Ecart-type de la loi
     */
    @JsonSetter("ecartTypeLoi")
    public void setEcartTypeLoi(double ecartType) {
        this.ecartTypeLoi = ecartType;
    }

    /**
     * Change les valeur de la moyenne et de l'??cart-type en param??tres de la loi.
     * @param moyenne Moyenne de la loi
     * @param ecartType Ecart-type de la loi
     */
    @JsonIgnore
    public void setParametresLoi(double moyenne, double ecartType) throws ParametresIncorrectsException{
        if(moyenne <= 0 || ecartType <= 0)
            throw new ParametresIncorrectsException("les valeurs doivent ??tre strictement positives.");
        if(moyenne - ecartType <= 0)
            throw new ParametresIncorrectsException("l'??cart-type est trop ??lev??.");

        setMoyenneLoi(moyenne);
        setEcartTypeLoi(ecartType);

        notifierObservateurs();
    }

    /**
     * Change le nombre de clients
     * @param nbClients nouveau nombre de clients
     */
    @JsonSetter("nbClients")
    public void setNbClients(int nbClients){
        this.nbClients = nbClients;

        notifierObservateurs();
    }

    /**
     * Setter de la Hashmap des ??tapes
     * @param etapes Etapes
     */
    @JsonSetter("etapes")
    public void setEtapes(HashMap<String, EtapeIG> etapes){
        this.etapes = etapes;
    }

    /**
     * Setter de l'Arraylist des arcs
     * @param arcs Arcs
     */
    @JsonSetter("arcs")
    public void setArcs(ArrayList<ArcIG> arcs){
        this.arcs = arcs;
    }

    /**
     * Setter de la liste des r??sultats de simulation
     * @param resultats liste des r??sultats de simulation
     */
    @JsonSetter("resultatsSimulations")
    public void setResultatsSimulation(ArrayList<ResultatSimulation> resultats){
        this.resultatsSimulations = resultats;
    }

    /**
     * Change le chemin de l'image de fond
     * @param nouveauFond
     */
    public void changerFond(String nouveauFond){
        this.fond = nouveauFond;

        notifierObservateurs();
    }

    /**
     * Ajoute une ??tape
     * @param type Type de l'??tape
     */
    public void ajouter(String type){
        String id = FabriqueIdentifiant.getInstance().getIdentifiantEtape();
        String nom = type + id;
        TailleComposants tailles = TailleComposants.getInstance();
        EtapeIG etape;
        if(type.equals("Activit??"))
            etape = new ActiviteIG(nom, id, tailles.getLargeurActivite(), tailles.getHauteurActivite());
        else
            etape = new GuichetIG(nom, id, tailles.getLargeurGuichet(), tailles.getHauteurGuichet());

        this.etapes.put(id, etape);
        notifierObservateurs();
    }

    /**
     * Ajoute une collection d'??tapes d??j?? cr????es
     * @param e Collection d'??tapes
     */
    public void ajouter(EtapeIG... e){

        for(EtapeIG etape : e) {
            this.etapes.put(etape.getId(), etape);
        }
        notifierObservateurs();
    }

    /**
     * Ajoute un arc entre 2 points de contr??le
     * @param pt1
     * @param pt2
     */
    public void ajouter(PointDeControleIG pt1, PointDeControleIG pt2) throws ArcNonValideException {

        ArcIG arc;
        if(coordsPointsCourbe.size() == 3) {
            arc = new CourbeIG(pt1,
                               (int)coordsPointsCourbe.get(1).getX(), (int)coordsPointsCourbe.get(1).getY(),
                               (int)coordsPointsCourbe.get(2).getX(), (int)coordsPointsCourbe.get(2).getY(),
                               pt2);
        }else{
            arc = new LigneDroiteIG(pt1, pt2);
        }
        
        // On v??rifie si les ??tapes ne sont pas d??j?? reli??es par un arc ??quivalent
        for(ArcIG a : arcs){
            if(a.equals(arc)) {
                annulerSelectionPoints();
                throw new ArcNonValideException("??tapes d??j?? reli??es par un arc ??quivalent.");
            }
        }

        // On v??rifie si l'arc ne cr??e pas une boucle sur une ??tape
        if(pt1.getEtape().equals(pt2.getEtape())){
            annulerSelectionPoints();
            throw new ArcNonValideException("boucle sur une ??tape.");
        }

        if(!verifierSensFile(pt1, pt2)){
            annulerSelectionPoints();
            throw new ArcNonValideException("arc incoh??rent avec le sens de la file du guichet.");
        }

        if(pt1.getEtape().estAccessibleDepuis(pt2.getEtape())){
            annulerSelectionPoints();
            throw new ArcNonValideException("cr??ation d'un circuit.");
        }

        // Cr??ation de l'arc
        arcs.add(arc);

        //Mise ?? jour des successeurs
        pt1.getEtape().ajouterSuccesseur(pt2.getEtape());

        mettreAJourSensFiles();

        notifierObservateurs();
    }

    /**
     * S??lectionne un point de contr??le pour la construction d'un arc
     * @param pt
     */
    public void selectionnerPoint(PointDeControleIG pt) throws ArcNonValideException{

        if(pointSelectionne == null){
            pointSelectionne = pt;
        }else{
            ajouter(pointSelectionne, pt);
            annulerSelectionPoints();
        }
        notifierObservateurs();
    }

    /**
     * S??lectionne un point sur la grille pour la construction d'une courbe.
     * @param point
     */
    public void selectionnerPoint(Point2D point) throws ArcNonValideException{

        if(getNombreCoordsPointsCourbe() >= 3){
            annulerSelectionPoints();
            throw new ArcNonValideException("le point de contr??le d'arriv??e doit ??tre s??lectionn??.");
        }
        if(pointSelectionne != null) {
            coordsPointsCourbe.add(point);
        }
        notifierObservateurs();
    }

    /**
     * S??lectionne une ??tape
     * @param etape
     */
    public void selectionnerEtape(EtapeIG etape){
        if(!etapesSelect.contains(etape)){
            etapesSelect.add(etape);
        }else{
            etapesSelect.remove(etape);
        }
        notifierObservateurs();
    }

    /**
     * S??lectionne un arc
     * @param arc
     */
    public void selectionnerArc(ArcIG arc){
        if(!arcsSelect.contains(arc)){
            arcsSelect.add(arc);
        }else{
            arcsSelect.remove(arc);
        }
        notifierObservateurs();
    }

    /**
     * D??selectionne toutes les ??tapes et tous les arcs s??lectionn??s
     */
    public void toutDeselectionner(){
        etapesSelect.clear();
        arcsSelect.clear();
        notifierObservateurs();
    }

    /**
     * Supprime toutes les ??tapes et tous les arcs s??lectionn??s
     */
    public void supprimerSelection(){

        for(EtapeIG e : etapesSelect){
            supprimerArcs(e);
            etapes.remove(e.getId());
        }
        for(ArcIG a : arcsSelect){
            EtapeIG successeur = a.getP2().getEtape();
            if(successeur != null)
                a.getP1().getEtape().supprimerSuccesseur(successeur);
            arcs.remove(a);
        }
        mettreAJourSensFiles();
        toutDeselectionner();
        notifierObservateurs();
    }


    /**
     * Supprime toutes les ??tapes et tous les arcs reli??s ?? l'??tape
     * @param e etape
     */
    public void supprimerArcs(EtapeIG e){

        ArrayList<ArcIG> aSupprimer = new ArrayList<ArcIG>();
        for(ArcIG arc : arcs){
            for(PointDeControleIG point : e){
                if(arc.getP1().equals(point) || arc.getP2().equals(point)){
                    aSupprimer.add(arc);
                }
            }
        }
        for(ArcIG arc : aSupprimer){
            EtapeIG successeur = arc.getP2().getEtape();
            if(successeur != null)
                arc.getP1().getEtape().supprimerSuccesseur(successeur);
            arcs.remove(arc);
        }
    }

    /**
     * Change le nom de l'??tape s??lectionn??e
     * @param nouveauNom
     */
    public void renommerSelection(String nouveauNom){
        etapesSelect.get(0).setNom(nouveauNom);
        toutDeselectionner();

        notifierObservateurs();
    }

    /**
     * Modifie les param??tres d'une activit??
     * @param temps
     * @param ecartTemps
     */
    public void modifierSelection(int temps, int ecartTemps) throws ParametresIncorrectsException {
        if(temps <= 0 || ecartTemps <= 0)
            throw new ParametresIncorrectsException("les valeurs doivent ??tre strictement positives.");
        if(temps - ecartTemps <= 0)
            throw new ParametresIncorrectsException("l'??cart-temps est trop ??lev??.");
        
        ActiviteIG a = (ActiviteIG)etapesSelect.get(0);
        a.setTemps(temps);
        a.setEcartTemps(ecartTemps);
        toutDeselectionner();

        notifierObservateurs();
    }

    /**
     * Modifie les param??tres d'un guichet
     * @param nbJetons
     */
    public void modifierSelection(int nbJetons) throws ParametresIncorrectsException {
        if(nbJetons <= 0)
            throw new ParametresIncorrectsException("la valeur doit ??tre strictement positive.");

        GuichetIG g = (GuichetIG)etapesSelect.get(0);
        g.setNbJetons(nbJetons);
        toutDeselectionner();

        notifierObservateurs();
    }

    /**
     * D??place l'??tape dont l'identifiant est donn?? en param??tres aux coordonn??es donn??es
     * @param id Identifiant de l'??tape
     * @param x
     * @param y
     */
    public void deplacerEtape(String id, int x, int y){

        EtapeIG etape = etapes.get(id);
        etape.deplacerVers(x, y);
        etape.setEnDeplacement(false);

        notifierObservateurs();
    }

    /**
     * Inverse l'??tat d'entr??e de l'??tape s??lectionn??e
     */
    public void inverserEntreeSelection(){
        for(EtapeIG e : etapesSelect) {
            e.setEntree(!e.estUneEntree());
        }
        toutDeselectionner();
        notifierObservateurs();
    }

    /**
     * Inverse l'??tat de sortie de l'??tape s??lectionn??e
     */
    public void inverserSortieSelection(){
        for(EtapeIG e : etapesSelect) {
            e.setSortie(!e.estUneSortie());
        }
        toutDeselectionner();
        notifierObservateurs();
    }

    /**
     * Annule la construction de l'arc en cours.
     */
    public void annulerSelectionPoints(){
        pointSelectionne = null;
        coordsPointsCourbe.clear();

        notifierObservateurs();
    }

    /**
     * V??rifie si l'arc nouvellement cr???? entre 2 points de contr??le n'est pas incoh??rent avec le sens des
     * files des guichets.
     * @param pt1 Premier point de contr??le de l'arc
     * @param pt2 Second point de contr??le de l'arc
     * @return true si l'arc est correct
     */
    public boolean verifierSensFile(PointDeControleIG pt1, PointDeControleIG pt2){

        boolean correct = true;
        if(pt1.getEtape().estUnGuichet()){
            GuichetIG g = (GuichetIG)pt1.getEtape();
            if(g.getSensFile() == SensFile.GAUCHE){
                if(pt1.getPosition() == Position.BAS || pt1.getPosition() == Position.DROITE){
                    correct = false;
                }
            }else if(g.getSensFile() == SensFile.DROITE){
                if(pt1.getPosition() == Position.HAUT || pt1.getPosition() == Position.GAUCHE) {
                    correct = false;
                }
            }
        }

        if(pt2.getEtape().estUnGuichet()){
            GuichetIG g = (GuichetIG)pt2.getEtape();
            if(g.getSensFile() == SensFile.GAUCHE){
                if(pt2.getPosition() == Position.HAUT || pt2.getPosition() == Position.GAUCHE){
                    correct = false;
                }
            }else if(g.getSensFile() == SensFile.DROITE){
                if(pt2.getPosition() == Position.BAS || pt2.getPosition() == Position.DROITE) {
                    correct = false;
                }
            }
        }
        return correct;
    }

    /**
     * Met ?? jour le sens des files des guichets.
     */
    public void mettreAJourSensFiles(){

        ArrayList<GuichetIG> guichetsAvecArcs = new ArrayList<GuichetIG>();
        for(ArcIG arc : arcs){
            PointDeControleIG p1 = arc.getP1();
            if(p1.getEtape().estUnGuichet()){
                GuichetIG g = (GuichetIG)p1.getEtape();
                guichetsAvecArcs.add(g);
                if(p1.getPosition() == Position.DROITE || p1.getPosition() == Position.BAS)
                    g.setSensFile(SensFile.DROITE);
                else
                    g.setSensFile(SensFile.GAUCHE);
            }

            PointDeControleIG p2 = arc.getP2();
            if(p2.getEtape().estUnGuichet()){
                GuichetIG g = (GuichetIG)p2.getEtape();
                guichetsAvecArcs.add(g);
                if(p2.getPosition() == Position.GAUCHE || p2.getPosition() == Position.HAUT)
                    g.setSensFile(SensFile.DROITE);
                else
                    g.setSensFile(SensFile.GAUCHE);
            }
        }

        Iterator<GuichetIG> ite = this.iteratorGuichets();
        while(ite.hasNext()){
            GuichetIG g = ite.next();
            if(!guichetsAvecArcs.contains(g)){
                g.setSensFile(SensFile.INCONNU);
            }
        }
    }

    /**
     * V??rifie si le graphe est connexe depuis une ??tape donn??e
     * @param etape Etape de d??part
     * @return true si le graphe est connexe
     */
    public boolean estConnexe(EtapeIG etape)
    {
        if(etape.estUneSortie())
            return true;
        if(etape.nbSuccesseurs() >= 1)
        {
            boolean bifu = true;
            Iterator<EtapeIG> iteSucc = etape.iteratorSucesseurs();
            while(iteSucc.hasNext())
            {
                bifu = bifu && estConnexe(iteSucc.next());
            }
            return bifu;
        }
        return false;
    }

    /**
     * V??rifie si le graphe est connexe depuis toutes les entr??es
     * @return true si le graphe est connexe
     */
    public boolean estCompletementConnexe(){

        for(EtapeIG e : this){
            if(e.estUneEntree()){
                if(!estConnexe(e)){
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * V??rifie qu'une sortie ne soit pas un guichet
     * @return true si aucune sortie n'est un guichet
     */
    public boolean verifierSortieGuichet(){

        for(EtapeIG e : this){
            if(e.estUneSortie() && e.estUnGuichet())
                return false;
        }
        return true;
    }

    /**
     * V??rifie le MondeIG pour s'assurer qu'il soit correct avant une simulation potentielle
     */
    private void verifierMondeIG() throws MondeException {
        if(this.getNombreEtapes() == 0)
            throw new MondeException("le monde est vide");
        else if(this.getNombreArcsSelectiones() > 0)
            throw new MondeException("un ou plusieurs arcs sont en s??lection");
        else if (this.getNombreEtapesSelectionnes() > 0)
            throw new MondeException("une ou plusieurs ??tapes sont en s??lection");
        else if(this.getNombreCoordsPointsCourbe() > 0)
            throw new MondeException("un ou plusieurs point de controle sont en s??lection");
        else if(this.getNombreEntree() <= 0)
            throw new MondeException("le monde n'a pas d'entr??e");
        else if(this.getNombreSortie() <= 0)
            throw new MondeException("le monde n'a pas de sortie");
        else if (!verifierSortieGuichet())
            throw new MondeException("une sortie est un guichet");
        else if (!estCompletementConnexe())
            throw new MondeException("le graphe n'est pas connexe");

        // R??initialisation des activit??s restreintes avant la nouvelle v??rification
        for(EtapeIG e : this){
            if(e.estUneActivite())
                ((ActiviteIG)e).setActiviteRestreinte(false);
        }

        for(EtapeIG e : this)
        {
            if(e.estUnGuichet()) {
                if (e.nbSuccesseurs() != 1)
                    throw new MondeException("un guichet a 0 ou plusieurs successeurs");
                EtapeIG succ = e.getSuccesseur();
                if (succ.estUnGuichet())
                    throw new MondeException("deux guichets se suivent");
                else
                    ((ActiviteIG) succ).setActiviteRestreinte(true);
            }
        }

        for(EtapeIG e : this){
            if(e.estUneActivite()){
                Iterator<EtapeIG> ite = e.iteratorSucesseurs();
                while(ite.hasNext()){
                    if(ite.next().estUneActiviteRestreinte())
                        throw new MondeException("une activit?? est suivie par une activit?? restreinte");
                }
            }
            if(e.estUneEntree() && e.estUneActiviteRestreinte())
                throw new MondeException("une entr??e est une activit?? restreinte");
        }
    }

    /**
     * Cr??e le monde pour la simulation
     * @return Monde cr????
     */
    private Monde creerMonde(){

        Monde monde = new Monde();
        monde.setLoiClient(this.loiClient.getId(), this.moyenneLoi, this.ecartTypeLoi);
        correspondanceEtapes = new CorrespondanceEtapes();

        // Cr??ation des ??tapes du monde
        for (EtapeIG etig : this){
            Etape et;
            if(etig.estUneActivite()) {
                ActiviteIG a = (ActiviteIG)etig;
                if(etig.estUneActiviteRestreinte()){
                    et = new ActiviteRestreinte(a.getNom(), a.getTemps(), a.getEcartTemps());
                }else{
                    et = new Activite(a.getNom(), a.getTemps(), a.getEcartTemps());
                }
            }else{
                GuichetIG g = (GuichetIG)etig;
                et = new Guichet(g.getNom(), g.getNbJetons());
            }
            monde.ajouter(et);
            correspondanceEtapes.ajouter(etig, et);

            // Gestion des entr??es et sorties
            if(etig.estUneEntree()) {
                monde.aCommeEntree(et);
            }
            if(etig.estUneSortie())
                monde.aCommeSortie(et);
        }
        // Gestion des successeurs
        for(EtapeIG etig : this){
            Etape et = correspondanceEtapes.get(etig);
            Iterator<EtapeIG> ite = etig.iteratorSucesseurs();
            while(ite.hasNext()){
                EtapeIG succ = ite.next();
                et.ajouterSuccesseur(correspondanceEtapes.get(succ));
            }
        }
        return monde;
    }

    /**
     * Permet de d??marrer la simulation du monde
     */
    public void simuler() throws MondeException{
        verifierMondeIG();
        Monde monde = creerMonde();
        MondeIG mondeIG = this;
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    ClassLoaderPerso classLoader = new ClassLoaderPerso(ClientTwisk.class.getClassLoader());
                    Class<?> simulation = classLoader.loadClass("twisk.simulation.Simulation");
                    Constructor construct = simulation.getConstructor();
                    Object simu = construct.newInstance();
                    Method setNbClients = simulation.getMethod("setNbClients", int.class);
                    setNbClients.invoke(simu, mondeIG.getNbClients());

                    Method ajouterObs = simulation.getMethod("ajouterObservateur", Observateur.class);
                    ajouterObs.invoke(simu, mondeIG);

                    Method getGestionnaireClients = simulation.getMethod("getGestionnaireClients");
                    clients = (GestionnaireClients) getGestionnaireClients.invoke(simu);

                    Method simuler = simulation.getMethod("simuler", Monde.class);
                    simuler.invoke(simu, monde);
                } catch (Exception e) {
                    throw new MondeException("Erreur lors de la simulation.");
                }
                return null;
            }
        };
        ThreadsManager.getInstance().lancer(task);
    }

    /**
     * Sauvegarde le MondeIG
     * @param fichier (endroit de la sauvegarde)
     */
    public void sauvegarder(File fichier) throws TwiskIOException{
        try {
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            ow.writeValue(fichier, this);
            notifierObservateurs();

        } catch (IOException e) {
            throw new TwiskIOException("Erreur lors de l'??criture du fichier.");
        }

    }

    /**
     * Charge le fichier d'un MondeIG
     * @param fichier Fichier ?? ouvrir
     */
    public void ouvrir(File fichier) throws TwiskIOException{

        try {
            toutDeselectionner();
            annulerSelectionPoints();

            MondeIG monde = new ObjectMapper().readValue(fichier, MondeIG.class);
            this.setEtapes(monde.getEtapes());
            this.setArcs(monde.getArcs());
            this.setNbClients(monde.getNbClients());
            this.setLoi(monde.getLoi());
            this.setMoyenneLoi(monde.getMoyenneLoi());
            this.setEcartTypeLoi(monde.getEcartTypeLoi());
            this.setResultatsSimulation(monde.getResultatsSimulation());

            FabriqueIdentifiant.getInstance().setNumeroEtape(getNombreEtapes());
            FabriqueNumero.getInstance().setNumeroResultat(resultatsSimulations.size() + 1);
        } catch (IOException e) {
            throw new TwiskIOException("Format .twk invalide.");
        }
        notifierObservateurs();
    }

    /**
     * Iterateur sur les ??tapes
     * @return iterateur
     */
    @Override
    public Iterator<EtapeIG> iterator() {
        return etapes.values().iterator();
    }

    /**
     * Iterateur sur les guichets
     * @return iterateur
     */
    public Iterator<GuichetIG> iteratorGuichets() {
        ArrayList<GuichetIG> guichets = new ArrayList<>();
        for(EtapeIG e : this){
            if(e.estUnGuichet())
                guichets.add((GuichetIG)e);
        }
        return guichets.iterator();
    }

    /**
     * Iterateur sur les arcs
     * @return iterateur
     */
    public Iterator<ArcIG> iteratorArcs() { return arcs.iterator(); }

    /**
     * Iterateur sur les clients
     * @return iterateur
     */
    public Iterator<Client> iteratorClients() {
        if(clients != null)
            return clients.iterator();
        return null;
    }

    /**
     * Permet de r??agir quand le mod??le notifie
     */
    @Override
    public void reagir() {

        if(!enSimulation()){
            ResultatSimulation resultat = clients.getResultat();
            if(resultat != null) {
                resultat.setLoi(this.loiClient, this.moyenneLoi, this.ecartTypeLoi);
                resultatsSimulations.add(resultat);
            }
        }

        notifierObservateurs();
    }
}
