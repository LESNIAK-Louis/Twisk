package twisk.vues;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import twisk.exceptions.ParametresIncorrectsException;
import twisk.mondeIG.ActiviteIG;
import twisk.mondeIG.EtapeIG;
import twisk.mondeIG.GuichetIG;
import twisk.mondeIG.MondeIG;

import java.util.Optional;

public class EcouteurParametres implements EventHandler<ActionEvent> {

    private MondeIG monde;

    /**
     * Constructeur de l'écouteur de modification du temps d'une activitée
     * @param monde
     */
    public EcouteurParametres(MondeIG monde){

        this.monde = monde;
    }

    /**
     * Action suite au clic de de l'item Paramètres du menu : modifie les durées de l'activité selectionné
     * @param actionEvent
     */
    @Override
    public void handle(ActionEvent actionEvent) {

        EtapeIG etape = monde.premEtapeSelectionnee();

        if(etape.estUnGuichet())
            afficherDialogueGuichet((GuichetIG)etape);
        else
            afficherDialogueActivite((ActiviteIG)etape);
    }

    /**
     * Affiche le dialogue des paramètres pour un guichet
     */
    private void afficherDialogueGuichet(GuichetIG guichet){

        TextInputDialog dialogue = new TextInputDialog();
        dialogue.setTitle("Paramètres du guichet");
        dialogue.getEditor().textProperty().setValue((guichet).getNbJetons() + "");
        dialogue.setHeaderText(null);
        dialogue.setGraphic(null);
        dialogue.setContentText("Nombre de jeton(s) : ");

        // Permet de s'assurer que le champ ne contient que des chiffres
        dialogue.getEditor().textProperty().addListener((o, oldValue, newValue) -> supprimerLettres(dialogue.getEditor(), newValue));
        Optional<String> nom = dialogue.showAndWait();
        nom.ifPresent(str -> {
            try {
                if(str.equals("")) str = "0";
                monde.modifierSelection(Integer.parseInt(str));
            } catch (ParametresIncorrectsException e) {e.afficherAlerte();}
        });
    }

    /**
     * Affiche le dialogue des paramètres pour une activité
     */
    private void afficherDialogueActivite(ActiviteIG activite){

        Dialog<int[]> dialogue = new Dialog<>();
        dialogue.setTitle("Paramètres de l'activité");
        dialogue.setHeaderText(null);

        Label labelTemps = new Label("Temps :");
        Label labelEcart = new Label("Ecart :");
        TextField champTemps = new TextField(Integer.toString(((ActiviteIG) activite).getTemps()));
        TextField champEcart = new TextField(Integer.toString(((ActiviteIG) activite).getEcartTemps()));

        // Permet de s'assurer que les champs ne contiennent que des chiffres
        champTemps.textProperty().addListener((o, oldValue, newValue) -> supprimerLettres(champTemps, newValue));
        champEcart.textProperty().addListener((o, oldValue, newValue) -> supprimerLettres(champEcart, newValue));

        GridPane grille = new GridPane();
        grille.add(labelTemps, 1, 1);
        grille.add(champTemps, 2, 1);
        grille.add(labelEcart, 1, 2);
        grille.add(champEcart, 2, 2);
        dialogue.getDialogPane().setContent(grille);

        ButtonType boutonOK = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        ButtonType boutonAnnuler = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialogue.getDialogPane().getButtonTypes().addAll(boutonOK, boutonAnnuler);

        dialogue.setResultConverter(b -> {
            if (b == boutonOK) {
                if(champTemps.getText().equals(""))
                    champTemps.setText("0");
                if(champEcart.getText().equals(""))
                    champEcart.setText("0");
                int[] res = {Integer.parseInt(champTemps.getText()), Integer.parseInt(champEcart.getText())};
                return res;
            }
            return null;
        });

        Optional<int[]> res = dialogue.showAndWait();
        res.ifPresent(val -> {
            try {
                monde.modifierSelection(val[0], val[1]);
            } catch (ParametresIncorrectsException e) {
                e.afficherAlerte();
            }
        });
    }

    /**
     * Supprime les caractères qui ne correspondent pas à un chiffre dans un TextField donné
     * @param champ
     * @param valeur
     */
    private void supprimerLettres(TextField champ, String valeur){

        if (!valeur.matches("[0-9]+")) {
            champ.setText(valeur.replaceAll("[^[0-9]]", ""));
        }
    }
}
