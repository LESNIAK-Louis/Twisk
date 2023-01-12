package twisk.vues;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextInputDialog;
import twisk.mondeIG.MondeIG;

import java.util.Optional;

public class EcouteurRenommer implements EventHandler<ActionEvent> {

    private MondeIG monde;

    /**
     * Constructeur de l'écouteur de modification du nom d'une étape
     * @param monde
     */
    public EcouteurRenommer(MondeIG monde){

        this.monde = monde;
    }

    /**
     * Action suite au clic de de l'item Renommer du menu : renomme l'étape sélectionné.
     * @param actionEvent
     */
    @Override
    public void handle(ActionEvent actionEvent) {

        TextInputDialog dialogue = new TextInputDialog();
        dialogue.setTitle("Renommer activité");
        dialogue.setHeaderText(null);
        dialogue.setGraphic(null);
        dialogue.setContentText("Nom : ");

        Optional<String> nom = dialogue.showAndWait();

        nom.ifPresent(str -> {
            monde.renommerSelection(str);
        });
    }
}
