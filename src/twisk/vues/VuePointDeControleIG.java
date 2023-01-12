package twisk.vues;

import javafx.application.Platform;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.shape.Circle;
import twisk.exceptions.ArcNonValideException;
import twisk.mondeIG.MondeIG;
import twisk.mondeIG.PointDeControleIG;
import twisk.outils.TailleComposants;

public class VuePointDeControleIG extends Circle implements Observateur{

    private MondeIG monde;
    private PointDeControleIG point;


    /**
     * Constructeur de la vue des PointDeControleIG
     * @param monde
     * @param point
     */
    public VuePointDeControleIG(MondeIG monde, PointDeControleIG point, VueEtapeIG vueEtape){

        this.monde = monde;
        this.point = point;
        this.setCenterX(point.getPosX());
        this.setCenterY(point.getPosY());

        // Binding des coordonnées de la vue à celles de l'étape
        int offsetX = point.getPosX() - point.getEtape().getPosX();
        int offsetY = point.getPosY() - point.getEtape().getPosY();
        this.layoutXProperty().bind(vueEtape.layoutXProperty().add(offsetX - this.getLayoutBounds().getMinX()));
        this.layoutYProperty().bind(vueEtape.layoutYProperty().add(offsetY - this.getLayoutBounds().getMinY()));

        int taille = TailleComposants.getInstance().getTaillePointControle();
        this.setOnMouseClicked(new EcouteurPointDeControle(monde, point));
        this.setRadius(taille);
        this.getStyleClass().add("point-controle");

        setEcouteursDragAndDrop();

        monde.ajouterObservateur(this);
    }

    /**
     * Modifie le visuel du PointDeControleIG en fonction de son état(selectionné ou non)
     */
    @Override
    public void reagir() {
        Circle cercle = this;
        Runnable command = new Runnable() {
            @Override
            public void run() {
                cercle.setVisible(point.estVisible());
            }
        };

        if(Platform.isFxApplicationThread())
            command.run();
        else
            Platform.runLater(command);
    }

    /**
     * Initialise les écouteurs pour le drag & drop d'arcs
     */
    private void setEcouteursDragAndDrop(){

        this.setOnDragDetected(event -> {
            startFullDrag();
            try {
                monde.selectionnerPoint(point);
            } catch (ArcNonValideException e) {
                e.afficherAlerte();
            }
            event.consume();
        });

        this.setOnMouseDragReleased(mouseDragEvent -> {
            try {
                if(monde.arcEnCoursDeCreation())
                    monde.selectionnerPoint(point);
            } catch (ArcNonValideException e) {
                e.afficherAlerte();
            }
            mouseDragEvent.consume();
        });
    }
}
