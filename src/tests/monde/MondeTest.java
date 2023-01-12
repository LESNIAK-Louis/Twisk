package tests.monde;

import org.junit.jupiter.api.Test;
import twisk.monde.*;

import static org.junit.jupiter.api.Assertions.*;

class MondeTest {
    @Test
    void aCommeEntreeSortie() {
        Monde monde = new Monde();
        Etape fileTobbogan = new Guichet("File tobbogan");
        Etape filePlage = new Guichet("File Plage");

        Etape tobbogan = new ActiviteRestreinte("Tobbogan");
        Etape piscine = new ActiviteRestreinte("Piscine");
        Etape plage = new ActiviteRestreinte("Plage");

        monde.ajouter(fileTobbogan, filePlage, tobbogan, piscine, plage);
        monde.aCommeEntree(fileTobbogan);
        monde.aCommeSortie(plage, piscine);

        assertEquals(1, plage.nbSuccesseurs());
        assertEquals(1, piscine.nbSuccesseurs());
        assertEquals(0, tobbogan.nbSuccesseurs());
        assertEquals(1, monde.getEntree().nbSuccesseurs());
        monde.aCommeEntree(filePlage);
        assertEquals(2, monde.getEntree().nbSuccesseurs());
    }

    @Test
    void nbGuichets() {

        Monde monde = new Monde();
        Etape fileTobbogan = new Guichet("File tobbogan");
        Etape plage = new ActiviteRestreinte("Plage");
        Etape filePlage = new Guichet("File Plage");

        Etape tobbogan = new ActiviteRestreinte("Tobbogan");
        Etape piscine = new ActiviteRestreinte("Piscine");

        fileTobbogan.ajouterSuccesseur(tobbogan);
        tobbogan.ajouterSuccesseur(piscine);
        piscine.ajouterSuccesseur(filePlage);
        filePlage.ajouterSuccesseur(plage);

        monde.ajouter(fileTobbogan, filePlage, tobbogan, piscine, plage);

        assertEquals(2, monde.nbGuichets());
    }

    @Test
    void getTabJetons(){

        Monde monde = new Monde();
        Guichet fileTobbogan = new Guichet("File tobbogan", 1);
        Etape plage = new ActiviteRestreinte("Plage");
        Guichet filePlage = new Guichet("File Plage", 5);
        Etape tobbogan = new ActiviteRestreinte("Tobbogan");
        monde.ajouter(fileTobbogan, filePlage, tobbogan, plage);

        int[] tab = monde.getTabJetons();
        assertEquals(2, tab.length);
        assertEquals(1, tab[0]);
        assertEquals(5, tab[1]);

        monde = new Monde();
        monde.ajouter(plage, filePlage, fileTobbogan, tobbogan);
        tab = monde.getTabJetons();
        assertEquals(2, tab.length);
        assertEquals(1, tab[0]);
        assertEquals(5, tab[1]);
    }

