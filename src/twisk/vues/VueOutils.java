package twisk.vues;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import twisk.exceptions.MondeException;
import twisk.mondeIG.LoiClients;
import twisk.mondeIG.MondeIG;
import twisk.outils.RessourcesLoader;
import twisk.outils.TailleComposants;
import twisk.outils.ThreadsManager;

public class VueOutils extends AnchorPane implements Observateur{

    private MondeIG monde;
    private Button ajouterActivite, ajouterGuichet, lancerSimulation, arreterSimulation;

    private ComboBox<LoiClients> loiChoisie;
    private VBox panneauClients;
    private Label nbClientsLabel;
    private Slider nbClientsSlider;

    /**
     * Constructeur de VueOutils
     * @param monde
     */
    public VueOutils(MondeIG monde) {
        TailleComposants tailles = TailleComposants.getInstance();

        this.monde = monde;
        this.ajouterActivite = new Button("+");
        this.ajouterGuichet = new Button("+");
        this.lancerSimulation = new Button((char)9654 + "");
        this.arreterSimulation = new Button((char)9632 + "");
        this.panneauClients = new VBox();
        this.nbClientsLabel = new Label(monde.getNbClients() + " clients");
        this.nbClientsSlider = new Slider();
        this.loiChoisie = new ComboBox<LoiClients>();

        loiChoisie.getItems().setAll(LoiClients.values());
        loiChoisie.setPrefSize(tailles.getTailleLargeurLoi(),tailles.getTailleHauteurLoi());
        loiChoisie.getSelectionModel().selectFirst();

        loiChoisie.setOnAction( a -> monde.setLoi(loiChoisie.getValue()));

        ajouterActivite.setOnAction(a -> monde.ajouter("Activité"));
        ajouterGuichet.setOnAction(a -> monde.ajouter("Guichet"));
        lancerSimulation.setOnAction(a -> {
            try {
                monde.simuler();
            } catch (MondeException e) {
                e.afficherAlerte();
            }
        });

        arreterSimulation.setOnAction(a -> {
                ThreadsManager.getInstance().detruireTout();
        });

        setStyleBoutons(ajouterActivite, "bouton-ajouter-activite", "Ajouter une activité");
        setStyleBoutons(ajouterGuichet, "bouton-ajouter-guichet", "Ajouter un guichet");
        setStyleBoutons(lancerSimulation, "bouton-lancer-simulation", "Démarrer la simulation");
        setStyleBoutons(arreterSimulation, "bouton-arreter-simulation", "Arrêter la simulation");

        nbClientsSlider.setMin(1);
        nbClientsSlider.setMax(49);
        nbClientsSlider.setBlockIncrement(1);
        nbClientsSlider.setShowTickLabels(false);
        nbClientsSlider.setShowTickMarks(false);
        nbClientsSlider.setValue(monde.getNbClients());
        nbClientsSlider.valueProperty().addListener(
                (observable, oldValue, newValue) -> monde.setNbClients(newValue.intValue())
        );

        int tailleSlider = tailles.getTailleSliderClients();
        nbClientsSlider.setPrefWidth(tailleSlider);
        panneauClients.getStyleClass().add("outils-clients");
        panneauClients.getChildren().addAll(nbClientsLabel, nbClientsSlider);

        this.getStyleClass().add("outils");

        int tailleBouton = tailles.getTailleBoutonOutils();
        AnchorPane.setLeftAnchor(ajouterActivite, 5.0);
        AnchorPane.setLeftAnchor(ajouterGuichet, tailleBouton + 10.0);
        AnchorPane.setRightAnchor(panneauClients, tailles.getTailleLargeurLoi() + 15.0);
        AnchorPane.setRightAnchor(lancerSimulation, tailles.getTailleLargeurLoi() + tailleSlider + 25.0);
        AnchorPane.setRightAnchor(arreterSimulation,  tailles.getTailleLargeurLoi() + tailleSlider + 25.0);
        AnchorPane.setRightAnchor(loiChoisie, 0.0);

        this.getChildren().addAll(ajouterActivite, ajouterGuichet, panneauClients, lancerSimulation, loiChoisie);
        this.monde.ajouterObservateur(this);
    }

    /**
     * Modifie le visuel de fond de la VueOutils en fonction du fond selectionné
     */
    @Override
    public void reagir() {
        AnchorPane pane = this;
        Runnable command = new Runnable() {
            @Override
            public void run() {
                Image img = RessourcesLoader.getInstance().getFond(monde.getFond());
                Background fond = new Background(new BackgroundImage(img, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT,
                        BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT));
                pane.setBackground(fond);

                if(monde.enSimulation())
                {
                    ajouterActivite.setDisable(true);
                    ajouterGuichet.setDisable(true);
                    nbClientsSlider.setDisable(true);
                    loiChoisie.setDisable(true);
                    pane.getChildren().remove(lancerSimulation);
                    if(!pane.getChildren().contains(arreterSimulation))
                        pane.getChildren().add(arreterSimulation);
                }
                else
                {
                    ajouterActivite.setDisable(false);
                    ajouterGuichet.setDisable(false);
                    nbClientsSlider.setDisable(false);
                    loiChoisie.setDisable(false);
                    pane.getChildren().remove(arreterSimulation);
                    if(!pane.getChildren().contains(lancerSimulation))
                        pane.getChildren().add(lancerSimulation);
                }
                int nbClients = monde.getNbClients();
                nbClientsLabel.setText(monde.getNbClients() + " client" + (nbClients > 1 ? "s" : ""));
                nbClientsSlider.setValue(nbClients);

                loiChoisie.getSelectionModel().select(loiChoisie.getItems().get(monde.getLoi().getId()));
            }
        };
        if(Platform.isFxApplicationThread())
            command.run();
        else
            Platform.runLater(command);
    }

    /**
     * Définit le style à attribuer à un bouton
     * @param bouton Bouton
     * @param styleClass Nom du style
     * @param tooltipText Texte du tooltip du bouton
     */
    private void setStyleBoutons(Button bouton, String styleClass, String tooltipText){

        int taille = TailleComposants.getInstance().getTailleBoutonOutils();
        bouton.setMinSize(taille, taille);
        bouton.getStyleClass().add(styleClass);
        Tooltip tooltip = new Tooltip();
        tooltip.setText(tooltipText);
        tooltip.getStyleClass().add("tooltip");
        bouton.setTooltip(tooltip);
    }
}
