package twisk.vues;

import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import twisk.mondeIG.EtapeIG;
import twisk.mondeIG.MondeIG;
import twisk.outils.RessourcesLoader;
import twisk.outils.TailleComposants;

public abstract class VueEtapeIG extends VBox implements Observateur{

    protected MondeIG monde;
    protected EtapeIG etape;
    protected HBox enTete;
    protected Label titre;
    protected ImageView entree, sortie;
    protected HBox zoneClients;

    protected Point2D pos;

    /**
     * Constructeur de la vue des EtapeIG
     * @param monde
     * @param etape
     */
    public VueEtapeIG(MondeIG monde, EtapeIG etape){
        this.monde = monde;
        this.etape = etape;

        this.enTete = new HBox();

        this.titre = new Label(etape.getNom());
        this.enTete.getStyleClass().add("titre-etape");

        this.entree = new ImageView(RessourcesLoader.getInstance().getImageEntree());
        this.sortie = new ImageView(RessourcesLoader.getInstance().getImageSortie());

        entree.setVisible(etape.estUneEntree());
        sortie.setVisible(etape.estUneSortie());

        enTete.getChildren().addAll(entree, titre, sortie);

        this.zoneClients = new HBox();
        this.zoneClients.setMinWidth(etape.getLargeur());
        this.zoneClients.setPrefHeight(etape.getHauteur() - enTete.getHeight() - 35);

        this.getChildren().addAll(enTete, zoneClients);

        this.relocate(etape.getPosX(), etape.getPosY());
        this.setMinSize(etape.getLargeur(), etape.getHauteur());

        if(!monde.enSimulation()) {
            this.setOnDragDetected(event -> etape.setEnDeplacement(true));
            this.setOnMouseClicked(event -> {
                monde.selectionnerEtape(etape);
            });
            this.setOnMousePressed(e -> {
                pos = new Point2D(e.getX(), e.getY());
            });
            this.setOnMouseDragged(e -> {
                if (etape.enDeplacement()) {
                    double x = this.getLayoutX() + e.getX() - pos.getX();
                    double y = this.getLayoutY() + e.getY() - pos.getY();
                    double hauteurFenetre = Stage.getWindows().get(0).getHeight();
                    double largeurFenetre = Stage.getWindows().get(0).getWidth();
                    double maxY = hauteurFenetre - TailleComposants.getInstance().getTailleBoutonOutils() * 2.5 - etape.getHauteur();
                    double maxX = largeurFenetre - etape.getLargeur();
                    if(y < 0) y = 0;
                    if(y > maxY) y = maxY;
                    if(x < 0) x = 0;
                    if(x > maxX) x = maxX;
                    this.relocate(x,y);
                }
            });
            this.setOnMouseReleased(e -> {
                if (etape.enDeplacement()) {
                    monde.deplacerEtape(etape.getId(), (int) this.getLayoutX(), (int) this.getLayoutY());
                }

            });
        }
        monde.ajouterObservateur(this);
    }

    /**
     * Modifie le visuel de l'étape si besoin
     */
    @Override
    public void reagir() {

    }
}