    @Test
    void toCMonde1() {

        // Monde avec : Activite -> Guichet -> ActiviteRestreinte

        Monde monde = new Monde();
        Etape activite1 = new Activite("Activite");
        Etape guichet = new Guichet("Guichet");
        Etape activite2 = new ActiviteRestreinte("Activite");

        monde.ajouter(activite1, guichet, activite2);
        monde.aCommeEntree(activite1);
        monde.aCommeSortie(activite2);
        activite1.ajouterSuccesseur(guichet);
        guichet.ajouterSuccesseur(activite2);

        StringBuilder res = new StringBuilder();
        res.append("#include <unistd.h>\n");
        res.append("#include <math.h>\n");
        res.append("#include \"def.h\"\n\n");
        res.append("#define ENTREE_0 0\n");
        res.append("#define SORTIE_1 1\n");
        res.append("#define ACTIVITE_2 2\n");
        res.append("#define GUICHET_3 3\n");
        res.append("#define SEM_GUICHET_3 1\n");
        res.append("#define ACTIVITE_4 4\n");
        res.append("\n#define RAND ((double) rand())/((double) RAND_MAX)\n\n");
        res.append("void delaiUniforme(double temps, double delta) {\n");
        res.append("int bi, bs ;\n");
        res.append("int n, nbSec ;\n");
        res.append("bi = temps - delta ;\n");
        res.append("if (bi < 0) bi = 0 ;\n");
        res.append("bs = temps + delta ;\n");
        res.append("n = bs - bi ;\n");
        res.append("nbSec = (rand()/ (float)RAND_MAX) * n ;\n");
        res.append("nbSec += bi ;\n");
        res.append("usleep(nbSec * 1000000);\n");
        res.append("}\n\n");
        res.append("void delaiGauss(double moyenne, double ecartype){\n");
        res.append("double u1 = RAND;\n");
        res.append("double x;\n");
        res.append("if(u1 != 0.){\n");
        res.append("x = sqrt(-2 * log(u1)) * cos(2 * 3.14 * RAND) * ecartype + moyenne;}\n");
        res.append("else{\n");
        res.append("x = 0.;}\n");
        res.append("if(x < 0) x = moyenne;\n");
        res.append("usleep(x*1000000);\n");
        res.append("}\n\n");
        res.append("void delaiExponentiel(double lambda){\n");
        res.append("double random = RAND;\n");
        res.append("double x;\n");
        res.append("if(random != 0.){\n");
        res.append("x = -(((double)(log(random)))/lambda);}\n");
        res.append("else{\n");
        res.append("x = 0.;}\n");
        res.append("usleep(x*1000000);\n");
        res.append("}\n");
        res.append("void delaiLoi(int loi, double temps, double ecartTemps){\n");
        res.append("if (loi == 0){\n");
        res.append("delaiUniforme(temps, ecartTemps);\n}\n");
        res.append("else if(loi == 1){\n");
        res.append("delaiGauss(temps, ecartTemps);\n}\n");
        res.append("else{\n");
        res.append("delaiExponentiel(1.0 / temps);\n}\n");
        res.append("}\n\n");
        res.append("void simulation(int ids){\n");
        res.append("srand(getpid());\n");
        res.append("entrer(ENTREE_0);\n");
        res.append("delaiLoi(0, 4.0, 2.0);\n");
        res.append("transfert(ENTREE_0, ACTIVITE_2);\n");
        res.append("delai(4, 2);\n");
        res.append("transfert(ACTIVITE_2, GUICHET_3);\n");
        res.append("P(ids, SEM_GUICHET_3);\n");
        res.append("transfert(GUICHET_3, ACTIVITE_4);\n");
        res.append("delai(4, 2);\n");
        res.append("V(ids, SEM_GUICHET_3);\n");
        res.append("transfert(ACTIVITE_4, SORTIE_1);\n");
        res.append("}");

        assertEquals(res.toString(), monde.toC());

    }
    @Test
    void toCMonde2(){

        // Monde avec : Activite

        Monde monde = new Monde();
        Activite activite1 = new Activite("Activite");

        monde.ajouter(activite1);
        monde.aCommeEntree(activite1);
        monde.aCommeSortie(activite1);

        StringBuilder res = new StringBuilder();
        res.append("#include <unistd.h>\n");
        res.append("#include <math.h>\n");
        res.append("#include \"def.h\"\n\n");
        res.append("#define ENTREE_0 0\n");
        res.append("#define SORTIE_1 1\n");
        res.append("#define ACTIVITE_2 2\n");
        res.append("\n#define RAND ((double) rand())/((double) RAND_MAX)\n\n");
        res.append("void delaiUniforme(double temps, double delta) {\n");
        res.append("int bi, bs ;\n");
        res.append("int n, nbSec ;\n");
        res.append("bi = temps - delta ;\n");
        res.append("if (bi < 0) bi = 0 ;\n");
        res.append("bs = temps + delta ;\n");
        res.append("n = bs - bi ;\n");
        res.append("nbSec = (rand()/ (float)RAND_MAX) * n ;\n");
        res.append("nbSec += bi ;\n");
        res.append("usleep(nbSec * 1000000);\n");
        res.append("}\n\n");
        res.append("void delaiGauss(double moyenne, double ecartype){\n");
        res.append("double u1 = RAND;\n");
        res.append("double x;\n");
        res.append("if(u1 != 0.){\n");
        res.append("x = sqrt(-2 * log(u1)) * cos(2 * 3.14 * RAND) * ecartype + moyenne;}\n");
        res.append("else{\n");
        res.append("x = 0.;}\n");
        res.append("if(x < 0) x = moyenne;\n");
        res.append("usleep(x*1000000);\n");
        res.append("}\n\n");
        res.append("void delaiExponentiel(double lambda){\n");
        res.append("double random = RAND;\n");
        res.append("double x;\n");
        res.append("if(random != 0.){\n");
        res.append("x = -(((double)(log(random)))/lambda);}\n");
        res.append("else{\n");
        res.append("x = 0.;}\n");
        res.append("usleep(x*1000000);\n");
        res.append("}\n");
        res.append("void delaiLoi(int loi, double temps, double ecartTemps){\n");
        res.append("if (loi == 0){\n");
        res.append("delaiUniforme(temps, ecartTemps);\n}\n");
        res.append("else if(loi == 1){\n");
        res.append("delaiGauss(temps, ecartTemps);\n}\n");
        res.append("else{\n");
        res.append("delaiExponentiel(1.0 / temps);\n}\n");
        res.append("}\n\n");
        res.append("void simulation(int ids){\n");
        res.append("srand(getpid());\n");
        res.append("entrer(ENTREE_0);\n");
        res.append("delaiLoi(0, 4.0, 2.0);\n");
        res.append("transfert(ENTREE_0, ACTIVITE_2);\n");
        res.append("delai(4, 2);\n");
        res.append("transfert(ACTIVITE_2, SORTIE_1);\n");
        res.append("}");

        assertEquals(res.toString(), monde.toC());

    }
    @Test
    void toCMonde3(){

        // Monde avec : Activite -> Activite

        Monde monde = new Monde();
        Activite activite1 = new Activite("Activite");
        Activite activite2 = new Activite("Activite");

        monde.ajouter(activite1, activite2);
        monde.aCommeEntree(activite1);
        monde.aCommeSortie(activite2);
        activite1.ajouterSuccesseur(activite2);

        StringBuilder res = new StringBuilder();
        res.append("#include <unistd.h>\n");
        res.append("#include <math.h>\n");
        res.append("#include \"def.h\"\n\n");
        res.append("#define ENTREE_0 0\n");
        res.append("#define SORTIE_1 1\n");
        res.append("#define ACTIVITE_2 2\n");
        res.append("#define ACTIVITE_3 3\n");
        res.append("\n#define RAND ((double) rand())/((double) RAND_MAX)\n\n");
        res.append("void delaiUniforme(double temps, double delta) {\n");
        res.append("int bi, bs ;\n");
        res.append("int n, nbSec ;\n");
        res.append("bi = temps - delta ;\n");
        res.append("if (bi < 0) bi = 0 ;\n");
        res.append("bs = temps + delta ;\n");
        res.append("n = bs - bi ;\n");
        res.append("nbSec = (rand()/ (float)RAND_MAX) * n ;\n");
        res.append("nbSec += bi ;\n");
        res.append("usleep(nbSec * 1000000);\n");
        res.append("}\n\n");
        res.append("void delaiGauss(double moyenne, double ecartype){\n");
        res.append("double u1 = RAND;\n");
        res.append("double x;\n");
        res.append("if(u1 != 0.){\n");
        res.append("x = sqrt(-2 * log(u1)) * cos(2 * 3.14 * RAND) * ecartype + moyenne;}\n");
        res.append("else{\n");
        res.append("x = 0.;}\n");
        res.append("if(x < 0) x = moyenne;\n");
        res.append("usleep(x*1000000);\n");
        res.append("}\n\n");
        res.append("void delaiExponentiel(double lambda){\n");
        res.append("double random = RAND;\n");
        res.append("double x;\n");
        res.append("if(random != 0.){\n");
        res.append("x = -(((double)(log(random)))/lambda);}\n");
        res.append("else{\n");
        res.append("x = 0.;}\n");
        res.append("usleep(x*1000000);\n");
        res.append("}\n");
        res.append("void delaiLoi(int loi, double temps, double ecartTemps){\n");
        res.append("if (loi == 0){\n");
        res.append("delaiUniforme(temps, ecartTemps);\n}\n");
        res.append("else if(loi == 1){\n");
        res.append("delaiGauss(temps, ecartTemps);\n}\n");
        res.append("else{\n");
        res.append("delaiExponentiel(1.0 / temps);\n}\n");
        res.append("}\n\n");
        res.append("void simulation(int ids){\n");
        res.append("srand(getpid());\n");
        res.append("entrer(ENTREE_0);\n");
        res.append("delaiLoi(0, 4.0, 2.0);\n");
        res.append("transfert(ENTREE_0, ACTIVITE_2);\n");
        res.append("delai(4, 2);\n");
        res.append("transfert(ACTIVITE_2, ACTIVITE_3);\n");
        res.append("delai(4, 2);\n");
        res.append("transfert(ACTIVITE_3, SORTIE_1);\n");
        res.append("}");

        assertEquals(res.toString(), monde.toC());
    }

