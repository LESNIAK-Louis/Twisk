package twisk.vues;

public interface Observateur {
    /**
     * Permet de faire réagir tous les observateurs afin d'actualiser les modifications effectuées
     */
    void reagir();
}
