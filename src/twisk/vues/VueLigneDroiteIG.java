package twisk.vues;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polyline;
import twisk.mondeIG.LigneDroiteIG;
import twisk.mondeIG.MondeIG;
import twisk.mondeIG.PointDeControleIG;

public class VueLigneDroiteIG extends VueArcIG {

    private Line line;

    /**
     * Constructeur de la vue des LigneDroiteIG
     * @param monde
     * @param arc
     */
    public VueLigneDroiteIG(MondeIG monde, LigneDroiteIG arc){

        super(monde, arc);

        line = new Line(arc.getP1().getPosX(), arc.getP1().getPosY(),
                arc.getP2().getPosX(), arc.getP2().getPosY());

        line.getStyleClass().add("arc");
        mettreAJourTriangle(line.getStartX(), line.getStartY(), line.getEndX(), line.getEndY());

        line.startXProperty().addListener((ov, oldValue, newValue) -> mettreAJourTriangle(line.getStartX(), line.getStartY(), line.getEndX(), line.getEndY()));
        line.startYProperty().addListener((ov, oldValue, newValue) -> mettreAJourTriangle(line.getStartX(), line.getStartY(), line.getEndX(), line.getEndY()));
        line.endXProperty().addListener((ov, oldValue, newValue) -> mettreAJourTriangle(line.getStartX(), line.getStartY(), line.getEndX(), line.getEndY()));
        line.endYProperty().addListener((ov, oldValue, newValue) -> mettreAJourTriangle(line.getStartX(), line.getStartY(), line.getEndX(), line.getEndY()));

        this.getChildren().add(line);
    }

    /**
     * Modifie le visuel de la flèche lors d'un changement d'état (sélectionné ou non)
     */
    @Override
    public void reagir() {
        Runnable command = new Runnable() {
            @Override
            public void run() {
                if (monde.estSelectionne(arc)) {
                    line.getStyleClass().set(0, "arc-select");
                    triangle.getStyleClass().set(0, "tete-fleche-select");
                } else {
                    line.getStyleClass().set(0, "arc");
                    triangle.getStyleClass().set(0, "tete-fleche");
                }
            }
        };
        if(Platform.isFxApplicationThread())
            command.run();
        else
            Platform.runLater(command);
    }

    /**
     * Attache les coordonnées des extrémités de l'arc aux coordonnées des points de contrôle correspondants.
     * @param vuePoint vue du point de contrôle auquel se rattacher
     * @param point point de contrôle auquel se rattacher
     */
    @Override
    public void setBindingPointDeControle(VuePointDeControleIG vuePoint, PointDeControleIG point) {

        if(arc.getP1().equals(point)){
            line.startXProperty().bind(vuePoint.layoutXProperty().add(vuePoint.getLayoutBounds().getMinX() + vuePoint.radiusProperty().intValue()));
            line.startYProperty().bind(vuePoint.layoutYProperty().add(vuePoint.getLayoutBounds().getMinY() + vuePoint.radiusProperty().intValue()));
        }
        if (arc.getP2().equals(point)) {
            line.endXProperty().bind(vuePoint.layoutXProperty().add(vuePoint.getLayoutBounds().getMinX() + vuePoint.radiusProperty().intValue()));
            line.endYProperty().bind(vuePoint.layoutYProperty().add(vuePoint.getLayoutBounds().getMinY() + vuePoint.radiusProperty().intValue()));
        }
    }
}