    @Test
    void toCMonde4(){

        // Monde avec bifurcation
        // Activite1 |-> Activite2 |-> Sortie
        //           |-> Activite3 |

        Monde monde = new Monde();
        Activite activite1 = new Activite("Activité");
        Activite activite2 = new Activite("Activité");
        Activite activite3 = new Activite("Activité");

        monde.ajouter(activite1, activite2, activite3);
        monde.aCommeEntree(activite1);
        monde.aCommeSortie(activite2, activite3);
        activite1.ajouterSuccesseur(activite2, activite3);

        StringBuilder res = new StringBuilder();
        res.append("#include <unistd.h>\n");
        res.append("#include <math.h>\n");
        res.append("#include \"def.h\"\n\n");
        res.append("#define ENTREE_0 0\n");
        res.append("#define SORTIE_1 1\n");
        res.append("#define ACTIVITE_2 2\n");
        res.append("#define ACTIVITE_3 3\n");
        res.append("#define ACTIVITE_4 4\n");
        res.append("\n#define RAND ((double) rand())/((double) RAND_MAX)\n\n");
        res.append("void delaiUniforme(double temps, double delta) {\n");
        res.append("int bi, bs ;\n");
        res.append("int n, nbSec ;\n");
        res.append("bi = temps - delta ;\n");
        res.append("if (bi < 0) bi = 0 ;\n");
        res.append("bs = temps + delta ;\n");
        res.append("n = bs - bi ;\n");
        res.append("nbSec = (rand()/ (float)RAND_MAX) * n ;\n");
        res.append("nbSec += bi ;\n");
        res.append("usleep(nbSec * 1000000);\n");
        res.append("}\n\n");
        res.append("void delaiGauss(double moyenne, double ecartype){\n");
        res.append("double u1 = RAND;\n");
        res.append("double x;\n");
        res.append("if(u1 != 0.){\n");
        res.append("x = sqrt(-2 * log(u1)) * cos(2 * 3.14 * RAND) * ecartype + moyenne;}\n");
        res.append("else{\n");
        res.append("x = 0.;}\n");
        res.append("if(x < 0) x = moyenne;\n");
        res.append("usleep(x*1000000);\n");
        res.append("}\n\n");
        res.append("void delaiExponentiel(double lambda){\n");
        res.append("double random = RAND;\n");
        res.append("double x;\n");
        res.append("if(random != 0.){\n");
        res.append("x = -(((double)(log(random)))/lambda);}\n");
        res.append("else{\n");
        res.append("x = 0.;}\n");
        res.append("usleep(x*1000000);\n");
        res.append("}\n");
        res.append("void delaiLoi(int loi, double temps, double ecartTemps){\n");
        res.append("if (loi == 0){\n");
        res.append("delaiUniforme(temps, ecartTemps);\n}\n");
        res.append("else if(loi == 1){\n");
        res.append("delaiGauss(temps, ecartTemps);\n}\n");
        res.append("else{\n");
        res.append("delaiExponentiel(1.0 / temps);\n}\n");
        res.append("}\n\n");
        res.append("void simulation(int ids){\n");
        res.append("srand(getpid());\n");
        res.append("entrer(ENTREE_0);\n");
        res.append("delaiLoi(0, 4.0, 2.0);\n");
        res.append("transfert(ENTREE_0, ACTIVITE_2);\n");
        res.append("delai(4, 2);\n");
        res.append("switch((int) ( (rand() / (float) RAND_MAX ) * 2)){\n");
        res.append("case 0 : {\n");
        res.append("transfert(ACTIVITE_2, ACTIVITE_3);\n");
        res.append("delai(4, 2);\n");
        res.append("transfert(ACTIVITE_3, SORTIE_1);\n");
        res.append("break;\n");
        res.append("}\n");
        res.append("case 1 : {\n");
        res.append("transfert(ACTIVITE_2, ACTIVITE_4);\n");
        res.append("delai(4, 2);\n");
        res.append("transfert(ACTIVITE_4, SORTIE_1);\n");
        res.append("break;\n");
        res.append("}\n");
        res.append("}\n");
        res.append("}");

        assertEquals(res.toString(), monde.toC());
    }

