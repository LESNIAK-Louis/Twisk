package twisk.vues;

import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import twisk.mondeIG.MondeIG;
import twisk.simulation.ResultatSimulation;

import java.text.DecimalFormat;

public class VueResultatsSimulations extends HBox implements Observateur{

    private MondeIG monde;

    private ListView<ResultatSimulation> simulations;
    private VBox resultats;

    private Label labelTitre, labelTitreLoi, labelTitreMonde, labelTitreActivite, labelTitreGuichet;
    private Label labelLoi, labelNbClients, labelResultatsMonde, labelResultatsActivite, labelResultatsGuichet;

    /**
     * Constructeur de la vue des résultats de simulation
     * @param monde Monde
     */
    public VueResultatsSimulations(MondeIG monde) {
        this.monde = monde;

        simulations = new ListView<ResultatSimulation>();
        simulations.getItems().addAll(monde.getResultatsSimulation());
        simulations.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        simulations.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> afficherResultats(newValue)) ;
        simulations.setPrefWidth(150);

        resultats = new VBox();
        resultats.setPrefWidth(350);
        resultats.getStyleClass().add("resultat");
        labelTitre = new Label("Résultats de la simulation");
        Separator sep1 = new Separator();
        labelNbClients = new Label("Nombre de clients : ");
        labelTitreLoi = new Label("Loi utilisée : ");
        labelLoi = new Label("Loi utilisée : ");
        Separator sep2 = new Separator();
        labelTitreMonde = new Label("Temps passé dans le monde : ");
        labelResultatsMonde = new Label();
        Separator sep3 = new Separator();
        labelTitreActivite = new Label("Temps passé dans les activités : ");
        labelResultatsActivite = new Label();
        Separator sep4 = new Separator();
        labelTitreGuichet = new Label("Temps d'attente dans les guichets : ");
        labelResultatsGuichet = new Label();

        resultats.getChildren().addAll(labelTitre, sep1, labelNbClients, labelTitreLoi, labelLoi,
                                        sep2, labelTitreActivite, labelResultatsActivite,
                                        sep3, labelTitreGuichet, labelResultatsGuichet,
                                        sep4, labelTitreMonde, labelResultatsMonde);

        this.getChildren().addAll(simulations, resultats);

        simulations.getSelectionModel().selectFirst();

        setStyle("titre-resultat", labelTitre);
        setStyle("sous-titre-resultat", labelNbClients, labelTitreLoi, labelTitreMonde);
        setStyle("sous-titre-resultat-activite", labelTitreActivite);
        setStyle("sous-titre-resultat-guichet", labelTitreGuichet);
        setStyle("donnees-resultat-monde", labelLoi, labelResultatsMonde);
        setStyle("donnees-resultat-activite", labelResultatsActivite);
        setStyle("donnees-resultat-guichet", labelResultatsGuichet);
    }

    /**
     * Fonction reagir
     */
    @Override
    public void reagir() {

    }

    /**
     * Affichage des résultats de la simulation sélectionnée
     * @param res Données du résultat de la simulation
     */
    private void afficherResultats(ResultatSimulation res){

        DecimalFormat df = new DecimalFormat("#.00");

        String loi = "\t- " + res.getLoi() + "\n\t- Moyenne :\t" + res.getMoyenneLoi() +
                "\n\t- Ecart-type :\t" + res.getEcartTypeLoi();
        String resultatsMonde = "\t- Minimum :\t" + res.calculerTempsMinDansMonde() + "\n\t- Maximum :\t" +
                res.calculerTempsMaxDansMonde() + "\n\t- Moyenne :\t" + df.format(res.calculerTempsMoyenDansMonde());
        String resultatsActivite = "\t- Minimum :\t" + res.calculerTempsMinDansActivite() + "\n\t- Maximum :\t" +
                res.calculerTempsMaxDansActivite() + "\n\t- Moyenne :\t" + df.format(res.calculerTempsMoyenDansActivite());
        String resultatsGuichet = "\t- Minimum :\t" + res.calculerTempsMinDansGuichet() + "\n\t- Maximum :\t" +
                res.calculerTempsMaxDansGuichet() + "\n\t- Moyenne :\t" + df.format(res.calculerTempsMoyenDansGuichet());

        labelTitre.setText("Résultats de la simulation n°" + res.getId());
        labelNbClients.setText("Nombre de clients : " + res.getNbClients());
        labelLoi.setText(loi);
        labelResultatsMonde.setText(resultatsMonde);
        labelResultatsActivite.setText(resultatsActivite);
        labelResultatsGuichet.setText(resultatsGuichet);
    }

    /**
     * Application d'un style aux labels donnés
     * @param style Nom du style
     * @param labels Labels
     */
    private void setStyle(String style, Label... labels){
        for(Label l : labels){
            l.getStyleClass().add(style);
        }
    }

}
