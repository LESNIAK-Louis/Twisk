package twisk.vues;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import twisk.mondeIG.ActiviteIG;
import twisk.mondeIG.GuichetIG;
import twisk.mondeIG.MondeIG;
import twisk.outils.TailleComposants;

public class VueGuichetIG extends VueEtapeIG{

    /**
     * Constructeur de la vue des Activités
     * @param monde
     * @param etape
     */
    public VueGuichetIG(MondeIG monde, GuichetIG etape){
        super(monde, etape);
        titre.setText(etape.getNom() + " : " + etape.getNbJetons() + " jetons");
        int taille = TailleComposants.getInstance().getTailleEmplacementClient();
        for(int i = 0; i < 8; i++){
            Pane placeClient = new Pane();
            placeClient.setPrefSize(taille, taille);
            placeClient.getStyleClass().add("zone-client-guichet");
            this.zoneClients.getChildren().add(placeClient);
        }
        this.zoneClients.setAlignment(Pos.CENTER);
        this.getStyleClass().add("guichet");
    }

    /**
     * Modifie le visuel du guichet si il est sélectionné
     */
    @Override
    public void reagir() {
        super.reagir();
        VBox vbox = this;
        Runnable command = new Runnable() {
            @Override
            public void run() {
                if (monde.estSelectionnee(etape)) {
                    vbox.getStyleClass().add("guichet-select");
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