    @Test
    void toCMonde5(){

        // Monde avec bifurcation
        // Activite1 |-> Activite2 -> Sortie
        //           |-> Sortie

        Monde monde = new Monde();
        Activite activite1 = new Activite("Activité");
        Activite activite2 = new Activite("Activité");

        monde.ajouter(activite1, activite2);
        monde.aCommeEntree(activite1);
        monde.aCommeSortie(activite1, activite2);
        activite1.ajouterSuccesseur(activite2);

        StringBuilder res = new StringBuilder();
        res.append("#include <unistd.h>\n");
        res.append("#include <math.h>\n");
        res.append("#include \"def.h\"\n\n");
        res.append("#define ENTREE_0 0\n");
        res.append("#define SORTIE_1 1\n");
        res.append("#define ACTIVITE_2 2\n");
        res.append("#define ACTIVITE_3 3\n");
        res.append("\n#define RAND ((double) rand())/((double) RAND_MAX)\n\n");
        res.append("void delaiUniforme(double temps, double delta) {\n");
        res.append("int bi, bs ;\n");
        res.append("int n, nbSec ;\n");
        res.append("bi = temps - delta ;\n");
        res.append("if (bi < 0) bi = 0 ;\n");
        res.append("bs = temps + delta ;\n");
        res.append("n = bs - bi ;\n");
        res.append("nbSec = (rand()/ (float)RAND_MAX) * n ;\n");
        res.append("nbSec += bi ;\n");
        res.append("usleep(nbSec * 1000000);\n");
        res.append("}\n\n");
        res.append("void delaiGauss(double moyenne, double ecartype){\n");
        res.append("double u1 = RAND;\n");
        res.append("double x;\n");
        res.append("if(u1 != 0.){\n");
        res.append("x = sqrt(-2 * log(u1)) * cos(2 * 3.14 * RAND) * ecartype + moyenne;}\n");
        res.append("else{\n");
        res.append("x = 0.;}\n");
        res.append("if(x < 0) x = moyenne;\n");
        res.append("usleep(x*1000000);\n");
        res.append("}\n\n");
        res.append("void delaiExponentiel(double lambda){\n");
        res.append("double random = RAND;\n");
        res.append("double x;\n");
        res.append("if(random != 0.){\n");
        res.append("x = -(((double)(log(random)))/lambda);}\n");
        res.append("else{\n");
        res.append("x = 0.;}\n");
        res.append("usleep(x*1000000);\n");
        res.append("}\n");
        res.append("void delaiLoi(int loi, double temps, double ecartTemps){\n");
        res.append("if (loi == 0){\n");
        res.append("delaiUniforme(temps, ecartTemps);\n}\n");
        res.append("else if(loi == 1){\n");
        res.append("delaiGauss(temps, ecartTemps);\n}\n");
        res.append("else{\n");
        res.append("delaiExponentiel(1.0 / temps);\n}\n");
        res.append("}\n\n");
        res.append("void simulation(int ids){\n");
        res.append("srand(getpid());\n");
        res.append("entrer(ENTREE_0);\n");
        res.append("delaiLoi(0, 4.0, 2.0);\n");
        res.append("transfert(ENTREE_0, ACTIVITE_2);\n");
        res.append("delai(4, 2);\n");
        res.append("switch((int) ( (rand() / (float) RAND_MAX ) * 2)){\n");
        res.append("case 0 : {\n");
        res.append("transfert(ACTIVITE_2, SORTIE_1);\n");
        res.append("break;\n");
        res.append("}\n");
        res.append("case 1 : {\n");
        res.append("transfert(ACTIVITE_2, ACTIVITE_3);\n");
        res.append("delai(4, 2);\n");
        res.append("transfert(ACTIVITE_3, SORTIE_1);\n");
        res.append("break;\n");
        res.append("}\n");
        res.append("}\n");
        res.append("}");

        assertEquals(res.toString(), monde.toC());
    }

