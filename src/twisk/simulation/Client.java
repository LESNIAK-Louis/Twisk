package twisk.simulation;

import twisk.monde.Etape;

import java.util.Random;

public class Client {

    private int numeroClient;
    private int rang;
    private Etape etape;

    private int tempsDansMonde;
    private int tempsAttenteGuichet, tempsDansActivite;

    /**
     * Constructeur de Client
     * @param numero
     */
    public Client(int numero){

        this.numeroClient = numero;
        this.tempsDansMonde = 0;
        this.tempsAttenteGuichet = 0;
        this.tempsDansActivite = 0;
    }

    /**
     * Getter du numéro du Client
     * @return numeroClient
     */
    public int getNumero(){
        return this.numeroClient;
    }

    /**
     * Getter du rang du Client
     * @return rang
     */
    public int getRang(){
        return this.rang;
    }

    /**
     * Getter de l'étape où est le client
     * @return etape
     */
    public Etape getEtape(){
        return this.etape;
    }

    /**
     * Getter du temps passé par le client dans le monde
     * @return temps passé dans le monde
     */
    public int getTempsDansMonde(){
        return this.tempsDansMonde;
    }

    /**
     * Getter du temps passé par le client dans un guichet
     * @return temps passé dans un guichet
     */
    public int getTempsAttenteGuichet(){
        return this.tempsAttenteGuichet;
    }

    /**
     * Getter du temps passé par le client dans une activité
     * @return temps passé dans une activité
     */
    public int getTempsDansActivite(){
        return this.tempsDansActivite;
    }

    /**
     * Permet de déplacer le client dans une étape à un rang donné
     * @param etape
     * @param rang
     */
    public void allerA(Etape etape, int rang){

        this.etape = etape;
        this.rang = rang;

        mettreAJourStatistiques();
    }

    /**
     * Met à jour les statistiques liées au temps passé dans les étapes du monde
     */
    private void mettreAJourStatistiques(){
        if(!etape.estUneEntree() && !etape.estUneSortie()) {
            this.tempsDansMonde++;
            if (etape.estUnGuichet())
                this.tempsAttenteGuichet++;
            else
                this.tempsDansActivite++;
        }
    }

    /**
     * Test l'égalité entre deux clients
     * @param o
     * @return true si égal, false sinon
     */
    @Override
    public boolean equals(Object o){
        if (o instanceof Client) {
            Client c = (Client) o;
            return this.getNumero() == c.getNumero();
        } else return false ;
    }
}
