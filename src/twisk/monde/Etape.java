package twisk.monde;

import twisk.outils.FabriqueNumero;

import java.text.Normalizer;
import java.util.Iterator;

public abstract class Etape implements Iterable<Etape>{

    protected String nom;
    protected GestionnaireSuccesseurs successeurs;
    protected int numero;

    /**
     * Constructeur d'une Etape via son nom
     * @param nom
     */
    public Etape(String nom){

        this.nom = nom;
        successeurs = new GestionnaireSuccesseurs();
        FabriqueNumero fn = FabriqueNumero.getInstance();
        numero = fn.getNumeroEtape();
    }

    /**
     * Getter du nombre de successeur de l'Etape actuelle
     * @return nombre de successeurs
     */
    public int nbSuccesseurs() { return successeurs.nbEtapes(); }

    /**
     * Ajout d'une ou d'une collection d'Etape en successeur
     * @param e Collection d'Etape
     */
    public void ajouterSuccesseur(Etape ... e) { successeurs.ajouter(e); }

    /**
     * Getter du nom de l'Etape actuelle
     * @return nom de l'Etape
     */
    public String getNom(){
        return nom;
    }

    /**
     * Getter du nom de l'Etape modifié pour convenir au #define en C
     * @return nom de l'Etape sans accents et où le caractère " " est remplacé par "_"
     */
    public String getNomDefine() {

        // Remplace les caractères accentués par leur équivalent non accentués
        String nomDefine = Normalizer.normalize(getNom(), Normalizer.Form.NFD);
        nomDefine = nomDefine.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");

        // Le numéro de l'étape est ajouté à la fin au cas où plusieurs étapes auraient le même nom
        return nomDefine.toUpperCase().replace(" ", "_") + "_" + this.getNumero();
    }

    /**
     * Permet de savoir si l'Etape est une activité ou non
     * @return booléen selon le type de l'Etape
     */
    public boolean estUneActivite(){
        return false;
    }

    /**
     * Permet de savoir si l'Etape est une entrée ou non
     * @return booléen
     */
    public boolean estUneEntree(){
        return false;
    }

    /**
     * Permet de savoir si l'Etape est une sortie ou non
     * @return booléen
     */
    public boolean estUneSortie(){
        return false;
    }

    /**
     * Retourne le premier successeur de l'Etape
     * @return une Etape
     */
    public Etape getSucesseur() { return successeurs.getSuccesseur(); }

    /**
     * Retourne le numéro de l'Etape
     * @return entier correspondant au numéro de l'Etape
     */
    public int getNumero() { return this.numero; }

    /**
     * Permet de savoir si l'Etape est un Guichet ou non
     * @return booléen selon le type de l'Etape
     */
    public boolean estUnGuichet(){
        return false;
    }

    /**
     * l'Iterateur des Etape
     * @return Iterator<Etape>
     */
    @Override
    public Iterator<Etape> iterator() {
        return successeurs.iterator();
    }

    /**
     * Permet de visualiser les informations principales de l'Etape
     * @return String des informations principales de l'Etape
     */
    public String toString()
    {
        StringBuilder toReturn = new StringBuilder();
        toReturn.append(getNom() + " : ");
        toReturn.append(successeurs.nbEtapes() + " successeur" + (successeurs.nbEtapes() > 1 ? "s":""));
        for(Etape e : successeurs)
            toReturn.append(" - " + e.getNom());
        toReturn.append('\n');
        return toReturn.toString();
    }

    /**
     * Transformation du monde java en C
     * @return instructions en C
     */
    public String toC(){

        StringBuilder str = new StringBuilder();

        Etape etapeActuelle = this;
        Etape etapeSuivante = this.getSucesseur();

        // Si l'étape actuelle est un guichet,
        // alors la prochaine étape est l'étape qui suit l'activité restreinte.
        if(etapeActuelle.estUnGuichet()){
            etapeActuelle = etapeSuivante;
            etapeSuivante = etapeActuelle.getSucesseur();
        }

        int nbSuccesseurs = etapeActuelle.nbSuccesseurs();
        // S'il y a une bifurcation
        if(nbSuccesseurs > 1){
            str.append("switch((int) ( (rand() / (float) RAND_MAX ) * ").append(nbSuccesseurs).append(")){\n");
            int i = 0;
            // On parcourt les successeurs de l'étape actuelle
            for(Etape s : etapeActuelle){
                str.append("case ").append(i).append(" : {\n");
                str.append("transfert(").append(etapeActuelle.getNomDefine()).append(", ");
                str.append(s.getNomDefine()).append(");\n");
                str.append(s.toC());
                str.append("break;\n}\n");
                i++;
            }
            str.append("}\n");
        } else {
            str.append("transfert(").append(etapeActuelle.getNomDefine()).append(", ");
            str.append(etapeSuivante.getNomDefine()).append(");\n");
            str.append(etapeSuivante.toC());
        }
        return str.toString();
    }

    /**
     * Override d'equals
     * @param o
     * @return true si equals
     */
    @Override
    public boolean equals(Object o){
        if (o instanceof Etape) {
            Etape e = (Etape) o;
            return this.getNumero() == e.getNumero();
        } else return false ;
    }
}