    @Test
    void toCMonde6(){

        // Monde avec bifurcation
        // Activite1 -> Guichet -> ActiviteRestreinte |-> Activite2 -> Sortie
        //                                            |-> Sortie

        Monde monde = new Monde();
        Activite activite1 = new Activite("Activité");
        Guichet guichet = new Guichet("Guichet");
        ActiviteRestreinte activiteRestreinte = new ActiviteRestreinte("Activité Restreinte");
        Activite activite2 = new Activite("Activité");

        monde.ajouter(activite1, guichet, activiteRestreinte, activite2);
        monde.aCommeEntree(activite1);
        monde.aCommeSortie(activite2, activiteRestreinte);
        activite1.ajouterSuccesseur(guichet);
        guichet.ajouterSuccesseur(activiteRestreinte);
        activiteRestreinte.ajouterSuccesseur(activite2);

        StringBuilder res = new StringBuilder();
        res.append("#include <unistd.h>\n");
        res.append("#include <math.h>\n");
        res.append("#include \"def.h\"\n\n");
        res.append("#define ENTREE_0 0\n");
        res.append("#define SORTIE_1 1\n");
        res.append("#define ACTIVITE_2 2\n");
        res.append("#define GUICHET_3 3\n");
        res.append("#define SEM_GUICHET_3 1\n");
        res.append("#define ACTIVITE_RESTREINTE_4 4\n");
        res.append("#define ACTIVITE_5 5\n");
        res.append("\n#define RAND ((double) rand())/((double) RAND_MAX)\n\n");
        res.append("void delaiUniforme(double temps, double delta) {\n");
        res.append("int bi, bs ;\n");
        res.append("int n, nbSec ;\n");
        res.append("bi = temps - delta ;\n");
        res.append("if (bi < 0) bi = 0 ;\n");
        res.append("bs = temps + delta ;\n");
        res.append("n = bs - bi ;\n");
        res.append("nbSec = (rand()/ (float)RAND_MAX) * n ;\n");
        res.append("nbSec += bi ;\n");
        res.append("usleep(nbSec * 1000000);\n");
        res.append("}\n\n");
        res.append("void delaiGauss(double moyenne, double ecartype){\n");
        res.append("double u1 = RAND;\n");
        res.append("double x;\n");
        res.append("if(u1 != 0.){\n");
        res.append("x = sqrt(-2 * log(u1)) * cos(2 * 3.14 * RAND) * ecartype + moyenne;}\n");
        res.append("else{\n");
        res.append("x = 0.;}\n");
        res.append("if(x < 0) x = moyenne;\n");
        res.append("usleep(x*1000000);\n");
        res.append("}\n\n");
        res.append("void delaiExponentiel(double lambda){\n");
        res.append("double random = RAND;\n");
        res.append("double x;\n");
        res.append("if(random != 0.){\n");
        res.append("x = -(((double)(log(random)))/lambda);}\n");
        res.append("else{\n");
        res.append("x = 0.;}\n");
        res.append("usleep(x*1000000);\n");
        res.append("}\n");
        res.append("void delaiLoi(int loi, double temps, double ecartTemps){\n");
        res.append("if (loi == 0){\n");
        res.append("delaiUniforme(temps, ecartTemps);\n}\n");
        res.append("else if(loi == 1){\n");
        res.append("delaiGauss(temps, ecartTemps);\n}\n");
        res.append("else{\n");
        res.append("delaiExponentiel(1.0 / temps);\n}\n");
        res.append("}\n\n");
        res.append("void simulation(int ids){\n");
        res.append("srand(getpid());\n");
        res.append("entrer(ENTREE_0);\n");
        res.append("delaiLoi(0, 4.0, 2.0);\n");
        res.append("transfert(ENTREE_0, ACTIVITE_2);\n");
        res.append("delai(4, 2);\n");
        res.append("transfert(ACTIVITE_2, GUICHET_3);\n");
        res.append("P(ids, SEM_GUICHET_3);\n");
        res.append("transfert(GUICHET_3, ACTIVITE_RESTREINTE_4);\n");
        res.append("delai(4, 2);\n");
        res.append("V(ids, SEM_GUICHET_3);\n");
        res.append("switch((int) ( (rand() / (float) RAND_MAX ) * 2)){\n");
        res.append("case 0 : {\n");
        res.append("transfert(ACTIVITE_RESTREINTE_4, SORTIE_1);\n");
        res.append("break;\n");
        res.append("}\n");
        res.append("case 1 : {\n");
        res.append("transfert(ACTIVITE_RESTREINTE_4, ACTIVITE_5);\n");
        res.append("delai(4, 2);\n");
        res.append("transfert(ACTIVITE_5, SORTIE_1);\n");
        res.append("break;\n");
        res.append("}\n");
        res.append("}\n");
        res.append("}");

        assertEquals(res.toString(), monde.toC());
    }

