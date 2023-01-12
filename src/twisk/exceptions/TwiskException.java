package twisk.exceptions;

import javafx.animation.PauseTransition;
import javafx.scene.control.Alert;
import javafx.util.Duration;

public class TwiskException extends Exception {

    /**
     * Constructeur TwiskException
     */
    public TwiskException(String msg){
        super(msg);
    }

    /**
     * Affiche une alerte box avec l'erreur en question
     */
    public void afficherAlerte(){

        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("Erreur");
        a.setHeaderText(null);
        a.setContentText(getMessage());
        a.show();
        PauseTransition p = new PauseTransition(Duration.seconds(6));
        p.setOnFinished(event -> a.hide());
        p.play();
    }
}
