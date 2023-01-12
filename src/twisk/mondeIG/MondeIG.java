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
     * Retourne la valeur de la moyenne en paramètre de la loi.
     * @return moyenne de la loi
     */
    @JsonGetter("moyenneLoi")
    public double getMoyenneLoi() {
        return this.moyenneLoi;
    }

    /**
     * Retourne la valeur de l'écart-type en paramètre de la loi.
     * @return écart-type de la loi
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
     * Retourne le nombre d'entrée
     * @return nombre d'entrée
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
     * Retourne le nombre d'étapes
     * @return nombre d'étapes
     */
    @JsonIgnore
    public int getNombreEtapes(){
        return etapes.size();
    }

    /**
     * Retourne l'étape correspondant à l'identifiant
     * @param id identifiant
     * @return etape
     */
    @JsonIgnore
    public EtapeIG getEtape(String id){
        return etapes.get(id);
    }

    /**
     * Retourne l'étapeIG correspondant à l'étape
     * @param e Etape
     * @return etape
     */
    @JsonIgnore
    public EtapeIG getEtape(Etape e){
        assert(correspondanceEtapes != null):"correspondanceEtapes n'existe encore pas";

        return correspondanceEtapes.get(e);
    }

    /**
     * Retourne la hashmap des étapes
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
     * Retourne l'étape correspondant à l'entrée
     * @return null si aucune, uniquement la première si il y en a plusieurs
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
     * Retourne l'étape correspondant à la sortie
     * @return null si aucune, uniquement la première si il y en a plusieurs
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
     * Vérifie si l'étape est sélectionnée
     * @param etape
     * @return true si l'étape est sélectionnée
     */
    public boolean estSelectionnee(EtapeIG etape){
        return etapesSelect.contains(etape);
    }

    /**
     * Vérifie si l'arc est sélectionné
     * @param arc
     * @return true si l'arc est sélectionné
     */
    public boolean estSelectionne(ArcIG arc){
        return arcsSelect.contains(arc);
    }

    /**
     * Vérifie si le point de contrôle est sélectionné
     * @param point
     * @return true si le point de contrôle est sélectionné
     */
    public boolean estSelectionne(PointDeControleIG point){
        return pointSelectionne != null && pointSelectionne.equals(point);
    }

    /**
     * Vérifie si un arc est en train d'être créé
     * @return true si un arc est en train d'être créé
     */
    public boolean arcEnCoursDeCreation(){
        return pointSelectionne != null;
    }

    /**
     * Retourne le nombre d'étapes sélectionnées
     * @return nombre d'étapes sélectionnées
     */
    @JsonIgnore
    public int getNombreEtapesSelectionnes(){
        return etapesSelect.size();
    }

    /**
     * Retourne le nombre d'arcs sélectionnés
     * @return nombre d'arcs sélectionnés
     */
    @JsonIgnore
    public int getNombreArcsSelectiones(){
        return arcsSelect.size();
    }

    /**
     * Retourne le nombre de points intermédiaires sélectinnés sur la courbe
     * @return nombre de points intermédiaires sélectinnés sur la courbe
     */
    @JsonIgnore
    public int getNombreCoordsPointsCourbe(){ return coordsPointsCourbe.size(); }

    /**
     * Retourne la première étape sélectionnée
     * @return etape sélectionnée
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
     * Retourne la liste des résultats de simulation
     * @return liste des résultats de simulation
     */
    @JsonGetter("resultatsSimulations")
    public ArrayList<ResultatSimulation> getResultatsSimulation(){
        return this.resultatsSimulations;
    }

    /**
     * Retourne le nombre de résultats de simulation enregistrés
     * @return nombre de résultats de simulation
     */
    @JsonIgnore
    public int nbResultatsSimulation(){
        return this.resultatsSimulations.size();
    }

    /**
     * Retourne un boolean correspondant à si la simulation est en cours ou pas
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
     * Change la valeur de la moyenne en paramètre de la loi.
     * @param moyenne Moyenne de la loi
     */
    @JsonSetter("moyenneLoi")
    public void setMoyenneLoi(double moyenne) {
        this.moyenneLoi = moyenne;
    }

    /**
     * Change la valeur de l'écart-type en paramètre de la loi.
     * @param ecartType Ecart-type de la loi
     */
    @JsonSetter("ecartTypeLoi")
    public void setEcartTypeLoi(double ecartType) {
        this.ecartTypeLoi = ecartType;
    }

    /**
     * Change les valeur de la moyenne et de l'écart-type en paramètres de la loi.
     * @param moyenne Moyenne de la loi
     * @param ecartType Ecart-type de la loi
     */
    @JsonIgnore
    public void setParametresLoi(double moyenne, double ecartType) throws ParametresIncorrectsException{
        if(moyenne <= 0 || ecartType <= 0)
            throw new ParametresIncorrectsException("les valeurs doivent être strictement positives.");
        if(moyenne - ecartType <= 0)
            throw new ParametresIncorrectsException("l'écart-type est trop élevé.");

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
     * Setter de la Hashmap des étapes
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
     * Setter de la liste des résultats de simulation
     * @param resultats liste des résultats de simulation
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
     * Ajoute une étape
     * @param type Type de l'étape
     */
    public void ajouter(String type){
        String id = FabriqueIdentifiant.getInstance().getIdentifiantEtape();
        String nom = type + id;
        TailleComposants tailles = TailleComposants.getInstance();
        EtapeIG etape;
        if(type.equals("Activité"))
            etape = new ActiviteIG(nom, id, tailles.getLargeurActivite(), tailles.getHauteurActivite());
        else
            etape = new GuichetIG(nom, id, tailles.getLargeurGuichet(), tailles.getHauteurGuichet());

        this.etapes.put(id, etape);
        notifierObservateurs();
    }

    /**
     * Ajoute une collection d'étapes déjà créées
     * @param e Collection d'étapes
     */
    public void ajouter(EtapeIG... e){

        for(EtapeIG etape : e) {
            this.etapes.put(etape.getId(), etape);
        }
        notifierObservateurs();
    }

    /**
     * Ajoute un arc entre 2 points de contrôle
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
        
        // On vérifie si les étapes ne sont pas déjà reliées par un arc équivalent
        for(ArcIG a : arcs){
            if(a.equals(arc)) {
                annulerSelectionPoints();
                throw new ArcNonValideException("étapes déjà reliées par un arc équivalent.");
            }
        }

        // On vérifie si l'arc ne crée pas une boucle sur une étape
        if(pt1.getEtape().equals(pt2.getEtape())){
            annulerSelectionPoints();
            throw new ArcNonValideException("boucle sur une étape.");
        }

        if(!verifierSensFile(pt1, pt2)){
            annulerSelectionPoints();
            throw new ArcNonValideException("arc incohérent avec le sens de la file du guichet.");
        }

        if(pt1.getEtape().estAccessibleDepuis(pt2.getEtape())){
            annulerSelectionPoints();
            throw new ArcNonValideException("création d'un circuit.");
        }

        // Création de l'arc
        arcs.add(arc);

        //Mise à jour des successeurs
        pt1.getEtape().ajouterSuccesseur(pt2.getEtape());

        mettreAJourSensFiles();

        notifierObservateurs();
    }

    /**
     * Sélectionne un point de contrôle pour la construction d'un arc
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
     * Sélectionne un point sur la grille pour la construction d'une courbe.
     * @param point
     */
    public void selectionnerPoint(Point2D point) throws ArcNonValideException{

        if(getNombreCoordsPointsCourbe() >= 3){
            annulerSelectionPoints();
            throw new ArcNonValideException("le point de contrôle d'arrivée doit être sélectionné.");
        }
        if(pointSelectionne != null) {
            coordsPointsCourbe.add(point);
        }
        notifierObservateurs();
    }

    /**
     * Sélectionne une étape
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
     * Sélectionne un arc
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
     * Déselectionne toutes les étapes et tous les arcs sélectionnés
     */
    public void toutDeselectionner(){
        etapesSelect.clear();
        arcsSelect.clear();
        notifierObservateurs();
    }

    /**
     * Supprime toutes les étapes et tous les arcs sélectionnés
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
     * Supprime toutes les étapes et tous les arcs reliés à l'étape
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
     * Change le nom de l'étape sélectionnée
     * @param nouveauNom
     */
    public void renommerSelection(String nouveauNom){
        etapesSelect.get(0).setNom(nouveauNom);
        toutDeselectionner();

        notifierObservateurs();
    }

    /**
     * Modifie les paramètres d'une activité
     * @param temps
     * @param ecartTemps
     */
    public void modifierSelection(int temps, int ecartTemps) throws ParametresIncorrectsException {
        if(temps <= 0 || ecartTemps <= 0)
            throw new ParametresIncorrectsException("les valeurs doivent être strictement positives.");
        if(temps - ecartTemps <= 0)
            throw new ParametresIncorrectsException("l'écart-temps est trop élevé.");
        
        ActiviteIG a = (ActiviteIG)etapesSelect.get(0);
        a.setTemps(temps);
        a.setEcartTemps(ecartTemps);
        toutDeselectionner();

        notifierObservateurs();
    }

    /**
     * Modifie les paramètres d'un guichet
     * @param nbJetons
     */
    public void modifierSelection(int nbJetons) throws ParametresIncorrectsException {
        if(nbJetons <= 0)
            throw new ParametresIncorrectsException("la valeur doit être strictement positive.");

        GuichetIG g = (GuichetIG)etapesSelect.get(0);
        g.setNbJetons(nbJetons);
        toutDeselectionner();

        notifierObservateurs();
    }

    /**
     * Déplace l'étape dont l'identifiant est donné en paramètres aux coordonnées données
     * @param id Identifiant de l'étape
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
     * Inverse l'état d'entrée de l'étape sélectionnée
     */
    public void inverserEntreeSelection(){
        for(EtapeIG e : etapesSelect) {
            e.setEntree(!e.estUneEntree());
        }
        toutDeselectionner();
        notifierObservateurs();
    }

    /**
     * Inverse l'état de sortie de l'étape sélectionnée
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
     * Vérifie si l'arc nouvellement créé entre 2 points de contrôle n'est pas incohérent avec le sens des
     * files des guichets.
     * @param pt1 Premier point de contrôle de l'arc
     * @param pt2 Second point de contrôle de l'arc
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
     * Met à jour le sens des files des guichets.
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
     * Vérifie si le graphe est connexe depuis une étape donnée
     * @param etape Etape de départ
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
     * Vérifie si le graphe est connexe depuis toutes les entrées
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
     * Vérifie qu'une sortie ne soit pas un guichet
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
     * Vérifie le MondeIG pour s'assurer qu'il soit correct avant une simulation potentielle
     */
    private void verifierMondeIG() throws MondeException {
        if(this.getNombreEtapes() == 0)
            throw new MondeException("le monde est vide");
        else if(this.getNombreArcsSelectiones() > 0)
            throw new MondeException("un ou plusieurs arcs sont en sélection");
        else if (this.getNombreEtapesSelectionnes() > 0)
            throw new MondeException("une ou plusieurs étapes sont en sélection");
        else if(this.getNombreCoordsPointsCourbe() > 0)
            throw new MondeException("un ou plusieurs point de controle sont en sélection");
        else if(this.getNombreEntree() <= 0)
            throw new MondeException("le monde n'a pas d'entrée");
        else if(this.getNombreSortie() <= 0)
            throw new MondeException("le monde n'a pas de sortie");
        else if (!verifierSortieGuichet())
            throw new MondeException("une sortie est un guichet");
        else if (!estCompletementConnexe())
            throw new MondeException("le graphe n'est pas connexe");

        // Réinitialisation des activités restreintes avant la nouvelle vérification
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
                        throw new MondeException("une activité est suivie par une activité restreinte");
                }
            }
            if(e.estUneEntree() && e.estUneActiviteRestreinte())
                throw new MondeException("une entrée est une activité restreinte");
        }
    }

    /**
     * Crée le monde pour la simulation
     * @return Monde créé
     */
    private Monde creerMonde(){

        Monde monde = new Monde();
        monde.setLoiClient(this.loiClient.getId(), this.moyenneLoi, this.ecartTypeLoi);
        correspondanceEtapes = new CorrespondanceEtapes();

        // Création des étapes du monde
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

            // Gestion des entrées et sorties
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
     * Permet de démarrer la simulation du monde
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
            throw new TwiskIOException("Erreur lors de l'écriture du fichier.");
        }

    }

    /**
     * Charge le fichier d'un MondeIG
     * @param fichier Fichier à ouvrir
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
     * Iterateur sur les étapes
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
     * Permet de réagir quand le modèle notifie
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