    @Test
    void toCMonde7(){

        // Monde de la diapo 4/51 du polycopié Projet de synthèse (3)

        Monde monde = new Monde();
        Guichet guichetC = new Guichet("Guichet caisse");
        ActiviteRestreinte caisse = new ActiviteRestreinte("Caisse");
        Activite zoo = new Activite("Zoo");
        Activite parc = new Activite("Parc");
        Guichet guichetT = new Guichet("Guichet toboggan");
        ActiviteRestreinte toboggan = new ActiviteRestreinte("Toboggan");
        Guichet guichetG = new Guichet("Guichet glace");
        ActiviteRestreinte glace = new ActiviteRestreinte("Glace");

        monde.ajouter(guichetC, caisse, zoo, parc, guichetT, toboggan, guichetG, glace);
        monde.aCommeEntree(guichetC, parc, guichetG);
        monde.aCommeSortie(toboggan, parc, glace);
        guichetC.ajouterSuccesseur(caisse);
        caisse.ajouterSuccesseur(zoo);
        zoo.ajouterSuccesseur(guichetT);
        guichetT.ajouterSuccesseur(toboggan);
        parc.ajouterSuccesseur(guichetT);
        guichetG.ajouterSuccesseur(glace);

        StringBuilder res = new StringBuilder();
        res.append("#include <unistd.h>\n");
        res.append("#include <math.h>\n");
        res.append("#include \"def.h\"\n\n");
        res.append("#define ENTREE_0 0\n");
        res.append("#define SORTIE_1 1\n");
        res.append("#define GUICHET_CAISSE_2 2\n");
        res.append("#define SEM_GUICHET_CAISSE_2 1\n");
        res.append("#define CAISSE_3 3\n");
        res.append("#define ZOO_4 4\n");
        res.append("#define PARC_5 5\n");
        res.append("#define GUICHET_TOBOGGAN_6 6\n");
        res.append("#define SEM_GUICHET_TOBOGGAN_6 2\n");
        res.append("#define TOBOGGAN_7 7\n");
        res.append("#define GUICHET_GLACE_8 8\n");
        res.append("#define SEM_GUICHET_GLACE_8 3\n");
        res.append("#define GLACE_9 9\n");

        res.append("\n#define RAND ((double) rand())/((double) RAND_MAX)\n\n");
        res.append("void delaiUniforme(double temps, double delta) {\n");
        res.append("int bi, bs ;\n");
        res.append("int n, nbSec ;\n");
        res.append("bi = temps - delta ;\n");
        res.append("if (bi < 0) bi = 0 ;\n");
        res.append("bs = temps + delta ;\n");
        res.append("n = bs - bi ;\n");
        res.append("nbSec = (rand()/ (float)RAND_MAX) * n ;\n");
        res.append("nbSec += bi ;\n");
        res.append("usleep(nbSec * 1000000);\n");
        res.append("}\n\n");
        res.append("void delaiGauss(double moyenne, double ecartype){\n");
        res.append("double u1 = RAND;\n");
        res.append("double x;\n");
        res.append("if(u1 != 0.){\n");
        res.append("x = sqrt(-2 * log(u1)) * cos(2 * 3.14 * RAND) * ecartype + moyenne;}\n");
        res.append("else{\n");
        res.append("x = 0.;}\n");
        res.append("if(x < 0) x = moyenne;\n");
        res.append("usleep(x*1000000);\n");
        res.append("}\n\n");
        res.append("void delaiExponentiel(double lambda){\n");
        res.append("double random = RAND;\n");
        res.append("double x;\n");
        res.append("if(random != 0.){\n");
        res.append("x = -(((double)(log(random)))/lambda);}\n");
        res.append("else{\n");
        res.append("x = 0.;}\n");
        res.append("usleep(x*1000000);\n");
        res.append("}\n");
        res.append("void delaiLoi(int loi, double temps, double ecartTemps){\n");
        res.append("if (loi == 0){\n");
        res.append("delaiUniforme(temps, ecartTemps);\n}\n");
        res.append("else if(loi == 1){\n");
        res.append("delaiGauss(temps, ecartTemps);\n}\n");
        res.append("else{\n");
        res.append("delaiExponentiel(1.0 / temps);\n}\n");
        res.append("}\n\n");

        res.append("void simulation(int ids){\n");
        res.append("srand(getpid());\n");
        res.append("entrer(ENTREE_0);\n");
        res.append("delaiLoi(0, 4.0, 2.0);\n");

        res.append("switch((int) ( (rand() / (float) RAND_MAX ) * 3)){\n");
        res.append("case 0 : {\n");
        res.append("transfert(ENTREE_0, GUICHET_CAISSE_2);\n");
        res.append("P(ids, SEM_GUICHET_CAISSE_2);\n");
        res.append("transfert(GUICHET_CAISSE_2, CAISSE_3);\n");
        res.append("delai(4, 2);\n");
        res.append("V(ids, SEM_GUICHET_CAISSE_2);\n");
        res.append("transfert(CAISSE_3, ZOO_4);\n");
        res.append("delai(4, 2);\n");
        res.append("transfert(ZOO_4, GUICHET_TOBOGGAN_6);\n");
        res.append("P(ids, SEM_GUICHET_TOBOGGAN_6);\n");
        res.append("transfert(GUICHET_TOBOGGAN_6, TOBOGGAN_7);\n");
        res.append("delai(4, 2);\n");
        res.append("V(ids, SEM_GUICHET_TOBOGGAN_6);\n");
        res.append("transfert(TOBOGGAN_7, SORTIE_1);\n");
        res.append("break;\n");
        res.append("}\n");

        res.append("case 1 : {\n");
        res.append("transfert(ENTREE_0, PARC_5);\n");
        res.append("delai(4, 2);\n");
        res.append("switch((int) ( (rand() / (float) RAND_MAX ) * 2)){\n");
        res.append("case 0 : {\n");
        res.append("transfert(PARC_5, SORTIE_1);\n");
        res.append("break;\n");
        res.append("}\n");
        res.append("case 1 : {\n");
        res.append("transfert(PARC_5, GUICHET_TOBOGGAN_6);\n");
        res.append("P(ids, SEM_GUICHET_TOBOGGAN_6);\n");
        res.append("transfert(GUICHET_TOBOGGAN_6, TOBOGGAN_7);\n");
        res.append("delai(4, 2);\n");
        res.append("V(ids, SEM_GUICHET_TOBOGGAN_6);\n");
        res.append("transfert(TOBOGGAN_7, SORTIE_1);\n");
        res.append("break;\n");
        res.append("}\n");
        res.append("}\n");
        res.append("break;\n");
        res.append("}\n");

        res.append("case 2 : {\n");
        res.append("transfert(ENTREE_0, GUICHET_GLACE_8);\n");
        res.append("P(ids, SEM_GUICHET_GLACE_8);\n");
        res.append("transfert(GUICHET_GLACE_8, GLACE_9);\n");
        res.append("delai(4, 2);\n");
        res.append("V(ids, SEM_GUICHET_GLACE_8);\n");
        res.append("transfert(GLACE_9, SORTIE_1);\n");
        res.append("break;\n");
        res.append("}\n");
        res.append("}\n");
        res.append("}");

        assertEquals(res.toString(), monde.toC());
    }

