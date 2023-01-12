package twisk.vues;

import javafx.application.Platform;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import twisk.mondeIG.ActiviteIG;
import twisk.mondeIG.EtapeIG;
import twisk.mondeIG.MondeIG;

public class VueActiviteIG extends VueEtapeIG{

    /**
     * Constructeur de la vue des Activités
     * @param monde
     * @param etape
     */
    public VueActiviteIG(MondeIG monde, ActiviteIG etape){
        super(monde, etape);
        titre.setText(etape.getNom() + " : " + etape.getTemps() + " " + (char)177 + " " + etape.getEcartTemps() + " temps");
        this.zoneClients.getStyleClass().add("zone-client-activite");
        this.getStyleClass().add("activite");
    }

    /**
     * Modifie le visuel de l'activité si elle est sélectionné
     */
    @Override
    public void reagir() {

        super.reagir();
        VBox vbox = this;
        Runnable command = new Runnable() {
            @Override
            public void run() {
                if (monde.estSelectionnee(etape)) {
                    vbox.getStyleClass().add("activite-select");
                } else if (vbox.getStyleClass().size() > 1) {
                    vbox.getStyleClass().remove(1);
                }
            }
        };
        if(Platform.isFxApplicationThread())
            command.run();
        else
            Platform.runLater(command);
    }
}
