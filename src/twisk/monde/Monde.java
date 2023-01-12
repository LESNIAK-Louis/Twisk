package twisk.monde;

import twisk.outils.FabriqueNumero;

import java.util.Iterator;

public class Monde implements Iterable<Etape>{

    private GestionnaireEtapes lesEtapes;
    private Etape entree;
    private Etape sortie;

    /**
     * Constructeur du Monde
     */
    public Monde(){
        FabriqueNumero.getInstance().reset();
        entree = new SasEntree();
        sortie = new SasSortie();
        lesEtapes = new GestionnaireEtapes();
        lesEtapes.ajouter(entree, sortie);
    }

    /**
     * Ajoute les Etape passées en paramètre en successeurs de SasEntree
     * @param etapes Collection d'Etape
     */
    public void aCommeEntree(Etape ... etapes) { entree.ajouterSuccesseur(etapes); }

    /**
     * Ajoute le SasSortie en successeur des Etape passées en paramètre
     * @param etapes Collection d'Etape
     */
    public void aCommeSortie(Etape ... etapes)
    {
        for(Etape e : etapes)
            e.ajouterSuccesseur(sortie);
    }

    /**
     * Ajout d'une ou d'une collection d'Etape dans le Monde
     * @param etapes Collection d'Etape
     */
    public void ajouter(Etape... etapes) { lesEtapes.ajouter(etapes); }

    /**
     * Getter du nombre d'Etape dans le Monde
     * @return entier correspondant au nombre d'Etape dans le Monde
     */
    public int nbEtapes(){ return lesEtapes.nbEtapes(); }

    /**
     * Getter de l'Etape SasEntree
     * @return Etape SasEntree
     */
    public Etape getEntree() { return entree; }

    /**
     * Getter de l'Etape SasSortie
     * @return Etape SasSortie
     */
    public Etape getSortie() { return sortie; }

    /**
     * Getter du tableau des jetons des Guichet du Monde
     * @return tableau d'entiers correspondant aux Guichet dans le Monde
     */
    public int[] getTabJetons() {
        int[] tabJetons = new int[this.nbGuichets()];
        for(Etape e : this) {
            if(e.estUnGuichet()) {
                Guichet g = (Guichet)e;
                int i = g.getNumeroSemaphore() - 1;
                tabJetons[i] = g.getNbJetons();
            }
        }
        return tabJetons;
    }

    /**
     * Getter du nombre de Guichet dans le Monde
     * @return entier correspondant au nombre de Guichet dans le Monde
     */
    public int nbGuichets(){
        int nbGuichets = 0;
        for(Etape e : lesEtapes) { if(e.estUnGuichet()) nbGuichets++; }
        return nbGuichets;
    }

    /**
     * Définit la loi d'entrée des clients dans le monde
     * @param loiClient Loi d'entrée des clients
     * @param moyenne Moyenne de la loi d'entrée des clients
     * @param ecartType Ecart-type de la loi d'entrée des clients
     */
    public void setLoiClient(int loiClient, double moyenne, double ecartType){
        ((SasEntree)this.entree).setLoiClient(loiClient, moyenne, ecartType);
    }

    /**
     * l'Iterateur des Etape dans le Monde
     * @return Iterator<Etape>
     */
    @Override
    public Iterator<Etape> iterator() {  return lesEtapes.iterator(); }

    /**
     * Permet de visualiser les informations principales du Monde
     * @return String des informations principales du Monde
     */
    public String toString()
    {
        StringBuilder toReturn = new StringBuilder();
        for(Etape e : lesEtapes)
            toReturn.append(e.toString());
        return toReturn.toString();
    }

    /**
     * Transformation du monde java en C
     * @return instructions en C
     */
    public String toC() {
        StringBuilder str = new StringBuilder();
        str.append("#include <unistd.h>\n");
        str.append("#include <math.h>\n");
        str.append("#include \"def.h\"\n\n");

        for(Etape e : this.lesEtapes)
        {
            str.append("#define " + e.getNomDefine() + " " + e.getNumero() + "\n");
            if(e.estUnGuichet())
                str.append("#define SEM_" + e.getNomDefine() + " "  + ((Guichet)e).getNumeroSemaphore() + "\n");
        }

        // RAND
        str.append("\n#define RAND ((double) rand())/((double) RAND_MAX)\n\n");

        str.append("void delaiUniforme(double temps, double delta) {\n");
        str.append("int bi, bs ;\n");
        str.append("int n, nbSec ;\n");
        str.append("bi = temps - delta ;\n");
        str.append("if (bi < 0) bi = 0 ;\n");
        str.append("bs = temps + delta ;\n");
        str.append("n = bs - bi ;\n");
        str.append("nbSec = (rand()/ (float)RAND_MAX) * n ;\n");
        str.append("nbSec += bi ;\n");
        str.append("usleep(nbSec * 1000000);\n");
        str.append("}\n\n");

        /* delaiGauss */
        str.append("void delaiGauss(double moyenne, double ecartype){\n");
        str.append("double u1 = RAND;\n");
        str.append("double x;\n");
        str.append("if(u1 != 0.){\n");
        str.append("x = sqrt(-2 * log(u1)) * cos(2 * 3.14 * RAND) * ecartype + moyenne;}\n");
        str.append("else{\n");
        str.append("x = 0.;}\n");
        str.append("if(x < 0) x = moyenne;\n");

        str.append("usleep(x*1000000);\n");
        str.append("}\n\n");

        /* delaiExponentiel */
        str.append("void delaiExponentiel(double lambda){\n");
        str.append("double random = RAND;\n");
        str.append("double x;\n");
        str.append("if(random != 0.){\n");
        str.append("x = -(((double)(log(random)))/lambda);}\n");
        str.append("else{\n");
        str.append("x = 0.;}\n");
        str.append("usleep(x*1000000);\n");
        str.append("}\n");

        /* Fonction qui gère le délais en fonction de la loi */
        str.append("void delaiLoi(int loi, double temps, double ecartTemps){\n");
        str.append("if (loi == 0){\n");
        str.append("delaiUniforme(temps, ecartTemps);\n}\n");
        str.append("else if(loi == 1){\n");
        str.append("delaiGauss(temps, ecartTemps);\n}\n");
        str.append("else{\n");
        str.append("delaiExponentiel(1.0 / temps);\n}\n");
        str.append("}\n\n");

        str.append("void simulation(int ids){\n");
        str.append("srand(getpid());\n");
        str.append(this.getEntree().toC());
        str.append("}");
        return str.toString();
    }
}
