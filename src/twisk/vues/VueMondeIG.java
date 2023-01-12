package twisk.vues;

import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import twisk.exceptions.ArcNonValideException;
import twisk.mondeIG.*;
import twisk.outils.RessourcesLoader;
import twisk.outils.TailleComposants;
import twisk.outils.ThreadsManager;
import twisk.simulation.Client;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class VueMondeIG extends Pane implements Observateur {

    private MondeIG monde;
    private Image imgEntree, imgSortie;

    /**
     * Constructeur de la vue du MondeIG
     * @param monde
     */
    public VueMondeIG(MondeIG monde){

        this.monde = monde;

        this.setOnMouseClicked(event -> {
            MouseButton button = event.getButton();
            if(button == MouseButton.PRIMARY){
                try {
                    monde.selectionnerPoint(new Point2D(event.getX(), event.getY()));
                } catch (ArcNonValideException e) {
                    e.afficherAlerte();
                }
            }else{
                monde.annulerSelectionPoints();
            }
        });
        this.getStyleClass().add("fond");
        this.monde.ajouterObservateur(this);
    }

    /**
     * Modifie le visuel du MondeIG en fonction du style sélectionné via le menu et actualise toutes les autres vues en les recréant
     */
    @Override
    public void reagir() {

        Pane panneau = this;

        Runnable command = new Runnable() {
            @Override
            public void run() {

                Image img = RessourcesLoader.getInstance().getFond(monde.getFond());
                Background fond = new Background(new BackgroundImage(img, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT,
                        BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT));
                panneau.setBackground(fond);
                panneau.getChildren().clear();

                afficherArcsEtapes();
                afficherClients();

                if(monde.arcEnCoursDeCreation()){
                    setCursor(Cursor.CROSSHAIR);
                }else{
                    setCursor(Cursor.DEFAULT);
                }
            }
        };

        if(Platform.isFxApplicationThread())
            command.run();
        else
            Platform.runLater(command);
    }

    /**
     * Affiche les arcs et les étapes
     */
    private void afficherArcsEtapes(){

        ArrayList<VueArcIG> vuesArcs = new ArrayList<>();
        Iterator<ArcIG> arcs = monde.iteratorArcs();
        while (arcs.hasNext()) {
            ArcIG a = arcs.next();
            if (a.estUneLigneDroite())
                this.getChildren().add(new VueLigneDroiteIG(monde, (LigneDroiteIG) a));
            else
                this.getChildren().add(new VueCourbeIG(monde, (CourbeIG) a));
            vuesArcs.add((VueArcIG)this.getChildren().get(this.getChildren().size()-1));
        }

        for (EtapeIG e : monde) {
            VueEtapeIG vueEtape = e.estUnGuichet() ? new VueGuichetIG(monde, (GuichetIG) e) : new VueActiviteIG(monde, (ActiviteIG) e);
            this.getChildren().add(vueEtape);
            if (!monde.enSimulation()) {
                for (PointDeControleIG p : e) {
                    VuePointDeControleIG vuePoint = new VuePointDeControleIG(monde, p, vueEtape);
                    this.getChildren().add(vuePoint);
                    for(VueArcIG arc : vuesArcs){
                        arc.setBindingPointDeControle(vuePoint, p);
                    }
                }
            }
        }
    }

    /**
     * Affiche les clients
     */
    private void afficherClients(){

        TailleComposants tailles = TailleComposants.getInstance();
        Random rand = new Random();

        Iterator<Client> iteClients = monde.iteratorClients();
        String[] couleurs = new String[] {
                "62dbc8", "7cd651", "d58558", "ffd14d", "ff8d48",
                "ff5757", "ff6ed4", "ad6fff", "4ebafd", "5882f8"
        };
        if (iteClients != null) {
            while (iteClients.hasNext()) {
                Client c = iteClients.next();
                EtapeIG etape = monde.getEtape(c.getEtape());
                if(etape != null) {
                    Circle client = new Circle();
                    int rayonClient = TailleComposants.getInstance().getTailleClients();
                    client.setStyle("-fx-fill: #" + couleurs[c.getNumero() % couleurs.length]);
                    client.setRadius(rayonClient);

                    if(etape.estUneActivite()) {
                        int randX = rand.nextInt(tailles.getLargeurActivite() - rayonClient * 2 - 20);
                        int randY = rand.nextInt(tailles.getHauteurActivite() - rayonClient * 2 - 45);
                        client.setCenterX(etape.getPosX() + randX + 10 + rayonClient);
                        client.setCenterY(etape.getPosY() + randY + 35 + rayonClient);
                        this.getChildren().add(client);
                    }else if (c.getRang() < 8){
                        GuichetIG g = (GuichetIG)etape;
                        int taille = tailles.getTailleEmplacementClient();
                        client.setCenterX(etape.getPosX() + (taille / 2 + 10) + taille * (7 - c.getRang()));
                        if(g.getSensFile() == SensFile.GAUCHE){
                            client.setCenterX(etape.getPosX() + (taille / 2 + 10) + taille * c.getRang());
                        }
                        client.setCenterY(etape.getPosY() + taille / 2 + 31);
                        this.getChildren().add(client);
                    }
                }
            }
        }
    }
}
