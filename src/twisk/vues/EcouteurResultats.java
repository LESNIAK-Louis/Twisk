package twisk.vues;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import twisk.mondeIG.MondeIG;

public class EcouteurResultats implements EventHandler<ActionEvent> {

    private MondeIG monde;

    /**
     * Constructeur de l'écouteur des Résultats
     * @param monde
     */
    public EcouteurResultats(MondeIG monde) { this.monde = monde; }

    /**
     * Permet d'afficher les résultats de la simulation lorsqu'ils sont demandés
     * @param actionEvent
     */
    @Override
    public void handle(ActionEvent actionEvent) {

        VueResultatsSimulations vueResultatsSimulations = new VueResultatsSimulations(monde);

        Scene scene = new Scene(vueResultatsSimulations, 500, 400);
        scene.getStylesheets().add(getClass().getResource("/style_resultats.css").toString());

        Stage fenetreResultats = new Stage();
        fenetreResultats.initModality(Modality.APPLICATION_MODAL);
        fenetreResultats.setResizable(false);
        fenetreResultats.setTitle("Résultats des simulations");
        fenetreResultats.setScene(scene);

        fenetreResultats.show();
    }
}
