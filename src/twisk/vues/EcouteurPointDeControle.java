package twisk.vues;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import twisk.exceptions.ArcNonValideException;
import twisk.mondeIG.MondeIG;
import twisk.mondeIG.PointDeControleIG;

public class EcouteurPointDeControle implements EventHandler<MouseEvent> {

    private MondeIG monde;
    private PointDeControleIG point;

    /**
     * Constructeur de l'écouteur de séléection d'un PointDeControleIG
     * @param monde
     * @param pt
     */
    public EcouteurPointDeControle(MondeIG monde, PointDeControleIG pt){

        this.monde = monde;
        this.point = pt;
    }

    /**
     * Action suite au clic sur un PointDeControleIG : sélectionne le point.
     * @param mouseEvent
     */
    @Override
    public void handle(MouseEvent mouseEvent) {

        try {
            monde.selectionnerPoint(point);
        } catch (ArcNonValideException e) {
            e.afficherAlerte();
        }
    }
}
