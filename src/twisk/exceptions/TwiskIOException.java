package twisk.exceptions;

import javafx.animation.PauseTransition;
import javafx.scene.control.Alert;
import javafx.util.Duration;

import java.io.IOException;

public class TwiskIOException extends TwiskException {

    public TwiskIOException(String msg){
        super("Erreur fichier : " + msg);
    }

}
