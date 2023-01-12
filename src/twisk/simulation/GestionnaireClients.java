package twisk.simulation;

import twisk.monde.Etape;

import java.util.HashMap;
import java.util.Iterator;

public class GestionnaireClients implements Iterable<Client> {

    private HashMap<Integer, Client> clients;
    private ResultatSimulation resultat;

    /**
     * Constructeur GestionnaireClients
     */
    public GestionnaireClients(){
        clients = new HashMap<>();
    }

    /**
     * Constructeur GestionnaireClients via le nombre de clients
     * @param nbClients
     */
    public GestionnaireClients(int nbClients) {
        clients = new HashMap<>(nbClients);
    }

    /**
     * Permet de récupérer un client via son numéro
     * @param numero
     * @return Client
     */
    public Client getClient(int numero){
        return clients.get(numero);
    }

    /**
     * Permet de récupérer le nombre de clients
     * @return Nombre de clients
     */
    public int getNbClients(){
        return clients.size();
    }

    /**
     * Permet de récupérer le résultat de la dernière simulation
     * @return Résultat de la dernière simulation
     */
    public ResultatSimulation getResultat(){
        return resultat;
    }

    /**
     * Ajoute des clients au gesitonnaire en les instanciant via leur numéro
     * @param tabClients numéro des clients à instancier
     * @return
     */
    public void setClients(int ... tabClients) {
        for(int numero : tabClients)
            clients.put(numero, new Client(numero));
    }

    /**
     * Permet de déplacer un client
     * @param numeroClient
     * @param etape
     * @param rang
     */
    public void allerA(int numeroClient, Etape etape, int rang) {
        clients.get(numeroClient).allerA(etape,rang);
    }

    /**
     * Supprime tout les clients actuels dans le GestionnaireClient
     * et récupère les résultats de la dernière simulation
     */
    public void nettoyer() {

        boolean recupereResultat = true;
        for(Client c : this){
            if(c.getEtape() == null || !c.getEtape().estUneSortie())
                recupereResultat = false;
        }

        if(recupereResultat)
            resultat = new ResultatSimulation(this);
        else
            resultat = null;

        clients.clear();
    }

    /**
     * Iterateur des clients
     * @return clients du gesitonnaire
     */
    public Iterator<Client> iterator() { return clients.values().iterator(); }
}