    @Test
    void toCMonde8(){

        // Monde avec : Guichet -> Activite Restreinte -> Guichet -> Activite Restreinte

        Monde monde = new Monde();
        Guichet guichet1 = new Guichet("Guichet1", 1);
        Guichet guichet2 = new Guichet("Guichet2", 2);
        Activite activite1 = new ActiviteRestreinte("Activite1", 5, 1);
        Activite activite2 = new ActiviteRestreinte("Activite2", 3, 1);

        monde.ajouter(guichet1, guichet2, activite1, activite2);
        monde.aCommeEntree(guichet1);
        monde.aCommeSortie(activite2);
        guichet1.ajouterSuccesseur(activite1);
        activite1.ajouterSuccesseur(guichet2);
        guichet2.ajouterSuccesseur(activite2);

        StringBuilder res = new StringBuilder();
        res.append("#include <unistd.h>\n");
        res.append("#include <math.h>\n");
        res.append("#include \"def.h\"\n\n");
        res.append("#define ENTREE_0 0\n");
        res.append("#define SORTIE_1 1\n");
        res.append("#define GUICHET1_2 2\n");
        res.append("#define SEM_GUICHET1_2 1\n");
        res.append("#define GUICHET2_3 3\n");
        res.append("#define SEM_GUICHET2_3 2\n");
        res.append("#define ACTIVITE1_4 4\n");
        res.append("#define ACTIVITE2_5 5\n");

        res.append("\n#define RAND ((double) rand())/((double) RAND_MAX)\n\n");
        res.append("void delaiUniforme(double temps, double delta) {\n");
        res.append("int bi, bs ;\n");
        res.append("int n, nbSec ;\n");
        res.append("bi = temps - delta ;\n");
        res.append("if (bi < 0) bi = 0 ;\n");
        res.append("bs = temps + delta ;\n");
        res.append("n = bs - bi ;\n");
        res.append("nbSec = (rand()/ (float)RAND_MAX) * n ;\n");
        res.append("nbSec += bi ;\n");
        res.append("usleep(nbSec * 1000000);\n");
        res.append("}\n\n");
        res.append("void delaiGauss(double moyenne, double ecartype){\n");
        res.append("double u1 = RAND;\n");
        res.append("double x;\n");
        res.append("if(u1 != 0.){\n");
        res.append("x = sqrt(-2 * log(u1)) * cos(2 * 3.14 * RAND) * ecartype + moyenne;}\n");
        res.append("else{\n");
        res.append("x = 0.;}\n");
        res.append("if(x < 0) x = moyenne;\n");
        res.append("usleep(x*1000000);\n");
        res.append("}\n\n");
        res.append("void delaiExponentiel(double lambda){\n");
        res.append("double random = RAND;\n");
        res.append("double x;\n");
        res.append("if(random != 0.){\n");
        res.append("x = -(((double)(log(random)))/lambda);}\n");
        res.append("else{\n");
        res.append("x = 0.;}\n");
        res.append("usleep(x*1000000);\n");
        res.append("}\n");
        res.append("void delaiLoi(int loi, double temps, double ecartTemps){\n");
        res.append("if (loi == 0){\n");
        res.append("delaiUniforme(temps, ecartTemps);\n}\n");
        res.append("else if(loi == 1){\n");
        res.append("delaiGauss(temps, ecartTemps);\n}\n");
        res.append("else{\n");
        res.append("delaiExponentiel(1.0 / temps);\n}\n");
        res.append("}\n\n");

        res.append("void simulation(int ids){\n");
        res.append("srand(getpid());\n");
        res.append("entrer(ENTREE_0);\n");
        res.append("delaiLoi(0, 4.0, 2.0);\n");

        res.append("transfert(ENTREE_0, GUICHET1_2);\n");
        res.append("P(ids, SEM_GUICHET1_2);\n");
        res.append("transfert(GUICHET1_2, ACTIVITE1_4);\n");
        res.append("delai(5, 1);\n");
        res.append("V(ids, SEM_GUICHET1_2);\n");

        res.append("transfert(ACTIVITE1_4, GUICHET2_3);\n");
        res.append("P(ids, SEM_GUICHET2_3);\n");
        res.append("transfert(GUICHET2_3, ACTIVITE2_5);\n");
        res.append("delai(3, 1);\n");
        res.append("V(ids, SEM_GUICHET2_3);\n");
        res.append("transfert(ACTIVITE2_5, SORTIE_1);\n");
        res.append("}");

        assertEquals(res.toString(), monde.toC());
    }
}