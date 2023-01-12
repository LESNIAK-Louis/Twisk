package twisk.simulation;

import twisk.monde.Etape;
import twisk.monde.Monde;
import twisk.mondeIG.SujetObserve;
import twisk.outils.KitC;
import twisk.outils.ThreadsManager;

public class Simulation extends SujetObserve {

    private KitC kitC;
    private int nbClients;
    private GestionnaireClients gestionnaireClients;

    /**
     * Fonction C Native permettant de démarrer une simulation
     * @param nbEtapes
     * @param nbServices
     * @param nbClients
     * @param tabJetonsServices
     * @return
     */
    public native int[] start_simulation(int nbEtapes, int nbServices, int nbClients, int[] tabJetonsServices);

    /**
     * Fonction C Native permettant d'obtenir des informations sur les clients de la simulation en cours
     * @param nbEtapes
     * @param nbClients
     * @return
     */
    public native int[] ou_sont_les_clients(int nbEtapes, int nbClients);

    /**
     * Fonction C Native permettant de nettoyer la simulation en cours
     */
    public native void nettoyage();

    /**
     * Contructeur de Simulation
     */
    public Simulation(){
        this.kitC = new KitC();
        kitC.creerEnvironnement();
        this.nbClients = 10;
        gestionnaireClients = new GestionnaireClients(nbClients);
    }

    /**
     * Getter du GestionnaireClients
     * @return gestionnaireClients
     */
    public GestionnaireClients getGestionnaireClients() {
        return gestionnaireClients;
    }

    /**
     * Setter du nombre de clients.
     * @param nbClients
     */
    public void setNbClients(int nbClients){
        this.nbClients = nbClients;
    }

    /**
     * Permet de simuler le Monde passé en paramètre
     * @param monde
     */
    public void simuler(Monde monde)
    {
        try{
            kitC.creerFichier(monde.toC());
            kitC.compiler();
            kitC.construireLaLibrairie();
            System.load(kitC.getNomLib());

            //System.out.println(monde.toString());

            int[] tabStart = start_simulation(monde.nbEtapes(), monde.nbGuichets(), nbClients, monde.getTabJetons());
            gestionnaireClients.setClients(tabStart);
            /*
            System.out.print("les clients: ");
            for (int i = 0; i < nbClients; i++) {
                System.out.print("" + tabStart[i] + " ");
            }
            System.out.println("\n");*/

            int nbClientsSortie = 0;
            while (nbClientsSortie < nbClients) {
                int[] tabClients = ou_sont_les_clients(monde.nbEtapes(), nbClients);
                nbClientsSortie = tabClients[nbClients + 1];

                for (Etape e : monde) {
                    // La sortie ne sera affichée qu'à la fin
                    if (!e.estUneSortie()) {
                        miseAJourClientsEtape(e, tabClients);
                    }
                }
                // Déplacement des clients
                miseAJourClientsEtape(monde.getSortie(), tabClients);

                notifierObservateurs();
                //System.out.println("");
                Thread.sleep(1000);
            }

        } catch(InterruptedException e) {
            kitC.tuerProcessus(gestionnaireClients);
        }
        gestionnaireClients.nettoyer();
        nettoyage();
        ThreadsManager.getInstance().detruireTout();
        notifierObservateurs();
    }

    /**
     * Met à jour la position des clients dans les étapes
     * @param e
     * @param clients
     */
    private void miseAJourClientsEtape(Etape e, int[] clients){

        int nbClientsEtape = clients[e.getNumero() * (nbClients+1)];
        //System.out.print("etape " + e.getNumero() + " (" + e.getNom()  + ")" + " - " + nbClientsEtape + " client" + (nbClientsEtape>1?"s":"") + " :");
        for(int j = 0; j < nbClientsEtape; j++){
            int indiceClient = j+(e.getNumero()*(nbClients+1))+1;
            int numeroClient = clients[indiceClient];
            //System.out.print(" " + numeroClient);

            gestionnaireClients.allerA(numeroClient, e, j);
        }
        //System.out.println("");
    }
}
