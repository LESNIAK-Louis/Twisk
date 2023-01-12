package twisk.vues;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import twisk.MainTwisk;
import twisk.exceptions.MondeException;
import twisk.exceptions.ParametresIncorrectsException;
import twisk.exceptions.TwiskException;
import twisk.exceptions.TwiskIOException;
import twisk.mondeIG.MondeIG;
import twisk.outils.ThreadsManager;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Optional;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class VueMenu extends MenuBar implements Observateur {

    private MondeIG monde;
    private Menu fichier, edition, menuMonde, simulation, style, fond, ouvrirDefaut, supprimerMonde;

    private MenuItem nouveau, quitter, enregistrerSous, ouvrir,
                     supprimer, renommer, effacerSelection,
                     entree, sortie, parametres,
                     lois, resultats;

    private RadioMenuItem vide, points, grille;

    /**
     * Constructeur de la vue du Menu
     * @param monde MondeIG

     */
    public VueMenu(MondeIG monde){

        this.monde = monde;

        this.fichier = new Menu("Fichier");
        nouveau = new MenuItem("Nouveau");
        nouveau.setOnAction(e -> {
            monde.initialiser();
            MainTwisk.setTitreStage("Twisk");
        });

        enregistrerSous = new MenuItem("Enregistrer sous...");
        enregistrerSous.setOnAction(e -> enregistrerSous());

        ouvrir = new MenuItem("Ouvrir");
        ouvrir.setOnAction(e -> ouvrir());

        ouvrirDefaut = new Menu("Ouvrir un monde préexistant");
        updateCharger();
        supprimerMonde = new Menu("Supprimer un monde préexistant");
        updateSupprimer();
        quitter = new MenuItem("Quitter");
        quitter.setOnAction(event -> {
            if(monde.enSimulation())
                ThreadsManager.getInstance().detruireTout();
            Platform.exit();
        });

        fichier.getItems().addAll(nouveau, new SeparatorMenuItem(),
                                ouvrir, ouvrirDefaut, new SeparatorMenuItem(),
                                enregistrerSous, new SeparatorMenuItem(),
                                supprimerMonde, new SeparatorMenuItem(),
                                quitter);

        this.edition = new Menu("Edition");
        supprimer = new MenuItem("Supprimer la sélection");
        renommer = new MenuItem("Renommer la sélection");
        effacerSelection = new MenuItem("Tout déselectionner");
        supprimer.setOnAction(event -> monde.supprimerSelection());
        supprimer.setDisable(true);
        renommer.setOnAction(new EcouteurRenommer(monde));
        renommer.setDisable(true);
        effacerSelection.setOnAction(event -> monde.toutDeselectionner());
        effacerSelection.setDisable(true);
        edition.getItems().addAll(supprimer, renommer, effacerSelection);

        this.menuMonde = new Menu("Monde");
        entree = new MenuItem("Entrée");
        sortie = new MenuItem("Sortie");
        parametres = new MenuItem("Paramètres");
        entree.setOnAction(event -> monde.inverserEntreeSelection());
        sortie.setOnAction(event -> monde.inverserSortieSelection());
        parametres.setOnAction(new EcouteurParametres(monde));
        entree.setDisable(true);
        sortie.setDisable(true);
        parametres.setDisable(true);
        menuMonde.getItems().addAll(entree, sortie, parametres);

        this.simulation = new Menu("Simulation");
        lois = new MenuItem("Lois d'arrivée des clients");
        lois.setOnAction(event -> afficherDialogueLoi());
        resultats = new MenuItem("Résultats");
        resultats.setOnAction(new EcouteurResultats(monde));
        resultats.setDisable(true);
        simulation.getItems().addAll(lois, resultats);

        this.style = new Menu("Style");
        this.fond = new Menu("Fond");
        vide = new RadioMenuItem("Vide");
        points = new RadioMenuItem("Points");
        grille = new RadioMenuItem("Grille");
        ToggleGroup groupe = new ToggleGroup();
        vide.setToggleGroup(groupe);
        points.setToggleGroup(groupe);
        grille.setToggleGroup(groupe);
        grille.setSelected(true);
        vide.setOnAction(event -> monde.changerFond("vide"));
        points.setOnAction(event -> monde.changerFond("points"));
        grille.setOnAction(event -> monde.changerFond("grille"));

        style.getItems().add(fond);
        fond.getItems().addAll(grille, points, vide);

        this.getMenus().addAll(fichier, edition, menuMonde, simulation, style);

        monde.ajouterObservateur(this);
    }

    /**
     * Modifie le visuel du menu en rendant cliquable ou non les items du menu en fonction des données du MondeIG
     */
    @Override
    public void reagir() {
        Runnable command = new Runnable() {
            @Override
            public void run() {
                updateCharger();
                updateSupprimer();

                nouveau.setDisable(monde.enSimulation());
                ouvrir.setDisable(monde.enSimulation());
                ouvrirDefaut.setDisable(monde.enSimulation());
                enregistrerSous.setDisable(monde.enSimulation());
                supprimerMonde.setDisable(monde.enSimulation());
                edition.setDisable(monde.enSimulation());
                menuMonde.setDisable(monde.enSimulation());
                simulation.setDisable(monde.enSimulation());
                style.setDisable(monde.enSimulation());

                renommer.setDisable(monde.getNombreEtapesSelectionnes() != 1);
                supprimer.setDisable(monde.getNombreEtapesSelectionnes() + monde.getNombreArcsSelectiones() == 0);
                effacerSelection.setDisable(monde.getNombreEtapesSelectionnes() + monde.getNombreArcsSelectiones() == 0);
                entree.setDisable(monde.getNombreEtapesSelectionnes() == 0);
                sortie.setDisable(monde.getNombreEtapesSelectionnes() == 0);
                parametres.setDisable(monde.getNombreEtapesSelectionnes() != 1);
                resultats.setDisable(monde.nbResultatsSimulation() <= 0);
            }
        };
        if(Platform.isFxApplicationThread())
            command.run();
        else
            Platform.runLater(command);
    }

    /**
     * Update le menu supprimerMonde
     */
    private void updateSupprimer()
    {
        supprimerMonde.getItems().clear();

        File[] fichiers = Paths.get("./mondes").toFile().listFiles();
        if(fichiers != null) {
            for (File fichier : fichiers) {
                MenuItem item = new MenuItem(fichier.getName().replace(".twk", "").replace("_"," "));
                supprimerMonde.getItems().add(item);
                item.setOnAction(e -> {
                    Alert a = new Alert(Alert.AlertType.CONFIRMATION);
                    a.setTitle("Confirmation");
                    a.setHeaderText(null);
                    a.setContentText("Souhaitez-vous vraiment supprimer le monde " + item.getText() + " ?");
                    Optional<ButtonType> res = a.showAndWait();
                    if (res.isPresent() && res.get() == ButtonType.OK) {
                        fichier.delete();
                        monde.notifierObservateurs();
                    }
                });
            }
        }
    }

    /**
     * Update le menu charger
     */
    private void updateCharger()
    {
        ouvrirDefaut.getItems().clear();
        File[] fichiers = Paths.get("./mondes").toFile().listFiles();
        if(fichiers != null) {
            for (File fichier : fichiers) {
                MenuItem item = new MenuItem(fichier.getName().replace(".twk", "").replace("_"," "));
                ouvrirDefaut.getItems().add(item);
                item.setOnAction(e -> {
                    try {
                        monde.ouvrir(fichier);
                        MainTwisk.setTitreStage("Twisk - " + fichier.getName());
                    } catch (TwiskIOException ex) {
                        ex.afficherAlerte();
                    }
                });
            }
        }
    }

    /**
     * Affiche le dialogue des paramètres pour une loi
     */
    private void afficherDialogueLoi(){

        Dialog<Double[]> dialogue = new Dialog<Double[]>();
        dialogue.setTitle("Paramètres de la loi d'arrivée des clients");
        dialogue.setHeaderText(null);

        GridPane grille = new GridPane();

        Label labelMoyenne = new Label("Moyenne :");
        Label labelEcart = new Label("Ecart-type :");

        TextFormatter<Double> textFormatterMoyenne = creerTextFormatterPourDouble();
        TextField champMoyenne = new TextField();
        champMoyenne.setTextFormatter(textFormatterMoyenne);
        champMoyenne.setText(Double.toString(monde.getMoyenneLoi()));

        TextFormatter<Double> textFormatterEcart = creerTextFormatterPourDouble();
        TextField champEcart = new TextField();
        champEcart.setTextFormatter(textFormatterEcart);
        champEcart.setText(Double.toString(monde.getEcartTypeLoi()));

        grille.add(labelMoyenne, 1, 1);
        grille.add(champMoyenne, 2, 1);
        grille.add(labelEcart, 1, 2);
        grille.add(champEcart, 2, 2);

        dialogue.getDialogPane().setContent(grille);

        ButtonType boutonOK = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        ButtonType boutonAnnuler = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialogue.getDialogPane().getButtonTypes().addAll(boutonOK, boutonAnnuler);

        dialogue.setResultConverter(b -> {
            if (b == boutonOK) {
                return new Double[] { Double.parseDouble(champMoyenne.getText()),
                                      Double.parseDouble(champEcart.getText())
                };
            }
            return null;
        });

        Optional<Double[]> res = dialogue.showAndWait();
        res.ifPresent(val -> {
            try {
                double moyenne = val[0];
                double ecartType = val[1];
                monde.setParametresLoi(moyenne, ecartType);

            } catch (ParametresIncorrectsException e) {
                e.afficherAlerte();
            }
        });
    }

    /**
     * Crée un TextFormatter adapté pour la saisie de double.
     */
    private TextFormatter<Double> creerTextFormatterPourDouble(){

        Pattern modificationValide = Pattern.compile("(([1-9][0-9]*)|0)?(\\.[0-9]*)?");

        UnaryOperator<TextFormatter.Change> filtre = c -> {
            String text = c.getControlNewText();
            if (modificationValide.matcher(text).matches()) {
                return c ;
            } else {
                return null ;
            }
        };

        StringConverter<Double> converter = new StringConverter<Double>() {
            @Override
            public Double fromString(String s) {
                if (s.isEmpty() || ".".equals(s)) {
                    return 0.0 ;
                } else {
                    return Double.valueOf(s);
                }
            }

            @Override
            public String toString(Double d) {
                return d.toString();
            }
        };

        return new TextFormatter<Double>(converter, 0.0, filtre);
    }

    /**
     * Ouvre le sélecteur de fichier pour choisir le monde à charger
     */
    private void ouvrir(){

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Ouvrir un monde");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Fichiers Twisk", "*.twk"));
        File fichier = fileChooser.showOpenDialog(Stage.getWindows().get(0));
        try {
            if(fichier != null) {
                monde.ouvrir(fichier);
                MainTwisk.setTitreStage("Twisk - " + fichier.getName());
            }
        } catch (TwiskIOException e) {
            e.afficherAlerte();
        }
    }

    /**
     * Ouverture de l'explorateur de fichier permettant d'enregistrer le monde.
     */
    private void enregistrerSous(){

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Enregistrer sous ...");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Fichiers Twisk", "*.twk"));
        fileChooser.setInitialFileName("monde.twk");
        File fichier = fileChooser.showSaveDialog(Stage.getWindows().get(0));
        try {
            if(fichier != null) {
                monde.sauvegarder(fichier);
                MainTwisk.setTitreStage("Twisk - " + fichier.getName());
                afficherDialogueSauvegardeMondePreExistant(fichier.getName());
            }
        } catch (TwiskIOException e) {
            e.afficherAlerte();
        }
    }

    /**
     * Affiche une fanêtre de dialogue permettant d'ajouter le monde enregistré à la
     * liste des mondes préexistants
     */
    private void afficherDialogueSauvegardeMondePreExistant(String nomFichier) throws TwiskIOException {

        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setTitle("Ajouter aux mondes préexistants");
        a.setHeaderText(null);
        a.setContentText("Souhaitez-vous ajouter ce monde à la liste des mondes préexistants ?");
        Optional<ButtonType> res = a.showAndWait();
        if(res.isPresent() && res.get() == ButtonType.OK) {

            try {
                if (!Files.exists(Paths.get("./mondes"))) {
                    Files.createDirectory(Paths.get("./mondes"));
                }
                File fichier = new File("./mondes/" + nomFichier);

                fichier.createNewFile();
                monde.sauvegarder(fichier);
            } catch (IOException e) {
                throw new TwiskIOException("Erreur lors de la création du fichier.");
            }
        }
    }
}
